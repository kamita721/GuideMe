package org.guideme.generated.model;

import java.util.List;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.time.LocalTime;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
public class Audio2 implements FlagSet, Playable, Filterable, Audio  {

	private String stopAt = "";
	private String set = "";
	private LocalTime ifBefore;
	private String unSet = "";
	private String ifNotSet = "";
	private String jscript = "";
	private String target = "";
	private int volume = 100;
	private String ifSet = "";
	private String repeat = "";
	private String scriptVar = "";
	private String id = "";
	private String startAt = "";
	private LocalTime ifAfter;

	public Audio2(XMLStreamReader reader) {
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.repeat = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "loops","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.startAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-at","");
		this.stopAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "stop-at","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.volume = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "volume",100);
	}

	public Audio2() {
		/* NOP */
	}

	public Element asXml(Document doc) {
		Element ans = doc.createElement("Audio2");
		ans.setAttribute("id",ModelConverters.toString(id));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("loops",ModelConverters.toString(repeat));
		ans.setAttribute("onTriggered",ModelConverters.toString(jscript));
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("start-at",ModelConverters.toString(startAt));
		ans.setAttribute("stop-at",ModelConverters.toString(stopAt));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		ans.setAttribute("volume",ModelConverters.toString(volume));
		return ans;
	}
	public void setStopAt(String stopAt) {
		this.stopAt = stopAt;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public Audio2(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Audio2")){
			logger.warn("Error reading state file. Expected element 'Audio2', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "set":
				set = ModelConverters.fromString(attrValue, set);
				break;
			case "if-after":
				ifAfter = ModelConverters.fromString(attrValue, ifAfter);
				break;
			case "start-at":
				startAt = ModelConverters.fromString(attrValue, startAt);
				break;
			case "onTriggered":
				jscript = ModelConverters.fromString(attrValue, jscript);
				break;
			case "loops":
				repeat = ModelConverters.fromString(attrValue, repeat);
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
			case "stop-at":
				stopAt = ModelConverters.fromString(attrValue, stopAt);
				break;
			case "volume":
				volume = ModelConverters.fromString(attrValue, volume);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
				break;
			case "scriptvar":
				scriptVar = ModelConverters.fromString(attrValue, scriptVar);
				break;
			case "id":
				id = ModelConverters.fromString(attrValue, id);
				break;
				default:
				logger.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public String getStopAt() {
		return stopAt;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public String getTarget() {
		return target;
	}
	public int getVolume() {
		return volume;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public String getStartAt() {
		return startAt;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getId() {
		return id;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public String getSet() {
		return set;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJscript() {
		return jscript;
	}
	
	public void setUnSet(List<String> setList) {
		ComonFunctions.getComonFunctions().setFlags(set, setList);
		ComonFunctions.getComonFunctions().unsetFlags(unSet, setList);
	}
	
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
