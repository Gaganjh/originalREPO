package com.manulife.pension.ps.service.report.participant.payrollselfservice.reporthandler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.dao.PayrollSelfServiceActivityHistoryReportDeferralDAO;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.dao.PayrollSelfServiceEnrollmentDAO;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.dao.PayrollSelfServiceLoanDAO;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeViewRecordType;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class PayrollSelfServiceChangesActivityHistoryReportHandler implements ReportHandler {

	PayrollSelfServiceActivityHistoryReportDeferralDAO payrollSelfServiceActivityHistoryReportDeferralDAO = new PayrollSelfServiceActivityHistoryReportDeferralDAO();
	PayrollSelfServiceEnrollmentDAO payrollSelfServiceEnrollmentDAO = new PayrollSelfServiceEnrollmentDAO();

	@SuppressWarnings("unchecked")
	public ReportData getReportData(ReportCriteria criteria) throws SystemException {

		List<PayrollSelfServiceChangeRecord> mergedReportDetails = new ArrayList<>();
		String recordType = (String) criteria.getFilterValue(PayrollSelfServiceChangesReportData.FILTER_RECORD_TYPE);

		// Enrollments
		if(PayrollSelfServiceChangeViewRecordType.ALL.getCode().equalsIgnoreCase(recordType)
				|| PayrollSelfServiceChangeViewRecordType.ENROLLMENT.getCode().equalsIgnoreCase(recordType)) {
			ReportData reportData = payrollSelfServiceEnrollmentDAO.getReportData(criteria);
			if(reportData != null 
					&& CollectionUtils.isNotEmpty(reportData.getDetails())) {
				mergedReportDetails.addAll(reportData.getDetails());
			}
		}

		// Deferrals	
		if (PayrollSelfServiceChangeViewRecordType.ALL.getCode().equalsIgnoreCase(recordType)
				|| PayrollSelfServiceChangeViewRecordType.DEFERRAL.getCode().equalsIgnoreCase(recordType)) {
			ReportData reportData = payrollSelfServiceActivityHistoryReportDeferralDAO.getReportData(criteria);
			if (reportData != null 
					&& CollectionUtils.isNotEmpty(reportData.getDetails())) {
				mergedReportDetails.addAll(reportData.getDetails());
			}
		}	

		// Loans
		if(Boolean.TRUE.equals(criteria.getFilterValue(PayrollSelfServiceChangesReportData.FILTER_INCLUDE_LOANS))) {
			if(PayrollSelfServiceChangeViewRecordType.ALL.getCode().equalsIgnoreCase(recordType)
					|| PayrollSelfServiceChangeViewRecordType.LOAN.getCode().equalsIgnoreCase(recordType)) {
				ReportData reportData = new PayrollSelfServiceLoanDAO().getReportData(criteria);
				if (reportData != null 
						&& CollectionUtils.isNotEmpty(reportData.getDetails())) {
					mergedReportDetails.addAll(reportData.getDetails());
				}
			}
		}

		//Create a new report and return
		PayrollSelfServiceChangesReportData mergedReportData = new PayrollSelfServiceChangesReportData(criteria,
				mergedReportDetails.size());	
		mergedReportData.setDetails(new ArrayList<PayrollSelfServiceChangeRecord>(mergedReportDetails));

		return mergedReportData;
	}


}
