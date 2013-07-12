package org.codeforamerica.open311.facade;

import static org.junit.Assert.assertEquals;

import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the {@link APIWrapperFactory} class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class APIWrapperFactoryTest {
	@BeforeClass
	public static void testInitialization() {
		System.out.println("[API WRAPPER FACTORY TEST] Starts");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[API WRAPPER FACTORY TEST] Ends");
	}

	@Test
	public void buildTest() throws APIWrapperException {
		APIWrapper wrapper = new APIWrapperFactory(City.SAN_FRANCISCO,
				EndpointType.TEST).setNetworkManager(new MockNetworkManager())
				.build();
		assertEquals(wrapper.getWrapperInfo(),
				"https://open311.sfgov.org/dev/v2 - TEST");
	}

	@Test
	public void buildFromEndpointTest() throws APIWrapperException {
		APIWrapper wrapper = new APIWrapperFactory("https://www.endpoint.com",
				"jurisdictionId").setNetworkManager(new MockNetworkManager())
				.build();
		assertEquals(wrapper.getWrapperInfo(),
				"https://www.endpoint.com - UNKNOWN");
	}
}
