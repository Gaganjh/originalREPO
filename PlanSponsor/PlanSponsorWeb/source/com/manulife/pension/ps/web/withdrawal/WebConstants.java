package com.manulife.pension.ps.web.withdrawal;

/**
 * Defines constants that are used on the web-tier.
 * 
 * @author dickand
 */
public class WebConstants {

    /**
     * Defines a parameter or attribute name for the profile ID.
     */
    public static final String PROFILE_ID_PARAMETER = "profileIdParameter";

    /**
     * Defines a parameter or attribute name for the contract ID.
     */
    public static final String CONTRACT_ID_PARAMETER = "contractIdParameter";

    /**
     * Defines a parameter or attribute name for the originator.
     */
    public static final String ORIGINATOR_PARAMETER = "originatorParameter";

    /**
     * Defines a parameter or attribute name for the submission ID.
     */
    public static final String SUBMISSION_ID_PARAMETER = "submissionIdParameter";

    /**
     * Defines a parameter or attribute name for the withdrawal status code.
     */
    public static final String WITHDRAWAL_STATUS_CODE_PARAMETER = "withdrawalStatusCodeParameter";

    /**
     * Defines a parameter or attribute name for the withdrawal status code.
     */
    public static final String WITHDRAWAL_INITIATED_BY_PARAMETER = "withdrawalInitiatedByParameter";

    /**
     * Defines the participant account originating page code.
     */
    public static final String PARTICIPANT_ACCOUNT_ORIGINATOR = "participantAccount";

    /**
     * Defines the search participant originating page code.
     */
    public static final String SEARCH_PARTICIPANT_ORIGINATOR = "searchSummary";

    /**
     * Defines a key for marking a request as print friendly.
     */
    public static final String PRINTFRIENDLY_KEY = "withdrawalRequestPrintFriendlyKey";
    
    /**
     * Defines the loan and withdrawal list originating page code.
     */
    public static final String WITHDRAWAL_LIST_ORIGINATOR = "loanAndWithdrawal";

    /**
     * Defines the attribute name for the withdrawal request meta data.
     */
    public static final String WITHDRAWAL_REQUEST_METADATA_ATTRIBUTE = "withdrawalRequestMetaData";
    
    /**
     * Defines the attribute name for the withdrawal request meta data.
     */
    public static final String WITHDRAWAL_REQUEST_METADATA_UI_ATTRIBUTE = "withdrawalRequestMetaDataUi";
    
    /**
     * Defines the name for the request list page
     */
    public static final String REQUEST_LIST_PAGE_NAME = "requestListPage";

    /**
     * LAST_ACTIVE_PAGE_LOCATION is the session key that holds the last active page location for the
     * withdrawals actions. The value is the action path (adjusted (no '/do' prefix)).
     */
    public static final String LAST_ACTIVE_PAGE_LOCATION = "lastActivePageLocation";
    
    public static final String YES = "Y";
    
    //	GIFL-2A - 
	public static final String BB_BATCH_STATUS = "BBBatchDateLessThenETL";    

    public static final String SUBMISSION_ID_LOAN_PARAMETER = "submissionId";
    public static final String CONTRACT_ID_LOAN_PARAMETER = "contractId";
}
