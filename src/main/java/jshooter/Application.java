package jshooter;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.brunothg.swing.mvp.annotation.EnableSwingMVP;

/**
 * Application starting point. Setting up spring-context and other initial
 * stuff.
 * 
 * @author Marvin Bruns
 *
 */
@SpringBootApplication
@EnableSwingMVP
public class Application {
	private static final Logger LOG = LoggerFactory
			.getLogger(Application.class);

	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		LOG.info("Launching application ...");

		initializeSpring(args);
		initializeUi();
	}

	private static void initializeUi() {
		LOG.info("Setting LookAndFeel to system default");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			LOG.warn("Could not set LookAndFeel to system default", e);
		}
	}

	private static void initializeSpring(String[] args) {
		ctx = new SpringApplicationBuilder(Application.class).headless(false)
				.web(false).run(args);
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
}
