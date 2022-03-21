package com.manulife.pension.platform.web.secureDocumentUpload;

import java.io.Serializable;

import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class SDUViewTabReportData extends ReportData implements Serializable {

	public static final String REPORT_ID = "com.manulife.pension.ps.web.tools.SDUViewTabReport";
	public static final String REPORT_NAME = "SDUViewTabReport";

	public static final String REQ_SHARED_WITH_USER_ID = "sharedWithUserId"; 
	public static final String REQ_SDU_CLIENT_ID = "clientId";
	public static final String REQ_CONTRACT_NO = "clientContract";
	
	public static final String SORT_FILE_NAME = "fileName";
	public static final String SORT_SHARED_BY_USER_NAME = "clientUserName";
	public static final String SORT_EXPIRY_TS = "shareExpiryTs";
	public static final String SORT_TYPE = "type";
	
	protected int totalPages=0;
	protected int totalItems=0;

	public SDUViewTabReportData(ReportCriteria criteria, int totalCount) {
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