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
	private String bpmString = "";
	private int loops = -1;
	private LocalTime ifBefore;
	private String rhythm = "";
	private int resolution = 4;
	private LocalTime ifAfter;
	private String ifNotSet = "";

	public Metronome(XMLStreamReader reader) {
		this.resolution = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "beats",4);
		this.bpmString = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "bpmString","");
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

	@Override
	public String getIfNotSet() {
		return ifNotSet;
	}
	@Override
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	@Override
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public void setRhythm(String rhythm) {
		this.rhythm = rhythm;
	}
	@Override
	public String getIfSet() {
		return ifSet;
	}
	public void setBpmString(String bpmString) {
		this.bpmString = bpmString;
	}
	public void setLoops(int loops) {
		this.loops = loops;
	}
	public String getRhythm() {
		return rhythm;
	}
	public int getResolution() {
		return resolution;
	}
	@Override
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setResolution(int resolution) {
		this.resolution = resolution;
	}
	public Metronome(Node n) {
		Logger LOGGER = LogManager.getLogger();
		if(!n.getNodeName().equals("Metronome")){
			LOGGER.warn("Error reading state file. Expected element 'Metronome', but got '{}'", n.getNodeName());
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
			case "bpmString":
				bpmString = ModelConverters.fromString(attrValue, bpmString);
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
				default:
				LOGGER.warn("Unhandled attribute '{}'", attrName);
				break;
			}
		}
	}
	public int getLoops() {
		return loops;
	}
	public String getBpmString() {
		return bpmString;
	}
	@Override
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	@Override
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public Element asXml(Document doc) {
		Element ans = doc.createElement("Metronome");
		ans.setAttribute("beats",ModelConverters.toString(resolution));
		ans.setAttribute("bpmString",ModelConverters.toString(bpmString));
		ans.setAttribute("if-after",ModelConverters.toString(ifAfter));
		ans.setAttribute("if-before",ModelConverters.toString(ifBefore));
		ans.setAttribute("if-not-set",ModelConverters.toString(ifNotSet));
		ans.setAttribute("if-set",ModelConverters.toString(ifSet));
		ans.setAttribute("loops",ModelConverters.toString(loops));
		ans.setAttribute("rhythm",ModelConverters.toString(rhythm));
		return ans;
	}
	@Override
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	
	@Override
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
	
	public int getBpm() {
		return ComonFunctions.getComonFunctions().getRandom(bpmString);
	}
	
}
