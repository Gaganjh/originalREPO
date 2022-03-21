package com.manulife.pension.ps.service.report.participant.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ejb.EJBException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresDetails;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresReportData;
import com.manulife.pension.ps.service.report.participant.valueobject.ParticipantForfeituresTotals;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * ParticipantForfeituresDAO class to retrieve the Accounts Forfeitures details page 
 * information from the CSDB Database to the Web application.
 * 
 * @author Vinothkumar Balasubramaniyam
 */
public class ParticipantForfeituresDAO extends BaseDatabaseDAO {

	// Constant Entries
	private static final String className = ParticipantForfeituresDAO.class.getName();
    private static final Logger logger = Logger.getLogger(ParticipantForfeituresDAO.class);
	private static final String AND = " and ";
	private static final String AS_OF_DATE = "AS_OF_DATE";
	private static final String NO_OF_PARTICIPANTS = "NO_OF_PARTICIPANTS";
	private static final String SORT_DIRECTION_ASC = "asc";
	private static final String SORT_DIRECTION_DESC = "desc";	

	private static Date currentAsOfDate = null;
    
    //Map to hold form fields & column mapping
    private static final Map<String, String> fieldToColumnMap = new HashMap<String,String>();    
	static {
		fieldToColumnMap.put("firstName", "first_name");		
		fieldToColumnMap.put("lastName", "last_name");
		fieldToColumnMap.put("ssn", "social_security_no");
		fieldToColumnMap.put("division", "employer_division");
		fieldToColumnMap.put("status", "status");
		fieldToColumnMap.put("employeeAssets", "employee_asset");
		fieldToColumnMap.put("employerAssets", "employer_asset");
		fieldToColumnMap.put("totalAssets", "total_balance");
		fieldToColumnMap.put("forfeitures", "forfeitures");
		fieldToColumnMap.put("terminationDate", "termination_date");
	}
	
	/**
	 * Default Private Constructor to make sure nobody instantiate this class
	 */
	private ParticipantForfeituresDAO() {}

