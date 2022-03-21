package com.manulife.pension.ps.web.delegate.mockable;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.Handle;
import javax.ejb.SessionContext;

import com.manulife.pension.delegate.AccountServiceDelegate;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

/**
 * Mock class for the ReportService
 *
 * @author drotele
 * Created on May 3, 2004
 *
 */
public class MockReportService
	extends BaseMockReportService
	implements javax.ejb.SessionBean {

	/**
	 * Constructor
	 */
	public MockReportService() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.mockable.BaseMockReportService#getHandle()
	 */
	public Handle getHandle() throws RemoteException {
		return super.getHandle();
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.mockable.BaseMockReportService#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)
		throws RemoteException, ReportServiceException {

			ReportHandler handler = null;
			ReportData data = null;

			try {
				handler =  MockReportHandlerFactory.getInstance().getReportHandler(reportCriteria.getReportId());
				data = handler.getReportData(reportCriteria);
			}
			catch (SystemException e)
			{
				throw ExceptionHandlerUtility.wrap(e);
			}
			catch (RuntimeException e)
			{
				SystemException se = new SystemException(e, this.getClass().getName(), "getProfileIdByParticipantId",
						"Unchecked exception occurred. Input Paramereter is "+
						"ReportCriteria:"+reportCriteria);
				throw ExceptionHandlerUtility.wrap(se);
			}

			return data;
	}

	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}
	/**
	 * Method to determine the 1st business day of the previous month for the Pending Withdrawal Summary page
	 * @param dateObjs
	 * @return
	 * @throws SystemException
	 */
	public Date[] determineBusinessDay(Date[] dateObjs) throws SystemException,RemoteException{
		try {
			return AccountServiceDelegate.getInstance().getFilteredNYSEClosureDatesIgnoringEmergencyClosure(null,dateObjs );
		} catch (Exception e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "determineBusinessDay",
					"Unchecked exception occured. Input Paramereter is "+
					"dateObjs :"+dateObjs);
			throw ExceptionHandlerUtility.wrap(se);
		} 
	}

}
