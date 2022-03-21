package com.manulife.pension.platform.web.investment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.platform.web.investment.valueobject.IPSFundInstructionPresentation;
import com.manulife.pension.service.ipsr.valueobject.FundInstruction;

public class IPSReviewResultDetailsForm extends AutoForm {
	
	private static final long serialVersionUID = 1L;

	private String reviewRequestId;
	
	private List<IPSFundInstructionPresentation> ipsReviewFundInstructionList = new ArrayList<IPSFundInstructionPresentation>();
	
	private List<FundInstruction> fundInstructionList = new ArrayList<FundInstruction>();
	
	private boolean ipsIATEffectiveDateAvailable;
	
	private boolean pdfCapped;
	
	private boolean currentReview;
	
	private String agreeApproval;
	
	private String contactName;
	
	private String contactInformation;
	
	private String comments;
	
	private boolean flag;
	
	private Date asOfDate;
	
	private boolean contractRedemptionFeesAvailable;
	
	private boolean participantRedemptionFeesAvailable;
	
	private boolean reportLinkAvailable;
	
	private boolean participantNotificationAvailable;
	
	private String validDatesForJavaScript;
	
	private String iatStartDate;
	
	private String mode;
	
	private String annualReviewDate;
	
	private boolean formChanged;
	
	private boolean isFromViewPage;
	
	private boolean isFromApprovePage;
	
	private boolean isFromEditPage;
	
	private String processingDateForReportLink;
	
	private boolean nyseAvailable;
	
	private String nyseNotAvailableText;
	
	private Date nyseDownTime;
	
	private String ipsIatEffectiveDate;
	
	private Date currentDate;
	
	private boolean isAllFundInstructionsIgnored;
	
	private String action;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

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
	 * @return the ipsReviewFundInstructionList
	 */
	public List<IPSFundInstructionPresentation> getIpsReviewFundInstructionList() {
		return ipsReviewFundInstructionList;
	}

	/**
	 * @param ipsReviewFundInstructionList the ipsReviewFundInstructionList to set
	 */
	public void setIpsReviewFundInstructionList(
			List<IPSFundInstructionPresentation> ipsReviewFundInstructionList) {
		this.ipsReviewFundInstructionList = ipsReviewFundInstructionList;
	}

	/**
	 * @return the fundInstructionList
	 */
	public List<FundInstruction> getFundInstructionList() {
		return fundInstructionList;
	}

	/**
	 * @param fundInstructionList the fundInstructionList to set
	 */
	public void setFundInstructionList(List<FundInstruction> fundInstructionList) {
		this.fundInstructionList = fundInstructionList;
	}

	/**
	 * @return the ipsIATEffectiveDateAvailable
	 */
	public boolean isIpsIATEffectiveDateAvailable() {
		return ipsIATEffectiveDateAvailable;
	}

	/**
	 * @param ipsIATEffectiveDateAvailable the ipsIATEffectiveDateAvailable to set
	 */
	public void setIpsIATEffectiveDateAvailable(boolean ipsIATEffectiveDateAvailable) {
		this.ipsIATEffectiveDateAvailable = ipsIATEffectiveDateAvailable;
	}

	/**
	 * @return the pdfCapped
	 */
	public boolean isPdfCapped() {
		return pdfCapped;
	}

	/**
	 * @param pdfCapped the pdfCapped to set
	 */
	public void setPdfCapped(boolean pdfCapped) {
		this.pdfCapped = pdfCapped;
	}

	public boolean isCurrentReview() {
		return currentReview;
	}

	public void setCurrentReview(boolean currentReview) {
		this.currentReview = currentReview;
	}

	/**
	 * @return the agreeApproval
	 */
	public String getAgreeApproval() {
		return agreeApproval;
	}

	/**
	 * @param agreeApproval the agreeApproval to set
	 */
	public void setAgreeApproval(String agreeApproval) {
		this.agreeApproval = agreeApproval;
	}

