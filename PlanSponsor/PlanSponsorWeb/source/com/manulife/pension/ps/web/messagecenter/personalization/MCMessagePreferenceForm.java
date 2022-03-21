package com.manulife.pension.ps.web.messagecenter.personalization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.messagecenter.model.MessageTemplateRepository;
import com.manulife.pension.ps.web.util.CloneableForm;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessagePreference;
import com.manulife.pension.service.message.valueobject.MessageTemplate.Priority;

public class MCMessagePreferenceForm extends AutoForm implements
		CloneableForm {

	private MCMessagePreferenceForm clonedForm;
	private Map<String, String> displayMap = new HashMap<String, String>();
	private Map<String, String> priorityMap = new HashMap<String, String>();
	private Map<String, String> defaultPriorityMap = new HashMap<String, String>();
	private Map<String, Boolean> priorityOverrideMap = new HashMap<String, Boolean>();
	private Map<String, Boolean> displayOverrideMap = new HashMap<String, Boolean>();
	private Map<String, String> expandMap = new HashMap<String, String>();

	private MessageCenterComponent messageCenterTop;
	private MessageTemplateRepository templateRepository;

	private int contractId;
	private long userProfileId;
	private String fromContract;
	private List<LabelValueBean> contracts;
	private String firstName;
	private String lastName;
	private String contractName;
	private boolean tpa;
	private boolean userCanEdit;
	
	
	

	

	public boolean isTpa() {
		return tpa;
	}

	public void setTpa(boolean tpa) {
		this.tpa = tpa;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	private boolean applyToAll = false;
	
	private static final String UrgentStr = Integer.toString(Priority.URGENT.getValue());
	private static final String ImportantStr = Integer.toString(Priority.IMPORTANT_INFORMATION.getValue());
	
	private static final String YesStr = "Y";
	private static final String NoStr = "N";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The name is in the format of <TemplateId>_<SectionId>
	 * 
	 * @param name
	 * @return
	 */
	private String getTemplateId(String name) {
		int index = name.indexOf('_');
		if (index >= 0) {
			return name.substring(0, index).trim();
		} else {
			return "";
		}
	}

	public void update(int contractId, List<MessagePreference> prefs) {
		this.contractId = contractId;
		displayMap.clear();
		priorityMap.clear();
		defaultPriorityMap.clear();
		for (MessagePreference p : prefs) {
			if (templateRepository.getMessageTemplateById(p
					.getMessageTemplateId()) != null) {
				String	 id = Integer.toString(p.getMessageTemplateId());
				String temp[] = id.split("_");
				id = temp[0];
				displayMap.put(id, p.isDisplayed() ? YesStr : NoStr);
				priorityMap.put(id, Integer.toString(p.getPriority().getValue()));
				defaultPriorityMap.put(id, Integer.toString(p.getDefaultPriority().getValue()));
				priorityOverrideMap.put(id, p.isPriorityOverrideAllowed());
				displayOverrideMap.put(id, p.isDisplayOverrideAllowed());
			}
		}
	}

	public List<MessagePreference> getMessagePreferences() {
		List<MessagePreference> prefs = new ArrayList<MessagePreference>(displayMap.size());
		for (String id : displayMap.keySet()) {
				String[] temp = id.split("_");
				id=temp[0];
			String display = displayMap.get(id);
			String priority = priorityMap.get(id);
			String defaultPriority = defaultPriorityMap.get(id);
			prefs.add(new MessagePreference(Integer.parseInt(id), contractId,
					UrgentStr.equals(priority) ? Priority.URGENT : ImportantStr.equals(priority) ? Priority.IMPORTANT_INFORMATION : Priority.NORMAL, 
					UrgentStr.equals(defaultPriority) ? Priority.URGENT : ImportantStr.equals(defaultPriority) ? Priority.IMPORTANT_INFORMATION : Priority.NORMAL, 
					YesStr.equals(display) ? true
					: false, false, false));
		}
		return prefs;
	}

	/**
	 * Get the display indicator for a message template
	 * 
	 * @param name
	 *            The name is in the format of <templateId>_<sectionId>, i.e.
	 *            12_2
	 * @return
	 */
	public String getDisplay(String name) {
		String value = displayMap.get(getTemplateId(name));
		
		
		return value == null ? "N" : value;
	}
	public String getPriority(String name) {
		return priorityMap.get(getTemplateId(name));
	}

	public Boolean getPriorityOverride(String name) {
		
		Boolean v = priorityOverrideMap.get(getTemplateId(name));
		// should not have null, be just for safe
		return v == null ? false : !v;
	}
	
	
	public Boolean getDisplayOverride(String name) {
		Boolean v = displayOverrideMap.get(getTemplateId(name));
		// should not have null, be just for safe
		return v == null ? false : !v;
	}

	/*public void setPriority(String name, String value) {

		
		priorityMap.put(getTemplateId(name), value);
	}*/
	
	/*public void setDefaultPriority(String name, String value) {
		
		defaultPriorityMap.put(getTemplateId(name), value);
	}*/

	public String getTemplateMsg(String messageTemplateId) {
		return "";
	}

	public String getExpand(String tabId) {
		String value = expandMap.get(tabId);
		return value == null ? "N" : value;
	}

	public void setExpand(String tabId, String value) {
		expandMap.put(tabId, value);
	}

	public void clear( HttpServletRequest request) {
	}

	public CloneableForm getClonedForm() {
		return clonedForm;
	}

	public void storeClonedForm() {
		clonedForm = new MCMessagePreferenceForm();
		clonedForm.setPriorityMap(new HashMap<String, String>(this.priorityMap));
		clonedForm.setDefaultPriorityMap(new HashMap<String, String>(this.defaultPriorityMap));
		clonedForm.setDisplayMap(new HashMap<String, String>(this.displayMap));
		
		
	}

	@Override
	public Object clone() {
		MCMessagePreferenceForm cloned = new MCMessagePreferenceForm();
		cloned.setPriorityMap(new HashMap<String, String>(this.priorityMap));
		cloned.setDefaultPriorityMap(new HashMap<String, String>(this.defaultPriorityMap));
		cloned.setDisplayMap(new HashMap<String, String>(this.displayMap));
		cloned.setTemplateRepository(this.templateRepository);
		return cloned;
	}

	
	public Map<String, Boolean> getPriorityOverrideMap() {
		
		return priorityOverrideMap;
	}

	public void setPriorityOverrideMap(Map<String, Boolean> priorityOverrideMap) {
		this.priorityOverrideMap = priorityOverrideMap;
	}

	public Map<String, Boolean> getDisplayOverrideMap() {
		
		return displayOverrideMap;
	}

	public void setDisplayOverrideMap(Map<String, Boolean> displayOverrideMap) {
		this.displayOverrideMap = displayOverrideMap;
	}

	public Map<String, String> getDefaultPriorityMap() {
		return defaultPriorityMap;
	}


	public Map<String, String> getPriorityMap() {
		
		return priorityMap;
	}

	public void setPriorityMap(Map<String, String> priorityMap) {
		this.priorityMap = priorityMap;
	}
	
	public void setDefaultPriorityMap(Map<String, String> defaultPriorityMap) {
		this.defaultPriorityMap = defaultPriorityMap;
	}

	public MessageCenterComponent getMessageCenterTop() {
		return messageCenterTop;
	}

	public void setMessageCenterTop(MessageCenterComponent messageCenterTop) {
		this.messageCenterTop = messageCenterTop;
	}

	public MessageTemplateRepository getTemplateRepository() {
		return templateRepository;
	}

	public void setTemplateRepository(
			MessageTemplateRepository templateRepository) {
		this.templateRepository = templateRepository;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	public boolean tabContainsTemplate(MessageCenterComponent tab) {
		for (MessageCenterComponent section : tab.getChildren()) {
			if (sectionContainsTemplate(section)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, String> getDisplayMap() {
		
		return displayMap;
	}

	public void setDisplayMap(Map<String, String> displayMap) {
		this.displayMap = displayMap;
	}

	public boolean sectionContainsTemplate(MessageCenterComponent section) {
		DisplayableMessageTemplate[] templates = templateRepository
				.getMessageTemplates(section);
		return templates != null && templates.length > 0;
	}

	@Override
	public void reset( HttpServletRequest request) {
		super.reset( request);
		expandMap.clear();
		fromContract = null;
		applyToAll = false;
	}

	public String getFromContract() {
		return fromContract;
	}

	public void setFromContract(String fromContract) {
		this.fromContract = StringUtils.trimToEmpty(fromContract);
	}

	public List<LabelValueBean> getContracts() {
		return contracts;
	}

	public void setContracts(List<LabelValueBean> contracts) {
		this.contracts = contracts;
	}

	public void copyPreferencesFrom(List<MessagePreference> prefs) {
		for (MessagePreference p : prefs) {
			if (templateRepository.getMessageTemplateById(p
					.getMessageTemplateId()) != null) {
				String id = Integer.toString(p.getMessageTemplateId());
				Boolean allowDisplayOverride = displayOverrideMap.get(id);
				Boolean allowPriorityOverride = priorityOverrideMap.get(id);
				if (allowDisplayOverride != null
						&& allowDisplayOverride.booleanValue()) {
					displayMap.put(id, p.isDisplayed() ? YesStr : NoStr);
				}
				if (allowPriorityOverride != null
						&& allowPriorityOverride.booleanValue()) {
					priorityMap.put(id, Integer.toString(p.getPriority()
							.getValue()));
				}
			}
		}
	}

	

	public boolean isApplyToAll() {
		return applyToAll;
	}

	public void setApplyToAll(boolean applyToAll) {
		this.applyToAll = applyToAll;
	}
	public long getUserProfileId() {
		return userProfileId;
	}

	public void setUserProfileId(long userProfileId) {
		this.userProfileId = userProfileId;
	}	

	public int getContractId() {
		return contractId;
	}

	public void setContractId(int contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the userCanEdit
	 */
	public boolean isUserCanEdit() {
		return userCanEdit;
	}

	/**
	 * @param userCanEdit the userCanEdit to set
	 */
	public void setUserCanEdit(boolean userCanEdit) {
		this.userCanEdit = userCanEdit;
	}
	
}
