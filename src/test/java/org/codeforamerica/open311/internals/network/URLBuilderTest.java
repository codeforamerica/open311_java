package org.codeforamerica.open311.internals.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the {@link URLBuilder} class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class URLBuilderTest {

	private static final String BASE_URL = "https://api.city.gov/dev/v2";
	private static final String FORMAT = "xml";
	private static final String JURISDICTION_ID = "city.gov";
	private URLBuilder builder = new URLBuilder(BASE_URL, JURISDICTION_ID,
			FORMAT);

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[URL BUILDER TEST] Starts");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[URL BUILDER TEST] Ends");
	}

	@Test
	public void urlBuilderWithoutOptionalArgumentsTest()
			throws MalformedURLException {
		assertEquals(builder.buildGetServiceListUrl().toString(),
				"https://api.city.gov/dev/v2/services.xml?jurisdiction_id="
						+ JURISDICTION_ID);
		assertEquals(builder.buildGetServiceDefinitionUrl("033").toString(),
				"https://api.city.gov/dev/v2/services/033.xml?jurisdiction_id="
						+ JURISDICTION_ID);
		assertEquals(builder.buildGetServiceRequestIdFromATokenUrl("123456")
				.toString(),
				"https://api.city.gov/dev/v2/tokens/123456.xml?jurisdiction_id="
						+ JURISDICTION_ID);
		assertEquals(builder.buildGetServiceRequest("123456").toString(),
				"https://api.city.gov/dev/v2/requests/123456.xml?jurisdiction_id="
						+ JURISDICTION_ID);
	}

	@Test
	public void urlBuilderWithOptionalArgumentsTest()
			throws MalformedURLException {
		Map<String, String> arguments = new HashMap<String, String>();
		arguments.put("lat", "8.12");
		arguments.put("long", "4.12");
		arguments.put("account_id", "1");
		arguments.put("api_key", "2");
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("ATTRIBUTE[code]", "value1");
		attributes.put("ATTRIBUTE[code2]", "value2");
		String body = builder
				.buildPostServiceRequestBody(arguments, attributes).toString();
		assertTrue(body.contains("api_key=2"));
		assertTrue(body.contains("lat=8.12"));
		assertTrue(body.contains("long=4.12"));
		assertTrue(body.contains("account_id=1"));
		assertTrue(body.contains("ATTRIBUTE[code]=value1"));
		assertTrue(body.contains("ATTRIBUTE[code2]=value2"));
		assertEquals(StringUtils.countMatches(body, "&"), 5);

		String url = builder.buildPostServiceRequestUrl().toString();
		assertEquals(url,
				"https://api.city.gov/dev/v2/requests.xml?jurisdiction_id=city.gov");
		assertEquals(StringUtils.countMatches(url, "&"), 0);

		arguments = new HashMap<String, String>();
		arguments.put("start_date", "2010-05-24T00:00:00Z");
		arguments.put("end_date", "2010-06-24T00:00:00Z");
		arguments.put("status", "open");
		url = builder.buildGetServiceRequests(arguments).toString();
		assertTrue(url.contains("https://api.city.gov/dev/v2/requests.xml?"));
		assertTrue(url.contains("start_date=2010-05-24T00:00:00Z"));
		assertTrue(url.contains("end_date=2010-06-24T00:00:00Z"));
		assertTrue(url.contains("status=open"));
		assertTrue(url.contains("jurisdiction_id=city.gov"));
		assertEquals(StringUtils.countMatches(url, "&"), 3);

		arguments = new HashMap<String, String>();
		arguments.put("service_request_id", "2");
		arguments.put("status_notes", "test");
		arguments.put("status", "open");
	}
}
