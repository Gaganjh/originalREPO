package com.manulife.pension.ps.web.tools;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.platform.web.controller.AutoForm;

public class SubmissionCensusForm extends AutoForm {

	private static final String SEND_ACTION = "send";

	public static final String PAYMENT_INSTRUCTION_ACCOUNT_PREFIX = "pmtaccount_";

	private String actionLabel;

	private boolean isDisplayFileUploadSection = false;

	/**
	 * file type: Regular, Misc., etc.
	 */
	private static final String fileType = GFTUploadDetail.SUBMISSION_TYPE_CENSUS;

	/**
	 * The file that the user has uploaded
	 */
	private transient MultipartFile uploadFile;
    
    /**
     * E-mail used for notification
     */
    private String email;

	private static final Map ACTION_LABEL_MAP = new HashMap();

	/*
	 * Maps the button label to the corresponding action.
	 */
	static {
		ACTION_LABEL_MAP.put("send", "send");
	}

	/**
	 * Retrieve a representation of the file the user has uploaded
	 */
	public MultipartFile getUploadFile() {
		return uploadFile;
	}

	/**
	 * Set a representation of the file the user has uploaded
	 */
	public void setUploadFile(MultipartFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public boolean isSendAction() {
		return SEND_ACTION.equals(getAction());
	}

	public String getFileType() {
		return fileType;
	}

	public boolean isDisplayFileUploadSection() {
		return isDisplayFileUploadSection;
	}

	public void setDisplayFileUploadSection(boolean b) {
		isDisplayFileUploadSection = b;
	}

	public void reset( HttpServletRequest request) {
		super.reset( request);
		actionLabel = null;
	}

	public void clear( HttpServletRequest request) {
		reset( request);
		isDisplayFileUploadSection = false;
	}

	public String getActionLabel() {
		return actionLabel;
	}

	public void setActionLabel(String actionLabel) {
		this.actionLabel = trimString(actionLabel);
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	public String getAction() {
		if (super.getAction().length() == 0 && actionLabel != null
				&& actionLabel.length() > 0) {
			setAction((String) ACTION_LABEL_MAP.get(actionLabel));
		}
		return super.getAction();
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

