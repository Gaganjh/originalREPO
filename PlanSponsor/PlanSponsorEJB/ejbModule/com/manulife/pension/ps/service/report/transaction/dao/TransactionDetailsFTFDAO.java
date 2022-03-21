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

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.heartbeat.monitor.TransactionMonitor;
import com.manulife.pension.ps.service.report.exception.ContractNumberDoesNotmatchException;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsFTFReportData;
import com.manulife.pension.ps.service.report.transaction.valueobject.TransactionDetailsItem;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.util.tools.MessageFormatHelper;
 
public class TransactionDetailsFTFDAO extends ReportServiceBaseDAO {
	private static final String className = TransactionDetailsFTFDAO.class.getName();
	private static final boolean DEBUG_SQL = true;
	/**
	 * A map of database column keyed by sort field.
	 */
	private static Map fieldToColumnMap = new HashMap();
	private static DetailItemTransformer transformer = new DetailItemTransformer();
	private static Logger logger = Logger.getLogger(TransactionDetailsFTFDAO.class);
	/**
	 * The substitutions used in all query strings.
	 */
	private static Object[] QUERY_SUBSTITUTIONS = new Object[] { APOLLO_SCHEMA_NAME };
	private static final String TRANSACTION_SUMMARY_SQL = getTransactionSummarySql();
	private static final String TRANSFER_FROM_TO_SQL = getTransferFromToSql();
	private static final String TRANSACTION_DETAILS_SQL = getTransactionDetailsSql();
	
	private static interface Columns {
			String SORT_NO = "WEBSRTNO";
			String CONTRACT_NUMBER = "CNNO";
			String PARTICIPANT_FIRSTNAME = "PRTFSTNM";
			String PARTICIPANT_LASTNAME = "PRTLSTNM";
			String PARTICIPANT_SSN = "PRTSSN";

	 		String RATE_EFFECTIVE_DATE = "RTEFDT";
	 		String REQUEST_DATE = "TREFDT";
			String AMOUNT = "TXNAMT";
			String MEDIUM = "MEDIUM";
			String SOURCE = "SOURCE";
			
			String FROM_TO = "FROMTO";
			String MONEY_TYPE_LONG_NAME = "CNMMLONG";
			String PERCENTAGE = "PCTINOUT";
			String INVESTMENT_ID = "IVACFAID";
		
			String UNIT_VALUE = "UNIINTRT";
			String NO_OF_UNITS = "NOUNIT";
			String RISK_GROUP_NO = "GRPORDNO";
			String TRANSACTION_SUB_TYPE = "TRSUBTYP";
			String TRANSACTION_TYPE = "TRANSTYPE";
			String REASONCODE = "REASONCODE";
	}

	static {
		/*
		 * Sets up the field to column map.
		 */
		fieldToColumnMap.put(TransactionDetailsFTFReportData.SORT_FIELD_RISK_CATEGORY, Columns.RISK_GROUP_NO);
		fieldToColumnMap.put(TransactionDetailsFTFReportData.SORT_FIELD_WEBSRTNO, Columns.SORT_NO);
		fieldToColumnMap.put(TransactionDetailsFTFReportData.SORT_FIELD_MONEY_TYPE_DESCRIPTION, Columns.MONEY_TYPE_LONG_NAME);
	}

