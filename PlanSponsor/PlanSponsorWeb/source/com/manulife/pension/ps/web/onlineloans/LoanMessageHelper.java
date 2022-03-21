package com.manulife.pension.ps.web.onlineloans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.manulife.pension.service.loan.LoanError;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.validator.ValidationError;

public class LoanMessageHelper {

	private static final Map<LoanErrorCode, Integer> validationErrors = new HashMap<LoanErrorCode, Integer>();
	private static final Map<LoanErrorCode, Integer> initialMessages = new HashMap<LoanErrorCode, Integer>();
	private static final Map<LoanErrorCode, Integer> initializationMessages = new HashMap<LoanErrorCode, Integer>();

	static {
		initializationMessages.put(LoanErrorCode.LRK01_IS_OFF, 2604);
		initializationMessages.put(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF,
				2605);
		initializationMessages
				.put(LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO,
						2607);
		initializationMessages
        .put(LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL,
        3630);
		initializationMessages.put(
				LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE, 2608);
		initializationMessages.put(
				LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE, 2609);
		initializationMessages.put(
				LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED, 2610);
		initializationMessages.put(
				LoanErrorCode.PARTICIPANT_DRAFT_LOAN_REQUEST_EXISTS, 2611);
		initializationMessages.put(
				LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST, 2612);
		initializationMessages.put(
				LoanErrorCode.FORWARD_UNREVERSED_LOAN_TRANSACTION_EXISTS, 2613);
		initializationMessages.put(
				LoanErrorCode.LIA_ENABLED_FOR_PARTICIPANT, 3032);
	}
	static {
		initialMessages.put(LoanErrorCode.LRK01_IS_OFF, 2614);
		initialMessages
        .put(LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL,
        3630);
		initialMessages
        .put(LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO,
        2617);
		initialMessages.put(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF, 2615);	
		initialMessages.put(
				LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_PARTICIPANT_INITIATED,
				2616);
		initialMessages.put(
				LoanErrorCode.PARTICIPANT_HAS_GIFL_FOR_EXTERNAL_USER_INITIATED,
				2702);
		initialMessages.put
		        (LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE,
		        2719);
        initialMessages
                .put(LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO,
                2617);
		initialMessages.put(LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST,
				2618);
		initialMessages.put(LoanErrorCode.PARTICIPANT_HAS_WITHDRAWAL_REQUEST,
				2619);
		initialMessages.put(LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE,
				2620);
		initialMessages.put(
				LoanErrorCode.CONTRACT_STATUS_NOT_ACTIVE_NOR_FROZEN, 2620);
		initialMessages.put(LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE,
				2620);
		initialMessages.put(LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED,
				2620);
		initialMessages.put(LoanErrorCode.LOAN_ABOUT_TO_EXPIRE, 2621);
		initialMessages.put(
				LoanErrorCode.FORWARD_UNREVERSED_LOAN_TRANSACTION_EXISTS, 2622);
		initialMessages.put(
				LoanErrorCode.VESTING_CREDITING_METHOD_IS_UNSPECIFIED, 2545);
		initialMessages.put(LoanErrorCode.VESTING_MISSING_EMPLOYEE_DATA, 2547);
		initialMessages.put(LoanErrorCode.VESTING_SCHEDULE_HAS_NOT_BEEN_SET_UP,
				2546);
		initialMessages
				.put(
						LoanErrorCode.VESTING_MORE_RECENT_DATA_USED_FOR_CALCULATION_CODE,
						2560);
		initialMessages.put(
				LoanErrorCode.VESTING_SERVICE_OTHER_NON_CRITICAL_ERROR, 2524);
		initialMessages.put(LoanErrorCode.VESTING_SERVICE_CRITICAL_ERROR, 2525);
		initialMessages.put(
				LoanErrorCode.REQUESTED_AMOUNT_EXCEEDS_AVAILABLE_AMOUNT, 2623);
	}

