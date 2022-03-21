package com.manulife.pension.ps.service.report.noticereports.valueobject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.FastDateFormat;
import com.manulife.pension.ps.service.report.noticereports.reporthandler.PlanSponsorWebsiteReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Provides report data for Notice Manager Upload and Share Report.
 * 
 */
public class UploadAndShareReportData extends ReportData {

    private static final long serialVersionUID = -343639867318332035L;

    public static final String REPORT_NAME = "Upload and Share as of";

    /** The filter parameter for a String object contract number */
    public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

    /** The filter parameter for the fromDate */
    public static final String FILTER_FROM_DATE = "fromDate";

    /** The filter parameter for the toDate */
    public static final String FILTER_TO_DATE = "toDate";

    /** The filter parameter for the task */
    public static final String FILTER_TASK = "task";

    public static final String TASK_PRINT = "print";
    
    //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public FastDateFormat format = FastDateFormat.getInstance("MMM dd, yyyy"); 
    
    public static final String PS_NEW_BUSINESS_USER_CATEGORY = "PS - New Business";
    public static final String PS_INFORCE_USER_CATEGORY = "PS - Inforce";
	public static final String TPA_USER_CATEGORY = "TPA";
    public static final String INTERMEDIARY_CONTACT_USER_CATEGORY = "Intermediary Contact";
	public static final String TOTAL_CARE_USER_CATEGORY = "TotalCare TPA";

    private Integer contractNo;

    private Date fromDate;

    private Date toDate;

    private Integer numberOfContractsUsingService;

    private Integer numberOfUsersUsingService;

    private Integer numberOfContractsThatPostedToParticipantWebsite;

    private Integer numberOfContractsThatUploadedNotices;

    private BigDecimal percentageOfContractsUsingShare;

    private Integer numberOfUsersAcceptedTermsOfUse;

    private Integer numberOfUsersNotAcceptedTermsOfUse;

    private Integer numberOfNoticesUploadedAndReplaced;

    private BigDecimal avgNumberOfDocumentsUploadedPerContract;

    private Integer numberOfDocumentsUploaded;

    private Integer numberOfDocumentsRenamed;

    private Integer numberOfDocumentsReplaced;

    private Integer numberOfDocumentsDeleted;

    private Integer numberDocumentsChangedAndReplaced;

    private Integer numberOfDocumentsSharedWithParticipants;

    private BigDecimal percentageOfDocumentsSharedWithParticipants;

    private List<UploadAndShareReportSourceOfUploadShareVO> sourceOfUploadList;

    private List<UploadAndShareReportSourceOfUploadShareVO> sourceOfShareList;

    private List<UploadAndShareReportTopTenDocumentNamesVO> topTenDocumentNamesList;

