package com.manulife.pension.platform.web.secureDocumentUpload;

import java.io.Serializable;

import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class SDUHistoryTabReportData extends ReportData implements Serializable {

	public static final String REPORT_ID = "com.manulife.pension.ps.web.tools.SecureDocumentUploadHistoryReport";
	public static final String REPORT_NAME = "SecureDocumentUploadHistoryReport";

	public static final String FILTER_TYPE = "type";
	public static final String FILTER_STATUS = "status";
	public static final String FILTER_CONTRACT_NO = "contractNumber";
	public static final String FILTER_START_DATE = "filterStartDate";
	public static final String FILTER_END_DATE = "filterEndDate";
	public static final String FILTER_USER_ID = "filterUserId"; 
	public static final String FILTER_SDU_CLIENT_ID = "clientID";
	public static final String FILTER_SUBMISSION_ID = "submissionID";
	
	public static final String SORT_SUBMISSION_ID = "submissionId";
	public static final String SORT_SUBMISSION_DATE = "submissionTs";
	public static final String SORT_DOCUMENT_NAME = "fileName";
	public static final String SORT_SUBMITTER_NAME = "clientUserName";
	public static final String SORT_SUBMITTER_ROLE = "clientUserRole";
	public static final String SORT_SUBMISSION_STATUS = "submissionStatus";	
	public static final String SORT_TYPE = "type";
	
	protected int totalPages=0;
	protected int totalItems=0;

	public SDUHistoryTabReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}
}