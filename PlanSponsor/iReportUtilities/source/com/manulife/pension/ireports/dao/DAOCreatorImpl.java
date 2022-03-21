package com.manulife.pension.ireports.dao;

import org.apache.commons.lang.exception.NestableRuntimeException;

import com.manulife.pension.service.dao.BaseDatabaseDAO;

public class DAOCreatorImpl implements DAOCreator {

	public BaseDatabaseDAO create(String daoClassName) {
		try {
			return (BaseDatabaseDAO) getClass().getClassLoader().loadClass(daoClassName)
                    .newInstance();
		} catch (InstantiationException e) {
			throw new NestableRuntimeException("Unable to create DAO", e);
		} catch (IllegalAccessException e) {
			throw new NestableRuntimeException("Unable to create DAO", e);
		} catch (ClassNotFoundException e) {
			throw new NestableRuntimeException("Unable to create DAO", e);
		}
	}

}
