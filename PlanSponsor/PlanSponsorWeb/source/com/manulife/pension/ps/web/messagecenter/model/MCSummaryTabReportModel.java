package com.manulife.pension.ps.web.messagecenter.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.service.message.valueobject.MessageCategory;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;

/**
 * The model for displaying the summary tab of MessageCenter
 * 
 * @author guweigu
 * 
 */
public class MCSummaryTabReportModel extends MCAbstractReportModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The summary information for section
	 */
	private Map<MessageCenterComponentId, MessageContainerSummary> sectionSummaryMap;

	/**
	 * All the urgent messages
	 */

	private MessageContainer urgentMessageContainer;

	public MCSummaryTabReportModel(MessageCenterComponent top,
			MCPreference preference, boolean multiContract) {
		super(top, preference, multiContract);
		sectionSummaryMap = new HashMap<MessageCenterComponentId, MessageContainerSummary>();
		tabSummaryMap = new HashMap<MessageCenterComponentId, MessageContainerSummary>();
	}

	public MCSummaryTabReportModel(MessageCenterComponent top,
			MCPreference preference,
			Set<MessageContainerSummary> sectionSummaries,
			MessageContainer urgentMessageContainer, boolean multiContract) {
		this(top, preference, multiContract);
		setSectionSummaries(sectionSummaries, top);
		setUrgentMessages(urgentMessageContainer);
	}

	/**
	 * SummaryReportModel's selected tab is always the summary tab
	 */
	@Override
	public MessageCenterComponent getSelectedTab() {
		return getSummaryTab();
	}

	/**
	 * Returns a message count for a section and category
	 * 
	 * @param c
	 * @param category
	 * @return
	 */
	public int getSectionMessageCount(MessageCenterComponent c,
			MessageCategory category) {
		int count = 0;
		MessageContainerSummary s = getSectionSummary(c);
		count = (s == null ? 0 : s.getMessageCount(category));
		return count;
	}

	public int getSectionTotalMessageCount(MessageCenterComponent c) {
		int count = 0;
		MessageContainerSummary s = getSectionSummary(c);
		count = (s == null ? 0 : s.getMessageCount());
		return count;
	}

	/**
	 * Returns total message count for a section
	 */
	public int getUrgentSectionMessageCount() {
		return urgentMessageContainer == null ? 0 : urgentMessageContainer
				.getMessageCount();
	}

	public int getUrgentSectionTotalMessageCount() {
		return urgentMessageContainer == null ? 0 : urgentMessageContainer
				.getTotalMessageCount();
	}

	/**
	 * Sets the section summaries set. It internally converts it to a map, key
	 * is the section's id, the value the MessageContainerSummary. It also
	 * creates the map of MessageContainerSummary for each tab.
	 * 
	 * @param sectionSummaries
	 * @param top
	 */
	public void setSectionSummaries(
			Set<MessageContainerSummary> sectionSummaries,
			MessageCenterComponent top) {
		sectionSummaryMap.clear();
		tabSummaryMap.clear();
		if (sectionSummaries == null) {
			return;
		}
		for (MessageContainerSummary s : sectionSummaries) {
			sectionSummaryMap.put(s.getId(), s);
		}
		tabSummaryMap = new HashMap<MessageCenterComponentId, MessageContainerSummary>();
		MessageCenterComponent[] tabs = top.getChildren();
		for (MessageCenterComponent tab : tabs) {
			MessageCenterComponent[] sections = tab.getChildren();
			int urgentCount = 0;
			int actionCount = 0;
			int fyiCount = 0;
			for (MessageCenterComponent section : sections) {
				MessageContainerSummary summary = sectionSummaryMap.get(section
						.getId());
				if (summary != null) {
					urgentCount += summary
							.getMessageCount(MessageCategory.URGENT);
					actionCount += summary
							.getMessageCount(MessageCategory.ACTION);
					fyiCount += summary.getMessageCount(MessageCategory.FYI);
				}
			}
			tabSummaryMap.put(tab.getId(), MCUtils.getSummary(tab.getId(),
					urgentCount, actionCount, fyiCount));
		}
	}

	/**
	 * Returns a MessageContainerSummary for a section
	 * 
	 * @param section
	 * @return
	 */
	public MessageContainerSummary getSectionSummary(
			MessageCenterComponent section) {
		return sectionSummaryMap == null ? null : sectionSummaryMap.get(section
				.getId());
	}

	/**
	 * set an array of urgent messages
	 * 
	 * @return
	 */
	public void setUrgentMessages(MessageContainer container) {
		urgentMessageContainer = container;
		if (container != null && container.getTotalMessageCount() > MCEnvironment
				.getMaximumMessageCount()) {
			setExceedLimit(true);
		}
	}

	/**
	 * Can only return urgent section's messages
	 */
	@Override
	public RecipientMessageDetail[] getMessages(MessageCenterComponent section) {
		if (UrgentMessageSection.equals(section)
				&& urgentMessageContainer != null) {
			RecipientMessageDetail[] messages = urgentMessageContainer
					.getMessages();
			return messages == null ? ZeroMessages : messages;
		} else {
			return ZeroMessages;
		}
	}

	@Override
	public MessageContainer getSectionContainer(MessageCenterComponent section) {
		if (UrgentMessageSection.equals(section)) {
			return urgentMessageContainer;
		} else {
			return null;
		}
	}

	/**
	 * Whether to show more for urgent message section
	 * 
	 * @return
	 */
	final public boolean showMoreForUrgentSection() {
		return super.showMoreForSectionContainer(getPreference()
				.getSectionPreference(UrgentMessageSection),
				urgentMessageContainer);
	}

	/**
	 * Whether to show less for urgent message section
	 * 
	 * @return
	 */
	final public boolean showLessForUrgentSection() {
		return super.showLessForSectionContainer(getPreference()
				.getSectionPreference(UrgentMessageSection),
				urgentMessageContainer);
	}

	final public String getUrgentSectionCount() {
		return super.getSectionContainerCount(UrgentMessageSection);
	}
}
