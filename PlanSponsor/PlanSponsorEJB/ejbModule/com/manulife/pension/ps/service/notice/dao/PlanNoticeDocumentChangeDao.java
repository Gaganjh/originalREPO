package com.manulife.pension.ps.service.notice.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.notice.valueobject.EmployeeEligibleVO;
import com.manulife.pension.ps.service.report.notice.valueobject.LookupDescription;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentChangeHistoryVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeDocumentVO;
import com.manulife.pension.ps.service.report.notice.valueobject.PlanNoticeMailingOrderVO;
import com.manulife.pension.ps.service.report.notice.valueobject.UserNoticeManagerAlertVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.exception.ReportServiceException;

/**
 * This method used to get the various  Custom Plan Notice information from Database.
 *  * @author krishta
 *
 */
public class PlanNoticeDocumentChangeDao extends BaseDatabaseDAO {


	private static final String className = PlanNoticeDocumentChangeDao.class.getName();
	private static final Logger logger = Logger.getLogger(PlanNoticeDocumentChangeDao.class);


	private static final String  SQL_SELECT_TERMS_ACCEPTANCE_CODE = 
			"SELECT TERMS_ACCEPTANCE_CODE FROM " +
					PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG WHERE USER_PROFILE_ID = ? ORDER BY CREATED_TS DESC";
	
	private static final String SQL_INSERT_TERMS_OF_USE_LOG = 
			"INSERT INTO "
				+ PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_TERMS_ACCEPTANCE_LOG "
				+ "(USER_PROFILE_ID, CREATED_TS, TERMS_ACCEPTANCE_CODE) "
				+ "values (?, CURRENT TIMESTAMP, ?)";
	
	private static final String SQL_INSERT_CONTRACT_NOTICE_DOCUMENT = 
			"INSERT INTO " + 
				PLAN_SPONSOR_SCHEMA_NAME + 
				"CONTRACT_NOTICE_DOCUMENT(DOCUMENT_ID, CONTRACT_ID,VERSION_NO, DOCUMENT_NAME, DISPLAY_ORDER, DOCUMENT_FILE_NAME, POST_TO_PPT_IND, SOFT_DELETE_IND) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?,?)";
	
	public static final String GET_MAX_DOCUMENT_ID = 
			"VALUES NEXTVAL FOR EZK100.CONTRACT_NOTICE_DOCUMENT_ID";
	private static final String GET_MAX_ORDER_NO = 
			"VALUES NEXTVAL FOR EZK100.CONTRACT_NOTICE_MAILING_ORDER_NO";
	private static final String GET_MAX_ALERT_ID = 
			"VALUES NEXTVAL FOR EZK100.USER_NOTICE_MANAGER_ALERT_ID";
	private static final String GET_MAX_USER_ACTION_ID = 
			"VALUES NEXTVAL FOR EZK100.WEBSITE_USER_ACTION_ID";
	private static final String GET_MAX_ORDER_ID = 
			"SELECT MAX(DISPLAY_ORDER) AS MAX FROM " + 
					PLAN_SPONSOR_SCHEMA_NAME +"CONTRACT_NOTICE_DOCUMENT";
	
	private static final String SQL_SOFT_DELETE_IND_VALUE = 
			"SELECT SOFT_DELETE_IND FROM " + 
					PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT WHERE CONTRACT_ID = ? AND DOCUMENT_ID = ? AND DOCUMENT_FILE_NAME = ?";
	
	private static final String SQL_CHECK_NOTICE_NAME = 
			"SELECT COUNT(*) COUNT FROM " + 
					PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT WHERE SOFT_DELETE_IND='N' AND DOCUMENT_NAME = ? AND CONTRACT_ID=? ";

	private static final String SQL_UPDATE_SOFT_DELETE_IND_CUSTOM_PLAN_NOTICE =
			"UPDATE "
			+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT "
					+ "SET (SOFT_DELETE_IND) =  'Y' " 
					+ "WHERE DOCUMENT_ID = ? ";

	private static final String SQL_INSERT_PLAN_NOTICE_DOCUMENT_CHANGE_LOG = 
			"INSERT INTO "
					+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG " 
					+ " ( DOCUMENT_ID,"
					+ "CONTRACT_ID, "
					+ "VERSION_NO,"
					+ "CREATED_TS, "
					+ "USER_PROFILE_ID, "
					+ "CHANGE_TYPE_CODE, "
					+ "PREV_DOCUMENT_NAME, "
					+ "CHANGED_DOCUMENT_NAME, "
					+ "CHANGED_POST_TO_PPT_IND "
					+ ") VALUES (?, ?,?,CURRENT TIMESTAMP, ?, ?,?,?,?)";

	private static final String SQL_SELECT_PLAN_NOTICE_DOCUMENT_CHANGE_LOCK_INFO = 
			"SELECT DOCUMENT_ID,CONTRACT_ID,CLOG.CREATED_TS,CLOG.USER_PROFILE_ID,UP.FIRST_NAME, UP.LAST_NAME, "+ 
					" CTYPE.LOOKUP_CODE as CHANGE_TYPE_CODE,CTYPE.LOOKUP_DESC as DOCUMENT_CHANGE_TYPE_DESCRIPTION  FROM "
					+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG, "+
					  PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE, "
		            + PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP WHERE DOCUMENT_ID =? AND CONTRACT_ID = ?  "+
					"	AND CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  "+
					"	AND CLOG.CHANGE_TYPE_CODE = ? "+
					"	AND CLOG.CHANGE_TYPE_CODE = CTYPE.LOOKUP_CODE "+
					"	AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID "+
					"	ORDER by CLOG.CREATED_TS DESC ";
	
	private static final String SQL_SELECT_PLAN_NOTICE_DOCUMENT_CHANGE_LOG_INFO = 
			"SELECT CND.DOCUMENT_ID,CND.CONTRACT_ID,CLOG.CREATED_TS,CLOG.USER_PROFILE_ID,UP.FIRST_NAME, UP.LAST_NAME, "+ 
					" CTYPE.LOOKUP_CODE as CHANGE_TYPE_CODE,CTYPE.LOOKUP_DESC as DOCUMENT_CHANGE_TYPE_DESCRIPTION  FROM "
				  + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG, "+
					PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE, "
				  + PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP , "
				  + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT CND " +
					"	WHERE CLOG.DOCUMENT_ID =? AND CND.CONTRACT_ID = ?  "+
					"	AND CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  "+
					"	AND CLOG.CHANGE_TYPE_CODE = CTYPE.LOOKUP_CODE "+
					"	AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID "+
					"	AND CLOG.DOCUMENT_ID = CND.DOCUMENT_ID "+
					"	AND CND.SOFT_DELETE_IND = ? "+
					"	ORDER by CLOG.CREATED_TS DESC ";

	private static final String SQL_SELECT_PLAN_NOTICE_DOCUMENT_COUNT_INFO = 
		"SELECT COUNT(1) FROM "
	+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG, "+
	  PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE, "
	 + PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP , "
	 + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT CND " +
				"	WHERE  CND.CONTRACT_ID = ?  "+
				"	AND CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  "+
				"	AND CLOG.CHANGE_TYPE_CODE = CTYPE.LOOKUP_CODE "+
				"	AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID "+
				"	AND CLOG.DOCUMENT_ID = CND.DOCUMENT_ID "+
				"	AND CND.SOFT_DELETE_IND = 'N' ";
	
	private static final String SQL_DELETE_PLAN_NOTICE_DOCUMENT_LOCK = 
			"DELETE from "
			+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG where DOCUMENT_ID = ? AND CHANGE_TYPE_CODE = 'LOCK'";
	
	 
	 private static String CONTRACT_NOTICE_DOCUMENT_SQL = 
			  "	SELECT DOCUMENT_ID,CONTRACT_ID,VERSION_NO,DOCUMENT_NAME ," +
			  "DISPLAY_ORDER,DOCUMENT_FILE_NAME,POST_TO_PPT_IND," +
			  "SOFT_DELETE_IND FROM "
			 + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT " +
			  "WHERE DOCUMENT_ID = ? "+" ORDER BY VERSION_NO DESC FETCH FIRST ROW ONLY";
	 
	 
	 private static String SELECT_USER_NOTICE_MANAGER_ALERT_SQL = 
			  "	SELECT ALERT_ID,USER_PROFILE_ID ," +
			  "ALERT_FREQUENCY_CODE,ALERT_TIMING_CODE,ALERT_NAME," +
			  "ALERT_URGENCY_IND, START_DATE, CONTRACT_ID FROM "
			 + PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT " +
			  "WHERE CONTRACT_ID = ? AND USER_PROFILE_ID = ? ";
	 
	 private static String SELECT_EXISTING_ALERT_SQL_BY_ALERT_ID = 
		  "	SELECT ALERT_ID,USER_PROFILE_ID ," +
		  "ALERT_FREQUENCY_CODE,ALERT_TIMING_CODE,ALERT_NAME," +
		  "ALERT_URGENCY_IND, START_DATE, CONTRACT_ID FROM "
		+ PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT " +
		  "WHERE ALERT_ID = ? ";
	 
