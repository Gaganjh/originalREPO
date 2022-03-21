<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<%@ page import="com.manulife.pension.content.valueobject.*" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean contentId="81088" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="rolloverContributionsErrorText" />
	
<content:contentBean contentId="81255" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="acknowledgeCheckboxErrorText" />

<content:contentBean contentId="81256" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="automaticEnrollmentSecErrorText" />	
	
<content:contentBean contentId="81257" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="autoContributionIncSecErrorText" />	
	
<content:contentBean contentId="80716" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="cancelAndExitMessage" />	
	
<content:contentBean contentId="92796" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="emptyPlanYearEndMessage" />
	
<%-- Added as part of DR-346147 --%>
<content:contentBean contentId="94065" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="employerTaxIdentificationNumberandIRSNumberMessage" />
	
<content:contentBean contentId="94067" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="employerTaxIdentificationNumberMessage" />
	
<content:contentBean contentId="94066" 
	type="${contentConstants.TYPE_MISCELLANEOUS}" 
	id="IRSNumberMessage" />
	
<%-- Defines page level event handlers --%>
<script type="text/javascript">

   var rolloverContributionsErrorText = "<content:getAttribute beanName='rolloverContributionsErrorText' attribute='text'  filter='true' escapeJavaScript="true"/>"; 
   var acknowledgeCheckboxErrorText = "<content:getAttribute beanName='acknowledgeCheckboxErrorText' attribute='text'  filter='true' escapeJavaScript="true"/>"; 
   var automaticEnrollmentSecErrorText = "<content:getAttribute beanName='automaticEnrollmentSecErrorText' attribute='text'  filter='true' escapeJavaScript="true"/>"; 
   var autoContributionIncSecErrorText = "<content:getAttribute beanName='autoContributionIncSecErrorText' attribute='text'  filter='true' escapeJavaScript="true"/>";    
   var cancelAndExitMessage = "<content:getAttribute beanName='cancelAndExitMessage' attribute='text'  filter='true' escapeJavaScript="true"/>";  
   var emptyPlanYearEndMessage = "<content:getAttribute beanName='emptyPlanYearEndMessage' attribute='text'  filter='true' escapeJavaScript="true"/>";
   <%-- Added as part of DR-346147 --%>
   var employerTaxIdentificationNumberandIRSNumberMessage = "<content:getAttribute beanName='employerTaxIdentificationNumberandIRSNumberMessage' attribute='text'  filter='true' escapeJavaScript="true"/>";
   var employerTaxIdentificationNumberMessage = "<content:getAttribute beanName='employerTaxIdentificationNumberMessage' attribute='text'  filter='true' escapeJavaScript="true"/>";
   var IRSNumberMessage = "<content:getAttribute beanName='IRSNumberMessage' attribute='text'  filter='true' escapeJavaScript="true"/>";

/**
 * Function that handles click of the save button.
 */
 function handleSaveClicked() {
	var result;
	<%-- Validate the Rollover Contribution --%>
	var rolloverContributionsPermitted = $("input[name='planInfoVO.pifMoneyType.rolloverContributionsPermitted']:checked").val();
	if (rolloverContributionsPermitted == "${planDataConstants.YES_CODE}") {
		result = ValidateRolloverContributions();
		if(!result){
			return false;
		}		
	}
	
	<%-- Validate the Automatic Enrollment --%>
	result = validateAutomaticEnrollment();
	if(!result){
		return false;
	}	
	
	<%-- Validate the Employer Tax Identification Number --%>
	<%-- Added as part of DR-346147 --%>
	result = checkEmployerTaxIdentificationNumber();
	if(!result){
		return false;
	}

	<%-- Validate the Employee Deferral Election --%>
	result = validateEmployeeDeferralElection();
	if(!result){
		return false;
	}		
	
	<%-- Validate the Automatic Contribution Increases --%>
	result = validateAutomaticContributionIncreases();
	if(!result){
		return false;
	}

	<%-- Validate the Contribution Formulas Rule types--%>
	result = validateMoneyTypeForRuleTypes();
	if(!result){
		return false;
	}	
	
	<%-- Validate the Contribution Formula Section Types--%>
	result = validateContributionFormulaSectionTypes();
	if(!result){
		return false;
	}	
	
	encodeRuleSets();
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
 }
 
