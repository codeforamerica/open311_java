package org.codeforamerica.open311.internals.caching;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Uses the <a href=
 * "http://developer.android.com/reference/android/content/SharedPreferences.html"
 * >SharedPreferences</a> of Android to store the data.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class AndroidCache extends AbstractCache {

	private SharedPreferences preferences;

	public AndroidCache(Context context) {
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
