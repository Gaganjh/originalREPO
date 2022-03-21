package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;

/**
 * Tests the suppresion rules around the withdrawal tools link.
 * 
 * @author dickand
 */
public class TestContractInfoShowWithdrawalsToolLink {

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
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithViewAllPermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasViewAllPermission(true);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithoutViewAllPermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasViewAllPermission(false);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithUnknownViewAllPermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasViewAllPermission(null);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithSelectedAccessPermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasSelectedAccessPermission(true);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithoutSelectedAccessPermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasSelectedAccessPermission(false);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithUnknownSelectedAccessPermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasSelectedAccessPermission(null);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithCsfAllowOnlineWithdrawals() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(true);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithoutCsfAllowOnlineWithdrawals() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(false);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithUnknownCsfAllowOnlineWithdrawals() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(false);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithActiveContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_ACTIVE);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithFrozenContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_FROZEN);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithDiscontinuedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_DISCONTINUED);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithProposalSignedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_PROPOSAL_SIGNED);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithDetailsCompletedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_DETAILS_COMPLETED);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithPendingContractApprovalContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo
                .setStatus(ContractInfo.CONTRACT_STATUS_PENDING_CONTRACT_APPROVAL);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithApprovedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_CONTRACT_APPROVED);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithOtherContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_OTHER);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithNullContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(null);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithBlankContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(StringUtils.EMPTY);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithDefinedBenefits() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setDefinedBenefits(true);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithoutDefinedBenefits() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setDefinedBenefits(false);

        assertTrue("Withdrawal tools link should be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }

    /**
     * Tests the suppression rules around the withdrawal tools link.
     */
    @Test
    public void testWithUnknownDefinedBenefits() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setDefinedBenefits(null);

        assertFalse("Withdrawal tools link should not be displayed.", contractInfo
                .getShowWithdrawalToolsLink());
    }
}
