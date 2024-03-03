package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
public class Delay implements FlagSet, Filterable  {

	private String seconds = "";
	private String ifSet = "";
	private String set = "";
	private String scriptVar = "";
	private LocalTime ifBefore;
	private String style = "";
	private String unSet = "";
	private int startWith = 0;
	private String target = "";
	private String ifNotSet = "";
	private String jscript = "";
	private LocalTime ifAfter;

	public Delay(XMLStreamReader reader) {
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.seconds = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "seconds","");
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.startWith = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-with",0);
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.style = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "style","");
	}

	public Delay() {
	}

	public void setSet(String set) {
		this.set = set;
	}
	public String getSet() {
		return set;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public String getStyle() {
		return style;
	}
	public void setSeconds(String seconds) {
		this.seconds = seconds;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public String getIfSet() {
		return ifSet;
	}
	public String getTarget() {
		return target;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public int getStartWith() {
		return startWith;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public String getSeconds() {
		return seconds;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getJscript() {
		return jscript;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setStartWith(int startWith) {
		this.startWith = startWith;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
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
