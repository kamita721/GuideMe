package org.guideme.guideme.readers.xml_guide_reader;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static org.guideme.guideme.util.XMLReaderUtils.getStringContentOrDefault;
import static org.guideme.guideme.util.XMLReaderUtils.isAtElementEnd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.settings.GuideSettings;
import org.guideme.guideme.util.XMLReaderUtils;

public class SettingsHandler {
	public static void handle(XMLStreamReader reader, Guide guide, GuideSettings guideSettings)
			throws XMLStreamException {
		int depth=1;
		while (depth>0) {
			int eventType2 = reader.next();
			if (eventType2 == XMLStreamConstants.START_ELEMENT) {
				depth++;
				String tagName = reader.getName().getLocalPart();
				switch (tagName) {
				case "AutoSetPageWhenSeen":
					guide.setAutoSetPage(getStringContentOrDefault(reader, true));
					break;
				case "PageSound":
					guideSettings.setPageSound(getStringContentOrDefault(reader, true));
					break;
				case "ForceStartPage":
					guideSettings.setForceStartPage(getStringContentOrDefault(reader, false));
					break;
				case "ConvertArgumentTypes":
					guideSettings.setConvertArgumentTypes(getStringContentOrDefault(reader, false));
					break;
				default:
					UserErrorManager.log("Unrecognized setting '" + tagName + "'", reader.getLocation());
					/* Consume XML until the end of the unhandled tag */
					XMLReaderUtils.getStringContentUntilElementEnd(reader);
					break;
				}
				eventType2 = reader.next();
				if (isAtElementEnd(reader, "Settings")) {
					return;
				}
			}
			if(eventType2 == XMLStreamConstants.END_ELEMENT) {
				depth--;
			}
		}
	}
}
