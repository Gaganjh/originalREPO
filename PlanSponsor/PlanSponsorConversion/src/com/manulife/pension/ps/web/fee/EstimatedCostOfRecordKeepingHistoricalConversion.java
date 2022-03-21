package com.manulife.pension.ps.web.fee;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.intware.dao.jdbc.SelectMultiValueQueryHandler;
import com.manulife.pension.delegate.FeeServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.fee.EstimatedCostOfRecordKeepingPeriodicProcess.EcrContextFacade;
import com.manulife.pension.ps.web.fee.EstimatedCostOfRecordKeepingPeriodicProcess.EstimatedCostOfRecordKeepingPeriodicAgent;
import com.manulife.pension.service.dao.DaoConstants.DataSourceJndiName;
import com.manulife.pension.service.fee.util.Constants.ContractFeeCodes;
import com.manulife.pension.service.fee.util.Constants.ContractFeeDetailType;
import com.manulife.pension.service.fee.util.ContractFeeDetail;
import com.manulife.pension.service.fee.util.exception.ContractNotApplicableExeption;
import com.manulife.pension.service.fee.valueobject.ClassType;
import com.manulife.pension.service.fee.valueobject.EstimatedCostOfRecordKeeping;
import com.manulife.pension.service.fee.valueobject.FeeData;
import com.manulife.pension.util.JdbcHelper;

public final class EstimatedCostOfRecordKeepingHistoricalConversion {
    
    private static final String PROPERTIES_FILE_NAME = "ecr-conversion.properties";
    private static final String PARM_APP_ID = "appId";
    private static final String PARM_DISCLOSURE_FILE_NAME = "disclosureFileName";
    private static final String PARM_FEE_FILE_NAME = "feeFileName";
    private static final String PARAM_REQ_FEE_CODES = "requiredFees";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    
    private final String applicationIdForLogging;
    private final String disclosureFileName;
    private final String feeFileName;
    private final String requiredFees;
    private final Date firstEffectiveDate;
    private final Date lastEffectiveDate;
    
    public static void main(final String[] args)
    throws Exception {
        
        DOMConfigurator.configure("log4j.xml");
        
        final int argCount = args.length;
        
        final Date startDate;
        if (argCount > 0) {
            startDate = DATE_FORMAT.parse(args[0]);
        } else {
            throw new IllegalArgumentException("Missing start date");
        }
        
        final Date endDate =
                argCount > 1
                ? DATE_FORMAT.parse(args[1])
                : startDate;
        
        new EcrMockContainer().setUp();
        
        final Properties prop = new Properties();
        prop.load(ClassLoader.class.getResourceAsStream("/" + PROPERTIES_FILE_NAME));
        
        try {
            
            new EstimatedCostOfRecordKeepingHistoricalConversion(
                    prop.getProperty(PARM_APP_ID),
                    prop.getProperty(PARM_DISCLOSURE_FILE_NAME),
                    prop.getProperty(PARM_FEE_FILE_NAME),
                    prop.getProperty(PARAM_REQ_FEE_CODES),
                    startDate,
                    endDate)
            .process();
            
        } catch (final Exception e) {
            
            Logger.getLogger(EstimatedCostOfRecordKeepingHistoricalConversion.class)
            .error(
                    ExceptionUtils.getFullStackTrace(e),
                    e);
            
        }
        
    }
    
    private EstimatedCostOfRecordKeepingHistoricalConversion(
            final String applicationIdForLogging,
            final String disclosureFileName,
            final String feeFileName,
            final String requiredFees,
            final Date firstEffectiveDate,
            final Date lastEffectiveDate) {
        
        if (StringUtils.isBlank(applicationIdForLogging)) {
            throw new IllegalArgumentException("Application ID for logging not specified");
        }
        
        if (StringUtils.isBlank(disclosureFileName)) {
            throw new IllegalArgumentException("Disclosure output file not specified");
        }
        
        if (StringUtils.isBlank(feeFileName)) {
            throw new IllegalArgumentException("Fee output file not specified");
        }
//        if (StringUtils.isBlank(requiredFees)) {
//        	throw new IllegalArgumentException("Required Fee  output file not specified");
//        }
        
        if (firstEffectiveDate.after(lastEffectiveDate)) {
            throw new IllegalArgumentException(
                    "Start date " + firstEffectiveDate +
                    " later than end date " + lastEffectiveDate);
        }
        
        this.applicationIdForLogging = applicationIdForLogging;
        
        this.disclosureFileName = disclosureFileName;
        this.feeFileName = feeFileName;
        this.requiredFees = requiredFees;
        this.firstEffectiveDate = firstEffectiveDate;
        this.lastEffectiveDate = lastEffectiveDate;
        
    }
    
