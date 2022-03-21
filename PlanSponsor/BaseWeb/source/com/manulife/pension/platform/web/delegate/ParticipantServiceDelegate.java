package com.manulife.pension.platform.web.delegate;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;				//CL 110234
import java.util.Collections;			//CL 110234
import java.util.Date;
import java.util.Iterator;				//CL 110234
import java.util.List;					//CL 110234
import java.util.Map;					//CL 110234
import java.util.TreeSet;				//CL 110234

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;					//CL 110234

import com.manulife.pension.delegate.EmployeeServiceDelegate;		//CL 110234
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.participant.ParticipantService;
import com.manulife.pension.ps.service.participant.ParticipantServiceHome;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.service.security.AbstractPrincipal;
import com.manulife.pension.service.security.Principal;


public class ParticipantServiceDelegate extends PsAbstractServiceDelegate {
	private static ParticipantServiceDelegate instance = new ParticipantServiceDelegate(); 


    private ParticipantServiceDelegate()
    {
    }
    
    public static ParticipantServiceDelegate getInstance()
    {
    	return instance;
    }
    
    /**
	 * @see AbstractServiceDelegate#getHomeClassName()
	 */
	protected String getHomeClassName() {
		return ParticipantServiceHome.class.getName();
	}
	
	/**
	 * @see AbstractServiceDelegate#getRemote(EJBHome)
	 */
	protected EJBObject create() throws SystemException, RemoteException, CreateException {
		return ((ParticipantServiceHome)getHome()).create();
	}

	
	public ParticipantAccountVO getParticipantAccount(AbstractPrincipal principal, int contractNumber,String productId,String profileID,
			Date asOfDate, boolean retrieveNetEEDeferralAmount, boolean organizeFundsByAssetClass) throws SystemException
	{ 
		ParticipantAccountVO result = null;
		ParticipantService service = (ParticipantService)getService();
		try
		{
			result = service.getParticipantAccount(
					principal, contractNumber, productId, profileID, asOfDate, retrieveNetEEDeferralAmount, organizeFundsByAssetClass);
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
	
    public void declineACIRequest(String contractNumber, String profileId, 
            long createdTS, int instructionNo, String remarks, 
            Long userId, Boolean isInternal, Long processedTimestamp,
            boolean isADHocRequest) throws SystemException {
    	
		ParticipantService service = (ParticipantService)getService();
		try {
			service.declineACIRequest(contractNumber, profileId, createdTS, instructionNo, remarks, userId, isInternal, processedTimestamp, isADHocRequest);
		} catch(RemoteException e) {
			handleRemoteException(e, "declineACIRequest");
		}
    }
    	
    
    public void approveACIRequest(String contractNumber, String profileId,
    		long createdTS, int instructionNo, Long userId,  
    		Boolean isInternal, Long processedTimestamp) throws SystemException {
		ParticipantService service = (ParticipantService)getService();
		try {
			service.approveACIRequest(contractNumber, profileId, createdTS, instructionNo, userId, isInternal, processedTimestamp);
		} catch(RemoteException e) {
			handleRemoteException(e, "approveACIRequest");
		}
    }
	
    //Gateway phase 1 starts 
    /**
     * Retrives the participantId from profileId
     * @param  profileId long
     * @param  contractNumber int 
     * @throws SystemException 
     * @throws RemoteException 
     */
    public int getParticipantIdByProfileId(long profileId, int contractNumber)throws SystemException{
    	
    	int participantId = 0 ;
		ParticipantService service = (ParticipantService)getService();
		try
		{
			participantId = service.getParticipantIdByProfileId(profileId, contractNumber);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "getParticipantIdByProfileId");
		}
		return participantId;
    	
    }
   //Gateway phase 1 end  
    //Gateway phase 1C Starts 
    /**
     * Retrives the giflDeselectionStatus from profileId
     * @param  profileId long
     * @param  contractNumber int 
     * @throws SystemException 
     * @throws RemoteException 
     */
    public String getParticipantGIFLStatus(String participantId, String contractNumber)throws SystemException{
    	
    	String giflDeselectionStatus="";
		ParticipantService service = (ParticipantService)getService();
		try
		{
			giflDeselectionStatus = service.getParticipantGIFLStatus(participantId, contractNumber);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "getParticipantGIFLStatus");
		}
		return giflDeselectionStatus;
    	
    }
    /**
     * Retrives the giflDeselectionStatus from participantId
     * @param  participantId String
     * @param  contractNumber String 
     * @throws SystemException 
     * @throws RemoteException 
     */
    public boolean getParticipantGIFLStatusAsBoolean(String participantId, String contractNumber)throws SystemException{
    	
    	boolean giflDeselectionStatus=false;
		ParticipantService service = (ParticipantService)getService();
		try
		{
			giflDeselectionStatus = service.getParticipantGIFLStatusAsBoolean(participantId, contractNumber);
		}
		catch(RemoteException e)
		{
			handleRemoteException(e, "getParticipantGIFLStatus");
		}
		return giflDeselectionStatus;
    	
    }

