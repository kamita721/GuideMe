package org.guideme.generated.model;

import javax.xml.stream.XMLStreamConstants;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.util.XMLReaderUtils;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import org.apache.logging.log4j.LogManager;
public class Settings  {

	private boolean convertArgumentTypes = false;
	private boolean pageSound = true;
	private boolean forceStartPage = false;
	private boolean autoSetPageWhenSeen = true;

	public Settings(XMLStreamReader reader) throws XMLStreamException {
		final Logger LOGGER = LogManager.getLogger();
		int depth = 1;
		while (depth > 0) {
			int eventType = reader.next();
			if (eventType == XMLStreamConstants.START_ELEMENT) {
				depth++;
				String tagName = reader.getName().getLocalPart();
				switch(tagName){
				case "PageSound":
					pageSound = XMLReaderUtils.getStringContentOrDefault(reader, true);
					break;
				case "ConvertArgumentTypes":
					convertArgumentTypes = XMLReaderUtils.getStringContentOrDefault(reader, false);
					break;
				case "ForceStartPage":
					forceStartPage = XMLReaderUtils.getStringContentOrDefault(reader, false);
					break;
				case "AutoSetPageWhenSeen":
					autoSetPageWhenSeen = XMLReaderUtils.getStringContentOrDefault(reader, true);
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

	public Settings() {
		/* NOP */
	}

	public boolean getPageSound() {
		return pageSound;
	}
	public void setPageSound(boolean pageSound) {
		this.pageSound = pageSound;
	}
	public void setAutoSetPageWhenSeen(boolean autoSetPageWhenSeen) {
		this.autoSetPageWhenSeen = autoSetPageWhenSeen;
	}
	public boolean getForceStartPage() {
		return forceStartPage;
	}
	public boolean getConvertArgumentTypes() {
		return convertArgumentTypes;
	}
	public boolean getAutoSetPageWhenSeen() {
		return autoSetPageWhenSeen;
	}
	public void setForceStartPage(boolean forceStartPage) {
		this.forceStartPage = forceStartPage;
	}
	public void setConvertArgumentTypes(boolean convertArgumentTypes) {
		this.convertArgumentTypes = convertArgumentTypes;
	}
}
