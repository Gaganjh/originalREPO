package com.manulife.pension.ps.web.fee;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.intware.batch.process.PeriodicProcess;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.Constants;
import com.manulife.pension.service.fee.util.Constants.ContractFeeCodes;
import com.manulife.pension.service.fee.util.ContractFeeTransformer;
import com.manulife.pension.service.fee.util.ContractRecordKeepingCharges;
import com.manulife.pension.service.fee.util.ContractRecordKeepingCharges.ContractRecordKeepingChargesUtility;
import com.manulife.pension.service.fee.util.ContractRecordKeepingCharges.EstimatedCostOfRecordKeepingUtility;
import com.manulife.pension.service.fee.util.exception.ContractNotApplicableExeption;
import com.manulife.pension.service.fee.valueobject.ClassType;
import com.manulife.pension.service.fee.valueobject.ContractClass;
import com.manulife.pension.service.fee.valueobject.ContractFee;
import com.manulife.pension.service.fee.valueobject.EstimatedCostOfRecordKeeping;
import com.manulife.pension.service.fee.valueobject.FeeData;
import com.manulife.pension.service.notification.handlers.vo.EmailInfoVO;
import com.manulife.pension.util.BaseEnvironment;
import com.manulife.pension.util.periodicprocess.AutomatedAgent;
import com.manulife.pension.util.periodicprocess.DataDrivenPeriodicProcess;
import com.manulife.pension.util.periodicprocess.DataDrivenPeriodicProcessRun;
import com.manulife.pension.util.periodicprocess.SupportNotifier;

