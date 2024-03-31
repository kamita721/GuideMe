package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

class FileLoadListener extends SelectionAdapter {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	FileLoadListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File Load from the menu
	@Override
	public void widgetSelected(SelectionEvent e) {
		LOGGER.trace("Enter Menu Load");
		// display a dialog to ask for a guide file to play
		FileDialog dialog = new FileDialog(this.mainShell.shell, SWT.OPEN);
		// TODO Need to change this here to implement the new html
		// format
		String[] filterNames = new String[] { "XML Files" };
		String[] filterExtensions = new String[] { "*.xml" };
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(this.mainShell.appSettings.getDataDirectory());

		mainShell.guideFile = dialog.open();

		if (mainShell.guideFile != null) {
			// if a guide file has been chosen load it
			// default the initial directory for future loads to
			// the current one
			mainShell.strGuidePath = dialog.getFilterPath()
					+ mainShell.appSettings.getFileSeparator();
			mainShell.appSettings.setDataDirectory(mainShell.strGuidePath);
			// load the file it will return the start page and
			// populate the guide object
			// TODO Need to change this here to implement the
			// new html format
			mainShell.loadGuide(mainShell.guideFile);
			mainShell.debugShell.clearJConsole();
		}

		LOGGER.trace("Exit Menu Load");
		super.widgetSelected(e);
	}

}