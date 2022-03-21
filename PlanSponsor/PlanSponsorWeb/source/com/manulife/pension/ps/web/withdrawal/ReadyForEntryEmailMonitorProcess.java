package com.manulife.pension.ps.web.withdrawal;

import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.EmailGeneratorServiceDelegate;

/**
 * Periodic process used to check if all Online Withdrawal Requests that were
 * approved since the last execution have a corresponding Ready-for-Entry Email
 * in the i:withdrawals mail-in database.
 * 
 * TBD: What is the time frame when this process should be executed?
 * 
 * The interval between calls and the necesary params are configured with the
 * following XML fragment in web.xml:
 * 
 * <process jobName="ReadyForEntryEmailMonitorProcess"
 *    className="com.manulife.pension.ps.web.withdrawal.ReadyForEntryEmailMonitorProcess"
 *    scheduleType="cron" scheduleExpression="0 30 23 * * ?"> 
 * </process>
 * 
 *  The scheduleExpression is a cron expression applicable to the org.quartz.CronTrigger. 
 *  with the following format:
 *  
 *  Field Name      Allowed Values      Allowed Special Characters
 *  Seconds         0-59                , - * /
 *  Minutes         0-59                , - * /
 *  Hours           0-23                , - * /
 *  Day-of-month    1-31                , - * ? / L W C
 *  Month           1-12 or JAN-DEC     , - * /
 *  Day-of-Week     1-7 or SUN-SAT      , - * ? / L C #
 *  Year (Optional) empty, 1970-2099    , - * /
 * 
 * @see http://quartz.sourceforge.net/javadoc/org/quartz/CronTrigger.html
 * 
 * @author Aurelian Penciu
 */
public class ReadyForEntryEmailMonitorProcess implements PeriodicProcess {
    private static final Logger logger = Logger.getLogger(ReadyForEntryEmailMonitorProcess.class);

    public void execute() {
        
        if (logger.isDebugEnabled()) {
            logger.debug("Starting ReadyForEntryEmailMonitorProcess.");
        }
        
        try {
            EmailGeneratorServiceDelegate service = EmailGeneratorServiceDelegate.getInstance();
            service.sendReadyForEntryNotificationMessage();
        } catch (SystemException e) {
            logger.error("Failed to run ReadyForEntryEmailMonitorProcess.", e);
        }
        
        if (logger.isDebugEnabled()) {
            logger.debug("Finished ReadyForEntryEmailMonitorProcess.");
        }
    }
}
