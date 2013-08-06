package org.codeforamerica.open311.internals.caching;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.codeforamerica.open311.internals.platform.PlatformManager;

/**
 * Basic implementation of the {@link Cache} interface. It uses a properties
 * file to save the data. Singleton class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class RegularJavaCache extends AbstractCache {
	private Properties properties;
	/**
	 * Unique instance of the class.
	 */
	private static RegularJavaCache instance = new RegularJavaCache();

	/**
	 * Returns the unique instance of the class.
	 */
	public static RegularJavaCache getInstance() {
		return instance;
	}

	private RegularJavaCache() {
		super();
		try {
			File cacheFile = new File(FILE);
			if (!cacheFile.exists()) {
				cacheFile.createNewFile();
			}
			this.properties = new Properties();
			this.properties.load(new FileInputStream(cacheFile));
		} catch (IOException e) {
			PlatformManager.getInstance().buildLogger()
					.logError("Error loading the cache: " + e.getMessage());
			throw new Error("Couldn't create/load the cache file.");
		}
	}

	@Override
	protected void saveProperty(String key, String value) {
		if (key != null && key.length() > 0 && value != null
				&& value.length() > 0) {
			try {
				properties.setProperty(key, value);
				properties.store(new FileOutputStream(FILE), null);
			} catch (IOException e) {
				PlatformManager.getInstance().buildLogger()
						.logError("Error saving a property: " + e.getMessage());
			}
		}
	}

	@Override
	protected String getProperty(String key) {
		return properties.getProperty(key);
	}

	@Override
	public void deleteCache() {
		try {
			properties.clear();
			properties.store(new FileOutputStream(FILE), null);
		} catch (IOException e) {
			PlatformManager.getInstance().buildLogger()
					.logError("Error deleting the cache: " + e.getMessage());
		}
	}

}
