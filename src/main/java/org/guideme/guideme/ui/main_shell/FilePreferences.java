package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.guideme.guideme.ui.preference_shell.PreferenceShell;

class FilePreferences extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	FilePreferences(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File Preferences from the menu
	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			MainShell.LOGGER.trace("Enter FilePreferences");
			// display a modal shell to change the preferences
			this.mainShell.inPrefShell = true;
			Shell prefShell = new PreferenceShell().createShell(this.mainShell.myDisplay, this.mainShell.appSettings);
			prefShell.open();
			while (!prefShell.isDisposed()) {
				if (!this.mainShell.myDisplay.readAndDispatch())
					this.mainShell.myDisplay.sleep();
			}
			this.mainShell.inPrefShell = false;
		} catch (Exception ex) {
			MainShell.LOGGER.error(" FilePreferences " + ex.getLocalizedMessage());
			this.mainShell.inPrefShell = false;
		}
		super.widgetSelected(e);
	}

}