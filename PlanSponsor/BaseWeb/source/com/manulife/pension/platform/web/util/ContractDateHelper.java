package com.manulife.pension.platform.web.util;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.environment.valueobject.ContractDatesVO;
import com.manulife.pension.util.DateComparator;

public class ContractDateHelper {
	/**
	 * The fields to compare when comparing two dates.
	 */
	protected static int[] DATE_COMPARISON_FIELDS = new int[]{Calendar.YEAR,
			Calendar.MONTH, Calendar.DATE};

	/**
	 * Convenient method to add dates into this report data. The report
	 * framework expects dropdown values to be a map of [key] [value] pairs.
	 * 
	 * <pre>TRP.26.	User must be able to select a "from" date from a drop down list that includes 
	 * EITHER the last 24 contract month start dates equal or prior to the default "To" date 
	 * OR the the contract effective date and each contract month start date equal to 
	 * 		or after that up to the default "from" date, 
	 * WHICHEVER is fewer.
	 * Example:  If contract month end is the 7th and the contract is older than 24 months, then 
	 * If defaulted " to date"  is Mar 15, 2004, show Apr 8, 2002 to Mar 8, 2004
	 * If defaulted "to date" is Mar 8, 2004, show Apr 8, 2002 to Mar 8, 2004
	 * If defaulted "to date" is Mar 7, 2004, show Mar 8, 2002 to Feb 8, 2004
	 * Example:  If contract month end is the 7th and the contract was effective Jan 5, 2004 
	 * 		and defaulted " to date" is Mar 15, 2004, show Jan 5, 2004, Jan 8, 2004, Feb 8, 2004 and Mar 8, 2004
	 * 
	 * 
	 * TRP.27.	User must be able to select a "To" date from a drop down list that includes the default date and 
	 * EITHER each contract month end date for the 23 months prior to the default "to" date 
	 * OR each contract month end date that is equal to or after the 1st contract month end equal to 
	 * 		or after the contract effective date, up to the default "to" date 
	 * WHICHEVER is fewer.  
	 * Example:  If contract month end is the 7th and the contract is older than 24 months, then
	 * If defaulted " to date" is Mar 15, 2004, show May 7, 2002 to Mar 7, 2004 plus Mar 15, 2004
	 * If defaulted "to date" is Mar 8, 2004, show May 7, 2002 to Mar 7, 2004 plus Mar 8, 2004
	 * If defaulted "to date" is Mar 7, 2004, show Apr 7, 2002 to Feb 7, 2004 plus Mar 7, 2004
	 * Example:  If contract month end is the 7th and the contract was effective Jan 5, 2004 
	 * 		and defaulted " to date" is Mar 15, 2004, show Feb 7, 2004, Mar 7, 2004 plus Mar 15, 2004.
	 * 
	 * </pre>
	 * 
	 * @param toDate
	 *            The to date.
	 */
	public static void populateFromToDates(
			Contract contract,
			List fromDates,
			List toDates)
	{
		ContractDatesVO contractDates = contract.getContractDates();
		Date asOfDate = contractDates.getAsOfDate();
		Date contractEffectiveDate = contract.getEffectiveDate();
		/*
		 * The contract month end dates are sorted in descending order.
		 */
		Collection monthEndDates = contractDates.getMonthEndDates();

		/*
		 * Initialize to and from dates. To dates are contract month end dates +
		 * asOfDate. Max. of 24 entries are available for the to dates.
		 * 
		 */
		toDates.add(asOfDate);
		Calendar cal = Calendar.getInstance();
		/*
		cal.setTime(contractEffectiveDate);
		cal.add(Calendar.MONTH, 1);
		Date lastMonthAfterEffectiveDate = cal.getTime();
		*/
		
		boolean loopedOnce = false;

		for (Iterator it = monthEndDates.iterator(); it.hasNext() && toDates.size() < 24; ) {
			Date contractMonthEnd = (Date) it.next();
			if (DateComparator.compare(contractMonthEnd, contractEffectiveDate /*lastMonthAfterEffectiveDate*/, DATE_COMPARISON_FIELDS) >= 0) {
				if (DateComparator.compare(contractMonthEnd, asOfDate, DATE_COMPARISON_FIELDS) < 0 ) {
					// add the monthend to the to dates
					toDates.add(contractMonthEnd);
					// also we need to add a single from date
					// which is after our most recent month end
					if (!loopedOnce) {
						cal.setTime(contractMonthEnd);
						cal.add(Calendar.DATE, 1);
						fromDates.add(cal.getTime() );
					}
				}
				else if(DateComparator.compare(contractMonthEnd, asOfDate, DATE_COMPARISON_FIELDS) > 0 ){
					toDates.remove(asOfDate);
					toDates.add(contractMonthEnd);
				}
			}
			
			cal.setTime(contractMonthEnd);
			// ******** the order of the following two operations is extremely important
			// for instance: 
			//		Apr 30 gives May 1 then Apr 1 which is what we want
			// but if you reverese the order, we'll have:
			// 		Apr 30 gives March 30 then March 31 which is not what we wanted
			//
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.MONTH, -1);
			Date fromDate = cal.getTime();
			if (DateComparator.compare(fromDate, contractEffectiveDate, DATE_COMPARISON_FIELDS) > 0
					&& DateComparator.compare(fromDate, asOfDate, DATE_COMPARISON_FIELDS) <= 0) {
				fromDates.add(fromDate );
			}
			loopedOnce = true;
		}
		
