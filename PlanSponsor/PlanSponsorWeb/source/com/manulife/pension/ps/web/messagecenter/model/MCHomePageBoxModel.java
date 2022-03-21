package com.manulife.pension.ps.web.messagecenter.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUrlGenerator;
import com.manulife.pension.ps.web.messagecenter.util.MCUrlGeneratorFactory;
import com.manulife.pension.service.message.valueobject.MessageCategory;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerImpl;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;
import com.manulife.pension.service.report.valueobject.ReportSort;
import com.manulife.pension.service.report.valueobject.ReportSortList;

public class MCHomePageBoxModel implements Serializable, MCConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6148764694392954825L;

	private MessageCenterComponent top;
	private Map<MessageCenterComponentId, MessageContainerSummary> tabSummaryMap;
	private MessageContainer urgentMessageContainer;
	private static final MessageContainer EmptyContainer = new MessageContainerImpl(
			UrgentMessageSectionId, 0);

	public static ReportSortList SortList = new ReportSortList();
	static {
		SortList
				.add(new ReportSort(PostedTsAttrName, ReportSort.DESC_DIRECTION));
		SortList
				.add(new ReportSort(ShortTextAttrName, ReportSort.ASC_DIRECTION));
	}

	public MCHomePageBoxModel(MessageCenterComponent top,
			Collection<MessageContainerSummary> tabsSummaries,
			MessageContainer urgentMessages) {
		this.top = top;
		tabSummaryMap = new HashMap<MessageCenterComponentId, MessageContainerSummary>();
		if (tabsSummaries != null) {
			for (MessageContainerSummary s : tabsSummaries) {
				tabSummaryMap.put(s.getId(), s);
			}
		}
		urgentMessageContainer = (urgentMessages == null ? EmptyContainer
				: urgentMessages);
	}

	public MessageCenterComponent getTop() {
		return top;
	}

	public int getTabMessageCount(MessageCenterComponent tab,
			MessageCategory category) {
		MessageContainerSummary c = tabSummaryMap.get(tab.getId());
		return c == null ? 0 : c.getMessageCount(category);
	}

	public int getTabMessageCount(MessageCenterComponent tab) {
		MessageContainerSummary c = tabSummaryMap.get(tab.getId());
		return c == null ? 0 : c.getMessageCount();
	}

	public RecipientMessageDetail[] getUrgentMessages() {
		return urgentMessageContainer.getMessages();
	}

	public int getTotalUrgentMessageCount() {
		return urgentMessageContainer.getTotalMessageCount();
	}
	public int getTotalMessageCount() {
		int returnValue = 0;
		for ( MessageContainerSummary container : tabSummaryMap.values() ) {
			returnValue += container.getMessageCount();
		}
		return returnValue;
		
	}
	
	final public MCUrlGenerator getUrlGenerator() {
		return MCUrlGeneratorFactory.getInstance();
	}

}
