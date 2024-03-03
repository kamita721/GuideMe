package org.guideme.generated.model;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class Title  {

	private String text = "";

	public Title(XMLStreamReader reader) throws XMLStreamException {
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public Title() {
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
}
