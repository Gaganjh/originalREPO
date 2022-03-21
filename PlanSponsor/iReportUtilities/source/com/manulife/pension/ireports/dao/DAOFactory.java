package com.manulife.pension.ireports.dao;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public class DAOFactory {

	private static DAOCreator creator = new DAOCreatorImpl();

	public static BaseDatabaseDAO create(String daoClassName) {
		return creator.create(daoClassName);
	}
	
	public static void setCreator(DAOCreator newCreator) {
		creator = newCreator;
	}
}
