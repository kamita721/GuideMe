package org.guideme.guideme.model;

import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

import java.time.LocalTime;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.scripting.functions.ComonFunctions;

public class Delay {
	private final String ifSet;
	private final String ifNotSet;
	private final String set;
	private final String unSet;
	private final String seconds;
	private final String target;
	private final int startWith;
	private final String style;
	private final String jScript;
	private LocalTime ifBefore; //Time of day must be before this time
	private LocalTime ifAfter; //Time of day must be after this time
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private final String scriptVar;
	
	public Delay(XMLStreamReader reader) {
		this.target = getAttributeOrDefaultNoNS(reader, "target", "");
		this.startWith = getAttributeOrDefaultNoNS(reader, "start-with", 0);
		this.style = getAttributeOrDefaultNoNS(reader, "style", "");
		this.seconds = getAttributeOrDefaultNoNS(reader, "seconds", "");
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
		this.set = getAttributeOrDefaultNoNS(reader, "set", "");
		this.unSet = getAttributeOrDefaultNoNS(reader, "unset", "");
		this.scriptVar = getAttributeOrDefaultNoNS(reader, "scriptvar", "");
		this.jScript = getAttributeOrDefaultNoNS(reader, "onTriggered", "");
	}
	
	public Delay(String target, String delay, String ifSet, String ifNotSet, int startWith, String style, String set, String unSet, String jScript, String ifAfter, String ifBefore, String scriptVar) {
		this.target = target;
		this.seconds = delay;
		this.ifNotSet = ifNotSet;
		this.ifSet = ifSet;
		this.startWith = startWith;
		this.style = style;
		this.set = set;
		this.unSet = unSet;
		this.jScript = jScript;
		if (ifBefore.equals("")) {
			this.ifBefore = null;
		} else {
			this.ifBefore = LocalTime.parse(ifBefore);
		}
		if (ifAfter.equals("")) {
			this.ifAfter = null;
		} else {
			this.ifAfter = LocalTime.parse(ifAfter);
		}
		this.scriptVar = scriptVar;

	}

	public boolean canShow(List<String> setList) {
		boolean retVal = comonFunctions.canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal =  comonFunctions.canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}

	public int getDelaySec() {
		return comonFunctions.getRandom(seconds);
	}

	public String getTarget() {
		return target;
	}

	public int getStartWith() {
		return startWith;
	}

	public String getstyle() {
		return style;
	}
	
	public void setUnSet(List<String> setList) {
		//pass in the current flags and either add or remove the ones for this delay
		comonFunctions.setFlags(set, setList);
		comonFunctions.unsetFlags(unSet, setList);
	}

	public String getSet() {
		return set;
	}

	public String getUnSet() {
		return unSet;
	}

	public String getjScript() {
		return jScript;
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

	public void setIfBefore(String ifBefore) {
		if (ifBefore.equals("")) {
			this.ifBefore = null;
		} else {
			this.ifBefore = LocalTime.parse(ifBefore);
		}
	}

	public LocalTime getIfAfter() {
		return ifAfter;
	}

	public void setIfAfter(String ifAfter) {
		if (ifAfter.equals("")) {
			this.ifAfter = null;
		} else {
			this.ifAfter = LocalTime.parse(ifAfter);
		}
	}

	public String getScriptVar() {
		return scriptVar;
	}



}