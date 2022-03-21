package com.manulife.pension.ps.web.onlineloans.displayrules;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.LoanContentConstants;
import com.manulife.pension.ps.web.onlineloans.LoanDisplayHelper;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanActivityRecord;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.util.JdbcHelper;

public class EditDisplayRules extends AbstractDisplayRules {

	public EditDisplayRules(UserProfile userProfile,
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
		return true;
	}

	@Override
	public boolean isDisplayExpandCollapseButton() {
		return true;
	}

	/**
	 * Determines if the "Loan details" section should be expanded or not.
	 * 
	 * @return
	 */
	@Override
	public boolean isExpandLoanDetailsSection() {
	    return true;
	}

	/**
	 * Determines if the calculate maximum loan amount section should be
	 * expanded or not.
	 * 
	 * @return
	 */
	@Override
	public boolean isExpandCalculateMaximumLoanAmountSection() {
		String[] fields = new String[] {
				LoanField.MAXIMUM_LOAN_BALANCE_IN_LAST_12_MONTHS.getFieldName(),
				LoanField.MONEY_TYPE_VESTING_PERCENTAGE_PREFIX.getFieldName(),
				LoanField.OUTSTANDING_LOANS_COUNT.getFieldName(),
				LoanField.CURRENT_OUTSTANDING_LOAN_BALANCE.getFieldName(), };
		if (this.isLoanPendingApprovalStatus() && !isAnyFieldInError(fields)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Determines if the loan calculation section should be expanded or not.
	 * 
	 * @return
	 */
	@Override
	public boolean isExpandLoanCalculationsSection() {
		String[] fields = new String[] { LoanField.LOAN_AMOUNT.getFieldName(),
				LoanField.PAYMENT_FREQUENCY.getFieldName(),
				LoanField.AMORTIZATION_MONTHS.getFieldName(),
				LoanField.INTEREST_RATE.getFieldName() };
		if (this.isLoanPendingApprovalStatus() && !isAnyFieldInError(fields)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Determines if the payment information section should be expanded or not.
	 * 
	 * @return
	 */
	@Override
	public boolean isExpandPaymentInformationSection() {
		String[] fields = new String[] {
				LoanField.ABA_ROUTING_NUMBER.getFieldName(),
				LoanField.ADDRESS_LINE1.getFieldName(),
				LoanField.ADDRESS_LINE2.getFieldName(),
				LoanField.CITY.getFieldName(), LoanField.STATE.getFieldName(),
				LoanField.ZIP_CODE.getFieldName(),
				LoanField.COUNTRY.getFieldName(),
				LoanField.ACCOUNT_TYPE.getFieldName(),
				LoanField.ACCOUNT_NUMBER.getFieldName(),
				LoanField.BANK_NAME.getFieldName() };
		if (!isAnyFieldInError(fields)
				&& (isLoanNewStatus()
						|| isLoanDraftStatus()
						|| (isLoanPendingReviewStatus() && (!isBundledContract() 
								|| !hasContractTPASigningAuthorityPermission())) || (isLoanPendingApprovalStatus()
						&& isBundledContract() && hasContractTPASigningAuthorityPermission()))) {
			return false;
		}
		return true;
	}

	/**
	 * Determines if the "Loan declarations" section should be expanded or not.
	 * 
	 * @return
	 */
	@Override
	public boolean isExpandDeclarationsSection() {
		String[] fields = new String[] {
				LoanField.TRUTH_IN_LENDING_NOTICE.getFieldName(),
				LoanField.PROMISSORY_NOTE.getFieldName(),
				LoanField.AT_RISK_TRANSACTION.getFieldName(), };
		if (isAnyFieldInError(fields)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isExpandNotesSection() {
		String[] fields = new String[] {
				LoanField.CURRENT_ADMINISTRATOR_NOTE.getFieldName(),
				LoanField.CURRENT_PARTICIPANT_NOTE.getFieldName() };
		if (isAnyFieldInError(fields)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowLegallyMarriedAsEditable() {
		if (!isParticipantInitiated() &&
				(isLoanNewStatus() || 
				isLoanDraftStatus() || 
				isLoanPendingReviewStatus() || 
				(isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission())))) {
			return true;
		}
		return false;
	}

	/*
	 * Loan Details
	 */
	@Override
	public boolean isShowLoanTypeAsEditable() {
		/*
		 * Returns true if loan is not Participant initiated and in one of the
		 * following statuses: new, draft, pending review
		 */
		if (!isParticipantInitiated()
				&& (isLoanDraftStatus() || isLoanPendingReviewStatus())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowLoanReasonAsEditable() {
		/*
		 * Returns true if loan is External user initiated and in one of the
		 * following statuses: new, draft, pending review
		 */
		if (!isParticipantInitiated()
				&& (isLoanDraftStatus() || isLoanPendingReviewStatus())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowExpirationDateAsEditable() {
		if (isLoanPendingReviewStatus() || 
				(isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission()))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowPayrollDateAsEditable() {
		return true;
	}

	@Override
	public boolean isShowTpaLoanIssueFeeAsEditable() {
		if (isLoanPendingReviewStatus() || 
				(!isParticipantInitiated() && (isLoanNewStatus() || 
						isLoanDraftStatus() || 
						(isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission()))))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isDisplayDefaultProvisionExplanation() {
		return true;
	}

	@Override
	public boolean isDisplayLoanCalculationEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isLoanCalculationEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

    @Override
    public boolean isLoanAmountEditable() {
        if (isLoanDraftStatus() 
                || (isLoanPendingReviewStatus() && !isParticipantInitiated())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLoanAmountDisplayOnlyRecalculated() {
        if (isLoanPendingReviewStatus() && isParticipantInitiated()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isLoanAmountDisplayOnly() {
        if (!isLoanDraftStatus() && !isLoanPendingReviewStatus()) {
            return true;
        }
        return false;
    }

	@Override
	protected boolean isShowActivityHistory() {
		return true;
	}

	@Override
	public boolean isShowMaximumAmortizationPeriodAsEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowMoneyTypeVestingPercentageAsEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowMaxBalanceLast12MonthsAsEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowOutstandingLoansCountAsEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowCurrentOutstandingBalanceAsEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowDefaultProvisionAsEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isMoneyTypeExcludeIndicatorEditable() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return loanPlanData.isNoMoneyTypeAllowedForLoan();
		}
		return false;
	}

	@Override
	public boolean isShowAddressDataAsEditable() {
		if (!isParticipantInitiated() &&
				(isLoanNewStatus() || 
				isLoanDraftStatus() || 
				isLoanPendingReviewStatus() || 
				(isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission())))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowBankInformationAsEditable() {
		if (!isParticipantInitiated() &&
				(isLoanNewStatus() || 
						isLoanDraftStatus() || 
						isLoanPendingReviewStatus() || 
						(isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission())))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isShowTruthInLendingNoticeAsDisabled() {
		if (!isParticipantInitiated() &&
				(!isBundledContract() ||
				(isBundledContract() && hasContractTPASigningAuthorityPermission() && hasReviewLoansPermission()) ||
				(isBundledContract() && !hasContractTPASigningAuthorityPermission() && hasSigningAuthorityPermission()))) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isShowApplyIrs10KDollarRuleAsDisabled() {
		if (isLoanDraftStatus() || isLoanPendingReviewStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isShowPromissoryNoteAsDisabled() {
		if (!isParticipantInitiated() &&
				(!isBundledContract() ||
				(isBundledContract() && hasContractTPASigningAuthorityPermission() && hasReviewLoansPermission()) ||
				(isBundledContract() && !hasContractTPASigningAuthorityPermission() && hasSigningAuthorityPermission()))) {
			return false;
		}
		return true;
	}

	/*
	 * Buttons
	 */
	@Override
	public boolean isDisplayLoanPackageLink() {
		if (hasErrorCode(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.LRK01_IS_OFF)) {
			return false;
		}
		if (isLoanPendingApprovalStatus()) {
			return false;
		}
		if (!hasReviewLoansPermission()) {
			return false;
		}
		if (isClientUser()) {
			return false;
		}
		if (isParticipantInitiated()) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE)) {
			return false;
		}
		if (!loanSettings.isAllowLoanPackageGeneration()) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST)) {
			return false;
		}
        if (hasErrorCode(LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE)) {
            return false;
        }
		return true;
	}

	@Override
	public boolean isDisplayLoanPackageButton() {
		return false;
	}

	@Override
	public boolean isDisplaySendForReviewButton() {
		if (hasErrorCode(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.LRK01_IS_OFF)) {
			return false;
		}
		if (isLoanPendingReviewStatus()) {
			return false;
		}
		if (isLoanPendingApprovalStatus()) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST)) {
			return false;
		}
        if (hasErrorCode(LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE)) {
            return false;
        }
		return true;
	}

	@Override
	public boolean isDisplaySendForAcceptanceButton() throws SystemException {
		if (loan.getSubmissionId() == null) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.LRK01_IS_OFF)) {
			return false;
		}
		if (!isParticipantInitiated()) {
			return false;
		}
		if (isLoanPendingApprovalStatus()) {
			return false;
		}
		if (!hasReviewLoansPermission()) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST)) {
			return false;
		}
        if (hasErrorCode(LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE)) {
            return false;
        }
		if (loan.isBundledContract()
				&& loan.isSigningAuthorityForContractTpaFirm()
				&& hasAlreadySentForAcceptance()) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isDisplaySendForApprovalButton() {
		if (hasErrorCode(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.LRK01_IS_OFF)) {
			return false;
		}
		if (isParticipantInitiated()) {
			return false;
		}
		if (isLoanPendingApprovalStatus()) {
			return false;
		}
		if (!hasReviewLoansPermission()) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST)) {
			return false;
		}
        if (hasErrorCode(LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE)) {
            return false;
        }
		return true;
	}

	@Override
	public boolean isDisplayApproveButton() {
		/*
		 * Cannot approve a loan request that is not yet saved.
		 */
		if (loan.getSubmissionId() == null) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.ALLOW_ONLINE_LOANS_IS_OFF)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.LRK01_IS_OFF)) {
			return false;
		}
		if (isLoanDraftStatus() || isLoanNewStatus()) {
			return false;
		}
		if (isLoanPendingReviewStatus() && isParticipantInitiated()) {
			return false;
		}
		if (!loanSettings.isInitiatorCanApproveLoan()
				&& loan.getCreatedId().intValue() == userProfile.getPrincipal()
						.getProfileId()) {
			return false;
		}
		if (!hasSigningAuthorityPermission()) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_STATUS_IS_NOT_ACTIVE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_POSITIVE_PBA_BALANCE)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_INSTALLMENT_WITHDRAWAL)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_CURRENT_ACCOUNT_BALANCE_IS_ZERO)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_NUMBER_OF_LOANS_EXCEEDED)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST)) {
			return false;
		}
		if (hasErrorCode(LoanErrorCode.REQUESTED_AMOUNT_EXCEEDS_AVAILABLE_AMOUNT)) {
			return false;
		}
        if (hasErrorCode(LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE)) {
            return false;
        }
		return true;
	}

