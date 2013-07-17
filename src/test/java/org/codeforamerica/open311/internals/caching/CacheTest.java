package org.codeforamerica.open311.internals.caching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;

import org.codeforamerica.open311.facade.APIWrapper;
import org.codeforamerica.open311.facade.APIWrapperFactory;
import org.codeforamerica.open311.facade.City;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.operations.GETServiceRequestsFilter;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.codeforamerica.open311.internals.platform.PlatformManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CacheTest {
	private Cache cache = PlatformManager.getInstance().buildCache();

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
		cache = PlatformManager.getInstance().buildCache();
	}

	@Test
	public void testServiceDiscoveryCaching() throws APIWrapperException {
		assertNull(cache.retrieveCachedServiceDiscoveryInfo(City.SAN_FRANCISCO));
		APIWrapperFactory wrapperFactory = new APIWrapperFactory(
				City.SAN_FRANCISCO).setCache(cache).setNetworkManager(
				new MockNetworkManager());
		wrapperFactory.build();
		assertNotNull(cache
				.retrieveCachedServiceDiscoveryInfo(City.SAN_FRANCISCO));
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

	@Test
	public void testServiceRequestsCaching() throws APIWrapperException {
		APIWrapperFactory wrapperFactory = new APIWrapperFactory(
				City.SAN_FRANCISCO).setCache(cache).setNetworkManager(
				new MockNetworkManager());
		APIWrapper wrapper = wrapperFactory.build();
		assertNull(cache.retrieveCachedServiceRequests(
				wrapper.getEndpointUrl(),
				new GETServiceRequestsFilter().setServiceCode("001")));
		List<ServiceRequest> requests = wrapper
				.getServiceRequests(new GETServiceRequestsFilter()
						.setServiceCode("001"));
		List<ServiceRequest> cachedRequests = cache
				.retrieveCachedServiceRequests(wrapper.getEndpointUrl(),
						new GETServiceRequestsFilter().setServiceCode("001"));
		assertNotNull(cachedRequests);
		assertEquals(cachedRequests.size(), requests.size());
	}

	@Test
	public void testServiceRequestCaching() throws APIWrapperException {
		APIWrapperFactory wrapperFactory = new APIWrapperFactory(
				City.SAN_FRANCISCO).setCache(cache).setNetworkManager(
				new MockNetworkManager());
		APIWrapper wrapper = wrapperFactory.build();
		assertNull(cache.retrieveCachedServiceRequest(wrapper.getEndpointUrl(),
				"001"));
		ServiceRequest request = wrapper.getServiceRequest("001");
		ServiceRequest cachedRequest = cache.retrieveCachedServiceRequest(
				wrapper.getEndpointUrl(), "001");
		assertNotNull(cachedRequest);
		assertEquals(cachedRequest.getServiceCode(), request.getServiceCode());
		assertEquals(cachedRequest.getDescription(), request.getDescription());
	}

	@Test
	public void testCitiesInfoCaching() {
		assertNull(cache.retrieveCitiesInfo());
		cache.saveCitiesInfo("test");
		assertNotNull(cache.retrieveCitiesInfo());
		assertEquals(cache.retrieveCitiesInfo(), "test");
	}

	@Test
	public void testDeleteCache() {
		cache.saveCitiesInfo("test");
		assertNotNull(cache.retrieveCitiesInfo());
		cache.deleteCache();
		assertNull(cache.retrieveCitiesInfo());
	}

	@Test
	public void testNullCaching() {
		cache.saveCitiesInfo(null);
		assertNull(cache.retrieveCitiesInfo());
		cache.saveListOfServices(null, null);
		assertNull(cache.retrieveCachedServiceList(null));
		cache.saveListOfServices("a", null);
		assertNull(cache.retrieveCachedServiceList("a"));
		cache.saveListOfServices(null, new LinkedList<Service>());
		assertNull(cache.retrieveCachedServiceList(null));
		cache.saveServiceDefinition(null, null, null);
		assertNull(cache.retrieveCachedServiceDefinition(null, null));
		cache.saveServiceDiscovery(null, null);
		assertNull(cache.retrieveCachedServiceDiscoveryInfo(null));
		cache.saveServiceRequestList(null, null, null);
		assertNull(cache.retrieveCachedServiceRequest(null, null));
		cache.saveSingleServiceRequest(null, null, null);
		assertNull(cache.retrieveCachedServiceRequest(null, null));
	}
}
