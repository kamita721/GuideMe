package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.guideme.guideme.ui.LibraryShell;

class FileLibraryListener extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	FileLibraryListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File Library from menu
	@Override
	public void widgetSelected(SelectionEvent e) {
		try {
			MainShell.logger.trace("Enter FileLibraryListener");
			// Display a modal shell for the guide specific preferences
			Shell libShell = new LibraryShell().createShell(this.mainShell.myDisplay, this.mainShell.appSettings, this.mainShell.mainShell);
			libShell.open();
			while (!libShell.isDisposed()) {
				if (!this.mainShell.myDisplay.readAndDispatch())
					this.mainShell.myDisplay.sleep();
			}
		} catch (Exception ex) {
			MainShell.logger.error(" FileLibraryListener " + ex.getLocalizedMessage());
			this.mainShell.inPrefShell = false;
		}
		super.widgetSelected(e);
	}

}