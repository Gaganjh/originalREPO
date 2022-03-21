package com.manulife.pension.ps.web.onlineloans.displayrules;

import java.util.Map;

import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.LoanContentConstants;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.util.JdbcHelper;

public class ConfirmationDisplayRules extends ViewDisplayRules {

	public ConfirmationDisplayRules(UserProfile userProfile,
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
	public boolean isDisplayDeleteButton() {
		return false;
	}

    @Override
    public boolean isDisplayExitViewModeButton() {
        return true;
    }

	@Override
	public boolean isDisplayLoanCalculationAcceptedColumn() {
		if (loan.getAcceptedParameter() != null) {
			if (!isLoanPendingAcceptanceStatus()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isDisplayLoanCalculationReviewedColumn() {
		if (loan.getReviewedParameter() != null) {
			if (!isLoanPendingReviewStatus()) {
				return true;
			}
		}
		return false;
	}

    @Override
    public boolean isDisplayGiflMsgExternalUserInitiated() {
        return false;
    }

    @Override
    public boolean isDisplaySubmissionProcessingDates() {
        if (isLoanPendingReviewStatus()
                || isLoanPendingAcceptanceStatus()
                || isLoanPendingApprovalStatus()
                || isLoanLoanPackageStatus()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isDisplayNoteToParticipantPrintContentText() {
        return false;
    }

    @Override
    public boolean isDisplayEmployeeSnapshotLink() {
        return false;
    }

    @Override
    public boolean isDisplayLegallyMarried() {
        if (JdbcHelper.INDICATOR_NO.equals(loanPlanData
                .getSpousalConsentReqdInd())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isDisplayVestingInformationLink() {
        return false;
    }

    @Override
    public String getAccountBalanceLabel() {
        String returnLabel = accountBalanceLabelNoCaret;
        if ((isLoanPendingReviewStatus() 
                || isLoanPendingApprovalStatus()
                || isLoanPendingAcceptanceStatus()
                || isLoanLoanPackageStatus())
                && loanParticipantData.getOutstandingLoansCount() > 0) {
            returnLabel = accountBalanceLabelWithCaret;
        } else if (isLoanApprovedStatus()) {
            returnLabel = accountBalanceLabelWithCaret;
        }    
        return returnLabel;
    }

    @Override
    public boolean isDisplayLoanCalculationEditable() {
        return false;
    }

    @Override
    public boolean isShowDeclarationsSection() {
        if (isLoanLoanPackageStatus()
                || isLoanPendingAcceptanceStatus()
                || isLoanPendingReviewStatus()) {
            return false;
        }
        if (!hasSigningAuthorityPermission()) {
            return false;
        }
        return true;
    }

    @Override
    public String getAccountBalanceFootnoteCmaKey() {
        if (loan.getOutstandingLoansCount() > 0 &&
                (isLoanPendingReviewStatus()
                        || isLoanPendingAcceptanceStatus()
                        || isLoanPendingApprovalStatus()
                        || isLoanLoanPackageStatus())) {
            return String.valueOf(LoanContentConstants.
                    ACCOUNT_BALANCE_FOOTNOTE_EXCLUDES_OUTSTANDING_LOAN_BALANCES);
        }
        if (isLoanApprovedStatus()) {
            return String.valueOf(LoanContentConstants.
                    ACCOUNT_BALANCE_FOOTNOTE_AT_TIME_REQUEST_REVIEWED);
        }
        return null;
    }

    public boolean isDisplayAtRiskTransactionCheckbox() {
        if (JdbcHelper.INDICATOR_YES.equals(loan.getAtRiskInd())) {
            return true;
        } else {
            return false;
        }
    }

}