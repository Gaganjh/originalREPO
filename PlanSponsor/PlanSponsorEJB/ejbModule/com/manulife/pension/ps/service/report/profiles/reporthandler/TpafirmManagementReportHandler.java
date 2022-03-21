package com.manulife.pension.ps.service.report.profiles.reporthandler;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.profiles.valueobject.TpafirmManagementReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.dao.SecurityChangeHistoryDAO;
import com.manulife.pension.service.security.valueobject.SecurityChangeHistory;

/**
 * @author marcest
 * 
 */
public class TpafirmManagementReportHandler implements ReportHandler {

    public static final String REPORT_ID = TpafirmManagementReportHandler.class.getName();
    public static final String REPORT_NAME = "tpafirmManagementReport";
    private static final Map fieldToColumnMap = new HashMap();

    static {
        fieldToColumnMap.put(TpafirmManagementReportData.SORT_CHANGED_BY, "CHANGED_BY_USER_PROFILE_ID");
        fieldToColumnMap.put(TpafirmManagementReportData.SORT_CONTRACT_NUMBER, "CN.CONTRACT_ID");
        fieldToColumnMap.put(TpafirmManagementReportData.SORT_DATE, "CREATED_TS");
        fieldToColumnMap.put(TpafirmManagementReportData.SORT_FIRM_ID, "TPA.THIRD_PARTY_ADMIN_ID");
        fieldToColumnMap.put(TpafirmManagementReportData.SORT_ITEM, "SECURITY_CHANGE_ITEM_CODE");
        fieldToColumnMap.put(TpafirmManagementReportData.SORT_ROLE, "CHANGED_BY_USER_ROLE_CODE");
        fieldToColumnMap.put(TpafirmManagementReportData.SORT_TEAM_CODE, "TEAM_CODE");

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
     */
    public ReportData getReportData(ReportCriteria reportCriteria) throws SystemException, ReportServiceException {
        ReportData data = null;

        String teamCode = (String) reportCriteria.getFilterValue(TpafirmManagementReportData.FILTER_TEAM_CODE);
        Integer contractNumber = (Integer) reportCriteria.getFilterValue(TpafirmManagementReportData.FILTER_CONTRACT_NUMBER);
        Date startDate = new Date(((java.util.Date) reportCriteria.getFilterValue(TpafirmManagementReportData.FILTER_START_DATE)).getTime());

        Object endDateFilter = reportCriteria.getFilterValue(TpafirmManagementReportData.FILTER_END_DATE);
        Date endDate = null;
        
        if (endDateFilter != null)
        {
        	endDate = new Date(((java.util.Date)endDateFilter).getTime());
        }

        String lastName = (String) reportCriteria.getFilterValue(TpafirmManagementReportData.FILTER_LAST_NAME);
        String userRoles = (String) reportCriteria.getFilterValue(TpafirmManagementReportData.FILTER_ROLES);
        String orderBy = reportCriteria.getSortPhrase(fieldToColumnMap);
        if (StringUtils.isEmpty(orderBy)) {
            orderBy = null;
        }

        int reportStartIndex = reportCriteria.getStartIndex();
        int reportEndIndex = getEndIndex(reportCriteria.getStartIndex(), reportCriteria.getPageSize());

        // The index passed into the dao may be different than that required by the report if we need to sort by item
        int daoStartIndex = -1;
        int daoEndIndex = -1;

        boolean sortByItem = reportCriteria.getSorts().size() > 0 && TpafirmManagementReportData.SORT_ITEM.equals(reportCriteria.getSorts().get(0).getSortField());
        if (!sortByItem) {
            daoStartIndex = reportStartIndex;
            daoEndIndex = reportEndIndex;
        }

        data = SecurityChangeHistoryDAO.getServiceChangeHistoryData(SecurityChangeHistoryDAO.ActionCode.UPDATE_TPA_FIRM_ACTION_CODE, teamCode, (contractNumber == null) ? 0
                : contractNumber, startDate, endDate, lastName, null, userRoles, orderBy, daoStartIndex, daoEndIndex);

        // If the data is to be sorted by item name we need to sort it manually since only the item code is in the database.
        if (sortByItem && !data.getDetails().isEmpty()) {
            ArrayList<SecurityChangeHistory> details = new ArrayList<SecurityChangeHistory>(data.getDetails());
            if (reportCriteria.getSorts().get(0).isAscending()) {
                Collections.sort(details, new Comparator<SecurityChangeHistory>() {
                    public int compare(SecurityChangeHistory sch1, SecurityChangeHistory sch2) {
                        return sch1.getItemName().compareToIgnoreCase(sch2.getItemName());
                    }
                });
            } else {
                Collections.sort(details, new Comparator<SecurityChangeHistory>() {
                    public int compare(SecurityChangeHistory sch1, SecurityChangeHistory sch2) {
                        return sch2.getItemName().compareToIgnoreCase(sch1.getItemName());
                    }
                });
            }
            data.getDetails().clear();
            data.getDetails().addAll(
                    details.subList(reportStartIndex < 1 ? 0 : reportStartIndex - 1, reportEndIndex < 0 || reportEndIndex > details.size() ? details.size() : reportEndIndex));
        }

        data.setReportCriteria(reportCriteria);
        return data;
    }

    /**
     * Get end index
     * 
     * @param startIndex
     * @param pageSize
     * @return
     */
    private static int getEndIndex(int startIndex, int pageSize) {
        if (pageSize == -1) {
            return -1;// need fix
        } else {
            return startIndex + (pageSize - 1);
        }
    }

}
