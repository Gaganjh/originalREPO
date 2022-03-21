<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="payeeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestPayee" />
<un:useConstants var="payeeUiConstants" className="com.manulife.pension.ps.web.withdrawal.WithdrawalRequestPayeeUi" />
<un:useConstants var="requestConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequest" />
<un:useConstants var="noteConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestNote" />
<un:useConstants var="addressConstants" className="com.manulife.pension.service.withdrawal.valueobject.Address" />
<un:useConstants var="moneyTypeConstants" className="com.manulife.pension.service.withdrawal.valueobject.WithdrawalRequestMoneyType" />
<un:useConstants var="stateTaxConstants" className="com.manulife.pension.service.environment.valueobject.StateTaxVO" />

<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_WITHDRAWAL_VALIDATION_ABA_NUMBER_CONTENT_TEXT}"
  type="${contentConstants.TYPE_MISCELLANEOUS}"
  id="abaNumberContentValidationText"/>

<%-- Defines validations for all editable fields --%>
<script type="text/javascript">
<%-- Participant Information Section --%>
  /**
   * Validates the birth date.
   */
  function validateBirthDate() {

    <%-- Validate the birth date --%>
    var field = document.getElementById("birthDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

<%-- Basic Information Section --%>
  /**
   * Validates the expiration date.
   */
  function validateExpirationDate() {

    <%-- Validate the expiration date --%>
    var field = document.getElementById("expirationDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the termination date.
   */
  function validateTerminationDate() {

    <%-- Validate the termination date --%>
    var field = document.getElementById("terminationDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the retirement date.
   */
  function validateRetirementDate() {

    <%-- Validate the retirement date --%>
    var field = document.getElementById("retirementDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the disability date.
   */
  function validateDisabilityDate() {

    <%-- Validate the retirement date --%>
    var field = document.getElementById("disabilityDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the final contribution date.
   */
  function validateFinalContributionDate() {

    <%-- Validate the final contribution date --%>
    var field = document.getElementById("finalContributionDateId");
    if (!validateMMddYYYY(field, true)) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

<%-- Loan Details Section --%>

<%-- Amount Details Section --%>
  /**
   * Validates the withdrawal amount.
   */
  function validateWithdrawalAmount() {

    <%-- Validate that the withdrawal amount is a float --%>
    if (!validateFloat(document.getElementById("withdrawalAmountId"),
        "Dollar amount must be numeric")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row vesting percentage
   */
  function validateMoneyTypeVestingPercentage(index) {

    <%-- Validate that the vesting percentage is a float --%>
    if (!validateFloat(document.getElementById("moneyTypeVestingPercentageId[" + index + "]"),
        "Vesting % must be numeric")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row withdrawal amount
   */
  function validateMoneyTypeRequestedAmount(index) {

    <%-- Validate that the withdrawal amount is a float --%>
    if (!validateFloat(document.getElementById("moneyTypeRequestedAmountId[" + index + "]"),
        "Requested amount must be numeric")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row withdrawal percentage
   */
  function validateMoneyTypeRequestedPercentage(index) {
  
    <%-- Validate that the withdrawal percentage is a float --%>
    if (!validateFloat(document.getElementById("moneyTypeRequestedPercentageId[" + index + "]"),
      "Portion of available amount must be numeric")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  <%-- Fees Section --%>
  /**
   * Validates the specified fee value
   */
  function validateFeeValue(index) {

    <%-- Validate that the TPA fee is a float --%>
    if (!validateFloat(document.getElementById("feeValueId[" + index + "]"),
        "TPA transaction fee must be numeric")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  <%-- Fees Section --%>
  /**
   * Validates the specified state tax input
   */
  function validateStateTaxInput() {

    <%-- Validate that the state tax input is a float --%>
    if (!validateFloat(document.getElementById("recipientStateTaxInputId"),
        "State tax must be numeric")) {
      return false;
    }
    
    <%-- Field is valid --%>
    return true;
  }

  // CL 131784 : Added Validation method for Federal Tax Input
  <%-- Fees Section --%>
  /**
   * Validates the specified Federal tax input
   */
  function validateFederalTaxInput() {

    <%-- Validate that the Federal tax input is a float --%>
    if (!validateFloat(document.getElementById("recipientFederalTaxInputId"),
        "Federal tax must be numeric")) {
      return false;
    }
    
    <%-- Field is valid --%>
    return true;
  }
  

<%-- Payment Instructions Section --%>
  /**
   * Validates the specified payee rollover account number
   */
  function validatePayeeRolloverAccountNumber(recipientIndex, payeeIndex) {

    <%-- Validate that the account number is alpha-numeric --%>
    if (!validateAllowedCharacters(document.getElementById("payeeRolloverAccountNumberId[" + recipientIndex + "][" + payeeIndex + "]"),
        apolloAllowedCharacterRegEx,
        "Account number for rollover")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified payee rollover plan name
   */
  function validatePayeeRolloverPlanName(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("payeeRolloverPlanNameId[" + recipientIndex + "][" + payeeIndex + "]"),
        apolloAllowedCharacterRegEx,
        "Name of Trustee of Plan")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  <%-- EFT Payee --%>
  /**
   * Validates the specified financial institution name
   */
  function validateEftPayeeFiName(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeFiNameId[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "EFT(FI) name")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee address line 1
   */
  function validateEftPayeeAddressLine1(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
<%--    
    if (!validateAllowedCharacters(document.getElementById("eftPayeeAddressLine1Id[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "EFT(FI) Address 1")) {
      return false;
    }
--%>

    <%-- Validate that the input only contains allowed characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeAddressLine1Id[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "EFT(FI) Address 1")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee address line 2
   */
  function validateEftPayeeAddressLine2(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeAddressLine2Id[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "EFT(FI) Address 2")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee city
   */
  function validateEftPayeeCity(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeCityId[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "EFT(FI) City")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee state code
   */
  function validateEftPayeeStateInputCode(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeStateInputId[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "EFT(FI) State")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee zip code 1 value
   */
  function validateEftPayeeZipCode1(recipientIndex, payeeIndex) {

    <%-- Validate that the check zip code is numeric --%>
    if (!validateInteger(document.getElementById("eftPayeeZipCode1Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "EFT(FI) Zip Code must be ${addressConstants.ZIP_FIRST_LENGTH} digit numeric value")) {
      
      return false;
    }
    
    <%-- Validate that the zip code is ${addressConstants.ZIP_FIRST_LENGTH} digits --%>
    if (!validateMinLength(document.getElementById("eftPayeeZipCode1Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "EFT(FI) Zip Code must be ${addressConstants.ZIP_FIRST_LENGTH} digit numeric value",
        ${addressConstants.ZIP_FIRST_LENGTH})) {
       
      return false;
    }
 
    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee zip code 2 value
   */
  function validateEftPayeeZipCode2(recipientIndex, payeeIndex) {

    <%-- Validate that the eft zip code is numeric --%>
    if (!validateInteger(document.getElementById("eftPayeeZipCode2Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "EFT(FI) Zip Code extension must be ${addressConstants.ZIP_SECOND_LENGTH} digits or blank")) {
      return false;
    }
    
    <%-- Validate that the zip code extension is ${addressConstants.ZIP_SECOND_LENGTH} digits --%>
    if (!validateMinLength(document.getElementById("eftPayeeZipCode2Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "EFT(FI) Zip Code extension must be ${addressConstants.ZIP_SECOND_LENGTH} digits or blank",
        ${addressConstants.ZIP_SECOND_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee zip code 1 value
   */
  function validateEftPayeeZipCode(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeZipCodeId[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "EFT(FI) Zip Code")) {
      return false;
    }
    
    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee bank name
   */
  function validateEftPayeeBankName(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeBankNameId[" + recipientIndex + "][" + payeeIndex + "]"),
        externalPartiesAllowedCharacterRegEx,
        "Bank name")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee bank ABA number
   */
  function validateEftPayeeBankAbaNumber(recipientIndex, payeeIndex) {

    <%-- Validate that the bank ABA number is numeric --%>
    if (!validateInteger(document.getElementById("eftPayeeBankAbaNumberId[" + recipientIndex + "][" + payeeIndex + "]"),
        "ABA / Routing number must be ${payeeConstants.BANK_ABA_NUMBER_LENGTH}-digit numeric value")) {
      return false;
    }
    
    <%-- Validate that the bank ABA number is ${payeeConstants.BANK_ABA_NUMBER_LENGTH} digits --%>
    if (!validateMinLength(document.getElementById("eftPayeeBankAbaNumberId[" + recipientIndex + "][" + payeeIndex + "]"),
        '<content:getAttribute beanName="abaNumberContentValidationText" attribute="text" escapeJavaScript="true"/>',
        ${payeeConstants.BANK_ABA_NUMBER_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee bank account number
   */
  function validateEftPayeeBankAccountNumber(recipientIndex, payeeIndex) {

    <%-- Validate that the bank account number is alpha-numeric --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeBankAccountNumberId[" + recipientIndex + "][" + payeeIndex + "]"),
        apolloAllowedCharacterRegEx,
        "Account Number (for EFT)")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified eft payee credit party name
   */
  function validateEftPayeeCreditPartyName(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("eftPayeeCreditPartyNameId[" + recipientIndex + "][" + payeeIndex + "]"),
        apolloAllowedCharacterRegEx,
        "Credit Party Name")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  <%-- Check Payee --%>
  /**
   * Validates the specified check payee name
   */
  function validateCheckPayeeName(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("checkPayeeNameId[" + recipientIndex + "][" + payeeIndex + "]"),
        webAllowedCharacterRegEx,
        "Check Payee Name")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified check payee address line 1
   */
  function validateCheckPayeeAddressLine1(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("checkPayeeAddressLine1Id[" + recipientIndex + "][" + payeeIndex + "]"),
        webAllowedCharacterRegEx,
        "Check Payee Address 1")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified check payee address line 2
   */
  function validateCheckPayeeAddressLine2(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("checkPayeeAddressLine2Id[" + recipientIndex + "][" + payeeIndex + "]"),
        webAllowedCharacterRegEx,
        "Check Payee Address 2")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified check payee city
   */
  function validateCheckPayeeCity(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("checkPayeeCityId[" + recipientIndex + "][" + payeeIndex + "]"),
        webAllowedCharacterRegEx,
        "Check Payee City")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified check payee state code
   */
  function validateCheckPayeeStateInputCode(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("checkPayeeStateInputId[" + recipientIndex + "][" + payeeIndex + "]"),
        webAllowedCharacterRegEx,
        "Check Payee State")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row check zip code 1 value
   */
  function validateCheckPayeeZipCode1(recipientIndex, payeeIndex) {

    <%-- Validate that the check zip code is numeric --%>
    if (!validateInteger(document.getElementById("checkPayeeZipCode1Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "Check Payee Zip Code must be ${addressConstants.ZIP_FIRST_LENGTH} digit numeric value")) {
      return false;
    }
    
    <%-- Validate that the zip code is ${addressConstants.ZIP_FIRST_LENGTH} digits --%>
    if (!validateMinLength(document.getElementById("checkPayeeZipCode1Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "Check Payee Zip Code must be ${addressConstants.ZIP_FIRST_LENGTH} digit numeric value",
        ${addressConstants.ZIP_FIRST_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row check zip code 2 value
   */
  function validateCheckPayeeZipCode2(recipientIndex, payeeIndex) {

    <%-- Validate that the check zip code is numeric --%>
    if (!validateInteger(document.getElementById("checkPayeeZipCode2Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "Check Payee Zip Code extension must be ${addressConstants.ZIP_SECOND_LENGTH} digits or blank")) {
      return false;
    }
    
    <%-- Validate that the zip code extension is ${addressConstants.ZIP_SECOND_LENGTH} digits --%>
    if (!validateMinLength(document.getElementById("checkPayeeZipCode2Id[" + recipientIndex + "][" + payeeIndex + "]"),
        "Check Payee Zip Code extension must be ${addressConstants.ZIP_SECOND_LENGTH} digits or blank",
        ${addressConstants.ZIP_SECOND_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified check payee zip code value
   */
  function validateCheckPayeeZipCode(recipientIndex, payeeIndex) {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("checkPayeeZipCodeId[" + recipientIndex + "][" + payeeIndex + "]"),
        webAllowedCharacterRegEx,
        "Check Payee Zip Code")) {
      return false;
    }
    
    <%-- Field is valid --%>
    return true;
  }

  <%-- Recipient Information Section --%>
  /**
   * Validates the specified row Recipient address line 1 value
   */
  function validateRecipientAddressLine1(index) {
    
    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("recipientAddressLine1Id[" + index + "]"),
        webAllowedCharacterRegEx,
        "1099R Address 1")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row Recipient address line 2 value
   */
  function validateRecipientAddressLine2(index) {
    
    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("recipientAddressLine2Id[" + index + "]"),
        webAllowedCharacterRegEx,
        "1099R Address 2")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row Recipient city value
   */
  function validateRecipientCity(index) {
    
    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("recipientCityId[" + index + "]"),
        webAllowedCharacterRegEx,
        "1099R City")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row Recipient state input value
   */
  function validateRecipientStateInput(index) {
    
    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("recipientStateInputId[" + index + "]"),
        webAllowedCharacterRegEx,
        "1099R State")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row Recipient zip code 1 value
   */
  function validateRecipientZipCode1(index) {
    <%-- Validate that the zip code is numeric --%>
    if (!validateInteger(document.getElementById("recipientZipCode1Id[" + index + "]"),
        "1099R Zip Code must be ${addressConstants.ZIP_FIRST_LENGTH} digit numeric value")) {
      return false;
    }
    
    <%-- Validate that the zip code is ${addressConstants.ZIP_FIRST_LENGTH} digits --%>
    if (!validateMinLength(document.getElementById("recipientZipCode1Id[" + index + "]"),
        "1099R Zip Code must be ${addressConstants.ZIP_FIRST_LENGTH} digit numeric value",
        ${addressConstants.ZIP_FIRST_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row Recipient zip code 2 value
   */
  function validateRecipientZipCode2(index) {
    
    <%-- Validate that the zip code is numeric --%>
    if (!validateInteger(document.getElementById("recipientZipCode2Id[" + index + "]"),
        "1099R Zip Code extension must be ${addressConstants.ZIP_SECOND_LENGTH} digits or blank")) {
      return false;
    }
    
    <%-- Validate that the zip code extension is ${addressConstants.ZIP_SECOND_LENGTH} digits --%>
    if (!validateMinLength(document.getElementById("recipientZipCode2Id[" + index + "]"),
        "1099R Zip Code extension must be ${addressConstants.ZIP_SECOND_LENGTH} digits or blank",
        ${addressConstants.ZIP_SECOND_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the specified row Recipient zip code value
   */
  function validateRecipientZipCode(index) {
    
    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("recipientZipCodeId[" + index + "]"),
        webAllowedCharacterRegEx,
        "1099R Zip Code")) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  <%-- Notes Section --%>
  /**
   * Validates the admin to participant note.
   */
  function validateAdminToParticipantNote() {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("adminToParticipantNoteId"),
        webMultiLineAllowedCharacterRegEx,
        "Participant note")) {
      return false;
    }

    <%-- Validate the max length on the note --%>
    if (!validateMaxLength(document.getElementById("adminToParticipantNoteId"),
        "Participant note",
        ${noteConstants.MAXIMUM_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

  /**
   * Validates the admin to admin note.
   */
  function validateAdminToAdminNote() {

    <%-- Validate that the input does not contain any special characters --%>
    if (!validateAllowedCharacters(document.getElementById("adminToAdminNoteId"),
        webMultiLineAllowedCharacterRegEx,
        "Administrator note")) {
      return false;
    }

    <%-- Validate the max length on the note --%>
    if (!validateMaxLength(document.getElementById("adminToAdminNoteId"),
        "Administrator note",
        ${noteConstants.MAXIMUM_LENGTH})) {
      return false;
    }

    <%-- Field is valid --%>
    return true;
  }

<%-- Declarations Section --%>

<%-- Global Section --%>
  <%-- Global Section --%>
  /**
   * Validates all the fields on the Withdrawal Step2 Form in order to prevent submit if errors.
   *
   * @param recalculateRequiredCheck True if the recalculate button check should be done.
   * @param recalculateClicked True if the recalculate button was clicked.
   * @param saveClicked True if the save button was clicked.
   */
  function validateFormFields(recalculateRequiredCheck, recalculateClicked, saveClicked) {

    <%-- Validate Participant Information Section --%>
    if (!validateBirthDate()) {
      return false;
    }

    <%-- Validate Basic Information Section --%>
    if (!validateExpirationDate()) {
      return false;
    }
    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_TERMINATION_OF_EMPLOYMENT_CODE || withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}">
      if (!validateTerminationDate()) {
        return false;
      }
    </c:if>
    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_RETIREMENT_CODE}">
      if (!validateRetirementDate()) {
        return false;
      }
    </c:if>
    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode == requestConstants.WITHDRAWAL_REASON_DISABILITY_CODE}">
      if (!validateDisabilityDate()) {
        return false;
      }
    </c:if>
    <c:if test="${withdrawalForm.withdrawalRequestUi.showFinalContributionDate}">
      if (!validateFinalContributionDate()) {
        return false;
      }
    </c:if>

    <%-- Money Types Section --%>
    <%-- Test specific amount if visible --%>
    var varAmtTypeCode;
  	var amtTypeElement = document.getElementById('amountTypeCodeId');
  	
  	if(amtTypeElement == 'select') {
  	
   	 varAmtTypeCode = getSelectedValueById('amountTypeCodeId');
   	}else {
   
   	  varAmtTypeCode = amtTypeElement.value;
   	}
    if ('${requestConstants.WITHDRAWAL_AMOUNT_SPECIFIC_AMOUNT_CODE}' == varAmtTypeCode) {
      if (!validateWithdrawalAmount()) {
        return false;
      }
    }

    <c:forEach items="${withdrawalForm.withdrawalRequestUi.moneyTypes}"
               var="moneyType"
               varStatus="moneyTypeStatus">
      <%-- Check if vesting percentage is editable --%>
      <c:if test="${moneyType.withdrawalRequestMoneyType.vestingPercentageUpdateable}">
        if (!validateMoneyTypeVestingPercentage(${moneyTypeStatus.index})) {
          return false;
        }
      </c:if>
      if (!validateMoneyTypeRequestedAmount(${moneyTypeStatus.index})) {
        return false;
      }
      if (!validateMoneyTypeRequestedPercentage(${moneyTypeStatus.index})) {
        return false;
      }
    </c:forEach>

    <%-- Fees Section --%>
    <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.contractInfo.hasATpaFirm}">
      <c:forEach items="${withdrawalForm.withdrawalRequestUi.fees}"
                 var="fee"
                 varStatus="feeStatus">

        if (!validateFeeValue(${feeStatus.index})) {
          return false;
        }
      </c:forEach>
    </c:if>

    <%-- Payee Section --%>
    <c:forEach items="${withdrawalForm.withdrawalRequestUi.recipients}"
               var="recipient"
               varStatus="recipientStatus">
      <c:forEach items="${recipient.payees}"
                 var="payee"
                 varStatus="payeeStatus">

        <%-- Handle rollover account number if visible --%>
        <c:if test="${payee.showAccountNumber}">
          if (!validatePayeeRolloverAccountNumber(${recipientStatus.index}, ${payeeStatus.index})) {
            return false;
          }
        </c:if>
        <%-- Handle rollover plan name if visible --%>
        <c:if test="${payee.showTrusteeForRollover}">
          if (!validatePayeeRolloverPlanName(${recipientStatus.index}, ${payeeStatus.index})) {
            return false;
          }
        </c:if>

        <%-- Handle payee details if present --%>
        if ('${requestConstants.WITHDRAWAL_REASON_MANDATORY_DISTRIBUTION_TERM_CODE}' != '${withdrawalForm.withdrawalRequestUi.withdrawalRequest.reasonCode}'
            || ('${requestConstants.IRA_SERVICE_PROVIDER_WMSI_CODE}' != getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'))
            && '${requestConstants.IRA_SERVICE_PROVIDER_PENCHECKS_CODE}' != getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode')))) {

          <%-- Handle eft payee if visible --%>
          if ('${payeeConstants.ACH_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))
              || '${payeeConstants.WIRE_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))) {
              
            if (!validateEftPayeeFiName(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            if (!validateEftPayeeAddressLine1(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            if (!validateEftPayeeAddressLine2(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            if (!validateEftPayeeCity(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            
            var eftCountryPayee;
            var eftCountryElement = document.getElementById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
            if(eftCountryElement == 'select') {
            	eftCountryPayee = getSelectedValueById('eftPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
            }else {
            	eftCountryPayee = eftCountryElement.value;
            }
            <%-- Test zip code and state input if visible --%>
            if (eftCountryPayee != '${addressConstants.USA_COUNTRY_CODE}') {
              if (!validateEftPayeeStateInputCode(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
              if (!validateEftPayeeZipCode(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
            } else {
              <%-- Otherwise test zip code 1 and 2 if visible --%>
              if (!validateEftPayeeZipCode1(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
              if (!validateEftPayeeZipCode2(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
            }
            if (!validateEftPayeeBankName(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            if(!recalculateClicked) {            
              if (!validateEftPayeeBankAbaNumber(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
            }
            <%-- Handle bank account number if visible --%>
            if (!validateEftPayeeBankAccountNumber(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            <%-- Handle bank account number if editable --%>
<%--            <c:if test="${payee.creditPartyNameEditable}">
              if (!validateEftPayeeCreditPartyName(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
            </c:if> --%>
          }
          
          <%-- Handle check payee if visible --%>
          if ('${payeeConstants.CHECK_PAYMENT_METHOD_CODE}' == getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.recipients[${recipientStatus.index}].payees[${payeeStatus.index}].paymentMethodCode'))) {
          
            if (!validateCheckPayeeName(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            if (!validateCheckPayeeAddressLine1(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            if (!validateCheckPayeeAddressLine2(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            if (!validateCheckPayeeCity(${recipientStatus.index}, ${payeeStatus.index})) {
              return false;
            }
            
            var chkCountryPayee;
            var chkCountryElement = document.getElementById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
            if(chkCountryElement == 'select') {
            	chkCountryPayee = getSelectedValueById('checkPayeeCountryId[${recipientStatus.index}][${payeeStatus.index}]');
            }else {
            	chkCountryPayee = chkCountryElement.value;
            }
            <%-- Test state input if visible --%>
            if (chkCountryPayee == '${addressConstants.USA_COUNTRY_CODE}') {
              if (!validateCheckPayeeZipCode1(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
              if (!validateCheckPayeeZipCode2(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
            } else {
              if (!validateCheckPayeeStateInputCode(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
              if (!validateCheckPayeeZipCode(${recipientStatus.index}, ${payeeStatus.index})) {
                return false;
              }
            }
          }
        }
      </c:forEach>

      <%-- Handle Recipient validations if present --%>      
      <c:if test="${withdrawalForm.withdrawalRequestUi.withdrawalRequest.paymentTo != requestConstants.PAYMENT_TO_PLAN_TRUSTEE_CODE}">
      
        if (!validateRecipientAddressLine1(${recipientStatus.index})) {
          return false;
        }
        
        if (!validateRecipientAddressLine2(${recipientStatus.index})) {
          return false;
        }
       
        if (!validateRecipientCity(${recipientStatus.index})) {
          return false;
        }
      
        var recipientCountryPayee;
        var recipientCountryElement = document.getElementById('recipientCountryId[${recipientStatus.index}]');
        if(recipientCountryElement == 'select') {
        	recipientCountryPayee = getSelectedValueById('recipientCountryId[${recipientStatus.index}]');
        }else {
        	recipientCountryPayee = recipientCountryElement.value;
        } 
      
        <%-- Handle zip code 1 and 2 if visible otherwise handle zipcode --%>
        if (recipientCountryPayee == '${addressConstants.USA_COUNTRY_CODE}') {
      
          if (!validateRecipientZipCode1(${recipientStatus.index})) {
            return false;
          }
           
          if (!validateRecipientZipCode2(${recipientStatus.index})) {
            return false;
          }
     ;  
        } else {
          if (!validateRecipientStateInput(${recipientStatus.index})) {
            return false;
          }
      
          if (!validateRecipientZipCode(${recipientStatus.index})) {
            return false;
          }
       
        }
      </c:if>
    </c:forEach>

    <%-- Notes Section --%>
    if (!validateAdminToParticipantNote()) {
      return false;
    }
    if (!validateAdminToAdminNote()) {
      return false;
    }

    <%-- If required do a recalculation check --%>
    if (recalculateRequiredCheck) {
       return checkRecalculateRequired();
    }
      
    <%-- Form is valid --%>
    return true;
  }

</script>