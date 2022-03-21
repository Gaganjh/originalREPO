package com.manulife.pension.ps.service.participant.valueobject;

import java.io.Serializable;


public class ParticipantLoanDetails implements Serializable{
	private String loanId;
	private double outstandingPrincipalAmount;
	
	public ParticipantLoanDetails (String loanId, double outstandingPrincipalAmount) {
		this.loanId = loanId;
		this.outstandingPrincipalAmount = outstandingPrincipalAmount;
	}
	
	
	/**
	 * Gets the loanId
	 * @return Returns a String
	 */
	public String getLoanId() {
		return loanId;
	}
	/**
	 * Sets the loanId
	 * @param loanId The loanId to set
	 */
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	/**
	 * Gets the outstandingPrincipalAmount
	 * @return Returns a double
	 */
	public double getOutstandingPrincipalAmount() {
		return outstandingPrincipalAmount;
	}
	/**
	 * Sets the outstandingPrincipalAmount
	 * @param outstandingPrincipalAmount The outstandingPrincipalAmount to set
	 */
	public void setOutstandingPrincipalAmount(double outstandingPrincipalAmount) {
		this.outstandingPrincipalAmount = outstandingPrincipalAmount;
	}

}

