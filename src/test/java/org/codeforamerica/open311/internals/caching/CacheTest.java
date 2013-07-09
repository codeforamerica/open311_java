package org.codeforamerica.open311.internals.caching;

import static org.junit.Assert.*;

import java.util.List;

import org.codeforamerica.open311.facade.APIWrapper;
import org.codeforamerica.open311.facade.APIWrapperFactory;
import org.codeforamerica.open311.facade.City;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CacheTest {
	private Cache cache = CacheFactory.getInstance().buildCache();

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[CACHE TEST] Starts");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[CACHE TEST] Ends");
	}

	@Before
	public void cleanUp() {
		System.out.println("Deleting the existing cache.");
		cache.deleteCache();
		cache = CacheFactory.getInstance().buildCache();
	}

	@Test
	public void testServiceDiscoveryCaching() throws APIWrapperException {
		Cache cache = CacheFactory.getInstance().buildCache();
		assertNull(cache.retrieveCachedServiceDiscoveryInfo(City.SAN_FRANCISCO));
		APIWrapperFactory wrapperFactory = new APIWrapperFactory(
				City.SAN_FRANCISCO).setCache(cache).setNetworkManager(
				new MockNetworkManager());
		wrapperFactory.build();
		assertNotNull(cache
				.retrieveCachedServiceDiscoveryInfo(City.SAN_FRANCISCO));
		cache.deleteCache();
	}

	@Test
	public void testServiceListCaching() throws APIWrapperException {
		APIWrapperFactory wrapperFactory = new APIWrapperFactory(
				City.SAN_FRANCISCO).setCache(cache).setNetworkManager(
				new MockNetworkManager());
		APIWrapper wrapper = wrapperFactory.build();
		assertNull(cache.retrieveCachedServiceList(wrapper.getEndpointUrl()));
		List<Service> services = wrapper.getServiceList();
		assertNotNull(cache.retrieveCachedServiceList(wrapper.getEndpointUrl()));
		assertEquals(services.size(),
				cache.retrieveCachedServiceList(wrapper.getEndpointUrl())
						.size());
	}

	@Test
	public void testServiceDefinitionCaching() throws APIWrapperException {
		APIWrapperFactory wrapperFactory = new APIWrapperFactory(
				City.SAN_FRANCISCO).setCache(cache).setNetworkManager(
				new MockNetworkManager());
		APIWrapper wrapper = wrapperFactory.build();
		assertNull(cache.retrieveCachedServiceDefinition(
				wrapper.getEndpointUrl(), "001"));
		ServiceDefinition serviceDefinition = wrapper
				.getServiceDefinition("001");
		ServiceDefinition cachedServiceDefinition = cache
				.retrieveCachedServiceDefinition(wrapper.getEndpointUrl(),
						"001");
		assertNotNull(cachedServiceDefinition);
		assertEquals(serviceDefinition.getServiceCode(),
				cachedServiceDefinition.getServiceCode());
		assertEquals(serviceDefinition.getAttributes().size(),
				cachedServiceDefinition.getAttributes().size());
	}
}
