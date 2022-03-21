package com.manulife.pension.ps.web.investment;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is the action form for fundcheck page.
 * 
 * @author Ilamparithi
 * 
 */
public class FundCheckForm extends AutoForm {

	private static final long serialVersionUID = 1L;

	private boolean fundCheckPDFAvailable = true;
	private int pdfCount;
	private FundCheckDocumentEntity currentFundCheck;
	private FundCheckDocumentEntity previousFundCheck;
	private String selectedSeason;
	private String selectedYear;
	private String selectedNotice;
	private String selectedParticipantNotice; 
	private String selectedLanguage;

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public String getSelectedParticipantNotice() {
		return selectedParticipantNotice;
	}

	public void setSelectedParticipantNotice(String selectedParticipantNotice) {
		this.selectedParticipantNotice = selectedParticipantNotice;
	}

	public String getSelectedNotice() {
		return selectedNotice;
	}

	public void setSelectedNotice(String selectedNotice) {
		this.selectedNotice = selectedNotice;
	}

	/**
	 * Returns the fundCheckPDFAvailable flag
	 * 
	 * @return the isFundCheckPDFAvailable
	 */
	public boolean isFundCheckPDFAvailable() {
		return fundCheckPDFAvailable;
	}

	/**
	 * Sets the fundCheckPDFAvailable flag
	 * 
	 * @param isFundCheckPDFAvailable
	 *            the isFundCheckPDFAvailable to set
	 */
	public void setFundCheckPDFAvailable(boolean isFundCheckPDFAvailable) {
		this.fundCheckPDFAvailable = isFundCheckPDFAvailable;
	}

	/**
	 * Returns the no. of pdf info returned
	 * 
	 * @return the pdfCount
	 */
	public int getPdfCount() {
		return pdfCount;
	}

	/**
	 * Sets the no. of pdf info returned
	 * 
	 * @param pdfCount
	 *            the pdfCount to set
	 */
	public void setPdfCount(int pdfCount) {
		this.pdfCount = pdfCount;
	}

	/**
	 * Returns the FundCheckDocumentEntity object for current fund check pdf
	 * 
	 * @return the currentFundCheck
	 */
	public FundCheckDocumentEntity getCurrentFundCheck() {
		return currentFundCheck;
	}

	/**
	 * Sets the FundCheckDocumentEntity object for current fund check pdf
	 * 
	 * @param currentFundCheck
	 *            the currentFundCheck to set
	 */
	public void setCurrentFundCheck(FundCheckDocumentEntity currentFundCheck) {
		this.currentFundCheck = currentFundCheck;
	}

	/**
	 * Returns the FundCheckDocumentEntity for previous fund check
	 * 
	 * @return the previousFundCheck
	 */
	public FundCheckDocumentEntity getPreviousFundCheck() {
		return previousFundCheck;
	}

	/**
	 * Sets the FundCheckDocumentEntity for previous fund check
	 * 
	 * @param previousFundCheck
	 *            the previousFundCheck to set
	 */
	public void setPreviousFundCheck(FundCheckDocumentEntity previousFundCheck) {
		this.previousFundCheck = previousFundCheck;
	}

	/**
	 * Returns the season of the selected pdf
	 * 
	 * @return the selectedSeason
	 */
	public String getSelectedSeason() {
		return selectedSeason;
	}

	/**
	 * Sets the season of the selected pdf
	 * 
	 * @param selectedSeason
	 *            the selectedSeason to set
	 */
	public void setSelectedSeason(String selectedSeason) {
		this.selectedSeason = selectedSeason;
	}

	/**
	 * Returns the year of the selected pdf
	 * 
	 * @return the selectedYear
	 */
	public String getSelectedYear() {
		return selectedYear;
	}

	/**
	 * Sets the year of the selected pdf
	 * 
	 * @param selectedYear
	 *            the selectedYear to set
	 */
	public void setSelectedYear(String selectedYear) {
		this.selectedYear = selectedYear;
	}

}
