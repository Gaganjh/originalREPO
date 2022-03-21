package com.manulife.pension.ps.web.fee;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is used to hold form info for regulatory disclosure page
 * 
 * @author Eswar
 * 
 */
public class RegulatoryDisclosureForm extends AutoForm {

	/**
	 * Default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	private boolean hasInforceFeeDisclosurePdf = false;
	private boolean feeDisclosureAvaialable = false;
	private String contractStatus = "";
	
	private boolean show404a5Section;
	private boolean showPiNoticeLink;
    private boolean showIccLink;
	private boolean showIccCalendarYearMessage;
	private boolean showMissingIccContactMessage;
	private boolean showIpiAddendumLink;
	private boolean showParticipantFundChangeNoticeTemplate;
	private boolean showParticipantStatementFeesTool;
	private boolean showIpiHypotheticalToolLink;
	private boolean show404a5NoticeInfoTool;
	
	private boolean svfIndicator = false;
	private boolean investmentRelatedCostsPageAvailable = false;
	
	private boolean pinpointContract = false;
	private String stableValueFundId = "";
	
	private boolean show404a5AddendumTransactionProcessingProcessFeeLink = false;
	private boolean download404a5AddendumTemplate = false;
	
	// RP and R1 fund suite discloser
	private boolean rPandR1Indicator = false;
	
	private boolean lTIndicator = false;
	private boolean contractandProductRestrictionFlag = false;
	
	
	/**
	 * @return the rPandR1Indicator
	 */
	
	public boolean isrPandR1Indicator() {
		return rPandR1Indicator;
	}

	public void setrPandR1Indicator(boolean rPandR1Indicator) {
		this.rPandR1Indicator = rPandR1Indicator;
	}

	/**
	 * @return the svfIndicator
	 */
	public boolean isSvfIndicator() {
		return svfIndicator;
	}

	/**
	 * @param svfIndicator
	 *            the svfIndicator to set
	 */
	public void setSvfIndicator(boolean svfIndicator) {
		this.svfIndicator = svfIndicator;
	}

	/**
	 * @return the contractStatus
	 */
	public String getContractStatus() {
		return contractStatus;
	}