	 private static String INSERT_USER_NOTICE_MANAGER_ALERT_SQL = 
			  "	INSERT INTO  "
			  + PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT (ALERT_ID,USER_PROFILE_ID ," +
			  "ALERT_FREQUENCY_CODE,ALERT_TIMING_CODE,ALERT_NAME," +
			  "ALERT_URGENCY_IND, START_DATE,CONTRACT_ID ) VALUES (?,?,?,?,?,?,?,?)";
	 
	 private static String UPDATE_USER_NOTICE_MANAGER_ALERT_SQL = 
			 	"UPDATE "+ PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT SET ALERT_FREQUENCY_CODE =?, " +
			 			"	ALERT_TIMING_CODE =?,ALERT_NAME=?,ALERT_URGENCY_IND=?,START_DATE = ? " +
			 			"	WHERE CONTRACT_ID= ? AND ALERT_ID = ? " ;
	 
		private static final String SQL_CHECK_ALERT_NAME = 
				"SELECT COUNT(*) COUNT FROM " + 
						PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT WHERE ALERT_NAME = ? AND ALERT_ID <> ? AND CONTRACT_ID= ? " ;
								
		
		private static final String SQL_DELETE_ALERT = 
				"DELETE FROM " + 
						PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT WHERE ALERT_ID = ?";
		
		private static String INSERT_WEBSITE_PAGE_LOG = 
				  "	INSERT INTO  "
				  + PLAN_SPONSOR_SCHEMA_NAME + "WEBSITE_USER_ACTION_LOG (USER_ACTION_ID,CONTRACT_ID,USER_PROFILE_ID,USER_ACTION_NAME,CREATED_TS " +
				  " ) VALUES (?,?,?,?,CURRENT TIMESTAMP)";
		

	 private static final String SQL_UPDATE_NOTICE = "UPDATE "+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT SET  " ;
	 private static final String SQL_UPDATE_NOTICE_WHERE_CLAUSE = 		" WHERE DOCUMENT_ID=? AND CONTRACT_ID=? AND VERSION_NO = ?";
	 
	 
	 private static String USER_NOTICE_MANAGER_ALERT_TIMINGS_SQL = 
			   "SELECT LOOKUP_CODE,LOOKUP_DESC FROM "
			   + PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE WHERE CTYPE.LOOKUP_TYPE_NAME = 'ALERT_TIMING_CODE'  ";
	
	private static String USER_ALERT_FREQUENCY_CODES_SQL = 
			   "SELECT LOOKUP_CODE,LOOKUP_DESC FROM "
			   + PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP  CTYPE WHERE CTYPE.LOOKUP_TYPE_NAME = 'ALERT_FREQUENCY_CODE'  ORDER BY DISPLAY_ORDER ASC ";

	private static String CONTRACT_NOTICE_CHANGE_TYPE_CODES_SQL = 
			   "SELECT LOOKUP_CODE,LOOKUP_DESC FROM "
			  + PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE WHERE CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  ";

	
	private static String CONTRACT_NOTICE_DOCUMENT_HISTORY_SQL_INFO = 
			 "	SELECT DOCUMENT_ID,CONTRACT_ID,CLOG.CREATED_TS,CLOG.USER_PROFILE_ID,UP.FIRST_NAME, UP.LAST_NAME, "+ 
					  "	CTYPE.LOOKUP_CODE as CHANGE_TYPE_CODE,CTYPE.LOOKUP_DESC as DOCUMENT_CHANGE_TYPE_DESCRIPTION  FROM "
					+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG, "+
					  PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE, "
					+ PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP "+
					  " WHERE CONTRACT_ID = ? AND DOCUMENT_ID = ? AND CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  AND CTYPE.LOOKUP_CODE = 'UPLD' AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID ORDER by CLOG.CREATED_TS DESC";
	
	private static String SELECT_PLAN_NOTICE_DELETED_USERNAME = 
			 "	SELECT DOCUMENT_ID,CONTRACT_ID,CLOG.CREATED_TS,CLOG.USER_PROFILE_ID,UP.FIRST_NAME, UP.LAST_NAME, "+ 
					  "	CTYPE.LOOKUP_CODE as CHANGE_TYPE_CODE,CTYPE.LOOKUP_DESC as DOCUMENT_CHANGE_TYPE_DESCRIPTION  FROM "
					+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG, "+
					  PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE, "
					+ PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP "+
					  " WHERE CONTRACT_ID = ? AND DOCUMENT_ID = ? AND CTYPE.LOOKUP_TYPE_NAME = 'DOCUMENT_CHANGE_TYPE_CODE'  AND CTYPE.LOOKUP_CODE = 'DEL' AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID ORDER by CLOG.CREATED_TS DESC";
	
	private static final String SQL_UPDATE_CONTRACT_NOTICE_DOCUMENT_ORDER = 
			"UPDATE " + 
			PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT SET DISPLAY_ORDER = ? " +
			"WHERE  DOCUMENT_ID = ?";
	
	
	 private static String SELECT_NOTICE_MANAGER_ALERT_NOTIFICATION_SQL = 
		  	" with FREQUENCY(MONTH_COUNT, ALERT_ID,CURRENT_DATE_DIFF ) AS 	"+	
		  	"	(SELECT case when ALERT_FREQUENCY_CODE = 'AN' then 12	"+
		  	"				when ALERT_FREQUENCY_CODE = 'SA'  then 6 	"+
		  	"				when ALERT_FREQUENCY_CODE = 'QR'  then 3  	"+
		  	"			 when ALERT_FREQUENCY_CODE = 'MN'  then 1  END 	"+
		  	"			, ALERT_ID,    (case  when DATE(START_DATE)>DATE(CURRENT TIMESTAMP) then MONTHS_BETWEEN(DATE(START_DATE),DATE(CURRENT TIMESTAMP)) "+
		  	"	when DATE(START_DATE)<DATE(CURRENT TIMESTAMP)  then MONTHS_BETWEEN(DATE(CURRENT TIMESTAMP),DATE(START_DATE)) end)as  CURRENT_DATE_DIFF "+
		  	"			FROM 											"+
		    PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT ) 							"+
		   "		SELECT UA.ALERT_ID,USER_PROFILE_ID , ALERT_FREQUENCY_CODE,ALERT_TIMING_CODE,ALERT_NAME, 	"+
		   	"	ALERT_URGENCY_IND, START_DATE, CONTRACT_ID  FROM "+
		    PLAN_SPONSOR_SCHEMA_NAME + "USER_NOTICE_MANAGER_ALERT UA ,FREQUENCY F		"+
		  		"	WHERE UA.ALERT_ID =  F.ALERT_ID  "+
		  		"		and (DATE(current timestamp)=(DATE(START_DATE- ALERT_TIMING_CODE day) ) "+
		  		"		or DATE(current timestamp)=(DATE(START_DATE + (F.MONTH_COUNT )  month- ALERT_TIMING_CODE day) ) "+
		 		"		or DATE(current timestamp)=(DATE(START_DATE+ (F.MONTH_COUNT * CEIL(CURRENT_DATE_DIFF/MONTH_COUNT))  month - ALERT_TIMING_CODE day) )) ";
		 
		 private static String CONTRACT_NOTICE_UPLOADED_DOCUMENT_COUNT_SQL = 
				  "	SELECT COUNT(*) FROM "
				  + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT CN, "
				  + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG " +
				  "		WHERE CN.CONTRACT_ID = ?   AND CLOG.DOCUMENT_ID = CN.DOCUMENT_ID   AND CLOG.CHANGE_TYPE_CODE = 'UPLD'" +
				  "					    AND CN.SOFT_DELETE_IND = 'N'";

