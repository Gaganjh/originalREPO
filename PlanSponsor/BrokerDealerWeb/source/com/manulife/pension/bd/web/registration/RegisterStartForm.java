package com.manulife.pension.bd.web.registration;

import com.manulife.pension.ezk.web.ActionForm;

/**
 * This is a form for the start action of the registration.
 * 
 * @author guweigu
 * 
 */
public class RegisterStartForm implements ActionForm {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private boolean disable;

    /**
     * Returns an indicator to enable or disable the link
     * 
     * @return
     */
    public boolean isDisable() {
        return disable;
    }

    /**
     * Sets an indicator to enable or disable the link
     * 
     * @param disable
     */
    public void setDisable(boolean disable) {
        this.disable = disable;
    }

}
