package com.manulife.pension.ps.service.submission;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.ArrayList;

import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
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
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.util.Pair;

/**
 * Remote interface for Enterprise Bean: SubmissionService
 */
public interface SubmissionService extends javax.ejb.EJBObject {
 	
	/**
	 * Deletes a submission (or specified case)
	 * @param id
	 * @param contractNo
	 * @param typeCode
	 * @throws RemoteException
	 */
	public void deleteSubmission(int id, int contractNo, String typeCode) 
		throws RemoteException;

    /**
     * Cancel a submission (or specified case)
     * 
     * @param id
     * @param contractNo
     * @param typeCode
     * @param userProfileId The user profile ID of the user cancelling the submission.
     * @throws RemoteException
     */
    public void cancelSubmission(int id, int contractNo, String typeCode, long userProfileId) 
        throws RemoteException; 
	
	/**
	 * Returns a single SubmissionHistoryItem 
	 * @param submissionNumber
	 * @param contractNumber
	 * @param typeCode
	 * @throws RemoteException
	 */
	public SubmissionHistoryItem getSubmissionCase(int submissionNumber, int contractNumber, String typeCode)
		throws RemoteException;

    /**
     * Returns the participant sort option for the given contract.
     */
    public String getParticipantSortOption(int contractNumber) throws RemoteException;
    
	/**
	 * Returns a single submitted census item
	 * @param cnno
	 * @param subId
	 * @param prtIdNum
	 * @return CensusSubmissionItem
	 * @throws RemoteException
	 */
	public CensusSubmissionItem getCensusSubmissionItem(int cnno, int subId, String prtIdNum)
	throws RemoteException;
    
    /**
     * Returns a single submitted census item from SDB
     * @param contractId
     * @param submissionId
     * @param sequenceNumber
     * @return EmployeeData
     * @throws RemoteException
     */
    public EmployeeData getSTPEmployeeData(Integer contractId, Integer submissionId, Integer sequenceNumber)
    throws RemoteException;

	/**
	 * Returns the SubmissionPaymentItem for a payment only submission
	 * @param id the submission id
	 * @return SubmissoinPaymentItem
	 * @throws RemoteException
	 */
	public SubmissionPaymentItem getPaymentOnlySubmission(int id, int contractNumber) 
   		throws RemoteException;
	
	/**
	 * Returns a single ContributionDetailItem 
	 * @param submissionNumber (tracking number)
	 * @param contractNumber
	 * @throws RemoteException
	 */
	public ContributionDetailItem getContributionDetails(int submissionNumber, int contractNumber)
	throws RemoteException;
    
    /**
     * Returns a single VestingDetailItem 
     * @param submissionNumber (tracking number)
     * @param contractNumber
     * @param criteria
     * @throws RemoteException
     */
    public VestingDetailItem getVestingDetails(int submissionNumber, int contractNumber, ReportCriteria criteria)
    throws RemoteException;
    
    /**
     * Returns a single LongTermPartTimeDetailItem 
     * @param submissionNumber (tracking number)
     * @param contractNumber
     * @param criteria
     * @throws RemoteException
     */
    public LongTermPartTimeDetailItem getLongTermPartTimeDetails(int submissionNumber, int contractNumber, ReportCriteria criteria)
    throws RemoteException;
	
	/**
	 * Returns a single ContributionDetailItem for a given page of data
	 * @param submissionNumber (tracking number)
	 * @param contractNumber
	 * @param reportCriteria
	 * @throws RemoteException
	 */
	public ContributionDetailItem getContributionDetails(int submissionNumber, int contractNumber, ReportCriteria reportCriteria)
	throws RemoteException;
	
	/**
	 * Copies the data for a given contribution returning
	 * object containing the tracking number of the new 
	 * contribution, and lists of any items not copied. The
	 * new contribution will be set to a cancelled status
	 * so it doesn't get picked up by STP. Any save of
	 * data for this contribution should set it to an 
	 * in-progress status.
	 * @param submissionId
	 * @param contractId
	 * @param userId
	 * @param userSSN
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @returns CopiedSubmissionHistoryItem 
	 * @throws RemoteException
	 */
	public CopiedSubmissionHistoryItem copyContributionDetails(int submissionId, int contractId, String userId,  
			String userName, String userTypeCode, BigDecimal userTypeId, String userTypeName,
			String notificationEmailAddress) throws RemoteException;

