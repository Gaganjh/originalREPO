package com.manulife.pension.ps.web.noticemanager;

import java.math.BigDecimal;
import java.util.List;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
/**
 *  UploadSharedNoticeManagerForm is used to set and get the custom plandocument data
 * @author Murali Vishwanadula
 *
 */
public class UploadSharedNoticeManagerForm extends BaseReportForm {
	private static final long serialVersionUID = 1L;
	private Integer contractId;
	private Integer documentId;
	private String planDocumentFileName;
	private String task;
	private String planNoticedocument;
	private String contractStatus;
	private String methodname;
	private String planDocumentName;
	private String postToPptInd;
	private String documentsOrder;
	private String sortTypeArrow;
	private String userTermsAndAcceptanceInd;
	private List<PlanNoticeDocumentVO> planNoticeDocuments;
	private PlanDocumentReportData report = null;
	private boolean noticeManagerAccessPermissions;
	private boolean tpaUser;
	private boolean bundledGAApprover;
	private boolean externalUser;
	private boolean internalUser;
	private boolean userAccessPermission;
	private boolean confirmIndicator;
	private boolean documentLocked;
	private boolean show404section;
	private boolean showPlanInvestmentNotice;
	private boolean showICC;
	private boolean showMissingIccContactMessage;
	private boolean showIccCalendarYearMessage;
	private boolean showPlanHighlight;
	private boolean documentDisplyOrderChanges;
	private boolean addDocumentPermission;
	private boolean uploadDocumentPostedCount;
	private boolean showActionButon = false;
	private boolean disableTheResetButton;
	private boolean disableTheDownSortArrow;
	private BigDecimal profileId;
	private Boolean sortAppliedInd = false;
	private boolean showPlanAndInvestmentReplaceMessage = false;
	private Boolean uploadAndShareTab = false;
	private Boolean buildYourPackageTab = false;
	private Boolean buildYourPackageNPTab = false;
	private Boolean orderStatusTab = false;
	private Boolean uploadAndSharePageInd = false;
	private Boolean noDocumentAvailableInd = false;
	private Boolean customSortInd = false;
	
