package com.manulife.pension.ps.web.onlineloans;

import java.math.BigDecimal;

import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanMoneyType;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;

/**
 * Used for loan calculation
 * @author ayyalsa
 *
 */
public class LoanCalculator {
	public static final BigDecimal HUNDRED = new BigDecimal(100.0);
	
	public static BigDecimal calculateVestedBalanceForMoneyType(LoanMoneyType loanMoneyType) {
		BigDecimal balance = null;
		if (loanMoneyType.getVestingPercentage() != null) {
			balance = (loanMoneyType.getAccountBalance().multiply(loanMoneyType.getVestingPercentage())).divide(HUNDRED);
		}
		return balance;
	}
	
	public static BigDecimal calculateVestedBalanceForMoneyTypeWithLoans(LoanMoneyType loanMoneyType) {
		if (loanMoneyType.getVestingPercentage() != null) {
			return loanMoneyType.getAccountBalance().add(
					loanMoneyType.getLoanBalance()).multiply(loanMoneyType.getVestingPercentage()).divide(HUNDRED);	    
	    } 
		return null;
	}
	
	public static BigDecimal calculateAvailableBalanceForMoneyType(LoanMoneyType loanMoneyType) {
		if (!loanMoneyType.getExcludeIndicator()) {
			return calculateVestedBalanceForMoneyType(loanMoneyType);
		} else {
			return BigDecimal.ZERO;
		}
	}

	public static BigDecimal calculateVestedAccountBalanceWithLoans(Loan loan) {
		BigDecimal vestedAccountBalance = BigDecimal.ZERO;
		BigDecimal balance = null;
		  
		for (LoanMoneyType loanMoneyType : loan.getMoneyTypesWithAccountBalance()) {
			balance = calculateVestedBalanceForMoneyTypeWithLoans(loanMoneyType);
		    if (balance != null) {
		    	vestedAccountBalance = vestedAccountBalance.add(balance);
		    } else {
		    	return null;
		    }
		}
		
		for (LoanMoneyType loanMoneyType : loan.getMoneyTypesWithoutAccountBalance()) {
			balance = calculateVestedBalanceForMoneyTypeWithLoans(loanMoneyType);
		    if (balance != null) {
		    	vestedAccountBalance = vestedAccountBalance.add(balance);
		    } else {
		    	return null;
		    }
		}
		return vestedAccountBalance;
	}
	
	public static BigDecimal calculateMaximumLoanAvailable(LoanPlanData loanPlanData, Loan loan) {
		
		BigDecimal maxResult = null;
		BigDecimal maxLoanAmount = loanPlanData.getMaximumLoanAmount();
		BigDecimal IRSLoanAmount = new BigDecimal(10000);

		if (maxLoanAmount == null || loan.getMaxBalanceLast12Months() == null || 
				loan.getCurrentOutstandingBalance() == null) {
			return null;
		}

		maxLoanAmount = maxLoanAmount.subtract(loan.getMaxBalanceLast12Months()).add(loan.getCurrentOutstandingBalance());
  
		BigDecimal vestedAccountBalance = calculateVestedAccountBalanceWithLoans(loan);
  
		if (vestedAccountBalance == null) {
			return null;
		}
  
		BigDecimal maxPercentLoanAmount = vestedAccountBalance.multiply(loanPlanData.getMaximumLoanPercentage()).divide(HUNDRED);
  
		if (loan.getApplyIrs10KDollarRuleInd() && maxPercentLoanAmount.compareTo(IRSLoanAmount) < 0) {
			maxPercentLoanAmount = IRSLoanAmount;
		}
  
		//lesser of maxLoanAmount or maxPercentLoanAmount
		if (maxLoanAmount.compareTo(maxPercentLoanAmount) < 0) {
			maxResult = maxLoanAmount;
		} else {
			maxResult = maxPercentLoanAmount;
		}
  
		maxResult = maxResult.subtract(loan.getCurrentOutstandingBalance());
  
		if (maxResult.compareTo(loanPlanData.getMinimumLoanAmount()) < 0) {
			maxResult = BigDecimal.ZERO;
		} else if (maxResult.compareTo(loanPlanData.getMaximumLoanAmount()) > 0) {
			maxResult = loanPlanData.getMaximumLoanAmount();
		}

		// return a round down version of the result.
		double value = Math.floor(maxResult.doubleValue() * 100)/100;
		maxResult = new BigDecimal(value);
  
		return maxResult;  
	}
}