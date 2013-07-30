package org.codeforamerica.open311.internals.parsing;

import java.net.URL;

import org.codeforamerica.open311.facade.exceptions.DataParsingException;

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

	/**
	 * Check if all the given strings are empty.
	 * 
	 * @param strings
	 *            List of strings.
	 * @return <code>true</code> is all are empty.
	 */
	protected boolean allStringsAreEmpty(String... strings) {
		for (int i = 0; i < strings.length; i++) {
			if (strings[i] != null && strings[i].length() > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if any parameter is valid.
	 * 
	 * @param strings
	 *            List of parameters.
	 * @throws DataParsingException
	 *             If all parameters are missing.
	 */
	protected void checkParameters(String... strings)
			throws DataParsingException {
		if (allStringsAreEmpty(strings)) {
			throw new DataParsingException(
					"Invalid data, required fields wasn't received.");
		}
	}
}