	/**
	 * @return the contractStatus
	 */
	public String getContractStatus() {
		return contractStatus;
	}
	/**
	 * @param contractStatus the contractStatus to set
	 */
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	/**
	 * @return the customSortInd
	 */
	public Boolean getCustomSortInd() {
		return customSortInd;
	}
	/**
	 * @param customSortInd the customSortInd to set
	 */
	public void setCustomSortInd(Boolean customSortInd) {
		this.customSortInd = customSortInd;
	}
	/**
	 * @return the showPlanAndInvestmentReplaceMessage
	 */
	public boolean isShowPlanAndInvestmentReplaceMessage() {
		return showPlanAndInvestmentReplaceMessage;
	}
	/**
	 * @param showPlanAndInvestmentReplaceMessage the showPlanAndInvestmentReplaceMessage to set
	 */
	public void setShowPlanAndInvestmentReplaceMessage(
			boolean showPlanAndInvestmentReplaceMessage) {
		this.showPlanAndInvestmentReplaceMessage = showPlanAndInvestmentReplaceMessage;
	}
	public Boolean getNoDocumentAvailableInd() {
		return noDocumentAvailableInd;
	}
	public void setNoDocumentAvailableInd(Boolean noDocumentAvailableInd) {
		this.noDocumentAvailableInd = noDocumentAvailableInd;
	}
	/**
	 * @return the sortAppliedInd
	 */
	public Boolean getSortAppliedInd() {
		return sortAppliedInd;
	}
	/**
	 * @param sortAppliedInd the sortAppliedInd to set
	 */
	public void setSortAppliedInd(Boolean sortAppliedInd) {
		this.sortAppliedInd = sortAppliedInd;
	}
	private boolean logged;
	private String webPageTypeCode="Upload & Share";
	/**
	 * @return the orderStatusTab
	 */
	public Boolean getOrderStatusTab() {
		return orderStatusTab;
	}
	/**
	 * @param orderStatusTab the orderStatusTab to set
	 */
	public void setOrderStatusTab(Boolean orderStatusTab) {
		this.orderStatusTab = orderStatusTab;
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
	 * @return the logged
	 */
	public boolean isLogged() {
		return logged;
	}
	/**
	 * @param logged the logged to set
	 */
	public void setLogged(boolean logged) {
		this.logged = logged;
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
	 * @return the planNoticedocument
	 */
	public String getPlanNoticedocument() {
		return planNoticedocument;
	}
	/**
	 * @param planNoticedocument the planNoticedocument to set
	 */
	public void setPlanNoticedocument(String planNoticedocument) {
		this.planNoticedocument = planNoticedocument;
	}
	/**
	 * @return the planDocumentFileName
	 */
	public String getPlanDocumentFileName() {
		return planDocumentFileName;
	}
	/**
	 * @param planDocumentFileName the planDocumentFileName to set
	 */
	public void setPlanDocumentFileName(String planDocumentFileName) {
		this.planDocumentFileName = planDocumentFileName;
	}
	/**
	 * @return the planDocumentName
	 */
	public String getPlanDocumentName() {
		return planDocumentName;
	}
	/**
	 * @param planDocumentName the planDocumentName to set
	 */
	public void setPlanDocumentName(String planDocumentName) {
		this.planDocumentName = planDocumentName;
	}
	/**
	 * @return the showActionButon
	 */
	public boolean isShowActionButon() {
		return showActionButon;
	}
	/**
	 * @param showActionButon the showActionButon to set
	 */
	public void setShowActionButon(boolean showActionButon) {
		this.showActionButon = showActionButon;
	}
	/**
	 * @return the disableTheDownSortArrow
	 */
	public boolean isDisableTheDownSortArrow() {
		return disableTheDownSortArrow;
	}
	/**
	 * @param disableTheDownSortArrow the disableTheDownSortArrow to set
	 */
	public void setDisableTheDownSortArrow(boolean disableTheDownSortArrow) {
		this.disableTheDownSortArrow = disableTheDownSortArrow;
	}
	/**
	 * @return the uploadDocumentPostedCount
	 */
	public boolean isUploadDocumentPostedCount() {
		return uploadDocumentPostedCount;
	}
	/**
	 * @param uploadDocumentPostedCount the uploadDocumentPostedCount to set
	 */
	public void setUploadDocumentPostedCount(boolean uploadDocumentPostedCount) {
		this.uploadDocumentPostedCount = uploadDocumentPostedCount;
	}
	/**
	 * @return the disableTheResetButton
	 */
	public boolean isDisableTheResetButton() {
		return disableTheResetButton;
	}
	/**
	 * @param disableTheResetButton the disableTheResetButton to set
	 */
	public void setDisableTheResetButton(boolean disableTheResetButton) {
		this.disableTheResetButton = disableTheResetButton;
	}
	/**
	 * @return the confirmIndicator
	 */
	public boolean isConfirmIndicator() {
		return confirmIndicator;
	}
	/**
	 * @param confirmIndicator the confirmIndicator to set
	 */
	public void setConfirmIndicator(boolean confirmIndicator) {
		this.confirmIndicator = confirmIndicator;
	}
	/**
	 * @return the sortTypeArrow
	 */
	public String getSortTypeArrow() {
		return sortTypeArrow;
	}
	/**
	 * @param sortTypeArrow the sortTypeArrow to set
	 */
	public void setSortTypeArrow(String sortTypeArrow) {
		this.sortTypeArrow = sortTypeArrow;
	}
	/**
	 * @return the documentDisplyOrderChanges
	 */
	public boolean isDocumentDisplyOrderChanges() {
		return documentDisplyOrderChanges;
	}
	/**
	 * @param documentDisplyOrderChanges the documentDisplyOrderChanges to set
	 */
	public void setDocumentDisplyOrderChanges(boolean documentDisplyOrderChanges) {
		this.documentDisplyOrderChanges = documentDisplyOrderChanges;
	}
	/**
	 * @return the documentsOrder
	 */
	public String getDocumentsOrder() {
		return documentsOrder;
	}
	/**
	 * @param documentsOrder the documentsOrder to set
	 */
	public void setDocumentsOrder(String documentsOrder) {
		this.documentsOrder = documentsOrder;
	}
	/**
	 * @return the userTermsAndAcceptanceInd
	 */
	public String getUserTermsAndAcceptanceInd() {
		return userTermsAndAcceptanceInd;
	}
	/**
	 * @param userTermsAndAcceptanceInd the userTermsAndAcceptanceInd to set
	 */
	public void setUserTermsAndAcceptanceInd(String userTermsAndAcceptanceInd) {
		this.userTermsAndAcceptanceInd = userTermsAndAcceptanceInd;
	}
	
	
	/**
	 * @return the contractId
	 */
	public Integer getContractId() {
		return contractId;
	}
	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	/**
	 * @return the documentId
	 */
	public Integer getDocumentId() {
		return documentId;
	}
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
	/**
	 * @return the postToPptInd
	 */
	public String getPostToPptInd() {
		return postToPptInd;
	}
	/**
	 * @param postToPptInd the postToPptInd to set
	 */
	public void setPostToPptInd(String postToPptInd) {
		this.postToPptInd = postToPptInd;
	}
	/**
	 * @return the documentLocked
	 */
	public boolean isDocumentLocked() {
		return documentLocked;
	}
	/**
	 * @param documentLocked the documentLocked to set
	 */
	public void setDocumentLocked(boolean documentLocked) {
		this.documentLocked = documentLocked;
	}

	
	/**
	 * @return the methodname
	 */
	public String getMethodname() {
		return methodname;
	}
	/**
	 * @param methodname the methodname to set
	 */
	public void setMethodname(String methodname) {
		this.methodname = methodname;
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
	 * @return the tpaUser
	 */
	public boolean isTpaUser() {
		return tpaUser;
	}
	/**
	 * @param tpaUser the tpaUser to set
	 */
	public void setTpaUser(boolean tpaUser) {
		this.tpaUser = tpaUser;
	}
	/**
	 * @return the bundledGAApprover
	 */
	public boolean isBundledGAApprover() {
		return bundledGAApprover;
	}
	/**
	 * @param bundledGAApprover the bundledGAApprover to set
	 */
	public void setBundledGAApprover(boolean bundledGAApprover) {
		this.bundledGAApprover = bundledGAApprover;
	}
	/**
	 * @return the externalUser
	 */
	public boolean isExternalUser() {
		return externalUser;
	}
	/**
	 * @param externalUser the externalUser to set
	 */
	public void setExternalUser(boolean externalUser) {
		this.externalUser = externalUser;
	}
	/**
	 * @return the internalUser
	 */
	public boolean isInternalUser() {
		return internalUser;
	}
	/**
	 * @param internalUser the internalUser to set
	 */
	public void setInternalUser(boolean internalUser) {
		this.internalUser = internalUser;
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
	 * @return the noticeManagerAccessPermissions
	 */
	public boolean isNoticeManagerAccessPermissions() {
		return noticeManagerAccessPermissions;
	}
	/**
	 * @param noticeManagerAccessPermissions the noticeManagerAccessPermissions to set
	 */
	public void setNoticeManagerAccessPermissions(boolean noticeManagerAccessPermissions) {
		this.noticeManagerAccessPermissions = noticeManagerAccessPermissions;
	}
	/**
	 * @return the planNoticeDocuments
	 */
	public List<PlanNoticeDocumentVO> getPlanNoticeDocuments() {
		return planNoticeDocuments;
	}
	/**
	 * @param planNoticeDocuments the planNoticeDocuments to set
	 */
	public void setPlanNoticeDocuments(
			List<PlanNoticeDocumentVO> planNoticeDocuments) {
		this.planNoticeDocuments = planNoticeDocuments;
	}
	/**
	 * @return the report
	 */
	public PlanDocumentReportData getReport() {
		return report;
	}
	/**
	 * @param report the report to set
	 */
	public void setReport(PlanDocumentReportData report) {
		this.report = report;
	}
	/**
	 * @return the show404section
	 */
	public boolean isShow404section() {
		return show404section;
	}
	/**
	 * @param show404section the show404section to set
	 */
	public void setShow404section(boolean show404section) {
		this.show404section = show404section;
	}
	/**
	 * @return the showPlanInvestmentNotice
	 */
	public boolean isShowPlanInvestmentNotice() {
		return showPlanInvestmentNotice;
	}
	/**
	 * @param showPlanInvestmentNotice the showPlanInvestmentNotice to set
	 */
	public void setShowPlanInvestmentNotice(boolean showPlanInvestmentNotice) {
		this.showPlanInvestmentNotice = showPlanInvestmentNotice;
	}
	/**
	 * @return the showICC
	 */
	public boolean isShowICC() {
		return showICC;
	}
	/**
	 * @param showICC the showICC to set
	 */
	public void setShowICC(boolean showICC) {
		this.showICC = showICC;
	}
	/**
	 * @return the showMissingIccContactMessage
	 */
	public boolean isShowMissingIccContactMessage() {
		return showMissingIccContactMessage;
	}
	/**
	 * @param showMissingIccContactMessage the showMissingIccContactMessage to set
	 */
	public void setShowMissingIccContactMessage(boolean showMissingIccContactMessage) {
		this.showMissingIccContactMessage = showMissingIccContactMessage;
	}
	/**
	 * @return the showIccCalendarYearMessage
	 */
	public boolean isShowIccCalendarYearMessage() {
		return showIccCalendarYearMessage;
	}
	/**
	 * @param showIccCalendarYearMessage the showIccCalendarYearMessage to set
	 */
	public void setShowIccCalendarYearMessage(boolean showIccCalendarYearMessage) {
		this.showIccCalendarYearMessage = showIccCalendarYearMessage;
	}
	/**
	 * @return the showPlanHighlight
	 */
	public boolean isShowPlanHighlight() {
		return showPlanHighlight;
	}
	/**
	 * @param showPlanHighlight the showPlanHighlight to set
	 */
	public void setShowPlanHighlight(boolean showPlanHighlight) {
		this.showPlanHighlight = showPlanHighlight;
	}
	/**
	 * @return the addDocumentPermission
	 */
	public boolean isAddDocumentPermission() {
		return addDocumentPermission;
	}
	/**
	 * @param addDocumentPermission the addDocumentPermission to set
	 */
	public void setAddDocumentPermission(boolean addDocumentPermission) {
		this.addDocumentPermission = addDocumentPermission;
	}
	/**
	 * @return the uploadAndShareTab
	 */
	public Boolean getUploadAndShareTab() {
		return uploadAndShareTab;
	}
	/**
	 * @param uploadAndShareTab the uploadAndShareTab to set
	 */
	public void setUploadAndShareTab(Boolean uploadAndShareTab) {
		this.uploadAndShareTab = uploadAndShareTab;
	}
	/**
	 * @return the buildYourPackageTab
	 */
	public Boolean getBuildYourPackageTab() {
		return buildYourPackageTab;
	}
	/**
	 * @param buildYourPackageTab the buildYourPackageTab to set
	 */
	public void setBuildYourPackageTab(Boolean buildYourPackageTab) {
		this.buildYourPackageTab = buildYourPackageTab;
	}
	/**
	 * @return the buildYourPackageNPTab
	 */
	public Boolean getBuildYourPackageNPTab() {
		return buildYourPackageNPTab;
	}
	/**
	 * @param buildYourPackageNPTab the buildYourPackageNPTab to set
	 */
	public void setBuildYourPackageNPTab(Boolean buildYourPackageNPTab) {
		this.buildYourPackageNPTab = buildYourPackageNPTab;
	}
	/**
	 * @return the uploadAndSharePageInd
	 */
	public Boolean getUploadAndSharePageInd() {
		return uploadAndSharePageInd;
	}
	/**
	 * @param uploadAndSharePageInd the uploadAndSharePageInd to set
	 */
	public void setUploadAndSharePageInd(Boolean uploadAndSharePageInd) {
		this.uploadAndSharePageInd = uploadAndSharePageInd;
	}
	
	
	
}
