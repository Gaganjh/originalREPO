package com.manulife.pension.ps.web.messagecenter.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.util.Environment;
import com.manulife.pension.ps.web.util.SessionHelper;
import com.manulife.pension.service.contract.util.Constants;
import com.manulife.pension.service.contract.valueobject.Contract;
import com.manulife.pension.service.contract.valueobject.ContractServiceFeature;

/**
 * Message center environment related utility methods
 * @author guweigu
 *
 */
public class MCEnvironment implements MCConstants {
	static int homePageUrgentMessageCount = 0;
	static int urgentMessageCount = 0;
	static int maxMessageCount = 0;
	static int defaultMessageCount = 0;

	/**
	 * Returns the environment defined Home Page maximum urgent message
	 * @return
	 */
	public static int getHomePageUrgentMessageCount() {
		if (homePageUrgentMessageCount == 0) {
			try {
				homePageUrgentMessageCount = Environment.getInstance()
						.getIntNamingVariable(HomePageUrgentMessageCountName,
								null, HomePageUrgentMessageCount);
			} catch (Exception e) {
				homePageUrgentMessageCount = HomePageUrgentMessageCount;
			}
		}
		return homePageUrgentMessageCount;
	}

	/**
	 * Returns the environment defined maximum urgent message count
	 * within Message center
	 * @return
	 */
	public static int getUrgentMessageCount() {
		if (urgentMessageCount == 0) {
			try {
				urgentMessageCount = Environment.getInstance()
						.getIntNamingVariable(UrgentMessageCountName, null,
								UrgentMessageCount);
			} catch (Exception e) {
				urgentMessageCount = UrgentMessageCount;
			}
		}
		return urgentMessageCount;
	}

	/**
	 * Returns the environment defined maximum  message count
	 * @return
	 */
	public static int getMaximumMessageCount() {
		if (maxMessageCount == 0) {
			try {
				maxMessageCount = Environment.getInstance()
						.getIntNamingVariable(MaxMessageCountName, null,
								MaxMessageCount);
			} catch (Exception e) {
				maxMessageCount = MaxMessageCount;
			}
		}
		return maxMessageCount;
	}

	/**
	 * Returns the environment defined default message count
	 * @return
	 */

	public static int getDefaultMessageCount() {
		if (defaultMessageCount == 0) {
			try {
				defaultMessageCount = Environment.getInstance()
						.getIntNamingVariable(DefaultMessageCountName, null,
								DefaultMessageCount);
			} catch (Exception e) {
				defaultMessageCount = DefaultMessageCount;
			}
		}
		return defaultMessageCount;
	}

	/**
	 * Whether a message center summary is available. (Not available for
	 * internal user)
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isMessageCenterSummaryBoxAvailable(
			HttpServletRequest request) {
		return !SessionHelper.getUserProfile(request).getRole()
				.isInternalUser()
				&& MCEnvironment.isMessageCenterAvailable(request);
	}

    /**
     * Whether the message center should be shown
     * Message Center is not available to unallocated contracts
     */
	public static boolean isMessageCenterAvailable(HttpServletRequest request) {
        // MessageCenterVersion is obsolete
//      if (MCEnvironment.getMessageCenterVersion() == PhaseIVersion) {
        
        if (request.getSession(false) == null)
            return false;
        
        UserProfile profile = SessionHelper.getUserProfile(request);
        if (profile != null) {
            Contract c = profile.getCurrentContract();
            if (c != null) {
                // unallocated contracts don't have CSF
                if (c.getServiceFeatureMap() != null ) {
                    return true;
                }
            } else if (c == null
                    && !CollectionUtils.isEmpty(profile
                            .getMessageCenterAccessibleContracts())) {
                return true;
            } else if ( c == null && profile.isInternalUser() ) {
                return true;
            }
        }
        return false;
        
//      } else {
//          return true;
//      }
	}

/*	public static int getMessageCenterVersion() {
		try {
			return Environment.getInstance().getIntNamingVariable(
					MessageCenterVersion, null, DefaultMessageCenterVersion);
		} catch (Exception e) {
			return DefaultMessageCenterVersion;
		}
	}
*/
}
