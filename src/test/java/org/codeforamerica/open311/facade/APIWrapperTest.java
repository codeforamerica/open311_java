package org.codeforamerica.open311.facade;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.util.List;

import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.data.operations.GETServiceRequestsFilter;
import org.codeforamerica.open311.facade.data.operations.POSTServiceRequestData;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException;
import org.codeforamerica.open311.internals.caching.NoCache;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.codeforamerica.open311.internals.parsing.DataParser;
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
				new NoCache(), "", "");
		errorWrapper = new APIWrapper("http://www.fakeurl/simulateIOException",
				Format.XML, EndpointType.TEST, new XMLParser(),
				new MockNetworkManager(), new NoCache(), "", "");
		apierrorWrapper = new APIWrapper("http://www.fakeurl/simulateAPIError",
				Format.XML, EndpointType.TEST, new XMLParser(),
				new MockNetworkManager(), new NoCache(), "", "key");
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
		ServiceRequestIdResponse id = wrapper
				.getServiceRequestIdFromToken("222");
		GlobalTests.serviceIdFromTokenTest(id);
	}

	@Test
	public void getServiceRequests() throws APIWrapperException,
			MalformedURLException {
		GETServiceRequestsFilter filter = new GETServiceRequestsFilter();
		// Null parameters or empty strings doesn't count
		assertTrue(filter.getOptionalParametersMap().equals(
				filter.setEndDate(null).setStartDate(null).setServiceCode("")
						.setServiceCode(null).setStatus(null)
						.setServiceRequestId("").setServiceRequestId(null)
						.getOptionalParametersMap()));
		filter = filter.setStatus(Status.OPEN);
		assertTrue(filter.getOptionalParametersMap().containsKey(
				DataParser.STATUS_TAG));
		assertEquals(filter.getOptionalParametersMap().get("status"), "open");
		List<ServiceRequest> serviceRequests = wrapper.getServiceRequests(null);
		GlobalTests.serviceRequestsTest(serviceRequests);
	}

	@Test
	public void getServiceRequest() throws APIWrapperException,
			MalformedURLException {
		ServiceRequest serviceRequest = wrapper.getServiceRequest("006");
		GlobalTests.serviceRequestTest(serviceRequest);
	}

	@Test
	public void postServiceRequest() throws APIWrapperException {

		POSTServiceRequestData postData = new POSTServiceRequestData("001", 1,
				null);
		assertEquals(postData.getBodyRequestParameters(),
				postData.setAddress("").setDescription("").setDeviceId("")
						.setMediaUrl("").getBodyRequestParameters());
		assertEquals(postData.getBodyRequestParameters().get("address_id"), "1");
		assertEquals(postData.getBodyRequestParameters().size(), 2);
		POSTServiceRequestResponse response = wrapper
				.postServiceRequest(postData);
		GlobalTests.postServiceRequestsTest(response);

		postData = new POSTServiceRequestData("001", "address!", null);
		assertEquals(postData.getBodyRequestParameters().size(), 2);
		assertEquals(postData.getBodyRequestParameters().get("address"),
				"address!");
		response = wrapper.postServiceRequest(postData);
		GlobalTests.postServiceRequestsTest(response);

		postData = new POSTServiceRequestData("001", 12F, 8F, null);
		assertEquals(postData.getBodyRequestParameters().size(), 3);
		assertTrue(Float
				.valueOf(postData.getBodyRequestParameters().get("lat")) == 12F);
		assertTrue(Float.valueOf(postData.getBodyRequestParameters()
				.get("long")) == 8F);
		assertEquals(postData.setDeviceId("device id works!")
				.getBodyRequestParameters().get("device_id"),
				"device id works!");
		response = wrapper.postServiceRequest(postData);
		GlobalTests.postServiceRequestsTest(response);
	}

	@Test(expected = APIWrapperException.class)
	public void postServiceRequestWithIOException() throws APIWrapperException {
		errorWrapper.postServiceRequest(new POSTServiceRequestData("001", 0,
				null));
	}

	@Test
	public void apiErrorTest() {
		try {
			apierrorWrapper.postServiceRequest(new POSTServiceRequestData(
					"001", 0, null));
		} catch (APIWrapperException e) {
			GlobalTests.errorTest(e.getGeoReportError());
		}
	}

	@Test
	public void serviceServiceDefinitionRelationship()
			throws APIWrapperException {
		List<Service> services = wrapper.getServiceList();
		Service randomService = services.get(0);
		assertNotNull(randomService.getServiceDefinition());
	}
}
