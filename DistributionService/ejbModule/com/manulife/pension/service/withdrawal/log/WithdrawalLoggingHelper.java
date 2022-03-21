/*
 * WithdrawalLoggingHelper.java,v 1.1.2.1 2007/03/28 22:02:28 Paul_Glenn Exp
 * WithdrawalLoggingHelper.java,v
 * Revision 1.1.2.1  2007/03/28 22:02:28  Paul_Glenn
 * Initial.
 *
 */
package com.manulife.pension.service.withdrawal.log;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest;
import com.manulife.pension.util.log.EventLog;
import com.manulife.pension.util.log.EventLogFactory;
import com.manulife.pension.util.log.LogEventException;

/**
 * This class is a helper class to aide with logging events.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.1.2.1 2007/03/28 22:02:28
 */
public final class WithdrawalLoggingHelper {

    private static final Logger logger = Logger.getLogger(WithdrawalLoggingHelper.class);

    /**
     * MAX_WITHDRAWAL_REQUEST_LOG_SIZE is the size at which we split the log record up into parts.
     */
    public static final int MAX_WITHDRAWAL_REQUEST_LOG_SIZE = 9000;

    /**
     * Default Constructor. Is private, as this is a helper class.
     */
    private WithdrawalLoggingHelper() {
    }

    /**
     * This method is used to record the details of the given event to the log.
     * 
     * @param withdrawalRequest - The withdrawal request to record in the log.
     * @param withdrawalEvent - The triggering event.
     * @param clazz - The class from which this event originated.
     * @param methodName - The method name from which this event originated.
     */
    public static void log(final WithdrawalRequest withdrawalRequest,
            final WithdrawalEvent withdrawalEvent, final Class clazz, final String methodName) {

        final EventLog eventLog = EventLogFactory.getInstance().createEventLog(
                WithdrawalEventLog.class);

        eventLog.setClassName(clazz.getName());
        eventLog.setMethodName(methodName);
        if (withdrawalRequest.getLastUpdatedById() != null) {
            eventLog.setUserName(withdrawalRequest.getLastUpdatedById().toString());
        } // fi

        eventLog.addLogInfo(WithdrawalEventLog.SUBMISSION_ID, withdrawalRequest.getSubmissionId());
        eventLog.addLogInfo(WithdrawalEventLog.WITHDRAWAL_ACTION, withdrawalEvent.getEventName());
        eventLog.addLogInfo(WithdrawalEventLog.SSN, withdrawalRequest.getParticipantSSN());

        // final
        String withdrawalRequestInLogFormat = withdrawalRequest.toLog();

        final int fullLogSize = withdrawalRequestInLogFormat.length();

        if (fullLogSize <= MAX_WITHDRAWAL_REQUEST_LOG_SIZE) {
            // Just log the request as usual, one part.
            eventLog
                    .addLogInfo(WithdrawalEventLog.WITHDRAWAL_REQUEST, withdrawalRequestInLogFormat);

            try {
                eventLog.log();
            } catch (LogEventException logEventException) {
                throw new RuntimeException(logEventException);
            } // end try/catch
        } else {
            // We need to split the request up into parts.
            String midString;

            // The number of parts the full log divides into, truncating the remainder.
            int numberOfParts = fullLogSize / MAX_WITHDRAWAL_REQUEST_LOG_SIZE;
            // If the remainder is non-zero, we use an extra part.
            if ((fullLogSize % MAX_WITHDRAWAL_REQUEST_LOG_SIZE) != 0) {
                numberOfParts++;
            } // fi

            int i = 0;

            int locationAlongTheString = 0;
            do {

                // Grab the part of the String to log.
                midString = StringUtils.mid(withdrawalRequestInLogFormat, locationAlongTheString,
                        MAX_WITHDRAWAL_REQUEST_LOG_SIZE);

                // Show which part number we have.
                eventLog.addLogInfo(WithdrawalEventLog.WITHDRAWAL_REQUEST_PART_NUMBER, (i + 1)
                        + " of " + numberOfParts);

                eventLog.addLogInfo(WithdrawalEventLog.WITHDRAWAL_REQUEST, midString);

                // Log this part.
                try {
                    eventLog.log();
                } catch (LogEventException logEventException) {
                    throw new RuntimeException(logEventException);
                } // end try/catch

                i++;
                locationAlongTheString = i * MAX_WITHDRAWAL_REQUEST_LOG_SIZE;

            } while (locationAlongTheString < fullLogSize);

        } // fi
    }

    /**
     * This method is used to record the details of the given event to the log.
     * 
     * @param submissionId - The submission ID of the withdrawal.
     * @param userProfileId - The user profile ID of the person who made the change.
     * @param withdrawalEvent - The triggering event.
     * @param clazz - The class from which this event originated.
     * @param methodName - The method name from which this event originated.
     */
    public static void logShort(final Integer submissionId, final Integer userProfileId,
            final WithdrawalEvent withdrawalEvent, final Class clazz, final String methodName) {

        final EventLog eventLog = EventLogFactory.getInstance().createEventLog(
                WithdrawalEventLog.class);

        eventLog.setClassName(clazz.getName());
        eventLog.setMethodName(methodName);
        if (userProfileId != null) {
            eventLog.setUserName(userProfileId.toString());
        } // fi

        eventLog.addLogInfo(WithdrawalEventLog.SUBMISSION_ID, submissionId);
        eventLog.addLogInfo(WithdrawalEventLog.WITHDRAWAL_ACTION, withdrawalEvent.getEventName());

        try {
            eventLog.log();
        } catch (LogEventException logEventException) {
            throw new RuntimeException(logEventException);
        } // end try/catch

    }

}
