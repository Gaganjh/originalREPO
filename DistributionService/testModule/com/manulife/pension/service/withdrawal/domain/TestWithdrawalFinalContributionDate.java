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
 * Tests the validations for the Final Contribution Date.
 * 
 * @author dickand
 */
public class TestWithdrawalFinalContributionDate {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        final Date now = getTimeFreeCurrentDate();
        request.setFinalContributionDate(now);
        final Date requestDate = DateUtils.addDays(now, -30);
        request.setRequestDate(requestDate);
        final Date contractEffectiveDate = DateUtils.addDays(now, -60);
        request.getParticipantInfo().setContractEffectiveDate(contractEffectiveDate);
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
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndTerminationAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndTerminationAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndTerminationAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndTerminationAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndRetirementAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndRetirementAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndRetirementAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndRetirementAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndDisabilityAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndDisabilityAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndDisabilityAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndDisabilityAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndVoluntaryContributionAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndVoluntaryContributionAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndVoluntaryContributionAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndVoluntaryContributionAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNullWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setFinalContributionDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be a warning and no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be one warning.", 1, CollectionUtils.size(request
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING).append("].")
                .toString(), CollectionUtils.exists(request.getWarningCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING)));
        assertEquals("There should be one warning for the field.", 1, CollectionUtils.countMatches(
                request.getWarningCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndTerminationAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndTerminationAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndTerminationAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndTerminationAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndRetirementAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndRetirementAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndRetirementAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndRetirementAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndDisabilityAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndDisabilityAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndDisabilityAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndDisabilityAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_PENCHECKS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateExistsIsNotNullWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsNotMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation exists validation
        withdrawal
                .validateFinalContributionDateExists(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithTotalWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithPartialWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithTotalWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithPartialWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithTotalWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithPartialWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithTotalWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithPartialWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsLessThanSixMonthsAfterRequestDateWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithTotalWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithPartialWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithTotalWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithPartialWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithTotalWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithPartialWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithTotalWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithPartialWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsSixMonthsAfterRequestDateWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Date finalContributionDate = DateUtils.addMonths(request.getRequestDate(), 6);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithTotalWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithPartialWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithTotalWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithPartialWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithTotalWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithPartialWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithTotalWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsMoreThanSixMonthsAfterRequestDateWithPartialWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date sixMonths = DateUtils.addMonths(request.getRequestDate(), 6);
        final Date finalContributionDate = DateUtils.addDays(sixMonths, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecksAndIsNonMandatory() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithTotalWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(null);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithPartialWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithTotalWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithPartialWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithTotalWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithPartialWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithTotalWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithPartialWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsNullWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    // /////////////////////////

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithTotalWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        request.setFinalContributionDate(finalContributionDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithPartialWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithTotalWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithPartialWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithTotalWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithPartialWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_NEITHER_CODE);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithTotalWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be one error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be an error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [")
                        .append(
                                WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)
                        .append("].").toString(),
                CollectionUtils
                        .exists(
                                request.getErrorCodes(),
                                new WithdrawalMessageTypePredicate(
                                        WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.FINAL_CONTRIBUTION_DATE)));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateBeforeContractEffectiveDateWithPartialWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, -1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithTotalWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithPartialWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithTotalWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithPartialWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithTotalWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithPartialWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithTotalWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithPartialWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateIsEqualToContractEffectiveDateWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        request.setFinalContributionDate(request.getParticipantInfo().getContractEffectiveDate());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithTotalWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);

        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithPartialWithdrawalAndTermination() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithTotalWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithPartialWithdrawalAndRetirement() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithTotalWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithPartialWithdrawalAndDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithTotalWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(true);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithPartialWithdrawalAndVoluntaryContribution() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithTotalWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithPartialWithdrawalAndMandatoryDistributionAndWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithTotalWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setParticipantLeavingPlanInd(true);
        request.setIraServiceProviderCode(WithdrawalRequest.IRA_SERVICE_PROVIDER_WMSI_CODE);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the final contribution date field.
     */
    @Test
    public void testFinalContributionDateAfterContractEffectiveDateWithPartialWithdrawalAndMandatoryDistributionAndNotWmsiPenChecks() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);
        request.setIraServiceProviderCode(null);
        request.setParticipantLeavingPlanInd(false);
        final Date contractEffectiveDate = request.getParticipantInfo().getContractEffectiveDate();
        final Date finalContributionDate = DateUtils.addDays(contractEffectiveDate, 1);
        request.setFinalContributionDate(finalContributionDate);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform final contribution date validation
        withdrawal.validateFinalContributionDate();

        // Should be no errors and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }
}
