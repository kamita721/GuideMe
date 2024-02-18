package org.guideme.guideme.ui.mainShell;

import java.util.HashMap;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;

// listener for the control that holds the image,
// will resize the image if the control is resized.
class ImageControlAdapter extends ControlAdapter {
	/**
	 * 
	 */
	private final MainShell mainShell;

	/**
	 * @param mainShell
	 */
	ImageControlAdapter(MainShell mainShell) {
		this.mainShell = mainShell;
	}

	@Override
	public void controlResized(ControlEvent e) {
		//TODO why is this needed?
		if(true) {
			return;
		}
		MainShell.logger.trace("Enter addControlListener");
		if (!this.mainShell.imgOverRide) {
			Browser me = (Browser) e.widget;
			if (me.getData("imgPath") != null) {
				String imgPath = (String) me.getData("imgPath");
				String strHtml = this.mainShell.leftHTML.replace("IMG_SRC", imgPath);
				me.setText(strHtml, true);
			}
		}
	}
}