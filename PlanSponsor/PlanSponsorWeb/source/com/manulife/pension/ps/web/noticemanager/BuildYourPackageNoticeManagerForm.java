package com.manulife.pension.ps.web.noticemanager;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanDocumentReportData;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;

/**
 * 
 * @author krishta
 *
 */
public class BuildYourPackageNoticeManagerForm extends UploadSharedNoticeManagerForm {

	private static final long serialVersionUID = 1L;
	private String ContactId;
	private boolean AccessPermissions;
	private List<PlanNoticeDocumentVO> PlanNoticeDocuments;
	private String mailingSelectionIndicator;
	private String task = "default";
	private MultipartFile censusFile;
	private String mailingName;
	private String fileType = "";
	private String documentsSelected;
	private String documentsOrder;
	private String documentsSelectedJH;
	private Boolean customSort;
	private String sortTypeArrow;
	private Boolean sortAppliedInd = false;
	private String selectedDocumentName;
	private Boolean customSortDisplayInd = false;
	private Integer customLastDocumentId = 0;
	private String merrillInteractionStatus = "";
	private String termsOfUse = "";
	private Boolean censusInfoPresent = true;
	private Integer countOfParticipantInCensusGenerated = 0;
	private Integer countOfPagesInPDFGenerated = 0;
	private Boolean errorsInd = false;
	private String script = "";
	private String fromPage = "";
	private String filePath= "";
	private Boolean downloadExceptionCached= false;
	private Boolean ereportError= false;
	private Boolean mergeError= false;
	
	
	public Boolean getMergeError() {
		return mergeError;
	}
	public void setMergeError(Boolean mergeError) {
		this.mergeError = mergeError;
	}
	public Boolean getEreportError() {
		return ereportError;
	}
	public void setEreportError(Boolean ereportError) {
		this.ereportError = ereportError;
	}
	
