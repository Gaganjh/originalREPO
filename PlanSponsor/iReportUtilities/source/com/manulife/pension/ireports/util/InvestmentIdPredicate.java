package com.manulife.pension.ireports.util;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.fund.standardreports.valueobject.FundSortable;

/**
 * True if fund.investmentid matches.
 */
public class InvestmentIdPredicate implements Predicate {
	private String investmentid;

	public InvestmentIdPredicate(String investementid) {
		if (StringUtils.isBlank(investementid)) {
			throw new IllegalArgumentException("investmentid required");
		}
		this.investmentid = investementid;
	}

	public boolean evaluate(Object fund) {
		return investmentid.equals(((FundSortable)fund).getInvestmentid());
	}

}
