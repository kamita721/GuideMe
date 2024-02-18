package org.guideme.guideme.ui.mainShell;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

class mediaPanelListener extends ControlAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	mediaPanelListener(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	// resize the video if the container changes size
	@Override
	public void controlResized(ControlEvent e) {
		super.controlResized(e);
		if (this.mainShell.videoOn) {
			try {
				// Rectangle rect = mediaPanel.getClientArea();
				// videoFrame.setSize(rect.width, rect.height);
			} catch (Exception ex) {
				MainShell.logger.error("Video resize " + ex.getLocalizedMessage(), ex);
			}
		}
	}

}