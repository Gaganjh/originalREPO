package com.manulife.pension.bd.web.bob.planReview;

import java.util.List;

import com.manulife.pension.service.environment.valueobject.LabelValueBean;

public class PlanReviewReportHistoryForm extends
		BasePlanReviewReportForm {

	private static final long serialVersionUID = 1L;

	private String selectedSearchByField;
	private String selectedSearchByFieldValue;
	private String requestedFromDate;
	private String requestedToDate;
	private String selectedReportMonthEndDate;
	private String printConfirmNumber;
	
	
	private String filterSearchByField;
	private String filterSearchByFieldValue;
	private String filterRequestedFromDate;
	private String filterRequestedToDate;
	private String filterReportMonthEndDate;
	private String filterPrintConfirmNumber;
	
	
	private List<LabelValueBean> requestedMonthendDateList;
	
	boolean externalUserView = false;
	
	private String requestId;
	
	private String selectedPlanReviewActivityId; 
	private String selectedPlanReviewRequestId;
	private String selectedPlanReviewContractId; 
	private String selectedPlanReviewContractName;
	private String selectedPlanReviewRequestedTS;
	
	public String getSelectedSearchByField() {
		return selectedSearchByField;
	}

	public void setSelectedSearchByField(String selectedSearchByField) {
		this.selectedSearchByField = selectedSearchByField;
	}

	public String getSelectedSearchByFieldValue() {
		return selectedSearchByFieldValue;
	}

	public void setSelectedSearchByFieldValue(String selectedSearchByFieldValue) {
		this.selectedSearchByFieldValue = selectedSearchByFieldValue;
	}

	public String getSelectedReportMonthEndDate() {
		return selectedReportMonthEndDate;
	}

	public void setSelectedReportMonthEndDate(String selectedReportMonthEndDate) {
		this.selectedReportMonthEndDate = selectedReportMonthEndDate;
	}

	public String getPrintConfirmNumber() {
		return printConfirmNumber;
	}

	public void setPrintConfirmNumber(String printConfirmNumber) {
		this.printConfirmNumber = printConfirmNumber;
	}

	public String getRequestedFromDate() {
		return requestedFromDate;
	}

	public void setRequestedFromDate(String requestedFromDate) {
		this.requestedFromDate = requestedFromDate;
	}

	public String getRequestedToDate() {
		return requestedToDate;
	}

	public void setRequestedToDate(String requestedToDate) {
		this.requestedToDate = requestedToDate;
	}

	public List<LabelValueBean> getRequestedMonthendDateList() {
		return requestedMonthendDateList;
	}

	public void setRequestedMonthendDateList(
			List<LabelValueBean> requestedMonthendDateList) {
		this.requestedMonthendDateList = requestedMonthendDateList;
	}

	public boolean isExternalUserView() {
		return externalUserView;
	}

	public void setExternalUserView(boolean externalUserView) {
		this.externalUserView = externalUserView;
	}

	public String getSelectedPlanReviewActivityId() {
		return selectedPlanReviewActivityId;
	}

	public void setSelectedPlanReviewActivityId(String selectedPlanReviewActivityId) {
		this.selectedPlanReviewActivityId = selectedPlanReviewActivityId;
	}

	public String getSelectedPlanReviewRequestId() {
		return selectedPlanReviewRequestId;
	}

	public void setSelectedPlanReviewRequestId(String selectedPlanReviewRequestId) {
		this.selectedPlanReviewRequestId = selectedPlanReviewRequestId;
	}

	public String getSelectedPlanReviewContractId() {
		return selectedPlanReviewContractId;
	}

	public void setSelectedPlanReviewContractId(
			String selectedPlanReviewContractId) {
		this.selectedPlanReviewContractId = selectedPlanReviewContractId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getFilterSearchByField() {
		return filterSearchByField;
	}

	public void setFilterSearchByField(String filterSearchByField) {
		this.filterSearchByField = filterSearchByField;
	}

	public String getFilterSearchByFieldValue() {
		return filterSearchByFieldValue;
	}

	public void setFilterSearchByFieldValue(String filterSearchByFieldValue) {
		this.filterSearchByFieldValue = filterSearchByFieldValue;
	}

	public String getFilterRequestedFromDate() {
		return filterRequestedFromDate;
	}

	public void setFilterRequestedFromDate(String filterRequestedFromDate) {
		this.filterRequestedFromDate = filterRequestedFromDate;
	}

	public String getFilterRequestedToDate() {
		return filterRequestedToDate;
	}

	public void setFilterRequestedToDate(String filterRequestedToDate) {
		this.filterRequestedToDate = filterRequestedToDate;
	}

	public String getFilterReportMonthEndDate() {
		return filterReportMonthEndDate;
	}

	public void setFilterReportMonthEndDate(String filterReportMonthEndDate) {
		this.filterReportMonthEndDate = filterReportMonthEndDate;
	}

	public String getFilterPrintConfirmNumber() {
		return filterPrintConfirmNumber;
	}

	public void setFilterPrintConfirmNumber(String filterPrintConfirmNumber) {
		this.filterPrintConfirmNumber = filterPrintConfirmNumber;
	}

	public String getSelectedPlanReviewContractName() {
		return selectedPlanReviewContractName;
	}

	public void setSelectedPlanReviewContractName(
			String selectedPlanReviewContractName) {
		this.selectedPlanReviewContractName = selectedPlanReviewContractName;
	}

	public String getSelectedPlanReviewRequestedTS() {
		return selectedPlanReviewRequestedTS;
	}

	public void setSelectedPlanReviewRequestedTS(
			String selectedPlanReviewRequestedTS) {
		this.selectedPlanReviewRequestedTS = selectedPlanReviewRequestedTS;
	}
	
}
