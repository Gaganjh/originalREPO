package com.manulife.pension.bd.web.messagecenter;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.content.BDContentConstants;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.MessageCenter;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;
import com.manulife.pension.util.content.manager.ContentCacheManager;

/**
 * The BD Message center message template
 * 
 * @author guweigu
 * 
 */
public class BDMessageCenterMessageTemplate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int templateId;
	private int contentKey;
	private MessageActionType type;

	public BDMessageCenterMessageTemplate(int templateId, int contentKey,
			MessageActionType type) {
		this.templateId = templateId;
		this.contentKey = contentKey;
		this.type = type;
	}

	/**
	 * Get the short text for message template. Global message has a
	 * special text.
	 * @return
	 * @throws SystemException
	 */
	public String getShortText() throws SystemException {
		try {
			if (templateId == BDMessageCenterConstants.GlobalMessageTemplateId) {
				Miscellaneous globalMsgText = (Miscellaneous) ContentCacheManager
						.getInstance().getContentById(
								BDContentConstants.GLOBAL_MESSAGE_TEXT,
								ContentTypeManager.instance().MISCELLANEOUS);
				return StringUtils.trimToEmpty(globalMsgText.getText());
			}
			MessageCenter content = (MessageCenter) ContentCacheManager
					.getInstance().getContentById(contentKey,
							ContentTypeManager.instance().MESSAGE_CENTER);
			return StringUtils.trimToEmpty(content.getText());
		} catch (ContentException e) {
			throw new SystemException(e,
					"Fail to retrieve CMA content for key = " + contentKey);
		}
	}

	public int getTemplateId() {
		return templateId;
	}

	public int getContentKey() {
		return contentKey;
	}

	public MessageActionType getType() {
		return type;
	}

	public String toString() {
		StringBuffer fieldValues = new StringBuffer("[templateId=" + templateId
				+ ";contentKey=" + contentKey + ";type=" + type + "shortText=");
		try {
			fieldValues.append(getShortText());
		} catch (SystemException e) {
			fieldValues.append("");
		}
		fieldValues.append("]\n");
		return fieldValues.toString();
	}
}
