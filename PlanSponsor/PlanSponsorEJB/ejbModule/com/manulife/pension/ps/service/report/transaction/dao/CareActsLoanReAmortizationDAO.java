package com.manulife.pension.ps.service.report.transaction.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CareActsLoanReAmortizationDAO extends BaseDatabaseDAO {
	
	private static final String className = CareActsLoanReAmortizationDAO.class.getName();
	private static final Logger logger = Logger.getLogger(CareActsLoanReAmortizationDAO.class);
	
	private static final String INSERT_LOAN_SUMMARY_DEATAILS_SQL = 
			" INSERT INTO "+ PLAN_SPONSOR_SCHEMA_NAME + " PARTICIPANT_LOAN_REAMORTIZATION_REQUEST (CONTRACT_ID,PARTICIPANT_ID,"
			+ "LOAN_ID,REQUESTED_BY,REQUESTED_BY_NAME,REQUESTED_DATE,ORIGINAL_LOAN_MATURITY_DATE) VALUES(?,?,?,?,?, CURRENT TIMESTAMP,?)";

	private static final String SELECT_BY_PARTICIPANT_ID_FOR_LOAN_SUMMARY =
			" SELECT PARTICIPANT_ID,LOAN_ID FROM "+ PLAN_SPONSOR_SCHEMA_NAME + " PARTICIPANT_LOAN_REAMORTIZATION_REQUEST WHERE  CONTRACT_ID = ? ";
	
	private static final String DELETE_PARTICIPANT_ID_FOR_LOAN_SUMMARY =
			" DELETE FROM "+ PLAN_SPONSOR_SCHEMA_NAME + " PARTICIPANT_LOAN_REAMORTIZATION_REQUEST WHERE  CONTRACT_ID = ? AND PARTICIPANT_ID = ? AND LOAN_ID = ? ";
 
	 /** 
	  * Make sure nobody instantiates this class
	  */
	 private CareActsLoanReAmortizationDAO()
	 {
	 }	
	 
	public static void updateLoanDetails(String[] loanDetails, ArrayList<String> recordsFromUI, int contractNumber,
			String tpaid, String tpaProfileName) throws SystemException {
		String loanNumber = "";
		String maturityDate = "";
		String participantID = "";
		ArrayList<String> selectedLoansList = new ArrayList<String>();
		List<String> listFromDB = getExistingParticipantIDS(contractNumber);
		
		for (String aLoanDetail : loanDetails) {
			String[] loanArray = aLoanDetail.split(":");
			loanNumber = loanArray[0];
			maturityDate = loanArray[1];
			participantID = loanArray[2];
			selectedLoansList.add(participantID+":"+loanNumber);
			if (listFromDB.size() > 0) {

				if (!listFromDB.contains(participantID+":"+loanNumber)) {
					insertLoanDetails(participantID,contractNumber,tpaid, loanNumber, maturityDate, tpaProfileName);
				}

			} else {
				insertLoanDetails(participantID,contractNumber,tpaid, loanNumber, maturityDate, tpaProfileName);
			}
		}

		if (recordsFromUI.size() > 0) {
			recordsFromUI.removeAll(selectedLoansList);
			for (String deleteRecords : recordsFromUI) {
				String[] deleteRecordsArray = deleteRecords.split(":");
				String deleteparticipantID = deleteRecordsArray[0];
				String deleteloanNumber = deleteRecordsArray[1];
				deleteLoanDetails(deleteparticipantID, contractNumber, deleteloanNumber);
			}
		}
	}
		
	private static void deleteLoanDetails(String participantID, int contractNumber, String loanNumber)
			throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> deleteLoanDetails");

		Connection conn = null;
		CallableStatement statement = null;

		try {

			// setup the connection and the statemnt
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareCall(DELETE_PARTICIPANT_ID_FOR_LOAN_SUMMARY);

			// set the input parameters
			statement.setInt(1, contractNumber);
			statement.setBigDecimal(2, new BigDecimal(participantID));
			statement.setBigDecimal(3, new BigDecimal(loanNumber));

			statement.execute();

		} catch (SQLException e) {
			throw new SystemException(e,
					"Problem occurred during DELETE_PARTICIPANT_ID_FOR_LOAN_SUMMARY query call. Input parameters are contractNumber:"
							+ contractNumber);
		} finally {
			close(statement, conn);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- deleteLoanDetails");

	}

	public static List<String> getExistingParticipantIDS(int contractNumber) throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> getExistingParticipantIDS");

		Connection conn = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;
		List<String> ParticipantIDS = new ArrayList<String>();

		try {

			// setup the connection and the statemnt
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareCall(SELECT_BY_PARTICIPANT_ID_FOR_LOAN_SUMMARY);

			// set the input parameters
			statement.setInt(1, contractNumber);

			statement.execute();

			// *************** Result set #1 ****************************
			resultSet = statement.getResultSet();

			if (resultSet != null) {

				while (resultSet.next()) {
					ParticipantIDS.add(resultSet.getString("PARTICIPANT_ID")+":"+resultSet.getString("LOAN_ID"));

				}

			}

		} catch (SQLException e) {
			throw new SystemException(e,
					"Problem occurred during getExistingParticipantIDS query call. Input parameters are contractNumber:"
							+ contractNumber);
		} finally {
			close(statement, conn);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- getExistingParticipantIDS");

		return ParticipantIDS;

	}

	public static void insertLoanDetails(String participantID,int contractNumber, String tpaID, String loanNumber,
			String maturityDate, String tpaProfileName) throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> insertLoanDetails");

		Connection conn = null;
		CallableStatement statement = null;

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(maturityDate);
			java.sql.Date maturityDateValue = new java.sql.Date(parsed.getTime());

			// setup the connection and the statemnt
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareCall(INSERT_LOAN_SUMMARY_DEATAILS_SQL);

			// set the input parameters
			statement.setInt(1, contractNumber);
			statement.setBigDecimal(2, new BigDecimal(participantID));
			statement.setInt(3, Integer.parseInt(loanNumber));
			statement.setBigDecimal(4, new BigDecimal(tpaID));
			statement.setString(5, tpaProfileName);
			statement.setDate(6, maturityDateValue);

			statement.execute();

		} catch (Exception e) {
			throw new SystemException(e,
					"Problem occurred during insertLoanDetails query call. Input parameters are contractNumber:"
							+ contractNumber);
		} finally {
			close(statement, conn);
		}

		if (logger.isDebugEnabled())
			logger.debug("exit <- insertLoanDetails");

	}
}
