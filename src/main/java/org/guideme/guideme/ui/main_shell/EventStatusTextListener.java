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
		if (!this.mainShell.ProcStatusText.equals(event.text)) {
			String statusText = event.text;

			String[] eventArgs = statusText.split("\\|");
			if (eventArgs[0].equals("ButtonClick") && eventArgs.length > 5) {
				this.mainShell.ProcStatusText = event.text;
				try {
					MainShell.logger.trace("Enter StatusTextListener");
					String strTag;
					strTag = eventArgs[1];// Set
					if (!strTag.equals("")) {
						this.mainShell.comonFunctions.setFlags(strTag, this.mainShell.guide.getFlags());
					}
					strTag = eventArgs[2];// UnSet
					if (!strTag.equals("")) {
						this.mainShell.comonFunctions.unsetFlags(strTag, this.mainShell.guide.getFlags());
					}
					String scriptVar = eventArgs[3];// scriptVar
					this.mainShell.comonFunctions.processSrciptVars(scriptVar, this.mainShell.guideSettings);
					strTag = eventArgs[4];// Target
					String javascript = eventArgs[5];// javaScript
					this.mainShell.runJscript(javascript, false);
					if (!strTag.equals(""))
						this.mainShell.mainLogic.displayPage(strTag, false, this.mainShell.guide, this.mainShell.mainShell, this.mainShell.appSettings, this.mainShell.userSettings,
								this.mainShell.guideSettings, this.mainShell.debugShell);
				} catch (Exception ex) {
					MainShell.logger.error(" StatusTextListener " + ex.getLocalizedMessage(), ex);
				}
				MainShell.logger.trace("Exit StatusTextListener");
			}
		}
	}
}