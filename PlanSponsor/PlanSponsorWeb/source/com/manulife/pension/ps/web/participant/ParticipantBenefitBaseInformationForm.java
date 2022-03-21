package com.manulife.pension.ps.web.participant;

import java.math.BigDecimal;
import java.util.Date;

import com.manulife.pension.ps.web.report.ReportForm;

public class ParticipantBenefitBaseInformationForm extends ReportForm{

	private static final long serialVersionUID = 1L;
	public static final String PARAMETER_KEY_PROFILE_ID = "profileId";
	public static final String PARAMETER_KEY_PARTICIPANT_ID = "participantId";
	public static final String FORMAT_DATE_SHORT_MDY = "MM/dd/yyyy";
	private String serviceInquiryLog;
	private Date asOfDate;
	private String fromDate;
	private String toDate;
	private String giflSelectionDate;
	private String profileId;
	private String participantId;
	private String proposalNumber;
	private String showFootnote = "N";
	private String bbBatchDateLessThenETL = "N";
	private String showTradingExpirationDate;
	
	private String displayTradingExpirationDate;
	private String rateForLastIncomeEnhancement;
	
	// Lifetime Income Amount Details
	private java.sql.Date liaSelectionDate;
	private String liaPercentage;
	private java.sql.Date liaAnniversaryDate;
	private BigDecimal liaAnnualAmount;
	private BigDecimal liaPeriodicAmt;
	private String liaIndividualOrSpousalOption;
	private String liaFrequencyCode;
	private boolean showLIADetailsSection;
	
	public ParticipantBenefitBaseInformationForm() {
	}
    
	public Date getAsOfDate() {
	    return asOfDate;
	}
	
	public void setAsOfDate(Date asOfDate) {
	    this.asOfDate = asOfDate;
	}
	
	
	/**
	 * Gets the fromDate
	 * @return String
	 */
	
	public String getFromDate() {
		return fromDate;
	}
    	
