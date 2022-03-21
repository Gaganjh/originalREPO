package com.manulife.pension.ps.web.profiles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.ps.web.controller.SecurityManager;
import com.manulife.pension.service.security.BDUserRoleType;
import com.manulife.pension.service.security.role.ExternalUser;
import com.manulife.pension.service.security.role.InternalUser;
import com.manulife.pension.service.security.role.InternalUserManager;
import com.manulife.pension.service.security.role.ThirdPartyAdministrator;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.UserRoleFactory;
import com.manulife.pension.service.security.utility.SecurityConstants;
import com.manulife.pension.service.security.valueobject.ManageUsersReportData;

/**
 * Helper class for ManageProfile pages.
 * 
 * @author Maria Lee
 */
public class AccessLevelHelper {
	private static final AccessLevelHelper instance = new AccessLevelHelper();

	public static final String NO_ACCESS = "NA";
	public static final String TPA_ACCESS = "TPA";
	public static final String NO_ACCESS_STRING = "No access";
	public static final String JHTC_OFFICER_ACCESS = "PTC";
	
	private static final String EZKCarRole_STRING = "CAR";
	private static final String EZKSuperCarRole_STRING = "Super CAR";
	private static final String EZKNonCarRole_STRING = "Non CAR";
	private static final String EZKQACarRole_STRING = "QA CAR";

	private static final String BDNoRole = "NA";

	private static Map externalAccessLevelAllocatedMap;
	private static Map externalAccessLevelUnallocatedMap;
	private static Map internalAccessLevelMap;
	private static Map ezkAccessLevelMap;
	private static Map<String, List<LabelValueBean>> bdAccessLevelMap;

	/**
	 * Constructor.
	 */
	private AccessLevelHelper() {
		super();
		initExternalAllocatedAccessMap();
		initExternalUnallocatedAccessMap();
		initInternalAccessMap();
		initEzkAccessMap();
		initBDAccessMap();
	}

	public static AccessLevelHelper getInstance() {
		return instance;
	}

	/**
	 * Allocated contracts - builds a list that contains all external access
	 * levels for that role These lists are used to populate the drop down
	 * access levels for the add/edit external and interal pages.
	 * 
	 */
	private static void initExternalAllocatedAccessMap() {

		externalAccessLevelAllocatedMap = new HashMap();
		List roles = UserRoleFactory.getUserRoles();

		for (Iterator it = roles.iterator(); it.hasNext();) {
			UserRole userRole = (UserRole) it.next();
			externalAccessLevelAllocatedMap.put(userRole.toString(), buildList(
					userRole, true, true));
		}
	}

	/**
	 * Unallocated contracts - builds a list that contains all external access
	 * levels for that role These lists are used to populate the drop down
	 * access levels for the add/edit external and interal pages.
	 * 
	 */
	private static void initExternalUnallocatedAccessMap() {

		externalAccessLevelUnallocatedMap = new HashMap();
		List roles = UserRoleFactory.getUserRoles();

		for (Iterator it = roles.iterator(); it.hasNext();) {
			UserRole userRole = (UserRole) it.next();
			externalAccessLevelUnallocatedMap.put(userRole.toString(),
					buildList(userRole, true, false));
		}
	}

	/**
	 * builds a list that contains all internal access levels for that role
	 * These lists are used to populate the drop down access levels for the
	 * add/edit external and interal pages.
	 * 
	 */
	private static void initInternalAccessMap() {

		internalAccessLevelMap = new HashMap();
		List roles = UserRoleFactory.getUserRoles();

		for (Iterator it = roles.iterator(); it.hasNext();) {
			UserRole userRole = (UserRole) it.next();
			internalAccessLevelMap.put(userRole.toString(), buildList(userRole,
					false, true));
		}

	}

	private static List buildList(UserRole userRole, boolean external,
			boolean unallocated) {

		Collection accessLevels = SecurityManager.getProfileAccessLevel(
				userRole, unallocated);

		List displayList = new ArrayList();

		/* 1st entry is "No access" */

		displayList.add(new LabelValueBean(NO_ACCESS_STRING, NO_ACCESS));

		if (accessLevels != null) {
			for (Iterator levels = accessLevels.iterator(); levels.hasNext();) {
				UserRole accessToRole = (UserRole) levels.next();

				if (external) {
					// all external type minus TPA
					if ((accessToRole instanceof ExternalUser)
							&& !(accessToRole instanceof ThirdPartyAdministrator)) {
						displayList.add(new LabelValueBean(accessToRole
								.getDisplayName(), accessToRole.toString()));
					}
				} else {
					if (accessToRole instanceof InternalUser) {
						displayList.add(new LabelValueBean(accessToRole
								.getDisplayName(), accessToRole.toString()));
					}
				}
			}
		}
		return displayList;
	}

