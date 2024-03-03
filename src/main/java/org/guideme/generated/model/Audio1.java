package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
public class Audio1 implements FlagSet, Playable, Filterable, Audio  {

	private String stopAt = "";
	private String set = "";
	private String jScript = "";
	private LocalTime ifBefore;
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

	public Audio1(XMLStreamReader reader) {
		this.volume = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "volume",100);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.scriptVar = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "scriptvar","");
		this.repeat = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "loops","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.jScript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onclick","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.stopAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "stop-at","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.startAt = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "start-at","");
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
	}

	public Audio1() {
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public String getJScript() {
		return jScript;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStopAt(String stopAt) {
		this.stopAt = stopAt;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public void setJScript(String jScript) {
		this.jScript = jScript;
	}
	public String getStartAt() {
		return startAt;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
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
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public String getScriptVar() {
		return scriptVar;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public int getVolume() {
		return volume;
	}
	public String getStopAt() {
		return stopAt;
	}
	public void setScriptVar(String scriptVar) {
		this.scriptVar = scriptVar;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public String getRepeat() {
		return repeat;
	}
	public String getTarget() {
		return target;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getJscript() {
		return jscript;
	}
	public String getUnSet() {
		return unSet;
	}
	public void setStartAt(String startAt) {
		this.startAt = startAt;
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
