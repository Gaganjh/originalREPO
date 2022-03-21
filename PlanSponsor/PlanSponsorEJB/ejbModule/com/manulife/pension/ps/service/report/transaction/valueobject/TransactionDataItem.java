package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.ObjectUtils;

import com.manulife.pension.service.contract.valueobject.ParticipantVO;

/**
 * This class represents the value object returned from the CashAccountDAO.
 * 
 * @author Maria Lee
 *  
 */
public class TransactionDataItem implements Serializable {

	private Date transactionEffectiveDate;
	private String transactionType;
	private String transactionNumber;
	private String transactionReasonCode;
	private String transactionReasonCodeExcessWD;
	private String transactionStatusCode;
	private BigDecimal transactionAmount;
	private String transactionMode;
	private String moneySource;
	private String transferInProtocolCode;
	private Date rateEffectiveDate;
	private String debitTiedToCreditFlag;
	private Date payrollEndingDate;
	private ParticipantVO participant;
	private boolean isDBContract = false;
	private BigDecimal originalAmount;
	private BigDecimal AvailableAmount;
	private String moneyType;

	public TransactionDataItem() {
		participant = new ParticipantVO();
	}

	/**
	 * Gets the transactionEffectiveDate
	 * 
	 * @return Returns a Date
	 */
	public Date getTransactionEffectiveDate() {
		return transactionEffectiveDate;
	}

	/**
	 * Sets the transactionEffectiveDate
	 * 
	 * @param transactionEffectiveDate
	 *            The transactionEffectiveDate to set
	 */
	public void setTransactionEffectiveDate(Date transactionEffectiveDate) {
		this.transactionEffectiveDate = transactionEffectiveDate;
	}

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

	/**
	 * Gets the transactionType
	 * 
	 * @return Returns a String
	 */
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * Sets the transactionType
	 * 
	 * @param transactionType
	 *            The transactionType to set
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Gets the transactionNumber
	 * 
	 * @return Returns a String
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * Sets the transactionNumber
	 * 
	 * @param transactionNumber
	 *            The transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * Gets the transactionReasonCode
	 * 
	 * @return Returns a String
	 */
	public String getTransactionReasonCode() {
		return transactionReasonCode;
	}

    /**
     * Gets the transactionReasonCodeExcessWD - (added for Roth - Excess Withdrawals) 
     * 
     * @return Returns a String
     */
    public String getTransactionReasonCodeExcessWD() {
        return transactionReasonCodeExcessWD;
    }

	/**
	 * Sets the transactionReasonCode
	 * 
	 * @param transactionReasonCode
	 *            The transactionReasonCode to set
	 */
	public void setTransactionReasonCode(String transactionReasonCode) {
		this.transactionReasonCode = transactionReasonCode;
	}

    /**
     * Sets the transactionReasonCodeExcessWD
     * 
     * @param transactionReasonCodeExcessWD
     *            The transactionReasonCodeExcessWD to set
     */
    public void setTransactionReasonCodeExcessWD(String transactionReasonCodeExcessWD) {
        this.transactionReasonCodeExcessWD = transactionReasonCodeExcessWD;
    }

	/**
	 * Gets the transactionStatusCode
	 * 
	 * @return Returns a String
	 */
	public String getTransactionStatusCode() {
		return transactionStatusCode;
	}

	/**
	 * Sets the transactionStatusCode
	 * 
	 * @param transactionStatusCode
	 *            The transactionStatusCode to set
	 */
	public void setTransactionStatusCode(String transactionStatusCode) {
		this.transactionStatusCode = transactionStatusCode;
	}

	/**
	 * Gets the transactionAmount
	 * 
	 * @return Returns a BigDecimal
	 */
	public BigDecimal getTransactionAmount() {
		return transactionAmount;
	}

