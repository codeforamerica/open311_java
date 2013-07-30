package org.codeforamerica.open311.facade.exceptions;

import static org.junit.Assert.assertEquals;

import org.codeforamerica.open311.facade.data.AttributeInfo;
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

}
