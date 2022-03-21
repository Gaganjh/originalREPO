package com.manulife.pension.service.distribution.dao;

import java.sql.Types;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public interface NoteDaoSql {

    public static final String SQL_INSERT_NOTES = new StringBuffer()
            .append("INSERT INTO ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("NOTE (")
            .append("SUBMISSION_ID,")
            .append("CREATED_TS,")
            .append("NOTE_TYPE_CODE,")
            .append("NOTE,")
            .append("CREATED_USER_PROFILE_ID)")
            .append(" VALUES (?,CURRENT TIMESTAMP,?,?,?)").toString();


    public static final String SELECT_ALL_NOTES_SQL = new StringBuffer()
            .append("SELECT N.CREATED_TS, ")
            .append("N.NOTE_TYPE_CODE, ")
            .append("N.NOTE, ")
            .append("N.CREATED_USER_PROFILE_ID ")
            .append("FROM ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("NOTE N ")
            .append(" WHERE  N.SUBMISSION_ID = ? ORDER BY CREATED_TS DESC")
            .toString();

    public static final int[] SELECT_ALL_NOTES_PARAMETER_TYPES = { Types.INTEGER };

    public static final String[] SELECT_ALL_NOTES_RESULT_FIELDS = { "created",
            "noteTypeCode", "note", "createdById" };

    public static final String SQL_DELETE_ALL_NOTES = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("NOTE where submission_id = ?").toString();

    public static final String SQL_DELETE_NOTE_TYPE = new StringBuffer()
            .append("delete from ")
            .append(BaseDatabaseDAO.STP_SCHEMA_NAME)
            .append("NOTE where submission_id = ? and note_type_code = ?").toString();
}
