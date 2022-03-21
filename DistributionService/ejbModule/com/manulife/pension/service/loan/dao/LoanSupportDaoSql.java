package com.manulife.pension.service.loan.dao;

import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.dao.DaoConstants;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.employee.EmployeeConstants;

public interface LoanSupportDaoSql extends DaoConstants {

	String PARTICIPANT_ID_BY_PROFILE_ID = "SELECT PARTICIPANT_ID FROM "
			+ SchemaName.CUSTOMER_SERVICE
			+ "PARTICIPANT_CONTRACT WHERE PROFILE_ID = ? FOR FETCH ONLY";

	String PLAN_DATA = " select p.minimum_loan_amt minimumLoanAmount"
			+ "   , p.maximum_loan_amt maximumLoanAmount"
			+ "   , p.maximum_loan_pct maximumLoanPercentage"
			+ "   , p.participant_concur_loan_count maxNumberOfOutstandingLoans"
			+ "   , p.loan_interest_over_prime_amt loanInterestRateOverPrime"
			+ "   , p.payroll_frequency_ind payrollFrequency"
			+ "   , p.spousal_consent_reqd_ind spousalConsentReqdInd " 
			+ "   , p.plan_legal_name planLegalName " 
			+ "   , cc.contract_status_code contractStatusCode "
			+ "   , cc.contract_name contractName "
			+ "   , cc.loan_setup_fee_amt contractLoanSetupFeeAmount "
			/*+ "   , cc.LOAN_EXPENSE_MARGIN_PCT contractLoanExpenseMarginPct "
			+ "   , cc.LOAN_MONTHLY_FLAT_FEE_AMT contractLoanMonthlyFlatFee "*/
            + "   , cc.THIRD_PARTY_ADMIN_ID thirdPartyAdminId "
            + "   , cc.MANULIFE_COMPANY_ID manulifeCompanyId "
			+ " FROM "
			+ SchemaName.CUSTOMER_SERVICE + "contract_cs cc, "
			+ SchemaName.CUSTOMER_SERVICE + "plan p where"
			+ "   p.plan_id = cc.plan_id and cc.contract_id = ?";
	
	String PLAN_DATA_FEES =   "WITH t0103_busdate (busdate) "
			+ "as (select batch_cycle_run_date "
			+ "  from Psw100.Apollo_Rundate   t0103 "
			+ "where t0103.batch_run_environment  = 'APOLLO' "
			+ "   and t0103.batch_cycle_id  = 'BUSDATE' "
			+ "   ) "
			+ "select t1023_cff.feature_fee_range_min_amt "
			+ ", t1023_cff.feature_fee_range_max_amt "
			+ "  ,sum(t1023_cff.feature_fee_amt)                        feeamt "
			+ " ,sum(t1023_cff.feature_fee_pct)                        chrgpcnt "
			+ "  from  PSW100.CONTRACT_FEATURE_FEE_HIST  t1023_cff "
			+ "      ,t0103_busdate "
			+ "where t1023_cff.contract_id = ? "
			+ "   and t1023_cff.product_feature_type_code                        in ('LRK','XRK') "
			+ "   and t1023_cff.product_feature_fee_type_code                        = 'LRC' "
			+ "   and t1023_cff.start_date   <=  t0103_busdate.busdate "
			+ "   and t1023_cff.end_date      >   t0103_busdate.busdate "
			+ "and To_date(CHAR(CAST(t1023_cff.feature_fee_range_min_amt as       DECIMAL(8, 0))), 'YYYYMMDD' )       <=   t0103_busdate.busdate "
			+ "and To_date(CHAR(CAST(t1023_cff.feature_fee_range_max_amt  as       DECIMAL(8, 0))), 'YYYYMMDD')       >=   t0103_busdate.busdate "
			+ "group by t1023_cff.contract_id "
			+ ", t1023_cff.feature_fee_range_min_amt "
			+ ", t1023_cff.feature_fee_range_max_amt";

    String SELECT_PLAN_LOAN_DATA = 
            "SELECT pl.loan_type_code, pl.max_amortization_years "
            + "FROM "
            + SchemaName.CUSTOMER_SERVICE + "contract_cs c, " 
            + SchemaName.CUSTOMER_SERVICE + "plan_loan pl " 
            + "WHERE c.contract_id = ? "
            + "AND   c.plan_id = pl.plan_id ";

	String PBA_BALANCE = "SELECT "
		+ "      COALESCE(SUM(PCB.TOTAL_BALANCE_AMT), 0) AS PBA_BALANCE "
		+ "FROM "
		+ "      PSW100.PARTICIPANT_CURRENT_BAL_LSA PCB "
		+ "WHERE "
	    + "      PCB.PROFILE_ID = ? "
		+ "  AND PCB.CONTRACT_ID = ? "
		+ "  AND (PCB.INVESTMENT_OPTION_ID LIKE 'PBA%' OR "
		+ "       PCB.INVESTMENT_OPTION_ID LIKE 'NPB%') "
		+ "FOR READ ONLY ";

