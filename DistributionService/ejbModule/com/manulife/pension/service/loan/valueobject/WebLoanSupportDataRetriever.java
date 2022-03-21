package com.manulife.pension.service.loan.valueobject;

import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.util.BusinessCalendar;

public class WebLoanSupportDataRetriever implements
		LoanSupportDataRetriever {

	private static final long serialVersionUID = 1L;

	private LoanServiceDelegate loanServiceDelegate;

	public WebLoanSupportDataRetriever() {
		this.loanServiceDelegate = LoanServiceDelegate.getInstance();
	}

	public LoanParticipantData getLoanParticipantData(Integer contractId,
			Integer participantProfileId) {
		try {
			return loanServiceDelegate.getLoanParticipantData(contractId,
					participantProfileId);
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
	}

	public LoanPlanData getLoanPlanData(Integer contractId) {
		try {
			return loanServiceDelegate.getLoanPlanData(contractId);
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
	}

	public LoanSettings getLoanSettings(Integer contractId) {
		try {
			return loanServiceDelegate.getLoanSettings(contractId);
		} catch (SystemException e) {
			throw new RuntimeException(e);
		}
	}
}
