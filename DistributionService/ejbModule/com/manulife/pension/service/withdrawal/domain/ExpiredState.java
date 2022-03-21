/*
 * ExpiredState.java,v 1.1 2006/10/10 19:42:57 Paul_Glenn Exp
 * ExpiredState.java,v
 * Revision 1.1  2006/10/10 19:42:57  Paul_Glenn
 * Update withdrawal states.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import org.apache.commons.lang.BooleanUtils;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;

/**
 * ExpiredState contains the implementation of the expired state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/10/10 19:42:57
 */
public final class ExpiredState extends EndState {

    /**
     * {@inheritDoc}
     */
    @Override
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.EXPIRED;
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final Withdrawal withdrawal) throws DistributionServiceException {

        // We are in the expired state so we know we are invalid - determine our origin for message
        if (BooleanUtils.isTrue(withdrawal.getWithdrawalRequest().getRequestInitiatedFromView())) {
            withdrawal.getWithdrawalRequest().addMessage(
                    new WithdrawalMessage(
                            WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_VIEW_PAGE));
        } else {
            withdrawal
                    .getWithdrawalRequest()
                    .addUniqueMessage(
                            new WithdrawalMessage(
                                    WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE));
        }
    }
}