	/**
	 * Sets the fromDate
	 * @param fromDate The fromDate to set
	 */
	
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * Gets Participant Id
	 * @return String
	 */
	
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * Sets ParticipantId
	 * @param string The ParticipantId to set
	 */
	
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}

	/**
	 * Gets the profileId
	 * @return Returns a String
	 */
	
	public String getProfileId() {
		return profileId;
	}
    
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}
    
	/**
     * Gets the SIL
     * @return Retuns a String 
     */
	
	public String getServiceInquiryLog() {
		return serviceInquiryLog;
	}

	/**
	 * Sets the serviceInquiryLog
	 * @param serviceInquiryLog The serviceInquiryLog to set
	 */
	
	public void setServiceInquiryLog(String serviceInquiryLog) {
		this.serviceInquiryLog = serviceInquiryLog;
	}
	
	/**
	 * Gets the toDate
	 * @return String
	 */
	
	public String getToDate() {
		return toDate;
	}

	/**
	 * Sets the toDate
	 * @param toDate The toDate to set
	 */
	
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	/**
	 * Gets the proposalNumber
	 * @return Returns a String
	 */
	
	public String getProposalNumber() {
		return proposalNumber;
	}
	
	/**
	 * Sets the proposalNumber
	 * @param proposalNumber The proposalNumber to set
	 */
	
	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	/**
	 * Gets the showFootnote
	 * @return Returns a String
	 */
	
	public String getShowFootnote() {
		return showFootnote;
	}

	/**
	 * Sets the showFootnote
	 * @param showFootnote a flag to decide whether to show the footnote
	 */
	
	public void setShowFootnote(String showFootnote) {
		this.showFootnote = showFootnote;
	}

	/**
	 * @return the bbBatchDateLessThenETL
	 */
	public String getBbBatchDateLessThenETL() {
		return bbBatchDateLessThenETL;
	}

	/**
	 * @param bbBatchDateLessThenETL the bbBatchDateLessThenETL to set
	 */
	public void setBbBatchDateLessThenETL(String bbBatchDateLessThenETL) {
		this.bbBatchDateLessThenETL = bbBatchDateLessThenETL;
	}

	/**
	 * returns "Y" or "N" value to decide whether Trading Expiration Date should be displayed or not
	 * @return
	 */
	
	public String getShowTradingExpirationDate() {
		return showTradingExpirationDate;
	}

	/**
	 * This method is set 'Y' or 'N' value based on trading restriction expiration date value  
	 * @param showTradingExpirationDate
	 */
	public void setShowTradingExpirationDate(String showTradingExpirationDate) {
		this.showTradingExpirationDate = showTradingExpirationDate;
	}

	/**
	 * Display Trading Expiration Date in JSP for GIFL version 3 
	 * @return string value of Trading Expiration Date 
	 */
	public String getDisplayTradingExpirationDate() {
		return displayTradingExpirationDate;
	}

	/**
	 * Sets the trading Expiration Date
	 * @param displayTradingExpirationDate
	 */
	public void setDisplayTradingExpirationDate(String displayTradingExpirationDate) {
		this.displayTradingExpirationDate = displayTradingExpirationDate;
	}

	/**
	 * Display the field Rate for Last Income Enhancement 
	 * @return the string value of Rate for Last Income Enhancement
	 */
	public String getRateForLastIncomeEnhancement() {
		return rateForLastIncomeEnhancement;
	}
	
	/**
	 * Sets the rate for last income enhancement method
	 * @param rateForLastIncomeEnhancement
	 */
	public void setRateForLastIncomeEnhancement(String rateForLastIncomeEnhancement) {
		this.rateForLastIncomeEnhancement = rateForLastIncomeEnhancement;
	}

	/**
	 * Gets the giflSelectionDate
	 * @return String
	 */
	public String getGiflSelectionDate() {
		return giflSelectionDate;
	}
    	
	/**
	 * Sets the giflSelectionDate
	 * @param giflSelectionDate The giflSelectionDate to set
	 */
	public void setGiflSelectionDate(String giflSelectionDate) {
		this.giflSelectionDate = giflSelectionDate;
	}

	/**
	 * @return
	 */
	public java.sql.Date getLiaSelectionDate() {
		return liaSelectionDate;
	}

	/**
	 * @param liaSelectionDate
	 */
	public void setLiaSelectionDate(java.sql.Date liaSelectionDate) {
		this.liaSelectionDate = liaSelectionDate;
	}

	/**
	 * @return
	 */
	public String getLiaPercentage() {
		return liaPercentage;
	}

	/**
	 * @param liaPercentage
	 */
	public void setLiaPercentage(String liaPercentage) {
		this.liaPercentage = liaPercentage;
	}

	/**
	 * @return
	 */
	public java.sql.Date getLiaAnniversaryDate() {
		return liaAnniversaryDate;
	}

	/**
	 * @param liaAnniversaryDate
	 */
	public void setLiaAnniversaryDate(java.sql.Date liaAnniversaryDate) {
		this.liaAnniversaryDate = liaAnniversaryDate;
	}

	/**
	 * @return
	 */
	public BigDecimal getLiaAnnualAmount() {
		return liaAnnualAmount;
	}

	/**
	 * @param liaAnnualAmount
	 */
	public void setLiaAnnualAmount(BigDecimal liaAnnualAmount) {
		this.liaAnnualAmount = liaAnnualAmount;
	}

	/**
	 * @return
	 */
	public BigDecimal getLiaPeriodicAmt() {
		return liaPeriodicAmt;
	}

	/**
	 * @param liaPeriodicAmt
	 */
	public void setLiaPeriodicAmt(BigDecimal liaPeriodicAmt) {
		this.liaPeriodicAmt = liaPeriodicAmt;
	}

	/**
	 * @return
	 */
	public String getLiaIndividualOrSpousalOption() {
		return liaIndividualOrSpousalOption;
	}

	/**
	 * @param liaIndividualOrSpousalOption
	 */
	public void setLiaIndividualOrSpousalOption(String liaIndividualOrSpousalOption) {
		this.liaIndividualOrSpousalOption = liaIndividualOrSpousalOption;
	}

	/**
	 * @return
	 */
	public String getLiaFrequencyCode() {
		return liaFrequencyCode;
	}

	/**
	 * @param liaFrequencyCode
	 */
	public void setLiaFrequencyCode(String liaFrequencyCode) {
		this.liaFrequencyCode = liaFrequencyCode;
	}

	/**
	 * @return
	 */
	public boolean isShowLIADetailsSection() {
		return showLIADetailsSection;
	}

	/**
	 * @param showLIADetailsSection
	 */
	public void setShowLIADetailsSection(boolean showLIADetailsSection) {
		this.showLIADetailsSection = showLIADetailsSection;
	}
}
