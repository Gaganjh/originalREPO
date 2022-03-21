package com.manulife.pension.ps.service.report.contract.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.dao.SelectContractParticipantDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ContractParticipantListingReportHandler implements ReportHandler {

	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
		return SelectContractParticipantDAO.getContractParticipantList(reportCriteria);
	}

}
