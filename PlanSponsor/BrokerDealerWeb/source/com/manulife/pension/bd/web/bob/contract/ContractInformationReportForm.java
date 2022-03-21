package com.manulife.pension.bd.web.bob.contract;

import com.manulife.pension.platform.web.report.BaseReportForm;

/**
 * This is the form bean for Contract Informations page
 * 
 * @author Siby Thomas
 * 
 */
public class ContractInformationReportForm extends BaseReportForm {
    
    private static final long serialVersionUID = 1L;
    
    private boolean showFiduciaryWarranty;
    private boolean isPlanHighlightsCSFavailable;
    private int giflFeaturesContentId;
	private boolean hasInforceFeeDisclosurePdf;
	private boolean warningMessage;
	
	/**
     * Method to check the Plan Highlights CSF available or not
     * @return
     */
    public boolean getIsPlanHighlightsCSFavailable() {
		return isPlanHighlightsCSFavailable;
	}
    
	/**
	 * Method to set the Plan Highlights CSF available
	 * @param isPlanHighlightsCSFavailable
	 */
	public void setPlanHighlightsCSFavailable(boolean isPlanHighlightsCSFavailable) {
		this.isPlanHighlightsCSFavailable = isPlanHighlightsCSFavailable;
	}

	/**
     * gets the showFiduciaryWarranty indicator
     * @return String
     */
    public boolean getShowFiduciaryWarranty() {
        return showFiduciaryWarranty;
    }

    /**
     * sets the showFiduciaryWarranty indicator
     * @param showFiduciaryWarranty String
     */
    public void setShowFiduciaryWarranty(boolean showFiduciaryWarranty) {
        this.showFiduciaryWarranty = showFiduciaryWarranty;
    }

	/**
	 * Returns the content id of the gifl features section
	 * 
	 * @return the giflFeaturesContentId
	 */
	public int getGiflFeaturesContentId() {
		return giflFeaturesContentId;
	}

	/**
	 * Sets the content id of the gifl features section
	 * 
	 * @param giflFeaturesContentId the giflFeaturesContentId to set
	 */
	public void setGiflFeaturesContentId(int giflFeaturesContentId) {
		this.giflFeaturesContentId = giflFeaturesContentId;
	}

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
	 * @return the warningMessage
	 */
	public boolean isWarningMessage() {
		return this.warningMessage;
	}

	/**
	 * @param warningMessage the warningMessage to set
	 */
	public void setWarningMessage(boolean warningMessage) {
		this.warningMessage = warningMessage;
	}

}
