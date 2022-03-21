package com.manulife.pension.ps.service.report.investment.reporthandler;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.investment.dao.InvestmentAllocationDetailsDAO;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 *
 *
 * @author nvintila
 * @date Feb 8, 2004
 * @time 3:50:40 PM
 */
public class InvestmentAllocationDetailsReportHandler implements ReportHandler {

    /**
     *
     */
    public ReportData getReportData(ReportCriteria criteria) throws SystemException {

        return InvestmentAllocationDetailsDAO.getReportData(criteria);
    }

}
