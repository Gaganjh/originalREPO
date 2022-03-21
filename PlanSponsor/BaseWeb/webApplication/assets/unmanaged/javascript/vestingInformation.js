function doConfirmAndSubmit(action) {
    // only there is change, do confirm
    if (isFormChanged()) {
	    if (confirm('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.')) {
	       doSubmit(action);
	       return true;
	    } else {
	       return false;
	    }
	 } else {
        doSubmit(action);
	    return true;
	 }
}

function doHowToPrintFriendly(key,ind) {
   location.href = "/do/contentpages/howTo?printFriendly=true&contentKey=" + key + "&ind=" + ind;
}

function doSubmit(action) {
    doFormSubmit('vestingInformationForm', action);
}

function doFormSubmit(frm, action) {
	if (action == "save") {
		if (!validateFullyVestedEffDateIndDiscrepancy()) {
			alert(fullyVestedEffDateIndDiscrepancy);
			document.getElementById('fullyVestedEffectiveDate').focus();
			return false;
		} 
		validateVYOSValues();
	}
	document.forms[frm].elements['action'].value = action;
    document.forms[frm].submit();
}

function fullyVestedChanged(value) {
    var fullyVestedEffDate = document.getElementById('fullyVestedEffDate');
	if (typeof fullyVestedEffDate == "undefined") {
		return;
	}
	//alert(value);
	//document.getElementById('fullyVestedEffectiveDate').value = "MM/DD/YYYY";
    if (value == "Y") {
       	fullyVestedEffDate.style.display = "block";
       	//fullyVestedAudit.style.display = "block";
       	// Adds the placeholder text, if blank.
        deactivateFullyVestedEffectiveDate();
    } else {
    	fullyVestedEffDate.style.display = "none";
    	fullyVestedAudit.style.display = "none";
    	// Clears the placeholder text, if set.
        activateFullyVestedEffectiveDate();
    }
}

function doCalendar(fieldName) {
    cal = new calendar(document.forms['vestingInformationForm'].elements[fieldName]);
  	cal.year_scoll = true;
  	cal.time_comp = false;
  	cal.popup();
}

function handleAsOfDateCalendarFocus() {
  // Do nothing.
}

function handleAsOfDateCalendarBlur() {
  // Do nothing.
}

function handleFullyVestedEffectiveDateCalendar() {
  doCalendar('fullyVestedEffectiveDate',0)
}

function handleFullyVestedEffectiveDateFocus() {
  activateFullyVestedEffectiveDate();
}

function handleFullyVestedEffectiveDateBlur() {
  deactivateFullyVestedEffectiveDate();
}

function handleFullyVestedEffectiveDateCalendarFocus() {
  activateFullyVestedEffectiveDate();
}

function handleFullyVestedEffectiveDateCalendarBlur() {
  deactivateFullyVestedEffectiveDate();
  var fullyVestedEffectiveDate = document.getElementById('fullyVestedEffectiveDate');
  if (fullyVestedEffectiveDate == null || typeof fullyVestedEffectiveDate == "undefined") {
    return;
  }
  validateFullyVestedEffDate(fullyVestedEffectiveDate);
}

var valueIfFullyVestedEffectiveDateIsBlank = "MM/DD/YYYY";

function activateFullyVestedEffectiveDate() {
  var fullyVestedEffectiveDate = document.getElementById('fullyVestedEffectiveDate');
  if (fullyVestedEffectiveDate == null || typeof fullyVestedEffectiveDate == "undefined") {
    return;
  }
  if (fullyVestedEffectiveDate.value == valueIfFullyVestedEffectiveDateIsBlank) {
    fullyVestedEffectiveDate.value = '';
  }
}
function deactivateFullyVestedEffectiveDate() {
  var fullyVestedEffectiveDate = document.getElementById('fullyVestedEffectiveDate');
  if (fullyVestedEffectiveDate == null || typeof fullyVestedEffectiveDate == "undefined") {
    return;
  }
  if (fullyVestedEffectiveDate.value == '') {
    fullyVestedEffectiveDate.value = valueIfFullyVestedEffectiveDateIsBlank;
  }
}

