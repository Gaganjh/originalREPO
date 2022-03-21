/*
 * DeniedState.java,v 1.1 2006/10/10 19:42:57 Paul_Glenn Exp
 * DeniedState.java,v
 * Revision 1.1  2006/10/10 19:42:57  Paul_Glenn
 * Update withdrawal states.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * DeniedState contains the implementation of the denied state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/10/10 19:42:57
 */
public final class DeniedState extends EndState {

    /**
     * Default Constructor.
     */
    protected DeniedState() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.DENIED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyDefaultDataForEdit(final Withdrawal withdrawal,
            final WithdrawalRequest defaultVo) throws SystemException {

        // In order to detect if the user has tried to edit a denied request, we
        // need to be able to read the request (including merging). We don't bother
        // doing the merge however as the edit request will result in a user error condition.
    }
}
