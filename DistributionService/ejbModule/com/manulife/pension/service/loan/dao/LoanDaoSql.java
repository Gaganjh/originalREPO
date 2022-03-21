package com.manulife.pension.service.loan.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.dao.DaoConstants.SchemaName;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;

public interface LoanDaoSql {
	public static final String SUBMISSION_SEQUENCE = new StringBuffer().append(
			BaseDatabaseDAO.JOURNAL_SCHEMA_NAME).append("submission_id")
			.toString();

	public static final String SQL_INSERT_SUBMISSION = new StringBuffer()
			.append("INSERT into  ").append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission (").append("SUBMISSION_ID,").append(
					"SUBMISSION_TS,").append("FILE_NAME,").append("MAP_NAME,")
			.append("INPUT_LOCATION_NAME,").append("USER_ID,").append(
					"CREATED_USER_ID,").append("CREATED_TS,").append(
					"LAST_UPDATED_USER_ID,").append("LAST_UPDATED_TS,").append(
					"PAYMENT_INFO_ONLY_IND").append(
					" ) values (?, ?, '', '', '', ?, ?, ").append(
					"?, ?, ?, 'N')").toString();

	public static final String SQL_INSERT_SUBMISSION_CASE = new StringBuffer()
			.append("INSERT into ").append(
					BaseDatabaseDAO.STP_SCHEMA_NAME + "submission_case (")
			.append("SUBMISSION_ID,").append("CONTRACT_ID,").append(
					"SUBMISSION_CASE_TYPE_CODE,").append("SYNTAX_ERROR_IND,")
			.append("PROCESSED_TS,").append("PROCESS_STATUS_CODE,").append(
					"CREATED_USER_ID,").append("CREATED_TS,").append(
					"LAST_UPDATED_USER_ID,").append("LAST_UPDATED_TS,").append(
					"LAST_LOCKED_BY_USER_ID,").append("LAST_LOCKED_TS,")
			.append("SUBMIT_COUNT").append(
					" ) values (?, ?, ?, 'N' ,?, ? , ?, ").append(
					"?, ?, ?, null, null, 1)").toString();

	public static final int[] SQL_INSERT_SUBMISSION_PARAMETER_TYPES = {
			Types.INTEGER, Types.TIMESTAMP, Types.INTEGER, Types.INTEGER,
			Types.TIMESTAMP, Types.INTEGER, Types.TIMESTAMP };

