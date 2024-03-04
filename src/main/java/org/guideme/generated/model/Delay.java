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
public class Delay implements FlagSet, Filterable  {

	private String ifSet = "";
	private String seconds = "";
	private String set = "";
	private String scriptVar = "";
	private LocalTime ifBefore;
	private String style = "";
	private String unSet = "";
	private int startWith = 0;
	private LocalTime ifAfter;
	private String ifNotSet = "";
	private String jscript = "";
	private String target = "";

	public Delay(XMLStreamReader reader) {
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.seconds = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "seconds","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.startWith = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-with",0);
		this.style = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "style","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
	}

	public Delay() {
	}

	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public String getStyle() {
		return style;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public String getSet() {
		return set;
	}
	public Delay(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Delay")){
		logger.warn("Error reading state file. Expected element 'Delay', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "if-not-set":
				ifNotSet = ModelConverters.fromString(attrValue, ifNotSet);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "seconds":
				seconds = ModelConverters.fromString(attrValue, seconds);
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
			case "onTriggered":
				jscript = ModelConverters.fromString(attrValue, jscript);
				break;
			case "scriptvar":
				scriptVar = ModelConverters.fromString(attrValue, scriptVar);
				break;
			case "style":
				style = ModelConverters.fromString(attrValue, style);
				break;
			case "start-with":
				startWith = ModelConverters.fromString(attrValue, startWith);
				break;
			case "unSet":
				unSet = ModelConverters.fromString(attrValue, unSet);
				break;
			case "target":
				target = ModelConverters.fromString(attrValue, target);
				break;
				default:
			logger.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
		
		
		
		
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public String getJscript() {
		return jscript;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public String getSeconds() {
		return seconds;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setStartWith(int startWith) {
		this.startWith = startWith;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getTarget() {
		return target;
	}
	public int getStartWith() {
		return startWith;
	}
	public String getIfSet() {
		return ifSet;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Delay");
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("onTriggered",ModelConverters.toString(jscript));
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("seconds",ModelConverters.toString(seconds));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("start-with",ModelConverters.toString(startWith));
		ans.setAttribute("style",ModelConverters.toString(style));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		return ans;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
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
	
	
	public int getDelaySec() {
		return ComonFunctions.getComonFunctions().getRandom(seconds);
	}
	
}
