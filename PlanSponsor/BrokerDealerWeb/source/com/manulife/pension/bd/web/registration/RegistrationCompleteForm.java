package com.manulife.pension.bd.web.registration;

import com.manulife.pension.ezk.web.ActionForm;

/**
 * The form for registration complete page
 * 
 * @author guweigu
 * 
 */
public class RegistrationCompleteForm implements ActionForm {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private boolean success;

    /**
     * Returns the success flag
     * 
     * @return
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success flag
     * 
     * @param success
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

}
