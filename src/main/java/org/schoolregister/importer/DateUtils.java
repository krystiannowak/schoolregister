package org.schoolregister.importer;

import java.util.*;

/**
 * 
 * @author Krystian Nowak
 * 
 */
public class DateUtils {
	private static final String[] MONTH_NAMES = { "stycznia", "lutego",
			"marca", "kwietnia", "maja", "czerwca", "lipca", "sierpnia",
			"września", "października", "listopada", "grudnia" };

	public static String extractDayMonth(Calendar date) {
		int day = date.get(Calendar.DAY_OF_MONTH);
		int month = date.get(Calendar.MONTH);
		return Integer.toString(day) + " " + MONTH_NAMES[month];
	}

	public static String extractYear(Calendar date) {
		return Integer.toString(date.get(Calendar.YEAR));
	}

	public static Calendar stringToDate(String str) {
		int day;
		int month;
		int year;

		StringTokenizer dateT = new StringTokenizer(str, ".");
		if (dateT.hasMoreTokens()) {
			day = Integer.parseInt(dateT.nextToken());
			if (dateT.hasMoreTokens()) {
				month = Integer.parseInt(dateT.nextToken());
				if (dateT.hasMoreTokens()) {
					year = Integer.parseInt(dateT.nextToken());

					Calendar cal = Calendar.getInstance();
					cal.set(year, month - 1, day);
					return cal;
				}
			}
		}

		return null;
	}

}
