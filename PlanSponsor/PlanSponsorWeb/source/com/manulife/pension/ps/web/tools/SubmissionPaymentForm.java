/*
 * Created on Jan 12, 2005
 */
package com.manulife.pension.ps.web.tools;

import java.util.Collection;

import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.service.contract.valueobject.ContractPaymentInfoVO;

/**
 * @author Tony Tomasone
 */
public interface SubmissionPaymentForm {
	
	public static int CASH_HEIGHT = 48;
	
	public static int DEBIT_HEIGHT = 30;
	
	public static final String CASH_ACCOUNT_TYPE = "C";

	public static final int MAX_PAYMENT_TABLE_HEIGHT = 168;
	
	public static final int MIN_PAYMENT_TABLE_HEIGHT = DEBIT_HEIGHT;

	public abstract Collection getAccounts();

	public abstract void setAccounts(Collection collection);

	/**
	 * @return Returns the isDisplayBillPaymentSection.
	 */
	public abstract boolean isDisplayBillPaymentSection();

	/**
	 * @return Returns the isDisplayBillPaymentSection.
	 */
	public abstract boolean isDisplayMoreSections();

	/**
	 * @param isDisplayBillPaymentSection The isDisplayBillPaymentSection to set.
	 */
	public abstract void setDisplayBillPaymentSection(
			boolean isDisplayBillPaymentSection);

	/**
	 * @return Returns the isDisplayTemporaryCreditSection.
	 */
	public abstract boolean isDisplayTemporaryCreditSection();

	/**
	 * @param isDisplayTemporaryCreditSection The isDisplayTemporaryCreditSection to set.
	 */
	public abstract void setDisplayTemporaryCreditSection(
			boolean isDisplayTemporaryCreditSection);

	/**
	 * @return Returns the paymentInfo.
	 */
	public abstract ContractPaymentInfoVO getPaymentInfo();

	/**
	 * @param paymentInfo The paymentInfo to set.
	 */
	public abstract void setPaymentInfo(ContractPaymentInfoVO paymentInfo);

	/**
	 * @return Returns the isViewMode.
	 */
	public abstract boolean isViewMode();

	/**
	 * @param isViewMode The isViewMode to set.
	 */
	public abstract void setViewMode(boolean isViewMode);
	
	public abstract ContributionDetailsReportData getTheReport();
	
	public abstract int getPaymentTableHeight();
	
}