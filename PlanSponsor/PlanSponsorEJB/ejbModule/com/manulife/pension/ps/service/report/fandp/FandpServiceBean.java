package com.manulife.pension.ps.service.report.fandp;

import java.rmi.RemoteException;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.fandp.dao.FundsAndPerformanceDAO;
import com.manulife.pension.ps.service.report.fandp.valueobject.CustomQueryRow;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedDataLists;
import com.manulife.pension.service.security.BDPrincipal;

public class FandpServiceBean implements SessionBean {
	
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 0L;	
	
	private SessionContext mySessionCtx;

	/**
	 * getSessionContext
	 */
	public SessionContext getSessionContext() {
		return mySessionCtx;
	}
	
	/**
	 * setSessionContext
	 */
	public void setSessionContext(SessionContext ctx) {
		mySessionCtx = ctx;
	}
	
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}
	
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws CreateException{
	}

	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}
	
	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}
	
	public String insertUserData(long profileId, UserSavedData userSavedData, boolean overwriteEisting) 
	throws SystemException {
		
		return FundsAndPerformanceDAO.insertUserDataLists(profileId, userSavedData, overwriteEisting);
	}
	
	public UserSavedDataLists retrieveUserSavedDataLists(long profileId, String savedDataType) 
	throws SystemException {
		
		return FundsAndPerformanceDAO.retrieveUserSavedDataLists(profileId, savedDataType);
		
	}
	
	public void deleteUserSavedDataLists(
            long profileId, String savedDataName, String savedDataType) 
	throws SystemException {
		
		FundsAndPerformanceDAO.deleteUserSavedDataLists(profileId, savedDataName, savedDataType);
		
	}
}
