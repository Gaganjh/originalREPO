/**
 * 
 * @ author kuthiha
 * Feb 6, 2007
 */
package com.manulife.pension.service.withdrawal.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.exception.WithdrawalDaoException;
import com.manulife.pension.service.withdrawal.valueobject.LegaleseInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.JdbcHelper;

/**
 * @author kuthiha
 * 
 */
public class WithdrawalLegaleseDao extends BaseDatabaseDAO {

    private static final String CLASS_NAME = WithdrawalLegaleseDao.class.getName();

    private static final Logger logger = Logger.getLogger(WithdrawalLegaleseDao.class);

    private static final String LEGALESE_TABLE = "LEGALESE";

    private static final String WITHDRAWAL_LEGALESE_TABLE = "WITHDRAWAL_LEGALESE";

    private static final int[] SQL_INSERT_LEGALESE_PARAMETER_TYPES = { Types.INTEGER,
            Types.VARCHAR, Types.VARCHAR, Types.INTEGER };

    private static final int[] SQL_INSERT_WITHDRAWAL_LEGALESE_PARAMETER_TYPES = { Types.INTEGER,
            Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER };

    private static final String SQL_INSERT_LEGALESE = " INSERT INTO "
            + BaseDatabaseDAO.STP_SCHEMA_NAME + LEGALESE_TABLE + " ( CONTENT_KEY, "
            + " CMA_SITE_CODE, " + " CONTENT_VERSION_NO, " + " LEGALESE_TEXT, "
            + " CREATED_USER_PROFILE_ID, " + " CREATED_TS) " + " ( SELECT " + " T.CONTENT_KEY, "
            + " T.CMA_SITE_CODE, " + " ( SELECT "
            + " COALESCE(MAX(LLL.CONTENT_VERSION_NO) + 1,1) AS CONTENT_VERSION_NO " + " FROM "
            + " SYSIBM.SYSDUMMY1 " + " LEFT OUTER JOIN " + BaseDatabaseDAO.STP_SCHEMA_NAME
            + LEGALESE_TABLE + " LLL " + " ON LLL.CONTENT_KEY = T.CONTENT_KEY " + " AND "
            + " LLL.CMA_SITE_CODE = T.CMA_SITE_CODE  "
            + " GROUP BY LLL.CONTENT_KEY) AS CONTENT_VERSION_NO, " + " T.LEGALESE_TEXT, "
            + " T.CREATED_USER_PROFILE_ID, " + " CURRENT TIMESTAMP AS CREATED_TS " + " FROM "
            + " SYSIBM.SYSDUMMY1, " + " ( VALUES ( " + " CAST(? AS INTEGER), "
            + " CAST(? AS CHAR(2)), " + " CAST(? AS VARCHAR(6000)), " + " CAST(? AS DECIMAL(9)))) "
            + " T(CONTENT_KEY,CMA_SITE_CODE,LEGALESE_TEXT,CREATED_USER_PROFILE_ID) " + " WHERE "
            + " NOT EXISTS " + " ( SELECT A.CONTENT_KEY, " + " A.CONTENT_VERSION_NO, "
            + " A.CMA_SITE_CODE " + " FROM " + BaseDatabaseDAO.STP_SCHEMA_NAME + LEGALESE_TABLE
            + " A " + " WHERE " + " A.CONTENT_KEY = T.CONTENT_KEY " + " AND "
            + " A.CMA_SITE_CODE = T.CMA_SITE_CODE " + " AND "
            + " A.LEGALESE_TEXT = T.LEGALESE_TEXT))";

    private static final String SQL_READ_LEGALESE_VERSION = " SELECT "
            // + " CONTENT_KEY AS contentKey, "
            + " CONTENT_VERSION_NO AS contentVersionNo " + " FROM "
            + BaseDatabaseDAO.STP_SCHEMA_NAME + LEGALESE_TABLE + " WHERE " + " CONTENT_KEY = ? "
            + " AND " + " VARCHAR(LEGALESE_TEXT) = ? " + " AND " + " CMA_SITE_CODE = ? ";

