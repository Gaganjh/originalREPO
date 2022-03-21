/*
 * Created on May 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.manulife.pension.ps.web.resources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.account.valueobject.LoanPayrollFrequency;

/**
 * @author sternlu
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class AmortizationScheduleForm extends AutoForm {

	private String contractName;

	private String participantName;

	private String loanDate;
	private Date   dLoanDate;

	private String firstPaymentDate;
	private Date   dFirstPaymentDate;

	private String loanAmount;
	private BigDecimal bdLoanAmount;

	private String amortizationYears;
	private int iAmortizationYears;

	private String paymentsPerYear;// repayment frequency
	private int iPaymentYear;

	private String nominalAnnualRate;
	private BigDecimal bdNominalAnnualRate;
	
	private String repaymentFrequency;

	private boolean validated;
	private String actionLabel;

	public static final String FIELD_CONTRACT_NAME = "contractName";

	public static final String FIELD_PARTICIPANT_NAME = "participantName";

	public static final String FIELD_LOAN_DATE = "loanDate";

	public static final String FIELD_FIRST_PAYMENT_DATE = "firstPaymentDate";

	public static final String FIELD_LOAN_AMOUNT = "loanAmount";

	public static final String FIELD_AMORTIZATION_YEARS = "amortizationYears";

	public static final String FIELD_PAYMENT_PER_YEAR = "paymentsPerYear";
	public static final String FIELD_REPAYMENT_FREQUENCY ="repaymentFrequency";

	public static final String FIELD_NOMINAL_ANNUAL_RATE = "nominalAnnualRate";

	public static final String GENERATE_PDF_ACTION = "generatePdf";
	private static final Map ACTION_LABEL_MAP = new HashMap();
	public static final Collection PAYROLL_FREQUENCY_LIST = getPayrollFrequencyList();

	static {
		ACTION_LABEL_MAP.put("Generate Pdf", "generatePdf");
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
	 * @param contractName
	 *            The contractName to set.
	 */

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	/**
	 * @return Returns the contractName.
	 */
	public String getContractName() {
		return contractName;
	}

	/**
	 * @param participantName
	 *            The participantName to set.
	 */
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	/**
	 * @return Returns the participantName.
	 */
	public String getParticipantName() {
		return participantName;
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
	 * @param firstPaymentDate
	 *            The firstPaymentDate to set.
	 */
	public void setFirstPaymentDate(String firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}

	/**
	 * @return Returns the firstPaymentDate.
	 */
	public String getFirstPaymentDate() {
		return firstPaymentDate;
	}

	/**
	 * @param loanAmount
	 *            The loanAmount to set.
	 */
	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	/**
	 * @return Returns the loanAmount.
	 */
	public String getLoanAmount() {
		return loanAmount;
	}

	/**
	 * @param amortizationYears
	 *            The amortizationYears to set.
	 */
	public void setAmortizationYears(String amortizationYears) {
		this.amortizationYears = amortizationYears;
	}

	/**
	 * @return Returns the amortizationYears.
	 */
	public String getAmortizationYears() {
		return amortizationYears;
	}

	/**
	 * @param paymentsPerYear
	 *            The paymentsPerYear to set.
	 */
	public void setPaymentsPerYear(String paymentsPerYear) {
		this.paymentsPerYear = paymentsPerYear;
	}

	/**
	 * @return Returns the paymentsPerYear.
	 */
	public String getPaymentsPerYear() {
		return paymentsPerYear;
	}

	/**
	 * @param nominalAnnualRate
	 *            The nominalAnnualRate to set.
	 */
	public void setNominalAnnualRate(String nominalAnnualRate) {
		this.nominalAnnualRate = nominalAnnualRate;
	}

	/**
	 * @return Returns the nominalAnnualRate.
	 */
	public String getNominalAnnualRate() {
		return nominalAnnualRate;
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

	public boolean isGeneratePdfAction() {
		return getAction().equals(GENERATE_PDF_ACTION);
		
	}

	/**
	 * @param dLoanDate The dLoanDate to set.
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
	 * @param dFirstPaymentDate The dFirstPaymentDate to set.
	 */
	public void setDFirstPaymentDate(Date dFirstPaymentDate) {
		this.dFirstPaymentDate = dFirstPaymentDate;
	}

	/**
	 * @return Returns the dFirstPaymentDate.
	 */
	public Date getDFirstPaymentDate() {
		return dFirstPaymentDate;
	}

	/**
	 * @param bdLoanAmount The bdLoanAmount to set.
	 */
	public void setBdLoanAmount(BigDecimal bdLoanAmount) {
		this.bdLoanAmount = bdLoanAmount;
	}

	/**
	 * @return Returns the bdLoanAmount.
	 */
	public BigDecimal getBdLoanAmount() {
		return bdLoanAmount;
	}

	/**
	 * @param iPaymentYear The iPaymentYear to set.
	 */
	public void setIPaymentYear(int iPaymentYear) {
		this.iPaymentYear = iPaymentYear;
	}

	/**
	 * @return Returns the iPaymentYear.
	 */
	public int getIPaymentYear() {
		return iPaymentYear;
	}

	/**
	 * @param iAmortizationYears The iAmortizationYears to set.
	 */
	public void setIAmortizationYears(int iAmortizationYears) {
		this.iAmortizationYears = iAmortizationYears;
	}

	/**
	 * @return Returns the iAmortizationYears.
	 */
	public int getIAmortizationYears() {
		return iAmortizationYears;
	}

	/**
	 * @param bdNominalAnnualRate The bdNominalAnnualRate to set.
	 */
	public void setBdNominalAnnualRate(BigDecimal bdNominalAnnualRate) {
		this.bdNominalAnnualRate = bdNominalAnnualRate;
	}

	/**
	 * @return Returns the bdNominalAnnualRate.
	 */
	public BigDecimal getBdNominalAnnualRate() {
		return bdNominalAnnualRate;
	}
	/**
	 * @param repaymentFrequency The repaymentFrequency to set.
	 */
	public void setRepaymentFrequency(String repaymentFrequency) {
		this.repaymentFrequency = repaymentFrequency;
	}
	/**
	 * @return Returns the repaymentFrequency.
	 */
	public String getRepaymentFrequency() {
		return repaymentFrequency;
	}
	private static  Collection getPayrollFrequencyList()
	{
		
		
		Collection list = new ArrayList();
		LoanPayrollFrequency[] payrollFrequency = LoanPayrollFrequency.LOAN_PAYROLL_FREQUENCIES;
		String label= null;
		String value =null;
		for(int i=0;i< payrollFrequency.length; i++)
		{
		    label =payrollFrequency[i].getDescription();
		    value = new Integer(payrollFrequency[i].getPeriodsPerYear()).toString();
		    list.add(new LabelValueBean(label, value));

		}
		return list;
	}


}