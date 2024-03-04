package org.guideme.generated.model;

import java.util.List;
import org.w3c.dom.Element;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import java.time.LocalTime;
import org.w3c.dom.NamedNodeMap;
import org.guideme.guideme.model.ModelConverters;
import org.apache.logging.log4j.LogManager;
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
		/* NOP */
	}

	public int getLoops() {
		return loops;
	}
	public void setLoops(int loops) {
		this.loops = loops;
	}
	public String getIfNotSet() {
		return ifNotSet;
	}
	public void setBpm(String bpm) {
		this.bpm = bpm;
	}
	public String getBpm() {
		return bpm;
	}
	public String getRhythm() {
		return rhythm;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public Metronome(Node n) {
		Logger logger = LogManager.getLogger();
		if(!n.getNodeName().equals("Metronome")){
			logger.warn("Error reading state file. Expected element 'Metronome', but got '{}'", n.getNodeName());
		}
		NamedNodeMap nnm = n.getAttributes();
		for(int i=0; i<nnm.getLength(); i++){
			Node child = nnm.item(i);
			String attrName = child.getNodeName();
			String attrValue = child.getNodeValue();
			switch(attrName){
			case "if-not-set":
				ifNotSet = ModelConverters.fromString(attrValue, ifNotSet);
				break;
			case "if-set":
				ifSet = ModelConverters.fromString(attrValue, ifSet);
				break;
			case "if-before":
				ifBefore = ModelConverters.fromString(attrValue, ifBefore);
				break;
			case "if-after":
				ifAfter = ModelConverters.fromString(attrValue, ifAfter);
				break;
			case "beats":
				resolution = ModelConverters.fromString(attrValue, resolution);
				break;
			case "loops":
				loops = ModelConverters.fromString(attrValue, loops);
				break;
			case "rhythm":
				rhythm = ModelConverters.fromString(attrValue, rhythm);
				break;
			case "bpm":
				bpm = ModelConverters.fromString(attrValue, bpm);
				break;
				default:
				logger.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Metronome");
		ans.setAttribute("beats",ModelConverters.toString(resolution));
		ans.setAttribute("bpm",ModelConverters.toString(bpm));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("loops",ModelConverters.toString(loops));
		ans.setAttribute("rhythm",ModelConverters.toString(rhythm));
		return ans;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public int getResolution() {
		return resolution;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
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
