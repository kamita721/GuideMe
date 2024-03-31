package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

class UnCompressGuideListener extends SelectionAdapter {
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	UnCompressGuideListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File UnCompressGuide from the menu
	@Override
	public void widgetSelected(SelectionEvent e) {
		LOGGER.trace("Enter Menu UnCompressGuide");
		// display a dialog to ask for a guide file to play
		FileDialog dialog = new FileDialog(this.mainShell.shell, SWT.OPEN);
		String[] filterNames = new String[] { "ZIP Files" };
		String[] filterExtensions = new String[] { "*.zip" };
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(this.mainShell.appSettings.getDataDirectory());
		String strFileToLoad;
		strFileToLoad = dialog.open();
		if (strFileToLoad != null) {
			// if a guide file has been chosen load it
			// default the initial directory for future loads to
			// the current one
			this.mainShell.strGuidePath = dialog.getFilterPath()
					+ this.mainShell.appSettings.getFileSeparator();
			this.mainShell.appSettings.setDataDirectory(this.mainShell.strGuidePath);
			// load the file it will return the start page and
			// populate the guide object
			this.mainShell.comonFunctions.unCompressGuide(strFileToLoad);
		}

		LOGGER.trace("Exit Menu UnCompressGuide");
		super.widgetSelected(e);
	}

}