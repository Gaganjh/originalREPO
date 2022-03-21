package com.manulife.pension.ps.service.report.transaction.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.exception.ContractNumberDoesNotmatchException;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionItem;
import com.manulife.pension.ps.service.report.transaction.valueobject.LoanRepaymentTransactionReportData;
import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.service.report.dao.DirectSqlReportDAOHelper;
import com.manulife.pension.service.report.dao.ReportItemTransformer;
import com.manulife.pension.service.report.dao.ReportServiceBaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * This class assembles the data required for the Loan Details transaction. It
 * uses db2connect to access mainframe Apollo tables directly. The schema it
 * uses is defined in the System.properties in the PlanSponsor Server instance.
 * Change the value of the "apollo.schema.name" property to access different
 * databases in Apollo.
 * 
 * @author Ludmila Stern
 */

public class LoanRepaymentTransactionDAO extends ReportServiceBaseDAO {
	private static final Logger logger = Logger.getLogger(LoanRepaymentTransactionDAO.class);
	private static final String DEBIT_TABLE = ".vlp7222";
	private static final String LOAN_ACCOUNT_MOVEMENT_TABLE = ".vlp1155";
	private static final String PARTICIPANT_TABLE = ".vlp1074";
	private static final String CONTRACT_NUMBER = "CNNO";
	private static final String NUMBER_OF_PARTICIPANTS = "NO_OF_PRT";
	private static final String INTEREST_AMT = "INTR_AMT";
	private static final String PRINCIPAL_AMT = "PRIN_AMT";
	private static final String LOAN_REPAYMENT_AMT = "LOAN_AMT";
	private static final String LOAN_NUMBER = "LOAN_NO";
	private static final String LAST_NAME = "PRTLSTNM";
	private static final String FIRST_NAME = "PRTFSTNM";
	private static final String PARTICIPANT_ID = "PART_ID";
	private static final String PARTICIPANT_SSN = "SSN";
	private static final String RECORD_COUNT = "REC_COUNT";
	private static final String className = LoanRepaymentTransactionDAO.class.getName();
	private static final TransactionLoanItemTransformer transformer =
		new TransactionLoanItemTransformer();
	private static final Map fieldToColumnMap = new HashMap();

	/*
	/* Sets up the field to column map.
	 */
	static {
		fieldToColumnMap.put(
			LoanRepaymentTransactionReportData.SORT_FIELD_LAST_NAME,
			LAST_NAME);
		fieldToColumnMap.put(
			LoanRepaymentTransactionReportData.SORT_FIELD_FIRST_NAME,
			FIRST_NAME);
		fieldToColumnMap.put(LoanRepaymentTransactionReportData.SORT_FIELD_SSN, PARTICIPANT_SSN);
		fieldToColumnMap.put(LoanRepaymentTransactionReportData.SORT_FIELD_LOAN_NUMBER, LOAN_NUMBER);
		fieldToColumnMap.put(LoanRepaymentTransactionReportData.SORT_FIELD_REPAYMENT, LOAN_REPAYMENT_AMT);
		fieldToColumnMap.put(LoanRepaymentTransactionReportData.SORT_FIELD_PRINCIPAL, PRINCIPAL_AMT);
		fieldToColumnMap.put(LoanRepaymentTransactionReportData.SORT_FIELD_INTEREST, INTEREST_AMT);
	}

