package com.manulife.pension.ps.service.participant;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBObject;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.participant.valueobject.ParticipantAccountVO;
import com.manulife.pension.service.security.AbstractPrincipal;

/**
 * Remote interface for Enterprise Bean: ParticipantService
 */
public interface ParticipantService extends EJBObject {
	

	public ParticipantAccountVO getParticipantAccount(AbstractPrincipal principal, int contractNumber, String productId, String profileID, 
			Date asOfDate, boolean retrieveNetEEDeferralAmount, boolean organizeFundsByAssetClass) throws RemoteException;

	public String getProfileIdByParticipantId(String participantId, int contractNumber) throws RemoteException;
	
	public void updateDeferralProcessInd(int contractNumber, double profileId, Timestamp createTS, boolean processInd) throws RemoteException;

    public void declineACIRequest(String contractNumber, String profileId, 
            long createdTS, int instructionNo, String remarks, 
            Long userId, Boolean isInternal, Long processedTimestamp,
            boolean isADHocRequest) throws RemoteException, SystemException;

    public void approveACIRequest(String contractNumber, String profileId,
    		long createdTS, int instructionNo, Long userId,  
    		Boolean isInternal, Long processedTimestamp) throws RemoteException, SystemException;
    
    //Gateway phase 1 starts 
    public int  getParticipantIdByProfileId (long profileId, int contractNumber )throws RemoteException;
    //Gateway phase 1 ends 

    //  Gateway phase 1C starts 
    public String getParticipantGIFLStatus(String participantId, String contractNumber) throws RemoteException;
    public boolean getParticipantGIFLStatusAsBoolean(String participantId, String contractNumber) throws RemoteException;
    //Gateway phase 1C ends  
    public void updateLoanDetails(String[] loanDetails,ArrayList<String> unSeletedLoans, int contractNumber, String tpaId,String tpaProfileName) throws RemoteException;
	public List<String> getExistingParticipantIDS(int contractNumber) throws RemoteException;

}
