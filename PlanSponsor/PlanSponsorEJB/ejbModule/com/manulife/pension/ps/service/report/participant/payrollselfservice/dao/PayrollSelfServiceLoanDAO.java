package com.manulife.pension.ps.service.report.participant.payrollselfservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Data Access for PayrollSelfService Loans.
 *
 */
public class PayrollSelfServiceLoanDAO extends PayrollSelfServiceBaseDAO {
	private static final String CLASSNAME = PayrollSelfServiceLoanDAO.class.getName();

	private static final Logger LOGGER = Logger.getLogger(PayrollSelfServiceLoanDAO.class);

	private static final String QUERY = "SELECT"
			+ " PALO.PROFILE_ID,"
			+ " PALO.CONTRACT_ID,"
			+ " PALO.LOAN_ID AS LOAN_NUMBER,"
			+ " PALO.LOAN_CREATE_DATE AS TIME_CREATED,"
			+ " PALO.ISSUE_TRANSACTION_EFFECTIVE_DATE,"
			+ " PALO.ISSUE_TRANSACTION_PROCESSING_DATE,"
			+ " PALO.LOAN_ORIGIN_CODE,"
			+ " PALO.EFFECTIVE_DATE,"
			+ " PALO.MATURITY_DATE,"
			+ " PALO.MEDIUM_TYPE_CODE AS CREATED_SOURCE_CODE,"	
			+ " PALO.LOAN_PRINCIPAL_AMT,"	
			+ " PALO.LOAN_INTEREST_RATE_PCT,"	
			+ " PALO.TOTAL_INTEREST_AMOUNT,"
			+ " PALO.LOAN_GOAL_AMOUNT,"
			+ " PALO.NUMBER_OF_PAYMENTS,"
			+ " PALO.EXPECTED_REPAYMENT_AMOUNT,"
			// Participant do not have User Profile so getting first name and last name from employee contract
			+ " (CASE WHEN LOSU.CREATED_BY_ROLE_CODE = 'PA' THEN EMCO.LAST_NAME ELSE LOSUUSPR.LAST_NAME END) AS SUBMITTED_BY_LAST_NAME,"
			+ " (CASE WHEN LOSU.CREATED_BY_ROLE_CODE = 'PA' THEN EMCO.FIRST_NAME ELSE LOSUUSPR.FIRST_NAME END) AS SUBMITTED_BY_FIRST_NAME,"
			+ " PALO.LOAN_STATUS_CODE,"
			+ " PALO.LOAN_STATUS_DATE,"
			+ " DATE(COSVFTHS_PSS_Y.LAST_UPDATED_TS) AS PFS_ADDED_DATE,"
			+ " EMCO.SOCIAL_SECURITY_NO,"
			+ " EMCO.FIRST_NAME,"
			+ " EMCO.LAST_NAME,"
			+ " EMCO.MIDDLE_INITIAL,"
			+ " EMCO.EMPLOYER_DIVISION,"
			+ " EMCO.EMPLOYEE_ID,"
			+ " PACO.PARTICIPANT_STATUS_CODE,"
			+ " COCS.CONTRACT_NAME"
			+ " FROM PSW100.PARTICIPANT_LOAN_EX PALO" 
			+ " INNER JOIN PSW100.CONTRACT_CS COCS ON COCS.CONTRACT_ID=PALO.CONTRACT_ID"
			// Only non-cancelled Participants
			+ " INNER JOIN PSW100.PARTICIPANT_CONTRACT PACO ON PACO.CONTRACT_ID=PALO.CONTRACT_ID"
			+ " 	AND PACO.PROFILE_ID=PALO.PROFILE_ID AND NOT (COALESCE(PACO.PARTICIPANT_STATUS_CODE,'AC')='CN')"
			+ " INNER JOIN PSW100.EMPLOYEE_CONTRACT EMCO ON EMCO.CONTRACT_ID=PALO.CONTRACT_ID AND EMCO.PROFILE_ID=PALO.PROFILE_ID"
			// Only Payroll Feedback Service Contracts
			+ " INNER JOIN PSW100.CONTRACT_SERVICE_FEATURE COSVFT_PSS_Y ON COSVFT_PSS_Y.CONTRACT_ID=PALO.CONTRACT_ID "
			+ " 	AND COSVFT_PSS_Y.SERVICE_FEATURE_CODE='PSS' AND COSVFT_PSS_Y.SERVICE_FEATURE_VALUE='Y'"
			// Get earliest Payroll Feedback Service addition
			+ " INNER JOIN PSW100.CONTRACT_SERVICE_FEATURE_HISTORY COSVFTHS_PSS_Y ON COSVFTHS_PSS_Y.CONTRACT_ID=PALO.CONTRACT_ID "
			+ " 	AND COSVFTHS_PSS_Y.SERVICE_FEATURE_CODE='PSS' AND COSVFTHS_PSS_Y.SERVICE_FEATURE_VALUE='Y' "
			+ " 	AND COSVFTHS_PSS_Y.LAST_UPDATED_TS=(SELECT MIN(LAST_UPDATED_TS) FROM PSW100.CONTRACT_SERVICE_FEATURE_HISTORY "
			+ " 		WHERE CONTRACT_ID=PALO.CONTRACT_ID AND SERVICE_FEATURE_CODE='PSS' AND SERVICE_FEATURE_VALUE='Y') "
			// Get Submission Actor
			+ " LEFT JOIN STP100.SUBMISSION_LOAN LOSU ON PALO.SUBMISSION_ID IS NOT NULL AND LOSU.SUBMISSION_ID=PALO.SUBMISSION_ID "
			+ " LEFT JOIN PSW100.USER_PROFILE LOSUUSPR ON LOSU.CREATED_BY_ROLE_CODE <> 'PA' AND LOSU.CREATED_USER_PROFILE_ID IS NOT NULL AND LOSUUSPR.USER_PROFILE_ID=LOSU.CREATED_USER_PROFILE_ID "
			+ " WHERE PALO.CONTRACT_ID=?"
			;
	
