package org.guideme.guideme.model;

import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.scripting.functions.ComonFunctions;

public class Page {
	private final String id; // Page Name
	private final ArrayList<Text> text = new ArrayList<Text>(); // HTML to display
	private final ArrayList<Text> leftText = new ArrayList<Text>(); // HTML to display in the left
																	// pane instead of an image
	private final ArrayList<Button> button = new ArrayList<Button>();
	private final ArrayList<GlobalButton> globalButton = new ArrayList<GlobalButton>();
	private final ArrayList<WebcamButton> webcamButton = new ArrayList<WebcamButton>();
	private final ArrayList<Delay> delay = new ArrayList<Delay>();
	private final ArrayList<Timer> timer = new ArrayList<Timer>();
	private final ArrayList<Video> video = new ArrayList<Video>();
	private final ArrayList<Webcam> webcam = new ArrayList<Webcam>();
	private final ArrayList<Image> image = new ArrayList<Image>();
	private final ArrayList<LoadGuide> loadGuide = new ArrayList<LoadGuide>();
	private final ArrayList<Audio> audio = new ArrayList<Audio>();
	private final ArrayList<Audio> audio2 = new ArrayList<Audio>();
	private final ArrayList<Metronome> metronome = new ArrayList<Metronome>();
	private String ifSet;
	private String ifNotSet;
	private LocalTime ifBefore; // Time of day must be before this time
	private LocalTime ifAfter; // Time of day must be after this time
	private String set;
	private String unSet;
	private String jScript = "";
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private static Logger logger = LogManager.getLogger();

	public Text getLeftText(int txtIndex) {
		return leftText.get(txtIndex);
	}

	public void addLeftText(Text leftText) {
		this.leftText.add(leftText);
	}

	public int getLeftTextCount() {
		return this.leftText.size();
	}

	public Page(XMLStreamReader reader) {
		this.id = getAttributeOrDefaultNoNS(reader, "id", "");
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.set = getAttributeOrDefaultNoNS(reader, "set", "");
		this.unSet = getAttributeOrDefaultNoNS(reader, "unset", "");
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
	}

	public Page(String id) {
		this.id = id;
	}

	public Page(String id, String ifSet, String ifNotSet, String set, String unSet, boolean autoSet,
			String ifAfter, String ifBefore) {
		this.id = id;
		this.ifSet = ifSet;
		this.ifNotSet = ifNotSet;
		this.set = set;
		this.unSet = unSet;
		if (ifBefore.equals("")) {
			this.ifBefore = null;
		} else {
			this.ifBefore = LocalTime.parse(ifBefore);
		}
		if (ifAfter.equals("")) {
			this.ifAfter = null;
		} else {
			this.ifAfter = LocalTime.parse(ifAfter);
		}

		if (autoSet) {
			if (this.set.length() == 0) {
				this.set = id;
			} else {
				this.set = this.set + "," + id;
			}
		}
	}

	public Button getButton(int butIndex) {
		return button.get(butIndex);
	}

	public Button[] getButtons() {
		return button.toArray(new Button[] {});
	}

	public void addButton(Button button) {
		this.button.add(button);
	}

	public WebcamButton getWebcamButton(int butIndex) {
		return webcamButton.get(butIndex);
	}

	public void addWebcamButton(WebcamButton button) {
		this.webcamButton.add(button);
	}

	public Delay[] getDelays() {
		return delay.toArray(new Delay[] {});
	}

	public Delay getDelay(int delIndex) {
		return delay.get(delIndex);
	}

	public void addDelay(Delay delay) {
		this.delay.add(delay);
	}

	public Timer getTimer(int timIndex) {
		return timer.get(timIndex);
	}

	public void addTimer(Timer timer) {
		this.timer.add(timer);
	}

	public Video getVideo(int vidIndex) {
		return video.get(vidIndex);
	}

	public Video[] getVideos() {
		return video.toArray(new Video[] {});
	}

	public void addVideo(Video video) {
		this.video.add(video);
	}

	public Webcam getWebcam(int vidIndex) {
		return webcam.get(vidIndex);
	}

	public Webcam[] getWebcams() {
		return webcam.toArray(new Webcam[] {});
	}

	public void addWebcam(Webcam webcam) {
		this.webcam.add(webcam);
	}

	public Image getImage(int imgIndex) {
		return image.get(imgIndex);
	}

