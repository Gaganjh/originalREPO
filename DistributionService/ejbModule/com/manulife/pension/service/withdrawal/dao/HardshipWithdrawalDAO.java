package com.manulife.pension.service.withdrawal.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.apache.log4j.Logger;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

public class HardshipWithdrawalDAO extends BaseDatabaseDAO {
	
	private static final String className = HardshipWithdrawalDAO.class.getName();
	private static final Logger logger = Logger.getLogger(HardshipWithdrawalDAO.class);
	private static final String LSA_FUND = "LSA";
	private static final String EEDEF_MONEY_TYPE = "EEDEF";
	private static final String EMPLOYEE_CONTRIBUTION = "EE";
	
	private static final String GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS =	
	        "call " + APOLLO_STORED_PROC_SCHEMA_NAME + ".LP_TPA_NET_EEDEF(?,?,?,?,?,?)";
	
	private static final String GET_PARTICIPANT_ACCOUNT_DTL = "SELECT "
			+" CB.INVESTMENT_OPTION_ID INVESTMENT_OPTION_ID, "
			+" MT.CONTRACT_MONEY_TYPE_LONG_NAME CONTRACT_MONEY_TYPE_LONG_NAME, "
			+" MT.MONEY_TYPE_CATEGORY_CODE MONEY_TYPE_CATEGORY_CODE,"
			+" CB.COMPOSITE_RATE COMPOSITE_RATE, "
			+" CB.UNITS_HELD_QTY UNITS_HELD_QTY, "
			+" CB.TOTAL_BALANCE_AMT TOTAL_BALANCE_AMT, "
			+" CB.MONEY_TYPE_ID"
			+" FROM "
			+" PSW100.CONTRACT_MONEY_TYPE MT, "
			+" PSW100.PARTICIPANT_CURRENT_BAL_LSA CB "
			+" WHERE MT.CONTRACT_ID=CB.CONTRACT_ID "
			+" AND MT.MONEY_TYPE_ID=CB.MONEY_TYPE_ID "
			+" AND CB.CONTRACT_ID=? "
			+" AND CB.PROFILE_ID=? "
			+" ORDER BY INVESTMENT_OPTION_ID,MONEY_TYPE_CATEGORY_CODE,CONTRACT_MONEY_TYPE_LONG_NAME ";
	
	private static final int ZEROPAD = 0;
	/**
	* Retrieves the participant net emploee controbutions (EE type) from the Apollo db2 stored procedure.
	* @param contractNumber contract id
	* @param ssn  ssn
	* @param asOfDate	The as of date for the request query
	* @return netEmployeeContibution
	* @throws SystemException
	*/
	public static double getParticipantNetEEDeferralContributions(int contractNumber, String ssn, Date asOfDate)
        throws SystemException	{

		if (logger.isDebugEnabled() )
			logger.debug("entry -> getParticipantNetEEDeferralContributions");
			
        Connection conn = null;
        CallableStatement statement = null;
        double netEEConributions = -1;
        
        try {

        	// setup the connection and the statement
            conn = getReadUncommittedConnection(className, APOLLO_DATA_SOURCE_NAME);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);            
            statement = conn.prepareCall(GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS);
            
 
            if (logger.isDebugEnabled() )
				logger.debug("Calling Stored Procedure: "+GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS);
            
            // pad zero with contract number to support 5 and 6 digits             
            String padContractNumber = Integer.toString(contractNumber);
            while(padContractNumber.length() < GlobalConstants.CONTRACT_NUMBER_MAX_LENGTH){
            	padContractNumber = ZEROPAD+padContractNumber;
            }
            // set the input parameters
            
            // Updated the call signature in stored-procedure in getParticipantNetEEDeferralContributions() 
            // to match the Apollo signature. It seems after the upgrade DB2 universal driver there is no 
            // default conversion and parms must be explicitly            
            statement.setString(1, padContractNumber); // statement.setInt(1, contractNumber);
            statement.setString(2, ssn);
            
            if (asOfDate != null)
            	statement.setString(3, new java.sql.Date(asOfDate.getTime()).toString()); // statement.setDate(3, new java.sql.Date(asOfDate.getTime()));
            else
               	statement.setString(3, " ");
            	//statement.setNull(3, Types.VARCHAR);
            
            // register the output parameters
            statement.registerOutParameter(4, Types.CHAR);// return Code
            statement.registerOutParameter(5, Types.CHAR);// Msg Number  
            statement.registerOutParameter(6, Types.CHAR);// Net EE Deferral conribution
            
            // execute the stored procedure
            statement.execute();
                      
            // get the output parameters            
            if ("OK".equalsIgnoreCase(statement.getString(4)) )
            	netEEConributions = (new Double(statement.getString(6))).doubleValue(); 
            else
            	throw new SQLException(statement.getString(4));
            // The following is the possible error codes from Apollo.	
            //	Contract number not numeric		+0001
			//	Contact does not exist on Apollo		+0003
			//	Participant SSN not numeric		+0002
			//	Participant does not exist under contract		+0004
			//	Request date not a business date or not a processed date in the past on Apollo.		+0005
			//	Request date cannot be found on Apollo calendar table 		+0006
			//	Request date not in the right date format of yyyy-mm-dd		+0007
			//	Unexpected DB2 SQLCODE		Sqlcode
			//	Apollo batch is running		9999
            	
        } catch (SQLException e) {
           throw new SystemException(e,
           		"Problem occurred during GET_PARTICIPANT_NET_EE_DEFERRAL_CONTRIBUTIONS stored proc call. Input parameters are contractNumber:" + 
           				contractNumber + 
           				", ssn:" + ssn +
           				", asOfDate:" + asOfDate);
        } finally {
        	close(statement, conn);
        }

