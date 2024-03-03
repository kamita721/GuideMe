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
	private LocalTime ifAfter;
	private String ifNotSet = "";

	public Metronome(XMLStreamReader reader) {
		this.resolution = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "beats",4);
		this.bpm = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bpm","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.loops = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "loops",-1);
		this.rhythm = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "rhythm","");
	}

	public Metronome() {
	}

	public int getResolution() {
		return resolution;
	}
	public String getBpm() {
		return bpm;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}
	public int getLoops() {
		return loops;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setLoops(int loops) {
		this.loops = loops;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public String getRhythm() {
		return rhythm;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setBpm(String bpm) {
		this.bpm = bpm;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
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
