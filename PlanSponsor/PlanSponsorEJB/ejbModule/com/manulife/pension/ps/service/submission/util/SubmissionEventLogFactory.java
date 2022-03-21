package com.manulife.pension.ps.service.submission.util;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.log.FileUploadEventLog;
import com.manulife.pension.ps.common.util.log.OnlineSubmissionEventLog;
import com.manulife.pension.ps.common.util.log.SubmissionEventLog;

/**
 * This class is used to instantiate the appropriate 
 * SubmissionEventLog depending on the event type. All SubmissionEventLog 
 * class has to be created by factory. Factory is implemented 
 * as singleton.
 * 
 * @author Jim Adamthwaite
 */
public class SubmissionEventLogFactory {
	private static SubmissionEventLogFactory instance = new SubmissionEventLogFactory();
	
	public final static Class SUBMISSION_EVENT_LOG = SubmissionEventLog.class;	
	public final static Class FILE_UPLOAD_EVENT_LOG = FileUploadEventLog.class;	
	public final static Class ONLINE_SUBMISSION_EVENT_LOG = OnlineSubmissionEventLog.class;	
	
	private SubmissionEventLogFactory() { 
	}
	
	public static SubmissionEventLogFactory getInstance() {
		return instance;
	}
	
	/**
	 * This method creates the specific EventLog
	 * 
	 * @param type
	 * 		Event Type
	 * @return EventLog	
	 * 		Returns the appropriate EventLog object
	 * @throws SystemException
	 * 		If there is problem with instantiation.
	 */	
	public SubmissionEventLog createEventLog(Class type) throws SystemException
	{
		try {
			return (SubmissionEventLog)type.newInstance();
		}
		catch (IllegalAccessException e)
		{
			throw new SystemException(e, "SubmissionEventLogFactory","createEventLog", "IllegalAccessException for "+ type.getName());
		}
		catch (InstantiationException e)
		{
			throw new SystemException(e, "SubmissionEventLogFactory","createEventLog", "InstantiationException for "+ type.getName());
		}
		
	}
	
}

