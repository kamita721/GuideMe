package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

class UnCompressGuideListener extends SelectionAdapter {
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
		try {
			MainShell.logger.trace("Enter Menu UnCompressGuide");
			// display a dialog to ask for a guide file to play
			FileDialog dialog = new FileDialog(this.mainShell.shell, SWT.OPEN);
			String[] filterNames = new String[] { "ZIP Files" };
			String[] filterExtensions = new String[] { "*.zip" };
			dialog.setFilterNames(filterNames);
			dialog.setFilterExtensions(filterExtensions);
			dialog.setFilterPath(this.mainShell.appSettings.getDataDirectory());
			String strFileToLoad;
			try {
				strFileToLoad = dialog.open();
				try {
					if (strFileToLoad != null) {
						// if a guide file has been chosen load it
						// default the initial directory for future loads to
						// the current one
						this.mainShell.strGuidePath = dialog.getFilterPath() + this.mainShell.appSettings.getFileSeparator();
						this.mainShell.appSettings.setDataDirectory(this.mainShell.strGuidePath);
						// load the file it will return the start page and
						// populate the guide object
						this.mainShell.comonFunctions.UnCompressGuide(strFileToLoad);
					}
				} catch (Exception ex5) {
					MainShell.logger.error("Process Image error " + ex5.getLocalizedMessage(), ex5);
				}
			} catch (Exception ex4) {
				MainShell.logger.error("Load Image Dialogue error " + ex4.getLocalizedMessage(), ex4);
			}
		} catch (Exception ex3) {
			MainShell.logger.error("Load Image error " + ex3.getLocalizedMessage(), ex3);
		}
		MainShell.logger.trace("Exit Menu UnCompressGuide");
		super.widgetSelected(e);
	}

}