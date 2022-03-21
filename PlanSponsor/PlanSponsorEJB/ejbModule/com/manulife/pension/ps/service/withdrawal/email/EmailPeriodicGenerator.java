package com.manulife.pension.ps.service.withdrawal.email;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.delegate.EmailGeneratorServiceDelegate;

/**
 * A generic periodic process to generate withdrawal emails. Retrieve all withdrawal requests that
 * are in draft or pending status or about to expire and generates appropriate email messages.
 * 
 * <pre>
 * &lt;process jobName=&quot;GenerateEmails&quot;
 *          className=&quot;com.manulife.pension.ps.service.withdrawal.email.EmailPeriodicGenerator&quot;
 *          scheduleType=&quot;timeinterval&quot; 
 *          scheduleExpression=&quot;120000&quot; /&gt;
 * </pre>
 * 
 * @author Mihai Popa
 */
public class EmailPeriodicGenerator implements PeriodicProcess {

    /**
     * Method that gets called by the timer framework when the job is to be run.
     */
    public void execute() {

        EmailGeneratorServiceDelegate emailGeneratorServiceDelegate = EmailGeneratorServiceDelegate
                .getInstance();
        try {
            emailGeneratorServiceDelegate.generateMessages();
        } catch (SystemException systemException) {
            throw ExceptionHandlerUtility.wrap(systemException);
        }
    }

}
