package org.codeforamerica.open311.internals.logging;

import java.util.HashSet;
import java.util.Set;

import org.codeforamerica.open311.facade.APIWrapper;
import org.codeforamerica.open311.internals.platform.PlatformManager;

/**
 * Manages all the login related operations.
 * 
 * Singleton class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class LogManager {
	/**
	 * Unique instance of the logger.
	 */
	private Logger logger = PlatformManager.getInstance().buildLogger();
	/**
	 * Unique instance of the class.
	 */
	private static LogManager instance = new LogManager();
	/**
	 * Contains a wrapper if it has to be logged.
	 */
	private static Set<APIWrapper> loggedWrappers = new HashSet<APIWrapper>();

	/**
	 * Prevents other classes to instantiate this class.
	 */
	private LogManager() {
	}

	/**
	 * Returns the unique instance of the class.
	 * 
	 * @return unique instance of the class.
	 */
	public static LogManager getInstance() {
		return instance;
	}

	/**
	 * Activates the logging for a concrete wrapper.
	 * 
	 * @param wrapperInstance
	 *            Wrapper to be logged.
	 */
	public void activate(APIWrapper wrapperInstance) {
		loggedWrappers.add(wrapperInstance);
	}

	/**
	 * Disables the logging for a concrete wrapper.
	 * 
	 * @param wrapperInstance
	 *            Wrapper which logging will be disabled.
	 */
	public void disable(APIWrapper wrapperInstance) {
		loggedWrappers.remove(wrapperInstance);
	}

	/**
	 * Logs a non-critical event.
	 * 
	 * @param wrapper
	 *            Origin of the message.
	 * @param message
	 *            Event message.
	 */
	public void logInfo(APIWrapper wrapper, String message) {
		if (loggedWrappers.contains(wrapper)) {
			logger.logInfo(buildMessage(wrapper, message));
		}
	}

	/**
	 * Logs any problem.
	 * 
	 * @param wrapper
	 *            Origin of the message.
	 * @param message
	 *            Event message.
	 */
	public void logError(APIWrapper wrapper, String message) {
		if (loggedWrappers.contains(wrapper)) {
			logger.logError(buildMessage(wrapper, message));
		}
	}

	/**
	 * Builds a log message containing the wrapper information.
	 * 
	 * @param wrapper
	 *            Origin of the message.
	 * @param message
	 *            Event message.
	 * @return
	 */
	private static String buildMessage(APIWrapper wrapper, String message) {
		return "(" + wrapper.getWrapperInfo() + ") --> " + message;
	}
}