    /**
     * Constructor
     * 
     * @param criteria
     * @param totalCount
     */
    public UploadAndShareReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);

    }

    public UploadAndShareReportData() {
        super(new ReportCriteria(PlanSponsorWebsiteReportHandler.REPORT_ID), 0);

    }


    /**
	 * @return the currentDate
	 */
	public String getCurrentDate() {
		return format.format(Calendar.getInstance().getTime());
	}

    /**
     * @return the contractNo
     */
    public Integer getContractNo() {
        return contractNo;
    }

    /**
     * @param contractNo the contractNo to set
     */
    public void setContractNo(Integer contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * @return the fromDate
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * @param fromDate the fromDate to set
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * @return the toDate
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * @param toDate the toDate to set
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * Returns the number of contracts using the service.
     * 
     * @return numberOfContractsUsingService
     */
    public Integer getNumberOfContractsUsingService() {
        return numberOfContractsUsingService;
    }

    /**
     * Sets the number of contracts using the service.
     * 
     * @param numberOfContractsUsingService
     */
    public void setNumberOfContractsUsingService(Integer numberOfContractsUsingService) {
        this.numberOfContractsUsingService = numberOfContractsUsingService;
    }

    /**
     * Returns the number of users using the service.
     * 
     * @return numberOfUsersUsingService
     */
    public Integer getNumberOfUsersUsingService() {
        return numberOfUsersUsingService;
    }

    /**
     * Sets the number of users using the service.
     * 
     * @param numberOfUsersUsingService
     */
    public void setNumberOfUsersUsingService(Integer numberOfUsersUsingService) {
        this.numberOfUsersUsingService = numberOfUsersUsingService;
    }

    /**
     * Returns Number Of Contracts That Posted To Participant Web site.
     * 
     * @return numberOfContractsThatPostedToParticipantWebsite
     */
    public Integer getNumberOfContractsThatPostedToParticipantWebsite() {
        return numberOfContractsThatPostedToParticipantWebsite;
    }

    /**
     * Sets Number Of Contracts That Posted To Participant Web site.
     * 
     * @param numberOfContractsThatPostedToParticipantWebsite
     */
    public void setNumberOfContractsThatPostedToParticipantWebsite(
            Integer numberOfContractsThatPostedToParticipantWebsite) {
        this.numberOfContractsThatPostedToParticipantWebsite = numberOfContractsThatPostedToParticipantWebsite;
    }

    /**
     * Returns total Number Of Contracts That Uploaded Custom Notices.
     * 
     * @return numberOfContractsThatUploadedNotices
     */
    public Integer getNumberOfContractsThatUploadedNotices() {
        return numberOfContractsThatUploadedNotices;
    }

    /**
     * Sets total Number Of Contracts That Uploaded Custom Notices.
     * 
     * @param numberOfContractsThatUploadedNotices
     */
    public void setNumberOfContractsThatUploadedNotices(Integer numberOfContractsThatUploadedNotices) {
        this.numberOfContractsThatUploadedNotices = numberOfContractsThatUploadedNotices;
    }

    /**
     * Returns Percentage Of Contracts Using Share
     * 
     * @return percentageOfContractsUsingShare
     */
    public BigDecimal getPercentageOfContractsUsingShare() {
        return percentageOfContractsUsingShare;
    }

    /**
     * Sets Percentage Of Contracts Using Share
     * 
     * @param percentageOfContractsUsingShare
     */
    public void setPercentageOfContractsUsingShare(BigDecimal percentageOfContractsUsingShare) {
        this.percentageOfContractsUsingShare = percentageOfContractsUsingShare;
    }

    /**
     * Returns Number Of Users Accepted Terms Of Use
     * 
     * @return numberOfUsersAcceptedTermsOfUse.
     */
    public Integer getNumberOfUsersAcceptedTermsOfUse() {
        return numberOfUsersAcceptedTermsOfUse;
    }

    /**
     * Sets Number Of Users Accepted Terms Of Use
     * 
     * @param numberOfUsersAcceptedTermsOfUse
     */
    public void setNumberOfUsersAcceptedTermsOfUse(Integer numberOfUsersAcceptedTermsOfUse) {
        this.numberOfUsersAcceptedTermsOfUse = numberOfUsersAcceptedTermsOfUse;
    }

    /**
     * Returns Number Of Users Not Accepted Terms Of Use
     * 
     * @return numberOfUsersAcceptedTermsOfUse.
     */
    public Integer getNumberOfUsersNotAcceptedTermsOfUse() {
        return numberOfUsersNotAcceptedTermsOfUse;
    }

    /**
     * Sets Number Of Users Not Accepted Terms Of Use
     * 
     * @param numberOfUsersAcceptedTermsOfUse
     */
    public void setNumberOfUsersNotAcceptedTermsOfUse(Integer numberOfUsersNotAcceptedTermsOfUse) {
        this.numberOfUsersNotAcceptedTermsOfUse = numberOfUsersNotAcceptedTermsOfUse;
    }

    /**
     * Returns Total number of custom notices uploaded and replaced
     * 
     * @return numberOfNoticesUploadedAndReplaced.
     */
    public Integer getNumberOfNoticesUploadedAndReplaced() {
        return numberOfNoticesUploadedAndReplaced;
    }

    /**
     * Sets Total number of custom notices uploaded and replaced
     * 
     * @param numberOfNoticesUploadedAndReplaced
     */
    public void setNumberOfNoticesUploadedAndReplaced(Integer numberOfNoticesUploadedAndReplaced) {
        this.numberOfNoticesUploadedAndReplaced = numberOfNoticesUploadedAndReplaced;
    }

    /**
     * Returns Average no. of documents uploaded per contract
     * 
     * @return avgNumberOfDocumentsUploadedPerContract
     */
    public BigDecimal getAvgNumberOfDocumentsUploadedPerContract() {
        return avgNumberOfDocumentsUploadedPerContract;
    }

    /**
     * Sets Average no. of documents uploaded per contract
     * 
     * @param avgNumberOfDocumentsUploadedPerContract
     */
    public void setAvgNumberOfDocumentsUploadedPerContract(
            BigDecimal avgNumberOfDocumentsUploadedPerContract) {
        this.avgNumberOfDocumentsUploadedPerContract = avgNumberOfDocumentsUploadedPerContract;
    }

    /**
     * Returns total number of Documents uploaded.
     * 
     * @return numberOfDocumentsUploaded
     */
    public Integer getNumberOfDocumentsUploaded() {
        return numberOfDocumentsUploaded;
    }

    /**
     * Sets total number of Documents uploaded.
     * 
     * @param setNumberOfDocumentsUploaded
     */
    public void setNumberOfDocumentsUploaded(Integer numberOfDocumentsUploaded) {
        this.numberOfDocumentsUploaded = numberOfDocumentsUploaded;
    }

    /**
     * Gets Number Of Documents Renamed.
     * 
     * @return getNumberOfDocumentsRenamed
     */
    public Integer getNumberOfDocumentsRenamed() {
        return numberOfDocumentsRenamed;
    }

    /**
     * Sets Number Of Documents Renamed.
     * 
     * @param numberOfDocumentsRenamed
     */
    public void setNumberOfDocumentsRenamed(Integer numberOfDocumentsRenamed) {
        this.numberOfDocumentsRenamed = numberOfDocumentsRenamed;
    }

    /**
     * Gets Number Of Documents Replaced.
     * 
     * @return numberOfDocumentsReplaced
     */
    public Integer getNumberOfDocumentsReplaced() {
        return numberOfDocumentsReplaced;
    }

    /**
     * Sets Number Of Documents Replaced.
     * 
     * @param numberOfDocumentsReplaced
     */
    public void setNumberOfDocumentsReplaced(Integer numberOfDocumentsReplaced) {
        this.numberOfDocumentsReplaced = numberOfDocumentsReplaced;
    }

    /**
     * Gets Number Of Documents Deleted.
     * 
     * @return numberOfDocumentsDeleted.
     */
    public Integer getNumberOfDocumentsDeleted() {
        return numberOfDocumentsDeleted;
    }

    /**
     * Sets Number Of Documents Deleted.
     * 
     * @param numberOfDocumentsDeleted
     */
    public void setNumberOfDocumentsDeleted(Integer numberOfDocumentsDeleted) {
        this.numberOfDocumentsDeleted = numberOfDocumentsDeleted;
    }

    /**
     * Gets Number Documents Changed And Replaced.
     * 
     * @return numberDocumentsChangedAndReplaced
     */
    public Integer getNumberDocumentsChangedAndReplaced() {
        return numberDocumentsChangedAndReplaced;
    }

    /**
     * Sets Number Documents Changed And Replaced.
     * 
     * @param numberDocumentsChangedAndReplaced
     */
    public void setNumberDocumentsChangedAndReplaced(Integer numberDocumentsChangedAndReplaced) {
        this.numberDocumentsChangedAndReplaced = numberDocumentsChangedAndReplaced;
    }

    /**
     * Gets number Of Documents Shared With Participants.
     * 
     * @return numberOfDocumentsSharedWithParticipants
     */
    public Integer getNumberOfDocumentsSharedWithParticipants() {
        return numberOfDocumentsSharedWithParticipants;
    }

    /**
     * Sets number Of Documents Shared With Participants.
     * 
     * @param numberOfDocumentsSharedWithParticipants
     */
    public void setNumberOfDocumentsSharedWithParticipants(
            Integer numberOfDocumentsSharedWithParticipants) {
        this.numberOfDocumentsSharedWithParticipants = numberOfDocumentsSharedWithParticipants;
    }

    /**
     * Gets percentage Of Documents Shared With Participants.
     * 
     * @return numberOfDocumentsSharedWithParticipants
     */
    public BigDecimal getPercentageOfDocumentsSharedWithParticipants() {
        return percentageOfDocumentsSharedWithParticipants;
    }

    /**
     * Sets percentage Of Documents Shared With Participants.
     * 
     * @param percentageOfDocumentsSharedWithParticipants
     */
    public void setPercentageOfDocumentsSharedWithParticipants(
            BigDecimal percentageOfDocumentsSharedWithParticipants) {
        this.percentageOfDocumentsSharedWithParticipants = percentageOfDocumentsSharedWithParticipants;
    }

    /**
     * Return source of upload document list by user category.
     * 
     * @return sourceOfUploadList
     */
    public List<UploadAndShareReportSourceOfUploadShareVO> getSourceOfUploadList() {
        return sourceOfUploadList;
    }

    /**
     * Sets source of upload document list by user category.
     * 
     * @param sourceOfUploadList
     */
    public void setSourceOfUploadList(
            List<UploadAndShareReportSourceOfUploadShareVO> sourceOfUploadList) {
        this.sourceOfUploadList = sourceOfUploadList;
    }

    /**
     * Returns list for source Of Share chart.
     * 
     * @return sourceOfShareList
     */
    public List<UploadAndShareReportSourceOfUploadShareVO> getSourceOfShareList() {
        return sourceOfShareList;
    }

    /**
     * Sets list for source Of Share chart.
     * 
     * @param sourceOfShareList
     */
    public void setSourceOfShareList(
            List<UploadAndShareReportSourceOfUploadShareVO> sourceOfShareList) {
        this.sourceOfShareList = sourceOfShareList;
    }

    /**
     * Returns top ten document names list.
     * 
     * @return topTenDocumentNamesList
     */
    public List<UploadAndShareReportTopTenDocumentNamesVO> getTopTenDocumentNamesList() {
        return topTenDocumentNamesList;
    }

    /**
     * Sets top ten document names list.
     * 
     * @param topTenDocumentNamesList
     */
    public void setTopTenDocumentNamesList(
            List<UploadAndShareReportTopTenDocumentNamesVO> topTenDocumentNamesList) {
        this.topTenDocumentNamesList = topTenDocumentNamesList;
    }

}
