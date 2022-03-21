package com.manulife.pension.ps.web.tpafeeschedule;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.fee.valueobject.FeeDataVO;

public class TpaStandardFeeScheduleChangeHistoryReportForm extends ReportForm {
	
	private String asOfDate;
	private String fromDate;
	private String toDate;
	private String userName;
	private String tpaFirmId;
	private Map<String, String> userNames = new HashMap<String, String>();
	private String feeType;
	private Map<String, String> feeTypes = new LinkedHashMap<String, String>();
	
	
	public String getAsOfDate() {
		return asOfDate;
	}
	public void setAsOfDate(String asOfDate) {
		this.asOfDate = asOfDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Map<String, String> getUserNames() {
		return userNames;
	}
	public void setUserNames(Map<String, String> userNames) {
		this.userNames = userNames;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public Map<String, String> getFeeTypes() {
		return feeTypes;
	}
	public void setFeeTypes(Map<String, String> feeTypes) {
		this.feeTypes = feeTypes;
	}
	
	
	// TODO - Useless
	private List<FeeDataVO> changeHistoryDefaultList;
	
	
	public List<FeeDataVO> getChangeHistoryDefaultList() {
		return changeHistoryDefaultList;
	}
	public void setChangeHistoryDefaultList(List<FeeDataVO> changeHistoryDefaultList) {
		this.changeHistoryDefaultList = changeHistoryDefaultList;
	}
	/**
	 * @return the tpaFirmId
	 */
	public String getTpaFirmId() {
		return tpaFirmId;
	}
	/**
	 * @param tpaFirmId the tpaFirmId to set
	 */
	public void setTpaFirmId(String tpaFirmId) {
		this.tpaFirmId = tpaFirmId;
	}
	
	
	
}
