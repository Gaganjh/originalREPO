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
 * Tests the mandatory validations for the Date of Birth.
 * 
 * @author dickand
 */
public class TestWithdrawalDateOfBirth {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        final Date validBirthDate = calendar.getTime();
        request.setBirthDate(validBirthDate);
        request.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);

        return request;
    }

    /**
     * Retrieves a calendar object with the time components cleared.
     */
    private Calendar getTimeFreeCalendar() {
        return DateUtils.truncate(Calendar.getInstance(), Calendar.DATE);
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsValid() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setBirthDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DATE_OF_BIRTH_EMPTY_OR_BLANK).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DATE_OF_BIRTH_EMPTY_OR_BLANK)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.DATE_OF_BIRTH)));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsLessThanFifteen() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that is 1 day less than 15 years
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -15);
        calendar.add(Calendar.DATE, 1);
        request.setBirthDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DATE_OF_BIRTH_INVALID).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DATE_OF_BIRTH_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.DATE_OF_BIRTH)));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsEqualToFifteen() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that is 15 years
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -15);
        request.setBirthDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsMoreThanFifteen() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that is 1 day more than 15 years
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -15);
        calendar.add(Calendar.DATE, -1);
        request.setBirthDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsLessThanOneHundredAndFifty() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that is 1 day less than 150 years
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -150);
        calendar.add(Calendar.DATE, 1);
        request.setBirthDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsEqualToOneHundredAndFifty() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that is 150 years
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -150);
        request.setBirthDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsMoreThanOneHundredAndFifty() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that is 1 day more than 150 years
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -150);
        calendar.add(Calendar.DATE, -1);
        request.setBirthDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DATE_OF_BIRTH_INVALID).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DATE_OF_BIRTH_INVALID)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.DATE_OF_BIRTH)));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsEqualToSystemOfRecordAndLessThanParticipantEnrollmentDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(DateUtils.truncate(DateUtils.truncate(calendar.getTime(),
                Calendar.DATE), Calendar.DATE));
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        // Create participant date enrollment date after date of birth
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsEqualToSystemOfRecordAndEqualToParticipantEnrollmentDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsEqualToSystemOfRecordAndGreaterThanParticipantEnrollmentDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        // Create participant date enrollment date before date of birth
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsNotEqualToSystemOfRecordAndLessThanParticipantEnrollmentDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        // Create participant date enrollment date after date of birth
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        // Create system of record that is different than date of birth
        calendar.add(Calendar.YEAR, 1);
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsNotEqualToSystemOfRecordAndEqualToParticipantEnrollmentDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        // Create system of record that is different than date of birth
        calendar.add(Calendar.YEAR, 1);
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsNotEqualToSystemOfRecordAndGreaterThanParticipantEnrollmentDate() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        // Create participant date enrollment date before date of birth
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        // Create system of record that is different than date of birth
        calendar.add(Calendar.YEAR, 1);
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DATE_OF_BIRTH_GREATER_THAN_ENROLLMENT_DATE).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DATE_OF_BIRTH_GREATER_THAN_ENROLLMENT_DATE)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.DATE_OF_BIRTH)));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsEqualToSystemOfRecordAndParticipantEnrollmentDateIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        request.getParticipantInfo().setParticipantEnrollmentDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsNotEqualToSystemOfRecordAndParticipantEnrollmentDateIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        // Create system of record that is different than date of birth
        calendar.add(Calendar.YEAR, 1);
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(calendar.getTime());
        request.getParticipantInfo().setParticipantEnrollmentDate(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsLessThanParticipantEnrollmentDateAndSystemOfRecordIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        // Create participant date enrollment date after date of birth
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsEqualToParticipantEnrollmentDateAndSystemOfRecordIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the date of birth field.
     */
    @Test
    public void testDateOfBirthIsGreaterThanParticipantEnrollmentDateAndSystemOfRecordIsNull() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        // Create birthdate that valid
        final Calendar calendar = getTimeFreeCalendar();
        calendar.add(Calendar.YEAR, -50);
        request.setBirthDate(calendar.getTime());
        // Create participant date enrollment date before date of birth
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        request.getParticipantInfo().setParticipantEnrollmentDate(calendar.getTime());
        request.getParticipantInfo().setSystemOfRecordDateOfBirth(null);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform date of birth validation
        withdrawal.validateBirthDate();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DATE_OF_BIRTH_GREATER_THAN_ENROLLMENT_DATE).append("].")
                .toString(), CollectionUtils.exists(request.getErrorCodes(),
                new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DATE_OF_BIRTH_GREATER_THAN_ENROLLMENT_DATE)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.DATE_OF_BIRTH)));
    }
}
