package org.guideme.guideme.ui.main_shell;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.eclipse.swt.events.SelectionEvent;

class WebcamCaptureListener extends DynamicButtonListner {
	/**
	 * 
	 */
	private final MainShell mainShell;

	WebcamCaptureListener(MainShell mainShell) {
		super(mainShell);
		this.mainShell = mainShell;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {
		try {

			MainShell.logger.trace("Enter WebcamCaptureListener");
			String strFile;
			com.snapps.swt.SquareButton btnClicked;
			btnClicked = (com.snapps.swt.SquareButton) event.widget;
			strFile = (String) btnClicked.getData("webcamFile");
			if (!mainShell.comonFunctions.canCreateFile(strFile)) {
				strFile = mainShell.comonFunctions.getMediaFullPath(strFile, mainShell.appSettings.getFileSeparator(), mainShell.appSettings,
						mainShell.guide);
			}

			try {
				BufferedImage image = mainShell.webcam.getImage();
				ImageIO.write(image, "JPG", new File(strFile));
			} catch (Exception ex) {
				MainShell.logger.error(" WebcamCaptureListener take and save image " + ex.getLocalizedMessage(), ex);
			}

			super.widgetSelected(event);
		} catch (Exception ex) {
			MainShell.logger.error(" WebcamCaptureListener " + ex.getLocalizedMessage(), ex);
		}
		MainShell.logger.trace("Exit WebcamCaptureListener");
	}
}