package com.manulife.pension.ps.service.submission.valueobject;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class SubmissionValidationBundle<SubmissionItemType, SystemOfRecordType, ErrorListType>
		extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	private SubmissionItemType submissionItem;

	private SystemOfRecordType systemOfRecord;

	private ErrorListType validationErrors;
	
	private Integer remainingNumberOfErrorRecords;

	public SubmissionValidationBundle(SubmissionItemType submissionItem,
			SystemOfRecordType systemOfRecord, ErrorListType validationErrors,
			Integer remainingNumberOfErrorRecords) {
		this.submissionItem = submissionItem;
		this.systemOfRecord = systemOfRecord;
		this.validationErrors = validationErrors;
		this.remainingNumberOfErrorRecords = remainingNumberOfErrorRecords;
	}

	public SubmissionItemType getSubmissionItem() {
		return submissionItem;
	}

	public void setSubmissionItem(SubmissionItemType submissionItem) {
		this.submissionItem = submissionItem;
	}

	public SystemOfRecordType getSystemOfRecord() {
		return systemOfRecord;
	}

	public void setSystemOfRecord(SystemOfRecordType systemOfRecord) {
		this.systemOfRecord = systemOfRecord;
	}

	public ErrorListType getValidationErrors() {
		return validationErrors;
	}

	public void setValidationErrors(ErrorListType validationErrors) {
		this.validationErrors = validationErrors;
	}

	public Integer getRemainingNumberOfErrorRecords() {
		return remainingNumberOfErrorRecords;
	}

	public void setRemainingNumberOfErrorRecords(
			Integer remainingNumberOfErrorRecords) {
		this.remainingNumberOfErrorRecords = remainingNumberOfErrorRecords;
	}
}