function handleRecalculateClicked() {

  var asOfDate = document.getElementById('asOfDateId');
  if (!(asOfDate == null || typeof asOfDate == "undefined")) {
    if (!(validateAsOfDate(asOfDate))) {
      return false;
    }
  }

  doSubmit('default');
  
  return true;
}

function validateAsOfDate(field) {
	return validateField(field, new Array(validateDate, validateMinimumDate), new Array(asOfDateFormatError, minimumAsOfDate), true);
}

function validateFullyVestedEffDate(field) {
    // Return as valid if it's the default value.
    if (field.value == valueIfFullyVestedEffectiveDateIsBlank) {
      return true;
    }
    // Otherwise validate the value.
  	return validateField(field, new Array(validateDate, validateMinimumDate, validateFutureDate), 
  			new Array(fullyVestedEffDateFormatError, minimumFullyVestedEffDate, fullyVestedEffDateInTheFuture), true);	
}

function validateVestedYrsOfService(field) {
	return validateField(field, new Array(validateNumber, validateInteger, validatePYOSRange), 
			new Array(previousYearsOfServiceFormatError, invalidPYOSRange, invalidPYOSRange), true);
}

function validatePYOSRange(value) {
	var v = parseInt(value);
	if (v > 99 || v < 0) {
	    return false;
	} else {
		return true;
	}	
}

function validateMinimumDate(value) {
	var selected_date = Date.parse(value);
	minimumDate = new Date(1900,0,1);
	var minimum_date = Date.parse(minimumDate);
	
	if (selected_date < minimum_date) {
		return false;
	} else {
		return true;
	}
}

function validateFutureDate(value) {
	var selected_date = Date.parse(value);
	//alert("selected_date " + selected_date);
	futurePlanYearEnd = document.forms['vestingInformationForm'].elements['futurePlanYearEnd'].value;
	var future_date = Date.parse(futurePlanYearEnd);
	//alert("future_date " + future_date);
	
	if (selected_date > future_date) {
		return false;
	} else {
		return true;
	}
}

function validateFullyVestedEffDateIndDiscrepancy() {
	var fullyVestedEffectiveDate = document.getElementById('fullyVestedEffectiveDate');
	if (typeof fullyVestedEffectiveDate == "undefined") {
		return true;
	}
	
	var fullyVestedIndChecked = document.forms['vestingInformationForm'].elements['fullyVestedInd'][0].checked;
	
	if (fullyVestedIndChecked == true &&
	     (fullyVestedEffectiveDate.value == "" || fullyVestedEffectiveDate.value == valueIfFullyVestedEffectiveDateIsBlank)
	   ) {
		return false;
	} else {
		return true;
	}
}

