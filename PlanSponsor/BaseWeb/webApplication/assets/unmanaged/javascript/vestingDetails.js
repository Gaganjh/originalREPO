totalErrorCount = 0;

var DEBUG = false;
var errorMessage="";
var submitted=false;

var errorContent="";
var buttonClicked = null;
var uploadFormObj = null;

var isNetscape=eval(navigator.appName == "Netscape");
var isExplorer=eval(navigator.appVersion.indexOf("MSIE")!=-1);

var isVestingProvided = false;
var isVestingCalculated = false;

function getFormByName(formName) {
	if (navigator.appName == "Netscape" && parseInt(navigator.appVersion) == 4) {
		return getFormByNameNav4(formName);
	} else {
		return document.forms[formName];
	}
}

function getFormByNameNav4(formName, parent) {
	var objForm;
	var parentObj= (parent) ? parent : document;
		objForm= parentObj.forms[formName];
	if (!objForm) {
		for (var i= 0; i < parentObj.layers.length && !objForm; i++) {
			objForm= getFormByNameNav4(formName, parentObj.layers[i].document);
		}
	}
	return objForm;
}

function writeError(text) {
	//alert('>>> Write error' + text + ' isNetscape = ' + isNetscape);

	var contentString;
	
	if (text=="") 
		errorContent = "";
	else 
		errorContent = errorContent + "<tr><td width=\"5%\">&nbsp;</td><td width=\"95%\"><img src='/assets/unmanaged/images/error.gif' alt='Error'/> " + text + "</td></tr>";
		
	var contentString;
	if (text.length > 0 )
		contentString = '<table id="psErrors">' +
			errorContent + '</table><br>';
	else 
		contentString = '';

	if ( document.getElementById ) {
		document.getElementById('errordivcs').innerHTML = contentString;
	}else if (isExplorer) {
		document.all.errordivcs.innerHTML = contentString;
	} else if (isNetscape) {
		//this is old netscape
		document.errordivcs.document.open();
		document.errordivcs.document.write(contentString);
		document.errordivcs.document.close();
	}

	// increment the error count by one		
	if (text!="" && text!=" " &&text.length>0) {
		if ( document.getElementById ) {
			document.getElementById('errorCount').innerHTML = parseInt(document.getElementById('errorCount').innerHTML)+1;
		} else if (isExplorer) {
			document.all.errorCount.innerHTML = parseInt(document.all.errorCount.innerHTML)+1;
		} else if (isNetscape) {
			//this is old netscape
			document.errorCount.document.open();
			document.errorCount.document.write(parseInt(document.errorCount)+1);
			document.errorCount.document.close();
		}
		location.href='#TopOfPage';
	}

	if(DEBUG) alert('Write error' + text);
}

function y2k(number) { 		
	if (isExplorer) { 
		return (number < 1000) ? number + 1900 : number; 
	} else {
		return (number + 1900); 
	}
}	
var months = new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");

