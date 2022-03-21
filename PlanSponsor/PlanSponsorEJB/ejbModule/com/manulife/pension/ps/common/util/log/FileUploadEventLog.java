package com.manulife.pension.ps.common.util.log;

import org.apache.log4j.Logger;

public class FileUploadEventLog extends SubmissionEventLog {
	
	public FileUploadEventLog() {
		logger = Logger.getLogger(FileUploadEventLog.class);		
		SERVICE_NAME = "FileUpload";
	}

}
