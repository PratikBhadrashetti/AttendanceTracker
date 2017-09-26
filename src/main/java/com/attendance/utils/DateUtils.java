package com.attendance.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

/**
 * @author harsha.patil
 *
 */

@Component
public class DateUtils {

	public String getCurrentMonth() {
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		Calendar cal = Calendar.getInstance();
		String month = monthName[cal.get(Calendar.MONTH)];
		return month;

	}
	
	public String getMonthName(int monthNum) {
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };

		//Calendar cal = Calendar.getInstance();
		String month = monthName[monthNum];
		return month;

	}

	public int getCurrentMonthNumber() {

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);

		System.out.println("Month name: " + month);
		return month;

	}

	public int getMonthNumber(String monthName) {

		Date date = null;
		try {
			date = new SimpleDateFormat("MMMM").parse(monthName);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int monthNum = cal.get(Calendar.MONTH);
		
		return monthNum;

	}
	
	public int getYearNumber(String year) {

		Date date = null;
		try {
			date = new SimpleDateFormat("YYYY").parse(year);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int yearNum = cal.get(Calendar.YEAR);
		
		return yearNum;

	}
	
	public int getMaxDaysInMonth(int month, int year) {
	    Calendar cal = Calendar.getInstance();
	    cal.set(year, month, 1);
	    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public String getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(cal.YEAR);
		int day = cal.get(cal.DAY_OF_MONTH);
		return String.valueOf(year);

	}
	
	public int getCurrentYearInInt() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(cal.YEAR);
		int day = cal.get(cal.DAY_OF_MONTH);
		return year;

	}
	
	
	private static Calendar getCalendarForNow() {
	    Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTime(new Date());
	    return calendar;
	}

	private static void setTimeToBeginningOfDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	}

	private static void setTimeToEndofDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	}
	
	public Date MonthBeginingDate()
	{
		Date begining, end;

	    {
	        Calendar calendar = getCalendarForNow();
	        calendar.set(Calendar.DAY_OF_MONTH,
	                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	        setTimeToBeginningOfDay(calendar);
	        begining = calendar.getTime();
	    }

	    {
	        Calendar calendar = getCalendarForNow();
	        calendar.set(Calendar.DAY_OF_MONTH,
	                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	        setTimeToEndofDay(calendar);
	        end = calendar.getTime();
	    }
	    
	    SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
	    String monthBeginString = dateFormat.format(begining);
	    
	    String monthEndString = dateFormat.format(end);
	    
	    try {
			Date monthBeginingDate = dateFormat.parse(monthBeginString);
			Date monthEndingDate =	dateFormat.parse(monthEndString);
			
			SimpleDateFormat df1 = new SimpleDateFormat( "MM/dd/yyyy" );
	        
			/*Date d = new Date();
			int year = d.getYear()+1900;
			int month = d.getMonth()+1;
			System.out.println("CurrentDate "+d.getDate()+" "+month+" "+year);
			
			String dt="08/31/2017";
	        java.util.Date lessThanDate = df1.parse(dt);
			
	        dt="08/01/2017";
	        java.util.Date greterThanDate = df1.parse(dt);
	        
	        System.out.println(lessThanDate+" "+greterThanDate);*/
			return monthBeginingDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public Date MonthEndingDate()
	{
		Date begining, end;

	    {
	        Calendar calendar = getCalendarForNow();
	        calendar.set(Calendar.DAY_OF_MONTH,
	                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	        setTimeToBeginningOfDay(calendar);
	        begining = calendar.getTime();
	    }

	    {
	        Calendar calendar = getCalendarForNow();
	        calendar.set(Calendar.DAY_OF_MONTH,
	                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	        setTimeToEndofDay(calendar);
	        end = calendar.getTime();
	    }
	
	    SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
	    String monthBeginString = dateFormat.format(begining);
	    
	    String monthEndString = dateFormat.format(end);
	    
	    try {
			Date monthBeginingDate = dateFormat.parse(monthBeginString);
			Date monthEndingDate =	dateFormat.parse(monthEndString);
			return monthEndingDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
}