	public static TransactionDetailsFTFReportData getReportData(
			ReportCriteria criteria) throws SystemException, ReportServiceException {

		Connection connection = null;
		PreparedStatement stmt = null;

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		String transactionNumber = getTransactionNumber(criteria);
		Integer contractNumber = getContractNumber(criteria);
		Integer participantId = getParticipantId(criteria);	

		TransactionDetailsFTFReportData reportData = null;

		try {
			TransactionMonitor.getTransactionMonitor().startTransaction(Thread.currentThread().getName(), "TransactionDetailsFTFDAO.getReportData()");
			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
				
			reportData = new TransactionDetailsFTFReportData(criteria, 0);
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
					"Something goes wrong with the statement - Contract ["
							+ contractNumber + "] TX [" 
							+ transactionNumber + "] Participant Id [" 
							+ getParticipantId(criteria) 
							+ "]");
		} finally {
			TransactionMonitor.getTransactionMonitor().finishTransaction(Thread.currentThread().getName());			
			close(stmt, connection);
		}

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
			return Integer.valueOf((String) criteria.getFilterValue(TransactionDetailsFTFReportData.FILTER_CONTRACT_NUMBER));
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
		try {
			return Integer.valueOf((String)criteria.getFilterValue(TransactionDetailsFTFReportData.FILTER_PARTICIPANT_ID));
		} catch (NumberFormatException e) {
			logger.error("Received NumberFormatException. Can't format participant id to Integer. ", e);
			throw new EJBException(e);								
		}
	}
	
	/**
	 * Gets the transaction number filter from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The transaction number.
	 */
	private static String getTransactionNumber(ReportCriteria criteria) {
		return (String) criteria.getFilterValue(TransactionDetailsFTFReportData.FILTER_TRANSACTION_NUMBER);
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
			ReportCriteria criteria, TransactionDetailsFTFReportData reportData) throws SQLException {

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
			stmt.setString(2, transactionNumber);
			stmt.setString(3, transactionNumber);
			
			ResultSet rs = stmt.executeQuery();
		
			while (rs.next()) {
				reportData.setContractNumber(rs.getString(Columns.CONTRACT_NUMBER));
				reportData.setTransactionDate(rs.getDate(Columns.RATE_EFFECTIVE_DATE));
				reportData.setRequestDate(rs.getDate(Columns.REQUEST_DATE));
				reportData.setTotalAmount(getBigDecimalOrZero(rs, Columns.AMOUNT));				
				reportData.setMediaCode(TransactionHistoryHelper
						.getMediumDescription(getString(rs, Columns.MEDIUM),
								null, getString(rs, Columns.TRANSACTION_TYPE),
								getString(rs, Columns.REASONCODE)));
				reportData.setSourceOfTransfer(getString(rs, Columns.SOURCE));
				String name = rs.getString(Columns.PARTICIPANT_LASTNAME) + ", " + rs.getString(Columns.PARTICIPANT_FIRSTNAME); 
				reportData.setParticipantName(name);
				reportData.setParticipantSSN(rs.getString(Columns.PARTICIPANT_SSN));
				reportData.setParticipantUnmaskedSSN(rs.getString(Columns.PARTICIPANT_SSN));
			}
		} finally {
			close(stmt, null);
		} 
		
		// translate data form db contract, PTF.3
		Boolean isDBContract = (Boolean)criteria.getFilterValue(TransactionDetailsFTFReportData.CONTRACT_TYPE_DB);
		if ((isDBContract != null) && 
			(isDBContract.booleanValue() == true) && 
			(reportData.getSourceOfTransfer() != null)) {
			
			if (reportData.getSourceOfTransfer().startsWith("Participant initiated")) {
				reportData.setSourceOfTransfer("Plan Sponsor initiated transfer");
			}	
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
			TransactionDetailsFTFReportData reportData) throws SQLException {

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
			stmt.setString(1, transactionNumber);
			stmt.setString(2, transactionNumber);
			stmt.setString(3, transactionNumber);
			stmt.setString(4, transactionNumber);
			stmt.setString(5, transactionNumber);
			
			ResultSet rs = stmt.executeQuery();

 			while (rs.next()) {
				TransactionDetailsItem item = new TransactionDetailsItem();
				
				String fromToString = getString(rs, Columns.FROM_TO);
				
				boolean isFrom = 
  					( fromToString.equals("FROM") ? true : false);
 				
 				item.setFundId(getString(rs, Columns.INVESTMENT_ID));
 					
 				item.setMoneyTypeDescription( 
 					getString(rs, Columns.MONEY_TYPE_LONG_NAME));
 					
				item.setAmount(
					getBigDecimalOrZero(rs, Columns.AMOUNT));
					
				item.setPercentage( 
					getBigDecimalOrZero(rs, Columns.PERCENTAGE));

				item.setRiskCategoryCode(getString(rs,Columns.RISK_GROUP_NO));
				
				if (isFrom) {
					reportData.getTransferFroms().add(item);
				} else {
					reportData.getTransferTos().add(item);
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
			TransactionDetailsFTFReportData reportData) throws SQLException,
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
		
		SqlStringBuffer sql = new SqlStringBuffer()
				.append("SELECT  A.CNNO, C.PRTLSTNM ")
				.append("	,C.PRTFSTNM ")
				.append("	,C.PRTSSN ")
				.append("	,A.RTEFDT ")
				.append("	,A.TREFDT ")
				.append("	,SUM(B.FIAMT)*-1 AS TXNAMT ")
				.append("	,A.TRANNO ")
				.append("	,A.ALLOCMED         AS MEDIUM ")
				.append("	,MAX(SEL.SRCETRAN)  AS SOURCE ")
				.append("	,A.TRTYP AS TRANSTYPE ")
				.append("	,A.TRRSNCD AS REASONCODE ")
				.append("FROM  {0}.VLP1015 A ")
				.append("	,{0}.VLP1070 B ")
				.append("	,{0}.VLP1074 C ")
				.append("	,(SELECT ")
				.append("	CASE ")
				.append("		WHEN E.TRTYP = 'IT' ")
				.append("		AND  E.ALLOCMED = 'PA' ")
				.append("		AND  E.PYEEIND = 'N' ")
				.append("		THEN 'John Hancock initiated transfer' ")
				.append("	 	WHEN  E.TRRSNCD = 'DO' ")
				.append("		THEN 'Transfer from Default Investment Options' ")
				.append("    	ELSE 'Participant initiated transfer' ")
				.append("   END AS SRCETRAN ")
				.append("FROM {0}.VLP1015 E ")
				.append("WHERE  E.TRANNO   = ? ")
				.append("AND NOT EXISTS ")
				.append("(SELECT * ")
				.append("FROM {0}.VLP1143 D ")
				.append("WHERE D.ASSORSN =  'CLEV' ")
				.append("AND D.TRTYP1 = 'IT' ")
				.append("AND E.TRANNO = D.TRANNO1) ")
				.append("UNION ALL ")
				.append("SELECT 'Plan Sponsor initiated transfer' AS SRCETRAN ")
				.append("FROM {0}.VLP1015 E ")
				.append("    ,{0}.VLP1143 F ")
				.append("WHERE E.TRTYP = 'IT' ")				
				.append("AND  F.ASSORSN = 'CLEV' ")
				.append("AND  E.TRANNO  = ? ")
				.append("AND  F.TRANNO1  = E.TRANNO ) AS SEL ")
				.append("WHERE  A.TRANNO = ? ")
				.append("AND  B.TRANNO = A.TRANNO ")
				.append("AND  B.FIAMT < 0 ")
				.append("AND  A.PRTID  = C.PRTID ")
				.append("GROUP BY A.CNNO, C.PRTLSTNM ")
				.append(",C.PRTFSTNM ")
				.append(",C.PRTSSN ")
				.append(",A.RTEFDT ")
				.append(",A.TREFDT ")
				.append(",A.TRANNO ")
				.append(",A.ALLOCMED ")
				.append(",SEL.SRCETRAN ")
				.append(",A.TRTYP ")
				.append(",A.TRRSNCD ");
		
		String query = MessageFormatHelper.format(sql.toString(),
				QUERY_SUBSTITUTIONS);

		return query;
	}

	private static String getTransferFromToSql() {
		
		SqlStringBuffer sql1 = new SqlStringBuffer()
				.append("SELECT                                               ")
				.append("        'FROM'			AS FROMTO                     ")
				.append("       ,D.IVACFAID                                   ")
				.append("       ,'' 			AS CNMMLONG                   ")
				.append("       ,SUM(E.FIAMT)  	AS TXNAMT                     ")
				.append("       ,D.WDRLPCNT     AS PCTINOUT                   ")
				.append("       ,A.XFRINSIN                                   ")
				.append("       ,C.GRPORDNO                                   ")
				.append("       ,C.WEBSRTNO                                   ")
				
				.append("  FROM  {0}.VLP1015 A                                ")
				.append("       ,{0}.VLP1055 C                                ")
				.append("       ,{0}.VLP1089 D                                ")
				.append("       ,{0}.VLP1070 E                                ")
				
				.append(" WHERE  D.TRANNO   = ?                       	      ")
				.append("   AND  A.TRANNO = D.TRANNO                          ")
				.append("   AND  A.TRANNO = E.TRANNO                          ")
				.append("   AND  D.MLIMT    = 'ALL'                           ")
				.append("   AND  C.IVACFAID = D.IVACFAID                      ")
				.append("   AND  D.IVACFAID = E.IVACFAID                      ")
				.append("   AND  E.FIAMT < 0                                  ")
				.append("GROUP BY                                             ") 
				.append("   D.IVACFAID                                        ")
				.append("  ,D.WDRLPCNT                                        ")
				.append("  ,A.XFRINSIN                                        ")
				.append("  ,C.GRPORDNO                                        ")
				.append("  ,C.WEBSRTNO                                        ");
				
		SqlStringBuffer sql2 = new SqlStringBuffer()				
				.append("UNION ALL                                            ")
				
				.append("SELECT                                               ")
				.append("        'FROM'			AS FROMTO                     ")
				.append("       ,D.IVACFAID                                   ")
				.append("       ,B.CNMMLONG                                   ")
				.append("       ,SUM(E.FIAMT)  	AS TXNAMT                     ")
				.append("       ,D.WDRLPCNT 	AS PCTINOUT                   ")
				.append("       ,A.XFRINSIN                                   ")
				.append("       ,C.GRPORDNO                                   ")
				.append("       ,C.WEBSRTNO                                   ")
				
				.append("  FROM  {0}.VLP1015 A                                ")
				.append("       ,{0}.VLP1031 B                                ")
				.append("       ,{0}.VLP1055 C                                ")
				.append("       ,{0}.VLP1089 D                                ")
				.append("       ,{0}.VLP1070 E                                ")
				
				.append(" WHERE  D.TRANNO   = ?                       	      ")
				.append("   AND  A.TRANNO   = D.TRANNO                        ")
				.append("   AND  D.TRANNO   = E.TRANNO                        ")
				.append("   AND  D.MLIMT <> 'ALL'                             ")
				.append("   AND  D.MLIMS = 'ALL'                              ")
				.append("   AND  C.IVACFAID = D.IVACFAID                      ")
				.append("   AND  C.IVACFAID = E.IVACFAID                      ")
				.append("   AND  A.PROPNO   = B.PROPNO                        ")
				.append("   AND  B.MLIMT    = D.MLIMT                         ")
				.append("   AND  D.MLIMT 	= E.MLIMT                         ")
				
				.append("   AND  B.STARTDTE <= A.RTEFDT                       ")
				.append("   AND  B.ENDDTE   >  A.RTEFDT                       ")
				.append("   AND  E.FIAMT < 0                                  ")
				
				.append("GROUP BY                                             ") 
				.append("   D.IVACFAID                                        ")
				.append("  ,B.CNMMLONG                                        ")
				.append("  ,D.WDRLPCNT                                        ")
				.append("  ,A.XFRINSIN                                        ")
				.append("  ,C.GRPORDNO                                        ")
				.append("  ,C.WEBSRTNO                                        ") 

				.append("UNION ALL                                            ")

				.append("SELECT                                               ")
				.append("        'FROM'			AS FROMTO                     ")
				.append("       ,D.IVACFAID                                   ")
				.append("       ,B.CNMMLONG                                   ")
				.append("       ,E.FIAMT 	  	AS TXNAMT                     ")
				.append("       ,D.WDRLPCNT 	AS PCTINOUT                   ")
				.append("       ,A.XFRINSIN                                   ")
				.append("       ,C.GRPORDNO                                   ")
				.append("       ,C.WEBSRTNO                                   ")
				
				.append("  FROM  {0}.VLP1015 A                                ")
				.append("       ,{0}.VLP1031 B                                ")
				.append("       ,{0}.VLP1055 C                                ")
				.append("       ,{0}.VLP1089 D                                ")
				.append("       ,{0}.VLP1070 E                                ")
				
				.append(" WHERE  D.TRANNO   = ?                       	      ")
				.append("   AND  A.TRANNO   = D.TRANNO                        ")
				.append("   AND  D.TRANNO   = E.TRANNO                        ")
				.append("   AND  D.MLIMT <> 'ALL'                             ")
				.append("   AND  D.MLIMS <> 'ALL'                             ")
				.append("   AND  C.IVACFAID = D.IVACFAID                      ")
				.append("   AND  C.IVACFAID = E.IVACFAID                      ")
				.append("   AND  A.PROPNO   = B.PROPNO                        ")
				.append("   AND  B.MLIMT    = D.MLIMT                         ")
				.append("   AND  D.MLIMT    = E.MLIMT                         ")
				.append("   AND  D.MLIMS    = E.MLIMS                         ")
				
				.append("   AND  B.STARTDTE <= A.RTEFDT                       ")
				.append("   AND  B.ENDDTE   >  A.RTEFDT                       ")
				.append("   AND  E.FIAMT < 0                                  ")

				.append("UNION ALL                                            ");
		
		SqlStringBuffer sql3 = new SqlStringBuffer()				
				.append("SELECT                                               ")
				.append("        'TO'			AS FROMTO                     ")
				.append("       ,SUM.IVACFAID                                 ")
				.append("       ,'' 			AS CNMMLONG                   ")
				.append("       ,SUM.AMT	  	AS TXNAMT                     ")
				.append("       ,DECIMAL(FLOAT(SUM.AMT)*100/TOT.AMT,6,3) AS PCTINOUT  ")
				.append("       ,' ' AS XFRINSIN                              ")
				.append("       ,B.GRPORDNO                                   ")
				.append("       ,B.WEBSRTNO                                   ")
				
				.append("  FROM  {0}.VLP1055 B                                ")
				.append("        ,(SELECT SUM(FIAMT) AS AMT                   ")
				.append("			 FROM {0}.VLP1070                         ")
				.append("           WHERE TRANNO  = ?                         ")
				.append("             AND FIAMT > 0 ) AS TOT                  ")
				
				.append("        ,(SELECT C.IVACFAID,SUM(FIAMT) AS AMT        ")
				.append("			 FROM {0}.VLP1070 C                       ")
				.append("			     ,{0}.VLP1055 D                       ")
				.append("           WHERE TRANNO  = ?                         ")
				.append("             AND FIAMT > 0                           ")
				.append("             AND C.IVACFAID = D.IVACFAID             ")
				.append("             GROUP BY C.IVACFAID) AS SUM             ")
				
				.append(" WHERE  B.IVACFAID = SUM.IVACFAID                    ")
								
				.append(" ORDER BY 1, GRPORDNO, WEBSRTNO, CNMMLONG  ASC  WITH UR     ");

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
				
				.append("   AND B.STARTDTE      <= A.RTEFDT                   ")
				.append("   AND B.ENDDTE        >  A.RTEFDT                   ")
				.append("   AND D.TRSUBTYP IN ('TA ','IC ')       			")		
				.append("   UNION ALL										")
				.append("     SELECT D.IVACFAID								")
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
				.append("   AND D.TRSUBTYP = 'RF '							")
				.append("   GROUP BY D.IVACFAID								")
				.append("   ,C.GRPORDNO										")
				.append("   ,C.WEBSRTNO										")
				.append("   ,D.TRSUBTYP										")
		 	 	.append("	,D.UNIINTRT										");
			SqlStringBuffer sql2 = new SqlStringBuffer();
			sql2.append("   UNION ALL										")
				.append("   SELECT D.IVACFAID								")
				.append("   ,''	 as CNMMLONG								")
				.append("   ,SUM(D.FIAMT)		AS TXNAMT					")
				.append("   ,D.UNIINTRT										")
				.append("   ,SUM(D.NOUNIT)	    AS	NOUNIT					")
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
	private static class DetailItemTransformer extends
			ReportItemTransformer {

		public Object transform(ResultSet rs) throws SQLException {
			
			TransactionDetailsItem item = new TransactionDetailsItem();
			
  			item.setFundId(rs.getString(Columns.INVESTMENT_ID).trim());
  			item.setMoneyTypeDescription(rs.getString(Columns.MONEY_TYPE_LONG_NAME).trim());
  			item.setSubtype(rs.getString(Columns.TRANSACTION_SUB_TYPE).trim());
  			
  			// suppress money type description for MVA & redemption fees
			if (TransactionHistoryHelper.isMva(item.getSubtype())
					|| TransactionHistoryHelper.isRedemptionFees(item.getSubtype()) ) {
				item.setMoneyTypeDescription("");
			}
			item.setAmount(
				getBigDecimalOrZero(rs, Columns.AMOUNT));
			item.setUnitValue(
				getBigDecimalOrZero(rs, Columns.UNIT_VALUE));
			item.setNumberOfUnits(
				getBigDecimalOrZero(rs, Columns.NO_OF_UNITS));
 			item.setSortNo(rs.getInt(Columns.SORT_NO));
 			item.setRiskCategoryCode(rs.getString(Columns.RISK_GROUP_NO));
 			
 			item.setComments(
 				TransactionHistoryHelper.getComments(item.getSubtype()));
 				
			return item;
		}
	}
}