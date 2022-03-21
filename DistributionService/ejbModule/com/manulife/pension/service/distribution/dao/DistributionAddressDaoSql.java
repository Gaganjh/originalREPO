package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;


public interface DistributionAddressDaoSql {
    public static final String SQL_DELETE_ADDRESSES = new StringBuffer().append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME).append("DISTRIBUTION_ADDRESS where ").append(
                    "submission_id = ? and DISTRIBUTION_TYPE_CODE = ? ").toString();

    public static final String SQL_INSERT_ADDRESS = new StringBuffer().append("insert into ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("distribution_address (")
            .append("SUBMISSION_ID,")
            .append("OCCURRENCE_NO,")
            .append("DISTRIBUTION_TYPE_CODE,")
            .append("RECIPIENT_NO,")
            .append("PAYEE_NO,")
            .append("ADDR_LINE1,")
            .append("ADDR_LINE2,")
            .append("CITY_NAME,")
            .append("STATE_CODE,")
            .append("ZIP_CODE,")
            .append("COUNTRY_CODE,")
            .append("CREATED_USER_PROFILE_ID,")
            .append("CREATED_TS,")
            .append("LAST_UPDATED_USER_PROFILE_ID,")
            .append("LAST_UPDATED_TS")
            .append(" ) values ( ?,")
            .append("(select coalesce((max(OCCURRENCE_NO) + 1),1)")
            .append("from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("DISTRIBUTION_ADDRESS where submission_id = ?),")
            .append("?,?,?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT TIMESTAMP)")
            .toString();

    public static final int[] INSERT_TYPES = { Types.INTEGER, Types.INTEGER, Types.SMALLINT,
            Types.CHAR, Types.SMALLINT, Types.SMALLINT, Types.VARCHAR, Types.VARCHAR,
            Types.VARCHAR, Types.CHAR, Types.CHAR, Types.DECIMAL, Types.DECIMAL };

    public static final String SELECT_ALL_ADDRESSES_SQL = new StringBuffer()
	    .append("SELECT ")
        .append("SUBMISSION_ID,")
        .append("DISTRIBUTION_TYPE_CODE,")
        .append("RECIPIENT_NO,")
        .append("PAYEE_NO,")
        .append("ADDR_LINE1,")
        .append("ADDR_LINE2,")
        .append("CITY_NAME,")
        .append("STATE_CODE,")
        .append("ZIP_CODE,")
        .append("COUNTRY_CODE,")
        .append("CREATED_USER_PROFILE_ID,")
        .append("CREATED_TS,")
        .append("LAST_UPDATED_USER_PROFILE_ID,")
        .append("LAST_UPDATED_TS")
	    .append(" FROM   ")
	    .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
	    .append("DISTRIBUTION_ADDRESS ")
	    .append("WHERE SUBMISSION_ID = ? AND DISTRIBUTION_TYPE_CODE = ?").toString();
	
	public static final int[] SELECT_ALL_ADDRESSES_PARAMETER_TYPES = { Types.INTEGER, Types.CHAR };
	
	public static final String[] SELECT_ALL_ADDRESSES_RESULT_FIELDS = {
			"submissionId", "distributionTypeCode",
			"recipientNo", "payeeNo", "addressLine1", "addressLine2", "city",
			"stateCode", "zipCode", "countryCode", "createdById", "created",
			"lastUpdatedById", "lastUpdated" };

}
