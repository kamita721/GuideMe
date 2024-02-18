package org.guideme.guideme.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

/*
 * TODO:
 * 
 * utilize pre-fetch support in caller code
 */
public class ImageManager {
	private static final int CACHE_SIZE = 128;

	static Logger logger = LogManager.getLogger();

	Device device;
	Point preferedSize;
	String currentImagePath;
	Image currentImage;
	Point largestImageDimension;

	private AsyncLoadingCache<String, ImageData> fullsizeCache;

	public ImageManager(Device device) {
		this.device = device;
		this.fullsizeCache = Caffeine.newBuilder().maximumSize(CACHE_SIZE).buildAsync(this::loadImageDataFromFile);

		this.largestImageDimension = getLargestImageSize();
	}

	public void setPreferedSize(Point preferedSize) {
		this.preferedSize = preferedSize;
	}

	public void setCurrentImagePath(String path) {
		prefetch(path);
		this.currentImagePath = path;
	}

	public void updateImageLabel(Label imageLabel) {
		Image oldImage = imageLabel.getImage();
		try {
			imageLabel.setImage(getImage());
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Error loading image: " + currentImagePath, e);
		}
		if (oldImage != null) {
			oldImage.dispose();
		}
	}

	public void prefetch(String path) {
		fullsizeCache.get(path);
	}

	private ImageData loadImageDataFromFile(String filePath) {
		// TODO handle errors
		ImageData raw = new ImageData(filePath);
		return scaleImageData(raw, largestImageDimension);
	}

	private Image getImage() throws InterruptedException, ExecutionException {
		if (currentImagePath == null) {
			return null;
		}
		ImageData data = fullsizeCache.get(currentImagePath).get();
		data = scaleImageData(data, preferedSize);
		if (currentImage != null) {
			currentImage.dispose();
		}
		currentImage = new Image(device, data);
		return currentImage;
	}

	private ImageData scaleImageData(ImageData raw, Point maxSize) {
		if (raw == null) {
			return null;
		}
		if (maxSize.x <= 0 || maxSize.y <= 0) {
			return null;
		}
		double xFactor = (double) maxSize.x / (double) raw.width;
		double yFactor = (double) maxSize.y / (double) raw.height;
		double factor = Math.min(xFactor, yFactor);
		return raw.scaledTo((int) (raw.width * factor), (int) (raw.height * factor));
	}

	private Point getLargestImageSize() {
		Rectangle virtualBounds = new Rectangle();
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		for (int j = 0; j < gs.length; j++) {
			GraphicsDevice gd = gs[j];
			GraphicsConfiguration[] gc = gd.getConfigurations();
			for (int i = 0; i < gc.length; i++) {
				virtualBounds = virtualBounds.union(gc[i].getBounds());
			}
		}
		return new Point(virtualBounds.width, virtualBounds.height);
	}
}
