package com.manulife.pension.ps.web.delegate;


import java.util.Date;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.delegate.AbstractRMIServiceDelegate;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.ps.web.delegate.exception.UnableToAccessDataCheckerException;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.stp.datachecker.ICon2DataProblem;
import com.manulife.pension.stp.datachecker.STPDataCheckerService;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.log.LogUtility;

/**
 * @author drotele
 * This class uses RMI connection to perform i:file operations
 * File upload etc.
 *
 */
public class DataCheckerServiceDelegate extends AbstractRMIServiceDelegate {

	private final static String RMI_SERVICE_NAME = "STPDataCheckerService";
	private static final int LEVEL_WARNINGS   = 10;
	private static final String SECURED_STP_DC_SERVICE_PORT = "SecuredSTPDCServicePort";

	private static DataCheckerServiceDelegate instance =
		new DataCheckerServiceDelegate();

	/**
	 * constructor
	 */
	public DataCheckerServiceDelegate() {
	}

	/* (non-Javadoc)
	 * @see com.manulife.pension.ps.web.delegate.AbstractRMIServiceDelegate#getReportServiceName()
	 */
	protected String getServiceName() {
		return RMI_SERVICE_NAME;
	}
	/*
	 * This method is used to get the STP Datachecker Port Number 
	 * */
	protected int  getPortNumber()
	{	
		String portNumber = new BaseEnvironment().getNamingVariable(SECURED_STP_DC_SERVICE_PORT, null);
		return Integer.parseInt(portNumber);
	}
	

	/**
	 * overrige the RMI server name that could be different for the
	 * i:file server
	 * @return
	 */
	protected String getRmiServer() {
		if (this.rmiServer == null) {
			this.rmiServer = Environment.getInstance().getRMISTPServerNamePrimary();
		}

		return rmiServer;
	}
	/**
	 * @return failover server name
	 */
	protected String getRmiFailoverServer() {
		// get RMI Server Name. Issue Environment lookup if it is not specified
		if (this.rmiFailoverServer == null) {
			this.rmiFailoverServer = Environment.getInstance().getRMISTPServerNameFailover();
		}
		return rmiFailoverServer;
	}

	/**
	 * @return
	 */
	public static DataCheckerServiceDelegate getInstance() {
		return instance;
	}

	/**
	 * Service Delegate method to do data checking of a transmission
	 * 
	 * contractID and submissionType are required since some transmissions contain more than one contract
	 * and/or both contributions and addresses
	 *
	 * @param transmissionID
	 * @param contractId
	 * @param submissionType
	 * @return ICon2DataProblem 
	 * @throws SystemException
	 */
	public ICon2DataProblem checkData(String transmissionId, String contractId, String submissionType)
			throws UnableToAccessDataCheckerException {

		ICon2DataProblem dataProblem;
		try {
			STPDataCheckerService dataCheckerService; 
			try {
				dataCheckerService = (STPDataCheckerService) getRemoteService();
			} catch (Exception e) {
				// log exception, but don't throw it
				SystemException se =new SystemException(e,
						DataCheckerServiceDelegate.class.getName(),
						"checkData",
						"DataCheckerServiceDelegate.checkData(): Caught exception trying to get dataCheckerService. " + 
						". The system will try failover server now.");
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
				// if data checker service can't be accessed, try again from the failover server
				dataCheckerService = (STPDataCheckerService) getRemoteServiceFailover();
			}
			dataProblem = dataCheckerService.dataCheck(transmissionId, contractId, submissionType);

		} catch (Exception e) {
			SystemException se =
				new SystemException(
					e,
					DataCheckerServiceDelegate.class.getName(),
					"checkData",
					"checkData for transmission[" + transmissionId
						+ "], contractId[" + contractId 
						+ "] and submissionType[" + submissionType + "]");
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw new UnableToAccessDataCheckerException(
					DataCheckerServiceDelegate.class.getName(),
					"checkData",
					"unable to checkData for transmission[" + transmissionId
					+ "], contractId[" + contractId 
					+ "] and submissionType[" + submissionType + "] as DataCheckerService cannot be accessed");
		}

		return dataProblem;
	}

