package com.manulife.pension.ps.web.onlineloans.displayrules;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.manulife.pension.common.GlobalConstants;
import com.manulife.pension.content.view.ContentText;
import com.manulife.pension.delegate.LoanDocumentServiceDelegate;
import com.manulife.pension.delegate.LoanServiceDelegate;
import com.manulife.pension.exception.SystemException;
import com.manulife.pension.ps.web.controller.UserProfile;
import com.manulife.pension.ps.web.onlineloans.LoanContentConstants;
import com.manulife.pension.ps.web.onlineloans.LoanDisplayHelper;
import com.manulife.pension.service.contract.util.ServiceFeatureConstants;
import com.manulife.pension.service.distribution.valueobject.ActivitySummary;
import com.manulife.pension.service.distribution.valueobject.ManagedContent;
import com.manulife.pension.service.loan.LoanDefaults;
import com.manulife.pension.service.loan.LoanErrorCode;
import com.manulife.pension.service.loan.LoanField;
import com.manulife.pension.service.loan.LoanMessage;
import com.manulife.pension.service.loan.domain.LoanConstants;
import com.manulife.pension.service.loan.domain.LoanStateEnum;
import com.manulife.pension.service.loan.valueobject.Loan;
import com.manulife.pension.service.loan.valueobject.LoanActivities;
import com.manulife.pension.service.loan.valueobject.LoanActivityRecord;
import com.manulife.pension.service.loan.valueobject.LoanActivitySummary;
import com.manulife.pension.service.loan.valueobject.LoanAddress;
import com.manulife.pension.service.loan.valueobject.LoanParticipantData;
import com.manulife.pension.service.loan.valueobject.LoanPayee;
import com.manulife.pension.service.loan.valueobject.LoanPlanData;
import com.manulife.pension.service.loan.valueobject.LoanSettings;
import com.manulife.pension.service.security.role.BundledGaCAR;
import com.manulife.pension.service.security.role.UserRole;
import com.manulife.pension.service.security.role.permission.PermissionType;
import com.manulife.pension.service.vesting.EmployeeVestingInformation;
import com.manulife.pension.service.vesting.MoneyTypeVestingPercentage;
import com.manulife.pension.service.withdrawal.valueobject.UserName;
import com.manulife.pension.util.TimestampUtils;

public abstract class AbstractDisplayRules {

	public static final String accountBalanceLabelNoCaret = "Account<br>balance ($)";
	public static final String accountBalanceLabelWithCaret = "Account<br>balance ($)^";

	protected Loan loan;

	protected UserRole userRoleWithPermissions;

	public abstract boolean isDisplayYouAreAt();

	public abstract boolean isDisplayExpandCollapseButton();

	public abstract boolean isDisplaySaveAndExitButton();

	public abstract boolean isDisplayExitEditModeButton();

    public abstract boolean isDisplayExitViewModeButton();

	public abstract boolean isDisplayDeleteButton();

	public abstract boolean isDisplayDenyButton();

	public abstract boolean isDisplayApproveButton();

	public abstract boolean isDisplaySendForApprovalButton();

	public abstract boolean isDisplaySendForAcceptanceButton() throws SystemException;

	public abstract boolean isDisplaySendForReviewButton();

	public abstract boolean isDisplayLoanPackageLink();

	public abstract boolean isDisplayLoanPackageButton();

	public abstract boolean isMoneyTypeExcludeIndicatorEditable();

	public abstract boolean isShowAddressDataAsEditable();

	public abstract boolean isShowBankInformationAsEditable();

	public abstract boolean isShowTruthInLendingNoticeAsDisabled();
	
	public abstract boolean isShowApplyIrs10KDollarRuleAsDisabled();

	public abstract boolean isShowPromissoryNoteAsDisabled();

	public abstract boolean isShowDefaultProvisionAsEditable();

	public abstract boolean isShowMaximumAmortizationPeriodAsEditable();

	public abstract boolean isShowMaxBalanceLast12MonthsAsEditable();

	public abstract boolean isShowMoneyTypeVestingPercentageAsEditable();

	public abstract boolean isShowOutstandingLoansCountAsEditable();

	public abstract boolean isShowCurrentOutstandingBalanceAsEditable();

	protected abstract boolean isShowActivityHistory();

	public abstract boolean isDisplayLoanCalculationEditable();

    public abstract boolean isLoanCalculationEditable();

    public abstract boolean isLoanAmountEditable();
    
	public abstract boolean isLoanAmountDisplayOnlyRecalculated();
    
	public abstract boolean isLoanAmountDisplayOnly();

	public abstract boolean isShowTpaLoanIssueFeeAsEditable();

	public abstract boolean isShowExpirationDateAsEditable();

	public abstract boolean isDisplayDefaultProvisionExplanation();

	public abstract boolean isShowPayrollDateAsEditable();

	public abstract boolean isShowLoanReasonAsEditable();

	public abstract boolean isShowLoanTypeAsEditable();

	public abstract boolean isShowLegallyMarriedAsEditable();

	public abstract boolean isExpandNotesSection();

	public abstract boolean isExpandDeclarationsSection();

	public abstract boolean isExpandPaymentInformationSection();

