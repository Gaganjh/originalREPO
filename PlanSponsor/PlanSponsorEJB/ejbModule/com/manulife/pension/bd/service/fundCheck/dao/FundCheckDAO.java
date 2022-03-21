package com.manulife.pension.bd.service.fundCheck.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.service.brokerListing.dao.BrokerListingDAO;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.util.JdbcHelper;

/**
 * This class is used to get the producer codes for the search input given by
 * the user in Fundcheck page.
 * 
 * @author 
 * 
 */
public class FundCheckDAO extends BaseDatabaseDAO {
	
	private static final Logger logger = Logger.getLogger(FundCheckDAO.class);
	
    private static String className = FundCheckDAO.class.getName();
	
    private static String PERCENT_SYM = "%";

    private static final String COMMA_SYMBOL = ",";

    private static String OR = " OR ";
    
    private static String AND = " AND ";

    private static String LIKE = " LIKE ";
    
    private static String OPEN_BRACKET = "(";

    private static String CLOSE_BRACKET = ")";	
    
    private static String FINANCIAL_REP_LAST_NAME_UCASE = "UCASE(BROKER_LAST_NAME)";

    private static String FINANCIAL_REP_FIRST_NAME_UCASE = "UCASE(BROKER_FIRST_NAME)";

    private static String ORGANIZATION_NAME_UCASE = "UCASE(BROKER_ORGANIZATION_NAME)";  
    
	private static String SEARCH_USER_PROFILE_BDW  = "SELECT USER_ASSISTANT_TO_PROFILE_ID FROM BDW100.USER_PROFILE_BDW "
		+ "WHERE USER_PROFILE_ID= ?";
	
	private static String SEARCH_BY_CONTRACT_NAME_FIRST_PART = "SELECT  CONTRACT_ID,CONTRACT_NAME FROM BDW100.USER_BROKER_CONTRACT_HIST "
		+ "WHERE USER_PROFILE_ID= ? AND ((CONTRACT_STATUS_CODE='DI'"
		+ "AND  CONTRACT_STATUS_EFF_DATE >= (CURRENT DATE - 6 MONTHS)) OR "
		+ "(CONTRACT_STATUS_CODE<>'DI')) AND UPPER(CONTRACT_NAME) LIKE ? ORDER BY CONTRACT_NAME,CONTRACT_ID FOR FETCH ONLY ";	
	
	private static String SEARCH_BY_CONTRACT_ID = "SELECT  CONTRACT_ID FROM BDW100.USER_BROKER_CONTRACT_HIST "
		+ "WHERE USER_PROFILE_ID= ? AND ((CONTRACT_STATUS_CODE='DI'"
		+ "AND  CONTRACT_STATUS_EFF_DATE >= (CURRENT DATE - 6 MONTHS)) OR "
		+ "(CONTRACT_STATUS_CODE<>'DI')) AND CONTRACT_ID= ? "
		+ " ORDER BY PRODUCER_CODE FOR FETCH ONLY ";

    private static String SEARCH_BY_PRODUCER_CODE_FOR_INTERNAL_USER = "SELECT PRODUCER_CODE FROM MQT100.PARTY_BROKER_CONTRACT_HIST "
    	+"WHERE PRODUCER_CODE = ?";
    
     private static String SEARCH_BY_CONTRACT_ID_FOR_INTERNAL_USER = " SELECT CONTRACT_ID FROM MQT100.PARTY_BROKER_CONTRACT_HIST WHERE  CONTRACT_ID= ? ";/* TODOunion "
       	+"SELECT CONTRACT_ID FROM " 
       	+ PLAN_SPONSOR_SCHEMA_NAME +"UNALLOCATED_CONTRACT WHERE  CONTRACT_ID = ?";*/ 
	
    private static String GET_CONTRACT_NAMES_FOR_INTERNAL_USER = "SELECT CONTRACT_ID,CONTRACT_NAME FROM MQT100.PARTY_BROKER_CONTRACT_HIST "
    	+"WHERE UPPER(CONTRACT_NAME) LIKE ?  ORDER BY CONTRACT_NAME,CONTRACT_ID";
    
