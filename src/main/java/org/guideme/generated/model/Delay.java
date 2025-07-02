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

	private String set = "";
	private LocalTime ifBefore;
	private String unSet = "";
	private String ifNotSet = "";
	private String jscript = "";
	private String target = "";
	private String seconds = "";
	private String ifSet = "";
	private String scriptVar = "";
	private String style = "";
	private static final Logger LOGGER = LogManager.getLogger();
	private int startWith = 0;
	private LocalTime ifAfter;

	public Delay(XMLStreamReader reader) {
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.seconds = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "seconds","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.style = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "style","");
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.startWith = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-with",0);
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
	}

	public Delay() {
		/* NOP */
	}

	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	@Override
	public String getUnSet() {
		return unSet;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public Delay(Node n) {
		if(!n.getNodeName().equals("Delay")){
			LOGGER.warn("Error reading state file. Expected element 'Delay', but got '{}'", n.getNodeName());
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
			case "seconds":
				seconds = ModelConverters.fromString(attrValue, seconds);
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
			case "onTriggered":
				jscript = ModelConverters.fromString(attrValue, jscript);
				break;
			case "style":
				style = ModelConverters.fromString(attrValue, style);
				break;
			case "scriptvar":
				scriptVar = ModelConverters.fromString(attrValue, scriptVar);
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
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	@Override
	public void setSet(String set) {
		this.set = set;
	}
	public int getStartWith() {
		return startWith;
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getSeconds() {
		return seconds;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	@Override
	public String getSet() {
		return set;
	}
	public String getTarget() {
		return target;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public String getJscript() {
		return jscript;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Delay");
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("seconds",ModelConverters.toString(seconds));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("onTriggered",ModelConverters.toString(jscript));
		ans.setAttribute("style",ModelConverters.toString(style));
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("start-with",ModelConverters.toString(startWith));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		ans.setAttribute("target",ModelConverters.toString(target));
		return ans;
	}
	public String getStyle() {
		return style;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public void setStartWith(int startWith) {
		this.startWith = startWith;
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
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
	
	
	public int getDelaySec() {
		return ComonFunctions.getComonFunctions().getRandom(seconds);
	}
	
}
