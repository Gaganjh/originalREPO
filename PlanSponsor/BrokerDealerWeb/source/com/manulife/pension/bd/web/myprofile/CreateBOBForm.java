package com.manulife.pension.bd.web.myprofile;

/**
 * Form for promote the Level 1 broker to Level 2 broker
 * 
 * @author guweigu
 * 
 */
public class CreateBOBForm extends AddBOBForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean producerLicense;

	private boolean emailNotification = false;
	
	public Boolean getProducerLicense() {
		return producerLicense;
	}

	public void setProducerLicense(Boolean producerLicense) {
		this.producerLicense = producerLicense;
	}

	@Override
	public void clear() {
		super.clear();
		producerLicense = null;
		emailNotification = false;
	}

	public boolean getEmailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}	
}
