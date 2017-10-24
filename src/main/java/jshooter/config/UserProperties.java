package jshooter.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom")
public class UserProperties {
	private static final transient Logger LOG = LoggerFactory
			.getLogger(UserProperties.class);

	private String language;
	private String lookAndFeel;

	public String getLanguage() {
		return language;
	}

	public Locale getLocale() {
		return Locale.forLanguageTag(getLanguage());
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLookAndFeel() {
		return lookAndFeel;
	}

	public void setLookAndFeel(String lookAndFeel) {
		this.lookAndFeel = lookAndFeel;
	}

	public void setLocale(Locale locale) {
		setLanguage(locale.toLanguageTag());
	}

	public void store() throws IOException {
		Properties properties = new Properties();

		Field[] fields = UserProperties.class.getDeclaredFields();
		for (Field field : fields) {
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}

			try {
				field.setAccessible(true);

				String name = "custom." + field.getName();
				String value = field.get(UserProperties.this).toString();

				properties.setProperty(name, value);
			} catch (SecurityException | IllegalArgumentException
					| IllegalAccessException e) {
				LOG.warn("Could not store property '{}'", field);
			}
		}

		properties.store(
				Files.newBufferedWriter(Paths.get("./application.properties"),
						StandardCharsets.UTF_8),
				"User settings");
	}
}
