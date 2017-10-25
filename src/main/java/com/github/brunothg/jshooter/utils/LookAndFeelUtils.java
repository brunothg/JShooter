package com.github.brunothg.jshooter.utils;

import java.awt.Component;
import java.awt.Window;

import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utils for LookAndFell handling
 * 
 * @author Marvin Bruns
 *
 */
public class LookAndFeelUtils {
	private static final Logger LOG = LoggerFactory.getLogger(LookAndFeelUtils.class);

	/**
	 * Sets the actual (and possibly new) {@link LookAndFeel} on every
	 * {@link Component} .
	 */
	public static void updateLookAndFeel(final String laf) {
		LOG.debug("Setting LaF -> '{}'", laf);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(laf);
				} catch (Exception e) {
					LOG.warn("Error setting LaF", e);
				}

				propagateLookAndFeel();
			}
		});
	}

	public static void updateLookAndFeel(final LookAndFeel laf) {
		updateLookAndFeel(laf.getClass().getCanonicalName());
	}

	/**
	 * Sets {@link LookAndFeel} for specified components, but keeps original
	 * {@link LookAndFeel} for other and new components
	 * 
	 * @param laf
	 * @param c
	 */
	public static void setLookAndFeelForComponent(final String laf, final Component... c) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				LookAndFeel currentLaF = UIManager.getLookAndFeel();

				try {
					UIManager.setLookAndFeel(laf);
				} catch (Exception e) {
					LOG.warn("Error setting LaF", e);
				}

				propagateLookAndFeel(c);

				try {
					UIManager.setLookAndFeel(currentLaF);
				} catch (Exception e) {
					LOG.warn("Error setting LaF", e);
				}
			}
		});
	}

	public static void setLookAndFeelForComponent(final LookAndFeel laf, final Component... c) {
		setLookAndFeelForComponent(laf.getClass().getCanonicalName(), c);
	}

	private static void propagateLookAndFeel(final Component... c) {
		Component[] components;

		if (c == null || c.length <= 0) {
			components = Window.getWindows();
		} else {
			components = c;
		}

		for (Component conponent : components) {
			if (conponent == null) {
				continue;
			}
			try {
				SwingUtilities.updateComponentTreeUI(conponent);
			} catch (Exception e) {
				LOG.warn("Could not update LookAndFeel of component '{}'", conponent, e);
			}
		}

	}
}
