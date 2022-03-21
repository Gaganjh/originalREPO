package com.manulife.pension.service.withdrawal.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * Tests the WithdrawalStatePredicate.
 * 
 * @author glennpa
 */
public class WithdrawalStatePredicateTestOld {

    private Collection<WithdrawalStateEnum> onlyApprovalState;

    private Collection<WithdrawalStateEnum> beforeEndStates;

    private Collection<WithdrawalStateEnum> noStates;

    private Collection<WithdrawalStateEnum> nullStates;

    private WithdrawalRequest approvedWithdrawalRequest;

    private WithdrawalRequest deletedWithdrawalRequest;

    private WithdrawalRequest deniedWithdrawalRequest;

    private WithdrawalRequest draftWithdrawalRequest;

    private WithdrawalRequest expiredWithdrawalRequest;

    private WithdrawalRequest pendingApprovalWithdrawalRequest;

    private WithdrawalRequest pendingReviewWithdrawalRequest;

    private WithdrawalRequest readyForEntryWithdrawalRequest;

    /**
     * Sets up data for the tests.
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        onlyApprovalState = new ArrayList<WithdrawalStateEnum>(1);
        onlyApprovalState.add(WithdrawalStateEnum.APPROVED);

        beforeEndStates = new ArrayList<WithdrawalStateEnum>(3);
        beforeEndStates.add(WithdrawalStateEnum.DRAFT);
        beforeEndStates.add(WithdrawalStateEnum.PENDING_REVIEW);
        beforeEndStates.add(WithdrawalStateEnum.PENDING_APPROVAL);

        noStates = new ArrayList<WithdrawalStateEnum>(0);

        nullStates = null;

        approvedWithdrawalRequest = new WithdrawalRequest();
        approvedWithdrawalRequest.setStatusCode(WithdrawalStateEnum.APPROVED.getStatusCode());

        deletedWithdrawalRequest = new WithdrawalRequest();
        deletedWithdrawalRequest.setStatusCode(WithdrawalStateEnum.DELETED.getStatusCode());

        deniedWithdrawalRequest = new WithdrawalRequest();
        deniedWithdrawalRequest.setStatusCode(WithdrawalStateEnum.DENIED.getStatusCode());

        draftWithdrawalRequest = new WithdrawalRequest();
        draftWithdrawalRequest.setStatusCode(WithdrawalStateEnum.DRAFT.getStatusCode());

        expiredWithdrawalRequest = new WithdrawalRequest();
        expiredWithdrawalRequest.setStatusCode(WithdrawalStateEnum.EXPIRED.getStatusCode());

        pendingApprovalWithdrawalRequest = new WithdrawalRequest();
        pendingApprovalWithdrawalRequest.setStatusCode(WithdrawalStateEnum.PENDING_APPROVAL
                .getStatusCode());

        pendingReviewWithdrawalRequest = new WithdrawalRequest();
        pendingReviewWithdrawalRequest.setStatusCode(WithdrawalStateEnum.PENDING_REVIEW
                .getStatusCode());

        readyForEntryWithdrawalRequest = new WithdrawalRequest();
        readyForEntryWithdrawalRequest.setStatusCode(WithdrawalStateEnum.READY_FOR_ENTRY
                .getStatusCode());
    }

    /**
     * Removes any test data that may need explicit actions at the end of the test.
     * 
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate#evaluate(java.lang.Object)}.
     */
    @Test
    public void testEvaluateOnlyApprovalState() {

        final WithdrawalStatePredicate withdrawalStatePredicate = new WithdrawalStatePredicate(
                onlyApprovalState);

        assertTrue("Should find it in the state list.", withdrawalStatePredicate
                .evaluate(approvedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deletedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deniedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(draftWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(expiredWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingApprovalWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingReviewWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(readyForEntryWithdrawalRequest));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate#evaluate(java.lang.Object)}.
     */
    @Test
    public void testEvaluateBeforeEndStates() {

        final WithdrawalStatePredicate withdrawalStatePredicate = new WithdrawalStatePredicate(
                beforeEndStates);

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(approvedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deletedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deniedWithdrawalRequest));

        assertTrue("Should find it in the state list.", withdrawalStatePredicate
                .evaluate(draftWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(expiredWithdrawalRequest));

        assertTrue("Should find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingApprovalWithdrawalRequest));

        assertTrue("Should find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingReviewWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(readyForEntryWithdrawalRequest));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate#evaluate(java.lang.Object)}.
     */
    @Test
    public void testEvaluateBeforeEndStatesWithVariableArgumentConstructor() {

        final WithdrawalStatePredicate withdrawalStatePredicate = new WithdrawalStatePredicate(
                WithdrawalStateEnum.DRAFT, WithdrawalStateEnum.PENDING_REVIEW,
                WithdrawalStateEnum.PENDING_APPROVAL);

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(approvedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deletedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deniedWithdrawalRequest));

        assertTrue("Should find it in the state list.", withdrawalStatePredicate
                .evaluate(draftWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(expiredWithdrawalRequest));

        assertTrue("Should find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingApprovalWithdrawalRequest));

        assertTrue("Should find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingReviewWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(readyForEntryWithdrawalRequest));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate#evaluate(java.lang.Object)}.
     */
    @Test
    public void testEvaluateNoStates() {

        final WithdrawalStatePredicate withdrawalStatePredicate = new WithdrawalStatePredicate(
                noStates);

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(approvedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deletedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(deniedWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(draftWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(expiredWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingApprovalWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(pendingReviewWithdrawalRequest));

        assertFalse("Should not find it in the state list.", withdrawalStatePredicate
                .evaluate(readyForEntryWithdrawalRequest));
    }

    /**
     * Test method for
     * {@link com.manulife.pension.service.withdrawal.common.WithdrawalStatePredicate#evaluate(java.lang.Object)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateNullStates() {
        new WithdrawalStatePredicate(nullStates);
    }

}
