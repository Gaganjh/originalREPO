package com.manulife.pension.service.withdrawal.common;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;

/**
 * A predicate for matching the specified message type to a message type within an error collection.
 * 
 * @author dickand
 * @see org.apache.commons.collections.Predicate
 */
public class WithdrawalMessageTypePredicate implements Predicate {

    private WithdrawalMessageType messageType;

    /**
     * Default constructor - takes a message type to evaluate against for.
     */
    public WithdrawalMessageTypePredicate(final WithdrawalMessageType messageType) {

        if (messageType == null) {
            throw new IllegalArgumentException("Message type was null");
        }
        this.messageType = messageType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.commons.collections.Predicate#evaluate(java.lang.Object)
     */
    public boolean evaluate(final Object target) {

        if (target == null) {
            throw new IllegalArgumentException("Target was null");
        }
        if (target instanceof WithdrawalMessage) {
            return ObjectUtils.equals(getMessageType(), ((WithdrawalMessage) target)
                    .getWithdrawalMessageType());
        } else {
            throw new IllegalArgumentException(new StringBuffer("Target class type [").append(
                    target.getClass().getName()).append("] is not supported.").toString());
        }
    }

    /**
     * @return the message type
     */
    public WithdrawalMessageType getMessageType() {
        return messageType;
    }
}
