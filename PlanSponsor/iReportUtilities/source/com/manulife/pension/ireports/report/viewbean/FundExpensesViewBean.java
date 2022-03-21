package com.manulife.pension.ireports.report.viewbean;


import com.manulife.pension.ireports.report.model.NullFundExpenses;
import com.manulife.pension.ireports.util.StandardReportsUtils;
import com.manulife.pension.service.fund.standardreports.valueobject.FundExpenses;

public class FundExpensesViewBean {

	public static final NullFundExpenses NULL_FUND_EXPENSES = new NullFundExpenses();
	
	private FundExpenses expenses;

	public FundExpensesViewBean(FundExpenses expenses) {
		this.expenses = expenses;
		if (expenses == null) {
			this.expenses = NULL_FUND_EXPENSES;
		}
	}

	public String getAnnualInvestmentCharge() {
		return StandardReportsUtils.formatPercentage(expenses.getAnnualInvestmentCharge());
	}

	public String getSalesAndServiceFee() {
        return StandardReportsUtils.formatPercentage(expenses.getSalesAndServiceFee());
    }
	
	public String getEffectiveDate() {
		return StandardReportsUtils.formatFundDataDate(expenses.getEffectiveDate());
	}

	public String getFundCd() {
		return expenses.getFundCd();
	}

	public String getFundExpenseRatio() {
		return StandardReportsUtils.formatPercentage(expenses.getFundExpenseRatio());
	}

	public String getInvestmentid() {
		return expenses.getInvestmentid();
	}

	public String getRateType() {
		return StandardReportsUtils.formatFundDataString(expenses.getRateType());
	}
	
	public String getMaintenanceCharge() {
		return StandardReportsUtils.formatPercentage(expenses.getAmc());
	}
	
	public String getUnderlyingFundNetCost() {
	       return StandardReportsUtils.formatPercentage(expenses.getUnderlyingFundNetCost());
	}
	
	public String getRevenueFromUnderlyingFund() {
	    return StandardReportsUtils.formatPercentage(expenses.getRevenueFromUnderlyingFund());
	}
    public String getRevenueFromSubAccount() {
        return StandardReportsUtils.formatPercentage(expenses.getRevenueFromSubAccount());
    }
    public String getTotalRevenueUsedTowardsPlanCosts() {
        return StandardReportsUtils.formatPercentage(expenses.getTotalRevenueUsedTowardsPlanCosts());
    }
}
