package com.manulife.pension.ps.service.report.submission.valueobject;

import java.util.List;

import com.manulife.pension.ps.service.report.submission.reporthandler.TPAVestingSubmissionReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class TPAVestingSubmissionReportData extends ReportData {

	public static String REPORT_ID = TPAVestingSubmissionReportHandler.class.getName();
	public static String REPORT_NAME = "tpaVestingSubmissionReport";
	
    public static final String SORT_CONTRACT_ID = "contractId";
    public static final String FILTER_TPA_FIRM = "tpaFirm";
    public static final String FILTER_CONTRACT_LIST = "contractList";
    public static final String FILTER_PSW_SITE = "pswSite";
    public static final String FILTER_PLAN_STAFF_PERMISSION = "planStaffPermission";
    
	
	
    private List columnLabels;
	
	
	public TPAVestingSubmissionReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

    public List getColumnLabels() {
        return columnLabels;
    }

    public void setColumnLabels(List columnLabels) {
        this.columnLabels = columnLabels;
    }
	
	
}