    private static final String SQL_INSERT_WITHDRAWAL_LEGALESE = " INSERT INTO "
            + BaseDatabaseDAO.STP_SCHEMA_NAME
            + WITHDRAWAL_LEGALESE_TABLE
            + " ( SUBMISSION_ID, CONTENT_KEY, "
            + " CMA_SITE_CODE, "
            + " CONTENT_VERSION_NO, "
            + " CREATED_USER_PROFILE_ID, "
            + " CREATED_TS) "
            + " ( SELECT "
            + "T.SUBMISSION_ID, "
            + " T.CONTENT_KEY, "
            + " T.CMA_SITE_CODE, "
            + " T.CONTENT_VERSION_NO, "
            + " T.CREATED_USER_PROFILE_ID, "
            + " CURRENT TIMESTAMP AS CREATED_TS "
            + " FROM "
            + " SYSIBM.SYSDUMMY1, "
            + " ( VALUES ( "
            + " CAST(? AS INTEGER), "
            + " CAST(? AS INTEGER),"
            + " CAST(? AS CHAR(2)), "
            + " CAST(? AS INTEGER),"
            + " CAST(? AS DECIMAL(9)))) "
            + " T(SUBMISSION_ID, CONTENT_KEY, CMA_SITE_CODE, CONTENT_VERSION_NO, CREATED_USER_PROFILE_ID) "
            + " WHERE " + " NOT EXISTS " + " ( SELECT A.SUBMISSION_ID, " + " A.CONTENT_KEY, "
            + "A.CMA_SITE_CODE, " + " A.CONTENT_VERSION_NO " + " FROM "
            + BaseDatabaseDAO.STP_SCHEMA_NAME + WITHDRAWAL_LEGALESE_TABLE + " A " + " WHERE "
            + " A.SUBMISSION_ID = T.SUBMISSION_ID" + " AND " + " A.CONTENT_KEY = T.CONTENT_KEY "
            + " AND " + " A.CMA_SITE_CODE = T.CMA_SITE_CODE " + " AND "
            + " A.CONTENT_VERSION_NO = T.CONTENT_VERSION_NO))";

    /**
     * This method inserts a row into the STP100.LEGALESE table if it does not already contain a row
     * with the same text.
     * 
     * @param withdrawalRequest The request to update from.
     * @throws SystemException If an exception occurs.
     */
    // public void updateLegaleseInfo(final WithdrawalRequest withdrawalRequest)
    public void updateLegaleseInfo(final int contractId, final int submissionId,
            LegaleseInfo legaleseInfo) throws SystemException {

        // final LegaleseInfo legaleseInfo = withdrawalRequest.getLegaleseInfo();
        if (legaleseInfo == null) {
            return;
        }

        final SQLInsertHandler handler = new SQLInsertHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_LEGALESE,
                SQL_INSERT_LEGALESE_PARAMETER_TYPES);
        final Integer contentKey = legaleseInfo.getContentId();
        final String site = legaleseInfo.getCmaSiteCode();

        final String legaleseText = legaleseInfo.getLegaleseText();
        final Integer userProfileId = legaleseInfo.getCreatorUserProfileId();

        final List<Object> params = new ArrayList<Object>();
        params.add(contentKey);
        params.add(site);
        params.add(legaleseText);
        params.add(userProfileId);

