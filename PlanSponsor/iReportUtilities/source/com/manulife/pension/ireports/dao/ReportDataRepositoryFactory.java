package com.manulife.pension.ireports.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class ReportDataRepositoryFactory {

	private static Class repositoryClass = ReportDataRepositoryImpl.class;

	public static ReportDataRepository getRepository() {
		if (!ReportDataRepository.class.isAssignableFrom(repositoryClass)) {
			throw new IllegalStateException("The factory is not configured with an instance of ReportDataRepository [" + repositoryClass.getName() + "]");
		}
		ReportDataRepository result = null;
		try {
			Method getInstanceMethod = repositoryClass.getDeclaredMethod("getInstance", new Class[]{});
			result = (ReportDataRepository)getInstanceMethod.invoke(null, new Object[]{});
		} catch (NoSuchMethodException e) {
			throw new NestableRuntimeException("Failed to create data repository", e);
		} catch (IllegalAccessException e) {
			throw new NestableRuntimeException("Failed to create data repository", e);
		} catch (InvocationTargetException e) {
			throw new NestableRuntimeException("Failed to create data repository", e);
		}
		return result;
	}
	
	public static void setImplementationClass(Class clazz) {
		repositoryClass = clazz;
	}
}
