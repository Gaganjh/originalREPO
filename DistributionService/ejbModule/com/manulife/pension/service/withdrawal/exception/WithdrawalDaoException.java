package com.manulife.pension.service.withdrawal.exception;

import com.manulife.pension.service.distribution.exception.DistributionServiceDaoException;

/**
 * WithdrawalEmailException is used to signal exceptions that happen while generating emails for the
 * when status changes.
 * 
 * @author Dennis Snowdon
 */
public class WithdrawalDaoException extends DistributionServiceDaoException {

    public WithdrawalDaoException(String className, String methodName, String message) {
        super(className, methodName, message);

    }

    public WithdrawalDaoException(Throwable exception, String className, String methodName,
            String message) {
        super(exception, className, methodName, message);
    }

    @Override
    public String getErrorCode() {
        return "";
    }

    static final long serialVersionUID = 1L;

    public WithdrawalDaoException(String message) {
        super(message);
    }
}
