/*
 * Apr 27, 2007
 * 4:27:36 PM
 */
package com.manulife.pension.service.distribution.log;

/**
 * This lists all the events that we log.
 * 
 * @see DistributionEventLog
 */
public enum DistributionEventEnum {

    SEND_FOR_REVIEW("Send for review – Initiates"),
    SEND_FOR_APPROVAL_FROM_DRAFT("Send for approval – Initiates"),
    SEND_FOR_APPROVAL_FROM_REVIEWED("Send for approval – Initiates"),
    SEND_FOR_APPROVAL_FROM_ACCEPTED("Send for approval – Initiates"),
    SEND_FOR_APPROVAL_VIA_CONRACT_SERVICE_FEATURE_CHANGE("Send for Approval-CSF"),
    APPROVE_FROM_DRAFT("Approve – Initiates"), 
    APPROVE_FROM_REVIEWED("Approve – Initiates"),
    APPROVE_FROM_APPROVAL("Approve – Initiates"),
    DENY_FROM_REVIEWED("Deny"), 
    DENY_FROM_APPROVAL("Deny"), 
    SAVE_FROM_POST_DRAFT("Save-Review+Approve"), 
    READY_FOR_ENTRY("Ready for Entry"),
    DELETE_FROM_REVIEWED("Delete"),
    DELETE_FROM_APPROVAL("Delete"),
    DELETE_FROM_VIEW("Delete"), 
    EXPIRE("Expire"),
    SEND_FOR_ACCEPTANCE("Send for acceptance – Initiates"),
    SAVE_AND_EXIT("Save & exit"),
    LOAN_PACKAGE("Loan package"),
    PRINT_LOAN_DOCUMENT("Print loan document");
    
    
    private String eventName;

    /**
     * Default Constructor.
     * 
     * @param eventName The name of the event.
     */
    private DistributionEventEnum(final String eventName) {
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
