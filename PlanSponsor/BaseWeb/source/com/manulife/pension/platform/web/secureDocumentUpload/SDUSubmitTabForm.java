package com.manulife.pension.platform.web.secureDocumentUpload;

public class SDUSubmitTabForm extends SDUReportForm {

	private static final long serialVersionUID = 1L;
	private boolean isDisplayFileUploadSection = false;
	private String widgetEndpointURL;
	private String submissionConfirmationJson="";
	private boolean isPendingContract = false;	
	
	public SDUSubmitTabForm() {
		super();
	}
		
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
	public String getSubmissionConfirmationJson() {
		return submissionConfirmationJson;
	}
	public void setSubmissionConfirmationJson(String submissionConfirmationJson) {
		this.submissionConfirmationJson = submissionConfirmationJson;
	}
	public boolean isPendingContract() {
		return isPendingContract;
	}
	public void setPendingContract(boolean isPendingContract) {
		this.isPendingContract = isPendingContract;
	}		
}