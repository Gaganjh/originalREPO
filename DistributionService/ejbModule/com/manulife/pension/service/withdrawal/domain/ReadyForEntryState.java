/*
 * ReadyForEntryState.java,v 1.1 2006/10/10 19:42:57 Paul_Glenn Exp
 * ReadyForEntryState.java,v
 * Revision 1.1  2006/10/10 19:42:57  Paul_Glenn
 * Update withdrawal states.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

/**
 * ReadyForEntryState contains the implementation of the ready for entry state.
 * 
 * @see WithdrawalState
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/10/10 19:42:57
 */
public final class ReadyForEntryState extends EndState {

    /**
     * {@inheritDoc}
     */
    @Override
    public WithdrawalStateEnum getWithdrawalStateEnum() {
        return WithdrawalStateEnum.READY_FOR_ENTRY;
    }

}