   //Gateway phase 1C end
    
	//CL 110234 Begin
    /**
     * Get the employee statuses from Employee Service.
     * 
     * @throws SystemException
     */
    public List<LabelValueBean> getEmployeeStatusesList(String applicationID) throws SystemException {
        Map empStatus = EmployeeServiceDelegate.getInstance(applicationID)
                .getEmployeeStatusList();
        List<LabelValueBean> employmentStatusList = new ArrayList<LabelValueBean>();
        
        TreeSet sortedSet = new TreeSet(empStatus.keySet());
        Iterator iter = sortedSet.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) empStatus.get(key);
            employmentStatusList.add(new LabelValueBean(value, key));
        }
        employmentStatusList = Collections.unmodifiableList(employmentStatusList);
        return employmentStatusList;
    }
    
    /**
     * Get the employee statuses from Employee Service without status 'C'.
     * 
     * @throws SystemException
     */
    public List<LabelValueBean> getEmployeeStatusesListWithoutC(String applicationID) throws SystemException {
        Map empStatus = EmployeeServiceDelegate.getInstance(applicationID)
                .getEmployeeStatusList();
        List<LabelValueBean> employmentStatusList = new ArrayList<LabelValueBean>();
        
        TreeSet sortedSet = new TreeSet(empStatus.keySet());
        Iterator iter = sortedSet.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value = (String) empStatus.get(key);
            if (!"C".equals(key)) {
            employmentStatusList.add(new LabelValueBean(value, key));
            }
        }
        employmentStatusList = Collections.unmodifiableList(employmentStatusList);
        return employmentStatusList;
    }
    //CL 110234 End
    
    /**
     * updates the loan details 
     * loanDetails String arry
     * @param  TpaId String
     * @param  contractNumber int 
     * @throws SystemException 
     * @throws RemoteException 
     */
	public void UpdateLoanDetails(String[] loanDetails,ArrayList<String> unSeletedLoans, int contractNumber, String TpaId,String tpaProfileName) throws SystemException {

		ParticipantService service = (ParticipantService) getService();
		try {
			service.updateLoanDetails(loanDetails,unSeletedLoans, contractNumber, TpaId,tpaProfileName);
		} catch (RemoteException e) {
			handleRemoteException(e, "UpdateLoanDetails");
		}

	}

	public List<String> getExistingParticipantIDS(int contractNumber) throws SystemException {
		List<String> existingParticipants = new ArrayList<String>();
		ParticipantService service = (ParticipantService) getService();
		try {
			existingParticipants = service.getExistingParticipantIDS(contractNumber);
		} catch (RemoteException e) {
			handleRemoteException(e, "getExistingParticipantIDS");
		}
		return existingParticipants;
	}

}

