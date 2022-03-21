package com.manulife.pension.ps.web.plandata;

import com.manulife.pension.platform.web.controller.AutoForm;

public class SelectTpaFirmForPlanDataForm extends AutoForm{

	private String selectedTpaFirmId;

	public String getSelectedTpaFirmId() {
		return selectedTpaFirmId;
	}

	public void setSelectedTpaFirmId(String selectedTpaFirmId) {
		this.selectedTpaFirmId = selectedTpaFirmId;
	}
	
}
