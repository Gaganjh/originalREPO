package com.manulife.pension.bd.web.bob.planReview;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.service.planReview.valueobject.PrintDocumentPackgeVo;

public class PlanReviewResultForm extends PlanReviewReportForm{

	/**
	 * PlanReviewResultForm is the Action Form for ContractReviewReportResult page.
	 */
	
	private boolean planReviewReportPdfsSelected;
	private boolean allPlanReviewReportPdfsSelected;
	private boolean allExeSummaryPdfsSelected;
	private boolean exeSummaryPdfsSelected;
	
	private List<PlanReviewReportUIHolder> selectedRecordList = new ArrayList<PlanReviewReportUIHolder>();
	private static final long serialVersionUID = 1L;
	private List<PlanReviewReportUIHolder> contractResultVOList = new ArrayList<PlanReviewReportUIHolder>();
	private String planReviewRequestId;
	private Boolean requestFromHistory;
	private String reportMonthEndDate;
	private List<PrintDocumentPackgeVo> printRequestedVoList = new ArrayList<PrintDocumentPackgeVo>();
	private Boolean incompleteErrorMessage=false;
	private int downloadContractId;
	private String type;
	private String requestType;
	private boolean checkCountvalue = false;
	
	
	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getDownloadContractId() {
		return downloadContractId;
	}
	public void setDownloadContractId(int downloadContractId) {
		this.downloadContractId = downloadContractId;
	}
	public Boolean getIncompleteErrorMessage() {
		return incompleteErrorMessage;
	}
	public void setIncompleteErrorMessage(Boolean incompleteErrorMessage) {
		this.incompleteErrorMessage = incompleteErrorMessage;
	}
	public List<PrintDocumentPackgeVo> getPrintRequestedVoList() {
		return printRequestedVoList;
	}
	public void setPrintRequestedVoList(
			List<PrintDocumentPackgeVo> printRequestedVoList) {
		this.printRequestedVoList = printRequestedVoList;
	}
	public void setContractResultVOList(List<PlanReviewReportUIHolder> contractResultVOList) {
		this.contractResultVOList = contractResultVOList;
	}
	public List<PlanReviewReportUIHolder> getContractResultVOList() {
		return contractResultVOList;
	}
	public int getContractResultVOListSize() {
		return contractResultVOList.size();
	}
	public int getPrintRequestedVoListSize() {
		return printRequestedVoList.size();
	}
	public PlanReviewReportUIHolder getPlanReviewReportVO(int index) {
		if (this.contractResultVOList == null || contractResultVOList.isEmpty()) {
			this.setDisplayCotractReviewReports(new ArrayList<PlanReviewReportUIHolder>());
			contractResultVOList = new ArrayList<PlanReviewReportUIHolder>();
		}

		int listSize = this.contractResultVOList.size();

		if (index >= listSize) {

			throw new IllegalArgumentException("Invalid accessing the index: "
					+ index
					+ ", from displayCotractReviewReports list of size: "
					+ listSize);
		}

		return contractResultVOList.get(index);
	}
//	
	public List<PlanReviewReportUIHolder> getSelectedRecordList() {
		return selectedRecordList;
	}
	public void setSelectedRecordList(
			List<PlanReviewReportUIHolder> selectedRecordList) {
		this.selectedRecordList = selectedRecordList;
	}
	public void setPlanReviewRequestId(String planReviewRequestId) {
		this.planReviewRequestId = planReviewRequestId;
	}
	public String getPlanReviewRequestId() {
		return planReviewRequestId;
	}
	public Boolean getRequestFromHistory() {
		return requestFromHistory;
	}
	public void setRequestFromHistory(Boolean requestFromHistory) {
		this.requestFromHistory = requestFromHistory;
	}
	public String getReportMonthEndDate() {
		return reportMonthEndDate;
	}
	public void setReportMonthEndDate(String reportMonthEndDate) {
		this.reportMonthEndDate = reportMonthEndDate;
	}
	public boolean isExeSummaryPdfsSelected() {
		return exeSummaryPdfsSelected;
	}
	public void setExeSummaryPdfsSelected(boolean exeSummaryPdfsSelected) {
		this.exeSummaryPdfsSelected = exeSummaryPdfsSelected;
	}
	public boolean isPlanReviewReportPdfsSelected() {
		return planReviewReportPdfsSelected;
	}
	public void setPlanReviewReportPdfsSelected(boolean planReviewReportPdfsSelected) {
		this.planReviewReportPdfsSelected = planReviewReportPdfsSelected;
	}
	public boolean isAllPlanReviewReportPdfsSelected() {
		return allPlanReviewReportPdfsSelected;
	}
	public void setAllPlanReviewReportPdfsSelected(
			boolean allPlanReviewReportPdfsSelected) {
		this.allPlanReviewReportPdfsSelected = allPlanReviewReportPdfsSelected;
	}
	public boolean isAllExeSummaryPdfsSelected() {
		return allExeSummaryPdfsSelected;
	}
	public void setAllExeSummaryPdfsSelected(boolean allExeSummaryPdfsSelected) {
		this.allExeSummaryPdfsSelected = allExeSummaryPdfsSelected;
	}
	public boolean isCheckCountvalue() {
		return checkCountvalue;
	}
	public void setCheckCountvalue(boolean checkCountvalue) {
		this.checkCountvalue = checkCountvalue;
	}

}

