package com.manulife.pension.ps.web.contacts;

import com.manulife.pension.ps.web.controller.PsForm;
import com.manulife.pension.service.contract.valueobject.ContactVO;
/**
 * Contact Management added the new class For JH Contact tab
 * This action form is used in JH Contacts tab page 
 * @author Venkatesh Kasiraj
 *
 */
public class JHContactsForm extends PsForm {
	/**
	 * Default Serial version ID
	 */
	private static final long serialVersionUID = 0L;
	
	/**
	 * Variable holds the contact value object
	 */
	private ContactVO contactVO = null;

	/**
	 * @return the contactVO
	 */
	public ContactVO getContactVO() {
		return contactVO;
	}

	/**
	 * @param contactVO the contactVO to set
	 */
	public void setContactVO(ContactVO contactVO) {
		this.contactVO = contactVO;
	}
	
	
	

}
