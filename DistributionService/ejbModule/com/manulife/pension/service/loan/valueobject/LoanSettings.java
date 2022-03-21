package com.manulife.pension.service.loan.valueobject;

import java.util.Date;

import com.manulife.pension.common.BaseSerializableCloneableObject;

public class LoanSettings extends BaseSerializableCloneableObject {

	private static final long serialVersionUID = 1L;

	private boolean lrk01;

	private boolean gifl;

	private boolean allowOnlineLoans;

	private boolean allowParticipantInitiateLoan;

	private boolean allowLoanPackageGeneration;
	
	private boolean initiatorCanApproveLoan;
	
	
	public boolean isInitiatorCanApproveLoan() {
		return initiatorCanApproveLoan;
	}

	public void setInitiatorCanApproveLoan(boolean initiatorCanApproveLoan) {
		this.initiatorCanApproveLoan = initiatorCanApproveLoan;
	}

	public boolean isLrk01() {
		return lrk01;
	}

	public void setLrk01(boolean lrk01) {
		this.lrk01 = lrk01;
	}

	public boolean isAllowOnlineLoans() {
		return allowOnlineLoans;
	}

	public void setAllowOnlineLoans(boolean allowOnlineLoans) {
		this.allowOnlineLoans = allowOnlineLoans;
	}

	public boolean isAllowParticipantInitiateLoan() {
		return allowParticipantInitiateLoan;
	}

	public void setAllowParticipantInitiateLoan(
			boolean allowParticipantInitiateLoan) {
		this.allowParticipantInitiateLoan = allowParticipantInitiateLoan;
	}

	public boolean isAllowLoanPackageGeneration() {
		return allowLoanPackageGeneration;
	}

	public void setAllowLoanPackageGeneration(boolean allowLoanPackageGeneration) {
		this.allowLoanPackageGeneration = allowLoanPackageGeneration;
	}

	

}
