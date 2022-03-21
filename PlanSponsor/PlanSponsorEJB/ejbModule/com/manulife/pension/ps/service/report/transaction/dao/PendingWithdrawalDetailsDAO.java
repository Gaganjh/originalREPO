package com.manulife.pension.ps.service.report.transaction.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.report.dao.DirectSqlReportDAOHelper;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.participant.transaction.valueobject.PendingWithdrawalDetailsReportData;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalMoneyTypeVO;
import com.manulife.pension.service.report.participant.transaction.valueobject.WithdrawalPayeePaymentVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.render.ZipCodeRender;
import com.manulife.util.tools.MessageFormatHelper;

/**
 * DAO class to retrieve the data for Pending Withdrawal Details screen.
 * 
 * @author Puttaiah Arugunta
 *
 */
public class PendingWithdrawalDetailsDAO extends ReportServiceBaseDAO {

	private static final String className = PendingWithdrawalDetailsDAO.class.getName();
	
	private static final Logger logger = Logger.getLogger(PendingWithdrawalDetailsDAO.class);
	
	public  final static String PAYMENT_METHOD_BY_PAID_BY_CHECK="Paid by check";
	
	/*
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] {
			APOLLO_SCHEMA_NAME };
	
	/* Withdrawal MoneyType VO Transformer */
	private static final WithdrawalMoneyTypeTransformer moneyTransformer = new WithdrawalMoneyTypeTransformer();
	
	/* Withdrawal Payee Payment VO Transformer */
	private static final WithdrawalPayeeTypeTransformer payeeTypeTransformer = new WithdrawalPayeeTypeTransformer();

