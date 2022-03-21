package com.manulife.pension.service.withdrawal.dao;

import static java.sql.Types.INTEGER;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.dao.ActivityDetailDao;
import com.manulife.pension.service.distribution.dao.ActivityDynamicDetailDao;
import com.manulife.pension.service.distribution.dao.ActivitySummaryDao;
import com.manulife.pension.service.distribution.dao.DeclarationDao;
import com.manulife.pension.service.distribution.dao.DistributionDao;
import com.manulife.pension.service.distribution.dao.FeeDao;
import com.manulife.pension.service.distribution.dao.NoteDao;
import com.manulife.pension.service.distribution.dao.RecipientDao;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Declaration;
import com.manulife.pension.service.distribution.valueobject.Fee;
import com.manulife.pension.service.distribution.valueobject.Note;
import com.manulife.pension.service.distribution.valueobject.Payee;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.withdrawal.common.WithdrawalDataManager;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.util.WithdrawalHelper;
import com.manulife.pension.service.withdrawal.valueobject.Address;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantCategory;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantFlag;
import com.manulife.pension.service.withdrawal.valueobject.PayeePaymentInstruction;
import com.manulife.pension.service.withdrawal.valueobject.PendingReviewApproveWithdrawalCount;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestFee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestRecipient;


/**
 * @author Dennis
 * 
 */
public class WithdrawalDao extends DistributionDao {
    private static final String CLASS_NAME = WithdrawalDao.class.getName();

    private static final Logger logger = Logger.getLogger(WithdrawalDao.class);
    
    private static final String OK = "OK";
    private static final String TRADITIONAL_IRA = "{\"Roth_IRA\":\"N\",\"Non_Taxable\":\"Y\",\"Taxable\":\"Y\",\"Roth_Non_Tax\":\"Y\",\"Roth_Taxable\":\"Y\"}";
    private static final String ROTH_IRA = "{\"Roth_IRA\":\"Y\",\"Non_Taxable\":\"Y\",\"Taxable\":\"Y\",\"Roth_Non_Tax\":\"Y\",\"Roth_Taxable\":\"Y\"}";

    private static final String PENDING_REVIEW_WITHDRAWAL_PROCESS_CODE = WithdrawalStateEnum.PENDING_REVIEW
            .getStatusCode();

    private static final String PENDING_APPROVAL_WITHDRAWAL_PROCESS_CODE = WithdrawalStateEnum.PENDING_APPROVAL
            .getStatusCode();

    private static final String SUBMISSION_SEQUENCE = new StringBuffer()
            .append(JOURNAL_SCHEMA_NAME).append("submission_id").toString();

    private static final int[] SQL_INSERT_SUBMISSION_PARAMETER_TYPES = { INTEGER, Types.TIMESTAMP,
            INTEGER, INTEGER, Types.TIMESTAMP, INTEGER, Types.TIMESTAMP };

    private static final int[] SQL_INSERT_SUBMISSION_CASE_PARAMETER_TYPES = {};

    private static final int[] SQL_INSERT_SUBMISSION_WITHDRAWAL_PARAMETER_TYPES = { Types.INTEGER,
            Types.INTEGER, Types.CHAR, Types.DECIMAL, Types.DECIMAL, Types.CHAR, Types.DATE,
            Types.CHAR, Types.CHAR, Types.VARCHAR, Types.CHAR, Types.CHAR, Types.DATE, Types.DATE,
            Types.DATE, Types.DATE, Types.DATE, Types.CHAR, Types.DECIMAL, Types.CHAR, Types.CHAR,
            Types.CHAR, Types.CHAR, Types.TIMESTAMP, Types.VARCHAR, Types.CHAR, Types.CHAR,
            // Types.DATE, Types.CHAR, Types.DECIMAL, Types.DECIMAL, Types.DECIMAL, Types.CHAR,
            Types.DATE, Types.CHAR, Types.DECIMAL, Types.CHAR, Types.INTEGER, Types.TIMESTAMP,
            Types.SMALLINT, Types.TIMESTAMP, Types.CHAR, Types.CHAR };

    private static final int[] SQL_UPDATE_SUBMISSION_PARAMETER_TYPES = {};

    private static final int[] SQL_UPDATE_SUBMISSION_CASE_PARAMETER_TYPES = { Types.CHAR,
            Types.VARCHAR, Types.INTEGER };

    private static final int[] SQL_UPDATE_SUBMISSION_WITHDRAWAL_PARAMETER_TYPES = { Types.CHAR,
            Types.DATE, Types.CHAR, Types.CHAR, Types.CHAR, Types.VARCHAR, Types.CHAR, Types.CHAR, Types.DATE,
            Types.DATE, Types.DATE, Types.DATE, Types.DATE, Types.CHAR, Types.DECIMAL, Types.CHAR,
            Types.CHAR, Types.CHAR, Types.CHAR, Types.TIMESTAMP, Types.VARCHAR, Types.CHAR,
            // Types.CHAR, Types.DATE, Types.CHAR, Types.DECIMAL, Types.INTEGER, Types.DECIMAL,
            Types.CHAR, Types.DATE, Types.CHAR, Types.DECIMAL, Types.CHAR, Types.INTEGER,
            Types.SMALLINT };

    private static final String SQL_INSERT_SUBMISSION = new StringBuffer().append("INSERT into  ")
            .append(STP_SCHEMA_NAME).append("submission (").append("SUBMISSION_ID,").append(
                    "SUBMISSION_TS,").append("FILE_NAME,").append("MAP_NAME,").append(
                    "INPUT_LOCATION_NAME,").append("USER_ID,").append("CREATED_USER_ID,").append(
                    "CREATED_TS,").append("LAST_UPDATED_USER_ID,").append("LAST_UPDATED_TS,")
            .append("PAYMENT_INFO_ONLY_IND").append(" ) values (?, ?, '', '', '', ?, ?, ").append(
                    "?, ?, ?, 'N')").toString();

    private static final String SQL_INSERT_SUBMISSION_CASE = new StringBuffer().append(
            "INSERT into ").append(STP_SCHEMA_NAME + "submission_case (").append("SUBMISSION_ID,")
            .append("CONTRACT_ID,").append("SUBMISSION_CASE_TYPE_CODE,")
            .append("SYNTAX_ERROR_IND,").append("PROCESSED_TS,").append("PROCESS_STATUS_CODE,")
            .append("CREATED_USER_ID,").append("CREATED_TS,").append("LAST_UPDATED_USER_ID,")
            .append("LAST_UPDATED_TS,").append("LAST_LOCKED_BY_USER_ID,").append("LAST_LOCKED_TS,")
            .append("SUBMIT_COUNT").append(" ) values (?, ?, 'W', 'N' ,?, ? , ?, ").append(
                    "?, ?, ?, null, null, 1)").toString();

    private static final String SQL_INSERT_SUBMISSION_WITHDRAWAL = new StringBuffer()
            .append("insert into ")
            .append(STP_SCHEMA_NAME)
            .append("SUBMISSION_WITHDRAWAL ( ")
            .append("SUBMISSION_ID,")
            .append("CONTRACT_ID,")
            .append("SUBMISSION_CASE_TYPE_CODE,")
            .append("CREATED_BY_ROLE_CODE,")
            .append("PROFILE_ID,")
            .append("PARTICIPANT_ID,")
            .append("RESIDENCE_STATE_CODE,")
            .append("BIRTH_DATE,")
            .append("WITHDRAWAL_REASON_CODE,")
            .append("WITHDRAWAL_REASON_DETAIL_CODE,")
            .append("WITHDRAWAL_REASON_EXPLANATION,")
            .append("PARTICIPANT_LEAVING_PLAN_IND,")
            .append("PAYMENT_TO_CODE,")
            .append("REQUEST_DATE,")
            .append("EXPIRATION_DATE,")
            .append("EVENT_DATE,")
            .append("LAST_CONTRIB_APPLIC_DATE,")
            .append("LATEST_PROCESSED_CONTRIB_DATE,")
            .append("WITHDRAWAL_AMOUNT_TYPE_CODE,")
            .append("WITHDRAWAL_AMOUNT,")
            .append("UNVESTED_AMOUNT_OPTION_CODE,")
            .append("IRS_DIST_CODE_LOAN_CLOSURE,")
            .append("LOAN_OPTION_CODE,")
            .append("IRA_SERVICE_PROVIDER_CODE,")
            .append("APPROVED_TS,")
            .append("LOAN_1099R_NAME,")
            .append("VESTING_CALLED_IND,")
            .append("VESTING_OVERWRITE_IND,")
            .append("EFFECTIVE_DATE,")
            .append("PART_WITH_PBA_MONEY_IND,")
            // .append("CONTENT_KEY,")
            // .append("CONTENT_VERSION_NO,")
            .append("LAST_FEE_CHNG_BY_TPA_USER_ID,")
            .append("LAST_FEE_CHNG_WAS_PS_USER_IND,")
            .append("CREATED_USER_PROFILE_ID,")
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")
            .append("LAST_UPDATED_TS,")
            .append("AT_RISK_IND,")
            .append("SPOUSAL_CONSENT_REQD_IND")
            .append(
            // ")values (?,?,'W',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,")
                    ")values (?,?,'W',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? )")
            .toString();

    private static final String SQL_UPDATE_SUBMISSION = new StringBuffer().append("UPDATE ")
            .append(STP_SCHEMA_NAME).append("submission set ").append(
                    "SUBMISSION_TS = CURRENT TIMESTAMP,").append("LAST_UPDATED_USER_ID = ?,")
            .append("LAST_UPDATED_TS = CURRENT TIMESTAMP ").append("WHERE submission_id = ?")
            .toString();

    private static final String SQL_UPDATE_SUBMISSION_CASE = ""
            + " UPDATE STP100.SUBMISSION_CASE SC \n"
            + " SET    (SC.PROCESS_STATUS_CODE,SC.PROCESSED_TS,SC.LAST_UPDATED_USER_ID, \n"
            + "         SC.LAST_UPDATED_TS) = (SELECT STATUS_CODE AS PROCESS_STATUS_CODE, \n"
            + "                                       CASE  \n"
            + "                                         WHEN SC2.PROCESS_STATUS_CODE = STATUS_CODE THEN SC2.PROCESSED_TS \n"
            + "                                         ELSE CURRENT TIMESTAMP \n"
            + "                                       END AS PROCESSED_TS, \n"
            + "                                       USER_ID, \n"
            + "                                       CURRENT TIMESTAMP \n"
            + "                                FROM   STP100.SUBMISSION_CASE SC2, \n"
            + "                                       (SELECT CAST(? AS CHAR(2)) AS STATUS_CODE, \n"
            + "                                               CAST(? AS VARCHAR(9)) AS USER_ID \n"
            + "                                        FROM   SYSIBM.SYSDUMMY1) STUFF \n"
            + "                                WHERE  SC2.SUBMISSION_ID = SC.SUBMISSION_ID) \n"
            + " WHERE  SC.SUBMISSION_ID = ? \n";

