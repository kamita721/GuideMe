package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.guideme.guideme.ui.GuidePreferenceShell;

class FileGuidePreferences extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	FileGuidePreferences(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File Guide Preferences from menu
	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			MainShell.LOGGER.trace("Enter FileGuidePreferences");
			// Display a modal shell for the guide specific preferences
			this.mainShell.guideSettings = this.mainShell.guide.getSettings();
			this.mainShell.inPrefShell = true;
			Shell prefShell = new GuidePreferenceShell().createShell(this.mainShell.myDisplay, this.mainShell.guideSettings, this.mainShell.appSettings);
			prefShell.open();
			while (!prefShell.isDisposed()) {
				if (!this.mainShell.myDisplay.readAndDispatch())
					this.mainShell.myDisplay.sleep();
			}
			this.mainShell.inPrefShell = false;
		} catch (Exception ex) {
			MainShell.LOGGER.error(" FileGuidePreferences " + ex.getLocalizedMessage());
			this.mainShell.inPrefShell = false;
		}
		super.widgetSelected(e);
	}

}