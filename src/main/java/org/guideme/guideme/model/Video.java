package org.guideme.guideme.model;

import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

import java.time.LocalTime;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.scripting.functions.ComonFunctions;

public class Video
{
	private final String id;
	private final String startAt;
	private final String stopAt;
	private final String target;
	private final String ifSet;
	private final String ifNotSet;
	private final String set;
	private final String unSet;
	private final int repeat;
	private final String jscript;
	private LocalTime ifBefore; //Time of day must be before this time
	private LocalTime ifAfter; //Time of day must be after this time
	private final ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private final String scriptVar;
	private final int volume;  //integer between 0 and 100 

	public Video(XMLStreamReader reader) {
		this.target = getAttributeOrDefaultNoNS(reader, "target", "");
		this.startAt = getAttributeOrDefaultNoNS(reader, "start-at", "");
		this.stopAt = getAttributeOrDefaultNoNS(reader, "stop-at", "");
		this.id = getAttributeOrDefaultNoNS(reader, "id", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.repeat = getAttributeOrDefaultNoNS(reader, "loops", 0); //TODO rename
		this.jscript = getAttributeOrDefaultNoNS(reader, "onTriggered", "");//TODO rename
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
		this.scriptVar = getAttributeOrDefaultNoNS(reader, "scriptvar", "");
		this.volume = getAttributeOrDefaultNoNS(reader, "volume", 100);
		this.unSet = ""; //TODO why is this always blank?
		this.set = ""; //TODO why is this always blank?
	}
	
	public Video(String id, String startAt, String stopAt, String target, String ifSet, String ifNotSet, String set, String unSet, int repeat, String jscript, String ifAfter, String ifBefore, String scriptVar, int volume)
	{
		this.id = id;
		this.startAt = startAt;
		this.stopAt = stopAt;
		this.target = target;
		this.ifSet = ifSet;
		this.ifNotSet = ifNotSet;
		this.set = set;
		this.unSet = unSet;
		this.repeat = repeat;
		this.jscript = jscript;
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
		this.volume = volume;

	}

	public String getId() {
		return this.id;
	}

	public String getStartAt() {
		return this.startAt;
	}

	public String getStopAt() {
		return this.stopAt;
	}

	public String getTarget() {
		return this.target;
	}

	public boolean canShow(List<String> setList) {
		boolean retVal = comonFunctions.canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal =  comonFunctions.canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}

	public void setUnSet(List<String> setList) {
		comonFunctions.setFlags(this.set, setList);
		comonFunctions.unsetFlags(this.unSet, setList);
	}

	public int getRepeat() {
		return this.repeat;
	}

	public String getJscript() {
		return jscript;
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

	public int getVolume() {
		return volume;
	}


}