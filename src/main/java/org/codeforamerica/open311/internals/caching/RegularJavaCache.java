package org.codeforamerica.open311.internals.caching;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class RegularJavaCache extends AbstractCache {
	private final static String FILE = "cache.prop";
	private Properties properties;

	/* package */RegularJavaCache() {
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
		File cache = new File(FILE);
		if (cache.exists()) {
			cache.delete();
		}
	}

}
