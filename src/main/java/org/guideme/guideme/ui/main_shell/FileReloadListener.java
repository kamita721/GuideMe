package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;

class FileReloadListener extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	FileReloadListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (this.mainShell.appSettings.isFileActionConfirmations()) {
			MessageBox dialog = new MessageBox(this.mainShell.shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
			dialog.setMessage("Do you really want to reload the guide?\n(All current status will be lost)");
			int returnCode = dialog.open();
			if (returnCode == SWT.CANCEL)
				return;
		}
		this.mainShell.loadGuide(this.mainShell.guideFile);
	}
}