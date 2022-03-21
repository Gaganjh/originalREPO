package com.manulife.pension.bd.web.bob.transaction;

import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * Action Form for Loan Repayment Details Report page.
 * 
 * @author harlomte
 * 
 */
public class LoanRepaymentDetailsReportForm extends BaseForm {

	private String profileId;
	private String maskedSsn;
	private String name;
	private String loanNumber;
	private String participantId;
	private boolean pdfCapped = false;
    private int cappedRowsInPDF = 0;
    
    /**
     * This method will return capped number of rows in PDF.
     * @return Returns the cappedRowsInPDF.
     */
    public int getCappedRowsInPDF() {
        return cappedRowsInPDF;
    }
    
    /**
     * This method sets capped number of rows in PDF.
     * @param cappedRowsInPDF The cappedRowsInPDF to set.
     */
    public void setCappedRowsInPDF(int cappedRowsInPDF) {
        this.cappedRowsInPDF = cappedRowsInPDF;
    }

    /**
     * This method will return true if the PDF has been capped.
     * @return boolean
     */
    public boolean getPdfCapped() {
        return pdfCapped;
    }

    /**
     * This method sets whether PDF capped or not.
     * @param pdfCapped
     */
    public void setPdfCapped(boolean pdfCapped) {
        this.pdfCapped = pdfCapped;
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
	 * Gets the maskedSsn
	 * @return Returns a String
	 */
	public String getMaskedSsn() {
		return maskedSsn;
	}
	/**
	 * Sets the maskedSsn
	 * @param maskedSsn The maskedSsn to set
	 */
	public void setMaskedSsn(String maskedSsn) {
		this.maskedSsn = maskedSsn;
	}


	/**
	 * Gets the name
	 * @return Returns a String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Gets the loanNumber
	 * @return Returns a String
	 */
	public String getLoanNumber() {
		return loanNumber;
	}
	/**
	 * Sets the loanNumber
	 * @param loanNumber The loanNumber to set
	 */
	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}


	/**
	 * Gets PArticipant Id
	 * @return String
	 */
	public String getParticipantId() {
		return participantId;
	}

	/**
	 * Sets Participant Id
	 * @param string
	 */
	public void setParticipantId(String string) {
		participantId = string;
	}

}
