package com.manulife.pension.ps.web.messagecenter.personalization;

import com.manulife.pension.exception.SystemException;
import com.manulife.pension.service.message.valueobject.MessageTemplate.MessageActionType;

/**
 * Value object interface for MessageTemplate (displaying
 * in MessageCenter UI)
 * 
 * @author guweigu
 *
 */
public interface DisplayableMessageTemplate {

	/**
	 * Returns the message id
	 * @return
	 */
	int getId();
	
	/**
	 * Returns the short text of the message
	 * @return
	 * @throws SystemException
	 */
	String getShortText() throws SystemException;
	
	/**
	 * Returns the formatted long text of the message
	 * @return
	 * @throws SystemException
	 */
	String getLongText() throws SystemException;
	
	/**
	 * Returns the info url of the message
	 */
	String getInfoURL() throws SystemException;
	
	/**
	 * Returns the action type of the message
	 * @return
	 */
	MessageActionType getType();
	
	/**
	 * Returns the string of the type
	 */
	String getTypeString();
}
