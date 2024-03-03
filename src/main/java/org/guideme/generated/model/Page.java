package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
public class Page implements FlagSet, Filterable  {

	private List<Image> image = new ArrayList<>();
	private String set = "";
	private LocalTime ifBefore;
	private String jScript = "";
	private List<Video> video = new ArrayList<>();
	private List<GlobalButton> globalButton = new ArrayList<>();
	private String unSet = "";
	private List<WebcamButton> webcamButton = new ArrayList<>();
	private String ifNotSet = "";
	private List<Button> button = new ArrayList<>();
	private List<Timer> timer = new ArrayList<>();
	private String ifSet = "";
	private List<Delay> delay = new ArrayList<>();
	private List<Webcam> webcam = new ArrayList<>();
	private List<LoadGuide> loadGuide = new ArrayList<>();
	private List<Metronome> metronome = new ArrayList<>();
	private List<IText> leftText = new ArrayList<>();
	private List<Audio> audio2 = new ArrayList<>();
	private List<IText> text = new ArrayList<>();
	private List<Audio> audio = new ArrayList<>();
	private String id = "";
	private LocalTime ifAfter;

	public Page(XMLStreamReader reader) {
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "jScript","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
	}

	public Page() {
	}

	public String getJScript() {
		return jScript;
	}
	public void setGlobalButton(List<GlobalButton> globalButton) {
		this.globalButton = globalButton;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void addWebcam(Webcam toAdd) {
		webcam.add(toAdd);
	}
	public void addLeftText(IText toAdd) {
		leftText.add(toAdd);
	}
	public void setButton(List<Button> button) {
		this.button = button;
	}
	public void addImage(Image toAdd) {
		image.add(toAdd);
	}
	public List<Video> getVideos() {
		return video;
	}
	public void setText(List<IText> text) {
		this.text = text;
	}
	public void setAudio(List<Audio> audio) {
		this.audio = audio;
	}
	public void addButton(Button toAdd) {
		button.add(toAdd);
	}
	public List<Timer> getTimers() {
		return timer;
	}
	public int getDelayCount() {
		return delay.size();
	}
	public void setLoadGuide(List<LoadGuide> loadGuide) {
		this.loadGuide = loadGuide;
	}
	public void addMetronome(Metronome toAdd) {
		metronome.add(toAdd);
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public List<Audio> getAudios() {
		return audio;
	}
	public int getLoadGuideCount() {
		return loadGuide.size();
	}
	public List<Delay> getDelays() {
		return delay;
	}
	public List<Image> getImages() {
		return image;
	}
	public int getAudioCount() {
		return audio.size();
	}
	public int getButtonCount() {
		return button.size();
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setAudio2(List<Audio> audio2) {
		this.audio2 = audio2;
	}
	public List<IText> getLeftTexts() {
		return leftText;
	}
	public void setTimer(List<Timer> timer) {
		this.timer = timer;
	}
	public void addTimer(Timer toAdd) {
		timer.add(toAdd);
	}
	public List<Button> getButtons() {
		return button;
	}
	public int getTimerCount() {
		return timer.size();
	}
	public void addText(IText toAdd) {
		text.add(toAdd);
	}
	public List<WebcamButton> getWebcamButtons() {
		return webcamButton;
	}
	public void addDelay(Delay toAdd) {
		delay.add(toAdd);
	}
	public void setImage(List<Image> image) {
		this.image = image;
	}
	public List<Metronome> getMetronomes() {
		return metronome;
	}
	public int getGlobalButtonCount() {
		return globalButton.size();
	}
	public List<GlobalButton> getGlobalButtons() {
		return globalButton;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void addWebcamButton(WebcamButton toAdd) {
		webcamButton.add(toAdd);
	}
	public String getUnSet() {
		return unSet;
	}
	public List<Webcam> getWebcams() {
		return webcam;
	}
	public String getSet() {
		return set;
	}
	public int getTextCount() {
		return text.size();
	}
	public List<LoadGuide> getLoadGuides() {
		return loadGuide;
	}
	public void setWebcamButton(List<WebcamButton> webcamButton) {
		this.webcamButton = webcamButton;
	}
	public void addAudio(Audio toAdd) {
		audio.add(toAdd);
	}
	public void setSet(String set) {
		this.set = set;
	}
	public List<IText> getTexts() {
		return text;
	}
	public List<Audio> getAudio2s() {
		return audio2;
	}
	public void addGlobalButton(GlobalButton toAdd) {
		globalButton.add(toAdd);
	}
	public void addVideo(Video toAdd) {
		video.add(toAdd);
	}
	public void setDelay(List<Delay> delay) {
		this.delay = delay;
	}
	public void setLeftText(List<IText> leftText) {
		this.leftText = leftText;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void setVideo(List<Video> video) {
		this.video = video;
	}
	public void addAudio2(Audio toAdd) {
		audio2.add(toAdd);
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public int getWebcamButtonCount() {
		return webcamButton.size();
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public int getImageCount() {
		return image.size();
	}
	public void addLoadGuide(LoadGuide toAdd) {
		loadGuide.add(toAdd);
	}
	public int getAudio2Count() {
		return audio2.size();
	}
	public void setWebcam(List<Webcam> webcam) {
		this.webcam = webcam;
	}
	public void setMetronome(List<Metronome> metronome) {
		this.metronome = metronome;
	}
	public int getMetronomeCount() {
		return metronome.size();
	}
	public String getId() {
		return id;
	}
	public int getWebcamCount() {
		return webcam.size();
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public int getVideoCount() {
		return video.size();
	}
	public int getLeftTextCount() {
		return leftText.size();
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
	
	public void setUnSet(List<String> setList) {
		ComonFunctions.getComonFunctions().setFlags(set, setList);
		ComonFunctions.getComonFunctions().unsetFlags(unSet, setList);
	}
	
}
