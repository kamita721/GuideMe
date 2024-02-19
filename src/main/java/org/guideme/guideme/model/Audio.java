package org.guideme.guideme.model;

import java.time.LocalTime;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.scripting.functions.ComonFunctions;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;

public class Audio {
	private final String id; // file name of the audio file
	private final String startAt; // time to start audio at
	private final String stopAt; // time to stop audio at
	private final String target; // page to go to when the audio finishes
	private final String set; // flags to set when the audio finishes
	private final String unSet; // flags to unset when the audio finishes
	private final String repeat; // number of times to repeat the audio
	private final String ifSet; // only play the audio if theses flags are set
	private final String ifNotSet; // don't play the audo if these flags are set
	private final String jscript; // javascript function to run on audio finish
	private LocalTime ifBefore; // Time of day must be before this time
	private LocalTime ifAfter; // Time of day must be after this time
	private final ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();
	private final String scriptVar;
	private final int volume; // integer between 0 and 100

	public Audio(XMLStreamReader reader) {
		this.target = getAttributeOrDefaultNoNS(reader, "target", "");
		this.startAt = getAttributeOrDefaultNoNS(reader, "start-at", "");
		this.stopAt = getAttributeOrDefaultNoNS(reader, "stop-at", "");
		this.id = getAttributeOrDefaultNoNS(reader, "id", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.repeat = getAttributeOrDefaultNoNS(reader, "loops", ""); //TODO rename
		this.jscript = getAttributeOrDefaultNoNS(reader, "onTriggered", "");//TODO rename
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
		this.scriptVar = getAttributeOrDefaultNoNS(reader, "scriptvar", "");
		this.volume = getAttributeOrDefaultNoNS(reader, "volume", 100);

		//TODO why are these always empty?
		this.unSet = "";
		this.set = "";
	}
	
	public Audio(String id, String startAt, String stopAt, String target, String ifSet, String ifNotSet, String set,
			String unSet, String repeat, String jscript, String ifAfter, String ifBefore, String scriptVar,
			int volume) {
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
		return id;
	}

	public String getStartAt() {
		return startAt;
	}

	public String getStopAt() {
		return stopAt;
	}

	public String getTarget() {
		return target;
	}

	// pass the current flags and check if we can play this audio
	public boolean canShow(List<String> setList) {
		boolean retVal = comonFunctions.canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = comonFunctions.canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}

	// pass the current flags and do the set / unset on it
	public void setUnSet(List<String> setList) {
		comonFunctions.setFlags(set, setList);
		comonFunctions.unsetFlags(unSet, setList);
	}

	public String getRepeat() {
		return repeat;
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
