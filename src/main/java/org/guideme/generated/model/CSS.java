package org.guideme.generated.model;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class CSS  {

	private String text = "";

	public CSS(XMLStreamReader reader) throws XMLStreamException {
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public CSS() {
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
