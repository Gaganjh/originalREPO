package com.manulife.pension.ps.web.home;

import com.manulife.pension.ps.web.report.ReportForm;

/**
 * FormBean for search TPA
 * 
 * @author gullara
 *
 */
public class SearchTPANameForm extends ReportForm {
	
	private static final long serialVersionUID = 1L;
	
	private String tpaLastName;

	public String getTpaLastName() {
		return tpaLastName;
	}

	public void setTpaLastName(String tpaLastName) {
		this.tpaLastName = tpaLastName;
	}

}
