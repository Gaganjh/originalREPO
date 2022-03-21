package com.manulife.pension.platform.web.secureDocumentUpload;

import com.manulife.pension.platform.web.controller.AutoForm;

public class SDUShareDocumentsForm extends AutoForm {

	private static final long serialVersionUID = 1L;
	private boolean isDisplayFileUploadSection = false;
	private String widgetEndpointURL;
	private String submissionComments;
	private String shareInfoJson;
	private String submissionId;
	private Boolean sendEmail = false;
	private SDUSubmissionMetaDataVO sduSubmissionMetaDataVO;
	
	public String getWidgetEndpointURL() {
		return widgetEndpointURL;
	}
	public void setWidgetEndpointURL(String widgetEndpointURL) {
		this.widgetEndpointURL = widgetEndpointURL;
	}
	
	public boolean isDisplayFileUploadSection() {
		return isDisplayFileUploadSection;
	}
	
	public void setDisplayFileUploadSection(boolean b) {
		isDisplayFileUploadSection = b;
	}
	
	public String getSubmissionComments() {
		return submissionComments;
	}
	
	public void setSubmissionComments(String submissionComments) {
		this.submissionComments = submissionComments;
	}
	
	public String getShareInfoJson() {
		return shareInfoJson;
	}
	
	public void setShareInfoJson(String shareInfoJson) {
		this.shareInfoJson = shareInfoJson;
	}
	public SDUSubmissionMetaDataVO getSduSubmissionMetaDataVO() {
		return sduSubmissionMetaDataVO;
	}
	public void setSduSubmissionMetaDataVO(SDUSubmissionMetaDataVO sduSubmissionMetaDataVO) {
		this.sduSubmissionMetaDataVO = sduSubmissionMetaDataVO;
	}
	public Boolean getSendEmail() {
		return sendEmail;
	}
	public void setSendEmail(Boolean sendEmail) {
		this.sendEmail = sendEmail;
	}
	public String getSubmissionId() {
		return submissionId;
	}
	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}
	
	
}