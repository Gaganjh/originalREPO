package com.manulife.pension.ps.web.withdrawal.util;

import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.dao.CensusVestingDAO;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.InternalServicesCAR;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.security.utility.SecurityHelper;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogEventException;


/**
 * This class contains generic helper methods to complete the Withdrawal Transactions.
 * 
 * @author kuthiha
 * 
 */
public final class WithdrawalWebUtil {
	 private static final String className = WithdrawalWebUtil.class.getName();
	 	public static final String LOAN_ONLY = "loanOnly";
	    public static final String WITHDRAWAL_ONLY = "withdrawalOnly";
	    public static final String LOANANDWITHDRAWAL = "loanAndWithdrawals";

    /**
     * Default Constructor.
     */
    private WithdrawalWebUtil() {
    }
    
    /**
     * Gets the principal from the user profile.
     * 
     * @param userProfile The {@link UserProfile} to use.
     * @return Principal The principal for this profile.
     */
    public static Principal getPrincipalFromUserProfile(final UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        } // fi

        return userProfile.getPrincipal();
    }

    /**
     * Gets the profile ID from the user profile.
     * 
     * @param userProfile The {@link UserProfile} to use.
     * @return Integer - The profile ID.
     */
    public static Integer getProfileIdFromUserProfile(final UserProfile userProfile) {
        if (userProfile == null) {
            return null;
        } // fi

        final Principal principal = userProfile.getPrincipal();
        if (principal == null) {
            return null;
        } // fi

        final Long longProfileId = new Long(principal.getProfileId());

        return new Integer(longProfileId.intValue());
        // return new Integer((int) userProfile.getPrincipal().getProfileId());
    }

    /**
     * This method is used to find the type of request(loan/withdrawal/loanAndWithdrawal)
     * @param contractInfo ContractInfo
     * @param userProfile UserProfile
     * @return String
     * @throws SystemException
     */
    public static String getTypeOfRequest(final UserProfile userProfile)throws SystemException{
    	
    	final Contract currentContract = userProfile.getCurrentContract();
    	String typeOfRequest = "";
    	Map csfMap = null;
    	
    
    	//check whether loans and withdrawals are allowed.
    	 
    	 if (currentContract != null) {
    		try{
    		 csfMap = ContractServiceDelegate.getInstance().
    		getContractServiceFeatures(currentContract.getContractNumber());
    		}
    		catch(ApplicationException applicationException){
    			throw new SystemException(applicationException, className,
    					"loadContractServiceFeatureData", applicationException
                        .getDisplayMessage());
    		}
    		
    		ContractServiceFeature withdrawalsCSF = (ContractServiceFeature) csfMap
            .get(ServiceFeatureConstants.IWITHDRAWALS_FEATURE); 
    		String loansCSF = ""; 
    		LoanSettings loanSettings = LoanServiceDelegate.getInstance().getLoanSettings(currentContract.getContractNumber()) ;
    		if(loanSettings.isAllowOnlineLoans() && loanSettings.isLrk01()){
    			loansCSF = "Y";
    		}    		
    		
    		//check whether loan and withdrawals are allowed
    		if((withdrawalsCSF!= null && "Y".equals(withdrawalsCSF.getValue()))
    				&& ("Y".equals(loansCSF))){	    	
	      		typeOfRequest = LOANANDWITHDRAWAL;
	      	}
	      	//check whether loans are allowed
	      	else if("Y".equals(loansCSF)){
	      		typeOfRequest = LOAN_ONLY;
	      	}            	
	      	//check whether withdrawals are allowed
	      	else if(withdrawalsCSF!= null && "Y".equals(withdrawalsCSF.getValue())){
	      		typeOfRequest = WITHDRAWAL_ONLY;
	      	}
    	}
    	else {//If tpa user      	 
        		typeOfRequest = LOANANDWITHDRAWAL;                	  
         
    	}    	
    	return typeOfRequest;
    	
    }

    /**
     * Determines if the provided contract has at least one non-fully vested money type. If the
     * contract has no money types, this returns false.
     * 
     * @param contractId - The contract ID to lookup the money types for.
     * @return boolean - True if the contract has a money type that's not fully vested, false
     *         otherwise.
     * @throws SystemException If a system exception occurs.
     */
    public static boolean getContractHasNonFullyVestedMoneyTypes(final Integer contractId)
            throws SystemException {
        return !MoneyTypeVO
                .getAreAllMoneyTypesFullyVested(CensusVestingDAO
                        .getContractMoneyTypeVOs(contractId));
    }
    
    
    
    /**
     * 
     * @param dateOfBirth
     * @return
     */
	public static double getAgeCalculate(Date dateOfBirth) {
		double age;
		Calendar birthDay = Calendar.getInstance();
		birthDay.setTimeInMillis(dateOfBirth.getTime());
		// direct age calculation
		LocalDate participantDateOfBirth = LocalDate.of(birthDay.get(Calendar.YEAR), birthDay.get(Calendar.MONTH) + 1,
				birthDay.get(Calendar.DAY_OF_MONTH)); // specify year, month, date directly
		LocalDate currentDate = LocalDate.now(); // gets localDate
		Period diff = Period.between(participantDateOfBirth, currentDate); // difference between the dates is calculated
		age = diff.getYears() + diff.getMonths()/12.0;
		return age;

	}
	/**
	 * Check PPT age is  before
	 * @param withdrawalRequest
	 * @return
	 */
	protected static boolean checkAgePre1936(WithdrawalRequest withdrawalRequest) {
		boolean resultDate=false;
		Date birthdate = withdrawalRequest.getBirthDate();	
	    Date preDate = new Date("1936/01/01");
	    	//check before date with DOB
	    if (birthdate.before(preDate) || birthdate.equals(preDate) ){
	    	return resultDate = true;
	    }else{
	    	return resultDate;
	    }
	 
	 }

	/***
     * 
     * @param withdrawalRequest
     * @param controlBlock
     */
    public static void logSTP(WithdrawalRequest withdrawalRequest, String controlBlock, String className,String methodName) {
    	EventLog eventLog;
    	String transactionId = "0";
		try {
			 eventLog =  EventLogFactory.getInstance().createEventLog(EventLogFactory.DISTRIBUTION_EVENT_LOG);
			 eventLog.setClassName(className);
		     eventLog.setMethodName(methodName);
		     eventLog.setUserName(String.valueOf(withdrawalRequest.getEmployeeProfileId()));
		     eventLog.setPrincipalUserName(withdrawalRequest.getPrincipal().getUserName());
		     eventLog.setPrincipalProfileId(String.valueOf(withdrawalRequest.getPrincipal().getProfileId()));		     
		     eventLog.addLogInfo(EventLog.SUBMISSION_ID, withdrawalRequest.getSubmissionId());
		     eventLog.addLogInfo(EventLog.ACTION, "APOLLO_OW_STP : "+controlBlock);
		     
		     if(StringUtils.isNotEmpty(controlBlock) && controlBlock.contains("STP SUCCESSFUL")) {
		    	 transactionId = controlBlock.substring(controlBlock.indexOf(":") + 1).trim();
		    	 if(StringUtils.isNotEmpty(transactionId) && transactionId.length() > 9)
		    	 {
		    		 transactionId = transactionId.substring(0,10);
		    	 }
		     }
			 eventLog.logTransactionID(Long.parseLong(transactionId));		
		} catch (LogEventException e) {
            SystemException se = new SystemException(e, className, "logSTP",
                    "Problem occurred during Apollo STP should be routed to AWD or not. STP LogEventException details:"
                            + controlBlock);
            throw ExceptionHandlerUtility.wrap(se);
        }  catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
       
    }
    public static java.sql.Date getSqlDate(java.util.Date date){
    	FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
    	final String stringDate= fastDateFormat.format(date);
    	final java.sql.Date sqlDate=  java.sql.Date.valueOf(stringDate);
		return sqlDate;
    	
    }
	
}