    private static String GET_FR_NAMES_FOR_INTERNAL_USER_PART_ONE = "SELECT BROKER_LAST_NAME,BROKER_FIRST_NAME,PRODUCER_CODE,BROKER_ORGANIZATION_NAME,TRIM(CONCAT(CONCAT(TRIM(BROKER_LAST_NAME),CONCAT(' ',TRIM(BROKER_FIRST_NAME))),TRIM(BROKER_ORGANIZATION_NAME))) FRNAME FROM MQT100.PARTY_BROKER_CONTRACT_HIST  "
    	+"WHERE ";       
    
    private static String GET_FR_NAMES_FOR_INTERNAL_USER_PART_TWO = " ORDER BY FRNAME,PRODUCER_CODE ";    	

    /**
     * This method searches USER_BROKER_CONTRACT_HIST view and fetches contract name and contract id  
     * that matches the given search input.
     * @param search Type
     * @param search Input
     * @return
     * @throws SystemException
     */
    public static String getAssistantProfileIdForLevel2User(long profileId)
                                                          throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getAssistantProfileIdForLevel2User");
    	String asstUserProfileId = null;
    	PreparedStatement stmt = null;
    	Connection conn = null;  
    	ResultSet result = null;
    	try{    	
		    	conn = getDefaultConnection(className,CUSTOMER_DATA_SOURCE_NAME);
		    	StringBuffer sb=new StringBuffer();
				sb.append(SEARCH_USER_PROFILE_BDW);
				stmt = conn.prepareStatement(sb.toString());
				stmt.setInt(1, (int) profileId);
				result = stmt.executeQuery();
				if(result.next()) {
					asstUserProfileId = (result.getString("USER_ASSISTANT_TO_PROFILE_ID"));
				}
	    	
    	}catch (SQLException e) {
    		logger.error("Problem in accessing or executing the query in getAssistantProfileIdForLevel2User method."+e);
            throw new SystemException(e,"Problem occurred in getAssistantProfileIdForLevel2User");
        } finally {
            closeResources(result, stmt, conn);
        }
		if (logger.isDebugEnabled())
			logger.debug("entry -> getAssistantProfileIdForLevel2User");
       	return asstUserProfileId;
    }     
    
    /**
     * This method searches USER_BROKER_CONTRACT_HIST view and fetches contract name and contract id  
     * that matches the given search input.
     * @param search Type
     * @param search Input
     * @return
     * @throws SystemException
     */
    public static Map<String,String> getContractNamesForLevel2User(String searchInput, long profileId)
                                                          throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getContractNamesForLevel2User");
    	Map<String,String> searchResultMap=new LinkedHashMap<String,String>();
    	PreparedStatement stmt = null;
    	Connection conn = null;  
    	ResultSet result = null;
    	try{    	
    		if(searchInput != null){
	    		searchInput = searchInput.trim().toUpperCase();
		    	conn = getDefaultConnection(className,CUSTOMER_DATA_SOURCE_NAME);
		    	StringBuffer sb=new StringBuffer();
				sb.append(SEARCH_BY_CONTRACT_NAME_FIRST_PART);
				stmt = conn.prepareStatement(sb.toString());
				stmt.setInt(1, (int) profileId);
				stmt.setString(2,PERCENT_SYM+searchInput+PERCENT_SYM); 
				result = stmt.executeQuery();
				while (result.next()) {
					searchResultMap.put(result.getString("CONTRACT_ID"),result.getString("CONTRACT_NAME"));
				}
    		}
			    	
    	}catch (SQLException e) {
    		logger.error("Problem in accessing or executing the query in getContractNamesForLevel2User method."+e);
            throw new SystemException(e,"Problem occurred in getContractNamesForLevel2User");
        } finally {
            closeResources(result, stmt, conn);
        }
		if (logger.isDebugEnabled())
			logger.debug("entry -> getContractNamesForLevel2User");
       	return searchResultMap;
    } 	
	
	
	
	/**
	 * This method searches USER_BROKER_CONTRACT_HIST view and fetches producer
	 * codes corresponding to the search input.
	 * 
	 * @param contractName
	 * @param contractId
	 * @param profileId
	 * @return
	 * @throws SystemException
	 */
	public static boolean checkUserExistForLevel2User(String searchType,
			String searchInput, long profileId) throws SystemException {		
		if (logger.isDebugEnabled())
			logger.debug("entry -> checkUserExistForLevel2User");
		boolean searchResult = false;
		PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet result = null;
		try {
			conn = getDefaultConnection(className,CUSTOMER_DATA_SOURCE_NAME);

			if ("contractNumber".equalsIgnoreCase(searchType)) {				
				int contractNo=0;
				contractNo = Integer.parseInt(searchInput.trim());
				stmt = conn.prepareStatement(SEARCH_BY_CONTRACT_ID);
				stmt.setInt(1, (int) profileId);
				stmt.setInt(2, contractNo);
				result = stmt.executeQuery();
				while (result.next()) {
					searchResult = true;
				}
			}
		}catch (NumberFormatException nfe){
			// Do nothing, it should never happen, assume 0
			logger.error("Received NumberFormatException. Can't format contract number to integer. ", nfe);
		}catch (SQLException e) {
			logger.error("Problem in accessing or executing the query in checkUserExistForLevel2User method."+e);
			throw new SystemException(e,"Problem occurred in checkUserExistForLevel2User");
		} finally {
			closeResources(result, stmt, conn);
		}
		if (logger.isDebugEnabled())
			logger.debug("exit -> checkUserExistForLevel2User");
		return searchResult;
	}

    /**
     * This method searches PARTY_BROKER_CONTRACT_HIST view and fetches producer codes corresponding to the 
     * search input.
     * @param search Type
     * @param search Input
     * @return
     * @throws SystemException
     */
    public static boolean checkUserExistForInternalUser(String searchType,String searchInput)
                                                          throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> checkUserExistForInternalUser");
    	boolean searchResult=false;
    	PreparedStatement stmt = null;
    	Connection conn = null; 
    	ResultSet result = null;
		int id = Integer.parseInt(searchInput.trim());
		
    	try{    	
	    	conn = getDefaultConnection(className,CUSTOMER_DATA_SOURCE_NAME);
	    	StringBuffer sb=new StringBuffer();
	    	if(searchType.equalsIgnoreCase("producerCode") || searchType.equalsIgnoreCase("contractNumber")){
		    	if(searchType.equalsIgnoreCase("producerCode")){
			    	sb.append(SEARCH_BY_PRODUCER_CODE_FOR_INTERNAL_USER);	
			    	stmt=conn.prepareStatement(sb.toString());
			        stmt.setInt(1,id);

		    	   
		    	}else if(searchType.equalsIgnoreCase("contractNumber")){
			    	sb.append(SEARCH_BY_CONTRACT_ID_FOR_INTERNAL_USER);
			    	stmt=conn.prepareStatement(sb.toString());
			        stmt.setInt(1,id);
			// union part from the query has been removed along with the second input parameter
			 //       stmt.setInt(2,id);
		    	}
		    	result=stmt.executeQuery();
		    	while(result.next()){
		    		searchResult = true;
		    		break;
		    	} 
		    }
    	}catch (SQLException e) {
    		logger.error("Problem in accessing or executing the query in checkUserExistForInternalUser method."+e);
            throw new SystemException(e,"Problem occurred in checkUserExistForInternalUser");
        } finally {
        	closeResources(result, stmt, conn);
        }
		if (logger.isDebugEnabled())
			logger.debug("exit -> checkUserExistForInternalUser");
       	return searchResult;
    }    
    
    /**
     * This method searches PARTY_BROKER_CONTRACT_HIST view and fetches contract name and contract id  
     * that matches the given search input.
     * @param search Type
     * @param search Input
     * @return
     * @throws SystemException
     */
    public static Map<String,String> getContractNamesForInternalUser(String searchInput)
                                                          throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getContractNamesForInternalUser");
    	Map<String,String> searchResultMap=new LinkedHashMap<String,String>();
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet result = null;
    	try{    
    		if(searchInput != null){
	    		searchInput = searchInput.trim().toUpperCase();
		    	conn = getDefaultConnection(className,CUSTOMER_DATA_SOURCE_NAME);
		    	StringBuffer sb=new StringBuffer();
		    	sb.append(GET_CONTRACT_NAMES_FOR_INTERNAL_USER);
		    	stmt=conn.prepareStatement(sb.toString());
		    	stmt.setString(1,PERCENT_SYM+searchInput+PERCENT_SYM); 
		    	result=stmt.executeQuery();
		    	while(result.next()){
		    		searchResultMap.put(result.getString("CONTRACT_ID"),result.getString("CONTRACT_NAME"));
		    	}
    		}
    	}catch (SQLException e) {
    		logger.error("Problem in accessing or executing the query in getContractNamesForInternalUser method."+e);
            throw new SystemException(e,"Problem occurred in getContractNamesForInternalUser");
        } finally {
        	closeResources(result, stmt, conn);
        }
		if (logger.isDebugEnabled())
			logger.debug("exit -> getContractNamesForInternalUser");
       	return searchResultMap;
    }     
    
    /**
     * This method searches PARTY_BROKER_CONTRACT_HIST view and fetches FR Name and Producer code  
     * that matches the given search input.
     * @param search Type
     * @param search Input
     * @return
     * @throws SystemException
     */
    public static Map<String,String> getFRNamesForInternalUser(String searchInput)
    throws SystemException{
		if (logger.isDebugEnabled())
			logger.debug("entry -> getFRNamesForInternalUser");
		Map<String,String> searchResultMap=new LinkedHashMap<String,String>();
		PreparedStatement stmt = null;
		Connection conn = null; 
		ResultSet result = null;
		try{    	
			conn = getDefaultConnection(className,CUSTOMER_DATA_SOURCE_NAME);
			StringBuffer sb=new StringBuffer();
		    String financialRepOrOrgName = searchInput;
		    			
//TODO SSA this is really-really bad. There should be no filter clause dynamically build for this query. It may open up the application for security issues plus that the db optimizer may not be able to cache the query, thus hurting performance. 		    
	    	sb.append(GET_FR_NAMES_FOR_INTERNAL_USER_PART_ONE).append(
	    			buildFilterClauseForFinancialRepName(financialRepOrOrgName)).append(
	    					GET_FR_NAMES_FOR_INTERNAL_USER_PART_TWO);
			
			stmt=conn.prepareStatement(sb.toString());    	
			result=stmt.executeQuery();
			while(result.next()){
				String firstName = result.getString("BROKER_FIRST_NAME");
				String lastName = result.getString("BROKER_LAST_NAME");
				String orgName = result.getString("BROKER_ORGANIZATION_NAME");
				if(firstName!=null){
					firstName = firstName.trim();
				}
				if(lastName!=null){
					lastName = lastName.trim();
				}
				String FRName = ((firstName!=null && firstName.length() > 0)
						||(lastName!=null && lastName.length() > 0)) ? (lastName+" "+firstName) : orgName;
				searchResultMap.put(result.getString("PRODUCER_CODE"),FRName);
			}    	
		}catch (SQLException e) {
			logger.error("Problem in accessing or executing the query in getFRNamesForInternalUser method."+e);
			throw new SystemException(e,"Problem occurred in getFRNamesForInternalUser");
		} finally {
			closeResources(result, stmt, conn);
		}
		if (logger.isDebugEnabled())
			logger.debug("exit -> getFRNamesForInternalUser");
		return searchResultMap;
	}     
    
    /**
     * This method will build the filter clause for Financial Rep Name. The filter clause for
     * Financial Rep Name has to built in such a way that we identify the last_name, first_name or
     * organization_name in it.
     * 
     * The Financial Rep filter criteria can contain zero or more commas.. For example: - abc or -
     * abc, def or - abc, def, ghi, xyz etc..
     * 
     * The Financial Rep filter criteria could be an - Individual Financial Rep's Name
     * (<lastName><comma><firstName> format) or - Organization name.
     * 
     * In case if the filter criteria contains more than 1 comma (for ex: abc, def, ghi), it is not
     * possible to know which is the last_name, which is the first_name, or is it a Organization
     * Name. So, in this case, we will divide the filter criteria at every comma and send it as a
     * last_name, first_name. The whole filter criteria will also be sent as organization_name.
     * 
     * So, in the above case, we will send filter clause as: ((BROKER_LAST_NAME LIKE 'abc%' AND BROKER_FIRST_NAME
     * LIKE 'def, ghi%') OR (BROKER_LAST_NAME LIKE 'abc, def%' AND BROKER_FIRST_NAME LIKE 'ghi%') OR (BROKER_LAST_NAME
     * LIKE 'abc, def, ghi%') OR (BROKER_ORGANIZATION_NAME LIKE 'abc, def, ghi%'))
     */
