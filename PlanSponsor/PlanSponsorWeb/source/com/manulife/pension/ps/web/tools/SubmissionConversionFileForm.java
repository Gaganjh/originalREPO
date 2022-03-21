package com.manulife.pension.ps.web.tools;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * <p>
 * Action Form for the Send a Conversion file page  
 * </p>
 *
 * @author ramavel
 */

public class SubmissionConversionFileForm extends AutoForm {

	/**
	 * Default Serial Version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * constant for send action
	 */
	private static final String SEND_ACTION = "submit";

	public static final String PAYMENT_INSTRUCTION_ACCOUNT_PREFIX = "pmtaccount_";

	/**
	 * Action label
	 */
	private String actionLabel;

	/**
	 * Tells whether the upload section should be displayed or not
	 */
	private boolean isDisplayFileUploadSection = false;

	/**
	 * file type: Conversion.
	 */
	private static final String fileType = GFTUploadDetail.SUBMISSION_TYPE_CONVERSION;

	/**
	 * The file that the user has uploaded
	 */
	private transient MultipartFile uploadFile;
	
	/**
	 * The file Information that the user has submitted
	 */
	private transient String fileInformation;

	/**
	 * Action label Map
	 */
	private static final Map<String, String> ACTION_LABEL_MAP = 
		new HashMap<String, String>();

	/**
	 * Maps the button label to the corresponding action.
	 */
	static {
		ACTION_LABEL_MAP.put("submit", "submit");
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

	/**
	 * Retrieve a file information 
	 */
	public String getFileInforamtion() {
		return fileInformation;
	}

	/**
	 * Set a file information while user submitted
	 */
	public void setFileInformation(String fileInformation) {
		this.fileInformation = fileInformation;
	}
	
	/**
	 * @return returns the action
	 */
	public boolean isSendAction() {
		return SEND_ACTION.equals(getAction());
	}

	/**
	 * @return returns the file type
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * @return TRUE: If the user has permission to upload file
	 */
	public boolean isDisplayFileUploadSection() {
		return isDisplayFileUploadSection;
	}

	/**
	 * Sets the user upload file permission
	 * @param isDisplayFileUploadSection
	 */
	public void setDisplayFileUploadSection(boolean isDisplayFileUploadSection) {
		this.isDisplayFileUploadSection = isDisplayFileUploadSection;
	}

	/**
	 * reset the form
	 */
	public void reset( HttpServletRequest request) {
		super.reset( request);
		actionLabel = null;
	}

	/**
	 * Clear the form values
	 * 
	 * @param mapping
	 * @param request
	 */
	public void clear( HttpServletRequest request) {
		reset( request);
		isDisplayFileUploadSection = false;
	}

	/**
	 * @return returns the action label
	 */
	public String getActionLabel() {
		return actionLabel;
	}

	/**
	 * Sets the action label
	 * @param actionLabel
	 */
	public void setActionLabel(String actionLabel) {
		this.actionLabel = trimString(actionLabel);
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	/**
	 * returns the action
	 */
	public String getAction() {
		if (super.getAction().length() == 0 && actionLabel != null
				&& actionLabel.length() > 0) {
			setAction((String) ACTION_LABEL_MAP.get(actionLabel));
		}
		return super.getAction();
	}
}