	/**
	 * Sets the transactionAmount
	 * 
	 * @param transactionAmount
	 *            The transactionAmount to set
	 */
	public void setTransactionAmount(BigDecimal transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	/**
	 * Gets the transactionMode
	 * 
	 * @return Returns a String
	 */
	public String getTransactionMode() {
		return transactionMode;
	}

	/**
	 * Sets the transactionMode
	 * 
	 * @param transactionMode
	 *            The transactionMode to set
	 */
	public void setTransactionMode(String transactionMode) {
		this.transactionMode = transactionMode;
	}

	/**
	 * Gets the moneySource
	 * 
	 * @return Returns a String
	 */
	public String getMoneySource() {
		return moneySource;
	}

	/**
	 * Sets the moneySource
	 * 
	 * @param moneySource
	 *            The moneySource to set
	 */
	public void setMoneySource(String moneySource) {
		this.moneySource = moneySource;
	}

	/**
	 * Gets the rateEffectiveDate
	 * 
	 * @return Returns a Date
	 */
	public Date getRateEffectiveDate() {
		return rateEffectiveDate;
	}

	/**
	 * Sets the rateEffectiveDate
	 * 
	 * @param rateEffectiveDate
	 *            The rateEffectiveDate to set
	 */
	public void setRateEffectiveDate(Date rateEffectiveDate) {
		this.rateEffectiveDate = rateEffectiveDate;
	}

	/**
	 * Gets the debitTiedToCreditFlag
	 * 
	 * @return Returns a String
	 */
	public String getDebitTiedToCreditFlag() {
		return debitTiedToCreditFlag;
	}

	/**
	 * Sets the debitTiedToCreditFlag
	 * 
	 * @param debitTiedToCreditFlag
	 *            The debitTiedToCreditFlag to set
	 */
	public void setDebitTiedToCreditFlag(String debitTiedToCreditFlag) {
		this.debitTiedToCreditFlag = debitTiedToCreditFlag;
	}

	/**
	 * Gets the payrollEndingDate
	 * 
	 * @return Returns a Date
	 */
	public Date getPayrollEndingDate() {
		return payrollEndingDate;
	}

	/**
	 * Sets the payrollEndingDate
	 * 
	 * @param payrollEndingDate
	 *            The payrollEndingDate to set
	 */
	public void setPayrollEndingDate(Date payrollEndingDate) {
		this.payrollEndingDate = payrollEndingDate;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (o instanceof TransactionDataItem) {
			TransactionDataItem item = (TransactionDataItem) o;
			return ObjectUtils.equals(item.debitTiedToCreditFlag,
					debitTiedToCreditFlag)
					&& ObjectUtils.equals(item.participant, participant)
					&& ObjectUtils.equals(item.moneySource, moneySource)
					&& ObjectUtils.equals(item.payrollEndingDate,
							payrollEndingDate)
					&& ObjectUtils.equals(item.rateEffectiveDate,
							rateEffectiveDate)
					&& ObjectUtils.equals(item.transactionAmount,
							transactionAmount)
					&& ObjectUtils.equals(item.transactionEffectiveDate,
							transactionEffectiveDate)
					&& ObjectUtils
							.equals(item.transactionMode, transactionMode)
					&& ObjectUtils.equals(item.transactionNumber,
							transactionNumber)
					&& ObjectUtils.equals(item.transactionReasonCode,
							transactionReasonCode)
					&& ObjectUtils.equals(item.transactionStatusCode,
							transactionStatusCode)
					&& ObjectUtils
							.equals(item.transactionType, transactionType);
		}
		return false;
	}

	public String toString() {

		StringBuffer dump = new StringBuffer();

		dump.append("transactionEffectiveDate: ").append(
				transactionEffectiveDate).append("\n");
		dump.append("transactionType: ").append(transactionType).append("\n");
		dump.append("transactionNumber: ").append(transactionNumber).append(
				"\n");
		dump.append("transactionReasonCode: ").append(transactionReasonCode)
				.append("\n");
		dump.append("transactionStatusCode: ").append(transactionStatusCode)
				.append("\n");
		dump.append("transactionAmount: ").append(transactionAmount).append(
				"\n");
		dump.append("transactionMode: ").append(transactionMode).append("\n");
		dump.append("moneySource: ").append(moneySource).append("\n");
		dump.append("rateEffectiveDate: ").append(rateEffectiveDate).append(
				"\n");
		dump.append("participant: ").append(participant).append("\n");
		dump.append("debitTiedToCreditFlag: ").append(debitTiedToCreditFlag)
				.append("\n");
		dump.append("payrollEndingDate: ").append(payrollEndingDate).append(
				"\n");
		dump.append("isDBContract: ").append(isDBContract).append("\n");
		
		return dump.toString();
	}
	/**
	 * @return Returns the transferInProtocolCode.
	 */
	public String getTransferInProtocolCode() {
		return transferInProtocolCode;
	}
	/**
	 * @param transferInProtocolCode The transferInProtocolCode to set.
	 */
	public void setTransferInProtocolCode(String transferInProtocolCode) {
		this.transferInProtocolCode = transferInProtocolCode;
	}

	public boolean isDBContract() {
		return isDBContract;
	}

	public void setDBContract(boolean isDBContract) {
		this.isDBContract = isDBContract;
	}

	/**
	 * @return the originalAmount
	 */
	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param originalAmount the originalAmount to set
	 */
	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return the availableAmount
	 */
	public BigDecimal getAvailableAmount() {
		return AvailableAmount;
	}

	/**
	 * @param availableAmount the availableAmount to set
	 */
	public void setAvailableAmount(BigDecimal availableAmount) {
		AvailableAmount = availableAmount;
	}

	/**
	 * @return the moneyType
	 */
	public String getMoneyType() {
		return moneyType;
	}

	/**
	 * @param moneyType the moneyType to set
	 */
	public void setMoneyType(String moneyType) {
		this.moneyType = moneyType;
	}
	
	
}
