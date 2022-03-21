package com.manulife.pension.ireports.report.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.viewbean.InvestmentGroupViewBean;
import com.manulife.pension.service.fund.standardreports.valueobject.Fund;
import com.manulife.pension.service.fund.standardreports.valueobject.InvestmentGroup;

public class RiskReturnCollator {

	private Collection funds;
	private Map investmentGroupLookupTable;
	public RiskReturnCollator(Collection funds, Map investmentGroupLookupTable) {
		this.funds = funds;
		this.investmentGroupLookupTable = investmentGroupLookupTable;
	}

	public List collate() {
		List result = new ArrayList();
		int lastOrder = -1;
		InvestmentGroupViewBean currentGroup = null;
		if (funds == null) {
			return Collections.EMPTY_LIST;
		}
		for (Iterator iter = funds.iterator(); iter.hasNext();) {
			ReportFund reportFund = (ReportFund) iter.next();
			Fund fund = reportFund.getFund();
			int order = fund.getOrder();
			if (currentGroup == null || order != lastOrder) {
				InvestmentGroup investmentGroup = 
					(InvestmentGroup) investmentGroupLookupTable.get(new Integer(order));
				currentGroup = new InvestmentGroupViewBean(investmentGroup);
				result.add(currentGroup);
			}
			if (fund.isMarketIndex() && !currentGroup.isMarketIndex()){
				currentGroup.setMarketIndex(true);
			}
			lastOrder = order;
			if(fund.isGuaranteedAccount()) {
				currentGroup.addGICFund(reportFund);
			} else {
				currentGroup.addFund(reportFund);
			}
		}
		
		return result;
	}

}
