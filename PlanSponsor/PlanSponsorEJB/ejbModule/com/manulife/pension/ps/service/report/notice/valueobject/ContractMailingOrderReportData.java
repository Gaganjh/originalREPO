package com.manulife.pension.ps.service.report.notice.valueobject;

import java.util.List;

import com.manulife.pension.ps.service.report.notice.reporthandler.ContractMailingOrderReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ContractMailingOrderReportData extends ReportData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String REPORT_ID = ContractMailingOrderReportHandler.class.getName();

    public static final String REPORT_NAME = "contractMailingOrderReport";
    
    /** The filter parameter for a String object contract number */
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

	/** The filter parameter for the task */
	public static final String FILTER_TASK = "task";
	
	public static final String TASK_PRINT = "print";
	
	
	public static final String MAILING_NAME = "mailingName";
	public static final String ORDER_NUMBER = "orderNumber";
	public static final String ORDER_STATUS = "orderStatus";
	public static final String ORDER_STATUS_DATE = "orderStatusDate";
    
    
    private List<PlanNoticeMailingOrderVO> planNoticeMailingOrders;
    
    private List<LookupDescription> contractNoticeOrderStatusCodes;
   
    
    private Integer profileId;
    
    private Integer contractNo;
    
    private boolean userAccessPermission;
    private String  termsOfUseInd;
    
    
    /**
	 * Constructor
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public ContractMailingOrderReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
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
	public boolean isUserAccessPermission() {
		return userAccessPermission;
	}


	/**
	 * @param userAccessPermission the userAccessPermission to set
	 */
	public void setUserAccessPermission(boolean userAccessPermission) {
		this.userAccessPermission = userAccessPermission;
	}


	/**
	 * @return the termsOfUseInd
	 */
	public String getTermsOfUseInd() {
		return termsOfUseInd;
	}


	/**
	 * @param termsOfUseInd the termsOfUseInd to set
	 */
	public void setTermsOfUseInd(String termsOfUseInd) {
		this.termsOfUseInd = termsOfUseInd;
	}

	
    
}
