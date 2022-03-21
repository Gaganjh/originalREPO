package com.manulife.pension.bd.web.myprofile;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is the form bean object for MyProfileLicenseAction
 * 
 * @author Ilamparithi
 * 
 */
public class MyProfileLicenseForm extends AutoForm {

    private static final long serialVersionUID = 1L;

    private Boolean producerLicense;

    private boolean success = false;

    private boolean changed = false;

    /**
     * Returns the producerLicense
     * 
     * @return the producerLicense
     */
    public Boolean getProducerLicense() {
        return producerLicense;
    }

    /**
     * Sets the producerLicense
     * 
     * @param producerLicense the producerLicense to set
     */
    public void setProducerLicense(Boolean producerLicense) {
        this.producerLicense = producerLicense;
    }

    /**
     * Return a flag to indicate whether the save action is success or not
     * 
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Sets the success flag
     * 
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Returns a flag that indicates whether the form is dirty or not
     * 
     * @return the changed
     */
    public boolean isChanged() {
        return changed;
    }

    /**
     * Sets a flag that indicates whether the form is dirty or not
     * 
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

}
