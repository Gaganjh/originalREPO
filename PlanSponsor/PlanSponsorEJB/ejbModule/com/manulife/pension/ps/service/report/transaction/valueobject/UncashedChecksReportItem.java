package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import org.apache.commons.lang.ObjectUtils;

public class UncashedChecksReportItem implements Serializable {
	public final static String SORT_CHECK_ISSUE_DATE = "checkIssueDate";
	public final static String SORT_PARTICIPANT_NAME = "participantName";
	public final static String SORT_PAYEE_NAME = "payeeName";
	public final static String SORT_CHECK_AMOUNT = "checkAmount";
	public final static String SORT_CHECK_STATUS = "checkStatus";

	private Date checkIssueDate;
	private String participantName;
	private String participantLastName;
	private String participantFirstName;
	private String participantSsn;
	private String payeeType;
	private String payeeName;
	private String ssn;
	private BigDecimal checkAmount;
	private Date transactionDate;
	private String transactionType;
	private String transactionNumber;

	private String checkStatus;
	private String profileId;

	public UncashedChecksReportItem(Date checkIssueDate,
			String participantName, String participantLastName,
			String participantFirstName, String participantSsn,
			String payeeType, String payeeName, BigDecimal checkAmount,
			Date transactionDate, String transactionType,
			String transactionNumber, String checkStatus, String ssn, String profileId) {

		this.checkIssueDate = checkIssueDate;
		this.participantName = participantName;
		this.participantLastName = participantLastName;
		this.participantFirstName = participantFirstName;
		this.participantSsn = participantSsn;
		this.payeeType = payeeType;
		this.payeeName = payeeName;
		this.checkAmount = checkAmount;
		this.transactionDate = transactionDate;
		this.transactionType = transactionType;
		this.transactionNumber = transactionNumber;
		this.checkStatus = checkStatus;
		this.profileId = profileId;
		this.ssn = ssn;
	}
	/*
	 * constructor.
	 */
	public UncashedChecksReportItem() {
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof UncashedChecksReportItem) {
			UncashedChecksReportItem item = (UncashedChecksReportItem) obj;
			return ObjectUtils.equals(checkIssueDate, item.checkIssueDate)
					&& ObjectUtils
							.equals(participantName, item.participantName)
					&& ObjectUtils.equals(participantLastName,
							item.participantLastName)
					&& ObjectUtils.equals(participantFirstName,
							item.participantFirstName)
					&& ObjectUtils.equals(participantSsn, item.participantSsn)
					&& ObjectUtils.equals(payeeType, item.payeeType)
					&& ObjectUtils.equals(payeeName, item.payeeName)
					&& ObjectUtils.equals(checkAmount, item.checkAmount)
					&& ObjectUtils
							.equals(transactionDate, item.transactionDate)
					&& ObjectUtils
							.equals(transactionType, item.transactionType)
					&& ObjectUtils.equals(transactionNumber,
							item.transactionNumber)
					&& ObjectUtils.equals(checkStatus, item.checkStatus)
					&& ObjectUtils.equals(profileId, item.profileId)
					&& ObjectUtils.equals(ssn,item.ssn);

		}
		return false;
	}

	/**
	 * @return checkIssueDate
	 */
	public Date getCheckIssueDate() {
		return checkIssueDate;
	}

	/**
	 * @return participantName
	 */
	public String getParticipantName() {
		return participantName;
	}

	/**
	 * @return participantLastName
	 */
	public String getParticipantLastName() {
		return participantLastName;
	}

	/**
	 * @return participantFirstName
	 */
	public String getParticipantFirstName() {
		return participantFirstName;
	}

	/**
	 * @return participantSsn
	 */
	public String getParticipantSsn() {
		return participantSsn;
	}

	/**
	 * @return payeeType
	 */
	public String getPayeeType() {
		return payeeType;
	}

	/**
	 * @return payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}

	/**
	 * @return checkAmount
	 */
	public BigDecimal getCheckAmount() {
		return checkAmount;
	}

	/**
	 * @return transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @return transactionType
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @return transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @return checkStatus
	 */
	public String getCheckStatus() {
		return checkStatus;
	}

	/**
	 * @return profileId
	 */
	public String getProfileId() {
		return profileId;
	}

	/**
	 * @return ssn
	 */
	public String getSsn() {
		return ssn;
	}
	
	/**
	 * @param checkIssueDate
	 */
	public void setCheckIssueDate(Date checkIssueDate) {
		this.checkIssueDate = checkIssueDate;
	}

	/**
	 * @param participantName
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	/**
	 * @param participantLastName
	 */
	public void setParticipantLastName(String participantLastName) {
		this.participantLastName = participantLastName;
	}

	/**
	 * @param participantFirstName
	 */
	public void setParticipantFirstName(String participantFirstName) {
		this.participantFirstName = participantFirstName;
	}

	/**
	 * @param participantSsn
	 */
	public void setParticipantSsn(String participantSsn) {
		this.participantSsn = participantSsn;
	}

	/**
	 * @param payeeType
	 */
	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}

	/**
	 * @param payeeName
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	/**
	 * @param checkAmount 
	 * 	 */
	public void setCheckAmount(BigDecimal checkAmount) {
		this.checkAmount = checkAmount;
	}

	/**
	 * @param transactionDate
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @param transactionType
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @param transactionNumber
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @param checkStatus
	 */
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	/**
	 * @param profileId
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
	
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

}