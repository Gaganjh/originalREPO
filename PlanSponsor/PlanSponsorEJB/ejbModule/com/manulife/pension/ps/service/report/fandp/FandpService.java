package com.manulife.pension.ps.service.report.fandp;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedData;
import com.manulife.pension.ps.service.report.fandp.valueobject.UserSavedDataLists;
import com.manulife.pension.service.security.BDPrincipal;

public interface FandpService extends EJBObject  {
	
	public String insertUserData(long profileId, UserSavedData userSavedData, boolean overwriteEisting) throws RemoteException, SystemException;
	
	public UserSavedDataLists retrieveUserSavedDataLists(long profileId, String savedDataType) throws RemoteException, SystemException;
	
	public void deleteUserSavedDataLists(
            long profileId, String savedDataName, String savedDataType) throws RemoteException, SystemException;
}