    private static final String SQL_UPDATE_SUBMISSION_WITHDRAWAL = new StringBuffer().append(
            "UPDATE ").append(STP_SCHEMA_NAME).append("SUBMISSION_WITHDRAWAL set ").append(
            "RESIDENCE_STATE_CODE = ?,").append("BIRTH_DATE = ?,").append("SPOUSAL_CONSENT_REQD_IND = ?,").append(
            "WITHDRAWAL_REASON_CODE = ?,").append("WITHDRAWAL_REASON_DETAIL_CODE = ?,").append(
            "WITHDRAWAL_REASON_EXPLANATION = ?,").append("PARTICIPANT_LEAVING_PLAN_IND = ?,")
            .append("PAYMENT_TO_CODE = ?,").append("REQUEST_DATE = ?,").append(
                    "EXPIRATION_DATE = ?,").append("EVENT_DATE = ?,").append(
                    "LAST_CONTRIB_APPLIC_DATE = ?,").append("LATEST_PROCESSED_CONTRIB_DATE = ?,")
            .append("WITHDRAWAL_AMOUNT_TYPE_CODE = ?,").append("WITHDRAWAL_AMOUNT = ?,").append(
                    "UNVESTED_AMOUNT_OPTION_CODE = ?,").append("IRS_DIST_CODE_LOAN_CLOSURE = ?,")
            .append("LOAN_OPTION_CODE = ?,").append("IRA_SERVICE_PROVIDER_CODE = ?,").append(
                    "APPROVED_TS = ?,").append("LOAN_1099R_NAME = ?,").append(
                    "VESTING_CALLED_IND = ?,").append("VESTING_OVERWRITE_IND = ?,").append(
                    "EFFECTIVE_DATE = ?,").append("PART_WITH_PBA_MONEY_IND = ?,").append(
            // "CONTENT_KEY = ?,").append("CONTENT_VERSION_NO = ?,").append(
                    "LAST_FEE_CHNG_BY_TPA_USER_ID = ?,").append(
                    "LAST_FEE_CHNG_WAS_PS_USER_IND = ?,").append(
                    "LAST_UPDATED_USER_PROFILE_ID = ?,").append(
                    "LAST_UPDATED_TS = CURRENT TIMESTAMP ").append("WHERE submission_id = ?")
            .toString();

    private static final String SQL_CALL_SELECT_WITHDRAWAL_INFO = new StringBuffer().append(
            "CALL STP100.SELECT_SUBMISSION_WITHDRAWAL_INFO(?)").toString();

