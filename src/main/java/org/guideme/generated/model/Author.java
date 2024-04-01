package org.guideme.generated.model;

import javax.xml.stream.XMLStreamConstants;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.LogManager;
public class Author  {

	private String name = "";
	private static final Logger LOGGER = LogManager.getLogger();
	private String url = "";

	public Author(XMLStreamReader reader) throws XMLStreamException {
		int depth = 1;
		while (depth > 0) {
			int eventType = reader.next();
			if (eventType == XMLStreamConstants.START_ELEMENT) {
				depth++;
				String tagName = reader.getName().getLocalPart();
				switch(tagName){
				case "Url":
					url = XMLReaderUtils.getStringContentOrDefault(reader, "");
					break;
				case "Name":
					name = XMLReaderUtils.getStringContentOrDefault(reader, "");
					break;
					default:
					LOGGER.warn("Unhandled tag '{}' at location \n{}", tagName, reader.getLocation());
					XMLReaderUtils.getStringContentUntilElementEnd(reader);
					break;
				}
				eventType = reader.next();
				if (XMLReaderUtils.isAtElementEnd(reader, "Settings")) {
					return;
				}
			}
			if (eventType == XMLStreamConstants.END_ELEMENT) {
				depth--;
			}
		}
	}

	public Author() {
		/* NOP */
	}

	public String getName() {
		return name;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
}
