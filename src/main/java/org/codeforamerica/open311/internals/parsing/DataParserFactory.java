package org.codeforamerica.open311.internals.parsing;

import org.codeforamerica.open311.facade.Format;

/**
 * Builds instances of {@link DataParser}.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class DataParserFactory {

	private static DataParserFactory instance = new DataParserFactory();

	private DataParserFactory() {
	}

	public static DataParserFactory getInstance() {
		return instance;
	}

	/**
	 * Builds a data parser taking care of the desired format.
	 * 
	 * @param format
	 *            Desired format.
	 * @return an instance of {@link DataParser} of <code>null</code> if the
	 *         given format is not supported.
	 */
	public DataParser buildDataParser(Format format) {
		if (format == Format.XML) {
			return new XMLParser();
		}
		if (format == Format.JSON) {
			return new JSONParser();
		}
		return null;
	}
}
