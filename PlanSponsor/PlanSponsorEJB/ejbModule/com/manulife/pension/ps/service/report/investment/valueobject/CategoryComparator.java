package com.manulife.pension.ps.service.report.investment.valueobject;

import java.util.Comparator;

public class CategoryComparator implements Comparator{
	public int compare(Object o1, Object o2){
		FundCategory cat1 = (FundCategory)o1;
		FundCategory cat2 = (FundCategory)o2;
		return (cat1.getSortOrder() - cat2.getSortOrder());
	}
}

