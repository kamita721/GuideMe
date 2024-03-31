package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

class ResizeGuideListener extends SelectionAdapter {
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	ResizeGuideListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File CompressGuide from the menu
	@Override
	public void widgetSelected(SelectionEvent e) {
		LOGGER.trace("Enter Menu CompressGuide");
		// display a dialog to ask for a guide file to play
		FileDialog dialog = new FileDialog(this.mainShell.shell, SWT.OPEN);
		String[] filterNames = new String[] { "XML Files" };
		String[] filterExtensions = new String[] { "*.xml" };
		dialog.setFilterNames(filterNames);
		dialog.setFilterExtensions(filterExtensions);
		dialog.setFilterPath(this.mainShell.appSettings.getDataDirectory());
		String strFileToLoad = dialog.open();
		if (strFileToLoad != null) {
			// if a guide file has been chosen load it
			// default the initial directory for future loads to
			// the current one
			this.mainShell.strGuidePath = dialog.getFilterPath()
					+ this.mainShell.appSettings.getFileSeparator();
			this.mainShell.appSettings.setDataDirectory(this.mainShell.strGuidePath);
			// load the file it will return the start page and
			// populate the guide object
			this.mainShell.comonFunctions.resizeGuideImages(strFileToLoad, mainShell.imageManager);
		}
		LOGGER.trace("Exit Menu CompressGuide");
		super.widgetSelected(e);
	}

}