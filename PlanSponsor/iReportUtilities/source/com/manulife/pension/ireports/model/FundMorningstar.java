package com.manulife.pension.ireports.model;

import com.manulife.pension.ireports.StandardReportsConstants;


public class FundMorningstar implements StandardReportsConstants {
	private String fundCd;
	private String securityId;
	private String ticker;
	private String underlyingFundName;
	private String shareClassType;
	private String morningstarRating;
	private String marketIndexId;
	private String morningstarCategoryId;
	private String assetCategoryId;
	
	public String getAssetCategoryId() {
		return assetCategoryId;
	}
	public void setAssetCategoryId(String assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}
	public String getFundCd() {
		return fundCd;
	}
	public void setFundCd(String fundCd) {
		this.fundCd = fundCd;
	}
	public String getMarketIndexId() {
		return marketIndexId;
	}
	public void setMarketIndexId(String marketIndexId) {
		this.marketIndexId = marketIndexId;
	}
	public String getMorningstarCategoryId() {
		return morningstarCategoryId;
	}
	public void setMorningstarCategoryId(String morningstarCategoryId) {
		this.morningstarCategoryId = morningstarCategoryId;
	}
	public String getMorningstarRating() {
		return morningstarRating;
	}
	public void setMorningstarRating(String morningstarRating) {
		this.morningstarRating = morningstarRating;
	}
	public String getSecurityId() {
		return securityId;
	}
	public void setSecurityId(String securityId) {
		this.securityId = securityId;
	}
	public String getShareClassType() {
		return shareClassType;
	}
	public void setShareClassType(String shareClassType) {
		this.shareClassType = shareClassType;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getUnderlyingFundName() {
		return underlyingFundName;
	}
	public void setUnderlyingFundName(String underlyingFundName) {
		this.underlyingFundName = underlyingFundName;
	}
}
