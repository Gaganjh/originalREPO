/*
 * Created on Jun 1, 2005
 * TODO Verify the parameters types!!!!!!!!!!
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.iloans.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.StoredProcedureHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.iloans.exception.IloansContractDoesNotAllowLoansException;
import com.manulife.pension.ps.service.iloans.exception.IloansContractNotActiveException;
import com.manulife.pension.ps.service.iloans.exception.IloansInvalidContractException;
import com.manulife.pension.ps.service.iloans.exception.IloansInvalidParticipantException;
import com.manulife.pension.ps.service.iloans.exception.IloansParticipantIsNotActiveException;
import com.manulife.pension.ps.service.iloans.exception.IloansServiceException;
import com.manulife.pension.ps.service.iloans.exception.IloansUnexpiredLoanRequestsException;
import com.manulife.pension.ps.service.report.iloans.dao.LoanRequestDAO;
import com.manulife.pension.service.account.valueobject.LoanRequestData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

/**
 * @author sternlu
 * 
 * This is DAO for initiate loan requests it validates participant SSN and
 * contract and inserts initial loan request record into CSDB
 */
public class InitiateLoanRequestsDAO extends BaseDatabaseDAO {

	private static final String className = LoanRequestDAO.class.getName();

	private static final Logger logger = Logger.getLogger(LoanRequestDAO.class);

	private static final String SELECT_UNEXPIRED_LOAN_REQUEST_IDS_FOR_PROFILE_AND_CONTRACT_SQL = 
		"{call "
		+ CUSTOMER_SCHEMA_NAME
		+ "SELECT_UNEXPIRED_NONDE_LOAN_REQUEST_IDS_FOR_PROFILE_AND_CONTRACT(?,?)}";

	private static final String VALIDATE_CONTRACT_LOAN_REQUEST = "{call "
			+ CUSTOMER_SCHEMA_NAME
			+ "VALIDATE_CONTRACT_LOAN_REQUEST(?,?,?,?,?)}";

	private static final String SELECT_BY_CONTRACT_ID_AND_SSN = "{call "
			+ CUSTOMER_SCHEMA_NAME + "SELECT_BY_CONTRACT_ID_AND_SSN(?,?)}";

	private static final String INSERT_LOAN_REQUEST_SQL = "{call "
			+ CUSTOMER_SCHEMA_NAME
			+ "INSERT_LOAN_REQUEST(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	private static final String SELECT_MAX_LOAN_REQUEST_ID_SQL = "{call "
			+ CUSTOMER_SCHEMA_NAME + "SELECT_MAX_LOAN_REQUEST_ID(?,?,?)}";

	private static final String SELECT_MAX_CONFIRMATION_NUMBER_SQL = "{call "
			+ CUSTOMER_SCHEMA_NAME + "SELECT_MAX_CONFIRMATION_NUMBER(?)}";

//	private static final Map fieldToColumnMap = new HashMap();

//	private static final String AND = " and ";

	private static final String ACTIVE_PARTICIPANT = "AC";

	// Make sure nobody instanciates this class
	private InitiateLoanRequestsDAO() {
	}

	public static void validateContractIlonRequest(int contractNumber,
			BigDecimal userProfileId, String siteLocation)
			throws SystemException, IloansServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateContractIloneRequest");

		}

