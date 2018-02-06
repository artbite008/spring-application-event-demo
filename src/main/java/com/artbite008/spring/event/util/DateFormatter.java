package com.artbite008.spring.event.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;

/**
 * Required by SAP REST API Harmonization.
 * Base on ISO 8601 and RFC 3339, use UTC time zone by default.
 *
 */
public class DateFormatter {

	private static String ISO_UTC_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'"; // Quoted "Z" to indicate UTC, no timezone offset
	
	private static String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	
	private static TimeZone UTC_TimeZone = TimeZone.getTimeZone("UTC");
	
	/**
	 * Convert java.util.Date to String in UTC timezone
	 * 
	 * @param datetime the datetime to be converted
	 * @return string representation of the datetime in UTC
	 */
	public static String getISODateTimeStr(Date datetime) {
		DateFormat df = new SimpleDateFormat(ISO_UTC_DATETIME_FORMAT); 
		df.setTimeZone(UTC_TimeZone);
		return df.format(datetime);
	}
	
	/**
	 * Convert java.utilDate to String based timeZoneId
	 * 
	 * @param datetime
	 * @param timeZoneId
	 * @return
	 */
	public static String getISODateTimeStr(Date datetime, String timeZoneId) {
		DateFormat df = new SimpleDateFormat(ISO_DATETIME_FORMAT); 
		df.setTimeZone(TimeZone.getTimeZone(timeZoneId));
		String string = df.format(datetime); //always ends with 'Z'
		string += getTimeZoneOffset(timeZoneId);
		return string;
	}
	
	/**
	 * Convert datetime representation String to java.util.Date
	 * @param time
	 * @return
	 */
	public static Date getISODateTime(String datetime) {
		return DatatypeConverter.parseDateTime(datetime).getTime();
	}
	
	/**
	 * Get timezone offset's string representation according to ISO 8601
	 * such as: Etc/UTC Z, Hongkong +08:00
	 * 
	 * @param timeZoneId
	 * @return string representation of timezone offset
	 */
	private static String getTimeZoneOffset(String timeZoneId) {
		ZoneId timeZone = ZoneId.of(timeZoneId);
		LocalDateTime dt = LocalDateTime.now();
		ZonedDateTime zdt = dt.atZone(timeZone);
	    ZoneOffset offset = zdt.getOffset();
	    String out = String.format("%6s", offset);
	    return out.trim();
	}
}
