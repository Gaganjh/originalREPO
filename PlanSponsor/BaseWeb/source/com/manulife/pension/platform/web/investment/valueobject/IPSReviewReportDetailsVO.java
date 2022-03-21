package com.manulife.pension.platform.web.investment.valueobject;

/**
 * Value Object for IPS Review Report Details
 * 
 * @author Karthik
 *
 */
public class IPSReviewReportDetailsVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String reviewRequestId;
	
	private String contractId;
	
	private boolean isViewAvailable;
	
	private boolean isEditAvailable;
	
	private boolean isCancelAvailable;
	
	private boolean isCurrentReportLinkAccessible;
	
	private boolean isPreviousReportLinkAccessible;
	
	private String annualReviewDate;
	
	private String reviewRequestStatus;
	
	private String reviewRequestSubStatus;
	
	private boolean isParticipantNoticationAvailable;
	
	private boolean isCurrentReview;
	
	private String previousReportLabel;
	
	private String viewResultsDisplay;
	
	private boolean showNoFundMatchingTresholdIcon;

	/**
	 * @return the reviewRequestId
	 */
	public String getReviewRequestId() {
		return reviewRequestId;
	}

	/**
	 * @param reviewRequestId the reviewRequestId to set
	 */
	public void setReviewRequestId(String reviewRequestId) {
		this.reviewRequestId = reviewRequestId;
	}

	/**
	 * @return the contractId
	 */
	public String getContractId() {
		return contractId;
	}

	/**
	 * @param contractId the contractId to set
	 */
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	/**
	 * @return the isViewAvailable
	 */
	public boolean isViewAvailable() {
		return isViewAvailable;
	}

	/**
	 * @param isViewAvailable the isViewAvailable to set
	 */
	public void setViewAvailable(boolean isViewAvailable) {
		this.isViewAvailable = isViewAvailable;
	}

	/**
	 * @return the isEditAvailable
	 */
	public boolean isEditAvailable() {
		return isEditAvailable;
	}

	/**
	 * @param isEditAvailable the isEditAvailable to set
	 */
	public void setEditAvailable(boolean isEditAvailable) {
		this.isEditAvailable = isEditAvailable;
	}

	/**
	 * @return the isCancelAvailable
	 */
	public boolean isCancelAvailable() {
		return isCancelAvailable;
	}

	/**
	 * @param isCancelAvailable the isCancelAvailable to set
	 */
	public void setCancelAvailable(boolean isCancelAvailable) {
		this.isCancelAvailable = isCancelAvailable;
	}

	/**
	 * @return the isCurrentReportLinkAccessible
	 */
	public boolean isCurrentReportLinkAccessible() {
		return isCurrentReportLinkAccessible;
	}

	/**
	 * @param isCurrentReportLinkAccessible the isCurrentReportLinkAccessible to set
	 */
	public void setCurrentReportLinkAccessible(boolean isCurrentReportLinkAccessible) {
		this.isCurrentReportLinkAccessible = isCurrentReportLinkAccessible;
	}

	/**
	 * @return the isPreviousReportLinkAccessible
	 */
	public boolean isPreviousReportLinkAccessible() {
		return isPreviousReportLinkAccessible;
	}

	/**
	 * @param isPreviousReportLinkAccessible the isPreviousReportLinkAccessible to set
	 */
	public void setPreviousReportLinkAccessible(
			boolean isPreviousReportLinkAccessible) {
		this.isPreviousReportLinkAccessible = isPreviousReportLinkAccessible;
	}

	/**
	 * @return the annualReviewDate
	 */
	public String getAnnualReviewDate() {
		return annualReviewDate;
	}

	/**
	 * @param annualReviewDate the annualReviewDate to set
	 */
	public void setAnnualReviewDate(String annualReviewDate) {
		this.annualReviewDate = annualReviewDate;
	}

	/**
	 * @return the reviewRequestStatus
	 */
	public String getReviewRequestStatus() {
		return reviewRequestStatus;
	}

	/**
	 * @param reviewRequestStatus the reviewRequestStatus to set
	 */
	public void setReviewRequestStatus(String reviewRequestStatus) {
		this.reviewRequestStatus = reviewRequestStatus;
	}

	/**
	 * @return the reviewRequestSubStatus
	 */
	public String getReviewRequestSubStatus() {
		return reviewRequestSubStatus;
	}

	/**
	 * @param reviewRequestSubStatus the reviewRequestSubStatus to set
	 */
	public void setReviewRequestSubStatus(String reviewRequestSubStatus) {
		this.reviewRequestSubStatus = reviewRequestSubStatus;
	}

	/**
	 * @return the isParticipantNoticationAvailable
	 */
	public boolean isParticipantNoticationAvailable() {
		return isParticipantNoticationAvailable;
	}

	/**
	 * @param isParticipantNoticationAvailable the isParticipantNoticationAvailable to set
	 */
	public void setParticipantNoticationAvailable(
			boolean isParticipantNoticationAvailable) {
		this.isParticipantNoticationAvailable = isParticipantNoticationAvailable;
	}

	/**
	 * @return the isCurrentReview
	 */
	public boolean isCurrentReview() {
		return isCurrentReview;
	}

	/**
	 * @param isCurrentReview the isCurrentReview to set
	 */
	public void setCurrentReview(boolean isCurrentReview) {
		this.isCurrentReview = isCurrentReview;
	}

	/**
	 * @return the previousReportLabel
	 */
	public String getPreviousReportLabel() {
		return previousReportLabel;
	}

	/**
	 * @param previousReportLabel the previousReportLabel to set
	 */
	public void setPreviousReportLabel(String previousReportLabel) {
		this.previousReportLabel = previousReportLabel;
	}

	/**
	 * @return the viewResultsDisplay
	 */
	public String getViewResultsDisplay() {
		return viewResultsDisplay;
	}

	/**
	 * @param viewResultsDisplay the viewResultsDisplay to set
	 */
	public void setViewResultsDisplay(String viewResultsDisplay) {
		this.viewResultsDisplay = viewResultsDisplay;
	}

	public boolean isShowNoFundMatchingTresholdIcon() {
		return showNoFundMatchingTresholdIcon;
	}

	public void setShowNoFundMatchingTresholdIcon(
			boolean showNoFundMatchingTresholdIcon) {
		this.showNoFundMatchingTresholdIcon = showNoFundMatchingTresholdIcon;
	}
}
