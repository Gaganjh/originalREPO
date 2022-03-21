package com.manulife.pension.service.loan.valueobject;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;

/**
 * @author Ilker Celikyilmaz
 * 
 * This class will be used to represent the outsating loans from Participant_Loan table	
 *
 */
public class OutstandingLoan extends BaseSerializableCloneableObject {
	  private final static long serialVersionUID = -1;
	
	  private Long profileId;
	  private Long participantId;
	  private Integer contractId;
	  private Integer loanId;
	  private BigDecimal principalAmount;
	  private Date effectiveDate;
	  private BigDecimal loanInterestPct;
	  private BigDecimal outstandingPrincipalAmount;
	  private BigDecimal outstandingInterestAmount;
	  private Date maturityDate;
	  private BigDecimal loanExpenseMarginRate;
	  private Date lastRepaymentDate;
	  private BigDecimal lastRepaymentAmount;
	  private Long lastRepaymentTransactionNo;
	  private Date loanCreateDate;
	/**
	 * @param profileId
	 * @param participantId
	 * @param contractId
	 * @param loanId
	 * @param principalAmount
	 * @param effectiveDate
	 * @param loanInterestPct
	 * @param outstandingPrincipalAmount
	 * @param outstandingInterestAmount
	 * @param maturityDate
	 * @param loanExpenseMarginRate
	 * @param lastRepaymentDate
	 * @param lastRepaymentAmount
	 * @param lastRepaymentTransactionNo
	 * @param loanCreateDate
	 */
	public OutstandingLoan(Long profileId, Long participantId, Integer contractId,
			Integer loanId, BigDecimal principalAmount, Date effectiveDate,
			BigDecimal loanInterestPct, BigDecimal outstandingPrincipalAmount,
			BigDecimal outstandingInterestAmount, Date maturityDate,
			BigDecimal loanExpenseMarginRate, Date lastRepaymentDate,
			BigDecimal lastRepaymentAmount, Long lastRepaymentTransactionNo,
			Date loanCreateDate) {
		super();
		this.participantId = participantId;
		this.contractId = contractId;
		this.loanId = loanId;
		this.principalAmount = principalAmount;
		this.effectiveDate = effectiveDate;
		this.loanInterestPct = loanInterestPct;
		this.outstandingPrincipalAmount = outstandingPrincipalAmount;
		this.outstandingInterestAmount = outstandingInterestAmount;
		this.maturityDate = maturityDate;
		this.loanExpenseMarginRate = loanExpenseMarginRate;
		this.lastRepaymentDate = lastRepaymentDate;
		this.lastRepaymentAmount = lastRepaymentAmount;
		this.lastRepaymentTransactionNo = lastRepaymentTransactionNo;
		this.loanCreateDate = loanCreateDate;
	}
	/**
	 * @return the participantId
	 */
	public Long getParticipantId() {
		return participantId;
	}
	/**
	 * @return the contractId
	 */
	public Integer getContractId() {
		return contractId;
	}
	/**
	 * @return the loanId
	 */
	public Integer getLoanId() {
		return loanId;
	}
	/**
	 * @return the principalAmount
	 */
	public BigDecimal getPrincipalAmount() {
		return principalAmount;
	}
	/**
	 * @return the effectiveDate
	 */
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @return the loanInterestPct
	 */
	public BigDecimal getLoanInterestPct() {
		return loanInterestPct;
	}
	/**
	 * @return the outstandingPrincipalAmount
	 */
	public BigDecimal getOutstandingPrincipalAmount() {
		return outstandingPrincipalAmount;
	}
	/**
	 * @return the outstandingInterestAmount
	 */
	public BigDecimal getOutstandingInterestAmount() {
		return outstandingInterestAmount;
	}
	/**
	 * @return the maturityDate
	 */
	public Date getMaturityDate() {
		return maturityDate;
	}
	/**
	 * @return the loanExpenseMarginRate
	 */
	public BigDecimal getLoanExpenseMarginRate() {
		return loanExpenseMarginRate;
	}
	/**
	 * @return the lastRepaymentDate
	 */
	public Date getLastRepaymentDate() {
		return lastRepaymentDate;
	}
	/**
	 * @return the lastRepaymentAmount
	 */
	public BigDecimal getLastRepaymentAmount() {
		return lastRepaymentAmount;
	}
	/**
	 * @return the lastRepaymentTransactionNo
	 */
	public Long getLastRepaymentTransactionNo() {
		return lastRepaymentTransactionNo;
	}
	/**
	 * @return the loanCreateDate
	 */
	public Date getLoanCreateDate() {
		return loanCreateDate;
	}
	  
	
	

}
