/*
 * Created on Jan 13, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.common.util.log;

import org.apache.log4j.Logger;

/**
 * @author adamthj
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OnlineSubmissionEventLog extends SubmissionEventLog {


	public OnlineSubmissionEventLog() {
		this.logger = Logger.getLogger(OnlineSubmissionEventLog.class);		
		SERVICE_NAME = "OnlineSubmission";
	}

}
