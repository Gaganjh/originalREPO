package com.manulife.pension.ps.web.tpadownload;

import java.util.ArrayList;

import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedCurrentFilesItem;
import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedCurrentFilesReportData;
import com.manulife.pension.ps.web.report.ReportForm;

public class TedCurrentFilesForm extends ReportForm {

	
	private String[] selectedIdentities;
	private String myAction;
	private TedCurrentFilesReportData report = null;
	private String filterContractName = null;
	private String filterContractNumber = null;
	private String filterDownloadStatus = TedCurrentFilesItem.DOWNLOAD_STATUS_NEW;
	private String filterCorrected = null;	
	private String singleFileIdentity = "";
	
	// variables for the sortViaSubmit tag.
	private String sortField=TedCurrentFilesItem.SORT_FIELD_DOWNLOAD_STATUS;
	private String sortDirection="desc";
    private String filterPeriodEndDate = null;
    private String filterYearEnd = null;

	
	public TedCurrentFilesForm() {
		super();
	}
	
	
	/**
	 * @return Returns the selectedIdentities.
	 */
	public String[] getSelectedIdentities() {
		return selectedIdentities;
	}
	
	public ArrayList getSelectedFilenameArrayList() {
		ArrayList list = new ArrayList();
		if (selectedIdentities != null) {
			for (int i=0; i < selectedIdentities.length; i++) {
				list.add(selectedIdentities[i]);			
			}
		}
		return list;
	}
	
	/**
	 * @param selectedIdentities The selectedIdentities to set.
	 */
	public void setSelectedIdentities(String[] selectedFilenames) {
		this.selectedIdentities = selectedFilenames;
	}
	/**
	 * @return Returns the myAction.
	 */
	public String getMyAction() {
		return myAction;
	}
	/**
	 * @param myAction The myAction to set.
	 */
	public void setMyAction(String myAction) {
		this.myAction = myAction;
	}
	
	public void setReport(TedCurrentFilesReportData report){
		this.report = report;
	}	

	public TedCurrentFilesReportData getReport(){
		return this.report;
	}
	public String getFilterContractName() {
		return filterContractName;
	}
	public void setFilterContractName(String filterContractName) {
		this.filterContractName = filterContractName;
	}
	public String getFilterContractNumber() {
		return filterContractNumber;
	}
	public void setFilterContractNumber(String filterContractNumber) {
		this.filterContractNumber = filterContractNumber;
	}
	public String getFilterCorrected() {
		return filterCorrected;
	}
	public void setFilterCorrected(String filterCorrected) {
		this.filterCorrected = filterCorrected;
	}
	public String getFilterDownloadStatus() {
		return filterDownloadStatus;
	}
	public void setFilterDownloadStatus(String filterDownloadStatus) {
		this.filterDownloadStatus = filterDownloadStatus;
	}	
	public String getSingleFileIdentity() {
		return singleFileIdentity;
	}
	public void setSingleFileIdentity(String singleFileIdentity) {
		this.singleFileIdentity = singleFileIdentity;
	}
	public String getSortDirection() {
		return sortDirection;
	}
	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getFilterPeriodEndDate() {
		return filterPeriodEndDate;
	}
	public void setFilterPeriodEndDate(String filterPeriodEndDate) {
		this.filterPeriodEndDate = filterPeriodEndDate;
	}
	public String getFilterYearEnd() {
		return filterYearEnd;
	}
	public void setFilterYearEnd(String filterYearEnd) {
		this.filterYearEnd = filterYearEnd;
	}
}


