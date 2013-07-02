package org.codeforamerica.open311.internals.parsing;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.codeforamerica.open311.facade.Format;
import org.codeforamerica.open311.facade.GlobalTests;
import org.codeforamerica.open311.facade.data.Service;
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

}