	/**
	 * Builds a drop down list for ezk access. This list is used to populate the
	 * drop down access levels for the internal add/edit pages. Note that only
	 * the IUMs has access to these pages.
	 */
	private static void initEzkAccessMap() {

		ezkAccessLevelMap = new HashMap();
		List ezkAccessDisplayList = new ArrayList();

		ezkAccessDisplayList.add(new LabelValueBean(NO_ACCESS_STRING,
				SecurityConstants.EZKNoRole));
		ezkAccessDisplayList.add(new LabelValueBean(EZKCarRole_STRING,
				SecurityConstants.EZKCarRole));
		ezkAccessDisplayList.add(new LabelValueBean(EZKNonCarRole_STRING,
				SecurityConstants.EZKNonCarRole));
		// PAR NOV 2014 Changes
		ezkAccessDisplayList.add(new LabelValueBean(EZKQACarRole_STRING,
				SecurityConstants.EZKQACarRole));
		ezkAccessDisplayList.add(new LabelValueBean(EZKSuperCarRole_STRING,
				SecurityConstants.EZKSuperCarRole));

		ezkAccessLevelMap.put((new InternalUserManager()).toString(),
				ezkAccessDisplayList);
	}

	/**
	 * Builds a drop down list for broker dealer access. This list is used to
	 * populate the drop down access levels for the internal add/edit pages.
	 * Note that only the IUMs has access to these pages.
	 */
	private static void initBDAccessMap() {
		bdAccessLevelMap = new HashMap<String, List<LabelValueBean>>();
		List<LabelValueBean> bdAccessDisplayList = new ArrayList<LabelValueBean>();

		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.NoAccess), BDNoRole));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.Administrator),
				BDUserRoleType.Administrator.getUserRoleCode()));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.InternalBasic),
				BDUserRoleType.InternalBasic.getUserRoleCode()));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.CAR),
				BDUserRoleType.CAR.getUserRoleCode()));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.ContentManager),
				BDUserRoleType.ContentManager.getUserRoleCode()));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.NationalAccounts),
				BDUserRoleType.NationalAccounts.getUserRoleCode()));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.RIAUserManager),
				BDUserRoleType.RIAUserManager.getUserRoleCode()));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.RVP),
				BDUserRoleType.RVP.getUserRoleCode()));
		bdAccessDisplayList.add(new LabelValueBean(ManageUsersReportData
				.getBdRoleDisplayName(BDUserRoleType.SuperCAR),
				BDUserRoleType.SuperCAR.getUserRoleCode()));
		bdAccessLevelMap.put((new InternalUserManager()).toString(), bdAccessDisplayList);
	}

	/**
	 * Method to get a list of LabelValueBean objects that contains the display
	 * string labels and the corresponding value to be returned allowed.
	 */
	public List getExternalAccessLevels(UserRole userRole, boolean allocated) {

		if (allocated)
			return (List) externalAccessLevelAllocatedMap.get(userRole
					.toString());
		else
			return (List) externalAccessLevelUnallocatedMap.get(userRole
					.toString());
	}

	public List getInternalAccessLevels(UserRole userRole) {
		return (List) internalAccessLevelMap.get(userRole.toString());
	}

	public List getEzkAccessLevels(UserRole userRole) {
		return (List) ezkAccessLevelMap.get(userRole.toString());
	}

	public List getBDAccessLevels(UserRole userRole) {
		return (List) bdAccessLevelMap.get(userRole.toString());
	}

	public static boolean canManageExternal(UserRole currentRole) {
		return SecurityManager.canManageExternal(currentRole);

	}

	public static boolean canManageTPA(UserRole currentRole) {
		return SecurityManager.canManageTPA(currentRole);
	}

	public static boolean canManageInternal(UserRole currentRole) {
		return SecurityManager.canManageInternal(currentRole);
	}
}