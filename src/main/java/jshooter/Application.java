package jshooter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import com.github.brunothg.swing.mvp.annotation.EnableSwingMVP;

import jshooter.config.ApplicationInfo;

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
	}

	private static void initializeSpring(String[] args) {
		ctx = new SpringApplicationBuilder(Application.class).headless(false).web(false).run(args);

		System.out.println(ctx.getBean(ApplicationInfo.class).getBuildTimestamp());
		System.out.println(ctx.getBean(ApplicationInfo.class).getVersion());
		System.out.println(ctx.getBean(ApplicationInfo.class).getBuildTimestampDate());
	}

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}
}
