package com.github.brunothg.jshooter.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class internationalization purposes.
 * 
 * @author Marvin Bruns
 *
 */
public class I18N {
	private Locale defaultLocale = Locale.getDefault();
	private boolean getKeyWehnMissing = true;
	private String baseName = "language";
	private ClassLoader classLoader = null;

	private Map<Locale, ResourceBundle> languages = new HashMap<>();

	private ResourceBundle loadLanguage(Locale locale) {
		ClassLoader classLoader = getClassLoader();

		ResourceBundle bundle;
		try {
			if (classLoader == null) {
				bundle = ResourceBundle.getBundle(getBaseName(), locale);
			} else {
				bundle = ResourceBundle.getBundle(getBaseName(), locale, classLoader);
			}
		} catch (Exception e) {
			bundle = null;
		}

		return bundle;
	}

	protected ResourceBundle getLanguage(Locale locale) {
		ResourceBundle language = languages.get(locale);
		if (language == null) {
			language = loadLanguage(locale);
			languages.put(locale, language);
		}
		return language;
	}

	/**
	 * Get all available languages as defined in default language.properties file
	 * and the language name as defined in the specific language's properties file.
	 * Should add property <code>language-codes = de, en</code> to default
	 * properties which specifies all available languages and property
	 * <code>language-name = German</code> to specific properties which localizes
	 * the language name.
	 * 
	 * @return List with available languages
	 */
	public Map<Locale, String> getAvailableLanguages() {
		// FIXME default language returns english
		HashMap<Locale, String> languages = new HashMap<>();

		String[] codes = getLanguage(Locale.ROOT).getString("language-codes").split(",");
		for (String code : codes) {
			code = code.trim();

			Locale locale = Locale.forLanguageTag(code);
			String name = get("language-name", locale);

			languages.put(locale, (name != null && !name.isEmpty()) ? name : locale.getDisplayLanguage());
		}

		return languages;
	}

	/**
	 * @see ResourceBundle#clearCache()
	 */
	public void clearCache() {
		ClassLoader classLoader = getClassLoader();

		if (classLoader == null) {
			ResourceBundle.clearCache();
		} else {
			ResourceBundle.clearCache(classLoader);
		}
	}

	public String get(String key, Locale locale) {
		ResourceBundle language = getLanguage(locale);

		String value;
		try {
			value = (language != null) ? language.getString(key) : null;
		} catch (Exception e) {
			value = null;
		}

		if (value == null) {
			if (isGetKeyWehnMissing()) {
				value = key;
			} else {
				throw new RuntimeException("Key '" + key + "' is not available.");
			}
		}

		return value;
	}

	public String get(String key) {
		return get(key, getDefaultLocale());
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = (defaultLocale != null) ? defaultLocale : Locale.getDefault();
	}

	public boolean isGetKeyWehnMissing() {
		return getKeyWehnMissing;
	}

	public void setGetKeyWehnMissing(boolean getKeyWehnMissing) {
		this.getKeyWehnMissing = getKeyWehnMissing;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		if (baseName == null || baseName.isEmpty()) {
			throw new IllegalArgumentException("Can not be empty");
		}

		this.baseName = baseName;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public void setClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public String toString() {
		return "I18N [defaultLocale=" + defaultLocale + ", baseName=" + baseName + "]";
	}
}
