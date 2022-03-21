package com.manulife.pension.ps.service.report.tpadownload.valueobject;

import java.util.ArrayList;

import com.manulife.pension.ps.service.report.tpadownload.reporthandler.TedHistoryFilesReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

public class TedHistoryFilesReportData extends ReportData {

	public static final String REPORT_ID = TedHistoryFilesReportHandler.class.getName();
	public static final String REPORT_NAME = "tedHistoryFilesReport";
	public static final String DEFAULT_SORT = TedHistoryFilesItem.SORT_FIELD_QUARTER_END_DATE;
	public static final String DEFAULT_SORT_ORDER = ReportSort.DESC_DIRECTION;

	public static final String STATUS_NO_ACCESS_TO_CONTRACT = "NA";
	public static final String STATUS_NO_HISTORY_FILES_AVAILABLE = "NHF";
	public static final String STATUS_NO_CONTRACT_NUMBER_PROVIDED = "NCN";
	public static final String STATUS_RESULTS_RETURNED = "RR";
	public static final String STATUS_INITIAL_ENTRY = "IN";
	public static final String STATUS_FTP_SERVER_DOWN = "NOFTP";
	
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_TPA_FIRM_ID = "tpaFirmId";

	// These filters used EVERY time to show only valid rows
	public static final String FILTER_PROFILE_ID = "profileId";
	public static final String FILTER_SITE_LOCATION = "siteLocation";

	
	private String returnCode=STATUS_INITIAL_ENTRY;
	private String contractNumber="";;
	private String contractName="";
	
	public TedHistoryFilesReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
		this.details = new ArrayList(0);
	}
	/**
	 * @return Returns the contractNumber.
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber The contractNumber to set.
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
}

