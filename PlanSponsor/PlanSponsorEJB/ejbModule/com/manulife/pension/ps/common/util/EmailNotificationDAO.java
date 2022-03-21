package com.manulife.pension.ps.common.util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.cache.PsProperties;
import com.manulife.pension.service.contract.util.NotificationEmailValueObject;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.sil.dao.ServiceInquiryLogDAO;
import com.manulife.pension.service.sil.valueobject.ServiceInquiryLog;

public class EmailNotificationDAO extends BaseDatabaseDAO {

	private static final String className = EmailNotificationDAO.class.getName();
	//private static final Logger logger = Logger.getLogger(EmailNotificationDAO.class);

	private static final String SQL_PAYROLL_PENDING_EMAIL_LIST = "SELECT A.FIRST_NAME,A.EMAIL_ADDRESS_TEXT,B.USER_PROFILE_ID, B.CONTRACT_ID,E.MANULIFE_COMPANY_ID FROM "
	        + CUSTOMER_SCHEMA_NAME + "USER_PROFILE A,"
	        + CUSTOMER_SCHEMA_NAME + "USER_CONTRACT B, "
	        + CUSTOMER_SCHEMA_NAME + "CONTRACT_CS E, "
	        + CUSTOMER_SCHEMA_NAME + "PERMISSION_GRANT C, "
	        + CUSTOMER_SCHEMA_NAME + "PERMISSION_HOLDER D WHERE B.CONTRACT_ID= ? AND "
	        + " B.USER_PROFILE_ID = A.USER_PROFILE_ID AND B.USER_PROFILE_ID = D.USER_CONTRACT_USER_PROFILE_ID AND E.CONTRACT_ID= B.CONTRACT_ID AND "
	        + " D.PERMISSION_HOLDER_ID = C.PERMISSION_HOLDER_ID AND B.CONTRACT_ID=D.USER_CONTRACT_CONTRACT_ID AND C.SECURITY_TASK_PERMISSION_CODE= ? ORDER BY B.CONTRACT_ID";

	private static final String SQL_CONTRACT_IDS_OF_NOTIICATION_SENT = "SELECT C.CONTRACT_ID,C.SUBMISSION_ID FROM "
	        + STP_SCHEMA_NAME + "SUBMISSION_CASE B, " + STP_SCHEMA_NAME + "SUBMISSION_CONTRIBUTION C, "
	        + JOURNAL_SCHEMA_NAME + "SUBMISSION_JOURNAL D " + " WHERE C.SUBMISSION_ID = B.SUBMISSION_ID "
	        + " AND B.CONTRACT_ID = C.CONTRACT_ID AND B.SUBMISSION_CASE_TYPE_CODE= ? AND B.PROCESS_STATUS_CODE= ? "
	        + " AND B.SUBMISSION_ID = D.SUBMISSION_ID "
	        + " AND D.APPLICATION_CODE = ? AND D.PAYROLL_COMPANY_SUBMISSION_IND = ? "
	        + " AND DATE(CURRENT DATE)- DATE(C.NOTIFICATION_SENT_TS) = ? ";

	private static final String GET_CONTACTS_DTL = "call " + CUSTOMER_SCHEMA_NAME + "GET_CONTACTS_DTL(?,?)";

	public static final String SIL_MSG_KEY = "psw.sil.message";

	private static final String SIL_EVENT_MSG = PsProperties.get(SIL_MSG_KEY);