    private final SimpleDateFormat FILE_TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMddhhmm");
    private final SimpleDateFormat EFFECTIVE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss.SSSSSS");
    private static final DecimalFormat FEE_FORMAT;
    
    static {
        FEE_FORMAT = new DecimalFormat("+000.0000;-000.0000");
        FEE_FORMAT.setRoundingMode(RoundingMode.DOWN);
    }
    
    private static BigDecimal roundForDatabase(final ContractFeeDetail detail) {
        return (detail.getComponentType() == ContractFeeDetailType.TOTAL) ? detail.getAmountValue(2) : detail.getAmountValue().setScale(4, RoundingMode.DOWN);
    }
    
    private void process()
    throws Exception {
        
        final Logger logger = Logger.getLogger(getClass());
        
        Date effectiveDate = firstEffectiveDate;
        do {
            
            if (logger.isInfoEnabled()) {
                
                logger.info("Processing effective date " + DATE_FORMAT.format(effectiveDate));
                
            }
            
            final String fileSuffix = "_" + DATE_FORMAT.format(effectiveDate) + "_" + FILE_TIMESTAMP_FORMAT.format(new Date()) + ".csv";
            final FileWriter disclosureWriter = new FileWriter(disclosureFileName + fileSuffix);
            final FileWriter feeWriter = new FileWriter(feeFileName + fileSuffix);
            
            final String formattedEffectiveDate = EFFECTIVE_DATE_FORMAT.format(effectiveDate);
            
            try {
                
                final EstimatedCostOfRecordKeepingPeriodicAgent agent =
                        new EstimatedCostOfRecordKeepingPeriodicAgent(
                                0,
                                -1,
                                effectiveDate,
                                new EcrContextFacade() {
                                    
                                    @Override
                                    public EstimatedCostOfRecordKeeping retrieveEstimatedCostOfRecordKeeping(int contractId, Date asOfDate) throws SystemException { return null; }
                                    
                                    @Override
                                    public void persistEstimatedCostOfRecordKeeping(EstimatedCostOfRecordKeeping ecr, boolean isPreAlignmentDate) throws SystemException {
                                        
                                        final String compoundKey = Integer.toString(ecr.getContractId()) + "," + formattedEffectiveDate + ",";
                                        final Date currentDate = new Date();
                                        
                                        try {
                                            boolean isFeeAvailable = false;
                                            
                                            for (final ContractFeeDetail detail : ecr.getRequiredContractFeesToStore(false)) {
                                            	boolean isHistoricalFee = (ContractFeeCodes.CONTRACT_COSTS_THIRDPARTY.getCode()
                                            			.equals(detail.getFeeCode())
                                            			|| ContractFeeCodes.EXCESS_REVENUE
                                            			.getCode().equals(detail.getFeeCode())
                                            			|| ContractFeeCodes.DOLLARBASED_THIRDPARTY_SERVICES
                                            			.getCode().equals(detail.getFeeCode())
                                            			|| ContractFeeCodes.THIRDPARTY_SERVICESANDMVE
                                            			.getCode().equals(detail.getFeeCode())
                                            			|| ContractFeeCodes.CREDITS_TO_PARTICIPANTS
                                            			.getCode().equals(detail.getFeeCode())
                                            			|| ContractFeeCodes.UNDERLAYING_FUNDNETCOST
                                            			.getCode().equals(detail.getFeeCode())
                                            			|| ContractFeeCodes.CREDITS_TO_FUNDPLAN_SERVICES
                                            			.getCode().equals(detail.getFeeCode())
                                            			|| ContractFeeCodes.MARKET_VALUE_EQUALIZER
                                            			.getCode().equals(detail.getFeeCode()));
                                            	
                                            	if (((StringUtils.isEmpty(requiredFees) || StringUtils.equalsIgnoreCase(requiredFees, "NEW")) && isHistoricalFee) || (StringUtils.equalsIgnoreCase(requiredFees, "OLD") && !isHistoricalFee)) {  
                                            		
                                            		try {
                                            			
                                            			final StringBuilder contractCostBuilder =
                                            				new StringBuilder()
                                            			.append(compoundKey)
                                            			.append("\"")
                                            			.append(detail.getFeeTypeCode())
                                            			.append("\",")
                                            			.append(FEE_FORMAT.format(roundForDatabase(detail)))
                                            			.append(",\"")
                                            			.append(StringUtils.stripToEmpty(detail.getRecoveryMethod()))
                                            			.append("\"\r\n");

                                            			feeWriter.write(contractCostBuilder.toString());
                                            			
                                            			isFeeAvailable = true;
                                            			
                                            		} catch (final SystemException se) {

                                            			throw new IllegalStateException(ExceptionUtils.getFullStackTrace(se));

                                            		}
                                            	}

                                            }
                                            if(isFeeAvailable){
                                            	
	                                            final StringBuilder disclosureBuilder =
	                                                new StringBuilder()
	                                                .append(compoundKey)
	                                                .append("\"")
	                                                .append(TIMESTAMP_FORMAT.format(currentDate))
	                                                .append("\",\"")
	                                                .append(TIMESTAMP_FORMAT.format(currentDate))
	                                                .append("\"\r\n");
	                                            
	                                            disclosureWriter.write(disclosureBuilder.toString());
	                                            
                                            }
                                            
                                        } catch (final IOException ioe) {
                                            
                                            throw new IllegalStateException(ExceptionUtils.getFullStackTrace(ioe));
                                            
                                        }
                                        
                                    }
                                    
                                    private final String SQL_SELECT_QUALIFYING_CONTRACTS_ESTIMATED_COST_OF_RECORDKEEPING =
                                    	"SELECT DISTINCT( CONTRACT_ID ) "
                                    	+ "FROM   PSW100.CONTRACT_FEE_DISCLOSURE_HIST "
                                    	+ "WHERE  EFFECTIVE_DATE = ? "
                                    	+ "ORDER  BY CONTRACT_ID "
                                    	+ "FOR FETCH ONLY";
    
                                    @Override
                                    public List<Integer> getContractsRequiringPersistenceOfEstimatedCostOfRecordKeeping(Date feeDataEffectiveDate, int maxRecords) throws SystemException {
                                        
                                        final SelectMultiValueQueryHandler handler;
                                        final Object[] results;
                                        try {
                                            
                                            handler =
                                                    new SelectMultiValueQueryHandler(
                                                            JdbcHelper.getCachedDataSource(DataSourceJndiName.CUSTOMER_SERVICE),
                                                            SQL_SELECT_QUALIFYING_CONTRACTS_ESTIMATED_COST_OF_RECORDKEEPING,
                                                            new int[] { Types.DATE },
                                                            Integer.class);
                                            
                                            final Object[] parameters = new Object[] { feeDataEffectiveDate };
                                            
                                            results = (Object[]) handler.select(parameters);
                                            
                                        } catch (final Exception e) {
                                            
                                            throw new SystemException(
                                                    e,
                                                    e.getMessage() +
                                                    " with effective date " + feeDataEffectiveDate);
                                            
                                        }
                                        
                                        final ArrayList<Integer> contractList = new ArrayList<Integer>();
                                        for (final Object contractId : results) {
                                            contractList.add((Integer) contractId);
                                        }
                                        return contractList;
                                        
                                    }
                                    
                                    @Override
                                    public int countConsecutiveEcrDailyRunsWithErrors(int contractId, int maxErrorRuns) throws SystemException { return 0; }
                                    
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
                                    
                                });
                
                List<Integer> jobs = Collections.emptyList();
                
                try {
                    
                    jobs = agent.fetchJobList();
                    
                } catch (final Exception e) {
                    logger.error(e.getMessage(), e);
                }
                
                for (final Integer job : jobs) {
                    
                    try {
                        
                        agent.processJob(job);
                        
                    } catch (final Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    
                }
                
            } finally {
                
                feeWriter.close();
                disclosureWriter.close();
                
            }
            
            final Calendar cal = GregorianCalendar.getInstance();
            cal.setTime(effectiveDate);
            cal.add(Calendar.MONTH, 1);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            effectiveDate = cal.getTime();
            
        } while (! effectiveDate.after(lastEffectiveDate));
        
        logger.info("Processing completed");
        
    }
    
}
