package jshooter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jshooter.utils.I18N;

@Configuration
public class Beans {

	@Bean(name = { "i18n" })
	public I18N getI18N() {
		I18N i18n = new I18N();
		i18n.setBaseName("languages.language");
		i18n.setGetKeyWehnMissing(true);
		i18n.setClassLoader(null);

		return i18n;
	}
}
