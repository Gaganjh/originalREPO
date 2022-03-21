package com.manulife.pension.ps.service.report.tpabob;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;

public interface TpaSearchService extends EJBObject {
    public ArrayList<String> getCarForUserProfile(Long userProfileId) throws RemoteException,
            SystemException;
}
