package com.manulife.pension.service.withdrawal.valueobject;

import java.sql.Timestamp;

import com.manulife.pension.common.BaseSerializableCloneableObject;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.distribution.valueobject.ActivitySummaryStatusCode;

/**
 * @author Dennis plain old VO for activitysummary table
 * 
 */
public class WithdrawalActivitySummary extends BaseSerializableCloneableObject implements
        ActivitySummary {

    /**
     * Default Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    public static final String ACTION_CODE_SENT_FOR_REVIEW = ActivitySummaryStatusCode.SENT_FOR_REVIEW
            .getStatusCode();

    public static final String ACTION_CODE_SENT_FOR_APPROVAL = ActivitySummaryStatusCode.SENT_FOR_APPROVAL
            .getStatusCode();

    public static final String ACTION_CODE_APPROVED = ActivitySummaryStatusCode.APPROVED
            .getStatusCode();

    public static final String ACTION_CODE_EXPIRED = ActivitySummaryStatusCode.EXPIRED
            .getStatusCode();

    public static final String ACTION_CODE_DELETED = ActivitySummaryStatusCode.DELETED
            .getStatusCode();

    public static final String ACTION_CODE_DENIED = ActivitySummaryStatusCode.DENIED
            .getStatusCode();

    public static final String ACTION_CODE_SYSTEM_OF_RECORD = "Y";

    public static final String ACTION_CODE_SAVED = "S";

    public static final String ACTION_ORIGINAL_VALUE = "O";

    private Integer submissionId;

    private Integer createdById;

    private String actionCode;

    private String actionName;

    private Timestamp actionTimestamp;

    /**
     * This field is used by the UI. Use createdById if you want the person who created the record
     */
    private String userName;
    
    /**
     * This field is used by the UI for internal users. 
     */
    private String internalUserName;

    /**
     * @deprecated This should be replaced by getCreated()
     * @return the action timestamp
     */
    public final Timestamp getActionTimestamp() {
        return actionTimestamp;
    }

    /**
     * @deprecated This should be replaced by setCreated()
     * @param actionTimestamp the action timestamp
     */
    public final void setActionTimestamp(final Timestamp actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    /**
     * @deprecated This should be replaced by getStatusCode()
     * @return the action code
     */
    public final String getActionCode() {
        return actionCode != null ? actionCode.trim() : null;
    }

    /**
     * @deprecated This should be replaced by setStatusCode()
     * @param actionCode the action code
     */
    public final void setActionCode(final String actionCode) {
        this.actionCode = actionCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.manulife.pension.service.withdrawal.valueobject.ActivitySummary#getCreatedById()
     */
    public final Integer getCreatedById() {
        return createdById;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.manulife.pension.service.withdrawal.valueobject.ActivitySummary#setCreatedById(java.lang
     * .Integer)
     */
    public final void setCreatedById(final Integer createdById) {
        this.createdById = createdById;
    }

    /**
     * @param string the user name
     */
    public void setUserName(final String string) {
        this.userName = string;

    }

    /**
     * @return the user name
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * @return the action name
     */
    public final String getActionName() {
        return actionName;
    }

    /**
     * @param actionName the action name
     */
    public final void setActionName(final String actionName) {
        this.actionName = actionName;
    }

    public Timestamp getCreated() {
        return getActionTimestamp();
    }

    public String getStatusCode() {
        return getActionCode();
    }

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setCreated(Timestamp created) {
        setActionTimestamp(created);
    }

    public void setStatusCode(String statusCode) {
        setActionCode(statusCode);
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

	public String getInternalUserName() {
		return internalUserName;
	}

	public void setInternalUserName(String internalUserName) {
		this.internalUserName = internalUserName;
	}

    
}
