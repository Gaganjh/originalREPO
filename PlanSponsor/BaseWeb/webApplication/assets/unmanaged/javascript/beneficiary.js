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
/**
 * Method to set the action in the form and submit the from.
 * Called when the user clicks the Search button	
 */
function doSubmit(action) {
	document.beneficiaryForm.action.value=action;
	document.beneficiaryForm.submit();
}

/**
* Method to set the task in the form and submit the from.
* Called when the user clicks the Search button	
*/
function doSet(beneficiaryNo) {
	document.beneficiaryForm.beneficiaryType.value=beneficiaryNo;
	document.beneficiaryForm.submit();
}

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
* Method to set the pop up window for unsaved data.
* Called when the user clicks other than 	
*/
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
					hrefs[i].href != undefined 
					&&  ( (hrefs[i].href.indexOf("javascript:doAddBeneficiary") !=-1 ) ||(hrefs[i].href.indexOf("javascript://cal") !=-1)))
			{
				// don't replace these links as we will do an automatic save on the server side before invoking the requested function
			}
			else if(hrefs[i].onclick != undefined) {
				if (hrefs[i].onclick.toString().indexOf("task=add") != -1) {
					// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				} else {
					hrefs[i].onclick = new Function ("var result = discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
				}				
			}
			else {
				if (hrefs[i].href.indexOf("task=add") != -1) {
					// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				} else {
					hrefs[i].onclick = new Function ("return discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
				}
			}
		
		//alert (hrefs[i].onclick.toString());
		}
	}
}	
/**
 * Generic handler for date icon clicked
 */ 
function handleDateIconClicked(evt, fieldId) {

	// Retrieve the field
	var field = document.getElementById(fieldId);
	


	// Pre-Validate date and blank if not valid
	if (typeof field  == "undefined") {
		field.value = '';
	}

	// Popup calendar
	handleCalendarSetup(field, evt);
}

 /**
  * enableOther text field is the selected drop down box contain other values selected
  * @param selectField
  * @param textFieldId
  * @return
  */
  function enableOther(selectField, textFieldId) {

		//if 'other' has been selected, enable the other relation box, otherwise, disable it.
		//get the current relation field

		if (document.getElementById) {
			var textField = document.getElementById(textFieldId);
			if (textField != null && typeof textField != 'undefined') {
				var selectedIndex = selectField.selectedIndex;
				if (selectField.options[selectedIndex].value == '09') {
					textField.style.display = "block";
				} else {
					textField.style.display = "none";
				}
			}
		}

		

	}

/**
 * Disable the div field if it is empty
 * @param divId
 * @param selectId
 */
 function disableOthers(divId,selectId) {

	    //if Select box  contain value other than 'Other' ,If other section to be hided.

	    if (document.getElementById) {
	        var divField = document.getElementById(divId);
	        var selectField = document.getElementById(selectId);
	        var selectedIndex = selectField.selectedIndex;
	        if (selectField.options[selectedIndex].value != '09') {
	        	divField.style.display = "none";
	            }
	        }
	    }

/**
* Method to set the beneficiaryType and the action in the form
* and submit the from.
* Called when the user clicks the Add Primary or Add contingent
*/
function doAddBeneficiary(beneficiaryType){
	document.beneficiaryForm.beneficiaryType.value = beneficiaryType;
	document.beneficiaryForm.action.value = 'add';
	document.beneficiaryForm.submit();
}