	String OUTSTANDING_LOAN_COUNT = "SELECT COUNT(*) FROM PSW100.PARTICIPANT_LOAN WHERE "
		+ "PARTICIPANT_ID = ? AND CONTRACT_ID = ? "
		+ "FOR READ ONLY ";

	String COUNT_MONEY_TYPE_ALLOWED_FOR_LOAN = "SELECT COUNT(*) FROM "
			+ SchemaName.CUSTOMER_SERVICE
			+ "contract_money_type cmt WHERE cmt.loans_allowed_ind = 'Y' AND cmt.contract_id = ? FOR fetch only";
	
	// Retrieve a list of money types and their balance (excluding
	// the LSA and PBA investment option types)
	// excluding UM money types
	String PARTICIPANT_MONEY_TYPES_BALANCE_LIST = "SELECT "
			+ "  CB.MONEY_TYPE_ID moneyTypeId"
			+ "  ,CASE "
			+ "    WHEN (RTRIM(CMT.CONTRACT_MONEY_TYPE_SHORT_NAME)) <> '' "
			+ "      THEN RTRIM(CMT.CONTRACT_MONEY_TYPE_SHORT_NAME) "
			+ "    WHEN (RTRIM(CMT.MONEY_TYPE_ALIAS_ID)) <> '' "
			+ "      THEN RTRIM(CMT.MONEY_TYPE_ALIAS_ID) "
			+ "    ELSE "
			+ "      RTRIM(MT.MONEY_TYPE_ALIAS_ID) "
			+ "  END AS contractMoneyTypeShortName"
			+ "  ,CASE "
			+ "    WHEN (RTRIM(CMT.CONTRACT_MONEY_TYPE_LONG_NAME)) <> '' "
			+ "      THEN RTRIM(CMT.CONTRACT_MONEY_TYPE_LONG_NAME) "
			+ "    WHEN (RTRIM(CMT.MONEY_TYPE_ALIAS_ID)) <> '' "
			+ "      THEN RTRIM(CMT.MONEY_TYPE_ALIAS_ID) "
			+ "    ELSE "
			+ "      RTRIM(MT.MONEY_TYPE_ALIAS_ID) "
			+ "  END AS contractMoneyTypeLongName"
			+ "  ,CASE "
			+ "    WHEN (CMT.LOANS_ALLOWED_IND = 'N') "
			+ "      THEN 'Y' "
			+ "    ELSE "
			+ "      'N' "
			+ "   END AS excludeIndicator"
			+ "  ,CMT.MONEY_TYPE_ALIAS_ID moneyTypeAliasId"
			+ "  ,RTRIM(MT.MONEY_TYPE_CATEGORY_CODE) AS moneyTypeCategoryCode "
			+ "  ,CB.TOTAL_BALANCE_AMT accountBalance "
			+ "FROM "
			+ "  ( "
			+ "    SELECT "
			+ "      PCB.PARTICIPANT_ID "
			+ "      ,PCB.CONTRACT_ID  "
			+ "      ,PCB.MONEY_TYPE_ID "
			+ "      ,SUM(PCB.TOTAL_BALANCE_AMT) AS TOTAL_BALANCE_AMT "
			+ "    FROM  "
			+ "      PSW100.PARTICIPANT_CURRENT_BAL_LSA AS PCB "
			+ "    WHERE  "
			+ "      PCB.PROFILE_ID=? "
			+ "      AND PCB.CONTRACT_ID=? "
            + "      AND PCB.INVESTMENT_OPTION_ID NOT LIKE 'LSA%' " 
		    + "      AND PCB.INVESTMENT_OPTION_ID NOT LIKE 'PBA%'  "
		    + "      AND PCB.INVESTMENT_OPTION_ID NOT LIKE 'NPB%'  "
			+ "    GROUP BY  "
			+ "      PCB.PARTICIPANT_ID "
			+ "      ,PCB.CONTRACT_ID "
			+ "      ,PCB.MONEY_TYPE_ID "
			+ "  ) AS CB "
			+ "  ,PSW100.CONTRACT_MONEY_TYPE AS CMT "
			+ "  ,EZK100.MONEY_TYPE AS MT "
			+ "WHERE  "
			+ "  CB.CONTRACT_ID=CMT.CONTRACT_ID AND CMT.MONEY_TYPE_ID=CB.MONEY_TYPE_ID "
			+ "  AND CB.TOTAL_BALANCE_AMT > 0 "
			+ "  AND MT.MONEY_TYPE_GROUP <> '" + EmployeeConstants.MoneyTypeGroup.MONEY_TYPE_UM + "' "
			+ "  AND MT.MONEY_TYPE_CODE = CB.MONEY_TYPE_ID ORDER BY  "
			+ "  RTRIM(MT.MONEY_TYPE_CATEGORY_CODE), contractMoneyTypeShortName FOR READ ONLY ";

