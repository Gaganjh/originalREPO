package com.manulife.pension.ps.web.iloans.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {

	public static final String DATE_FORMAT = "MM/dd/yyyy";

	private static final DateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat(
			DATE_FORMAT);

	private static synchronized String  dateFomatMMDDYY(Date inputDate){
		return STANDARD_DATE_FORMAT.format(inputDate);
	}
	
	private static synchronized Date  dateParseMMDDYY(String value) throws ParseException{
		return STANDARD_DATE_FORMAT.parse(value);
	}
	
	static {
		STANDARD_DATE_FORMAT.setLenient(false);
	}

	/**
	 * DataFormatter constructor comment.
	 */
	public DateFormatter() {
		super();
	}

	public static String format(Date inputDate) {

		if (inputDate == null)
			return null;

		return dateFomatMMDDYY(inputDate);
	}

	public static Date parse(String value) throws ParseException {

		return dateParseMMDDYY(value);
	}
}