package com.manulife.pension.ps.web.delegate.mockable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.manulife.pension.delegate.mockable.MockUserInfos;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.report.exception.ReportServiceException;
import com.manulife.pension.service.report.handler.ReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.service.security.valueobject.UserInfo;

/**
 * This is a mock object for ManageUserReportHandler.
 * 
 * @author Charles Chan
 */
public class MockManageUsersReportHandler implements ReportHandler {

	/**
	 * Constructor.
	 *  
	 */
	public MockManageUsersReportHandler() {
	}

	public ReportData getReportData(ReportCriteria criteria)
			throws SystemException, ReportServiceException {

		List userinfos = MockUserInfos.getInstance().getUserInfos();

		List result = new ArrayList();

		String filter = (String) criteria
				.getFilterValue(ManageUsersReportData.FILTER_FILTER);

		for (Iterator it = userinfos.iterator(); it.hasNext();) {

			UserInfo userInfo = (UserInfo) it.next();

			if (filter.equals(ManageUsersReportData.FILTER_EMPLOYEE_LAST_NAME)
					|| filter
							.equals(ManageUsersReportData.FILTER_EMPLOYEE_NUMBER)) {

				if (userInfo.getRole() instanceof InternalUser) {
					result.add(userInfo);
				}
			} else if (filter.equals(ManageUsersReportData.FILTER_TPA_FIRM_ID)
					|| filter
							.equals(ManageUsersReportData.FILTER_TPA_LAST_NAME)) {
				if (userInfo.getRole() instanceof ThirdPartyAdministrator) {
					result.add(userInfo);
				}
			} else {
				if (userInfo.getRole() instanceof ExternalUser
						&& (!(userInfo.getRole() instanceof ThirdPartyAdministrator))) {
					result.add(userInfo);
				}
			}
		}

		ManageUsersReportData data = new ManageUsersReportData(criteria,
				result.size());

		/*
		 * Handles paging properly.
		 */
		if (result.size() > criteria.getPageSize()) {
			int startIndex = criteria.getStartIndex() - 1;
			int endIndex = startIndex + criteria.getPageSize();
			if (endIndex > result.size()) {
				endIndex = result.size();
			}
			List subList = result.subList(startIndex, endIndex);
			result = new ArrayList();
			result.addAll(subList);
		}

		data.setDetails(result);

		return data;
	}
}