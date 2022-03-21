package com.manulife.pension.service.loan.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

/**
 * Class which contains SQL queries to make insertion / update / retrieve
 * participant risk information to database.
 * 
 * @author Vasanth Balaji
 * 
 */
public class DistributionAtRiskDaoSql {
	
	
	public static final int[] INSERT_TYPES = { Types.INTEGER, Types.DATE, Types.DATE,
		Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.CHAR, Types.CHAR, Types.VARCHAR,
		Types.DECIMAL, Types.CHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
		Types.CHAR, Types.CHAR, Types.VARCHAR, Types.DECIMAL, Types.CHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE,
		Types.VARCHAR, Types.DECIMAL, Types.DECIMAL, Types.CHAR, Types.DATE, Types.VARCHAR, Types.DECIMAL, Types.CHAR, 
		Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.TIMESTAMP };
	
	public static final int[] UPDATE_TYPES = { Types.DATE, Types.DATE,
		Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.CHAR, Types.CHAR, Types.VARCHAR,
		Types.DECIMAL, Types.CHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
		Types.CHAR, Types.CHAR, Types.VARCHAR, Types.DECIMAL, Types.CHAR, Types.VARCHAR, Types.VARCHAR, Types.DATE,
		Types.VARCHAR, Types.DECIMAL, Types.DECIMAL, Types.CHAR, Types.DATE, Types.VARCHAR, Types.DECIMAL, Types.CHAR, 
		Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER };
	
	
	public static final String SQL_SELECT_DISTRIBUTION_ATRISK_DATA = new StringBuffer().append("select SUBMISSION_ID,")
			.append("WEB_REGISTRATION_DATE,")
			.append("WEB_REG_CONFIRMATION_MAILED_DATE,")
			.append("CONFIRM_MAIL_ADDR_LINE1,")
			.append("CONFIRM_MAIL_ADDR_LINE2,")
			.append("CONFIRM_MAIL_CITY_NAME,")
			.append("CONFIRM_MAIL_STATE_CODE,")
			.append("CONFIRM_MAIL_ZIP_CODE,")
			.append("CONFIRM_MAIL_COUNTRY_NAME,")
			.append("CONFIRM_UPDATED_PROFILE_ID,")
			.append("CONFIRM_UPDATED_USER_ID_TYPE,")			
			.append("CONFIRM_UPDATED_USER_FIRST_NAME,")
			.append("CONFIRM_UPDATED_USER_LAST_NAME,")
			.append("APPROVAL_MAIL_ADDR_LINE1,")
			.append("APPROVAL_MAIL_ADDR_LINE2,")
			.append("APPROVAL_MAIL_CITY_NAME,")
			.append("APPROVAL_MAIL_STATE_CODE,")
			.append("APPROVAL_MAIL_ZIP_CODE,")
			.append("APPROVAL_MAIL_COUNTRY_NAME,")
			.append("APPROVAL_UPDATED_PROFILE_ID,")
			.append("APPROVAL_UPDATED_USER_ID_TYPE,")
			.append("APPROVAL_UPDATED_USER_FIRST_NAME,")
			.append("APPROVAL_UPDATED_USER_LAST_NAME,")
			.append("EMAIL_PASSWORD_RESET_DATE,")
			.append("EMAIL_PASSWORD_RESET_USED_EMAIL_ADDRESS,")
			.append("EMAIL_PASSWORD_RESET_INITIATED_PROFILE_ID,")
			.append("EMAIL_ADDRESS_LAST_UPDATED_PROFILE_ID,")
			.append("EMAIL_ADDRESS_LAST_UPDATED_USER_ID_TYPE,")
			.append("FORGOT_PASSWORD_REQUESTED_DATE,")
			.append("FORGOT_PASSWORD_EMAIL_ADDRESS,")
			.append("FORGOT_PASSWORD_UPDATED_PROFILE_ID,")
			.append("FORGOT_PASSWORD_UPDATED_USER_ID_TYPE,")
			.append("FORGOT_PASSWORD_UPDATED_USER_FIRST_NAME,")
			.append("FORGOT_PASSWORD_UPDATED_USER_LAST_NAME,")
			.append("CREATED_TS,")
			.append("LAST_UPDATED_TS from ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME).append("DISTRIBUTION_AT_RISK_DETAIL")
			.append(" where SUBMISSION_ID = ? FOR FETCH ONLY")
			.toString();
	
	
	public static final String SQL_INSERT_DISTRIBUTION_ATRISK_DATA = new StringBuffer()
			.append("insert into ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME).append("DISTRIBUTION_AT_RISK_DETAIL (")
			.append("SUBMISSION_ID,")
			.append("WEB_REGISTRATION_DATE,")
			.append("WEB_REG_CONFIRMATION_MAILED_DATE,")
			.append("CONFIRM_MAIL_ADDR_LINE1,")
			.append("CONFIRM_MAIL_ADDR_LINE2,")
			.append("CONFIRM_MAIL_CITY_NAME,")
			.append("CONFIRM_MAIL_STATE_CODE,")
			.append("CONFIRM_MAIL_ZIP_CODE,")
			.append("CONFIRM_MAIL_COUNTRY_NAME,")
			.append("CONFIRM_UPDATED_PROFILE_ID,")
			.append("CONFIRM_UPDATED_USER_ID_TYPE,")			
			.append("CONFIRM_UPDATED_USER_FIRST_NAME,")
			.append("CONFIRM_UPDATED_USER_LAST_NAME,")
			.append("APPROVAL_MAIL_ADDR_LINE1,")
			.append("APPROVAL_MAIL_ADDR_LINE2,")
			.append("APPROVAL_MAIL_CITY_NAME,")
			.append("APPROVAL_MAIL_STATE_CODE,")
			.append("APPROVAL_MAIL_ZIP_CODE,")
			.append("APPROVAL_MAIL_COUNTRY_NAME,")
			.append("APPROVAL_UPDATED_PROFILE_ID,")
			.append("APPROVAL_UPDATED_USER_ID_TYPE,")
			.append("APPROVAL_UPDATED_USER_FIRST_NAME,")
			.append("APPROVAL_UPDATED_USER_LAST_NAME,")
			.append("EMAIL_PASSWORD_RESET_DATE,")
			.append("EMAIL_PASSWORD_RESET_USED_EMAIL_ADDRESS,")
			.append("EMAIL_PASSWORD_RESET_INITIATED_PROFILE_ID,")
			.append("EMAIL_ADDRESS_LAST_UPDATED_PROFILE_ID,")
			.append("EMAIL_ADDRESS_LAST_UPDATED_USER_ID_TYPE,")
			.append("FORGOT_PASSWORD_REQUESTED_DATE,")
			.append("FORGOT_PASSWORD_EMAIL_ADDRESS,")
			.append("FORGOT_PASSWORD_UPDATED_PROFILE_ID,")
			.append("FORGOT_PASSWORD_UPDATED_USER_ID_TYPE,")
			.append("FORGOT_PASSWORD_UPDATED_USER_FIRST_NAME,")
			.append("FORGOT_PASSWORD_UPDATED_USER_LAST_NAME,")
			.append("CREATED_TS,")
			.append("LAST_UPDATED_TS")
			.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
			.toString();
	
	public static final String SQL_UPDATE_DISTRIBUTION_ATRISK_DATA = new StringBuffer()
    .append("update ")
    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
    .append("DISTRIBUTION_AT_RISK_DETAIL set ")
    .append("WEB_REGISTRATION_DATE = ?,") 
    .append("WEB_REG_CONFIRMATION_MAILED_DATE = ?,")
    .append("CONFIRM_MAIL_ADDR_LINE1 = ?,") 
    .append("CONFIRM_MAIL_ADDR_LINE2 = ?,") 
    .append("CONFIRM_MAIL_CITY_NAME = ?,") 
    .append("CONFIRM_MAIL_STATE_CODE = ?,")
    .append("CONFIRM_MAIL_ZIP_CODE = ?,") 
    .append("CONFIRM_MAIL_COUNTRY_NAME = ?,")
    .append("CONFIRM_UPDATED_PROFILE_ID = ?,")
    .append("CONFIRM_UPDATED_USER_ID_TYPE = ?,")
    .append("CONFIRM_UPDATED_USER_FIRST_NAME = ?,")
    .append("CONFIRM_UPDATED_USER_LAST_NAME = ?,")
    .append("APPROVAL_MAIL_ADDR_LINE1 = ?,")
    .append("APPROVAL_MAIL_ADDR_LINE2 = ?,")
    .append("APPROVAL_MAIL_CITY_NAME = ?,")
    .append("APPROVAL_MAIL_STATE_CODE = ?,")
    .append("APPROVAL_MAIL_ZIP_CODE = ?,")
    .append("APPROVAL_MAIL_COUNTRY_NAME = ?,")
    .append("APPROVAL_UPDATED_PROFILE_ID = ?,")
    .append("APPROVAL_UPDATED_USER_ID_TYPE = ?,")
    .append("APPROVAL_UPDATED_USER_FIRST_NAME = ?,")
    .append("APPROVAL_UPDATED_USER_LAST_NAME = ?,")
    .append("EMAIL_PASSWORD_RESET_DATE = ?,")
    .append("EMAIL_PASSWORD_RESET_USED_EMAIL_ADDRESS = ?,")
    .append("EMAIL_PASSWORD_RESET_INITIATED_PROFILE_ID = ?,")
    .append("EMAIL_ADDRESS_LAST_UPDATED_PROFILE_ID = ?,")
    .append("EMAIL_ADDRESS_LAST_UPDATED_USER_ID_TYPE = ?,")
    .append("FORGOT_PASSWORD_REQUESTED_DATE = ?,")
    .append("FORGOT_PASSWORD_EMAIL_ADDRESS = ?,")
    .append("FORGOT_PASSWORD_UPDATED_PROFILE_ID = ?,")
    .append("FORGOT_PASSWORD_UPDATED_USER_ID_TYPE = ?,")
    .append("FORGOT_PASSWORD_UPDATED_USER_FIRST_NAME = ?,")
    .append("FORGOT_PASSWORD_UPDATED_USER_LAST_NAME = ?,")
    .append("LAST_UPDATED_TS = ? ")    
    .append("WHERE SUBMISSION_ID = ?").toString();
	
	public static final String SQL_CHECK_EXISTS = new StringBuffer()
    .append("select submission_id from ")
    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
    .append("DISTRIBUTION_AT_RISK_DETAIL ")
    .append("where submission_id = ? FOR FETCH ONLY").toString();
	
	
	public static final String SELECT_WEB_REG_ADDRESS = new StringBuffer() 
	.append("select ")
	.append("addr_line1 ")
	.append("from ")
	.append(BaseDatabaseDAO.CUSTOMER_SCHEMA_NAME)
	.append("EMPLOYEE_NOTIFICATION_MAIL ")
	.append("where ")
	.append("ACTIVITY_TYPE_CODE = '25' AND ")
	.append("PROFILE_ID = ? AND ")
	.append("CONTRACT_ID = ? AND ")
	.append("PROCESSED_TS is not null AND ")
	.append("CREATED_TS <= ?  order by CREATED_TS fetch first 1 rows only ").toString();
	   
}


