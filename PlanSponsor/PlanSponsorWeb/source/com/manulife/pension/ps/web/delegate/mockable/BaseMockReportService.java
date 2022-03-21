package com.manulife.pension.ps.web.delegate.mockable;

import java.rmi.RemoteException;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.Handle;
import javax.ejb.RemoveException;

import com.manulife.pension.service.report.ReportService;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * The intention behind this class is to provide a simple implementation of all
 * methods. We intend that the web tier's Mock implementation subclasses this
 * class, and therefore picks up "automagically" any new methods that we add to
 * the Report Service. (This prevents us from having to change their code any
 * time we add a method to the account service).
 * 
 * Created on May 4, 2004
 * 
 * @author drotele
 *  
 */

public abstract class BaseMockReportService implements ReportService {

	/**
	 * Constructor
	 */
	public BaseMockReportService() {
		super();
	}

	/**
	 * @see com.manulife.pension.ps.service.report.ReportService#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws RemoteException, ReportServiceException {
		return null;
	}

	/**
	 * @see javax.ejb.EJBObject#getEJBHome()
	 */
	public EJBHome getEJBHome() throws RemoteException {
		return null;
	}

	/**
	 * @see javax.ejb.EJBObject#getHandle()
	 */
	public Handle getHandle() throws RemoteException {
		return null;
	}

	/**
	 * @see javax.ejb.EJBObject#getPrimaryKey()
	 */
	public Object getPrimaryKey() throws RemoteException {
		return null;
	}

	/**
	 * @see javax.ejb.EJBObject#isIdentical(javax.ejb.EJBObject)
	 */
	public boolean isIdentical(EJBObject arg0) throws RemoteException {
		return false;
	}

	/**
	 * @see javax.ejb.EJBObject#remove()
	 */
	public void remove() throws RemoteException, RemoveException {
	}

}