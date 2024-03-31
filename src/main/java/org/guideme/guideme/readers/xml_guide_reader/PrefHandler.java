package org.guideme.guideme.readers.xml_guide_reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.guideme.generated.model.Pref;
import org.guideme.guideme.settings.GuideSettings;

public class PrefHandler {
	private static Logger LOGGER = LogManager.getLogger();
	
	private PrefHandler() {
	}

	public static void handle(Pref pref, GuideSettings guideSettings) {

		int sortOrder = 0;
		
		String key = pref.getKey();
		String type = pref.getType();
		String screen = pref.getScreen();
		String value = pref.getValue();
		String order = pref.getSortOrder();
		try {
			sortOrder = Integer.parseInt(order);
		} catch (NumberFormatException ex) {
			LOGGER.warn("Malformed sortOrder '{}' in pref field of guide xml.", order);
			sortOrder = 0;
		}
		if (!guideSettings.keyExists(key, type)) {
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
