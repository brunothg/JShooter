package com.github.brunothg.jshooter;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.brunothg.game.engine.image.InternalImage;
import com.github.brunothg.game.engine.utils.event.EventBus;
import com.github.brunothg.jshooter.config.UserProperties;
import com.github.brunothg.jshooter.gui.home.GameDisplay;
import com.github.brunothg.jshooter.utils.LookAndFeelUtils;
import com.github.brunothg.jshooter.utils.SplashScreenUtil;
import com.github.brunothg.jshooter.utils.ThreadUtils;
import com.github.brunothg.swing2.dialog.ExceptionDialog;
import com.github.brunothg.swing2.utils.Null;

/**
 * Application starting point. Setting up spring-context and other initial
 * stuff.
 * 
 * @author Marvin Bruns
 *
 */
@SpringBootApplication
public class Application {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private static ConfigurableApplicationContext ctx;
	private static EventBus applicationEventBus;

	public static void main(String[] args) {
		try {
			saveMain(args);
		} catch (Exception e) {
			ExceptionDialog.showExceptionDialog(null, e);
			LOG.error("Unknown error", e);
		}
	}

	private static void saveMain(String[] args) {
		LOG.info("Launching application ...");

		initializeSpring(args);

		URL imageURL = Application.class.getClassLoader().getResource("media/images/splashscreen.png");
		SplashScreenUtil.showSplashScreen(imageURL, new Runnable() {
			public void run() {
				initializeApplication();

				ThreadUtils.sleep(2000);
			}
		});

		launchApplication();
	}

	private static void initializeSpring(String[] args) {
		ctx = new SpringApplicationBuilder(Application.class).headless(false).web(WebApplicationType.NONE).run(args);
	}

	private static void initializeApplication() {
		UserProperties userProperties = ctx.getBean(UserProperties.class);

		// Hook for cleaning up application before real shutdown
		registerShutdownHook();

		// Resotre LookAndFeel
		LookAndFeelUtils.updateLookAndFeel(
				Null.nvl(userProperties.getLookAndFeel(), UIManager.getSystemLookAndFeelClassName()));

		// Setting locale
		Locale.setDefault(userProperties.getLocale());

		// Setup default imagelocation
		InternalImage.setRootFolder("/media/images/");

		// Setup event bus
		applicationEventBus = new EventBus(Application.class);
	}

	private static void launchApplication() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GameDisplay display = ctx.getBean(GameDisplay.class);
				display.setVisible(true);
			}
		});
	}

	private static void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				// Update and save UserProperties
				UserProperties userProperties = ctx.getBean(UserProperties.class);
				userProperties.setLookAndFeel(UIManager.getLookAndFeel().getClass().getCanonicalName());
				try {
					userProperties.store();
				} catch (IOException e) {
					LOG.warn("Could not store user settings", e);
				}
			}
		});
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

	public static EventBus getApplicationEventBus() {
		return applicationEventBus;
	}
}
