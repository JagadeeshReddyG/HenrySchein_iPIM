package com.pim.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.pim.exceptions.UnableToParseStringForSpecifiedFormatException;

public class DateandTimeUtils {

	private DateandTimeUtils() {

	}

	public static String getTodaysDate() {
		String pattern = "MM/dd/yyyy";
		String dateInString = new SimpleDateFormat(pattern).format(new Date());
		String replaceLeadZeros = "^0+(?!$)";
		dateInString = dateInString.replaceAll(replaceLeadZeros, "");
		return dateInString;
	}

	public static String getTodaysDateForQualityStatus() {
		String pattern = "M/d/yyyy";
		String dateInString = new SimpleDateFormat(pattern).format(new Date());
		return dateInString;
	}

	public static String getFutureDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 5);
		String addDays = dateFormat.format(c.getTime());
		String replaceLeadZeros = "^0+(?!$)";
		addDays = addDays.replaceAll(replaceLeadZeros, "");
		return addDays;
	}

	public static String getFutureDateFiveDaysAdded() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 5);
		String addDays = dateFormat.format(c.getTime());
		String replaceLeadZeros = "^0+(?!$)";
		addDays = addDays.replaceAll(replaceLeadZeros, "");
		return addDays;
	}

	public static String getPastDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, -1);
		String addDays = dateFormat.format(c.getTime());
		String replaceLeadZeros = "^0+(?!$)";
		addDays = addDays.replaceAll(replaceLeadZeros, "");
		return addDays;
	}

	public static String getPreviousDaysDate() {
	    DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
	    Calendar c = Calendar.getInstance();
	    c.add(Calendar.DATE, -1);
	    return dateFormat.format(c.getTime());
	}
	
	public static String getSpecifiedDayFromCurrentDay(int day) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, day);

		String addDays = dateFormat.format(c.getTime());
		String replaceLeadZeros = "^0+(?!$)";
		addDays = addDays.replaceAll(replaceLeadZeros, "");
		return addDays;
	}


	public static String current_date_form_of_year_month_day() {
		String pattern = "yyyy-MM-dd";
		String dateInString = new SimpleDateFormat(pattern).format(new Date());
		String replaceLeadZeros = "^0+(?!$)";
		dateInString = dateInString.replaceAll(replaceLeadZeros, "");
		return dateInString;
	}

	public static boolean greaterDateComparision(String greaterDate, String lesserDate) {
		boolean result = true;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		try {
			result = sdf.parse(greaterDate).before(sdf.parse(lesserDate));
		} catch (ParseException e) {
			throw new UnableToParseStringForSpecifiedFormatException(
					"Please check the passed strings for date comparision");
		}
		return result;
	}

	public static String getNextDayDate() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 1);
		String addDays = dateFormat.format(c.getTime());
		String replaceLeadZeros= "^0+(?!$)";
		addDays=addDays.replaceAll(replaceLeadZeros, "");
		return addDays;
	}
	
	public static String getFutureDateFromTodaysDateInFormMonthDateYear(int days) {
		DateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, days);
		String futuredate = dateFormat.format(c.getTime());
		String replaceLeadZeros = "^0+(?!$)";
		futuredate = futuredate.replaceAll(replaceLeadZeros, "");
		return futuredate;
	}


}