	static {
		validationErrors.put(
				LoanErrorCode.LOAN_ISSUE_FEE_GREATER_THAN_PLAN_MINIMUM, 1269);
		validationErrors.put(
				LoanErrorCode.CONTRACT_STATUS_IS_FROZEN_FOR_APPROVAL, 2689);
		validationErrors.put(
				LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE_FOR_APPROVAL,
				2690);
		validationErrors.put(
				LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST, 2696);
		validationErrors.put(
				LoanErrorCode.FORWARD_UNREVERSED_LOAN_TRANSACTION_EXISTS, 2697);
		validationErrors.put(LoanErrorCode.CONCURRENT_UPDATE_DETECTED, 2698);
		validationErrors.put(LoanErrorCode.LRK01_IS_OFF, 2699);
		validationErrors.put(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF, 2700);
		validationErrors.put(LoanErrorCode.MISSING_LEGALLY_MARRIED_IND, 2624);
		validationErrors.put(LoanErrorCode.MISSING_LOAN_TYPE, 54314);
		validationErrors.put(LoanErrorCode.LOAN_REASON_TOO_LONG, 2701);
		validationErrors.put(LoanErrorCode.LOAN_REASON_INVALID_CHARACTER, 2625);
		validationErrors.put(
				LoanErrorCode.MUST_ENTER_NOTE_TO_PARTICIPANT_ON_DENY, 2686);
		validationErrors.put(LoanErrorCode.NOTE_TO_PARTICIPANT_TOO_LONG, 2684);
		validationErrors
				.put(LoanErrorCode.NOTE_TO_ADMINISTRATOR_TOO_LONG, 2687);
        validationErrors.put(LoanErrorCode.NOTE_TO_PARTICIPANT_INVALID_CHARACTERS, 2685);
        validationErrors
                .put(LoanErrorCode.NOTE_TO_ADMINISTRATOR_INVALID_CHARACTERS, 2688);
		validationErrors.put(LoanErrorCode.MUST_ENTER_LOAN_REASON, 2626);
		validationErrors.put(LoanErrorCode.MISSING_EXPIRATION_DATE, 2474);
		validationErrors.put(
				LoanErrorCode.EXPIRATION_DATE_LESS_THAN_MININUM_ALLOWED, 2629);
		validationErrors.put(
				LoanErrorCode.EXPIRATION_DATE_GREATER_THAN_MAXIMUM_ALLOWED,
				3051);
		validationErrors
				.put(LoanErrorCode.EXPIRATION_DATE_INVALID_FORMAT, 2627);
		validationErrors.put(LoanErrorCode.EXPIRATION_DATE_INVALID_VALUE, 2628);

		validationErrors.put(
				LoanErrorCode.PAYROLL_DATE_LESS_THAN_EQUAL_TO_ESTIMATED_LOAN_START_DATE,
				2631);
		validationErrors.put(LoanErrorCode.MISSING_PAYROLL_DATE, 2632);
		validationErrors.put(
				LoanErrorCode.PAYROLL_DATE_LESS_THAN_MININUM_ALLOWED, 2633);
        validationErrors.put(
                LoanErrorCode.PAYROLL_DATE_GREATER_THAN_MAXIMUM_ALLOWED, 1270);
		validationErrors.put(
				LoanErrorCode.PAYROLL_DATE_LESS_THAN_EQUAL_TO_NEXT_BUSINESS_DATE, 2634);
		validationErrors.put(LoanErrorCode.PAYROLL_DATE_INVALID_FORMAT, 2627);
		validationErrors
				.put(
						LoanErrorCode.PAYROLL_DATE_INVALID_FOR_SEMI_MONTHLY_PAYMENT_FREQUENCY,
						1411);
		validationErrors.put(LoanErrorCode.PAYROLL_DATE_INVALID_VALUE, 2628);
		validationErrors.put(LoanErrorCode.DEFAULT_PROVISION_TOO_LONG, 2638);
		validationErrors.put(LoanErrorCode.DEFAULT_PROVISION_INVALID_CHARACTER,
				2639);
		validationErrors.put(LoanErrorCode.MISSING_DEFAULT_PROVISION, 2640);

		validationErrors
				.put(LoanErrorCode.VESTING_PERCENTAGE_NON_NUMERIC, 2641);
		validationErrors.put(LoanErrorCode.VESTING_PERCENTAGE_INVALID_FORMAT,
				2642);
		validationErrors.put(LoanErrorCode.VESTING_PERCENTAGE_TOO_HIGH, 2643);
		validationErrors
				.put(
						LoanErrorCode.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_NON_NUMERIC,
						2644);
		validationErrors.put(
				LoanErrorCode.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_TOO_LOW,
				2646);
		validationErrors
				.put(
						LoanErrorCode.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS_INVALID_FORMAT,
						2645);
		validationErrors.put(LoanErrorCode.OUTSTANDING_LOANS_COUNT_NON_NUMERIC,
				2647);
		validationErrors.put(LoanErrorCode.OUTSTANDING_LOANS_COUNT_TOO_HIGH,
				2649);
		validationErrors.put(LoanErrorCode.OUTSTANDING_LOANS_COUNT_TOO_LOW,
				2648);
		validationErrors.put(
				LoanErrorCode.CURRENT_OUTSTANDING_LOAN_BALANCE_NON_NUMERIC,
				2650);
		validationErrors.put(
				LoanErrorCode.CURRENT_OUTSTANDING_LOAN_BALANCE_INVALID_FORMAT,
				2651);
		validationErrors.put(
				LoanErrorCode.CURRENT_OUTSTANDING_LOAN_BALANCE_TOO_LOW, 2652);
		validationErrors
				.put(LoanErrorCode.TPA_LOAN_ISSUE_FEE_NON_NUMERIC, 2635);
		validationErrors.put(LoanErrorCode.TPA_LOAN_ISSUE_FEE_INVALID_FORMAT,
				2636);
		validationErrors.put(LoanErrorCode.TPA_LOAN_ISSUE_FEE_EXCEEDS_MAXIMUM,
				2460);
		validationErrors
				.put(LoanErrorCode.TPA_LOAN_ISSUE_FEE_MAY_BE_PAYABLE_BY_PARTICIPANT, 2637);
		validationErrors.put(LoanErrorCode.LOAN_AMOUNT_BLANK_OR_NON_NUMERIC,
				2654);
		validationErrors.put(LoanErrorCode.LOAN_AMOUNT_INVALID_FORMAT, 1259);
		validationErrors.put(LoanErrorCode.LOAN_AMOUNT_LESS_THAN_MINIMUM, 2655);
		validationErrors.put(LoanErrorCode.LOAN_AMOUNT_GREATER_THAN_MAXIMUM,
				2656);
		validationErrors.put(LoanErrorCode.AMORTIZATION_MONTHS_IS_ZERO, 2657);
		validationErrors.put(LoanErrorCode.AMORTIZATION_PERIOD_TOO_HIGH, 2659);

		validationErrors.put(LoanErrorCode.PAYMENT_FREQUENCY_IS_EMPTY, 2660);
		validationErrors.put(
				LoanErrorCode.PAYMENT_FREQUENCY_NOT_MATCHING_PLAN_VALUE, 2661);
		validationErrors.put(LoanErrorCode.INTEREST_RATE_BLANK_OR_NON_NUMERIC,
				2662);
		validationErrors.put(LoanErrorCode.INTEREST_RATE_INVALID_FORMAT, 2663);
        validationErrors.put(LoanErrorCode.INTEREST_RATE_OUT_OF_RANGE, 1260);

		validationErrors.put(LoanErrorCode.ADDRESS_LINE1_INVALID_CHARACTER,
				2664);
		// LRA.331
		validationErrors.put(LoanErrorCode.ADDRESS_LINE1_REQUIRED, 2665);
		// LRA.338, 339
		validationErrors.put(LoanErrorCode.ADDRESS_LINE2_INVALID_CHARACTER,
				2666);
		// LRA.347, 348
		validationErrors.put(LoanErrorCode.CITY_INVALID_CHARACTER, 2667);
		// LRA.349
		validationErrors.put(LoanErrorCode.CITY_REQUIRED, 2668);
		// LRA.363, 364
		validationErrors.put(LoanErrorCode.STATE_INVALID_CHARACTER, 2669);
		// LRA.365
		validationErrors.put(LoanErrorCode.STATE_REQUIRED, 2670);
		// LRA.374
		validationErrors.put(LoanErrorCode.COUNTRY_REQUIRED, 2671);
		// LRA.382
		validationErrors.put(LoanErrorCode.ZIP_CODE_INVALID_US_FORMAT, 2672);
		// LRA.383
		validationErrors.put(LoanErrorCode.ZIP_CODE_INVALID_RANGE, 2673);
		// LRA.384
		validationErrors.put(LoanErrorCode.ZIP_CODE_REQUIRED, 2674);
		// LRA.401, 402
		validationErrors.put(LoanErrorCode.ZIP_CODE_INVALID_CHARACTER, 2675);
		// LRA.410
		validationErrors
				.put(LoanErrorCode.ABA_ROUTING_NUMBER_NON_NUMERIC, 2676);
		// LRA.421
		validationErrors.put(LoanErrorCode.ACCOUNT_NUMBER_INVALID_CHARACTER,
				2678);
		// LRA.422
		validationErrors.put(LoanErrorCode.ACCOUNT_NUMBER_REQUIRED, 2679);
		// LRA.429
		validationErrors.put(LoanErrorCode.ACCOUNT_TYPE_REQUIRED, 2680);
		// LRA.416
		validationErrors.put(LoanErrorCode.BANK_NAME_REQUIRED, 2677);

		validationErrors.put(
				LoanErrorCode.TRUTH_IN_LENDING_NOTICE_NOT_ACCEPTED, 2681);
		validationErrors.put(LoanErrorCode.PROMISSORY_NOTE_NOT_ACCEPTED, 2682);
		validationErrors.put(LoanErrorCode.AT_RISK_TRANSACTION_NOT_ACCEPTED,
				2683);
		validationErrors.put(
				LoanErrorCode.LIA_ENABLED_FOR_PARTICIPANT, 3032);
	}

