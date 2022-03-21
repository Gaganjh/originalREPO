/**
 * 
 */
package com.manulife.pension.ps.web.onlineloans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.cache.CodeLookupCache;
import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.PaymentInstruction;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;

/**
 * The utility class encapsulating the Lookups for Online Loans
 * 
 * @author Ted Matyszczuk
 * 
 */
public class LoanLookups {

    /**
     * Loan Type codes and descriptions list
     */
    private Map<String, String> loanTypeList = new HashMap<String, String>();

    /**
     * Loan payment frequency codes and descriptions list
     */
    private Map<String, String> loanPaymentFrequencyList = new HashMap<String, String>();
    
    /**
     * Payment method codes and descriptions list
     */
    private Map<String, String> paymentMethodList = new HashMap<String, String>();
    
    /**
     * Bank account type codes and descriptions list
     */
    private Map<String, String> bankAccountTypeList = new HashMap<String, String>();
    
    /**
     * Online Loans status codes and descriptions
     */
    private Map<String, String> onlineLoansStatusList = new HashMap<String, String> ();

    /**
     * i:loan status codes and descriptions
     */
    private Map<String, String> iLoanStatusList = new HashMap<String, String> ();
    
    /**
     * Address state codes and descriptions list
     */
    private Map<String, String> stateList = new HashMap<String, String>();
    
    /**
     * Country codes and descriptions list
     */
    private Map<String, String> countryList = new HashMap<String, String>();
    
    private EnvironmentServiceDelegate environmentServiceDelegate;

    public static final Logger logger = Logger.getLogger(LoanLookups.class);
    
    private static LoanLookups instance = new LoanLookups();


    /**
     * Singleton instance
     * 
     * @return singleton instance
     */
    public static LoanLookups getInstance() {
        return instance;
    }

    /**
     * Construtor.
     */
    private LoanLookups() {
        loadLoanTypes();
        loadLoanPaymentFrequencies();
        loadPaymentMethods();
        loadBankAccountTypes();
        loadOnlineLoansStatusList();
        loadILoanStatusList();
        
        environmentServiceDelegate = EnvironmentServiceDelegate
                .getInstance(Constants.PS_APPLICATION_ID);
        loadStateList();
        loadCountryList();
    }


    /**
     * Load the loan type descriptions.
     */
    private void loadLoanTypes() {
        loanTypeList.put(LoanConstants.TYPE_GENERAL_PURPOSE, 
                LoanConstants.TYPE_GENERAL_PURPOSE_DESC);
        loanTypeList.put(LoanConstants.TYPE_HARDSHIP, 
                LoanConstants.TYPE_HARDSHIP_DESC);
        loanTypeList.put(LoanConstants.TYPE_PRIMARY_RESIDENCE, 
                LoanConstants.TYPE_PRIMARY_RESIDENCE_DESC);
    }
    
    /**
     * Load the loan payment frequency descriptions.
     */
    private void loadLoanPaymentFrequencies() {
        loanPaymentFrequencyList.put(GlobalConstants.FREQUENCY_TYPE_WEEKLY, 
                GlobalConstants.FREQUENCY_TYPE_WEEKLY_DESC);
        loanPaymentFrequencyList.put(GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY, 
                GlobalConstants.FREQUENCY_TYPE_BI_WEEKLY_DESC);
        loanPaymentFrequencyList.put(GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY, 
                GlobalConstants.FREQUENCY_TYPE_SEMI_MONTHLY_DESC);
        loanPaymentFrequencyList.put(GlobalConstants.FREQUENCY_TYPE_MONTHLY, 
                GlobalConstants.FREQUENCY_TYPE_MONTHLY_DESC);
    }

    /**
     * Load the payment method descriptions.
     */
    private void loadPaymentMethods() {
        paymentMethodList.put(Payee.WIRE_PAYMENT_METHOD_CODE, 
                Payee.WIRE_PAYMENT_METHOD_CODE_DESC);
        paymentMethodList.put(Payee.ACH_PAYMENT_METHOD_CODE, 
                Payee.ACH_PAYMENT_METHOD_CODE_DESC);
        paymentMethodList.put(Payee.CHECK_PAYMENT_METHOD_CODE, 
                Payee.CHECK_PAYMENT_METHOD_CODE_DESC);
    }

    /**
     * Load the bank account type descriptions.
     */
    private void loadBankAccountTypes() {
        bankAccountTypeList.put(PaymentInstruction.BANK_ACCOUNT_TYPE_CHECKING, 
                PaymentInstruction.BANK_ACCOUNT_TYPE_CHECKING_DESC);
        bankAccountTypeList.put(PaymentInstruction.BANK_ACCOUNT_TYPE_SAVING, 
                PaymentInstruction.BANK_ACCOUNT_TYPE_SAVING_DESC);
    }