	public Boolean getDownloadExceptionCached() {
		return downloadExceptionCached;
	}
	public void setDownloadExceptionCached(Boolean downloadExceptionCached) {
		this.downloadExceptionCached = downloadExceptionCached;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFromPage() {
		return fromPage;
	}
	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public Boolean getErrorsInd() {
		return errorsInd;
	}
	public void setErrorsInd(Boolean errorsInd) {
		this.errorsInd = errorsInd;
	}
	public Integer getCountOfPagesInPDFGenerated() {
		return countOfPagesInPDFGenerated;
	}
	public void setCountOfPagesInPDFGenerated(Integer countOfPagesInPDFGenerated) {
		this.countOfPagesInPDFGenerated = countOfPagesInPDFGenerated;
	}
	public Integer getCountOfParticipantInCensusGenerated() {
		return countOfParticipantInCensusGenerated;
	}
	public void setCountOfParticipantInCensusGenerated(
			Integer countOfParticipantInCensusGenerated) {
		this.countOfParticipantInCensusGenerated = countOfParticipantInCensusGenerated;
	}
	public Boolean getCensusInfoPresent() {
		return censusInfoPresent;
	}
	public void setCensusInfoPresent(Boolean censusInfoPresent) {
		this.censusInfoPresent = censusInfoPresent;
	}
	public String getTermsOfUse() {
		return termsOfUse;
	}
	public void setTermsOfUse(String termsOfUse) {
		this.termsOfUse = termsOfUse;
	}
	public String getMerrillInteractionStatus() {
		return merrillInteractionStatus;
	}
	public void setMerrillInteractionStatus(String merrillInteractionStatus) {
		this.merrillInteractionStatus = merrillInteractionStatus;
	}
	public Integer getCustomLastDocumentId() {
		return customLastDocumentId;
	}
	public void setCustomLastDocumentId(Integer customLastDocumentId) {
		this.customLastDocumentId = customLastDocumentId;
	}
	public Boolean getCustomSortDisplayInd() {
		return customSortDisplayInd;
	}
	public void setCustomSortDisplayInd(Boolean customSortDisplayInd) {
		this.customSortDisplayInd = customSortDisplayInd;
	}
	public String getSelectedDocumentName() {
		return selectedDocumentName;
	}
	public void setSelectedDocumentName(String selectedDocumentName) {
		this.selectedDocumentName = selectedDocumentName;
	}
	public Boolean getSortAppliedInd() {
		return sortAppliedInd;
	}
	public void setSortAppliedInd(Boolean sortAppliedInd) {
		this.sortAppliedInd = sortAppliedInd;
	}
	public String getSortTypeArrow() {
		return sortTypeArrow;
	}
	public void setSortTypeArrow(String sortTypeArrow) {
		this.sortTypeArrow = sortTypeArrow;
	}
	public Boolean getCustomSort() {
		return customSort;
	}
	public void setCustomSort(Boolean customSort) {
		this.customSort = customSort;
	}
	public String getDocumentsSelectedJH() {
		return documentsSelectedJH;
	}
	public void setDocumentsSelectedJH(String documentsSelectedJH) {
		this.documentsSelectedJH = documentsSelectedJH;
	}
	public String getDocumentsSelected() {
		return documentsSelected;
	}
	public void setDocumentsSelected(String documentsSelected) {
		this.documentsSelected = documentsSelected;
	}
	public String getDocumentsOrder() {
		return documentsOrder;
	}
	public void setDocumentsOrder(String documentsOrder) {
		this.documentsOrder = documentsOrder;
	}
	private Integer documentId;
	
	
	public Integer getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/**
	 * 
	 * @return the mailingSelectionIndicator
	 */
	public String getMailingSelectionIndicator() {
		return mailingSelectionIndicator;
	}
	/**
	 * @param mailingSelectionIndicator the mailingSelectionIndicator to set
	 */
	public void setMailingSelectionIndicator(String mailingSelectionIndicator) {
		this.mailingSelectionIndicator = mailingSelectionIndicator;
	}
	/**
	 * 
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
	 * 
	 * @return the censusFile
	 */
	public MultipartFile getCensusFile() {
		return censusFile;
	}
	/**
	 * @param censusFile the censusFile to set
	 */
	public void setCensusFile(MultipartFile censusFile) {
		this.censusFile = censusFile;
	}
	/**
	 * 
	 * @return the mailingName
	 */
	public String getMailingName() {
		return mailingName;
	}
	/**
	 * @param mailingName the mailingName to set
	 */
	public void setMailingName(String mailingName) {
		this.mailingName = mailingName;
	}
	/**
	 * 
	 * @return the serialVersionUID
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * 
	 * @return the contactId
	 */
	public String getContactId() {
		return ContactId;
	}
	/**
	 * @param contactId the contactId to set
	 */
	public void setContactId(String contactId) {
		ContactId = contactId;
	}
	/**
	 * @return the accessPermissions
	 */
	public boolean isAccessPermissions() {
		return AccessPermissions;
	}
	/**
	 * @param accessPermissions the accessPermissions to set
	 */
	public void setAccessPermissions(boolean accessPermissions) {
		AccessPermissions = accessPermissions;
	}
	/**
	 * @return the planNoticeDocuments
	 */
	public List<PlanNoticeDocumentVO> getPlanNoticeDocuments() {
		return PlanNoticeDocuments;
	}
	/**
	 * @param planNoticeDocuments the planNoticeDocuments to set
	 */
	public void setPlanNoticeDocuments(
			List<PlanNoticeDocumentVO> planNoticeDocuments) {
		PlanNoticeDocuments = planNoticeDocuments;
	}

	public void reset(){
		this.censusFile = null;
		this.documentId = null;
		this.documentsOrder = null;
		this.documentsSelected = null;
		this.documentsSelectedJH = null;
		this.mailingName = null;
		this.mailingSelectionIndicator = null;
		this.sortAppliedInd = false;
		this.fileType = "";
		
	}
}
