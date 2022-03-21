package com.manulife.pension.ps.service.domain;

/**
 * Home interface for Enterprise Bean: DomainService
 */
public interface DomainServiceHome extends javax.ejb.EJBHome {
    /**
     * Creates a default instance of Session Bean: DomainService
     */
    public com.manulife.pension.ps.service.domain.DomainService create()
            throws javax.ejb.CreateException, java.rmi.RemoteException;
}