		Connection conn = null;
		CallableStatement stmt = null;
//		ResultSet resultSet = null;
		//	LoanRequestReportData vo = null;
		try {
			// setup the connection and the statement
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(VALIDATE_CONTRACT_LOAN_REQUEST);
			if (logger.isInfoEnabled()) {
				logger.debug("Calling Stored Procedure: "
						+ VALIDATE_CONTRACT_LOAN_REQUEST);
			}
			stmt.setInt(1, contractNumber);
			stmt.setString(2, siteLocation); // 
			stmt.setBigDecimal(3, userProfileId); //

			stmt.registerOutParameter(4, Types.DECIMAL);// version
			stmt.registerOutParameter(5, Types.DECIMAL); //version

			stmt.execute();

			int reasonCode = stmt.getBigDecimal(5).intValue(); // reason code

			switch (reasonCode) {

			case 1:
				throw new IloansContractNotActiveException(
						"contract " +contractNumber + " not active.");

			case 2:
				throw new IloansContractDoesNotAllowLoansException(
						"contract not set for looans");

			case 3:
				throw new IloansInvalidContractException("wrong tpa for contract");

			case 4:
				throw new IloansInvalidContractException(
						"tpa has no staff plan access");
			case 5:
				throw new SystemException("Problem occurred during VALIDATE_CONTRACT_LOAN_REQUEST stored proc call. No site indicator was provided");
			case 6:
				throw new IloansInvalidContractException(
						"contract does not exist");
			default:
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(e, className, "validateInput",
					"Problem occurred during VALIDATE_CONTRACT_LOAN_REQUEST stored proc call.");
		} finally {
			close(stmt, conn);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- validateContractIloneRequest");
		}
	}

	/**
	 * public static String validateContractSSN(String contractNumber, String
	 * ssn, String siteLocation) take as input contract_id and SSN and will
	 * return to you the profile_id, contract_id and participant_status_code.
	 * The participant_status_code is a new column that I added. will check to
	 * see if 1- no records, which means the Social Security number entered is
	 * not found under the contract. throw 2- if there is a record by
	 * participant_status_code is not AC , throw error
	 */
	public static BigDecimal validateContractSSN(int contractNumber, String ssn)
			throws SystemException, IloansServiceException {
//		int reasonCode = 0;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> validateContractNumber");

		}

		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet resultSet = null;
		BigDecimal profileId = null;
		//	LoanRequestReportData vo = null;
		try {
			// setup the connection and the statement
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SELECT_BY_CONTRACT_ID_AND_SSN);
			if (logger.isInfoEnabled()) {
				logger.debug("Calling Stored Procedure: "
						+ SELECT_BY_CONTRACT_ID_AND_SSN);
			}
			stmt.setInt(1, contractNumber);
			stmt.setString(2, ssn); // 
			stmt.execute();
			resultSet = stmt.getResultSet();
			
