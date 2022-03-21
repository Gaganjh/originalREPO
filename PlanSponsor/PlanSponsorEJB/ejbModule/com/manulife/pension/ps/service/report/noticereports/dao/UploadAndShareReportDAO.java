package com.manulife.pension.ps.service.report.noticereports.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportSourceOfUploadShareVO;
import com.manulife.pension.ps.service.report.noticereports.valueobject.UploadAndShareReportTopTenDocumentNamesVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Provide Data access services for Upload And Share report.
 * 
 */
public class UploadAndShareReportDAO extends NoticeReportsBaseDAO {

    private static final String className = UploadAndShareReportDAO.class.getName();
    
    //SimpleDateFormat is converted to FastDateFormat to make it thread safe
    public static FastDateFormat searchDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S", Locale.US);

    private static final Logger logger = Logger.getLogger(UploadAndShareReportDAO.class);

    private static final String CONTRACT_NOTICE_MAILING_ORDER_QUALIFIER = "CM.";

    private static final String WEBSITE_USER_ACTION_LOG_QUALIFIER = "WU.";

    private static final String CONTRACT_NOTICE_DOCUMENT_LOG_QUALIFIER = "CL.";

    private static final String CHANGE_TYPE_CODE_RENAMED = "CHNGN";

    private static final String CHANGE_TYPE_CODE_REPLACED = "REPL";

    private static final String CHANGE_TYPE_CODE_DELETED = "DEL";
    
    private static final String CHANGE_TYPE_CODE_PPT_CHANGED = "CHNGP";
    
    private static final String CHANGE_TYPE_CODE_REPLACED_AND_RENAMED = "CHRPN";
    
    private static final String CHANGE_TYPE_CODE_REPLACED_AND_PPT = "CHRPP";
    
    private static final String CHANGE_TYPE_CODE_RENAMED_AND_PPT = "CHNGB";
    
    private static final String CHANGE_TYPE_CODE_REPLACED_RENAMED_AND_PPT = "CHRPB";

    private static final String SQL_SELECT_TOTAL_CONTRACTS_USING_SERVICE = "WITH CONTRACTS_USING_SERVICE (CONTRACT_ID)  "
            + "AS  " + "( " + "SELECT CL.CONTRACT_ID  " + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CL.CHANGE_TYPE_CODE IN ('UPLD', 'DEL', 'REPL', 'CHNGN', 'CHRPN', 'CHRPB', 'CHNGB', 'CHRPP') "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "UNION "
            + "SELECT CM.CONTRACT_ID  "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_MAILING_ORDER  CM "
            + "WHERE CM.ORDER_STATUS_DATE BETWEEN ? AND ?  "
            + "{1} "
            + "UNION "
            + "SELECT WU.CONTRACT_ID  "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "WEBSITE_USER_ACTION_LOG WU "
            + "WHERE WU.USER_ACTION_NAME = '"
            + PACKAGE_DOWNLOAD
            + "' "
            + "AND WU.CREATED_TS BETWEEN ? AND ? "
            + "{2} "
            + ") "
            + "SELECT COUNT(UNIQUE CONTRACT_ID) AS TOTAL_CONTRACTS  "
            + "FROM CONTRACTS_USING_SERVICE ";

