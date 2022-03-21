package com.manulife.pension.ps.web;

import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.ps.web.validation.pentest.PsValidator1;

public class PsValidationPeriodicProcess implements PeriodicProcess {

	private Logger logger = Logger.getLogger(PsValidationPeriodicProcess.class);

	@Override
	public void execute() {
		logger.error("ERROR! calling execute() in unsupported PS Periodic Process");
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> execute");

		try {
			PsValidator1.getInstance().reloadCatalog();
		
			if (logger.isDebugEnabled())
				logger.debug("exit <-- execute");
		} 
		catch (Exception e) {
			logger.error("ERROR! Failed PsValidationPeriodicProcess - execute(): " + e.getMessage());
		}
	}

}
