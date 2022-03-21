package com.manulife.pension.service.loan.domain;

public enum LoanStateEnum {

	DRAFT("14",              DraftState.class), 
	PENDING_REVIEW("L1",     PendingReviewState.class), 
	PENDING_ACCEPTANCE("L2", PendingAcceptanceState.class), 
	PENDING_APPROVAL("L3",   PendingApprovalState.class), 
	APPROVED("L4",           ApprovedState.class), 
	COMPLETED("L6",          CompletedState.class), 
	EXPIRED("L7",            ExpiredState.class), 
	DECLINED("L8",           DeclinedState.class), 
	REJECTED("L9",           RejectedState.class), 
	DELETED("LA",            DeletedState.class), 
	LOAN_PACKAGE("LB",       LoanPackageState.class), 
	CANCELLED("99",          CancelledState.class);

	private static final String[] NON_END_STATE_ORDER = new String[] {
			DRAFT.statusCode, 
			PENDING_REVIEW.statusCode,
			PENDING_ACCEPTANCE.statusCode, 
			PENDING_APPROVAL.statusCode,
			APPROVED.statusCode };

	/**
	 * This holds the data field value for this state.
	 */
	private String statusCode;

	/**
	 * This holds the class that implements the {@link LoanState} interface for
	 * this state.
	 */
	private Class stateClass;

	/**
	 * Default Constructor.
	 * 
	 * @param statusCode
	 *            The data field value for this state.
	 * @param stateClass
	 *            The implementation of the {@link LoanState} for this state.
	 */
	private LoanStateEnum(final String statusCode, final Class stateClass) {
		this.statusCode = statusCode;
		this.stateClass = stateClass;
	}

	public static final LoanStateEnum fromStatusCode(String statusCode) {
		for (LoanStateEnum loanStateEnum : LoanStateEnum.values()) {
			if (loanStateEnum.getStatusCode().equals(statusCode)) {
				return loanStateEnum;
			}
		}
		return null;
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

	/**
	 * Checks whether this state occurs before the given state.
	 * 
	 * @param beforeStatusCode
	 * @return
	 */
	public boolean isBefore(LoanStateEnum beforeState) {
		return isBefore(beforeState.getStatusCode());
	}

	/**
	 * Checks whether this state occurs before the given state.
	 * 
	 * @param beforeStatusCode
	 * @return
	 */
	public boolean isBefore(String beforeStatusCode) {
		int nonEndStateIndex = -1;
		/*
		 * First check our own state and see if it is an end state.
		 */
		for (int i = 0; i < NON_END_STATE_ORDER.length; i++) {
			if (NON_END_STATE_ORDER[i].equals(statusCode)) {
				nonEndStateIndex = i;
			}
		}
		if (nonEndStateIndex == -1) {
			/*
			 * This means we are one of the end state. No other state can be
			 * before us. So, we should return false
			 */
			return false;
		}
		for (int i = 0; i < NON_END_STATE_ORDER.length; i++) {
			if (NON_END_STATE_ORDER[i].equals(beforeStatusCode)) {
				if (nonEndStateIndex < i) {
					return true;
				} else {
					return false;
				}
			}
		}
		/*
		 * If the given status is an end state status (and we've already checked
		 * that we are not in the end state), return true.
		 */
		return true;
	}

}
