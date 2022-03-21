package com.manulife.pension.service.loan.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public interface LoanMoneyTypeDaoSql {
    
    public static final int[] INSERT_TYPES = { Types.INTEGER, Types.CHAR,
        Types.DECIMAL, Types.DECIMAL, Types.DECIMAL, Types.CHAR, Types.CHAR, Types.DECIMAL,
        Types.TIMESTAMP, Types.DECIMAL, Types.TIMESTAMP };

    public static final String SQL_INSERT_MONEY_TYPE = new StringBuffer()
            .append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("LOAN_MONEY_TYPE (")
            .append("SUBMISSION_ID,")//Types.INTEGER
            .append("MONEY_TYPE_ID,") //Types.CHAR
            .append("VESTING_PCT,")// Types.DECIMAL
            .append("MONEY_TYPE_ACCT_BALANCE_AMT,")// Types.DECIMAL
            .append("MONEY_TYPE_LOAN_BALANCE_AMT,")// Types.DECIMAL
            .append("MONEY_TYPE_EXCLUDE_IND,")// Types.CHAR
            .append("VESTING_PCT_UPDATABLE_IND,") // Types.CHAR
            .append("CREATED_USER_PROFILE_ID,")// Types.DECIMAL
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")// Types.DECIMAL
            .append("LAST_UPDATED_TS")
            .append(") VALUES (?,?,?,?,?,?,?,?,?,?,?)").toString();

    public static final int[] UPDATE_TYPES = { Types.DECIMAL, Types.DECIMAL, Types.DECIMAL,
			Types.CHAR, Types.CHAR, Types.DECIMAL, Types.TIMESTAMP, Types.INTEGER,
			Types.CHAR };
    
    
    public static final String SQL_UPDATE_MONEY_TYPE = new StringBuffer()
            .append("update ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("LOAN_MONEY_TYPE set ")
            .append("VESTING_PCT = ?,") //Types.DECIMAL
            .append("MONEY_TYPE_ACCT_BALANCE_AMT = ?,")// Types.DECIMAL
            .append("MONEY_TYPE_LOAN_BALANCE_AMT = ?,")// Types.DECIMAL
            .append("MONEY_TYPE_EXCLUDE_IND = ?,") // Types.CHAR
            .append("VESTING_PCT_UPDATABLE_IND = ?, ") // Types.CHAR
            .append("LAST_UPDATED_USER_PROFILE_ID = ?,") // Types.DECIMAL 
            .append("LAST_UPDATED_TS = ? ") // Types.TIMESTAMP
            .append("WHERE SUBMISSION_ID = ? AND MONEY_TYPE_ID = ?").toString();  // Types.DECIMAL, Types.CHAR
    
    public static final String SQL_CHECK_EXISTS = new StringBuffer()
            .append("select submission_id from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("LOAN_MONEY_TYPE ")
            .append("where submission_id = ? and money_type_id = ? FOR FETCH ONLY").toString();
            
    public static final String SQL_DELETE_ALL_MONEY_TYPES = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("LOAN_MONEY_TYPE where submission_id = ?").toString();
    
    public static final String SQL_SELECT_CONTRACT_MONEY_TYPE_INFO = "SELECT "
			+ "  CMT.MONEY_TYPE_ID moneyTypeId"
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
			+ "  ,RTRIM(MT.MONEY_TYPE_CATEGORY_CODE) AS moneyTypeCategoryCode "
			+ "  ,CMT.MONEY_TYPE_ALIAS_ID moneyTypeAliasId"
			+ " FROM "
			+ "  PSW100.CONTRACT_MONEY_TYPE AS CMT "
			+ "  ,EZK100.MONEY_TYPE AS MT "
			+ "WHERE  "
			+ "  CMT.MONEY_TYPE_ID = ? "
			+ "  AND CMT.CONTRACT_ID = ? "
			+ "  AND MT.MONEY_TYPE_CODE = CMT.MONEY_TYPE_ID FOR READ ONLY ";
    
    public static final String SQL_SELECT_MONEY_TYPES = new StringBuffer()
    		.append("select ")
			.append("MONEY_TYPE_ID,")
			.append("VESTING_PCT,")
			.append("MONEY_TYPE_ACCT_BALANCE_AMT,")
			.append("MONEY_TYPE_LOAN_BALANCE_AMT,")
			.append("MONEY_TYPE_EXCLUDE_IND,")
			.append("CREATED_USER_PROFILE_ID,")
			.append("VESTING_PCT_UPDATABLE_IND,")
			.append("CREATED_TS,")
			.append("LAST_UPDATED_USER_PROFILE_ID,")
			.append("LAST_UPDATED_TS ")
			.append("from ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME).append("loan_money_type ")
			.append("where submission_id = ? FOR FETCH ONLY").toString();


}
