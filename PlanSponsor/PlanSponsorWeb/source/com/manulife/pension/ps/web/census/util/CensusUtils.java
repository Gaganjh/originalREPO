/**
 * 
 */
package com.manulife.pension.ps.web.census.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EligibilityServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.EmployeeEnrollmentSummaryDAO;
import com.manulife.pension.ps.service.report.census.util.PlanDataHelper;
import com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.StatisticsSummary;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.eligibility.valueobject.PlanEntryRequirementDetailsVO;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;


/**
 * A utility class providing Web tier utility methods for Census.
 * 
 * @author guweigu
 *
 */
public class CensusUtils {
    
    private static final Logger log = Logger.getLogger(CensusUtils.class);
    
    /**
     * Utility method that returns an array with 5 years, 
     * 2 before and 2 after current date
     * 
     * @return
     * @throws SystemException 
     * @throws ApplicationException 
     */
    public static List<LabelValueBean> getPEDYears() {
        List<LabelValueBean> yearsArray = new ArrayList<LabelValueBean>();
        Date currentDate = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(currentDate);
        
        calendar.add(GregorianCalendar.YEAR, -2);
        
        for (int i = 0; i < 5; i++) {
            String year = Integer.toString(calendar.get(GregorianCalendar.YEAR));
            yearsArray.add( new LabelValueBean(year, year));
            calendar.add(GregorianCalendar.YEAR, 1);
        }        
        
        return yearsArray;
    }
    
