package com.manulife.pension.ps.service.submission;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.ejb.SessionContext;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.eligibility.EligibilityDataHelper;
import com.manulife.pension.event.Event;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.event.util.FileSubmissionHelper;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.submission.dao.CensusSubmissionDAO;
import com.manulife.pension.ps.service.submission.dao.LockDAO;
import com.manulife.pension.ps.service.submission.dao.SubmissionConstants;
import com.manulife.pension.ps.service.submission.dao.SubmissionDAO;
import com.manulife.pension.ps.service.submission.util.SubmissionParticipantComparatorFactory;
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
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.eligibility.EligibilityRequestConstants;
import com.manulife.pension.service.employee.util.DeferralProcessing;
import com.manulife.pension.service.employee.util.EligibilityDataValidator;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeDataValidatorHelper;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.employee.util.SubmittedEmployeeDataValidator;
import com.manulife.pension.service.employee.util.EmployeeData.Property;
import com.manulife.pension.service.employee.util.dao.EmployeeDataDAO;
import com.manulife.pension.service.employee.util.rules.EmployeeIdRules;
import com.manulife.pension.service.employee.util.rules.SSNRules.SimilarSSNRule;
import com.manulife.pension.service.employee.valueobject.AddressVO;
import com.manulife.pension.service.employee.valueobject.UserIdType;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.submission.SubmissionCaseProcessStatusCodes;
import com.manulife.pension.submission.SubmissionCaseStatusHelper;
import com.manulife.pension.submission.SubmissionCaseStatusMap;
import com.manulife.pension.submission.SubmissionCaseStatusMapFactory;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.Pair;

/**
 * Bean implementation class for Enterprise Bean: ReportService
 */
public class SubmissionServiceBean implements javax.ejb.SessionBean {

	private static final long serialVersionUID = 1L;

	private SessionContext mySessionCtx;
	private SubmittedEmployeeDataValidator submittedEmployeeDataValidator;
	private static final String TRANSFER_CONTRIBUTION = "X";
	private static final String CONTRIBUTION_TYPE = "C";
	private static final Logger logger = Logger.getLogger(SubmissionServiceBean.class);
	private static final String SOURCE_CENSUS_FILE = "IF"; // I-File
    public static final int SUBMISSION_SYSTEM_ADMIN_ID = 3;	
    public static final String DEFERRAL_TYPE_DOLLAR = "$";
    private static final String LONG_TERM_PART_TIME_TYPE = "Q";

	/**
	 * Deletes a submission.
	 * If the submission has multiple types/contracts, only the specified contract and type submission case is deleted.
	 * @param id
	 * @param sjNum
	 * @param contractNo
	 * @param typeCode
	 */
	public void deleteSubmission(int id, int contractNo, String typeCode) {
		try {
			SubmissionDAO.deleteSubmission(id, contractNo, typeCode);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "deleteSubmission",
					"Unchecked exception occurred. Input Paramereter is "+
					"submission id:"+id);
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

    /**
     * Cancel a submission.
     * 
     * @param id
     * @param sjNum
     * @param contractNo
     * @param typeCode
     * @param userProfileId The user profile ID of the user cancelling the submission.
     */
    public void cancelSubmission(int id, int contractNo, String typeCode, long userProfileId) {
        try {
            SubmissionDAO.cancelSubmission(id, contractNo, typeCode);

            if(StringUtils.equalsIgnoreCase(LONG_TERM_PART_TIME_TYPE, typeCode)) {
            	fireLongTermPartTimeFileSubmissionCancelledEvent(id, contractNo, userProfileId);
            } else {
            	fireVestingFileSubmissionCancelledEvent(id, contractNo, userProfileId);
            }

        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass().getName(), "cancelSubmission",
                    "Unchecked exception occurred. Input Paramereter is "+
                    "submission id:"+id);
            throw ExceptionHandlerUtility.wrap(se);
        }
    }

    /**
     * Fires the vesting file submission cancelled event.
     * 
     * @param id The submission ID.
     * @param contractNo The contract ID.
     * @param userProfileId The user profile ID of the user cancelling this submission.
     * @throws SystemException If a system exception occurs.
     */
    private void fireVestingFileSubmissionCancelledEvent(final int id, final int contractNo,
            final long userProfileId) throws SystemException {
        final Integer userProfileIdInteger = ((Long) userProfileId).intValue();

        final Event event = FileSubmissionHelper.getInstance().getVestingDataCheckEvent(id,
                contractNo, userProfileIdInteger,
                SubmissionCaseProcessStatusCodes.CANCELED_STATUS);

        if (event != null) {
            // Get the event client utility which is used to fire the event.
            final EventClientUtility eventClientUtility = EventClientUtility
                    .getInstance(new BaseEnvironment().getAppId());

            // Fire the event.
            try {
                eventClientUtility.prepareAndSendJMSMessage(event);
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            } // end try/catch
        } // fi
    }
    
    /**
     * Fires the LongTermPartTime file submission cancelled event.
     * 
     * @param id The submission ID.
     * @param contractNo The contract ID.
     * @param userProfileId The user profile ID of the user cancelling this submission.
     * @throws SystemException If a system exception occurs.
     */
    private void fireLongTermPartTimeFileSubmissionCancelledEvent(final int id, final int contractNo,
            final long userProfileId) throws SystemException {
        final Integer userProfileIdInteger = ((Long) userProfileId).intValue();

        final Event event = FileSubmissionHelper.getInstance().getLongTermPartTimeDataCheckEvent(id,
                contractNo, userProfileIdInteger,
                SubmissionCaseProcessStatusCodes.CANCELED_STATUS, false);

        if (event != null) {
            // Get the event client utility which is used to fire the event.
            final EventClientUtility eventClientUtility = EventClientUtility
                    .getInstance(new BaseEnvironment().getAppId());

            // Fire the event.
            try {
                eventClientUtility.prepareAndSendJMSMessage(event);
            } catch (SystemException systemException) {
                throw new RuntimeException(systemException);
            } // end try/catch
        } // fi
    }

	/**
	 * @param id
	 * @param contractNo
	 * @param typeCode
	 * @return
	 */
	public SubmissionHistoryItem getSubmissionCase(int id, int contractNo, String typeCode) {
		SubmissionHistoryItem submissionCase = null;
		try {
			submissionCase = SubmissionDAO.getSubmissionCase(id, contractNo, typeCode);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "getSubmissionCase",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+id+" contractNo:"+contractNo+" type:"+typeCode);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return submissionCase;
	}

