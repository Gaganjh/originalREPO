package com.manulife.pension.bd.service.brokerListing.reporthandler;

import com.manulife.pension.bd.service.brokerListing.dao.BrokerListingDAO;
import com.manulife.pension.bd.service.brokerListing.valueobject.BrokerListingReportData;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * This class is used to retrieve the Report Data for Broker Listing Report.
 * 
 * @author HArlomte
 * 
 */
public class BrokerListingReportHandler implements ReportHandler {

    /**
     * This method retrieves the Report data specific to Broker Listing report.
     */
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException,
            ReportServiceException {
        BrokerListingReportData brokerListingReportData = (BrokerListingReportData) BrokerListingDAO
                .getReportData(reportCriteria);
        return brokerListingReportData;
    }

    
}
