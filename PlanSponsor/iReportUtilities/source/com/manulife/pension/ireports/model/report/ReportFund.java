package com.manulife.pension.ireports.model.report;

import com.manulife.pension.ireports.StandardReportsConstants;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.FundExpenses;
import com.manulife.pension.service.fund.standardreports.valueobject.FundMetrics;
import com.manulife.pension.service.fund.standardreports.valueobject.FundSortable;
import com.manulife.pension.service.fund.standardreports.valueobject.FundStandardDeviation;

public class ReportFund implements StandardReportsConstants, FundSortable {
	private Fund fund;
	private FundMetrics fundMetrics;
	private FundStandardDeviation fundStandardDeviation;
	private FundExpenses fundExpenses;
	private String[] footnoteSymbols;

	public ReportFund(Fund fund) {
		this.fund = fund;
	}

	private ReportFund() {
		// not used
	}

	public String[] getFootnoteSymbols() {
		return footnoteSymbols;
	}
	public void setFootnoteSymbols(String[] footnoteSymbols) {
		this.footnoteSymbols = footnoteSymbols;
	}

	public Fund getFund() {
		return fund;
	}
	public void setFund(Fund fund) {
		this.fund = fund;
	}

	public FundMetrics getFundMetrics() {
		return fundMetrics;
	}
	public void setFundMetrics(FundMetrics fundMetrics) {
		this.fundMetrics = fundMetrics;
	}
	
	public FundStandardDeviation getFundStandardDeviation() {
		return fundStandardDeviation;
	}
	public void setFundStandardDeviation(FundStandardDeviation fundStandardDeviation) {
		this.fundStandardDeviation = fundStandardDeviation;
	}
	
	public FundExpenses getFundExpenses() {
		return fundExpenses;
	}
	public void setFundExpenses(FundExpenses fundExpenses) {
		this.fundExpenses = fundExpenses;
	}
	
	public String getFundname() {
		return fund.getFundname();
	}

	public int getSortnumber() {
		return fund.getSortnumber();
	}

	public Integer getAssetclsOrder() {
		return fund.getAssetclsOrder();
	}

	public String getInvestmentid() {
		return fund.getInvestmentid();
	}

	public int getOrder() {
		return fund.getOrder();
	}
	
	public String toString() {
		return "ReportFund:" + getInvestmentid();
	}
}
