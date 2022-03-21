package com.manulife.pension.ps.service.submission.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.DAOIntegrityException;
import com.intware.dao.jdbc.SQLDeleteHandler;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldMultiRowQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldQueryHandler;
import com.intware.dao.jdbc.SelectMultiValueQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.common.MoneyBean;
import com.manulife.pension.lp.model.gft.CashAccount;
import com.manulife.pension.lp.model.gft.DirectDebitAccount;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.lp.model.gft.PaymentAccount;
import com.manulife.pension.lp.model.gft.PaymentInstruction;
import com.manulife.pension.ps.service.domain.dao.DomainDAO;
import com.manulife.pension.ps.service.report.census.dao.CensusVestingDAO;
import com.manulife.pension.ps.service.report.participant.valueobject.LongTermPartTimeParticipant;
import com.manulife.pension.ps.service.report.participant.valueobject.VestingParticipant;
import com.manulife.pension.ps.service.report.submission.valueobject.LongTermPartTimeDetailsReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.VestingDetailsReportData;
import com.manulife.pension.ps.service.report.transaction.handler.MoneySourceDescription;
import com.manulife.pension.ps.service.submission.util.LongTermPartTimeSubmissionErrorUtil;
import com.manulife.pension.ps.service.submission.util.MoneySourceHelper;
import com.manulife.pension.ps.service.submission.util.StatusGroupHelper;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.ps.service.submission.util.VestingSubmissionErrorUtil;
import com.manulife.pension.ps.service.submission.valueobject.AddableParticipant;
import com.manulife.pension.ps.service.submission.valueobject.AddableParticipantList;
import com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.ContributionInfo;
import com.manulife.pension.ps.service.submission.valueobject.CopiedSubmissionHistoryItem;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.MoneyTypeHeader;
import com.manulife.pension.ps.service.submission.valueobject.ReportDataErrors;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionParticipant;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionPaymentItem;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.contract.dao.ContractServiceFeatureDAO;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.dao.ProfileDAO;
import com.manulife.pension.submission.SubmissionError;

/**
 * This is the DAO for Submission operations.
 *
 * @author Tony Tomasone
 * @version
 */
public class SubmissionDAO extends BaseDatabaseDAO {

	private static final String YES = "Y";
	private static final String NO = "N";
	private static final String EMPTY_STRING = "";

	private static final String className = SubmissionDAO.class.getName();

	private static final int DEFAULT_YEAR_INTERVAL = 2;

	private static final String EMPLOYEE_MONEY_TYPE_PREFIX = "EE";

	private static final String APPLICATION_CODE = "IC";

	private static final Integer ZERO_INTEGER = new Integer("0");
	private static final BigDecimal ZERO_SCALE_2 = BigDecimal.ZERO.setScale(2);

	private static final String ACTIVE_STATUS = "AC";

	private static final int DELTA_END_DAYS = 10;

	private static final String DRAFT_STATUS = "14";
	private static final String COPIED_STATUS = "97";
	private static final String FATAL_ERROR_STATUS = "04";
	private static final String REJECT_WITH_ERRORS_STATUS = "05";
	private static final String SUSPEND_WITH_WARNINGS_STATUS = "07";
	private static final String UPLOAD_TO_APOLLO_FAILED_STATUS = "09";

	private static final String MONEY_SOURCE_REGULAR = "REG";

	private static final String REGULAR_TRANSACTION_CODE = "505";

	private static final String ERROR_COND_STRING_OK = "OK";

	private static final String MANULIFE_COMPANY_ID = "019";

	private static final String USA_LOCATION_CODE = "U";
	private static final String NEW_YORK_LOCATION_CODE = NO;

	private static final String REGULAR_CONTRIBUTION = "Regular Contribution";

	private static final int DUPLICATE_KEY_EXCEPTION_SQLCODE = -803;

	public static final String COPY_SIZE_LIMIT_KEY = "copy.size.limit";
	protected static final String COPY_SIZE_LIMIT = System.getProperty(COPY_SIZE_LIMIT_KEY, "1500");

	private static final String INTERNAL_USER_TYPE_CODE = "IU";

	private static final String SUBMISSION_TYPE_LTPT = "Q";
	
	private static final Set PARTICIPANT_ADDABLE_STATES = new HashSet();
	static {
		PARTICIPANT_ADDABLE_STATES.add(DRAFT_STATUS);
		PARTICIPANT_ADDABLE_STATES.add(COPIED_STATUS);
		PARTICIPANT_ADDABLE_STATES.add(FATAL_ERROR_STATUS);
		PARTICIPANT_ADDABLE_STATES.add(REJECT_WITH_ERRORS_STATUS);
		PARTICIPANT_ADDABLE_STATES.add(SUSPEND_WITH_WARNINGS_STATUS);
		PARTICIPANT_ADDABLE_STATES.add(UPLOAD_TO_APOLLO_FAILED_STATUS);
	}

    private static final Map fieldToColumnMap = new HashMap();
    static {
        fieldToColumnMap.put("name", "LAST_NAME");
        fieldToColumnMap.put("ssn", "SOCIAL_SECURITY_NUMBER");
        fieldToColumnMap.put("empId", "EMPLOYEE_ID");
        fieldToColumnMap.put("sourceRecordNo", "SOURCE_RECORD_NO");
        fieldToColumnMap.put("sequenceNo", "SEQUENCE_NO");
    }

	private static final Logger logger = Logger.getLogger(SubmissionDAO.class);

	private static final String SQL_HISTORY_SELECT = "select submission_id submissionId, "
			+ "contract_id contractId, "
			+ "user_type submitterType, "
			+ "user_id submitterID, "
			+ "user_name submitterName, "
            + "user_email submitterEmail, "
			+ "submission_date submissionDate, "
			+ "payment_total_amt paymentTotal, "
			+ "contribution_applicable_date payrollDate, "
			+ "submission_type_code type, "
			+ "process_status_code systemStatus, "
			+ "total_contribution_amt contributionTotal, "
			+ "money_source_id moneySourceID, "
			+ "application_code applicationCode, "
			+ "last_locked_by_user_id lockUserId, "
			+ "last_locked_ts lockTS, "
            + "tpa_system_name tpaSystemName, "
            + "tpa_submission_type tpaSubmissionType, "
            + "tpa_version_no tpaVersionNo, "
            + "combo_file_ind comboFileInd "
			+ "from "
			+ STP_SCHEMA_NAME
			+ "submission_history_view "
			+ "where contract_id = ? ";

    private static final String SQL_LTPT_SUBMISSION_CASE_SELECT = "select error_level errorLevel, "
            + "error_message errorMessage, "
            + "error_cond_string errorCondString, "
            + "last_updated_ts lastUpdatedTimestamp "
            + "from "
            + STP_SCHEMA_NAME
            + "submission_case "
            + "where contract_id = ? and submission_id = ? and submission_case_type_code = ? ";
	
    private static final String SQL_VESTING_SUBMISSION_CASE_SELECT = "select error_level errorLevel, "
        + "error_message errorMessage, "
        + "error_cond_string errorCondString, "
        + "last_updated_ts lastUpdatedTimestamp "
        + "from "
        + STP_SCHEMA_NAME
        + "submission_case "
        + "where contract_id = ? and submission_id = ? and submission_case_type_code = ? ";

	private static final String AND_SUBMISSION_DATE_CLAUSE = " and submission_date between ? and ? ";

	private static final String AND_TYPE_CLAUSE = " and submission_type_code = ? ";
	
	private static final String AND_STATUS_CLAUSE_START = " and process_status_code in( ";
	
	private static final String AND_STATUS_CLAUSE_END = " ) ";

	private static final String AND_TYPE_IN_CLAUSE = " and submission_type_code in (?, ?) ";

	private static final String AND_SUBMITTER_ID_SIMPLE_CLAUSE = " and user_id = ? ";

	private static final String AND_SUBMITTER_ID_IN_CLAUSE = " and user_id in (?, ?) ";

    private static final String AND_SUBMITTER_ID_IN_CLAUSE2 = " and user_id in ";

    private static final String AND_START_PAYROLL_DATE_CLAUSE = " and contribution_applicable_date >= ? ";

	private static final String AND_END_PAYROLL_DATE_CLAUSE = " and contribution_applicable_date <= ";

	private static final String AND_SUBMISSION_NUMBER_CLAUSE = " and submission_id = ? ";

	private static final String AND_SYSTEM_STATUS_CLAUSE = " and process_status_code ^= '97' ";

	private static final String SQL_HISTORY_SUFIX = " order by 1 "
			+ " for fetch only";

	private static final String SQL_HISTORY_LOCK_SELECT = "select  "
			+ "last_locked_by_user_id lockUserId, "
			+ "last_locked_ts lockTS "
			+ "from "
			+ STP_SCHEMA_NAME
			+ "submission_history_view "
			+ "where contract_id = ? "
            + "and submission_type_code = ? "
			+ "and submission_id = ? ";

	private static final String SQL_SELECT_MONEY_SOURCE = "select money_source_id, contract_money_src_long_name "
			+ "from "
			+ CUSTOMER_SCHEMA_NAME
			+ "contract_money_source "
			+ "where contract_id = ? " + "for fetch only";

	private static final String SQL_SSN_SELECT = "select personal_id "
			+ "from " + PLAN_SPONSOR_SCHEMA_NAME + "user_profile "
			+ "where personal_id_type_code = 'SN' "
			+ "and user_profile_id = ? " + "for fetch only";

	private static final String SQL_SELECT_COUNT_SUBMISSION_CASES = "select count(*) "
			+ "from "
			+ STP_SCHEMA_NAME
			+ "submission_case "
			+ "where submission_id=? and contract_id=?";

	private static final String SQL_SELECT_CONTRACT_NAME = "select contract_name "
			+ "from "
			+ JOURNAL_SCHEMA_NAME
			+ "submission_journal "
			+ "where submission_id=?";

	private static final String SQL_LAST_SUBMITTED_CONTRIBUTION_PREFIX =
			"select s.submission_id" +
			"  from " + STP_SCHEMA_NAME  + "submission s," +
			            STP_SCHEMA_NAME  + "submission_case scase," +
						STP_SCHEMA_NAME  + "submission_contribution sc" +
			" where s.submission_id = scase.submission_id" +
			"	 and scase.submission_id = sc.submission_id" +
			"     and scase.contract_id = sc.contract_id" +
			"     and scase.submission_case_type_code = sc.submission_case_type_code" +
			"     and sc.money_source_id = 'REG'" +
			"     and scase.process_status_code = '16'" +
			"     and s.submission_ts is not null" +
			"     and scase.contract_id = ?" +
			"     and s.user_id ";

	private static final String SQL_LAST_SUBMITTED_CONTRIBUTION_SHORT_INFIX = "= ?";
	private static final String SQL_LAST_SUBMITTED_CONTRIBUTION_LONG_INFIX = "in (?, ?)";

	private static final String SQL_LAST_SUBMITTED_CONTRIBUTION_SUFIX =
			" order by s.submission_ts desc" +
			" fetch first 1 row only for fetch only";

	private static final String SQL_CONTRIBUTION_SELECT = "select SCASE.LAST_LOCKED_BY_USER_ID as lockUserId, "
			+ "SCASE.LAST_LOCKED_TS as lockTS, "
			+ "SCASE.CONTRACT_ID as contractId, "
			+ "SCASE.SUBMISSION_ID as submissionId, "
			+ "SCASE.PROCESS_STATUS_CODE as processStatusCode, "
			+ "GPI.REQUESTED_PAYMENT_EFFECT_DATE as paymentEffectiveDate, "
			+ "SCON.TOTAL_LOAN_REPAYMENT_AMT as totalLoanRepyamentAmount, "
			+ "SCON.TOTAL_PARTICIPANT_COUNT as totalParticipantCount, "
			+ "SJ.GENERATE_STATEMENT_IND as statementRequestInd, "
			+ "SCON.ERROR_COND_STRING as errorCondString "
			+ "from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_CASE SCASE "
			+ "left join "
			+ JOURNAL_SCHEMA_NAME
			+ "SUBMISSION_JOURNAL SJ on scase.submission_id = sj.submission_id "
			+ "left join "
			+ JOURNAL_SCHEMA_NAME
			+ "GFT_PAYMENT_INFO GPI on scase.submission_id = gpi.submission_id and scase.contract_id = gpi.contract_id, "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_CONTRIBUTION SCON "
			+ "where scase.submission_id = scon.submission_id "
			+ "and scase.contract_id = scon.contract_id "
			+ "and scase.submission_case_type_code = scon.submission_case_type_code "
			+ "and scase.submission_id = ? "
			+ "and scase.contract_id = ? "
			+ "and scase.SUBMISSION_CASE_TYPE_CODE = 'C'";

	private static final String SQL_TOTAL_PARTICIPANT_COUNT_SELECT = "select "
			+ "SCON.TOTAL_PARTICIPANT_COUNT as totalParticipantCount "
			+ "from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_CONTRIBUTION SCON "
			+ "where scon.submission_id = ? "
			+ "and scon.contract_id = ? "
			+ "and scon.SUBMISSION_CASE_TYPE_CODE = 'C'";

	private static final String SQL_CASE_STATUS_SELECT =
			"select process_status_code" +
			"  from " + STP_SCHEMA_NAME  + "submission_case" +
			" where submission_id = ?" +
			"   and contract_id = ?" +
			"   and submission_case_type_code = 'C'";

	private static final String SQL_PARTICIPANT_SELECT = "select employer_designated_id as employerDesignatedId, full_name as fullName, "
			+ "source_record_no as sourceRecordNo, ERROR_COND_STRING as errorCondString "
			+ "from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_PARTICIPANT "
			+ "where submission_id = ? and contract_id = ? ";
	
    private static final String SQL_LONG_TERM_PART_TIME_PARTICIPANT_SELECT = "select sequence_no as sourceRecordNo, "
            + "social_security_number as ssn, "
            + "first_name as firstName, last_name as lastName, middle_initial as middleInitial, part_time_qualification_year as ltptAssessYr, "
            + "ERROR_COND_STRING as errorCondString, PROCESS_STATUS_CODE as processStatusCode "
            + "from "
            + STP_SCHEMA_NAME
            + "EMPLOYEE_LTPT_INFO ltpt  ";

    private static final String SQL_LONG_TERM_PART_TIME_PARTICIPANT_JOIN_CLAUSE = " join  ( select record_no, max(level) as part_order "
            + "from ("
            +           "select v.sequence_no as record_no, stp100.stp_err_cat(v.error_cond_string) as level "
            +           "from stp100.EMPLOYEE_LTPT_INFO v "
            +           "where v.submission_id = ? and v.contract_id = ? "
            + ") as temp_rows "
            + "group by record_no) as temp_level "
            + "on ltpt.SEQUENCE_NO = temp_level.record_no ";
    
    private static final String SQL_LONG_TERM_PART_TIME_PARTICIPANT_WHERE_CLAUSE = "where ltpt.submission_id = ? and ltpt.contract_id = ? ";
    
    private static final String SQL_VESTING_PARTICIPANT_SELECT = "select source_record_no as sourceRecordNo, "
        + "social_security_number as ssn, employee_id as empId, "
        + "first_name as firstName, last_name as lastName, middle_initial as middleInitial,"
        + "PERCENTAGE_EFFECTIVE_DATE as percDate, VESTED_YRS_OF_SERVICE as vyos, VESTED_YRS_OF_SERVICE_EFF_DT vyosDate, "
        + "ERROR_COND_STRING as errorCondString, PROCESS_STATUS_CODE as processStatusCode,APPLY_LTPT_CREDITING as applyLTPTCrediting "
        + "from "
        + STP_SCHEMA_NAME
        + "employee_vesting ev ";

    private static final String SQL_VESTING_PARTICIPANT_JOIN_CLAUSE = " join  ( select record_no, max(level) as part_order "
                + "from ("
                +           "select v.source_record_no as record_no, stp100.stp_err_cat(v.error_cond_string) as level "
                +           "from stp100.employee_vesting v "
                +           "where v.submission_id = ? and v.contract_id = ? "
                +           "union all "
                +           "select v.source_record_no as record_no, stp100.stp_err_cat(p.error_cond_string) as level "
                +           "from stp100.employee_vesting v join stp100.employee_vesting_percentage p "
                +           "on p.SUBMISSION_ID = v.SUBMISSION_ID "
                +           "and p.CONTRACT_ID = v.CONTRACT_ID "
                +           "and p.SOURCE_RECORD_NO = v.SOURCE_RECORD_NO "
                +           "where v.submission_id = ? and v.contract_id = ? "
                + ") as temp_rows "
                + "group by record_no) as temp_level "
                + "on ev.SOURCE_RECORD_NO = temp_level.record_no ";

    private static final String SQL_VESTING_PARTICIPANT_DEFAULT_ORDER_CLAUSE = " part_order desc, source_record_no asc ";

    private static final String SQL_LTPT_PARTICIPANT_DEFAULT_ORDER_CLAUSE = " part_order desc, sequence_no asc ";
    
    private static final String SQL_VESTING_PARTICIPANT_WHERE_CLAUSE = "where ev.submission_id = ? and ev.contract_id = ? ";

	private static final String ID_ORDER_CLAUSE = "order by employer_designated_id";

	private static final String RECORD_NO_ORDER_CLAUSE = "order by source_record_no";

	private static final String NEGATIVE_RECORD_NUMBER_CLAUSE = "and source_record_no < 0";

	private static final String SQL_CHECK_PARTICIPANT_EXISTS = "select count(*) from "
    		+ STP_SCHEMA_NAME
    		+ "SUBMISSION_PARTICIPANT "
    		+ "where submission_id = ? and contract_id = ? "
    		+ "and employer_designated_id = ?";

	private static final String SQL_PARTICIPANT_COUNT_SELECT = "select count(*) from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_PARTICIPANT "
			+ "where submission_id = ? and contract_id = ? ";

    private static final String SQL_VESTING_PARTICIPANT_COUNT_SELECT = "select count(*) from "
        + STP_SCHEMA_NAME
        + "employee_vesting "
        + "where submission_id = ? and contract_id = ? ";

    private static final String SQL_LONG_TERM_PART_TIME_PARTICIPANT_COUNT_SELECT = "select count(*) from "
            + STP_SCHEMA_NAME
            + "EMPLOYEE_LTPT_INFO "
            + "where submission_id = ? and contract_id = ? ";

    private static final String SQL_STATUS_SELECT = "select p.participant_status_code as participantStatus, "
			+ "p.participant_id as participantId, "
			+ "e.employee_id as employeeId, "
			+ "e.social_security_no as socialSecurityNo, "
			+ "e.first_name as firstName, e.last_name as lastName "
			+ "from "
			+ CUSTOMER_SCHEMA_NAME
			+ "EMPLOYEE_CONTRACT E, "
			+ CUSTOMER_SCHEMA_NAME
			+ "PARTICIPANT_CONTRACT P "
			+ "where e.profile_id = p.profile_id "
			+ "and e.contract_id = p.contract_id " + "and e.contract_id = ? ";

	private static final String SQL_ACTIVE_PARTICIPANT_COUNT = "select count(*)"
			+ "from "
			+ CUSTOMER_SCHEMA_NAME
			+ "PARTICIPANT_CONTRACT P "
			+ "where p.contract_id = ? "
			+ "and p.participant_status_code = 'AC'";

	private static final String SSN_SELECT_CLAUSE = " and e.social_security_no = ? for fetch only";

	private static final String EE_SELECT_CLAUSE = " and e.employee_id = ? for fetch only";

	private static final String SQL_ACTIVE_LOANS_SELECT = "select loan_id as loanId "
			+ "from "
			+ CUSTOMER_SCHEMA_NAME
			+ "participant_loan "
			+ "where participant_id = ? "
			+ "and contract_id = ? "
			+ "and (outstanding_principal_amt > 0 "
			+ "or outstanding_interest_amt > 0) ";

	private static final String SQL_ACTIVE_LOANS_SELECT_BY_IDENTIFIER =
			"select pl.loan_id as loanId" +
			"  from " + CUSTOMER_SCHEMA_NAME + "participant_loan pl," +
						CUSTOMER_SCHEMA_NAME + "employee_contract e" +
			" where pl.profile_id = e.profile_id" +
			"   and pl.contract_id = e.contract_id" +
			"   and pl.contract_id = ?" +
			"   and (pl.outstanding_principal_amt > 0" +
			"   or pl.outstanding_interest_amt > 0)";

	private static final String SQL_ALLOCATION_MONEY_TYPE_SELECT = "select distinct money_type_id as moneyTypeId "
		    + "from "
			+ STP_SCHEMA_NAME
			+ "submission_allocation  "
			+ "where submission_id = ? "
			+ "and contract_id = ? "
			+ "for fetch only";


    private static final String SQL_VESTING_MONEY_TYPE_SELECT = "select distinct money_type_short_name as moneyTypeId "
        + "from "
        + STP_SCHEMA_NAME
        + "employee_vesting_percentage  "
        + "where submission_id = ? "
        + "and contract_id = ? "
        + "for fetch only";

	private static final String SQL_MONEY_TYPE_COUNT_SELECT = "select money_type_id as moneyTypeId, "
		+ "source_record_no, "
		+ "count(*) as count "
	    + "from "
		+ STP_SCHEMA_NAME
		+ "submission_allocation  "
		+ "where submission_id = ? "
		+ "and contract_id = ? "
		+ "group by money_type_id, "
		+ "source_record_no "
		+ "for fetch only";

    private static final String SQL_VESTING_MONEY_TYPE_COUNT_SELECT = "select money_type_short_name as moneyTypeId, "
        + "source_record_no as sourceRecordNo, "
        + "count(*) as count "
        + "from "
        + STP_SCHEMA_NAME
        + "employee_vesting_percentage  "
        + "where submission_id = ? "
        + "and contract_id = ? "
        + "group by money_type_short_name, "
        + "source_record_no "
        + "for fetch only";

	private static final String SQL_SELECT_SUBMISSION_CONTRACT_MONEY_TYPES = "SELECT "
			+ "distinct MONEY_TYPE_ID as moneyTypeId, '0' as occurrenceNo "
			+ "FROM " + STP_SCHEMA_NAME
			+ "submission_contribution_money_type "
			+ "where submission_id = ? "
			+ "and contract_id = ? "
			+ "for fetch only";

	public static final String SQL_SELECT_CONTRACT_DATA =
			"SELECT PARTICIPANT_SORT_OPTION_CODE as participantSortOption," +
			"    	CONTRACT_NAME as contractName," +
			"       MANULIFE_COMPANY_ID as companyId" +
			"  FROM " + CUSTOMER_SCHEMA_NAME + "CONTRACT_CS" +
			" WHERE CONTRACT_ID = ?" +
			"   FOR FETCH ONLY";

	public static final String SQL_SELECT_COMPANY_CODE =
			"SELECT MANULIFE_COMPANY_ID as companyCode " +
			"  FROM " + CUSTOMER_SCHEMA_NAME + "CONTRACT_CS" +
			" WHERE CONTRACT_ID = ?" +
			"   FOR FETCH ONLY";

	public static final String SQL_SELECT_LOAN_FEATURE =
		"SELECT count(*)" +
		"  FROM " + CUSTOMER_SCHEMA_NAME + "CONTRACT_PRODUCT_FEATURE" +
		" WHERE CONTRACT_ID = ?" +
		"   AND PRODUCT_FEATURE_TYPE_CODE LIKE 'LRK%'" +
		"   FOR FETCH ONLY";

	private static final String SQL_ALLOCATION_SELECT = "select MONEY_TYPE_ID as moneyTypeId, OCCURRENCE_NO as occurrenceNo, ALLOCATION_AMT as allocationAmount, "
			+ "ERROR_COND_STRING as errorCondString, source_record_no as sourceRecordNo "
			+ "from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_ALLOCATION "
			+ "where submission_id = ? and contract_id = ? "
			+ "and employer_designated_id = ? ";

    private static final String SQL_VESTING_PERCENTAGE_SELECT = "select money_type_short_name as moneyTypeId, "
        + "VESTING_PCT as percentage, sequence_no as sequence, "
        + "ERROR_COND_STRING as errorCondString, source_record_no as sourceRecordNo "
        + "from "
        + STP_SCHEMA_NAME
        + "employee_vesting_percentage "
        + "where submission_id = ? and contract_id = ? "
        + "and source_record_no = ? "
        + "for fetch only ";

	private static final String ALLOCATION_AMOUNT_NOT_ZERO = "and allocation_amt ^= 0.00";

	public static final String SQL_LOAN_REPAYMENT_AMOUNTS = "SELECT "
			+ " LOAN_ID as loanId, OCCURRENCE_NO as occurrenceNo, LOAN_REPAYMENT_AMT as loanRepaymentAmount, "
			+ " ERROR_COND_STRING as errorCondString, source_record_no as sourceRecordNo FROM "
			+ STP_SCHEMA_NAME + "SUBMISSION_LOAN_REPAYMENT WHERE "
			+ "     SUBMISSION_ID = ? AND CONTRACT_ID = ?"
			+ " AND EMPLOYER_DESIGNATED_ID = ? ";

	public static final String LOAN_AMT_GREATER_THAN_ZERO_CLAUSE = " AND LOAN_REPAYMENT_AMT > 0 ";

	public static final String LOAN_ORDER_BY_CLAUSE = " ORDER BY 1";

	private static final String SQL_MAXIMUM_ALLOCATION_RECORD_SELECT = "select max(occurrence_no) from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_ALLOCATION "
			+ "where submission_id = ? and contract_id = ? "
			+ "and employer_designated_id = ? "
			+ "and money_type_id = ?";

	private static final String SQL_ALLOCATION_OCCURRENCE_COUNT_SELECT = "select count(*) from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_ALLOCATION "
			+ "where submission_id = ? and contract_id = ? "
			+ "and employer_designated_id = ? "
			+ "and money_type_id = ?";

    private static final String SQL_VESTING_SEQUENCE_SELECT = "select max(sequence_no) from "
            + STP_SCHEMA_NAME
            + "employee_vesting_percentage "
            + "where submission_id = ? and contract_id = ? "
            + "and source_record_no = ? ";


	private static final String SQL_MAXIMUM_LOAN_REPAYMENT_RECORD_SELECT = "select max(occurrence_no) from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_LOAN_REPAYMENT "
			+ "where submission_id = ? and contract_id = ? "
			+ "and employer_designated_id = ? "
			+ "and loan_id = ?";

	private static final String SQL_LOAN_REPAYMENT_OCCURRENCE_COUNT_SELECT = "select count(*) from "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_LOAN_REPAYMENT "
			+ "where submission_id = ? and contract_id = ? "
			+ "and employer_designated_id = ? "
			+ "and loan_id = ?";

    private static final String NEXT_SUBMISSION_ID_SQL = "CALL "
			+ JOURNAL_SCHEMA_NAME + "GET_NEXT_SUBMISSION_ID()";

	private static final String SQL_SELECT_SUBMISSION_JOURNAL = "SELECT  "
			+ "  SJ.CONTRACT_NAME as contractName "
            + "  ,SJ.SUBMISSION_TYPE_CODE "
			+ "  ,SJ.LOCATION_CODE as locationCode FROM  "
			+ JOURNAL_SCHEMA_NAME + "SUBMISSION_JOURNAL SJ"
			+ " WHERE SJ.SUBMISSION_ID = ? "
			+ "   FOR FETCH ONLY";

    private static final String SQL_SELECT_LAST_SUBMISSION = "SELECT  "
            + "  max(SJ.UPLOAD_END_TS) as lastSubmission FROM  "
            + JOURNAL_SCHEMA_NAME + "SUBMISSION_JOURNAL SJ"
            + " WHERE SJ.CONTRACT_ID = ? "
            + "   FOR FETCH ONLY";

	private static final String SQL_SUBMISSION_JOURNAL_INSERT = "INSERT INTO "
			+ JOURNAL_SCHEMA_NAME
			+ "SUBMISSION_JOURNAL "
			+ " (SUBMISSION_ID, CONTRACT_ID, CONTRACT_NAME, USER_NAME "
			+ "  ,USER_SSN, USER_TYPE, USER_TYPE_ID, USER_TYPE_NAME "
			+ "  ,LOCATION_CODE,  UPLOAD_START_TS, UPLOAD_END_TS, APPLICATION_CODE "
            + "  ,SUBMISSION_TYPE_CODE, USER_FILE_NAME, USER_FILE_SIZE, NOTIFICATION_EMAIL_ADDR)"
			+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String SQL_SUBMISSION_JOURNAL_UPDATE = "UPDATE "
			+ JOURNAL_SCHEMA_NAME + "SUBMISSION_JOURNAL SET "
			+ "USER_NAME = ?, USER_SSN = ?, USER_TYPE = ?, "
			+ "USER_TYPE_ID = ?, USER_TYPE_NAME = ?, UPLOAD_END_TS = CURRENT TIMESTAMP, "
            + "SUBMISSION_TYPE_CODE = ?, GENERATE_STATEMENT_IND = ? WHERE "
			+ "SUBMISSION_ID = ?";

	private static final String SQL_GFT_PAYMENT_INFO_INSERT = "INSERT INTO "
			+ JOURNAL_SCHEMA_NAME
			+ "GFT_PAYMENT_INFO "
			+ " (SUBMISSION_ID, CONTRACT_ID, PAYMENT_TOTAL_AMT, REQUESTED_PAYMENT_EFFECT_DATE) "
			+ " VALUES(?,?,?,?)";

	private static final String SQL_GFT_PAYMENT_INFO_UPDATE = "UPDATE "
			+ JOURNAL_SCHEMA_NAME + "GFT_PAYMENT_INFO SET "
			+ "PAYMENT_TOTAL_AMT = ?, REQUESTED_PAYMENT_EFFECT_DATE = ? "
			+ "WHERE SUBMISSION_ID = ? AND CONTRACT_ID = ?";

	private final static String SQL_GFT_PAYMENT_DETAILS_INSERT = "INSERT into "
			+ JOURNAL_SCHEMA_NAME
			+ "GFT_PAYMENT_DETAILS ( "
			+ "SUBMISSION_ID, CONTRACT_ID, SEQUENCE_ID, INSTRUCTION_NO, ACCOUNT_NO, ACCOUNT_NAME, ACCOUNT_TYPE_CODE, "
			+ "PAYMENT_AMT, TRANSIT_NO, BANK_NAME, PURPOSE_CODE "
			+ " ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";

	private static final String SQL_INSERT_SUBMISSION = "INSERT into  " + STP_SCHEMA_NAME
            + "submission (SUBMISSION_ID, SUBMISSION_TS, FILE_NAME, "
            + "MAP_NAME, USER_ID, INPUT_LOCATION_NAME, "
            + "PAYMENT_INFO_ONLY_IND, CREATED_USER_ID, LAST_UPDATED_USER_ID "
            + ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_INSERT_SUBMISSION_CASE =
		"INSERT into " + STP_SCHEMA_NAME + "submission_case ("
			+ "SUBMISSION_ID, CONTRACT_ID, SUBMISSION_CASE_TYPE_CODE, PROCESSED_TS, PROCESS_STATUS_CODE, "
			+ "SYNTAX_ERROR_IND, SUBMIT_COUNT, CREATED_USER_ID, LAST_UPDATED_USER_ID) "
			+ "values (?, ?, ?, CURRENT TIMESTAMP, ?, ?, ?, ?, ?)";

	private static final String SQL_SELECT_SUBMISSION_CONTRIBUTION_DATA = "SELECT "
			+ "rtrim(A.MONEY_SOURCE_ID) AS moneySourceId, "
			+ "A.PRIORITY_NO AS priorityNumber, "
			+ "A.TRANSACTION_CODE as transactionCode "
			+ "FROM "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION_CONTRIBUTION A, "
			+ STP_SCHEMA_NAME
			+ "SUBMISSION B "
			+ "WHERE B.SUBMISSION_ID = ? "
			+ "AND A.SUBMISSION_ID = B.SUBMISSION_ID "
			+ "AND A.CONTRACT_ID = ? "
			+ "AND A.SUBMISSION_CASE_TYPE_CODE = 'C'";

	private static final String SQL_INSERT_SUBMISSION_CONTRIBUTION = "INSERT into "
			+ STP_SCHEMA_NAME
			+ "submission_contribution ("
			+ "SUBMISSION_ID, CONTRACT_ID, SUBMISSION_CASE_TYPE_CODE, MONEY_SOURCE_ID, "
			+ "CONTRIBUTION_APPLICABLE_DATE, "
			+ "TOTAL_ALLOCATION_AMT, TOTAL_WITHDRAWAL_AMT, "
			+ "TOTAL_LOAN_REPAYMENT_AMT, TOTAL_PARTICIPANT_COUNT, "
			+ "TRANSACTION_CODE, "
			+ "PRIORITY_NO, PROCESSOR_USER_ID, ERROR_COND_STRING, "
			+ "CREATED_USER_ID, LAST_UPDATED_USER_ID "
			+ " ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_SUBMISSION_PAYMENT = "INSERT into "
        + STP_SCHEMA_NAME
        + "submission_payment ("
        + "SUBMISSION_ID, CONTRACT_ID, SUBMISSION_CASE_TYPE_CODE, "
        + "CREATED_USER_ID, LAST_UPDATED_USER_ID "
        + " ) values (?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_SUBMISSION_CONTRACT_MONEY_TYPES = "INSERT into "
			+ STP_SCHEMA_NAME
			+ "submission_contribution_money_type ("
			+ "SUBMISSION_ID, CONTRACT_ID, MONEY_TYPE_ID, CREATED_USER_ID, LAST_UPDATED_USER_ID"
			+ " ) values (?, ?, ?, ?, ?)";

	private static final String SQL_SELECT_MINIMUM_RECORD_NUMBER = "SELECT min(sourceRecordNo) "
			+ "from (select min(source_record_no) as sourceRecordNo from "
			+ STP_SCHEMA_NAME
			+ "submission_participant "
			+ "WHERE submission_id = ? and contract_id = ? "
			+ "union "
			+ "select min(source_record_no) as sourceRecordNo from "
			+ STP_SCHEMA_NAME
			+ "submission_allocation "
			+ "WHERE submission_id = ? and contract_id = ? "
			+ "union "
			+ "select min(source_record_no) as sourceRecordNo from "
			+ STP_SCHEMA_NAME
			+ "submission_loan_repayment "
			+ "WHERE submission_id = ? and contract_id = ? "
			+ ") as sourceRecordNo";

	private static final String SQL_SELECT_MAXIMUM_RECORD_NUMBER = "SELECT max(sourceRecordNo) "
			+ "from (select max(source_record_no) as sourceRecordNo from "
			+ STP_SCHEMA_NAME
			+ "submission_participant "
			+ "WHERE submission_id = ? and contract_id = ?"
			+ "union "
			+ "select max(source_record_no) as sourceRecordNo from "
			+ STP_SCHEMA_NAME
			+ "submission_allocation "
			+ "WHERE submission_id = ? and contract_id = ? "
			+ "union "
			+ "select max(source_record_no) as sourceRecordNo from "
			+ STP_SCHEMA_NAME
			+ "submission_loan_repayment "
			+ "WHERE submission_id = ? and contract_id = ? "
			+ ") as sourceRecordNo";

	private static final String SQL_INSERT_SUBMISSION_PARTICIPANT = "INSERT into "
			+ STP_SCHEMA_NAME
			+ "submission_participant ("
			+ "SUBMISSION_ID, CONTRACT_ID, EMPLOYER_DESIGNATED_ID, FULL_NAME, "
			+ "ERROR_COND_STRING, SOURCE_RECORD_NO, CREATED_USER_ID, LAST_UPDATED_USER_ID"
			+ " ) values (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_INSERT_SUBMISSION_ALLOCATION = "INSERT into "
			+ STP_SCHEMA_NAME
			+ "submission_allocation ("
			+ "SUBMISSION_ID, CONTRACT_ID, EMPLOYER_DESIGNATED_ID, MONEY_TYPE_ID, OCCURRENCE_NO, ALLOCATION_AMT,  "
			+ "CONTRIBUTION_APPLICABLE_DATE, ERROR_COND_STRING, SOURCE_RECORD_NO, CREATED_USER_ID, LAST_UPDATED_USER_ID"
			+ " ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_VESTING_PERCENTAGES = "INSERT into "
        + STP_SCHEMA_NAME
        + "employee_vesting_percentage ("
        + "SUBMISSION_ID, CONTRACT_ID, SOURCE_RECORD_NO, SEQUENCE_NO, MONEY_TYPE_SHORT_NAME, VESTING_PCT,  "
        + "ERROR_COND_STRING, CREATED_USER_ID, CREATED_TS, LAST_UPDATED_USER_ID, LAST_UPDATED_TS"
        + " ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_UPDATE_VESTING_PARTICIPANT = "UPDATE "
        + STP_SCHEMA_NAME
        + "employee_vesting "
        + "set PERCENTAGE_EFFECTIVE_DATE = ?, "
        + "VESTED_YRS_OF_SERVICE = ?, "
        + "VESTED_YRS_OF_SERVICE_EFF_DT = ?, "
        + "ERROR_COND_STRING = ?, "
        + "APPLY_LTPT_CREDITING=?, "
        + "LAST_UPDATED_USER_ID = ?, LAST_UPDATED_TS = ? "
        + "where submission_id = ? and contract_id = ? "
        + "and source_record_no = ? ";
    
    private static final String SQL_UPDATE_LONG_TERM_PART_TIME_PARTICIPANT = "UPDATE "
            + STP_SCHEMA_NAME
            + "EMPLOYEE_LTPT_INFO "
            + "set PART_TIME_QUALIFICATION_YEAR = ?, "
            + "ERROR_COND_STRING = ?, "
            + "LAST_UPDATED_USER_ID = ?, LAST_UPDATED_TS = ? "
            + "where submission_id = ? and contract_id = ? "
            + "and sequence_no = ? ";

