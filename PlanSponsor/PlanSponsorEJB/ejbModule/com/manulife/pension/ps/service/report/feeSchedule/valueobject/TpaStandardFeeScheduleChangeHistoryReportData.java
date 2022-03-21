package com.manulife.pension.ps.service.report.feeSchedule.valueobject;

import java.util.Map;

import com.manulife.pension.ps.service.report.feeSchedule.reporthandler.TpaStandardFeeScheduleChangeHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class TpaStandardFeeScheduleChangeHistoryReportData extends ReportData{
	
	public static final String REPORT_ID = TpaStandardFeeScheduleChangeHistoryReportHandler.class.getName();
	public static final String REPORT_NAME = "tpaStandardFeeScheduleChangeHistoryReportData"; 
	public static final String FILTER_TPA_FIRM_ID = "tpaFirmId";
	public static final String FILTER_FROM_DATE = "fromDate";
	public static final String FILTER_TO_DATE = "toDate";
	public static final String FILTER_USER_NAME = "userName";
	public static final String FILTER_FEE_TYPE = "feeType";
	
	public static final String DEFAULT_SORT = "CREATED_TS DESC, FEE_TYPE_ORDER, FIRST_NAME, LAST_NAME, AMOUNT_VALUE ";
	public static final String SORT_DEFAULT = "default";
	public static final String SORT_CREATED_TS = "changeDate";
	public static final String SORT_FEE_TYPE_ORDER = "FEE_TYPE_ORDER";
	public static final String SORT_USER_NAME = "USER_NAME";
	public static final String SORT_AMOUNT_VALUE = "AMOUNT_VALUE";
	public static final String SORT_COMMENT = "COMMENT";
	
	public static final String DEFAULT_SORT_DIRECTION = "ASC";
	public static final int    PAGE_SIZE = 35; 
	
	/**
	 * Constructor
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public TpaStandardFeeScheduleChangeHistoryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);	
	}
	
	private Map<String, String> userNames;
	private Map<String, String> standardFeeTypes;
	private Map<String, String> nonStandardFeeTypes;
	
	public Map<String, String> getUserNames() {
		return userNames;
	}
	public void setUserNames(Map<String, String> userNames) {
		this.userNames = userNames;
	}
	public Map<String, String> getStandardFeeTypes() {
		return standardFeeTypes;
	}
	public void setStandardFeeTypes(Map<String, String> standardFeeTypes) {
		this.standardFeeTypes = standardFeeTypes;
	}
	public Map<String, String> getNonStandardFeeTypes() {
		return nonStandardFeeTypes;
	}
	public void setNonStandardFeeTypes(Map<String, String> nonStandardFeeTypes) {
		this.nonStandardFeeTypes = nonStandardFeeTypes;
	}

}
