package org.guideme.guideme.ui.main_shell;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

// hotkey stuff here
class ShellKeyEventListener implements Listener {
	private static final Logger LOGGER = LogManager.getLogger();
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	ShellKeyEventListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void handleEvent(Event event) {
		if (!event.display.getActiveShell().getText().equals(mainShell.shell.getText())) {
			return;
		}

		LOGGER.trace("{}|{}|{}|{}", event.character, event.keyCode, event.keyLocation, event.stateMask);
		if (event.keyCode == 13
				&& (event.widget.getClass().toString().equals("class org.eclipse.swt.browser.Browser"))) {
			event.doit = false;
		}

		if (((event.stateMask & SWT.ALT) == SWT.ALT)) {
			if (event.character == 'm' || event.character == 'M') {
				altM();
			}
		} else {
			handleHotkeys(event.character);
		}
	}

	private void handleHotkeys(char character) {
		com.snapps.swt.SquareButton hotKeyButton;
		String key = String.valueOf(character);
		hotKeyButton = mainShell.hotKeys.get(key);
		if (hotKeyButton != null) {
			String strTag;
			strTag = (String) hotKeyButton.getData("Set");
			if (!strTag.equals("")) {
				mainShell.comonFunctions.setFlags(strTag, mainShell.guide.getFlags());
			}
			strTag = (String) hotKeyButton.getData("UnSet");
			if (!strTag.equals("")) {
				mainShell.comonFunctions.unsetFlags(strTag, mainShell.guide.getFlags());
			}
			strTag = (String) hotKeyButton.getData("Target");
			String javascript = (String) hotKeyButton.getData("javascript");
			mainShell.runJscript(javascript, false);
			if (!strTag.equals("")) {
				mainShell.mainLogic.displayPage(strTag, false, mainShell.guide, mainShell, mainShell.appSettings,
						mainShell.userSettings, mainShell.guideSettings, mainShell.debugShell);
			}
		}
	}

	private void altM() {
		Rectangle rect = mainShell.shell.getBounds();
		Rectangle rect2;
		if (!mainShell.showMenu) {
			if (!mainShell.shell.isDisposed()) {
				mainShell.shell.setMenuBar(mainShell.menuBar);
				mainShell.shell.pack();
				mainShell.shell.setBounds(rect);
			}
			if (mainShell.multiMonitor && (!mainShell.shell2.isDisposed())) {
				rect2 = mainShell.shell2.getBounds();
				mainShell.shell2.pack();
				mainShell.shell2.setBounds(rect2);

			}
			mainShell.showMenu = true;
		} else {
			if (!mainShell.shell.isDisposed()) {
				mainShell.shell.setMenuBar(null);
				mainShell.shell.pack();
				mainShell.shell.setBounds(rect);
			}
			if (mainShell.multiMonitor && (!mainShell.shell2.isDisposed())) {
				rect2 = mainShell.shell2.getBounds();
				mainShell.shell2.pack();
				mainShell.shell2.setBounds(rect2);

			}
			mainShell.showMenu = false;
		}
	}
}