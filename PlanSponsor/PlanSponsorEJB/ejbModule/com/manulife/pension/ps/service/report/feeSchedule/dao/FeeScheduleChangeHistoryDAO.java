package com.manulife.pension.ps.service.report.feeSchedule.dao;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.ContractFeeScheduleChangeHistoryReportData;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.FeeScheduleChangeItem;
import com.manulife.pension.ps.service.report.feeSchedule.valueobject.TpaStandardFeeScheduleChangeHistoryReportData;
import com.manulife.pension.service.contract.valueobject.ContractValidationDetail;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.fee.valueobject.DesignatedInvestmentManager;
import com.manulife.pension.service.fee.valueobject.FeeScheduleHistoryDetails.TpaFirm;
import com.manulife.pension.service.fee.valueobject.FeeScheduleHistoryDetails.TpaFirmDetailsPeriod;
import com.manulife.pension.service.fee.valueobject.PBARestriction;
import com.manulife.pension.service.fee.valueobject.PersonalBrokerageAccount;
import com.manulife.pension.service.plan.validators.ContractValidator;
import com.manulife.pension.service.plan.validators.ContractValidatorFactory;
import com.manulife.pension.service.report.dao.SqlPair;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.ps.common.util.Constants;

/**
 * 
 * Notice Info , Standard Fee Schedule and Contract Fee Schedule Change History
 * Retrieve Logic
 * 
 * 
 * @author Siby Thomas
 * 
 */
public class FeeScheduleChangeHistoryDAO extends BaseDatabaseDAO {

	private static Logger logger = Logger
			.getLogger(FeeScheduleChangeHistoryDAO.class);

	public static final String className = FeeScheduleChangeHistoryDAO.class
			.getName();

	private static final String SQL_DIM_HISTORY_SELECT = "SELECT CIMH.CREATED_USER_ID, "
			+ "CIMH.CREATED_TS, CIMH.LAST_NAME, CIMH.FIRST_NAME, COMPANY_NAME, ADDR_LINE1, ADDR_LINE2,   "
			+ "CITY_NAME, STATE_CODE, ZIP_CODE, PHONE_NO, PHONE_EXT, EMAIL_ADDR, COMMENT, DELETED_IND   "
			+ "FROM  PSW100.CONTRACT_INVESTMENT_MANAGER_HIST CIMH "
			+ "WHERE  "
			+ "CONTRACT_ID = ? "
			+ "AND DATE(CIMH.CREATED_TS) BETWEEN ? AND ? FOR READ ONLY ";
	
	private static final String SQL_PBA_HISTORY_SELECT = "SELECT CREATED_USER_PROFILE_ID, "
			+ "CREATED_TS, PBA_PROVIDER_NAME, PBA_PROVIDER_AREA_CODE, PBA_PROVIDER_PHONE_PREFIX, PBA_PROVIDER_PHONE_NO, PBA_PROVIDER_PHONE_EXT,   "
			+ "PBA_PROVIDER_EMAIL_ADDR, PBA_MIN_ACCOUNT_BAL, PBA_RESTRICTION_IND, DELETED_IND   "
			+ "FROM  " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_PBA CP "
			+ "WHERE  "
			+ "CONTRACT_ID = ? "
			+ "AND DATE(CP.CREATED_TS) BETWEEN ? AND ? FOR READ ONLY ";
	
		
	private static final String SQL_PBA_RESTRICTION_HISTORY_SELECT_ADDED_DELETED = "WITH BASE AS ("
			+"SELECT PBA_TRADE_RESTRICTION_SEQ_NO, PBA_TRADE_RESTRICTION_DESC, DELETED_IND, CREATED_TS, CREATED_USER_PROFILE_ID, 'PBA Restriction Added' AS CATEGORY "
			+"FROM " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_PBA_TRADING_RESTRICTION R1 WHERE CONTRACT_ID=? AND DELETED_IND = 'N' "
			+"AND CREATED_TS = ( "
					+"SELECT MIN( CREATED_TS ) FROM PSW100. CONTRACT_PBA_TRADING_RESTRICTION R2 "
					+"WHERE R1.CONTRACT_ID = R2.CONTRACT_ID  AND R1.PBA_TRADE_RESTRICTION_SEQ_NO = R2.PBA_TRADE_RESTRICTION_SEQ_NO) "	 
			+"UNION ALL "
			+"SELECT PBA_TRADE_RESTRICTION_SEQ_NO, PBA_TRADE_RESTRICTION_DESC, DELETED_IND, CREATED_TS, CREATED_USER_PROFILE_ID, 'PBA Restriction Deleted' AS CATEGORY "
			+"FROM " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_PBA_TRADING_RESTRICTION R1 WHERE CONTRACT_ID=? AND DELETED_IND = 'Y' "
			+"AND CREATED_TS = ( "
					+"SELECT MAX( CREATED_TS ) FROM PSW100. CONTRACT_PBA_TRADING_RESTRICTION R2 "
					+"WHERE R1.CONTRACT_ID = R2.CONTRACT_ID  AND R1.PBA_TRADE_RESTRICTION_SEQ_NO = R2.PBA_TRADE_RESTRICTION_SEQ_NO)) "						
            +"SELECT * FROM BASE WHERE DATE(CREATED_TS) BETWEEN ? AND ? FOR READ ONLY ";
	
	private static final String SQL_PBA_RESTRICTION_HISTORY_SELECT_ALL  = "SELECT * FROM " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_PBA_TRADING_RESTRICTION WHERE CONTRACT_ID=? AND DATE(CREATED_TS) BETWEEN ? AND ? FOR READ ONLY";
	
	
	private static final String SQL_NON_TPA_CONTRACT_FEE_HISTORY = "WITH BASE AS ( SELECT  "
			+ "CTFH.CREATED_TS,  "
			+ "CTFH.NON_STD_FEE_DESCRIPTION AS FEE_DESC,  "
			+ "CTFH.FEE_CODE, "
			+ "CTFH.FEE_CATEGORY_CODE, "
			+ "CASE  "
			+ "  WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT  "
			+ "  ELSE CTFH.FEE_PCT  "
			+ "END AS AMOUNT_VALUE,  "
			+ "CASE  "
			+ "   WHEN CTFH.FEE_AMT  IS NOT NULL THEN '$'  "
			+ "   ELSE '%'  "
			+ "END AS AMOUNT_TYPE,  "
			+ "CTFH.COMMENT,  "
			+ "CTFH.DELETED_IND,  "
			+ "CTFH.CREATED_USER_ID,  "
			+ " '7777'  AS  FEE_TYPE_ORDER, "
			+ "CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  AS  FEE_SEARCH_CASE "
			+ "FROM   PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH  "
			+ "WHERE CTFH.CONTRACT_ID = ? "
			+ "AND CTFH.THIRD_PARTY_ADMIN_ID = 0  "
			+ "AND FEE_CATEGORY_CODE = 'NT' "
			+ "AND DATE(CTFH.CREATED_TS) BETWEEN ? AND ? ) "
			+ "SELECT * FROM BASE ";
	
	private static final String SQL_PBA_FEE_HISTORY = "WITH BASE AS ( SELECT "
			 +"CPFD.CREATED_TS, CPFD.PBA_FEE_DESC, CPFD.PBA_FEE_TYPE_CODE, CPFD.PBA_FEE_AMT, "
			 +"CASE WHEN CPFD.PBA_FEE_UNIT_TYPE ='P' "
			 +"THEN '%' ELSE '$' END AS PBA_FEE_UNIT_TYPE, "
			 +"CPFD.DELETED_IND, CPFD.CREATED_USER_PROFILE_ID, "
			 +"CASE WHEN  CPFD.PBA_FEE_TYPE_CODE IN(SELECT LOOKUP_CODE FROM PSW100.LOOKUP WHERE LOOKUP_CODE<> 'CUST' AND LOOKUP_TYPE_NAME='PBA_FEE_TYPE')"
			 +"		THEN  CPFD.PBA_FEE_TYPE_CODE "
			 +"		ELSE  'PC' || ' ' || CPFD.PBA_FEE_DESC  END AS  FEE_SEARCH_CASE "
			 +"FROM " + PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_PBA_FEE_DETAIL CPFD "
			 +"WHERE CPFD.CONTRACT_ID = ? "
			 +"AND DATE(CPFD.CREATED_TS) BETWEEN ? AND ? ) "
			 +"SELECT * FROM BASE ";

	private static final String SQL_CUSTOMIZE_CONTRACT_FEE_HISTORY = "WITH BASE AS ( SELECT  "
			+ "CTFH.CREATED_TS,  "
			+ "CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL THEN TSFT.FEE_DESCRIPTION ELSE  CTFH.NON_STD_FEE_DESCRIPTION END AS FEE_DESC,  "
			+ "CTFH.FEE_CODE, "
			+ "CTFH.FEE_CATEGORY_CODE, "
			+ "CASE  "
			+ "  WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT  "
			+ "  ELSE CTFH.FEE_PCT  "
			+ "END AS AMOUNT_VALUE,  "
			+ "CASE  "
			+ "   WHEN CTFH.FEE_AMT  IS NOT NULL THEN '$'  "
			+ "   ELSE '%'  "
			+ "END AS AMOUNT_TYPE,  "
			+ "CTFH.COMMENT,  "
			+ "CTFH.DELETED_IND,  "
			+ "CTFH.CREATED_USER_ID,  "
			+ "CASE WHEN  TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ "CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' "
			+ "     THEN  CTFH.FEE_CODE "
			+ "     ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE "
			+ "FROM   PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH  "
			+ "LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT  "
			+ "        ON CTFH.FEE_CODE = TSFT.FEE_CODE  "
			+ "WHERE CTFH.CONTRACT_ID = ? "
			+ "AND CTFH.THIRD_PARTY_ADMIN_ID = ?  "
			+ "AND FEE_CATEGORY_CODE IN ('TP', 'TN') "
			+ "AND DATE(CTFH.CREATED_TS) BETWEEN ? AND ? ) "
			+ "SELECT * FROM BASE ";
	
	
	private static final String SQL_STANDARD_CONTRACT_FEE_HISTORY = "WITH BASE AS ( SELECT  "
			+ "CTFH.CREATED_TS,  "
			+ "CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL THEN TSFT.FEE_DESCRIPTION ELSE  CTFH.NON_STD_FEE_DESCRIPTION END AS FEE_DESC,  "
			+ "CTFH.FEE_CODE, "
			+ "CTFH.FEE_CATEGORY_CODE, "
			+ "CASE  "
			+ "  WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT  "
			+ "  ELSE CTFH.FEE_PCT  "
			+ "END AS AMOUNT_VALUE,  "
			+ "CASE  "
			+ "   WHEN CTFH.FEE_AMT  IS NOT NULL THEN '$'  "
			+ "   ELSE '%'  "
			+ "END AS AMOUNT_TYPE,  "
			+ "CTFH.COMMENT,  "
			+ "CTFH.DELETED_IND,  "
			+ "CTFH.CREATED_USER_ID,  "
			+ "CASE WHEN  TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ "CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' "
			+ "     THEN  CTFH.FEE_CODE "
			+ "     ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE "
			+ "FROM   PSW100.TPA_STANDARD_FEE_HIST CTFH  "
			+ "LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT  "
			+ "        ON CTFH.FEE_CODE = TSFT.FEE_CODE  "
			+ "WHERE THIRD_PARTY_ADMIN_ID = ? "
			+ "AND DATE(CTFH.CREATED_TS) BETWEEN ? AND ? ) "
			+ "SELECT * FROM BASE ";

	private static final String SQL_SELECT_STANDARD_TPA_FEE_HISTORY = "with BASE as( SELECT tsft.fee_description, "
			+ "       tsfh.created_ts, "
			+ "       tsfh.fee_code, "
			+ "       tsfh.fee_category_code, "
			+ "       tsfh.non_std_fee_description, "
			+ "       case when tsfh.fee_amt  IS NOT NULL  then tsfh.fee_amt "
			+ "       else tsfh.fee_pct end as AMOUNT_VALUE, "
			+ "       case when tsfh.fee_amt  IS NOT NULL  then '$' "
			+ "       else '%' end as AMOUNT_TYPE, "
			+ "       tsfh.comment, "
			+ "       tsfh.DELETED_IND,  "
			+ "       tsfh.created_user_id, "
			+ "UP.FIRST_NAME ||  ' ' ||   UP.LAST_NAME CREATED_USER,  "
			+ "CASE WHEN  TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER "
			+ "FROM   "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "tpa_standard_fee_hist tsfh "
			+ "       LEFT JOIN "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "tpa_standard_fee_type tsft "
			+ "              ON tsfh.fee_code = tsft.fee_code "
			+ "       LEFT JOIN "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "user_profile up "
			+ "              ON up.user_profile_id = tsfh.created_user_id "
			+ "WHERE  tsfh.third_party_admin_id = ? and date(tsfh.CREATED_TS) between ? and  ? ";

