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
	private String jScript = "";
	private LocalTime ifBefore;
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
	private static final Logger LOGGER = LogManager.getLogger();
	private List<IText> text = new ArrayList<>();
	private List<Audio> audio = new ArrayList<>();
	private String id = "";
	private LocalTime ifAfter;

	public Page(XMLStreamReader reader) {
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "jScript","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
	}

	public Page() {
		/* NOP */
	}

	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	public int getImageCount() {
		return image.size();
	}
	public List<IText> getLeftTexts() {
		return leftText;
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public void addLoadGuide(LoadGuide toAdd) {
		loadGuide.add(toAdd);
	}
	public int getLeftTextCount() {
		return leftText.size();
	}
	public void addWebcam(Webcam toAdd) {
		webcam.add(toAdd);
	}
	public Page(Node n) {
		if(!n.getNodeName().equals("Page")){
			LOGGER.warn("Error reading state file. Expected element 'Page', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "":
				metronome = ModelConverters.fromString(attrValue, metronome);
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
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public List<LoadGuide> getLoadGuides() {
		return loadGuide;
	}
	public List<Metronome> getMetronomes() {
		return metronome;
	}
	public List<GlobalButton> getGlobalButtons() {
		return globalButton;
	}
	public List<Audio> getAudio2s() {
		return audio2;
	}
	public int getWebcamButtonCount() {
		return webcamButton.size();
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void setImage(List<Image> image) {
		this.image = image;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setMetronome(List<Metronome> metronome) {
		this.metronome = metronome;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void addButton(Button toAdd) {
		button.add(toAdd);
	}
	public void setAudio(List<Audio> audio) {
		this.audio = audio;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public String getId() {
		return id;
	}
	public void addDelay(Delay toAdd) {
		delay.add(toAdd);
	}
	public List<Timer> getTimers() {
		return timer;
	}
	public void addLeftText(IText toAdd) {
		leftText.add(toAdd);
	}
	public List<WebcamButton> getWebcamButtons() {
		return webcamButton;
	}
	public List<Video> getVideos() {
		return video;
	}
	public void setWebcam(List<Webcam> webcam) {
		this.webcam = webcam;
	}
	@Override
	public void setSet(String set) {
		this.set = set;
	}
	public void setLoadGuide(List<LoadGuide> loadGuide) {
		this.loadGuide = loadGuide;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	public int getGlobalButtonCount() {
		return globalButton.size();
	}
	public List<Audio> getAudios() {
		return audio;
	}
	public List<Delay> getDelays() {
		return delay;
	}
	public int getLoadGuideCount() {
		return loadGuide.size();
	}
	public String getJScript() {
		return jScript;
	}
	public List<IText> getTexts() {
		return text;
	}
	public void addGlobalButton(GlobalButton toAdd) {
		globalButton.add(toAdd);
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setWebcamButton(List<WebcamButton> webcamButton) {
		this.webcamButton = webcamButton;
	}
	public void addVideo(Video toAdd) {
		video.add(toAdd);
	}
	public int getAudio2Count() {
		return audio2.size();
	}
	public int getButtonCount() {
		return button.size();
	}
	public void setGlobalButton(List<GlobalButton> globalButton) {
		this.globalButton = globalButton;
	}
	public void setTimer(List<Timer> timer) {
		this.timer = timer;
	}
	public int getWebcamCount() {
		return webcam.size();
	}
	public void addAudio2(Audio toAdd) {
		audio2.add(toAdd);
	}
	@Override
	public String getSet() {
		return set;
	}
	public int getTimerCount() {
		return timer.size();
	}
	public void addImage(Image toAdd) {
		image.add(toAdd);
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setText(List<IText> text) {
		this.text = text;
	}
	public void setLeftText(List<IText> leftText) {
		this.leftText = leftText;
	}
	public void setVideo(List<Video> video) {
		this.video = video;
	}
	public List<Button> getButtons() {
		return button;
	}
	public int getAudioCount() {
		return audio.size();
	}
	public void addWebcamButton(WebcamButton toAdd) {
		webcamButton.add(toAdd);
	}
	public void addMetronome(Metronome toAdd) {
		metronome.add(toAdd);
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Page");
		ans.setAttribute("",ModelConverters.toString(text));
		ans.setAttribute("",ModelConverters.toString(leftText));
		ans.setAttribute("",ModelConverters.toString(button));
		ans.setAttribute("",ModelConverters.toString(audio));
		ans.setAttribute("",ModelConverters.toString(globalButton));
		ans.setAttribute("",ModelConverters.toString(webcamButton));
		ans.setAttribute("",ModelConverters.toString(delay));
		ans.setAttribute("",ModelConverters.toString(timer));
		ans.setAttribute("",ModelConverters.toString(video));
		ans.setAttribute("",ModelConverters.toString(webcam));
		ans.setAttribute("",ModelConverters.toString(audio2));
		ans.setAttribute("",ModelConverters.toString(image));
		ans.setAttribute("",ModelConverters.toString(loadGuide));
		ans.setAttribute("",ModelConverters.toString(metronome));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("jScript",ModelConverters.toString(jScript));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("id",ModelConverters.toString(id));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		return ans;
	}
	public int getMetronomeCount() {
		return metronome.size();
	}
	public void setDelay(List<Delay> delay) {
		this.delay = delay;
	}
	public int getDelayCount() {
		return delay.size();
	}
	public void addTimer(Timer toAdd) {
		timer.add(toAdd);
	}
	public List<Webcam> getWebcams() {
		return webcam;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	@Override
	public String getUnSet() {
		return unSet;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void addAudio(Audio toAdd) {
		audio.add(toAdd);
	}
	public void addText(IText toAdd) {
		text.add(toAdd);
	}
	public void setAudio2(List<Audio> audio2) {
		this.audio2 = audio2;
	}
	public List<Image> getImages() {
		return image;
	}
	public int getTextCount() {
		return text.size();
	}
	public void setButton(List<Button> button) {
		this.button = button;
	}
	public int getVideoCount() {
		return video.size();
	}
	
	@Override
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
	
	@Override
	public void setUnSet(List<String> setList) {
		ComonFunctions.getComonFunctions().setFlags(set, setList);
		ComonFunctions.getComonFunctions().unsetFlags(unSet, setList);
	}
	
}
