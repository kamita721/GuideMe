package org.guideme.guideme.settings;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.guideme.guideme.ui.preference_shell.PreferenceShell;

public class PreferenceShellTest {
	public PreferenceShellTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		Display myDisplay = new Display();
		AppSettings appSettings = AppSettings.getAppSettings();
		Shell prefShell = new PreferenceShell().createShell(myDisplay, appSettings);
		prefShell.open();
		while (!prefShell.isDisposed()) {
			if (!myDisplay.readAndDispatch())
				myDisplay.sleep();
		}
	}

}
