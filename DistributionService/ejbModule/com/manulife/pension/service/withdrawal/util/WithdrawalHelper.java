package com.manulife.pension.service.withdrawal.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.delegate.TPAServiceDelegate;
import com.manulife.pension.event.Event;
import com.manulife.pension.event.WithdrawalAboutToExpireEvent;
import com.manulife.pension.event.WithdrawalApprovedEvent;
import com.manulife.pension.event.WithdrawalDeletedEvent;
import com.manulife.pension.event.WithdrawalDeniedEvent;
import com.manulife.pension.event.WithdrawalExpiredEvent;
import com.manulife.pension.event.WithdrawalPendingApprovalEvent;
import com.manulife.pension.event.WithdrawalPendingReviewEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.exception.ApplicationException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.tpa.valueobject.TPAFirmInfo;
import com.manulife.pension.service.security.valueobject.ContractPermission;
import com.manulife.pension.service.security.valueobject.UserInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMetaData;
import com.manulife.pension.util.BaseEnvironment;

/**
 * This class provides a location to call common functions that deal with Withdrawals.
 * 
 * @author glennpa
 */
public final class WithdrawalHelper {

    static final Logger logger = Logger.getLogger(WithdrawalHelper.class);

    private static final String CLASS_NAME = WithdrawalHelper.class.getName();

    /**
     * Default Constructor.
     * 
     */
    private WithdrawalHelper() {
        super();
    }

