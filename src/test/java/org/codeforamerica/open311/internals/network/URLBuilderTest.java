package org.codeforamerica.open311.internals.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.MultiValueAttribute;
import org.codeforamerica.open311.facade.data.SingleValueAttribute;
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

		List<Attribute> attributes = new LinkedList<Attribute>();
		attributes.add(new SingleValueAttribute("code", "value1"));
		attributes.add(new SingleValueAttribute("code2", "value2"));
		attributes.add(new MultiValueAttribute("code3", "value1", "value2"));
		Map<String, String> bodyParameters = builder
				.buildPostServiceRequestBody(arguments, attributes);
		assertEquals(bodyParameters.get("api_key"),"2");
		assertEquals(bodyParameters.get("lat"),"8.12");
		assertEquals(bodyParameters.get("long"),"4.12");
		assertEquals(bodyParameters.get("account_id"),"1");
		assertEquals(bodyParameters.get("attribute[code]"),"value1");
		assertEquals(bodyParameters.get("attribute[code2]"),"value2");
		assertEquals(bodyParameters.get("attribute[code3][0]"),"value1");
		assertEquals(bodyParameters.get("attribute[code3][1]"),"value2");

		String url = builder.buildPostServiceRequestUrl().toString();
		assertEquals(url, "https://api.city.gov/dev/v2/requests.xml");

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

		arguments = new HashMap<String, String>();
		arguments.put("service_request_id", "2");
		arguments.put("status_notes", "test");
		arguments.put("status", "open");
	}
}
