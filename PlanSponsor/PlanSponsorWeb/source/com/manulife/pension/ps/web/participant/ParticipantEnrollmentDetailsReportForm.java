package com.manulife.pension.ps.web.participant;

import com.manulife.pension.ps.web.report.ReportForm;

public class ParticipantEnrollmentDetailsReportForm extends ReportForm {

    private static final long serialVersionUID = -5598242159312014406L;
    private String profileId=null;
    private boolean specialSortCategoryInd;
    
	/**
	 * Constructor for ParticipantEnrollmentSummaryReportForm
	 */
	public ParticipantEnrollmentDetailsReportForm() {
		super();
	}

	/**
	 * Gets the profile id
	 * @return Returns a String
	 */
	public String getProfileId() {
		
		return profileId;
	}
    
	/**
	 * Sets the profileId
	 * @param profileId The profileId to set
	 */
	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

    /**
	 * resets the form
	 */
	public void clear() {
		profileId=null;
	}

    public boolean hasSpecialSortCategoryInd() {
        return specialSortCategoryInd;
    }

    public void setSpecialSortCategoryInd(boolean specialSortCategoryInd) {
        this.specialSortCategoryInd = specialSortCategoryInd;
    }
 }
