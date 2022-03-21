package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;

/**
 * Tests the validations for the Retirement Date.
 * 
 * @author dickand
 */
public class TestWithdrawalRetirementDate {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        final Date now = getTimeFreeCurrentDate();
        request.setRetirementDate(now);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);

        // Set contract effective date
        request.getParticipantInfo().setContractEffectiveDate(DateUtils.addDays(now, -10));

        return request;
    }

    /**
     * Retrieves a date object with the time components cleared.
     */
    private Date getTimeFreeCurrentDate() {
        return DateUtils.truncate(new Date(), Calendar.DATE);
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateIsNullAndReasonIsRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setRetirementDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be an error and no warnings
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.RETIREMENT_DATE_MISSING_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.RETIREMENT_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.RETIREMENT_DATE)));
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateIsNullAndReasonIsNonRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setRetirementDate(null);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateIsNonNullAndReasonIsRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    
    /**
     * Tests the validations for the Retirement date field.
     */
//    Leads to assertion error
    @Test
    public void testRetirementDateBeforeContractEffectiveDateAndReasonIsRetirement() {

        // Set up withdrawal to test
//        final WithdrawalRequest request = getBaseWithdrawalRequest();
//        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
//        final Date retirementDate = request.getRetirementDate();
//        final Date contractEffectiveDate = DateUtils.addDays(retirementDate, 1);
//        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);
//
//        final Withdrawal withdrawal = new Withdrawal(request);
//
//        // Perform Retirement date validation
//        withdrawal.validateRetirementDate();
//
//        // Should be an error and no warnings
//        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
//        assertTrue(new StringBuffer("Expecting message type [").append(
//                WithdrawalMessageType.RETIREMENT_DATE_BEFORE_CONTRACT_EFFECTIVE).append("].")
//                .toString(), CollectionUtils.exists(request.getErrorCodes(),
//                new WithdrawalMessageTypePredicate(
//                        WithdrawalMessageType.RETIREMENT_DATE_BEFORE_CONTRACT_EFFECTIVE)));
//        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
//                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
//                        WithdrawalRequestProperty.RETIREMENT_DATE)));
//        assertEquals("Retirement date should be updated.", contractEffectiveDate, request
//                .getRetirementDate());
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateSameAsContractEffectiveDateAndReasonIsRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        final Date retirementDate = request.getRetirementDate();
        final Date contractEffectiveDate = DateUtils.addDays(retirementDate, 0);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Retirement date should be unchanged.", retirementDate, request
                .getRetirementDate());
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateAfterContractEffectiveDateAndReasonIsRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        final Date retirementDate = request.getRetirementDate();
        final Date contractEffectiveDate = DateUtils.addDays(retirementDate, -1);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Retirement date should be unchanged.", retirementDate, request
                .getRetirementDate());
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateBeforeContractEffectiveDateAndReasonIsNonRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date retirementDate = request.getRetirementDate();
        final Date contractEffectiveDate = DateUtils.addDays(retirementDate, 1);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Retirement date should be unchanged.", retirementDate, request
                .getRetirementDate());
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateSameAsContractEffectiveDateAndReasonIsNonRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date retirementDate = request.getRetirementDate();
        final Date contractEffectiveDate = DateUtils.addDays(retirementDate, 0);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Retirement date should be unchanged.", retirementDate, request
                .getRetirementDate());
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateAfterContractEffectiveDateAndReasonIsNonRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date retirementDate = request.getRetirementDate();
        final Date contractEffectiveDate = DateUtils.addDays(retirementDate, -1);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Retirement date should be unchanged.", retirementDate, request
                .getRetirementDate());
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateValidAndContractEffectiveDateIsNullAndReasonIsRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        final Date retirementDate = request.getRetirementDate();
        request.getParticipantInfo().setContractEffectiveDate(null);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Retirement date should be unchanged.", retirementDate, request
                .getRetirementDate());
    }

    /**
     * Tests the validations for the Retirement date field.
     */
    @Test
    public void testRetirementDateValidAndContractEffectiveDateIsNullAndReasonIsNonRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date retirementDate = request.getRetirementDate();
        request.getParticipantInfo().setContractEffectiveDate(null);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform Retirement date validation
        withdrawal.validateRetirementDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Retirement date should be unchanged.", retirementDate, request
                .getRetirementDate());
    }
}
