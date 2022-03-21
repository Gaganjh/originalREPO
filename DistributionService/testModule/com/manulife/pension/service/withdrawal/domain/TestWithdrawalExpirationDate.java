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

import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;

/**
 * Tests the mandatory validations for the Expiration Date.
 * 
 * @author dickand
 */
public class TestWithdrawalExpirationDate {

    private static final int FUTURE_EXPIRATION_DATE_THRESHOLD = 60;

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        final Date now = getTimeFreeCurrentDate();
        final Date validExpirationDate = DateUtils.addDays(now, 20);
        final Date validRequestDate = DateUtils.addDays(now, -10);
        final Date validDateOfBirth = DateUtils.addYears(now, -50);
        request.setBirthDate(validDateOfBirth);
        request.setExpirationDate(validExpirationDate);
        request.setRequestDate(validRequestDate);
        final WithdrawalRequestRecipient recipient = new WithdrawalRequestRecipient();
        recipient.setStateOfResidenceCode("CA");
        final Collection<Recipient> recipients = new ArrayList<Recipient>();
        recipients.add(recipient);
        request.setRecipients(recipients);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_PENDING_REVIEW_CODE);

        return request;
    }

    /**
     * Retrieves a date object with the time components cleared.
     */
    private Date getTimeFreeCurrentDate() {
        return DateUtils.truncate(new Date(), Calendar.DATE);
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateIsValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation (mandatory is in save post draft)
        withdrawal.validateForSavePostDraft();

        // Should be no errors or warnings
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setExpirationDate(null);
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation (mandatory is in save post draft)
        withdrawal.validateForSavePostDraft();

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.EXPIRATION_DATE_INVALID).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.EXPIRATION_DATE_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.EXPIRATION_DATE)));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateIsLessThanSixtyDaysAfterRequestDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Date newExpirationDate = DateUtils.addDays(request.getRequestDate(),
                FUTURE_EXPIRATION_DATE_THRESHOLD - 1);
        request.setExpirationDate(newExpirationDate);
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation
        withdrawal.validateExpirationDate();

        // Should be no errors or warnings
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateIsSixtyDaysAfterRequestDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Date newExpirationDate = DateUtils.addDays(request.getRequestDate(),
                FUTURE_EXPIRATION_DATE_THRESHOLD);
        request.setExpirationDate(newExpirationDate);
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation
        withdrawal.validateExpirationDate();

        // Should be no errors or warnings
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateIsMoreThanSixtyDaysAfterRequestDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Date newExpirationDate = DateUtils.addDays(request.getRequestDate(),
                FUTURE_EXPIRATION_DATE_THRESHOLD + 1);
        request.setExpirationDate(newExpirationDate);
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation
        withdrawal.validateExpirationDate();

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(
                new StringBuffer("Expecting message type [").append(
                        WithdrawalMessageType.EXPIRATION_DATE_GREATER_THAN_MAXIMUM).append("].")
                        .toString(), CollectionUtils.exists(request.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.EXPIRATION_DATE_GREATER_THAN_MAXIMUM)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.EXPIRATION_DATE)));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testNewExpirationDateIsAfterSavedExpirationDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Date newExpirationDate = DateUtils.addDays(savedRequest.getExpirationDate(), 1);
        request.setExpirationDate(newExpirationDate);
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation
        withdrawal.validateExpirationDate();

        // Should be no errors or warnings
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateIsEqualToSavedExpirationDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation
        withdrawal.validateExpirationDate();

        // Should be no errors or warnings
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateIsBeforeSavedExpirationDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        final Date newExpirationDate = DateUtils.addDays(savedRequest.getExpirationDate(), -1);
        request.setExpirationDate(newExpirationDate);
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation
        withdrawal.validateExpirationDate();

        // Should be an error and no warnings
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.EXPIRATION_DATE_BEFORE_SAVED).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.EXPIRATION_DATE_BEFORE_SAVED)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.EXPIRATION_DATE)));
    }

    /**
     * Tests the validations for the expiration date field.
     */
    @Test
    public void testExpirationDateWithNullSavedExpirationDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final WithdrawalRequest savedRequest = getBaseWithdrawalRequest();
        savedRequest.setExpirationDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);
        withdrawal.setSavedWithdrawalRequest(savedRequest);

        // Perform expiration date validation
        withdrawal.validateExpirationDate();

        // Should be no errors or warnings
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
        assertEquals("There should be no warnings.", 0, CollectionUtils.size(request
                .getWarningCodes()));
    }
}
