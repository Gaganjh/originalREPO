package com.manulife.pension.bd.web.usermanagement;

import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.bd.web.BDErrorCodes;
import com.manulife.pension.bd.web.util.BDWebCommonUtils;
import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.broker.valueobject.BrokerEntityAddress;
import com.manulife.pension.service.broker.valueobject.BrokerEntityExtendedProfile;
import com.manulife.pension.service.security.bd.BDUserPasswordStatus;
import com.manulife.pension.service.security.bd.BDUserProfileStatus;
import com.manulife.pension.service.security.bd.exception.BDSecurityOperationNotAllowedException;
import com.manulife.pension.service.security.bd.exception.BDUserPartyNotExistException;
import com.manulife.pension.service.security.bd.exception.BDUserPartyRemoveException;
import com.manulife.pension.service.security.exception.DisabledUserException;
import com.manulife.pension.service.security.exception.SecurityServiceException;
import com.manulife.pension.service.security.exception.UserNotFoundException;

/**
 * A helper class for UserManagement pages
 * 
 * @author guweigu
 * 
 */
public class UserManagementHelper {
	private static EnumMap<BDUserProfileStatus, String> SelfRegisteredProfileStatusMap = new EnumMap<BDUserProfileStatus, String>(
			BDUserProfileStatus.class);

	private static EnumMap<BDUserProfileStatus, String> CreatedProfileStatusMap = new EnumMap<BDUserProfileStatus, String>(
			BDUserProfileStatus.class);

	private static EnumMap<BDUserPasswordStatus, String> PasswordStatusMap = new EnumMap<BDUserPasswordStatus, String>(
			BDUserPasswordStatus.class);

	public final static Map<String, Integer> UserManagementSecurityServiceExceptionMapping = new HashMap<String, Integer>(
			7);

	private static final String UserManagementExceptionAttr = "security.userManagementException";

	static {
		SelfRegisteredProfileStatusMap.put(BDUserProfileStatus.New,
				"Not Activated");
		SelfRegisteredProfileStatusMap.put(BDUserProfileStatus.Registered,
				"Activated");

		CreatedProfileStatusMap.put(BDUserProfileStatus.New, "Not Registered");
		CreatedProfileStatusMap.put(BDUserProfileStatus.Registered,
				"Registered");

		PasswordStatusMap.put(BDUserPasswordStatus.Active, "Active");
		PasswordStatusMap.put(BDUserPasswordStatus.Locked, "Locked");
		PasswordStatusMap
				.put(BDUserPasswordStatus.Reset,
						"Reset requested by John Hancock (e-mail with temporary password sent)");
		PasswordStatusMap.put(BDUserPasswordStatus.Inactive,
				"Forgot password initiated (e-mail with activation link sent)");

		UserManagementSecurityServiceExceptionMapping.put(
				DisabledUserException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);

		UserManagementSecurityServiceExceptionMapping.put(
				UserNotFoundException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);

		UserManagementSecurityServiceExceptionMapping.put(
				BDUserPartyNotExistException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);

		UserManagementSecurityServiceExceptionMapping.put(
				BDUserPartyRemoveException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);
		
		UserManagementSecurityServiceExceptionMapping.put(
				BDSecurityOperationNotAllowedException.class.getName(),
				BDErrorCodes.USER_MANAGEMENT_CONFLICT_ERROR);
	}

	public static EnumMap<BDUserProfileStatus, String> getSelfRegisteredProfileStatusMap() {
		return SelfRegisteredProfileStatusMap;
	}

	public static EnumMap<BDUserProfileStatus, String> getCreatedProfileStatusMap() {
		return CreatedProfileStatusMap;
	}

	public static EnumMap<BDUserPasswordStatus, String> getPasswordStatusMap() {
		return PasswordStatusMap;
	}

	/**
	 * Returns a flag to indicate whether to show the company name or not
	 * 
	 * @param profile
	 * @return boolean
	 */
	public static boolean showCompanyName(BrokerEntityExtendedProfile profile) {
		return !StringUtils.isEmpty(profile.getOrgName())
				|| !BDWebCommonUtils.isSsn(profile.getSsnTaxId());
	}

