package org.guideme.generated.model;

import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import org.guideme.guideme.model.ModelConverters;
import java.util.List;
import java.util.Calendar;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.stream.XMLStreamException;
import java.time.LocalTime;
import org.w3c.dom.NamedNodeMap;
import org.apache.logging.log4j.LogManager;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class Timer implements FlagSet, Filterable  {

	private String imageId = "";
	private String set = "";
	private LocalTime ifBefore;
	private Calendar timerEnd;
	private String unSet = "";
	private String ifNotSet = "";
	private String target = "";
	private String jscript = "";
	private String ifSet = "";
	private String delay = "";
	private static final Logger LOGGER = LogManager.getLogger();
	private String id = "";
	private String text = "";
	private LocalTime ifAfter;

	public Timer(XMLStreamReader reader) throws XMLStreamException {
		this.delay = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "seconds","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.imageId = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "imageId","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.text = XmlGuideReader.processText(reader, "text","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
	}

	public Timer() {
		/* NOP */
	}

	public void setTarget(String target) {
		this.target = target;
	}
	public String getText() {
		return text;
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void setTimerEnd(Calendar timerEnd) {
		this.timerEnd = timerEnd;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public String getJscript() {
		return jscript;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Timer");
		ans.setAttribute("seconds",ModelConverters.toString(delay));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("imageId",ModelConverters.toString(imageId));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("id",ModelConverters.toString(id));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("",ModelConverters.toString(timerEnd));
		ans.setAttribute("text",ModelConverters.toString(text));
		ans.setAttribute("onTriggered",ModelConverters.toString(jscript));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		return ans;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Timer(Node n) {
		if(!n.getNodeName().equals("Timer")){
			LOGGER.warn("Error reading state file. Expected element 'Timer', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "":
				timerEnd = ModelConverters.fromString(attrValue, timerEnd);
				break;
			case "imageId":
				imageId = ModelConverters.fromString(attrValue, imageId);
				break;
			case "set":
				set = ModelConverters.fromString(attrValue, set);
				break;
			case "if-after":
				ifAfter = ModelConverters.fromString(attrValue, ifAfter);
				break;
			case "onTriggered":
				jscript = ModelConverters.fromString(attrValue, jscript);
				break;
			case "unSet":
				unSet = ModelConverters.fromString(attrValue, unSet);
				break;
			case "target":
				target = ModelConverters.fromString(attrValue, target);
				break;
			case "if-not-set":
				ifNotSet = ModelConverters.fromString(attrValue, ifNotSet);
				break;
			case "seconds":
				delay = ModelConverters.fromString(attrValue, delay);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
				break;
			case "id":
				id = ModelConverters.fromString(attrValue, id);
				break;
			case "text":
				text = ModelConverters.fromString(attrValue, text);
				break;
				default:
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	@Override
	public String getSet() {
		return set;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	public String getDelay() {
		return delay;
	}
	public Calendar getTimerEnd() {
		return timerEnd;
	}
	@Override
	public String getUnSet() {
		return unSet;
	}
	public String getTarget() {
		return target;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public String getId() {
		return id;
	}
	@Override
	public void setSet(String set) {
		this.set = set;
	}
	public String getImageId() {
		return imageId;
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
	
	
	public int getTimerSec() {
		return ComonFunctions.getComonFunctions().getRandom(delay);
	}
	
}
