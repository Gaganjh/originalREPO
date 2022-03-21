package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.valueobject.ContractInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * Tests the suppresion rules around the create withdrawal request link.
 * 
 * @author dickand
 */
public class TestContractInfoShowCreateWithdrawalRequestLink {

    /**
     * Returns a generic contract info object populated such that it will display the create
     * withdrawal request link.
     * 
     * @return ContractInfo - A contract info object that will display the create
     *         withdrawal request link.
     */
    private ContractInfo getBaseContractInfo() {

        final ContractInfo contractInfo = new ContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(true);
        contractInfo.setHasInitiatePermission(true);
        contractInfo.setDefinedBenefits(false);
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_ACTIVE);
        return contractInfo;
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithInitiatePermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasInitiatePermission(true);

        assertTrue("Create withdrawal request link should be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithoutInitiatePermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasInitiatePermission(false);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithUnknownInitiatePermission() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setHasInitiatePermission(null);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithCsfAllowOnlineWithdrawals() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(true);

        assertTrue("Create withdrawal request link should be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithoutCsfAllowOnlineWithdrawals() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(false);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithUnknownCsfAllowOnlineWithdrawals() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setCsfAllowOnlineWithdrawals(false);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithActiveContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_ACTIVE);

        assertTrue("Create withdrawal request link should be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithFrozenContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_FROZEN);

        assertTrue("Create withdrawal request link should be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithDiscontinuedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_DISCONTINUED);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithProposalSignedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_PROPOSAL_SIGNED);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithDetailsCompletedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_DETAILS_COMPLETED);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithPendingContractApprovalContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo
                .setStatus(ContractInfo.CONTRACT_STATUS_PENDING_CONTRACT_APPROVAL);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithApprovedContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_CONTRACT_APPROVED);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithOtherContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(ContractInfo.CONTRACT_STATUS_OTHER);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithNullContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(null);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithBlankContractStatus() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setStatus(StringUtils.EMPTY);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithDefinedBenefits() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setDefinedBenefits(true);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithoutDefinedBenefits() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setDefinedBenefits(false);

        assertTrue("Create withdrawal request link should be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }

    /**
     * Tests the suppression rules around the create withdrawal request link.
     */
    @Test
    public void testWithUnknownDefinedBenefits() {

        final ContractInfo contractInfo = getBaseContractInfo();
        contractInfo.setDefinedBenefits(null);

        assertFalse("Create withdrawal request link should not be displayed.", contractInfo
                .getShowCreateWithdrawalRequestLink());
    }
}