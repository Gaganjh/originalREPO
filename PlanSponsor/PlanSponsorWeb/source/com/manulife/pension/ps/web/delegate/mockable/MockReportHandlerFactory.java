package com.manulife.pension.ps.web.delegate.mockable;

import com.manulife.pension.ps.service.report.transaction.valueobject.ContributionTransactionReportData;
import com.manulife.pension.service.report.handler.ReportHandlerFactory;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;

/**
 * Mock ReportHandler factory Singleton - returns the class name of the specific
 * Report Handler that maps into the key value of the report id. It could use
 * the data retrieved from the backend or return mock objects using
 * MockReportHandlers
 * 
 * Created on May 4, 2004
 * 
 * @author drotele
 *  
 */
public class MockReportHandlerFactory extends ReportHandlerFactory {

	/**
	 * Creates set of Mock Report Handlers
	 */
	protected void init() {
		theMap.put(ContributionTransactionReportData.REPORT_ID,
			new MockContributionTransactionReportHandler());
		theMap.put(ManageUsersReportData.REPORT_ID,
			new MockManageUsersReportHandler());
	}
}