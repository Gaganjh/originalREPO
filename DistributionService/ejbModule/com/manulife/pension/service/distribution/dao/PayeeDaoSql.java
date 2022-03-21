package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;


public interface PayeeDaoSql {
    public static final String SQL_DELETE_PAYEE = new StringBuffer()
    .append("delete from ")
    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
    .append("PAYEE where ") 
    .append("submission_id = ?").toString();

	public static final String SQL_INSERT_PAYEE = new StringBuffer()
	    .append("insert into ")
	    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
	    .append("PAYEE (")
	    .append("SUBMISSION_ID,")
	    .append("RECIPIENT_NO,")
	    .append("PAYEE_NO,")
	    .append("FIRST_NAME,")
	    .append("LAST_NAME,")
	    .append("ORGANIZATION_NAME,")
	    .append("PAYEE_TYPE_CODE,")
	    .append("PAYEE_REASON_CODE,")
	    .append("PAYMENT_METHOD_CODE,")
	    .append("SHARE_TYPE_CODE,")
	    .append("SHARE_VALUE,")
	    .append("ROLLOVER_ACCOUNT_NO,")
	    .append("ROLLOVER_PLAN_NAME,")
	    .append("IRS_DIST_CODE_WITHDRAWAL,")
	    .append("MAIL_CHECK_TO_ADDRESS_IND,")
	    .append("SEND_CHECK_BY_COURIER_IND,")
	    .append("COURIER_COMPANY_CODE,")
	    .append("COURIER_NO,")
	    .append("CREATED_USER_PROFILE_ID,")
	    .append("CREATED_TS,")
	    .append("LAST_UPDATED_USER_PROFILE_ID,")
	    .append("LAST_UPDATED_TS,")
	    .append("TAX_FLAGS,")
	    .append("PAYEE_INFO")
	    .append(") values (")
	    .append("?,?,?,?,?,?,?,?,?,")
	    .append("?,?,?,?,?,?,?,?,?,?, CURRENT TIMESTAMP, ? , CURRENT TIMESTAMP,?,?) ").toString();
	
	public static final int[] INSERT_TYPES = { Types.INTEGER, Types.SMALLINT, Types.SMALLINT,
	    Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.CHAR, Types.CHAR, Types.CHAR,
	    Types.CHAR, Types.DECIMAL, Types.VARCHAR, Types.VARCHAR, Types.CHAR, Types.CHAR,
	    Types.CHAR, Types.CHAR, Types.VARCHAR, Types.DECIMAL, Types.DECIMAL,Types.VARCHAR, Types.VARCHAR };

    public static final String SELECT_ALL_PAYEES_SQL = new StringBuffer()
	    .append("SELECT ")
	    .append("SUBMISSION_ID,")
	    .append("RECIPIENT_NO,")
	    .append("PAYEE_NO,")
	    .append("FIRST_NAME,")
	    .append("LAST_NAME,")
	    .append("ORGANIZATION_NAME,")
	    .append("PAYEE_TYPE_CODE,")
	    .append("PAYEE_REASON_CODE,")
	    .append("PAYMENT_METHOD_CODE,")
	    .append("SHARE_TYPE_CODE,")
	    .append("SHARE_VALUE,")
	    .append("ROLLOVER_ACCOUNT_NO,")
	    .append("ROLLOVER_PLAN_NAME,")
	    .append("IRS_DIST_CODE_WITHDRAWAL,")
	    .append("MAIL_CHECK_TO_ADDRESS_IND,")
	    .append("SEND_CHECK_BY_COURIER_IND,")
	    .append("COURIER_COMPANY_CODE,")
	    .append("COURIER_NO,")
	    .append("CREATED_USER_PROFILE_ID,")
	    .append("CREATED_TS,")
	    .append("LAST_UPDATED_USER_PROFILE_ID,")
	    .append("LAST_UPDATED_TS")
	    .append(" FROM   ")
	    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
	    .append("PAYEE ")
	    .append("WHERE SUBMISSION_ID = ?").toString();
	
	public static final int[] SELECT_ALL_PAYEES_PARAMETER_TYPES = { Types.INTEGER };
	
	public static final String[] SELECT_ALL_PAYEES_RESULT_FIELDS = {
			"submissionId", "recipientNo", "payeeNo", "firstName", "lastName",
			"organizationName", "typeCode", "reasonCode", "paymentMethodCode",
			"shareTypeCode", "shareValue", "rolloverAccountNo",
			"rolloverPlanName", "irsDistCode", "mailCheckToAddress",
			"sendCheckByCourier", "courierCompanyCode", "courierNo",
			"createdById", "created", "lastUpdatedById", "lastUpdated" };

}
