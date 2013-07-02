package org.codeforamerica.open311.facade.exceptions;

import static org.junit.Assert.assertEquals;

import org.codeforamerica.open311.facade.data.Attribute;
import org.codeforamerica.open311.facade.data.Service;
import org.codeforamerica.open311.facade.data.ServiceRequest;
import org.codeforamerica.open311.facade.exceptions.APIWrapperException.Error;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class APIWrapperExceptionTest {
	@BeforeClass
	public static void testInitialization() {
		System.out.println("[APIWRAPPEREXCEPTION TEST] Starts");
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[APIWRAPPEREXCEPTION TEST] Ends");
	}

	@Test
	public void apiWrapperExceptionTest() {
		try {
			throw new APIWrapperException("Fatal error!", Error.DATA_PARSING, null);
		} catch (APIWrapperException e) {
			assertEquals(e.toString(),
					"Fatal error! (Problem while parsing text data.)");
		}
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

}
