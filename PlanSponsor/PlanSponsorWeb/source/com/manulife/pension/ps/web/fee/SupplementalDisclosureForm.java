package com.manulife.pension.ps.web.fee;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


import com.manulife.pension.ps.web.controller.PsForm;

/**
 * This form is used for the internal pop up page referred to as the Supplemental Disclosure for
 * 408B2 regulations. The link to this pop up is on the regulatory disclosure page.
 * 
 * @author Mark Eldridge
 * 
 */
public class SupplementalDisclosureForm extends PsForm {

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    private Map<String, BigDecimal> recordKeepingFeeBreakdown; // The fee data for the page.

    private Date effectiveDate;
    
    private boolean pinpointContract = false;

    /**
     * Setter for the fee data.
     * 
     * @param feeDetails
     */
    public void setRecordKeepingFeeBreakdown(Map<String, BigDecimal> feeDetails) {
        this.recordKeepingFeeBreakdown = feeDetails;
    }

    /**
     * The getter for the fee data.
     * 
     * @return
     */
    public Map<String, BigDecimal> getRecordKeepingFeeBreakdown() {
        return recordKeepingFeeBreakdown;
    }

    public void setEffectiveDate(Date reportAsOfDate) {
        this.effectiveDate = reportAsOfDate;
    }

    /**
     * @return the effectiveDate
     */
    public Date getEffectiveDate() {
        return effectiveDate;
    }
    
    public BigDecimal getFee(String code) {
    	Map<String, BigDecimal> fees = getRecordKeepingFeeBreakdown();
    	if(fees != null){
    		return fees.get(code);
    	}
    	return null;
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
}
