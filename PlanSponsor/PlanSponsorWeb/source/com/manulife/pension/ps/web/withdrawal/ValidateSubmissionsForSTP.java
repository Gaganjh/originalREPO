package com.manulife.pension.ps.web.withdrawal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.SynchronizationServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalIrsDistributionCodesUtil;
import com.manulife.pension.ps.web.withdrawal.util.WithdrawalWebUtil;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantFlag;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;


public class ValidateSubmissionsForSTP  {
	private static final String CLASS_NAME = ValidateSubmissionsForSTP.class.getName();
	private static final Logger logger = Logger.getLogger(CLASS_NAME);
	public static final String DISPLAY_EMP_PLAN = "Employer Sponsored Qualified Plan";
	public  StringBuffer validationResult= new StringBuffer();
	
	public StringBuffer getValidationResult() {
		return validationResult;
	}

	
	public boolean isValidForSTP(WithdrawalRequest withdrawalRequest) throws SystemException {		
		boolean returnValue=false;
  		if (checkWithdrawalReason(withdrawalRequest) && !isParticipantFlag(withdrawalRequest) && !checkProviderName(withdrawalRequest) 
  				&& !isPre1936(withdrawalRequest) && !isVestingPercentageLessThanHundred(withdrawalRequest)
  				&& !isATotalCarePlan(withdrawalRequest) && !unvestedMoney(withdrawalRequest) && !checkComment(withdrawalRequest) && !isResidencePR(withdrawalRequest)  && !hardshipwithRothandAftertaxMoneyType(withdrawalRequest)
  				&& !checkApolloBacthRunning()) {			
  			returnValue = true;	
			}
  		WithdrawalWebUtil.logSTP(withdrawalRequest, validationResult.toString(), this.getClass().getName(), "isValidForSTP");
		return returnValue;
	}     
		
	/***
	 * 
	 * @param withdrawalRequest
	 * @return
	 */
	protected boolean isPre1936(WithdrawalRequest withdrawalRequest) {
		boolean returnValue=false;
		Date birthdate = withdrawalRequest.getBirthDate();	
	    Date preDate = new Date("1936/01/01");
	    	//check before date with DOB
	    boolean resultDate = birthdate.before(preDate);  
		if( resultDate && withdrawalRequest.getPaymentTo().equals("PA") &&
		    (("7A ").equals(new WithdrawalIrsDistributionCodesUtil().getIrsCode(withdrawalRequest)))) {
			returnValue= true; 
		   }	
		getValidationResult().append(", isPre1936= ").append(returnValue);
		return returnValue; 
	 }
	
	protected boolean checkWithdrawalReason(WithdrawalRequest withdrawalRequest) {
		boolean returnValue = getWithdrawalTypes().contains(withdrawalRequest.getReasonCode());
		getValidationResult().append(" checkWithdrawalReason= ").append(returnValue);
		return returnValue;
	}
	
	/***
	 * 
	 * @param withdrawalRequest
	 * @return
	 */
	protected boolean isResidencePR(WithdrawalRequest withdrawalRequest) {
		boolean returnValue = false;
		if(withdrawalRequest.getParticipantStateOfResidence().equals("PR")) {
			returnValue= true;
		}
		getValidationResult().append(", isResidencePR= ").append(returnValue);
		return returnValue;
	}
	
	/***
	 * ULTRAS-561
	 * If Participant's flag is set to 'Y' at the CAR home page, request needs to be routed to AWD after the approval process.
	 * @param withdrawalRequest
	 * @return
	 */
	public boolean isParticipantFlag(WithdrawalRequest withdrawalRequest) {
		boolean returnValue = false;
		try {
			ParticipantFlag participantFlag = WithdrawalServiceDelegate.getInstance().getPartitcipantExceptionFlagDetials(String.valueOf(withdrawalRequest.getEmployeeProfileId()), 
					String.valueOf(withdrawalRequest.getContractId()));
			if(participantFlag != null && "Y".equals(participantFlag.getExceptionFlag()) ) {
				returnValue= true;
       	}
		} catch (SystemException e) {
			final String message = "@@@@@ SystemException from isParticipantFlag() in thread "
					+ e.getMessage();
			logger.error(message);
		}
		getValidationResult().append(", isParticipantFlag= ").append(returnValue);
		return returnValue;
		
	}
	
