/*
 * Created on May 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.iloans;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * @author sternlu
 * 
 * LoanCreatePackageForm
 */
public class LoanCreatePackageForm extends AutoForm {
	private String loanDate;

	private String nextRepaymentDate;

	private Date dNextRepaymentDate;

	private Date dLoanDate;

	private boolean validated;
	private boolean populated;

	private String actionLabel;

	public static final String CREATE_PACKAGE_ACTION = "createPackage";

	public static final String BACK_ACTION = "backToLoanRequestAction";

	private static final Map ACTION_LABEL_MAP = new HashMap();

	public static final String FIELD_LOAN_DATE = "loanDate";

	public static final String FIELD_NEXT_REPAYMENT_DATE = "nextRepaymentDate";
	static {
		ACTION_LABEL_MAP.put("create package", CREATE_PACKAGE_ACTION);
		ACTION_LABEL_MAP.put("i:loans home", BACK_ACTION);
	}

	/**
	 * @param actionLabel
	 *            The actionLabel to set.
	 */
	public void setActionLabel(String actionLabel) {
		this.actionLabel = trimString(actionLabel);
		setAction((String) ACTION_LABEL_MAP.get(actionLabel));
	}

	/**
	 * @return Returns the actionLabel.
	 */
	public String getActionLabel() {
		return actionLabel;
	}

	/**
	 * @param loanDate
	 *            The loanDate to set.
	 */
	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	/**
	 * @return Returns the loanDate.
	 */
	public String getLoanDate() {
		return loanDate;
	}

	/**
	 * @param nextRepaymentDate
	 *            The nextRepaymentDate to set.
	 */
	public void setNextRepaymentDate(String nextRepaymentDate) {
		this.nextRepaymentDate = nextRepaymentDate;
	}

	/**
	 * @return Returns the nextRepaymentDate.
	 */
	public String getNextRepaymentDate() {
		return nextRepaymentDate;
	}

	/**
	 * @param dNextRepaymentDate
	 *            The dNextRepaymentDate to set.
	 */
	public void setDNextRepaymentDate(Date dNextRepaymentDate) {
		this.dNextRepaymentDate = dNextRepaymentDate;
	}

	/**
	 * @return Returns the dNextRepaymentDate.
	 */
	public Date getDNextRepaymentDate() {
		return dNextRepaymentDate;
	}

	/**
	 * @param validated
	 *            The validated to set.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	/**
	 * @return Returns the validated.
	 */
	public boolean isValidated() {
		return validated;
	}

	public boolean isCreatePackageAction() {
		return getAction().equals(CREATE_PACKAGE_ACTION);

	}

	/**
	 * @param dLoanDate
	 *            The dLoanDate to set.
	 */
	public void setDLoanDate(Date dLoanDate) {
		this.dLoanDate = dLoanDate;
	}

	/**
	 * @return Returns the dLoanDate.
	 */
	public Date getDLoanDate() {
		return dLoanDate;
	}

	/**
	 * @param populated The populated to set.
	 */
	public void setPopulated(boolean populated) {
		this.populated = populated;
	}

	/**
	 * @return Returns the populated.
	 */
	public boolean isPopulated() {
		return populated;
	}
}