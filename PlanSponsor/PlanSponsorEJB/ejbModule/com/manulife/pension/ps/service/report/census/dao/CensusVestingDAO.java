package com.manulife.pension.ps.service.report.census.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SelectMultiFieldQueryHandler;
import com.manulife.pension.exception.ExceptionHandlerUtility;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails;
import com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData;
import com.manulife.pension.ps.service.submission.util.VestingMoneyTypeComparator;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.contract.valueobject.MoneyTypeVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.employee.util.CensusVestingEmployeeDataValidator;
import com.manulife.pension.service.employee.util.EmployeeData;
import com.manulife.pension.service.employee.util.EmployeeValidationError;
import com.manulife.pension.service.employee.util.EmployeeValidationErrors;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.VestingConstants;
import com.manulife.pension.service.vesting.VestingException;
import com.manulife.pension.service.vesting.VestingRetriever;
import com.manulife.pension.service.vesting.VestingRetrieverFactory;

/**
 * This is the DAO for Census Vesting
 *
 * @author Diana Macean
 */
public final class CensusVestingDAO extends BaseDatabaseDAO {

	private static final String className = CensusVestingDAO.class.getName();

	private static final Logger logger = Logger.getLogger(CensusVestingDAO.class);

    private static final String GET_CENSUS_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_CENSUS_SUMMARY(?,?,?,?,?,?,?, ?)}";

    
    private static final String SQL_SELECT_CONTRACT_SORT_OPTIONS =
        "SELECT C.PARTICIPANT_SORT_OPTION_CODE, " +
        "       C.SPECIAL_SORT_CATEGORY_IND " +
        "FROM   EZK100.CONTRACT_CS C " +
        "WHERE  C.CONTRACT_ID = ?";

    private static final String SQL_VESTING_PERCENTAGE_SELECT =
        "select p1.MONEY_TYPE_ID as moneyTypeId,"
        + " p1.PERCENTAGE as percentage,"
        + " p1.EFFECTIVE_DATE as effDate"
        + " from  EZK100.EMPLOYEE_MONEY_TYPE_VESTING_PCT p1"
        + " where p1.CONTRACT_ID = ? and p1.PROFILE_ID = ?"
        + " and   p1.EFFECTIVE_DATE = (select max(p2.EFFECTIVE_DATE)"
        + "                                    from EZK100.EMPLOYEE_MONEY_TYPE_VESTING_PCT p2"
        + "                                    where p2.CONTRACT_ID = ? and p2.PROFILE_ID = ?"
        + "                                    and  p2.EFFECTIVE_DATE <= ?"
        + "                                    and  p2.MONEY_TYPE_ID = p1.MONEY_TYPE_ID)";


	private static final Map<String, String> fieldToColumnMap = new HashMap<String, String>();
    private static final String AND = " and ";
    private static final String ALL = "All";
    private static final int BATCH_SIZE = 1000;

	/**
     * Make sure nobody instanciates this class.
	 */
	private CensusVestingDAO() {
	}

	static {
		fieldToColumnMap.put("firstName", "FIRST_NAME");
		fieldToColumnMap.put("lastName", "LAST_NAME");
		fieldToColumnMap.put("ssn", "SOCIAL_SECURITY_NO");
        fieldToColumnMap.put("empId", "EMPLOYEE_ID");
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
	public static ReportData getReportData(final ReportCriteria criteria, final boolean showAllMoneyTypes)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}

		// get contract number from criteria
        int contractNumber = (new Integer((String)criteria.getFilterValue(
                CensusVestingReportData.FILTER_CONTRACT_NUMBER))).intValue();


        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet resultSet = null;

        // create the value object
        CensusVestingReportData censusVestingReportData = new CensusVestingReportData(criteria, 0);


        try {

            // Retrieve the sort options.
            final Class[] resultClasses = new Class[2];
            resultClasses[0] = String.class;
            resultClasses[1] = String.class;
            final SelectMultiFieldQueryHandler handler
                    = new SelectMultiFieldQueryHandler(
                            CUSTOMER_DATA_SOURCE_NAME,
                            SQL_SELECT_CONTRACT_SORT_OPTIONS, resultClasses );

            final Object[] results = (Object[]) handler.select(new Integer[] { new Integer(contractNumber) });

            // Retrieve the employee sort option [PARTICIPANT_SORT_OPTION_CODE].
            final String sortOptionCode = (String) results[0];
            censusVestingReportData.setSortOptionCode((sortOptionCode == null) ? "  " : sortOptionCode );

            // Retrieve the special sort indicator [SPECIAL_SORT_CATEGORY_IND].
            final String specialSortIndicator = (String) results[1];
            censusVestingReportData.setSpecialSortIndicator(specialSortIndicator);
            
            // get money types
            List<MoneyTypeVO> contractMoneyTypes = getContractMoneyTypeVOsForVesting(new Integer(contractNumber),showAllMoneyTypes);

            // sort according to the rule defined in the comparator
            Collections.sort(contractMoneyTypes,new VestingMoneyTypeComparator());
            censusVestingReportData.setMoneyTypes(contractMoneyTypes);

            // setup the connection and the statement
            conn = getDefaultConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_CENSUS_SUMMARY);

            if (logger.isDebugEnabled()) {
                logger.debug("Calling Stored Procedure: " + GET_CENSUS_SUMMARY);
            }

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

            String segment = (String)criteria.getFilterValue(CensusVestingReportData.FILTER_SEGMENT);
            if (StringUtils.isEmpty(segment))
                stmt.setNull(idx++, Types.CHAR);
            else
                stmt.setString(idx++, segment);

            // get the selectedAsOfDate
            String asOfDate = (String)criteria.getFilterValue(CensusVestingReportData.FILTER_ASOFDATE);
            Date selectedAsOfDate = null;
            if (asOfDate != null && asOfDate.trim().length() != 0)
                selectedAsOfDate = new Date(Long.parseLong(asOfDate));
            else
                selectedAsOfDate = new Date();

            stmt.setDate(idx++, new java.sql.Date(selectedAsOfDate.getTime()));

            // register the output parameters:  employeeCount
            stmt.registerOutParameter(idx, Types.INTEGER);

            // execute the stored procedure
            stmt.execute();
            resultSet = stmt.getResultSet();

            int  totalCount = stmt.getInt(idx);

            censusVestingReportData.setTotalCount(totalCount);

            List<CensusVestingDetails> censusVestingDetailsList = new ArrayList<CensusVestingDetails>();
            List<Long> profiles = new ArrayList<Long>();
            Map<Long, EmployeeVestingInformation> percentageMap = new HashMap<Long, EmployeeVestingInformation>();

            if (resultSet != null) {
                while (resultSet.next()) {
                    CensusVestingDetails censusVestingDetails = new CensusVestingDetails(
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
                            resultSet.getString("PARTICIPANT_STATUS_CODE"),
                            resultSet.getString("FULLY_VESTED_IND"),
                            resultSet.getDate("FULLY_VESTED_IND_EFF_DT"),
                            selectedAsOfDate,
                            resultSet.getString("MASK_SENSITIVE_INFO_IND"),
                            resultSet.getString("PROVIDED_ELIGIBILITY_DATE_IND"));

                    profiles.add(Long.parseLong(resultSet.getString("PROFILE_ID")));

                    String applyLTPTCrediting = resultSet.getString("APPLY_LTPT_CREDITING");
                    if("Y".equalsIgnoreCase(applyLTPTCrediting)) {
                    	censusVestingDetails.setApplyLTPTCrediting(applyLTPTCrediting);
                    }else {
                    	censusVestingDetails.setApplyLTPTCrediting("N");
                    }
                  
                    censusVestingDetailsList.add(censusVestingDetails);
                }
            }

            List profilesSubset = null;
            // call Vesting Engine to retrieve map: Map(profileId,EmployeeVestingInformation)
            // retrieve for up to BATCH_SIZE records at one time
            while (profiles.size() > 0) {
                if (profiles.size() <= BATCH_SIZE) {
                    percentageMap.putAll(getPercentages(contractNumber,profiles,selectedAsOfDate));
                    profiles.clear();
                } else {
                    profilesSubset = profiles.subList(0, BATCH_SIZE);
                    percentageMap.putAll(getPercentages(contractNumber,profilesSubset,selectedAsOfDate));
                    List temp = clone(profilesSubset);
                    profiles.removeAll(temp);
                }
            }

            // update census vesting details with percentages, effective date, errors/warnings
            String vestingServiceFeature=VestingConstants.VestingServiceFeature.COLLECTION;
            String creditingMethod = VestingConstants.CreditingMethod.UNSPECIFIED;
            Iterator detailsIter = censusVestingDetailsList.iterator();
            while (detailsIter.hasNext()) {
                CensusVestingDetails censusVestingDetails = (CensusVestingDetails)detailsIter.next();
                EmployeeVestingInformation vestingInfo = (EmployeeVestingInformation)percentageMap.get(new Long(censusVestingDetails.getProfileId()));
                if (vestingInfo != null) {
                    censusVestingDetails.setVestingEffDate(vestingInfo.getVestingEffectiveDate());
                    censusVestingDetails.setPercentages(vestingInfo.getMoneyTypeVestingPercentages());
                    censusVestingDetails.setCensusErrors(vestingInfo.getErrors());
                    censusVestingDetails.setCalculationFact(vestingInfo.getRetrievalDetails());
                    vestingServiceFeature = vestingInfo.getVestingServiceFeature();
                    if (vestingInfo.getRetrievalDetails() != null) {
                        creditingMethod = vestingInfo.getRetrievalDetails().getCreditingMethod();
                    }
                } else {
                    vestingInfo = EmployeeVestingInformation.EMPTY;
                    censusVestingDetails.setPercentages(vestingInfo.getMoneyTypeVestingPercentages());
                }
                // convert to EmployeeData
                EmployeeData employee = convertToEmployeeData(censusVestingDetails);
                
                    DataSource csdbDataSource = BaseDatabaseDAO
                    .getDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME);
                    
                    DataSource vfDataSource = BaseDatabaseDAO
                    .getDataSource(BaseDatabaseDAO.VIEW_FUNDS_DATA_SOURCE_NAME);
                    
                    DataSource sdbDataSource = BaseDatabaseDAO
                    .getDataSource(BaseDatabaseDAO.SUBMISSION_DATA_SOURCE_NAME);

                // find warnings for each record
                EmployeeValidationErrors errors = CensusVestingEmployeeDataValidator.getInstance().validate(employee, csdbDataSource, vfDataSource, sdbDataSource);
                
                if (errors.hasWarning()) {
                    final HashSet<EmployeeValidationError> errorSet = new HashSet<EmployeeValidationError>(errors.getErrors());
                    // These are different objects in censusVestingDetails.getCensusErrors() which 
                    // contains com.manulife.pension.service.vesting.util.VestingMessageType 
                    // objects, so we don't mix our errors in there. We use another list.
                    censusVestingDetails.setEmployeeErrors(errorSet);
                    censusVestingDetails.getWarnings().setWarnings(true);
                } // fi
            }

            censusVestingReportData.setVestingServiceFeature(vestingServiceFeature);
            censusVestingReportData.setCreditingMethod(creditingMethod);
            censusVestingReportData.setContractNumber(contractNumber);
            censusVestingReportData.setDetails(censusVestingDetailsList);
            censusVestingReportData.setAsOfDate(selectedAsOfDate);

        } catch (SQLException e) {
           throw new SystemException(e, className, "getReportData",
           "Problem occurred during GET_CENSUS_SUMMARY stored proc call. Report criteria:"
           + criteria);
        } catch (DAOException e) {
            throw handleDAOException(e, className, "getReportData",
                    "Problem occurred for: " + SQL_SELECT_CONTRACT_SORT_OPTIONS
                            + " with contractNumber: " + contractNumber);
        } finally {
            close(stmt, conn);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("exit <- getCensusVesting");
        }

        return censusVestingReportData;

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
    

    private static List clone(List profileList) {
        List temp = new ArrayList();
        Iterator iter = profileList.iterator();
        while (iter.hasNext()) {
            temp.add(iter.next());
        }
        return temp;
    }

    /**
     * Get percentage list for each employee
     *
     * @param int contractNumber
     * @param String profileId
     * @param Connection conn
     * @return SortedMap
     */
    private static Map<Long, EmployeeVestingInformation> getPercentages(Integer contractId, List profileIds, Date selectedAsOfDate) throws SystemException {
       Map<Long, EmployeeVestingInformation> percentages = new HashMap<Long, EmployeeVestingInformation>();

       try {
            VestingRetriever vr = VestingRetrieverFactory
                    .getVestingRetriever(BaseDatabaseDAO
                            .getDataSource(BaseDatabaseDAO.CUSTOMER_DATA_SOURCE_NAME));
            percentages = vr.getEmployeesVestingInformation(
                    contractId, profileIds, selectedAsOfDate,
                    VestingRetriever.PARAMETER_USAGE_CODE_USED);
       } catch (DAOException e) {
            SystemException se = new SystemException(e, className, "getPercentages",
                    "DAO Exception occured. Input Paramereter is "
                            + "contractId: " + contractId);
            throw ExceptionHandlerUtility.wrap(se);
        } catch (VestingException e) {
            SystemException se = new SystemException(e, className, "getEmployeeByProfileId",
                    "VestingException occured. Input Paramereter is "
                            + "contractId: " + contractId);
            throw ExceptionHandlerUtility.wrap(se);
        }
       return percentages;
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
         String status = (String) criteria.getFilterValue(CensusVestingReportData.FILTER_STATUS);
    	 String lastNameValue = (String) criteria.getFilterValue(
                 CensusVestingReportData.FILTER_LAST_NAME);
    	 String ssnValue = (String) criteria.getFilterValue(CensusVestingReportData.FILTER_SSN);
         String divisionValue = (String)criteria.getFilterValue(CensusVestingReportData.FILTER_DIVISION);

         // if "All" selected, exclude Cancelled employees for external users only
         // if "Active" selected, retrieve employees with blank or active status
         // else retrieve employees that have selected status
        // Note: This value is from the EMPLOYEE_VESTING_PARAMETER table, to get the value that is
        // effective (not last submitted) hence we use: ES_val
         if (StringUtils.isEmpty(status) || status.equals(ALL)) {
             if (criteria.isExternalUser()) {
                 result.append("VALUE(ES_val,'') != 'C'").append(AND);
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

    /*
     * For vesting purposes, the following money types will be excluded
     * - if showAllMoneyTypes is false:
     *  1)  Unvested money types (money types that begin with ‘UM’)
     *  2)  Employee money types (money types that belong to the Money Group ‘EE’)
     *  3)  Fully vested Employer money types (money types that belong to the Money Group ‘ER’
     *      and where the Fully Vested Indicator is ‘Y’)
     * - if showAllMoneyTypes is true:
     *  1)  Unvested money types (money types that begin with ‘UM’)
     *
     */
    public static List<MoneyTypeVO> getContractMoneyTypeVOsForVesting(Integer contractId, boolean showAllMoneyTypes) throws SystemException {
        List<MoneyTypeVO> interimContractMoneyTypes = null;

        // retrieve all money types for contract (exclude UM money types)
        interimContractMoneyTypes = ContractDAO.getContractMoneyTypeVOs(new Integer(contractId), true);

        for (Iterator<MoneyTypeVO> it = interimContractMoneyTypes.iterator(); it.hasNext();) {
                MoneyTypeVO vo = it.next();

                if (!showAllMoneyTypes && vo.getMoneyGroup().equals("EE")) {
                    it.remove();
                } else if (!showAllMoneyTypes && vo.getMoneyGroup().equals("ER") && vo.getFullyVested().equals("Y")) {
                    it.remove();
                }
        }
        return interimContractMoneyTypes;
    }

    /**
     * Gets a {@link List} of {@link MoneyTypeVO} objects for the given contract.
     * 
     * @param contractId The contract ID to search with.
     * @return List - A list of MoneyTypeVO objects.
     * @throws SystemException If a DAOException is encountered, it's nested in this exception.
     */
    public static List<MoneyTypeVO> getContractMoneyTypeVOs(Integer contractId)
            throws SystemException {
        return ContractDAO.getContractMoneyTypeVOs(new Integer(contractId), false);
    }

}
