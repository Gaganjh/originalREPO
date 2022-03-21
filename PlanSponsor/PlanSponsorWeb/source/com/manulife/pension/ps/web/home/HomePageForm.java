package com.manulife.pension.ps.web.home;

import com.manulife.pension.ps.web.controller.PsForm;

public class HomePageForm extends PsForm {

	private String contractNumber;
	private String selectedReport;
	private String selectedParticipant;
	
	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public String getContractNumber() {
		return contractNumber == null ? "" : contractNumber.trim();
	}
	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}


	/**
	 * Gets the selectedReport
	 * @return Returns a String
	 */
	public String getSelectedReport() {
		return selectedReport;
	}
	/**
	 * Sets the selectedReport
	 * @param selectedReport The selectedReport to set
	 */
	public void setSelectedReport(String selectedReport) {
		this.selectedReport = selectedReport;
	}


	/**
	 * Gets the selectedParticipant
	 * @return Returns a String
	 */
	public String getSelectedParticipant() {
		return selectedParticipant;
	}
	/**
	 * Sets the selectedParticipant
	 * @param selectedParticipant The selectedParticipant to set
	 */
	public void setSelectedParticipant(String selectedParticipant) {
		this.selectedParticipant = selectedParticipant;
	}


}