function protectLinksFromCancel() {
		
		var hrefs  = document.links;
		if (hrefs != null)
		{
			for (i=0; i<hrefs.length; i++) { 
				if(
					hrefs[i].onclick != undefined && 
					(hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
				) {
					// don't replace window open or popups as they won't loose their changes with those
				}
				else if(
					hrefs[i].href != undefined && 
					(hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
				) {
					// don't replace window open or popups as they won't loose their changes with those
				}
				else if(
					hrefs[i].onclick != undefined 
							&& (hrefs[i].onclick.toString().indexOf("toggleSection") != -1) 
			    ) {
						// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				}
				else if(
					hrefs[i].href != undefined 
							&&  ( hrefs[i].href.indexOf("toggleSection") != -1 
                                    || hrefs[i].href.indexOf("javascript:doCalendar") != -1
                                    || hrefs[i].href.indexOf("javascript:handleFullyVestedEffectiveDateCalendar") != -1) 
				) {
						// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				}
				else if(hrefs[i].onclick != undefined) {
					if (hrefs[i].onclick.toString().indexOf("task=download") != -1) {
						hrefs[i].onclick = new Function ("var result = discardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
					} else {
						hrefs[i].onclick = new Function ("var result = discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
					}				
				}
				else {
					if (hrefs[i].href.indexOf("task=download") != -1) {
						hrefs[i].onclick = new Function ("return discardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');");
					} else {
						hrefs[i].onclick = new Function ("return discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
					}
				}
				
			}
		}
}	

/*
 Checks whether any of the prior pye vyos values are not lower than any of the next pye vyos values OR
 if any of the next pye vyos values are less than any of the previous pye vyos values, e.g.
  Case 1:
    previous pye vyos = 1
    current pye vyos = 2
    future pye vyos = 3   => no warning popup
  Case 2:
    previous pye vyos = 1
    current pye vyos = 1
    future pye vyos = 3   => warning popup because previous pye vyos is NOT lower than current (or future) pye vyos
  Case 3:
    previous pye vyos = 2
    current pye vyos = 1
    future pye vyos = 3   => warning popup because current pye vyos is not greater than previous pye vyos
*/
function validateVYOSValues() {
	var vyos = new Array(3);
	var vyosCloned = new Array(3);
	var vyosDate = new Array(3);
	
    var changed = new Array(3);

	for (i=0; i < vyos.length; i++) {
		vyos[i] = document.forms['vestingInformationForm'].elements['vyos[' + i + ']'].value;
		vyosCloned[i] = document.forms['vestingInformationForm'].elements['clonedForm.vyos[' + i + ']'].value;
		
		if (vyos[i] == vyosCloned[i]) {
		    changed[i] = "";
		} else {
		    changed[i] = vyos[i];
		}
	}
	
	vyosTempFunc = new validateChangedVYOSValues(changed, vyos);

    if (vyosTempFunc.subsequentWarn) {
	 	alert(onlyPriorVYOSUpdated);
	} else if (vyosTempFunc.prevWarn){
	 	alert(updatedVYOSLowerThanPriorVYOS);
	}
}

/*
 Validates the vyos values in given args and sets prevWarn/subsequentWarn variables to indicate
 whether the changed values should result in a previous vyos warning or subsequent vyos warning.
 
 param chagnedVyosOnly: contains only the changed vyos values. If a value is not 
      changed, then it will be represented by an empty string at that index
 param vyosEntered: contains all the vyos values as given in vyos fields on the page
*/
function validateChangedVYOSValues(changedVyosOnly, vyosEntered) {
     this.prevWarn = false;
     this.subsequentWarn = false;

    for (idx = 0; idx < changedVyosOnly.length; idx++) {
        if (changedVyosOnly[idx] != "") {
            // Compare changed value with others to see if a warning is to be issued
            if (idx == 0) {
                // Check with 1 and 2
                this.subsequentWarn = ((vyosEntered[idx + 1] != "" && changedVyosOnly[idx] >= vyosEntered[idx + 1]) ||
                    (vyosEntered[idx + 2] != "" && changedVyosOnly[idx] >= vyosEntered[idx + 2]));
                this.prevWarn = false;
            } else if (idx == 1) {
                // Check with 0 and 2
                this.prevWarn = (vyosEntered[idx - 1] != "" && changedVyosOnly[idx] < vyosEntered[idx - 1]);
                this.subsequentWarn = (vyosEntered[idx + 1] != "" && changedVyosOnly[idx] >= vyosEntered[idx + 1]);
            } else if (idx == 2) {
                // Check with 0 and 1
                this.prevWarn = ((vyosEntered[idx - 1] != "" && changedVyosOnly[idx] < vyosEntered[idx - 1]) ||
                    (vyosEntered[idx - 2] != "" && changedVyosOnly[idx] < vyosEntered[idx - 2]));
                this.subsequentWarn = false;
            }
        }
    }
}
