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
public class Video implements FlagSet, Playable, Filterable  {

	private String stopAt = "";
	private String set = "";
	private LocalTime ifBefore;
	private String unSet = "";
	private String jscript = "";
	private String target = "";
	private String ifNotSet = "";
	private int volume = 100;
	private String ifSet = "";
	private String repeat = "";
	private String scriptVar = "";
	private static final Logger LOGGER = LogManager.getLogger();
	private String id = "";
	private String startAt = "";
	private LocalTime ifAfter;

	public Video(XMLStreamReader reader) {
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.startAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-at","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.repeat = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "loops","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.stopAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "stop-at","");
		this.volume = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "volume",100);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
	}

	public Video() {
		/* NOP */
	}

	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	@Override
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	@Override
	public void setStopAt(String stopAt) {
		this.stopAt = stopAt;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	@Override
	public String getUnSet() {
		return unSet;
	}
	public String getTarget() {
		return target;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	@Override
	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Video");
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("start-at",ModelConverters.toString(startAt));
		ans.setAttribute("onTriggered",ModelConverters.toString(jscript));
		ans.setAttribute("loops",ModelConverters.toString(repeat));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("stop-at",ModelConverters.toString(stopAt));
		ans.setAttribute("volume",ModelConverters.toString(volume));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("id",ModelConverters.toString(id));
		return ans;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public void setSet(String set) {
		this.set = set;
	}
	@Override
	public int getVolume() {
		return volume;
	}
	public Video(Node n) {
		if(!n.getNodeName().equals("Video")){
			LOGGER.warn("Error reading state file. Expected element 'Video', but got '{}'", n.getNodeName());
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
			case "stop-at":
				stopAt = ModelConverters.fromString(attrValue, stopAt);
				break;
			case "volume":
				volume = ModelConverters.fromString(attrValue, volume);
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
			case "scriptvar":
				scriptVar = ModelConverters.fromString(attrValue, scriptVar);
				break;
			case "id":
				id = ModelConverters.fromString(attrValue, id);
				break;
				default:
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	@Override
	public String getRepeat() {
		return repeat;
	}
	@Override
	public String getStopAt() {
		return stopAt;
	}
	@Override
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public String getStartAt() {
		return startAt;
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	@Override
	public String getJscript() {
		return jscript;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public String getId() {
		return id;
	}
	@Override
	public String getSet() {
		return set;
	}
	@Override
	public void setVolume(int volume) {
		this.volume = volume;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
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
