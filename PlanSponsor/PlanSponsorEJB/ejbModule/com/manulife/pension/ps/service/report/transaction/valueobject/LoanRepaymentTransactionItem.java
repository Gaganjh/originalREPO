package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

import com.manulife.pension.service.contract.valueobject.ParticipantVO;

/**
 * @author Ludmila Stern
 */
public class LoanRepaymentTransactionItem implements Serializable {
	
	private short loanNumber;
	private BigDecimal principalAmount;
	private BigDecimal interestAmount;
	private BigDecimal repaymentAmount;
	private ParticipantVO participant;
	private String transactionNumber;
	
	/**
	 * @return Returns the participant.
	 */
	public ParticipantVO getParticipant() {
		return participant;
	}

	/**
	 * @param participant
	 *            The participant to set.
	 */
	public void setParticipant(ParticipantVO participant) {
		this.participant = participant;
	}

	public void setLoanNumber(short loanNumber) {
		this.loanNumber = loanNumber;
	}

	public short getLoanNumber() {
		return loanNumber;
	}

	public void setPrincipalAmount (BigDecimal principalAmount) {
		this.principalAmount = principalAmount;
	}

	public BigDecimal getPrincipalAmount () {
		return principalAmount;
	}

	public void setInterestAmount (BigDecimal interestAmount) {
		this.interestAmount = interestAmount;
	}

	public BigDecimal getInterestAmount () {
		return interestAmount;
	}

	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}

	public BigDecimal getRepaymentAmount() {
	 return repaymentAmount;
	}
	
	/**
	 * Gets the transactionNumber
	 * @return Returns a String
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * Sets the transactionNumber
	 * @param transactionNumber The transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	public String toString() {
		StringBuffer strbf = new StringBuffer();
		strbf.append (" participantId: ").append (participant.getId())
		     .append (" ssn: " ).append (participant.getSsn())
		     .append (" last name: ").append(participant.getLastName())
		     .append (" first name: ").append(participant.getFirstName())
		     .append (" loan id: ").append(loanNumber)
		     .append (" repaymentAmount: ").append (repaymentAmount)
		     .append (" interestAmount: ").append(interestAmount)
		     .append (" principalAmount: ").append(principalAmount)
		     .append (" transactionNumber: " ).append (transactionNumber);		     
		return strbf.toString();
	} 
}