package jshooter.utils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * {@link URL} utility class
 * 
 * @author Marvin Bruns
 *
 */
public class UrlUtils {

	public static URL getUrl(String url) {
		URL ret = null;
		try {
			ret = new URL(url);
		} catch (MalformedURLException e) {
		}

		return ret;
	}
}
