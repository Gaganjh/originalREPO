package com.manulife.pension.ps.service.report.census.valueobject;

import com.manulife.pension.ps.service.report.census.reporthandler.CensusSummaryReportHandler;
import com.manulife.pension.ps.service.report.contract.reporthandler.CensusTemplateReportHandler;
import com.manulife.pension.ps.service.report.contract.reporthandler.VestingTemplateReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class CensusSummaryReportData extends ReportData {

    private static final long serialVersionUID = -1590411619352751433L;

    public static final String REPORT_ID = CensusSummaryReportHandler.class.getName();
	public static final String REPORT_NAME = "censusSummaryReport";
    
    public static final String TEMPLATE_REPORT_ID = CensusTemplateReportHandler.class.getName();
    public static final String TEMPLATE_REPORT_NAME = "censusTemplateReport";
    
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_STATUS = "status";
	public static final String FILTER_LAST_NAME = "lastName";
	public static final String FILTER_SSN = "ssn";
    public static final String FILTER_DIVISION = "division";
    public static final String FILTER_SEGMENT = "segment";
    
	public static final String DEFAULT_SORT = "lastName";
    
    public static final String SORT_LAST_NAME = "lastName";
    public static final String SORT_BIRTH_DATE = "birthDate";
    public static final String SORT_HIRE_DATE = "hireDate";
    public static final String SORT_STATUS = "status";
    public static final String SORT_DIVISION = "division";
    
    public static final int    PAGE_SIZE = 35; 
    
    private int contractNumber;
    private String EDTSortOption = "EE"; 
    private String specialSortIndicator = "Yes";

	public CensusSummaryReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
    }

    public int getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(int contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getEDTSortOption() {
        return EDTSortOption;
    }

    public void setEDTSortOption(String sortOption) {
        EDTSortOption = sortOption;
    }

    public String getSpecialSortIndicator() {
        return specialSortIndicator;
    }

    public void setSpecialSortIndicator(String specialSortIndicator) {
        this.specialSortIndicator = specialSortIndicator;
    }
}
