package com.manulife.pension.service.withdrawal.exception;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;

/**
 * WithdrawalEmailException is used to signal exceptions that happen while generating emails for the
 * when status changes.
 * 
 * @author Dennis Snowdon
 */
public class WithdrawalServiceFeatureException extends DistributionServiceException {

    public WithdrawalServiceFeatureException(String className, String methodName, String message) {
        super(className, methodName, message);

    }

    public WithdrawalServiceFeatureException(Throwable exception, String className,
            String methodName, String message) {
        super(exception, className, methodName, message);
    }

    @Override
    public String getErrorCode() {
        return "";
    }

    static final long serialVersionUID = 1L;

    public WithdrawalServiceFeatureException(String message) {
        super(message);
    }
}
