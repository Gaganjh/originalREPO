package com.manulife.pension.ps.web.noticemanager;


import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.manulife.pension.ps.web.util.CloneableAutoForm;

/**
 * This Class is the Action Form of AddContractDocumentNoticeManagerAction.
 * @author krishta
 *
 */
public class AddContractDocumentNoticeManagerForm extends CloneableAutoForm {
	
	private static final long serialVersionUID = 1L;
	private String fileName;
	private MultipartFile file;
	private boolean postToParticipantIndicator;
	private String action = "default";
	private BigDecimal profileId ;
    private boolean logged;

	
	/**
	 * @return the profileId
	 */
	public BigDecimal getProfileId() {
		return profileId;
	}


	/**
	 * @param profileId the profileId to set
	 */
	public void setProfileId(BigDecimal profileId) {
		this.profileId = profileId;
	}


	


	/**
	 * @return the logged
	 */
	public boolean isLogged() {
		return logged;
	}


	/**
	 * @param logged the logged to set
	 */
	public void setLogged(boolean logged) {
		this.logged = logged;
	}


	/**
	 * Default Constructor
	 */
	public AddContractDocumentNoticeManagerForm() {
		super();	       			
	}


	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}


	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	/**
	 * @return the file
	 */
	public MultipartFile getFile() {
		return file;
	}


	/**
	 * @param file the file to set
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}


	/**
	 * @return the postToParticipantIndicator
	 */
	public boolean isPostToParticipantIndicator() {
		return postToParticipantIndicator;
	}


	/**
	 * @param postToParticipantIndicator the postToParticipantIndicator to set
	 */
	public void setPostToParticipantIndicator(boolean postToParticipantIndicator) {
		this.postToParticipantIndicator = postToParticipantIndicator;
	}

	

	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}

}
