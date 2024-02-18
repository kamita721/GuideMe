package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

// hotkey stuff here
class shellKeyEventListener implements Listener {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	shellKeyEventListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void handleEvent(Event event) {
		try {
			if (event.display.getActiveShell().getText().equals(this.mainShell.shell.getText())) {

				MainShell.logger.trace(
						event.character + "|" + event.keyCode + "|" + event.keyLocation + "|" + event.stateMask);
				if (event.keyCode == 13
						&& (event.widget.getClass().toString().equals("class org.eclipse.swt.browser.Browser"))) {
					event.doit = false;
				}
				;
				if (((event.stateMask & SWT.ALT) == SWT.ALT)) {
					switch (event.character) {
					/*
					 * case 'd' : shell3.setVisible(!shell3.getVisible());
					 * if (shell3.isVisible()) { shell3.setActive(); }
					 * break;
					 */
					case 'm':
					case 'M':
						Rectangle rect = this.mainShell.shell.getBounds();
						Rectangle rect2;
						if (!this.mainShell.showMenu) {
							if (!this.mainShell.shell.isDisposed()) {
								this.mainShell.shell.setMenuBar(this.mainShell.MenuBar);
								this.mainShell.shell.pack();
								// shell.setMaximized(true);
								this.mainShell.shell.setBounds(rect);
							}
							if (this.mainShell.multiMonitor) {
								if (!this.mainShell.shell2.isDisposed()) {
									rect2 = this.mainShell.shell2.getBounds();
									this.mainShell.shell2.pack();
									// shell2.setMaximized(true);
									this.mainShell.shell2.setBounds(rect2);
								}
							}
							this.mainShell.showMenu = true;
						} else {
							if (!this.mainShell.shell.isDisposed()) {
								this.mainShell.shell.setMenuBar(null);
								this.mainShell.shell.pack();
								// shell.setMaximized(true);
								this.mainShell.shell.setBounds(rect);
							}
							if (this.mainShell.multiMonitor) {
								if (!this.mainShell.shell2.isDisposed()) {
									rect2 = this.mainShell.shell2.getBounds();
									this.mainShell.shell2.pack();
									// shell2.setMaximized(true);
									this.mainShell.shell2.setBounds(rect2);
								}
							}
							this.mainShell.showMenu = false;
						}
						break;
					}
					/*
					 * if (comonFunctions.onWindows() && event.character !=
					 * "d".charAt(0)) { //ignore } else {
					 * shell3.setVisible(!shell3.getVisible()); if
					 * (shell3.isVisible()) { shell3.setActive(); } }
					 */
				} else {
					com.snapps.swt.SquareButton hotKeyButton;
					String key = String.valueOf(event.character);
					hotKeyButton = this.mainShell.hotKeys.get(key);
					if (hotKeyButton != null) {
						String strTag;
						strTag = (String) hotKeyButton.getData("Set");
						if (!strTag.equals("")) {
							this.mainShell.comonFunctions.SetFlags(strTag, this.mainShell.guide.getFlags());
						}
						strTag = (String) hotKeyButton.getData("UnSet");
						if (!strTag.equals("")) {
							this.mainShell.comonFunctions.UnsetFlags(strTag, this.mainShell.guide.getFlags());
						}
						strTag = (String) hotKeyButton.getData("Target");
						String javascript = (String) hotKeyButton.getData("javascript");
						this.mainShell.runJscript(javascript, false);
						if (!strTag.equals("")) {
							this.mainShell.mainLogic.displayPage(strTag, false, this.mainShell.guide, this.mainShell.mainShell, this.mainShell.appSettings, this.mainShell.userSettings,
									this.mainShell.guideSettings, this.mainShell.debugShell);
						}
					}
				}
			}
		} catch (Exception ex) {
			MainShell.logger.error(" hot key " + ex.getLocalizedMessage(), ex);
		}
	}
}