			if (resultSet != null && resultSet.next()) {
				if (!ACTIVE_PARTICIPANT.equalsIgnoreCase(resultSet
						.getString("participant_status_code")))
					throw new IloansParticipantIsNotActiveException(
							"Participant Status is not AC");
				profileId = resultSet.getBigDecimal("profile_id");

			} else {
				throw new IloansInvalidParticipantException(
						"participant does not exist for this contract");
			}
		} catch (SQLException e) {
			throw new SystemException(e, className, "validateInput",
					"Problem occurred during SELECT_BY_CONTRACT_ID_AND_SSN stored proc call.");
		} finally {
			close(stmt, conn);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- validateContractSSN");
		}
		return profileId;
	}

	// throws exception if finds any records

	public static void validateForUnexpiredLoanRequests(int contractNumber,
			BigDecimal profileId) throws SystemException,
			IloansServiceException {
//		int reasonCode = 0;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> checkForUnexpiredLoanRequests");

		}
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet resultSet = null;
		try {
	
			// setup the connection and the statement
			
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareCall(SELECT_UNEXPIRED_LOAN_REQUEST_IDS_FOR_PROFILE_AND_CONTRACT_SQL);

			if (logger.isInfoEnabled()) {
				logger
						.debug("Calling Stored Procedure: "
								+ SELECT_UNEXPIRED_LOAN_REQUEST_IDS_FOR_PROFILE_AND_CONTRACT_SQL);
			}

			stmt.setBigDecimal(1, profileId);
			stmt.setInt(2, contractNumber);

			stmt.execute();
			resultSet = stmt.getResultSet();
			if (resultSet != null && resultSet.next()) {
				throw new IloansUnexpiredLoanRequestsException(
						"Unexpired loan requests for profile and contract");
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during SELECT_UNEXPIRED_LOAN_REQUEST_IDS_FOR_PROFILE_AND_CONTRACT_SQL stored proc call.");
		} finally {
			close(stmt, conn);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- checkForUnexpiredLoanRequests");
		}
	}

	public static int getMaxLoanRequestId(int contractId, BigDecimal profileId)
			throws SystemException {
		if (logger.isDebugEnabled()) {
			logger.debug("enter <- getMaxLoanRequestId");
		}
//		int requestId =0;

		try {
			StoredProcedureHandler handler = new StoredProcedureHandler(
					CUSTOMER_DATA_SOURCE_NAME,
					SELECT_MAX_LOAN_REQUEST_ID_SQL,
					new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.FieldOutputDefinition(
							3, Integer.class) }, new int[] { Types.DECIMAL,
							Types.INTEGER });

			Object[] objects = handler.execute(new Object[] { profileId,
					new Integer(contractId) });

			Integer result = (Integer) objects[0];
			return result.intValue();

		} catch (DAOException e) {
			throw new SystemException(e, className, "getMaxLoanRequestId",
					"Problem getting maximum loan request id");
		}

	}

	public static int getMaxConfirmationNumber() throws SystemException {

		try {
			StoredProcedureHandler handler = new StoredProcedureHandler(
					CUSTOMER_DATA_SOURCE_NAME,
					SELECT_MAX_CONFIRMATION_NUMBER_SQL,
					new StoredProcedureHandler.OutputDefinition[] { new StoredProcedureHandler.FieldOutputDefinition(
							1, Integer.class) }, new int[] {});

			Object[] objects = handler.execute(new Object[] {});

			Integer result = (Integer) objects[0];
			return result.intValue();

		} catch (DAOException e) {
			throw new SystemException(e, className, "getMaxConfirmationNumber",
					"Problem getting maximum confirmation number");
		}

	}

	// insert record
	public static void insertLoanRequest(LoanRequestData loanRequest)
			throws SystemException, DAOException {
		// setup the connection and the statement

		if (logger.isInfoEnabled()) {
			logger
					.debug("Calling Stored Procedure: "
							+ SELECT_UNEXPIRED_LOAN_REQUEST_IDS_FOR_PROFILE_AND_CONTRACT_SQL);
		}
		StoredProcedureHandler handler = new StoredProcedureHandler(
				CUSTOMER_DATA_SOURCE_NAME, INSERT_LOAN_REQUEST_SQL,
				new StoredProcedureHandler.OutputDefinition[] {});
		BigDecimal profileId = new BigDecimal(loanRequest.getProfileId());
		
		java.sql.Date sqlReqDate = null;
		java.sql.Date sqlReqExpiryDate = null;
		if ( loanRequest != null ) {
			if ( loanRequest.getReqDate() != null ) {
				sqlReqDate = new java.sql.Date(loanRequest.getReqDate().getTime());
			}
			if ( loanRequest.getReqExpiryDate() != null ) {
				sqlReqExpiryDate = new java.sql.Date(loanRequest.getReqExpiryDate().getTime());
			}
		}		
		
		handler.execute(new Object[] { 
				profileId, //BigDecimal 0
				new Integer(loanRequest.getContractNumber()),//1
				new Integer(loanRequest.getLoanRequestId()),//2
				loanRequest.getRequestStatusCode(),//char//3
				loanRequest.getReqInterestRatePct(), //BigDecimal//4
				loanRequest.getReqPaymentAmt(),//BigDecimal//5
				loanRequest.getReqLoanAmt(),//BigDecimal//6
				loanRequest.getReqPaymentAdjustmentAmt(),//BigDecimal//7
				new Integer(loanRequest.getReqAmortizationYears()),//8
				new Integer(loanRequest.getReqPaymentsPerYear()),//9
				loanRequest.getReqLoanReasonCode(), //char//10
				loanRequest.getReqVestingPct(), ////BigDecimal//11
				loanRequest.getReqMaxLoanAmt(), //12
				sqlReqDate,//date//13
				sqlReqExpiryDate, //date//14
				new Integer(loanRequest.getConfirmationNumber()),//15
				loanRequest.getReasonForLoan(),//16
				new Boolean(loanRequest.isTpaInitiated()),//17
				loanRequest.getLegallyMarried() });//18
	}
}