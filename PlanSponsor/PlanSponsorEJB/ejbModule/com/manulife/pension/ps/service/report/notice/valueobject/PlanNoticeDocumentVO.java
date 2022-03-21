package com.manulife.pension.ps.service.report.notice.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author krishta
 *
 */
public class PlanNoticeDocumentVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer documentId;
	private Integer contractId;
	private String documentName;
	private String docNameAndUpdatedDate;
	private Integer documentDisplayOrder;
	private String documentFileName;
	private String postToPptInd;
	private String softDelIndicator;
	private boolean packageInd;
	private boolean jhDocument;
	private boolean iccDocument;
	private Date dateCreated;
	private byte[] planDocument;
	private boolean documentLocked;
	private boolean plandocumentexistind;
	private PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeDetail;
	private boolean uploadDocFiveYears= false;
	private String lastActionedUserName;
	private Integer versionNumber;
	private BigDecimal profileId;
	private String planNoticeDeletedUserName;
	private String planNoticeLockedUserName;
	
	
	/**
	 * @return the planNoticeLockedUserName
	 */
	public String getPlanNoticeLockedUserName() {
		return planNoticeLockedUserName;
	}
	/**
	 * @param planNoticeLockedUserName the planNoticeLockedUserName to set
	 */
	public void setPlanNoticeLockedUserName(String planNoticeLockedUserName) {
		this.planNoticeLockedUserName = planNoticeLockedUserName;
	}
	/**
	 * @return the planNoticeDeletedUserName
	 */
	public String getPlanNoticeDeletedUserName() {
		return planNoticeDeletedUserName;
	}
	/**
	 * @param planNoticeDeletedUserName the planNoticeDeletedUserName to set
	 */
	public void setPlanNoticeDeletedUserName(String planNoticeDeletedUserName) {
		this.planNoticeDeletedUserName = planNoticeDeletedUserName;
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
	 * @return the versionNumber
	 */
	public Integer getVersionNumber() {
		return versionNumber;
	}
	/**
	 * @param versionNumber the versionNumber to set
	 */
	public void setVersionNumber(Integer versionNumber) {
		this.versionNumber = versionNumber;
	}
	/**
	 * @return the lastActionedUserName
	 */
	public String getLastActionedUserName() {
		return lastActionedUserName;
	}
	/**
	 * @param lastActionedUserName the lastActionedUserName to set
	 */
	public void setLastActionedUserName(String lastActionedUserName) {
		this.lastActionedUserName = lastActionedUserName;
	}
	/**
	 * @return the uploadDocFiveYears
	 */
	public boolean isUploadDocFiveYears() {
		return uploadDocFiveYears;
	}
	/**
	 * @param uploadDocFiveYears the uploadDocFiveYears to set
	 */
	public void setUploadDocFiveYears(boolean uploadDocFiveYears) {
		this.uploadDocFiveYears = uploadDocFiveYears;
	}
	/**
	 * @return the plandocumentexistind
	 */
	public boolean isPlandocumentexistind() {
		return plandocumentexistind;
	}
	/**
	 * @param plandocumentexistind the plandocumentexistind to set
	 */
	public void setPlandocumentexistind(boolean plandocumentexistind) {
		this.plandocumentexistind = plandocumentexistind;
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
	 * @return the documentName
	 */
	public String getDocumentName() {
		return documentName;
	}
	/**
	 * @param documentName the documentName to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	/**
	 * @return the documentDisplayOrder
	 */
	public Integer getDocumentDisplayOrder() {
		return documentDisplayOrder;
	}
	/**
	 * @param documentDisplayOrder the documentDisplayOrder to set
	 */
	public void setDocumentDisplayOrder(Integer documentDisplayOrder) {
		this.documentDisplayOrder = documentDisplayOrder;
	}
	/**
	 * @return the documentFileName
	 */
	public String getDocumentFileName() {
		return documentFileName;
	}
	/**
	 * @param documentFileName the documentFileName to set
	 */
	public void setDocumentFileName(String documentFileName) {
		this.documentFileName = documentFileName;
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
	 * @return the softDelIndicator
	 */
	public String isSoftDelIndicator() {
		return softDelIndicator;
	}
	/**
	 * @param softDelIndicator the softDelIndicator to set
	 */
	public void setSoftDelIndicator(String softDelIndicator) {
		this.softDelIndicator = softDelIndicator;
	}
	/**
	 * @return the packageInd
	 */
	public boolean isPackageInd() {
		return packageInd;
	}
	/**
	 * @param packageInd the packageInd to set
	 */
	public void setPackageInd(boolean packageInd) {
		this.packageInd = packageInd;
	}
	/**
	 * @return the jhDocument
	 */
	public boolean isJhDocument() {
		return jhDocument;
	}
	/**
	 * @param jhDocument the jhDocument to set
	 */
	public void setJhDocument(boolean jhDocument) {
		this.jhDocument = jhDocument;
	}
	/**
	 * @return the iccDocument
	 */
	public boolean isIccDocument() {
		return iccDocument;
	}
	/**
	 * @param iccDocument the iccDocument to set
	 */
	public void setIccDocument(boolean iccDocument) {
		this.iccDocument = iccDocument;
	}
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	/**
	 * @return the planDocument
	 */
	public byte[] getPlanDocument() {
		return planDocument;
	}
	/**
	 * @param planDocument the planDocument to set
	 */
	public void setPlanDocument(byte[] planDocument) {
		this.planDocument = planDocument;
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
	 * @return the planNoticeDocumentChangeDetail
	 */
	public PlanNoticeDocumentChangeHistoryVO getPlanNoticeDocumentChangeDetail() {
		return planNoticeDocumentChangeDetail;
	}
	/**
	 * @param planNoticeDocumentChangeDetail the planNoticeDocumentChangeDetail to set
	 */
	public void setPlanNoticeDocumentChangeDetail(
			PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeDetail) {
		this.planNoticeDocumentChangeDetail = planNoticeDocumentChangeDetail;
	}
	/**
	 * @return the docNameAndUpdatedDate
	 */
	public String getDocNameAndUpdatedDate() {
		return docNameAndUpdatedDate;
	}
	/**
	 * @param docNameAndUpdatedDate the docNameAndUpdatedDate to set
	 */
	public void setDocNameAndUpdatedDate(String docNameAndUpdatedDate) {
		this.docNameAndUpdatedDate = docNameAndUpdatedDate;
	}
}
