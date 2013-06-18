package org.codeforamerica.open311.internals.network;

import static org.junit.Assert.*;

import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test the {@link URLBuilder} class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class URLBuilderTest {
	private static final String BASE_URL = "https://api.city.gov/dev/v2";
	private static final String FORMAT = "xml";

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[URL BUILDER TEST] Starts");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[URL BUILDER TEST] Ends");
	}

	@Test
	public void urlBuilderTest() throws MalformedURLException {
		URLBuilder builder = new URLBuilder(BASE_URL, FORMAT);
		assertEquals(builder.buildGetServiceListUrl().toString(),
				"https://api.city.gov/dev/v2/services.xml");
		assertEquals(builder.buildGetServiceDefinitionUrl("033").toString(),
				"https://api.city.gov/dev/v2/services/033.xml");
		assertEquals(builder.buildPostServiceRequestUrl().toString(),
				"https://api.city.gov/dev/v2/requests.xml");
		assertEquals(builder.buildGetServicesRequests().toString(), BASE_URL
				+ "/" + "requests" + "." + FORMAT);
		assertEquals(builder.buildGetServiceRequestIdFromATokenUrl("123456")
				.toString(), "https://api.city.gov/dev/v2/tokens/123456.xml");
		assertEquals(builder.buildGetServiceRequest("123456").toString(),
				"https://api.city.gov/dev/v2/requests/123456.xml");

	}
}
