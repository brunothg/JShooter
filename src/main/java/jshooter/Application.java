package jshooter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.brunothg.swing.mvp.annotation.EnableSwingMVP;

import jshooter.config.UserProperties;
import jshooter.utils.I18N;

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
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	private static ConfigurableApplicationContext ctx;

	public static void main(String[] args) {
		LOG.info("Launching application ...");

		initializeSpring(args);
		registerShotdownHook();
	}

	private static void initializeSpring(String[] args) {
		ctx = new SpringApplicationBuilder(Application.class).headless(false).web(false).run(args);

		System.out.println(ctx.getBean(I18N.class).getAvailableLanguages());
	}

	private static void registerShotdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				UserProperties userProperties = ctx.getBean(UserProperties.class);
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
}
