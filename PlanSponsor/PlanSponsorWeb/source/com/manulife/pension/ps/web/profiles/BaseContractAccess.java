package com.manulife.pension.ps.web.profiles;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Super class for Client User & TPA contract access view bean
 * 
 * @author Steven Wang 
 */
public class BaseContractAccess implements Cloneable, Serializable {

    private Integer contractNumber;
    public static final String FIELD_CONTRACT_NUMBER = "contractNumber";
    public static final String FIELD_USER_PERMISSIONS = "userPermissions";

    /**
     * User permission related current contract
     */
    protected UserPermissions userPermissions = new UserPermissions();

    public UserPermissions getUserPermissions() {
        return userPermissions;
    }

    public void setUserPermissions(UserPermissions userPermissions) {
        this.userPermissions = userPermissions;
    }

    /**
     * Constructor.
     */
    public BaseContractAccess() {
        super();
    }

    /**
     * Gets the contractNumber
     * 
     * @return Returns a int
     */
    public Integer getContractNumber() {
        return contractNumber;
    }

    /**
     * Sets the contractNumber
     * 
     * @param contractNumber The contractNumber to set
     */
    public void setContractNumber(Integer contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("contractNumber [").append(contractNumber);
        return sb.toString();
    }

    public Map getFormAsMap(String fieldPrefix) {
        Map formMap = new HashMap();

        String fieldId = fieldPrefix + FIELD_CONTRACT_NUMBER;
        formMap.put(fieldId, getContractNumber());

        return formMap;
    }

}
