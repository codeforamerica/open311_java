package org.codeforamerica.open311.facade;

import java.util.List;

import org.codeforamerica.open311.facade.APIWrapper.EndpointType;
import org.codeforamerica.open311.facade.APIWrapper.Format;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.codeforamerica.open311.internals.parsing.XMLParser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test of the APIWrapper (uses a {@link MockNetworkManager}).
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class APIWrapperTest {

	private static APIWrapper wrapper;

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[API WRAPPER TEST] Starts");
		wrapper = new APIWrapper("http://www.fakeurl/", Format.XML,
				EndpointType.TEST, new XMLParser(), new MockNetworkManager(),
				"", "");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[API WRAPPER TEST] Ends");
	}

	@Test
	public void getServicesTest() throws APIWrapperException {
		List<Service> services = wrapper.getServiceList();
		GlobalTests.serviceListTest(services);
	}

	@Test
	public void getServiceDefinitionTest() throws APIWrapperException {
		ServiceDefinition serviceDefinition = wrapper
				.getServiceDefinition("001");
		GlobalTests.serviceDefinitionTest(serviceDefinition);
	}

	@Test
	public void getServiceRequestIdFromTokenTest() throws APIWrapperException {
		List<ServiceRequestIdResponse> ids = wrapper
				.getServiceRequestIdFromToken("222");
		GlobalTests.serviceIdFromTokenTest(ids);
	}

}
