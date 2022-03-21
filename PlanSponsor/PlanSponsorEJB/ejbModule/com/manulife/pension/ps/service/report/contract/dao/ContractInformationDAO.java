package com.manulife.pension.ps.service.report.contract.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectMultiFieldMultiRowQueryHandler;
import com.intware.dao.jdbc.SelectMultiFieldQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.contract.valueobject.ContractBrokerShareInformationVO;
import com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData;
import com.manulife.pension.service.contract.managedaccount.ManagedAccountServiceFeatureLite;
import com.manulife.pension.service.contract.util.CoFidPlanReviewHelper;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.util.SmallPlanFeature;
import com.manulife.pension.service.contract.valueobject.CoFiduciaryVO;
import com.manulife.pension.service.contract.valueobject.ContactVO;
import com.manulife.pension.service.contract.valueobject.ContractProfileVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.contract.valueobject.ContractSnapshotVO;
import com.manulife.pension.service.contract.valueobject.ContractSummaryVO;
import com.manulife.pension.service.contract.valueobject.DefaultInvestmentFundVO;
import com.manulife.pension.service.contract.valueobject.MoneySourceVO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.contract.valueobject.PlanAssetsVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.environment.EnvironmentServiceHelper;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.fund.valueobject.FundVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.security.dao.ProfileDAO;
import com.manulife.pension.service.security.dao.SearchDAO;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.util.ldap.LDAPUser;
import com.manulife.util.ldap.LdapDao;

public class ContractInformationDAO extends BaseDatabaseDAO {

    private static final Logger logger = Logger.getLogger(ContractInformationDAO.class);

    private static final String className = ContractInformationDAO.class.getName();
    
    private static final String CO_FID_FEATURE_CONSTANT = "Wilshire 3(21) Adviser Service";
    
    private static final String VF100_SCHEMA ="VF100.";

    private static final String SELECT_CONTRACT_FUNDS = "call " + VF100_SCHEMA
    + "SELECT_CONTRACT_FUNDS(?)";

    private static final String GET_DEFAULT_INVESTMENTS_LIST = "call " + VF100_SCHEMA
    + "GET_CONTRACT_DEFAULT_INVESTMENTS(?)";
    
	private static final String SQL_SELECT_ASOFDATE = " SELECT  "
			+ " CYCLE_DATE AS AS_OF_DATE " + "FROM " + PLAN_SPONSOR_SCHEMA_NAME
			+ " RF_RUNDATE_CS " + "WHERE " + " CYCLE_ID = 'RUNDATE' "
			+ " AND BUSINESS_UNIT = 'PS' FOR FETCH ONLY ";

	private static final String SQL_SELECT_TOTAL_PLAN_ASSETS_AMOUNT = " SELECT  "
			+ " (CCB.GTD_FUND_BALANCE_AMT + "
			+ " CCB.POOLED_FUND_BALANCE_AMT + "
			+ " CCB.LSA_BALANCE_AMT + "
			+ " CCB.PBA_BALANCE_AMT + "
			+ " CCB.UNINVESTED_AMT + "
			+ " (COALESCE(CCA.BALANCE_AMT,0)) ) AS TOTAL_PLAN_ASSETS_AMOUNT, "
			+ " (CCB.GTD_FUND_BALANCE_AMT + "
			+ " CCB.POOLED_FUND_BALANCE_AMT ) AS ALLOCATED_ASSETS_AMOUNT, "
			+ " CCB.PBA_BALANCE_AMT AS PBA_BALANCE_AMT, "
			+ " CCB.UNINVESTED_AMT AS UNINVESTED_AMT, "
			+ " COALESCE(CCA.BALANCE_AMT,0) PENDING_TRANSACTIONS "
			+ " FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "CONTRACT_CURR_BALANCE CCB ,"
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ "CONTRACT_CS CCS LEFT OUTER JOIN "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " CLIENT_CASH_ACCT_CURR_BALANCE CCA ON (CCS.CLIENT_ID = CCA.CLIENT_ID ) "
			+ " WHERE "
			+ " CCB.CONTRACT_ID = ? "
			+ " AND CCS.CONTRACT_ID = CCB.CONTRACT_ID ";

	private static final String SQL_SELECT_CASHACCOUNT_AMOUNT = " SELECT  "
			+ " BALANCE_AMT AS CASH_ACCOUNT_AMOUNT " + " FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME + " CLIENT_CASH_ACCT_CURR_BALANCE CCA, "
			+ PLAN_SPONSOR_SCHEMA_NAME + " CONTRACT_CS C " + " WHERE "
			+ " C.CONTRACT_ID = ? " + " AND C.CLIENT_ID = CCA.CLIENT_ID ";

	private static final String SQL_SELECT_LOAN_ASSETS = " SELECT  "
			+ " LSA_BALANCE_AMT AS LOAN_ASSET " + "FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME + " CONTRACT_CURR_BALANCE " + "WHERE "
			+ " CONTRACT_ID = ? ";

	private static final String SQL_SELECT_CONTRACT_MONEY_TYPE = " SELECT "
			+ " MONEY_TYPE_ID, " + " CONTRACT_MONEY_TYPE_SHORT_NAME, "
			+ " CONTRACT_MONEY_TYPE_MED_NAME, "
			+ " CONTRACT_MONEY_TYPE_LONG_NAME " + " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_MONEY_TYPE WHERE CONTRACT_ID=? ";

	private static final String SQL_SELECT_CONTRACT_MONEY_SOURCE = " SELECT "
			+ " MONEY_SOURCE_ID, " + " CONTRACT_MONEY_SRC_SHORT_NAME, "
			+ " CONTRACT_MONEY_SRC_MED_NAME, "
			+ " CONTRACT_MONEY_SRC_LONG_NAME " + " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_MONEY_SOURCE WHERE CONTRACT_ID=? ";

	private static final String SQL_SELECT_PRODUCT_FEATURES = " (SELECT  "
			+ "	CPF.PRODUCT_FEATURE_TYPE_CODE "
			+ " , PFT.PRODUCT_FEATURE_TYPE_DESC " + " FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " PRODUCT_FEATURE_TYPE PFT, "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " CONTRACT_PRODUCT_FEATURE CPF "
			+ "WHERE "
			+ " CPF.CONTRACT_ID = ? "
			+ " AND CPF.PRODUCT_FEATURE_TYPE_CODE = PFT.PRODUCT_FEATURE_TYPE_CODE "
			+ " AND ? >= CPF.EFFECTIVE_DATE "
			+ " AND ? <= '9999-12-31' "
			+ " AND NOT (CPF.PRODUCT_FEATURE_TYPE_CODE LIKE 'RB%' "
			+ " OR  CPF.PRODUCT_FEATURE_TYPE_CODE LIKE 'SF%' "
			+ " OR  CPF.PRODUCT_FEATURE_TYPE_CODE LIKE 'PMO%' "
			+ " OR  CPF.PRODUCT_FEATURE_TYPE_CODE LIKE 'PST%' "
			+ " OR  CPF.PRODUCT_FEATURE_TYPE_CODE LIKE 'PDC%' "
			+ " OR  CPF.PRODUCT_FEATURE_TYPE_CODE LIKE 'GTW%' ) "
			+ " ) "
			+ " UNION ALL "
			+ " SELECT SERVICE_FEATURE_ATTR_CODE PRODUCT_FEATURE_TYPE_CODE,' ' PRODUCT_FEATURE_TYPE_DESC "
			+ " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_SERVICE_FEATURE_ATTR "
			+ " WHERE SERVICE_FEATURE_CODE='VP' AND "
			+ " SERVICE_FEATURE_ATTR_CODE='VPS' AND "
			+ " SERVICE_FEATURE_ATTR_VALUE='Y' AND "
			+ " CONTRACT_ID=? "
			+ " UNION ALL "
			+ " SELECT "
			+ " CASE  WHEN C.PERSONAL_BROKERAGE_ACCOUNT_IND = 'Y' THEN 'PBA' ELSE NULL "
			+ " END AS PRODUCT_FEATURE_TYPE_CODE, "
			+ " CASE WHEN C.PERSONAL_BROKERAGE_ACCOUNT_IND = 'Y' THEN  'Personal Brokerage Account (PBA)' ELSE NULL END PRODUCT_FEATURE_TYPE_DESC "
			+ " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_CS C "
			+ " WHERE "
			+ " C.CONTRACT_ID = ? AND "
			+ " CASE  WHEN C.PERSONAL_BROKERAGE_ACCOUNT_IND = 'Y' THEN 'PBA' ELSE NULL "
			+ " END IS NOT NULL ";

	private static final String SQL_SELECT_ACCESS_CHANNELS = " SELECT "
			+ " CONTRACT_SERVICE_FEATURE_CODE,' ' " + " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_APOLLO_SERVICE_FEATURE "
			+ " WHERE END_DATE > CURRENT DATE "
			+ " AND CONTRACT_SERVICE_FEATURE_CODE IN ('IIQ', 'WIQ') "
			+ " AND CONTRACT_ID=? "
			+ " AND ? BETWEEN EFFECTIVE_DATE AND END_DATE "
			+ " UNION ALL "
			+ " SELECT "
			+ " PRODUCT_FEATURE_TYPE_CODE CONTRACT_SERVICE_FEATURE_CODE, ' ' "
			+ " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_PRODUCT_FEATURE "
			+ " WHERE  "
			+ "	PRODUCT_FEATURE_TYPE_CODE='LRK01' "
			+ " AND CONTRACT_ID=? "
			+ " AND ? BETWEEN EFFECTIVE_DATE AND END_DATE "
			+ " UNION ALL "
			+ " SELECT "
			+ " SERVICE_FEATURE_CODE CONTRACT_SERVICE_FEATURE_CODE ,SERVICE_FEATURE_VALUE "
			+ " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_SERVICE_FEATURE "
			+ " WHERE SERVICE_FEATURE_CODE='PIL' " + " AND CONTRACT_ID=? ";

	private static final String SQL_SELECT_PRODUCT_FEATURES1 = " SELECT "
			+ " CPF.PRODUCT_FEATURE_TYPE_CODE, PFT.PRODUCT_FEATURE_TYPE_DESC "
			+ " FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " CONTRACT_PRODUCT_FEATURE CPF"
			+ " LEFT OUTER JOIN PSW100.PRODUCT_FEATURE_TYPE PFT ON PFT.PRODUCT_FEATURE_TYPE_CODE=CPF.PRODUCT_FEATURE_TYPE_CODE "
			+ " WHERE CPF.PRODUCT_FEATURE_TYPE_CODE IN ( "
			+ " 'PSTS', "
			+ // statement type summary
			"  'PSTD', "
			+ // statement type detail
			"     'RB AC', "
			+ // basis cash
			"     'RB CA', "
			+ // basis accured
			"     'RB CO', "
			+ // basis combo
			"     'PMOBU', "
			+ // delivery method bulk
			"     'PMOOP', "
			+ // delivery method open envelope
			"     'PMODR') "
			+ // delivery method direct
			" AND ? BETWEEN EFFECTIVE_DATE AND END_DATE"
			+ " AND CONTRACT_ID = ? "
			+ " UNION ALL "
			+ " SELECT CPF1.PRODUCT_FEATURE_TYPE_CODE, "
			+ " ' ' PRODUCT_FEATURE_TYPE_DESC "
			+ " FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " CONTRACT_PRODUCT_FEATURE CPF1 WHERE "
			+ " CPF1.PRODUCT_FEATURE_TYPE_CODE IN ( "
			+ " 'PDCD', "
			+ " 'PDCS', " + " 'PDCN') " + " AND CPF1.CONTRACT_ID = ? ";

