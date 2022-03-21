package com.manulife.pension.ps.web.tpafeeschedule;

import com.manulife.pension.platform.web.controller.AutoForm;

public class SelectTpaFirmForm extends AutoForm{

	private String selectedTpaFirmId;

	public String getSelectedTpaFirmId() {
		return selectedTpaFirmId;
	}

	public void setSelectedTpaFirmId(String selectedTpaFirmId) {
		this.selectedTpaFirmId = selectedTpaFirmId;
	}
	
}
