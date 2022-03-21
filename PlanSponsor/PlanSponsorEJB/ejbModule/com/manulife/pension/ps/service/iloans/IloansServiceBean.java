/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.iloans;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.iloans.dao.InitiateLoanRequestsDAO;
import com.manulife.pension.ps.service.iloans.exception.IloansServiceException;
import com.manulife.pension.service.account.valueobject.LoanRequestData;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IloansServiceBean implements SessionBean
{
	private final Collection exceptionsNeedsLogging = new ArrayList();	
	private SessionContext mySessionCtx;
	/**
	 * getSessionContext
	 */
	public SessionContext getSessionContext()
	{
		return mySessionCtx;
	}
	/**
	 * setSessionContext
	 */
	public void setSessionContext(SessionContext ctx)
	{
		mySessionCtx = ctx;
	}
	
	public void ejbActivate()
	{
	}
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws CreateException
	{

	}

	/**
	 * ejbPassivate
	 */
	public void ejbPassivate()
	{
	}
	/**
	 * ejbRemove
	 */
	public void ejbRemove()
	{
	}
	public int validateContractSSN(String contractNumber, String ssn,  String profileId ) throws RemoteException, SystemException, IloansServiceException
	{	
		return 1;
	}
	public String insertLoanRequest(LoanRequestData data, String ssn, BigDecimal userProfileId, String siteLocation) throws SystemException, IloansServiceException
	{
		StringBuffer loanRequestDataOut = new StringBuffer();
	try{

		BigDecimal profileId = null;	
		// first validate the parameters:
		// validate contract and the TPA -an appropriate  IloansServiceException will be thrown by DAO is something is wrong 
		InitiateLoanRequestsDAO.validateContractIlonRequest(Integer.parseInt(data.getContractNumber()), userProfileId, siteLocation);
		// validate contract and ssn - an appropriate IloansServiceException will be thrown by DAO is something is wrong 
		// if validated - profileId will get value
		profileId =InitiateLoanRequestsDAO.validateContractSSN(Integer.parseInt(data.getContractNumber()),ssn);

		data.setProfileId(profileId.toString());
		// check if participant does not have unexpired loan requests. if yes -an appropriate IloansServiceException will be thrown by DAO
		InitiateLoanRequestsDAO.validateForUnexpiredLoanRequests(Integer.parseInt(data.getContractNumber()), profileId);
		// find the latest MaxLoanRequestId
		int maxLoanRequestId = InitiateLoanRequestsDAO.getMaxLoanRequestId(Integer.parseInt(data.getContractNumber()), profileId);	
		// find the latest MaxConfirmationNumber
		int maxConfirmationNumber =InitiateLoanRequestsDAO.getMaxConfirmationNumber();
		//assign the next available LoanRequestId by increasing MaxLoanRequestId by 1);
		//assign the next available ConfirmationNumber by increasing maxConfirmationNumber by 1);
		String loanRequestId = new Integer(++maxLoanRequestId).toString();
		data.setLoanRequestId(loanRequestId);
		data.setConfirmationNumber(++maxConfirmationNumber);
		// try to insert new loan request record
		try {
		InitiateLoanRequestsDAO.insertLoanRequest(data);
		// problem with insertion: in case someone already has used the next id
		// or confirmation number - add 1 and try again

		} catch (DAOException e) {
			loanRequestId = new Integer(++maxLoanRequestId).toString();
			data.setLoanRequestId(loanRequestId);
			data.setConfirmationNumber(++maxConfirmationNumber);
			InitiateLoanRequestsDAO.insertLoanRequest(data);
		}
	}	
		catch (DAOException e) {
			throw new SystemException(e, this.getClass().getName(), "insertLoanRequest",
			"Problem inserting loan request");		
		}
		loanRequestDataOut.append(data.getProfileId())
		.append(",").append(data.getLoanRequestId());
		return loanRequestDataOut.toString();
	}

}