    /**
     * Retrieves the EEDEF plan entry frequency for the specified contract number
     * and returns as an int value
     * 
     * @param contractNumber
     * @return plan entry frequency for the contract
     * @throws ApplicationException
     * @throws SystemException
     */
    public static int getFrequency(int contractNumber) throws ApplicationException, SystemException {
        int numberOfMonths = 12;
        
        String frequency = getPlanEntryFrequencyForEEDEF(contractNumber);
        
        if (frequency != null){
		    if(frequency.equalsIgnoreCase(ServiceFeatureConstants.MONTHLY)) {
		        numberOfMonths = 1;        
		    } else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.QUARTERLY)) {
		        numberOfMonths = 3;
		    } else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.SEMI_ANNUAL)) {
		        numberOfMonths = 6;
		    }
        }
        
        return numberOfMonths;
    }
    
    /**
     * Retrieves the EEDEF plan entry frequency for the specified contract number
     * @param contractNumber
     * @return
     * @throws SystemException
     */
    private static String getPlanEntryFrequencyForEEDEF(int contractNumber) 
    throws SystemException {
    	String frequency = null;
    	
    	frequency = ContractServiceDelegate.getInstance().getPlanFrequency(
     			contractNumber);
         
    	return frequency;
    }
    
    /**
     * Retrieves the Opt out days for the specified contract
     * 
     * @param contractNumber
     * @return opt out days
     * @throws ApplicationException
     * @throws SystemException
     */
    public static int getOOD(int contractNumber) throws ApplicationException, SystemException {
        ContractServiceFeature contractServiceFeature = 
        	ContractServiceDelegate.getInstance().getContractServiceFeature(
        			contractNumber,ServiceFeatureConstants.MANAGING_DEFERRALS);        
        String optOut = contractServiceFeature.getAttributeValue(
        		ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE);
        
        int optOutNumber = 0;
        
        if(optOut != null) {            
            optOutNumber = Integer.parseInt(optOut);            
        }
        
        return optOutNumber;
    }
    
    /**
     * Utility method that returns an array with day and month for PED during a year
     * 
     * @param contractNumber
     * @return
     * @throws ApplicationException
     * @throws SystemException
     * @throws ParseException
     */
    public static List<LabelValueBean> getPEDMonthAndDay(int contractNumber) 
    throws ApplicationException, SystemException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
        String tempDate = null;
        SortedMap<Date, LabelValueBean> sortedMap = new TreeMap<Date, LabelValueBean>();
        Calendar calendar = GregorianCalendar.getInstance();
        int rolledMonth = 12;   
        int arraySize = 1;
           
        String frequency = getPlanEntryFrequencyForEEDEF(contractNumber);
        Date nextDate = PlanDataHelper.getBasePlanEntryDate(contractNumber);
        
        // Check for data validity
        if(nextDate == null || frequency == null || "".equals(frequency)) {
            return new ArrayList<LabelValueBean>();
        } else {        
            calendar.setTime(nextDate);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    
            if(frequency.equalsIgnoreCase(ServiceFeatureConstants.MONTHLY)) {
                rolledMonth = 1;
                arraySize = 12;            
            } else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.QUARTERLY)) {
                rolledMonth = 3;
                arraySize = 4;
            } else if(frequency.equalsIgnoreCase(ServiceFeatureConstants.SEMI_ANNUAL)) {
                rolledMonth = 6;
                arraySize = 2;
            }
            
            for (int i = 0; i < arraySize; i++) {
                tempDate = dateFormat.format(calendar.getTime());
                try {
                    sortedMap.put(dateFormat.parse(tempDate), new LabelValueBean(tempDate, tempDate));
                } catch (ParseException e) {
                    throw new SystemException("100002");
                }
                calendar.roll(GregorianCalendar.MONTH, rolledMonth);  
                int diff = dayOfMonth - calendar.get(Calendar.DAY_OF_MONTH);
                // roll to last day of month for all months except february
                int m = calendar.get(Calendar.MONTH);
                
                if(diff > 0 && m != 1)
                {
                    if(m == 3 || m == 5 || m ==8 || m ==10)
                    {
                        if(calendar.get(Calendar.DAY_OF_MONTH) + diff > 30)
                            diff--;
                    }
                    if(diff > 0)
                        calendar.roll(Calendar.DAY_OF_MONTH, diff);
                }
            }
            
            return new ArrayList<LabelValueBean>(sortedMap.values());
        }
    }
    
    /**
     * Utility method that returns next PED starting from today 
     * 
     * @return
     * @throws SystemException 
     * @throws ApplicationException 
     */
    public static String getNextPlanEntryDate(int contractNumber) throws SystemException, ApplicationException {
        String date = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(date);
                
        Date nextDate = getNextPlanEntryDateAsDate(contractNumber);
        
        if(nextDate != null) {
            date = dateFormat.format(nextDate);
        }
        
        return date;
    }
    
    /**
     * Calculates the next PED
     * 
     * @param contractNumber
     * @return
     * @throws ApplicationException
     * @throws SystemException
     */
    public static Date getNextPlanEntryDateAsDate(int contractNumber) throws ApplicationException, SystemException {
        Date dateForPEDCalculation = new Date();
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(dateForPEDCalculation);        
                
        Date ped = ContractServiceDelegate.getInstance()
                .getNextPlanEntryDate(contractNumber, calendar.getTime());

        if(ped != null) {
            GregorianCalendar ref = new GregorianCalendar();
            ref.setTime(ped);
            
            // Test if current day is PED day
            if(ref.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) &&
                    ref.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
              // Calculate for tomorrow, because the Auto Enrollment Periodic Process 
              // runs 1 day before PED
              calendar.add(Calendar.DAY_OF_YEAR, 1);
              ped = ContractServiceDelegate.getInstance()
                  .getNextPlanEntryDate(contractNumber, calendar.getTime());
            }
        }
        
        return ped;
    }
    
    /**
     * Calculates the opt out deadline based on the current date 
     * 
     * @param contractNumber
     * @return
     * @throws ApplicationException
     * @throws SystemException
     */
    public static Date getOptOutDeadlineAsDate(int contractNumber) throws ApplicationException, SystemException {
        Date nextPED = getNextPlanEntryDateAsDate(contractNumber);
        ContractServiceFeature contractServiceFeature = ContractServiceDelegate.getInstance().getContractServiceFeature(contractNumber,ServiceFeatureConstants.MANAGING_DEFERRALS);        
        String optOut = contractServiceFeature.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE);
        int optOutNumber = 0;
        
        if(optOut != null) {            
            optOutNumber = Integer.parseInt(optOut);            
        }

        if(nextPED != null) {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(nextPED);
            
            calendar.add(Calendar.DAY_OF_YEAR, -optOutNumber);
            
            return calendar.getTime();
        } else {
            return null;
        }
    }
    
    /**
     * 
     * @param contractNumber
     * @param nextPED
     * @return
     * @throws ApplicationException
     * @throws SystemException
     */
    public static Date getOptOutDeadline(int contractNumber, Date nextPED) throws ApplicationException, SystemException {
        ContractServiceFeature contractServiceFeature = ContractServiceDelegate.getInstance().getContractServiceFeature(contractNumber,ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);        
        String optOut = contractServiceFeature.getAttributeValue(ServiceFeatureConstants.AUTO_ENROLLMENT_OPT_OUT_DEADLINE);
        int optOutNumber = 0;
        
        if(optOut != null) {            
            optOutNumber = Integer.parseInt(optOut);            
        }

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(nextPED);
        
        calendar.roll(Calendar.DAY_OF_YEAR, -optOutNumber);
        
        return calendar.getTime();
    }
    
    public static StatisticsSummary getStatisticsSummary(int contractId) throws SystemException {

        StatisticsSummary summary = EmployeeEnrollmentSummaryDAO.getStatisticsSummary(contractId);
        
        return summary;
    }
    
    /**
     * Find out if Auto Enrollment is enabled for a specified Contract
     * 
     * @param contractNumber
     * @return true/false
     * @throws ApplicationException
     * @throws SystemException
     */
    public static boolean isAutoEnrollmentEnabled(int contractNumber) throws SystemException {
        boolean isEnabled = false;
        ContractServiceFeature csf = null;
        
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        try {
            csf = service.getContractServiceFeature(contractNumber, ServiceFeatureConstants.AUTO_ENROLLMENT_FEATURE);
        } catch (ApplicationException e) {
            String msg = "Failed to get Contract Service Feature for contractID = " + contractNumber;
            log.error(msg, e);
            throw new RuntimeException(msg,e);
        }
        if (csf != null) {
            isEnabled = ContractServiceFeature.internalToBoolean(csf.getValue()).booleanValue();
        }
        return isEnabled;
    }
    
    /**
     * Find out if Vesting is enabled for a specified Contract
     * 
     * The Vesting link will only be displayed when one of the following 
     * Vesting Contract Service Features is selected for the contract:
     * [a] Vesting Calculated by John Hancock
     * [b] Vesting Provided by TPA

     * @param contractNumber
     * @return true/false
     * @throws SystemException
     */
    public static boolean isVestingEnabled(int contractNumber) throws SystemException {
        boolean isEnabled = false;
        ContractServiceFeature csf = null;
        
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        try {
            csf = service.getContractServiceFeature(contractNumber, ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
        } catch (ApplicationException e) {
            String msg = "Failed to get Contract Service Feature for contractID = " + contractNumber;
            log.error(msg, e);
            throw new RuntimeException(msg,e);
        } 
        
        if (csf != null) {
            isEnabled = ServiceFeatureConstants.PROVIDED.equals(csf.getValue()) || ServiceFeatureConstants.CALCULATED.equals(csf.getValue());
        }
        return isEnabled;
    }
    
    /**
     * Find out the Vesting CSF for a specified Contract
     * 
     * @param contractNumber
     * @return String
     * @throws SystemException
     */
    public static String getVestingCSF(int contractNumber) throws SystemException {
        String vestingCSF = null;
        ContractServiceFeature csf = null;
        
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        try {
            csf = service.getContractServiceFeature(contractNumber, ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
        } catch (ApplicationException e) {
            String msg = "Failed to get Contract Service Feature for contractID = " + contractNumber;
            log.error(msg, e);
            throw new RuntimeException(msg,e);
        } 
        
        if (csf != null) {
            vestingCSF = csf.getValue();
        }
        
        return vestingCSF;
    }
    
    /**
     * Find out if Vesting Calculated by John Hancock
     * 
     * @param contractNumber
     * @return true/false
     */
    public static boolean isVestingCalculated(int contractNumber) throws SystemException {
        boolean isCalculated = false;
        ContractServiceFeature csf = null;
        
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        try {
            csf = service.getContractServiceFeature(contractNumber, ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
        } catch (ApplicationException e) {
            String msg = "Failed to get Contract Service Feature for contractID = " + contractNumber;
            log.error(msg, e);
            throw new RuntimeException(msg,e);
        } 
        
        if (csf != null) {
            isCalculated = ServiceFeatureConstants.CALCULATED.equals(csf.getValue());
        }
        return isCalculated;
    }
    
    /**
     * Find out if Vesting Provided by TPA 
     * 
     * @param contractNumber
     * @return true/false
     */
    public static boolean isVestingProvidedByTPA(int contractNumber) throws SystemException {
        boolean isProvided = false;
        ContractServiceFeature csf = null;
        
        ContractServiceDelegate service = ContractServiceDelegate.getInstance();
        try {
            csf = service.getContractServiceFeature(contractNumber, ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
        } catch (ApplicationException e) {
            String msg = "Failed to get Contract Service Feature for contractID = " + contractNumber;
            log.error(msg, e);
            throw new RuntimeException(msg,e);
        } 
        
        if (csf != null) {
            isProvided = ServiceFeatureConstants.PROVIDED.equals(csf.getValue());
        }
        return isProvided;
    }
    
    /**
     * Web utility returns HTML code for an error or warning icon 
     * 
     * @param item
     * @return
     */
    public static String getEligibilityIndicatorError(EmployeeSummaryDetails item) {
        StringBuffer addressHTML = new StringBuffer();
        
        String errorIndicator = getErrorIndicator(item.getEligibilityIndicatorStatus());
        if ( errorIndicator != null ) {
            addressHTML.append(errorIndicator);
        }
        
        return addressHTML.toString();
    }
    
    /**
     * Web utility returns HTML code for an error or warning icon 
     * 
     * @param item
     * @return
     */
    public static String getEligibilityDateError(EmployeeSummaryDetails item) {
        StringBuffer addressHTML = new StringBuffer();
        
        String errorIndicator = getErrorIndicator(item.getEligibilityDateStatus());
        if ( errorIndicator != null ) {
            addressHTML.append(errorIndicator);
        }
        
        return addressHTML.toString();
    }
    
    /**
     * Web utility returns HTML code for an error or warning icon 
     * 
     * @param item
     * @return
     */
    public static String getOptOutError(EmployeeSummaryDetails item) {
        StringBuffer addressHTML = new StringBuffer();
        
        String errorIndicator = getErrorIndicator(item.getOptOutStatus());
        if ( errorIndicator != null ) {
            addressHTML.append(errorIndicator);
        }
        
        return addressHTML.toString();
    }
    
    /**
     * Web utility returns HTML code for an error or warning icon 
     * 
     * @param item
     * @return
     */
    public static String getDeferralError(EmployeeSummaryDetails item) {
        StringBuffer addressHTML = new StringBuffer();
        
        String errorIndicator = getErrorIndicator(item.getDeferralStatus());
        if ( errorIndicator != null ) {
            addressHTML.append(errorIndicator);
        }
        
        return addressHTML.toString();
    }

    @SuppressWarnings("unchecked")
	public static boolean isMaskSensitiveInformation(UserProfile userProfile, UserInfo userInfo,
			boolean maskSensitiveInfoInd) {

		if (userProfile.getRole().hasPermission(PermissionType.VIEW_SALARY)) {

			/* Internal user with view salary permission can view everything */
			if (userProfile.getRole() instanceof InternalUser) {
				return false;
			}
			
			/* TPA firm */
			if (userProfile.getRole() instanceof ThirdPartyAdministrator) {
                if (userInfo == null) {
                    userInfo = SecurityServiceDelegate.getInstance()
						.getUserInfo(userProfile.getPrincipal());
                }
				if (userInfo.getTpaFirmsAsCollection() != null) {
					for (Iterator it = userInfo.getTpaFirmsAsCollection().iterator(); it.hasNext();) {
                        TPAFirmInfo tpaFirm = (TPAFirmInfo) it.next();
						ContractPermission contractPermission = tpaFirm
								.getContractPermission();
						if (contractPermission.getContractNumber() == userProfile
								.getCurrentContract().getContractNumber()) {
							/*
							 * find the matching contract first and check if we
							 * have the view salary permission.
							 */
							if (contractPermission.isViewSalary()) {
								return false;
							}
							break;
						}
					}
				}
			}
            
            /* External user and employee's mask sensitive information is not Y */
            if (userProfile.getRole() instanceof ExternalUser) {
                if (maskSensitiveInfoInd) {
                    /*
                     * if employee data has the mask sensitive information
                     * indicator set, we mask the data if it's user is
                     * external.
                     */
                    return true;
                } else {
                    // don't need to mask it.
                    return false;
                }
            }           
		}
		
		return true;
	}
    
    /**
     * Identifies the required HTML code  
     *  
     * @param errorStatus
     * @return
     */
    private static String getErrorIndicator(int errorStatus) {
        if ( errorStatus == EmployeeSummaryDetails.WARNING ) {
            return com.manulife.pension.ps.web.Constants.WARNING_ICON_NO_TITLE;
        } else if ( errorStatus == EmployeeSummaryDetails.ERROR ) {
            return com.manulife.pension.ps.web.Constants.ERROR_ICON_NO_TITLE;
        } else {
            return "";
        }
    }
    
    /**
     * Web utility returns if opt-out checkbox disabled
     * 
     * @param item
     * @return
     */
    public static String getOptOutDisabled(EmployeeSummaryDetails item) {
        if (item != null && 
           (/* !item.isEligible() || !item.isEmploymentStatusActiveOrBlank() || */ !item.isOptOutEditable())) {
            return "disabled";
        }
        
        return "";
    }
    
    /**
     * Web utility returns if opt-out checkbox checked
     * 
     * @param item
     * @return
     */
    public static String getOptOutChecked(EmployeeSummaryDetails item) {
        if (item != null && "Y".equalsIgnoreCase(item.getAutoEnrollOptOutInd()) ) {
            return "checked";
        }
        
        return "";
    }
    
    /**
     * Utility method that prepares strings to be displayed 
     * 
     * @param field
     * @return
     */
    public static String processString(String field) {
        if(field == null || "mm/dd/yyyy".equalsIgnoreCase(field)) {
            return "";
        } else {
            return field.trim();
        }
    }
}
