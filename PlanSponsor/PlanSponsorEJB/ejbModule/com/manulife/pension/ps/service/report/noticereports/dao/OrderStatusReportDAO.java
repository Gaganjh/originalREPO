package com.manulife.pension.ps.service.report.noticereports.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.noticereports.valueobject.OrderStatusReportData;
import com.manulife.pension.ps.service.report.noticereports.valueobject.OrderStatusReportVO;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.valueobject.ReportCriteria;

/**
 * Provides data access to Notice Manager Alerts Control Report.
 * 
 * 
 */

public class OrderStatusReportDAO extends NoticeReportsBaseDAO {

	/**
	 * Class name
	 */
	//SimpleDateFormat is converted to FastDateFormat to make it thread safe		
	public static FastDateFormat searchDateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.S", Locale.US);
	private static final String className = OrderStatusReportDAO.class
			.getName();
	
	private static final String SQL_ORDER_STATUS_COUNT = "SELECT L.LOOKUP_DESC, CNM.ORDER_STATUS_CODE, "
				 + "COUNT(DISTINCT CNM.ORDER_NO ) FROM "
		         + PLAN_SPONSOR_SCHEMA_NAME 
		         + "CONTRACT_NOTICE_MAILING_ORDER CNM ,"
		         + PLAN_SPONSOR_SCHEMA_NAME
		         + "LOOKUP L WHERE CNM.ORDER_STATUS_CODE = L.LOOKUP_CODE "
		         + "AND L.LOOKUP_CODE IN(''CM'',"
		         + "''CN'',''ER'',"
		         + "''IC'',''IN'', ''IP'') "
		         + "AND CNM.ORDER_STATUS_DATE BETWEEN ? AND ?  {0} "
		         + "GROUP BY L.LOOKUP_DESC,"
		         + "CNM.ORDER_STATUS_CODE";
	/**
	 * Logger object
	 */
	private static final Logger logger = Logger
			.getLogger(OrderStatusReportDAO.class);

