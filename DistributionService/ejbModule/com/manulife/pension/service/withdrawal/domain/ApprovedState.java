/*
 * ApprovedState.java,v 1.1 2006/10/10 19:42:57 Paul_Glenn Exp
 * ApprovedState.java,v
 * Revision 1.1  2006/10/10 19:42:57  Paul_Glenn
 * Update withdrawal states.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.log.WithdrawalEvent;
import com.manulife.pension.service.withdrawal.log.WithdrawalLoggingHelper;


/**
 * ApprovedState contains the implementation of the approved state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/10/10 19:42:57
 */
public final class ApprovedState extends EndState {

    /**
     * {@inheritDoc}
     */
    @Override
    public void processApproved(final Withdrawal withdrawal) throws DistributionServiceException {
        transitionToState(withdrawal, WithdrawalStateEnum.READY_FOR_ENTRY);
        withdrawal.doSave();

        WithdrawalLoggingHelper.logShort(withdrawal.getWithdrawalRequest().getSubmissionId(),
                withdrawal.getWithdrawalRequest().getLastUpdatedById(),
                WithdrawalEvent.READY_FOR_ENTRY, ApprovedState.class, "processApproved");

        withdrawal.afterEnteringReadyForEntryState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.APPROVED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void transitionToState(final Withdrawal withdrawal, final WithdrawalStateEnum newState) {

        switch (newState) {
            case READY_FOR_ENTRY:
                withdrawal.setWithdrawalState(WithdrawalStateFactory.getState(newState));
                break;

            default:
                super.transitionToState(withdrawal, newState);
        } // end case
    }
}
