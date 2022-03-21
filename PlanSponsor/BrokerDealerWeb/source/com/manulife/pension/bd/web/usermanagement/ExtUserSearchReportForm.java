package com.manulife.pension.bd.web.usermanagement;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.platform.web.report.BaseReportForm;

public class ExtUserSearchReportForm extends BaseReportForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1234274738810217814L;

	private String lastName="";
	private String firstName="";
	private String contractNum="";
	private String producerCode="";
	private Integer contractNumValue = null;
	private Long producerCodeValue = null;
	private String emailAddress="";
	public static final String FIELD_EMAIL = "emailAddress";
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = StringUtils.trimToEmpty(firstName);
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = StringUtils.trimToEmpty(contractNum);
		if (!StringUtils.isEmpty(this.contractNum)) {
			try {
				contractNumValue = Integer.parseInt(this.contractNum);
			} catch (NumberFormatException e) {
				contractNumValue = null;
			}
		} else {
			contractNumValue = null;

		}
	}

	public String getProducerCode() {
		return producerCode;
	}

	public void setProducerCode(String producerCode) {
		this.producerCode = StringUtils.trimToEmpty(producerCode);
		if (!StringUtils.isEmpty(this.producerCode)) {
			try {
				producerCodeValue = Long.parseLong(this.producerCode);
			} catch (NumberFormatException e) {
				producerCodeValue = null;
			}
		} else {
			producerCodeValue = null;
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = StringUtils.trimToEmpty(lastName);
	}

	public Integer getContractNumValue() {
		return contractNumValue;
	}

	public Long getProducerCodeValue() {
		return producerCodeValue;
	}

	/**
	 * Return whether the form is empty
	 * @return
	 */
	public boolean isEmptyForm() {
		return StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)
				&& StringUtils.isEmpty(contractNum)
				&& StringUtils.isEmpty(producerCode) && StringUtils.isEmpty(emailAddress);
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = StringUtils.trimToEmpty(emailAddress);
	}
}
