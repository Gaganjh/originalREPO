package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Tests the is post draft query.
 * 
 * @author dickand
 */
public class TestWithdrawalRequestIsPostDraft {

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testNullStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(null);
        assertFalse("Status is null.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testBlankStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(StringUtils.EMPTY);
        assertFalse("Status is blank.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testDraftStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);
        assertFalse("Status is draft.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testPendingReviewStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);
        assertTrue("Status is pending review.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testPendingApprovalStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_APPROVAL_CODE);
        assertTrue("Status is pending approval.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testApprovedStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_APPROVED_CODE);
        assertTrue("Status is approved.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testDeletedStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DELETED_CODE);
        assertFalse("Status is deleted.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testDeniedStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DENIED_CODE);
        assertFalse("Status is denied.", request.getIsPostDraft());
    }

    /**
     * Tests the is post draft query.
     */
    @Test
    public void testExpiredStatusCode() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_EXPIRED_CODE);
        assertFalse("Status is expired.", request.getIsPostDraft());
    }
}
