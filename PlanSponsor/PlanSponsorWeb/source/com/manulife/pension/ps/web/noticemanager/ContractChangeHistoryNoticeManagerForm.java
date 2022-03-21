package com.manulife.pension.ps.web.noticemanager;


import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.FastDateFormat;


import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.platform.web.util.ContractDateHelper;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;

/**
 * 
 * @author krishta
 *
 */
public class ContractChangeHistoryNoticeManagerForm extends BaseReportForm {

	private static final long serialVersionUID = 1L;

	private String fromDate;


	private String defaultFromDate;

	private String toDate;

	private String defaultToDate;

	private Integer userId;
	private String actionType;
	private String documentType;
	private String documentName;	
	private String userName;
	private String task;
	private BigDecimal profileId;
	private List<LookupDescription> planNoticeDocumentChangeTypes;
	private LinkedHashMap<BigDecimal,LookupDescription> userProfileDetails;
	private String webPageTypeCode="CHANGE_HISTORY_PAGE"; 
	private boolean indicator;
	private FastDateFormat currentDateDisplay = ContractDateHelper.getDateFormatterLocale("MMM dd, yyyy", Locale.US);



	/**
	 * @return the indicator
	 */
	public boolean isIndicator() {
		return indicator;
	}




	/**
	 * @param indicator the indicator to set
	 */
	public void setIndicator(boolean indicator) {
		this.indicator = indicator;
	}




	/**
	 * @return the webPageTypeCode
	 */
	public String getWebPageTypeCode() {
		return webPageTypeCode;
	}




	/**
	 * @param webPageTypeCode the webPageTypeCode to set
	 */
	public void setWebPageTypeCode(String webPageTypeCode) {
		this.webPageTypeCode = webPageTypeCode;
	}






	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}




	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the documentType
	 */
	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}






	/**
	 * @return the todayDate
	 */
	public String getTodayDate() {
		return currentDateDisplay.format(Calendar.getInstance().getTime());
	}



	/**
	 * @return the task
	 */
	public String getTask() {
		return task;
	}




	/**
	 * @param task the task to set
	 */
	public void setTask(String task) {
		this.task = task;
	}




	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}




	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}




	/**
	 * @return the defaultFromDate
	 */
	public String getDefaultFromDate() {
		return defaultFromDate;
	}




	/**
	 * @param defaultFromDate the defaultFromDate to set
	 */
	public void setDefaultFromDate(String defaultFromDate) {
		this.defaultFromDate = defaultFromDate;
	}




	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}




	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}




	/**
	 * @return the defaultToDate
	 */
	public String getDefaultToDate() {
		return defaultToDate;
	}




	/**
	 * @param defaultToDate the defaultToDate to set
	 */
	public void setDefaultToDate(String defaultToDate) {
		this.defaultToDate = defaultToDate;
	}




	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}




	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}




	/**
	 * @return the actionType
	 */
	public String getActionType() {
		return actionType;
	}




	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}





	public String getDocumentName() {
		return documentName;
	}





	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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
	 * @return the profileId
	 */
	public BigDecimal getProfileId() {
		return profileId;
	}




	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(BigDecimal profileId) {
		this.profileId = profileId;
	}






	/**
	 * @param documentType the documentType to set
	 */

	public void reset( HttpServletRequest request) {
		//super.reset( request);
	}


}
