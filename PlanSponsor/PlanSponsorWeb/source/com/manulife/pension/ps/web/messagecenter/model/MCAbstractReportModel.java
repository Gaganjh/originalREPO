package com.manulife.pension.ps.web.messagecenter.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCUrlGenerator;
import com.manulife.pension.ps.web.messagecenter.util.MCUrlGeneratorFactory;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageContainer;
import com.manulife.pension.service.message.valueobject.MessageContainerSummary;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent.Type;

/**
 * The report model contains all the model information for displaying a message
 * center page
 * 
 * @author guweigu
 * 
 */
abstract public class MCAbstractReportModel implements Serializable,
		MCConstants {
	private static final long serialVersionUID = 1L;
	protected static final Map<String, Boolean> DefaultSortOrder = new HashMap<String, Boolean>();

	static {
		DefaultSortOrder.put(ContractIDAttrName, true);
		DefaultSortOrder.put(ContractNameAttrName, true);
		DefaultSortOrder.put(PriorityAttrName, false);
		DefaultSortOrder.put(PostedTsAttrName, false);
		DefaultSortOrder.put(ShortTextAttrName, true);
	}

	/**
	 * The preference for display a MessageCenter page
	 */
	private MCPreference preference;

	/**
	 * The static MessageCenterComponent tree from the TOP.
	 */
	private MessageCenterComponent top;

	private boolean navigatable = true;

	private boolean printFriendly = false;

	private boolean multiContract = false;

	private boolean exceedLimit = false;

	/**
	 * The MessageContainersummary Map for each MC tab
	 */
	protected Map<MessageCenterComponentId, MessageContainerSummary> tabSummaryMap;

	/**
	 * Returns the current selected Tab
	 * 
	 * @return
	 */
	abstract public MessageCenterComponent getSelectedTab();

	/**
	 * Returns unsorted messages for a section
	 * 
	 * @param section
	 * @return
	 */
	abstract public RecipientMessageDetail[] getMessages(
			MessageCenterComponent section);

	/**
	 * Returns the MessageContainer for a section
	 * 
	 * @param section
	 * @return
	 */
	abstract public MessageContainer getSectionContainer(
			MessageCenterComponent section);

	/**
	 * 
	 * @param top
	 *            can not be null
	 */
	protected MCAbstractReportModel(MessageCenterComponent top,
			MCPreference preference, boolean multiContract) {
		if (top == null) {
			throw new RuntimeException(
					"Can not have null Top MessageCenterComponent");
		}
		if (preference == null) {
			throw new RuntimeException("Can not have null preference");
		}
		this.top = top;
		this.preference = preference;
		this.multiContract = multiContract;
	}

	/**
	 * Retrieves the Top component of MessageCenter
	 * 
	 * @return
	 */
	final public MessageCenterComponent getTop() {
		return top;
	}

	/**
	 * Returns the MCPreference for the model
	 * 
	 * @return
	 */
	final public MCPreference getPreference() {
		return preference;
	}

	/**
	 * Returns the summary tab
	 * 
	 * @return
	 */
	final public MessageCenterComponent getSummaryTab() {
		return SummaryTab;
	}

	/**
	 * Set the tabs summary information
	 * 
	 * @param tabsSummaries
	 *            A set of MessageContainerSummary for each MessageCenter tab
	 */
	public void setTabsSummary(Set<MessageContainerSummary> tabsSummaries) {
		tabSummaryMap = new HashMap<MessageCenterComponentId, MessageContainerSummary>();
		for (MessageContainerSummary s : tabsSummaries) {
			tabSummaryMap.put(s.getId(), s);
		}
	}

	/**
	 * Returns the MessageContainerSummary for the <code>tab</code>
	 * 
	 * @param tab
	 * @return
	 */
	protected MessageContainerSummary getTabSummary(MessageCenterComponent tab) {
		return tabSummaryMap == null ? null : tabSummaryMap.get(tab.getId());
	}

	/**
	 * Returns if the specified <code>tab</code> is the selected Tab
	 * 
	 * @param tab
	 * @return
	 */
	final public Boolean isSelectedTab(MessageCenterComponent tab) {
		return getSelectedTab() != null
				&& tab.getId().getType().equals(Type.TAB)
				&& tab.getId().getValue().equals(
						getSelectedTab().getId().getValue());
	}

	/**
	 * Returns whether the whole model is navigatable
	 * 
	 * @return
	 */
	final public boolean isNavigatable() {
		return navigatable;
	}

	/**
	 * Set the navigatable
	 * 
	 * @param value
	 */
	final public void setNavigatable(boolean value) {
		this.navigatable = value;
	}

	/**
	 * Get a total message count for a tab
	 * 
	 * @param tab
	 * @return
	 */
	final public int getTabMessageCount(MessageCenterComponent tab) {
		MessageContainerSummary s = getTabSummary(tab);
		return s == null ? 0 : s.getMessageCount();
	}

	/**
	 * Get Tab's display name (including the message count)
	 * 
	 * @param tab
	 * @return
	 */
	public String getTabDisplayCount(MessageCenterComponent tab) {
		if (tab.equals(SummaryTab)) {
			return "";
		} else {
			return " (" + getTabMessageCount(tab) + ")";
		}
	}

	final public MCUrlGenerator getUrlGenerator() {
		return MCUrlGeneratorFactory.getInstance();
	}

	final public Boolean getDefaultSortOrder(String attrName) {
		return DefaultSortOrder.get(attrName);
	}

	final public boolean isPrintFriendly() {
		return printFriendly;
	}

	final public void setPrintFriendly(boolean printFriendly) {
		this.printFriendly = printFriendly;
	}

	/**
	 * Returns the section's display name in the format of Name
	 * (messagecount/total count)
	 * 
	 * @param section
	 * @return
	 */
	protected String getSectionContainerCount(MessageCenterComponent section) {
		MessageContainer container = getSectionContainer(section);
		if (container == null) {
			return "";
		}
		int totalCount = container.getTotalMessageCount();
		int count = container.getMessageCount();
		return " (" + count + " of " + totalCount + ")";
	}

	protected boolean showMoreForSectionContainer(MCSectionPreference pref,
			MessageContainer container) {
		if (pref.isShowAll()) {
			return false;
		} else {
			return container.hasMoreMessages();
		}
	}

	protected boolean showLessForSectionContainer(MCSectionPreference pref,
			MessageContainer container) {
		return pref.isShowAll()
				&& container.getMessageCount() > pref.getDefaultMessageCount();
	}

	public boolean isMultiContract() {
		return multiContract;
	}
	
	public boolean isExceedLimit() {
		return exceedLimit;
	}

	protected void setExceedLimit(boolean exceedLimit) {
		this.exceedLimit = exceedLimit;
	}
}