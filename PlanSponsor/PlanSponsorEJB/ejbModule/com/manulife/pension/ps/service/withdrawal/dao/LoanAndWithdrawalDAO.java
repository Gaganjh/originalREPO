/**
 * Created on August 23, 2006
 */ 
package com.manulife.pension.ps.service.withdrawal.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalItem;
import com.manulife.pension.ps.service.withdrawal.valueobject.LoanAndWithdrawalReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

/**
 * This is the DAO for LoanAndWithdrawal report
 * 
 * @author Mihai Popa
 */
public class LoanAndWithdrawalDAO extends BaseDatabaseDAO {


	private static final String className = LoanAndWithdrawalDAO.class.getName();

	private static final Logger logger = Logger.getLogger(LoanAndWithdrawalDAO.class);
	
    private static final String GET_WITHDRAWALS_SP_SQL =
        "{call EZK100.GET_FEDERATED_WITHDRAWAL_INFO(?,?,?,?,?,?," +
                                                    "?,?,?,?,?," +
                                                    "?,?,?,?,?)}";    
	
    private static final String FEDERATED_DATA_SOURCE_NAME = CUSTOMER_DATA_SOURCE_NAME;
    
    
	private static Map<String, String> sortStrings = new HashMap<String, String>();
	static {

		
		sortStrings.put(LoanAndWithdrawalReportData.SORT_REQUEST_REASON + ":" + "asc",
				" FS1.REASON_CODE_DESC ASC, FS1.REQUEST_DATE ASC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_REQUEST_REASON + ":" + "desc",
				" FS1.REASON_CODE_DESC DESC, FS1.REQUEST_DATE DESC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_CONTRACT_NUMBER + ":" + "asc",
				" FS1.CONTRACT_ID DESC, FS1.REQUEST_DATE ASC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_CONTRACT_NUMBER + ":" + "desc",
				" FS1.CONTRACT_ID ASC, FS1.REQUEST_DATE DESC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_CONTRACT_NAME + ":" + "asc",
				" FS1.CONTRACT_NAME ASC, FS1.REQUEST_DATE ASC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_CONTRACT_NAME + ":" + "desc",
				" FS1.CONTRACT_NAME DESC, FS1.REQUEST_DATE DESC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_PARTICIPANT_NAME + ":" + "asc",
				" FS1.LAST_NAME ASC, FS1.FIRST_NAME ASC, FS1.REQUEST_DATE ASC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_PARTICIPANT_NAME + ":" + "desc",
				" FS1.LAST_NAME DESC, FS1.FIRST_NAME DESC, FS1.REQUEST_DATE DESC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_REQUEST_DATE + ":" + "asc",
				" FS1.REQUEST_DATE ASC, FS1.LAST_NAME ASC, FS1.FIRST_NAME ASC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_REQUEST_DATE + ":" + "desc",
				" FS1.REQUEST_DATE DESC, FS1.LAST_NAME DESC, FS1.FIRST_NAME DESC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_REFERENCE_NUMBER + ":" + "asc",
				" FS1.SUBMISSION_ID ASC");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_REFERENCE_NUMBER + ":" + "desc",
				" FS1.SUBMISSION_ID DESC");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_STATUS + ":" + "asc",
				" FS1.PROCESS_STATUS_DESC ASC, FS1.REQUEST_DATE ASC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_STATUS + ":" + "desc",
				" FS1.PROCESS_STATUS_DESC DESC, FS1.REQUEST_DATE DESC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_INITIATED_BY + ":" + "asc",
				" FS1.ROLE_DESC ASC, FS1.REQUEST_DATE ASC ");
		sortStrings.put(LoanAndWithdrawalReportData.SORT_INITIATED_BY + ":" + "desc",
				" FS1.ROLE_DESC DESC, FS1.REQUEST_DATE DESC ");		
	}    
	

	public static LoanAndWithdrawalReportData getReportData(
			ReportCriteria criteria) throws SystemException {
		LoanAndWithdrawalReportData returnData = getWithdrawals(criteria);
		return returnData;
	}

	/**
	 * Simple method to produce mockup data
	 * 
	 * @return
	 */
    private static LoanAndWithdrawalReportData getMockData(ReportCriteria criteria) {
    	
    	LoanAndWithdrawalReportData data = new LoanAndWithdrawalReportData(criteria, 6);
    	
    	ArrayList<LoanAndWithdrawalItem> requests = new ArrayList<LoanAndWithdrawalItem>(6);
    	
    	LoanAndWithdrawalItem requestItem1 = new LoanAndWithdrawalItem( new Date(), new Date(), "Withdrawal",
	              							"requestReason", "Bell", new Integer(85357), "Rob Norton", "123456789", new Date(), 
	              							new Integer(1), "14", "initiatedBy1", new Integer(116496357));
    	requests.add(requestItem1);
    	
    	LoanAndWithdrawalItem requestItem2 = new LoanAndWithdrawalItem( new Date(), new Date(), "Loan",
					"requestReason2", "Rogers", new Integer(85357),"Joe Part", "123456781", new Date(), 
					new Integer(2), "W6", "initiatedBy2", new Integer(116496358));
    	requests.add(requestItem2);
    	
    	LoanAndWithdrawalItem requestItem3 = new LoanAndWithdrawalItem( new Date(), new Date(), "Loan",
				"requestReason3", "contractName", new Integer(85357),"John Doe", "123456782", new Date(), 
				new Integer(3), "status", "initiatedBy3", new Integer(116496359));
    	requests.add(requestItem3);
    	
    	LoanAndWithdrawalItem requestItem4 = new LoanAndWithdrawalItem( new Date(), new Date(), "Withdrawal",
				"requestReason4", "contractName4", new Integer(85357),"Mark", "123456783", new Date(), 
				new Integer(4), "status", "initiatedBy4", new Integer(116496360));
    	requests.add(requestItem4);
    	
    	LoanAndWithdrawalItem requestItem5 = new LoanAndWithdrawalItem( new Date(), new Date(), "Withdrawal",
				"requestReason5", "contractName5", new Integer(85357),"Mark", "123456784", new Date(), 
				new Integer(5), "status", "initiatedBy5", new Integer(116496361));
    	requests.add(requestItem5);
    	
    	LoanAndWithdrawalItem requestItem6 = new LoanAndWithdrawalItem( new Date(), new Date(), "Withdrawal",
				"requestReason6", "contractName6", new Integer(85357),"Mark", "123456785", new Date(), 
				new Integer(6), "status", "initiatedBy6", new Integer(116496357));
    	requests.add(requestItem6);    	
    	
    	data.setDetails(requests);
    	return data;
    }
   

	public static LoanAndWithdrawalReportData getWithdrawals(ReportCriteria criteria) 
	throws SystemException {
		/*
		From Stored Procedure Declaration
		EZK100.GET_FEDERATED_CONTRACT_LIST

		IN PARAMS
		in_userProfileId decimal(10,0)
     	in_USNY_Ind char(2)
     	in_sortOrder varchar(254)
     	in_startingRecordNo decimal(6,0)
     	in_recordPerPage decimal(6,0)
     	in_DI_duration integer
     	in_securityRoleCode char(3)

	  	Set #1 -
			
		DYNAMIC RESULT SETS ?
		
		*/
		if (logger.isDebugEnabled() )
			logger.debug("entry -> getWithdrawals");

        Connection conn = null;
        CallableStatement statement = null;
        LoanAndWithdrawalReportData reportDataVO = new LoanAndWithdrawalReportData(criteria, 0);

        StringBuffer debugSb = new StringBuffer(1024);
        try {
           	// setup the connection and the statemnt
           	conn = getDefaultConnection(className, FEDERATED_DATA_SOURCE_NAME);
           	statement = conn.prepareCall(GET_WITHDRAWALS_SP_SQL);
           	if (logger.isDebugEnabled() )
				logger.debug("Calling Stored Procedure: GET_FEDERATED_WITHDRAWAL_INFO" );
        
           	List<Integer> searchableContractIds = (List<Integer>) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_SEARCHABLE_CONTRACTS);
            if ( searchableContractIds.size() == 0 ) {
                reportDataVO.setDetails(new ArrayList<LoanAndWithdrawalItem>());
                return reportDataVO;
            }
           	String contractIds = "";
           	Iterator<Integer> it = searchableContractIds.iterator();
           	while (it.hasNext()) {
           		contractIds += it.next().toString();
           		if ( it.hasNext() ) {
           			contractIds += ",";
           		}
           	}

           	statement.setString(1, contractIds);
           	
            // set the input parameters
           	statement.setBigDecimal(2, (BigDecimal)criteria
           			.getFilterValue(LoanAndWithdrawalReportData.FILTER_PROFILE_ID));

           	statement.setString(3, (String)criteria
           			.getFilterValue(LoanAndWithdrawalReportData.FILTER_SITE_LOCATION));

           	statement.setString(4, (String)criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_ROLE_CODE));

			statement.setBigDecimal(5, new BigDecimal(criteria.getStartIndex()));

            debugSb.append("call EZK100.GET_FEDERATED_WITHDRAWAL_INFO('").append(contractIds).append("'");
            String profileId = ((BigDecimal)criteria.getFilterValue(LoanAndWithdrawalReportData.FILTER_PROFILE_ID)).toPlainString();
            debugSb.append(", ").append(profileId);
            debugSb.append(", '").append((String)criteria.getFilterValue(LoanAndWithdrawalReportData.FILTER_SITE_LOCATION)).append("'");
            debugSb.append(", '").append((String)criteria.getFilterValue(LoanAndWithdrawalReportData.FILTER_ROLE_CODE)).append("'");
            String startIndex = String.valueOf(criteria.getStartIndex());
            debugSb.append(", ").append(startIndex);
			
			if(criteria.getPageSize() == ReportCriteria.NOLIMIT_PAGE_SIZE) {
				statement.setBigDecimal(6, null);
	            debugSb.append(", ").append("null");
			} else {
				statement.setBigDecimal(6, new BigDecimal(criteria.getPageSize()));
	            debugSb.append(", ").append(String.valueOf(criteria.getPageSize()));
			}
 	
			// request from date filter
			Date requestFromDate = (Date) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_REQUEST_FROM_DATE);
			java.sql.Date sqlFromDate = new java.sql.Date(requestFromDate.getTime());
			statement.setDate(7, sqlFromDate);
            debugSb.append(", ").append(sqlFromDate.toString());

			// request to date filter
			Date requestToDate = (Date) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_REQUEST_TO_DATE);
			java.sql.Date sqlToDate = new java.sql.Date(requestToDate.getTime());				
			statement.setDate(8, sqlToDate);
            debugSb.append(", ").append(sqlToDate.toString());
			
			String requestType = (String) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_REQUEST_TYPE);
			if(requestType != null) {
				statement.setString(9, requestType);
	            debugSb.append(", '").append(requestType).append("'");
			} else {
				statement.setNull(9, Types.VARCHAR);
	            debugSb.append(", ").append("null");
			}
			
			String requestReason = (String) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_REQUEST_REASON);
			if (requestReason != null) {
				statement.setString(10, requestReason);			
	            debugSb.append(", '").append(requestReason).append("'");
			} else {
				statement.setNull(10, Types.CHAR);
	            debugSb.append(", ").append("null");
			}

			//request status
			String requestStatus = (String) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_REQUEST_STATUS);
			if (requestStatus != null) {
				StringBuffer buf = new StringBuffer();
				StringTokenizer st = new StringTokenizer(requestStatus, ",");
				debugSb.append(", '");
				while (st.hasMoreTokens()) {
					String nextToken = st.nextToken().trim();
					buf.append("'").append(nextToken).append("\', ");
					debugSb.append("''").append(nextToken).append("'', ");
				}
				int last = buf.length();
				buf.delete(last - 2, last);
				statement.setString(11, buf.toString());
	            debugSb.append("'");

			} else {
				statement.setNull(11, Types.CHAR);
	            debugSb.append(", ").append("null");			}
			
			//participant last name
			String lastName = (String) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_LAST_NAME);
			if (lastName != null) {
				statement.setString(12, lastName.toUpperCase());
				debugSb.append(", '").append(lastName.toUpperCase()).append("'");
			} else {
				statement.setNull(12, Types.CHAR);
	            debugSb.append(", ").append("null");
			}
			
			//SSN
			String ssn = (String) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_SSN);
			if (ssn != null) {
				statement.setString(13, ssn);			
				debugSb.append(", '").append(ssn).append("'");
			} else {
				statement.setNull(13, Types.CHAR);
	            debugSb.append(", ").append("null");
			}

			//contract id
           	Integer contractId = (Integer)criteria
           			.getFilterValue(LoanAndWithdrawalReportData.FILTER_CONTRACT_NUMBER);
           	if(contractId != null) {
               	statement.setInt(14, (Integer)criteria
               			.getFilterValue(LoanAndWithdrawalReportData.FILTER_CONTRACT_NUMBER));
				debugSb.append(", ").append((Integer)criteria.getFilterValue(LoanAndWithdrawalReportData.FILTER_CONTRACT_NUMBER));
               	
           	} else {
           		//statement.setNull(13, Types.INTEGER);
           		statement.setInt(14, 0);
				debugSb.append(", 0");
           		
           	}
           	
			//contract name
			String contractName = (String) criteria
					.getFilterValue(LoanAndWithdrawalReportData.FILTER_CONTRACT_NAME);
			if (contractName != null) {
				statement.setString(15, contractName.toUpperCase());			
				debugSb.append(", '").append(contractName.toUpperCase()).append("'");
			} else {
				statement.setNull(15, Types.CHAR);
				debugSb.append(", ").append("null");
			}
			
			String sortOrder = generateSortString(criteria);
			statement.setString(16, sortOrder);
			debugSb.append(", '").append(sortOrder).append("')");
			if (logger.isDebugEnabled()){
				logger.debug("DB2 CALL FOR LOANS AND WITHDRAWLS = " + debugSb.toString());
			}
			StopWatch stopWatch = new StopWatch();
			try {
				stopWatch.start();
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.start() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
			statement.execute();
			int participantCount = 0;

			ResultSet resultSet = statement.getResultSet();
			
			if (resultSet != null) {
			    
    			if (resultSet.next()) {
    				participantCount = resultSet.getInt(1);
    				reportDataVO = new LoanAndWithdrawalReportData(criteria,
    						participantCount);
    				reportDataVO.setTotalCount(participantCount);
    			}
    			
			}
			
			if (statement.getMoreResults()) {
				resultSet = statement.getResultSet();
				Collection<LoanAndWithdrawalItem> details = new ArrayList();
				
				if (resultSet != null) {
				    
    				while (resultSet.next()) {
    					LoanAndWithdrawalItem item = new LoanAndWithdrawalItem();
    					item.setFirstName(resultSet.getString("FIRST_NAME"));
    					item.setLastName(resultSet.getString("LAST_NAME"));
    					item
    							.setMiddleInitial(resultSet
    									.getString("MIDDLE_INITIAL"));
    					item.setContractName(resultSet.getString("CONTRACT_NAME"));
    					item.setContractNumber(resultSet.getInt("CONTRACT_ID"));
    					item.setInitiatedBy(resultSet.getString("ROLE_DESC"));
    					item.setRequestDate(resultSet.getDate("REQUEST_DATE"));
    					item.setRequestReason(resultSet
    							.getString("REASON_CODE_DESC"));
    					item.setRequestType(resultSet
    							.getString("SUBMISSION_CASE_TYPE_CODE"));
    					item.setSsn(resultSet.getString("SOCIAL_SECURITY_NO"));
    					item.setProfileId(resultSet.getInt("PROFILE_ID"));
    					item.setStatus(resultSet.getString("PROCESS_STATUS_DESC"));
    					item.setStatusCode(resultSet
    							.getString("PROCESS_STATUS_CODE"));
    					item.setReferenceNumber(resultSet.getInt("SUBMISSION_ID"));
    					item.setCreatedByUserProfileId(resultSet
    							.getInt("CREATED_USER_PROFILE_ID"));
    					// participantCount++;
    					details.add(item);
    				}
    				
				}
				
				reportDataVO.setDetails(details);
			}         	   

			try {
				stopWatch.stop();

				if (logger.isInfoEnabled()) {
					BigDecimal userProfileId = (BigDecimal) criteria
							.getFilterValue(LoanAndWithdrawalReportData.FILTER_PROFILE_ID);
					logger.info("User Profile ID [" + userProfileId
							+ "] # of Contracts: ["
							+ searchableContractIds.size() + "] Criteria ["
							+ criteria + "] Stored procedure timing: "
							+ stopWatch.toString());
				}
			} catch (IllegalStateException e) {
				final String message = "@@@@@ IllegalStateException from stopWatch.stop() in thread "
						+ Thread.currentThread().toString();
				logger.error(message);
			}
        } catch (SQLException e) {
            throw new SystemException(e, LoanAndWithdrawalDAO.class.getName(), "getWithdrawals", "Failed during GET_FEDERATED_WITHDRAWAL_INFO stored proc call.");
        } finally {
        	close(statement, conn);
        }

        if (logger.isDebugEnabled() )
			logger.debug("exit <- getWithdrawals");
		
        return reportDataVO;
    }	
	
	/**
	 * Finds (or not) a sql string in a list of predefined sort strings
	 * 
	 * @param criteria
	 * @return
	 */
	private static String generateSortString(ReportCriteria criteria) {
		ReportSortList sorts = criteria.getSorts();		
		if (sorts != null && sorts.size() != 0) {
			StringBuffer  buf = new StringBuffer("");
			ReportSort firstSort = (ReportSort) sorts.get(0);
			String searchKey = firstSort.getSortField() + ":" + firstSort.getSortDirection();
			buf.append(sortStrings.containsKey(searchKey) ? (String)sortStrings.get(searchKey) : "");
			return buf.toString();
		}
		return null;
	}
	
}


