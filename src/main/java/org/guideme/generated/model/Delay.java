package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
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

	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public String getJscript() {
		return jscript;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getStyle() {
		return style;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getTarget() {
		return target;
	}
	public int getStartWith() {
		return startWith;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setStartWith(int startWith) {
		this.startWith = startWith;
	}
	public String getSeconds() {
		return seconds;
	}
	public String getSet() {
		return set;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
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
