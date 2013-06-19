package org.codeforamerica.open311.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.Attribute.Datatype;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceDefinition;
import org.codeforamerica.open311.facade.data.ServiceRequestIdResponse;

/**
 * This class contains test which will be use for more than one test class. For
 * example, the data parsing of a GET Service has to return the same as the
 * whole GET Service done by the APIWrapper.
 */
public class GlobalTests {

	public static void serviceListTest(List<Service> services) {
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

	public static void serviceDefinitionTest(ServiceDefinition serviceDefinition) {
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

	public static void serviceIdFromTokenTest(List<ServiceRequestIdResponse> ids) {
		assertEquals(ids.size(), 2);
		ServiceRequestIdResponse id1 = ids.get(0);
		ServiceRequestIdResponse id2 = ids.get(1);
		assertEquals(id1.getToken(), "12345");
		assertEquals(id1.getServiceRequestId(), "638344");
		assertEquals(id2.getToken(), "12345");
		assertEquals(id2.getServiceRequestId(), "111");
	}

}
