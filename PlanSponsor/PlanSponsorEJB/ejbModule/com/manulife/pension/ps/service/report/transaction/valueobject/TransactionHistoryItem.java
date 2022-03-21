package com.manulife.pension.ps.service.report.transaction.valueobject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;

import com.manulife.pension.service.contract.valueobject.ParticipantVO;
import com.manulife.pension.util.EqualityUtils;

/**
 * @author Charles Chan
 */
public class TransactionHistoryItem implements java.io.Serializable, Comparable {

	/**
	 * Constructor.
	 *  
	 */
	public TransactionHistoryItem() {
		super();
	}

	/**
	 * @return Returns the creditAmount.
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param creditAmount
	 *            The creditAmount to set.
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return Returns the participant.
	 */
	public ParticipantVO getParticipant() {
		return participant;
	}

	public Integer getParticipantId() {
		if (participant != null) { 
			return participant.getId();
		} else {
			return null;
		}
	}
	
	/**
	 * @param participant
	 *            The participant to set.
	 */
	public void setParticipant(ParticipantVO participant) {
		this.participant = participant;
	}

	/**
	 * @return Returns the transactionDate.
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            The transactionDate to set.
	 */
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return Returns the transactionNumber.
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber
	 *            The transactionNumber to set.
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return Returns the transactionType.
	 */
	public String getTypeDescription1() {
		return typeDescription1;
	}

	/**
	 * @param transactionType
	 *            The transactionType to set.
	 */
	public void setTypeDescription1(String transactionType1) {
		this.typeDescription1 = transactionType1;
	}

	/**
	 * @return Returns the transactionType.
	 */
	public String getTypeDescription2() {
		return typeDescription2;
	}

	/**
	 * @param transactionType
	 *            The transactionType to set.
	 */
	public void setTypeDescription2(String transactionType2) {
		this.typeDescription2 = transactionType2;
	}

	/**
	 * Gets the isComplete
	 * 
	 * @return Returns a boolean
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Sets the isComplete
	 * 
	 * @param isComplete
	 *            The isComplete to set
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	/**
	 * @return Returns the descriptions.
	 */
	public List getDescriptionsUnmasked() {
		return descriptionsUnmasked;
	}

	/**
	 * @param descriptionsUnmasked
	 *            The descriptions to set.
	 */
	public void setDescriptionsUnmasked(List descriptionsUnmasked) {
		this.descriptionsUnmasked = descriptionsUnmasked;
	}
	/**
	 * @return Returns the descriptions.
	 */
	public List getDescriptions() {
		return descriptions;
	}

	/**
	 * @param descriptions
	 *            The descriptions to set.
	 */
	public void setDescriptions(List descriptions) {
		this.descriptions = descriptions;
	}

	public int compareTo(Object o) {
		if (o instanceof TransactionHistoryItem) {
			TransactionHistoryItem item = (TransactionHistoryItem) o;
			return this.transactionDate.compareTo(item.transactionDate);
		}
		throw new UnsupportedOperationException(
				"Cannot compare CashAccountItem to " + o.getClass());
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (o instanceof TransactionHistoryItem) {
			TransactionHistoryItem item = (TransactionHistoryItem) o;
			return ObjectUtils
					.equals(transactionNumber, item.transactionNumber)
					&& ObjectUtils.equals(participant, item.participant)
					&& ObjectUtils
							.equals(transactionDate, item.transactionDate)
					&& ObjectUtils.equals(typeDescription1,
							item.typeDescription1)
					&& ObjectUtils.equals(typeDescription2,
							item.typeDescription2)
					&& ObjectUtils.equals(amount, item.amount)
					&& (complete == item.complete)
					&& EqualityUtils.equals(descriptions, item.descriptions);
		}
		return false;
	}

	public String toString() {

		StringBuffer dump = new StringBuffer();

		dump.append("transactionNumber: ").append(transactionNumber).append(
				"\n");
		dump.append("participant: ").append(participant).append("\n");
		dump.append("transactionDate: ").append(transactionDate).append("\n");
		dump.append("transactionType1: ").append(typeDescription1).append("\n");
		dump.append("transactionType2: ").append(typeDescription2).append("\n");
		dump.append("amount: ").append(amount).append("\n");
		dump.append("isComplete: ").append(complete).append("\n");
		return dump.toString();
	}

	private String type;
	private String subType; /* Subtype is only used by InterAccount transfer: Fund to Fund or Rebalance */
	private Date transactionDate;
	private ParticipantVO participant;
	private String typeDescription1;
	private String typeDescription2;
	private List descriptions;
	private List descriptionsUnmasked;
	private String transactionNumber;
	private BigDecimal amount;
	private boolean complete = true;
	private BigDecimal originalAmount;
	private BigDecimal availableAmount;
	private String moneyType;
	
	/**
	 * @return Returns the subType.
	 */
	public String getSubType() {
		return subType;
	}
	/**
	 * @param subType The subType to set.
	 */
	public void setSubType(String subType) {
		this.subType = subType;
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
		return availableAmount;
	}

	/**
	 * @param availableAmount the availableAmount to set
	 */
	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
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