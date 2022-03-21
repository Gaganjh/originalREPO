package com.manulife.pension.ps.service.report.tpabob.valueobject;

import com.manulife.pension.ps.service.report.tpabob.reporthandler.TPABlockOfBusinessReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class TPABlockOfBusinessReportData extends ReportData {
	
    public TPABlockOfBusinessReportData() {
        super();
    }

    public TPABlockOfBusinessReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
    }
    
    private static final long serialVersionUID = 1L;

    public static final String REPORT_ID = TPABlockOfBusinessReportHandler.class.getName();

    public static final String REPORT_NAME = "TPABlockOfBusiness";

    // BOB Column ID's that should match those present in JSP page
    public static final String COL_CONTRACT_NAME_ID = "contractName";

    public static final String COL_CONTRACT_NUMBER_ID = "contractNumber";

    public static final String COL_CONTRACT_PLAN_YEAR_END_ID = "contractPlanYearEnd";

    public static final String COL_EXPECTED_FIRST_YEAR_ASSETS_ID = "expectedFirstYearAssets";

    public static final String COL_EXPECTED_NUM_OF_LIVES_ID = "expectedNumOfLives";

    public static final String COL_NUM_OF_LIVES_ID = "numOfLives";

    public static final String COL_TOTAL_ASSETS_ID = "totalAssets";

    public static final String COL_TRANSFERRED_OUT_ASSETS_ID = "transferredOutAssets";

    public static final String COL_DISCONTINUANCE_DATE_ID = "discontinuanceDate";

    public static final String COL_FINANCIAL_REP_ID = "financialRep";

    public static final String COL_CAR_NAME_ID = "carName";
    
    // Extra columns that will be shown only in CSV..
    public static final String COL_TPA_FIRM_ID = "tpaFirm";

    public static final String COL_CONTRACT_STATUS_ID = "contractStatus";

    public static final String COL_CONTRACT_EFFECTIVE_ID = "contractEffective";

    public static final String COL_ALLOCATED_ASSETS_ID = "allocatedAssets";

    public static final String COL_LOAN_ASSETS_ID = "loanAssets";

    public static final String COL_CASH_ACCOUNT_BALANCE_ID = "cashAccountBalance";

    public static final String COL_PBA_ASSETS_ID = "pbaAssets";

    public static final String COL_EZ_START_ID = "ezStart";

    public static final String COL_EZ_INCREASE_ID = "ezIncrease";

    public static final String COL_DIRECT_MAIL_ID = "directMail";

    public static final String COL_GIFL_ID = "gifl";
    
    public static final String COL_MANAGED_ACCOUNT_ID = "managedAccount";

    public static final String COL_PAM_ID = "pam";
    
    public static final String COL_ONLINE_BENEFICIARY_DESIGNATION_ID = "onlineBeneficiaryDsgn";

    public static final String COL_ONLINE_WITHDRAWALS_ID = "onlineWithdrawals";
    

    public static final String COL_SYS_WITHDRAWALS_ID = "sysWithdrawals";
    
    
    public static final String COL_SEND_SERVICE_ID = "sendService";
    
    public static final String COL_JOHNHANCOCK_PASSIVE_TRUSTEE_CODE_ID = "johnHancockPassiveTrusteeCode";

    public static final String COL_VESTING_PERCENTAGE_ID = "vestingPercentage";

    public static final String COL_VESTING_ON_STATEMENTS_ID = "vesetingOnStatements";

    public static final String COL_PERMITTED_DISPARITY_ID = "permittedDisparity";

    public static final String COL_FSW_ID = "fsw";

    public static final String COL_DIO_ID = "dio";
    
    public static final String COL_TPA_SIGNING_AUTHORITY_ID = "tpaSigningAuthority";

    public static final String COL_COW_ID = "cow";
    
    public static final String COL_PAYROL_PATH_ID = "payRollPath";
    
    public static final String COL_PAYROLL_FEEDBACK_SVC_ID = "payrollFeedback";
    
    public static final String COL_PLAN_HIGHLIGHTS_ID = "planHighlights";
    
    public static final String COL_PLAN_HIGHLIGHTS_REVIEWED_ID = "planHighlightsReviewed";
    
	public static final String COL_SFC_ID = "SignatureFiduciaryConnect";

	public static final String COL_PEP_ID = "pooledEmployerPlan";
    
    public static final String COL_INSTALLMENTS_ID = "installments";
    
    // Filter ID's that help in identifying a given filter.
    public static final String FILTER_CONTRACT_NAME_ID = "contractName";

    public static final String FILTER_CONTRACT_NUMBER_ID = "contractNumber";

    public static final String FILTER_FINANCIAL_REP_OR_ORG_NAME_ID = "financialRepNameOrOrgName";

    public static final String FILTER_US_OR_NY = "usOrNy";

    public static final String FILTER_CAR_NAME_ID = "carName";
    
    public static final String FILTER_AS_OF_DATE = "asOfDate";
    
    public static final String FILTER_USER_PROFILE_ID = "userProfileID";

    public static final String FILTER_MIMIC_USER_PROFILE_ID = "mimicUserProfileID";

    public static final String FILTER_USER_ROLE = "userRole";

    public static final String FILTER_MIMIC_USER_ROLE = "mimicUserRole";

    public static final String FILTER_PARTY_ID = "partyID";

    public static final String FILTER_DB_SESSION_ID = "dbSessionID";

    public static final String FILTER_CONTRACT_STATUS_CODES = "contractStatusCodes";

    public static final String FILTER_START_ROW_NUM = "startRowNum";

    public static final String FILTER_END_ROW_NUM = "endRowNum";
    
    public static final String CSV_DOWNLOAD_IND = "csvDownloadInd";
    

    // This will hold the SUmmary Information to be shown in TPA BOB page.
    private TPABlockOfBusinessSummaryVO tpaBlockOfBusinessSummaryVO;
    
    private Integer dbSessionID;

    private Boolean resultTooBigInd;
    
   

    public TPABlockOfBusinessSummaryVO getTpaBlockOfBusinessSummaryVO() {
        return tpaBlockOfBusinessSummaryVO;
    }

    public void setTpaBlockOfBusinessSummaryVO(
            TPABlockOfBusinessSummaryVO tpaBlockOfBusinessSummaryVO) {
        this.tpaBlockOfBusinessSummaryVO = tpaBlockOfBusinessSummaryVO;
    }

    public Integer getDbSessionID() {
        return dbSessionID;
    }

    public void setDbSessionID(Integer dbSessionID) {
        this.dbSessionID = dbSessionID;
    }

    public Boolean getResultTooBigInd() {
        return resultTooBigInd;
    }

    public void setResultTooBigInd(Boolean resultTooBigInd) {
        this.resultTooBigInd = resultTooBigInd;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    
}
