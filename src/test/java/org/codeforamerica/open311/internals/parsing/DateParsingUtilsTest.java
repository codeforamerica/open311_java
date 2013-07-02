package org.codeforamerica.open311.internals.parsing;

import static org.junit.Assert.*;

import java.util.Date;

import org.joda.time.DateTimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the date parsing features.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class DateParsingUtilsTest {
	private static DateParser dateParsingUtils;
	private static final String ISO8601DATE = "2013-01-01T17:15:00+01:00";
	private static final String BASIC_DATE = "2013-01-01 17:15";
	private static final String INVALID_DATE = " 2013 18:10";
	private static final String DEFAULT_TIME_ZONE_FOR_TESTING_ID = "Europe/Madrid";

	@BeforeClass
	public static void testInitialization() {
		System.out.println("[DATE PARSING UTILS TEST] Starts");
		System.out.println("Setting the timezone "
				+ DEFAULT_TIME_ZONE_FOR_TESTING_ID);
		dateParsingUtils = new DateParser().withTimezone(DateTimeZone
				.forID(DEFAULT_TIME_ZONE_FOR_TESTING_ID));
	}

	@AfterClass
	public static void testFinish() {
		System.out.println("[DATE PARSING UTILS TEST] Ends");
	}

	@Test
	public void date8601ParsingTest() {
		Date date = dateParsingUtils.parseDate(ISO8601DATE);
		assertEquals(dateParsingUtils.printDate(date),
				"2013-01-01T17:15:00+01:00");
	}

	@Test
	public void basicDate() {
		Date date = dateParsingUtils.parseDate(BASIC_DATE);
		assertEquals(dateParsingUtils.printDate(date),
				"2013-01-01T17:15:00+01:00");
	}

	@Test
	public void invalidDate() {
		assertNull(dateParsingUtils.parseDate(INVALID_DATE));
	}

}
