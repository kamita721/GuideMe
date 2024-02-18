package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;

class AudioTwoDeviceChangedListener extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	AudioTwoDeviceChangedListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (((MenuItem) e.widget).getSelection()) {
			String newOutputDevice = e.widget.getData("device-id").toString();
			this.mainShell.appSettings.setAudioTwoDevice(newOutputDevice);
			if (this.mainShell.threadAudioPlayer2 != null && newOutputDevice != null) {
				this.mainShell.audioPlayer2.setAudioDevice(newOutputDevice);
			}
		}
	}
}