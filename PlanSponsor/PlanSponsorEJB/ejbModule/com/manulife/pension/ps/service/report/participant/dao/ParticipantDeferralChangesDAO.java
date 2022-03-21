package com.manulife.pension.ps.service.report.participant.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.intware.dao.DAOException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantDeferralChangesDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantDeferralChangesItem;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantDeferralChangesReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData;
import com.manulife.pension.service.account.dao.ContractDAO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * This is the DAO for Participant Deferral Changes
 * 
 */
public class ParticipantDeferralChangesDAO extends BaseDatabaseDAO {
	
	private static final String className = ParticipantDeferralChangesDAO.class.getName();
	
    private static final Logger logger = Logger.getLogger(ParticipantDeferralChangesDAO.class);
	
	private static final String GET_PARTICIPANT_DEFERRAL_CHANGE =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_PARTICIPANT_DEFERRAL_CHANGE(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	private static final String UPDATE_DEFERRAL_PROCESS_IND = 
		"{call " + CUSTOMER_SCHEMA_NAME + "UPDATE_DEFERRAL_PROCESS_IND(?,?,?,?,?)}";
		
	private static final Map fieldToColumnMap = new HashMap();
	private static final String AND = " and ";
	
	// Make sure nobody instanciates this class
	private ParticipantDeferralChangesDAO() {
	}
	
	static {
		fieldToColumnMap.put("firstName", "first_name");		
		fieldToColumnMap.put("lastName", "last_name");
		fieldToColumnMap.put("ssn", "social_security_no");
		fieldToColumnMap.put("changeDate", "created_ts");
		fieldToColumnMap.put("employerDesignatedID", "payroll_no");
		fieldToColumnMap.put("organizationUnitID", "organization_unit_id");
		fieldToColumnMap.put("contributionStatus", "status");
		fieldToColumnMap.put("contributionAmt", "contribution_amt");
		fieldToColumnMap.put("contributionPct", "contribution_pct");
		fieldToColumnMap.put("processedInd", "processed_ind");
	}
	
		
	public static int updateDeferralIndicator(int contractNumber, double profileId, 
		Timestamp createTS, boolean processInd) throws SystemException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> updateDeferralIndicator");
		}

		Connection conn = null;
		CallableStatement stmt = null;
		int updatedRows = 0;
		try {	
			// setup the connection and the statement
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(UPDATE_DEFERRAL_PROCESS_IND);
            
            if(logger.isDebugEnabled()) 
				logger.debug("Calling Stored Procedure: "+ UPDATE_DEFERRAL_PROCESS_IND);
				
			String processIndStr = "N";
			if(processInd)
				processIndStr = "Y";
				
			stmt.setBigDecimal(1, intToBigDecimal(contractNumber) );
			stmt.setBigDecimal(2, new BigDecimal(profileId));
			stmt.setTimestamp(3, createTS);
			stmt.setString(4, processIndStr);
			stmt.registerOutParameter(5, Types.INTEGER);
			
			stmt.execute();
			
			updatedRows = stmt.getInt(5);
			
		} catch (SQLException e) {
			e.printStackTrace();
           throw new SystemException(e, className, "getReportData", "Problem occurred during UPDATE_DEFERRAL_PROCESS_IND stored proc call.");
        } finally {
        	close(stmt, conn);
        }

		if(logger.isDebugEnabled()) {
			logger.debug("exit <- updateDeferralIndicator");
		}
		
