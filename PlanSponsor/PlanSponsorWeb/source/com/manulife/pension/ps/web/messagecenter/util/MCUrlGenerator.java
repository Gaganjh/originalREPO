package com.manulife.pension.ps.web.messagecenter.util;

import com.manulife.pension.ps.web.messagecenter.model.MCAbstractReportModel;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.RecipientMessageDetail;

/**
 * URL generators for Message center
 * @author guweigu
 *
 */
abstract public class MCUrlGenerator {
	/**
	 * Returns the Information Url for a message
	 * 
	 * @param tab
	 * @param section
	 * @param message
	 * @return
	 */
	abstract public String getInfoUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message);

	/**
	 * Returns the act url for a message
	 * 
	 * @param tab
	 * @param section
	 * @param message
	 * @return
	 */
	abstract public String getActionUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message);

	/**
	 * Returns URL for remove (hide) the message
	 * @param tab
	 * @param section
	 * @param message
	 * @return
	 */
	abstract public String getRemoveMessageUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message);
	
	/**
	 * Returns URL for complete the message
	 * 
	 * @param tab
	 * @param section
	 * @param message
	 * @return
	 */
	abstract public String getCompleteMessageUrl(MessageCenterComponent tab,
			MessageCenterComponent section, RecipientMessageDetail message);

	/**
	 * Returns the URL for a tab
	 * @param tab
	 * @return
	 */
	final public String getTabUrl(MessageCenterComponent tab) {
		return getTabUrl(tab.getId());
	}

	/**
	 * Returns the URL for a tab
	 * @param tabId
	 * @return
	 */
	abstract public String getTabUrl(MessageCenterComponentId tabId);
	
	/**
	 * Returns the URL for a section in a detail tab
	 * @param tab
	 * @param section
	 * @return
	 */
	final public String getDetailSectionUrl(MessageCenterComponent tab,
			MessageCenterComponent section) {
		return getDetailSectionUrl(tab.getId(), section.getId());
	}

	/**
	 * Returns the URL for a section URL in a detail tab
	 * 
	 * @param tabId
	 * @param sectionId
	 * @return
	 */
	abstract public String getDetailSectionUrl(MessageCenterComponentId tabId,
			MessageCenterComponentId sectionId);

	/**
	 * Returns an URL for selecting a section
	 * @param tab
	 * @param section
	 * @return
	 */
	abstract public String getSelectSectionUrl(MessageCenterComponent tab,
			MessageCenterComponent section);

	/**
	 * Returns an URL for selecting a section
	 * @param tab
	 * @param section
	 * @return
	 */
	abstract public String getTabMessageUrl(MessageCenterComponent tab,
			String anchorName);
	
	/**
	 * Returns URL to expand a section
	 * 
	 * @param tab
	 * @param section
	 * @param expand
	 * @return
	 */
	abstract public String getExpandSectionUrl(MessageCenterComponent tab,
			MessageCenterComponent section, boolean expand);

	/**
	 * Returns an URL for showAll for a section
	 * @param tab
	 * @param section
	 * @param showAll
	 * @return
	 */
	abstract public String getShowAllSectionUrl(MessageCenterComponent tab,
			MessageCenterComponent section, boolean showAll);

	/**
	 * Returns an URL for sorting urgent message section
	 * @param model
	 * @param attrName
	 * @return
	 */
	abstract public String getUrgentMessageSortingUrl(MCAbstractReportModel model, String attrName);

	/**
	 * Returns an URL for shownAll for UrgentMessage section
	 * @param showAll
	 * @return
	 */
	abstract public String getUrgentMessageShowAllUrl(boolean showAll);

	/**
	 * Returns a sorting URL for a section
	 * 
	 * @param model
	 * @param section
	 * @param attrName
	 * @return
	 */
	abstract public String getSortingUrl(MCAbstractReportModel model, MessageCenterComponent section,
			String attrName);

	/**
	 * Returns a print friendly url for a tab
	 * @param tab
	 * @return
	 */
	abstract public String getTabPrintFriendlyUrl(MessageCenterComponent tab);
	
	/**
	 * Returns the anchor name for a section
	 * @param section
	 * @return
	 */
	abstract public String getSectionAnchorName(MessageCenterComponent section);
	
	/**
	 * Returns the base url for visit a message under an specified tab
	 */
	abstract public String getVisitMessageBaseUrl(MessageCenterComponent tab);
	
	/**
	 * Returns a URL for a section directly
	 */
	abstract public String getRedirectSectionUrl(int sectionId);
}
