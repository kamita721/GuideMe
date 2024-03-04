package org.guideme.guideme;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.generated.model.Audio;
import org.guideme.generated.model.Button;
import org.guideme.generated.model.Delay;
import org.guideme.generated.model.GlobalButton;
import org.guideme.generated.model.IText;
import org.guideme.generated.model.Image;
import org.guideme.generated.model.LoadGuide;
import org.guideme.generated.model.Metronome;
import org.guideme.generated.model.Page;
import org.guideme.generated.model.Text;
import org.guideme.generated.model.Timer;
import org.guideme.generated.model.Video;
import org.guideme.generated.model.Webcam;
import org.guideme.guideme.model.*;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
import org.guideme.guideme.scripting.OverRide;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.AppSettings;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.settings.UserSettings;
import org.guideme.guideme.ui.debug_shell.DebugShell;
import org.guideme.guideme.ui.main_shell.MainShell;
import org.guideme.guideme.util.PageFilter;
import org.guideme.guideme.util.XMLReaderUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MainLogic {
	private static Logger logger = LogManager.getLogger();
	private static MainLogic mainLogic;
	private static ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private OverRide overRide = new OverRide();
	private static Clip song; // Sound player
	private static HashMap<String, Object> globalScriptVariables = new HashMap<>(); // variables
																					// used
																					// by
																					// javascript
	private static final String MEDIA_PATH_PLACEHOLDER = "\\MediaDir\\";
	private static String filename; // name of file to store persistent state
	private final PageFilter pageFilter = new PageFilter();

	public static Map<String, Object> getGlobalScriptVariables() {
		return globalScriptVariables;
	}

	// singleton class stuff (force there to be only one instance without making
	// it static)
	private MainLogic() {
	}

	public static synchronized MainLogic getMainLogic() {
		URL songPath;
		if (mainLogic == null) {
			mainLogic = new MainLogic();
			songPath = MainLogic.class.getResource("/tick.wav");
			logger.info("MainLogic getMainLogic songPath {}", songPath);
			AudioInputStream audioIn;
			try {
				audioIn = AudioSystem.getAudioInputStream(songPath);
				AudioFormat format = audioIn.getFormat();
				song = AudioSystem.getClip();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				song = (Clip) AudioSystem.getLine(info);
				song.open(audioIn);
				loadGlobalScriptVariables();
			} catch (IllegalArgumentException | UnsupportedAudioFileException | IOException
					| LineUnavailableException | NullPointerException e) {
				logger.error("audio clip Exception ", e);
			}
		}
		return mainLogic;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	// display page without a chapter
	public void displayPage(String pageId, boolean reDisplay, Guide guide, MainShell mainShell,
			AppSettings appSettings, UserSettings userSettings, GuideSettings guideSettings,
			DebugShell debugShell) {
		displayPage("default", pageId, reDisplay, guide, mainShell, appSettings, userSettings,
				guideSettings, debugShell);
	}

	// main display page
	// TODO currently chapters are ignored, need to implement
	public void displayPage(String chapterName, String pageId, boolean reDisplay, Guide guide,
			MainShell mainShell, AppSettings appSettings, UserSettings userSettings,
			GuideSettings guideSettings, DebugShell debugShell) {
		// Main code that displays a page
		String strPageId;
		boolean blnMetronome;
		String strFlags;
		Page objCurrPage;
		Delay objDelay;

		logger.debug("displayPage PagePassed {}", pageId);
		logger.debug(() -> "displayPage Flags " + comonFunctions.getFlags(guide.getFlags()));

		mainShell.stopAll(false);
		overRide.clear();
		guideSettings.setChapter(chapterName);
		// handle random page
		strPageId = pageFilter.getSingleMatchingPage(pageId, guide, chapterName);
		// get the page to display
		objCurrPage = guide.getChapters().get(chapterName).getPages().get(strPageId);
		if (objCurrPage == null) {
			objCurrPage = generate404Page(guide, chapterName, strPageId);
			strPageId = objCurrPage.getId();
		}
		guideSettings.setPrevPage(guideSettings.getCurrPage());
		guideSettings.setCurrPage(strPageId);

		// load new guide?
		objCurrPage = processLoadGuide(objCurrPage, guide, debugShell, appSettings, mainShell);
		strPageId = objCurrPage.getId();

		debugShell.setPage(strPageId, true);

		mainShell.runJscript("pageLoad()", overRide, true);

		if (!overRide.getPage().isBlank()) {
			displayPage(chapterName, overRide.getPage(), reDisplay, guide, mainShell, appSettings,
					userSettings, guideSettings, debugShell);
			return;
		}

		// PageChangeClick
		if (appSettings.isPageSound() && guideSettings.isPageSound()) {
			song.setFramePosition(0);
			song.start();
		}

		// delay
		objDelay = getDelay(guide, objCurrPage);

		// If we are going straight to another page we can skip this section
		if (objDelay == null || objDelay.getDelaySec() > 0) {
			renderPage(mainShell, objCurrPage, guide, appSettings, userSettings, objDelay,
					debugShell);
		}

		if (!reDisplay) {
			// Audio / Metronome
			blnMetronome = processMetronome(objCurrPage, guide, mainShell);
			if (!blnMetronome) {
				// Audio
				Audio audio = getAudio(objCurrPage, guide);
				mainShell.playAudioUncooked(audio);

				// Audio2
				audio = getAudio2(objCurrPage, guide);
				mainShell.playAudio2Uncooked(audio);
			}
		}

		// Save current page and flags
		// set page
		if (guide.getAutoSetPage()) {
			comonFunctions.setFlags(strPageId, guide.getFlags());
		}

		// do page set / unset
		objCurrPage.setUnSet(guide.getFlags());

		// Start all media at the same time
		mainShell.startDeferredMedia();

		guide.getSettings().setPage(strPageId);
		strFlags = comonFunctions.getFlags(guide.getFlags());
		logger.debug("displayPage End Flags {}", strFlags);
		guide.getSettings().setFlags(strFlags);
		guide.getSettings().saveSettings();
		appSettings.saveSettings();
		guide.getSettings().formFieldsReset();
		mainShell.layoutButtons();

	}

	/**
	 * Process all of the UI elements of a new page. This section can be safely
	 * skipped for 'intermediate' pages that immidietly forward the user to another
	 * page.
	 * 
	 * @param mainShell
	 * @param objCurrPage
	 * @param guide
	 * @param appSettings
	 * @param userSettings
	 * @param objDelay
	 * @param debugShell
	 */
	private void renderPage(MainShell mainShell, Page objCurrPage, Guide guide,
			AppSettings appSettings, UserSettings userSettings, Delay objDelay,
			DebugShell debugShell) {
		addTimers(mainShell, objCurrPage, guide);
		Video objVideo;
		Webcam objWebcam;
		String imgPath = null;
		// Video
		objVideo = getVideo(objCurrPage, guide);
		objWebcam = getWebcam(objCurrPage, guide);
		imgPath = getImage(objCurrPage, guide, mainShell);
		String imgName = "";

		if (!overRide.getLeftBody().isBlank()) {
			mainShell.setLeftPaneTextUncooked(overRide.getLeftBody(), overRide.getLeftCss());
		} else if (!overRide.getLeftHtml().isBlank()) {
			mainShell.setLeftPaneHtmlUncooked(overRide.getLeftHtml());
		}
		if (objVideo != null) {
			mainShell.playVideoUncooked(objVideo);
		} else if (objWebcam != null) {
			mainShell.showWebcam();
		} else if (imgPath != null) {
			mainShell.setImage(imgPath);
			imgName = new File(imgPath).getName();
			if (imgName.equals("")) {
				processLeftText(objCurrPage, appSettings, guide, mainShell, userSettings);
			}
		} else {
			// TODO guard on getWebCamFound() ?
			mainShell.clearImage();
		}

		// Browser text
		processRightPanel(objCurrPage, appSettings, guide, mainShell, userSettings);

		// Buttons
		processButtons(objCurrPage, appSettings, guide, mainShell, objDelay, debugShell);

		// TODO why clear lblRight?
		mainShell.setLblRight("");

		if (objDelay == null) {
			mainShell.setLblLeft("");
		} else {
			Calendar calCountDown = guide.setDelay(objDelay);
			mainShell.setCalCountDown(calCountDown);
		}

		if (appSettings.getDebug()) {
			mainShell.setLblCentre(" " + objCurrPage.getId() + " / " + imgName);
		} else {
			mainShell.setLblCentre(guide.getTitle());
		}
	}

	private Page generate404Page(Guide guide, String chapterName, String strPageId) {
		Page objCurrPage = guide.getChapters().get(chapterName).getPages().get("GuideMe404Error");
		String strText = "<div><p><h1>Oops it looks like page " + strPageId
				+ " does not exist</h1></p>";
		strText = strText + "<p>Please contact the Author to let them know the issue</p></div>";
		Text toAdd = new Text();
		toAdd.setText(strText);
		objCurrPage.addText(toAdd);
		return objCurrPage;
	}

	private Delay getDelay(Guide guide, Page objCurrPage) {

		Delay objDelay = overRide.getDelay();
		if (objDelay != null) {
			return objDelay;
		}

		if (objCurrPage.getDelayCount() > 0) {
			for (Delay d : objCurrPage.getDelays()) {
				if (d.canShow(guide.getFlags())) {
					return d;
				}
			}
		}

		return null;

	}

	private void addTimers(MainShell mainShell, Page objCurrPage, Guide guide) {
		for (Timer objTimer : overRide.getTimers()) {
			Calendar timCountDown = Calendar.getInstance();
			timCountDown.add(Calendar.SECOND, objTimer.getTimerSec());
			objTimer.setTimerEnd(timCountDown);
			mainShell.addTimer(objTimer);
		}
		for (Timer objTimer : objCurrPage.getTimers()) {
			if (objTimer.canShow(guide.getFlags())) {
				Calendar timCountDown = Calendar.getInstance();
				timCountDown.add(Calendar.SECOND, objTimer.getTimerSec());
				objTimer.setTimerEnd(timCountDown);
				mainShell.addTimer(objTimer);
			}
		}

	}

	private Video getVideo(Page objCurrPage, Guide guide) {
		Video objVideo = overRide.getVideo();
		if (objVideo != null) {
			return objVideo;
		}

		for (Video video : objCurrPage.getVideos()) {
			if (video.canShow(guide.getFlags())) {
				return video;
			}
		}

		return null;

	}

	private Webcam getWebcam(Page objCurrPage, Guide guide) {
		Webcam objWebcam = overRide.getWebcam();
		if (objWebcam != null) {
			return objWebcam;
		}

		for (Webcam webcam : objCurrPage.getWebcams()) {
			if (webcam.canShow(guide.getFlags())) {
				return webcam;
			}
		}

		return null;

	}

	private String getImage(Page objCurrPage, Guide guide, MainShell mainShell) {
		// if there is an image over ride from javascript use it
		String strImage = overRide.getImage();
		strImage = mainShell.cookImage(strImage);
		if (strImage != null) {
			return strImage;
		}

		for (Image img : objCurrPage.getImages()) {
			if (img.canShow(guide.getFlags())) {
				strImage = mainShell.cookImage(img.getId());
				if (strImage != null) {
					return strImage;
				}
			}
		}

		return null;
	}

	private void processLeftText(Page objCurrPage, AppSettings appSettings, Guide guide,
			MainShell mainShell, UserSettings userSettings) {
		// Replace any string pref in the HTML with the user preference
		// they are encoded #prefName#
		try {
			StringBuilder displayTextBuilder = new StringBuilder();
			for (IText objText : objCurrPage.getTexts()) {
				if (objText.canShow(guide.getFlags())) {
					displayTextBuilder.append(objText.getText());
				}

			}
			String displayText = displayTextBuilder.toString();

			// Media Directory
			String mediaPath;
			mediaPath = comonFunctions.getMediaFullPath("", appSettings.getFileSeparator(),
					appSettings, guide);
			displayText = displayText.replace(MEDIA_PATH_PLACEHOLDER, mediaPath);

			displayText = comonFunctions.substituteTextVars(displayText, guide.getSettings(),
					userSettings);

			mainShell.setLeftPaneText(displayText, overRide.getRightCss());
		} catch (Exception e) {
			logger.error("displayPage BrwsText Exception " + e.getLocalizedMessage(), e);
			mainShell.setLeftPaneText("", "");
		}

	}

	private void processRightPanel(Page objCurrPage, AppSettings appSettings, Guide guide,
			MainShell mainShell, UserSettings userSettings) {
		GuideSettings guideSettings = guide.getSettings();

		// Replace any string pref in the HTML with the user preference
		// they are encoded #prefName#
		try {
			String displayText = "";
			if (overRide.getHtml().equals("") && overRide.getRightHtml().equals("")) {
				StringBuilder displayTextBuilder = new StringBuilder();
				for (IText objText : objCurrPage.getTexts()) {
					if (objText.canShow(guide.getFlags())) {
						displayTextBuilder.append(objText.getText());
					}
				}
				displayText = displayTextBuilder.toString();
			} else {
				if (!overRide.getHtml().equals("")) {
					displayText = overRide.getHtml();
					overRide.setHtml("");
				}
			}

			// Media Directory
			String mediaPath;
			mediaPath = comonFunctions.getMediaFullPath("", appSettings.getFileSeparator(),
					appSettings, guide);
			displayText = displayText.replace("\\MediaDir\\", mediaPath);

			if (overRide.getRightHtml().equals("")) {
				displayText = comonFunctions.substituteTextVars(displayText, guideSettings,
						userSettings);
				mainShell.setBrwsText(displayText, overRide.getRightCss());
			} else {
				mainShell.setRightHtml(comonFunctions.substituteTextVars(overRide.getRightHtml(),
						guideSettings, userSettings));
			}
		} catch (Exception e) {
			logger.error("displayPage BrwsText Exception " + e.getLocalizedMessage(), e);
			mainShell.setBrwsText("", "");
		}

	}

	private void processButtons(Page objCurrPage, AppSettings appSettings, Guide guide,
			MainShell mainShell, Delay objDelay, DebugShell debugShell) {
		List<Button> button = new ArrayList<>();

		for (GlobalButton b : overRide.getGlobalButtons()) {
			objCurrPage.addGlobalButton(b);
		}
		overRide.clearGlobalButtons();

		// process global buttons on page
		for (GlobalButton b : objCurrPage.getGlobalButtons()) {
			switch (b.getAction()) {
			case ADD:
				guide.getSettings().addGlobalButton(b.getId(), b);
				break;
			case REMOVE:
				guide.getSettings().removeGlobalButton(b.getId());
				break;
			default:
				logger.error("displayPage Global Button invalid action " + b.getAction());
			}
		}

		// remove old buttons
		mainShell.removeButtons();

		Collections.addAll(button, guide.getSettings().getGlobalButtons());
		Collections.addAll(button, overRide.getButtons());
		Collections.addAll(button, overRide.getWebcamButtons());

		button.removeIf(btn -> !btn.canShow(guide.getFlags()));
		debugShell.addOverrideButtons(button);

		button.addAll(objCurrPage.getButtons());
		button.addAll(objCurrPage.getWebcamButtons());

		button.removeIf(btn -> !btn.canShow(guide.getFlags()));

		Collections.sort(button, new ButtonSorter());

		button.forEach(mainShell::addButtonUncooked);

		if (appSettings.getDebug() && objDelay != null) {
			mainShell.addDelayButton(guide);
		}

	}

	private boolean processMetronome(Page objCurrPage, Guide guide, MainShell mainShell) {
		Metronome objMetronome = overRide.getMetronome();

		for (Metronome metronome : objCurrPage.getMetronomes()) {
			if (metronome.canShow(guide.getFlags())) {
				objMetronome = metronome;
				break;
			}
		}

		if (objMetronome != null) {
			// Metronome
			int intbpm = objMetronome.getbpm();
			logger.debug("displayPage Metronome {} BPM", intbpm);

			mainShell.setMetronomeBPM(objMetronome.getbpm(), objMetronome.getLoops(),
					objMetronome.getResolution(), objMetronome.getRhythm());

		}
		return objMetronome != null;
	}

	private Audio getAudio(Page objCurrPage, Guide guide) {
		Audio objAudio = overRide.getAudio();

		if (objAudio != null) {
			return objAudio;
		}
		for (Audio aud : objCurrPage.getAudios()) {
			if (aud.canShow(guide.getFlags())) {
				return aud;
			}
		}
		return null;

	}

	private Audio getAudio2(Page objCurrPage, Guide guide) {
		Audio objAudio = overRide.getAudio2();
		if (objAudio != null) {
			return objAudio;
		} else {
			for (Audio audio : objCurrPage.getAudio2s()) {
				if (audio.canShow(guide.getFlags())) {
					return audio;
				}
			}
		}
		return null;

	}

	private Page processLoadGuide(Page objCurrPage, Guide guide, DebugShell debugShell,
			AppSettings appSettings, MainShell mainShell) {
		LoadGuide objLoadGuide = null;
		String strPageId = "";
		String pageJavascript = objCurrPage.getJScript();
		for (LoadGuide lg : objCurrPage.getLoadGuides()) {
			if (lg.canShow(guide.getFlags())) {
				objLoadGuide = lg;
				break;
			}
		}

		if (objLoadGuide == null) {
			return objCurrPage;
		}

		String returnTarget = objLoadGuide.getReturnTarget();
		if (returnTarget.equals("")) {
			returnTarget = "start";
		}
		guide.getSettings().setPage(returnTarget);

		mainShell.runJscript(objLoadGuide.getPreScript(), false);

		guide.getSettings().saveSettings();
		guide.getSettings().formFieldsReset();
		debugShell.clearPagesCombo();

		try {
			XmlGuideReader.loadXML((appSettings.getDataDirectory() + objLoadGuide.getGuidePath()),
					guide, appSettings, debugShell);
		} catch (XMLStreamException | IOException e) {
			logger.error("Error loading new guide {}", objLoadGuide.getGuidePath(), e);
			// TODO, this should go to an error page.
			return objCurrPage;
		}
		GuideSettings guideSettings = guide.getSettings();

		mainShell.runJscript(objLoadGuide.getPostScript(), pageJavascript);

		guide.getSettings().saveSettings();

		String strTarget = objLoadGuide.getTarget();
		String chapterName = guideSettings.getChapter();
		if (chapterName.equals("")) {
			chapterName = "default";
			guideSettings.setChapter(chapterName);
		}

		if (!strTarget.equals("")) {
			strPageId = pageFilter.getSingleMatchingPage(strTarget, guide, chapterName);
		} else {
			strPageId = guideSettings.getCurrPage();
		}
		// get the page to display
		objCurrPage = guide.getChapters().get(chapterName).getPages().get(strPageId);
		if (objCurrPage == null) {
			objCurrPage = generate404Page(guide, chapterName, strPageId);
			strPageId = objCurrPage.getId();
		}
		guideSettings.setPrevPage(strPageId);
		guideSettings.setCurrPage(strPageId);
		guideSettings.setPage(strPageId);

		mainShell.setGuideSettings(guideSettings);
		if (guide.getCss().equals("")) {
			mainShell.setDefaultStyle();
		} else {
			mainShell.setStyle(guide.getCss());
		}

		return objCurrPage;
	}

	private static void loadGlobalScriptVariables() {
		// globalScriptVariables
		String dataDirectory;
		AppSettings appSettings = AppSettings.getAppSettings();
		ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
		if (appSettings.isStateInDataDir()) {
			dataDirectory = appSettings.getTempDir();
			filename = dataDirectory + "Global.state";
		} else {
			dataDirectory = appSettings.getDataDirectory();
			String prefix = "";
			if (dataDirectory.startsWith("/")) {
				prefix = "/";
			}
			dataDirectory = prefix
					+ comonFunctions.fixSeparator(dataDirectory, appSettings.getFileSeparator());
			filename = dataDirectory + appSettings.getFileSeparator() + "Global.state";
		}
		File xmlFile = new File(filename);
		if (!xmlFile.exists()) {
			return;
		}

		DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
		Document doc;
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(xmlFile);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.warn("Error reading in global state {}", filename, e);
			return;
		}
		Element rootElement = doc.getDocumentElement();
		rootElement.normalize();

		Element elScriptVariables = comonFunctions.getElement("//scriptVariables", rootElement);

		if (elScriptVariables == null) {
			return;
		}

		ContextFactory cntxFact = new ContextFactory();
		try (Context context = cntxFact.enterContext()) {
			context.setOptimizationLevel(-1);
			context.getWrapFactory().setJavaPrimitiveWrap(false);
			ScriptableObject scope = context.initStandardObjects();

			NodeList nodeList = elScriptVariables.getElementsByTagName("Var");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node currentNode = nodeList.item(i);
				if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
					Element elVar = (Element) currentNode;
					String strName = elVar.getAttribute("id");
					String strType = elVar.getAttribute("type");
					Object objValue = null;
					CharacterData elChar = (CharacterData) elVar.getFirstChild();
					if (elChar != null) {
						objValue = comonFunctions.getSavedObject(elChar.getData(), strType, scope);
					}
					globalScriptVariables.put(strName, objValue);
				}
			}

		}

	}

	public static void saveGlobalScriptVariables() {
		try {
			ContextFactory cntxFact = new ContextFactory();

			Context context = cntxFact.enterContext();
			context.setOptimizationLevel(-1);
			context.getWrapFactory().setJavaPrimitiveWrap(false);
			ScriptableObject scope = context.initStandardObjects();

			File xmlFile = new File(filename);
			logger.trace("MainLogic saveSettings filename: {}", filename);
			Element rootElement;
			Document doc;

			// if the file exists then use the current one, otherwise create a
			// new one.
			// if nodes do not exist it will add them
			if (xmlFile.exists()) {
				logger.trace("MainLogic saveSettings file exists ");
				DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				doc = docBuilder.parse(xmlFile);
				rootElement = doc.getDocumentElement();
				rootElement.normalize();
			} else {
				logger.trace("MainLogic saveSettings does not file exist ");
				DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				doc = docBuilder.newDocument();
				rootElement = doc.createElement("SETTINGS");
				doc.appendChild(rootElement);
			}

			Element elScriptVariables = comonFunctions.getElement("//scriptVariables", rootElement);
			if (elScriptVariables != null) {
				rootElement.removeChild(elScriptVariables);
			}
			elScriptVariables = comonFunctions.addElement("scriptVariables", rootElement, doc);

			Iterator<String> it = globalScriptVariables.keySet().iterator();
			Element elVar;
			while (it.hasNext()) {
				String key = it.next();
				Object value = globalScriptVariables.get(key);
				String strType;
				String strValue;
				if (value == null) {
					strType = "Null";
					strValue = "";
				} else {
					strType = value.getClass().getName();
					strValue = comonFunctions.createSaveObject(value, strType, scope);
				}
				if (!strValue.equals("ignore")) {
					elVar = comonFunctions.addElement("Var", elScriptVariables, doc);
					elVar.setAttribute("id", key);
					comonFunctions.addCdata(strValue, elVar, doc);
					elVar.setAttribute("type", strType);
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = XMLReaderUtils.getTransformFactory();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			logger.trace("MainLogic saveSettings save file: {}", filename);
			StreamResult result = new StreamResult(new File(filename));
			transformer.transform(source, result);

		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(), ex);
		} finally {
			Context.exit();
		}
	}

}
