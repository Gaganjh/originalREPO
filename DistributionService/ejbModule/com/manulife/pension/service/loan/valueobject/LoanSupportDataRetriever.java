package com.manulife.pension.service.loan.valueobject;

public interface LoanSupportDataRetriever {

	LoanParticipantData getLoanParticipantData(Integer contractId, Integer participantProfileId);
	
	LoanPlanData getLoanPlanData(Integer contractId);
	
	LoanSettings getLoanSettings(Integer contractId);
}
