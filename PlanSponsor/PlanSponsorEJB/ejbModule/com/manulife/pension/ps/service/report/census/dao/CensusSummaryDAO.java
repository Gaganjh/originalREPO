package com.manulife.pension.ps.service.report.census.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.employee.util.CensusEmployeeDataValidator;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * This is the DAO for Census Summary.
 * 
 * @author Diana Macean
 */
public final class CensusSummaryDAO extends BaseDatabaseDAO {

	private static final String className = CensusSummaryDAO.class.getName();

	private static final Logger logger = Logger.getLogger(CensusSummaryDAO.class);
    
    private static final String GET_CENSUS_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_CENSUS_SUMMARY(?,?,?,?,?,?,?, ?)}";
    
	private static final Map fieldToColumnMap = new HashMap();
    private static final String AND = " and ";
    private static final String ALL = "All";

	/**
     * Make sure nobody instanciates this class.
	 */ 
	private CensusSummaryDAO() {
	}

	static {
		fieldToColumnMap.put("firstName", "FIRST_NAME");
		fieldToColumnMap.put("lastName", "LAST_NAME");
		fieldToColumnMap.put("ssn", "SOCIAL_SECURITY_NO");
        fieldToColumnMap.put("birthDate", "BIRTH_DATE");
		fieldToColumnMap.put("hireDate", "HIRE_DATE");
		fieldToColumnMap.put("status", "EMPLOYMENT_STATUS_CODE");
        fieldToColumnMap.put("division", "EMPLOYER_DIVISION");
	}

    /**
     * Get report data.
     * 
     * @param criteria ReportCriteria
     * @return ReportData
     * 
     * @throws SystemException
     */ 
	public static ReportData getReportData(final ReportCriteria criteria, final boolean processWarnings)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}

        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;
        CensusSummaryReportData psReportDataVO = null;
        
        try {   
            
            DataSource csdbDataSource = BaseDatabaseDAO
            .getDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
            
            DataSource vfDataSource = BaseDatabaseDAO
            .getDataSource(BaseDatabaseDAO.VIEW_FUNDS_DATA_SOURCE_NAME);
            
            DataSource sdbDataSource = BaseDatabaseDAO
            .getDataSource(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);
            
            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_CENSUS_SUMMARY);
            
            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_CENSUS_SUMMARY);
            }
            
            // get contract number from criteria
            int contractNumber = (new Integer((String) criteria.getFilterValue(
                    CensusSummaryReportData.FILTER_CONTRACT_NUMBER))).intValue();
            int idx = 1;
            stmt.setBigDecimal(idx++, intToBigDecimal(contractNumber));
            
            // get filter from criteria
            String filterPhrase = createFilterPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Filter phrase: " + filterPhrase);
            }
            if (filterPhrase == null) {
                stmt.setNull(idx++, Types.VARCHAR);
            } else {
                stmt.setString(idx++, filterPhrase);
            }
            
            // build sorting
            String sortPhrase = createSortPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Sort phrase: " + sortPhrase);
            }
            stmt.setString(idx++, sortPhrase);
            
            if (criteria.getPageSize() == -1) {
                stmt.setNull(idx++, Types.DECIMAL);
            } else {
                stmt.setBigDecimal(idx++, intToBigDecimal(criteria.getPageSize()));
            } 
            stmt.setBigDecimal(idx++, intToBigDecimal(criteria.getStartIndex()));
            
            String segment = (String)criteria.getFilterValue(CensusSummaryReportData.FILTER_SEGMENT);
            if (StringUtils.isEmpty(segment))
                stmt.setNull(idx++, Types.CHAR);
            else
                stmt.setString(idx++, segment);

            // Set the asOfDate for retrieving the Parameters (employment_Status_code, vyos etc.)
            stmt.setDate(idx++, new java.sql.Date(System.currentTimeMillis()));

            // register the output parameters:  employeeCount
            stmt.registerOutParameter(idx, Types.INTEGER);
            
            // execute the stored procedure
            stmt.execute();
            resultSet = stmt.getResultSet();
            
            int  totalCount = stmt.getInt(idx);
                        
            // set the attributes in the value object
            psReportDataVO = new CensusSummaryReportData(criteria, totalCount);
            
            List censusSummaryDetails = new ArrayList();
            
            if (resultSet != null) {
                while (resultSet.next()) {
                    CensusSummaryDetails item = new CensusSummaryDetails(
                        resultSet.getString("SOCIAL_SECURITY_NO"),
                        resultSet.getString("EMPLOYEE_ID"),
                        StringUtils.trimToEmpty(resultSet.getString("FIRST_NAME")),
                        StringUtils.trimToEmpty(resultSet.getString("LAST_NAME")),
                        trim(resultSet.getString("MIDDLE_INITIAL")),
                        trim(resultSet.getString("NAME_PREFIX")),
                        resultSet.getString("ADDR_LINE1"),
                        resultSet.getString("ADDR_LINE2"),
                        resultSet.getString("CITY_NAME"),
                        resultSet.getString("STATE_CODE"),
                        resultSet.getString("ZIP_CODE"),
                        resultSet.getString("COUNTRY_NAME"),
                        resultSet.getString("RESIDENCE_STATE_CODE"),
                        resultSet.getString("EMPLOYER_PROVIDED_EMAIL_ADDR"),
                        resultSet.getString("EMPLOYER_DIVISION"),
                        resultSet.getDate("BIRTH_DATE"),
                        resultSet.getDate("HIRE_DATE"),
                        resultSet.getString("EMPLOYMENT_STATUS_CODE"),
                        resultSet.getDate("EMPLOYMENT_STATUS_EFF_DATE"),
                        resultSet.getString("PLAN_ELIGIBLE_IND"),
                        resultSet.getDate("ELIGIBILITY_DATE"),
                        resultSet.getString("AUTO_ENROLL_OPT_OUT_IND"),
                        resultSet.getString("PLAN_YTD_HRS_WORKED"),
                        resultSet.getBigDecimal("PLAN_YTD_COMP"),
                        resultSet.getDate("PLAN_YTD_HRS_WORK_COMP_EFF_DT"),
                        resultSet.getString("PREVIOUS_YRS_OF_SERVICE"),
                        resultSet.getDate("PREVIOUS_YRS_OF_SERVICE_EFF_DT"),
                        resultSet.getBigDecimal("ANNUAL_BASE_SALARY"),
                        resultSet.getBigDecimal("BEFORE_TAX_DEFER_PCT"),
                        resultSet.getBigDecimal("DESIG_ROTH_DEF_PCT"),
                        resultSet.getBigDecimal("BEFORE_TAX_DEFER_AMT"),
                        resultSet.getBigDecimal("DESIG_ROTH_DEF_AMT"),
                        resultSet.getString("PROFILE_ID"),
                        resultSet.getInt("CONTRACT_ID"),
                        resultSet.getBoolean("PARTICIPANT_IND"),
                        resultSet.getString("FULLY_VESTED_IND"),
                        resultSet.getDate("FULLY_VESTED_IND_EFF_DT"),
                        resultSet.getString("MASK_SENSITIVE_INFO_IND"),
                        resultSet.getString("PROVIDED_ELIGIBILITY_DATE_IND")
                        );
                        
                    if (processWarnings) {
                        // convert to EmployeeData
                        EmployeeData employee = convertToEmployeeData(item);
                        
                        // find warnings for each record
                        EmployeeValidationErrors errors = CensusEmployeeDataValidator.getInstance().validate(employee, csdbDataSource, vfDataSource, sdbDataSource);
                        if (errors.hasWarning()) {
                            item.setCensusErrors(new HashSet<EmployeeValidationError>(errors.getErrors()));
                            item.getWarnings().setWarnings(true);
                        }
                    }
                    
                    censusSummaryDetails.add(item);            
                }
            }
            
            psReportDataVO.setContractNumber(contractNumber); 
            psReportDataVO.setDetails(censusSummaryDetails);
            
            
        } catch (SQLException e) {
           throw new SystemException(e, className, "getReportData", 
           "Problem occurred during GET_CENSUS_SUMMARY stored proc call. Report criteria:"
           + criteria);
        } catch (DAOException e) {
            throw new SystemException(e, className, "getReportData",
            "Fail to get datasource " + BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
        } finally {
            close(stmt, conn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getCensusSummary");
        }
        
        return psReportDataVO;

	}
		
    /**
     * Building the sort order.
     * 
     * @param criteria ReportCriteria
     * @return String
     */
    private static String createSortPhrase(ReportCriteria criteria) {
        StringBuffer result = new StringBuffer();
        Iterator sorts = criteria.getSorts().iterator();
        String sortDirection = null;  

        for (int i=0; sorts.hasNext(); i++) {
            ReportSort sort = (ReportSort)sorts.next();
            
            if (i==0) sortDirection = sort.getSortDirection();

            result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');           
            result.append(sortDirection);
            
            if ("lastName".equals(sort.getSortField())) {
                result.append(", FIRST_NAME ").append(sortDirection); 
                result.append(", MIDDLE_INITIAL ").append(sortDirection);
            } 
            
            result.append(',');
        }
        
        if (result.length() > 0) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    /**
     * Building the where clause.
     * 
     * @param criteria ReportCriteria
     * @return String
     */
    private static String createFilterPhrase(final ReportCriteria criteria) {
    	 StringBuffer result = new StringBuffer();
         String status = (String) criteria.getFilterValue(CensusSummaryReportData.FILTER_STATUS);
    	 String lastNameValue = (String) criteria.getFilterValue(
                 CensusSummaryReportData.FILTER_LAST_NAME);
    	 String ssnValue = (String) criteria.getFilterValue(CensusSummaryReportData.FILTER_SSN);
         String divisionValue = (String)criteria.getFilterValue(CensusSummaryReportData.FILTER_DIVISION);
         
         // if "All" selected, exclude Cancelled employees for external users only
         // if "Active" selected, retrieve employees with blank or active status
         // else retrieve employees that have selected status
        // Note: This value is from the EMPLOYEE_VESTING_PARAMETER table, to get the value that is
        // effective (not last submitted) hence we use: ES_val
         if (StringUtils.isEmpty(status) || status.equals(ALL)) {
             if (criteria.isExternalUser()) {
                 result.append(" VALUE(E.EMPLOYMENT_STATUS_CODE,'') != 'C'").append(AND);
             }
         } else if (status.equals("A")) {
             result.append("VALUE(ES_val,'') in ('A','')").append(AND);
         } else {
             result.append("VALUE(ES_val,'') = ").append(
                    wrapInSingleQuotes(status).
             toUpperCase()).append(AND);
         }
    	
    	 if (!StringUtils.isEmpty(lastNameValue)) {
        	 result.append("LAST_NAME like ").append(wrapInSingleQuotes(lastNameValue + "%").
             toUpperCase()).append(AND);//CL 115918 - removed upper
    	 }
    	
    	 if (!StringUtils.isEmpty(ssnValue)) {
        	 result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).
             append(AND);
    	 }
         
         if (!StringUtils.isEmpty(divisionValue)) {
             result.append("UPPER(EMPLOYER_DIVISION) like ").append(wrapInSingleQuotes(divisionValue + "%").
             toUpperCase() ).append(AND);
         }
    	
    	 if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()))) {
    	     result.delete(result.length() - AND.length(), result.length());
    	 }
    	
         return (result.toString().trim().length() > 0 ? result.toString() : null);
	}
	
    private static String trim(String invalue) {
        return (invalue != null) ? invalue.trim() : invalue;
    }
    
    /**
     * Convert the CensusSummaryDetails value object to the 
     * EmployeeData object for validation.
     *
     * @param item
     * @return
     */
    private static EmployeeData convertToEmployeeData(CensusSummaryDetails item) {
        EmployeeData employeeData = new EmployeeData();
        
        employeeData.setContractId(item.getContractId());
        employeeData.setProfileId(Long.parseLong(item.getProfileId()));
        employeeData.setSsn(item.getSsn());
        employeeData.setFirstName(item.getFirstName());
        employeeData.setLastName(item.getLastName());
        employeeData.setMiddleInit(item.getMiddleInitial());
        employeeData.setNamePrefix(item.getNamePrefix());
        employeeData.setBirthDate(item.getBirthDate());
        employeeData.setHireDate(item.getHireDate());
        employeeData.setDivision(item.getDivision());
        employeeData.setEmployeeNumber(item.getEmployeeNumber());

        employeeData.setStateOfResidence(item.getStateOfResidence());
        employeeData.setEmployerProvidedEmail(item.getEmployeeProvidedEmail());

        employeeData.setEmploymentStatus(item.getStatus());
        employeeData.setEmploymentStatusDate(item.getEmployeeStatusDate());
        employeeData.setAnnualBaseSalary(item.getAnnualBaseSalary());
        employeeData.setBeforeTaxDeferralPercentage(item.getBeforeTaxDeferralPercentage());
        employeeData.setDesignatedRothDeferralPct(item.getDesigRothDeferralPercentage());
        employeeData.setBeforeTaxFlatDollarDeferral(item.getBeforeTaxDeferralAmount());
        employeeData.setDesignatedRothDeferralAmt(item.getDesigRothDeferralAmount());
        employeeData.setOptOutIndicator(item.getOptOut());
        
        employeeData.setAddressLine1(item.getAddressLine1());
        employeeData.setAddressLine2(item.getAddressLine2());
        employeeData.setCityName(item.getCity());
        employeeData.setStateCode(item.getStateCode());
        employeeData.setCountryName(item.getCountry());
        employeeData.setZipCode(item.getZipCode());
        
        employeeData.setEligibilityDate(item.getEligibilityDate());
        employeeData.setEligibilityIndicator(item.getEligibleToDeferInd());
        employeeData.setPlanYTDCompensation(item.getPlanYTDCompensation());
        employeeData.setPlanYTDHoursWorked(item.getPlanYTDHoursWorked());
        employeeData.setPlanYTDHoursWorkedEffectiveDate(item.getPlanYTDHoursWorkedEffDate());
        employeeData.setPreviousYearsOfService(item.getPreviousYearsOfService());
        employeeData.setPreviousYearsOfServiceEffectiveDate(item.getPreviousYearsOfServiceEffDate());
        employeeData.setFullyVestedInd(item.getFullyVestedInd());
        employeeData.setFullyVestedEffectiveDate(item.getFullyVestedIndEffDate());
        return employeeData;
    }
    
    /**
     * Method used to get the employee details for given contract_id.
     * This is method is used for beneficiary download report
     * 
     * @param criteria
     * @param processWarnings
     * @return
     * @throws SystemException
     */
    public static List<EmployeeData> getEmployeeDetails(ReportCriteria criteria, boolean processWarnings) 
	throws SystemException {
		
		CensusSummaryReportData censusSummaryReportData;
		List<CensusSummaryDetails> censusSummaryDetailsList;
		List<EmployeeData> employeeDataList = new ArrayList<EmployeeData>();
		
		if(criteria != null) {
			censusSummaryReportData = (CensusSummaryReportData) getReportData(criteria, processWarnings);
			censusSummaryDetailsList = (List<CensusSummaryDetails>) censusSummaryReportData.getDetails();
			
			if(censusSummaryDetailsList != null && censusSummaryDetailsList.size() > 0) {
				
				for(CensusSummaryDetails item : censusSummaryDetailsList) {
					employeeDataList.add(convertToEmployeeData(item));			
				}
			}
		}
		return employeeDataList;
	}
    
}
