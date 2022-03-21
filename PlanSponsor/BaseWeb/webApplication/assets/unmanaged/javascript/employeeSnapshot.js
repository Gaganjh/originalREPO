var isSubmitted = false;
function isSubmitInProgress() {
	  if (!isSubmitted) {
	    isSubmitted = true;
	    return false;
	  } else {
	    window.status = "Transaction already in progress.  Please wait.";
	    return true;
	  }
	}

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

function doEnableAndSubmit(action) {
	var size = document.getElementById("eligibilityServiceMoneyTypeSize");
	if(size != null && size.value>0 ){
	for (id=0;id<size.value;id=id+1)
		{
			var calculationOverrideField='eligibilityServiceMoneyTypes['+id+'].calculationOverride';
			var calculationOverride=document.getElementsByName(calculationOverrideField);
			calculationOverride[0].disabled=false;	
		}
	}	
	var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }
    doSubmit(action);
}

function doFormSubmit(frm, action) {
	document.forms[frm].elements['action'].value = action;
    document.forms[frm].submit();
}

function expand(sectionId) {
	ele = document.getElementById(sectionId); 
	if (typeof ele == "undefined") {
    	   return;
    }
    if (ele.style.display == "none") {
       ele.style.display = "block";
    } else {
       ele.style.display = "none";
    }
}

function toggleFormSection(frm, sectionId, statusId, indId, icon) {
	ele = document.getElementById(sectionId);   	
	statusEle = document.getElementById(statusId);
   	if (typeof ele == "undefined") {
    	   return;
    }
    if (ele.style.display == "none") {
       ele.style.display = "block";
   	   document.forms[frm].elements[indId].value="true";
       statusEle.style.display= "none";
       document.getElementById(icon).src="/assets/unmanaged/images/minus_icon.gif";
       
    } else {
       ele.style.display = "none";
   	   document.forms[frm].elements[indId].value="false";
       statusEle.style.display= "block";
       document.getElementById(icon).src="/assets/unmanaged/images/plus_icon.gif";
    }
}

function expandFormSection(frm, sectionId, statusId, indId, icon, expand) {
   if (document.forms[frm].elements[indId] != null && document.forms[frm].elements[indId].value!=expand) {
      toggleFormSection(frm, sectionId, statusId, indId, icon);
   }
}

function expandFormAllSection(frm, expand) {
    expandFormSection(frm, 'basic', 'headerBasic', 'expandBasic', 'expandBasicIcon', expand);
    expandFormSection(frm, 'employment', 'headerEmployment', 'expandEmployment', 'expandEmploymentIcon', expand);
    expandFormSection(frm, 'contact', 'headerContact', 'expandContact', 'expandContactIcon', expand);
    expandFormSection(frm, 'participation', 'headerParticipation', 'expandParticipation', 'expandParticipationIcon', expand);        
    expandFormSection(frm, 'vesting', 'headerVesting', 'expandVesting', 'expandVestingIcon', expand);        
}


// Change the country selection between USA and Non USA
// will trigger the zipcode/state rendering change
 function changeCountry(selection) {
    if (selection.value=='USA' || selection.value=='') {
        document.getElementById("stateUSA").style.display="block";
        document.getElementById("stateNonUSA").style.display="none";
        document.getElementById("zipCodeUSA").style.display="block";
        document.getElementById("zipCodeNonUSA").style.display="none";
        selection.form.elements['zipCode.zipCodeNonUSA'].value = "";
        selection.form.elements['state.stateNonUSA'].value = "";
     } else {
        document.getElementById("stateUSA").style.display="none";
        document.getElementById("stateNonUSA").style.display="block";
        document.getElementById("zipCodeUSA").style.display="none";
        document.getElementById("zipCodeNonUSA").style.display="block";
        selection.form.elements['zipCode.zipCode1'].value = "";
        selection.form.elements['zipCode.zipCode2'].value = "";
        selection.form.elements['state.stateUSA'].value = "";
     }
 }

  // related to vesting
   function pyosChanged(frm) {
       frm.elements['value(previousYearsOfServiceEffectiveDate)'].disabled=false;
       frm.elements['value(previousYearsOfServiceEffectiveDate)'].value="";
   }
   
	function fullyVestedChanged(frm) {
       frm.elements['value(fullyVestedIndEffectiveDate)'].disabled=false;
       frm.elements['value(fullyVestedIndEffectiveDate)'].value="";
	   var fullyVestedEffCal = document.getElementById('fullyVestedEffCal');
	   	if (typeof fullyVestedEffCal =="undefined") {
    		   return;
	    }
       fullyVestedEffCal.style.display = "inline";
	}