	/**
	 * Service Delegate method to do archiving of a transmission
	 * 
	 * contractID and submissionType are required since some transmissions contain more than one contract
	 * and/or both contributions and addresses
	 *
	 * @param transmissionID
	 * @param contractId
	 * @param submissionType
	 * @param ICon2DataProblem
	 * @param userId
	 * @param userName
	 * @param clientId
	 * @param paymentEFfectiveDate
	 * @param isInternalUser
	 * @throws SystemException
	 */
	public void backupSubmission(String transmissionId, String contractId, String submissionType,
			ICon2DataProblem dataProblem, String userId, String userName, String clientId, Date paymentEffectiveDate,
			boolean isInternalUser) throws UnableToAccessDataCheckerException {

		try {
			STPDataCheckerService dataCheckerService; 
			try {
				dataCheckerService = (STPDataCheckerService) getRemoteService();
			} catch (Exception e) {	
				// log exception, but don't throw it
				SystemException se =new SystemException(e,
						DataCheckerServiceDelegate.class.getName(),
						"backupSubmission",
						"DataCheckerServiceDelegate.backupSubmission(): Caught exception trying to get dataCheckerService. " + 
						"The system will try failover server now.");
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
				
				dataCheckerService = (STPDataCheckerService) getRemoteServiceFailover();
			}
			dataCheckerService.backupSubmission(transmissionId, contractId, submissionType, dataProblem,
					userId, userName, clientId, paymentEffectiveDate, isInternalUser);

		/*	THIS CODE IS COMMENTED AS PART OF CL # 131762  - 
		 	This utility is making a remote call to STP DC service to connect to PROD2 DC error directory 
			for synchronous purge operation which is creating hung threads.
			
			// CL 118787 - START 
			// Decouple error file deletions from submission database updates 
			// This was to handle OutOfMemory exceptions encountered during online contribution submissions 
			try{
				// Common Log 55720: Delete the problem files only if there are no problems or errors
				if(dataProblem.getErrorLevel() <= LEVEL_WARNINGS){
					dataCheckerService.deleteAllDataCheckProblemFiles(
							Integer.parseInt(transmissionId),
							Integer.parseInt(contractId),
							submissionType);
				}

			}catch (Throwable cause) {
				
				// Log the exception in MRL and eat up the exception...
				final String message = 
					"Problem in STPDataCheckerService::deleteAllDataCheckProblemFiles: " 
					+ "Uunable to delete all data checker problem files for " 
					+ "submission[" + transmissionId + "], contractId[" + contractId + "] and submissionType[" + submissionType + "]";
				
				SystemException sysEx = new SystemException(cause, message);
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID, sysEx);
			}
			// CL 118787 - END  
		 */
			
		} catch (Exception e) {
			SystemException se =
				new SystemException(
					e,
					DataCheckerServiceDelegate.class.getName(),
					"backupSubmission",
					"backupSubmission for transmission[" + transmissionId
						+ "], contractId[" + contractId 
						+ "] and submissionType[" + submissionType + "}");
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw new UnableToAccessDataCheckerException(
					DataCheckerServiceDelegate.class.getName(),
					"backupSubmission",
					"unable to backup submisison for transmission[" + transmissionId
					+ "], contractId[" + contractId 
					+ "] and submissionType[" + submissionType + "] as DataCheckerService cannot be accessed");
		}

		return;
	}
    
    /**
     * Service Delegate method to do archiving of a vesting submission
     * 
     * @param submissionID
     * @param contractId
     * @param submissionType
     * @param ICon2DataProblem
     * @param userId
     * @param userName
     * 
     * @throws SystemException
     */
    public void dataCheckWrapUp(String submissionID, String contractId, String submissionType,
            ICon2DataProblem dataProblem, String userId, String userName) throws UnableToAccessDataCheckerException {

        try {
            STPDataCheckerService dataCheckerService; 
            try {
                dataCheckerService = (STPDataCheckerService) getRemoteService();
            } catch (Exception e) { 
                // log exception, but don't throw it
                SystemException se =new SystemException(e,
                        DataCheckerServiceDelegate.class.getName(),
                        "dataCheckWrapUp",
                        "DataCheckerServiceDelegate.dataCheckWrapUp(): Caught exception trying to get dataCheckerService. " + 
                        "The system will try failover server now.");
                LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
                
                dataCheckerService = (STPDataCheckerService) getRemoteServiceFailover();
            }
            dataCheckerService.dataCheckWrapUp(submissionID, contractId, submissionType, dataProblem,
                    userId, userName);

         /* THIS CODE IS COMMENTED AS PART OF CL # 131762  - 
	  		This utility is making a remote call to STP DC service to connect to PROD2 DC error directory 
			for synchronous purge operation which is creating hung threads.
			// CL 118787 - START 
			// Decouple error file deletions from submission database updates 
			// This was to handle OutOfMemory exceptions encountered during vesting submissions 
			try{
					dataCheckerService.deleteAllDataCheckProblemFiles(
							Integer.parseInt(submissionID),
							Integer.parseInt(contractId),
							submissionType);

			}catch (Throwable cause) {
				
				// Log the exception in MRL and eat up the exception...
				final String message = 
					"Problem in STPDataCheckerService::deleteAllDataCheckProblemFiles: " 
					+ "Uunable to delete all data checker problem files for " 
					+ "submission[" + submissionID + "], contractId[" + contractId + "] and submissionType[" + submissionType + "]";
				
				SystemException sysEx = new SystemException(cause, message);
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID, sysEx);
			}
			// CL 118787 - END  
          	*/            
            
        } catch (Exception e) {
            SystemException se =
                new SystemException(
                    e,
                    DataCheckerServiceDelegate.class.getName(),
                    "dataCheckWrapUp",
                    "dataCheckWrapUp for submission[" + submissionID
                        + "], contractId[" + contractId 
                        + "] and submissionType[" + submissionType + "}");
            LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
            throw new UnableToAccessDataCheckerException(
                    DataCheckerServiceDelegate.class.getName(),
                    "dataCheckWrapUp",
                    "unable to dataCheckWrapUp for submission[" + submissionID
                    + "], contractId[" + contractId 
                    + "] and submissionType[" + submissionType + "] as DataCheckerService cannot be accessed");
        }

        return;
    }
	
