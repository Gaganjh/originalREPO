package com.manulife.pension.bd.web.controller;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.bob.BobContext;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.Contract;

/**
 * The Authorization Handler for the contract status and types
 * 
 * @author Siby Thomas
 * 
 */
public class BDContractAuthorizationHandler extends BDAuthorizationHandler {

    private String allowedcontractStatuses;
    private String allowedcontractTypes;
    
    private enum contractStatus {
        DEFINED_BENEFIT("DB"), DEFINED_CONTRIBUTION("DC");
        contractStatus(String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    };

    /**
     * Performs the validations of contract once the user profile validations are over
     * 
     * @see BDAuthorizationHandler#isUserAuthorized(AuthorizationSubject, String)
     */
    public boolean isUserAuthorized(AuthorizationSubject subject, String url)
            throws SystemException {

        boolean isAutorized = false;
        BobContext bobContext = null;
        
        isAutorized = super.isUserAuthorized(subject, url);

        /*
         * if user profile validation fails return from here
         */
        if (!isAutorized) {
            return false;
        }

        bobContext = ((BDAuthorizationSubject) subject).getBobContext();

        /*
         * if bobContext or current contract is null return false
         */
        if (bobContext == null || bobContext.getCurrentContract() == null) {
            return false;
        }
        Contract contract = bobContext.getCurrentContract();

        String typeOfContract;
        if (contract.isDefinedBenefitContract()) {
            typeOfContract = contractStatus.DEFINED_BENEFIT.getValue();
        } else {
            typeOfContract = contractStatus.DEFINED_CONTRIBUTION.getValue();
        }
        
        /**
         * return false if contract type is invalid
         */
        if(!checkIfContractTypeIsAuthorized(typeOfContract, allowedcontractTypes)){
            return false;
        }

        /**
         * return false if contract status is invalid
         */
        if (!checkIfContractStatusIsAuthorized(contract.getStatus(), allowedcontractStatuses)) {
            return false;
        }
      
        return true;
    }

    /**
     * gets the allowedcontractStatuses
     * 
     * @return String
     */
    public String getAllowedcontractStatuses() {
        return allowedcontractStatuses;
    }

    /**
     * sets the allowedcontractStatuses
     * 
     * @param allowedcontractStatuses
     */
    public void setAllowedcontractStatuses(String allowedcontractStatuses) {
        this.allowedcontractStatuses = allowedcontractStatuses;
    }

    /**
     * gets the allowedcontractTypes
     * 
     * @return String
     */
    public String getAllowedcontractTypes() {
        return allowedcontractTypes;
    }

    /**
     * sets the allowedcontractTypes
     * 
     * @param allowedcontractTypes
     */
    public void setAllowedcontractTypes(String allowedcontractTypes) {
        this.allowedcontractTypes = allowedcontractTypes;
    }

    /**
     * returns the comma separated field as an array of values
     * 
     * @param values
     * @return String[]
     */
    private String[] getAsList(String values) {
        String[] list = values.split(",");
        return list;
    }

    /**
     * checks if contract type is valid
     * 
     * @param typeOfContract
     * @param contractTypes
     * @return boolean
     */
    private boolean checkIfContractTypeIsAuthorized(String typeOfContract, String contractTypes) {
        for (String type : getAsList(contractTypes)) {
            if (StringUtils.trim(type).equals(typeOfContract)) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if contract status is valid
     * 
     * @param contractStatus
     * @param allowedContractStatus
     * @return boolean
     */
    private boolean checkIfContractStatusIsAuthorized(String contractStatus,
            String allowedContractStatus) {
        for (String status : getAsList(allowedContractStatus)) {
            if (StringUtils.trim(status).equals(contractStatus)) {
                return true;
            }
        }
        return false;
    }
}
