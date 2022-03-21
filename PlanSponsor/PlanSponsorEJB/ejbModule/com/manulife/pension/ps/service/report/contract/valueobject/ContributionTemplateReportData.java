package com.manulife.pension.ps.service.report.contract.valueobject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.manulife.pension.ps.service.report.contract.reporthandler.ContributionTemplateReportHandler;
import com.manulife.pension.service.report.valueobject.ReportCriteria;
import com.manulife.pension.service.report.valueobject.ReportData;

public class ContributionTemplateReportData extends ReportData {

	public static String REPORT_ID = ContributionTemplateReportHandler.class.getName();
	public static String REPORT_NAME = "contributionTemplateReport"; 
	public static final String FILTER_FIELD_1 = "contractNumber";
	
	public static final String DATE_FORMAT = "MMddyyyy";
	public static final SimpleDateFormat FORMATTER = new SimpleDateFormat(DATE_FORMAT);
	
	private int contractNumber;
	private String transactionNumber;
	private Date date;
	private int maxLoanCount;
	private List columnLabels;
	
	public ContributionTemplateReportData(ReportCriteria criteria, int totalCount) {
		super(criteria, totalCount);
	}

	/**
	 * Gets the contractNumber
	 * @return Returns a String
	 */
	public int getContractNumber() {
		return contractNumber;
	}

	/**
	 * Sets the contractNumber
	 * @param contractNumber The contractNumber to set
	 */
	public void setContractNumber(int contractNumber) {
		this.contractNumber = contractNumber;
	}
	/**
	 * @return Returns the date.
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return Returns the date.
	 */
	public String getFormattedDate() {
		if(getDate() == null) return "";
		String dateString = null;
		synchronized (FORMATTER) {
			dateString = FORMATTER.format(getDate());
		}
		return dateString;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return Returns the transactionNumber.
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber The transactionNumber to set.
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @return Returns the columnLabels.
	 */
	public List getColumnLabels() {
		return columnLabels;
	}

	/**
	 * @param columnLabels The columnLabels to set.
	 */
	public void setColumnLabels(List columnLabels) {
		this.columnLabels = columnLabels;
	}

	/**
	 * @return Returns the maxLoanCount.
	 */
	public int getMaxLoanCount() {
		return maxLoanCount;
	}

	/**
	 * @param maxLoanCount The maxLoanCount to set.
	 */
	public void setMaxLoanCount(int maxLoanCount) {
		this.maxLoanCount = maxLoanCount;
	}

	public String toString() {
		return super.toString() + "\ncontractNumber: " + getContractNumber() + "\ntransactionNumber: " +
				getTransactionNumber() + "\ndate: " + getFormattedDate() + "\nmaxLoanCount: " + getMaxLoanCount();
	}	
}
