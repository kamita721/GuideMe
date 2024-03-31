package org.guideme.guideme.util;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Label;
import org.imgscalr.Scalr;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;

/*
 * TODO:
 * 
 * utilize pre-fetch support in caller code
 */
public class ImageManager {
	private static final int CACHE_SIZE = 128;

	static Logger LOGGER = LogManager.getLogger();

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
		} catch (ExecutionException e) {
			LOGGER.error("Error loading image: " + currentImagePath, e);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if (oldImage != null) {
			oldImage.dispose();
		}
	}

	public void prefetch(String path) {
		if(path == null) {
			return;
		}
		fullsizeCache.get(path);
	}

	/*
	 * TODO, why is this a thing?
	 */
	public void scaleImageOnDisk(File imageFile) {
		String extension = FilenameUtils.getExtension(imageFile.getPath());
		if (extension.equals("jpg") || extension.equals("bmp")) {
			try {
				ImageData imgData = new ImageData(imageFile.toString());
				Point scaledDimensions = getImageScaledDimensions(imgData, largestImageDimension);
				if(scaledDimensions == null) {
					LOGGER.error("Error downscaling image file {}", imageFile);
					return;
				}
				if(scaledDimensions.x >= imgData.width || scaledDimensions.y >= imgData.height) {
					return;
				}
				
				BufferedImage img = null;
				ImageIO.setUseCache(false);
				img = ImageIO.read(new File(imageFile.getAbsolutePath()));

				if (img.getColorModel().hasAlpha()) {
					BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
					convertedImg.getGraphics().drawImage(img, 0, 0, null);
					img = convertedImg;
				}
				BufferedImage imageNew = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, scaledDimensions.x,
						scaledDimensions.y, Scalr.OP_ANTIALIAS);
				ImageIO.write(imageNew, extension, imageFile);

			} catch (Exception ex6) {
				LOGGER.error("Process Image error " + ex6.getLocalizedMessage(), ex6);
			}
		}

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
		Point scaledDimensions = getImageScaledDimensions(raw, maxSize);
		if(scaledDimensions == null) {
			return null;
		}
		return raw.scaledTo(scaledDimensions.x, scaledDimensions.y);
	}
	
	private Point getImageScaledDimensions(ImageData img, Point maxSize) {
		if (img == null) {
			return null;
		}
		if (maxSize.x <= 0 || maxSize.y <= 0) {
			return null;
		}
		double xFactor = (double) maxSize.x / (double) img.width;
		double yFactor = (double) maxSize.y / (double) img.height;
		double factor = Math.min(xFactor, yFactor);
		return new Point((int) (img.width * factor), (int) (img.height * factor));
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
