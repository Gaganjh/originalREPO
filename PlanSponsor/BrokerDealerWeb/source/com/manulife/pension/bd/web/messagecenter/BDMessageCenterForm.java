package com.manulife.pension.bd.web.messagecenter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * The Message Center report form
 * 
 * @author guweigu
 *
 */
public class BDMessageCenterForm extends BaseReportForm {
	private static final Logger log = Logger
			.getLogger(BDMessageCenterForm.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String messageIds = "";

	public String getMessageIds() {
		return messageIds;
	}

	/**
	 * Set the message ids as , separated string
	 * @param messageIds
	 */
	public void setMessageIds(String messageIds) {
		this.messageIds = messageIds;
	}

	/**
	 * Convert the , separated message id string to a integer array
	 * 
	 * @return
	 */
	public int[] getSelectedMessageIds() {
		String[] idStrs = StringUtils.split(messageIds, ',');
		int[] ids = new int[idStrs.length];
		int i = 0;
		for (String idStr : idStrs) {
			try {
				ids[i++] = Integer.parseInt(StringUtils.trimToEmpty(idStr));
			} catch (NumberFormatException e) {
				// ignore it
				log.error("Fail to parse a message id string", e);
			}
		}
		return ids;
	}
}