	public static void getReportData(ReportCriteria criteria,
			OrderStatusReportData orderStatusReportData)
			throws SystemException, ReportServiceException {
		Integer contractId = getContractNumber(criteria);
        boolean isContractSearch = (contractId != null && contractId.intValue() > 0);
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ArrayList<OrderStatusReportVO> orderStatusReportVOList = null;
		try {
			connection = getReadUncommittedConnection(className,
					CUSTOMER_DATA_SOURCE_NAME);
			
			String contractClause = getContractClause(isContractSearch);
	        String query = MessageFormat.format(SQL_ORDER_STATUS_COUNT,
	                contractClause);
	        stmt = connection.prepareStatement(query);
			setReportFilters(orderStatusReportData,contractId,isContractSearch, stmt);
			
			rs = stmt.executeQuery();
			orderStatusReportVOList = new ArrayList<OrderStatusReportVO>();
			OrderStatusReportVO orderStatusVO1 = new OrderStatusReportVO();
			OrderStatusReportVO orderStatusVO2 = new OrderStatusReportVO();
			OrderStatusReportVO orderStatusVO3 = new OrderStatusReportVO();
			OrderStatusReportVO orderStatusVO4 = new OrderStatusReportVO();
			OrderStatusReportVO orderStatusVO5 = new OrderStatusReportVO();
			OrderStatusReportVO orderStatusVO6 = new OrderStatusReportVO();
			orderStatusVO1.setOrderStatusType(OrderStatusReportData.INITIATED_STATUS);
			orderStatusVO1.setTotalOrderStatusCount(0);
			orderStatusVO2.setOrderStatusType(OrderStatusReportData.ERROR_INVALID_REQUEST_STATUS);
			orderStatusVO2.setTotalOrderStatusCount(0);
			orderStatusVO3.setOrderStatusType(OrderStatusReportData.NOT_COMPLETED_STATUS);
			orderStatusVO3.setTotalOrderStatusCount(0);
			orderStatusVO4.setOrderStatusType(OrderStatusReportData.IN_PROGRESS_STATUS);
			orderStatusVO4.setTotalOrderStatusCount(0);
			orderStatusVO5.setOrderStatusType(OrderStatusReportData.CANCELLED_STATUS);
			orderStatusVO5.setTotalOrderStatusCount(0);
			orderStatusVO6.setOrderStatusType(OrderStatusReportData.COMPLETED_STATUS);
			orderStatusVO6.setTotalOrderStatusCount(0);
			orderStatusReportVOList.add(orderStatusVO1);
			orderStatusReportVOList.add(orderStatusVO2);
			orderStatusReportVOList.add(orderStatusVO3);
			orderStatusReportVOList.add(orderStatusVO4);
			orderStatusReportVOList.add(orderStatusVO5);
			orderStatusReportVOList.add(orderStatusVO6);
			if (rs != null ) {
            	while (rs.next()) {
            		OrderStatusReportVO orderStatusReportVO = new OrderStatusReportVO();
            		if(OrderStatusReportData.INITIATED_STATUS_CODE.equals(rs.getString(2))){
            			orderStatusReportVO.setOrderStatusType(OrderStatusReportData.INITIATED_STATUS);
            			orderStatusReportVO.setTotalOrderStatusCount(rs.getInt(3));
            			orderStatusReportVOList.set(0,orderStatusReportVO);
            		}else if(OrderStatusReportData.ERROR_INVALID_REQUEST_STATUS_CODE.equals(rs.getString(2))){
            			orderStatusReportVO.setOrderStatusType(OrderStatusReportData.ERROR_INVALID_REQUEST_STATUS);
            			orderStatusReportVO.setTotalOrderStatusCount(rs.getInt(3));
            			orderStatusReportVOList.set(1,orderStatusReportVO);
            		}else if(OrderStatusReportData.NOT_COMPLETED_STATUS_CODE.equals(rs.getString(2))){
            			orderStatusReportVO.setOrderStatusType(OrderStatusReportData.NOT_COMPLETED_STATUS);
            			orderStatusReportVO.setTotalOrderStatusCount(rs.getInt(3));
            			orderStatusReportVOList.set(2,orderStatusReportVO);
            		}else if(OrderStatusReportData.IN_PROGRESS_STATUS_CODE.equals(rs.getString(2))){
            			orderStatusReportVO.setOrderStatusType(OrderStatusReportData.IN_PROGRESS_STATUS);
            			orderStatusReportVO.setTotalOrderStatusCount(rs.getInt(3));
            			orderStatusReportVOList.set(3,orderStatusReportVO);
            		}else if(OrderStatusReportData.CANCELLED_STATUS_CODE.equals(rs.getString(2))){
            			orderStatusReportVO.setOrderStatusType(OrderStatusReportData.CANCELLED_STATUS);
            			orderStatusReportVO.setTotalOrderStatusCount(rs.getInt(3));
            			orderStatusReportVOList.set(4,orderStatusReportVO);
            		}else{
            			orderStatusReportVO.setOrderStatusType(OrderStatusReportData.COMPLETED_STATUS);
        			    orderStatusReportVO.setTotalOrderStatusCount(rs.getInt(3));
        			    orderStatusReportVOList.set(5,orderStatusReportVO);
			        }
			}
            	orderStatusReportData.setOrderStatusReportVOList(orderStatusReportVOList); 	
		  } 
		} catch (SQLException e) {
			handleSqlException(e, className, "getReportData",
					"Error while accessing OrderStatus Report data ");
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					handleSqlException(e, className,
							"getReportData",
							"Something went wrong while getting the data from resultset "); 
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					handleSqlException(e, className,
							"getReportData",
							"Something went wrong while executing the statement "); 
				}
			}
			if (connection != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					handleSqlException(e, className,
							"getReportData",
							"Something went wrong while executing the statement "); 
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("exit <- getAlertFrequencyStats");
		}
	}
	
	 /**
     * Sets Report Search Filters.
     * 
     * @param orderStatusReportData
     * @param contractId
     * @param isContractSearch
     * @param stmt
     * @throws SQLException
	 * @throws SystemException 
     */
    private static void setReportFilters(OrderStatusReportData orderStatusReportData,
            Integer contractId, boolean isContractSearch, PreparedStatement stmt)
            throws SQLException, SystemException {
    	Date fromDate = null;
	    Date toDate = null;
		try {
			fromDate = (Date) searchDateFormat.parse(orderStatusReportData.getFromDate().toString() + " 00:00:00.0");
			toDate = (Date) searchDateFormat.parse(orderStatusReportData.getToDate().toString() + " 23:59:59.66");
		} catch (ParseException e) {
			throw new SystemException(
					"Error while parsing fromDate and toDate" + e.getMessage());
		}
			 Timestamp fromTimestamp = new Timestamp(fromDate.getTime());
			 Timestamp toTimestamp = new Timestamp(toDate.getTime());
	        stmt.setTimestamp(1, fromTimestamp);
	        stmt.setTimestamp(2, toTimestamp);
        if (isContractSearch) {
            stmt.setInt(3, contractId.intValue());
        }

    }

	
	/**
     * Method to get the Contract Number for the given Report Criteria.
     * 
     * @param criteria The ReportCriteria that contains the filter.
     * @return The contract number.
     * @throws SQLException
     * @throws SystemException
     */
    public static Integer getContractNumber(ReportCriteria criteria) throws SystemException {
        Integer contractNumberString = (Integer) criteria.getFilterValue(FILTER_CONTRACT_NUMBER);
        Integer contractNumber = Integer.valueOf(0);
        if (contractNumberString != null) {
            contractNumber = Integer.valueOf(contractNumberString);
        }
        return contractNumber;

    }
    
    /**
     * Returns the SQL clause if contract filter is active.
     * 
     * @param isContractSearch
     * @return
     */
    private static String getContractClause(boolean isContractSearch) {
        String clause = "";
        if (isContractSearch) {
            clause = SQL_CONTRACT_FILTER_CLAUSE_PLAIN;
        }
        return clause;
    }
}