	String PARTICIPANT_MONEY_TYPES_LOAN_BALANCE_LIST = "SELECT "
			+ "  CB.MONEY_TYPE_ID moneyTypeId"
			+ "  ,CASE "
			+ "    WHEN (RTRIM(CMT.CONTRACT_MONEY_TYPE_SHORT_NAME)) <> '' "
			+ "      THEN RTRIM(CMT.CONTRACT_MONEY_TYPE_SHORT_NAME) "
			+ "    WHEN (RTRIM(CMT.MONEY_TYPE_ALIAS_ID)) <> '' "
			+ "      THEN RTRIM(CMT.MONEY_TYPE_ALIAS_ID) "
			+ "    ELSE "
			+ "      RTRIM(MT.MONEY_TYPE_ALIAS_ID) "
			+ "  END AS contractMoneyTypeShortName"
			+ "  ,CASE "
			+ "    WHEN (RTRIM(CMT.CONTRACT_MONEY_TYPE_LONG_NAME)) <> '' "
			+ "      THEN RTRIM(CMT.CONTRACT_MONEY_TYPE_LONG_NAME) "
			+ "    WHEN (RTRIM(CMT.MONEY_TYPE_ALIAS_ID)) <> '' "
			+ "      THEN RTRIM(CMT.MONEY_TYPE_ALIAS_ID) "
			+ "    ELSE "
			+ "      RTRIM(MT.MONEY_TYPE_ALIAS_ID) "
			+ "  END AS contractMoneyTypeLongName"
			+ "  ,CASE "
			+ "    WHEN (CMT.LOANS_ALLOWED_IND = 'N') "
			+ "      THEN 'Y' "
			+ "    ELSE "
			+ "      'N' "
			+ "   END AS excludeIndicator"
			+ "  ,CMT.MONEY_TYPE_ALIAS_ID moneyTypeAliasId"
			+ "  ,RTRIM(MT.MONEY_TYPE_CATEGORY_CODE) AS moneyTypeCategoryCode "
			+ "  ,CB.TOTAL_BALANCE_AMT loanBalance "
			+ "FROM "
			+ "  ( "
			+ "    SELECT "
			+ "      PCB.PARTICIPANT_ID "
			+ "      ,PCB.CONTRACT_ID  "
			+ "      ,PCB.MONEY_TYPE_ID "
			+ "      ,SUM(PCB.TOTAL_BALANCE_AMT) AS TOTAL_BALANCE_AMT "
			+ "    FROM  "
			+ "      PSW100.PARTICIPANT_CURRENT_BAL_LSA AS PCB "
			+ "    WHERE  "
			+ "      PCB.PROFILE_ID=? "
			+ "      AND PCB.CONTRACT_ID=? "
            + "      AND PCB.INVESTMENT_OPTION_ID LIKE 'LSA%' " 
			+ "    GROUP BY  "
			+ "      PCB.PARTICIPANT_ID "
			+ "      ,PCB.CONTRACT_ID "
			+ "      ,PCB.MONEY_TYPE_ID "
			+ "  ) AS CB "
			+ "  ,PSW100.CONTRACT_MONEY_TYPE AS CMT "
			+ "  ,EZK100.MONEY_TYPE AS MT "
			+ "WHERE  "
			+ "  CB.CONTRACT_ID=CMT.CONTRACT_ID AND CMT.MONEY_TYPE_ID=CB.MONEY_TYPE_ID "
			+ "  AND CB.TOTAL_BALANCE_AMT > 0 "
			+ "  AND MT.MONEY_TYPE_GROUP <> '" + EmployeeConstants.MoneyTypeGroup.MONEY_TYPE_UM + "' "
			+ "  AND MT.MONEY_TYPE_CODE = CB.MONEY_TYPE_ID ORDER BY  "
			+ "  RTRIM(MT.MONEY_TYPE_CATEGORY_CODE), contractMoneyTypeShortName FOR READ ONLY ";

