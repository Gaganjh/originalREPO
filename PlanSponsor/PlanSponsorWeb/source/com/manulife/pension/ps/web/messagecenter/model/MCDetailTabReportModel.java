package com.manulife.pension.ps.web.messagecenter.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;

/**
 * The model for displaying MessageCenter's non-summary tab
 * 
 * @author guweigu
 * 
 */
public class MCDetailTabReportModel extends MCAbstractReportModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The selected tab for the message center
	 */
	private MessageCenterComponent selectedTab;
	/**
	 * The message detail data for each section in the displayed tab
	 */
	private Map<MessageCenterComponentId, MessageContainer> sectionsData;

	private List<MessageCenterComponent> displayableSections;


	/**
	 * Can not have Null selectedTab
	 * 
	 * @param top
	 * @param selectedTab
	 */
	public MCDetailTabReportModel(MessageCenterComponent top,
			MCPreference preference, MessageCenterComponent selectedTab,
			Collection<MessageContainer> sectionDetails, boolean multiContract) {
		super(top, preference, multiContract);

		if (selectedTab == null) {
			throw new RuntimeException("Can not have null for selected tab");
		}
		this.selectedTab = selectedTab;
		displayableSections = new ArrayList<MessageCenterComponent>(selectedTab
				.getChildrenSize());
		setSectionDetails(sectionDetails);
	}

	@Override
	public MessageCenterComponent getSelectedTab() {
		return selectedTab;
	}

	public int getSectionMessageCount(MessageCenterComponent section) {
		MessageContainerSummary summary = getSectionContainer(section);
		return summary == null ? 0 : summary.getMessageCount();
	}

	public int getSectionTotalMessageCount(MessageCenterComponent section) {
		MessageContainer contaier = getSectionContainer(section);
		return contaier == null ? 0 : contaier.getTotalMessageCount();
	}

	/**
	 * Returns the messages for a section
	 */
	public RecipientMessageDetail[] getMessages(
			MessageCenterComponent section) {
		MessageContainer container = getSectionContainer(section);
		RecipientMessageDetail[] messages = (container == null ? null
				: container.getMessages());
		return messages == null ? ZeroMessages : messages;
	}

	/**
	 * Set the MessageCenterContainer for each sections under the tab
	 * 
	 * @param sectionDetails
	 */
	private void setSectionDetails(Collection<MessageContainer> sectionDetails) {
		this.sectionsData = new HashMap<MessageCenterComponentId, MessageContainer>();

		// no message at all
		if (sectionDetails == null) {
			return;
		}
		for (MessageContainer messageContainer : sectionDetails) {
			sectionsData.put(messageContainer.getId(), messageContainer);
			if (messageContainer.getTotalMessageCount() > MCEnvironment
					.getMaximumMessageCount()) {
				setExceedLimit(true);
			}
		}
		displayableSections.clear();
		for (MessageCenterComponent section : selectedTab.getChildren()) {
			MessageContainer c = sectionsData.get(section.getId());
			if (c != null && c.getMessageCount() > 0) {
				displayableSections.add(section);
			}
		}
	}

	public List<MessageCenterComponent> getDisplayableSections() {
		return displayableSections;
	}

	/**
	 * Convenient method to return a MessageContainer for a section.
	 * 
	 * @param section
	 * @return
	 */
	public MessageContainer getSectionContainer(MessageCenterComponent section) {
		return sectionsData == null ? null : sectionsData.get(section.getId());
	}

	/**
	 * Check whether a section is expanded. When there is only one section is
	 * display-able, the section should be expanded otherwise it is determined by
	 * the preference
	 * 
	 * @param section
	 * @return
	 */
	public boolean isSectionExpand(MessageCenterComponent section) {
		return getPreference().getSectionPreference(section).isExpand();
	}
	
	/**
	 * Returns if show more for a section (when the message count is more than
	 * default displayed)
	 * 
	 * @param section
	 * @return
	 */
	final public boolean showMoreForSection(MessageCenterComponent section) {
		MCSectionPreference pref = getPreference()
				.getSectionPreference(section);
		return showMoreForSectionContainer(pref, getSectionContainer(section));
	}

	/**
	 * Returns if show less for a section (when the message count is more than
	 * default displayed)
	 * 
	 * @param section
	 * @return
	 */
	final public boolean showLessForSection(MessageCenterComponent section) {
		MCSectionPreference pref = getPreference()
		.getSectionPreference(section);
		return showLessForSectionContainer(pref, getSectionContainer(section));
	}
	
	final public String getSectionCount(MessageCenterComponent section) {
		return getSectionContainerCount(section);
	}
}
