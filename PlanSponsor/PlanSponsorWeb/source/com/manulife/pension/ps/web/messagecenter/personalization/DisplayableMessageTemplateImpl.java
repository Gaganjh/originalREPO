package com.manulife.pension.ps.web.messagecenter.personalization;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.content.bizdelegates.BrowseServiceDelegate;
import com.manulife.pension.content.exception.ContentException;
import com.manulife.pension.content.valueobject.Content;
import com.manulife.pension.content.valueobject.ContentTypeManager;
import com.manulife.pension.content.valueobject.MessageCenter;
import com.manulife.pension.content.valueobject.Miscellaneous;
import com.manulife.pension.event.exception.UnrecoverableEventException;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.messagecenter.util.MCUtils;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;
import com.manulife.pension.util.StringUtility;
import com.manulife.pension.util.content.helper.ContentUtility;
import com.manulife.pension.util.content.manager.ContentCacheManager;

public class DisplayableMessageTemplateImpl extends
		BaseSerializableCloneableObject implements DisplayableMessageTemplate {
	private static Logger logger = Logger
			.getLogger(DisplayableMessageTemplateImpl.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private int contentKey;
	public static final int SUMMARY_LINK_TEXT = 61872;
	public static final int ARCHIVE_LINK_TEXT =61873;
	public static final int MESSAGE_LINK_TEXT =61875;
	public static final int PERSONALIZE_LINK_TEXT =61874;
	public static final String SUMMARY_LINK = "{summaryLink}";
	public static final String ARCHIVE_LINK = "{archiveLink}";
	public static final String PERSONALIZE_LINK = "{personalizeLink}";
	public static final String MESSAGE_LINK = "{messageLink}";
	public static final String INFO_LINK = "{infoLink}";
	public static final String ACT_LINK = "{actLink}";

	
	private MessageActionType type;

	private static EnumMap<MessageActionType, String> ActionTypeStrMap = new EnumMap<MessageActionType, String>(
			MessageActionType.class);
	static {
		ActionTypeStrMap.put(MessageActionType.FYI, "FYI");
		ActionTypeStrMap.put(MessageActionType.ACTION, "Action");
		ActionTypeStrMap.put(MessageActionType.DECLARE_COMPLETE, "Action");
	}

	public DisplayableMessageTemplateImpl(int id, int contentKey,
			MessageActionType type) {
		this.id = id;
		this.contentKey = contentKey;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public String getShortText() throws SystemException {
		try {
			MessageCenter content = (MessageCenter) ContentCacheManager
					.getInstance().getContentById(contentKey,
							ContentTypeManager.instance().MESSAGE_CENTER);
			return substituteParams(content.getText());
		} catch (ContentException e) {
			throw new SystemException(e,
					"Fail to retrieve CMA content for key = " + contentKey);
		}
	}

	public String getLongText() throws SystemException {
		try {
			MessageCenter content = (MessageCenter) ContentCacheManager
					.getInstance().getContentById(contentKey,
							ContentTypeManager.instance().MESSAGE_CENTER);
			String parameterValues = content.getParameterValues();
			String longText = substituteParams(content.getLongText());
			String formattedText = longText;
			if (parameterValues != null) {
				try {
					formattedText = StringUtility.substituteParams(longText,
							(Object[]) MCUtils
									.parseParameterValues(parameterValues));
				} catch (Exception e) {
					logger.error("Fail to format the long text for content id = " + contentKey, e);
				}
			}
			return formattedText;
		} catch (ContentException e) {
			throw new SystemException(e,
					"Fail to retrieve CMA content for key = " + contentKey);
		}
	}

	/**
	 * Substitute the {infoLink}{actLink}{summaryLink}}{archiveLink}{personalizeLink} global placeholders
	 * 
	 * @param formattedText
	 * @return
	 * @throws SystemException 
	 */
	private String substituteParams(String source) throws SystemException {
		Map<String,String> subMap = new HashMap<String,String>();
		subMap.put(INFO_LINK, getInfoURL());
		subMap.put(PERSONALIZE_LINK,getMiscellaneousContent(PERSONALIZE_LINK_TEXT).getText());
		subMap.put(SUMMARY_LINK,getMiscellaneousContent(SUMMARY_LINK_TEXT).getText());
		subMap.put(ARCHIVE_LINK,getMiscellaneousContent(ARCHIVE_LINK_TEXT).getText());
		subMap.put(ACT_LINK,"Click here to take immediate action on this message");
		subMap.put(MESSAGE_LINK, getMiscellaneousContent(MESSAGE_LINK_TEXT).getText());
		for ( String key : subMap.keySet()) {
			int index = 1;
			while ( index > -1 ) {
				index = source.indexOf(key);
				if ( index > -1 ) {
					String before = source.substring(0, index);
					String after = source.substring(index + key.length());
					source = before + subMap.get(key) + after;
				}
			}
		}
		return source;		
	}

	public MessageActionType getType() {
		return type;
	}

	public String getTypeString() {
		return ActionTypeStrMap.get(type);
	}

	public int getContentKey() {
		return contentKey;
	}

	public String getInfoURL() throws SystemException {
		try {
			MessageCenter content = (MessageCenter) ContentCacheManager
					.getInstance().getContentById(contentKey,
							ContentTypeManager.instance().MESSAGE_CENTER);
			return content.getInfoUrlDescription();
		} catch (ContentException e) {
			throw new SystemException(e,
					"Fail to retrieve CMA content for key = " + contentKey);
		}
	}
	private  Miscellaneous getMiscellaneousContent(int contentId) throws SystemException, UnrecoverableEventException {
        if (logger.isDebugEnabled())
            logger.debug("entry -> getMiscellaneousContent");
        
		Miscellaneous content = null; 
		try {
			Content cntBean = ContentCacheManager.getInstance().getContentById(contentId,ContentTypeManager.instance().MISCELLANEOUS);
			
			
			if ( cntBean != null ) {
				content = (Miscellaneous) cntBean;
			} else {
				Exception e = new Exception("Content does not exisit. Content id:"+contentId);
                throw new UnrecoverableEventException(e, "Failed to retrieve content id:"+contentId); 
			}
		} catch (ContentException e) {
				throw new SystemException(e, ContentUtility.class.getName(), "getMiscellaneousContent", 
					"Failed to retrieve content id:"+contentId);
		}		
			
        if (logger.isDebugEnabled())
            logger.debug("exit <- getMiscellaneousContent");
        
		return content;			
	}	
}
