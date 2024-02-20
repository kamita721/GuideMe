package org.guideme.guideme.model;

import static org.guideme.guideme.util.XMLReaderUtils.getAttributeLocalTime;
import static org.guideme.guideme.util.XMLReaderUtils.getAttributeOrDefaultNoNS;

import java.time.LocalTime;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
import org.guideme.guideme.scripting.functions.ComonFunctions;

public class Text
{
	private final String ifSet;
	private final String ifNotSet;
	private final LocalTime ifBefore; //Time of day must be before this time
	private final LocalTime ifAfter; //Time of day must be after this time
	private final String body;
	private final ComonFunctions comonFunctions = ComonFunctions.getComonFunctions();

	public Text(XMLStreamReader reader) throws XMLStreamException {
		this.ifSet = getAttributeOrDefaultNoNS(reader, "if-set", "");
		this.ifNotSet = getAttributeOrDefaultNoNS(reader, "if-not-set", "");
		this.ifBefore = getAttributeLocalTime(reader, "if-before");
		this.ifAfter = getAttributeLocalTime(reader, "if-after");
		this.body = XmlGuideReader.processText(reader);
	}
	
	public Text(String text)
	{
		this(text, "", "", "", "");
	}

	public Text(String text, String ifSet, String ifNotSet)
	{
		this(text, ifSet, ifNotSet, "", "");
	}

	public Text(String text, String ifSet, String ifNotSet, String ifBefore, String ifAfter)
	{
		this.body = text;
		this.ifNotSet = ifNotSet;
		this.ifSet = ifSet;
		this.ifBefore = ifBefore == null || ifBefore.isEmpty() ? null : LocalTime.parse(ifBefore);
		this.ifAfter = ifAfter == null || ifAfter.isEmpty() ? null : LocalTime.parse(ifAfter);
	}

	public boolean canShow(List<String> setList)
	{
		return comonFunctions.canShow(setList, ifSet, ifNotSet) && comonFunctions.canShowTime(ifBefore, ifAfter);
	}

	public String getText() {
		return this.body;
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

	public LocalTime getIfAfter() {
		return ifAfter;
	}
}
