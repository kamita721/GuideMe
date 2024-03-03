package org.guideme.generated.model;

import java.util.List;
import java.util.Calendar;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import java.time.LocalTime;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class Timer implements FlagSet, Filterable  {

	private String imageId = "";
	private String set = "";
	private LocalTime ifBefore;
	private String unSet = "";
	private Calendar timerEnd;
	private String ifNotSet = "";
	private String jscript = "";
	private String target = "";
	private String ifSet = "";
	private String delay = "";
	private String id = "";
	private String text = "";
	private LocalTime ifAfter;

	public Timer(XMLStreamReader reader) throws XMLStreamException {
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.jscript = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "onTriggered","");
		this.imageId = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "imageId","");
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.target = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "target","");
		this.unSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "unSet","");
		this.set = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.id = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "id","");
		this.delay = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "seconds","");
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public Timer() {
	}

	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public void setUnSet(String unSet) {
		this.unSet = unSet;
	}
	public String getText() {
		return text;
	}
	public String getImageId() {
		return imageId;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public String getUnSet() {
		return unSet;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getId() {
		return id;
	}
	public void setTimerEnd(Calendar timerEnd) {
		this.timerEnd = timerEnd;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getSet() {
		return set;
	}
	public String getDelay() {
		return delay;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public Calendar getTimerEnd() {
		return timerEnd;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setSet(String set) {
		this.set = set;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setDelay(String delay) {
		this.delay = delay;
	}
	public String getJscript() {
		return jscript;
	}
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
	
	public void setUnSet(List<String> setList) {
		ComonFunctions.getComonFunctions().setFlags(set, setList);
		ComonFunctions.getComonFunctions().unsetFlags(unSet, setList);
	}
	
	
	public int getTimerSec() {
		return ComonFunctions.getComonFunctions().getRandom(delay);
	}
	
}
