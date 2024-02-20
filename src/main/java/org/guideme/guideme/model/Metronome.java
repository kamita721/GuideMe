package org.guideme.guideme.model;

import java.time.LocalTime;
import java.util.List;

import javax.xml.stream.XMLStreamReader;

import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

import org.guideme.guideme.scripting.functions.ComonFunctions;

public class Metronome {
	private final String ifSet;
	private final String ifNotSet;
	private LocalTime ifBefore; //Time of day must be before this time
	private LocalTime ifAfter; //Time of day must be after this time
	private final String bpm; //beats per minute
	private final int resolution; //ticks per beat
	private final int loops;//number of loops, -1 infinite, 0 play once, 1 repeat once
	private final String rhythm; //beat rhythm 
	/*
	 Rhythm is a list of ticks so at 60 bpm, resolution of 4 we get 4 ticks per second
	 a straight 1 beat per second would be 0,4,8,12,16,20,24 (giving 7 one second beats)
	 So if we want 20 fast beats (at 120 bpm) then 5 slow we can use
	 0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,42,46,50,54,58 
	 */
	private ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();

	public Metronome(XMLStreamReader reader) {
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
		this.bpm = getAttributeOrDefaultNoNS(reader, "bpm", "");
		this.resolution = getAttributeOrDefaultNoNS(reader, "beats", 4);
		this.loops = getAttributeOrDefaultNoNS(reader, "loops", -1);
		this.rhythm = getAttributeOrDefaultNoNS(reader, "rhythm", "");
	}
	
	public Metronome(String bpm, String ifSet, String ifNotSet) {
		this(bpm, ifSet, ifNotSet, 4, -1, "", "", "");
	}
	
	public Metronome(String bpm, String ifSet, String ifNotSet, int resolution,	int loops, String rhythm, String ifAfter, String ifBefore) {
		this.bpm = bpm;
		this.ifSet = ifSet;
		this.ifNotSet = ifNotSet;
		this.resolution = resolution;
		this.loops = loops;
		this.rhythm = rhythm;
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
	}

	public boolean canShow(List<String> setList) {
		boolean retVal = comonFunctions.canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal =  comonFunctions.canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}

	public int getbpm() {
		//will either return bpm as an integer 
		//or if it is in the format (1..20) it will return a random number between 1 and 20
		return comonFunctions.getRandom(bpm);
	}
	
	public boolean isValid() {
		return !bpm.isBlank();
	}

	public int getResolution() {
		return resolution;
	}

	public int getLoops() {
		return loops;
	}

	public String getRhythm() {
		return rhythm;
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

}