	private static final String SQL_INSERT_SUBMISSION_LOAN_REPAYMENT = "INSERT into "
			+ STP_SCHEMA_NAME
			+ "submission_loan_repayment ("
			+ "SUBMISSION_ID, CONTRACT_ID, EMPLOYER_DESIGNATED_ID, LOAN_ID, OCCURRENCE_NO, LOAN_REPAYMENT_AMT, "
			+ "LOAN_REPAYMENT_APPLICABLE_DATE, ERROR_COND_STRING, SOURCE_RECORD_NO, CREATED_USER_ID, LAST_UPDATED_USER_ID"
			+ " ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_INSERT_SUBMISSION_PAYMENT_INSTRUCTION = "INSERT into "
			+ STP_SCHEMA_NAME
			+ "submission_payment_instruction ("
			+ "SUBMISSION_ID, CONTRACT_ID, PAYMENT_OCCURRENCE_NO, "
			+ "PAYMENT_METHOD_CODE, BANKING_INSTRUCTION_NO, BANK_TRANSIT_NO, BANK_ACCOUNT_NO,"
			+ "PAYMENT_AMT, PURPOSE_CODE, ERROR_COND_STRING, SOURCE_RECORD_NO, CREATED_USER_ID, LAST_UPDATED_USER_ID"
			+ " ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_SELECT_SUBMISSION_PAYMENT_INSTRUCTION =
			"select error_cond_string as errorCondString," +
			" 		source_record_no as sourceRecordNo" +
			"  from " + STP_SCHEMA_NAME + "submission_payment_instruction" +
			" where submission_id = ?" +
			"   and contract_id = ?";

	private static final String SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_NEW = "UPDATE "
			+ STP_SCHEMA_NAME
			+ "submission_contribution SET "
			+ "TOTAL_ALLOCATION_AMT = ?, TOTAL_WITHDRAWAL_AMT = ?, "
			+ "TOTAL_LOAN_REPAYMENT_AMT = ?, TOTAL_PARTICIPANT_COUNT = ?, "
			+ "LAST_UPDATED_USER_ID = ?, LAST_UPDATED_TS = CURRENT TIMESTAMP "
			+ "where submission_id = ? " + "and contract_id = ? ";

    private static final String SQL_RESET_PARTICIPANT_COUNT = "UPDATE "
			+ STP_SCHEMA_NAME + "submission_contribution SET "
			+ "TOTAL_PARTICIPANT_COUNT = ? " + "where submission_id = ? "
			+ "and contract_id = ? ";

    private static final String SQL_CANCEL_SUBMISSION = "UPDATE "
            + STP_SCHEMA_NAME + "submission_case SET "
            + "PROCESS_STATUS_CODE = '99' " + "where submission_id = ? "
            + "and contract_id = ? and submission_case_type_code =  ? ";

	private static final String SQL_SET_SUBMISSION_TIMESTAMP = "UPDATE "
    		+ STP_SCHEMA_NAME + "submission SET "
    		+ "SUBMISSION_TS = CURRENT TIMESTAMP, "
    		+ "LAST_UPDATED_USER_ID = ?, "
    		+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
    		+ "where submission_id = ?";
    
    private static final String SQL_SET_SUBMISSION_JOURNAL_USER_TYPE = "UPDATE "
            + JOURNAL_SCHEMA_NAME + "submission_journal SET "
            + "USER_TYPE = ? "
            + "where submission_id = ?";

    private static final String SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_SAVE = "UPDATE "
			+ STP_SCHEMA_NAME
			+ "submission_contribution SET "
			+ "MONEY_SOURCE_ID = ?, "
			+ "CONTRIBUTION_APPLICABLE_DATE = ?, "
			+ "TOTAL_ALLOCATION_AMT = ?, "
			+ "TOTAL_WITHDRAWAL_AMT = ?, "
			+ "TOTAL_LOAN_REPAYMENT_AMT = ?, "
			+ "TOTAL_PARTICIPANT_COUNT = ?, "
			+ "PROCESSOR_USER_ID = ?, "
			+ "LAST_UPDATED_USER_ID = ?, "
			+ "TRANSACTION_CODE = ?, "
			+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
			+ "WHERE "
			+ "SUBMISSION_ID = ? AND CONTRACT_ID = ? ";

    private static final String SQL_UPDATE_SUBMISSION_PAYMENT_FOR_SAVE = "UPDATE "
            + STP_SCHEMA_NAME
            + "SUBMISSION_PAYMENT set "
            + "TOTAL_CONTRIBUTION_PAYMENT_AMT = ?, "
            + "TOTAL_BILL_PAYMENT_AMT = ?, "
            + "TOTAL_TEMP_CREDIT_PAYMENT_AMT = ?, "
            + "REQUESTED_PAYMENT_EFFECT_DATE = ? "
            + "WHERE "
            + "SUBMISSION_ID = ? AND CONTRACT_ID = ? ";

	private static final String SQL_UPDATE_SUBMISSION_CASE_PROCESS_STATUS = "update "
			+ STP_SCHEMA_NAME
			+ "submission_case "
			+ "set "
			+ "PROCESS_STATUS_CODE = ?,"
			+ "LAST_UPDATED_USER_ID = ?, "
			+ "LAST_UPDATED_TS = CURRENT TIMESTAMP "
			+ "where "
			+ "SUBMISSION_ID = ? and contract_id=? ";

    private static final String SQL_UPDATE_SUBMISSION_CASE_SUBMIT_COUNT = "update "
        + STP_SCHEMA_NAME
        + "submission_case "
        + "set "
        + "SUBMIT_COUNT = ?,"
        + "LAST_UPDATED_USER_ID = ?, "
        + "LAST_UPDATED_TS = CURRENT TIMESTAMP "
        + "where "
        + "SUBMISSION_ID = ? and contract_id=? ";

    private static final String SQL_UPDATE_SUBMISSION_CASE_SUBMIT_COUNT_ZERO = "update "
        + STP_SCHEMA_NAME
        + "submission_case "
        + "set "
        + "SUBMIT_COUNT = ?, PROCESS_STATUS_CODE = '" + SubmissionConstants.Census.PROCESS_STATUS_CODE_CANCELLED + "', "
        + "LAST_UPDATED_USER_ID = ?, "
        + "LAST_UPDATED_TS = CURRENT TIMESTAMP "
        + "where "
        + "SUBMISSION_ID = ? and contract_id=? ";

	private static final String SQL_UPDATE_SUBMISSION_PARTICIPANT = "UPDATE "
		+ STP_SCHEMA_NAME + "submission_participant "
		+ "SET source_record_no = ? "
		+ "WHERE submission_id = ? AND contract_id = ? "
		+ "AND employer_designated_id = ?";


	private static final String SQL_DELETE_STP_ROLLUP_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "stp_rollup where submission_id=? and contract_id=?";

	private static final String SQL_DELETE_SUBMISSION_ALLOCATION_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_allocation where submission_id=? and contract_id=?";

    private static final String SQL_DELETE_VESTING_PERCENTAGES = "delete from "
            + STP_SCHEMA_NAME
            + "employee_vesting_percentage where submission_id=? and contract_id=? ";

    private static final String SQL_DELETE_VESTING_PERCENTAGES_EMPTY_CLAUSE =
            "and VALUE(vesting_pct,'') = ''";

	private static final String SQL_DELETE_SUBMISSION_LOAN_REPAYMENT_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_loan_repayment where submission_id=? and contract_id=?";

	private static final String SQL_DELETE_SUBMISSION_PARTICIPANT_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_participant where submission_id=? and contract_id=?";

    private static final String SQL_DELETE_VESTING_PARTICIPANT_ITEM = "delete from "
        + STP_SCHEMA_NAME
        + "employee_vesting where submission_id=? and contract_id=? and source_record_no=? ";

	private static final String SQL_DELETE_SUBMISSION_CONTRIBUTION_MONEY_TYPE_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_contribution_money_type where submission_id=? and contract_id=?";

	private static final String SQL_DELETE_SUBMISSION_PAYMENT_INSTRUCTION_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_payment_instruction where submission_id=? and contract_id=?";

	private static final String SQL_DELETE_SUBMISSION_CONTRIBUTION_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_contribution where submission_id=? and contract_id=?";

    private static final String SQL_DELETE_SUBMISSION_PAYMENT_ITEM = "delete from "
            + STP_SCHEMA_NAME
            + "submission_payment where submission_id=? and contract_id=?";

    private static final String SQL_DELETE_EMPLOYEE_CENSUS_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "employee_census where submission_id=? and contract_id=?";

	private static final String SQL_DELETE_ADDRESS_SUBMISSION_CASE_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_case where submission_id=? and contract_id=? and submission_case_type_code='A'";

	private static final String SQL_DELETE_CONTRIBUTION_SUBMISSION_CASE_ITEM = "delete from "
			+ STP_SCHEMA_NAME
			+ "submission_case where submission_id=? and contract_id=? and submission_case_type_code='C'";

	private static final String SQL_DELETE_SUBMISSION_ITEM = "delete from "
			+ STP_SCHEMA_NAME + "submission where submission_id=?";

	private static final String SQL_DELETE_GFT_PAYMENT_DETAILS_ITEM = "delete from "
			+ JOURNAL_SCHEMA_NAME + "gft_payment_details where submission_id=?";

	private static final String SQL_DELETE_GFT_PAYMENT_INFO_ITEM = "delete from "
			+ JOURNAL_SCHEMA_NAME + "gft_payment_info where submission_id=?";

	private static final String SQL_DELETE_SUBMISSION_JOURNAL_ITEM = "delete from "
			+ JOURNAL_SCHEMA_NAME + "submission_journal where submission_id=?";

	private static final String SQL_GET_PAYMENT_ONLY_ITEM = "SELECT "
			+ "SJ.SUBMISSION_ID, "
			+ "SJ.CONTRACT_ID, "
			+ "SJ.CONTRACT_NAME, "
			+ "SJ.USER_TYPE, "
			+ "SJ.USER_SSN, "
			+ "SJ.USER_NAME, "
			+ "SJ.UPLOAD_END_TS, "
			+ "PMT.REQUESTED_PAYMENT_EFFECT_DATE, "
			+ "DET.SEQUENCE_ID, "
			+ "DET.INSTRUCTION_NO, "
			+ "DET.ACCOUNT_NO, "
			+ "DET.ACCOUNT_NAME, "
			+ "DET.ACCOUNT_TYPE_CODE, "
			+ "DET.PAYMENT_AMT, "
			+ "DET.TRANSIT_NO, "
			+ "DET.BANK_NAME, "
			+ "DET.PURPOSE_CODE, "
			+ "COALESCE(SCASE.PROCESS_STATUS_CODE,'13') AS PROCESS_STATUS_CODE "
			+ "FROM " + JOURNAL_SCHEMA_NAME + "SUBMISSION_JOURNAL SJ "
			+ "LEFT JOIN " + STP_SCHEMA_NAME + "SUBMISSION S ON "
			+ "SJ.SUBMISSION_ID = S.SUBMISSION_ID "
			+ "LEFT JOIN " + STP_SCHEMA_NAME + "SUBMISSION_CASE SCASE ON "
			+ "S.SUBMISSION_ID = SCASE.SUBMISSION_ID "
			+ "AND SCASE.SUBMISSION_CASE_TYPE_CODE = ?, "
			+ JOURNAL_SCHEMA_NAME + "GFT_PAYMENT_INFO PMT, " + JOURNAL_SCHEMA_NAME
			+ "GFT_PAYMENT_DETAILS DET WHERE "
			+ "SJ.SUBMISSION_ID = ? AND SCASE.CONTRACT_ID = ? AND "
			+ "SJ.SUBMISSION_ID = PMT.SUBMISSION_ID AND "
			+ "SJ.SUBMISSION_ID = DET.SUBMISSION_ID AND "
			+ "SJ.SUBMISSION_TYPE_CODE IN (?, ?) ORDER BY "
			+ "DET.SEQUENCE_ID ASC ";

	private static final String SQL_EMPLOYER_ID_WHERE = " and employer_designated_id=? ";

	private static final String SQL_LOAN_ID_WHERE = " and loan_id = ? and occurrence_no = ? ";

	private static final String SQL_MONEY_TYPE_WHERE = " and money_type_id = ? and occurrence_no = ? ";

	private static final String SQL_RECORD_NUMBER_WHERE = " and source_record_no = ? ";

    private static final String SQL_SELECT_TPA_USERS = "select eutf.user_profile_id from psw100.external_user_tpa_firm eutf, psw100.contract_cs c "
            + "where eutf.tpa_firm_id = c.third_party_admin_id "
            + "and c.contract_id = ? "
            + "and (select count(*) from "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "permission_holder ph, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "permission_grant pg where "
            + "            ph.permission_holder_id = pg.permission_holder_id "
            + "        and ph.user_tpa_firm_user_profile_id = ? "
            + "        and pg.security_task_permission_code = '"
            + ProfileDAO.VIEW_ALL_SUBMISSIONS
            + "' and ph.user_tpa_firm_tpa_firm_id = c.third_party_admin_id) > 0 "
            + "and (select count(*) from "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "permission_holder ph, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "permission_grant pg where "
            + "            ph.permission_holder_id = pg.permission_holder_id "
            + "        and ph.tpa_firm_contract_id = c.contract_id "
            + "        and pg.security_task_permission_code = '"
            + ProfileDAO.VIEW_ALL_SUBMISSIONS + "') = 0";

	private static final String SQL_USER_NAME_SELECT =
		"SELECT A.FIRST_NAME as firstName," +
		"       A.LAST_NAME as lastName," +
		"       B.TYPE_CODE as typeCode" +
		"  FROM " + PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE A," +
		            PLAN_SPONSOR_SCHEMA_NAME + "SECURITY_ROLE B" +
		" WHERE USER_PROFILE_ID = ?" +
		"   AND A.PSW_DIRECTORY_ROLE_CODE = B.DIRECTORY_ROLE_CODE";

	private static final String SQL_USER_NAME_SELECT_BY_PROFILE =
		"SELECT FIRST_NAME," +
		"       LAST_NAME" +
		"  FROM " + PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE" +
		" WHERE USER_PROFILE_ID = ?";

	private static final String SQL_SELECT_APPLICATION_CODE = "SELECT APPLICATION_CODE FROM "
			+ JOURNAL_SCHEMA_NAME
			+ "SUBMISSION_JOURNAL WHERE "
			+ "SUBMISSION_ID = ?";
	
	private static final String SQL_SUBMISSION_CREATOR_ID = "SELECT TRIM(USER_ID) FROM "
		+ STP_SCHEMA_NAME
		+ "SUBMISSION "
		+ "WHERE SUBMISSION_ID = ?" + "for fetch only";

	private static final String SQL_DELETE_LONG_TERM_PART_TIME_PARTICIPANT_ITEM = "delete from "
            + STP_SCHEMA_NAME
            + "EMPLOYEE_LTPT_INFO where submission_id=? and contract_id=? and sequence_no=? ";

	private SubmissionDAO() {
	}

	/**
	 * @see com.manulife.pension.service.report.handler.ReportHandlerFactory
	 * @param ReportCriteria
	 *            The filters and sorts for the report
	 * @return ReportData The report data value object
	 */
	public static SubmissionHistoryReportData getSubmissions(ReportCriteria criteria)
			throws SystemException {

		logger.debug("entry -> getSubmissions");
		logger.debug("Report criteria -> " + criteria.toString());

		SubmissionHistoryReportData reportDataVO =
			new SubmissionHistoryReportData(criteria, 0);
		Integer contractID =
			(Integer) criteria.getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO);