    /**
     * Load the Online Loans status codes and descriptions.
     */
    private void loadOnlineLoansStatusList() {
        Collection<String> lookupKeys = new ArrayList<String>();
        lookupKeys.add(CodeLookupCache.LOAN_REQUEST_STATUS);
        try {
            final Map lookupData = WithdrawalServiceDelegate.getInstance()
                .getLookupData(null, StringUtils.EMPTY, lookupKeys);
            Collection<DeCodeVO> loanStatusCodes = (Collection) lookupData.get(CodeLookupCache.LOAN_REQUEST_STATUS);
            for (DeCodeVO deCodeVO : loanStatusCodes ) {
                onlineLoansStatusList.put(deCodeVO.getCode(), deCodeVO.getDescription());
            }
        } catch (SystemException systemException) {
            // Just log the exception and keep going, so onlineLoansStatusList
            // will be empty.  Should never happen and is not critical.
            logger
                .error(
                        "LoanLookups.loadOnlineLoansStatusList could not "
                        + "retrieve Loan Request status values",
                            systemException);
        }
    }

    /**
     * Load the i:loan status codes and descriptions.
     */
    private void loadILoanStatusList() {
    	iLoanStatusList.put(Constants.ILOANS_STATUS_CODE_PENDING, 
                Constants.ILOANS_STATUS_TEXT_PENDING);
    	iLoanStatusList.put(Constants.ILOANS_STATUS_CODE_REVIEW, 
                Constants.ILOANS_STATUS_TEXT_REVIEW);
    	iLoanStatusList.put(Constants.ILOANS_STATUS_CODE_APPROVED, 
                Constants.ILOANS_STATUS_TEXT_APPROVED);
    }

    /**
     * Load the state codes and their descriptions.
     */
    private void loadStateList() {
        try {
            stateList = environmentServiceDelegate.getUSAStates();
        } catch(SystemException e) {
            throw new RuntimeException("SystemException thrown by EnvironmentServiceDelegate.getUSAStates()", e);
        }
    }

    /**
     * Load the country codes and their descriptions.
     */
    private void loadCountryList() {
        try {
            countryList = environmentServiceDelegate.getCountries();
        } catch(SystemException e) {
            throw new RuntimeException("SystemException thrown by EnvironmentServiceDelegate.getCountries()", e);
        }
    }


    /**
     * Returns a list of loan type codes ant their descriptions.
     * 
     * @return Map<String - loan type code , String - loan type description>
     */
    public Map<String, String> getLoanTypesList() {
        return loanTypeList;
    }

    /**
     * Returns a list of loan payment frequency codes and their descriptions.
     * 
     * @return Map<String - loan payment frequency code , String - loan payment
     *             frequency description>
     */
    public Map<String, String> getLoanPaymentFrequencyList() {
        return loanPaymentFrequencyList;
    }

    /**
     * Returns a list of payment method codes and their descriptions.
     * 
     * @return Map<String - payment method code , String - payment 
     *             method description>
     */
    public Map<String, String> getPaymentMethodList() {
        return paymentMethodList;
    }

    /**
     * Returns a list of bank account type codes and their descriptions.
     * 
     * @return Map<String - bank account type code , String - bank account 
     *             type description>
     */
    public Map<String, String> getBankAccountTypeList() {
        return bankAccountTypeList;
    }

    /**
     * Returns the map of Online Loans status codes & descriptions
     * 
     * @return Map<String - Status code, String - description>
     */
    public Map<String, String> getOnlineLoansStatusList() {
        return onlineLoansStatusList;
    }

    /**
     * Returns the map of old i:loan status code & description
     * 
     * @return Map<String - Status code, String - description>
     */
    public Map<String, String> getILoanStatusList() {
        return iLoanStatusList;
    }
    
    /**
     * return the status description given the status code for i:loan requests
     * 
     * @param String - Code
     * @return String - Description
     */
    public String getStatusDescription(String statusCode) {
    	if (iLoanStatusList == null) {
    		loadILoanStatusList();
    	}
    	return iLoanStatusList.get(statusCode);
    }
    
    /**
     * Returns a list of address state codes and their descriptions.
     * 
     * @return Map<String - state code , String - state description/name>
     */
    public Map<String, String> getStateList() {
        return stateList;
    }

    /**
     * Returns a list of country codes and their descriptions.
     * 
     * @return Map<String - country code , String - country description/name>
     */
    public Map<String, String> getCountryList() {
        return countryList;
    }

}