	/**
 	 * Service Delegate method to do remove syntactic problem files
	 * 
	 * @param submissionId
	 * @param contractId
	 * @param submissionType
	 * @return
	 * @throws SystemException
	 */
	public int deleteAllSyntacticProblemFiles(int submissionId, int contractId, String submissionType) 
			throws UnableToAccessDataCheckerException {
		int errorLevel = 0;
		try {
			STPDataCheckerService dataCheckerService; 
			try {
				dataCheckerService = (STPDataCheckerService) getRemoteService();
			} catch (Exception e) {
				// log exception, but don't throw it
				SystemException se = new SystemException(e,
						DataCheckerServiceDelegate.class.getName(),
						"deleteAllSyntacticProblemFiles",
						"DataCheckerServiceDelegate.deleteAllSyntacticProblemFiles(): Caught exception trying to get dataCheckerService. " + 
						"The system will try failover server now.");
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
				
				// if data checker service can't be accessed, try again from the failover server
				dataCheckerService = (STPDataCheckerService) getRemoteServiceFailover();
			}
			errorLevel = dataCheckerService.deleteAllSyntacticProblemFiles(submissionId, contractId, submissionType);
		} catch (Exception e) {
			SystemException se =
				new SystemException(
					e,
					DataCheckerServiceDelegate.class.getName(),
					"deleteAllSyntacticProblemFiles",					
					"deleteAllSyntacticProblemFiles for transmission[" + submissionId
						+ "], contractId[" + contractId 
						+ "] and submissionType[" + submissionType + "}");
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw new UnableToAccessDataCheckerException(
					DataCheckerServiceDelegate.class.getName(),
					"backupSubmission",
					"unable to backup submisison for transmission[" + submissionId
					+ "], contractId[" + contractId 
					+ "] and submissionType[" + submissionType + "] as DataCheckerService cannot be accessed");
		}
		return errorLevel;
	}
	
	/**
 	 * Service Delegate method to do remove datacheck problem files
	 * 
	 * @param submissionId
	 * @param contractId
	 * @param submissionType
	 * @return
	 * @throws SystemException
	 */
	public int deleteAllDataCheckProblemFiles(int submissionId, int contractId, String submissionType) 
			throws UnableToAccessDataCheckerException {
		int errorLevel = 0;
		try {
			STPDataCheckerService dataCheckerService; 
			try {
				dataCheckerService = (STPDataCheckerService) getRemoteService();
			} catch (Exception e) {
				// log exception, but don't throw it	
				SystemException se = new SystemException(e,
						DataCheckerServiceDelegate.class.getName(),
						"deleteAllDataCheckProblemFiles",
						"DataCheckerServiceDelegate.deleteAllDataCheckProblemFiles(): Caught exception trying to get dataCheckerService. " + 
						"The system will try failover server now.");
				LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);				
	
				// if data checker service can't be accessed, try again from the failover server
				dataCheckerService = (STPDataCheckerService) getRemoteServiceFailover();
			}
			errorLevel = dataCheckerService.deleteAllDataCheckProblemFiles(submissionId, contractId, submissionType);
		} catch (Exception e) {
			SystemException se =
				new SystemException(
					e,
					DataCheckerServiceDelegate.class.getName(),
					"deleteAllDataCheckProblemFiles",
					"deleteAllDataCheckProblemFiles for transmission[" + submissionId
						+ "], contractId[" + contractId 
						+ "] and submissionType[" + submissionType + "}");
			LogUtility.logSystemException(Constants.PS_APPLICATION_ID,se);
			throw new UnableToAccessDataCheckerException(
					DataCheckerServiceDelegate.class.getName(),
					"backupSubmission",
					"unable to backup submisison for transmission[" + submissionId
					+ "], contractId[" + contractId 
					+ "] and submissionType[" + submissionType + "] as DataCheckerService cannot be accessed");
		}
		return errorLevel;
	}


}
