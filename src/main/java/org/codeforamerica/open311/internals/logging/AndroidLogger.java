package org.codeforamerica.open311.internals.logging;

import android.util.Log;

/**
 * Logger compatible with Android.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class AndroidLogger implements Logger {

	@Override
	public void logInfo(String message) {
		Log.i(TAG, message);
	}

	@Override
	public void logError(String message) {
		Log.e(TAG, message);
	}
}
