package com.manulife.pension.ps.web.userguide;

import java.io.Serializable;

import com.manulife.pension.ps.web.controller.PsForm;


public class LandingPageForm extends PsForm implements Serializable{

	private String parentId;

	public LandingPageForm() {
		super();	       			
	}

	public void setParentId(String parentId) { this.parentId = parentId; }
	public String getParentId() { return this.parentId; }			

}

