package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang.BooleanUtils;
import org.junit.Test;

/**
 * Tests the 7 new instance variable properties added to the 
 * ContractInfo project.
 * 
 * @author kuthiha
 */
public class TestContractInfoSpousalConsent {

    /**
     * Returns a generic contract info object populated such that it will display the
     * withdrawal tools link.
     * 
     * @return ContractInfo - A contract info object that will display the withdrawal
     *         tools link.
     */
    private ContractInfo getBaseContractInfo() {

        final ContractInfo contractInfo = new ContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(true);
        contractInfo.setHasViewAllPermission(true);
        contractInfo.setHasSelectedAccessPermission(false);
        contractInfo.setDefinedBenefits(false);
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_ACTIVE);
        return contractInfo;
    }

    /**
     * Tests the SpousalConsent is true.
     */
    @Test
    public void testSetSpousalConsentTrue() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setSpousalConsentRequired(BooleanUtils.toBooleanObject(true));
        
        assertEquals(BooleanUtils.toBooleanObject(true), contractInfo
                .getSpousalConsentRequired());
    }
    
    @Test
    public void testSetSpousalConsentFalse() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setSpousalConsentRequired(BooleanUtils.toBooleanObject(false));
        
        assertEquals(BooleanUtils.toBooleanObject(false), contractInfo
                .getSpousalConsentRequired());
    }
    
    @Test
    public void testSetSpousalConsentBlank() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setSpousalConsentRequired(BooleanUtils.toBooleanObject("  "));

        assertEquals(BooleanUtils.toBooleanObject("  "), contractInfo
                .getSpousalConsentRequired());
    }
    
    @Test
    public void testSetSpousalConsentNull() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setSpousalConsentRequired(null);
 
        assertEquals(null, contractInfo
                .getSpousalConsentRequired());
    }


}
