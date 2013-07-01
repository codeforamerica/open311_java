package org.codeforamerica.open311.facade.exceptions;

/**
 * Represents a problem in the parsing of any string.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class DataParsingException extends Exception {

	private static final long serialVersionUID = 1L;

	public DataParsingException(String message) {
		super(message);
	}

}
