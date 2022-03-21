package com.manulife.pension.ps.service.report.participant.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.intware.dao.jdbc.SQLUpdateHandler;
import com.intware.dao.jdbc.SelectBeanQueryHandler;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddress;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData;
import com.manulife.pension.ps.service.submission.valueobject.Address;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * This is the DAO for Participant Addresses
 * 
 * @author stoicsi
 */
public class ParticipantAddressesDAO extends BaseDatabaseDAO {

	private static final String className = ParticipantAddressesDAO.class.getName();
    private static final Logger logger = Logger.getLogger(ParticipantAddressesDAO.class);
	
    private static final String GET_CENSUS_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_CENSUS_SUMMARY(?,?,?,?,?,?,?, ?)}";
    
	private static final Map fieldToColumnMap = new HashMap();
	private static final String AND = " and ";
    private static final String ALL = "All";
	
 	private static final String SQL_UPDATE_CSDB_ADDRESS = 
 		"update "
 		+ PLAN_SPONSOR_SCHEMA_NAME + "employee_contract set "
		+ "addr_line1 = ?, "
		+ "addr_line2 = ?, "
		+ "city_name = ?, "
		+ "state_code = ?, " 
		+ "zip_code = ?, "
		+ "country_name = ? "
		+ "where profile_id = "
		+ "(select profile_id from " + PLAN_SPONSOR_SCHEMA_NAME + "participant_contract where contract_id=? and participant_id=?)";

 	private static final String SQL_SELECT_CSDB_ADDRESS = 
 		"select "
 		+ "ec.contract_id contractNumber, "
		+ "pc.participant_id participantId, "
 		+ "ec.first_name firstName, "
 		+ "ec.last_name lastName, "
 		+ "ec.social_security_no ssn, "
 		+ "ec.employer_designated_id employeeId, "
		+ "ah.addr_line1 addressLine1, "
		+ "ah.addr_line2 addressLine2, "
		+ "ah.city_name city, "
		+ "ah.state_code stateCode, " 
		+ "ah.zip_code zipCode, "
		+ "ah.country_name country, "
		+ "ah.employee_addr_eff_ts updatedDate "
 		+ "from " + PLAN_SPONSOR_SCHEMA_NAME + "employee_contract ec, " 
 		+  	        PLAN_SPONSOR_SCHEMA_NAME + "participant_contract pc, "
 		+           PLAN_SPONSOR_SCHEMA_NAME + "employee_address_history2 ah " 
		+ "where ec.contract_id = ? and " 
		+		"ec.profile_id  = ? and " 
		+       "ec.contract_id = ah.contract_id and "
		+       "ec.profile_id  = ah.profile_id and "
		+		"ec.contract_id = pc.contract_id and "
		+       "DATE(EMPLOYEE_ADDR_END_TS) = DATE('9999-12-31') and "
		+		"ec.profile_id  = pc.profile_id";
		
	// Make sure nobody instanciates this class
	private ParticipantAddressesDAO() {
	}
	
	static {
		fieldToColumnMap.put("lastName", "last_name");
		fieldToColumnMap.put("stateCode", "state_code");
		fieldToColumnMap.put("country", "country_name");
		fieldToColumnMap.put("city","city_name");
		fieldToColumnMap.put("zip","zip_code");
        fieldToColumnMap.put("status", "EMPLOYMENT_STATUS_CODE");
        fieldToColumnMap.put("division", "EMPLOYER_DIVISION");
	}
	
	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantsList");
			logger.debug("Report criteria -> " + criteria.toString());
		}
			
		Connection conn = null;
       	CallableStatement stmt = null;
       	ResultSet resultSet = null;
       	ParticipantAddressesReportData vo = null;
		
		try {	
			// setup the connection and the statement
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_CENSUS_SUMMARY);
            
            if (logger.isInfoEnabled()) {
				logger.debug("Calling Stored Procedure: "+ GET_CENSUS_SUMMARY);
			}				
			
            // get contract from criteria
			Integer contractNumber = (Integer)criteria.getFilterValue(ParticipantAddressesReportData.FILTER_CONTRACT_NUMBER);			
			stmt.setBigDecimal(1, new BigDecimal(contractNumber.intValue()) );
            
			// get filter from criteria
            String filterPhrase = createFilterPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Filter phrase: " + filterPhrase);
            }
            if (filterPhrase == null) {
                stmt.setNull(2, Types.VARCHAR);
            } else {
                stmt.setString(2, filterPhrase);
            }
			
            // build sorting
            String sortPhrase = createSortPhrase(criteria);
            if (logger.isDebugEnabled()) {
                logger.debug("Sort phrase: " + sortPhrase);
            }
            stmt.setString(3, sortPhrase);
			
            // page size and start index
			if (criteria.getPageSize() == -1)
				stmt.setNull(4, Types.DECIMAL);		
			else
				stmt.setInt(4, criteria.getPageSize());		
			stmt.setInt(5, criteria.getStartIndex()); 
            
            String segment = (String)criteria.getFilterValue(ParticipantAddressesReportData.FILTER_SEGMENT);
            if (StringUtils.isEmpty(segment))
                stmt.setNull(6, Types.CHAR);
            else
                stmt.setString(6, segment);
            
            // Set the asOfDate for retrieving the Parameters (employment_Status_code, vyos etc.)
            stmt.setDate(7, new java.sql.Date(System.currentTimeMillis()));

            
			// register the output parameters - employeeCount
            stmt.registerOutParameter(8, Types.INTEGER);
            
            	
            // execute the stored procedure
            stmt.execute();
			resultSet = stmt.getResultSet();
			
   			 			
			// get the output parameters from the statement
			int  totalCount = stmt.getInt(8);
			
			// set the attributes in the value object
			vo = new ParticipantAddressesReportData(criteria, totalCount);
			vo.setContractNumber(contractNumber.intValue());

			List participants = new ArrayList();
			
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
                                            
    						
    				participants.add(item);			
    			}

			}
			
			vo.setDetails(participants);

		} catch (SQLException e) {
           throw new SystemException(e, className, "getReportData", "Problem occurred during GET_CENSUS_SUMMARY stored proc call.");
        } finally {
        	close(stmt, conn);
        }

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantsList");
		}
		return vo;
	}
	
	private static String createSortPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		Iterator sorts = criteria.getSorts().iterator();
		String sortDirection = null;
		String orjSortDirection = null;		

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
	
	private static String createFilterPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
        String status = (String) criteria.getFilterValue(ParticipantAddressesReportData.FILTER_STATUS);
		String lastNameValue = (String)criteria.getFilterValue(ParticipantAddressesReportData.FILTER_LAST_NAME);
		String ssnValue = (String)criteria.getFilterValue(ParticipantAddressesReportData.FILTER_SSN);
        String divisionValue = (String)criteria.getFilterValue(ParticipantAddressesReportData.FILTER_DIVISION);

        // if "All" selected, exclude Cancelled employees for external users only
        // if "Active" selected, retrieve employees with blank or active status
        // else retrieve employees that have selected status
        if (StringUtils.isEmpty(status) || status.equals(ALL)) {
            if (criteria.isExternalUser()) {
                result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') != 'C'").append(AND);
            }
        } else if (status.equals("A")) {
            result.append("VALUE(ES_val,'') in ('A','')").append(AND);
        } else {
            result.append("VALUE(ES_val,'') = ").append(wrapInSingleQuotes(status).
            toUpperCase()).append(AND);
        }
        
		if (!StringUtils.isEmpty(lastNameValue)) {
			result.append("LAST_NAME like ").append(wrapInSingleQuotes(lastNameValue + "%").toUpperCase() ).append(AND);//CL 115918 - removed upper
		}

		if (!StringUtils.isEmpty(ssnValue)) {
			result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).append(AND);
		}
        
        if (!StringUtils.isEmpty(divisionValue)) {
        	result.append("UPPER(EMPLOYER_DIVISION) like ").append(wrapInSingleQuotes(divisionValue + "%").toUpperCase() ).append(AND);
        }

		if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()) ) ) {
			result.delete(result.length() - AND.length(), result.length());
		}

		return (result.toString().trim().length() > 0 ? result.toString() : null);
	}

	public static ParticipantAddress getCSDBAddress(long profileId, int contractNumber) throws SystemException {
		
		ParticipantAddress address = null;
		
		List params = new ArrayList(2);
		params.add(0,new Integer(contractNumber));
		params.add(1,new BigDecimal(String.valueOf(profileId)));
		
		SelectBeanQueryHandler handler = new SelectBeanQueryHandler(CUSTOMER_DATA_SOURCE_NAME, SQL_SELECT_CSDB_ADDRESS, ParticipantAddress.class);
		
		try {
			logger.debug("Executing Prepared SQL Statment: "+ SQL_SELECT_CSDB_ADDRESS);
			
			// retrieve and trim the address
			address = (ParticipantAddress)handler.select(params.toArray());
			trimAddress(address);
			
			// get the sort option code
			address.setSortOptionCode(ContractDAO.getSortOptionCode(contractNumber));
			
		} catch (DAOException e) {
			// unable to find an address record - return null
		}

		return address;

	}

	private static void trimAddress(ParticipantAddress address) {
		if ( address.getAddressLine1() != null ) {
			address.setAddressLine1(address.getAddressLine1().trim());
		}
		if ( address.getAddressLine2() != null ) {
			address.setAddressLine2(address.getAddressLine2().trim());
		}
		if ( address.getCity() != null ) {
			address.setCity(address.getCity().trim());
		}
		if ( address.getCountry() != null ) {
			address.setCountry(address.getCountry().trim().toUpperCase());
		}
		if ( address.getFirstName() != null ) {
			address.setFirstName(address.getFirstName().trim());
		}
		if ( address.getLastName() != null ) {
			address.setLastName(address.getLastName().trim());
		}
		if ( address.getStateCode() != null ) {
			address.setStateCode(address.getStateCode().trim().toUpperCase());
		}
		if ( address.getZipCode() != null ) {
			address.setZipCode(address.getZipCode().trim());
		}
	}

	
	/**
	 * Updates the CSDB with the given address
	 * 
	 * @param address
	 * @throws SystemException
	 */
	public static void updateCSDBAddress(Address address) throws SystemException {
		
		if ( address.getContractNumber() == null || address.getParticipantId() == null ) {
			// can't update - not enough info to execute update statement
			// don't throw an exception - just return;
			return;
		}
		
		List params = new ArrayList(8);
		params.add(0,address.getAddressLine1());
		params.add(1,address.getAddressLine2());
		params.add(2,address.getCity());
		params.add(3,address.getStateCode());
		params.add(4,address.getZipCode());
		params.add(5,address.getCountry());
		params.add(6,address.getContractNumber());
		params.add(7,address.getParticipantId());

		SQLUpdateHandler handler = new SQLUpdateHandler(CUSTOMER_DATA_SOURCE_NAME, SQL_UPDATE_CSDB_ADDRESS);
		
		try {
			logger.debug("Executing Prepared SQL Statment: "+ SQL_UPDATE_CSDB_ADDRESS);
			handler.update(params.toArray());
		} catch (DAOException e) {
			throw new SystemException("OnlineAddresDAO.updateCSDBAddress(): unable to update address on CSDB");
		}
	}
    
    private static String trim(String invalue) {
        return (invalue != null) ? invalue.trim() : invalue;
    }

}
