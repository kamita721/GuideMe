package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalTime;
public class Metronome implements Filterable  {

	private String ifSet = "";
	private int loops = -1;
	private LocalTime ifBefore;
	private String rhythm = "";
	private int resolution = 4;
	private String bpm = "";
	private String ifNotSet = "";
	private LocalTime ifAfter;

	public Metronome(XMLStreamReader reader) {
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.bpm = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bpm","");
		this.resolution = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "beats",4);
		this.loops = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "loops",-1);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.rhythm = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "rhythm","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
	}

	public Metronome() {
	}

	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getBpm() {
		return bpm;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}
	public String getRhythm() {
		return rhythm;
	}
	public void setLoops(int loops) {
		this.loops = loops;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public String getIfSet() {
		return ifSet;
	}
	public int getLoops() {
		return loops;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public int getResolution() {
		return resolution;
	}
	public void setBpm(String bpm) {
		this.bpm = bpm;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
	
	//TODO it is kinda bad that we rely on the case distinction between getbpm and getBpm
	public int getbpm() {
		return ComonFunctions.getComonFunctions().getRandom(bpm);
	}
	
}
