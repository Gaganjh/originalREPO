package com.manulife.pension.ps.web.ipitool;

import java.io.Serializable;
import java.math.BigDecimal;

import com.manulife.pension.service.fee.valueobject.DiscontinuaceFee;

public class DiscontinuanceChargeLine
implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    
    
    
   
	private BigDecimal mveCharge;
    private  BigDecimal dbCompCharge;
    private BigDecimal participantFeeCharge;
    private DiscontinuaceFee discontinuaceFee;
    
    
    
    /**
	 * @return the discontinuaceFee
	 */
	public DiscontinuaceFee getDiscontinuaceFee() {
		return discontinuaceFee;
	}
	/**
	 * @param discontinuaceFee the discontinuaceFee to set
	 */
	public void setDiscontinuaceFee(DiscontinuaceFee discontinuaceFee) {
		this.discontinuaceFee = discontinuaceFee;
	}
	

   

    /**
	 * @return the mveCharge
	 */
	public BigDecimal getMveCharge() {
		return mveCharge;
	}
	/**
	 * @param mveCharge the mveCharge to set
	 */
	public void setMveCharge(BigDecimal mveCharge) {
		this.mveCharge = mveCharge;
	}
	/**
	 * @return the dbCompCharge
	 */
	public BigDecimal getDbCompCharge() {
		return dbCompCharge;
	}
	/**
	 * @param dbCompCharge the dbCompCharge to set
	 */
	public void setDbCompCharge(BigDecimal dbCompCharge) {
		this.dbCompCharge = dbCompCharge;
	}
	/**
	 * @return the participantFeeCharge
	 */
	public BigDecimal getParticipantFeeCharge() {
		return participantFeeCharge;
	}
	/**
	 * @param participantFeeCharge the participantFeeCharge to set
	 */
	public void setParticipantFeeCharge(BigDecimal participantFeeCharge) {
		this.participantFeeCharge = participantFeeCharge;
	}
}
