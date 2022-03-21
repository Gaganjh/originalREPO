package com.manulife.pension.ps.service.report.investment.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetailsReportDetailVO;
import com.manulife.pension.ps.service.report.investment.valueobject.AllocationDetailsReportSummaryVO;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationDetailsReportData;
import com.manulife.pension.ps.service.report.investment.valueobject.InvestmentAllocationReportData;
import com.manulife.pension.service.contract.dao.ContractDAO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.fund.cache.FundInfoCache;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;

/**
 * @author Nicolae Vintila
 * @date Feb 8, 2004
 * @time 3:47:47 PM
 */
public class InvestmentAllocationDetailsDAO extends BaseDatabaseDAO {

    private static final Logger logger = Logger.getLogger(InvestmentAllocationDetailsDAO.class);
    private static final String className = InvestmentAllocationDetailsDAO.class.getName();
    
    private static final Map attributeToColumnMap = new HashMap();

    public static interface dbf {
        final static String PARTICIPANT_ID = "PARTICIPANT_ID";
        final static String LAST_NAME = "LAST_NAME";
        final static String FIRST_NAME = "FIRST_NAME";
        final static String SOCIAL_SECURITY_NO = "SOCIAL_SECURITY_NO";
        final static String ONGOING_CONTRIB_IND = "ONGOING_CONTRIB_IND";
        final static String EMPLOYEE_BALANCE_AMT = "EMPLOYEE_BALANCE_AMT";
        final static String EMPLOYER_BALANCE_AMT = "EMPLOYER_BALANCE_AMT";
        final static String TOTAL_BALANCE_AMT = "TOTAL_BALANCE_AMT";
        final static String NUMBER_OF_PARTICIPANTS = "NUMBER_OF_PARTICIPANTS";
    }

    static {
        attributeToColumnMap.put(AllocationDetailsReportDetailVO.keys.name, new String[]{dbf.LAST_NAME, dbf.FIRST_NAME, dbf.SOCIAL_SECURITY_NO});
        attributeToColumnMap.put(AllocationDetailsReportDetailVO.keys.ssn, dbf.SOCIAL_SECURITY_NO);
        attributeToColumnMap.put(AllocationDetailsReportDetailVO.keys.ongoingContributions, dbf.ONGOING_CONTRIB_IND);
        attributeToColumnMap.put(AllocationDetailsReportDetailVO.keys.employeeAssetsAmount, dbf.EMPLOYEE_BALANCE_AMT);
        attributeToColumnMap.put(AllocationDetailsReportDetailVO.keys.employerAssetsAmount, dbf.EMPLOYER_BALANCE_AMT);
        attributeToColumnMap.put(AllocationDetailsReportDetailVO.keys.totalAssetsAmount, dbf.TOTAL_BALANCE_AMT);
    }

    private static final String GET_PARTICIPANTS_INVESTMENTS_ALLOCATION_DETAILS =
        "{call " + CUSTOMER_SCHEMA_NAME + "GET_PARTICIPANT_INVESTMENT_DTL(?,?,?,?,?,?,?)}";

