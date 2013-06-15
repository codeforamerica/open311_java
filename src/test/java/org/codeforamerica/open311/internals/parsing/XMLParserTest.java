package org.codeforamerica.open311.internals.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.Attribute.Datatype;
import org.codeforamerica.open311.facade.data.Service.Type;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.internals.network.MockNetworkManager;
import org.codeforamerica.open311.internals.network.NetworkManager;
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

	/**
	 * Tests a correct service list XML parsing.
	 */
	@Test
	public void serviceListParsingTest() throws MalformedURLException,
			IOException {
		DataParser parser = new XMLParser();
		List<Service> services = parser.parseServiceList(netManager.doGet(
				new URL(BASE_URL + "/services.xml"), null));
		assertEquals(services.size(), 2);
		Service service1 = services.get(0);
		assertEquals(service1.getServiceCode(), "001");
		assertEquals(service1.getServiceName(), "Cans left out 24x7");
		assertEquals(service1.getDescription(),
				"Garbage or recycling cans that have been left "
						+ "out for more than 24 hours after collection. "
						+ "Violators will be cited.");
		assertTrue(service1.hasMetadata());
		assertEquals(service1.getType(), Type.REALTIME);
		assertEquals(service1.getGroup(), "sanitation");
		String[] keywordList = service1.getKeywords();
		assertEquals(keywordList.length, 3);
		assertEquals(keywordList[0], "lorem");
		assertEquals(keywordList[1], "ipsum");
		assertEquals(keywordList[2], "dolor");
	}

	/**
	 * Tests a correct service definition list XML parsing.
	 */
	@Test
	public void serviceDefinitionParsingTest() throws MalformedURLException,
			IOException {
		DataParser parser = new XMLParser();
		ServiceDefinition serviceDefinition = parser
				.parseServiceDefinition(netManager.doGet(new URL(BASE_URL
						+ "/services/001.xml"), null));
		assertEquals(serviceDefinition.getServiceCode(), "DMV66");
		Attribute at1 = serviceDefinition.getAttributes().get(0);
		assertEquals(at1.isVariable(), true);
		assertEquals(at1.getCode(), "WHISHETN");
		assertEquals(at1.getDatatype(), Datatype.SINGLEVALUELIST);
		assertEquals(at1.isRequired(), true);
		assertEquals(at1.getDatatypeDescription(), "");
		assertEquals(at1.getOrder(), 1);
		assertEquals(at1.getDescription(), "What is the ticket/tag/DL number?");
		assertEquals(at1.getValues().get("123"), "Ford");
		assertEquals(at1.getValues().get("124"), "Chrysler");
	}
}