        if (logger.isDebugEnabled() )
			logger.debug("exit <- getParticipantNetEEDeferralContributions");

        return netEEConributions;
    }
	/**
	 * getParticiantEEDeferralContributions
	 * @param profileId
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public static double getParticiantEEDeferralContributions(int profileId,int contractId) throws SystemException{
		
		double eeDefBalance = 0;
		Connection conn = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        String moneyType ;
        String moneyTypeName ;
        double compositeRate;
        double balance;
		 try {

	        	// setup the connection and the statement
	            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
	            statement = conn.prepareCall(GET_PARTICIPANT_ACCOUNT_DTL);
	            statement.setInt(1, contractId);
	            statement.setInt(2, profileId);
	            statement.execute();
	            resultSet = statement.getResultSet();

           	    
	            while (resultSet.next())
	            {
	            	
	            	String investmentId = resultSet.getString("INVESTMENT_OPTION_ID").trim();
	            	// protect against bad data coming back from the stored procedure
	            	
	   	           // HardshipMoneyTypeVO moneyTypeDetailVO = new HardshipMoneyTypeVO();
	   	            moneyType =resultSet.getString("CONTRACT_MONEY_TYPE_LONG_NAME").trim();
	   	           moneyTypeName =resultSet.getString("MONEY_TYPE_CATEGORY_CODE").trim();
	   	            compositeRate = resultSet.getDouble("COMPOSITE_RATE");
	   	            
	   	            // Suppressing number of units values for SVGIF 
	   	           
	   	            balance = resultSet.getDouble("TOTAL_BALANCE_AMT");    
	   	            String moteyTypeId = resultSet.getString("MONEY_TYPE_ID").trim();
	            	
	            	if ( !investmentId.startsWith(LSA_FUND) ) {
		   	         	
	
		   	            if ( moneyTypeName.equals(EMPLOYEE_CONTRIBUTION) ) {
		   	            	
							// Ths following code added in order to PPR.424
							// We need to display the lesser amount of the CSDB EEDEF value 
							// or Apollo STP result		   	            	
		   	            	if ( EEDEF_MONEY_TYPE.equals(moteyTypeId) )
		   	            		eeDefBalance += balance;
		   	            }
		   	            
	            	
	            		
	            }
	            
	            }
	 
	            if (logger.isDebugEnabled() )
					logger.debug("Calling Stored Procedure: "+GET_PARTICIPANT_ACCOUNT_DTL);
		 } catch (SQLException e) {
	           throw new SystemException(e,
	              		"Problem occurred during GET_PARTICIPANT_ACCOUNT_DTL stored proc call. Input parameters are contractNumber:" + 
	              				contractId + 
	              				", profileId:" + profileId );
	           } finally {
	           	close(statement, conn);
	           }
		
		return eeDefBalance;
		
	}
}