	/***
	 * ULTRAS-773
	 * If 'Retirement' withdrawal and vesting in any money type is NOT 100%, vested route to AWD
	 * @param withdrawalRequest
	 * @return
	 */
	public boolean isVestingPercentageLessThanHundred(WithdrawalRequest withdrawalRequest) {
	  boolean returnValue = false;
	  moneyTypeLoop: for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
            .getMoneyTypes()) {

          if (("RE".equals(withdrawalRequest.getReasonCode()) || WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE.equals(withdrawalRequest.getReasonCode())) && 
    		  withdrawalRequestMoneyType.getVestingPercentage().compareTo(new BigDecimal("100")) != 0) {
        	  returnValue= true;
    	  }
	  }
	  getValidationResult().append(", isVestingPercentageLessThanHundred= ").append(returnValue);
	  return returnValue;
	}
	
	/****
	 * ULTRAS-563 : Participants born Pre 1936 and IRS distribution code 7A need to go to AWD
	 * ULTRAS-561 : If Participant's flag is set to 'Y' at the CAR home page, request needs to be routed to AWD after the approval process.
	 * ULTRAS-773 : If 'Retirement' withdrawal and vesting in any money type is NOT 100%, vested route to AWD
	 * @param withdrawalRequest
	 * @return
	 * @throws SystemException 
	 */
	public boolean isRouteToAWD(WithdrawalRequest withdrawalRequest) throws SystemException {
		 boolean returnValue = false;
		if(checkWithdrawalReason(withdrawalRequest) &&
				(isPre1936(withdrawalRequest) || checkProviderName(withdrawalRequest) || isParticipantFlag(withdrawalRequest)  ||checkApolloBacthRunning() 
						|| isVestingPercentageLessThanHundred(withdrawalRequest) || isATotalCarePlan(withdrawalRequest) ||
						unvestedMoney(withdrawalRequest) || checkComment(withdrawalRequest) || isResidencePR(withdrawalRequest) || hardshipwithRothandAftertaxMoneyType(withdrawalRequest))) {
			returnValue= true;
		}		
		getValidationResult().append(" isRouteToAWD= ").append(returnValue);
		WithdrawalWebUtil.logSTP(withdrawalRequest, validationResult.toString(), this.getClass().getName(), "isRouteToAWD");
		return returnValue;
	
	}
	
	/****
	 * ULTRAS-565
	 * @param withdrawalRequest
	 * @return
	 */
	public boolean isATotalCarePlan(WithdrawalRequest withdrawalRequest) {
		boolean returnValue = false;
        ContractInfo contractInfo = withdrawalRequest.getContractInfo();
        boolean bga = contractInfo.isBundledGaIndicator();
        if (bga  == true) {
        	returnValue= true;
        }
        getValidationResult().append(", isATotalCarePlan= ").append(returnValue);
        return returnValue;
    }
	/**
	 * ULTRAS-771 Employer unvested money - Route to AWD/STP based on USER SELECTION
	 * @param wdRequest
	 * @return
	 */
	public boolean unvestedMoney(WithdrawalRequest wdRequest){
		boolean returnValue = false;
    	moneyTypeLoop: for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : wdRequest
                .getMoneyTypes()) {
    		
    		if (withdrawalRequestMoneyType.getVestingPercentage().compareTo(new BigDecimal("100")) != 0 && wdRequest.getUnvestedAmountOptionCode() !=null && 
    		   (wdRequest.getUnvestedAmountOptionCode().equals(CommonConstants.LEAVE_IN_PARTICIPANT_ACCOUNT_MOVE_TO_DIO) ||  wdRequest.getUnvestedAmountOptionCode().equals(CommonConstants.USE_TO_PAY_JOHN_HANCOCK_CONTRACT_CHARGES)) )
    			    {
    			returnValue= true ;
    			    }
    		}
		 getValidationResult().append(", unvestedMoney= ").append(returnValue);
    		return returnValue;
    		
    	}
	/**
	 * ULTRAS -791 OW - STP Rules - 'Trustee of plan' field, 'Account number for rollover' - Payee 'Rollover all to Other plan' via CHECK
	 * CHECK COMMENT more than 40 chare route to AWD else Route to STP
	 */
	public boolean checkComment(WithdrawalRequest wdRequest){
		boolean returnValue = false;
		boolean checkCommentFlag = false;
		String checkComment = null;
    	StringBuilder sb = new StringBuilder("FBO:");
    	String firstName = wdRequest.getFirstName();
    	String lastName = wdRequest.getLastName();
    	String rolloverAccountNumber = null;
    	String payeeInfo = null;
    	for(final Recipient recip: wdRequest.getRecipients()){
    		
    		for (final Payee payee : recip.getPayees()){
    			
    			rolloverAccountNumber = payee.getRolloverAccountNo();
    			payeeInfo = payee.getParticipant();
    			
    	
    	if ((wdRequest.getPaymentTo().equals("RP")|| (payeeInfo != null && payeeInfo.indexOf(DISPLAY_EMP_PLAN) !=-1)) && rolloverAccountNumber != null && payee.getPaymentMethodCode().equals("CH")){
    	sb.append(" A/C ");
    	sb.append(rolloverAccountNumber);
        if (!"".equals(lastName)) {
        	sb.append(" ");
        	sb.append(lastName);
        }
        if (!"".equals(firstName)) {
            if (!"".equals(lastName)) {
            	sb.append(", ");
            }
            if(sb.length() <=39){
            	checkCommentFlag =true;
            sb.append(firstName);
            }
    	}
        if(checkCommentFlag) {
        	if(sb.length() >40) {
        	 checkComment =sb.toString().substring(0, 40);
        	}
        }
        if(sb.length() <40){
        	getValidationResult().append(", checkComment= "+sb.toString() +" checkComment length= "+sb.length()).append(returnValue);
        	returnValue = false;
        }else if (checkCommentFlag && (checkComment !=null & checkComment.length() ==40)){
        	getValidationResult().append(", checkComment= "+checkComment +" checkComment length= "+sb.length()).append(returnValue);
        	returnValue =false;
        }else {
        	getValidationResult().append(", checkComment= "+checkComment +" checkComment length= "+sb.length()).append("true");
        	returnValue = true;
        }
    	}
    		}
        	
    	}
    	getValidationResult().append(", checkComment= "+sb.toString() +" checkComment length= "+sb.length()).append(returnValue);
    	return returnValue;
    }
	/**
	 * ULTRA-562 Batch Window Is Closed - Send to AWD
	 * @return
	 * @throws SystemException
	 */
	public boolean checkApolloBacthRunning() throws SystemException{
		
		boolean batchFlag = SynchronizationServiceDelegate.getInstance("psw").isApolloBatchRunning(); 
		getValidationResult().append(" checkApolloBacthRunning= ").append(batchFlag);
		return batchFlag;
	}
	/**
	 * ULTRAS-812 OW - STP Rules - Foreign address indicator 'Y' - Route to AWD
	 * @param wdReq
	 * @return
	 */
	public boolean checkForeignInd(WithdrawalRequest wdReq){
		boolean foreignIndFlag = false;
		for(Recipient recipient : wdReq.getRecipients()){
			if(recipient.getAddress().getPayeeNo() == null){
				if (!(recipient.getAddress().getCountryCode().equals(Address.USA_COUNTRY_CODE))){
					foreignIndFlag = true;
					getValidationResult().append(" checkForeignInd= ").append(foreignIndFlag);
					return foreignIndFlag;
				}
			}
		}
		
		getValidationResult().append(" checkForeignInd= ").append(foreignIndFlag);
		return foreignIndFlag;
	}
	
	/**
	 * ULTRAS -1994
	 * Multipayee Payement
	 */
	public boolean checkMultipayee (WithdrawalRequest wdReq){
		
		boolean returnValue = false;
		if (wdReq.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION)){
			returnValue= true;
		}
		getValidationResult().append(", checkMultipayee= ").append(returnValue);
		return returnValue ;
	}
	
