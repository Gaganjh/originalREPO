package com.manulife.pension.ps.service.submission.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLInsertHandler;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanListQueryHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.intware.dao.jdbc.SelectSingleOrNoValueQueryHandler;
import com.intware.dao.jdbc.SelectSingleValueQueryHandler;
import com.manulife.pension.exception.ContractDoesNotExistException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.common.util.Constants;
import com.manulife.pension.ps.service.report.submission.valueobject.CensusSubmissionReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.SubmissionHistoryReportData;
import com.manulife.pension.ps.service.report.submission.valueobject.TPAVestingSubmissionReportData;
import com.manulife.pension.ps.service.submission.dao.SubmissionConstants.Census;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.CensusSubmissionItem;
import com.manulife.pension.ps.service.submission.valueobject.TPAVestingSubmissionDetailsVO;
import com.manulife.pension.ps.service.submission.valueobject.ReportDataErrors;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionHistoryItem;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.contract.dao.ContractServiceFeatureDAO;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.contract.valueobject.ContractDetailsVO;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeValidationErrorCode;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.submission.SubmissionError;
import com.manulife.pension.submission.util.SubmissionErrorComparator;

/**
 * @author maceadi
 *
 */
public class TPAVestingSubmissionDAO extends BaseDatabaseDAO {

	private static final String className = TPAVestingSubmissionDAO.class.getName();
	private static final Logger logger = Logger.getLogger(TPAVestingSubmissionDAO.class);
    
    public static final String SQL_SELECT_CONTRACT_DATA = 
         "  SELECT "
        +"      CS.CONTRACT_ID as contractId, " 
        +"      CS.CONTRACT_NAME as contractName, " 
        +"      CS.THIRD_PARTY_ADMIN_ID as tpaFirmId, " 
        +"      THIRD_PARTY_ADMIN_NAME as tpaFirmName, " 
        +"      PLAN_REPORTING_YEAR_END_DATE as planYearEnd, " 
        +"      temp_calc.LAST_UPDATED_TS as calcOnlineUpdateDate, "
        +"      temp_calc.LAST_UPDATED_USER_ID as calcOnlineUserId, "
        +"      temp_prov.LAST_UPDATED_TS as provOnlineUpdateDate, "
        +"      temp_prov.LAST_UPDATED_USER_ID as provOnlineUserId "
        +"  FROM " 
        +       PLAN_SPONSOR_SCHEMA_NAME + "CONTRACT_CS CS "
        +"      JOIN " +  PLAN_SPONSOR_SCHEMA_NAME + "THIRD_PARTY_ADMINISTRATOR TPA "
        +"      ON CS.THIRD_PARTY_ADMIN_ID = TPA.THIRD_PARTY_ADMIN_ID "
        +"      JOIN " +  PLAN_SPONSOR_SCHEMA_NAME + "PRODUCT_CS P "
        +"      ON CS.PRODUCT_ID = P.PRODUCT_ID " 
        +"      LEFT JOIN (select * from "
        +                  PLAN_SPONSOR_SCHEMA_NAME + "EMPLOYEE_VESTING_PARAMETER EVP "
        +"                 where (EVP.CONTRACT_ID,EVP.LAST_UPDATED_TS) in " 
        +"                              (select CONTRACT_ID, max(LAST_UPDATED_TS) "
        +"                               from " + PLAN_SPONSOR_SCHEMA_NAME + "EMPLOYEE_VESTING_PARAMETER "
        +"                               where CONTRACT_ID in (@@@) "
        +"                               and   PARAMETER_NAME in ('PREVIOUS_YRS_OF_SERVICE','FULLY_VESTED_IND') "
        +"                               and   SOURCE_CHANNEL_CODE in ('PE','PC','PS') "
        +"                               group by CONTRACT_ID) "
        +"                ) as temp_calc "
        +"      ON CS.CONTRACT_ID = temp_calc.CONTRACT_ID "  
        +"      LEFT JOIN (select * from "
        +                  PLAN_SPONSOR_SCHEMA_NAME + "EMPLOYEE_MONEY_TYPE_VESTING_PCT EMTVP "
        +"                 where (EMTVP.CONTRACT_ID,EMTVP.LAST_UPDATED_TS) in "
        +"                              (select CONTRACT_ID, max(LAST_UPDATED_TS) "
        +"                               from " + PLAN_SPONSOR_SCHEMA_NAME + "EMPLOYEE_MONEY_TYPE_VESTING_PCT "
        +"                               where CONTRACT_ID in (@@@) "
        +"                               and   SOURCE_CHANNEL_CODE in ('PE','PC','PS') "
        +"                               group by CONTRACT_ID) "
        +"                ) as temp_prov "
        +"      ON CS.CONTRACT_ID = temp_prov.CONTRACT_ID "
        +"  WHERE CS.CONTRACT_STATUS_CODE in ('PS','DC','PC','CA','AC','CF') "
        +"  AND P.PRODUCT_CATEGORY_CODE = 'AL' "
        +"  AND CS.PRODUCT_ID not in ('DB06','DBNY06') " 
        +"  AND CS.CONTRACT_ID in (@@@) ";
    