	private static final String SQL_SELECT_CONTTRACT_INFO = " SELECT "
			+ " CN.CONTRACT_ID, "
			+ " CN.CONTRACT_NAME, "
			+ " CA.ADDR_LINE1, "
			+ " CA.ADDR_LINE2, "
			+ " CA.CITY_NAME, "
			+ " CA.STATE_CODE, "
			+ " CA.ZIP_CODE, "
			+ " CA.COUNTRY_NAME, "
			+ " CAR.CLIENT_ACCOUNT_REP_NAME, "
			+ " CN.EFFECTIVE_DATE, "
			+ " P.PLAN_YEAR_END, "
			+ " CN.CONTRACT_ACCESS_CODE, "
			+ " CN.INTER_ACCT_TRANSFER_1_DATE, "
			+ " CN.INTER_ACCT_TRANSFER_2_DATE, "
			+ " CN.INTER_ACCT_TRANSFER_3_DATE, "
			+ " CN.INTER_ACCT_TRANSFER_4_DATE, "
			+ " CN.DIRECT_DEBIT_BANKING_IND, "
			+ " C.CONTACT_NAME, "
			+ " C.CONTACT_PHONE_NO, "
			+ " C.CONTACT_PHONE_EXT_NO, "
			+ " CN.CONTRACT_STATUS_CODE, "
			+ " CN.PRODUCT_ID,	 "
			+ " TPA.THIRD_PARTY_ADMIN_NAME, "
			+ " CN.LAST_STMT_PRODUCTION_DATE, "
			+ " P.PERMITTED_DISPARITY_IND, "
			+ " CN.ASSET_CHARGE_RATE, "
			+ " TPA.THIRD_PARTY_ADMIN_NAME, "
			+ " TPA.CONTACT_NAME TPA_CONTACT_NAME, "
			+ " CS.FSW_IND, "
			/*+ " CN.LOAN_MONTHLY_FLAT_FEE_AMT, "*/
			+ " CN.LOAN_SETUP_FEE_AMT, "
			+ " CN.LOAN_SETUP_FEE_AUTODEDUCT_IND, "
			+ " ASSET_CHARGE_AS_OF_DATE, "
			+ " CAR.PHONE_EXTENSION_NO, "
			+ " CASE "
			+ " WHEN MANULIFE_COMPANY_ID = '019' THEN RTRIM(CAR.CLIENT_ACCOUNT_REP_ID)||'@jhancock.com' "
			+ " ELSE RTRIM(CAR.CLIENT_ACCOUNT_REP_ID)||'@jhancockny.com'  "
			+ " END AS EMAIL_ADDRESS "
			+ " FROM "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_CS CN "
			+ " LEFT OUTER JOIN "
			+ CUSTOMER_SCHEMA_NAME
			+ " CLIENT_ACCOUNT_REP CAR ON CAR.CLIENT_ACCOUNT_REP_ID=CN.CLIENT_ACCOUNT_REP_ID "
			+ " LEFT OUTER JOIN "
			+ CUSTOMER_SCHEMA_NAME
			+ " CONTRACT_MAIL_RECIPIENT C ON C.CONTRACT_ID=CN.CONTRACT_ID "
			+ " LEFT OUTER JOIN "
			+ CUSTOMER_SCHEMA_NAME
			+ " CLIENT_ADDRESS CA ON CN.CLIENT_ID=CA.CLIENT_ID AND ADDRESS_TYPE_CODE='M' "
			+ " LEFT OUTER JOIN "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " THIRD_PARTY_ADMINISTRATOR TPA ON TPA.THIRD_PARTY_ADMIN_ID=CN.THIRD_PARTY_ADMIN_ID "
			+ " LEFT OUTER JOIN "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " PLAN P ON P.PLAN_ID=CN.CONTRACT_ID "
			+ " LEFT OUTER JOIN PSW100.CONTRACT_SUMMARY_VF CS ON CS.CONTRACT_ID=CN.CONTRACT_ID "
			+ " WHERE CN.CONTRACT_ID=? ";

	private static final String SQL_SELECT_PARTICIPANT_COUNT = " SELECT  COUNT(*) AS NO_OF_PARTICIPANTS"
			+ " FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " EMPLOYEE_CONTRACT E,"
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " PARTICIPANT_CONTRACT P "
			+ " WHERE "
			+ " E.PROFILE_ID  = P.PROFILE_ID "
			+ " AND E.CONTRACT_ID = P.CONTRACT_ID "
			+ " AND E.CONTRACT_ID = ? "
			+ " AND ( P.PARTICIPANT_STATUS_CODE = 'AC' "
			+ " OR (SELECT SUM (PB.TOTAL_BALANCE_AMT) "
			+ " FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME
			+ " PARTICIPANT_CURRENT_BAL_LSA PB "
			+ " WHERE PB.CONTRACT_ID=P.CONTRACT_ID "
			+ " AND PB.CONTRACT_ID = ? "
			+ " AND PB.PARTICIPANT_ID=P.PARTICIPANT_ID ) > 0 ) ";

	private static final String SQL_SELECT_CONTRIBUTION_DETAILS = " SELECT  "
			+ " CFA.LAST_ALLOCATION_AMOUNT AS LAST_PAYROLL_ALLOCATION_AMOUNT, "
			+ " CFA.LAST_ALLOCATION_APPLIC_DT AS FOR_PAYROLL_ENDING_DATE,  "
			+ " CFA.LAST_ALLOCATION_INVEST_DT AS RECEIVED_DATE, "
			+ " CFA.LAST_ALLOCATION_TRANSACTION_NO  " + "FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME + " CONTRACT_CS C, "
			+ PLAN_SPONSOR_SCHEMA_NAME + " CONTRACT_FINANCIAL_ACTIVITY CFA  "
			+ "WHERE " + " C.CONTRACT_ID = ?  "
			+ " AND  C.CONTRACT_ID = CFA.CONTRACT_ID "
			+ " AND  C.CONTRACT_STATUS_CODE != 'CA' ";
	
	private static final String SQL_TPA_CONTACT_INFO = "SELECT U.USER_PROFILE_ID,U.FIRST_NAME,U.LAST_NAME, " 
			+"TPA_FIRM_ID,THIRD_PARTY_ADMIN_NAME, ET.SECURITY_ROLE_CODE FROM "
			+ PLAN_SPONSOR_SCHEMA_NAME +" USER_PROFILE AS U, "
			+ PLAN_SPONSOR_SCHEMA_NAME +" SECURITY_ROLE AS S, "
			+ PLAN_SPONSOR_SCHEMA_NAME +" CONTRACT_CS AS CS, "
			+ PLAN_SPONSOR_SCHEMA_NAME +" THIRD_PARTY_ADMINISTRATOR AS T, "
			+ PLAN_SPONSOR_SCHEMA_NAME +" EXTERNAL_USER_TPA_FIRM AS ET "
			+ "LEFT OUTER JOIN "
			+ PLAN_SPONSOR_SCHEMA_NAME +" PERMISSION_HOLDER AS H "
			+ "ON ( H.USER_TPA_FIRM_USER_PROFILE_ID = ET.USER_PROFILE_ID "
			+ "AND H.USER_TPA_FIRM_TPA_FIRM_ID = ET.TPA_FIRM_ID ) "
			+ "WHERE  U.END_TS > CURRENT TIMESTAMP "
			+ "AND U.USER_PROFILE_ID = ET.USER_PROFILE_ID "
			+ "AND ET.TPA_FIRM_ID = CS.THIRD_PARTY_ADMIN_ID "
			+ "AND T.THIRD_PARTY_ADMIN_ID = CS.THIRD_PARTY_ADMIN_ID "
			+ "AND CS.CONTRACT_ID = ? "
			+ "AND U.CREATED_SECURITY_ROLE_CODE = S.SECURITY_ROLE_CODE "
			+ "ORDER BY FIRST_NAME FOR FETCH ONLY ";

