package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.delegate.BrokerServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.broker.valueobject.RegionalVicePresident;
import com.manulife.pension.service.security.ium.valueobject.RelationshipManagerRole;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;
import com.manulife.pension.util.BaseEnvironment;

@SuppressWarnings("unchecked")
public class ManageInternalUserHelper {
	private static List<LabelValueBean> pswRoles = new ArrayList<LabelValueBean>();
	private static List<LabelValueBean> bdwRoles = new ArrayList<LabelValueBean>();
	private static Map<Boolean, String> LicenseDisplayMap = new HashMap<Boolean, String>();
	static {
		pswRoles.add(new LabelValueBean("All PSW Roles", ManageUsersReportData.ALL_ROLE));
		pswRoles.addAll(AccessLevelHelper.getInstance()
				.getInternalAccessLevels(new InternalUserManager()));
		bdwRoles.add(new LabelValueBean("All BDW Roles", ManageUsersReportData.ALL_ROLE));
		bdwRoles.addAll(AccessLevelHelper.getInstance().getBDAccessLevels(
				new InternalUserManager()));
		
		LicenseDisplayMap.put(null, "Not provided");
		LicenseDisplayMap.put(true, "Yes");
		LicenseDisplayMap.put(false, "No");
	}

	public static List<LabelValueBean> getPSWRoles() {
		return pswRoles;
	}

	public static List<LabelValueBean> getBDWRoles() {
		return bdwRoles;
	}

	public static String getBDWRoleName(String value) {
		for (LabelValueBean v : bdwRoles) {
			if (v.getValue().equals(value)) {
				return v.getLabel();
			}
		}
		return AccessLevelHelper.NO_ACCESS_STRING;
	}

	/**
	 * This method returns a List of RVP names
	 * 
	 * @return List a list of LableValueBean objects with RVP info.
	 */
	public static List<LabelValueBean> getRVPs() throws SystemException {
		List<RegionalVicePresident> rvpInfo = new ArrayList<RegionalVicePresident>();

		rvpInfo = BrokerServiceDelegate.getInstance(
				new BaseEnvironment().getApplicationId()).getAllRVPs();

		List<LabelValueBean> rvpNames = new ArrayList<LabelValueBean>();

		rvpNames.add(new LabelValueBean("Select", ""));
		for (RegionalVicePresident rvpInfoBean : rvpInfo) {
			rvpNames.add(new LabelValueBean(rvpInfoBean.getLastName() + ", "
					+ rvpInfoBean.getFirstName() + " "
					+ rvpInfoBean.getMiddleName(), Long.toString(rvpInfoBean
					.getId())));
		}
		return rvpNames;
	}
	
	/**
	 * This method returns a List of RM names
	 * 
	 * @return List a list of LableValueBean objects with RM info.
	 * @throws SystemException 
	 */
	
	public static List<LabelValueBean> getRMs() throws SystemException {
		List<RelationshipManagerRole> rmInfo = new ArrayList<RelationshipManagerRole>();

		rmInfo = SecurityServiceDelegate.getInstance().getAllRMs();

		List<LabelValueBean> rmNames = new ArrayList<LabelValueBean>();

		rmNames.add(new LabelValueBean("Select", ""));
		rmNames.add(new LabelValueBean("* Unassigned *", "UA"));
		for (RelationshipManagerRole rmInfoBean : rmInfo) {
			rmNames.add(new LabelValueBean(rmInfoBean.getLastName() + ", "
					+ rmInfoBean.getFirstName() + " "
					+ rmInfoBean.getMiddleName(), rmInfoBean
					.getRmId()));
		}
		return rmNames;
	}
	
	/**
	 * Returns the license verified display according the the boolean value
	 * 
	 * @param value
	 * @return
	 */
	public static String getLicenseVerifiedDisplay(Boolean value) {
		return LicenseDisplayMap.get(value);
	}
}