    public static final String SQL_SELECT_SUBMISSION_DATA =
         "  SELECT "
        +"      CONTRACT_ID as contractId, " 
        +"      PROCESS_STATUS_CODE as lastSubmissionStatus, "
        +"      LAST_UPDATED_TS as lastSubmissionDate, "
        +"      SUBMISSION_ID as submissionId, "
        +"      (select min(case    when locate('VC',ev1.error_cond_string) = 0 "
        +"                          then date(  substr(replace(percentage_effective_date,'/',''),5,4) || '-' || "                       
        +"                                      substr(replace(percentage_effective_date,'/',''),1,2) || '-' || "                       
        +"                                      substr(replace(percentage_effective_date,'/',''),3,2)) "
        +"                          else null "   
        +"                  end)  "        
        +"      from stp100.EMPLOYEE_VESTING ev1 "        
        +"      where ev1.submission_id = sc.submission_id ) as earliestProvidedDate, "
        +"      (select max(case    when locate('VC',ev2.error_cond_string) = 0 "
        +"                          then date(  substr(replace(percentage_effective_date,'/',''),5,4) || '-' || "                       
        +"                                      substr(replace(percentage_effective_date,'/',''),1,2) || '-' || "                       
        +"                                      substr(replace(percentage_effective_date,'/',''),3,2)) "
        +"                          else null "   
        +"                  end)  " 
        +"       from stp100.EMPLOYEE_VESTING ev2 "
        +"       where ev2.submission_id = sc.submission_id ) as latestProvidedDate, "
        +"      (select min(case    when locate('VC',ev3.error_cond_string) = 0 "
        +"                          then date(  substr(replace(vested_yrs_of_service_eff_dt,'/',''),5,4) || '-' || "                       
        +"                                      substr(replace(vested_yrs_of_service_eff_dt,'/',''),1,2) || '-' || "                       
        +"                                      substr(replace(vested_yrs_of_service_eff_dt,'/',''),3,2)) "
        +"                          else null "
        +"                  end)  "      
        +"       from stp100.EMPLOYEE_VESTING ev3 "
        +"       where ev3.submission_id = sc.submission_id ) as earliestCalculatedDate, "
        +"      (select max(case    when locate('VC',ev4.error_cond_string) = 0 "
        +"                          then date(  substr(replace(vested_yrs_of_service_eff_dt,'/',''),5,4) || '-' || "                       
        +"                                      substr(replace(vested_yrs_of_service_eff_dt,'/',''),1,2) || '-' || "                       
        +"                                      substr(replace(vested_yrs_of_service_eff_dt,'/',''),3,2)) "
        +"                          else null "
        +"                  end)  "   
        +"       from stp100.EMPLOYEE_VESTING ev4 "
        +"       where ev4.submission_id = sc.submission_id ) as latestCalculatedDate "     
        +"  FROM stp100.SUBMISSION_CASE sc "
        +"  WHERE contract_id in (@@@) "
        +"  AND submission_case_type_code = 'V' "
        +"  AND (contract_id, last_updated_ts) in "
        +"              (select contract_id, max(last_updated_ts) "
        +"               from stp100.SUBMISSION_CASE "
        +"               where contract_id in (@@@) "
        +"               and submission_case_type_code = 'V' "
        +"               group by contract_id) "
        +"  FOR FETCH ONLY";
         
        
    public static final String TPA_PLAN_STAFF_ACCESS_CLAUSE =
        "   AND TPA_STAFF_PLAN_IND != 'Y' ";
       
