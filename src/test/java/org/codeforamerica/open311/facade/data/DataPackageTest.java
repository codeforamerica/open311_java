package org.codeforamerica.open311.facade.data;

import static org.junit.Assert.assertEquals;

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

}
