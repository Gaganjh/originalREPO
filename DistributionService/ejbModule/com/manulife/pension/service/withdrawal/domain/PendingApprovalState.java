package com.manulife.pension.service.withdrawal.domain;

import org.apache.commons.lang.BooleanUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.helper.ActivityHistoryHelper;
import com.manulife.pension.service.withdrawal.log.WithdrawalEvent;
import com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalActivitySummary;

/**
 * PendingApprovalState contains the implementation of the pending approval state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public final class PendingApprovalState extends PendingState {

    private static final Logger logger = Logger.getLogger(PendingApprovalState.class);

    /**
     * {@inheritDoc}
     */
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.PENDING_APPROVAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(final Withdrawal withdrawal) throws DistributionServiceException {

        // Validate
        withdrawal.validateForSavePendingApproval();

        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, this.getWithdrawalStateEnum());
            
            // Have to get the oldWithdrawal before we call do save. Must not call
            // updateActivityHistory until after 'doSave' since there is a lot of logic that gets
            // applied to show/suppress rules during prepareforSave.
            final Withdrawal oldWithdrawal = ActivityHistoryHelper.getOldWithdrawal(withdrawal);

            withdrawal.doSave();

            ActivityHistoryHelper.updateActivityHistory(withdrawal, oldWithdrawal);

        } // fi
    }

    /**
     * {@inheritDoc}
     */
    public void deny(final Withdrawal withdrawal) throws DistributionServiceException {

        // Deny pending approval specific validations
        withdrawal.validateForDenyPendingApproval();

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
     */
    void transitionToState(final Withdrawal withdrawal, final WithdrawalStateEnum newState) {

        switch (newState) {
            case PENDING_APPROVAL:
                // I'm already in this state. This is valid. Do nothing.
                break;
            case PENDING_REVIEW:
            	// This only happens for a special case:
            	// Only a BGA CAR on a BGA Contract can pull withdrawals back from Pending Approval
            	// status to Pending Review status.            	
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
            //PAR changes, Save At Risk details to DB
            try {
				withdrawal.updateAtRiskAddress();
			} catch (SystemException e) {
				throw new DistributionServiceDaoException( " Exception at updateAtRiskAddress() for withdrawal request :"+withdrawal, e);
			}

            ActivityHistoryHelper.updateActivityHistory(withdrawal, oldWithdrawal);
            ActivityHistoryHelper.saveSummary(withdrawal, WithdrawalActivitySummary.ACTION_CODE_APPROVED);

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(),
                    WithdrawalEvent.APPROVE_FROM_POST_DRAFT, BeforeEndState.class, "approve");

            withdrawal.afterEnteringApprovedState();
        } // fi
    }
    
    /**
     * SPECIAL CASE CODED SPECIAL FOR BGA and NAVSMART:
     * NavSmart allows Bundled GA contracts to revert a withdrawal's state from
     * Pending Approval, back to Pending Review.
     * 
     * This is a very simple transition, without validations, etc.
     * 
     * {@inheritDoc}
     */
    @Override
    public void sendForReview(final Withdrawal withdrawal) throws DistributionServiceException {
        // Save
        if (withdrawal.isReadyForDoSave()) {
            transitionToState(withdrawal, WithdrawalStateEnum.PENDING_REVIEW);

            // This indicator is set to skip the reset of state tax percent
            withdrawal.setRestartReview(true);
            
            withdrawal.doSave();

            // Reset the value to false so that the value object wont carry over the value any more
            withdrawal.setRestartReview(false);
            
            ActivityHistoryHelper.saveSummary(withdrawal,
                    WithdrawalActivitySummary.ACTION_CODE_SENT_FOR_REVIEW);

            withdrawal.readWithdrawalRequestForEdit();

            WithdrawalLoggingHelper.log(withdrawal.getWithdrawalRequest(),
                    WithdrawalEvent.SEND_FOR_REVIEW, PendingApprovalState.class, "sendForReview");            
        }
    }
    
    
}
