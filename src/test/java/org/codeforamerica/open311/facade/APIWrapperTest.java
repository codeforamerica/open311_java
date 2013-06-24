package org.codeforamerica.open311.facade;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.List;

import org.codeforamerica.open311.facade.APIWrapper.EndpointType;
import org.codeforamerica.open311.facade.APIWrapper.Format;
import org.codeforamerica.open311.facade.data.PostServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
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

	private static APIWrapper wrapper, errorWrapper, apierrorWrapper;

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[API WRAPPER TEST] Starts");
		wrapper = new APIWrapper("http://www.fakeurl/", Format.XML,
				EndpointType.TEST, new XMLParser(), new MockNetworkManager(),
				"", "");
		errorWrapper = new APIWrapper("http://www.fakeurl/simulateIOException",
				Format.XML, EndpointType.TEST, new XMLParser(),
				new MockNetworkManager(), "", "");
		apierrorWrapper = new APIWrapper("http://www.fakeurl/simulateAPIError",
				Format.XML, EndpointType.TEST, new XMLParser(),
				new MockNetworkManager(), "", "");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[API WRAPPER TEST] Ends");
	}

	@Test
	public void getWrapperInfoTest() {
		assertEquals(wrapper.getWrapperInfo(), "http://www.fakeurl/" + " - "
				+ EndpointType.TEST.toString());
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

	@Test
	public void getServiceRequests() throws APIWrapperException,
			MalformedURLException {
		List<ServiceRequest> serviceRequests = wrapper.getServiceRequests(null);
		GlobalTests.serviceRequestsTest(serviceRequests);
	}

	@Test
	public void getServiceRequest() throws APIWrapperException,
			MalformedURLException {
		List<ServiceRequest> serviceRequests = wrapper.getServiceRequest("006");
		GlobalTests.serviceRequestsTest(serviceRequests);
	}

	@Test
	public void postServiceRequest() throws APIWrapperException {
		wrapper.setAPIKey("key");
		List<PostServiceRequestResponse> responses = wrapper
				.postServiceRequest("001", 0, 0, null, null);
		GlobalTests.postServiceRequestsTest(responses);
		responses = wrapper.postServiceRequest("001", "address_id", "1", null,
				null);
		GlobalTests.postServiceRequestsTest(responses);
		responses = wrapper.postServiceRequest("001", "address_string",
				"Fake street", null, null);
		GlobalTests.postServiceRequestsTest(responses);
	}

	@Test(expected = APIWrapperException.class)
	public void postServiceRequestWithIOException() throws APIWrapperException {
		errorWrapper.postServiceRequest("001", 0, 0, null, null);
	}

	@Test
	public void apiErrorTest() {
		try {
			apierrorWrapper.postServiceRequest("001", 0, 0, null, null);
		} catch (APIWrapperException e) {
			GlobalTests.errorTest(e.getGeoReportErrors());
		}
	}

}
