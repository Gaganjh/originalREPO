package com.manulife.pension.ps.service.participant.misc;

import com.intware.batch.process.PeriodicProcess;

//import java.io.InputStream;
//import org.apache.log4j.xml.DOMConfigurator;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.spi.LoggerRepository;

public class ParticipantEnrollmentNotifier implements PeriodicProcess {
	
	public void execute() {
		System.out.println("Called ParticipantEnrollmentNotifier!!");
	}
}