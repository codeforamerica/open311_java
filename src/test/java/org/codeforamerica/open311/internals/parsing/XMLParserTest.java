package org.codeforamerica.open311.internals.parsing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.codeforamerica.open311.facade.GlobalTests;
import org.codeforamerica.open311.facade.data.PostServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;
import org.codeforamerica.open311.facade.exceptions.DataParsingException;
import org.codeforamerica.open311.facade.exceptions.GeoReportV2Error;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.codeforamerica.open311.internals.network.NetworkManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests of the {@link XMLParser} class.
 * 
 * Please, <b>check</b> {@link MockNetworkManager} to find out what should
 * return each <code>netManager.doGet or .doPost</code> methods.
 * 
 * @author Santiago Munín <santimuni@gmail.com>
 * 
 */
public class XMLParserTest {
	private NetworkManager netManager = new MockNetworkManager();
	private static final String BASE_URL = "http://www.fakeurl";

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[XML PARSER TEST] Starts");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[XML PARSER TEST] Ends");
	}

	/**
	 * Tests a correct service list XML parsing.
	 */
	@Test
	public void serviceListParsingTest() throws MalformedURLException,
			IOException, DataParsingException {
		DataParser parser = new XMLParser();
		List<Service> services = parser.parseServiceList(netManager
				.doGet(new URL(BASE_URL + "/services.xml")));
		GlobalTests.serviceListTest(services);
	}

	/**
	 * Tests if an exception is thrown if a wrong XML is given.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceListParsingWithErrorsTest()
			throws MalformedURLException, IOException, DataParsingException {
		DataParser parser = new XMLParser();
		String dataWithError = netManager.doGet(new URL(BASE_URL
				+ "/services.xml"))
				+ "ERRORSTRING";
		parser.parseServiceList(dataWithError);
	}

	/**
	 * Tests a correct service definition list XML parsing.
	 */
	@Test
	public void serviceDefinitionParsingTest() throws MalformedURLException,
			IOException, DataParsingException {
		DataParser parser = new XMLParser();
		ServiceDefinition serviceDefinition = parser
				.parseServiceDefinition(netManager.doGet(new URL(BASE_URL
						+ "/services/001.xml")));
		GlobalTests.serviceDefinitionTest(serviceDefinition);

	}

	/**
	 * Tests if an exception is thrown if a wrong XML is given.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceDefinitionParsingWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
		DataParser parser = new XMLParser();
		String dataWithError = netManager.doGet(new URL(BASE_URL
				+ "/services/001.xml"))
				+ "ERRORSTRING";
		parser.parseServiceDefinition(dataWithError);
	}

	/**
	 * Tests if the parser is able to read service request ids.
	 */
	@Test
	public void serviceRequestIdFromATokenTest() throws MalformedURLException,
			IOException, DataParsingException {
		DataParser parser = new XMLParser();
		List<ServiceRequestIdResponse> ids = parser
				.parseServiceRequestIdFromAToken(netManager.doGet(new URL(
						BASE_URL + "/tokens/222.xml")));
		GlobalTests.serviceIdFromTokenTest(ids);
	}

	/**
	 * An exception must be thrown if the XML is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceRequestIdFromATokenTestWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
		DataParser parser = new XMLParser();
		String dataWithError = netManager.doGet(new URL(BASE_URL
				+ "/tokens/001.xml"))
				+ "ERRORSTRING";
		parser.parseServiceRequestIdFromAToken(dataWithError);
	}

	/**
	 * Service requests parsing test.
	 */
	@Test
	public void serviceRequestsTest() throws MalformedURLException,
			IOException, DataParsingException {
		DataParser parser = new XMLParser();
		List<ServiceRequest> list = parser.parseServiceRequests(netManager
				.doGet(new URL(BASE_URL + "/requests.xml")));
		GlobalTests.serviceRequestsTest(list);
	}

	/**
	 * An exception must be thrown if the XML is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceRequestsWithErrorTest() throws MalformedURLException,
			IOException, DataParsingException {
		DataParser parser = new XMLParser();
		String dataWithError = netManager.doGet(new URL(BASE_URL
				+ "/requests.xml"))
				+ "ERRORSTRING";
		parser.parseServiceRequests(dataWithError);
	}

	@Test
	public void postServiceRequestResponseTest() throws MalformedURLException,
			IOException, DataParsingException {
		DataParser parser = new XMLParser();
		List<PostServiceRequestResponse> list = parser
				.parsePostServiceRequestResponse(netManager.doPost(new URL(
						BASE_URL + "/requests.xml"), ""));
		assertEquals(list.size(), 1);
		PostServiceRequestResponse response = list.get(0);
		assertEquals(response.getAccountId(), "");
		assertEquals(response.getToken(), "");
		assertEquals(response.getServiceRequestId(), "293944");
		assertEquals(
				response.getServiceNotice(),
				"The City will inspect and require the responsible party to correct within 24 hours and/or issue a Correction Notice or Notice of Violation of the Public Works Code");
	}

	/**
	 * An exception must be thrown if the XML is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void postServiceRequestResponseWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
		DataParser parser = new XMLParser();
		String dataWithError = netManager.doPost(new URL(BASE_URL
				+ "/requests.xml"), "")
				+ "ERRORSTRING";
		parser.parsePostServiceRequestResponse(dataWithError);
	}

	/**
	 * Tests the correct parsing of GeoReport v2 errors.
	 */
	@Test
	public void geoReportV2ErrorTest() throws MalformedURLException,
			DataParsingException, IOException {
		DataParser parser = new XMLParser();
		List<GeoReportV2Error> list = parser.parseGeoReportV2Errors(netManager
				.doPost(new URL(BASE_URL + "/requests/fail.xml"), ""));
		assertEquals(list.size(), 2);
		GeoReportV2Error error1 = list.get(0);
		assertEquals(error1.getCode(), "403");
		assertEquals(error1.getDescription(),
				"Invalid api_key received -- can't proceed with create_request.");
		GeoReportV2Error error2 = list.get(1);
		assertEquals(error2.getCode(), "404");
		assertEquals(error2.getDescription(), "Whatever");
	}

	/**
	 * An exception must be thrown if the XML is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void geoReportV2ErrorWithErrorTest() throws MalformedURLException,
			IOException, DataParsingException {
		DataParser parser = new XMLParser();
		String dataWithError = netManager.doPost(new URL(BASE_URL
				+ "/requests.xml"), "")
				+ "ERRORSTRING";
		parser.parseGeoReportV2Errors(dataWithError);
	}
}
