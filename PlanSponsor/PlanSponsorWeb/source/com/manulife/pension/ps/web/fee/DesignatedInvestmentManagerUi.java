package com.manulife.pension.ps.web.fee;


import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.service.fee.valueobject.DesignatedInvestmentManager;


/**
 * 
 * Value Object to store Designated Investment Manager UI Details
 * 
 * @author Siby Thomas
 *
 */
public class DesignatedInvestmentManagerUi implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final String HYPHON = "-";
	private static final String BLANK_SPACE = " ";
	
	public enum DesignatedInvestmentManagerUIFields {
		  firstName,
		  lastName,
		  company,
		  addressLine1,
		  addressLine2,
		  city,
		  state,
		  zipCode,
		  phone,
		  phoneExt,
		  emailAddress,
		  specialNotes;
	}
	
	private DesignatedInvestmentManager actualDesignatedInvestmentManager = null;
	
	public DesignatedInvestmentManagerUi(DesignatedInvestmentManager display, 
			DesignatedInvestmentManager actual) {
		if(display != null) {
			this.firstName = display.getFirstName();
			this.lastName = display.getLastName();
			this.company = display.getCompany();
			this.addressLine1 = display.getAddressLine1();
			this.addressLine2 = display.getAddressLine2();
			this.city = display.getCity();
			this.state = display.getState();
			if(StringUtils.isNotEmpty(display.getZipCode())) {
				String[]  numbers = display.getZipCode().split(HYPHON);
				if(numbers.length > 0) {
					this.zipCode1 = numbers[0];
				}
				if(numbers.length > 1) {
					this.zipCode2 = numbers[1];
				}
			}
			if(StringUtils.isNotEmpty(display.getPhone())) {
				String[]  numbers = display.getPhone().split(BLANK_SPACE);
				if(numbers.length > 0) {
					this.phonePrefix = numbers[0];
				}
				if(numbers.length > 1) {
					this.phoneAreaCode = numbers[1];
				}
				if(numbers.length > 2) {
					this.phoneNumber = numbers[2];
				}
			}
		    this.phoneExt = display.getPhoneExt();
			this.emailAddress = display.getEmailAddress();
			this.specialNotes = display.getSpecialNotes();
		}
		if(actual != null) {
			this.actualDesignatedInvestmentManager = actual;
		}
	}
	
	public DesignatedInvestmentManager getEditedDesignatedInvestmentManager() {
		DesignatedInvestmentManager designatedInvestmentManager = new DesignatedInvestmentManager();
		designatedInvestmentManager.setFirstName(getFirstName());
    	designatedInvestmentManager.setLastName(getLastName());
    	designatedInvestmentManager.setCompany(getCompany());
    	designatedInvestmentManager.setAddressLine1(getAddressLine1());
    	designatedInvestmentManager.setAddressLine2(getAddressLine2());
    	designatedInvestmentManager.setCity(getCity());
    	designatedInvestmentManager.setState(getState());
    	designatedInvestmentManager.setZipCode(getZipCode());
    	designatedInvestmentManager.setEmailAddress(getEmailAddress());
    	designatedInvestmentManager.setPhone(getPhone());
    	designatedInvestmentManager.setPhoneExt(getPhoneExt());
    	designatedInvestmentManager.setSpecialNotes(getSpecialNotes());
		return designatedInvestmentManager;
	}
	
	public DesignatedInvestmentManager getStoredDesignatedInvestmentManager() {
		return this.actualDesignatedInvestmentManager;
	}
	
	
	// Manager Details
	private String firstName = StringUtils.EMPTY;
	private String lastName = StringUtils.EMPTY;
	private String company = StringUtils.EMPTY;
	private String addressLine1 = StringUtils.EMPTY;
	private String addressLine2 = StringUtils.EMPTY;
	private String city = StringUtils.EMPTY;
	private String state = StringUtils.EMPTY;
	private String zipCode1 = StringUtils.EMPTY;
	private String zipCode2 = StringUtils.EMPTY;
	private String phoneAreaCode = StringUtils.EMPTY;
	private String phoneNumber = StringUtils.EMPTY;
	private String phonePrefix = StringUtils.EMPTY;
	private String phoneExt = StringUtils.EMPTY;
	private String emailAddress = StringUtils.EMPTY;
	private String specialNotes = StringUtils.EMPTY;
	
	private Set<String> changedItems = new TreeSet<String>();
	
	
	public Set<String> getChangedItems() {
		return changedItems;
	}
	public void setChangedItems(
			Set<String> changedItems) {
		this.changedItems = changedItems;
	}
	
	public static Set<String> getDimFields() {
		Set<String> values = new TreeSet<String>();
		for(DesignatedInvestmentManagerUIFields field : DesignatedInvestmentManagerUIFields.values()) {
			values.add(field.name());
		}
		return values;
	}
	
	public boolean getItemChanged(String item) {
		return changedItems.contains(item);
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	public String getZipCode1() {
		return zipCode1;
	}
	public void setZipCode(String zipCode1) {
		this.zipCode1 = zipCode1;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getSpecialNotes() {
		return specialNotes;
	}
	public void setSpecialNotes(String specialNotes) {
		this.specialNotes = specialNotes;
	}
	
	public String getZipCode() {
		StringBuilder zip = new StringBuilder();
		if(StringUtils.isNotEmpty(this.getZipCode1())) {
			zip.append(this.getZipCode1());
			if(StringUtils.isNotEmpty(this.getZipCode2())) {
				zip.append(HYPHON);
				zip.append(this.getZipCode2());
			}
		}
		return zip.toString();
	}
	
    public String getZipCodeValue() {
        return new StringBuffer(StringUtils.defaultString(zipCode1)).append(
                StringUtils.defaultString(zipCode2)).toString();
    }
	
	public String getZipCode2() {
		return this.zipCode2;
	}

	public void setZipCode2(String zipCode2) {
		this.zipCode2 = zipCode2;
	}

	public String getPhoneAreaCode() {
		return phoneAreaCode;
	}

	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhonePrefix() {
		return phonePrefix;
	}

	public void setPhonePrefix(String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	public String getPhoneExt() {
		return phoneExt;
	}
	
	public String getPhone() {
		StringBuilder phone = new StringBuilder();
		if(StringUtils.isNotEmpty(this.getPhonePrefix())) {
			phone.append(this.getPhonePrefix());
			phone.append(BLANK_SPACE);
		}
		if(StringUtils.isNotEmpty(this.getPhoneAreaCode())) {
			phone.append(this.getPhoneAreaCode());
			phone.append(BLANK_SPACE);
		}
		if(StringUtils.isNotEmpty(this.getPhoneNumber())) {
			phone.append(this.getPhoneNumber());
		}
		return phone.toString();
	}

	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}

	public void setZipCode1(String zipCode1) {
		this.zipCode1 = zipCode1;
	}
	
	public boolean getValueEmpty() {
		if(StringUtils.isEmpty(getFirstName())
			&& StringUtils.isEmpty(this.getLastName())
			&& StringUtils.isEmpty(this.getCompany())
			&& StringUtils.isEmpty(this.getAddressLine1())
			&& StringUtils.isEmpty(this.getAddressLine2())
			&& StringUtils.isEmpty(this.getCity())
			&& StringUtils.isEmpty(this.getState())
			&& StringUtils.isEmpty(this.getZipCode())
			&& StringUtils.isEmpty(this.getPhone())
			&& StringUtils.isEmpty(this.getPhoneExt())
			&& StringUtils.isEmpty(this.getEmailAddress())
			&& StringUtils.isEmpty(this.getSpecialNotes())) {
				return true;
			}
		return false;
	}
}
