package com.manulife.pension.service.loan.valueobject;

import java.util.HashMap;
import java.util.Map;

import com.manulife.pension.service.loan.util.LoanDataHelper;
import com.manulife.pension.service.loan.util.LoanObjectFactory;
import com.manulife.pension.util.BusinessCalendar;

/**
 * A helper class to retrieve support data for the during of a loan request.
 * 
 * NOTE: Do not store this object for more than the duration of a loan request.
 */
public class EjbLoanSupportDataRetriever implements
		LoanSupportDataRetriever {

	private static final long serialVersionUID = 1L;

	private LoanDataHelper loanDataHelper = LoanObjectFactory.getInstance()
			.getLoanDataHelper();

	private Map<Integer, LoanPlanData> loanPlanDataMap = new HashMap<Integer, LoanPlanData>();

	private Map<Integer, LoanSettings> loanSettingsMap = new HashMap<Integer, LoanSettings>();

	private Map<Integer, LoanParticipantData> loanParticipantDataMap = new HashMap<Integer, LoanParticipantData>();

	public LoanParticipantData getLoanParticipantData(Integer contractId,
			Integer participantProfileId) {
		LoanParticipantData loanParticipantData = loanParticipantDataMap
				.get(participantProfileId);
		if (loanParticipantData == null) {
			loanParticipantData = loanDataHelper.getLoanParticipantData(
					contractId, participantProfileId);
			loanParticipantDataMap.put(participantProfileId,
					loanParticipantData);
		}
		return loanParticipantData;
	}

	public LoanPlanData getLoanPlanData(Integer contractId) {
		LoanPlanData loanPlanData = loanPlanDataMap.get(contractId);
		if (loanPlanData == null) {
			loanPlanData = loanDataHelper.getLoanPlanData(contractId);
			loanPlanDataMap.put(contractId, loanPlanData);
		}
		return loanPlanData;
	}

	public LoanSettings getLoanSettings(Integer contractId) {
		LoanSettings loanSettings = loanSettingsMap.get(contractId);
		if (loanSettings == null) {
			loanSettings = loanDataHelper.getLoanSettings(contractId);
			loanSettingsMap.put(contractId, loanSettings);
		}
		return loanSettings;
	}
}
