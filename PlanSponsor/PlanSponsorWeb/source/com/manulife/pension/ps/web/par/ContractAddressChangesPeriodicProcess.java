package com.manulife.pension.ps.web.par;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.delegate.EmployeeServiceDelegate;
import com.manulife.pension.event.ContractParticipantMailingAddressChangedEvent;
import com.manulife.pension.event.client.EventClientUtility;
import com.manulife.pension.event.periodicprocess.dao.EventPeriodicProcessDAO.ProcessStatus;
import com.manulife.pension.event.service.EventServiceConstants;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.CommonConstants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.service.notification.handlers.EcommPrimaryPagerNotificationHandler;
import com.manulife.pension.service.notification.handlers.vo.EmailInfoVO;
import com.manulife.pension.service.par.valueobject.ContractAddressChangeVO;
import com.manulife.pension.util.periodicprocess.AutomatedAgent;
import com.manulife.pension.util.periodicprocess.DataDrivenPeriodicProcess;
import com.manulife.pension.util.periodicprocess.DataDrivenPeriodicProcessRun;
import com.manulife.pension.util.periodicprocess.SupportNotifier;
/**
 * This is the PeriodicProcess class for Contract Address Changes made by users.
 * This contains the logic to instantiate,execute the PeriodicProcess and Fire the event.
 * 
 * @author khannak
 */

public class ContractAddressChangesPeriodicProcess implements PeriodicProcess {
	
	private static final Logger LOGGER = Logger.getLogger(ContractAddressChangesPeriodicProcess.class);
	private static final DataDrivenPeriodicProcess pp;
	private static final String CLASS_NAME = "ContractAddressChangesPeriodicProcess";

	 private static final String PROCESS_ID = "EmpAddrChgByUsers";

	// Block for instantiating DDPP.
	static {
		
		Properties supportProperties = new Properties();
		
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream("acpp.properties"); 
			if (input != null) {
				try {
					supportProperties.load(input);
				} finally {
					input.close();
				}
			}
		} catch (Exception exception) {
			// TODO: PA we should stop the process, if we any errors occurred
			// while loading the file
			LOGGER.error("Unable to load periodic process properties from acpp.properties file.",
							exception);
		}

		SupportNotifier notifier = new SupportNotifier() {
			
			public void sendNotification(String subject, String body)throws SystemException {
				EcommPrimaryPagerNotificationHandler pageEcomm = new EcommPrimaryPagerNotificationHandler();
				EmailInfoVO message = new EmailInfoVO();
				message.setSubject(subject);
				message.setBody(body);
				pageEcomm.handleNotification("EVT", message);
			}

		};

