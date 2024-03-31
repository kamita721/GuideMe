package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;

// Restart
class FileRestartListener extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	FileRestartListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// File Restart From the menu
	// will restart the Guide from the start page
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (mainShell.appSettings.isFileActionConfirmations()) {
			MessageBox dialog = new MessageBox(mainShell.shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			dialog.setMessage("Do you really want to restart the guide?\n(All current status will be lost)");
			int returnCode = dialog.open();
			if (returnCode == SWT.CANCEL)
				return;
		}
		try {
			MainShell.LOGGER.trace("Enter Menu Restart");
			// stop all activity for the current page to prevent timers
			// jumping to a different page
			mainShell.stopAll(false);
			mainShell.guide.restart();
			mainShell.guideSettings = mainShell.guide.getSettings();
			mainShell.debugShell.clearJConsole();
			mainShell.mainLogic.displayPage("start", false, mainShell.guide, mainShell, mainShell.appSettings, mainShell.userSettings, mainShell.guideSettings,
					mainShell.debugShell);
		} catch (Exception ex) {
			MainShell.LOGGER.error("Restart error " + ex.getLocalizedMessage(), ex);
		}
		MainShell.LOGGER.trace("Exit Menu Restart");
		super.widgetSelected(e);
	}

}