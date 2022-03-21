package com.manulife.pension.ps.service.report.bob.valueobject;

import com.manulife.pension.ps.service.report.bob.reporthandler.BlockOfBusinessReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class will contain the information that would be shown on the JSP page.
 * 
 * This class will have information on the Summary Section, information on Rows to be shown in
 * actual report.
 * 
 * @author harlomte
 * 
 */
public class BlockOfBusinessReportData extends ReportData {
    
    public BlockOfBusinessReportData() {
        
    }
    
    public BlockOfBusinessReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
    }

    private static final long serialVersionUID = -2361827774482375825L;

    public static final String REPORT_ID = BlockOfBusinessReportHandler.class.getName(); 
    
    // This string will be used for giving CSV File name for NBDW reports.
    public static final String CSV_REPORT_NAME = "BlockOfBusiness";

    public static final String CSV_ALL_REPORT_NAME = "BlockOfBusinessAll";

    // Filter ID's that help in identifying a given filter.
    public static final String FILTER_AS_OF_DATE = "asOfDate";
    
    public static final String FILTER_CONTRACT_NAME = "contractName";
    
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
    
    public static final String FILTER_FINANCIAL_REP_NAME = "financialRepName";

    // This holds the firm ID of the firm name displayed in the page.
    public static final String FILTER_BDFIRM_NAME = "firmName";

    // This holds the actual firm name as displayed in the page.
    public static final String FILTER_FIRM_NAME = "firmNameFilterCriteria";

    public static final String FILTER_ASSET_RANGE_FROM = "assetsRangeFrom";

    public static final String FILTER_ASSET_RANGE_TO = "assetsRangeTo";

    public static final String FILTER_CONTRACT_STATE = "contractState";

    public static final String FILTER_FUND_CLASS = "class";

    public static final String FILTER_RPV_NAME = "rvpName";

    public static final String FILTER_SALES_REGION = "salesRegion";

    public static final String FILTER_SALES_DIVISION = "salesDivision";

    public static final String FILTER_CSF_FEATURE = "csf";

    public static final String FILTER_US_OR_NY = "usOrNy";

    public static final String FILTER_USER_PROFILE_ID = "userProfileID";
    
    public static final String FILTER_MIMIC_USER_PROFILE_ID = "mimicUserProfileID";

    public static final String FILTER_USER_ROLE = "userRole";

    public static final String FILTER_MIMIC_USER_ROLE = "mimicUserRole";

    public static final String FILTER_PARTY_ID = "partyID";
    
    public static final String FILTER_DB_SESSION_ID = "dbSessionID";
    
    public static final String FILTER_CONTRACT_STATUS_CODES = "contractStatusCodes";
    
    public static final String FILTER_START_ROW_NUM = "startRowNum";

    public static final String FILTER_END_ROW_NUM = "endRowNum";
    
    public static final String FILTER_PRODUCT_TYPE = "productType";
    
    // BOB Columns Id's that should match with the key values in BOBColumnsInfo.xml file
    public static final String COL_CONTRACT_NAME_ID = "contractName";

    public static final String COL_PROPOSAL_NAME_ID = "proposalName";

    public static final String COL_CONTRACT_NUMBER_ID = "contractNumber";

    public static final String COL_PROPOSAL_NUMBER_ID = "proposalNumber";

    public static final String COL_CONTRACT_EFF_DT_ID = "contractEffDt";

    public static final String COL_PROPOSAL_DT_ID = "proposalDt";

    public static final String COL_CONTRACT_PLAN_YEAR_END_MMDD_ID = "contractPlanYearEndMMDD";

    public static final String COL_CONTRACT_STATE_ID = "contractState";

    public static final String COL_NUM_OF_LIVES = "numOfLives";

    public static final String COL_TOTAL_ASSETS_ID = "totalAssets";

    public static final String COL_TRANSFERREDOUT_ASSETS_PRIORTO_CHARGES_ID = "transferredOutAssetsPriorToCharges";

    public static final String COL_ASSET_CHARGE_ID = "assetCharge";

    public static final String COL_EXPECTED_FIRST_YEAR_ASSETS_ID = "expectedFirstYearAssets";

    public static final String COL_COMMISSIONS_DEPOSIT_TR_ID = "commissionsDepositTR";

    public static final String COL_COMMISSIONS_DEPOSIT_REG_ID = "commissionsDepositReg";

    public static final String COL_COMMISSIONS_ASSET_AB_ID = "commissionsAssetAB";

    public static final String COL_COMMISSIONS_PRICE_CREDIT_ID = "commissionsPriceCredit";

    public static final String COL_PRODUCER_CODES_OF_BROKERS_ID = "producerCodesOfBrokers";

    public static final String COL_NAMES_OF_THE_BROKERS_ID = "namesOfTheBrokers";

    public static final String COL_BDFIRM_NAME_ID = "firmName";

    public static final String COL_RVP_ID = "rvpName";

    public static final String COL_PRODUCT_TYPE_ID = "productType";

    public static final String COL_US_OR_NY_ID = "usOrNy";

    public static final String COL_CLASS_ID = "class";

    public static final String COL_DISCONTINUED_DATE_ID = "discontinuedDate";

    public static final String DEFAULT_SORT_COLUMN_NAME = COL_CONTRACT_NAME_ID;
    
    // CSF Values.
    public static final String FIDUCIARY_WARRANTY_VALUE = "fiduciaryWarranty";

    public static final String PBA_VALUE = "pba";

    public static final String LOANS_VALUE = "loans";

    public static final String GIFL_VALUE = "gifl";
    
    public static final String MA_VALUE = "ma";
    
  	public static final String SFC_VALUE = "signatureFiduciaryConnect";
  	   
    public static final String PEP_VALUE = "pooledEmployerPlan";

    public static final String ROTH_VALUE = "roth";

    public static final String EZINCREASE_VALUE = "ezIncrease";

    public static final String EZSTART_VALUE = "ezStart";

    public static final String PLAN_CHECK_VALUE = "planCheck";

    public static final String EZGOALS_VALUE = "ezGoal";

    public static final String DF_FUND_FAMILY_CODE = "dfFundFamilyCode";
    
    public static final String DF_MONEY_MARKET_FUNDS_VALUE = "dfMoneyMarketFunds";

    public static final String DF_STABLE_VALUE_FUNDS_VALUE = "dfStableValueFunds";

    public static final String DF_OTHERS_VALUE = "dfOthers";

    public static final String IPSM_VALUE = "ipsManager";
    
    public static final String JH_PASSIVE_TRUSTEE_SERVICE_VALUE = "jhPassiveTrusteeService";
    
    public static final String TOTAL_CARE_VALUE = "totalCare";
    
    public static final String WILSHIRE321_SERVICE = "wilshire321";
    
   
    public static final String SYSTEMATIC_WTTHDRAWAL_SERVICE = "syswithdrawal";
    
    public static final String SEND_SERVICE = "sendService";
    
    public static final String WILSHIRE338_SERVICE = "wilshire338";

    public static final String JHI_INVESTMENT_SELECTION_VALUE = "jhinvestmentselection";
    
    public static final String JH_SVGIF_CREDIT_VALUE = "jhsvgifcredit";
       
    public static final String JH_CREDITS_PROGRAM_VALUE = "jhcreditsprogram";
    
    // User Roles
    public static final String NATIONAL_ACCT_USERROLE = "NACCT";

    public static final String ADMINISTRATOR_USERROLE = "FLDADM";

    public static final String CONTENT_MGR_USERROLE = "CMAMGR";

    public static final String FIELD_USERROLE = "FIELD";

    public static final String RVP_USERROLE = "RVP";
    
    public static final String RUM_USERROLE = "RUM";
    
    public static final String COMMISSION_ASSET_1YR = "commissionAsset1Year";
    
    public static final String COMMISSION_ASSET_REN = "commissionAssetRen";
    
    public static final String COMMISSION_DEPOSIT_REG_1YR = "commissionDepositReg1Yr";
    
    public static final String COMMISSION_DEPOSIT_TR_1YR = "commissionDepositTr1yr";
    
    public static final String COMMISSION_DEPOSIT_TR_REN = "commissionDepositTrRen";
    
    public static final String COMMISSION_DEPOSIT_REG_REN = "commissionDepositRegRen";
    
    public static final String COMMISSION_ASSET_ALL_YRS = "commissionAssetAllYrs";
    
    public static final String RIA_FLAT_PRORATA_FEE_AMT = "riaFlatFeeProrata";
    
    public static final String RIA_FLAT_PER_HEAD_FEE_AMT = "riaFlatFeePerHead";
    
    public static final String RIA_ASSET_BASED_BPS_FEE_AMT = "riaBps";
    
    public static final String RIA_ASSET_BASED_BPS_MAX_FEE_AMT = "riaBpsMax";
    
    public static final String RIA_ASSET_BASED_BLENDED_FEE_AMT = "riaAcBlend";
    
    public static final String RIA_ASSET_BASED_TIERED_FEE_AMT = "riaAcTiered";
    
    public static final String RIA_338_DESIGNATION_INDICATOR = "des338Ind";
    
    public static final String FR_CONTRACT_SHARE_LIST = "frContractShareList";
    
    public static final String TRUSTEE_NAME = "trusteeName";
    
    public static final String COL_COMMISSIONS_RIA_FEES_ID = "commissionsRIAFees";
    
    public static final String RIA_ASSET_BASED_BPS_MIN_FEE_AMT = "riaBpsMin";
    
    public static final String COFID_321_ASSET_BASED_BPS_FEE = "cofid321ABFee";
    
    public static final String COFID_321_DOLLAR_BASED_FEE_AMT = "cofid321DBFee";
    
    public static final String COFID_338_ASSET_BASED_BPS_FEE = "cofid338ABFee";
    
    public static final String COFID_338_DOLLAR_BASED_FEE_AMT = "cofid338DBFee";

	public static final String UBS321_SERVICE = "ubs321";
	
	public static final String RJ_338_SERVICE = "rjames338";

	public static final String SERVICE_PROVIDER_CODE = "SERVICE_PROVIDER_CODE";
       
    // This will hold the BOB summary info.
    private BlockOfBusinessSummaryVO bobSummaryVO;
    
    private Integer dbSessionID;
    
    private Boolean resultTooBigInd;

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

    public BlockOfBusinessSummaryVO getBobSummaryVO() {
        return bobSummaryVO;
    }

    public void setBobSummaryVO(BlockOfBusinessSummaryVO bobSummaryVO) {
        this.bobSummaryVO = bobSummaryVO;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