    private static final String SQL_SELECT_NUMBER_NOTICES_UPLOADED_AND_REPLACED = "SELECT COUNT(*) AS NOTICES_UPLOADED_REPLACED "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CL.CHANGE_TYPE_CODE IN ('UPLD', 'REPL', 'CHRPN', 'CHRPP', 'CHRPB') "
            + "AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_OF_USERS_USING_SERVICE = "SELECT COUNT(UNIQUE CL.USER_PROFILE_ID) AS NUMBER_USERS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CL.CHANGE_TYPE_CODE IN ('UPLD', 'DEL', 'REPL', 'CHNGP', 'CHNGN', 'CHRPN', 'CHRPB', 'CHNGB', 'CHRPP') "
            + "AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_CONTRACTS_POSTED_PARTICIPANTS_WEBSITE = "SELECT COUNT(UNIQUE CN.CONTRACT_ID) AS CONTRACTS_POSTED  "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CN.POST_TO_PPT_IND = 'Y'  "
            + "AND CN.CONTRACT_ID = CL.CONTRACT_ID "
            + "AND CN.VERSION_NO = CL.VERSION_NO " + "AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_OF_CONTRACTS_AND_UPLOADED_DOCUMENTS = "SELECT COUNT(UNIQUE CN.CONTRACT_ID) AS NUM_UPLOADED_CONTRACTS, COUNT(UNIQUE CN.DOCUMENT_ID) AS NUM_UPLOADED_DOCS "
        + "FROM "
        + PLAN_SPONSOR_SCHEMA_NAME
        + "CONTRACT_NOTICE_DOCUMENT CN, "
        + PLAN_SPONSOR_SCHEMA_NAME
        + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
        + "WHERE CN.CONTRACT_ID = CL.CONTRACT_ID "
        + "AND CN.VERSION_NO = CL.VERSION_NO "
        + "AND CL.CHANGE_TYPE_CODE = 'UPLD' " + "AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_OF_CONTRACTS_AND_UPLOADED_DOCUMENTS_NON_DELETED = "SELECT COUNT(UNIQUE CL.CONTRACT_ID) AS NUM_UPLOADED_CONTRACTS, COUNT(UNIQUE CL.DOCUMENT_ID) AS NUM_UPLOADED_DOCS "
        + "FROM "
        + PLAN_SPONSOR_SCHEMA_NAME
        + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
        + "WHERE CL.CHANGE_TYPE_CODE = 'UPLD' "
        + "AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_OF_USERS_ACCEPTED_TERMS_OF_USE = "WITH USERS_ACCEPTED(USER_PROFILE_ID, CREATED_TS) "
            + "AS  "
            + "(SELECT UN.USER_PROFILE_ID, MAX(UN.CREATED_TS) AS CREATED_TS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG UN "
            + "WHERE UN.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "GROUP BY UN.USER_PROFILE_ID) "
            + "SELECT COUNT(UNIQUE UL.USER_PROFILE_ID) AS NUM_USERS_ACCEPTED "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG UL, USERS_ACCEPTED UA "
            + "WHERE UL.USER_PROFILE_ID = UA.USER_PROFILE_ID "
            + "AND UL.CREATED_TS = UA.CREATED_TS " + "AND UL.TERMS_ACCEPTANCE_CODE = 'Y' ";

    private static final String SQL_SELECT_NUMBER_OF_USERS_NOT_ACCEPTED_TERMS_OF_USE = "WITH USERS_TERMS(USER_PROFILE_ID, CREATED_TS) "
            + "AS  "
            + "(SELECT UN.USER_PROFILE_ID, MAX(UN.CREATED_TS) AS CREATED_TS "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG UN "
            + "WHERE UN.CREATED_TS BETWEEN ? AND ?  "
            + "{0} "
            + "GROUP BY UN.USER_PROFILE_ID) "
            + "SELECT COUNT(UNIQUE UL.USER_PROFILE_ID) AS NUM_USERS_NOT_ACCEPTED "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG UL, USERS_TERMS UT "
            + "WHERE UL.USER_PROFILE_ID = UT.USER_PROFILE_ID "
            + "AND UL.CREATED_TS = UT.CREATED_TS " + "AND UL.TERMS_ACCEPTANCE_CODE = 'N' ";

    private static final String SQL_SELECT_FILTER_CONTRACT_ID_NUMBER_OF_USERS_TERMS_OF_USE = "AND UN.USER_PROFILE_ID IN  "
	        + "(SELECT USER_PROFILE_ID  "
	        + "FROM "
	        + PLAN_SPONSOR_SCHEMA_NAME
	        + "USER_CONTRACT UC  "
	        + "WHERE UC.CONTRACT_ID = ? ) ";

    private static final String SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED = "SELECT COUNT(*) AS DOCUMENTS_COUNT "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CL.CHANGE_TYPE_CODE IN ( " ;
            
    private static final String SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED_DATE_CLAUSE = " ) AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_NUMBER_OF_DOCS_CHANGED_AND_REPLACED = "SELECT COUNT(*) AS DOCS_CHANGED_REPLACED "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CL.CHANGE_TYPE_CODE IN ('CHRPP', 'CHRPB', 'CHRPN') "
            + "AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_WITH_PARTICIPANTS_WEBSITE = "WITH LATEST_DOC_VERSION(DOCUMENT_ID, VERSION_NO, CREATED_TS) "
            + "AS  "
            + "( "
            + " "
            + "SELECT CL.DOCUMENT_ID, CL.VERSION_NO,  CL.CREATED_TS AS CREATED_TS "
            + "FROM  "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE  CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "GROUP BY CL.DOCUMENT_ID, CL.VERSION_NO, CL.CREATED_TS "
            + "HAVING CL.CREATED_TS = MAX(CL.CREATED_TS) "
            + "AND CL.VERSION_NO = MAX(CL.VERSION_NO) "
            + ")  "
            + "SELECT COUNT(CN.DOCUMENT_ID) AS NUM_DOCS_SHARED "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, LATEST_DOC_VERSION LD "
            + "WHERE CN.DOCUMENT_ID = LD.DOCUMENT_ID "
            + "AND CN.VERSION_NO = LD.VERSION_NO "
            + "AND CN.POST_TO_PPT_IND = 'Y' ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_WITH_PARTICIPANTS_WEBSITE_NON_DELETED = 
    	"SELECT COUNT(UNIQUE CL.DOCUMENT_ID) AS COUNT "
    	+ "FROM " 
    	+ PLAN_SPONSOR_SCHEMA_NAME
    	+ "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL  INNER JOIN " 
    	+ PLAN_SPONSOR_SCHEMA_NAME
    	+ "CONTRACT_NOTICE_DOCUMENT CN "
    	+ "ON CN.DOCUMENT_ID = CL.DOCUMENT_ID AND "
    	+ "CN.POST_TO_PPT_IND = 'Y' AND CL.CREATED_TS BETWEEN   ? AND ? ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_WITH_PARTICIPANTS_WEBSITE_ALL = "SELECT COUNT(UNIQUE CN.CONTRACT_ID) AS NUM_UPLOADED_CONTRACTS, COUNT(UNIQUE CN.DOCUMENT_ID) AS NUM_UPLOADED_DOCS "
        + "FROM "
        + PLAN_SPONSOR_SCHEMA_NAME
        + "CONTRACT_NOTICE_DOCUMENT CN, "
        + PLAN_SPONSOR_SCHEMA_NAME
        + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
        + "WHERE CN.CONTRACT_ID = CL.CONTRACT_ID "
        + "AND CN.VERSION_NO = CL.VERSION_NO "
        + "AND CL.CREATED_TS BETWEEN ? AND ? ";

    private static final String SQL_SELECT_DOCUMENTS_UPLOADED_BY_PLAN_SPONSOR_NEW_BUSINESS = "WITH CONTRACTS_NEW_BUSINESS (DOCUMENT_ID, USER_PROFILE_ID, CONTRACT_ID)  "
            + "AS  "
            + "(SELECT CL.DOCUMENT_ID, CL.USER_PROFILE_ID, CL.CONTRACT_ID   "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C  "
            + "WHERE CL.CHANGE_TYPE_CODE = 'UPLD' "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "AND CL.CONTRACT_ID = C.CONTRACT_ID  "
            + "AND C.CONTRACT_STATUS_CODE IN ('PS', 'DC', 'PC', 'CA')  "
            + ")  "
            + "SELECT COUNT(UNIQUE DOCUMENT_ID) "
            + "FROM CONTRACTS_NEW_BUSINESS CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CN.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') ";

    private static final String SQL_SELECT_DOCUMENTS_UPLOADED_BY_PLAN_SPONSOR_INFORCE = "WITH CONTRACTS_INFORCE (DOCUMENT_ID, USER_PROFILE_ID, CONTRACT_ID)  "
            + "AS  "
            + "(SELECT CL.DOCUMENT_ID, CL.USER_PROFILE_ID, CL.CONTRACT_ID   "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C  "
            + "WHERE CL.CHANGE_TYPE_CODE = 'UPLD' "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "AND CL.CONTRACT_ID = C.CONTRACT_ID  "
            + "AND C.CONTRACT_STATUS_CODE IN ('AC', 'CF')  "
            + ")  "
            + "SELECT COUNT(UNIQUE DOCUMENT_ID) "
            + "FROM CONTRACTS_INFORCE CI, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CI.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CI.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') ";

    private static final String SQL_SELECT_DOCUMENTS_UPLOADED_BY_INTERMEDIARY_CONTACT = "SELECT COUNT(UNIQUE CL.DOCUMENT_ID) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CL.CHANGE_TYPE_CODE = 'UPLD'  "
            + "AND CL.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CL.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE = 'INC' "
            + "AND CL.CREATED_TS BETWEEN ? AND ? {0} ";

    private static final String SQL_SELECT_DOCUMENTS_UPLOADED_BY_TPA = "SELECT COUNT(UNIQUE CL.DOCUMENT_ID) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS CS "
            + "WHERE CL.CHANGE_TYPE_CODE = 'UPLD'  "
            + "AND CL.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND CL.CONTRACT_ID = CS.CONTRACT_ID "
            + "AND CS.THIRD_PARTY_ADMIN_ID <> "
            + TOTAL_CARE_TPA_ADMIN_ID
            + " "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
            + "AND CL.CREATED_TS BETWEEN ? AND ? {0} ";

    private static final String SQL_SELECT_DOCUMENTS_UPLOADED_BY_TOTAL_CARE = "SELECT COUNT(UNIQUE CL.DOCUMENT_ID) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS CS "
            + "WHERE CL.CHANGE_TYPE_CODE = 'UPLD'  "
            + "AND CL.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND CL.CONTRACT_ID = CS.CONTRACT_ID "
            + "AND CS.THIRD_PARTY_ADMIN_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID
            + " "
            + "AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' "
            + "AND CL.CREATED_TS BETWEEN ? AND ? {0} ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_BY_PLAN_SPONSOR_NEW_BUSINESS = "WITH CONTRACTS_NEW_BUSINESS (DOCUMENT_ID, USER_PROFILE_ID, CONTRACT_ID)  "
            + "AS  "
            + "( "
            + "SELECT CL.DOCUMENT_ID, CL.USER_PROFILE_ID, CL.CONTRACT_ID   "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C  "
            + "WHERE CN.POST_TO_PPT_IND = 'Y' "
            + "AND CL.DOCUMENT_ID = CN.DOCUMENT_ID "
            + "AND CL.VERSION_NO = CN.VERSION_NO "
            + "AND CN.CONTRACT_ID = C.CONTRACT_ID  "
            + "AND C.CONTRACT_STATUS_CODE IN ('PS', 'DC', 'PC', 'CA')  "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + ")  "
            + "SELECT COUNT(UNIQUE DOCUMENT_ID) AS NEW_BUSINESS_PS_DOCS "
            + "FROM CONTRACTS_NEW_BUSINESS CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CN.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_BY_PLAN_SPONSOR_INFORCE = "WITH CONTRACTS_NEW_BUSINESS (DOCUMENT_ID, USER_PROFILE_ID, CONTRACT_ID)  "
            + "AS  "
            + "( "
            + "SELECT CL.DOCUMENT_ID, CL.USER_PROFILE_ID, CL.CONTRACT_ID   "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS C  "
            + "WHERE CN.POST_TO_PPT_IND = 'Y' "
            + "AND CL.DOCUMENT_ID = CN.DOCUMENT_ID "
            + "AND CL.VERSION_NO = CN.VERSION_NO "
            + "AND CN.CONTRACT_ID = C.CONTRACT_ID  "
            + "AND C.CONTRACT_STATUS_CODE IN ('AC', 'CF')  "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + ")  "
            + "SELECT COUNT(UNIQUE DOCUMENT_ID) AS INFORCE_PS_DOCS "
            + "FROM CONTRACTS_NEW_BUSINESS CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CN.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CN.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE IN ('PSU', 'TRT', 'AUS', 'ADC') ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_BY_INTERMEDIARY_CONTACT = "SELECT COUNT(UNIQUE CL.DOCUMENT_ID) "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_CONTRACT UC "
            + "WHERE CN.POST_TO_PPT_IND = 'Y' "
            + "AND CL.DOCUMENT_ID = CN.DOCUMENT_ID "
            + "AND CL.VERSION_NO = CN.VERSION_NO "
            + "AND CL.CONTRACT_ID = UC.CONTRACT_ID "
            + "AND CL.USER_PROFILE_ID = UC.USER_PROFILE_ID "
            + "AND UC.SECURITY_ROLE_CODE = 'INC' "
            + "AND CL.CREATED_TS BETWEEN ? AND ? {0} ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_BY_TPA = "WITH TPA_DOCS (DOCUMENT_ID, USER_PROFILE_ID, CONTRACT_ID)  "
            + "AS  "
            + "( "
            + "SELECT CN.DOCUMENT_ID, CL.USER_PROFILE_ID, CN.CONTRACT_ID   "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CN.POST_TO_PPT_IND = 'Y' "
            + "AND CN.DOCUMENT_ID = CL.DOCUMENT_ID "
            + "AND CN.VERSION_NO = CL.VERSION_NO "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + ")  "
            + "SELECT COUNT(UNIQUE TD.DOCUMENT_ID) "
            + "FROM TPA_DOCS TD, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS CS "
            + "WHERE TD.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND TD.CONTRACT_ID = CS.CONTRACT_ID "
            + "AND CS.THIRD_PARTY_ADMIN_ID <> "
            + TOTAL_CARE_TPA_ADMIN_ID + " AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' ";

    private static final String SQL_SELECT_DOCUMENTS_SHARED_BY_TOTAL_CARE = "WITH TC_DOCS (DOCUMENT_ID, USER_PROFILE_ID, CONTRACT_ID)  "
            + "AS  "
            + "( "
            + "SELECT CN.DOCUMENT_ID, CL.USER_PROFILE_ID, CN.CONTRACT_ID   "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CN.POST_TO_PPT_IND = 'Y' "
            + "AND CN.DOCUMENT_ID = CL.DOCUMENT_ID "
            + "AND CN.VERSION_NO = CL.VERSION_NO "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + ")  "
            + "SELECT COUNT(UNIQUE TC.DOCUMENT_ID) "
            + "FROM TC_DOCS TC, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "USER_PROFILE UP, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_CS CS "
            + "WHERE TC.USER_PROFILE_ID = UP.USER_PROFILE_ID "
            + "AND TC.CONTRACT_ID = CS.CONTRACT_ID "
            + "AND CS.THIRD_PARTY_ADMIN_ID = "
            + TOTAL_CARE_TPA_ADMIN_ID + " AND UP.PSW_DIRECTORY_ROLE_CODE = 'TPA' ";

    private static final String SQL_SELECT_NUMBER_OF_SHARED_DOCUMENTS = "SELECT COUNT(CN.DOCUMENT_ID) AS TOTAL_DOCS_SHARED "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CN.POST_TO_PPT_IND = 'Y' "
            + "AND CN.DOCUMENT_ID = CL.DOCUMENT_ID "
            + "AND CN.VERSION_NO = CL.VERSION_NO " + "AND CL.CREATED_TS BETWEEN ? AND ?  ";

    private static final String SQL_SELECT_TOP_TEN_DOCUMENTS_NAMES_AND_PERCENTAGES = "SELECT CN.DOCUMENT_NAME, COUNT(*) AS NUMBER_OF_TIMES, MAX(CL.CREATED_TS) AS LATEST_TIMESTAMP "
            + "FROM "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT CN, "
            + PLAN_SPONSOR_SCHEMA_NAME
            + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CL "
            + "WHERE CL.CHANGE_TYPE_CODE IN ('UPLD') "
            + "AND CN.DOCUMENT_ID = CL.DOCUMENT_ID "
            + "AND CN.VERSION_NO = CL.VERSION_NO "
            + "AND CL.CREATED_TS BETWEEN ? AND ? "
            + "{0} "
            + "GROUP BY CN.DOCUMENT_NAME "
            + "ORDER BY NUMBER_OF_TIMES DESC, MAX(CL.CREATED_TS) DESC  "
            + "FETCH FIRST 10 ROWS ONLY ";

    /**
     * Gets Report Data for Upload and Share Report.
     * 
     * @param criteria
     * @param uploadAndShareReportData
     * @throws SystemException
     * @throws ReportServiceException
     */
    public static void getReportData(ReportCriteria criteria,
            UploadAndShareReportData uploadAndShareReportData) throws SystemException,
            ReportServiceException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getReportData");
        }

        Integer contractId = getContractNumber(criteria);
        boolean isContractSearch = (contractId != null && contractId.intValue() > 0);
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            getUserStats(connection, stmt, criteria, uploadAndShareReportData, contractId,
                    isContractSearch);
            getDocumentStatistics(connection, stmt, criteria, uploadAndShareReportData, contractId,
                    isContractSearch);
            getSourceOfUploadChart(connection, stmt, criteria, uploadAndShareReportData,
                    contractId, isContractSearch);
            getSourceOfShareChart(connection, stmt, criteria, uploadAndShareReportData, contractId,
                    isContractSearch);
            getTopTenDocumentNames(connection, stmt, criteria, uploadAndShareReportData,
                    contractId, isContractSearch);

        } catch (SQLException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Exception Trace: ", e);
            }

            handleSqlException(e, className, "getReportData", MessageFormat.format(
                    GET_REPORT_DATA_ERROR_MESSAGE, UploadAndShareReportData.REPORT_NAME,
                    getContractNumber(criteria), getFromDate(criteria), getToDate(criteria)));
        } finally {
            close(stmt, connection);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getReportData");
        }
    }

    /**
     * Gets the statistics for users.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getUserStats(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, UploadAndShareReportData uploadAndShareReportData,
            Integer contractId, boolean isContractSearch) throws SQLException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getUserStats");
        }

        // Number of contracts using the service
        getNumberOfContractsUsingService(uploadAndShareReportData, connection, stmt, contractId,
                isContractSearch);

        // Number of users using the service
        getNumberOfUsersUsingService(uploadAndShareReportData, connection, stmt, contractId,
                isContractSearch);

        // Percentage of contracts sharing documents
        getPercentageOfContractsSharingDocuments(uploadAndShareReportData, connection, stmt,
                contractId, isContractSearch);

        // users that accepted Terms of use
        getNumberOfUsersThatAcceptedTermsOfUse(uploadAndShareReportData, connection, stmt,
                contractId, isContractSearch);

        // users that Not accepted Terms of use
        getNumberOfUsersThatNotAcceptedTermsOfUse(uploadAndShareReportData, connection, stmt,
                contractId, isContractSearch);

        // Average no. of documents uploaded per contract
        getAverageNumberOfDocumentsUploadedPerContract(uploadAndShareReportData, connection, stmt,
                contractId, isContractSearch);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getUserStats");
        }

    }

    /**
     * Gets the statistics for documents.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getDocumentStatistics(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, UploadAndShareReportData uploadAndShareReportData,
            Integer contractId, boolean isContractSearch) throws SQLException, SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getDocumentStatistics");
        }

        // Total number of documents renamed, replaced and deleted.
        getNumberDocumentsRenamedReplacedAndDeleted(uploadAndShareReportData, connection, stmt,
                contractId, isContractSearch);

        // Total number of documents changed and replaced
        getTotalNumberDocumentsChangedAndReplaced(uploadAndShareReportData, connection, stmt,
                contractId, isContractSearch);

        // Percentage of documents shared with participants
        getPercentageOfDocumentsSharedWithParticipants(uploadAndShareReportData, connection, stmt,
                contractId, isContractSearch);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDocumentStatistics");
        }

    }

    /**
     * Gets the numbers of notices/documents uploaded by user category as well as by business type.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getSourceOfUploadChart(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, UploadAndShareReportData uploadAndShareReportData,
            Integer contractId, boolean isContractSearch) throws SQLException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getSourceOfUploadGraph");
        }

        List<UploadAndShareReportSourceOfUploadShareVO> sourceOfUploadList = new ArrayList<UploadAndShareReportSourceOfUploadShareVO>();
        String[][] uploadByUserCategorySQL = {
                { PLAN_SPONSOR_NEW_BUSINESS,
                        SQL_SELECT_DOCUMENTS_UPLOADED_BY_PLAN_SPONSOR_NEW_BUSINESS },
                { PLAN_SPONSOR_INFORCE, SQL_SELECT_DOCUMENTS_UPLOADED_BY_PLAN_SPONSOR_INFORCE },
                { INTERMEDIARY_CONTACT, SQL_SELECT_DOCUMENTS_UPLOADED_BY_INTERMEDIARY_CONTACT },
                { TPA, SQL_SELECT_DOCUMENTS_UPLOADED_BY_TPA },
                { TOTAL_CARE, SQL_SELECT_DOCUMENTS_UPLOADED_BY_TOTAL_CARE } };

        for (int i = 0; i < uploadByUserCategorySQL.length; i++) {
            getDocumentsUploadedByUser(uploadAndShareReportData, connection, contractId,
                    isContractSearch, uploadByUserCategorySQL[i][0], uploadByUserCategorySQL[i][1],
                    sourceOfUploadList);
        }

        uploadAndShareReportData.setSourceOfUploadList(sourceOfUploadList);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getSourceOfUploadGraph");
        }
    }

    /**
     * Gets the numbers of notices/documents shared by user category as well as by business type.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getSourceOfShareChart(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, UploadAndShareReportData uploadAndShareReportData,
            Integer contractId, boolean isContractSearch) throws SQLException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getSourceOfShareChart");
        }
        int numDocShared = getDocumentShared(uploadAndShareReportData, connection, contractId,
                isContractSearch);
        List<UploadAndShareReportSourceOfUploadShareVO> sourceOfShareList = new ArrayList<UploadAndShareReportSourceOfUploadShareVO>();
        String[][] sharedByUserCategorySQL = {
                { PLAN_SPONSOR_NEW_BUSINESS,
                        SQL_SELECT_DOCUMENTS_SHARED_BY_PLAN_SPONSOR_NEW_BUSINESS },
                { PLAN_SPONSOR_INFORCE, SQL_SELECT_DOCUMENTS_SHARED_BY_PLAN_SPONSOR_INFORCE },
                { INTERMEDIARY_CONTACT, SQL_SELECT_DOCUMENTS_SHARED_BY_INTERMEDIARY_CONTACT },
                { TPA, SQL_SELECT_DOCUMENTS_SHARED_BY_TPA },
                { TOTAL_CARE, SQL_SELECT_DOCUMENTS_SHARED_BY_TOTAL_CARE } };

        for (int i = 0; i < sharedByUserCategorySQL.length; i++) {
            getDocumentsSharedByUser(uploadAndShareReportData, connection, contractId,
                    isContractSearch, sharedByUserCategorySQL[i][0], sharedByUserCategorySQL[i][1],
                    sourceOfShareList, numDocShared);
        }
        uploadAndShareReportData.setSourceOfShareList(sourceOfShareList);
        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getSourceOfShareChart");
        }
    }

    /**
     * Gets the Top ten Custom notices that are most replaced by Notice Manager.
     * 
     * @param connection
     * @param stmt
     * @param criteria
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getTopTenDocumentNames(Connection connection, PreparedStatement stmt,
            ReportCriteria criteria, UploadAndShareReportData uploadAndShareReportData,
            Integer contractId, boolean isContractSearch) throws SQLException, SystemException {
        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getTopTenDocumentNames");
        }

        List<UploadAndShareReportTopTenDocumentNamesVO> topTenDocNamesList = new ArrayList<UploadAndShareReportTopTenDocumentNamesVO>();
        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_TOP_TEN_DOCUMENTS_NAMES_AND_PERCENTAGES),
                getContractClause(isContractSearch));
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null) {
            int numTimesDocReplaced = 0;
            String docName = null;
            while (rs.next()) {
                docName = rs.getString(1);
                numTimesDocReplaced = rs.getInt(2);
            
            BigDecimal docNamesPercentage = new BigDecimal(0);
            docNamesPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
            if (uploadAndShareReportData.getNumberOfDocumentsUploaded().intValue() > 0) {
                docNamesPercentage = (new BigDecimal(numTimesDocReplaced))
                        .divide(new BigDecimal(uploadAndShareReportData
                                .getNumberOfDocumentsUploaded().intValue()), DIVIDEND_PRECISION,
                                RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                        .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
            }
            if (docName != null) {
                topTenDocNamesList.add(new UploadAndShareReportTopTenDocumentNamesVO(docName,
                        docNamesPercentage));
            }
        }
        }

        uploadAndShareReportData.setTopTenDocumentNamesList(topTenDocNamesList);

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getTopTenDocumentNames");
        }
    }

    /**
     * Returns number of documents Shared.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param contractId
     * @param isContractSearch
     * @return numDocsShared
     * @throws SQLException
     * @throws SystemException 
     */
    private static int getDocumentShared(UploadAndShareReportData uploadAndShareReportData,
            Connection connection, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        int numDocsShared = 0;
        String query = SQL_SELECT_NUMBER_OF_SHARED_DOCUMENTS + getContractClause(isContractSearch);
        PreparedStatement stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numDocsShared = rs.getInt(1);
        }
        return numDocsShared;
    }

    /**
     * Gets Number of contracts using the service.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getNumberOfContractsUsingService(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        // Number of contracts using the service.
        String query = MessageFormat
                .format(replaceSingleQuote(SQL_SELECT_TOTAL_CONTRACTS_USING_SERVICE),
                        getContractClauseWithQualifier(isContractSearch,
                                CONTRACT_NOTICE_DOCUMENT_LOG_QUALIFIER),
                        getContractClauseWithQualifier(isContractSearch,
                                CONTRACT_NOTICE_MAILING_ORDER_QUALIFIER),
                        getContractClauseWithQualifier(isContractSearch,
                                WEBSITE_USER_ACTION_LOG_QUALIFIER));
        ResultSet rs;
        stmt = connection.prepareStatement(query);
        setReportFiltersUnionQuery(uploadAndShareReportData, contractId, isContractSearch, stmt, 3);
        rs = stmt.executeQuery();
        int totalContracts = 0;

        if (rs != null) {
            while (rs.next()) {
                totalContracts = rs.getInt(1);
            }
        }

        uploadAndShareReportData.setNumberOfContractsUsingService(totalContracts);
    }

    /**
     * Gets Number of users using notice manager service.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getNumberOfUsersUsingService(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        String query = SQL_SELECT_NUMBER_OF_USERS_USING_SERVICE
                + getContractClause(isContractSearch);
        int numberOfUsers = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numberOfUsers = rs.getInt(1);
        }

        uploadAndShareReportData.setNumberOfUsersUsingService(numberOfUsers);
    }

    /**
     * Gets percentage of contracts sharing documents.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getPercentageOfContractsSharingDocuments(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        // Contracts that posted at least one custom notice to participant web site
        String query = SQL_SELECT_CONTRACTS_POSTED_PARTICIPANTS_WEBSITE
                + getContractClause(isContractSearch);
        int contractsPostedPW = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            contractsPostedPW = rs.getInt(1);
        }

        uploadAndShareReportData
                .setNumberOfContractsThatPostedToParticipantWebsite(contractsPostedPW);

        // Total number of Notice Manager Contracts that have at least uploaded one custom notice
        // number of documents uploaded.
        rs = null;
        query = SQL_SELECT_NUMBER_OF_CONTRACTS_AND_UPLOADED_DOCUMENTS_NON_DELETED
                + getContractClause(isContractSearch);

        int contractsUploadedDocs = 0;
        int numDocsUploaded = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            contractsUploadedDocs = rs.getInt(1);
            numDocsUploaded = rs.getInt(2);
        }

        uploadAndShareReportData.setNumberOfContractsThatUploadedNotices(contractsUploadedDocs);
        uploadAndShareReportData.setNumberOfDocumentsUploaded(numDocsUploaded);

        // Number of custom notices shared with participants
        query = SQL_SELECT_DOCUMENTS_SHARED_WITH_PARTICIPANTS_WEBSITE_ALL
               + getContractClause(isContractSearch);
        int numContracts = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
        	numContracts = rs.getInt(1);
        }
        
        // Calculate the percentage of contracts sharing documents.
        BigDecimal contractsSharingDocsPercent = new BigDecimal(0);
        contractsSharingDocsPercent.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if ( numContracts > 0) {
            contractsSharingDocsPercent = (new BigDecimal(contractsPostedPW))
                    .divide(new BigDecimal(numContracts),
                            DIVIDEND_PRECISION, RoundingMode.HALF_UP).multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        uploadAndShareReportData.setPercentageOfContractsUsingShare(contractsSharingDocsPercent);

    }

    /**
     * Gets percentage of documents shared with participants.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getPercentageOfDocumentsSharedWithParticipants(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        // Number of custom notices shared with participants
        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_DOCUMENTS_SHARED_WITH_PARTICIPANTS_WEBSITE),
                getContractClause(isContractSearch));
        int numDocumentShared = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numDocumentShared = rs.getInt(1);
        }

        uploadAndShareReportData.setNumberOfDocumentsSharedWithParticipants(numDocumentShared);

        // Number of custom notices shared with participants
        query = SQL_SELECT_DOCUMENTS_SHARED_WITH_PARTICIPANTS_WEBSITE_NON_DELETED
               + getContractClause(isContractSearch);
        int numDocumentSharedNonDeleted = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
        	numDocumentSharedNonDeleted = rs.getInt(1);
        }

        
        // Total number of Notice Manager Contracts that have at least uploaded one custom notice and not deleted.
        // number of documents uploaded.
        rs = null;
        query = SQL_SELECT_NUMBER_OF_CONTRACTS_AND_UPLOADED_DOCUMENTS_NON_DELETED
                + getContractClause(isContractSearch);

        int contractsUploadedDocs = 0;
        int numDocsUploaded = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            contractsUploadedDocs = rs.getInt(1);
            numDocsUploaded = rs.getInt(2);
        }
        
        
        // Percentage of documents shared with participants
        BigDecimal sharedDocsPercent = new BigDecimal(0);
        sharedDocsPercent.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (numDocsUploaded > 0) {
            sharedDocsPercent = (new BigDecimal(numDocumentSharedNonDeleted))
                    .divide(new BigDecimal(numDocsUploaded
                            ), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        uploadAndShareReportData.setPercentageOfDocumentsSharedWithParticipants(sharedDocsPercent);
    }

    /**
     * Gets Number of users that accepted Terms of use
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getNumberOfUsersThatAcceptedTermsOfUse(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_NUMBER_OF_USERS_ACCEPTED_TERMS_OF_USE),
                getContractClauseTermsOfUse(isContractSearch));
        int numUsersAcceptedTerms = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numUsersAcceptedTerms = rs.getInt(1);
        }

        uploadAndShareReportData.setNumberOfUsersAcceptedTermsOfUse(numUsersAcceptedTerms);

    }

    /**
     * Gets Number of users that did not accept Terms of use
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getNumberOfUsersThatNotAcceptedTermsOfUse(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {
        String query = MessageFormat.format(
                replaceSingleQuote(SQL_SELECT_NUMBER_OF_USERS_NOT_ACCEPTED_TERMS_OF_USE),
                getContractClauseTermsOfUse(isContractSearch));
        int numUsersNotAcceptedTerms = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numUsersNotAcceptedTerms = rs.getInt(1);
        }
        uploadAndShareReportData.setNumberOfUsersNotAcceptedTermsOfUse(numUsersNotAcceptedTerms);
    }

    /**
     * Gets average number of documents uploaded per contract
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getAverageNumberOfDocumentsUploadedPerContract(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

        // Total number of custom notices uploaded and replaced
        String query = SQL_SELECT_NUMBER_NOTICES_UPLOADED_AND_REPLACED
                + getContractClause(isContractSearch);
        int numNoticeUploadedReplaced = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numNoticeUploadedReplaced = rs.getInt(1);
        }

        uploadAndShareReportData.setNumberOfNoticesUploadedAndReplaced(numNoticeUploadedReplaced);

        // average number of documents uploaded per contract
        BigDecimal avgNoOfDocsPerContract = new BigDecimal(0);
        avgNoOfDocsPerContract.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        if (uploadAndShareReportData.getNumberOfContractsThatUploadedNotices().intValue() > 0) {

            avgNoOfDocsPerContract = (new BigDecimal(numNoticeUploadedReplaced)).divide(
                    new BigDecimal(uploadAndShareReportData
                            .getNumberOfContractsThatUploadedNotices().intValue()),
                    DIVIDEND_PRECISION, RoundingMode.HALF_UP).setScale(PERCENTAGE_PRECISION,
                    BigDecimal.ROUND_HALF_EVEN);
        }

        uploadAndShareReportData.setAvgNumberOfDocumentsUploadedPerContract(avgNoOfDocsPerContract);
    }

    /**
     * Gets Total number of documents renamed, replaced and deleted.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getNumberDocumentsRenamedReplacedAndDeleted(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {

    	// documents Renamed
        String query = SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED 
        		+ "'" + CHANGE_TYPE_CODE_RENAMED +  "' "
        		+ SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED_DATE_CLAUSE
                + getContractClause(isContractSearch);
        stmt = connection.prepareStatement(query);

        uploadAndShareReportData.setNumberOfDocumentsRenamed(getDocumentsCount(
                uploadAndShareReportData, stmt, contractId, isContractSearch,
                CHANGE_TYPE_CODE_RENAMED));

        // documents Replaced
        query = SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED 
				+ "'" + CHANGE_TYPE_CODE_REPLACED + "' "
				+ SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED_DATE_CLAUSE
		        + getContractClause(isContractSearch);
		stmt = connection.prepareStatement(query);

        uploadAndShareReportData.setNumberOfDocumentsReplaced(getDocumentsCount(
                uploadAndShareReportData, stmt, contractId, isContractSearch,
                CHANGE_TYPE_CODE_REPLACED));

        // documents Deleted
        query = SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED 
				+ "'" + CHANGE_TYPE_CODE_DELETED + "' "
				+ SQL_SELECT_NUMBER_DOCUMENTS_RENAMED_REPLACED_DELETED_DATE_CLAUSE
		        + getContractClause(isContractSearch);
		stmt = connection.prepareStatement(query);

        uploadAndShareReportData.setNumberOfDocumentsDeleted(getDocumentsCount(
                uploadAndShareReportData, stmt, contractId, isContractSearch,
                CHANGE_TYPE_CODE_DELETED));

    }

    /**
     * Returns number of Documents for given change type code.
     * 
     * @param uploadAndShareReportData
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @param changeTypeCode
     * @return number of Documents
     * @throws SQLException
     * @throws SystemException 
     */
    private static Integer getDocumentsCount(UploadAndShareReportData uploadAndShareReportData,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch,
            String changeTypeCode) throws SQLException, SystemException {

        int numDocuments = 0;
        setReportFiltersDocumentCount(uploadAndShareReportData, contractId, isContractSearch, stmt,
                changeTypeCode);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numDocuments = rs.getInt(1);
        }

        return numDocuments;
    }

    /**
     * Gets Total number of documents changed and replaced
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param stmt
     * @param contractId
     * @param isContractSearch
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getTotalNumberDocumentsChangedAndReplaced(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            PreparedStatement stmt, Integer contractId, boolean isContractSearch)
            throws SQLException, SystemException {
        String query = SQL_SELECT_NUMBER_OF_DOCS_CHANGED_AND_REPLACED
                + getContractClause(isContractSearch);
        int numDocsChangedReplaced = 0;
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            numDocsChangedReplaced = rs.getInt(1);
        }

        uploadAndShareReportData.setNumberDocumentsChangedAndReplaced(numDocsChangedReplaced);
    }

    /**
     * Gets Percentage of documents uploaded by user.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param contractId
     * @param isContractSearch
     * @param userQuery
     * @param sourceOfUploadList
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getDocumentsUploadedByUser(
            UploadAndShareReportData uploadAndShareReportData, Connection connection,
            Integer contractId, boolean isContractSearch, String userCategory, String userQuery,
            List<UploadAndShareReportSourceOfUploadShareVO> sourceOfUploadList) throws SQLException, SystemException {
        PreparedStatement stmt;
        int docsUploadedByUser = 0;
        String query = MessageFormat.format(replaceSingleQuote(userQuery),
                getContractClause(isContractSearch));
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            docsUploadedByUser = rs.getInt(1);
        }

        BigDecimal docsUploadedByUserPercentage = new BigDecimal(0);
        docsUploadedByUserPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);

        if (uploadAndShareReportData.getNumberOfDocumentsUploaded().intValue() > 0) {
            docsUploadedByUserPercentage = (new BigDecimal(docsUploadedByUser))
                    .divide(new BigDecimal(uploadAndShareReportData.getNumberOfDocumentsUploaded()
                            .intValue()), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        sourceOfUploadList.add(new UploadAndShareReportSourceOfUploadShareVO(userCategory,
                docsUploadedByUserPercentage));

    }

    /**
     * Gets Percentage of documents shared by user.
     * 
     * @param uploadAndShareReportData
     * @param connection
     * @param contractId
     * @param isContractSearch
     * @param userQuery
     * @param sourceOfSharedList
     * @param numDocShared
     * @throws SQLException
     * @throws SystemException 
     */
    private static void getDocumentsSharedByUser(UploadAndShareReportData uploadAndShareReportData,
            Connection connection, Integer contractId, boolean isContractSearch,
            String userCategory, String userQuery,
            List<UploadAndShareReportSourceOfUploadShareVO> sourceOfSharedList, int numDocShared)
            throws SQLException, SystemException {
        PreparedStatement stmt;
        int docsSharedByUser = 0;
        String query = MessageFormat.format(replaceSingleQuote(userQuery),
                getContractClause(isContractSearch));
        stmt = connection.prepareStatement(query);
        setReportFilters(uploadAndShareReportData, contractId, isContractSearch, stmt);
        ResultSet rs = stmt.executeQuery();
        if (rs != null && rs.next()) {
            docsSharedByUser = rs.getInt(1);
        }

        BigDecimal docsSharedByUserPercentage = new BigDecimal(0);
        docsSharedByUserPercentage.setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);

        if (numDocShared > 0) {
            docsSharedByUserPercentage = (new BigDecimal(docsSharedByUser))
                    .divide(new BigDecimal(numDocShared), DIVIDEND_PRECISION, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(100))
                    .setScale(PERCENTAGE_PRECISION, BigDecimal.ROUND_HALF_EVEN);
        }

        sourceOfSharedList.add(new UploadAndShareReportSourceOfUploadShareVO(userCategory,
                docsSharedByUserPercentage));

    }

    /**
     * Returns the SQL clause if contract filter is active. Note: default table for contract filter
     * is contract notice document log so default qualifier is used.
     * 
     * @param isContractSearch
     * @return SQL clause
     */
    private static String getContractClause(boolean isContractSearch) {
        String clause = "";
        if (isContractSearch) {
            clause = MessageFormat.format(SQL_CONTRACT_FILTER_CLAUSE_WITH_QUALIFIER,
                    CONTRACT_NOTICE_DOCUMENT_LOG_QUALIFIER);
        }
        return clause;
    }

    /**
     * Returns the SQL clause if contract filter is active for users using terms of use.
     * 
     * @param isContractSearch
     * @return SQL clause
     */
    private static String getContractClauseTermsOfUse(boolean isContractSearch) {
        String clause = "";
        if (isContractSearch) {
            clause = SQL_SELECT_FILTER_CONTRACT_ID_NUMBER_OF_USERS_TERMS_OF_USE;
        }
        return clause;
    }

    /**
     * Returns the SQL clause if contract filter is active.
     * 
     * @param isContractSearch
     * @return SQL clause
     */
    public static String getContractClauseWithQualifier(boolean isContractSearch, String qualifier) {
        String clause = "";
        if (isContractSearch) {
            clause = MessageFormat.format(SQL_CONTRACT_FILTER_CLAUSE_WITH_QUALIFIER, qualifier);
        }
        return clause;
    }

    /**
     * Sets Report Search Filters.
     * 
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFilters(UploadAndShareReportData uploadAndShareReportData,
            Integer contractId, boolean isContractSearch, PreparedStatement stmt)
            throws SQLException, SystemException {
    	Date fromDate = null;
   	    Date toDate = null;
   		try {
   			fromDate = (Date) searchDateFormat.parse(uploadAndShareReportData.getFromDate().toString() + " 00:00:00.0");
   			toDate = (Date) searchDateFormat.parse(uploadAndShareReportData.getToDate().toString() + " 23:59:59.66");
   		} catch (ParseException e) {
   			throw new SystemException(
					"Error while parsing fromDate and toDate" + e.getMessage());
   		}
   			 Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
   			 Timestamp toTimestamp = new Timestamp(toDate.getTime());
   	        stmt.setTimestamp(1, fromTimestamp);
   	        stmt.setTimestamp(2, toTimestamp);
        if (isContractSearch) {
            stmt.setInt(3, contractId.intValue());
        }

    }

    /**
     * Sets Report Search Filters for Document Count for given change type code.
     * 
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFiltersDocumentCount(
            UploadAndShareReportData uploadAndShareReportData, Integer contractId,
            boolean isContractSearch, PreparedStatement stmt, String changeTypeCode)
            throws SQLException, SystemException {
        int paramNumber = 1;
        //stmt.setString(paramNumber++, changeTypeCode);
        Date fromDate = null;
   	    Date toDate = null;
   		try {
   			fromDate = (Date) searchDateFormat.parse(uploadAndShareReportData.getFromDate().toString() + " 00:00:00.0");
   			toDate = (Date) searchDateFormat.parse(uploadAndShareReportData.getToDate().toString() + " 23:59:59.66");
   		} catch (ParseException e) {
   			throw new SystemException(
					"Error while parsing fromDate and toDate" + e.getMessage());
   		}
   			 Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
   			 Timestamp toTimestamp = new Timestamp(toDate.getTime());
   	        stmt.setTimestamp(paramNumber++, fromTimestamp);
   	        stmt.setTimestamp(paramNumber++, toTimestamp);

        if (isContractSearch) {
            stmt.setInt(paramNumber++, contractId.intValue());
        }
    }

    /**
     * Sets Report Search Filters for Union Queries.
     * 
     * @param uploadAndShareReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
     * @throws SystemException 
     */
    private static void setReportFiltersUnionQuery(
            UploadAndShareReportData uploadAndShareReportData, Integer contractId,
            boolean isContractSearch, PreparedStatement stmt, int numberOfUnionQueries)
            throws SQLException, SystemException {
        int paramNumber = 1;
        for (int i = 0; i < numberOfUnionQueries; i++) {
       	    Date fromDate = null;
       	    Date toDate = null;
       		try {
       			fromDate = (Date) searchDateFormat.parse(uploadAndShareReportData.getFromDate().toString() + " 00:00:00.0");
       			toDate = (Date) searchDateFormat.parse(uploadAndShareReportData.getToDate().toString() + " 23:59:59.66");
       		} catch (ParseException e) {
       			throw new SystemException(
    					"Error while parsing fromDate and toDate" + e.getMessage());
       		}
       			 Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
       			 Timestamp toTimestamp = new Timestamp(toDate.getTime());
       	        stmt.setTimestamp(paramNumber++, fromTimestamp);
       	        stmt.setTimestamp(paramNumber++, toTimestamp);
            if (isContractSearch) {
                stmt.setInt(paramNumber++, contractId.intValue());
            }
        }

    }

}
