package org.guideme.guideme.ui.main_shell;

import java.util.HashMap;

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
		if (this.mainShell.appSettings.isFileActionConfirmations()) {
			MessageBox dialog = new MessageBox(this.mainShell.shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			dialog.setMessage("Do you really want to restart the guide?\n(All current status will be lost)");
			int returnCode = dialog.open();
			if (returnCode == SWT.CANCEL)
				return;
		}
		try {
			MainShell.logger.trace("Enter Menu Restart");
			// stop all activity for the current page to prevent timers
			// jumping to a different page
			this.mainShell.stopAll(false);
			this.mainShell.guide.getFlags().clear();
			this.mainShell.guide.getSettings().setPage("start");
			this.mainShell.guide.getSettings().setFlags(this.mainShell.comonFunctions.getFlags(this.mainShell.guide.getFlags()));
			// guide.getSettings().setScope(null);
			HashMap<String, Object> scriptVariables = new HashMap<String, Object>();
			this.mainShell.guide.getSettings().setScriptVariables(scriptVariables);
			this.mainShell.guide.getSettings().setScope(null);
			this.mainShell.guide.getSettings().saveSettings();
			this.mainShell.guideSettings = this.mainShell.guide.getSettings();
			this.mainShell.debugShell.clearJConsole();
			this.mainShell.mainLogic.displayPage("start", false, this.mainShell.guide, this.mainShell.mainShell, this.mainShell.appSettings, this.mainShell.userSettings, this.mainShell.guideSettings,
					this.mainShell.debugShell);
		} catch (Exception ex) {
			MainShell.logger.error("Restart error " + ex.getLocalizedMessage(), ex);
		}
		MainShell.logger.trace("Exit Menu Restart");
		super.widgetSelected(e);
	}

}