package com.manulife.pension.ps.web.tpabob;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * 
 * @author Baburaj Ramasamy
 *
 */

public class TPABlockOfBusinessEntryForm extends AutoForm{

	private static final long serialVersionUID = 1L;
	
    // This variable will hold the tpa UserID recieved thru request parameter. This happens when a
    // Internal user navigates to TPA BOB page thru TPA-select page.
    private String tpaUserIDSelected;

	public String getTpaUserIDSelected() {
		return tpaUserIDSelected;
	}

	public void setTpaUserIDSelected(String tpaUserIDSelected) {
		this.tpaUserIDSelected = tpaUserIDSelected;
	}
    
    

}
