package com.manulife.pension.bd.web.bob.planReview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.planReview.valueobject.PlanReviewHistoryDetailsReportItem;
import com.manulife.pension.service.planReview.valueobject.ShippingVO;

public class PlanReviewHistoryDetailsReportForm extends
		BasePlanReviewReportForm {

	private static final long serialVersionUID = 1L;

	private String selectedPlanReviewActivityId;
	private String selectedPlanReviewRequestId;
	private String selectedPrintActivityId;
	private String selectedPlanReviewContractId;
	private String selectedPlanReviewContractName;
	private String selectedPlanReviewRequestedTS;
	
	private boolean viewDisableIndicator;
	
	private String downloadPlanReviewReportInd;
	private String downloadExcecutiveSummaryInd;
	
	private boolean downloadPlanReviewReportIndicator;
	private boolean downloadExcecutiveSummaryIndicator;

	
	private ShippingVO shippingAddressDetails;
	
	private List<PlanReviewReportUIHolder> uiHolders = new ArrayList<PlanReviewReportUIHolder>();
	private List<PlanReviewHistoryDetailsReportItem> historyDetails = new ArrayList<PlanReviewHistoryDetailsReportItem>();

	private String viewDisableReason = StringUtils.EMPTY;
	private Map<String, String> viewDisableReasonMap = new LinkedHashMap<String, String>();
	
	private String type;
	private String requestType;
	private String requestHistoryDetailsReport;
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getRequestHistoryDetailsReport() {
		return requestHistoryDetailsReport;
	}

	public void setRequestHistoryDetailsReport(String requestHistoryDetailsReport) {
		this.requestHistoryDetailsReport = requestHistoryDetailsReport;
	}

	public String getSelectedPlanReviewActivityId() {
		return selectedPlanReviewActivityId;
	}

	public void setSelectedPlanReviewActivityId(
			String selectedPlanReviewActivityId) {
		this.selectedPlanReviewActivityId = selectedPlanReviewActivityId;
	}

	public String getSelectedPlanReviewRequestId() {
		return selectedPlanReviewRequestId;
	}

	public void setSelectedPlanReviewRequestId(
			String selectedPlanReviewRequestId) {
		this.selectedPlanReviewRequestId = selectedPlanReviewRequestId;
	}

	public boolean isViewDisableIndicator() {
		return viewDisableIndicator;
	}

	public void setViewDisableIndicator(boolean viewDisableIndicator) {
		this.viewDisableIndicator = viewDisableIndicator;
	}

	public String getDownloadPlanReviewReportInd() {
		return downloadPlanReviewReportInd;
	}

	public void setDownloadPlanReviewReportInd(String downloadPlanReviewReportInd) {
		this.downloadPlanReviewReportInd = downloadPlanReviewReportInd;
	}

	public String getDownloadExcecutiveSummaryInd() {
		return downloadExcecutiveSummaryInd;
	}

	public void setDownloadExcecutiveSummaryInd(String downloadExcecutiveSummaryInd) {
		this.downloadExcecutiveSummaryInd = downloadExcecutiveSummaryInd;
	}

	public List<PlanReviewHistoryDetailsReportItem> getHistoryDetails() {
		return historyDetails;
	}

	public void setHistoryDetails(
			List<PlanReviewHistoryDetailsReportItem> historyDetails) {
		this.historyDetails = historyDetails;
	}

	public ShippingVO getShippingAddressDetails() {
		return shippingAddressDetails;
	}

	public void setShippingAddressDetails(ShippingVO shippingAddressDetails) {
		this.shippingAddressDetails = shippingAddressDetails;
	}

	public String getViewDisableReason() {
		return viewDisableReason;
	}

	public void setViewDisableReason(String viewDisableReason) {
		this.viewDisableReason = viewDisableReason;
	}

	public Map<String, String> getViewDisableReasonMap() {
		return viewDisableReasonMap;
	}

	public void setViewDisableReasonMap(Map<String, String> viewDisableReasonMap) {
		this.viewDisableReasonMap = viewDisableReasonMap;
	}

	public String getSelectedPrintActivityId() {
		return selectedPrintActivityId;
	}

	public void setSelectedPrintActivityId(String selectedPrintActivityId) {
		this.selectedPrintActivityId = selectedPrintActivityId;
	}

	public String getSelectedPlanReviewContractId() {
		return selectedPlanReviewContractId;
	}

	public void setSelectedPlanReviewContractId(String selectedPlanReviewContractId) {
		this.selectedPlanReviewContractId = selectedPlanReviewContractId;
	}

	public List<PlanReviewReportUIHolder> getUiHolders() {
		return uiHolders;
	}

	public void setUiHolders(List<PlanReviewReportUIHolder> uiHolders) {
		this.uiHolders = uiHolders;
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

	public boolean isDownloadPlanReviewReportIndicator() {
		return downloadPlanReviewReportIndicator;
	}

	public void setDownloadPlanReviewReportIndicator(
			boolean downloadPlanReviewReportIndicator) {
		this.downloadPlanReviewReportIndicator = downloadPlanReviewReportIndicator;
	}

	public boolean isDownloadExcecutiveSummaryIndicator() {
		return downloadExcecutiveSummaryIndicator;
	}

	public void setDownloadExcecutiveSummaryIndicator(
			boolean downloadExcecutiveSummaryIndicator) {
		this.downloadExcecutiveSummaryIndicator = downloadExcecutiveSummaryIndicator;
	}
	
}
