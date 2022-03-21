package com.manulife.pension.ps.service.report.contract.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.contract.valueobject.PlanDataHistoryDetails;
import com.manulife.pension.ps.service.report.contract.valueobject.PlanDataHistoryReportData;
import com.manulife.pension.service.contract.util.PlanConstants;
import com.manulife.pension.service.contract.valueobject.PlanDataHistoryVO;
import com.manulife.pension.service.dao.BaseDatabaseDAO;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class PlanDataHistoryDAO extends BaseDatabaseDAO {
    private static final String className = PlanDataHistoryDAO.class.getName();

    private static final Map<String, String> queriesMap = new HashMap<String, String>();
    static {
        queriesMap.put(PlanDataHistoryReportData.SORT_FIELD_CREATED_TS, "event.created_ts");
        queriesMap.put(PlanDataHistoryReportData.SORT_FIELD_USER_ID, "event.created_user_id");
        queriesMap.put(PlanDataHistoryReportData.SORT_FIELD_CHANNEL_CODE, "event.source_channel_code");
        queriesMap.put(PlanDataHistoryReportData.SORT_FIELD_FIELD_NAME, "detail.field_name");
    }

    private static final String SELECT_PLAN_DATA_HISTORY_TOTAL_COUNT = 
          " select "
        + "   count(*) as no_of_records"
        + " from"
        + "    " + CUSTOMER_SCHEMA_NAME + "plan_change_history_event event"
        + "   ," + CUSTOMER_SCHEMA_NAME + "plan_change_history_detail detail"
        + " where"
        + "   event.plan_id = detail.plan_id"
        + "   and event.created_ts = detail.created_ts"
        + "   and event.plan_id = ? {0}";
    
    private static final String SELECT_PLAN_DATA_HISTORY =
          " select * from("
        + " select"
        + "   event.plan_id"
        + "   , event.created_ts"
        + "   , event.created_user_id"
        + "   , event.created_user_id_type"
        + "   , event.source_channel_code"
        + "   , detail.sequence_no"
        + "   , detail.field_name"
        + "   , detail.field_value"
        + "   , rownumber() over (order by {0}) as rowNum"
        + " from"
        + "    " + CUSTOMER_SCHEMA_NAME + "plan_change_history_event event"
        + "   ," + CUSTOMER_SCHEMA_NAME + "plan_change_history_detail detail"
        + " where"
        + "   event.plan_id = detail.plan_id"
        + "   and event.created_ts = detail.created_ts"
        + "   and event.plan_id = ?"
        + "   {1}) as planHistoryInfo";

    
    public static ReportData findPlanDataHistory(ReportCriteria criteria) throws SystemException {
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        
        Integer planId = (Integer)criteria.getFilterValue(PlanDataHistoryReportData.FILTER_PLAN_ID);
        String fieldName = (String)criteria.getFilterValue(PlanDataHistoryReportData.FILTER_FIELD_NAME);
        String userId = (String)criteria.getFilterValue(PlanDataHistoryReportData.FILTER_USER_ID);
        String fromDate = (String)criteria.getFilterValue(PlanDataHistoryReportData.FILTER_FROM_DATE);
        String toDate = (String)criteria.getFilterValue(PlanDataHistoryReportData.FILTER_TO_DATE);
        
        String filterClause = "";
        if (!StringUtils.isBlank(fieldName)) {
            filterClause += " and detail.field_name like " + wrapInSingleQuotes(fieldName + "%");
        }
        if (!StringUtils.isBlank(userId)) {
            if (PlanConstants.HISTORY_DUMMY_USER_NAME.toUpperCase().startsWith(userId.toUpperCase())) {
                filterClause += " and event.created_user_id = " + wrapInSingleQuotes(PlanConstants.HISTORY_DUMMY_USER_ID);
            } else {
                filterClause += " and event.created_user_id like " + wrapInSingleQuotes(userId + "%");
            }
        }
        if (!StringUtils.isBlank(fromDate)) {
            filterClause += " and event.created_ts >= '" + fromDate + "'";
        }
        if (!StringUtils.isBlank(toDate)) {
            filterClause += " and event.created_ts < '" + toDate + "'";
        }
        
        String rowNumClause = "";
        int startIndex = criteria.getStartIndex();
        if (startIndex > 0) {
            rowNumClause = " where rowNum >= " + startIndex;
        }
        
        String fetchClause = "";
        int pageSize = criteria.getPageSize();
        if (pageSize > 0) {
            fetchClause = " fetch first " + pageSize + " rows only";
        }
        
        List<PlanDataHistoryDetails> planDataHistoryDetails = new ArrayList<PlanDataHistoryDetails>();
        Integer totalCount = new Integer(0);
        
        try {
            conn = getReadUncommittedConnection(className, CUSTOMER_DATA_SOURCE_NAME);
            
            Object[] args = new Object[]{filterClause};
            String query = MessageFormat.format(SELECT_PLAN_DATA_HISTORY_TOTAL_COUNT, args);
            statement = conn.prepareStatement(query);
            statement.setInt(1, planId);
            statement.execute();
            rs = statement.getResultSet();
            
            if (rs != null) {
                
                if (rs.next()) {
                    totalCount = rs.getInt("no_of_records");
                }
                
            }
            
            args = new Object[]{criteria.getSortPhrase(queriesMap), filterClause};
            query = MessageFormat.format(SELECT_PLAN_DATA_HISTORY, args) + rowNumClause + fetchClause;
            statement = conn.prepareStatement(query);
            statement.setInt(1, planId);
            statement.execute();
            rs = statement.getResultSet();
            
            if (rs != null) {
                
                while (rs.next()) {
                    PlanDataHistoryDetails pdhd = new PlanDataHistoryDetails();
                    pdhd.setPlanId(rs.getInt("plan_id"));
                    pdhd.setCreatedTs(rs.getTimestamp("created_ts"));
                    
                    String uId = StringUtils.trimToEmpty(rs.getString("created_user_id"));
                    uId = (uId.equals(PlanConstants.HISTORY_DUMMY_USER_ID)) ? PlanConstants.HISTORY_DUMMY_USER_NAME : uId;
                    pdhd.setUserId(uId);
                    
                    pdhd.setUserIdType(StringUtils.trimToEmpty(rs.getString("created_user_id_type")));
                    pdhd.setChannelCode(StringUtils.trimToEmpty(rs.getString("source_channel_code")));
                    pdhd.setSequenceNo(rs.getInt("sequence_no"));
                    
                    String fName = StringUtils.trimToEmpty(rs.getString("field_name"));
                    pdhd.setFieldName(fName);
                    
                    String nValue = StringUtils.trimToEmpty(rs.getString("field_value"));
                    
                    if (!Arrays.asList(PlanDataHistoryVO.txtFields).contains(fName)) {
                        nValue = (nValue.equals("")) ? PlanConstants.U_VALUE : nValue;
                    }
                    
                    pdhd.setNewValue(nValue);
                    
                    planDataHistoryDetails.add(pdhd);
                }
                
            }
            
        } catch (SQLException e) {
            throw new SystemException(e, className, "findPlanDataHistory",
                    "Problem occurred during call.");
        } finally {
            close(statement, conn);
        }
        
        PlanDataHistoryReportData reportData = new PlanDataHistoryReportData(criteria, totalCount);
        reportData.setDetails(planDataHistoryDetails);
        
        return reportData;
    }
}
