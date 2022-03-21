<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="un"
  uri="http://jakarta.apache.org/taglibs/unstandard-1.0"%>
<%@ taglib prefix="content" uri="manulife/tags/content"%>

<un:useConstants var="moneyTypeConstants"
  className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType" />
<un:useConstants var="requestConstants"
  className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="contentConstants"
  className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_STEP_2_APPROVE_LEGALESE_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}" id="approveLegaleseText" />

<%-- Defines handlers for all editable fields --%>
<script type="text/javascript">

var noIndex = null;
var noValidate = null;
var noRecalculate = null;
var noPreProcess = null;
var noPostProcess = null;
var noUpdate = null;
var update = updateReviewPage;
var recalculate = setRecalculateRequired;
var preProcess = preProcessField;
var noPreProcessArg = null;
var noPostProcessArg = null;

<%-- Participant Information Section --%>
/**
 * Handler for changes in the state of residence
 */ 
 function handleStateOfResidenceChanged() {
    return handleFieldChanged(document.getElementById("stateOfResidenceId"), 
                              noIndex, noIndex, noValidate, recalculate, update, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the birth date
 */ 
 function handleBirthDateChanged() {
    return handleFieldChanged(document.getElementById("birthDateId"), 
                              noIndex, noIndex, validateBirthDate, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

<%-- Basic Information Section --%>
/**
 * Handler for changes in the expiration date
 */ 
 function handleExpirationDateChanged() {
    return handleFieldChanged(document.getElementById("expirationDateId"), 
                              noIndex, noIndex, validateExpirationDate, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the hardship reason
 */ 
 function handleHardshipReasonChanged() {
    return handleFieldChanged(document.getElementById("hardshipReasonId"), 
                              noIndex, noIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the WMSI Paychecks
 */ 
 var oldWmsiPaychecks = '${withdrawalForm.withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode}';

  function handleWmsiPaychecksChanged() {
    <%-- Radio buttons must use on click to capture changed value - so we need to filter out situations where user clicks on selected value --%>
    var newValue = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'));
    if (newValue != oldWmsiPaychecks) {
      // Set dirty flag
      onFieldChange(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'));
      // Update page
      updateReviewPage();
    }

    <%-- Update our copy of the old value --%>
    oldWmsiPaychecks = newValue;
  }

/**
 * Handler for changes in the termination date
 */ 
 function handleTerminationDateChanged() {
 
    <%-- Update robust date indicator --%>
    checkRobustDateChangedAfterVesting(); 
 
    return handleFieldChanged(document.getElementById("terminationDateId"), 
                              noIndex, noIndex, validateTerminationDate, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the retirement date
 */ 
 function handleRetirementDateChanged() {

    <%-- Update robust date indicator --%>
    checkRobustDateChangedAfterVesting(); 
 
    return handleFieldChanged(document.getElementById("retirementDateId"), 
                              noIndex, noIndex, validateRetirementDate, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the disability date
 */ 
 function handleDisabilityDateChanged() {
 
    <%-- Update robust date indicator --%>
    checkRobustDateChangedAfterVesting(); 
 
    return handleFieldChanged(document.getElementById("disabilityDateId"), 
                              noIndex, noIndex, validateDisabilityDate, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the final contribution date
 */ 
 function handleFinalContributionDateChanged() {
    return handleFieldChanged(document.getElementById("finalContributionDateId"), 
                              noIndex, noIndex, validateFinalContributionDate, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }
 
<%-- Loan Details Section --%>
/**
 * Handler for changes in the loan options
 */ 
 function handleLoanOptionChanged() {
    return handleFieldChanged(document.getElementById("loanOptionId"), 
                              noIndex, noIndex, noValidate, noRecalculate, update, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the loan IRS distribution code options
 */ 
 function handleLoanIrsDistributionCodeChanged() {
    return handleFieldChanged(document.getElementById("loanIrsDistributionCodeId"), 
                              noIndex, noIndex, noValidate, noRecalculate, update, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }
 
<%-- Amount Details Section --%>
/**
 * Handler for changes in the withdrawal amount
 */ 
 function handleAmountTypeChanged() {
    return handleFieldChanged(document.getElementById("amountTypeCodeId"), 
                              noIndex, noIndex, noValidate, recalculate, update, 
                              noPreProcess, noPreProcessArg, postProcessAmountField, noPostProcessArg);
 }

/**
 * Handler for changes in the withdrawal amount
 */ 
 function handleWithdrawalAmountChanged() {
    return handleFieldChanged(document.getElementById("withdrawalAmountId"), 
                              noIndex, noIndex, validateWithdrawalAmount, recalculate, noUpdate, 
                              preProcess, noPreProcessArg, postProcessAmountField, noPostProcessArg);
 }
 
/**
 * Handler for changes in the vesting percentage
 */ 
 function handleMoneyTypeVestingPercentageChanged(index) {
    return handleFieldChanged(document.getElementById("moneyTypeVestingPercentageId[" + index + "]"), 
                              index, noIndex, validateMoneyTypeVestingPercentage, recalculate, noUpdate, 
                              preProcess, noPreProcessArg, postProcessPercentageField, ${moneyTypeConstants.VESTING_PERCENTAGE_SCALE});
 }

/**
 * Handler for changes in the withdrawal amount
 */ 
 function handleMoneyTypeRequestedAmountChanged(index) {
    return handleFieldChanged(document.getElementById("moneyTypeRequestedAmountId[" + index + "]"), 
                              index, noIndex, validateMoneyTypeRequestedAmount, recalculate, updateTotalRequestedAmount, 
                              preProcess, noPreProcessArg, postProcessAmountField, noPostProcessArg);
 }
 
/**
 * Handler for changes in the requested percentage
 */ 
 function handleMoneyTypeRequestedPercentageChanged(index) {
    return handleFieldChanged(document.getElementById("moneyTypeRequestedPercentageId[" + index + "]"), 
                              index, noIndex, validateMoneyTypeRequestedPercentage, recalculate, noUpdate, 
                              preProcess, noPreProcessArg, postProcessPercentageField, ${moneyTypeConstants.WITHDRAWAL_PERCENTAGE_SCALE});
 }

<%-- TPA Section --%>
/**
 * Handler for changes in the fee value
 */ 
 function handleFeeValueChanged(index) {
    return handleFieldChanged(document.getElementById("feeValueId[" + index + "]"), 
                              index, noIndex, validateFeeValue, recalculate, noUpdate, 
                              preProcess, noPreProcessArg, postProcessAmountField, noPostProcessArg);
 }

/**
 * Handler for changes in the TPA fee amount type
 */ 
 function handleFeeValueTypeChanged(index) {
    return handleFieldChanged(document.getElementById("feeValueTypeId[" + index + "]"), 
                              index, noIndex, noValidate, recalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

<%-- Options for Unvested Section --%>
/**
 * Handler for changes in the option for unvested money
 */ 
 function handleOptionForUnvestedMoneyChanged() {
    return handleFieldChanged(document.getElementById("optionForUnvestedMoneyId"), 
                              noIndex, noIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

<%-- Tax Section --%>
/**
 * Handler for changes in the federal tax drop down
 */ 
 function handleFederalTaxChanged(index) {
    return handleFieldChanged(document.getElementById("recipientFederalTaxId[" + index + "]"), 
                              index, noIndex, noValidate, recalculate, update, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }
 
 /**
  * Handler for changes in the federal tax input
  */ 
  function handleFederalTaxInputChanged() {
     return handleFieldChanged(document.getElementById("recipientFederalTaxInputId"), 
                               noindex, noIndex, validateFederalTaxInput, recalculate, update, 
                               preProcess, noPreProcessArg, postProcessPercentageField, ${requestConstants.TAX_PERCENTAGE_SCALE});
  }

/**
 * Handler for changes in the state tax input
 */ 
 function handleStateTaxInputChanged() {
    return handleFieldChanged(document.getElementById("recipientStateTaxInputId"), 
                              noIndex, noIndex, validateStateTaxInput, recalculate, noUpdate, 
                              preProcess, noPreProcessArg, postProcessPercentageField, ${requestConstants.TAX_PERCENTAGE_SCALE});
 }
 
/**
 * Handler for changes in the state tax dropdown
 */ 
 function handleStateTaxDropdownChanged() {
    return handleFieldChanged(document.getElementById("recipientStateTaxDropdownId"), 
                              noIndex, noIndex, noValidate, recalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }
 
<%-- Payment Instructions Section --%>
/**
 * Handler for changes in the payee RolloverType
 */ 
 function handlePayeeRolloverTypeChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeRolloverTypeId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, update, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

 
 
 
 <%-- Payment Instructions Section --%>
/**
 * Handler for changes in the payee RolloverType
 */ 
 function handleCheckPayeeRolloverTypeChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeRolloverTypeId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, update, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }
 
<%-- Payment Instructions Section --%>
/**
 * Handler for changes in the payee rollover account number
 */ 
 function handlePayeeRolloverAccountNumberChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("payeeRolloverAccountNumberId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validatePayeeRolloverAccountNumber, noRecalculate, update, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the payee rollover plan name
 */ 
 function handlePayeeRolloverPlanNameChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("payeeRolloverPlanNameId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validatePayeeRolloverPlanName, noRecalculate, update, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the payee IRS distribution code
 */ 
 function handlePayeeIrsDistributionCodeChanged(recipientIndex, payeeIndex) {
     return handleFieldChanged(document.getElementById("payeeIrsDistributionCodeId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, update, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

<%-- EFT Payee Section --%>
/**
 * Handler for changes in the eft payee fi name
 */ 
 function handleEftPayeeFiNameChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeFiNameId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeFiName, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee address line 1
 */ 
 function handleEftPayeeAddressLine1Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeAddressLine1Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeAddressLine1, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee address line 2
 */ 
 function handleEftPayeeAddressLine2Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeAddressLine2Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeAddressLine2, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee city
 */ 
 function handleEftPayeeCityChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeCityId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeCity, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee state dropdown
 */ 
 function handleEftPayeeStateDropdownChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeStateDropdownId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee state input
 */ 
 function handleEftPayeeStateInputChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeStateInputId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeStateInputCode, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee zip code 1
 */ 
 function handleEftPayeeZipCode1Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeZipCode1Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeZipCode1, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee zip code 2
 */ 
 function handleEftPayeeZipCode2Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeZipCode2Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeZipCode2, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee zip code
 */ 
 function handleEftPayeeZipCodeChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeZipCodeId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeZipCode, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee country
 */ 
 function handleEftPayeeCountryChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeCountryId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, update, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee bank name
 */ 
 function handleEftPayeeBankNameChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeBankNameId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeBankName, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee bank ABA number
 */ 
 function handleEftPayeeBankAbaNumberChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeBankAbaNumberId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeBankAbaNumber, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the eft payee bank account number
 */ 
 function handleEftPayeeBankAccountNumberChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeBankAccountNumberId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeBankAccountNumber, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }
 
/**
 * Handler for changes in the eft payee credit party name
 */
<%-- 
 function handleEftPayeeCreditPartyNameChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("eftPayeeCreditPartyNameId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateEftPayeeCreditPartyName, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 } --%>
 
<%-- Check Payee Section --%>
/**
 * Handler for changes in the check payee name
 */ 
 function handleCheckPayeeNameChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeNameId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeName, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee address line 1
 */ 
 function handleCheckPayeeAddressLine1Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeAddressLine1Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeAddressLine1, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee address line 2
 */ 
 function handleCheckPayeeAddressLine2Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeAddressLine2Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeAddressLine2, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee city
 */ 
 function handleCheckPayeeCityChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeCityId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeCity, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee state dropdown
 */ 
 function handleCheckPayeeStateDropdownChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeStateDropdownId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee state input
 */ 
 function handleCheckPayeeStateInputChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeStateInputId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeStateInputCode, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee zip code 1
 */ 
 function handleCheckPayeeZipCode1Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeZipCode1Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeZipCode1, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee zip code 2
 */ 
 function handleCheckPayeeZipCode2Changed(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeZipCode2Id[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeZipCode2, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee zip code
 */ 
 function handleCheckPayeeZipCodeChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeZipCodeId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, validateCheckPayeeZipCode, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee country
 */ 
 function handleCheckPayeeCountryChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeCountryId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, update, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the check payee send to address above
 */ 
 function handleSendToAddressAboveChanged(recipientIndex, payeeIndex) {
    return handleFieldChanged(document.getElementById("checkPayeeSendToAddressAboveId[" + recipientIndex + "][" + payeeIndex + "]"), 
                              recipientIndex, payeeIndex, noValidate, noRecalculate, noUpdate, preprocessCheckBox, 
                              document.getElementById("checkPayeeSendToAddressAboveCheckboxId[" + recipientIndex + "][" + payeeIndex + "]"),
                              noPostProcess, noPostProcessArg);
 }

<%-- Recipient Information Section --%>
/**
 * Handler for changes in the address line 1
 */ 
 function handleRecipientAddressLine1Changed(index) {
    return handleFieldChanged(document.getElementById("recipientAddressLine1Id[" + index + "]"), 
                              index, noIndex, validateRecipientAddressLine1, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the address line 2
 */ 
 function handleRecipientAddressLine2Changed(index) {
    return handleFieldChanged(document.getElementById("recipientAddressLine2Id[" + index + "]"), 
                              index, noIndex, validateRecipientAddressLine2, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the city
 */ 
 function handleRecipientCityChanged(index) {
    return handleFieldChanged(document.getElementById("recipientCityId[" + index + "]"), 
                              index, noIndex, validateRecipientCity, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the state dropdown
 */ 
 function handleRecipientStateDropdownChanged(index) {
    return handleFieldChanged(document.getElementById("recipientStateDropdownId[" + index + "]"), 
                              index, noIndex, noValidate, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the state input
 */ 
 function handleRecipientStateInputChanged(index) {
    return handleFieldChanged(document.getElementById("recipientStateInputId[" + index + "]"), 
                              index, noIndex, validateRecipientStateInput, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the zip code 1
 */ 
 function handleRecipientZipCode1Changed(index) {
    return handleFieldChanged(document.getElementById("recipientZipCode1Id[" + index + "]"), 
                              index, noIndex, validateRecipientZipCode1, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the zip code 2
 */ 
 function handleRecipientZipCode2Changed(index) {
    return handleFieldChanged(document.getElementById("recipientZipCode2Id[" + index + "]"), 
                              index, noIndex, validateRecipientZipCode2, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the zip code
 */ 
 function handleRecipientZipCodeChanged(index) {
    return handleFieldChanged(document.getElementById("recipientZipCodeId[" + index + "]"), 
                              index, noIndex, validateRecipientZipCode, noRecalculate, noUpdate, 
                              preProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the country
 */ 
 function handleRecipientCountryChanged(index) {
    return handleFieldChanged(document.getElementById("recipientCountryId[" + index + "]"), 
                              index, noIndex, noValidate, noRecalculate, update, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

<%-- Notes Section --%>
/**
 * Handler for changes in the admin to participant note
 */ 
 function handleAdminToParticipantNoteChanged() {
    return handleFieldChanged(document.getElementById("adminToParticipantNoteId"), 
                              noIndex, noIndex, validateAdminToParticipantNote, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the admin to admin note
 */ 
 function handleAdminToAdminNoteChanged() {
    return handleFieldChanged(document.getElementById("adminToAdminNoteId"), 
                              noIndex, noIndex, validateAdminToAdminNote, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

<%-- Declaration Section --%>
/**
 * Handler for changes in the tax notice declaration
 */ 
 function handleTaxNoticeDeclarationChanged() {
    return handleFieldChanged(document.getElementById("taxNoticeDeclarationId"), 
                              noIndex, noIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the waiting period declaration
 */ 
 function handleWaitingPeriodDeclarationChanged() {
    return handleFieldChanged(document.getElementById("waitingPeriodDeclarationId"), 
                              noIndex, noIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }
 
 /**
 * Handler for changes in the pin exposure declaration
 */ 
 function handleRiskPinExposureDeclarationChanged() {
    return handleFieldChanged(document.getElementById("riskPinExpDeclarationId"), 
                              noIndex, noIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

/**
 * Handler for changes in the WMSI declaration
 */ 
 function handleWmsiDeclarationChanged() {
    return handleFieldChanged(document.getElementById("wmsiDeclarationId"), 
                              noIndex, noIndex, noValidate, noRecalculate, noUpdate, 
                              noPreProcess, noPreProcessArg, noPostProcess, noPostProcessArg);
 }

<%-- Actions Section --%>
/**
 * Handler for recalculate button
 */ 
 function handleRecalculateButtonClicked() {

   <%-- Check if submit is in progress --%>
   var submitInProgress = isSubmitInProgress();
   if (submitInProgress) {
     return false;
   }  
  
   <%-- Perform validation --%>  
   if (!validateFormFields(false, true, false)) {
      resetSubmitInProgress();
      return false;
   }
   
   return true;
 }

  <%-- Handles save and exit being clicked --%>
  function handleSaveAndExitClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    <%-- Perform validation --%>  
    if (!validateFormFields(false, false, true)) {
      resetSubmitInProgress();
      return false;
    }
   
    return true;
  }  
  
  <%-- Handles cancel and exit being clicked --%>
  function handleCancelAndExitClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  

    <%-- Check if page has been changed --%>
    if (document.getElementById('dirty').value == 'true') {
  
      <%-- Check user confirmation --%>
      var confirmResponse = confirm('Are you sure?');
      if (!confirmResponse) {
        resetSubmitInProgress();
      }
    
      return confirmResponse;
    } else {
      <%-- No changes to page so we don't need to confirm --%>
      return true;
    }
  }  
  
  <%-- Handles delete being clicked --%>
  function handleDeleteClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    <%-- Check user confirmation --%>
    var confirmResponse = confirm('Are you sure?');
    if (!confirmResponse) {
      resetSubmitInProgress();
    }
  
    return confirmResponse;
  }  

  <%-- Handles deny being clicked --%>
  function handleDenyClicked() {

    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  

    var isValid = validateFormFields(false, false, false);
  
    if (!isValid) {
	  resetSubmitInProgress();
      return isValid;
    }
  
    <%-- Check user confirmation --%>
    var confirmResponse = confirm('Withdrawal request will be permanently declined and cannot proceed beyond this stage.  Are you sure?');
    if (!confirmResponse) {
      resetSubmitInProgress();
    }
  
    return confirmResponse;
  }  
  
  <%-- Handles send for approve being clicked --%>
  function handleSendForApproveClicked() {

    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
    
    var isValid = validateFormFields(true, false, false);
    if (!isValid) {
      resetSubmitInProgress();
      return isValid;
    }
    
    <%-- Check user confirmation --%>
    var confirmResponse = confirm('Are you sure?');
    if (!confirmResponse) {
      resetSubmitInProgress();
    }
  
    return confirmResponse;
  }  
  
  <%-- Handles approve being clicked --%>
  function handleApproveClicked() {
	  <%-- Check if submit is in progress --%>
	    var submitInProgress = isSubmitInProgress();
	    if (submitInProgress) {
	      return false;
	    }  

    var isValid = validateFormFields(true, false, false);
   
    if (!isValid) {
      resetSubmitInProgress();
      return isValid;
    }
  }  

  <%-- General Section --%>
  <%-- Opens up a window with the employee snapshot --%>
  function doEmployeeSnapshot(employeeProfileId) {
  
    var printURL = "/do/census/viewEmployeeSnapshot/" + "?profileId=" + employeeProfileId + "&printFriendly=true";
    window.open(printURL,"","width=720,height=480,resizable,toolbar,scrollbars,");
  }
</script>
