package com.manulife.pension.ps.service.report.notice.valueobject;

import java.util.Date;
import java.util.List;

import com.manulife.pension.ps.service.report.notice.reporthandler.PlanDocumentReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * 
 * @author krishta
 *
 */
public class PlanDocumentReportData extends ReportData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String REPORT_ID = PlanDocumentReportHandler.class.getName();

    public static final String REPORT_NAME = "planNoticeDocumentReport";
    
    /** The filter parameter for a String object contract number */
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	
	/** The filter parameter for a String object contract number */
	public static final String FILTER_USER_PROFILE_ID= "profileId";

	/** The filter parameter for the task */
	public static final String FILTER_TASK = "task";
	
	public static final String TASK_PRINT = "print";
	
	/** The filter parameter for the asOfDate */
	public static final String FILTER_AS_OF_DATE = "asOfDate";
	
	public static final String SORT_FIELD_DATE = "displaysortNumber";
	
	public static final String SORT_FIELD_DISPLAY_SORT_NUMBER = "displaysortNumber";
    
    private List<PlanNoticeDocumentVO> customPlanNoticeDocuments;
    
    private List<PlanNoticeDocumentVO> jhPlanNoticeDocuments;
    
    private List<PlanNoticeDocumentChangeHistoryVO> planNoticeDocumentChangeHistorys;
    
    private List<LookupDescription> planNoticeDocumentChangeTypes;
    
    private List<PlanNoticeMailingOrderVO> planNoticeMailingOrders;
    
    private List<LookupDescription> userManagerAlertFrequencyCodes;
    
    private List<LookupDescription> userManagerAlertTimingCodes;
    
    private List<LookupDescription> contractNoticeOrderStatusCodes;
   
    
    private Integer profileId;
    
    private Integer contractNo;
    
    private Date fromDate;
    
    private Date toDate;
    
    private boolean termOfUseInd;
    
    private String  termsOfUseCode;
    
    
    /**
	 * Constructor
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public PlanDocumentReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * @return the customPlanNoticeDocuments
	 */
	public List<PlanNoticeDocumentVO> getCustomPlanNoticeDocuments() {
		return customPlanNoticeDocuments;
	}

	/**
	 * @param customPlanNoticeDocuments the customPlanNoticeDocuments to set
	 */
	public void setCustomPlanNoticeDocuments(
			List<PlanNoticeDocumentVO> customPlanNoticeDocuments) {
		this.customPlanNoticeDocuments = customPlanNoticeDocuments;
	}

	/**
	 * @return the jhPlanNoticeDocuments
	 */
	public List<PlanNoticeDocumentVO> getJhPlanNoticeDocuments() {
		return jhPlanNoticeDocuments;
	}

	/**
	 * @param jhPlanNoticeDocuments the jhPlanNoticeDocuments to set
	 */
	public void setJhPlanNoticeDocuments(
			List<PlanNoticeDocumentVO> jhPlanNoticeDocuments) {
		this.jhPlanNoticeDocuments = jhPlanNoticeDocuments;
	}

	/**
	 * @return the planNoticeDocumentChangeHistorys
	 */
	public List<PlanNoticeDocumentChangeHistoryVO> getPlanNoticeDocumentChangeHistorys() {
		return planNoticeDocumentChangeHistorys;
	}

	/**
	 * @param planNoticeDocumentChangeHistorys the planNoticeDocumentChangeHistorys to set
	 */
	public void setPlanNoticeDocumentChangeHistorys(
			List<PlanNoticeDocumentChangeHistoryVO> planNoticeDocumentChangeHistorys) {
		this.planNoticeDocumentChangeHistorys = planNoticeDocumentChangeHistorys;
	}

	/**
	 * @return the planNoticeDocumentChangeTypes
	 */
	public List<LookupDescription> getPlanNoticeDocumentChangeTypes() {
		return planNoticeDocumentChangeTypes;
	}

	/**
	 * @param planNoticeDocumentChangeTypes the planNoticeDocumentChangeTypes to set
	 */
	public void setPlanNoticeDocumentChangeTypes(
			List<LookupDescription> planNoticeDocumentChangeTypes) {
		this.planNoticeDocumentChangeTypes = planNoticeDocumentChangeTypes;
	}

	/**
	 * @return the planNoticeMailingOrders
	 */
	public List<PlanNoticeMailingOrderVO> getPlanNoticeMailingOrders() {
		return planNoticeMailingOrders;
	}

	/**
	 * @param planNoticeMailingOrders the planNoticeMailingOrders to set
	 */
	public void setPlanNoticeMailingOrders(
			List<PlanNoticeMailingOrderVO> planNoticeMailingOrders) {
		this.planNoticeMailingOrders = planNoticeMailingOrders;
	}

	
	/**
	 * @return the userManagerAlertFrequencyCodes
	 */
	public List<LookupDescription> getUserManagerAlertFrequencyCodes() {
		return userManagerAlertFrequencyCodes;
	}

	/**
	 * @param userManagerAlertFrequencyCodes the userManagerAlertFrequencyCodes to set
	 */
	public void setUserManagerAlertFrequencyCodes(
			List<LookupDescription> userManagerAlertFrequencyCodes) {
		this.userManagerAlertFrequencyCodes = userManagerAlertFrequencyCodes;
	}

	/**
	 * @return the userManagerAlertTimingCodes
	 */
	public List<LookupDescription> getUserManagerAlertTimingCodes() {
		return userManagerAlertTimingCodes;
	}

	/**
	 * @param userManagerAlertTimingCodes the userManagerAlertTimingCodes to set
	 */
	public void setUserManagerAlertTimingCodes(
			List<LookupDescription> userManagerAlertTimingCodes) {
		this.userManagerAlertTimingCodes = userManagerAlertTimingCodes;
	}

	/**
	 * @return the contractNoticeOrderStatusCodes
	 */
	public List<LookupDescription> getContractNoticeOrderStatusCodes() {
		return contractNoticeOrderStatusCodes;
	}

	/**
	 * @param contractNoticeOrderStatusCodes the contractNoticeOrderStatusCodes to set
	 */
	public void setContractNoticeOrderStatusCodes(
			List<LookupDescription> contractNoticeOrderStatusCodes) {
		this.contractNoticeOrderStatusCodes = contractNoticeOrderStatusCodes;
	}

	/**
	 * @return the profileId
	 */
	public Integer getProfileId() {
		return profileId;
	}

	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
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
	 * @return the userAccessPermission
	 */
	public boolean isTermOfUseAccepted() {
		return termOfUseInd;
	}

	/**
	 * @param userAccessPermission the userAccessPermission to set
	 */
	public void setTermOfUseAcceptance(boolean termOfUseInd) {
		this.termOfUseInd = termOfUseInd;
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
	 * @return the termsOfUseCode
	 */
	public String getTermsOfUseCode() {
		return termsOfUseCode;
	}

	/**
	 * @param termsOfUseCode the termsOfUseCode to set
	 */
	public void setTermsOfUseCode(String termsOfUseCode) {
		this.termsOfUseCode = termsOfUseCode;
	}

	
	
    
}
