package com.manulife.pension.ps.web.messagecenter.personalization;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;

/**
 * A default implementation for DisplayableMessageTemplate which
 * the client has to populate the content
 * 
 * @author guweigu
 *
 */
public class DefaultDisplayableMessageTemplateImpl implements DisplayableMessageTemplate {
	private int id;
	private String shortText;
	private String longText;
	private MessageActionType type;
	
	public DefaultDisplayableMessageTemplateImpl(int id, String shortText,
			String longText, MessageActionType type) {
		this.id = id;
		this.shortText = shortText;
		this.longText = longText;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getLongText() throws SystemException {
		return longText;
	}

	public String getShortText() throws SystemException {
		return shortText;
	}

	public MessageActionType getType() {
		return type;
	}

	public String getInfoURL() throws SystemException {
		return "info";
	}

	public String getTypeString() {
		return type.toString();
	}
}
