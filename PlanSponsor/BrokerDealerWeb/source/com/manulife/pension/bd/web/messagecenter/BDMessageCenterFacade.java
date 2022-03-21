package com.manulife.pension.bd.web.messagecenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.bd.web.userprofile.BDUserProfile;
import com.manulife.pension.delegate.MessageServiceDelegate;
import com.manulife.pension.delegate.SecurityServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.message.report.valueobject.BDMessageCenterSummary;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessagePreference;
import com.manulife.pension.service.message.valueobject.MessageTemplateSectionRel;
import com.manulife.pension.service.message.valueobject.EmailPreference.NormalMessageFrequency;
import com.manulife.pension.service.message.valueobject.MessageRecipient.RecipientStatus;
import com.manulife.pension.service.message.valueobject.MessageTemplate.Priority;
import com.manulife.pension.service.security.bd.valueobject.BDMessagePreference;
import com.manulife.pension.service.security.bd.valueobject.BDUserMessageCenterPreferences;
import com.manulife.pension.service.security.valueobject.UserPreferenceKeys;
import com.manulife.pension.service.security.valueobject.UserPreferences;

/**
 * Facade for BDW's message center. Interfacing the MessageService
 * 
 * @author guweigu
 * 
 */
public class BDMessageCenterFacade {
	private static final Logger log = Logger
			.getLogger(BDMessageCenterFacade.class);

	private static BDMessageCenterFacade instance = new BDMessageCenterFacade();
	private MessageCenterComponent bdMCSection = null;
	private List<BDMessageCenterMessageTemplate> templates = null;

	private MessageServiceDelegate delegate;

	public static BDMessageCenterFacade getInstance() {
		return instance;
	}

	private BDMessageCenterFacade() {
		delegate = MessageServiceDelegate
				.getInstance(BDMessageCenterConstants.APPLICATION_ID);
	}

	/**
	 * Get the Message Center preferences for a BDUser
	 * 
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	public BDUserMessageCenterPreferences getMCPreferences(long userProfileId)
			throws SystemException {
		List<MessagePreference> mps = delegate.getBDMessagePreferences(
				userProfileId, getBDMCSection());
		BDUserMessageCenterPreferences preferences = new BDUserMessageCenterPreferences();
		for (MessagePreference mp : mps) {
			preferences.setMessagePreference(convert(mp));
		}
		UserPreferences ups = SecurityServiceDelegate.getInstance()
				.getUserPreferences(userProfileId);
		String normal = ups.get(UserPreferenceKeys.NORMAL_MESSAGE_PREF,
				NormalMessageFrequency.Never.getValue());
		if (StringUtils.equals(normal, NormalMessageFrequency.Never.getValue())) {
			preferences.setReceiveSummaryEmail(false);
		} else {
			preferences.setReceiveSummaryEmail(true);
		}
		return preferences;
	}

	/**
	 * Get the one and only MC Section for BDW site
	 * 
	 * @return
	 * @throws SystemException
	 */
	public synchronized MessageCenterComponent getBDMCSection()
			throws SystemException {
		if (bdMCSection == null) {
			MessageCenterComponent top = delegate
					.getMessageCenterComponentTree();
			if (top.getChildrenSize() == 1) {
				MessageCenterComponent tab = top.getChildren()[0];
				if (tab != null && tab.getChildrenSize() == 1) {
					bdMCSection = tab.getChildren()[0];
				}
			}
		}
		if (bdMCSection == null) {
			log.error("Can not find the message center section for BDW");
			throw new SystemException(
					"Can not find message center section for BDW");
		}
		return bdMCSection;
	}

	/**
	 * Get all of the system defined message templates for BDW
	 * 
	 * @return
	 * @throws SystemException
	 */
	public synchronized List<BDMessageCenterMessageTemplate> getMessageTemplates()
			throws SystemException {
		if (templates == null) {
			List<MessageTemplateSectionRel> sectionTemplateRels = delegate
					.getMessageTemplateSectionRel(BDMessageCenterConstants.APPLICATION_ID);
			Integer sectionId = (Integer) getBDMCSection().getId().getValue();
			templates = new ArrayList<BDMessageCenterMessageTemplate>();
			for (MessageTemplateSectionRel t : sectionTemplateRels) {
				if (sectionId == t.getSectionId()) {
					templates.add(new BDMessageCenterMessageTemplate(t
							.getTemplateId(), t.getContentKey(), t.getType()));
				}
			}
			BDMessageCenterUtils.sortMessageTemplate(templates);
			templates = Collections.unmodifiableList(templates);
		}
		return templates;
	}

	/**
	 * Returns the message center summary for a BD user
	 * 
	 * @param user
	 * @return
	 * @throws SystemException
	 */
	public BDMessageCenterSummary getMesageCenterSummary(BDUserProfile user)
			throws SystemException {
		long profileId = user.getBDPrincipal().getProfileId();
		BDMessageCenterSummary summary = delegate.getBDMessageCenterSummary(
				profileId, getBDMCSection());
		return summary;
	}

	private BDMessagePreference convert(MessagePreference mp) {
		BDMessagePreference bmp = new BDMessagePreference();
		bmp.setTemplateId(mp.getMessageTemplateId());
		bmp
				.setEmailNotification(mp.getPriority().compareTo(
						Priority.URGENT) == 0);
		bmp.setReceiveMessage(mp.isDisplayed());
		return bmp;
	}

	/**
	 * Delete the message (Set the recipient message status = 'HI' as hidden')
	 * 
	 * @param userProfile
	 * @param messageIds
	 * @throws SystemException
	 */
	public void deleteMessages(BDUserProfile userProfile, int[] messageIds)
			throws SystemException {
		delegate.hideMessages(userProfile.getBDPrincipal().getProfileId(),
				messageIds);
	}

	/**
	 * Set the Recipient message status = 'VI' as visited
	 * 
	 * @param userProfile
	 * @param messageId
	 * @throws SystemException
	 */
	public void visitMessage(BDUserProfile userProfile, Integer messageId) throws SystemException {
		delegate.updateRecipientMessageStatus(userProfile.getBDPrincipal()
				.getProfileId(), messageId, RecipientStatus.VISITED);

	}
}
