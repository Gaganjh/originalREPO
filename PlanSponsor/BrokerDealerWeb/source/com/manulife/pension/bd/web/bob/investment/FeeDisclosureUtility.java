package com.manulife.pension.bd.web.bob.investment;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.documents.model.FeeDisclosureDocumentInfo;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.bd.web.util.Environment;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.order.delegate.OrderServiceDelegate;
/**
 * This is util class which provides information to check weather links will be available in page  or not
 * 
 * @author siby
 * 
 */
public class FeeDisclosureUtility {

	
	private static final String FEE_INVESTMENT_PAGE_DATE_LIMIT = "FEE_INVESTMENT_PAGE_DATE_LIMIT";
	
	public static boolean isFeeDisclsoureAvaiable(Contract contract) throws SystemException{

		boolean is408AccessAllowed = false;
		try {
			is408AccessAllowed = !contract.isMta() && !isGovernmentPlan(contract.getContractNumber());
		} catch (ContractDoesNotExistException e) {
			is408AccessAllowed = false;
		}
		
		Date invesmentFeePageDateLowerLimit = getInvestmentPageLowerLimitDate();
		List<Date> reportDates = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
				.getDatesForInvestmentCostPage(contract.getContractNumber(), invesmentFeePageDateLowerLimit);
		
		boolean hasStableValueFund = false;
		boolean isR1andRPFundAvaliableForSelectedContact = false;
		if (reportDates != null && reportDates.size() > 0) {
			Date asOfDate = reportDates.get(0);
			hasStableValueFund = checkIfStableValueFundAvailable(contract.getContractNumber(), asOfDate);			
			isR1andRPFundAvaliableForSelectedContact = checkR1andRPFundAvaliableForSelectedContact(contract.getContractNumber(), asOfDate);
		}

		boolean disclosurePdfAvaiable = checkIfInforceFeeDisclosurePdfAvailable(contract.getContractNumber());

		boolean isInvestmentCostPageAvaiable = checkIfInvestmentCostPageAvaiable(contract.getContractNumber());
		return is408AccessAllowed && (hasStableValueFund || disclosurePdfAvaiable || isInvestmentCostPageAvaiable || isR1andRPFundAvaliableForSelectedContact);

	}
	
	
	/*
	 * This method used to check  weather Inforce fee disclosure pdf available
     * @param contractNumber
     * @throws SystemException
     */
	public static boolean checkIfInforceFeeDisclosurePdfAvailable(int contractNumber) throws SystemException {
        FeeDisclosureDocumentInfo documentInfo = OrderServiceDelegate.getInstance().getInforceFeeDisclosurePdfDetails(contractNumber, 0);
       return documentInfo != null;
    }
	
	
	/*
	 * This method used to check  if contract is government plan
     * @param contractNumber
     * @throws SystemException
     */
	public static boolean isGovernmentPlan(int contractNumber) throws SystemException, ContractDoesNotExistException {
	  boolean isGovernmentPlan = ContractServiceDelegate.getInstance().isGovernmentPlan(contractNumber);
      return isGovernmentPlan;
    }
	
	/**
	 * This method used to check  weather Stable value fund available for a given contract
	 * with a particular asOfDate.
	 * 
     * @param contractNumber
	 * @param asOfDate
	 * @return true if the contract has stable value fund for a given asOfDate.
	 * @throws SystemException
	 */
	public static boolean checkIfStableValueFundAvailable(int contractNumber, Date asOfDate)
			throws SystemException {
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		boolean hasStableValueFund = contractServiceDelegate.hasStableValueFund(contractNumber, asOfDate);
		return hasStableValueFund;
	}

	/**
	 * Returns RPand R1 fund count associated with this contract.
	 *
	 * @param contractNumber
	 * @param asOfDate
	 * @return true if the contract has stable value fund for given asOfDate.
	 * @throws SystemException
	 */
	public static boolean checkR1andRPFundAvaliableForSelectedContact(int contractNumber, Date asOfDate)
			throws SystemException {
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		return contractServiceDelegate.checkR1andRPFundAvaliableForSelectedContact(contractNumber, asOfDate);
	}
	
	
	/*
	 * This method used to check  weather Investment cost page Available
     * @param contractNumber
     * @throws SystemException
     */
	public static boolean checkIfInvestmentCostPageAvaiable(int contractNumber)
			throws SystemException {
		boolean isInvestmentCostPageAvaiable = false;
		Date invesmentFeePageDateLowerLimit = getInvestmentPageLowerLimitDate();
		FeeServiceDelegate feeService = FeeServiceDelegate.getInstance(Environment.getInstance().getAppId());
		if (feeService.isInvestmentRelatedCostsPageAvailable(contractNumber, invesmentFeePageDateLowerLimit)) {
			isInvestmentCostPageAvaiable = true;
		}
		return isInvestmentCostPageAvaiable;
	}
		
	  /*
	 * This method used to get the investment page lower date range of effective date 
	 * @return date - lower date range 
	 * @throws SystemException
	 */
	public static Date getInvestmentPageLowerLimitDate() throws SystemException {
		Map<String, String> businessParams = EnvironmentServiceDelegate.getInstance(Environment.getInstance().getAppId()).getBusinessParamMap();
		String value = businessParams.get(FEE_INVESTMENT_PAGE_DATE_LIMIT);
		Date lowerLimitDate = null;
		if(StringUtils.isNotEmpty(value)) {
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				lowerLimitDate = formatter.parse(value);
			} catch (ParseException e) {
			    throw new SystemException("Exception the business param FEE_INVESTMENT_PAGE_DATE_LIMIT " +
			    		"is not valid " + e.getMessage());
			}
		}
		return lowerLimitDate;
	}
	
	/*
	 *  This method used to check  weather contract is pin point contract
     * @param contractNumber
     * @throws SystemException
     */
	public static boolean isPinpoinContract(int contractNumber)
			throws SystemException {
		return FeeServiceDelegate.getInstance(Environment.getInstance().getAppId())
		.isPinpoinContract(contractNumber);
	}
	
	/**
	 *@author shakhas
	 * @param contractNumber
	 * @param asOfDate
	 * @return true if the contract has stable value fund for given asOfDate.
	 * @throws SystemException
	 */
	public static boolean checkLTFundAvaliableForSelectedContact(int contractNumber, Date asOfDate)
			throws SystemException {
		ContractServiceDelegate contractServiceDelegate = ContractServiceDelegate.getInstance();
		return contractServiceDelegate.checkLTFundAvaliableForSelectedContact(contractNumber, asOfDate);
	}
}
