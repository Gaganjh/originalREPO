/*
 * Apr 27, 2007
 * 4:27:36 PM
 */
package com.manulife.pension.service.withdrawal.log;

/**
 * This lists all the events that we log.
 * 
 * @see WithdrawalEventLog
 * @see WithdrawalLoggingHelper
 * 
 * @author glennpa
 */
public enum WithdrawalEvent {

    SEND_FOR_REVIEW("Send for Review-Initiates"), SEND_FOR_APPROVAL_FROM_DRAFT(
            "Send for Approval-Initiates"), SEND_FOR_APPROVAL_FROM_POST_DRAFT(
            "Send for Approval-Review+Approve"),
    SEND_FOR_APPROVAL_VIA_CONRACT_SERVICE_FEATURE_CHANGE("Send for Approval-CSF"),
    APPROVE_FROM_DRAFT("Approve-Initiates"), APPROVE_FROM_POST_DRAFT("Approve-Review+Approve"),
    DENY("Deny"), SAVE_FROM_POST_DRAFT("Save-Review+Approve"), READY_FOR_ENTRY("Ready for Entry"),
    DELETE_FROM_DRAFT("Delete-Initiates"), DELETE_FROM_POST_DRAFT("Delete-Review+Approve"),
    DELETE_FROM_VIEW("Delete-View"), EXPIRE("Expire");

    private String eventName;

    /**
     * Default Constructor.
     * 
     * @param eventName The name of the event.
     */
    private WithdrawalEvent(final String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return String - The eventName.
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName - The eventName to set.
     */
    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }

}
