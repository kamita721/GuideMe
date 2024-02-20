package org.guideme.guideme.ui.main_shell;

import java.io.File;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.guideme.guideme.readers.xml_guide_reader.XmlGuideReader;

class FilePreferencesGuide extends SelectionAdapter {
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
		try {
			MainShell.logger.trace("Enter Preferences Guide Load");
			// special guide for user preferences
			// this loads automatically from the application directory with
			// a hard coded name.
			//
			mainShell.debugShell.clearPagesCombo();
			String appDir = mainShell.appSettings.getUserDir().replace("\\", "\\\\");
			String fileName = "userSettingsUI_" + mainShell.appSettings.getLanguage() + "_" + mainShell.appSettings.getCountry()
					+ ".xml";
			File f = new File(appDir + mainShell.appSettings.getFileSeparator() + fileName);
			if (!f.exists()) {
				fileName = "userSettingsUI_" + mainShell.appSettings.getLanguage() + ".xml";
				f = new File(appDir + mainShell.appSettings.getFileSeparator() + fileName);
				if (!f.exists()) {
					fileName = "userSettingsUI.xml";
				}
			}
			XmlGuideReader.loadXML(fileName, mainShell.guide, mainShell.appSettings, mainShell.debugShell);
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
			mainShell.mainLogic.displayPage("start", false, mainShell.guide, mainShell, mainShell.appSettings, mainShell.userSettings, mainShell.guideSettings,
					mainShell.debugShell);
		} catch (Exception ex3) {
			MainShell.logger.error("Load Image error " + ex3.getLocalizedMessage(), ex3);
		}
		MainShell.logger.trace("Exit Preferences Guide Load");
		super.widgetSelected(e);
	}

}