	public static final String SQL_INSERT_SUBMISSION_LOAN = new StringBuffer()
			.append("insert into ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission_loan (")
			.append("SUBMISSION_ID,")
			.append("CONTRACT_ID,")
			.append("SUBMISSION_CASE_TYPE_CODE,")
			.append("PROFILE_ID,")
			.append("PARTICIPANT_ID,")
			.append("CREATED_BY_ROLE_CODE,")
			.append("LEGALLY_MARRIED_IND,")
			.append("LOAN_TYPE_CODE,")
			.append("LOAN_REASON_EXPLANATION,")
			.append("REQUEST_DATE,")
			.append("EXPIRATION_DATE,")
			.append("LOAN_EFFECTIVE_DATE,")
			.append("LOAN_MATURITY_DATE,")
			.append("FIRST_PAYROLL_DATE,")
			.append("MAX_AMORTIZATION_YEARS,")
			.append("DEFAULT_PROVISION,")
			.append("MAX_OS_LOAN_BAL_LAST12MTHS_AMT,")
			.append("OUTSTANDING_LOANS_COUNT,")
			.append("CURR_OUTSTANDING_LOAN_BAL_AMT,")
			.append("LAST_FEE_CHNG_BY_TPA_PROF_ID,")
			.append("LAST_FEE_CHNG_WAS_PS_USER_IND,")
			.append("CREATED_USER_PROFILE_ID,")
			.append("CREATED_TS,")
			.append("LAST_UPDATED_USER_PROFILE_ID,")
			.append("LAST_UPDATED_TS,")
			.append("LOAN_EXPENSE_MARGIN_PCT,")
            .append("MINIMUM_LOAN_AMT,")
            .append("MAXIMUM_LOAN_AMT,")
            .append("MAXIMUM_LOAN_PCT,")
            .append("LOAN_MONTHLY_FLAT_FEE_AMT,")
            .append("SPOUSAL_CONSENT_REQD_IND,")
			.append("AT_RISK_IND,")
			.append("APPLY_IRS_10K_DOLLAR_RULE_IND)")
			.append(
					"values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
			.toString();

	public static final int[] SQL_INSERT_SUBMISSION_LOAN_PARAMETER_TYPES = {
			Types.INTEGER, Types.INTEGER, Types.CHAR, Types.DECIMAL,
			Types.DECIMAL, Types.CHAR, Types.CHAR, Types.CHAR, Types.VARCHAR,
			Types.DATE, Types.DATE, Types.DATE, Types.DATE, Types.DATE,
			Types.SMALLINT, Types.VARCHAR, Types.DECIMAL, Types.SMALLINT,
			Types.DECIMAL, Types.DECIMAL, Types.CHAR,
			Types.DECIMAL, Types.TIMESTAMP, Types.DECIMAL, Types.TIMESTAMP,
			Types.DECIMAL, Types.DECIMAL, Types.DECIMAL, Types.DECIMAL,
			Types.DECIMAL, Types.CHAR, Types.CHAR, Types.CHAR};

	public static final String SQL_DELETE_SUBMISSION = new StringBuffer()
			.append("delete from ").append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission where submission_id = ?").toString();

	public static final String SQL_DELETE_SUBMISSION_CASE = new StringBuffer()
			.append("delete from ").append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission_case where submission_id = ?").toString();

	public static final String SQL_DELETE_SUBMISSION_LOAN = new StringBuffer()
			.append("delete from ").append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission_loan where submission_id = ?").toString();

	public static final String SQL_UPDATE_SUBMISSION = new StringBuffer()
			.append("UPDATE ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"submission set ").append("SUBMISSION_TS = ?,").append(
					"LAST_UPDATED_USER_ID = ?,").append("LAST_UPDATED_TS = ? ")
			.append("WHERE submission_id = ?").toString();

	public static final int[] SQL_UDPATE_SUBMISSION_PARAMETER_TYPES = {
			Types.TIMESTAMP, Types.INTEGER, Types.TIMESTAMP, Types.INTEGER };

	public static final String SQL_SELECT_PROCESS_STATUS_CODE = new StringBuffer()
			.append("select PROCESS_STATUS_CODE from ").append(
					BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"SUBMISSION_CASE WHERE submission_id = ? FOR FETCH ONLY").toString();

	public static final String SQL_UPDATE_SUBMISSION_CASE = new StringBuffer()
			.append("update ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission_case ")
			.append(
					" set (PROCESS_STATUS_CODE, PROCESSED_TS, LAST_UPDATED_USER_ID, LAST_UPDATED_TS) =")
			.append(" (?, ?, ?, ?) where submission_id = ? and last_updated_ts <= ? ").toString();

	public static final String SQL_UPDATE_SUBMISSION_CASE_NO_STATUS_CHANGE = new StringBuffer()
			.append("update ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
					"submission_case ").append(
					" set (LAST_UPDATED_USER_ID, LAST_UPDATED_TS) =").append(
					" (?, ?) where submission_id = ? and last_updated_ts <= ? ").toString();

	public static final String SQL_UPDATE_SUBMISSION_LOAN = new StringBuffer()
			.append("update ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission_loan set (")
			.append("CONTRACT_ID,")
			.append("PROFILE_ID,")
			.append("PARTICIPANT_ID,")
			.append("LEGALLY_MARRIED_IND,")
			.append("LOAN_TYPE_CODE,")
			.append("LOAN_REASON_EXPLANATION,")
			.append("REQUEST_DATE,")
			.append("EXPIRATION_DATE,")
			.append("LOAN_EFFECTIVE_DATE,")
			.append("LOAN_MATURITY_DATE,")
			.append("FIRST_PAYROLL_DATE,")
			.append("MAX_AMORTIZATION_YEARS,")
			.append("DEFAULT_PROVISION,")
			.append("MAX_OS_LOAN_BAL_LAST12MTHS_AMT,")
			.append("OUTSTANDING_LOANS_COUNT,")
			.append("CURR_OUTSTANDING_LOAN_BAL_AMT,")
			.append("LAST_FEE_CHNG_BY_TPA_PROF_ID,")
			.append("LAST_FEE_CHNG_WAS_PS_USER_IND,")
			.append("LAST_UPDATED_USER_PROFILE_ID,")
			.append("LAST_UPDATED_TS,")
	        .append("LOAN_EXPENSE_MARGIN_PCT,")
            .append("MINIMUM_LOAN_AMT,")
            .append("MAXIMUM_LOAN_AMT,")
            .append("MAXIMUM_LOAN_PCT,")
            .append("LOAN_MONTHLY_FLAT_FEE_AMT,")
            .append("SPOUSAL_CONSENT_REQD_IND,")
            .append("AT_RISK_IND,")
            .append("APPLY_IRS_10K_DOLLAR_RULE_IND)")
			.append(
					" = (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) where submission_id = ?")
			.toString();

	public static final int[] SQL_UPDATE_SUBMISSION_LOAN_PARAMETER_TYPES = {
			Types.INTEGER, Types.DECIMAL, Types.DECIMAL, Types.CHAR,
			Types.CHAR, Types.VARCHAR, Types.DATE, Types.DATE, Types.DATE,
			Types.DATE, Types.DATE, Types.SMALLINT, Types.VARCHAR,
			Types.DECIMAL, Types.SMALLINT, Types.DECIMAL,
			Types.DECIMAL, Types.CHAR, Types.DECIMAL, Types.TIMESTAMP,
			Types.DECIMAL, Types.DECIMAL, Types.DECIMAL, Types.DECIMAL,
            Types.DECIMAL, Types.CHAR, Types.CHAR, Types.CHAR, Types.INTEGER };

	public static final String SQL_SELECT_LOAN = new StringBuffer()
			.append("select ")
			.append("SL.SUBMISSION_ID,")
			.append("SL.CONTRACT_ID,")
			.append("SL.SUBMISSION_CASE_TYPE_CODE,")
			.append("SL.PROFILE_ID,")
			.append("SL.PARTICIPANT_ID,")
			.append("SL.CREATED_BY_ROLE_CODE,")
			.append("SL.LEGALLY_MARRIED_IND,")
			.append("SL.LOAN_TYPE_CODE,")
			.append("SL.LOAN_REASON_EXPLANATION,")
			.append("SL.REQUEST_DATE,")
			.append("SL.EXPIRATION_DATE,")
			.append("SL.LOAN_EFFECTIVE_DATE,")
			.append("SL.LOAN_MATURITY_DATE,")
			.append("SL.FIRST_PAYROLL_DATE,")
			.append("SL.MAX_AMORTIZATION_YEARS,")
			.append("SL.DEFAULT_PROVISION,")
			.append("SL.MAX_OS_LOAN_BAL_LAST12MTHS_AMT,")
			.append("SL.OUTSTANDING_LOANS_COUNT,")
			.append("SL.CURR_OUTSTANDING_LOAN_BAL_AMT,")
			.append("SL.LAST_FEE_CHNG_BY_TPA_PROF_ID, ")
			.append("SL.LAST_FEE_CHNG_WAS_PS_USER_IND, ")
			.append("SL.CREATED_USER_PROFILE_ID,")
			.append("SL.CREATED_TS,")
			.append("SL.LOAN_EXPENSE_MARGIN_PCT,")
            .append("SL.MINIMUM_LOAN_AMT,")
            .append("SL.MAXIMUM_LOAN_AMT,")
            .append("SL.MAXIMUM_LOAN_PCT,")
            .append("SL.LOAN_MONTHLY_FLAT_FEE_AMT,")
            .append("SL.AT_RISK_IND,")
			.append("SL.LAST_UPDATED_USER_PROFILE_ID,")
			.append("SL.SPOUSAL_CONSENT_REQD_IND,")
			.append("SC.LAST_UPDATED_TS,")     // use SC's last updated ts because the expiration process only updates SC and not SL.
			.append("SC.PROCESS_STATUS_CODE, ")
			.append("SL.APPLY_IRS_10K_DOLLAR_RULE_IND ")
			.append("from ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append("submission_case SC ")
			.append("inner join ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME)
			.append(
					"submission_loan SL on sc.submission_id = sl.submission_id ")
			.toString();

	public static final String SQL_WHERE_SUBMISSION_CONTRACT_ID = new StringBuffer(
			" where SC.submission_id = ? and SC.contract_id = ?").toString();

	public static final String SQL_WHERE_SUBMISSION_ID = new StringBuffer(
			" where SC.submission_id = ?").toString();

	public static final String SQL_WHERE_CREATED_USER_PROFILE_ID_AND_CONTRACT_ID = new StringBuffer(
			" where SL.created_user_profile_id = ? and SL.contract_id = ? ")
			.toString();

	public static final String SQL_WHERE_PROFILE_ID_AND_CONTRACT_ID = new StringBuffer(
			" where SL.profile_id = ? and SL.contract_id = ? ").toString();

	public static final String SQL_PENDING_REQUEST = new StringBuffer(
			"select sc.submission_id from "
					+ DaoConstants.SchemaName.STP
					+ "submission_case sc, "
					+ DaoConstants.SchemaName.STP
					+ "submission_loan sl where sc.SUBMISSION_CASE_TYPE_CODE = '")
			.append(LoanConstants.SUBMISSION_CASE_TYPE_CODE)
			.append("' and sc.PROCESS_STATUS_CODE in ('")
			.append(LoanStateEnum.PENDING_REVIEW.getStatusCode())
			.append("', '")
			.append(LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode())
			.append("', '")
			.append(LoanStateEnum.PENDING_APPROVAL.getStatusCode())
			.append("', '")
			.append(LoanStateEnum.APPROVED.getStatusCode())
			.append("') and sl.PROFILE_ID = ? and sc.CONTRACT_ID = ? ")
			.append("and sc.submission_id = sl.submission_id ")
			.append("and sc.contract_id = sl.contract_id ")
			.append(
					"and sc.submission_case_type_code = sl.submission_case_type_code ")
			.toString();

	public static final String SQL_COUNT_DRAFT_REQUEST_BY_PARTICIPANT_AND_CREATED_USER = new StringBuffer(
			"select count(*) from "
					+ DaoConstants.SchemaName.STP
					+ "submission_case sc, "
					+ DaoConstants.SchemaName.STP
					+ "submission_loan sl where sc.SUBMISSION_CASE_TYPE_CODE = '")
			.append(LoanConstants.SUBMISSION_CASE_TYPE_CODE)
			.append("' and sc.PROCESS_STATUS_CODE in ('")
			.append(LoanStateEnum.DRAFT.getStatusCode())
			.append(
					"') and sl.PROFILE_ID = ? and sc.CREATED_USER_ID = ? and sc.CONTRACT_ID = ? ")
			.append("and sc.submission_id = sl.submission_id ")
			.append("and sc.contract_id = sl.contract_id ")
			.append(
					"and sc.submission_case_type_code = sl.submission_case_type_code ")
			.toString();

	/**
	 * SQL_SELECT_EXPIRING_REQUESTS Retrieves a list of requests to be marked as
	 * expired (i.e. Status Code = Draft or Pending and expiration date <
	 * current date
	 */
	public static final String SQL_SELECT_EXPIRING_REQUESTS = "select sc.submission_id, sc.process_status_code, sl.expiration_date, sc.contract_id "
			+ " from "
			+ BaseDatabaseDAO.STP_SCHEMA_NAME
			+ "submission_case sc, "
			+ BaseDatabaseDAO.STP_SCHEMA_NAME
			+ "submission_loan sl "
			+ " where "
			+ " sl.expiration_date < ? "
			+ " and sl.submission_id = sc.submission_id "
			+ " and sc.submission_case_type_code = '"
			+ LoanConstants.SUBMISSION_CASE_TYPE_CODE
			+ "' and sc.process_status_code in('"
			+ LoanStateEnum.DRAFT.getStatusCode()
			+ "', '"
			+ LoanStateEnum.PENDING_APPROVAL.getStatusCode()
			+ "', '"
			+ LoanStateEnum.PENDING_REVIEW.getStatusCode()
			+ "', '"
			+ LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode()
			+ "') "
			+ " for read only";

	/**
	 * SQL_SELECT_EXPIRING_REQUESTS Retrieves a list of requests to be marked as
	 * expired (i.e. Status Code = Draft or Pending and expiration date <
	 * current date
	 */
	public static final String SQL_SELECT_ABOUT_TO_EXPIRE_REQUESTS = "select sc.submission_id, sc.process_status_code, sl.expiration_date, sc.contract_id, sl.profile_id, sl.created_by_role_code "
			+ " from "
			+ BaseDatabaseDAO.STP_SCHEMA_NAME
			+ "submission_case sc, "
			+ BaseDatabaseDAO.STP_SCHEMA_NAME
			+ "submission_loan sl "
			+ " where "
			+ " sl.expiration_date = ? "
			+ " and sl.submission_id = sc.submission_id "
			+ " and sc.process_status_code in('"
			+ LoanStateEnum.DRAFT.getStatusCode()
			+ "', '"
			+ LoanStateEnum.PENDING_APPROVAL.getStatusCode()
			+ "', '"
			+ LoanStateEnum.PENDING_REVIEW.getStatusCode()
			+ "', '"
			+ LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode()
			+ "') "
			+ " for read only";
	/**
	 * SQL_FETCH_UOL_COUNT retrieves the number of record for the given contract id 
	 * which is having contract service feature code as "UOL"
	 */
	public static final String SQL_FETCH_UOL_COUNT =	 
		"select count(*) from " +
		 SchemaName.CUSTOMER_SERVICE +
		 " CONTRACT_SERVICE_FEATURE " +
		 " where contract_id = ? and SERVICE_FEATURE_CODE='UOL'";
}