/**
 * Function that handles click of the cancel button.
 */  
 function handleCancelClicked() {
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }
    
    <%-- Check user confirmation --%>
    var confirmResponse = confirmDiscardChanges(cancelAndExitMessage);
    if (!confirmResponse) {
      resetSubmitInProgress();
    }
  
    return confirmResponse;
 }
 
/**
 * Function that handles click of the submit button.
 */
 function handleSubmitClicked() {
	
	 <%-- validate the acknowledge disclaimer value.--%>
		planInfoVOAuthorizationIndicatorElement = document.getElementById('planInfoVO_authorizationIndicator');
		if(!$(planInfoVOAuthorizationIndicatorElement).is(':checked')){ 
			alert(acknowledgeCheckboxErrorText);
			return false;
		}
	
	var result;	
	<%-- Validate the Rollover Contribution --%>
	var rolloverContributionsPermitted = $("input[name='planInfoVO.pifMoneyType.rolloverContributionsPermitted']:checked").val();
	if (rolloverContributionsPermitted == "${planDataConstants.YES_CODE}") {
		result = ValidateRolloverContributions();
		if(!result){
			return false;
		}		
	}
	
	<%-- Validate the Plan year End date --%>
	result = checkEmptyPlanYearEndDate();
	if(!result){
		return false;
	}
	
	<%-- Validate the Employer Tax Identification Number --%>
	<%-- Added as part of DR-346147 --%>
	result = checkEmployerTaxIdentificationNumber();
	if(!result){
		return false;
	}
	
	<%-- Validate the Automatic Enrollment --%>
	result = validateAutomaticEnrollment();
	if(!result){
		return false;
	}

	<%-- Validate the Employee Deferral Election --%>
	result = validateEmployeeDeferralElection();
	if(!result){
		return false;
	}	
	
	<%-- Validate the Automatic Contribution Increases --%>
	result = validateAutomaticContributionIncreases();
	if(!result){
		return false;
	}

	<%-- Validate the Contribution Formulas Rule types--%>
	result = validateMoneyTypeForRuleTypes();
	if(!result){
		return false;
	}

	<%-- Validate the Contribution Formula Section Types--%>
	result = validateContributionFormulaSectionTypes();
	if(!result){
		return false;
	}		
 
	encodeRuleSets();
	
	
	
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }
    
 } 

var einOnRecord = '${pifDataUi.employerTaxIdentificationNumber}';
 /**
  * Function that handles blur of the employer tax id number .  The dirty check had to be moved here with 
  * validation because the autotab feature was hiding the onchange event. 
  */
  function handleEinBlur(field) {
    if (field.value != einOnRecord) {
      setDirtyFlag();
    }
    validateEin(field);
  }
  

 
/**
 * Handles the change of the plan name.
 *
 * @field The field that changed.
 */
 function handlePlanNameChanged(field) {
   setDirtyFlag();
   field.value = trim(field.value);
 } 

var oldDefaultUnvestedMoneyOption = '${defaultOption}';
var oldDefaultUnvestedMoneyOptionIndex = getCheckedIndex(document.getElementsByName('planInfoVO.forfeitures.defaultOption'));
/**
 * Function that handles change of a selected unvested money option.
 */
 function handleSelectedUnvestedMoneyOptionClicked(field, index) {
 
   setDirtyFlag();
  
   <%-- Get radio button we will be changing --%>
   var radio = document.getElementById('unvestedMoneyOptionDefault[' + index + ']');
   
   <%-- If field was checked, enable radio button --%>
   if (field.checked) {
     radio.disabled=false;
   } else {
     <%-- Field was unchecked, disable radio button --%>
     radio.disabled=true;
   }
 }

/**
 * Function that handles change of a default unvested money option.
 */
