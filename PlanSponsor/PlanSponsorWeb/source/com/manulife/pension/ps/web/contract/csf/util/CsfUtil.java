package com.manulife.pension.ps.web.contract.csf.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.web.contract.csf.CsfConstants;
import com.manulife.pension.service.contract.util.ContractServiceFeatureUtil;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
/* 
 * CsfUtil class 
 * author Ludmila Stern
 */
public class CsfUtil {
private static final FastDateFormat DATE_FORMATTER = ContractDateHelper.getDateFormatter("MM/dd/yyyy");
private static final int DEFAULT_YEAR = 1970; // the Form only has month and date
public static final Map <Object, Object>  FREQUENCYMAP = new HashMap <Object, Object>();
static {
	FREQUENCYMAP.put(CsfConstants.PAYROLL_FREQUENCY_SEMI_MONTHLY, ServiceFeatureConstants.MONTHLY);
	FREQUENCYMAP.put(CsfConstants.PAYROLL_FREQUENCY_MONTHLY, ServiceFeatureConstants.QUARTERLY);
	FREQUENCYMAP.put(CsfConstants.PAYROLL_FREQUENCY_BI_WEEKLY, ServiceFeatureConstants.SEMI_ANNUAL);
}
/**
 * 
 * getNextPlanEntryDate
 * @param basePlanEntryDate
 * @param frequency
 * @param fromDate
 * @return Date 
 * @throws ApplicationException
 * @throws ParseException
 * @throws SystemException
 * the method calculates Next PED for Direct Mail functionality on Csf Page
 * It interfaces with ContractServiceFeatureUtil.calculateNectPED
 * the method sets IPED to small date so it would be ignored during PED calculation
 */
public static Date getNextPlanEntryDate(String basePlanEntryDate, String frequency, Date fromDate, Date initialEnrollmentDate)
throws ApplicationException, ParseException, SystemException {

Date contractNextPED = null;
if (fromDate == null || basePlanEntryDate == null || basePlanEntryDate.trim().length()==0 ||frequency == null
|| frequency.trim().length() == 0 ){
// do nothing and return null; 
}
else 
{
	// first check if initialEnrollmentDate is applicable
	GregorianCalendar ref = new GregorianCalendar();
	ref.setTime(fromDate);
	ref.set(Calendar.HOUR, 0);
	ref.set(Calendar.MINUTE, 0);
	ref.set(Calendar.SECOND, 0);
	ref.set(Calendar.MILLISECOND, 0);
	ref.set(Calendar.AM_PM, Calendar.AM);
	
 
	Date dInitialEnrollmentDate =DATE_FORMATTER.parse("01/01/1008");//pass small to be ignored during calculation of NextPED as per AEDM CR14
	
	//calculate next PED based on the original fromDate (Today) and frequency
		String []  dt =basePlanEntryDate.split("/");
		GregorianCalendar ped = new GregorianCalendar();
		int year =DEFAULT_YEAR;
		year = ped.get(GregorianCalendar.YEAR);
		int month = Integer.parseInt(dt[0])-1;
		int day =Integer.parseInt(dt[1]);
		ped.set(Calendar.YEAR, year);
		ped.set(Calendar.MONTH, month);
		ped.set(Calendar.DAY_OF_MONTH, day);
		ped.set(Calendar.HOUR, 0);
		ped.set(Calendar.MINUTE, 0);
		ped.set(Calendar.SECOND, 0);
		ped.set(Calendar.MILLISECOND, 0);
		ped.set(Calendar.AM_PM, Calendar.AM);
		Date planEntryDate = ped.getTime();
        if (ref.getTime().getTime() <= initialEnrollmentDate.getTime()) {
            fromDate = new Date(initialEnrollmentDate.getTime() + 86400000L);// the next PED should always be after the IED
        }
		//contractNextPED = calculateNextPlanEntryDate(fromDate, dInitialEnrollmentDate, planEntryDate, frequency);
		contractNextPED = ContractServiceFeatureUtil.calculateNextPlanEntryDate(fromDate, dInitialEnrollmentDate, planEntryDate, frequency);
	}
	return contractNextPED;
}
	/**DM CSF331 
	 * If does the plan want direct mail of enrollment materials
	 * is changed to no and the current date within 52 days of the plan’s next applicable plan entry date,
	 * issue a warning message when ‘Save’ is clicked  
	 */ 
	public static boolean isDM52Warning (Date basePlanEntryDate, 
			String frequency, String initialEnrollmentDate) 
	throws SystemException, ParseException, ApplicationException{

		boolean displayWarning = false;

		Date dInitialEnrollmentDate =null;

		if (initialEnrollmentDate !=null && initialEnrollmentDate.length()>0)
			dInitialEnrollmentDate =DATE_FORMATTER.parse(initialEnrollmentDate);

		Date fromDate = new Date();
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(fromDate);  
		
		Calendar iedCalendar = GregorianCalendar.getInstance();
		if(dInitialEnrollmentDate != null){
			calendar.setTime(dInitialEnrollmentDate);  
		}

		Date nextPED = ContractServiceFeatureUtil.calculateNextPlanEntryDate(fromDate,
				dInitialEnrollmentDate, basePlanEntryDate, frequency);

		if(nextPED != null) {
			GregorianCalendar ref = new GregorianCalendar();

			ref.setTime(nextPED);

			// Test if PED day is current day or initial enrollment day 
			if((ref.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
					ref.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) ||
					(ref.get(Calendar.DAY_OF_MONTH) == iedCalendar.get(Calendar.DAY_OF_MONTH) &&
					ref.get(Calendar.MONTH) == iedCalendar.get(Calendar.MONTH))) {
				// Calculate for tomorrow, because the Auto Enrollment Periodic Process 
				// runs 1 day before PED
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				fromDate = calendar.getTime();
				nextPED = ContractServiceFeatureUtil.calculateNextPlanEntryDate(fromDate,
						dInitialEnrollmentDate, basePlanEntryDate, frequency);
			}
		}

		if (nextPED != null && dInitialEnrollmentDate !=null 
				&& nextPED.getTime()!= dInitialEnrollmentDate.getTime()){

			long cutOffDate = nextPED.getTime () - (52L * 86400000L);
			GregorianCalendar cd = new GregorianCalendar();
			cd.setTimeInMillis(cutOffDate);
			if (cutOffDate <= fromDate.getTime())
				displayWarning = true;	
		}
		return displayWarning;
	}

	/**
	 * formats the String to the specified date format
	 * 
	 * @param date
	 * @param fromDateFormat
	 * @param toDateFormat
	 * @return
	 */
	public static String convertDateFormat(
			String date, FastDateFormat fromDateFormat, FastDateFormat toDateFormat) {
	
		String convertedDate = CsfConstants.EMPTY_STRING;
		if(date != null && StringUtils.isNotEmpty(date) && date.trim().length() > 0) {
			try {
				Date d = fromDateFormat.parse(date);
				convertedDate = toDateFormat.format(d);
	
			} catch(ParseException pe){
				// unparsable date - return empty string
			}
		}
		return convertedDate;
	}
	
	
	
	public static boolean isNullOrEmpty(String s){
        boolean isNullorEmpty = false;
        if(s==null || "".equals(s)){
            isNullorEmpty = true; 
        }
        return isNullorEmpty;
    }
	
	  public static boolean isNull(Object obj){
	        boolean isNull = false;
	        if(obj==null){
	            isNull = true; 
	        }
	        return isNull;
	    }
	    
}
