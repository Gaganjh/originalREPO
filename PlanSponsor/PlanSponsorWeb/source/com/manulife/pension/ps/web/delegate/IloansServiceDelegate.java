/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.delegate;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.iloans.IloansService;
import com.manulife.pension.ps.service.iloans.IloansServiceHome;
import com.manulife.pension.ps.service.iloans.exception.IloansServiceException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.account.valueobject.LoanRequestData;

/**
 * @author sternlu
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class IloansServiceDelegate extends PsAbstractServiceDelegate {

	public static final String SITE_LOCATION_USA = "usa";
	public static final String US ="us";
	public static final String NY ="ny";
	private static IloansServiceDelegate instance;

	private Logger logger = Logger.getLogger(IloansServiceDelegate.class);

	static {
		try {

			instance = new IloansServiceDelegate();
		} catch (SystemException e) {
		}
	}

	protected IloansServiceDelegate() throws SystemException {
		super();
	}

	public static IloansServiceDelegate getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#getHome()
	 */
	protected EJBObject create() throws SystemException, RemoteException,
			CreateException {
		try{
		return ((IloansServiceHome) getHome()).create();
		}catch(CreateException e)
		{
			e.printStackTrace();
			throw e;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#getHomeClassName()
	 */
	protected String getHomeClassName() {
		// TODO Auto-generated method stub

		return IloansServiceHome.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#getInitialContext()
	 */
	protected Context getInitialContext() throws NamingException {
		// TODO Auto-generated method stub
		return super.getInitialContext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#getService()
	 */
	protected EJBObject getService() throws SystemException {
		// TODO Auto-generated method stub

		return super.getService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#handleRemoteException(java.rmi.RemoteException,
	 *      java.lang.String)
	 */
	protected void handleRemoteException(RemoteException re, String methodName)
			throws SystemException {
		// TODO Auto-generated method stub
		super.handleRemoteException(re, methodName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#refreshHome()
	 */
	protected void refreshHome() throws SystemException {
		// TODO Auto-generated method stub
		super.refreshHome();
	}


	public void insertLoanRequest(LoanRequestData data, String ssn, BigDecimal userProfileId)
			throws IloansServiceException, SystemException {
		String profileId = null;

		String response = null;
		IloansService service = (IloansService) getService();

		try {
			String siteLocation =Environment
			.getInstance().getSiteLocation();

			siteLocation = (SITE_LOCATION_USA.equalsIgnoreCase(siteLocation))? US:NY;			
			response = service.insertLoanRequest(data, ssn, userProfileId, siteLocation);
			if(response!=null)
			{
				StringTokenizer tokenizer = new StringTokenizer(response, ",");
				String temp =(String) tokenizer.nextElement();

				data.setProfileId(temp);
				temp =(String) tokenizer.nextElement();

				data.setLoanRequestId(temp);
				
				
			}

		} catch (RemoteException e) {
			e.printStackTrace();
			handleRemoteException(e, "insertLoanRequest");
			e.printStackTrace();
		}
	}
}