package com.manulife.pension.ps.service.report.tpadownload.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.EStatementsItem;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.EStatementsReportData;
import com.manulife.pension.service.contract.report.dao.SelectContractDAO;
import com.manulife.pension.service.contract.report.valueobject.SelectContract;
import com.manulife.pension.service.contract.report.valueobject.SelectContractReportData;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.utility.ConversionUtility;

/**
 *
 *
 * @author Ilker Celikyilmaz
 *
 * This DAO is used to retieve the data for EStatements
 */
public class EStatementsDAO extends BaseDatabaseDAO {

	private static final String className = EStatementsDAO.class.getName();
    private static final Logger logger = Logger.getLogger(EStatementsDAO.class);
    private static final String COMMA = ",";

    private static final Map fieldToColumnMap = new HashMap();

	private static final String GET_REPORT_LIST_CNLIST  =
        "{call " + EREPORTS_SCHEMA_NAME + "GET_REPORT_LIST_CNLIST (?,?,?,?,?,?,?,?)}";


	static {
		fieldToColumnMap.put(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER, "CONTRACT_NO");
		fieldToColumnMap.put(EStatementsItem.SORT_FIELD_CONTRACT_NAME, "CONTRACT_NAME");
		fieldToColumnMap.put(EStatementsItem.SORT_FIELD_STATEMENT_TYPE, "REPORT_TYPE_CODE");
		fieldToColumnMap.put(EStatementsItem.SORT_FIELD_PERIOD_END_DATE,"REPORT_END_DATE");
		fieldToColumnMap.put(EStatementsItem.SORT_FIELD_PRODUCED_DATE,"PROCESSING_DATE");
		fieldToColumnMap.put(EStatementsItem.SORT_FIELD_CORRECTED,"UPDATED_CODE");
		fieldToColumnMap.put(EStatementsItem.SORT_FIELD_YEAR_END,"YEAREND_IND");
        fieldToColumnMap.put(EStatementsItem.SORT_FIELD_CONTRACT_TYPE,"CONTRACT_TYPE");        
	}

	//Make sure nobody instantiates this class
	private EStatementsDAO()
	{
	}

