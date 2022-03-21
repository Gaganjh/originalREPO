package com.manulife.pension.ps.web.noticemanager;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.platform.web.controller.AutoForm;
/**
 * This Class is the Action Form of EditContractDocumentNoticeManagerAction.
 * 
 * @author grandan
 */
public class EditContractDocumentNoticeManagerForm extends AutoForm{
	
	private static final long serialVersionUID = 1L;
	private String action="default";
	private int documentId;
	private String documentName;
	private MultipartFile documentFileName;
	private String postToPptIndicator;
	private BigDecimal profileId;
	private boolean logged=false;
	private boolean crossedTwelveMonthRule;
	private String docAvailableUntilDate; 
	
	public enum DocumentChangeTypeCode{
		CHNGN("Document renamed"),
		CHNGP("Document PPT site posting switched"),
		REPL("Document replaced "),
		CHRPN("Document replaced and renamed"),
		CHNGB("Document renamed and PPT site posting switched"),
		CHRPP("Document replaced and PPT site posting swithed"),
		CHRPB("Document replaced and renamed and PPT site posting switched");
		
		final String code;
		DocumentChangeTypeCode(String code){
			this.code = code;
		}
		public String getChangeTypeCode(){
			return code;
		}
	}
	
	public BigDecimal getProfileId() {
		return profileId;
	}
	public void setProfileId(BigDecimal profileId) {
		this.profileId = profileId;
	}
	
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	/**
	 * This will get the documentId which is the primary key to perform all operations
	 * 
	 * @return the documentId
	 */
	public int getDocumentId() {
		return documentId;
	}
	/**
	 * This will hold the documentId
	 * 
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}
	/**
	 * This method will get the name of the document 
	 * 
	 * @return the documentName
	 */
	public String getDocumentName() {
		return documentName;
	}
	/**
	 * This method will set the name of the document i.e rename the document 
	 * 
	 * @param documentName the documentName to set
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	/**
	 * This method will get the name of the upload document user browsed
	 * 
	 * @return the documentFileName
	 */
	public MultipartFile getDocumentFileName() {
		return documentFileName;
	}
	/**
	 * This method will set the name of the upload document that user replaced the upload File 
	 * 
	 * @param documentFileName the documentFileName to set
	 */
	public void setDocumentFileName(MultipartFile documentFileName) {
		this.documentFileName = documentFileName;
	}
	/**
	 * This method will get the PostToPptIndicator value whether it is Yes or No
	 * 
	 * @return the postToPptIndicator
	 */
	public String getPostToPptIndicator() {
		return postToPptIndicator;
	}
	/**
	 * This method will set the PostTopptIndicato value to Yes or No based on the requirement
	 * 
	 * @param postToPptIndicator the postToPptIndicator to set
	 */
	public void setPostToPptIndicator(String postToPptIndicator) {
		this.postToPptIndicator = postToPptIndicator;
	}
	/**
	 * This method will get the Action that user trying to perform
	 * 
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * This method will set the Action
	 * 
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	public boolean isCrossedTwelveMonthRule() {
		return crossedTwelveMonthRule;
	}
	public void setCrossedTwelveMonthRule(boolean crossedTwelveMonthRule) {
		this.crossedTwelveMonthRule = crossedTwelveMonthRule;
	}
	public String getDocAvailableUntilDate() {
		return docAvailableUntilDate;
	}
	public void setDocAvailableUntilDate(String docAvailableUntilDate) {
		this.docAvailableUntilDate = docAvailableUntilDate;
	}
}

