package com.manulife.pension.ps.service.contract;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

public interface ContractAssemblyServiceHome extends javax.ejb.EJBHome {

    public static final String JNDI_NAME = "com.manulife.pension.ps.service.contract.ContractAssemblyServiceHome";

    public ContractAssemblyService create() throws CreateException, RemoteException;
}
