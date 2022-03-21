package com.manulife.pension.ps.web.withdrawal.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.manulife.pension.common.MessageCategory;
import com.manulife.pension.common.MessageType;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;
import com.manulife.pension.validator.ValidationError;
import com.manulife.pension.validator.ValidationError.Type;

/**
 * WithdrawalValidationMappingHelper is used to hold the withdrawal validation mappings between the
 * business and web tiers.
 * 
 * @author glennpa
 */
public final class WithdrawalValidationMappingHelper {

    public static final Logger logger = Logger.getLogger(WithdrawalValidationMappingHelper.class);

    /**
     * The constructor is private, as this helper class should not be instantiated.
     */
    private WithdrawalValidationMappingHelper() {
        super();
    }

    private static Map<MessageType, Integer> messageToErrorCodeMap;

    /**
     * getWithdrawalMessageToErrorCodeMap gets the static mapping of {@link WithdrawalMessageType}
     * to {@link ErrorCodes}.
     * 
     * @return Map - The map of message types (Biz) to error codes (UI).
     */
    private static Map<MessageType, Integer> getWithdrawalMessageToErrorCodeMap() {

        if (messageToErrorCodeMap != null) {
            return messageToErrorCodeMap;
        }

        final Map<MessageType, Integer> map = new HashMap<MessageType, Integer>();
        messageToErrorCodeMap = map;

        // Put in a default value - TODO: Remove this, as they should all be mapped.
        for (WithdrawalMessageType withdrawalMessageType : WithdrawalMessageType.values()) {
            map.put(withdrawalMessageType, ErrorCodes.INVALID_ENTRY);
        }

        // Override the defaults....
        map.put(WithdrawalMessageType.AMOUNT_TYPE_REQUIRED,
                ErrorCodes.WITHDRAWAL_AMOUNT_TYPE_REQUIRED);
        map.put(WithdrawalMessageType.WITHDRAWAL_AMOUNT_INVALID,
                ErrorCodes.WITHDRAWAL_AMOUNT_NOT_VALID);
        map.put(WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_AVAILABLE_AMOUNT,
                ErrorCodes.REQUESTED_AMOUNT_EXCEEDS_AVAILABLE_AMOUNT);
        map.put(WithdrawalMessageType.VESTED_PERCENTAGE_INVALID,
                ErrorCodes.VESTED_PERCENTAGE_INVALID);
        map.put(WithdrawalMessageType.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC,
                ErrorCodes.TOTAL_REQUESTED_NOT_EQUAL_TO_SPECIFIC);
        map
                .put(WithdrawalMessageType.REQUESTED_AMOUNT_INVALID,
                        ErrorCodes.REQUESTED_AMOUNT_INVALID);
        map.put(WithdrawalMessageType.REQUESTED_AMOUNT_NOT_GREATER_THAN_ZERO,
                ErrorCodes.REQUESTED_AMOUNT_NOT_GREATER_THAN_ZERO);
        map.put(WithdrawalMessageType.REQUESTED_PERCENTAGE_INVALID,
                ErrorCodes.REQUESTED_PERCENTAGE_INVALID);
        map.put(WithdrawalMessageType.TOTAL_REQUESTED_NOT_GREATER_THAN_ZERO,
                ErrorCodes.TOTAL_REQUESTED_NOT_GREATER_THAN_ZERO);
        map.put(WithdrawalMessageType.SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE,
                ErrorCodes.SPECIFIC_AMOUNT_GREATER_THAN_TOTAL_BALANCE);
        map.put(WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_ZERO,
                ErrorCodes.REQUESTED_AMOUNT_EXCEEDS_ZERO);
        map.put(WithdrawalMessageType.REQUESTED_AMOUNT_EXCEEDS_BALANCE,
                ErrorCodes.REQUESTED_AMOUNT_EXCEEDS_BALANCE);
        map.put(WithdrawalMessageType.SPECIFIC_AMOUNT_WITHIN_THRESHOLD,
                ErrorCodes.SPECIFIC_AMOUNT_WITHIN_THRESHOLD);
        map.put(WithdrawalMessageType.REQUESTED_AMOUNT_WITHIN_THRESHOLD,
                ErrorCodes.REQUESTED_AMOUNT_WITHIN_THRESHOLD);
        map.put(WithdrawalMessageType.REQUESTED_AMOUNT_WITHIN_THRESHOLD_FOR_HA,
                ErrorCodes.REQUESTED_AMOUNT_WITHIN_THRESHOLD_FOR_HA);
        map
                .put(
                        WithdrawalMessageType.TOTAL_REQUESTED_AMOUNT_RESTRICTED_FOR_MANDATORY_DISTRIBUTION_TERMINATION,
                        ErrorCodes.TOTAL_REQUESTED_AMOUNT_RESTRICTED_FOR_MANDATORY_DISTRIBUTION_TERMINATION);
        map.put(WithdrawalMessageType.WMSI_UNDER_THRESHHOLD, ErrorCodes.WMSI_UNDER_THRESHHOLD);
        map.put(WithdrawalMessageType.TPA_FEE_AMOUNT_INVALID, ErrorCodes.TPA_FEE_AMOUNT_INVALID);
        map.put(WithdrawalMessageType.TPA_FEE_PERCENTAGE_INVALID,
                ErrorCodes.TPA_FEE_PERCENTAGE_INVALID);
        map.put(WithdrawalMessageType.TPA_FEE_TYPE_INVALID, ErrorCodes.TPA_FEE_TYPE_INVALID);
        map.put(WithdrawalMessageType.TPA_DOLLAR_EXCEEDS_TOTAL_BALANCE,
                ErrorCodes.TPA_FEE_AMOUNT_EXCEEDS_TOTAL_BALANCE);
        map.put(WithdrawalMessageType.TAXES_OVER_ONE_HUNDRED_PERCENT,
                ErrorCodes.TAXES_OVER_ONE_HUNDRED_PERCENT);
        map.put(WithdrawalMessageType.FEDERAL_TAX_INVALID, ErrorCodes.FEDERAL_TAX_INVALID);
        map.put(WithdrawalMessageType.STATE_TAX_NOT_ZERO_WHEN_FEDERAL_IS,
                ErrorCodes.STATE_TAX_NOT_ZERO_WHEN_FEDERAL_IS);
        map.put(WithdrawalMessageType.STATE_TAX_INVALID, ErrorCodes.STATE_TAX_INVALID);
        map.put(WithdrawalMessageType.STATE_TAX_EXCEEDS_MAXIMUM,
                ErrorCodes.STATE_TAX_EXCEEDS_MAXIMUM);
        map.put(WithdrawalMessageType.TPA_DOLLAR_THRESHOLD, ErrorCodes.TPA_DOLLAR_THRESHOLD);
        map.put(WithdrawalMessageType.TPA_PERCENT_THRESHOLD, ErrorCodes.TPA_PRECENT_THRESHOLD);
        map.put(WithdrawalMessageType.OPTION_FOR_UNVESTED_AMOUNT_INVALID,
                ErrorCodes.OPTION_FOR_UNVESTED_AMOUNT_INVALID);
        map.put(WithdrawalMessageType.PAYMENT_METHOD_INVALID, ErrorCodes.PAYMENT_METHOD_INVALID);
        map.put(WithdrawalMessageType.FI_NAME_INVALID, ErrorCodes.FI_NAME_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_FI_LINE_ONE_INVALID,
                ErrorCodes.ADDRESS_FI_LINE_ONE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_FI_CITY_INVALID, ErrorCodes.ADDRESS_FI_CITY_INVALID);
        map
                .put(WithdrawalMessageType.ADDRESS_FI_STATE_INVALID,
                        ErrorCodes.ADDRESS_FI_STATE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_FI_ZIP_ONE_INVALID,
                ErrorCodes.ADDRESS_FI_ZIP_ONE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_FI_ZIP_TWO_INVALID,
                ErrorCodes.ADDRESS_FI_ZIP_TWO_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE,
                ErrorCodes.ADDRESS_FI_ZIP_CODE_INVALID_FOR_STATE);
        map.put(WithdrawalMessageType.ADDRESS_FI_COUNTRY_INVALID,
                ErrorCodes.ADDRESS_FI_COUNTRY_INVALID);
        map.put(WithdrawalMessageType.FI_BANK_NAME_INVALID, ErrorCodes.FI_BANK_NAME_INVALID);
        map.put(WithdrawalMessageType.FI_ACCOUNT_NUMBER_INVALID,
                ErrorCodes.FI_ACCOUNT_NUMBER_INVALID);
        map.put(WithdrawalMessageType.FI_CREDIT_PARTY_NAME_INVALID,
                ErrorCodes.FI_CREDIT_PARTY_NAME_INVALID);
        map.put(WithdrawalMessageType.FI_ABA_NUMBER_INVALID, ErrorCodes.FI_ABA_NUMBER_INVALID);
        map.put(WithdrawalMessageType.FI_ABA_NUMBER_NOT_GREATER_THAN_ZERO,
                ErrorCodes.FI_ABA_NUMBER_NOT_GREATER_THAN_ZERO);
        map
                .put(WithdrawalMessageType.CHECK_PAYEE_NAME_INVALID,
                        ErrorCodes.CHECK_PAYEE_NAME_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID,
                ErrorCodes.ADDRESS_CHECK_PAYEE_LINE_ONE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_CHECK_PAYEE_CITY_INVALID,
                ErrorCodes.ADDRESS_CHECK_PAYEE_CITY_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_CHECK_PAYEE_STATE_INVALID,
                ErrorCodes.ADDRESS_CHECK_PAYEE_STATE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_CODE_INVALID_FOR_STATE,
                ErrorCodes.ADDRESS_CHECK_PAYEE_ZIP_CODE_INVALID_FOR_STATE);
        map.put(WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID,
                ErrorCodes.ADDRESS_CHECK_PAYEE_ZIP_ONE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_CHECK_PAYEE_ZIP_TWO_INVALID,
                ErrorCodes.ADDRESS_CHECK_PAYEE_ZIP_TWO_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_CHECK_PAYEE_COUNTRY_INVALID,
                ErrorCodes.ADDRESS_CHECK_PAYEE_COUNTRY_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_1099R_LINE_ONE_INVALID,
                ErrorCodes.ADDRESS_1099R_LINE_ONE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_1099R_CITY_INVALID,
                ErrorCodes.ADDRESS_1099R_CITY_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_1099R_STATE_INVALID,
                ErrorCodes.ADDRESS_1099R_STATE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_1099R_ZIP_ONE_INVALID,
                ErrorCodes.ADDRESS_1099R_ZIP_ONE_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_1099R_ZIP_TWO_INVALID,
                ErrorCodes.ADDRESS_1099R_ZIP_TWO_INVALID);
        map.put(WithdrawalMessageType.ADDRESS_1099R_ZIP_CODE_INVALID_FOR_STATE,
                ErrorCodes.ADDRESS_1099R_ZIP_CODE_INVALID_FOR_STATE);
        map.put(WithdrawalMessageType.ADDRESS_1099R_COUNTRY_INVALID,
                ErrorCodes.ADDRESS_1099R_COUNTRY_INVALID);
        map.put(WithdrawalMessageType.ACCOUNT_TYPE_INVALID, ErrorCodes.ACCOUNT_TYPE_INVALID);
        map.put(WithdrawalMessageType.PARTICIPANT_US_CITIZEN_INVALID_ERROR,
                ErrorCodes.PARTICIPANT_US_CITIZEN_INVALID);
        map.put(WithdrawalMessageType.PARTICIPANT_US_CITIZEN_INVALID_WARNING,
                ErrorCodes.PARTICIPANT_US_CITIZEN_INVALID);
        map.put(WithdrawalMessageType.PARTICIPANT_NOT_US_CITIZEN,
                ErrorCodes.PARTICIPANT_NOT_US_CITIZEN);
        map.put(WithdrawalMessageType.NOTE_TO_PARTICIPANT_INVALID,
                ErrorCodes.NOTE_TO_PARTICIPANT_INVALID);
        map.put(WithdrawalMessageType.ACCOUNT_NUMBER_FOR_ROLLOVER_INVALID,
                ErrorCodes.ACCOUNT_NUMBER_FOR_ROLLOVER_INVALID);
        map.put(WithdrawalMessageType.NAME_OF_PLAN_INVALID, ErrorCodes.NAME_OF_PLAN_INVALID);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_ERROR,
                ErrorCodes.IRS_CODE_FOR_WITHDRAWAL_ERROR);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_WARNING,
                ErrorCodes.IRS_CODE_FOR_WITHDRAWAL_WARNING);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL,
                ErrorCodes.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_NORMAL);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION,
                ErrorCodes.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_EARLY_DISTRIBUTION);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER,
                ErrorCodes.IRS_CODE_FOR_WITHDRAWAL_SHOULD_BE_ROLLOVER);
        map.put(WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_ERROR,
                ErrorCodes.DECLARATION_TAX_NOTICE_INVALID_ERROR);
        map.put(WithdrawalMessageType.DECLARATION_TAX_NOTICE_INVALID_WARNING,
                ErrorCodes.DECLARATION_TAX_NOTICE_INVALID_WARNING);
        map.put(WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_ERROR,
                ErrorCodes.DECLARATION_WAITING_PERIOD_INVALID_ERROR);
        map.put(WithdrawalMessageType.DECLARATION_WAITING_PERIOD_INVALID_WARNING,
                ErrorCodes.DECLARATION_WAITING_PERIOD_INVALID_WARNING);
        map.put(WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_ERROR,
                ErrorCodes.DECLARATION_IRA_PROVIDER_INVALID_ERROR);
        map.put(WithdrawalMessageType.DECLARATION_IRA_PROVIDER_INVALID_WARNING,
                ErrorCodes.DECLARATION_IRA_PROVIDER_INVALID_WARNING);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_LOAN_ERROR, ErrorCodes.IRS_CODE_FOR_LOAN_ERROR);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_LOAN_WARNING,
                ErrorCodes.IRS_CODE_FOR_LOAN_WARNING);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL,
                ErrorCodes.IRS_CODE_FOR_LOAN_SHOULD_BE_NORMAL);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION,
                ErrorCodes.IRS_CODE_FOR_LOAN_SHOULD_BE_EARLY_DISTRIBUTION);
        map.put(WithdrawalMessageType.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER,
                ErrorCodes.IRS_CODE_FOR_LOAN_SHOULD_BE_ROLLOVER);
        map.put(WithdrawalMessageType.HARDSHIP_REASON_MISSING_WARNING,
                ErrorCodes.HardshipReasonCodeMissingError);
        map.put(WithdrawalMessageType.HARDSHIP_REASON_EXPLANATION_MISSISNG_WARNING,
                ErrorCodes.HardshipReasonExplanationMissingError);
        map.put(WithdrawalMessageType.TERMINATION_DATE_MISSING_ERROR,
                ErrorCodes.TERMINATION_DATE_MISSING_ERROR);
        map.put(WithdrawalMessageType.TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE,
                ErrorCodes.TERMINATION_DATE_BEFORE_CONTRACT_EFFECTIVE);       
        map.put(WithdrawalMessageType.RETIREMENT_DATE_MISSING_ERROR,
                ErrorCodes.RETIREMENT_DATE_MISSING_ERROR);
        map.put(WithdrawalMessageType.IRS_DISTRIBUTION_CODE_FOR_LOANS_ERROR,
                ErrorCodes.IRS_DISTRIBUTION_CODE_FOR_LOANS_ERROR);
        map.put(WithdrawalMessageType.RETIREMENT_DATE_BEFORE_CONTRACT_EFFECTIVE,
                ErrorCodes.RETIREMENT_DATE_BEFORE_CONTRACT_EFFECTIVE);
        map.put(WithdrawalMessageType.DISABILITY_DATE_MISSING_ERROR,
                ErrorCodes.DISABILITY_DATE_MISSING_ERROR);
        map.put(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_ERROR,
                ErrorCodes.FINAL_CONTRIBUTION_DATE_MISSING_ERROR);
        map.put(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_MISSING_WARNING,
                ErrorCodes.FINAL_CONTRIBUTION_DATE_MISSING_WARNING);
        map.put(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR,
                ErrorCodes.FINAL_CONTRIBUTION_DATE_BEFORE_CONTRACT_EFFECTIVE_ERROR);
        map.put(WithdrawalMessageType.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR,
                ErrorCodes.FINAL_CONTRIBUTION_DATE_OVER_SIX_MONTHS_IN_FUTURE_ERROR);
        map.put(WithdrawalMessageType.PAYMENT_TO_INVALID, ErrorCodes.PAYMENT_TO_INVALID);
        map.put(WithdrawalMessageType.WITHDRAWAL_REASON_INVALID,
                ErrorCodes.WITHDRAWAL_REASON_INVALID);
        map.put(WithdrawalMessageType.STATE_OF_RESIDENCE_INVALID,
                ErrorCodes.STATE_OF_RESIDENCE_INVALID);
