package jshooter.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtils {
	private static final Logger LOG = LoggerFactory
			.getLogger(ThreadUtils.class);

	public static void sleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			LOG.warn("Could not sleep", e);
		}
	}
}
