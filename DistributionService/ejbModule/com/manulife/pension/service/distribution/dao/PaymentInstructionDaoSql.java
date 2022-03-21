package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;


public interface PaymentInstructionDaoSql {


    public static final String SQL_DELETE_PAYMENT_INSTRUCTION = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("PAYEE_PAYMENT_INSTRUCTION where ") 
            .append("submission_id = ?").toString();    

    public static final String SQL_INSERT_PAYMENT_INSTRUCTION = new StringBuffer()
            .append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("PAYEE_PAYMENT_INSTRUCTION (")
            .append("SUBMISSION_ID,")
            .append("RECIPIENT_NO,")
            .append("PAYEE_NO,")
            .append("BANK_ACCOUNT_TYPE_CODE,")
            .append("BANK_TRANSIT_NO,")
            .append("BANK_ACCOUNT_NO,")
            .append("BANK_NAME,")
            .append("CREDIT_PARTY_NAME,")
            .append("CREATED_USER_PROFILE_ID,")
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")
            .append("LAST_UPDATED_TS,")
            .append("PAYMENT_AMOUNT")
            .append(") values (")
            .append("?,?,?,?,?,?,?,?,?,CURRENT TIMESTAMP, ? , CURRENT TIMESTAMP,?) ").toString();
    
    public static final int[] INSERT_TYPES = { Types.INTEGER, Types.SMALLINT, Types.SMALLINT,
            Types.CHAR, Types.DECIMAL, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DECIMAL,
            Types.DECIMAL,Types.DECIMAL };
    
    public static final String SELECT_ALL_PAYMENT_INSTRUCTIONS_SQL = new StringBuffer()
	    .append("SELECT ")
        .append("SUBMISSION_ID,")
        .append("RECIPIENT_NO,")
        .append("PAYEE_NO,")
        .append("BANK_ACCOUNT_TYPE_CODE,")
        .append("BANK_TRANSIT_NO,")
        .append("BANK_ACCOUNT_NO,")
        .append("BANK_NAME,")
        .append("CREDIT_PARTY_NAME,")
        .append("CREATED_USER_PROFILE_ID,")
        .append("CREATED_TS,")
        .append("LAST_UPDATED_USER_PROFILE_ID,")
        .append("LAST_UPDATED_TS")
	    .append(" FROM   ")
	    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
	    .append("PAYEE_PAYMENT_INSTRUCTION ")
	    .append("WHERE SUBMISSION_ID = ?").toString();
	
	public static final int[] SELECT_ALL_PAYMENT_INSTRUCTIONS_PARAMETER_TYPES = { Types.INTEGER };
	
	public static final String[] SELECT_ALL_PAYMENT_INSTRUCTIONS_RESULT_FIELDS = {
			"submissionId", "recipientNo", "payeeNo", "bankAccountTypeCode",
			"bankTransitNumber", "bankAccountNumber", "bankName",
			"creditPartyName", "createdById", "created", "lastUpdatedById",
			"lastUpdated" };

}
