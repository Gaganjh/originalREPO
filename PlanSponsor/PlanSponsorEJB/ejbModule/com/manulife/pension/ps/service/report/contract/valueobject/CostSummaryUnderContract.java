package com.manulife.pension.ps.service.report.contract.valueobject;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Value object that holds Cost summary for services and investment options under given Contract
 * @author Chavva Akhilesh
 *
 */
public class CostSummaryUnderContract implements Serializable {

	private static final long serialVersionUID = 1L;
	private String to;
	private BigDecimal contractCosts;
	private BigDecimal investmentCosts;
	private BigDecimal total;
	
	public CostSummaryUnderContract(String to, BigDecimal contractCosts, BigDecimal investmentCosts, BigDecimal total){
		this.to = to;
		this.contractCosts = contractCosts;
		this.investmentCosts = investmentCosts;
		this.total = total;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the contractCosts
	 */
	public BigDecimal getContractCosts() {
		return contractCosts;
	}

	/**
	 * @param contractCosts the contractCosts to set
	 */
	public void setContractCosts(BigDecimal contractCosts) {
		this.contractCosts = contractCosts;
	}

	/**
	 * @return the investmentCosts
	 */
	public BigDecimal getInvestmentCosts() {
		return investmentCosts;
	}

	/**
	 * @param investmentCosts the investmentCosts to set
	 */
	public void setInvestmentCosts(BigDecimal investmentCosts) {
		this.investmentCosts = investmentCosts;
	}

	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
