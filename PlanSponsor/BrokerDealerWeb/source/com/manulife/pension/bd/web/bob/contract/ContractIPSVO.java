package com.manulife.pension.bd.web.bob.contract;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.manulife.pension.service.contract.valueobject.DayOfYear;
import com.manulife.pension.util.Pair;

/**
 * The value object to hold the information for the contract IPS information
 * 
 * @author guweigu
 *
 */
public class ContractIPSVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// whether if IPSR service is available
	private boolean ipsAvailable;

	// this contains MMDD
	private DayOfYear annualReviewDate;

	// The sorted ips criteria weighting list
	List<Pair<String, Integer>> ipsWeightingList;

	public ContractIPSVO() {
		ipsAvailable = false;
		annualReviewDate = null;
		ipsWeightingList = null;
	}

	public boolean isIpsAvailable() {
		return ipsAvailable;
	}

	public void setIpsAvailable(boolean ipsAvailable) {
		this.ipsAvailable = ipsAvailable;
	}

	public DayOfYear getAnnualReviewDate() {
		return annualReviewDate;
	}

	public void setAnnualReviewDate(DayOfYear annualReviewDate) {
		this.annualReviewDate = annualReviewDate;
	}

	public List<Pair<String, Integer>> getIpsWeightingList() {
		return ipsWeightingList;
	}

	public void setIpsWeightingList(List<Pair<String, Integer>> ipsWeightingList) {
		this.ipsWeightingList = ipsWeightingList;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}
}