	/**
	 * Copies the data for the last complete regular contribution,
	 * returning an object containing the tracking number of the new 
	 * contribution, and lists of any items not copied. The
	 * new contribution will be set to a cancelled status
	 * so it doesn't get picked up by STP. Any save of
	 * data for this contribution should set it to an 
	 * in-progress status.
	 * @param contractId
	 * @param userId
	 * @param userSSN
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @returns CopiedSubmissionHistoryItem 
	 * @throws RemoteException
	 */
	public CopiedSubmissionHistoryItem copyLastSubmittedContributionDetails(int contractId, String userId,  
			String userName, String userTypeCode, BigDecimal userTypeId, String userTypeName,
			String notificationEmailAddress) throws RemoteException;

	/**
	 * Adds a list of participants, currently active in the
	 * contract to a given submission.
	 * @param submissionId
	 * @param contractId
	 * @param userId
	 * @param addableParticipants
	 * @throws RemoteException
	 */
	public void addPartcipantsToContribution(int submissionId, int contractId, String useriId,
			ArrayList<AddableParticipant> addableParticipants) throws RemoteException;
	/**
	 * given a contractId, create a new submission case 
	 * and return an object containing the submissionId 
	 * of the new submission 
	 * 
	 * @param submissionId
	 * @param ContractId
	 * @param userId
	 * @param userSSN
	 * @param userName
	 * @param userType
	 * @param userTypeId
	 * @param userTypeName
	 * @return CopiedSubmissionHistoryItem
	 * @throws SystemException
	 */

	public CopiedSubmissionHistoryItem createContributionDetails(int contractId, String userId,
			String userName, String userTypeCode, BigDecimal userTypeId, String userTypeName, 
			String notificationEmailAddress) throws RemoteException;

	/**
	 * Saves changes to a contribution.
	 * @param userId
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @param processorUserId
	 * @param newItem
	 * @param isRebubmit
	 * @return
	 */
	public void saveContributionDetails(String userId, String userName, String userType, String userTypeID, 
			String userTypeName, String processorUserId, ContributionDetailItem newItem, boolean isResubmit) 
			throws RemoteException;
    
    
    /**
     * Saves changes to a vesting and returns the final participant count.
     * 
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param processorUserId
     * @param newItem
     * @param isRebubmit
     * @return
     */
    public Integer saveVestingDetails(String userId, String userName, String userType, String userTypeID, 
            String userTypeName, String processorUserId, VestingDetailItem newItem, boolean isResubmit) 
            throws RemoteException;

    /**
     * Saves LTPT changes and returns the final participant count.
     * 
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param processorUserId
     * @param newItem
     * @param isRebubmit
     * @return
     */
    public Integer saveLongTermPartTimeDetails(String userId, String userName, String userType, String userTypeID, 
            String userTypeName, String processorUserId, LongTermPartTimeDetailItem newItem, boolean isResubmit) 
            throws RemoteException;
    
	/**
	 * cleans up redundant contribution data after submit.
	 * @param submissionId
	 * @param contractNumber
	 * @param userId
	 * @param userType
	 * @return
	 */
	public void cleanupContributionDetails(Integer submissionId, Integer contractNumber, String userId,
			String userType) throws RemoteException;
    
    
    /**
     * cleans up redundant vesting data after submit.
     * @param submissionId
     * @param contractNumber
     * @param userId
     * @param userType
     * @return
     */
    public void cleanupVestingDetails(Integer submissionId, Integer contractNumber, String userId,
            String userType) throws RemoteException;
	
	/**
	 * @param address CensusSubmissionItem
	 * @param addressVo AddressVO
	 * @throws RemoteException
	 */
	public void updateAddress(CensusSubmissionItem address, AddressVO addressVo) throws RemoteException;  
	
	
	/**
	 * @param cnno
	 * @param subId
	 * @param seqNum
	 * @param userId
	 * @throws RemoteException
	 */
	public void removeCensusSubmissionItem(Integer cnno, Integer subId, 
            Integer seqNum, String userId) throws RemoteException;
	
