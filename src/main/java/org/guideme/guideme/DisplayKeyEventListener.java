package org.guideme.guideme;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.guideme.guideme.ui.main_shell.MainShell;

public class DisplayKeyEventListener implements Listener {
	private static final Logger LOGGER = LogManager.getLogger();
	private MainShell mainShell;

	public void setMainShell(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void handleEvent(Event event) {
		LOGGER.trace("{}|{}|{}|{}", event.character, event.keyCode, event.keyLocation,
				event.stateMask);
		if (((event.stateMask & SWT.ALT) == SWT.ALT)
				&& (event.character == 'd' || event.character == 'D')) {
			mainShell.showDebug();
		}

	}

}
