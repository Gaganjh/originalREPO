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

/**
 * Display rules for the print friendly version of the Loan request confirmation
 * page, which differs from the PrintFriendlyDisplayRules by extending from
 * ConfirmationDisplayRules instead of from ViewDisplayRules.
 * 
 * @author matyste
 * 
 */
public class PrintFriendlyConfirmationDisplayRules extends
		ConfirmationDisplayRules {

	public PrintFriendlyConfirmationDisplayRules(UserProfile userProfile,
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
	protected boolean isShowActivityHistory() {
		return false;
	}

	@Override
	public boolean isDisplayPrintLoanDocumentsButton() {
		return false;
	}

	@Override
	public boolean isDisplayDeleteButton() {
		return false;
	}

	@Override
	public boolean isDisplayLoanPackageButton() {
		return false;
	}

	@Override
	public boolean isDisplayExitEditModeButton() {
	    return false;
	}

	@Override
    public boolean isDisplayExitViewModeButton() {
        return false;
    }
	
	@Override
	public boolean isDisplayEmployeeSnapshotLink() {
		return false;
	}

	@Override
	public boolean isDisplayTruthInLendingNoticeViewLink() {
		return false;
	}

	@Override
	public boolean isDisplayPromissoryNoteViewLink() {
		return false;
	}

	@Override
	public boolean isDisplayButtonFooterText() {
		return false;
	}

	@Override
	public boolean isDisplayPageContentNotFinalDisclaimer() {
		if (isLoanPendingReviewStatus() || isLoanPendingAcceptanceStatus()
				|| isLoanPendingApprovalStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDisplayVestingInformationLink() {
		return false;
	}

	@Override
	public boolean isPrintFriendly() {
		return true;
	}
	
	public boolean isDisplayPrintLoanDocReviewButton(){
		return false;
	}

}
