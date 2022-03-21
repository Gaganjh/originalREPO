package com.manulife.pension.ps.service.report.tools.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/*
 * Created on Mar 23, 2004
 */

/**
 * @author drotele
 *
 */

public class UploadHistoryItem implements Serializable {
	public final static String SORT_SUBMISSION_ID = "submissionId";
	public final static String SORT_FILE_TYPE = "fileType";
	public final static String SORT_RECEIVED_DATE = "receivedDate";
	public final static String SORT_PMT_EFFECTIVE_DATE = "paymentEffectiveDate";
	public final static String SORT_PMT_TOTAL_AMOUNT = "paymentTotalAmount";

	private final String submissionId;
	private final String fileType;
	private final Date receivedDate;
	private final Date paymentEffectiveDate;
	private final BigDecimal paymentTotalAmount;


	/**
	 * @param submissionId
	 * @param fileType
	 * @param receivedDate
	 * @param paymentEffectiveDate
	 * @param paymentTotalAmount
	 */
	public UploadHistoryItem(
		String submissionId,
		String fileType,
		Date receivedDate,
		Date paymentEffectiveDate,
		BigDecimal paymentTotalAmount) {

		this.submissionId = submissionId;
		this.fileType = fileType;
		this.receivedDate = receivedDate;
		this.paymentEffectiveDate = paymentEffectiveDate;
		this.paymentTotalAmount = paymentTotalAmount;
	}

	/**
	 * @return file type
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @return payment effective date
	 */
	public Date getPaymentEffectiveDate() {
		return paymentEffectiveDate;
	}

	/**
	 * @return payment total amount
	 */
	public BigDecimal getPaymentTotalAmount() {
		return paymentTotalAmount;
	}

	/**
	 * @return received date
	 */
	public Date getReceivedDate() {
		return receivedDate;
	}

	/**
	 * @return tracking number
	 */
	public String getSubmissionId() {
		return submissionId;
	}
}
