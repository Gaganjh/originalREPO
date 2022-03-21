package com.manulife.pension.ps.service.report.investment.valueobject;

import java.util.Date;

public class ContractFundDetails {
	private String fundId;
	private String rateType;
	private Double unitValue;
	private String marketingOrder;
	private boolean selected;
	private Date endDate;
	

	public ContractFundDetails(String fundId,String marketingOrder,String rateType, String selectedFlag, Date endDate )
	{
		this.fundId =fundId;
		this.marketingOrder = marketingOrder;
		this.rateType = rateType;
		this.marketingOrder =marketingOrder;
		this.setSelected("Y".equalsIgnoreCase(selectedFlag.trim()));
		this.setEndDate(endDate);
	}
	public ContractFundDetails(String fundId,String rateType, Double unitValue)
	{
		this.fundId =fundId;
		this.rateType = rateType;
		this.unitValue =unitValue;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getFundId() {
		return fundId;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public String getRateType() {
		return rateType;
	}
	public void setUnitValue(Double unitValue) {
		this.unitValue = unitValue;
	}
	public Double getUnitValue() {
		return unitValue;
	}
	public void setMarketingOrder(String marketingOrder) {
		this.marketingOrder = marketingOrder;
	}
	public String getMarketingOrder() {
		return marketingOrder;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getEndDate() {
		return endDate;
	}

}
