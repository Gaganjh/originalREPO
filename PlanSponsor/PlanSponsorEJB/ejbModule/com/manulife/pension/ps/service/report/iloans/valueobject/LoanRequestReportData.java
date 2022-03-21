package com.manulife.pension.ps.service.report.iloans.valueobject;

import com.manulife.pension.ps.service.report.iloans.reporthandler.LoanRequestReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class LoanRequestReportData extends ReportData {
	public static final String REPORT_ID = LoanRequestReportHandler.class.getName();
	public static final String REPORT_NAME = "loanRequestReport";
	public static final String FILTER_PROFILE_ID = "profileId";
	public static final String FILTER_SITE_LOCATION = "siteLocation";
	public static final String FILTER_DI_DURATION = "DIDuration";
	public static final String FILTER_USER_ROLE = "role";
	
	public static final String SORT_STATUS_CODE = "statusCode";
	public static final String SORT_INITIATED_BY = "initiatedBy";
	public static final String SORT_REQUEST_DATE = "requestDate";
	
	public LoanRequestReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

}


