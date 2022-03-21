/**
 * 
 */
package com.manulife.pension.bd.web.registration;

/**
 * @author rajanch
 *
 */
public class BrokerRequestBody {

	private String lastName;
	private String stateCode;
	private String emailAddress;
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
}
