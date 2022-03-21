package com.manulife.pension.ps.web.census;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.environment.valueobject.LabelValueBean;

public class EligibilityReportsForm extends ReportForm {

	private List<LabelValueBean> moneyTypes ;
	private String histroyPlanEntryDate ;
	private List<String> historyPlanEntryDatesList;
	private boolean isEligibiltyCalcOn ;
	private boolean isEZstartOn;
	private List<LabelValueBean> moneyTypesHoS ;
	private List<String> eligibilityChangePEDsList;
	private Map<String, List<LabelValueBean>> moneyTypePEDMap;
	private String reportedPED ;
	private List<String> changeReportMoneyTypes ;
	private String selectedMoneyType ;
	private int optOutDays;
	private boolean DMContract; 
	private boolean hasPayrollNumberFeature = true;
	private boolean hasDivisionFeature = true;
	private String initialEnrollmentDate ;
	private boolean hasIED;
	private Map<String,String> moneyTypeIdName ;
	private List<LabelValueBean> histDatesAndCreatedTS ;
	private String eedefShortName = ""; 
	private String reportedFromDate;
	private String reportedToDate;
	
	public String getHistroyPlanEntryDate() {
		return histroyPlanEntryDate;
	}

	public void setHistroyPlanEntryDate(String histroyPlanEntryDate) {
		this.histroyPlanEntryDate = histroyPlanEntryDate;
	}

	public List<LabelValueBean> getMoneyTypes() {
		return moneyTypes;
	}

	public void setMoneyTypes(List<LabelValueBean> moneyTypes) {
		this.moneyTypes = moneyTypes;
	}

	public List<String> getHistoryPlanEntryDatesList() {
		return historyPlanEntryDatesList;
	}

	public void setHistoryPlanEntryDatesList(List<String> historyPlanEntryDatesList) {
		this.historyPlanEntryDatesList = historyPlanEntryDatesList;
	}

	public boolean isEligibiltyCalcOn() {
		return isEligibiltyCalcOn;
	}

	public void setEligibiltyCalcOn(boolean isEligibiltyCalcOn) {
		this.isEligibiltyCalcOn = isEligibiltyCalcOn;
	}

	public boolean isEZstartOn() {
		return isEZstartOn;
	}

	public void setEZstartOn(boolean isEZstartOn) {
		this.isEZstartOn = isEZstartOn;
	}

	public List<LabelValueBean> getMoneyTypesHoS() {
		return moneyTypesHoS;
	}

	public void setMoneyTypesHoS(List<LabelValueBean> moneyTypesHoS) {
		this.moneyTypesHoS = moneyTypesHoS;
	}

	public boolean isDMContract() {
		return DMContract;
	}

	public void setDMContract(boolean contract) {
		DMContract = contract;
	}

	public List<String> getEligibilityChangePEDsList() {
		return eligibilityChangePEDsList;
	}

	public void setEligibilityChangePEDsList(List<String> eligibilityChangePEDsList) {
		this.eligibilityChangePEDsList = eligibilityChangePEDsList;
	}

	public Map<String, List<LabelValueBean>> getMoneyTypePEDMap() {
		return moneyTypePEDMap;
	}

	public void setMoneyTypePEDMap(Map<String, List<LabelValueBean>> moneyTypePEDMap) {
		this.moneyTypePEDMap = moneyTypePEDMap;
	}

	public String getReportedPED() {
		return reportedPED;
	}

	public void setReportedPED(String reportedPED) {
		this.reportedPED = reportedPED;
	}

	public List<String> getChangeReportMoneyTypes() {
		return changeReportMoneyTypes;
	}

	public void setChangeReportMoneyTypes(List<String> changeReportMoneyTypes) {
		this.changeReportMoneyTypes = changeReportMoneyTypes;
	}

	public String getSelectedMoneyType() {
		return selectedMoneyType;
	}

	public void setSelectedMoneyType(String selectedMoneyType) {
		this.selectedMoneyType = selectedMoneyType;
	}

	public int getOptOutDays() {
		return optOutDays;
	}

	public void setOptOutDays(int optOutDays) {
		this.optOutDays = optOutDays;
	}

	public boolean isHasPayrollNumberFeature() {
		return hasPayrollNumberFeature;
	}

	public void setHasPayrollNumberFeature(boolean hasPayrollNumberFeature) {
		this.hasPayrollNumberFeature = hasPayrollNumberFeature;
	}

	public boolean isHasDivisionFeature() {
		return hasDivisionFeature;
	}

	public void setHasDivisionFeature(boolean hasDivisionFeature) {
		this.hasDivisionFeature = hasDivisionFeature;
	}

	public String getInitialEnrollmentDate() {
		return initialEnrollmentDate;
	}

	public void setInitialEnrollmentDate(String initialEnrollmentDate) {
		this.initialEnrollmentDate = initialEnrollmentDate;
	}

	public boolean isHasIED() {
		return hasIED;
	}

	public void setHasIED(boolean hasIED) {
		this.hasIED = hasIED;
	}

	public Map<String, String> getMoneyTypeIdName() {
	    return moneyTypeIdName;
	}

	public void setMoneyTypeIdName(Map<String, String> moneyTypeIdName) {
	    this.moneyTypeIdName = moneyTypeIdName;
	}

	public List<LabelValueBean> getHistDatesAndCreatedTS() {
	    return histDatesAndCreatedTS;
	}

	public void setHistDatesAndCreatedTS(List<LabelValueBean> histDatesAndCreatedTS) {
	    this.histDatesAndCreatedTS = histDatesAndCreatedTS;
	}

	public String getEedefShortName() {
	    return eedefShortName;
	}

	public void setEedefShortName(String eedefShortName) {
	    this.eedefShortName = eedefShortName;
	}

	public String getReportedFromDate() {
		return reportedFromDate;
	}

	public void setReportedFromDate(String reportedFromDate) {
		this.reportedFromDate = reportedFromDate;
	}

	public String getReportedToDate() {
		return reportedToDate;
	}

	public void setReportedToDate(String reportedToDate) {
		this.reportedToDate = reportedToDate;
	}
	
}
