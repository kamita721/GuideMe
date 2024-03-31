package org.guideme.guideme.readers.xml_guide_reader;

import org.guideme.generated.model.BasicButton;
import org.guideme.generated.model.Page;
import org.guideme.generated.model.Tease;
import org.guideme.generated.model.Text;
import org.guideme.guideme.model.Chapter;
import org.guideme.guideme.scripting.functions.ComonFunctions;
import org.guideme.guideme.settings.GuideSettings;

import com.fasterxml.jackson.core.Version;

public class TeaseHandler {
	private TeaseHandler() {
	}

	public static void handle(Tease tease, Chapter chapter, GuideSettings guideSettings) {
		String currentVersionString = ComonFunctions.getVersion();
		String minVersionString = tease.getMinimumVersion();
		if (minVersionString.equals("")) {
			return;
		}

		String[] minVersionParts = minVersionString.split("\\.");
		String[] currentVersionParts = currentVersionString.split("\\.");
		if (minVersionParts.length != 3 || currentVersionParts.length != 3) {
			return;
		}

		Version minVersion = new Version(Integer.parseUnsignedInt(minVersionParts[0]),
				Integer.parseUnsignedInt(minVersionParts[1]),
				Integer.parseUnsignedInt(minVersionParts[2]), "", null, null);
		Version currentVersion = new Version(Integer.parseUnsignedInt(currentVersionParts[0]),
				Integer.parseUnsignedInt(currentVersionParts[1]),
				Integer.parseUnsignedInt(currentVersionParts[2]), "", null, null);

		if (currentVersion.compareTo(minVersion) < 0) {

			Page pageVersionNotMet = new Page();
			pageVersionNotMet.setId("GuideMeVersionNotMet");
			String nextPage = guideSettings.getPage().equals(pageVersionNotMet.getId()) ? "start"
					: guideSettings.getPage();
			BasicButton toAdd = new BasicButton();
			toAdd.setTarget(nextPage);
			toAdd.setText("Continue");
			pageVersionNotMet.addButton(toAdd);

			Text textToAdd = new Text();
			textToAdd.setText("<p><h1>App Version Not Supported</h1>"
					+ "<p>This tease specifies a minimum GuideMe version of " + minVersionString
					+ " but you're running " + currentVersionString + "."
					+ "<br/>Please consider updating or this tease may not function as designed.</p>");

			pageVersionNotMet.addLeftText(textToAdd);
			
			chapter.addPage(pageVersionNotMet);
			guideSettings.setPage(pageVersionNotMet.getId());
		}
	}

}
