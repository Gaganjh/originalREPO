package com.manulife.pension.ps.service.report.transaction.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.exception.ContractNumberDoesNotmatchException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsRebalReportData;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.participant.transaction.handler.TransactionType;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;
 
public class TransactionDetailsRebalDAO extends ReportServiceBaseDAO {

	private static final String className = TransactionDetailsRebalDAO.class.getName();

	private static final boolean DEBUG_SQL = true;

	/**
	 * A map of database column keyed by sort field.
	 */
	private static final Map fieldToColumnMap = new HashMap();

	private static final DetailItemTransformer transformer = new DetailItemTransformer();

	private static final Logger logger = 
					Logger.getLogger(TransactionDetailsRebalDAO.class);
	


	/**
	 * The substitutions used in all query strings.
	 */
	private static final Object[] QUERY_SUBSTITUTIONS = new Object[] { APOLLO_SCHEMA_NAME };

	private static final String TRANSACTION_SUMMARY_SQL = getTransactionSummarySql();
	private static final String TRANSFER_FROM_TO_SQL = getTransferFromToSql();
	private static final String TRANSACTION_DETAILS_SQL = getTransactionDetailsSql();
	
	private static interface Columns {
		
		String SORT_NO = "WEBSRTNO";

		String PARTICIPANT_FIRSTNAME = "PRTFSTNM";
		String PARTICIPANT_LASTNAME = "PRTLSTNM";
		String PARTICIPANT_SSN = "PRTSSN";
		
 		String RATE_EFFECTIVE_DATE = "RTEFDT";
 		String REQUEST_DATE = "TREFDT";
		String MEDIUM = "MEDIUM";
		String AMOUNT = "TXNAMT";
		String CONTRACT_NUMBER = "CNNO";
		String FROM_TO = "FROMTO";
		String EE_PERCENTAGE = "EE_PCT";
		String EE_AMOUNT = "EE_BAL";
		String ER_PERCENTAGE = "ER_PCT";
		String ER_AMOUNT = "ER_BAL";
		
		String MONEY_TYPE_LONG_NAME = "CNMMLONG";
		String INVESTMENT_ID = "IVACFAID";
	
		String UNIT_VALUE = "UNIINTRT";
		String NO_OF_UNITS = "NOUNIT";
		String RISK_GROUP_NO = "GRPORDNO";
		String TRANSACTION_SUB_TYPE = "TRSUBTYP";
		String REASONCODE = "REASONCODE";
		
	}

	static {
		/*
		 * Sets up the field to column map.
		 */
		fieldToColumnMap.put(TransactionDetailsRebalReportData.SORT_FIELD_RISK_CATEGORY,
				Columns.RISK_GROUP_NO);
		fieldToColumnMap.put(TransactionDetailsRebalReportData.SORT_FIELD_WEBSRTNO,
				Columns.SORT_NO);
		fieldToColumnMap.put(TransactionDetailsRebalReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION,
				Columns.MONEY_TYPE_LONG_NAME);


	}

	public static TransactionDetailsRebalReportData getReportData(
			ReportCriteria criteria) throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		String transactionNumber = getTransactionNumber(criteria);
		Integer contractNumber = getContractNumber(criteria);
		Integer participantId = getParticipantId(criteria);	

		TransactionDetailsRebalReportData reportData = null;

