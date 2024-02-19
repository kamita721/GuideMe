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
			this.mainShell.debugShell.clearPagesCombo();
			String appDir = this.mainShell.appSettings.getUserDir().replace("\\", "\\\\");
			String fileName = "userSettingsUI_" + this.mainShell.appSettings.getLanguage() + "_" + this.mainShell.appSettings.getCountry()
					+ ".xml";
			File f = new File(appDir + this.mainShell.appSettings.getFileSeparator() + fileName);
			if (!f.exists()) {
				fileName = "userSettingsUI_" + this.mainShell.appSettings.getLanguage() + ".xml";
				f = new File(appDir + this.mainShell.appSettings.getFileSeparator() + fileName);
				if (!f.exists()) {
					fileName = "userSettingsUI.xml";
				}
			}
			XmlGuideReader.loadXML(fileName, this.mainShell.guide, this.mainShell.appSettings, this.mainShell.debugShell);
			this.mainShell.guide.setMediaDirectory("userSettings");
			this.mainShell.guideSettings = this.mainShell.guide.getSettings();
			if (this.mainShell.guide.getCss().equals("")) {
				this.mainShell.style = this.mainShell.defaultStyle;
			} else {
				this.mainShell.style = this.mainShell.guide.getCss();
			}
			// flag to allow updating of user preferences which is normally
			// disabled in guides
			this.mainShell.guide.setInPrefGuide(true);
			// display the first page
			this.mainShell.mainLogic.displayPage("start", false, this.mainShell.guide, this.mainShell.mainShell, this.mainShell.appSettings, this.mainShell.userSettings, this.mainShell.guideSettings,
					this.mainShell.debugShell);
		} catch (Exception ex3) {
			MainShell.logger.error("Load Image error " + ex3.getLocalizedMessage(), ex3);
		}
		MainShell.logger.trace("Exit Preferences Guide Load");
		super.widgetSelected(e);
	}

}