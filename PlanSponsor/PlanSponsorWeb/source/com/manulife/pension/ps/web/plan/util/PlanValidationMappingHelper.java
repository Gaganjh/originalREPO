package com.manulife.pension.ps.web.plan.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.manulife.pension.common.MessageCategory;
import com.manulife.pension.ps.web.ErrorCodes;
import com.manulife.pension.ps.web.contract.PlanDataUi;
import com.manulife.pension.service.contract.common.PlanMessage;
import com.manulife.pension.service.contract.common.PlanMessageType;
import com.manulife.pension.validator.ValidationError;

/**
 * PlanValidationMappingHelper is used to hold the plan validation mappings between the business and
 * web tiers.
 * 
 * @author dickand
 */
public final class PlanValidationMappingHelper {

    public static final Logger logger = Logger.getLogger(PlanValidationMappingHelper.class);

    /**
     * The constructor is private, as this helper class should not be instantiated.
     */
    private PlanValidationMappingHelper() {
        super();
    }

    private static Map<String, Integer> messageToErrorCodeMap;

    private static Map<String, String> messageToGroupMap;

    /**
     * getPlanMessageToErrorCodeMap gets the static mapping of {@link PlanMessageType} to
     * {@link ErrorCodes}.
     * 
     * @return Map - The map of message types (Biz) to error codes (UI).
     */
    private static Map<String, Integer> getPlanMessageToErrorCodeMap() {

        if (messageToErrorCodeMap != null) {
            return messageToErrorCodeMap;
        }

        // Need to use strings because the enumerated types do not serialize properly
        final Map<String, Integer> map = new HashMap<String, Integer>();
        messageToErrorCodeMap = map;

        map.put(PlanMessageType.PLAN_NAME_NOT_ENTERED.toString(), ErrorCodes.PLAN_NAME_NOT_ENTERED);
        map.put(PlanMessageType.INVALID_PLAN_NAME.toString(), ErrorCodes.INVALID_PLAN_NAME);
        map.put(PlanMessageType.EMPLOYER_TAX_IDENTIFICATION_NUMBER_NOT_ENTERED.toString(),
                ErrorCodes.EMPLOYER_TAX_IDENTIFICATION_NUMBER_NOT_ENTERED);
        map.put(PlanMessageType.PLAN_NUMBER_NOT_ENTERED.toString(),
                ErrorCodes.PLAN_NUMBER_NOT_ENTERED);
        map.put(PlanMessageType.INVALID_EMPLOYER_TAX_IDENTIFICATION_NUMBER.toString(),
                ErrorCodes.INVALID_EMPLOYER_TAX_IDENTIFICATION_NUMBER);
        map.put(PlanMessageType.NO_EMPLOYER_TAX_IDENTIFICATION_NUMBER_WITH_PLAN_NUMBER.toString(),
                ErrorCodes.NO_EMPLOYER_TAX_IDENTIFICATION_NUMBER_WITH_PLAN_NUMBER);
        map.put(PlanMessageType.INVALID_PLAN_NUMBER.toString(), ErrorCodes.INVALID_PLAN_NUMBER);
        map.put(PlanMessageType.NO_PLAN_NUMBER_WITH_EMPLOYER_TAX_IDENTIFICATION_NUMBER.toString(),
                ErrorCodes.NO_PLAN_NUMBER_WITH_EMPLOYER_TAX_IDENTIFICATION_NUMBER);
        map.put(PlanMessageType.EMPLOYER_TAX_IDENTIFICATION_NUMBER_PLAN_NUMBER_NOT_UNIQUE
                .toString(), ErrorCodes.EMPLOYER_TAX_IDENTIFICATION_NUMBER_PLAN_NUMBER_NOT_UNIQUE);
        map.put(PlanMessageType.PLAN_EFFECTIVE_DATE_AFTER_CONTRACT_EFFECTIVE_DATE.toString(),
                ErrorCodes.PLAN_EFFECTIVE_DATE_AFTER_CONTRACT_EFFECTIVE_DATE);
        //map.put(PlanMessageType.PLAN_EFFECTIVE_DATE_AFTER_EMPLOYEE_EFFECTIVE_DATE.toString(),
       //         ErrorCodes.PLAN_EFFECTIVE_DATE_AFTER_EMPLOYEE_EFFECTIVE_DATE);
              map.put(PlanMessageType.PLAN_EFFECTIVE_DATE_NOT_ENTERED.toString(),
                ErrorCodes.PLAN_EFFECTIVE_DATE_NOT_ENTERED);
        map.put(PlanMessageType.INVALID_PLAN_EFFECTIVE_DATE.toString(),
                ErrorCodes.INVALID_PLAN_EFFECTIVE_DATE);
        map.put(PlanMessageType.INVALID_ENTITY_TYPE_OTHER_DESCRIPTION.toString(),
                ErrorCodes.INVALID_ENTITY_TYPE_OTHER_DESCRIPTION);
        map.put(PlanMessageType.MISSING_ENTITY_TYPE_OTHER_DESCRIPTION.toString(),
                ErrorCodes.MISSING_ENTITY_TYPE_OTHER_DESCRIPTION);
        map.put(PlanMessageType.INVALID_FIRST_PLAN_ENTRY_DATE.toString(),
                ErrorCodes.INVALID_FIRST_PLAN_ENTRY_DATE); 
        map.put(PlanMessageType.EXISTING_PLAN_ENTRY_DATE_REMOVED.toString(),
                ErrorCodes.EXISTING_PLAN_ENTRY_DATE_REMOVED);
        map.put(PlanMessageType.INVALID_NORMAL_RETIREMENT_AGE.toString(),
                ErrorCodes.INVALID_NORMAL_RETIREMENT_AGE);
        map.put(PlanMessageType.INVALID_EARLY_RETIREMENT_AGE.toString(),
                ErrorCodes.INVALID_EARLY_RETIREMENT_AGE);
        map.put(PlanMessageType.INVALID_MINIMUM_HARDSHIP_AMOUNT.toString(),
                ErrorCodes.INVALID_MINIMUM_HARDSHIP_AMOUNT);
        map.put(PlanMessageType.EEDEF_EARNINGS_FLAG_CHANGES_WARNING.toString(),
                ErrorCodes.EEDEF_EARNINGS_FLAG_CHANGES_WARNING);
        map.put(PlanMessageType.INVALID_MAXIMUM_HARDSHIP_AMOUNT.toString(),
                ErrorCodes.INVALID_MAXIMUM_HARDSHIP_AMOUNT);
        map.put(PlanMessageType.MINIMUM_HARDSHIP_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT.toString(),
                ErrorCodes.MINIMUM_HARDSHIP_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT);
        map.put(PlanMessageType.INVALID_INSTALMENT_STATUS_CHANGE_ERROR.toString(),
                ErrorCodes.INVALID_INSTALMENT_STATUS_CHANGE_ERROR);
        map.put(PlanMessageType.INVALID_APOLLO_INSTALMENT_STATUS_CHANGE_ERROR.toString(),
                ErrorCodes.INVALID_APOLLO_INSTALMENT_STATUS_CHANGE_ERROR);
        map.put(PlanMessageType.MAX_OUTSTANDING_LOANS_NOT_ENTERED.toString(),
                ErrorCodes.MAX_OUTSTANDING_LOANS_NOT_ENTERED);
        map.put(PlanMessageType.INVALID_MINIMUM_LOAN_AMOUNT.toString(),
                ErrorCodes.INVALID_MINIMUM_LOAN_AMOUNT);
        map.put(PlanMessageType.MINIMUM_LOAN_AMOUNT_LESS_THAN_MINIMUM_BOUND.toString(),
                ErrorCodes.MINIMUM_LOAN_AMOUNT_LESS_THAN_MINIMUM_BOUND);
        map.put(PlanMessageType.INVALID_MAXIMUM_LOAN_AMOUNT.toString(),
                ErrorCodes.INVALID_MAXIMUM_LOAN_AMOUNT);
        map.put(PlanMessageType.MAXIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_BOUND.toString(),
                ErrorCodes.MAXIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_BOUND);
        map.put(PlanMessageType.MINIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT.toString(),
                ErrorCodes.MINIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT);
        map.put(PlanMessageType.INVALID_MAXIMUM_LOAN_PERCENT.toString(),
                ErrorCodes.INVALID_MAXIMUM_LOAN_PERCENT);
        map.put(PlanMessageType.MAXIMUM_LOAN_PERCENT_GREATER_THAN_MAXIMUM.toString(),
                ErrorCodes.MAXIMUM_LOAN_PERCENT_GREATER_THAN_MAXIMUM);
        map.put(PlanMessageType.INVALID_LOAN_INTEREST_RATE_ABOVE_PRIME.toString(),
                ErrorCodes.INVALID_LOAN_INTEREST_RATE_ABOVE_PRIME);
        map.put(PlanMessageType.LOAN_INTEREST_RATE_ABOVE_PRIME_NOT_IN_RANGE.toString(),
                ErrorCodes.LOAN_INTEREST_RATE_ABOVE_PRIME_NOT_IN_RANGE);
        map.put(PlanMessageType.GENERAL_PURPOSE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE.toString(),
                ErrorCodes.GENERAL_PURPOSE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE);
        map.put(PlanMessageType.HARDSHIP_MAXIMUM_AMORTIZATION_NOT_IN_RANGE.toString(),
                ErrorCodes.HARDSHIP_MAXIMUM_AMORTIZATION_NOT_IN_RANGE);
        map.put(PlanMessageType.PRIMARY_RESIDENCE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE.toString(),
                ErrorCodes.PRIMARY_RESIDENCE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE);
        map.put(PlanMessageType.NO_HOURS_OF_SERVICE_WITH_HOURS_OF_SERVICE_CREDIT_METHOD.toString(),
                ErrorCodes.NO_HOURS_OF_SERVICE_WITH_HOURS_OF_SERVICE_CREDIT_METHOD);
        map.put(PlanMessageType.INVALID_HOURS_OF_SERVICE.toString(),
                ErrorCodes.INVALID_HOURS_OF_SERVICE);
        map.put(PlanMessageType.HOURS_OF_SERVICE_NOT_IN_RANGE.toString(),
                ErrorCodes.HOURS_OF_SERVICE_NOT_IN_RANGE);
        map.put(PlanMessageType.VESTING_PERCENTAGE_NOT_ENTERED.toString(),
                ErrorCodes.VESTING_PERCENTAGE_NOT_ENTERED);
        map.put(PlanMessageType.INVALID_VESTING_PERCENTAGE.toString(),
                ErrorCodes.INVALID_VESTING_PERCENTAGE);
        map.put(PlanMessageType.VESTING_PERCENTAGE_NOT_IN_RANGE.toString(),
                ErrorCodes.VESTING_PERCENTAGE_NOT_IN_RANGE);
        map.put(PlanMessageType.VESTING_PERCENT_LESS_THAN_PREVIOUS_YEAR.toString(),
                ErrorCodes.VESTING_PERCENT_LESS_THAN_PREVIOUS_YEAR);
        map.put(PlanMessageType.VESTING_PERCENT_CAN_NOT_BE_HUNDRAD_PERCENT_FOR_YEAR_ZERO.toString(),
                ErrorCodes.VESTING_PERCENT_CAN_NOT_BE_HUNDRAD_PERCENT_FOR_YEAR_ZERO);
        map.put(PlanMessageType.NO_VESTING_INFORMATION_WHEN_CSF_CALCULATE_VESTING_YES.toString(),
                ErrorCodes.NO_VESTING_INFORMATION_WHEN_CSF_CALCULATE_VESTING_YES);
        
        // added for eligibility calculation
        map.put(PlanMessageType.EC_MIN_AGE_NOT_BETWEEEN_EC_EE_AGE_LOWER_LIMIT_AND_UPPER_LIMIT.toString(),
        		ErrorCodes.EC_MIN_AGE_NOT_BETWEEEN_EC_EE_AGE_LOWER_LIMIT_AND_UPPER_LIMIT);
        map.put(PlanMessageType.EC_MIN_AGE_NOT_BETWEEEN_EC_ER_AGE_LOWER_LIMIT_AND_UPPER_LIMIT.toString(),
        		ErrorCodes.EC_MIN_AGE_NOT_BETWEEEN_EC_ER_AGE_LOWER_LIMIT_AND_UPPER_LIMIT);
        map.put(PlanMessageType.PLAN_YEAR_END_REQUIRED_TO_SELECT_HOS.toString(),
        		ErrorCodes.PLAN_YEAR_END_REQUIRED_TO_SELECT_HOS);        
        map.put(PlanMessageType.EC_HOS_GREATER_THAN_EE_HOS_UPPER_LIMIT.toString(),
        		ErrorCodes.EC_HOS_GREATER_THAN_EE_HOS_UPPER_LIMIT);
        map.put(PlanMessageType.EC_HOS_GREATER_THAN_ER_HOS_UPPER_LIMIT.toString(),
        		ErrorCodes.EC_HOS_GREATER_THAN_ER_HOS_UPPER_LIMIT);
        map.put(PlanMessageType.EC_POS_GREATER_THAN_EE_POS_UPPER_LIMIT.toString(),
        		ErrorCodes.EC_POS_GREATER_THAN_EE_POS_UPPER_LIMIT);
        map.put(PlanMessageType.EC_POS_GREATER_THAN_ER_POS_UPPER_LIMIT.toString(),
        		ErrorCodes.EC_POS_GREATER_THAN_ER_POS_UPPER_LIMIT);
        map.put(PlanMessageType.EC_POS_NOT_EQUAL_TO_MONTH_OR_DAYS.toString(),
        		ErrorCodes.EC_POS_NOT_EQUAL_TO_MONTH_OR_DAYS);
        map.put(PlanMessageType.EC_PLAN_ENTRY_FREQUENCY_NOT_SELECTED.toString(),
        		ErrorCodes.EC_PLAN_ENTRY_FREQUENCY_NOT_SELECTED);
        map.put(PlanMessageType.EC_PLAN_EFFECTIVE_DATE_NOT_ENTERED.toString(),
        		ErrorCodes.EC_PLAN_EFFECTIVE_DATE_NOT_ENTERED);
        map.put(PlanMessageType.EC_ELIG_REQ_CANT_BE_REMOVED_IF_EC_CSF_ON.toString(),
        		ErrorCodes.EC_ELIG_REQ_CANT_BE_REMOVED_IF_EC_CSF_ON);     
        map.put(PlanMessageType.EC_MULTIPLE_ELIG_RULES_FOR_SINGLE_MT_NOT_EQUAL_NO.toString(),
        		ErrorCodes.EC_MULTIPLE_ELIG_RULES_FOR_SINGLE_MT_NOT_EQUAL_NO);
        map.put(PlanMessageType.EC_ELIG_COMP_PERIOD_AFTER_INITIAL_PERIOD_NOT_VALID.toString(),
        		ErrorCodes.EC_ELIG_COMP_PERIOD_AFTER_INITIAL_PERIOD_NOT_VALID);
        map.put(PlanMessageType.EC_EMPLOYEE_PLAN_ENTRY_DATE_BASIS_CODE_NOT_VALID_FOR_EC_SERVICE.toString(), 
        		ErrorCodes.EC_EMPLOYEE_PLAN_ENTRY_DATE_BASIS_CODE_NOT_VALID_FOR_EC_SERVICE);
        map.put(PlanMessageType.EC_EMPLOYEE_PLAN_ENTRY_DATE_BASIS_CODE_NOT_VALID_FOR_EZSTART.toString(),
        		ErrorCodes.EC_EMPLOYEE_PLAN_ENTRY_DATE_BASIS_CODE_NOT_VALID_FOR_EZSTART);
        
        // added for onboarding
        map.put(PlanMessageType.FIRST_PLAN_ENTRY_DATE_REQUIRED.toString(),
                ErrorCodes.FIRST_PLAN_ENTRY_DATE_REQUIRED);        
        map.put(PlanMessageType.CANNOT_TURN_AUTO_ENROLLMENT_OFFERED_OFF_WHEN_AE_CSF_IS_ON.toString(),
                ErrorCodes.CANNOT_TURN_AUTO_ENROLLMENT_OFFERED_OFF_WHEN_AE_CSF_IS_ON);        
        map.put(PlanMessageType.PERIOD_OF_SERVICE_REQUIRED_FOR_ELAPSED_TIME_SERVICE_CREDITING_METHOD.toString(),
                ErrorCodes.PERIOD_OF_SERVICE_REQUIRED_FOR_ELAPSED_TIME_SERVICE_CREDITING_METHOD);        
        map.put(PlanMessageType.HOURS_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD.toString(),
                ErrorCodes.HOURS_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD);        
        map.put(PlanMessageType.PERIOD_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD.toString(),
                ErrorCodes.PERIOD_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD);        
        map.put(PlanMessageType.BASIS_FOR_PERIOD_OF_SERVICE_REQUIRED.toString(),
                ErrorCodes.BASIS_FOR_PERIOD_OF_SERVICE_REQUIRED);        
        map.put(PlanMessageType.INVALID_PLAN_FREQUENCY_FOR_EZSTART.toString(),
                ErrorCodes.INVALID_PLAN_FREQUENCY_FOR_EZSTART);        
        map.put(PlanMessageType.PLAN_ENTRY_FREQUENCY_REQUIRED_FOR_ELIGIBILITY_REQUIREMENT.toString(),
                ErrorCodes.PLAN_ENTRY_FREQUENCY_REQUIRED_FOR_ELIGIBILITY_REQUIREMENT);        
        map.put(PlanMessageType.FIRST_PLAN_ENTRY_DATE_OR_EEDEF_PLAN_ENTRY_FREQUENCY_CHANGED_WARNING.toString(),
                ErrorCodes.FIRST_PLAN_ENTRY_DATE_OR_EEDEF_PLAN_ENTRY_FREQUENCY_CHANGED_WARNING);
        map.put(PlanMessageType.AUTO_ENROLLMENT_MUST_BE_ON_WHEN_PLAN_INCLUDES_QACA.toString(),
                ErrorCodes.AUTO_ENROLLMENT_MUST_BE_ON_WHEN_PLAN_INCLUDES_QACA);
        map.put(PlanMessageType.AUTO_ENROLLMENT_MUST_NOT_BE_ON_WITH_MISSING_ELIGIBILITY_REQUIREMENTS_FOR_EEDEF.toString(),
                ErrorCodes.AUTO_ENROLLMENT_MUST_NOT_BE_ON_WITH_MISSING_ELIGIBILITY_REQUIREMENTS_FOR_EEDEF);
        map.put(PlanMessageType.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_REQUIRED.toString(),
                ErrorCodes.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_REQUIRED);
        map.put(PlanMessageType.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_LESS_THAN_CONTRIBUTION_DEFERRAL_MIN_PERCENT.toString(),
                ErrorCodes.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_LESS_THAN_CONTRIBUTION_DEFERRAL_MIN_PERCENT);
        map.put(PlanMessageType.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_GREATER_THAN_CONTRIBUTION_DEFERRAL_MAX_PERCENT.toString(),
                ErrorCodes.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_GREATER_THAN_CONTRIBUTION_DEFERRAL_MAX_PERCENT);
        map.put(PlanMessageType.INVALID_VESTNG_COMPUTATION_PERIOD_FOR_HOURS_OF_SERVICE.toString(),
                ErrorCodes.INVALID_VESTNG_COMPUTATION_PERIOD_FOR_HOURS_OF_SERVICE);        
        map.put(PlanMessageType.INVALID_VESTNG_COMPUTATION_PERIOD_FOR_ELAPSED_TIME.toString(),
                ErrorCodes.INVALID_VESTNG_COMPUTATION_PERIOD_FOR_ELAPSED_TIME);        
        map.put(PlanMessageType.INVALID_MULTIPLE_VESTING_SCHEDULES_FOR_ONE_SINGLE_MONEY_TYPE.toString(),
                ErrorCodes.INVALID_MULTIPLE_VESTING_SCHEDULES_FOR_ONE_SINGLE_MONEY_TYPE);        
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_REQUIRED.toString(),
                ErrorCodes.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_REQUIRED); 
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_NOT_BEGINNING_OF_PLAN_YEAR_WARNING.toString(),
                ErrorCodes.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_NOT_BEGINNING_OF_PLAN_YEAR_WARNING); 
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_AFTER_INITIAL_ENROLLMENT_DATE.toString(),
                ErrorCodes.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_AFTER_INITIAL_ENROLLMENT_DATE);
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_AUTOMATIC_WITHDRAWALS_REQUIRED.toString(),
        		ErrorCodes.AUTOMATIC_ENROLLMENT_AUTOMATIC_WITHDRAWALS_REQUIRED);
        map.put(PlanMessageType.QDIA_RESTRICTION_DETAILS_REQUIRED.toString(),
                ErrorCodes.QDIA_RESTRICTION_DETAILS_REQUIRED); 
        map.put(PlanMessageType.ACI_EFFECTIVE_DATE_REQUIRED.toString(), ErrorCodes.ACI_EFFECTIVE_DATE_REQUIRED);
        map.put(PlanMessageType.ACI_EFFECTIVE_DATE_PLAN_EFFECTIVE_DATE_WARNING.toString(), ErrorCodes.ACI_EFFECTIVE_DATE_PLAN_EFFECTIVE_DATE_WARNING);
        map.put(PlanMessageType.ACI_EFFECTIVE_DATE_BEFORE_CSF_ANNIVERSARY_DATE.toString(), ErrorCodes.ACI_EFFECTIVE_DATE_BEFORE_CSF_ANNIVERSARY_DATE);
        map.put(PlanMessageType.ACI_APPLIES_TO_REQUIRED.toString(), ErrorCodes.ACI_APPLIES_TO_REQUIRED);
        map.put(PlanMessageType.ACI_APPLY_DATE_REQUIRED.toString(), ErrorCodes.ACI_APPLY_DATE_REQUIRED);
        map.put(PlanMessageType.ACI_APPLY_DATE_NOT_ALLOWED.toString(), ErrorCodes.ACI_APPLY_DATE_NOT_ALLOWED);
        map.put(PlanMessageType.ACI_APPLIES_TO_EFFECTIVE_DATE_REQUIRED.toString(), ErrorCodes.ACI_APPLIES_TO_EFFECTIVE_DATE_REQUIRED);
        map.put(PlanMessageType.ACI_APPLIES_TO_EFFECTIVE_DATE_BEFORE_ACI_EFFECTIVE_DATE.toString(), ErrorCodes.ACI_APPLIES_TO_EFFECTIVE_DATE_BEFORE_ACI_EFFECTIVE_DATE);
        map.put(PlanMessageType.ACI_NO_BUT_CSF_ACI_IS_YES.toString(), ErrorCodes.ACI_CSF_ON_USING_AUTO_SIGNUP);
        map.put(PlanMessageType.PLAN_ALLOWS_LOANS_IS_NO_FOR_LRK01.toString(), ErrorCodes.PLAN_ALLOWS_LOANS_IS_NO_FOR_LRK01);
        map.put(PlanMessageType.ACI_DEFUALT_INCREASE_GREATER_THAN_DEFERRAL_MAX.toString(), ErrorCodes.ACI_DEFUALT_INCREASE_GREATER_THAN_DEFERRAL_MAX);
        map.put(PlanMessageType.ACI_DEFUALT_INCREASE_GREATER_THAN_DEFAULT_MAX.toString(), ErrorCodes.ACI_DEFUALT_INCREASE_GREATER_THAN_DEFAULT_MAX);
        map.put(PlanMessageType.ACI_DEFUALT_MAXIMUM_GREATER_THAN_DEFFERRAL_MAX.toString(), ErrorCodes.ACI_DEFUALT_MAXIMUM_GREATER_THAN_DEFFERRAL_MAX);
        map.put(PlanMessageType.ACI_DEFUALT_MAXIMUM_LESS_THAN_DEFFERRAL_MIN.toString(), ErrorCodes.ACI_DEFUALT_MAXIMUM_LESS_THAN_DEFFERRAL_MIN);
        map.put(PlanMessageType.ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_DAY_EMPTY.toString(), ErrorCodes.ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_DAY_EMPTY);
        map.put(PlanMessageType.ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_MONTHS_EMPTY.toString(), ErrorCodes.ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_MONTHS_EMPTY);
        map.put(PlanMessageType.ACI_DEFERRAL_LIMIT_MAX_MIN_ERROR.toString(), ErrorCodes.ACI_DEFERRAL_LIMIT_MAX_MIN_ERROR);
        map.put(PlanMessageType.ACI_DEFERRAL_LIMIT_MAX_EMPTY_ERROR.toString(), ErrorCodes.ACI_DEFERRAL_LIMIT_MAX_EMPTY_ERROR);
        map.put(PlanMessageType.ACI_DEFERRAL_ANNUAL_LIMIT_EMPTY.toString(), ErrorCodes.ACI_DEFERRAL_ANNUAL_LIMIT_EMPTY);
        map.put(PlanMessageType.ACI_DEFERRAL_ANNUAL_LIMIT_GREATER_THAN_IRS_LIMIT.toString(), ErrorCodes.ACI_DEFERRAL_ANNUAL_LIMIT_GREATER_THAN_IRS_LIMIT);
        map.put(PlanMessageType.ACI_FIRST_OF_FIELD_REQUIRES_TYPE.toString(), ErrorCodes.ACI_FIRST_OF_FIELD_REQUIRES_TYPE);
        map.put(PlanMessageType.ACI_NEXT_OF_FIELD_REQUIRES_TYPE.toString(), ErrorCodes.ACI_NEXT_OF_FIELD_REQUIRES_TYPE);
        map.put(PlanMessageType.ACI_MAX_MATCH_FIELD_REQUIRES_TYPE.toString(), ErrorCodes.ACI_MAX_MATCH_FIELD_REQUIRES_TYPE);
        map.put(PlanMessageType.ACI_NEXT_PERCENT_REQUIRED.toString(), ErrorCodes.ACI_NEXT_PERCENT_REQUIRED);
        map.put(PlanMessageType.ACI_MONEY_TYPE_SELECTED_BUT_NOT_RULE_TYPE.toString(), ErrorCodes.ACI_MONEY_TYPE_SELECTED_BUT_NOT_RULE_TYPE);
        map.put(PlanMessageType.ACI_RULE_FIRST_PERCENT_OR_NON_MATCHING_PERCENT_REQUIRED.toString(), ErrorCodes.ACI_RULE_FIRST_PERCENT_OR_NON_MATCHING_PERCENT_REQUIRED);
        map.put(PlanMessageType.ACI_RULE_MATCH_AND_NON_MATCH_ARE_BOTH_SPECIFIED.toString(), ErrorCodes.ACI_RULE_MATCH_AND_NON_MATCH_ARE_BOTH_SPECIFIED);
        map.put(PlanMessageType.ACI_MUST_BE_ON_IF_QACA_AND_MAX_DEFERRAL_LIMITS_TOO_LOW.toString(), ErrorCodes.ACI_MUST_BE_ON_IF_QACA_AND_MAX_DEFERRAL_LIMITS_TOO_LOW);
        map.put(PlanMessageType.ACI_RULE_PERCENTS_OR_DOLLAR_RADIO_BUTTONS_DONT_MATCH.toString(), ErrorCodes.ACI_RULE_PERCENTS_OR_DOLLAR_RADIO_BUTTONS_DONT_MATCH);
        map.put(PlanMessageType.ACI_RULE_NO_MONEY_TYPES.toString(), ErrorCodes.ACI_RULE_NO_MONEY_TYPES);
        map.put(PlanMessageType.ACI_RULE_FIRST_PECENT_REQUIRED_WHEN_FIRST_LIMIT_ENTERED.toString(), ErrorCodes.ACI_RULE_FIRST_PECENT_REQUIRED_WHEN_FIRST_LIMIT_ENTERED);
        map.put(PlanMessageType.ACI_PPTS_CAN_CHANGE_SALARY_DEFERRAL_ELECTIONS_REQUIRED.toString(), ErrorCodes.ACI_PPTS_CAN_CHANGE_SALARY_DEFERRAL_ELECTIONS_REQUIRED);
        map.put(PlanMessageType.ACI_DEFUALT_INCREASE_REQUIRED.toString(), ErrorCodes.ACI_DEFUALT_INCREASE_REQUIRED);
        map.put(PlanMessageType.ACI_DEFUALT_MAXIMUM_REQUIRED.toString(), ErrorCodes.ACI_DEFUALT_MAXIMUM_REQUIRED);
        map.put(PlanMessageType.PLAN_NAME_REQUIRED_FOR_CSF_SUMMARY_PLAN_HIGHLIGHTS.toString(), ErrorCodes.PLAN_NAME_REQUIRED_FOR_CSF_SUMMARY_PLAN_HIGHLIGHTS);
        map.put(PlanMessageType.QDIA_RESTRICTION_DETAILS_EXCEEDED_MAX_ALLOWED.toString(), ErrorCodes.QDIA_RESTRICTION_DETAILS_EXCEEDED_MAX_ALLOWED);
        
        map.put(PlanMessageType.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_CHANGED_WHEN_AE_CSF_IS_ON.toString(), ErrorCodes.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_CHANGED_WHEN_AE_CSF_IS_ON);
        map.put(PlanMessageType.ACI_DEFAULT_MAXIMUM_CHANGED_IMPACT_FUTURE_SERVICES_SETUP.toString(), ErrorCodes.ACI_DEFAULT_MAXIMUM_CHANGED_IMPACT_FUTURE_SERVICES_SETUP);
        map.put(PlanMessageType.ACI_DEFAULT_INCREASE_CHANGED_IMPACT_INCREASE_NOT_CUSTOMIZED_EMPLOYEES.toString(), ErrorCodes.ACI_DEFAULT_INCREASE_CHANGED_IMPACT_INCREASE_NOT_CUSTOMIZED_EMPLOYEES);
        map.put(PlanMessageType.ACI_ANNUAL_APPLY_DATE_CHANGED_IMPACT_ANNIV_DATES.toString(), ErrorCodes.ACI_ANNUAL_APPLY_DATE_CHANGED_IMPACT_ANNIV_DATES);
        map.put(PlanMessageType.ACI_EFFECTIVE_DATE_CHANGED_IMPACT_AFTER_ENROLLED_EMPLOYEES.toString(), ErrorCodes.ACI_EFFECTIVE_DATE_CHANGED_IMPACT_AFTER_ENROLLED_EMPLOYEES);
        map.put(PlanMessageType.ACI_APPLIES_TO_AUTO_ENROLLED_PPTS_WHEN_ACI_CSF_IS_ON.toString(), ErrorCodes.ACI_APPLIES_TO_AUTO_ENROLLED_PPTS_WHEN_ACI_CSF_IS_ON);
        map.put(PlanMessageType.ACI_APPLIES_TO_CHANGED_IMPACT_ALL_EMPLOYEES.toString(), ErrorCodes.ACI_APPLIES_TO_CHANGED_IMPACT_ALL_EMPLOYEES);
        map.put(PlanMessageType.PPT_SALARY_DEFERRAL_ELECTION_IS_UNSPECIFIED_WHEN_PPT_ALLOW_CHANGE_DEFERRALS_ONLINE_ON.toString(), ErrorCodes.PPT_SALARY_DEFERRAL_ELECTION_IS_UNSPECIFIED_WHEN_PPT_ALLOW_CHANGE_DEFERRALS_ONLINE_ON);
        map.put(PlanMessageType.ACI_ANNUAL_APPLY_DATE_INVALID_DUE_TO_CONTRACT_FREEZE_PERIOD.toString(), ErrorCodes.ACI_ANNUAL_APPLY_DATE_INVALID_DUE_TO_CONTRACT_FREEZE_PERIOD);
        map.put(PlanMessageType.PPT_SALARY_DEFERRAL_ELECTION_IS_UNSPECIFIED_WHEN_PLAN_ACI_ALLOWED_ON.toString(), ErrorCodes.PPT_SALARY_DEFERRAL_ELECTION_IS_UNSPECIFIED_WHEN_PLAN_ACI_ALLOWED_ON);
        map.put(PlanMessageType.AUTO_ENROLLMENT_MUST_NOT_BE_ON_WITH_EEDEF_FUTURE_END_DATE.toString(), ErrorCodes.AUTO_ENROLLMENT_MUST_NOT_BE_ON_WITH_EEDEF_FUTURE_END_DATE);
        map.put(PlanMessageType.MIN_LOAN_IS_NULL.toString(), ErrorCodes.MIN_LOAN_IS_EMPTY);
        map.put(PlanMessageType.PLAN_DEFERRAL_LIMIT_PCT_LESS_THAN_CSF_LIMIT.toString(), ErrorCodes.PLAN_DEFERRAL_PCT_LESS_THAN_CSF_DEFERRAL_PCT);
        map.put(PlanMessageType.PLAN_DEFERRAL_LIMIT_AMT_LESS_THAN_CSF_LIMIT.toString(), ErrorCodes.PLAN_DEFERRAL_AMT_LESS_THAN_CSF_DEFERRAL_AMT);
        return map;  
    }

    /**
     * getPlanMessageToGroupMap gets the static mapping of {@link PlanMessageType} to group names.
     * 
     * @return Map - The map of message types (Biz) to group names (UI).
     */
    private static Map<String, String> getPlanMessageToGroupMap() {

        if (messageToGroupMap != null) {
            return messageToGroupMap;
        }

        // Need to use strings because the enumerated types do not serialize properly
        final Map<String, String> map = new HashMap<String, String>();
        messageToGroupMap = map;

        // general
        map.put(PlanMessageType.PLAN_NAME_NOT_ENTERED.toString(), PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.INVALID_PLAN_NAME.toString(), PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.EMPLOYER_TAX_IDENTIFICATION_NUMBER_NOT_ENTERED.toString(),
                PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.PLAN_NUMBER_NOT_ENTERED.toString(), PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.INVALID_EMPLOYER_TAX_IDENTIFICATION_NUMBER.toString(),
                PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.NO_EMPLOYER_TAX_IDENTIFICATION_NUMBER_WITH_PLAN_NUMBER.toString(),
                PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.INVALID_PLAN_NUMBER.toString(), PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.NO_PLAN_NUMBER_WITH_EMPLOYER_TAX_IDENTIFICATION_NUMBER.toString(),
                PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.EMPLOYER_TAX_IDENTIFICATION_NUMBER_PLAN_NUMBER_NOT_UNIQUE
                .toString(), PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.PLAN_EFFECTIVE_DATE_AFTER_CONTRACT_EFFECTIVE_DATE.toString(),
                PlanDataUi.SECTION_GENERAL);
        //map.put(PlanMessageType.PLAN_EFFECTIVE_DATE_AFTER_EMPLOYEE_EFFECTIVE_DATE.toString(),
        //        PlanDataUi.SECTION_GENERAL);
            map.put(PlanMessageType.PLAN_EFFECTIVE_DATE_NOT_ENTERED.toString(),
                PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.INVALID_PLAN_EFFECTIVE_DATE.toString(), PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.INVALID_ENTITY_TYPE_OTHER_DESCRIPTION.toString(),
                PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.MISSING_ENTITY_TYPE_OTHER_DESCRIPTION.toString(),
                PlanDataUi.SECTION_GENERAL);
        map.put(PlanMessageType.PLAN_NAME_REQUIRED_FOR_CSF_SUMMARY_PLAN_HIGHLIGHTS.toString(),
                PlanDataUi.SECTION_GENERAL);
        
        // eligibility section        
        map.put(PlanMessageType.INVALID_FIRST_PLAN_ENTRY_DATE.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.EXISTING_PLAN_ENTRY_DATE_REMOVED.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);        
        map.put(PlanMessageType.FIRST_PLAN_ENTRY_DATE_REQUIRED.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);        
        map.put(PlanMessageType.CANNOT_TURN_AUTO_ENROLLMENT_OFFERED_OFF_WHEN_AE_CSF_IS_ON.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.PERIOD_OF_SERVICE_REQUIRED_FOR_ELAPSED_TIME_SERVICE_CREDITING_METHOD.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.HOURS_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.PERIOD_OF_SERVICE_REQUIRED_FOR_HOURS_OF_SERVICE_CREDITING_METHOD.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.BASIS_FOR_PERIOD_OF_SERVICE_REQUIRED.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.INVALID_PLAN_FREQUENCY_FOR_EZSTART.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.PLAN_ENTRY_FREQUENCY_REQUIRED_FOR_ELIGIBILITY_REQUIREMENT.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.FIRST_PLAN_ENTRY_DATE_OR_EEDEF_PLAN_ENTRY_FREQUENCY_CHANGED_WARNING.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.AUTO_ENROLLMENT_MUST_BE_ON_WHEN_PLAN_INCLUDES_QACA.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.AUTO_ENROLLMENT_MUST_NOT_BE_ON_WITH_MISSING_ELIGIBILITY_REQUIREMENTS_FOR_EEDEF.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_REQUIRED.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_LESS_THAN_CONTRIBUTION_DEFERRAL_MIN_PERCENT.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        map.put(PlanMessageType.AUTO_ENROLLMENT_DEFERRAL_PERCENTAGE_GREATER_THAN_CONTRIBUTION_DEFERRAL_MAX_PERCENT.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);        
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_REQUIRED.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);        
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_NOT_BEGINNING_OF_PLAN_YEAR_WARNING.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);        
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_EFFECTIVE_DATE_AFTER_INITIAL_ENROLLMENT_DATE.toString(),
                PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);        
        map.put(PlanMessageType.AUTOMATIC_ENROLLMENT_AUTOMATIC_WITHDRAWALS_REQUIRED.toString(),
        		PlanDataUi.SECTION_ELIGIBILITY_AND_PARTICIPATION);
        //withdrawals section 
        map.put(PlanMessageType.INVALID_NORMAL_RETIREMENT_AGE.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.INVALID_EARLY_RETIREMENT_AGE.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.INVALID_MINIMUM_HARDSHIP_AMOUNT.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.INVALID_MAXIMUM_HARDSHIP_AMOUNT.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.EEDEF_EARNINGS_FLAG_CHANGES_WARNING.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.MINIMUM_HARDSHIP_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.INVALID_INSTALMENT_STATUS_CHANGE_ERROR.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.INVALID_APOLLO_INSTALMENT_STATUS_CHANGE_ERROR.toString(),
                PlanDataUi.SECTION_WITHDRAWALS);
        map.put(PlanMessageType.MAX_OUTSTANDING_LOANS_NOT_ENTERED.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.INVALID_MINIMUM_LOAN_AMOUNT.toString(), PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.MINIMUM_LOAN_AMOUNT_LESS_THAN_MINIMUM_BOUND.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.INVALID_MAXIMUM_LOAN_AMOUNT.toString(), PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.MAXIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_BOUND.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.MINIMUM_LOAN_AMOUNT_GREATER_THAN_MAXIMUM_AMOUNT.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.INVALID_MAXIMUM_LOAN_PERCENT.toString(), PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.MAXIMUM_LOAN_PERCENT_GREATER_THAN_MAXIMUM.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.INVALID_LOAN_INTEREST_RATE_ABOVE_PRIME.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.LOAN_INTEREST_RATE_ABOVE_PRIME_NOT_IN_RANGE.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.GENERAL_PURPOSE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.HARDSHIP_MAXIMUM_AMORTIZATION_NOT_IN_RANGE.toString(),
                PlanDataUi.SECTION_LOANS);
        map.put(PlanMessageType.PRIMARY_RESIDENCE_MAXIMUM_AMORTIZATION_NOT_IN_RANGE.toString(),
                PlanDataUi.SECTION_LOANS);
        
        // vesting section
        map.put(PlanMessageType.NO_HOURS_OF_SERVICE_WITH_HOURS_OF_SERVICE_CREDIT_METHOD.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.INVALID_HOURS_OF_SERVICE.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.HOURS_OF_SERVICE_NOT_IN_RANGE.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.VESTING_PERCENTAGE_NOT_ENTERED.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.INVALID_VESTING_PERCENTAGE.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.VESTING_PERCENTAGE_NOT_IN_RANGE.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.VESTING_PERCENT_LESS_THAN_PREVIOUS_YEAR.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.NO_VESTING_INFORMATION_WHEN_CSF_CALCULATE_VESTING_YES.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.INVALID_VESTNG_COMPUTATION_PERIOD_FOR_HOURS_OF_SERVICE.toString(),
                PlanDataUi.SECTION_VESTING);
        map.put(PlanMessageType.INVALID_VESTNG_COMPUTATION_PERIOD_FOR_ELAPSED_TIME.toString(),
                PlanDataUi.SECTION_VESTING);        
        map.put(PlanMessageType.INVALID_MULTIPLE_VESTING_SCHEDULES_FOR_ONE_SINGLE_MONEY_TYPE.toString(),
                PlanDataUi.SECTION_VESTING);        
        // contributions section
        map.put(PlanMessageType.ACI_EFFECTIVE_DATE_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_EFFECTIVE_DATE_PLAN_EFFECTIVE_DATE_WARNING.toString(),
				PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_EFFECTIVE_DATE_BEFORE_CSF_ANNIVERSARY_DATE.toString(),
				PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_APPLIES_TO_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_APPLY_DATE_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_APPLY_DATE_NOT_ALLOWED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_APPLIES_TO_EFFECTIVE_DATE_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_NO_BUT_CSF_ACI_IS_YES.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFUALT_INCREASE_GREATER_THAN_DEFERRAL_MAX.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFUALT_INCREASE_GREATER_THAN_DEFAULT_MAX.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFUALT_MAXIMUM_GREATER_THAN_DEFFERRAL_MAX.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFUALT_MAXIMUM_LESS_THAN_DEFFERRAL_MIN.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_DAY_EMPTY.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_PLAN_EMPLOYEE_DEFERRAL_SELECTED_MONTHS_EMPTY.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFERRAL_LIMIT_MAX_MIN_ERROR.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFERRAL_ANNUAL_LIMIT_EMPTY.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFERRAL_ANNUAL_LIMIT_GREATER_THAN_IRS_LIMIT.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_FIRST_OF_FIELD_REQUIRES_TYPE.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_DEFERRAL_ANNUAL_LIMIT_GREATER_THAN_IRS_LIMIT.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_NEXT_OF_FIELD_REQUIRES_TYPE.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_MAX_MATCH_FIELD_REQUIRES_TYPE.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_NEXT_PERCENT_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_MONEY_TYPE_SELECTED_BUT_NOT_RULE_TYPE.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_RULE_FIRST_PERCENT_OR_NON_MATCHING_PERCENT_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_RULE_MATCH_AND_NON_MATCH_ARE_BOTH_SPECIFIED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_MUST_BE_ON_IF_QACA_AND_MAX_DEFERRAL_LIMITS_TOO_LOW.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_RULE_PERCENTS_OR_DOLLAR_RADIO_BUTTONS_DONT_MATCH.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		map.put(PlanMessageType.ACI_RULE_NO_MONEY_TYPES.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
        map.put(PlanMessageType.ACI_PPTS_CAN_CHANGE_SALARY_DEFERRAL_ELECTIONS_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
        map.put(PlanMessageType.ACI_DEFUALT_INCREASE_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
        map.put(PlanMessageType.ACI_DEFUALT_MAXIMUM_REQUIRED.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
        map.put(PlanMessageType.PLAN_DEFERRAL_LIMIT_PCT_LESS_THAN_CSF_LIMIT.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
        map.put(PlanMessageType.PLAN_DEFERRAL_LIMIT_AMT_LESS_THAN_CSF_LIMIT.toString(), PlanDataUi.SECTION_CONTRIBUTIONS);
		
		
		// loans section
        map.put(PlanMessageType.PLAN_ALLOWS_LOANS_IS_NO_FOR_LRK01.toString(),
				PlanDataUi.SECTION_LOANS);
        // other plan information        
        map.put(PlanMessageType.QDIA_RESTRICTION_DETAILS_REQUIRED.toString(),
                PlanDataUi.SECTION_OTHER_PLAN_INFORMATION);
        map.put(PlanMessageType.QDIA_RESTRICTION_DETAILS_EXCEEDED_MAX_ALLOWED.toString(),
                PlanDataUi.SECTION_OTHER_PLAN_INFORMATION);
        
   


        return map;
    }

    /**
     * Converts from the general {@link MessageCategory} to the {@link ValidationError.Type}.
     * 
     * @param planMessage The plan message to get the type for.
     * @return ValidationError.Type - The type this message correlates to.
     */
    public static ValidationError.Type getValidationTypeFromMessage(final PlanMessage planMessage) {
        switch (planMessage.getPlanMessageType().getMessageCategory()) {
            case ERROR:
                return ValidationError.Type.error;
            case WARNING:
                return ValidationError.Type.warning;
            case ALERT:
                return ValidationError.Type.alert;
            default:
                throw new NotImplementedException("Missing Mapping for MessageCategory: ["
                        + planMessage.getPlanMessageType().getMessageCategory() + "]");
        }
    }

    /**
     * This method maps the biz tier message type (found in {@link PlanMessageType}) to a web tier
     * error code (found in {@link ErrorCodes}). This method throws a {@link RuntimeException} if
     * the mapping is not found.
     * 
     * @param planMessage The message to map.
     * @return int The error code for the given error.
     */
    public static int getErrorCodeFromMessage(final PlanMessage planMessage) {

        final Integer errorCode = getPlanMessageToErrorCodeMap().get(
               planMessage.getPlanMessageType().toString());

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer().append("Mapping for [").append(
                    planMessage.getPlanMessageType().toString()).append("] is [").append(errorCode)
                    .append("] - for properties: [").append(planMessage.getPropertyNames()).append(
                            "]").toString());
        }

        if (errorCode == null) {
            throw new NotImplementedException("Unable to deal with MessageType: "
                    + planMessage.getPlanMessageType().toString());
        } else {
            return errorCode;
        }
    }

    /**
     * This method maps the biz tier message type (found in {@link PlanMessageType}) to a web tier
     * group code. This method throws a {@link RuntimeException} if the mapping is not found.
     * 
     * @param planMessage The message to map.
     * @return int The error code for the given error.
     */
    public static String getGroupFromMessage(final PlanMessage planMessage) {

        final String group = getPlanMessageToGroupMap().get(
                planMessage.getPlanMessageType().toString());

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer().append("Mapping for [").append(
                    planMessage.getPlanMessageType().toString()).append("] is [").append(group)
                    .append("] - for properties: [").append(planMessage.getPropertyNames()).append(
                            "]").toString());
        }

        return group;
    }
}