	@Override
	public boolean isDisplayDenyButton() {
		/*
		 * Cannot deny a loan request that is not yet saved.
		 */
		if (loan.getSubmissionId() == null) {
			return false;
		}
		if (LoanStateEnum.DRAFT.getStatusCode().equals(loan.getStatus())) {
			return false;
		}
		if (LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(
				loan.getStatus())
				&& loan.isBundledContract()
				&& loan.isSigningAuthorityForContractTpaFirm()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayDeleteButton() {
		/*
		 * Cannot delete a loan request that is not yet saved.
		 */
		if (loan.getSubmissionId() == null) {
			return false;
		}
		
		if (isLoanNewStatus()) {
			return false;
		}
		/*
		 * If the login user is not the same as the user who created this
		 * request, hide the delete button.
		 */
		if (loan.getCreatedId().intValue() != (int) userProfile.getPrincipal()
				.getProfileId()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayExitEditModeButton() {
		return true;
	}

    @Override
    public boolean isDisplayExitViewModeButton() {
        return false;
    }
    
	@Override
	public boolean isDisplaySaveAndExitButton() {
		if (hasErrorCode(LoanErrorCode.PARTICIPANT_HAS_PENDING_LOAN_REQUEST)
		        || hasErrorCode(LoanErrorCode.MONEY_TYPE_IS_NO_LONGER_A_CONTRACT_MONEY_TYPE)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplaySubmissionNumber() {
		if (isLoanDraftStatus()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplaySubmissionStatus() {
		return false;
	}

	@Override
	public boolean isDisplaySubmissionProcessingDates() {
		return false;
	}

	@Override
	public boolean isDisplayGiflMsgExternalUserInitiated() {
		return false;
	}

	@Override
	public boolean isDisplayGiflMsgParticipantInitiated() {
		return false;
	}

	@Override
	public boolean isDisplayNotesViewSection() {
		if (isLoanDraftStatus()) {
			return false;
		}
		if (loan.getPreviousParticipantNotes().isEmpty()
				&& loan.getPreviousAdministratorNotes().isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean isDisplayEmployeeSnapshotLink() {
		return true;
	}

	@Override
	public boolean isMaskSsn() {
		if (isLoanPendingReviewStatus() || isLoanPendingApprovalStatus()) {
			return true;
		}
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
    public boolean isDisplaySpousalConsentText() {
        return true;
    }

	@Override
	public boolean isShowPaymentMethodActivityHistoryIcon() {
		if (!isShowActivityHistory() || isParticipantInitiated()) {
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
		if (!isShowActivityHistory() || isParticipantInitiated()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.ADDRESS_LINE1
				.getActivityDetailItemNo(), loanParticipantData
				.getAddressLine1());
	}

	@Override
	public boolean isShowAddressLine2ActivityHistoryIcon() {
		if (!isShowActivityHistory() || isParticipantInitiated()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.ADDRESS_LINE2
				.getActivityDetailItemNo(), loanParticipantData
				.getAddressLine2());
	}

	@Override
	public boolean isShowCityActivityHistoryIcon() {
		if (!isShowActivityHistory() || isParticipantInitiated()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.CITY
				.getActivityDetailItemNo(), loanParticipantData.getCity());
	}

	@Override
	public boolean isShowStateActivityHistoryIcon() {
		if (!isShowActivityHistory() || isParticipantInitiated()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.STATE
				.getActivityDetailItemNo(), loanParticipantData.getStateCode());
	}

	@Override
	public boolean isShowCountryActivityHistoryIcon() {
		if (!isShowActivityHistory() || isParticipantInitiated()) {
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
		if (!isShowActivityHistory() || isParticipantInitiated()) {
			return false;
		}
		return isShowAddressInfoActivityHistoryIcon(LoanField.ZIP_CODE
				.getActivityDetailItemNo(), loanParticipantData.getZipCode());
	}

	@Override
	public boolean isShowAbaRoutingNumberActivityHistoryIcon() {
		if (!isShowActivityHistory() || isParticipantInitiated()) {
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
		if (!isShowActivityHistory() || isParticipantInitiated()) {
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
		if (!isShowActivityHistory() || isParticipantInitiated()) {
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
		if (!isShowActivityHistory() || isParticipantInitiated()) {
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
		return false;
	}

	@Override
	public boolean isDisplayApproverAgreedToText() {
		return false;
	}

	@Override
	public boolean isShowDeclarationsSection() {
		return LoanDisplayHelper.isShowDeclarationsSection(loan, userRoleWithPermissions);
	}

	@Override
	public boolean isDisplayPrintLoanDocumentsButton() {
		if (isParticipantInitiated()) {
			return false;
		}
		if (isBundledContract() && hasContractTPASigningAuthorityPermission() && hasReviewLoansPermission()) {
			return false;
		}
		return true;
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
	public boolean isDisplayNotesEditSection() {
		return true;
	}

	@Override
	public boolean isDisplayButtonFooterText() {
		return true;
	}

	@Override
	public int getButtonsFooterCmaKey() {
		return LoanContentConstants.BUTTON_FOOTER_EDIT_MODE;
	}

	@Override
	public boolean isDisplayPageContentNotFinalDisclaimer() {
		return false;
	}

	@Override
	public boolean isShowAtRiskTransactionCheckBoxAsDisabled() {
		return false;
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
		return true;
	}

	public boolean isDisplayAtRiskTransactionCheckbox() {
	    if (JdbcHelper.INDICATOR_YES.equals(loan.getAtRiskInd())) {
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
		if(isLoanPendingReviewStatus() && 
				!isParticipantInitiated() && 
				isBundledContract() && 
				hasContractTPASigningAuthorityPermission()){
			return true;
		}
		return false;
	}

}