	private static final String SELECT_TPA_FEES_FOR_DROPDOWN = "WITH base "
			+ "     AS (SELECT order_no, "
			+ "                fee_code, "
			+ "                fee_description as fee_description, "
			+ "                upper(fee_description) as order_fee_desc, "
			+ "                'TP' AS FEE_CATEGORY_CODE "
			+ "         FROM   "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "tpa_standard_fee_type "
			+ "         UNION ALL "
			+ "         SELECT DISTINCT '7777' AS ORDER_NO, "
			+ "                non_std_fee_description as fee_code, "
			+ "                non_std_fee_description as fee_description, "
			+ "                upper(non_std_fee_description) as order_fee_desc, "
			+ "                'TN'   AS FEE_CATEGORY_CODE "
			+ "         FROM   "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "tpa_standard_fee_hist "
			+ "         WHERE  fee_category_code = 'TN' and THIRD_PARTY_ADMIN_ID = ? ) "
			+ "SELECT * " + "FROM   base " + "ORDER  BY order_no, "
			+ "          order_fee_desc " + "FOR READ only ";

	private static final String SELECT_USER_NAMES_FOR_DROPDOWN = "SELECT DISTINCT tsfh.created_user_id, "
			+ "                up.first_name, "
			+ "                up.last_name "
			+ "FROM   "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "tpa_standard_fee_hist tsfh "
			+ "       LEFT JOIN "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "user_profile up "
			+ "              ON up.user_profile_id = tsfh.created_user_id "
			+ "              where tsfh.THIRD_PARTY_ADMIN_ID = ? ";

