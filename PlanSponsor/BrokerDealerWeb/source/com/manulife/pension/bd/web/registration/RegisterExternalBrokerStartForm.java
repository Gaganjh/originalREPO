package com.manulife.pension.bd.web.registration;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * This is the form bean for RegisterExternalBrokerStartAction
 * 
 * @author Ilamparithi
 * 
 */
public class RegisterExternalBrokerStartForm extends AutoForm {

    private static final long serialVersionUID = 1L;

    private String userHasContract;

    public String getUserHasContract() {
        return userHasContract;
    }

    public void setUserHasContract(String userHasContract) {
        this.userHasContract = userHasContract;
    }

}
