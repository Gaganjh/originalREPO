package com.manulife.pension.ps.service.report.participant.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryTotals;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * This is the DAO for Participant Summary
 * 
 * @author stoicsi
 */
public class ParticipantSummaryDAO extends BaseDatabaseDAO {
	
	private static final String className = ParticipantSummaryDAO.class.getName();
	
    private static final Logger logger = Logger.getLogger(ParticipantSummaryDAO.class);
	
	private static final String GET_PARTICIPANTS_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_PARTICIPANTS_SUMMARY(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";  //CL 110234
	
	private static final Map fieldToColumnMap = new HashMap();
	private static final String AND = " and ";
    private static final String ALL = "All";												//CL 110234

	// String constants
	private static final String NA = "n/a";
	private static final String YES = "Yes";
	private static final String NO = "No";
	private static final String YES_IND = "Y";
	private static final String NO_IND = "N";
	
	// Make sure nobody instanciates this class
	private ParticipantSummaryDAO() {
	}
	
	static {
		fieldToColumnMap.put("firstName", "first_name");		
		fieldToColumnMap.put("lastName", "last_name");
		fieldToColumnMap.put("ssn", "ssn");
		fieldToColumnMap.put("division", "employer_division");
		fieldToColumnMap.put("age", "age");
		fieldToColumnMap.put("defaultBirthDate", "default_birth_date");
		fieldToColumnMap.put("birthDate", "birth_date");
		fieldToColumnMap.put("status", "status");
		fieldToColumnMap.put("contributionStatus", "status");							//CL 110234
		fieldToColumnMap.put("employmentStatus", "employment_status_code");				//CL 110234
		fieldToColumnMap.put("defaultInvestment", "default_investment");
		fieldToColumnMap.put("investmentInstructionType", "investment_instruction_typ_cd");
		fieldToColumnMap.put("rothInd", "roth_ind");
		fieldToColumnMap.put("employeeAssets", "employee_asset");
		fieldToColumnMap.put("employerAssets", "employer_asset");
		fieldToColumnMap.put("totalAssets", "total_balance");
		fieldToColumnMap.put("outstandingLoans", "outstanding_loans");
		fieldToColumnMap.put("participantGatewayInd", "gateway_option_status_code");
		fieldToColumnMap.put("managedAccountStatusInd", "managed_account_status_ind");
        fieldToColumnMap.put("eligibilityDate", "eligibility_date");
	}
	
	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantsSummary");
			logger.debug("Report criteria -> " + criteria.toString());
		}
			
		Connection conn = null;
       	CallableStatement stmt = null;
       	ResultSet resultSet = null;
       	ParticipantSummaryReportData psReportDataVO = null;
      		
		try {	
			// setup the connection and the statement
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_PARTICIPANTS_SUMMARY);
            
            if(logger.isDebugEnabled()) 
				logger.debug("Calling Stored Procedure: "+ GET_PARTICIPANTS_SUMMARY);
			
