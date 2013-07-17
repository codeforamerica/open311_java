package org.codeforamerica.open311.facade;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test of the Format enum.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class FormatTest {

	@Test
	public void descriptionTest() {
		assertEquals(Format.XML.getDescription(), "xml");
		assertEquals(Format.JSON.getDescription(), "json");
	}

	@Test
	public void HTTPContentTypeTest() {
		assertEquals(Format.XML.getHTTPContentType(), "text/xml");
		assertEquals(Format.JSON.getHTTPContentType(), "application/json");
	}

	@Test
	public void getFromStringTest() {
		assertEquals(Format.getFromHTTPContentTypeString("text/xml"),
				Format.XML);
		assertEquals(Format.getFromHTTPContentTypeString("application/json"),
				Format.JSON);
		assertEquals(Format.getFromHTTPContentTypeString(""), null);
	}

}
