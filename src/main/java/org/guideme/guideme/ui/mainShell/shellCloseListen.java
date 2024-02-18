package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Event;

class shellCloseListen extends ShellAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	shellCloseListen(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// Clean up stuff when the application closes
	@Override
	public void shellClosed(ShellEvent e) {
		try {
			// exitTriggered = true;
			this.mainShell.myDisplay.removeFilter(SWT.KeyDown, this.mainShell.keyListener);
			this.mainShell.keyListener = null;
			if (this.mainShell.shell2 != null) {
				this.mainShell.shell2.close();
			}
			this.mainShell.debugShell.closeShell();
			int[] intWeights;
			if (!this.mainShell.multiMonitor) {
				intWeights = this.mainShell.sashform.getWeights();
				this.mainShell.appSettings.setSash1Weights(intWeights);
				int[] intWeights2;
				intWeights2 = this.mainShell.sashform2.getWeights();
				this.mainShell.appSettings.setSash2Weights(intWeights2);
			}
			// appSettings.setDataDirectory(strGuidePath);
			this.mainShell.appSettings.saveSettings();
			this.mainShell.controlFont.dispose();
			this.mainShell.timerFont.dispose();
			this.mainShell.buttonFont.dispose();
			this.mainShell.stopAll(true);
			this.mainShell.metronome.metronomeKill();
		} catch (Exception ex) {
			MainShell.logger.error("shellCloseListen ", ex);
		}
		super.shellClosed(e);
	}

	public void handleEvent(Event event) {
	}
}