package com.manulife.pension.ps.web.messagecenter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.manulife.pension.ps.web.messagecenter.personalization.DisplayableMessageTemplate;
import com.manulife.pension.service.message.valueobject.MessageCenterComponent;
import com.manulife.pension.service.message.valueobject.MessageCenterComponentId;

public class MessageTemplateRepository implements Serializable {
	private static final DisplayableMessageTemplate[] NoTemplates = new DisplayableMessageTemplate[0];

	private Map<MessageCenterComponentId, DisplayableMessageTemplate[]> sectionMessageTemplateMap;
	private Map<Integer, DisplayableMessageTemplate> idMessageTemplateMap;

	public MessageTemplateRepository(
			Map<MessageCenterComponentId, DisplayableMessageTemplate[]> templateMap) {
		sectionMessageTemplateMap = new HashMap<MessageCenterComponentId, DisplayableMessageTemplate[]>();
		sectionMessageTemplateMap.putAll(templateMap);
		idMessageTemplateMap = new HashMap<Integer, DisplayableMessageTemplate>();
		for (DisplayableMessageTemplate[] ts : templateMap.values()) {
			for (DisplayableMessageTemplate template : ts) {
				idMessageTemplateMap.put(template.getId(), template);
			}
		}
	}

	public DisplayableMessageTemplate[] getMessageTemplates(
			MessageCenterComponent section) {
		DisplayableMessageTemplate[] results = sectionMessageTemplateMap
				.get(section.getId());
		return results == null ? NoTemplates : results;
	}

	/**
	 * Get the number of message templates in one section
	 * 
	 * @param section
	 * @return
	 */
	public int getNumberOfMessageTemplates(MessageCenterComponent section) {
		DisplayableMessageTemplate[] templates = sectionMessageTemplateMap
				.get(section.getId());
		return templates == null ? 0 : templates.length;
	}

	public DisplayableMessageTemplate getMessageTemplateById(int id) {
		return idMessageTemplateMap.get(id);
	}

	public MessageTemplateRepository filter(Set<Integer> templateIds) {
		Iterator<MessageCenterComponentId> it = sectionMessageTemplateMap
				.keySet().iterator();
		Map<MessageCenterComponentId, DisplayableMessageTemplate[]> filteredMap = new HashMap<MessageCenterComponentId, DisplayableMessageTemplate[]>();
		while (it.hasNext()) {
			MessageCenterComponentId cid = it.next();
			DisplayableMessageTemplate[] templates = sectionMessageTemplateMap.get(cid);
			List<DisplayableMessageTemplate> newList = new ArrayList<DisplayableMessageTemplate>();
			for (DisplayableMessageTemplate t : templates) {
				if (templateIds.contains(t.getId())) {
					newList.add(t);
				}
			}
			DisplayableMessageTemplate[] filteredTemplates = new DisplayableMessageTemplate[newList.size()];
			newList.toArray(filteredTemplates);
			filteredMap.put(cid, filteredTemplates);
		}
		return new MessageTemplateRepository(filteredMap);
	}
}
