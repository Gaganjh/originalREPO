package com.manulife.pension.ps.service.submission.valueobject;

import java.io.Serializable;


public class SubmissionCase implements Serializable{

	private int submission_id;
	private int contract_id;
	
	public SubmissionCase() {
		super();
	}

	public SubmissionCase(int submission_id, int contract_id) {
		this.submission_id = submission_id;
		this.contract_id = contract_id;
		}	
	/**
	 * @param contractId The contractId to set.
	 */	
	public void setContract_id(int contract_id){
		this.contract_id = contract_id;
	}
	
	/**
	 * @return Returns the contract_id.
	 */	
	public int getContract_id(){
		return contract_id;
	}
	/**
	 * @param contractId The contractId to set.
	 */	
	public void setSubmission_id(int submission_id){
		this.submission_id = submission_id;
	}
	/**
	 * @return Returns the submission_id.
	 */	
	public int getSubmission_id(){
		return submission_id;
	}
	public String toString() {
		return "SubmissionCase: " + this.submission_id + "," + this.contract_id ;  
	}
}