	/**
	 * Default Constructor
	 */
	public PayrollSelfServiceLoanDAO() {
		// empty constructor - sonarqube rules
	}

	public ReportData getReportData(final ReportCriteria criteria) throws SystemException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("entry -> getReportData");
			LOGGER.debug("Report criteria -> " + criteria.toString());
		}
		try (Connection connection = getReadUncommittedConnection(CLASSNAME, CUSTOMER_DATA_SOURCE_NAME)) {
			// Get the connection object which will be used for invoking the helper methods
			List<PayrollSelfServiceChangeRecord> loanRecords = fetchData(criteria, connection);
			int loanRecordCount = loanRecords == null ? 0 : loanRecords.size();
			PayrollSelfServiceChangesReportData reportData = new PayrollSelfServiceChangesReportData(criteria, loanRecordCount);
			reportData.setDetails(loanRecords);

			return reportData;
		} catch (SQLException se) {
			throw new SystemException(se, "SQLException generated:" + se.getMessage());
		}
	}

	private List<PayrollSelfServiceChangeRecord> fetchData(final ReportCriteria reportCriteria, Connection connection)
			throws SQLException {
		PayrollSelfServiceSearchCriteria searchCriteria = collectCriteria(reportCriteria);
		Date effectiveDateFromCriterion = parseFilterTextDate(searchCriteria.getEffectiveDateFrom());
		Date effectiveDateToCriterion = parseFilterTextDate(searchCriteria.getEffectiveDateTo());		
		
		List<PayrollSelfServiceChangeRecord> results = new ArrayList<>();
		final String sqlStatement = createSQLStatement(createFilterPhrase(searchCriteria));

		try (PreparedStatement stmt = connection.prepareStatement(sqlStatement)) {
			stmt.setInt(1, searchCriteria.getContractId().intValue());
			stmt.execute();
			try (ResultSet resultSet = stmt.getResultSet()) {
				while(resultSet.next()) {
					Collection<PayrollSelfServiceChangeRecord> changeRecords = PayrollSelfServiceDAOUtils.getLoanChangeRecords(resultSet, effectiveDateFromCriterion, effectiveDateToCriterion);
					if(CollectionUtils.isNotEmpty(changeRecords)) {
						results.addAll(changeRecords);
					}
				}
			}
		}
	
		return results;
	}

	private Date parseFilterTextDate(String dateAsText) {
		try {
			if(StringUtils.isNotBlank(dateAsText)) {
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				return dateFormat.parse(dateAsText);
			}
		} catch (ParseException e) {
			LOGGER.error("Failed to convert [" + dateAsText + "] to Date!");
		}
		
		return null;
	}

	protected final String createSQLStatement(String filter) {
		return String.join(" ", PayrollSelfServiceLoanDAO.QUERY, filter);
	}

	protected String createFilterPhrase(PayrollSelfServiceSearchCriteria searchCriteria) {
		StringBuilder filterBuilder = new StringBuilder("");
		
		if(StringUtils.isNotEmpty(searchCriteria.getLastNameValue())) {
			filterBuilder.append(" AND EMCO.last_name LIKE ")
				.append(wrapInSingleQuotes(searchCriteria.getLastNameValue().toUpperCase().concat("%")));
		} else if (StringUtils.isNotEmpty(searchCriteria.getSsnValue())) {
			filterBuilder.append(" AND EMCO.SOCIAL_SECURITY_NO = ")
				.append(wrapInSingleQuotes(searchCriteria.getSsnValue()));
		}
		
		if(StringUtils.isNotEmpty(searchCriteria.getEffectiveDateFrom())) {
			String dateAsString = wrapInSingleQuotes(searchCriteria.getEffectiveDateFrom());
			filterBuilder.append(" AND ( ");
			filterBuilder.append(" (PALO.ISSUE_TRANSACTION_EFFECTIVE_DATE IS NOT NULL AND PALO.ISSUE_TRANSACTION_EFFECTIVE_DATE>=DATE(").append(dateAsString).append(")) ");
			filterBuilder.append(" OR ");
			filterBuilder.append(" (COALESCE(PALO.LOAN_STATUS_CODE,'LNAC') != 'LNAC' AND PALO.LOAN_STATUS_DATE IS NOT NULL AND PALO.LOAN_STATUS_DATE>=DATE(").append(dateAsString).append(")) ");
			filterBuilder.append(") ");
		}
		
		if(StringUtils.isNotEmpty(searchCriteria.getEffectiveDateTo())) {
			String dateAsString = wrapInSingleQuotes(searchCriteria.getEffectiveDateTo());
			filterBuilder.append(" AND ( ");
			filterBuilder.append(" (PALO.ISSUE_TRANSACTION_EFFECTIVE_DATE IS NOT NULL AND PALO.ISSUE_TRANSACTION_EFFECTIVE_DATE<=DATE(").append(dateAsString).append(")) ");
			filterBuilder.append(" OR ");
			filterBuilder.append(" (COALESCE(PALO.LOAN_STATUS_CODE,'LNAC') != 'LNAC' AND PALO.LOAN_STATUS_DATE IS NOT NULL AND PALO.LOAN_STATUS_DATE<=DATE(").append(dateAsString).append(")) ");
			filterBuilder.append(") ");
		}
		
		return filterBuilder.toString();
	}

}


