package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

// Click event code for the dynamic buttons
class DynamicButtonListner extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	DynamicButtonListner(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	public void widgetSelected(SelectionEvent event) {
		if (this.mainShell.pauseRequested) {
			return;
		}
		try {

			MainShell.logger.trace("Enter DynamicButtonListner");
			String strTag;
			com.snapps.swt.SquareButton btnClicked = (com.snapps.swt.SquareButton) event.widget;
			strTag = (String) btnClicked.getData("Set");
			if (!strTag.equals("")) {
				this.mainShell.comonFunctions.SetFlags(strTag, this.mainShell.guide.getFlags());
			}
			strTag = (String) btnClicked.getData("UnSet");
			if (!strTag.equals("")) {
				this.mainShell.comonFunctions.UnsetFlags(strTag, this.mainShell.guide.getFlags());
			}
			String scriptVar = (String) btnClicked.getData("scriptVar");
			this.mainShell.comonFunctions.processSrciptVars(scriptVar, this.mainShell.guideSettings);
			strTag = (String) btnClicked.getData("Target");
			String javascript = (String) btnClicked.getData("javascript");
			this.mainShell.runJscript(javascript, false);
			if (!strTag.equals("")) {
				this.mainShell.mainLogic.displayPage(strTag, false, this.mainShell.guide, this.mainShell.mainShell, this.mainShell.appSettings, this.mainShell.userSettings, this.mainShell.guideSettings,
						this.mainShell.debugShell);
			}
		} catch (Exception ex) {
			MainShell.logger.error(" DynamicButtonListner " + ex.getLocalizedMessage(), ex);
		}
		MainShell.logger.trace("Exit DynamicButtonListner");
	}
}