package org.codeforamerica.open311.internals.platform;

import org.codeforamerica.open311.internals.caching.AndroidCache;
import org.codeforamerica.open311.internals.caching.Cache;
import org.codeforamerica.open311.internals.caching.NoCache;
import org.codeforamerica.open311.internals.caching.RegularJavaCache;
import org.codeforamerica.open311.internals.logging.AndroidLogger;
import org.codeforamerica.open311.internals.logging.Logger;
import org.codeforamerica.open311.internals.logging.RegularJavaLogger;

/**
 * Builds some objects which implementation depends of the execution
 * environment.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class PlatformManager {
	/**
	 * Unique instance.
	 */
	private static PlatformManager instance = new PlatformManager();
	private boolean androidPlatform;

	/**
	 * Prevents other objects to instantiate instances of this class.
	 */
	private PlatformManager() {
		androidPlatform = System.getProperty("java.vm.name").equalsIgnoreCase(
				"Dalvik");
	}

	/**
	 * Returns the unique instance of the class.
	 * 
	 * @return The unique instance of the class.
	 */
	public static PlatformManager getInstance() {
		return instance;
	}

	/**
	 * Builds a cache instance taking care of the execution environment.
	 * 
	 * @return {@link NoCache} under Android, {@link RegularJavaCache}
	 *         otherwise. {@link AndroidCache} has to be built explicitly.
	 */
	public Cache buildCache() {
		return androidPlatform ? new NoCache() : new RegularJavaCache();
	}


}
