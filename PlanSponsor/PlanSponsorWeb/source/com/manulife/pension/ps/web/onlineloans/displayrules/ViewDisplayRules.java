package com.manulife.pension.ps.web.onlineloans.displayrules;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.LoanContentConstants;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanActivityRecord;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.util.JdbcHelper;

public class ViewDisplayRules extends AbstractDisplayRules {

	public ViewDisplayRules(UserProfile userProfile,
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
	public boolean isDisplayYouAreAt() {
		return false;
	}

	@Override
	public boolean isDisplayExpandCollapseButton() {
		return false;
	}

	@Override
	public boolean isDisplayApproveButton() {
		return false;
	}

	@Override
	public boolean isDisplayDeleteButton() {
		/*
		 * If the login user is not the same as the user who created this
		 * request, hide the delete button.
		 */
		if (loan.getCreatedId().intValue() != (int) userProfile.getPrincipal()
				.getProfileId()) {
			return false;
		}
		if (isLoanPendingAcceptanceStatus() || isLoanApprovedStatus()
				|| isLoanCompletedStatus() || isLoanLoanPackageStatus()
				|| isLoanDeniedStatus() || isLoanRejectedStatus()
				|| isLoanCancelledStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayDenyButton() {
		return false;
	}

	@Override
	public boolean isDisplayLoanPackageLink() {
		return false;
	}

	@Override
	public boolean isDisplayLoanPackageButton() {
		if (isLoanPendingReviewStatus() || isLoanPendingAcceptanceStatus()
				|| isLoanPendingApprovalStatus() || isLoanApprovedStatus()
				|| isLoanCompletedStatus() || isLoanDeniedStatus()
				|| isLoanRejectedStatus() || isLoanCancelledStatus()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isDisplaySendForAcceptanceButton() {
		return false;
	}

	@Override
	public boolean isDisplaySendForApprovalButton() {
		return false;
	}

	@Override
	public boolean isDisplaySendForReviewButton() {
		return false;
	}

	@Override
	public boolean isDisplayExitEditModeButton() {
		return false;
	}

    @Override
    public boolean isDisplayExitViewModeButton() {
        return true;
    }

	@Override
	public boolean isDisplaySaveAndExitButton() {
		return false;
	}

	@Override
	public boolean isExpandCalculateMaximumLoanAmountSection() {
		return true;
	}

	@Override
	public boolean isExpandDeclarationsSection() {
		return true;
	}

	@Override
	public boolean isExpandLoanCalculationsSection() {
		return true;
	}

	@Override
	public boolean isExpandLoanDetailsSection() {
		return true;
	}

	@Override
	public boolean isExpandNotesSection() {
		return true;
	}

	@Override
	public boolean isExpandPaymentInformationSection() {
		return true;
	}

	@Override
	public boolean isDisplayLoanCalculationEditable() {
		/*
		 * Displays the "Editable" Loan Calculation column but the fields under
		 * it will not be editable.
		 */
		if (isLoanPendingReviewStatus() || isLoanPendingAcceptanceStatus()) {
			return true;
		}
		return false;
	}

    @Override
    public boolean isLoanCalculationEditable() {
        return false;
    }

    @Override
    public boolean isLoanAmountEditable() {
        return false;
    }

    @Override
    public boolean isLoanAmountDisplayOnlyRecalculated() {
        return false;
    }

    @Override
    public boolean isLoanAmountDisplayOnly() {
        return true;
    }

	@Override
	public boolean isMoneyTypeExcludeIndicatorEditable() {
		return false;
	}

	@Override
	protected boolean isShowActivityHistory() {
		return true;
	}

	@Override
	public boolean isShowDefaultProvisionAsEditable() {
		return false;
	}

	@Override
	public boolean isShowExpirationDateAsEditable() {
		return false;
	}

	@Override
	public boolean isShowPayrollDateAsEditable() {
		return false;
	}

	@Override
	public boolean isShowLegallyMarriedAsEditable() {
		return false;
	}

	@Override
	public boolean isShowLoanReasonAsEditable() {
		return false;
	}

	@Override
	public boolean isShowLoanTypeAsEditable() {
		return false;
	}

	@Override
	public boolean isShowMaximumAmortizationPeriodAsEditable() {
		return false;
	}

	@Override
	public boolean isShowMoneyTypeVestingPercentageAsEditable() {
		return false;
	}

	@Override
	public boolean isShowAddressDataAsEditable() {
		return false;
	}

	@Override
	public boolean isShowBankInformationAsEditable() {
		return false;
	}

	@Override
	public boolean isShowTruthInLendingNoticeAsDisabled() {
		return true;
	}

	@Override
	public boolean isShowApplyIrs10KDollarRuleAsDisabled() {
		return true;
	}

	@Override
	public boolean isShowPromissoryNoteAsDisabled() {
		return true;
	}

	@Override
	public boolean isShowMaxBalanceLast12MonthsAsEditable() {
		return false;
	}

	@Override
	public boolean isShowOutstandingLoansCountAsEditable() {
		return false;
	}

	@Override
	public boolean isShowCurrentOutstandingBalanceAsEditable() {
		return false;
	}

	@Override
	public boolean isShowTpaLoanIssueFeeAsEditable() {
		return false;
	}

	@Override
	public boolean isDisplayDefaultProvisionExplanation() {
		return false;
	}

	@Override
	public boolean isDisplayLoanCalculationReviewedColumn() {
		if (isLoanPendingApprovalStatus() || isLoanApprovedStatus()
				|| isLoanCompletedStatus() || isLoanDeniedStatus()
				|| isLoanLoanPackageStatus() || isLoanRejectedStatus()
				|| isLoanCancelledStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDisplayLoanCalculationOriginalColumn() {
		if (isLoanPendingApprovalStatus() 
		        || isLoanPendingReviewStatus() || isLoanCompletedStatus()
				|| isLoanPendingAcceptanceStatus() || isLoanApprovedStatus()
				|| isLoanDeniedStatus() || isLoanLoanPackageStatus()
				|| isLoanRejectedStatus() || isLoanCancelledStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDisplayLoanCalculationAcceptedColumn() {
		if (!isLoanDeniedStatus()) {
			if (isParticipantInitiated()
					&& (isLoanPendingApprovalStatus() || isLoanApprovedStatus()
							|| isLoanCompletedStatus()
							|| isLoanRejectedStatus() || isLoanCancelledStatus())) {
				return true;
			}
		} else {
			if (loan.getAcceptedParameter() != null) {
				// Pending acceptance loan calculation history exists.
				return true;
			}
		}
		return false;
	}

	@Override
	public String getLoanCalculationReviewedColumnHeader() {
		if (isLoanPendingReviewStatus() || isLoanPendingAcceptanceStatus()) {
			return "Reviewer information";
		} else {
			return super.getLoanCalculationReviewedColumnHeader();
		}
	}

	@Override
	public boolean isDisplaySubmissionNumber() {
		return true;
	}

	@Override
	public boolean isDisplaySubmissionStatus() {
		return true;
	}

	@Override
	public boolean isDisplaySubmissionProcessingDates() {
		if (isLoanPendingReviewStatus() || isLoanPendingAcceptanceStatus()
				|| isLoanPendingApprovalStatus() || isLoanDeniedStatus()
				|| isLoanRejectedStatus() || isLoanLoanPackageStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayGiflMsgExternalUserInitiated() {
		if (loanParticipantData.isGiflFeatureSelected()
				&& !isParticipantInitiated()
				&& (isLoanPendingReviewStatus() || isLoanPendingApprovalStatus())) {
			return true;
		}
		return false;
	}

	public boolean isDisplayGiflMsgParticipantInitiated() {
		if (loanParticipantData.isGiflFeatureSelected()
				&& isParticipantInitiated()
				&& (isLoanPendingReviewStatus() || isLoanPendingApprovalStatus())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDisplayNotesViewSection() {
		if (loan.getPreviousParticipantNotes().isEmpty()
				&& loan.getPreviousAdministratorNotes().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayEmployeeSnapshotLink() {
		if (isLoanApprovedStatus() || isLoanCompletedStatus()
				|| isLoanDeniedStatus() || isLoanRejectedStatus()
				|| isLoanLoanPackageStatus() || isLoanCancelledStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isMaskSsn() {
		return true;
	}

	@Override
	public boolean isDisplayLegallyMarried() {
		if (JdbcHelper.INDICATOR_NO.equals(loanPlanData
				.getSpousalConsentReqdInd())
				&& (isLoanPendingReviewStatus()
						|| isLoanPendingAcceptanceStatus() || isLoanPendingApprovalStatus())) {
			return false;
		}
		if (loan.getLegallyMarriedInd() == null
				&& (isLoanApprovedStatus() || isLoanCompletedStatus()
						|| isLoanDeniedStatus() || isLoanRejectedStatus()
						|| isLoanLoanPackageStatus() || isLoanCancelledStatus())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplaySpousalConsentText() {
        if (isLoanApprovedStatus() || isLoanCompletedStatus()
                || isLoanDeniedStatus() || isLoanRejectedStatus()
                || isLoanLoanPackageStatus() || isLoanCancelledStatus()) {
            return false;
        }
        return true;
	}

	@Override
	public boolean isShowPaymentMethodActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {
			if (activityRecord.getItemNumber().equals(
					LoanField.PAYMENT_METHOD.getActivityDetailItemNo())) {
				// Now we have the ActivityRecord for the field of interest

				return isActivityHistorySavedValueDiffersFromOriginal(activityRecord);
			}
		}

		return false;
	}

	@Override
	public boolean isShowAddressLine1ActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.ADDRESS_LINE1
				.getActivityDetailItemNo(), loanParticipantData
				.getAddressLine1());
	}

	@Override
	public boolean isShowAddressLine2ActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.ADDRESS_LINE2
				.getActivityDetailItemNo(), loanParticipantData
				.getAddressLine2());
	}

	@Override
	public boolean isShowCityActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.CITY
				.getActivityDetailItemNo(), loanParticipantData.getCity());
	}

	@Override
	public boolean isShowStateActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.STATE
				.getActivityDetailItemNo(), loanParticipantData.getStateCode());
	}

	@Override
	public boolean isShowCountryActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}
		// Translate a null or blank country code to 'USA'.
		String countryCode = 
		    StringUtils.isNotEmpty(loanParticipantData.getCountry()) 
		        ? loanParticipantData.getCountry()
		        : GlobalConstants.COUNTRY_CODE_USA;
		return isShowAddressInfoActivityHistoryIcon(LoanField.COUNTRY
				.getActivityDetailItemNo(), countryCode);
	}

	@Override
	public boolean isShowZipCodeActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.ZIP_CODE
				.getActivityDetailItemNo(), loanParticipantData.getZipCode());
	}

	@Override
	public boolean isShowAbaRoutingNumberActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {
			if (activityRecord.getItemNumber().equals(
					LoanField.ABA_ROUTING_NUMBER.getActivityDetailItemNo())) {
				// Now we have the ActivityRecord for the field of interest

				return isActivityHistorySavedValueDiffersFromOriginal(activityRecord);
			}
		}

		return false;
	}

	@Override
	public boolean isShowBankNameActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {
			if (activityRecord.getItemNumber().equals(
					LoanField.BANK_NAME.getActivityDetailItemNo())) {
				// Now we have the ActivityRecord for the field of interest

				return isActivityHistorySavedValueDiffersFromOriginal(activityRecord);
			}
		}

		return false;
	}

	@Override
	public boolean isShowAccountNumberActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {
			if (activityRecord.getItemNumber().equals(
					LoanField.ACCOUNT_NUMBER.getActivityDetailItemNo())) {
				// Now we have the ActivityRecord for the field of interest

				return isActivityHistorySavedValueDiffersFromOriginal(activityRecord);
			}
		}

		return false;
	}

	@Override
	public boolean isShowAccountTypeActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {
			if (activityRecord.getItemNumber().equals(
					LoanField.ACCOUNT_TYPE.getActivityDetailItemNo())) {
				// Now we have the ActivityRecord for the field of interest

				return isActivityHistorySavedValueDiffersFromOriginal(activityRecord);
			}
		}

		return false;
	}

	@Override
	public boolean isDisplayApproverAgreedToLabel() {
		if (isDisplayApproverAgreedToText()
				|| isDisplayAtRiskTransactionCheckbox()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDisplayApproverAgreedToText() {
		if (isLoanPendingReviewStatus() || isLoanPendingApprovalStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isShowDeclarationsSection() {
		if (isLoanDeniedStatus() || isLoanRejectedStatus()
				|| isLoanLoanPackageStatus() || isLoanPendingAcceptanceStatus()) {
			return false;
		}
		if (isParticipantInitiated() && isLoanPendingReviewStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayPrintLoanDocumentsButton() {
		if (isLoanPendingReviewStatus() || isLoanCancelledStatus()
				|| (isLoanPendingApprovalStatus() && isParticipantInitiated())) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayNoteToParticipantPrintContentText() {
		if (isParticipantInitiated()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayViewNotesSectionFooter() {
		return true;
	}

	@Override
	public boolean isDisplayNotesEditSection() {
		return false;
	}

	@Override
	public boolean isDisplayButtonFooterText() {
		return true;
	}

	@Override
	public int getButtonsFooterCmaKey() {
		return LoanContentConstants.BUTTON_FOOTER_VIEW_MODE;
	}

	@Override
	public boolean isDisplayPageContentNotFinalDisclaimer() {
		return false;
	}

	@Override
	public boolean isShowAtRiskTransactionCheckBoxAsDisabled() {
		return true;
	}

	@Override
	public boolean isDisplayNotesToAdministrators() {
		return true;
	}

	@Override
	public boolean isPrintFriendly() {
		return false;
	}

	@Override
	public boolean isEditMode() {
		return false;
	}

	public boolean isDisplayAtRiskTransactionCheckbox() {
	    if (JdbcHelper.INDICATOR_YES.equals(loan.getAtRiskInd())
	            && (isLoanPendingReviewStatus() 
	                    || isLoanPendingApprovalStatus())) {
	        return true;
	    } else {
	        return false;
	    }
	}
	
	@Override
    public boolean isShowMaskedAccountNumber() {
        if (isLoanCompletedStatus() || isLoanLoanPackageStatus()) {
            return true;
        }
        return false;
    }
	
	public boolean isDisplayPrintLoanDocReviewButton(){
		return false;
	}

}