	 private static final String SQL_INSERT_CONTRACT_NOTICE_MAILING_ORDER = 
			"INSERT INTO " + 
			PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_MAILING_ORDER (ORDER_NO, CONTRACT_ID, USER_PROFILE_ID, ORDER_STATUS_CODE, " +
			"ORDER_STATUS_DATE, COLOR_PRINT_IND, PAGE_COUNT, PARTICIPANT_COUNT, TOTAL_MAILING_COST_AMOUNT,ORDER_NAME,ADDRESS_FILE_OPTION)" +
			"VALUES (?,?,?,?,CURRENT TIMESTAMP,?,?,?,?,?,?)";

	 private static final String SQL_UPDATE_CONTRACT_NOTICE_MAILING_ORDER_STATUS= 
		 "UPDATE " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_MAILING_ORDER SET " +
		 		"CONTRACT_ID = ?, USER_PROFILE_ID =?, ORDER_STATUS_CODE = ?, " +
				"COLOR_PRINT_IND = ?, PAGE_COUNT = ?, PARTICIPANT_COUNT = ?, TOTAL_MAILING_COST_AMOUNT = ?, " +
				"ORDER_NAME = ? WHERE ORDER_NO = ?";
	
	 private static final String SQL_CUSTOM_DOCUMENT_NAME_COUNT = 
			"SELECT COUNT(*) COUNT FROM " + 
			PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT WHERE DOCUMENT_ID  = ? AND DOCUMENT_NAME = ?"; 
	 
	 private static final String SQL_CUSTOM_DOCUMENT_PRESENCE_COUNT = 
			"SELECT COUNT(*) COUNT FROM " + 
			PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT WHERE DOCUMENT_ID  = ? AND SOFT_DELETE_IND = 'N'"; 
	 
	 private static final String GET_COUNT_OF_DOCUMENTS_IN_CONTRACT = 
		 "SELECT MAX(DISPLAY_ORDER) AS  COUNT FROM " + 
			PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT WHERE SOFT_DELETE_IND = 'N' AND CONTRACT_ID = ?"; 

	 private static String CONTRACT_NOTICE_DOCUMENT_USER_DETAIL_HISTORY_SQL = 
		" SELECT CLOG.USER_PROFILE_ID," +
		" UP.FIRST_NAME, UP.LAST_NAME FROM "
		+ PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT_CHANGE_LOG CLOG," +
		PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_DOCUMENT CND, "+
		PLAN_SPONSOR_SCHEMA_NAME + "USER_PROFILE UP " +
		" WHERE CLOG.CONTRACT_ID = ?  AND CLOG.CREATED_TS <= ? AND CLOG.CREATED_TS >= ? " +
		" AND CLOG.USER_PROFILE_ID = UP.USER_PROFILE_ID order by UP.FIRST_NAME ASC,UP.LAST_NAME ASC";
	 
	 private static String SELECT_PLAN_NOTICE_LOCKED_USERNAME = 
		"SELECT UP.FIRST_NAME,UP.LAST_NAME FROM "
		+ PLAN_SPONSOR_SCHEMA_NAME
		+ "USER_PROFILE UP,"
		+ PLAN_SPONSOR_SCHEMA_NAME
		+ "COMPONENT_LOCK_EVENT CL WHERE "
		+ "CL.LOCK_USER_PROFILE_ID = UP.USER_PROFILE_ID AND CL.COMPONENT_KEY = ? "
		+ "AND CL.LOCK_USER_PROFILE_ID=? "; 
		
	 private static String CONTRACT_NOTICE_MAILING_ORDER_SQL = 
		   "SELECT ORDER_NO,CONTRACT_ID,USER_PROFILE_ID,CTYPE.LOOKUP_CODE as ORDER_STATUS_CODE,CTYPE.LOOKUP_DESC as ORDER_STATUS_DESSCRIPTION, ORDER_STATUS_DATE,COLOR_PRINT_IND,COLOR_PRINT_IND," +
		   " PAGE_COUNT, PARTICIPANT_COUNT,TOTAL_MAILING_COST_AMOUNT, ORDER_NAME,VIP_ORDER_IND , MERRILL_ORDER_NO FROM "
		   + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_MAILING_ORDER CNMO ,"
		   + PLAN_SPONSOR_SCHEMA_NAME + "LOOKUP CTYPE  " +
		   "WHERE CTYPE.LOOKUP_TYPE_NAME = 'CONTRACT_NOTICE_ORDER_STATUS_CODE' AND CNMO.ORDER_STATUS_CODE = CTYPE.LOOKUP_CODE AND ORDER_NO = ?";
	 
	 private static final String SQL_UPDATE_CONTRACT_NOTICE_MAILING_ORDER= 
			"UPDATE  " + 
			PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_NOTICE_MAILING_ORDER SET  ORDER_STATUS_CODE = ?  " ;
	 
	 private static final String ORDER_STATUS_WHERE_CLAUSE_SQL = " WHERE ORDER_NO = ? ";
		
		private static final String ORDER_STATUS_DATE_COLUMN_UPDATE = ", ORDER_STATUS_DATE =? " ;
		private static final String COLOR_PRINT_IND_COLUMN_UPDATE = ", COLOR_PRINT_IND = ? ";
		private static final String PAGE_COUNT_COLUMN_UPDATE = ", PAGE_COUNT = ?  ";
		private static final String PARTICIPANT_COUNT_COLUMN_UPDATE = ", PARTICIPANT_COUNT = ? ";
		private static final String TOTAL_MAILING_COST_AMOUNT_COLUMN_UPDATE = ", TOTAL_MAILING_COST_AMOUNT = ?  ";
		private static final String VIP_ORDER_IND_COLUMN_UPDATE = ", VIP_ORDER_IND = ?  ";
		private static final String ORDER_SEALED_IND_COLUMN_UPDATE = ", ORDER_SEALED_IND = ?  ";
		private static final String ORDER_STAPLED_IND_COLUMN_UPDATE = ", ORDER_STAPLED_IND = ?  ";
		private static final String BULK_ORDER_IND_COLUMN_UPDATE = ", BULK_ORDER_IND = ?  ";
		private static final String LARGE_ENVELOPE_IND_COLUMN_UPDATE = ", LARGE_ENVELOPE_IND = ?  ";
		private static final String MERRILL_ORDER_NO_COLUMN_UPDATE = ", MERRILL_ORDER_NO = ?  ";
		
		private static final String SQL_SELECT_ELIGIBLE_EMPLOYEE_DETAILS = 
				"SELECT     FIRST_NAME AS FNAME, "
				+ " LAST_NAME AS LNAME, "
				+ " ADDR_LINE1 AS ADDR1, "
				+ " ADDR_LINE2 AS ADDR2, "
				+ " CITY_NAME AS CITY, "
				+ " STATE_CODE AS STATE, "
				+ " ZIP_CODE AS ZIP, "
				+ " COUNTRY_NAME AS COUNTRY , "
				+ " EMPLOYMENT_STATUS_CODE, "
				+ " PLAN_ELIGIBLE_IND, "
				+ " ELIGIBILITY_DATE, "
				+ " PARTICIPANT_IND, "
				+ " TOTAL_BALANCE "
				+ " FROM ( SELECT ROWNUMBER() OVER (ORDER BY last_name asc, first_name asc) AS ROW_NUM, " 
				+ " FIRST_NAME, "
				+ " LAST_NAME, " 
				+ " ADDR_LINE1, " 
				+ " ADDR_LINE2, " 
				+ " CITY_NAME, " 
				+ " STATE_CODE, " 
				+ " ZIP_CODE, "
				+ " COUNTRY_NAME, " 
				+ " EMPLOYMENT_STATUS_CODE, " 
				+ " PLAN_ELIGIBLE_IND, "
				+ " ELIGIBILITY_DATE, " 
				+ " PARTICIPANT_IND, "
				+ " TOTAL_BALANCE, "
				+ " ELIGIBLE_PLAN_ENTRY_DATE "
				+ " FROM   (SELECT FIRST_NAME, " 
				+ " LAST_NAME, " 
				+ " ADDR_LINE1, " 
				+ " ADDR_LINE2, " 
				+ " CITY_NAME, " 
				+ " STATE_CODE, "
				+ " ZIP_CODE, " 
				+ " H.COUNTRY_NAME AS COUNTRY_NAME, "
				+ " COALESCE(ES_VAL, E.EMPLOYMENT_STATUS_CODE) AS EMPLOYMENT_STATUS_CODE, "
				+ " PLAN_ELIGIBLE_IND AS PLAN_ELIGIBLE_IND, " 
				+ " E.ELIGIBILITY_DATE AS ELIGIBILITY_DATE, "
				+ " EZK100.PARTICIPANT_IND(P.PARTICIPANT_STATUS_CODE, "
				+ " SUM(PB.TOTAL_BALANCE_AMT), "
				+ " CHAR(COALESCE(ES_VAL, E.EMPLOYMENT_STATUS_CODE))) PARTICIPANT_IND, "
				+ " SUM(PB.TOTAL_BALANCE_AMT) AS TOTAL_BALANCE, " 
				+ " EE.ELIGIBLE_PLAN_ENTRY_DATE "
				+ " FROM   PSW100.EMPLOYEE_CONTRACT E " 
				+ " LEFT OUTER JOIN PSW100.PARTICIPANT_CONTRACT P "
				+ " ON E.PROFILE_ID = P.PROFILE_ID " 
				+ " AND P.CONTRACT_ID = E.CONTRACT_ID "
				+ " LEFT OUTER JOIN (SELECT PARTICIPANT_ID, " 
				+ " CONTRACT_ID, " 
				+ " TOTAL_BALANCE_AMT "
				+ " FROM   PSW100.PARTICIPANT_CURRENT_BAL_LSA PB, " 
				+ " PSW100.MONEY_TYPE MT "
				+ " WHERE  PB.MONEY_TYPE_ID = MT.MONEY_TYPE_CODE " 
				+ " AND MONEY_TYPE_GROUP <> 'UM') AS PB "
				+ " ON P.CONTRACT_ID = PB.CONTRACT_ID " 
				+ " AND P.PARTICIPANT_ID = PB.PARTICIPANT_ID "
				+ " LEFT OUTER JOIN PSW100.EMPLOYEE_ADDRESS_HISTORY H " 
				+ " ON E.PROFILE_ID = H.PROFILE_ID "
				+ " AND E.CONTRACT_ID = H.CONTRACT_ID " 
				+ " AND DATE(EMPLOYEE_ADDR_END_TS) = DATE('9999-12-31') "
				+ " LEFT OUTER JOIN (SELECT CONTRACT_ID, " 
				+ " PROFILE_ID, " 
				+ " FULLY_VESTED_IND AS FVI_VAL, "
				+ " FULLY_VESTED_IND_EFF_DT        AS FVI_DTE, " 
				+ " PREVIOUS_YRS_OF_SERVICE        AS VYOS_VAL, "
				+ " PREVIOUS_YRS_OF_SERVICE_EFF_DT AS VYOS_DTE, " 
				+ " EMPLOYMENT_STATUS_CODE         AS ES_VAL, "
				+ " EMPLOYMENT_STATUS_EFF_DATE     AS ES_DTE, " 
				+ " PLAN_YTD_HRS_WORKED            AS PYHW_VAL, "
				+ " PLAN_YTD_HRS_WORKED_EFF_DATE   AS PYHW_DTE " 
				+ " FROM   PSW100.EMPLOYEE_VESTING "
				+ " WHERE  CONTRACT_ID = "
				+ " ?) AS EV " 
				+ " ON E.CONTRACT_ID = EV.CONTRACT_ID "
				+ " AND E.PROFILE_ID = EV.PROFILE_ID " 
				+ " LEFT OUTER JOIN PSW100.EMPLOYEE_PLAN_ENTRY_ELIGIBILITY EE "
				+ " ON EE.PROFILE_ID = E.PROFILE_ID " 
				+ " AND EE.CONTRACT_ID = E.CONTRACT_ID "
				+ " AND EE.MONEY_TYPE_ID = 'EEDEF' " 
				+ " WHERE  E.CONTRACT_ID = ? " 
				+ " AND E.CONFIRMED_IND = 'Y' "
				+ " AND ( P.CONTRACT_ID IS NULL " 
				+ " OR P.CONTRACT_ID = ? ) " 
				+ " AND ( P.CONTRACT_ID IS NULL  "
				+ "		OR P.PARTICIPANT_TYPE_CODE <> 'PW' ) "
				+ " AND ( PB.CONTRACT_ID IS NULL "
				+ " OR PB.CONTRACT_ID = ? ) " 
				+ " GROUP  BY FIRST_NAME, "
				+ " LAST_NAME, " 
				+ " ADDR_LINE1, " 
				+ " ADDR_LINE2, " 
				+ " CITY_NAME, " 
				+ " STATE_CODE, " 
				+ " ZIP_CODE, "
				+ " H.COUNTRY_NAME, " 
				+ " COALESCE(ES_VAL, E.EMPLOYMENT_STATUS_CODE), "
				+ " COALESCE(ES_DTE, E.EMPLOYMENT_STATUS_EFF_DATE), " 
				+ " PLAN_ELIGIBLE_IND, " 
				+ " E.ELIGIBILITY_DATE, "
				+ " E.PROFILE_ID, " 
				+ " E.CONTRACT_ID, " 
				+ " P.PARTICIPANT_STATUS_CODE, " 
				+ " PROVIDED_ELIGIBILITY_DATE_IND, "
				+ " ELIGIBLE_PLAN_ENTRY_DATE "
				+ " ) AS INITIAL_ROWS " 
				+ "  WHERE "
				+ " ((PARTICIPANT_IND=1   and (EMPLOYMENT_STATUS_CODE IS NULL "
				+ " OR EMPLOYMENT_STATUS_CODE = 'A')) "
				+ " OR (PARTICIPANT_IND=1   and (EMPLOYMENT_STATUS_CODE NOT IN ('A') "
				+ " AND TOTAL_BALANCE > 0) ) "
				+ " OR (PARTICIPANT_IND<>1  and (EMPLOYMENT_STATUS_CODE IS NULL "
				+ " OR EMPLOYMENT_STATUS_CODE = 'A') " 
				+ " and (PLAN_ELIGIBLE_IND !='N' AND ELIGIBILITY_DATE <= CURRENT DATE )) )  )";
		
	/**
	 * This method will insert record into DB 
	 * 
	 * 
	 * @param planNoticeDocumentDetail
	 *            PlanNoticeDocumentVO       
	 * @return Integer
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static Integer addCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws SystemException {

		logger.debug("Executing insert SQL statement: " + SQL_INSERT_CONTRACT_NOTICE_DOCUMENT);
      
		Connection conn = null;
		CallableStatement stmt = null;
		
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SQL_INSERT_CONTRACT_NOTICE_DOCUMENT);
			
		    stmt.setInt(1, planNoticeDocumentDetail.getDocumentId());			
			stmt.setInt(2, planNoticeDocumentDetail.getContractId());
			stmt.setInt(3, planNoticeDocumentDetail.getVersionNumber());
			stmt.setString(4, planNoticeDocumentDetail.getDocumentName());
			stmt.setInt(5, getMaxDocumentOrderForContract(planNoticeDocumentDetail.getContractId(), conn)+1);
			stmt.setString(6, planNoticeDocumentDetail.getDocumentFileName());
			stmt.setString(7, planNoticeDocumentDetail.getPostToPptInd());
			stmt.setString(8, "N");			
			stmt.execute();
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during " + SQL_INSERT_CONTRACT_NOTICE_DOCUMENT);
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed insert SQL statement: " + SQL_INSERT_CONTRACT_NOTICE_DOCUMENT);

		return 1;
	}

	/**
	 * This method will return next sequence of the DOCUMENT_ID 
	 *          
	 * @return Integer
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static int getNextSequenceId(String query) throws SystemException {

		logger.debug("Executing insert SQL statement: " + query);
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet resultSet = null;
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(query);

			stmt.execute();			
			resultSet = stmt.getResultSet();

			if (resultSet != null && resultSet.next()) {
				return resultSet.getInt("1");			
			}else{
				return 1;				
			}

		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during " + GET_MAX_DOCUMENT_ID);
		} finally {
			close(stmt, conn);
		}
	}

	/**
	 * This method will return next document order 
	 *          
	 * @return Integer
	 * 
	 */
	public static int getMaxDocumentOrder() throws SystemException {

		logger.debug("Executing insert SQL statement: " + GET_MAX_ORDER_ID);
		return getNextSequenceId(GET_MAX_ORDER_NO);
	}

	/**
	 * This method will return next document order 
	 *          
	 * @return Integer
	 * 
	 */
	public static int getMaxDocumentOrderForContract(Integer contract, Connection conn) throws SystemException {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer count = 0;
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_COUNT_OF_DOCUMENTS_IN_CONTRACT);
			stmt.setInt(1, contract);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count =rs.getInt("COUNT");
			}
			
			}catch (SQLException e) {
				throw new SystemException(e, 
						"Problem occurred during getMaxDocumentOrderForContract sql call. ");
			} finally {
				close(stmt, conn);
			}
			return count;
		
	}


