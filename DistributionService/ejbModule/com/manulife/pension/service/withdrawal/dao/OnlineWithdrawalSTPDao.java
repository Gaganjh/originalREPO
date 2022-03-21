package com.manulife.pension.service.withdrawal.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;

public class OnlineWithdrawalSTPDao extends  BaseDatabaseDAO {
    
    private static final String CLASS_NAME = OnlineWithdrawalSTPDao.class.getName();
    private static final String LP_TXN_GEN  = 
     		"call " + APOLLO_STORED_PROC_SCHEMA_NAME+".LP_TXN_GEN(?,?,?,?,?,?,?,?,?)";
    private static final String APOLLO_OW_STP_SQL = 
    		"call " + APOLLO_STORED_PROC_SCHEMA_NAME+".LP_OWD_TXN_UPD(?,?,?,?)";
    
    
    public String executeLpTxnGenSTPStoredProc(final Integer contractId, 
            final BigDecimal participantId, final Integer submissionId,  final String reasonCode,final Date rateEffectiveDate,final String tpaFeeFlag,int payeeCount) throws SystemException {

        Connection connection = null;
        CallableStatement callableStatement = null;
        String controlBlock = null;
        //String statusCode = null;
       // String returnCode = null;
        try {
            long beforePrepareCall = System.currentTimeMillis();
            connection = getReadUncommittedConnection(CLASS_NAME, APOLLO_DATA_SOURCE_NAME);
            callableStatement = connection.prepareCall(LP_TXN_GEN);
            callableStatement.registerOutParameter(1, java.sql.Types.CHAR); // Control block
            callableStatement.setInt(2, contractId); // Contract number 
            callableStatement.setBigDecimal(3, participantId); // participant id
            callableStatement.setInt(4, submissionId); // submission id
            callableStatement.setString(5, "WD"); // TRANSACTION_TYPE 
            callableStatement.setString(6, reasonCode); // TRANSACTION_REASON_CODE
            callableStatement.setDate(7, rateEffectiveDate); // RATE_EFFECTIVE_DATE DATE
            callableStatement.setString(8, tpaFeeFlag); // TPA_FEE_FLAG 
            callableStatement.setInt(9, payeeCount);
           
            callableStatement.execute();
            
            // Log the procedure execution time for reference
            long afterPrepareCall = System.currentTimeMillis();
            //Verify return status
            controlBlock = callableStatement.getString(1);
            // parse the control block: This is not used here, you can log them here or even remove them from here
           // statusCode = controlBlock.substring(0, 2);
           // returnCode = controlBlock.substring(2, 7);
        } catch (SQLException sqlException) {
            String errorMsg = "SQL exception occurred during executeApolloStoredProc method call. Input parameters  are: "+
                  " contractId: "+ contractId
                + " partId: " + participantId 
                + " submissionId: " + submissionId;
            throw new SystemException(sqlException, errorMsg);
            
        } finally {
            close(callableStatement, connection);
        }
        //log the details from this control block like error or transaction number at the place where you call
        return controlBlock;
    }
    public String executeApolloOWSTPStoredProc(final Integer contractId, 
            final BigDecimal participantId, final Integer submissionId) throws SystemException {

        Connection connection = null;
        CallableStatement callableStatement = null;
        String controlBlock = null;
        String statusCode = null;
        String returnCode = null;
        try {
            long beforePrepareCall = System.currentTimeMillis();
            connection = getReadUncommittedConnection(CLASS_NAME, APOLLO_DATA_SOURCE_NAME);
            callableStatement = connection.prepareCall(APOLLO_OW_STP_SQL);
            callableStatement.registerOutParameter(1, java.sql.Types.CHAR); // Control block
            callableStatement.setInt(2, contractId); // Contract number 
            callableStatement.setBigDecimal(3, participantId); // participant id
            callableStatement.setInt(4, submissionId); // submission id
           
            callableStatement.execute();
            
            // Log the procedure execution time for reference
            long afterPrepareCall = System.currentTimeMillis();
            //Verify return status
            controlBlock = callableStatement.getString(1);
            // parse the control block: This is not used here, you can log them here or even remove them from here
            statusCode = controlBlock.substring(0, 2);
            returnCode = controlBlock.substring(2, 7);
        } catch (SQLException sqlException) {
            String errorMsg = "SQL exception occurred during executeApolloStoredProc method call. Input parameters  are: "+
                  " contractId: "+ contractId
                + " partId: " + participantId 
                + " submissionId: " + submissionId;
            throw new SystemException(sqlException, errorMsg);
            
        } finally {
            close(callableStatement, connection);
        }
        //log the details from this control block like error or transaction number at the place where you call
        return controlBlock;
    }

}

