package com.manulife.pension.bd.web.ikit;

import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the Action Form for the GetIkitFundInfoAction action class.
 * 
 * @author harlomte
 * 
 */
public class GetIKitFundInfoForm extends BaseForm {

	private static final long serialVersionUID = 1L;

	private String contractNumber;

	private String contractAccessCode;

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getContractAccessCode() {
		return contractAccessCode;
	}

	public void setContractAccessCode(String contractAccessCode) {
		this.contractAccessCode = contractAccessCode;
	}

}
