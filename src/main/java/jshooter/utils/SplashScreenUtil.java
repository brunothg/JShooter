package jshooter.utils;

import java.awt.Graphics;
import java.awt.SplashScreen;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities for {@link java.awt.SplashScreen}
 * 
 * @author Marvin Bruns
 *
 */
public class SplashScreenUtil {
	private static final Logger LOG = LoggerFactory.getLogger(SplashScreenUtil.class);

	public static SplashScreen getSplashScreen(URL imageURL) throws IOException {
		SplashScreen screen = SplashScreen.getSplashScreen();
		if (screen != null) {
			screen.setImageURL(imageURL);
		}

		return screen;
	}

	public static void showSplashScreen(URL imageURL, Runnable action) {
		SplashScreen splashScreen = null;
		try {
			splashScreen = getSplashScreen(imageURL);
		} catch (Exception e) {
			LOG.warn("Could not create splash screen", e);
		}

		if (splashScreen != null) {
			action.run();
			splashScreen.close();
		} else {
			BufferedImage image = null;
			try {
				image = ImageIO.read(imageURL);
			} catch (Exception e) {
				LOG.warn("Could not fetch image from URL '{}'", imageURL, e);
			}
			showSplashDialog(image, action);
		}

	}

	public static void showSplashDialog(BufferedImage image, Runnable action) {
		JSplashDialog splashDialog = new JSplashDialog();
		splashDialog.setVisible(true);

		action.run();

		splashDialog.setVisible(false);
		splashDialog.dispose();
	}

	public static class JSplashDialog extends JDialog {
		private static final long serialVersionUID = 1L;

		private BufferedImage image;

		public JSplashDialog() {
			build();
		}

		private void build() {
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setTitle("Loading Application...");

			setUndecorated(true);
			setSize(800, 600);
			setLocationRelativeTo(null);
			setOpacity(0.00000000000000000000000000000000000000000001f);
		}

		@Override
		public void paintComponents(Graphics g) {
			if (image == null) {
				super.paintComponents(g);
				return;
			}

			double difX = (double) getWidth() / image.getWidth();
			double difY = (double) getHeight() / image.getHeight();

			double dif = Math.min(difX, difY);

			int imgW = (int) (image.getWidth() * dif);
			int imgH = (int) (image.getHeight() * dif);
			int imgX = (int) ((getWidth() - imgW) * 0.5);
			int imgY = (int) ((getHeight() - imgH) * 0.5);

			g.drawImage(image, imgX, imgY, imgX + imgW, imgY + imgH, 0, 0, image.getWidth(), image.getHeight(), this);
		}

		public void setImage(BufferedImage img) {
			this.image = img;
		}

		public void setImageURL(URL imageURL) throws IOException {
			setImage(ImageIO.read(imageURL));
		}
	}
}
