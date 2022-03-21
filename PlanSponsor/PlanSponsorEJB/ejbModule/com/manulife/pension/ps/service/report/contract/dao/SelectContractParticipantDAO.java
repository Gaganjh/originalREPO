package com.manulife.pension.ps.service.report.contract.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.valueobject.ContractParticipantReportData;
import com.manulife.pension.service.contract.valueobject.ContractParticipantVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSortList;

public class SelectContractParticipantDAO extends BaseDatabaseDAO {
	private static final Logger log = Logger
			.getLogger(SelectContractParticipantDAO.class);

	private static final String SELECT_CONTRACT_PARTICIPANT_LIST = "call "
			+ CUSTOMER_SCHEMA_NAME
			+ "SELECT_CONTRACT_LEVEL_REGEN_PARTICIPANT(?,?,?,?,?)";

	private static final String className = SelectContractParticipantDAO.class
			.getName();

	private static final Map fieldToColumnMap = new HashMap();

	static {
		fieldToColumnMap.put(ContractParticipantReportData.SORT_NAME, "name");
		fieldToColumnMap.put(ContractParticipantReportData.SORT_SSN, "ssn");
		fieldToColumnMap.put(
				ContractParticipantReportData.SORT_DEFAULT_ENROLLED,
				"defEnroll");
		fieldToColumnMap.put(ContractParticipantReportData.SORT_PIN_STATUAS,
				"pinStat");
		fieldToColumnMap.put(ContractParticipantReportData.SORT_ENROLLMENT_DATE,
				"enrollDate");
		fieldToColumnMap.put(ContractParticipantReportData.SORT_PIN_TRANS_CREATED,
				"pinTranCrtInd");
	}

	protected static String getSortPhrase(ReportCriteria criteria) {
		ReportSortList list = criteria.getSorts();
		String sortName = (String) fieldToColumnMap.get(list.get(0).getSortField());
		String direction = list.get(0).getSortDirection();
		String sortPhrase = sortName + Character.toUpperCase(direction.charAt(0)) + direction.substring(1);
		return sortPhrase;
	}
	
	public static ReportData getContractParticipantList(ReportCriteria criteria)
			throws SystemException {
		if (log.isDebugEnabled()) {
			log.debug("entry -> getContractParticipantList");
			log.debug("Report criteria -> " + criteria.toString());
		}

		Connection conn = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;

		try {
			// setup the connection and the statement
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareCall(SELECT_CONTRACT_PARTICIPANT_LIST);
			String sortPhrase = getSortPhrase(criteria);
			
			if (log.isInfoEnabled()) {
				log.debug("Calling Stored Procedure: "
						+ SELECT_CONTRACT_PARTICIPANT_LIST);
				log.debug("Search criteria sorting:"
						+ sortPhrase);
			}

			statement
					.setInt(
							1,
							((Integer) criteria
									.getFilterValue(ContractParticipantReportData.FILTER_CONTRACT_ID))
									.intValue());
			statement.setString(2, sortPhrase);
			statement.setInt(3, criteria.getPageSize());
			statement.setInt(4, criteria.getStartIndex());
			
			statement.registerOutParameter(5, java.sql.Types.DECIMAL);

			statement.execute();

			// for count
			resultSet = statement.getResultSet();
			
			if (resultSet == null) {
			    throw new SystemException(
	                    "No total count during "
                        + SELECT_CONTRACT_PARTICIPANT_LIST
                        + " stored proc call."
                        + "Input parameters are "
                        + "clientId:"
                        + criteria
                                .getFilterValue(ContractParticipantReportData.FILTER_CONTRACT_ID));
			}
			resultSet.next();
			int totalCount = resultSet.getInt(1);
			ContractParticipantReportData reportData = new ContractParticipantReportData(
					criteria, totalCount);

			if (log.isInfoEnabled()) {
				log.debug("Calling Stored Procedure: "
						+ SELECT_CONTRACT_PARTICIPANT_LIST
						+ " Get Total Count = " + totalCount);
			}

			// for the report data
			if (statement.getMoreResults()) {
				resultSet = statement.getResultSet();
				List cpList = new ArrayList(criteria.getPageSize());
				
				if (resultSet != null) {
				    
    				while (resultSet.next()) {
    					ContractParticipantVO v = new ContractParticipantVO();
    					v.setProfileId(resultSet.getBigDecimal(2));
    					v.setFirstName(resultSet.getString(4));
    					v.setLastName(resultSet.getString(6));
    					v.setSocialSecurityNumber(resultSet.getString(8));
    					v.setAddressLine1(resultSet.getString(9).trim());
    					v.setAddressLine2(resultSet.getString(10).trim());
    					v.setCity(resultSet.getString(11).trim());
    					v.setState(resultSet.getString(12).trim());
    					v.setZipCode(resultSet.getString(13).trim());
    					v.setCountry(resultSet.getString(14).trim());
    					v.setDefaultEnrolled(resultSet.getString(15));
    					v.setPinStatus(resultSet.getString(16));
    					v.setEnrollmentDate(resultSet.getDate(17));
    					cpList.add(v);
    				}
    				
				}
				
				reportData.setDetails(cpList);
			}
			reportData.setTotalCount(totalCount);
			return reportData;
		} catch (SQLException e) {
			throw new SystemException(
					e,
					className,
					"getReportData",
					"Problem occurred during "
							+ SELECT_CONTRACT_PARTICIPANT_LIST
							+ " stored proc call."
							+ "Input parameters are "
							+ "clientId:"
							+ criteria
									.getFilterValue(ContractParticipantReportData.FILTER_CONTRACT_ID));
		} finally {
			close(statement, conn);
		}
	}
}
