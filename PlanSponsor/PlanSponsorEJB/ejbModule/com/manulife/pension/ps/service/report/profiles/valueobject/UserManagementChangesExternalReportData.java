package com.manulife.pension.ps.service.report.profiles.valueobject;

import java.util.Vector;

import com.manulife.pension.ps.service.report.profiles.reporthandler.UserManagementChangesExternalReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author marcest
 *
 */
public class UserManagementChangesExternalReportData extends ReportData {
	private static final long serialVersionUID = 8905620136209683895L;
	
	public static final String FILTER_ACTION = "FILTER_ACTION";
    public static final String FILTER_CHANGED_BY_TEAM_CODE = "FILTER_CHANGED_BY_TEAM_CODE";
	public static final String FILTER_TEAM_CODE = "FILTER_TEAM_CODE";
	public static final String FILTER_CONTRACT_NUMBER = "FILTER_CONTRACT_NUMBER";
	public static final String FILTER_START_DATE = "FILTER_START_DATE";
	public static final String FILTER_END_DATE = "FILTER_END_DATE";
	public static final String FILTER_LAST_NAME = "FILTER_LAST_NAME";
	public static final String FILTER_ROLES = "FILTER_ROLES";
	public static final String FILTER_USER_TYPE = "FILTER_USER_TYPE";
	
	public UserManagementChangesExternalReportData() {
		super( new ReportCriteria(UserManagementChangesExternalReportHandler.REPORT_ID), 0);
		setDetails( new Vector() );
	}


}
