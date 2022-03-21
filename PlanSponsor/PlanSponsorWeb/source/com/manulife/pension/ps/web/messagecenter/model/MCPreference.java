package com.manulife.pension.ps.web.messagecenter.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.ps.web.messagecenter.MCConstants;
import com.manulife.pension.ps.web.messagecenter.util.MCEnvironment;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent.Type;

/**
 * The preference for displaying Message Center. This class will be the central
 * place to determine what the final display behavior will be. The page will
 * combine the Model and the MCPreference to show a page. The page will have
 * same logic for all different scenarios (i.e. print-friendly), it is
 * preference driven.
 * 
 * The action will base on different scenario to populate the preference before
 * forwarding to the page.
 * 
 * @author guweigu
 * 
 */
public class MCPreference implements Serializable, MCConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MCSectionPreference defaultSectionPreference;
	private MCSectionPreference defaultUrgentSectionPreference;

	private Map<MessageCenterComponentId, MCSectionPreference> sections;

	private MessageCenterComponentId selectedTabId;
	private MessageCenterComponentId selectedSectionId;

	// default constructor
	// for subclass
	protected MCPreference() {
		this.defaultUrgentSectionPreference = new MCSectionPreference(true,
				false, PostedTsAttrName, false, MCEnvironment.getUrgentMessageCount());
		this.defaultSectionPreference = new MCSectionPreference();
		sections = new HashMap<MessageCenterComponentId, MCSectionPreference>();
	}

	public MCPreference(MCPreference pref) {
	}

	/**
	 * Constructor for global mc preference
	 * 
	 * @param
	 */
	public MCPreference(MCSectionPreference defaultUrgentSectionPreference,
			MCSectionPreference defaultSectionPreference) {
		this.defaultUrgentSectionPreference = defaultUrgentSectionPreference;
		this.defaultSectionPreference = defaultSectionPreference;
		sections = new HashMap<MessageCenterComponentId, MCSectionPreference>();
	}

	public void reset() {
		sections.clear();
		clearSelection();
	}

	/**
	 * Clear the selected tab and section
	 */
	public void clearSelection() {
		selectedSectionId = null;
		selectedTabId = null;
	}

	/**
	 * Returns the selected tab id
	 * 
	 * @return
	 */
	public MessageCenterComponentId getSelectedTabId() {
		return selectedTabId;
	}

	/**
	 * Sets the selected Tab id
	 * 
	 * @param selectedTabId
	 */
	public void setSelectedTabId(MessageCenterComponentId selectedTabId) {
		this.selectedTabId = selectedTabId;
	}

	/**
	 * Returns the selected section id
	 * 
	 * @return
	 */
	public MessageCenterComponentId getSelectedSectionId() {
		return selectedSectionId;
	}

	/**
	 * Sets the selected section id
	 * 
	 * @param selectedSectionId
	 */
	public void setSelectedSectionId(MessageCenterComponentId selectedSectionId) {
		this.selectedSectionId = selectedSectionId;
	}

	/**
	 * Get the SectionPreference for a <code>section</code>
	 * 
	 * @param section
	 * @return
	 */
	public MCSectionPreference getSectionPreference(
			MessageCenterComponent section) {
		MCSectionPreference pref = sections.get(section.getId());
		if (pref == null) {
			pref = (UrgentMessageSection.equals(section) ? defaultUrgentSectionPreference
					: defaultSectionPreference);
		}
		return pref;
	}

	/**
	 * Update a SectionPreferene
	 * 
	 * @param section
	 * @param pref
	 */
	public void updateSectionPreference(MessageCenterComponent section,
			MCSectionPreference pref) {
		sections.put(section.getId(), pref);
	}

	/**
	 * collapse all sections but the specified one
	 * 
	 * @param tab
	 *            The tab that all sections but one to be collapsed
	 * @param section
	 *            The section to be expanded
	 */
	public void collapseAllSectionButOne(MessageCenterComponent tab,
			MessageCenterComponent section) {
		for (MessageCenterComponent s : tab.getChildren()) {
			MCSectionPreference curPref = getSectionPreference(s);
			boolean expand = (s.equals(section));
			MCSectionPreference newPref = new MCSectionPreference(expand,
					curPref.isShowAll(), curPref.getPrimarySortAttribute(),
					curPref.isAscending(), curPref.getDefaultMessageCount());
			updateSectionPreference(s, newPref);
		}
	}

	/**
	 * Expand or Collapse one specified section
	 * 
	 * @param section
	 *            The section to be expanded/collapsed
	 */
	public void expandSection(MessageCenterComponent section, boolean expand) {
		if (section.getId().getType().compareTo(Type.SECTION) == 0) {
			MCSectionPreference curPref = getSectionPreference(section);
			MCSectionPreference newPref = new MCSectionPreference(expand,
					curPref.isShowAll(), curPref.getPrimarySortAttribute(),
					curPref.isAscending(), curPref.getDefaultMessageCount());
			updateSectionPreference(section, newPref);
		}
	}

	/**
	 * Set the section sort order
	 * 
	 * @param selectedSection
	 * @param key
	 * @param ascending
	 */
	public void setSectionSort(MessageCenterComponent section, String key,
			boolean ascending) {
		if (section.getId().getType().compareTo(Type.SECTION) == 0) {
			MCSectionPreference curPref = getSectionPreference(section);
			MCSectionPreference newPref = new MCSectionPreference(curPref
					.isExpand(), curPref.isShowAll(), key, ascending, curPref
					.getDefaultMessageCount());
			updateSectionPreference(section, newPref);
		}
	}

	/**
	 * Set the section showAll
	 * 
	 * 
	 * @param selectedSection
	 * @param key
	 * @param ascending
	 */
	public void setSectionShowAll(MessageCenterComponent section,
			boolean showAll) {
		if (section.getId().getType().compareTo(Type.SECTION) == 0) {
			MCSectionPreference curPref = getSectionPreference(section);
			MCSectionPreference newPref = new MCSectionPreference(curPref
					.isExpand(), showAll, curPref.getPrimarySortAttribute(),
					curPref.isAscending(), curPref.getDefaultMessageCount());
			updateSectionPreference(section, newPref);
		}
	}
}
