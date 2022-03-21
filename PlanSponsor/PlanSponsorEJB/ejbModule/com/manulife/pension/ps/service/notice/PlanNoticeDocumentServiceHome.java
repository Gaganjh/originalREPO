package com.manulife.pension.ps.service.notice;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * Home interface for Enterprise Bean: PlanNoticeDocumentService
 */
public interface PlanNoticeDocumentServiceHome extends EJBHome {
	/**
	 * Creates a default instance of Session Bean: PlanNoticeDocumentService
	 */
	public PlanNoticeDocumentService create() throws CreateException, RemoteException;
}
