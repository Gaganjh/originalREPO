/*
 * WithdrawalState.java,v 1.1 2006/09/25 19:15:28 Paul_Glenn Exp
 * WithdrawalState.java,v
 * Revision 1.1  2006/09/25 19:15:28  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.distribution.exception.DistributionServiceException;
import com.manulife.pension.service.withdrawal.valueobject.ActivityHistory;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * WithdrawalState is the interface for all states. Any 'stateful' methods on the withdrawal should
 * be delegated from the withdrawal object to the state (see State patterm from Gang Of Four).
 * 
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1 2006/09/25 19:15:28
 */
public interface WithdrawalState {

    /**
     * This method persists the withdrawal object.
     * 
     * @param withdrawal The object to act on.
     * @throws DistributionServiceException thrown if there is an exception
     */
    void save(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method is used to deny the withdrawal request.
     * 
     * @param withdrawal The withdrawal to deny.
     * @throws DistributionServiceException Thrown if there is an exception
     */
    void deny(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method is used to send the withdrawal request for review.
     * 
     * @param withdrawal The withdrawal to send for review.
     * @throws DistributionServiceException Thrown if there is an exception
     */
    void sendForReview(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method is used to send the withdrawal request for approval.
     * 
     * @param withdrawal The withdrawal to send for approval.
     * @throws DistributionServiceException Thrown if there is an exception
     */
    void sendForApproval(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method approves the withdrawal object.
     * 
     * @param withdrawal The object to act on.
     * @throws DistributionServiceException Thrown if there is an exception
     */
    void approve(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method processes the approved withdrawal object.
     * 
     * @param withdrawal The object to act on.
     * @throws DistributionServiceException thrown if there is an exception
     */
    void processApproved(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method expires the withdrawal.
     * 
     * @param withdrawal The withdrawal to act on.
     * @throws DistributionServiceException Thrown if there is an exception
     */
    void expire(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method cancels the current processing of the withdrawal (no save, reverts to old).
     * 
     * @param withdrawal The withdrawal to act on.
     */
    void cancel(Withdrawal withdrawal);

    /**
     * This method deletes the withdrawal.
     * 
     * @param withdrawal The withdrawal to act on.
     * @throws DistributionServiceException Thrown if there is an exception
     */
    void delete(Withdrawal withdrawal) throws DistributionServiceException;

    /**
     * This method applies the given data to the withdrawal depending on the current state of the
     * withdrawal. For example, draft withdrawals are always updated with the active data, whereas
     * an approved withdrawal does not use active data, it uses the data it was approved with.
     * 
     * It is also dependent on whether the user is editing or viewing.
     * 
     * @param withdrawal The withdrawal to apply the data to.
     * @param withdrawalRequest The data to be applied to the withdrawal.
     * @throws SystemException thrown if a system exception occurs.
     */
    void applyDefaultDataForEdit(Withdrawal withdrawal, WithdrawalRequest withdrawalRequest)
            throws SystemException;

    /**
     * This method applies the given data to the withdrawal depending on the current state of the
     * withdrawal. For example, draft withdrawals are always updated with the active data, whereas
     * an approved withdrawal does not use active data, it uses the data it was approved with.
     * 
     * It is also dependent on whether the user is editing or viewing.
     * 
     * @param withdrawal The withdrawal to apply the data to.
     * @param withdrawalRequest The data to be applied to the withdrawal.
     * @throws SystemException thrown if a system exception occurs.
     */
    void applyDefaultDataForView(Withdrawal withdrawal, WithdrawalRequest withdrawalRequest)
            throws SystemException;

    /**
     * Gets the enum value of this state.
     * 
     * @see WithdrawalStateEnum
     * 
     * @return WithdrawalStateEnum The state value.
     */
    WithdrawalStateEnum getWithdrawalStateEnum();

    /**
     * creates the activity history portion of the withdrawal request.
     * 
     * @param withdrawal The withdrawal that contains the withdrawal request to populate
     * @return returns the activity history object
     * @throws DistributionServiceException Thrown if there is an underlying exception
     */
    ActivityHistory readActivityHistory(Withdrawal withdrawal) throws DistributionServiceException;

}
