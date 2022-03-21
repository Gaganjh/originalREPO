package com.manulife.pension.ps.web.withdrawal;

import java.util.Date;

import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.SystemUser;
import com.manulife.pension.service.withdrawal.delegate.WithdrawalServiceDelegate;

/**
 * Periodic process used to process expired Withdrawals requests.
 * 
 * Needs to run every day after 00:00 and [complete (TBD) by] 08:00.
 * 
 * The interval between calls and the necesary params are configured with the following XML fragment
 * in web.xml:
 * 
 * <pre>
 *  &lt;process jobName=&quot;ExpireWithdrawalRequest&quot;
 *      className=&quot;com.manulife.pension.ps.web.withdrawal.ExpireWithdrawalRequestProcess&quot;
 *      scheduleType=&quot;cron&quot;
 *      scheduleExpression=&quot;0 15 0 * * ?&quot;&gt;
 *  &lt;/process&gt;
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
 * </pre>
 * 
 * @see http://quartz.sourceforge.net/javadoc/org/quartz/CronTrigger.html
 * 
 * @author Aurelian Penciu
 */
public class ExpireWithdrawalRequestsProcess implements PeriodicProcess {
    private static final Logger logger = Logger.getLogger(ExpireWithdrawalRequestsProcess.class);

    /**
     * {@inheritDoc}
     */
    public void execute() {

        logger.debug("Starting ExpireWithdrawalRequestsProcess.");

        try {
            WithdrawalServiceDelegate.getInstance().markExpiredWithdrawals(new Date(),
                    SystemUser.SUBMISSION.getProfileId());
        } catch (SystemException systemException) {
            logger.error("Failed to execute ExpireWithdrawalRequestsProcess.", systemException);
        } finally {
            logger.debug("Finished ExpireWithdrawalRequestsProcess.");
        } // end try/catch/finally
    }
}
