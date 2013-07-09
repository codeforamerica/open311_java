package org.codeforamerica.open311.internals.caching;

/**
 * Builds a cache taking care of the used platform (regular Java, Android...).
 * 
 * Singleton class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class CacheFactory {

	private static CacheFactory instance = new CacheFactory();

	private CacheFactory() {
	}

	public static CacheFactory getInstance() {
		return instance;
	}

	/**
	 * Builds a suitable Cache object.
	 * 
	 * @return A suitable cache object.
	 */
	public Cache buildCache() {
		if (System.getProperty("java.vm.name").equalsIgnoreCase("Dalvik")) {
			// Android
			return new NoCache();
		} else {
			return new RegularJavaCache();
		}
	}
}
