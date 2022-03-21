package com.manulife.pension.service.withdrawal.exception;

import com.manulife.pension.service.distribution.exception.DistributionServiceException;

/**
 * WithdrawalEmailException is used to signal exceptions that happen while generating emails for the
 * when status changes.
 * 
 * @author Mihai Popa
 */
public class WithdrawalEmailException extends DistributionServiceException {

    /**
     * Default Constructor.
     * 
     * @param className
     * @param methodName
     * @param message
     */
    public WithdrawalEmailException(String className, String methodName, String message) {
        super(className, methodName, message);

    }

    public WithdrawalEmailException(Throwable exception, String className, String methodName,
            String message) {
        super(exception, className, methodName, message);
    }

    @Override
    public String getErrorCode() {
        return "";
    }

    static final long serialVersionUID = 1L;

    public WithdrawalEmailException(String message) {
        super(message);
    }

    /**
     * Default Constructor.
     * 
     * @param message The error message.
     * @param throwable The cause of the exception.
     */
    public WithdrawalEmailException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

}
