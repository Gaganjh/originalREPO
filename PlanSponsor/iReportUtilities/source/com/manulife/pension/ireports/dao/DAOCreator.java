package com.manulife.pension.ireports.dao;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public interface DAOCreator {

	public BaseDatabaseDAO create(String daoClassName);

}