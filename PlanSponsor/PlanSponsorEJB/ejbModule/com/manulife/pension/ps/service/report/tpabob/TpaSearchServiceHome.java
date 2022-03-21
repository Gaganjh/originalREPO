package com.manulife.pension.ps.service.report.tpabob;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

public interface TpaSearchServiceHome extends javax.ejb.EJBHome {
    /**
     * Creates a default instance of Session Bean: TpaService
     */
    public TpaSearchService create() throws CreateException, RemoteException;
}
