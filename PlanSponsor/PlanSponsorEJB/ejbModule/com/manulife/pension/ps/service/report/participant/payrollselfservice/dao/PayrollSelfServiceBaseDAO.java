package com.manulife.pension.ps.service.report.participant.payrollselfservice.dao;

import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public abstract class PayrollSelfServiceBaseDAO extends BaseDatabaseDAO {

	protected static final String RECORD_TYPE_KEY = "RECORD_TYPE";
	protected static final String RECORD_TYPE_ENROLLMENT = "ENROLLMENT";
	protected static final String RECORD_TYPE_DEFERRAL = "DEFERRAL";

	protected static final String EMPLOYEE_CONTRACT_ALIASE = "EC.";
	protected static final String PARTICIPANT_CONTRB_INSTRUCTION_ALIASE = "PCI.";
	protected static final String PARTICIPANT_ENROLLMENT_INFO_ALIASE = "PEI.";
	protected static final String INITIATED = "initiated";

	protected PayrollSelfServiceSearchCriteria collectCriteria(final ReportCriteria criteria) {
		return new PayrollSelfServiceSearchCriteria(
				(Integer) criteria.getFilterValue(PayrollSelfServiceChangesReportData.FILTER_CONTRACT_ID),
				(String) criteria.getFilterValue(PayrollSelfServiceChangesReportData.FILTER_LAST_NAME),
				(String) criteria.getFilterValue(PayrollSelfServiceChangesReportData.FILTER_SSN),
				(String) criteria.getFilterValue(PayrollSelfServiceChangesReportData.FILTER_FROM_EFFECTIVE_DATE),
				(String) criteria.getFilterValue(PayrollSelfServiceChangesReportData.FILTER_TO_EFFECTIVE_DATE),
				(String) criteria.getFilters().get(RECORD_TYPE_KEY));
	}

	@Getter
	@Setter
	@AllArgsConstructor
	protected class PayrollSelfServiceSearchCriteria {
		private Integer contractId;
		private String lastNameValue;
		private String ssnValue;
		private String effectiveDateFrom;
		private String effectiveDateTo;
		private String recordType;
	}
	
}
