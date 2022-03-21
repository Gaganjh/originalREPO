package com.manulife.pension.ps.web.tpadownload;

import java.util.ArrayList;

import com.manulife.pension.ps.service.report.tpadownload.valueobject.TedHistoryFilesReportData;
import com.manulife.pension.ps.web.report.ReportForm;

public class TedHistoryFilesForm extends ReportForm {
	private String[] selectedFilenames;
	private String contractNumber;
	private String myAction;
	private String singleFileIdentity = "";
	
	private TedHistoryFilesReportData report = null;
	
	public TedHistoryFilesForm() {
		super();
	}


	public ArrayList getSelectedFilenameArrayList() {
		ArrayList list = new ArrayList();
		if (selectedFilenames != null) {
			for (int i=0; i < selectedFilenames.length; i++) {
				list.add(selectedFilenames[i]);			
			}
		}
		return list;
	}
	
	/**
	 * @return Returns the contractNumber.
	 */
	public String getContractNumber() {
		return contractNumber;
	}
	/**
	 * @param contractNumber The contractNumber to set.
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	public TedHistoryFilesReportData getReport() {
		return report;
	}
	public void setReport(TedHistoryFilesReportData report) {
		this.report = report;
	}
	public String[] getSelectedFilenames() {
		return selectedFilenames;
	}
	public void setSelectedFilenames(String[] selectedFilenames) {
		this.selectedFilenames = selectedFilenames;
	}
	public String getMyAction() {
		return myAction;
	}
	public void setMyAction(String myAction) {
		this.myAction = myAction;
	}
	public String getSingleFileIdentity() {
		return singleFileIdentity;
	}
	public void setSingleFileIdentity(String singleFileIdentity) {
		this.singleFileIdentity = singleFileIdentity;
	}
}


