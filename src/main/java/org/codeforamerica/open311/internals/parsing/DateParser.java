package org.codeforamerica.open311.internals.parsing;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
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
public class DateParser {

	private static DateParser instance = new DateParser();
	/**
	 * List of possible formats. Note the order of preference.
	 */
	private DateTimeFormatter[] dateFormats = {
			ISODateTimeFormat.dateTimeNoMillis(),
			DateTimeFormat.forPattern("YYYY-MM-DD HH:mm") };

	private DateParser() {
	}

	public static DateParser getInstance() {
		return instance;
	}

	/**
	 * Sets the timezone of the system.
	 * 
	 * @param timeZone
	 *            A valid timezone.
	 */
	public void setTimezone(DateTimeZone timeZone) {
		for (int i = 0; i < dateFormats.length; i++) {
			dateFormats[i] = dateFormats[i].withZone(timeZone);
		}
	}

	/**
	 * Parses a string date.
	 * 
	 * @param rawDate
	 *            ISO 8601 is the preferred format. Check
	 *            <code>dateFormats</code> in order to know all the accepted
	 *            formats.
	 * @return A date object.
	 */
	public Date parseDate(String rawDate) {
		for (int i = 0; i < dateFormats.length; i++) {
			try {
				return dateFormats[i].parseDateTime(rawDate).toDate();
			} catch (Exception e) {
			}
		}
		return null;
	}

	/**
	 * Prints a date. ISO 8601 is the preferred format. Check
	 * <code>dateFormats</code> in order to know all the accepted formats.
	 * 
	 * @param date
	 *            Date to print.
	 * @return ISO 8601 format date is possible (else, the first valid) or
	 *         <code>null</code> if it didn't match any format.
	 */
	public String printDate(Date date) {
		for (int i = 0; i < dateFormats.length; i++) {
			try {
				return dateFormats[i].print(new DateTime(date));
			} catch (Exception e) {
			}
		}
		return null;
	}
}
