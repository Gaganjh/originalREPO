package com.manulife.pension.service.loan.domain;

import java.math.BigDecimal;

public interface LoanConstants {

	int LOAN_INTEREST_RATE_SCALE = 3;

	int LOAN_INTEREST_RATE_ROUND_RULE = BigDecimal.ROUND_HALF_UP;

	int AMOUNT_SCALE = 2;

	int ZIP_CODE_2_LENGTH = 4;

	int ABA_ROUTING_NUMBER_LENGTH = 9;
	
	int VESTING_PERCENTAGE_SCALE = 3;

	int VESTING_PERCENTAGE_MINIMUM = 0;

	BigDecimal VESTING_PERCENTAGE_MAXIMUM = new BigDecimal("100.000");

	BigDecimal INTEREST_RATE_MAXIMUM = new BigDecimal("100.000");

	BigDecimal INTEREST_RATE_MINIMUM = new BigDecimal("0.000");

	int INTEREST_RATE_PERCENTAGE_SCALE = 3;

	BigDecimal ONE_HUNDRED_2_DECIMAL_PLACES = new BigDecimal("100.00");
	
    int HOUR_OF_DAY_FOUR_PM = 16;       // i.e. 16th hour of the day
	
	/**
	 * The submission type code for Loan.
	 */
	String SUBMISSION_CASE_TYPE_CODE = "L";

	/**
	 * Loan type General Purpose
	 */
	String TYPE_GENERAL_PURPOSE = "GP";
    String TYPE_GENERAL_PURPOSE_DESC = "General purpose";

	/**
	 * Loan type Hardship
	 */
	String TYPE_HARDSHIP = "HA";
    String TYPE_HARDSHIP_DESC = "Hardship";

	/**
	 * Loan type Primary residence
	 */
	String TYPE_PRIMARY_RESIDENCE = "PR";
    String TYPE_PRIMARY_RESIDENCE_DESC = "Purchase of primary residence";

	/**
	 * User role code for Participant.
	 */
	String USER_ROLE_PARTICIPANT_CODE = "PA";

	/**
	 * User role code for Plan Sponsor.
	 */
	String USER_ROLE_PLAN_SPONSOR_CODE = "PS";

	/**
	 * User role code for TPA.
	 */
	String USER_ROLE_TPA_CODE = "TP";

	/**
	 * User role code for John Hancock BGA CAR.
	 * Bundled GA Project 2012 - Added new code for loans initiated by John Hancock (as TPA)
	 */
	String USER_ROLE_JH_BUNDLED_GA_CODE = "JH";
    String USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME = "John Hancock Representative";
	
	String USER_ROLE_PARTICIPANT_DISPLAY_NAME = "Participant";
    String USER_ROLE_PLAN_SPONSOR_DISPLAY_NAME = "Plan Sponsor";
	String USER_ROLE_TPA_DISPLAY_NAME = "TPA";

    int LOAN_REASON_MAXIMUM_LENGTH = 250;
    int DEFAULT_PROVISION_MAXIMUM_LENGTH = 500;
    BigDecimal TPA_LOAN_ISSUE_FEE_MAXIMUM_VALUE = new BigDecimal("1000.00");  
    
	int FREQUENCY_TYPE_WEEKLY_PERIODS_PER_YEAR = 52;
	int FREQUENCY_TYPE_WEEKLY_DAYS_PER_PERIOD = 7;
	int FREQUENCY_TYPE_WEEKLY_PERIODS_PER_MONTH = 4;

	int FREQUENCY_TYPE_BI_WEEKLY_PERIODS_PER_YEAR = 26;
	int FREQUENCY_TYPE_BI_WEEKLY_DAYS_PER_PERIOD = 14;
	int FREQUENCY_TYPE_BI_WEEKLY_PERIODS_PER_MONTH = 2;

	int FREQUENCY_TYPE_MONTHLY_PERIODS_PER_YEAR = 12;
	BigDecimal FREQUENCY_TYPE_MONTHLY_DAYS_PER_PERIOD = new BigDecimal("30.5");
	int FREQUENCY_TYPE_MONTHLY_PERIODS_PER_MONTH = 1;

	int FREQUENCY_TYPE_SEMI_MONTHLY_PERIODS_PER_YEAR = 24;
	BigDecimal FREQUENCY_TYPE_SEMI_MONTHLY_DAYS_PER_PERIOD = new BigDecimal(
			"15.25");
	BigDecimal FREQUENCY_TYPE_SEMI_MONTHLY_PERIODS_PER_MONTH = new BigDecimal(
			"0.5");

	String USA = "USA";
	String PRODUCT_FEATURE_BUNDLED_GA = "%BGA%";
}
