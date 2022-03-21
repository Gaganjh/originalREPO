package com.manulife.pension.platform.web.investment;

import com.manulife.pension.common.PhoneNumber;
import com.manulife.pension.platform.web.controller.AutoForm;


/**
 * This action form handles properties in ipsParticipantOverlay.jsp page.
 *  
 * @author Vellaisamy S
 *
 */
public class IPSViewParticiapantNotificationForm extends AutoForm {

	private static final long serialVersionUID = 1L;

	private String contactName;
	
    private String street;
    
    private String cityAndState;
    
    private String zipCode;
    
    private String contactInformation;
    
    private PhoneNumber telephoneNumber = new PhoneNumber();
    
    private String comments;
    

	/**
	 * @return contactName
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param contactName
	 * 			the contactName to set.
	 */
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	/**
	 * @return contactInformation
	 */
	public String getContactInformation() {
		return contactInformation;
	}

	/**
	 * @param contactInformation
	 * 			the contactInformation to set
	 */
	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}

	/**
	 * @return comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments
	 * 			the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCityAndState() {
        return cityAndState;
    }

    public void setCityAndState(String cityAndState) {
        this.cityAndState = cityAndState;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public PhoneNumber getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(PhoneNumber telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

	public void reset() {
		this.contactName = null;
		this.contactInformation = null;
		this.comments = null;
		this.street = null;
		this.cityAndState = null;
		this.zipCode = null;
		this.telephoneNumber = new PhoneNumber();;
	}

	
}
