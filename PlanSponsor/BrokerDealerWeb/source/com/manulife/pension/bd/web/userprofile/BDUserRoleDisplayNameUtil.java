package com.manulife.pension.bd.web.userprofile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.BDUserRoleType;

/**
 * This utility class provide the sorting for broker dealer user role type
 * 
 * @author guweigu
 * 
 */
public class BDUserRoleDisplayNameUtil {
	private static final Logger logger = Logger.getLogger(BDUserRoleDisplayNameUtil.class);
	private static BDUserRoleDisplayNameUtil instance = new BDUserRoleDisplayNameUtil();

	private EnumMap<BDUserRoleType, String> userRoleDisplayMap = new EnumMap<BDUserRoleType, String>(
			BDUserRoleType.class);

	private List<BDUserRoleType> extUserAscendingOrder = new ArrayList<BDUserRoleType>(
			4);
	private List<BDUserRoleType> intUserAscendingOrder = new ArrayList<BDUserRoleType>(
			5);
	private List<BDUserRoleType> allUserAscendingOrder = new ArrayList<BDUserRoleType>(
			BDUserRoleType.values().length);

	private BDUserRoleDisplayNameUtil() {
		
		try {
			Map<String, String> userRoleLookup = EnvironmentServiceDelegate
					.getInstance(BDConstants.BD_APPLICATION_ID).getUserRoles();
			userRoleDisplayMap.put(BDUserRoleType.BasicFinancialRep,
					userRoleLookup.get(BDUserRoleType.BasicFinancialRep
							.getUserRoleCode()));
			userRoleDisplayMap.put(BDUserRoleType.FinancialRep, userRoleLookup
					.get(BDUserRoleType.FinancialRep.getUserRoleCode()));
			userRoleDisplayMap.put(BDUserRoleType.FinancialRepAssistant,
					userRoleLookup.get(BDUserRoleType.FinancialRepAssistant
							.getUserRoleCode()));
			userRoleDisplayMap.put(BDUserRoleType.FirmRep, userRoleLookup
					.get(BDUserRoleType.FirmRep.getUserRoleCode()));			
//			userRoleDisplayMap.put(BDUserRoleType.RIAUser, userRoleLookup
//					.get(BDUserRoleType.RIAUser.getUserRoleCode()));
			userRoleDisplayMap.put(BDUserRoleType.RIAUser,
					"RIA Statement Viewer");
			userRoleDisplayMap.put(BDUserRoleType.NoAccess,
					"-");
			userRoleDisplayMap.put(BDUserRoleType.NationalAccounts, userRoleLookup
					.get(BDUserRoleType.NationalAccounts.getUserRoleCode()));
			userRoleDisplayMap.put(BDUserRoleType.ContentManager, userRoleLookup
					.get(BDUserRoleType.ContentManager.getUserRoleCode()));
			userRoleDisplayMap.put(BDUserRoleType.RIAUserManager,
					"RIA User Manager");
		} catch (SystemException e) {
			logger.error("Fail to get UserRole lookup, fail back to hard coded ones", e);
			userRoleDisplayMap.put(BDUserRoleType.BasicFinancialRep,
					"Basic Broker");
			userRoleDisplayMap
					.put(BDUserRoleType.FinancialRep, "Financial Rep");
			userRoleDisplayMap.put(BDUserRoleType.FinancialRepAssistant,
					"Financial Rep Assisstant");
			userRoleDisplayMap.put(BDUserRoleType.FirmRep,
					"Broker Dealer Firm Rep");
			userRoleDisplayMap.put(BDUserRoleType.RIAUser,
					"RIA Statement Viewer");
			userRoleDisplayMap.put(BDUserRoleType.NoAccess,
					"-");
			userRoleDisplayMap.put(BDUserRoleType.NationalAccounts, 
					"National Accounts");
			userRoleDisplayMap.put(BDUserRoleType.ContentManager, 
					"Content Manager");
			userRoleDisplayMap.put(BDUserRoleType.RIAUserManager,
					"RIA User Manager");
		}
		extUserAscendingOrder.add(BDUserRoleType.BasicFinancialRep);
		extUserAscendingOrder.add(BDUserRoleType.FinancialRep);
		extUserAscendingOrder.add(BDUserRoleType.FinancialRepAssistant);
		extUserAscendingOrder.add(BDUserRoleType.FirmRep);
		extUserAscendingOrder.add(BDUserRoleType.RIAUser);
		extUserAscendingOrder.add(BDUserRoleType.NoAccess);
		sortRole(extUserAscendingOrder);

		userRoleDisplayMap.put(BDUserRoleType.CAR, "CAR");
		userRoleDisplayMap.put(BDUserRoleType.SuperCAR, "Super CAR");
		userRoleDisplayMap.put(BDUserRoleType.InternalBasic, "Internal Basic");
		userRoleDisplayMap.put(BDUserRoleType.Administrator,
				"Internal Administrator");		
		userRoleDisplayMap.put(BDUserRoleType.RVP, "RVP");
		intUserAscendingOrder.add(BDUserRoleType.CAR);
		intUserAscendingOrder.add(BDUserRoleType.SuperCAR);
		intUserAscendingOrder.add(BDUserRoleType.InternalBasic);
		intUserAscendingOrder.add(BDUserRoleType.Administrator);
		intUserAscendingOrder.add(BDUserRoleType.RVP);
		sortRole(intUserAscendingOrder);

		allUserAscendingOrder.addAll(extUserAscendingOrder);
		allUserAscendingOrder.addAll(intUserAscendingOrder);
		sortRole(allUserAscendingOrder);
	}

	public static BDUserRoleDisplayNameUtil getInstance() {
		return instance;
	}

	public String getDisplayName(BDUserRoleType roleType) {
		return userRoleDisplayMap.get(roleType);
	}

	public List<BDUserRoleType> getSortedExtUserRoles(boolean ascending) {
		return getSortedRoles(extUserAscendingOrder, ascending);
	}

	public List<BDUserRoleType> getSortedIntUserRoles(boolean ascending) {
		return getSortedRoles(intUserAscendingOrder, ascending);
	}

	public List<BDUserRoleType> getSortedAllUserRoles(boolean ascending) {
		return getSortedRoles(allUserAscendingOrder, ascending);
	}

	private List<BDUserRoleType> getSortedRoles(
			List<BDUserRoleType> sortedRoles, boolean ascending) {
		List<BDUserRoleType> sorted = new ArrayList<BDUserRoleType>(sortedRoles
				.size());
		sorted.addAll(sortedRoles);
		if (!ascending) {
			Collections.reverse(sorted);
		}
		return sorted;
	}

	private void sortRole(List<BDUserRoleType> roleList) {
		Collections.sort(roleList, new Comparator<BDUserRoleType>() {
			public int compare(BDUserRoleType t1, BDUserRoleType t2) {

				return getDisplayName(t1).compareTo(getDisplayName(t2));
			}
		});
	}

	public List<BDUserRoleType> sortExtUserRoleSet(EnumSet<BDUserRoleType> roles) {
		List<BDUserRoleType> sortedRoles = BDUserRoleDisplayNameUtil
				.getInstance().getSortedExtUserRoles(true);
		for (Iterator<BDUserRoleType> it = sortedRoles.iterator(); it.hasNext();) {
			BDUserRoleType r = it.next();
			if (!roles.contains(r)) {
				it.remove();
			}
		}
		return sortedRoles;
	}
}