		return updatedRows;
	}
	
	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantDeferralChanges");
			logger.debug("Report criteria -> " + criteria.toString());
		}
			
		Connection conn = null;
       	CallableStatement stmt = null;
       	ResultSet resultSet = null;
       	ParticipantDeferralChangesReportData pdcReportDataVO = null;
      		
		try {	
			// setup the connection and the statement
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            stmt = conn.prepareCall(GET_PARTICIPANT_DEFERRAL_CHANGE);
            
            if(logger.isDebugEnabled()) 
				logger.debug("Calling Stored Procedure: "+ GET_PARTICIPANT_DEFERRAL_CHANGE);
			
			int contractNumber = (new Integer((String)criteria.getFilterValue(ParticipantDeferralChangesReportData.FILTER_FIELD_1))).intValue();
			Date fromDate = (Date)criteria.getFilterValue(ParticipantDeferralChangesReportData.FILTER_FIELD_2);
			Date toDate = (Date)criteria.getFilterValue(ParticipantDeferralChangesReportData.FILTER_FIELD_3);
			Integer procIndObj = (Integer)criteria.getFilterValue(ParticipantDeferralChangesReportData.FILTER_FIELD_4);
	        
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(toDate);
	        cal.add(Calendar.DAY_OF_WEEK, 1);
	        
	        toDate = cal.getTime();
	        
							
			stmt.setBigDecimal(1, intToBigDecimal(contractNumber) );

			if (fromDate == null)
				stmt.setNull(2, Types.DATE); 
			else
				stmt.setDate(2, new java.sql.Date(fromDate.getTime()));
				
			if (toDate == null)
				stmt.setNull(3, Types.DATE); 
			else
				stmt.setDate(3, new java.sql.Date(toDate.getTime()));
			
			stmt.setInt(4, procIndObj.intValue());
					    
			String sortPhrase = createSortPhrase(criteria);
		    if(logger.isDebugEnabled()) 
				logger.debug("Sort phrase: "+ sortPhrase);

			stmt.setString(5, sortPhrase);		// sort order
			if ( criteria.getPageSize() == -1)
				stmt.setInt(6, 1000000);					// TODO: handle more gracefully
			else
				stmt.setInt(6, criteria.getPageSize());			
					
			stmt.setInt(7, criteria.getStartIndex()); 			    // start index	(aka readStartPosition)
			
			// register the output parameters
			stmt.registerOutParameter(8, Types.INTEGER);        	// Total record count
			stmt.registerOutParameter(9, Types.INTEGER);        	// Total participant count
			stmt.registerOutParameter(10, Types.INTEGER);        	// Total non empty payroll count
			stmt.registerOutParameter(11, Types.INTEGER);        	// Total non empty organization unit id count
			stmt.registerOutParameter(12, Types.INTEGER);			// Total ROTH count
			stmt.registerOutParameter(13, Types.DECIMAL);        	// version
            	
            // execute the stored procedure
            stmt.execute();
			resultSet = stmt.getResultSet();
			
			int totalCount = stmt.getInt(8);
			int totalParticipantCount = stmt.getInt(9);
			int totalNonEmptyPayrollCount = stmt.getInt(10);
			int totalNonEmptyOrganizationUnitCount = stmt.getInt(11);
			int totalRothCount = stmt.getInt(12);
						
			// set the attributes in the value object
			pdcReportDataVO = new ParticipantDeferralChangesReportData(criteria, totalCount);
			

			//List participantSummaryDetails = new ArrayList();
			List deferralChangeList = new ArrayList();
			
			ParticipantDeferralChangesDetails detail = null;
			ParticipantDeferralChangesDetails detailTemp = null;
			ArrayList contribItems = null;
			
			if (resultSet != null) {
			    
    			if (resultSet.next()) {
    				contribItems = new ArrayList();	
    				String processIndStr = resultSet.getString("processed_ind");
    				boolean processInd = false;
    				if("Y".equalsIgnoreCase(processIndStr))
    					processInd = true;
    				
    				detail = new ParticipantDeferralChangesDetails(
    						resultSet.getString("first_name"),
    						resultSet.getString("last_name"),
    						resultSet.getString("SOCIAL_SECURITY_NO"),
    						resultSet.getTimestamp("created_ts"),
    						resultSet.getString("payroll_no"),
    						resultSet.getString("organization_unit_id"),
    						resultSet.getString("status"),
    						resultSet.getDouble("profile_id"),
    						null,
    						processInd);
    				detail.setChangeDate(new Date(detail.getChangeTS().getTime()));
    				
    				contribItems.add(new ParticipantDeferralChangesItem(resultSet.getDouble("contribution_amt"),
    						resultSet.getDouble("contribution_pct"),
    						resultSet.getString("money_type_code")));
    				
    				detail.setDeferralItems(contribItems);
    				deferralChangeList.add(detail);
                    
                    while(resultSet.next())
                    {
                        if(detail.getChangeTS().equals(resultSet.getTimestamp("created_ts")) && 
                                detail.getProfileId() == resultSet.getDouble("profile_id")) 
                        {
                            //We are in the same set, so add to old contribItems
                            contribItems.add(new ParticipantDeferralChangesItem(resultSet.getDouble("contribution_amt"),
                                    resultSet.getDouble("contribution_pct"),
                                    resultSet.getString("money_type_code")));
                        } else {
                            //create a new detail object
                            contribItems = new ArrayList(); 
                            processIndStr = resultSet.getString("processed_ind");
                            processInd = false;
                            if("Y".equalsIgnoreCase(processIndStr))
                                processInd = true;
                            
                            detail = new ParticipantDeferralChangesDetails(
                                    resultSet.getString("first_name"),
                                    resultSet.getString("last_name"),
                                    resultSet.getString("SOCIAL_SECURITY_NO"),
                                    resultSet.getTimestamp("created_ts"),
                                    resultSet.getString("payroll_no"),
                                    resultSet.getString("organization_unit_id"),
                                    resultSet.getString("status"),
                                    resultSet.getDouble("profile_id"),
                                    null,
                                    processInd);
                            detail.setChangeDate(new Date(detail.getChangeTS().getTime()));
                            
                            contribItems.add(new ParticipantDeferralChangesItem(resultSet.getDouble("contribution_amt"),
                                    resultSet.getDouble("contribution_pct"),
                                    resultSet.getString("money_type_code")));
                            
                            detail.setDeferralItems(contribItems);
                            deferralChangeList.add(detail);
                        }               
                    }
    			}
			
			}
			
			/*
			for(int i=0; i<deferralChangeList.size(); i++)
			{
				ParticipantDeferralChangesDetails PDCD = (ParticipantDeferralChangesDetails)deferralChangeList.get(i);
				System.out.println(PDCD.getChangeTS());
				System.out.println(PDCD.getChangeDate());
			}
			*/
			
			
			//If search criteria is created_ts, perform sort on names
			Iterator sorts = criteria.getSorts().iterator();
			for (int i=0; sorts.hasNext(); i++) {
				ReportSort sort = (ReportSort)sorts.next();
				if ("changeDate".equals(sort.getSortField())) {
					secondarySortForCreateTS(deferralChangeList, "asc");
				}
				break;
			}
			
			pdcReportDataVO.setContractNumber(contractNumber); 
			pdcReportDataVO.setFromDate(fromDate);
			pdcReportDataVO.setToDate(toDate);
			pdcReportDataVO.setNumberParticipantsChanged(totalParticipantCount);
			pdcReportDataVO.setDetails(deferralChangeList);
			pdcReportDataVO.setNonEmptyPayrollCount(totalNonEmptyPayrollCount);
			pdcReportDataVO.setNonEmptyOrganizationUnitCount(totalNonEmptyOrganizationUnitCount);
			pdcReportDataVO.setRothCount(totalRothCount);
			
			//Retrieve deferral instructions indicator from AccountService's ContractDAO
			//Should probably expose this method via AccountService....
			boolean deferralInd = ContractDAO.getDeferralInstructionInd((new Integer(contractNumber)).toString());
			pdcReportDataVO.setDeferralInstructionInd(deferralInd);
			
		} catch (DAOException e) {
			e.printStackTrace();
	           throw new SystemException(e, className, "getReportData", "Problem occurred during call to Account Service's ContractDAO.getDeferralInstructionInd(). Report criteria:"+criteria);
	    }catch (SQLException e) {
			e.printStackTrace();
           throw new SystemException(e, className, "getReportData", "Problem occurred during GET_PARTICIPANT_DEFERRAL_CHANGE stored proc call. Report criteria:"+criteria);
        } finally {
        	close(stmt, conn);
        }

		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantDeferralChanges");
		}
		
		return pdcReportDataVO;
	}
	
	public static void secondarySortForCreateTS(List deferralChangeList, String sortDirection) {
		
		for(int m=0; m<deferralChangeList.size(); m++) {
			ParticipantDeferralChangesDetails item1 = 
				(ParticipantDeferralChangesDetails)deferralChangeList.get(m);
			String name1 = item1.getLastName() + item1.getFirstName();
		
			for(int n=m+1; n<deferralChangeList.size(); n++) {
				ParticipantDeferralChangesDetails item2 = 
					(ParticipantDeferralChangesDetails)deferralChangeList.get(n);
				String name2 = item2.getLastName() + item2.getFirstName();
				if(item1.getChangeDate().equals(item2.getChangeDate())) {
					if(name1.compareTo(name2) < 0) {
						if("desc".equalsIgnoreCase(sortDirection)) {
							//Make the switch
							deferralChangeList.set(m, item2);
							deferralChangeList.set(n, item1);
							item1 = item2;
							name1 = name2;
						}
					} else if(name1.compareTo(name2) > 0) {
						if("asc".equalsIgnoreCase(sortDirection)) {
							//Make Switch
							deferralChangeList.set(m, item2);
							deferralChangeList.set(n, item1);
							item1 = item2;
							name1 = name2;
						}
					}
				}//End if statement for comparing change date 
								
			}//End for(int n=m+1; n<deferralChangeList.size(); n++)
		}
					
	}
	
	private static String createSortPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		Iterator sorts = criteria.getSorts().iterator();
		String sortDirection = null;
		String orjSortDirection = null;		

		for (int i=0; sorts.hasNext(); i++) {
			ReportSort sort = (ReportSort)sorts.next();
			
			if (i==0) orjSortDirection = sort.getSortDirection();
			
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
		
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		return result.toString();
	}
	
	private static String createFilterPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		String lastNameValue = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_4);
		String ssnValue = (String)criteria.getFilterValue(ParticipantSummaryReportData.FILTER_FIELD_5);

		if (lastNameValue != null && lastNameValue.trim().length() > 0 ) {
			result.append("UPPER(last_name) like ").append(wrapInSingleQuotes(lastNameValue + "%").toUpperCase() ).append(AND);
		}

		if (ssnValue != null && ssnValue.trim().length() > 0 ) {
			result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).append(AND);
		}

		if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()) ) ) {
			result.delete(result.length() - AND.length(), result.length());
		}

		return (result.toString().trim().length() > 0 ? result.toString() : null);
	}
	
	
}

