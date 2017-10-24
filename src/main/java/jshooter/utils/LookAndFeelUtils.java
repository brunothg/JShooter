package jshooter.utils;

import java.awt.Component;
import java.awt.Window;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils for LookAndFell handling
 * 
 * @author Marvin Bruns
 *
 */
public class LookAndFeelUtils {
	private static final Logger LOG = LoggerFactory
			.getLogger(LookAndFeelUtils.class);

	public static void updateLookAndFeel(String laf)
			throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(laf);

		propagateLookAndFeel();
	}

	public static void updateLookAndFeel(LookAndFeel laf)
			throws UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(laf);

		propagateLookAndFeel();
	}

	/**
	 * Sets the actual (and possibly new) {@link LookAndFeel} on every
	 * {@link Component}, that is already visible inside an
	 * {@link java.awt.Window} .
	 */
	public static void propagateLookAndFeel() {
		Window[] windows = Window.getWindows();

		for (Window window : windows) {
			try {
				SwingUtilities.updateComponentTreeUI(window);
			} catch (Exception e) {
				LOG.warn("Could not update LookAndFeel of window '{}'", window,
						e);
			}
		}
	}
}