//        map.put(WithdrawalMessageType.USER_MATCHES_THE_PARTICIPANT,
//                ErrorCodes.USER_MATCHES_THE_PARTICIPANT);
        map.put(WithdrawalMessageType.DATE_OF_BIRTH_INVALID, ErrorCodes.BIRTH_DATE_INVALID);
        map.put(WithdrawalMessageType.DATE_OF_BIRTH_EMPTY_OR_BLANK,
                ErrorCodes.BIRTH_DATE_EMPTY_OR_BLANK);
        map.put(WithdrawalMessageType.DATE_OF_BIRTH_GREATER_THAN_ENROLLMENT_DATE,
                ErrorCodes.BIRTH_DATE_GREATER_THAN_ENROLLMENT_DATE);
        map.put(WithdrawalMessageType.LEGALLY_MARRIED_IND_NULL,
                ErrorCodes.MISSING_LEGALLY_MARRIED_IND);
        map.put(WithdrawalMessageType.EXPIRATION_DATE_INVALID, ErrorCodes.EXPIRATION_DATE_INVALID);
        map.put(WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_VIEW_PAGE,
                ErrorCodes.WITHDRAWAL_REQUEST_HAS_EXPIRED_VIEW_PAGE);
        map.put(WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE,
                ErrorCodes.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE);
        map.put(WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_LIST_PAGE,
                ErrorCodes.WITHDRAWAL_REQUEST_HAS_EXPIRED_LIST_PAGE);
        map.put(WithdrawalMessageType.EXPIRATION_DATE_BEFORE_SAVED,
                ErrorCodes.EXPIRATION_DATE_BEFORE_SAVED);
        map.put(WithdrawalMessageType.EXPIRATION_DATE_GREATER_THAN_MAXIMUM,
                ErrorCodes.EXPIRATION_DATE_GREATER_THAN_MAXIMUM);
        map.put(WithdrawalMessageType.LOAN_REPAYMENT, ErrorCodes.LOAN_REPAYMENT);
        map.put(WithdrawalMessageType.LOAN_OPTION_REPAY_SELECTED_ERROR,
                ErrorCodes.LOAN_OPTION_REPAY_SELECTED_ERROR);
        map.put(WithdrawalMessageType.PARTICIPANT_SEARCH_REQUIRES_A_CONTRACT,
                ErrorCodes.PARTICIPANT_SEARCH_REQUIRES_A_CONTRACT);
        map.put(WithdrawalMessageType.INVALID_REASON_CODE_RETIREMENT,
                ErrorCodes.INVALID_REASON_CODE_RETIREMENT);
        map.put(WithdrawalMessageType.WITHDRAWAL_REASON_DOES_NOT_MATCH_PARTICIPANT_STATUS,
                ErrorCodes.WITHDRAWAL_REASON_DOES_NOT_MATCH_PARTICIPANT_STATUS);
        map.put(WithdrawalMessageType.STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE_POST_DRAFT,
                ErrorCodes.STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE_POST_DRAFT);
        map.put(WithdrawalMessageType.STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE,
                ErrorCodes.STEP_1_DRIVER_FIELDS_CHANGED_SINCE_SAVE);
        map.put(WithdrawalMessageType.CONTRACT_STATUS_FROZEN,
                ErrorCodes.CONTRACT_STATUS_FROZEN_ERROR);

        // Vesting messages.
        map.put(WithdrawalMessageType.VESTING_CREDITING_METHOD_IS_UNSPECIFIED,
                ErrorCodes.VESTING_CREDITING_METHOD_IS_UNSPECIFIED);
        map.put(WithdrawalMessageType.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP,
                ErrorCodes.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP);
        map.put(WithdrawalMessageType.VESTING_MISSING_EMPLOYEE_DATA,
                ErrorCodes.VESTING_MISSING_EMPLOYEE_DATA);
        map.put(WithdrawalMessageType.VESTING_MORE_RECENT_DATA_USED_FOR_CALCULATION_CODE,
                ErrorCodes.VESTING_MORE_RECENT_DATA_USED_FOR_CALCULATION_CODE);
        map.put(WithdrawalMessageType.VESTING_GENERAL_MESSAGE_ERROR,
                ErrorCodes.VESTING_GENERAL_CRITICAL_MESSAGE);
        map.put(WithdrawalMessageType.VESTING_GENERAL_MESSAGE_ALERT,
                ErrorCodes.VESTING_GENERAL_NON_CRITICAL_MESSAGE);
        map.put(WithdrawalMessageType.VESTING_ENGINE_ALLOWS_VALUE_UPDATES,
                ErrorCodes.VESTING_ENGINE_ALLOWS_VALUE_UPDATES);
        map.put(WithdrawalMessageType.VESTING_ROBUST_DATE_CHANGED_AFTER_VESTING_CALLED,
                ErrorCodes.VESTING_ROBUST_DATE_CHANGED_AFTER_VESTING_CALLED);

        map.put(WithdrawalMessageType.MISSING_IRA_PROVIDER_ERROR, ErrorCodes.IRA_PROVIDER_MISSING);
        map
                .put(WithdrawalMessageType.MISSING_IRA_PROVIDER_WARNING,
                        ErrorCodes.IRA_PROVIDER_MISSING);

        // Initial messages
        map.put(WithdrawalMessageType.PARTICIPANT_HAS_ANOTHER_WITHDRAWAL,
                ErrorCodes.PARTICIPANT_HAS_ANOTHER_WITHDRAWAL);
        map.put(WithdrawalMessageType.EXPIRATION_DATE_WITHIN_WARNING_THRESHOLD,
                ErrorCodes.EXPIRATION_DATE_WITHIN_WARNING_THRESHOLD);
        map.put(WithdrawalMessageType.PARTICIPANT_HAS_PBA_MONEY,
                ErrorCodes.PARTICIPANT_HAS_PBA_MONEY);
        map.put(WithdrawalMessageType.PARTICIPANT_HAS_ROTH_MONEY,
                ErrorCodes.PARTICIPANT_HAS_ROTH_MONEY);
        map.put(WithdrawalMessageType.INVALID_PARTICIPANT_ADDRESS_COUNTRY,
                ErrorCodes.INVALID_PARTICIPANT_ADDRESS_COUNTRY);
        map.put(WithdrawalMessageType.INVALID_TRUSTEE_ADDRESS_COUNTRY,
                ErrorCodes.INVALID_TRUSTEE_ADDRESS_COUNTRY);
        map.put(WithdrawalMessageType.PARTICIPANT_HAS_ZERO_ACCOUNT_BALANCE,
                ErrorCodes.PARTICIPANT_HAS_ZERO_ACCOUNT_BALANCE);
        map.put(WithdrawalMessageType.PARTICIPANT_HAS_I_LOANS, ErrorCodes.PARTICIPANT_HAS_I_LOANS);
        map.put(WithdrawalMessageType.PARTICIPANT_HAS_ONLINE_LOANS, ErrorCodes.PARTICIPANT_HAS_ONLINE_LOANS);
        map.put(WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ALERT,
                ErrorCodes.REQUEST_CAN_NOT_BE_PROCESSED);
        map.put(WithdrawalMessageType.REQUEST_CAN_NOT_BE_PROCESSED_ERROR,
                ErrorCodes.REQUEST_CAN_NOT_BE_PROCESSED);
        map.put(WithdrawalMessageType.STATE_TAX_CHANGED, ErrorCodes.STATE_TAX_CHANGED);
        map.put(WithdrawalMessageType.VESTING_SERVICE_HAS_ALREADY_CALLED_INDICATOR_NO,
                ErrorCodes.VESTING_SERVICE_CALLED_INDICATOR_NO);
        map.put(WithdrawalMessageType.NOTE_PARTICIPANT_INVALID_FOR_POW,
                ErrorCodes.NOTE_TO_PARTICIPANT_INVALID_POW);
        map.put(WithdrawalMessageType.TWO_STEP_REQUEST_APPROVAL,
                ErrorCodes.TWO_STEP_REQUEST_APPROVAL);
        map.put(WithdrawalMessageType.DECLARATION_AT_RISK_INDICATOR_ERROR,
                ErrorCodes.AT_RISK_REQUEST_DECLARATION_ERROR);
        // Security Enhancements - remove validation on credit party name length
