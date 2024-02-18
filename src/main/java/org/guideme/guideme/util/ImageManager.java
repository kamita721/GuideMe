package org.guideme.guideme.util;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;

public class ImageManager {
	Device device;
	Point preferedSize;
	String currentImagePath;
	Image currentImage;

	public ImageManager(Device device) {
		this.device = device;
	}

	public void setPreferedSize(Point preferedSize) {
		this.preferedSize = preferedSize;
	}

	public void setCurrentImagePath(String path) {
		this.currentImagePath = path;
	}

	public void updateImageLabel(Label imageLabel) {
		Image oldImage = imageLabel.getImage();
		imageLabel.setImage(getImage());
		if (oldImage != null) {
			oldImage.dispose();
		}
	}

	/*
	 * TODO add smarts:
	 * 
	 * Cache images Resize images Support image prefetch
	 * pre-scale large images
	 */
	private Image getImage() {
		if (currentImagePath == null) {
			return null;
		}
		Image raw = new Image(device, currentImagePath);
		ImageData data = raw.getImageData();
		data = data.scaledTo(preferedSize.x, preferedSize.y);
		if (currentImage != null) {
			currentImage.dispose();
		}
		raw.dispose();
		currentImage = new Image(device, data);
		return currentImage;
	}
}