	/**
	 * @param contractStatus
	 *            the contractStatus to set
	 */
	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}
	
	public void setShow404a5Section(boolean show) { show404a5Section = show; }
	public boolean getShow404a5Section() { return show404a5Section; }
	
	public void setShowPiNoticeLink(boolean show) { showPiNoticeLink = show; }
    public boolean getShowPiNoticeLink() { return showPiNoticeLink; }
    
    public void setShowIccLink(boolean show) { showIccLink = show; }
    public boolean getShowIccLink() { return showIccLink; }
    
    public void setShowIccCalendarYearMessage(boolean show) { showIccCalendarYearMessage = show; }
    public boolean getShowIccCalendarYearMessage() { return showIccCalendarYearMessage; }
    
    public void setShowMissingIccContactMessage(boolean show) { showMissingIccContactMessage = show; }
    public boolean getShowMissingIccContactMessage() { return showMissingIccContactMessage; }
    
    public void setShowIpiAddendumLink(boolean show) { showIpiAddendumLink = show; }
    public boolean getShowIpiAddendumLink() { return showIpiAddendumLink; }
    
    public void setShowParticipantFundChangeNoticeTemplate(boolean show) { showParticipantFundChangeNoticeTemplate = show; }
    public boolean getShowParticipantFundChangeNoticeTemplate() { return showParticipantFundChangeNoticeTemplate; }
    
    public void setShowParticipantStatementFeesTool(boolean show) { showParticipantStatementFeesTool = show; }
    public boolean getShowParticipantStatementFeesTool() { return showParticipantStatementFeesTool; }

    public void setShowIpiHypotheticalToolLink(boolean showIpiHypotheticalToolLink) { this.showIpiHypotheticalToolLink = showIpiHypotheticalToolLink; }
    public boolean getShowIpiHypotheticalToolLink() { return showIpiHypotheticalToolLink; }
    
    public void setShow404a5NoticeInfoTool(boolean show404a5NoticeInfoTool) { this.show404a5NoticeInfoTool = show404a5NoticeInfoTool; }
    public boolean getShow404a5NoticeInfoTool() { return show404a5NoticeInfoTool; }
    

	/**
	 * 
	 * @return hasInforceFeeDisclosurePdf
	 */
	public boolean getHasInforceFeeDisclosurePdf() {
		return hasInforceFeeDisclosurePdf;
	}

	/**
	 * @param hasInforceFeeDisclosurePdf
	 */
	public void setHasInforceFeeDisclosurePdf(boolean hasInforceFeeDisclosurePdf) {
		this.hasInforceFeeDisclosurePdf = hasInforceFeeDisclosurePdf;
	}

	/**
	 * 
	 * @return feeDisclosureAvaialable
	 */
	public boolean isFeeDisclosureAvaialable() {
		return feeDisclosureAvaialable;
	}

	/**
	 * @param feeDisclosureAvaialable
	 */
	public void setFeeDisclosureAvaialable(boolean feeDisclosureAvaialable) {
		this.feeDisclosureAvaialable = feeDisclosureAvaialable;
	}

	/**
	 * @return the investmentRelatedCostsPageAvailable
	 */
	public boolean isInvestmentRelatedCostsPageAvailable() {
		return investmentRelatedCostsPageAvailable;
	}

	/**
	 * @param investmentRelatedCostsPageAvailable the investmentRelatedCostsPageAvailable to set
	 */
	public void setInvestmentRelatedCostsPageAvailable(
			boolean investmentRelatedCostsPageAvailable) {
		this.investmentRelatedCostsPageAvailable = investmentRelatedCostsPageAvailable;
	}

	/**
	 * @return the pinpointContract
	 */
	public boolean isPinpointContract() {
		return pinpointContract;
	}

	/**
	 * @param pinpointContract the pinpointContract to set
	 */
	public void setPinpointContract(boolean pinpointContract) {
		this.pinpointContract = pinpointContract;
	}

	/**
	 * Returns Stable Value Fund Id.
	 * 
	 * @return stableValueFundId
	 */
	public String getStableValueFundId() {
		return stableValueFundId;
	}

	/**
	 * Sets Stable Value Fund Id to given id.
	 * 
	 * @param stableValueFundId
	 */
	public void setStableValueFundId(String stableValueFundId) {
		this.stableValueFundId = stableValueFundId;
	}

	/**
	 * @return the show404a5AddendumTransactionProcessingProcessFeeLink
	 */
	public boolean isShow404a5AddendumTransactionProcessingProcessFeeLink() {
		return show404a5AddendumTransactionProcessingProcessFeeLink;
	}

	/**
	 * @param show404a5AddendumTransactionProcessingProcessFeeLink the show404a5AddendumTransactionProcessingProcessFeeLink to set
	 */
	/*public void setShow404a5AddendumTransactionProcessingProcessFeeLink(
			boolean show404a5AddendumTransactionProcessingProcessFeeLink) {
		this.show404a5AddendumTransactionProcessingProcessFeeLink = show404a5AddendumTransactionProcessingProcessFeeLink;
	}*/

	/**
	 * @return the download404a5AddendumTemplate
	 */
	public boolean isDownload404a5AddendumTemplate() {
		return download404a5AddendumTemplate;
	}

	/**
	 * @param download404a5AddendumTemplate the download404a5AddendumTemplate to set
	 */
	public void setDownload404a5AddendumTemplate(
			boolean download404a5AddendumTemplate) {
		this.download404a5AddendumTemplate = download404a5AddendumTemplate;
	}
	
	/**
	 * @return the lTIndicator
	 */
	public boolean islTIndicator() {
		return lTIndicator;
	}

	/**
	 * @param lTIndicator the lTIndicator to set
	 */
	public void setlTIndicator(boolean lTIndicator) {
		this.lTIndicator = lTIndicator;
	}
	
	/**
	 * @return the contractandProductRestrictionFlag
	 */
	public boolean isContractandProductRestrictionFlag() {
		return contractandProductRestrictionFlag;
	}

	/**
	 * @param contractandProductRestrictionFlag the contractandProductRestrictionFlag to set
	 */
	public void setContractandProductRestrictionFlag(boolean contractandProductRestrictionFlag) {
		this.contractandProductRestrictionFlag = contractandProductRestrictionFlag;
	}
	

}
