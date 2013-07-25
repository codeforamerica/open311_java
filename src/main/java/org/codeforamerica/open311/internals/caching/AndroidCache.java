package org.codeforamerica.open311.internals.caching;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Uses the <a href=
 * "http://developer.android.com/reference/android/content/SharedPreferences.html"
 * >SharedPreferences</a> of Android to store the data. Singleton class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class AndroidCache extends AbstractCache {
	/**
	 * Unique instance of the class.
	 */
	private static AndroidCache instance;
	private SharedPreferences preferences;

	/**
	 * Returns the unique instance of the class.
	 * 
	 * @param context
	 *            Android context. Needed the first time is called to build the
	 *            {@link SharedPreferences}.
	 * @return Unique instance of the class.
	 */
	public static AndroidCache getInstance(Context context) {
		if (instance == null) {
			instance = new AndroidCache(context);
		}
		return instance;
	}

	/**
	 * Prevents this class to be instantiate from outside itself.
	 * 
	 * @param context
	 *            Android context. Needed the first time is called to build the
	 *            {@link SharedPreferences}.
	 */
	private AndroidCache(Context context) {
		preferences = context.getSharedPreferences(FILE, 0);
	}

	@Override
	public void deleteCache() {
		Editor editor = preferences.edit();
		editor.clear();
		editor.commit();
	}

	@Override
	protected void saveProperty(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	@Override
	protected String getProperty(String key) {
		return preferences.getString(key, "");
	}
}