        try {
            handler.insert(params.toArray());
        } catch (DAOException e) {
            throw handleDAOException(e, CLASS_NAME, "updateLegaleseInfo",
                    "Problem occurred in prepared call: "
                            + SQL_INSERT_LEGALESE
                            // + " for contract ID " + withdrawalRequest.getContractId()
                            // + " and SubmissionId " + withdrawalRequest.getSubmissionId());
                            + " for contract ID " + contractId + " and SubmissionId "
                            + submissionId);
        }

    }

    /**
     * This method inserts a row into the STP100.WITHDRAWAL_LEGALESE table if it does not already
     * contain a row with the same keys (submissionId, contentKey, cmaSiteCode and contentVersionNo.
     * 
     * @param withdrawalRequest The request to update from.
     * @throws SystemException If an exception occurs.
     */
    public void insertWithdrawalLegaleseInfo(int submissionId,
            final WithdrawalRequest withdrawalRequest) throws DistributionServiceException {

        LegaleseInfo legaleseInfo = null;
        Integer version = null;

        if (StringUtils.equals(WithdrawalRequest.CMA_SITE_CODE_EZK, withdrawalRequest
                .getCmaSiteCode())) {
            legaleseInfo = withdrawalRequest.getParticipantLegaleseInfo();
        } else {
            legaleseInfo = withdrawalRequest.getLegaleseInfo();
        }
        if (legaleseInfo == null) {
            return;
        }
        legaleseInfo.setCmaSiteCode(withdrawalRequest.getCmaSiteCode());
        try {
            updateLegaleseInfo(withdrawalRequest.getContractId(), submissionId, legaleseInfo);
            version = getLegaleseTextVersion(legaleseInfo);
        } catch (SystemException systemException) {
            throw ExceptionHandlerUtility.wrap(systemException);
        }

        final SQLInsertHandler handler = new SQLInsertHandler(
                BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME, SQL_INSERT_WITHDRAWAL_LEGALESE,
                SQL_INSERT_WITHDRAWAL_LEGALESE_PARAMETER_TYPES);

        final Integer contentKey = legaleseInfo.getContentId();
        final String site = legaleseInfo.getCmaSiteCode();
        final Integer contentVersionNo = version;
        final Integer userProfileId = legaleseInfo.getCreatorUserProfileId();

        final List<Object> params = new ArrayList<Object>();
        params.add(submissionId);
        params.add(contentKey);
        params.add(site);
        params.add(contentVersionNo);
        params.add(userProfileId);

        try {
            handler.insert(params.toArray());
        } catch (DAOException e) {
            throw new WithdrawalDaoException(e, CLASS_NAME, "insertWithdrawalLegaleseInfo",
                    "Problem occurred in prepared call: " + SQL_INSERT_WITHDRAWAL_LEGALESE
                            + " for contract ID " + withdrawalRequest.getContractId()
                            + " and SubmissionId " + withdrawalRequest.getSubmissionId()
                            + " and CmaSiteCode " + legaleseInfo.getCmaSiteCode());
        }
    }

    /**
     * This method returns the version number for the content_key , the location_code and
     * legalese_text from the Legalese table in Submission database.
     * 
     * @param info
     * @return
     * @throws SystemException
     */
    public Integer getLegaleseTextVersion(final LegaleseInfo info) throws SystemException {
        logger.debug("entry -> getLegaleseVersion");
        logger.debug("contentId -> " + info.getContentId() + " cmaSiteCode :: "
                + info.getCmaSiteCode());
        Integer version = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        StringBuffer query = new StringBuffer(SQL_READ_LEGALESE_VERSION);

        try {
            conn = getDefaultConnection(WithdrawalLegaleseDao.CLASS_NAME,
                    SUBMISSION_DATA_SOURCE_NAME);
            logger.debug("Executing SQL_READ_LEGALESE_VERSION Query: " + query);
            stmt = conn.prepareStatement(SQL_READ_LEGALESE_VERSION);
            stmt.setInt(JdbcHelper.PARAM_1, info.getContentId());
            stmt.setString(JdbcHelper.PARAM_2, info.getLegaleseText());
            stmt.setString(JdbcHelper.PARAM_3, info.getCmaSiteCode());
            rs = stmt.executeQuery();
            while (rs.next()) {

                version = new Integer(rs.getInt("contentVersionNo"));
            }
            rs.close();

        } catch (SQLException e) {
            throw new SystemException(e, CLASS_NAME, "getLegaleseTextVersion", "contentId: "
                    + info.getContentId());
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
                logger.error("could not close connection in "
                        + "WithdrawalLegaleseDao.getLegaleseTextVersion()");
            }
        }
        logger.debug("exit <- getLegaleseTextVersion");
        return version;
    }

}
