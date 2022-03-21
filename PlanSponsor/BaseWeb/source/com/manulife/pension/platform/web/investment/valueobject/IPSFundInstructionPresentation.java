package com.manulife.pension.platform.web.investment.valueobject;

import java.io.Serializable;
import java.util.List;

import com.manulife.pension.service.ipsr.valueobject.FromFundVO;
import com.manulife.pension.service.ipsr.valueobject.ToFundVO;

public class IPSFundInstructionPresentation implements Serializable, Comparable {

	private static final long serialVersionUID = 1L;
	private int reviewRequestId;
	private String assetClass;
	private String actionIndicator;
	private String assetClassName;
	private boolean isActionEnabled;
	private List<FromFundVO> fromFundVO;
	private List<ToFundVO> toFundVO;
	private int assetClsOrder;
	private boolean noActionTaken;

	public int getReviewRequestId() {
		return reviewRequestId;
	}

	public void setReviewRequestId(int reviewRequestId) {
		this.reviewRequestId = reviewRequestId;
	}

	public String getAssetClass() {
		return assetClass;
	}

	public void setAssetClass(String assetClass) {
		this.assetClass = assetClass;
	}

	public String getActionIndicator() {
		return actionIndicator;
	}

	public void setActionIndicator(String actionIndicator) {
		this.actionIndicator = actionIndicator;
	}

	public String getAssetClassName() {
		return assetClassName;
	}

	public void setAssetClassName(String assetClassName) {
		this.assetClassName = assetClassName;
	}

	public boolean isActionEnabled() {
		return isActionEnabled;
	}

	public void setActionEnabled(boolean isActionEnabled) {
		this.isActionEnabled = isActionEnabled;
	}

	public List<ToFundVO> getToFundVO() {
		return toFundVO;
	}

	public void setToFundVO(List<ToFundVO> toFundVO) {
		this.toFundVO = toFundVO;
	}

	public int getAssetClsOrder() {
		return assetClsOrder;
	}

	public void setAssetClsOrder(int assetClsOrder) {
		this.assetClsOrder = assetClsOrder;
	}

	public int compareTo(Object o) {
		IPSFundInstructionPresentation that = (IPSFundInstructionPresentation) o;
		return this.getAssetClsOrder() - that.getAssetClsOrder();
	}

	public boolean isNoActionTaken() {
		return noActionTaken;
	}

	public void setNoActionTaken(boolean noActionTaken) {
		this.noActionTaken = noActionTaken;
	}

	public List<FromFundVO> getFromFundVO() {
		return fromFundVO;
	}

	public void setFromFundVO(List<FromFundVO> fromFundVO) {
		this.fromFundVO = fromFundVO;
	}
}
