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
	private String returnTarget = "";
	private String guidePath = "";
	private String target = "";
	private String ifNotSet = "";
	private LocalTime ifAfter;

	public LoadGuide(XMLStreamReader reader) {
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.preScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "preScript","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.returnTarget = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "return-target","");
		this.postScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "postScript","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.guidePath = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "guidePath","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
	}

	public LoadGuide() {
	}

	public void setTarget(String target) {
		this.target = target;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setPreScript(String preScript) {
		this.preScript = preScript;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public String getPreScript() {
		return preScript;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setPostScript(String postScript) {
		this.postScript = postScript;
	}
	public String getGuidePath() {
		return guidePath;
	}
	public String getTarget() {
		return target;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public String getIfSet() {
		return ifSet;
	}
	public String getReturnTarget() {
		return returnTarget;
	}
	public void setGuidePath(String guidePath) {
		this.guidePath = guidePath;
	}
	public void setReturnTarget(String returnTarget) {
		this.returnTarget = returnTarget;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public String getPostScript() {
		return postScript;
	}
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
