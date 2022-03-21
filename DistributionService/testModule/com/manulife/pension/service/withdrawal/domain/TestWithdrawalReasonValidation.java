package com.manulife.pension.service.withdrawal.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.manulife.pension.service.distribution.DistributionContainerEnvironment;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessagePropertyPredicate;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageTypePredicate;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestProperty;

/**
 * Tests the validations for the Withdrawal Reason.
 * 
 * @author Paul Glenn
 */
public class TestWithdrawalReasonValidation extends DistributionContainerEnvironment {

    private static final Logger logger = Logger.getLogger(TestWithdrawalReasonValidation.class);

    private static final int TEST_PROFILE_ID = 139447213;

    private static final int TEST_CONTRACT_ID = 67501;
    
    /**
     * Returns a base withdrawal request class.
     */
    private WithdrawalRequest getBaseWithdrawalRequest() {
        final WithdrawalRequest withdrawalRequest = new WithdrawalRequest();

        withdrawalRequest.setStatusCode(WithdrawalRequest.WITHDRAWAL_STATUS_DRAFT_CODE);

        withdrawalRequest.setParticipantStateOfResidence("FL");
        withdrawalRequest.setPaymentTo(WithdrawalRequest.PAYMENT_TO_DIRECT_TO_PARTICIPANT_CODE);

        withdrawalRequest
                .setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE);

        withdrawalRequest.setEmployeeProfileId(TEST_PROFILE_ID);
        withdrawalRequest.setContractId(TEST_CONTRACT_ID);

        return withdrawalRequest;
    }

    /**
     * Tests the validations for the withdrawal reason field.
     */
    @Test
    public void testWithdrawalReasonFieldWithRetirementAndNoBirthDate() {

        // Set up withdrawal to test
        final WithdrawalRequest withdrawalRequest = getBaseWithdrawalRequest();

        withdrawalRequest.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        withdrawalRequest.setRetirementDate(new Date());

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.validateForProceedToStep2();

        // Should be an error
        assertEquals("There should be one error.", 1, CollectionUtils.size(withdrawalRequest
                .getErrorCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.DATE_OF_BIRTH_EMPTY_OR_BLANK).append("].").toString(),
                CollectionUtils.exists(withdrawalRequest.getErrorCodes(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.DATE_OF_BIRTH_EMPTY_OR_BLANK)));
        assertEquals("There should be one error for the field.", 1, CollectionUtils.countMatches(
                withdrawalRequest.getErrorCodes(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.DATE_OF_BIRTH)));
    }

    /**
     * Tests the validations for the withdrawal reason field.
     */
    @Test
    public void testWithdrawalReasonFieldWithRetirementAndNoPlanValueAndUnder65() {

        // Set up withdrawal to test
        final WithdrawalRequest withdrawalRequest = getBaseWithdrawalRequest();

        withdrawalRequest.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        withdrawalRequest.setRetirementDate(new Date());

        // Make them 30 years old.
        withdrawalRequest.setBirthDate(DateUtils.addYears(new Date(), -30));

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.validateForProceedToStep2();

        // There should be a warning.
        assertEquals("There should be one warning.", 1, CollectionUtils.size(withdrawalRequest
                .getWarningCodes()));
        assertTrue(new StringBuffer("Expecting message type [").append(
                WithdrawalMessageType.INVALID_REASON_CODE_RETIREMENT).append("].").toString(),
                CollectionUtils.exists(withdrawalRequest.getMessages(),
                        new WithdrawalMessageTypePredicate(
                                WithdrawalMessageType.INVALID_REASON_CODE_RETIREMENT)));
        assertEquals("There should be one message for the field.", 1, CollectionUtils.countMatches(
                withdrawalRequest.getMessages(), new WithdrawalMessagePropertyPredicate(
                        WithdrawalRequestProperty.REASON_CODE)));

        // Should be no errors
        assertEquals("There should be no errors.", 0, CollectionUtils.size(withdrawalRequest
                .getErrorCodes()));
    }

    /**
     * Tests the validations for the withdrawal reason field.
     */
    @Test
    public void testWithdrawalReasonFieldWithRetirementAndNoPlanValueAndOver65() {

        // Set up withdrawal to test
        final WithdrawalRequest withdrawalRequest = getBaseWithdrawalRequest();

        withdrawalRequest.setReasonCode(WithdrawalRequest.WITHDRAWAL_REASON_RETIREMENT_CODE);
        withdrawalRequest.setRetirementDate(new Date());

        // Make them 75 years old.
        withdrawalRequest.setBirthDate(DateUtils.addYears(new Date(), -75));

        final Withdrawal withdrawal = new Withdrawal(withdrawalRequest);

        withdrawal.validateForProceedToStep2();

        // Should be no errors
        assertEquals("There should be no messages.", 0, CollectionUtils.size(withdrawalRequest
                .getMessages()));
    }

}
