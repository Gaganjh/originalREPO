package com.manulife.pension.ps.service.notice.periodicProcess;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.delegate.ContractServiceDelegate;
import com.manulife.pension.event.Event;
import com.manulife.pension.event.RiaDesignationChangedEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.event.service.EventServiceConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.contract.valueobject.Contract338ChangeDetailsData;
import com.manulife.pension.util.log.LogUtility;

/**
 * 
 * @author narintr
 *
 */
public class RiaDesignationChangedEventPeriodicProcess implements PeriodicProcess {

	private static final Logger logger = Logger.getLogger(RiaDesignationChangedEventPeriodicProcess.class);
	
	private static final String PROCESS_ID = "RiaDesignationChanged";
	
	private static String businessUnitProperty = "";
	
	private static String cycleIdProperty = "";
	
	private static String manualRunDateProperty = "";
	
	public RiaDesignationChangedEventPeriodicProcess(){

	}
	
	static {
			
			Properties riaProperties = new Properties();
			
			try {
				InputStream input = ClassLoader.getSystemResourceAsStream("riaPeriodicProcess.properties"); 
				if (input != null) {
					try {
						riaProperties.load(input);
						businessUnitProperty = riaProperties.getProperty("RiaDesignationChangedEventPeriodicProcess.businessUnit");
						cycleIdProperty = riaProperties.getProperty("RiaDesignationChangedEventPeriodicProcess.cycleDate");
						manualRunDateProperty = riaProperties.getProperty("RiaDesignationChangedEventPeriodicProcess.implementationDate");
					} finally {
						input.close();
					}
				}
			} catch (Exception exception) {
				logger.error("Unable to load periodic process properties from riaPeriodicProcess.properties file.",
								exception);
			}
	}

	@Override
	public void execute() {

		if (logger.isDebugEnabled()){
			logger.debug("entry -> execute");
		}
		
		List<Contract338ChangeDetailsData> contracts338changesDetails = null;
		RiaDesignationChangedEvent event = null;
		int contractNo = 0;
		Date onDate = null;
		List<String> progressDetailsList = null;

		try{
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtils.isNotEmpty(manualRunDateProperty)){
				Date manualRunDate = df.parse(manualRunDateProperty);
				onDate = manualRunDate;
			}
			if(onDate == null){
				if(StringUtils.isNotEmpty(businessUnitProperty) && StringUtils.isNotEmpty(cycleIdProperty)){
					onDate = ContractServiceDelegate.getInstance().getRunDate(businessUnitProperty, cycleIdProperty);
				}
			}
			/*progressDetailsList = ContractServiceDelegate.getInstance().checkOnDateIsThereInProcessProgressStatus(PROCESS_ID);
			if(progressDetailsList == null && !progressDetailsList.contains(dateFormat.format(onDate))){
				//Insert
			}
			if(progressDetailsList != null && progressDetailsList.contains(dateFormat.format(onDate))){
				//Update
			}*/
			contracts338changesDetails = ContractServiceDelegate.getInstance().getContracts338changesDetails(contractNo, onDate);
			if(contracts338changesDetails != null){
				for(Contract338ChangeDetailsData contract338ChangeDetailsData : contracts338changesDetails){
					event = new RiaDesignationChangedEvent(this.getClass().getName(), "execute");
					event.setConsumerName("MessageGeneratorMDB");
					event.setOriginatorMethod("execute");
					event.setInitiator(Event.SYSTEM_USER_PROFILE_ID);
					event.setContractId(contract338ChangeDetailsData.getContractId());
					event.setContractShortName(contract338ChangeDetailsData.getContractShortName());
					event.setRiaDesignationChangeDate(String.valueOf(df.format(contract338ChangeDetailsData.getEffectiveDate())));
					
					if(contract338ChangeDetailsData.getProducers338added() != null){
						event.setRiaDesignationAddedList(String.valueOf(contract338ChangeDetailsData.getProducers338added().size()));
					}else{
						event.setRiaDesignationAddedList("");
					}
					if(contract338ChangeDetailsData.getProducers338Removed() != null){
						event.setRiaDesignationRemovedList(String.valueOf(contract338ChangeDetailsData.getProducers338Removed().size()));
					}else{
						event.setRiaDesignationRemovedList("");
					}
					EventClientUtility.getInstance("PS").prepareAndSendJMSMessage(event);
				}
			}

		} catch (SystemException se) {
			LogUtility.logSystemException(EventServiceConstants.EVENT_APPLICATION_ID, se);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (logger.isDebugEnabled()){
			logger.debug("exit <- execute");	
		}
	}
}