	public abstract boolean isExpandLoanCalculationsSection();

	public abstract boolean isExpandCalculateMaximumLoanAmountSection();

	public abstract boolean isExpandLoanDetailsSection();

	public abstract boolean isDisplaySubmissionNumber();

	public abstract boolean isDisplaySubmissionStatus();

	public abstract boolean isDisplaySubmissionProcessingDates();

	public abstract boolean isDisplayGiflMsgExternalUserInitiated();

	public abstract boolean isDisplayGiflMsgParticipantInitiated();

	public abstract boolean isDisplayNotesViewSection();

	public abstract boolean isDisplayEmployeeSnapshotLink();

	public abstract boolean isMaskSsn();

	public abstract boolean isDisplayLegallyMarried();

    public abstract boolean isDisplaySpousalConsentText();

    public abstract boolean isShowPaymentMethodActivityHistoryIcon();

	public abstract boolean isShowAddressLine1ActivityHistoryIcon();

	public abstract boolean isShowAddressLine2ActivityHistoryIcon();

	public abstract boolean isShowCityActivityHistoryIcon();

	public abstract boolean isShowStateActivityHistoryIcon();

	public abstract boolean isShowCountryActivityHistoryIcon();

	public abstract boolean isShowZipCodeActivityHistoryIcon();

	public abstract boolean isShowAbaRoutingNumberActivityHistoryIcon();

	public abstract boolean isShowBankNameActivityHistoryIcon();

	public abstract boolean isShowAccountNumberActivityHistoryIcon();

	public abstract boolean isShowAccountTypeActivityHistoryIcon();

	public abstract boolean isDisplayApproverAgreedToLabel();

	public abstract boolean isDisplayApproverAgreedToText();

	public abstract boolean isDisplayPrintLoanDocumentsButton();

	public abstract boolean isDisplayNoteToParticipantPrintContentText();

	public abstract boolean isDisplayViewNotesSectionFooter();

	public abstract boolean isDisplayNotesEditSection();

	public abstract boolean isDisplayButtonFooterText();

	public abstract int getButtonsFooterCmaKey();

	public abstract boolean isDisplayPageContentNotFinalDisclaimer();

    public abstract boolean isDisplayAtRiskTransactionCheckbox();

	public abstract boolean isShowAtRiskTransactionCheckBoxAsDisabled();

	public abstract boolean isDisplayNotesToAdministrators();

	public abstract boolean isPrintFriendly();

	public abstract boolean isEditMode();
	
	public abstract boolean isShowMaskedAccountNumber();
	
	protected LoanPlanData loanPlanData;
	protected UserProfile userProfile;
	protected LoanParticipantData loanParticipantData;
	protected LoanSettings loanSettings;
	protected LoanActivities loanActivities;
	protected Map<Integer, UserName> userNames;
	protected List<LoanMessage> errors;
	protected Map<String, String> stateMap = null;
	protected Map<String, String> countryMap = null;

	private Map<String, Boolean> vestingPercentageActivityHistoryDisplayMap = null;

	public AbstractDisplayRules(UserProfile userProfile,
			UserRole userRoleWithPermissions, Loan loan,
			LoanPlanData loanPlanData, LoanParticipantData loanParticipantData,
			LoanSettings loanSettings, LoanActivities loanActivities,
			Map<Integer, UserName> userNames, Map<String, String> stateMap,
			Map<String, String> countryMap) {
		this.userProfile = userProfile;
		this.userRoleWithPermissions = userRoleWithPermissions;
		this.loan = loan;
		this.loanPlanData = loanPlanData;
		this.loanParticipantData = loanParticipantData;
		this.loanActivities = loanActivities;
		this.loanSettings = loanSettings;
		this.userNames = userNames;
		this.stateMap = stateMap;
		this.countryMap = countryMap;

		/*
		 * Save the errors and the messages together.
		 */
		this.errors = new ArrayList<LoanMessage>(loan.getErrors());
		errors.addAll(loan.getMessages());

	}

