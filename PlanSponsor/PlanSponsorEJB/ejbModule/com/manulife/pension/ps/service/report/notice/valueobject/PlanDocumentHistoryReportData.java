package com.manulife.pension.ps.service.report.notice.valueobject;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.manulife.pension.ps.service.report.notice.reporthandler.PlanDocumentHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * 
 * @author krishta
 *
 */
public class PlanDocumentHistoryReportData extends ReportData {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String REPORT_ID = PlanDocumentHistoryReportHandler.class.getName();

    public static final String REPORT_NAME = "planNoticeHistoryDocumentReport";
    
  //The following attributes for common filter criteria
  	
  	public static final String FILTER_USER_PROFILE_ID = "profileId";
  	/**
  	 * The criteria filter parameter for from date. (A Date object)
  	 */
  	public static final String FILTER_FROM_DATE = "fromDate";

  	/**
  	 * The report criteria filter parameter for to date. (A Date object)
  	 */
  	public static final String FILTER_TO_DATE = "toDate";

  	/**
  	 * The criteria filter parameter for Action Change
  	 */
  	public static final String FILTER_ACTION_CHANGE = "actionChange";
  	
  	
    
    /** The filter parameter for a String object contract number */
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";

	/** The filter parameter for the task */
	public static final String FILTER_TASK = "task";
	
	public static final String TASK_PRINT = "print";
	
	public static final String TASK_DOWNLOAD = "download";
	
	
	public static final String USER_FIRST_NAME_FIELD = "firstName";
	public static final String USER_LAST_NAME_FIELD = "lastName";
    public static final String DOCUMENT_NAME = "documentName";
    public static final String ACTION_DATE_FIELD = "actionDate";
    public static final String ACTION_TAKEN_FIELD = "actionChange";
    public static final String DOCUMENT_FIELD = "documentName";
    public static final String REVISED_DOCUMENT_FIELD = "replacedfileName";
    public static final String POST_TO_PPT_FIELD = "changedPPT";
    
    public static final String FILTER_USER_NAME = "userName";
    
    private List<PlanNoticeDocumentChangeHistoryVO> planNoticeDocumentChangeHistorys;
    
    private List<LookupDescription> planNoticeDocumentChangeTypes;
	private List<LookupDescription> planNoticeDocumentName;
    
   
    
  private LinkedHashMap<BigDecimal,LookupDescription> userProfileDetails;
  

	private LinkedHashMap<Integer,String> user;
    
    
    public LinkedHashMap<Integer, String> getUser() {
		return user;
	}


	public void setUser(LinkedHashMap<Integer, String> user) {
		this.user = user;
	}


	private Integer contractNo;
	
	private Date display;
	
    
    /**
	 * @return the display
	 */
	public Date getDisplay() {
		return display;
	}


	/**
	 * @param display the display to set
	 */
	public void setDisplay(Date display) {
		this.display = display;
	}


	private boolean userAccessPermission;
    
    
    /**
	 * Constructor
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public PlanDocumentHistoryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
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
	 * @return the userProfileDetails
	 */
	public LinkedHashMap<BigDecimal, LookupDescription> getUserProfileDetails() {
		return userProfileDetails;
	}


	/**
	 * @param userProfileDetails the userProfileDetails to set
	 */
	public void setUserProfileDetails(
			LinkedHashMap<BigDecimal, LookupDescription> userProfileDetails) {
		this.userProfileDetails = userProfileDetails;
	}


	 /**
		 * @return the planNoticeDocumentName
		 */
		public List<LookupDescription> getPlanNoticeDocumentName() {
			return planNoticeDocumentName;
		}


		/**
		 * @param planNoticeDocumentName the planNoticeDocumentName to set
		 */
		public void setPlanNoticeDocumentName(
				List<LookupDescription> planNoticeDocumentName) {
			this.planNoticeDocumentName = planNoticeDocumentName;
		}


	
	
}
