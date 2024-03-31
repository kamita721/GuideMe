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
public class LoadGuide implements Filterable  {

	private String ifSet = "";
	private String preScript = "";
	private String postScript = "";
	private LocalTime ifBefore;
	private String guidePath = "";
	private String returnTarget = "";
	private LocalTime ifAfter;
	private String ifNotSet = "";
	private String target = "";

	public LoadGuide(XMLStreamReader reader) {
		this.guidePath = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "guidePath","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.postScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "postScript","");
		this.preScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "preScript","");
		this.returnTarget = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "return-target","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
	}

	public LoadGuide() {
		/* NOP */
	}

	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public String getPostScript() {
		return postScript;
	}
	public void setReturnTarget(String returnTarget) {
		this.returnTarget = returnTarget;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("LoadGuide");
		ans.setAttribute("guidePath",ModelConverters.toString(guidePath));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("postScript",ModelConverters.toString(postScript));
		ans.setAttribute("preScript",ModelConverters.toString(preScript));
		ans.setAttribute("return-target",ModelConverters.toString(returnTarget));
		ans.setAttribute("target",ModelConverters.toString(target));
		return ans;
	}
	public void setPreScript(String preScript) {
		this.preScript = preScript;
	}
	public String getPreScript() {
		return preScript;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public LoadGuide(Node n) {
		Logger LOGGER = LogManager.getLogger();
		if(!n.getNodeName().equals("LoadGuide")){
			LOGGER.warn("Error reading state file. Expected element 'LoadGuide', but got '{}'", n.getNodeName());
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
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
				break;
			case "if-after":
				ifAfter = ModelConverters.fromString(attrValue, ifAfter);
				break;
			case "preScript":
				preScript = ModelConverters.fromString(attrValue, preScript);
				break;
			case "postScript":
				postScript = ModelConverters.fromString(attrValue, postScript);
				break;
			case "guidePath":
				guidePath = ModelConverters.fromString(attrValue, guidePath);
				break;
			case "return-target":
				returnTarget = ModelConverters.fromString(attrValue, returnTarget);
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
	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setGuidePath(String guidePath) {
		this.guidePath = guidePath;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getGuidePath() {
		return guidePath;
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public String getReturnTarget() {
		return returnTarget;
	}
	public String getTarget() {
		return target;
	}
	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	
	@Override
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
