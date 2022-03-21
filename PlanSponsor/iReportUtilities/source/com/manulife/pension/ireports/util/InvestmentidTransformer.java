package com.manulife.pension.ireports.util;

import org.apache.commons.collections.Transformer;

import com.manulife.pension.service.fund.standardreports.valueobject.FundSortable;

/**
 * Get investmentid from Fund
 */
public class InvestmentidTransformer implements Transformer {
	public Object transform(Object fund) {
		return ((FundSortable)fund).getInvestmentid();
	}
}