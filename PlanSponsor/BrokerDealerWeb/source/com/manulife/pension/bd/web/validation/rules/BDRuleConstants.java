package com.manulife.pension.bd.web.validation.rules;

/**
 * This is the constants file for rules package
 * 
 * @author Ilamparithi
 * 
 */
public class BDRuleConstants {

    public static final String USA_COUNTRY_CODE = "USA";

    public static final String ACCESS_CODE_RE = "^[a-zA-Z]{5,25}$";

    // ASCII values between 32-126 excluding < > "  % ; ) ( & +. Represented in
    // the RE by octal values
    public static final String INVALID_VALUE_RE = "[\40-\41\43\44\47\52\54-\72\75-\75\77-\176]+";

    // Can be empty. Hence the *.
    public static final String INVALID_VALUE_WITH_EMPTY_RE = "[\40-\41\43\44\47\52\54-\72\75-\75\77-\176]*";

    // should be numeric, 5 digits and should not be all
    // zeros
    public static final String ZIPCODE1_FOR_USA_RE = "(?=.*[1-9])[0-9]{5}";

    // should be numeric, 4 digits and should not be all
    // zeros
    public static final String ZIPCODE2_FOR_USA_RE = "(?=.*[1-9])[0-9]{4}";

    public static final String EMAIL_ADDRESS_RE = "^[a-zA-Z0-9\\-\\._']+@([a-zA-Z0-9\\-]+\\.)+[a-zA-Z0-9]+$";

    public static final String CREATE_CHALLENGE_QUESTION_RE = "^.{1,100}$";

    public static final String CHALLENGE_QUESTION_ANSWER_RE = "^.{1,32}$";

    public static final String PHONE_NUMBER_RE = "^[0-9]{10}$";
    
    public static final String IARD_RE = "^[0-9]*$";

    public static final String SSN_RE = "^[0-9]{9}$";

    public static final String TAX_ID_RE = "^[0-9]{9}$";

    // The - and the last 4 digits are optional
    public static final String US_ZIPCODE_RE = "^\\d{5}(-\\d{4})?$";

    // Matches the telephone number of format ###-###-#### or an empty string
    public static final String TELEPHONE_MOBILE_FAX_NUMBER_RE = "^((\\d{3}\\-\\d{3}\\-\\d{4})?)$";

    // First char should not be an empty space
    public static final String FIRST_NAME_LAST_NAME_RE = "^[0-9a-zA-Z\\-\\.\\']{1}[0-9a-zA-Z \\-\\.\\']{0,29}$";
    
    // Company name should not be an empty space
    public static final String COMPANY_NAME_RE = "^[\41\43\44\47\52\54-\72\75-\75\77-\176]{1}[\40-\41\43\44\47\52\54-\72\75-\75\77-\176]{0,29}$";
    
    // All ASCII printable chars are allowed. (32-126). Represented in the RE by octal values 
    public static final String BROKER_PERSONAL_INFO_INVALID_VALUE_RE = "[\40-\176]+";
    
    // Can be empty. Hence the *.
    public static final String BROKER_PERSONAL_INFO_INVALID_VALUE_WITH_EMPTY_RE = "[\40-\176]*";
    
    /*
	 * Max. 30 characters, no special characters except apostrophe,no Numbers, dash and
	 * period. No leading space.
	 * NOTE: [:alnum:] accepts underscore '_' which we want to avoid.
	 */
    public static final String VALIDATE_FIRST_NAME_AND_LAST_NAME_RE = "^[a-zA-Z\\-\\.\\']{1}[a-zA-Z \\-\\.\\']{0,29}$";
}
