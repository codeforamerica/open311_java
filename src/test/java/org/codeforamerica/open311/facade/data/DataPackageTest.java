package org.codeforamerica.open311.facade.data;

import static org.junit.Assert.assertEquals;

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
		assertEquals(Attribute.Datatype.getFromString("string"),
				Attribute.Datatype.STRING);
		assertEquals(Attribute.Datatype.getFromString("number"),
				Attribute.Datatype.NUMBER);
		assertEquals(Attribute.Datatype.getFromString("datetime"),
				Attribute.Datatype.DATETIME);
		assertEquals(Attribute.Datatype.getFromString("text"),
				Attribute.Datatype.TEXT);
		assertEquals(Attribute.Datatype.getFromString("singlevaluelist"),
				Attribute.Datatype.SINGLEVALUELIST);
		assertEquals(Attribute.Datatype.getFromString("multivaluelist"),
				Attribute.Datatype.MULTIVALUELIST);
		assertEquals(Attribute.Datatype.getFromString(""), null);
		assertEquals(Attribute.Datatype.getFromString("STRING"),
				Attribute.Datatype.STRING);
		assertEquals(Attribute.Datatype.getFromString("nUmBer"),
				Attribute.Datatype.NUMBER);
		assertEquals(Attribute.Datatype.getFromString("Datetime"),
				Attribute.Datatype.DATETIME);
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
				"", "A random description", "", "", null, null, null, "", "",
				0, 0F, 0F, null);
		assertEquals(request.toString(), "[001] A random description (open)");
	}

}
