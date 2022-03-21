package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;


public interface DeclarationDaoSql {

    public static final String SQL_INSERT_DECLARATIONS = new StringBuffer()
            .append("INSERT INTO ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("DECLARATION (")
            .append("SUBMISSION_ID,")
            .append("DECLARATION_TYPE_CODE,")
            .append("CREATED_USER_PROFILE_ID,")
            .append("CREATED_TS")
            .append(") VALUES (?,?,?,CURRENT TIMESTAMP)").toString();

    public static final String SQL_DELETE_ALL_DECLARATIONS = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("DECLARATION where submission_id = ?").toString();

    public static final String SQL_DELETE_SPECIFIC_DECLARATION_TYPES = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("DECLARATION where submission_id = ? ")
            .append("and declaration_type_code in (?, ?)").toString();

    public static final int[] DELETE_SPECIFIC_DECLARATION_TYPES_PARAMETER_TYPES = { Types.INTEGER, Types.CHAR };

    public static final String SELECT_ALL_DECLARATIONS_SQL = new StringBuffer()
	    .append("SELECT ")
        .append("SUBMISSION_ID,")
        .append("DECLARATION_TYPE_CODE,")
        .append("CREATED_USER_PROFILE_ID,")
        .append("CREATED_TS")
	    .append(" FROM   ")
	    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
	    .append("DECLARATION ")
	    .append("WHERE SUBMISSION_ID = ?").toString();
	
	public static final int[] SELECT_ALL_DECLARATIONS_PARAMETER_TYPES = { Types.INTEGER };
	
	public static final String[] SELECT_ALL_DECLARATIONS_RESULT_FIELDS = {
			"submissionId", "typeCode", "createdById", "created" };

}
