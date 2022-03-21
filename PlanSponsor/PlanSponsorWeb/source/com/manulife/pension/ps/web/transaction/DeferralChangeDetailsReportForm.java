package com.manulife.pension.ps.web.transaction;

import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.service.report.participant.transaction.valueobject.TransactionDetailsDeferralReportData;

public class DeferralChangeDetailsReportForm  extends ReportForm {
	private TransactionDetailsDeferralReportData report;
	private String profileId;
	private String transactionDate;
	private String contractNumber;
	
	
    public DeferralChangeDetailsReportForm() {   
    }

	public TransactionDetailsDeferralReportData getReport() {
		return report;
	}
	
	public void setReport(TransactionDetailsDeferralReportData report) {
		this.report = report;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	// format we have is yyyy-mm-dd need mm/dd/yyyy (string)
	public String getTransactionDateFormatted() {
		return transactionDate.substring(5,7)+"/"+transactionDate.substring(8)+"/"+
		       transactionDate.substring(0,4);
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	
   
   
}