    /**
     * Looks up the contracts that are searchable for the given user.
     * 
     * @param principal The user to look up the searchable contracts for.
     * @param contract The contract, if set, or null if the user is accessing as a TPA.
     * @return List - The list of contract numbers as {@link Integer} objects.
     * @throws SystemException If an exception occurs.
     */
    protected static List<Integer> getSearchableContracts(final Principal principal,
            final Contract contract) throws SystemException {

        if (logger.isDebugEnabled()) {
            logger.debug("getSearchableContracts> Entry.");
        } // fi

        List<Integer> searchableContracts = new ArrayList<Integer>();

        List<Integer> availableContracts = new ArrayList<Integer>();
        if (contract != null) {
            availableContracts.add(contract.getContractNumber());
        } else if (principal.getRole().isTPA()) {
            if (contract != null) {
                availableContracts.add(contract.getContractNumber());
            } else {
                UserInfo userInfo = SecurityServiceDelegate.getInstance().getUserInfo(principal);
                for (TPAFirmInfo firmInfo : userInfo.getTpaFirmsAsCollection()) {
                    availableContracts.addAll(TPAServiceDelegate.getInstance().getContractsByFirm(
                            firmInfo.getId()));
                }
            }
        }
        for (Integer contractNumber : availableContracts) {
            ContractServiceFeature feature;
            try {
                feature = ContractServiceDelegate.getInstance().getContractServiceFeature(
                        contractNumber, ServiceFeatureConstants.IWITHDRAWALS_FEATURE);
            } catch (ApplicationException applicationException) {
                throw new SystemException(applicationException,
                        "failed to get contract service feature");
            }
            if (ContractServiceFeature.internalToBoolean(feature.getValue())) {
                searchableContracts.add(contractNumber);
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getSearchableContracts> Exit.");
        } // fi

        return searchableContracts;
    }

    /**
     * Creates and fires the event for a withdrawal entering the pending approval state.
     * 
     * @param withdrawalRequest The withdrawal request to create the event for.
     */
    public static void fireWithdrawalPendingApprovalEvent(final WithdrawalRequest withdrawalRequest) {
        // Create the withdrawal "Pending Approval" event.
        final WithdrawalPendingApprovalEvent withdrawalPendingApprovalEvent = new WithdrawalPendingApprovalEvent(
                CLASS_NAME, "fireWithdrawalPendingApprovalEvent", withdrawalRequest
                        .getLastUpdatedById(), withdrawalRequest.getSubmissionId(),
                withdrawalRequest.getContractId(), getParticipantProfileId(withdrawalRequest));

     // If the Contract is a bundled contract and the contract TPA Firm has
		// Signing Authority then do not generate this message.
		if (!(isBundledIndicator(withdrawalRequest.getContractId()) && getTpaFirmSigningAuthority(
				withdrawalRequest.getContractId()).isSigningAuthority())) {
			fireEvent(withdrawalPendingApprovalEvent);
		}
    }

    /**
     * Creates and fires the event for a withdrawal entering the pending review state.
     * 
     * @param withdrawalRequest The withdrawal request to create the event for.
     */
    public static void fireWithdrawalPendingReviewEvent(final WithdrawalRequest withdrawalRequest) {
        // Create the withdrawal "Pending Review" event.
        final WithdrawalPendingReviewEvent withdrawalPendingReviewEvent = new WithdrawalPendingReviewEvent(
                CLASS_NAME, "fireWithdrawalPendingReviewEvent", withdrawalRequest
                        .getLastUpdatedById(), withdrawalRequest.getSubmissionId(),
                withdrawalRequest.getContractId(), getParticipantProfileId(withdrawalRequest));

        if(!isBundledIndicator(withdrawalRequest.getContractId())){
        	fireEvent(withdrawalPendingReviewEvent);
        }
    }

    /**
     * Creates and fires the event for a withdrawal entering the approved state.
     * 
     * @param withdrawalRequest The withdrawal request to create the event for.
     */
    public static void fireWithdrawalApprovedEvent(final WithdrawalRequest withdrawalRequest) {
        // Create the withdrawal "Approved" event.
        final WithdrawalApprovedEvent withdrawalApprovedEvent = new WithdrawalApprovedEvent(
                CLASS_NAME, "fireWithdrawalApprovedEvent", withdrawalRequest.getLastUpdatedById(),
                withdrawalRequest.getSubmissionId(), withdrawalRequest.getContractId(),
                getParticipantProfileId(withdrawalRequest));

        fireEvent(withdrawalApprovedEvent);
    }

    /**
     * Creates and fires the event for a withdrawal entering the deleted state.
     * 
     * @param withdrawalRequest The withdrawal request to create the event for.
     */
    public static void fireWithdrawalDeletedEvent(final WithdrawalRequest withdrawalRequest) {
        // Create the withdrawal "Deleted" event.
        final WithdrawalDeletedEvent withdrawalDeletedEvent = new WithdrawalDeletedEvent(
                CLASS_NAME, "fireWithdrawalDeletedEvent", withdrawalRequest.getLastUpdatedById(),
                withdrawalRequest.getSubmissionId(), withdrawalRequest.getContractId(),
                getParticipantProfileId(withdrawalRequest));

        fireEvent(withdrawalDeletedEvent);
    }

    /**
     * Creates and fires the event for a withdrawal entering the denied state.
     * 
     * @param withdrawalRequest The withdrawal request to create the event for.
     */
    public static void fireWithdrawalDeniedEvent(final WithdrawalRequest withdrawalRequest) {
        // Create the withdrawal "Denied" event.
        final WithdrawalDeniedEvent withdrawalDeniedEvent = new WithdrawalDeniedEvent(CLASS_NAME,
                "fireWithdrawalDeniedEvent", withdrawalRequest.getLastUpdatedById(),
                withdrawalRequest.getSubmissionId(), withdrawalRequest.getContractId(),
                getParticipantProfileId(withdrawalRequest));

        fireEvent(withdrawalDeniedEvent);
    }

    /**
     * Creates and fires the event for a withdrawal when a withdrawal request is either pending
     * review or pending approval and the system time is two business days before it's expiration.
     * 
     * @param withdrawalRequestMetaData The {@link WithdrawalRequestMetaData} of the withdrawal
     *            request to create the event for
     * @param initiator The initiator of this request.
     */
    public static void fireWithdrawalAboutToExpireEvent(
            final WithdrawalRequestMetaData withdrawalRequestMetaData, final Integer initiator) {
        // Create the withdrawal "AboutToExpire" event.
        final WithdrawalAboutToExpireEvent withdrawalAboutToExpireEvent = new WithdrawalAboutToExpireEvent(
                CLASS_NAME, "fireWithdrawalAboutToExpireEvent", initiator,
                withdrawalRequestMetaData.getSubmissionId(), withdrawalRequestMetaData
                        .getContractId(), getParticipantProfileId(withdrawalRequestMetaData));

        fireEvent(withdrawalAboutToExpireEvent);
    }

    /**
     * Creates and fires the event for a withdrawal entering the expired state.
     * 
     * @param withdrawalRequestMetaData The {@link WithdrawalRequestMetaData} of the expired
     *            withdrawal.
     * @param initiator The initiator of this request.
     */
    public static void fireWithdrawalExpiredEvent(
            final WithdrawalRequestMetaData withdrawalRequestMetaData, final Integer initiator) {
        // Create the withdrawal "Expired" event.
        final WithdrawalExpiredEvent withdrawalExpiredEvent = new WithdrawalExpiredEvent(
                CLASS_NAME, "fireWithdrawalExpiredEvent", initiator, withdrawalRequestMetaData
                        .getSubmissionId(), withdrawalRequestMetaData.getContractId(),
                getParticipantProfileId(withdrawalRequestMetaData));

        fireEvent(withdrawalExpiredEvent);
    }

    /**
     * Fires the event.
     * 
     * @param event The event to fire.
     */
    private static void fireEvent(final Event event) {
        // Get the event client utility which is used to fire the event.
        final EventClientUtility eventClientUtility = EventClientUtility
                .getInstance(new BaseEnvironment().getAppId());

        // Fire the event.
        try {
            eventClientUtility.prepareAndSendJMSMessage(event);
        } catch (SystemException systemException) {
            throw new RuntimeException(systemException);
        } // end try/catch
    }

    /**
     * Looks up the participant profile ID for the given withdrawal request.
     * 
     * @param withdrawalRequest The withdrawal request.
     * @return Integer - The participant's profile ID.
     */
    private static Integer getParticipantProfileId(final WithdrawalRequest withdrawalRequest) {
        final long participantId = withdrawalRequest.getParticipantId().longValue();
        final Integer contractId = withdrawalRequest.getContractId();
        return getParticipantProfileId(participantId, contractId);
    }

    /**
     * Looks up the participant profile ID for the given withdrawal request meta data.
     * 
     * @param withdrawalRequestMetaData The withdrawal request meta data.
     * @return Integer - The participant's profile ID.
     */
    private static Integer getParticipantProfileId(
            final WithdrawalRequestMetaData withdrawalRequestMetaData) {
        final long participantId = withdrawalRequestMetaData.getParticipantId().longValue();
        final Integer contractId = withdrawalRequestMetaData.getContractId();
        return getParticipantProfileId(participantId, contractId);
    }

    /**
     * Looks up the participant profile ID for the given participant ID and contract ID.
     * 
     * @param participantId The participant ID.
     * @param contractId The contract ID.
     * @return Integer - The participant's profile ID.
     */
    private static Integer getParticipantProfileId(final long participantId,
            final Integer contractId) {
        String participantProfileIdString;
        try {
            participantProfileIdString = EmployeeServiceDelegate.getInstance(
                    new BaseEnvironment().getApplicationId()).getProfileIdByParticipantId(
                    participantId, contractId);
        } catch (SystemException systemException) {
            throw new RuntimeException(systemException);
        } // end try/catch

        return new Integer(participantProfileIdString);
    }
    
    
    /**
	 * Identify the give contract is bundled or non bundled contract.
	 * 
	 * @return the bundledIndicator
	 */
	private static boolean isBundledIndicator(Integer contractId) {
		boolean isBundledIndicator = false;
		try {
			isBundledIndicator = ContractServiceDelegate.getInstance()
					.isBundledGaContract(contractId);
		} catch (SystemException systemException) {
			throw new RuntimeException(systemException);
		}
		return isBundledIndicator;
	}
	
	/**
	 * Method to retrieving the contract permission.
	 * 
	 * @return the ContractPermission
	 */
	private static ContractPermission getTpaFirmSigningAuthority(Integer contractId) {
		
		ContractPermission contractPermission =  null;
		try {
			contractPermission = SecurityServiceDelegate.getInstance().getTpaFirmContractPermission(contractId);
		} catch (SystemException systemException) {
			throw new RuntimeException(systemException);
		}
		return contractPermission;
		
	}

}