    private static final String SQL_DELETE_SUBMISSION = new StringBuffer().append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME).append("submission where submission_id = ?")
            .toString();

    private static final String SQL_DELETE_SUBMISSION_CASE = new StringBuffer().append(
            "delete from ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
            "submission_case where submission_id = ?").toString();

    private static final String SQL_DELETE_SUBMISSION_WITHDRAWAL = new StringBuffer().append(
            "delete from ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
            "submission_withdrawal where submission_id = ?").toString();

    private static final String SQL_DELETE_WITHDRAWAL_LEGALESE = new StringBuffer().append(
            "delete from ").append(BaseDatabaseDAO.STP_SCHEMA_NAME).append(
            "withdrawal_legalese where submission_id = ?").toString();

    private static final String SQL_GET_WITHDRAWAL_REQUESTS = "select sw.submission_id referenceNumber, "
            + "sc.process_status_code status, "
            + "sw.last_updated_user_profile_id lastUpdatedUserProfileId, "
            + "sw.last_updated_ts lastUpdatedTimestamp,"
            + "sw.created_ts createdTimestamp,"
            + "SW.EFFECTIVE_DATE "
            + "from "
            + STP_SCHEMA_NAME
            + "submission_withdrawal sw "
            + "left outer join "
            + STP_SCHEMA_NAME
            + "submission_case sc "
            + "on sw.submission_id = sc.submission_id "
            + "and sw.contract_id = sc.contract_id "
            + "and sw.submission_case_type_code = sc.submission_case_type_code "
            + "where "
            + "sw.contract_id = ? and " + "sw.profile_id = ? ";

    private static final String SQL_PENDING_REVIEW_APPROVE_REQUEST_COUNT = "select count (*) numberPending "
            + "from "
            + STP_SCHEMA_NAME
            + "submission_case sc "
            + "where "
            + "sc.contract_id = ? and "
            + "sc.process_status_code = ? and "
            + "sc.submission_case_type_code = 'W'";
    
    private static final String SQL_SELECT_IRS_DIST_CODE_LOAN_CLOSURE = "select sw.irs_dist_code_loan_closure "
            + "from " + STP_SCHEMA_NAME + "submission_withdrawal sw " + "where " + "sw.submission_id = ? ";
    
    private static final String SQL_SELECT_WITHDRAWAL_FEES = "select f.fee_type_code, f.fee_value "
            + "from " + STP_SCHEMA_NAME + "fee f " + "where " + "f.submission_id = ? ";

    // Withdrawal Request expiration SQL statements
    /**
     * SQL_SELECT_EXPIRING_REQUESTS Retrieves a list of requests to be marked as expired (i.e.
     * Status Code = Draft or Pending and expiration date < current date
     */
    private static final String SQL_SELECT_EXPIRING_REQUESTS = "select sc.submission_id, sc.process_status_code, wd.expiration_date "
            + " from "
            + STP_SCHEMA_NAME
            + "submission_case sc, "
            + STP_SCHEMA_NAME
            + "submission_withdrawal wd "
            + " where "
            + " wd.expiration_date < ? "
            + " and wd.submission_id = sc.submission_id "
            + " and sc.process_status_code in('"
            + WithdrawalStateEnum.DRAFT.getStatusCode()
            + "', '"
            + WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode()
            + "', '"
            + WithdrawalStateEnum.PENDING_REVIEW.getStatusCode() + "') " + " for read only";

    /**
     * Logs the Submission status change in the Activity History table (summary)
     */
    private static final String SQL_LOG_ACTIVITY_SUMMARY = "insert into " + STP_SCHEMA_NAME
            + "activity_summary (" + " submission_id " + ", status_code "
            + ", created_user_profile_id " + ", created_ts "
            + ") values(?, ?, ?, CURRENT TIMESTAMP) ";

    /**
     * gets the legalese content that was agreed upon by the participant on Approval of the
     * Withdrawal Request
     * 
     */

    private static final String SQL_LEGALESE_CONTENT_AGREED = "Select "
            + " L.legalese_text "
            + " from "
            + STP_SCHEMA_NAME
            + "legalese L, "
            + STP_SCHEMA_NAME
            // + "submission_withdrawal SW " + " where " + " SW.submission_id = ? "
            // + " and SW.content_key = L.content_key "
            // + " and SW.content_version_no = L.content_version_no " + " and L.location_code = ? "
            + "withdrawal_legalese W " + " where " + " W.submission_id = ? "
            + " and W.content_key = L.content_key "
            + " and W.content_version_no = L.content_version_no "
            + " and w.cma_site_code = L.cma_site_code " + " and W.cma_site_code = ? "
            + " for read only";

    /**
     * Retrieves the IDs of all Withdrawal records in a non-terminal status.
     * 
     * NOTE: The second parameter cannot be bound! Use string replacement operations to set the SET
     * of status codes.
     */
    private static final String SQL_SELECT_REQUESTS_BY_CONTRACT_AND_STATUS = "select sc.submission_id "
            + " from "
            + STP_SCHEMA_NAME
            + "submission_case sc, "
            + STP_SCHEMA_NAME
            + "submission_withdrawal wd"
            + " where "
            + " sc.contract_id = ? "
            + " and sc.submission_id = wd.submission_id"
            + " and wd.created_by_role_code = ? "
            + " and sc.submission_case_type_code = 'W' "
            + " and sc.process_status_code in (?) "
            + " for read only";

    private static final String SQL_SELECT_REQUESTS_BY_CONTRACT_ONLY = "select sc.submission_id "
            + " from " + STP_SCHEMA_NAME + "submission_case sc, " + STP_SCHEMA_NAME
            + "submission_withdrawal wd" + " where " + " sc.contract_id = ? "
            + " and sc.submission_id = wd.submission_id"
            + " and sc.submission_case_type_code = 'W' " + " and sc.process_status_code in (?) "
            + " for read only";

    /**
     * Sets the Expiration date for a given submission ID
     */
    private static final String SQL_SET_EXPIRATION_DATE = "update " + STP_SCHEMA_NAME
            + "    submission_withdrawal " + " set expiration_date = ? "
            + "    , last_updated_user_profile_id  = ? "
            + "    , last_updated_ts = CURRENT TIMESTAMP " + " where submission_id = ?";

    private static final String SQL_SELECT_WITHDRAWAL_REQUEST_METADATA = ""
            + "SELECT SC.SUBMISSION_ID       AS SUBMISSIONID, "
            + "       SC.CONTRACT_ID         AS CONTRACTID, "
            + "       SW.PROFILE_ID          AS PROFILEID, "
            + "       SW.EXPIRATION_DATE     AS EXPIRATIONDATE, "
            + "       SC.PROCESS_STATUS_CODE AS STATUSCODE, "
            + "       SW.PARTICIPANT_ID      AS PARTICIPANTID, " + "       CASE "
            + "         WHEN SW.CREATED_BY_ROLE_CODE = 'PA' THEN 'Participant' "
            + "         ELSE 'Non-Participant' " + "       END AS INITIATEDBY "
            + "FROM   STP100.SUBMISSION_CASE SC, " + "       STP100.SUBMISSION_WITHDRAWAL SW "
            + "WHERE  SC.SUBMISSION_ID = ? " + "       AND SC.SUBMISSION_CASE_TYPE_CODE = 'W' "
            + "       AND SC.SUBMISSION_ID = SW.SUBMISSION_ID ";

    private static final Class<WithdrawalRequestMetaData> SQL_SELECT_WITHDRAWAL_REQUEST_METADATA_CLASS = WithdrawalRequestMetaData.class;

	private static final String SQL_SELECT_SPOUSAL_CONSENT_REQUIRED_IND = "SELECT spousal_consent_reqd_ind"
			+ " FROM "
			+CUSTOMER_SCHEMA_NAME +"plan"
			+ " WHERE plan_id = ?";

	private static final String SQL_PARTICIPANT_EXCEPTION_FLAG_CATEGORY_LIST = "select CATEGORY_CODE,RESTRICTED_CATEGORY,CATEGORY_CODE_DESCRIPTION from EZK100C.PARTICIPANT_EXCEPTION_FLAG_CATEGORY";
	private static final String SQL_PARTICIPANT_EXCEPTION_FLAG_DETAILS = "select PROFILE_ID,EXCEPTION_FLAG,SIL_REQUEST_NO,CREATED_USER_ID,EXCEPTION_CATEGORY_CODE,CREATED_TS FROM EZK100C.PARTICIPANT_EXCEPTION_FLAG WHERE PROFILE_ID  = ? AND CONTRACT_ID = ? ORDER BY CREATED_TS DESC LIMIT 1";
  
	private static final String SQL_SAVE_PARTICIPANT_EXCEPTION_FLAG_DETAILS = "INSERT INTO EZK100C.PARTICIPANT_EXCEPTION_FLAG(PROFILE_ID,CONTRACT_ID,EXCEPTION_FLAG,EXCEPTION_CATEGORY_CODE,SIL_REQUEST_NO,CREATED_USER_ID,CREATED_TS) values(?,?,?,?,?,?,CURRENT_TIMESTAMP)";
	private static final int[] SQL_SAVE_PARTICIPANT_EXCEPTION_FLAG_PARAMETER_TYPES = { Types.VARCHAR, Types.INTEGER ,Types.CHAR, Types.SMALLINT, Types.BIGINT,Types.VARCHAR,Types.TIMESTAMP };
	
	
	/**
     * insert - insert a new withdrawal request.
     * 
     * @param contractId The current contract id
     * @param userProfileId The user profile id
     * @param req - the Withdrawal Request
     * @param timestamp - The timestamp to use for creation timestamps.
     * @return The submission Id of the new submission
     * @throws DistributionServiceException thrown if an error is encountered
     */
    public Integer insert(final Integer contractId, final Integer userProfileId,
            final WithdrawalRequest req, final Timestamp timestamp)
            throws DistributionServiceException {

        if (contractId == null || userProfileId == null || req == null) {
            throw new WithdrawalDaoException(
                    "WithdrawalDao.insert failed.  One of contractid userprofile id"
                            + "or req is null");
        }

        List<Object> params;
        Integer submissionId = null;
        // create a submission
        try {
            submissionId = new Integer(BaseDatabaseDAO.getNextSequenceValue(
                    BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SUBMISSION_SEQUENCE).intValue());

            params = new ArrayList<Object>();
            params.add(submissionId);
            params.add(timestamp);
            params.add(userProfileId);
            params.add(userProfileId);
            params.add(timestamp);
            params.add(userProfileId);
            params.add(timestamp);

            new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION,
                    SQL_INSERT_SUBMISSION_PARAMETER_TYPES).insert(params.toArray());
        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "insert",
                    "Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION
                            + " for contract ID " + contractId + " and newSubmissionId "
                            + submissionId);
        }

        // create a submission case
        try {
            params = new ArrayList<Object>();
            params.add(submissionId);
            params.add(contractId);
            params.add(timestamp);
            params.add(req.getStatusCode());
            params.add(userProfileId);
            params.add(timestamp);
            params.add(userProfileId);
            params.add(timestamp);

            new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION_CASE)
                    .insert(params.toArray());
        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "insert",
                    "Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION_CASE
                            + " for contract ID " + contractId + " and newSubmissionId "
                            + submissionId);
        }

        try {

            params = new ArrayList<Object>();
            params.add(submissionId);
            params.add(req.getContractId());
            params.add(req.getUserRoleCode());
            params.add(req.getEmployeeProfileId());
            params.add(req.getParticipantId());
            params.add(req.getParticipantStateOfResidence());
            params.add(req.getBirthDate());
            params.add(req.getReasonCode());
            params.add(req.getReasonDetailCode());
            params.add(req.getReasonDescription());
            params.add(req.getParticipantLeavingPlanInd());
            params.add(req.getPaymentTo());
            params.add(req.getRequestDate());
            params.add(req.getExpirationDate());
            if (StringUtils.isNotEmpty(req.getReasonCode())) {
                String withdrawalType = req.getReasonCode();
                if (WithdrawalReason.isTermination(withdrawalType)) {
                    params.add(req.getTerminationDate());
                } else if (withdrawalType.equals(WithdrawalReason.DISABILITY)) {
                    params.add(req.getDisabilityDate());
                } else if (withdrawalType.equals(WithdrawalReason.DEATH)) {
                    params.add(req.getDeathDate());
                } else if (withdrawalType.equals(WithdrawalReason.RETIREMENT)) {
                    params.add(req.getRetirementDate());
                } else {
                    params.add(null);
                }
            } else {
                params.add(null);
            }
            params.add(req.getFinalContributionDate());
            params.add(req.getMostRecentPriorContributionDate());
            params.add(req.getAmountTypeCode());
            params.add(req.getWithdrawalAmount());
            params.add(req.getUnvestedAmountOptionCode());
            params.add(req.getIrsDistributionCodeLoanClosure());
            params.add(req.getLoanOption());
            params.add(req.getIraServiceProviderCode());
            params.add(req.getApprovedTimestamp());
            params.add(req.getLoan1099RName());
            params.add(req.getVestingCalledInd());
            params.add(req.getVestingOverwriteInd());
            params.add(req.getExpectedProcessingDate());
            params.add(req.getPartWithPbaMoneyInd());
            /*
             * if (null != req.getLegaleseInfo()) {
             * params.add(req.getLegaleseInfo().getContentId());
             * params.add(req.getLegaleseInfo().getContentVersionNumber()); } else {
             * params.add(null); params.add(null); }
             */
            params.add(req.getLastFeeChangeByTPAUserID());
            params.add(req.isLastFeeChangeWasPSUserInd());
            params.add(userProfileId);
            params.add(timestamp);
            params.add(userProfileId);
            params.add(timestamp);
            params.add(req.getRequestRiskIndicator());
            params.add(req.getLegallyMarriedInd());

            new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION_WITHDRAWAL,
                    SQL_INSERT_SUBMISSION_WITHDRAWAL_PARAMETER_TYPES).insert(params.toArray());
        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "insert",
                    "Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION_WITHDRAWAL
                            + " for contract ID " + contractId + " and newSubmissionId "
                            + submissionId);

        }
        new WithdrawalMoneyTypeDao().insert(submissionId, contractId, userProfileId, req
                .getMoneyTypes());
        new WithdrawalLoanDao().insert(submissionId, contractId, userProfileId, req.getLoans());
        Class<WithdrawalRequestFee> type = WithdrawalRequestFee.class;
        new FeeDao().insertUpdatePrune(submissionId, contractId, userProfileId, req.getFees(),
                WithdrawalRequestFee.class);

        final NoteDao noteDao = new NoteDao();
        if (!req.getCurrentAdminToParticipantNote().isBlank()) {
            noteDao.insert(submissionId, contractId, userProfileId, req
                    .getCurrentAdminToParticipantNote());
        } // fi
        if (!req.getCurrentAdminToAdminNote().isBlank()) {
            noteDao.insert(submissionId, contractId, userProfileId, req
                    .getCurrentAdminToAdminNote());
        } // fi

        new DeclarationDao().insert(submissionId, contractId, userProfileId, req.getDeclarations());
        new RecipientDao().insert(submissionId, contractId, userProfileId, req.getRecipients());

        if (req.getParticipantLegaleseInfo() != null) {
            WithdrawalLegaleseDao withdrawalLegaleseDao = new WithdrawalLegaleseDao();
            withdrawalLegaleseDao.insertWithdrawalLegaleseInfo(submissionId, req);
        }

        return submissionId;
    }

    /**
     * Update - Typical Update routing for updating a withdrawal request.
     * @param contractId
     * @param submissionId
     * @param userProfileId
     * @param withdrawalRequest
     * @throws DistributionServiceException
     */
    public void update(final Integer contractId, final Integer submissionId,
            final Integer userProfileId, final WithdrawalRequest withdrawalRequest)
            throws DistributionServiceException {
    	update(contractId, submissionId, userProfileId, withdrawalRequest, false);
    }
    
    /**
     * Update - Update a withdrawal request.
     *        - Includes parameter for a special BGA case where we want to update
     *          only the withdrawal status without affecting any other data.
     *          Overloaded from previous update method.
     * 
     * @param contractId The current contract id
     * @param submissionId The submission id to update
     * @param userProfileId The user profile id
     * @param withdrawalRequest - the Withdrawal Request
     * @param statusUpdateOnly - If true, does minimal updates to change the status of the withdrawal.
     *                           this is used as a special case for BGA where a withdrawal must move
     *                           from Pending Approval back to Pending Review without altering any
     *                           withdrawal data other than the submission status and user/TS.
     * @throws DistributionServiceException thrown if an error is encountered
     */
    public void update(final Integer contractId, final Integer submissionId,
            final Integer userProfileId, final WithdrawalRequest withdrawalRequest, boolean statusUpdateOnly)
            throws DistributionServiceException {
    
        if (submissionId == null) {
            throw new WithdrawalDaoException(
                    "WithdrawalDAO:update failed.  no submission Id on request");
        }

        if (contractId == null || userProfileId == null || submissionId == null
                || withdrawalRequest == null) {
            throw new WithdrawalDaoException("WithdrawalDao.insert failed.  One of contractid ["
                    + contractId + "], submissionid[" + submissionId + "],  userprofile id ["
                    + userProfileId + "]" + " or withdrawalRequest was null");
        }

        // create a submission
        List<Object> params = null;
        DeclarationDao declarationDao = new DeclarationDao();
        RecipientDao recipientDao = new RecipientDao();

        // update submission
        try {
            params = new ArrayList<Object>();
            params.add(userProfileId);
            params.add(submissionId);

            new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_UPDATE_SUBMISSION).update(params
                    .toArray());
        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "saveWithdrawalRequest",
                    "Problem occurred in prepared call: " + SQL_UPDATE_SUBMISSION
                            + " for contract ID " + contractId + " and newSubmissionId "
                            + submissionId);
        }

        // update submission case
        try {
            params = new ArrayList<Object>();
            params.add(withdrawalRequest.getStatusCode());
            params.add(userProfileId);
            params.add(submissionId);
            new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_UPDATE_SUBMISSION_CASE,
                    SQL_UPDATE_SUBMISSION_CASE_PARAMETER_TYPES).update(params.toArray());
        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "saveWithdrawalRequest",
                    "Problem occurred in prepared call: " + SQL_UPDATE_SUBMISSION_CASE
                            + " for contract ID " + contractId + " and newSubmissionId "
                            + submissionId);
        } // end try/catch

        if (!statusUpdateOnly) {
	        // update submission
	        try {
	
	            params = new ArrayList<Object>();
	            params.add(withdrawalRequest.getParticipantStateOfResidence());
	            params.add(withdrawalRequest.getBirthDate());
	            params.add(withdrawalRequest.getLegallyMarriedInd());
	            params.add(withdrawalRequest.getReasonCode());
	            params.add(withdrawalRequest.getReasonDetailCode());
	            params.add(withdrawalRequest.getReasonDescription());
	            params.add(withdrawalRequest.getParticipantLeavingPlanInd());
	            params.add(withdrawalRequest.getPaymentTo());
	            params.add(withdrawalRequest.getRequestDate());
	            params.add(withdrawalRequest.getExpirationDate());
	            if (StringUtils.isNotEmpty(withdrawalRequest.getReasonCode())) {
	                String withdrawalType = withdrawalRequest.getReasonCode();
	                if (WithdrawalReason.isTermination(withdrawalType)) {
	                    params.add(withdrawalRequest.getTerminationDate());
	                } else if (withdrawalType.equals(WithdrawalReason.DISABILITY)) {
	                    params.add(withdrawalRequest.getDisabilityDate());
	                } else if (withdrawalType.equals(WithdrawalReason.RETIREMENT)) {
	                    params.add(withdrawalRequest.getRetirementDate());
	                } else if (withdrawalType.equals(WithdrawalReason.DEATH)) {
	                    params.add(withdrawalRequest.getDeathDate());
	                } else {
	                    params.add(null);
	                } // fi
	            } else {
	                params.add(null);
	            } // fi
	            params.add(withdrawalRequest.getFinalContributionDate());
	            params.add(withdrawalRequest.getMostRecentPriorContributionDate());
	            params.add(withdrawalRequest.getAmountTypeCode());
	            params.add(withdrawalRequest.getWithdrawalAmount());
	            params.add(withdrawalRequest.getUnvestedAmountOptionCode());
	            params.add(withdrawalRequest.getIrsDistributionCodeLoanClosure());
	            params.add(withdrawalRequest.getLoanOption());
	            params.add(withdrawalRequest.getIraServiceProviderCode());
	            params.add(withdrawalRequest.getApprovedTimestamp());
	            params.add(withdrawalRequest.getLoan1099RName());
	            params.add(withdrawalRequest.getVestingCalledInd());
	            params.add(withdrawalRequest.getVestingOverwriteInd());
	            params.add(withdrawalRequest.getExpectedProcessingDate());
	            params.add(withdrawalRequest.getPartWithPbaMoneyInd());
	            /*
	             * if (withdrawalRequest.getLegaleseInfo() != null) {
	             * params.add(withdrawalRequest.getLegaleseInfo().getContentId());
	             * params.add(withdrawalRequest.getLegaleseInfo().getContentVersionNumber()); } else {
	             * params.add(null); params.add(null); } // fi
	             */
	            params.add(withdrawalRequest.getLastFeeChangeByTPAUserID());
	            params.add(withdrawalRequest.isLastFeeChangeWasPSUserInd());
	            params.add(userProfileId);
	            params.add(submissionId);
	
	            new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_UPDATE_SUBMISSION_WITHDRAWAL,
	                    SQL_UPDATE_SUBMISSION_WITHDRAWAL_PARAMETER_TYPES).update(params.toArray());
	        } catch (DAOException e) {
	            throw new WithdrawalDaoException(e, CLASS_NAME, "saveWithdrawalRequest",
	                    "Problem occurred in prepared call: " + SQL_UPDATE_SUBMISSION_WITHDRAWAL
	                            + " for contract ID " + contractId + " and newSubmissionId "
	                            + submissionId);
	        } // end try/catch
	
	        new WithdrawalMoneyTypeDao().insertUpdate(submissionId, contractId, userProfileId,
	                withdrawalRequest.getMoneyTypes());
	        new WithdrawalLoanDao().insertUpdatePrune(submissionId, contractId, userProfileId,
	                withdrawalRequest.getLoans());
	        new FeeDao().insertUpdatePrune(submissionId, contractId, userProfileId, withdrawalRequest
	                .getFees(), WithdrawalRequestFee.class);
	
	        NoteDao noteDao = new NoteDao();
	        if (withdrawalRequest.isRemoveAllNotesOnSave()) {
	            noteDao.deleteAll(submissionId, contractId, userProfileId);
	        } // fi
	        if (!withdrawalRequest.getCurrentAdminToParticipantNote().isBlank()) {
	            noteDao.insert(submissionId, contractId, userProfileId, withdrawalRequest
	                    .getCurrentAdminToParticipantNote());
	        } // fi
	        if (!withdrawalRequest.getCurrentAdminToAdminNote().isBlank()) {
	            noteDao.insert(submissionId, contractId, userProfileId, withdrawalRequest
	                    .getCurrentAdminToAdminNote());
	        } // fi
	
	        declarationDao.deleteAll(submissionId, contractId, userProfileId);
	        declarationDao.insert(submissionId, contractId, userProfileId, withdrawalRequest
	                .getDeclarations());
	
	        recipientDao.deleteAll(submissionId, contractId, userProfileId);
	        recipientDao.insert(submissionId, contractId, userProfileId, withdrawalRequest
	                .getRecipients());
        }
    }
    
    /**
     * Returns a withdrawal request value object based on the submissino id.
     * 
     * @param submissionId the id of the withdrawal request
     * @return the value object
     * @throws DistributionServiceException thrown if a system exception occurs
     */
    public WithdrawalRequest read(final Integer submissionId) throws DistributionServiceException {

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        WithdrawalRequest wr = new WithdrawalRequest();
        Collection<WithdrawalRequestMoneyType> moneyTypes = new ArrayList<WithdrawalRequestMoneyType>();
        Collection<WithdrawalRequestLoan> loanDetails = new ArrayList<WithdrawalRequestLoan>();
        Collection<Fee> feeDetails = new ArrayList<Fee>();
        Collection<Note> noteDetails = new ArrayList<Note>();
        Collection<Declaration> declarationDetails = new ArrayList<Declaration>();
        Map<Integer, WithdrawalRequestRecipient> recipientDetails = new HashMap<Integer, WithdrawalRequestRecipient>();
        Map<Integer, List<Payee>> recipientToPayee = new HashMap<Integer, List<Payee>>();
        wr.setMoneyTypes(moneyTypes);
        wr.setLoans(loanDetails);
        wr.setFees(feeDetails);
        wr.setNotes(noteDetails);
        wr.setDeclarations(declarationDetails);
        Date tempDate = null;

        int i = 0;

        try {

        	conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
        	stmt = conn.prepareStatement(SQL_CALL_SELECT_WITHDRAWAL_INFO);

        	stmt.setInt(1, submissionId.intValue());

        	stmt.execute(); // submission detail result set

        	rs = stmt.getResultSet();

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException("missing result set for submission case detail");
        	}
        	rs = stmt.getResultSet(); // submission case detail

        	if (rs != null) {

        		if (rs.next()) {
        			wr.setStatusCode(rs.getString("PROCESS_STATUS_CODE"));
        			wr.setRequestType(rs.getString("SUBMISSION_CASE_TYPE_CODE"));
        			wr.setSubmissionCaseLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));
        		}

        	}

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException("missing result set for withdrawal money type");
        	}
        	rs = stmt.getResultSet(); // withdrawal money type

        	if (rs != null) {

        		while (rs.next()) {
        			i = 1;
        			WithdrawalRequestMoneyType mt = new WithdrawalRequestMoneyType();
        			mt.setSubmissionId(rs.getInt(i++));
        			mt.setMoneyTypeId(rs.getString(i++));
        			mt.setTotalBalance(rs.getBigDecimal(i++));
        			mt.setVestingPercentage(rs.getBigDecimal(i++));
        			mt.setVestingPercentageUpdateable(isTrue(rs.getString(i++)));
        			mt.setWithdrawalAmount(rs.getBigDecimal(i++));
        			mt.setWithdrawalPercentage(rs.getBigDecimal(i++));
        			mt.setCreatedById(rs.getInt(i++));
        			mt.setCreated(rs.getTimestamp(i++));
        			mt.setLastUpdatedById(rs.getInt(i++));
        			mt.setLastUpdated(rs.getTimestamp(i++));
        			mt.setAvailableHarshipAmount(rs.getBigDecimal(i++));
        			moneyTypes.add(mt);
        		}

        	}

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException("missing result set for withdrawal loan detail");
        	}
        	rs = stmt.getResultSet(); // withdrawal loan detail

        	if (rs != null) {

        		while (rs.next()) {
        			i = 1;
        			WithdrawalRequestLoan loan = new WithdrawalRequestLoan();
        			loan.setSubmissionId(rs.getInt(i++));
        			loan.setLoanNo(new Integer(rs.getInt(i++)));
        			loan.setOutstandingLoanAmount(rs.getBigDecimal(i++));
        			loan.setCreatedById(rs.getInt(i++));
        			loan.setCreated(rs.getTimestamp(i++));
        			loan.setLastUpdatedById(rs.getInt(i++));
        			loan.setLastUpdated(rs.getTimestamp(i++));
        			loanDetails.add(loan);
        		}

        	}

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException("missing result set for withdrawal fee detail");
        	}
        	rs = stmt.getResultSet(); // withdrawal fee detail

        	if (rs != null) {

        		while (rs.next()) {
        			i = 1;
        			WithdrawalRequestFee fee = new WithdrawalRequestFee();
        			fee.setSubmissionId(rs.getInt(i++));
        			fee.setTypeCode(rs.getString(i++));
        			fee.setValue(rs.getBigDecimal(i++));
        			fee.setCreatedById(rs.getInt(i++));
        			fee.setCreated(rs.getTimestamp(i++));
        			fee.setLastUpdatedById(rs.getInt(i++));
        			fee.setLastUpdated(rs.getTimestamp(i++));
        			feeDetails.add(fee);
        		}

        	}

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException("missing result set for withdrawal note detail");
        	}
        	rs = stmt.getResultSet(); // withdrawal note detail

        	if (rs != null) {

        		while (rs.next()) {
        			i = 1;
        			WithdrawalRequestNote note = new WithdrawalRequestNote();
        			note.setSubmissionId(rs.getInt(i++));
        			note.setCreated(rs.getTimestamp(i++));
        			note.setNoteTypeCode(rs.getString(i++));
        			note.setNote(rs.getString(i++));
        			note.setCreatedById(rs.getInt(i++));
        			noteDetails.add(note);
        		}

        	}

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException(
        				"missing result set for withdrawal declaration detail");
        	}
        	rs = stmt.getResultSet(); // withdrawal declaration detail

        	if (rs != null) {

        		while (rs.next()) {
        			i = 1;
        			WithdrawalRequestDeclaration dec = new WithdrawalRequestDeclaration();
        			dec.setSubmissionId(rs.getInt(i++));
        			dec.setTypeCode(rs.getString(i++));
        			dec.setCreatedById(rs.getInt(i++));
        			dec.setCreated(rs.getTimestamp(i++));
        			declarationDetails.add(dec);
        		}

        	}

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException("missing result set for withdrawal detail");
        	}
        	rs = stmt.getResultSet(); // withdrawal detail

        	if (rs != null && rs.next()) {
        		// Repalced the i++ notations with Column names to ensure that no Null Pointers are
        		// thrown.
        		// Pin Exposure Ph2 has added AT_RISK_IND to the resultset. The result set had
        		// certain
        		// added to it but we are not accessing it and it was causing a problem when trying
        		// to access
        		// AT_RISK_IND using the i++ notation.

        		wr.setSubmissionId(rs.getInt("SUBMISSION_ID"));
        		wr.setContractId(new Integer(rs.getInt("CONTRACT_ID")));
        		wr.setUserRoleCode(rs.getString("CREATED_BY_ROLE_CODE"));
        		if (StringUtils.equals(wr.getUserRoleCode(),
        				WithdrawalRequest.USER_ROLE_PARTICIPANT_CODE)) {
        			wr.setIsParticipantCreated(true);
        		}
        		wr.setEmployeeProfileId(new Integer(rs.getInt("PROFILE_ID")));
        		wr.setParticipantId(new Integer(rs.getInt("PARTICIPANT_ID")));
        		wr.setParticipantStateOfResidence(rs.getString("RESIDENCE_STATE_CODE"));
        		tempDate = rs.getDate("BIRTH_DATE");
        		if (tempDate != null) {
        			wr.setBirthDate(new java.util.Date(tempDate.getTime()));
        		}
        		wr.setReasonCode(rs.getString("WITHDRAWAL_REASON_CODE"));
        		wr.setReasonDetailCode(rs.getString("WITHDRAWAL_REASON_DETAIL_CODE"));
        		wr.setReasonDescription(rs.getString("WITHDRAWAL_REASON_EXPLANATION"));
        		wr
        		.setParticipantLeavingPlanInd(isTrue(rs
        				.getString("PARTICIPANT_LEAVING_PLAN_IND")));
        		wr.setPaymentTo(rs.getString("PAYMENT_TO_CODE").trim());
        		tempDate = rs.getDate("REQUEST_DATE");
        		if (tempDate != null) {
        			wr.setRequestDate(new java.util.Date(tempDate.getTime()));
        		}
        		tempDate = rs.getDate("EXPIRATION_DATE");
        		if (tempDate != null) {
        			wr.setExpirationDate(new java.util.Date(tempDate.getTime()));
        		}
        		tempDate = rs.getDate("EVENT_DATE");
        		if (StringUtils.isNotEmpty(wr.getReasonCode())) {
        			String withdrawalType = wr.getReasonCode();
        			if (tempDate != null) {
        				java.util.Date eventDate = new java.util.Date(tempDate.getTime());
        				if (WithdrawalReason.isTermination(withdrawalType)) {
        					wr.setTerminationDate(eventDate);
        				} else if (withdrawalType.equals(WithdrawalReason.DISABILITY)) {
        					wr.setDisabilityDate(eventDate);
        				} else if (withdrawalType.equals(WithdrawalReason.DEATH)) {
        					wr.setDeathDate(eventDate);
        				} else if (withdrawalType.equals(WithdrawalReason.RETIREMENT)) {
        					wr.setRetirementDate(eventDate);
        				}
        			}
        		}

        		tempDate = rs.getDate("LAST_CONTRIB_APPLIC_DATE");
        		if (tempDate != null) {
        			wr.setFinalContributionDate(new java.util.Date(tempDate.getTime()));
        		}
        		tempDate = rs.getDate("LATEST_PROCESSED_CONTRIB_DATE");
        		if (tempDate != null) {
        			wr.setMostRecentPriorContributionDate(new java.util.Date(tempDate.getTime()));
        		}
        		wr.setAmountTypeCode(rs.getString("WITHDRAWAL_AMOUNT_TYPE_CODE"));
        		wr.setWithdrawalAmount(rs.getBigDecimal("WITHDRAWAL_AMOUNT"));
        		wr.setUnvestedAmountOptionCode(rs.getString("UNVESTED_AMOUNT_OPTION_CODE"));
        		// If the CHAR field is blank, set it to null.
        		final String irsDistributionCodeLoanClosure = rs
        		.getString("IRS_DIST_CODE_LOAN_CLOSURE");
        		if (StringUtils.isNotBlank(irsDistributionCodeLoanClosure)) {
        			/*if(irsDistributionCodeLoanClosure.trim().equalsIgnoreCase("1")) {
        				wr.setIrsDistributionCodeLoanClosure("1M");        				
        			}else if(irsDistributionCodeLoanClosure.trim().equalsIgnoreCase("2")) {
        				wr.setIrsDistributionCodeLoanClosure("2M");        				
        			}else if(irsDistributionCodeLoanClosure.trim().equalsIgnoreCase("7")) {
        				wr.setIrsDistributionCodeLoanClosure("7M");        				
        			}else {*/
        			wr.setIrsDistributionCodeLoanClosure(irsDistributionCodeLoanClosure.trim());
        			//}
        		} else {
        			wr.setIrsDistributionCodeLoanClosure(StringUtils.EMPTY);
        		} // fi
        		// If the CHAR field is blank, set it to null.
        		final String loanOption = rs.getString("LOAN_OPTION_CODE");
        		if (StringUtils.isNotBlank(loanOption)) {
        			wr.setLoanOption(loanOption);
        		} else {
        			wr.setLoanOption(StringUtils.EMPTY);
        		} // fi
        		wr.setIraServiceProviderCode(rs.getString("IRA_SERVICE_PROVIDER_CODE"));
        		wr.setApprovedTimestamp(rs.getTimestamp("APPROVED_TS"));
        		wr.setLoan1099RName(rs.getString("LOAN_1099R_NAME"));
        		wr.setVestingCalledInd(isTrue(rs.getString("VESTING_CALLED_IND")));
        		wr.setVestingOverwriteInd(isTrue(rs.getString("VESTING_OVERWRITE_IND")));
        		tempDate = rs.getDate("EFFECTIVE_DATE");
        		if (tempDate != null) {
        			wr.setExpectedProcessingDate(new java.util.Date(tempDate.getTime()));
        		}
        		wr.setPartWithPbaMoneyInd(isTrue(rs.getString("PART_WITH_PBA_MONEY_IND")));
        		/*
        		 * Integer contentKey = rs.getInt(i++); int contentVersionNo = rs.getInt(i++);
        		 * LegaleseInfo legalese = new LegaleseInfo(""); legalese.setContentId(contentKey);
        		 * legalese.setContentVersionNumber(contentVersionNo); wr.setLegaleseInfo(legalese);
        		 */
        		wr.setLastFeeChangeByTPAUserID(rs.getBigDecimal("LAST_FEE_CHNG_BY_TPA_USER_ID"));
        		wr.setLastFeeChangeWasPSUserInd(isTrue(rs
        				.getString("LAST_FEE_CHNG_WAS_PS_USER_IND")));
        		wr.setCreatedById(rs.getInt("CREATED_USER_PROFILE_ID"));
        		wr.setCreated(rs.getTimestamp("CREATED_TS"));
        		wr.setLastUpdatedById(rs.getInt("LAST_UPDATED_USER_PROFILE_ID"));
        		wr.setLastUpdated(rs.getTimestamp("LAST_UPDATED_TS"));
        		wr.setRequestRiskIndicator(isTrue(rs.getString("AT_RISK_IND")));
        		wr.setLegallyMarriedInd(rs.getString("SPOUSAL_CONSENT_REQD_IND"));
        	} else {
        		throw new WithdrawalDaoException("No rows found for submissionid:" + submissionId);
        	}

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException(
        				"missing result set for withdrawal recipient detail");
        	}
        	rs = stmt.getResultSet(); // withdrawal recipient detail

        	if (rs != null) {

        		while (rs.next()) {
        			i = 1;
        			WithdrawalRequestRecipient rec = new WithdrawalRequestRecipient();
        			rec.setSubmissionId(rs.getInt(i++));
        			rec.setRecipientNo(rs.getInt(i++));
        			rec.setFirstName(rs.getString(i++));
        			rec.setLastName(rs.getString(i++));
        			rec.setOrganizationName(rs.getString(i++));
        			rec.setUsCitizenInd(isTrue(rs.getString(i++)));
        			rec.setStateOfResidenceCode(rs.getString(i++));
        			rec.setShareTypeCode(rs.getString(i++));
        			rec.setShareValue(rs.getBigDecimal(i++));
        			rec.setFederalTaxPercent(rs.getBigDecimal(i++));
        			rec.setStateTaxPercent(rs.getBigDecimal(i++));
        			rec.setStateTaxTypeCode(rs.getString(i++));
        			rec.setTaxpayerIdentTypeCode(rs.getString(i++));
        			rec.setTaxpayerIdentNo(rs.getString(i++));
        			rec.setCreatedById(rs.getInt(i++));
        			rec.setCreated(rs.getTimestamp(i++));
        			rec.setLastUpdatedById(rs.getInt(i++));
        			rec.setLastUpdated(rs.getTimestamp(i++));

        			// address portion
        			Address addr = new Address();
        			i++; // occurrence number
        			String distributionCode = rs.getString(i++);
        			int recipientNo = rs.getInt(i++);
        			int payeeNo = rs.getInt(i++);

        			addr.setAddressLine1(rs.getString(i++));
        			addr.setAddressLine2(rs.getString(i++));
        			addr.setCity(rs.getString(i++));
        			addr.setStateCode(rs.getString(i++));
        			addr.setZipCode(StringUtils.trim(rs.getString(i++)));
        			addr.setCountryCode(rs.getString(i++));
        			if (!addr.isBlank()) {
        				addr.setSubmissionId(submissionId);
        				addr.setDistributionTypeCode(distributionCode);
        				addr.setRecipientNo(recipientNo);
        				addr.setPayeeNo(payeeNo);

        				addr.setCreatedById(rs.getInt(i++));
        				addr.setCreated(rs.getTimestamp(i++));
        				addr.setLastUpdatedById(rs.getInt(i++));
        				addr.setLastUpdated(rs.getTimestamp(i++));

        				rec.setAddress(addr);
        			}

        			recipientDetails.put(new Integer(rec.getRecipientNo()), rec);
        		}

        	}

        	// need to use arraylist since hashmap.values is not serializeable
        	wr.setRecipients(new ArrayList<Recipient>(recipientDetails.values()));

        	if (!stmt.getMoreResults() && (stmt.getUpdateCount() == -1)) {
        		throw new WithdrawalDaoException("missing result set for withdrawal payee detail");
        	}
        	rs = stmt.getResultSet(); // withdrawal payee detail

        	if (rs != null) {

        		while (rs.next()) {
        			i = 1;
        			WithdrawalRequestPayee payee = new WithdrawalRequestPayee();
        			PayeePaymentInstruction pi = new PayeePaymentInstruction();
        			payee.setPaymentInstruction(pi);

        			payee.setSubmissionId(rs.getInt(i++));
        			payee.setRecipientNo(rs.getInt(i++));
        			payee.setPayeeNo(rs.getInt(i++));
        			payee.setFirstName(rs.getString(i++));
        			payee.setLastName(rs.getString(i++));
        			payee.setOrganizationName(rs.getString(i++));
        			payee.setTypeCode(rs.getString(i++));
        			payee.setReasonCode(rs.getString(i++));
        			payee.setPaymentMethodCode(rs.getString(i++));
        			payee.setShareTypeCode(rs.getString(i++));
        			payee.setShareValue(rs.getBigDecimal(i++));
        			payee.setRolloverAccountNo(rs.getString(i++));
        			payee.setRolloverPlanName(rs.getString(i++));
        			payee.setMailCheckToAddress(isTrue(rs.getString(i++)));
        			payee.setSendCheckByCourier(isTrue(rs.getString(i++)));
        			payee.setCourierCompanyCode(rs.getString(i++));
        			payee.setCourierNo(rs.getString(i++));
        			payee.setIrsDistCode(rs.getString(i++));
        			payee.setCreatedById(rs.getInt(i++));
        			payee.setCreated(rs.getTimestamp(i++));
        			payee.setLastUpdatedById(rs.getInt(i++));
        			payee.setLastUpdated(rs.getTimestamp(i++));
        			payee.setTaxes(rs.getString(i++));
        			payee.setParticipant(rs.getString(i++));
        			Address addr = new Address();
        			i++; // occurrence number
        			String distributionCode = rs.getString(i++);
        			int recipientNo = rs.getInt(i++);
        			int payeeNo = rs.getInt(i++);

        			addr.setAddressLine1(rs.getString(i++));
        			addr.setAddressLine2(rs.getString(i++));
        			addr.setCity(rs.getString(i++));
        			addr.setStateCode(rs.getString(i++));
        			addr.setZipCode(StringUtils.trim(rs.getString(i++)));
        			addr.setCountryCode(rs.getString(i++));
        			if (!addr.isBlank()) {
        				addr.setSubmissionId(submissionId);
        				addr.setDistributionTypeCode(distributionCode);
        				addr.setRecipientNo(recipientNo);
        				addr.setPayeeNo(payeeNo);

        				addr.setCreatedById(rs.getInt(i++));
        				addr.setCreated(rs.getTimestamp(i++));
        				addr.setLastUpdatedById(rs.getInt(i++));
        				addr.setLastUpdated(rs.getTimestamp(i++));

        				payee.setAddress(addr);
        				
        				// Security Enhancements 
        				payee.setDefaultAddress(addr);

        			} else {
        				i += 4;
        			}

        			pi.setSubmissionId(submissionId);
        			pi.setRecipientNo(rs.getInt(i++));
        			pi.setPayeeNo(rs.getInt(i++));
        			pi.setBankAccountTypeCode(rs.getString(i++));
        			pi.setBankTransitNumber(getInteger(rs, i++));
        			pi.setBankAccountNumber(rs.getString(i++));
        			pi.setBankName(rs.getString(i++));
        			pi.setCreditPartyName(rs.getString(i++));
        			pi.setCreatedById(rs.getInt(i++));
        			pi.setCreated(rs.getTimestamp(i++));
        			pi.setLastUpdatedById(rs.getInt(i++));
        			pi.setLastUpdated(rs.getTimestamp(i++));
        			pi.setPaymentAmount(rs.getBigDecimal(i++));
        			// now that we have all the payees,
        			// establish the relationship
        			Integer recNo = payee.getRecipientNo();
        			if (recipientToPayee.get(recNo) == null) {
        				recipientToPayee.put(recNo, new ArrayList<Payee>());
        			}
        			List<Payee> payees = recipientToPayee.get(recNo);
        			payees.add(payee);
        		}

        	}

        	for (Integer recNo : recipientToPayee.keySet()) {
        		recipientDetails.get(recNo).setPayees(recipientToPayee.get(recNo));
        	}

        } catch (SQLException sqlException) {
            throw new WithdrawalDaoException(sqlException, CLASS_NAME, "readWithdrawalRequest",
                    "Problem occurred in prepared call: " + SQL_CALL_SELECT_WITHDRAWAL_INFO
                            + " for submission Id " + submissionId);
        } catch (SystemException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "readWithdrawalRequest",
                    "Problem occurred in prepared call: " + SQL_CALL_SELECT_WITHDRAWAL_INFO
                            + " for submission Id " + submissionId);
        } finally {
            close(stmt, conn);
        }
        return wr;
    }

	

   /* private String getRolloverType(String rolloverType) {
    	if(StringUtils.isNotEmpty(rolloverType)) {
    		if(TRADITIONAL_IRA.equals(rolloverType)) {
    			return WithdrawalRequest.TRADTIONAL_IRA;	
    		}else if(ROTH_IRA.equals(rolloverType)){
    			return WithdrawalRequest.ROTH_IRA;
    		}
    	}
		return "";
	}*/

	/**
     * @param contractId the contractId
     * @param profileId the profileId
     * @return a list of WithdrawalRequest objects
     * @throws SystemException thrown if there is an underlying exception
     */
    public Collection<WithdrawalRequest> getWithdrawalRequests(final Integer profileId,
            final Integer contractId) throws SystemException {

        logger.debug("contractId -> " + contractId);
        logger.debug("profileId -> " + profileId);

        Collection<WithdrawalRequest> withdrawalRequests = new ArrayList<WithdrawalRequest>();
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement stmt = null;

        StringBuffer query = new StringBuffer(SQL_GET_WITHDRAWAL_REQUESTS);
        try {
            connection = getReadUncommittedConnection(CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_GET_WITHDRAWAL_REQUESTS Query: " + query);
            stmt = connection.prepareStatement(SQL_GET_WITHDRAWAL_REQUESTS);
            stmt.setInt(1, contractId);
            stmt.setInt(2, profileId);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String status = resultSet.getString(2);
                Integer lastUpdatedUserProfileId = resultSet.getInt("lastUpdatedUserProfileId");
                Timestamp lastUpdatedTimestamp = resultSet.getTimestamp("lastUpdatedTimestamp");
                Timestamp createdTimestamp = resultSet.getTimestamp("createdTimestamp");
                Date expectedDate = resultSet.getDate("EFFECTIVE_DATE");
                WithdrawalRequest aRequest = new WithdrawalRequest();
                aRequest.setEmployeeProfileId(profileId);
                aRequest.getContractInfo().setContractId(contractId);
                aRequest.setSubmissionId(resultSet.getInt("referenceNumber"));
                aRequest.setStatusCode(status);
                aRequest.setLastUpdatedById(lastUpdatedUserProfileId);
                aRequest.setLastUpdated(lastUpdatedTimestamp);
                aRequest.setCreated(createdTimestamp);
                aRequest.setExpectedProcessingDate(expectedDate);
                withdrawalRequests.add(aRequest);
            }
        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getWithdrawalRequests", "contractId: "
                    + contractId + " profileId: " + profileId);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("could not close connection in withdrawalDAO.getWithdrawalRequests()");
            }
        }
        return withdrawalRequests;
    }

    /**
     * Does a cascade delete of 1. submission 2. submission case 3. submission withdrawal 4. money
     * types 5. declarations 6. fees 7. loans 8. notes 9. recipients 10. payees 11. address 12.
     * payment instructions 13. withdrawal legalese
     * 
     * @param contractId The contract Id
     * @param submissionId The submission Id
     * @param userProfileId The userProfile Id
     * @throws DistributionServiceException thrown if there is an under lying excpetion.
     */
    public void delete(final Integer contractId, final Integer submissionId,
            final Integer userProfileId) throws DistributionServiceException {

        List<Object> params = new ArrayList<Object>();
        params.add(submissionId);
        try {
            new ActivitySummaryDao().deleteAll(submissionId, contractId, userProfileId);
            new ActivityDynamicDetailDao().deleteAll(submissionId, contractId, userProfileId);
            new ActivityDetailDao().deleteAll(submissionId, contractId, userProfileId);
            new WithdrawalMoneyTypeDao().deleteAll(submissionId, contractId, userProfileId);
            new DeclarationDao().deleteAll(submissionId, contractId, userProfileId);
            new FeeDao().deleteAll(submissionId, contractId, userProfileId);
            new WithdrawalLoanDao().deleteAll(submissionId, contractId, userProfileId);
            new NoteDao().deleteAll(submissionId, contractId, userProfileId);
            new RecipientDao().deleteAll(submissionId, contractId, userProfileId);

            new SQLDeleteHandler(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
                    SQL_DELETE_WITHDRAWAL_LEGALESE).delete(params.toArray());
            new SQLDeleteHandler(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
                    SQL_DELETE_SUBMISSION_WITHDRAWAL).delete(params.toArray());
            new SQLDeleteHandler(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME,
                    SQL_DELETE_SUBMISSION_CASE).delete(params.toArray());
            new SQLDeleteHandler(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_DELETE_SUBMISSION)
                    .delete(params.toArray());

        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "getNewSubmissionId",
                    "Problem occurred while deleting submission");
        }
    }

    /**
     * Returns the count for the pending review requests and pending approve requests wrapped in the
     * PendingReviewApproveWithdrawalCount object
     * 
     * It access the SUBMISSION_CASE table on the Submission database.
     * 
     * @param contractId The contract ID to use.
     * @return PendingReviewApproveWithdrawalCount - The count.
     * @throws SystemException If a system exception occurs.
     */
    public PendingReviewApproveWithdrawalCount getPendingReviewApproveCount(final Integer contractId)
            throws SystemException {
        logger.debug("contractId -> " + contractId);

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        StringBuffer query = new StringBuffer(SQL_PENDING_REVIEW_APPROVE_REQUEST_COUNT);

        PendingReviewApproveWithdrawalCount count = new PendingReviewApproveWithdrawalCount();

        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_PENDING_REVIEW_APPROVE_REQUEST_COUNT Query: " + query);
            stmt = conn.prepareStatement(SQL_PENDING_REVIEW_APPROVE_REQUEST_COUNT);
            stmt.setInt(1, contractId);
            stmt.setString(2, PENDING_APPROVAL_WITHDRAWAL_PROCESS_CODE);

            rs = stmt.executeQuery();
            while (rs.next()) {
                count.setCountPendingApproveRequests(rs.getInt("numberPending"));
            }
            rs.close();
            stmt.setString(2, PENDING_REVIEW_WITHDRAWAL_PROCESS_CODE);
            rs = stmt.executeQuery();
            while (rs.next()) {
                count.setCountPendingReviewRequests(rs.getInt("numberPending"));
            }
        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getPendingReviewApproveCount", "contractId: "
                    + contractId);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger
                        .error("could not close connection in withdrawalDAO.getPendingReviewApproveCount()");
            }
        }
        return count;
    }

    /**
     * Retrieve the list of Submission IDs to be marked as expired
     * 
     * @param checkDate The date (java.util.Date) used to check if records are expired
     * @return List of submission IDs to be marked as expired. If no requests the returned
     *         Collection is empty.
     */
    public Collection<Integer> getExpiringWithdrawals(java.util.Date checkDate)
            throws SystemException {
        logger.debug("checkDate -> "
                + java.text.SimpleDateFormat.getDateInstance().format(checkDate));

        Collection<Integer> expiringWithdrawals = new ArrayList<Integer>();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_SELECT_EXPIRING_REQUESTS Query: "
                    + SQL_SELECT_EXPIRING_REQUESTS);
            stmt = conn.prepareStatement(SQL_SELECT_EXPIRING_REQUESTS);
            stmt.setDate(1, new Date(checkDate.getTime()));
            rs = stmt.executeQuery();
            while (rs.next()) {
                expiringWithdrawals.add(rs.getInt("submission_id"));
            }

        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getExpiringWithdrawals", "checkDate: "
                    + java.text.SimpleDateFormat.getDateInstance().format(checkDate));
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger
                        .error("could not close connection in withdrawalDAO.getPendingReviewApproveCount()");
            }
        }
        return expiringWithdrawals;
    }

    /**
     * Marks a specific Submission IDs as expired.
     * 
     * @param submissionId ID of submission to be marked as expired.
     * @param userProfileId - User Profile ID of the user that changed the record (could be system).
     * 
     * @return boolean - True if one row was updated, false otherwise.
     * 
     * @throws SystemException - If an exception occurs.
     */
    public boolean expireWithdrawal(final Integer submissionId, final Integer userProfileId)
            throws SystemException {
        final Timestamp lastUpdatedTs = new Timestamp(new java.util.Date().getTime());
        final boolean submissionWasExpired = expireDistributionRequest(submissionId, userProfileId,
                WithdrawalStateEnum.EXPIRED.getStatusCode(), lastUpdatedTs);

        if (submissionWasExpired) {
            final WithdrawalRequestMetaData withdrawalRequestMetaData = WithdrawalDataManager
                    .getWithdrawalRequestMetaData(submissionId);

            WithdrawalHelper.fireWithdrawalExpiredEvent(withdrawalRequestMetaData, userProfileId);
        } // fi

        return submissionWasExpired;
    }

    /**
     * Logs the Submission status change in the Activity History table (summary)
     * 
     * @param submissionId - The ID of the submission being changed.
     * @param newStatusCode - New status of the submission
     * @param profileId - User Profile ID of the user that changed the record (could be system)
     */
    public boolean logActivitySummary(Integer submissionId, String newStatusCode, Integer profileId)
            throws SystemException {
        logger.debug("submissionId -> " + submissionId);
        logger.debug("newStatusCode -> " + newStatusCode);
        logger.debug("profileId -> " + profileId);

        Connection conn = null;
        PreparedStatement stmt = null;
        int rowCount = 0;

        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_LOG_ACTIVITY_SUMMARY Query: " + SQL_LOG_ACTIVITY_SUMMARY);
            stmt = conn.prepareStatement(SQL_LOG_ACTIVITY_SUMMARY);
            stmt.setInt(1, submissionId.intValue());
            stmt.setString(2, newStatusCode);
            stmt.setInt(3, profileId);

            rowCount = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SystemException(e, CLASS_NAME, "logActivitySummary", stmt + "(submissionId: "
                    + submissionId + "; newStatusCode -> " + newStatusCode + "; profileId -> "
                    + profileId + ")");
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger
                        .error("could not close connection or statement in withdrawalDAO.logActivitySummary()");
            }
        }

        return rowCount == 1;
    }

    /**
     * Returns the Legalese content agreed upon by the participant on Withdrawal Request Approval.
     * 
     */

    public String getAgreedLegaleseText(Integer submissionId, String cmaSiteCode)
            throws SystemException {
        String legaleseText = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        // Location location =
        // BrowseServiceHelper.convertManulifeCompanyIdToLocation(request.getParticipantInfo().getManulifeCompanyId());
        // String siteId = BrowseServiceHelper.getLocationCodeFromLocation(location);
        StringBuffer query = new StringBuffer(SQL_LEGALESE_CONTENT_AGREED);
        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_LEGALESE_CONTENT_AGREED Query: " + query);
            stmt = conn.prepareStatement(SQL_LEGALESE_CONTENT_AGREED);
            stmt.setInt(1, submissionId);
            stmt.setString(2, cmaSiteCode);

            rs = stmt.executeQuery();
            while (rs.next()) {
                legaleseText = rs.getString("LEGALESE_TEXT");
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getAgreedLegaleseText", "submission ID: "
                    + submissionId);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("could not close connection in withdrawalDao.getAgreedLegaleseText()");
            }
        }

        return legaleseText;
    }

    /**
     * Retrieve the list of Submission IDs having one of the given statuses for the given contract
     * 
     * @param contractId ID of the contract
     * @param
     * @return List of submission IDs in one of the given statuses.
     */
    public Collection<Integer> getWithdrawalsByContractAndStatus(Integer contractId,
            Collection<WithdrawalStateEnum> statusList, String userRoleCode)
            throws DistributionServiceException {

        logger.debug("contractId -> " + contractId);

        StringBuffer sbStatus = new StringBuffer("(");
        for (WithdrawalStateEnum status : statusList) {
            if (sbStatus.length() > 1) {
                sbStatus.append(",");
            }
            sbStatus.append("'" + status.getStatusCode() + "'");
        }
        sbStatus.append(")");
        logger.debug("status codes -> " + sbStatus.toString());

        Collection<Integer> withdrawalList = new ArrayList<Integer>();
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            String sql = null;
            // NOTE: instantiate the status code set according to the current status list
            if (StringUtils.equals(WithdrawalRequest.CMA_SITE_CODE_PSW, userRoleCode)) {
                sql = SQL_SELECT_REQUESTS_BY_CONTRACT_ONLY.replace("(?)", sbStatus.toString());
            } else {
                sql = SQL_SELECT_REQUESTS_BY_CONTRACT_AND_STATUS
                        .replace("(?)", sbStatus.toString());
            }

            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_SELECT_REQUESTS_BY_CONTRACT_AND_STATUS Query: " + sql);
            stmt = conn.prepareStatement(sql);

            stmt.setInt(1, contractId);
            if (StringUtils.equals(WithdrawalRequest.CMA_SITE_CODE_EZK, userRoleCode)) {
                stmt.setString(2, userRoleCode);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                withdrawalList.add(rs.getInt("submission_id"));
            }

        } catch (SQLException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "getExpiringWithdrawals",
                    "contractId: " + contractId + "; status codes: " + sbStatus.toString());
        } catch (SystemException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "getExpiringWithdrawals",
                    "contractId: " + contractId + "; status codes: " + sbStatus.toString());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger
                        .error("could not close connection in withdrawalDAO.getWithdrawalsWithStatus()");
            }
        }
        return withdrawalList;
    }

    /**
     * Sets a specific Submission ID's expiration date
     * 
     * @param submissionId ID of submission for which the expiration date is set
     * @param expirationDate New expiration date
     * @param profileId ID of the user that changed the record
     */
    public boolean setExpirationDate(Integer submissionId, Date expirationDate, Integer profileId)
            throws WithdrawalDaoException {
        logger.debug("submissionId -> " + submissionId);
        logger.debug("expirationDate -> "
                + SimpleDateFormat.getDateInstance().format(expirationDate));
        logger.debug("profileId -> " + profileId);

        Connection conn = null;
        PreparedStatement stmt = null;
        int rowCount = 0;

        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_SET_EXPIRATION_DATE Query: " + SQL_SET_EXPIRATION_DATE);
            stmt = conn.prepareStatement(SQL_SET_EXPIRATION_DATE);
            stmt.setDate(1, expirationDate);
            stmt.setInt(2, profileId);
            stmt.setInt(3, submissionId);
            rowCount = stmt.executeUpdate();

        } catch (SQLException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "setExpirationDate", "submissionId: "
                    + submissionId + "; expirationDate: "
                    + SimpleDateFormat.getDateInstance().format(expirationDate) + "profileId: "
                    + profileId);
        } catch (SystemException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "setExpirationDate", "submissionId: "
                    + submissionId + "; expirationDate: "
                    + SimpleDateFormat.getDateInstance().format(expirationDate) + "profileId: "
                    + profileId);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger
                        .error("could not close connection or statement in withdrawalDAO.setExpirationDate()");
            }
        }
        return rowCount == 1;
    }

    /**
     * This returns true if the provided value is 'y' or 'Y', and false otherwise.
     * 
     * @param stringValue The value to test.
     * @return Boolean - Returns true if the provided value is 'y' or 'Y', and false otherwise.
     */
    private Boolean isTrue(final String stringValue) {
        return stringValue == null ? null : new Boolean(stringValue.equalsIgnoreCase("y"));
    }

    /**
     * This returns TPA fees stored in the FEE table
     * 
     * @param submissionId
     * @return returns WithdrawalRequestFee or null
     * @throws SystemException
     */
    public WithdrawalRequestFee getFees(Integer submissionId) throws SystemException {
        WithdrawalRequestFee fees = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;

        // StringBuffer query = new StringBuffer(SQL_SELECT_WITHDRAWAL_FEES);
        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_SELECT_WITHDRAWAL_FEES Query: "
                    + SQL_SELECT_WITHDRAWAL_FEES);
            stmt = conn.prepareStatement(SQL_SELECT_WITHDRAWAL_FEES);
            stmt.setInt(1, submissionId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                fees = new WithdrawalRequestFee();
                fees.setTypeCode(rs.getString("FEE_TYPE_CODE"));
                fees.setValue(rs.getBigDecimal("FEE_VALUE"));
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getFees", "submission ID: " + submissionId);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("could not close connection in withdrawalDao.getFees()");
            }
        }

        return fees;
    }

    /**
     * This returns IrsDistCodeLoanClosure stored in the SUBMISSION_WITHDRAWAL table
     * 
     * @param submissionId
     * @return returns IrsDistCodeLoanClosure or null
     * @throws SystemException
     */
    public String getIrsDistCodeLoanClosure(Integer submissionId) throws SystemException {
    	String irsDistCodeLoanClosure = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
     
        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_SELECT_IRS_DIST_CODE_LOAN_CLOSURE Query: "
                    + SQL_SELECT_IRS_DIST_CODE_LOAN_CLOSURE);
            stmt = conn.prepareStatement(SQL_SELECT_IRS_DIST_CODE_LOAN_CLOSURE);
            stmt.setInt(1, submissionId);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	irsDistCodeLoanClosure = StringUtils.trimToEmpty(rs.getString("IRS_DIST_CODE_LOAN_CLOSURE"));            	
            	
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getIrsDistCodeLoanClosure", "submission ID: " + submissionId);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("could not close connection in withdrawalDao.getIrsDistCodeLoanClosure()");
            }
        }

        return irsDistCodeLoanClosure;
    }
    /**
     * Gets the meta data for the given submission ID.
     * 
     * @param submissionId - The ID of the submission to lookup.
     * @return {@link WithdrawalRequestMetaData} - The meta data for the given withdrawal, null if
     *         it's not found.
     * @throws DAOException If a DAO exception occurs.
     */
    public static WithdrawalRequestMetaData getWithdrawalRequestMetaData(final Integer submissionId)
            throws DAOException {

        final String sqlStatement = SQL_SELECT_WITHDRAWAL_REQUEST_METADATA;
        final Class<WithdrawalRequestMetaData> valueClass = SQL_SELECT_WITHDRAWAL_REQUEST_METADATA_CLASS;

        final SelectBeanQueryHandler selectBeanQueryHandler = new SelectBeanQueryHandler(
                SUBMISSION_DATA_SOURCE_NAME, sqlStatement, valueClass);

        Object object = selectBeanQueryHandler.select(new Object[] { submissionId });

        final WithdrawalRequestMetaData withdrawalRequestMetaData = (WithdrawalRequestMetaData) object;

        // Add in the activity summary history.
        final Integer contractId = withdrawalRequestMetaData.getContractId();
        final Integer userProfileId = null;

        final ActivitySummaryDao activitySummaryDao = new ActivitySummaryDao();
        final List summaries;
        try {
            summaries = activitySummaryDao.select(submissionId, contractId, userProfileId,
                    WithdrawalActivitySummary.class);
        } catch (DistributionServiceException distributionServiceException) {
            throw new RuntimeException(distributionServiceException);
        } // end try/catch
        final List<WithdrawalActivitySummary> withdrawalActivitySummaries = new ArrayList<WithdrawalActivitySummary>(
                summaries.size());

        for (Iterator iterator = summaries.iterator(); iterator.hasNext();) {
            WithdrawalActivitySummary withdrawalActivitySummary = (WithdrawalActivitySummary) iterator
                    .next();
            withdrawalActivitySummaries.add(withdrawalActivitySummary);
        } // end for

        withdrawalRequestMetaData.setWithdrawalActivitySummaries(withdrawalActivitySummaries);

        return withdrawalRequestMetaData;
    }
    
    public String getRequiresSpousalConsentForDistributions(Integer contractNumber) throws SystemException{
    	 Connection conn = null;
         ResultSet rs = null;
         PreparedStatement stmt = null;
         String spousalConsentRequired = null;
         try {
             conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
             logger.debug("Executing SQL_SELECT_SPOUSAL_CONSENT_ Query: "
                     + SQL_SELECT_SPOUSAL_CONSENT_REQUIRED_IND);
             stmt = conn.prepareStatement(SQL_SELECT_SPOUSAL_CONSENT_REQUIRED_IND);
             stmt.setInt(1, contractNumber);
             rs = stmt.executeQuery();
             while (rs.next()) {
            	 spousalConsentRequired= rs.getString("spousal_consent_reqd_ind");
             }
             rs.close();

         } catch (SQLException e) {
             throw new SystemException(e, CLASS_NAME, "getRequiresSpousalConsentForDistributions", "contractNumber: " +contractNumber);
         } finally {
             try {
                 if (rs != null) {
                     rs.close();
                 }
                 if (stmt != null) {
                     stmt.close();
                 }
                 if (conn != null) {
                     conn.close();
                 }
             } catch (SQLException e) {
                 logger.error("could not close connection in withdrawalDao.getRequiresSpousalConsentForDistributions()");
             }
         }

         return spousalConsentRequired;
	}

   

    public List<ParticipantCategory> getParticipantCategoryList()
            throws SystemException {
    	List<ParticipantCategory> participantCategoryList = new ArrayList();
    	ParticipantCategory participantCategory = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        StringBuffer query = new StringBuffer(SQL_LEGALESE_CONTENT_AGREED);
        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_PARTICIPANT_EXCEPTION_FLAG_CATEGORY_LIST Query: " + query);
            stmt = conn.prepareStatement(SQL_PARTICIPANT_EXCEPTION_FLAG_CATEGORY_LIST);

            rs = stmt.executeQuery();
            while (rs.next()) {
                participantCategory = new ParticipantCategory();
            	participantCategory.setCode(rs.getString("CATEGORY_CODE"));
            	participantCategory.setDescription(rs.getString("CATEGORY_CODE_DESCRIPTION"));
            	participantCategory.setRestrictedCategory(rs.getString("RESTRICTED_CATEGORY"));
            	participantCategoryList.add(participantCategory);
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getParticipantCategoryList", "ContractNumber: "
                    );
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("could not close connection in withdrawalDao.getParticipantCategoryList()");
            }
        }

        return participantCategoryList;
    }

   

    public ParticipantFlag getPartitcipantExceptionFlagDetials(String profileId , String contractNumber)
            throws SystemException {
    	ParticipantFlag participantFlag = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        StringBuffer query = new StringBuffer(SQL_LEGALESE_CONTENT_AGREED);
        try {
            conn = getDefaultConnection(WithdrawalDao.CLASS_NAME, SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing PARTICIPANT_EXCEPTION_FLAG_DETAILS Query: " + query);
            stmt = conn.prepareStatement(SQL_PARTICIPANT_EXCEPTION_FLAG_DETAILS);
            stmt.setString(1, profileId);
            stmt.setString(2, contractNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	participantFlag = new ParticipantFlag();
            	participantFlag.setProfileId(rs.getString("PROFILE_ID"));
            	participantFlag.setExceptionFlag(rs.getString("EXCEPTION_FLAG"));
            	participantFlag.setSilRefNum(rs.getString("SIL_REQUEST_NO"));
            	participantFlag.setLastUpdatedBy(rs.getString("CREATED_USER_ID"));
            	participantFlag.setLastUpdateDate(rs.getTimestamp("CREATED_TS"));
            	participantFlag.setExceptionCategoryCode(rs.getString("EXCEPTION_CATEGORY_CODE"));
            	
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getPartitcipantExceptionFlagDetials", "profileId: "
                    + profileId);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                logger.error("could not close connection in withdrawalDao.getPartitcipantExceptionFlagDetials()");
            }
        }

        return participantFlag;
    }
    
    /**
     * Save Participant flag info into EZK100.PARTICIPANT_EXCEPTION_FLAG
     * @param submissionId
     * @param withdrawalRequest
     * @throws DistributionServiceException
     */
    public void insertParticipantFlagInfo(ParticipantFlag participantFlag) throws DistributionServiceException {



        final SQLInsertHandler handler = new SQLInsertHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_SAVE_PARTICIPANT_EXCEPTION_FLAG_DETAILS,
                SQL_SAVE_PARTICIPANT_EXCEPTION_FLAG_PARAMETER_TYPES);

        
        final List<Object> params = new ArrayList<Object>();
        params.add(participantFlag.getProfileId());
        params.add(Integer.parseInt(participantFlag.getContractId()));
        params.add(participantFlag.getExceptionFlag());
        if(StringUtils.isNotEmpty(participantFlag.getExceptionCategoryCode())){
          params.add(Integer.parseInt(participantFlag.getExceptionCategoryCode()));
        }else {
        	  params.add(Integer.parseInt("0"));
        }
        if(StringUtils.isNotEmpty(participantFlag.getSilRefNum())) {
        	params.add(new Long(participantFlag.getSilRefNum().trim()));
        }else {
        	params.add(new Long(0));
        }
        params.add(participantFlag.getCreatedBy());


        try {
            handler.insert(params.toArray());
        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "insertParticipantFlagInfo",
                    "Problem occurred in prepared call: " + SQL_SAVE_PARTICIPANT_EXCEPTION_FLAG_DETAILS
                            + " for contract ID " + participantFlag.getContractId()
                            + " and ProfileId " + participantFlag.getProfileId()
                            + " and ExceptionCategoryCode " + participantFlag.getExceptionCategoryCode());
        }
    }
    
}
