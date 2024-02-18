package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;

class AudioOneDeviceChangedListener extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	AudioOneDeviceChangedListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (((MenuItem) e.widget).getSelection()) {
			String newOutputDevice = e.widget.getData("device-id").toString();
			this.mainShell.appSettings.setAudioOneDevice(newOutputDevice);
			if (this.mainShell.threadAudioPlayer != null && newOutputDevice != null) {
				this.mainShell.audioPlayer.setAudioDevice(newOutputDevice);
			}
		}
	}
}