//////////////////////////////////////////////////////////////////////////////////
//    Client side validation
//////////////////////////////////////////////////////////////////////////////////
function validateFirstName(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableFirstName);
}
         
function validateMiddleInit(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableMiddleInit);
}

function validateLastName(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableLastName);
}

function validateEmployeeId(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableEmployeeId)
}

function validateBirthDate(field) {
	return validateField(field, new Array(validateDate), new Array(birthDateFormatError), true)
}

function validateHireDate(field) {
  	return validateField(field, new Array(validateDate), new Array(hireDateFormatError), true);
}

function validateFullyVestedEffDate(field) {
  	return validateField(field, new Array(validateDate), new Array(fullyVestedEffDateFormatError), true);	
}

function validateDivision(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableDivision);
}
	
function validateEmploymentStatusEffDate(field) {
	return validateField(field, new Array(validateDate), new Array(employmentEffDateFormatError), true);
}

function validateAnnualBaseSalary(field) {
	return validateCurrencyField(field, -999999999.99, 999999999.99, annualSalaryFormatError, annualSalaryDecimalError, annualSalaryOutOfRangeError, annualSalaryOutOfRangeError);
}

function validateYTDCompensation(field) {
	return validateCurrencyField(field, -999999999.99, 999999999.99, ytdCompensationFormatError, ytdCompensationDecimalError, ytdCompensationOutOfRangeError, ytdCompensationOutOfRangeError);
}

function validateYTDHoursWorked(field) {
	return validateField(field, new Array(validateNumber, validateInteger, validateYTDHoursRange), 
			new Array(ytdHoursWorkedFormatError, invalidYTDHoursWorkd, ytdHoursWorkedOutOfRangeError), true);
}

function validatePreviousYrsOfService(field) {
	return validateField(field, new Array(validatePYOSBlank, validateNumber, validateInteger, validatePYOSRange), 
			new Array(pyosBeBlank, previousYearsOfServiceFormatError, invalidPYOSRange, invalidPYOSRange), true);
}

function validatePreviousYrsOfServiceInAdd(field) {
	return validateField(field, new Array(validateNumber, validateInteger, validatePYOSRange), 
			new Array(previousYearsOfServiceFormatError, invalidPYOSRange, invalidPYOSRange), true);
}




function validatePYOSBlank(value) {
   if (document.editEmployeeForm.elements['clonedForm.value(previousYearsOfService)'].value != "" &&
      trim(value)=="") {
     return false;
   } else {
     return true;
   }
}

function validatePYOSRange(value) {
	var v = parseInt(value);
	if (v > 99 || v < 0) {
	    return false;
	} else {
		return true;
	}	
}

function validateYTDHoursRange(value) {
	var v = parseInt(value);
	if (v > 99999 || v < 0) {
	    return false;
	} else {
		return true;
	}
}

function validateYTDCompensationDate(field) {
	return validateField(field, new Array(validateDate), new Array(ytdCompEffDateFormatError), true);
}

function validateAddressLine1(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableAddressLine1);
}

function validateAddressLine2(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableAddressLine2);
}

function validateCity(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableCity);
}

function validateState(field) {
	return validateField(field, validateNonPrintableAscii, nonPrintableState);
}

function validateEmailAddress(field) {
	return validateField(field, new Array(validateNonPrintableAscii, validateEmailAddr), new Array(nonPrintableEmail, emailAddrError));
}

function validateEligibilityDate(field) {
	return validateField(field, validateDate, eligibleDateFormatError, true);
}

