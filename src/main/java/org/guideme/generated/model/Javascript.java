package org.guideme.generated.model;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
public class Javascript  {

	private String text = "";

	public Javascript(XMLStreamReader reader) throws XMLStreamException {
		this.text = XmlGuideReader.processText(reader, "text","");
	}

	public Javascript() {
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
}