	// Query to retrieve outstanding old i:loan requests for a given contract
	String OUTSTANDING_OLD_ILOAN_REQUEST_COUNT = "SELECT COUNT(*) " +
			"FROM  " +
			SchemaName.CUSTOMER_SERVICE +
			"PARTICIPANT_LOAN_REQUEST " +
			"WHERE CONTRACT_ID=? " +
			"AND (REQ_EXPIRY_DATE > CURRENT DATE " +
			"OR APV_EXPIRY_DATE >= CURRENT DATE) " +
			"AND LOAN_REQUEST_STATUS_CODE IN ('RE', 'PE', 'AP') FOR FETCH ONLY";
	
	String LRK_FEATURE_SQL = "SELECT COUNT(*) FROM " +
				SchemaName.CUSTOMER_SERVICE +
				"CONTRACT_PRODUCT_FEATURE "+
				"WHERE CONTRACT_ID = ? " +
				"AND PRODUCT_FEATURE_TYPE_CODE = 'LRK01' FOR FETCH ONLY";

	String GET_PARTIAL_LOAN_SETTINGS =
			" SELECT A.CONTRACT_ID ,A.PRODUCT_FEATURE_TYPE_CODE, " +			
			" B.SERVICE_FEATURE_CODE FEATURE, " +
			" B.SERVICE_FEATURE_VALUE " +
			" FROM " + SchemaName.CUSTOMER_SERVICE +
			"CONTRACT_PRODUCT_FEATURE A LEFT OUTER JOIN " +
			" ( SELECT CONTRACT_ID, SERVICE_FEATURE_CODE , " +
			" SERVICE_FEATURE_VALUE FROM " +
			SchemaName.CUSTOMER_SERVICE + "CONTRACT_SERVICE_FEATURE " +  
			" WHERE SERVICE_FEATURE_CODE = 'UOL' ) AS B" +
			" ON A.CONTRACT_ID = B.CONTRACT_ID"+
			" WHERE A.PRODUCT_FEATURE_TYPE_CODE = 'LRK01'"+
			" AND A.CONTRACT_ID IN " ; 	
	
	String GET_PARTICIPANT_PROFILE_ID_FOR_SUBMISSION =
		" SELECT PROFILE_ID FROM SUBMISSION_LOAN " +
		" WHERE SUBMISSION_ID = ? " +
		" AND CONTRACT_ID = ? FOR FETCH ONLY";

	// Query to retrieve the data needed for Event recipients generation
	String SELECT_LOAN_INITIATOR_FOR_EVENTS = 
		" SELECT A.CREATED_USER_PROFILE_ID AS PROFILEID, A.CREATED_BY_ROLE_CODE AS ROLECODE " +  
		" FROM " + SchemaName.STP + " SUBMISSION_LOAN A WHERE A.SUBMISSION_ID = ? ";
	
	// Query to retrieve the data needed for Event recipients generation
	String SELECT_LOAN_REVIEWER_FOR_EVENTS = 
		" SELECT B.CREATED_USER_PROFILE_ID AS PROFILEID, B.STATUS_CODE AS STATUS " +
		" FROM " + SchemaName.STP + " ACTIVITY_SUMMARY B " + 
		" WHERE B.SUBMISSION_ID = ? AND B.STATUS_CODE IN ('" + 
		ActivitySummary.SENT_FOR_APPROVAL + 
		"', '" + ActivitySummary.APPROVED +
		"', '" + ActivitySummary.SENT_FOR_ACCEPTANCE+
		" ') ";
	
	// Query to retrieve the name of the contract
	String GET_CONTRACT_NAME_SQL =
		" SELECT CONTRACT_NAME FROM " +
		SchemaName.CUSTOMER_SERVICE +
		" CONTRACT_CS WHERE CONTRACT_ID = ?";
	
	String SQL_SELECT_OUTSTANDING_LOAN = 
  	 	  "select "
		+ "PROFILE_ID,"
		+ "PARTICIPANT_ID,"
		+ "CONTRACT_ID,"
		+ "LOAN_ID,"
		+ "LOAN_PRINCIPAL_AMT,"
		+ "EFFECTIVE_DATE,"
		+ "LOAN_INTEREST_RATE_PCT,"
		+ "OUTSTANDING_PRINCIPAL_AMT,"
		+ "OUTSTANDING_INTEREST_AMT,"
		+ "MATURITY_DATE,"
		+ "LOAN_EXPENSE_MARGIN_RATE,"
		+ "LAST_UPDATED_TS,"
		+ "LAST_REPAYMENT_DATE,"
		+ "LAST_REPAYMENT_AMT,"
		+ "LAST_REPAYMENT_TRANSACTION_NO,"
		+ "LOAN_CREATE_DATE "
		+ "from "
		+ BaseDatabaseDAO.CUSTOMER_SCHEMA_NAME
		+ "Participant_Loan  "
		+ "where  "
		+ "contract_id = ? and "
		+ "profile_Id = ? and "
		+ "loan_id = ? "
		+ "for read only ";
	
	
}

