package com.manulife.pension.ps.service.report.feeSchedule.valueobject;

import java.io.Serializable;

import com.manulife.pension.ps.service.report.feeSchedule.reporthandler.ContractFeeScheduleChangeHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * 
 * Report Data for 404a5 Notice Info Change History
 * 
 * @author Siby Thomas
 *
 */
public class ContractFeeScheduleChangeHistoryReportData extends ReportData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String REPORT_ID = ContractFeeScheduleChangeHistoryReportHandler.class.getName();
	
	public enum FilterSections {
		DimSection,
		FeeSection,
		PbaDetailsSection
	}
	
	public static final String REPORT_NAME = "noticeInfoChangeHistoryReport";
	
	public static final String FILTER_SECTION = "filterSection";
	

	public static final String FILTER_CONTRACT_ID = "contractId";
	public static final String FILTER_FROM_DATE = "fromDate";
	public static final String FILTER_TO_DATE = "toDate";
	public static final String FILTER_USER_NAME = "userId";
	public static final String FILTER_FEE_TYPE = "feeType";
	public static final String FILTER_STD_SCHEDULE_APPLIED_IND = "stdScheduleAppliedInd";
	public static final String FILTER_TPA_FIRM_HISTORY = "tpaFirmHistory";
	public static final String FILTER_GET_NON_TPA_FEE = "getNonTpaFees";

	public static final String SORT_DEFAULT = "default";
	public static final String SORT_CHANGER_NAME = "userName";
	public static final String SORT_VALUE = "changedValue";
	public static final String SORT_CHANGE_DATE = "changeDate";
	public static final String SORT_TYPE = "type";
	public static final String SORT_SCHEDULE_IND = "standardScheduleApplied";
	
	public static final String FILTER_PLAN_PROVISION_HISTORY = "planProvisonHistory";

	
	public ContractFeeScheduleChangeHistoryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
}