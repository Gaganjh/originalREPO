package com.manulife.pension.bd.web.ikit;

/**
 * Error codes and messages that are passed back to the iKit application in the
 * XML returned.
 * 
 * @author harlomte
 * 
 */
public final class IKitErrorCodes {

	/*
	 * Application Error codes:
	 */
	public static final String CONTRACT_ID_NOT_FOUND_CODE = "001";
	public static final String CONTRACT_ID_NOT_FOUND_MSG = "Contract ID does not exist";

	public static final String CONTRACT_ACCESS_CODE_DOES_NOT_MATCH_CODE = "002";
	public static final String CONTRACT_ACCESS_CODE_DOES_NOT_MATCH_MSG = 
		"IAN does not match the one associated with the contract ID";

	public static final String CONTRACT_STATUS_NOT_SUPPORTED_CODE = "003";
	public static final String CONTRACT_STATUS_NOT_SUPPORTED_MSG = 
		"Request not allowed, contract status code = ";

// 2010-03-05: i:enrollment edit removed at the request of the business
//	public static final String IENROLLMENT_NOT_ALLOWED_CODE = "004";
//	public static final String IENROLLMENT_NOT_ALLOWED_MSG = 
//		"Contract's i:enrollment indicator is not 'Y', request not allowed";

	public static final String CONTRACT_ID_NON_NUMERIC_CODE = "005";
	public static final String CONTRACT_ID_NON_NUMERIC_MSG = "Contract ID is not numeric";

	public static final String CONTRACT_ACCESS_CODE_CANNOT_BE_BLANK_CODE = "006";
	public static final String CONTRACT_ACCESS_CODE_CANNOT_BE_BLANK_MSG = "IAN must not be blank";
	
	public static final String DEFINED_BENEFIT_CONTRACT_NOT_SUPPORTED_CODE = "007";
	public static final String DEFINED_BENEFIT_CONTRACT_NOT_SUPPORTED_MSG = "Request not allowed for Defined Benefit contracts"; 

	/*
	 * System Exception Error codes:
	 */
	public static final String CONTRACT_DATA_RETRIEVAL_EXCEPTION_CODE = "900";
	public static final String CONTRACT_DATA_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling ContractServiceDelegate.getIKitContractData(): ";

	public static final String FUND_PERFORMANCE_DATA_RETRIEVAL_EXCEPTION_CODE = "901";
	public static final String FUND_PERFORMANCE_DATA_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling FundServiceDelegate.getFundsAndPerformance(): ";

	public static final String FUND_REPORT_ASOFDATE_RETRIEVAL_EXCEPTION_CODE = "902";
	public static final String FUND_REPORT_ASOFDATE_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling FundServiceDelegate.getReportAsOfDate(): ";

	public static final String FUND_PERF_DATA_BY_INV_CTGRY_RETRIEVAL_EXCEPTION_CODE = "903";
	public static final String FUND_PERF_DATA_BY_INV_CTGRY_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when fetching Fund ID's by Risk/Return by call to FundServiceDelegate.getFundsAndPerformance():";

	public static final String ASSET_CLASSES_LIST_RETRIEVAL_EXCEPTION_CODE = "904";
	public static final String ASSET_CLASSES_LIST_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling FundServiceDelegate.getAssetClassesList(): ";

	public static final String INV_CATEGORIES_LIST_RETRIEVAL_EXCEPTION_CODE = "905";
	public static final String INV_CATEGORIES_LIST_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling FundServiceDelegate.getInvestmentCategoriesList(): ";

	public static final String CONTRACT_FUNDS_RETRIEVAL_EXCEPTION_CODE = "906";
	public static final String CONTRACT_FUNDS_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling FundServiceDelegate.getContractFunds(): ";
	
	public static final String CONTRACT_GA_RATES_RETRIEVAL_EXCEPTION_CODE = "907";
	public static final String CONTRACT_GA_RATES_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling FundServiceDelegate.getGARatesForContract(): ";
	
	public static final String CONTRACT_PROFILE_DATA_RETRIEVAL_EXCEPTION_CODE = "908";
	public static final String CONTRACT_PROFILE_DATA_RETRIEVAL_EXCEPTION_MSG = 
		"Unexpected System exception when calling ContractServiceDelegate.getContractProfileDetails(): ";
	
	public static final String PARSER_CONFIGURATION_EXCEPTION_CODE = "909";
	public static final String PARSER_CONFIGURATION_EXCEPTION_MSG = 
		"ParserConfigurationException occured in GetIKitFundInfoAction.generateXMLDocument()" +
		"while generating XML";

	 
	
	public static final String UNKNOWN_SYSTEM_EXCEPTION_CODE = "999";
	public static final String UNKNOWN_SYSTEM_EXCEPTION_MSG = "Unexpected System Exception occurred.";
}