//TODO SSA this method really needs unit tests to prove it really works as intended
//TODO SSA this method is utterly complex and error prone. Instead, just decide on a limit of parameters (4, for example) and craft a static filter query that will work for all cases,     
    private static String buildFilterClauseForFinancialRepName(String financialRepOrOrgName) {
		if (logger.isDebugEnabled())
			logger.debug("entry -> buildFilterClauseForFinancialRepName");
        StringBuffer result = new StringBuffer(300);

        if (!StringUtils.isBlank(financialRepOrOrgName)) {

            result.append(OPEN_BRACKET);

            // Find if the string contains a comma
            if (StringUtils.contains(financialRepOrOrgName, COMMA_SYMBOL)) {
                int numOfCommas = StringUtils.countMatches(financialRepOrOrgName, COMMA_SYMBOL);

                int commaIndex = 0;
                boolean firstIndex = true;
                for (; numOfCommas > 0; numOfCommas--) {
                    // Find the location of comma.
                    commaIndex = StringUtils.indexOf(financialRepOrOrgName, COMMA_SYMBOL,
                            commaIndex);
//TODO SSA instead of all this org, consider using String.split() then process the results.
                    String stringBeforeComma = StringUtils.left(financialRepOrOrgName, commaIndex);
                    String stringAfterComma = StringUtils.right(financialRepOrOrgName,
                            financialRepOrOrgName.length() - commaIndex - 1);

                    if (firstIndex) {
                        firstIndex = false;
                    } else {
                        result.append(OR);
                    }

                    if (!StringUtils.isBlank(stringBeforeComma)
                            || !StringUtils.isBlank(stringAfterComma)) {
                        result.append(OPEN_BRACKET);
                    }

                    if (!StringUtils.isBlank(stringBeforeComma)) {
                        result.append(FINANCIAL_REP_LAST_NAME_UCASE).append(LIKE).append(
                                wrapInSingleQuotes(
                                        PERCENT_SYM + stringBeforeComma.trim() + PERCENT_SYM)
                                        .toUpperCase());
                    }

                    if (!StringUtils.isBlank(stringAfterComma)) {
                        if (!StringUtils.isBlank(stringBeforeComma)) {
                            result.append(AND);
                        }
                        result.append(FINANCIAL_REP_FIRST_NAME_UCASE).append(LIKE).append(
                                wrapInSingleQuotes(
                                        PERCENT_SYM + stringAfterComma.trim() + PERCENT_SYM)
                                        .toUpperCase());
                    }

                    if (!StringUtils.isBlank(stringBeforeComma)
                            || !StringUtils.isBlank(stringAfterComma)) {
                        result.append(CLOSE_BRACKET);
                    }

                    commaIndex++;
                }
                result.append(OR);
            }
            // Adding the filter criteria as last name because, it might be possible that the
            // user has entered only the last name which has commas as well.
            result.append(OPEN_BRACKET).append(FINANCIAL_REP_LAST_NAME_UCASE).append(LIKE).append(
                    wrapInSingleQuotes(PERCENT_SYM + financialRepOrOrgName.trim() + PERCENT_SYM)
                            .toUpperCase()).append(CLOSE_BRACKET);

            result.append(OR).append(OPEN_BRACKET).append(ORGANIZATION_NAME_UCASE).append(LIKE)
                    .append(
                    wrapInSingleQuotes(
                            PERCENT_SYM + StringUtils.trim(financialRepOrOrgName) + PERCENT_SYM)
                            .toUpperCase()).append(CLOSE_BRACKET);

            result.append(CLOSE_BRACKET);
        }
		if (logger.isDebugEnabled())
			logger.debug("exit -> buildFilterClauseForFinancialRepName");
        return result.toString();
    } 

	/**
	 * This method ensures that the resources are closed.
	 * 
	 * @param result
	 * @param stmt
	 * @param con
	 */
	private static void closeResources(ResultSet result,
			PreparedStatement stmt, Connection connection) {
		try {
			try {
				if (result != null)
					result.close();
			} finally {
				try {
					if (stmt != null)
						stmt.close();
				} finally {
					if (connection != null && !connection.isClosed()) {
						connection.close();
						connection = null;
					}
				}
			}
		} catch (SQLException sqle) {
			// ignore errors on closing
		}
	}
}
