package com.manulife.pension.ps.web.withdrawal;

import java.util.Map;

import com.manulife.pension.platform.web.controller.AutoForm;
import com.manulife.pension.service.withdrawal.valueobject.ActivityHistory;

/**
 * Created on Jan 22, 2007 - snowdde.
 *
 * This class is the form used to show activity history for withdrawals
 */
public class ActivityHistoryForm extends AutoForm {

    /**
     * Default serialVersionUID.
     */
	private static final long serialVersionUID = 1L;
	
	private ActivityHistory activityHistory = null;
	
	private String submissionId = null;

	private Map lookupData = null;

	public final Map getLookupData() {
		return lookupData;
	}

	public final String getSubmissionId() {
		return submissionId;
	}

	public final void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}

	/**
	 * @return returns the activity history value object
	 */
	public final ActivityHistory getActivityHistory() {
		return activityHistory;
	}

	/**
	 * @param activityHistory the activity history value object
	 */
	public final void setActivityHistory(ActivityHistory activityHistory) {
		this.activityHistory = activityHistory;
	}

	public void setLookupData(Map lookupData) {
		this.lookupData = lookupData;
		
	}


 
}