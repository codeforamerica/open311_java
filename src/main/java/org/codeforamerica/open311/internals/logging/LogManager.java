package org.codeforamerica.open311.internals.logging;

import org.codeforamerica.open311.internals.platform.PlatformManager;

/**
 * Manages all the login related operations.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class LogManager {
	/**
	 * Unique instance of the class.
	 */
	private static Logger logger = PlatformManager.getInstance().buildLogger();
	/**
	 * <code>true</code> if the logging is activated.
	 */
	private static boolean working;

	/**
	 * Prevents other classes to instantiate this class.
	 */
	private LogManager() {
	}

	/**
	 * Activates the logging.
	 */
	public static void activate() {
		working = true;
	}

	/**
	 * Disables the logging.
	 */
	public static void disable() {
		working = false;
	}

	/**
	 * Logs a non-critical event.
	 * 
	 * @param message
	 *            Event message.
	 */
	public static void logInfo(String message) {
		if (working) {
			logger.logInfo(message);
		}
	}

	/**
	 * Logs any problem.
	 * 
	 * @param message
	 *            Event message.
	 */
	public static void logError(String message) {
		if (working) {
			logger.logError(message);
		}
	}

}