	public Image[] getImages() {
		return image.toArray(new Image[] {});
	}

	public void addImage(Image image) {
		if (image.getId().isBlank()) {
			logger.warn("Image without id. Ignoring.");
			return;
		}
		this.image.add(image);
	}

	public LoadGuide getLoadGuide(int guideIndex) {
		return loadGuide.get(guideIndex);
	}

	public void addLoadGuide(LoadGuide loadGuide) {
		if (loadGuide.getGuidePath().isBlank()) {
			logger.warn("Image without guidePath. Ignoring.");
			return;
		}
		this.loadGuide.add(loadGuide);
	}

	public Audio getAudio(int audIndex) {
		return audio.get(audIndex);
	}

	public Audio[] getAudios() {
		return audio.toArray(new Audio[] {});
	}

	public void addAudio(Audio audio) {
		this.audio.add(audio);
	}

	public Metronome getMetronome(int metIndex) {
		return metronome.get(metIndex);
	}

	public Metronome[] getMetronomes() {
		return metronome.toArray(new Metronome[] {});
	}

	public void addMetronome(Metronome metronome) {
		if (!metronome.isValid()) {
			logger.warn("Invalid metronome. Ignoring.");
			return;
		}
		this.metronome.add(metronome);
	}

	public String getId() {
		return id;
	}

	public int getButtonCount() {
		return button.size();
	}

	public int getWebcamButtonCount() {
		return webcamButton.size();
	}

	public int getDelayCount() {
		return delay.size();
	}

	public int getTimerCount() {
		return timer.size();
	}

	public int getVideoCount() {
		return video.size();
	}

	public int getWebcamCount() {
		return webcam.size();
	}

	public int getImageCount() {
		return image.size();
	}

	public int getLoadGuideCount() {
		return loadGuide.size();
	}

	public int getMetronomeCount() {
		return metronome.size();
	}

	public boolean canShow(List<String> setList) {
		boolean retVal = comonFunctions.canShowTime(ifBefore, ifAfter);
		if (retVal) {
			String ifNotSetPage = id;
			if (ifNotSet.length() > 0) {
				ifNotSetPage = ifNotSetPage + "+" + ifNotSet;
			}
			retVal = comonFunctions.canShow(setList, ifSet, ifNotSetPage);
		}
		return retVal;
	}

	public void setUnSet(List<String> setList) {
		comonFunctions.setFlags(set, setList);
		comonFunctions.unsetFlags(unSet, setList);
	}

	@Override
	public String toString() {
		return "page [Page Name=" + id + "]";
	}

	public int getAudioCount() {
		return audio.size();
	}

	public Text getText(int txtIndex) {
		return text.get(txtIndex);
	}

	public void addText(Text text) {
		this.text.add(text);
	}

	public int getTextCount() {
		return this.text.size();
	}

	public String getjScript() {
		return jScript;
	}

	public void setjScript(String jScript) {
		this.jScript = jScript;
	}

	public String getIfSet() {
		return ifSet;
	}

	public String getIfNotSet() {
		return ifNotSet;
	}

	public String getSet() {
		return set;
	}

	public String getUnSet() {
		return unSet;
	}

	public LocalTime getIfBefore() {
		return ifBefore;
	}

	public void setIfBefore(String ifBefore) {
		if (ifBefore.equals("")) {
			this.ifBefore = null;
		} else {
			this.ifBefore = LocalTime.parse(ifBefore);
		}
	}

	public LocalTime getIfAfter() {
		return ifAfter;
	}

	public void setIfAfter(String ifAfter) {
		if (ifAfter.equals("")) {
			this.ifAfter = null;
		} else {
			this.ifAfter = LocalTime.parse(ifAfter);
		}
	}

	public Audio getAudio2(int audIndex) {
		return audio2.get(audIndex);
	}

	public void addAudio2(Audio audio) {
		this.audio2.add(audio);
	}

	public int getAudio2Count() {
		return audio2.size();
	}

	public GlobalButton getGlobalButton(int butIndex) {
		return globalButton.get(butIndex);
	}

	public void addGlobalButton(GlobalButton button) {
		globalButton.add(button);
	}

	public int getGlobalButtonCount() {
		return globalButton.size();
	}

	public void clearGlobalButtons() {
		globalButton.clear();
	}

}
