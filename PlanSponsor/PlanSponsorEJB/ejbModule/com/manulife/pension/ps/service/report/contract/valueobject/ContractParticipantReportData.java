package com.manulife.pension.ps.service.report.contract.valueobject;

import com.manulife.pension.ps.service.report.contract.reporthandler.ContractParticipantListingReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ContractParticipantReportData extends ReportData {
	public static final String REPORT_NAME = "contractLevelParticipantReport";
	
	public static final String REPORT_ID = ContractParticipantListingReportHandler.class.getName();
	
	public static final String FILTER_CONTRACT_ID = "contractId";
	
	public static final String SORT_SSN = "socialSecurityNumber";
	public static final String SORT_ENROLLMENT_DATE = "enrollmentDate";
	public static final String SORT_NAME = "name";
	public static final String SORT_DEFAULT_ENROLLED = "defaultEnrolled";
	public static final String SORT_PIN_STATUAS = "pinStatus";
	public static final String SORT_PIN_TRANS_CREATED = "pinTranCrtInd";
	
	public ContractParticipantReportData() {
		super();
	}
	
	public ContractParticipantReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
}
