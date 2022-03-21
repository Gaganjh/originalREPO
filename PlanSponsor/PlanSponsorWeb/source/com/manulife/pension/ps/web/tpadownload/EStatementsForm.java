package com.manulife.pension.ps.web.tpadownload;

import java.util.ArrayList;
import java.util.List;

import com.manulife.pension.ps.service.report.tpadownload.valueobject.EStatementsReportData;
import com.manulife.pension.ps.web.report.ReportForm;

public class EStatementsForm extends ReportForm {

	
	private String[] selectedStatements;
	private EStatementsReportData report = null;
	private String filterContractName = null;
	private String filterContractNumber = null;
	private String filterCorrected = null;
	private String filterStatementType = null;
	private String filterReportEndDateFrom = null;
	private String filterReportEndDateTo = null;
	private String filterIsYearEnd = null;
	private List reportDateFromList = new ArrayList();
	private List reportDateToList = new ArrayList();
	
	
		
	public EStatementsForm() {
		super();
	}
	
	public ArrayList getSelectedFilenameArrayList() {
		ArrayList list = new ArrayList();
		if (selectedStatements != null) {
			for (int i=0; i < selectedStatements.length; i++) {
				list.add(selectedStatements[i]);			
			}
		}
		return list;
	}
	
	public void setReport(EStatementsReportData report){
		this.report = report;
	}	

	public EStatementsReportData getReport(){
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

	/**
	 * @return Returns the filterStatementType.
	 */
	public String getFilterStatementType() {
		return filterStatementType;
	}
	/**
	 * @param filterStatementType The filterStatementType to set.
	 */
	public void setFilterStatementType(String filterStatementType) {
		this.filterStatementType = filterStatementType;
	}
	/**
	 * @return Returns the filterIsYearEnd.
	 */
	public String getFilterIsYearEnd() {
		return filterIsYearEnd;
	}
	/**
	 * @param filterIsYearEnd The filterIsYearEnd to set.
	 */
	public void setFilterIsYearEnd(String filterIsYearEnd) {
		this.filterIsYearEnd = filterIsYearEnd;
	}
	/**
	 * @return Returns the filterReportEndDateFrom.
	 */
	public String getFilterReportEndDateFrom() {
		return filterReportEndDateFrom;
	}

	/**
	 * @return Returns the filterReportEndDateTo.
	 */
	public String getFilterReportEndDateTo() {
		return filterReportEndDateTo;
	}
	/**
	 * @param filterReportEndDateTo The filterReportEndDateTo to set.
	 */
	public void setFilterReportEndDateTo(String filterReportEndDateTo) {
		this.filterReportEndDateTo = filterReportEndDateTo;
	}
	/**
	 * @return Returns the reportDateFromList.
	 */
	public List getReportDateFromList() {
		return reportDateFromList;
	}
	/**
	 * @param reportDateFromList The reportDateFromList to set.
	 */
	public void setReportDateFromList(List reportDateFromList) {
		this.reportDateFromList = reportDateFromList;
	}
	/**
	 * @return Returns the reportDateToList.
	 */
	public List getReportDateToList() {
		return reportDateToList;
	}
	/**
	 * @param reportDateToList The reportDateToList to set.
	 */
	public void setReportDateToList(List reportDateToList) {
		this.reportDateToList = reportDateToList;
	}
	/**
	 * @param filterReportEndDateFrom The filterReportEndDateFrom to set.
	 */
	public void setFilterReportEndDateFrom(String filterReportEndDateFrom) {
		this.filterReportEndDateFrom = filterReportEndDateFrom;
	}

	/**
	 * @return Returns the selectedStatements.
	 */
	public String[] getSelectedStatements() {
		return selectedStatements;
	}
	/**
	 * @param selectedStatements The selectedStatements to set.
	 */
	public void setSelectedStatements(String[] selectedStatements) {
		this.selectedStatements = selectedStatements;
	}
	

}


