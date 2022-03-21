/*
 * WithdrawalStateEnum.java,v 1.2 2006/10/10 19:42:57 Paul_Glenn Exp
 * WithdrawalStateEnum.java,v
 * Revision 1.2  2006/10/10 19:42:57  Paul_Glenn
 * Update withdrawal states.
 *
 */
package com.manulife.pension.service.withdrawal.domain;

/**
 * WithdrawalStateEnum has enumerated values of the available withdrawal states.
 * 
 * @author Paul_Glenn
 * @author glennpa
 * @version 1.2 2006/10/10 19:42:57
 */
public enum WithdrawalStateEnum {

    // The valid withdrawal states.
    DRAFT("14", DraftState.class), APPROVED("W1", ApprovedState.class), DELETED("W2",
            DeletedState.class), DENIED("W3", DeniedState.class),
    EXPIRED("W4", ExpiredState.class), PENDING_REVIEW("W6", PendingReviewState.class),
    PENDING_APPROVAL("W5", PendingApprovalState.class), READY_FOR_ENTRY("W7",
            ReadyForEntryState.class);

    /**
     * This holds the data field value for this state.
     */
    private String statusCode;

    /**
     * This holds the class that implements the {@link WithdrawalState} interface for this state.
     */
    private Class stateClass;

    /**
     * Default Constructor.
     * 
     * @param statusCode The data field value for this state.
     * @param stateClass The implementation of the {@link WithdrawalState} for this state.
     */
    private WithdrawalStateEnum(final String statusCode, final Class stateClass) {
        this.statusCode = statusCode;
        this.stateClass = stateClass;
    }

    /**
     * @return the statusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * @return the stateClass
     */
    public Class getStateClass() {
        return stateClass;
    }

}
