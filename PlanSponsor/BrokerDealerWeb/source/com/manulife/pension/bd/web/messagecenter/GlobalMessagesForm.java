package com.manulife.pension.bd.web.messagecenter;

import java.util.List;

import com.manulife.pension.platform.web.report.BaseReportForm;
import com.manulife.pension.service.message.report.valueobject.BDGlobalMessage;

/**
 * Form Bean for Global Messages Page
 * 
 * @author Sithomas
 */
public class GlobalMessagesForm extends BaseReportForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<BDGlobalMessage> globalMessages;
	private String selectedContentId;
	private boolean messagePublished;

	/**
	 * gets the global messages list
	 * @return globalMessages List<BDGlobalMessage>
	 */
    public List<BDGlobalMessage> getGlobalMessages() {
		return globalMessages;
	}

    /**
     * sets the global messages list
     * @param globalMessages List<BDGlobalMessage>
     */
	public void setGlobalMessages(List<BDGlobalMessage> globalMessages) {
		this.globalMessages = globalMessages;
	}
	
	/**
	 * sets the message content id
	 * @return selectedContentId String
	 */
	public String getSelectedContentId() {
        return selectedContentId;
    }

	/**
	 * gets the message content id
	 * @param selectedContentId String
	 */
    public void setSelectedContentId(String selectedContentId) {
        this.selectedContentId = selectedContentId;
    }
    
    /**
     * gets the message published indicator
     * @return messagePublished boolean
     */
    public boolean isMessagePublished() {
        return messagePublished;
    }

    /**
     * sets the message published indicator
     * @param messagePublished boolean
     */
    public void setMessagePublished(boolean messagePublished) {
        this.messagePublished = messagePublished;
    }
	
}
