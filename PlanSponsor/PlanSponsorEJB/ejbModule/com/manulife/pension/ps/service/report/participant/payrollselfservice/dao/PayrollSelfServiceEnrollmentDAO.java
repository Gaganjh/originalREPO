package com.manulife.pension.ps.service.report.participant.payrollselfservice.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceEnrollmentRecord;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Data Access for PayrollSelfService Enrollments.
 *
 */
public class PayrollSelfServiceEnrollmentDAO extends PayrollSelfServiceBaseDAO {

	private static final Logger LOGGER = Logger.getLogger(PayrollSelfServiceEnrollmentDAO.class);
	private static final String CLASSNAME = PayrollSelfServiceEnrollmentDAO.class.getName();

	private static final String QUERY = "SELECT"
			+ " PEI.CONTRACT_ID,"
			+ " PEI.PROFILE_ID,"	
			+ " PEI.ENROLLMENT_EFFECTIVE_DATE AS EFFECTIVE_DATE,"
			+ " PEI.ENROLLMENT_PROCESSED_DATE AS TIME_CREATED,"
			+ " EC.SOCIAL_SECURITY_NO,"
			+ " EC.FIRST_NAME,"
			+ " EC.LAST_NAME,"
			+ " EC.MIDDLE_INITIAL,"
			+ " EC.EMPLOYER_DIVISION,"
			+ " EC.EMPLOYEE_ID,"
			+ " EC.BEFORE_TAX_DEFER_AMT,"
			+ " EC.BEFORE_TAX_DEFER_PCT,"
			+ " EC.DESIG_ROTH_DEF_AMT,"
			+ " EC.DESIG_ROTH_DEF_PCT,"
			+ " PC.PARTICIPANT_STATUS_CODE,"
			+ " PC.ENROLL_BEFORE_TAX_DEFER_PCT,"
			+ " PC.ENROLL_BEFORE_TAX_DEFER_AMT,"
			+ " PC.ENROLL_DESIG_ROTH_DEF_PCT,"
			+ " PC.ENROLL_DESIG_ROTH_DEF_AMT,"
			+ " PC.ENROLLMENT_PROCESSED_DATE,"
			+ " PC.ENROLLMENT_METHOD_CODE AS CREATED_SOURCE_CODE,"
			+ " CS.CONTRACT_NAME"
			+ " FROM PSW100.PARTICIPANT_ENROLLMENT_INFO PEI"
			+ " INNER JOIN PSW100.EMPLOYEE_CONTRACT EC ON EC.CONTRACT_ID = PEI.CONTRACT_ID AND EC.PROFILE_ID = PEI.PROFILE_ID"
			+ " INNER JOIN EZK100.CONTRACT_CS CS ON CS.CONTRACT_ID = PEI.CONTRACT_ID"
			+ " INNER JOIN EZK100.PARTICIPANT_CONTRACT PC ON PC.CONTRACT_ID = PEI.CONTRACT_ID"
			+ " 	AND PC.PROFILE_ID = PEI.PROFILE_ID AND NOT (COALESCE(PC.PARTICIPANT_STATUS_CODE,'AC')='CN')"
			+ " WHERE PEI.CONTRACT_ID = ?"
			; 

	@SuppressWarnings("unchecked")
	public ReportData getReportData(final ReportCriteria criteria) throws SystemException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("entry -> getReportData");
			LOGGER.debug("Report criteria -> " + criteria.toString());
		}
		criteria.getFilters().put(RECORD_TYPE_KEY, RECORD_TYPE_ENROLLMENT);
		try (Connection connection = getReadUncommittedConnection(CLASSNAME, CUSTOMER_DATA_SOURCE_NAME)) {
			// Get the connection object which will be used for invoking the helper methods
			List<PayrollSelfServiceEnrollmentRecord> enrollments = fetchData(criteria, connection);
			int enrollmentCount = enrollments == null ? 0 : enrollments.size();
			PayrollSelfServiceChangesReportData reportData = new PayrollSelfServiceChangesReportData(criteria, enrollmentCount);
			reportData.setDetails(enrollments);	

			return reportData;
		} catch (SQLException se) {
			throw new SystemException(se, "SQLException generated:" + se.getMessage());
		}
	}

	private List<PayrollSelfServiceEnrollmentRecord> fetchData(final ReportCriteria reportCriteria, Connection connection)
			throws SQLException, SystemException {
		PayrollSelfServiceSearchCriteria searchCriteria = collectCriteria(reportCriteria);
		List<PayrollSelfServiceEnrollmentRecord> results = new ArrayList<>();
		final String sqlStatement = createSQLStatement(createFilterPhrase(searchCriteria));
		try (PreparedStatement stmt = connection.prepareStatement(sqlStatement)) {
			stmt.setInt(1, searchCriteria.getContractId().intValue());			
			stmt.execute();
			try (ResultSet resultSet = stmt.getResultSet()) {
				while(resultSet.next()) {
					Collection<PayrollSelfServiceEnrollmentRecord> changeRecords = PayrollSelfServiceDAOUtils.getEnrollmentChangeRecords(resultSet);
					if(CollectionUtils.isNotEmpty(changeRecords)) {
						results.addAll(changeRecords);
					}
				}
			}
		}

		return results;
	}

	private static String createSQLStatement(String filter) {
		return String.join(" ", PayrollSelfServiceEnrollmentDAO.QUERY, filter);
	}

	protected static String createFilterPhrase(PayrollSelfServiceSearchCriteria searchCriteria) {
		StringBuilder filterBuilder = new StringBuilder();
		if(StringUtils.isNotEmpty(searchCriteria.getLastNameValue())) {
			filterBuilder.append(" AND EC.LAST_NAME LIKE ")
			.append(wrapInSingleQuotes(searchCriteria.getLastNameValue().toUpperCase().concat("%")));
		} else if (StringUtils.isNotEmpty(searchCriteria.getSsnValue())) {
			filterBuilder.append(" AND EC.SOCIAL_SECURITY_NO = ")
			.append(wrapInSingleQuotes(searchCriteria.getSsnValue()));
		}
		if(StringUtils.isNotEmpty(searchCriteria.getEffectiveDateFrom())) {
			filterBuilder.append(" AND COALESCE(PEI.ENROLLMENT_EFFECTIVE_DATE,PC.ENROLLMENT_PROCESSED_DATE) >= DATE(")
			.append(wrapInSingleQuotes(searchCriteria.getEffectiveDateFrom()))
			.append(")");
		}
		if(StringUtils.isNotEmpty(searchCriteria.getEffectiveDateTo())) {
			filterBuilder.append(" AND COALESCE(PEI.ENROLLMENT_EFFECTIVE_DATE,PC.ENROLLMENT_PROCESSED_DATE) <= DATE(")
			.append(wrapInSingleQuotes(searchCriteria.getEffectiveDateTo()))
			.append(")");
		}

		filterBuilder.append(" AND ")
		.append(" (")
		.append(" PC.ENROLL_BEFORE_TAX_DEFER_PCT IS NOT NULL ")
		.append(" OR PC.ENROLL_BEFORE_TAX_DEFER_AMT IS NOT NULL")
		.append(" OR PC.ENROLL_DESIG_ROTH_DEF_PCT IS NOT NULL")
		.append(" OR PC.ENROLL_DESIG_ROTH_DEF_AMT IS NOT NULL")
		.append(")")	
		;

		return filterBuilder.toString();
	}	

}
