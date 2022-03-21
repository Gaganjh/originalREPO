/**
 * 
 */
package com.manulife.pension.ps.service.report.participant.valueobject;

import com.manulife.pension.ps.service.report.participant.reporthandler.ParticipantStatementsReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * @author arugupu
 *
 */
public class ParticipantStatementsReportData extends ReportData{

	/**
	 * Default Serial Version UID 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String REPORT_ID = ParticipantStatementsReportHandler.class.getName();
	
	public static final String REPORT_NAME = "participantStatementReport"; 
	public static final String FILTER_CONTRACT_NUMBER = "contractNumber";
	public static final String FILTER_LAST_NAME = "lastName";
	public static final String FILTER_SSN = "ssn";
	public static final String FILTER_FIRST_NAME = "firstName";
	public static final String FILTER_STMT_START_DATE = "stmtGenStartDate";
	public static final String FILTER_STMT_END_DATE = "stmtGenEndDate";
	
	public static final String SORT_LAST_NAME = "PARTICIPANT_LAST_NAME";
	
	public static final String SORT_FIRST_NAME = "PARTICIPANT_FIRST_NAME";
	
	public static final String SORT_SSN_FIELD = "SOCIAL_SECURITY_NO";
	
	public static final String DEFAULT_SORT = "PARTICIPANT_LAST_NAME, PARTICIPANT_FIRST_NAME, SOCIAL_SECURITY_NO";
	
	public static final int  PAGE_SIZE = 35; 
	
	/**
	 * Constructor
	 * 
	 * @param criteria
	 * @param totalCount
	 */
	public ParticipantStatementsReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}
	
}
