package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public interface FeeDaoSql {

    public static final int[] INSERT_TYPES = { Types.INTEGER, Types.CHAR, Types.DECIMAL,
            Types.DECIMAL, Types.DECIMAL, };

    public static final String SQL_INSERT_FEE = new StringBuffer()
            .append("INSERT INTO ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("FEE (")
            .append("SUBMISSION_ID,")
            .append("FEE_TYPE_CODE,")
            .append("FEE_VALUE,")
            .append("CREATED_USER_PROFILE_ID,")
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")
            .append("LAST_UPDATED_TS")
            .append(") VALUES (?,?,?,?,CURRENT TIMESTAMP,?,CURRENT TIMESTAMP)").toString();

    public static final String SQL_UPDATE_FEE = new StringBuffer()
            .append("update ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("FEE  set ")
            .append("FEE_TYPE_CODE = ?,")
            .append("FEE_VALUE = ?,")
            .append("LAST_UPDATED_USER_PROFILE_ID = ?,")
            .append("LAST_UPDATED_TS = CURRENT TIMESTAMP ")
            .append("WHERE SUBMISSION_ID = ?").toString();
    

    public static final int[] UPDATE_TYPES = { Types.CHAR, Types.DECIMAL, Types.DECIMAL,
            Types.INTEGER };
    
    public static final String SQL_DELETE_FEE = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("FEE where ") 
            .append("submission_id = ?").toString();
    
    
    public static final String SQL_DELETE_ALL_FEES = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("FEE where submission_id = ?").toString();


    public static final String SELECT_ALL_FEES_SQL = new StringBuffer()
            .append("SELECT ")
            .append("F.SUBMISSION_ID,")
            .append("F.FEE_TYPE_CODE,")
            .append("F.FEE_VALUE,")
            .append("F.CREATED_USER_PROFILE_ID,")
            .append("F.CREATED_TS,")
            .append("F.LAST_UPDATED_USER_PROFILE_ID,")
            .append("F.LAST_UPDATED_TS ")
            .append(" FROM   ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("FEE F")
            .append(" WHERE  F.SUBMISSION_ID = ?").toString();
    
    public static final int[] SELECT_ALL_FEES_PARAMETER_TYPES = { Types.INTEGER };

    public static final String[] SELECT_ALL_FEES_RESULT_FIELDS = { "submissionId", "typeCode",
            "value", "createdById", "created", "lastUpdatedById", "lastUpdated" };
}
