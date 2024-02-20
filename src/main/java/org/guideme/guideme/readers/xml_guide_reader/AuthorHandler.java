package org.guideme.guideme.readers.xml_guide_reader;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.model.Guide;
import org.guideme.guideme.util.XMLReaderUtils;

public class AuthorHandler {
	private AuthorHandler() {
	}

	public static void handle(XMLStreamReader reader, Guide guide) throws XMLStreamException {
		while (!XMLReaderUtils.isAtElementEnd(reader, "Author")) {
			// TODO, is there anything else under Author we need to handle?
			if (XMLReaderUtils.isAtElementStart(reader, "Name")) {
				guide.setAuthorName(XMLReaderUtils.getStringContent(reader));
			}
			reader.next();
		}
	}
}
