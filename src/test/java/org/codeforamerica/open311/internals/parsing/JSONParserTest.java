package org.codeforamerica.open311.internals.parsing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.GlobalTests;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JSONParserTest {
	private static NetworkManager netManager = new MockNetworkManager();
	private static JSONParser parser = new JSONParser();
	private static final String BASE_URL = "http://www.fakeurl";

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[JSON PARSER TEST] Starts");
		netManager.setFormat(Format.JSON);
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[JSON PARSER TEST] Ends");
	}

	/**
	 * Tests a correct service list JSON parsing.
	 */
	@Test
	public void serviceListParsingTest() throws MalformedURLException,
			IOException, DataParsingException {
		List<Service> services = parser.parseServiceList(netManager
				.doGet(new URL(BASE_URL + "/services.json")));
		GlobalTests.serviceListTest(services);
	}

	/**
	 * Tests if an exception is thrown if a wrong JSON is given.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceListParsingWithErrorsTest()
			throws MalformedURLException, IOException, DataParsingException {
		String dataWithError = netManager.doGet(
				new URL(BASE_URL + "/services.json")).replace("\"", ":");
		parser.parseServiceList(dataWithError);
	}

	/**
	 * Tests a correct service definition list JSON parsing.
	 */
	@Test
	public void serviceDefinitionParsingTest() throws MalformedURLException,
			IOException, DataParsingException {
		ServiceDefinition serviceDefinition = parser
				.parseServiceDefinition(netManager.doGet(new URL(BASE_URL
						+ "/services/001.json")));
		GlobalTests.serviceDefinitionTest(serviceDefinition);

	}

	/**
	 * Tests if an exception is thrown if a wrong JSON is given.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceDefinitionParsingWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
		String dataWithError = netManager.doGet(
				new URL(BASE_URL + "/services/001.json")).replace("\"", ":");
		parser.parseServiceDefinition(dataWithError);
	}

	/**
	 * Tests if the parser is able to read service request ids.
	 */
	@Test
	public void serviceRequestIdFromATokenTest() throws MalformedURLException,
			IOException, DataParsingException {
		List<ServiceRequestIdResponse> ids = parser
				.parseServiceRequestIdFromAToken(netManager.doGet(new URL(
						BASE_URL + "/tokens/222.json")));
		GlobalTests.serviceIdFromTokenTest(ids);
	}

	/**
	 * An exception must be thrown if the JSON is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceRequestIdFromATokenTestWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
		String dataWithError = netManager.doGet(
				new URL(BASE_URL + "/tokens/001.json")).replace("\"", ":");
		parser.parseServiceRequestIdFromAToken(dataWithError);
	}

	/**
	 * Service requests parsing test.
	 */
	@Test
	public void serviceRequestsTest() throws MalformedURLException,
			IOException, DataParsingException {
		List<ServiceRequest> list = parser.parseServiceRequests(netManager
				.doGet(new URL(BASE_URL + "/requests.json")));
		GlobalTests.serviceRequestsTest(list);
	}

	/**
	 * An exception must be thrown if the JSON is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceRequestsWithErrorTest() throws MalformedURLException,
			IOException, DataParsingException {
		String dataWithError = netManager.doGet(
				new URL(BASE_URL + "/requests.json")).replace("\"", ":");
		parser.parseServiceRequests(dataWithError);
	}

	@Test
	public void postServiceRequestResponseTest() throws MalformedURLException,
			IOException, DataParsingException {
		List<POSTServiceRequestResponse> list = parser
				.parsePostServiceRequestResponse(netManager.doPost(new URL(
						BASE_URL + "/requests.json"), ""));
		GlobalTests.postServiceRequestsTest(list);
	}

	/**
	 * An exception must be thrown if the JSON is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void postServiceRequestResponseWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
		String dataWithError = netManager.doPost(
				new URL(BASE_URL + "/requests.xml"), "").replace("\"", ":");
		parser.parsePostServiceRequestResponse(dataWithError);
	}

}
