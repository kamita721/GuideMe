package org.guideme.guideme.ui.main_shell;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;

class WebcamCaptureListener extends DynamicButtonListner {
	private static final Logger LOGGER = LogManager.getLogger();

	private final MainShell mainShell;

	WebcamCaptureListener(MainShell mainShell) {
		super(mainShell);
		this.mainShell = mainShell;
	}

	@Override
	public void widgetSelected(SelectionEvent event) {

		LOGGER.trace("Enter WebcamCaptureListener");
		String strFile;
		com.snapps.swt.SquareButton btnClicked;
		btnClicked = (com.snapps.swt.SquareButton) event.widget;
		strFile = (String) btnClicked.getData("webcamFile");
		if (!mainShell.comonFunctions.CanCreateFile(strFile)) {
			strFile = mainShell.comonFunctions.getMediaFullPath(strFile,
					mainShell.appSettings.getFileSeparator(), mainShell.appSettings,
					mainShell.guide);
		}

		BufferedImage image = mainShell.webcam.getImage();
		try {
			ImageIO.write(image, "JPG", new File(strFile));
		} catch (IOException e) {
			LOGGER.warn("Unable to save webcam image", e);
		}

		super.widgetSelected(event);

		LOGGER.trace("Exit WebcamCaptureListener");
	}
}