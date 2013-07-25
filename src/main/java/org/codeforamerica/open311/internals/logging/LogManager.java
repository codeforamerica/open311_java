package org.codeforamerica.open311.internals.logging;

import java.util.HashSet;
import java.util.Set;

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
	private static Set<Object> loggedObjects = new HashSet<Object>();

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
	 * @param objectToBeLogged
	 *            Object to be logged.
	 */
	public void activate(Object objectToBeLogged) {
		loggedObjects.add(objectToBeLogged);
	}

	/**
	 * Disables the logging for a concrete wrapper.
	 * 
	 * @param loggedObject
	 *            Object which logging will be disabled.
	 */
	public void disable(Object loggedObject) {
		loggedObjects.remove(loggedObject);
	}

	/**
	 * Logs a non-critical event.
	 * 
	 * @param messageCreator
	 *            origin of the message.
	 * @param message
	 *            Event message.
	 */
	public void logInfo(Object messageCreator, String message) {
		if (loggedObjects.contains(messageCreator)) {
			logger.logInfo(buildMessage(messageCreator, message));
		}
	}

	/**
	 * Logs any problem.
	 * 
	 * @param messageCreator
	 *            origin of the message.
	 * @param message
	 *            Event message.
	 */
	public void logError(Object messageCreator, String message) {
		if (loggedObjects.contains(messageCreator)) {
			logger.logError(buildMessage(messageCreator, message));
		}
	}

	/**
	 * Builds a log message containing the wrapper information.
	 * 
	 * @param messageCreator
	 *            origin of the message.
	 * 
	 * @param message
	 *            Event message.
	 * @return
	 */
	private static String buildMessage(Object messageCreator, String message) {
		return "(" + messageCreator + ") --> " + message;
	}
}
