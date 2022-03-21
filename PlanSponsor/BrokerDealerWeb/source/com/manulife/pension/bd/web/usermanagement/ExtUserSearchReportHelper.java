package com.manulife.pension.bd.web.usermanagement;

import com.manulife.pension.bd.web.userprofile.BDUserRoleDisplayNameUtil;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.bd.report.valueobject.BDExtUserSearchData;

/**
 * Helper class for External user search report
 * @author guweigu
 *
 */
public class ExtUserSearchReportHelper {
	private static String NoCriteria = "At least one search criteria needs to be entered to do search";
	private static String NoMatch = "The search does not have matching record";

	public static String getEmptyResultReason(BDExtUserSearchData reportData) {
		if (reportData.getReportCriteria().getFilters().size() == 0) {
			return NoCriteria;
		} else {
			return NoMatch;
		}
	}
	
	public static String getUserRoleDisplay(BDUserRoleType roleType) {
		return BDUserRoleDisplayNameUtil.getInstance().getDisplayName(roleType);
	}
}
