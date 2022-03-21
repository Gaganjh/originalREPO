package com.manulife.pension.ps.service.withdrawal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.common.Period;
import com.manulife.pension.content.valueobject.Location;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.withdrawal.valueobject.SubmissionEmailVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.notification.util.BrowseServiceHelper;
import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;

/**
 * Retrieves withdrawal requests that need to have a reminder email generated, based on their status
 * and creation date.
 * 
 * Aurelian_Penciu [20070316] Added code to retrieve Ready-For-Entry W/Ds that need to have an Notes
 * email missing notification
 * 
 * @author Mihai Popa
 * 
 */
public class EmailGeneratorDAO extends BaseDatabaseDAO {

    private static final String className = EmailGeneratorDAO.class.getName();

    private static final Logger logger = Logger.getLogger(EmailGeneratorDAO.class);

    private static final String getReviewReminderQuery = "select sc.submission_id, sc.contract_id, "
            + "sc.process_status_code, sc.submission_case_type_code, "
            + "sw.created_ts requestDate, sw.expiration_date, "
            + "ec.first_name, ec.last_name, ec.middle_initial, "
            + " was.created_ts, "
            + " ccs.contract_name, ccs.manulife_company_id, "
            + " ccs.contract_status_code, "
            + " up.email_address_text " + "from "
            + STP_SCHEMA_NAME
            + "submission_case sc "
            + " inner join "
            + STP_SCHEMA_NAME
            + "submission_withdrawal sw on sc.submission_id = sw.submission_id "
            + " inner join "
            + STP_SCHEMA_NAME
            + "activity_summary was on sc.submission_id = was.submission_id "
            + " inner join "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "employee_contract ec on sw.profile_id = ec.profile_id and sc.contract_id = ec.contract_id "
            + " inner join "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "contract_cs ccs on  sc.contract_id = ccs.contract_id "
            + " inner join "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "user_profile up on sw.created_user_profile_id = up.user_profile_id "
            + "where "
            + " sc.submission_case_type_code = 'W' "
            + "and ( "
            + "       (sc.process_status_code='"
            + WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode()
            + "' and was.status_code ='"
            + WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_APPROVAL
            + "' )"
            + "     or "
            + "       (sc.process_status_code='"
            + WithdrawalStateEnum.PENDING_REVIEW.getStatusCode()
            + "' and was.status_code ='"
            + WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW
            + "' )"
            + "    )"
            + "and was.created_ts between ? and ? ";

    public static final String EXPIRY_REMINDER_QUERY = "select sc.submission_id, sc.contract_id, "
            + "sc.process_status_code, sc.submission_case_type_code, "
            + "sw.created_ts requestDate, sw.expiration_date, sw.created_by_role_code, "
            + "ec.first_name, ec.last_name, ec.middle_initial, " + " was.created_ts, "
            + " ccs.contract_name, ccs.manulife_company_id, " + " ccs.contract_status_code, "
            + " up.email_address_text " + "from "
            + STP_SCHEMA_NAME
            + "submission_case sc "
            + " inner join "
            + STP_SCHEMA_NAME
            + "submission_withdrawal sw on sc.submission_id = sw.submission_id "
            + " inner join "
            + STP_SCHEMA_NAME
            + "activity_summary was on sc.submission_id = was.submission_id "
            + " inner join "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "employee_contract ec on sw.profile_id = ec.profile_id and sc.contract_id = ec.contract_id "
            + " inner join "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "contract_cs ccs on  sc.contract_id = ccs.contract_id "
            + " inner join "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "user_profile up on sw.created_user_profile_id = up.user_profile_id "
            + "where "
            + " sc.submission_case_type_code = 'W' "
            + "and ( "
            + "       (sc.process_status_code='"
            + WithdrawalStateEnum.PENDING_APPROVAL.getStatusCode()
            + "' and was.status_code ='"
            + WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_APPROVAL
            + "' )"
            + "     or "
            + "       (sc.process_status_code='"
            + WithdrawalStateEnum.PENDING_REVIEW.getStatusCode()
            + "' and was.status_code ='"
            + WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW
            + "' )"
            + "    )"
            + " and sw.expiration_date between ? and ? ";

