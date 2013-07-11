package org.codeforamerica.open311.internals.parsing;

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
}
