package org.guideme.generated.model;

import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.guideme.guideme.model.ModelConverters;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.time.LocalTime;
import org.w3c.dom.NamedNodeMap;
import org.apache.logging.log4j.LogManager;
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
	private List<Metronome> metronome = new ArrayList<>();
	private List<LoadGuide> loadGuide = new ArrayList<>();
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
		/* NOP */
	}

	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public String getUnSet() {
		return unSet;
	}
	public int getAudioCount() {
		return audio.size();
	}
	public int getGlobalButtonCount() {
		return globalButton.size();
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public List<IText> getTexts() {
		return text;
	}
	public List<Image> getImages() {
		return image;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public int getWebcamButtonCount() {
		return webcamButton.size();
	}
	public List<Video> getVideos() {
		return video;
	}
	public void addMetronome(Metronome toAdd) {
		metronome.add(toAdd);
	}
	public int getVideoCount() {
		return video.size();
	}
	public List<Button> getButtons() {
		return button;
	}
	public void addWebcam(Webcam toAdd) {
		webcam.add(toAdd);
	}
	public void setMetronome(List<Metronome> metronome) {
		this.metronome = metronome;
	}
	public int getImageCount() {
		return image.size();
	}
	public List<LoadGuide> getLoadGuides() {
		return loadGuide;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void addVideo(Video toAdd) {
		video.add(toAdd);
	}
	public void setWebcamButton(List<WebcamButton> webcamButton) {
		this.webcamButton = webcamButton;
	}
	public void addLeftText(IText toAdd) {
		leftText.add(toAdd);
	}
	public void addLoadGuide(LoadGuide toAdd) {
		loadGuide.add(toAdd);
	}
	public void setGlobalButton(List<GlobalButton> globalButton) {
		this.globalButton = globalButton;
	}
	public void setButton(List<Button> button) {
		this.button = button;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public int getTimerCount() {
		return timer.size();
	}
	public int getWebcamCount() {
		return webcam.size();
	}
	public List<Audio> getAudios() {
		return audio;
	}
	public void addWebcamButton(WebcamButton toAdd) {
		webcamButton.add(toAdd);
	}
	public void setAudio(List<Audio> audio) {
		this.audio = audio;
	}
	public void addText(IText toAdd) {
		text.add(toAdd);
	}
	public List<Metronome> getMetronomes() {
		return metronome;
	}
	public int getLoadGuideCount() {
		return loadGuide.size();
	}
	public void addAudio2(Audio toAdd) {
		audio2.add(toAdd);
	}
	public void setLeftText(List<IText> leftText) {
		this.leftText = leftText;
	}
	public void addAudio(Audio toAdd) {
		audio.add(toAdd);
	}
	public void setDelay(List<Delay> delay) {
		this.delay = delay;
	}
	public List<Webcam> getWebcams() {
		return webcam;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void addButton(Button toAdd) {
		button.add(toAdd);
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void addImage(Image toAdd) {
		image.add(toAdd);
	}
	public int getTextCount() {
		return text.size();
	}
	public List<GlobalButton> getGlobalButtons() {
		return globalButton;
	}
	public String getJScript() {
		return jScript;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void addTimer(Timer toAdd) {
		timer.add(toAdd);
	}
	public List<IText> getLeftTexts() {
		return leftText;
	}
	public List<Timer> getTimers() {
		return timer;
	}
	public String getSet() {
		return set;
	}
	public int getLeftTextCount() {
		return leftText.size();
	}
	public String getId() {
		return id;
	}
	public int getMetronomeCount() {
		return metronome.size();
	}
	public List<Audio> getAudio2s() {
		return audio2;
	}
	public int getDelayCount() {
		return delay.size();
	}
	public void addDelay(Delay toAdd) {
		delay.add(toAdd);
	}
	public void setWebcam(List<Webcam> webcam) {
		this.webcam = webcam;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Page");
		ans.setAttribute("",ModelConverters.toString(image));
		ans.setAttribute("",ModelConverters.toString(webcamButton));
		ans.setAttribute("",ModelConverters.toString(button));
		ans.setAttribute("",ModelConverters.toString(text));
		ans.setAttribute("",ModelConverters.toString(webcam));
		ans.setAttribute("",ModelConverters.toString(audio));
		ans.setAttribute("",ModelConverters.toString(leftText));
		ans.setAttribute("",ModelConverters.toString(metronome));
		ans.setAttribute("",ModelConverters.toString(timer));
		ans.setAttribute("",ModelConverters.toString(audio2));
		ans.setAttribute("",ModelConverters.toString(loadGuide));
		ans.setAttribute("",ModelConverters.toString(video));
		ans.setAttribute("",ModelConverters.toString(globalButton));
		ans.setAttribute("",ModelConverters.toString(delay));
		ans.setAttribute("id",ModelConverters.toString(id));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("jScript",ModelConverters.toString(jScript));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		return ans;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void setVideo(List<Video> video) {
		this.video = video;
	}
	public List<WebcamButton> getWebcamButtons() {
		return webcamButton;
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public void setAudio2(List<Audio> audio2) {
		this.audio2 = audio2;
	}
	public int getButtonCount() {
		return button.size();
	}
	public int getAudio2Count() {
		return audio2.size();
	}
	public void setLoadGuide(List<LoadGuide> loadGuide) {
		this.loadGuide = loadGuide;
	}
	public void addGlobalButton(GlobalButton toAdd) {
		globalButton.add(toAdd);
	}
	public void setText(List<IText> text) {
		this.text = text;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setTimer(List<Timer> timer) {
		this.timer = timer;
	}
	public void setImage(List<Image> image) {
		this.image = image;
	}
	public Page(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Page")){
			logger.warn("Error reading state file. Expected element 'Page', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "":
				delay = ModelConverters.fromString(attrValue, delay);
				break;
			case "if-not-set":
				ifNotSet = ModelConverters.fromString(attrValue, ifNotSet);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
				break;
			case "set":
				set = ModelConverters.fromString(attrValue, set);
				break;
			case "if-after":
				ifAfter = ModelConverters.fromString(attrValue, ifAfter);
				break;
			case "jScript":
				jScript = ModelConverters.fromString(attrValue, jScript);
				break;
			case "id":
				id = ModelConverters.fromString(attrValue, id);
				break;
			case "unSet":
				unSet = ModelConverters.fromString(attrValue, unSet);
				break;
				default:
				logger.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public List<Delay> getDelays() {
		return delay;
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
