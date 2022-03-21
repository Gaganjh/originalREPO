package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public interface RecipientDaoSql  {

    public static final String SQL_INSERT_RECIPIENT = new StringBuffer()
            .append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("RECIPIENT (")
            .append("SUBMISSION_ID,")
            .append("RECIPIENT_NO,")
            .append("FIRST_NAME,")
            .append("LAST_NAME,")
            .append("ORGANIZATION_NAME,")
            .append("US_CITIZEN_IND,")
            .append("RESIDENCE_STATE_CODE,")
            .append("SHARE_TYPE_CODE,")
            .append("SHARE_VALUE,")
            .append("FEDERAL_TAX_PCT,")
            .append("STATE_TAX_PCT,")
            .append("STATE_TAX_TYPE_CODE,")
            .append("TAXPAYER_IDENT_TYPE_CODE,")
            .append("TAXPAYER_IDENT_NO,")
            .append("CREATED_USER_PROFILE_ID,")
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")
            .append("LAST_UPDATED_TS")
            .append(") values (")
            .append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, CURRENT TIMESTAMP, ?,  CURRENT TIMESTAMP) ")
            .toString();

    public static final String SQL_DELETE_RECIPIENT = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("RECIPIENT where ") 
            .append("submission_id = ?").toString();
    
    public static final int[] INSERT_TYPES = { Types.INTEGER, Types.SMALLINT, Types.VARCHAR,
            Types.VARCHAR, Types.VARCHAR, Types.CHAR, Types.CHAR, Types.CHAR, Types.DECIMAL,
            Types.DECIMAL, Types.DECIMAL, Types.CHAR, Types.CHAR, Types.CHAR, Types.DECIMAL,
            Types.DECIMAL };
    

    public static final String SELECT_ALL_RECIPIENTS_SQL = new StringBuffer()
	    .append("SELECT ")
	    .append("SUBMISSION_ID,")
	    .append("RECIPIENT_NO,")
        .append("FIRST_NAME,")
        .append("LAST_NAME,")
        .append("ORGANIZATION_NAME,")
        .append("US_CITIZEN_IND,")
        .append("RESIDENCE_STATE_CODE,")
        .append("SHARE_TYPE_CODE,")
        .append("SHARE_VALUE,")
        .append("FEDERAL_TAX_PCT,")
        .append("STATE_TAX_PCT,")
        .append("STATE_TAX_TYPE_CODE,")
        .append("TAXPAYER_IDENT_TYPE_CODE,")
        .append("TAXPAYER_IDENT_NO,")
        .append("CREATED_USER_PROFILE_ID,")
        .append("CREATED_TS,")
        .append("LAST_UPDATED_USER_PROFILE_ID,")
        .append("LAST_UPDATED_TS")
	    .append(" FROM   ")
	    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
	    .append("RECIPIENT ")
	    .append("WHERE SUBMISSION_ID = ?").toString();

    public static final int[] SELECT_ALL_RECIPIENTS_PARAMETER_TYPES = { Types.INTEGER };

    public static final String[] SELECT_ALL_RECIPIENTS_RESULT_FIELDS = {
			"submissionId", "recipientNo", "firstName", "lastName",
			"organizationName", "usCitizenInd", "stateOfResidenceCode",
			"shareTypeCode", "shareValue", "federalTaxPercent",
			"stateTaxPercent", "stateTaxTypeCode", "taxpayerIdentTypeCode",
			"taxpayerIdentNo", "createdById", "created", "lastUpdatedById",
			"lastUpdated" };

}