	/**
	 * @return the contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName the contactName to set
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return the contactInformation
	 */
	public String getContactInformation() {
		return contactInformation;
	}

	/**
	 * @param contactInformation the contactInformation to set
	 */
	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * @return the asOfDate
	 */
	public Date getAsOfDate() {
		return asOfDate;
	}

	/**
	 * @param asOfDate the asOfDate to set
	 */
	public void setAsOfDate(Date asOfDate) {
		this.asOfDate = asOfDate;
	}

	/**
	 * @return the contractRedemptionFeesAvailable
	 */
	public boolean isContractRedemptionFeesAvailable() {
		return contractRedemptionFeesAvailable;
	}

	/**
	 * @param contractRedemptionFeesAvailable the contractRedemptionFeesAvailable to set
	 */
	public void setContractRedemptionFeesAvailable(
			boolean contractRedemptionFeesAvailable) {
		this.contractRedemptionFeesAvailable = contractRedemptionFeesAvailable;
	}

	/**
	 * @return the participantRedemptionFeesAvailable
	 */
	public boolean isParticipantRedemptionFeesAvailable() {
		return participantRedemptionFeesAvailable;
	}

	/**
	 * @param participantRedemptionFeesAvailable the participantRedemptionFeesAvailable to set
	 */
	public void setParticipantRedemptionFeesAvailable(
			boolean participantRedemptionFeesAvailable) {
		this.participantRedemptionFeesAvailable = participantRedemptionFeesAvailable;
	}

	/**
	 * @return the reportLinkAvailable
	 */
	public boolean isReportLinkAvailable() {
		return reportLinkAvailable;
	}

	/**
	 * @param reportLinkAvailable the reportLinkAvailable to set
	 */
	public void setReportLinkAvailable(boolean reportLinkAvailable) {
		this.reportLinkAvailable = reportLinkAvailable;
	}

	/**
	 * @return the participantNotificationAvailable
	 */
	public boolean isParticipantNotificationAvailable() {
		return participantNotificationAvailable;
	}

	/**
	 * @param participantNotificationAvailable the participantNotificationAvailable to set
	 */
	public void setParticipantNotificationAvailable(
			boolean participantNotificationAvailable) {
		this.participantNotificationAvailable = participantNotificationAvailable;
	}

	/**
	 * @return the validDatesForJavaScript
	 */
	public String getValidDatesForJavaScript() {
		return validDatesForJavaScript;
	}

	/**
	 * @param validDatesForJavaScript the validDatesForJavaScript to set
	 */
	public void setValidDatesForJavaScript(String validDatesForJavaScript) {
		this.validDatesForJavaScript = validDatesForJavaScript;
	}

	/**
	 * @return the iatStartDate
	 */
	public String getIatStartDate() {
		return iatStartDate;
	}

	/**
	 * @param iatStartDate the iatStartDate to set
	 */
	public void setIatStartDate(String iatStartDate) {
		this.iatStartDate = iatStartDate;
	}

