package com.manulife.pension.service.withdrawal.valueobject;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;

/**
 * @author dickand
 */
public class TestParticipantInitiated {

    /**
     * Tests the participant initiated query.
     */
    @Test
    public void testNullUserRole() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setUserRoleCode(null);
        assertFalse("Request is not particiant initiated.", request.getParticipantInitiated());
    }

    /**
     * Tests the participant initiated query.
     */
    @Test
    public void testBlankUserRole() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setUserRoleCode(StringUtils.EMPTY);
        assertFalse("Request is not particiant initiated.", request.getParticipantInitiated());
    }

    /**
     * Tests the participant initiated query.
     */
    @Test
    public void testTpaUserRole() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setUserRoleCode(WithdrawalRequest.USER_ROLE_TPA_CODE);
        assertFalse("Request is not particiant initiated.", request.getParticipantInitiated());
    }

    /**
     * Tests the participant initiated query.
     */
    @Test
    public void testPlanSponsorUserRole() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setUserRoleCode(WithdrawalRequest.USER_ROLE_PLAN_SPONSOR_CODE);
        assertFalse("Request is not particiant initiated.", request.getParticipantInitiated());
    }

    /**
     * Tests the participant initiated query.
     */
    @Test
    public void testParticipantUserRole() {

        final WithdrawalRequest request = new WithdrawalRequest();
        request.setUserRoleCode(WithdrawalRequest.USER_ROLE_PARTICIPANT_CODE);
        assertTrue("Request is particiant initiated.", request.getParticipantInitiated());
    }

    /**
     * Creates a suite of Junit 4 tests.
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestParticipantInitiated.class);
    }
}
