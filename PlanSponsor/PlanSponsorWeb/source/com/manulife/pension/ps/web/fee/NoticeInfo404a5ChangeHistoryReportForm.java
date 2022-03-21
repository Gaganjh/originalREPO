package com.manulife.pension.ps.web.fee;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.contract.valueobject.ContractValidationDetail;
import com.manulife.pension.service.environment.valueobject.DeCodeVO;
import com.manulife.pension.service.fee.valueobject.FeeScheduleHistoryDetails;

/**
 * 
 * Form bean to store 404a5 notice info change history
 * 
 * @author Siby Thomas
 *
 */
public class NoticeInfo404a5ChangeHistoryReportForm extends ReportForm {
	
	private static final long serialVersionUID = 1L;
	
	private int tpaId;
	
	private String fromDate;
	private String toDate;
	private String selectedUserId;
	private String selectedFeeType;
	
	private FeeScheduleHistoryDetails historyDetails;
	
	private Map<String, String> displayNames = new HashMap<String, String>();
	private Map<String, String> allUsers = new HashMap<String, String>();

	private LinkedHashSet<DeCodeVO> tpaStandardFees = new LinkedHashSet<DeCodeVO>();
	private LinkedHashSet<DeCodeVO> tpaCustomFees = new LinkedHashSet<DeCodeVO>();
	private LinkedHashSet<DeCodeVO> nonTpaFees = new LinkedHashSet<DeCodeVO>();
	private LinkedHashSet<DeCodeVO> pbaStandardFees = new LinkedHashSet<DeCodeVO>();
	private LinkedHashSet<DeCodeVO> pbaCustomFees = new LinkedHashSet<DeCodeVO>();
	
	
	private HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisonHistory 
	= new HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>>();
	
	private LinkedList<DeCodeVO> customFees = new LinkedList<DeCodeVO>();
	
	private String standardScheduleApplied;
	
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
	
	public String getSelectedUserId() {
		return selectedUserId;
	}
	public void setSelectedUserId(String selectedUserId) {
		this.selectedUserId = selectedUserId;
	}
	
	public String getSelectedFeeType() {
		return StringUtils.trimToEmpty(selectedFeeType);
	}
	public void setSelectedFeeType(String selectedFeeType) {
		this.selectedFeeType = selectedFeeType;
	}
	
	public Map<String, String> getAllUsers() {
		return allUsers;
	}
	public void setAllUsers(Map<String, String> allUsers) {
		this.allUsers = allUsers;
	}
	
	public LinkedHashSet<DeCodeVO> getTpaStandardFees() {
		return tpaStandardFees;
	}
	public void setTpaStandardFees(LinkedHashSet<DeCodeVO> tpaStandardFees) {
		this.tpaStandardFees = tpaStandardFees;
	}
	
	public LinkedHashSet<DeCodeVO> getTpaCustomFees() {
		return tpaCustomFees;
	}
	public void setTpaCustomFees(LinkedHashSet<DeCodeVO> tpaCustomFees) {
		this.tpaCustomFees = tpaCustomFees;
	}
	
	public LinkedHashSet<DeCodeVO> getNonTpaFees() {
		return nonTpaFees;
	}
	public void setNonTpaFees(LinkedHashSet<DeCodeVO> nonTpaFees) {
		this.nonTpaFees = nonTpaFees;
	}
	
	public LinkedHashSet<DeCodeVO> getPbaStandardFees() {
		return pbaStandardFees;
	}
	public void setPbaStandardFees(LinkedHashSet<DeCodeVO> pbaStandardFees) {
		this.pbaStandardFees = pbaStandardFees;
	}
	public LinkedHashSet<DeCodeVO> getPbaCustomFees() {
		return pbaCustomFees;
	}
	public void setPbaCustomFees(LinkedHashSet<DeCodeVO> pbaCustomFees) {
		this.pbaCustomFees = pbaCustomFees;
	}
	public Map<String, String> getDisplayNames() {
		return displayNames;
	}
	public void setDisplayNames(Map<String, String> displayNames) {
		this.displayNames = displayNames;
	}
	
	public LinkedList<DeCodeVO> getCustomFees() {
		return customFees;
	}
	public void setCustomFees(LinkedList<DeCodeVO> customFees) {
		this.customFees = customFees;
	}
	
	public String getFeeDescription(String code) {
		if(StringUtils.isEmpty(code)) {
			return StringUtils.EMPTY;
		}
		for(DeCodeVO vo : getTpaStandardFees()) {
			if(StringUtils.equals(vo.getCode(), code)) {
				return vo.getDescription();
			}
		}
		for(DeCodeVO vo : getTpaCustomFees()) {
			if(StringUtils.equals(vo.getCode(), code)) {
				return vo.getDescription();
			}
		}
		for(DeCodeVO vo : getNonTpaFees()) {
			if(StringUtils.equals(vo.getCode(), code)) {
				return vo.getDescription();
			}
		}
		return StringUtils.EMPTY;
	}
	
	public String getStandardScheduleApplied() {
		return standardScheduleApplied;
	}
	public void setStandardScheduleApplied(String standardScheduleApplied) {
		this.standardScheduleApplied = standardScheduleApplied;
	}
	
	public int getTpaId() {
		return tpaId;
	}
	public void setTpaId(int tpaId) {
		this.tpaId = tpaId;
	}
	
	public FeeScheduleHistoryDetails getHistoryDetails() {
		return historyDetails;
	}
	public void setHistoryDetails(FeeScheduleHistoryDetails historyDetails) {
		this.historyDetails = historyDetails;
	}
	
	public HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> getPlanProvisonHistory() {
		return planProvisonHistory;
	}
	public void setPlanProvisonHistory(
			HashMap<ContractValidationDetail, TreeMap<Timestamp, Timestamp>> planProvisonHistory) {
		this.planProvisonHistory = planProvisonHistory;
	}
	
}