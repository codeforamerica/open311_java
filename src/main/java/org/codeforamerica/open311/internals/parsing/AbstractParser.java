package org.codeforamerica.open311.internals.parsing;

import java.net.URL;

/**
 * Contains common methods of the different {@link DataParser} implementations.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public abstract class AbstractParser implements DataParser {

	/**
	 * Parses a comma separated list of keywords.
	 * 
	 * @param rawKeywords
	 *            A comma separated list of keywords.
	 * @return An array of strings (keywords).
	 */
	protected String[] getKeywords(String rawKeywords) {
		String[] result = rawKeywords.split(KEYWORDS_SEPARATOR);
		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}

	/**
	 * Tries to build a URL.
	 * 
	 * @param url
	 *            Raw url.
	 * @return <code>null</code> if it was any problem.
	 */
	protected URL buildUrl(String url) {
		try {
			return new URL(url);
		} catch (Exception e) {
			return null;
		}
	}
}
