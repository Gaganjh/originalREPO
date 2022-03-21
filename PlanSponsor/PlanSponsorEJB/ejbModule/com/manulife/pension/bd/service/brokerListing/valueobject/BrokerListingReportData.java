package com.manulife.pension.bd.service.brokerListing.valueobject;

import com.manulife.pension.bd.service.brokerListing.reporthandler.BrokerListingReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class will hold the Broker Listing Report Data.
 * 
 * @author HArlomte
 * 
 */
public class BrokerListingReportData extends ReportData {

    /**
     * Constructor
     */
    public BrokerListingReportData() {
    }

    /**
     * Constructor
     * 
     * @param criteria - Report Criteria
     * @param totalCount - total Count.
     */
    public BrokerListingReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
    }
    
    private static final long serialVersionUID = 5362587791232939338L;

    public static final String REPORT_ID = BrokerListingReportHandler.class.getName(); 

    // This string will be used for giving CSV File name for NBDW reports.
    public static final String CSV_REPORT_NAME = "FinancialRepList";

    // Filter ID's of the filters used in Broker Listing.
    public static final String FILTER_FINANCIALREP_NAME_ID = "financialRepName";

    // This holds the firm ID of the firm name displayed in the page.
    public static final String FILTER_BDFIRM_NAME_ID = "bdFirmName";
    
    // This holds the actual firm name as displayed in the page.
    public static final String FILTER_BROKER_DEALER_FIRM_NAME = "firmName";

    public static final String FILTER_CITY_NAME_ID = "cityName";

    public static final String FILTER_STATE_CODE_ID = "stateCode";

    public static final String FILTER_ZIP_CODE_ID = "zipCode";

    public static final String FILTER_PRODUCER_CODE_ID = "producerCode";

    public static final String FILTER_RVP_ID = "rvpName";

    public static final String FILTER_REGION_ID = "region";

    public static final String FILTER_DIVISION_ID = "division";

    // The following constants are used only in the Action class.
    public static final String FILTER_QF_FINANCIALREP_NAME_ID = "QFfinancialRepName";

    // This holds the firm ID of the firm name displayed in the page.
    public static final String FILTER_QF_BDFIRM_NAME_ID = "QFbdFirmName";

    // This holds the actual firm name as displayed in the page.
    public static final String FILTER_QF_BROKER_DEALER_FIRM_NAME = "QFfirmName";

    public static final String FILTER_QF_CITY_NAME_ID = "QFcityName";

    public static final String FILTER_QF_STATE_CODE_ID = "QFstateCode";

    public static final String FILTER_QF_ZIP_CODE_ID = "QFzipCode";

    public static final String FILTER_QF_PRODUCER_CODE_ID = "QFproducerCode";

    public static final String FILTER_QF_RVP_ID = "QFrvpName";

    public static final String FILTER_QF_REGION_ID = "QFregion";

    public static final String FILTER_QF_DIVISION_ID = "QFdivision";

    
    public static final String FILTER_AS_OF_DATE = "asOfDate";

    public static final String FILTER_USER_PROFILE_ID = "userProfileID";

    public static final String FILTER_MIMIC_USER_PROFILE_ID = "mimicUserProfileID";

    public static final String FILTER_USER_ROLE = "userRole";

    public static final String FILTER_MIMIC_USER_ROLE = "mimicUserRole";

    public static final String FILTER_PARTY_ID = "partyID";

    public static final String FILTER_DB_SESSION_ID = "dbSessionID";
    
    
    // Broker Listing COlumn IDs.
    public static final String COL_FINANCIAL_REP_NAME_ID = "financialRepName";

    public static final String COL_FIRM_NAME_ID = "firmName";

    public static final String COL_CITY_ID = "city";

    public static final String COL_STATE_ID = "stateName";

    public static final String COL_ZIP_CODE_ID = "zipCode";

    public static final String COL_PRODUCER_CODE_ID = "producerCode";

    public static final String COL_NUM_OF_CONTRACTS_ID = "numOfContracts";

    public static final String COL_BL_TOTAL_ASSETS_ID = "totalAssets";
    
    // The Stored Proc Session ID recieved from stored proc call.
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

    // Broker Listing Summary Information.
    BrokerListingSummaryVO brokerListingSummaryVO;

    public BrokerListingSummaryVO getBrokerListingSummaryVO() {
        return brokerListingSummaryVO;
    }

    public void setBrokerListingSummaryVO(BrokerListingSummaryVO brokerListingSummaryVO) {
        this.brokerListingSummaryVO = brokerListingSummaryVO;
    }

}
