package com.manulife.pension.service.withdrawal.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.WithdrawalReason;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.distribution.valueobject.Recipient;
import com.manulife.pension.service.environment.valueobject.CodeEqualityPredicate;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.withdrawal.dao.WithdrawalInfoDao;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.log.WithdrawalEvent;
import com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper;
import com.manulife.pension.service.withdrawal.valueobject.ActivityHistory;
import com.manulife.pension.service.withdrawal.valueobject.ParticipantInfo;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestLoan;

/**
 * DraftState contains the implementation of the draft state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public final class DraftState extends BeforeEndState {

    private static final Logger logger = Logger.getLogger(DraftState.class);

    private static final String CLASS_NAME = DraftState.class.getName();

    /**
     * {@inheritDoc}
     */
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.DRAFT;
    }

    /**
     * Default Constructor.
     */
    protected DraftState() {
        super();
    }

    @Override
    public ActivityHistory readActivityHistory(Withdrawal withdrawal)
            throws DistributionServiceException {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws DistributionServiceException
     */
    @Override
    public void save(final Withdrawal withdrawal) throws DistributionServiceException {

        logger.debug("\n\nSave the object that's in draft state\n\n");

        // Validate
        withdrawal.validateForSaveFromDraft();

        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.DRAFT);

            withdrawal.doSave();
        } // fi
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendForReview(final Withdrawal withdrawal) throws DistributionServiceException {

        // Validate
        withdrawal.validateForSendForReview();

        // Save
        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.PENDING_REVIEW);

            withdrawal.doSave();

            ActivityHistoryHelper.saveOriginalValues(withdrawal);
            ActivityHistoryHelper.saveSummary(withdrawal,
                    WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW);

            // don't need to read again if coming from ezk
            if (!StringUtils.equals(WithdrawalRequest.CMA_SITE_CODE_EZK, withdrawal
                    .getWithdrawalRequest().getCmaSiteCode())) {
                withdrawal.readWithdrawalRequestForEdit();
            }

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(),
                    WithdrawalEvent.SEND_FOR_REVIEW, DraftState.class, "sendForReview");

            withdrawal.afterEnteringPendingReviewState();
        } // fi
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendForApproval(final Withdrawal withdrawal) throws DistributionServiceException {

        // Validate
        withdrawal.validateForSendForApproval();

        // Save
        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.PENDING_APPROVAL);

            withdrawal.doSave();

            ActivityHistoryHelper.saveOriginalValues(withdrawal);
            if (withdrawal.getWithdrawalRequest().getContractInfo().getTwoStepApprovalRequired()) {
                ActivityHistoryHelper.saveSummary(withdrawal,
                        WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW);
            }
            ActivityHistoryHelper.saveSummary(withdrawal,
                    WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_APPROVAL);

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(),
                    WithdrawalEvent.SEND_FOR_APPROVAL_FROM_DRAFT, DraftState.class,
                    "sendForApproval");

            withdrawal.afterEnteringPendingApprovalState();
        } // fi
    }

    /**
     * {@inheritDoc}
     */
    void transitionToState(final Withdrawal withdrawal, final WithdrawalStateEnum newState) {

        switch (newState) {
            case DRAFT:
                // I'm already in draft state. This is valid. Do nothing.
                break;

            case PENDING_REVIEW:
                // This flows into the case below (as there is no break).

            case PENDING_APPROVAL:
                withdrawal.setWithdrawalState(WithdrawalStateFactory.getState(newState));
                break;

            default:
                super.transitionToState(withdrawal, newState);
        } // end case
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyDefaultDataForEdit(final Withdrawal withdrawal,
            final WithdrawalRequest defaultVo) throws SystemException {

        WithdrawalRequest withdrawalRequest = withdrawal.getWithdrawalRequest();
        ParticipantInfo participantInfo = defaultVo.getParticipantInfo();
        boolean hasTerminationDate = withdrawalRequest.getTerminationDate() != null;
        boolean hasRetirementDate = withdrawalRequest.getRetirementDate() != null;
        boolean hasDisabilityDate = withdrawalRequest.getDisabilityDate() != null;
        boolean hasReasonCode = StringUtils.isNotEmpty(withdrawalRequest.getReasonCode());
        boolean hasPaymentTo = StringUtils.isNotEmpty(withdrawalRequest.getPaymentTo());

        withdrawalRequest.setParticipantInfo(defaultVo.getParticipantInfo());
        withdrawalRequest.setContractInfo(defaultVo.getContractInfo());
        withdrawalRequest.setRequestDate(defaultVo.getRequestDate());
        withdrawalRequest.setExpirationDate(defaultVo.getExpirationDate());

        // Refresh until End
        for (Recipient withdrawalRequestRecipient : withdrawalRequest
                .getRecipients()) {
            withdrawalRequestRecipient.setFirstName(defaultVo.getFirstName());
            withdrawalRequestRecipient.setLastName(defaultVo.getLastName());
        } // end for

        // if the withdrawal reason they chose is no longer valid, then redefault the value
        Collection<DeCodeVO> withdrawalReasons = WithdrawalInfoDao.getParticipantWithdrawalReasons(
                defaultVo.getParticipantInfo().getContractStatusCode(), defaultVo
                        .getParticipantInfo().getParticipantStatusCode());
        if (hasReasonCode
                && !CollectionUtils.exists(withdrawalReasons, new CodeEqualityPredicate(
                        withdrawalRequest.getReasonCode()))) {
            // re-default it
            String defaulReasonCode = defaultVo.getReasonCode();
            withdrawalRequest.setReasonCode(defaulReasonCode);

            // if its termination status and termiated value is not populated, and
            // there is a termination value, use it. ( same for retirement and disability)
            if (StringUtils.isNotEmpty(defaulReasonCode)) {
                if (WithdrawalReason.isTermination(defaulReasonCode) && !hasTerminationDate) {
                    withdrawalRequest.setTerminationDate(defaultVo.getTerminationDate());
                } else if (defaulReasonCode.equals(WithdrawalReason.RETIREMENT)
                        && !hasRetirementDate) {
                    withdrawalRequest.setRetirementDate(defaultVo.getRetirementDate());
                } else if (defaulReasonCode.equals(WithdrawalReason.DISABILITY)
                        && !hasDisabilityDate) {
                    withdrawalRequest.setDisabilityDate(defaultVo.getDisabilityDate());
                }
            }
        }

        withdrawalRequest.setMostRecentPriorContributionDate(defaultVo
                .getMostRecentPriorContributionDate());
        /* all disbursments to plan trustee */
        if (participantInfo != null
                && participantInfo.getChequePayableToCode().equals(
                        WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE) && !hasPaymentTo) {
            withdrawalRequest.setPaymentTo(WithdrawalRequest.PAYMENT_TO_PLAN_TRUSTEE_CODE);
        }

        withdrawalRequest.setFirstName(defaultVo.getFirstName());
        withdrawalRequest.setLastName(defaultVo.getLastName());
        withdrawalRequest.setParticipantSSN(defaultVo.getParticipantSSN());
        withdrawalRequest.setContractName(defaultVo.getContractName());
        withdrawalRequest.setParticipantAddress(defaultVo.getParticipantAddress());

        // update new values form csdb loans
        for (WithdrawalRequestLoan csdbLoan : defaultVo.getLoans()) {
            boolean foundLoan = false;
            for (WithdrawalRequestLoan sdbLoan : withdrawalRequest.getLoans()) {
                if (sdbLoan.getLoanNo().equals(csdbLoan.getLoanNo())) {
                    sdbLoan.setOutstandingLoanAmount(csdbLoan.getOutstandingLoanAmount());
                    foundLoan = true;
                    break;
                }
            }
            if (!foundLoan) {
                withdrawalRequest.getLoans().add(csdbLoan);
            }
        }
        // remove any loans that are not in csdb loans
        Collection<WithdrawalRequestLoan> newLoans = new ArrayList<WithdrawalRequestLoan>();
        for (WithdrawalRequestLoan sdbLoan : withdrawalRequest.getLoans()) {
            for (WithdrawalRequestLoan csdbLoan : defaultVo.getLoans()) {
                if (csdbLoan.getLoanNo().equals(sdbLoan.getLoanNo())) {
                    newLoans.add(sdbLoan);
                }
            }
        }
        withdrawalRequest.setLoans(newLoans);

        updateMoneyTypesWithDefaultData(withdrawalRequest, defaultVo);

        updateMoneyTypeNames(withdrawalRequest, defaultVo);
    }

    /**
     * {@inheritDoc}
     */
    public void approve(final Withdrawal withdrawal) throws DistributionServiceException {

        withdrawal.validateForApproval();

        // Ensure that the user has confirmed the legalese, if not, we just return back to the web
        // tier.
        if (BooleanUtils.isNotTrue(withdrawal.getWithdrawalRequest().getIsLegaleseConfirmed())) {
            logger.debug("Need to confirm legalese...");
            return;
        } // fi

        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.APPROVED);

            withdrawal.beforeEnteringApprovedState();

            withdrawal.doSave();
            withdrawal.doSaveWithdrawalLegaleseInfo();

            ActivityHistoryHelper.saveOriginalValues(withdrawal);
            if (withdrawal.getWithdrawalRequest().getContractInfo().getTwoStepApprovalRequired()) {
                ActivityHistoryHelper.saveSummary(withdrawal,
                        WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW);
            }
            ActivityHistoryHelper.saveSummary(withdrawal,
                    WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_APPROVAL);
            ActivityHistoryHelper.saveSummary(withdrawal, WithdrawalActivitySummary.ACTION_CODE_APPROVED);

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(),
                    WithdrawalEvent.APPROVE_FROM_DRAFT, BeforeEndState.class, "approve");

            withdrawal.afterEnteringApprovedState();

        } // fi
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final Withdrawal withdrawal) throws DistributionServiceException {

        if (withdrawal.getWithdrawalRequest().getSubmissionId() == null) {
            throw new RuntimeException("There should be no delete for new requests.");
        } // fi

        // Check if request has expired
        withdrawal.getWithdrawalRequest().removeMessages();
        withdrawal.validateRequestTimestampHasNotChangedOrExpiryTimePassed();

        // We only perform the delete if no errors were found
        if (CollectionUtils.isEmpty(withdrawal.getWithdrawalRequest().getErrorCodes())) {

            transitionToState(withdrawal, WithdrawalStateEnum.DELETED);

            withdrawal.doSave();

            ActivityHistoryHelper.saveSummary(withdrawal, WithdrawalActivitySummary.ACTION_CODE_DELETED);

            WithdrawalLoggingHelper.logShort(withdrawal.getWithdrawalRequest().getSubmissionId(),
                    withdrawal.getWithdrawalRequest().getLastUpdatedById(),
                    WithdrawalEvent.DELETE_FROM_DRAFT, DraftState.class, "delete");

            withdrawal.afterEnteringDeletedState();
        }
    }
}