			int contractNumber = (new Integer((String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_1))).intValue();
			String asOfDateStr = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_2);
	        java.sql.Date asOfDate = null;
	        if(asOfDateStr!=null&&asOfDateStr.length()!=0) asOfDate = new java.sql.Date(Long.parseLong(asOfDateStr));
		
			String status = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_3);
			String gatewayChecked = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_6);
            String totalAssetsFrom = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_8);
            String totalAssetsTo = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_9);
			stmt.setBigDecimal(1, intToBigDecimal(contractNumber) );
			
			if (asOfDate == null)
				stmt.setNull(2, Types.DATE); 
			else
				stmt.setDate(2, asOfDate);
			
			//stmt.setNull(3, Types.VARCHAR); 
			stmt.setString(3, status);
			if (gatewayChecked == null)
				stmt.setNull(4, Types.VARCHAR ); 
			else
				stmt.setString(4, gatewayChecked);
				
		    if(logger.isDebugEnabled()) 
				logger.debug("Filter phrase: "+ createFilterPhrase(criteria));
			stmt.setString(5, createFilterPhrase(criteria));
			String sortPhrase = createSortPhrase(criteria);
		    if(logger.isDebugEnabled()) 
				logger.debug("Sort phrase: "+ sortPhrase);
			stmt.setString(6, sortPhrase);		// sort order
			if ( criteria.getPageSize() == -1)
				stmt.setNull(7, Types.DECIMAL);					// page size (aka numberOfRecords)
			else
				stmt.setInt(7, criteria.getPageSize());			
					
			stmt.setInt(8, criteria.getStartIndex()); 			    // start index	(aka readStartPosition)
            
            if (totalAssetsFrom == null){               //Added the total assets range from filter for NBDW
                stmt.setNull(9, Types.VARCHAR);
            } else{
                stmt.setString(9, totalAssetsFrom);
            }

            if (totalAssetsTo == null){         //Added the total assets range to filter for NBDW
                stmt.setNull(10, Types.VARCHAR);
            } else{
                stmt.setString(10, totalAssetsTo);
            }
            
            stmt.setDate(11, new java.sql.Date(System.currentTimeMillis()));	//CL 110234
            
			// register the output parameters
			//CL 110234 Begin
			stmt.registerOutParameter(12, Types.DECIMAL);        	// employeeTotal
			stmt.registerOutParameter(13, Types.DECIMAL);        	// employerTotal
			stmt.registerOutParameter(14, Types.DECIMAL);        	// employeeAve
			stmt.registerOutParameter(15, Types.DECIMAL);        	// employerAve
			stmt.registerOutParameter(16, Types.DECIMAL);        	// outTotal
			stmt.registerOutParameter(17, Types.DECIMAL);        	// outAve
			stmt.registerOutParameter(18, Types.DECIMAL);        	// outLoanTotal
			stmt.registerOutParameter(19, Types.DECIMAL);        	// outLoanAve
			stmt.registerOutParameter(20, Types.INTEGER);        	// participantCount (displayed as 'Total participants')
			stmt.registerOutParameter(21, Types.INTEGER);        	// participantSummaryCount
			stmt.registerOutParameter(22, Types.DECIMAL);        	// version
			//CL 110234 End
            // execute the stored procedure
            stmt.execute();
			resultSet = stmt.getResultSet();
			//CL 110234 Begin
			double  employeeTotal = (stmt.getBigDecimal(12)).doubleValue();
			double  employerTotal = (stmt.getBigDecimal(13)).doubleValue();
			double  employeeAve = (stmt.getBigDecimal(14)).doubleValue();
			double  employerAve = (stmt.getBigDecimal(15)).doubleValue();
			double  outTotal = (stmt.getBigDecimal(16)).doubleValue();
			double  outAve = (stmt.getBigDecimal(17)).doubleValue();
			double  outLoanTotal = (stmt.getBigDecimal(18)).doubleValue();
			double  outLoanAve = (stmt.getBigDecimal(19)).doubleValue();
			int  totalCount = stmt.getInt(20);
			int  participantSummaryCount = stmt.getInt(21);
   			//CL 110234 End
			
			ParticipantSummaryTotals participantSummaryTotals = 
					new ParticipantSummaryTotals(
			        					participantSummaryCount,
							  		  	employeeTotal, 
							  		 	employerTotal,
							  		 	outTotal,
							  			outLoanTotal, 
							  			employeeAve, 
							  			employerAve, 
							  			outAve,
							  			outLoanAve);
						
			// set the attributes in the value object
			psReportDataVO = new ParticipantSummaryReportData(criteria, totalCount);
			

			List participantSummaryDetails = new ArrayList();
			
			if (resultSet != null) {
			    
    			while (resultSet.next()) 
    			{
     				// Extract default investment and convert to 'n/a', 'Yes' or 'No'
    				String defaultInvestment = StringUtils.trim(resultSet.getString("default_investment"));
    				if (!StringUtils.equals(NA, defaultInvestment)) {
    					
    					defaultInvestment = StringUtils.equals(defaultInvestment, YES_IND) ? YES : NO;
    				} 
    				String investmentInstructionType = StringUtils.trim(resultSet.getString("investment_instruction_typ_cd"));
     				// Extract default birth date and convert to 'n/a', 'Yes' or 'No'
    				String defaultBirthDate = StringUtils.trim(resultSet.getString("default_birth_date"));
    				if (!StringUtils.equals(NA, defaultBirthDate)) {
    					
    					defaultBirthDate = StringUtils.equals(defaultBirthDate, YES_IND) ? YES : NO;
    				} 
    				//Gateway phase 1 -- start
    				//Assigning default value i.e. 'n/a' for previous date
    				String defaultGateway = StringUtils.trim(resultSet.getString("gateway_option_status_code"));
    				if (!StringUtils.equals(NA, defaultGateway)) {
    					defaultGateway = StringUtils.equals(defaultGateway, YES_IND) ? YES : NO;
    				}
    				//Gateway Phase 1 -- end
    				
    				String defaultManagedAccount = StringUtils.trim(resultSet.getString("managed_account_status_ind"));
    				if (!StringUtils.equals(NA, defaultManagedAccount)) {
    					defaultManagedAccount = StringUtils.equals(defaultManagedAccount, YES_IND) ? YES : NO;
    				}
    				ParticipantSummaryDetails item = new ParticipantSummaryDetails(
    					StringUtils.trim(resultSet.getString("first_name")),
    					StringUtils.trim(resultSet.getString("last_name")),
    					resultSet.getString("SOCIAL_SECURITY_NO"),
    					resultSet.getString("EMPLOYER_DIVISION"),					
    					resultSet.getInt("age"),
    					defaultBirthDate,
    					resultSet.getDate("birth_date"),
    					resultSet.getString("status"),
    					defaultInvestment,
    					investmentInstructionType,
    					resultSet.getString("roth_ind"),
    					resultSet.getDouble("employee_asset"),
    					resultSet.getDouble("employer_asset"),
    					resultSet.getDouble("total_balance"),
    					resultSet.getDouble("outstanding_loans"),
    					resultSet.getString("PROFILE_ID"),//Gateway phase 1 -- //Gateway Phase 1 -- start
    					//fetching participant gateway information from resultset
    					resultSet.getString("gateway_option_status_code"),
    					defaultGateway,
    					resultSet.getString("managed_account_status_ind"),
    					defaultManagedAccount,
                        resultSet.getDate("eligibility_date"),//Added the new select field for NBDW
                        resultSet.getString("employment_status_code"),							//CL 110234
                        resultSet.getString("employment_status_eff_date")						//CL 110234
    				    );
    					//Gateway Phase 1 -- end
    				
    				// requirement PPR.44
    				if (item.getBirthDate() == null) { 
    					item.setShowAge(false);
    				}
    				else {
    					item.setShowAge(true);
    				}
    									
    				participantSummaryDetails.add(item);			
    			}
    			
			}
			
			psReportDataVO.setContractNumber(contractNumber); 
			psReportDataVO.setDetails(participantSummaryDetails);
			psReportDataVO.setParticipantSummaryTotals(participantSummaryTotals);
			
		} catch (SQLException e) {
           throw new SystemException(e, className, "getReportData", "Problem occurred during GET_PARTICIPANTS_SUMMARY stored proc call. Report criteria:"+criteria);
        } finally {
        	close(stmt, conn);
        }

		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantsSummary");
		}
		
		return psReportDataVO;
			
	}
	
	private static String createSortPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		Iterator sorts = criteria.getSorts().iterator();
		String sortDirection = null;
		String orjSortDirection = null;
		boolean sortByDivision = false;

		for (int i=0; sorts.hasNext(); i++) {
			ReportSort sort = (ReportSort)sorts.next();
			
			if (i==0) {
				orjSortDirection = sort.getSortDirection();
				if ("division".equals(sort.getSortField())) {
					sortByDivision = true;
				}
			}
			
			sortDirection = sort.getSortDirection();

			if ("lastName".equals(sort.getSortField()))
			{
				result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
				
				if ( i > 0 && !sortByDivision)
				{
					result.append("asc".equals(orjSortDirection) ? "desc":"asc");
					result.append(", first_name ").append("asc".equals(orjSortDirection) ? "desc":"asc");
				}
				else
				{
					result.append(sortDirection);
					result.append(", first_name ").append(sortDirection);					
				}
			}
			else if ("age".equals(sort.getSortField()))
			{
				result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
				result.append(sortDirection);				
				result.append(", birth_date ").append("asc".equals(sortDirection) ? "desc":"asc");
			}
			else if ("birthDate".equals(sort.getSortField()) || 
						"status".equals(sort.getSortField()) ||
						"eligibilityDate".equals(sort.getSortField()) ||
						sortByDivision){
				result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
				result.append(sortDirection);
				result.append(", last_name ").append(sortDirection);
				result.append(", first_name ").append(sortDirection);
			}
			else
			{
				result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
				result.append(sortDirection);
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
		String lastNameValue = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_4);
		String ssnValue = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_5);
		String gatewayChecked = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_6);
		String divisionValue = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_7);
		String managedAccountChecked = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_14);

		if (lastNameValue != null && lastNameValue.trim().length() > 0 ) {
			result.append("last_name like ").append(wrapInSingleQuotes(lastNameValue + "%").toUpperCase() ).append(AND);//CL 115918 - removed upper
		}

		if (ssnValue != null && ssnValue.trim().length() > 0 ) {
			result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).append(AND);
		}
		
        if (!StringUtils.isEmpty(divisionValue)) {
            result.append("UPPER(EMPLOYER_DIVISION) like ").append(wrapInSingleQuotes(divisionValue + "%").toUpperCase() ).append(AND);
        }
		
		if(gatewayChecked != null){
			result.append("GATEWAY_OPTION_STATUS_CODE = ").append(wrapInSingleQuotes(YES)).append(AND);
		}
		if(managedAccountChecked != null){
			result.append("MANAGED_ACCOUNT_STATUS_IND = ").append(wrapInSingleQuotes(YES)).append(AND);
		}
		
        //CL 110234 Begin		
		String employmentStatus = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_10);
        
		// if "All" selected, exclude Cancelled employees for external users only
        // if "Active" selected, retrieve employees with blank or active status
        // else retrieve employees that have selected status
       // Note: This value is from the EMPLOYEE_VESTING_PARAMETER table, to get the value that is
       // effective (not last submitted) hence we use: EVP_ES.ES_val
        if (StringUtils.isEmpty(employmentStatus) || employmentStatus.equals(ALL)) {
            if (criteria.isExternalUser()) {
                result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') != 'C'").append(AND);
            }
        } else if (employmentStatus.equals("A")) {
            result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') in ('A','')").append(AND);
        } else {
            result.append("VALUE(EMPLOYMENT_STATUS_CODE,'') = ").append(
                   wrapInSingleQuotes(employmentStatus).
            toUpperCase()).append(AND);
        }
        //CL 110234 End
		if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()) ) ) {
			result.delete(result.length() - AND.length(), result.length());
		}
		
		return (result.toString().trim().length() > 0 ? result.toString() : null);
	}
	
}
