package com.manulife.pension.ps.web.delegate;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;

import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.service.delegate.PsAbstractServiceDelegate;
import com.manulife.pension.ps.service.submission.SubmissionService;
import com.manulife.pension.ps.service.submission.SubmissionServiceHome;
import com.manulife.pension.ps.service.submission.valueobject.AddableParticipant;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.service.submission.valueobject.ContributionDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.CopiedSubmissionHistoryItem;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.ps.service.submission.valueobject.Lockable;
import com.manulife.pension.ps.service.submission.valueobject.LongTermPartTimeDetailItem;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionPaymentItem;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionValidationBundle;
import com.manulife.pension.ps.service.submission.valueobject.VestingDetailItem;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalLockableStub;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.util.Pair;

/**
 * SubmissionServiceDelegate - business delegate that wraps the SubmissionService ejb
 */
public class SubmissionServiceDelegate extends PsAbstractServiceDelegate {

	private static final String TRANSFER_CONTRIBUTION = "X";
	private static final String CONTRIBUTION_TYPE = "C";

	private static SubmissionServiceDelegate instance = new SubmissionServiceDelegate();

	protected SubmissionServiceDelegate() {
		super();
	}

	public static SubmissionServiceDelegate getInstance() {
		return instance;
	}

	/**
	 * @see SubmissionService#delete()
	 * 
	 * @param id
	 * @exception ServiceDelegateException
	 */
	public void deleteSubmission(int id, int contractNo, String typeCode)
			throws SystemException {

		try {
			SubmissionService service = (SubmissionService) getService();
			service.deleteSubmission(id,contractNo,typeCode);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "deleteSubmission");
		}
	}

    /**
     * @see SubmissionService#delete()
     * 
     * @param id
     * @param userProfileId The user profile ID of the person cancelling the submission.
     * @exception ServiceDelegateException
     */
    public void cancelSubmission(int id, int contractNo, String typeCode, long userProfileId)
            throws SystemException {

        try {
            SubmissionService service = (SubmissionService) getService();
            service.cancelSubmission(id, contractNo, typeCode, userProfileId);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "cancelSubmission");
        }
    }

	/**
	 * 	 * @see SubmissionService#getCensusSubmissionItem()
	 * 
	 * @param contractNumber
	 * @param submissionNumber
	 * @param participant identifier
	 * @return
	 * @throws SystemException
	 */
	public CensusSubmissionItem getCensusSubmissionItem(int cnno, int subId, String prtIdNum)
		throws SystemException {
		CensusSubmissionItem item = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			item = service.getCensusSubmissionItem(cnno,subId,prtIdNum);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "deleteSubmission");
		}
		return item;
	}

	/**
	 * 	 * @see SubmissionService#getSubmissionCase()
	 * 
	 * @param submissionNumber
	 * @param contractNumber
	 * @param typeCode
	 * @return
	 * @throws SystemException
	 */
	public SubmissionHistoryItem getSubmissionCase(int submissionNumber, int contractNumber, String typeCode)
		throws SystemException {
		SubmissionHistoryItem submissionCase = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			submissionCase = service.getSubmissionCase(submissionNumber,contractNumber,typeCode);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "getSubmissionCase");
		}
		return submissionCase;
	}
    
    
    public String getParticipantSortOption(int contractNumber) throws SystemException {
        String sortOption = null;
        try {
            SubmissionService service = (SubmissionService) getService();
            sortOption = service.getParticipantSortOption(contractNumber);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getParticipantSortOption");
        }
        return sortOption;
    }

    
	/**
	 * Returns the SubmissionPaymentItem for a payment only submission
	 * @param id the submission id
	 * @param contractNumber
	 * @return SubmissoinPaymentItem
	 * @throws RemoteException
	 */
	public SubmissionPaymentItem getPaymentOnlySubmission(int id, int contractNumber) 
   		throws SystemException {
		SubmissionPaymentItem paymentItem = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			paymentItem = service.getPaymentOnlySubmission(id, contractNumber);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "getPaymentOnlySubmission");
		}
		return paymentItem;
	}
	
	/**
	 * 	 @see SubmissionService#getContributionDetails()
	 * 
	 * @param submissionNumber (Tracking number)
	 * @param contractNumber
	 * @return ContributionDetailItem
	 * @throws SystemException
	 */
	public ContributionDetailItem getContributionDetails(int submissionNumber, int contractNumber)
			throws SystemException {
		ContributionDetailItem contributionDetail = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			contributionDetail = service.getContributionDetails(submissionNumber, contractNumber);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "getContributionDetailItem");
		}
		return contributionDetail;
	}
	
    
    /**
     *   @see SubmissionService#getVestingDetails()
     * 
     * @param submissionNumber (Tracking number)
     * @param contractNumber
     * @param criteria
     * @return VestingDetailItem
     * @throws SystemException
     */
    public VestingDetailItem getVestingDetails(int submissionNumber, int contractNumber, ReportCriteria criteria)
            throws SystemException {
        VestingDetailItem vestingDetail = null;
        try {
            SubmissionService service = (SubmissionService) getService();
            vestingDetail = service.getVestingDetails(submissionNumber, contractNumber, criteria);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getVestingDetails");
        }
        return vestingDetail;
    }
    
    /**
     *   @see SubmissionService#getVestingDetails()
     * 
     * @param submissionNumber (Tracking number)
     * @param contractNumber
     * @param criteria
     * @return VestingDetailItem
     * @throws SystemException
     */
    public LongTermPartTimeDetailItem getLongTermPartTimeDetails(int submissionNumber, int contractNumber, ReportCriteria criteria)
            throws SystemException {
    	LongTermPartTimeDetailItem longTermPartTimeDetail = null;
        try {
            SubmissionService service = (SubmissionService) getService();
            longTermPartTimeDetail = service.getLongTermPartTimeDetails(submissionNumber, contractNumber, criteria);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getLongTermPartTimeDetails");
        }
        return longTermPartTimeDetail;
    }
    
	/**
	 * 	 @see SubmissionService#getContributionDetails()
	 * 
	 * @param submissionNumber (Tracking number)
	 * @param contractNumber
	 * @return ContributionDetailItem
	 * @throws SystemException
	 */
	public ContributionDetailItem getContributionDetails(int submissionNumber, int contractNumber, 
			ReportCriteria reportCriteria)
	throws SystemException {
		ContributionDetailItem contributionDetail = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			contributionDetail = service.getContributionDetails(submissionNumber, contractNumber, reportCriteria);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "getContributionDetailItem");
		}
		return contributionDetail;
	}
	/**
	 * Copies the data for a given contribution returning
	 * object containing the tracking number of the new 
	 * contribution, and lists of any items not copied. The
	 * new contribution will be set to a cancelled status
	 * so it doesn't get picked up by STP. Any save of
	 * data for this contribution should set it to an 
	 * in-progress status.
	 * @param trackingNumber
	 * @param gftUploadDetail
	 * @returns CopiedSubmissionHistoryItem
	 * @throws RemoteException
	 */
	public CopiedSubmissionHistoryItem copyContributionDetails(int trackingNumber, GFTUploadDetail gftUploadDetail)
			throws SystemException {
		CopiedSubmissionHistoryItem copiedItem = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			copiedItem = service.copyContributionDetails(trackingNumber, 
					Integer.parseInt(gftUploadDetail.getContractNumber()),
					gftUploadDetail.getUserSSN(), gftUploadDetail.getUserName(), gftUploadDetail.getUserType(), 
					new BigDecimal(gftUploadDetail.getUserTypeID()), gftUploadDetail.getUserTypeName(),
					gftUploadDetail.getNotificationEmailAddress());
		} catch (RemoteException ex) {
			handleRemoteException(ex, "getContributionDetailItem");
		}
		return copiedItem;
	}
    
    public CopiedSubmissionHistoryItem copyVestingDetails(int trackingNumber, GFTUploadDetail gftUploadDetail)
            throws SystemException {
        CopiedSubmissionHistoryItem copiedItem = null;
        try {
            SubmissionService service = (SubmissionService) getService();
            copiedItem = service.copyContributionDetails(trackingNumber, 
                    Integer.parseInt(gftUploadDetail.getContractNumber()),
                    gftUploadDetail.getUserSSN(), gftUploadDetail.getUserName(), gftUploadDetail.getUserType(), 
                    new BigDecimal(gftUploadDetail.getUserTypeID()), gftUploadDetail.getUserTypeName(),
                    gftUploadDetail.getNotificationEmailAddress());
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getVestingDetailItem");
        }
        return copiedItem;
        }
    
	/**
	 * Copies the data for the last complete regular contribution
	 * returning an object containing the tracking number of the new 
	 * contribution, and lists of any items not copied. The
	 * new contribution will be set to a cancelled status
	 * so it doesn't get picked up by STP. Any save of
	 * data for this contribution should set it to an 
	 * in-progress status.
	 * @param contractNumber
	 * @param userId
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @returns CopiedSubmissionHistoryItem
	 * @throws RemoteException
	 */
	public CopiedSubmissionHistoryItem copyLastSubmittedContributionDetails(int contractNumber,
			String userId, String userName, String userTypeCode, BigDecimal userTypeId,
			String userTypeName, String notificationEmailAddress)
			throws SystemException {
		CopiedSubmissionHistoryItem copiedItem = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			copiedItem = service.copyLastSubmittedContributionDetails(contractNumber, userId,
					userName, userTypeCode, userTypeId, userTypeName, notificationEmailAddress);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "getContributionDetailItem");
		}
		return copiedItem;
	}
	/**
	 * Create the data for a skeleton contribution based on
	 * a contrct number and return the tracking number of the new 
	 * contribution. The new contribution will be set to a cancelled 
	 * status so it doesn't get picked up by STP. Any save of
	 * data for this contribution should set it to an 
	 * in-progress status.
	 * @param gftUploadDetail
	 * @returns CopiedSubmissionHistoryItem
	 * @throws RemoteException
	 */
	public CopiedSubmissionHistoryItem createContributionDetails(GFTUploadDetail gftUploadDetail)
			throws SystemException {
		CopiedSubmissionHistoryItem createdItem = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			createdItem = service.createContributionDetails(
					Integer.parseInt(gftUploadDetail.getContractNumber()),
					gftUploadDetail.getUserSSN(), gftUploadDetail.getUserName(), gftUploadDetail.getUserType(), 
					new BigDecimal(gftUploadDetail.getUserTypeID()), gftUploadDetail.getUserTypeName(),
					gftUploadDetail.getNotificationEmailAddress());
		} catch (RemoteException ex) {
			handleRemoteException(ex, "createContributionDetails");
		}
		return createdItem;
	}
	/**
	 * Adds a list of participants, currently active in the
	 * contract to a given submission.
	 * @param trackingNumber
	 * @param contractId
	 * @param userId
	 * @param participantsToAdd
	 * @throws RemoteException
	 */
	public void addPartcipantsToContribution(int trackingNumber, int contractId, String userId,
			ArrayList<AddableParticipant> participantsToAdd) throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			service.addPartcipantsToContribution(trackingNumber, contractId, userId,
					participantsToAdd);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "addPartcipantsToContribution");
		}
	}

	/**
	 * Saves changes to a contribution.
	 * @param userId
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @param newItem
	 * @return
	 */
	public void saveContributionDetails(String userId, String userName, String userType, String userTypeID, 
			String userTypeName, String processorUserId, ContributionDetailItem newItem, boolean isResubmit)
			throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			service.saveContributionDetails(userId, userName, userType, userTypeID, userTypeName, processorUserId, 
					newItem, isResubmit);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "saveContributionDetails");
		}
	}
    
    /**
     * Saves changes to a vesting.
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param newItem
     * @return
     */
    public Integer saveVestingDetails(String userId, String userName, String userType, String userTypeID, 
            String userTypeName, String processorUserId, VestingDetailItem newItem, boolean isResubmit)
            throws SystemException {
        try {
            SubmissionService service = (SubmissionService) getService();
            return service.saveVestingDetails(userId, userName, userType, userTypeID, userTypeName, processorUserId, 
            		newItem, isResubmit);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "saveVestingDetails");
        }
        return null;
    }

    /**
     * Saves LTPT changes.
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param newItem
     * @return
     */
    public Integer saveLongTermPartTimeDetails(String userId, String userName, String userType, String userTypeID, 
            String userTypeName, String processorUserId, LongTermPartTimeDetailItem newItem, boolean isResubmit)
            throws SystemException {
        try {
            SubmissionService service = (SubmissionService) getService();
            return service.saveLongTermPartTimeDetails(userId, userName, userType, userTypeID, userTypeName, processorUserId, 
            		newItem, isResubmit);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "saveLongTermPartTimeDetails");
        }
        return null;
    }
    
	/**
	 * cleans up redundant data (zero amounts)
	 * @param submissionId
	 * @param contractId
	 * @param userId
	 * @param userType
	 * @return
	 */
	public void cleanupContributionDetails(Integer trackingNumber, Integer contractNumber, String userId, 
			String userType ) throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			service.cleanupContributionDetails(trackingNumber, contractNumber, userId, userType);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "saveContributionDetails");
		}
	}
    
    
    /**
     * cleans up redundant data (zero amounts)
     * @param submissionId
     * @param contractId
     * @param userId
     * @param userType
     * @return
     */
    public void cleanupVestingDetails(Integer trackingNumber, Integer contractNumber, String userId, 
            String userType ) throws SystemException {
        try {
            SubmissionService service = (SubmissionService) getService();
            service.cleanupVestingDetails(trackingNumber, contractNumber, userId, userType);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "saveVestingDetails");
        }
    }
    
	
	public void updateAddress(CensusSubmissionItem address, AddressVO addressVo) throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			service.updateAddress(address, addressVo);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "updateAddress");
		}
	}
	
	public void removeCensusSubmissionItem(Integer cnno, Integer subId, 
            Integer seqNum, String userId) throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			service.removeCensusSubmissionItem(cnno,subId,seqNum,userId);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "removeCensusSubmissionItem");
		}
	}
	
	public SubmissionValidationBundle validateAndSaveCensusSubmissionItem(
			EmployeeData submittedData, String lastUpdatedUserId,
			Long lastUpdatedUserProfileId, boolean ignoreWarnings, boolean ignoreSimilarSsn,
			boolean ignoreEmployeeIdRules) throws SystemException, ApplicationException {
		try {
			SubmissionService service = (SubmissionService) getService();
			return service.validateAndSaveCensusSubmissionItem(submittedData,
					lastUpdatedUserId, lastUpdatedUserProfileId, ignoreWarnings, 
					ignoreSimilarSsn, ignoreEmployeeIdRules);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "validateAndSaveCensusSubmissionItem");
		}
		return null;
	}
	

	/**
	 * Returns the next census submission item in error using the given source
	 * record no.
	 * 
	 * @param contractId
	 * @param submissionId
	 * @param sourceRecordNo
	 * @return A pair object. The first item is the census submission item, the second item is
	 * the number of remaining errors. If no more errors can be found, null is returned.
	 */
	public Pair<CensusSubmissionItem, Integer> getNextCensusSubmissionItemInError(
			Integer contractId, Integer submissionId, Integer sourceRecordNo)
			throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			return service.getNextCensusSubmissionItemInError(contractId,
					submissionId, sourceRecordNo);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "getNextCensusSubmissionItemInError");
		}
		return null;
	}
	
	public void discardCensusRecordsWithSyntaxErrors(int submissionId, int cnno, String userId) throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			service.discardCensusRecordsWithSyntaxErrors(submissionId,cnno,userId);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "discardAddressRecrodsWithSyntaxErrors");
		}
	}

	public Lock acquireLock(Integer submissionId, Integer contractId, String type, String userId) throws SystemException {
		Lock lock = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			lock = service.acquireLock(submissionId,contractId,type,userId);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "acquireLock");
		}
		return lock;
	}

	public Lock acquireLock(Lockable submissionCase, String userId) throws SystemException {
		return acquireLock(submissionCase.getSubmissionId(),submissionCase.getContractId(), getType(submissionCase), userId);
	}

	public boolean releaseLock(Lock lock) throws SystemException {
		boolean success = false;
		try {
			SubmissionService service = (SubmissionService) getService();
			success = service.releaseLock(lock);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "releaseLock");
		}
		return success;
	}

	
	public boolean releaseLock(Lockable submissionCase) throws SystemException {
		boolean success = false;
		try {
			SubmissionService service = (SubmissionService) getService();
			success = service.releaseLock(submissionCase);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "releaseLock");
		}
		return success;
	}
	
	public Lock checkLock(Lockable submissionCase) throws SystemException {
		return checkLock(submissionCase,false);
	}
	
	public Lock checkLock(Lockable submissionCase, boolean retrieveUserName) throws SystemException {
		Lock lock = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			lock = service.checkLock(submissionCase, retrieveUserName);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "checkLock");
		}
		return lock;
	}

	public Lock refreshLock(Integer submissionNumber, Integer contractId, String type, String userId) throws SystemException {
		Lock refreshedLock = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			refreshedLock = service.refreshLock(submissionNumber, contractId, type, userId);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "refreshLock");
		}
		return refreshedLock;
	}

	public Lock refreshLock(Lockable submissionCase) throws SystemException {
		Lock refreshedLock = null;
		try {
			SubmissionService service = (SubmissionService) getService();
			refreshedLock = service.refreshLock(submissionCase);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "refreshLock");
		}
		return refreshedLock;
	}

	private String getType(Lockable submissionCase) {
		String type = submissionCase.getType();
		if (type.equals(TRANSFER_CONTRIBUTION)) {
			type = CONTRIBUTION_TYPE;
		}
		return type;
	}
	
	public boolean isParticipantCountTooBigForEdit(int trackingNumber, int contractId) throws SystemException {
		boolean isParticipantCountTooBig = false;
		try {
			SubmissionService service = (SubmissionService) getService();
			isParticipantCountTooBig = service.isParticipantCountTooBigForEdit(trackingNumber, contractId);
		} catch (RemoteException e) {
			handleRemoteException(e, "isParticipantCountTooBigForEdit");
		}
		return isParticipantCountTooBig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#getHomeClassName()
	 */
	protected String getHomeClassName() {
		return SubmissionServiceHome.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.manulife.pension.ps.web.delegate.AbstractServiceDelegate#create()
	 */
	protected EJBObject create() throws SystemException, RemoteException,
			CreateException {
		return ((SubmissionServiceHome) getHome()).create();
	}
    
    public Date getLastSubmissionDate(int contractId) throws SystemException {
        Date lastSubmission = null;
        try {
            SubmissionService service = (SubmissionService) getService();
            lastSubmission = service.getLastSubmissionDate(contractId);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getLastSubmissionDate");
        }
        return lastSubmission;
    }
    
    public String getApplicationCode(Integer submissionId) throws SystemException {
        
        String applicationId = null;
        
        try {
            SubmissionService service = (SubmissionService) getService();
            applicationId = service.getApplicationCode(submissionId);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getApplicationId");
        }
        
        return applicationId;
        
    }

    /**
     * Retrieves and validates a census submission item.
     * 
     * @param contractId
     * @param submissionId
     * @param employerDesignatedId
     * @param sequenceNumber
     * @return
     * @throws SystemException
     */
    public SubmissionValidationBundle<EmployeeData, EmployeeData, EmployeeValidationErrors> retrieveAndValidateCensusSubmission(
			Integer contractId, Integer submissionId,
			String employerDesignatedId, Integer sequenceNumber) throws SystemException {
		try {
			SubmissionService service = (SubmissionService) getService();
			return service.retrieveAndValidateCensusSubmission(contractId,
					submissionId, employerDesignatedId, sequenceNumber);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "retrieveAndValidateCensusSubmission");
		}
		return null;
	}
    
    /**
     * Retrieves a census submission item from SDB
     * 
     * @param contractId
     * @param submissionId
     * @param sequenceNumber
     * @return
     * @throws SystemException
     */
    public EmployeeData getSTPEmployeeData(
            Integer contractId, Integer submissionId, Integer sequenceNumber) 
            throws SystemException {
        try {
            
            SubmissionService service = (SubmissionService) getService();
            return service.getSTPEmployeeData(contractId, submissionId, sequenceNumber);
                    
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getSTPEmployeeData");
        }
        return null;
    }

    /**
     * Builds a lockable lightweight object used in conjunction with 
     * the Lock Manager to lock Withdrawal requests while beeing edited or deleted 
     * 
     * @param submissionId
     * @param contractId
     * @param profileid
     * @return
     */
    public LoanAndWithdrawalLockableStub getLoanAndWithdrawalLockable(Integer submissionId, Integer contractId, Integer profileId) {
    	return new LoanAndWithdrawalLockableStub(submissionId, contractId, profileId);
    }
    
    /**
     * This method is to update the duplicate submission employerProvidedEmailAddress as blank.
     * @param employeeData
     * @param formSequenceNumber
     * @param employerProvidedEmailAddress
     * @throws SystemException
     */
    public void deleteDuplicateEmailAddressesOnFile(Integer contractId, Integer submissionId, Integer formSequenceNumber,
			String employerProvidedEmailAddress, String userId) throws SystemException {
    	try {
			SubmissionService service = (SubmissionService) getService();
			service.deleteDuplicateEmailAddressesOnFile(contractId, submissionId, formSequenceNumber,
					employerProvidedEmailAddress, userId);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "deleteDuplicateEmailAddressesOnFile");
		}
    }
    
    /**
     * This method is to retrieve the original creator for the given submission Id
     * @param submissionId
     * @return
     * @throws SystemException
     */
    public String getSubmissionCreatorId(Integer submissionId) throws SystemException {
        
        String CreatorId = null;
        try {
            SubmissionService service = (SubmissionService) getService();
            CreatorId = service.getSubmissionCreatorId(submissionId);
        } catch (RemoteException ex) {
            handleRemoteException(ex, "getApplicationId");
        }
        
        return CreatorId;
    }
    
    /**Method to determines whether the DB table has process_status_code in draft or not.
     * @param submissionId
     * @param contractId
     */
    public boolean isSubmissionInDraft(int submissionId, int contractId) throws SystemException{
    	boolean submissionDraft = false;
		try {
			SubmissionService service = (SubmissionService) getService();
			submissionDraft = service.isSubmissionInDraft(submissionId,contractId);
		} catch (RemoteException ex) {
			handleRemoteException(ex, "isSubmissionInDraft");
		}
    	return submissionDraft;
    }
    
}