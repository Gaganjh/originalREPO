package com.manulife.pension.ps.service.report.tpabob.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.tpabob.dao.TPABlockOfBusinessDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class is used to call the DAO to get the TPA BOB report information.
 * 
 * @author HArlomte
 * 
 */
public class TPABlockOfBusinessReportHandler implements ReportHandler {

    /**
     * This method will call the TPABlockOfBusinessDAO to get the TPA BOB report information.
     */
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {
        ReportData reportData = null;

        reportData = TPABlockOfBusinessDAO.getReportData(reportCriteria);

        return reportData;
    }

}