		// find out what the date is 24 months ago from asOfDate
		Calendar cutoffCal = Calendar.getInstance();
		cutoffCal.setTime(asOfDate);
		cutoffCal.add(Calendar.MONTH, -24);
		Date cutOffDate = cutoffCal.getTime();
	
		// if this is a new contract, or if there are not enough month Start dates
		// make sure the contract effective date is listed
		// 
		if (fromDates.size() < 24 )
		{
			// common log 45147 - only if contract effective date is after or equal to the cutoff date
			if (DateComparator.compare(contractEffectiveDate, cutOffDate, DATE_COMPARISON_FIELDS) >= 0 )
				fromDates.add(contractEffectiveDate);
		}

	}
	
	/**
	 * Method to determine the "from" date for Pending Withdrawal Summary page. 
	 * PWS.26	For “from” date, system will default it to the 1st business day 
	 * of the previous month or contract effective date whichever is the later date.
	 *  
	 * @param contract
	 * @return Date
	 * @throws SystemException 
	 */
	public static Date determinePendingSummaryFromDate(Contract contract) throws SystemException{
		
		/*1)Get the 1st day of  the previous month from the current month.
		 * 2) Calculate the next 6 days from the 1st day of  the previous month.
		 *  (for example: if current date is Sept-07-2010, then the calculated dates will be Oct-01-2010,
		 *  	      Oct-02-2010, Oct-03-2010...... Oct-07-2010)
		 *  */
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, -1);
		Date[] _iDates = new Date[7];
		for(int i = 0; i < 7; i++){
			_iDates[i] = cal.getTime();
			cal.add(Calendar.DATE, 1);
		}
		
		Date[] _oDates;
		Date _1stBusinessDay = null;
		
		try {
			// Statement to get the contract effective date
			Date contractEffectiveDate = contract.getEffectiveDate();
			
			_oDates = AccountServiceDelegate.getInstance().getFilteredNYSEClosureDatesIgnoringEmergencyClosure(null,_iDates);
			// Logic to get the least day from the returned list as 1st business day. 
			if(_oDates != null && _oDates.length > 0){
				_1stBusinessDay = Collections.min(Arrays.asList(_oDates));
			
				//Logic to set the "from" date as the 1st business day of the previous month 
				//or contract effective date whichever is the later date.
				if(DateComparator.compare(_1stBusinessDay,contractEffectiveDate, DATE_COMPARISON_FIELDS) < 0 ){
					_1stBusinessDay = contractEffectiveDate;
				}
			}
		} catch (AccountException e) {
			SystemException se = new SystemException(e, ContractDateHelper.class.getName()+ "determineBusinessDay" +
					"Unchecked exception occured. Input Paramereter is "+
					"dateObjs :"+_iDates);
			throw se;
		}  catch (Exception e) {
			SystemException se = new SystemException(e, ContractDateHelper.class.getName()+ "determineBusinessDay" +
					"Unchecked exception occured. Input Paramereter is "+
					"dateObjs :"+_iDates);
			throw se;
		} 
		return _1stBusinessDay;
	}
	
	/**
	 * Retrieves the next 10 business dates from the current date and 
	 * returns the 10th business day as the toDate for Pending Summary page
	 * 
	 * @return 10th business day from today
	 * @throws SystemException
	 */
	public static Date determinePendingSummaryToDate() throws SystemException {
		Date toDate = null;
		
		try {
			Date[] nDates = AccountServiceDelegate.getInstance().getNextNBusinessDates(new Date(), 10);
			
			if (nDates != null && nDates.length == 10) {
				toDate = nDates[9];
			}
			
		} catch (AccountException e) {
			SystemException se = new SystemException(e, ContractDateHelper.class.getName()+ "determinePendingSummaryToDate" +
					"Unchecked exception occured.");
			throw se;
		}  catch (Exception e) {
			SystemException se = new SystemException(e, ContractDateHelper.class.getName()+ "determinePendingSummaryToDate" +
					"Unchecked exception occured.");
			throw se;
		} 
		
		
		return toDate;
	}
	
	/**
	 * Determine the change History To date
	 * @param toDate
	 * @return
	 * @throws SystemException
	 */
	public static Date determineChangeHistoryToDate(Date toDate) throws SystemException {
		Date selectedToDate = null;
		
		try {
			Date[] nDates = AccountServiceDelegate.getInstance().getNextNBusinessDates(toDate, 10);
			
			
			if (nDates != null && nDates.length == 10) {
				selectedToDate = nDates[0];
			}
			
		} catch (AccountException e) {
			SystemException se = new SystemException(e, ContractDateHelper.class.getName()+ "determineChangeHistoryToDate" +
					"Unchecked exception occured.");
			throw se;
		}  catch (Exception e) {
			SystemException se = new SystemException(e, ContractDateHelper.class.getName()+ "determineChangeHistoryToDate" +
					"Unchecked exception occured.");
			throw se;
		} 
		
		
		return selectedToDate;
	}


	
	
	
	/**
	 * 
	 * Determine the change History From date
	 * @return
	 * @throws SystemException
	 */

	public static Date determineChangeHistoryFromDate() throws SystemException{
		
		/*1)Get the 1st day of  the previous month from the current month.
		 * 2) Calculate the next 6 days from the 1st day of  the previous month.
		 *  (for example: if current date is Sept-07-2010, then the calculated dates will be Oct-01-2010,
		 *  	      Oct-02-2010, Oct-03-2010...... Oct-07-2010)
		 *  */
		Calendar cal = Calendar.getInstance();
	    
		cal.add(Calendar.YEAR,-2);
		return determineChangeHistoryToDate(cal.getTime());
	}
	
	
	/**
	 * 
	 * Determine the change History To date
	 * @return
	 * @throws SystemException
	 */

	public static Date determineChangeHistoryToDate() throws SystemException{
		
		/*1)Get the 1st day of  the previous month from the current month.
		 * 2) Calculate the next 6 days from the 1st day of  the previous month.
		 *  (for example: if current date is Sept-07-2010, then the calculated dates will be Oct-01-2010,
		 *  	      Oct-02-2010, Oct-03-2010...... Oct-07-2010)
		 *  */
		Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
		
		return determineChangeHistoryToDate(cal.getTime());
	}
	
	/**
	 * Gets the instance of FastDateFormat
	 * @param format
	 * @return
	 */
	public static FastDateFormat getDateFormatter(String format){
		return FastDateFormat.getInstance(format);
	}
	/**
	 * Gets the instance of FastDate Format, Locale
	 * @param format
	 * @return
	 */
	public static FastDateFormat getDateFormatterLocale(String format, Locale locale){
		return FastDateFormat.getInstance(format, locale);
	}
	
}