		pp = new DataDrivenPeriodicProcess(ContractAddressChangesPeriodicProcess.class,CACProcessRun.class,supportProperties, notifier, true);
	}

	public ContractAddressChangesPeriodicProcess() throws Throwable {
		pp.instantiateRun();
	}

	
	public void execute() {
		pp.execute();
	}

	public static class CACProcessRun implements DataDrivenPeriodicProcessRun {

		/**
		 * This method will be used to logging.
		 */
		public String getPeriodicProcessIdentifier() throws Exception {
			return CLASS_NAME;
		}

		/** This method returns a list of tasks for the Periodic Process to
		 * execute.
		 */ 
		public List<AutomatedAgent<?>> initializeAutomatedAgents()throws Exception {
			int contractId = 0;
			ContractAddressChangeVO addChngVO= EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).getLastProcessedItemForContractAddressChanges(new java.util.Date(), PROCESS_ID);
			if(StringUtils.isNotEmpty(addChngVO.getLastProcessedContract())){
				 contractId = Integer.parseInt(addChngVO.getLastProcessedContract());
			} else{
				 contractId = 0;
			}
			
			List<AutomatedAgent<?>> agents = new ArrayList<AutomatedAgent<?>>();
			agents.add(new CACProcessAgent(contractId,
							addChngVO.getCurrentStatus(),
							addChngVO.getLastProcessedStartTs(),
							addChngVO.getLastProcessedEndTs(),
							addChngVO.getCurrentProcessStartTs(),
							addChngVO.getCurrentProcessEndTs()));
			
			return agents;
		}

		/**
		 * This method will be called before data-driven activities defined in
		 * the AutomatedAgents and executes at the beginning of each run.
		 */
		public void preProcess() throws Exception {
		}

	}

	public static class CACProcessAgent extends AutomatedAgent<ContractAddressChangeVO> {

		private int lastProcessedContractId;
		private int contractId;
		
		private String lastProcessedStartTs;
		private String lastProcessedEndTs;
		
		private String currentProcessedStartTs;
		private String currentProcessedEndTs;
		
		private String currentStatus;

		// Constructor
		public CACProcessAgent(int contractId, 
				String currentStatus,String lastProcessedStartTs,
				String lastProcessedEndTs, String currentProcessedStartTs, 
				String currentProcessedEndTs) {
			this.contractId = contractId;
			this.lastProcessedStartTs = lastProcessedStartTs;
			this.lastProcessedEndTs = lastProcessedEndTs;
			this.currentStatus = currentStatus;
			this.lastProcessedContractId = contractId;
			this.currentProcessedStartTs = currentProcessedStartTs;
			this.currentProcessedEndTs = currentProcessedEndTs;
		}

		@Override
		protected String createJobDescription(ContractAddressChangeVO job) throws Exception {
			return new StringBuilder("cid ").append(job).toString();
		}

		/** 
		 * This method returns a list of objects that provide the data to
		 * process each job.
		 */
		
		@Override
		protected List<ContractAddressChangeVO> fetchJobList() throws Exception {
			
			List<ContractAddressChangeVO> addressVOs = EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).getEligibleContractsForAddressChanges(contractId,currentProcessedStartTs,currentProcessedEndTs,lastProcessedStartTs,lastProcessedEndTs,currentStatus);
			EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).insertProcessProgressStatusForContractAddressChanges(String.valueOf(contractId), PROCESS_ID,currentProcessedStartTs,currentProcessedEndTs);
			if(addressVOs.size() == 0){
				EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).updateProcessProgressStatusForContractAddressChanges(lastProcessedContractId, ProcessStatus.COMPLETED.getValue(), PROCESS_ID,currentProcessedStartTs,currentProcessedEndTs);
			}
			/*for(ContractAddressChangeVO vo :addressVOs){
				//TODO: System.out.println to be removed
				System.out.println("fetched last processed contract id is"+vo.getLastProcessedContract());
				System.out.println("fetched contract id is"+vo.getContractNumber());
				if(vo.isCurrentRunContract()){
					System.out.println("fetched getCurrentProcessStartTs is"+vo.getCurrentProcessStartTs());
					System.out.println("fetched getCurrentProcessEndTs is"+vo.getCurrentProcessEndTs());
				}else{
					System.out.println("fetched getLastProcessedStartTs is"+vo.getLastProcessedStartTs());
					System.out.println("fetched getLastProcessedEndTs is"+vo.getLastProcessedEndTs());
				}
			}
			System.out.println("List size is "+addressVOs.size());*/
			return addressVOs;
		}

		/**
		 * This method will be called for each job that fails with an irrecoverable application error
		 */
		@Override
		protected void invalidateJob(ContractAddressChangeVO job) throws Exception {
			job.setContractNumber(String.valueOf(lastProcessedContractId));
			job.setCurrentStatus(ProcessStatus.ACTIVE.getValue());
			job.setUpdatedStatus(ProcessStatus.ACTIVE.getValue());
			job.setLastProcessedStartTs(currentProcessedStartTs);
			job.setLastProcessedEndTs(currentProcessedEndTs);
			/*//TODO: System.out.println to be removed
			System.out.println("We are in invalidateJob");
			System.out.println("invalidateJob: fetched last processed contract id is"+lastProcessedContractId);
			System.out.println("invalidateJob: Status is"+ProcessStatus.ACTIVE.getValue());
			System.out.println("invalidateJob: fetched startTs is"+currentProcessedStartTs);
			System.out.println("invalidateJob: fetched endTs is"+currentProcessedEndTs);*/
			EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).updateProcessProgressStatusForContractAddressChanges(job,PROCESS_ID);
		}

		/**
		 * This method will be called for each job and is expected to either
		 * process it to completion or throw the appropriate Exception
		 */
		@Override
		protected void processJob(ContractAddressChangeVO addressChangeVO) throws Exception {
			ContractParticipantMailingAddressChangedEvent employeeAddressChangedEvent = new ContractParticipantMailingAddressChangedEvent();
			
			employeeAddressChangedEvent.setInitiator(EventServiceConstants.SYSTEM_USER_PROFILE_ID);
			employeeAddressChangedEvent.setContractId(Integer.parseInt(addressChangeVO.getContractNumber()));
			if(addressChangeVO.isCurrentRunContract()){
				employeeAddressChangedEvent.setStartTs(addressChangeVO.getCurrentProcessStartTs());
				employeeAddressChangedEvent.setEndTs(addressChangeVO.getCurrentProcessEndTs());
			}else{
				employeeAddressChangedEvent.setStartTs(addressChangeVO.getLastProcessedStartTs());
				employeeAddressChangedEvent.setEndTs(addressChangeVO.getLastProcessedEndTs());
			}
			
			try {
//				System.out.println(employeeAddressChangedEvent);
				EventClientUtility.getInstance(Environment.getInstance().getAppId()).prepareAndSendJMSMessage(employeeAddressChangedEvent);
				lastProcessedContractId = Integer.parseInt(addressChangeVO.getContractNumber());
				addressChangeVO.setContractNumber(String.valueOf(lastProcessedContractId));
				addressChangeVO.setLastProcessedContract(String.valueOf(lastProcessedContractId));
				addressChangeVO.setCurrentStatus(ProcessStatus.ACTIVE.getValue());
				addressChangeVO.setUpdatedStatus(ProcessStatus.ACTIVE.getValue());
				addressChangeVO.setLastProcessedStartTs(currentProcessedStartTs);
				addressChangeVO.setLastProcessedEndTs(currentProcessedEndTs);
				
//				System.out.println("Fetched contract is "+addressChangeVO.getContractNumber());
				EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).updateProcessProgressStatusForContractAddressChanges(addressChangeVO,PROCESS_ID);
			} catch (Exception exception) {
				EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).updateProcessProgressStatusForContractAddressChanges(addressChangeVO,PROCESS_ID);
			}
		}

		/**
		 * This method will be called after data-driven activities defined in
		 * the AutomatedAgents and executes at the end of each run.
		 * 
		 * @throws Exception
		 */
		protected void postProcess() throws Exception {
			/*//TODO: System.out.println to be removed
			System.out.println("We are in postProcess");
			System.out.println("postProcess: fetched last processed contract id is"+lastProcessedContractId);
			System.out.println("postProcess: Status is"+ProcessStatus.COMPLETED.getValue());
			System.out.println("postProcess: fetched startTs is"+currentProcessedStartTs);
			System.out.println("postProcess: fetched endTs is"+currentProcessedEndTs);*/
			EmployeeServiceDelegate.getInstance(CommonConstants.PS_APPLICATION_ID).updateProcessProgressStatusForContractAddressChanges(lastProcessedContractId, ProcessStatus.COMPLETED.getValue(), PROCESS_ID,currentProcessedStartTs,currentProcessedEndTs);		
		}
	}
}
