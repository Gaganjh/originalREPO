package com.manulife.pension.service.withdrawal.valueobject;

/**
 * Defines the field property names for the withdrawal request object.
 * 
 * @author dickand
 */
public class WithdrawalRequestProperty {

    public static final String AMOUNT_TYPE_CODE = "amountTypeCode";

    public static final String ADDRESS_LINE_ONE = "addressLine1";

    public static final String CITY = "city";

    public static final String STATE_CODE = "stateCode";

    public static final String ZIP_CODE = "zipCode";

    public static final String COUNTRY_CODE = "countryCode";

    public static final String FEE_VALUE = "value";

    public static final String SPECIFIC_AMOUNT = "withdrawalAmount";

    public static final String VESTING_PERCENTAGE = "vestingPercentage";

    public static final String REQUESTED_PERCENTAGE = "withdrawalPercentage";

    public static final String REQUESTED_AMOUNT = "withdrawalAmount";

    public static final String STATE_TAX_PERCENT = "stateTaxPercent";

    public static final String DATE_OF_BIRTH = "birthDate";
    
    public static final String LEGALLY_MARRIED_IND = "legallyMarriedInd";

    public static final String EXPIRATION_DATE = "expirationDate";

    public static final String TERMINATION_DATE = "terminationDate";

    public static final String DISABILITY_DATE = "disabilityDate";

    public static final String REASON_CODE = "reasonCode";

    public static final String RETIREMENT_DATE = "retirementDate";

    public static final String FINAL_CONTRIBUTION_DATE = "finalContributionDate";

    public static final String IRS_CODE_FOR_WITHDRAWAL = "irsDistCode";

    public static final String IRS_CODE_FOR_LOAN = "irsDistributionCodeLoanClosure";

    public static final String LOAN_OPTION = "loanOption";

    public static final String ROLLOVER_PLAN_NAME = "rolloverPlanName";

    public static final String TAX_NOTICE_DECLARATION = "taxNoticeDeclaration";

    public static final String WAITING_PERIOD_WAIVED_DECLARATION = "waitingPeriodDeclaration";

    public static final String IRA_SERVICE_PROVIDER_DECLARATION = "iraProviderDeclaration";

    public static final String AT_RISK_TRANSACTION_DECLARATION = "atRiskTransactionDeclaration";

    public static final String IRA_SERVICE_PROVIDER_CODE = "iraServiceProviderCode";

    public static final String NOTE_TO_PARTICIPANT = "noteToParticipant";
    
    // Security Enhancements
    public static final String ORGANIZATION_NAME = "organizationName";
    public static final String ROLLOVER_TYPE = "rolloverType";
    
    public static final String INVALID_MONEY_TYPE ="invalidMoneyType";
    public static final String MINIMUM_HARDSHIP_AMOUNT ="minimumHardhShipAmount";
    public static final String MAXIMUM_HARDSHIP_AMOUNT ="maximumHardshipAmount";
    public static final String AVAILABLE_HARDSHIP_AMOUNT="availableHardshipAmount";
}
