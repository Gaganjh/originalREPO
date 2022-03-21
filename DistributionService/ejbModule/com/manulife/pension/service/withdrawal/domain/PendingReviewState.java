/*
 * PendingReviewState.java,v 1.1 2006/09/25 19:15:28 Paul_Glenn Exp
 * PendingReviewState.java,v
 * Revision 1.1  2006/09/25 19:15:28  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.log.WithdrawalEvent;
import com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * PendingReviewState contains the implementation of the pending review state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public final class PendingReviewState extends PendingState {

    private static final Logger logger = Logger.getLogger(PendingReviewState.class);

    /**
     * {@inheritDoc}
     */
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.PENDING_REVIEW;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final Withdrawal withdrawal) throws DistributionServiceException {

        // Validate
        withdrawal.validateForSavePendingReview();

        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, this.getWithdrawalStateEnum());

            // Have to get the oldWithdrawal before we call do save. Must not call
            // updateActivityHistory until after 'doSave' since there is a lot of logic that gets
            // applied to show/suppress rules during prepareforSave.
            final Withdrawal oldWithdrawal = ActivityHistoryHelper.getOldWithdrawal(withdrawal);

            withdrawal.doSave();

            ActivityHistoryHelper.updateActivityHistory(withdrawal, oldWithdrawal);

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(),
                    WithdrawalEvent.SAVE_FROM_POST_DRAFT, PendingReviewState.class, "save");

        } // fi
    }

    /**
     * {@inheritDoc}
     */
    public void deny(final Withdrawal withdrawal) throws DistributionServiceException {

        // Deny pending approval specific validations
        withdrawal.validateForDenyPendingReview();

        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.DENIED);

            // Have to get the oldWithdrawal before we call do save. Must not call
            // updateActivityHistory until after 'doSave' since there is a lot of logic that gets
            // applied to show/suppress rules during prepareforSave.
            final Withdrawal oldWithdrawal = ActivityHistoryHelper.getOldWithdrawal(withdrawal);

            withdrawal.doSave();

            ActivityHistoryHelper.updateActivityHistory(withdrawal, oldWithdrawal);
            ActivityHistoryHelper.saveSummary(withdrawal, WithdrawalActivitySummary.ACTION_CODE_DENIED);

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(), WithdrawalEvent.DENY,
                    PendingState.class, "deny");

            withdrawal.afterEnteringDeniedState();
        } // fi
    }

    /**
     * {@inheritDoc}
     * 
     * @throws DistributionServiceException
     */
    public void sendForApproval(final Withdrawal withdrawal) throws DistributionServiceException {

        withdrawal.validateForSendForApproval();

        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.PENDING_APPROVAL);

            // Have to get the oldWithdrawal before we call do save. Must not call
            // updateActivityHistory until after 'doSave' since there is a lot of logic that gets
            // applied to show/suppress rules during prepareforSave.
            final Withdrawal oldWithdrawal = ActivityHistoryHelper.getOldWithdrawal(withdrawal);

            withdrawal.doSave();

            ActivityHistoryHelper.updateActivityHistory(withdrawal, oldWithdrawal);
            ActivityHistoryHelper.saveSummary(withdrawal,
                    WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_APPROVAL);

            final WithdrawalRequest withdrawalRequest = withdrawal.getWithdrawalRequest();
            if (BooleanUtils.isTrue(withdrawalRequest.getIgnoreErrors())) {
                // This means that we're forcing the request due to a CSF change.
                // Ensure the system user's ID is set for the logging event.
                withdrawalRequest.setLastUpdatedById((int) withdrawalRequest.getPrincipal()
                        .getProfileId());

                WithdrawalLoggingHelper.log(withdrawalRequest,
                        WithdrawalEvent.SEND_FOR_APPROVAL_VIA_CONRACT_SERVICE_FEATURE_CHANGE,
                        PendingReviewState.class, "sendForApproval");
            } else {
                WithdrawalLoggingHelper.log(withdrawalRequest,
                        WithdrawalEvent.SEND_FOR_APPROVAL_FROM_POST_DRAFT,
                        PendingReviewState.class, "sendForApproval");
            } // fi

            withdrawal.afterEnteringPendingApprovalState();
        } // fi
    }

    /**
     * {@inheritDoc}
     */
    void transitionToState(final Withdrawal withdrawal, final WithdrawalStateEnum newState) {

        switch (newState) {
            case PENDING_REVIEW:
                // I'm already in this state. This is valid. Do nothing.
                break;

            case PENDING_APPROVAL:
                // Pending Approval is valid.

                // Now that it's validated, set it to the new state.
                withdrawal.setWithdrawalState(WithdrawalStateFactory.getState(newState));
                break;

            default:
                super.transitionToState(withdrawal, newState);
        } // end case
    }

    /**
     * {@inheritDoc}
     */
    public void approve(final Withdrawal withdrawal) throws DistributionServiceException {

        withdrawal.validateForApproval();

        // Ensure that the user has confirmed the legalese, if not, we just return back to the web
        // tier.
        if (BooleanUtils.isNotTrue(withdrawal.getWithdrawalRequest().getIsLegaleseConfirmed())) {
            return;
        } // fi
        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.APPROVED);

            withdrawal.beforeEnteringApprovedState();

            // Have to get the oldWithdrawal before we call do save. Must not call
            // updateActivityHistory until after 'doSave' since there is a lot of logic that gets
            // applied to show/suppress rules during prepareforSave.
            final Withdrawal oldWithdrawal = ActivityHistoryHelper.getOldWithdrawal(withdrawal);

            withdrawal.doSave();
            withdrawal.doSaveWithdrawalLegaleseInfo();

            ActivityHistoryHelper.updateActivityHistory(withdrawal, oldWithdrawal);
            ActivityHistoryHelper.saveSummary(withdrawal,
                    WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_APPROVAL);
            ActivityHistoryHelper.saveSummary(withdrawal, WithdrawalActivitySummary.ACTION_CODE_APPROVED);

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(),
                    WithdrawalEvent.APPROVE_FROM_POST_DRAFT, BeforeEndState.class, "approve");

            withdrawal.afterEnteringApprovedState();

        } // fi
    }
}
