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
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
		Video objVideo;
		Webcam objWebcam;
		String fileSeparator = appSettings.getFileSeparator();
		String imgName = "";

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

		// PageChangeClick
		if (appSettings.isPageSound() && guideSettings.isPageSound()) {
			song.setFramePosition(0);
			song.start();
		}

		// delay
		objDelay = processDelay(mainShell, guide, objCurrPage);

		// If we are going straight to another page we can skip this section
		if ((objDelay == null || objDelay.getDelaySec() > 0) && overRide.getPage().equals("")) {
			// add timers
			addTimers(mainShell, objCurrPage, guide);

			// If we haven't over ridden the left pane then populate it
			if (overRide.getLeftHtml().equals("") && overRide.getLeftBody().equals("")) {
				// Video
				objVideo = processVideo(objCurrPage, guide, fileSeparator, appSettings, mainShell);
				if (objVideo == null) {
					objWebcam = processWebcam(objCurrPage, guide, fileSeparator, appSettings,
							mainShell);
				} else {
					objWebcam = null;
				}
				if (objVideo == null && objWebcam == null) {
					// image
					imgName = ProcessImage(objCurrPage, fileSeparator, appSettings, guide,
							mainShell);
					if (imgName.equals("")) {
						// Left text
						processLeftText(objCurrPage, fileSeparator, appSettings, guide, mainShell,
								userSettings);
					}
				} else {
					if (objWebcam == null || objWebcam.getWebCamFound()) {
						mainShell.clearImage();
						// No image
					}
				}

			} else {
				processLeftTextOverRide(fileSeparator, appSettings, guide, mainShell, userSettings);
			}

			// Browser text
			processRightPanel(objCurrPage, fileSeparator, appSettings, guide, mainShell,
					userSettings);

			// Buttons
			processButtons(objCurrPage, appSettings, guide, mainShell, userSettings, objDelay,
					imgName, debugShell);

		}

		if (!reDisplay) {
			// Audio / Metronome
			blnMetronome = processMetronome(objCurrPage, guide, mainShell);
			if (!blnMetronome) {
				// Audio
				processAudio(objCurrPage, guide, mainShell, fileSeparator, appSettings);
				// Audio2
				processAudio2(objCurrPage, guide, mainShell, fileSeparator, appSettings);
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

	private Page generate404Page(Guide guide, String chapterName, String strPageId) {
		Page objCurrPage = guide.getChapters().get(chapterName).getPages().get("GuideMe404Error");
		String strText = "<div><p><h1>Oops it looks like page " + strPageId
				+ " does not exist</h1></p>";
		strText = strText + "<p>Please contact the Author to let them know the issue</p></div>";
		objCurrPage.addText(new Text(strText));
		return objCurrPage;
	}

	private Delay processDelay(MainShell mainShell, Guide guide, Page objCurrPage) {
		Delay objDelay = null;
		mainShell.setLblRight("");
		boolean blnDelay = false;
		int intDelSeconds = 1;
		// override page
		try {
			if (!overRide.getPage().equals("")) {
				intDelSeconds = 0;
				guide.setDelStyle("hidden");
				guide.setDelTarget(overRide.getPage());
				guide.setDelayjScript("");
				guide.setDelStartAtOffSet(0);
				guide.setDelaySet("");
				guide.setDelayUnSet("");
				Calendar calCountDown = Calendar.getInstance();
				calCountDown.add(Calendar.SECOND, intDelSeconds);
				mainShell.setCalCountDown(calCountDown);
			} else {
				objDelay = overRide.getDelay();
				if (objDelay != null) {
					blnDelay = true;
				} else {
					if (objCurrPage.getDelayCount() > 0) {
						for (int i2 = 0; i2 < objCurrPage.getDelayCount(); i2++) {
							objDelay = objCurrPage.getDelay(i2);
							if (objDelay.canShow(guide.getFlags())) {
								blnDelay = true;
								break;
							}
						}
					}
				}
				if (blnDelay) {
					logger.debug("displayPage Delay");
					guide.setDelStyle(objDelay.getstyle());
					guide.setDelTarget(objDelay.getTarget());
					guide.setDelayjScript(objDelay.getjScript());
					guide.setDelayScriptVar(objDelay.getScriptVar());
					intDelSeconds = objDelay.getDelaySec();
					guide.setDelStartAtOffSet(objDelay.getStartWith());
					guide.setDelStartAtOffSet(guide.getDelStartAtOffSet() - intDelSeconds);

					// record any delay set / unset
					guide.setDelaySet(objDelay.getSet());
					guide.setDelayUnSet(objDelay.getUnSet());
					logger.debug("displayPage Delay Seconds {} Style {} Target {} Set {} UnSet {}",
							intDelSeconds, guide.getDelStyle(), guide.getDelTarget(),
							guide.getDelaySet(), guide.getDelayUnSet());
					Calendar calCountDown = Calendar.getInstance();
					calCountDown.add(Calendar.SECOND, intDelSeconds);
					mainShell.setCalCountDown(calCountDown);
				} else {
					mainShell.setLblLeft("");
				}
			}
		} catch (Exception e1) {
			logger.error("displayPage Delay Exception " + e1.getLocalizedMessage(), e1);
			mainShell.setLblLeft("");
		}
		if (blnDelay) {
			return objDelay;
		} else {
			return null;
		}

	}

	private void addTimers(MainShell mainShell, Page objCurrPage, Guide guide) {
		Timer objTimer;
		if (overRide.timerCount() > 0) {
			for (int i2 = 0; i2 < overRide.timerCount(); i2++) {
				objTimer = overRide.getTimer(i2);
				Calendar timCountDown = Calendar.getInstance();
				timCountDown.add(Calendar.SECOND, objTimer.getTimerSec());
				objTimer.setTimerEnd(timCountDown);
				mainShell.addTimer(objTimer);
			}
		}
		if (objCurrPage.getTimerCount() > 0) {
			for (int i2 = 0; i2 < objCurrPage.getTimerCount(); i2++) {
				objTimer = objCurrPage.getTimer(i2);
				if (objTimer.canShow(guide.getFlags())) {
					Calendar timCountDown = Calendar.getInstance();
					timCountDown.add(Calendar.SECOND, objTimer.getTimerSec());
					objTimer.setTimerEnd(timCountDown);
					mainShell.addTimer(objTimer);
				}
			}
		}

	}

	private Video processVideo(Page objCurrPage, Guide guide, String fileSeparator,
			AppSettings appSettings, MainShell mainShell) {
		Video objVideo;
		boolean blnVideo = false;
		objVideo = overRide.getVideo();
		try {
			if (objVideo != null) {
				blnVideo = true;
			} else {
				if (objCurrPage.getVideoCount() > 0) {
					for (int i2 = 0; i2 < objCurrPage.getVideoCount(); i2++) {
						objVideo = objCurrPage.getVideo(i2);
						if (objVideo.canShow(guide.getFlags())) {
							blnVideo = true;
							break;
						}
					}
				}
			}
			if (blnVideo) {
				String strVideo = objVideo.getId();
				logger.trace("displayPage Video {}", strVideo);
				String strStartAt = objVideo.getStartAt();
				logger.trace("displayPage Video Start At {}", strStartAt);
				int intStartAt = 0;
				if (!strStartAt.isBlank()) {
					intStartAt = comonFunctions.getMilisecFromTime(strStartAt) / 1000;
				}

				String strStopAt = objVideo.getStopAt();
				logger.trace("displayPage Video Stop At {}", strStopAt);
				int intStopAt = 0;
				if (!strStopAt.isBlank()) {
					intStopAt = comonFunctions.getMilisecFromTime(strStopAt) / 1000;
				}

				String imgPath = comonFunctions.getMediaFullPath(strVideo, fileSeparator,
						appSettings, guide);

				int repeat = objVideo.getRepeat();

				// Play video
				mainShell.playVideo(imgPath, intStartAt, intStopAt, repeat, objVideo.getTarget(),
						objVideo.getJscript(), objVideo.getScriptVar(), objVideo.getVolume(), true);
			}
		} catch (Exception e1) {
			logger.trace("displayPage Video Exception " + e1.getLocalizedMessage());
		}

		if (blnVideo) {
			return objVideo;
		} else {
			return null;
		}

	}

	private Webcam processWebcam(Page objCurrPage, Guide guide, String fileSeparator,
			AppSettings appSettings, MainShell mainShell) {
		Webcam objWebcam;
		boolean blnWebcam = false;
		objWebcam = overRide.getWebcam();
		try {
			if (objWebcam != null) {
				blnWebcam = true;
			} else {
				if (objCurrPage.getWebcamCount() > 0) {
					for (int i2 = 0; i2 < objCurrPage.getWebcamCount(); i2++) {
						objWebcam = objCurrPage.getWebcam(i2);
						if (objWebcam.canShow(guide.getFlags())) {
							blnWebcam = true;
							break;
						}
					}
				}
			}
			if (blnWebcam) {
				mainShell.showWebcam();
			}
		} catch (Exception e1) {
			logger.trace("displayPage Webcam Exception " + e1.getLocalizedMessage());
		}

		if (blnWebcam) {
			return objWebcam;
		} else {
			return null;
		}

	}

	private String ProcessImage(Page objCurrPage, String fileSeparator, AppSettings appSettings,
			Guide guide, MainShell mainShell) {
		// if there is an image over ride from javascript use it
		boolean blnImage = false;
		String strImage = overRide.getImage();
		String imgPath = "";
		String imgName = "";
		Image objImage;
		if (!strImage.equals("")) {
			File flImage = new File(strImage);
			if (flImage.exists()) {
				imgPath = strImage;
				blnImage = true;
			}
			if (!blnImage) {
				imgPath = comonFunctions.getMediaFullPath(strImage, fileSeparator, appSettings,
						guide);
				flImage = new File(imgPath);
				if (flImage.exists()) {
					blnImage = true;
				}
			}
		}
		if (!blnImage && (objCurrPage.getImageCount() > 0)) {
			for (int i2 = 0; i2 < objCurrPage.getImageCount(); i2++) {
				objImage = objCurrPage.getImage(i2);
				if (objImage.canShow(guide.getFlags())) {
					strImage = objImage.getId();
					File flImage = new File(strImage);
					if (flImage.exists()) {
						imgPath = strImage;
						blnImage = true;
					}
					if (!blnImage) {
						imgPath = comonFunctions.getMediaFullPath(strImage, fileSeparator,
								appSettings, guide);
						flImage = new File(imgPath);
						if (flImage.exists()) {
							blnImage = true;
							break;
						}
					}
				}
			}

		}
		if (blnImage) {
			try {
				mainShell.setImage(imgPath);
			} catch (Exception e1) {
				logger.error("displayPage Image Exception " + e1.getLocalizedMessage(), e1);
				mainShell.clearImage();
			}
		}
		imgName = new File(imgPath).getName();
		if (blnImage) {
			return imgName;
		} else {
			return "";
		}
	}

	private void processLeftText(Page objCurrPage, String fileSeparator, AppSettings appSettings,
			Guide guide, MainShell mainShell, UserSettings userSettings) {
		// Replace any string pref in the HTML with the user preference
		// they are encoded #prefName#
		try {
			StringBuilder displayTextBuilder = new StringBuilder();
			for (int i = 0; i < objCurrPage.getLeftTextCount(); i++) {
				if (objCurrPage.getLeftText(i).canShow(guide.getFlags())) {
					displayTextBuilder.append(objCurrPage.getLeftText(i).getText());
				}
			}
			String displayText = displayTextBuilder.toString();

			// Media Directory
			String mediaPath;
			mediaPath = comonFunctions.getMediaFullPath("", fileSeparator, appSettings, guide);
			displayText = displayText.replace(MEDIA_PATH_PLACEHOLDER, mediaPath);

			displayText = comonFunctions.substituteTextVars(displayText, guide.getSettings(),
					userSettings);

			mainShell.setLeftText(displayText, overRide.getRightCss());
		} catch (Exception e) {
			logger.error("displayPage BrwsText Exception " + e.getLocalizedMessage(), e);
			mainShell.setLeftText("", "");
		}

	}

	private void processLeftTextOverRide(String fileSeparator, AppSettings appSettings, Guide guide,
			MainShell mainShell, UserSettings userSettings) {
		if (!overRide.getLeftHtml().equals("")) {
			String leftHtml = overRide.getLeftHtml();
			// Media Directory
			try {
				String mediaPath;
				mediaPath = comonFunctions.getMediaFullPath("", fileSeparator, appSettings, guide);
				mediaPath = mediaPath.replace("\\", "/");
				leftHtml = leftHtml.replace(MEDIA_PATH_PLACEHOLDER, mediaPath);
			} catch (Exception e) {
				logger.error("displayPage Over ride lefthtml Media Directory Exception "
						+ e.getLocalizedMessage(), e);
			}

			// Guide CSS Directory
			try {
				leftHtml = leftHtml.replace("\\GuideCSS\\", guide.getCss());
			} catch (Exception e) {
				logger.error("displayPage Over ride lefthtml Guide CSS Exception "
						+ e.getLocalizedMessage(), e);
			}

			mainShell.setleftPaneHtml(leftHtml);
		} else {
			// Left text
			// Replace any string pref in the HTML with the user preference
			// they are encoded #prefName#
			try {
				String displayText = overRide.getLeftBody();
				// Media Directory
				String mediaPath;
				mediaPath = comonFunctions.getMediaFullPath("", fileSeparator, appSettings, guide);
				displayText = displayText.replace("\\MediaDir\\", mediaPath);

				displayText = comonFunctions.substituteTextVars(displayText, guide.getSettings(),
						userSettings);

				mainShell.setLeftText(displayText, overRide.getLeftCss());
			} catch (Exception e) {
				logger.error("displayPage BrwsText Exception " + e.getLocalizedMessage(), e);
				mainShell.setLeftText("", "");
			}
		}

	}

	private void processRightPanel(Page objCurrPage, String fileSeparator, AppSettings appSettings,
			Guide guide, MainShell mainShell, UserSettings userSettings) {
		GuideSettings guideSettings = guide.getSettings();

		// Replace any string pref in the HTML with the user preference
		// they are encoded #prefName#
		try {
			String displayText = "";
			if (overRide.getHtml().equals("") && overRide.getRightHtml().equals("")) {
				StringBuilder displayTextBuilder = new StringBuilder();
				for (int i = 0; i < objCurrPage.getTextCount(); i++) {
					if (objCurrPage.getText(i).canShow(guide.getFlags())) {
						displayTextBuilder.append(objCurrPage.getText(i).getText());
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
			mediaPath = comonFunctions.getMediaFullPath("", fileSeparator, appSettings, guide);
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
			MainShell mainShell, UserSettings userSettings, Delay objDelay, String imgName,
			DebugShell debugShell) {
		Button objButton;
		GuideSettings guideSettings = guide.getSettings();
		ArrayList<Button> button = new ArrayList<>();

		// Move global buttons into page object.
		for (int i1 = 0; i1 < overRide.globalButtonCount(); i1++) {
			GlobalButton objGlobalButton = overRide.getGlobalButton(i1);
			objCurrPage.addGlobalButton(objGlobalButton);
		}
		overRide.clearGlobalButtons();

		// process global buttons on page
		for (int i1 = 0; i1 < objCurrPage.getGlobalButtonCount(); i1++) {
			GlobalButton objGlobalButton = objCurrPage.getGlobalButton(i1);
			switch (objGlobalButton.getAction()) {
			case ADD:
				guide.addGlobalButton(objGlobalButton.getId(), objGlobalButton);
				break;
			case REMOVE:
				guide.removeGlobalButton(objGlobalButton.getId());
				break;
			default:
				logger.error(
						"displayPage Global Button invalid action " + objGlobalButton.getAction());
			}
		}

		// remove old buttons
		mainShell.removeButtons();

		// add top placement global buttons
		ArrayList<Button> globalTopButtons = new ArrayList<>();
		for (GlobalButton globalButton : guide.getGlobalButtons()) {
			if (globalButton.canShow(guide.getFlags())
					&& globalButton.getPlacement() == GlobalButton.Placement.TOP) {
				globalTopButtons.add(globalButton);
				debugShell.addOverrideButton(globalButton);
			}
		}
		Collections.sort(globalTopButtons);

		// add new buttons
		ArrayList<Button> pageButtons = new ArrayList<>();
		for (int i1 = 0; i1 < objCurrPage.getButtonCount(); i1++) {
			objButton = objCurrPage.getButton(i1);
			if (objButton.canShow(guide.getFlags())) {
				pageButtons.add(objButton);
			}
		}
		for (int i1 = 0; i1 < overRide.buttonCount(); i1++) {
			objButton = overRide.getButton(i1);
			if (objButton.canShow(guide.getFlags())) {
				pageButtons.add(objButton);
				debugShell.addOverrideButton(objButton);
			}
		}
		for (int i1 = 0; i1 < objCurrPage.getWebcamButtonCount(); i1++) {
			objButton = objCurrPage.getWebcamButton(i1);
			if (objButton.canShow(guide.getFlags())) {
				pageButtons.add(objButton);
			}
		}
		for (int i1 = 0; i1 < overRide.webcamButtonCount(); i1++) {
			objButton = overRide.getWebcamButton(i1);
			if (objButton.canShow(guide.getFlags())) {
				pageButtons.add(objButton);
				debugShell.addOverrideButton(objButton);
			}
		}
		Collections.sort(pageButtons);

		// add bottom placement global buttons
		ArrayList<Button> globalBottomButtons = new ArrayList<>();
		for (GlobalButton globalButton : guide.getGlobalButtons()) {
			if (globalButton.canShow(guide.getFlags())
					&& globalButton.getPlacement() == GlobalButton.Placement.BOTTOM) {
				globalBottomButtons.add(globalButton);
				debugShell.addOverrideButton(globalButton);
			}
		}
		Collections.sort(globalBottomButtons);

		// Add all buttons in reverse order
		button.addAll(globalBottomButtons);
		button.addAll(pageButtons);
		button.addAll(globalTopButtons);

		for (int i1 = button.size() - 1; i1 >= 0; i1--) {
			try {
				objButton = button.get(i1);
				String javascriptid = objButton.getjScript();
				String btnText = objButton.getText();
				btnText = comonFunctions.substituteTextVars(btnText, guideSettings, userSettings);
				objButton.setText(btnText);
				mainShell.addButton(objButton, javascriptid);
			} catch (Exception e1) {
				logger.error("displayPage Button Exception " + e1.getLocalizedMessage(), e1);
			}
		}

		try {
			if (appSettings.getDebug()) {
				// add a button to trigger the delay target if debug is set by
				// the user
				if (objDelay != null) {
					mainShell.addDelayButton(guide);
				}
				mainShell.setLblCentre(" " + objCurrPage.getId() + " / " + imgName);
			} else {
				mainShell.setLblCentre(guide.getTitle());
			}
		} catch (Exception e1) {
			logger.error("displayPage Debug Exception " + e1.getLocalizedMessage(), e1);
		}

	}

	private boolean processMetronome(Page objCurrPage, Guide guide, MainShell mainShell) {
		boolean blnMetronome = false;
		try {
			Metronome objMetronome = overRide.getMetronome();
			if (objMetronome != null) {
				blnMetronome = true;
			} else {
				if (objCurrPage.getMetronomeCount() > 0) {
					for (int i2 = 0; i2 < objCurrPage.getMetronomeCount(); i2++) {
						objMetronome = objCurrPage.getMetronome(i2);
						if (objMetronome.canShow(guide.getFlags())) {
							blnMetronome = true;
							break;
						}
					}
				}
			}
			if (blnMetronome) {
				// Metronome
				int intbpm = objMetronome.getbpm();
				logger.debug("displayPage Metronome {} BPM", intbpm);

				mainShell.setMetronomeBPM(objMetronome.getbpm(), objMetronome.getLoops(),
						objMetronome.getResolution(), objMetronome.getRhythm());

			}
		} catch (Exception e) {
			logger.error("displayPage Metronome Exception ", e);
		}
		return blnMetronome;
	}

	private void processAudio(Page objCurrPage, Guide guide, MainShell mainShell,
			String fileSeparator, AppSettings appSettings) {
		boolean blnAudio = false;
		Audio objAudio = overRide.getAudio();
		try {
			if (objAudio != null) {
				blnAudio = true;
			} else {
				if (objCurrPage.getAudioCount() > 0) {
					for (int i2 = 0; i2 < objCurrPage.getAudioCount(); i2++) {
						objAudio = objCurrPage.getAudio(i2);
						if (objAudio.canShow(guide.getFlags())) {
							blnAudio = true;
							break;
						}
					}
				}
			}
			if (blnAudio) {
				int intAudioLoops;
				String strAudio;
				String strAudioTarget;
				String strIntAudio = objAudio.getRepeat();
				if (strIntAudio.equals("")) {
					intAudioLoops = 0;
				} else {
					intAudioLoops = Integer.parseInt(strIntAudio);
				}
				strAudio = objAudio.getId();
				logger.debug("displayPage Audio {}", strAudio);
				String strStartAt = objAudio.getStartAt();
				int startAtSeconds;
				if (!strStartAt.equals("")) {
					startAtSeconds = comonFunctions.getMilisecFromTime(strStartAt) / 1000;
				} else {
					startAtSeconds = 0;
				}
				String strStopAt = objAudio.getStopAt();
				int stopAtSeconds;
				if (!strStopAt.equals("")) {
					stopAtSeconds = comonFunctions.getMilisecFromTime(strStopAt) / 1000;
				} else {
					stopAtSeconds = 0;
				}

				String imgPath = comonFunctions.getMediaFullPath(strAudio, fileSeparator,
						appSettings, guide);
				strAudioTarget = objAudio.getTarget();
				mainShell.playAudio(imgPath, startAtSeconds, stopAtSeconds, intAudioLoops,
						strAudioTarget, objAudio.getJscript(), objAudio.getScriptVar(),
						objAudio.getVolume(), true);
				logger.debug("displayPage Audio target " + strAudioTarget);
			}
		} catch (Exception e) {
			logger.error("displayPage Audio Exception " + e.getLocalizedMessage(), e);
		}

	}

	private void processAudio2(Page objCurrPage, Guide guide, MainShell mainShell,
			String fileSeparator, AppSettings appSettings) {
		boolean blnAudio = false;
		Audio objAudio = overRide.getAudio2();
		try {
			if (objAudio != null) {
				blnAudio = true;
			} else {
				if (objCurrPage.getAudio2Count() > 0) {
					for (int i2 = 0; i2 < objCurrPage.getAudio2Count(); i2++) {
						objAudio = objCurrPage.getAudio2(i2);
						if (objAudio.canShow(guide.getFlags())) {
							blnAudio = true;
							break;
						}
					}
				}
			}
			if (blnAudio) {
				int intAudioLoops;
				String strAudio;
				String strAudioTarget;
				String strIntAudio = objAudio.getRepeat();
				if (strIntAudio.equals("")) {
					intAudioLoops = 0;
				} else {
					intAudioLoops = Integer.parseInt(strIntAudio);
				}
				strAudio = objAudio.getId();
				logger.debug("displayPage Audio {}", strAudio);
				String strStartAt = objAudio.getStartAt();
				int startAtSeconds;
				if (!strStartAt.equals("")) {
					startAtSeconds = comonFunctions.getMilisecFromTime(strStartAt) / 1000;
				} else {
					startAtSeconds = 0;
				}
				String strStopAt = objAudio.getStopAt();
				int stopAtSeconds;
				if (!strStopAt.equals("")) {
					stopAtSeconds = comonFunctions.getMilisecFromTime(strStopAt) / 1000;
				} else {
					stopAtSeconds = 0;
				}

				String imgPath = comonFunctions.getMediaFullPath(strAudio, fileSeparator,
						appSettings, guide);
				strAudioTarget = objAudio.getTarget();
				mainShell.playAudio2(imgPath, startAtSeconds, stopAtSeconds, intAudioLoops,
						strAudioTarget, objAudio.getJscript(), objAudio.getScriptVar(),
						objAudio.getVolume(), true);
				logger.debug("displayPage Audio target {}", strAudioTarget);
			}
		} catch (Exception e) {
			logger.error("displayPage Audio Exception {}", e.getLocalizedMessage(), e);
		}

	}

	private Page processLoadGuide(Page objCurrPage, Guide guide, DebugShell debugShell,
			AppSettings appSettings, MainShell mainShell) {
		boolean blnLoadGuide = false;
		LoadGuide objLoadGuide = null;
		String strPageId = "";
		String pageJavascript = objCurrPage.getjScript();
		if (objCurrPage.getLoadGuideCount() > 0) {
			for (int i2 = 0; i2 < objCurrPage.getLoadGuideCount(); i2++) {
				objLoadGuide = objCurrPage.getLoadGuide(i2);
				if (objLoadGuide.canShow(guide.getFlags())) {
					blnLoadGuide = true;
					break;
				}
			}
		}

		if (blnLoadGuide) {
			String returnTarget = objLoadGuide.getReturnTarget();
			if (returnTarget.equals("")) {
				returnTarget = "start";
			}
			guide.getSettings().setPage(returnTarget);

			// run preScript
			String preScript = objLoadGuide.getPrejScript();
			if (!(preScript.equals("") || pageJavascript.equals(""))) {
				try {
					mainShell.runJscript(preScript, false);
				} catch (Exception e) {
					logger.error("displayPage javascript Exception " + e.getLocalizedMessage(), e);
				}
			}
			guide.getSettings().saveSettings();
			guide.getSettings().formFieldsReset();
			try {
				debugShell.clearPagesCombo();
			} catch (Exception ex) {
				logger.error("Clear debug pages " + ex.getLocalizedMessage(), ex);
			}
			try {
				XmlGuideReader.loadXML(
						(appSettings.getDataDirectory() + objLoadGuide.getGuidePath()), guide,
						appSettings, debugShell);
				GuideSettings guideSettings = guide.getSettings();

				// run postScript
				String postScript = objLoadGuide.getPostjScript();
				if (!(postScript.equals("") || pageJavascript.equals(""))) {
					mainShell.runJscript(postScript, pageJavascript);
				}
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

			} catch (Exception ex) {
				logger.error("Load Guide " + ex.getLocalizedMessage(), ex);
			}

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

		try {

			ContextFactory cntxFact = new ContextFactory();

			Context context = cntxFact.enterContext();
			context.setOptimizationLevel(-1);
			context.getWrapFactory().setJavaPrimitiveWrap(false);
			ScriptableObject scope = context.initStandardObjects();

			// if a state file already exists use it
			File xmlFile = new File(filename);

			if (xmlFile.exists()) {
				DocumentBuilderFactory docFactory = XMLReaderUtils.getDocumentBuilderFactory();
				DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(xmlFile);
				Element rootElement = doc.getDocumentElement();
				rootElement.normalize();

				Element elScriptVariables = comonFunctions.getElement("//scriptVariables",
						rootElement);
				if (elScriptVariables != null) {
					NodeList nodeList = elScriptVariables.getElementsByTagName("Var");
					String strName;
					String strType;
					String strValue;
					Object objValue;
					for (int i = 0; i < nodeList.getLength(); i++) {
						Node currentNode = nodeList.item(i);
						if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
							Element elVar = (Element) currentNode;
							strName = elVar.getAttribute("id");
							strType = elVar.getAttribute("type");
							CharacterData elChar;
							elChar = (CharacterData) elVar.getFirstChild();
							if (elChar != null) {
								strValue = elChar.getData();
								objValue = comonFunctions.getSavedObject(strValue, strType, scope);
							} else {
								objValue = null;
							}
							globalScriptVariables.put(strName, objValue);

						}
					}
				}

			}

		} catch (ParserConfigurationException pce) {
			logger.error(pce.getLocalizedMessage(), pce);
		} catch (SAXException | IOException e) {
			logger.error(e.getLocalizedMessage(), e);
		} finally {
			Context.exit();
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