   	public static ReportData getReportData(ReportCriteria criteria) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getReportData");
			logger.debug("Report criteria -> " + criteria.toString());
		}


    	EStatementsReportData data = new EStatementsReportData(criteria, 0);
    	ArrayList statements = new ArrayList();
		data.setDetails(statements);

		int numStatements = 0;
		Connection conn = null;
       	CallableStatement stmt = null;
       	ResultSet resultSet = null;

    	try {
    		String contractNumber = (String)criteria.getFilterValue(EStatementsReportData.FILTER_CONTRACT_NUMBER);
    		String contractName = (String)criteria.getFilterValue(EStatementsReportData.FILTER_CONTRACT_NAME);

    		if (contractNumber != null && contractNumber.trim().length() > 0 )
    			contractNumber = contractNumber.trim();
    		else if ( contractName != null &&  contractName.trim().length() > 0 )
    			contractName = contractName.trim();

   			// get a list of contracts available to TPA
   			SelectContract[] contracts = getContracts((String)criteria.getFilterValue(EStatementsReportData.FILTER_PROFILE_ID),
   									contractNumber, contractName,
									(String)criteria.getFilterValue(EStatementsReportData.FILTER_SITE_LOCATION) );

   			StringBuffer contractList = new StringBuffer(0);

    		if (contracts != null && contracts.length > 0) {

    			for (int i=0; i < contracts.length; i++) {
    				contractList.append( contracts[i].getContractNumber() );
    				contractList.append(COMMA);
    			}

    			// Remove the last COMMA
				contractList.deleteCharAt(contractList.length()-1);

				// setup the connection and the statement
	            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
	            stmt = conn.prepareCall(GET_REPORT_LIST_CNLIST);

	            if (logger.isInfoEnabled()) {
					logger.debug("Calling Stored Procedure: "+ GET_REPORT_LIST_CNLIST);
				}

				stmt.setString(1, contractList.toString() );

				Object objStatementType = criteria.getFilterValue(EStatementsReportData.FILTER_STATEMENT_TYPE_CODE);
				if ( objStatementType == null || ((String)objStatementType).trim().length() == 0 )
					stmt.setNull(2, Types.VARCHAR);
				else
					stmt.setString(2, ((String)objStatementType).trim());

				Object objFrom = criteria.getFilterValue(EStatementsReportData.FILTER_REPORT_END_DATE_FROM);
				if ( objFrom == null )
					stmt.setNull(3, Types.DATE);
				else
					stmt.setDate(3, new java.sql.Date(((Date)objFrom).getTime()));

				Object objTo = criteria.getFilterValue(EStatementsReportData.FILTER_REPORT_END_DATE_TO);
				if ( objTo == null )
					stmt.setNull(4, Types.DATE);
				else
					stmt.setDate(4, new java.sql.Date(((Date)objTo).getTime()));

				Object objCorrectedCode = criteria.getFilterValue(EStatementsReportData.FILTER_CORRECTED_CODE);
				if ( objCorrectedCode == null || ((String)objCorrectedCode).trim().length() == 0 )
					stmt.setNull(5, Types.VARCHAR);
				else
					stmt.setString(5,((String)objCorrectedCode).trim());

				Object objYearEnd = criteria.getFilterValue(EStatementsReportData.FILTER_YEAR_END);
				if ( objYearEnd == null || ((String)objYearEnd).trim().length() == 0 )
					stmt.setNull(6, Types.VARCHAR);
				else
					stmt.setString(6,((String)objYearEnd).trim());

				String sortPhrase = createSortPhrase(criteria);

			    if (logger.isDebugEnabled())
					logger.debug("Sort phrase: "+ sortPhrase);
				stmt.setString(7, sortPhrase);			// sort order

				stmt.setInt(8, criteria.getPageSize());

	            // execute the stored procedure
	            stmt.execute();
				resultSet = stmt.getResultSet();
				
				if (resultSet != null) {
				    
    				while ( resultSet.next() ) {
    					EStatementsItem item =
    						new EStatementsItem(
    								resultSet.getString("CONTRACT_NO"),
    								resultSet.getString("CONTRACT_NAME"),
    								resultSet.getString("REPORT_TYPE_CODE"),
    								resultSet.getDate("REPORT_END_DATE"),
    								resultSet.getDate("PROCESSING_DATE"),
    								resultSet.getString("UPDATED_CODE"),
    								resultSet.getString("YEAREND_IND"),
    								resultSet.getString("CLIENT_ID"),
                                    resultSet.getString("CONTRACT_TYPE")
    								);
    					    statements.add(item);
    					    numStatements++;
    				}
    				
				}
				
				data.setTotalCount(numStatements);

			}
    	} catch (SQLException e) {
    		throw new SystemException(e, className, "getReportData", "Problem occurred during GET_REPORT_LIST_CNLIST stored proc call.");
	    } finally {
	       	close(stmt, conn);
	    }

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getReportData");
		}

        return data;
	}

	private static SelectContract[] getContracts(String profileId, String contractNumber,
					String contractName, String siteLocation) throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("entry -> getContracts");
		}

		SelectContract[] contractDetails = new SelectContract[0];
		ReportCriteria criteria = new ReportCriteria(SelectContractReportData.REPORT_ID);
		//set report criteria
		criteria.insertSort("contractName", ReportSort.ASC_DIRECTION);
		criteria.setPageSize(50000);
		criteria.addFilter(SelectContractReportData.FILTER_CLIENT_ID, profileId);
		criteria.addFilter(SelectContractReportData.FILTER_SITE_LOCATION, siteLocation);
		criteria.addFilter(SelectContractReportData.FILTER_USER_ROLE, ConversionUtility.convertToStoredProcRole(new ThirdPartyAdministrator()));

		if ( contractNumber != null && contractNumber.trim().length() > 0 )
			criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, "C.CONTRACT_ID="+ contractNumber);
		else if (contractName != null && contractName.trim().length() > 0 )
			criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, "upper(C.CONTRACT_NAME) LIKE '"+ sqlizeTheString(contractName.toUpperCase()) + "%'");
		else
			criteria.addFilter(SelectContractReportData.FILTER_SEARCH_STRING, null);

		try {
			ReportData vo = SelectContractDAO.getReportData(criteria);
			if (vo != null && vo.getDetails() != null) {
				contractDetails = (SelectContract[])vo.getDetails().toArray(new SelectContract[0]);
			}
		} catch (Exception e) {
			SystemException se = new SystemException(e,EStatementsDAO.className, "getContracts", "getContracts throws Exception.");
			logger.error(se);
			throw se;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getContracts");
		}

		return contractDetails;
	}


	private static String createSortPhrase(ReportCriteria criteria) {
		if (logger.isDebugEnabled()) {
			logger.debug("entry -> createSortPhrase");
		}

		StringBuffer result = new StringBuffer();
		Iterator sorts = criteria.getSorts().iterator();
		String sortDirection = null;
		String reverseSortDirection = null;
		String orjSortDirection = null;

		if ( sorts.hasNext() ) {
			ReportSort sort = (ReportSort)sorts.next();

			sortDirection = sort.getSortDirection();
			reverseSortDirection = sort.getReverseSortDirection();

			result.append(fieldToColumnMap.get(sort.getSortField())).append(' ');
			if ( EStatementsItem.SORT_FIELD_CORRECTED.equals(sort.getSortField()) )
				result.append(reverseSortDirection);
			else
				result.append(sortDirection);

			if ( EStatementsItem.SORT_FIELD_CONTRACT_NUMBER.equals(sort.getSortField()) ) {
                result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_TYPE)).append(' ').append(ReportSort.DESC_DIRECTION);                
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);

            } else if ( EStatementsItem.SORT_FIELD_CONTRACT_TYPE.equals(sort.getSortField()) ) {
                result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
                result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
                result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
                result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);
           
            
			} else if ( EStatementsItem.SORT_FIELD_CONTRACT_NAME.equals(sort.getSortField()) ) {
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);
			} else if ( EStatementsItem.SORT_FIELD_STATEMENT_TYPE.equals(sort.getSortField()) ) {
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NAME)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
			} else if ( EStatementsItem.SORT_FIELD_PERIOD_END_DATE.equals(sort.getSortField()) ) {
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NAME)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
			} else if ( EStatementsItem.SORT_FIELD_PRODUCED_DATE.equals(sort.getSortField()) ) {
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NAME)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
			} else if ( EStatementsItem.SORT_FIELD_PRODUCED_DATE.equals(sort.getSortField()) ) {
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NAME)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
			} else if ( EStatementsItem.SORT_FIELD_CORRECTED.equals(sort.getSortField()) ) {
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_YEAR_END)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NAME)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
			} else if ( EStatementsItem.SORT_FIELD_YEAR_END.equals(sort.getSortField()) ) {
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_PERIOD_END_DATE)).append(' ').append(ReportSort.DESC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_STATEMENT_TYPE)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NAME)).append(' ').append(ReportSort.ASC_DIRECTION);
				result.append(COMMA).append(fieldToColumnMap.get(EStatementsItem.SORT_FIELD_CONTRACT_NUMBER)).append(' ').append(ReportSort.ASC_DIRECTION);
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("exit <- createSortPhrase");
		}

		return result.toString();
	}


    /**
     * This method is to used to add another ' after ' in order
     * to make the SQL statement valid
     *
     * @param str
     * @return String
     */
    private static String sqlizeTheString(String str) {
    	StringBuffer temp = new StringBuffer();
    	int initialIndex = 0;
    	int index = 0;
    	while ((index = str.indexOf("'", initialIndex)) != -1) {
    		temp.append(str.substring(initialIndex,index+1)).append("'");
    		initialIndex = index+1;
    	}

    	if (temp.length() == 0) temp.append(str);

    	return temp.toString();
    }


}

