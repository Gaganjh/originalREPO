package com.manulife.pension.bd.web.ikit;

import java.math.BigDecimal;

import com.manulife.pension.service.fund.fandp.valueobject.PerformanceAndFees;

/**
 * This class will hold the Fund Information to be sent to IKit Application.
 * 
 * @author harlomte
 * 
 */
public class ReturnsAndFeesVO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Performance And Fees object holding most of the Fund related Information.
	private PerformanceAndFees performanceAndFees;

	// ror_1month, ror_3month, ror_ytd values
	private BigDecimal oneMonthRORAsOfMonthEnd;
	private BigDecimal threeMonthRORAsOfMonthEnd;
	private BigDecimal ytdRORAsOfMonthEnd;
	
	private BigDecimal threeYearSDAsOfQuaterEnd;
	private BigDecimal fiveYearSDAsOfQuaterEnd;
	private BigDecimal tenYearSDAsOfQuaterEnd;
	
	// morningstar category values.
	private String morningstarCategory;

	public ReturnsAndFeesVO(PerformanceAndFees performanceAndFees) {
		this.performanceAndFees = performanceAndFees;
	}

	public PerformanceAndFees getPerformanceAndFees() {
		return performanceAndFees;
	}

	public void setPerformanceAndFees(PerformanceAndFees performanceAndFees) {
		this.performanceAndFees = performanceAndFees;
	}

	public BigDecimal getOneMonthRORAsOfMonthEnd() {
		return oneMonthRORAsOfMonthEnd;
	}

	public void setOneMonthRORAsOfMonthEnd(BigDecimal oneMonthRORAsOfMonthEnd) {
		this.oneMonthRORAsOfMonthEnd = oneMonthRORAsOfMonthEnd;
	}

	public BigDecimal getThreeMonthRORAsOfMonthEnd() {
		return threeMonthRORAsOfMonthEnd;
	}

	public void setThreeMonthRORAsOfMonthEnd(
			BigDecimal threeMonthRORAsOfMonthEnd) {
		this.threeMonthRORAsOfMonthEnd = threeMonthRORAsOfMonthEnd;
	}

	public BigDecimal getYtdRORAsOfMonthEnd() {
		return ytdRORAsOfMonthEnd;
	}

	public void setYtdRORAsOfMonthEnd(BigDecimal ytdRORAsOfMonthEnd) {
		this.ytdRORAsOfMonthEnd = ytdRORAsOfMonthEnd;
	}

	public String getMorningstarCategory() {
		return morningstarCategory;
	}

	public void setMorningstarCategory(String morningstarCategory) {
		this.morningstarCategory = morningstarCategory;
	}
	
	/**
	 * @return the threeYearSDAsOfQuaterEnd
	 */
	public BigDecimal getThreeYearSDAsOfQuaterEnd() {
		return threeYearSDAsOfQuaterEnd;
	}

	/**
	 * @param threeYearSDAsOfQuaterEnd the threeYearSDAsOfQuaterEnd to set
	 */
	public void setThreeYearSDAsOfQuaterEnd(BigDecimal threeYearSDAsOfQuaterEnd) {
		this.threeYearSDAsOfQuaterEnd = threeYearSDAsOfQuaterEnd;
	}

	/**
	 * @return the fiveYearSDAsOfQuaterEnd
	 */
	public BigDecimal getFiveYearSDAsOfQuaterEnd() {
		return fiveYearSDAsOfQuaterEnd;
	}

	/**
	 * @param fiveYearSDAsOfQuaterEnd the fiveYearSDAsOfQuaterEnd to set
	 */
	public void setFiveYearSDAsOfQuaterEnd(BigDecimal fiveYearSDAsOfQuaterEnd) {
		this.fiveYearSDAsOfQuaterEnd = fiveYearSDAsOfQuaterEnd;
	}

	/**
	 * @return the tenYearSDAsOfQuaterEnd
	 */
	public BigDecimal getTenYearSDAsOfQuaterEnd() {
		return tenYearSDAsOfQuaterEnd;
	}

	/**
	 * @param tenYearSDAsOfQuaterEnd the tenYearSDAsOfQuaterEnd to set
	 */
	public void setTenYearSDAsOfQuaterEnd(BigDecimal tenYearSDAsOfQuaterEnd) {
		this.tenYearSDAsOfQuaterEnd = tenYearSDAsOfQuaterEnd;
	}
	
}