/*      map.put(WithdrawalMessageType.FI_CREDIT_PARTY_NAME_LENGTH_ACH,
        		ErrorCodes.FI_CREDIT_PARTY_NAME_LENGTH_CHECK_ACH);
        map.put(WithdrawalMessageType.FI_CREDIT_PARTY_NAME_LENGTH_WIRE,
        		ErrorCodes.FI_CREDIT_PARTY_NAME_LENGTH_CHECK_WIRE);  
*/
        map.put(WithdrawalMessageType.PARTICPANT_APPLICABLE_TO_LIA,
        		ErrorCodes.ERROR_PARTICPANT_APPLICABLE_TO_LIA);
        map.put(WithdrawalMessageType.TERMINATION_DATE_EXCEEDED,
        		ErrorCodes.ERROR_TERMINATION_DATE_EXCEEDED);
        map.put(WithdrawalMessageType.RETIREMENT_DATE_EXCEEDED,
        		ErrorCodes.ERROR_RETIREMENT_DATE_EXCEEDED);
        map.put(WithdrawalMessageType.WD_STATE_TAX_INVALID_FOR_PR_STATE_ROLLOVER,
        		ErrorCodes.WD_STATE_TAX_INVALID_FOR_PR_STATE_ROLLOVER);
        map.put(WithdrawalMessageType.WD_STATE_TAX_INVALID_FOR_PR_STATE_NONROLLOVER,
        		ErrorCodes.WD_STATE_TAX_INVALID_FOR_PR_STATE_NONROLLOVER);
        
        // Security Enhancements - add validation on organization name length
        map.put(WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_ACH,
                ErrorCodes.ORGANIZATION_NAME_LENGTH_CHECK_ACH);
        map.put(WithdrawalMessageType.ORGANIZATION_NAME_LENGTH_WIRE,
                ErrorCodes.ORGANIZATION_NAME_LENGTH_CHECK_WIRE);
        map.put(WithdrawalMessageType.ROLLOVER_TYPE_MUST_BE_ROTH_IRA,
        		ErrorCodes.ERROR_ROLLOVER_TYPE_MUST_BE_ROTH_IRA);
        map.put(WithdrawalMessageType.MISSING_ROLLOVER_TYPE,
        		ErrorCodes.ERROR_MISSING_ROLLOVER_TYPE);
        // multidestination
        map.put(WithdrawalMessageType.ROLLOVER_REMAINING_BALANCE_MANDATORY,
                ErrorCodes.ROLLOVER_REMAINING_BALANCE_MANDATORY);
        map.put(WithdrawalMessageType.SELECTED_ONE_PAYEE,
                ErrorCodes.SELECTED_ONE_PAYEE);
        map.put(WithdrawalMessageType.MISSING_MULTIPLE_DESTINATION_SELECTION,
                ErrorCodes.MISSING_MULTIPLE_DESTINATION_SELECTION);
        map.put(WithdrawalMessageType.PAY_DIRECT_TO_ME_OPTIONLA_SEC_USER_INPUT,
                ErrorCodes.PAY_DIRECT_TO_ME_OPTIONLA_SEC_USER_INPUT);
        map.put(WithdrawalMessageType.INVALID_MONEY_TYPE, ErrorCodes.HARDSHIP_INVALID_MONEY_TYPE);
        map.put(WithdrawalMessageType.MAXIMUM_HARDSHIP_AMOUNT, ErrorCodes.MAXIMUM_HARDSHIP_AMOUNT);
        map.put(WithdrawalMessageType.MINIMUM_HARDSHIP_AMOUNT, ErrorCodes.MINIMUM_HARDSHIP_AMOUNT);
        map.put(WithdrawalMessageType.MAX_HARDSHIP_AMOUNT, ErrorCodes.MAXIMUM_HARDSHIP_AMOUNT);
        map.put(WithdrawalMessageType.MIN_HARDSHIP_AMOUNT, ErrorCodes.MINIMUM_HARDSHIP_AMOUNT);
        map.put(WithdrawalMessageType.AVAILABLE_HARDSHIP_AMOUNT, ErrorCodes.AVAILABLE_HARDSHIP_AMOUNT);
        map.put(WithdrawalMessageType.DISABILITY_DATE_EXCEEDED,ErrorCodes.DISABILITY_DATE_EXCEEDED);
        return map;
    }

    /**
     * Converts from the general {@link MessageCategory} to the {@link Type} in
     * {@link ValidationError}.
     * 
     * @param withdrawalMessage The withdrawal message to get the type for.
     * @return ValidationError.Type - The type this message correlates to.
     */
    public static ValidationError.Type getValidationTypeFromMessage(
            final WithdrawalMessage withdrawalMessage) {
        switch (withdrawalMessage.getWithdrawalMessageType().getMessageCategory()) {
            case ERROR:
                return ValidationError.Type.error;
            case WARNING:
                return ValidationError.Type.warning;
            case ALERT:
                return ValidationError.Type.alert;
            default:
                throw new NotImplementedException("Missing Mapping for MessageCategory: ["
                        + withdrawalMessage.getWithdrawalMessageType().getMessageCategory() + "]");
        } // end case
    }

    /**
     * This method maps the biz tier message type (found in {@link WithdrawalMessageType}) to a web
     * tier error code (found in {@link ErrorCodes}). This method throws a {@link RuntimeException}
     * if the mapping is not found.
     * 
     * @param withdrawalMessage The message to map.
     * @return int The error code for the given error.
     */
    public static int getErrorCodeFromMessage(final WithdrawalMessage withdrawalMessage) {

        withdrawalMessage.getWithdrawalMessageType();
        final Integer errorCode = getWithdrawalMessageToErrorCodeMap().get(
                withdrawalMessage.getWithdrawalMessageType());

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer().append("\n\tMapping for [").append(
                    withdrawalMessage.getWithdrawalMessageType()).append("] is [")
                    .append(errorCode).append("] - for properties: [").append(
                            withdrawalMessage.getPropertyNames()).append("]").toString());
        } // fi

        if (errorCode == null) {
            throw new NotImplementedException("Unable to deal with MessageType: "
                    + withdrawalMessage.getWithdrawalMessageType().toString());
        } else {
            return errorCode;
        } // fi
    }
}
