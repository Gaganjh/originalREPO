package com.manulife.pension.ps.web.onlineloans.displayrules;

import java.util.Map;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;

public class PrintFriendlyForParticipantDisplayRules extends
		PrintFriendlyDisplayRules {

	public PrintFriendlyForParticipantDisplayRules(UserProfile userProfile,
			UserRole userRoleWithPermissions, Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData,
			LoanSettings loanSettings, LoanActivities loanActivities,
			Map<Integer, UserName> userNames, Map<String, String> stateMap,
			Map<String, String> countryMap) {
		super(userProfile, userRoleWithPermissions, loan, loanPlanData,
				loanParticipantData, loanSettings, loanActivities, userNames,
				stateMap, countryMap);
	}

	@Override
	public boolean isDisplayNotesToAdministrators() {
		return false;
	}

	@Override
	public boolean isDisplayNoteToParticipantPrintContentText() {
		return false;
	}

	@Override
	public boolean isDisplayViewNotesSectionFooter() {
		return false;
	}

	@Override
	public boolean isDisplayAtRiskTransactionCheckbox() {
		return false;
	}

}
