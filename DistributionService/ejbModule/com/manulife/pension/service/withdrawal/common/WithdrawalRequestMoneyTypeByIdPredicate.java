package com.manulife.pension.service.withdrawal.common;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType;

/**
 * A predicate for matching the specified {@link WithdrawalRequestMoneyType} to a
 * {@link WithdrawalRequestMoneyType} within a collection using the
 * {@link WithdrawalRequestMoneyType#getMoneyTypeId()} to match, ignoring other field values.
 * 
 * @author glennpa
 * 
 * @see org.apache.commons.collections.Predicate
 */
public class WithdrawalRequestMoneyTypeByIdPredicate implements Predicate {

    private WithdrawalRequestMoneyType withdrawalRequestMoneyType;

    /**
     * Default Constructor.
     * 
     * @param withdrawalRequestMoneyType The money type to compare against.
     */
    public WithdrawalRequestMoneyTypeByIdPredicate(
            final WithdrawalRequestMoneyType withdrawalRequestMoneyType) {
        if (withdrawalRequestMoneyType == null) {
            throw new IllegalArgumentException("WithdrawalRequestMoneyType type was null");
        } // fi

        this.withdrawalRequestMoneyType = withdrawalRequestMoneyType;
    }

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(final Object object) {

        if (object == null) {
            throw new IllegalArgumentException("Object to check was null");
        }
        if (object instanceof WithdrawalRequestMoneyType) {
            return StringUtils.equals(getWithdrawalRequestMoneyType().getMoneyTypeId(),
                    ((WithdrawalRequestMoneyType) object).getMoneyTypeId());
        } else {
            throw new IllegalArgumentException(new StringBuffer("Object to check class type [")
                    .append(object.getClass().getName()).append("] is not supported.").toString());
        }
    }

    /**
     * @return WithdrawalRequestMoneyType - The withdrawalRequestMoneyType.
     */
    public WithdrawalRequestMoneyType getWithdrawalRequestMoneyType() {
        return withdrawalRequestMoneyType;
    }

}
