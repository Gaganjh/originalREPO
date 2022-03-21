package com.manulife.pension.ps.web.delegate;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.participant.ParticipantService;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.ps.web.census.util.EmployeeServiceFacade;
import com.manulife.pension.service.delegate.AbstractServiceDelegate;
import com.manulife.pension.service.employee.EmployeeService;
import com.manulife.pension.service.employee.EmployeeServiceHome;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.security.Principal;

public class EmployeeServiceDelegate extends AbstractServiceDelegate {
	private static EmployeeServiceDelegate instance = new EmployeeServiceDelegate(); 
    private String applicationId;


    /* (non-Javadoc)
     * @see com.manulife.pension.service.delegate.AbstractServiceDelegate#getApplicationId()
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * @param applicationId
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    private EmployeeServiceDelegate()
    {
    }
    
    public static EmployeeServiceDelegate getInstance()
    {
    	return instance;
    }
    
    /**
	 * @see AbstractServiceDelegate#getHomeClassName()
	 */
	protected String getHomeClassName() {
		return EmployeeServiceHome.class.getName();
	}
	
	/**
	 * @see AbstractServiceDelegate#getRemote(EJBHome)
	 */
	protected EJBObject create() throws SystemException, RemoteException, CreateException {
		return ((EmployeeServiceHome)getHome()).create();
	}

	
	public ParticipantAccountVO getParticipantAccount(Principal principal, int contractNumber,String productId,String profileID,
			Date asOfDate, boolean retrieveNetEEDeferralAmount) throws SystemException
	{ 
		ParticipantAccountVO result = null;
		ParticipantService service = (ParticipantService)getService();
		try
		{
			result = service.getParticipantAccount(principal, contractNumber, productId, profileID, asOfDate, retrieveNetEEDeferralAmount, false);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "getParticipantAccount");
		}
		return result;
	}
	
	
	public String getProfileIdByParticipantId(String participantId, int contractNumber) throws SystemException
	{ 
		String profileId = null;
		ParticipantService service = (ParticipantService)getService();
		try
		{
			profileId = service.getProfileIdByParticipantId(participantId, contractNumber);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "getProfileIdByParticipantId");
		}
		return profileId;
	}
	
//	public void updateApolloAddress(OnlineAddress address) throws SystemException, ReportServiceException {
//		ParticipantService service = (ParticipantService)getService();
//		try {
//			service.updateApolloAddress(address);
//		} catch (RemoteException e) {
//			handleRemoteException(e, "updateAddress");
//		}
//	}
//
//	public void updateCSDBAddress(OnlineAddress address) throws SystemException, ReportServiceException {
//		ParticipantService service = (ParticipantService)getService();
//		try {
//			service.updateCSDBAddress(address);
//		} catch (RemoteException e) {
//			handleRemoteException(e, "updateAddress");
//		}
//	}
//
//	public void insertApolloAddress(OnlineAddress address) throws SystemException, ReportServiceException {
//		ParticipantService service = (ParticipantService)getService();
//		try {
//			service.insertApolloAddress(address);
//		} catch (RemoteException e) {
//			handleRemoteException(e, "updateAddress");
//		}
//	}

	public void updateDeferralProcessInd(int contractNumber, double profileId, Timestamp createTS, boolean processInd) throws SystemException
	{ 
		
		ParticipantService service = (ParticipantService)getService();
		try
		{
			service.updateDeferralProcessInd(contractNumber, profileId, createTS,processInd);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "updateDeferralProcessInd");
		}
	}
	
//	public OnlineAddress getOnlineAddress(int cnno, String prtIdNum) throws SystemException, ReportServiceException {
//		OnlineAddress address = null;
//		ParticipantService service = (ParticipantService)getService();
//		try {
//			address = service.getOnlineAddress(cnno, prtIdNum);
//		} catch (RemoteException e) {
//            handleRemoteException(e, "getOnlineAddress");
//		}
//		return address;
//	}
//
//	public OnlineAddress getOnlineAddress(int cnno, int prtId, String prtIdNum) throws SystemException, ReportServiceException {
//		OnlineAddress address = null;
//		ParticipantService service = (ParticipantService)getService();
//		try {
//			address = service.getOnlineAddress(cnno, prtId, prtIdNum);
//		} catch (RemoteException e) {
//            handleRemoteException(e, "getOnlineAddress");
//		}
//		return address;
//	}
//	
//	public OnlineAddress getOnlineAddress(int cnno, long profileId, String prtIdNum) throws SystemException, ReportServiceException {
//		OnlineAddress address = null;
//		ParticipantService service = (ParticipantService)getService();
//		try {
//			address = service.getOnlineAddress(cnno, profileId, prtIdNum);
//		} catch (RemoteException e) {
//            handleRemoteException(e, "getOnlineAddress");
//		}
//		return address;
//	}
//
//	public ParticipantAddress getParticipantAddress(int cnno, long profileId) throws SystemException, ReportServiceException {
//		ParticipantAddress address = null;
//		ParticipantService service = (ParticipantService)getService();
//		try {
//			address = service.getParticipantAddress(cnno, profileId);
//		} catch (RemoteException e) {
//			handleRemoteException(e, "getParticipantAddress");
//		}
//		return address;
//	}

}

