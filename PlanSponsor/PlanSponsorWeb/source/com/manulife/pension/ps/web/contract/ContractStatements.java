package com.manulife.pension.ps.web.contract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.manulife.pension.lp.model.ereports.RequestConstants;
import com.manulife.pension.platform.web.taglib.util.LabelValueBean;
import com.manulife.pension.ps.web.Constants;

/**
 * For Audit on Web
 * Added new Report options
 * AUDIT_SUMMARY(as),AUDIT_CERTIFICATION(cl)
 */
/**
 * ContractStatement class 
 * This class is used as a value object for 
 * ContractStatements page
 * 
 * @ LS. revised 2004-04-05 to use LabelValueBean for statement dropdowns
 */

public class ContractStatements implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String[] REPORT_TYPES =
		new String[] {
			RequestConstants.REPORT_TYPE_PLAN_ADMIN,
			RequestConstants.REPORT_TYPE_EMPLOYER_FINANCIAL,
			RequestConstants.REPORT_TYPE_SCHEDULE_A,
			RequestConstants.REPORT_TYPE_SCHEDULE_C,
			RequestConstants.REPORT_TYPE_AUDIT_SUMMARY,
			RequestConstants.REPORT_TYPE_AUDIT_CERTIFICATION,
			RequestConstants.REPORT_TYPE_CLASS_CONVERSION,
			RequestConstants.REPORT_TYPE_DEFINED_BENEFIT,
			Constants.REPORT_TYPE_CONT_HISTORY,
			Constants.REPORT_TYPE_OUTSTANDING_LOAN,
			Constants.REPORT_TYPE_LOAN_REPAYMENT};
	private ArrayList efOptions;
	private ArrayList paOptions;
	private ArrayList saOptions;
	private ArrayList asOptions;
	private ArrayList clOptions;
	private ArrayList crOptions; //class conversion
	private ArrayList bpOptions; // defined benefit
	private ArrayList scOptions; // For Schedule C
	private ArrayList chOptions; //Contribution History
	private ArrayList olOptions; //Outstanding Loan
	private ArrayList lrOptions; //Loan Repayments Report
	private String reportOption;
	private String statementOption;

	public ContractStatements() {

		StringBuffer prompt = new StringBuffer("Select a statement...");
		for (int i = 0; i < 94; i++) {
			prompt.append("&nbsp;");
		}
		efOptions = new ArrayList();
		clOptions = new ArrayList();
		statementOption = prompt.toString();
				
		
		prompt = new StringBuffer("Select a report...");
		for (int i = 0; i < 100; i++) {
			prompt.append("&nbsp;");
		}
		paOptions = new ArrayList();
		saOptions = new ArrayList();
		crOptions = new ArrayList();
		bpOptions = new ArrayList();
		scOptions = new ArrayList();
		asOptions = new ArrayList();
		chOptions = new ArrayList();
		olOptions = new ArrayList();
		lrOptions = new ArrayList();
		reportOption = prompt.toString();
	}

	public String getReportOption() {
		return this.reportOption;
	}

	public void addStatement(String reportType, String key, String label) {

		if (reportType.equals(RequestConstants.REPORT_TYPE_PLAN_ADMIN))
			paOptions.add(new LabelValueBean(label, key));
		else if (reportType.equals(RequestConstants.REPORT_TYPE_EMPLOYER_FINANCIAL))
				efOptions.add(new LabelValueBean(label, key));
		else if (reportType.equals(RequestConstants.REPORT_TYPE_SCHEDULE_A))
				saOptions.add(new LabelValueBean(label, key));
		else if (reportType.equals(RequestConstants.REPORT_TYPE_DEFINED_BENEFIT))
			bpOptions.add(new LabelValueBean(label, key));
		else if (reportType.equals(RequestConstants.REPORT_TYPE_SCHEDULE_C))
			scOptions.add(new LabelValueBean(label, key));
		else if (reportType.equals(RequestConstants.REPORT_TYPE_AUDIT_SUMMARY))
			asOptions.add(new LabelValueBean(label, key));
		else if (reportType.equals(RequestConstants.REPORT_TYPE_AUDIT_CERTIFICATION))
			clOptions.add(new LabelValueBean(label, key));
		else if (reportType.equals(RequestConstants.REPORT_TYPE_CLASS_CONVERSION))
			crOptions.add(new LabelValueBean(label, key)); //report type class conversion
		else if (reportType.equals(Constants.REPORT_TYPE_CONT_HISTORY))
			chOptions.add(new LabelValueBean(label, key)); //report type Contribution History
		else if (reportType.equals(Constants.REPORT_TYPE_OUTSTANDING_LOAN))
			olOptions.add(new LabelValueBean(label, key)); //report type Outstanding Loan
		else 
			lrOptions.add(new LabelValueBean(label, key)); //report type Loan repayment
	}

	public List getPaOptions() {
		return paOptions;
	}

	public List getEfOptions() {
		return efOptions;
	}

	public List getSaOptions() {
		return saOptions;
	}
	public List getCrOptions() {
		return crOptions;
	}
	
	public List getBpOptions() {
		return bpOptions;
	}

	/**
	 * Gets the statementOption
	 * @return Returns a String
	 */
	public String getStatementOption() {
		return statementOption;
	}
	
	public List getScOptions() {
		return scOptions;
	}
	
	public List getAsOptions() {
		return asOptions;
	}
	
	public List getClOptions() {
		return clOptions;
	}
	
	public List getChOptions() {
		return chOptions;
	}
	public List getOlOptions() {
		return olOptions;
	}
	public List getLrOptions() {
		return lrOptions;
	}

}