package com.manulife.pension.ps.service.submission;

import java.rmi.RemoteException;
/**
 * Home interface for Enterprise Bean: ReportService
 */
public interface SubmissionServiceHome extends javax.ejb.EJBHome {
	
	public static final String JNDI_NAME = "com.manulife.pension.ps.service.submission.SubmissionServiceHome";
	/**
	 * Creates a default instance of Session Bean: ReportService
	 */
	public SubmissionService create() throws javax.ejb.CreateException, RemoteException;
}
