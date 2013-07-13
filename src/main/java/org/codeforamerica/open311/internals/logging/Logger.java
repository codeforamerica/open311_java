package org.codeforamerica.open311.internals.logging;

/**
 * Defines all the required operations to log events in the library.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public interface Logger {
	
	public final static String TAG = "open311_java";

	/**
	 * Logs a non-critical event.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void logInfo(String message);

	/**
	 * Logs an error.
	 * 
	 * @param message
	 *            Message to log.
	 */
	public void logError(String message);

}
