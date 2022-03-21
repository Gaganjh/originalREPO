package com.manulife.pension.ireports.report.viewbean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.set.ListOrderedSet;

import com.manulife.pension.ireports.model.report.ReportFund;
import com.manulife.pension.ireports.report.util.AssetHouseSubGroup;
/**
 *  Business terminology for this item is Style Box
 */
public class AssetHouseSubGroupViewBean implements Cloneable {

	private final AssetHouseSubGroup subGroup;
	private final List funds = new ArrayList();
	private final List gicFunds = new ArrayList();
	private final Set marketIndexPerformances = new ListOrderedSet();
	private final Set morningstarPerformances = new ListOrderedSet();

	public AssetHouseSubGroupViewBean(AssetHouseSubGroup subGroup) {
		this.subGroup = subGroup;
	}
	
	public List getFunds() {
		return Collections.unmodifiableList(funds);
	}
	
	public void addFund(ReportFund reportFund) {
		funds.add(reportFund);
	}
	
	public boolean hasFunds() {
		return funds != null && !funds.isEmpty();
	}
	
	public int getNumberOfFunds() {
		if (funds != null) {
			return funds.size();
		}
		return 0;
	}

	public List getGICFunds() {
		return Collections.unmodifiableList(gicFunds);
	}

	public void addGICFund(ReportFund fund) {
		gicFunds.add(fund);
	}

	public boolean hasGICFunds() {
		return gicFunds != null && !gicFunds.isEmpty();
	}
	
	public int getNumberOfGICFunds() {
		if (gicFunds != null) {
			return gicFunds.size();
		}
		return 0;
	}
	
	public String getName() {
		return subGroup.getName();
	}
	
	public int getOrder() {
		return subGroup.getOrder();
	}

	public void addMarketIndexPerformance(MarketIndexIbPerformanceViewBean performance) {
		marketIndexPerformances.add(performance);
	}

	public void addMorningstarCategoryPerformance(MorningstarCategoryPerformanceViewBean performance) {
		morningstarPerformances.add(performance);
	}

	public Set getMarketIndexIbPerformances() {
		return Collections.unmodifiableSet(marketIndexPerformances);
	}

	public Set getMorningstarCategoryPerformances() {
		return Collections.unmodifiableSet(morningstarPerformances);
	}

	public AssetHouseSubGroup getSubGroup() {
		return subGroup;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AssetHouseSubGroupViewBean)) {
			return false;
		}
		AssetHouseSubGroupViewBean otherSubGroup = (AssetHouseSubGroupViewBean)obj;
		if (this.subGroup == null || otherSubGroup.subGroup == null) {
			return false;
		}
		return this.subGroup.equals(otherSubGroup.subGroup);
	}
	
	public int hashCode() {
		if (subGroup == null) {
			return -1; 
		}
		return subGroup.hashCode();
	}

}
