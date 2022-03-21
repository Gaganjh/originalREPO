package com.manulife.pension.ps.web.passcodeTrans;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.service.passcodeTrans.dao.UserCommBounceBackStatusDAO;
import com.manulife.pension.ps.service.passcodeTrans.valueobject.UserCommBounceBackStatusVO;
import com.manulife.pension.service.security.utility.sms.UserCommBounceBackStatusService;

/**
 * A generic periodic process to retrieve all SIDs from DB table with
 * non-delivered status and calling the Get API to get the latest status and
 * update the DB record.
 * 
 * @author bobbave
 */

public class UserCommBounceBackPeriodicProcess implements PeriodicProcess {
	private static final Logger logger = Logger.getLogger(UserCommBounceBackPeriodicProcess.class);

	/**
	 * Method that gets called by the timer framework when the job is to be run.
	 */
	// changes for Periodic process DB properties with default values
	protected int maxRecords = new Integer(1000);
	protected int maxRetry = new Integer(3); 
	protected String applicationId = "PS";
	
	public int getMaxRecords() {
		return maxRecords;
	}
	
	public int getMaxRetry() {
		return maxRetry;
	}
	
	public void setMaxRecords(int records) {
		this.maxRecords = records;
	}
	
	public void setMaxRetry(int retry) {
		this.maxRetry = retry;
	}

    // end changes to handle Periodic Process DB properties
	public void execute() {
		try {
			logger.info("Started User Comm Bounce Back Periodic Process");
			// Pull the user Comm Bounce back status records from the
			// User_Passcode_Comm_Status table
			List<UserCommBounceBackStatusVO> userCommBounceBackStatusList = UserCommBounceBackStatusDAO
					.getMaxUserCommBounceBackStatus(getMaxRecords(),getMaxRetry());
			String statusFromAPI = null;
			if (userCommBounceBackStatusList != null && !userCommBounceBackStatusList.isEmpty()) {
				// Iterate over each user passcode transition record
             try {
					for (UserCommBounceBackStatusVO userCommBounceBackStatusVO : userCommBounceBackStatusList) {
						    // Process if COMM_TYPE is SMS
							if (userCommBounceBackStatusVO.getCommType() != null && userCommBounceBackStatusVO
									.getCommType().trim().equalsIgnoreCase(CommonConstants.COMM_TYPE_SMS)) {
								statusFromAPI = getCommTypeSMSStatus(userCommBounceBackStatusVO,
										CommonConstants.COMM_TYPE_SMS);
							} else {
								// Process COMM_TYPE Voice
								statusFromAPI = getCommTypeVoiceStatus(userCommBounceBackStatusVO,
										CommonConstants.COMM_TYPE_VOICE);
							}
							updateCommBounceBackStatus(userCommBounceBackStatusVO, statusFromAPI);
						}
				} catch (Exception e) {
					// Construct String for logging
					StringBuilder errorDetails = new StringBuilder();
					errorDetails.append(this.getClass().getName());
					errorDetails.append(": execute() failed");
					String exceptionMessage = e.getMessage() != null ? e.getMessage() : e.toString();
					errorDetails.append(exceptionMessage + ". ");
                    String errorLogStr = getExceptionForLogging(errorDetails.toString(), e);
					logger.error(errorLogStr, e);
				}

			} else {
				logger.info("No records found in User Comm Bounce Back Periodic Process.");
			}
			logger.info("Completed User Comm Bounce Back Periodic Process.");
		} catch (SystemException e) {
			logger.error("User Communication Bounce Back Periodic Process failed. Exception: " + e.toString(), e);
		}
	}

	/**
	 * Get Voice Comm Bounce Back status from digital API
	 * 
	 * @param userPasscodeTransitionStatusVO
	 * @return
	 * @throws SystemException
	 */
	private String getCommTypeVoiceStatus(UserCommBounceBackStatusVO userCommBounceBackStatusVO, String commTypeVoice)
			throws SystemException {
		// Get Voice Status for the given SID from digital API
		if (userCommBounceBackStatusVO.getsId() != null && !StringUtils.isEmpty(userCommBounceBackStatusVO.getsId())) {
			return UserCommBounceBackStatusService.getInstance().getSMSorVoiceCommStatusFromNotificationAPI(
					userCommBounceBackStatusVO.getUserProfileId(), userCommBounceBackStatusVO.getsId(), commTypeVoice);
		} else {
			logger.info("SID is null or empty for the profileID:" + userCommBounceBackStatusVO.getUserProfileId());
			return null;
		}
	}

	/**
	 * Get SMS Comm Bounce Back status from digital API
	 * 
	 * @param userPasscodeTransitionStatusVO
	 * @return
	 * @throws SystemException
	 */
	private String getCommTypeSMSStatus(UserCommBounceBackStatusVO userCommBounceBackStatusVO, String commTypeSMS)
			throws SystemException {
		// Get SMS Status for the given SID from digital API
		if (userCommBounceBackStatusVO.getsId() != null && !StringUtils.isEmpty(userCommBounceBackStatusVO.getsId())) {
			return UserCommBounceBackStatusService.getInstance().getSMSorVoiceCommStatusFromNotificationAPI(
					userCommBounceBackStatusVO.getUserProfileId(), userCommBounceBackStatusVO.getsId(), commTypeSMS);
		} else {
			logger.info("SID is null or empty for the profileID:" + userCommBounceBackStatusVO.getUserProfileId());
			return null;
		}

	}

	/**
	 * Update Comm Bounce Back Status for CommType SMS or Voice
	 * 
	 * @param userPasscodeTransitionStatusVO
	 * @throws SystemException
	 */
	private void updateCommBounceBackStatus(UserCommBounceBackStatusVO userCommBounceBackStatusVO, String status)
			throws SystemException {
		logger.info("Started Processing Comm Type SMS or Voice");

		if (!(status == null || StringUtils.isEmpty(status))) {
			// update the table with status , last_updated_TS and no_of_attempts
			int noOfAttempts = userCommBounceBackStatusVO.getNoOfAttempts() + 1;
			UserCommBounceBackStatusDAO.updateUserPasscodeCommStatus(userCommBounceBackStatusVO.getUserProfileId(),
					userCommBounceBackStatusVO.getsId(), status, noOfAttempts);
		} else {
			logger.info("Invalid data (or) No_Of_Attempts are exceeded for profileId"
					+ userCommBounceBackStatusVO.getUserProfileId());
		}
		logger.info("Completed Processing of Comm Type SMS or Voice for profileId "
				+ userCommBounceBackStatusVO.getUserProfileId());
	}
	
	

		
	private static String getExceptionForLogging(String errorMessage, Throwable e)
	{
		StringBuffer errorStr = new StringBuffer();
		errorStr.append(errorMessage);
		errorStr.append(" ");
		
		if(e instanceof SystemException) {
			errorStr.append(ExceptionUtils.getStackTrace(e.getCause() != null ? e.getCause(): e).trim());
		} else {
			errorStr.append(ExceptionUtils.getStackTrace(e).trim());
		}
		
		return errorStr.toString();		
	}

}
