package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
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
	}

	public void setGuidePath(String guidePath) {
		this.guidePath = guidePath;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setReturnTarget(String returnTarget) {
		this.returnTarget = returnTarget;
	}
	public String getTarget() {
		return target;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public String getPreScript() {
		return preScript;
	}
	public String getGuidePath() {
		return guidePath;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public String getPostScript() {
		return postScript;
	}
	public String getReturnTarget() {
		return returnTarget;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void setPreScript(String preScript) {
		this.preScript = preScript;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}
	public String getIfSet() {
		return ifSet;
	}
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