public final class EstimatedCostOfRecordKeepingPeriodicProcess
implements PeriodicProcess {
	
	private static final Logger LOGGER = Logger.getLogger(EstimatedCostOfRecordKeepingPeriodicProcess.class);
	private static final int STALE_NOTIFICATION_AGE_IN_DAYS = 45;
	private static final DataDrivenPeriodicProcess pp;
	static final SupportNotifier notifier;
	
	private static final String CLASS_NAME = "EstimatedCostOfRecordKeepingPeriodicProcess";

	// Block for instantiating DDPP.
	static {
		
		Properties supportProperties = new Properties();
		
		try {
			InputStream input = ClassLoader.getSystemResourceAsStream("ecrpp.properties"); 
			if (input != null) {
				try {
					supportProperties.load(input);
				} finally {
					input.close();
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Unable to load periodic process properties from ecrpp.properties file.",
							exception);
		}

		notifier = new SupportNotifier() {
			
			public void sendNotification(String subject, String body) throws SystemException {
				ConfigurableEmailNotificationHandler pageEcomm = new ConfigurableEmailNotificationHandler("ecrFailureNotificationList");
				EmailInfoVO message = new EmailInfoVO();
				message.setSubject(subject);
				message.setBody(body);
				pageEcomm.handleNotification("PS", message);
			}

		};
		
		pp =
		        DataDrivenPeriodicProcess.createWithErrorsInRunHandling(
		                EstimatedCostOfRecordKeepingPeriodicProcess.class,
		                EstimatedCostOfRecordKeepingPeriodicProcessRun.class,
		                supportProperties,
		                notifier,
		                true,
		                0,
		                false);
		
	}

	public EstimatedCostOfRecordKeepingPeriodicProcess() throws Throwable {
		pp.instantiateRun();
	}

	public void setMaxRecords(Integer maxRecords)throws Throwable{
		pp.setProperty("maxRecords", maxRecords);
	}
	
	public void setMaxErrorRuns(Integer maxErrorRuns)throws Throwable{
		pp.setProperty("maxErrorRuns", maxErrorRuns);
	}
	
	public void execute() {
		pp.execute();
	}

	public static class EstimatedCostOfRecordKeepingPeriodicProcessRun implements DataDrivenPeriodicProcessRun {

		private int maxRecords;
		private int maxErrorRuns;
		
		public void setMaxRecords(Integer maxRecords) {
			if(maxRecords != null){
				this.maxRecords = maxRecords;
			}
		}
		
		public void setMaxErrorRuns(Integer maxErrorRuns) {
			if(maxErrorRuns != null){
				this.maxErrorRuns = maxErrorRuns;
			}
		}
		
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
		    
            final String applicationIdForLogging = new BaseEnvironment().getAppId();
            final Date latestFeeDataEffectiveDate = FeeServiceDelegate.getInstance(applicationIdForLogging).selectCurrentFeeEffectiveDate();
            
            // bring the max ProcessProgressStatus with completed status
            // If the effective date from ProcessProgressStatus matches the latestFeeDataEffectiveDate
            // 	then applicable for PN status
            //		if current date, ProcessProgressStatus date month and year is all same - don't do any thing 
            //		else insert PN record
            //	else if latestFeeDataEffectiveDate is the recent run agent
            String currentStatus = FeeServiceDelegate.getInstance(applicationIdForLogging).getProcessProgressStatus(Constants.ESTIMATED_COST_OF_RK_PERIODIC_PROCESS, latestFeeDataEffectiveDate);
            
            boolean processInProgress = false;
            
            if(StringUtils.equals(currentStatus, "IP")) {
            	processInProgress = true;
			} 
            
            
            Date lastRunDate = FeeServiceDelegate.getInstance(applicationIdForLogging).getMaxProcessStatusDate(Constants.ESTIMATED_COST_OF_RK_PERIODIC_PROCESS);
            
            // Current Date
            Date currentDate = EnvironmentServiceDelegate.getInstance(Constants.PS_APPLICATION_ID).getAsOfDate();
           
            final List<AutomatedAgent<?>> agents = new ArrayList<AutomatedAgent<?>>();
            
            if(!processInProgress) {
            	
	            if (latestFeeDataEffectiveDate.compareTo(lastRunDate) == 0) {

	            	// Send a notification if we still haven't processed the month end that is more than 45 days from the currentDate.
	            	if (DateUtils.addDays(latestFeeDataEffectiveDate, STALE_NOTIFICATION_AGE_IN_DAYS).before(currentDate)) {
	                    notifier.sendNotification(
	                            CLASS_NAME + " is waiting to process a month end greater than " + STALE_NOTIFICATION_AGE_IN_DAYS + " days ago",
	                            "Please check if AIC fund expense ratios have been uploaded and once confirmed, ensure that this job is re-run.  This job has auditing implications.");
	                }
	                
	            } else if (latestFeeDataEffectiveDate.compareTo(lastRunDate) > 0 ) {
	            	// Set process Progress status to PN, or update it to IP
	            	FeeServiceDelegate.getInstance(applicationIdForLogging).insertOrUpdateProgressStatus(Constants.ESTIMATED_COST_OF_RK_PERIODIC_PROCESS, "IP", latestFeeDataEffectiveDate);
	            	agents.add(new EstimatedCostOfRecordKeepingPeriodicAgent(
	                        maxRecords,
	                        maxErrorRuns,
	                        latestFeeDataEffectiveDate,
	                        new EcrContextFacade() {
	                            
	                            @Override
	                            public EstimatedCostOfRecordKeeping retrieveEstimatedCostOfRecordKeeping(int contractId, Date asOfDate) throws SystemException {
	                                return FeeServiceDelegate.getInstance(applicationIdForLogging).retrieveEstimatedCostOfRecordKeeping(contractId, asOfDate);
	                            }
	                            
	                            @Override
	                            public void persistEstimatedCostOfRecordKeeping(EstimatedCostOfRecordKeeping ecr, boolean isPreAlignmentDate) throws SystemException {
	                                FeeServiceDelegate.getInstance(applicationIdForLogging).persistEstimatedCostOfRecordKeeping(ecr, isPreAlignmentDate);
	                            }
	                            
	                            @Override
	                            public List<Integer> getContractsRequiringPersistenceOfEstimatedCostOfRecordKeeping(Date feeDataEffectiveDate, int maxRecords) throws SystemException {
	                                return FeeServiceDelegate.getInstance(applicationIdForLogging).getContractsRequiringPersistenceOfEstimatedCostOfRecordKeeping(feeDataEffectiveDate, maxRecords);                        }
	                            
	                            @Override
	                            public int countConsecutiveEcrDailyRunsWithErrors(int contractId, int maxErrorRuns) throws SystemException {
	                                return FeeServiceDelegate.getInstance(applicationIdForLogging).countConsecutiveEcrDailyRunsWithErrors(contractId, maxErrorRuns);
	                            }
	                            
	                            @Override
	                            public FeeData getPlanCharacteristicsDetails(int contractId, Date feeDataEffectiveDate) throws SystemException, ContractNotApplicableExeption {
	                                return FeeServiceDelegate.getInstance(applicationIdForLogging).getPlanCharacteristicsDetails(contractId, feeDataEffectiveDate);
	                            }
	
								@Override
								public List<ClassType> getClassTypes(Date asOfDate) throws SystemException {
									return FeeServiceDelegate.getInstance(applicationIdForLogging).getClassTypes(asOfDate);
								}
								
								@Override
								public void insertOrUpdateProgressStatus(String processType, String status,  Date effDate) throws SystemException {
									FeeServiceDelegate.getInstance(applicationIdForLogging).insertOrUpdateProgressStatus(processType, status,  effDate);
								}

								@Override
								public void insertOrUpdatePlanReviewJobStatus(String jobDesc, String status, Date effDate) throws SystemException {
									FeeServiceDelegate.getInstance(applicationIdForLogging).insertOrUpdatePlanReviewJobStatus(jobDesc, status, effDate);
								}
	                            
	                        }));
	            } else {
	            	
	            	notifier.sendNotification(
	                        CLASS_NAME + " is processing for the month end " + latestFeeDataEffectiveDate + ", but seems to less than the last run date " + lastRunDate + " ",
	                        "Please check if AIC fund expense ratios have been uploaded properly there seems to be a wrong data in AIC");
	                
	            }
            } else {
            	
            	notifier.sendNotification(
                        CLASS_NAME + " is processing for the month end " + latestFeeDataEffectiveDate + ", but there seems to be a "+ CLASS_NAME +" triggered earlier and running for " + lastRunDate + " ",
                        "Please check if the Process is still running, as it should have completed by now.");
            }
            
			return agents;
			
		}

		/**
		 * This method will be called before data-driven activities defined in
		 * the AutomatedAgents and executes at the beginning of each run.
		 */
		public void preProcess() throws Exception {
		}

	}
	
	public interface EcrContextFacade
	extends EstimatedCostOfRecordKeepingUtility {
	    
	    List<Integer> getContractsRequiringPersistenceOfEstimatedCostOfRecordKeeping(Date feeDataEffectiveDate, int maxRecords) throws SystemException;
	    int countConsecutiveEcrDailyRunsWithErrors(int contractId, int maxErrorRuns) throws SystemException;
	    FeeData getPlanCharacteristicsDetails(int contractId, Date feeDataEffectiveDate) throws SystemException, ContractNotApplicableExeption;
	    void insertOrUpdateProgressStatus(String processType, String status,  Date effDate) throws SystemException;
	    void insertOrUpdatePlanReviewJobStatus(String jobDesc, String status,  Date effDate) throws SystemException;
	    
	}
	
	public static class EstimatedCostOfRecordKeepingPeriodicAgent
	extends AutomatedAgent<Integer> {
		
        private final int maxRecords;
        private final int maxErrorRuns;
        private final Date feeDataEffectiveDate;
        private final EcrContextFacade context;
        
        private boolean isErrorRunCounted;

		// Constructor
		public EstimatedCostOfRecordKeepingPeriodicAgent(
		        final int maxRecords,
		        final int maxErrorRuns,
		        final Date feeDataEffectiveDate,
		        final EcrContextFacade context) {
		    this.maxRecords = maxRecords;
			this.maxErrorRuns = maxErrorRuns;
            this.feeDataEffectiveDate = feeDataEffectiveDate;
            this.context = context;
		}

		@Override
		protected String createJobDescription(Integer job) throws Exception {
			return new StringBuilder("cid ").append(job).toString();
		}

		/** 
		 * This method returns a list of objects that provide the data to
		 * process each job.
		 */

		@Override
		protected List<Integer> fetchJobList() throws Exception {
			
			List<Integer> contractIds = new ArrayList<Integer>();
			contractIds = context.getContractsRequiringPersistenceOfEstimatedCostOfRecordKeeping(feeDataEffectiveDate, maxRecords);
			
			if (contractIds.size() == maxRecords && maxRecords > 0) {
			    notifier.sendNotification(
			            CLASS_NAME + " is processing at maximum capacity",
			            "The PeriodicProcess is processing at capacity, currently configured at " + maxRecords +
			            ".  Please ensure there will be another run for data effective date " + feeDataEffectiveDate +
			            " before the following month end data is uploaded.");
			}
			
			return contractIds;
			
		}

		/**
		 * This method will be called for each job that fails with an irrecoverable application error
		 */
		@Override
		protected void invalidateJob(Integer job) throws Exception {
            
		    if (! isErrorRunCounted) {
		        
                final boolean isMaxFailureDaysReached =
                        context.countConsecutiveEcrDailyRunsWithErrors(job.intValue(), maxErrorRuns)
                        >= maxErrorRuns;
                        
                isErrorRunCounted = true;
                
                if (isMaxFailureDaysReached) {
                    
                    notifier.sendNotification(
                            CLASS_NAME + " has experienced " + maxErrorRuns + " consecutive runs with errors" ,
                            "Please check application log.");
                    
                }
                    
            }
            
		}
		
		/**
		 * This method will be called for each job and is expected to either
		 * process it to completion or throw the appropriate Exception
		 */
		@Override
		protected void processJob(final Integer contractId) throws Exception {
		    
            final FeeData feeData = context.getPlanCharacteristicsDetails(contractId, feeDataEffectiveDate);
            
            final ContractFeeTransformer fee = new ContractFeeTransformer(feeData.getContractFee().getContractFeeDetails());
            
            final ContractFee fees = feeData.getContractFee();
            final ContractClass contractClass = feeData.getContractDetails().getContractClass();
            final BigDecimal averageRevenueFromSubAccount =
                    (contractClass != null && contractClass.isClassZero())
                    ? ((BigDecimal) ObjectUtils.defaultIfNull(fees.getUnroundedAverageJhRevenue(), BigDecimal.ZERO)).negate()
                    :
                        ((BigDecimal) ObjectUtils.defaultIfNull(fees.getUnroundedAverageAmcRate(), BigDecimal.ZERO))
                        .add((BigDecimal) ObjectUtils.defaultIfNull(fees.getUnroundedAverageSsfRate(), BigDecimal.ZERO));
                    
            final Date runDate = feeDataEffectiveDate;    
		    ContractRecordKeepingCharges.calculateAndPersistContractEstimatedCostOfRecordKeeping(
		            contractId.intValue(),
		            feeDataEffectiveDate,
                    fee,
                    feeData.getContractDetails(),
                    feeData.getContractFee().getUnroundedAverageAmcRate(),
                    feeData.getContractFee().getAverageJHRevenue(),
                    averageRevenueFromSubAccount,
                    feeData.getFundDataAsOfDate(),
                    context,
                    
                    new ContractRecordKeepingChargesUtility() {
		            	
		            	Map<ContractFeeCodes, Object> feeMap = null;
                    	
                    	@Override
							public Map<ContractFeeCodes, Object> getOtherContractFeeDetails()
									throws SystemException {
								if (feeMap == null) {
									feeMap = FeeServiceDelegate
											.getInstance("PS")
											.getOtherContractFeeDetails(
													contractId,
													runDate);
								}
								return feeMap;
							}
		            	
						@Override
						public String getBusinessParam(String paramName)
								throws SystemException {
							return EnvironmentServiceDelegate.getInstance()
									.getBusinessParam(paramName);
						}
					});
		    
	   }

		/**
		 * This method will be called after data-driven activities defined in
		 * the AutomatedAgents and executes at the end of each run.
		 * 
		 * @throws Exception
		 */
		protected void postProcess() throws Exception {
			// update or Insert that the process is completed.
			if (! isErrorRunCounted) {
				context.insertOrUpdateProgressStatus(Constants.ESTIMATED_COST_OF_RK_PERIODIC_PROCESS, "CP", feeDataEffectiveDate);
				context.insertOrUpdatePlanReviewJobStatus(Constants.FEE_DATA_PLAN_REVIEW_JOB, Constants.YES, feeDataEffectiveDate);
			} else {
				context.insertOrUpdateProgressStatus(Constants.ESTIMATED_COST_OF_RK_PERIODIC_PROCESS, "FL", feeDataEffectiveDate);
				context.insertOrUpdatePlanReviewJobStatus(Constants.FEE_DATA_PLAN_REVIEW_JOB, Constants.NO, feeDataEffectiveDate);
			}
	    }
		
	}
}