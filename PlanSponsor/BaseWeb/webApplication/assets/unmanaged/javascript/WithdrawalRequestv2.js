/**
 * This file contains WithdrawalRequest Functionality
 *
 * @author Dennis Snowdon
 */
var legalButton = false;

function onFieldChange(obj) {
  document.getElementById('dirty').value = 'true';
}

function isFormDirty() {
	return (document.getElementById('dirty').value == 'true');
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

/**
 * Handle window for loan details.
 */
function doLoanDetail(loanNumber, profileId, contractId) {
	var baseURL = "/do/transaction/loanRepaymentDetailsReport/";
	
	if ( typeof(isTpa) != "undefined"  && isTpa ) 
		baseURL = "/do/transaction/tpaLoanRepaymentDetailsReport/";

	var reportURL = new URL(baseURL);
	reportURL.setParameter("loanNumber", loanNumber);
	reportURL.setParameter("profileId", profileId);
	reportURL.setParameter("contractId", contractId);
	reportURL.setParameter("task", "printLoanDetails");
	reportURL.setParameter("printFriendly", "true");
	window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

function getSelectedRadioValue(buttonGroup) {
   // returns the value of the selected radio button or "" if no button is selected
   var i = getSelectedRadio(buttonGroup);
   if (i == -1) {
      return "";
   } else {
      if (buttonGroup[i]) { // Make sure the button group is an array (not just one button)
         return buttonGroup[i].value;
      } else { // The button group is just the one button, and it is checked
         return buttonGroup.value;
      }
   }
}
 
function getSelectedRadio(buttonGroup) {
   // returns the array number of the selected radio button or -1 if no button is selected
   if (buttonGroup[0]) { // if the button group is an array (one button is not an array)
      for (var i=0; i<buttonGroup.length; i++) {
         if (buttonGroup[i].checked) {
            return i
         }
      }
   } else {
      if (buttonGroup.checked) { return 0; } // if the one button is checked, return zero
   }
   // if we get to this point, no radio button is selected
   return -1;
} // Ends the "getSelectedRadio" function

/**
 * Retrieves the selected value of the specified dropdown by id.
 *
 * @param selectId The id of the dropdown to query.
 * @return value
 */
function getSelectedValueById(selectId) {
	return getSelectedValue(document.getElementById(selectId));
}

/**
 * Retrieves the selected value of the specified dropdown.
 *
 * @param select The dropdown to query.
 * @return value
 */
function getSelectedValue(select) {
	return select.options[select.selectedIndex].value;		
}

/**
 * Resets the selected index to 0 for the specified dropdown id.
 *
 * @param selectId The dropdown id to reset.
 */
function resetDropdownById(selectId) {
	document.getElementById(selectId).selectedIndex = 0;
}

/**
 * Resets to blank the value of the specified input.
 *
 * @param inputId The input to reset.
 */
function resetInputById(inputId) {
	document.getElementById(inputId).value = '';
}
	
// Gets the nth element with the given name (nth is the index value).
function findElementByNameAndIndex(name, index) {
   var elementList=document.getElementsByName(name);
   // Note: The length is one more than the index, so we use ">" rather than ">="
   if ((elementList != null) && (elementList.length > index)) {
     return elementList[index];
   } else {
     return null;
   }
}
  
 // Gets the first element with the given name.
 function findFirstElementByName(name) {
   return findElementByNameAndIndex(name, 0);
 }

// Gets the total for fields in all rows in an indexed (STRUTS) table.
// (Note: Assumes the fields exist for every row in the table - dense population.)
// rowVariable - the row variable name for the <c:forEach> tag.
// addFieldArray - list of fields to add to the total (i.e. ["field1", "field2"])
// subtractFieldArray - list of fields to subtract from the total (i.e. ["field1", "field2"])
function getColumnTotal(rowVariable, addFieldArray, subtractFieldArray) {
  var sum=0;
  var rowExists=true;
  var value=0;
  for (index=0;rowExists;index++) {
  
    // Check to see if the row exists or not.
    // See if the field exists for the row.
    for(i=0;i<addFieldArray.length;i++) {
      if (findFirstElementByName(rowVariable+ "["+index+"]."+addFieldArray[i]) == undefined) {
        // The row does not exist, break and exit the row loop.
        rowExists=false;
        break;
      } // fi
     
      value = parseFloat(deformatAmount(findFirstElementByName(rowVariable+ "["+index+"]."+addFieldArray[i]).value));
      
      // Treat NaN as zero
      if (!isNaN(value)) {
         sum += value;
       }
    } // end for
    for(i=0;i<subtractFieldArray.length;i++) {
      if (findFirstElementByName(rowVariable+ "["+index+"]."+subtractFieldArray[i]) == undefined) {
        // The row does not exist, break and exit the row loop.
        rowExists=false;
        break;
      } // fi
      value = parseFloat(deformatAmount(findFirstElementByName(rowVariable+ "["+index+"]."+subtractFieldArray[i]).value));
      // Treat NaN as zero
      if (!isNaN(value)) {
      alert("sum - 2 :: " + sum);
       sum -= value;
       alert("sum - 3 :: " + sum);
     }
    } // end for
    
  } // end for
 
  return sum.toFixed(2); 
}

/**
 * Strips all occurances of the group separator ','.
 */
function deformatAmount(num) {

	// Remove grouping separators
	return num.toString().replace(/,/g, '');
}

/**
 * Formats the specified amount to use grouping separators, and optionally a 
 * dollar sign and two different negative formats.
 *
 * @param num The amount to format.
 * @param showDollarSign True if the number should use a dollar sign.
 * @param formatForInput True if the number should use the negative format -<num> rather than (<num>).
 */
function formatAmount(num, showDollarSign, formatForInput) {
	
	num = num.toString().replace(/\(|\)|\$|\,/g,'');
	if(isNaN(num)) {
		num = "0";
	}
	// Limit of 15 digits for JavaScript numbers
	if (num.length > 15) {
		return num;
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
	return (((sign)?'':((formatForInput)?'-':'(')) 
				 + (showDollarSign?'$':'') 
				 + num 
				 + '.' 
				 + cents 
				 + ((sign)?'':((formatForInput)?'':')')));
}

var g_FloatPanel = {
    panelId : -1,
    initialX : 0,
    initialY : 0,
    testTimer : 1000, //ms
    showDelay : 500, //ms
    testTimerId : -1,
    enabled : true,

    //always invoke this method to initialize the floating panel objext
    init : function (panelId) {
        this.panelId = panelId;
        
        var panel = document.getElementById(this.panelId);

        this.initialX = parseInt(panel.style.left);
        this.initialY = parseInt(panel.style.top);

        // Create compatibility layer for Netscape
        if (document.layers) {
            document.body = new Object
            setInterval("g_FloatPanel.testScroll()",testTimer)
        }

        window.onscroll = this.doScroll;
    },

    testScroll : function() {
        // Initialize scrollbar cache if necessary
        if (window._pageXOffset==null) {
            window._pageXOffset = window.pageXOffset
            window._pageYOffset = window.pageYOffset
        }

        // Expose Internet Explorer compatible object model
        document.body.scrollTop = window.pageYOffset
        document.body.scrollLeft = window.pageXOffset
        window.document.body.scrollHeight = document.height
        window.document.body.scrollWidth = document.width    

        // If cache!=current values, call the onscroll event
        if (((window.pageXOffset!=window._pageXOffset) || 
            (window.pageYOffset!=window._pageYOffset)) && (window.onscroll)) 
                window.onscroll()
        // Cache new values
        window._pageXOffset = window.pageXOffset
        window._pageYOffset = window.pageYOffset
    },

    doScroll : function () {
    	if(!g_FloatPanel.enabled) return;
    	
        if(this.testTimerId != -1)
            window.clearTimeout(this.testTimerId);
        
        document.getElementById(g_FloatPanel.panelId).style.visibility="hidden";
        
        this.testTimerId=window.setTimeout("g_FloatPanel.scrollPanel(g_FloatPanel.panelId)", this.testTimer);
    },

    scrollPanel: function (panelId, noDelay) {
        //Browser independent code to calculate window width (iWW), window height (iWH), horizontal scroll (iSX) and vertical scroll (iSY)

        if (window.innerWidth) { // NS4, NS6 and Opera
            var oW = window; iWW = oW.innerWidth; iWH = oW.innerHeight; iSX = oW.pageXOffset; iSY = oW.pageYOffset; }
        else if (document.documentElement && document.documentElement.clientWidth) { // IE6 in standards compliant mode
            var oDE = document.documentElement; iWW = oDE.clientWidth; iWH = oDE.clientHeight; iSX = oDE.scrollLeft; iSY = oDE.scrollTop; }
        else if (document.body) { // IE4+
            var oDB = document.body; iWW = oDB.clientWidth; iWH = oDB.clientHeight; iSX = oDB.scrollLeft; iSY = oDB.scrollTop; }

        var panel = document.getElementById(panelId);
        
        var newX = this.initialX;
        var newY = iSY + this.initialY;

        if (document.layers) {
            panel.style.left = newX;
            panel.style.top = newY;
        }
        else {
            panel.style.left = newX + "px";
            panel.style.top = newY + "px";
        }

		if(noDelay)
	        g_FloatPanel.showPanel(g_FloatPanel.panelId);
		else
	        window.setTimeout("g_FloatPanel.showPanel(g_FloatPanel.panelId)", g_FloatPanel.showDelay);
    },

    showPanel : function(panelId) {
        document.getElementById(panelId).style.visibility="visible";
    },
    
    enablePanel : function() {
    	this.enabled = true;
    	this.scrollPanel(this.panelId, true);
    },
    
    disablePanel : function() {
    	this.enabled = false;
        document.getElementById(g_FloatPanel.panelId).style.visibility="hidden";
    },
    
    setFieldValue : function(fieldId, fieldValue) {
    	var field = document.getElementById(fieldId);
    	field.innerHTML = fieldValue;
    }, 
    
    setFieldChanged : function(fieldId, fieldChanged) {
    	var field = document.getElementById(fieldId);
    	if(fieldChanged) {
	    	field.innerHTML = field.innerHTML+'<img id=chg'+fieldId+' src="/assets/unmanaged/images/warning2.gif" height="14" width="13">';
	    } else {
	    	var chgIcon = document.getElementById('chg'+fieldId);
			chgIcon.parentNode.removeChild(chgIcon);
	    }
    }
}
  
function protectLinks() {
		
		var hrefs  = document.links;
		if (hrefs != null)
		{
			for (i=0; i<hrefs.length; i++) { 
				if(
					hrefs[i].onclick != undefined && 
					(hrefs[i].onclick.toString().indexOf("openWin") != -1 || 
					 hrefs[i].onclick.toString().indexOf("popup") != -1 ||
					 hrefs[i].onclick.toString().indexOf("PDFWindow") != -1 ||
					 hrefs[i].onclick.toString().indexOf("doSignOut") != -1 ||
					 hrefs[i].onclick.toString().indexOf("doLoanDetail") != -1 ||
					 hrefs[i].onclick.toString().indexOf("doEmployeeSnapshot") != -1 ||
					 hrefs[i].onclick.toString().indexOf("doVestingInformation") != -1 ||
					 hrefs[i].onclick.toString().indexOf("doBankNameList") != -1 ||
					 hrefs[i].onclick.toString().indexOf("showPopupGuide") != -1 ||
					 hrefs[i].onclick.toString().indexOf("handleBirthDateIconClicked") != -1 ||
					 hrefs[i].onclick.toString().indexOf("handleTerminationDateIconClicked") != -1 ||
					 hrefs[i].onclick.toString().indexOf("handleRetirementDateIconClicked") != -1 ||
					 hrefs[i].onclick.toString().indexOf("handleDisabilityDateIconClicked") != -1 ||
					 hrefs[i].onclick.toString().indexOf("handleFinalContributionDateIconClicked") != -1 ||
					 hrefs[i].onclick.toString().indexOf("handleExpirationDateIconClicked") != -1 ||
					 hrefs[i].onclick.toString().indexOf("handleShowSuppressFloatingSummaryClicked") != -1 ||
					 hrefs[i].onclick.toString().indexOf("showActivityHistory") != -1 ||
					 hrefs[i].onclick.toString().indexOf("#") != -1 ||
					 hrefs[i].onclick.toString().indexOf("expand") != -1 ||
					 hrefs[i].onclick.toString().indexOf("collapse") != -1 ||
					 hrefs[i].onclick.toString().indexOf("showParticipantLegaleseContent") != -1
					 )
				) {
					// Don't replace window open or popups as they won't lose their changes with those
				}
				else if(
					hrefs[i].href != undefined && 
					(hrefs[i].href.indexOf("openWin") != -1 ||
					 hrefs[i].href.indexOf("popup") != -1 ||
					 hrefs[i].href.indexOf("PDFWindow") != -1 ||
					 hrefs[i].href.indexOf("doSignOut") != -1 ||
					 hrefs[i].href.indexOf("doLoanDetail") != -1 ||
					 hrefs[i].href.indexOf("doEmployeeSnapshot") != -1 ||
					 hrefs[i].href.indexOf("doVestingInformation") != -1 ||
					 hrefs[i].href.indexOf("doBankNameList") != -1 ||
					 hrefs[i].href.indexOf("showPopupGuide") != -1 ||
					 hrefs[i].href.indexOf("handleBirthDateIconClicked") != -1 ||
					 hrefs[i].href.indexOf("handleTerminationDateIconClicked") != -1 ||
					 hrefs[i].href.indexOf("handleRetirementDateIconClicked") != -1 ||
					 hrefs[i].href.indexOf("handleDisabilityDateIconClicked") != -1 ||
					 hrefs[i].href.indexOf("handleFinalContributionDateIconClicked") != -1 ||
					 hrefs[i].href.indexOf("handleExpirationDateIconClicked") != -1 ||
					 hrefs[i].href.indexOf("handleShowSuppressFloatingSummaryClicked") != -1 ||
					 hrefs[i].href.indexOf("showActivityHistory") != -1 ||
					 hrefs[i].href.indexOf("#") != -1 ||
					 hrefs[i].href.indexOf("expand") != -1 ||
					 hrefs[i].href.indexOf("collapse") != -1 ||
					 hrefs[i].href.indexOf("showParticipantLegaleseContent") != -1
					 )
				) {
					// Don't replace window open or popups as they won't lose their changes with those
				}
				else {
					if (hrefs[i].href.indexOf("task=download") != -1) {
						hrefs[i].onclick = new Function ("return confirmDiscardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');");
					} else {
						hrefs[i].onclick = new Function ("return confirmDiscardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
					}
				}
			}
		}
 }	
 
/**
 * Determines if a discard confirmation should be displayed.
 */ 
function confirmDiscardChanges(warning) {
  if (document.getElementById('dirty').value == 'true') {
  	return window.confirm(warning);
  } else {
	  return true;
	}
}
 
/**
 * Trims the whitespace from the specified string.
 */
function trim(string) {

  if(string.charAt(0) == " ") {
    string = trim(string.substring(1));
  }
  if (string.charAt(string.length-1) == " "){
    string = trim(string.substring(0, string.length - 1));
  }
  
  return string;
}

/**
 * Set the radio button with the given value as being checked do nothing if there are no radio buttons
 * if the given value does not exist, all the radio buttons are reset to unchecked
 */
function setRadioCheckedValue(radioObj, newValue) {
  if(!radioObj)
    return;
  var radioLength = radioObj.length;
  if(radioLength == undefined) {
    radioObj.checked = (radioObj.value == newValue.toString());
    return;
  }
  for(var i = 0; i < radioLength; i++) {
    radioObj[i].checked = false;
    if(radioObj[i].value == newValue.toString()) {
      radioObj[i].checked = true;
    }
  }
}

/**
 * This function prepares and submits the form. This is tied closely to the MessagesTag, as
 * it's used to submit the form if the user clicks 'OK' on the warnings popup message.
 * The 'submitAction' is hidden field in the form that holds the latest action attempted.
 * The 'ignoreWarningsId' is a hidden field in the form that determines if warnings are ignored.
 */
function processForm() {
	  var submitInProgress = isSubmitInProgress();
	document.getElementById("action").disabled = false;
	 document.getElementById("withdrawalRequestUi.withdrawalRequest.ignoreWarnings").disabled = false;
	 document.withdrawalForm.submit();
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
 function handleDateIconClicked(evt, fieldId, setDirtyFlag, checkVestingFlag) {

    // Retrieve the field
    var field = document.getElementById(fieldId);

    // Mark field as changed
    if (setDirtyFlag) {
	    onFieldChange(field);
	  }

    // Mark vesting flag as changed
    if (checkVestingFlag) {
	    checkRobustDateChangedAfterVesting(); 
	  }
    
    // Pre-Validate date and blank if not valid
    if (!validateMMddYYYY(field, false)) {
      field.value = '';
    }
    
    // Popup calendar
    handleCalendarSetup(field, evt);
 }

 /**
 * Generic Handler for changes in input fields
 *
 * @param field The field that changed.
 * @param index1 The first index of the field or null if the field is not indexed.
 * @param index2 The second index of the field or null if the field is not double indexed.
 * @param validate The validate method to invoke or null if the field does not require validation.
 * @param recalculate The recalculate flag method or null if the field change does not force recalculation.
 * @param update The page update method or null if the field change does not force a page update.
 * @param preProcess The preprocess method or null if the field does not use preprocessing.
 * @param preProcessArg An optional pre processing argument - passed through to pre process.
 * @param postProcess The post process method or null if the field does not use postprocessing.
 * @param postProcessArg An optional post processing argument - passed through to post process.
 */ 
 function handleFieldChanged(field, index1, index2, validate, recalculate, update, preProcess, preProcessArg, postProcess, postProcessArg) {

/*
    alert('Handle field changed called with field [' 
          + field.value
          + '], index1 ['
          + index1
          + '], index2 ['
          + index2
          + '], validate ['
          + validate
          + '], recalculate ['
          + recalculate
          + '], update ['
          + update
          + '], preProcess ['
          + preProcess
          + '], preProcessArg ['
          + preProcessArg
          + '], postProcess ['
          + postProcess
          + '], postProcessArg ['
          + postProcessArg
          + ']');
*/
    // Pre-process
    if (preProcess != null) {
      if (preProcessArg != null) {
        preProcess(field, preProcessArg);
      } else {
        preProcess(field);
      }
    }

    // Flag updates
    onFieldChange(field);
    if (recalculate != null) {
      recalculate();
    }

    // Validation (if necessary)
    if (validate != null) {
      var isValid;
      // Determine what level of index we are using (zero, one or two)
      if (index2 != null) {
        isValid = validate(index1, index2);
      } else if (index1 != null) {
        isValid = validate(index1);
      } else {
        isValid = validate();
      }
      
      // Check validity
      if (!isValid) {
        return false;
      }
    }

    // Page Update
    if (update != null) {
      update();
    }

    // Post-process (if necessary)
    if (postProcess != null) {
      if (postProcessArg != null) {
        postProcess(field, postProcessArg);
      } else {
        postProcess(field);
      }
    }

    // Allow processing to continue    
    return true;    
 }
 
 /**
 * Preprocess checkbox
 *
 * @param checkBox The checkbox being processed.
 * @param field The backing field.
 */
 function preprocessCheckBox(field, checkBox) {
    if (checkBox.checked) {
      field.value = 'true';
    } else {
      field.value = 'false';
    }
 }
