package com.manulife.pension.ireports.model;

import java.util.Collection;

import com.manulife.pension.ireports.CoreToolGlobalData;
import com.manulife.pension.ireports.StandardReportsConstants;

public class Contract {
	private String productId;

	private String contractNumber;

	private String subsidiaryCode;

	private String rateType;

	private Collection funds;

	private String fundPackageSeries;

	public String getFundPackageSeries() {
		return fundPackageSeries;
	}

	public void setFundPackageSeries(String fundPackageSeries) {
		this.fundPackageSeries = fundPackageSeries;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public Collection getFunds() {
		return funds;
	}

	public void setFunds(Collection funds) {
		this.funds = funds;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSubsidiaryCode() {
		return subsidiaryCode;
	}

	public void setSubsidiaryCode(String subsidiaryCode) {
		this.subsidiaryCode = subsidiaryCode;
	}

	public boolean isDefinedBenefits() {
		return CoreToolGlobalData.definedBenefitContractProductIds.contains(this.productId);
	}

    public String getRateType() {
		return rateType;
	}

    public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	/**
	 * The new StandardReports ISF (Investment Selection Form) includes all funds, but some old contracts (i.e. Venture) are not allowed to chose all funds.
	 * 
	 * @return if this Contract is allowed to see our beautiful new form.
	 */
	public boolean isSupportedByISF() {
		boolean isVentureOrVRS = StandardReportsConstants.PACKAGE_SERIES_VRS.equals(getFundPackageSeries()) || StandardReportsConstants.PACKAGE_SERIES_VENTURE.equals(getFundPackageSeries());
		return !isVentureOrVRS;
	}

}