    private static final String getReadyForEntryRequestsQuery = "SELECT sc.submission_id, "
            + "  sc.contract_id, " + "  sc.process_status_code, " + "  sc.processed_ts, "
            + "  ec.first_name,  " + "  ec.last_name, ec.middle_initial " + " FROM "
            + STP_SCHEMA_NAME + "submission_case sc, " + STP_SCHEMA_NAME
            + "submission_withdrawal sw, " + PLAN_SPONSOR_SCHEMA_NAME + "employee_contract ec "
            + " WHERE sc.process_status_code = 'W7' " + "  AND sc.submission_case_type_code = 'W' "
            + "  AND sc.submission_id = sw.submission_id "
            + "  AND sc.contract_id = ec.contract_id " + "  AND sw.profile_id = ec.profile_id "
            + "  AND sc.processed_ts >= ? ";

    // Queries for setting/reading the Last run date for the ReadyForEntry eMail reconciliation
    // process
    public static final String READY_FOR_ENTRY_EMAIL_LAST_RECONCILED_DATE_PARAMETER_NAME = "ready.for.entry.last.reconciled.on";

    public static final String READY_FOR_ENTRY_EMAIL_LAST_RECONCILED_DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";

    public static final String updateReadyForEntryEmailLastReconciledDateQuery = "UPDATE "
            + STP_SCHEMA_NAME + "stp_configuration_parameter " + " SET parameter_value = ? "
            + " WHERE parameter_name = '"
            + READY_FOR_ENTRY_EMAIL_LAST_RECONCILED_DATE_PARAMETER_NAME + "'";

    public static final String insertReadyForEntryEmailLastReconciledDateQuery = "INSERT INTO "
            + STP_SCHEMA_NAME + "stp_configuration_parameter "
            + " (parameter_name, parameter_value) " + " VALUES( '"
            + READY_FOR_ENTRY_EMAIL_LAST_RECONCILED_DATE_PARAMETER_NAME + "', ?)";

    public static final String selectReadyForEntryEmailLastReconciledDateQuery = "SELECT parameter_value FROM "
            + STP_SCHEMA_NAME
            + "stp_configuration_parameter "
            + " WHERE parameter_name = '"
            + READY_FOR_ENTRY_EMAIL_LAST_RECONCILED_DATE_PARAMETER_NAME + "'";

    // Lotus Notes JDBC/ODBC bridge
    private static final String SQL_GET_INBOX_CONTENT = "SELECT Subject, Body, PostedDate FROM Memo "
            + " WHERE Form = 'Memo' AND PostedDate >= ?";

