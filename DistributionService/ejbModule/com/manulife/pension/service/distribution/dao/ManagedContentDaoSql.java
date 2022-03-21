package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public interface ManagedContentDaoSql {
    
    public static final int[] INSERT_TYPES = { Types.INTEGER, Types.INTEGER,
			Types.INTEGER, Types.CHAR, Types.CHAR, Types.DECIMAL, Types.TIMESTAMP};
    
    public static final String SQL_INSERT_MANAGED_CONTENT = new StringBuffer()
            .append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("MANAGED_CONTENT_REFERENCE (")
            .append("SUBMISSION_ID,")//Types.INTEGER
            .append("CONTENT_KEY,") //Types.INTEGER
            .append("CONTENT_ID,")// Types.INTEGER
            .append("CMA_SITE_CODE,")// Types.CHAR
            .append("CONTENT_TYPE_CODE,")// Types.CHAR
            .append("CREATED_USER_PROFILE_ID,")// Types.DECIMAL
            .append("CREATED_TS ")
            .append(") VALUES (?,?,?,?,?,?,?)").toString();

    public static final String SQL_DELETE_ALL_MANAGED_CONTENT = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("MANAGED_CONTENT_REFERENCE where submission_id = ?").toString();

    public static final String SQL_DELETE_SPECIFIC_MANAGED_CONTENT_TYPES = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("MANAGED_CONTENT_REFERENCE where submission_id = ?")
            .append("and content_type_code in (?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();

    public static final String SQL_SELECT_MANAGED_CONTENTS = new StringBuffer()
		    .append("select ")
			.append("CONTENT_KEY,")
			.append("CONTENT_ID,")
			.append("CMA_SITE_CODE,")
			.append("CONTENT_TYPE_CODE,")
			.append("CREATED_USER_PROFILE_ID,")
			.append("CREATED_TS ")
			.append("from ")
			.append(BaseDatabaseDAO.STP_SCHEMA_NAME).append("MANAGED_CONTENT_REFERENCE ")
			.append("where submission_id = ?").toString();
    



}
