package com.manulife.pension.bd.web.myprofile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.bd.web.messagecenter.BDMessageCenterConstants;
import com.manulife.pension.bd.web.messagecenter.BDMessageCenterUtils;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.security.bd.valueobject.BDMessagePreference;
import com.manulife.pension.service.security.bd.valueobject.BDUserMessageCenterPreferences;

public class MyProfilePreferenceForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private boolean changed = false;

	private boolean hasMessagePreference = false;

	private String defaultSiteLocation;

	private Map<String, BDMessagePreference> messagePrefMap;
	private boolean summaryEmail;
	private List<BDMessagePreference> recivemsgList;

	public BDUserMessageCenterPreferences getMessageCenterPreferences()
			throws SystemException {
		BDUserMessageCenterPreferences messageCenterPreferences = BDMessageCenterUtils
				.getEmptyUserPreferences();
		if (messagePrefMap != null) {
			for (BDMessagePreference mp : messagePrefMap.values()) {
				messageCenterPreferences.setMessagePreference(mp);
			}
		}
		// make sure Global Message can't be turned off
		messageCenterPreferences.getMessagePreference(
				BDMessageCenterConstants.GlobalMessageTemplateId)
				.setReceiveMessage(true);
		messageCenterPreferences.setReceiveSummaryEmail(summaryEmail);
		return messageCenterPreferences;
	}

	public void setMessageCenterPreferences(
			BDUserMessageCenterPreferences messageCenterPreferences) {
		messagePrefMap = new LinkedHashMap<String, BDMessagePreference>(23);
		for (BDMessagePreference mp : messageCenterPreferences
				.getMessagePreferences()) {
			if(mp.getTemplateId()== BDMessageCenterConstants.FundCheckMessageId){
				mp.setEmailNotification(true);
				mp.setReceiveMessage(true);
				messagePrefMap.put(Integer.toString(mp.getTemplateId()), mp);
			}else{
				messagePrefMap.put(Integer.toString(mp.getTemplateId()), mp);
			}
			
		}
		summaryEmail = messageCenterPreferences.getReceiveSummaryEmail();
	}
	public List<BDMessagePreference> getRecivemsgList() {
		if (messagePrefMap == null) {
			messagePrefMap = new LinkedHashMap<String, BDMessagePreference>(23);
		}
		/*if(recivemsgList!=null){
		for(int i=0;i<recivemsgList.size();i++){
			
			messagePrefMap.put(messagePrefMap.keySet().toArray()[i].toString(), recivemsgList.get(i));
		}
		}*/
		return  new ArrayList<BDMessagePreference>(messagePrefMap.values());
	}
	public boolean getEmailNotification(String id) {
		return getMessagePref(id).getEmailNotification();
	}

	public void setEmailNotification(String id, boolean value) {
		getMessagePref(id).setEmailNotification(value);
	}

	private BDMessagePreference getMessagePref(String id) {
		if (messagePrefMap == null) {
			messagePrefMap = new LinkedHashMap<String, BDMessagePreference>(23);
		}
		BDMessagePreference value = messagePrefMap.get(id);
		if (value == null) {
			value = new BDMessagePreference();
			int tid = Integer.parseInt(id);
			value.setTemplateId(tid);
			value.setEmailNotification(Boolean.FALSE);
			if (tid == BDMessageCenterConstants.GlobalMessageTemplateId) {
				value.setReceiveMessage(Boolean.TRUE);
			} else {
				value.setReceiveMessage(Boolean.FALSE);
			}
			messagePrefMap.put(id, value);
		}
		return value;
	}

	public boolean isSummaryEmail() {
		return summaryEmail;
	}

	public void setSummaryEmail(boolean summaryEmail) {
		this.summaryEmail = summaryEmail;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isHasMessagePreference() {
		return hasMessagePreference;
	}

	public void setHasMessagePreference(boolean hasMessagePreference) {
		this.hasMessagePreference = hasMessagePreference;
	}

	public String getDefaultSiteLocation() {
		return defaultSiteLocation;
	}

	public void setDefaultSiteLocation(String defaultSiteLocation) {
		this.defaultSiteLocation = defaultSiteLocation;
	}
}
