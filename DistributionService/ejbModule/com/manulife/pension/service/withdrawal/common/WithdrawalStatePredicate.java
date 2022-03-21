package com.manulife.pension.service.withdrawal.common;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections.Predicate;

import com.manulife.pension.service.withdrawal.domain.WithdrawalStateEnum;
import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;

/**
 * Determines if a {@link WithdrawalRequest} has one of the withdrawal states that are provided to
 * the constructor.
 * 
 * @author glennpa
 */
public class WithdrawalStatePredicate implements Predicate {

    private Collection<WithdrawalStateEnum> states;

    private Collection<String> stateCodes;

    /**
     * Default Constructor.
     * 
     * @param states The states for which this predicate will return a match for.
     */
    public WithdrawalStatePredicate(final Collection<WithdrawalStateEnum> states) {
        super();

        if (states == null) {
            throw new IllegalArgumentException("Provided states were null.");
        }

        this.states = states;

        convertStatesToCodes();
    }

    /**
     * Default Constructor.
     * 
     * @param states The states for which this predicate will return a match for.
     */
    public WithdrawalStatePredicate(final WithdrawalStateEnum... states) {
        this.states = new ArrayList<WithdrawalStateEnum>(states.length);
        for (WithdrawalStateEnum withdrawalStateEnum : states) {
            this.states.add(withdrawalStateEnum);
        } // end for

        convertStatesToCodes();
    }

    /**
     * Converts States To Codes.
     */
    private void convertStatesToCodes() {
        stateCodes = new ArrayList<String>(states.size());
        for (WithdrawalStateEnum withdrawalStateEnum : states) {
            stateCodes.add(withdrawalStateEnum.getStatusCode());
        } // end for
    }

    /**
     * {@inheritDoc}
     */
    public boolean evaluate(final Object target) {

        if (target == null) {
            throw new IllegalArgumentException("Target was null");
        }

        if (target instanceof WithdrawalRequest) {
            final WithdrawalRequest withdrawalRequest = (WithdrawalRequest) target;

            return stateCodes.contains(withdrawalRequest.getStatusCode());
        } else {
            throw new IllegalArgumentException(new StringBuffer("Target class type [").append(
                    target.getClass().getName()).append("] is not supported.").toString());
        }
    }

    /**
     * @return Collection<WithdrawalStateEnum> - The states.
     */
    public Collection<WithdrawalStateEnum> getStates() {
        return states;
    }

}
