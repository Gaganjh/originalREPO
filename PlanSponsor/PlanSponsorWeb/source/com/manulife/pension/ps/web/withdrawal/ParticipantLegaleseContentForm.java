package com.manulife.pension.ps.web.withdrawal;

import com.manulife.pension.platform.web.controller.AutoForm;

/**
 * ParticipantLegaleseContentForm is the action form for viewing the participant legalese content page.
 *
 * @author Maria Lee
 * @version
 */
public class ParticipantLegaleseContentForm extends AutoForm {

    private String submissionId = null;
    private String legaleseContentText = null;

    /**
     * Default serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public ParticipantLegaleseContentForm() {
        super();
    }

    public String getLegaleseContentText() {
        return legaleseContentText;
    }

    public void setLegaleseContentText(String legaleseContentText) {
        this.legaleseContentText = legaleseContentText;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

}