	public static Set<ValidationError> toValidationError(
			List<? extends LoanMessage> loanMessages,
			boolean showInitializationMessage, boolean showInitialMessage) {
		Set<ValidationError> errors = new HashSet<ValidationError>();
		for (LoanMessage loanMessage : loanMessages) {
			String[] fieldNames = (String[]) loanMessage.getFieldNames()
					.toArray(new String[loanMessage.getFieldNames().size()]);
			Object[] params = loanMessage.getParams().toArray();
			ValidationError.Type type = ValidationError.Type.alert;
			if (loanMessage.getType() == LoanError.Type.warning) {
				type = ValidationError.Type.warning;
			} else if (loanMessage.getType() == LoanError.Type.error) {
				type = ValidationError.Type.error;
			}

			Integer messageCode = null;
			if (showInitializationMessage) {
				messageCode = initializationMessages.get(loanMessage
						.getErrorCode());
			} else if (showInitialMessage) {
				messageCode = initialMessages.get(loanMessage.getErrorCode());
				// For initial message, override the type to alert so that no
				// icon is displayed next to the message.
				type = ValidationError.Type.alert;
			} else {
				messageCode = validationErrors.get(loanMessage.getErrorCode());
			}

			if (messageCode != null) {
				/*
				 * If duplicate error/warning code but for a different 
				 * fieldName, save only one instance of the error code 
				 * but indicate it refers to the different fields, so each
				 * field gets flagged with the error/warning icon.
				 */
				boolean found = false;
				for (ValidationError error : errors) {
                    if (error.getErrorCode() == messageCode) {
					    found = true;
					    if (fieldNames != null && fieldNames.length > 0) {
					        boolean fieldNameFound;

                            for (String fieldName : fieldNames) {
                                fieldNameFound = false;
                                for (String fieldId : (List<String>) error.getFieldIds()) {
                                    if (fieldId.equals(fieldName)) {
                                        fieldNameFound = true;
                                    }
                                }
                                if (!fieldNameFound) {
                                    error.getFieldIds().add(fieldName);
                                }
                            }
					    }
					}
				}
				if (!found) {
					ValidationError validationError = new ValidationError(
							fieldNames, messageCode, params, type);
					errors.add(validationError);
				}
			}
		}
		return errors;
	}

}
