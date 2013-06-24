package org.codeforamerica.open311.internals.parsing;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Provides operations to handle date parsing. Check the <a
 * href="http://wiki.open311.org/GeoReport_v2#Date.2Ftime_format">GeoReport
 * wiki</a> for more information.
 * 
 * Singleton class.
 * 
 * @author Santiago Munín <santimunin@gmail.com>
 * 
 */
public class DateParsingUtils {

	private static DateParsingUtils instance = new DateParsingUtils();
	private DateTimeFormatter dateFormat = ISODateTimeFormat.dateTimeNoMillis();

	private DateParsingUtils() {
	}

	public static DateParsingUtils getInstance() {
		return instance;
	}

	/**
	 * Parses a string date.
	 * 
	 * @param rawDate
	 *            ISO 8601 format.
	 * @return A date object.
	 */
	public Date parseDate(String rawDate) {
		return dateFormat.parseDateTime(rawDate).toDate();
	}

	/**
	 * Prints a date with the ISO 8601 format.
	 * 
	 * @param date
	 *            Date to print.
	 * @return ISO 8601 format date.
	 */
	public String printDate(Date date) {
		return dateFormat.print(new DateTime(date));
	}
}