    public static ReportData getReportData(ReportCriteria criteria) throws SystemException {

        if (logger.isDebugEnabled())
            logger.debug("entry -> getReportData");

        Integer contractNumber = (Integer) criteria.getFilterValue(
            InvestmentAllocationReportData.FILTER_CONTRACT_NO);

        String asOfDateStr = (String) criteria.getFilterValue(InvestmentAllocationReportData.FILTER_ASOFDATE_DETAILS);
        java.sql.Date asOfDate = new java.sql.Date(Long.valueOf(asOfDateStr).longValue());

        String fundId = (String) criteria.getFilterValue(InvestmentAllocationReportData.FILTER_FUND_ID);

        String sortCriteria = createSortPhrase(criteria);

        InvestmentAllocationDetailsReportData reportData =
            new InvestmentAllocationDetailsReportData(criteria, -1);

        Connection conn = null;
        CallableStatement statement = null;

        try {
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            statement = conn.prepareCall(GET_PARTICIPANTS_INVESTMENTS_ALLOCATION_DETAILS);

            doLoadReportData(statement, contractNumber.intValue(), fundId, asOfDate, sortCriteria,
                criteria.getStartIndex(), criteria.getPageSize(), reportData);
        } catch (SQLException e) {
            throw new SystemException(e, className, "getReportData",
                "Problem occurred during GET_PARTICIPANTS_INVESTMENTS_ALLOCATION_DETAILS stored proc call.");
        } finally {
        	close(statement, conn);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit -> getReportData");
        return reportData;
    }

    private static void doLoadReportData(CallableStatement stmt, int contractNumber,
        String fundId, java.sql.Date asOfDate, String sortCriteria, int startIndex,
        int pageSize, InvestmentAllocationDetailsReportData reportData)
        throws SQLException, SystemException {
        if (logger.isDebugEnabled())
            logger.debug("entry -> doLoadReportData");

        boolean isForDownload = pageSize == ReportCriteria.NOLIMIT_PAGE_SIZE;
        if (isForDownload) {
            // An arbitrary limit for total number of rows to download.
            pageSize = 100000; //todo
        }
        Map fundsRateTypesMap = new HashMap();
        fundsRateTypesMap = ContractDAO.getContractRateTypes(contractNumber);

        stmt.setBigDecimal(1, new BigDecimal(String.valueOf(contractNumber)));
        stmt.setDate(2, asOfDate);
        stmt.setString(3, fundId);
        stmt.setString(4, sortCriteria);
        stmt.setInt(5, startIndex);
        stmt.setInt(6, pageSize);
        stmt.registerOutParameter(7, java.sql.Types.DECIMAL);

        if (logger.isDebugEnabled())
            logger.debug("Calling Stored Procedure: " + GET_PARTICIPANTS_INVESTMENTS_ALLOCATION_DETAILS);

        List items = null;

        stmt.execute();

        ResultSet rsSummary = stmt.getResultSet();
        
        if (rsSummary == null) {
            throw new SystemException("Problem occurred during GET_PARTICIPANTS_INVESTMENTS_ALLOCATION_DETAILS stored proc call.");
        }
        
        {
            rsSummary.next();

            AllocationDetailsReportSummaryVO summary =
                new AllocationDetailsReportSummaryVO(fundId,
                    FundInfoCache.getFundData(fundId).getName(),
                    FundInfoCache.getFundData(fundId).getType(),
                    (String) fundsRateTypesMap.get(fundId),
                    rsSummary.getBigDecimal(dbf.NUMBER_OF_PARTICIPANTS),
                    rsSummary.getBigDecimal(dbf.EMPLOYEE_BALANCE_AMT),
                    rsSummary.getBigDecimal(dbf.EMPLOYER_BALANCE_AMT),
                    rsSummary.getBigDecimal(dbf.TOTAL_BALANCE_AMT));
            reportData.setSummary(summary);
            reportData.setTotalCount(summary.getParticipantsCount().intValue());
        }

        {
            stmt.getMoreResults();
            ResultSet rsDetails = stmt.getResultSet();

            String nameSeparator = " ";
            if (!isForDownload) {
                nameSeparator = ", ";
            }

            items = new ArrayList();
            
            if (rsDetails != null) {
                
                while (rsDetails.next()) {
                    AllocationDetailsReportDetailVO item = new AllocationDetailsReportDetailVO(
                        rsDetails.getString(dbf.PARTICIPANT_ID),
                        rsDetails.getString(dbf.LAST_NAME).trim() + nameSeparator +
                        rsDetails.getString(dbf.FIRST_NAME).trim(),
                        rsDetails.getString(dbf.SOCIAL_SECURITY_NO),
                        rsDetails.getString(dbf.ONGOING_CONTRIB_IND),
                        rsDetails.getBigDecimal(dbf.EMPLOYEE_BALANCE_AMT),
                        rsDetails.getBigDecimal(dbf.EMPLOYER_BALANCE_AMT),
                        rsDetails.getBigDecimal(dbf.TOTAL_BALANCE_AMT));
    
                    items.add(item);
                }
                
            }
            
            reportData.setDetails(items);
        }

        if (logger.isDebugEnabled())
            logger.debug("exit <- doLoadReportData");
    }

    /**
     * todo: this could be generic if we had getFieldToColumnMap
     */
    private static String createSortPhrase(ReportCriteria criteria) {

        StringBuffer result = new StringBuffer();
        Iterator sorts = criteria.getSorts().iterator();

		String initialSortDirection = null;
		boolean isSortOnOngoing = false;
		
        while (sorts.hasNext()) {
            ReportSort sort = (ReportSort) sorts.next();
            Object fieldsRaw = attributeToColumnMap.get(sort.getSortField());
            String[] fields = (fieldsRaw.getClass().isArray() ? (String[]) fieldsRaw :
                new String[]{(String) fieldsRaw});
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i];
                result.append(field).append(" ");
                if (AllocationDetailsReportDetailVO.keys.ongoingContributions.equals(sort.getSortField())) {
                    result.append("desc".equals(sort.getSortDirection()) ? "asc" : "desc");
					isSortOnOngoing = true;
                } else {
                	if (initialSortDirection == null || isSortOnOngoing) {
	                    result.append(sort.getSortDirection());
                	} else {
	                    result.append("desc".equals(initialSortDirection) ? "asc" : "desc");
                	}
                }

                if (i < fields.length - 1)
                    result.append(", ");
            }

			initialSortDirection = sort.getSortDirection();
			
            if (sorts.hasNext()) {
                result.append(", ");
            }
        }
		//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXX ===== " + result.toString());
        return result.toString();
    }
}
