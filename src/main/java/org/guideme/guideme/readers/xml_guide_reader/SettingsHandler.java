package org.guideme.guideme.readers.xml_guide_reader;


import org.guideme.generated.model.Settings;
import org.guideme.guideme.model.Guide;
import org.guideme.guideme.settings.GuideSettings;

public class SettingsHandler {
	private SettingsHandler() {
	}

	public static void handle(Settings settings, Guide guide, GuideSettings guideSettings) {
		guide.setAutoSetPage(settings.getAutoSetPageWhenSeen());
		guideSettings.setPageSound(settings.getPageSound());
		guideSettings.setForceStartPage(settings.getForceStartPage());
		guideSettings.setConvertArgumentTypes(settings.getConvertArgumentTypes());
	}
}
