package org.guideme.guideme.readers.xml_guide_reader;

import javax.xml.stream.XMLStreamReader;

import org.guideme.guideme.settings.GuideSettings;

public class PrefHandler {
	public static void handle(XMLStreamReader reader, GuideSettings guideSettings) {
		String key;
		String screen = "";
		String type;
		String value = "";
		String order = "";
		int sortOrder = 0;
		key = reader.getAttributeValue(null, "key");
		type = reader.getAttributeValue(null, "type");
		order = reader.getAttributeValue(null, "sortOrder");
		try {
			sortOrder = Integer.parseInt(order);
		}
		catch (Exception ex) {
			sortOrder = 0;
		}
		if (! guideSettings.keyExists(key, type)) {
			screen = reader.getAttributeValue(null, "screen");
			value = reader.getAttributeValue(null, "value");
			if (type.equals("String")) {
				guideSettings.addPref(key, value, screen, sortOrder);
			}
			if (type.equals("Boolean")) {
				guideSettings.addPref(key, Boolean.parseBoolean(value), screen, sortOrder);
			}
			if (type.equals("Number")) {
				guideSettings.addPref(key, Double.parseDouble(value), screen, sortOrder);
			}
		} else {
			guideSettings.setPrefOrder(key, sortOrder);
		}
	}
}
