package com.manulife.pension.ps.web.delegate.mockable;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import com.manulife.pension.delegate.mock.account.BaseMockAccountService;
import com.manulife.pension.service.account.AccountException;
import com.manulife.pension.service.account.AccountService;
import com.manulife.pension.service.account.valueobject.CustomerServicePrincipal;
import com.manulife.pension.service.account.valueobject.FundUnitValueHistory;
import com.manulife.pension.service.account.valueobject.FundUnitValueHistorySummary;
import com.manulife.pension.service.account.valueobject.HistoricalUnitValue;
import com.manulife.pension.service.account.valueobject.LoanHoldings;
import com.manulife.pension.service.account.valueobject.ParticipantDataValueObject;

/**
 * @author drotele
 *
 * Mocked Account Service 
 * To be used by Service Delegates Unit Tests
 * 
 */
public class MockAccountService
	extends BaseMockAccountService
	implements javax.ejb.SessionBean {

	/**
	 * @see AccountService#executeLoanListTransaction(CustomerServicePrincipal, String, String)
	 */
	public LoanHoldings executeLoanListTransaction(
		CustomerServicePrincipal principal,
		String profileId,
		String contractNumber)
		throws RemoteException, AccountException {

		LoanHoldings lh = new LoanHoldings();

		lh.setReturnCode(LoanHoldings.RETURN_CODE_OK);
		//@ TODO some more data here

		return lh;
	}
	
	/**
	 * mock method to support performance charting
	 * There is enough data here to support the action test.  No time for anything else.
	 */
	public FundUnitValueHistorySummary[] executeFundListForUnitValueHistoryTransaction(
		CustomerServicePrincipal principal, String contractNumber, String profileId,
		boolean excludeUnselectedFunds)
		throws RemoteException, AccountException {
			
		List fund = new ArrayList();
		
		FundUnitValueHistorySummary funditem = new FundUnitValueHistorySummary();
		funditem.setId("GEQ");
		
		Calendar cal = Calendar.getInstance();
		cal.set(2002,8,22,0,0,0);
		
		funditem.setEarliestUnitValueHistoryDate(cal.getTime());
		funditem.setFootnoteSymbols(new String[] {"#"});
		
		cal.set(2003,8,22,0,0,0);
		funditem.setLatestUnitValueHistoryDate(cal.getTime());
		
		fund.add(funditem);
		
		return (FundUnitValueHistorySummary[])fund.toArray(new FundUnitValueHistorySummary[0]);
	}

	/**
	 * mock method to support performance charting
	 * There is enough data here to support the action test.  No time for anything else.
	 */
	public FundUnitValueHistory[] executeViewFundsUnitValueHistoriesTransaction(
			CustomerServicePrincipal principal, String contractNumber, 
			String profileId, String[] fundIds, Date startDate, Date endDate)
			throws RemoteException, AccountException {

		List fund = new ArrayList();
		
		FundUnitValueHistory funditem = new FundUnitValueHistory();
		funditem.setFundId(fundIds[0]);
		
		HistoricalUnitValue[] unitValue = new HistoricalUnitValue[2];
		unitValue[0] = new HistoricalUnitValue();
		unitValue[0].setDate(startDate);
		unitValue[0].setUnitValue(new BigDecimal("1.24"));
		unitValue[1] = new HistoricalUnitValue();
		unitValue[1].setDate(endDate);
		unitValue[1].setUnitValue(new BigDecimal("1.44"));

		funditem.setHistoricalValues(unitValue);
		fund.add(funditem);
		
		return (FundUnitValueHistory[])fund.toArray(new FundUnitValueHistory[0]);
	}


	/* (non-Javadoc)
	 * @see com.manulife.pension.service.account.AccountService#getParticipantDataValueObject(com.manulife.pension.service.account.valueobject.CustomerServicePrincipal, java.lang.String)
	 */
	public ParticipantDataValueObject getParticipantDataValueObject(
		CustomerServicePrincipal cs,
		String participantId){
			String profileId = "100004175";
			String contractId = "80394";
			
			ParticipantDataValueObject pVO = new ParticipantDataValueObject();
			
			pVO.setContractNumber(contractId);
			pVO.setParticipantId(participantId);
			pVO.setProfileId(profileId);

			return pVO;
		}


	/* (non-Javadoc)
	 * @see com.manulife.pension.service.account.AccountService#getParticipantDataValueObject(com.manulife.pension.service.account.valueobject.CustomerServicePrincipal, java.lang.String, java.lang.String)
	 */
	public ParticipantDataValueObject getParticipantDataValueObject(
		CustomerServicePrincipal cs,
		String profileId,
		String contractNumber){

			String participantId = "1341593";
			ParticipantDataValueObject pVO = new ParticipantDataValueObject();
			
			pVO.setContractNumber(contractNumber);
			pVO.setParticipantId(participantId);
			pVO.setProfileId(profileId);

			return pVO;
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
	public void setSessionContext(SessionContext arg0)
		throws EJBException, RemoteException {
	}

	/**
	 * getSessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return null;
	}
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}
}
