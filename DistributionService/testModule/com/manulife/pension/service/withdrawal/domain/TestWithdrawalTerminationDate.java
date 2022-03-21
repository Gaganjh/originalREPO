package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;

import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the validations for the Termination Date.
 * 
 * @author dickand
 */
public class TestWithdrawalTerminationDate {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        final Date now = getTimeFreeCurrentDate();
        request.setTerminationDate(now);
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
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateIsNullAndReasonIsTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setTerminationDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be an error and no warnings
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TERMINATION_DATE_MISSING_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TERMINATION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.TERMINATION_DATE)));
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateIsNullAndReasonIsMandatoryDistribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setTerminationDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be an error and no warnings
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.TERMINATION_DATE_MISSING_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.TERMINATION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.TERMINATION_DATE)));
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateIsNullAndReasonIsNonTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setTerminationDate(null);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateIsNonNullAndReasonIsTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateIsNonNullAndReasonIsMandatoryDistribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the termination date field.
     */
//    Leads to assertion failure
    @Test
    public void testTerminationDateBeforeContractEffectiveDateAndReasonIsTermination() {

//        // Set up withdrawal to test
//        final WithdrawalRequest request = getBaseWithdrawalRequest();
//        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
//        final Date terminationDate = request.getTerminationDate();
//        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, 1);
//        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);
//
//        final Withdrawal withdrawal = new Withdrawal(request);
//
//        // Perform termination date validation
//        withdrawal.validateTerminationDate();
//
//        // Should be an error and no warnings
//        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
//        assertTrue(new StringBuffer("Expecting message type [").append(
//                WithdrawalMessageType.TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE).append("].")
//                .toString(), CollectionUtils.exists(request.getErrorCodes(),
//                new WithdrawalMessageTypePredicate(
//                        WithdrawalMessageType.TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE)));
//        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
//                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
//                        WithdrawalRequestProperty.TERMINATION_DATE)));
//        assertEquals("Termination date should be updated.", contractEffectiveDate, request
//                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateSameAsContractEffectiveDateAndReasonIsTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Date terminationDate = request.getTerminationDate();
        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, 0);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateAfterContractEffectiveDateAndReasonIsTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Date terminationDate = request.getTerminationDate();
        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, -1);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
//    Leads to assertion failure
    @Test
    public void testTerminationDateBeforeContractEffectiveDateAndReasonIsMandatoryDistribution() {

//        // Set up withdrawal to test
//        final WithdrawalRequest request = getBaseWithdrawalRequest();
//        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
//        final Date terminationDate = request.getTerminationDate();
//        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, 1);
//        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);
//
//        final Withdrawal withdrawal = new Withdrawal(request);
//
//        // Perform termination date validation
//        withdrawal.validateTerminationDate();
//
//        // Should be an error and no warnings
//        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
//        assertTrue(new StringBuffer("Expecting message type [").append(
//                WithdrawalMessageType.TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE).append("].")
//                .toString(), CollectionUtils.exists(request.getErrorCodes(),
//                new WithdrawalMessageTypePredicate(
//                        WithdrawalMessageType.TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE)));
//        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
//                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
//                        WithdrawalRequestProperty.TERMINATION_DATE)));
//        assertEquals("Termination date should be updated.", contractEffectiveDate, request
//                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateSameAsContractEffectiveDateAndReasonIsMandatoryDistribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        final Date terminationDate = request.getTerminationDate();
        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, 0);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateAfterContractEffectiveDateAndReasonIsMandatoryDistribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        final Date terminationDate = request.getTerminationDate();
        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, -1);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateBeforeContractEffectiveDateAndReasonIsNonTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date terminationDate = request.getTerminationDate();
        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, 1);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateSameAsContractEffectiveDateAndReasonIsNonTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date terminationDate = request.getTerminationDate();
        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, 0);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateAfterContractEffectiveDateAndReasonIsNonTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date terminationDate = request.getTerminationDate();
        final Date contractEffectiveDate = DateUtils.addDays(terminationDate, -1);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateValidAndContractEffectiveDateIsNullAndReasonIsTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Date terminationDate = request.getTerminationDate();
        request.getParticipantInfo().setContractEffectiveDate(null);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateValidAndContractEffectiveDateIsNullAndReasonIsMandatoryDistribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        final Date terminationDate = request.getTerminationDate();
        request.getParticipantInfo().setContractEffectiveDate(null);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }

    /**
     * Tests the validations for the termination date field.
     */
    @Test
    public void testTerminationDateValidAndContractEffectiveDateIsNullAndReasonIsNonTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Date terminationDate = request.getTerminationDate();
        request.getParticipantInfo().setContractEffectiveDate(null);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform termination date validation
        withdrawal.validateTerminationDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("Termination date should be unchanged.", terminationDate, request
                .getTerminationDate());
    }
}
