package org.codeforamerica.open311.internals.logging;

/**
 * Default implementation of the {@link Logger} interface.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class RegularJavaLogger implements Logger {

	@Override
	public void logInfo(String message) {
		System.out.println("[" + TAG + "]: " + message);
	}

	@Override
	public void logError(String message) {
		System.err.println("[" + TAG + "]: " + message);
	}
}