		try {	
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
			
			reportData = new TransactionDetailsRebalReportData(criteria, 0);
 			reportData.setTransactionNumber(transactionNumber);
 			
 			populateTransactionSummary(connection, criteria, reportData);
 			//The following change in the code is implemented for handling the book marking.
			//Check if the user contract number and database contract numbers are present.
			if(contractNumber==null||reportData.getContractNumber()==null)
 			{
 				logger.debug("Show 1047 error. There is no Contract.");
 				throw new ContractNumberDoesNotmatchException("Show 1047 error. There is no Contract.");
 			}	
			//Check if the user contract number matches with the database contract number.
 			else if((!contractNumber.toString().equals(reportData.getContractNumber()))||(contractNumber.intValue()==0)){
 				
 				logger.debug("Show 1047 error. There is no such record.");
 				throw new ContractNumberDoesNotmatchException("Show 1047 error. There is no Contract.");
 			}
 			else{
 			
 			populateTransferFromTos(connection, criteria, reportData);
  			populateTransactionDetails(connection, criteria, reportData);
 			
  			if (logger.isDebugEnabled()) {
				logger.debug("exit -> getReportData");
			}
 			}
		} catch (SQLException e) {
			handleSqlException(e, className,
					"getReportData",
					"Something went wrong while executing the statement - "
							+ getContractNumber(criteria) + "]");
		} finally {
			close(stmt, connection);
		}
		logger.debug("exit <- getReportData");	
		return reportData;
	}

	/**
	 * Gets the contract number filter from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The contract number.
	 */
	private static Integer getContractNumber(ReportCriteria criteria) {
		
		try {
			return Integer.valueOf( 
				(String) criteria.getFilterValue(TransactionDetailsRebalReportData.FILTER_CONTRACT_NUMBER));
		} catch (NumberFormatException e) {
			logger.error("Received NumberFormatException. Can't format contract number to Integer. ", e);
			throw new EJBException(e);								
		}
	}

	/**
	 * Gets the participant number filter from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The participantId.
	 */
	private static Integer getParticipantId(ReportCriteria criteria) {
		return new Integer((String)criteria.getFilterValue(TransactionDetailsRebalReportData.FILTER_PARTICIPANT_ID));
	}
	
	/**
	 * Gets the transaction number filter from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The transaction number.
	 */
	private static String getTransactionNumber(ReportCriteria criteria) {
		return (String) criteria.getFilterValue(TransactionDetailsRebalReportData.FILTER_TRANSACTION_NUMBER);
	}

	/**
	 * Populates transaction summary info which includes transaction date,
	 * request date, total amount, medium (requested through) and 
	 * source of transfer.
	 * 
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to be populated.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
	private static void populateTransactionSummary(Connection connection,
			ReportCriteria criteria,
			TransactionDetailsRebalReportData reportData) throws SQLException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateTransactionSummary");
			if (DEBUG_SQL)  
				logger.debug(TRANSACTION_SUMMARY_SQL );
		}

		PreparedStatement stmt = null;

		String transactionNumber = getTransactionNumber(criteria);
 		try {
			stmt = connection.prepareStatement(TRANSACTION_SUMMARY_SQL );
			stmt.setString(1, transactionNumber);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				reportData.setContractNumber(rs.getString(Columns.CONTRACT_NUMBER));
				reportData.setTransactionDate(rs.getDate(Columns.RATE_EFFECTIVE_DATE));
				reportData.setRequestDate(rs.getDate(Columns.REQUEST_DATE));
				
				if (StringUtils.equals(TransactionType.MANAGED_ACCOUNT, getString(rs, Columns.REASONCODE))) {
					reportData.setMediaCode("Managed Accounts");
				}
				else {
					reportData.setMediaCode(TransactionHistoryHelper.getMediumDescription(getString(rs, Columns.MEDIUM),null, null, null));
				}
				String name = rs.getString(Columns.PARTICIPANT_LASTNAME).trim() + ", " + rs.getString(Columns.PARTICIPANT_FIRSTNAME).trim(); 
				reportData.setParticipantName(name);
				reportData.setParticipantSSN(rs.getString(Columns.PARTICIPANT_SSN));
				reportData.setParticipantUnmaskedSSN(rs.getString(Columns.PARTICIPANT_SSN));	
			}
		} finally {
			close(stmt, null);
		} 
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateTransactionSummary");
		}		
	}


	/**
	 * Populates transfer from and transfer to, which includes the money type 
	 * description and the associated amount.
	 * 
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to be populated.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
	private static void populateTransferFromTos(Connection connection,
			ReportCriteria criteria,
			TransactionDetailsRebalReportData reportData) throws SQLException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateTransferFromTos");
			if (DEBUG_SQL) {
				logger.debug(TRANSFER_FROM_TO_SQL);
			}
		}

		PreparedStatement stmt = null;
 
 		try {
			stmt = connection.prepareStatement(TRANSFER_FROM_TO_SQL);
			String transactionNumber = getTransactionNumber(criteria);
			int parameterCount = 1;
			for (int i = 0; i < 13; i++) {
				stmt.setString(parameterCount++, transactionNumber);
			}
			ResultSet rs = stmt.executeQuery();

 			while (rs.next()) {
				TransactionDetailsItem item = new TransactionDetailsItem();
				
				String fromToString = getString(rs, Columns.FROM_TO);
				
				boolean isFrom = 
  					( fromToString.equals("FROM") ? true : false);
 				
 				item.setFundId(getString(rs, Columns.INVESTMENT_ID));
 					
				item.setEmployeeAmount(
					getBigDecimalOrZero(rs, Columns.EE_AMOUNT));
					
				item.setEmployeePercentage( 
					getBigDecimalOrZero(rs, Columns.EE_PERCENTAGE));
					
				item.setEmployerAmount(
					getBigDecimalOrZero(rs, Columns.ER_AMOUNT));
					
				item.setEmployerPercentage( 
					getBigDecimalOrZero(rs, Columns.ER_PERCENTAGE));
					
				item.setRiskCategoryCode(getString(rs,Columns.RISK_GROUP_NO));
				
				if (isFrom) {
					reportData.getBeforeChange().add(item);
				} else {
					reportData.getAfterChange().add(item);
				}
			}  
			
		} finally {
			close(stmt, null);
		}
  
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateTransferFromTos");
		}	
	}


	/**
	 * Populates the detail transfer items.
	 * 
	 * @param connection
	 *            The database connection to use.
	 * @param criteria
	 *            The report criteria to use.
	 * @param reportData
	 *            The report data to be populated.
	 * @throws SQLException
	 *             If there's an error when we execute the statement.
	 */
 	private static void populateTransactionDetails(Connection connection,
			ReportCriteria criteria,
			TransactionDetailsRebalReportData reportData) throws SQLException,
			EJBException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> populateTransactionDetails");
		}
				
		String query = TRANSACTION_DETAILS_SQL;
 
		/*
		 * Appends the ORDER BY clause when there is any sorting requirement.
		 */
		if (criteria.getSorts().size() > 0) {
			query = new StringBuffer(query).append(" ORDER BY ").append(
					criteria.getSortPhrase(fieldToColumnMap)).toString();
		}

		if (logger.isDebugEnabled() && DEBUG_SQL) {
			logger.debug(query);
		}

		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(query);

			stmt.setString(1, getTransactionNumber(criteria));
			stmt.setString(2, getTransactionNumber(criteria));
			stmt.setString(3, getTransactionNumber(criteria));
			ResultSet rs = stmt.executeQuery();

			List items = new ArrayList();
			
			while (rs.next()) {
				items.add(transformer.transform(rs));
			}
			
			reportData.setDetails(items);
			
		} finally {
			close(stmt, null);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- populateTransactionDetails");
		}			
	}
 	 

	private static String getTransactionSummarySql() {
		
		SqlStringBuffer sql = new SqlStringBuffer();
		
		sql.append("SELECT   A.CNNO 					")
			.append("		,B.PRTLSTNM 					")
           .append("		,B.PRTFSTNM						")
		   .append("		,B.PRTSSN						")
		   .append("		,A.RTEFDT						")
		   .append("		,A.TREFDT						")
		   .append("		,A.TRANNO						")
		   .append("		,A.ALLOCMED			AS MEDIUM	")
		   .append("		,A.TRRSNCD AS REASONCODE 		")
		   .append("FROM	 {0}.VLP1015 A					")
		   .append("		,{0}.VLP1074 B					")
		   .append("WHERE	A.TRANNO = ?					")
		   .append("AND  	A.PRTID  = B.PRTID				");

		String query = MessageFormatHelper.format(sql.toString(), QUERY_SUBSTITUTIONS);

		return query;
	}

	private static String getTransferFromToSql() {
		
		SqlStringBuffer sql1 = new SqlStringBuffer()
		.append("    SELECT 'FROM'  AS FROMTO												")
		.append("          ,A.IVACFAID 														")
		.append("          ,A.TREEBAL AS EE_BAL												")
		.append("          ,CASE 															")
		.append("             WHEN SUM.EE_SUM > 0 											")
		.append("             THEN  DEC(ROUND(FLOAT(A.TREEBAL)/SUM.EE_SUM * 100,2),5,2) 	")
		.append("             ELSE 0 														")
		.append("           END AS EE_PCT 													")
		.append("          ,A.TRERBAL		AS ER_BAL											")
		.append("          ,CASE 															")
		.append("             WHEN SUM.ER_SUM > 0 											")
		.append("             THEN  DEC(ROUND(FLOAT(A.TRERBAL)/SUM.ER_SUM * 100,2),5,2) 	")
		.append("             ELSE 0 														")
		.append("           END AS ER_PCT 													")
		.append("          ,C.GRPORDNO AS GRPORDNO 											")
		.append("          ,C.WEBSRTNO AS WEBSRTNO 											")
		.append("    FROM {0}.VLP1090 A 													")
		.append("        ,{0}.VLP1055 C 													")
		.append("        ,(SELECT TRANNO 													")
		.append("                ,SUM(TREEBAL) AS EE_SUM 									")
		.append("                ,SUM(TRERBAL) AS ER_SUM 									")
		.append("          FROM {0}.VLP1090 												")
		.append("          WHERE TRANNO = ? 												")
		.append("          GROUP BY TRANNO 													")
		.append("          ) AS SUM 														")
		.append("    WHERE A.TRANNO=SUM.TRANNO 												")
		.append("      AND A.IVACFAID = C.IVACFAID 											")
		.append("      AND ( TREEBAL > 0  OR  TRERBAL > 0 ) 								")
		
		.append("  UNION ALL 																")
		.append("  SELECT 'TO' AS FROMTO 													")
		.append("        ,A.IVACFAID 														")
		.append("        ,A.TREEBAL + EE.FIAMT  AS EE_BAL									")
		.append("        ,DEC(A.TREEPCNT,5,2)  AS EE_PCT									")
		.append("        ,A.TRERBAL + ER.FIAMT  AS ER_BAL									")
		.append("        ,DEC(A.TRERPCNT,5,2)  AS ER_PCT									")
		.append("        ,C.GRPORDNO  AS GRPORDNO											")
		.append("        ,C.WEBSRTNO  AS WEBSRTNO											")
		.append("  FROM {0}.VLP1090 A 														")
		.append("      ,{0}.VLP1055 C 														")
		.append("      ,(SELECT B.IVACFAID, SUM(B.FIAMT) AS FIAMT 							")
		.append("        FROM {0}.VLP1070 B 												")
		.append("        WHERE B.TRANNO = ? 												")
		.append("        AND SUBSTR(B.MLIMT,1,2) = 'EE' 									")
		.append("        GROUP BY B.IVACFAID) AS EE 										")
		.append("      ,(SELECT B.IVACFAID, SUM(B.FIAMT) AS FIAMT 							")
		.append("        FROM {0}.VLP1070 B 												")
		.append("        WHERE B.TRANNO = ? 												")
		.append("        AND SUBSTR(B.MLIMT,1,2) = 'ER' 									")
		.append("        GROUP BY B.IVACFAID) AS ER 										")
		.append("  WHERE A.IVACFAID = EE.IVACFAID 											")
		.append("    AND A.IVACFAID = ER.IVACFAID 											")
		.append("    AND A.IVACFAID = C.IVACFAID 											")
		.append("    AND A.TRANNO = ? 														")		
		.append("    AND (A.TREEPCNT > 0 OR A.TRERPCNT > 0) 								");
		
		SqlStringBuffer sql2 = new SqlStringBuffer()				
		.append("UNION ALL 																	")
		.append("  SELECT 'TO' AS FROMTO 													")
		.append("        ,A.IVACFAID 														")
		.append("        ,A.TREEBAL + EE.FIAMT AS EE_BAL 									")
		.append("        ,DEC(A.TREEPCNT,5,2) AS EE_PCT 									")
		.append("        ,A.TRERBAL AS ER_BAL 												")
		.append("        ,DEC(A.TRERPCNT,5,2) AS ER_PCT 									")
		.append("        ,C.GRPORDNO AS GRPORDNO 											")
		.append("        ,C.WEBSRTNO AS WEBSRTNO 											")
		.append("  FROM {0}.VLP1090 A 														")
		.append("      ,{0}.VLP1055 C 														")
		.append("      ,(SELECT B.IVACFAID, SUM(B.FIAMT) AS FIAMT 							")
		.append("        FROM {0}.VLP1070 B 												")
		.append("        WHERE B.TRANNO = ? 												")
		.append("        AND SUBSTR(B.MLIMT,1,2) = 'EE' 									")
		.append("        GROUP BY B.IVACFAID) AS EE 										")
		.append("  WHERE A.IVACFAID = EE.IVACFAID 											")
		.append("    AND NOT EXISTS (SELECT B.IVACFAID 										")
		.append("                      FROM {0}.VLP1070 B 									")
		.append("                      WHERE B.TRANNO = ? 									")
		.append("                      AND A.IVACFAID = B.IVACFAID 							")
		.append("                      AND SUBSTR(B.MLIMT,1,2) = 'ER') 						")
		.append("    AND A.IVACFAID = C.IVACFAID 											")
		.append("    AND A.TRANNO = ? 														")
		.append("    AND (A.TREEPCNT > 0 OR A.TRERPCNT > 0) 								")
		.append("UNION ALL 																	")
		.append("  SELECT 'TO' AS FROMTO 													")
		.append("        ,A.IVACFAID 														")
		.append("        ,A.TREEBAL AS EE_BAL 												")
		.append("        ,DEC(A.TREEPCNT,5,2) AS EE_PCT 									")
		.append("        ,A.TRERBAL + ER.FIAMT AS ER_BAL 									")
		.append("        ,DEC(A.TRERPCNT,5,2) AS ER_PCT 									")
		.append("        ,C.GRPORDNO AS GRPORDNO 											")
		.append("        ,C.WEBSRTNO AS WEBSRTNO 											")
		.append("  FROM {0}.VLP1090 A 														")
		.append("      ,{0}.VLP1055 C 														")
		.append("      ,(SELECT B.IVACFAID, SUM(B.FIAMT) AS FIAMT 							")
		.append("        FROM {0}.VLP1070 B 												")
		.append("        WHERE B.TRANNO = ? 												")
		.append("        AND SUBSTR(B.MLIMT,1,2) = 'ER' 									")
		.append("        GROUP BY B.IVACFAID) AS ER 										")
		.append("  WHERE A.IVACFAID = ER.IVACFAID 											")
		.append("    AND NOT EXISTS (SELECT B.IVACFAID 										")
		.append("                      FROM {0}.VLP1070 B 									")
		.append("                      WHERE B.TRANNO = ? 									")
		.append("                      AND A.IVACFAID = B.IVACFAID 							")
		.append("                      AND SUBSTR(B.MLIMT,1,2) = 'EE') 						")
		.append("    AND A.IVACFAID = C.IVACFAID 											")
		.append("    AND A.TRANNO = ? 														")
		.append("    AND (A.TREEPCNT > 0 OR A.TRERPCNT > 0) 								");
		
		SqlStringBuffer sql3 = new SqlStringBuffer()				
		.append("UNION ALL 																	")
		.append("  SELECT 'TO' AS FROMTO 													")
		.append("        ,A.IVACFAID 														")
		.append("        ,A.TREEBAL AS EE_BAL 												")
		.append("        ,DEC(A.TREEPCNT,5,2) AS EE_PCT										")
		.append("        ,A.TRERBAL AS ER_BAL 												")
		.append("        ,DEC(A.TRERPCNT,5,2) AS ER_PCT 									")
		.append("        ,C.GRPORDNO AS GRPORDNO 											")
		.append("        ,C.WEBSRTNO AS WEBSRTNO 											")
		.append("  FROM {0}.VLP1090 A 														")
		.append("      ,{0}.VLP1055 C 														")
		.append("  WHERE A.IVACFAID = C.IVACFAID 											")
		.append("    AND NOT EXISTS (SELECT B.IVACFAID 										")
		.append("                      FROM {0}.VLP1070 B 									")
		.append("                      WHERE B.TRANNO = ? 									")
		.append("                      AND A.IVACFAID = B.IVACFAID 							")
		.append("                      AND SUBSTR(B.MLIMT,1,2) = 'EE') 						")
		.append("    AND NOT EXISTS (SELECT B.IVACFAID 										")
		.append("                      FROM {0}.VLP1070 B 									")
		.append("                      WHERE B.TRANNO = ? 									")
		.append("                      AND A.IVACFAID = B.IVACFAID 							")
		.append("                      AND SUBSTR(B.MLIMT,1,2) = 'ER') 						")
		.append("    AND A.TRANNO = ? 														")
		.append("    AND (A.TREEPCNT > 0 OR A.TRERPCNT > 0) 								")
		.append(" ORDER BY 1, GRPORDNO, WEBSRTNO  ASC                 						");
		
 		String query = MessageFormatHelper.format(sql1.toString(), QUERY_SUBSTITUTIONS) 
 			   + " " + MessageFormatHelper.format(sql2.toString(), QUERY_SUBSTITUTIONS)
 			   + " " + MessageFormatHelper.format(sql3.toString(), QUERY_SUBSTITUTIONS);

		return query;
	}

	private static String getTransactionDetailsSql() {
		
		SqlStringBuffer sql = new SqlStringBuffer();
			 sql.append("SELECT                                               ")
				.append("     D.IVACFAID                                      ")
				.append("    ,B.CNMMLONG                                      ")
				.append("    ,D.FIAMT    	AS TXNAMT                         ")
				.append("    ,D.UNIINTRT                                      ")
				.append("    ,D.NOUNIT                                        ")
				.append("    ,C.GRPORDNO                                      ")
				.append("    ,C.WEBSRTNO                                      ")
				.append("    ,D.TRSUBTYP                                      ")
				.append("  FROM   {0}.VLP1015 A                               ")
				.append("        ,{0}.VLP1031 B                               ")
				.append("        ,{0}.VLP1055 C                               ")
				.append("        ,{0}.VLP1070 D                               ")
				
				.append(" WHERE D.TRANNO 		= ?                           ")
				.append("   AND A.TRANNO 		= D.TRANNO                    ")
				.append("   AND C.IVACFAID 		= D.IVACFAID                  ")
				.append("   AND A.PROPNO 		= B.PROPNO                    ")
				.append("   AND B.MLIMT  		= D.MLIMT                     ")
				
				.append("   AND B.STARTDTE     <= A.RTEFDT                    ")
				.append("   AND B.ENDDTE        > A.RTEFDT                    ")				
				.append("   AND D.TRSUBTYP IN ('TA ','IC ')       			")		
				.append("   UNION ALL										")
				.append("     SELECT D.IVACFAID								")
				.append("   ,''	as CNMMLONG									")
				.append("   ,SUM(D.FIAMT)		AS TXNAMT					")
				.append("   ,D.UNIINTRT										")
				.append("   ,SUM(D.NOUNIT)		AS NOUNIT  					")
				.append("   ,C.GRPORDNO										")
				.append("   ,C.WEBSRTNO										")
				.append("   ,D.TRSUBTYP										")
				.append("   FROM {0}.VLP1015 A								")
				.append("   ,{0}.VLP1031 B									")
				.append("   ,{0}.VLP1055 C									")
				.append("   ,{0}.VLP1070 D									")
				.append("   WHERE D.TRANNO = ?								")
				.append("   AND A.TRANNO = D.TRANNO							")
				.append("   AND C.IVACFAID = D.IVACFAID						")
				.append("   AND A.PROPNO = B.PROPNO							")
				.append("   AND B.MLIMT = D.MLIMT							")
				.append("   AND B.STARTDTE <= A.RTEFDT						")
				.append("   AND B.ENDDTE   >  A.RTEFDT						")
				.append("   AND D.TRSUBTYP = 'RF '							")
				.append("   GROUP BY D.IVACFAID								")
				.append("   ,C.GRPORDNO										")
				.append("   ,C.WEBSRTNO										")
				.append("   ,D.TRSUBTYP										")
			 	.append("	,D.UNIINTRT										");
			SqlStringBuffer sql2 = new SqlStringBuffer();
			sql2.append("   UNION ALL										")
				.append("   SELECT D.IVACFAID								")
				.append("   ,'' as CNMMLONG									")
				.append("   ,SUM(D.FIAMT)		AS TXNAMT					")
				.append("   ,D.UNIINTRT										")
				.append("   ,SUM(D.NOUNIT)		AS NOUNIT					")
				.append("   ,C.GRPORDNO										")
				.append("   ,C.WEBSRTNO										")
				.append("   ,D.TRSUBTYP										")
				.append("   FROM {0}.VLP1015 A								")
				.append("   ,{0}.VLP1031 B									")
				.append("   ,{0}.VLP1055 C									")
				.append("   ,{0}.VLP1070 D									")
				.append("   WHERE D.TRANNO = ?								")
				.append("   AND A.TRANNO = D.TRANNO							")
				.append("   AND C.IVACFAID = D.IVACFAID						")
				.append("   AND A.PROPNO = B.PROPNO							")
				.append("   AND B.MLIMT = D.MLIMT							")
				.append("   AND B.STARTDTE <= A.RTEFDT						")
				.append("   AND B.ENDDTE   >  A.RTEFDT						")
				.append("   AND D.TRSUBTYP = 'MV '							")
				.append("   GROUP BY D.IVACFAID								")
				.append("   ,C.GRPORDNO										")
				.append("   ,C.WEBSRTNO										")
				.append("   ,D.TRSUBTYP										")			
				.append("	,D.UNIINTRT										");	
			String query = MessageFormatHelper.format(sql.toString(), QUERY_SUBSTITUTIONS) 
			   	   + " " + MessageFormatHelper.format(sql2.toString(), QUERY_SUBSTITUTIONS);
		
		return query;
	}

	/**
	 * This inner class transforms a result set into a ContributionDetailsItem
	 * object.
	 */
	private static class DetailItemTransformer extends ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			TransactionDetailsItem item = new TransactionDetailsItem();

  			item.setFundId(rs.getString(Columns.INVESTMENT_ID).trim());
  			item.setMoneyTypeDescription(rs.getString(Columns.MONEY_TYPE_LONG_NAME).trim());
  			item.setSubtype(rs.getString(Columns.TRANSACTION_SUB_TYPE).trim());
			item.setAmount(getBigDecimalOrZero(rs, Columns.AMOUNT));
			item.setUnitValue(getBigDecimalOrZero(rs, Columns.UNIT_VALUE));
			item.setNumberOfUnits(getBigDecimalOrZero(rs, Columns.NO_OF_UNITS));
 			item.setSortNo(rs.getInt(Columns.SORT_NO));
			item.setRiskCategoryCode(getString(rs,Columns.RISK_GROUP_NO)); 			
 			item.setComments(TransactionHistoryHelper.getComments(item.getSubtype()));

			return item;
		}
	}
}