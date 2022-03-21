package com.manulife.pension.ps.web.contract;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * Contract statement form to handle the CSV report attributes
 * @author krishta
 *
 */
public class ContractStatementsForm extends AutoForm {

	
	private static final long serialVersionUID = 1L;
	private String reportType;
	private String periodEnd;
	private boolean isApolloBatchRun;
	
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getPeriodEnd() {
		return periodEnd;
	}
	public void setPeriodEnd(String periodEnd) {
		this.periodEnd = periodEnd;
	}
	public boolean isApolloBatchRun() {
		return isApolloBatchRun;
	}
	public void setApolloBatchRun(boolean isApolloBatchRun) {
		this.isApolloBatchRun = isApolloBatchRun;
	}



}
