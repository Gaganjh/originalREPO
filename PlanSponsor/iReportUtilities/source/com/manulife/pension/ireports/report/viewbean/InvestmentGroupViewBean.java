package com.manulife.pension.ireports.report.viewbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.service.fund.standardreports.valueobject.InvestmentGroup;

public class InvestmentGroupViewBean {

	private final List reportFunds = new ArrayList();
	private final List gicFunds = new ArrayList();
	private InvestmentGroup investmentGroup;
	private boolean isMarketIndex;
	
	public InvestmentGroupViewBean(InvestmentGroup group) {
		investmentGroup = group;
	}

	public InvestmentGroup getInvestmentGroup() {
		return investmentGroup;
	}

	public void setInvestmentGroup(InvestmentGroup investmentGroup) {
		this.investmentGroup = investmentGroup;
	}

	public String getColorcode() {
		return investmentGroup.getColorcode();
	}

	public String getFontcolor() {
		return investmentGroup.getFontcolor();
	}

	public String getGroupname() {
		return investmentGroup.getGroupname();
	}

	public int getOrder() {
		return investmentGroup.getOrder();
	}
	
	public void addFund(ReportFund fund) {
		reportFunds.add(fund);
	}

	public void addGICFund(ReportFund fund) {
		gicFunds.add(fund);
	}

	public void setMarketIndex(boolean isMarketIndex) {
		this.isMarketIndex = isMarketIndex;
	}
	
	public boolean isMarketIndex() {
		return isMarketIndex;
	}
		
	public boolean isConservative() {
		return investmentGroup.isConservative();
	}
	
	public boolean hasGICs() {
		return !gicFunds.isEmpty();
	}
	
	public List getFunds() {
		return Collections.unmodifiableList(reportFunds);
	}

	public List getGICFunds() {
		return Collections.unmodifiableList(gicFunds);
	}
}
