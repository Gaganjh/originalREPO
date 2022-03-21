package com.manulife.pension.ps.web.tools;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * @author parkand
 */
public class SubmissionDeleteForm extends AutoForm {
	
	private String subNo;
	private String type;
	private String status;


	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Returns the submissionId.
	 */
	public String getSubNo() {
		return subNo;
	}
	/**
	 * @param submissionId The submissionId to set.
	 */
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
}