	/**
	 * Method to populate Payee Payment List for the given Proposal Number and for the 
	 * Transaction Number. 
	 * 
	 * @param Proposal Number
	 * @param Transaction Number
	 * @return payeePaymentList
	 * 					List of WithdrawalPayeePaymentVO for the given Transaction
	 * @throws SystemException
	 *             If anything goes wrong with the connection.
	 */
	@SuppressWarnings("unchecked")
	public static List<WithdrawalPayeePaymentVO>  getPayeePaymentListForTransaction(String proposalNumber, String txnNumber)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getPayeePaymentListForTransaction");
		}
		
		Connection connection = null;
		PreparedStatement stmt = null;
        List<WithdrawalPayeePaymentVO> payeePaymentList = null;

		try {
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
			
			stmt = connection.prepareStatement(getTxnPayeeDetailsSql());
			stmt.setString(1, txnNumber);
			stmt.setString(2, proposalNumber);
			
			ResultSet rs = stmt.executeQuery();
      
			payeePaymentList = DirectSqlReportDAOHelper.getReportItems( rs, payeeTypeTransformer);
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getPayeePaymentListForTransaction",
					"Something went wrong while executing the statement - " +
					"proposalNumber ["+proposalNumber +"] and Transaction Number ["+txnNumber+"]");
		} finally {
			close(stmt, connection);
		}
		logger.debug("exit <- getPayeePaymentListForTransaction");
		return payeePaymentList;
	}
	/**
	 * Method to populate Money Type List at the Transaction level for the 
	 * given Proposal Number, Transaction Number and for the given Transaction Date. 
	 * 
	 * @param Proposal Number
	 * @param Transaction Number
	 * @param txnDate 
	 * 			Transaction Date to retrieve the corresponding Money Types information. Here
	 * 			(V1066.FINXTCDT) Txn extract date to be used in Vesting MT SQL in case the 
	 * 			(V1066.RTEFDT) actual Txn date is ‘9999-12-31’                    
	 * @return Money Type List
	 * 					List of WithdrawalMoneyTypeVO for the given Transaction
	 * @throws SystemException
	 *             If anything goes wrong with the connection.
	 */
	@SuppressWarnings("unchecked")
	public static List<WithdrawalMoneyTypeVO> getMoneyTypeListByTransactionNumber(ReportCriteria criteria,Date txnDate)
			throws SystemException, ReportServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getMoneyTypeListByTransactionNumber");
		}
		
		Connection connection = null;
		PreparedStatement stmt = null;
        List<WithdrawalMoneyTypeVO> moneyTypeList = null;
        
        // get the participant number,contract number,transaction number 
        //and proposal number from criteria object
        String participantId=(String) criteria.getFilterValue(
				PendingWithdrawalDetailsReportData.FILTER_PARTICIPANT_NUMBER);
		String contractId=(String) criteria.getFilterValue(
				PendingWithdrawalDetailsReportData.FILTER_CONTRACT_NUMBER);
		String txnNumber = (String) criteria.getFilterValue(
				PendingWithdrawalDetailsReportData.FILTER_TRANSACTION_NUMBER);
		String proposalNumber = (String) criteria.getFilterValue(
				PendingWithdrawalDetailsReportData.FILTER_PROPOSAL_NUMBER);

		try {
			connection = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			
			stmt = connection.prepareStatement(ER_SQL_FOR_TXN_NUMBER);
			
			stmt.setString(1, participantId);
			stmt.setString(2, contractId);
			
			ResultSet rs = stmt.executeQuery();
			rs.next();
			BigDecimal total_amt =getBigDecimalOrZero(rs,1);
			 //If total_amt  amount > 0 then execute the Money type SQL ,
			 //else return empty object
			if ( total_amt.compareTo(new BigDecimal(0)) > 0 ) {

				connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);

				stmt = connection.prepareStatement(getTxnMoneyTypeSql());

				stmt.setString(1, txnNumber);
				stmt.setString(2, proposalNumber);
				stmt.setDate(3, new java.sql.Date(txnDate.getTime()));
				stmt.setDate(4, new java.sql.Date(txnDate.getTime()));

				rs = stmt.executeQuery();

				moneyTypeList =  DirectSqlReportDAOHelper.getReportItems( rs, moneyTransformer);
				for( WithdrawalMoneyTypeVO WithdrawalMoneyTypeVO : moneyTypeList )  {
					// check at least one record should have vesting percentage value as > 0
					if( WithdrawalMoneyTypeVO.getVestingPercentage().compareTo( new BigDecimal(0)) > 0 ) {
						// if records have vesting percentage > 0, then read all the results and return a valid object.
						return moneyTypeList;
					}
				}
			}
			
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getMoneyTypeListByTransactionNumber",
					"Something went wrong while executing the statement - " +
					"participantId ["+participantId +"], Contract Number ["+contractId+"] "+
					"proposalNumber ["+proposalNumber +"], Transaction Number ["+txnNumber+"] and Txn Date ["+txnDate+"]");
		} finally {
			close(stmt, connection);
		}
		logger.debug("exit <- getMoneyTypeListByTransactionNumber");
		return moneyTypeList;
	}
	
	/*   SQL Name in MF document: SRWDMTD
	 *   SQL for Pending Withdrawal Vesting Money Type detail (SRWDMTD)
	 */
	private static final String MONEY_TYPE_SQL_FOR_TXN_NUMBER = "SELECT" +
			" 	    V1031.CNMMLONG" +
			"      ,V1076.VESTPCNT" +
			"	FROM {0}.VLP1076 V1076" +
			"	   	,{0}.VLP1031 V1031" +
			"	WHERE V1076.TRANNO      = ?" +
			"  		AND V1031.PROPNO    = ?" +
			"  		AND V1076.MLIMT     = V1031.MLIMT" +
			"  		AND V1031.STARTDTE <= ?" +
			"  		AND V1031.ENDDTE    > ?" +
			"	ORDER BY V1031.CNMMLONG						";
	
	/*  
	 *   SQL to get the ER balance for the participant
	 */
	private static final String ER_SQL_FOR_TXN_NUMBER ="SELECT "+ 
				"SUM(TOTAL_BALANCE_AMT) "+ 
				"FROM EZK100.PARTICIPANT_CURRENT_BALANCE PCB" +
				"	, EZK100.MONEY_TYPE MT "+
				"WHERE	PCB.MONEY_TYPE_ID = MT.MONEY_TYPE_CODE "+
				"	AND MT.MONEY_TYPE_CATEGORY_CODE = 'ER' "+
				"	AND PCB.PARTICIPANT_ID = ? "+
				"	AND PCB.CONTRACT_ID =? " +
				"	FOR FETCH ONLY ";

	
	/* SQL query to get the Money Types at Transaction Level*/
	private static String getTxnMoneyTypeSql() {

		return MessageFormatHelper.format(MONEY_TYPE_SQL_FOR_TXN_NUMBER, QUERY_SUBSTITUTIONS);
	}
	/*  SQL Name in MF document: SRWDPEY2
	 * 	SQL for PENDING SR WITHDRAWAL PAYEE DETAIL INFO with case logic (SRWDPEY2) for one WD txn.
	 */
	private static final String PAYEE_DETAILS_SQL_FOR_TXN_NUMBER =  " SELECT															" +
	 "       PAYEE.PYEETYP  												" +// PAYMENT TO
	 "       ,(CASE          											" +                                            
	 "          WHEN (V1304.EFTTYPE IS NULL AND (PAYEE.PYMTMTD = 'HA'    " +
	 "               OR PAYEE.PYMTMTD = 'HM'))                           " +
	 "            THEN 'Paid by check'                                   " +
	 "          WHEN (V1304.EFTTYPE IS NULL AND PAYEE.PYMTMTD = 'WT')    " +
	 "            THEN 'Wire transfer'                                   " +
	 "          WHEN (V1304.EFTTYPE = 'AC'  AND PAYEE.PYMTMTD = 'WT')    " +
	 "            THEN 'Direct deposit'                                  " +
	 "          WHEN (V1304.EFTTYPE = 'WT'  AND PAYEE.PYMTMTD = 'WT')    " +
	 "			 THEN 'Wire transfer'                          			" +
	 "          WHEN V1304.EFTTYPE IS NULL                               " +
	 "          	 THEN ' '                                               " +
	 "         END) AS PAYMTMTD                        					" +// PAYMENT METHOD    
	 "       ,COALESCE(V1304.ACHACTTY,' ') AS ACCTTYP   					" +// BANK ACCOUNT
	 "       ,(CASE														" +
	 "           WHEN (PAYEE.PYEETYP = 'PA' OR							" +
	 "                 PAYEE.PYEETYP = 'BE' OR							" +
	 "                 PAYEE.PYEETYP = 'OT')								" +
	 "             THEN PAYEE.PYEEFSTN									" +
	 "           WHEN (PAYEE.PYEETYP = 'CL' OR							" +
	 "                 PAYEE.PYEETYP = 'TR')  							" +
	 "             THEN PAYEE.CNLNGM1         							" +
	 "           WHEN (PAYEE.PYEETYP = 'DB' OR 							" +
	 "                 PAYEE.PYEETYP = 'FI')  							" +
	 "            THEN PAYEE.TRCLFINM        							" +
	 "           ELSE ' '   												" +
	 "          END) AS FIRST_NAME            							" +// PAYEE ADDRESS LINE 1
	 "       ,(CASE														" +
	 "           WHEN (PAYEE.PYEETYP = 'PA' OR 							" +
	 "                 PAYEE.PYEETYP = 'BE' OR 							" +
	 "                 PAYEE.PYEETYP = 'OT') 							" +
	 "             THEN PAYEE.PYEELSTN 									" +
	 "           ELSE ' ' 												" +
	 "          END) AS LAST_NAME        								" +// PAYEE ADDRESS LINE 2
	 "       ,PAYEE.PYEEADR1               								" +// PAYEE ADDRESS LINE 3
	 "       ,PAYEE.PYEEADR2                 							" +// PAYEE ADDRESS LINE 4
	 "       ,PAYEE.PYEECITY                  							" +// PAYEE ADDRESS LINE 5
	 "       ,PAYEE.PYEESTCD                     						" +// PAYEE ADDRESS LINE 5
	 "       ,PAYEE.PYEEZPCD                       						" +// PAYEE ADDRESS LINE 5
	 "       ,(CASE                       								" +
	 "           WHEN PAYEE.FRADRIND = 'N' AND							" +
	 "				(PAYEE.PYEEADR1 != '' OR							" +
	 "				 PAYEE.PYEEADR2 != '' OR							" +
	 "	 			 PAYEE.PYEECITY != '' OR							" +
	 " 				 PAYEE.PYEESTCD != '' OR							" +
	 "	 			 PAYEE.PYEEZPCD != '') 								" +
	 "           THEN 'USA'             								" +
	 "           ELSE ' '                 								" +
	 "          END) AS CNTRYCD            								" +// PAYEE ADDRESS LINE 6
	 "       ,COALESCE(V1304.BANKNAM,' ') AS BANK_NAME 					" +// BANK/BRANCH NAME
	 "       ,COALESCE(V1304.BANKABA,0) AS BRANCH_NUM  					" +// ABA/ROUTING NUMBER
	 "       ,COALESCE(V1304.ACCTNO,' ')  AS ACCT_NUM   				" +// ACCOUNT NUMBER
	 "       ,COALESCE(V1304.CRDTPRTY,' ') AS ACCT_NAME 				" +// CREDIT PARTY NAME
	 "   FROM   														" +
	 "       (SELECT  													" +
	 "              V1079.CLNTID  										" +
	 "             ,V1079.PROPNO   										" +
	 "             ,V1079.PYEETYP										" +
	 "             ,V1080.PYMTMTD										" +
	 "             ,V1080.TRANNO										" +
	 "             ,V1080.PYEEID										" +
	 "             ,V1079.PYEEFSTN										" +
	 "             ,V1079.PYEELSTN										" +
	 "             ,V1079.TRCLFINM										" +
	 "             ,V1036.CNLNGM1										" +
	 "             ,V1079.PYEEADR1										" +
	 "             ,V1079.PYEEADR2										" +
	 "             ,V1079.PYEECITY										" +
	 "             ,V1079.PYEESTCD										" +
	 "             ,V1079.PYEEZPCD										" +
	 "             ,V1079.FRADRIND										" +
	 "             FROM {0}.VLP1066 V1066 							" +
	 "                 ,{0}.VLP1079 V1079							" +
	 "                 ,{0}.VLP1080 V1080							" +
	 "                 ,{0}.VLP1036 V1036							" +
	 "             WHERE V1066.TRANNO = ?								" +
	 "               AND V1066.PROPNO = ?								" +
	 "               AND V1066.TRSTATCD IN ('WB', 'SI', 'SM', 'FL') 		" +
	 "               AND V1066.TRANNO = V1080.TRANNO						" +
	 "               AND V1080.PYEEID = V1079.PYEEID						" +
	 "               AND V1079.PROPNO = V1036.PROPNO) AS PAYEE	 		" +
	 " LEFT OUTER JOIN {0}.VLP1304 V1304 							" +
	 "      ON  V1304.TRANNO = PAYEE.TRANNO								" +
	 "      AND V1304.PYEEID = PAYEE.PYEEID 								" +
	 " ORDER BY PAYEE.PYEEID 											" ;

	/* SQL to get the Payee Payment Details for at Transaction Level */
	private static String getTxnPayeeDetailsSql() {

		return MessageFormatHelper.format(PAYEE_DETAILS_SQL_FOR_TXN_NUMBER, QUERY_SUBSTITUTIONS);
	}

	/**
	 * Inner class to transform the Result Set to WithdrawalMoneyTypeVO Objects.
	 * @author Puttaiah Arugunta
	 *
	 */
	private static class WithdrawalMoneyTypeTransformer extends
									ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			
			WithdrawalMoneyTypeVO moneyTypeVO = new WithdrawalMoneyTypeVO();
			
			moneyTypeVO.setMoneyType(getString(rs, "CNMMLONG"));
			moneyTypeVO.setVestingPercentage(rs.getBigDecimal("VESTPCNT"));

			return moneyTypeVO;
		}
	}
	
	/**
	 * Inner class to transform the Result Set to WithdrawalPayeePaymentVO Objects.
	 * @author Puttaiah Arugunta
	 *
	 */
	private static class WithdrawalPayeeTypeTransformer extends
									ReportItemTransformer {
		
		public Object transform(ResultSet rs) throws SQLException {
			
			WithdrawalPayeePaymentVO payeePaymentVO = new WithdrawalPayeePaymentVO();
			payeePaymentVO.setPaymentTo(getString(rs, "PYEETYP"));
			
			String paymentMethod = getString(rs, "PAYMTMTD");
			payeePaymentVO.setPaymentMethod(paymentMethod);
			
			StringBuffer fullName = new StringBuffer();			
			String firstName = getString(rs, "FIRST_NAME");
			String lastName = getString(rs, "LAST_NAME");
			if (StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
				fullName.append(firstName).append(" ").append(lastName);
			} else if (StringUtils.isBlank(lastName) && StringUtils.isNotBlank(firstName)) {
				fullName.append(firstName);
			} else if (StringUtils.isBlank(firstName) && StringUtils.isNotBlank(lastName)) {
				fullName.append(lastName);
			}
			payeePaymentVO.setPayeeName(fullName.toString());
			
			payeePaymentVO.setAddressLine1(getString(rs, "PYEEADR1"));
			payeePaymentVO.setAddressLine2(getString(rs, "PYEEADR2"));
			payeePaymentVO.setCity(getString(rs, "PYEECITY"));
			payeePaymentVO.setState(getString(rs, "PYEESTCD"));
			
			String zipCode = getString(rs, "PYEEZPCD");
			if(zipCode != null) {
				payeePaymentVO.setZip(ZipCodeRender.format(zipCode,StringUtils.EMPTY));
			}
			
			payeePaymentVO.setCountry(getString(rs, "CNTRYCD"));
			//Check for Payment method not equal to paid by check
			if(!PAYMENT_METHOD_BY_PAID_BY_CHECK.equalsIgnoreCase(paymentMethod)){
				payeePaymentVO.setAccountType(getString(rs, "ACCTTYP"));
				payeePaymentVO.setBankBranchName(getString(rs, "BANK_NAME"));
				
				String routingNumber = getString(rs, "BRANCH_NUM");
				if ( Integer.valueOf(routingNumber) == 0){
					routingNumber = StringUtils.EMPTY;
				} else if(routingNumber.length() < 9){
					routingNumber = String.format("%09d", 
												Integer.valueOf(routingNumber.trim()));
				}
				payeePaymentVO.setRoutingABAnumber(routingNumber);
				
				payeePaymentVO.setAccountNumber(getString(rs, "ACCT_NUM"));
				payeePaymentVO.setCreditPayeeName(getString(rs, "ACCT_NAME"));
			}

			return payeePaymentVO;
		}
	}
}
