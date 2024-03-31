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
	private String target = "";
	private String jscript = "";
	private String seconds = "";
	private String ifSet = "";
	private String scriptVar = "";
	private String style = "";
	private static final Logger LOGGER = LogManager.getLogger();
	private int startWith = 0;
	private LocalTime ifAfter;

	public Delay(XMLStreamReader reader) {
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.seconds = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "seconds","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.style = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "style","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.startWith = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-with",0);
	}

	public Delay() {
		/* NOP */
	}

	public String getTarget() {
		return target;
	}
	public String getStyle() {
		return style;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	@Override
	public String getSet() {
		return set;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public void setStartWith(int startWith) {
		this.startWith = startWith;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public int getStartWith() {
		return startWith;
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
	public String getScriptVar() {
		return scriptVar;
	}
	@Override
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Delay");
		ans.setAttribute("scriptvar",ModelConverters.toString(scriptVar));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("set",ModelConverters.toString(set));
		ans.setAttribute("seconds",ModelConverters.toString(seconds));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("target",ModelConverters.toString(target));
		ans.setAttribute("onTriggered",ModelConverters.toString(jscript));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("style",ModelConverters.toString(style));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("unSet",ModelConverters.toString(unSet));
		ans.setAttribute("start-with",ModelConverters.toString(startWith));
		return ans;
	}
	@Override
	public void setSet(String set) {
		this.set = set;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
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
			case "set":
				set = ModelConverters.fromString(attrValue, set);
				break;
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
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
			case "unSet":
				unSet = ModelConverters.fromString(attrValue, unSet);
				break;
			case "start-with":
				startWith = ModelConverters.fromString(attrValue, startWith);
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
	public String getSeconds() {
		return seconds;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getJscript() {
		return jscript;
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