/*
select contract_id contractId, 
submission_case_type_code requestType,
withdrawal_reason_code requestReason, 
contract_name contractName, 
contract_id contarctNumber, 
last_name lastName, 
first_name firstName,
participant_ssn ssn,
request_date requestDate,
created_user_profile_id initiatedBy
from
STP100.submission_withdrawal
where contract_id = 85357;




insert into STP100.submission_withdrawal
(contract_id, submission_id,
submission_case_type_code, profile_id, 
participant_id, residence_state_code,
withdrawal_reason_code, payment_to_code, 
request_date, expiration_date,
created_user_profile_id, created_ts, 
last_updated_user_profile_id, last_updated_ts)
VALUES
(
85357, 1,
'W', 116495961,
1, 'CA',
'RE', 'PA',
'2004-03-09', '2006-10-10',
1, '2004-02-08 04:14:11.913636',
1, '2004-02-08 04:14:11.913636');



insert into STP100.Submission 
(Submission_id, submission_ts, 
file_name, map_name,
input_location_name, user_id,
created_user_id, created_ts,
last_updated_user_id, last_updated_ts,
payment_info_only_ind
)
VALUES
(
1, '2004-02-08 04:14:11.913636',
'c:/SubmissionFilename', 'MFS.djs',
'', 'user_id',
'STP', '2004-02-08 04:14:11.913636',
'STP', '2004-02-08 04:14:11.913636',
'n'
)

insert into STP100.Submission_CASE 
(Submission_id, Contract_id, 
Submission_case_type_code, syntax_error_ind,
processed_ts, process_status_code,
created_user_id, created_ts,
last_updated_user_id, last_updated_ts,
submit_count
)
VALUES
(
1, 85357,
'W', 'N',
'2004-02-08 04:14:11.913636', '16',
'STP', '2004-02-08 04:14:11.913636',
'STP', '2004-02-08 04:14:11.913636',
1
)

select contract_id contractId, 
  submission_case_type_code requestType, withdrawal_reason_code requestReason, 
  contract_id contractNumber, 
  request_date requestDate, 
  created_user_profile_id initiatedBy 
from STP100.submission_withdrawal 
where contract_id = 85357  
and request_date > '2006-01-09 04:14:11.913636'   
and request_date < '2006-01-09 04:14:11.913636'  


*/ 