/***
 * ULTRAS-2063 (defect)
 * For Single Payee If Rollover to Roth IRA is yes then it is not valid for STP, request should be routed to AWD via email
 * @param withdrawalRequest
 * @return
 */
	public boolean isSinglePayeeRolloverToRoth(WithdrawalRequest withdrawalRequest){
		boolean returnValue = false;
		if (withdrawalRequest.getPaymentTo().equals(WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE) && withdrawalRequest.isRolloverTypeEligible()){
		for(final Recipient recip: withdrawalRequest.getRecipients()){
	    	for (final Payee payee : recip.getPayees()){
				if(payee.getTaxes().indexOf("\"Roth_IRA\":\"Y\"")!=-1){
					returnValue = true; // The participant selected Roth IRA radio button on the UI for Single Payee
				}
	    	}
	   	}
	    }
		getValidationResult().append(", isSinglePayeeRolloverToRoth= ").append(returnValue);
		return returnValue;
	}
	/***
	 * ULTRAS-1903
	 *  Participant with after tax and ROTH money then it is not valid for STP, request should be routed to AWD via email
	 * @param withdrawalRequest
	 * @return
	 */
	
	
	public boolean hardshipwithRothandAftertaxMoneyType(WithdrawalRequest withdrawalRequest) {
		boolean returnValue = false;
		if (WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE.equals(withdrawalRequest.getReasonCode())){
			 
			  moneyTypeLoop: for (final WithdrawalRequestMoneyType withdrawalRequestMoneyType : withdrawalRequest
					  .getMoneyTypes()) {
			  

				if( getMoneyType().contains(withdrawalRequestMoneyType.getMoneyTypeId())){
				  
					  returnValue =true;
				  }
			  }
		}			
	       getValidationResult().append("  , hardshipwithRothandAftertaxMoneyType= ").append(returnValue);
	        	return returnValue ;
			  
		 }
	
	/***
	 * ULTRAS-2269
	 * OW-All reason codes except HA and MD- When FI name has JH or Hancock, send to AWD
	 * @param withdrawalRequest
	 * @return
	 */
	
	public boolean checkProviderName(WithdrawalRequest withdrawalRequest) {
		boolean returnValue = false;
		if ((!(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE.equals(withdrawalRequest.getReasonCode())))
				&& (!(WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE
						.equals(withdrawalRequest.getReasonCode())))) {

			if ((WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_IRA_CODE.equals(withdrawalRequest.getPaymentTo()))
					|| (WithdrawalRequest.PAYMENT_TO_ROLLOVER_TO_PLAN_CODE.equals(withdrawalRequest.getPaymentTo()))
					|| (WithdrawalRequest.PAYMENT_TO_MULTIPLE_DESTINATION.equals(withdrawalRequest.getPaymentTo()))) {

				Collection<Recipient> recipients = withdrawalRequest.getRecipients();
				for (Recipient recipient : recipients) {
					Collection<Payee> payees = recipient.getPayees();

					for (Payee payee : payees) {
						if (StringUtils.isNotBlank(payee.getOrganizationName())) {
							if (payee.getOrganizationName().toLowerCase().contains(("jh").toLowerCase())
									|| payee.getOrganizationName().toLowerCase().contains(("hancock").toLowerCase())) {

								returnValue = true;
							}
						}
					}
				}

			}
		}
		getValidationResult().append("  , checkProviderName= ").append(returnValue);
		return returnValue;
	}
	/**
	 * Withdrawal Reason list 
	 * @return
	 */
	
	public List<String> getWithdrawalTypes(){
		
		List<String> withdrawalTypes = new ArrayList <String>();
		
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_PRE_RETIREMENT_WITHDRAWAL_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_MINIMUM_DISTRIBUTION_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_HARDSHIP_WITHDRAWAL_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_EE_ROLLOVER_MONEY_CODE);
		withdrawalTypes.add(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
		
		return withdrawalTypes;
				
	}
	public List<String> getMoneyType(){
	 List<String> MoneyTypes = new ArrayList<String>();
	  MoneyTypes.add("EERRT");
	  MoneyTypes.add("EEROT");
	  MoneyTypes.add("EEMAN");
	  MoneyTypes.add("EEVND");
	  MoneyTypes.add("EEAT1");
	  MoneyTypes.add("EEAT2");
	  return MoneyTypes;
	}

}