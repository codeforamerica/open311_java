package org.codeforamerica.open311.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.codeforamerica.open311.facade.APIWrapper.Type;
import org.codeforamerica.open311.facade.data.Service;
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

	private static APIWrapper wrapper;

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[API WRAPPER TEST] Starts");
		wrapper = new APIWrapper("http://www.fakeurl/", "xml", Type.TEST,
				new XMLParser(), new MockNetworkManager(), "", "");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[API WRAPPER TEST] Ends");
	}

	@Test
	public void getServicesTest() throws APIWrapperException {
		List<Service> services = wrapper.getServiceList();
		assertEquals(services.size(), 2);
		Service service1 = services.get(0);
		assertEquals(service1.getServiceCode(), "001");
		assertEquals(service1.getServiceName(), "Cans left out 24x7");
		assertEquals(service1.getDescription(),
				"Garbage or recycling cans that have been left "
						+ "out for more than 24 hours after collection. "
						+ "Violators will be cited.");
		assertTrue(service1.hasMetadata());
		assertEquals(service1.getType(), Service.Type.REALTIME);
		assertEquals(service1.getGroup(), "sanitation");
		String[] keywordList = service1.getKeywords();
		assertEquals(keywordList.length, 3);
		assertEquals(keywordList[0], "lorem");
		assertEquals(keywordList[1], "ipsum");
		assertEquals(keywordList[2], "dolor");

	}

}
