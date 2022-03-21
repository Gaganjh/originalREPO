package com.manulife.pension.bd.web.messagecenter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.bd.web.util.BDSessionHelper;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.security.bd.valueobject.BDMessagePreference;
import com.manulife.pension.service.security.bd.valueobject.BDUserMessageCenterPreferences;

/**
 * Utility class for BDW's Message center
 * 
 * @author guweigu
 * 
 */
public class BDMessageCenterUtils {
	private static final Logger log = Logger
			.getLogger(BDMessageCenterUtils.class);

	public static boolean isGlobalMessage(int templateId) {
		return templateId == BDMessageCenterConstants.GlobalMessageTemplateId;
	}
	public static boolean isFundCheckMessage(int templateId) {
		return templateId == BDMessageCenterConstants.FundCheckMessageId;
	}
	public static void sortMessageTemplate(
			List<BDMessageCenterMessageTemplate> templates) {
		Collections.sort(templates,
				new Comparator<BDMessageCenterMessageTemplate>() {
					/**
					 * Global Message always stays the last, the others sort by
					 * ascending short text
					 * 
					 * @param o1
					 * @param o2
					 * @return
					 */
					public int compare(BDMessageCenterMessageTemplate o1,
							BDMessageCenterMessageTemplate o2) {
						if (o1.getTemplateId() == BDMessageCenterConstants.GlobalMessageTemplateId) {
							return 1;
						}

						if (o2.getTemplateId() == BDMessageCenterConstants.GlobalMessageTemplateId) {
							return -1;
						}

						try {
							return o1.getShortText().compareTo(
									o2.getShortText());
						} catch (SystemException e) {
							log.error("Fail to get short text", e);
							return 0;
						}
					}
				});
	}

	/**
	 * Initialize the empty user preferences
	 * @return
	 * @throws SystemException
	 */
	public static BDUserMessageCenterPreferences getEmptyUserPreferences()
			throws SystemException {
		BDUserMessageCenterPreferences pref = new BDUserMessageCenterPreferences();
		pref.setReceiveSummaryEmail(false);
		List<BDMessageCenterMessageTemplate> templates = BDMessageCenterFacade
				.getInstance().getMessageTemplates();
		for (BDMessageCenterMessageTemplate t : templates) {
			BDMessagePreference p = new BDMessagePreference();
			p.setTemplateId(t.getTemplateId());
			p.setEmailNotification(false);
			if (t.getTemplateId() != BDMessageCenterConstants.GlobalMessageTemplateId) {
				p.setReceiveMessage(false);
			} else {
				p.setReceiveMessage(true);
			}
			pref.setMessagePreference(p);
		}
		return pref;
	}
	
	/**
	 * Check whether the current request is in Mimic mode
	 * @param request
	 * @return
	 */
	public static boolean isUnderMimic(HttpServletRequest request) {
		BDUserProfile userProfile = (BDUserProfile) BDSessionHelper
				.getUserProfile(request);
		return isUnderMimic(userProfile);		
	}
	
	/**
	 * Check whether the current userprofile is in Mimic mode
	 * @param request
	 * @return
	 */
	public static boolean isUnderMimic(BDUserProfile userProfile) {
		return userProfile != null && userProfile.isInMimic();		
	}
	
}
