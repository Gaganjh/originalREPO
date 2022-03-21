package com.manulife.pension.service.withdrawal.common;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestDeclaration;

/**
 * A predicate for matching the specified declaration type to a declaration type within a collection
 * of WithdrawalRequestDeclarations.
 * 
 * @author dickand
 * @see org.apache.commons.collections.Predicate
 */
public class WithdrawalDeclarationPredicate implements Predicate {

    private String typeCode;

    /**
     * Default constructor - takes a type code to evaluate against for.
     */
    public WithdrawalDeclarationPredicate(final String typeCode) {

        if (typeCode == null) {
            throw new IllegalArgumentException("Type code was null");
        }
        this.typeCode = typeCode;
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
        if (target instanceof WithdrawalRequestDeclaration) {
            return StringUtils.equals(getTypeCode(), ((WithdrawalRequestDeclaration) target)
                    .getTypeCode());
        } else {
            throw new IllegalArgumentException(new StringBuffer("Target class type [").append(
                    target.getClass().getName()).append("] is not supported.").toString());
        }
    }

    /**
     * @return the type code
     */
    public String getTypeCode() {
        return typeCode;
    }
}
