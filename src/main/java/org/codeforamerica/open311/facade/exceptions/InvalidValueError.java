package org.codeforamerica.open311.facade.exceptions;

/**
 * Will be thrown if one given argument has an incorrect value.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class InvalidValueError extends Error {

	private static final long serialVersionUID = 1L;

	public InvalidValueError(String message) {
		super(message);
	}

}
