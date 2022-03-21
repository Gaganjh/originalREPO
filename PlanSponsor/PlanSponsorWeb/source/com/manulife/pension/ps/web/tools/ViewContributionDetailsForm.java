package com.manulife.pension.ps.web.tools;

import java.util.Collection;
import java.util.Iterator;

import com.manulife.pension.lp.model.gft.GFTUploadDetail;
import com.manulife.pension.ps.service.report.submission.valueobject.ContributionDetailsReportData;
import com.manulife.pension.ps.service.submission.util.SubmissionErrorHelper;
import com.manulife.pension.ps.service.submission.valueobject.SubmissionPaymentItem;
import com.manulife.pension.ps.web.report.ReportForm;
import com.manulife.pension.ps.web.tools.util.SubmissionHistoryItemActionHelper;
import com.manulife.pension.submission.SubmissionError;

/**
 * @author Tony Tomasone
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

public class ViewContributionDetailsForm extends ReportForm {
	private static final String EDIT_MODE_IND = "e";
	private String subNo;
	private String mode;
	private boolean isAllowedView = false;
	private ContributionDetailsReportData theReport;
	private boolean isViewMode = false;
	private boolean isEditFunctionAvailable = false;
	private boolean isDisplayPaymentInstructionSection = false;
	private boolean isShowLoans = true;
	private Collection statementDates;
    private Collection contractMoneyTypes;

	private static String ERROR_TYPE_ERROR = "error";
	private static String ERROR_TYPE_WARN = "warn";
	private static String ERROR_TYPE_NONE = "none";

	
	/**
	 * @return Returns the isAllowedView.
	 */
	public boolean isAllowedView() {
		return isAllowedView;
	}
	/**
	 * @param isAllowedView The isAllowedView to set.
	 */
	public void setAllowedView(boolean isAllowedView) {
		this.isAllowedView = isAllowedView;
	}
	/**
	 * @return Returns the subNo.
	 */
	public String getSubNo() {
		return subNo;
	}
	/**
	 * @param subNo The subNo to set.
	 */
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}
	/**
	 * @return Returns the isViewMode.
	 */
	public boolean isViewMode() {
		return isViewMode;
	}
	/**
	 * @param isViewMode The isViewMode to set.
	 */
	public void setViewMode(boolean isViewMode) {
		this.isViewMode = isViewMode;
	}
	/**
	 * @return Returns the isDisplayPaymentInstructionSection.
	 */
	public boolean isDisplayPaymentInstructionSection() {
		return isDisplayPaymentInstructionSection;
	}
	/**
	 * @param isDisplayPaymentInstructionSection The isDisplayPaymentInstructionSection to set.
	 */
	public void setDisplayPaymentInstructionSection(
			boolean isDisplayPaymentInstructionSection) {
		this.isDisplayPaymentInstructionSection = isDisplayPaymentInstructionSection;
	}
	/**
	 * @return Returns the theReport.
	 */
	public ContributionDetailsReportData getTheReport() {
		return theReport;
	}
	/**
	 * @param theReport The theReport to set.
	 */
	public void setTheReport(ContributionDetailsReportData theReport) {
		this.theReport = theReport;
	}
	/**
	 * @return Returns the statementDates.
	 */
	public Collection getStatementDates() {
		return statementDates;
	}
	/**
	 * @param statementDates The statementDates to set.
	 */
	public void setStatementDates(Collection statementDates) {
		this.statementDates = statementDates;
	}

	/**
	 * @return Returns the paymentDetails.
	 */
	public SubmissionUploadDetailBean getPaymentDetails() {
		
		SubmissionUploadDetailBean det = null;
		if (
				theReport != null && 
				theReport.getContributionData() != null &&
				theReport.getContributionData().getSubmissionPaymentItem() != null
		) {
			SubmissionPaymentItem paymentItem = theReport.getContributionData().getSubmissionPaymentItem();
			// show the payment section
			setDisplayPaymentInstructionSection(true);
			
			// use some existing DTOs for presentation of the payment details
			GFTUploadDetail result = new GFTUploadDetail();
			result.setContractName(paymentItem.getContractName());
			result.setContractNumber(String.valueOf(paymentItem.getContractId().toString()));
			result.setReceivedDate(paymentItem.getSubmissionDate());
			result.setRequestedPaymentEffectiveDate(paymentItem.getRequestedPaymentEffectiveDate());
			result.setSubmissionId(paymentItem.getSubmissionId().toString());
		
			
//			if (userProfile.isInternalUser()) {
//				result.setUserName(theReport.getContributionData().getSubmitterName() +	" (" + contributionItem.getSubmitterID() + ")");
//			} else {
//				result.setUserName(theReport.getContributionData().getSubmitterName());
//			}
			result.setUserName(theReport.getContributionData().getSubmitterName());
			result.setUserSSN(theReport.getContributionData().getSubmitterID());
		
			result.setPaymentInstructions(paymentItem.getPaymentInstructions());
			det = new SubmissionUploadDetailBean(result);
			det.setStatus(SubmissionHistoryItemActionHelper.getInstance().getDisplayStatus(theReport.getContributionData()));
		}
		return det;
	}
	

	public boolean isEditMode() {
		if ( EDIT_MODE_IND.equals(mode) ) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @return Returns the mode.
	 */
	public String getMode() {
		return mode;
	}
	/**
	 * @param mode The mode to set.
	 */
	public void setMode(String mode) {
		this.mode = mode;
	}
	/**
	 * @return Returns the error type.
	 */
	public boolean getErrorType(String field, int row, String severity) {
		if (
				theReport == null ||
				theReport.getContributionData() == null ||
				theReport.getContributionData().getReportDataErrors() == null
		)	return false;
		
		Iterator errorsIt = theReport.getContributionData().getReportDataErrors().getErrors().iterator();
		
		while (errorsIt.hasNext()) {
			SubmissionError error = (SubmissionError) errorsIt.next();
			if (error.getField().equals(field) && error.getRowNumber() == row) {
				if (SubmissionErrorHelper.isError(error) && ERROR_TYPE_ERROR.equals(severity))
					return true;
				else if (SubmissionErrorHelper.isWarning(error) && ERROR_TYPE_WARN.equals(severity))
					return true;
			}
		}
		return false;
	}
	public boolean isEditFunctionAvailable() {
		return this.isEditFunctionAvailable;
	}
	public void setEditFunctionAvailable(boolean isEditFunctionAvailable) {
		this.isEditFunctionAvailable = isEditFunctionAvailable;
	}
	/**
	 * @return Returns the isShowLoans.
	 */
	public boolean isShowLoans() {
		return isShowLoans;
	}
	/**
	 * @param isShowLoans The isShowLoans to set.
	 */
	public void setShowLoans(boolean isShowLoans) {
		this.isShowLoans = isShowLoans;
	}
    
    /**
     * @return Returns the contractMoneyTypes.
     */
    public Collection getContractMoneyTypes() {
        return contractMoneyTypes;
    }

    /**
     * @param contractMoneyTypes The contractMoneyTypes to set.
     */
    public void setContractMoneyTypes(Collection contractMoneyTypes) {
        this.contractMoneyTypes = contractMoneyTypes;
    }
}