	/**
	 * Returns true if any one of the given fields is marked as an error field
	 * in the errors collection.
	 * 
	 * @param fields
	 * @return true if any one of the given fields is marked as an error field
	 *         in the errors collection. false if otherwise.
	 */
	protected boolean isAnyFieldInError(String[] fields) {
		if (errors != null) {
			for (String field : fields) {
				for (LoanMessage error : errors) {
					for (String errorFieldId : error.getFieldNames()) {
						if (errorFieldId.startsWith(field)) {
							// Do a startsWith check since the
							// moneyTypeVestingPercentage
							// fields have the moneyTypeId added to the field
							// name
							// as a suffix.
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	protected boolean hasErrorCode(LoanErrorCode loanErrorCode) {
		if (errors != null) {
			for (LoanMessage error : errors) {
				if (error.getErrorCode().equals(loanErrorCode)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the address state name corresponding to the state code if found
	 * on the state name list, if the country is deemed to be USA. Otherwise the
	 * state code is returned.
	 * 
	 * @return state name or state code
	 */
	public String getStateName() {
		String state = null;
		if (loan.getRecipient() != null
				&& loan.getRecipient().getAddress() != null) {
			LoanAddress address = (LoanAddress) loan.getRecipient()
					.getAddress();
			if (isCountryUSA()) {
				if (stateMap != null) {
					state = stateMap.get(address.getStateCode());
				}
			}
			if (state == null) {
				state = address.getStateCode();
			}
		}
		return state;
	}

	/**
	 * Returns the address country name corresponding to the country code if
	 * found on the country name list. Otherwise the country code is returned.
	 * 
	 * @return country name or country code
	 */
	public String getCountryName() {
		String country = null;
		if (loan.getRecipient() != null
				&& loan.getRecipient().getAddress() != null) {
			LoanAddress address = (LoanAddress) loan.getRecipient()
					.getAddress();
			if (countryMap != null) {
				if (isCountryUSA()) {
					country = countryMap.get(GlobalConstants.COUNTRY_CODE_USA);
				} else {
					country = countryMap.get(address.getCountryCode());
				}
			}
			if (country == null) {
				country = address.getCountryCode();
			}
		}
		return country;
	}

	/**
	 * Indicates if the address country code is USA, or blank, or does not exist
	 * on the country code map. In each of the above cases the country is deemed
	 * to be USA.
	 * 
	 * @return
	 */
	public boolean isCountryUSA() {
		String countryCode = null;
		if (loan.getRecipient() != null
				&& loan.getRecipient().getAddress() != null) {
			countryCode = loan.getRecipient().getAddress().getCountryCode();
		}
		// USA will be shown if country is empty
		if (StringUtils.isBlank(countryCode)) {
			return true;
		}

		if (GlobalConstants.COUNTRY_CODE_USA.equalsIgnoreCase(countryCode)) {
			return true;
		}

		// if it is an invalid country, it becomes USA in UI
		if (!countryMap.containsKey(countryCode)) {
			return true;
		}
		return false;
	}

	public int getYouAreAtCmaId() {
		if (isLoanNewStatus() || isLoanDraftStatus()) {
			return LoanContentConstants.YOU_ARE_AT_TEXT_NEW_OR_DRAFT;
		} else if (isLoanPendingReviewStatus()) {
			if (isParticipantInitiated()) {
				return LoanContentConstants.YOU_ARE_AT_TEXT_PENDING_REVIEW_PRT_INITIATED;
			} else {
				return LoanContentConstants.YOU_ARE_AT_TEXT_PENDING_REVIEW_EXT_USER_INITIATED;
			}
		} else if (isLoanPendingApprovalStatus()) {
			if (isParticipantInitiated()) {
				return LoanContentConstants.YOU_ARE_AT_TEXT_PENDING_APPROVAL_PRT_INITIATED;
			} else {
				return LoanContentConstants.YOU_ARE_AT_TEXT_PENDING_APPROVAL_EXT_USER_INITIATED;
			}
		}
		return 0;
	}

	public boolean isDisplayPaymentInstructionSection() {
		if (isParticipantInitiated()
				&& (isLoanPendingReviewStatus()
						|| isLoanPendingAcceptanceStatus() || isLoanRejectedStatus())) {
			return false;
		}
		return true;
	}

	public boolean isDisplayMiddleInitial() {
		if (StringUtils.isBlank(loanParticipantData.getMiddleInitial())) {
			return false;
		}
		return true;
	}

	public boolean isDisplayVestingInformationLink() {
		if (isLoanApprovedStatus() || isLoanCompletedStatus()
				|| isLoanLoanPackageStatus() || isLoanDeniedStatus()
				|| isLoanRejectedStatus() || isLoanCancelledStatus()) {
			return false;
		}
		if (loanPlanData.isVestingServiceCreditMethodUnspecified()
		        || ServiceFeatureConstants.PROVIDED.equals(
	                    loanPlanData.getVestingServiceFeature())
                || ServiceFeatureConstants.NA.equals(
                        loanPlanData.getVestingServiceFeature())
		        || loan.getEmployeeVestingInformation()
						.getMoneyTypeVestingPercentages().isEmpty()
				|| loanPlanData.isAllMoneyTypesFullyVested()) {
			return false;
		}
		return true;
	}

	public String getParticipantLabelText() {
		return (isParticipantInitiated() ? "To participant if denying (max. 750 characters)"
				: "To participant (max. 750 characters)");
	}

	public boolean isDisplayTpaLoanIssueFee() {
		// If contract does not have a TPA firm, don't show the TPA loan issue
		// fee field.
		if (loanPlanData.getThirdPartyAdminId() != null) {
			return true;
		}
		return false;
	}

	public boolean isTpaLoanFeeHasErrors() {
		return isAnyFieldInError(new String[] { LoanField.TPA_LOAN_ISSUE_FEE
				.getFieldName() });
	}

	public String getLoanCalculationEditableColumnHeader() {
		if (isLoanDraftStatus()) {
			if (isClientUser()) {
				return "Initiated by Plan Sponsor";
			} else if(isTpaUser()) {
				return "Initiated by TPA";
			}else if (userRoleWithPermissions instanceof BundledGaCAR){
				return "Initiated by John Hancock";
			}
		} else if (isLoanPendingReviewStatus()
				|| isLoanPendingAcceptanceStatus()) {
			return "Reviewer information";
		}
		return "Unknown";
	}

	public boolean isDisplayLoanCalculationReviewedColumn() {
		if (isLoanPendingApprovalStatus()) {
			return true;
		}
		return false;
	}

	public boolean isDisplayLoanCalculationOriginalColumn() {
		if (isLoanPendingApprovalStatus() || isLoanPendingReviewStatus()) {
			return true;
		}
		return false;
	}

	public boolean isDisplayLoanCalculationAcceptedColumn() {
		if (isParticipantInitiated() && isLoanPendingApprovalStatus()) {
			return true;
		}
		return false;
	}

	public boolean isDisplayLoanCalculationBlankColumn() {
	    if (isDisplayLoanCalculationAcceptedColumn() 
	            && isDisplayLoanCalculationReviewedColumn()
                && isDisplayLoanCalculationOriginalColumn()) {
            return false;
        }
        return true;
	}

	public String getLoanCalculationAcceptedColumnHeader() {
		return "Accepted by Participant";
	}

	public String getLoanCalculationReviewedColumnHeader() {
		UserName userName = userNames.get(loan.getReviewedParameter()
				.getLastUpdatedById());
        if (userName != null) {
            if (LoanConstants.USER_ROLE_TPA_DISPLAY_NAME.equals(userName.getRole())) {
                return "Reviewed by TPA";
            } else if (LoanConstants.USER_ROLE_PLAN_SPONSOR_DISPLAY_NAME
                    .equals(userName.getRole())) {
                return "Reviewed by Plan Sponsor";
            }else if(LoanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME.equals(userName.getRole())){
            	return "Reviewed by John Hancock";
            } else {
                return "Reviewed by unknown user";
            }
        } else {
            return "Reviewed by unknown user";
        }
	}

	public String getLoanCalculationOriginalColumnHeader() {
		if (isParticipantInitiated()) {
			return "Initiated by Participant";
		} else {
			UserName userName = userNames.get(loan.getOriginalParameter()
					.getLastUpdatedById());
			if (userName != null) {
	            if (LoanConstants.USER_ROLE_TPA_DISPLAY_NAME.equals(userName.getRole())){
	                return "Initiated by TPA";
	            } else if (LoanConstants.USER_ROLE_PLAN_SPONSOR_DISPLAY_NAME.equals(userName.getRole())) {
	                return "Initiated by Plan Sponsor";
	            }else if(LoanConstants.USER_ROLE_BUNDLED_GA_REP_DISPLAY_NAME.equals(userName.getRole())){
	            	return "Initiated by John Hancock";
	            } else {
	                return "Initiated by unknown user";
	            }
			} else {
			    return "Initiated by unknown user";
			}
		}
	}

	public boolean isShowFeeActivityHistoryIcon() {
		if (!isShowActivityHistory()) {
			return false;
		}

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {
			if (activityRecord.getItemNumber().equals(
					LoanField.TPA_LOAN_ISSUE_FEE.getActivityDetailItemNo())) {
				// Now we have the ActivityRecord for the tpa_loan_issue_fee
				// field.

				if (activityRecord.getChangedByTimestamp() != null) {
					// implies an 'S' activity history record exists
					if (isLoanPendingReviewStatus()
							|| isLoanPendingApprovalStatus()
							|| isLoanPendingAcceptanceStatus()) {
		                // Convert values to BigDecimal before doing the compare, and 
		                // consider a value of null to = BigDecimal zero. 
					    BigDecimal systemOfRecordValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
					    if (loanPlanData.getContractLoanSetupFeeAmount() != null
					            && loanPlanData.getContractLoanSetupFeeAmount()
					            .compareTo(GlobalConstants.ZERO_AMOUNT) != 0) {
					        systemOfRecordValue = loanPlanData.getContractLoanSetupFeeAmount();
					    }
					    BigDecimal savedValue = 
					        com.manulife.pension.util.NumberUtils
					            .parseBigDecimal(activityRecord.getSavedValue(),
					                true);
					    if (savedValue == null) {
					        // Zero is assumed
					        savedValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
					    }
					    if (systemOfRecordValue.compareTo(savedValue) != 0) {
							return true;
						}
					}

					// Do the checks that are common across fields.
					if (isShowActivityHistoryIcon(activityRecord, true)) {
						return true;
					}
				}
				return false;
			}
		}

		return false;
	}

	/**
	 * Checks that are common across fields for when the activity history icon
	 * (green arrow) should be displayed.
	 * 
	 * @param activityRecord
	 * @param compareValuesAsBigDecimals If the values should be converted to 
	 *         BigDecimal and then compared, with a null being converted to zero.
	 * @return true if the activity history icon should be displayed.
	 */
	protected boolean isShowActivityHistoryIcon(
			LoanActivityRecord activityRecord, boolean compareValuesAsBigDecimals) {
	    if (activityRecord.getSubmittedByTimestamp() != null) {
			// implies an 'O' activity history record exists
	        if (!compareValuesAsBigDecimals) {
	            if (activityRecord.getSavedValue() != null) {
	                if (!activityRecord.getSavedValue().equals(
	                        activityRecord.getOriginalValue())) {
	                    return true;
	                }
	            } else if (activityRecord.getOriginalValue() != null) {
	                // So the 'O' record value isn't null, but the 'S'
	                // record value is null, so they're not equal.
	                return true;
	            }
	        } else {
	            // Convert values to BigDecimal before doing the compare, and 
	            // consider a value of null to = BigDecimal zero. 
                BigDecimal originalValue = 
                    com.manulife.pension.util.NumberUtils.parseBigDecimal(
                            activityRecord.getOriginalValue(), true);
                if (originalValue == null) {
                    // Zero is assumed
                    originalValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
                }
                BigDecimal savedValue =
                    com.manulife.pension.util.NumberUtils.parseBigDecimal(
                            activityRecord.getSavedValue(), true);
                if (savedValue == null) {
                    // Zero is assumed
                    savedValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
                }
                if (originalValue.compareTo(savedValue) != 0) {
                    return true;
                }
	        }
		}
		/*
		 * The below is a condition for View mode. The status values listed will
		 * only exist when processing in View mode.
		 */
		if (isLoanApprovedStatus() || isLoanCompletedStatus()
				|| isLoanDeniedStatus() || isLoanRejectedStatus()
				|| isLoanLoanPackageStatus() || isLoanCancelledStatus()) {
		    if (activityRecord.getSystemOfRecordTimestamp() != null) {
		        // Implies a Y system of record value exists.
	            if (!compareValuesAsBigDecimals) {
	                if (activityRecord.getSavedValue() != null) { 
	                    if (!activityRecord.getSavedValue().equals(
	                            activityRecord.getSystemOfRecordValue())) {
	                        return true;
	                    }
	                } else if (activityRecord.getSystemOfRecordValue() != null) {
	                    // So the 'Y' record value isn't null, but the 'S'
	                    // record value is null, so they're not equal.
	                    return true;
	                }
	            } else {
	                // Convert values to BigDecimal before doing the compare, and 
	                // consider a value of null to = BigDecimal zero. 

	                BigDecimal systemOfRecordValue = 
	                    com.manulife.pension.util.NumberUtils.parseBigDecimal(
	                            activityRecord.getSystemOfRecordValue(), true);
	                if (systemOfRecordValue == null) {
	                    // Zero is assumed
	                    systemOfRecordValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
	                }
	                BigDecimal savedValue =
	                    com.manulife.pension.util.NumberUtils.parseBigDecimal(
	                            activityRecord.getSavedValue(), true);
	                if (savedValue == null) {
	                    // Zero is assumed
	                    savedValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
	                }
	                if (systemOfRecordValue.compareTo(savedValue) != 0) {
	                    return true;
	                }
	            }
		    }
		}
		return false;
	}

	protected boolean isShowAddressInfoActivityHistoryIcon(Integer itemNumber,
			String currentAddressFieldValue) {
		LoanField loanField = LoanField.getFieldFromItemNumber(itemNumber);
		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {
			if (activityRecord.getItemNumber().equals(
					loanField.getActivityDetailItemNo())) {
				// Now we have the ActivityRecord for the field of interest

				if (activityRecord.getChangedByTimestamp() != null) {
					// implies an 'S' activity history record exists
					if ((isLoanPendingReviewStatus() || isLoanPendingApprovalStatus())
							&& loanParticipantData.getAddressLine1() != null) {
						// loanParticipantData.getAddressLine1() != null implies
						// that
						// a participant address is available in CSDB.
						if (currentAddressFieldValue != null
								&& !currentAddressFieldValue
										.equals(activityRecord.getSavedValue())) {
							return true;
						}
					}

					// Do the checks that are common across fields.
					if (isShowActivityHistoryIcon(activityRecord, false)) {
						return true;
					}
				}
				return false;
			}
		}

		return false;
	}

	protected boolean isActivityHistorySavedValueDiffersFromOriginal(
			LoanActivityRecord activityRecord) {
		if (activityRecord.getChangedByTimestamp() != null) {
			// implies an 'S' activity history record exists
			if (activityRecord.getSubmittedByTimestamp() != null) {
				// implies an 'O' activity history record exists
				if (activityRecord.getSavedValue() != null) {
					if (!activityRecord.getSavedValue().equals(
							activityRecord.getOriginalValue())) {
						return true;
					}
				} else if (activityRecord.getOriginalValue() != null) {
					// So the 'O' record value isn't null, but the 'S'
					// record value is null, so they're not equal.
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Generates Map vestingPercentageActivityHistoryDisplayMap which will
	 * control if the activity history icon (green arrow) gets displayed next to
	 * a vesting percentage associated with a particular money type. The map
	 * entrys are <moneyTypeID, vestingPercentage>.
	 * 
	 */
	private void buildVestingPercentageActivityHistoryDisplayMap() {

		if (!isShowActivityHistory()) {
			// Don't add any map entries, which will cause no activity history
			// icons to appear.
			return;
		}

        BigDecimal originalValue;
        BigDecimal savedValue;

		vestingPercentageActivityHistoryDisplayMap = new HashMap<String, Boolean>();
		EmployeeVestingInformation vestingInfo = loan
				.getEmployeeVestingInformation();

		for (LoanActivityRecord activityRecord : loanActivities
				.getActivityRecords()) {

			if (activityRecord.getSubItemName() != null
					&& activityRecord.getItemNumber().equals(
							LoanField.MONEY_TYPE_VESTING_PERCENTAGE_PREFIX
									.getActivityDetailItemNo())) {
				// We now have an ActivityRecord for a money type vesting
				// percentage field, so lets process it.

				// Indicates whether the activity history icon should be
				// displayed for the current money type.
				Boolean displayActivityHistory = false;
				
                // Convert the activity history values to BigDecimal
                // before comparing them.
		        originalValue = 
		            com.manulife.pension.util.NumberUtils.parseBigDecimal(
		                    activityRecord.getOriginalValue(), true);
		        if (originalValue == null) {
		            // Zero is assumed
		            originalValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
		        }
		        savedValue =
		            com.manulife.pension.util.NumberUtils.parseBigDecimal(
		                    activityRecord.getSavedValue(), true);
		        if (savedValue == null) {
		            // Zero is assumed
		            savedValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
		        }

				String moneyTypeId = activityRecord.getSubItemName();
				if (activityRecord.getSubmittedByTimestamp() == null
				        || savedValue.compareTo(originalValue) != 0) {
				    displayActivityHistory = true;
				}

				if ((isLoanPendingReviewStatus()
						|| isLoanPendingApprovalStatus() || isLoanPendingAcceptanceStatus())
						&& (loan.getEmployeeVestingInformation().getErrors() == null || loan
								.getEmployeeVestingInformation().getErrors()
								.size() == 0)) {
					MoneyTypeVestingPercentage vestingServiceVestingPercentage = (MoneyTypeVestingPercentage) vestingInfo
							.getMoneyTypeVestingPercentages().get(moneyTypeId);
					BigDecimal vestingServiceVestingPercentageValue = vestingServiceVestingPercentage != null ? vestingServiceVestingPercentage
							.getPercentage()
							: null;
					if (vestingServiceVestingPercentageValue != null) {
					    if (vestingServiceVestingPercentageValue
					            .compareTo(savedValue) != 0) {
					        displayActivityHistory = true;
					    }
					}
				}

				/*
				 * The below is a condition for View mode. The status values
				 * listed will only exist in View mode.  If no 'Y' system
				 * of record rows exist in the SDB, then don't display the
				 * activity history icon.
				 */
				if (activityRecord.getSystemOfRecordTimestamp() != null
				        && (isLoanApprovedStatus() || isLoanCompletedStatus()
						|| isLoanDeniedStatus() || isLoanRejectedStatus()
						|| isLoanCancelledStatus() || isLoanLoanPackageStatus())) {
					// Convert the activity history values to BigDecimal
					// before comparing them.
				    BigDecimal systemOfRecordValue =
	                    com.manulife.pension.util.NumberUtils.parseBigDecimal(
	                            activityRecord.getSystemOfRecordValue(), true);
	                if (systemOfRecordValue == null) {
	                    // Zero is assumed
	                    systemOfRecordValue = new BigDecimal(GlobalConstants.INTEGER_ZERO);
	                }

				    if (savedValue.compareTo(systemOfRecordValue) != 0) {
				        displayActivityHistory = true;
				    }
				}

				this.vestingPercentageActivityHistoryDisplayMap.put(
						moneyTypeId, displayActivityHistory);
			}
		}
	}

	public boolean isDisplayTruthInLendingNoticeViewLink() {
		if (!isParticipantInitiated()) {
			return false;
		}
		return true;
	}

	public boolean isDisplayPromissoryNoteViewLink() {
		if (!isParticipantInitiated()) {
			return false;
		}
		return true;
	}

	public boolean isDisplayParticipantDeclarationCheckbox() {
		if (!isParticipantInitiated()) {
			return false;
		}
		return true;
	}

	public String getPayrollDateStyleClass() {
		if ((isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission())) ||
		        (isLoanPendingReviewStatus() && !isParticipantInitiated())) {
			return "mandatory";
		} else {
			return "";
		}
	}
	
	public boolean isPayrollDatePendingStatus() {
		if (isLoanPendingApprovalStatus() && isBundledContract() && hasContractTPASigningAuthorityPermission()) {
			return true;
		} else {
			return false;
		}
	}

	public String getAddressDataStyleClass() {
		if (!isParticipantInitiated() &&
				((isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission())) ||
						(isLoanPendingReviewStatus() && isBundledContract() && hasContractTPASigningAuthorityPermission()))) {
			return "mandatory";
		} else {
			return "";
		}
	}

	public String getBankInformationStyleClass() {
		if (!isParticipantInitiated() &&
				((isLoanPendingApprovalStatus() && (!isBundledContract() || !hasContractTPASigningAuthorityPermission())) ||
						(isLoanPendingReviewStatus() && isBundledContract() && hasContractTPASigningAuthorityPermission()))) {
			return "mandatory";
		} else {
			return "";
		}
	}

	public String getAccountBalanceLabel() {
		String returnLabel = accountBalanceLabelNoCaret;
		if ((isLoanDraftStatus() || isLoanPendingReviewStatus())
				&& loanParticipantData.getOutstandingLoansCount() > 0) {
			returnLabel = accountBalanceLabelWithCaret;
		} else if (isLoanPendingApprovalStatus()
				|| isLoanPendingAcceptanceStatus() || isLoanApprovedStatus()
				|| isLoanCompletedStatus() || isLoanDeniedStatus()
				|| isLoanRejectedStatus() || isLoanLoanPackageStatus()
				|| isLoanCancelledStatus()) {
			returnLabel = accountBalanceLabelWithCaret;
		}
		return returnLabel;
	}

	public boolean hasReviewLoansPermission() {
		if (userRoleWithPermissions.hasPermission(PermissionType.REVIEW_LOANS)) {
			return true;
		}
		return false;
	}

	public boolean hasSigningAuthorityPermission() {
		if (userRoleWithPermissions
				.hasPermission(PermissionType.SIGNING_AUTHORITY)) {
			return true;
		}
		return false;
	}
	
	public boolean hasContractTPASigningAuthorityPermission() {
		if (loan.isSigningAuthorityForContractTpaFirm()) {
			return true;
		}
		return false;
	}
	
	public boolean isBundledContract() {
		return loan.isBundledContract();
	}

	protected boolean isParticipantNotInitiated() {
		return loan.isParticipantNotInitiated();
	}
	
	

	protected boolean isParticipantInitiated() {
		return loan.isParticipantInitiated();
	}

	/**
	 * Is user a PSW (Client) user or a TPA user
	 * 
	 * @return true if user is a PSW (Client) user
	 */
	protected boolean isClientUser() {
		return userRoleWithPermissions.isPlanSponsor();
	}
	
	/**
	 * Is user a PSW (Client) user or a TPA user
	 * 
	 * @return true if user is a PSW (Client) user
	 */
	protected boolean isTpaUser() {
		return userRoleWithPermissions.isTPA();
	}

	public boolean isLoanDraftStatus() {
		if (LoanStateEnum.DRAFT.getStatusCode().equals(loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanNewStatus() {
		if (LoanStateEnum.DRAFT.getStatusCode().equals(loan.getStatus())
				&& loan.getSubmissionId() == null) {
			return true;
		}
		return false;
	}

	protected boolean isLoanPendingReviewStatus() {
		if (LoanStateEnum.PENDING_REVIEW.getStatusCode().equals(
				loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanPendingApprovalStatus() {
		if (LoanStateEnum.PENDING_APPROVAL.getStatusCode().equals(
				loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanPendingAcceptanceStatus() {
		if (LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode().equals(
				loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanApprovedStatus() {
		if (LoanStateEnum.APPROVED.getStatusCode().equals(loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanCompletedStatus() {
		if (LoanStateEnum.COMPLETED.getStatusCode().equals(loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanDeniedStatus() {
		if (LoanStateEnum.DECLINED.getStatusCode().equals(loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanRejectedStatus() {
		if (LoanStateEnum.REJECTED.getStatusCode().equals(loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanCancelledStatus() {
		if (LoanStateEnum.CANCELLED.getStatusCode().equals(loan.getStatus())) {
			return true;
		}
		return false;
	}

	protected boolean isLoanLoanPackageStatus() {
		if (LoanStateEnum.LOAN_PACKAGE.getStatusCode().equals(loan.getStatus())) {
			return true;
		}
		return false;
	}

	public Map<String, Boolean> getVestingPercentageActivityHistoryDisplayMap() {
		if (vestingPercentageActivityHistoryDisplayMap == null) {
			this.buildVestingPercentageActivityHistoryDisplayMap();
		}
		return vestingPercentageActivityHistoryDisplayMap;
	}

	public void setVestingPercentageActivityHistoryDisplayMap(
			Map<String, Boolean> vestingPercentageActivityHistoryDisplayMap) {
		this.vestingPercentageActivityHistoryDisplayMap = vestingPercentageActivityHistoryDisplayMap;
	}

	public Map<String, String> getStatesMap() {
		return stateMap;
	}

	public Map<String, String> getCountriesMap() {
		return countryMap;
	}

	public String getLoanApprovalGenericContent() throws SystemException {
		String retrievedContent = null;
		ManagedContent mangangedContent = loan
				.getManagedContent(ManagedContent.LOAN_APPROVAL_GENERIC);
		if (mangangedContent != null) {
			LoanDocumentServiceDelegate delegate = LoanDocumentServiceDelegate
					.getInstance();
            ContentText contentText = delegate.getContentTextById(
                    mangangedContent.getContentId());
            if (contentText != null) {
                retrievedContent = contentText.getText();
            } else {
                throw new SystemException("Unexpected null returned for "
                        + "contentText for contentId = "
                        + mangangedContent.getContentId());
            }
		}
		return retrievedContent;
	}

	/**
	 * Retrieves the first plan spousal consent content type retrieved from
	 * loan.getManagedContent(). There should only be one.
	 * 
	 * @return Content text corresponding to the plan spousal consent content Id
	 *         for the loan.
	 * @throws SystemException
	 */
	public String getLoanApprovalPlanSpousalConsentContent()
			throws SystemException {
		String[] spousalConsentContentTypes = new String[] {
				ManagedContent.LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_YES,
				ManagedContent.LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NO,
				ManagedContent.LOAN_APPROVAL_EXT_USER_INITIATED_PLAN_SPOUSAL_CONSENT_NULL,
				ManagedContent.LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_YES,
				ManagedContent.LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NO,
				ManagedContent.LOAN_APPROVAL_PARTICIPANT_INITIATED_PLAN_SPOUSAL_CONSENT_NULL };
		String retrievedContent = null;
		ManagedContent mangangedContent;

		for (String contentType : spousalConsentContentTypes) {
			mangangedContent = loan.getManagedContent(contentType);
			if (mangangedContent != null) {
				LoanDocumentServiceDelegate delegate = LoanDocumentServiceDelegate
						.getInstance();
				ContentText contentText = delegate.getContentTextById(
                        mangangedContent.getContentId());
	            if (contentText != null) {
	                retrievedContent = contentText.getText();
	            } else {
	                throw new SystemException("Unexpected null returned for "
	                        + "contentText for contentId = "
	                        + mangangedContent.getContentId());
	            }
				break;
			}
		}
		return retrievedContent;
	}

	/**
	 * Returns false if the create date for the Loan package status on Loan
	 * Request Status History + 30 calendar days is less than the current date.
	 * Otherwise returns true.
	 * 
	 * @return boolean
	 */
	public boolean isLoanPackageStillValidForRequest() {
		LoanActivitySummary activitySummary = loanActivities
				.getSummaryRecord(ActivitySummary.LOAN_PACKAGE_REQUESTED);
		if (activitySummary == null) {
			return false;
		}
		Date createDate = TimestampUtils.convertToDate(activitySummary
				.getCreated());
		if (createDate == null) {
			return false;
		}
		Date loanPackageInvalidDate = DateUtils.addDays(createDate,
				LoanDefaults.getLoanPackageInvalidAfterThisManyDays());
		Date currentDate = new Date();
		// Set the time component for the current date to be the start of the
		// day.
		currentDate.setHours(0);
		currentDate.setMinutes(0);
		currentDate.setSeconds(0);
		if (loanPackageInvalidDate.compareTo(currentDate) < 0) {
			return false;
		}
		return true;
	}

	public String getAccountBalanceFootnoteCmaKey() {
		if (loan.getOutstandingLoansCount() != null
		        && loan.getOutstandingLoansCount() > 0
				&& (isLoanDraftStatus() || isLoanPendingReviewStatus())) {
			return String
					.valueOf(LoanContentConstants.ACCOUNT_BALANCE_FOOTNOTE_EXCLUDES_OUTSTANDING_LOAN_BALANCES);
		}
		if (isLoanPendingApprovalStatus() || isLoanPendingAcceptanceStatus()
				|| isLoanApprovedStatus() || isLoanCompletedStatus()
				|| isLoanLoanPackageStatus() || isLoanDeniedStatus()
				|| isLoanRejectedStatus() || isLoanCancelledStatus()) {
			return String
					.valueOf(LoanContentConstants.ACCOUNT_BALANCE_FOOTNOTE_AT_TIME_REQUEST_REVIEWED);
		}
		return null;
	}

	/**
	 * Returns the loan object's abaRoutningNumber formatted for display as follows:
	 * a) null, if value is zero
	 * b) the numberic value, left padded with zeroes to length 9. 
	 * @return
	 */
	public String getAbaRountingNumber() {
	    String abaRoutingNumber = null;
	    LoanPayee requestPayee = null;
	    if (loan.getRecipient() != null) {
	        if (loan.getRecipient().getPayees() != null) {
	            requestPayee = (LoanPayee) loan.getRecipient()
	                    .getPayees().iterator().next();
	            if (requestPayee != null) {
	                if (requestPayee.getPaymentInstruction() != null ) {
	                    Integer bankTransitNumber = requestPayee.
	                            getPaymentInstruction().getBankTransitNumber();
	                    if (bankTransitNumber != null && bankTransitNumber != 0) {
	                        abaRoutingNumber = StringUtils.leftPad(
	                                bankTransitNumber.toString(), 
	                                LoanConstants.ABA_ROUTING_NUMBER_LENGTH,
	                                '0');
	                    }
	                }
	            }
	        }
	    }
	    return abaRoutingNumber;
	}    
	
	public abstract boolean isDisplayPrintLoanDocReviewButton();
	
	/**
	 * Returns true if there exists a Loan request status history of Sent For
	 * acceptance
	 * 
	 * @return
	 * @throws SystemException
	 */
	public boolean hasAlreadySentForAcceptance() throws SystemException {
		return LoanServiceDelegate.getInstance().checkLoanStatusExists(
				loan.getSubmissionId(), LoanStateEnum.PENDING_ACCEPTANCE.getStatusCode());
	}
	
	public boolean isShowDeclarationsSection() {
		return LoanDisplayHelper.isShowDeclarationsSection(loan, userRoleWithPermissions);
	}
}