	/**
	 * Returns a flag to indicate whether to show the personal name
	 * 
	 * @param profile
	 * @return boolean
	 */
	public static boolean showPersonalName(BrokerEntityExtendedProfile profile) {
		return !StringUtils.isEmpty(profile.getFirstName())
				|| !StringUtils.isEmpty(profile.getMiddleInit())
				|| !StringUtils.isEmpty(profile.getLastName())
				|| BDWebCommonUtils.isSsn(profile.getSsnTaxId());
	}

	/**
	 * Returns mailing address. If mailing address is empty then returns home
	 * address
	 * 
	 * @param profile
	 * @return BrokerEntityAddress
	 */
	public static BrokerEntityAddress getAddress(
			BrokerEntityExtendedProfile profile) {
		return (!profile.getMailingAddress().isBlank()) ? profile
				.getMailingAddress() : profile.getHomeAddress();
	}

	/**
	 * Returns the display string for USA indicator
	 * 
	 * @param profile
	 * @return String
	 */
	public static String getUSAIndicatorValue(
			BrokerEntityExtendedProfile profile) {
		BrokerEntityAddress address = getAddress(profile);
		return (address.getUsaIndicator()) ? BDConstants.YES_VALUE
				: BDConstants.NO_VALUE;
	}

	/**
	 * Returns the sorted BDFirms by firm name in ascending order
	 * 
	 * @param firms
	 * @return
	 */
	public static SortedSet<String> getSortedBDFirmNames(
			List<BrokerDealerFirm> firms) {
		SortedSet<String> firmNames = new TreeSet<String>();
		if (firms != null) {
			for (BrokerDealerFirm firm : firms) {
				firmNames.add(firm.getFirmName());
			}
		}
		return firmNames;
	}
	
	/**
	 * This method is used to get the RIA Firm names with permissions
	 * 
	 * @param firms
	 * @return riaFirmNamesWithPermissions
	 */
	public static SortedSet<String> getSortedRiaFirmNamesWithPermissions(
			List<BrokerDealerFirm> firms) {
		SortedSet<String> riaFirmNamesWithPermissions = new TreeSet<String>();
		if (firms != null) {
			for (BrokerDealerFirm firm : firms) {
				if(firm.isFirmPermission()){
					riaFirmNamesWithPermissions.add(firm.getFirmName());
				}
			}
		}
		return riaFirmNamesWithPermissions;
	}

	/**
	 * Sort the broker dealer firm by name
	 * 
	 * @param firms
	 */
	public static void sortFirmsByName(List<BrokerDealerFirm> firms) {
		Collections.sort(firms, new Comparator<BrokerDealerFirm>() {
			public int compare(BrokerDealerFirm o1, BrokerDealerFirm o2) {
				return o1.getFirmName().compareTo(o2.getFirmName());
			}
		});
	}

	/**
	 * Sort the broker dealer firm by Id
	 * 
	 * @param firms
	 */
	public static void sortFirmsById(List<BrokerDealerFirm> firms) {
		Collections.sort(firms, new Comparator<BrokerDealerFirm>() {
			public int compare(BrokerDealerFirm o1, BrokerDealerFirm o2) {
				return o1.getBrokerDealerFirmId().compareTo(
						o2.getBrokerDealerFirmId());
			}
		});
	}

	/**
	 * Whether the SecurityServiceException is caused by user or Entity is
	 * deleted or not found
	 * 
	 * @param e
	 * @return
	 */
	public static boolean isManagementConflictException(
			SecurityServiceException e) {
		String className = e.getClass().getName();
		if (UserManagementSecurityServiceExceptionMapping
				.containsKey(className)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Save SecurityServiceException in session for search user page to display
	 * 
	 * @param e
	 * @return
	 */
	public static void setUserManagementException(HttpServletRequest request,
			SecurityServiceException e) {
		request.getSession().setAttribute(UserManagementExceptionAttr, e);
	}

	/**
	 * Save SecurityServiceException in session for search user page to display
	 * 
	 * @param e
	 * @return
	 */
	public static SecurityServiceException getUserManagementException(
			HttpServletRequest request) {
		SecurityServiceException e = (SecurityServiceException) request
				.getSession().getAttribute(UserManagementExceptionAttr);
		request.getSession().removeAttribute(UserManagementExceptionAttr);
		return e;
	}
}