    public static final String CONTRACT_SITE_CLAUSE =
        "   AND CS.MANULIFE_COMPANY_ID = ";
    
    public static final String ORDER_BY_CLAUSE = 
        "   ORDER BY CS.THIRD_PARTY_ADMIN_ID, CS.CONTRACT_ID ";
    
    public static final String FOR_FETCH_ONLY = "  FOR FETCH ONLY";
    
    /**
     * Retrieve latest vesting submission for a given list of contracts
     * @param ReportCriteria    Contains the list of contracts,
     *                          the user plan staff access flag,
     *                          the PSW site (NY or US)
     * @return ReportData       The report data value object
     */
    public static TPAVestingSubmissionReportData getTPAVestingSubmissions(ReportCriteria criteria)
            throws SystemException {

        logger.debug("entry -> getTPAVestingSubmissions");
        logger.debug("Report criteria -> " + criteria.toString());
        
        List onlineDataList = new ArrayList<TPAVestingSubmissionDetailsVO>();
        List submissionDataList = new ArrayList<TPAVestingSubmissionDetailsVO>();
        TreeMap<BigDecimal,TPAVestingSubmissionDetailsVO> tpaReportData = new TreeMap<BigDecimal,TPAVestingSubmissionDetailsVO>();

        TPAVestingSubmissionReportData reportDataVO =
            new TPAVestingSubmissionReportData(criteria, 0);
        
        // get contract list
        List<BigDecimal> contractList =
            (List<BigDecimal>) criteria.getFilterValue(TPAVestingSubmissionReportData.FILTER_CONTRACT_LIST);

        // get user plan staff access flag
        boolean planStaffAccessFlag = 
            (Boolean) criteria.getFilterValue(TPAVestingSubmissionReportData.FILTER_PLAN_STAFF_PERMISSION);
        
        // get PSW site location (019 vs. 094)
        String siteLocation = 
            (String) criteria.getFilterValue(TPAVestingSubmissionReportData.FILTER_PSW_SITE);

        // retrieve online data 
        List params = new ArrayList();
                
        // filter out contracts
        String commaDelimitedString = convertListToCommaDelimitedString(contractList);
        if (commaDelimitedString.length() == 0) {
            commaDelimitedString = "0";
        }
        StringBuffer onlineQuery = new StringBuffer(SQL_SELECT_CONTRACT_DATA.replaceAll("@@@", commaDelimitedString));
        
        // if user does not have plan staff access, filter out the plan staff contracts
        if (!planStaffAccessFlag) {
            onlineQuery.append(TPA_PLAN_STAFF_ACCESS_CLAUSE);
        }
        
        // filter out the PSW site
        onlineQuery.append(CONTRACT_SITE_CLAUSE)
        .append("'").append(siteLocation).append("'");
        
        onlineQuery.append(ORDER_BY_CLAUSE).append(FOR_FETCH_ONLY);
        
        SelectBeanListQueryHandler onlineHandler = new SelectBeanListQueryHandler(
                CUSTOMER_DATA_SOURCE_NAME, onlineQuery.toString(), TPAVestingSubmissionDetailsVO.class);

        try {
            logger.debug("Executing Prepared SQL Statment: " + onlineQuery);
            onlineDataList = (List<TPAVestingSubmissionDetailsVO>) onlineHandler.select(params.toArray());
        } catch (DAOException e) {
            throw handleDAOException(e, className, "getTPAVestingSubmissions",
                    "Problem occurred prepared call: " + onlineQuery + " with criteria: " + criteria);
        }
        
        // if any contracts found
        if (onlineDataList.size() > 0) {
        
            for (TPAVestingSubmissionDetailsVO onlineVO : (List<TPAVestingSubmissionDetailsVO>)onlineDataList) {
                tpaReportData.put(onlineVO.getContractId(), onlineVO);
            }
            
            List<BigDecimal> validContractList = new ArrayList(tpaReportData.keySet());
            
            // filter out contracts
            String validCommaDelimitedString = convertListToCommaDelimitedString(validContractList);
            
            // retrieve submission data using the validContractList
            StringBuffer subQuery = new StringBuffer(SQL_SELECT_SUBMISSION_DATA.replaceAll("@@@", validCommaDelimitedString));
            
            SelectBeanListQueryHandler subHandler = new SelectBeanListQueryHandler(
                    SUBMISSION_DATA_SOURCE_NAME, subQuery.toString(), TPAVestingSubmissionDetailsVO.class);
    
            try {
                logger.debug("Executing Prepared SQL Statment: " + subQuery);
                submissionDataList = (List<TPAVestingSubmissionDetailsVO>) subHandler.select(params.toArray());
            } catch (DAOException e) {
                throw handleDAOException(e, className, "getTPAVestingSubmissions",
                        "Problem occurred prepared call: " + subQuery + " with criteria: " + criteria);
            }
            
            for (TPAVestingSubmissionDetailsVO submissionVO : (List<TPAVestingSubmissionDetailsVO>)submissionDataList) {
                TPAVestingSubmissionDetailsVO onlineVO = (TPAVestingSubmissionDetailsVO)tpaReportData.get(submissionVO.getContractId());
                onlineVO.setLastSubmissionDate(submissionVO.getLastSubmissionDate());
                onlineVO.setSubmissionId(submissionVO.getSubmissionId());
                onlineVO.setLastSubmissionStatus(submissionVO.getLastSubmissionStatus());
                onlineVO.setEarliestProvidedDate(submissionVO.getEarliestProvidedDate());
                onlineVO.setLatestProvidedDate(submissionVO.getLatestProvidedDate());
                onlineVO.setEarliestCalculatedDate(submissionVO.getEarliestCalculatedDate());
                onlineVO.setLatestCalculatedDate(submissionVO.getLatestCalculatedDate());
                
                try {
                    ContractServiceFeature csf = ContractServiceFeatureDAO.getContractServiceFeatureByTimestamp(
                            submissionVO.getContractId().intValue(), 
                            new Timestamp(submissionVO.getLastSubmissionDate().getTime()), 
                            ServiceFeatureConstants.VESTING_PERCENTAGE_FEATURE);
                    
                    if (csf != null) {
                        onlineVO.setVestingServiceFeature(csf.getValue());
                    }
                    
                    
                } catch (ContractDoesNotExistException ce) {
                    throw new SystemException(ce, className,
                            "getTPAVestingSubmissions",
                            "Problem occurred when retrieving historical Vesting CSF "
                                    + " for contractNumber: " + submissionVO.getContractId());
                }
                
                // if no vesting submission was ever received or the last vesting submission was received 
                // after the latest online update, ignore and blank out the online update date and userId 
                if (onlineVO.getLastSubmissionDate() == null || (onlineVO.getOnlineUpdateDate() != null &&
                    onlineVO.getLastSubmissionDate().after(onlineVO.getOnlineUpdateDate()))) {
                    onlineVO.setOnlineUpdateDate(null);
                    onlineVO.setOnlineUserId(null);
                }
            }    
            
            reportDataVO =
                new TPAVestingSubmissionReportData(criteria, onlineDataList.size());
    
            
            List voList = new ArrayList<TPAVestingSubmissionDetailsVO>();
            for (Map.Entry<BigDecimal, TPAVestingSubmissionDetailsVO> entry : tpaReportData.entrySet()) {
                voList.add(entry.getValue());
            } 
            
            reportDataVO.setDetails(voList);
        }
        
        logger.debug("exit <- getTPAVestingSubmissions");
        return reportDataVO;
    }
    
    private static String convertListToCommaDelimitedString(List<BigDecimal> contractList) {
        StringBuffer result = new StringBuffer();
        for (BigDecimal contractId : contractList) {
            result.append(contractId.intValue()).append(",");
        }
        
        if (result.toString().trim().endsWith(",")) {
            return result.toString().trim().substring(0, result.toString().trim().length() - 1);
        }
        return result.toString().trim();
    }
}