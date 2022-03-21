package com.manulife.pension.ps.web.participant;

import com.manulife.pension.ps.web.controller.PsForm;

public class ParticipantStatementsForm extends PsForm 
{
	private String viewingPreference = "4";
	private String submitButton;
	
	
	public String getViewingPreference() 
	{
		return viewingPreference;
	}
	
	public void setViewingPreference(String viewingPreference) 
	{
		this.viewingPreference = viewingPreference;
	}
	
	public String getSubmitButton() 
	{
		return submitButton;
	}
	
	public void setSubmitButton(String submitButton) 
	{
		this.submitButton = submitButton;
	}
	
	
}

