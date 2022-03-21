<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<%-- Defines handlers for all editable fields --%>
<script type="text/javascript">

var noIndex = null;
var noValidate = null;
var noRecalculate = null;
var noPreProcess = null;
var noPostProcess = null;
var noUpdate = null;
var update = updateStep1Page;
var preProcess = preProcessField;
var noPreProcessArg = null;
var noPostProcessArg = null;

<%-- Participant Information Section --%>
/**
 * Handler for changes in the state of residence
 */ 
 function handleStateOfResidenceChanged() {

    <%-- Grab field --%>
    var field = document.getElementById("stateOfResidenceId");

    <%-- Pre-process --%>

    <%-- Flag updates --%>
    onFieldChange(field);

    <%-- Validation (if necessary) --%>

    <%-- Page Update (if necessary) --%>

    <%-- Post-process (if necessary) --%>

    <%-- Allow processing to continue --%>    
    return true;    
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
 * Handler for changes in the withdrawal reason
 */ 
 function handleWithdrawalReasonChanged() {

    <%-- Grab field --%>
    var field = document.getElementById("withdrawalReasonId");

    <%-- Pre-process --%>

    <%-- Flag updates --%>
    onFieldChange(field);

    <%-- Validation (if necessary) --%>

    <%-- Page Update (if necessary) --%>
    updateStep1Page();

    <%-- Post-process (if necessary) --%>

    <%-- Allow processing to continue --%>
          
    return true;    
 }

/**
 * Handler for changes in the hardship reason
 */ 
 function handleHardshipReasonChanged() {

    <%-- Grab field --%>
    var field = document.getElementById("hardshipReasonId");

    <%-- Pre-process --%>

    <%-- Flag updates --%>
    onFieldChange(field);

    <%-- Validation (if necessary) --%>

    <%-- Page Update (if necessary) --%>

    <%-- Post-process (if necessary) --%>

    <%-- Allow processing to continue --%>    
    return true;    
 }

/**
 * Handler for changes in the hardship explanation.
 */ 
 function handleHardshipExplanationChanged() {

    <%-- Grab field --%>
    var field = document.getElementById("hardshipExplanationId");

    <%-- Pre-process --%>

    <%-- Flag updates --%>
    onFieldChange(field);

    <%-- Validation (if necessary) --%>
    if (!validateHardshipExplanation()) {
      return false;
    }

    <%-- Page Update (if necessary) --%>

    <%-- Post-process (if necessary) --%>

    <%-- Allow processing to continue --%>    
    return true;    
 }

/**
 * Handler for changes in the WMSI Paychecks
 */ 
 var oldWmsiPaychecks = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode)}";
  function handleWmsiPaychecksChanged() {
    <%-- Radio buttons must use on click to capture changed value - so we need to filter out situations where user clicks on selected value --%>
    var newValue = getCheckedValue(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'));
    if (newValue != oldWmsiPaychecks) {
      // Set dirty flag
      onFieldChange(document.getElementsByName('withdrawalRequestUi.withdrawalRequest.iraServiceProviderCode'));
      // Update page
      updateStep1Page();
    }

    <%-- Update our copy of the old value --%>
    oldWmsiPaychecks = newValue;
  }

/**
 * Handler for changes in the Participant Leaving Plan
 */ 
 var oldParticipantLeavingPlan = "${e:forJavaScriptBlock(withdrawalForm.withdrawalRequestUi.participantLeavingPlanInd)}";
  function handleParticipantLeavingPlanChanged() {
    <%-- Radio buttons must use on click to capture changed value - so we need to filter out situations where user clicks on selected value --%>
    var newValue = getCheckedValue(document.getElementsByName('withdrawalRequestUi.participantLeavingPlanInd'));
    if (newValue != oldParticipantLeavingPlan) {
      // Set dirty flag
      onFieldChange(document.getElementsByName('withdrawalRequestUi.participantLeavingPlanInd'));
      // Update page
      updateStep1Page();
    }

    <%-- Update our copy of the old value --%>
    oldParticipantLeavingPlan = newValue;
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

/**
 * Handler for changes in the payment to
 */ 
 function handlePaymentToChanged() {

    <%-- Grab field --%>
    var field = document.getElementById("paymentToId");

    <%-- Pre-process --%>
    preProcessField(field);

    <%-- Flag updates --%>
    onFieldChange(field);

    <%-- Validation (if necessary) --%>

    <%-- Page Update (if necessary) --%>
    updateStep1Page();

    <%-- Post-process (if necessary) --%>

    <%-- Allow processing to continue --%>    
    return true;    
 }

<%-- Loan Details Section --%>
/**
 * Handler for changes in the loan options
 */ 
 function handleLoanOptionChanged() {

    <%-- Grab field --%>
    var field = document.getElementById("loanOptionId");

    <%-- Pre-process --%>

    <%-- Flag updates --%>
    onFieldChange(field);

    <%-- Validation (if necessary) --%>

    <%-- Page Update (if necessary) --%>
    updateStep1Page();

    <%-- Post-process (if necessary) --%>

    <%-- Allow processing to continue --%>    
    return true;    
 }

/**
 * Handler for changes in the loan IRS distribution code options
 */ 
 function handleLoanIrsDistributionCodeChanged() {

    <%-- Grab field --%>
    var field = document.getElementById("loanIrsDistributionCodeId");

    <%-- Pre-process --%>

    <%-- Flag updates --%>
    onFieldChange(field);

    <%-- Validation (if necessary) --%>

    <%-- Page Update (if necessary) --%>

    <%-- Post-process (if necessary) --%>

    <%-- Allow processing to continue --%>    
    return true;    
 }





<%-- Action Section --%>
  <%-- Handles save and exit being clicked --%>
  function handleSaveAndExitClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    <%-- Perform validation --%>  
    if (!validateFormFields(false)) {
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

  <%-- Handles next being clicked --%>
  function handleNextClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    var isValid = validateFormFields(true);
    if (!isValid) {
      resetSubmitInProgress();
    }
    
    return isValid;
  }   
</script>