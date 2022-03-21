package com.manulife.pension.ps.service.submission.dao;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.InvalidKeyException;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.submission.valueobject.Lock;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.security.dao.SearchDAO;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * @author parkand
 */
public class LockDAO extends BaseDatabaseDAO {

	private static final String CONTRIBUTION_CASE_TYPE_CODE = "C";
	private static final String ADDRESS_CASE_TYPE_CODE = "A";
    private static final String CENSUS_CASE_TYPE_CODE = "E";
    private static final String WITHDRAWAL_CASE_TYPE_CODE = "W";
    private static final String VESTING_CASE_TYPE_CODE = "V";
    private static final String LTPT_CASE_TYPE_CODE = "Q";
	
	private static final String className = LockDAO.class.getName();
	private static final Logger logger = Logger.getLogger(LockDAO.class);
	
	private static final String SQL_ACQUIRE_LOCK = 
		"update "
		+ STP_SCHEMA_NAME + "submission_case "
		+ "set last_locked_by_user_id = ?, last_locked_ts = current timestamp "
		+ "where submission_id = ? and contract_id = ? and submission_case_type_code = ?";
	
	private static final String SQL_RELEASE_LOCK = 
		"update "
		+ STP_SCHEMA_NAME + "submission_case "
		+ "set last_locked_by_user_id = null, last_locked_ts = null "
		+ "where submission_id = ? and contract_id = ? and submission_case_type_code = ?";
	
	private static final String SQL_CHECK_LOCK = 
		"select "
		+ "submission_id submissionId, "
		+ "contract_id contractId, "
		+ "submission_case_type_code submissionCaseType, "
		+ "last_locked_by_user_id userId, "
		+ "last_locked_ts lockTS "
		+ "from " + STP_SCHEMA_NAME + "submission_case "
		+ "where submission_id = ? and contract_id = ? and submission_case_type_code = ?";
	
	private static final String SQL_REFRESH_LOCK =
		"update "
		+ STP_SCHEMA_NAME + "submission_case "
		+ "set last_locked_ts = current timestamp "
		+ "where last_locked_by_user_id = ? and submission_id = ? and contract_id = ? and submission_case_type_code = ?";

	
	public static Lock acquireLock(Integer submissionId, Integer contractNumber, String submissionCaseType, String userId) throws SystemException {

		Lock lock = null;

		// first check if the lock is available
		lock = checkLock(submissionId, contractNumber, submissionCaseType, true);
		
		if ( lock.isActive() ) {
			
			// check if the lock is by the same person - if so, simply refresh it
			if ( userId.equals(lock.getUserId().trim()) ) {
				return refreshLock(lock);
			}
			
			// otherwise, the lock does not belong to this user - return null
			return null;
		}

		// build parameter array
		ArrayList params = new ArrayList(4);
		params.add(0,userId);
		params.add(1,submissionId);
		params.add(2,contractNumber);
		params.add(3,convertSubmissionCaseTypeCode(submissionCaseType));
		
		// now acquire the lock
		SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_ACQUIRE_LOCK);
		try {
			logger.debug("Executing Prepared SQL Statement: " + SQL_ACQUIRE_LOCK);
			handler.update(params.toArray());
			lock = checkLock(submissionId,contractNumber,submissionCaseType, true);
			// trim the userid
			lock.setUserId(lock.getUserId().trim());
		} catch (DAOException e) {
			throw new SystemException(e, className, "acquireLock", "Problem occurred prepared call: " + SQL_ACQUIRE_LOCK);
		}
		
