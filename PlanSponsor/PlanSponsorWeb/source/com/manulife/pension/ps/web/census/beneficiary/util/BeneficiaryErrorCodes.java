package com.manulife.pension.ps.web.census.beneficiary.util;

/**
 * Error codes for Online Beneficiary edit page
 * 
 * @author Tamilarasu krishnamoorthy
 *
 */
public interface BeneficiaryErrorCodes {

	public static final int ZERO_BENEFICIARY = 1512;

	public static final int ERROR_FIRST_NAME_REQUIRED = 1500;

	public static final int ERROR_LAST_NAME_REQUIRED = 1502;

	public static final int ERROR_FIRST_NAME_INVALID = 1501;

	public static final int ERROR_LAST_NAME_INVALID = 1503;

	public static final int ERROR_DATE_OF_BIRTH_INVALID = 1504;

	public static final int ERROR_DOB_GREATER_THAN_CURRENT_DATE = 1505;

	public static final int ERROR_RELATIONSHIP_REQUIRED = 1506;

	public static final int ERROR_RELATIONSHIP_OTHER_INVALID = 1507;

	public static final int ERROR_PERCENTAGE_SHARE_NOT_NULL_OR_NOT_NUMERIC = 1508;

	public static final int ERROR_PERCENTAGE_SHARE_NOT_BW_0_AND_100 = 1509;

	public static final int ERROR_PERCENTAGE_SHARE_NOT_EQUAL_100 = 1510;
	
	public static final int ERROR_ADD_MAXIMUM_BENEFICIARY = 1511;
	
	public static final int ERROR_ZERO_PRIMARY_WITH_CONTIGENT = 1513;

}
