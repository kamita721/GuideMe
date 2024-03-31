package org.guideme.guideme.ui.main_shell;

import java.io.File;
import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;
import org.guideme.guideme.util.ErrorManager;

class FilePreferencesGuide extends SelectionAdapter {
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	FilePreferencesGuide(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File Preferences from the menu
	@Override
	public void widgetSelected(SelectionEvent e) {
		LOGGER.trace("Enter Preferences Guide Load");
		// special guide for user preferences
		// this loads automatically from the application directory with
		// a hard coded name.
		//
		mainShell.debugShell.clearPagesCombo();
		String appDir = mainShell.appSettings.getUserDir().replace("\\", "\\\\");
		String fileName = "userSettingsUI_" + mainShell.appSettings.getLanguage() + "_"
				+ mainShell.appSettings.getCountry() + ".xml";
		File f = new File(appDir + mainShell.appSettings.getFileSeparator() + fileName);
		if (!f.exists()) {
			fileName = "userSettingsUI_" + mainShell.appSettings.getLanguage() + ".xml";
			f = new File(appDir + mainShell.appSettings.getFileSeparator() + fileName);
			if (!f.exists()) {
				fileName = "userSettingsUI.xml";
			}
		}
		try {
			XmlGuideReader.loadXML(fileName, mainShell.guide, mainShell.appSettings,
					mainShell.debugShell);
		} catch (XMLStreamException | IOException e1) {
			ErrorManager.getInstance().recordError(e1, "Could not load Guide " + fileName);
		}

		mainShell.guide.setMediaDirectory("userSettings");
		mainShell.guideSettings = mainShell.guide.getSettings();
		if (mainShell.guide.getCss().equals("")) {
			mainShell.style = mainShell.defaultStyle;
		} else {
			mainShell.style = mainShell.guide.getCss();
		}
		// flag to allow updating of user preferences which is normally
		// disabled in guides
		mainShell.guide.setInPrefGuide(true);
		// display the first page
		mainShell.mainLogic.displayPage("start", false, mainShell.guide, mainShell,
				mainShell.appSettings, mainShell.userSettings, mainShell.guideSettings,
				mainShell.debugShell);

		LOGGER.trace("Exit Preferences Guide Load");
		super.widgetSelected(e);
	}

}