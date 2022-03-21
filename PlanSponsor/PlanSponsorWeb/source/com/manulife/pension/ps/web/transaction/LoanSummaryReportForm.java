package com.manulife.pension.ps.web.transaction;



import com.manulife.pension.ps.web.report.ReportForm;

public class LoanSummaryReportForm extends ReportForm {
  
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String[] loanDetails;

	public LoanSummaryReportForm() {
        super();
    }
    
    public String[] getLoanDetails() {
        return loanDetails;
    }
    public void setLoanDetails(String[] loanDetails) {
        this.loanDetails = loanDetails;
    }
   
    
    
}
