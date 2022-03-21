/*
 * Created on May 27, 2005
 * 
 */
package com.manulife.pension.ps.web.iloans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.account.valueobject.LoanRequestData;

/**
 * @author sternlu
 * 
 * InitiateLoanRequestForm Input form for InitiateLoanRequestAction
 */
public class InitiateLoanRequestsForm extends AutoForm {

	private static final String CONTINUE_ACTION = "continue";

	private static final String BACK_ACTION = "back";

	public static final String FIELD_SSN = "ssn";

	public static final String FIELD_CONTRACT = "contract";

	public static final String FIELD_LEGALLY_MARRIED = "legallyMaried";

	public static final String FIELD_TYPE_OF_LOAN = "typeOfLoan";

	public static final String FIELD_REASON_FOR_LOAN = "reasonForLoan";

	public static final int REASON_FOR_LOAN_MAX_LENGTH = 250;

	public static final String YES = "Y";

	public static final String NO = "N";

	public static final String LOAN_TYPE_CODE_EMPTY = "";

	private String actionLabel;

	private String ssn1;

	private String ssn2;

	private String ssn3;

	private String ssn;

	private String contractNumber;

	private String typeOfLoan;

	private String reasonForLoan;

	private String legallyMarried;
	
	private boolean onlineLoan = false;

	public static final Collection TYPE_OF_LOAN_LIST = getTypeOfLoanList();

	private static Collection getTypeOfLoanList() {
		Collection list = new ArrayList();
		list.add(new LabelValueBean("Select", LOAN_TYPE_CODE_EMPTY));
		list.add(new LabelValueBean("Hardship",
				LoanRequestData.LOAN_REASON_CODE_HARDSHIP));
		list.add(new LabelValueBean("Primary Residence",
				LoanRequestData.LOAN_REASON_CODE_PRIMARY_RESIDENCE));
		list.add(new LabelValueBean("General purpose",
				LoanRequestData.LOAN_REASON_CODE_GENERAL));
		return list;
	}

	private static final Map ACTION_LABEL_MAP = new HashMap();

	static {
		ACTION_LABEL_MAP.put("continue", CONTINUE_ACTION);
		ACTION_LABEL_MAP.put("back", BACK_ACTION);
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
	 * @param ssn1
	 *            The ssn1 to set.
	 */
	public void setSsn1(String ssn1) {
		this.ssn1 = ssn1;
	}

	/**
	 * @return Returns the ssn1.
	 */
	public String getSsn1() {
		return ssn1;
	}

	/**
	 * @param ssn2
	 *            The ssn2 to set.
	 */
	public void setSsn2(String ssn2) {
		this.ssn2 = ssn2;
	}

	/**
	 * @return Returns the ssn2.
	 */
	public String getSsn2() {
		return ssn2;
	}

	/**
	 * @param ssn3
	 *            The ssn3 to set.
	 */
	public void setSsn3(String ssn3) {
		this.ssn3 = ssn3;
	}

	/**
	 * @return Returns the ssn3.
	 */
	public String getSsn3() {
		return ssn3;
	}

	/**
	 * @return Returns the ssn.
	 */
	public String getSsn() {
		return (new StringBuffer(ssn1).append(ssn2).append(ssn3)).toString();
	}

	/**
	 * @param contractNumber
	 *            The contractNumber to set.
	 */
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	/**
	 * @return Returns the contractNumber.
	 */
	public String getContractNumber() {
		return contractNumber;
	}

	/**
	 * @param legallyMaried
	 *            The legallyMaried to set.
	 */

	/**
	 * @param typeOfLoan
	 *            The typeOfLoan to set.
	 */
	public void setTypeOfLoan(String typeOfLoan) {
		this.typeOfLoan = typeOfLoan;
	}

	/**
	 * @return Returns the typeOfLoan.
	 */
	public String getTypeOfLoan() {
		return typeOfLoan;
	}

	/**
	 * @param reasonForLoan
	 *            The reasonForLoan to set.
	 */
	public void setReasonForLoan(String reasonForLoan) {
		this.reasonForLoan = reasonForLoan;
	}

	/**
	 * @return Returns the reasonForLoan.
	 */
	public String getReasonForLoan() {
		return reasonForLoan;
	}

	public boolean isContinueAction() {

		return getAction().equals(CONTINUE_ACTION);

	}

	/**
	 * @param legallyMarried
	 *            The legallyMarried to set.
	 */
	public void setLegallyMarried(String legallyMarried) {
		this.legallyMarried = legallyMarried;
	}

	/**
	 * @return Returns the legallyMarried.
	 */
	public String getLegallyMarried() {
		return legallyMarried;
	}

	/**
	 * @return the onlineLoan
	 */
	public boolean isOnlineLoan() {
		return onlineLoan;
	}

	/**
	 * @param onlineLoan the onlineLoan to set
	 */
	public void setOnlineLoan(boolean onlineLoan) {
		this.onlineLoan = onlineLoan;
	}

}