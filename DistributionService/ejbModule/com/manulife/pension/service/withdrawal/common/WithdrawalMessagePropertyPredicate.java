package com.manulife.pension.service.withdrawal.common;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;

/**
 * A predicate for counting the occurrances specified message field property within an error
 * collection.
 * 
 * @author dickand
 * @see org.apache.commons.collections.Predicate
 */
public class WithdrawalMessagePropertyPredicate implements Predicate {

    private String property;

    /**
     * Default constructor - takes a message type to evaluate against for.
     */
    public WithdrawalMessagePropertyPredicate(final String property) {

        if (property == null) {
            throw new IllegalArgumentException("Property was null");
        }
        this.property = property;
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
            return CollectionUtils.exists(((WithdrawalMessage) target).getPropertyNames(),
                    PredicateUtils.equalPredicate(getProperty()));
        } else {
            throw new IllegalArgumentException(new StringBuffer("Target class type [").append(
                    target.getClass().getName()).append("] is not supported.").toString());
        }
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }
}
