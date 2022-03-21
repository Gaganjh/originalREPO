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
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangeRecord;
import com.manulife.pension.ps.service.report.participant.payrollselfservice.valueobject.PayrollSelfServiceChangesReportData;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Data Access for PayrollSelfService Deferrals.
 *
 */
public class PayrollSelfServiceDeferralDAO extends PayrollSelfServiceBaseDAO {
	private static final String CLASSNAME = PayrollSelfServiceDeferralDAO.class.getName();

	private static final Logger LOGGER = Logger.getLogger(PayrollSelfServiceDeferralDAO.class);

	private static final String QUERY = "SELECT"
			+ " PCI.PROFILE_ID,"
			+ " PCI.CONTRACT_ID,"
			+ " PCI.CREATED_TS AS TIME_CREATED,"
			//+ " PCI.INSTRUCTION_NO,"
			//+ " PCI.INCREASE_AMT,"
			//+ " PCI.INCREASE_PCT,"
			+ " PCI.EFFECTIVE_DATE,"
			//+ " PCI.MONEY_SOURCE_CATEGORY_CODE,"
			//+ " PCI.MONEY_TYPE_CATEGORY_CODE,"
			+ " PCI.CONTRIBUTION_INSTRUCT_SRC_CODE AS CREATED_SOURCE_CODE,"
			+ " PCI.CONTRIBUTION_AMT,"
			//+ " PCI.CONTRIBUTION_FREQUENCY_CODE,"
			+ " PCI.CONTRIBUTION_PCT,"
			+ " COALESCE(PCI.MONEY_TYPE_CODE, 'EEDEF') AS MONEY_TYPE_CODE,"
			+ " PCI.PROCESSED_STATUS_CODE AS STATUS_CODE,"
			+ " PCI.PROCESSED_SOURCE_CODE,"
			+ " PCI.PROCESSED_TS AS TIME_PROCESSED,"
			+ " PCI.PROCESSED_USER_PROFILE_ID,"
			//+ " PCI.CONTRIBUTION_OLD_AMT,"
			//+ " PCI.CONTRIBUTION_OLD_PCT,"
			//+ " PCI.ACI_ANNIVERSARY_DATE,"
			+ " EC.SOCIAL_SECURITY_NO,"
			+ " EC.FIRST_NAME,"
			+ " EC.LAST_NAME,"
			+ " EC.MIDDLE_INITIAL,"
			+ " EC.EMPLOYER_DIVISION,"
			+ " EC.EMPLOYEE_ID,"
			+ " PC.PARTICIPANT_STATUS_CODE,"
			+ " PCI.CREATED_USER_ID_TYPE,"
			+ " PCI.PROCESSED_USER_ID_TYPE,"
			+ " CS.CONTRACT_NAME"
			+ " FROM EZK100.PARTICIPANT_CONTRB_INSTRUCTION PCI" 
			+ " INNER JOIN EZK100.CONTRACT_CS CS ON CS.CONTRACT_ID=PCI.CONTRACT_ID"
			+ " INNER JOIN EZK100.PARTICIPANT_CONTRACT PC ON PC.CONTRACT_ID=PCI.CONTRACT_ID"
			+ " 	AND PC.PROFILE_ID=PCI.PROFILE_ID AND NOT (COALESCE(PC.PARTICIPANT_STATUS_CODE,'AC')='CN')"
			+ " INNER JOIN PSW100.EMPLOYEE_CONTRACT EC ON EC.CONTRACT_ID=PCI.CONTRACT_ID AND EC.PROFILE_ID=PCI.PROFILE_ID"
			+ " WHERE PCI.CONTRACT_ID=?"
			+ " 	AND PCI.CONTRIBUTION_INSTRUCT_SRC_CODE IN ('AC', 'CI') AND PCI.EFFECTIVE_DATE IS NOT NULL";

	/**
	 * Default Constructor
	 */
	public PayrollSelfServiceDeferralDAO() {
		// empty constructor - sonarqube rules
	}

	@SuppressWarnings("unchecked")
	public ReportData getReportData(final ReportCriteria criteria) throws SystemException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("entry -> getReportData");
			LOGGER.debug("Report criteria -> " + criteria.toString());
		}
		criteria.getFilters().put(RECORD_TYPE_KEY, RECORD_TYPE_DEFERRAL);
		try (Connection connection = getReadUncommittedConnection(CLASSNAME, CUSTOMER_DATA_SOURCE_NAME)) {
			// Get the connection object which will be used for invoking the helper methods
			List<PayrollSelfServiceChangeRecord> deferrals = fetchData(criteria, connection);
			int deferralCount = deferrals == null ? 0 : deferrals.size();
			PayrollSelfServiceChangesReportData reportData = new PayrollSelfServiceChangesReportData(criteria, deferralCount);
			reportData.setDetails(deferrals);

			return reportData;
		} catch (SQLException se) {
			throw new SystemException(se, "SQLException generated:" + se.getMessage());
		}
	}

	private List<PayrollSelfServiceChangeRecord> fetchData(final ReportCriteria reportCriteria, Connection connection)
			throws SQLException {
		PayrollSelfServiceSearchCriteria searchCriteria = collectCriteria(reportCriteria);
		List<PayrollSelfServiceChangeRecord> results = new ArrayList<>();
		final String sqlStatement = createSQLStatement(createFilterPhrase(searchCriteria));
		try (PreparedStatement stmt = connection.prepareStatement(sqlStatement)) {
			stmt.setInt(1, searchCriteria.getContractId().intValue());
			stmt.execute();
			try (ResultSet resultSet = stmt.getResultSet()) {
				while(resultSet.next()) {
					Collection<PayrollSelfServiceChangeRecord> changeRecords = PayrollSelfServiceDAOUtils.getDeferralChangeRecords(resultSet);
					if(CollectionUtils.isNotEmpty(changeRecords)) {
						results.addAll(changeRecords);
					}
				}
			}
		}

		return results;
	}

	protected final String createSQLStatement(String filter) {
		return String.join(" ", PayrollSelfServiceDeferralDAO.QUERY, filter);
	}

	protected String createFilterPhrase(PayrollSelfServiceSearchCriteria searchCriteria) {
		StringBuilder filterBuilder = new StringBuilder("AND PCI.PROCESSED_STATUS_CODE IN ('PA', 'AP')");
		
		if (StringUtils.isNotEmpty(searchCriteria.getLastNameValue())) {
			filterBuilder.append(" AND EC.last_name LIKE ")
				.append(wrapInSingleQuotes(searchCriteria.getLastNameValue().toUpperCase().concat("%")));
		} else if (StringUtils.isNotEmpty(searchCriteria.getSsnValue())) {
			filterBuilder.append(" AND EC.SOCIAL_SECURITY_NO = ")
				.append(wrapInSingleQuotes(searchCriteria.getSsnValue()));
		}
		
		if (StringUtils.isNotEmpty(searchCriteria.getEffectiveDateFrom())) {
			filterBuilder.append(" AND PCI.EFFECTIVE_DATE >= date(")
				.append(wrapInSingleQuotes(searchCriteria.getEffectiveDateFrom())).append(")");
		}
		
		if (StringUtils.isNotEmpty(searchCriteria.getEffectiveDateTo())) {
			filterBuilder.append(" AND PCI.EFFECTIVE_DATE <= date(")
				.append(wrapInSingleQuotes(searchCriteria.getEffectiveDateTo())).append(")");
		}
		
		return filterBuilder.toString();
	}


}


