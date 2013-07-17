package org.codeforamerica.open311.internals.caching;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Basic implementation of the {@link Cache} interface. It uses a properties
 * file to save the data.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class RegularJavaCache extends AbstractCache {
	private Properties properties;

	public RegularJavaCache() {
		super();
		try {
			File cacheFile = new File(FILE);
			if (!cacheFile.exists()) {
				cacheFile.createNewFile();
			}
			this.properties = new Properties();
			this.properties.load(new FileInputStream(cacheFile));
		} catch (IOException e) {
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
		}
	}

}