function handleDefaultUnvestedMoneyOptionClicked(field, index) {
  <%-- Radio buttons must use on click to capture changed value - so we need to filter out situations where user clicks on selected value --%>
  var newValue = getCheckedValue(document.getElementsByName('planInfoVO.forfeitures.defaultOption'));

  if (newValue != oldDefaultUnvestedMoneyOption) {
    setDirtyFlag();    
    <%-- Need to disable new checkbox and enable oldcheckbox --%>
    var oldCheckBoxField = document.getElementById('unvestedMoneyOption[' + oldDefaultUnvestedMoneyOptionIndex + ']');
    var newCheckBoxField = document.getElementById('unvestedMoneyOption[' + index + ']');

    oldCheckBoxField.disabled=false;
    newCheckBoxField.disabled=true;
    
    <%-- Update tracking fields --%>
    oldDefaultUnvestedMoneyOption = newValue;
    oldDefaultUnvestedMoneyOptionIndex = index;
    
    <%-- Update default checkbox (default selected is disabled so we have to track it this way --%>
    document.getElementById('defaultUnvestedMoneyOptionId').value = newValue;
  }
}

/**
 * Handler for calendar window setup
 */ 
function handleCalendarSetup(field, event) {

   Calendar = new calendar(field);
   Calendar.year_scroll = true;
   Calendar.time_comp = false;
   
   // Modify calendar position to be slightly above and to right of mouse click
   yPosition = event.screenY - 150;
   xPosition = event.screenX + 80;
   
   Calendar.popup();
   field.select();
}

/**
 * Generic handler for date icon clicked
 */ 
 function handleDateIconClicked(evt, fieldId) {

    setDirtyFlag();

    // Retrieve the field
    var field = document.getElementById(fieldId);

    // Pre-Validate date and blank if not valid
    if (!validateMMddYYYY(field, false)) {
      field.value = '';
    }
    
    // Popup calendar
    handleCalendarSetup(field, evt);
 }
 
/**
 * DHTML date validation script. Courtesy of SmartWebby.com (http://www.smartwebby.com/dhtml/)
 */
// Declaring valid date character, minimum year and maximum year
var dtCh= "/";
var minYear=1800;
var maxYear=2100;

function isInteger(s){
  var i;
    for (i = 0; i < s.length; i++){   
        // Check that current character is number.
        var c = s.charAt(i);
        if (((c < "0") || (c > "9"))) return false;
    }
    // All characters are numbers.
    return true;
}

function stripCharsInBag(s, bag){
  var i;
    var returnString = "";
    // Search through string's characters one by one.
    // If character is not in bag, append to returnString.
    for (i = 0; i < s.length; i++){   
        var c = s.charAt(i);
        if (bag.indexOf(c) == -1) returnString += c;
    }
    return returnString;
}


function daysInFebruary (year){
  // February has 29 days in any year evenly divisible by four,
    // EXCEPT for centurial years which are not also divisible by 400.
    return (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
}
function DaysArray(n) {
  for (var i = 1; i <= n; i++) {
    this[i] = 31
    if (i==4 || i==6 || i==9 || i==11) {this[i] = 30}
    if (i==2) {this[i] = 29}
   } 
   return this
}
function isDate(dtStr, showAlert){
  var daysInMonth = DaysArray(12)
  var pos1=dtStr.indexOf(dtCh)
  var pos2=dtStr.indexOf(dtCh,pos1+1)
  var strMonth=dtStr.substring(0,pos1)
  var strDay=dtStr.substring(pos1+1,pos2)
  var strYear=dtStr.substring(pos2+1)
  strYr=strYear
  if (strDay.charAt(0)=="0" && strDay.length>1) strDay=strDay.substring(1)
  if (strMonth.charAt(0)=="0" && strMonth.length>1) strMonth=strMonth.substring(1)
  for (var i = 1; i <= 3; i++) {
    if (strYr.charAt(0)=="0" && strYr.length>1) strYr=strYr.substring(1)
  }
  month=parseInt(strMonth)
  day=parseInt(strDay)
  year=parseInt(strYr)
  if (pos1==-1 || pos2==-1){
    if (showAlert) {
      alert("The date format should be : mm/dd/yyyy")
    }
    return false
  }
  if (strMonth.length<1 || month<1 || month>12){
    if (showAlert) {
      alert("Please enter a valid month")
    }
    return false
  }
  if (strDay.length<1 || day<1 || day>31 || (month==2 && day>daysInFebruary(year)) || day > daysInMonth[month]){
    if (showAlert) {
      alert("Please enter a valid day")
    }
    return false
  }
  if (strYear.length != 4 || year==0 || year<minYear || year>maxYear){
    if (showAlert) {
      alert("Please enter a valid 4 digit year")
    }
    return false
  }
  if (dtStr.indexOf(dtCh,pos2+1)!=-1 || isInteger(stripCharsInBag(dtStr, dtCh))==false){
    if (showAlert) {
      alert("Please enter a valid date")
    }
    return false
  }
return true
}

function validateMMddYYYY(field, showAlert){

  if (isDefined(field) && hasContent(field)) {
    return isDate(field.value, showAlert);
  } else {
    return true;
  }
}

// Checks if an object is defined or not
function isDefined(object) {
   varToStr=eval("' " + object + "'");
   if (varToStr == " undefined") {
     return false;
   }
   else {
     return true;
   }
 } // end function

// Queries if the specified field has content
function hasContent(field) {
   if ( field != null ) {
      if ( field.value != null ) {
         if ( field.value.length > 0 ) {
            return true;
         }
      }
   }

   return false;
} // end function

// set the radio button with the given value as being checked
// do nothing if there are no radio buttons
// if the given value does not exist, all the radio buttons
// are reset to unchecked
function setCheckedValue(radioObj, newValue) {
  if (!radioObj) return;
  var radioLength = radioObj.length;
  if (radioLength == undefined) {
    radioObj.checked = (radioObj.value == newValue.toString());
    return;
  }

  for (var i = 0; i < radioLength; i++) {
    radioObj[i].checked = false;
    if (radioObj[i].value == newValue.toString())
        radioObj[i].checked = true;
  }
}
 
// return the value of the radio button that is checked
// return an empty string if none are checked, or
// there are no radio buttons
function getCheckedValue(radioObj) {

  if (!radioObj) return "";
  var radioLength = radioObj.length;
  if (radioLength == undefined) {
    if (radioObj.checked) return radioObj.value;
    else return "";
  }
  for(var i = 0; i < radioLength; i++) {
    if(radioObj[i].checked) return radioObj[i].value;
  }
  return "";
}

// return the index of the radio button that is checked
// return an empty string if none are checked, or
// there are no radio buttons
function getCheckedIndex(radioObj) {

  if (!radioObj) {
    return "";
  }
  var radioLength = radioObj.length;
  if (radioLength == undefined) {
    if (radioObj.checked) {
      return 0;
    } else {
      return "";
    }
  }
  for(var i = 0; i < radioLength; i++) {
    if(radioObj[i].checked) {
      return i;
    }
  }
  return "";
}

/**
 * Handles the change of a vesting schedule which can result in value changes in the text field 
 * as well as disabling of the text field.
 *
 * @rowIndex The index of the row that was changed.
 */
 function handleVestingScheduleChanged(scheduleSelect, rowIndex) {
   setDirtyFlag();
   var newSchedule = scheduleSelect.options[scheduleSelect.selectedIndex].value;

   for (var i = 0; i <= ${scheduleConstants.YEARS_OF_SERVICE}; i++) {
     <%-- Get text field and hidden fields --%>
     var textField = document.getElementById('vestedAmount[' + rowIndex + '][' + i + ']TextId');
     var hiddenField = document.getElementById('vestedAmount[' + rowIndex + '][' + i + ']HiddenId');   

     var defaultPercent;
     var key = 'Row' + rowIndex + 'Year' + i;
     if ((newSchedule == '${scheduleConstants.VESTING_SCHEDULE_CUSTOMIZED}') && (isDefined(vestingScheduleArray[key]))) {
       defaultPercent = vestingScheduleArray[key].percent;
     } else if((newSchedule == '${scheduleConstants.VESTING_SCHEDULE_FULLY_VESTED}') && (isDefined(vestingScheduleArray[key]))){
	   //defaultPercent = vestingScheduleArray[key].percent;
	     defaultPercent = vestingScheduleArray['' + newSchedule + i].percent;
	 }else {
       defaultPercent = vestingScheduleArray['' + newSchedule + i].percent;
     } 
     
     textField.value = defaultPercent;
     hiddenField.value = defaultPercent;
     <%-- Only customized schedules are editable --%>
     if (i < ${scheduleConstants.YEARS_OF_SERVICE}) {
       if (newSchedule == '${scheduleConstants.VESTING_SCHEDULE_CUSTOMIZED}') {
         enableNode(textField);
         disableNode(hiddenField);
       }else {
         enableNode(hiddenField);
         disableNode(textField);
       }    
     }
   }
 }
<%-- TODO : Need to create the property hoursOfService in PIVO
var hoursOfServiceOnRecord = '${pifDataForm.planInfoVO.eligibility.eligibilityRequirements.hoursOfService}';
var vestingMethodOnRecord = '${pifDataForm.planInfoVO.eligibility.eligibilityRequirements.serviceCreditingMethod}';
--%>
var hoursOfServiceOnRecord = '';
var vestingMethodOnRecord = '';
 /**
  * Handles the click of the vesting service credit method radio buttons.  
  *
  * @param value The new selected value.
  */
 function handleVestingServiceCreditMethodClicked(value) {
 
   <%-- Check dirty flag --%>
   if (value != vestingMethodOnRecord) {
     setDirtyFlag();
   }
   var input = document.getElementById('hoursOfServiceTextId');
   var hidden = document.getElementById('hoursOfServiceHiddenId');
   if (value == '${planDataConstants.VESTING_SERVICE_CREDIT_METHOD_HOURS_OF_SERVICE}') {
     <%-- Hours of service required - enable textfield, disable backing field and switch to value on record 
     input.value = (hoursOfServiceOnRecord == '') ? '${planDataConstants.HOURS_OF_SERVICE_DEFAULT}' : hoursOfServiceOnRecord; --%>
     input.disabled = false;
     hidden.disabled = true;
   } else {
     <%-- Hours of service not required - disable textfield, enable backing field and blank value --%>
     input.value = '';
     input.disabled=true;
     hidden.disabled=false;
     hidden.value = '';
   }
 } 
 
 var spousalConsentOnRecord = "${e:forJavaScriptBlock(pifDataForm.planInfoVO.withdrawals.requiresSpousalConsentForDistributions)}";
 /**
  * Handles the click of the spousal consent radio buttons.  This is primarily to handle keeping the
  * read only portion of the field (in the loans section) up to date.
  *
  * @param display The new display value.
  */
 function handleRequiresSpousalConsentForDistributionsClicked(value) {
   <%-- Check dirty flag --%>
   if (value != spousalConsentOnRecord) {
     setDirtyFlag();
   }
	var requireSpousalConsentHiddenId = document.getElementById('loansRequireSpousalConsentHidden');
	if (value == '${planDataConstants.YES_CODE}') {
		 requireSpousalConsentHiddenId.value='Y';
	}else if (value == '${planDataConstants.NO_CODE}'){
		 requireSpousalConsentHiddenId.value='N';	
	}		 

 } 
 
 var hardshipProvisionOnRecord = '${pifDataForm.planInfoVO.withdrawals.hardshipWithdrawalsBase}';
 /**
  * Handles the click of the hardship withdrawal provision radio buttons.
  *
  * @param value The new value.
  */
 function handleHardshipProvisionClicked(value) {
   <%-- Check dirty flag --%>
   if (value != hardshipProvisionOnRecord) {
     setDirtyFlag();
   }
 }

 var eedefEarningsAllowedIndOnRecord = '${pifDataForm.planInfoVO.withdrawals.eedefEarningsAllowedInd}';
 /**
  * Handles the click of the eedefEarningsAllowedInd radio buttons.
  *
  * @param value The new value.
  */
 function handleEedefEarningsAllowedIndClicked(value) {
   <%-- Check dirty flag --%>
   if (value != eedefEarningsAllowedIndOnRecord) {
     setDirtyFlag();
   }
 }


 var preRetirementWithdrawalsAge = '${pifDataForm.planInfoVO.withdrawals.preRetirementWithdrawalsAge}';
  /**
  * Handles the click of the Allow PreRetirement Withdrawals radio buttons.
  *
  * @param value The new value.
  */
 function handleAllowPreRetirementWithdrawalsClicked(value) {

   var input = document.getElementById('preRetirementWithdrawalsId');
   var hidden = document.getElementById('preRetirementWithdrawalsAgeId');
   var hiddenId = document.getElementById('preRetirementWithdrawalsAgeHidden');   
   if (value == 'true') {
     <%-- Pre Retirement Withdrawals Age required - enable textfield and switch to value on record --%>
     hidden.disabled = false;
	 hidden.value = (preRetirementWithdrawalsAge == '') ? '' : preRetirementWithdrawalsAge;
	 hiddenId.value=(preRetirementWithdrawalsAge == '') ? '' : preRetirementWithdrawalsAge;
   } else {
     <%-- Pre Retirement Withdrawals Age not required - disable textfield and blank value --%>
	 hidden.value = '';
     hidden.disabled=true;
	 hiddenId.value='';
   }
 } 
 
 var minimumHardshipAmountValue = '${pifDataForm.planInfoVO.withdrawals.minimumHardshipAmount}';
 var maximumHardshipAmountValue = '${pifDataForm.planInfoVO.withdrawals.maximumHardshipAmount}';
  /**
  * Handles the click of the Allow Hardship Withdrawals radio buttons.
  *
  * @param allowHardship The new value.
  */
 function handleAllowHardshipWithdrawalsClicked(allowHardship) {
   <%-- Check dirty flag --%>
   var input = document.getElementById('allowHardshipWithdrawalsId');
   var hardshipWithdrawalProvisionsFC = document.getElementById('hardshipWithdrawalProvisionsFactsCircumstancesId');
   var hardshipWithdrawalProvisionsSH = document.getElementById('hardshipWithdrawalProvisionsSafeHarborId');
   var hardshipWithdrawalProvisionsHidden = document.getElementById('hardshipWithdrawalProvisionsHiddenId');
   var eedefEarningsAllowedIndIdYes = document.getElementById('eedefEarningsAllowedIndIdYes');
   var eedefEarningsAllowedIndIdNo = document.getElementById('eedefEarningsAllowedIndIdNo');   
   var eedefEarningsAllowedIndHidden = document.getElementById('eedefEarningsAllowedIndHiddenId');
   
   var minimumHardshipAmount = document.getElementById('minimumHardshipAmountId');
   var maximumHardshipAmount = document.getElementById('maximumHardshipAmountId');
   var minimumHardshipAmountHidden = document.getElementById('minimumHardshipAmountHiddenId');
   var maximumHardshipAmountHidden = document.getElementById('maximumHardshipAmountHiddenId');   
   
   if (allowHardship == 'true') {
     <%--  Hardship Provisions required - enable textfield and switch to value on record --%>
     hardshipWithdrawalProvisionsFC.disabled = false;
	 hardshipWithdrawalProvisionsFC.value = '${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_FACTS_AND_CIRCUMSTANCES}';
     hardshipWithdrawalProvisionsSH.disabled = false;
	 hardshipWithdrawalProvisionsSH.value = '${planDataConstants.HARDSHIP_WITHDRAWAL_PROVISION_SAFE_HARBOR}';
	 eedefEarningsAllowedIndIdYes.disabled = false;
	 eedefEarningsAllowedIndIdNo.disabled = false;
	 eedefEarningsAllowedIndIdYes.value ='${planDataConstants.YES_CODE}';
	 eedefEarningsAllowedIndIdNo.value = '${planDataConstants.NO_CODE}';	 
	
     minimumHardshipAmount.disabled = false;
	 minimumHardshipAmount.value = (minimumHardshipAmountValue == '') ? '' : minimumHardshipAmountValue;
	 maximumHardshipAmount.disabled = false;
	 maximumHardshipAmount.value = (maximumHardshipAmountValue == '') ? '' : maximumHardshipAmountValue;
	// eedefEarningsAllowedInd.value = (eedefEarningsAllowedIndOnRecord == '') ? '' : eedefEarningsAllowedIndOnRecord;
	// sixMonthsContributionSuspendedCode.value = (sixMonthsContributionSuspendedCodeOnRecord == '') ? '' : sixMonthsContributionSuspendedCodeOnRecord;
   } else {
     <%-- Hardship Provisions not required - disable textfield and blank value --%>
	 hardshipWithdrawalProvisionsFC.checked = false;
	 hardshipWithdrawalProvisionsFC.value = '';
	 hardshipWithdrawalProvisionsHidden.value = 'U';	 
     hardshipWithdrawalProvisionsFC.disabled=true;
	 hardshipWithdrawalProvisionsSH.checked = false;
	 hardshipWithdrawalProvisionsSH.value = '';
     hardshipWithdrawalProvisionsSH.disabled=true;
     eedefEarningsAllowedIndIdYes.disabled = true;
     eedefEarningsAllowedIndIdYes.checked=false;
     eedefEarningsAllowedIndIdNo.disabled = true;
     eedefEarningsAllowedIndIdNo.checked=false;
     eedefEarningsAllowedIndHidden.value='U';     
    // eedefEarnIllegalAccessError.value = '';
	 minimumHardshipAmount.value = '';
	 minimumHardshipAmountHidden.value='';
     minimumHardshipAmount.disabled=true;
	 maximumHardshipAmount.value = '';
	 maximumHardshipAmountHidden.value='';
     maximumHardshipAmount.disabled=true;

   }
 
   <c:if test="${pifDataForm.editMode or pifDataForm.confirmMode}">
<c:forEach items="${pifDataForm.planInfoVO.withdrawals.allowedMoneyTypesForHardship}" var="allowableMoneyType" varStatus="count" >
<c:if test="${allowableMoneyType.selectedMoneyType ==true}">
			var selectedIndicatorValue = '${allowableMoneyType.selectedIndicator}';
			var selIndicatorInput = document.getElementById('pifDataUi_planInfoVO_withdrawals_allowedMoneyTypesForHardship[${count.index}]_selectedIndicator');
			var selIndicatorHiddenInput = document.getElementById('withdrawals_allowedMoneyTypes_selectedIndicator${count.index}');			
   			if (allowHardship == 'true') {
     				<%-- Allow Withdrawals - enable textfield, disable backing field and switch to value on record --%>
     			selIndicatorHiddenInput.value = (selectedIndicatorValue == '') ? 'N' : selectedIndicatorValue;
				if(selectedIndicatorValue == '${planDataConstants.YES_CODE}'){selIndicatorInput.checked='checked';}else{selIndicatorInput.checked='';}
				selIndicatorInput.disabled = false;
			}else{
     			 selIndicatorHiddenInput.value = 'N';
				selIndicatorInput.checked='';
     			selIndicatorInput.disabled=true;
			}
					
</c:if>
</c:forEach>
   </c:if>   
 } 

  /**
  * Handles the Rollover Contributions Money types validation on tab navigation and save/submit.
  *
  */ 
function ValidateRolloverContributions() {
	var selectedRolloverMTIndicator = false;
<c:forEach items="${pifDataForm.planInfoVO.pifMoneyType.permittedRolloverMoneyTypes}" var="rolloverMT" varStatus="count" >

		rolloverMoneyTypeSelected = $("input[name='planInfoVO.pifMoneyType.permittedRolloverMoneyTypes[${count.index}].selectedIndicator']:checked").val();
		if (rolloverMoneyTypeSelected == "true") {
			selectedRolloverMTIndicator = true;		
		}			
</c:forEach>
	if(!selectedRolloverMTIndicator){
		alert(rolloverContributionsErrorText);
		return false;
	}	
	return true;
} 
  /**
  * Handles the Employee Deferral Election validation on tab navigation and save/submit.
  *
  */ 
function validateEmployeeDeferralElection(){
	var employeeDeferralElectionSelectId = "#employeeDeferralElectionCode";
   	var value = $(employeeDeferralElectionSelectId).val();
 	if (value == "O") {
		var result = validateSalaryDeferalElectionDay();
		if(!result){
			return false;
		}
		result = validateSalaryDeferalElectionMonth();
		if(!result){
			return false;
		}		
	}
	return true;
}

/**
 * Handles the Automatic Contribution Increases validation on tab navigation and save/submit.
 * CMA Key: 81257 - All fields in the Automatic Contribution Increases section must be specified if the plan provides for automatic contribution increases.
 */ 
function validateAutomaticContributionIncreases(){
	if($("input[name='planInfoVO.contributions.aciAllowed']:checked").val() == "${planDataConstants.YES_CODE}" ) {

		var result = validateAppliesTo();
		if(!result){
			return false;
		}
		result = validateAnnualIncreasesAppliedDate();
		if(!result){
			return false;
		}
		var result = validateDateWithACIDate();
		if(!result){
			return false;
		}	    
		result = validateDefaultAnnualInc();
		if(!result){
			return false;
		}	
		result = validateDefaultMaxAutoIncreases();
		if(!result){
			return false;
		}		
	}
	return true;
}

function validateAppliesTo() {

	if ( $("input[name='planInfoVO.contributions.planAutoContributionIncrease.appliesTo']:checked").length <= 0 ) {
		//alert("Enter the value for Applies to field..");
		alert(autoContributionIncSecErrorText);
        return false;
	} else {
        return true;
	}   
}

function validateAnnualIncreasesAppliedDate() {
	
	var selectedACIAppliedDate =  $("input[name='pifDataUi.aciApplyDate']").val();
    if (selectedACIAppliedDate == '') {
		//alert("Enter the value for aci Applies Date field..");
		alert(autoContributionIncSecErrorText);		
        return false;
	} else {
        return true;
	}	
}


function checkEmptyPlanYearEndDate() {
		
	var selectedPlanYearEndDate =  $("input[name='pifDataUi.planYearEndString']").val();
   if (selectedPlanYearEndDate == '') {
		alert(emptyPlanYearEndMessage);		
        return false;
	} else {
        return true;
	}	
}
<%-- Added as part of DR-346147 --%>
function checkEmployerTaxIdentificationNumber(){
	var selectedEmployerTaxIdentificationNumber =  $("input[name='pifDataUi.employerTaxIdentificationNumber']").val();
	var selectedirsPlanNumberNumber =  $("input[name='planInfoVO.generalInformations.irsPlanNumber']").val();
	
    if (selectedEmployerTaxIdentificationNumber == '' && selectedirsPlanNumberNumber == '') {
		alert(employerTaxIdentificationNumberandIRSNumberMessage);		
        return false;
	} 
    else if(selectedEmployerTaxIdentificationNumber == '') {
    	alert(employerTaxIdentificationNumberMessage)
    	return false;
	}
    else if(selectedirsPlanNumberNumber == ''){
    	alert(IRSNumberMessage)
    	return false;
    }
   else {
    	return true;
    }
}

function validateDefaultAnnualInc () {
	
	var selectedDefaultAnnualIncrease  =  $("input[name='pifDataUi.annualIncrease']").val();
    if (selectedDefaultAnnualIncrease == '') {
		//alert("Enter the value for Default Annual Increase field..");
		alert(autoContributionIncSecErrorText);		
        return false;
	} else {
        return true;
	}	
}
 
function validateDefaultMaxAutoIncreases () {
	
	var selectedDefaultMaxAutoIncreases  =  $("input[name='pifDataUi.maxAutomaticIncrease']").val();
    if (selectedDefaultMaxAutoIncreases == '') {
		//alert("Enter the value for Default maximum for automatic increases field..");
		alert(autoContributionIncSecErrorText);		
        return false;
	} else {
        return true;
	}	
} 

/**
 * Handles the Automatic Enrollment validation on tab navigation and save/submit.
 * CMA Key: 81256 - All fields in the Automatic Enrollment section must be specified if the plan provides for automatic enrollment.
 */ 
function validateAutomaticEnrollment(){
	if($("input[name='planInfoVO.eligibility.isAutomaticEnrollmentAllowed']:checked").val() == "${planDataConstants.YES_CODE}" ) {
		if($("input[name='pifDataUi.automaticEnrollmentDate']").val() == '' ){
			//alert("Enter the value for Automatic Enrollment Date.");
			alert(automaticEnrollmentSecErrorText);
			return false;
		}else if(!$("input[name='planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed'][value='${planDataConstants.YES_CODE}']").is(":checked") &&
			!$("input[name='planInfoVO.eligibility.isAutomaticContributionsWithdrawalsAllowed'][value='${planDataConstants.NO_CODE}']").is(":checked")){
			//alert("Enter the value for Automatic Contributions Withdrawals Allowed.");
			alert(automaticEnrollmentSecErrorText);
			return false;
		}else if($("input[name='pifDataUi.deferralPercentageForAutomaticEnrollment']").val() == ''){
			//alert("Enter the value for deferral Percentage For Automatic Enrollment.");
			alert(automaticEnrollmentSecErrorText);
			return false;	
		}
	}
	return true;
}

function validateForInteger(field) { 
   var value = field.value;
   if (value.length == 0) {
      return true;
   }
   var valid = isAllDigits(value);
   if (valid) {
       var v = parseInt(value);
       if (isNaN(v)) {
          return false;
       } else {
         return true;
       }
   } else {
	  field.value="";
	  field.select();
      return false;
   }
}

</script>
