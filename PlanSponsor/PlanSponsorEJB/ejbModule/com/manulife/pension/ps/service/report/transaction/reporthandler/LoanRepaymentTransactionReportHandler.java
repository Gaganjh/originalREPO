
package com.manulife.pension.ps.service.report.transaction.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.transaction.dao.LoanRepaymentTransactionDAO;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author Ludmila Stern
 */
public class LoanRepaymentTransactionReportHandler implements ReportHandler {


	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {
		LoanRepaymentTransactionDAO dao = new LoanRepaymentTransactionDAO();

		LoanRepaymentTransactionReportData reportData = LoanRepaymentTransactionDAO.getData(criteria);

		return reportData;
	}
	
}