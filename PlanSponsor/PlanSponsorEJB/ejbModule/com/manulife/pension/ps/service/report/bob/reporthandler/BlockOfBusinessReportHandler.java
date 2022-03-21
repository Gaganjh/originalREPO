package com.manulife.pension.ps.service.report.bob.reporthandler;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.bob.dao.BlockOfBusinessDAO;
import com.manulife.pension.ps.service.report.bob.valueobject.BlockOfBusinessReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class will call the DAO to retrieve the report data to be shown on BOB page.
 * 
 * @author harlomte
 * 
 */
public class BlockOfBusinessReportHandler implements ReportHandler {
    private static final Logger logger = Logger.getLogger(BlockOfBusinessReportHandler.class);

    /**
     * This method gets the BOB report data by calling the BlockOfBusinessDAO.
     */
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData()");
        }

        BlockOfBusinessReportData bobReportData = (BlockOfBusinessReportData) BlockOfBusinessDAO
                .getReportData(reportCriteria);
        
        if (logger.isDebugEnabled()) {
            logger.debug("exit -> getReportData()");
        }
        return bobReportData;
    }
}
