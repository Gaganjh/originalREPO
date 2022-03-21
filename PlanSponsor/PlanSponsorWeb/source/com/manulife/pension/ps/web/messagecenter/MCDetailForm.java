package com.manulife.pension.ps.web.messagecenter;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.message.valueobject.MessageDetail;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;




public class MCDetailForm extends AutoForm {
	
	private MessageDetail message;
	private String contractId;
	private String messageId;
	private Boolean carView = false;

	public Boolean getCarView() {
		return carView;
	}

	public void setCarView(Boolean carView) {
		this.carView = carView;
	}
	
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public MessageDetail getMessage() {
		return message;
	}

	public void setMessage(MessageDetail message) {
		this.message = message;
	}

}
