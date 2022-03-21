package com.manulife.pension.bd.web.bob.planReview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;
import com.manulife.pension.service.planReview.valueobject.ShippingVO;

public class PlanReviewPrintForm extends PlanReviewReportForm{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<PlanReviewReportUIHolder> planPrintRequestedVOList = new ArrayList<PlanReviewReportUIHolder>();
    private ShippingVO newShippingVO;
    private ShippingVO defaultBrokerShippingAddress;
	boolean empty;
	boolean defaultAddress = true;
	private String contractIdList = null;
	
	Map<String, String> usaStates = new LinkedHashMap<String, String>();
	Map<String, String> canadianProvinces = new LinkedHashMap<String, String>();
	
	Map<String, String> countryMap = new LinkedHashMap<String, String>();

	public boolean isDefaultAddress() {
		return defaultAddress;
	}

	public void setDefaultAddress(boolean defaultAddress) {
		this.defaultAddress = defaultAddress;
	}

	private ShippingVO shippingVO;
	
	private List<PrintDocumentPackgeVo> printDetailsRecord;  
	
	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public ShippingVO getNewShippingVO() {
		return newShippingVO;
	}

	public void setNewShippingVO(ShippingVO newShippingVO) {
		this.newShippingVO = newShippingVO;
	}

	public ShippingVO getShippingVO() {
		return shippingVO;
	}

	public void setShippingVO(ShippingVO shippingVO) {
		this.shippingVO = shippingVO;
	}
	
	public List<PrintDocumentPackgeVo> getPrintDetailsRecord() {
		return printDetailsRecord;
	}

	public void setPrintDetailsRecord(List<PrintDocumentPackgeVo> printDetailsRecord) {
		this.printDetailsRecord = printDetailsRecord;
	}

	public List<PlanReviewReportUIHolder> getPlanPrintRequestedVOList() {
		return planPrintRequestedVOList;
	}

	public void setPlanPrintRequestedVOList(
			List<PlanReviewReportUIHolder> planPrintRequestedVOList) {
		this.planPrintRequestedVOList = planPrintRequestedVOList;
	}

	public ShippingVO getDefaultBrokerShippingAddress() {
		return defaultBrokerShippingAddress;
	}

	public void setDefaultBrokerShippingAddress(
			ShippingVO defaultBrokerShippingAddress) {
		this.defaultBrokerShippingAddress = defaultBrokerShippingAddress;
	}

	public Map<String, String> getUsaStates() {
		return usaStates;
	}

	public void setUsaStates(Map<String, String> usaStates) {
		this.usaStates = usaStates;
	}

	public Map<String, String> getCanadianProvinces() {
		return canadianProvinces;
	}

	public void setCanadianProvinces(Map<String, String> canadianProvinces) {
		this.canadianProvinces = canadianProvinces;
	}

	public Map<String, String> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(Map<String, String> countryMap) {
		this.countryMap = countryMap;
	}

	public String getContractIdList() {
		return contractIdList;
	}

	public void setContractIdList(String contractIdList) {
		this.contractIdList = contractIdList;
	}
	
}
