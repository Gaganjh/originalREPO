package com.manulife.pension.ps.service.notice.periodicProcess;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.notification.handlers.EcommPrimaryPagerNotificationHandler;
import com.manulife.pension.service.notification.handlers.vo.EmailInfoVO;
import com.manulife.pension.util.BaseEnvironment;

/**
 * 
 * @author Tamilarasu Krishnamoorthy
 *
 */
public class NoticeManagerAlertNotifyPeriodicProcess implements PeriodicProcess {
	
private static final Logger logger = Logger.getLogger(NoticeManagerAlertNotifyPeriodicProcess.class);
private static final String PROPERTY_MONITOR_LOG_DIR = "periodicProcessLogDir";
private static String MONITOR_LOG_DIR;
private static String MONITOR_LOG_FILE_NAME = "noticeManager.log";
private static FastDateFormat LOG_SDF = FastDateFormat.getInstance("EEE MMM d HH:mm:ss z yyyy");
	
	/**
	 * Executes the FeeDisclosure Zip periodic process.
	 */
    public void execute() {	           
        try {
        	logger.debug("Started Notice Manager Alert Notification periodic processing.");
        	
        	NoticeManagerAlertNotifyPeriodicProcessor.getInstance().execute();
        	
            logger.debug("Finished Notice Manager Alert Notification periodic processing.");
            
            updateRunLogFile(System.currentTimeMillis());
        	
            logger.debug("updating the log file with current RunTime for Notice Manager Alert Notification periodic processing");
	            
        } catch (SystemException e) {
        	logger.error("Notice Manager Alert Notification periodic processing failed", e);
        	sendAlertEmailToSupport(e);
        	
        } catch (IOException e)
        {
        logger.error("updating the log file with current RunTime for Notice Manager Alert Notification periodic processing Failed", e);
        sendAlertEmailToSupport(e);
		}
    }
    
    public static void main(String[] args) {
    	new NoticeManagerAlertNotifyPeriodicProcess().execute();
    }
    
    /**
	 *  Used to handle Exception with sendAlertEmailToSupport
	 *  
	 * @param Exception 
	
	 */
    
    private void sendAlertEmailToSupport(Exception e)
	{
		EcommPrimaryPagerNotificationHandler pageEcomm = new EcommPrimaryPagerNotificationHandler();
		EmailInfoVO message = new EmailInfoVO();
		message.setSubject("NOTICE MANAGER ALERT NOTIFICATION PROBLEM");
		message.setBody("NOTICE MANAGER ALERT NOTIFICATION PROCESS encountered a problem : "+e);
		try
		{
			pageEcomm.handleNotification("PS", message);
		}
		catch(SystemException se)
		{
			logger.error("Could not send out alert to ECOMM primary"+se);
		}

	}
    
    /**
	 *  Generates the log file for the Notice Manager Alert Notification periodic process.
	 *  
	 * @param successRunTimeInMilli
	 * 
	 * @throws IOException
	 */
	private void updateRunLogFile(long successRunTimeInMilli)
			throws IOException {
		label0: {
			Date successRunTime = new Date(successRunTimeInMilli);
			String runTimeString = (new StringBuilder()).append(
					LOG_SDF.format(successRunTime)).append("\n").toString();
			MONITOR_LOG_DIR = (new BaseEnvironment()).getNamingVariable(
					PROPERTY_MONITOR_LOG_DIR, null);
			if (MONITOR_LOG_DIR == null) {
				return;
			}
			// For Testing in local Environment
			
			//String path = (new StringBuilder()).append("C:\\db\\PeriodicProcessMonitor\\").append(SUCCESS_RUN_FILENAME).toString();
			
			// Remote Environment path
			
			String path = (new StringBuilder()).append(MONITOR_LOG_DIR).append(
					"\\").append(MONITOR_LOG_FILE_NAME).toString();
			
			OutputStream fileOutputStream = null;
			
			
			
			try {
				fileOutputStream = new FileOutputStream(path);
				fileOutputStream.write(runTimeString.getBytes());
			} catch (FileNotFoundException fnf) {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException ioe) {
				}
				break label0;
			} catch (IOException ioe) {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				}
				// Misplaced declaration of an exception variable
				catch (IOException ioe1) {
				}
				break label0;
			} finally {
				
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException ioe) {}
			
				//throw exception;
			}
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException ioe) {
			}
			break label0;
		}
	}
}
