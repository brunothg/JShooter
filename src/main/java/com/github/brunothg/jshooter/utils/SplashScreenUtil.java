package com.github.brunothg.jshooter.utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

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
			try {
				action.run();
			} catch (Exception e) {
				LOG.warn("Error executing action", e);
				throw e;
			} finally {
				splashScreen.close();
			}
		} else {
			LOG.warn("SplashScreen could not be created - switching to dialog fallback");

			BufferedImage image = null;
			try {
				image = ImageIO.read(imageURL);
			} catch (Exception e) {
				LOG.warn("Could not fetch image from URL '{}'", imageURL, e);
			}
			showSplashDialog(image, action);
		}

	}

	public static void showSplashDialog(final BufferedImage image, final Runnable action) {
		final JSplashDialog splashDialog = new JSplashDialog();
		splashDialog.setImage(image);
		splashDialog.setVisible(true);

		try {
			action.run();
		} catch (Exception e) {
			LOG.warn("Error executing action", e);
			throw e;
		} finally {
			splashDialog.setVisible(false);
			splashDialog.dispose();
		}

	}

	public static class JSplashDialog extends JDialog {
		private static final long serialVersionUID = 1L;

		private BufferedImage image;
		private JProgressBar pb;

		public JSplashDialog() {
			build();
		}

		private void build() {
			setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			setTitle("");
			setLayout(new BorderLayout());

			setUndecorated(true);
			setAlwaysOnTop(true);
			setSize(800, 600);
			setLocationRelativeTo(null);
			setBackground(new Color(0, 0, 0, 0));

			pb = new JProgressBar();
			pb.setIndeterminate(true);
			pb.setStringPainted(false);
			pb.setOpaque(false);
			add(pb, BorderLayout.SOUTH);
		}

		@Override
		public void paint(Graphics g) {
			if (image == null) {
				return;
			}

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			double difX = (double) getWidth() / image.getWidth();
			double difY = (double) getHeight() / image.getHeight();

			double dif = Math.min(difX, difY);

			int imgW = (int) (image.getWidth() * dif);
			int imgH = (int) (image.getHeight() * dif);
			int imgX = (int) ((getWidth() - imgW) * 0.5);
			int imgY = (int) ((getHeight() - imgH) * 0.5);

			g2d.drawImage(image, imgX, imgY, imgX + imgW, imgY + imgH, 0, 0, image.getWidth(), image.getHeight(), null);

			super.paintComponents(g);

			if (getWidth() != imgW || getHeight() != (imgH + pb.getHeight())) {
				setSize(imgW, imgH + pb.getHeight());
				setLocationRelativeTo(null);
				setBackground(new Color(0, 0, 0, 0));
			}
		}

		public void setProgress(Double progress) {
			if (progress == null) {
				pb.setIndeterminate(true);
			} else {
				pb.setValue((int) Math.round(100 * progress));
			}
		}

		public void setImage(BufferedImage img) {
			this.image = img;
			setIconImage(image);
		}

		public void setImageURL(URL imageURL) throws IOException {
			setImage(ImageIO.read(imageURL));
		}
	}
}
