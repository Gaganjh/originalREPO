package com.manulife.pension.platform.web.delegate;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.service.report.ReportService;
import com.manulife.pension.service.report.ReportServiceHome;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * ReportServiceDelegate - business delegate that wraps the ReportService ejb
 * to return a specialized report data value object
 */
public class ReportServiceDelegate extends PsAbstractServiceDelegate {
	
	private static ReportServiceDelegate instance = new ReportServiceDelegate();

	protected ReportServiceDelegate() {
		super();
	}

	public static ReportServiceDelegate getInstance() {
		return instance;
	}

	/**
	 * @see ReportService#getReportData()
	 * 
	 * @param reportCriteria
	 * @return ReportData
	 * @exception ServiceDelegateException
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException {

		ReportData result = null;
		try {
			ReportService service = (ReportService) getService();
			result = service.getReportData(reportCriteria);

		} catch (RemoteException ex) {
			handleRemoteException(ex, "getReportData");
		}
		return result;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#getHomeClassName()
	 */
	protected String getHomeClassName() {
		return ReportServiceHome.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#create()
	 */
	protected EJBObject create() throws SystemException, RemoteException,
			CreateException {
		return ((ReportServiceHome) getHome()).create();
	}

}