		return lock;
	}
	
	private static String getUserName(String userId) throws SystemException {
		String userName = "";
		UserInfo userInfo = null;
		if (userId != null && userId.length() != 0) {
			try {
				Integer.parseInt(userId);
				//LDAPUser user = LdapDao.getUser(userId);
				userInfo = SearchDAO.searchUserInDatabase(null, userId);
			} catch (NumberFormatException e) { 
				// might be characters in which case we don't lookup the user info
			}
		}
		
		if (userInfo != null) {
			if (userInfo.getLastName() != null) 
				userName += userInfo.getLastName();
			if (userInfo.getLastName() != null && userInfo.getFirstName() != null) 
				userName = userName += ", ";
			if (userInfo.getFirstName() != null) 
				userName += userInfo.getFirstName();
		}
		
		return userName;

	}
	
	public static Lock checkLock(Integer submissionId, Integer contractNumber, String submissionCaseType) throws SystemException {
		return checkLock(submissionId, contractNumber, convertSubmissionCaseTypeCode(submissionCaseType), false);
	}
	
	public static Lock checkLock(Integer submissionId, Integer contractNumber, String submissionCaseType, boolean retrieveUserName) throws SystemException {

		Lock lock = null;
		
		ArrayList params = new ArrayList(3);
		params.add(0,submissionId);
		params.add(1,contractNumber);
		params.add(2,convertSubmissionCaseTypeCode(submissionCaseType));

		SelectBeanQueryHandler handler = new SelectBeanQueryHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_CHECK_LOCK, Lock.class);
		try {
			logger.debug("Executing Prepared SQL Statement: " + SQL_CHECK_LOCK);
			lock = (Lock)handler.select(params.toArray());
		} catch (DAOException e) {
			throw new SystemException(e, className, "checkLock", "Problem occurred prepared call: " + SQL_CHECK_LOCK);
		}

		// retrieve the username (if the userid is all numeric)
		if (retrieveUserName) {
			lock.setUserName(getUserName(lock.getUserId()));
		}

		return lock;
	}
	
	public static boolean releaseLock(Lock lock) throws SystemException {
		// build parameter array
		ArrayList params = new ArrayList(3);
		params.add(0,lock.getSubmissionId());
		params.add(1,lock.getContractId());
		params.add(2,convertSubmissionCaseTypeCode(lock.getSubmissionCaseType()));

		// first check to make sure the lock belongs to this user
		Lock releasedLock = checkLock(lock.getSubmissionId(),lock.getContractId(),convertSubmissionCaseTypeCode(lock.getSubmissionCaseType()));
		if ( lock.getUserId() == null || !lock.getUserId().trim().equals(releasedLock.getUserId()) ) {
			// lock does not belong to this user
			return false;
		}

		// release the lock
		SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_RELEASE_LOCK);
		try {
			logger.debug("Executing Prepared SQL Statement: " + SQL_RELEASE_LOCK);
			handler.update(params.toArray());
		} catch(InvalidKeyException invalidKeyException){
			logger.debug("Executing Prepared SQL Statement: " + SQL_RELEASE_LOCK + " No resultset retrieved.");
		} catch (DAOException e) {
			throw new SystemException(e, className, "releaseLock", "Problem occurred prepared call: " + SQL_RELEASE_LOCK);
		}

		return true;
	}
	
	public static Lock refreshLock(Lock lock) throws SystemException {
		Lock refreshedLock = null;
		
		// build parameter array
		ArrayList params = new ArrayList(4);
		params.add(0,lock.getUserId());
		params.add(1,lock.getSubmissionId());
		params.add(2,lock.getContractId());
		params.add(3,convertSubmissionCaseTypeCode(lock.getSubmissionCaseType()));
		
		// first check to make sure the lock belongs to this user
		refreshedLock = checkLock(lock.getSubmissionId(),lock.getContractId(),convertSubmissionCaseTypeCode(lock.getSubmissionCaseType()));
		if ( lock.getUserId() == null || !lock.getUserId().trim().equals(refreshedLock.getUserId()) ) {
			// lock does not belong to this user
			return null;
		}

		// refresh the lock ts
		SQLUpdateHandler handler = new SQLUpdateHandler(SUBMISSION_DATA_SOURCE_NAME, SQL_REFRESH_LOCK);
		try {
			logger.debug("Executing Prepared SQL Statement: " + SQL_REFRESH_LOCK);
			handler.update(params.toArray());
			refreshedLock = checkLock(lock.getSubmissionId(),lock.getContractId(),lock.getSubmissionCaseType(),true);
		} catch (DAOException e) {
			throw new SystemException(e, className, "refreshLock", "Problem occurred prepared call: " + SQL_REFRESH_LOCK);
		}

		return refreshedLock;

	}

	/**
	 * Convert the given submission case type code to 'C' if it's not a
	 * recognized type code.
	 * 
	 * @param submissionCaseType
	 * @return
	 */
	private static String convertSubmissionCaseTypeCode(
			String submissionCaseType) {
		String convertedCaseTypeCode = submissionCaseType.trim();
		String[] validCodes = new String[] {
				LoanConstants.SUBMISSION_CASE_TYPE_CODE,
				WITHDRAWAL_CASE_TYPE_CODE, VESTING_CASE_TYPE_CODE,
				ADDRESS_CASE_TYPE_CODE, CENSUS_CASE_TYPE_CODE, LTPT_CASE_TYPE_CODE};
		for (String validCode : validCodes) {
			if (validCode.equals(convertedCaseTypeCode)) {
				return convertedCaseTypeCode;
			}
		}
		return CONTRIBUTION_CASE_TYPE_CODE;
	}

}
