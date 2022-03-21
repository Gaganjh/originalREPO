package com.manulife.pension.ps.service.report.profiles.reporthandler;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.profiles.valueobject.TpaUserSummaryReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.dao.SearchDAO;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.UserInfo;


/**
 * @author marcest
 *
 */
public class TpaUserSummaryReportHandler implements ReportHandler {
	
	public static final String REPORT_ID = TpaUserSummaryReportHandler.class.getName(); 
	public static final String REPORT_NAME = "TpaUserSummaryReport";
    
	/* (non-Javadoc)
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {
        ReportData data = new TpaUserSummaryReportData();
		
		List<TPAFirmInfo> firmInfos = (List<TPAFirmInfo>)reportCriteria.getFilterValue( TpaUserSummaryReportData.FILTER_FIRM_IDS );
		List<UserInfo> details = null;
		
		if (firmInfos.size() > 0)
		{
			details = SearchDAO.searchTpaUserFirmPermissions(firmInfos);
		}
		else
		{
			details = new ArrayList<UserInfo>();
		}

		data.setReportCriteria(reportCriteria);
        data.setDetails(details);
        return data;
	}
    
}
