package org.codeforamerica.open311.internals.caching.android;

import org.codeforamerica.open311.internals.caching.AndroidCache;

import android.app.Application;
import android.content.Context;

/**
 * Useful to get the application context and build an {@link AndroidCache}
 * object.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class MyApp extends Application {
	private static Context context;

	public void onCreate() {
		context = getApplicationContext();
	}

	public static Context getCustomAppContext() {
		return context;
	}
}
