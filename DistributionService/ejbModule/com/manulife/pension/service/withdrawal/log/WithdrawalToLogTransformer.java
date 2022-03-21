/*
 * Apr 27, 2007
 * 2:51:58 PM
 */
package com.manulife.pension.service.withdrawal.log;

import org.apache.commons.collections.Transformer;

import com.manulife.pension.service.withdrawal.valueobject.BaseWithdrawal;

/**
 * Transforms the given object into it's logging format.
 * 
 * @author glennpa
 */
public class WithdrawalToLogTransformer implements Transformer {

    /**
     * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
     */
    public Object transform(final Object input) {

        if (input instanceof BaseWithdrawal) {
            BaseWithdrawal baseWithdrawal = (BaseWithdrawal) input;
            return baseWithdrawal.toLog();
        }
        return input;
    }

}
