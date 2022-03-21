package com.manulife.pension.bd.web.myprofile;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.bd.web.registration.util.Ssn;
import com.manulife.pension.bd.web.registration.util.TaxId;
import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * The action form for adding a new SSN/TAX ID of a broker entity
 * @author guweigu
 *
 */
public class AddBOBForm extends AutoForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String emailAddress;

	private String contractNumber;

	private Integer contractNumValue = null;

	private Ssn ssn = new Ssn();

	private TaxId taxId = new TaxId();

	private boolean changed = false;
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = StringUtils.trimToEmpty(emailAddress);
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = StringUtils.trimToEmpty(contractNumber);
		if (!StringUtils.isEmpty(this.contractNumber)) {
			try {
				contractNumValue = Integer.parseInt(contractNumber);
			} catch (NumberFormatException e) {
				contractNumValue = null;
			}
		}
	}

	public Ssn getSsn() {
		return ssn;
	}

	public void setSsn(Ssn ssn) {
		this.ssn = ssn;
	}

	public TaxId getTaxId() {
		return taxId;
	}

	public void setTaxId(TaxId taxId) {
		this.taxId = taxId;
	}

	public Integer getContractNumValue() {
		return contractNumValue;
	}

	/**
	 * Clear the form. 
	 */
	public void clear() {
		emailAddress = null;
		setContractNumber(null);
		taxId.clear();
		ssn.clear();
		changed = false;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}
