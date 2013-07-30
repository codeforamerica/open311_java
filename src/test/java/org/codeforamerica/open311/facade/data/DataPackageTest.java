package org.codeforamerica.open311.facade.data;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.codeforamerica.open311.facade.data.ServiceRequest.Status;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests data classes.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class DataPackageTest {
	@BeforeClass
	public static void testInitialization() {
		System.out.println("[DATA PACKAGE TEST] Starts");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[DATA PACKAGE TEST] Ends");
	}

	@Test
	public void serviceTypeTest() {
		assertEquals(Service.Type.getFromString("realtime"),
				Service.Type.REALTIME);
		assertEquals(Service.Type.getFromString("batch"), Service.Type.BATCH);
		assertEquals(Service.Type.getFromString("blackbox"),
				Service.Type.BLACKBOX);
		assertEquals(Service.Type.getFromString("ReAltIme"),
				Service.Type.REALTIME);
		assertEquals(Service.Type.getFromString("BATCH"), Service.Type.BATCH);
		assertEquals(Service.Type.getFromString("Blackbox"),
				Service.Type.BLACKBOX);
		assertEquals(Service.Type.getFromString(""), null);
		assertEquals(Service.Type.getFromString("asda"), null);
	}

	@Test
	public void serviceRequestStatusTest() {
		assertEquals(ServiceRequest.Status.getFromString("open"),
				ServiceRequest.Status.OPEN);
		assertEquals(ServiceRequest.Status.getFromString("closed"),
				ServiceRequest.Status.CLOSED);
		assertEquals(ServiceRequest.Status.getFromString("OpEn"),
				ServiceRequest.Status.OPEN);
		assertEquals(ServiceRequest.Status.getFromString("Closed"),
				ServiceRequest.Status.CLOSED);
		assertEquals(ServiceRequest.Status.getFromString("ClosedOpen"), null);
	}

	@Test
	public void attributeTypeTest() {
		assertEquals(AttributeInfo.Datatype.getFromString("string"),
				AttributeInfo.Datatype.STRING);
		assertEquals(AttributeInfo.Datatype.getFromString("number"),
				AttributeInfo.Datatype.NUMBER);
		assertEquals(AttributeInfo.Datatype.getFromString("datetime"),
				AttributeInfo.Datatype.DATETIME);
		assertEquals(AttributeInfo.Datatype.getFromString("text"),
				AttributeInfo.Datatype.TEXT);
		assertEquals(AttributeInfo.Datatype.getFromString("singlevaluelist"),
				AttributeInfo.Datatype.SINGLEVALUELIST);
		assertEquals(AttributeInfo.Datatype.getFromString("multivaluelist"),
				AttributeInfo.Datatype.MULTIVALUELIST);
		assertEquals(AttributeInfo.Datatype.getFromString(""), null);
		assertEquals(AttributeInfo.Datatype.getFromString("STRING"),
				AttributeInfo.Datatype.STRING);
		assertEquals(AttributeInfo.Datatype.getFromString("nUmBer"),
				AttributeInfo.Datatype.NUMBER);
		assertEquals(AttributeInfo.Datatype.getFromString("Datetime"),
				AttributeInfo.Datatype.DATETIME);
	}

	@Test
	public void serviceTest() {
		Service service = new Service("001", "Name", "Description", false,
				null, null, null);
		assertEquals(service.toString(), "[001] Name (Description)");
	}

	@Test
	public void serviceRequestTest() {
		ServiceRequest request = new ServiceRequest("001", Status.OPEN, "", "",
				"", "A random description", "", "", null, null, null, "", 0, 0,
				0F, 0F, null);
		assertEquals(request.toString(), "[001] A random description (open)");
	}

	@Test
	public void attributeTest() {
		Attribute att = new SingleValueAttribute("CODE1", "VALUE1");
		Map<String, String> parameters = att.generatePOSTRequestParameter();
		assertEquals(parameters.get("attribute[CODE1]"), "VALUE1");

		Attribute multiAtt = new MultiValueAttribute("CODE1", "VALUE1",
				"VALUE2", "VALUE3");
		parameters = multiAtt.generatePOSTRequestParameter();
		assertEquals(parameters.size(), 3);
		assertEquals(parameters.get("attribute[CODE1][0]"), "VALUE1");
		assertEquals(parameters.get("attribute[CODE1][1]"), "VALUE2");
		assertEquals(parameters.get("attribute[CODE1][2]"), "VALUE3");
	}

}