	public static LoanRepaymentTransactionReportData getData(ReportCriteria criteria)
		throws SystemException, ReportServiceException {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("schema set to -> " + APOLLO_SCHEMA_NAME);
		}
		String strTranno =
			(String) criteria.getFilterValue(
				LoanRepaymentTransactionReportData.FILTER_TRANSACTION_NUMBER);
		Integer contractNumber = Integer.parseInt((String) criteria.getFilterValue(
				LoanRepaymentTransactionReportData.FILTER_CONTRACT_NUMBER));
		BigDecimal tranno = new BigDecimal (strTranno);
		LoanRepaymentTransactionReportData reportData = null;
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {

			connection = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
			// get summary
			stmt = connection.prepareStatement(getSummarySql());
			stmt.setBigDecimal(1, tranno);
			stmt.setBigDecimal(2, tranno);
			rs = stmt.executeQuery();
			reportData = new LoanRepaymentTransactionReportData(criteria, 0);
			rs.next();
			reportData.setTransactionNumber(tranno.toString());
			reportData.setContractNumber(rs.getString(CONTRACT_NUMBER));
			reportData.setNumberOfParticipants(rs.getInt(NUMBER_OF_PARTICIPANTS));
			reportData.setTotalInterestAmount(rs.getBigDecimal(INTEREST_AMT));
			reportData.setTotalPrincipalAmount(rs.getBigDecimal(PRINCIPAL_AMT));
			reportData.setTotalRepaymentAmount(rs.getBigDecimal(LOAN_REPAYMENT_AMT));
			close(stmt, null);
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
			//get count
			stmt = connection.prepareStatement(getCountSql());
			stmt.setBigDecimal(1, tranno);
			rs = stmt.executeQuery();
			rs.next();
			reportData.setTotalCount(rs.getInt(RECORD_COUNT));
			close(stmt, null);

			//get details
			stmt = connection.prepareStatement(getSortedDetailsSql(criteria));
			stmt.setBigDecimal(1, tranno);
			rs = stmt.executeQuery();
			List items = DirectSqlReportDAOHelper.getReportItems(criteria, rs, transformer);
			reportData.setDetails(items);
 			}
		} catch (SQLException e) {
			handleSqlException(
				e,
				className,
				"LoanRepaymentTransactionReportData",
				"SQLException" + "tranno: " + tranno);

		} finally {
			close(stmt, connection);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

		return reportData;

	}
		/*
	/* The loan principal and interest are kept as neagtive amounts on Apollo. 
	/* We are reversing them here (* -1) to display as positive numbers as per business specifications
	*/
	private static String getSummarySql() {
		StringBuffer sql =
			new StringBuffer()
          		.append("SELECT VLOAN.CNNO,  ")                                                
            	.append("	VLOAN.RTEFDT, ")                                        
            	.append(" 	VLOAN.TRTYP,  ")                                       
            	.append("   VLOAN.TXAMT AS LOAN_AMT,  ")                                                                        
            	.append("	SUM(VLOAN.PRINAMT * -1) AS PRIN_AMT, ")                            
            	.append("   SUM( VLOAN.INTRAMT * -1) AS INTR_AMT, ")
		   		.append("	VLOAN2.NOOFPRT AS NO_OF_PRT")                             
            	.append(" FROM ")                                           
          		.append(" 	(SELECT   A.CNNO,   ")                                            
            	.append("		A.RTEFDT, ")                                      
            	.append("	    A.TRTYP, ")                                       
            	.append("       A.TXAMT, ")                                                                   
            	.append("		CASE  WHEN B.TRSUBTYP = 'PRI' ")                  
            	.append("			THEN SUM(B.LOMOVAMT)  ")
            	.append("			ELSE DEC(0.00,11,0) ")                      
           	 	.append("		END AS PRINAMT, ")                                
           	 	.append("		CASE  WHEN B.TRSUBTYP = 'INT' ")                  
            	.append("	        THEN SUM(B.LOMOVAMT)  ")
            	.append("           ELSE DEC(0.00,11,0) ")                      
            	.append("		END AS INTRAMT  ") 
                .append("	FROM ")                                
				.append(APOLLO_SCHEMA_NAME).append(DEBIT_TABLE).append(" A, ")
				.append(APOLLO_SCHEMA_NAME).append(LOAN_ACCOUNT_MOVEMENT_TABLE).append(" B ")
				.append(" 		WHERE  A.TRANNO   = ? ")
				.append(" 		AND    A.TRANNO   =  B.TRANNO ")
				.append(" 		AND    A.TRTYP    =  'LR' ")
				.append("		GROUP BY A.CNNO,A.RTEFDT, ")
				.append(" 			A.TRTYP, ")
				.append(" 			A.TXAMT, ")
				.append("			B.TRSUBTYP ") 
				.append(" 	) AS VLOAN, ")
				.append("	( SELECT ")
				.append("		C.RTEFDT, ")                                      
                .append("		C.TRTYP, ")                                       
                .append("	    C.TXAMT, ")                                                                    
				.append("		COUNT(DISTINCT(D.PRTID)) AS NOOFPRT ") 
				.append("	FROM ")                           
				.append(APOLLO_SCHEMA_NAME).append(DEBIT_TABLE).append(" C, ")
				.append(APOLLO_SCHEMA_NAME).append(LOAN_ACCOUNT_MOVEMENT_TABLE).append(" D ")
                .append(" 		WHERE  C.TRANNO   = ?")                                       
           		.append("		AND    C.TRANNO   =  D.TRANNO  ")                       
           		.append("		AND    C.TRTYP    =  'LR'  ")                            
           		.append("		GROUP BY C.RTEFDT, ")                                    
             	.append("		C.TRTYP, ")                                             
             	.append("		C.TXAMT	")                                                                                   
         		.append("	) AS VLOAN2 ")                                            
           		.append("	GROUP BY VLOAN.CNNO, VLOAN.RTEFDT, ")                                
             	.append("	VLOAN.TRTYP,  ")                                     
             	.append("	VLOAN.TXAMT , ")
				.append("	VLOAN2.NOOFPRT ");
		return sql.toString().trim();
	}

	private static String getCountSql() {

		String sql =
			new SqlStringBuffer(" SELECT COUNT(*) AS ")
				.append(RECORD_COUNT)
				.append(" FROM (")
				.append(getDetailsSql())
				.append(") AS IT_TEMP")
				.toString();
		return sql;

	}

	private static String getSortedDetailsSql(ReportCriteria criteria) {

		StringBuffer sql = new StringBuffer(getDetailsSql());
		//Appends the ORDER BY clause when there is a specific sorting requirement.

		if (criteria.getSorts().size() > 0) {
			sql.append(" ORDER BY ").append(criteria.getSortPhrase(fieldToColumnMap));
		}

		return sql.toString().trim();

	}
