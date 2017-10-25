package com.github.brunothg.jshooter.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.brunothg.jshooter.utils.I18N;

@Configuration
public class Beans {

	@Bean(name = { "i18n" })
	@Autowired
	public I18N getI18N(UserProperties userSettings) {
		I18N i18n = new I18N();
		i18n.setBaseName("languages.language");
		i18n.setGetKeyWehnMissing(true);
		i18n.setClassLoader(null);
		i18n.setDefaultLocale(Locale.forLanguageTag(userSettings.getLanguage()));

		return i18n;
	}
}
