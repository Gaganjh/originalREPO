package com.manulife.pension.ps.service.lock;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

public interface LockServiceHome extends javax.ejb.EJBHome {

    public static final String JNDI_NAME = "com.manulife.pension.ps.service.lock.LockServiceHome";

    public LockService create() throws CreateException, RemoteException;
}
