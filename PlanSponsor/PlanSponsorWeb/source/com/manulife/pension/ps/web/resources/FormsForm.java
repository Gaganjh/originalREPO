/**
 * 
 */
package com.manulife.pension.ps.web.resources;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * @author akarave
 *
 */
public class FormsForm extends AutoForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8289467876588189682L;
	
	private String formType ;
	private String docketFormNo;
	
	public String getDocketFormNo() {
		return docketFormNo;
	}

	public void setDocketFormNo(String docketFormNo) {
		this.docketFormNo = docketFormNo;
	}

	public String getFormType() {
		return formType;
	}
	
	public void setFormType(String formType) {
		this.formType = formType;
	}

}
