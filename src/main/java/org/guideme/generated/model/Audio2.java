package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
public class Audio2 implements FlagSet, Playable, Filterable, Audio  {

	private String stopAt = "";
	private String set = "";
	private LocalTime ifBefore;
	private String jScript = "";
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
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onclick","");
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.startAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-at","");
		this.stopAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "stop-at","");
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.volume = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "volume",100);
	}

	public Audio2() {
	}

	public String getId() {
		return id;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public String getStopAt() {
		return stopAt;
	}
	public void setStartAt(String startAt) {
		this.startAt = startAt;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public String getTarget() {
		return target;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public String getJScript() {
		return jScript;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public int getVolume() {
		return volume;
	}
	public String getRepeat() {
		return repeat;
	}
	public String getStartAt() {
		return startAt;
	}
	public String getSet() {
		return set;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public String getJscript() {
		return jscript;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setStopAt(String stopAt) {
		this.stopAt = stopAt;
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
