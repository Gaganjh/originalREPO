package com.manulife.pension.ps.service.report.notice.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class PlanNoticeDocumentChangeHistoryVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer documentId;
	private Integer contractId;
	private String documentName;
	private String replacedfileName;
	private BigDecimal changedProfileId;
	private String changedUserName;
	private Timestamp changedDate;
	private String firstName;
	private String lastName;
	private String changedPPT;
	private Integer versionNumber;
	private String previousdocumentName;
	private boolean crossedTwelveMonthRule;
	private Timestamp changedDatePlusOneYear;
	
	
	
	
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
	 * @return the previousdocumentName
	 */
	public String getPreviousdocumentName() {
		return previousdocumentName;
	}
	/**
	 * @param previousdocumentName the previousdocumentName to set
	 */
	public void setPreviousdocumentName(String previousdocumentName) {
		this.previousdocumentName = previousdocumentName;
	}
	private LookupDescription planNoticeDocumentChangeTypeDetail;
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the replacedfileName
	 */
	public String getReplacedfileName() {
		return replacedfileName;
	}
	/**
	 * @param replacedfileName the replacedfileName to set
	 */
	public void setReplacedfileName(String replacedfileName) {
		this.replacedfileName = replacedfileName;
	}
	/**
	 * @return the changedProfileId
	 */
	public BigDecimal getChangedProfileId() {
		return changedProfileId;
	}
	/**
	 * @param changedProfileId the changedProfileId to set
	 */
	public void setChangedProfileId(BigDecimal changedProfileId) {
		this.changedProfileId = changedProfileId;
	}
	/**
	 * @return the changedDate
	 */
	public Timestamp getChangedDate() {
		return changedDate;
	}
	/**
	 * @param changedDate the changedDate to set
	 */
	public void setChangedDate(Timestamp changedDate) {
		this.changedDate = changedDate;
	}
	/**
	 * @return the planNoticeDocumentChangeTypeDetail
	 */
	public LookupDescription getPlanNoticeDocumentChangeTypeDetail() {
		return planNoticeDocumentChangeTypeDetail;
	}
	/**
	 * @param planNoticeDocumentChangeTypeDetail the planNoticeDocumentChangeTypeDetail to set
	 */
	public void setPlanNoticeDocumentChangeTypeDetail(
			LookupDescription planNoticeDocumentChangeTypeDetail) {
		this.planNoticeDocumentChangeTypeDetail = planNoticeDocumentChangeTypeDetail;
	}
	/**
	 * @return the changedUserName
	 */
	public String getChangedUserName() {
		String[] username=changedUserName.split(" ");
		String firstName=username[0].charAt(0)+username[0].substring(1,username[0].length()).toLowerCase();
		String lastName=username[1].charAt(0)+username[1].substring(1,username[1].length()).toLowerCase();
		return firstName+" "+ lastName;
	}
	/**
	 * @param changedUserName the changedUserName to set
	 */
	public void setChangedUserName(String changedUserName) {
		this.changedUserName = changedUserName;
	}

	/**
	 * @return the changedPPT
	 */
	public String getChangedPPT() {
		return changedPPT;
	}
	/**
	 * @param changedPPT the changedPPT to set
	 */
	public void setChangedPPT(String changedPPT) {
		this.changedPPT = changedPPT;
	}
	public boolean isCrossedTwelveMonthRule() {
		return crossedTwelveMonthRule;
	}
	public void setCrossedTwelveMonthRule(boolean crossedTwelveMonthRule) {
		this.crossedTwelveMonthRule = crossedTwelveMonthRule;
	}
	public Timestamp getChangedDatePlusOneYear() {
		return changedDatePlusOneYear;
	}
	public void setChangedDatePlusOneYear(Timestamp changedDatePlusOneYear) {
		this.changedDatePlusOneYear = changedDatePlusOneYear;
	}	
	
}