function validateDesignatedRothDefPer(field) {
	var res = validateField(field, new Array(validatePercentage, validatePerDecimal, validatePositive, validatePercRange), new Array(designatedRothDefPerFormatError, designatedRothDefPerDecimalError, designatedRothDefPerPositive, desigRothDefPerNotInRange), true);
	formatPercentage(field);
	return res;
}

function validateBeforeTaxDefPer(field) {
	var res = validateField(field, new Array(validatePercentage, validatePerDecimal, validatePositive, validatePercRange), new Array(beforeTaxDefPerFormatError, beforeTaxDefPerDecimalError, beforeTaxDefPerPositive, beforeTaxDefPerNotInRange), true);
	formatPercentage(field);
	return res;
}

function validateDesignatedRothDefAmt(field) {
	return validateCurrencyField(field, 0, 999999.99, designatedRothDefAmtFormatError, designatedRothDefAmtDecimalError, designatedRothDefAmtPositive, designatedRothDefAmtOutOfRangeError);
}

function validateBeforeTaxDefDollar(field) {
	return validateCurrencyField(field, 0, 999999.99, beforeTaxDefFormatError, beforeTaxDefDecimalError, beforeTaxDefPositiveError, beforeTaxDefOutOfRangeError);
}

function validatePerDecimal(value) {   
	return value.indexOf(".") == -1 || value.substring(value.indexOf(".")+1,value.length).length <= 3
}

function formatPercentage(field) {
	var v = field.value;
	if (!isNaN(v) && !validatePerDecimal(v)) {
		var index = v.indexOf(".");
		var newValue = v.substring(0, index + 4);
		field.value = newValue;
	}
}

function limitTextArea(field, maxSize) {
	if (field.value.length > maxSize) {
		field.value = field.value.substring(0, maxSize);
		return false;
	} else {
	    return true;
	}
}

function formatCurrency(num) {
	num = num.toString().replace(/\(|\)|\$|\,/g,'');
	if(isNaN(num)) {
		num = "0";
	}
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10) {
		cents = "0" + cents;
	}
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++) {
		num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
	}
	return (((sign)?'':'(') + '$' + num + '.' + cents + ((sign)?'':')'));
}
	
function formatCurrencyForInputTextBox(num) {
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10) {
		cents = "0" + cents;
	}
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++) {
		num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
	}
	return (((sign)?'':'-') + num + '.' + cents);
}

function parseCurrencyInput(num) {
	var multiplier = 1.0;
	if(num.toString().indexOf("(") != -1 || num.toString().indexOf("-") != -1)
		multiplier = -1.0;
	return multiplier * Math.round(num.toString().replace(/\-|\(|\)|\$|\,/g,'')*100)/100;
}

function validateCurrencyField(field, min, max, invalidMsg, decimalMsg, minMsg, maxMsg) {
    if (field.value.length==0) {
       return true;
    }
	if (field.value!="0.00") {
	    if (field.value == "******") {
	      // masked currency field.
	      return true;
	    }
		var num = field.value.replace(/\$|\,/g,'');
		if (num.length == 0) {
			alert(invalidMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (isNaN(num)) {
			alert(invalidMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
			alert(decimalMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num < min) {
			alert(minMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else if (num > max) {
			alert(maxMsg);
			setTimeout(function(){field.focus(); field.select();},10);
			field.value="";
			return false;
		} else {
			field.value=formatCurrencyForInputTextBox(num);
			return true;
		}
	}		 
}

// TODO: need to customize....
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
							&& ((hrefs[i].onclick.toString().indexOf("toggle") != -1)
									|| (hrefs[i].onclick.toString().indexOf("expand") != -1) 
									|| (hrefs[i].onclick.toString().indexOf("showPreviousValue") != -1))) {
						// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				}
				else if(
					hrefs[i].href != undefined 
							&&  ((hrefs[i].href.indexOf("toggle") != -1)
									|| (hrefs[i].href.indexOf("expand") != -1) 
									|| (hrefs[i].href.indexOf("showPreviousValue") != -1)
									|| (hrefs[i].href.indexOf("showSimilarRecord") != -1)
									|| (hrefs[i].href.indexOf("javascript:doCalendar") !=-1)
									)) {
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
				//alert (hrefs[i].onclick.toString());
			}
		}
 }	

 