	private static final String SQL_TPA_CONTACTS_ATTRIBUTES = "SELECT ET.USER_PROFILE_ID, C.CONTACT_ID, " 
			+ " CO.CONTACT_OPTION_CODE FROM "
			+ " PSW100.USER_PROFILE U, PSW100.CONTRACT_CS CS,  PSW100.PERMISSION_HOLDER H,"
			+ " PSW100.EXTERNAL_USER_TPA_FIRM ET , PSW100.THIRD_PARTY_ADMINISTRATOR T,"
			+ " PSW100.CONTACT_OPTION CO, PSW100.CONTACT C"
			+ " WHERE U.END_TS > CURRENT TIMESTAMP   "
	        + " AND U.USER_PROFILE_ID = ET.USER_PROFILE_ID"
	        + " AND ET.TPA_FIRM_ID = CS.THIRD_PARTY_ADMIN_ID"
	        + " AND T.THIRD_PARTY_ADMIN_ID = CS.THIRD_PARTY_ADMIN_ID" 
	        + " AND CS.CONTRACT_ID = ?"
			+ " AND CO.CONTACT_ID = C.CONTACT_ID"
			+ " AND C.PERMISSION_HOLDER_ID = H.PERMISSION_HOLDER_ID"
			+ " AND H.TPA_U_CONTRACT_USER_PROFILE_ID = ET.USER_PROFILE_ID"
			+ " AND H.TPA_U_CONTRACT_CONTRACT_ID = CS.CONTRACT_ID FOR FETCH ONLY ";
	 private static final String SQL_GET_TPA_PRIMARY_CONTACT_DETAILS = 
   		  "WITH CONTACT_INFO ( USER_PROFILE_ID, FIRST_NAME, LAST_NAME, EMAIL_ADDRESS_TEXT, "
   		 + "      PERMISSION_HOLDER_ID, CONTRACT_ID ) "
   		 + "     AS (SELECT U.USER_PROFILE_ID, "
   		 + "                U.FIRST_NAME, "
   		 + "                U.LAST_NAME, "
   		 + "                U.EMAIL_ADDRESS_TEXT, "
   		 + "                H.PERMISSION_HOLDER_ID, "
   		 + "                CS.CONTRACT_ID "
   		 + "         FROM   PSW100.USER_PROFILE AS U, "
   		 + "                PSW100.SECURITY_ROLE AS S, "
   		 + "                PSW100.CONTRACT_CS AS CS, "
   		 + "                PSW100.THIRD_PARTY_ADMINISTRATOR AS T, "
   		 + "                PSW100.EXTERNAL_USER_TPA_FIRM AS ET "
   		 + "                LEFT OUTER JOIN PSW100.PERMISSION_HOLDER AS H "
   		 + "                             ON ( H.USER_TPA_FIRM_USER_PROFILE_ID = "
   		 + "                                  ET.USER_PROFILE_ID "
   		 + "                                  AND H.USER_TPA_FIRM_TPA_FIRM_ID = "
   		 + "                                      ET.TPA_FIRM_ID ) "
   		 + "         WHERE  U.END_TS > CURRENT TIMESTAMP "
   		 + "                AND U.USER_PROFILE_ID > 100 "
   		 + "                AND U.USER_PROFILE_ID = ET.USER_PROFILE_ID "
   		 + "                AND ET.TPA_FIRM_ID = CS.THIRD_PARTY_ADMIN_ID "
   		 + "                AND T.THIRD_PARTY_ADMIN_ID = CS.THIRD_PARTY_ADMIN_ID "
   		 + "                AND CS.CONTRACT_ID = ? "
   		 + "                AND U.CREATED_SECURITY_ROLE_CODE = S.SECURITY_ROLE_CODE), "
   		 + "     PHONE_FAX (PERMISSION_HOLDER_ID, CONTACT_ID, AREA_CODE, PHONE_NO_PREFIX, "
   		 + "     PHONE_NO_SUFFIX, EXTENSION_NO, DEVICE_TYPE_CODE ) "
   		 + "     AS (SELECT C.PERMISSION_HOLDER_ID, "
   		 + "                C.CONTACT_ID, "
   		 + "                CP.AREA_CODE AS PHONE_AREA_CODE, "
   		 + "                CP.PHONE_NO_PREFIX, "
   		 + "                CP.PHONE_NO_SUFFIX, "
   		 + "                CP.EXTENSION_NO, "
   		 + "                DEVICE_TYPE_CODE "
   		 + "         FROM   PSW100.CONTACT AS C, "
   		 + "                PSW100.CONTACT_METHOD AS CM, "
   		 + "                PSW100.CONTACT_PHONE AS CP, "
   		 + "                CONTACT_INFO TEMP "
   		 + "         WHERE  CM.CONTACT_ID = C.CONTACT_ID "
   		 + "                AND CP.SEQUENCE_NO = CM.SEQUENCE_NO "
   		 + "                AND CP.CONTACT_ID = CM.CONTACT_ID "
   		 + "                AND CP.DEVICE_TYPE_CODE IN ( 'PH' ) "
   		 + "                AND TEMP.PERMISSION_HOLDER_ID = C.PERMISSION_HOLDER_ID), "
   		 + "     PRIMARY_CONTACT (USER_PROFILE_ID) "
   		 + "     AS (SELECT DISTINCT U.USER_PROFILE_ID "
   		 + "         FROM   PSW100.USER_PROFILE U, "
   		 + "                PSW100.CONTRACT_CS CS, "
   		 + "                PSW100.PERMISSION_HOLDER H, "
   		 + "                PSW100.EXTERNAL_USER_TPA_FIRM ET, "
   		 + "                PSW100.THIRD_PARTY_ADMINISTRATOR T, "
   		 + "                PSW100.CONTACT_OPTION CO, "
   		 + "                PSW100.CONTACT C, "
   		 + "                CONTACT_INFO TP "
   		 + "         WHERE  U.END_TS > CURRENT TIMESTAMP "
   		 + "                AND CO.CONTACT_OPTION_CODE = 'PRIM' "
   		 + "                AND U.USER_PROFILE_ID > 100 "
   		 + "                AND U.USER_PROFILE_ID = ET.USER_PROFILE_ID "
   		 + "                AND ET.TPA_FIRM_ID = CS.THIRD_PARTY_ADMIN_ID "
   		 + "                AND T.THIRD_PARTY_ADMIN_ID = CS.THIRD_PARTY_ADMIN_ID "
   		 + "                AND TP.CONTRACT_ID = CS.CONTRACT_ID "
   		 + "                AND CO.CONTACT_ID = C.CONTACT_ID "
   		 + "                AND C.PERMISSION_HOLDER_ID = H.PERMISSION_HOLDER_ID "
   		 + "                AND H.TPA_U_CONTRACT_USER_PROFILE_ID = ET.USER_PROFILE_ID "
   		 + "                AND H.TPA_U_CONTRACT_CONTRACT_ID = CS.CONTRACT_ID) "
   		 + "SELECT DISTINCT INFO.USER_PROFILE_ID    AS USER_PROFILE_ID, "
   		 + "                INFO.FIRST_NAME         AS FIRST_NAME, "
   		 + "                INFO.LAST_NAME          AS LAST_NAME, "
   		 + "                INFO.EMAIL_ADDRESS_TEXT AS EMAIL_ADDRESS_TEXT, "
   		 + "                PHONE.AREA_CODE         PHONE_AREA_CODE, "
   		 + "                PHONE.PHONE_NO_PREFIX   PHONE_NO_PREFIX, "
   		 + "                PHONE.PHONE_NO_SUFFIX   PHONE_NO_SUFFIX, "
   		 + "                PHONE.EXTENSION_NO "
   		 + "FROM   CONTACT_INFO INFO "
   		 + "       INNER JOIN PRIMARY_CONTACT PRIMARY "
   		 + "               ON ( INFO.USER_PROFILE_ID = PRIMARY.USER_PROFILE_ID ) "
   		 + "       LEFT OUTER JOIN (SELECT PERMISSION_HOLDER_ID "
   		 + "                        FROM   PSW100.PERMISSION_GRANT "
   		 + "                        WHERE  SECURITY_TASK_PERMISSION_CODE = 'EXMN' "
   		 + "                                OR SECURITY_TASK_PERMISSION_CODE = 'TUMN') AS "
   		 + "                       EXMN "
   		 + "                    ON ( EXMN.PERMISSION_HOLDER_ID = INFO.PERMISSION_HOLDER_ID ) "
   		 + "       LEFT OUTER JOIN (SELECT PERMISSION_HOLDER_ID, "
   		 + "                               AREA_CODE, "
   		 + "                               PHONE_NO_PREFIX, "
   		 + "                               PHONE_NO_SUFFIX, "
   		 + "                               EXTENSION_NO "
   		 + "                        FROM   PHONE_FAX "
   		 + "                        WHERE  DEVICE_TYPE_CODE = 'PH') PHONE "
   		 + "                    ON ( INFO.PERMISSION_HOLDER_ID = PHONE.PERMISSION_HOLDER_ID "
   		 + "                       )";
	 private static final String SQL_GET_PS_PRIMARY_CONTACT_DETAILS = 
			 "WITH CONTACT_INFO (PERMISSION_HOLDER_ID, FIRST_NAME, LAST_NAME, "
			+ "     EMAIL_ADDRESS_TEXT ) "
			+ "     AS (SELECT H.PERMISSION_HOLDER_ID, "
			+ "                U.FIRST_NAME         AS FIRST_NAME, "
			+ "                U.LAST_NAME          AS LAST_NAME, "
			+ "                U.EMAIL_ADDRESS_TEXT AS EMAIL_ADDRESS_TEXT "
			+ "         FROM   PSW100.USER_PROFILE AS U, "
			+ "                PSW100.SECURITY_ROLE AS S, "
			+ "                PSW100.USER_CONTRACT AS C, "
			+ "                PSW100.PERMISSION_HOLDER AS H "
			+ "         WHERE  U.END_TS > CURRENT TIMESTAMP "
			+ "                AND U.USER_PROFILE_ID > 100 "
			+ "                AND C.SECURITY_ROLE_CODE = S.SECURITY_ROLE_CODE "
			+ "                AND U.USER_PROFILE_ID = C.USER_PROFILE_ID "
			+ "                AND U.PSW_DIRECTORY_ROLE_CODE IS NOT NULL "
			+ "                AND S.TYPE_CODE = 'EU' "
			+ "                AND C.CONTRACT_ID = ? "
			+ "                AND C.USER_PROFILE_ID = H.USER_CONTRACT_USER_PROFILE_ID "
			+ "                AND C.CONTRACT_ID = H.USER_CONTRACT_CONTRACT_ID "
			+ "                AND S.SECURITY_ROLE_CODE <> 'TPA'), "
			+ "     PHONE_FAX (PERMISSION_HOLDER_ID, AREA_CODE, PHONE_NO_PREFIX, "
			+ "     PHONE_NO_SUFFIX, EXTENSION_NO, DEVICE_TYPE_CODE ) "
			+ "     AS (SELECT CT.PERMISSION_HOLDER_ID, "
			+ "                CP.AREA_CODE, "
			+ "                CP.PHONE_NO_PREFIX, "
			+ "                CP.PHONE_NO_SUFFIX, "
			+ "                EXTENSION_NO, "
			+ "                DEVICE_TYPE_CODE "
			+ "         FROM   PSW100.CONTACT_METHOD AS CM, "
			+ "                PSW100.CONTACT_PHONE AS CP, "
			+ "                PSW100.CONTACT AS CT, "
			+ "                CONTACT_INFO TEMP1 "
			+ "         WHERE  CM.CONTACT_METHOD_TYPE_CODE = 'PH' "
			+ "                AND CM.CONTACT_METHOD_PURPOSE_CODE = 'WO' "
			+ "                AND CT.CONTACT_ID = CM.CONTACT_ID "
			+ "                AND CM.CONTACT_ID = CP.CONTACT_ID "
			+ "                AND CM.SEQUENCE_NO = CP.SEQUENCE_NO "
			+ "                AND CT.PERMISSION_HOLDER_ID = TEMP1.PERMISSION_HOLDER_ID) "
			+ "SELECT INFO.FIRST_NAME, "
			+ "       INFO.LAST_NAME, "
			+ "       INFO.EMAIL_ADDRESS_TEXT, "
			+ "       PHONE.AREA_CODE, "
			+ "       PHONE.PHONE_NO_PREFIX, "
			+ "       PHONE.PHONE_NO_SUFFIX, "
			+ "       PHONE.EXTENSION_NO "
			+ "FROM   PSW100.CONTACT CT, "
			+ "       PSW100.CONTACT_OPTION CO, "
			+ "       CONTACT_INFO INFO "
			+ "       LEFT OUTER JOIN (SELECT PERMISSION_HOLDER_ID "
			+ "                        FROM   PSW100.PERMISSION_GRANT "
			+ "                        WHERE  SECURITY_TASK_PERMISSION_CODE = 'EXMN' "
			+ "                                OR SECURITY_TASK_PERMISSION_CODE = 'TUMN') AS "
			+ "                       EXMN "
			+ "                    ON ( EXMN.PERMISSION_HOLDER_ID = INFO.PERMISSION_HOLDER_ID ) "
			+ "       LEFT OUTER JOIN (SELECT PERMISSION_HOLDER_ID, "
			+ "                               AREA_CODE, "
			+ "                               PHONE_NO_PREFIX, "
			+ "                               PHONE_NO_SUFFIX, "
			+ "                               EXTENSION_NO "
			+ "                        FROM   PHONE_FAX "
			+ "                        WHERE  DEVICE_TYPE_CODE = 'PH') AS PHONE "
			+ "                    ON ( INFO.PERMISSION_HOLDER_ID = PHONE.PERMISSION_HOLDER_ID "
			+ "                       ) "
			+ "WHERE  INFO.PERMISSION_HOLDER_ID = CT.PERMISSION_HOLDER_ID "
			+ "       AND CT.CONTACT_ID = CO.CONTACT_ID "
			+ "       AND CO.CONTACT_OPTION_CODE = 'PRIM' ";

