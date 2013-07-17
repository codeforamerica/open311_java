package org.codeforamerica.open311.facade;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test the {@link EndpointType} enum.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class EndpointTypeTest {

	@Test
	public void fromStringTest() {
		assertEquals(EndpointType.getFromString("acceptation"),
				EndpointType.ACCEPTATION);
		assertEquals(EndpointType.getFromString("test"), EndpointType.TEST);
		assertEquals(EndpointType.getFromString("production"),
				EndpointType.PRODUCTION);
		assertEquals(EndpointType.getFromString(""), null);
	}
}