	public static String getCARName(int contract_id) throws SystemException {
		String car_name = "";
		Connection conn = null;
		CallableStatement statement = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareCall(GET_CONTACTS_DTL);
			statement.setBigDecimal(1, new BigDecimal(contract_id));
			statement.registerOutParameter(2, Types.DECIMAL);
			statement.execute();
			ResultSet rs = statement.getResultSet();
			
			if (rs != null) {
			    
    			if (rs.next()) {
    				car_name = rs.getString("CLIENT_ACCOUNT_REP_NAME");
    			}
    			rs.close();
    			
			}
			
		} catch (SQLException ex) {
			throw new SystemException("Error getting CAR name for given contract number.");
		} finally {
			close(statement, conn);
		}
		return car_name;
	}

    // /**
    // * Getting list of NotificationEmailValueObject containing firstName,
    // * email,manulife company id
    // *
    // * @param notificationEmailObjectsList
    // *
    // * @throws SystemException
    // */
    //
    // public static ArrayList getEmailList(ArrayList notificationEmailObjectsList) throws
    // SystemException {
    // Connection connection = null;
    // PreparedStatement preparedStatment = null;
    // try {
    // connection = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
    // preparedStatment = connection.prepareStatement(SQL_PAYROLL_PENDING_EMAIL_LIST);
    // Iterator iterator = notificationEmailObjectsList.iterator();
    // while (iterator.hasNext()) {
    // NotificationEmailValueObject obj = (NotificationEmailValueObject) iterator.next();
    // preparedStatment.setInt(1, obj.getContract_id());
    // preparedStatment.setString(2, ProfileDAO.RECEIVE_PAYROLL_EMAIL);
    // ResultSet rs = preparedStatment.executeQuery();
    // ArrayList emailDetailsList = new ArrayList();
    // while (rs.next()) {
    // NotificationEmailDetailsValueObject emailDetails = new NotificationEmailDetailsValueObject();
    // emailDetails.setFirstName(rs.getString("FIRST_NAME"));
    // emailDetails.setEmail(rs.getString("EMAIL_ADDRESS_TEXT"));
    // emailDetails.setManulife_company_id(rs.getString("MANULIFE_COMPANY_ID"));
    // emailDetailsList.add(emailDetails);
    // }
    // obj.setEmailDetailsList(emailDetailsList);
    // rs.close();
    // }
    // } catch (SQLException e) {
    // throw new SystemException(e, className, "getEmailList",
    // "Problem occurred in prepared call in getEmailList: " + SQL_PAYROLL_PENDING_EMAIL_LIST);
    // } finally {
    // close(preparedStatment, connection);
    // }
    // return notificationEmailObjectsList;
    // }
    //
	/**
	 * Return a list of NotificationEmailValueObjects containing contract_id and submission_id
	 *  
	 * @param sumissionCase
	 * @param processStatusCode
	 * @param sourceSystemCode
	 * @param payrollCompanySubmissionInd
	 * @param isNotificationSent
	 * @param paymentPendingDays
	 * 
	 * @throws SystemException
	 */

	public static ArrayList getNotificationEmailObjectsList(String sumissionCase, String processStatusCode,
	        String sourceSystemCode, String payrollCompanySubmissionInd, int paymentPendingDays) throws SystemException {

		Connection connection = null;
		PreparedStatement preparedStatment = null;
		ArrayList notificationEmailList = new ArrayList();
		try {
			connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);
			preparedStatment = connection.prepareStatement(SQL_CONTRACT_IDS_OF_NOTIICATION_SENT);
			preparedStatment.setString(1, sumissionCase);
			preparedStatment.setString(2, processStatusCode);
			preparedStatment.setString(3, sourceSystemCode);
			preparedStatment.setString(4, payrollCompanySubmissionInd);
			preparedStatment.setInt(5, paymentPendingDays);
			ResultSet rs = preparedStatment.executeQuery();
			while (rs.next()) {
				NotificationEmailValueObject obj = new NotificationEmailValueObject();
				obj.setContract_id(rs.getInt("CONTRACT_ID"));
				obj.setSubmission_id(rs.getInt("SUBMISSION_ID"));
				notificationEmailList.add(obj);
			}
			rs.close();
		} catch (SQLException e) {
			throw new SystemException(e, className, "getContractIdForPendingPaymentContribution",
			        "Problem occurred in prepared call getNotificationEmailObjectsList: "
			                + SQL_CONTRACT_IDS_OF_NOTIICATION_SENT);
		} finally {
			close(preparedStatment, connection);
		}
		return notificationEmailList;
	}

	public static ServiceInquiryLog createSilWithContractId(String contract_id) throws DAOException {
		ServiceInquiryLog sil = new ServiceInquiryLog();
		sil.setSilNumber(new BigDecimal("0"));
		sil.setSilTypeNumber((short) 71);
		sil.setSilInquiryTypeCode("FU");
		sil.setContractId(new Integer(contract_id));
		sil.setCreatorTypeCode("AP");
		sil.setAssigneeTypeCode("AA");
		sil.setSilPriorityCode("1");
		sil.setSilTranTypeCode("AL");
		sil.setSilSourceCode("PT");
		sil.setSilStatusCode("00");
		sil.setTimeSentCount((short) 0);
		sil.setSilErrorTypeCode("");
		sil.setSilErrorText("Payment has not been received");
		sil.setSilErrorParam1("");
		sil.setSilErrorParam2("");
		sil.setSilErrorParam3("");
		sil.setSilErrorTs(null);
		List tokenList = getTokenList(SIL_EVENT_MSG, "*");
		if (tokenList != null && tokenList.size() > 0) {
			for (int x = 0; x < tokenList.size(); x++) {
				sil.addComment("APOLLO", (String) tokenList.get(x));
			}
		} else {
			sil.addComment("APOLLO", SIL_EVENT_MSG);
		}
		ServiceInquiryLogDAO.insertServiceInquiryLogWithComments(sil);
		return sil;
	}

	/**
	 * To tokenize the given string and gives the tokens as list.
	 *
	 * @param sil message String
	 * @param seperator String
	 * @return List
	 */
	private static List getTokenList(String silMsg, String seperator) {
		List tokenList = new ArrayList();
		if (silMsg != null) {
			StringTokenizer silTokens = new StringTokenizer(silMsg, seperator);
			while (silTokens.hasMoreTokens()) {
				tokenList.add(silTokens.nextToken());
			}
		}
		return tokenList;
	}
}
