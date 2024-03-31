package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Shell;
import org.guideme.guideme.ui.LibraryShell;

class FileLibraryListener extends SelectionAdapter {
	private static final Logger LOGGER = LogManager.getLogger();

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
		LOGGER.trace("Enter FileLibraryListener");
		// Display a modal shell for the guide specific preferences
		Shell libShell = new LibraryShell().createShell(mainShell.myDisplay, mainShell.appSettings,
				mainShell);
		libShell.open();
		while (!libShell.isDisposed()) {
			if (!mainShell.myDisplay.readAndDispatch())
				mainShell.myDisplay.sleep();
		}
		super.widgetSelected(e);
	}

}