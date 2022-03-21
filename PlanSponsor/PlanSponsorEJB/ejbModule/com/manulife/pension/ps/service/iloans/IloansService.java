/*
 * Created on May 31, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.service.iloans;

import java.math.BigDecimal;
import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.iloans.exception.IloansServiceException;
import com.manulife.pension.service.account.valueobject.LoanRequestData;

/**
 * @author sternlu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IloansService extends EJBObject  {
	
	public String insertLoanRequest(LoanRequestData data, String ssn, BigDecimal userProfileId, String siteLocation) throws RemoteException, SystemException, IloansServiceException;	
}
