package com.manulife.pension.ps.web.ipitool;

import java.io.Serializable;

import com.manulife.pension.service.fee.valueobject.BasicAssetChargeRate;

public class BasicAssetChargeLine implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String bandStart;
    private String bandEnd;
    private String charge;
    
    private BasicAssetChargeRate basicAssetChargeRate;
    
    /**
	 * @return the basicAssetChargeRate
	 */
	public BasicAssetChargeRate getBasicAssetChargeRate() {
		return basicAssetChargeRate;
	}
	/**
	 * @param basicAssetChargeRate the basicAssetChargeRate to set
	 */
	public void setBasicAssetChargeRate(BasicAssetChargeRate basicAssetChargeRate) {
		this.basicAssetChargeRate = basicAssetChargeRate;
	}
	public String getBandStart() { return bandStart; }
    public void setBandStart(String bandStart) { this.bandStart = bandStart; }
    public String getBandEnd() { return bandEnd; }
    public void setBandEnd(String bandEnd) { this.bandEnd = bandEnd; }
    public String getCharge() { return charge; }
    public void setCharge(String charge) { this.charge = charge; }
    
}
