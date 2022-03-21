package com.manulife.pension.ps.web.onlineloans;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;

/**
 * Used for Loan calculation in PDF
 * @author ayyalsa
 *
 */
public class LoanMoneyTypeCalculation {

	private Map<String, String> vestedBalanceMap = new HashMap<String, String>();
	private Map<String, String> availableBalanceMap = new HashMap<String, String>();
	private String maxLoanAvailableSpan = StringUtils.EMPTY;
	private String calculatorMaxLoanAvailableSpan = StringUtils.EMPTY;
	private String totalVestedBalance = StringUtils.EMPTY;
	private String totalAvailableBalance = StringUtils.EMPTY;
	private String totalAccountBalance = StringUtils.EMPTY;

	/**
	 * @return the vestedBalanceMap
	 */
	public Map<String, String> getVestedBalanceMap() {
		return vestedBalanceMap;
	}

	/**
	 * @param vestedBalanceMap the vestedBalanceMap to set
	 */
	public void setVestedBalanceMap(Map<String, String> vestedBalanceMap) {
		this.vestedBalanceMap = vestedBalanceMap;
	}

	/**
	 * @return the availableBalanceMap
	 */
	public Map<String, String> getAvailableBalanceMap() {
		return availableBalanceMap;
	}

	/**
	 * @param availableBalanceMap the availableBalanceMap to set
	 */
	public void setAvailableBalanceMap(Map<String, String> availableBalanceMap) {
		this.availableBalanceMap = availableBalanceMap;
	}

	/**
	 * @return the totalAccountBalance
	 */
	public String getTotalAccountBalance() {
		return totalAccountBalance;
	}

	/**
	 * @param totalAccountBalance the totalAccountBalance to set
	 */
	public void setTotalAccountBalance(String totalAccountBalance) {
		this.totalAccountBalance = totalAccountBalance;
	}

	/**
	 * @return the maxLoanAvailableSpan
	 */
	public String getMaxLoanAvailableSpan() {
		return maxLoanAvailableSpan;
	}

	/**
	 * @param maxLoanAvailableSpan the maxLoanAvailableSpan to set
	 */
	public void setMaxLoanAvailableSpan(String maxLoanAvailableSpan) {
		this.maxLoanAvailableSpan = maxLoanAvailableSpan;
	}

	/**
	 * @return the calculatorMaxLoanAvailableSpan
	 */
	public String getCalculatorMaxLoanAvailableSpan() {
		return calculatorMaxLoanAvailableSpan;
	}

	/**
	 * @param calculatorMaxLoanAvailableSpan the calculatorMaxLoanAvailableSpan to set
	 */
	public void setCalculatorMaxLoanAvailableSpan(
			String calculatorMaxLoanAvailableSpan) {
		this.calculatorMaxLoanAvailableSpan = calculatorMaxLoanAvailableSpan;
	}

	/**
	 * @return the totalVestedBalance
	 */
	public String getTotalVestedBalance() {
		return totalVestedBalance;
	}

	/**
	 * @param totalVestedBalance the totalVestedBalance to set
	 */
	public void setTotalVestedBalance(String totalVestedBalance) {
		this.totalVestedBalance = totalVestedBalance;
	}

	/**
	 * @return the totalAvailableBalance
	 */
	public String getTotalAvailableBalance() {
		return totalAvailableBalance;
	}

	/**
	 * @param totalAvailableBalance the totalAvailableBalance to set
	 */
	public void setTotalAvailableBalance(String totalAvailableBalance) {
		this.totalAvailableBalance = totalAvailableBalance;
	}

	public String format(BigDecimal value, boolean showDollarSign){
		NumberFormat currencyNumberFormat = NumberFormat.getCurrencyInstance();
		DecimalFormat df = (DecimalFormat)currencyNumberFormat;
		DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
		if (!showDollarSign) {
			dfs.setCurrencySymbol(StringUtils.EMPTY);
		}
		df.setDecimalFormatSymbols(dfs);
		currencyNumberFormat.setMaximumFractionDigits(2);
		currencyNumberFormat.setMinimumFractionDigits(2);
		return currencyNumberFormat.format(value);
	}
	
	/**
	 * Calculates and updates the maximum loan available balance and
	 * all vested balances and available balances along the way.
	 *
	 */
	public void recalculateBalances(boolean limitMaxLoanAmtToAvailableAccountBalance, Loan loan, LoanPlanData loanPlanData) {
	  
		BigDecimal totAccountBalance = BigDecimal.ZERO;
		BigDecimal totVestedBalance = BigDecimal.ZERO;
		BigDecimal totAvailableBalance = BigDecimal.ZERO;
		boolean vestedBalanceNotExist = false;
		boolean availableBalanceNotExist = false;
	  
		for (LoanMoneyType loanMoneyType : loan.getMoneyTypesWithAccountBalance()){
			BigDecimal vestedBalance;
			BigDecimal availableBalance;
			String vestedBalanceValue = StringUtils.EMPTY;
			String availableBalanceValue = StringUtils.EMPTY;
		  
			vestedBalance = LoanCalculator.calculateVestedBalanceForMoneyType(loanMoneyType);
			availableBalance = LoanCalculator.calculateAvailableBalanceForMoneyType(loanMoneyType);
			String vestedBalanceKey = "vestedBalance_" + loanMoneyType.getMoneyTypeId();
			String availableBalanceKey = "availableBalance_" + loanMoneyType.getMoneyTypeId();
    
		    if (vestedBalance != null) {
		    	vestedBalanceValue = format(vestedBalance, false);
		    	totVestedBalance = totVestedBalance.add(vestedBalance.setScale(2, BigDecimal.ROUND_HALF_UP));
		    } else {
		    	vestedBalanceNotExist = true;
		    }
		    
		    if (availableBalance != null) {
		    	availableBalanceValue = format(availableBalance, false);
		    	totAvailableBalance = totAvailableBalance.add(availableBalance.setScale(2, BigDecimal.ROUND_HALF_UP));
		    } else {
		    	availableBalanceNotExist = true;
		    }
    
		    totAccountBalance = totAccountBalance.add(loanMoneyType.getAccountBalance());
		    
		    vestedBalanceMap.put(vestedBalanceKey, vestedBalanceValue);
		    availableBalanceMap.put(availableBalanceKey, availableBalanceValue);
		}
  
		BigDecimal maxLoanAvailable = LoanCalculator.calculateMaximumLoanAvailable(loanPlanData, loan);

		if (limitMaxLoanAmtToAvailableAccountBalance) {
			if (maxLoanAvailable != null && !availableBalanceNotExist && maxLoanAvailable.compareTo(totAvailableBalance) > 0) {
				maxLoanAvailable = totAvailableBalance;
			}	
		}
  
		if (maxLoanAvailable != null) {
			String formattedAmount = format(maxLoanAvailable, true);
			this.maxLoanAvailableSpan = formattedAmount;
			this.calculatorMaxLoanAvailableSpan = formattedAmount;
		}

		if (!vestedBalanceNotExist) {
			this.totalVestedBalance = format(totVestedBalance, false);
		}

		if (!availableBalanceNotExist) {
			this.totalAvailableBalance = format(totAvailableBalance, false);
		}
  
		this.totalAccountBalance = format(totAccountBalance, false);
	}
}
