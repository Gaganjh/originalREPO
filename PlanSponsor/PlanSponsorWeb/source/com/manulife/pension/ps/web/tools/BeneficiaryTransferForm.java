
package com.manulife.pension.ps.web.tools;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * Form Bean for  Beneficiary Internal Transfer
 * @author Tamilarasu Krishnamoorthy
 *
 */
public class BeneficiaryTransferForm extends AutoForm {

	
	private static final long serialVersionUID = 1L;
	
	private String oldContract;
	private String oldContractName;
	private String newContract;
	private int recordCount;
	private String message;
	private boolean validContract;
	private String newContractName;
	
	/**
	 * Default constructor	
	 */
	public BeneficiaryTransferForm() {
	}
	
	/**
	 * Get the Old contract Id
	 * @return oldContract
	 */
	public String getOldContract() {
		return oldContract;
	}
	
	/**
	 * Set the old Contract Id
	 * @param oldContract
	 */
	public void setOldContract(String oldContract) {
		this.oldContract = oldContract;
	}
	
	/**
	 * Get the New Contract Id
	 * @return
	 */
	public String getNewContract() {
		return newContract;
	}
	
	/**
	 *Set the new contract Id
	 * @param newContract
	 */
	public void setNewContract(String newContract) {
		this.newContract = newContract;
	}

	/**
	 * Get the total transfered record count
	 * @return recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}
	
	/**
	 * Set the total transfered record count
	 * @param recordCount
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	
	/**
	 * get the pop up message 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * set the pop up message
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Get the old contract name
	 * @return
	 */
	public String getOldContractName() {
		return oldContractName;
	}
	
	/**
	 * Set the old contract name
	 * @param oldContractName
	 */
	public void setOldContractName(String oldContractName) {
		this.oldContractName = oldContractName;
	}

	
	/**
	 * Get the isValidContract boolean value
	 * @return boolean validContract
	 */
	public boolean isValidContract() {
		return validContract;
	}
	
	/**
	 * Set the validContract Boolean value
	 * @param  boolean 
	 * 				validContract
	 */
	public void setValidContract(boolean validContract) {
		this.validContract = validContract;
	}

	/***
	 * Get the name of the new contract.
	 * 
	 * @return newContractName
	 */
	public String getNewContractName() {
		return newContractName;
	}

	/**
	 * Set the name of the new contract.
	 * 
	 * @param newContractName
	 */
	public void setNewContractName(String newContractName) {
		this.newContractName = newContractName;
	}

}
