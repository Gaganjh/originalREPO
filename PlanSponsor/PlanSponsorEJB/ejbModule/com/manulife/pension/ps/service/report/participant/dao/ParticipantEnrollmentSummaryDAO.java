/*
 * Created on May 26, 2005
 *
 * This is the DAO for ParticipantEnrollmentSummary.
 */
package com.manulife.pension.ps.service.report.participant.dao;

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

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantDeferralChangesItem;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummary;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantEnrollmentSummaryReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;


public class ParticipantEnrollmentSummaryDAO extends BaseDatabaseDAO {

	private static final String className = ParticipantEnrollmentSummaryDAO.class.getName();
	
    private static final Logger logger = Logger.getLogger(ParticipantEnrollmentSummaryDAO.class);
    
    private static final String GET_PARTICIPANT_ENROLLMENT_SUMMARY =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_PARTICIPANT_ENROLLMENT_SUMMARY(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    
    private static final Map fieldToColumnMap = new HashMap();
    private static final String AND = " and ";
    
//  Make sure nobody instanciates this class
	private ParticipantEnrollmentSummaryDAO() {}
	
	static {
		fieldToColumnMap.put("firstName", "first_name");		
		fieldToColumnMap.put("lastName", "last_name");
		fieldToColumnMap.put("ssn", "social_security_no");
		fieldToColumnMap.put("birthDate", "birth_date");
		fieldToColumnMap.put("employerDesignatedID", "employer_designated_id");
		fieldToColumnMap.put("eligibleToDeferInd", "eligible_to_defer_ind");
		fieldToColumnMap.put("organizationUnitID", "organization_unit_id");
		fieldToColumnMap.put("enrollmentProcessedDate", "enrollment_processed_date");
		fieldToColumnMap.put("enrollmentMethod", "enrollment_method");
	}
	

	public static ReportData getParticipantEnrollmentSummary(ReportCriteria criteria) throws SystemException {		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantEnrollmentSummary");
			logger.debug("Report criteria -> " + criteria.toString());
		}
    
		Connection conn = null;
       	CallableStatement stmt = null;
       	ResultSet resultSet = null;
       	ParticipantEnrollmentSummaryReportData pesReportDataVO = null;
      		
		try {	
			// setup the connection and the statement
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_PARTICIPANT_ENROLLMENT_SUMMARY);
            
            if(logger.isDebugEnabled()) 
				logger.debug("Calling Stored Procedure: "+ GET_PARTICIPANT_ENROLLMENT_SUMMARY);
			
			int contractNumber = (new Integer((String)criteria.getFilterValue(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_1))).intValue();
			Date fromDate = (Date)criteria.getFilterValue(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_2);
			Date toDate = (Date)criteria.getFilterValue(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_3);

			stmt.setBigDecimal(1, intToBigDecimal(contractNumber) );

			if (fromDate == null)
				stmt.setNull(2, Types.DATE); 
			else
				stmt.setDate(2, new java.sql.Date(fromDate.getTime()));
				
			if (toDate == null)
				stmt.setNull(3, Types.DATE); 
			else
				stmt.setDate(3, new java.sql.Date(toDate.getTime()));
			
			String filter = createFilterPhrase(criteria);
			if (filter==null) {
				stmt.setNull(4, Types.VARCHAR);
			} else {
				stmt.setString(4, filter);
			}
	
			String sortPhrase = createSortPhrase(criteria);
		    if(logger.isDebugEnabled()) 
				logger.debug("Sort phrase: "+ sortPhrase);

		    if (sortPhrase == null || sortPhrase.trim().length()==0) {
		    	stmt.setNull(5, Types.VARCHAR);
		    } else {
		    	stmt.setString(5, sortPhrase);		// sort order
		    }
		    
			if ( criteria.getPageSize() == -1)
				stmt.setInt(6, 1000000);					// TODO: handle more gracefully
			else
				stmt.setInt(6, criteria.getPageSize());			
					
			stmt.setInt(7, criteria.getStartIndex()); 			    // start index	(aka readStartPosition)
			
//			 register the output parameters			
			stmt.registerOutParameter(8, Types.INTEGER);        	// Total enrollment count
			stmt.registerOutParameter(9, Types.INTEGER);        	// Total non empty payroll count
			stmt.registerOutParameter(10, Types.INTEGER);        	// Total organization unit count
			stmt.registerOutParameter(11, Types.INTEGER);        	// Total internet enrollment count
            stmt.registerOutParameter(12, Types.INTEGER);           // Total auto enrollment count
			stmt.registerOutParameter(13, Types.INTEGER);        	// Total beneficiary count
			stmt.registerOutParameter(14, Types.INTEGER);        	// Total Roth money type count
			stmt.registerOutParameter(15, Types.DECIMAL);        	// version
			
//			 execute the stored procedure
            stmt.execute();
			resultSet = stmt.getResultSet();
			
			int totalCount = stmt.getInt(8);
			int totalNonEmptyPayrollCount = stmt.getInt(9);
			int totalNonEmptyOrganizationUnitCount = stmt.getInt(10);
			int totalInternetEnrollmentCount = stmt.getInt(11);
            int totalAutoEnrollmentCount = stmt.getInt(12);
			int totalRothMoneyTypeCount = stmt.getInt(14);
			
//			 set the attributes in the value object
			pesReportDataVO = new ParticipantEnrollmentSummaryReportData(criteria, totalCount);

			List enrollmentSummaryList = new ArrayList();			
			ParticipantEnrollmentSummary pes = null;
			ArrayList contribItems = null;
			
			if (resultSet != null) {
			    
    			if(resultSet.next()) {
                    contribItems = new ArrayList();
                    
                    pes = createNewParticipantEnrollmentSummary(resultSet, contribItems, contractNumber);
                	
                    if(pes != null) {
                        enrollmentSummaryList.add(pes);
                    }
                    
        			while(resultSet.next()) {
        				//if pes.getDeferralCreatedTS() == null, it must be a enrollment summary new record
        				if(pes != null && pes.getDeferralCreatedTS() != null && pes.getDeferralCreatedTS().equals(resultSet.getTimestamp("created_ts")) && 
        						pes.getProfileId() == resultSet.getDouble("profile_id")) 
        				{
        					if("Entered".equalsIgnoreCase(resultSet.getString("deferral_comment"))) {
        						//Need to this in case the original entry (Trad) never had an entry with pct and amt 0 
        						pes.setDeferralComment(resultSet.getString("deferral_comment"));
        					}
        					
        					//We are in the same set, so add to old contribItems
        					contribItems.add(new ParticipantDeferralChangesItem(resultSet.getDouble("contribution_amt"),
        							resultSet.getDouble("contribution_pct"),
        							resultSet.getString("money_type_code")));
        				} else {
                            contribItems = new ArrayList();
                            pes = createNewParticipantEnrollmentSummary(resultSet, contribItems, contractNumber);
                            
                            if(pes != null) {
                                enrollmentSummaryList.add(pes);
                            }
                        }
        			}
                }
    			
			}
			
			pesReportDataVO.setDetails(enrollmentSummaryList);
			pesReportDataVO.setContractNumber(contractNumber); 
			pesReportDataVO.setFromDate(fromDate);
			pesReportDataVO.setToDate(toDate);
			pesReportDataVO.setNumberParticipantsWithEnrollments(totalCount);
			pesReportDataVO.setNonEmptyPayrollCount(totalNonEmptyPayrollCount);
			pesReportDataVO.setNonEmptyOrganizationUnitCount(totalNonEmptyOrganizationUnitCount);
			pesReportDataVO.setNumberInternetEnrollments(totalInternetEnrollmentCount);
            pesReportDataVO.setNumberAutoEnrollments(totalAutoEnrollmentCount);            
			pesReportDataVO.setNumberOfRothParticipants(totalRothMoneyTypeCount);			
		} catch (SQLException e) {
			e.printStackTrace();
           throw new SystemException(e, className, "getReportData", "Problem occurred during GET_PARTICIPANT_DEFERRAL_CHANGE stored proc call. Report criteria:"+criteria);
        } finally {
        	close(stmt, conn);
        }

		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantDeferralChanges");
		}
		
		return pesReportDataVO;
			
	}
	
    /**
     * Utility method that creates a new Participant Enrollment summary
     * 
     * @param resultSet
     * @param contribItems
     * @param pes
     * @param stmtBen
     * @param contractNumber
     * @throws SQLException
     */
    private static ParticipantEnrollmentSummary createNewParticipantEnrollmentSummary(ResultSet resultSet, ArrayList contribItems, int contractNumber ) throws SQLException {         
        boolean hasBeneficiary = false;
        ResultSet resultSetBen = null;
        ParticipantEnrollmentSummary pes = null;
            
        int profileId = resultSet.getInt("profile_id");
        
        pes = new ParticipantEnrollmentSummary(
                profileId,
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("social_security_no"),
                resultSet.getDate("birth_date"),
                resultSet.getString("employer_designated_id"),
                resultSet.getString("eligible_to_defer_ind"),
                resultSet.getString("organization_unit_id"),
                resultSet.getDate("enrollment_processed_date"),
                resultSet.getString("enrollment_method"),
                resultSet.getDate("MFC_CONTRACT_ENROLLMENT_DATE"),
                resultSet.getString("contribution_instruct_src_code"),
                null,
                resultSet.getString("deferral_comment"),
                resultSet.getString("PARTICIPANT_STATUS_CODE"),
                resultSet.getDate("NORMAL_RETIREMENT_DATE"),
                resultSet.getString("PLAN_ENTRY"),
                resultSet.getString("RESIDENCE_STATE_CODE"),
                resultSet.getString("CONTRIBUTION_STATUS"));
        
        pes.setDeferralCreatedTS(resultSet.getTimestamp("created_ts"));
        String moneyTypeCode = resultSet.getString("money_type_code");
        if(moneyTypeCode == null || moneyTypeCode.length()==0 )
            moneyTypeCode = "TRAD";
        
        contribItems.add(new ParticipantDeferralChangesItem(resultSet.getDouble("contribution_amt"),
                resultSet.getDouble("contribution_pct"),
                moneyTypeCode));
        
        
        pes.setDeferralItems(contribItems);
        
        return pes;
    }
		
		private static String createSortPhrase(ReportCriteria criteria) {
			StringBuffer result = new StringBuffer();
			Iterator sorts = criteria.getSorts().iterator();
			String sortDirection = null;
			String orjSortDirection = null;		

			for (int i=0; sorts.hasNext(); i++) {
				ReportSort sort = (ReportSort)sorts.next();
				
				if (i==0) orjSortDirection = sort.getSortDirection();
				
				if (sort.getSortField().trim().length() !=0) {
					sortDirection = sort.getSortDirection();
					if ("changeDate".equals(sort.getSortField()))
					{
						result.append("created_ts").append(' ');
						
						if ( i > 0 )
						{
							result.append("asc".equals(orjSortDirection) ? "desc":"asc");
							result.append(", last_name ").append("asc".equals(orjSortDirection) ? "desc":"asc");
							result.append(", first_name ").append("asc".equals(orjSortDirection) ? "desc":"asc");
						}
						else
						{
							result.append(sortDirection);
							result.append(", first_name ").append(sortDirection);					
						}
					}
					else if ("lastName".equals(sort.getSortField()))
					{
						result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
						result.append(sortDirection);				
						result.append(", first_name ").append("asc".equals(sortDirection) ? "desc":"asc");
					}
					else
					{
						result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
						result.append(sortDirection);
					}	
						
					result.append(',');
				}
			}
			
			if (result.length() > 0) {
				result.deleteCharAt(result.length() - 1);
			}
			return result.toString();
		}

	private static String createFilterPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		
		String lastNameValue = (String)criteria.getFilterValue(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_4);
		String ssnValue = (String)criteria.getFilterValue(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_5);
		String profileId = (String)criteria.getFilterValue(ParticipantEnrollmentSummaryReportData.FILTER_FIELD_6);
		
		if (lastNameValue != null && lastNameValue.trim().length() > 0 ) {
			result.append("UPPER(last_name) like ").append(wrapInSingleQuotes(lastNameValue + "%").toUpperCase() ).append(AND);
		}

		if (ssnValue != null && ssnValue.trim().length() > 0 ) {
			result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).append(AND);
		}
		if (profileId != null && profileId.trim().length() > 0 ) {
			result.append("ec.profile_Id = ").append(profileId).append(AND);
		}

		if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()) ) ) {
			result.delete(result.length() - AND.length(), result.length());
		}

		return (result.toString().trim().length() > 0 ? result.toString() : null);
	}
		

}
		

