package org.codeforamerica.open311.internals.parsing;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.codeforamerica.open311.facade.EndpointType;
import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.GlobalTests;
import org.codeforamerica.open311.facade.data.Endpoint;
import org.codeforamerica.open311.facade.data.POSTServiceRequestResponse;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceDiscoveryInfo;
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
	private DateParser dateParser = new DateParser();
	private NetworkManager netManager = new MockNetworkManager();
	private DataParser parser = new XMLParser();
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
		ServiceRequestIdResponse id = parser
				.parseServiceRequestIdFromAToken(netManager.doGet(new URL(
						BASE_URL + "/tokens/222.xml")));
		GlobalTests.serviceIdFromTokenTest(id);
	}

	/**
	 * An exception must be thrown if the XML is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void serviceRequestIdFromATokenTestWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
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
		String dataWithError = netManager.doGet(new URL(BASE_URL
				+ "/requests.xml"))
				+ "ERRORSTRING";
		parser.parseServiceRequests(dataWithError);
	}

	@Test
	public void postServiceRequestResponseTest() throws MalformedURLException,
			IOException, DataParsingException {
		POSTServiceRequestResponse response = parser
				.parsePostServiceRequestResponse(netManager.doPost(new URL(
						BASE_URL + "/requests.xml"), null));
		GlobalTests.postServiceRequestsTest(response);
	}

	/**
	 * An exception must be thrown if the XML is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void postServiceRequestResponseWithErrorTest()
			throws MalformedURLException, IOException, DataParsingException {
		String dataWithError = netManager.doPost(new URL(BASE_URL
				+ "/requests.xml"), null)
				+ "ERRORSTRING";
		parser.parsePostServiceRequestResponse(dataWithError);
	}

	/**
	 * Tests the correct parsing of GeoReport v2 errors.
	 */
	@Test
	public void geoReportV2ErrorTest() throws MalformedURLException,
			DataParsingException, IOException {
		List<GeoReportV2Error> list = parser.parseGeoReportV2Errors(netManager
				.doPost(new URL(BASE_URL + "/requests/simulateAPIError.xml"),
						null));
		GlobalTests.errorTest(list);
	}

	/**
	 * An exception must be thrown if the XML is not well formed.
	 */
	@Test(expected = DataParsingException.class)
	public void geoReportV2ErrorWithErrorTest() throws MalformedURLException,
			IOException, DataParsingException {
		String dataWithError = netManager.doPost(new URL(BASE_URL
				+ "/requests.xml"), null)
				+ "ERRORSTRING";
		parser.parseGeoReportV2Errors(dataWithError);
	}

	@Test
	public void testServiceDiscoveryTest() throws MalformedURLException,
			IOException, DataParsingException {
		ServiceDiscoveryInfo serviceDiscoveryInfo = parser
				.parseServiceDiscovery(netManager.doGet(new URL(BASE_URL
						+ "discovery")));
		assertEquals(
				serviceDiscoveryInfo.getContact(),
				"Please email ( content.311@sfgov.org )  or call ( 415-701-2311 ) for assistance or to report bugs");
		assertEquals(serviceDiscoveryInfo.getChangeset(),
				dateParser.parseDate("2011-04-05T17:48:34Z"));
		assertEquals(
				serviceDiscoveryInfo.getKeyService(),
				"To get an API_KEY please visit this website:  http://apps.sfgov.org/Open311API/?page_id=486");
		List<Endpoint> endpoints = serviceDiscoveryInfo.getEndpoints();
		assertEquals(endpoints.size(), 4);
		Endpoint endpoint = endpoints.get(0);
		assertEquals(endpoint.getBestFormat(), Format.XML);
		assertEquals(endpoint.getSpecificationUrl(),
				"http://wiki.open311.org/GeoReport_v2");
		assertEquals(endpoint.getUrl(), "https://open311.sfgov.org/dev/v2");
		assertEquals(endpoint.getType(), EndpointType.TEST);
		assertEquals(endpoint.getChangeset(),
				dateParser.parseDate("2011-04-20T17:48:34Z"));
	}
}
