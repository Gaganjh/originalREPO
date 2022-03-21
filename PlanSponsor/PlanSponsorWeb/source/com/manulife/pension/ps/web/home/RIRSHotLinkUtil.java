package com.manulife.pension.ps.web.home;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.delegate.EnvironmentServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.security.PINCipher;
import com.manulife.pension.service.contract.ContractConstants.ContractStatus;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.security.Principal;
import com.manulife.pension.service.security.role.AdministrativeContact;
import com.manulife.pension.service.security.role.AuthorizedSignor;
import com.manulife.pension.service.security.role.Trustee;
import com.manulife.pension.service.security.role.UserRole;

/**
 * The utility class for RIRS HotLink section in Contract Home page
 * 
 * @author guweigu
 * 
 */
public class RIRSHotLinkUtil {

	private static final Logger log = Logger.getLogger(RIRSHotLinkUtil.class);

	public static String USER_ID = "UID";
	public static String FIRST_NAME = "FN";
	public static String LAST_NAME = "LN";
	public static String EMAIL = "Email";
	public static String CONTRACT_ID = "C";

	private static String RIRS_BUSINESS_PARAM_NAME = "RIRS_SMART_LINK_ACTIVE_IND";
	private static String RIRS_BUSINESS_PARAM_ON_VALUE = "Y";

	// The attribute name in the ServletContext for the switch indicator
	private static String RIRS_HOTLINK_ATTR = "RIRS_HOTLINK";

	// The naming space binding for RIRS HOTLINK
	private static String RIRS_HOTLINK_URL_NAMING = "rirsHotLink.url";

	@SuppressWarnings("rawtypes")
	private static List<Class> RIRSAllowedRole = new ArrayList<Class>(3);

	// 15 minutes for Switch indicator cache to expire
	private static long EXPIRY_MILLI_SECOND = 15 * 60 * 1000L;

	static {
		RIRSAllowedRole.add(Trustee.class);
		RIRSAllowedRole.add(AuthorizedSignor.class);
		RIRSAllowedRole.add(AdministrativeContact.class);
	}

	/**
	 * The cached Switch indicator
	 *  
	 */
	static class RIRSHotLinkSwitch {
		private boolean on;
		private long lastUpdatedTs;

		public RIRSHotLinkSwitch() {
			this.on = false;
			this.lastUpdatedTs = 0;
		}

		public boolean isExpired() {
			return System.currentTimeMillis() > lastUpdatedTs
					+ EXPIRY_MILLI_SECOND;
		}

		public void update(boolean on) {
			this.on = on;
			this.lastUpdatedTs = System.currentTimeMillis();
		}

		public boolean isOn() {
			return on;
		}
	}

	/**
	 * Whether the RIRSHotLink should be shown
	 * 
	 * @param user
	 * @return
	 */
	public static boolean showRIRSHotLink(UserProfile user) {
		// just be cautious, if the contract is not selected
		if (user == null) {
			return false;
		}

		Contract contract = user.getCurrentContract();

		// The link is visible only to active DC contract
		if (contract == null
				|| contract.isDefinedBenefitContract()
				|| !ContractStatus.ACTIVE.equals(user.getCurrentContract()
						.getStatus())) {
			return false;
		}

		UserRole role = user.getRole();
		return isAllowedUserRole(role);
	}

	@SuppressWarnings("rawtypes")
	public static boolean isAllowedUserRole(UserRole role) {
		if (role.isInternalUser()) {
			return true;
		} else {
			for (Class c : RIRSAllowedRole) {
				if (c.isInstance(role)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Generate the encrypted HotLink parameters based on the user profile
	 * 
	 * @param user
	 * @return
	 */
	public static String generateRIRHotLinkParam(UserProfile user) {
		// just be cautious, if the contract is not selected
		if (user == null || user.getCurrentContract() == null) {
			return null;
		}

		Principal principal = user.getPrincipal();
		try {
			return generateRIRHotLinkParam(principal.getUserName(), user
					.getCurrentContract().getContractNumber(),
					principal.getFirstName(), principal.getLastName(),
					user.getEmail());
		} catch (SystemException ex) {
			log.error("Fail to generate RIRS hotlink parameter", ex);
			return null;
		}
	}

	/**
	 * The parameter String is the encrypted value of this data:
	 * 
	 * UID=joe_smith (this is the unique key for the user) FN=Joe LN=Smith Jr.
	 * Email=joe_smith@abc.com C=70300
	 * 
	 * @param userId
	 * @param contractId
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @return
	 */
	public static String generateRIRHotLinkParam(String userId, int contractId,
			String firstName, String lastName, String email)
			throws SystemException {
		StringBuffer buffer = new StringBuffer();
		addField(buffer, USER_ID, StringUtils.trimToEmpty(userId));
		addField(buffer, FIRST_NAME, StringUtils.trimToEmpty(firstName));
		addField(buffer, LAST_NAME, StringUtils.trimToEmpty(lastName));
		addField(buffer, EMAIL, StringUtils.trimToEmpty(email));
		addField(buffer, CONTRACT_ID, contractId);

		try {
			return PINCipher.encryptLongString(buffer.toString());
		} catch (Exception ex) {
			log.error("Fail to encrypt the parameters", ex);
			throw new SystemException(ex, "Fail to encrypt the parameters");
		}
	}

	private static void addField(StringBuffer buf, String field, Object value) {
		buf.append(field);
		buf.append("=");
		buf.append(value);
		buf.append("\n");
	}

	/**
	 * Get the RIRSHotLink URL
	 * @return
	 */
	public static String getRIRSHotLinkURL() {
		return Environment.getInstance().getNamingVariable(
				RIRS_HOTLINK_URL_NAMING, null);
	}

	/**
	 * Check if the RIRSHotLink is On
	 * @param context
	 * @return
	 */
	public synchronized static boolean isRIRSHotLinkOn(ServletContext context) {
		RIRSHotLinkSwitch v = (RIRSHotLinkSwitch) context
				.getAttribute(RIRS_HOTLINK_ATTR);

		if (v == null) {
			v = new RIRSHotLinkSwitch();
			context.setAttribute(RIRS_HOTLINK_ATTR, v);
		}
		if (v.isExpired()) {
			boolean isOn = checkRIRSHotLinkSwitch();
			v.update(isOn);
		}
		return v.isOn();
	}

	/**
	 * Get the Switch value from database
	 * @return
	 */
	private static boolean checkRIRSHotLinkSwitch() {
		try {
			String value = EnvironmentServiceDelegate.getInstance()
					.getBusinessParam(RIRS_BUSINESS_PARAM_NAME);
			return StringUtils.equals(RIRS_BUSINESS_PARAM_ON_VALUE,
					value);
		} catch (SystemException ex) {
			log.error("Fail to retrieve RIRSHotLinkSwitch value", ex);
			return false;
		}
	}
}