	public Pair getNextCensusSubmissionItemInError(Integer contractId,
			Integer submissionId, Integer sourceRecordNo)
			throws RemoteException;
	
	/**
	 * @param submissionId int
	 * @param cnno int
	 * @param userId String
	 * @throws RemoteException
	 */
	public void discardCensusRecordsWithSyntaxErrors(int submissionId, int cnno, String userId)
    throws RemoteException;
	
	public Lock acquireLock(Lockable submissionCase, String userId) throws RemoteException;
	public Lock acquireLock(Integer submissionNumber, Integer contractId, String type, String userId) throws RemoteException;
	
	public boolean releaseLock(Lockable submissionCase) throws RemoteException;
	public boolean releaseLock(Lock lock) throws RemoteException;
	
	public Lock checkLock(Lockable submissionCase) throws RemoteException;
	public Lock checkLock(Lockable submissionCase, boolean retrieveUserName) throws RemoteException;
	
	public Lock refreshLock(Lockable submissionCase) throws RemoteException;
	public Lock refreshLock(Integer submissionNumber, Integer contractId, String type, String userId) throws RemoteException;

	public boolean isParticipantCountTooBigForEdit(int submissionId, int contractId) throws RemoteException;
    
    /**
     * Returns the last submission date for the given contract.
     * @param contractId int
     * @return Date
     * @throws RemoteException
     */
    public Date getLastSubmissionDate(int contractId) throws RemoteException;
    
    /**
     * Returns the application ID for the given submission.
     * @param submissionId
     * @return
     * @throws RemoteException
     */
    public String getApplicationCode(Integer submissionId) throws RemoteException;
    
    /**
     * Retrieves and validate a census submission. This method is primarily used by the
     * error correction page.
     * 
     * @param contractId
     * @param submissionId
     * @param employerDesignatedId
     * @param sequenceNumber
     * @return
     * @throws RemoteException
     * @throws SystemException
     */
    public SubmissionValidationBundle retrieveAndValidateCensusSubmission(
			Integer contractId, Integer submissionId,
			String employerDesignatedId, Integer sequenceNumber) throws RemoteException;

	/**
	 * Validates and saves a census submission. Regardless of whether there is
	 * any validation error, the record on Submission database is always
	 * updated. The record on CSDB will only be updated if the record is clean.
	 * This method is primarily used by the error correction page.
	 * 
	 * @param submittedData
	 * @param lastUpdatedUserId
	 * @param lastUpdatedUserProfileId
	 * @return
	 * @throws RemoteException
	 * @throws SystemException
	 */
	public SubmissionValidationBundle validateAndSaveCensusSubmissionItem(
			EmployeeData submittedData, String lastUpdatedUserId,
			Long lastUpdatedUserProfileId, boolean ignoreWarnings, boolean ignoreSimilarSsn,
			boolean ignoreEmployeeIdRules) throws ApplicationException, RemoteException;
	
	/**
	 * This method is to update the duplicate submission
	 * employerProvidedEmailAddress as blank.
	 * 	
	 * @param contractId
	 * @param submissionId
	 * @param formSequenceNumber
	 * @param employerProvidedEmailAddress
	 * @param userId
	 * @throws RemoteException
	 */
	
	public void deleteDuplicateEmailAddressesOnFile(Integer contractId,
			Integer submissionId, Integer formSequenceNumber,
			String employerProvidedEmailAddress, String userId)
			throws RemoteException;
	
	 /**
     * Returns the Creator userID for the given submission.
     * @param submissionId
     * @return
     * @throws RemoteException
     */
    public String getSubmissionCreatorId(Integer submissionId) throws RemoteException;

    /**Method to determines whether the DB table has process_status_code in draft or not.
     * @param submissionId
     * @param contractId
     * @throws RemoteException
     */
    public boolean isSubmissionInDraft(int submissionId, int contractId) throws RemoteException;
}
