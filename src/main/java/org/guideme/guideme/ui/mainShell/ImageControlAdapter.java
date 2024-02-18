package org.guideme.guideme.ui.mainShell;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.imgscalr.Scalr;

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

	public void controlResized(ControlEvent e) {
		MainShell.logger.trace("Enter addControlListener");
		if (!this.mainShell.imgOverRide) {
			HashMap<String, Integer> imageDimentions;

			// Label me = (Label)e.widget;
			Browser me = (Browser) e.widget;
			// Image myImage = (Image) me.getData("image");
			try {
				if (me.getData("imgPath") != null) {
					String strHtml;
					String tmpImagePath;
					String imgPath = (String) me.getData("imgPath");
					double imageRatio = ((Double) me.getData("imageRatio")).doubleValue();
					Rectangle RectImage = me.getBounds();
					double dblScreenRatio = (double) RectImage.height / (double) RectImage.width;
					MainShell.logger.trace("imgPath: " + imgPath);
					MainShell.logger.trace("dblScreenRatio: " + dblScreenRatio);
					MainShell.logger.trace("dblImageRatio: " + imageRatio);
					MainShell.logger.trace("Lable Height: " + RectImage.height);
					MainShell.logger.trace("Lable Width: " + RectImage.width);

					int maxImageScale = (int) this.mainShell.imageLabel.getData("maxImageScale");
					MainShell.logger.trace("maxImageScale: " + maxImageScale);
					int maxheight = (int) this.mainShell.imageLabel.getData("maxheight");
					MainShell.logger.trace("maxheight: " + maxheight);
					int maxwidth = (int) this.mainShell.imageLabel.getData("maxwidth");
					MainShell.logger.trace("maxwidth: " + maxwidth);

					imageDimentions = MainShell.GetNewDimentions(imageRatio, RectImage.height, RectImage.width,
							this.mainShell.appSettings.getImgOffset(), maxImageScale != 0, maxheight, maxwidth);

					// String strHtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD
					// XHTML 1.0 Strict//EN\"
					// \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html
					// xmlns=\"http://www.w3.org/1999/xhtml\"
					// xml:lang=\"en\"><head><meta
					// http-equiv=\"Content-type\"
					// content=\"text/html;charset=UTF-8\"
					// /><title></title><style type=\"text/css\">" +
					// defaultStyle + "</style></head><body><table
					// id=\"wrapper\"><tr><td><img src=\"" + tmpImagePath +
					// "\" height=\"" + newHeight + "\" width=\"" + newWidth
					// + "\" /></td></tr></table></body></html>";
					if (imgPath.endsWith(".gif")) {
						tmpImagePath = imgPath;
						strHtml = this.mainShell.leftHTML.replace("DefaultStyle",
								this.mainShell.defaultStyle + " body { overflow:hidden }");
						strHtml = strHtml.replace("BodyContent",
								"<table id=\"wrapper\"><tr><td><img src=\"" + tmpImagePath + "\" height=\""
										+ imageDimentions.get("newHeight") + "\" width=\""
										+ imageDimentions.get("newWidth") + "\" /></td></tr></table>");
					} else {
						//TODO read this info in once on load
//						BufferedImage img = null;
//						try {
//							ImageIO.setUseCache(false);
//							img = ImageIO.read(new File(imgPath));
//						} catch (IOException e1) {
//						}
//						if (img.getColorModel().hasAlpha()) {
//							img = this.mainShell.comonFunctions.dropAlphaChannel(img);
//						}
						strHtml = this.mainShell.leftHTML.replace("DefaultStyle",
								this.mainShell.defaultStyle + " body { overflow:hidden }");
						strHtml = strHtml.replace("BodyContent",
								"<table id=\"wrapper\"><tr><td><img src=\"" + imgPath + "\" height=\""
										+ imageDimentions.get("newHeight") + "\" width=\""
										+ imageDimentions.get("newWidth") + "\" /></td></tr></table>");
					}
					me.setText(strHtml, true);
					System.err.println(me.getText());
					// Image tmpImage = me.getImage();
					// me.setImage(resize(myImage, newWidth, newHeight));
					// tmpImage.dispose();
				}
			} catch (Exception ex7) {
				MainShell.logger.error("Shell Resize error " + ex7.getLocalizedMessage(), ex7);
			}
		}
	}
}