function formatDate(date) {
	return months[date.getMonth()] + " " + date.getDate() + ", " + y2k(date.getYear());
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
	
function formatPercentageForInputTextBox(num) {
	if (num.indexOf(".") != -1) {
		dec = num.substring(num.indexOf(".")+1,num.length);
		for (var i = dec.length; i < 3; i++) {
			num = num + '0';
		}
	} else {
		num = num + '.000';
	}
	return num;
}

function resetErrorCount() {
	if ( document.getElementById ) {
		document.getElementById('errorCount').innerHTML = initialErrorCount;
	} else if (isExplorer) {
		document.all.errorCount.innerHTML = initialErrorCount;
	} else if (isNetscape) {
		//this is old netscape
		document.errorCount.document.open();
		document.errorCount.document.write(initialErrorCount);
		document.errorCount.document.close();
	}
}

function doSaveWithValue(value)	{

	if (submitted) {
		window.status = "Transaction already in progress.  Please wait.";
		return false;
	} else {
	    buttonClicked = value;
		return true;
	}
}


function doCancelWithValue(value)	{

	buttonClicked = value;
	return doCancel(buttonClicked.value);
}


function doCancel(name)	{

	if (submitted) {
		window.status = "Transaction already in progress.  Please wait.";
		return false;
	} else {	
		resetErrorCount();
		var discChanges = discardChanges(warningDiscardChanges);

		if (discChanges) {
			if (name == "cancel submission") {
				var doCancel = confirm(warningCancelSubmission);
				if (!doCancel)  return false;
			}
			submitted = true;
			submitInProgress = true;
		} 
		
		return discChanges;
	}
}

function protectLinks() {
		
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
							&& ((hrefs[i].onclick.toString().indexOf("prepareAddParticipant") != -1)
									|| (hrefs[i].onclick.toString().indexOf("addMoneyType") != -1) 
							)) {
						// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				}
				else if(
					hrefs[i].href != undefined 
							&&  ((hrefs[i].href.indexOf("prepareAddParticipant") != -1)
									|| (hrefs[i].href.indexOf("addMoneyType") != -1) 
									|| (hrefs[i].href.indexOf("javascript:doCalendar") !=-1) )) {
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
		
		if(uploadFormObj != null && uploadFormObj.showConfirmDialog.value == 'true')
			confirmSubmitWithWarnings();
 }	



function initPage() {
	
	// get the initial error count in the errors/warning box
	initialErrorCount = 0;
	if ( document.getElementById && document.getElementById('errorCount') != null ) {
		initialErrorCount = document.getElementById('errorCount').innerHTML;
	} else if (isExplorer && document.all.errorCount != null) {
		initialErrorCount = document.all.errorCount.innerHTML;
	} else if (isNetscape) {
		//this is old netscape
		initialErrorCount = document.errorCount;
	}

	uploadFormObj = getFormByName("vestingDetailsForm");
	isVestingProvided=eval(uploadFormObj.elements['vestingCSF'].value == "TPAP");
	isVestingCalculated=eval(uploadFormObj.elements['vestingCSF'].value == "JHC");

}

function validateVestingEffDateInputOnChange(object, index) {

	// called from the jsp onchange event
	if (!isRowCheckedForDelete(index)) {
		return validateVestingEffDateInput(object);
	} else {
		return true;
	}
}

function validateVestingEffDateInput(object) {
	var isValid = true;
	//alert("validating " + object.value);
	var str_date = object.value;
	if (str_date.length != 0) {
		var arr_date = str_date.split('/');
		
		if(isNaN(arr_date[0]) || isNaN(arr_date[1]) || isNaN(arr_date[2]) || 
		arr_date[0].length > 2 || arr_date[1].length > 2 || arr_date[2].length != 4) {
			// check if numbers
			object.select();
			//alert ("Invalid date. Please use mm/dd/yyyy format or select a valid date from the calendar.");
			alert(messageInvalidDate);
			object.focus();
			isValid = false;
		} else if (typeof(cal) != 'undefined') {  
		
			// check the date
			var result = cal.prs_date_no_alert(str_date, false);
				
			if (typeof(result) == "string" || result == null) {
				object.select();
				//alert ("Invalid date. Please use mm/dd/yyyy format or select a valid date from the calendar.");
				alert(messageInvalidDate);
				object.focus();
				isValid = false;
			} else {
				var current_date = new Date();
				current_date.setHours(0);
				current_date.setMinutes(0);
				current_date.setSeconds(0);
				current_date.setMilliseconds(0);
				//alert("current_date " + current_date);
				var selected_date = Date.parse(str_date);
				//alert("selected_date " + selected_date);
				var past_date = new Date(current_date.getFullYear() - 2,current_date.getMonth(),current_date.getDate());
				//alert("past_date " + past_date);
				var futurePlanYearEnd = document.getElementById('futurePlanYearEnd').value;
				var future_date = Date.parse(futurePlanYearEnd);
				//alert("future_date " + future_date);
				if (selected_date > future_date) {
					object.select();
					alert(messageDateInTheFuture);
					object.focus();
					isValid = false;
				} else if (selected_date < past_date) {
					object.select();
					alert(messageDate24MonthsInThePast);
					object.focus();
					isValid = false;
				}
			}
		}
	}
	return isValid;
}

function resetValues(){
	if (confirm('Are you sure you want to discard changes?')) {
		resetErrorCount();
		writeError("");
		uploadFormObj.reset();
		location.href='#TopOfPage';
	}
}

function isNullInput(str) {

	b = ((str==null) || (""==str));
	//alert("isNullInput returns " + b + "for '" + str + "'" );
	return b;
}

function validateParticipantSection() {
	var isValid = true;
	var col = 0;
	
	// re-validate each vesting effective date/vyos date
	var row = 0;
	while(document.getElementsByName('vestingDatesMap['+row+']').length != 0) {
		if (!isRowCheckedForDelete(row)) {
			var dateObj = document.getElementsByName('vestingDatesMap['+row+']')[0];
			if (validateVestingEffDateInput(dateObj) == false) {
				isValid = false;
				break;
			} 
		}
		row++;
	}
    
	
	if (isValid) {
	
		if (isVestingCalculated) {
			// re-validate each vyos field on the page (if available)
			row = 0;
			while(document.getElementsByName('vestedYearsOfService['+row+']').length != 0) {
				if (!isRowCheckedForDelete(row)) {
					var stringObj = document.getElementsByName('vestedYearsOfService['+row+']')[0];
					if (validateVestedYearsOfServiceInput(stringObj) == false) {
						isValid = false;
						break;
					} 
				}
				row++;
			}
		}
	
		if (isVestingProvided) {
			// re-validate each perc field on the page (if available)
			row = 0;
			while(document.getElementsByName('deleteBoxesMap['+row+']').length != 0) {
				if (!isRowCheckedForDelete(row)) {
					var fieldArray = document.getElementsByName('vestingColumnsMap[0].row['+row+']');
					if (fieldArray != null) {
						for (var j=0; j<fieldArray.length; j++) {
							var arrayObject = eval(fieldArray[j]);
							var fullyVested = eval("moneyTypeFullyVestedIndicator"+j);
							if (validateParticipantVestingInput(arrayObject,fullyVested) == false) {
								isValid = false;
								break;
							}
						}
					}
					if (!isValid) break;
				}
				row++;
			}
		}
	}
	return isValid;
}

function isRowCheckedForDelete(row) {
	var isChecked = false;
	
	if (document.getElementsByName('deleteBoxesMap['+row+']').length != 0) {
		if(document.getElementsByName('deleteBoxesMap['+row+']')[0].checked) {
			isChecked = true;
		} 
	}
	
	return isChecked;
}

function confirmSend() {
	//writeError("");
	resetErrorCount();

	if (buttonClicked.value == "undo" || buttonClicked.value == "submission history" || 
	    buttonClicked.value == "cancel submission") {
		submitted = true;
		submitInProgress = true;
		return true;
	}
	
	// validate participant section
	if(validateParticipantSection() == false) {
		return false;
	}
	
	// delete participants
	if(deletingParticipants() == false) {
		return false;
	}
	
	// we only check if any changes were done, for the save actions
	if (buttonClicked.value == "save") {
		if (isFormChanged() || document.forms['vestingDetailsForm'].elements['forwardFromSave'].value != "") {
			submitted = true;
			submitInProgress = true;
			return true;
		} else {
			alert(messageNoChangesMade);
			return false;
		}		
	}
	
	var message;
	message = "";
	
	
	if (uploadFormObj.ignoreDataCheckWarnings.value == 'true' &&
			uploadFormObj.showConfirmDialog.value == 'false') {
		uploadFormObj.showConfirmDialog.value = 'true';
		writeError("");
		submitted = true;
		submitInProgress = true;
		return true;
	}
	
	// check for duplicate money type selections and present a warning
	var moneyTypes = new Array();
	var col = 0;
	while(document.getElementsByName('moneyTypeColumns['+col+']').length != 0) {
		if(document.getElementsByName('moneyTypeColumns['+col+']')[0].value != '-1')
			moneyTypes[col] = document.getElementsByName('moneyTypeColumns['+col+']')[0].value;
		col++;
	}
	
	moneyTypes.sort();
	for(i=0; i<(moneyTypes.length-1); i++) {
		if(moneyTypes[i]==moneyTypes[i+1]) {
			message = message+"WARNING:  You have duplicate money types in the participant details section. Are you sure you want to proceed?\n\n";
			break;
		}
	}

	if (message == "") {
		message = message+"You are about to make a vesting submission. Do you want to continue?";
	}
	
	if (confirm(message)) {
		writeError("");
		submitted = true;
		submitInProgress = true;
		return true;
	} else {
		return false;
	}
	
}


function confirmSubmitWithWarnings() {
	//alert('confirmSubmitWithWarnings');
	var message = "\nThe following data checker warnings were generated:";
	message = message + messageDataCheckerWarnings;
	message = message + "\nDo you wish to proceed with the submission?";

	if (confirm(message)) {
		// yes we want to ignore the datacheckor warnings
		uploadFormObj.ignoreDataCheckWarnings.value = 'true';
		uploadFormObj.showConfirmDialog.value = 'false';
		document.getElementById("submitButton").click();
		return true;
	} else {
		resetErrorCount();
		return false;
	}
}

function initDocument() {
	writeError(errorMessage);
	// get the initial error count in the errors/warning box
	initialErrorCount = 0;
	if ( document.getElementById ) {
		initialErrorCount = document.getElementById('errorCount').innerHTML;
	} else if (isExplorer) {
		initialErrorCount = document.all.errorCount.innerHTML;
	} else if (isNetscape) {
		//this is old netscape
		initialErrorCount = document.errorCount;
	}
}


// START

function validateParticipantVestingInputOnChange(object, index, fullyVested) {

	// called from the jsp onchange event
	if (!isRowCheckedForDelete(index)) {
		return validateParticipantVestingInput(object, fullyVested);
	} else {
		return true;
	}
}

function validateParticipantVestingInput(object, fullyVested) {

	var isValid = true;
	//alert("validating " + object.value);
	if (object.value!="") {
		var num = object.value.replace(/\%|\,/g,'');
		if(isNaN(num)) {
			isValid = false;
			object.select();
			alert(messageInvalidPerc);
			object.focus();
		} else if (num != 100.00 && fullyVested == 'Y') {
			isValid = false;
			object.select();
			alert(messageInvalidPercFullyVestedMT);
			object.focus();
		} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 3) {
			isValid = false;
			object.select();
			alert(messageTooManyDecimals);
			object.focus();
		} else if (num != Math.abs(num)) {
			isValid = false;
			object.select();
			alert(messageInvalidPerc);
			object.focus();
		} else if (num > 100.00) {
			isValid = false;
			object.select();
			alert(messageInvalidPerc);
			object.focus();
		} else if (num < 1.00 && num != 0.00) {
			isValid = false;
			object.select();
			alert(messageInvalidPerc);
			object.focus();
		} 
	}		 

	return isValid;
}

//START

function validateParticipantLongTermPartTimeAssessmentYearInputOnChange(object, index) {

	// called from the jsp onchange event
	if (!isRowCheckedForDelete(index)) {
		return validateParticipantLongTermPartTimeAssessmentYearInput(object);
	} else {
		return true;
	}
}

function validateParticipantLongTermPartTimeAssessmentYearInput(object) {

	var isValid = true;
	
	if(object.value == 0 || object.value == null){
	alert(messageInvalidLTPTAssessmentYear);
	}
	
	if (object.value!=0) {
		var num = object.value.replace(/\%|\,/g,'');
		if(isNaN(num)) {
			isValid = false;
			object.select();
			alert(messageInvalidLTPTAssessmentYear);
			object.focus();
		} else if (num < 1 || num > 3) {
			isValid = false;
			object.select();
			alert(messageInvalidLTPTAssessmentYear);
			object.focus();
		} 
	}		 
	
	return isValid;
}

function validateVestedYearsOfServiceInputOnChange(object, index) {

	// called from the jsp onchange event
	if (!isRowCheckedForDelete(index)) {
		return validateVestedYearsOfServiceInput(object);
	} else {
		return true;
	}
}

function validateVestedYearsOfServiceInput(object) {

	var isValid = true;
	//alert("validating " + object.value);
	if (object.value!="") {
		var num = object.value.replace(/\$|\,/g,'');
		if(isNaN(num)) {
			isValid = false;
			object.select();
			alert(messageInvalidVYOS);
			object.focus();
		} else if (num.indexOf(".") != -1) {
			isValid = false;
			object.select();
			alert(messageVYOSNotAnInteger);
			object.focus();
		} else if (num != Math.abs(num)) {
			isValid = false;
			object.select();
			alert(messageInvalidVYOS);
			object.focus();
		} else if (num > 99) {
			isValid = false;
			object.select();
			alert(messageInvalidVYOS);
			object.focus();
		} else if (num == 0 && num != '0') {
			isValid = false;
			object.select();
			alert(messageInvalidVYOS);
			object.focus();
		} 
	}		 

	return isValid;
}

function changingMoneyTypes(dropDown, fieldName, pageCount) {
	//alert(dropDown.value);
	//alert(changeTracker.getOldValue(fieldName));
	//alert(pageCount);
	if(dropDown.value != changeTracker.getOldValue(fieldName)
			&& changeTracker.getOldValue(fieldName) != '-1') {
		if (submitted) {
			window.status = "Transaction already in progress.  Please wait.";
			dropDown.value = changeTracker.getOldValue(fieldName);
			return;
		} 
		if (confirm("The new money type applies to all participant contributions in this column.\nWould you like to continue?")) {
			//submitted = true;
			document.getElementById('saveButton').click();
		} else {
			dropDown.value = changeTracker.getOldValue(fieldName);
		}
	}
}

function updateParticipantCountForDelete(checkBox, row) {
	var numberOfRecords = document.getElementById('numberOfRecords');
	//alert(numberOfRecords);
	if (!checkBox.checked) {
		numberOfRecords++;
	} else {	
		if (checkBox.checked) {
			numberOfRecords--;
		}	
	}
	numberOfRecords.innerHTML = numberOfRecords; 
}

function deletingParticipants() {
	var row = 0;
	var numParticipantsToDelete = 0;
	var deletedRows = new Array();
	while(document.getElementsByName('deleteBoxesMap['+row+']').length != 0) {
		if(document.getElementsByName('deleteBoxesMap['+row+']')[0].checked) {
			deletedRows[numParticipantsToDelete] = row;
			numParticipantsToDelete++;
		}
		row++;
	}
	
	if(numParticipantsToDelete != 0) {
		if(confirm(numParticipantsToDelete + ' participant(s) will be deleted from this submission. Do you wish to Continue?')) {
			// don't do anything
		} else {
			return false;
		}
	}
	return true;
}




function saveAndForward(forwardLocation) {

	if (submitted) {
		window.status = "Transaction already in progress.  Please wait.";
		return false;
	} else {
		document.forms['vestingDetailsForm'].elements['forwardFromSave'].value = forwardLocation;
		buttonClicked = document.getElementById("saveButton");
		document.getElementById("saveButton").click();
		return true;
	}
}

function setReportPageSizePreferenceForEdit(warningMessage) {
  var textFields = document.getElementsByName("newPageSize");
  if (textFields == null || textFields.length == 0) {
    return;
  }

  /*
   * Check if a number was entered in the text field
   */
  if (isNaN(textFields[0].value)) {
    alert(warningMessage);
    textFields[0].value = reportPageSize;
    return;
  }
  
  /*
   * Obtains the integer value from the text field.
   */
  var newPageSize = parseInt(textFields[0].value,10);

  /*
   * If it's not valid, show the warning message and
   * reset text field value to the original text value.
   */
  if (isNaN(newPageSize) || newPageSize <= 0 || newPageSize > 100) {
    alert(warningMessage);
    textFields[0].value = reportPageSize;
    return;
  }

  // uncomment rows below if you want to save data on changing the report page size
  //document.forms['vestingDetailsForm'].elements['forwardFromSave'].value = "setReportPageSize";
  //buttonClicked = document.getElementById("saveButton");
  //document.getElementById("saveButton").click();
  
  callToServer("/do/preferences/setReportPageSize/?newPageSize=" + newPageSize);

}
var windowTimer = null;

function Timer() {
	window.clearTimeout(windowTimer);
	newPageRequested = false;
	sortRequested = false;
}

var newPageRequested = false;    
function pagingSubmit(pageNumber){
	var newLocation = location.href;
	var pos = newLocation.indexOf("?");
	if (pos != -1) {
		newLocation = newLocation.substring(0,pos) + "?";
	} else {
		newLocation = location.href+"?";
	}	
	if (newPageRequested) {
		window.status = "Transaction already in progress.  Please wait.";
		document.body.style.cursor = "wait";
	} else {
		windowTimer = window.setTimeout("Timer()",1000);
		newLocation= newLocation+"task=page&pageNumber="+pageNumber+"#participantTable";
		newPageRequested = true;
		document.location.href=newLocation;
	}
}

var sortRequested = false;    
function sortSubmit(sortfield, sortDirection){
	var newLocation = location.href;
	var pos = newLocation.indexOf("?");
	if (pos != -1) {
		newLocation = newLocation.substring(0,pos) + "?";
	} else {
		newLocation = location.href+"?";
	}	
	if (sortRequested) {
		window.status = "Transaction already in progress.  Please wait.";
		document.body.style.cursor = "wait";
	} else {
		windowTimer = window.setTimeout("Timer()",1000);
		newLocation= newLocation+"task=sort&sortField="+sortfield+"&sortDirection="+sortDirection+"#participantTable";
		sortRequested = true;
		document.location.href=newLocation;
	}

}

//Apply LTPT Crediting
function validateApplyLTPTCreditingInputOnChange(object, index) {

	// called from the jsp onchange event
	if (!isRowCheckedForDelete(index)) {
		return validateApplyLTPTCreditingInput(object);
	} else {
		return true;
	}
}

function validateApplyLTPTCreditingInput(object) {

	var isValid = true;
	
	if (object.value!=null) {
		var applyLTPT = object.value.replace(/\%|\,/g,'');
		if(applyLTPT!='Y' && applyLTPT!='N') {
			isValid = false;
			object.select();
			alert(messageInvalidApplyLTPTCreditingValue);
			object.focus();
		} else {
			isValid = true;
			object.select();
			object.focus();
		} 
	}		 
	return isValid;
}