    public ArrayList<SubmissionEmailVO> selectAllPendingRequestsForReminders(
            final Period reminderPeriod) throws DAOException {

        logger.debug("entry -> selectAllPendingRequestsForReminders()");

        ArrayList<SubmissionEmailVO> reviewReminderRequests = new ArrayList();
        Connection conn = null;
        PreparedStatement statement = null;
        
        try {
            logger.debug("Executing  Query: " + getReviewReminderQuery);
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareStatement(getReviewReminderQuery);
            Timestamp reminderTimestampEarly = new Timestamp(reminderPeriod.getFrom().getTime());
            Timestamp reminderTimestampLate = new Timestamp(reminderPeriod.getTo().getTime());
            statement.setTimestamp(1, reminderTimestampEarly);
            statement.setTimestamp(2, reminderTimestampLate);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            
            if (resultSet != null) {
                
                while (resultSet.next()) {
                    Integer submissionId = resultSet.getInt("SUBMISSION_ID");
                    Integer contractId = resultSet.getInt("CONTRACT_ID");
                    String contractName = resultSet.getString("CONTRACT_NAME");
                    Date lastStatusUpdateTS = resultSet.getTimestamp("CREATED_TS");
                    String requestStatus = resultSet.getString("PROCESS_STATUS_CODE");
                    String requestType = resultSet.getString("SUBMISSION_CASE_TYPE_CODE");
                    String firstName = resultSet.getString("FIRST_NAME");
                    String lastName = resultSet.getString("LAST_NAME");
                    String middleInitial = resultSet.getString("MIDDLE_INITIAL");
                    String manulifeCompanyId = resultSet.getString("MANULIFE_COMPANY_ID");
                    final String contractStatusCode = resultSet.getString("CONTRACT_STATUS_CODE");
                    Date requestDate = resultSet.getTimestamp("REQUESTDATE");
                    Date expirationDate = resultSet.getDate("EXPIRATION_DATE");
                    String creatorEmail = resultSet.getString("EMAIL_ADDRESS_TEXT");
    
                    final Location location = BrowseServiceHelper
                            .convertManulifeCompanyIdToLocation(manulifeCompanyId);
    
                    SubmissionEmailVO emailVO = new SubmissionEmailVO(submissionId, contractId,
                            contractName, contractStatusCode, null, lastStatusUpdateTS, requestStatus,
                            requestType, firstName, lastName, middleInitial, location, requestDate,
                            expirationDate, creatorEmail);
                    reviewReminderRequests.add(emailVO);
                }
                
            }
            
        } catch (SQLException ex) {
            throw new DAOException("Problem occurred with sub query1: " + getReviewReminderQuery);
        } catch (SystemException e) {
            throw new DAOException("Problem occurred with sub query: " + getReviewReminderQuery);
        } finally {
        	//Fortify:Closed the resource 
        	if(statement != null){
        		 try {
 					statement.close();
 				} catch (SQLException e) {
 					logger.warn("Could not close connexion in selectAllPendingRequestsForReminders()");
 				}
        	}
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.warn("Could not close connexion in selectAllPendingRequestsForReminders()");
                } // end try/catch
            } // fi
            
        } // end try/catch
        logger.debug("exit <- selectAllPendingRequestsForReminders");
        return reviewReminderRequests;
    }

    /**
     * TODO selectAllPendingRequestsForExpiryReminder Description.
     * 
     * @param minExpiryMaturity
     * @param maxExpiryMaturity
     * @return
     * @throws DAOException
     */
    public ArrayList<SubmissionEmailVO> selectAllPendingRequestsForExpiryReminder(
            final Period expiryPeriod) throws DAOException {

        logger.debug("entry -> selectAllPendingRequestsForExpiryReminder()");

        ArrayList<SubmissionEmailVO> reviewReminderRequests = new ArrayList<SubmissionEmailVO>();
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            logger.debug("Executing  Query: " + EXPIRY_REMINDER_QUERY);
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareStatement(EXPIRY_REMINDER_QUERY);
            statement.setDate(1, getSqlDate(expiryPeriod.getFrom()));
            statement.setDate(2, getSqlDate(expiryPeriod.getTo()));
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            
            if (resultSet != null) {
                
                while (resultSet.next()) {
                    Integer submissionId = resultSet.getInt("SUBMISSION_ID");
                    Integer contractId = resultSet.getInt("CONTRACT_ID");
                    String contractName = resultSet.getString("CONTRACT_NAME");
                    Date lastStatusUpdateTS = resultSet.getTimestamp("CREATED_TS");
                    String requestStatus = resultSet.getString("PROCESS_STATUS_CODE");
                    String requestType = resultSet.getString("SUBMISSION_CASE_TYPE_CODE");
                    String firstName = resultSet.getString("FIRST_NAME");
                    String lastName = resultSet.getString("LAST_NAME");
                    String middleInitial = resultSet.getString("MIDDLE_INITIAL");
                    String manulifeCompanyId = resultSet.getString("MANULIFE_COMPANY_ID");
                    final String contractStatusCode = resultSet.getString("CONTRACT_STATUS_CODE");
                    Date requestDate = resultSet.getTimestamp("REQUESTDATE");
                    Date expirationDate = resultSet.getDate("EXPIRATION_DATE");
                    String creatorEmail = resultSet.getString("EMAIL_ADDRESS_TEXT");
                    String createdByRoleCode = resultSet.getString("CREATED_BY_ROLE_CODE");
                    final Location location = BrowseServiceHelper
                            .convertManulifeCompanyIdToLocation(manulifeCompanyId);
    
                    SubmissionEmailVO emailVO = new SubmissionEmailVO(submissionId, contractId,
                            contractName, contractStatusCode, createdByRoleCode, lastStatusUpdateTS,
                            requestStatus, requestType, firstName, lastName, middleInitial, location,
                            requestDate, expirationDate, creatorEmail);
                    reviewReminderRequests.add(emailVO);
                }
                
            }
            
        } catch (SQLException ex) {
            throw new DAOException("Problem occurred with sub query: " + EXPIRY_REMINDER_QUERY, ex);
        } catch (SystemException e) {
            throw new DAOException("Problem occurred with sub query: " + EXPIRY_REMINDER_QUERY, e);
        } finally {
        	if(statement!= null){
        		//Fortify:Closed the resource 
                try {
					statement.close();
				} catch (SQLException e) {
					logger.warn("Could not close connexion in selectAllPendingRequestsForReminders()");
				}
        	}
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.warn("Could not close connexion in reviewReminderRequests()", e);
                }
              
            }
        }
        logger.debug("exit <- selectAllPendingRequestsForExpiryReminder");
        return reviewReminderRequests;
    }

    public static ArrayList<SubmissionEmailVO> selectReadyForEntryRequests(
            final Date approvedAfterDate) throws DAOException {

        logger.debug("entry -> selectReadyForEntryRequests()");

        ArrayList<SubmissionEmailVO> reviewReminderRequests = new ArrayList<SubmissionEmailVO>();
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            logger.debug("Executing  Query: " + getReadyForEntryRequestsQuery);

            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareStatement(getReadyForEntryRequestsQuery);
            Timestamp ts = new Timestamp(approvedAfterDate.getTime());
            statement.setTimestamp(1, ts);
            statement.execute();
           resultSet = statement.getResultSet();
            
            if (resultSet != null) {
                
                while (resultSet.next()) {
                    Timestamp timestamp = resultSet.getTimestamp("PROCESSED_TS");
                    reviewReminderRequests.add(new SubmissionEmailVO(resultSet.getInt("SUBMISSION_ID"),
                            resultSet.getInt("CONTRACT_ID"), null, // contractName
                            null, // contractStatusCode
                            null, // createdByRoleCode
                            timestamp != null ? new Date(timestamp.getTime()) : null, null, // requestStatus
                            null, // requestType
                            StringUtils.trim(resultSet.getString("FIRST_NAME")), StringUtils
                                    .trim(resultSet.getString("LAST_NAME")), StringUtils.trim(resultSet
                                    .getString("MIDDLE_INITIAL")), null, // siteLocation
                            null, // requestDate
                            null, // expirationDate
                            null)); // creator id
                }
                
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DAOException("Problem occurred with query: " + getReadyForEntryRequestsQuery);
        } catch (SystemException e) {
            e.printStackTrace();
            throw new DAOException("Problem occurred with query: " + getReadyForEntryRequestsQuery);
        } finally {
        	//Fortify:Closed the resource 
        	if(statement!= null){
        		 try {
 					statement.close();
 				} catch (SQLException e) {
 					logger.warn("Could not close connexion in selectAllPendingRequestsForReminders()");
 				}
        	}
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.warn("Could not close connexion in reviewReminderRequests()");
                }
               
              
            }
        }
        logger.debug("exit <- selectReadyForEntryRequests");
        return reviewReminderRequests;
    }

    public static Date getReadyForEntryEmailLastReconciledDate() throws DAOException {
        logger.debug("entry ->getReadyForEntryEmailLastReconciledDate()");

        Date lastReconciledDate = new Date(0); // By default: way in the past
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            logger.debug("Executing  Query: " + selectReadyForEntryEmailLastReconciledDateQuery);

            conn = getDefaultConnection(className, STP_DATA_SOURCE_NAME);
            statement = conn
                    .prepareStatement(selectReadyForEntryEmailLastReconciledDateQuery);
            statement.execute();
            resultSet = statement.getResultSet();
            
            if (resultSet != null) {
                
                if (resultSet.next()) {
                    SimpleDateFormat df = new SimpleDateFormat(
                            READY_FOR_ENTRY_EMAIL_LAST_RECONCILED_DATE_FORMAT);
                    lastReconciledDate = df.parse(resultSet.getString("parameter_value"));
                }
                
            }
            
        } catch (ParseException ex) {
            logger.error(ex);
            throw new DAOException("Problem occurred with query: "
                    + selectReadyForEntryEmailLastReconciledDateQuery);
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException("Problem occurred with query: "
                    + selectReadyForEntryEmailLastReconciledDateQuery);
        } catch (SystemException e) {
            logger.error(e);
            throw new DAOException("Problem occurred with query: "
                    + selectReadyForEntryEmailLastReconciledDateQuery);
        } finally {
        	//Fortify:Closed the resource 
        	if(statement != null){
        		try {
        			statement.close();
        		} catch (SQLException e) {
        			logger.warn("Could not close connexion in selectAllPendingRequestsForReminders()");
        		}
        	}
        	
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger
                            .warn("Could not close connexion in getReadyForEntryEmailLastReconciledDate()");
                }
                
            }
        }
        logger.debug("exit <- getReadyForEntryEmailLastReconciledDate");
        return lastReconciledDate;
    }

    public static void setReadyForEntryEmailLastReconciledDate(Date lastReconciledDate)
            throws DAOException {
        logger.debug("entry -> setReadyForEntryEmailLastReconciledDate()");

        SimpleDateFormat df = new SimpleDateFormat(
                READY_FOR_ENTRY_EMAIL_LAST_RECONCILED_DATE_FORMAT);

        if (lastReconciledDate == null) {
            lastReconciledDate = new Date();
        }

        String sql = "";
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            conn = getDefaultConnection(className, STP_DATA_SOURCE_NAME);
            sql = selectReadyForEntryEmailLastReconciledDateQuery;
            logger.debug("Executing  Query: " + sql);
            statement = conn.prepareStatement(sql);
            resultSet = statement.executeQuery();
            boolean exists = resultSet.next();
            resultSet.close();
            statement.close();
            statement = null;
            sql = (exists) ? updateReadyForEntryEmailLastReconciledDateQuery
                    : insertReadyForEntryEmailLastReconciledDateQuery;

            logger.debug("Executing  Query: " + sql);
            statement = conn.prepareStatement(sql);
            statement.setString(1, df.format(lastReconciledDate));
            statement.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
            throw new DAOException("Problem occurred with query: " + sql);
        } catch (SystemException e) {
            logger.error(e);
            throw new DAOException("Problem occurred with query: " + sql);
        } finally {
        	//Fortify:Closed the resource 
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger
                            .warn("Could not close statement in setReadyForEntryEmailLastReconciledDate()");
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger
                            .warn("Could not close connexion in setReadyForEntryEmailLastReconciledDate()");
                }
                
            }
        }
        logger.debug("exit <- setReadyForEntryEmailLastReconciledDate");
    }

    /**
     * @See ReqtSpec-ReadyForEntry Email Monitor [WMM-5, WMM-6]
     * 
     * Retrieves a list of submission IDs posted AFTER the date/time of the previous run.
     * 
     * WMM-5: Posted date/time >= Previous run date/time - 1hour
     * 
     * @param previousRunTs Date & time of the last successful execution of this process.
     * 
     * @return List of Submission IDs with notes posted during the specified interval
     */
    public static Collection<Integer> getPostedSubmissions(String notesDbDsn, Date previousRunTs)
            throws DAOException {
        Collection<Integer> submissionIds = new ArrayList<Integer>();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getIWithdrawalsConnection(notesDbDsn);
            stmt = conn.prepareStatement(SQL_GET_INBOX_CONTENT);
            stmt.setTimestamp(1, new Timestamp(previousRunTs.getTime()));
            rs = stmt.executeQuery();

            while (rs.next()) {
                Integer submissionId = parseIntegerFromSubject(rs.getString("Subject"));
                if (submissionId != -1) {
                    submissionIds.add(submissionId);
                }
            }

            rs.close();
            rs = null;
        } catch (SQLException sqlException) {
            throw new DAOException("Failed to execute ReadyForEntryEmailMonitorProcess statement.",
                    sqlException);
        } finally {
            try {
            	  try {
            		  if(stmt!= null){
            			  stmt.close();
            		  }
      			} catch (SQLException e) {
      				logger.warn("Could not close connexion in selectAllPendingRequestsForReminders()");
      			}
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                logger
                        .error(
                                "Could not close connection to "
                                        + notesDbDsn
                                        + ". You may need to terminate the NSQLE32.exe Windows process if still running.",
                                e);
            }
          
        }

        return submissionIds;
    }

    /**
     * @See ReqtSpec-ReadyForEntry Email Monitor [WMM-6]
     * 
     * Parses the subject line and returns the Submission ID value. The subject line must be
     * formatted like: <team> <CAR ID> [<contract#> - <submission#>] <participant last name>.
     * 
     * @param subject i:withdrawals mail-in message subject
     * 
     * @return Integer value of the attribute identified by label
     * 
     */
    private static Integer parseIntegerFromSubject(final String subject) {

        final int errorReturnValue = -1;
        Integer submissionId = errorReturnValue;

        final NumberFormat numberFormat = java.text.DecimalFormat.getIntegerInstance();
        int start = subject.indexOf('[');
        if (start >= 0) {
            start = subject.indexOf('-', start);
        }

        int end = subject.indexOf(']');

        if (start < 0 || end < 0 || start == end) {
            // If subject markers not found in order, a -1 value is returned.
            return errorReturnValue;
        }

        final String sSubmissionId = subject.substring(start + 1, end).trim();

        try {
            submissionId = numberFormat.parse(sSubmissionId).intValue();
        } catch (ParseException e) {
            // If unable to parse, a -1 value is returned.
            return errorReturnValue;
        }
        return submissionId;
    }

    private static Connection getIWithdrawalsConnection(String notesDbDsn) throws SQLException,
            DAOException {
        final String driverClassName = "sun.jdbc.odbc.JdbcOdbcDriver";
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new DAOException("Unable to find class for driver[" + driverClassName + "]",
                    classNotFoundException);
        } // end try/catch
        Connection conn = DriverManager.getConnection(notesDbDsn);
        conn.setReadOnly(true);

        return conn;
    }
}
