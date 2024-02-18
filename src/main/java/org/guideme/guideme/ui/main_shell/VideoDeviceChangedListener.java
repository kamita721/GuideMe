package org.guideme.guideme.ui.main_shell;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;

class VideoDeviceChangedListener extends SelectionAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	VideoDeviceChangedListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (((MenuItem) e.widget).getSelection()) {
			String newOutputDevice = e.widget.getData("device-id").toString();
			this.mainShell.appSettings.setVideoDevice(newOutputDevice);
			if (newOutputDevice != null) {
				// TODO this doesn't seem quite right
				this.mainShell.mediaPlayer.audio().setOutputDevice(null, newOutputDevice);
			}
		}
	}
}