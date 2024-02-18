package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;

class FileLoadListener extends SelectionAdapter {
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
		try {
			MainShell.logger.trace("Enter Menu Load");
			// display a dialog to ask for a guide file to play
			FileDialog dialog = new FileDialog(this.mainShell.shell, SWT.OPEN);
			// TODO Need to change this here to implement the new html
			// format
			String[] filterNames = new String[] { "XML Files" };
			String[] filterExtensions = new String[] { "*.xml" };
			dialog.setFilterNames(filterNames);
			dialog.setFilterExtensions(filterExtensions);
			dialog.setFilterPath(this.mainShell.appSettings.getDataDirectory());
			try {
				this.mainShell.guideFile = dialog.open();
				try {
					if (this.mainShell.guideFile != null) {
						// if a guide file has been chosen load it
						// default the initial directory for future loads to
						// the current one
						this.mainShell.strGuidePath = dialog.getFilterPath() + this.mainShell.appSettings.getFileSeparator();
						this.mainShell.appSettings.setDataDirectory(this.mainShell.strGuidePath);
						// load the file it will return the start page and
						// populate the guide object
						// TODO Need to change this here to implement the
						// new html format
						this.mainShell.loadGuide(this.mainShell.guideFile);
						this.mainShell.debugShell.clearJConsole();
					}
				} catch (Exception ex5) {
					MainShell.logger.error("Load Guide " + ex5.getLocalizedMessage(), ex5);
				}
			} catch (Exception ex4) {
				MainShell.logger.error("Load Guide Dialogue error " + ex4.getLocalizedMessage(), ex4);
			}
		} catch (Exception ex3) {
			MainShell.logger.error("Load Guide error " + ex3.getLocalizedMessage(), ex3);
		}
		MainShell.logger.trace("Exit Menu Load");
		super.widgetSelected(e);
	}

}