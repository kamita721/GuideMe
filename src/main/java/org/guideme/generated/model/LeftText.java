package org.guideme.generated.model;

import java.util.List;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import java.time.LocalTime;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class LeftText implements Filterable, IText  {

	private String ifSet = "";
	private LocalTime ifBefore;
	private String text = "";
	private String ifNotSet = "";
	private LocalTime ifAfter;

	public LeftText(XMLStreamReader reader) throws XMLStreamException {
		this.ifNotSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-not-set","");
		this.ifBefore = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-before",null);
		this.ifSet = XMLReaderUtils.getAttributeOrDefaultNoNS(reader, "if-set","");
		this.ifAfter = XMLReaderUtils.getAttributeLocalTimeDefaultable(reader, "if-after",null);
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public LeftText() {
	}

	public String getIfNotSet() {
		return ifNotSet;
	}
	public String getText() {
		return text;
	}
	public void setIfAfter(LocalTime ifAfter) {
		this.ifAfter = ifAfter;
	}
	public LocalTime getIfAfter() {
		return ifAfter;
	}
	public LocalTime getIfBefore() {
		return ifBefore;
	}
	public void setIfNotSet(String ifNotSet) {
		this.ifNotSet = ifNotSet;
	}
	public void setIfBefore(LocalTime ifBefore) {
		this.ifBefore = ifBefore;
	}
	public String getIfSet() {
		return ifSet;
	}
	public void setIfSet(String ifSet) {
		this.ifSet = ifSet;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public boolean canShow(List<String> setList) {
		boolean retVal = ComonFunctions.getComonFunctions().canShowTime(ifBefore, ifAfter);
		if (retVal) {
			retVal = ComonFunctions.getComonFunctions().canShow(setList, ifSet, ifNotSet);
		}
		return retVal;
	}
	
}