    public String getParticipantSortOption(int contractNo) {
        String sortOption = null;
        try {
            sortOption = SubmissionDAO.getParticipantSortOption(contractNo);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass()
                    .getName(), "getParticipantSortOption",
                    "Unchecked exception occurred. Input Paramereters are "
                            + "contractNo:" + contractNo);
            throw ExceptionHandlerUtility.wrap(se);
        }
        return sortOption;
    }
    
    public String getDeferralType(int contractId) {
        String deferralType = null;
        try {
            deferralType = SubmissionDAO.getDeferralType(contractId);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass()
                    .getName(), "getDeferralType",
                    "Unchecked exception occurred. Input Paramereters are "
                            + "contractId:" + contractId);
            throw ExceptionHandlerUtility.wrap(se);
        }
        return deferralType;
    }    
    
	/**
	 * @param cnno
	 * @param subId
	 * @param prtIdNum
	 * @return
	 */
	public CensusSubmissionItem getCensusSubmissionItem(int cnno, int subId, String prtIdNum) {
		CensusSubmissionItem item = null;
		try {
			 item = CensusSubmissionDAO.getCensusSubmissionItem(cnno, subId, prtIdNum);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "getSubmittedAddress",
					"Unchecked exception occurred. Input Paramereters are "+
					"cnno:"+cnno+" subId:"+subId+" prtIdNum:"+prtIdNum);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return item;
	}
    
    
    /**
     * Retrieves one census submission record from SDB
     * 
     * @param contractId
     * @param submissionId
     * @param sequenceNumber
     * @return
     * @throws SystemException
     */
    public EmployeeData getSTPEmployeeData(Integer contractId, Integer submissionId, 
            Integer sequenceNumber) {
        EmployeeData submitted = null;
        try {
            DataSource sdbDataSource = BaseDatabaseDAO
            .getDataSource(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
            
            submitted = EmployeeDataDAO.getSTPEmployeeData(
                    sdbDataSource, submissionId, contractId,
                    sequenceNumber, getParticipantSortOption(contractId));
        } catch (DAOException e) {
            throw ExceptionHandlerUtility.wrap(BaseDatabaseDAO
                    .handleDAOException(e, SubmissionServiceBean.class
                            .getName(), "getSTPEmployeeData",
                            "Failed to perform operation for contract ID:"
                                    + contractId + " submissionId:"
                                    + submissionId + " sequenceNumber:"
                                    + sequenceNumber));
        } 
        
        return submitted;
    }

	/**
	 * Returns the SubmissionPaymentItem for a payment only submission
	 * @param id the submission id
	 * @param contractNumber
	 * @return SubmissoinPaymentItem
	 */
	public SubmissionPaymentItem getPaymentOnlySubmission(int id, int contractNumber) {
		SubmissionPaymentItem item = null;
		try {
			item = SubmissionDAO.getPaymentOnlySubmission(id, contractNumber);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "getPaymentOnlySubmission",
					"Unchecked exception occurred. Input Paramereter is "+
					"submission id:"+id);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return item;
	}

	/**
	 *
	 * @deprecated getContributionDetails(int submisisonId, int contractId, ReportCriteria reportCriteria) should be used instead.
	 * @param submisisonId
	 * @param contractId
	 * @return ContributionDetail item including all participants
	 */
	public ContributionDetailItem getContributionDetails(int submisisonId, int contractId) {
		ContributionDetailItem contributionDetail = null;
		try {
			contributionDetail = SubmissionDAO.getContributionDetails(submisisonId, contractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "getContributionDetail",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+submisisonId+" contractNo:"+contractId);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return contributionDetail;
	}
    
    /**
     *
     * getVestingDetails(int submisisonId, int contractId, ReportCriteria reportCriteria)
     * @param submisisonId
     * @param contractId
     * @param criteria
     * @return VestingDetail item including all participants
     */
    public VestingDetailItem getVestingDetails(int submisisonId, int contractId, ReportCriteria criteria) {
        VestingDetailItem vestingDetail = null;
        try {
            vestingDetail = SubmissionDAO.getVestingDetails(submisisonId, contractId, criteria);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass().getName(), "getVestingDetail",
                    "Unchecked exception occurred. Input Paramereters are "+
                    "id:"+submisisonId+" contractNo:"+contractId);
            throw ExceptionHandlerUtility.wrap(se);
        }
        return vestingDetail;
    }

    /**
    *
    * getLongTermPartTimeDetails(int submisisonId, int contractId, ReportCriteria reportCriteria)
    * @param submisisonId
    * @param contractId
    * @param criteria
    * @return LongTermPartTimeDetail item including all participants
    */
   public LongTermPartTimeDetailItem getLongTermPartTimeDetails(int submisisonId, int contractId, ReportCriteria criteria) {
	   LongTermPartTimeDetailItem longTermPartTimeDetail = null;
       try {
    	   longTermPartTimeDetail = SubmissionDAO.getLongTermPartTimeDetails(submisisonId, contractId, criteria);
       } catch (SystemException e) {
           throw ExceptionHandlerUtility.wrap(e);
       } catch (RuntimeException e) {
           SystemException se = new SystemException(e, this.getClass().getName(), "getLongTermPartTimeDetails",
                   "Unchecked exception occurred. Input Paramereters are "+
                   "id:"+submisisonId+" contractNo:"+contractId);
           throw ExceptionHandlerUtility.wrap(se);
       }
       return longTermPartTimeDetail;
   }
    
	/**
	 *
	 * @param submisisonId
	 * @param contractId
	 * @param reportCriteria
	 * @return ContributionDetail item for participants on page specified by reportCriteria
	 */

	public ContributionDetailItem getContributionDetails(int submisisonId, int contractId, ReportCriteria reportCriteria) {
		ContributionDetailItem contributionDetail = null;
		try {
			contributionDetail = SubmissionDAO.getContributionDetails(submisisonId, contractId);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "getContributionDetail",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+submisisonId+" contractNo:"+contractId);
			throw ExceptionHandlerUtility.wrap(se);
		}
		sort(contributionDetail.getSubmissionParticipants(), reportCriteria.getSorts());
		contributionDetail.setSubmissionParticipants(page(contributionDetail.getSubmissionParticipants(), reportCriteria));
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
			String notificationEmailAddress) {
		CopiedSubmissionHistoryItem copiedItem = null;
		try {
			copiedItem = SubmissionDAO.copyContributionDetails(submissionId, contractId, userId,
					userName, userTypeCode, userTypeId, userTypeName, notificationEmailAddress);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "copyContributionDetails",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+submissionId+" contractNo:"+contractId);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return copiedItem;
	}

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
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @returns CopiedSubmissionHistoryItem
	 * @throws RemoteException
	 */
	public CopiedSubmissionHistoryItem copyLastSubmittedContributionDetails(int contractId, String userId,
			String userName, String userTypeCode, BigDecimal userTypeId, String userTypeName, 
			String notificationEmailAddress) {
		CopiedSubmissionHistoryItem copiedItem = null;
		int submissionId = -1;
		try {
			// first, retrieve the tracking number of the submission to be copied
			submissionId = SubmissionDAO.getLastSubmittedContributionDetailsSubmissionId(contractId, userId);

			if ( submissionId == -1) {
				return null;
			}
			copiedItem = SubmissionDAO.copyContributionDetails(submissionId, contractId, userId,
					userName, userTypeCode, userTypeId, userTypeName, notificationEmailAddress);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "copyContributionDetails",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+submissionId+" contractNo:"+contractId);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return copiedItem;
	}

	/**
	 * Saves changes to a contribution.
	 * @param userId
	 * @param userName
	 * @param userTypeCode
	 * @param userTypeId
	 * @param userTypeName
	 * @param processorUserId
	 * @param newItem
	 * @param isResubmit
	 * @return
	 */
	public void saveContributionDetails(String userId, String userName, String userType, String userTypeID,
			String userTypeName, String processorUserId, ContributionDetailItem newItem, boolean isResubmit) {
		ContributionDetailItem contributionDetail = null;
		try {
			SubmissionDAO.saveContributionDetails(userId, userName, userType, userTypeID, userTypeName,
					processorUserId, newItem, isResubmit);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "saveContributionDetails",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+newItem.getSubmissionId()+" contractNo:"+newItem.getContractId().toString());
			throw ExceptionHandlerUtility.wrap(se);
		}
	}
    
    /**
     * Saves changes to a vesting submission.
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param processorUserId
     * @param newItem
     * @param isResubmit
     * @return
     */
    public Integer saveVestingDetails(String userId, String userName, String userType, String userTypeID,
            String userTypeName, String processorUserId, VestingDetailItem newItem, boolean isResubmit) {
        try {
            return SubmissionDAO.saveVestingDetails(userId, userName, userType, userTypeID, userTypeName,
                    processorUserId, newItem, isResubmit);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass().getName(), "saveVestingDetails",
                    "Unchecked exception occurred. Input Paramereters are "+
                    "id:"+newItem.getSubmissionId()+" contractNo:"+newItem.getContractId().toString());
            throw ExceptionHandlerUtility.wrap(se);
        }
    }

    /**
     * Saves changes to a LTPT submission.
     * @param userId
     * @param userName
     * @param userTypeCode
     * @param userTypeId
     * @param userTypeName
     * @param processorUserId
     * @param newItem
     * @param isResubmit
     * @return
     */
    public Integer saveLongTermPartTimeDetails(String userId, String userName, String userType, String userTypeID,
            String userTypeName, String processorUserId, LongTermPartTimeDetailItem newItem, boolean isResubmit) {
        try {
            return SubmissionDAO.saveLongTermPartTimeDetails(userId, userName, userType, userTypeID, userTypeName,
                    processorUserId, newItem, isResubmit);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass().getName(), "saveLongTermPartTimeDetails",
                    "Unchecked exception occurred. Input Paramereters are "+
                    "id:"+newItem.getSubmissionId()+" contractNo:"+newItem.getContractId().toString());
            throw ExceptionHandlerUtility.wrap(se);
        }
    }
    
	/**
	 * cleans up redundant contribution data after submit.
	 * @param submissionId
	 * @param contractNumber
	 * @param userId
	 * @param userType
	 * @return
	 */
	public void cleanupContributionDetails(Integer submissionId, Integer contractNumber, String userId,
			String userType) {
		try {
			SubmissionDAO.cleanupContributionDetails(submissionId, contractNumber, userId, userType);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "saveContributionDetails",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+submissionId+" contractNo:"+contractNumber);
			throw ExceptionHandlerUtility.wrap(se);
		}
	}
    
    /**
     * cleans up redundant vesting data after submit.
     * @param submissionId
     * @param contractNumber
     * @param userId
     * @param userType
     * @return
     */
    public void cleanupVestingDetails(Integer submissionId, Integer contractNumber, String userId,
            String userType) {
        try {
            SubmissionDAO.cleanupVestingDetails(submissionId, contractNumber, userId, userType);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass().getName(), "cleanupVestingDetails",
                    "Unchecked exception occurred. Input Paramereters are "+
                    "id:"+submissionId+" contractNo:"+contractNumber);
            throw ExceptionHandlerUtility.wrap(se);
        }
    }

	/**
	 * Sort the list according to the required sort order.
	 *
	 * @param items the list to sort
	 * @param sorts the sorting fields list
	 */
	private static void sort(List items, ReportSortList sorts) {
		if (items != null && sorts != null && sorts.size() != 0) {
			ReportSort firstSort = (ReportSort) sorts.get(0);
			Collections.sort(items,
					SubmissionParticipantComparatorFactory.getInstance().getComparator(firstSort.getSortField(), firstSort.getSortDirection()));
		}
	}
	/**
	 * Return only the items for the current page and page size.
	 *
	 * @param items the list to paginate
	 * @param criteria to get the paging info from
	 *
	 * @return
	 */
	private static List page(List items, ReportCriteria criteria) {
		List pageDetails = new ArrayList(criteria.getPageSize());

		if (items != null) {
			for (int i = criteria.getStartIndex() - 1;
			i < criteria.getPageNumber() * criteria.getPageSize() && i < items.size(); i++) {
				pageDetails.add(items.get(i));
			}
		}
		return pageDetails;
	}

	/**
	 * Adds a list of participants, currently active in the
	 * contract to a given submission.
	 * @param submissionId
	 * @param contractId
	 * @param userId
	 * @param participantsToAdd
	 */
	public void addPartcipantsToContribution(int submissionId, int contractId, String userId, ArrayList<AddableParticipant> participantsToAdd)  {
		try {
			SubmissionDAO.addParticipantsToContribution(submissionId, contractId, userId, participantsToAdd);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "copyContributionDetails",
					"Unchecked exception occurred. Input Paramereters are "+
					"id:"+submissionId+" contractNo:"+contractId);
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

	/**
	 * given a contractId, create a new submission case
	 * and return an object containing the trackingNumber
	 * of the new submission
	 *
	 * @param trackingNumber
	 * @param ContractId
	 * @param userId
	 * @param userSSN
	 * @param userName
	 * @param userType
	 * @param userTypeId
	 * @param userTypeName
	 * @return newTrackingNumber
	 * @throws SystemException
	 */
	public CopiedSubmissionHistoryItem createContributionDetails(int contractId, String userId,
			String userName, String userTypeCode, BigDecimal userTypeId, String userTypeName,
			String notificationEmailAddress){
		CopiedSubmissionHistoryItem createdItem = null;
		try {
			createdItem = SubmissionDAO.createContributionDetails(contractId, userId,
					userName, userTypeCode, userTypeId, userTypeName, notificationEmailAddress);
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "createContributionDetails",
					"Unchecked exception occurred. Input Paramereters are "+
					"contractNo:"+contractId);
			throw ExceptionHandlerUtility.wrap(se);
		}
		return createdItem;
	}

	public void updateAddress(CensusSubmissionItem address, AddressVO addressVo)	{
		try {
			String partIdentifier = "EE";
			if ( "SS".equals(address.getSortOptionCode()) ) {
				partIdentifier = address.getSsn();
			}

			EmployeeServiceDelegate.getInstance(Constants.PS_APPLICATION_ID).
						updateAddressByParticipantIdentifier(address.getContractNumber().intValue(), partIdentifier, addressVo);
		} catch (SystemException e) {
			// unable to update apollo for some reason
			// set the status code of the address record to 08
			address.setProcessStatus("08");
		} finally {
			try {
				// update the address record in STP
				CensusSubmissionDAO.updateAddress(address);

				// update the case level process status code
                String newCode = recalculateCensusSubmissionCaseProcessStatus(address
                        .getContractNumber(), address.getSubmissionId(), address
                        .getLastUpdatedUserId());
				SubmissionDAO.updateSubmissionCaseProcessStatus(newCode,address.getLastUpdatedUserId(),address.getSubmissionId(),address.getContractNumber(), new String[] {SubmissionConstants.SUBMISSION_CASE_TYPE_ADDRESS});

			} catch (SystemException e) {
				// can't update into STP either
				SystemException se = new SystemException(e, this.getClass().getName(), "updateAddress",
						"Unchecked exception occurred. Input Parameters are "+
						"address:"+address.toString());
				throw ExceptionHandlerUtility.wrap(se);
			}
		}
	}

	public void removeCensusSubmissionItem(Integer contractId, Integer submissionId, 
        Integer sequenceNumber, String userId)	{
		try {

			// cancel employee census record in STP
			CensusSubmissionDAO.cancelEmployeeCensus(contractId, submissionId, 
                    sequenceNumber, userId);

			// update the case level process status code
			String newCode = recalculateCensusSubmissionCaseProcessStatus(
                    contractId, submissionId,
                    userId);
			SubmissionDAO.updateSubmissionCaseProcessStatus(newCode,
					userId, submissionId, contractId, new String[] {
							SubmissionConstants.SUBMISSION_CASE_TYPE_CENSUS,
							SubmissionConstants.SUBMISSION_CASE_TYPE_ADDRESS });

		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "removeAddress",
					"Unchecked exception occurred. Input Parameters are "+
					" contractId: " + contractId +
					" submissionId: " + submissionId +
					" sequenceNumber: " + sequenceNumber +
					" userId: " + userId);
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

	public void discardCensusRecordsWithSyntaxErrors(int subId, int cnno, String userId) {
		try {
			Integer submissionId = new Integer(subId);
			Integer contractId = new Integer(cnno);
			CensusSubmissionDAO.discardSubmissionCase(submissionId, contractId, userId, SubmissionConstants.SUBMISSION_CASE_TYPE_CENSUS);
			CensusSubmissionDAO.logDiscardSubmissionCase(submissionId, contractId, userId);
			// update the case level process status code
            String newCode = recalculateCensusSubmissionCaseProcessStatus(contractId, submissionId,
                    userId);
			SubmissionDAO.updateSubmissionCaseProcessStatus(newCode,userId,submissionId,contractId, new String[] {SubmissionConstants.SUBMISSION_CASE_TYPE_CENSUS});

		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "discardCensusRecordsWithSyntaxErrors",
					"Unchecked exception occurred. Input Parameters are "+
					" cnno:"+cnno+
					" subId:"+subId+
					" userId:"+userId);
			throw ExceptionHandlerUtility.wrap(se);
		}
	}

	private String recalculateCensusSubmissionCaseProcessStatus(
            Integer contractNumber,
            Integer subId, String userId) throws SystemException {
		// first retrieve the census records
		ReportCriteria criteria = new ReportCriteria(null);
		criteria.addFilter(SubmissionHistoryReportData.FILTER_CONTRACT_NO,contractNumber);
		criteria.addFilter(CensusSubmissionReportData.FILTER_SUBMISSION_ID,subId);
		CensusSubmissionReportData censusSubmissionReportData = null;
		try {
			censusSubmissionReportData = CensusSubmissionDAO.getCensusSubmissions(criteria);
		} catch ( SystemException e ) {
			SystemException se = new SystemException(e, this.getClass().getName(), "recalculateAddressCaseProcessStatus",
					"Unable to retrieve address for case-level process status recalculation.");
			throw ExceptionHandlerUtility.wrap(e);
		}
		// now recalculate the status code
        final SubmissionCaseStatusMap asm = SubmissionCaseStatusMapFactory.getInstance()
                .getSubmissionCaseStatusMap("./censusCaseStatus.xml");
        final String statusCode = SubmissionCaseStatusHelper.getStatusCode(asm,
                censusSubmissionReportData.getDetails(), censusSubmissionReportData
                        .isSyntaxErrorIndicator());

        final boolean hasWarning = (0 != censusSubmissionReportData.getNumWarnings());

        // Need to check that this submission isn't submitted from autoloader.
        final SubmissionHistoryItem submissionHistoryItem = getSubmissionCase(subId,
                contractNumber, GFTUploadDetail.SUBMISSION_TYPE_CENSUS);

        final String sourceSystemCode = submissionHistoryItem.getApplicationCode();
        final boolean sourceSystemIsNotAutoloader = (!(StringUtils.equals("AL", sourceSystemCode)));
        
        if (sourceSystemIsNotAutoloader) {
            final Event event = FileSubmissionHelper.getInstance().getCensusDataCheckEvent(subId,
                    contractNumber, FileSubmissionHelper.getUserIdFromString(userId), statusCode,
                    hasWarning);

            if (event != null) {
                EventClientUtility.getInstance(new BaseEnvironment().getAppId())
                        .prepareAndSendJMSMessage(event);
            } // fi
        } // fi
        //Triggering the Auto Payroll Census Submission Events
        else if("AL".equals(sourceSystemCode)){
        	final Event event = FileSubmissionHelper.getInstance().getAutoPayrollCensusFileUploadedDataCheckEvent(subId,
                    contractNumber, FileSubmissionHelper.getUserIdFromString(userId), statusCode,
                    hasWarning);

            if (event != null) {
                EventClientUtility.getInstance(new BaseEnvironment().getAppId())
                        .prepareAndSendJMSMessage(event);
            } 
        }
        return statusCode;
	}

	public Lock acquireLock(Integer submissionId, Integer contractId, String type, String userId) {
		Lock lock = null;
		try {
			lock = LockDAO.acquireLock(submissionId, contractId, type, userId);
		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "acquireLock",
			"Unable to acquire lock.");
			throw ExceptionHandlerUtility.wrap(e);
		}
		return lock;
	}

	public Lock acquireLock(Lockable submissionCase, String userId) {
		return acquireLock(submissionCase.getSubmissionId(), submissionCase.getContractId(), getType(submissionCase), userId);
	}

	public boolean releaseLock(Lock lock) {
		boolean success = false;
		try {
			success = LockDAO.releaseLock(lock);
		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "releaseLock",
			"Unable to release lock.");
			throw ExceptionHandlerUtility.wrap(e);
		}
		return success;
	}

	public boolean releaseLock(Lockable submissionCase) {
		return releaseLock(submissionCase.getLock());
	}

	public Lock checkLock(Lockable submissionCase) {
		return checkLock(submissionCase,false);
	}

	public Lock checkLock(Lockable submissionCase, boolean retrieveUserName) {
		Lock lock = null;
		try {
			lock = LockDAO.checkLock(submissionCase.getSubmissionId(),submissionCase.getContractId(),getType(submissionCase),retrieveUserName);
		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "checkLock",
			"Unable to check lock.");
			throw ExceptionHandlerUtility.wrap(e);
		}
		return lock;
	}

	public Lock refreshLock(Integer submissionNumber, Integer contractId, String type, String userId) {
		Lock lock = new Lock();
		lock.setSubmissionId(submissionNumber);
		lock.setContractId(contractId);
		lock.setSubmissionCaseType(type);
		lock.setUserId(userId);
		Lock refreshedLock = null;
		try {
			refreshedLock = LockDAO.refreshLock(lock);
		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "refreshLock",
			"Unable to refresh lock.");
			throw ExceptionHandlerUtility.wrap(e);
		}
		return refreshedLock;
	}

	public Lock refreshLock(Lockable submissionCase) {
		Lock refreshedLock = null;
		try {
			refreshedLock = LockDAO.refreshLock(submissionCase.getLock());
		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "refreshLock",
			"Unable to refresh lock.");
			throw ExceptionHandlerUtility.wrap(e);
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

	public boolean isParticipantCountTooBigForEdit(int submissionId, int contractId) {
		boolean isParticipantCountTooBig = false;
		try {
			isParticipantCountTooBig = SubmissionDAO.isParticipantCountToBigForEdit(submissionId, contractId);
		} catch (SystemException e) {
			SystemException se = new SystemException(e, this.getClass().getName(), "isParticipantCountToBigEdit",
					"Unchecked exception occurred. Input Paramereters are "+
					"submissionId: " + submissionId + ", contractId: " + contractId);
			throw ExceptionHandlerUtility.wrap(e);
		}
		return isParticipantCountTooBig;
	}

    public Date getLastSubmissionDate(int contractId) {
        Date lastSubmission = null;
        try {
            lastSubmission = SubmissionDAO.getLastSubmissionDate(contractId);
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        } catch (RuntimeException e) {
            SystemException se = new SystemException(e, this.getClass()
                    .getName(), "getLastSubmissionDate",
                    "Unchecked exception occurred. Input Paramereters are "
                            + "contractId:" + contractId);
            throw ExceptionHandlerUtility.wrap(se);
        }
        return lastSubmission;
    }
    
    public String getApplicationCode(Integer submissionId) {
        
        String applicationCode = null;
        try {
            DataSource sdbDataSource = BaseDatabaseDAO.getDataSource(
                    BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
            applicationCode = SubmissionDAO.getApplicationCode(
                    sdbDataSource,
                    submissionId);
        } catch (DAOException e) {
            throw ExceptionHandlerUtility.wrap(BaseDatabaseDAO
                    .handleDAOException(e, SubmissionServiceBean.class
                            .getName(), "getApplicationId",
                            "Failed to perform operation for submission ID:"
                                    + submissionId ));
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
        return applicationCode;
        
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
	public Pair getNextCensusSubmissionItemInError(
			Integer contractId, Integer submissionId, Integer sourceRecordNo) {

		ReportCriteria criteria = new ReportCriteria(null);
		criteria.addFilter(SubmissionHistoryReportData.FILTER_CONTRACT_NO,
				contractId);
		criteria.addFilter(CensusSubmissionReportData.FILTER_SUBMISSION_ID,
				submissionId);
		criteria.insertSort(CensusSubmissionReportData.SORT_RECORD_NUMBER,
				ReportSort.ASC_DIRECTION);

		Pair<CensusSubmissionItem, Integer> result = null;

		try {
			CensusSubmissionItem foundItem = null;

			List<CensusSubmissionItem> censusSubmissions = CensusSubmissionDAO
					.getCensusSubmissionsInError(criteria);
			if (censusSubmissions.size() > 0) {
				for (CensusSubmissionItem item : censusSubmissions) {
					/*
					 * Find the first submission item with greater source record
					 * number.
					 */
					if (item.getSourceRecordNo().compareTo(sourceRecordNo) > 0) {
						foundItem = item;
						break;
					}
				}
				if (foundItem == null) {
					/*
					 * If there is no other submission item with greater source
					 * record number, return the first one.
					 */
					foundItem = censusSubmissions.get(0);
					if (foundItem.getSourceRecordNo().equals(sourceRecordNo)) {
						/*
						 * If we loop back to the originating record (i.e. it's
						 * the only record in file), we return null.
						 */
						foundItem = null;
					}
				}

				if (foundItem != null) {
                    // populate the employerDesignatedId based on the sort option
                    String contractSortOptionCode = getParticipantSortOption(contractId);
                    foundItem.setEmpId(contractSortOptionCode);
                    
					result = new Pair<CensusSubmissionItem, Integer>(foundItem,
							censusSubmissions.size());
				}
			}
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		} catch (RuntimeException e) {
			SystemException se = new SystemException(e, this.getClass()
					.getName(), "getNextCensusSubmissionItemInError",
					"Unchecked exception occurred. Input Paramereter is "
							+ "contractId [" + contractId + "] submissionId ["
							+ submissionId + "] sourceRecordNo ["
							+ sourceRecordNo + "]");
			throw ExceptionHandlerUtility.wrap(se);
		}
		return result;
	}

	/**
	 * Obtains the number of census submission records that are still in error.
	 *
	 * @param contractId
	 * @param submissionId
	 * @return
	 */
	public Integer getNumberOfCensusSubmissionItemsInError(Integer contractId,
			Integer submissionId) {

		ReportCriteria criteria = new ReportCriteria(null);
		criteria.addFilter(SubmissionHistoryReportData.FILTER_CONTRACT_NO,
				contractId);
		criteria.addFilter(CensusSubmissionReportData.FILTER_SUBMISSION_ID,
				submissionId);
		criteria.insertSort(CensusSubmissionReportData.SORT_RECORD_NUMBER,
				ReportSort.ASC_DIRECTION);

		try {
			Integer result = CensusSubmissionDAO
					.getNumberOfCensusSubmissionsInError(criteria);
			return result;
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

    /**
	 * Retrieves and validates a census submission item. This can potentially
	 * lead to an update to the error condition string and the process status
	 * code for both the submission case record and the employee census record.
	 *
	 * @param contractId
	 * @param submissionId
     * @param employerDesignatedId
	 * @param sequenceNumber
	 * @return
	 * @throws SystemException
	 */
    public SubmissionValidationBundle retrieveAndValidateCensusSubmission(
			Integer contractId, Integer submissionId,
			String employerDesignatedId, Integer sequenceNumber) {

		try {
			DataSource csdbDataSource = BaseDatabaseDAO
					.getDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
			DataSource sdbDataSource = BaseDatabaseDAO
					.getDataSource(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
            DataSource vfDataSource = BaseDatabaseDAO
                    .getDataSource(BaseDatabaseDAO.VIEW_FUNDS_DATA_SOURCE_NAME);

            EmployeeData submitted = EmployeeDataDAO.getSTPEmployeeData(
                    sdbDataSource, submissionId, contractId,
                    sequenceNumber, getParticipantSortOption(contractId));
			EmployeeData online = EmployeeDataDAO.getOnlineEmployeeData(
					csdbDataSource, contractId, submitted.getSsn(), submitted.getEmployeeNumber());
			
			Integer numberOfRecordsInError = getNumberOfCensusSubmissionItemsInError(
					submitted.getContractId(), submitted.getSubmissionId());

			if (SubmissionConstants.Census.PROCESS_STATUS_CODE_CANCELLED
					.equals(submitted.getProcessStatusCode())
					|| SubmissionConstants.Census.PROCESS_STATUS_CODE_COMPLETE
							.equals(submitted.getProcessStatusCode())) {
				/*
				 * If the item is already fixed or cancelled, we return an empty
				 * error list.
				 */
				return new SubmissionValidationBundle<EmployeeData, EmployeeData, EmployeeValidationErrors>(
						submitted, online, new EmployeeValidationErrors(),
						numberOfRecordsInError);
			}

			EmployeeValidationErrors errors = submittedEmployeeDataValidator.validate(submitted,
					online, csdbDataSource, vfDataSource, sdbDataSource);
			
			//remove error for apollo availability since this is only for empluyee snapshot
			errors.removeError(Property.OPT_OUT_IND, EmployeeValidationErrorCode.AeeApolloAvailability);
			errors.removeError(Property.BEFORE_TAX_DEFERRAL_PERCENTAGE, EmployeeValidationErrorCode.Ae90DaysOptOutBeforeTaxDefPer);
			errors.removeError(Property.BEFORE_TAX_FLAT_DOLLAR_DEFERRAL, EmployeeValidationErrorCode.Ae90DaysOptOutBeforeTaxDef);
			errors.removeError(Property.DESIGNATED_ROTH_DEFERRAL, EmployeeValidationErrorCode.Ae90DaysOptOutDesignatedRothDefAmt);
			errors.removeError(Property.DESIGNATED_ROTH_DEFERRAL_PERCENTAGE, EmployeeValidationErrorCode.Ae90DaysOptOutDesignatedRothDefPer);
			
			

			return new SubmissionValidationBundle<EmployeeData, EmployeeData, EmployeeValidationErrors>(
					submitted, online, errors, numberOfRecordsInError);

		} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(BaseDatabaseDAO
					.handleDAOException(e, SubmissionServiceBean.class
							.getName(), "retrieveAndValidateCensusSubmission",
							"Failed to perform operation for contract ID:"
									+ contractId + " submissionId:"
									+ submissionId + " sequenceNumber:"
									+ sequenceNumber));
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
    
    /**
     * This method is to update the duplicate submission employerProvidedEmailAddress as blank.
     * @param contractId
     * @param submissionId
     * @param formSequenceNumber
     * @param employerProvidedEmailAddress
     * @param userId
     */

	public void deleteDuplicateEmailAddressesOnFile(Integer contractId, Integer submissionId, Integer formSequenceNumber,
			String employerProvidedEmailAddress, String userId) {
		try {

			DataSource sdbDataSource = BaseDatabaseDAO
					.getDataSource(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

			CensusSubmissionDAO.deleteDuplicateEmailAddressesOnFile(sdbDataSource,
					contractId, submissionId, formSequenceNumber,
					employerProvidedEmailAddress, userId);

		} catch (DAOException e) {
			throw ExceptionHandlerUtility
					.wrap(BaseDatabaseDAO
							.handleDAOException(e, SubmissionServiceBean.class
									.getName(), "updateSubmissionEmailAddress",
									"Failed to perform operation of updating the email id: "));
		}
	}
	
	/**
	 * Validates and saves a census submission. Regardless of whether there is
	 * any validation error, the record on Submission database is always
	 * updated. The record on CSDB will only be updated if the record is clean.
	 *
	 * @param submittedData
	 * @param lastUpdatedUserId
	 * @param lastUpdatedUserProfileId
	 * @return
	 * @throws ApplicationException 
	 * @throws SystemException
	 */
	public SubmissionValidationBundle validateAndSaveCensusSubmissionItem(
			EmployeeData submittedData, String lastUpdatedUserId,
			Long lastUpdatedUserProfileId, boolean ignoreWarnings, boolean ignoreSimilarSsn, 
			boolean ignoreEmployeeIdRules) throws ApplicationException {

		try {
			/*
			 * Retrieve online data again and validate.
			 */
			DataSource csdbDataSource = BaseDatabaseDAO
					.getDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);

            DataSource vfDataSource = BaseDatabaseDAO
                    .getDataSource(BaseDatabaseDAO.VIEW_FUNDS_DATA_SOURCE_NAME);
            
            DataSource sdbDataSource = BaseDatabaseDAO
                    .getDataSource(BaseDatabaseDAO.STP_DATA_SOURCE_NAME);

            EmployeeData online = EmployeeDataDAO.getOnlineEmployeeData(
					csdbDataSource, submittedData.getContractId(),
                    submittedData.getSsn(),
                    submittedData.getEmployeeNumber());

			EmployeeValidationErrors errors = null;
			if (ignoreSimilarSsn) {
				if (ignoreEmployeeIdRules) {
					errors = submittedEmployeeDataValidator.validate(submittedData, online,
							csdbDataSource, vfDataSource, sdbDataSource, SimilarSSNRule.class, 
							EmployeeIdRules.class);
				} else {
					errors = submittedEmployeeDataValidator.validate(submittedData, online,
							csdbDataSource, vfDataSource, sdbDataSource, SimilarSSNRule.class);
				}
			} else {
				if (ignoreEmployeeIdRules) {
					errors = submittedEmployeeDataValidator.validate(submittedData, online,
							csdbDataSource, vfDataSource, sdbDataSource, EmployeeIdRules.class);
				} else {
					errors = submittedEmployeeDataValidator.validate(submittedData, online,
							csdbDataSource, vfDataSource, sdbDataSource);
				}
			}

			boolean persistData = true;
			
			/*
			 * Don't persist any data if there are only warnings and the
			 * ignoreWarnings override is false.
			 */
			if (! errors.hasError()) {
				if (errors.hasWarning()) {
					if (!ignoreWarnings) {
						persistData = false;
					}
				}
			}

			if (persistData) {

				/*
				 * First, we save the submitted data into SDB.
				 */
				EmployeeDataDAO.updateSTPEmployeeData(sdbDataSource,
						submittedData, lastUpdatedUserId);

				updateProcessStatusAndErrorCondString(submittedData, errors,
						lastUpdatedUserId, lastUpdatedUserProfileId);
				
				// STP EC Triggers
				EligibilityDataValidator eligibilityDataValidator = new EligibilityDataValidator();
				
				if(online != null && online.getProfileId() != null) {
					
					String employmentStatus = eligibilityDataValidator
					.getEmploymentStatusWithGreatestEffecDate(online.getProfileId().longValue(),submittedData
							.getContractId());
					online.setEmploymentCurrentStatus(employmentStatus);
				}

				if (! errors.hasError()) {
                	// for diferral processing we need to know what was submitted(prior to merge with csdb)
                	boolean rothValuesSubmitted = false;
                	boolean beforeTaxValuesSubmitted = false;
                	if ((submittedData.getBeforeTaxDeferralPercentage() != null) ||
                	    (submittedData.getBeforeTaxFlatDollarDeferral() != null)) {
                		beforeTaxValuesSubmitted = true;
                	}
                	if ((submittedData.getDesignatedRothDeferralAmt() != null) ||
                		(submittedData.getDesignatedRothDeferralPct() != null)) {
                		rothValuesSubmitted = true;
                	}					
					
					EmployeeData mergedData = EmployeeDataValidatorHelper.mergeEmployeeData(
					        submittedData,
					        online,
					        Constants.EMPLOYEE_ID_SORT_OPTION_CODE.equals(
					                getParticipantSortOption(submittedData.getContractId())),
					        DEFERRAL_TYPE_DOLLAR.equals(
					                getDeferralType(submittedData.getContractId())));

                    // Override the submitted statusEffectiveDate with hireDate (for submitted
                    // Active status with null date)
                    mergedData = EmployeeDataValidatorHelper
                            .overrideEmploymentStatusEffectiveDateWithHireDate(mergedData,
                                    online, errors);

					if (logger.isDebugEnabled()) {
						logger.debug("No validation error, update CSDB with:\n"
								+ mergedData);
					}

					String applicationCode = SubmissionDAO.getApplicationCode(
							sdbDataSource, submittedData.getSubmissionId());

                    mergedData.setSourceChannelCode(applicationCode);

                    // EC2 - CR14 setAutoPlanEligibleIndicator
					this.setAutoPlanEligibleIndicator(mergedData);
					
					EmployeeDataDAO.updateOnlineEmployeeData(csdbDataSource,
							mergedData, lastUpdatedUserProfileId);
					
					
                	int userProfileId = 0;
                	
                	if (UserIdType.PAY.equals(mergedData.getUserTypeCode()) || 
                		UserIdType.CAR.equals(mergedData.getUserTypeCode())) { // see setUserInfo
                		userProfileId = SUBMISSION_SYSTEM_ADMIN_ID; 
                	} else {
                		if (StringUtils.isEmpty(mergedData.getUserProfileId())) {
                			userProfileId = SUBMISSION_SYSTEM_ADMIN_ID;
                		} else {
                			userProfileId = Integer.parseInt(mergedData.getUserProfileId());
                		}
                	}
                	
                	// Census Error Correction EC triggers
					
					EmployeeData csdbEmployeeData = EmployeeDataDAO.getOnlineEmployeeData(
							csdbDataSource, submittedData.getContractId(),
		                    submittedData.getSsn(),
		                    submittedData.getEmployeeNumber());
					
					triggerEligibilityCalculation(csdbEmployeeData.getProfileId(),
							submittedData, online, String.valueOf(userProfileId));
					
                	
	                DeferralProcessing.passiveDeferralProcessing(mergedData, rothValuesSubmitted,
	                          beforeTaxValuesSubmitted, userProfileId,
	                          SOURCE_CENSUS_FILE, null); 
				}
			}

			Integer numberOfRecordsInError = getNumberOfCensusSubmissionItemsInError(
					submittedData.getContractId(), submittedData
							.getSubmissionId());

			return new SubmissionValidationBundle<EmployeeData, EmployeeData, EmployeeValidationErrors>(
					submittedData, online, errors, numberOfRecordsInError);

		} catch (DAOException e) {
			throw ExceptionHandlerUtility.wrap(BaseDatabaseDAO
					.handleDAOException(e, SubmissionServiceBean.class
							.getName(), "validateAndSaveCensusSubmissionItem",
							"Failed to perform operation for submitted data: "
									+ submittedData));
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}

	/**
	 * Update the error cond string and process status code for the submission
	 * item.
	 *
	 * @param submitted
	 * @param errors
	 * @param lastUpdatedUserId
	 * @throws SystemException
	 */
	private void updateProcessStatusAndErrorCondString(EmployeeData submitted,
			EmployeeValidationErrors errors, String lastUpdatedUserId, Long lastUpdatedUserpProfileId) {

		String newProcessStatusCode = submitted.getProcessStatusCode();
		if (! errors.hasError()) {
			newProcessStatusCode = SubmissionConstants.Census.PROCESS_STATUS_CODE_COMPLETE;
		}

		try {
			Integer contractId = submitted.getContractId();
			Integer submissionId = submitted.getSubmissionId();
			Integer sequenceNumber = submitted.getSequenceNumber();

			String errorCondString = EmployeeDataValidatorHelper
					.getErrorCondString(errors);

			CensusSubmissionDAO.updateEmployeeCensusProcessStatusCodeAndErrorCondString(
							contractId, submissionId, sequenceNumber,
							lastUpdatedUserId, newProcessStatusCode,
							errorCondString, getParticipantSortOption(contractId));

			if (SubmissionConstants.Census.PROCESS_STATUS_CODE_COMPLETE
					.equals(newProcessStatusCode)) {

				/*
				 * We recalculate the parent submission case process status code
				 * only if the errors are cleared.
				 */

				String newCode = recalculateCensusSubmissionCaseProcessStatus(submitted
                        .getContractId(), submitted.getSubmissionId(), lastUpdatedUserpProfileId.toString());

				SubmissionDAO
						.updateSubmissionCaseProcessStatus(
								newCode,
								lastUpdatedUserId,
								submitted.getSubmissionId(),
								submitted.getContractId(),
								new String[] {
										SubmissionConstants.SUBMISSION_CASE_TYPE_ADDRESS,
										SubmissionConstants.SUBMISSION_CASE_TYPE_CENSUS });
			}
		} catch (SystemException e) {
			throw ExceptionHandlerUtility.wrap(e);
		}
	}
	
	
	
	/**
	 * This method verifies the submitted employee data after census error 
	 * correction and check for the eligibility calculation triggers.
	 *
	 * @param submittedEmployeeData
	 * @param csdbEmployeeData
	 * @throws SystemException
	 * @throws ApplicationException 
	 */
	private void triggerEligibilityCalculation(Long submittedEmployeeId,
			EmployeeData submittedEmployeeData, EmployeeData csdbEmployeeData,
			String userProfileId) throws SystemException, ApplicationException {
		
		
		// STP EC Triggers
		EligibilityDataValidator eligibilityDataValidator = new EligibilityDataValidator();
		
        boolean deferralMoneyType = false;
        
        boolean eligibilityCalculation = false;
        
        // Validate employee data for triggering plan entry date calculation
		boolean pedCalculation = eligibilityDataValidator
				.validatePEDCalcTrigger(submittedEmployeeData);
        
        long profileId = 0;
        
        if(submittedEmployeeData.getProfileId() != null) {
        	profileId = submittedEmployeeData.getProfileId().longValue();
        }else if (csdbEmployeeData != null && csdbEmployeeData.getProfileId() != null) {
        	profileId = csdbEmployeeData.getProfileId().longValue();
        } else {
        	profileId = submittedEmployeeId.longValue();
        }
        
        
        
        	// Validate employee data for triggering eligibility calculation
            eligibilityCalculation = eligibilityDataValidator
					.validateEligibilityCalcTrigger(profileId,submittedEmployeeData, 
							csdbEmployeeData);
            
            // Check whether the contract has EEDEF money type
            if(!pedCalculation) {             	
            	
            	if (submittedEmployeeData.getEligibilityDate() != null) {
					deferralMoneyType = eligibilityDataValidator
							.checkForEEDEFMoneyType(submittedEmployeeData
									.getContractId());
				}
            }
        
        
        // Setting the Provided Eligibility Date Indicator
		if (deferralMoneyType || pedCalculation) {
			eligibilityDataValidator
			.setProvidedEligibilityDateIndicator(profileId,
					submittedEmployeeData.getContractId(),
					submittedEmployeeData.getEligibilityDate(),
					userProfileId,
					submittedEmployeeData.getUserTypeCode(),
					SOURCE_CENSUS_FILE);
		}
                
        if (eligibilityCalculation) {
        	
        	// Triggering eligibility calculation
        	eligibilityDataValidator.triggerEligibilityCalculation(
        			EligibilityRequestConstants.ELIGIBILITY_CALCULATION, 
        			submittedEmployeeData.getContractId(), 
        			(int)profileId, 
        			userProfileId, 
        			submittedEmployeeData.getUserTypeCode(),SOURCE_CENSUS_FILE);
        	
        } 
        
        if (pedCalculation) {
        	// Triggering plan entry date calculation
        	eligibilityDataValidator.triggerEligibilityCalculation(
        			EligibilityRequestConstants.PED_ONLY_CALCULATION, 
        			submittedEmployeeData.getContractId(), 
        			(int)profileId, 
        			userProfileId, 
        			submittedEmployeeData.getUserTypeCode(),SOURCE_CENSUS_FILE);
       }
        
        
	
	}

    /**
	 * Method used to set AutoPlanEligibleIndicator as true when Contract
	 * Service Feature Code equal to AE [EZStart] and Contract Service Feature
	 * Code equal to EC as EEDEF Money Type and Employee Contract Plan Eligible
	 * indicator = blank and Employee Contract Eligibility Date not = blank
	 * 
	 * @param csdbDataSource -
	 *            CSDB DataSource
	 * @param employeeData -
	 *            Employee Information
	 * @throws SystemException
	 * @throws ApplicationException
	 */
    private void setAutoPlanEligibleIndicator(EmployeeData employeeData) throws SystemException,
			ApplicationException {
    	
    	int contractId = employeeData.getContractId();
    	// Get EzkServiceFeature value
    	String ezkServiceFeature = EligibilityDataHelper.getEzkServiceFeature(null, contractId);
		String planEligibleIndicator = employeeData.getEligibilityIndicator();
		Date eligibilityDate = employeeData.getEligibilityDate();

		if (StringUtils.equals(EligibilityRequestConstants.YES,	ezkServiceFeature)
				&& StringUtils.isBlank(planEligibleIndicator)
				&& eligibilityDate != null) {
			// Set AutoPlanEligibleIndicator and  EligibilityIndicator
			employeeData.setEligibilityIndicator(EligibilityRequestConstants.YES);
			employeeData.setAutoPlanEligibleIndicator(true);
		}
    }
    
    /**Method used to get the Original creator of the given submission id.
     * @param submissionId
     * @return
     */
    public String getSubmissionCreatorId(Integer submissionId) {
        String originator = null;
        try {
            DataSource sdbDataSource = BaseDatabaseDAO.getDataSource(
                    BaseDatabaseDAO.STP_DATA_SOURCE_NAME);
            originator = SubmissionDAO.getSubmissionCreatorId(sdbDataSource,submissionId);
        } catch (DAOException e) {
            throw ExceptionHandlerUtility.wrap(BaseDatabaseDAO
                    .handleDAOException(e, SubmissionServiceBean.class
                            .getName(), "getApplicationId",
                            "Failed to perform operation for submission ID:"
                                    + submissionId ));
        } catch (SystemException e) {
            throw ExceptionHandlerUtility.wrap(e);
        }
        return originator;
    }
    
    
    
	/**
	 * getSessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}
	/**
	 * setSessionContext
	 */
	public void setSessionContext(javax.ejb.SessionContext ctx) {
		mySessionCtx = ctx;
		submittedEmployeeDataValidator = new SubmittedEmployeeDataValidator();
	}
	/**
	 * ejbActivate
	 */
	public void ejbActivate() {
	}
	/**
	 * ejbCreate
	 */
	public void ejbCreate() throws javax.ejb.CreateException {
	}
	/**
	 * ejbPassivate
	 */
	public void ejbPassivate() {
	}
	/**
	 * ejbRemove
	 */
	public void ejbRemove() {
	}

	//CL89281 fix  	
    /**Method to determines whether the DB table has process_status_code in draft or not.
     * @param submissionId
     * @param contractId
     */
	public boolean isSubmissionInDraft(int submissionId, int contractId) {
		boolean submissionDraft = false;
		try {
			submissionDraft = SubmissionDAO.isSubmissionInDraft(submissionId, contractId);
		} catch (Exception e) {
			SystemException se = new SystemException(e,this.getClass().getName(), "isSubmissionInDraft", "Exception while deleting submission" );
			throw ExceptionHandlerUtility.wrap(se);
		} 
		return submissionDraft;
	}
	
}