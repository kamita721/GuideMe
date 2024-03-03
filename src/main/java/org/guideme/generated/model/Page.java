package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
public class Page implements FlagSet, Filterable  {

	private List<Image> image;
	private String set = "";
	private String jScript = "";
	private LocalTime ifBefore;
	private List<Video> video;
	private List<GlobalButton> globalButton;
	private String unSet = "";
	private String ifNotSet = "";
	private List<WebcamButton> webcamButton;
	private List<Button> button;
	private List<Timer> timer;
	private String ifSet = "";
	private List<Delay> delay;
	private List<Webcam> webcam;
	private List<LoadGuide> loadGuide;
	private List<Metronome> metronome;
	private List<IText> leftText;
	private List<Audio> audio2;
	private List<IText> text;
	private String id = "";
	private List<Audio> audio;
	private LocalTime ifAfter;

	public Page(XMLStreamReader reader) {
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "jScript","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
	}

	public Page() {
	}

	public void addImage(Image toAdd) {
		image.add(toAdd);
	}
	public void setTimer(List<Timer> timer) {
		this.timer = timer;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void addButton(Button toAdd) {
		button.add(toAdd);
	}
	public void addLeftText(IText toAdd) {
		leftText.add(toAdd);
	}
	public void setLoadGuide(List<LoadGuide> loadGuide) {
		this.loadGuide = loadGuide;
	}
	public List<Audio> getAudio2s() {
		return audio2;
	}
	public List<GlobalButton> getGlobalButtons() {
		return globalButton;
	}
	public void setText(List<IText> text) {
		this.text = text;
	}
	public int getAudio2Count() {
		return audio2.size();
	}
	public void addLoadGuide(LoadGuide toAdd) {
		loadGuide.add(toAdd);
	}
	public List<WebcamButton> getWebcamButtons() {
		return webcamButton;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void addVideo(Video toAdd) {
		video.add(toAdd);
	}
	public int getMetronomeCount() {
		return metronome.size();
	}
	public void addAudio2(Audio toAdd) {
		audio2.add(toAdd);
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public String getIfSet() {
		return ifSet;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public int getLoadGuideCount() {
		return loadGuide.size();
	}
	public void setImage(List<Image> image) {
		this.image = image;
	}
	public void setDelay(List<Delay> delay) {
		this.delay = delay;
	}
	public List<IText> getLeftTexts() {
		return leftText;
	}
	public void setWebcamButton(List<WebcamButton> webcamButton) {
		this.webcamButton = webcamButton;
	}
	public void addWebcamButton(WebcamButton toAdd) {
		webcamButton.add(toAdd);
	}
	public List<LoadGuide> getLoadGuides() {
		return loadGuide;
	}
	public int getWebcamButtonCount() {
		return webcamButton.size();
	}
	public void addText(IText toAdd) {
		text.add(toAdd);
	}
	public List<Webcam> getWebcams() {
		return webcam;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public List<Video> getVideos() {
		return video;
	}
	public int getTimerCount() {
		return timer.size();
	}
	public List<Timer> getTimers() {
		return timer;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void addWebcam(Webcam toAdd) {
		webcam.add(toAdd);
	}
	public void addAudio(Audio toAdd) {
		audio.add(toAdd);
	}
	public List<Button> getButtons() {
		return button;
	}
	public List<Audio> getAudios() {
		return audio;
	}
	public int getTextCount() {
		return text.size();
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void setWebcam(List<Webcam> webcam) {
		this.webcam = webcam;
	}
	public String getSet() {
		return set;
	}
	public void setAudio(List<Audio> audio) {
		this.audio = audio;
	}
	public List<IText> getTexts() {
		return text;
	}
	public List<Delay> getDelays() {
		return delay;
	}
	public void addTimer(Timer toAdd) {
		timer.add(toAdd);
	}
	public void addGlobalButton(GlobalButton toAdd) {
		globalButton.add(toAdd);
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setLeftText(List<IText> leftText) {
		this.leftText = leftText;
	}
	public void addMetronome(Metronome toAdd) {
		metronome.add(toAdd);
	}
	public void setGlobalButton(List<GlobalButton> globalButton) {
		this.globalButton = globalButton;
	}
	public void addDelay(Delay toAdd) {
		delay.add(toAdd);
	}
	public void setMetronome(List<Metronome> metronome) {
		this.metronome = metronome;
	}
	public List<Metronome> getMetronomes() {
		return metronome;
	}
	public int getWebcamCount() {
		return webcam.size();
	}
	public int getImageCount() {
		return image.size();
	}
	public void setAudio2(List<Audio> audio2) {
		this.audio2 = audio2;
	}
	public String getJScript() {
		return jScript;
	}
	public int getDelayCount() {
		return delay.size();
	}
	public void setButton(List<Button> button) {
		this.button = button;
	}
	public void setVideo(List<Video> video) {
		this.video = video;
	}
	public int getVideoCount() {
		return video.size();
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public int getButtonCount() {
		return button.size();
	}
	public List<Image> getImages() {
		return image;
	}
	public int getAudioCount() {
		return audio.size();
	}
	public int getGlobalButtonCount() {
		return globalButton.size();
	}
	public int getLeftTextCount() {
		return leftText.size();
	}
	public String getId() {
		return id;
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
