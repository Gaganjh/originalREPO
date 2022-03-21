package com.manulife.pension.ps.service.report.contract.valueobject;

import com.manulife.pension.ps.service.report.contract.reporthandler.PlanDataHistoryReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

public class PlanDataHistoryReportData extends ReportData {
    private static final long serialVersionUID = -2457543778789917301L;
    
    public static final String REPORT_ID = PlanDataHistoryReportHandler.class.getName();
    public static final String REPORT_NAME = "planDataHistoryReport";
    public static final String DEFAULT_SORT = "createdTs";
    public static final String DEFAULT_SORT_DIRECTION = ReportSort.DESC_DIRECTION;
    
    public static final String FILTER_PLAN_ID = "planId";
    public static final String FILTER_FIELD_NAME = "fieldName";
    public static final String FILTER_USER_ID = "userId";
    public static final String FILTER_FROM_DATE = "fromDate";
    public static final String FILTER_TO_DATE = "toDate";
    
    public static final String SORT_FIELD_CREATED_TS = "createdTs";
    public static final String SORT_FIELD_USER_ID = "userId";
    public static final String SORT_FIELD_CHANNEL_CODE = "channelCode";
    public static final String SORT_FIELD_FIELD_NAME = "fieldName";
    
    public static final int PAGE_SIZE = 35;
    
    public PlanDataHistoryReportData() {
        super();
    }
    
    public PlanDataHistoryReportData(ReportCriteria criteria, int totalCount) {
        super(criteria, totalCount);
    }
}
