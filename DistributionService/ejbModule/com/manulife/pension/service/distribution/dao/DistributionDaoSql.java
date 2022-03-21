package com.manulife.pension.service.distribution.dao;

import com.manulife.pension.service.dao.DaoConstants;

public interface DistributionDaoSql extends DaoConstants {
	/**
	 * Marks the specified submission as expired
	 */
	String SET_EXPIRED_REQUEST = "update " + SchemaName.STP
			+ "submission_case sc "
			+ " set sc.process_status_code = ?, sc.last_updated_user_id = ? "
			+ " , sc.last_updated_ts = ? "
			+ " , sc.PROCESSED_TS = ? "
			+ " where submission_id = ?";

}
