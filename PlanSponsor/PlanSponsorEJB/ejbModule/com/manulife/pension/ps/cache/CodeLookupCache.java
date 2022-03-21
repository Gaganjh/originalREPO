package com.manulife.pension.ps.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A Singleton class to cache the codes and their descriptions
 * 
 * In order to have your specific codes retrieved you need to: 1. add a String
 * representing the type of the code i.e. PRODUCT_FEATURE_TYPE 2. write setXXX()
 * and getXXX() methods for your type (see ProductFeatures) 3. call the
 * DomainDAO.getLookupCodes(String) with the desired type
 * 
 */

//[TODO] AP 20060927: Shouldn't this file be deprecated and replaced with com.manulife.pension.cache.CodeLookupCache?

public class CodeLookupCache {

    public static final String PRODUCT_FEATURE_TYPE = "PRODUCT_FEATURE_TYPE";

    public static final String COUNTRY_TYPE = "VALIDATION.COUNTRY";

    public static final String USA_STATE_TYPE = "VALIDATION.USA.STATE";

    //[Refactoring] AP 20060927: Moving the following cache keys to com.manulife.pension.cache.CodeLookupCache
    //  - WITHDRAWAL_TYPE
    //  - PAYMENT_TO_TYPE
    //  - LOAN_OPTION_TYPE
    //  - WITHDRAWAL_AMOUNT_TYPE
    //  - TPA_TRANSACTION_FEE_TYPE
    //  - OPTIONS_FOR_UNVESTED_AMOUNTS
    //  - HARDSHIP_REASONS
    //  - REQUEST_TYPES
    //  - WITHDRAWAL_REQUEST_REASONS
    //  - WITHDRAWAL_REQUEST_STATUS
    
    /* private static members */
    /** internal reference to the instance */
    private static CodeLookupCache instance = new CodeLookupCache();

    /** internal map to hold types and their lookup codes */
    private Map cache;

    protected CodeLookupCache() {
        cache = Collections.synchronizedMap(new HashMap());
    }

    /**
     * returns the instance
     * 
     * @return CodeLookupCache
     */
    public static CodeLookupCache getInstance() {
        return instance;
    }

    /**
     * returns the codes and descriptions for Product Features
     * 
     * @return java.util.Map
     */
    public Map getProductFeatures() {
        return (Map) cache.get(PRODUCT_FEATURE_TYPE);
    }

    /**
     * Sets the codes and descriptions for Product Features.
     * 
     * @param java.util.Map
     */
    public void setProductFeatures(Map features) {
        cache.put(PRODUCT_FEATURE_TYPE, features);
    }

    //[Refactoring] AP20061011: Deprecated getStates/getCountries methods. Use the methods from USGPSUtilities\com.manulife.pension.cache.CodeLookupCache instead
    /**
     * returns the codes long names of countries
     * 
     * @return Map
     */
    /*
    public Map getCountries() {
        return (Map) cache.get(COUNTRY_TYPE);
    }
    */
    
    /**
     * sets the codes and long names of countries
     * 
     * @param countries
     */
    /*
    public void setCountries(Map countries) {
        cache.put(COUNTRY_TYPE, countries);
    }
    */

    /**
     * returns the codes and long names of USA states
     * 
     * @return Map
     */
    /*
    public Map getUSAStates() {
        return (Map) cache.get(USA_STATE_TYPE);
    }
    */

    /**
     * sets the codes and long names of USA states
     * 
     * @param usaStates
     */
    /*
    public void setUSAStates(Map usaStates) {
        cache.put(USA_STATE_TYPE, usaStates);
    }
    */

    //[Refactoring] AP 20060927: Moving the following cache methods to com.manulife.pension.cache.CodeLookupCache
    // - getPaymentToTypes
    // - setPaymentToTypes
    // - getLoanOptionTypes
    // - setLoanOptionTypes
    // - getWithdrawalAmountTypes
    // - setWithdrawalAmountTypes
    // - getTPATransactionFeeTypes
    // - setTPATransactionFeeTypes
    // - getOptionsForUnvestedAmounts
    // - setOptionsForUnvestedAmounts
    // - getHardshipReasons
    // - setHardshipReasons
    // - getRequestTypes
    // - setRequestTypes
    // - getRequestReasons
    // - setRequestReasons
    // - getRequestStatus
    // - setRequestStatus
    
    /**
     * resets (empties) the cache
     */
    public void reset() {
        cache.clear();
    }
}
