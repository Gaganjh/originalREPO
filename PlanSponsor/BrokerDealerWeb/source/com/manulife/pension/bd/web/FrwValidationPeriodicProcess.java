package com.manulife.pension.bd.web;


import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;

public class FrwValidationPeriodicProcess implements PeriodicProcess {

	private Logger logger = Logger.getLogger(FrwValidationPeriodicProcess.class);

	@Override
	public void execute() {
		logger.error("ERROR! calling execute() in unsupported FRW Periodic Process");
		
		if (logger.isDebugEnabled())
			logger.debug("entry -> execute");

		try {
			FrwValidation.getInstance().reloadCatalog();
		
			if (logger.isDebugEnabled())
				logger.debug("exit <-- execute");
		} 
		catch (Exception e) {
			logger.error("ERROR! Failed FrwValidationPeriodicProcess - execute(): " + e.getMessage());
		}
	}
}
