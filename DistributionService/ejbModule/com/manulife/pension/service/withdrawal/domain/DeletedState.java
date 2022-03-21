/*
 * DeletedState.java,v 1.1 2006/10/10 19:42:57 Paul_Glenn Exp
 * DeletedState.java,v
 * Revision 1.1  2006/10/10 19:42:57  Paul_Glenn
 * Update withdrawal states.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessage;
import com.manulife.pension.service.withdrawal.common.WithdrawalMessageType;

/**
 * DeletedState contains the implementation of the deleted state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/10/10 19:42:57
 */
public final class DeletedState extends EndState {

    /**
     * {@inheritDoc}
     */
    @Override
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.DELETED;
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final Withdrawal withdrawal) throws DistributionServiceException {

        withdrawal.getWithdrawalRequest().addUniqueMessage(
                new WithdrawalMessage(
                        WithdrawalMessageType.WITHDRAWAL_REQUEST_HAS_EXPIRED_INITIATE_REVIEW_PAGE));
    }
}
