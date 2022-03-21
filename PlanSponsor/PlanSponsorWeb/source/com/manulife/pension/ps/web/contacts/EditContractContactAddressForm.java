package com.manulife.pension.ps.web.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.taglib.util.LabelValueBean;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.ps.web.contacts.util.AddressVO;

/**
 * Edit contract contact addresses action form
 * @author Ranjith Kumar
 *
 */
public class EditContractContactAddressForm extends AutoForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SAVE_ACTION = "save";

	private static final String CANCEL_ACTION = "cancel";

	List<AddressVO> contactAddresses = new ArrayList<AddressVO>();

	private boolean applyToAllAddresses = false;

	private List<LabelValueBean> states;

	private String legalAddressLine1 = StringUtils.EMPTY;

	// first client contact CSF
	private FirstPointOfContact firstPointOfContact = new FirstPointOfContact();

	private String passiveTrustee;

	private Map<String, String> passiveTrusteeOptions = new HashMap<String, String>();

	// CR 16 oldpassiveTrustee added

	private String oldpassiveTrustee;

	/**
	 * Returns 0ldpassiveTrustee
	 * 
	 * @return
	 */
	public String getoldPassiveTrustee() {
		return oldpassiveTrustee;
	}

	/**
	 * sets oldpassiveTrustee
	 * 
	 * @param oldpassiveTrustee
	 */
	public void setoldPassiveTrustee(String oldpassiveTrustee) {
		this.oldpassiveTrustee = oldpassiveTrustee;
	}

	// CR 16 end

	/**
	 * Returns passiveTrustee
	 * @return
	 */
	public String getPassiveTrustee() {
		return passiveTrustee;
	}

	/**
	 * sets passiveTrustee
	 * @param passiveTrustee
	 */
	public void setPassiveTrustee(String passiveTrustee) {
		this.passiveTrustee = passiveTrustee;
	}
	

	/**
	 * returns passiveTrusteeOptions
	 * @return
	 */
	public Map<String, String> getPassiveTrusteeOptions() {
		return passiveTrusteeOptions;
	}

	/**
	 * @param passiveTrusteeOptions
	 */
	public void setPassiveTrusteeOptions(
			Map<String, String> passiveTrusteeOptions) {
		this.passiveTrusteeOptions = passiveTrusteeOptions;
	}

	/**
	 * Returns states
	 * 
	 * @return
	 */
	public List<LabelValueBean> getStates() {
		return states;

	}

	/**
	 * Sets states
	 * 
	 * @param states
	 */
	public void setStates(List<LabelValueBean> states) {
		this.states = states;
	}

	/**
	 * Legal address apply to all other address flag
	 * 
	 * @return
	 */
	public boolean isApplyToAllAddresses() {
		return applyToAllAddresses;
	}

	/**
	 * Sets legal address apply to all addresses flag
	 * 
	 * @param applyToAllAddresses
	 */
	public void setApplyToAllAddresses(boolean applyToAllAddresses) {
		this.applyToAllAddresses = applyToAllAddresses;
	}

	/**
	 * Returns the contract address of the specified inddex
	 * 
	 * @param index
	 * @return
	 */
	public AddressVO getContactAddress(int index) {
		return ((AddressVO) contactAddresses.get(index));

	}

	/**
	 * Sets contract address
	 * 
	 * @param address
	 */
	public void setContactAddress(int index, AddressVO address) {
		this.contactAddresses.add(index, address);

	}

	/**
	 * Setter for the list used to pre-populate the form
	 * 
	 * @param contactAddresses
	 */
	public void setContactAddresses(List<AddressVO> contactAddresses) {
		if (contactAddresses == null) {
			contactAddresses = new ArrayList<AddressVO>();
		}

		this.contactAddresses = contactAddresses;
	}

	/**
	 * Returns all contract addresses
	 * 
	 * @return
	 */
	public List<AddressVO> getContactAddresses() {
		return this.contactAddresses;
	}

	public boolean isSaveAction() {
		return SAVE_ACTION.equals(getAction());
	}

	public boolean isCancelAction() {
		return CANCEL_ACTION.equals(getAction());
	}

	/**
	 * @return the legalAddressLine1
	 */
	public String getLegalAddressLine1() {
		return legalAddressLine1;
	}

	/**
	 * @param legalAddressLine1
	 *            the legalAddressLine1 to set
	 */
	public void setLegalAddressLine1(String legalAddressLine1) {
		this.legalAddressLine1 = legalAddressLine1;
	}

	/*
	 * Reset method overridden from Action Form is added to reset checkbox of
	 * actionform, which is in session will not reset properly.
	 * 
	 * @see
	 * com.manulife.pension.platform.web.controller.AutoForm#reset(org
	 * .apache.struts.action.ActionMapping,
	 * javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void reset( HttpServletRequest arg1) {
		//super.reset(arg0, arg1);
		action = DEFAULT;
		this.firstPointOfContact = new FirstPointOfContact();
		this.applyToAllAddresses = false;
	}

	/**
	 * @return the firstPointOfContact
	 */
	public FirstPointOfContact getFirstPointOfContact() {
		return firstPointOfContact;
	}

	/**
	 * @param firstPointOfContact
	 *            the firstPointOfContact to set
	 */
	public void setFirstPointOfContact(FirstPointOfContact firstPointOfContact) {
		this.firstPointOfContact = firstPointOfContact;
	}
}