	private static final String SELECT_CUSTOM_CONTRACT_STANDARDIZED_HISTORY = " WITH STANDARDIZED_BY AS ( "
			+ " SELECT DISTINCT CTFH.CREATED_USER_ID FROM   PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH" 
			+ " WHERE CREATED_TS = ? AND CONTRACT_ID = ? ), "
			+ " CUSTOM_VALUE AS " 
		    + " (SELECT CASE  WHEN TSFT.FEE_DESCRIPTION IS NOT NULL THEN TSFT.FEE_DESCRIPTION  ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC, "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT,  "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE, "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE " 
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE "
			+ " FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE "
			+ " WHERE CREATED_TS = ? "
			+ " AND CONTRACT_ID = ? "
			+ " AND THIRD_PARTY_ADMIN_ID = ? "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0)), "
			+ " STANDARD_VALUE AS "
			+ " (SELECT CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL THEN TSFT.FEE_DESCRIPTION ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC, "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT,  "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE, "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE " 
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE "
			+ " FROM PSW100.TPA_STANDARD_FEE_HIST CTFH "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE "
			+ " WHERE THIRD_PARTY_ADMIN_ID = ? "
			+ " AND DELETED_IND = 'N' "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0) "
			+ " AND CTFH.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.TPA_STANDARD_FEE_HIST "
			+ "        WHERE FEE_CODE = CTFH.FEE_CODE "
			+ "        AND THIRD_PARTY_ADMIN_ID = CTFH.THIRD_PARTY_ADMIN_ID "
			+ "        AND CREATED_TS <= ? "
			+ "        AND FEE_CATEGORY_CODE = CTFH.FEE_CATEGORY_CODE)) "
			+ " SELECT " 
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN CV.FEE_CODE ELSE SV.FEE_CODE END AS FEE_CODE, "
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN CV.FEE_DESC ELSE SV.FEE_DESC END AS FEE_DESC, "
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN CV.FEE_CATEGORY_CODE ELSE SV.FEE_CATEGORY_CODE END AS FEE_CATEGORY_CODE, "
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN 0 ELSE SV.AMOUNT_VALUE END AS AMOUNT_VALUE, "
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN CV.AMOUNT_TYPE ELSE SV.AMOUNT_TYPE END AS AMOUNT_TYPE, "
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN CV.FEE_TYPE_ORDER ELSE SV.FEE_TYPE_ORDER END AS FEE_TYPE_ORDER, "
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN '' ELSE SV.COMMENT END AS COMMENT, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN SV.FEE_SEARCH_CASE ELSE CV.FEE_SEARCH_CASE END AS FEE_SEARCH_CASE, "
			+ " (SELECT CREATED_USER_ID FROM STANDARDIZED_BY) "
			+ " FROM STANDARD_VALUE SV FULL "
			+ " OUTER JOIN CUSTOM_VALUE CV ON CV.FEE_SEARCH_CASE = SV.FEE_SEARCH_CASE "
	        + " WHERE (CV.FEE_CODE IS NULL OR SV.FEE_CODE IS NULL OR SV.AMOUNT_TYPE <> CV.AMOUNT_TYPE OR SV.AMOUNT_VALUE <> CV.AMOUNT_VALUE OR SV.COMMENT <> CV.COMMENT) ";
	
	private static final String SELECT_CUSTOM_CONTRACT_CUSTOMIZED_HISTORY = " WITH CUSTOMIZED_BY AS ( "
			+ " SELECT DISTINCT CTFH.CREATED_USER_ID FROM   PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH" 
			+ " WHERE CREATED_TS = ? AND CONTRACT_ID = ? ), "
			+ " CUSTOM_VALUE AS " 
			+ "(SELECT CASE  WHEN TSFT.FEE_DESCRIPTION IS NOT NULL THEN TSFT.FEE_DESCRIPTION  ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC, "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT, "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE, "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE " 
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE "
			+ " FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE "
			+ " WHERE CREATED_TS = ? "
			+ " AND CONTRACT_ID = ? "
			+ " AND THIRD_PARTY_ADMIN_ID = ? "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0)), "
			+ " STANDARD_VALUE AS "
			+ " (SELECT CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL THEN TSFT.FEE_DESCRIPTION ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC, "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT,  "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE, "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE " 
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE "
			+ " FROM PSW100.TPA_STANDARD_FEE_HIST CTFH "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE "
			+ " WHERE THIRD_PARTY_ADMIN_ID = ? "
			+ " AND DELETED_IND = 'N' "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0) "
			+ " AND CTFH.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.TPA_STANDARD_FEE_HIST "
			+ "        WHERE FEE_CODE = CTFH.FEE_CODE "
			+ "        AND THIRD_PARTY_ADMIN_ID = CTFH.THIRD_PARTY_ADMIN_ID "
			+ "        AND CREATED_TS <= ? "
			+ "        AND FEE_CATEGORY_CODE = CTFH.FEE_CATEGORY_CODE)) "
			+ " SELECT " 
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN SV.FEE_CODE ELSE CV.FEE_CODE END AS FEE_CODE, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN SV.FEE_DESC ELSE CV.FEE_DESC END AS FEE_DESC, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN SV.FEE_CATEGORY_CODE ELSE CV.FEE_CATEGORY_CODE END AS FEE_CATEGORY_CODE, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN 0 ELSE CV.AMOUNT_VALUE END AS AMOUNT_VALUE, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN '' ELSE CV.COMMENT END AS COMMENT, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN SV.AMOUNT_TYPE ELSE CV.AMOUNT_TYPE END AS AMOUNT_TYPE, "
			+ " CASE WHEN SV.FEE_CODE IS NULL THEN CV.FEE_TYPE_ORDER ELSE SV.FEE_TYPE_ORDER END AS FEE_TYPE_ORDER, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN 0 ELSE CV.AMOUNT_VALUE END AS AMOUNT_VALUE, "
			+ " CASE WHEN CV.FEE_CODE IS NULL THEN SV.FEE_SEARCH_CASE ELSE CV.FEE_SEARCH_CASE END AS FEE_SEARCH_CASE, "
			+ " (SELECT CREATED_USER_ID FROM CUSTOMIZED_BY) "
			+ " FROM CUSTOM_VALUE CV FULL "
			+ " OUTER JOIN STANDARD_VALUE SV ON CV.FEE_SEARCH_CASE = SV.FEE_SEARCH_CASE "
	        + " WHERE (CV.FEE_CODE IS NULL OR SV.FEE_CODE IS NULL OR SV.AMOUNT_TYPE <> CV.AMOUNT_TYPE OR SV.AMOUNT_VALUE <> CV.AMOUNT_VALUE OR SV.COMMENT <> CV.COMMENT ) ";
	
	
	private static final String SELECT_TPA_FIRM_END_STANDARDIZED_SNAPSHOT =  " WITH BASE AS (  SELECT CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL"
			+ " THEN TSFT.FEE_DESCRIPTION ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC,  "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT,   "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE,  "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE,  "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE   "
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE,  "
			+ " '8' AS   CREATED_USER_ID "
			+ " FROM PSW100.TPA_STANDARD_FEE_HIST CTFH  "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE  "
			+ " WHERE THIRD_PARTY_ADMIN_ID = ?  "
			+ " AND DELETED_IND = 'N'  "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0)  "
			+ " AND CTFH.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.TPA_STANDARD_FEE_HIST  "
			+ "      WHERE FEE_CODE = CTFH.FEE_CODE  "
			+ "      AND THIRD_PARTY_ADMIN_ID = CTFH.THIRD_PARTY_ADMIN_ID  "
			+ "      AND CREATED_TS <= ? "
			+ "      AND FEE_CATEGORY_CODE = CTFH.FEE_CATEGORY_CODE) ) "
	        + " SELECT * FROM BASE ";
	
	private static final String SELECT_TPA_FIRM_START_STANDARDIZED_SNAPSHOT =  " WITH BASE AS (  SELECT CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL"
			+ " THEN TSFT.FEE_DESCRIPTION ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC,  "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT,   "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE,  "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE   "
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE,  "
			+ " '8' AS   CREATED_USER_ID "
			+ " FROM PSW100.TPA_STANDARD_FEE_HIST CTFH  "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE  "
			+ " WHERE THIRD_PARTY_ADMIN_ID = ?  "
			+ " AND DELETED_IND = 'N'  "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0)  "
			+ " AND CTFH.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.TPA_STANDARD_FEE_HIST  "
			+ "      WHERE FEE_CODE = CTFH.FEE_CODE  "
			+ "      AND THIRD_PARTY_ADMIN_ID = CTFH.THIRD_PARTY_ADMIN_ID  "
			+ "      AND CREATED_TS <= ? "
			+ "      AND FEE_CATEGORY_CODE = CTFH.FEE_CATEGORY_CODE) ) "
	        + " SELECT * FROM BASE ";
	
	private static final String SELECT_TPA_FIRM_START_CUSTOMIZED_SNAPSHOT =  " WITH BASE AS ( SELECT CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL"
			+ " THEN TSFT.FEE_DESCRIPTION ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC,  "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT,   "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE,  "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE,  "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE   "
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE,  "
			+ " '8' AS   CREATED_USER_ID "
			+ " FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH  "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE  "
			+ " WHERE THIRD_PARTY_ADMIN_ID = ?  "
			+ " AND CONTRACT_ID = ?  "
			+ " AND DELETED_IND = 'N'  "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0)  "
			+ " AND CTFH.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST  "
			+ "      WHERE FEE_CODE = CTFH.FEE_CODE  "
			+ "      AND THIRD_PARTY_ADMIN_ID = CTFH.THIRD_PARTY_ADMIN_ID  "
			+ "      AND CONTRACT_ID = CTFH.CONTRACT_ID  "
			+ "      AND CREATED_TS <= ? "
			+ "      AND FEE_CATEGORY_CODE = CTFH.FEE_CATEGORY_CODE) )  "
            + " SELECT * FROM BASE ";
	
	private static final String SELECT_TPA_FIRM_END_CUSTOMIZED_SNAPSHOT =  " WITH BASE AS ( SELECT CASE WHEN TSFT.FEE_DESCRIPTION IS NOT NULL"
			+ " THEN TSFT.FEE_DESCRIPTION ELSE CTFH.NON_STD_FEE_DESCRIPTION  END AS FEE_DESC,  "
			+ " CTFH.FEE_CODE, CTFH.FEE_CATEGORY_CODE, CTFH.COMMENT,   "
			+ " CASE WHEN TSFT.ORDER_NO IS NOT NULL THEN TSFT.ORDER_NO ELSE '7777' END AS  FEE_TYPE_ORDER, "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN CTFH.FEE_AMT ELSE CTFH.FEE_PCT END AS AMOUNT_VALUE,   "
			+ " CASE WHEN CTFH.FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE,  "
			+ " CASE WHEN  CTFH.FEE_CATEGORY_CODE = 'TP' THEN  CTFH.FEE_CODE   "
			+ "      ELSE  CTFH.FEE_CATEGORY_CODE || ' ' || CTFH.NON_STD_FEE_DESCRIPTION  END AS  FEE_SEARCH_CASE,  "
			+ " '8' AS   CREATED_USER_ID "
			+ " FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST CTFH  "
			+ " LEFT OUTER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON CTFH.FEE_CODE = TSFT.FEE_CODE  "
			+ " WHERE THIRD_PARTY_ADMIN_ID = ?  "
			+ " AND CONTRACT_ID = ?  "
			+ " AND DELETED_IND = 'N'  "
			+ " AND (FEE_AMT > 0  OR FEE_PCT > 0)  "
			+ " AND CTFH.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST  "
			+ "      WHERE FEE_CODE = CTFH.FEE_CODE  "
			+ "      AND THIRD_PARTY_ADMIN_ID = CTFH.THIRD_PARTY_ADMIN_ID  "
			+ "      AND CONTRACT_ID = CTFH.CONTRACT_ID  "
			+ "      AND CREATED_TS < ? "
			+ "      AND FEE_CATEGORY_CODE = CTFH.FEE_CATEGORY_CODE) )  "
            + " SELECT * FROM BASE ";
	
	private static final String SELECT_PLAN_PROVISION_VALIDATORS = " select fee_code, validator_class_name from  PSW100.TPA_STANDARD_FEE_TYPE tsft, "
			+ " PSW100.CONTRACT_VALIDATOR_CLASS cvc "
			+ " where tsft.validator_class_code = cvc.validator_class_code for read only ";
	
	private static final String SELECT_APPLICALBE_TPA_STANDARD_FEE = " WITH BASE AS ( SELECT   TPSF.FEE_CODE,  TSFT.FEE_DESCRIPTION FEE_DESC,  FEE_CATEGORY_CODE, "
			+ "CASE WHEN FEE_AMT IS NOT NULL THEN FEE_AMT ELSE FEE_PCT END AS AMOUNT_VALUE , COMMENT,  "
			+ "CASE WHEN FEE_AMT IS NOT NULL THEN '$' ELSE '%' END AS AMOUNT_TYPE , "
			+ "TSFT.ORDER_NO FEE_TYPE_ORDER, "
			+ "CASE WHEN  TPSF.FEE_CATEGORY_CODE = 'TP' THEN  TPSF.FEE_CODE    ELSE  TPSF.FEE_CATEGORY_CODE || ' ' || TPSF.NON_STD_FEE_DESCRIPTION  "
			+ "END AS  FEE_SEARCH_CASE, "
			+ "'8' AS  CREATED_USER_ID  "
			+ "FROM PSW100.TPA_STANDARD_FEE_HIST  TPSF   "
			+ "INNER JOIN PSW100.TPA_STANDARD_FEE_TYPE TSFT ON TPSF.FEE_CODE = TSFT.FEE_CODE "
			+ "			WHERE TPSF.THIRD_PARTY_ADMIN_ID = ?  "
			+ "			AND DELETED_IND = 'N'    "
			+ "			AND (FEE_AMT > 0  OR FEE_PCT > 0)   "
			+ "         AND TSFT.FEE_CODE  in ({0})   "
			+ "			AND TPSF.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.TPA_STANDARD_FEE_HIST     "
			+ "			WHERE FEE_CODE = TPSF.FEE_CODE     "
			+ "			AND THIRD_PARTY_ADMIN_ID = TPSF.THIRD_PARTY_ADMIN_ID   "
			+ "			AND CREATED_TS <= ? "
			+ "			AND FEE_CATEGORY_CODE = TPSF.FEE_CATEGORY_CODE)  "
			+ "AND NOT EXISTS ( "
			+ "         SELECT 1  FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST  CTFH    "
			+ "			WHERE CTFH.CONTRACT_ID = ? "
			+ "			AND CTFH.THIRD_PARTY_ADMIN_ID = ?   "
			+ "			AND DELETED_IND = 'N'    "
			+ "			AND CTFH.CREATED_TS = (SELECT MAX(CREATED_TS) FROM PSW100.CONTRACT_CUSTOM_TPA_FEE_HIST   "
			+ "			WHERE FEE_CODE = CTFH.FEE_CODE     "
			+ "			AND THIRD_PARTY_ADMIN_ID = CTFH.THIRD_PARTY_ADMIN_ID  AND CONTRACT_ID = CTFH.CONTRACT_ID   "
			+ "			AND CREATED_TS <= ?  AND FEE_CATEGORY_CODE = 'TP'  "
			+ "			AND FEE_CATEGORY_CODE = CTFH.FEE_CATEGORY_CODE))) " 
			+ " SELECT * FROM BASE ";
	
	

	public FeeScheduleChangeHistoryDAO() {
	}

	/**
	 * Method to retrieve Contract fee changes history records.
	 * 
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 */
	public static List<FeeScheduleChangeItem> getDimHistory(
			ReportCriteria criteria) throws SystemException {

		Connection connection = null;
		PreparedStatement statement = null;
		DesignatedInvestmentManager designatedInvestmentManager = null;
		List<DesignatedInvestmentManager> changeHistoryDim = new ArrayList<DesignatedInvestmentManager>();

		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		
		int contractId = getContractId(criteria);

		try {

			connection = getDefaultConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);

			PreparedStatement preparedStatement = connection
					.prepareStatement(SQL_DIM_HISTORY_SELECT);
			
			java.sql.Date fromDate = getFromDate(criteria);
			java.sql.Date toDate = getToDate(criteria);
			
			int parameterCount = 1;

			if (logger.isDebugEnabled()) {
				logger.debug(" contractId [" + contractId + "]");
				logger.debug(" DIM History From Date [" + fromDate + "]");
				logger.debug(" DIM History To Date [" + toDate + "]");
			}
			
			preparedStatement.setLong(parameterCount++, contractId);
			preparedStatement.setDate(parameterCount++, fromDate);
			preparedStatement.setDate(parameterCount++, toDate);
			
			ResultSet result = preparedStatement.executeQuery();

			if (result != null) {
				while (result.next()) {
					designatedInvestmentManager = new DesignatedInvestmentManager();
					designatedInvestmentManager.setFirstName(result
							.getString("FIRST_NAME"));
					designatedInvestmentManager.setLastName(result
							.getString("LAST_NAME"));
					designatedInvestmentManager.setCompany(result
							.getString("COMPANY_NAME"));
					designatedInvestmentManager.setAddressLine1(result
							.getString("ADDR_LINE1"));
					designatedInvestmentManager.setAddressLine2(result
							.getString("ADDR_LINE2"));
					designatedInvestmentManager.setCity(result
							.getString("CITY_NAME"));
					designatedInvestmentManager.setState(result
							.getString("STATE_CODE"));
					designatedInvestmentManager.setZipCode(result
							.getString("ZIP_CODE"));
					designatedInvestmentManager.setEmailAddress(result
							.getString("EMAIL_ADDR"));
					designatedInvestmentManager.setPhone(result
							.getString("PHONE_NO"));
					designatedInvestmentManager.setPhoneExt(result
							.getString("PHONE_EXT"));
					designatedInvestmentManager.setSpecialNotes(result
							.getString("COMMENT"));
					designatedInvestmentManager.setDeleted(result.getString(
							"DELETED_IND").equals("Y") ? true : false);
					designatedInvestmentManager.setCreatedUserId(result
							.getString("CREATED_USER_ID"));
					designatedInvestmentManager.setCreatedTimeStamp(result
							.getTimestamp("CREATED_TS"));
					changeHistoryDim.add(designatedInvestmentManager);
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(statement, connection);
		}

		Collections.sort(changeHistoryDim,
				new Comparator<DesignatedInvestmentManager>() {
					@Override
					public int compare(DesignatedInvestmentManager object1,
							DesignatedInvestmentManager object2) {
						return object2.getCreatedTimeStamp().compareTo(
								object1.getCreatedTimeStamp());
					}
				});

		if (!changeHistoryDim.isEmpty()) {

			DesignatedInvestmentManager lastRow = changeHistoryDim
					.get(changeHistoryDim.size() - 1);

			for (int i = 0; i < changeHistoryDim.size() - 1; i++) {
				DesignatedInvestmentManager latestChange = changeHistoryDim
						.get(i);
				DesignatedInvestmentManager previousChange = changeHistoryDim
						.get(i + 1);
				try {
					if (latestChange.isDeleted()) {
						DesignatedInvestmentManager emptyRow = new DesignatedInvestmentManager();
						emptyRow.setCreatedUserId(latestChange
								.getCreatedUserId());
						emptyRow.setCreatedTimeStamp(latestChange
								.getCreatedTimeStamp());
						compareDesignatedInvestmentManager(emptyRow,
								previousChange, DesignatedInvestmentManager
										.getDimFields(), changeItems);
					} else {
						compareDesignatedInvestmentManager(latestChange,
								previousChange, DesignatedInvestmentManager
										.getDimFields(), changeItems);
					}
				} catch (Exception e) {
					throw new SystemException(e,
							"Error occured comparing DIM change rows");
				}
			}

			if (lastRow != null) {
				try {
					compareDesignatedInvestmentManager(lastRow,
							new DesignatedInvestmentManager(),
							DesignatedInvestmentManager.getDimFields(),
							changeItems);
				} catch (Exception e) {
					throw new SystemException(e,
							"Error occured comparing DIM change rows");
				}
			}
		}
		return changeItems;
	}
	
	
	/**
	 * Method to retrieve PBA details changes history records.
	 * 
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 */
	public static List<FeeScheduleChangeItem> getPBADetailsHistory(
			ReportCriteria criteria) throws SystemException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PersonalBrokerageAccount personalBrokerageAccount = null;
		List<PersonalBrokerageAccount> changeHistoryPba = new ArrayList<PersonalBrokerageAccount>();

		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();		
		int contractId = getContractId(criteria);

		try {
			connection = getDefaultConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			
			//Restriction history
			List<FeeScheduleChangeItem> pbaRestrictionHistory = getPbaRestrictionHistory(connection, contractId, criteria);
			changeItems.addAll(pbaRestrictionHistory);

			preparedStatement = connection
					.prepareStatement(SQL_PBA_HISTORY_SELECT);
			
			java.sql.Date fromDate = getFromDate(criteria);
			java.sql.Date toDate = getToDate(criteria);
			
			int parameterCount = 1;

			if (logger.isDebugEnabled()) {
				logger.debug(" contractId [" + contractId + "]");
				logger.debug(" PBA History From Date [" + fromDate + "]");
				logger.debug(" PBA History To Date [" + toDate + "]");
			}
			
			preparedStatement.setLong(parameterCount++, contractId);
			preparedStatement.setDate(parameterCount++, fromDate);
			preparedStatement.setDate(parameterCount++, toDate);
			
			ResultSet result = preparedStatement.executeQuery();

			if (result != null) {
				while (result.next()) {
					personalBrokerageAccount = new PersonalBrokerageAccount();
					personalBrokerageAccount.setPbaProviderName(result
							.getString("PBA_PROVIDER_NAME"));
					personalBrokerageAccount.setPbaEmailAddress(result
							.getString("PBA_PROVIDER_EMAIL_ADDR"));
					personalBrokerageAccount.setPbaPhonePrefix(result
							.getString("PBA_PROVIDER_PHONE_PREFIX"));
					personalBrokerageAccount.setPbaPhoneAreaCode(result
							.getString("PBA_PROVIDER_AREA_CODE"));
					personalBrokerageAccount.setPbaPhoneNum(result
							.getString("PBA_PROVIDER_PHONE_NO"));
					personalBrokerageAccount.setPbaPhoneExt(result
							.getString("PBA_PROVIDER_PHONE_EXT"));
					personalBrokerageAccount.setPbaMinDeposit(result
							.getBigDecimal("PBA_MIN_ACCOUNT_BAL"));
					if(result.getString("PBA_RESTRICTION_IND")!=null){
						personalBrokerageAccount.setPbaRestriction(result
								.getString("PBA_RESTRICTION_IND").trim().equals(Constants.YES) ? Constants.PBA_YES: Constants.PBA_NO);
					}					
					personalBrokerageAccount.setDeleted(result.getString(
							"DELETED_IND").equals("Y") ? true : false);
					personalBrokerageAccount.setCreatedUserId(result
							.getString("CREATED_USER_PROFILE_ID"));
					personalBrokerageAccount.setCreatedTimeStamp(result
							.getTimestamp("CREATED_TS"));
					changeHistoryPba.add(personalBrokerageAccount);
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, connection);
		}

		Collections.sort(changeHistoryPba,
				new Comparator<PersonalBrokerageAccount>() {
					@Override
					public int compare(PersonalBrokerageAccount object1,
							PersonalBrokerageAccount object2) {
						return object2.getCreatedTimeStamp().compareTo(
								object1.getCreatedTimeStamp());
					}
				});

		if (!changeHistoryPba.isEmpty()) {

			PersonalBrokerageAccount lastRow = changeHistoryPba
					.get(changeHistoryPba.size() - 1);

			for (int i = 0; i < changeHistoryPba.size() - 1; i++) {
				PersonalBrokerageAccount latestChange = changeHistoryPba
						.get(i);
				PersonalBrokerageAccount previousChange = changeHistoryPba
						.get(i + 1);
				try {
					if (latestChange.isDeleted()) {
						PersonalBrokerageAccount emptyRow = new PersonalBrokerageAccount();
						emptyRow.setCreatedUserId(latestChange
								.getCreatedUserId());
						emptyRow.setCreatedTimeStamp(latestChange
								.getCreatedTimeStamp());
						comparePersonalBrokerageAccount(emptyRow,
								previousChange, PersonalBrokerageAccount
										.getPbaFields(), changeItems);
					} else {
						comparePersonalBrokerageAccount(latestChange,
								previousChange, PersonalBrokerageAccount
										.getPbaFields(), changeItems);
					}
				} catch (Exception e) {
					throw new SystemException(e,
							"Error occured comparing PBA details change rows");
				}
			}

			if (lastRow != null) {
				try {
					comparePersonalBrokerageAccount(lastRow,
							new PersonalBrokerageAccount(),
							PersonalBrokerageAccount.getPbaFields(),
							changeItems);
				} catch (Exception e) {
					throw new SystemException(e,
							"Error occured comparing PBA details change rows");
				}
			}
		}
		
		
		return changeItems;
	}

	/**
	 * Method to retrieve Contract fee changes history records.
	 * 
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 */
	public static List<FeeScheduleChangeItem> getFeeHistory(
			ReportCriteria criteria) throws SystemException {

		Connection connection = null;

		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();

		try {

			connection = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			
			int contractId = getContractId(criteria);
			
			// get non TPA fee items
			List<FeeScheduleChangeItem> nonTpaFeeItems = getContractNonTpaFeeHistory(connection, contractId, criteria);
			changeItems.addAll(nonTpaFeeItems);
			
			//get PBA std and custom fee items
			List<FeeScheduleChangeItem> pbaFeeItems = getPbaFeeHistory(connection, contractId, criteria);
			changeItems.addAll(pbaFeeItems);
			
			// get contract schedule reset period
			List<TpaFirm> tpaFirmHistory = getTpaFirmHistory(criteria);
			
			TreeMap<TpaFirmDetailsPeriod, TpaFirm> firmPeriods = new TreeMap<TpaFirmDetailsPeriod, TpaFirm>();
			Map<TpaFirm, List<FeeScheduleChangeItem>> tpaFirmStandardSchduleMap = new HashMap<TpaFirm, List<FeeScheduleChangeItem>>();
			Map<TpaFirm, List<FeeScheduleChangeItem>> tpaFirmContractSchduleMap = new HashMap<TpaFirm, List<FeeScheduleChangeItem>>();
			
			Map<String, ContractValidator> planProvisionValidators = getPlanProvisionValidators(connection);
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory = getPlanProvisionHistory(criteria);
			
			Map<ContractValidationDetail, List<String>> planProvisionValidatorsInverseMap = new HashMap<ContractValidationDetail, List<String>>();
			for(Entry<String, ContractValidator> entry : planProvisionValidators.entrySet()) {
				List<String> items = planProvisionValidatorsInverseMap.get(entry.getValue().getValidatorCode());
				if(items != null) {
					items.add(entry.getKey());
				} else {
					items = new ArrayList<String>();
					items.add(entry.getKey());
				}
				planProvisionValidatorsInverseMap.put(entry.getValue().getValidatorCode(), items);
			}
			
			for (TpaFirm firmDetails : tpaFirmHistory) {
				
				int tpaFirmId = firmDetails.getTpaFirmId();
				
				List<FeeScheduleChangeItem> customAllFeeItems = getContractCustomizedScheduleFeeHistory(connection,
						contractId, tpaFirmId, criteria);
				tpaFirmContractSchduleMap.put(firmDetails, customAllFeeItems);
				
				List<FeeScheduleChangeItem> standardAllFeeItems = getContractStandardScheduleFeeHistory(connection,
						contractId, tpaFirmId, criteria);
				tpaFirmStandardSchduleMap.put(firmDetails, standardAllFeeItems);
				
				for(TpaFirmDetailsPeriod firmPeriod : firmDetails.getTpaFirmPeriodHistory()) {
					firmPeriods.put(firmPeriod, firmDetails);
				}
				
				changeItems.addAll(findPlanProvisionsChangesWithinTpaHistory(connection, contractId, tpaFirmId,
						criteria, firmDetails, planProvisionHistory, planProvisionValidatorsInverseMap));
				
			}
			
			List<FeeScheduleChangeItem> previousFirmEndDateSnapshot = new LinkedList<FeeScheduleChangeItem>();
			
			for(Entry<TpaFirmDetailsPeriod, TpaFirm> firmPeriodEntry : firmPeriods.entrySet()) {
				
				TpaFirmDetailsPeriod firmPeriod = firmPeriodEntry.getKey();
				TpaFirm firmDetail = firmPeriodEntry.getValue();
				
				int tpaFirmId = firmDetail.getTpaFirmId();
				
				List<FeeScheduleChangeItem> firmStartDateSnapShot = getContractFirmStartDateSnapshot(connection, contractId, 
						tpaFirmId, firmPeriod, criteria, firmDetail, previousFirmEndDateSnapshot,
						planProvisionValidators, planProvisionHistory);
				changeItems.addAll(firmStartDateSnapShot);
				
				// add custom fees if contract is customized
			    List<FeeScheduleChangeItem> customFeeItems = filterCustomContractValuesForDisplay(connection,
			    		contractId, tpaFirmId, tpaFirmContractSchduleMap.get(firmDetail),
			    		firmPeriod, getFromDateFormatted(criteria),
			    		getToDateFormatted(criteria), criteria, planProvisionValidators, planProvisionHistory);
			    changeItems.addAll(customFeeItems);
				
				// add standard fees
				List<FeeScheduleChangeItem> standardFeeItems = filterStandardScheduleFeeItems(firmPeriod,
						tpaFirmStandardSchduleMap.get(firmDetail), planProvisionValidators, planProvisionHistory);
				changeItems.addAll(standardFeeItems);
				
				previousFirmEndDateSnapshot = getContractFirmEndDateSnapshot(connection, contractId, 
						tpaFirmId, firmPeriod, criteria, firmDetail, planProvisionValidators, planProvisionHistory);
				
			}
			
			if(!previousFirmEndDateSnapshot.isEmpty()) {
				for(FeeScheduleChangeItem item : previousFirmEndDateSnapshot) {
					item.setChangedValue("0");
				}
				changeItems.addAll(previousFirmEndDateSnapshot);
			}
			
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(null, connection);
		}
		return changeItems;
	}

	public static List<FeeScheduleChangeItem> getContractCustomizedScheduleFeeHistory(Connection connection,
			int contractId,
			int tpaFirmId,
			ReportCriteria criteria) throws SystemException {

		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();

		try {
			String sql = buildContractFeeScheduleFilterWhereClause(
					SQL_CUSTOMIZE_CONTRACT_FEE_HISTORY, criteria);
			preparedStatement = connection.prepareStatement(sql);
			
			java.sql.Date fromDate = getFromDate(criteria);
			java.sql.Date toDate = getToDate(criteria);
			
			int parameterCount = 1;

			if (logger.isDebugEnabled()) {
				logger.debug(" contractId [" + contractId + "]");
				logger.debug(" Fee Schedule History From Date [" + fromDate + "]");
				logger.debug(" Fee Schedule History To Date [" + toDate + "]");
			}
			preparedStatement.setLong(parameterCount++, contractId);
			preparedStatement.setLong(parameterCount++, tpaFirmId);
			preparedStatement.setDate(parameterCount++, fromDate);
			preparedStatement.setDate(parameterCount++, toDate);
			
			ResultSet result = preparedStatement.executeQuery();

			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(result
							.getTimestamp("CREATED_TS"), result
							.getString("CREATED_USER_ID"), result
							.getString("FEE_DESC"), result
							.getString("AMOUNT_VALUE"), result
							.getString("AMOUNT_TYPE"), Boolean.FALSE, result
							.getString("COMMENT"), result
							.getInt("FEE_TYPE_ORDER"),
							"Y".equals(result
									.getString("deleted_ind")) ? true : false,
											result
											.getString("FEE_CATEGORY_CODE"),
							result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		return changeItems;
	}
	
	/**
	 * get non TPA fees.
	 * 
	 * @param connection
	 * @param contractId
	 * @param criteria
	 * 
	 * @return List<FeeScheduleChangeItem>
	 * 
	 * @throws SystemException
	 */
	public static List<FeeScheduleChangeItem> getContractNonTpaFeeHistory(final Connection connection,
			final int contractId,
			final ReportCriteria criteria) throws SystemException {
		
		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		java.sql.Date fromDate = getFromDate(criteria);
		java.sql.Date toDate = getToDate(criteria);
		
		// if no need to get non TPA fees , then return empty list
		if (!getNonTpaFeeCriteria(criteria)) {
			return changeItems;
		}

		// get non TPA fees
		try {
			String sql = buildContractFeeScheduleFilterWhereClause(SQL_NON_TPA_CONTRACT_FEE_HISTORY, criteria);
			preparedStatement = connection.prepareStatement(sql);
			int parameterCount = 1;
			preparedStatement.setLong(parameterCount++, contractId);
			preparedStatement.setDate(parameterCount++, fromDate);
			preparedStatement.setDate(parameterCount++, toDate);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(result
							.getTimestamp("CREATED_TS"), result
							.getString("CREATED_USER_ID"), result
							.getString("FEE_DESC"), result
							.getString("AMOUNT_VALUE"), result
							.getString("AMOUNT_TYPE"), null, result
							.getString("COMMENT"), result
							.getInt("FEE_TYPE_ORDER"),
							"Y".equals(result
									.getString("deleted_ind")) ? true : false,
											result
											.getString("FEE_CATEGORY_CODE"),
							result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		return changeItems;
	}
	
	
	/**
	 * get PBA Standard and Custom fees.
	 * 
	 * @param connection
	 * @param contractId
	 * @param criteria
	 * 
	 * @return List<FeeScheduleChangeItem>
	 * 
	 * @throws SystemException
	 */
	public static List<FeeScheduleChangeItem> getPbaFeeHistory(final Connection connection,
			final int contractId,
			final ReportCriteria criteria) throws SystemException {
		
		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		java.sql.Date fromDate = getFromDate(criteria);
		java.sql.Date toDate = getToDate(criteria);		

		// get PBA std and Custom fees
		try {
			String sql = buildContractFeeScheduleFilterWhereClause(SQL_PBA_FEE_HISTORY, criteria);
			preparedStatement = connection.prepareStatement(sql);
			int parameterCount = 1;
			preparedStatement.setLong(parameterCount++, contractId);
			preparedStatement.setDate(parameterCount++, fromDate);
			preparedStatement.setDate(parameterCount++, toDate);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(result.getTimestamp("CREATED_TS"), 
							result.getString("PBA_FEE_DESC"), 
							result.getString("PBA_FEE_AMT"), 
							result.getString("PBA_FEE_UNIT_TYPE"), 
							null, 
							result.getString("CREATED_USER_PROFILE_ID"), 
							"Y".equals(result.getString("DELETED_IND")) ? true : false,
							result.getString("PBA_FEE_TYPE_CODE"),
							result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		return changeItems;
	}
	
	
	/**
	 * get PBA restriction changes history.
	 * 
	 * @param connection
	 * @param contractId
	 * @param criteria
	 * 
	 * @return List<FeeScheduleChangeItem>
	 * 
	 * @throws SystemException
	 */
	public static List<FeeScheduleChangeItem> getPbaRestrictionHistory(final Connection connection,
			final int contractId,
			final ReportCriteria criteria) throws SystemException {
		
		PreparedStatement preparedStatement = null;
		PreparedStatement prpdStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		java.sql.Date fromDate = getFromDate(criteria);
		java.sql.Date toDate = getToDate(criteria);	
		List<PBARestriction> addedAndDeletedList = new ArrayList<PBARestriction>();
		List<PBARestriction> allResList = new ArrayList<PBARestriction>();
		try {
			
			String query = SQL_PBA_RESTRICTION_HISTORY_SELECT_ALL;
			preparedStatement = connection.prepareStatement(query);
			int paramCount = 1;
			preparedStatement.setLong(paramCount++, contractId);
			preparedStatement.setDate(paramCount++, fromDate);
			preparedStatement.setDate(paramCount++, toDate);
			ResultSet resultSet = preparedStatement.executeQuery(); 
			if(resultSet!=null){
				while(resultSet.next()){
					PBARestriction resAll = new PBARestriction();
					resAll.setCreatedTimeStamp(resultSet.getTimestamp("CREATED_TS"));
					resAll.setSeqNo(resultSet.getInt("PBA_TRADE_RESTRICTION_SEQ_NO"));
					resAll.setRestriction(resultSet.getString("PBA_TRADE_RESTRICTION_DESC"));
					resAll.setCreatedUserId(resultSet.getString("CREATED_USER_PROFILE_ID"));
					allResList.add(resAll);
				}
			}
			
			String sql = SQL_PBA_RESTRICTION_HISTORY_SELECT_ADDED_DELETED;
			prpdStatement = connection.prepareStatement(sql);
			int parameterCount = 1;
			prpdStatement.setLong(parameterCount++, contractId);
			prpdStatement.setLong(parameterCount++, contractId);
			prpdStatement.setDate(parameterCount++, fromDate);
			prpdStatement.setDate(parameterCount++, toDate);
			ResultSet result = prpdStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					PBARestriction res = new PBARestriction();
					res.setCreatedTimeStamp(result.getTimestamp("CREATED_TS"));
					res.setSeqNo(result.getInt("PBA_TRADE_RESTRICTION_SEQ_NO"));
					res.setRestriction(result.getString("PBA_TRADE_RESTRICTION_DESC"));
					res.setCreatedUserId(result.getString("CREATED_USER_PROFILE_ID"));
					addedAndDeletedList.add(res);
					changeItems.add(new FeeScheduleChangeItem(result.getTimestamp("CREATED_TS"), 
							result.getString("CATEGORY"), 
							StringUtils.EMPTY,
							null, 
							result.getString("CREATED_USER_PROFILE_ID"), 
							result.getString("PBA_TRADE_RESTRICTION_DESC").length()<50 ? result.getString("PBA_TRADE_RESTRICTION_DESC") : result.getString("PBA_TRADE_RESTRICTION_DESC").substring(0, 49)));
				}
			}
			//Checking whether the Changed Res is available. If not available, add in ChangedItems
			for(PBARestriction pbaRes : allResList){
				if(!addedAndDeletedList.contains(pbaRes)){
					changeItems.add(new FeeScheduleChangeItem(pbaRes.getCreatedTimeStamp(), 
							"PBA Restriction Changed", 
							StringUtils.EMPTY,
							null, 
							pbaRes.getCreatedUserId(), 
							pbaRes.getRestriction().length()<50 ? pbaRes.getRestriction() : pbaRes.getRestriction().substring(0, 49)));
				}
			}			
			
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
			close(prpdStatement, null);
		}
		return changeItems;
	}
	
	private static List<FeeScheduleChangeItem> getContractStandardizedFeeHistory(Connection connection,
			Timestamp contractStandardizedDate,
			int contractId,
			int tpaId,
			ReportCriteria criteria,
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory,
			Map<String, ContractValidator> planProvisionValidators) throws SystemException {
		
		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> items = new LinkedList<FeeScheduleChangeItem>();
		int parameterCount = 1;
		try {
			String sql = buildContractCustomizedFeeScheduleFilterWhereClause(
					SELECT_CUSTOM_CONTRACT_STANDARDIZED_HISTORY, criteria);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setTimestamp(parameterCount++, contractStandardizedDate);
			preparedStatement.setInt(parameterCount++, contractId);
			preparedStatement.setTimestamp(parameterCount++, contractStandardizedDate);
			preparedStatement.setInt(parameterCount++, contractId);
			preparedStatement.setInt(parameterCount++, tpaId);
			preparedStatement.setInt(parameterCount++, tpaId);
			preparedStatement.setTimestamp(parameterCount++, contractStandardizedDate);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					items.add(new FeeScheduleChangeItem(
							 contractStandardizedDate, 
							 result.getString("CREATED_USER_ID"),
							 result.getString("FEE_DESC"),
							 result.getString("AMOUNT_VALUE"),
							 result.getString("AMOUNT_TYPE"),
							 Boolean.TRUE,
							 result.getString("COMMENT"),
							 result.getInt("FEE_TYPE_ORDER"),
							 false,
							 result.getString("FEE_CATEGORY_CODE"),
							 result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		
		items = filterFeesBasedOnPlanProvison(items, planProvisionHistory,
				planProvisionValidators);
		
		return items;
		
	}
	
	private static List<FeeScheduleChangeItem> getContractScheduleFirmEndSnapshot(final Connection connection,
			final int contractId,
			final int tpaFirmid,
			final Timestamp firmEndDate,
			final Timestamp date,
			ReportCriteria criteria) throws SystemException {

		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		try {
			String sql = buildContractFeeScheduleFilterWhereClause(
					SELECT_TPA_FIRM_END_CUSTOMIZED_SNAPSHOT, criteria);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, tpaFirmid);
			preparedStatement.setInt(2, contractId);
			preparedStatement.setTimestamp(3, date);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(
							 firmEndDate, 
							 result.getString("CREATED_USER_ID"),
							 result.getString("FEE_DESC"),
							 result.getString("AMOUNT_VALUE"),
							 result.getString("AMOUNT_TYPE"),
							 Boolean.FALSE,
							 result.getString("COMMENT"),
							 result.getInt("FEE_TYPE_ORDER"),
							 false,
							 result.getString("FEE_CATEGORY_CODE"),
							 result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		return changeItems;
	}
	
	private static List<FeeScheduleChangeItem> getContractScheduleFirmStartSnapshot(final Connection connection,
			final int contractId,
			final int tpaFirmid,
			final Timestamp firmStartDate,
			final Timestamp date,
			ReportCriteria criteria) throws SystemException {

		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		try {
			String sql = buildContractFeeScheduleFilterWhereClause(
					SELECT_TPA_FIRM_START_CUSTOMIZED_SNAPSHOT, criteria);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, tpaFirmid);
			preparedStatement.setInt(2, contractId);
			preparedStatement.setTimestamp(3, date);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(
							 firmStartDate, 
							 result.getString("CREATED_USER_ID"),
							 result.getString("FEE_DESC"),
							 result.getString("AMOUNT_VALUE"),
							 result.getString("AMOUNT_TYPE"),
							 Boolean.FALSE,
							 result.getString("COMMENT"),
							 result.getInt("FEE_TYPE_ORDER"),
							 false,
							 result.getString("FEE_CATEGORY_CODE"),
							 result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		return changeItems;
	}
	
	private static List<FeeScheduleChangeItem> getStandardScheduleFirmStartSnapshot(final Connection connection,
			final int contractId,
			final int tpaFirmid,
			final Timestamp date,
			ReportCriteria criteria,
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory,
			Map<String, ContractValidator> planProvisionValidators) throws SystemException {

		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		try {
			String sql = buildContractFeeScheduleFilterWhereClause(
					SELECT_TPA_FIRM_START_STANDARDIZED_SNAPSHOT, criteria);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, tpaFirmid);
			preparedStatement.setTimestamp(2, date);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(
							 date, 
							 result.getString("CREATED_USER_ID"),
							 result.getString("FEE_DESC"),
							 result.getString("AMOUNT_VALUE"),
							 result.getString("AMOUNT_TYPE"),
							 Boolean.TRUE,
							 result.getString("COMMENT"),
							 result.getInt("FEE_TYPE_ORDER"),
							 false,
							 result.getString("FEE_CATEGORY_CODE"),
							 result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		
		// filter standard schedule based on history
		changeItems = filterFeesBasedOnPlanProvison(changeItems,
				planProvisionHistory, planProvisionValidators);
		
		return changeItems;
	}
	
	private static List<FeeScheduleChangeItem> getStandardScheduleFirmEndSnapshot(final Connection connection,
			final int contractId,
			final int tpaFirmid,
			final Timestamp date,
			ReportCriteria criteria,
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory,
			Map<String, ContractValidator> planProvisionValidators) throws SystemException {

		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		try {
			String sql = buildContractFeeScheduleFilterWhereClause(
					SELECT_TPA_FIRM_END_STANDARDIZED_SNAPSHOT, criteria);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, tpaFirmid);
			preparedStatement.setTimestamp(2, date);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(
							 date, 
							 result.getString("CREATED_USER_ID"),
							 result.getString("FEE_DESC"),
							 result.getString("AMOUNT_VALUE"),
							 result.getString("AMOUNT_TYPE"),
							 Boolean.TRUE,
							 result.getString("COMMENT"),
							 result.getInt("FEE_TYPE_ORDER"),
							 false,
							 result.getString("FEE_CATEGORY_CODE"),
							 result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		
		// filter standard schedule based on history
		changeItems = filterFeesBasedOnPlanProvison(changeItems,
				planProvisionHistory, planProvisionValidators);
		
		return changeItems;
	}
	
	public static List<FeeScheduleChangeItem> getContractFirmStartDateSnapshot(final Connection connection,
			final int contractId,
			final int tpaFirmid,
			final TpaFirmDetailsPeriod firmPeriod,
			ReportCriteria criteria,
			TpaFirm firmDetails,
			List<FeeScheduleChangeItem> previousTpaSnapshot,
			final Map<String, ContractValidator> planProvisionValidators,
			final HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planHistory) throws SystemException {
		
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();
		
		List<FeeScheduleChangeItem> currentTpaSnapshot = new LinkedList<FeeScheduleChangeItem>();
		Timestamp contractFirmStartDateEntry  = firmPeriod.getContractScheduleFirmStartDateEntry();
		if(contractFirmStartDateEntry == null) {
			Timestamp tpaStandardScheduleStartDate = firmDetails.getStandardScheduleFirmStartDateEntry();
			if (tpaStandardScheduleStartDate == null
					|| tpaStandardScheduleStartDate.after(firmPeriod.getFirmStartDate())) {
				currentTpaSnapshot = new LinkedList<FeeScheduleChangeItem>();
			} else {
				currentTpaSnapshot = getStandardScheduleFirmStartSnapshot(connection, contractId, tpaFirmid,
						firmPeriod.getFirmStartDate(), criteria, planHistory, planProvisionValidators);
			}
		} else {
			currentTpaSnapshot = getContractScheduleFirmStartSnapshot(connection, contractId, tpaFirmid, firmPeriod.getFirmStartDate(), contractFirmStartDateEntry, criteria);
		}
		
		if(currentTpaSnapshot.isEmpty()
				&& previousTpaSnapshot.isEmpty()) {
			changeItems =  new LinkedList<FeeScheduleChangeItem>();
		} 
		
		if(currentTpaSnapshot.isEmpty()
				&& !previousTpaSnapshot.isEmpty()) {
			for(FeeScheduleChangeItem item : previousTpaSnapshot) {
				item.setChangedValue("0");
				item.setChangedDate(firmPeriod.getFirmStartDate());
			}
			changeItems = previousTpaSnapshot;
		}
		
		if(!currentTpaSnapshot.isEmpty()
				&& previousTpaSnapshot.isEmpty()) {
			changeItems =  currentTpaSnapshot;
		}
		
		if(!currentTpaSnapshot.isEmpty()
				&& !previousTpaSnapshot.isEmpty()) {
			changeItems = new LinkedList<FeeScheduleChangeItem>();
			nextCurrentFee : for(FeeScheduleChangeItem currentItem : currentTpaSnapshot) {
				nextPreviousFee : for(FeeScheduleChangeItem previousItem : previousTpaSnapshot) {
					if(StringUtils.equals(currentItem.getFeeSearchCase(), previousItem.getFeeSearchCase())) {
						if(StringUtils.equals(currentItem.getChangedValue(),
								previousItem.getChangedValue()) 
							&&  StringUtils.equals(currentItem.getChangedValueType(),
									previousItem.getChangedValueType())){
							continue nextCurrentFee;
						} else {
							changeItems.add(currentItem);
							continue nextCurrentFee;
						}
					} else {
						continue nextPreviousFee;
					}
				}
		      changeItems.add(currentItem);
			}
			nextPreviousFee : for(FeeScheduleChangeItem previousItem : previousTpaSnapshot) {
				nextCurrentFee : for(FeeScheduleChangeItem currentItem : currentTpaSnapshot) {
					if(StringUtils.equals(currentItem.getFeeSearchCase(), previousItem.getFeeSearchCase())) {
							continue nextPreviousFee;
						} else {
							continue nextCurrentFee;
						}
					}
			      previousItem.setChangedValue("0");
			      previousItem.setChangedDate(firmPeriod.getFirmStartDate());
			      changeItems.add(previousItem);
			}
		}
		
		// filter based on date
		Date fromDate = getFromDateFormatted(criteria);
		Date toDate = getToDateFormatted(criteria);
		List<FeeScheduleChangeItem> filteredItems = new LinkedList<FeeScheduleChangeItem>();
		for(FeeScheduleChangeItem item : changeItems) {
			if ((item.getChangedDate().compareTo(fromDate) >= 0)
					&& (item.getChangedDate().compareTo(toDate) < 0)) {
				filteredItems.add(item);
			}
		}
		
		return filteredItems;
	}
	
	public static List<FeeScheduleChangeItem> getContractFirmEndDateSnapshot(final Connection connection,
			final int contractId,
			final int tpaFirmid,
			final TpaFirmDetailsPeriod firmPeriod,
			final ReportCriteria criteria,
			final TpaFirm firmDetails,
			final Map<String, ContractValidator> planProvisionValidators,
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planHistory) throws SystemException {
		
		Timestamp contractFirmEndDateEntry  = firmPeriod.getContractScheduleFirmEndDateEntry();
		
		if(contractFirmEndDateEntry == null) {
			Timestamp tpaStandardScheduleStartDate = firmDetails.getStandardScheduleFirmStartDateEntry();
		    // if no tpa standard schedule or max and min standard schedule dates are same,
			//then dont fetch snapshot
		    if (tpaStandardScheduleStartDate == null || 
		    		firmPeriod.getFirmEndDate().after(new Timestamp(new java.util.Date().getTime()))) {
				return new LinkedList<FeeScheduleChangeItem>();
			} else {
				return getStandardScheduleFirmEndSnapshot(connection, contractId, tpaFirmid,
						firmPeriod.getFirmEndDate(), criteria, planHistory, planProvisionValidators);
			}
		} else {
			if(contractFirmEndDateEntry.after(new Timestamp(new java.util.Date().getTime()))) {
				return new LinkedList<FeeScheduleChangeItem>();
			} else {
				return getContractScheduleFirmEndSnapshot(connection, contractId, tpaFirmid,
						firmPeriod.getFirmEndDate(), contractFirmEndDateEntry, criteria);
			}
		}
	}
	
	
	private static List<FeeScheduleChangeItem> getContractCustomizedFeeHistory(Connection connection,
			Timestamp contractCustomizedDate,
			int contractId,
			int tpaId,
			ReportCriteria criteria,
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory,
			Map<String, ContractValidator> planProvisionValidators) throws SystemException {
		
		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> items = new LinkedList<FeeScheduleChangeItem>();
		int parameterCount = 1;
		try {
			String sql = buildContractCustomizedFeeScheduleFilterWhereClause(
					SELECT_CUSTOM_CONTRACT_CUSTOMIZED_HISTORY, criteria);
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setTimestamp(parameterCount++, contractCustomizedDate);
			preparedStatement.setInt(parameterCount++, contractId);
			preparedStatement.setTimestamp(parameterCount++,  contractCustomizedDate);
			preparedStatement.setInt(parameterCount++, contractId);
			preparedStatement.setInt(parameterCount++, tpaId);
			preparedStatement.setInt(parameterCount++, tpaId);
			preparedStatement.setTimestamp(parameterCount++,  contractCustomizedDate);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					items.add(new FeeScheduleChangeItem(
							 contractCustomizedDate, 
							 result.getString("CREATED_USER_ID"),
							 result.getString("FEE_DESC"),
							 result.getString("AMOUNT_VALUE"),
							 result.getString("AMOUNT_TYPE"),
							 Boolean.FALSE,
							 result.getString("COMMENT"),
							 result.getInt("FEE_TYPE_ORDER"),
							 false,
							 result.getString("FEE_CATEGORY_CODE"),
							 result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		
		items = filterFeesBasedOnPlanProvison(items, planProvisionHistory,
				planProvisionValidators);
		
		return items;
		
	}
	
	private static List<FeeScheduleChangeItem> filterCustomContractValuesForDisplay(Connection connection,
			int contractId,
			int tpaId,
			List<FeeScheduleChangeItem> changeItems,
			TpaFirmDetailsPeriod tpaFirmPeriod,
			Date fromDate,
			Date toDate,
			ReportCriteria criteria,
			Map<String, ContractValidator> planProvisionValidators,
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory) throws SystemException {
		
		List<FeeScheduleChangeItem> filteredItems = new LinkedList<FeeScheduleChangeItem>();
		
		// figure out the contract customized and subsequent standardized dates
		List<Timestamp> contractCustomizedDates = new ArrayList<Timestamp>();
		List<Timestamp> contractStandardizedDates = new ArrayList<Timestamp>();
		List<Timestamp> contractFirmChangeDate = new ArrayList<Timestamp>();
		if(tpaFirmPeriod.getContractCustomizedDate() != null) {
			contractCustomizedDates.add(tpaFirmPeriod.getContractCustomizedDate());
			for( Entry<Timestamp, Timestamp>  period : tpaFirmPeriod.getContractFeeScheduleResetPeriod().entrySet()) {
				if(period.getValue() != null) {
					contractCustomizedDates.add(period.getValue());
				}
				if(period.getKey() != null) {
					contractStandardizedDates.add(period.getKey());
				}
			}
		}
		
		// fetch history for contract standardized dates
		for (Timestamp date : contractStandardizedDates) {
			if ((date.compareTo(fromDate) >= 0)
					&& (date.compareTo(toDate) < 0)) {
				filteredItems.addAll(getContractStandardizedFeeHistory(connection, date, contractId, tpaId,
						criteria, planProvisionHistory, planProvisionValidators));
		     }
		}
	    
		// fetch history for contract customized dates
		for (Timestamp date : contractCustomizedDates) {
			if ((date.compareTo(fromDate) >= 0)
					&& (date.compareTo(toDate) < 0)) {
				// if date is a customization because of the TPA firm change, don't get history snapshot
				Timestamp contractScheduleFirmChangeEntry = tpaFirmPeriod.getContractScheduleFirmStartDateEntry();
				if(contractScheduleFirmChangeEntry == null 
						|| date.compareTo(contractScheduleFirmChangeEntry) != 0) {
				   filteredItems.addAll(getContractCustomizedFeeHistory(connection, date, contractId, tpaId,
						   criteria, planProvisionHistory, planProvisionValidators));
				}
			}
		}
        
		Timestamp contractScheduleFirmEndChangeEntry = tpaFirmPeriod.getContractScheduleFirmEndDateEntry();
		if(contractScheduleFirmEndChangeEntry != null ) {
			contractFirmChangeDate.add(contractScheduleFirmEndChangeEntry);
		}
		
		// include items which are before contract is customized 
		// or between contract reset period
		nextItem : for(FeeScheduleChangeItem item : changeItems) {
			
			item.setCustomContractEntry(true);
			
			// if this change is not within tpa firm history, go to next item
			if(!isChangeEntryWithinTheTpaFirmHistory(item.getChangedDate(), tpaFirmPeriod)) {
				continue nextItem;
			}
			
			if(contractCustomizedDates.contains(item.getChangedDate())) {
				continue nextItem;
			} else if(contractStandardizedDates.contains(item.getChangedDate())) {
			    continue nextItem;
			}  else if(contractFirmChangeDate.contains(item.getChangedDate())) {
			    continue nextItem;
			} else {
				filteredItems.add(item);
				continue nextItem;
			}
		}
		
		return filteredItems;
	}
	
	@SuppressWarnings("unchecked")
	public static List<TpaFirm> getTpaFirmHistory(ReportCriteria criteria) throws SystemException {
		if (criteria
				.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_TPA_FIRM_HISTORY) != null) {
			return (List<TpaFirm>) criteria
					.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_TPA_FIRM_HISTORY);
		}
		return null;
	}
	
	private static boolean isChangeEntryWithinTheTpaFirmHistory(Timestamp changedDate, TpaFirmDetailsPeriod firmHistory) {
		if(changedDate.after(firmHistory.getFirmStartDate()) && 
					changedDate.before(firmHistory.getFirmEndDate())){
				return true;
		}
		return false;
	}
	
	public static List<FeeScheduleChangeItem> getContractStandardScheduleFeeHistory(Connection connection,
			int contractId,
			int tpaFirmId,
			ReportCriteria criteria) throws SystemException {

		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> changeItems = new LinkedList<FeeScheduleChangeItem>();

		try {
			String sql = buildContractFeeScheduleFilterWhereClause(
					SQL_STANDARD_CONTRACT_FEE_HISTORY, criteria);
			preparedStatement = connection.prepareStatement(sql);
			
			java.sql.Date fromDate = getFromDate(criteria);
			java.sql.Date toDate = getToDate(criteria);
			
			int parameterCount = 1;

			if (logger.isDebugEnabled()) {
				logger.debug(" contractId [" + contractId + "]");
				logger.debug(" tpaFirmId [" + tpaFirmId + "]");
				logger.debug(" Fee Schedule History From Date [" + fromDate + "]");
				logger.debug(" Fee Schedule History To Date [" + toDate + "]");
			}
			
			preparedStatement.setLong(parameterCount++, tpaFirmId);
			preparedStatement.setDate(parameterCount++, fromDate);
			preparedStatement.setDate(parameterCount++, toDate);
			
			ResultSet result = preparedStatement.executeQuery();

			if (result != null) {
				while (result.next()) {
					changeItems.add(new FeeScheduleChangeItem(result
							.getTimestamp("CREATED_TS"), result
							.getString("CREATED_USER_ID"), result
							.getString("FEE_DESC"), result
							.getString("AMOUNT_VALUE"), result
							.getString("AMOUNT_TYPE"), Boolean.TRUE, result
							.getString("COMMENT"), result
							.getInt("FEE_TYPE_ORDER"),
							"Y".equals(result
									.getString("deleted_ind")) ? true : false,
											result
											.getString("FEE_CATEGORY_CODE"),
							result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		
		return changeItems;
	}
	
	public static  List<FeeScheduleChangeItem> filterStandardScheduleFeeItems(
			TpaFirmDetailsPeriod tpaFirmPeriod,
			List<FeeScheduleChangeItem> changeItems,
			final Map<String, ContractValidator> planProvisionValidators,
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory) {

		List<FeeScheduleChangeItem> filteredItems = new LinkedList<FeeScheduleChangeItem>();

		// include items which are before contract is customized
		// or between contract reset period
		nextItem: for (FeeScheduleChangeItem item : changeItems) {
			
			// if this change is not within tpa firm history, go to next item
			if(!isChangeEntryWithinTheTpaFirmHistory(item.getChangedDate(), tpaFirmPeriod)) {
				continue nextItem;
			}

			// if contract is not customized,
			if (tpaFirmPeriod.getContractCustomizedDate() == null) {
				filteredItems.add(item);
				continue nextItem;
			}
			
			if (tpaFirmPeriod.getContractScheduleFirmStartDateEntry() != null &&
					item.getChangedDate().before(tpaFirmPeriod.getContractScheduleFirmStartDateEntry())) {
				continue nextItem;
			}
			
			if (tpaFirmPeriod.getContractScheduleFirmEndDateEntry() != null &&
					item.getChangedDate().after(tpaFirmPeriod.getContractScheduleFirmEndDateEntry())) {
				continue nextItem;
			}
			
			if (item.getChangedDate().before(tpaFirmPeriod.getContractCustomizedDate())) {
				filteredItems.add(item);
				continue nextItem;
			}
			
			for (Entry<Timestamp, Timestamp> period : tpaFirmPeriod.getContractFeeScheduleResetPeriod()
					.entrySet()) {
				if (item.getChangedDate().after(period.getKey())) {
					if (period.getValue() != null) {
						if (item.getChangedDate().before(period.getValue())) {
							filteredItems.add(item);
							continue nextItem;
						}
					} else {
						filteredItems.add(item);
						continue nextItem;
					}
				}
			}
		}
		
		filteredItems = filterFeesBasedOnPlanProvison(filteredItems,
				planProvisionHistory, planProvisionValidators);
		
		return filteredItems;

	}
	
	/**
	 * Gets the Statement Start date filter as SQL Date from the report
	 * criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The FROM date.
	 */
	private static java.sql.Date getFromDate(ReportCriteria criteria) {
		java.util.Date fromDate = (java.util.Date) criteria
				.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_FROM_DATE);
		java.sql.Date returnFromDate = null;
		if (fromDate != null) {
			returnFromDate = new java.sql.Date(fromDate.getTime());
		}
		return returnFromDate;
	}
	
	/**
	 * Gets the Statement Start date filter as SQL Date from the report
	 * criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The FROM date.
	 */
	private static java.sql.Date getFromDateFormatted(ReportCriteria criteria) {
		java.sql.Date returnFromDate = null;
		java.util.Date fromDate = getFromDate(criteria);
		if (fromDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fromDate);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			returnFromDate = new java.sql.Date(calendar.getTimeInMillis());
		}
		return returnFromDate;
	}
	
	/**
	 * Gets the Statement Start date filter as SQL Date from the report
	 * criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The FROM date.
	 */
	private static java.sql.Date getToDateFormatted(ReportCriteria criteria) {
		java.sql.Date returnToDate = null;
		java.util.Date toDate = getToDate(criteria);
		if (toDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toDate);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.add(Calendar.DATE, 1);
			returnToDate = new java.sql.Date(calendar.getTimeInMillis());
		}
		return returnToDate;
	}
	
	/**
	 * Gets the Statement Start date filter as SQL Date from the report
	 * criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The FROM date.
	 */
	private static int getContractId(ReportCriteria criteria) {
		int contractId = (Integer) criteria
				.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_CONTRACT_ID);
		return contractId;
	}

	/**
	 * Gets the Statement To Date filter as SQL Date from the report criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The TO date.
	 */
	private static java.sql.Date getToDate(ReportCriteria criteria) {
		java.util.Date toDate = (java.util.Date) criteria
				.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_TO_DATE);
		java.sql.Date returnToDate = null;
		if (toDate != null) {
			returnToDate = new java.sql.Date(toDate.getTime());
		}
		return returnToDate;
	}

	private static String getFeeTypeCode(ReportCriteria criteria) {
		if (criteria
				.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE) != null) {
			return (String) criteria
					.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE);
		}
		return null;
	}

	private static Boolean getNonTpaFeeCriteria(ReportCriteria criteria) {
		if (criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_GET_NON_TPA_FEE) != null) {
			return (Boolean) criteria.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_GET_NON_TPA_FEE);
		}
		return false;
	}

	private static String buildContractFeeScheduleFilterWhereClause(String sql,
			ReportCriteria searchCriteria) {
		StringBuffer filterString = new StringBuffer();
		filterString.append(sql);

		// if searching by fee type
		if (getFeeTypeCode(searchCriteria) != null) {
			filterString.append(" WHERE FEE_SEARCH_CASE= '"
					+ getFeeTypeCode(searchCriteria) + "' ");
		}
		filterString.append(" FOR READ ONLY ");
		return filterString.toString();
	}
	
	private static String buildContractCustomizedFeeScheduleFilterWhereClause(String sql,
			ReportCriteria searchCriteria) {
		StringBuffer filterString = new StringBuffer();
		filterString.append(sql);
		
		String feeSearchCriteria = getFeeTypeCode(searchCriteria);

		// if searching by fee type
		if (feeSearchCriteria != null) {
			filterString.append(" AND ( CV.FEE_SEARCH_CASE= '"+ feeSearchCriteria 
					         + "' OR  SV.FEE_SEARCH_CASE= '"+ feeSearchCriteria + "' ) ");
		}
		filterString.append(" FOR READ ONLY ");
		return filterString.toString();
	}

	private static void compareDesignatedInvestmentManager(
			DesignatedInvestmentManager latestRow,
			DesignatedInvestmentManager previousRow,
			HashMap<String, String> fields,
			List<FeeScheduleChangeItem> changeItems)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(latestRow.getClass());
		Set<String> names = fields.keySet();
		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (names.contains(prop.getName())) {
				Method getter = prop.getReadMethod();
				Object newValue = getter.invoke(latestRow);
				Object oldValue = getter.invoke(previousRow);
				if (newValue == oldValue
						|| (newValue != null && newValue.equals(oldValue))) {
					continue;
				}
				changeItems.add(new FeeScheduleChangeItem(latestRow
						.getCreatedTimeStamp(), latestRow.getCreatedUserId(),
						fields.get(prop.getName()), String.valueOf(newValue),
						StringUtils.EMPTY));
			}
		}
	}
	
	
	private static void comparePersonalBrokerageAccount(
			PersonalBrokerageAccount latestRow,
			PersonalBrokerageAccount previousRow,
			HashMap<String, String> fields,
			List<FeeScheduleChangeItem> changeItems)
			throws IntrospectionException, IllegalAccessException,
			InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(latestRow.getClass());
		Set<String> names = fields.keySet();
		for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
			if (names.contains(prop.getName())) {
				Method getter = prop.getReadMethod();
				Object newValue = getter.invoke(latestRow);
				Object oldValue = getter.invoke(previousRow);
				if (newValue == oldValue
						|| (newValue != null && newValue.equals(oldValue))) {
					continue;
				}
				changeItems.add(new FeeScheduleChangeItem(latestRow
						.getCreatedTimeStamp(), latestRow.getCreatedUserId(),
						fields.get(prop.getName()), String.valueOf(newValue),
						StringUtils.EMPTY));
			}
		}
	}

	/**
	 * Method to retrieve standard fee changes history records.
	 * 
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 */
	public static ReportData getReportData(ReportCriteria reportCriteria,
			TpaStandardFeeScheduleChangeHistoryReportData reportData)
			throws SystemException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		Map<String, String> standardFeeTypes = new LinkedHashMap<String, String>();
		Map<String, String> nonStandardFeeTypes = new LinkedHashMap<String, String>();

		try {
			connection = getReadUncommittedConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);

			loadTpaFeeTypes(reportCriteria, connection, standardFeeTypes,
					nonStandardFeeTypes);
			reportData.setStandardFeeTypes(standardFeeTypes);
			reportData.setNonStandardFeeTypes(nonStandardFeeTypes);

			// get the total record count, used for paging
			int totalCount = getTotalRecordCount(connection, reportCriteria,
					reportData);
			reportData.setTotalCount(totalCount);

			statement = getPreparedStatement(connection, reportCriteria,
					reportData, false);

			statement.execute();
			result = statement.getResultSet();
			return getReportData(reportCriteria, connection, reportData, result);

		} catch (SQLException exception) {
			logger.error("TPAFeeScheduleDAO.getReportData method Failed.");
			throw new SystemException(exception,
					"Failed in TPAFeeScheduleDAO.getReportData");
		} finally {
			close(statement, connection);
		}

	}

	/**
	 * Gets the TpaFirmId filter from criteria.
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The tpaFirmId.
	 */
	private static int getTpaFirmId(ReportCriteria criteria) {
		int tpaFirmId = (Integer) criteria
				.getFilterValue(TpaStandardFeeScheduleChangeHistoryReportData.FILTER_TPA_FIRM_ID);
		return tpaFirmId;
	}

	/**
	 * Method to retrieve standard fee changes history records.
	 * 
	 * @param criteria
	 * @param connection
	 * @param reportData
	 * @param resultSet
	 * @return reportData
	 * @throws SystemException
	 */
	private static ReportData getReportData(ReportCriteria reportCriteria,
			Connection connection,
			TpaStandardFeeScheduleChangeHistoryReportData reportData,
			ResultSet result) throws SQLException, SystemException {

		List<FeeScheduleChangeItem> changeItems = new ArrayList<FeeScheduleChangeItem>();
		List<FeeScheduleChangeItem> dataItems = new ArrayList<FeeScheduleChangeItem>();

		Map<String, String> userNames = new HashMap<String, String>();

		while (result.next()) {
			String feeCategoryCode = StringUtils.trimToEmpty(result
					.getString("fee_category_code"));
			changeItems.add(new FeeScheduleChangeItem(result
					.getTimestamp("CREATED_TS"),
					"TP".equals(feeCategoryCode) ? result
							.getString("FEE_DESCRIPTION") : result
							.getString("non_std_fee_description"), result
							.getString("AMOUNT_VALUE"), result
							.getString("AMOUNT_TYPE"), null, result
							.getString("COMMENT"), result
							.getString("CREATED_USER"), result
							.getInt("FEE_TYPE_ORDER"),
							"Y".equals(result
									.getString("deleted_ind")) ? true : false, feeCategoryCode));
		}
		loadUserNames(reportCriteria, connection, userNames);

		dataItems.addAll(getSortedReportItems(reportCriteria, changeItems));
		reportData.setDetails(dataItems);
		reportData.setUserNames(userNames);

		return reportData;
	}

	private static void loadUserNames(ReportCriteria reportCriteria,
			Connection connection, Map<String, String> userNames)
			throws SystemException, SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;

		connection = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
		statement = connection.prepareStatement(SELECT_USER_NAMES_FOR_DROPDOWN);
		statement.setInt(1, getTpaFirmId(reportCriteria));
		statement.execute();
		result = statement.getResultSet();
		String firstName;
		String lastName;

		while (result.next()) {
			firstName = StringUtils.trimToEmpty(result.getString("FIRST_NAME"));
			lastName = StringUtils.trimToEmpty(result.getString("LAST_NAME"));
			userNames.put(firstName.concat(",".concat(lastName)), firstName
					.concat(" ".concat(lastName)));
		}
	}

	private static void loadTpaFeeTypes(ReportCriteria reportCriteria,
			Connection connection, Map<String, String> standardFeeTypes,
			Map<String, String> nonStandardFeeTypes) throws SystemException,
			SQLException {
		PreparedStatement statement = null;
		ResultSet result = null;

		connection = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
		statement = connection.prepareStatement(SELECT_TPA_FEES_FOR_DROPDOWN);
		statement.setInt(1, getTpaFirmId(reportCriteria));
		statement.execute();
		result = statement.getResultSet();

		while (result.next()) {
			String feeCategoryCode = StringUtils.trimToEmpty(result
					.getString("fee_category_code"));

			if (feeCategoryCode.equals("TP")) {
				standardFeeTypes.put(result.getString("fee_code"), result
						.getString("fee_description"));

			} else if (feeCategoryCode.equals("TN")) {

				nonStandardFeeTypes.put(result.getString("fee_code"), result
						.getString("fee_description"));
			}
		}
	}

	/**
	 * Gets the total record count (number of Participants in a contract who can
	 * acess Participant Statements) for the given report criteria.
	 * 
	 * @param connection
	 *            The Connection object to use to obtain the total record count.
	 * @param criteria
	 *            The ReportCriteria to use.
	 * @return The total record count.
	 * @throws SQLException
	 * @throws SystemException
	 */
	private static int getTotalRecordCount(Connection connection,
			ReportCriteria criteria,
			TpaStandardFeeScheduleChangeHistoryReportData reportData)
			throws SQLException, SystemException {

		PreparedStatement stmt = null;
		int totalCount = 0;
		try {

			// Prepare a statement for counting
			stmt = getPreparedStatement(connection, criteria, reportData, true);
			ResultSet rs = stmt.executeQuery();

			/*
			 * Typically, we only have one record but in the case when we have a
			 * UNION of all types, we will have multiple records. We should add
			 * them all up to get the total record count.
			 */
			if (rs != null && rs.next()) {
				totalCount += rs.getInt(1);
			}

		} finally {
			close(stmt, null);
		}

		return totalCount;
	}

	/**
	 * 1. Based on the countOnly indicator, creates the a. SQL to get summary
	 * data for all the participants in a contract to view statements OR b. SQL
	 * to get the total number of participants in a contract to view statements
	 * 
	 * 2. Adds the ORDER BY clause if applicable
	 * 
	 * 3. Creates a PreparedStatment
	 * 
	 * 4. Sets all the input parameters to the SQL
	 * 
	 * 5. Returns the preparedStatement
	 * 
	 * @param connection
	 *            The connection object to use to prepare the statement
	 * @param criteria
	 *            The report criteria used to setup the WHERE clause and the
	 *            ORDER by clause.
	 * @param countOnly
	 *            If true, returns a statement that only counts the number of
	 *            records. If false, the complete result set is returned.
	 * @throws SQLException
	 *             If it fails to prepare the statement.
	 * 
	 * @return PreparedStatement The newly prepared statement.
	 */
	private static PreparedStatement getPreparedStatement(
			Connection connection, ReportCriteria criteria,
			TpaStandardFeeScheduleChangeHistoryReportData reportData,
			boolean countOnly) throws SQLException, SystemException {

		StringBuffer query = new StringBuffer();
		SqlPair pair = null;

		pair = getSqlPair(createTpaStdHistoryQuery(criteria, reportData));

		if (countOnly) {
			query.append(pair.getCountQuery());
		} else {
			query.append(pair.getQuery());
		}

		/*
		 * // append the ORDER BY clause when there is any sorting requirement.
		 * if (!countOnly && criteria.getSorts().size() > 0) {
		 * query.append(" ORDER BY ").append(
		 * criteria.getSortPhrase(fieldToColumnMap)); }
		 */
		query.append(" FOR FETCH only ");

		PreparedStatement stmt = connection.prepareStatement(query.toString());

		// set filter parameters into the WHERE clause.
		setStandardFeeScheduleParameters(stmt, criteria);

		return stmt;
	}

	/**
	 * This method returns SQL pairs for the given SQL statement. 1. SQL to get
	 * the all the data 2. SQL to get the count of records
	 * 
	 * @param sql
	 *            Actual Sql statement
	 * @return SqlPair object for the given SQL
	 * 
	 */
	private static SqlPair getSqlPair(String query) {

		String countQuery = new SqlStringBuffer(query).append(
				" SELECT COUNT(*) FROM BASE ").toString();
		String baseQuery = new SqlStringBuffer(query).append(
				" SELECT * FROM BASE ").toString();

		return new SqlPair(countQuery, baseQuery);
	}

	private static String createTpaStdHistoryQuery(ReportCriteria criteria,
			TpaStandardFeeScheduleChangeHistoryReportData reportData) {

		StringBuffer query = new StringBuffer(
				SQL_SELECT_STANDARD_TPA_FEE_HISTORY);
		query.append(buildStandardFeeScheduleFilterWhereClause(criteria,
				reportData));
		query.append(")");

		return query.toString();
	}

	/**
	 * Obtains the report item list from the given result set. It uses the
	 * report criteria to determine the page number and the size of data to
	 * retrieve. A report item transformer must be provided to transform a
	 * record in the result set into a report item.
	 * 
	 * @param criteria
	 *            The report criteria to use.
	 * @param rs
	 *            The complete result set.
	 * @param transformer
	 *            The transformer that transforms a record into a report item.
	 * @return The report items list.
	 * @throws SystemException
	 */
	public static List<FeeScheduleChangeItem> getSortedReportItems(
			ReportCriteria criteria, List<FeeScheduleChangeItem> dataItemsInitial)
			throws SystemException {

		List<FeeScheduleChangeItem> dataItems = new ArrayList<FeeScheduleChangeItem>();

		int startIndex = criteria.getStartIndex();

		List<FeeScheduleChangeItem> defaultDateList = new ArrayList<FeeScheduleChangeItem>();

		defaultDateList.addAll(dataItemsInitial);

		/*
		 * Sorting.
		 */
		sort(defaultDateList, criteria.getSorts());

		/*
		 * Move result set to the proper location. Start index begins at 1.
		 */
		int transactionCount = 1;
		for (; transactionCount < startIndex
				&& transactionCount < defaultDateList.size(); transactionCount++)
			;

		/*
		 * ResultSet ends before start index...
		 */
		/*
		 * if (transactionCount < startIndex) { String errorString =
		 * "Invalid report criteria start index [" + startIndex +
		 * "] result set size [" + (transactionCount - 1) + "]"; throw new
		 * SystemException(new IllegalArgumentException( errorString),
		 * errorString); }
		 */

		boolean limitPageSize = true;

		int recordCount = 0;
		int pageSize = criteria.getPageSize();
		if (pageSize == ReportCriteria.NOLIMIT_PAGE_SIZE) {
			limitPageSize = false;
		}

		/*
		 * Retrieve one page worth of data.
		 */
		while ((!limitPageSize || recordCount < pageSize)
				&& transactionCount <= defaultDateList.size()) {
			FeeScheduleChangeItem item = defaultDateList
					.get(transactionCount - 1);
			dataItems.add(item);
			recordCount++;
			transactionCount++;
		}

		return dataItems;
	}

	/**
	 * Sort the list according to the required sort order.
	 * 
	 * @param items
	 *            the list to sort
	 * @param sorts
	 *            the sorting fields list
	 */
	private static void sort(List<FeeScheduleChangeItem> items,
			ReportSortList sorts) {
		if (items != null && sorts != null && sorts.size() != 0) {
			ReportSort firstSort = (ReportSort) sorts.get(0);
			Collections.sort(items, FeeScheduleChangeItem.getComparator(
					firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}

	/**
	 * Method to build where clause for report sql
	 * 
	 * @param criteria
	 * @return String
	 */
	private static String buildStandardFeeScheduleFilterWhereClause(
			ReportCriteria reportCriteria,
			TpaStandardFeeScheduleChangeHistoryReportData reportData) {
		StringBuffer sb = new StringBuffer();
		Map<String, String> standardFeeTypes = reportData.getStandardFeeTypes();

		if (StringUtils
				.isNotBlank((String) reportCriteria
						.getFilterValue(TpaStandardFeeScheduleChangeHistoryReportData.FILTER_USER_NAME))) {
			String UserName = (String) reportCriteria
					.getFilterValue(TpaStandardFeeScheduleChangeHistoryReportData.FILTER_USER_NAME);

			String firstName = StringUtils.EMPTY;
			String lastName = StringUtils.EMPTY;

			String[] names = UserName.split(",");
			if (names.length == 2) {
				firstName = names[0];
				lastName = names[1];
			}
			sb.append(" and up.FIRST_NAME= " + wrapInSingleQuotes(firstName)
					+ " and up.LAST_NAME= " + wrapInSingleQuotes(lastName)
					+ " ");
		}

		if (StringUtils
				.isNotBlank((String) reportCriteria
						.getFilterValue(TpaStandardFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE))) {
			String feeCode = (String) reportCriteria
					.getFilterValue(TpaStandardFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE);

			if (standardFeeTypes.containsKey(feeCode)) {
				sb
						.append(" and tsfh.FEE_CODE = '"
								+ (String) reportCriteria
										.getFilterValue(TpaStandardFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE)
								+ "' ");
			} else {
				sb
						.append(" and tsfh.non_std_fee_description = '"
								+ (String) reportCriteria
										.getFilterValue(TpaStandardFeeScheduleChangeHistoryReportData.FILTER_FEE_TYPE)
								+ "' ");
			}
		}
		return sb.toString();
	}

	/**
	 * Method to pass the required input parameters to the SQL
	 * 
	 * @param stmt
	 * @param criteria
	 * @throws SQLException
	 * @throws SystemException
	 */
	private static void setStandardFeeScheduleParameters(
			PreparedStatement stmt, ReportCriteria criteria)
			throws SQLException, SystemException {

		java.sql.Date fromDate = getFromDate(criteria);
		java.sql.Date toDate = getToDate(criteria);
		int tpaFirmId = getTpaFirmId(criteria);
		int parameterCount = 1;

		if (logger.isDebugEnabled()) {
			logger.debug(" tpaFirmId [" + tpaFirmId + "]");
			logger.debug(" Fee Schedule History From Date [" + fromDate + "]");
			logger.debug(" Fee Schedule History To Date [" + toDate + "]");
		}
		stmt.setInt(parameterCount++, tpaFirmId);
		stmt.setDate(parameterCount++, fromDate);
		stmt.setDate(parameterCount++, toDate);

	}
	
	private static Map<String, ContractValidator> getPlanProvisionValidators(final Connection connection)
			throws SystemException {
		PreparedStatement preparedStatement = null;
		Map<String, ContractValidator> validators = new HashMap<String, ContractValidator>();
		try {
			preparedStatement = connection.prepareStatement(SELECT_PLAN_PROVISION_VALIDATORS);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					String feeCode = result.getString("fee_code");
					String className = result.getString("validator_class_name");
					ContractValidator validator = ContractValidatorFactory.getInstance().getContractValidatorInstance(className);
					validators.put(feeCode, validator);
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		return validators;
	}
	
	private static List<FeeScheduleChangeItem> filterFeesBasedOnPlanProvison(final List<FeeScheduleChangeItem> changeItems,
			final HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory,
			final Map<String, ContractValidator> planProvisionValidators) {
		List<FeeScheduleChangeItem> filteredItems = new LinkedList<FeeScheduleChangeItem>();
		for(FeeScheduleChangeItem item : changeItems) {
			ContractValidator validator = planProvisionValidators.get(item.getFeeSearchCase());
			if(validator == null) {
				filteredItems.add(item);
			} else {
				TreeMap<Timestamp, Timestamp> history = planProvisionHistory.get(validator.getValidatorCode());
				if(isChangeEntryWithinThePlanProvisionHistory(item.getChangedDate(), history)) {
					filteredItems.add(item);
				}
			}
		}
		return filteredItems;
	}
	
	private static List<FeeScheduleChangeItem> findPlanProvisionsChangesWithinTpaHistory(
			final Connection connection,
			final int contractId,
			final int tpaFirmId,
			final ReportCriteria criteria,
			final TpaFirm tpaFirmPeriod,
			final HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory,
			final Map<ContractValidationDetail, List<String>> planProvisionValidators) throws SystemException {
		List<FeeScheduleChangeItem> newItems = new LinkedList<FeeScheduleChangeItem>();
		for(Entry<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> item : planProvisionHistory.entrySet()) {
			ContractValidationDetail validator =  item.getKey();
			for( Entry<Timestamp, Timestamp> range: item.getValue().entrySet()) {
				if(isChangeEntryWithinTheTpaStandardScheduleHistory(range.getKey(), tpaFirmPeriod)) {
					newItems.addAll(getTpaStandardFeeOnPlanProvisionChange(connection, contractId, tpaFirmId,
							range.getKey(),
							null,
							planProvisionValidators.get(validator),
							criteria));
				}
				if(isChangeEntryWithinTheTpaStandardScheduleHistory(range.getValue(), tpaFirmPeriod)) {
					newItems.addAll(getTpaStandardFeeOnPlanProvisionChange(connection, contractId, tpaFirmId,
							null,
							range.getValue(),
							planProvisionValidators.get(validator),
							criteria));
				}
			}
			
		}
		
		// filter based on date
		Date fromDate = getFromDateFormatted(criteria);
		Date toDate = getToDateFormatted(criteria);
		List<FeeScheduleChangeItem> filteredItems = new LinkedList<FeeScheduleChangeItem>();
		for(FeeScheduleChangeItem item : newItems) {
			if ((item.getChangedDate().compareTo(fromDate) >= 0)
					&& (item.getChangedDate().compareTo(toDate) < 0)) {
				filteredItems.add(item);
			}
		}
		
		return filteredItems;
	}
	
	private static boolean isChangeEntryWithinThePlanProvisionHistory(
			final Timestamp changedDate,
			final TreeMap<Timestamp, Timestamp> planHistory) {
		for(Entry<Timestamp, Timestamp>  history : planHistory.entrySet()) {
			if(changedDate.after(history.getKey()) && 
					(history.getValue() == null || changedDate.before(history.getValue()))){
				return true;
		     }
		}
		return false;
	}
	
	private static boolean isChangeEntryWithinTheTpaStandardScheduleHistory(Timestamp changedDate,
			TpaFirm firmHistory) {
		if(changedDate == null
				|| firmHistory.getStandardScheduleFirmStartDateEntry() == null) {
			return false;
		}
		if(changedDate.after(firmHistory.getStandardScheduleFirmStartDateEntry())) {
			for (TpaFirmDetailsPeriod period : firmHistory.getTpaFirmPeriodHistory()) {
				if (changedDate.after(period.getFirmStartDate())
						&& changedDate.before(period.getFirmEndDate())) {
					return true;
				}
			}
	    }
		return false;
	}
	
	private static List<FeeScheduleChangeItem> getTpaStandardFeeOnPlanProvisionChange(final Connection connection,
			final int contractId,
			final int tpaFirmId,
			final Timestamp planProvisionStartDate,
			final Timestamp planProvisionEndDate,
			final List<String> feeCodes,
			final ReportCriteria criteria) throws SystemException {
		PreparedStatement preparedStatement = null;
		List<FeeScheduleChangeItem> items = new LinkedList<FeeScheduleChangeItem>();
		int parameterCount = 1;
		Timestamp changeDate = null;
		if(planProvisionStartDate != null) {
			changeDate = planProvisionStartDate;
		} else {
			changeDate = planProvisionEndDate;
		}
		
		try {
			StringBuilder selectColumns = new StringBuilder();
			for (String feeCode : feeCodes) {
				selectColumns.append("'").append(feeCode).append("'").append(", ");
			}
			if (selectColumns.length() > 2) {
				selectColumns.delete(selectColumns.length() - ", ".length(), selectColumns.length());
			}
			String query = SELECT_APPLICALBE_TPA_STANDARD_FEE.replace("{0}", selectColumns.toString());
			query = buildContractFeeScheduleFilterWhereClause(query, criteria);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(parameterCount++, tpaFirmId);
			preparedStatement.setTimestamp(parameterCount++, changeDate);
			preparedStatement.setInt(parameterCount++, contractId);
			preparedStatement.setInt(parameterCount++, tpaFirmId);
			preparedStatement.setTimestamp(parameterCount++, changeDate);
			ResultSet result = preparedStatement.executeQuery();
			if (result != null) {
				while (result.next()) {
					items.add(new FeeScheduleChangeItem(
							 changeDate, 
							 result.getString("CREATED_USER_ID"),
							 result.getString("FEE_DESC"),
							 planProvisionStartDate != null ? result.getString("AMOUNT_VALUE") : "",
							 result.getString("AMOUNT_TYPE"),
							 Boolean.TRUE,
							 planProvisionStartDate != null ? result.getString("COMMENT") : "Plan provision turned off",
							 result.getInt("FEE_TYPE_ORDER"),
							 false,
							 result.getString("FEE_CATEGORY_CODE"),
							 result.getString("FEE_SEARCH_CASE")));
				}
			}
		} catch (SQLException e) {
			throw new SystemException(e, e.getMessage());
		} finally {
			close(preparedStatement, null);
		}
		return items;
	}
	
	
	@SuppressWarnings("unchecked")
	private static HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>>
	               getPlanProvisionHistory(ReportCriteria criteria) {
		HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisionHistory = 
				(HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>>) criteria
				.getFilterValue(ContractFeeScheduleChangeHistoryReportData.FILTER_PLAN_PROVISION_HISTORY);
		return planProvisionHistory;
	}
}