/*
	/* The loan principal and interest are kept as neagtive amounts on Apollo. 
	/* We are reversing them here (* -1) to display as positive numbers as per business specifications
	/* Track Record 7240 - reversing has to be done in the presentation layer due to the arrow  
*/
	private static String getDetailsSql() {

		StringBuffer sql =
			new StringBuffer()
				.append("	SELECT ")
                .append("	RTEFDT, ")                                           
                .append("	TRTYP, ")                                          
                .append("	PRTID AS PART_ID, ")                                           
                .append("	LOANNO AS LOAN_NO, ")                                           
                .append("	SUM(PRINAMT) AS PRIN_AMT,  ")                         
                .append("	SUM(INTRAMT) AS INTR_AMT,  ")                         
                .append("	SUM(PRINAMT + INTRAMT) AS LOAN_AMT, ") 
/*                
                .append("	SUM(PRINAMT * -1) AS PRIN_AMT,  ")                         
                .append("	SUM(INTRAMT * -1) AS INTR_AMT,  ")                         
                .append("	SUM(PRINAMT + INTRAMT) * -1 AS LOAN_AMT, ") 
*/                                
                .append("	PRTSSN AS SSN, ")                                           
                .append("	PRTLSTNM, ")                                        
                .append("	PRTFSTNM ")
                .append("FROM ")
                .append("(SELECT ")                                                     
                .append("	A.RTEFDT, ") 
                .append("   A.TRTYP, ")                                                                                     
                .append("	SUM(B.LOMOVAMT) AS LOANAMT, ")                         
                .append("	B.PRTID, ")                                            
                .append("	B.LOANNO, ")                                      
                .append("	CASE  WHEN B.TRSUBTYP = 'PRI' ")                   
                .append("		THEN SUM(B.LOMOVAMT) ")                     
                .append("		ELSE DEC(0.00,11,0) ")                     
                .append("	END AS PRINAMT, ")                                 
                .append("	CASE  WHEN B.TRSUBTYP = 'INT' ")                   
                .append("		THEN SUM(B.LOMOVAMT) ")                     
                .append("		ELSE DEC(0.00,11,0) ")                      
                .append("	END AS INTRAMT, ")                                
                .append("	E.PRTSSN, ")                                       
                .append("	E.PRTLSTNM, ")                                     
                .append("	E.PRTFSTNM ")                                 
				.append(" 		FROM ")
				.append(APOLLO_SCHEMA_NAME).append(DEBIT_TABLE).append(" A, ")
				.append(APOLLO_SCHEMA_NAME).append(LOAN_ACCOUNT_MOVEMENT_TABLE).append(" B, ")
				.append(APOLLO_SCHEMA_NAME).append(PARTICIPANT_TABLE).append(" E ")
           		.append(" 		WHERE  A.TRANNO   = ?  ")                       
           		.append(" 		AND    A.TRANNO   =  B.TRANNO ")                         
           		.append(" 		AND    A.TRTYP    =  'LR' ")                                
          		.append(" 		AND    B.TRSUBTYP IN ('INT','PRI') ")                       
           		.append("		AND    B.PRTID    = E.PRTID ") 
				.append(" 		GROUP BY A.RTEFDT, ")
             	.append(" 			A.TRTYP, ")             	                                             
             	.append(" 			B.PRTID, ")                                              
             	.append(" 			B.TRSUBTYP, ")                                            
             	.append(" 			B.LOANNO, ")                                             
             	.append(" 			B.LOMOVAMT, ")                                            
             	.append(" 			E.PRTSSN,  ")                                             
             	.append(" 			E.PRTLSTNM, ")                                            
             	.append(" 			E.PRTFSTNM ") 
        		.append(" ) AS DETLOAN ")                                                
           		.append("	GROUP BY DETLOAN.RTEFDT, ")                                 
             	.append("		DETLOAN.TRTYP, ")                                     
             	.append("		DETLOAN.PRTID, ")                                        
             	.append("		DETLOAN.LOANNO, ")                                        
             	.append("		DETLOAN.PRTSSN, ")                                       
             	.append("		DETLOAN.PRTLSTNM, ")                                      
                .append("		DETLOAN.PRTFSTNM ");

		return sql.toString().trim();
	}

	private static class TransactionLoanItemTransformer
		extends ReportItemTransformer {
		public Object transform(ResultSet rs) throws SQLException {
			LoanRepaymentTransactionItem item = new LoanRepaymentTransactionItem();
			item.setLoanNumber((short) rs.getBigDecimal(LOAN_NUMBER).intValue());
			item.setInterestAmount(rs.getBigDecimal(INTEREST_AMT));
			item.setPrincipalAmount(rs.getBigDecimal(PRINCIPAL_AMT));
			item.setRepaymentAmount(rs.getBigDecimal(LOAN_REPAYMENT_AMT));
			ParticipantVO participant = new ParticipantVO();
			participant.setLastName(getString(rs, LAST_NAME));
			participant.setFirstName(getString(rs, FIRST_NAME));
			participant.setSsn(getString(rs, PARTICIPANT_SSN));
			Integer id = new Integer(rs.getInt(PARTICIPANT_ID));
			participant.setId(id);
			item.setParticipant(participant);
			return item;

		}
	}
}
