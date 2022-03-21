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
 * Tests the validations for the Disability Date.
 * 
 * @author dickand
 */
public class TestWithdrawalDisabilityDate {

    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest request = new WithdrawalRequest();
        final Date now = getTimeFreeCurrentDate();
        request.setDisabilityDate(now);
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
     * Tests the validations for the disability date field.
     */
    @Test
    public void testDisabilityDateIsNullAndReasonIsDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDisabilityDate(null);
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform disability date validation
        withdrawal.validateDisabilityDate();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(request.getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DISABILITY_DATE_MISSING_ERROR).append("].").toString(),
                CollectionUtils.exists(request.getErrorCodes(), new WithdrawalMessageTypePredicate(
                        WithdrawalMessageType.DISABILITY_DATE_MISSING_ERROR)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                request.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.DISABILITY_DATE)));
    }

    /**
     * Tests the validations for the disability date field.
     */
    @Test
    public void testDisabilityDateIsNullAndReasonIsNonDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setDisabilityDate(null);
        request
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_WITHDRAWAL_VOLUNTARY_CONTRIBUTIONS_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform disability date validation
        withdrawal.validateDisabilityDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }

    /**
     * Tests the validations for the disability date field.
     */
    @Test
    public void testDisabilityDateIsNonNullAndReasonIsDisability() {

        // Set up withdrawal to test
        final WithdrawalRequest request = getBaseWithdrawalRequest();
        request.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_DISABILITY_CODE);
        final Withdrawal withdrawal = new Withdrawal(request);

        // Perform disability date validation
        withdrawal.validateDisabilityDate();

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(request.getErrorCodes()));
    }
}
