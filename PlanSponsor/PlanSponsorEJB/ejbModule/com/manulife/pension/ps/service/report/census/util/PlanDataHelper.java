package com.manulife.pension.ps.service.report.census.util;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.PlanData;

/**
 * Helper class to get the Plan related data
 * 
 * @author ayyalsa
 *
 */
public class PlanDataHelper {
	
	/**
	 * static variable for the class name
	 */
	private static final String className = PlanDataHelper.class.getName();

	/**
	 * static variable for the logger 
	 */
	private static final Logger logger = Logger.getLogger(className);
	
    /**
     * Retrieves the plan entry frequency for the 'EEDEF'
     * money type
     * 
     * @param contractNumber
     * @return
     * @throws ApplicationException
     * @throws SystemException
     * @throws SQLException
     */
    public static int getPlanEntryFrequencyForEEDEF(int contractNumber) 
    throws ApplicationException, SystemException, SQLException {
    	
    	if (logger.isDebugEnabled()) {
    		logger.debug("getPlanEntryFrequencyForEEDEF() --> Entry");
        }
    	 
    	String frequency = null;
        int intFreq = 1;
        
        /*
         * get the Plan Entry Frequency for the EEDEF money type
         */
     	frequency = ContractServiceDelegate.getInstance().getPlanFrequency(
     			contractNumber);
 		
		// THIS IS DONE FOR CONTRACTS WITH AE OFF 
		if(frequency == null) return intFreq;
		 
		if(frequency.equalsIgnoreCase(ServiceFeatureConstants.QUARTERLY)) {
		    intFreq = 3;
		} else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.SEMI_ANNUAL)) {
		    intFreq = 6;
		} else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.ANNUAL)) {
		    intFreq = 12;
		}
		
		if (logger.isDebugEnabled()) {
    		logger.debug("getPlanEntryFrequencyForEEDEF() --> exit");
        }
		return intFreq;
    }
    
    /**
     * Returns the Base plan entry date from plan page
     * 
     * @param contractNumber
     * @return
     * @throws SystemException
     */
    public static Date getBasePlanEntryDate(int contractNumber) 
    throws SystemException {
    	Date basePlanEntryDate = null;
    	
    	PlanData planData = 
    		ContractServiceDelegate.getInstance().loadPlanEligibilityData(
    				contractNumber);
    	
    	if(planData != null && planData.getFirstPlanEntryDate() != null) {
    		basePlanEntryDate = planData.getFirstPlanEntryDate().getAsDate();
		}
    	
    	return basePlanEntryDate;
    }
}
