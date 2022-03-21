package com.manulife.pension.ps.service.report.tpadownload.valueobject;

import java.util.ArrayList;

import com.manulife.pension.ps.service.report.tpadownload.reporthandler.TedCurrentFilesReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

public class TedCurrentFilesReportData extends ReportData {

	public static final String REPORT_ID = TedCurrentFilesReportHandler.class.getName();
	public static final String REPORT_NAME = "tedHistoryFilesReport";
	public static final String DEFAULT_SORT = TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS;
	public static final String DEFAULT_SORT_ORDER = ReportSort.ASC_DIRECTION;
		
	// These filters used EVERY time to show only valid rows
	public static final String FILTER_PROFILE_ID = "profileId";
	public static final String FILTER_SITE_LOCATION = "siteLocation";
	
	// These filters are used by column filter interaction by user.
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_CONTRACT_NAME = "contractName";
	public static final String FILTER_CORRECTED = "correctedIndicator";
	public static final String FILTER_DOWNLOAD_STATUS = "downloadStatus";
	public static final String FILTER_YEAR_END = "yearEndInd";
	public static final String FILTER_PERIOD_END_DATE = "periodEndDate";
	
	public static final String RETURN_CODE_FTP_SERVER_DOWN = "NOFTP";
	public String errorCode="";
	
	public int totalNewAndDownloadedFiles = 0;
	
	public TedCurrentFilesReportData(ReportCriteria criteria, int totalCount, int newAndDownloadedCount) {
		super(criteria, totalCount);
		this.setTotalNewAndDownloadedFiles(newAndDownloadedCount);		
		this.details = new ArrayList(0);
	}
	public int getTotalNewAndDownloadedFiles() {
		return totalNewAndDownloadedFiles;
	}
	public void setTotalNewAndDownloadedFiles(int totalNewAndDownloadedFiles) {
		this.totalNewAndDownloadedFiles = totalNewAndDownloadedFiles;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

