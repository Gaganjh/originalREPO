package com.manulife.pension.bd.web.usermanagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


import com.manulife.pension.service.broker.valueobject.BrokerDealerFirm;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBDExtUserProfile;
import com.manulife.pension.service.security.bd.valueobject.ExtendedBasicBrokerUserProfile;

/**
 * This is the form class for BasicBrokerUserManagementAction
 * 
 * @author Ilamparithi
 * 
 */
public class BasicBrokerUserManagementForm extends
		AbstractUserManagementForm {

	private static final long serialVersionUID = 1L;

	public static final String FIRM_TYPE = "O";
	public static final String INDEPENDENT_TYPE = "I";
    public static final String FIELD_COMPANY_NAME = "companyName";

	private ExtendedBasicBrokerUserProfile basicBrokerUserProfile;

	private String partyType;

	private String companyName;

	private String firmId;

	private String firmName;
	
	public static final String FIELD_FIRMS = "firms";
	
	private String firmListStr;
	
	private String firmPermissionsListStr;

    private List<BrokerDealerFirm> firms = new ArrayList<BrokerDealerFirm>(0);

    private String selectedFirmId;

    private String selectedFirmName;
    
    private String riaFirmListStr;
	
	private String riaFirmPermissionsListStr;

	private List<BrokerDealerFirm> riafirms;

	private String selectedRiaFirmId;

	private String selectedRiaFirmName;

	@Override
	protected ExtendedBDExtUserProfile getExtUserProfile() {
		return basicBrokerUserProfile;
	}

	/**
	 * Return the display string for profile status
	 * 
	 * @return
	 */
	public String getProfileStatus() {
		if (basicBrokerUserProfile != null) {
			return UserManagementHelper.getSelfRegisteredProfileStatusMap()
					.get(basicBrokerUserProfile.getProfileStatus());
		} else {
			return null;
		}
	}

	/**
	 * Returns the basicBrokerUserProfile
	 * 
	 * @return ExtendedBasicBrokerUserProfile
	 */
	public ExtendedBasicBrokerUserProfile getBasicBrokerUserProfile() {
		return basicBrokerUserProfile;
	}

	/**
	 * Sets the basicBrokerUserProfile
	 * 
	 * @param basicBrokerUserProfile
	 */
	public void setBasicBrokerUserProfile(
			ExtendedBasicBrokerUserProfile basicBrokerUserProfile) {
		this.basicBrokerUserProfile = basicBrokerUserProfile;
	}

	/**
	 * Returns the partyType
	 * 
	 * @return the partyType
	 */
	public String getPartyType() {
		return partyType;
	}

	/**
	 * Sets the partyType
	 * 
	 * @param partyType
	 *            the partyType to set
	 */
	public void setPartyType(String partyType) {
		this.partyType = partyType;
	}

	/**
	 * Returns the companyName
	 * 
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * Sets the compnayName
	 * 
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = StringUtils.trimToEmpty(companyName);
	}

	/**
	 * Returns the firmId
	 * 
	 * @return the firmId
	 */
	public String getFirmId() {
		return firmId;
	}

	/**
	 * Sets the firmId
	 * 
	 * @param firmId
	 *            the firmId to set
	 */
	public void setFirmId(String firmId) {
		this.firmId = StringUtils.trimToNull(firmId);
	}

	/**
	 * Returns the firmName
	 * 
	 * @return the firmName
	 */
	public String getFirmName() {
		return firmName;
	}

	/**
	 * Sets the firmName
	 * 
	 * @param firmName
	 *            the firmName to set
	 */
	public void setFirmName(String firmName) {
		this.firmName = StringUtils.trimToEmpty(firmName);
	}

	public String getFirmListStr() {
		return firmListStr;
	}

	public void setFirmListStr(String firmListStr) {
		this.firmListStr = firmListStr;
	}
	
	public String getFirmPermissionsListStr() {
		return firmPermissionsListStr;
	}

	public void setFirmPermissionsListStr(String firmPermissionsListStr) {
		this.firmPermissionsListStr = firmPermissionsListStr;
	}

	public String getSelectedFirmId() {
		return selectedFirmId;
	}

	public void setSelectedFirmId(String selectedFirmId) {
		this.selectedFirmId = selectedFirmId;
	}

	public List<BrokerDealerFirm> getFirms() {
		return firms;
	}

	public void setFirms(List<BrokerDealerFirm> firms) {
		this.firms = firms;
	}

	public String getSelectedFirmName() {
		return selectedFirmName;
	}

	public void setSelectedFirmName(String selectedFirmName) {
		this.selectedFirmName = selectedFirmName;
	}

	public String getRiaFirmListStr() {
		return riaFirmListStr;
	}

	public void setRiaFirmListStr(String riaFirmListStr) {
		this.riaFirmListStr = riaFirmListStr;
	}

	public String getRiaFirmPermissionsListStr() {
		return riaFirmPermissionsListStr;
	}

	public void setRiaFirmPermissionsListStr(String riaFirmPermissionsListStr) {
		this.riaFirmPermissionsListStr = riaFirmPermissionsListStr;
	}

	public List<BrokerDealerFirm> getRiafirms() {
		return riafirms;
	}

	public void setRiafirms(List<BrokerDealerFirm> riafirms) {
		this.riafirms = riafirms;
	}

	public String getSelectedRiaFirmId() {
		return selectedRiaFirmId;
	}

	public void setSelectedRiaFirmId(String selectedRiaFirmId) {
		this.selectedRiaFirmId = selectedRiaFirmId;
	}

	public String getSelectedRiaFirmName() {
		return selectedRiaFirmName;
	}

	public void setSelectedRiaFirmName(String selectedRiaFirmName) {
		this.selectedRiaFirmName = selectedRiaFirmName;
	}
	@Override
    public void reset( HttpServletRequest request) {
    
    	super.setUpdateSuccess(false);
    	this.selectedRiaFirmName=StringUtils.EMPTY;
    	super.setAction(StringUtils.EMPTY);
    }

}
