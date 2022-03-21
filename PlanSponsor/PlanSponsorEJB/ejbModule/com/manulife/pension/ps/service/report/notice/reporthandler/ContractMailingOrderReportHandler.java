/**
 * 
 */
package com.manulife.pension.ps.service.report.notice.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.dao.ContractMailingOrderDao;
import com.manulife.pension.ps.service.report.notice.valueobject.ContractMailingOrderReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author krishta
 *
 */
public class ContractMailingOrderReportHandler implements ReportHandler {

	/* (non-Javadoc)
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	@Override
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
		ContractMailingOrderReportData reportData = new ContractMailingOrderReportData(reportCriteria, 0);
		 ContractMailingOrderDao.getReportData(reportCriteria, reportData);
	return reportData;
	}

}
