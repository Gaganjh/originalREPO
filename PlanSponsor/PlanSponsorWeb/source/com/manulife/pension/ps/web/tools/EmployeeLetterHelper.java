package com.manulife.pension.ps.web.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.DefaultInvestmentFundVO;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;


public class EmployeeLetterHelper {
	static private final Logger logger = Logger
			.getLogger(EmployeeLetterHelper.class);
	
	/**
	 * SimpleDateFormat is converted to FastDateFormat to make it thread safe
	 */
	private static final FastDateFormat DISPLAY_DATE_FORMATTER =FastDateFormat.getInstance("MMM dd, yyyy");
		
	public static String getDefaultInvestmentOptions(int contractNumber) throws SystemException
	{
		 if(logger.isDebugEnabled()) {
			    logger.debug("entry EmployeeLetterHelper -> getDefaultInvestmentOptions");
		    }
		StringBuffer dio = new StringBuffer();
		ContractProfileVO cpVO = ContractServiceDelegate.getInstance().getContractProfileDetails(
				contractNumber, Environment.getInstance().getSiteLocation());
		Map <String, String> familyNames = EnvironmentServiceHelper.getInstance().getLifecycleFamilyNames();
				
		if(cpVO != null)
		{
			Collection <DefaultInvestmentFundVO> dios = cpVO.getDefaultInvestments();
			trimLifecycleLifestyleFundsFromColl(dios);
			
			Iterator diosIt = dios.iterator();
			while(diosIt.hasNext())
			{
				DefaultInvestmentFundVO dif = (DefaultInvestmentFundVO)diosIt.next();
				if(dif.isLifeCycleFund()){
					dio.append(familyNames.get(dif.getFamilyFundCd()));
					
				}else if(!dif.isLifeCycleFund()){
					dio.append(dif.getFundName());
				}
				
				if(dif.getPercentage() != 100)
				{
					dio.append(" - ");
					dio.append(dif.getPercentage());
					dio.append("% ");
				}
				dio.append("; ");
			}
		}
		
		if(logger.isDebugEnabled()) {
		    logger.debug("EmployeeLetterHelper -> getDefaultInvestmentOptions");
	    }
	    
		if(dio.toString().trim().length() > 0 && dio.toString().trim().endsWith(";"))
		{
			return dio.toString().trim().substring(0, dio.toString().trim().length()-1);
		}
		return dio.toString();
		
	}
	
	public static String formatDate(Date d)
	{
		String formattedDate = "";
		if(d != null)
		{
			formattedDate = DISPLAY_DATE_FORMATTER.format(d);
		}
		
		return formattedDate;
	}

	public static String calculateOptOutDeadline(Date d, String optOutDays)
	{
		int ood = 10;
		if (d == null) {
			return  "";
		}
		if(optOutDays != null)
		{
			try{
				ood = Integer.parseInt(optOutDays);
			}catch(NumberFormatException ne )
			{
				// default to 10 if no valid value is entered
			}
		}
		
		Date optOutDate = new Date(d.getTime() - ood * 86000000L);
		
		return formatDate(optOutDate);
	}
	
	public static Date calculatePreviousPED(Date nextPED, Date initialPED, String frequency)
	{
		
		if(nextPED.getTime() == initialPED.getTime())
			return new Date(86400000L);
		
		int rolledMonth = 12;
		
		if(frequency.equalsIgnoreCase(ServiceFeatureConstants.MONTHLY))
			rolledMonth = 1;
		else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.QUARTERLY))
			rolledMonth = 3;
		else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.SEMI_ANNUAL))
			rolledMonth = 6;
		
		GregorianCalendar nextDate = new GregorianCalendar();
		nextDate.setTime(nextPED);
		
		if(nextDate.get(Calendar.MONTH)-rolledMonth < 0)
		{
			nextDate.roll(Calendar.YEAR, -1);
		}
		
		nextDate.roll(Calendar.MONTH, -rolledMonth);
		
		
		
		if(initialPED.getTime() > nextDate.getTimeInMillis())
			return initialPED;
		
		return nextDate.getTime();
	
	}
	
	/**
	 * This is a helper method that parses through the collection of Default DIO collection 
	 * and keeps only one fund for RC and one fund for RL, if those funds
	 * are DIO options for the contract.
	 * @param coll
	 * 
	 */
	public static void trimLifecycleLifestyleFundsFromColl (Collection <DefaultInvestmentFundVO> coll) {
		Collection <DefaultInvestmentFundVO> trimmedCollection = new ArrayList <DefaultInvestmentFundVO> (); 
		
		for (Iterator iter = coll.iterator(); iter.hasNext();) {
			DefaultInvestmentFundVO fund = (DefaultInvestmentFundVO) iter.next();
			if(fund.isLifeCycleFund()) {
				boolean hasSuite = isLifecycleFundExistsInCollection (trimmedCollection, fund.getFamilyFundCd());
				if(!hasSuite) {
					trimmedCollection.add(fund);
				}
			}else {
				trimmedCollection.add(fund);
			}
		}
		coll.clear();
		coll.addAll(trimmedCollection);
	}
	
	/**
	 * Checks if Lifecycle fund already exists in the collection.
	 * @param coll
	 * @param code
	 * @return boolean
	 */
	private static boolean isLifecycleFundExistsInCollection (Collection<DefaultInvestmentFundVO> coll, 
								String code) {
		boolean isExists = false;
		Iterator<DefaultInvestmentFundVO> it = coll.iterator();
		while (it.hasNext()) {
			DefaultInvestmentFundVO vo = it.next();
			if(StringUtils.equals(code, vo.getFamilyFundCd())) {
				isExists = true;
				break;
			}
		}
		return isExists;
		
	}
		
}
