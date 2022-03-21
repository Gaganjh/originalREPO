package com.manulife.pension.ps.service.report.profiles.reporthandler;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.service.report.profiles.valueobject.AddressChangesExternalReportData;
import com.manulife.pension.ps.service.report.profiles.valueobject.TpafirmManagementReportData;
import com.manulife.pension.ps.service.report.profiles.valueobject.UserManagementChangesExternalReportData;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;
import com.manulife.pension.service.security.dao.SecurityChangeHistoryDAO;
import com.manulife.pension.service.security.valueobject.SecurityChangeHistory;


/**
 * @author Ranjith
 *
 */
public class AddressChangesExternalReportHandler implements ReportHandler {
	
	public static final String REPORT_ID = AddressChangesExternalReportHandler.class.getName(); 

	/* (non-Javadoc)
	 * @see com.manulife.pension.service.report.handler.ReportHandler#getReportData(com.manulife.pension.service.report.valueobject.ReportCriteria)
	 */
	public ReportData getReportData(ReportCriteria reportCriteria)
			throws SystemException, ReportServiceException { 

		AddressChangesExternalReportData reportData = new AddressChangesExternalReportData();
		reportData.setReportCriteria(reportCriteria);
		
		String actionCode = (String)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_ACTION );
		SecurityChangeHistoryDAO.ActionCode action = SecurityChangeHistoryDAO.ActionCode.getAction(actionCode);
		String teamCode = (String)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_TEAM_CODE );
        String changedByTeamCode = (String)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_CHANGED_BY_TEAM_CODE );
		Integer contractNumber = (Integer)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_CONTRACT_NUMBER );
		String startDateStr = (String)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_START_DATE );
		String endDateStr = (String)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_END_DATE );
		String lastName = (String)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_LAST_NAME );
		String userRoles = (String)reportCriteria.getFilterValue( AddressChangesExternalReportData.FILTER_ROLES );
		String userType =  (String)reportCriteria.getFilterValue(AddressChangesExternalReportData.FILTER_USER_TYPE);
		String orderBy = ""; 
		ReportSortList sortCriteria = reportCriteria.getSorts();
		if (sortCriteria != null) {
			for (int i = 0; i < sortCriteria.size(); i++) {
				ReportSort sort = sortCriteria.get(i);
				String fieldName = sort.getSortField();
				if (fieldName != null) {
					if (i > 0) {
						orderBy += ",";
					}
                    if (fieldName.equals("sortByChangeByTeamCode")) {
                        orderBy += "CHANGED_BY_TEAM_CODE " + sort.getSortDirection();
                    }
					if (fieldName.equals("sortByTeamCode")) {
						orderBy += "TEAM_CODE " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByContractNumber")) {
						orderBy += "SCE.CONTRACT_ID " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByUserName")) {
						orderBy += "CHANGED_BY_LAST_NAME " + sort.getSortDirection() + ", CHANGED_BY_FIRST_NAME " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByUserRole")) {
						orderBy += "USER_ROLE_CODE " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByDate")) {
						orderBy += "CREATED_TS " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByAction")) {
						orderBy += "SECURITY_CHANGE_ACTION_CODE " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByItem")) {
						orderBy += "SECURITY_CHANGE_ITEM_CODE " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByPrimaryContact")) {
						orderBy += "PRIMARY_CONTACT_IND " + sort.getSortDirection();
					}
					if (fieldName.equals("sortByMailRecipientUserRole")) {
						orderBy += "MAIL_RECIPIENT_IND " + sort.getSortDirection();
					}
				}
			}
		}
		if (orderBy == null || orderBy.trim().length() == 0) {
			orderBy = "CHANGED_BY_TEAM_CODE, CHANGED_BY_LAST_NAME, CHANGED_BY_FIRST_NAME, SCE.CONTRACT_ID, USER_ROLE_CODE DESC, CREATED_TS DESC";
		}
		DateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy"); 
		Date startDate = null, endDate = null;

		if (startDateStr != null && startDateStr.trim().length() > 0) {

			java.util.Date parsedStartDate = null;

			try { parsedStartDate = dateFormatter.parse(startDateStr); }
			catch(ParseException e) {}
			
			if (parsedStartDate != null) {
				startDate = new Date(parsedStartDate.getTime());
			}
		}

		if (endDateStr != null && endDateStr.trim().length() > 0) {

			java.util.Date parsedEndDate = null;

			try { parsedEndDate = dateFormatter.parse(endDateStr); }
			catch(ParseException e) {}

			if (parsedEndDate != null) {
				endDate = new Date(parsedEndDate.getTime());
			}
		}
		
		int startIndex = reportCriteria.getStartIndex();
		int endIndex = reportCriteria.getStartIndex() + reportCriteria.getPageSize() - 1;

        // The index passed into the dao may be different than that required by the report if we need to sort by item
        int daoStartIndex = -1;
        int daoEndIndex = -1;

        boolean sortByItem = reportCriteria.getSorts().size() > 0 && TpafirmManagementReportData.SORT_ITEM.equals(reportCriteria.getSorts().get(0).getSortField());
        if (!sortByItem) {
            daoStartIndex = startIndex;
            daoEndIndex = endIndex;
        }

        Collection reportDetails = SecurityChangeHistoryDAO.getServiceChangeHistory(action, teamCode, ((contractNumber == null) ? 0 : contractNumber.intValue()), 
				startDate, endDate, lastName, userType, changedByTeamCode, userRoles, orderBy, daoStartIndex, daoEndIndex );
		
        // If the data is to be sorted by item name we need to sort it manually since only the item code is in the database.
        if (sortByItem && !reportDetails.isEmpty()) {
            ArrayList<SecurityChangeHistory> details = new ArrayList<SecurityChangeHistory>(reportDetails);
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
            reportDetails.clear();
            reportDetails.addAll(details.subList(startIndex < 1 ? 0 : startIndex - 1, endIndex < 0 || endIndex > details.size() ? details.size() : endIndex));
        }
		
		reportData.setDetails(reportDetails);
		reportData.setReportCriteria(reportCriteria);
		reportData.setTotalCount(0);
		if (reportDetails != null && !reportDetails.isEmpty()) {
			Object o[] = reportDetails.toArray();;
			SecurityChangeHistory event1 = (SecurityChangeHistory)o[0];
			reportData.setTotalCount( event1.getTotalCount());
		}
		
		return reportData;
	}

}