	/**
	 * Method to retrieve the Accounts Forfeitures Information
	 * 
	 * @param criteria
	 * 			as ReportCriteria
	 * @return ReportData
	 * @throws SystemException
	 */
	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}
			
      	ParticipantForfeituresReportData pfReportDataVO = null;
      	ParticipantForfeituresTotals participantForfeituresTotals = null;
      	List<ParticipantForfeituresDetails> participantForfeituresDetails = null;
      	
		Connection connection = null;
		PreparedStatement preparedStatement = null;
      		
		try {
			
			//Get the connection object which will be used for invoking the helper methods
			connection = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
			
			//Get the contract number from ReportCriteria Object
			int contractNumber = getContractNumber(criteria).intValue();
			
			//Get the Sort criteria and assign into ReportCriteria Object
			String sortCriteria = createSortPhrase(criteria);
			if (!StringUtils.isEmpty(sortCriteria)) {
				criteria.addFilter(ParticipantForfeituresReportData.FILTER_SORT_CRITERIA, 
						sortCriteria);
			}
			
			//Get the Filter criteria and assign into ReportCriteria Object
			String filterCriteria = createFilterPhrase(criteria);
			if (!StringUtils.isEmpty(filterCriteria)) {
				criteria.addFilter(ParticipantForfeituresReportData.FILTER_FILTER_CRITERIA, 
						filterCriteria);
			}
			
			//Assigning the current AsOfDate from the DB.
			//To avoid the redundant calls, it has been stored in instance variable.
			currentAsOfDate = getCurrentAsOfDate(connection, preparedStatement);
			
			//Get the Total Number of Participants
			int totalPartcipantsCount = getTotalParticipants(criteria, connection, preparedStatement);
			
			//Get the Number of Participants for the given Search Criteria
			//so that the exact number of records can be controlled through Pagination
			int searchResultsCount = getParticipantsCountForSearchCriteria(criteria, 
					connection, preparedStatement);

			//Get the Forfeitures Summary 
			participantForfeituresTotals = getParticipantsSummary(criteria, 
					totalPartcipantsCount, searchResultsCount, connection, preparedStatement);
			
			//Get the Participant Details
			participantForfeituresDetails = getParticipantDetails(criteria, connection, preparedStatement);
			
			// set the attributes in the value object
			pfReportDataVO = new ParticipantForfeituresReportData(criteria, searchResultsCount);
			
			//Assign the values into ReportData Value Object
			pfReportDataVO.setContractNumber(contractNumber);
			pfReportDataVO.setDetails(participantForfeituresDetails);
			pfReportDataVO.setParticipantForfeituresTotals(participantForfeituresTotals);
			
		} catch (Exception exception) {
           throw new SystemException(exception,  
        		   "Problem occurred during Accounts Forfeitures details. Report criteria:"+criteria);
        } finally {
        	close(preparedStatement, connection);
        }

		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}
		
		return pfReportDataVO;
			
	}
	
	/**
	 * Gets the Contract Number from the ReportCriteria and converts 
	 * to an Integer object
	 * 
	 * @param criteria
	 *            The ReportCriteria that contains the filter.
	 * @return The contract number as an Integer
	 */
	private static Integer getContractNumber(ReportCriteria criteria) {
		try {
			return Integer.valueOf((String) criteria.getFilterValue(
					ParticipantForfeituresReportData.FILTER_CONTRACT_NUMBER));
		} catch (NumberFormatException numberFormatException) {
			logger.error("Received NumberFormatException. Can't format " +
					"contract number to Integer. ",
					numberFormatException);
			throw new EJBException(numberFormatException);
		}
	}
	
	/**
	 * Method to retrieve the current As Of Date
	 * 
	 * @param connection
	 *			The database connection to use
	 * @param statement
	 *			The PreparedStatement   
	 * @return java.sql.Date
	 * 			as Current AsOfDate 
	 * @throws SystemException
	 * @throws SQLException
	 */
	private static Date getCurrentAsOfDate(Connection connection, 
			PreparedStatement statement) throws SystemException, SQLException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getCurrentAsOfDate");
		}
		
		String asOfDateQuery = null;		
		ResultSet rs = null;
		Date asOfDate = null;
		
		try {
			asOfDateQuery = ParticipantForfeituresQueryHelper.getCurrentAsOfDateSQL();
			statement = connection.prepareStatement(asOfDateQuery);
			rs = statement.executeQuery();
			
			if (rs !=null && rs.next()) {	
				asOfDate = rs.getDate(AS_OF_DATE);			        							        					
			}
			
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException,
					"Something goes wrong with the statement - while reading the as of date.");
		} finally {
			closeResultSet(rs);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getCurrentAsOfDate");
		}
		
		return asOfDate;
	}

	/**
	 * Method to retrieve the Total Number of Participants 
	 * 
	 * @param criteria
	 * 			as ReportCriteria
	 * @param connection
	 *			The database connection to use
	 * @param statement
	 *			The PreparedStatement            
	 * @return ParticipantForfeituresTotals
	 * @throws SystemException
	 * @throws SQLException
	 */
	private static int getTotalParticipants(ReportCriteria criteria,
			Connection connection, PreparedStatement statement) 
			throws SystemException, SQLException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getTotalParticipants");
		}
		
		String totalParticipantsSQL = null;
		ResultSet rs = null;
		int participantsCount = 0;
		
		try {
			totalParticipantsSQL = ParticipantForfeituresQueryHelper.getTotalParticipantsSQL();
			statement = connection.prepareStatement(totalParticipantsSQL);
			statement.setInt(1, getContractNumber(criteria).intValue());
			
			rs = statement.executeQuery();
			
			if (rs != null) { 
				while (rs.next()){
					participantsCount = rs.getInt(NO_OF_PARTICIPANTS);			        							        					
				}
			}
			
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException, 
					"SQLException - Problem retrieving total number of participants. " +
					"Report criteria:"+criteria);				
		} finally {
			closeResultSet(rs);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getTotalParticipants");
		}
		return participantsCount;
	}
	
	/**
	 * Method to get the number of participants for given search criteria
	 * 
	 * @param criteria
	 * 			ReportCriteria Object
	 * @param connection
	 *			The database connection to use
	 * @param statement
	 *			The PreparedStatement
	 * @return integer 
	 * 		as Number of participants for given search criteria
	 * @throws SystemException 
	 * @throws SQLException 
	 */	
	private static int getParticipantsCountForSearchCriteria(
			ReportCriteria criteria,
			Connection connection, 
			PreparedStatement statement) throws SystemException, SQLException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantsCountForSearchCriteria");
		}
		
		ResultSet rs = null;
		String participantsCountSQL = null;
		int recordCount = 0;
		
		try {
			participantsCountSQL = ParticipantForfeituresQueryHelper.
				getParticipantsCountForSearchCriteriaSQL(criteria);
			statement = connection.prepareStatement(participantsCountSQL);
			int contractNumber = getContractNumber(criteria).intValue();
			statement.setInt(1, contractNumber);
			statement.setInt(2, contractNumber);
			statement.setInt(3, contractNumber);
			statement.setInt(4, contractNumber);
			statement.setInt(5, contractNumber);
			
			//Get the asOfDate
			String asOfDateStr = (String)criteria.getFilterValue(
					ParticipantForfeituresReportData.FILTER_ASOFDATE);
	        java.sql.Date asOfDate = null;
	        if(asOfDateStr !=null && asOfDateStr.length() !=0 ) { 
	        	asOfDate = new java.sql.Date(Long.parseLong(asOfDateStr));
	        }
	        
			if (asOfDate == null){
				statement.setDate(6, currentAsOfDate);
			} else {
				statement.setDate(6, asOfDate);
			}
			
			statement.setInt(7, contractNumber);
			statement.setInt(8, contractNumber);
			
			rs = statement.executeQuery();
			
			if  (rs != null && rs.next()){
				recordCount = rs.getInt("NO_OF_RECORDS");
			}
			
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException, 
					"SQLException - Problem retrieving number of participants for search criteria. " +
					"Report criteria:"+criteria);
		} finally {
			closeResultSet(rs);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantsCountForSearchCriteria");
		}

		return recordCount;
	}	
	
	
	/**
	 * Method to get the Summary Details of Accounts Forfeitures Page
	 * 
	 * @param criteria
	 * 			ReportCriteria Object
	 * @param totalParticipants
	 * 			total number of participants
	 * @param connection
	 *			The database connection to use
	 * @param statement
	 *			The PreparedStatement 
	 * @return ParticipantForfeituresTotals
	 * @throws SystemException
	 * @throws SQLException 
	 */
	private static ParticipantForfeituresTotals getParticipantsSummary(
			ReportCriteria criteria, int totalParticipants, int totalPptsWithForfeiture,
			Connection connection, PreparedStatement statement) 
			throws SystemException, SQLException {
		
		if(logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantsSummary");
		}

		//Attributes to hold the resulted data and SQL Query
		ParticipantForfeituresTotals participantForfeituresTotals = null;
		String summarySQL = null;
		
		ResultSet rs = null;

		//Attributes to hold the computational results
		Double employeeAssetsTotal = null;
		Double employerAssetsTotal = null;
		Double totalAssets = null;
		Double forfeituresTotal = null;		
		Double employeeAssetsAvg = null;
		Double employerAssetsAvg = null;
		Double totalAssetsAvg = null;
		Double forfeituresAvg = null;
		
		try {
			
			if (totalParticipants > 0) {
				summarySQL = ParticipantForfeituresQueryHelper.getParticipantsSummarySQL();
				statement = connection.prepareStatement(summarySQL);
				statement.setInt(1, getContractNumber(criteria).intValue());
				rs = statement.executeQuery();
				
				if (rs != null) {
					while (rs.next()) {
						//Employee Money Type
						if (rs.getString("MONEY_TYPE_GROUP").substring(0,2).equals("EE")) 
							employeeAssetsTotal = rs.getDouble("TOTAL_ASSESTS");
						
						//Employer Money Type
						if (rs.getString("MONEY_TYPE_GROUP").substring(0,2).equals("ER")) 
							employerAssetsTotal = rs.getDouble("TOTAL_ASSESTS");
						
						//Forfeitures Money Type
						if (rs.getString("MONEY_TYPE_GROUP").substring(0,2).equals("UM")) 
							forfeituresTotal = rs.getDouble("TOTAL_ASSESTS");	
					}
				}
			
				//Computing the parameters for Summary fields
				employeeAssetsTotal = (employeeAssetsTotal != null) ? employeeAssetsTotal : new Double("0.0");
				employerAssetsTotal = (employerAssetsTotal != null) ? employerAssetsTotal : new Double("0.0");
				forfeituresTotal = (forfeituresTotal != null) ? forfeituresTotal : new Double("0.0");
				
				//Sum the employer total with forfeitures total 
				if (employerAssetsTotal != null && forfeituresTotal != null){
					employerAssetsTotal +=  forfeituresTotal;
				}
				
				totalAssets = employeeAssetsTotal + employerAssetsTotal;
				totalAssetsAvg = (totalAssets > 0) ? totalAssets/totalParticipants : new Double("0.0");
				employeeAssetsAvg = (employeeAssetsTotal != null && employeeAssetsTotal > 0) ? employeeAssetsTotal/totalParticipants : new Double("0.0");				
				employerAssetsAvg = (employerAssetsTotal != null &&employerAssetsTotal > 0) ? employerAssetsTotal/totalParticipants : new Double("0.0");
				
				if (totalPptsWithForfeiture > 0) {
					forfeituresAvg = (forfeituresTotal != null && forfeituresTotal > 0) ? forfeituresTotal/totalPptsWithForfeiture : new Double("0.0");
				} else {
					forfeituresAvg = new Double("0.0");
				}
			
			} else {
				//Assigning the default values
				employeeAssetsTotal = new Double("0.0");
				employerAssetsTotal = new Double("0.0");
				totalAssets = new Double("0.0");
				forfeituresTotal = new Double("0.0");		
				employeeAssetsAvg = new Double("0.0");
				employerAssetsAvg = new Double("0.0");
				totalAssetsAvg = new Double("0.0");
				forfeituresAvg = new Double("0.0");
			}
			
			//Assigning the evaluated parameters into ParticipantForfeituresTotals object
			participantForfeituresTotals = 
				new ParticipantForfeituresTotals(
						totalParticipants,
						employeeAssetsTotal,
						employerAssetsTotal,
						totalAssets,
						forfeituresTotal,
						employeeAssetsAvg,
						employerAssetsAvg,
						totalAssetsAvg,
						forfeituresAvg );
			
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException, 
					"SQLException - Problem retrieving participant forfeitures summary totals. Report criteria:"+criteria);
		} finally {
			closeResultSet(rs);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantsSummary");
		}
		return participantForfeituresTotals;
	}
	
	/**
	 * Method to get the Participant Details
	 * 
	 * @param criteria
	 * 			ReportCriteria Object
	 * @param connection
	 *			The database connection to use
	 * @param statement
	 *			The PreparedStatement
	 * @return List<ParticipantForfeituresDetails> 
	 * 		as List of participant details
	 * @throws SystemException 
	 * @throws SQLException 
	 */	
	private static List<ParticipantForfeituresDetails> getParticipantDetails(
			ReportCriteria criteria,
			Connection connection, 
			PreparedStatement statement) throws SystemException, SQLException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getParticipantDetails");
		}
		
		List<ParticipantForfeituresDetails> participantDetails = 
			new ArrayList<ParticipantForfeituresDetails>();
		ParticipantForfeituresDetails item = null;
		ResultSet rs = null;
		String participantDetailsSQL = null;
		
		try {
			participantDetailsSQL = ParticipantForfeituresQueryHelper.
				getParticipantDetailsSQL(criteria);
			statement = connection.prepareStatement(participantDetailsSQL);
			int contractNumber = getContractNumber(criteria).intValue();
			statement.setInt(1, contractNumber);
			statement.setInt(2, contractNumber);
			statement.setInt(3, contractNumber);
			statement.setInt(4, contractNumber);
			statement.setInt(5, contractNumber);
			
			//Get the asOfDate
			String asOfDateStr = (String)criteria.getFilterValue(
					ParticipantForfeituresReportData.FILTER_ASOFDATE);
	        java.sql.Date asOfDate = null;
	        if(asOfDateStr != null && asOfDateStr.length() !=0 ) { 
	        	asOfDate = new java.sql.Date(Long.parseLong(asOfDateStr));
	        }
	        
			if (asOfDate == null){
				statement.setDate(6, currentAsOfDate);
			} else {
				statement.setDate(6, asOfDate);
			}
			
			statement.setInt(7, contractNumber);
			statement.setInt(8, contractNumber);
			statement.setInt(9, contractNumber);
			
			rs = statement.executeQuery();
			
			if (rs != null) {
				while (rs.next()){
					item = new ParticipantForfeituresDetails();
					
					item.setFirstName(rs.getString("FIRST_NAME"));
					item.setLastName(rs.getString("LAST_NAME"));
					item.setSsn(rs.getString("SOCIAL_SECURITY_NO"));
					item.setDivision(rs.getString("EMPLOYER_DIVISION"));
					item.setStatus(rs.getString("STATUS"));
					item.setEmployeeAssets(rs.getDouble("EMPLOYEE_ASSET"));
					item.setEmployerAssets(rs.getDouble("EMPLOYER_ASSET"));
					item.setTotalAssets(rs.getDouble("TOTAL_BALANCE"));
					item.setForfeitures(rs.getDouble("FORFEITURES"));
					item.setProfileId(rs.getString("PROFILE_ID"));
					item.setTerminationDate(rs.getString("TERMINATION_DATE"));
					participantDetails.add(item);
				}
			}
			
		} catch (SQLException sqlException) {
			throw new SystemException(sqlException, 
					"SQLException - Problem retrieving participant forfeitures details. " +
					"Report criteria:"+criteria);
		} finally {
			closeResultSet(rs);
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getParticipantDetails");
		}

		return participantDetails;
	}
	
	/**
	 * Method to generate Sorting Phrase
	 * 
	 * @param criteria
	 * 			ReportCriteria Object
	 * @return SortCriteria
	 * 			as a String
	 */
	@SuppressWarnings("unchecked")
	private static String createSortPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		
		Iterator sorts = criteria.getSorts().iterator();
		String sortDirection = null;
		String orjSortDirection = null;
		boolean sortByDivisionOrStatus = false;

		for (int i=0; sorts.hasNext(); i++) {
			ReportSort sort = (ReportSort)sorts.next();
			String selectedSortField = sort.getSortField();
			
			if (i==0) {
				orjSortDirection = sort.getSortDirection();
				if (ParticipantForfeituresReportData.FILTER_DIVISION.equals(selectedSortField) ||
						ParticipantForfeituresReportData.FILTER_STATUS.equals(selectedSortField)) {
					sortByDivisionOrStatus = true;
				}
			}
			
			if (sortByDivisionOrStatus) {
				sortDirection = orjSortDirection;
			} else {
				sortDirection = sort.getSortDirection();
			}

			if (ParticipantForfeituresReportData.FILTER_LASTNAME.equals(selectedSortField)) {
				result.append(fieldToColumnMap.get(selectedSortField)).append(' ');
				
				if (i > 0 && !sortByDivisionOrStatus) {
					result.append(SORT_DIRECTION_ASC.equals(orjSortDirection) 
							? SORT_DIRECTION_DESC : SORT_DIRECTION_ASC);
					result.append(", first_name ").append(SORT_DIRECTION_ASC.equals(orjSortDirection) 
							? SORT_DIRECTION_DESC : SORT_DIRECTION_ASC);
				} else {
					result.append(sortDirection);
					result.append(", first_name ").append(sortDirection);					
				}
			} else {
				result.append(fieldToColumnMap.get( selectedSortField )).append(' ');
				result.append(sortDirection);
			}
				
			result.append(',');
		}
		
		if (result.length() > 0) {
			result.deleteCharAt(result.length() - 1);
		}
		
		return result.toString();
	}
	
	/**
	 * Method to generate the Filter Phrase
	 * 
	 * @param criteria
	 * 			ReportCriteria Object
	 * @return filterCriteria 
	 * 			as String
	 */
	private static String createFilterPhrase(ReportCriteria criteria) {
		StringBuffer result = new StringBuffer();
		String lastNameValue = (String)criteria.getFilterValue(ParticipantForfeituresReportData.FILTER_LASTNAME);
		String ssnValue = (String)criteria.getFilterValue(ParticipantForfeituresReportData.FILTER_SSN);
		String divisionValue = (String)criteria.getFilterValue(ParticipantForfeituresReportData.FILTER_DIVISION);
		String status = (String)criteria.getFilterValue(ParticipantForfeituresReportData.FILTER_STATUS);

		if (lastNameValue != null && lastNameValue.trim().length() > 0) {
			result.append("LAST_NAME like ").append(
					wrapInSingleQuotes(lastNameValue + "%").toUpperCase()).append(AND);
		}

		if (ssnValue != null && ssnValue.trim().length() > 0) {
			result.append("SOCIAL_SECURITY_NO = ").append(wrapInSingleQuotes(ssnValue)).append(AND);
		}
		
        if (!StringUtils.isEmpty(divisionValue)) {
            result.append("EMPLOYER_DIVISION like ").append(
            		wrapInSingleQuotes(divisionValue + "%").toUpperCase()).append(AND);
        }
        
        if (!StringUtils.isEmpty(status)) {
            result.append("STATUS like ").append(wrapInSingleQuotes(status)).append(AND);
        }
		
		if (result.length() > 0 && AND.equals(result.substring(result.length() - AND.length()))) {
			result.delete(result.length() - AND.length(), result.length());
		}
		
		return (result.toString().trim().length() > 0 ? result.toString() : null);
	}
	
	/**
	 * Close the resultSet
	 * 
	 * @param rs
	 * @throws SQLException 
	 */
	private static void closeResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
	}
}
