package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;

class ShellCloseListen extends ShellAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	ShellCloseListen(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// Clean up stuff when the application closes
	@Override
	public void shellClosed(ShellEvent e) {
		mainShell.myDisplay.removeFilter(SWT.KeyDown, this.mainShell.keyListener);
		mainShell.keyListener = null;
		if (mainShell.shell2 != null && !mainShell.shell2.isDisposed()) {
			mainShell.shell2.close();
		}
		mainShell.debugShell.closeShell();
		int[] intWeights;
		if (!mainShell.multiMonitor) {
			intWeights = mainShell.singleMonitorWrapper.getWeights();
			mainShell.appSettings.setSash1Weights(intWeights);
			int[] intWeights2;
			intWeights2 = mainShell.textPaneWrapper.getWeights();
			mainShell.appSettings.setSash2Weights(intWeights2);
		}
		mainShell.appSettings.saveSettings();
		mainShell.controlFont.dispose();
		mainShell.timerFont.dispose();
		mainShell.buttonFont.dispose();
		mainShell.stopAll(true);
		mainShell.metronome.metronomeKill();

		super.shellClosed(e);
	}
}