package org.codeforamerica.open311.internals.logging;

/**
 * Default implementation of the {@link Logger} interface.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class RegularJavaLogger implements Logger {
	private org.slf4j.Logger logger;

	public RegularJavaLogger() {
		this.logger = org.slf4j.LoggerFactory.getLogger(TAG);
	}

	@Override
	public void logInfo(String message) {
		this.logger.info(("[" + TAG + "]: " + message));
	}

	@Override
	public void logError(String message) {
		this.logger.error("[" + TAG + "]: " + message);
	}
}
