package com.manulife.pension.bd.web.userprofile;

import java.util.HashSet;
import java.util.Set;

/**
 * Broker user profile for the session
 * 
 * @author guweigu
 * 
 */
public class BDBrokerUserProfile extends BDExternalUserProfile {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The profile id set that contains all the assistants that can not be
	 * sent invitation emails
	 */
	private Set<Long> disableAssistantsInvitationList;

	public boolean isDisableAssistant(long profileId) {
		return disableAssistantsInvitationList == null ? false
				: disableAssistantsInvitationList.contains(profileId);
	}

	public void removeDisableAssistant(long profileId) {
		if (disableAssistantsInvitationList != null) {
			disableAssistantsInvitationList.remove(profileId);
		}
	}
	public void addDisableAssistant(long profileId) {
		if (disableAssistantsInvitationList == null) {
			disableAssistantsInvitationList = new HashSet<Long>();
		}
		disableAssistantsInvitationList.add(profileId);
	}

}
