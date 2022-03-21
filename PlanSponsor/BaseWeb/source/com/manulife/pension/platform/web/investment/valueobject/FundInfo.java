package com.manulife.pension.platform.web.investment.valueobject;

import java.util.List;

import com.manulife.pension.service.ipsr.valueobject.FromFundVO;
import com.manulife.pension.service.ipsr.valueobject.ToFundVO;


/**
 * This VO class is used in FRW-IPSReviewResult page PDF generation.
 * 
 * @author Vellaisamy S
 *
 */
public class FundInfo {
	
	private String assetClassName;
	private String currentFundName;
	private List<ToFundVO> topFundInfo;
	private String actionIndicator;
	private List<FromFundVO> currentFundInfo;
	private List<String> actionIndicators;
	
	
	/**
	 * @return assetClassName
	 */
	public String getAssetClassName() {
		return assetClassName;
	}
	
	/**
	 * @param assetClassName
	 * 				the assetClassName to set.
	 */
	public void setAssetClassName(String assetClassName) {
		this.assetClassName = assetClassName;
	}
	
	/**
	 * @return currentFundName
	 */
	public String getCurrentFundName() {
		return currentFundName;
	}
	
	/**
	 * @param currentFundName
	 * 				the currentFundName to set.
	 */
	public void setCurrentFundName(String currentFundName) {
		this.currentFundName = currentFundName;
	}
	
	/**
	 * @return the topFundInfo
	 */
	public List<ToFundVO> getTopFundInfo() {
		return topFundInfo;
	}

	/**
	 * @param topFundInfo the topFundInfo to set
	 */
	public void setTopFundInfo(List<ToFundVO> topFundInfo) {
		this.topFundInfo = topFundInfo;
	}

	/**
	 * @return actionIndicator
	 */
	public String getActionIndicator() {
		return actionIndicator;
	}
	
	/**
	 * @param actionIndicator
	 * 			the actionIndicator to set. 
	 */
	public void setActionIndicator(String actionIndicator) {
		this.actionIndicator = actionIndicator;
	}

	/**
	 * @return the currentFundInfo
	 */
	public List<FromFundVO> getCurrentFundInfo() {
		return currentFundInfo;
	}

	/**
	 * @param currentFundInfo the currentFundInfo to set
	 */
	public void setCurrentFundInfo(List<FromFundVO> currentFundInfo) {
		this.currentFundInfo = currentFundInfo;
	}

	/**
	 * @return actionIndicators
	 */
	public List<String> getActionIndicators() {
		return actionIndicators;
	}

	/**
	 * @param actionIndicators
	 * 			the actionIndicators to set.
	 */
	public void setActionIndicators(List<String> actionIndicators) {
		this.actionIndicators = actionIndicators;
	}
}
