package com.manulife.pension.service.loan.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public interface LoanParameterDaoSql {
    
    public static final int[] INSERT_TYPES = { Types.INTEGER, Types.CHAR, Types.DECIMAL,
			Types.DECIMAL, Types.DECIMAL, Types.CHAR, Types.INTEGER, Types.DECIMAL, Types.DECIMAL,
			Types.TIMESTAMP, Types.DECIMAL, Types.TIMESTAMP };

    public static final String SQL_INSERT_LOAN_PARAMETER = new StringBuffer()
            .append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("loan_stage_parameter (")
            .append("SUBMISSION_ID,")//Types.INTEGER
			.append("SUB_CASE_PROCESS_STATUS_CODE,") //Types.CHAR
			.append("MAX_LOAN_AVAILABLE_AMT,")//Types.DECIMAL
			.append("LOAN_AMOUNT,")//Types.DECIMAL
			.append("PAYMENT_AMOUNT,")//Types.DECIMAL
			.append("PAYMENT_FREQUENCY_CODE,")//Types.CHAR
			.append("AMORTIZATION_MONTHS,")//Types.INTEGER
			.append("INTEREST_RATE_PCT,")//Types.DECIMAL
            .append("CREATED_USER_PROFILE_ID,")// Types.DECIMAL
            .append("CREATED_TS,")//Types.TIMESTAMP
            .append("LAST_UPDATED_USER_PROFILE_ID,")// Types.DECIMAL
            .append("LAST_UPDATED_TS") //Types.TIMESTAMP
            .append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)").toString();

    public static final int[] UPDATE_TYPES = { Types.DECIMAL, Types.DECIMAL,
			Types.DECIMAL, Types.CHAR, Types.INTEGER, Types.DECIMAL,
			Types.INTEGER, Types.TIMESTAMP, Types.INTEGER, Types.CHAR };
    
    public static final String SQL_UPDATE_LOAN_PARAMETER = new StringBuffer()
            .append("update ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("loan_stage_parameter set ")
            .append("MAX_LOAN_AVAILABLE_AMT = ?,") //Types.DECIMAL
            .append("LOAN_AMOUNT = ?,")// Types.DECIMAL
            .append("PAYMENT_AMOUNT = ?,") //Types.DECIMAL
            .append("PAYMENT_FREQUENCY_CODE = ?,") // Types.CHAR
            .append("AMORTIZATION_MONTHS = ?,") // Types.INTEGER
            .append("INTEREST_RATE_PCT = ?,") //Types.DECIMAL
            .append("LAST_UPDATED_USER_PROFILE_ID = ?,") // Types.DECIMAL 
            .append("LAST_UPDATED_TS = ? ") // Types.TIMESTAMP
            .append("WHERE SUBMISSION_ID = ? AND SUB_CASE_PROCESS_STATUS_CODE = ?").toString();  // Types.DECIMAL, Types.CHAR
    
    public static final String SQL_CHECK_EXISTS = new StringBuffer()
            .append("select submission_id from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("loan_stage_parameter ")
            .append("where submission_id = ? and SUB_CASE_PROCESS_STATUS_CODE = ? FOR FETCH ONLY").toString();    
    
    public static final String SQL_DELETE_ALL_LOAN_PARAMETERS = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("loan_stage_parameter where submission_id = ?").toString();
    

    public static final String SQL_SELECT_LOAN_PARAMETERS = new StringBuffer()
    		.append("select ")
			.append("SUB_CASE_PROCESS_STATUS_CODE,")
			.append("MAX_LOAN_AVAILABLE_AMT,")
			.append("LOAN_AMOUNT,")
			.append("PAYMENT_AMOUNT,")
			.append("PAYMENT_FREQUENCY_CODE,")
			.append("AMORTIZATION_MONTHS,")
			.append("INTEREST_RATE_PCT,")
			.append("CREATED_USER_PROFILE_ID,")
			.append("CREATED_TS,")
			.append("LAST_UPDATED_USER_PROFILE_ID,")
			.append("LAST_UPDATED_TS ")
			.append("from ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME).append("loan_stage_parameter ")
			.append("where submission_id = ? FOR FETCH ONLY").toString();


}