	 private static final String SQL_GET_RELATIONSHIP_MANAGER_DETAILS = " SELECT RMCI.EMAIL_ADDR, "
	 		+ " RMCI.PHONE_NO,RMCI.PHONE_EXT_NO, "
	 		+ " PERS.FIRST_NAME, "
	 		+ " PERS.LAST_NAME "
            + " FROM  PSW100.CONTRACT_PARTY_HISTORY CNPTLP, "
            + " PSW100.PARTY_LP_PSOFT PTLPPS, "
            + " BDW100.PARTY_ROLE PR, "
            + " PSW100.PERSON PERS "
            + " LEFT OUTER JOIN PSW100.PARTY_RELATIONSHIP REL "
            + " ON PERS.PARTY_ID = REL.PARTY_ID_1 "
            + " AND REL.PARTY_RELATIONSHIP_CODE = 'MLI-MGR' "
            + " AND REL.DELETED_IND = 'N' "
            + " AND REL.START_DATE <= ? "
            + " AND REL.END_DATE > ? "
            + " LEFT OUTER JOIN PSW100.PERSON PER1 "
            + " ON REL.PARTY_ID_2 = PER1.PARTY_ID "
            + " LEFT OUTER JOIN PSW100.RELATIONSHIP_MANAGER_CONTACT_INFO RMCI "
            + " ON RMCI.PERSON_ID  =  PERS. PERSON_ID  "    
            + " WHERE  PTLPPS.PERSON_ID = PERS.PERSON_ID "
            + " AND PTLPPS.PARTY_LP_ID = CNPTLP.PARTY_ID "
            + " AND PR.PARTY_ID = PERS.PARTY_ID "
            + " AND PR.PARTY_ROLE_CODE = 'RM' "
            + " AND PR.START_DATE <= ? "
            + " AND PR.END_DATE > ? "
            + " AND CNPTLP.PARTY_ROLE_START_DATE <= ? "
            + " AND CNPTLP.PARTY_ROLE_END_DATE > ? "
            + " AND PERS.DELETED_IND = 'N' "
            + " AND PERS.PERSON_STATUS_CODE = 'A' " 
            + " AND CNPTLP.CONTRACT_ID =  ? ";

  
	/**
	 * Method to retrieve the contract information details by providing criteria
	 * and reportData.
	 * 
	 * @param criteria
	 * @param reportData
	 * @throws SystemException
	 */
	@SuppressWarnings("unchecked")
	public static void getContractInformationDetails(ReportCriteria criteria,
			ContractInformationReportData reportData) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger
					.debug("entry -> getContractInformationDetails(ReportCriteria criteria, ContractInformationReportData reportData) ");
		}

		Calendar asOfDate = Calendar.getInstance();
		Calendar contractEffectiveDate = Calendar.getInstance();

		Integer contractNumber = (Integer) criteria
				.getFilterValue(ContractInformationReportData.FILTER_CONTRACT_NUMBER);
		asOfDate
				.setTime((Date) criteria
						.getFilterValue(ContractInformationReportData.FILTER_AS_OF_DATE));
		contractEffectiveDate
				.setTime((Date) criteria
						.getFilterValue(ContractInformationReportData.FILTER_CONTRACT_EFFECTIVE_DATE));

		ContractSummaryVO contractSummaryVo = new ContractSummaryVO();
		ContractSnapshotVO contractSnapshotVo = new ContractSnapshotVO();
		ContractProfileVO contractProfileVo = new ContractProfileVO();

		reportData.setContractProfileVo(contractProfileVo);
		reportData.setContractSnapshotVo(contractSnapshotVo);
		reportData.setContractSummaryVo(contractSummaryVo);

		PlanAssetsVO planAssetsVO = new PlanAssetsVO();
		contractSnapshotVo.setPlanAssets(planAssetsVO);

		ContractProfileVO.FeaturesAndServices featuresAndServices = contractProfileVo.new FeaturesAndServices();
		contractProfileVo.setFeaturesAndServices(featuresAndServices);

		ContractProfileVO.Address address = contractProfileVo.new Address();
		contractProfileVo.setAddress(address);

		Collection<Date> garunteedAccountTransferDates = new ArrayList<Date>();
		contractProfileVo
				.setGuaranteedAccountTransferDates(garunteedAccountTransferDates);

		ContractProfileVO.StatementInfo statementInfo = contractProfileVo.new StatementInfo();
		contractProfileVo.setStatementInfo(statementInfo);

		ContactVO carContact = new ContactVO();
		contractSummaryVo.setCarContact(carContact);

		ContactVO planSponsorContact = new ContactVO();
		reportData.setPlanSponsorContact(planSponsorContact);

		List<MoneySourceVO> moneySources = new ArrayList<MoneySourceVO>();
		reportData.setMoneySources(moneySources);

		List<MoneyTypeVO> moneyTypes = new ArrayList<MoneyTypeVO>();
		reportData.setMoneyTypes(moneyTypes);

		Collection<String> accessChannels = new ArrayList<String>();
		featuresAndServices.setAccessChannels(accessChannels);

		BigDecimal loanSetUpFeeAmount = new BigDecimal(0);
		boolean loanSetUpFeeIndicator = false;

		try {
			int j = 0;
			Object[] inputParams = new Object[0];
			BigDecimal totalPlanAssetsAmount = null;
			BigDecimal allocatedAssetsAmount = null;
			BigDecimal personalBrokerageAccountAmount = null;
			BigDecimal uninvestedAssetsAmount = null;
			BigDecimal pendingTransactionsAmount = null;
			BigDecimal cashAccountAmount = null;
			BigDecimal resultOfLoanAssets = null;
			Integer participantCount = null;
			Date currentAsOfdate = null;

			SelectSingleOrNoValueQueryHandler asOfDateHandler = new SelectSingleOrNoValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_ASOFDATE,
					new int[] { Types.TIMESTAMP }, Date.class);
			Date selectAsOfdate = (Date) asOfDateHandler.select(inputParams);
			if (selectAsOfdate != null) {
				currentAsOfdate = selectAsOfdate;
			}

			SelectMultiFieldQueryHandler assetsAmountHandler = new SelectMultiFieldQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME,
					SQL_SELECT_TOTAL_PLAN_ASSETS_AMOUNT, new Class[] {
							BigDecimal.class, BigDecimal.class,
							BigDecimal.class, BigDecimal.class,
							BigDecimal.class });
			Object[] resultOfAssetsAmount = (Object[]) assetsAmountHandler
					.select(new Object[] { contractNumber });
			if (resultOfAssetsAmount != null) {

				totalPlanAssetsAmount = (BigDecimal) resultOfAssetsAmount[0];
				allocatedAssetsAmount = (BigDecimal) resultOfAssetsAmount[1];
				personalBrokerageAccountAmount = (BigDecimal) resultOfAssetsAmount[2];
				uninvestedAssetsAmount = (BigDecimal) resultOfAssetsAmount[3];
				pendingTransactionsAmount = (BigDecimal) resultOfAssetsAmount[4];

			} else {

				totalPlanAssetsAmount = new BigDecimal("0.00");
				allocatedAssetsAmount = new BigDecimal("0.00");
				personalBrokerageAccountAmount = new BigDecimal("0.00");
				uninvestedAssetsAmount = new BigDecimal("0.00");
				pendingTransactionsAmount = new BigDecimal("0.00");

			}

			SelectSingleOrNoValueQueryHandler cashAccountAmountHandler = new SelectSingleOrNoValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CASHACCOUNT_AMOUNT,
					new int[] { Types.DECIMAL }, BigDecimal.class);
			BigDecimal resultOfCashAccountAmount = (BigDecimal) cashAccountAmountHandler
					.select(new Object[] { contractNumber });
			if (resultOfCashAccountAmount != null) {
				cashAccountAmount = resultOfCashAccountAmount;
			} else {
				cashAccountAmount = new BigDecimal("0.00");
			}

			planAssetsVO.setTotalPlanAssetsAmount(totalPlanAssetsAmount);
			planAssetsVO.setAllocatedAssetsAmount(allocatedAssetsAmount);
			planAssetsVO.setCashAccountAmount(cashAccountAmount);
			planAssetsVO
					.setPersonalBrokerageAccountAmount(personalBrokerageAccountAmount);
			planAssetsVO.setUninvestedAssetsAmount(uninvestedAssetsAmount);
			reportData.setPendingTransactionAmount(pendingTransactionsAmount);

			// gets the loan assets
			SelectSingleOrNoValueQueryHandler loanAssetsHandler = new SelectSingleOrNoValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_LOAN_ASSETS,
					new int[] { Types.DECIMAL }, BigDecimal.class);
			resultOfLoanAssets = (BigDecimal) loanAssetsHandler
					.select(new Object[] { contractNumber });
			if (resultOfLoanAssets != null) {

				planAssetsVO.setLoanAssets(resultOfLoanAssets);

			}

			// gets the money types
			SelectMultiFieldMultiRowQueryHandler moneyTypeHandler = new SelectMultiFieldMultiRowQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTRACT_MONEY_TYPE,
					new Class[] { String.class, String.class, String.class,
							String.class });
			Object[][] resultOfMoneyType = (Object[][]) moneyTypeHandler
					.select(new Object[] { contractNumber });

			MoneyTypeVO moneyTypeVO = null;
			if (resultOfMoneyType != null) {

				for (int i = 0; i < resultOfMoneyType.length; i++) {
					j = 0;
					moneyTypeVO = new MoneyTypeVO();
					String moneyTyeId = StringUtils
							.trim((String) resultOfMoneyType[i][j]);
					// ignore employer unvested money type
					if (!moneyTyeId.startsWith("UM")) {
						moneyTypeVO.setId(moneyTyeId);
						moneyTypeVO
								.setContractShortName((String) resultOfMoneyType[i][j + 1]);
						moneyTypeVO
								.setContractLongName((String) resultOfMoneyType[i][j + 3]);
						moneyTypes.add(moneyTypeVO);
					}
				}

			}

			// sort the money types based on alphabetical order of money type
			// short names
			Collections.sort(moneyTypes, new Comparator<MoneyTypeVO>() {
				public int compare(MoneyTypeVO vo1, MoneyTypeVO vo2) {
					return vo1.getContractShortName().compareTo(
							vo2.getContractShortName());
				}
			});

			// gets the money sources
			SelectMultiFieldMultiRowQueryHandler moneySourceHandler = new SelectMultiFieldMultiRowQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME,
					SQL_SELECT_CONTRACT_MONEY_SOURCE, new Class[] {
							String.class, String.class, String.class,
							String.class });
			Object[][] resultOfMoneySource = (Object[][]) moneySourceHandler
					.select(new Object[] { contractNumber });

			MoneySourceVO moneySourceVO = null;

			if (resultOfMoneySource != null) {

				for (int i = 0; i < resultOfMoneySource.length; i++) {
					j = 0;
					moneySourceVO = new MoneySourceVO();
					moneySourceVO.setId((String) resultOfMoneySource[i][j]);
					moneySourceVO
							.setContractShortName((String) resultOfMoneySource[i][j + 1]);
					moneySourceVO
							.setContractLongName((String) resultOfMoneySource[i][j + 3]);
					moneySources.add(moneySourceVO);
				}

			}

			// sort the money sources based on alphabetical order of money
			// source short names
			Collections.sort(moneySources, new Comparator<MoneySourceVO>() {
				public int compare(MoneySourceVO vo1, MoneySourceVO vo2) {
					return vo1.getContractShortName().compareTo(
							vo2.getContractShortName());
				}
			});

			// gets contract related general info
			SelectMultiFieldQueryHandler contractInfoHandler = new SelectMultiFieldQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTTRACT_INFO,
					new Class[] { Integer.class, String.class, String.class,
							String.class, String.class, String.class,
							String.class, String.class, String.class,
							Date.class, String.class, String.class, Date.class,
							Date.class, Date.class, Date.class, String.class,
							String.class, String.class, String.class,
							String.class, String.class, String.class,
							Date.class, String.class, BigDecimal.class,
							String.class, String.class, String.class,
							BigDecimal.class, String.class,
							Date.class, String.class, String.class });
			Object[] resultOfContractInfo = (Object[]) contractInfoHandler
					.select(new Object[] { contractNumber } ) ;

			if (resultOfContractInfo != null) {

				address.setLine1(StringUtils
						.trimToEmpty((String) resultOfContractInfo[2]));
				address.setLine2(StringUtils
						.trimToEmpty((String) resultOfContractInfo[3]));
				address.setCity(StringUtils
						.trimToEmpty((String) resultOfContractInfo[4]));
				address.setStateCode(StringUtils
						.trimToEmpty((String) resultOfContractInfo[5]));
				address.setZipCode(StringUtils
						.trimToEmpty((String) resultOfContractInfo[6]));
				carContact.setName((String) resultOfContractInfo[8]);
				planSponsorContact.setName((String) resultOfContractInfo[17]);
				planSponsorContact.setPhone((String) resultOfContractInfo[18]);
				contractProfileVo
						.setContractEffectiveDate((Date) resultOfContractInfo[9]);

				String planYearEndDate = (String) resultOfContractInfo[10];

				// sets the plan year end date
				contractProfileVo
						.setContractYearEndDate(getPlanYearEndDate(planYearEndDate));

				Date garunteedYear1 = (Date) resultOfContractInfo[12];
				Date garunteedYear2 = (Date) resultOfContractInfo[13];
				Date garunteedYear3 = (Date) resultOfContractInfo[14];
				Date garunteedYear4 = (Date) resultOfContractInfo[15];

				Calendar year1 = Calendar.getInstance();
				Calendar year2 = Calendar.getInstance();
				Calendar year3 = Calendar.getInstance();
				Calendar year4 = Calendar.getInstance();

				year1.setTime(garunteedYear1);
				year2.setTime(garunteedYear2);
				year3.setTime(garunteedYear3);
				year4.setTime(garunteedYear4);

				if (year1.get(Calendar.YEAR) == 8888) {
					garunteedAccountTransferDates.add(year1.getTime());
				}
				if (year2.get(Calendar.YEAR) == 8888) {
					garunteedAccountTransferDates.add(year2.getTime());
				}
				if (year3.get(Calendar.YEAR) == 8888) {
					garunteedAccountTransferDates.add(year3.getTime());
				}
				if (year4.get(Calendar.YEAR) == 8888) {
					garunteedAccountTransferDates.add(year4.getTime());
				}

				contractProfileVo
						.setContractAccessCode((String) resultOfContractInfo[11]);

				if ("Y".equals((String) resultOfContractInfo[16])) {
					featuresAndServices.setIsDirectDebitSelected(true);
				} else {
					featuresAndServices.setIsDirectDebitSelected(false);
				}

				if ("Y".equals((String) resultOfContractInfo[24])) {
					reportData.setIsPermittedDisparity(true);
				} else {
					reportData.setIsPermittedDisparity(false);
				}

				reportData.setTpaFirmName((String) resultOfContractInfo[26]);
				reportData.setTpaContactName((String) resultOfContractInfo[27]);
				statementInfo.setLastPrintDate((Date) resultOfContractInfo[23]);
				reportData
						.setBlendedAssetCharge((BigDecimal) resultOfContractInfo[25]);
				reportData
						.setAssetChargeAsOfDate((Date) resultOfContractInfo[31]);
				if ((BigDecimal) resultOfContractInfo[29] != null) {
					loanSetUpFeeAmount = (BigDecimal) resultOfContractInfo[29];
				}
				if ("Y".equals((String) resultOfContractInfo[30])) {
					loanSetUpFeeIndicator = true;
				}

				if ((String) resultOfContractInfo[32] != null) {
					carContact.setExtension((String) resultOfContractInfo[32]);
				}
				if ((String) resultOfContractInfo[33] != null) {
					carContact.setEmail((String) resultOfContractInfo[33]);
				}
			}
			
			TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(contractNumber);

			if (firmInfo != null) {
				reportData.setTpaContactsName(getTpaContacts(contractNumber));
			}

			// gets the participant count
			SelectSingleOrNoValueQueryHandler participantCountHandler = new SelectSingleOrNoValueQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_PARTICIPANT_COUNT,
					new int[] { Types.INTEGER }, Integer.class);
			participantCount = (Integer) participantCountHandler
					.select(new Object[] { contractNumber, contractNumber });
			if (participantCount != null) {

				reportData.setParticipantCount(participantCount.intValue());

			}

			// gets payroll allocation details
			SelectMultiFieldMultiRowQueryHandler allocationDetailsHandler = new SelectMultiFieldMultiRowQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CONTRIBUTION_DETAILS,
					new Class[] { BigDecimal.class, Date.class, Date.class,
							BigDecimal.class });
			Object[][] resultOfAllocationDetails = (Object[][]) allocationDetailsHandler
					.select(new Object[] { contractNumber });
			if (resultOfAllocationDetails != null
					&& resultOfAllocationDetails.length > 0) {

				for (int i = 0; i < resultOfAllocationDetails.length; i++) {
					j = 0;
					contractSummaryVo
							.setLastAllocationAmount((Double
									.valueOf(resultOfAllocationDetails[i][j]
											.toString())));
					contractSummaryVo
							.setLastPayrollDate((Date) resultOfAllocationDetails[i][j + 1]);
					contractSummaryVo
							.setLastSubmissionDate((Date) resultOfAllocationDetails[i][j + 2]);
				}

			}

			// get product feature types
			SelectMultiFieldMultiRowQueryHandler productFeaturesHandler = new SelectMultiFieldMultiRowQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_PRODUCT_FEATURES,
					new Class[] { String.class, String.class });
			Object[][] resultOfProductFeatures = (Object[][]) productFeaturesHandler
					.select(new Object[] { contractNumber,
							getSqlDate(currentAsOfdate),
							getSqlDate(currentAsOfdate), contractNumber,
							contractNumber });
			Map<String, String> contractFeatures = new TreeMap<String, String>();
			if (resultOfProductFeatures != null) {

				for (int i = 0; i < resultOfProductFeatures.length; i++) {
					j = 0;
					if (StringUtils
							.isNotEmpty((String) resultOfProductFeatures[i][j])) {
						contractFeatures
								.put(
										StringUtils
												.trimToEmpty((String) resultOfProductFeatures[i][j]),
										(String) resultOfProductFeatures[i][j + 1]);
					}
					if ((String) resultOfProductFeatures[i][j] != null
							&& ((String) resultOfProductFeatures[i][j])
									.startsWith("VPS")) {
						reportData.setIsVestingShownOnStatements(true);
					}
				}

			}

			// checks if vesting statement feature is there
			// remove it from features descritpion list
			/*
			 * if (contractFeatures.remove("VPS") != null) {
			 * reportData.setIsVestingShownOnStatements(true); }
			 */

			String standingLoanFeeDescription = "";
			// show Standing Loan Fee contract feature desc if
			// a) contract allows loans
			// b) Auto-deduct indicator is yes
			// c) Loan Set Up fee (a.k.a. Standing Loan Fee) was chosen by the
			// contract
			if (contractFeatures.containsKey(Constants.LOAN_FEATURE_CODE)
					&& loanSetUpFeeIndicator
					&& loanSetUpFeeAmount.intValue() != 0) {
				standingLoanFeeDescription = "Loan Fee:"
						+ NumberFormat.getCurrencyInstance().format(
								loanSetUpFeeAmount);
			}

			List<String> contractFeatureList = new ArrayList<String>();
			contractFeatureList.addAll(contractFeatures.values());
			Collection tempCollection = new ArrayList();
			
			// Payroll feedback service
			getPayrollFeedbackService(contractNumber, tempCollection);
			
			
			// SysWd features change
 			String sysWdProductFeatureTypeDescription = ContractServiceDelegate
 					.getInstance().getSystematicProductFeatureDescription(
 							contractNumber);
 
 			if (sysWdProductFeatureTypeDescription != null) {
 				tempCollection.add(sysWdProductFeatureTypeDescription);
 			}
 			if(standingLoanFeeDescription != null ){
 				tempCollection.add(standingLoanFeeDescription);
 				
 			}
 			contractFeatureList.addAll(tempCollection);
			// Standing Loan Fee should immediately follow “Loans with
			// Recordkeeping”
			/*if (!StringUtils.isEmpty(standingLoanFeeDescription)) {
				int index = contractFeatureList
						.indexOf("Loans with Recordkeeping");
				if (index != -1) {
					contractFeatureList
							.add(++index, standingLoanFeeDescription);
				}
			}*/
			//CoFid feature change.
			 tempCollection = new ArrayList();
			CoFiduciaryVO coFiduciaryVO =ContractServiceDelegate.getInstance().getCoFiduciaryVO(contractNumber);
            if(coFiduciaryVO.isCoFiduciary()){
            	tempCollection.add(CoFidPlanReviewHelper.getInstance().getCoFidLookUpDescription(coFiduciaryVO.getServiceProviderCode(),coFiduciaryVO.getFiduciaryType().getDbCode(),
            			coFiduciaryVO.isCoFiduciary321Selected() && coFiduciaryVO.isAutoExecuteIndicator()));
            	contractFeatureList.addAll(tempCollection);
            }
            
			// Small Plan Feature Change
			SmallPlanFeature smallPlanFeature = ContractServiceDelegate.getInstance().getSmallPlanFeatureCode(contractNumber);
			if (null != smallPlanFeature) {
				contractFeatureList.add(smallPlanFeature.getTitle());
			}
            
			featuresAndServices.setContractFeatures(contractFeatureList);
			
			reportData.setManagedAccountServiceFeature(getManagedAccountServiceDesc(contractNumber));
			
			// get access channels
			SelectMultiFieldMultiRowQueryHandler accessChannelsHandler = new SelectMultiFieldMultiRowQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_ACCESS_CHANNELS,
					new Class[] { String.class, String.class });
			Object[][] resultOfAccessChannels = (Object[][]) accessChannelsHandler
					.select(new Object[] { contractNumber,
							getSqlDate(currentAsOfdate), contractNumber,
							getSqlDate(currentAsOfdate), contractNumber });
			Collection<String> accessChannelCodes = new ArrayList<String>();
			String indicator;
			String accessChannelCode;

			if (resultOfAccessChannels != null) {

				for (int i = 0; i < resultOfAccessChannels.length; i++) {
					j = 0;
					accessChannelCode = StringUtils
							.trimToEmpty((String) resultOfAccessChannels[i][j]);
					if (Constants.PARTICIPANT_INITIATE_LOAN_CODE
							.equals(accessChannelCode)) {
						indicator = (String) resultOfAccessChannels[i][j + 1];
						if ("Y".equals(indicator)) {
							accessChannelCodes.add(accessChannelCode);
						}
					} else {
						accessChannelCodes.add(accessChannelCode);
					}
				}

			}

			boolean canParticipantInitiateLoans = false;
			boolean loanFeatureEnabled = false;

			for (String code : accessChannelCodes) {
				if (Constants.ACCESS_CHANNEL_IVR_CODE.equals(code)) {
					accessChannels.add("IVR");
				} else if (Constants.ACCESS_CHANNEL_WEB_CODE.equals(code)) {
					accessChannels.add("Web");
				} else if (Constants.PARTICIPANT_INITIATE_LOAN_CODE
						.equals(code)) {
					canParticipantInitiateLoans = true;
				} else if (Constants.LOAN_FEATURE_CODE.equals(code)) {
					loanFeatureEnabled = true;
				}
			}

			if (loanFeatureEnabled) {
				if (canParticipantInitiateLoans) {
					accessChannels
							.add("Complete Online Loans Access (includes participant initiated loans) ");
				} else {
					accessChannels
							.add("Limited Online Loans Access (excludes participant initiated loans) ");
				}
			}

			// get statement feature details
			SelectMultiFieldMultiRowQueryHandler productFeaturesOneHandler = new SelectMultiFieldMultiRowQueryHandler(
					CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_PRODUCT_FEATURES1,
					new Class[] { String.class, String.class });
			Object[][] resultOfProductFeaturesOne = (Object[][]) productFeaturesOneHandler
					.select(new Object[] { getSqlDate(currentAsOfdate),
							contractNumber, contractNumber });
			Collection<String> statementFeatureDesc = new ArrayList<String>();

			if (resultOfProductFeaturesOne != null) {

				for (int i = 0; i < resultOfProductFeaturesOne.length; i++) {
					j = 0;
					String code = (String) resultOfProductFeaturesOne[i][j];
					String desc = (String) resultOfProductFeaturesOne[i][j + 1];
					if (code != null && code.startsWith("PDCD")) {
						statementFeatureDesc.add("Fee disclosure Detail");
					} else if (code != null && code.startsWith("PDCS")) {
						statementFeatureDesc.add("Fee disclosure Summary");
					} else if (code != null && code.startsWith("PDCN")) {
						statementFeatureDesc.add("Fee disclosure No");
					} else {
						statementFeatureDesc.add(desc);
					}
				}

			}

			for (String desc : statementFeatureDesc) {
				if (desc.startsWith("Reporting Basis")) {
					if (desc.endsWith("Accrued"))
						statementInfo.setBasis("Accrual");
					else if (desc.endsWith("Cash"))
						statementInfo.setBasis("Cash");
					else if (desc.endsWith("Combo"))
						statementInfo.setBasis("Combo");
				} else if (desc.startsWith("Delivery method")) {
					if (desc.endsWith("Direct mail"))
						statementInfo.setDeliveryMethod("Direct mail");
					else if (desc.endsWith("Bulk"))
						statementInfo.setDeliveryMethod("Bulk");
					else if (desc.endsWith("Open envelope"))
						statementInfo.setDeliveryMethod("Open envelope");
				} else if (desc.startsWith("Statement type")) {
					if (desc.endsWith("Detail"))
						statementInfo.setStatementType("Detail");
					else if (desc.endsWith("Summary"))
						statementInfo.setStatementType("Summary");
					else
						statementInfo.setStatementType("No");
				} else if (desc.startsWith("Fee disclosure")) {
					if (desc.endsWith("Detail"))
						statementInfo.setFeeDisclosure("Detail");
					else if (desc.endsWith("Summary"))
						statementInfo.setFeeDisclosure("Summary");
					else if (desc.endsWith("No")) {
						statementInfo.setFeeDisclosure("No");
					}
				}
			}

			// gets the count of number of funds available and selected
			getFundsSelected(contractNumber, contractSummaryVo,
					contractProfileVo);

			// gets the default investment funds
			getDefaultInvestments(contractNumber, contractProfileVo);

			// To check if contract has lifecycle funds
			boolean hasLifecycle = false;
			for (String fundId : (List<String>) contractSummaryVo
					.getSelectedFunds()) {
				if (FundVO.RISK_LIFECYCLE.equals(FundInfoCache
						.getRiskCategoryCode(fundId))) {
					hasLifecycle = true;
					break;
				}
			}
			reportData.setHasLifeCycleFunds(hasLifecycle);
		} catch (DAOException dAOException) {

			// throw the DAOIntigrity Exception
		}

		if (logger.isDebugEnabled()) {
			logger
					.debug("exit <- getContractInformationDetails(ReportCriteria criteria, ContractInformationReportData reportData) ");
		}
	}

	private static void getPayrollFeedbackService(Integer contractNumber, Collection tempCollection)
			throws SystemException {
		try
		{
			Map csFeaturesMap = ContractServiceDelegate.getInstance().
					getContractServiceFeatures(contractNumber);
			ContractServiceFeature payrollFeedbackService = (ContractServiceFeature) csFeaturesMap
					.get(ServiceFeatureConstants.PAYROLL_FEEDBACK_SERVICE_FEATURE_CODE);
			if (payrollFeedbackService != null && ServiceFeatureConstants.YES.equals(payrollFeedbackService.getValue())) {			
				if (ServiceFeatureConstants.YES.equals(payrollFeedbackService.getAttributeMap().get(ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE))) {
					tempCollection.add(payrollFeedbackService.getAttributeDescription(ServiceFeatureConstants.PAYROLL_FEEDBACK_360_SERVICE_ATTRIBUTE_CODE));
				}else {
					tempCollection.add(payrollFeedbackService.getDescription());	
				}
			}

		} catch (ApplicationException ae) {
			throw new SystemException("An exception occured at " 
					+ "ContractServiceDelegate.getContractServiceFeatures()" + ae.getDisplayMessage());
		}
	}

    /**
     * Update ContractSummaryVO with the contract selected funds
     * 
     * @param int contractNumber
     * @param ContractSummary vo
     * @return updated vo
     * @throws SystemException
     */
    public static void getFundsSelected(int contractNumber, ContractSummaryVO summaryVo,
            ContractProfileVO profileVo) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger
            .debug("entry -> getFundsSelected(int contractNumber, ContractSummaryVO summaryVo,ContractProfileVO profileVo)");
        }

        int selected = 0;
        List<String> fundsSelected = new ArrayList<String>();
        Map<String, String> fundsSelectedMap = getFundsSelected(contractNumber);
        Iterator<Entry<String, String>> its = fundsSelectedMap.entrySet().iterator();
        while (its.hasNext()) {
            Map.Entry<String, String> it = its.next();
            String fundSelectedFlag = (String) it.getValue();
            if (fundSelectedFlag.equals("Y")) {
                fundsSelected.add(it.getKey());
                if(it.getKey()!=null && (it.getKey().startsWith("10YC")||
                		it.getKey().startsWith("3YC")|| it.getKey().startsWith("5YC"))){
                	summaryVo.setContractHasGAFunds(true);
                }
                selected++;
            }
        }

        summaryVo.setSelectedFunds(fundsSelected);
        profileVo.getFeaturesAndServices().setSelectedFundsNumber(selected);
        profileVo.getFeaturesAndServices().setAvailableFundsNumber(fundsSelectedMap.size());

        if (logger.isDebugEnabled()) {
            logger
            .debug("exit <- getFundsSelected(int contractNumber, ContractSummaryVO summaryVo, ContractProfileVO profileVo)");
        }
    }

    /**
     * Retrieve a Map of the contract selected funds
     * 
     * @param int contractNumber
     * @return a map of contract selected funds
     * @throws SystemException
     */
    public static Map<String, String> getFundsSelected(int contractNumber) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getFundsSelected(int contractNumber)");
        }

        Connection conn = null;
        CallableStatement statement = null;
        ResultSet rs = null;

        Map<String, String> fundsSelectedMap = new HashMap<String, String>();

        try {
            // setup the connection and the statement
            conn = getReadUncommittedConnection(className,
                    BaseDatabaseDAO.VIEW_FUNDS_DATA_SOURCE_NAME);
            statement = conn.prepareCall(SELECT_CONTRACT_FUNDS);

            // set the input parameters
            statement.setBigDecimal(1, intToBigDecimal(contractNumber));

            // execute the stored procedure
            statement.execute();
            if (logger.isDebugEnabled())
                logger.debug("Calling Stored Procedure: " + "SELECT_CONTRACTS_FUNDS");

            rs = statement.getResultSet();
            
            if (rs != null) {
                
                for (int i = 0; rs.next(); i++) {
                    fundsSelectedMap.put(rs.getString("FUNDID").trim(), rs.getString("SELECTEDFLAG")
                            .trim());
                }
                
            }

        } catch (SQLException e) {
            throw new SystemException(
                    e,
                    className,
                    "getFundsSelected",
                    "Problem occurred during SELECT_CONTRACT_FUNDS stored proc call. Input parameter is contractNumber:"
                    + contractNumber);
        } finally {
            close(statement, conn);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- getFundsSelected(int contractNumber)");

        return fundsSelectedMap;
    }

    /**
     * Update contract profile with the contract default investments.
     * 
     * @param contractNumber
     * @param profile
     * @throws SystemException
     */
    public static void getDefaultInvestments(final int contractNumber,
            final ContractProfileVO profile) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug(new StringBuffer(
            "ContractDAO.getDefaultInvestments> Entry with contract number [").append(
                    contractNumber).append("].").toString());
        }

        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        final Collection<DefaultInvestmentFundVO> defaultInvestmentFunds = new ArrayList<DefaultInvestmentFundVO>();

        try {
            // setup the connection and the statement
            connection = getDefaultConnection(className,
                    BaseDatabaseDAO.VIEW_FUNDS_DATA_SOURCE_NAME);
            statement = connection.prepareCall(GET_DEFAULT_INVESTMENTS_LIST);

            // set the input parameters
            statement.setString(1, intToBigDecimal(contractNumber).toString());

            // execute the stored procedure
            statement.execute();
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + "CONTRACT_DETAILS_SQL");
            }

            resultSet = statement.getResultSet();

            DefaultInvestmentFundVO fund;
            Collection<String> fundSuites = new ArrayList<String> ();
            if (resultSet != null) {
            	// To get LifeCycle fund family name from a lookup table
				Map<String, String> familyNames = EnvironmentServiceHelper
						.getInstance().getLifecycleFamilyNames();    
                while (resultSet.next()) {
    
                    fund = new DefaultInvestmentFundVO();
                    fund.setFundId(resultSet.getString("fund_investmentid"));
                    fund.setFundName(resultSet.getString("fundname").trim());
                    fund.setPercentage(resultSet.getInt("default_investment_pct"));
                    fund.setLifeCycleFund("Y".equals(resultSet.getString("lifecycle_fund_ind")));
                    fund.setFamilyFundCd(resultSet.getString("fund_family_cd"));
    
                    // Always add non-life cycle date funds
                    if (!fund.isLifeCycleFund()) {
                        defaultInvestmentFunds.add(fund);
                    } else {
                    	// Adds one fund for each suite of Lifecycle funds.
                    	if(!fundSuites.contains(fund.getFamilyFundCd())) {
                    		fundSuites.add(fund.getFamilyFundCd());
                    		fund.setFundFamilyDisplayName(familyNames.get(fund.getFamilyFundCd()));
                    		defaultInvestmentFunds.add(fund);
                    	}
                    	
                   }
                }
                
            }
            
        } catch (SQLException e) {
            throw new SystemException(e, className, "getDefaultInvestments",
                    "Problem occurred during CONTRACT_DETAILS_SQL stored proc call. Input parameter is contractNumber:"
                    + contractNumber);
        } finally {
            close(statement, connection);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getDefaultInvestments(int contractNumber) ");
        }

        profile.setDefaultInvestments(defaultInvestmentFunds);
    }
    
    
    

    /**
     * Populates the broker share information from the result set
     * 
     * @param resultSet
     * @param brokerShareInfoMap
     * 
     * @throws SQLException
     */
    private static void populateContractBrokerShareInfoVo(Object[] resultOfBrokerShare,
            Map<String, ContractBrokerShareInformationVO> brokerShareInfoMap,String depositTypeCode) throws SQLException {

        if (logger.isDebugEnabled()) {
            logger
                    .debug("entry -> populateContractBrokerShareInfoVo(ResultSet resultSet, Map<String, ContractBrokerShareInformationVO> brokerShareInfoMap) ");
        }
        
        BigDecimal assetBasedPercentage = null;
        BigDecimal transferPercentageAccrued = null;
        BigDecimal transferPercentageUpfront = null;
        BigDecimal regularPercentageAccrued = null;
        BigDecimal regularPercentageUpfront = null;
        BigDecimal priceCreditPercentage = null;
        
        String producerCode = StringUtils.trim(resultOfBrokerShare[1].toString());
        String moneySourceCode = StringUtils
                .trim(resultOfBrokerShare[3].toString());
        String compensationTypeCode = StringUtils.trim(resultOfBrokerShare[4].toString());
        BigDecimal producerSharePercentage = (BigDecimal)resultOfBrokerShare[6];
        String brokerFirstName = resultOfBrokerShare[7].toString();
        String brokerLastName = resultOfBrokerShare[8].toString();

        if (Constants.MONEY_SOURCE_TRANSFER_CODE.equals(moneySourceCode) && Constants.COMPENSATION_TYPE_ACCRUED_CODE.equals(compensationTypeCode)) {
            transferPercentageAccrued = producerSharePercentage;
        } else if (Constants.MONEY_SOURCE_TRANSFER_CODE.equals(moneySourceCode) && Constants.COMPENSATION_TYPE_UPFRONT_CODE.equals(compensationTypeCode)) {
            transferPercentageUpfront = producerSharePercentage;
        } else if (Constants.MONEY_SOURCE_REGULAR_CODE.equals(moneySourceCode) && Constants.COMPENSATION_TYPE_ACCRUED_CODE.equals(compensationTypeCode)) {
            regularPercentageAccrued = producerSharePercentage;
        } else if (Constants.MONEY_SOURCE_REGULAR_CODE.equals(moneySourceCode) && Constants.COMPENSATION_TYPE_UPFRONT_CODE.equals(compensationTypeCode)) {
            regularPercentageUpfront = producerSharePercentage;
        } else if (Constants.COMPENSATION_TYPE_ASSET_BASED_CODE.equals(compensationTypeCode)) {
            assetBasedPercentage = producerSharePercentage;
        } else if (Constants.COMPENSATION_TYPE_PRICE_CREDIT_CODE.equals(compensationTypeCode)) {
            priceCreditPercentage = producerSharePercentage;
        }

        if (brokerShareInfoMap.containsKey(producerCode)) {
            ContractBrokerShareInformationVO vo = brokerShareInfoMap.get(producerCode);
            if (depositTypeCode.equals(resultOfBrokerShare[2].toString())) {
	            if (transferPercentageAccrued != null) {
	                vo.setTranferCommissionAccrued(transferPercentageAccrued);
	            } else if (transferPercentageUpfront != null) {
	                vo.setTranferCommissionUpfront(transferPercentageUpfront);
	            } else if (regularPercentageAccrued != null) {
	                vo.setRegularCommissionAccrued(regularPercentageAccrued);
	            } else if (regularPercentageUpfront != null) {
	                vo.setRegularCommissionUpfront(regularPercentageUpfront);
	            } else if (assetBasedPercentage != null) {
	                vo.setAssetBasedComission(assetBasedPercentage);
	            } 
            }
            if (priceCreditPercentage != null) {
                vo.setPriceCredit(priceCreditPercentage);
            }
        } else {
            ContractBrokerShareInformationVO vo = new ContractBrokerShareInformationVO();
            vo.setBrokerFirstName(brokerFirstName);
            vo.setBrokerLastName(brokerLastName);
            if (depositTypeCode.equals(resultOfBrokerShare[2].toString())) {
	            vo.setAssetBasedComission(assetBasedPercentage);
	            vo.setRegularCommissionAccrued(regularPercentageAccrued);
	            vo.setRegularCommissionUpfront(regularPercentageUpfront);
	            vo.setTranferCommissionAccrued(transferPercentageAccrued);
	            vo.setTranferCommissionUpfront(transferPercentageUpfront);
            }
            vo.setPriceCredit(priceCreditPercentage);
            brokerShareInfoMap.put(producerCode, vo);
        }

        if (logger.isDebugEnabled()) {
            logger
                    .debug("exit <- populateContractBrokerShareInfoVo(ResultSet resultSet, Map<String, ContractBrokerShareInformationVO> brokerShareInfoMap) ");
        }
    }

    /**
     * create a date instance of plan year end date
     * 
     * @param planEndDate format mmdd
     * @return Date
     */
    private static Date getPlanYearEndDate(String planEndDate) {

        if (logger.isDebugEnabled()) {
            logger.debug("entry -> getPlanYearEndDate(String planEndDate) ");
        }

        planEndDate = StringUtils.trimToEmpty(planEndDate);
        if (planEndDate.equals("")) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(planEndDate.substring(0, 2)) - 1, Integer
                .parseInt(planEndDate.substring(2, 4)));

        if (logger.isDebugEnabled()) {
            logger.debug(" exit <- getPlanYearEndDate(String planEndDate) ");
        }

        return calendar.getTime();
    }

    /**
     * Retrieves the tpa contacts for the given contract id.
     * @param contractId
     * @return
     * @throws SystemException
     */
    private static String[] getTpaContacts(int contractId)throws SystemException{
    	
    	ArrayList<String> tpaContacts = new ArrayList<String>();
    	HashMap<Long, UserInfo> profileIds = new HashMap<Long, UserInfo>();
    	
    	Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSet resultSetContractPermissions = null;
		try{
			//Fetch a list TPA contacts specific to the chosen contract
		    conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
		    statement = conn.prepareStatement(SQL_TPA_CONTACT_INFO);
		    statement.setInt(1, contractId);
		    resultSet = statement.executeQuery();
		    if (resultSet != null) {
			    while (resultSet.next()) {
			        UserInfo userInfo = null;
			        long profileId = resultSet.getLong("USER_PROFILE_ID");
			
			        if (profileIds.containsKey(Long.valueOf(profileId))) {
			            userInfo = (UserInfo) profileIds.get(Long.valueOf(profileId));
			        } else {
			            userInfo = new UserInfo();
			            LDAPUser ldapUser = LdapDao.getUserByProfileId((Long.valueOf(profileId))
	                            .toString());
	                    //Filter the converted UserInfo objects, if any user’s information is not available in the LDAP.
	                    if (ldapUser == null){
	                    	continue;
	                    }
	                    userInfo.setUserName(ldapUser.getUserName());
			        }
		            profileIds.put(Long.valueOf(profileId), userInfo);
			        userInfo.setFirstName(resultSet.getString("FIRST_NAME"));
			        userInfo.setLastName(resultSet.getString("LAST_NAME"));
			
			        userInfo.setProfileId(profileId);
			      
		            TPAFirmInfo tpaFirm = new TPAFirmInfo();
		            tpaFirm.setId(resultSet.getInt("TPA_FIRM_ID"));
		            tpaFirm.setName(resultSet.getString("THIRD_PARTY_ADMIN_NAME"));
		            ContractPermission contractPermission = new ContractPermission(ProfileDAO
	                        .convertToUserRole(resultSet.getString("SECURITY_ROLE_CODE")));
	            tpaFirm.setContractPermission(contractPermission);
	            userInfo.addTpaFirm(tpaFirm);
		            
			    } 
		    }
		    
		    //Set the TPA firm permissions detail to all the UserInfo objects.
			// Step1: Find all the firm id's to pull back.
		    if(profileIds.size() > 0){
		    	Map<Integer, TPAFirmInfo> firmInfoMap = new HashMap<Integer, TPAFirmInfo>();
				Iterator<UserInfo> it = profileIds.values().iterator();
				while (it.hasNext()) {
					UserInfo userInfo = (UserInfo) it.next();
					for (TPAFirmInfo firmInfo : userInfo
							.getTpaFirmsAsCollection()) {
						if (!firmInfoMap.containsKey(firmInfo.getId())) {
							firmInfoMap.put(firmInfo.getId(), firmInfo);
						}
					}
				}
				// Step2: retrieve the UserInfo list with all permissions.
				List<UserInfo> permissionList = SearchDAO
						.searchTpaUserFirmPermissions(firmInfoMap.values());
				it = profileIds.values().iterator();
				// Step3: Iterate over the result list and populate the firm permissions.
				while (it.hasNext()) {
					UserInfo userInfo = (UserInfo) it.next();
					for (UserInfo permInfo : permissionList) {
						if (userInfo.getProfileId() == permInfo.getProfileId()) {
							copyTpaFirmContractPermissions(permInfo, userInfo);
							break;
						}
					}
				}
			    
			    //Set the special attributes like PrimaryContact, SignReceivedAuthSigner, etc. into the Userinfo objects
			    statement = conn.prepareStatement(SQL_TPA_CONTACTS_ATTRIBUTES);
			    statement.setInt(1, contractId);
			    resultSetContractPermissions = statement.executeQuery();
			    if(resultSetContractPermissions != null) {
	            	while(resultSetContractPermissions.next()) {
	            		long profileId = resultSetContractPermissions.getLong("USER_PROFILE_ID");
	                    if (profileIds.containsKey(Long.valueOf(profileId))) {
	                    	UserInfo userInfo = (UserInfo) profileIds.get(Long.valueOf(profileId));
	                    	String contactOptionCode = resultSetContractPermissions.getString("CONTACT_OPTION_CODE");
	                    	if(contactOptionCode.equalsIgnoreCase("PRIM")) {
	                    		userInfo.setPrimaryContact(Boolean.TRUE);
	                    	} else if(contactOptionCode.equalsIgnoreCase("CLMR")) {
	                    		userInfo.setClientMailRecipient(Boolean.TRUE);
	                    	} else if(contactOptionCode.equalsIgnoreCase("TRMR")) {
	                    		userInfo.setTrusteeMailRecipient(Boolean.TRUE);
	                    	} else if(contactOptionCode.equalsIgnoreCase("STMR")) {
	                    		userInfo.setStatementRecipient(Boolean.TRUE);
	                    	} else if(contactOptionCode.equalsIgnoreCase("SRTR")) {
	                    		userInfo.setSignReceivedTrustee(Boolean.TRUE);
	                    	} else if(contactOptionCode.equalsIgnoreCase("SRAS")) {
	                    		userInfo.setSignReceivedAuthSigner(Boolean.TRUE);
	                    	} 
	                    }
	            	}
			    }
			    
			    //Filter the UserInfo objects again, if user does not have these 
			    //permissions (SigningAuthority or SignReceivedAuthSigner or PrimaryContact)
	        	Iterator<UserInfo> userInfoIterator = profileIds.values().iterator();
	        	while(userInfoIterator.hasNext()){
			     	UserInfo userInfo = userInfoIterator.next();
					TPAFirmInfo firmInfo = TPAServiceDelegate.getInstance().getFirmInfoByContractId(
							contractId);
					userInfo.setTpaContactWithSigningAuthority(isSigningAuthority(firmInfo, userInfo));
					if(!userInfo.getPrimaryContact()){
						userInfoIterator.remove();
					}
				}

	        	int contactsCount = 0;
	        	for (UserInfo userInfo : profileIds.values()) {
		            tpaContacts.add(userInfo.getFirstName()+" "+userInfo.getLastName());
		            contactsCount++;
		            if(contactsCount == 2){
		            	break;
		            }
				}
		    }
		}
		catch (SQLException e) {
		    throw new SystemException(e, 
		            "Problem occurred during Get TPA contacts method call. Input parameters are:"
		                    + "  Contract=" + contractId);
		} finally {
		    close(statement, conn);
		}
    	return tpaContacts.toArray(new String[0]);
    }
    
	/**
	 * To determine whether a Contact has SigningAuthority permission or not.
	 * 		SigningAuthority = yes, under the contRact for both firm and user
	 * 		AND SigningAuthority = yes, at user profile level
	 * 
	 * @param firmInfo
	 * @param userInfo
	 * @return
	 */
	private static boolean isSigningAuthority(TPAFirmInfo firmInfo, UserInfo userInfo){

		boolean isSigningAuthority = false;
		int profileID = Long.valueOf(userInfo.getProfileId()).intValue();
		
		if(firmInfo.getContractPermission() != null &&
				(firmInfo.getUseridToContractPermission() != null &&
						firmInfo.getUseridToContractPermission().get(profileID) != null) &&
				(userInfo.getTpaFirm(firmInfo.getId()) != null && 
						userInfo.getTpaFirm(firmInfo.getId()).getContractPermission() != null)
				){
			
			isSigningAuthority = ( // permission for TPA firm under current contract
					firmInfo.getContractPermission().isSigningAuthority()
					// permission for TPA User under current contract
							&& firmInfo.getUseridToContractPermission().get(profileID).isSigningAuthority()
					// permission for TPA User under TPA Firm(User Profile level)
							&& userInfo.getTpaFirm(firmInfo.getId()).getContractPermission()
									.isSigningAuthority());
			
		}else{
			// If none of the permissions are available for the firm OR the contact under the contRact 
			//		OR for the contact under the firm
			// then, SigningAuthority permission will be false
			isSigningAuthority = false;
		}
	
		return isSigningAuthority;
	}
	
	private static void copyTpaFirmContractPermissions(UserInfo from, UserInfo to) {
		for (TPAFirmInfo fromInfo : from.getTpaFirmsAsCollection()) {
			for (TPAFirmInfo toInfo : to.getTpaFirmsAsCollection()) {
				if (fromInfo.getId() == toInfo.getId()) {
					toInfo.setContractPermission(fromInfo
							.getContractPermission());
				}
			}
		}

	}
	public static void getTpaPrimaryContactDetails(Integer contractId,ContractInformationReportData reportData)throws SystemException{

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getTpaPrimaryContactDetails");
		}
		final String SPACE =" ";
		final String DOT = ".";
		final String ONE = "1";
		ArrayList<ContactVO> userList = new ArrayList<ContactVO>();
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {

			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn
					.prepareStatement(SQL_GET_TPA_PRIMARY_CONTACT_DETAILS);
			statement.setInt(1, contractId.intValue());
			
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ContactVO contactVO = new ContactVO();
					String contactName = rs.getString("FIRST_NAME") + SPACE
							+ rs.getString("LAST_NAME");
					contactVO.setName(contactName);
					contactVO.setEmail(rs.getString("EMAIL_ADDRESS_TEXT"));
					String areaCode  = rs.getString("PHONE_AREA_CODE");
					String phoneNumberPrefix = rs.getString("PHONE_NO_PREFIX");
					String phoneNumberSuffix = rs.getString("PHONE_NO_SUFFIX");
					String extension = rs.getString("EXTENSION_NO");
					if (StringUtils.isNotBlank(areaCode)
							&& StringUtils.isNotBlank(phoneNumberPrefix)
							&& StringUtils.isNotBlank(phoneNumberSuffix)) {
						StringBuffer contact = new StringBuffer();
						contact.append(ONE + DOT + areaCode + DOT
								+ phoneNumberPrefix + DOT + phoneNumberSuffix);
						if(StringUtils.isNotBlank(extension)){
							contact.append(" ext. "+ extension);
						}
						contactVO.setPhone(contact.toString());
					}
					userList.add(contactVO);
				}
			}
			reportData.setTpaPrimaryContactDetails(userList);

		} catch (SQLException e) {
			throw new SystemException(
					e,
					"Problem occurred during getTpaPrimaryContactDetails(). Input parameter Contract_id: "
							+ contractId);
		} finally {
			close(statement, conn);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getTpaPrimaryContactDetails");
		}
	}
	public static void getPlanSponsorPrimaryContactDetails(Integer contractId,ContractInformationReportData reportData)throws SystemException{

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getPlanSponsorPrimaryContactDetails");
		}
		final String SPACE =" ";
		final String DOT = ".";
		final String ONE = "1";
		ArrayList<ContactVO> primaryContactList = new ArrayList<ContactVO>();
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		try {

			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn
					.prepareStatement(SQL_GET_PS_PRIMARY_CONTACT_DETAILS);
			statement.setInt(1, contractId.intValue());
			
			rs = statement.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ContactVO contactVO = new ContactVO();
					String contactName = rs.getString("FIRST_NAME") + SPACE
							+ rs.getString("LAST_NAME");
					contactVO.setName(contactName);
					contactVO.setEmail(rs.getString("EMAIL_ADDRESS_TEXT"));
					String areaCode  = rs.getString("AREA_CODE");
					String phoneNumberPrefix = rs.getString("PHONE_NO_PREFIX");
					String phoneNumberSuffix = rs.getString("PHONE_NO_SUFFIX");
					String extension = rs.getString("EXTENSION_NO");
					if (StringUtils.isNotBlank(areaCode)
							&& StringUtils.isNotBlank(phoneNumberPrefix)
							&& StringUtils.isNotBlank(phoneNumberSuffix)) {
						StringBuffer contact = new StringBuffer();
						contact.append(ONE + DOT + areaCode + DOT
								+ phoneNumberPrefix + DOT + phoneNumberSuffix);
						if(StringUtils.isNotBlank(extension)){
							contact.append(" ext. "+ extension);
						}
						contactVO.setPhone(contact.toString());
					}
					primaryContactList.add(contactVO);
				}
			}
			reportData.setPlanSponsorPrimaryContactDetails(primaryContactList);

		} catch (SQLException e) {
			throw new SystemException(
					e,
					"Problem occurred during getPlanSponsorPrimaryContactDetails(). Input parameter Contract_id: "
							+ contractId);
		} finally {
			close(statement, conn);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getPlanSponsorPrimaryContactDetails");
		}
	}

	
	/**
	 * Method to fetch Contract selected ManagedAccount Service Description 
	 * @param contractId
	 * @return
	 * @throws SystemException
	 */
	private static String getManagedAccountServiceDesc(int contractId) throws SystemException {
		ManagedAccountServiceFeatureLite service;
		try {
			service = ContractServiceDelegate.getInstance()
					.getContractSelectedManagedAccountServiceLite(contractId);
			if (service != null) {
				return service.getServiceDescription();
			}
		} catch (SystemException e) {
			logger.error("Exception thrown in getManagedAccountServiceDesc of ContractInformationDAO " + e.getMessage());
			throw ExceptionHandlerUtility.wrap(e);
		}
		return null;
	}
	public static void getRMUserInfo(ReportCriteria criteria,Integer contractId, ContractSummaryVO contractSummaryVO) 
			throws SystemException {

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		contractSummaryVO.setRmContractExist(false);
		try {
			Date asOfDate = null;

			asOfDate = (Date) criteria.getFilterValue(ContractInformationReportData.FILTER_AS_OF_DATE);

			if (asOfDate == null) {
				asOfDate = new java.util.Date();
			}

			conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			statement = conn.prepareStatement(SQL_GET_RELATIONSHIP_MANAGER_DETAILS);
			statement.setDate(1, new java.sql.Date(asOfDate.getTime()));
			statement.setDate(2, new java.sql.Date(asOfDate.getTime()));
			statement.setDate(3, new java.sql.Date(asOfDate.getTime()));
			statement.setDate(4, new java.sql.Date(asOfDate.getTime()));
			statement.setDate(5, new java.sql.Date(asOfDate.getTime()));
			statement.setDate(6, new java.sql.Date(asOfDate.getTime()));
			statement.setInt(7, contractId);
			rs = statement.executeQuery();
			if (rs != null && rs.next()) {

					if (StringUtils.isNotBlank(rs.getString("FIRST_NAME"))
							|| StringUtils.isNotBlank(rs.getString("LAST_NAME"))) {
						contractSummaryVO.setRmContractExist(true);
						contractSummaryVO.setRmUserName(convertNullToEmpty(rs.getString("FIRST_NAME")) + " "
								+ convertNullToEmpty(rs.getString("LAST_NAME")));

						if (contractSummaryVO.getRmUserName() != null
								&& (contractSummaryVO.getRmUserName().contains("IRM")
										|| contractSummaryVO.getRmUserName().contains("Team")
										|| contractSummaryVO.getRmUserName().contains("TEAM"))) {
							contractSummaryVO.setRmContractExist(false);
						}

						if (StringUtils.isNotBlank(rs.getString("EMAIL_ADDR"))) {
							contractSummaryVO.setRmUserEmail(rs.getString("EMAIL_ADDR").trim());
						}

						if (StringUtils.isNotBlank(rs.getString("PHONE_NO"))) {

							String phoneNumberFormat = formatPhoneNumber(rs.getString("PHONE_NO").trim());
							contractSummaryVO.setRmUserPhoneNumber(phoneNumberFormat);

						}
						
						if (StringUtils.isNotBlank(rs.getString("PHONE_EXT_NO"))) {
							contractSummaryVO.setRmUserPhoneExtension(rs.getString("PHONE_EXT_NO").trim());
						}

					}
			}
			
		} catch (SQLException e) {
			throw new SystemException(e, "Problem occurred during getRMUserInfo() method call");
		} finally {
			close(statement, conn);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getPlanSponsorPrimaryContactDetails");
		}
	}
	
	
	
	private static String formatPhoneNumber(String rmUserPhoneNumber) {
		final String DOT = ".";
		String phoneNumberFormat = "";
		if (rmUserPhoneNumber.length() > 0) {
			String areaCode = rmUserPhoneNumber.substring(0, 3);
			String phoneNumberPrefix = rmUserPhoneNumber.substring(3, 6);
			String phoneNumberSuffix = rmUserPhoneNumber.substring(6);
			phoneNumberFormat ="1"+ DOT + areaCode + DOT + phoneNumberPrefix + DOT + phoneNumberSuffix;
		}

		return phoneNumberFormat;

	}

	private static String convertNullToEmpty(String rmName) {
		if (rmName == null) {
			rmName = "";
		}
		else{
			rmName=rmName.replaceAll("\\s", "");
		}

		return rmName;

	}
}