	/**
	 * @return the mode
	 */
	public String getMode() {
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(String mode) {
		this.mode = mode;
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
	 * @return the formChanged
	 */
	public boolean isFormChanged() {
		return formChanged;
	}

	/**
	 * @param formChanged the formChanged to set
	 */
	public void setFormChanged(boolean formChanged) {
		this.formChanged = formChanged;
	}

	/**
	 * @return the isFromViewPage
	 */
	public boolean isFromViewPage() {
		return isFromViewPage;
	}

	/**
	 * @param isFromViewPage the isFromViewPage to set
	 */
	public void setFromViewPage(boolean isFromViewPage) {
		this.isFromViewPage = isFromViewPage;
	}

	/**
	 * @return the processingDateForReportLink
	 */
	public String getProcessingDateForReportLink() {
		return processingDateForReportLink;
	}

	/**
	 * @param processingDateForReportLink the processingDateForReportLink to set
	 */
	public void setProcessingDateForReportLink(String processingDateForReportLink) {
		this.processingDateForReportLink = processingDateForReportLink;
	}

	
	public boolean isFromApprovePage() {
		return isFromApprovePage;
	}

	public void setFromApprovePage(boolean isFromApprovePage) {
		this.isFromApprovePage = isFromApprovePage;
	}
	
	/**
	 * @return the isFromEditPage
	 */
	public boolean isFromEditPage() {
		return isFromEditPage;
	}

	/**
	 * @param isFromEditPage the isFromEditPage to set
	 */
	public void setFromEditPage(boolean isFromEditPage) {
		this.isFromEditPage = isFromEditPage;
	}

	/**
	 * @return the nyseAvailable
	 */
	public boolean isNyseAvailable() {
		return nyseAvailable;
	}

	/**
	 * @param nyseAvailable the nyseAvailable to set
	 */
	public void setNyseAvailable(boolean nyseAvailable) {
		this.nyseAvailable = nyseAvailable;
	}

	/**
	 * @return the nyseNotAvailableText
	 */
	public String getNyseNotAvailableText() {
		return nyseNotAvailableText;
	}

	/**
	 * @param nyseNotAvailableText the nyseNotAvailableText to set
	 */
	public void setNyseNotAvailableText(String nyseNotAvailableText) {
		this.nyseNotAvailableText = nyseNotAvailableText;
	}

	/**
	 * @return the nyseDownTime
	 */
	public Date getNyseDownTime() {
		return nyseDownTime;
	}

	/**
	 * @param nyseDownTime the nyseDownTime to set
	 */
	public void setNyseDownTime(Date nyseDownTime) {
		this.nyseDownTime = nyseDownTime;
	}

	/**
	 * @return the ipsIatEffectiveDate
	 */
	public String getIpsIatEffectiveDate() {
		return ipsIatEffectiveDate;
	}
	
	/**
	 * @param ipsIatEffectiveDate the ipsIatEffectiveDate to set
	 */
	public void setIpsIatEffectiveDate(String ipsIatEffectiveDate) {
		this.ipsIatEffectiveDate = ipsIatEffectiveDate;
	}

	/**
	 * @return the currentDate
	 */
	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate the currentDate to set
	 */
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	public void resetData(){
		this.reviewRequestId = null;
		this.ipsReviewFundInstructionList = new ArrayList<IPSFundInstructionPresentation>();	
		this.fundInstructionList = new ArrayList<FundInstruction>();	
		this.ipsIATEffectiveDateAvailable = false; 	
		this.pdfCapped = false;	
		this.currentReview = false;	
		this.agreeApproval  = null;
		this.contactName = null;	
		this.contactInformation = null;	
		this.comments = null;	
		this.flag = false;	
		this.asOfDate = null;	
		this.contractRedemptionFeesAvailable = false;	
		this.participantRedemptionFeesAvailable	= false;
		this.reportLinkAvailable = false;	
		this.participantNotificationAvailable = false;	
		this.validDatesForJavaScript = null;	
		this.iatStartDate = null;	
		this.mode = null;	
		this.annualReviewDate = null;	
		this.formChanged = false;	
		this.isFromViewPage = false;
		this.isFromApprovePage = false;
		this.isFromEditPage = false;
		this.nyseAvailable = false;
		this.nyseNotAvailableText = null;
		this.nyseDownTime = null;
		this.ipsIatEffectiveDate = null;
		this.currentDate = null;
	}

	/**
	 * @return the isAllFundInstructionsIgnored
	 */
	public boolean isAllFundInstructionsIgnored() {
		return isAllFundInstructionsIgnored;
	}

	/**
	 * @param isAllFundInstructionsIgnored the isAllFundInstructionsIgnored to set
	 */
	public void setAllFundInstructionsIgnored(boolean isAllFundInstructionsIgnored) {
		this.isAllFundInstructionsIgnored = isAllFundInstructionsIgnored;
	}
}
