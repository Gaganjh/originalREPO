package com.manulife.pension.ps.service.report.tpadownload.valueobject;

import java.util.ArrayList;

import com.manulife.pension.ps.service.report.tpadownload.reporthandler.EStatementsReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

public class EStatementsReportData extends ReportData {

	public static final String REPORT_ID = EStatementsReportHandler.class.getName();
	public static final String REPORT_NAME = "eStatementsReport";
	public static final String DEFAULT_SORT = EStatementsItem.SORT_FIELD_YEAR_END;
	public static final String DEFAULT_SORT_ORDER = ReportSort.DESC_DIRECTION;
		
	// These filters used EVERY time to show only valid rows
	public static final String FILTER_PROFILE_ID = "profileId";
	public static final String FILTER_SITE_LOCATION = "siteLocation";
	
	// These filters are used by column filter interaction by user.
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_CONTRACT_NAME = "contractName";
	public static final String FILTER_STATEMENT_TYPE_CODE = "statementTypeCode";	
	public static final String FILTER_REPORT_END_DATE_FROM = "reportEndDateFrom";
	public static final String FILTER_REPORT_END_DATE_TO = "reportEndDateTo";	
	public static final String FILTER_YEAR_END = "yearEnd";
	public static final String FILTER_CORRECTED_CODE = "correctedCode";
	
	
	public EStatementsReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		this.details = new ArrayList(0);
	}
}

