package com.manulife.pension.service.loan.log;

import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.LogEventException;

/**
 * This object is used to log distribution events.
 * 
 * Requires the following standard EventLog parameters to be set: - updatedTS - className -
 * methodName - userName
 * 
 * Requires the following extra parameters to be set before logging: - SUBMISSION_ID
 * 
 * @see EventLog#addLogInfo(String, Object)
 * @see EventLog
 */
public class LoanEventLog extends EventLog {

    /**
     * This is the key used to denote the loan request object.
     */
    public static final String LOAN_REQUEST = "LOAN_REQUEST";

    /**
     * This is the key used to denote the action being logged in this event.
     */
    public static final String ACTION = "ACTION";

    private static final String EVENT_NAME = "Loan Event";

    private String applicationId = "PS";

    /**
     * Creates a WithdrawalEventLog instance.
     */
    public LoanEventLog() {
        super(LoanEventLog.class);
    }

    /**
     * Returns the Application ID.
     * 
     * @return applicationId - The current application ID.
     */
    @Override
    protected String getApplicationId() {
        return applicationId;
    }

    /**
     * Returns the read-only event name.
     * 
     * @return String - The event name.
     */
    @Override
    protected String getEventName() {
        return EVENT_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String prepareLogData() {
        final StringBuffer stringBuffer = new StringBuffer();

        // Write out all the parameters that have been set.
        for (String key : getParameterKeys()) {
            stringBuffer.append(key.toString());
            stringBuffer.append(": ");
            stringBuffer.append(
                    getParameter(key) != null ? getParameter(key).toString()
                            : null);
            stringBuffer.append(", ");
        } // end for

        // Append the common log data.
        stringBuffer.append(getCommonLogData());
        return stringBuffer.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() throws LogEventException {
        if (!logInfoExists(SUBMISSION_ID)) {
            throw new LogEventException("The submission ID has not been set.");
        } // fi
        if (!logInfoExists(ACTION)) {
            throw new LogEventException("The action has not been set.");
        } // fi
    }

    /**
     * Sets the applicationId.
     * 
     * @param applicationId The application ID to set.
     */
    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }
}