		if (criteria.getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO) != null) {

			List params = new ArrayList();
			String query = generateQuery(criteria, params);

			SelectBeanListQueryHandler handler =
				new SelectBeanListQueryHandler(SUBMISSION_DATA_SOURCE_NAME, query,
					SubmissionHistoryItem.class);

			try {
				logger.debug("Executing Prepared SQL Statment: " + query);
				List initialList = (List) handler.select(params.toArray());

				// do post-processing on the retrieved list
				List submissionHistoryDetails = massageItems(initialList, contractID);

				reportDataVO =
					new SubmissionHistoryReportData(criteria, submissionHistoryDetails.size());

				reportDataVO.setDetails(submissionHistoryDetails);

			} catch (DAOException e) {
				throw handleDAOException(e, className, "getSubmissions",
						"Problem occurred prepared call: " + query + " with criteria: " + criteria);
			}
		}
		logger.debug("exit <- getSubmissions");
		return reportDataVO;
	}

	public static SubmissionHistoryItem getSubmissionCase(int submissionNumber,
			int contractNumber, String type) throws SystemException {

		logger.debug("entry -> getSubmissionCase");
		logger.debug("submissionNumber -> " + submissionNumber);
		logger.debug("contractNumber -> " + contractNumber);
		logger.debug("type -> " + type);

		SubmissionHistoryItem submissionCase = null;

		StringBuffer query = new StringBuffer(SQL_HISTORY_SELECT);
		query.append(AND_SUBMISSION_NUMBER_CLAUSE);
		query.append(AND_TYPE_CLAUSE);

		SelectBeanListQueryHandler handler = new SelectBeanListQueryHandler(
				SUBMISSION_DATA_SOURCE_NAME, query.toString(),
				SubmissionHistoryItem.class);

		try {
			logger.debug("Executing Prepared SQL Statment: " + query.toString());
			List list = (List) handler.select(new Object[] {
					new Integer(contractNumber),
					new BigDecimal(String.valueOf(submissionNumber)), type });
			if (list == null || list.size() == 0) {
				// no cases retrieved - return null
				return null;
			}
			submissionCase = (SubmissionHistoryItem) list.get(0);
		} catch (DAOException e) {
			throw handleDAOException(e, className, "getSubmissionCase",
					"Problem occurred prepared call: " + query
							+ " with submissionNumber: " + submissionNumber
							+ " contractNumber: " + contractNumber + " type: "
							+ type);
		}

		setSubmitterType(submissionCase);

		if (SubmissionConstants.SUBMISSION_CASE_TYPE_ADDRESS.equals(type) ||
            SubmissionConstants.SUBMISSION_CASE_TYPE_CENSUS.equals(type)  ) {
			// this is done elsewhere for contributions
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			try {
				connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);
				statement = connection.prepareStatement(SQL_HISTORY_LOCK_SELECT);
				statement.setInt(1, contractNumber);
				statement.setString(2, type);
				statement.setInt(3, submissionNumber);

				rs = statement.executeQuery();
				if (!rs.next())
					return null;

				Lock lock = new Lock();
				lock.setUserId(rs.getString("lockUserId"));
				lock.setLockTS(rs.getTimestamp("lockTS"));
				submissionCase.setLock(lock);

			} catch (SQLException e) {

				throw new SystemException(e, className, "getSubmissionCase",
						"Problem occurred prepared call: "
						+ SQL_HISTORY_LOCK_SELECT
						+ " with submissionNumber: " + submissionNumber
						+ " contractNumber: " + contractNumber);
			} finally {
				try {
					if (rs != null) {
						rs.close();
						rs = null;
					}
				} catch (SQLException e) {
					throw new SystemException(e, className, "getSubmissionCase",
							"Problem occurred closing result set");
				}
				close(statement, connection);
			}
			setLockedByType(submissionCase);
		}
		logger.debug("exit <- getSubmissionCase");

		return submissionCase;

	}

	public static ContributionDetailItem getContributionDetails(int submissionId,
			int contractId) throws SystemException {

		logger.debug("entry -> getContributionDetails");
		logger.debug("submissionId -> " + submissionId);
		logger.debug("contractId -> " + contractId);

		// gather basic data
		SubmissionHistoryItem submissionCase = null;
		String processStatusCode = EMPTY_STRING;

		StringBuffer query = new StringBuffer(SQL_HISTORY_SELECT);
		query.append(AND_SUBMISSION_NUMBER_CLAUSE);
		query.append(AND_TYPE_IN_CLAUSE);

		SelectBeanListQueryHandler handler = new SelectBeanListQueryHandler(
				SUBMISSION_DATA_SOURCE_NAME, query.toString(),
				SubmissionHistoryItem.class);

		try {
			logger.debug("Executing Prepared SQL Statment: " + query.toString());
			List list = (List) handler.select(new Object[] {
					new Integer(contractId), new BigDecimal(String.valueOf(submissionId)),
					GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR,
					GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER });

            if (list == null || list.size() == 0) {
                return null;
            }

			submissionCase = (SubmissionHistoryItem) list.get(0);
		} catch (DAOException e) {
			throw handleDAOException(e, className, "getContributionDetails",
					"Problem occurred prepared call: " + query
							+ " with submissionId: " + submissionId
							+ " contractNumber: " + contractId);
		}
		// get contract name
		String contractName = EMPTY_STRING;
		try {
			SelectSingleValueQueryHandler handler3 = new SelectSingleValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_NAME,
					String.class);
			contractName = (String) handler3
					.select(new Object[] { new Integer(submissionId) });
		} catch (DAOException e) {
			throw handleDAOException(e, className, "getContributionDetails",
					"Problem occurred in prepared call: " + SQL_SELECT_CONTRACT_NAME +
					" with submissionId " + submissionId);
		}

		// build basic value object
		ContributionDetailItem contributionDetail = null;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			try {
				connection = getDefaultConnection(className,
						SUBMISSION_DATA_SOURCE_NAME);
				statement = connection
						.prepareStatement(SQL_CONTRIBUTION_SELECT);
				statement.setInt(1, submissionId);
				statement.setInt(2, contractId);

				rs = statement.executeQuery();
				if (!rs.next())
					return null;

				Lock lock = new Lock();
				lock.setUserId(rs.getString("lockUserId"));
				lock.setLockTS(rs.getTimestamp("lockTS"));

				String rawStatementRequestInd = rs
						.getString("statementRequestInd");
				contributionDetail = new ContributionDetailItem(
						submissionCase.getSubmitterID(),
						submissionCase.getSubmitterType(),
						submissionCase.getSubmitterName(),
						submissionCase.getSubmitterEmail(),
						submissionCase.getSubmissionId(),
						submissionCase.getSubmissionDate(),
						submissionCase.getType(),
						submissionCase.getSystemStatus(),
						submissionCase.getPayrollDate(),
						submissionCase.getContributionTotal(),
						submissionCase.getPaymentTotal(),
						submissionCase.getMoneySourceID(),
						submissionCase.getApplicationCode(),
						new Integer(contractId),
						contractName,
						rs.getInt("totalParticipantCount"),
						rs.getBigDecimal("totalLoanRepyamentAmount"),
						(rawStatementRequestInd == null ? null
								: (rawStatementRequestInd.equals(YES) ? Boolean.TRUE
										: Boolean.FALSE)),
                        null, 
                        null, 
                        null,
						lock);
				contributionDetail.setTransmissionId(submissionId);
				contributionDetail
						.setContractId(submissionCase.getContractId());
				contributionDetail.setSubmissionId(submissionCase
						.getSubmissionId());
				String errorCondString = rs.getString("errorCondString");
				if (null != errorCondString
						&& !ERROR_COND_STRING_OK.equals(errorCondString)
						&& !EMPTY_STRING.equals(errorCondString)) {
					ReportDataErrors errors = new ReportDataErrors();
					errors.addErrors(SubmissionErrorHelper.parseErrorConditionString(
							errorCondString, 0));
					contributionDetail.setReportDataErrors(errors);
				}
				processStatusCode = rs.getString("processStatusCode");
			} catch (SQLException e) {

				throw new SystemException(e, className,
						"getContributionDetails",
						"Problem occurred prepared call: "
								+ SQL_CONTRIBUTION_SELECT
								+ " with submissionNumber: " + submissionId
								+ " contractNumber: " + contractId);
			} finally {
				// close the statement, but leave the connection open for use
				// below
				try {
					if (rs != null) {
						rs.close();
						rs = null;
					}
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"getContributionDetails",
							"Problem occurred closing result set");
				}
				close(statement, null);
			}

			// add payments to value object
			contributionDetail.setSubmissionPaymentItem(getPaymentOnlySubmission(
					submissionId, contractId,
                    GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR,
                    GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_TRANSFER));

			// add the allowable money sources for the contract
			contributionDetail.setContractMoneySources(loadMoneySourceIDs(new Integer(contractId)));

			// add money types to value object
			getMoneyTypes(connection, new Integer(submissionId), contractId, contributionDetail);

			// add participants and miscellaneous totals to value object
			getParticipantData(connection, new Integer(submissionId), contractId,
					contributionDetail, processStatusCode);

			contributionDetail.setParticipantSortOption(getParticipantSortOption(contractId));

			setLoanFeature(contributionDetail);

			// add payment errors to value object
			getPaymentErrors(contributionDetail);

			setUserName(contributionDetail);

			setLockedByType(contributionDetail);

		} finally {
			close(statement, connection);
		}

		logger.debug("exit <- getContributionDetails");

		return contributionDetail;

	}

    public static VestingDetailItem getVestingDetails(int submissionId,
            int contractId, ReportCriteria criteria) throws SystemException {

        logger.debug("entry -> getVestingDetails");
        logger.debug("submissionId -> " + submissionId);
        logger.debug("contractId -> " + contractId);

        // gather basic data from the submission history view
        SubmissionHistoryItem submissionCase = null;
        String processStatusCode = EMPTY_STRING;

        StringBuffer query = new StringBuffer(SQL_HISTORY_SELECT);
        query.append(AND_SUBMISSION_NUMBER_CLAUSE);
        query.append(AND_TYPE_CLAUSE);

        SelectBeanListQueryHandler handler = new SelectBeanListQueryHandler(
                SUBMISSION_DATA_SOURCE_NAME, query.toString(),
                SubmissionHistoryItem.class);

        try {
            logger.debug("Executing Prepared SQL Statment: " + query.toString());
            List list = (List) handler.select(new Object[] {
                    new Integer(contractId), new BigDecimal(String.valueOf(submissionId)),
                    GFTUploadDetail.SUBMISSION_TYPE_VESTING});

            if (list == null || list.size() == 0) {
                return null;
            }

            submissionCase = (SubmissionHistoryItem) list.get(0);

            // populate the lock object
            Lock lock = new Lock();
            lock.setUserId(submissionCase.getLockUserId());
            lock.setLockTS(submissionCase.getLockTS());
            submissionCase.setLock(lock);

        } catch (DAOException e) {
            throw handleDAOException(e, className, "getVestingDetails",
                    "Problem occurred prepared call: " + query
                            + " with submissionId: " + submissionId
                            + " contractNumber: " + contractId);
        }

        // get contract name
        String contractName = EMPTY_STRING;
        try {
            SelectSingleValueQueryHandler handler4 = new SelectSingleValueQueryHandler(
                    SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_NAME,
                    String.class);
            contractName = (String) handler4
                    .select(new Object[] { new Integer(submissionId) });
        } catch (DAOException e) {
            throw handleDAOException(e, className, "getVestingDetails",
                    "Problem occurred in prepared call: " + SQL_SELECT_CONTRACT_NAME +
                    " with submissionId " + submissionId);
        }

        // build basic value object
        VestingDetailItem  vestingDetail = new VestingDetailItem(
                        submissionCase.getSubmitterID(),
                        submissionCase.getSubmitterType(),
                        submissionCase.getSubmitterName(),
                        submissionCase.getSubmissionId(),
                        submissionCase.getSubmissionDate(),
                        submissionCase.getType(),
                        submissionCase.getSystemStatus(),
                        submissionCase.getApplicationCode(),
                        new Integer(contractId),
                        contractName,
                        submissionCase.getTpaSystemName(),
                        submissionCase.getTpaSubmissionType(),
                        submissionCase.getTpaVersionNo(),
                        0,
                        submissionCase.getLock());
        vestingDetail.setTransmissionId(submissionId);
        vestingDetail.setContractId(submissionCase.getContractId());
        vestingDetail.setSubmissionId(submissionCase.getSubmissionId());
        vestingDetail.setSubmitterEmail(submissionCase.getSubmitterEmail());
        processStatusCode = submissionCase.getSystemStatus();

        Connection connection = null;
        try {
            connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);

            // add error details from submission_case table
            getErrorDetails(connection, submissionId, contractId, vestingDetail);
            
            // get csf value at time of submission
            getHistoricalVestingCSF(contractId, vestingDetail);

            // add money types to value object
            getMoneyTypes(connection, new Integer(submissionId), contractId, vestingDetail);

            // add participants to value object
            getParticipantData(connection, new Integer(submissionId), contractId,
                    vestingDetail, processStatusCode, criteria);

            vestingDetail.setParticipantSortOption(getParticipantSortOption(contractId));

            setUserName(vestingDetail);

            setLockedByType(vestingDetail);

        } catch (SQLException e) {
            throw new SystemException(e, className,
                    "getVestingDetails",
                    "Problem occurred when getting connection "
                            + " with submissionNumber: " + submissionId
                            + " contractNumber: " + contractId);
        } catch (ContractDoesNotExistException ce) {
            throw new SystemException(ce, className,
                    "getVestingDetails",
                    "Problem occurred when retrieving historical Vesting CSF "
                            + " for contractNumber: " + contractId);
        } finally {
            close(null,connection);
        }

        logger.debug("exit <- getVestingDetails");

        return vestingDetail;

    }

	private static void getMoneyTypes(Connection connection,
			Integer submissionId, int contractId,
			ContributionDetailItem contributionDetail) throws SystemException {

		Connection csdbConnection = null;
		try {
			csdbConnection = getDefaultConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
		} catch (SQLException e) {
			try {
				if (csdbConnection != null) csdbConnection.close();
			} catch (SQLException e2) {
				logger.error(className + "getMoneyTypes() " +
						"Problem occurred closing csdb connection", e2);
			}
			throw new SystemException(e, className, "getMoneyTypes",
					"Problem occurred getting csdb connection");
		}

        Set validMoneyTypes = DomainDAO.getValidMoneyTypesForMoneySource(contributionDetail
                .getMoneySourceID());

        List contractMoneyTypes = ContractDAO.getContractMoneyTypesFilteredByValidMoneyTypes(
                new Integer(contractId), true, validMoneyTypes, true);

		// sort them according to the odd sorting algorithm
		// required for display
		Collections.sort(contractMoneyTypes);
		contributionDetail.setContractMoneyTypes(contractMoneyTypes);

		// retrieve all money types for allocation and put them in a map with a
		// list of occurrences (empty to start)
		PreparedStatement statement = null;
		ResultSet rs = null;
		List columnHeaderMap = new ArrayList();
		Map allocationMoneyTypes = new HashMap();
		List allocationMoneyTypeOccurrences;

		try {
			statement = connection
					.prepareStatement(SQL_ALLOCATION_MONEY_TYPE_SELECT);
			statement.setInt(1, submissionId.intValue());
			statement.setInt(2, contractId);

			rs = statement.executeQuery();
			while (rs.next()) {
				String moneyTypeId = rs.getString("moneyTypeId").trim();
				allocationMoneyTypeOccurrences = new ArrayList();
				allocationMoneyTypes.put(moneyTypeId,
						allocationMoneyTypeOccurrences);

			}
		} catch (SQLException e) {
			throw new SystemException(e, className, "getContributionDetails",
					"Problem occurred in prepared call: "
							+ SQL_ALLOCATION_MONEY_TYPE_SELECT
							+ " with submissionNumber: " + submissionId);

		} finally {
			try {
				if (rs != null)
					rs.close();
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"getContributionDetails",
						"Problem occurred closing result set or statement");
			}
		}

		String moneyTypeId;
		int recordNumberCount;
		Integer moneyTypeCount;
		Map moneyTypeCounts = new HashMap();

		try {
			statement = connection.prepareStatement(SQL_MONEY_TYPE_COUNT_SELECT);
			statement.setInt(1, submissionId.intValue());
			statement.setInt(2, contractId);

			rs = statement.executeQuery();
			while (rs.next()) {
				moneyTypeId = rs.getString("moneyTypeId").trim();
				moneyTypeCount = (Integer)moneyTypeCounts.get(moneyTypeId);
				recordNumberCount = rs.getInt("count");
				if (null == moneyTypeCount || recordNumberCount > moneyTypeCount.intValue()) {
					moneyTypeCounts.put(moneyTypeId, new Integer(recordNumberCount));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, className, "getContributionDetails",
					"Problem occurred in prepared call: "
					+ SQL_MONEY_TYPE_COUNT_SELECT
					+ " with submissionNumber: " + submissionId);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (statement != null)
					statement.close();
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"getContributionDetails",
						"Problem occurred closing result set or statement");
			}
		}
		for (Iterator itr = moneyTypeCounts.keySet().iterator(); itr.hasNext(); ) {
			moneyTypeId = (String)itr.next();
			moneyTypeCount = (Integer)moneyTypeCounts.get(moneyTypeId);
			if (moneyTypeCount != null) {
				allocationMoneyTypeOccurrences = (List)allocationMoneyTypes.get(moneyTypeId);
				for (int k = 0; k < moneyTypeCount.intValue(); k++) {
					allocationMoneyTypeOccurrences.add(new Integer(k).toString().trim());
				}
			}
		}

		if (allocationMoneyTypes.size() == 0) {
			// if there were no money types in the allocation table, see if any money types were saved in the submission money type table
			try {
				statement = connection.prepareStatement(SQL_SELECT_SUBMISSION_CONTRACT_MONEY_TYPES);
				statement.setInt(1, submissionId.intValue());
				statement.setInt(2, contractId);

				rs = statement.executeQuery();
				while (rs.next()) {
					moneyTypeId = rs.getString("moneyTypeId").trim();
					allocationMoneyTypeOccurrences = (List) allocationMoneyTypes.get(moneyTypeId);
					if (null == allocationMoneyTypeOccurrences) {
						allocationMoneyTypeOccurrences = new ArrayList();
					}
					allocationMoneyTypeOccurrences.add(rs.getString("occurrenceNo"));
					allocationMoneyTypes.put(moneyTypeId,
							allocationMoneyTypeOccurrences);

				}

			} catch (SQLException e) {
				throw new SystemException(e, className, "getContributionDetails",
						"Problem occurred in prepared call: "
						+ SQL_SELECT_SUBMISSION_CONTRACT_MONEY_TYPES
						+ " with submissionNumber: " + submissionId
						+ " and contractId: " + contractId);

			} finally {
				try {
					if (rs != null)
						rs.close();
					if (statement != null)
						statement.close();
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"getContributionDetails",
					"Problem occurred closing result set or statement");
				}
			}
		}

		// for each contract money type that matches an allocation money type
		// and is valid for the money source
		// create a column header entry for each occurrence
		MoneyTypeVO moneyType = null;
		for (Iterator itr = contractMoneyTypes.iterator(); itr.hasNext();) {
			moneyType = (MoneyTypeVO) itr.next();
			moneyTypeId = moneyType.getId().trim();
			allocationMoneyTypeOccurrences = (List) allocationMoneyTypes
					.get(moneyTypeId);
			if (null != allocationMoneyTypeOccurrences) {
				if ( validMoneyTypes.size() == 0 || validMoneyTypes.contains(moneyTypeId)) {
					for (Iterator aitr = allocationMoneyTypeOccurrences
							.iterator(); aitr.hasNext();) {
						String occurrence = (String) aitr.next();
						columnHeaderMap.add(new MoneyTypeHeader(moneyTypeId,
								occurrence.toString().trim(), moneyType));
					}
				}
				// this is required to allow the logic of the next iterator to
				// be done
				allocationMoneyTypes.remove(moneyTypeId);
			}
		}

		// If there are any money types left in the allocation, then these must
		// no longer be valid
		// for the contract. In this case, create a fake value ojbect and add
		// the entry(ies) to the
		// column header map. This should rarely happen.
		for (Iterator aitr = allocationMoneyTypes.keySet().iterator(); aitr
				.hasNext();) {
			moneyTypeId = (String) aitr.next();
			allocationMoneyTypeOccurrences = (List) allocationMoneyTypes
					.get(moneyTypeId);
			if (null != allocationMoneyTypeOccurrences) {
				moneyType = new MoneyTypeVO();
				moneyType.setAliasId(moneyTypeId);
				moneyType.setContractShortName(moneyTypeId);
				moneyType.setId(moneyTypeId);
				for (Iterator oitr = allocationMoneyTypeOccurrences.iterator(); oitr
						.hasNext();) {
					String occurrence = (String) oitr.next();
					columnHeaderMap.add(new MoneyTypeHeader(moneyTypeId,
							occurrence, moneyType));
				}
			}
		}

		contributionDetail.setAllocationMoneyTypes(columnHeaderMap);

	}
    
    
    private static void getHistoricalVestingCSF(int contractId, VestingDetailItem vestingDetail) 
        throws SystemException, ContractDoesNotExistException {

        if ((vestingDetail != null) && (vestingDetail.getSubmissionDate() != null)) {
            ContractServiceFeature csf = ContractServiceFeatureDAO.getContractServiceFeatureByTimestamp(contractId, 
                            new Timestamp(vestingDetail.getSubmissionDate().getTime()), 
                            ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
            
            if (csf != null) {
                vestingDetail.setVestingCSF(csf.getValue());
            }
        }
    }

    private static void getErrorDetails(Connection connection,
            int submissionId, int contractId,
            VestingDetailItem vestingDetail) throws SystemException {

        // get error message and error level from submission_case
        String errorMessage = EMPTY_STRING;
        String errorLevel = EMPTY_STRING;
        String errorCondString = EMPTY_STRING;
        Timestamp lastUpdatedTimestamp = null;
        
        
        PreparedStatement submissionCaseStatement = null;
        try {
            submissionCaseStatement = connection
                    .prepareStatement(SQL_VESTING_SUBMISSION_CASE_SELECT);
            submissionCaseStatement.setInt(1, contractId);
            submissionCaseStatement.setInt(2, submissionId);
            submissionCaseStatement.setString(3, GFTUploadDetail.SUBMISSION_TYPE_VESTING);

            ResultSet rs = submissionCaseStatement.executeQuery();

            while (rs.next()) {
                errorMessage = rs.getString("errorMessage");
                errorLevel = rs.getString("errorLevel");
                errorCondString = rs.getString("errorCondString");
                lastUpdatedTimestamp = rs.getTimestamp("lastUpdatedTimestamp");
                
                vestingDetail.setErrorMessage(errorMessage);
                vestingDetail.setErrorLevel(errorLevel);
                vestingDetail.setErrorCondString(errorCondString);
                if (lastUpdatedTimestamp != null) {
                    vestingDetail.setLastUpdatedDate(new Date(lastUpdatedTimestamp.getTime()));
                }
                
                
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, className,
                    "getVestingDetails",
                    "Problem occurred in prepared call: "
                            + SQL_VESTING_SUBMISSION_CASE_SELECT
                            + " for contractId " + contractId
                            + " and submissionId " + submissionId);
        }


    }
    /**
     * Get Long Term Part Time Sumission error details
     * 
     * @param connection
     * @param submissionId
     * @param contractId
     * @param longTermPartTimeDetail
     * @throws SystemException
     */
    private static void getLTPTErrorDetails(Connection connection,
            int submissionId, int contractId,
            LongTermPartTimeDetailItem longTermPartTimeDetail) throws SystemException {

        // get error message and error level from submission_case
        String errorMessage = EMPTY_STRING;
        String errorLevel = EMPTY_STRING;
        String errorCondString = EMPTY_STRING;
        Timestamp lastUpdatedTimestamp = null;
        
        
        PreparedStatement submissionCaseStatement = null;
        try {
            submissionCaseStatement = connection
                    .prepareStatement(SQL_LTPT_SUBMISSION_CASE_SELECT);
            submissionCaseStatement.setInt(1, contractId);
            submissionCaseStatement.setInt(2, submissionId);
            submissionCaseStatement.setString(3, SUBMISSION_TYPE_LTPT);

            ResultSet rs = submissionCaseStatement.executeQuery();

            while (rs.next()) {
                errorMessage = rs.getString("errorMessage");
                errorLevel = rs.getString("errorLevel");
                errorCondString = rs.getString("errorCondString");
                lastUpdatedTimestamp = rs.getTimestamp("lastUpdatedTimestamp");
                
                longTermPartTimeDetail.setErrorMessage(errorMessage);
                longTermPartTimeDetail.setErrorLevel(errorLevel);
                longTermPartTimeDetail.setErrorCondString(errorCondString);
                if (lastUpdatedTimestamp != null) {
                	longTermPartTimeDetail.setLastUpdatedDate(new Date(lastUpdatedTimestamp.getTime()));
                }
                
                
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, className,
                    "getVestingDetails",
                    "Problem occurred in prepared call: "
                            + SQL_LTPT_SUBMISSION_CASE_SELECT
                            + " for contractId " + contractId
                            + " and submissionId " + submissionId);
        }


    }

    private static void getMoneyTypes(Connection connection,
            Integer submissionId, int contractId,
            VestingDetailItem vestingDetail) throws SystemException {

        Connection csdbConnection = null;
        try {
            csdbConnection = getDefaultConnection(className,
                    CUSTOMER_DATA_SOURCE_NAME);
        } catch (SQLException e) {
            try {
                if (csdbConnection != null) csdbConnection.close();
            } catch (SQLException e2) {
                logger.error(className + "getMoneyTypes() " +
                        "Problem occurred closing csdb connection", e2);
            }
            throw new SystemException(e, className, "getMoneyTypes",
                    "Problem occurred getting csdb connection");
        }

        List contractMoneyTypes = CensusVestingDAO.getContractMoneyTypeVOs(
                                  new Integer(contractId));

        // sort them according to the odd sorting algorithm
        // required for vesting
        Collections.sort(contractMoneyTypes,new VestingMoneyTypeComparator());
        vestingDetail.setContractMoneyTypes(contractMoneyTypes);

        // retrieve all money types for allocation and put them in a map with a
        // list of occurrences (empty to start)
        PreparedStatement statement = null;
        ResultSet rs = null;
        List columnHeaderMap = new ArrayList();
        Map percentageMoneyTypes = new TreeMap();
        List percentageMoneyTypeOccurrences;

        try {
            statement = connection
                    .prepareStatement(SQL_VESTING_MONEY_TYPE_SELECT);
            statement.setInt(1, submissionId.intValue());
            statement.setInt(2, contractId);

            rs = statement.executeQuery();
            while (rs.next()) {
                String moneyTypeId = rs.getString("moneyTypeId").trim();
                percentageMoneyTypeOccurrences = new ArrayList();
                percentageMoneyTypes.put(moneyTypeId,
                        percentageMoneyTypeOccurrences);

            }
        } catch (SQLException e) {
            throw new SystemException(e, className, "getVestingDetails",
                    "Problem occurred in prepared call: "
                            + SQL_VESTING_MONEY_TYPE_SELECT
                            + " with submissionNumber: " + submissionId);

        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                throw new SystemException(e, className,
                        "getVestingDetails",
                        "Problem occurred closing result set or statement");
            }
        }

        String moneyTypeId;
        int recordNumberCount;
        Integer moneyTypeCount;
        Map moneyTypeCounts = new HashMap();

        try {
            statement = connection.prepareStatement(SQL_VESTING_MONEY_TYPE_COUNT_SELECT);
            statement.setInt(1, submissionId.intValue());
            statement.setInt(2, contractId);

            rs = statement.executeQuery();
            while (rs.next()) {
                moneyTypeId = rs.getString("moneyTypeId").trim();
                moneyTypeCount = (Integer)moneyTypeCounts.get(moneyTypeId);
                recordNumberCount = rs.getInt("count");
                if (null == moneyTypeCount || recordNumberCount > moneyTypeCount.intValue()) {
                    moneyTypeCounts.put(moneyTypeId, new Integer(recordNumberCount));
                }
            }
        } catch (SQLException e) {
            throw new SystemException(e, className, "getVestingDetails",
                    "Problem occurred in prepared call: "
                    + SQL_VESTING_MONEY_TYPE_COUNT_SELECT
                    + " with submissionNumber: " + submissionId);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
            } catch (SQLException e) {
                throw new SystemException(e, className,
                        "getVestingDetails",
                        "Problem occurred closing result set or statement");
            }
        }
        for (Iterator itr = moneyTypeCounts.keySet().iterator(); itr.hasNext(); ) {
            moneyTypeId = (String)itr.next();
            moneyTypeCount = (Integer)moneyTypeCounts.get(moneyTypeId);
            if (moneyTypeCount != null) {
                percentageMoneyTypeOccurrences = (List)percentageMoneyTypes.get(moneyTypeId);
                for (int k = 0; k < moneyTypeCount.intValue(); k++) {
                    percentageMoneyTypeOccurrences.add(new Integer(k).toString().trim());
                }
            }
        }

        // for each contract money type that matches a vesting money type
        // create a column header entry for each occurrence
        MoneyTypeVO moneyType = null;
        String moneyTypeIdentifier = null;
        for (Iterator itr = contractMoneyTypes.iterator(); itr.hasNext();) {
            moneyType = (MoneyTypeVO) itr.next();
            
            // if submitted file is a standard file, use the moneyTypeShortName as an identifier, 
            // else if submitted file is a TPA vendor file, use the moneyTypeTEDCode as an identifier
            moneyTypeIdentifier = moneyType.getVestingIdentifier(vestingDetail.getTpaSystemName());
            
            percentageMoneyTypeOccurrences = (List) percentageMoneyTypes
                    .get(moneyTypeIdentifier);
            if (null != percentageMoneyTypeOccurrences) {

                    for (Iterator aitr = percentageMoneyTypeOccurrences
                            .iterator(); aitr.hasNext();) {
                        String occurrence = (String) aitr.next();
                        columnHeaderMap.add(new MoneyTypeHeader(moneyTypeIdentifier,
                                occurrence.toString().trim(), moneyType));
                    }

                // this is required to allow the logic of the next iterator to be done
                percentageMoneyTypes.remove(moneyTypeIdentifier);
            }
        }

        // If there are any money types left in the allocation, then these must
        // be invalid money types for the contract.
        // In this case, create a fake value ojbect and add
        // the entry(ies) to the column header map in no particular order.
        for (Iterator aitr = percentageMoneyTypes.keySet().iterator(); aitr.hasNext();) {
            moneyTypeIdentifier = (String) aitr.next();
            percentageMoneyTypeOccurrences = (List) percentageMoneyTypes
                    .get(moneyTypeIdentifier);
            if (null != percentageMoneyTypeOccurrences) {
                moneyType = new MoneyTypeVO();
                moneyType.setAliasId(moneyTypeIdentifier);
                moneyType.setId(moneyTypeIdentifier);
                
                if (StringUtils.isEmpty(vestingDetail.getTpaSystemName())) {
                    // standard vesting file, the identifier is the contract short name
                    moneyType.setContractShortName(moneyTypeIdentifier);
                } else {
                    // vendor vesting file, the identifier is the ted code
                    moneyType.setTedCode(moneyTypeIdentifier);
                }
                moneyType.setFullyVested(NO);
                
                for (Iterator oitr = percentageMoneyTypeOccurrences.iterator(); oitr
                        .hasNext();) {
                    String occurrence = (String) oitr.next();
                    columnHeaderMap.add(new MoneyTypeHeader(moneyTypeIdentifier,
                            occurrence, moneyType));
                }
            }
        }

        vestingDetail.setPercentageMoneyTypes(columnHeaderMap);

    }
    
	private static void getParticipantData(Connection connection, Integer submissionId, int contractId,
			ContributionDetailItem contributionDetail, String processStatusCode)
				throws SystemException {

		int participantCount = 0;
		int participantsWithAmountsCount = 0;
		int maximumNumberOfLoans = 0;
		BigDecimal employeeContributionTotal = BigDecimal.ZERO;
		BigDecimal employerContributionTotal = BigDecimal.ZERO;
		List participants = new ArrayList();
		Map participantOccurrences = null;
		Map participantOccurrenceAmounts = null;
		SubmissionParticipant participant = null;
		SubmissionParticipant allocationParticipant = null;
		SubmissionParticipant repaymentParticipant = null;
		SortedMap allocations = null;
		SortedMap loanRepayments = null;
		PreparedStatement statement = null;
		PreparedStatement allocationStatement = null;
		PreparedStatement loanRepaymentStatement = null;
		ResultSet rs = null;
		String loanSelectString = SQL_LOAN_REPAYMENT_AMOUNTS;
		int recordNumber = 0;
		// only select zero repayments if this submission isn't in
		// copied/created status
		if (!COPIED_STATUS.equals(processStatusCode)
				&& !DRAFT_STATUS.equals(processStatusCode)) {
			loanSelectString = loanSelectString
					+ LOAN_AMT_GREATER_THAN_ZERO_CLAUSE;
		}
		loanSelectString = loanSelectString + LOAN_ORDER_BY_CLAUSE;
		try {
			String participantSelect = SQL_PARTICIPANT_SELECT + ID_ORDER_CLAUSE;
			statement = connection.prepareStatement(participantSelect);
			statement.setInt(1, submissionId.intValue());
			statement.setInt(2, contractId);

			rs = statement.executeQuery();

			while (rs.next()) {
				if (logger.isDebugEnabled() && participantCount > 0 && participantCount % 100 == 0)
					logger.debug("retrieving participant: " + participantCount);

				participantOccurrences = new TreeMap();
				participantOccurrenceAmounts = new TreeMap();
				participant = new SubmissionParticipant(rs
						.getString("employerDesignatedId"), rs
						.getString("fullName"), rs.getInt("sourceRecordNo"),
						new TreeMap(), new TreeMap());
				participantOccurrences.put(new Integer(rs.getInt("sourceRecordNo")), participant);
				participantOccurrenceAmounts.put(new Integer(rs.getInt("sourceRecordNo")), BigDecimal.ZERO);
				// save participant level errors, if any
				String errorCondString = rs.getString("errorCondString");
				if (null != errorCondString
						&& !ERROR_COND_STRING_OK.equals(errorCondString)
						&& !EMPTY_STRING.equals(errorCondString)) {
					recordNumber = rs.getInt("sourceRecordNo");
					if (recordNumber < 0) recordNumber = recordNumber * -1;
					contributionDetail.getReportDataErrors().addErrors(
							SubmissionErrorHelper.parseErrorConditionString(errorCondString,
									recordNumber));
				}

				try {
					allocationStatement = connection
							.prepareStatement(SQL_ALLOCATION_SELECT);
					allocationStatement.setInt(1, submissionId.intValue());
					allocationStatement.setInt(2, contractId);
					allocationStatement.setString(3, rs
							.getString("employerDesignatedId"));

					ResultSet rs2 = allocationStatement.executeQuery();

					while (rs2.next()) {
						Integer allocationRecordNo = new Integer(rs2.getInt("sourceRecordNo"));
						allocationParticipant = (SubmissionParticipant)participantOccurrences.get(allocationRecordNo);
						if (null == allocationParticipant) {
							Iterator itr = participantOccurrences.values().iterator();
							participant = (SubmissionParticipant)itr.next();
							allocationParticipant = new SubmissionParticipant(participant.getIdentifier(),
									participant.getName(), rs2.getInt("sourceRecordNo"),
									new TreeMap(), new TreeMap());
							participantOccurrences.put(allocationRecordNo, allocationParticipant);
							participantOccurrenceAmounts.put(allocationRecordNo, BigDecimal.ZERO);
						}

						allocations = allocationParticipant.getMoneyTypeAmounts();
						String occurrenceNo = "0";
						boolean misMatchFound = false;
						for (int i = 0; i < 99 && misMatchFound == false; i++) {
							if (!allocations.containsKey(rs2.getString("moneyTypeId").trim()
									+ "/" + String.valueOf(i))) {
								misMatchFound = true;
								occurrenceNo = String.valueOf(i);
							}
						}
						allocations.put(rs2.getString("moneyTypeId").trim()
								+ "/" + occurrenceNo, rs2.getBigDecimal("allocationAmount"));
						if (EMPLOYEE_MONEY_TYPE_PREFIX.equals(rs2.getString(
								"moneyTypeId").substring(0, 2))) {
							employeeContributionTotal = employeeContributionTotal
									.add(rs2.getBigDecimal("allocationAmount"));
						} else {
							employerContributionTotal = employerContributionTotal
									.add(rs2.getBigDecimal("allocationAmount"));
						}
						errorCondString = rs2.getString("errorCondString");
						if (null != errorCondString
								&& !ERROR_COND_STRING_OK
										.equals(errorCondString)
								&& !EMPTY_STRING.equals(errorCondString)) {
							recordNumber = rs2.getInt("sourceRecordNo");
							if (recordNumber < 0) recordNumber = recordNumber * -1;
							contributionDetail.getReportDataErrors().addErrors(
									SubmissionErrorHelper.parseErrorConditionString(errorCondString, recordNumber));
						}
						BigDecimal amount = (BigDecimal)participantOccurrenceAmounts.get(allocationRecordNo);
						participantOccurrenceAmounts.put(allocationRecordNo, amount.add(rs2.getBigDecimal("allocationAmount").abs()));

					}
					rs2.close();

				} catch (SQLException e) {

					throw new SystemException(e, className,
							"getPartcipantData",
							"Problem occurred prepared call: "
									+ SQL_ALLOCATION_SELECT
									+ ALLOCATION_AMOUNT_NOT_ZERO
									+ " with submissionNumber: " + submissionId
									+ " contractNumber: " + contractId);
				} finally {
					close(allocationStatement, null);
				}

				try {
					loanRepaymentStatement = connection
							.prepareStatement(loanSelectString);
					loanRepaymentStatement.setInt(1, submissionId.intValue());
					loanRepaymentStatement.setInt(2, contractId);
					loanRepaymentStatement.setString(3, rs
							.getString("employerDesignatedId"));

					ResultSet rs3 = loanRepaymentStatement.executeQuery();

					while (rs3.next()) {
						Integer repaymentRecordNo = new Integer(rs3.getInt("sourceRecordNo"));
						repaymentParticipant = (SubmissionParticipant)participantOccurrences.get(repaymentRecordNo);
						if (null == repaymentParticipant) {
							Iterator itr = participantOccurrences.values().iterator();
							participant = (SubmissionParticipant)itr.next();
							repaymentParticipant = new SubmissionParticipant(participant.getIdentifier(),
									participant.getName(), rs3.getInt("sourceRecordNo"),
									new TreeMap(), new TreeMap());
							participantOccurrences.put(repaymentRecordNo, repaymentParticipant);
							participantOccurrenceAmounts.put(repaymentRecordNo, BigDecimal.ZERO);
						}

						loanRepayments = repaymentParticipant.getLoanAmounts();
						String occurrenceNo = "0";
						boolean misMatchFound = false;
						for (int i = 0; i < 99 && misMatchFound == false; i++) {
							if (!loanRepayments.containsKey(rs3.getString("loanId").trim()
									+ "/" + String.valueOf(i))) {
								misMatchFound = true;
								occurrenceNo = String.valueOf(i);
							}
						}

						loanRepayments.put(rs3.getInt("loanId") + "/"
								+ occurrenceNo, rs3.getBigDecimal("loanRepaymentAmount"));
						errorCondString = rs3.getString("errorCondString");
						if (null != errorCondString
								&& !ERROR_COND_STRING_OK
										.equals(errorCondString)
								&& !EMPTY_STRING.equals(errorCondString)) {
							recordNumber = rs3.getInt("sourceRecordNo");
							if (recordNumber < 0) recordNumber = recordNumber * -1;
							contributionDetail.getReportDataErrors().addErrors(
									SubmissionErrorHelper.parseErrorConditionString(
											errorCondString, recordNumber));
						}
						BigDecimal amount = (BigDecimal)participantOccurrenceAmounts.get(repaymentRecordNo);
						participantOccurrenceAmounts.put(repaymentRecordNo, amount.add(rs3.getBigDecimal("loanRepaymentAmount")));

					}
					rs3.close();
				} catch (SQLException e) {

					throw new SystemException(e, className,
							"getPartcipantData",
							"Problem occurred prepared call: "
									+ SQL_LOAN_REPAYMENT_AMOUNTS
									+ " with submissionNumber: " + submissionId
									+ " contractNumber: " + contractId);
				} finally {
					close(loanRepaymentStatement, null);
				}
				for (Iterator pitr = participantOccurrences.values().iterator(); pitr.hasNext(); ) {
					participant = (SubmissionParticipant)pitr.next();
					if (participant.getLoanAmounts().size() > maximumNumberOfLoans) {
						maximumNumberOfLoans = participant.getLoanAmounts().size();
					}
					participants.add(participant);
				}
				for (Iterator amountItr = participantOccurrenceAmounts.values().iterator(); amountItr.hasNext(); ) {
					BigDecimal amount = (BigDecimal)amountItr.next();
					if (amount.compareTo(BigDecimal.ZERO) > 0) {
						participantsWithAmountsCount++;
					}
				}
				participantCount++;
			}
		} catch (SQLException e) {

			throw new SystemException(e, className, "getPartcipantData",
					"Problem occurred prepared call: " + SQL_PARTICIPANT_SELECT
							+ " with submissionNumber: " + submissionId
							+ " contractNumber: " + contractId);
		} finally {
			try {
				if (null != rs) rs.close();
			} catch (SQLException e) {
				throw new SystemException(e, className, "getPartcipantData",
						"Problem occurred closing result set for: " + SQL_PARTICIPANT_SELECT
								+ " with submissionNumber: " + submissionId
								+ " contractNumber: " + contractId);
			}
			close(statement, null);

		}
		contributionDetail.setMaximumNumberOfLoans(maximumNumberOfLoans);
		contributionDetail.setEmployeeContributionTotal(employeeContributionTotal);
		contributionDetail.setEmployerContributionTotal(employerContributionTotal);
		contributionDetail.setSubmissionParticipants(participants);

		// for display count only those participants with allocations or loan
		// repayments
		contributionDetail.setNumberOfParticipants(participantsWithAmountsCount);
	}

    private static String createSortPhrase(final ReportCriteria criteria) {
        StringBuffer result = new StringBuffer();
        Iterator sorts = criteria.getSorts().iterator();
        String sortDirection = null;

        for (int i = 0; sorts.hasNext(); i++) {
            ReportSort sort = (ReportSort) sorts.next();

            if (i==0) sortDirection = sort.getSortDirection();

            if ("name".equals(sort.getSortField())) {
                result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
                result.append(sortDirection);
                result.append(", FIRST_NAME ").append(sortDirection);
                result.append(", MIDDLE_INITIAL ").append(sortDirection);
            } else {
                result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
                // source record number is always in ASC order, unless the first sort
                if (VestingDetailsReportData.SORT_RECORD_NUMBER.equals(sort.getSortField()) && (i!=0)) {
                    result.append(sort.ASC_DIRECTION);
                } else {
                    result.append(sort.getSortDirection());
                }
            }

            result.append(',');
        }

        if (result.length() > 0) {
         /*
          * Delete the last ','
          */
            result.deleteCharAt(result.length() - 1);
        } else {
            result.append(SQL_VESTING_PARTICIPANT_DEFAULT_ORDER_CLAUSE);
        }
        result.insert(0, " ORDER BY ");
        return result.toString();
   }

    private static String createLTPTSortPhrase(final ReportCriteria criteria) {
        StringBuffer result = new StringBuffer();
        Iterator sorts = criteria.getSorts().iterator();
        String sortDirection = null;

        for (int i = 0; sorts.hasNext(); i++) {
            ReportSort sort = (ReportSort) sorts.next();

            if (i==0) sortDirection = sort.getSortDirection();

            if ("name".equals(sort.getSortField())) {
                result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
                result.append(sortDirection);
                result.append(", FIRST_NAME ").append(sortDirection);
                result.append(", MIDDLE_INITIAL ").append(sortDirection);
            } else {
                result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
                // source record number is always in ASC order, unless the first sort
                if (LongTermPartTimeDetailsReportData.SORT_RECORD_NUMBER.equals(sort.getSortField()) && (i!=0)) {
                    result.append(sort.ASC_DIRECTION);
                } else {
                    result.append(sort.getSortDirection());
                }
            }

            result.append(',');
        }

        if (result.length() > 0) {
         /*
          * Delete the last ','
          */
            result.deleteCharAt(result.length() - 1);
        } else {
            result.append(SQL_LTPT_PARTICIPANT_DEFAULT_ORDER_CLAUSE);
        }
        result.insert(0, " ORDER BY ");
        return result.toString();
   }
    
    private static void getParticipantData(Connection connection, Integer submissionId, int contractId,
            VestingDetailItem vestingDetail, String processStatusCode, ReportCriteria criteria)
                throws SystemException {

        int participantCount = 0;
        List participants = new ArrayList();
        Map participantOccurrences = null;
        VestingParticipant participant = null;
        VestingParticipant percentageParticipant = null;
        SortedMap percentages = null;
        SortedMap errors = null;
        PreparedStatement statement = null;
        PreparedStatement percentageStatement = null;
        List<SubmissionError> recordErrorsList = null;

        String participantSelect = "";
        ResultSet rs = null;

        String errorCondString = "";

        try {

            // create sort clause
            String VESTING_SORT_CLAUSE = createSortPhrase(criteria);
            String VESTING_JOIN_CLAUSE = VESTING_SORT_CLAUSE.contains(SQL_VESTING_PARTICIPANT_DEFAULT_ORDER_CLAUSE)?
                                 SQL_VESTING_PARTICIPANT_JOIN_CLAUSE : "";

            participantSelect = SQL_VESTING_PARTICIPANT_SELECT +
                                VESTING_JOIN_CLAUSE +
                                SQL_VESTING_PARTICIPANT_WHERE_CLAUSE +
                                VESTING_SORT_CLAUSE;

            statement = connection.prepareStatement(participantSelect);
            statement.setInt(1, submissionId.intValue());
            statement.setInt(2, contractId);
            if (!VESTING_JOIN_CLAUSE.equals("")) {
                statement.setInt(3, submissionId.intValue());
                statement.setInt(4, contractId);
                statement.setInt(5, submissionId.intValue());
                statement.setInt(6, contractId);
            }

            rs = statement.executeQuery();
            int recordNumber = 0;

            while (rs.next()) {
                if (logger.isDebugEnabled() && participantCount > 0)
                    logger.debug("retrieving participant: " + rs.getInt("sourceRecordNo"));

                participantOccurrences = new TreeMap();
                recordErrorsList = new ArrayList<SubmissionError>();

                participant = new VestingParticipant(rs.getString("ssn"),rs.getString("empId"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("middleInitial"),
                        rs.getInt("sourceRecordNo"), rs.getString("errorCondString"), rs.getString("percDate"), 
                        rs.getString("vyos"), rs.getString("vyosDate"), rs.getInt("processStatusCode"), 
                        new TreeMap(), new TreeMap(),rs.getString("applyLTPTCrediting"));

                // save participant level errors, if any
                errorCondString = rs.getString("errorCondString");
                if (null != errorCondString
                        && !ERROR_COND_STRING_OK.equals(errorCondString)
                        && !EMPTY_STRING.equals(errorCondString)) {
                    recordNumber = rs.getInt("sourceRecordNo");
                    if (recordNumber < 0) recordNumber = recordNumber * -1;
                    participant.setErrors(true);
                    recordErrorsList.addAll(
                    SubmissionErrorHelper.parseErrorConditionString(errorCondString, recordNumber));
                }

                participantOccurrences.put(new Integer(rs.getInt("sourceRecordNo")), participant);


                try {
                    percentageStatement = connection
                            .prepareStatement(SQL_VESTING_PERCENTAGE_SELECT);
                    percentageStatement.setInt(1, submissionId.intValue());
                    percentageStatement.setInt(2, contractId);
                    percentageStatement.setInt(3, rs.getInt("sourceRecordNo"));

                    ResultSet rs2 = percentageStatement.executeQuery();

                    while (rs2.next()) {
                        Integer sourceRecordNo = new Integer(rs2.getInt("sourceRecordNo"));
                        percentageParticipant = (VestingParticipant)participantOccurrences.get(sourceRecordNo);
                        if (null == percentageParticipant) {
                            Iterator itr = participantOccurrences.values().iterator();
                            participant = (VestingParticipant)itr.next();
                            percentageParticipant = new VestingParticipant(participant.getSsn(),participant.getEmpId(),
                                    participant.getFirstName(), participant.getLastName(), participant.getMiddleInitial(),
                                    rs2.getInt("sourceRecordNo"), participant.getErrorCondString(), participant.getPercDate(), 
                                    participant.getVyos(), participant.getVyosDate(), participant.getRecordStatus(), 
                                    new TreeMap(), new TreeMap(),participant.getApplyLTPTCrediting());
                            participantOccurrences.put(sourceRecordNo, percentageParticipant);

                        }

                        percentages = percentageParticipant.getMoneyTypePercentages();
                        errors = percentageParticipant.getMoneyTypeErrors();

                        String occurrenceNo = "0";
                        boolean misMatchFound = false;
                        for (int i = 0; i < 99 && misMatchFound == false; i++) {
                            if (!percentages.containsKey(rs2.getString("moneyTypeId").trim()
                                    + "/" + String.valueOf(i))) {
                                misMatchFound = true;
                                occurrenceNo = String.valueOf(i);
                            }
                        }

                        percentages.put(rs2.getString("moneyTypeId").trim()
                                + "/" + occurrenceNo, rs2.getString("percentage"));
                        errors.put(rs2.getString("moneyTypeId").trim()
                                + "/" + occurrenceNo, rs2.getString("errorCondString"));

                        errorCondString = rs2.getString("errorCondString");
                        if (null != errorCondString
                                && !ERROR_COND_STRING_OK
                                        .equals(errorCondString)
                                && !EMPTY_STRING.equals(errorCondString)) {
                            recordNumber = rs2.getInt("sourceRecordNo");
                            if (recordNumber < 0) recordNumber = recordNumber * -1;

                            percentageParticipant.setErrors(true);
                            // the error ER,percentage:VP does not uniquely identify the field in error
                            // concatenating moneyType/occurenceNumber at the end of errorCondString for uniqueness
                            errorCondString = errorCondString + ":" +
                                              rs2.getString("moneyTypeId").trim() + "/" + occurrenceNo;
                            recordErrorsList.addAll(
                            SubmissionErrorHelper.parseErrorConditionString(errorCondString, recordNumber));
                        }

                    }
                    rs2.close();

                } catch (SQLException e) {

                    throw new SystemException(e, className,
                            "getPartcipantData",
                            "Problem occurred prepared call: "
                                    + SQL_VESTING_PERCENTAGE_SELECT
                                    + " with submissionNumber: " + submissionId
                                    + " contractNumber: " + contractId);
                } finally {
                    close(percentageStatement, null);
                }
                
                // sort errors by order of fields
                Collection recordErrorsCollection = (Collection) VestingSubmissionErrorUtil.getInstance().sort(recordErrorsList);
                
                // set errors into vestingDetail
                vestingDetail.getReportDataErrors().addErrors(recordErrorsCollection);

                // count participants
                for (Iterator pitr = participantOccurrences.values().iterator(); pitr.hasNext(); ) {
                    participant = (VestingParticipant)pitr.next();
                    participants.add(participant);
                }

                participantCount++;
            }
            
        } catch (SQLException e) {

            throw new SystemException(e, className, "getPartcipantData",
                    "Problem occurred prepared call: " + participantSelect
                            + " with submissionNumber: " + submissionId
                            + " contractNumber: " + contractId);
        } finally {
            try {
                if (null != rs) rs.close();
            } catch (SQLException e) {
                throw new SystemException(e, className, "getPartcipantData",
                        "Problem occurred closing result set for: " + SQL_VESTING_PERCENTAGE_SELECT
                                + " with submissionNumber: " + submissionId
                                + " contractNumber: " + contractId);
            }
            close(statement, null);

        }

        vestingDetail.setSubmissionParticipants(participants);

        // for display count all participants
        vestingDetail.setNumberOfRecords(participantCount);
    }
    
	private static void getLTPTParticipantData(Connection connection, Integer submissionId, int contractId,
			LongTermPartTimeDetailItem longTermPartTimeDetail, ReportCriteria criteria)
			throws SystemException {

		int participantCount = 0;
		List participants = new ArrayList();
		Map participantOccurrences = null;
		LongTermPartTimeParticipant participant = null;
		PreparedStatement statement = null;
		List<SubmissionError> recordErrorsList = null;

		String participantSelect = "";
		ResultSet rs = null;

		String errorCondString = "";

		try {

			// create sort clause
			String LTPT_SORT_CLAUSE = createLTPTSortPhrase(criteria);
			String LTPT_JOIN_CLAUSE = LTPT_SORT_CLAUSE.contains(SQL_LTPT_PARTICIPANT_DEFAULT_ORDER_CLAUSE)
					? SQL_LONG_TERM_PART_TIME_PARTICIPANT_JOIN_CLAUSE
					: "";

			participantSelect = SQL_LONG_TERM_PART_TIME_PARTICIPANT_SELECT + LTPT_JOIN_CLAUSE
					+ SQL_LONG_TERM_PART_TIME_PARTICIPANT_WHERE_CLAUSE + LTPT_SORT_CLAUSE;

			statement = connection.prepareStatement(participantSelect);
			statement.setInt(1, submissionId.intValue());
			statement.setInt(2, contractId);
			if (!LTPT_JOIN_CLAUSE.equals("")) {
				statement.setInt(3, submissionId.intValue());
				statement.setInt(4, contractId);
			}

			rs = statement.executeQuery();
			int recordNumber = 0;

			while (rs.next()) {
				if (logger.isDebugEnabled() && participantCount > 0)
					logger.debug("retrieving participant: " + rs.getInt("sourceRecordNo"));

				participantOccurrences = new TreeMap();
				recordErrorsList = new ArrayList<SubmissionError>();

				participant = new LongTermPartTimeParticipant(rs.getString("ssn"), rs.getString("firstName"),
						rs.getString("lastName"), rs.getString("middleInitial"), rs.getString("LTPTAssessYr"),
						rs.getInt("sourceRecordNo"), rs.getString("errorCondString"), rs.getInt("processStatusCode"));

				// save participant level errors, if any
				errorCondString = rs.getString("errorCondString");
				if (null != errorCondString && !ERROR_COND_STRING_OK.equals(errorCondString)
						&& !EMPTY_STRING.equals(errorCondString)) {
					recordNumber = rs.getInt("sourceRecordNo");
					if (recordNumber < 0)
						recordNumber = recordNumber * -1;
					participant.setErrors(true);
					recordErrorsList
							.addAll(SubmissionErrorHelper.parseErrorConditionString(errorCondString, recordNumber));
				}

				participantOccurrences.put(new Integer(rs.getInt("sourceRecordNo")), participant);

				// sort errors by order of fields
				Collection recordErrorsCollection = (Collection) LongTermPartTimeSubmissionErrorUtil.getInstance()
						.sort(recordErrorsList);

				// set errors into longTermPartTimeDetail
				longTermPartTimeDetail.getReportDataErrors().addErrors(recordErrorsCollection);

				// count participants
				for (Iterator pitr = participantOccurrences.values().iterator(); pitr.hasNext();) {
					participant = (LongTermPartTimeParticipant) pitr.next();
					participants.add(participant);
				}

				participantCount++;
			}

		} catch (SQLException e) {

			throw new SystemException(e, className, "getLTPTParticipantData", "Problem occurred prepared call: "
					+ participantSelect + " with submissionNumber: " + submissionId + " contractNumber: " + contractId);
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e) {
				throw new SystemException(e, className, "getLTPTParticipantData",
						"Problem occurred closing result set for: " + SQL_LONG_TERM_PART_TIME_PARTICIPANT_SELECT
								+ " with submissionNumber: " + submissionId + " contractNumber: " + contractId);
			}
			close(statement, null);

		}

		longTermPartTimeDetail.setSubmissionParticipants(participants);

		// for display count all participants
		longTermPartTimeDetail.setNumberOfRecords(participantCount);
    }


	private static void getPaymentErrors(ContributionDetailItem contributionDetail)
		throws SystemException {

		String errorCondString = EMPTY_STRING;
		try {
			SelectMultiFieldQueryHandler handler = new SelectMultiFieldQueryHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_SELECT_SUBMISSION_PAYMENT_INSTRUCTION, new Class[] {String.class, Integer.class});
			Object[] result =
				(Object[]) handler.select(new Object[] {contributionDetail.getSubmissionId(),
						contributionDetail.getContractId()});
			if (result != null) {
				errorCondString = (String) result[0];
				if (null != errorCondString && errorCondString.length() > 0 && !ERROR_COND_STRING_OK.equals(errorCondString)) {
					contributionDetail.getReportDataErrors().addErrors(
							SubmissionErrorHelper.parseErrorConditionString(errorCondString, ((Integer) result[1]).intValue()));
				}
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className, "getPaymentErrors",
					"Problem occurred prepared call: " + SQL_SELECT_SUBMISSION_PAYMENT_INSTRUCTION +
					" with submissionNumber: " + contributionDetail.getSubmissionId() +
					" contractNumber: " + contributionDetail.getContractId());
		}
	}

	private static void setUserName(SubmissionHistoryItem detail)
		throws SystemException {

		String userID = detail.getSubmitterID();

		// override user name only if the id is a profile id
		if (userID == null || userID.length() != 7) {
			return;
		}

		Integer profileID = null;
		try {
			profileID = new Integer(userID);
		} catch (NumberFormatException e) {
			// this is okay, just return to use the value already set
			return;
		}

		try {
			SelectMultiFieldMultiRowQueryHandler handler =
				new SelectMultiFieldMultiRowQueryHandler(CUSTOMER_DATA_SOURCE_NAME, SQL_USER_NAME_SELECT,
						new Class[] {String.class, String.class, String.class});

			Object[] resultList = (Object[]) handler.select(new Object[] {profileID});

			// if the select doesn't return anything, the name will stay at the value already set
			boolean internalUser = false;
			for (int i=0; i < resultList.length; i++) {
                detail.setSubmitterName(((Object[])resultList[i])[1] + ", " + ((Object[])resultList[i])[0]);
				if (INTERNAL_USER_TYPE_CODE.equals(((Object[])resultList[i])[2])) {
					internalUser = true;
					break;
				}
			}
            detail.setInternalSubmitter(internalUser);

		} catch (DAOException e) {
			throw new SystemException(e, className, "setUserName",
					"Problem occurred in prepared call: " + SQL_USER_NAME_SELECT +
					" with profile ID: " + profileID + ", contract ID " + detail.getContractId() +
					", submission ID " + detail.getSubmissionId());
		}
	}

	private static void setLockedByType(SubmissionHistoryItem submissionCase)
		throws SystemException {

		// set type only if there is a lock
		Lock lock = submissionCase.getLock();
		if (lock != null) {
			Boolean isInternalUser = isInternalUser(lock.getUserId());
			if (isInternalUser != null) {
				submissionCase.setLockedByInternalUser(isInternalUser.booleanValue());
			}
		}
	}

	private static void setSubmitterType(SubmissionHistoryItem item) throws SystemException {

		Boolean isInternalUser = isInternalUser(item.getSubmitterID());
		if (isInternalUser != null) {
			item.setInternalSubmitter(isInternalUser.booleanValue());
		}
	}

    /**
	 * @param user ID
	 *
	 * @throws SystemException
	 */
	private static Boolean isInternalUser(String userID) throws SystemException {

		Boolean isInternalUser = null;

		// override user name only if the id is a profile id
		if (userID == null || userID.length() != 7) {
			return isInternalUser;
		}

		Integer profileID = null;
		try {
			profileID = new Integer(userID);
		} catch (NumberFormatException e) {
			// this is okay, just return to use the value already set
			return isInternalUser;
		}

		try {
			SelectMultiFieldMultiRowQueryHandler handler =
				new SelectMultiFieldMultiRowQueryHandler(CUSTOMER_DATA_SOURCE_NAME, SQL_USER_NAME_SELECT,
						new Class[] {String.class, String.class, String.class});

			Object[] resultList = (Object[]) handler.select(new Object[] {profileID});

			// if the select doesn't return anything, the name will stay at the value already set
			for (int i=0; i < resultList.length; i++) {
				if (INTERNAL_USER_TYPE_CODE.equals(((Object[])resultList[i])[2])) {
					isInternalUser = Boolean.TRUE;
					break;	// exit now
				} else { // keep going
					isInternalUser = Boolean.FALSE;
				}
			}
			return isInternalUser;

		} catch (DAOException e) {
			throw new SystemException(e, className, "isInternalUser",
					"Problem occurred in prepared call: " + SQL_USER_NAME_SELECT +
					" with profile ID: " + profileID);
		}
	}

	/**
     * Returns the participant sort option for the given contract. Empty
     * string is never returned. Instead, null will be returned if the sort
     * option is empty.
     *
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static String getParticipantSortOption(int contractId) throws SystemException {
        try {
            SelectSingleValueQueryHandler handler4 = new SelectSingleValueQueryHandler(
                    CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_DATA, String.class);

            String participantSortOption = (String) handler4.select(new Object[] { new Integer(contractId) });
            if (participantSortOption != null) {
                participantSortOption = participantSortOption.trim();
                if (participantSortOption.length() == 0) {
                    return null;
                }
            }
            return participantSortOption;
        } catch (DAOException e) {
            throw handleDAOException(e, className, "getParticipantSortOption",
                    "Problem occurred in prepared call: " + SQL_SELECT_CONTRACT_DATA +
					" with contract id " + contractId);
        }
    }
    
    public static String getDeferralType(int contractId)
    throws SystemException {
        
        String deferralType = null;
        
        Collection<String> serviceFeature = new ArrayList<String>();
        serviceFeature.add(ServiceFeatureConstants.MANAGING_DEFERRALS);
        
        try {
            
            Map csfMap = ContractServiceFeatureDAO.getContractServiceFeatures(
                    contractId,
                    serviceFeature);
            ContractServiceFeature csf = (ContractServiceFeature) csfMap.get(ServiceFeatureConstants.MANAGING_DEFERRALS);
            
            if (csf != null) {
                String csfa = csf.getAttributeValue(ServiceFeatureConstants.MD_DEFERRAL_TYPE);
                if (csfa != null) {
                    deferralType = csfa;
                }
            }
            
        } catch (ApplicationException ae) {
            assert ae instanceof ContractDoesNotExistException;
            deferralType = null;
        }
        
        return deferralType;
        
    }
    
    
    /**
     * Returns a boolean indicating if the given contract is for New York.
    *
     * @param contractId
     * @return boolean
     * @throws SystemException
     */
    public static boolean isNY(int contractId) throws SystemException {
        try {
            SelectSingleValueQueryHandler handler4 = new SelectSingleValueQueryHandler(
                    CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_COMPANY_CODE,
                    String.class);
            String location = (String) handler4.select(new Object[] { new Integer(contractId) });
			boolean isNY = (location.equals(MANULIFE_COMPANY_ID) ? false : true);
			return isNY;

        } catch (DAOException e) {
            throw handleDAOException(e, className, "isNY",
                    "Problem occurred in prepared call: "
                            + SQL_SELECT_COMPANY_CODE + " with contract id "
                            + contractId);
        }
    }

	private static void setLoanFeature(ContributionDetailItem contributionDetail) throws SystemException {

		try {
			Integer loanInd =
				(Integer) new SelectSingleOrNoValueQueryHandler(CUSTOMER_DATA_SOURCE_NAME,
						SQL_SELECT_LOAN_FEATURE, Integer.class).select(new Object[] {contributionDetail.getContractId()});

			contributionDetail.setContractHasLoanFeature(loanInd == null || loanInd.equals(ZERO_INTEGER) ? false : true);

		} catch (DAOException e) {
			throw new SystemException(e, className, "setLoanFeature",
					"Problem occurred in prepared call: " + SQL_SELECT_LOAN_FEATURE
					+ " for contractId " + contributionDetail.getContractId());
		}
	}

	/**
	 * Returns the submission ID of the latest completed regular contribution submission made by a user
	 * for a given contract.
	 *
	 * @param contractNumber - the contract ID for which we are looking
	 * @param userId - the profile ID of the user for which we perform the operation
	 * @return submissionId of the last completed regular contribution submission or -1 if none found
	 * @throws SystemException - for failures during DB operations
	 */
	public static int getLastSubmittedContributionDetailsSubmissionId(int contractNumber, String userId)
			throws SystemException {

		logger.debug("entry -> getLastSubmittedContributionDetailsSubmissionId");
		logger.debug("contractNumber -> " + contractNumber);
		logger.debug("userId -> " + userId);

		Collection params = new ArrayList(3);
		params.add(new Integer(contractNumber));
		params.add(userId);

		StringBuffer query = new StringBuffer(SQL_LAST_SUBMITTED_CONTRIBUTION_PREFIX);

		Integer submissionId = null;
		try {

			// user ID is the profile ID - For older submissions, we need to retrieve the SSN
			// TODO: Two years after all the users have been migrated to the new PSW, drop the SSN all together
			String ssn = getSSN(new Long(userId));
			if (ssn == null) {
				query.append(SQL_LAST_SUBMITTED_CONTRIBUTION_SHORT_INFIX);
			} else {
				query.append(SQL_LAST_SUBMITTED_CONTRIBUTION_LONG_INFIX);
				params.add(ssn);
			}
			query.append(SQL_LAST_SUBMITTED_CONTRIBUTION_SUFIX);

			SelectSingleOrNoValueQueryHandler handler = new SelectSingleOrNoValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, query.toString(), Integer.class);
			submissionId = (Integer) handler.select(params.toArray());

		} catch (DAOException e) {
			throw handleDAOException(e, className, "getLastSubmittedContributionDetailsSubmissionId",
					"Problem occurred in prepared call: " + query.toString() + " with contractNumber " + contractNumber);
		}

		logger.debug("exit <- getLastSubmittedContributionDetailsSubmissionId");

		if (submissionId == null) {
			return -1;
		} else {
			return submissionId.intValue();
		}
	}

	/**
	 * given an existing submission ID and contractId, copy the case and return
	 * an object containing the submission ID of the new submission and data on
	 * the items not copied due to various business rules
	 *
	 * @param submissionId
	 * @param ContractId
	 * @param userId
	 * @param userSSN
	 * @param userName
	 * @param userType
	 * @param userTypeId
	 * @param userTypeName
	 * @return CopiedSubmissionHistoryItem
	 * @throws SystemException
	 */
	public static CopiedSubmissionHistoryItem copyContributionDetails(
			int submissionId, int contractId, String userId, String userName,
			String userTypeCode, BigDecimal userTypeId, String userTypeName, 
			String notificationEmailAddress) throws SystemException {

		logger.debug("entry -> copyContributionDetails");
		logger.debug("submissionId -> " + submissionId);
		logger.debug("contractId -> " + contractId);

		Timestamp dummyTimestamp = new Timestamp(System.currentTimeMillis());

		Connection connection = null;
		Integer newSubmissionId = new Integer(0);
		CopiedSubmissionHistoryItem copiedItem = null;

		// start overall try block for the entire method
		try {
			int errorCode = getCopyAllowedReasonCode(submissionId, contractId);
			if (errorCode != CopiedSubmissionHistoryItem.NO_ERROR) {
				copiedItem = new CopiedSubmissionHistoryItem();
                copiedItem.setErrorCode(errorCode);
				return copiedItem;
			}
			connection = getDefaultConnection(className,
					SUBMISSION_DATA_SOURCE_NAME);

			newSubmissionId = getNewSubmissionId();
			copiedItem = new CopiedSubmissionHistoryItem(submissionId, newSubmissionId.intValue());

			copySubmissionJournalData(connection, contractId, submissionId,
					userName, userId, userTypeCode, userTypeId, userTypeName,
					newSubmissionId, dummyTimestamp, notificationEmailAddress);
			
			//Changed the calling method for QC 4773, as payroll date should show the next investment date. 
			Date contributionApplicableDate = determinePaymentEffectiveDate();

			createSubmissionCaseData(new Integer(contractId), userId, newSubmissionId,
                    dummyTimestamp, GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR);

			// copy contribution data, setting moneySourceId and returning
			// submissionId
			ContributionInfo contributionInfo = copySubmissionContributionData(
					new Integer(contractId), new Integer(submissionId), userId,
					newSubmissionId, contributionApplicableDate);

			// copy participant data
			Map moneyTypeList = copyParticipantData(connection,
					contributionInfo.getSubmissionId(), contractId,
					newSubmissionId,
					contributionApplicableDate, userId, contributionInfo
							.getMoneySourceId(), copiedItem);

			// insert to the money type table for each distinct money type copied to the allocation table
			insertMoneyTypes(newSubmissionId, new Integer(contractId), userId, moneyTypeList);

		} catch (SQLException e) {
			throw new SystemException(e, className, "copyContributionDetails",
					"Problem getting connection " + " for contractId "
							+ contractId + " and submissionId "
							+ submissionId);
		} finally {
            close(null, connection);
		}
		logger.debug("exit <- copyContributionDetails");

		return copiedItem;
	}

	/**
	 *
	 * @param contractId
	 * @return next available submission ID on submission journal schema of
	 *         stp database
	 * @throws SystemException
	 */
	public static Integer getNewSubmissionId() throws SystemException {
		Integer newSubmissionId;
		try {
			SelectSingleValueQueryHandler handler2 = new SelectSingleValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, NEXT_SUBMISSION_ID_SQL,
					Integer.class);
			newSubmissionId = (Integer) handler2.select(new Object[] {});
		} catch (DAOException e) {
			throw handleDAOException(e, className, "copyContributionDetails",
					"Problem occurred in prepared call: "
							+ NEXT_SUBMISSION_ID_SQL);
		}

		return newSubmissionId;
	}

	/**
	 * Copy appropriate submission journal data for a given submissionId to a
	 * new submissionId for a given user
	 *
	 * @param connection
	 * @param contractId
	 * @param submissionId
	 * @param userName
	 * @param userId
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @param newSubmissionId
	 * @param dummyTimestamp
	 * @throws SystemException
	 */
	private static void copySubmissionJournalData(Connection connection,
			int contractId, int submissionId, String userName, String userId,
			String userTypeCode, BigDecimal userTypeId, String userTypeName,
			Integer newSubmissionId, Timestamp dummyTimestamp, String notificationEmailAddress)
			throws SystemException {

		// get required submission journal data
		String contractName = EMPTY_STRING;
		String locationCode = EMPTY_STRING;
		PreparedStatement submissionJournalStatement = null;
		try {
			submissionJournalStatement = connection
					.prepareStatement(SQL_SELECT_SUBMISSION_JOURNAL);
			submissionJournalStatement.setInt(1, submissionId);

			ResultSet rs = submissionJournalStatement.executeQuery();

			while (rs.next()) {
				// save participant level errors, if any
				contractName = rs.getString("contractName");
				locationCode = rs.getString("locationCode");
			}
			rs.close();

		} catch (SQLException e) {
			throw new SystemException(e, className,
					"copySubmissionJournalData",
					"Problem occurred in prepared call: "
							+ SQL_SELECT_SUBMISSION_JOURNAL
							+ " for contractId " + contractId
							+ " and submissionId " + submissionId);
		}

		// insert new submission journal row
		try {
			submissionJournalStatement = connection
					.prepareStatement(SQL_SUBMISSION_JOURNAL_INSERT);
			submissionJournalStatement.setInt(1, newSubmissionId.intValue());
			submissionJournalStatement.setInt(2, contractId);
			submissionJournalStatement.setString(3, contractName);
			submissionJournalStatement.setString(4, userName);
			submissionJournalStatement.setString(5, userId);
			submissionJournalStatement.setString(6, userTypeCode);
			submissionJournalStatement.setBigDecimal(7, userTypeId);
			submissionJournalStatement.setString(8, userTypeName);
			submissionJournalStatement.setString(9, locationCode);
			submissionJournalStatement.setTimestamp(10, dummyTimestamp);
			submissionJournalStatement.setTimestamp(11, dummyTimestamp);
			submissionJournalStatement.setString(12, APPLICATION_CODE);
            submissionJournalStatement.setString(13, GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR);
            submissionJournalStatement.setString(14, EMPTY_STRING);
            submissionJournalStatement.setBigDecimal(15, ZERO_SCALE_2);
            submissionJournalStatement.setString(16, notificationEmailAddress);

			submissionJournalStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SystemException(e, className,
					"copySubmissionJournalData",
					"Problem occurred in prepared call: "
							+ SQL_SUBMISSION_JOURNAL_INSERT
							+ " for contractId " + contractId
							+ " and newSubmissionId " + newSubmissionId);
		}
		createPaymentData(newSubmissionId, new Integer(contractId));
	}

	/**
	 * Create appropriate submission journal gft_payment_info and
	 * gft_upload_data rows for a given submissionId and contractId
	 *
	 * @param newSubmissionId
	 * @throws SystemException
	 */
	private static void createPaymentData(Integer newSubmissionId, Integer contractId)
			throws SystemException {

		// insert new payment info row
		try {
            Date paymentEffectiveDate = determinePaymentEffectiveDate();
			new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_GFT_PAYMENT_INFO_INSERT).insert(
					new Object[] {newSubmissionId, contractId, BigDecimal.ZERO, paymentEffectiveDate});

		} catch (DAOException e) {
			throw new SystemException(e, className, "copySubmissionJournalData",
					"Problem occurred in prepared call: " + SQL_GFT_PAYMENT_INFO_INSERT +
					" for newSubmissionId " + newSubmissionId + " contractId " + contractId);
		}
	}

	private static void createSubmissionCaseData(Integer contractId, String userId,
			Integer newSubmissionId, Timestamp submissionTs, String submissionCaseTypeCode)
		throws SystemException {

		// insert new submission row
		try {
            new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION)
                    .insert(new Object[] { newSubmissionId, submissionTs, EMPTY_STRING,
                            EMPTY_STRING, userId, EMPTY_STRING, NO, userId, userId });
		} catch (DAOException e) {
			throw new SystemException(e, className, "copyContributionDetails",
					"Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION +
					" for contract ID " + contractId + " and newSubmissionId " + newSubmissionId);
		}

		// insert new submission case row
		try {
			new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION_CASE).insert(
					new Object[] {newSubmissionId, contractId, submissionCaseTypeCode,
							COPIED_STATUS, NO, ZERO_INTEGER, userId, userId});
		} catch (DAOException e) {
			throw new SystemException(e, className, "copyContributionDetails",
					"Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION_CASE +
					" for contract ID " + contractId + " and newSubmissionId " + newSubmissionId);
		}
	}

	private static ContributionInfo copySubmissionContributionData(Integer contractId, Integer submissionId,
			String userId, Integer newSubmissionId, Date contributionApplicableDate)
		throws SystemException {

		// get existing case info
		String moneySourceId = EMPTY_STRING;
		Integer priorityNumber = ZERO_INTEGER;
		String transactionCode = EMPTY_STRING;
		try {
			SelectMultiFieldQueryHandler handler =
				new SelectMultiFieldQueryHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_SELECT_SUBMISSION_CONTRIBUTION_DATA,
						new Class[] {String.class, Integer.class, String.class});
			Object[] result =  (Object[])handler.select(new Object[] {submissionId, contractId});

			// save contribution level data
			moneySourceId = (String) result[0];
			priorityNumber = (Integer) result[1];
			transactionCode = (String) result[2];

		} catch (DAOException e) {
			throw new SystemException(e, className, "copyContributionDetails",
					"Problem occurred in prepared call: " + SQL_SELECT_SUBMISSION_CONTRIBUTION_DATA +
					" for contractId " + contractId + " and submissionId " + submissionId);
		}

		createSubmissionContributionData(contractId, userId, newSubmissionId, moneySourceId,
				transactionCode, priorityNumber, contributionApplicableDate);

		return new ContributionInfo(submissionId.intValue(), moneySourceId);
	}

	/**
	 * returns the default contribution applicable date  - current date
	 * removed requirement: if today is a valid investment date, use
	 * use today's date otherwise get the next investment date
	 *
	 * @return default contribution applicable date
	 * @throws SystemException
	 */
	private static Date determineContributionApplicableDate() {
        return new Date();
	}

	/**
	 * returns the default payment effective date if today is a valid investment
	 * date and it is before the stock exchange closing, use today's date
	 * otherwise get the next investment date
	 *
	 * @return default payment effective date
	 * @throws SystemException
	 */
	private static Date determinePaymentEffectiveDate()
			throws SystemException {

		Date paymentEffectiveDate = null;
		Date marketClose = null;
		try {
			marketClose = AccountServiceDelegate.getInstance()
					.getNextNYSEClosureDateIgnoringEmergencyClosure(null);
		} catch (Exception e) {
			SystemException se = new SystemException(e, className,
					"determinePaymentEffectiveDate",
					"AccountException occurred while getting the NYSE close datetime.");
			throw se;
		}

		GregorianCalendar calMarketClose = new GregorianCalendar();
		calMarketClose.setTime(marketClose);
		calMarketClose.set(Calendar.HOUR_OF_DAY, 0);
		calMarketClose.set(Calendar.MINUTE, 0);
		calMarketClose.set(Calendar.SECOND, 0);
		calMarketClose.set(Calendar.MILLISECOND, 0);

		Date currentDate = new GregorianCalendar().getTime();

		// if the market hasn't closed yet, this is the date to use
		// otherwise, find the next market date
		if (marketClose != null && marketClose.after(currentDate)) {
			paymentEffectiveDate = calMarketClose.getTime();
		} else {
			Date[] dates = new Date[DELTA_END_DAYS];
			for (int i = 0; i < DELTA_END_DAYS; i++) {
				calMarketClose.add(Calendar.DATE, 1);
				dates[i] = calMarketClose.getTime();
			}

			// filter out the dates that are not NYSE valid market trading days
			try {
				dates = AccountServiceDelegate.getInstance()
						.getFilteredNYSEClosureDatesIgnoringEmergencyClosure(null, dates);
			} catch (Exception e) {
				SystemException se = new SystemException(e, className,
						"determinePaymentEffectiveDate",
						"AccountException occurred while getting the NYSE filtered dates.");
				throw se;
			}
			// return the first valid one (if any)
			if (dates != null && dates.length > 0 && dates[0] != null) {
				paymentEffectiveDate = dates[0];
			}
		}
		return paymentEffectiveDate;

	}

	private static Map copyParticipantData(Connection connection,
			int submissionId, int contractId, Integer newSubmissionId,
			Date sqlDate, String userId,
			String moneySourceId, CopiedSubmissionHistoryItem copiedItem)
			throws SystemException {

		// copy participant and associated tables
		Map moneyTypeList = new HashMap();
		PreparedStatement participantSelectStatement = null;
		PreparedStatement activeLoansSelectStatement = null;
		PreparedStatement statusSelectStatement = null;
		PreparedStatement participantInsertStatement = null;
		PreparedStatement allocationSelectStatement = null;
		PreparedStatement allocationInsertStatement = null;
		PreparedStatement loanRepaymentSelectStatement = null;
		PreparedStatement loanRepaymentInsertStatement = null;
		BigDecimal totalAllocationAmount = ZERO_SCALE_2;
		BigDecimal totalWithdrawalAmount = ZERO_SCALE_2;
		BigDecimal totalLoanRepaymentAmount = ZERO_SCALE_2;

		Connection csdbConnection = null;
		try {
			csdbConnection = getDefaultConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
		} catch (SQLException e) {
			try {
				if (null != csdbConnection) csdbConnection.close();
			} catch	(SQLException se) {
				logger.error(className + ".copyParticipantData() " +
					"Problem occurred in closing csdb database connection "
					+ " for contractId " + contractId + " and newSubmissionId " + newSubmissionId, se);
			}
			throw new SystemException(e, className, "copyParticipantData",
					"Problem getting connection!");

		}

		// get money types valid for the money source
        Set validMoneyTypes = DomainDAO.getValidMoneyTypesForMoneySource(moneySourceId.trim());
        List contractMoneyTypes = ContractDAO.getContractMoneyTypeIdsFilteredByValidMoneyTypes(
                new Integer(contractId), true, validMoneyTypes, false);

		String statusSelectString = getStatusStatement(contractId);
		try {
			statusSelectStatement = csdbConnection
					.prepareStatement(statusSelectString);
		} catch (SQLException e) {
			throw new SystemException(e, className, "copyParticipantData",
					"Problem occurred in prepared call: " + SQL_STATUS_SELECT
							+ " for contractId " + contractId);
		}

		int targetRecordNo = 0;
		ResultSet rs = null;
		try {
			String selectStatement = SQL_PARTICIPANT_SELECT
					+ RECORD_NO_ORDER_CLAUSE;
			participantSelectStatement = connection
					.prepareStatement(selectStatement);
			participantSelectStatement.setInt(1, submissionId);
			participantSelectStatement.setInt(2, contractId);

			rs = participantSelectStatement.executeQuery();

			String participantStatusCode = EMPTY_STRING;
			BigDecimal participantId = BigDecimal.ZERO;

			while (rs.next()) {
				ResultSet statusResultSet = null;
				String employerDesignatedId = rs
						.getString("employerDesignatedId");
				// get participant status
				try {
					statusSelectStatement.setInt(1, contractId);
					statusSelectStatement.setString(2, employerDesignatedId);
					statusResultSet = statusSelectStatement.executeQuery();
					// reset identifiers to handle rare case where participant
					// not found
					participantStatusCode = EMPTY_STRING;
					participantId = BigDecimal.ZERO;
					while (statusResultSet.next()) {
						participantStatusCode = statusResultSet
								.getString("participantStatus");
						participantId = statusResultSet
								.getBigDecimal("participantId");
					}
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"copyParticipantData",
							"Problem occurred in prepared call: "
									+ SQL_STATUS_SELECT + " with contract id "
									+ contractId + " and employerDesignatedId"
									+ employerDesignatedId);
				} finally {
					try {
						if (null != statusResultSet)
							statusResultSet.close();
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"copyParticipantData",
								"Problem occurred in closing money type result set or select statement");
					}

				}

				// get active loans
				ResultSet activeLoansResultSet = null;
				Map activeLoanMap = new HashMap();
				try {
					activeLoansSelectStatement = csdbConnection.prepareStatement(SQL_ACTIVE_LOANS_SELECT);
					activeLoansSelectStatement.setBigDecimal(1, participantId);
					activeLoansSelectStatement.setInt(2, contractId);
					activeLoansResultSet = activeLoansSelectStatement.executeQuery();
					while(activeLoansResultSet.next()) {
						Integer loanId = new Integer(activeLoansResultSet.getInt("loanId"));
						// value represents last occurrence of this loan written to the database
						// starting value -1 means we haven't written anything yet
						activeLoanMap.put(loanId, new Integer(-1));
					}
				} catch (SQLException e) {
					throw new SystemException(e, className, "copyParticipantData",
							"Problem occurred in prepared call: "
							+ SQL_ACTIVE_LOANS_SELECT + " for contractId " + contractId
							+ " and participantId " + participantId);
				} finally {
					try {
						if (null != activeLoansResultSet) activeLoansResultSet.close();
						if (null != activeLoansSelectStatement) activeLoansSelectStatement.close();
					} catch (SQLException e) {
						throw new SystemException(e, className, "copyParticipantData", "Problem occurred in closing"
								+ " active loan result set");
					}
				}

				// only copy the participant if it is active or has active loans
				if (ACTIVE_STATUS.equals(participantStatusCode)
						|| activeLoanMap.size() > 0) {


					targetRecordNo++;
					if (logger.isDebugEnabled() && targetRecordNo % 100 == 0)
						logger.debug("starting copy for participant " + targetRecordNo);

					participantInsertStatement = connection.prepareStatement(SQL_INSERT_SUBMISSION_PARTICIPANT);
					participantInsertStatement.setInt(1, newSubmissionId.intValue());
					participantInsertStatement.setInt(2, contractId);
					participantInsertStatement.setString(3, employerDesignatedId);
					participantInsertStatement.setString(4, rs.getString("fullName"));
					participantInsertStatement.setString(5, EMPTY_STRING);
					participantInsertStatement.setInt(6, targetRecordNo);
					participantInsertStatement.setString(7, userId);
					participantInsertStatement.setString(8, userId);

					try {
						participantInsertStatement.executeUpdate();
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"copyParticipantData",
								"Problem occurred in prepared call: "
										+ SQL_INSERT_SUBMISSION_PARTICIPANT
										+ " for contractId " + contractId
										+ " and newSubmissionId "
										+ newSubmissionId);
					} finally {
						try {
							if (null != participantInsertStatement) participantInsertStatement.close();
						} catch (SQLException e) {
							throw new SystemException(e, className,
									"copyParticipantData",
									"Problem occurred in closing participant insert statement");
						}
					}
					ResultSet allocationResultSet = null;
					int allocationTargetRecordNo = targetRecordNo;
					int allocationSourceRecordNo = rs.getInt("sourceRecordNo");
					try {
						allocationSelectStatement = connection
								.prepareStatement(SQL_ALLOCATION_SELECT + RECORD_NO_ORDER_CLAUSE);
						allocationSelectStatement.setInt(1, submissionId);
						allocationSelectStatement.setInt(2, contractId);
						allocationSelectStatement.setString(3, rs.getString("employerDesignatedId"));

						allocationResultSet = allocationSelectStatement
								.executeQuery();
						while (allocationResultSet.next()) {
							String moneyTypeId = allocationResultSet.getString(
									"moneyTypeId").trim();
							// only copy money type if it is still valid for the
							// contract
							if (contractMoneyTypes.contains(moneyTypeId)) {
								allocationInsertStatement
										= connection.prepareStatement(SQL_INSERT_SUBMISSION_ALLOCATION);

								BigDecimal allocationAmount = allocationResultSet
										.getBigDecimal("allocationAmount");
								if (allocationAmount
										.compareTo(ZERO_SCALE_2) > 0) {
									totalAllocationAmount = totalAllocationAmount
											.add(allocationAmount);
								} else {
									totalWithdrawalAmount = totalWithdrawalAmount
											.add(allocationAmount);
								}
								allocationInsertStatement.setInt(1,
										newSubmissionId.intValue());
								allocationInsertStatement.setInt(2, contractId);
								allocationInsertStatement.setString(3,
										employerDesignatedId);
								allocationInsertStatement.setString(4,
										moneyTypeId);
								allocationInsertStatement.setInt(5,
										allocationResultSet
												.getInt("occurrenceNo"));
								allocationInsertStatement.setBigDecimal(6,
										allocationAmount);
								allocationInsertStatement.setDate(7, getSqlDate(sqlDate));
								allocationInsertStatement.setString(8, EMPTY_STRING);
								// increment record number if record number has changed
								if (allocationResultSet.getInt("sourceRecordNo") != allocationSourceRecordNo) {
									allocationSourceRecordNo = allocationResultSet.getInt("sourceRecordNo");
									allocationTargetRecordNo++;
								}
								allocationInsertStatement.setInt(9,
										allocationTargetRecordNo);
								allocationInsertStatement.setString(10, userId);
								allocationInsertStatement.setString(11, userId);

								try {
									allocationInsertStatement.executeUpdate();
									// save each money type so we can insert it
									// to the money type table at the end
									moneyTypeList.put(allocationResultSet
											.getString("moneyTypeId"),
											allocationResultSet
													.getString("moneyTypeId"));
								} catch (SQLException e) {
									if (logger.isDebugEnabled())
										logger.debug("Error inserting to allocation table. Set statement parameters to: "
													+ "1["
													+ newSubmissionId
													+ "] 2["
													+ contractId
													+ "] 3["
													+ employerDesignatedId
													+ "] 4["
													+ moneyTypeId
													+ "] 5["
													+ allocationResultSet
															.getInt("occurrenceNo")
													+ "] 6["
													+ allocationAmount
													+ "] 7["
													+ sqlDate
													+ "] 8["
													+ EMPTY_STRING
													+ "] 9["
													+ allocationTargetRecordNo
													+ "] 10["
													+ userId
													+ "] 11["
													+ userId
													+ "]");
									throw new SystemException(
											e,
											className,
											"copyParticipantData",
											"Problem occurred in prepared call: "
													+ SQL_INSERT_SUBMISSION_ALLOCATION
													+ " for contractId "
													+ contractId
													+ " and newSubmissionId "
													+ newSubmissionId
													+ " and employerDesignatedId "
													+ employerDesignatedId);
								} finally {
									try {
										if (null != allocationInsertStatement) allocationInsertStatement.close();
									} catch (SQLException e) {
										throw new SystemException(e, className,
												"copyParticipantData",
												"Problem occurred in closing allocation insert statement");
									}
								}
							} else {
								copiedItem.getMoneyTypesNotCopied().put(
										moneyTypeId, moneyTypeId);
							}

						}
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"copyParticipantData",
								"Problem occurred in prepared call: "
										+ SQL_ALLOCATION_SELECT
										+ ALLOCATION_AMOUNT_NOT_ZERO
										+ " for contractId " + contractId
										+ " and newSubmissionId "
										+ newSubmissionId
										+ " and employerDesignatedId "
										+ employerDesignatedId);
					} finally {
						try {
							if (null != allocationResultSet) allocationResultSet.close();
							if (null != allocationSelectStatement) allocationSelectStatement.close();
						} catch (SQLException e) {
							throw new SystemException(e, className,
									"copyParticipantData",
									"Problem occurred in closing allocation result set or select statement");
						}
					}
					int loanRepaymentTargetRecordNo = targetRecordNo;
					ResultSet loanRepaymentResultSet = null;
					int loanRepaymentSourceRecordNo = rs.getInt("sourceRecordNo");
					try {
						loanRepaymentSelectStatement = connection.prepareStatement(SQL_LOAN_REPAYMENT_AMOUNTS);
						loanRepaymentSelectStatement.setInt(1, submissionId);
						loanRepaymentSelectStatement.setInt(2, contractId);
						loanRepaymentSelectStatement.setString(3, employerDesignatedId);

						loanRepaymentResultSet = loanRepaymentSelectStatement.executeQuery();

						int loanId;
						Integer occurrenceNo;
						while (loanRepaymentResultSet.next()) {
							loanRepaymentInsertStatement = connection.prepareStatement(SQL_INSERT_SUBMISSION_LOAN_REPAYMENT);
							loanId = loanRepaymentResultSet.getInt("loanId");
							BigDecimal loanRepaymentAmount = loanRepaymentResultSet
							.getBigDecimal("loanRepaymentAmount");
							// only copy the repayment if the loan is still active and amount greater than zero
							occurrenceNo = (Integer)activeLoanMap.get(new Integer(loanId));
							if (null !=  occurrenceNo) {

								if (loanRepaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
									occurrenceNo = new Integer(occurrenceNo.intValue() + 1);

									activeLoanMap.put(new Integer(loanId), occurrenceNo);
									totalLoanRepaymentAmount = totalLoanRepaymentAmount.add(loanRepaymentAmount);

									loanRepaymentInsertStatement.setInt(1, newSubmissionId.intValue());
									loanRepaymentInsertStatement.setInt(2, contractId);
									loanRepaymentInsertStatement.setString(3, employerDesignatedId);
									loanRepaymentInsertStatement.setInt(4, loanId);
									loanRepaymentInsertStatement.setInt(5, occurrenceNo.intValue());
									loanRepaymentInsertStatement.setBigDecimal(6, loanRepaymentAmount);
									loanRepaymentInsertStatement.setDate(7, getSqlDate(sqlDate));
									loanRepaymentInsertStatement.setString(8, EMPTY_STRING);
									// increment target record number if source record number has changed
									if (loanRepaymentResultSet.getInt("sourceRecordNo")
											!= loanRepaymentSourceRecordNo) {
										loanRepaymentSourceRecordNo = loanRepaymentResultSet.getInt("sourceRecordNo");
										loanRepaymentTargetRecordNo++;
									}
									loanRepaymentInsertStatement.setInt(9, loanRepaymentTargetRecordNo);
									loanRepaymentInsertStatement.setString(10, userId);
									loanRepaymentInsertStatement.setString(11, userId);

									try {
										loanRepaymentInsertStatement.executeUpdate();
									} catch (SQLException e) {
										throw new SystemException(e, className, "copyParticipantData",
												"Problem occurred in prepared call: "
												+ SQL_INSERT_SUBMISSION_LOAN_REPAYMENT
												+ " for contractId "
												+ contractId
												+ " and newSubmissionId "
												+ newSubmissionId
												+ " and employerDesignatedId "
												+ employerDesignatedId);
									} finally {
										try {
											if (null != loanRepaymentInsertStatement) {
												loanRepaymentInsertStatement.close();
											}
										} catch (SQLException e) {
											throw new SystemException(e, className, "copyParticipantData",
											"Problem occurred in closing loan insert statement");
										}
									}
								}

							} else {
								// add participant and loan id to list of
								// repayments not copied
								if (loanId != 0 && loanId != 99) {
									copiedItem.getLoanRepaymentsNotCopied()
									.put(employerDesignatedId + "/" + loanId, rs.getString("fullName"));

								} else {
									if (loanRepaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
										copiedItem.getLoanRepaymentsNotCopied()
										.put(employerDesignatedId + "/" + "na", rs.getString("fullName"));
									}
								}
							}
						}
						// any loans in the map that weren't copied from the original submission,
						// but are active so should be copied with zero amounts for each target record
						for (Iterator itr = activeLoanMap.keySet().iterator(); itr.hasNext();) {
							loanId = ((Integer) itr.next()).intValue();
							for (int i = targetRecordNo; i < loanRepaymentTargetRecordNo + 1; i++) {
								occurrenceNo = (Integer)activeLoanMap.get(new Integer(loanId));
								// if no repayments have been written (occurrence number still < 0)
								// insert a zero amount entry
								if (null !=  occurrenceNo && occurrenceNo.compareTo(new Integer(0)) < 0) {
									occurrenceNo = new Integer(occurrenceNo.intValue() + 1);
									activeLoanMap.put(new Integer(loanId), occurrenceNo);
									loanRepaymentInsertStatement
									= connection.prepareStatement(SQL_INSERT_SUBMISSION_LOAN_REPAYMENT);
									BigDecimal loanRepaymentAmount = ZERO_SCALE_2;

									loanRepaymentInsertStatement.setInt(1, newSubmissionId.intValue());
									loanRepaymentInsertStatement.setInt(2, contractId);
									loanRepaymentInsertStatement.setString(3, employerDesignatedId);
									loanRepaymentInsertStatement.setInt(4, loanId);
									loanRepaymentInsertStatement.setInt(5, (occurrenceNo.intValue()));
									loanRepaymentInsertStatement.setBigDecimal(6, loanRepaymentAmount);
									loanRepaymentInsertStatement.setDate(7, getSqlDate(sqlDate));
									loanRepaymentInsertStatement.setString(8, EMPTY_STRING);
									loanRepaymentInsertStatement.setInt(9, i);
									loanRepaymentInsertStatement.setString(10, userId);
									loanRepaymentInsertStatement.setString(11, userId);

									try {
										loanRepaymentInsertStatement.executeUpdate();
									} catch (SQLException e) {
										throw new SystemException(e, className, "copyParticipantData",
												"Problem occurred in prepared call: "
												+ SQL_INSERT_SUBMISSION_LOAN_REPAYMENT
												+ " for contractId "
												+ contractId
												+ " and newSubmissionId "
												+ newSubmissionId
												+ " and employerDesignatedId "
												+ employerDesignatedId);
									} finally {
										try {
											if (null != loanRepaymentInsertStatement) {
												loanRepaymentInsertStatement.close();
											}
										} catch (SQLException e) {
											throw new SystemException(e, className, "copyParticipantData",
											"Problem occurred in closing loan insert statement");
										}
									}
								}
							}
						}

					} catch (SQLException e) {
						throw new SystemException(e, className, "copyParticipantData",
								"Problem occurred in prepared call: "
								+ SQL_LOAN_REPAYMENT_AMOUNTS
								+ " for contractId " + contractId
								+ " and newSubmissionId "
								+ newSubmissionId
								+ " and employerDesignatedId "
								+ employerDesignatedId);
					} finally {
						try {
							if (null != loanRepaymentResultSet) loanRepaymentResultSet.close();
							if (null != loanRepaymentSelectStatement) loanRepaymentSelectStatement.close();
						} catch (SQLException e) {
							throw new SystemException(e, className,
									"copyParticipantData",
							"Problem occurred in closing loan result set");
						}
					}
					if (allocationTargetRecordNo > targetRecordNo) targetRecordNo = allocationTargetRecordNo;
					if (loanRepaymentTargetRecordNo > targetRecordNo) targetRecordNo = loanRepaymentTargetRecordNo;
				} else {
					// add participant to list of participants not copied
					//copiedItem.getParticipantsNotCopied().put(employerDesignatedId, employerDesignatedId);
					copiedItem.getParticipantsNotCopied().put(employerDesignatedId, rs.getString("fullName"));
				}	// ends if (ACTIVE_STATUS.equals(participantStatusCode)|| activeLoanMap.size() > 0) {


			}
			if (null != allocationInsertStatement) allocationInsertStatement.close();
			if (null != activeLoansSelectStatement) activeLoansSelectStatement.close();
			if (null != statusSelectStatement) statusSelectStatement.close();
			if (null != allocationSelectStatement) allocationSelectStatement.close();
			if (null != loanRepaymentSelectStatement) loanRepaymentSelectStatement.close();
			if (null != loanRepaymentInsertStatement) loanRepaymentInsertStatement.close();
			if (null != participantInsertStatement) participantInsertStatement.close();

		} catch (SQLException e) {
			throw new SystemException(e, className, "copyParticipantData", "Problem occurred in prepared call: "
					+ SQL_INSERT_SUBMISSION_PARTICIPANT + " for contractId " + contractId + " and newSubmissionId "
					+ newSubmissionId);
		} finally {
			try {
				if (null != rs) rs.close();
				if (null != csdbConnection) csdbConnection.close();
			} catch	(SQLException e) {
				throw new SystemException(e, className, "copyParticipantData",
						"Problem occurred in closing database connection "
						+ " for contractId " + contractId + " and newSubmissionId " + newSubmissionId);
			}
		}

		// update totals
		PreparedStatement updateContributionStatement = null;
		try {
			updateContributionStatement = connection
					.prepareStatement(SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_NEW);
			updateContributionStatement.setBigDecimal(1, totalAllocationAmount);
			updateContributionStatement.setBigDecimal(2, totalWithdrawalAmount);
			updateContributionStatement.setBigDecimal(3,
					totalLoanRepaymentAmount);
			updateContributionStatement.setInt(4, targetRecordNo);
			updateContributionStatement.setString(5, userId);
			updateContributionStatement.setInt(6, newSubmissionId.intValue());
			updateContributionStatement.setInt(7, contractId);
			updateContributionStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SystemException(e, className, "copyParticipantData",
					"Problem occurred in prepared call: "
							+ SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_NEW
							+ " for contractId " + contractId
							+ " and newSubmissionId " + newSubmissionId);
		} finally {
			try {
				if (null != updateContributionStatement)
					updateContributionStatement.close();
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"copyParticipantData",
						"Problem closing updateContributionStatement");
			}
		}

		return moneyTypeList;
	}

	/**
	 * Insert into the submission_contribution_money_type table for each distinct
	 * money type provided.
	 *
	 * @param submissionId
	 * @param contractId
	 * @param userId
	 * @param moneyTypeList
	 *
	 * @throws SystemException
	 */
	private static void insertMoneyTypes(Integer submissionId, Integer contractId, String userId,
			Map moneyTypeList) throws SystemException {

		String moneyTypeId = EMPTY_STRING;
		try {
			SQLInsertHandler handler =
				new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION_CONTRACT_MONEY_TYPES);
			for (Iterator itr = moneyTypeList.values().iterator(); itr.hasNext();) {
				moneyTypeId = (String) itr.next();
				handler.insert(new Object[] {submissionId, contractId, moneyTypeId.trim(), userId, userId});
			}
		} catch (DAOException e) {
			throw new SystemException(e, className, "copyContributionDetails",
					"Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION_CONTRACT_MONEY_TYPES +
					" for contractId " + contractId + " and newSubmissionId " + submissionId +
					" and moneyTypeId " + moneyTypeId);
		}
	}

	/**
	 * given a contractId, create a new submission case and return an object
	 * containing the submissionId of the new submission
	 *
	 * @param ContractId
	 * @param userId
	 * @param userSSN
	 * @param userName
	 * @param userType
	 * @param userTypeId
	 * @param userTypeName
	 * @return CopiedSubmissionHistoryItem
	 * @throws SystemException
	 */
	public static CopiedSubmissionHistoryItem createContributionDetails(
			int contractId, String userId, String userName,
			String userTypeCode, BigDecimal userTypeId, String userTypeName,
			String notificationEmailAddress) throws SystemException {

		logger.debug("entry -> createContributionDetails");
		logger.debug("contractId -> " + contractId);

		Timestamp dummyTimestamp = new Timestamp(System.currentTimeMillis());

		Connection connection = null;
		Integer newSubmissionId = new Integer(0);
		CopiedSubmissionHistoryItem copiedItem = null;

		// start overall try block for the entire method
		try {

			// check participant count isn't above the limit
			Integer activeParticipantCount;
			try {
				SelectSingleValueQueryHandler handler2 = new SelectSingleValueQueryHandler(
						CUSTOMER_DATA_SOURCE_NAME, SQL_ACTIVE_PARTICIPANT_COUNT,
						Integer.class);
				activeParticipantCount = (Integer) handler2.select(new Object[] {new Integer(contractId)});
				if (activeParticipantCount.intValue() > Integer.parseInt(COPY_SIZE_LIMIT)) {
					return null;
				}

			} catch (DAOException e) {
				throw handleDAOException(e, className, "createContributionDetails",
						"Problem occurred in prepared call: " + SQL_ACTIVE_PARTICIPANT_COUNT);
			}

			connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);

			newSubmissionId = getNewSubmissionId();
			copiedItem = new CopiedSubmissionHistoryItem(0, newSubmissionId.intValue());

			String participantSortOption = createSubmissionJournalData(new Integer(contractId), userName,
					userId, userTypeCode, userTypeId, userTypeName, newSubmissionId, dummyTimestamp,
					notificationEmailAddress);

			//Changed the calling method for QC 4773, as payroll date should show the next investment date.
			Date contributionApplicableDate = determinePaymentEffectiveDate();

			createSubmissionCaseData(new Integer(contractId), userId, newSubmissionId,
                    dummyTimestamp, GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR);

			createSubmissionContributionData(new Integer(contractId), userId, newSubmissionId,
					MONEY_SOURCE_REGULAR, REGULAR_TRANSACTION_CODE, ZERO_INTEGER, contributionApplicableDate);

			// copy participant data
			Map moneyTypeList = createParticipantData(connection, contractId,
					newSubmissionId,
					contributionApplicableDate, userId, participantSortOption, copiedItem);

			// insert to the money type table for each distinct money type
			// copied to the allocation table
			insertMoneyTypes(newSubmissionId, new Integer(contractId), userId, moneyTypeList);

		} catch (SQLException e) {
			throw new SystemException(e, className,
					"createContributionDetails", "Problem getting connection!");
		} finally {
			try {
				if (null != connection) connection.close();
			} catch (SQLException f) {
				throw new SystemException(f, className, "createContributionDetails",
						"Problem occurred for contractId " + contractId);
			}
		}

		logger.debug("exit <- createContributionDetails");

		return copiedItem;
	}

	/**
	 * Copy appropriate submission journal data for a given submissionId to a
	 * new submissionId for a given user
	 *
	 * @param contractId
	 * @param submissionId
	 * @param userName
	 * @param userId
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @param newSubmissionId
	 * @param dummyTimestamp
	 * @return participantSortOption
	 * @throws SystemException
	 */
	private static String createSubmissionJournalData(Integer contractId, String userName, String userId,
			String userTypeCode, BigDecimal userTypeId, String userTypeName, Integer newSubmissionId,
			Timestamp dummyTimestamp, String notificationEmailAddress) throws SystemException {

		// get required contract data
		String contractName = EMPTY_STRING;
		String participantSortOption = EMPTY_STRING;
		String locationCode = EMPTY_STRING;
		try {
			SelectMultiFieldQueryHandler handler =
				new SelectMultiFieldQueryHandler(CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_DATA,
						new Class[] {String.class, String.class, String.class});
			Object[] result = (Object[]) handler.select(new Object[] {contractId});
			participantSortOption = (String) result[0];
			contractName = (String) result[1];
			locationCode = (MANULIFE_COMPANY_ID.equals(result[2]) ? USA_LOCATION_CODE
					: NEW_YORK_LOCATION_CODE);

		} catch (DAOException e) {
			throw new SystemException(e, className, "createSubmissionJournalData",
					"Problem occurred in prepared call: " + SQL_SELECT_CONTRACT_DATA +
					" for contractId " + contractId);
		}

		// insert new submission journal row
		try {
			SQLInsertHandler insHandler =
				new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_SUBMISSION_JOURNAL_INSERT);
			insHandler.insert(new Object[] {newSubmissionId, contractId, contractName, userName, userId,
					userTypeCode, userTypeId, userTypeName, locationCode, dummyTimestamp, dummyTimestamp,
					APPLICATION_CODE, GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR, EMPTY_STRING, 
					ZERO_SCALE_2, notificationEmailAddress });

		} catch (DAOException e) {
			throw new SystemException(e, className, "copySubmissionJournalData",
					"Problem occurred in prepared call: " + SQL_SUBMISSION_JOURNAL_INSERT +
					" for contractId " + contractId + " and newSubmissionId " + newSubmissionId);
		}
		createPaymentData(newSubmissionId, contractId);

		return participantSortOption;
	}

	/**
     * Create initial submission contribution data.
     *
	 * @param contractId Contract ID for this contribution submission.
	 * @param userId
	 * @param submissionId
	 * @param moneySourceId
	 * @param transactionCode
	 * @param priorityNumber
	 * @param contributionApplicableDate
	 * @throws SystemException
	 */
	private static void createSubmissionContributionData(final Integer contractId,
            final String userId, final Integer submissionId, final String moneySourceId,
            final String transactionCode, final Integer priorityNumber,
            final Date contributionApplicableDate) throws SystemException {

		// insert new submission contribution row
		try {
            java.sql.Date sqlContributionApplicableDate = getSqlDate(contributionApplicableDate);

            SQLInsertHandler insHandler =
				new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
                    SQL_INSERT_SUBMISSION_CONTRIBUTION, new int[] {
                            Types.INTEGER, // SUBMISSION_ID
                            Types.INTEGER, // CONTRACT_ID
                            Types.CHAR, // SUBMISSION_CASE_TYPE_CODE
                            Types.CHAR, // MONEY_SOURCE_ID
                            Types.DATE, // CONTRIBUTION_APPLICABLE_DATE
                            Types.DECIMAL, // TOTAL_ALLOCATION_AMT
                            Types.DECIMAL, // TOTAL_WITHDRAWAL_AMT
                            Types.DECIMAL, // TOTAL_LOAN_REPAYMENT_AMT
                            Types.INTEGER, // TOTAL_PARTICIPANT_COUNT
                            Types.VARCHAR, // TRANSACTION_CODE
                            Types.SMALLINT, // PRIORITY_NO
                            Types.CHAR, // PROCESSOR_USER_ID
                            Types.VARCHAR, // ERROR_COND_STRING
                            Types.CHAR, // CREATED_USER_ID
                            Types.CHAR // LAST_UPDATED_USER_ID
                            });

			insHandler.insert(new Object[] { submissionId, // SUBMISSION_ID
                    contractId, // CONTRACT_ID
                    GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR, // SUBMISSION_CASE_TYPE_CODE
                    moneySourceId, // MONEY_SOURCE_ID
                    sqlContributionApplicableDate, // CONTRIBUTION_APPLICABLE_DATE
                    ZERO_SCALE_2, // TOTAL_ALLOCATION_AMT
                    ZERO_SCALE_2, // TOTAL_WITHDRAWAL_AMT
                    ZERO_SCALE_2, // TOTAL_LOAN_REPAYMENT_AMT
                    ZERO_INTEGER, // TOTAL_PARTICIPANT_COUNT
                    transactionCode, // TRANSACTION_CODE
                    priorityNumber, // PRIORITY_NO
                    userId, // PROCESSOR_USER_ID
                    EMPTY_STRING, // ERROR_COND_STRING
                    userId,
                    userId });

            // insert submission payment
            insHandler = new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
                    SQL_INSERT_SUBMISSION_PAYMENT, new int[] { Types.INTEGER, // SUBMISSION_ID
                            Types.INTEGER, // CONTRACT_ID
                            Types.CHAR, // SUBMISSION_TYPE_CASE_CODE
                            Types.CHAR, // CREATED_USER_ID
                            Types.CHAR // LAST_UPDATED_USER_ID
                    });

            /*
             * total will be calculated as we loop through the participants and the table updated
             * once we're done
             */
            insHandler.insert(new Object[] { submissionId, contractId,
                    GFTUploadDetail.SUBMISSION_TYPE_CONTRIB_REGULAR, userId, userId });

        } catch (DAOException e) {
			throw new SystemException(e, className, "createSubmissionContributionData",
					"Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION_CONTRIBUTION +
					" for contractId " + contractId + " and submission ID " + submissionId);
		}
	}

	private static Map createParticipantData(Connection connection,
			int contractId, Integer newSubmissionId,
			Date sqlDate, String userId, String participantSortOption, CopiedSubmissionHistoryItem copiedItem)
			throws SystemException {
		// create participant and associated tables
		Map moneyTypeList = new HashMap();
		PreparedStatement participantSelectStatement = null;
		PreparedStatement participantInsertStatement = null;
		PreparedStatement allocationInsertStatement = null;
		PreparedStatement activeLoansSelectStatement = null;
		PreparedStatement loanRepaymentInsertStatement = null;
		int targetRecordNo = 0;

		Connection csdbConnection = null;
		Connection apolloConnection = null;

		try {
			csdbConnection = getDefaultConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			try {
				apolloConnection = getDefaultConnection(className,
						APOLLO_DATA_SOURCE_NAME);
			} catch (SQLException e) {
				// ignore, just don't use this connection
			}
			// get money types valid for the money source
            Set validMoneyTypes = DomainDAO.getValidMoneyTypesForMoneySource(MONEY_SOURCE_REGULAR);
            List contractMoneyTypes = ContractDAO.getContractMoneyTypesFilteredByValidMoneyTypes(
                    new Integer(contractId), true, validMoneyTypes, false);

			String participantStatusCode = EMPTY_STRING;
			BigDecimal participantId = BigDecimal.ZERO;
			String employerDesignatedId = EMPTY_STRING;
			String fullName = EMPTY_STRING;
			ResultSet participantResultSet;
			if (null != apolloConnection) {
				try {
					participantSelectStatement = apolloConnection
							.prepareStatement(SQL_APOLLO_PARTICIPANTS_SELECT);
					participantSelectStatement.setInt(1, contractId);
					participantResultSet = participantSelectStatement
							.executeQuery();
				} catch (SQLException e) {
					// if we fail trying to get the list of participants from
					// APOLLO, try to get them from CSDB
					// (this may not be quite up to date, but is better than nothing
					try {
						participantSelectStatement = csdbConnection
								.prepareStatement(SQL_STATUS_SELECT);
						participantSelectStatement.setInt(1, contractId);
						participantResultSet = participantSelectStatement
								.executeQuery();
					} catch (SQLException f) {
						throw new SystemException(f, className,
								"createParticipantData",
								"Problem occurred in prepared call: "
										+ SQL_STATUS_SELECT + " for contractId "
										+ contractId + " and newSubmissionId "
										+ newSubmissionId);
					}
				}
			} else {
				try {
					participantSelectStatement = csdbConnection
							.prepareStatement(SQL_STATUS_SELECT);
					participantSelectStatement.setInt(1, contractId);
					participantResultSet = participantSelectStatement
							.executeQuery();
				} catch (SQLException f) {
					throw new SystemException(f, className,
							"createParticipantData",
							"Problem occurred in prepared call: "
									+ SQL_STATUS_SELECT + " for contractId "
									+ contractId + " and newSubmissionId "
									+ newSubmissionId);
				}
			}
			try {
				participantInsertStatement = connection
						.prepareStatement(SQL_INSERT_SUBMISSION_PARTICIPANT);
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"createParticipantData",
						"Problem occurred in prepared call: "
								+ SQL_INSERT_SUBMISSION_PARTICIPANT
								+ " for contractId " + contractId
								+ " and newSubmissionId " + newSubmissionId);
			}
			try {
				activeLoansSelectStatement = csdbConnection
						.prepareStatement(SQL_ACTIVE_LOANS_SELECT);
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"createParticipantData",
						"Problem occurred in prepared call: "
								+ SQL_ACTIVE_LOANS_SELECT + " for contractId "
								+ contractId + " and newSubmissionId "
								+ newSubmissionId);
			}
			try {
				allocationInsertStatement = connection
						.prepareStatement(SQL_INSERT_SUBMISSION_ALLOCATION);
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"createParticipantData",
						"Problem occurred in prepared call: "
								+ SQL_INSERT_SUBMISSION_ALLOCATION
								+ " for contractId " + contractId
								+ " and newSubmissionId " + newSubmissionId);
			}
			try {
				loanRepaymentInsertStatement = connection
						.prepareStatement(SQL_INSERT_SUBMISSION_LOAN_REPAYMENT);
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"createParticipantData",
						"Problem occurred in prepared call: "
								+ SQL_INSERT_SUBMISSION_LOAN_REPAYMENT
								+ " for contractId " + contractId
								+ " and newSubmissionId " + newSubmissionId);
			}

			while (participantResultSet.next()) {
				if (participantSortOption.equals(EE_SORT_OPTION)) {
					employerDesignatedId = participantResultSet
							.getString("employeeId");
				} else {
					employerDesignatedId = participantResultSet
							.getString("socialSecurityNo");
				}
				participantStatusCode = participantResultSet
						.getString("participantStatus");
				participantId = participantResultSet
						.getBigDecimal("participantId");
				fullName = participantResultSet.getString("lastName").trim()
						+ ", "
						+ participantResultSet.getString("firstName").trim();

				// get active loans
				ResultSet activeLoansResultSet = null;
				Map activeLoanMap = new HashMap();
				try {
					//BigDecimal tempPrtId = participantId.subtract(new
					// BigDecimal("1000"));
					activeLoansSelectStatement.setBigDecimal(1, participantId);
					//activeLoansSelectStatement.setBigDecimal(1, new
					// BigDecimal("152309"));
					activeLoansSelectStatement.setInt(2, contractId);
					//activeLoansSelectStatement.setInt(2, 88614);
					activeLoansResultSet = activeLoansSelectStatement
							.executeQuery();
					while (activeLoansResultSet.next()) {
						Integer loanId = new Integer(activeLoansResultSet
								.getInt("loanId"));
						activeLoanMap.put(loanId, loanId);
					}
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"createParticipantData",
							"Problem occurred in prepared call: "
									+ SQL_ACTIVE_LOANS_SELECT
									+ " for contractId " + contractId
									+ " and participantId " + participantId);
				} finally {
					try {
						if (null != activeLoansResultSet)
							activeLoansResultSet.close();
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"createContributionDetails",
								"Problem occurred in closing active loans result set "
										+ " for contractId " + contractId
										+ " and newSubmissionId "
										+ newSubmissionId);
					}
				}

				// only copy the participant if it is active or has active loans
				if ((ACTIVE_STATUS.equals(participantStatusCode)
						|| activeLoanMap.size() > 0) && !employerDesignatedId.trim().equals(EMPTY_STRING)) {

					targetRecordNo++;
					participantInsertStatement.setInt(1, newSubmissionId.intValue());
					participantInsertStatement.setInt(2, contractId);
					participantInsertStatement.setString(3,
							employerDesignatedId);
					// ensure data size not greater than column size
					if (fullName.length() > 40) {
						fullName = fullName.substring(0, 40);
					}
					participantInsertStatement.setString(4, fullName);
					participantInsertStatement.setString(5, EMPTY_STRING);
					participantInsertStatement.setInt(6, targetRecordNo);
					participantInsertStatement.setString(7, userId);
					participantInsertStatement.setString(8, userId);

					try {
						participantInsertStatement.executeUpdate();
					} catch (SQLException e) {
						// if duplicate key, ignore error, but report on edit page
						if (e.getErrorCode() == DUPLICATE_KEY_EXCEPTION_SQLCODE) {
							copiedItem.getParticipantsNotCopiedNonUniqueId().put(fullName, fullName);
						} else {
							throw new SystemException(e, className,
									"createParticipantData",
									"Problem occurred in prepared call: "
										+ SQL_INSERT_SUBMISSION_PARTICIPANT
										+ " for contractId " + contractId
										+ " and newSubmissionId "
										+ newSubmissionId
										+ " and employerDesignatedId "
										+ employerDesignatedId);
						}
					}

					//  for first participant, create $0 alloaction for each
					// money type to allow edit page to show
					//  all money types available to the contract
					if (targetRecordNo == 1) {
						String moneyTypeId = EMPTY_STRING;
						try {
							for (Iterator moneyTypeIterator = contractMoneyTypes.iterator(); moneyTypeIterator
                                    .hasNext();) {
                                MoneyTypeVO moneyTypeVo = (MoneyTypeVO) moneyTypeIterator.next();
								moneyTypeId = moneyTypeVo.getId();
								allocationInsertStatement.setInt(1,
										newSubmissionId.intValue());
								allocationInsertStatement.setInt(2, contractId);
								allocationInsertStatement.setString(3,
										employerDesignatedId);
								allocationInsertStatement.setString(4,
										moneyTypeId);
								allocationInsertStatement.setInt(5, 0);
								allocationInsertStatement.setBigDecimal(6,
										ZERO_SCALE_2);
                                allocationInsertStatement.setDate(7, getSqlDate(sqlDate));
								allocationInsertStatement.setString(8, EMPTY_STRING);
								allocationInsertStatement.setInt(9,
										targetRecordNo);
								allocationInsertStatement.setString(10, userId);
								allocationInsertStatement.setString(11, userId);

								allocationInsertStatement.executeUpdate();

								// save each money type so we can insert it to
								// the money type table at the end
								moneyTypeList.put(moneyTypeId, moneyTypeId);
							}
						} catch (SQLException e) {
							if ( logger.isDebugEnabled() )
									logger.debug("Error inserting to allocation table. Set statement parameters to: "
											+ "1["
											+ newSubmissionId
											+ "] 2["
											+ contractId
											+ "] 3["
											+ employerDesignatedId
											+ "] 4["
											+ moneyTypeId
											+ "] 5"
											+ 0
											+ "] 6["
											+ ZERO_SCALE_2
											+ "] 7["
											+ sqlDate
											+ "] 8["
											+ EMPTY_STRING
											+ "] 9["
											+ targetRecordNo
											+ "] 10["
											+ userId
											+ "] 11[" + userId + "].");
							throw new SystemException(e, className,
									"createParticipantData",
									"Problem occurred in prepared call: "
											+ SQL_INSERT_SUBMISSION_ALLOCATION
											+ " for contractId " + contractId
											+ " and newSubmissionId "
											+ newSubmissionId);
						}

					}

					// insert loan_repyament of $0 for each active loan
					try {
						for (Iterator loanIterator = activeLoanMap.keySet()
								.iterator(); loanIterator.hasNext();) {
							int loanId = ((Integer) loanIterator.next())
									.intValue();

							loanRepaymentInsertStatement.setInt(1,
									newSubmissionId.intValue());
							loanRepaymentInsertStatement.setInt(2, contractId);
							loanRepaymentInsertStatement.setString(3,
									employerDesignatedId);
							loanRepaymentInsertStatement.setInt(4, loanId);
							loanRepaymentInsertStatement.setInt(5, 0);
							loanRepaymentInsertStatement.setBigDecimal(6,
									ZERO_SCALE_2);
                            loanRepaymentInsertStatement.setDate(7, getSqlDate(sqlDate));
							loanRepaymentInsertStatement.setString(8, EMPTY_STRING);
							loanRepaymentInsertStatement.setInt(9,
									targetRecordNo);
							loanRepaymentInsertStatement.setString(10, userId);
							loanRepaymentInsertStatement.setString(11, userId);

							loanRepaymentInsertStatement.executeUpdate();
						}
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"createParticipantData",
								"Problem occurred in prepared call: "
										+ SQL_INSERT_SUBMISSION_LOAN_REPAYMENT
										+ " for contractId " + contractId
										+ " and newSubmissionId "
										+ newSubmissionId
										+ " and employerDesignatedId "
										+ employerDesignatedId);
					}
				} else {
					// if participant doesn't have an id, report on edit page
					if ((ACTIVE_STATUS.equals(participantStatusCode)
							|| activeLoanMap.size() > 0) && employerDesignatedId.trim().equals(EMPTY_STRING)) {
						copiedItem.getParticipantsNotCopiedNonUniqueId().put(fullName, fullName);
					}
				} // ends if (ACTIVE_STATUS.equals(participantStatusCode)||
				  // activeLoanMap.size() > 0) {

			}
			allocationInsertStatement.close();
			activeLoansSelectStatement.close();
			loanRepaymentInsertStatement.close();
			participantInsertStatement.close();
			participantSelectStatement.close();

		} catch (SQLException e) {
			throw new SystemException(e, className, "createParticipantData",
					"Problem occurred for contractId " + contractId
							+ " and newSubmissionId " + newSubmissionId);
		} finally {
			try {
				if (csdbConnection != null) {
					csdbConnection.close();
				}
				if (apolloConnection != null) {
					apolloConnection.close();
				}
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"createContributionDetails",
						"Problem occurred in closing database connection "
								+ " for contractId " + contractId
								+ " and newSubmissionId " + newSubmissionId);
			}
		}

		// update totals - for create only the number of participants has a
		// value; the amounts are all zero
		try {
			PreparedStatement updateContributionStatement = connection
					.prepareStatement(SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_NEW);
			updateContributionStatement.setBigDecimal(1, ZERO_SCALE_2);
			updateContributionStatement.setBigDecimal(2, ZERO_SCALE_2);
			updateContributionStatement.setBigDecimal(3, ZERO_SCALE_2);
			updateContributionStatement.setInt(4, targetRecordNo);
			updateContributionStatement.setString(5, userId);
			updateContributionStatement.setInt(6, newSubmissionId.intValue());
			updateContributionStatement.setInt(7, contractId);
			updateContributionStatement.executeUpdate();
		} catch (SQLException e) {
			throw new SystemException(e, className, "createParticipantData",
					"Problem occurred in prepared call: "
							+ SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_NEW
							+ " for contractId " + contractId
							+ " and newSubmissionId " + newSubmissionId);
		}

		return moneyTypeList;
	}

	/**
	 *
	 * @param contractId
	 * @return SQL for selecting the particpant depending on the participant
	 *         sort option
	 */

	private static String getStatusStatement(int contractId)
			throws SystemException {

		String participantSortOption = EMPTY_STRING;
		try {
			SelectSingleValueQueryHandler handler3 = new SelectSingleValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_DATA,
					String.class);
			participantSortOption = (String) handler3
					.select(new Object[] { new Integer(contractId) });
		} catch (DAOException e) {
			throw handleDAOException(e, className, "copyContributionDetails",
					"Problem occurred in prepared call: "
							+ SQL_SELECT_CONTRACT_DATA + " with contract id "
							+ contractId);
		}

		String statusSelectString = null;
		if (participantSortOption.equals(EE_SORT_OPTION)) {
			statusSelectString = SQL_STATUS_SELECT + EE_SELECT_CLAUSE;
		} else {
			statusSelectString = SQL_STATUS_SELECT + SSN_SELECT_CLAUSE;
		}

		return statusSelectString;
	}



	/**
	 * Generates the query and the array of parameters from the criteria.
	 *
	 * @param query
	 *            The base query
	 * @param ReportCriteria
	 *            The filters and sorts for the report
	 * @param params
	 *            The generated array of parameters
	 */
	private static String generateQuery(ReportCriteria criteria, List paramList)
			throws SystemException {

		StringBuffer query = new StringBuffer(SQL_HISTORY_SELECT);
		query.append(AND_SUBMISSION_DATE_CLAUSE);

        Integer contractId = (Integer)criteria
            .getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO);

		// contract number filter is mandatory
		paramList.add(contractId);

		Calendar cal = Calendar.getInstance();

		// submission start date filter
		Date startSubmissionDate = (Date) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_START_SUBMISSION_DATE);
		if (startSubmissionDate == null) {
			cal.add(Calendar.YEAR, -DEFAULT_YEAR_INTERVAL);
			startSubmissionDate = cal.getTime();
		}
		paramList.add(startSubmissionDate);

		// submission end date filter
		Date endSubmissionDate = (Date) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_END_SUBMISSION_DATE);
		if (endSubmissionDate == null) {
			// no end submission date filter - want to get everything that's been submitted from the start date to present
			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.HOUR, 2); // add 2 hrs into the future to get around clock synchronization issues
			endSubmissionDate = cal2.getTime();
		}
		paramList.add(endSubmissionDate);

		String submissionType = (String) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_TYPE);
		if (submissionType != null) {
			query.append(AND_TYPE_CLAUSE);
			paramList.add(submissionType);
		}
		
		//The status filter is added for advanced Search option.
		//This checks if the status is selected, then brings all the values and append in the query.
		String newStatus=null;
		String status = (String) criteria.getFilterValue(SubmissionHistoryReportData.FILTER_STATUS);
		if (status != null) {
			newStatus = StatusGroupHelper.getInstance().getAllStatusesByValue(status);
			query.append(AND_STATUS_CLAUSE_START);
			query.append(newStatus);
			query.append(AND_STATUS_CLAUSE_END);
		}
		Long submitterId = (Long) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_SUBMITTER_ID);
		if (submitterId != null) {
			String submitterIdClause = AND_SUBMITTER_ID_SIMPLE_CLAUSE;
			paramList.add(submitterId.toString());
			String ssn = getSSN(submitterId);
			if (ssn != null) {
				submitterIdClause = AND_SUBMITTER_ID_IN_CLAUSE;
				paramList.add(ssn);
			}
			query.append(submitterIdClause);
		}

        Long tpaUserId = (Long) criteria
                .getFilterValue(SubmissionHistoryReportData.FILTER_TPA_USER_ID);
        if (tpaUserId != null) {
            /*
             * Get a list of TPA users within the same firm.
             */
            List tpaUserIds = getAccessibleTpaUserIds(tpaUserId.intValue(),
                    contractId.intValue());
            if (tpaUserIds.size() > 0) {
                /*
                 * Query needs to be constructed manually because STP and CSDB
                 * are not linked. If the above query does not return any
                 * result, that means:
                 *
                 * 1. TPA user has VWAS and TPA firm has VWAS
                 * 2. TPA user does NOT have VWAS
                 *
                 * Case 1 means we should not add conditions to the query,
                 * so it is fine.
                 * Case 2 should be caught by the Action class. The Action
                 * class will supply SUBMITTER_ID instead of TPA_USER_ID if
                 * TPA user does NOT have VWAS.
                 */
                query.append(AND_SUBMITTER_ID_IN_CLAUSE2).append("(");
                for (Iterator it = tpaUserIds.iterator(); it.hasNext();) {
                    Integer userId = (Integer)it.next();
                    query.append("'").append(userId.toString()).append("'");
                    String ssn = getSSN(new Long(userId.longValue()));
                    if (ssn != null) {
                        query.append(",'").append(ssn).append("'");
                    }
                    if (it.hasNext()) {
                        query.append(",");
                    }
                }
                query.append(") ");
            }
        }

		Date startPayrollDate = (Date) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_START_PAYROLL_DATE);
		if (startPayrollDate != null) {
			query.append(AND_START_PAYROLL_DATE_CLAUSE);
			paramList.add(startPayrollDate);
		}

		Date endPayrollDate = (Date) criteria
				.getFilterValue(SubmissionHistoryReportData.FILTER_END_PAYROLL_DATE);
		if (endPayrollDate != null) {
			query.append(AND_END_PAYROLL_DATE_CLAUSE);
            /*
             * If we use parameter substitution for end date, we will get an
             * exception when the query's result set is retrieved. It is not
             * clear why this is the case. You can reproduce the problem by
             * using parameter substitution, shutdown and restart the server.
             * (NOTE, it only occurs on the first attempt to filter by payroll
             * date (with end date) after a server restart)
             *
             * paramList.add(endPayrollDate);
             */
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            query.append("DATE('").append(
            		dateFormat.format(endPayrollDate)).append("') ");
		}

		query.append(AND_SYSTEM_STATUS_CLAUSE);
		query.append(SQL_HISTORY_SUFIX);

		return query.toString();
	}

	/**
	 * @param submitterId
	 * @return @throws
	 *         SystemException
	 */
	private static String getSSN(Long submitterId) throws SystemException {
		String ssn = null;
		try {
			logger.debug("Executing Prepared SQL Statment: " + SQL_SSN_SELECT);
			SelectSingleOrNoValueQueryHandler handler = new SelectSingleOrNoValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SSN_SELECT, String.class);
			ssn = (String) handler.select(new Object[] { submitterId });
		} catch (DAOException e) {
			throw handleDAOException(e, className, "getSSN",
					"Problem occurred prepared call: " + SQL_SSN_SELECT
							+ " with submitter ID: " + submitterId);
		}
		return ssn != null ? ssn.trim() : ssn;
	}

	/**
	 * Iterate through the item list and perform any post-retrieval massaging of
	 * the data before passing it back
	 *
	 * @param itemsList
	 * @return
	 */
	private static List massageItems(List itemsList, Integer contractID) throws SystemException {

		// load the valid money source ids for this contract
		List validMoneySourceIDs = new ArrayList(loadMoneySourceIDs(contractID).keySet());

		for (int i = 0; i < itemsList.size(); i++) {
			SubmissionHistoryItem item = (SubmissionHistoryItem) itemsList .get(i);

			// next validate the money source
			validateMoneySourceID(item, validMoneySourceIDs);

			// find out if submitter was internal or external user
			setSubmitterType(item);
		}
		return itemsList;
	}

	/**
	 * Validates that the money source of the submission history item is valid
	 * for this contract
	 *
	 * @param item
	 * @param validMoneySourceIDs
	 */
	private static void validateMoneySourceID(SubmissionHistoryItem item,
			List validMoneySourceIDs) {
		if (validMoneySourceIDs == null
				|| !validMoneySourceIDs.contains((item.getMoneySourceID()))) {
			item.setValidMoneySource(false);
		} else {
			item.setValidMoneySource(true);
		}
	}

	/**
	 * Loads the money source IDs for the current contract from the CSDB
	 *
	 * @param criteria
	 * @return @throws
	 *         SystemException
	 */
	private static Map loadMoneySourceIDs(Integer contractID)
			throws SystemException {
		Map contractMoneySourceIDs = new HashMap();
		if (contractID == null)
			return contractMoneySourceIDs;
		//Integer contractID =
		// (Integer)criteria.getFilterValue(SubmissionHistoryReportData.FILTER_CONTRACT_NO);

		SelectMultiFieldMultiRowQueryHandler handler = new SelectMultiFieldMultiRowQueryHandler(
				CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_MONEY_SOURCE,
				new Class[] { String.class, String.class });
		try {
			logger.debug("Executing Prepared SQL Statment: "
					+ SQL_SELECT_MONEY_SOURCE);
			Object[][] ids = ((Object[][]) handler
					.select(new Integer[] { contractID }));
			for (int i = 0; i < ids.length; i++) {
				// unfortunately, the specs use a different literal from that
				// the the MoneySourceDescription, so we override it here
				if (((String) ids[i][0]).trim().equals(
						MoneySourceDescription.REGULAR_CODE)) {
					contractMoneySourceIDs.put(((String) ids[i][0]).trim(),
							REGULAR_CONTRIBUTION);
				} else {
					contractMoneySourceIDs
							.put(
									((String) ids[i][0]).trim(), MoneySourceDescription
									.getViewDescription(((String) ids[i][0]).trim())
									);
				}
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className, "loadMoneySourceIDs",
					"Problem occurred prepared call: "
							+ SQL_SELECT_MONEY_SOURCE + " with contractID: "
							+ contractID);
		}
		return contractMoneySourceIDs;
	}

    /**
     * @param submissionId
     * @param contractNumber
     * @param typeCode
     * @throws SystemException
     */
    public static void cancelSubmission(int subId, int contractId, String typeCode) throws SystemException {

        // null check
        if (typeCode == null) {
            return;
        }

        Connection connection = null;

        logger.debug("entry -> cancelSubmission");

        try {
            try {
                connection = getDefaultConnection(className,
                        SUBMISSION_DATA_SOURCE_NAME);
            } catch (SQLException e) {
                throw new SystemException(e, className,
                        "cancelSubmission",
                        "Problem occurred getting a connection");
            }

            PreparedStatement submissionCancelStatement = null;
            try {
                submissionCancelStatement = connection
                        .prepareStatement(SQL_CANCEL_SUBMISSION);
                submissionCancelStatement.setInt(1, subId);
                submissionCancelStatement.setInt(2, contractId);
                submissionCancelStatement.setString(3, typeCode);
                submissionCancelStatement.executeUpdate();
            } catch (SQLException e) {
                throw new SystemException(e, className,
                        "cleanupVestingDetails",
                        "Problem occurred in prepare statement for: "
                                + SQL_CANCEL_SUBMISSION
                                + " with submissionNumber: " + subId
                                + " contractNumber: " + contractId);
            } finally {
                close(submissionCancelStatement, null);
            }

        } finally {
            close(null, connection);
        }

        logger.debug("exit <- cancelSubmission");
    }

	/**
	 * @param submissionId
	 * @param contractNumber
	 * @param typeCode
	 * @throws SystemException
	 */
	public static void deleteSubmission(int subId, int contractNumber, String typeCode) throws SystemException {

		// null check
		if (typeCode == null) {
			return;
		}

		Integer contractNo = new Integer(contractNumber);
		Integer submissionId = new Integer(subId);

		logger.debug("entry -> deleteSubmission");

		// determine if this case has any siblings (i.e., if this was a combo
		// (contribution+address) submission)
		// because we only want to delete the one case and not the other if so
		boolean isComboSubmission = false;
		try {
			SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					SQL_SELECT_COUNT_SUBMISSION_CASES, Integer.class);
			Integer numCases = (Integer) handler.select(new Object[] {
					submissionId, contractNo });
			if (numCases.intValue() > 1) {
				isComboSubmission = true;
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className, "deleteSubmission",
					"Problem occurred in prepared call: "
							+ SQL_SELECT_COUNT_SUBMISSION_CASES
							+ " with submission id " + submissionId);
		}

		if (SubmissionConstants.SUBMISSION_CASE_TYPE_ADDRESS.equals(typeCode)) {
			// delete address records
			try {
				SQLDeleteHandler handler = new SQLDeleteHandler(
						SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_EMPLOYEE_CENSUS_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_ADDRESS_SUBMISSION_CASE_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
			} catch (DAOException e) {
				throw handleDAOException(
						e,
						className,
						"deleteSubmission",
						"Problem occurred deleting address records with submission id "
								+ subId);
			}
		} else {
			// delete contribution records
			try {
				SQLDeleteHandler handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_SUBMISSION_CONTRIBUTION_MONEY_TYPE_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_SUBMISSION_LOAN_REPAYMENT_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_SUBMISSION_ALLOCATION_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(
						SUBMISSION_DATA_SOURCE_NAME, SQL_DELETE_STP_ROLLUP_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_SUBMISSION_PARTICIPANT_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_SUBMISSION_PAYMENT_INSTRUCTION_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_SUBMISSION_CONTRIBUTION_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
                handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
                        SQL_DELETE_SUBMISSION_PAYMENT_ITEM);
                handler.delete(new Object[] { submissionId, contractNo });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_CONTRIBUTION_SUBMISSION_CASE_ITEM);
				handler.delete(new Object[] { submissionId, contractNo });
			} catch (DAOException e) {
				throw handleDAOException(
						e,
						className,
						"deleteSubmission",
						"Problem occurred deleting contribution records with submission id "
								+ subId);
			}
		}

		if (!isComboSubmission) {
			try {
				// delete the parent submission
				SQLDeleteHandler handler = new SQLDeleteHandler(
						SUBMISSION_DATA_SOURCE_NAME, SQL_DELETE_SUBMISSION_ITEM);
				handler.delete(new Object[] { submissionId });

				// delete the SJ records
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_GFT_PAYMENT_DETAILS_ITEM);
				handler.delete(new Object[] { submissionId });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_GFT_PAYMENT_INFO_ITEM);
				handler.delete(new Object[] { submissionId });
				handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_SUBMISSION_JOURNAL_ITEM);
				handler.delete(new Object[] { submissionId });
			} catch (DAOException e) {
				throw handleDAOException(e, className, "deleteSubmission",
						"Problem occurred deleting SJ records with submissionId "
								+ submissionId.intValue());
			}

		}

		logger.debug("exit <- deleteSubmission");
	}

	/**
	 * Gets the payment only submission according to the criteria
	 *
	 * @param submissionNumber
	 * @param contractNumber
	 * @return The SubmissionPaymentItem
	 * @throws SystemException
	 */
	public static SubmissionPaymentItem getPaymentOnlySubmission(
			int submissionNumber, int contractNumber) throws SystemException {
		// the user file type code for payment only submissions is spaces, so
		// pass that to the worker method
		return getPaymentOnlySubmission(submissionNumber, contractNumber, "P",
				"P");
	}

	/**
	 * Gets the payment information according to the criteria
	 *
	 * @param submissionNumber
	 * @param contractNumber
	 * @return The SubmissionPaymentItem
	 * @throws SystemException
	 */
	private static SubmissionPaymentItem getPaymentOnlySubmission(
			int submissionNumber, int contractNumber, String submissionTypeCode1,
			String submissionTypeCode2) throws SystemException {
		logger.debug("entry -> getPaymentOnlySubmission");
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;

		boolean isNY = isNY(contractNumber);

		if (submissionNumber == 0 || contractNumber == 0)
			return null;
		SubmissionPaymentItem item = null;

		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Executing Prepared SQL Statment: "
						+ SQL_GET_PAYMENT_ONLY_ITEM
						+ "\nParameters: [submissionNumber " + submissionNumber
						+ "] [contractNumber " + contractNumber
						+ "] [submissionTypeCode1 " + submissionTypeCode1
						+ "] [submissionTypeCode2 " + submissionTypeCode2 + "]");
			}

			conn = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);
			statement = conn.prepareStatement(SQL_GET_PAYMENT_ONLY_ITEM);
			// parameterized the search for the case type with the submissionTypeCode1
			statement.setString(1, submissionTypeCode1);
			statement.setInt(2, submissionNumber);
			statement.setInt(3, contractNumber);
			statement.setString(4, submissionTypeCode1);
			statement.setString(5, submissionTypeCode2);
			rs = statement.executeQuery();

			List paymentList = new ArrayList();

			// first fill the common header information and the first payment
			// value
			if (rs.next()) {
				item = new SubmissionPaymentItem();
				item.setSubmissionId(new Integer(submissionNumber));
				item.setContractId(new Integer(rs.getInt("CONTRACT_ID")));
				item.setContractName(rs.getString("CONTRACT_NAME"));
				item.setSubmitterType(rs.getString("USER_TYPE"));
				item.setSubmitterID(rs.getString("USER_SSN"));
				item.setSubmitterName(rs.getString("USER_NAME"));
				item.setSubmissionDate(rs.getTimestamp("UPLOAD_END_TS"));
				item.setRequestedPaymentEffectiveDate(rs
						.getDate("REQUESTED_PAYMENT_EFFECT_DATE"));
				item.setSystemStatus(rs.getString("PROCESS_STATUS_CODE"));
				item.setType(GFTUploadDetail.SUBMISSION_TYPE_PAYMENT);

				PaymentAccount account = null;
				if ("D".equals(rs.getString("ACCOUNT_TYPE_CODE"))) {
					DirectDebitAccount dbAccount = new DirectDebitAccount();
					dbAccount.setBankAccountNumber(rs.getString("ACCOUNT_NO"));
					dbAccount.setBankTransitNumber(String.valueOf(rs
							.getInt("TRANSIT_NO")));
					dbAccount.setInstructionNumber(rs
							.getString("INSTRUCTION_NO"));
					dbAccount.setBankName(rs.getString("BANK_NAME"));
					dbAccount.setBankAccountDescription(rs
							.getString("ACCOUNT_NAME"));
					account = dbAccount;
				} else {
					account = new CashAccount(isNY);
				}

				PaymentInstruction instruction = new PaymentInstruction(
						account,
						new MoneyBean(rs.getBigDecimal("PAYMENT_AMT")), rs
								.getString("PURPOSE_CODE"));
				paymentList.add(instruction);

				// next lets fill the payment details
				while (rs.next()) {
					PaymentAccount paymentAccount = null;
					if ("D".equals(rs.getString("ACCOUNT_TYPE_CODE"))) {
						DirectDebitAccount dbAccount = new DirectDebitAccount();
						String accountNo = rs.getString("ACCOUNT_NO");
						if (accountNo != null)
							accountNo = accountNo.trim();
						dbAccount.setBankAccountNumber(accountNo);
						dbAccount.setBankTransitNumber(String.valueOf(rs
								.getInt("TRANSIT_NO")));
						dbAccount.setInstructionNumber(rs
								.getString("INSTRUCTION_NO"));
						dbAccount.setBankName(rs.getString("BANK_NAME"));
						dbAccount.setBankAccountDescription(rs
								.getString("ACCOUNT_NAME"));
						paymentAccount = dbAccount;
					} else {
						paymentAccount = new CashAccount(isNY);
					}

					PaymentInstruction paymentInstruction = new PaymentInstruction(
							paymentAccount,
							new MoneyBean(rs.getBigDecimal("PAYMENT_AMT")), rs
									.getString("PURPOSE_CODE"));
					paymentList.add(paymentInstruction);
				}
			}
			if (item != null) {
				PaymentInstruction[] instructions = new PaymentInstruction[paymentList
						.size()];
				item.setPaymentInstructions((PaymentInstruction[]) paymentList
						.toArray(instructions));
			}

		} catch (SQLException e) {
			throw new SystemException(e, className, "getPaymentOnlySubmission",
					"Problem occurred during SQL_GET_PAYMENT_ONLY_ITEM call. Input parameter is "
							+ "submissionNumber:" + submissionNumber);
		} finally {
			close(statement, conn);
		}

		getUserName(item);

		logger.debug("exit <- getPaymentOnlySubmission");
		return item;
	}

	private static void getUserName(SubmissionPaymentItem submissionPaymentItem) throws SystemException {

		if (submissionPaymentItem == null) {
			return;
		}
		String userID = submissionPaymentItem.getSubmitterID();

		// override user name only if the id is a profile id
		if (userID == null || userID.length() != 7) {
			return;
		}

		Integer profileID = null;
		try {
			profileID = new Integer(userID);
		} catch (NumberFormatException e) {
			// this is okay, just return to use the value already set
			return;
		}

		try {
			SelectMultiFieldQueryHandler handler =
				new SelectMultiFieldQueryHandler(CUSTOMER_DATA_SOURCE_NAME, SQL_USER_NAME_SELECT_BY_PROFILE,
						new Class[] {String.class, String.class});

			Object[] result = (Object[]) handler.select(new Object[] {profileID});

			// if the select doesn't return anything, the name will stay at the value already set
			if (result != null) {
				submissionPaymentItem.setSubmitterName(result[1] + ", " + result[0]);
			}

		} catch (DAOException e) {
			throw new SystemException(e, className, "getUserName",
					"Problem occurred in prepared call: " + SQL_USER_NAME_SELECT_BY_PROFILE +
					" with profile ID: " + profileID + ", contract ID " + submissionPaymentItem.getContractId() +
					", submission ID " + submissionPaymentItem.getSubmissionId());
		}
	}

	public static AddableParticipantList getParticipantList(int submissionId,
			int contractId) throws SystemException {

		logger.debug("entry -> getParticipantList");
		logger.debug("submissionId -> " + submissionId);
		logger.debug("contractId -> " + contractId);

		AddableParticipantList participants = null;

		Connection connection = null;
		try {
			connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);
			participants = getParticipantList(connection, submissionId, contractId);
		} catch (SQLException e) {
			throw new SystemException(e, className, "getParticipantList",
					"Problem getting connection!");
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new SystemException(e, className, "getParticipantList",
						"Problem occurred in closing STP database connection "
								+ " for contractId " + contractId
								+ " and submissionId " + submissionId);
			}
		}
		logger.debug("exit <- getParticipantList");

		return participants;
	}

	private static AddableParticipantList getParticipantList(Connection connection, int submissionId, int contractId)
			throws SystemException {

		List participantList = new ArrayList();

		// get participant sort option
		String participantSortOption = EMPTY_STRING;
		try {
			SelectSingleValueQueryHandler handler3 = new SelectSingleValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_DATA, String.class);
			participantSortOption = (String) handler3.select(new Object[] { new Integer(contractId) });
		} catch (DAOException e) {
			throw handleDAOException(e, className, "getParticipantList",
					"Problem occurred in prepared call: " + SQL_SELECT_CONTRACT_DATA +
					" with contract id " + contractId);
		}

		// ensure case has correct status
		String systemStatus = null;
		try {
			SelectSingleOrNoValueQueryHandler queryHandler =
				new SelectSingleOrNoValueQueryHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_CASE_STATUS_SELECT, String.class);
			systemStatus = (String) queryHandler.select( new Object[] {new Integer(submissionId), new Integer(contractId)});
		} catch (DAOException e) {
			throw handleDAOException(e, className, "addParticipantsToContribution",
					"Problem occurred in prepared call: " + SQL_CASE_STATUS_SELECT + " for contractId "+ contractId +
					" and submissionId " + submissionId);
		}

		// first get any participants already added to the case
		Connection stpConnection = null;
		PreparedStatement stpParticipantStatement = null;
		ResultSet participantResultSet = null;
		AddableParticipant participant = null;
 		try {
			stpConnection = getDefaultConnection(className,
					SUBMISSION_DATA_SOURCE_NAME);
			try {
				stpParticipantStatement = stpConnection
						.prepareStatement(SQL_PARTICIPANT_SELECT + NEGATIVE_RECORD_NUMBER_CLAUSE);
				stpParticipantStatement.setInt(1, submissionId);
				stpParticipantStatement.setInt(2, contractId);
				participantResultSet = stpParticipantStatement.executeQuery();
				while (participantResultSet.next()) {
					participant = new AddableParticipant(participantResultSet.getString("employerDesignatedId"),
							participantResultSet.getString("fullName"), null);
					participantList.add(participant);
				}
				if (participantResultSet != null) {
					participantResultSet.close();
				}
			} catch (SQLException e) {
				throw new SystemException(e, className, "getParticipantList",
						"Problem occurred in prepared call: "
								+ SQL_PARTICIPANT_SELECT + NEGATIVE_RECORD_NUMBER_CLAUSE
								+ " for submissionId " + submissionId
								+ " contractId " + contractId);
			} finally {
				if (stpParticipantStatement != null) {
					try {
						stpParticipantStatement.close();
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"getParticipantList",
								"Problem occurred in closing prepared statement "
										+ SQL_PARTICIPANT_SELECT + NEGATIVE_RECORD_NUMBER_CLAUSE
										+ " for submissionId " + submissionId
										+ " contractId " + contractId);
					}
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, className, "getParticipantList",
					"Problem occurred in creating stp connection");
		} finally {
			if (stpConnection != null) {
				try {
					stpConnection.close();
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"getParticipantList",
							"Problem occurred in closing stp database connection");
				}
			}
		}

		// create map of participants included on the submission
		PreparedStatement participantStatement = null;
		ResultSet rs = null;
		Map alreadyIncludedParticipants = new HashMap();
		try {
			String participantSelect = SQL_PARTICIPANT_SELECT + ID_ORDER_CLAUSE;
			participantStatement = connection
					.prepareStatement(participantSelect);
			participantStatement.setInt(1, submissionId);
			participantStatement.setInt(2, contractId);

			rs = participantStatement.executeQuery();

			while (rs.next()) {
				alreadyIncludedParticipants.put(rs.getString("employerDesignatedId"),
						rs.getString("sourceRecordNo"));
			}
		} catch (SQLException e) {

			throw new SystemException(e, className, "getParticipantList",
					"Problem occurred prepared call: " + SQL_PARTICIPANT_SELECT
							+ " with submissionNumber: " + submissionId
							+ " contractNumber: " + contractId);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"getParticipantList",
							"Problem occurred in closing result set for: "
									+ SQL_PARTICIPANT_SELECT
									+ " with contract id " + contractId
									+ " and submissionId " + submissionId);
			}
		}
			if (participantStatement != null) {
				try {
					participantStatement.close();
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"getParticipantList",
							"Problem occurred in closing statement: "
									+ SQL_PARTICIPANT_SELECT
									+ " with contract id " + contractId
									+ " and submissionId " + submissionId);
				}
			}
		}

		// loop through the participants on apollo (if available) or csdb, creating an entry in the add
		// participant list only if
		// a. the participant is active, or
		// b. the participant has at least one outstanding loan
		PreparedStatement participantSelectStatement = null;
		Connection csdbConnection = null;
		Connection apolloConnection = null;
		PreparedStatement activeLoansSelectStatement = null;
		BigDecimal participantId = null;
		try {
			csdbConnection = getDefaultConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			try {
				apolloConnection = getDefaultConnection(className,
						APOLLO_DATA_SOURCE_NAME);
			} catch (SQLException e) {
				// ignore, just don't use this connection
			}
			String participantStatusCode = EMPTY_STRING;
			String fullName = EMPTY_STRING;
			if (null != apolloConnection ) {
				try {
					participantSelectStatement = apolloConnection.prepareStatement(SQL_APOLLO_PARTICIPANTS_SELECT);
					participantSelectStatement.setInt(1, contractId);
					participantResultSet = participantSelectStatement.executeQuery();
				} catch (SQLException e) {
					// if we fail trying to get the list of participants from
					// APOLLO, try to get them from CSDB
					// (this may not be quite up to date, but is better than nothing
					try {
						participantSelectStatement = csdbConnection.prepareStatement(SQL_STATUS_SELECT);
						participantSelectStatement.setInt(1, contractId);
						participantResultSet = participantSelectStatement.executeQuery();
					} catch (SQLException f) {
						throw new SystemException(f, className,
								"createParticipantData",
								"Problem occurred in prepared call: "
								+ SQL_STATUS_SELECT + " for contractId "
								+ contractId + " and submissionId "
								+ submissionId);
					}
				}
			} else {
				try {
					participantSelectStatement = csdbConnection
							.prepareStatement(SQL_STATUS_SELECT);
					participantSelectStatement.setInt(1, contractId);
					participantResultSet = participantSelectStatement
							.executeQuery();
				} catch (SQLException f) {
					throw new SystemException(f, className,
							"createParticipantData",
							"Problem occurred in prepared call: "
									+ SQL_STATUS_SELECT + " for contractId "
									+ contractId + " and submissionId "
									+ submissionId);
				}
			}
			try {
				while (participantResultSet.next()) {
					participantStatusCode = participantResultSet
							.getString("participantStatus");
					int loanCount = 0;
					if (!ACTIVE_STATUS.equals(participantStatusCode)) {
						participantId = participantResultSet
								.getBigDecimal("participantId");

						// get active loans
						ResultSet activeLoansResultSet = null;
						try {
							activeLoansSelectStatement = csdbConnection
									.prepareStatement(SQL_ACTIVE_LOANS_SELECT);
							activeLoansSelectStatement.setBigDecimal(1,
									participantId);
							activeLoansSelectStatement.setInt(2, contractId);
							activeLoansResultSet = activeLoansSelectStatement
									.executeQuery();
							while (activeLoansResultSet.next()) {
								loanCount++;
							}
						} catch (SQLException e) {
							throw new SystemException(e, className,
									"getParticipantList",
									"Problem occurred in prepared call: "
											+ SQL_ACTIVE_LOANS_SELECT
											+ " for contractId " + contractId
											+ " and participantId "
											+ participantId);
						} finally {
							try {
								if (activeLoansResultSet != null) {
									activeLoansResultSet.close();
								}
								if (activeLoansSelectStatement != null) {
									activeLoansSelectStatement.close();
								}
							} catch (SQLException e) {
								throw new SystemException(e, className,
										"getParticipantList",
										"Problem occurred in closing result set or statement for: "
												+ SQL_ACTIVE_LOANS_SELECT
												+ " for contractId " + contractId
												+ " and participantId "
												+ participantId);
							}
						}
					}
					// only copy the participant if it is active or has active
					// loans
					if (ACTIVE_STATUS.equals(participantStatusCode)
							|| loanCount > 0) {
						String employeeId = null;
						;
						if (EE_SORT_OPTION.equals(participantSortOption)) {
							employeeId = participantResultSet
									.getString("employeeId");
						} else {
							employeeId = participantResultSet
									.getString("socialSecurityNo");
						}
						// ignore any participant with a blank employee id (this sometimes happens for sort option 'EE'
						AddableParticipant tempParticipant = null;
						boolean alreadyOnList = false;
						if (!employeeId.trim().equals(EMPTY_STRING)) {
							// ignore any participant already on the list (a participant already on the case, can
							// also be added to the case, but we want to see only one entry here)
							int i = 0;
							while (i < participantList.size()) {
								tempParticipant = (AddableParticipant)participantList.get(i);
								if (tempParticipant.getIdentifier().equals(employeeId)) {
									alreadyOnList = true;
								}
								i++;
							}
							if (!alreadyOnList) {
								// mark any on the submission as already included on the
								// case (null) and others as false
								// any way an item from 4 will not be on original list
								// from 1???
								Boolean alreadyIncluded = null;
								if (!alreadyIncludedParticipants
										.containsKey(employeeId)) {
									alreadyIncluded = new Boolean(false);
								}
								
								fullName = participantResultSet.getString(
								"lastName").trim()
								+ ", "
								+ participantResultSet.getString("firstName")
								.trim();
								participant = new AddableParticipant(employeeId,
										fullName, alreadyIncluded);
								participantList.add(participant);
							}
						}
					}
				}
				if (participantResultSet != null) {
					participantResultSet.close();
				}
			} catch (SQLException e) {
				throw new SystemException(e, className, "getParticipantList",
						"Problem occurred in prepared call: "
								+ SQL_STATUS_SELECT + " for contractId "
								+ contractId);
			} finally {
				if (participantSelectStatement != null) {
					try {
						participantSelectStatement.close();
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"getParticipantList",
								"Problem occurred in closing prepared statement "
										+ SQL_STATUS_SELECT
										+ " for contractId " + contractId
										+ " and submissionId " + submissionId);
					}
				}
			if (activeLoansSelectStatement != null) {
					try {
						activeLoansSelectStatement.close();
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"getParticipantList",
								"Problem occurred in closing prepared statement "
										+ SQL_ACTIVE_LOANS_SELECT
										+ " for contractId " + contractId
										+ " and participantId " + participantId);
					}
				}
			}

		} catch (SQLException e) {
			throw new SystemException(e, className, "getParticipantList",
					"Problem occurred in creating csdb connection");
		} finally {
			if (csdbConnection != null) {
				try {
					csdbConnection.close();
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"getParticipantList",
							"Problem occurred in closing csdb database connection");
				}
			}
			if (apolloConnection != null) {
				try {
					apolloConnection.close();
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"getParticipantList",
							"Problem occurred in closing csdb database connection");
				}
			}
		}
		return new AddableParticipantList(participantSortOption, systemStatus, participantList, new Integer(submissionId));
	}

	public static void addParticipantsToContribution(int submissionId, int contractId, String userId,
			List participantsToAdd)	throws SystemException {

		logger.debug("entry -> addParticipantsToContribution");
		logger.debug("submissionId -> " + submissionId);
		logger.debug("contractId -> " + contractId);
		logger.debug("userId -> " + userId);

		// get submissionId
		//Changed the calling method for QC 4773, as payroll date should show the next investment date.
		Date contributionApplicableDate = determinePaymentEffectiveDate();

		// ensure case has allowable status for update
		String systemStatus = null;
		try {
			SelectSingleOrNoValueQueryHandler queryHandler =
				new SelectSingleOrNoValueQueryHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_CASE_STATUS_SELECT, String.class);
			systemStatus = (String) queryHandler.select( new Object[] {new Integer(submissionId), new Integer(contractId)});

		} catch (DAOException e) {
			throw handleDAOException(e, className, "addParticipantsToContribution",
					"Problem occurred in prepared call: " + SQL_CASE_STATUS_SELECT +
					" for contractId " + contractId + " and submissionId " + submissionId);
		}
		if (!PARTICIPANT_ADDABLE_STATES.contains(systemStatus)) {
			throw new SystemException(className + ".addParticipantsToContribution() - Not allowed to add participant(s) " +
					" to submission not in draft or copy status for contractId " + contractId +
					" and submissionId " + submissionId);
		}

		Integer minimumSourceRecordNumber = new Integer(0);
		Integer maximumSourceRecordNumber = new Integer(0);
		Integer contractIdObject = new Integer(contractId);
		try {
			SelectSingleValueQueryHandler queryHandler = new SelectSingleValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					SQL_SELECT_MINIMUM_RECORD_NUMBER, Integer.class);
			minimumSourceRecordNumber = (Integer) queryHandler
					.select(new Object[] { new Integer(submissionId), contractIdObject,
							new Integer(submissionId), contractIdObject, new Integer(submissionId), contractIdObject });
		} catch (DAOException e) {
			throw handleDAOException(e, className, "addParticipantsToContribution",
					"Problem occurred in prepared call: " + SQL_SELECT_MINIMUM_RECORD_NUMBER +
					" for contractId " + contractId + " and submissionId " + submissionId);
		}

		// we will add participants using sequential source record numbers
		// starting at the negative of one more than the highest absolute value of existing participants
		int nextSourceRecordNumber = minimumSourceRecordNumber.intValue();
		if (nextSourceRecordNumber > 0) {
			try {
				SelectSingleValueQueryHandler queryHandler = new SelectSingleValueQueryHandler(
						SUBMISSION_DATA_SOURCE_NAME,
						SQL_SELECT_MAXIMUM_RECORD_NUMBER, Integer.class);
				maximumSourceRecordNumber = (Integer) queryHandler
						.select(new Object[] { new Integer(submissionId), contractIdObject,
							new Integer(submissionId), contractIdObject, new Integer(submissionId), contractIdObject });
			} catch (DAOException e) {
				throw handleDAOException(e, className, "addParticipantsToContribution",
						"Problem occurred in prepared call: " + SQL_SELECT_MAXIMUM_RECORD_NUMBER +
						" for contractId " + contractId+ " and submissionId " + submissionId);
			}
			nextSourceRecordNumber = maximumSourceRecordNumber.intValue() * -1;
		}
		AddableParticipant participant = null;

		String activeLoansSelectString = null;
		try {
			SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_DATA, String.class);
			String participantSortOption = (String) handler.select(new Object[] { new Integer(contractId) });

			if (EE_SORT_OPTION.equals(participantSortOption)) {
				activeLoansSelectString = SQL_ACTIVE_LOANS_SELECT_BY_IDENTIFIER + EE_SELECT_CLAUSE;
			} else {
				activeLoansSelectString = SQL_ACTIVE_LOANS_SELECT_BY_IDENTIFIER + SSN_SELECT_CLAUSE;
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className, "addParticipantsToContribution",
					"Problem occurred in prepared call: "+ SQL_SELECT_CONTRACT_DATA + " with contract id " + contractId);
		}

		try {
			SQLInsertHandler insertHandler =
				new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION_PARTICIPANT);

			// insert all participants in list
			for (Iterator itr = participantsToAdd.iterator(); itr.hasNext();) {
				participant = (AddableParticipant) itr.next();
				Integer[] activeParticipantLoans = null;
				SelectMultiValueQueryHandler queryHandler = new SelectMultiValueQueryHandler(CUSTOMER_DATA_SOURCE_NAME,
						activeLoansSelectString, Integer.class);
				activeParticipantLoans =
					(Integer[]) queryHandler.select(new Object[] {new Integer(contractId), participant.getIdentifier()});

				SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
						SUBMISSION_DATA_SOURCE_NAME, SQL_CHECK_PARTICIPANT_EXISTS, Integer.class);
				Integer participantCount = (Integer) handler.select(new Object[] {new Integer(submissionId),
						new Integer(contractId), participant.getIdentifier() } );

				nextSourceRecordNumber--;
				if (participantCount.intValue() < 1) {
					// if the participant doesn't exist, add it plus zero loan repayments for any outstanding lonas
					insertHandler.insert( new Object[] {new Integer(submissionId), new Integer(contractId), participant.getIdentifier(),
							participant.getName(), EMPTY_STRING, new Integer(nextSourceRecordNumber), userId, userId});
					addLoanRepayments(new Integer(submissionId), new Integer(contractId), participant.getIdentifier(),
							new Integer(nextSourceRecordNumber), userId, contributionApplicableDate, activeParticipantLoans);
				} else {
					// otherwise, add zero loan repayments for any outstanding loans
					int loanCount = addLoanRepayments(new Integer(submissionId), new Integer(contractId),
							participant.getIdentifier(), new Integer(nextSourceRecordNumber), userId,
							contributionApplicableDate, activeParticipantLoans);
					// if no loans, add zero allocations (we need one or another in order for the "duplicate"
					// participant to show on the page
					if (loanCount == 0) {
						addDummyAllocations(submissionId, contractId, participant.getIdentifier(),
								nextSourceRecordNumber, userId, contributionApplicableDate);
					}
				}
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className, "addParticipantsToContribution",
					"Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION_PARTICIPANT +
					" for contractId " + contractId + " and submissionId " + submissionId);
		}

		logger.debug("exit <- addParticipantsToContribution");
	}

	private static int addLoanRepayments(Integer submissionId, Integer contractId, String employerDesignatedId,
			Integer recordNumber, String userId, Date contributionApplicableDate, Integer[] activeParticipantLoans)
				throws SystemException {

		int loanCount = 0;
		try {
			SQLInsertHandler insertHandler =
				new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_SUBMISSION_LOAN_REPAYMENT);
			for (int i = 0; i< activeParticipantLoans.length; i++) {
				Integer loanId = activeParticipantLoans[i];
				// figure out the highest occurrence number used by this participant/loan id combination
				// if not found, assume -1
				// the number will be incremented by one to determine the next one to insert
				Integer maxOccurrenceNo;
				Integer occurrenceCount;
				SelectSingleValueQueryHandler queryHandler;
				try {
					queryHandler = new SelectSingleValueQueryHandler(
							SUBMISSION_DATA_SOURCE_NAME, SQL_LOAN_REPAYMENT_OCCURRENCE_COUNT_SELECT,
							new int[] {}, Integer.class);
					occurrenceCount = (Integer) queryHandler.select(new Object[] {
							submissionId, contractId, employerDesignatedId, loanId});
				} catch (DAOException e) {
					throw handleDAOException(e, className, "addLoanRepayments",
							"Problem occurred in prepared call: "
							+ SQL_LOAN_REPAYMENT_OCCURRENCE_COUNT_SELECT
							+ " with submisison id " + submissionId
							+ ", contract id " + contractId
							+ ", participant " + employerDesignatedId
							+ " and loan id " + loanId);
				}
				if (occurrenceCount.intValue() == 0) {
					maxOccurrenceNo = new Integer(-1);
				} else {
					try {
						queryHandler = new SelectSingleValueQueryHandler(
								SUBMISSION_DATA_SOURCE_NAME, SQL_MAXIMUM_LOAN_REPAYMENT_RECORD_SELECT,
								new int[] {}, Integer.class);
						maxOccurrenceNo = (Integer) queryHandler.select(new Object[] {
								submissionId, contractId, employerDesignatedId, loanId });
					} catch (DAOIntegrityException e) {
						//expected first time, so set value to what we want
						maxOccurrenceNo = new Integer(-1);
					} catch (DAOException e) {
						throw handleDAOException(e, className, "addLoanRepayments",
								"Problem occurred in prepared call: "
								+ SQL_MAXIMUM_LOAN_REPAYMENT_RECORD_SELECT
								+ " with submisison id " + submissionId
								+ ", contract id " + contractId
								+ ", participant " + employerDesignatedId
								+ " and loan id " + loanId);
					}
				}

				insertHandler.insert(new Object[] { submissionId, contractId, employerDesignatedId,
                        activeParticipantLoans[i], new Integer(maxOccurrenceNo.intValue() + 1),
                        ZERO_SCALE_2,
                        getSqlDate(contributionApplicableDate),
                        EMPTY_STRING, recordNumber, userId, userId });
				loanCount++;
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className, "addLoanRepayments",
					"Problem occurred in prepared call: " + SQL_INSERT_SUBMISSION_LOAN_REPAYMENT +
					" for submisisonId " + submissionId + ", contractId " + contractId);
		}
		return loanCount;
	}
	private static void addDummyAllocations(int submissionId, int contractId,
			String employerDesignatedId, int recordNumber, String userId, Date contributionApplicableDate)
			throws SystemException {

		Connection connection = null;
		try {
			connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);
		} catch (SQLException e) {
			throw new SystemException(e, className,
					"deleteParticipantData",
					"Problem getting connection");
		}
		PreparedStatement allocationInsertStatement = null;
		PreparedStatement moneyTypeStatement = null;
		ResultSet rs = null;
		List moneyTypes = new ArrayList();
		try {
			try {
				moneyTypeStatement = connection
				.prepareStatement(SQL_ALLOCATION_MONEY_TYPE_SELECT);
				moneyTypeStatement.setInt(1, submissionId);
				moneyTypeStatement.setInt(2, contractId);

				rs = moneyTypeStatement.executeQuery();
				while (rs.next()) {
					String moneyTypeId = rs.getString("moneyTypeId").trim();
					moneyTypes.add(moneyTypeId);
				}
			} catch (SQLException e) {
				throw new SystemException(e, className, "addDummyAllocations",
						"Problem occurred in prepared call: "
						+ SQL_ALLOCATION_MONEY_TYPE_SELECT
						+ " with submissionNumber: " + submissionId
						+ " and contractId: " + contractId);

			} finally {
				try {
					if (rs != null)
						rs.close();
					if (moneyTypeStatement != null)
						moneyTypeStatement.close();
				} catch (SQLException e) {
					throw new SystemException(e, className,
							"addDummyAllocations",
					"Problem occurred closing money type result set or statement");
				}
			}

			for (Iterator moneyTypeIterator = moneyTypes.iterator(); moneyTypeIterator.hasNext(); ) {
				String moneyTypeId = (String)moneyTypeIterator.next();
				// figure out the highest occurrence number used by this participant/loan id	combination
				// if not found, assume -1
				// the number will be incremented by one to determine the next one to insert
				Integer maxOccurrenceNo;
				Integer occurrenceCount;
				SelectSingleValueQueryHandler queryHandler;
				try {
					queryHandler = new SelectSingleValueQueryHandler(
							SUBMISSION_DATA_SOURCE_NAME, SQL_ALLOCATION_OCCURRENCE_COUNT_SELECT,
							new int[] {}, Integer.class);
					occurrenceCount = (Integer) queryHandler.select(new Object[] {
							new Integer(submissionId), new Integer(contractId), employerDesignatedId, moneyTypeId});
				} catch (DAOException e) {
					throw handleDAOException(e, className, "addDummyAllocations",
							"Problem occurred in prepared call: "
							+ SQL_LOAN_REPAYMENT_OCCURRENCE_COUNT_SELECT
							+ " with submisison id " + submissionId
							+ ", contract id " + contractId
							+ ", participant " + employerDesignatedId
							+ " and money type id " + moneyTypeId);
				}
				if (occurrenceCount.intValue() == 0) {
					maxOccurrenceNo = new Integer(-1);
				} else {
					try {
						queryHandler = new SelectSingleValueQueryHandler(
								SUBMISSION_DATA_SOURCE_NAME, SQL_MAXIMUM_ALLOCATION_RECORD_SELECT,
								new int[] {}, Integer.class);
						maxOccurrenceNo = (Integer) queryHandler.select(new Object[] {
								new Integer(submissionId), new Integer(contractId), employerDesignatedId, moneyTypeId});
					} catch (DAOIntegrityException e) {
						//expected first time, so set value to what we want
						maxOccurrenceNo = new Integer(-1);
					} catch (DAOException e) {
						throw handleDAOException(e, className, "addDummyAllocations",
								"Problem occurred in prepared call: "
								+ SQL_MAXIMUM_LOAN_REPAYMENT_RECORD_SELECT
								+ " with submisison id " + submissionId
								+ ", contract id " + contractId
								+ ", participant " + employerDesignatedId
								+ " and money type id " + moneyTypeId);
					}
				}

				try {
					allocationInsertStatement = connection.prepareStatement(SQL_INSERT_SUBMISSION_ALLOCATION);
					allocationInsertStatement.setInt(1, submissionId);
					allocationInsertStatement.setInt(2, contractId);
					allocationInsertStatement.setString(3, employerDesignatedId);
					allocationInsertStatement.setString(4, moneyTypeId);
					allocationInsertStatement.setInt(5, maxOccurrenceNo.intValue() + 1);
					allocationInsertStatement.setBigDecimal(6, ZERO_SCALE_2);
					allocationInsertStatement.setDate(7, getSqlDate(contributionApplicableDate));
					allocationInsertStatement.setString(8, EMPTY_STRING);
					allocationInsertStatement.setInt(9, recordNumber);
					allocationInsertStatement.setString(10, userId);
					allocationInsertStatement.setString(11, userId);
					allocationInsertStatement.executeUpdate();

				} catch (SQLException e) {
					throw new SystemException(e, className,
							"addDummyAllocations",
							"Problem occurred in prepared call: "
							+ SQL_INSERT_SUBMISSION_ALLOCATION
							+ " for contractId " + contractId
							+ " and submissionId "
							+ submissionId
							+ " and employerDesignatedId "
							+ employerDesignatedId);
				} finally {
					try {
						if (null != allocationInsertStatement)
							allocationInsertStatement.close();
					} catch (SQLException e) {
						throw new SystemException(e, className,
								"addDummyAllocations",
								"Problem occurred in closing insert statement"
								+ " for contractId " + contractId
								+ " and submissionId "
								+ submissionId);
					}
				}

			}
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"addParticipantsToContribution",
						"Problem occurred in closing STP Connection "
								+ " for contractId " + contractId
								+ " and submissionId " + submissionId);
			}
		}

	}

	/**
	 * This will save changes to a contribution
	 *
	 * @param userId
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @param newItem
	 * @throws SystemException
	 */
	public static void saveContributionDetails(String userId, String userName,
			String userType, String userTypeID, String userTypeName, String processorUserId,
			ContributionDetailItem newItem, boolean isResubmit) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> saveContributionDetails");
		}

		if (newItem == null || newItem.getContractId() == null
				|| newItem.getSubmissionId() == null)
			return;

		Integer contractId = newItem.getContractId();

		Integer submissionId = newItem.getSubmissionId();
		if (submissionId == null) {
			// this should never happen
			return;
		}

		// get the old contribution detail item
		ContributionDetailItem oldItem = getContributionDetails(submissionId
				.intValue(), contractId.intValue());

		// we need to keep track of all of the money types needed for this
		// submission
		Map moneyTypeList = new HashMap();

		// clean-up unneeded data before we start
		deleteContributionData(contractId, submissionId);

		// first delete the particpants that have been deleted from the
		// submission
		// to do that we need to delete all the child table records first
		// find out which participants in the modified newItem have been deleted
		Map participantIdMap = new TreeMap();
		for (Iterator itr = newItem.getSubmissionParticipants().iterator(); itr.hasNext(); ) {
			SubmissionParticipant participant = (SubmissionParticipant)itr.next();
			List recordNumbers = (List) participantIdMap.get(participant.getIdentifier());
			if (recordNumbers == null) {
				recordNumbers = new ArrayList();
			}
			recordNumbers.add(new Integer(participant.getRecordNumber()));
			participantIdMap.put(participant.getIdentifier(), recordNumbers);
		}
		Iterator newParticipants = newItem.getSubmissionParticipants()
				.iterator();
		while (newParticipants.hasNext()) {
			SubmissionParticipant newParticipant = (SubmissionParticipant) newParticipants
					.next();
			// delete the deleted ones
			// we will know that the ones that have no allocations or loans
			// should be deleted
			if (newParticipant.isMarkedForDelete()) {
				deleteParticipantData(contractId, submissionId, newParticipant,
						moneyTypeList, true, participantIdMap); // true for also deleting the
												 // participant
				// save the remaining ones
				// this will replace the existing allocations and loans
				// and will add any new allocations or loans
			} else {
				saveParticipantData(submissionId, userId, newItem,
						newParticipant, moneyTypeList);
			}
		}

		// on re-submit by internal user, remove any participants with no allocation or loan repayments
		// and save any money types still referenced
		if (isResubmit) {
			cleanupContributionDetails(submissionId, contractId, userId, userType);
		} else {
			// otherwise, insert to the money type table for each distinct money type copied to
			// the allocation table
			insertMoneyTypes(submissionId, newItem.getContractId(), userId, moneyTypeList);
		}
		// next get the payment instructions
		// and save them
		savePaymentInstructions(submissionId, userId, newItem);

		// next we update the summary tables in STP and GFT to correspond to the
		// new submission
		// in GFT update GFT_Payment_Info, and Submission_Journal
		// SQL_GFT_PAYMENT_INFO_UPDATE
        java.sql.Date sqlRequestedPaymentEffectiveDate = null;
		SQLUpdateHandler handler = new SQLUpdateHandler(
				SUBMISSION_DATA_SOURCE_NAME, SQL_GFT_PAYMENT_INFO_UPDATE);
		if (newItem.getSubmissionPaymentItem()
				.getRequestedPaymentEffectiveDate() != null) {
			try {
				logger.debug("Executing update SQL statement: "
						+ SQL_GFT_PAYMENT_INFO_UPDATE);
				BigDecimal paymentTotal = newItem.getSubmissionPaymentItem()
						.getPaymentTotal();
				if (paymentTotal == null) {
					paymentTotal = BigDecimal.ZERO;
                }

                if (newItem != null
                        && newItem.getSubmissionPaymentItem() != null
                        && newItem.getSubmissionPaymentItem().getRequestedPaymentEffectiveDate() != null) {
                    sqlRequestedPaymentEffectiveDate = new java.sql.Date(newItem
                            .getSubmissionPaymentItem().getRequestedPaymentEffectiveDate()
                            .getTime());
                }
                handler.update(new Object[] { paymentTotal, sqlRequestedPaymentEffectiveDate,
                        submissionId, contractId });
			} catch (DAOException e) {
				throw handleDAOException(e, className,
						"saveContributionDetails",
						"Problem occurred prepared call: "
								+ SQL_GFT_PAYMENT_INFO_UPDATE);
			}
		}

		// START MOD
		// Update Submission_Journal

        String stmtIndicator = null;
        if (newItem.getGenerateStatementsIndicator() != null)
            stmtIndicator = newItem.getGenerateStatementsIndicator()
                    .booleanValue() ? YES : NO; // STATEMENT_REQUEST_IND

        handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_SUBMISSION_JOURNAL_UPDATE,
                new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DECIMAL, Types.VARCHAR,
                Types.VARCHAR, Types.VARCHAR, Types.INTEGER });

		if (oldItem.getSystemStatus().equals(DRAFT_STATUS)
				|| oldItem.getSystemStatus().equals(COPIED_STATUS)) {
			try {
				logger.debug("Executing update SQL statement: "
						+ SQL_SUBMISSION_JOURNAL_UPDATE);
				BigDecimal decimalUserTypeID = new BigDecimal(userTypeID);
                String submissionType = MoneySourceHelper.getSubmissionTypeCode(newItem.getMoneySourceID().trim());

                handler.update(new Object[] { userName, userId,
						userType, decimalUserTypeID, userTypeName,
                        submissionType, stmtIndicator,
						submissionId });
			} catch (DAOException e) {
				throw handleDAOException(e, className,
						"saveContributionDetails",
						"Problem occurred prepared call: "
								+ SQL_SUBMISSION_JOURNAL_UPDATE);
			}
		}
		// END MOD

		// retrieve the number of participants
		Integer participantCount;
		try {
			SelectSingleValueQueryHandler queryHandler = new SelectSingleValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, SQL_PARTICIPANT_COUNT_SELECT,
					Integer.class);
			participantCount = (Integer) queryHandler.select(new Object[] {
					submissionId, contractId });
		} catch (DAOException e) {
			throw handleDAOException(e, className, "saveContributionDetails",
					"Problem occurred in prepared call: "
							+ SQL_PARTICIPANT_COUNT_SELECT
							+ " with submission ID " + submissionId);
		}

		// in STP update the submission_contribution, submission_payment, submission_case, and
		// submission
		int[] paramTypes = { Types.CHAR, // MONEY_SOURCE_ID
				Types.DATE, // CONTRIBUTION_APPLICABLE_DATE
				Types.DECIMAL, // TOTAL_ALLOCATION_AMT
				Types.DECIMAL, // TOTAL_WITHDRAWAL_AMT
				Types.DECIMAL, // TOTAL_LOAN_REPAYMENT_AMT
				Types.INTEGER, // TOTAL_PARTICIPANT_COUNT
				Types.CHAR, // PROCESSOR_USER_ID
				Types.CHAR, // LAST_UPDATED_USER_ID
				Types.CHAR, // TRANSACTION_CODE
				Types.INTEGER, // SUBMISSION_ID
				Types.INTEGER // CONTRACT_ID
		};
		handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME,
				SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_SAVE, paramTypes);

		// use money source to look up transaction code
		String transactionCode = MoneySourceHelper.getMoneySource(newItem.getMoneySourceID()).getTransactionCode();
		int startByte = 0;
		// trim off leading zeros
		for (int i = 0; i < transactionCode.length(); i++) {
			if (transactionCode.substring(i,i+1).equals("0")) {
				startByte++;
			} else {
				i = 999;
			}
		}
		transactionCode = transactionCode.substring(startByte);

        try {
            logger.debug("Executing update SQL statement: "
                    + SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_SAVE);

            java.sql.Date sqlPayrollDate = null;
            if ( newItem != null && newItem.getPayrollDate() != null ) {
                sqlPayrollDate = new java.sql.Date(newItem.getPayrollDate().getTime());
            }

            handler.update(new Object[] {
                    newItem.getMoneySourceID(), // MONEY_SOURCE_ID
                    sqlPayrollDate, // CONTRIBUTION_APPLICABLE_DATE
                    newItem.getContributionTotal(), // TOTAL_ALLOCATION_AMT
                    newItem.getWithdrawlTotal(), // TOTAL_WITHDRAWAL_AMT
                    newItem.getLoanRepaymentTotal(), // TOTAL_LOAN_REPAYMENT_AMT
                    participantCount, // TOTAL_PARTICIPANT_COUNT
                    processorUserId, // PROCESSOR_USER_ID
                    userId, // LAST_UPDATED_USER_ID
                    transactionCode, // TRANSACTION_CODE
                    submissionId, // SUBMISSION_ID
                    contractId // CONTRACT_ID
                    });
        } catch (DAOException e) {
            throw handleDAOException(e, className, "saveContributionDetails",
                    "Problem occurred prepared call: "
                            + SQL_UPDATE_SUBMISSION_CONTRIBUTION_FOR_SAVE);
        }

        // Update submission payment table.
        int[] paramTypes2 = { Types.DECIMAL, // TOTAL_CONTRIBUTION_PAYMENT_AMT
                Types.DECIMAL, // TOTAL_BILL_PAYMENT_AMT
                Types.DECIMAL, // TOTAL_TEMP_CREDIT_PAYMENT_AMT
                Types.DATE, // REQUESTED_PAYMENT_EFFECT_DATE
                Types.INTEGER, // SUBMISSION_ID
                Types.INTEGER // CONTRACT_ID
        };
        handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME,
                SQL_UPDATE_SUBMISSION_PAYMENT_FOR_SAVE, paramTypes2);

        BigDecimal totalContributionPayment = BigDecimal.ZERO;
		BigDecimal totalBillPayment = BigDecimal.ZERO;
		BigDecimal totalCreditPayment = BigDecimal.ZERO;
		SubmissionPaymentItem paymentItem = null;
		// for draft, save the payment totals passed in the new item
		if (oldItem.isDraft() || oldItem.getSystemStatus().equals(COPIED_STATUS)) {
			paymentItem = newItem.getSubmissionPaymentItem();
			if (paymentItem == null) paymentItem = new SubmissionPaymentItem();
			totalContributionPayment = paymentItem.getTotalContributionPayment();
			if (totalContributionPayment == null) totalContributionPayment = BigDecimal.ZERO;
			totalBillPayment = paymentItem.getTotalBillPayment();
			if (totalBillPayment == null) totalBillPayment = BigDecimal.ZERO;
			totalCreditPayment = paymentItem.getTotalCreditPayment();
			if (totalCreditPayment == null) totalCreditPayment = BigDecimal.ZERO;
		} else {
			// otherwise, retrieve the payment from the old item and calculate the totals (they aren't populated
			// on retrieval from database as they aren't normally used for display)
			paymentItem = oldItem.getSubmissionPaymentItem();
			if (paymentItem != null) {
				PaymentInstruction [] instructions = paymentItem.getPaymentInstructions();

				if (instructions != null) {
					for (int i = 0; i < instructions.length; i++) {
						if(instructions[i].getPurposeCode().equalsIgnoreCase(PaymentInstruction.CONTRIBUTION))
							totalContributionPayment = totalContributionPayment.add(instructions[i].getAmount().getAmount());
						else if(instructions[i].getPurposeCode().equalsIgnoreCase(PaymentInstruction.BILL_PAYMENT))
							totalBillPayment = totalBillPayment.add(instructions[i].getAmount().getAmount());
						else if(instructions[i].getPurposeCode().equalsIgnoreCase(PaymentInstruction.TEMPORARY_CREDIT))
							totalCreditPayment = totalCreditPayment.add(instructions[i].getAmount().getAmount());
					}
				}
			}

		}
		try {
			logger.debug("Executing update SQL statement: "
					+ SQL_UPDATE_SUBMISSION_PAYMENT_FOR_SAVE);

            Date effDate = null;
            if (sqlRequestedPaymentEffectiveDate != null) {
               effDate =  new Date(sqlRequestedPaymentEffectiveDate.getTime());
            }

			handler.update(new Object[] {
					totalContributionPayment, // TOTAL_CONTRIBUTION_PAYMENT_AMT
					totalBillPayment, // TOTAL_BILL_PAYMENT_AMT
					totalCreditPayment, // TOTAL_TEMP_CREDIT_PAYMENT_AMT
                    effDate, // REQUESTED_PAYMENT_EFFECT_DATE
					submissionId, // SUBMISSION_ID
					contractId // CONTRACT_ID
					});
		} catch (DAOException e) {
			throw handleDAOException(e, className, "saveContributionDetails",
					"Problem occurred prepared call: "
							+ SQL_UPDATE_SUBMISSION_PAYMENT_FOR_SAVE);
		}

		updateSubmissionCaseProcessStatus(newItem.getSystemStatus(), userId,
				submissionId, contractId, new String[] { newItem.getType() });

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- saveContributionDetails");
		}
	}

    /**
     * This will save changes to a vesting submission and returns the final participant count.
     *
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param newItem
     * @throws SystemException
     */
    public static Integer saveVestingDetails(String userId, String userName,
            String userType, String userTypeID, String userTypeName, String processorUserId,
            VestingDetailItem newItem, boolean isResubmit) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> saveVestingDetails");
        }

        if (newItem == null || newItem.getContractId() == null || newItem.getSubmissionId() == null) {
            return null;
        }

        Integer contractId = newItem.getContractId();

        Integer submissionId = newItem.getSubmissionId();
        if (submissionId == null) {
            // this should never happen
            return null;
        }

        // we need to keep track of all of the money types needed for this submission
        Map moneyTypeList = new HashMap();

        // clean-up unneeded data before we start
        deleteVestingData(contractId, submissionId);

        // first delete the particpants that have been deleted from the submission
        Iterator newParticipants = newItem.getSubmissionParticipants()
                .iterator();
        while (newParticipants.hasNext()) {
            VestingParticipant newParticipant = (VestingParticipant) newParticipants
                    .next();
            
            if (newParticipant.isMarkedForDelete()) {
                // delete the deleted ones
                deleteParticipantData(contractId, submissionId, newParticipant,
                        moneyTypeList);

            } else {
                // save the remaining ones
                // this will update existing participant data and insert new percentages
                saveParticipantData(submissionId, userId, newItem,
                        newParticipant, moneyTypeList);
            }
        }

        // on re-submit by internal user, remove any empty percentages
        if (isResubmit) {
            cleanupVestingDetails(submissionId, contractId, userId, userType);
        }

        // retrieve the number of participants
        Integer participantCount;
        try {
            SelectSingleValueQueryHandler queryHandler = new SelectSingleValueQueryHandler(
                    SUBMISSION_DATA_SOURCE_NAME, SQL_VESTING_PARTICIPANT_COUNT_SELECT,
                    Integer.class);
            participantCount = (Integer) queryHandler.select(new Object[] {
                    submissionId, contractId });
        } catch (DAOException e) {
            throw handleDAOException(e, className, "saveVestingDetails",
                    "Problem occurred in prepared call: "
                            + SQL_VESTING_PARTICIPANT_COUNT_SELECT
                            + " with submission ID " + submissionId);
        }

        updateSubmitCount(participantCount, userId,
                submissionId, contractId, new String[] { newItem.getType() });

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- saveVestingDetails");
        }

        return participantCount;
    }

    /**
     * This will save changes to a LTPT submission and returns the final participant count.
     *
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param newItem
     * @throws SystemException
     * @throws DAOException 
     */
    public static Integer saveLongTermPartTimeDetails(String userId, String userName,
            String userType, String userTypeID, String userTypeName, String processorUserId,
            LongTermPartTimeDetailItem newItem, boolean isResubmit) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> saveLongTermPartTimeDetails");
        }

        if (newItem == null || newItem.getContractId() == null || newItem.getSubmissionId() == null) {
            return null;
        }

        Integer contractId = newItem.getContractId();

        Integer submissionId = newItem.getSubmissionId();
        if (submissionId == null) {
            // this should never happen
            return null;
        }

        // first delete the particpants that have been deleted from the submission
        Iterator newParticipants = newItem.getSubmissionParticipants()
                .iterator();
		while (newParticipants.hasNext()) {
			LongTermPartTimeParticipant newParticipant = (LongTermPartTimeParticipant) newParticipants.next();

			if (newParticipant.isMarkedForDelete()) {
				// delete the deleted ones
				deleteLTPTParticipantData(contractId, submissionId, newParticipant);
			}else {
				//Save LongTermPartTime Assessment Year
				saveLTPTParticipantData(submissionId, userId, newItem,
                        newParticipant);
			}
		}

        // retrieve the number of participants
        Integer participantCount;
        try {
            SelectSingleValueQueryHandler queryHandler = new SelectSingleValueQueryHandler(
                    SUBMISSION_DATA_SOURCE_NAME, SQL_LONG_TERM_PART_TIME_PARTICIPANT_COUNT_SELECT,
                    Integer.class);
            participantCount = (Integer) queryHandler.select(new Object[] {
                    submissionId, contractId });
        } catch (DAOException e) {
            throw handleDAOException(e, className, "saveLongTermPartTimeDetails",
                    "Problem occurred in prepared call: "
                            + SQL_LONG_TERM_PART_TIME_PARTICIPANT_COUNT_SELECT
                            + " with submission ID " + submissionId);
        }

        updateSubmitCount(participantCount, userId,
                submissionId, contractId, new String[] { newItem.getType() });

        if (logger.isDebugEnabled()) {
            logger.debug("exit <-saveLongTermPartTimeDetailss");
        }

        return participantCount;
    }
    
	public static void updateSubmissionCaseProcessStatus(String statusCode, String userId, Integer subId, Integer contractNumber, String[] caseTypeCode) throws SystemException {
		logger.debug("entry -> updateSubmissionCaseProcessStatus");

		StringBuffer sql = new StringBuffer(SQL_UPDATE_SUBMISSION_CASE_PROCESS_STATUS);
		if (caseTypeCode.length == 1) {
			sql.append(" and submission_case_type_code='").append(caseTypeCode[0]).append("'");
		} else {
			sql.append(" and submission_case_type_code in (");
			for (int i = 0; i < caseTypeCode.length; i++) {
				sql.append("'").append(caseTypeCode[i]).append("'");
				if (i != caseTypeCode.length - 1) {
					sql.append(", ");
				}
			}
			sql.append(")");
		}

		SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, sql.toString());
		try {
			logger.debug("Executing Prepared SQL Statement: " + sql.toString());
			handler.update(new Object[] { statusCode, userId, subId, contractNumber });
		} catch (DAOException e) {
			throw new SystemException(e, className, "updateSubmissionCaseProcessStatus", "Problem occurred prepared call: " + sql.toString());
		}

		logger.debug("exit <- updateSubmissionCaseProcessStatus");
	}

    /**
     * Update the submit count in the SUBMISSION_CASE table. If the count reaches 0, the
     * processing status code to cancelled.
     *
     * @param participantCount
     * @param userId
     * @param subId
     * @param contractNumber
     * @param caseTypeCode
     * @throws SystemException
     */
    public static void updateSubmitCount(Integer participantCount, String userId, Integer subId, Integer contractNumber, String[] caseTypeCode) throws SystemException {
        logger.debug("entry -> updateSubmissionCaseSubmitCount");

        StringBuffer sql = null;
        if (participantCount == 0) {
        	sql = new StringBuffer(SQL_UPDATE_SUBMISSION_CASE_SUBMIT_COUNT_ZERO);
        } else {
        	sql = new StringBuffer(SQL_UPDATE_SUBMISSION_CASE_SUBMIT_COUNT);
        }
        if (caseTypeCode.length == 1) {
            sql.append(" and submission_case_type_code='").append(caseTypeCode[0]).append("'");
        } else {
            sql.append(" and submission_case_type_code in (");
            for (int i = 0; i < caseTypeCode.length; i++) {
                sql.append("'").append(caseTypeCode[i]).append("'");
                if (i != caseTypeCode.length - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");
        }

        SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, sql.toString());
        try {
            logger.debug("Executing Prepared SQL Statement: " + sql.toString());
            handler.update(new Object[] { participantCount, userId, subId, contractNumber });
        } catch (DAOException e) {
            throw new SystemException(e, className, "updateSubmissionCaseSubmitCount", "Problem occurred prepared call: " + sql.toString());
        }

        logger.debug("exit <- updateSubmissionCaseSubmitCount");
    }

	/**
	 * Deletes participant data with optionally the participant as well.
	 *
	 * @param contractId
	 * @param submissionId
	 * @param employerDesignatedID
 	 * @param moneyTypeList
	 * @param includeParticipant
	 * @throws SystemException
	 */
	private static void deleteParticipantData(Integer contractId,
			Integer submissionId, SubmissionParticipant participant, Map moneyTypeList,
			boolean includeParticipant, Map participantIdMap) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> deleteParticipantData");
		}
		Connection connection = null;
		try {
			connection = getDefaultConnection(className,
					SUBMISSION_DATA_SOURCE_NAME);
			deleteParticipantData(connection, contractId, submissionId, participant, moneyTypeList,
					includeParticipant, participantIdMap);
		} catch (SQLException e) {
			throw new SystemException(e, className,
					"deleteParticipantData",
					"Problem getting connection");
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				throw new SystemException(e, className,
						"addParticipantsToContribution",
						"Problem occurred in closing STP Connection "
								+ " for contractId " + contractId
								+ " and submissionId " + submissionId);
			}
		}
	}

	 /**
     * Deletes LTPT participant data. 
     *
     * @param contractId
     * @param submissionId
     * @param participant
     * 
     * @throws SystemException
     */
		private static void deleteLTPTParticipantData(Integer contractId, Integer submissionId,
				LongTermPartTimeParticipant participant) throws SystemException {

			if (logger.isDebugEnabled()) {
				logger.debug("entry -> deleteLTPTParticipantData");
			}
			Connection connection = null;
			Integer sourceRecordNumber = new Integer(participant.getRecordNumber());
			try {
				connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);

				// delete from EMPLOYEE_LTPT_INFO where submission_id,
				// contract_id, source_record_no match
				SQLDeleteHandler handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
						SQL_DELETE_LONG_TERM_PART_TIME_PARTICIPANT_ITEM);
				
				handler.delete(new Object[] { submissionId, contractId, sourceRecordNumber });

			} catch (SQLException e) {
				throw new SystemException(e, className, "deleteLTPTParticipantData", "Problem getting connection");
			} catch (DAOException e) {
		        throw handleDAOException(
		                e,
		                className,
		                "deleteLTPTParticipantData",
		                "Problem occurred deleting participant LTPT records with submissionId "
		                        + submissionId);
			} finally {
				try {
					if (connection != null) {
						connection.close();
					}
				} catch (SQLException e) {
					throw new SystemException(e, className, "deleteLTPTParticipantData",
							"Problem occurred in closing STP Connection " + " for contractId " + contractId
									+ " and submissionId " + submissionId);
				}
			}
		}
	
    /**
     * Deletes participant data. 
     *
     * @param contractId
     * @param submissionId
     * @param participant
     * @param moneyTypeList
     * 
     * @throws SystemException
     */
    private static void deleteParticipantData(Integer contractId,
            Integer submissionId, VestingParticipant participant, Map moneyTypeList) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> deleteParticipantData");
        }
        Connection connection = null;
        try {
            connection = getDefaultConnection(className,
                    SUBMISSION_DATA_SOURCE_NAME);
            deleteParticipantData(connection, contractId, submissionId, participant, moneyTypeList);
        } catch (SQLException e) {
            throw new SystemException(e, className,
                    "deleteParticipantData",
                    "Problem getting connection");
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new SystemException(e, className,
                        "addParticipantsToContribution",
                        "Problem occurred in closing STP Connection "
                                + " for contractId " + contractId
                                + " and submissionId " + submissionId);
            }
        }
    }
	private static void deleteParticipantData(Connection connection, Integer contractId,
				Integer submissionId, SubmissionParticipant participant, Map moneyTypeList,
				boolean includeParticipant, Map participantIdMap) throws SystemException {

		// even though the participant is being deleted we have to preserve the money types
		SortedMap moneyTypeAmounts = participant.getMoneyTypeAmounts();
		if (moneyTypeAmounts != null && moneyTypeAmounts.size() > 0) {
			Iterator moneyKeys = moneyTypeAmounts.keySet().iterator();

			while (moneyKeys.hasNext()) {
				String moneyKey = (String) moneyKeys.next();
				String moneyKeyID = moneyKey.substring(0, moneyKey.indexOf("/"));
				// save each money type so we can insert it to the money
				// type table at the end
				moneyTypeList.put(moneyKeyID, moneyKeyID);
			}
		}

		String employerDesignatedID = participant.getIdentifier();
		Integer sourceRecordNumber = new Integer(participant.getRecordNumber());
		try {
			// delete from submission_loan_repayment where submission_id,
			// contract_id, employer_designated_id match
			SQLDeleteHandler handler = new SQLDeleteHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_SUBMISSION_LOAN_REPAYMENT_ITEM
							+ SQL_EMPLOYER_ID_WHERE + SQL_RECORD_NUMBER_WHERE);
			handler.delete(new Object[] { submissionId, contractId,
					employerDesignatedID, sourceRecordNumber});
			// delete from submission_allocation where submission_id,
			// contract_id, employer_designated_id match
			handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_SUBMISSION_ALLOCATION_ITEM
							+ SQL_EMPLOYER_ID_WHERE + SQL_RECORD_NUMBER_WHERE);
			handler.delete(new Object[] { submissionId, contractId,
					employerDesignatedID, sourceRecordNumber });
			Integer targetValue = new Integer(0);
			if (includeParticipant) {
				// check if there are any other records for this participant
				List sourceRecordList = (List)participantIdMap.get(employerDesignatedID);
				int i = 0;
				boolean misMatchNotFound = true;
				while (i < sourceRecordList.size() && misMatchNotFound) {
					if (sourceRecordNumber.intValue() != ((Integer)sourceRecordList.get(i)).intValue()) {
						targetValue = (Integer)sourceRecordList.get(i);
						misMatchNotFound = false;
					}
					i++;
				}
				// if so, update the participant's record number
				if (targetValue.intValue() != 0) {
					try {
						SQLUpdateHandler handler3 = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME,
								SQL_UPDATE_SUBMISSION_PARTICIPANT);
						handler3.update(new Object[] { targetValue, submissionId, contractId, employerDesignatedID } );
					} catch (DAOException e) {
						throw handleDAOException(e, className,
								"deleteParticipantData",
								"Problem occurred in prepared call: "
								+ SQL_UPDATE_SUBMISSION_PARTICIPANT
								+ " with submissionNumber: "
								+ submissionId + " contractNumber: "
								+ contractId
								+ " employer_designated_id: "
								+ employerDesignatedID);
					}
				} else {
					// otherwise, delete participant
					handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
							SQL_DELETE_SUBMISSION_PARTICIPANT_ITEM + SQL_EMPLOYER_ID_WHERE);
					handler.delete(new Object[] { submissionId, contractId,
							employerDesignatedID });
				}
			}

		} catch (DAOException e) {
			throw handleDAOException(
					e,
					className,
					"deleteParticipantData",
					"Problem occurred deleting participant contribution records with submission journal submissionId "
							+ submissionId);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- deleteParticipantData");
		}
	}

    private static void deleteParticipantData(Connection connection, Integer contractId,
            Integer submissionId, VestingParticipant participant, Map moneyTypeList) 
            throws SystemException {

    // even though the participant is being deleted we have to preserve the money types
    SortedMap moneyTypePerc = participant.getMoneyTypePercentages();
    if (moneyTypePerc != null && moneyTypePerc.size() > 0) {
        Iterator moneyKeys = moneyTypePerc.keySet().iterator();

        while (moneyKeys.hasNext()) {
            String moneyKey = (String) moneyKeys.next();
            String moneyKeyID = moneyKey.substring(0, moneyKey.indexOf("/"));
            // save each money type so we can insert it to the money
            // type table at the end
            moneyTypeList.put(moneyKeyID, moneyKeyID);
        }
    }

    Integer sourceRecordNumber = new Integer(participant.getRecordNumber());
    try {

        // delete from employee_vesting where submission_id,
        // contract_id, source_record_no match
        SQLDeleteHandler handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
                SQL_DELETE_VESTING_PARTICIPANT_ITEM);
        handler.delete(new Object[] { submissionId, contractId, sourceRecordNumber });
   

    } catch (DAOException e) {
        throw handleDAOException(
                e,
                className,
                "deleteParticipantData",
                "Problem occurred deleting participant vesting records with submissionId "
                        + submissionId);
    }

    if (logger.isDebugEnabled()) {
        logger.debug("exit <- deleteParticipantData");
    }
}

	/**
	 * Deletes all rows from several sumbission tables for a given submisison id
	 * and contract id .
	 *
	 * @param contractId
	 * @param submissionId
	 * @throws SystemException
	 */
	private static void deleteContributionData(Integer contractId,
			Integer submissionId) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> deleteContributionData");
		}

		try {

			// delete from submission_contribution_money_type where
			// submission_id, contract_id match
			SQLDeleteHandler deleteHandler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_SUBMISSION_CONTRIBUTION_MONEY_TYPE_ITEM);
			deleteHandler.delete(new Object[] { submissionId, contractId });
			// delete from submission_loan_repayment where submission_id,
			// contract_id, employer_designated_id match
			deleteHandler = new SQLDeleteHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_SUBMISSION_LOAN_REPAYMENT_ITEM);
			deleteHandler.delete(new Object[] { submissionId, contractId });
			// delete from submission_allocation where submission_id,
			// contract_id, employer_designated_id match
			deleteHandler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_SUBMISSION_ALLOCATION_ITEM);
			deleteHandler.delete(new Object[] { submissionId, contractId });
			// delete from stp_rollup where submission_id, contract_id match (we
			// don't expect these, but want to be safe)
			deleteHandler = new SQLDeleteHandler(
					SUBMISSION_DATA_SOURCE_NAME, SQL_DELETE_STP_ROLLUP_ITEM);
			deleteHandler.delete(new Object[] { submissionId, contractId });
		} catch (DAOException e) {
			throw handleDAOException(
					e,
					className,
					"deleteContributionData",
					"Problem occurred deleting participant contribution records with submission journal submissionId "
							+ submissionId);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- deleteContributionData");
		}
	}

    /**
     * Deletes all rows from the employee_vesting_percentage table
     * for a given submisison id and contract id .
     *
     * @param contractId
     * @param submissionId
     * @throws SystemException
     */
    private static void deleteVestingData(Integer contractId,
            Integer submissionId) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> deleteVestingData");
        }

        try {
            // delete from employee_vesting_percentage where submission_id,
            // contract_id match
            SQLDeleteHandler deleteHandler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
                    SQL_DELETE_VESTING_PERCENTAGES);
            deleteHandler.delete(new Object[] { submissionId, contractId });
        } catch (DAOException e) {
            throw handleDAOException(
                    e,
                    className,
                    "deleteVestingData",
                    "Problem occurred deleting vesting records with submission journal submissionId "
                            + submissionId);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- deleteVestingData");
        }
    }

	/**
	 * Saves the participant data changes.
	 *
	 * @param submissionId
	 * @param userId
	 * @param newItem
	 * @param newParticipant
	 * @param moneyTypeList
	 * @throws SystemException
	 */
	private static void saveParticipantData(Integer submissionId,
			String userId, ContributionDetailItem newItem,
			SubmissionParticipant newParticipant, Map moneyTypeList)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> saveParticipantData");
		}
		SQLInsertHandler handler = null;

		// grab the allocations first
		SortedMap moneyTypeAmounts = newParticipant.getMoneyTypeAmounts();
		if (moneyTypeAmounts != null && moneyTypeAmounts.size() > 0) {
			Iterator moneyKeys = moneyTypeAmounts.keySet().iterator();

			handler = new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_INSERT_SUBMISSION_ALLOCATION);

			try {
				while (moneyKeys.hasNext()) {
					String moneyKey = (String) moneyKeys.next();
					String moneyKeyID = moneyKey.substring(0, moneyKey
							.indexOf("/"));
					// this is a new money type allocation amount
					// insert the new allocations
					BigDecimal allocationAmount = (BigDecimal) moneyTypeAmounts
							.get(moneyKey);

					// figure out the highest occurrence number used by this participant/money type	combination
					// if not found, assume -1
					// the number will be incremented by one to determine the next one to insert
					Integer maxOccurrenceNo;
					Integer occurrenceCount;
					SelectSingleValueQueryHandler queryHandler;
					try {
						queryHandler = new SelectSingleValueQueryHandler(
								SUBMISSION_DATA_SOURCE_NAME, SQL_ALLOCATION_OCCURRENCE_COUNT_SELECT,
								new int[] {}, Integer.class);
						occurrenceCount = (Integer) queryHandler.select(new Object[] {
								submissionId, newItem.getContractId(), newParticipant.getIdentifier(), moneyKeyID });
					} catch (DAOException e) {
						throw handleDAOException(e, className, "saveParticipantDetails",
								"Problem occurred in prepared call: "
								+ SQL_ALLOCATION_OCCURRENCE_COUNT_SELECT
								+ " with submisison id " + submissionId
								+ ", contract id " + newItem.getContractId()
								+ ", participant " + newParticipant.getIdentifier()
								+ " and money type id " + moneyKeyID);
					}
					if (occurrenceCount.intValue() == 0) {
						maxOccurrenceNo = new Integer(-1);
					} else {
						try {
							queryHandler = new SelectSingleValueQueryHandler(
									SUBMISSION_DATA_SOURCE_NAME, SQL_MAXIMUM_ALLOCATION_RECORD_SELECT,
									new int[] {}, Integer.class);
							maxOccurrenceNo = (Integer) queryHandler.select(new Object[] {
									submissionId, newItem.getContractId(), newParticipant.getIdentifier(), moneyKeyID });
						} catch (DAOIntegrityException e) {
							//expected first time, so set value to what we want
							maxOccurrenceNo = new Integer(-1);
						} catch (DAOException e) {
							throw handleDAOException(e, className, "saveParticipantDetails",
									"Problem occurred in prepared call: "
									+ SQL_MAXIMUM_ALLOCATION_RECORD_SELECT
									+ " with submisison id " + submissionId
									+ ", contract id " + newItem.getContractId()
									+ ", participant " + newParticipant.getIdentifier()
									+ " and money type id " + moneyKeyID);
						}
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Executing insert SQL statement: "
								+ SQL_INSERT_SUBMISSION_ALLOCATION);
					}
                    java.sql.Date sqlPayrollDate = null;
                    if ( newItem != null && newItem.getPayrollDate() != null ) {
                        sqlPayrollDate = new java.sql.Date(newItem.getPayrollDate().getTime());
                    }
					handler.insert(new Object[] { submissionId,
							newItem.getContractId(),
							newParticipant.getIdentifier(), moneyKeyID,
							new Integer(maxOccurrenceNo.intValue() + 1), allocationAmount,
                            sqlPayrollDate, // contribution applicable
											// date - payroll date
							EMPTY_STRING, new Integer(newParticipant.getRecordNumber()),
							userId, // create id
							userId // update id
							});

					// save each money type so we can insert it to the money
					// type table at the end
					moneyTypeList.put(moneyKeyID, moneyKeyID);
				}
			} catch (DAOException e) {
				throw handleDAOException(e, className, "saveParticipantData",
						"Problem occurred in prepared call: "
								+ SQL_INSERT_SUBMISSION_ALLOCATION
								+ " for contractId "
								+ newItem.getContractId().toString()
								+ " and submissionId "
								+ newItem.getSubmissionId());
			}
		}
		// grab the loans next
		SortedMap loanAmounts = newParticipant.getLoanAmounts();
		if (loanAmounts != null && loanAmounts.size() > 0) {
			// find out which of the allocations and loans are new
			Iterator loanKeys = loanAmounts.keySet().iterator();
			// all of the old loan amounts have already been deleted above

			handler = new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_INSERT_SUBMISSION_LOAN_REPAYMENT);
			try {
				while (loanKeys.hasNext()) {
					String loanKey = (String) loanKeys.next();
					String loanKeyID = loanKey.substring(0, loanKey
							.indexOf("/"));
					if (loanKeyID == null || loanKeyID.equals(EMPTY_STRING)
							|| loanKeyID.equals(" ")) {
						loanKeyID = "99";
					}

					// insert the new loans
					// where do I get new loan ids from?
					BigDecimal loanAmount = (BigDecimal) loanAmounts
							.get(loanKey);

					// figure out the highest occurrence number used by this participant/loan id	combination
					// if not found, assume -1
					// the number will be incremented by one to determine the next one to insert
					Integer maxOccurrenceNo;
					Integer occurrenceCount;
					SelectSingleValueQueryHandler queryHandler;
					try {
						queryHandler = new SelectSingleValueQueryHandler(
								SUBMISSION_DATA_SOURCE_NAME, SQL_LOAN_REPAYMENT_OCCURRENCE_COUNT_SELECT,
								new int[] {}, Integer.class);
						occurrenceCount = (Integer) queryHandler.select(new Object[] {
								submissionId, newItem.getContractId(), newParticipant.getIdentifier(), loanKeyID });
					} catch (DAOException e) {
						throw handleDAOException(e, className, "saveParticipantDetails",
								"Problem occurred in prepared call: "
								+ SQL_LOAN_REPAYMENT_OCCURRENCE_COUNT_SELECT
								+ " with submisison id " + submissionId
								+ ", contract id " + newItem.getContractId()
								+ ", participant " + newParticipant.getIdentifier()
								+ " and money type id " + loanKeyID);
					}
					if (occurrenceCount.intValue() == 0) {
						maxOccurrenceNo = new Integer(-1);
					} else {
						try {
							queryHandler = new SelectSingleValueQueryHandler(
									SUBMISSION_DATA_SOURCE_NAME, SQL_MAXIMUM_LOAN_REPAYMENT_RECORD_SELECT,
									new int[] {}, Integer.class);
							maxOccurrenceNo = (Integer) queryHandler.select(new Object[] {
									submissionId, newItem.getContractId(), newParticipant.getIdentifier(), loanKeyID });
						} catch (DAOIntegrityException e) {
							//expected first time, so set value to what we want
							maxOccurrenceNo = new Integer(-1);
						} catch (DAOException e) {
							throw handleDAOException(e, className, "saveParticipantDetails",
									"Problem occurred in prepared call: "
									+ SQL_MAXIMUM_LOAN_REPAYMENT_RECORD_SELECT
									+ " with submisison id " + submissionId
									+ ", contract id " + newItem.getContractId()
									+ ", participant " + newParticipant.getIdentifier()
									+ " and money type id " + loanKeyID);
						}
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Executing insert SQL statement: "
								+ SQL_INSERT_SUBMISSION_LOAN_REPAYMENT);
					}
                    java.sql.Date sqlPayrollDate = null;
                    if ( newItem != null && newItem.getPayrollDate() != null ) {
                        sqlPayrollDate = new java.sql.Date(newItem.getPayrollDate().getTime());
                    }
					handler.insert(new Object[] { submissionId,
							newItem.getContractId(),
							newParticipant.getIdentifier(),
							Integer.valueOf(loanKeyID.trim()),
							new Integer(maxOccurrenceNo.intValue() + 1), loanAmount,
                            sqlPayrollDate, // loan repayment date -
											// payroll date
							EMPTY_STRING, new Integer(newParticipant.getRecordNumber()),
							userId, // create id
							userId // update id
							});
				}
			} catch (DAOException e) {
				throw handleDAOException(e, className, "saveParticipantData",
						"Problem occurred in prepared call: "
								+ SQL_INSERT_SUBMISSION_LOAN_REPAYMENT
								+ " for contractId "
								+ newItem.getContractId().toString()
								+ " and submissionId "
								+ newItem.getSubmissionId());
			}
		}
		// done updating participant

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- saveParticipantData");
		}
	}

    /**
     * Saves the participant data changes for vesting.
     *
     * @param submissionId
     * @param userId
     * @param newItem
     * @param newParticipant
     * @param moneyTypeList
     * @throws SystemException
     */
    private static void saveParticipantData(Integer submissionId,
            String userId, VestingDetailItem newItem,
            VestingParticipant newParticipant, Map moneyTypeList)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> saveParticipantData");
        }
        

        // insert the percentages first
        SortedMap moneyTypePerc = newParticipant.getMoneyTypePercentages();
        SortedMap moneyTypeErr = newParticipant.getMoneyTypeErrors();
        if (moneyTypePerc != null && moneyTypePerc.size() > 0) {
            Integer sequenceNumber = 0;
            
            // find max sequence number for given contract, submission and record number
            try {
                SelectSingleValueQueryHandler queryHandler = new SelectSingleValueQueryHandler(
                        SUBMISSION_DATA_SOURCE_NAME, SQL_VESTING_SEQUENCE_SELECT,
                        new int[] {}, Integer.class);
                sequenceNumber = (Integer) queryHandler.select(new Object[] {
                        submissionId, newItem.getContractId(), newParticipant.getRecordNumber()});
            } catch (DAOException e) {
                throw handleDAOException(e, className, "saveParticipantDetails",
                        "Problem occurred in prepared call: "
                        + SQL_VESTING_SEQUENCE_SELECT
                        + " with submisison id " + submissionId
                        + ", contract id " + newItem.getContractId()
                        + ", record number " + newParticipant.getRecordNumber());

            }
            
            SQLInsertHandler insertHandler = new SQLInsertHandler(SUBMISSION_DATA_SOURCE_NAME,
                    SQL_INSERT_VESTING_PERCENTAGES);

          
            Iterator moneyKeys = moneyTypePerc.keySet().iterator();


            while (moneyKeys.hasNext()) {
                String moneyKey = (String) moneyKeys.next();
                String moneyKeyID = moneyKey.substring(0, moneyKey
                        .indexOf("/"));

                String percentage = (String) moneyTypePerc.get(moneyKey);
                String error = (String) moneyTypeErr.get(moneyKey);

                // insert the new percentage
                try {
                    sequenceNumber = new Integer(sequenceNumber.intValue() + 1);
                    insertHandler.insert(new Object[] { submissionId,
                            newItem.getContractId(), newParticipant.getRecordNumber(),
                            sequenceNumber, moneyKeyID,
                            percentage, error,
                            userId, new Date(), userId, new Date()});
                } catch (DAOException e) {
                    throw handleDAOException(e, className, "saveParticipantData",
                            "Problem occurred in prepared call: "
                                    + SQL_INSERT_VESTING_PERCENTAGES
                                    + " with submisison id " + submissionId
                                    + ", contract id " + newItem.getContractId()
                                    + ", record number " + newParticipant.getRecordNumber()
                                    + " and money type id " + moneyKeyID);
                }
                
                // save each money type
                moneyTypeList.put(moneyKeyID, moneyKeyID);

            }
        }
        
        // update vesting effective date, VYOS, VYOS date and error condition string
        // in the employee_vesting table
        try {
            SQLUpdateHandler participantUpdateHandler = new SQLUpdateHandler(
					SUBMISSION_DATA_SOURCE_NAME,
                    SQL_UPDATE_VESTING_PARTICIPANT,
					new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
							Types.VARCHAR,Types.VARCHAR,Types.VARCHAR, Types.TIMESTAMP,
							Types.INTEGER, Types.INTEGER, Types.INTEGER });
            participantUpdateHandler.update(new Object[] { newParticipant.getPercDate(), 
                    newParticipant.getVyos(), newParticipant.getVyosDate(),
                    newParticipant.getErrorCondString(),newParticipant.getApplyLTPTCrediting(), userId, new Timestamp(new Date().getTime()), 
                    submissionId, newItem.getContractId(), newParticipant.getRecordNumber()});
        } catch (DAOException e) {
            throw handleDAOException(e, className, "saveParticipantData",
                    "Problem occurred in prepared call: "
                            + SQL_UPDATE_VESTING_PARTICIPANT
                            + " with submisison id " + submissionId
                            + ", contract id " + newItem.getContractId()
                            + ", record number " + newParticipant.getRecordNumber());

        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- saveParticipantData");
        }
    }

    /**
     * Saves the LongTermPartTime participant data changes for LTPT.
     *
     * @param submissionId
     * @param userId
     * @param newItem
     * @param newParticipant
     * @throws SystemException
     */
    private static void saveLTPTParticipantData(Integer submissionId,
            String userId, LongTermPartTimeDetailItem newItem,
            LongTermPartTimeParticipant newParticipant)
            throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> saveParticipantData");
        }
        
        // update LongTermPartTime LTPT Assessment Year and error condition string
        // in the EMPLOYEE_LTPT_INFO table
        try {
            SQLUpdateHandler participantUpdateHandler = new SQLUpdateHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					SQL_UPDATE_LONG_TERM_PART_TIME_PARTICIPANT,
					new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
							    Types.TIMESTAMP, Types.INTEGER, Types.INTEGER, Types.INTEGER });
            participantUpdateHandler.update(new Object[] { newParticipant.getLongTermPartTimeAssessmentYear(), 
                    newParticipant.getErrorCondString(), userId, new Timestamp(new Date().getTime()), 
                    submissionId, newItem.getContractId(), newParticipant.getRecordNumber()});
        } catch (DAOException e) {
            throw handleDAOException(e, className, "saveLTPTParticipantData",
                    "Problem occurred in prepared call: "
                            + SQL_UPDATE_VESTING_PARTICIPANT
                            + " with submisison id " + submissionId
                            + ", contract id " + newItem.getContractId()
                            + ", record number " + newParticipant.getRecordNumber());

        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- saveLTPTParticipantData");
        }
    }
    
	/**
	 * Saves changes to the payment instructions.
	 *
	 * @param submissionId
	 * @param userId
	 * @param newItem
	 * @throws SystemException
	 */
	private static void savePaymentInstructions(Integer submissionId,
			String userId, ContributionDetailItem newItem)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> savePaymentInstructions");
		}

		if (submissionId == null
				|| newItem == null)
			return;

		Integer contractId = newItem.getContractId();

		// update the iFile GFT database as well for the payment instructions
		if (newItem.isDraft() || newItem.getSystemStatus().equals(COPIED_STATUS)) {
			deletePaymentData(contractId, submissionId, newItem);
		} else {
			return; // we don't update the payment in any other status
		}

		// if there are no payment instructions, there's nothing to do  (except delete any existing ones above)
		if (newItem.getSubmissionPaymentItem().getPaymentInstructions() == null)
			return;

		PaymentInstruction[] paymentInstructions = newItem
					.getSubmissionPaymentItem().getPaymentInstructions();

		// grab the instructions
		// insert the payment instructions in STP and GFT
		try {
			SQLInsertHandler gftHandler = new SQLInsertHandler(
					SUBMISSION_DATA_SOURCE_NAME, SQL_GFT_PAYMENT_DETAILS_INSERT);
			SQLInsertHandler stpHandler = new SQLInsertHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					SQL_INSERT_SUBMISSION_PAYMENT_INSTRUCTION);
			for (int i = 0; i < paymentInstructions.length; i++) {
				PaymentInstruction instruction = paymentInstructions[i];
				// update the existing payment instuctions based on
				// submission_id, contract_id and payment_occurrence_no in STP
				// and GFT
				// where do we get new sequence_ids from for GFT?
				Integer instructionNumber = null;
				String accountNumber = null;
				String accountName = null;
				String accountTypeCode = null;
				Integer transitNumber = null;
				String bankName = null;
				String paymentMethodCode = null;
				if (instruction.getPaymentAccount() instanceof DirectDebitAccount) {
					DirectDebitAccount debitAccount = (DirectDebitAccount) instruction
							.getPaymentAccount();
					instructionNumber = Integer.valueOf(debitAccount
							.getInstructionNumber());
					accountNumber = debitAccount.getBankAccountNumber();
					accountName = debitAccount.getBankAccountDescription();
					accountTypeCode = debitAccount.getAccountType();
					transitNumber = Integer.valueOf(debitAccount
							.getBankTransitNumber());
					bankName = debitAccount.getBankName();
					paymentMethodCode = SubmissionConstants.PAYMENT_METHOD_CODE_DIRECT_DEBIT;
				} else {
					CashAccount cashAccount = (CashAccount) instruction
							.getPaymentAccount();
					instructionNumber = new Integer(0);
					accountNumber = EMPTY_STRING;
					accountName = cashAccount.getCashAccountDescription();
					accountTypeCode = cashAccount.getAccountType();
					transitNumber = new Integer(0);
					bankName = EMPTY_STRING;
					paymentMethodCode = SubmissionConstants.PAYMENT_METHOD_CODE_CASH_ACCOUNT;
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Executing insert SQL statement: "
							+ SQL_GFT_PAYMENT_DETAILS_INSERT);
				}
				gftHandler.insert(new Object[] { submissionId, contractId,
						new Integer(i), instructionNumber, accountNumber,
						accountName, accountTypeCode,
						instruction.getAmount().getAmount(), transitNumber,
						bankName, instruction.getPurposeCode() });
				if (logger.isDebugEnabled()) {
					logger.debug("Executing insert SQL statement: "
							+ SQL_INSERT_SUBMISSION_PAYMENT_INSTRUCTION);
				}
				stpHandler.insert(new Object[] { submissionId, contractId,
						new Integer(i), paymentMethodCode, instructionNumber,
						transitNumber, accountNumber,
						instruction.getAmount().getAmount(),
						instruction.getPurposeCode(), EMPTY_STRING, new Integer(i),
						userId, userId });
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className, "savePaymentInstructions",
					"Problem occurred in prepared call: "
							+ SQL_INSERT_SUBMISSION_LOAN_REPAYMENT
							+ " for contractId " + contractId
							+ " and submission ID " + submissionId);
		}
		// done inserting paymentInstructions
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- savePaymentInstructions");
		}
	}

	/**
	 * Deletes payment data as part of changes to a contribution.
	 *
	 * @param contractId
	 * @param submissionId
	 * @throws SystemException
	 */
	private static void deletePaymentData(Integer contractId,
			Integer submissionId, ContributionDetailItem newItem)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> deletePaymentData");
		}

		try {
			// delete the STP records
			SQLDeleteHandler handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_SUBMISSION_PAYMENT_INSTRUCTION_ITEM);
			handler.delete(new Object[] { submissionId, contractId });

			// delete the SJ records
			handler = new SQLDeleteHandler(
					SUBMISSION_DATA_SOURCE_NAME,
					SQL_DELETE_GFT_PAYMENT_DETAILS_ITEM);
			handler.delete(new Object[] { submissionId });
			handler = new SQLDeleteHandler(SUBMISSION_DATA_SOURCE_NAME,
					SQL_GFT_PAYMENT_INFO_UPDATE);
            Date DUMMY_DATE = DateUtils.parseDate("0001-01-01", new String[] { "yyyy-MM-dd" });
			handler.delete(new Object[] { BigDecimal.ZERO, getSqlDate(DUMMY_DATE), submissionId, contractId });

		} catch (DAOException e) {
			throw handleDAOException(
					e,
					className,
					"deletePaymentData",
					"Problem occurred deleting payment details with submission journal submissionId "
							+ submissionId
							+ " and contractID = "
							+ contractId);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Cannot parse date [0001-01-01]. Somthing is seriously broken");
        }

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- deletePaymentData");
		}
	}

	/**
	 * @param submissionId
	 * @param contractid
	 * @param userId
	 * @throws SystemException
	 */
	public static void cleanupContributionDetails(Integer submissionId,
			Integer contractId, String userId, String userType) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> cleanupContributionDetails");
		}

		if (submissionId == null) {
			// this should never happen
			return;
		}

		// get the old contribution detail item
		ContributionDetailItem oldItem = getContributionDetails(submissionId
				.intValue(), contractId.intValue());

        Map moneyTypeList = new HashMap();
		Connection connection = null;

        try {
            try {
    			connection = getDefaultConnection(className,
    					SUBMISSION_DATA_SOURCE_NAME);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred getting a connection");
    		}
    		try {

    			// delete from submission_contribution_money_type where
    			// submission_id, contract_id match
    			SQLDeleteHandler deleteHandler = new SQLDeleteHandler(
    					SUBMISSION_DATA_SOURCE_NAME,
    					SQL_DELETE_SUBMISSION_CONTRIBUTION_MONEY_TYPE_ITEM);
    			deleteHandler.delete(new Object[] { submissionId, contractId });
    		} catch (DAOException e) {
    			throw handleDAOException(
    					e,
    					className,
    					"cleanupContributionDetails",
    					"Problem occurred deleting participant contribution records with submission journal submissionId "
    							+ submissionId);
    		}

    		PreparedStatement participantSelectStatement = null;
    		try {
    			participantSelectStatement = connection
    					.prepareStatement(SQL_PARTICIPANT_SELECT);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred prepared call: " + SQL_PARTICIPANT_SELECT
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}

    		PreparedStatement participantDeleteStatement = null;
    		try {
    			participantDeleteStatement = connection
    					.prepareStatement(SQL_DELETE_SUBMISSION_PARTICIPANT_ITEM
    							+ SQL_EMPLOYER_ID_WHERE);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_DELETE_SUBMISSION_PARTICIPANT_ITEM
    							+ SQL_EMPLOYER_ID_WHERE
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}

    		PreparedStatement allocationSelectStatement = null;
    		try {
    			allocationSelectStatement = connection
    					.prepareStatement(SQL_ALLOCATION_SELECT);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_ALLOCATION_SELECT
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}

    		PreparedStatement allocationDeleteStatement = null;
    		try {
    			allocationDeleteStatement = connection
    					.prepareStatement(SQL_DELETE_SUBMISSION_ALLOCATION_ITEM
    							+ SQL_EMPLOYER_ID_WHERE + SQL_MONEY_TYPE_WHERE);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_DELETE_SUBMISSION_ALLOCATION_ITEM
    							+ SQL_EMPLOYER_ID_WHERE + SQL_MONEY_TYPE_WHERE
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}
    		PreparedStatement loanRepaymentSelectStatement = null;
    		try {
    			loanRepaymentSelectStatement = connection
    					.prepareStatement(SQL_LOAN_REPAYMENT_AMOUNTS);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_LOAN_REPAYMENT_AMOUNTS
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}
    		PreparedStatement loanRepaymentDeleteStatement = null;
    		try {
    			loanRepaymentDeleteStatement = connection
    					.prepareStatement(SQL_DELETE_SUBMISSION_LOAN_REPAYMENT_ITEM
    							+ SQL_EMPLOYER_ID_WHERE + SQL_LOAN_ID_WHERE);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_DELETE_SUBMISSION_LOAN_REPAYMENT_ITEM
    							+ SQL_EMPLOYER_ID_WHERE + SQL_LOAN_ID_WHERE
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}
    		PreparedStatement rollupDeleteStatement = null;
    		try {
    			rollupDeleteStatement = connection
    					.prepareStatement(SQL_DELETE_STP_ROLLUP_ITEM
    							+ SQL_EMPLOYER_ID_WHERE);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_DELETE_STP_ROLLUP_ITEM
    							+ SQL_EMPLOYER_ID_WHERE
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}
    		PreparedStatement participantUpdateStatement = null;
    		try {
    			participantUpdateStatement = connection
    					.prepareStatement(SQL_UPDATE_SUBMISSION_PARTICIPANT);
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_UPDATE_SUBMISSION_PARTICIPANT
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		}
    		// we need to keep track of all of the money types actually used for
    		// this submission
    		int selectParticipantCount = 0;
    		int deleteParticipantCount = 0;
    		int selectAllocationCount = 0;
    		int deleteAllocationCount = 0;
    		int selectLoanRepaymentCount = 0;
    		int deleteLoanRepaymentCount = 0;
    		try {
    			participantSelectStatement.setInt(1, submissionId.intValue());
    			participantSelectStatement.setInt(2, contractId.intValue());

    			ResultSet rs = participantSelectStatement.executeQuery();

    			while (rs.next()) {
    				selectParticipantCount++;
    				String employerDesignatedId = rs
    						.getString("employerDesignatedId");
    				int minimumSourceRecordNo = 0;
    				selectAllocationCount = 0;
    				deleteAllocationCount = 0;
    				selectLoanRepaymentCount = 0;
    				deleteLoanRepaymentCount = 0;

    				try {
    					loanRepaymentSelectStatement.setInt(1, submissionId
    							.intValue());
    					loanRepaymentSelectStatement.setInt(2, contractId
    							.intValue());
    					loanRepaymentSelectStatement.setString(3,
    							employerDesignatedId);

    					ResultSet rs3 = loanRepaymentSelectStatement.executeQuery();

    					while (rs3.next()) {
    						selectLoanRepaymentCount++;
    						if (rs3.getBigDecimal("loanRepaymentAmount").compareTo(
    								BigDecimal.ZERO) == 0) {
    							deleteLoanRepaymentCount++;
    							try {
    								loanRepaymentDeleteStatement.setInt(1,
    										submissionId.intValue());
    								loanRepaymentDeleteStatement.setInt(2,
    										contractId.intValue());
    								loanRepaymentDeleteStatement.setString(3,
    										employerDesignatedId);
    								loanRepaymentDeleteStatement.setInt(4, rs3
    										.getInt("loanId"));
    								loanRepaymentDeleteStatement.setInt(5, rs3
    										.getInt("occurrenceNo"));
    								loanRepaymentDeleteStatement.executeUpdate();
    							} catch (SQLException e) {
    								throw new SystemException(
    										e,
    										className,
    										"cleanupContributionDetails",
    										"Problem occurred in prepared call: "
    												+ SQL_DELETE_SUBMISSION_LOAN_REPAYMENT_ITEM
    												+ SQL_EMPLOYER_ID_WHERE
    												+ SQL_LOAN_ID_WHERE
    												+ " with submissionNumber: "
    												+ submissionId
    												+ " contractNumber: "
    												+ contractId
    												+ " employerDesignatedID: "
    												+ employerDesignatedId
    												+ " and loanId: "
    												+ rs3.getInt("loanId"));
    							}
    						} else {
    							// and save source record number in case we have to renumber the participant
    							if (minimumSourceRecordNo != 0  && rs3.getInt("sourceRecordNo") < minimumSourceRecordNo) {
    								minimumSourceRecordNo = rs3.getInt("sourceRecordNo");
    							}
    						}
    					}
    					rs3.close();
    				} catch (SQLException e) {
    					throw new SystemException(e, className,
    							"cleanupContributionDetails",
    							"Problem occurred prepared call: "
    									+ SQL_LOAN_REPAYMENT_AMOUNTS
    									+ " with submissionNumber: " + submissionId
    									+ " contractNumber: " + contractId);
    				}

    				try {
    					allocationSelectStatement
    							.setInt(1, submissionId.intValue());
    					allocationSelectStatement.setInt(2, contractId.intValue());
    					allocationSelectStatement
    							.setString(3, employerDesignatedId);

    					ResultSet rs2 = allocationSelectStatement.executeQuery();
    					while (rs2.next()) {
    						selectAllocationCount++;
    						String moneyTypeId = rs2.getString("moneyTypeId");
    						// if the allocation amount is zero, delete the row
    						if (rs2.getBigDecimal("allocationAmount").compareTo(
    								BigDecimal.ZERO) == 0) {
    							deleteAllocationCount++;
    							try {
    								allocationDeleteStatement.setInt(1,
    										submissionId.intValue());
    								allocationDeleteStatement.setInt(2, contractId
    										.intValue());
    								allocationDeleteStatement.setString(3,
    										employerDesignatedId);
    								allocationDeleteStatement.setString(4,
    										moneyTypeId);
    								allocationDeleteStatement.setInt(5, rs2
    										.getInt("occurrenceNo"));
    								allocationDeleteStatement.executeUpdate();
    							} catch (SQLException e) {
    								throw new SystemException(
    										e,
    										className,
    										"cleanupContributionDetails",
    										"Problem occurred in prepared call: "
    												+ SQL_DELETE_SUBMISSION_ALLOCATION_ITEM
    												+ SQL_EMPLOYER_ID_WHERE
    												+ SQL_MONEY_TYPE_WHERE
    												+ " with submissionNumber: "
    												+ submissionId
    												+ " contractNumber: "
    												+ contractId
    												+ " employerDesignatedID: "
    												+ employerDesignatedId
    												+ " and moneyTypeId: "
    												+ rs2.getString("moneyTypeId"));
    							}
    						} else {
    							// otherwise add it to the list of used money types
    							moneyTypeList.put(moneyTypeId, moneyTypeId);
    							// and save source record number in case we have to renumber the participant
    							if (minimumSourceRecordNo != 0  && rs2.getInt("sourceRecordNo") < minimumSourceRecordNo) {
    								minimumSourceRecordNo = rs2.getInt("sourceRecordNo");
    							}
    						}
    					}
    					rs2.close();

    				} catch (SQLException e) {
    					throw new SystemException(e, className,
    							"cleanupContributionDetails",
    							"Problem occurred prepared call: "
    									+ SQL_ALLOCATION_SELECT
    									+ " with submissionNumber: " + submissionId
    									+ " contractNumber: " + contractId);
    				}

    				// if either all allocations and all loan repayments deleted, or
    				// no allocations of loan repayments for this participant
    				// delete the participant row and the rollup row for the
    				// participant
    				if (selectAllocationCount == deleteAllocationCount
    						&& selectLoanRepaymentCount == deleteLoanRepaymentCount) {
    					deleteParticipantCount++;

    					// we don't expect any of these, but it doesn't hurt
    					try {
    						rollupDeleteStatement
    								.setInt(1, submissionId.intValue());
    						rollupDeleteStatement.setInt(2, contractId.intValue());
    						rollupDeleteStatement
    								.setString(3, employerDesignatedId);
    						rollupDeleteStatement.executeUpdate();
    					} catch (SQLException e) {
    						throw new SystemException(e, className,
    								"cleanupContributionDetails",
    								"Problem occurred in prepared call: "
    										+ SQL_DELETE_STP_ROLLUP_ITEM
    										+ SQL_EMPLOYER_ID_WHERE
    										+ " with submissionNumber: "
    										+ submissionId + " contractNumber: "
    										+ contractId
    										+ " employer_designated_id: "
    										+ employerDesignatedId);
    					}
    					try {
    						participantDeleteStatement.setInt(1, submissionId
    								.intValue());
    						participantDeleteStatement.setInt(2, contractId
    								.intValue());
    						participantDeleteStatement.setString(3,
    								employerDesignatedId);
    						participantDeleteStatement.executeUpdate();
    					} catch (SQLException e) {
    						throw new SystemException(
    								e,
    								className,
    								"cleanupContributionDetails",
    								"Problem occurred in prepared call: "
    										+ SQL_DELETE_SUBMISSION_PARTICIPANT_ITEM
    										+ SQL_EMPLOYER_ID_WHERE
    										+ " with submissionNumber: "
    										+ submissionId + " contractNumber: "
    										+ contractId
    										+ " and employerDesignatedId: "
    										+ employerDesignatedId);
    					}

    				} else {
    					// if all underlying rows for the current source number have been deleted, change the
    					// source number to that of the minimum allocation or loan repayment (if there is one)
    					if (minimumSourceRecordNo != 0  && minimumSourceRecordNo != rs.getInt("sourceRecordNo")) {
    						try {
    							participantUpdateStatement.setInt(1, minimumSourceRecordNo);
    							participantUpdateStatement.setInt(2, submissionId.intValue());
    							participantUpdateStatement.setInt(3, contractId.intValue());
    							participantUpdateStatement.setString(4, employerDesignatedId);
    							participantUpdateStatement.executeUpdate();
    						} catch (SQLException e) {
    							throw new SystemException(e, className,
    									"cleanupContributionDetails",
    									"Problem occurred in prepared call: "
    											+ SQL_UPDATE_SUBMISSION_PARTICIPANT
    											+ " with submissionNumber: "
    											+ submissionId + " contractNumber: "
    											+ contractId
    											+ " employer_designated_id: "
    											+ employerDesignatedId);
    						}
    					}
    				}
    			}
    		} catch (SQLException e) {

    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred prepared call: " + SQL_PARTICIPANT_SELECT
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		} finally {
    			close(participantSelectStatement, null);
    			close(allocationSelectStatement, null);
    			close(loanRepaymentSelectStatement, null);
    			close(participantDeleteStatement, null);
    			close(allocationDeleteStatement, null);
    			close(loanRepaymentDeleteStatement, null);
    			close(rollupDeleteStatement, null);
    			close(participantUpdateStatement, null);
    		}

    		// update participant count
    		PreparedStatement resetParticipantCountStatement = null;
    		try {
    			resetParticipantCountStatement = connection
    					.prepareStatement(SQL_RESET_PARTICIPANT_COUNT);
    			resetParticipantCountStatement.setInt(1, selectParticipantCount
    					- deleteParticipantCount);
    			resetParticipantCountStatement.setInt(2, submissionId.intValue());
    			resetParticipantCountStatement.setInt(3, contractId.intValue());
    			resetParticipantCountStatement.executeUpdate();
    		} catch (SQLException e) {
    			throw new SystemException(e, className,
    					"cleanupContributionDetails",
    					"Problem occurred in prepare statement for: "
    							+ SQL_RESET_PARTICIPANT_COUNT
    							+ " with submissionNumber: " + submissionId
    							+ " contractNumber: " + contractId);
    		} finally {
    		    close(resetParticipantCountStatement, null);
            }

    		PreparedStatement updateSubmissionTimestampStatement = null;
    		if (oldItem.isDraft()) {
    			// set submission timestamp for orignal submission only
    			try {
    				updateSubmissionTimestampStatement = connection.prepareStatement(SQL_SET_SUBMISSION_TIMESTAMP);
    				updateSubmissionTimestampStatement.setString(1, userId);
    				updateSubmissionTimestampStatement.setInt(2, submissionId.intValue());
    				updateSubmissionTimestampStatement.executeUpdate();
    			} catch (SQLException e) {
    				throw new SystemException(e, className,
    						"cleanupContributionDetails",
    						"Problem occurred in prepare statement for: "
    						+ SQL_SET_SUBMISSION_TIMESTAMP
    						+ " with submissionNumber: " + submissionId);
        		} finally {
        		    close(updateSubmissionTimestampStatement, null);
                }
            }

            PreparedStatement updateSubmissionJournalUserTypeStatement = null;
            if (oldItem.isDraft()) {
                // set submission timestamp for orignal submission only
                try {
                    updateSubmissionJournalUserTypeStatement = connection.prepareStatement(SQL_SET_SUBMISSION_JOURNAL_USER_TYPE);
                    updateSubmissionJournalUserTypeStatement.setString(1, userType);
                    updateSubmissionJournalUserTypeStatement.setInt(2, submissionId.intValue());
                    updateSubmissionJournalUserTypeStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new SystemException(e, className,
                            "cleanupContributionDetails",
                            "Problem occurred in prepare statement for: "
                            + SQL_SET_SUBMISSION_JOURNAL_USER_TYPE
                            + " with submissionNumber: " + submissionId + " contract ID:" + contractId);
                } finally {
                    close(updateSubmissionTimestampStatement, null);
                }
            }
        } finally {
            close(null, connection);
        }

        // insert to the money type table for each distinct money type used in the allocation
		insertMoneyTypes(submissionId, contractId, userId, moneyTypeList);

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- cleanupContributionDetails");
		}
	}

    /**
     * Delete records from employee_vesting_percentage where percentage is empty
     * @param submissionId
     * @param contractid
     * @param userId
     * @throws SystemException
     */
    public static void cleanupVestingDetails(Integer submissionId,
            Integer contractId, String userId, String userType) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> cleanupVestingDetails");
        }

        if (submissionId == null) {
            // this should never happen
            return;
        }

        Map moneyTypeList = new HashMap();
        Connection connection = null;

        try {
            try {
                connection = getDefaultConnection(className,
                        SUBMISSION_DATA_SOURCE_NAME);
            } catch (SQLException e) {
                throw new SystemException(e, className,
                        "cleanupVestingDetails",
                        "Problem occurred getting a connection");
            }

            PreparedStatement percentageDeleteStatement = null;
            try {
                percentageDeleteStatement = connection
                        .prepareStatement(SQL_DELETE_VESTING_PERCENTAGES +
                                SQL_DELETE_VESTING_PERCENTAGES_EMPTY_CLAUSE);
            } catch (SQLException e) {
                throw new SystemException(e, className,
                        "cleanupVestingDetails",
                        "Problem occurred in prepare statement for: "
                                + SQL_DELETE_VESTING_PERCENTAGES
                                + SQL_DELETE_VESTING_PERCENTAGES_EMPTY_CLAUSE
                                + " with submissionNumber: " + submissionId
                                + " contractNumber: " + contractId);
            }


            try {
                percentageDeleteStatement.setInt(1, submissionId.intValue());
                percentageDeleteStatement.setInt(2, contractId.intValue());

                percentageDeleteStatement.executeUpdate();
            } catch (SQLException e) {
                throw new SystemException(
                        e,
                        className,
                        "cleanupVestingDetails",
                        "Problem occurred in prepared call: "
                        + SQL_DELETE_VESTING_PERCENTAGES
                        + SQL_DELETE_VESTING_PERCENTAGES_EMPTY_CLAUSE
                        + " with submissionNumber: " + submissionId
                        + " contractNumber: " + contractId);

            } finally {
                close(percentageDeleteStatement, null);
            }

        } finally {
            close(null, connection);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- cleanupVestingDetails");
        }
    }

	/**
	 * given an existing submissionId and contractId, check if the case is too large
	 * for the edit function
	 *
	 * @param submissionId
	 * @param ContractId
	 * @return boolean
	 * @throws SystemException
	 */
	public static boolean isParticipantCountToBigForEdit(int submissionId, int contractId)
			throws SystemException {

		logger.debug("entry -> checkParticipantCountForEdit");
		logger.debug("submissionId -> " + submissionId);
		logger.debug("contractId -> " + contractId);

		boolean participantCountTooBig = false;
		// start overall try block for the entire method
		try {
			// check if the number of participants in the case are greater than the limit for copy
			SelectSingleOrNoValueQueryHandler handler = new SelectSingleOrNoValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, SQL_TOTAL_PARTICIPANT_COUNT_SELECT, Integer.class);
			Integer participantCount = (Integer)handler.select(new Integer[] {new Integer(submissionId),
					new Integer(contractId)});
			if (null != participantCount && participantCount.intValue() > Integer.parseInt(COPY_SIZE_LIMIT)) {
				participantCountTooBig = true;
			}
		} catch (DAOException e) {
			throw handleDAOException(e, className,
					"checkParticipantCountForEdit",
					"Problem occurred in prepared call: "
							+ SQL_TOTAL_PARTICIPANT_COUNT_SELECT);
		}
		logger.debug("exit <- checkParticipantCountForEdit");

		return participantCountTooBig;
	}

    /**
     * This method returns the list of TPA user IDs whose submissions are
     * accessible by the given TPA user.
     *
     * @param tpaUserId
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static List getAccessibleTpaUserIds(int tpaUserId, int contractId)
            throws SystemException {
        try {
            // check if the number of participants in the case are greater than
            // the limit for copy
            SelectMultiValueQueryHandler handler = new SelectMultiValueQueryHandler(
                    CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_TPA_USERS, Integer.class);
            Object[] ids = (Object[]) handler.select(new Object[] {
                    new Integer(contractId), new Integer(tpaUserId) });
            if (ids != null && ids.length > 0) {
                return Arrays.asList(ids);
            } else {
                return new ArrayList();
            }
        } catch (DAOException e) {
            throw handleDAOException(e, className,
                    "checkParticipantCountForEdit",
                    "Problem occurred in prepared call: "
                            + SQL_SELECT_TPA_USERS);
        }

    }

	/**
	 * Check if the number of participants in the case are greater than the limit for copy
	 */
    private static int getCopyAllowedReasonCode(int submissionId, int contractId) throws SystemException {

    	int result = CopiedSubmissionHistoryItem.NO_ERROR;
    	try {
			SelectSingleOrNoValueQueryHandler handler = new SelectSingleOrNoValueQueryHandler(
					SUBMISSION_DATA_SOURCE_NAME, SQL_TOTAL_PARTICIPANT_COUNT_SELECT, Integer.class);
			Integer participantCount = (Integer)handler.select(new Integer[] {new Integer(submissionId),
					new Integer(contractId)});
            if (participantCount == null) {
            	result = CopiedSubmissionHistoryItem.ERROR_NO_VALID_DATA;
            } else if (participantCount.intValue() > Integer.parseInt(COPY_SIZE_LIMIT)) {
            	result = CopiedSubmissionHistoryItem.ERROR_COPY_SIZE_LIMIT_REACHED;
            }
            return result;

		} catch (DAOException e) {
			throw handleDAOException(e, className, "getCopyAllowedReasonCode", "Problem occurred in prepared call: " +
					SQL_TOTAL_PARTICIPANT_COUNT_SELECT + ", for " + submissionId + ", " + contractId);
		}
    }

    /**
     * Get the last submission date for the given contractId.
     *
     * @param contractId
     * @return last submission date
     * @throws SystemException
     */
    public static Date getLastSubmissionDate(int contractId) throws SystemException {
        Date lastSubmissionDate;
        try {
            SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
                    SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_LAST_SUBMISSION,
                    Date.class);
            lastSubmissionDate = (Date) handler.select(new Object[] { new Integer(contractId) });
        } catch (DAOException e) {
            throw handleDAOException(e, className, "getLastSubmissionDate",
                    "Problem occurred in prepared call: "
                            + SQL_SELECT_LAST_SUBMISSION);
        }

        return lastSubmissionDate;
    }

    /**
     * Get application code from the submission journal entry
     *
     * @param contractId
     * @param submissionId
     * @return application code
     * @throws SystemException
     */
    public static String getApplicationCode(DataSource sdbDataSource,
			int submissionId) throws SystemException {
		String applicationCode;
		try {
			SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
					sdbDataSource, SQL_SELECT_APPLICATION_CODE, String.class);
			applicationCode = (String) handler.select(new Object[] {
					new Integer(submissionId) });
		} catch (DAOException e) {
			throw handleDAOException(e, className, "getApplicationCode",
					"Problem occurred in prepared call: "
							+ SQL_SELECT_APPLICATION_CODE);
		}

		return applicationCode;
	}

    /**
     * This method returns the list of TPA user IDs whose submissions are
     * accessible by the given TPA user.
     *
     * @param tpaUserId
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static List getsAccessibleTpaUserIds(int tpaUserId, int contractId)
            throws SystemException {
        try {
            // check if the number of participants in the case are greater than
            // the limit for copy
            SelectMultiValueQueryHandler handler = new SelectMultiValueQueryHandler(
                    CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_TPA_USERS, Integer.class);
            Object[] ids = (Object[]) handler.select(new Object[] {
                    new Integer(contractId), new Integer(tpaUserId) });
            if (ids != null && ids.length > 0) {
                return Arrays.asList(ids);
            } else {
                return new ArrayList();
            }
        } catch (DAOException e) {
            throw handleDAOException(e, className,
                    "checkParticipantCountForEdit",
                    "Problem occurred in prepared call: "
                            + SQL_SELECT_TPA_USERS);
        }

    }

    public static java.sql.Date convertStringToSQLDate(String dateAsString, SimpleDateFormat format) throws SystemException {
	try {
			if (dateAsString == null)
				return null;
			java.sql.Date date = null;
			synchronized(format) {
				date = new java.sql.Date(format.parse(dateAsString).getTime());
			}
			return date;
		} catch (ParseException e) {
			throw new SystemException(e, className, "convertStringToSQLDate", ""
					+ dateAsString
					+ "\" contains invalid characters");
		}
    }
    
    /**
     * This method returns the Original creator for the given submission ID
     * @param submissionId
     * @return
     * @throws SystemException
     */
    public static String getSubmissionCreatorId(DataSource sdbDataSource,int submissionId) throws SystemException{
    	String creatorId;
    	try{
    		SelectSingleValueQueryHandler handler = new SelectSingleValueQueryHandler(
    				sdbDataSource, SQL_SUBMISSION_CREATOR_ID, String.class);
    		creatorId = (String) handler.select(new Object[] {
    					new Integer(submissionId) });
    	}catch (DAOException e) {
    		throw handleDAOException(e, className, "getSubmissionCreatorId",
    				"Problem occurred in prepared call: "
    					+ SQL_SUBMISSION_CREATOR_ID+" for the submission : " + submissionId);
    	}
    	return creatorId;
    }
    
  //CL89281 fix - delete only draft status records but not others    
    /**
     * This method determines whether the DB table has process_status_code in draft or not.  
     * @param submissionId
     * @param contractId
     * @return
     * @throws SystemException
     */
    public static boolean isSubmissionInDraft(int submissionId, int contractId) throws SystemException
    {
    	if(logger.isDebugEnabled())logger.debug("entry -> isSubmissionInDraft");
		PreparedStatement st = null;
    	Connection con = null;
    	ResultSet rs =  null;    	
    	boolean submissionDraft = false;
    	StringBuffer selectQuery = new StringBuffer(SQL_CASE_STATUS_SELECT);
    	selectQuery.append(" and process_status_code in ('14', '97')");
		try {
	    		con = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);
	    		st = con.prepareStatement(selectQuery.toString());
				st.setInt(1, submissionId);
				st.setInt(2, contractId);
				rs = st.executeQuery();
				
				if(rs.next()) submissionDraft = true;
				
			}catch(SQLException se){
				throw new SystemException(
                se,
                className,
                "isSubmissionInDraft",
                "Problem when executing SQL_CASE_STATUS_SELECT query,"
                + " submissionId="+submissionId
                + ", contractId="+contractId);
	        } finally {
	        	try{
	        		if(rs != null) rs.close();
	        	}catch(SQLException e){
	        		
	        	}
	            close(st, null);
	            close(null, con);
	        }
	    	if(logger.isDebugEnabled())logger.debug("exit -> isSubmissionInDraft");
	    	
		return submissionDraft;
    }
    
    public static LongTermPartTimeDetailItem getLongTermPartTimeDetails(int submissionId,
            int contractId, ReportCriteria criteria) throws SystemException {

        logger.debug("entry -> getLongTermPartTimeDetails");
        logger.debug("submissionId -> " + submissionId);
        logger.debug("contractId -> " + contractId);

        // gather basic data from the submission history view
        SubmissionHistoryItem submissionCase = null;
        String processStatusCode = EMPTY_STRING;

        StringBuffer query = new StringBuffer(SQL_HISTORY_SELECT);
        query.append(AND_SUBMISSION_NUMBER_CLAUSE);
        query.append(AND_TYPE_CLAUSE);

        SelectBeanListQueryHandler handler = new SelectBeanListQueryHandler(
                SUBMISSION_DATA_SOURCE_NAME, query.toString(),
                SubmissionHistoryItem.class);

        try {
            logger.debug("Executing Prepared SQL Statment: " + query.toString());
            List list = (List) handler.select(new Object[] {
                    new Integer(contractId), new BigDecimal(String.valueOf(submissionId)),
                    SUBMISSION_TYPE_LTPT});

            if (list == null || list.size() == 0) {
                return null;
            }

            submissionCase = (SubmissionHistoryItem) list.get(0);

            // populate the lock object
            Lock lock = new Lock();
            lock.setUserId(submissionCase.getLockUserId());
            lock.setLockTS(submissionCase.getLockTS());
            submissionCase.setLock(lock);

        } catch (DAOException e) {
            throw handleDAOException(e, className, "getLongTermPartTimeDetails",
                    "Problem occurred prepared call: " + query
                            + " with submissionId: " + submissionId
                            + " contractNumber: " + contractId);
        }

        // get contract name
        String contractName = EMPTY_STRING;
        try {
            SelectSingleValueQueryHandler handler4 = new SelectSingleValueQueryHandler(
                    SUBMISSION_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_NAME,
                    String.class);
            contractName = (String) handler4
                    .select(new Object[] { new Integer(submissionId) });
        } catch (DAOException e) {
            throw handleDAOException(e, className, "getLongTermPartTimeDetails",
                    "Problem occurred in prepared call: " + SQL_SELECT_CONTRACT_NAME +
                    " with submissionId " + submissionId);
        }

        // build basic value object
        LongTermPartTimeDetailItem  longTermPartTimeDetail = new LongTermPartTimeDetailItem(
                        submissionCase.getSubmitterID(),
                        submissionCase.getSubmitterType(),
                        submissionCase.getSubmitterName(),
                        submissionCase.getSubmissionId(),
                        submissionCase.getSubmissionDate(),
                        submissionCase.getType(),
                        submissionCase.getSystemStatus(),
                        submissionCase.getApplicationCode(),
                        new Integer(contractId),
                        contractName,
                        submissionCase.getTpaSystemName(),
                        submissionCase.getTpaSubmissionType(),
                        submissionCase.getTpaVersionNo(),
                        0,
                        submissionCase.getLock());
        longTermPartTimeDetail.setTransmissionId(submissionId);
        longTermPartTimeDetail.setContractId(submissionCase.getContractId());
        longTermPartTimeDetail.setSubmissionId(submissionCase.getSubmissionId());
        longTermPartTimeDetail.setSubmitterEmail(submissionCase.getSubmitterEmail());
        processStatusCode = submissionCase.getSystemStatus();

        Connection connection = null;
        try {
            connection = getDefaultConnection(className, SUBMISSION_DATA_SOURCE_NAME);

            // add error details from submission_case table
            getLTPTErrorDetails(connection, submissionId, contractId, longTermPartTimeDetail);

            // add participants to value object
            getLTPTParticipantData(connection, new Integer(submissionId), contractId,
            		longTermPartTimeDetail, criteria);

            longTermPartTimeDetail.setParticipantSortOption(getParticipantSortOption(contractId));

            setUserName(longTermPartTimeDetail);

            setLockedByType(longTermPartTimeDetail);

        } catch (SQLException e) {
            throw new SystemException(e, className,
                    "getLongTermPartTimeDetails",
                    "Problem occurred when getting connection "
                            + " with submissionNumber: " + submissionId
                            + " contractNumber: " + contractId);
        } finally {
            close(null,connection);
        }

        logger.debug("exit <- getLongTermPartTimeDetails");

        return longTermPartTimeDetail;

    }
    
}
