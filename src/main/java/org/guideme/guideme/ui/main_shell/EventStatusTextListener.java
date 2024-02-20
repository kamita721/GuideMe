package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.browser.StatusTextEvent;
import org.eclipse.swt.browser.StatusTextListener;

// Is triggered by Java script writing to the Status Text
// Checks the text for "commands" that GuideMe understands
// Original implementation is to move to HTML buttons where the onclick
// javascript needs to trigger Guideme
// Needs a pipe | separated string containing the command and parameters
// ButtonClick|Set|UnSet|scriptVar|target|javaScript|filler
// The filler item is not strictly required, but commands without a
// javaScript item will require something
// in there due to how the split function works. It's contents are ignored.
// If the exact same command string is used repeatedly, it will be ignored.
// To prevent this, use varying text in the filler, such as the time or a
// counter to the command string.
class EventStatusTextListener implements StatusTextListener {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	EventStatusTextListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void changed(StatusTextEvent event) {
		if (!mainShell.procStatusText.equals(event.text)) {
			String statusText = event.text;

			String[] eventArgs = statusText.split("\\|");
			if (eventArgs[0].equals("ButtonClick") && eventArgs.length > 5) {
				mainShell.procStatusText = event.text;

				MainShell.logger.trace("Enter StatusTextListener");
				String strTag;
				strTag = eventArgs[1];// Set
				if (!strTag.equals("")) {
					mainShell.comonFunctions.setFlags(strTag, mainShell.guide.getFlags());
				}
				strTag = eventArgs[2];// UnSet
				if (!strTag.equals("")) {
					mainShell.comonFunctions.unsetFlags(strTag, mainShell.guide.getFlags());
				}
				String scriptVar = eventArgs[3];// scriptVar
				mainShell.comonFunctions.processSrciptVars(scriptVar, mainShell.guideSettings);
				strTag = eventArgs[4];// Target
				String javascript = eventArgs[5];// javaScript
				mainShell.runJscript(javascript, false);
				if (!strTag.equals(""))
					mainShell.mainLogic.displayPage(strTag, false, mainShell.guide, mainShell, mainShell.appSettings,
							mainShell.userSettings, mainShell.guideSettings, mainShell.debugShell);

				MainShell.logger.trace("Exit StatusTextListener");
			}
		}
	}
}