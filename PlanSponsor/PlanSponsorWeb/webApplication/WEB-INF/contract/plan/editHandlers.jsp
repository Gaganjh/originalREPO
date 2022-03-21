<%@page buffer="none" autoFlush="true" isErrorPage="false" %>
<jsp:include flush="true" page="commonHandlers.jsp"></jsp:include>

<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib uri="/WEB-INF/java-encoder-advanced.tld" prefix="e" %>

<un:useConstants var="scheduleConstants" className="com.manulife.pension.service.contract.valueobject.VestingSchedule" />
<un:useConstants var="planDataConstants" className="com.manulife.pension.service.contract.valueobject.PlanData" />

<%-- Defines page level event handlers --%>
<script type="text/javascript">
/**
 * Function that handles click of the save button.
 */
 function handleSaveClicked() {
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
    var confirmResponse = confirmDiscardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');
    if (!confirmResponse) {
      resetSubmitInProgress();
    }
  
    return confirmResponse;
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

/**
 * Handles the change of the entity type other description.
 *
 * @field The field that changed.
 */
 function handleEntityTypeOtherDescriptionChanged(field) {
   setDirtyFlag();
   field.value = trim(field.value);
 }


var ein1OnRecord = '${planDataForm.planDataUi.employerTaxIdentificationNumberPart1}';
/**
 * Function that handles blur of the employer tax id number part 1.  The dirty check had to be moved here with 
 * validation because the autotab feature was hiding the onchange event. 
 */
 function handleEin1Blur(field) {
   if (field.value != ein1OnRecord) {
     setDirtyFlag();
   }
   validateEin1(field);
 }

var payrollFrequencyOnRecord = '${planDataForm.planDataUi.planData.payrollFrequency}';
/**
 * Function that handles change of a selected payroll frequency.
 */
 function handlePayrollFrequencyClicked(value) {
   <%-- Check dirty flag --%>
   if (value != payrollFrequencyOnRecord) {
     setDirtyFlag();
   }
 } 
 
var isSafeHarborPlanOnRecord = "${e:forJavaScriptBlock(planDataForm.planDataUi.planData.isSafeHarborPlan)}";
/**
 * Function that handles change of a selected is safe harbor plan.
 */
 function handleIsSafeHarborPlanClicked(value) {
   <%-- Check dirty flag --%>
   if (value != isSafeHarborPlanOnRecord) {
     setDirtyFlag();
   }
 } 

var oldDefaultUnvestedMoneyOption = "${e:forJavaScriptBlock(planDataForm.planDataUi.planData.defaultUnvestedMoneyOption)}";
var oldDefaultUnvestedMoneyOptionIndex = getCheckedIndex(document.getElementsByName('planDataUi.planData.defaultUnvestedMoneyOption'));
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
  var newValue = getCheckedValue(document.getElementsByName('planDataUi.planData.defaultUnvestedMoneyOption'));
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
     } else {
       defaultPercent = vestingScheduleArray['' + newSchedule + i].percent;
     } 
     
     textField.value = defaultPercent;
     hiddenField.value = defaultPercent;
     
     <%-- Only customized schedules are editable --%>
     if (i < ${scheduleConstants.YEARS_OF_SERVICE}) {
       if (newSchedule == '${scheduleConstants.VESTING_SCHEDULE_CUSTOMIZED}') {
         enableNode(textField);
         disableNode(hiddenField);
       } else {
         enableNode(hiddenField);
         disableNode(textField);
       }    
     }
   }
 }
 
var spousalConsentOnRecord = "${e:forJavaScriptBlock(planDataForm.planDataUi.planData.requiresSpousalConsentForDistributions)}";
var eedefEarningsAllowedIndOnRecord = '${planDataForm.planDataUi.planData.eedefEarningsAllowedInd}';
 /**
  * Handles the click of the spousal consent radio buttons.  This is primarily to handle keeping the
  * read only portion of the field (in the loans section) up to date.
  *
  * @param display The new display value.
  */
 function handleRequiresSpousalConsentForDistributionsClicked(value, display) {
   <%-- Check dirty flag --%>
   if (value != spousalConsentOnRecord) {
     setDirtyFlag();
   }
  document.getElementById('loanSpousalConsentSpanId').innerHTML = display;
 } 

 function handleRequiresEedefEarningsAllowedIndOnRecordClicked(value) {
	   <%-- Check dirty flag --%>
	   if (value != eedefEarningsAllowedIndOnRecord) {
	     setDirtyFlag();
	   }	 
	 } 

 
var hardshipProvisionOnRecord = "${e:forJavaScriptBlock(planDataForm.planDataUi.planData.hardshipWithdrawalProvisions)}";
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

 
var hoursOfServiceOnRecord = '${planDataForm.planDataUi.planData.hoursOfService}';
var vestingMethodOnRecord = "${e:forJavaScriptBlock(planDataForm.planDataUi.planData.vestingServiceCreditMethod)}";
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
     <%-- Hours of service required - enable textfield, disable backing field and switch to value on record --%>
     input.value = (hoursOfServiceOnRecord == '') ? '${planDataConstants.HOURS_OF_SERVICE_DEFAULT}' : hoursOfServiceOnRecord;
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
</script>