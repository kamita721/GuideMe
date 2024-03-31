package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

// Click event code for the dynamic buttons
class DynamicButtonListner extends SelectionAdapter {
	private static final Logger LOGGER = LogManager.getLogger();
	
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

	@Override
	public void widgetSelected(SelectionEvent event) {
		if (mainShell.pauseRequested) {
			return;
		}

		LOGGER.trace("Enter DynamicButtonListner");
		String strTag;
		com.snapps.swt.SquareButton btnClicked = (com.snapps.swt.SquareButton) event.widget;
		strTag = (String) btnClicked.getData("Set");
		if (!strTag.equals("")) {
			mainShell.comonFunctions.setFlags(strTag, mainShell.guide.getFlags());
		}
		strTag = (String) btnClicked.getData("UnSet");
		if (!strTag.equals("")) {
			mainShell.comonFunctions.unsetFlags(strTag, mainShell.guide.getFlags());
		}
		String scriptVar = (String) btnClicked.getData("scriptVar");
		mainShell.comonFunctions.processSrciptVars(scriptVar, mainShell.guideSettings);
		strTag = (String) btnClicked.getData("Target");
		String javascript = (String) btnClicked.getData("javascript");
		mainShell.runJscript(javascript, false);
		if (!strTag.equals("")) {
			mainShell.mainLogic.displayPage(strTag, false, mainShell.guide, mainShell,
					mainShell.appSettings, mainShell.userSettings, mainShell.guideSettings,
					mainShell.debugShell);
		}
		LOGGER.trace("Exit DynamicButtonListner");
	}
}