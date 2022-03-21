package com.manulife.pension.platform.web.passcode;

import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * This is the form bean for secured home page action
 * 
 * @author nellopa
 * 
 */
public class PasscodeForm extends BaseForm{
	
	private static final long serialVersionUID = 1L;
	private String passcode;

	    public String getPasscode() {
	        return passcode;
	    }

	    public void setPasscode(String passcode) {
	        this.passcode = passcode;
	    }

		
	    
}
