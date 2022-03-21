package com.manulife.pension.service.distribution.log;

import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.LogEventException;

/**
 * Distribution Expired notification event.
 * 
 * Requires the following standard EventLog parameters to be set: - updatedTS - className -
 * methodName - userName
 * 
 * @see EventLog
 * 
 * Requires the following extra parameters to be set before logging: - SUBMISSION_ID
 * @see EventLog#addLogInfo(String, Object)
 */
public class ExpiredDistributionEventLog extends EventLog {

    private static final String EVENT_NAME = DistributionEventEnum.EXPIRE.getEventName();

    private String applicationId = "PS";

    /**
     * Creates an ExpiredWithdrawalEventLog instance and initializes the applicationId.
     * 
     * @param applicationId
     */
    public ExpiredDistributionEventLog() {
        super(ExpiredDistributionEventLog.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getApplicationId() throws LogEventException {
        return applicationId;
    }

    /**
     * Returns the R/O event name.
     * 
     * @return Name of the event
     */
    @Override
    protected String getEventName() {
        return EVENT_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String prepareLogData() throws LogEventException {
        String logData = SUBMISSION_ID + ": " + this.getParameter(SUBMISSION_ID) + ", "
                + getCommonLogData();

        return logData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validate() throws LogEventException {
        if (!logInfoExists(SUBMISSION_ID)) {
            throw new LogEventException("Submission ID has not been set.");
        }
    }

    /**
     * Sets the ApplicationId.
     * 
     * @param applicationId The application ID to set.
     */
    public void setApplicationId(final String applicationId) {
        this.applicationId = applicationId;
    }
}
