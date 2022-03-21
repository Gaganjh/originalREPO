package com.manulife.pension.bd.web.bob.participant;

import com.manulife.pension.bd.web.BDConstants;
import com.manulife.pension.platform.web.controller.BaseForm;

/**
 * ParticipantStatementsForm to store the ParticipantStatements page values
 * 
 * @author AAmbrose
 * 
 */
public class ParticipantStatementsForm extends BaseForm {
    private String viewingPreference = BDConstants.DEFAULT_VIEWING_PREFERENCE;

    private String submitButton;

    /**
     * @return String viewingPreference
     */
    public String getViewingPreference() {
        return viewingPreference;
    }

    /**
     * @param String viewingPreference
     */
    public void setViewingPreference(String viewingPreference) {
        this.viewingPreference = viewingPreference;
    }

    /**
     * @return String submitButton
     */
    public String getSubmitButton() {
        return submitButton;
    }

    /**
     * @param String submitButton
     */
    public void setSubmitButton(String submitButton) {
        this.submitButton = submitButton;
    }

}