/**
 * This method will return whether the document name is existing or not
 * 
 * @param fileName
 * @param ContractId
 * @return
 * @throws SystemException
 */
	@SuppressWarnings("deprecation")
	public static boolean isNoticeDocumentNameExists(String fileName,Integer ContractId) throws SystemException {

		logger.debug("Executing insert SQL statement: " + SQL_CHECK_NOTICE_NAME);
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SQL_CHECK_NOTICE_NAME);			
			stmt.setString(1, fileName);
			stmt.setInt(2, ContractId);
            rs = stmt.executeQuery();
			if (rs.next()) {
				count =rs.getInt("COUNT");
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during " + SQL_CHECK_NOTICE_NAME);
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed insert SQL statement: " + SQL_CHECK_NOTICE_NAME);
		if(count > 0){
			return true;        	
		}else{
			return false;
		}
	}


	/**
	 * Edit the plan notice document and its log table
	 * 
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SystemException
	 */
	public static boolean editCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws SystemException {
    	
		Connection conn = null;
		PreparedStatement stmt = null;
		boolean updateCount = false; 

		try {
			StringBuffer query = new StringBuffer(SQL_UPDATE_NOTICE);
			int pCount = 0;
			StringBuffer updateQuery =  new StringBuffer();
			String documentFileName = planNoticeDocumentDetail.getDocumentFileName();
			if(StringUtils.isNotBlank(documentFileName)){
				documentFileName = documentFileName.trim();
				updateQuery.append(" DOCUMENT_FILE_NAME = ? ");
			}
			
			String documentName = planNoticeDocumentDetail.getDocumentName();
			if(StringUtils.isNotBlank(documentName)){
				documentName = documentName.trim();
				if(StringUtils.isNotBlank(updateQuery.toString())){
					updateQuery.append(" ,  ");
				}
				updateQuery.append(" DOCUMENT_NAME = ? ");
			}
			
			String postPptInd = planNoticeDocumentDetail.getPostToPptInd();
			if(StringUtils.isNotBlank(postPptInd)){
				postPptInd = postPptInd.trim();
				if(StringUtils.isNotBlank(updateQuery.toString())){
					updateQuery.append(" ,  ");
				}
				updateQuery.append(" POST_TO_PPT_IND = ? ");
			}
			updateQuery.append(SQL_UPDATE_NOTICE_WHERE_CLAUSE);
			query.append(updateQuery.toString());
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(query.toString());
            if(StringUtils.isNotBlank(documentFileName)){
            	stmt.setString(++pCount, documentFileName);
        	}
            if(StringUtils.isNotBlank(documentName)){
            	stmt.setString(++pCount, documentName);
        	}
        	if(StringUtils.isNotBlank(postPptInd)){
            	stmt.setString(++pCount, postPptInd);
        	}
	        stmt.setInt(++pCount, planNoticeDocumentDetail.getDocumentId());
			stmt.setInt(++pCount, planNoticeDocumentDetail.getContractId());
			stmt.setInt(++pCount, planNoticeDocumentDetail.getVersionNumber());
			stmt.execute();
			updateCount = insertCustomPlanNoticeDocumentLogs(planNoticeDocumentDetail.getPlanNoticeDocumentChangeDetail()); 
		} catch (SQLException e) {
			logger.error("Edit the plan notice document and its log table", e);
			throw new SystemException(e, 
					"Problem occurred during editCustomPlanNoticeDocument sql call. ");
		} finally {
			close(stmt, conn);
		}

        return updateCount;
    
	}

	public static void applyCustomPlanNoticeDocumentSortOrder(List<PlanNoticeDocumentVO> planNoticeDocumentDetails) throws SystemException {

	}

	/**
	 * Update the user acceptance of Term of use
	 * 
	 * @param ProfileId
	 * @param acceptanceInd
	 * @return
	 * @throws SystemException
	 */
	public static boolean updateUsersTermConditionAcceptance(BigDecimal ProfileId, boolean acceptanceInd) throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> updateUsersTermConditionAcceptance");

		boolean flag = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		String userAcceptInd = "N";
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_INSERT_TERMS_OF_USE_LOG);
			stmt.setBigDecimal(1,ProfileId);
			if(acceptanceInd){
				userAcceptInd = "Y";
			}
			stmt.setString(2, userAcceptInd);
			Integer count  = stmt.executeUpdate();
			if(count>0){
				flag = true;
			}
		}catch (SQLException se) {
			logger.error("Update the user acceptance of Term of use", se);
			throw new SystemException(se, 
					"Problem occurred during updateUsersTermConditionAcceptance sql call. ");
		} finally {
			close(stmt, conn);
		}
		return flag;
	}

	/**
	 * Delete the plan Notice document by updating the soft delete indicator
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SystemException
	 */
	public static boolean deleteCustomPlanNoticeDocument(PlanNoticeDocumentVO planNoticeDocumentDetail) throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> insertCustomPlanNoticeDocumentLogs");
		boolean flag = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		PlanNoticeDocumentVO planVO = null;
		
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_UPDATE_SOFT_DELETE_IND_CUSTOM_PLAN_NOTICE);
			stmt.setInt(1,planNoticeDocumentDetail.getDocumentId());
			Integer softDeletecount  = stmt.executeUpdate();
			stmt.close();
			planVO	= getCustomPlanNoticeDocumentInfo(planNoticeDocumentDetail.getDocumentId());
			stmt = conn
					.prepareStatement(SQL_INSERT_PLAN_NOTICE_DOCUMENT_CHANGE_LOG);
			stmt.setInt(1,planNoticeDocumentDetail.getDocumentId());
			stmt.setInt(2,planNoticeDocumentDetail.getContractId());
			stmt.setInt(3,planVO.getVersionNumber());
			stmt.setBigDecimal(4,planNoticeDocumentDetail.getProfileId());
			stmt.setString(5,"DEL");
			String previousDocumentName = StringUtils.EMPTY;
			stmt.setString(6,previousDocumentName);
			stmt.setString(7,planVO.getDocumentName());
			stmt.setString(8,planVO.getPostToPptInd());
			Integer changeTypecount  = stmt.executeUpdate();
			if(softDeletecount>0 && changeTypecount>0){
				flag = true;
			}
		}catch (SQLException se) {
			logger.error("Problem occurred while executing the update sql in deleteCustomPlanNoticeDocument method", se);
			throw new SystemException(se, 
					"Problem occurred during deleteCustomPlanNoticeDocument method call. ");
		} finally {
			close(stmt, conn);
		}
		return flag;

	}
	/**
	 * This method is used to check the  PlanNoticeDocumentSoftDeleteIndicator value
	 * @param documentId
	 * @return
	 * @throws SystemException
	 * @throws SQLException
	 */
	public static PlanNoticeDocumentVO checkPlanNoticeDocumentSoftDeleteIndicator(PlanNoticeDocumentVO planNoticeDocumentVO) throws SystemException, SQLException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> insertCustomPlanNoticeDocumentLogs");

		Connection conn = null;
		PreparedStatement stmt = null;
		PlanNoticeDocumentVO planNoticeDocument = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_SELECT_PLAN_NOTICE_DOCUMENT_CHANGE_LOG_INFO);
			stmt.setInt(1,planNoticeDocumentVO.getDocumentId());
			stmt.setInt(2,planNoticeDocumentVO.getContractId());
			stmt.setString(3, "Y");
			ResultSet rs = stmt.executeQuery();
			if (rs!= null && rs.next()){
				planNoticeDocument = new PlanNoticeDocumentVO();
				planNoticeDocument.setContractId(rs.getInt("CONTRACT_ID"));
				planNoticeDocument.setDocumentId(rs.getInt("DOCUMENT_ID"));
				planNoticeDocument.setDocumentLocked(true);
				PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistory =  new PlanNoticeDocumentChangeHistoryVO();
				planNoticeDocumentChangeHistory.setChangedProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
				planNoticeDocumentChangeHistory.setPlanNoticeDocumentChangeTypeDetail(new LookupDescription(rs.getString("CHANGE_TYPE_CODE"),rs.getString("DOCUMENT_CHANGE_TYPE_DESCRIPTION")));
				planNoticeDocumentChangeHistory.setChangedUserName(rs.getString("FIRST_NAME")+", "+rs.getString("LAST_NAME"));
				planNoticeDocumentChangeHistory.setChangedDate(rs.getTimestamp("CREATED_TS"));
				planNoticeDocument.setPlanNoticeDocumentChangeDetail(planNoticeDocumentChangeHistory);
			}


		}catch (SQLException se) {
			logger.error("Retrieve the plan notice document plan info", se);
			throw new SystemException(se, 
					"Problem occurred during retreivePlanNoticeLockInfo sql call. ");
		} finally {
			close(stmt, conn);
		}
		return planNoticeDocument;
	}
	
	/**
	 * This method is used to check the  Custom notice document Count value
	 * @param documentId
	 * @return
	 * @throws SystemException
	 * @throws SQLException
	 */
	public static int checkCustomNoticeDocumentCount(Integer contractId) throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> checkCustomNoticeDocumentCount");

		Connection conn = null;
		PreparedStatement stmt = null;
		int customNoticeCount = 0;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_SELECT_PLAN_NOTICE_DOCUMENT_COUNT_INFO);
			stmt.setInt(1,contractId);
			ResultSet rs = stmt.executeQuery();
			if (rs!= null && rs.next()){
				customNoticeCount = rs.getInt(1);
			}


		}catch (SQLException se) {
			logger.error("Retrieve the Custom notice Document Count", se);
			throw new SystemException(se, 
					"Problem occurred during ustom notice Document Count sql call. ");
		} finally {
			close(stmt, conn);
		}
		return customNoticeCount;
	}
	/**
	 * Insert the plan notice document change history info
	 * @param planNoticeDocumentChangeHistoryVO
	 * @return
	 * @throws SystemException
	 */
	public static boolean insertCustomPlanNoticeDocumentLogs(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> insertCustomPlanNoticeDocumentLogs");
       
		boolean flag = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt2 = conn.prepareStatement(SQL_INSERT_PLAN_NOTICE_DOCUMENT_CHANGE_LOG);
			stmt2.setInt(1,planNoticeDocumentChangeHistoryVO.getDocumentId());
			stmt2.setInt(2,planNoticeDocumentChangeHistoryVO.getContractId());
			stmt2.setInt(3, planNoticeDocumentChangeHistoryVO.getVersionNumber());
			stmt2.setBigDecimal(4, planNoticeDocumentChangeHistoryVO.getChangedProfileId());
			stmt2.setString(5, planNoticeDocumentChangeHistoryVO.getPlanNoticeDocumentChangeTypeDetail().getLookupCode());
			String previousDocumentName = planNoticeDocumentChangeHistoryVO.getPreviousdocumentName();
			if(StringUtils.isBlank(previousDocumentName)){
				previousDocumentName = StringUtils.EMPTY;
			}
			
			stmt2.setString(6, previousDocumentName);
			String documentName = planNoticeDocumentChangeHistoryVO.getDocumentName();
			if(StringUtils.isBlank(documentName)){
				documentName = StringUtils.EMPTY;
			}
			stmt2.setString(7, documentName);
			stmt2.setString(8, planNoticeDocumentChangeHistoryVO.getChangedPPT());
			
			Integer count  = stmt2.executeUpdate();
			if(count>0){
				flag = true;
			}
		}catch (SQLException se) {
			logger.error("Insert the Plan notice document change history info", se);
			throw new SystemException(se, 
					"Problem occurred during insertCustomPlanNoticeDocumentLogs sql call. ");
		} finally {
			close(stmt, conn);
		}
		return flag;
	}

	/**
	 * Retrieve the plan notice document plan info
	 * @param planNoticeDocumentChangeHistoryVO
	 * @return
	 * @throws SystemException
	 */
	public static PlanNoticeDocumentVO retreivePlanNoticeLockInfo(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> insertCustomPlanNoticeDocumentLogs");

		Connection conn = null;
		PreparedStatement stmt = null;
		PlanNoticeDocumentVO planNoticeDocument = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_SELECT_PLAN_NOTICE_DOCUMENT_CHANGE_LOCK_INFO);
			stmt.setInt(1,planNoticeDocumentChangeHistoryVO.getDocumentId());
			stmt.setInt(2,planNoticeDocumentChangeHistoryVO.getContractId());

			stmt.setString(3, planNoticeDocumentChangeHistoryVO.getPlanNoticeDocumentChangeTypeDetail().getLookupCode());
			ResultSet rs = stmt.executeQuery();
			if (rs!= null && rs.next()){
				planNoticeDocument = new PlanNoticeDocumentVO();
				planNoticeDocument.setContractId(rs.getInt("CONTRACT_ID"));
				planNoticeDocument.setDocumentId(rs.getInt("DOCUMENT_ID"));
				planNoticeDocument.setDocumentLocked(true);
				PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistory =  new PlanNoticeDocumentChangeHistoryVO();
				planNoticeDocumentChangeHistory.setChangedProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
				planNoticeDocumentChangeHistory.setPlanNoticeDocumentChangeTypeDetail(new LookupDescription(rs.getString("CHANGE_TYPE_CODE"),rs.getString("DOCUMENT_CHANGE_TYPE_DESCRIPTION")));
				planNoticeDocumentChangeHistory.setChangedUserName(rs.getString("FIRST_NAME")+", "+rs.getString("LAST_NAME"));
				planNoticeDocumentChangeHistory.setChangedDate(rs.getTimestamp("CREATED_TS"));
				planNoticeDocument.setPlanNoticeDocumentChangeDetail(planNoticeDocumentChangeHistory);
			}


		}catch (SQLException se) {
			logger.error("Retrieve the plan notice document plan info", se);
			throw new SystemException(se, 
					"Problem occurred during retreivePlanNoticeLockInfo sql call. ");
		} finally {
			close(stmt, conn);
		}
		return planNoticeDocument;
	}

	/**
	 * Release the plan notice document Lock
	 * @param planNoticeDocumentChangeHistoryVO
	 * @return
	 * @throws SystemException
	 */
	public static boolean releaseCustomPlanNoticeDocumentLock(PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistoryVO) throws SystemException {
		if (logger.isDebugEnabled())
			logger.debug("entry -> releaseCustomPlanNoticeDocumentLock");

		boolean flag = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_DELETE_PLAN_NOTICE_DOCUMENT_LOCK);
			stmt.setInt(1,planNoticeDocumentChangeHistoryVO.getDocumentId());
			Integer count  = stmt.executeUpdate();
			if(count>0){
				flag = true;
			}
		}catch (SQLException se) {
			logger.error("Release the Plan notice document Lock ", se);
			throw new SystemException(se, 
					"Problem occurred during releaseCustomPlanNoticeDocumentLock sql call. ");
		} finally {
			close(stmt, conn);
		}
		return flag;
	}
	
	/**
	 * Retrieve the plan notice document info based  on the document id
	 * @param documentId
	 * @return
	 * @throws SystemException
	 */
	public static PlanNoticeDocumentVO getCustomPlanNoticeDocumentInfo(Integer documentId) throws SystemException {
        
		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(CONTRACT_NOTICE_DOCUMENT_SQL);
			stmt.setInt(1, documentId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				planNoticeDocumentVO.setDocumentName(rs.getString("DOCUMENT_NAME"));
				planNoticeDocumentVO.setDocumentId(rs.getInt("DOCUMENT_ID"));
				planNoticeDocumentVO.setVersionNumber(rs.getInt("VERSION_NO"));
				planNoticeDocumentVO.setDocumentDisplayOrder(rs.getInt("DISPLAY_ORDER"));
				planNoticeDocumentVO.setDocumentFileName(rs.getString("DOCUMENT_FILE_NAME"));
				planNoticeDocumentVO.setDocumentLocked(false);
				planNoticeDocumentVO.setPostToPptInd(rs.getString("POST_TO_PPT_IND"));
        		planNoticeDocumentVO.setSoftDelIndicator(rs.getString("SOFT_DELETE_IND"));
			}

		} catch (SQLException e) {
			logger.error("Retrieve the plan notice document info based  on the document id", e);
			throw new SystemException(e, 
					"Problem occurred during getCustomPlanNoticeDocumentInfo sql call. ");
		} finally {
			close(stmt, conn);
		}

		return planNoticeDocumentVO;

	}
	
	/**
	 * Populates the Existing Alerts for the user
	 * @param profileId
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public static List<UserNoticeManagerAlertVO> getUserNoticePreferences(BigDecimal profileId, Integer contractId) 
			throws SystemException {
		List<UserNoticeManagerAlertVO> userNoticePreference = new ArrayList<UserNoticeManagerAlertVO>();
		UserNoticeManagerAlertVO userNoticeManagerAlert = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(SELECT_USER_NOTICE_MANAGER_ALERT_SQL);
			stmt.setInt(1, contractId);
			stmt.setBigDecimal(2, profileId);
			rs = stmt.executeQuery();
			if (rs != null ){
				while (rs.next()){
				userNoticeManagerAlert =  new UserNoticeManagerAlertVO();
				userNoticeManagerAlert.setAlertFrequenceCode(rs.getString("ALERT_FREQUENCY_CODE"));
				userNoticeManagerAlert.setAlertId(rs.getInt("ALERT_ID"));
				userNoticeManagerAlert.setAlertName(rs.getString("ALERT_NAME"));
				userNoticeManagerAlert.setAlertTimingCode(String.valueOf(rs.getInt("ALERT_TIMING_CODE")));
				userNoticeManagerAlert.setAlertUrgencyName(rs.getString("ALERT_URGENCY_IND"));
				userNoticeManagerAlert.setContractId(rs.getInt("CONTRACT_ID"));
				userNoticeManagerAlert.setProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
				userNoticeManagerAlert.setStartDate(rs.getDate("START_DATE"));
				userNoticePreference.add(userNoticeManagerAlert);
				
				}
			}

			
		} catch (SQLException e) {
			logger.error("Retrieve the User Notice alert info based  on the contract and user profile id", e);
			throw new SystemException(e, 
					"Problem occurred during getUserNoticePreferences sql call. ");
		} finally {
			close(stmt, conn);
		}

		return userNoticePreference;
	}

	/**
	 * Saves the alert for the particular user
	 * @param profileId
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public static int addUserNoticePreferences(List<UserNoticeManagerAlertVO> userNoticePreference) 
			throws SystemException { 
		Integer userNoticeAddCount = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			for(UserNoticeManagerAlertVO userNoticeManagerAlert:userNoticePreference){
				conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
				if(userNoticeManagerAlert.getAlertId()==0){
					
					stmt = conn.prepareCall(INSERT_USER_NOTICE_MANAGER_ALERT_SQL);
					int alertId = getNextSequenceId(GET_MAX_ALERT_ID);
					stmt.setInt(1, alertId);

					stmt.setBigDecimal(2, userNoticeManagerAlert.getProfileId());
					stmt.setString(3, userNoticeManagerAlert.getAlertFrequenceCode());
					stmt.setInt(4, Integer.parseInt(userNoticeManagerAlert.getAlertTimingCode()));
					stmt.setString(5, userNoticeManagerAlert.getAlertName());
					stmt.setString(6, userNoticeManagerAlert.getAlertUrgencyName());
					stmt.setDate(7, new Date(userNoticeManagerAlert.getStartDate().getTime()));
					stmt.setInt(8, userNoticeManagerAlert.getContractId());
					userNoticeAddCount  += stmt.executeUpdate();
				}else{
					stmt = conn.prepareCall(UPDATE_USER_NOTICE_MANAGER_ALERT_SQL);
					stmt.setString(1, userNoticeManagerAlert.getAlertFrequenceCode());

					stmt.setInt(2, Integer.parseInt(userNoticeManagerAlert.getAlertTimingCode()));
					stmt.setString(3, userNoticeManagerAlert.getAlertName());
					stmt.setString(4, userNoticeManagerAlert.getAlertUrgencyName());
					stmt.setDate(5,  new Date(userNoticeManagerAlert.getStartDate().getTime()));
					stmt.setInt(6,userNoticeManagerAlert.getContractId());
					stmt.setInt(7,userNoticeManagerAlert.getAlertId());
					userNoticeAddCount += stmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			logger.error("Add the User Notice alert info which was added in Notice preference page", e);
			throw new SystemException(e, 
					"Problem occurred during addUserNoticePreferences sql call. ");
		} finally {
			close(stmt, conn);
		}

		return userNoticeAddCount;
	}
	
	/**
	 * Checks if the alert name already exists
	 * @param alertName
	 * @return
	 * @throws SystemException
	 */
	public static boolean checkAlertNameExists(String alertName, Integer alertId, Integer contractId) 
			throws SystemException { 
		logger.debug("Executing select SQL statement: " + SQL_CHECK_ALERT_NAME);
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		boolean alertNameExist =  false;
		int count = 0;
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SQL_CHECK_ALERT_NAME);			
			stmt.setString(1, alertName);
			stmt.setInt(2, alertId);
			stmt.setInt(3, contractId);

			rs = stmt.executeQuery();
			if (rs.next()) {
				count =rs.getInt("COUNT");
				if(count > 0){
					 alertNameExist =  true;        	
				}
			}
		}catch (SQLException e) {
			logger.error("Check if the new alert name already exists", e);
			throw new SystemException(e, 
					"Problem occurred during checkAlertNameExists sql call. ");
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed select SQL statement: " + SQL_CHECK_ALERT_NAME);
		return alertNameExist;
		
	}
	
	/**
	 * Deletes the particular alert
	 * @param alertId
	 * @return
	 * @throws SystemException
	 */
	public static boolean deleteAlert(Integer alertId) 
			throws SystemException { 
		logger.debug("Executing delete SQL statement: " + SQL_DELETE_ALERT);
		Connection conn = null;
		CallableStatement stmt = null;
		boolean flag = false;
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SQL_DELETE_ALERT);			
			stmt.setInt(1, alertId);			

		
			Integer count  = stmt.executeUpdate();
			if(count>0){
				 flag = true;
			}
		}catch (SQLException e) {
			logger.error("Delete the User Notice alert info which was added in Notice preference page", e);
			throw new SystemException(e, 
					"Problem occurred during deleteAlert sql call. ");
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed delete SQL statement: " + SQL_DELETE_ALERT);
	return flag;
	}
	
	/**
	 * Adds the user action information
	 * @param profileId
	 * @param WebPageTypeCode
	 * @return
	 * @throws SystemException
	 */
	public static boolean userActionLog(Integer contractId,BigDecimal profileId,String userAction) 
			throws SystemException { 
		logger.debug("Executing insert SQL statement: " + INSERT_WEBSITE_PAGE_LOG);
		Connection conn = null;
		CallableStatement stmt = null;
		boolean flag = false;
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			int userActionId = getNextSequenceId(GET_MAX_USER_ACTION_ID);
			stmt = conn.prepareCall(INSERT_WEBSITE_PAGE_LOG);		
			stmt.setInt(1, userActionId);
			stmt.setInt(2, contractId);
			stmt.setBigDecimal(3, profileId);
			stmt.setString(4, userAction);
			stmt.executeUpdate();
			flag = true;
			
		}catch (SQLException e) {
			logger.error("Add the user action information of the page corresponding to the user", e);
			throw new SystemException(e, 
					"Problem occurred during userActionLog sql call. ");
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed insert SQL statement: " + INSERT_WEBSITE_PAGE_LOG);
	return flag;
	}
	
	/**
	 * Get the existing notice manager alert details
	 * @param alertId
	 * @return UserNoticeManagerAlertVO
	 * @throws SystemException
	 */
	public static UserNoticeManagerAlertVO getExistingAlertDetails(int alertId) 
			throws SystemException { 
		UserNoticeManagerAlertVO existingNoticeManagerAlert = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareStatement(SELECT_EXISTING_ALERT_SQL_BY_ALERT_ID);
			stmt.setInt(1,alertId);
			rs = stmt.executeQuery();
			if (rs != null ){
				while (rs.next()){
					existingNoticeManagerAlert =  new UserNoticeManagerAlertVO();
					existingNoticeManagerAlert.setAlertFrequenceCode(rs.getString("ALERT_FREQUENCY_CODE"));
					existingNoticeManagerAlert.setAlertId(rs.getInt("ALERT_ID"));
					existingNoticeManagerAlert.setAlertName(rs.getString("ALERT_NAME"));
					existingNoticeManagerAlert.setAlertTimingCode(String.valueOf(rs.getInt("ALERT_TIMING_CODE")));
					existingNoticeManagerAlert.setAlertUrgencyName(rs.getString("ALERT_URGENCY_IND"));
					existingNoticeManagerAlert.setContractId(rs.getInt("CONTRACT_ID"));
					existingNoticeManagerAlert.setProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
					existingNoticeManagerAlert.setStartDate(rs.getDate("START_DATE"));
				
				}
			}
			
		} catch (SQLException e) {
			logger.error("Retrieve the User Notice alert info based  on the alert id", e);
			throw new SystemException(e, 
					"Problem occurred during getExistingAlertDetails sql call. ");
		} finally {
			close(stmt, conn);
		}

		return existingNoticeManagerAlert;

	}
	
	/**
	 * Get the UserManager AlertFrequency Codes
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public static List<LookupDescription> getUserManagerAlertFrequencyCodes(Integer ContractId)
			throws SystemException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription userManagerAlertFrequencyCode = null;
		List<LookupDescription> userManagerAlertFrequencyCodes = null;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getUserManagerAlertFrequencyCodes");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

			stmt = connection.prepareStatement(USER_ALERT_FREQUENCY_CODES_SQL);
			 rs = stmt.executeQuery();
			 if (rs != null ) {
				 userManagerAlertFrequencyCodes = new ArrayList<LookupDescription>();
	            	while (rs.next())
	            	{
	            		userManagerAlertFrequencyCode   = new LookupDescription();
	            		userManagerAlertFrequencyCode.setLookupCode(rs.getString("LOOKUP_CODE"));
	            		userManagerAlertFrequencyCode.setLookupDesc(rs.getString("LOOKUP_DESC"));
	            		userManagerAlertFrequencyCodes.add(userManagerAlertFrequencyCode);
	            	}
			 }


		} catch (SQLException e) {
			logger.error("Retreive the Alert frequency code from Look up table", e);
			throw new SystemException(e, 
					"Problem occurred during getUserManagerAlertFrequencyCodes sql call. ");
		} finally {
			close(stmt, connection);
		}
		return userManagerAlertFrequencyCodes;
	}
	
	/**
	 * Get the UserManager Alert TimingCodes 
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public static List<LookupDescription> getUserManagerAlertTimingCodes(Integer contractId)
			throws SystemException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription userManagerAlertTimingCode = null;
		List<LookupDescription> userManagerAlertTimingCodes = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getUserManagerAlertTimingCodes");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

						
						
			stmt = connection.prepareStatement(USER_NOTICE_MANAGER_ALERT_TIMINGS_SQL);
			 rs = stmt.executeQuery();

			 if (rs != null ) {
				 userManagerAlertTimingCodes = new ArrayList<LookupDescription>();
	            	while (rs.next())
	            	{
	            		userManagerAlertTimingCode   = new LookupDescription();
	            		userManagerAlertTimingCode.setLookupCode(rs.getString("LOOKUP_CODE"));
	            		userManagerAlertTimingCode.setLookupDesc(rs.getString("LOOKUP_DESC"));
	            		userManagerAlertTimingCodes.add(userManagerAlertTimingCode);
	            	}
			 }

		} catch (SQLException e) {
			logger.error("Retreive the Alert timing code from Look up table", e);
			throw new SystemException(e, 
					"Problem occurred during addUserNoticePreferences sql call. ");
		} finally {
			close(stmt, connection);
		}
		return userManagerAlertTimingCodes;
	}
	/**
	 *  This method is used to get the terms and acceptance indicator value 
	 * @param profileId
	 * @return
	 * @throws SystemException
	 */
	public static String getTermsAndAcceptanceInd(BigDecimal profileId) throws SystemException{ 

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String termsAndAccepatance = ""; 
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(SQL_SELECT_TERMS_ACCEPTANCE_CODE);
			stmt.setBigDecimal(1, profileId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				termsAndAccepatance = rs.getString("TERMS_ACCEPTANCE_CODE");
			}
			}catch (SQLException e) {
				logger.error("Retrieve the terms acceptance code based  on the profile id", e);
				throw new SystemException(e, 
						"Problem occurred during getTermsAndAcceptanceInd sql call. ");
			} finally {
				close(stmt, conn);
			}
			return termsAndAccepatance;
		}
	/**
	 * Get the Document Posted Username
	 * @param contractId
	 * @param documentId
	 * @return
	 * @throws SystemException
	 */
	
	public  static PlanNoticeDocumentVO getDocumentPostedUsername(int contractId,int documentId) throws SystemException{
		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(CONTRACT_NOTICE_DOCUMENT_HISTORY_SQL_INFO);
			stmt.setInt(1, contractId);
			stmt.setInt(2, documentId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				PlanNoticeDocumentChangeHistoryVO planNoticeDocumentChangeHistory =  new PlanNoticeDocumentChangeHistoryVO();
				String firstName = (rs.getString("FIRST_NAME")).toLowerCase();
        		String FName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        	    String lastName = rs.getString("LAST_NAME").toLowerCase();
        		String LName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        		planNoticeDocumentChangeHistory.setChangedUserName(FName + " " +LName);
				
				
				planNoticeDocumentVO.setPlanNoticeDocumentChangeDetail(planNoticeDocumentChangeHistory);
			}
				} catch (SQLException e) {
			logger.error("Retrieve the plan notice document posted username  based  on the document id", e);
			throw new SystemException(e, 
					"Problem occurred during getCustomPlanNoticeDocumentInfo sql call. ");
		} finally {
			close(stmt, conn);
		}

		return planNoticeDocumentVO;

	}
	

	/**
	 * This method will insert records into Contract Notice Mailing Order 
	 * 
	 * 
	 * @param planNoticeDocumentDetail
	 *            PlanNoticeDocumentVO       
	 * @return Integer
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static boolean updateCustomPlanNoticeDocumentOrder(List<Integer> documentOrderList) throws SystemException {

		logger.debug("Executing insert SQL statement: " + SQL_UPDATE_CONTRACT_NOTICE_DOCUMENT_ORDER);
		
		boolean flag = false;
		Connection conn = null;
		CallableStatement stmt = null;
		
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SQL_UPDATE_CONTRACT_NOTICE_DOCUMENT_ORDER);
			Iterator<Integer> documentIdIterator = documentOrderList.iterator();
			int documentIndex = documentOrderList.size();
			while(documentIdIterator.hasNext()){
				stmt.setInt(1, documentIndex);			
				stmt.setInt(2, documentIdIterator.next());
				stmt.execute();
				documentIndex--;
				flag = true;
			}


		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during " + SQL_UPDATE_CONTRACT_NOTICE_DOCUMENT_ORDER);
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed insert SQL statement: " + SQL_UPDATE_CONTRACT_NOTICE_DOCUMENT_ORDER);

		return flag;
	}
	/**
	 * To Get the NoticeManager Alert Notification Preferences
	 * @param profileId
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public static List<UserNoticeManagerAlertVO> getNoticeManagerAlertNotificationPreferences() 
			throws SystemException {
		List<UserNoticeManagerAlertVO> userNoticePreference = new ArrayList<UserNoticeManagerAlertVO>();
		UserNoticeManagerAlertVO userNoticeManagerAlert = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(SELECT_NOTICE_MANAGER_ALERT_NOTIFICATION_SQL);
			rs = stmt.executeQuery();
			if (rs != null ){
				while (rs.next()){
				userNoticeManagerAlert =  new UserNoticeManagerAlertVO();
				userNoticeManagerAlert.setAlertFrequenceCode(rs.getString("ALERT_FREQUENCY_CODE"));
				userNoticeManagerAlert.setAlertId(rs.getInt("ALERT_ID"));
				userNoticeManagerAlert.setAlertName(rs.getString("ALERT_NAME"));
				userNoticeManagerAlert.setAlertTimingCode(String.valueOf(rs.getInt("ALERT_TIMING_CODE")));
				userNoticeManagerAlert.setAlertUrgencyName(rs.getString("ALERT_URGENCY_IND"));
				userNoticeManagerAlert.setContractId(rs.getInt("CONTRACT_ID"));
				userNoticeManagerAlert.setProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
				userNoticeManagerAlert.setStartDate(rs.getDate("START_DATE"));
				userNoticePreference.add(userNoticeManagerAlert);
				
				}
			}

			
		} catch (SQLException e) {
			logger.error("Retrieve the User Notice alert info based  on the contract and user profile id", e);
			throw new SystemException(e, 
					"Problem occurred during getUserNoticePreferences sql call. ");
		} finally {
			close(stmt, conn);
		}

		return userNoticePreference;
	}
	
	
	/**
	 * Get the count of all users uploaded document
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	public  static int getUserUploadedDocumentPostedCount(Integer contractId) throws SystemException{
		int uploadedDocumentCount = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(CONTRACT_NOTICE_UPLOADED_DOCUMENT_COUNT_SQL);
			stmt.setInt(1, contractId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				uploadedDocumentCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("Retrieve the plan notice document posted username  based  on the document id", e);
			throw new SystemException(e, 
					"Problem occurred during getCustomPlanNoticeDocumentInfo sql call. ");
		} finally {
			close(stmt, conn);
		}

		return uploadedDocumentCount;

	}
	
	/**
	 * This method is used to get the list of ContractNoticeDocumentTypeCodes
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public static List<LookupDescription> getContractNoticeDocumentTypeCodes()
			throws SystemException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		LookupDescription contractNoticeDocumentTypeCode = null;
		List<LookupDescription> contractNoticeDocumentTypeCodes = null;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getContractNoticeDocumentTypeCodes");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

			stmt = connection.prepareStatement(CONTRACT_NOTICE_CHANGE_TYPE_CODES_SQL);
			 rs = stmt.executeQuery();
			 if (rs != null ) {
				 contractNoticeDocumentTypeCodes = new ArrayList<LookupDescription>();
	            	while (rs.next())
	            	{
	            		contractNoticeDocumentTypeCode   = new LookupDescription();
	            		contractNoticeDocumentTypeCode.setLookupCode(rs.getString("LOOKUP_CODE"));
	            		contractNoticeDocumentTypeCode.setLookupDesc(rs.getString("LOOKUP_DESC"));
	            		contractNoticeDocumentTypeCodes.add(contractNoticeDocumentTypeCode);
	            	}
			 }
		} catch (SQLException e) {
			logger.error("Retrieve the plan notice document type code ", e);
			throw new SystemException(e, 
					"Problem occurred during getContractNoticeDocumentTypeCodes sql call. ");
		} finally {
			close(stmt, connection);
		}
		return contractNoticeDocumentTypeCodes;
	}
	
	/**
	 * This method will insert records into Contract Notice Mailing Order 
	 * 
	 * 
	 * @param planNoticeDocumentDetail
	 *            PlanNoticeDocumentVO       
	 * @return Integer
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static boolean insertContractNoticeMailingOrder(PlanNoticeMailingOrderVO planNoticeMailingOrderVO) throws SystemException {

		logger.debug("Executing insert SQL statement: " + SQL_INSERT_CONTRACT_NOTICE_MAILING_ORDER);
		
		boolean flag = false;
		Connection conn = null;
		CallableStatement stmt = null;
		
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SQL_INSERT_CONTRACT_NOTICE_MAILING_ORDER);
			//int orderId = getNextSequenceId(GET_MAX_ORDER_NO);
			stmt.setInt(1, planNoticeMailingOrderVO.getOrderNumber());			
			stmt.setInt(2, planNoticeMailingOrderVO.getContractId());
			stmt.setBigDecimal(3, planNoticeMailingOrderVO.getProfileId());
			stmt.setString(4, planNoticeMailingOrderVO.getPlanNoticeMailingOrderStatus().getLookupCode());
			stmt.setString(5, planNoticeMailingOrderVO.getColorPrintInd());
			stmt.setInt(6, planNoticeMailingOrderVO.getTotalPageCount());
			stmt.setInt(7, planNoticeMailingOrderVO.getNoOfParticipant());	
			stmt.setBigDecimal(8, planNoticeMailingOrderVO.getTotalMailingCost());
			stmt.setString(9, planNoticeMailingOrderVO.getMailingName());
			stmt.setString(10, planNoticeMailingOrderVO.getAdressFileOption());
			stmt.execute();
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during " + SQL_INSERT_CONTRACT_NOTICE_MAILING_ORDER);
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed insert SQL statement: " + SQL_UPDATE_CONTRACT_NOTICE_DOCUMENT_ORDER);

		return flag;
	}
	
	/**
	 * Get the custom document based on the document id
	 * @param documentId
	 * @return
	 * @throws SystemException
	 */
	public static boolean getCustomDocumentPresence(Integer documentId) throws SystemException{

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer count = 0;
		try {
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(SQL_CUSTOM_DOCUMENT_PRESENCE_COUNT);
			stmt.setInt(1, documentId);
			rs = stmt.executeQuery();
			if (rs.next()) {
				count =rs.getInt("COUNT");
			}
			
			}catch (SQLException e) {
				throw new SystemException(e, 
						"Problem occurred during getCustomDocumentPresence sql call. ");
			} finally {
				close(stmt, conn);
			}
		if(count > 0){
			return true;
		}else{
			return false;
		}
	}
   
	/**
	 * updating the soft delete indicator
	 * 
	 * @param planNoticeDocumentDetail
	 * @return
	 * @throws SystemException
	 */
	public static boolean updateSoftDelgateIndicator(PlanNoticeDocumentVO planNoticeDocumentDetail) throws SystemException {

		if (logger.isDebugEnabled())
			logger.debug("entry -> insertCustomPlanNoticeDocumentLogs");

		boolean flag = false;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_UPDATE_SOFT_DELETE_IND_CUSTOM_PLAN_NOTICE);
			stmt.setInt(1,planNoticeDocumentDetail.getDocumentId());
			Integer count  = stmt.executeUpdate();
			if(count>0){
				flag = true;
			}
		}catch (SQLException se) {
			logger.error("Release the Plan notice document Lock ", se);
			throw new SystemException(se, 
					"Problem occurred during releaseCustomPlanNoticeDocumentLock sql call. ");
		} finally {
			close(stmt, conn);
		}
		return flag;

	}
	/**
	 * This method will insert records into Contract Notice Mailing Order 
	 * 
	 * 
	 * @param planNoticeDocumentDetail
	 *            PlanNoticeDocumentVO       
	 * @return Integer
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static boolean updateContractNoticeMailingOrderStatus(PlanNoticeMailingOrderVO planNoticeMailingOrderVO) throws SystemException {

		logger.debug("Executing update SQL statement: " + SQL_UPDATE_CONTRACT_NOTICE_MAILING_ORDER_STATUS);
		
		boolean flag = false;
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		try{
				 
				conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
				stmt = conn.prepareCall(SQL_UPDATE_CONTRACT_NOTICE_MAILING_ORDER_STATUS);
				Integer appendCount = 0;
				if(planNoticeMailingOrderVO.getContractId().equals(null)){
					stmt.setInt(++appendCount, rs.getInt("CONTRACT_ID"));
				}
				else{
					stmt.setInt(++appendCount, planNoticeMailingOrderVO.getContractId());
				}
				
				if(planNoticeMailingOrderVO.getProfileId().equals(null)){
					stmt.setInt(++appendCount, rs.getInt("USER_PROFILE_ID"));
				}
				else{
					stmt.setBigDecimal(++appendCount, planNoticeMailingOrderVO.getProfileId());
				}	
				
				if(planNoticeMailingOrderVO.getPlanNoticeMailingOrderStatus().getLookupCode().equals(null)){
					stmt.setString(++appendCount, rs.getString("ORDER_STATUS_CODE"));
				}
				else{
					stmt.setString(++appendCount, planNoticeMailingOrderVO.getPlanNoticeMailingOrderStatus().getLookupCode());
				}	
				
				if(planNoticeMailingOrderVO.getColorPrintInd().equals(null)){
					stmt.setString(++appendCount, rs.getString("COLOR_PRINT_IND"));
				}
				else{
					stmt.setString(++appendCount, planNoticeMailingOrderVO.getColorPrintInd());
				}	
				
				if(planNoticeMailingOrderVO.getTotalPageCount().equals(null)){
					stmt.setInt(++appendCount, rs.getInt("PAGE_COUNT"));
				}
				else{
					stmt.setInt(++appendCount, planNoticeMailingOrderVO.getTotalPageCount());
				}
				
				if(planNoticeMailingOrderVO.getNoOfParticipant().equals(null)){
					stmt.setInt(++appendCount, rs.getInt("PARTICIPANT_COUNT"));
				}
				else{
					stmt.setInt(++appendCount, planNoticeMailingOrderVO.getNoOfParticipant());	
				}
				
				if(planNoticeMailingOrderVO.getTotalMailingCost().equals(null)){
					stmt.setInt(++appendCount, rs.getInt("TOTAL_MAILING_COST_AMOUNT"));
				}
				else{
					stmt.setBigDecimal(++appendCount, planNoticeMailingOrderVO.getTotalMailingCost());
				}
				
				if(planNoticeMailingOrderVO.getMailingName().equals(null)){
					stmt.setString(++appendCount, rs.getString("ORDER_NAME"));
				}
				else{
					stmt.setString(++appendCount, planNoticeMailingOrderVO.getMailingName());
				}
				stmt.setInt(++appendCount, planNoticeMailingOrderVO.getOrderNumber());			
				
				stmt.execute();
				
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during " + SQL_UPDATE_CONTRACT_NOTICE_MAILING_ORDER_STATUS);
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed insert SQL statement: " + SQL_UPDATE_CONTRACT_NOTICE_MAILING_ORDER_STATUS);

		return flag;
	}

	/**
	 * checkCustomNoticeDocumentNameChange
	 * @param documentId
	 * @return
	 * @throws SystemException
	 */
	@SuppressWarnings("deprecation")
	public static boolean checkCustomNoticeDocumentNameChange(Integer documentId,String documentName) throws SystemException {


		logger.debug("Executing insert SQL statement: " + SQL_CUSTOM_DOCUMENT_NAME_COUNT);
		Connection conn = null;
		CallableStatement stmt = null;
		ResultSet rs = null;
		int count = 0;
		try{
			conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn.prepareCall(SQL_CUSTOM_DOCUMENT_NAME_COUNT);			
			stmt.setInt(1, documentId);			
			stmt.setString(2, documentName);			

			rs = stmt.executeQuery();
			if (rs.next()) {
				count =rs.getInt("COUNT");
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new SystemException(
					e,
					className,
					"validateInput",
					"Problem occurred during " + SQL_CUSTOM_DOCUMENT_NAME_COUNT);
		} finally {
			close(stmt, conn);
		}
		logger.debug("Executed insert SQL statement: " + SQL_CHECK_NOTICE_NAME);
		if(count > 0){
			return false;        	
		}else{
			return true;
		}
	}
	/**
	 * This method is used to check the  PlanNoticeDocumentSoftDeleteIndicator value
	 * @param documentId
	 * @return
	 * @throws SystemException
	 * @throws SQLException
	 */
	public static String checkPlanNoticeSoftDeleteIndicator(int contractId,int documentId,String documentFileName) throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> insertCustomPlanNoticeDocumentLogs");

		Connection conn = null;
		PreparedStatement stmt = null;
		String softDeleteIndValue = null;
		ResultSet rs = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_SOFT_DELETE_IND_VALUE);
			stmt.setInt(1, contractId);
			stmt.setInt(2, documentId);
			stmt.setString(3, documentFileName);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				softDeleteIndValue = rs.getString("SOFT_DELETE_IND");
			}
		}catch (SQLException se) {
			logger.error("Retrieve the plan notice SoftDeleteIndValue", se);
			throw new SystemException(se, 
					"Problem occurred during retreive PlanNotice SoftDeleteIndValue sql call. ");
		} finally {
			close(stmt, conn);
		}
		return softDeleteIndValue;
	}
	
	/**
	 * To get the PlanNotice Deleted Username
	 * @param contractId
	 * @param documentId
	 * @return
	 * @throws SystemException
	 */
	public  static PlanNoticeDocumentVO getPlanNoticeDeletedUsername(int contractId,int documentId) throws SystemException{
		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SELECT_PLAN_NOTICE_DELETED_USERNAME);
			stmt.setInt(1, contractId);
			stmt.setInt(2, documentId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				String firstName = (rs.getString("FIRST_NAME")).toLowerCase();
        		String FName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        	    String lastName = rs.getString("LAST_NAME").toLowerCase();
        		String LName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        		planNoticeDocumentVO.setPlanNoticeDeletedUserName(FName + " " +LName);
			}
				} catch (SQLException e) {
			logger.error("Retrieve the plan notice document posted username  based  on the document id", e);
			throw new SystemException(e, 
					"Problem occurred during getCustomPlanNoticeDocumentInfo sql call. ");
		} finally {
			close(stmt, conn);
		}
		return planNoticeDocumentVO;
	}
	

	/**
	 * This method is used to get the user details who have made updates to the contract notice.
	 *
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public static LinkedHashMap<BigDecimal,String> getContractNoticeUpdatedUserDetails(Integer contractId, java.sql.Timestamp fromDate, java.sql.Timestamp toDate)
					throws SystemException{
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String noticeChangedUserDetail = null;
		LinkedHashMap<BigDecimal,String> noticeChangedUserDetails = new LinkedHashMap<BigDecimal,String>();
		int paramCount = 0;
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
		}

		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);


			stmt = connection.prepareStatement(CONTRACT_NOTICE_DOCUMENT_USER_DETAIL_HISTORY_SQL);
			stmt.setInt(++paramCount, contractId );
			stmt.setTimestamp(++paramCount, toDate);
			stmt.setTimestamp(++paramCount, fromDate);
			rs = stmt.executeQuery();
			if (rs != null ) {
				while (rs.next())
				{
					noticeChangedUserDetail   = rs.getString("FIRST_NAME") + " " + rs.getString("LAST_NAME");
					noticeChangedUserDetails.put(rs.getBigDecimal("USER_PROFILE_ID"), noticeChangedUserDetail);
				}
				return noticeChangedUserDetails;
			}

		} catch (SQLException e) {
			logger.error("Retrieve the contract notice update user details", e);
			throw new SystemException(e, 
					"Problem occurred during getContractNoticeUpdatedUserDetails sql call. ");
		} finally {
			close(stmt, connection);
		}
		return null;
	}
/**
	 * This method is used to get the  Custom notice document locked UserName
	 *
	 * @param criteria
	 * @param planDocumentReportData
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public  static PlanNoticeDocumentVO  getPlanNoticeLockedUserName(String componentKey,long lockUserProfileId)throws SystemException{
	
		PlanNoticeDocumentVO planNoticeDocumentVO = new PlanNoticeDocumentVO();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SELECT_PLAN_NOTICE_LOCKED_USERNAME);
			stmt.setString(1, componentKey);
			stmt.setLong(2, lockUserProfileId);
			rs = stmt.executeQuery();
			if (rs != null && rs.next()) {
				String firstName = (rs.getString("FIRST_NAME")).toLowerCase();
        		String FName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1);
        	    String lastName = rs.getString("LAST_NAME").toLowerCase();
        		String LName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1);
        		planNoticeDocumentVO.setPlanNoticeLockedUserName(FName + " " +LName);
			}
				} catch (SQLException e) {
			logger.error("Retrieve the plan notice document loked username  based  on the document id", e);
			throw new SystemException(e, 
					"Problem occurred during getPlanNoticeLockedUserName sql call. ");
		} finally {
			close(stmt, conn);
		}
		return planNoticeDocumentVO;
	}
	
	/**
	 * This method will update the order status records into Contract Notice Mailing Order 
	 * @param contractNoticeMailingOrderVO
	 * @return
	 * @throws DAOException
	 * @throws SystemException 
	 */
	public static boolean updateContractNoticeMailingOrder(PlanNoticeMailingOrderVO contractNoticeMailingOrderVO) throws  SystemException {


		boolean updatedStatusInd = false;
		Connection conn = null;
		PreparedStatement stmt = null;

		try{
			int pCount  = 0;
			StringBuffer query =  new StringBuffer(SQL_UPDATE_CONTRACT_NOTICE_MAILING_ORDER);
			String colorPrintInd = contractNoticeMailingOrderVO.getColorPrintInd();
			int pageCount = contractNoticeMailingOrderVO.getTotalPageCount();
			int noOfParticipant = contractNoticeMailingOrderVO.getNoOfParticipant();
			BigDecimal totalMailingCost =  contractNoticeMailingOrderVO.getTotalMailingCost();
			java.util.Date orderMailedDate = contractNoticeMailingOrderVO.getOrderStatusDate();
			String vipInd = contractNoticeMailingOrderVO.getVipInd();
			String orderSealedInd = contractNoticeMailingOrderVO.getOrderSealedInd();
			String orderStapledInd = contractNoticeMailingOrderVO.getOrderStapledInd();
			String bulkOrderInd = contractNoticeMailingOrderVO.getBulkOrderInd();
			String largeEnvelopeInd = contractNoticeMailingOrderVO.getLargeEnvelopeInd();
			int merrilOrderNumber = contractNoticeMailingOrderVO.getMerrilOrderNumber();
			if(StringUtils.isNotBlank(colorPrintInd)){
				query.append(COLOR_PRINT_IND_COLUMN_UPDATE);
			}

			if( pageCount > 0){
				query.append(PAGE_COUNT_COLUMN_UPDATE);
			}

			if( noOfParticipant > 0 ){
				query.append(PARTICIPANT_COUNT_COLUMN_UPDATE);
			}
			if( totalMailingCost!= null && totalMailingCost.compareTo(BigDecimal.ZERO)>0){
				query.append(TOTAL_MAILING_COST_AMOUNT_COLUMN_UPDATE);
			}
			
			if(orderMailedDate != null){
				query.append(ORDER_STATUS_DATE_COLUMN_UPDATE);
			}
		
			if(StringUtils.isNotBlank(vipInd) ){
				query.append(VIP_ORDER_IND_COLUMN_UPDATE);
			}
			if(StringUtils.isNotBlank(orderSealedInd) ){
				query.append(ORDER_SEALED_IND_COLUMN_UPDATE);
			}
			if(StringUtils.isNotBlank(orderStapledInd) ){
				query.append(ORDER_STAPLED_IND_COLUMN_UPDATE);
			}
			if(StringUtils.isNotBlank(bulkOrderInd) ){
				query.append(BULK_ORDER_IND_COLUMN_UPDATE);
			}
			if(StringUtils.isNotBlank(largeEnvelopeInd) ){
				query.append(LARGE_ENVELOPE_IND_COLUMN_UPDATE);
			}
			if(merrilOrderNumber>0){
				query.append(MERRILL_ORDER_NO_COLUMN_UPDATE);
			}
			query.append(ORDER_STATUS_WHERE_CLAUSE_SQL);
			// get the connection for CSDB
			conn = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);

			stmt = conn.prepareCall(query.toString());
			stmt.setString(++pCount, contractNoticeMailingOrderVO.getOrderStatusCode());	
			if(StringUtils.isNotBlank(colorPrintInd)){
				stmt.setString(++pCount, colorPrintInd);	
			}

			if( pageCount > 0){
				stmt.setInt(++pCount, pageCount);	
			}

			if( noOfParticipant > 0 ){
				stmt.setInt(++pCount, noOfParticipant);	
			}
			if( totalMailingCost!= null && totalMailingCost.compareTo(BigDecimal.ZERO)>0){
				stmt.setBigDecimal(++pCount, totalMailingCost);	
			}
			if(	orderMailedDate != null){
				stmt.setDate(++pCount, new Date(orderMailedDate.getTime()));
			}
			if(StringUtils.isNotBlank(vipInd) ){
				stmt.setString(++pCount,vipInd);
			}
			if(StringUtils.isNotBlank(orderSealedInd) ){
				stmt.setString(++pCount,orderSealedInd);
			}
			if(StringUtils.isNotBlank(orderStapledInd) ){
				stmt.setString(++pCount,orderStapledInd);
			}
			if(StringUtils.isNotBlank(bulkOrderInd) ){
				stmt.setString(++pCount,bulkOrderInd);
			}
			if(StringUtils.isNotBlank(largeEnvelopeInd) ){
				stmt.setString(++pCount,largeEnvelopeInd);
			}
			if(merrilOrderNumber>0){
				stmt.setInt(++pCount, merrilOrderNumber);	
			}
			stmt.setInt(++pCount, contractNoticeMailingOrderVO.getOrderNumber());		
			int updatedCount  = stmt.executeUpdate();
			if(updatedCount>0){
				updatedStatusInd = true;
			}
		}catch (SQLException e) {
			logger.error("update the Contract Notice Mailing Order", e);
			throw new SystemException(e, 
					"Something went wrong while executing the updateContractNoticeMailingOrder() -  Contract Number ["+ contractNoticeMailingOrderVO.getContractId() + "]");
		} catch (SystemException e) {
			logger.error("update the Contract Notice Mailing Order", e);
			throw new SystemException(e, 
					"Something went wrong while executing the updateContractNoticeMailingOrder() -  Contract Number ["+ contractNoticeMailingOrderVO.getContractId() + "]");
		}finally {
			close(stmt, conn);
		}

		return updatedStatusInd;
	}
	
	/**
	 * Get the contract notice mailing order details for the given orderNumber
	 * @param orderNumber
	 * @return
	 * @throws SystemException 
	 * @throws SystemException 
	 * @throws SystemException
	 * @throws ReportServiceException
	 */
	public static PlanNoticeMailingOrderVO getContractNoticeMailingOrder(int orderNumber)
			throws SystemException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PlanNoticeMailingOrderVO orderDetail = new PlanNoticeMailingOrderVO();
		try {
			// get the connection for CSDB
			connection = getReadUncommittedConnection(
					className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = connection.prepareCall(CONTRACT_NOTICE_MAILING_ORDER_SQL);
			stmt.setInt(1, orderNumber);
			 rs = stmt.executeQuery();
			 if (rs != null ) {
	            	while (rs.next())
	            	{
	            		orderDetail.setColorPrintInd(rs.getString("COLOR_PRINT_IND"));
	            		orderDetail.setContractId(rs.getInt("CONTRACT_ID"));
	            		orderDetail.setNoOfParticipant(rs.getInt("PARTICIPANT_COUNT"));
	            		orderDetail.setOrderNumber(rs.getInt("ORDER_NO"));
	            		orderDetail.setOrderStatusDate(rs.getDate("ORDER_STATUS_DATE"));
	            		orderDetail.setProfileId(rs.getBigDecimal("USER_PROFILE_ID"));
	            		orderDetail.setTotalMailingCost(rs.getBigDecimal("TOTAL_MAILING_COST_AMOUNT"));
	            		orderDetail.setTotalPageCount(rs.getInt("PAGE_COUNT"));
	            		orderDetail.setMailingName(rs.getString("ORDER_NAME"));
	            		orderDetail.setOrderStatusCode(rs.getString("ORDER_STATUS_CODE"));
	            		orderDetail.setVipInd(rs.getString("VIP_ORDER_IND"));
	            		orderDetail.setMerrilOrderNumber(rs.getInt("MERRILL_ORDER_NO"));
	            	}
			 }

		} catch (SQLException e) {
			throw new SystemException(e, 
			"Problem occurred during getContractNoticeMailingOrder sql call. ");
		}  catch (SystemException e) {
			throw new SystemException(e, 
			"Problem occurred during getContractNoticeMailingOrder sql call. ");
		} finally {
			close(stmt, connection);
		}
		return orderDetail;
	}

	/**
	 * this method is used to get the eligible employee list
	 * @param contractNumber
	 * @param eligibleEmployeeAddressList
	 * @return
	 * @throws SystemException
	 */
	public static List<EmployeeEligibleVO> getEligibleEmployeeDetails(int contractNumber,
			List<EmployeeEligibleVO> eligibleEmployeeAddressList) throws SystemException{
		if (logger.isDebugEnabled()) {
			logger.debug("Entered <- getEligibleEmployeeDetails()");
		}
		
		final List<EmployeeEligibleVO> eligibleEmployeeAddrList = new ArrayList<EmployeeEligibleVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			
			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			stmt = conn
					.prepareStatement(SQL_SELECT_ELIGIBLE_EMPLOYEE_DETAILS);
			stmt.setInt(1, contractNumber);
			stmt.setInt(2, contractNumber);
			stmt.setInt(3, contractNumber);
			stmt.setInt(4, contractNumber);
			rs = stmt.executeQuery();
			if (rs != null) {
				
				while (rs.next()) {
					EmployeeEligibleVO mailingDetailsVo = new EmployeeEligibleVO();
					mailingDetailsVo.setParticiapantFirstName(StringUtils.trimToEmpty(rs.getString("FNAME")));
					mailingDetailsVo.setParticiapantLastName(StringUtils.trimToEmpty(rs.getString("LNAME")));
					mailingDetailsVo.setParticiapantAddressLine1(StringUtils.trimToEmpty(rs.getString("ADDR1")));
					mailingDetailsVo.setParticiapantAddressLine2(StringUtils.trimToEmpty(rs.getString("ADDR2")));
					mailingDetailsVo.setParticiapantCity(StringUtils.trimToEmpty(rs.getString("CITY")));
					mailingDetailsVo.setParticiapantState(StringUtils.trimToEmpty(rs.getString("STATE")));
					mailingDetailsVo.setParticiapantZip(StringUtils.trimToEmpty(rs.getString("ZIP")));
					mailingDetailsVo.setParticiapantCountry(StringUtils.trimToEmpty(rs.getString("COUNTRY")));
					mailingDetailsVo.setEmployeeStatus(StringUtils.trimToEmpty(rs.getString("EMPLOYMENT_STATUS_CODE")));
					mailingDetailsVo.setEligibilityIndicator(StringUtils.trimToEmpty(rs.getString("PLAN_ELIGIBLE_IND")));
					mailingDetailsVo.setAccountBalance(StringUtils.trimToEmpty(rs.getString("TOTAL_BALANCE")));
					mailingDetailsVo.setPlanEntryDate(rs.getDate("ELIGIBILITY_DATE"));
					mailingDetailsVo.setAccountHolderFlag(StringUtils.trimToEmpty(rs.getString("PARTICIPANT_IND")));
					eligibleEmployeeAddrList.add(mailingDetailsVo);
					
				}
			}
				} catch (SQLException e) {
			logger.error("Problem occurred during getEligibleEmployeeDetails sql call", e);
			throw new SystemException(e, 
					"Problem occurred during getEligibleEmployeeDetails sql call. ");
		} finally {
			close(stmt, conn);
		}
		return eligibleEmployeeAddrList;
		
	}

}
