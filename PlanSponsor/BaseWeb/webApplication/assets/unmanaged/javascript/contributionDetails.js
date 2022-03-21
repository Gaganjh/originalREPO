totalErrorCount = 0;

var DEBUG = false;
var errorMessage="";
var submitted=false;

/* Helper Functions */
//AG: formName == submissionPaymentForm

var errorContent="";
var buttonClicked = null;

var isNetscape=eval(navigator.appName == "Netscape");
var isExplorer=eval(navigator.appVersion.indexOf("MSIE")!=-1);

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

var uploadFormObj = getFormByName("editContributionDetailsForm");

function writeError(text) {
	//alert('>>> Write error' + text + ' isNetscape = ' + isNetscape);

	var contentString;
	
	if (text=="") 
		errorContent = "";
	else 
		errorContent = errorContent + "<tr><td width=\"10%\">&nbsp;</td><td width=\"90%\"><img src='/assets/unmanaged/images/error.gif' alt='Error'/> " + text + "</td></tr>";
		
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
	return doCancel();
}


function doCancel()	{

	if (submitted) {
		window.status = "Transaction already in progress.  Please wait.";
		return false;
	} else {	
		resetErrorCount();
		var discChanges = discardChanges(warningDiscardChanges);

		if (discChanges) {
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
									|| (hrefs[i].onclick.toString().indexOf("addLoan") != -1)
									|| (hrefs[i].onclick.toString().indexOf("setReportPageSize") != -1))) {
						// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				}
				else if(
					hrefs[i].href != undefined 
							&&  ((hrefs[i].href.indexOf("prepareAddParticipant") != -1)
									|| (hrefs[i].href.indexOf("addMoneyType") != -1) 
									|| (hrefs[i].href.indexOf("addLoan") != -1)
									|| (hrefs[i].href.indexOf("setReportPageSize") != -1))) {
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
		
		if(uploadFormObj != null && uploadFormObj.showConfirmDialog.value ==  'true')
			confirmSubmitWithWarnings();
 }	

function validatePaymentInstructionInput(object) {

	//alert(object.name);
	var validColumn = true;
	var validRow = true;
	var fieldArray = null;
	var row = object.name.substring(object.name.indexOf("[")+1,object.name.indexOf("]"));

	if (object.value!="0.00") {
		var num = object.value.replace(/\$|\,/g,'');
		if(isNaN(num)) {
			alert("Invalid entry. Please enter only numeric values.");
			object.value="0.00";
		} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
			alert("Invalid payment amount. Only two decimal places are permitted.");
			object.value="0.00";
		} else if (num != Math.abs(num)) {
			alert(messageNegativeAmount);
			object.value="0.00";
		} else if (num > 99999999.99) {
			alert(messageMaxAmount);
			object.value="0.00";
		} else {
			object.value=formatCurrencyForInputTextBox(num);
		}
	}		 

	if(object.name.indexOf("bill")!=-1) {
		fieldArray = billFields;
	} else if(object.name.indexOf("credit")!=-1) {
		fieldArray = creditFields;
	} else {
		fieldArray = contributionFields;
	}
			
	for (var i=0; i<fieldArray.length && validColumn; i++) {
		var arrayObject = eval(fieldArray[i]);
		if (arrayObject.value!="0.00") {
				var num = arrayObject.value.replace(/\$|\,/g,'');
				if(isNaN(num) ||
				   (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) ||
				   (num != Math.abs(num)) ||
				   (num > 99999999.99)) {
					validColumn = false;
				} else {
					arrayObject.value=formatCurrencyForInputTextBox(num);
				}
			}
	}
	
	fieldArray = eval("accountsRow"+row);
	for (var i=0; i<fieldArray.length && validRow; i++) {
		var arrayObject = eval(fieldArray[i]);
		if (arrayObject.value!="0.00") {
				var num = arrayObject.value.replace(/\$|\,/g,'');
				if(isNaN(num) ||
				   (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) ||
				   (num != Math.abs(num)) ||
				   (num > 99999999.99)) {
					validRow = false;
				} else {
					arrayObject.value=formatCurrencyForInputTextBox(num);
				}
			}
	}

	// grand total
	if(!validColumn || !validRow) {
			var grandTotal = document.getElementById('grandTotal');
			if(grandTotal != null)  grandTotal.innerHTML ="NaN";
	} else {
			var grandTotal = document.getElementById('grandTotal');
			if(grandTotal != null)  grandTotal.innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(contributionFields) + calculateTotal(billFields) +calculateTotal(creditFields));
	}
	
	// column totals
	if(!validColumn) {
			if(object.name.indexOf("bill")!=-1) {
				document.getElementById('billPaymentTotal').innerHTML="NaN";
			} else if(object.name.indexOf("credit")!=-1) {
				document.getElementById('temporaryCreditTotal').innerHTML="NaN";
			} else {
				document.getElementById('contributionTotal').innerHTML="NaN";
			}
	} else {
			if(object.name.indexOf("bill")!=-1) {
				document.getElementById('billPaymentTotal').innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(billFields));
			} else if(object.name.indexOf("credit")!=-1) {
				document.getElementById('temporaryCreditTotal').innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(creditFields));
			} else {
				document.getElementById('contributionTotal').innerHTML = '$'+formatCurrencyForInputTextBox(calculateTotal(contributionFields));
			}
	}
	
	// row totals
	if (!validRow) {
			var rowTotal = document.getElementById('accountsRow'+row+'Total');
			if(rowTotal != null) rowTotal.innerHTML="NaN";
	} else {
			var rowTotal = document.getElementById('accountsRow'+row+'Total');
			if(rowTotal != null) rowTotal.innerHTML='$'+formatCurrencyForInputTextBox(calculateTotal(eval("accountsRow"+row)));
	}
}

function calculateTotal(fields) {
	if(fields==null || fields.length == 0) return 0;
	//alert('calculate total');
	var totalValue = 0;
	for (var i=0; i<fields.length; i++) {
		totalValue = totalValue + parseCurrencyInput((eval(fields[i])).value);
	}
	return totalValue;
}

function calculateTotalPaymentInstructions() {
	var fieldArray = contributionFields;
	if(billFields!=null) fieldArray = fieldArray.concat(billFields);
	if(creditFields!=null) fieldArray = fieldArray.concat(creditFields);
	return calculateTotal(fieldArray);
}

function validatePaymentInstructionInputs() {
	
	var fieldArray = contributionFields;
	if(billFields!=null) fieldArray = fieldArray.concat(billFields);
	if(creditFields!=null) fieldArray = fieldArray.concat(creditFields);
	
	for (var i=0; i<fieldArray.length; i++) {
		var object = eval(fieldArray[i]);
		if (object.value!="0.00") {
			var num = object.value.replace(/\$|\,/g,'');
			if(isNaN(num)) {
				writeError("Invalid entry. Please enter only numeric values.");
				object.value="0.00";
				return false;
			} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
				writeError("Invalid payment amount. Only two decimal places are permitted.");
				object.value="0.00";
				return false;
			} else if (num != Math.abs(num)) {
				writeError(messageNegativeAmount);
				object.value="0.00";
				return false;
			} else if (num > 99999999.99) {
				writeError(messageMaxAmount);
				object.value="0.00";
				return false;
			} else {
				object.value=formatCurrencyForInputTextBox(num);
			}
		}		 
	}
	//alert("All Fields are valid = " + isValid);
	return true;
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

	uploadFormObj = getFormByName("editContributionDetailsForm");
	
	// disable or enable the generate statements radio button if required
	if (uploadFormObj != null && uploadFormObj.moneySourceID != null && uploadFormObj.moneySourceID.value == 'REG') {
		disableLastquaterSelection(false);
	} else {
		disableLastquaterSelection(true);
	}
	// initialize all of the row and column totals for the payment section by
	// initiating the validate on the top row and left column
	
	// first the top row
	if (typeof(accountsRow0) != 'undefined') {
		var fieldArray = eval("accountsRow0");
		for (var i=0; i<fieldArray.length; i++) {
			var arrayObject = eval(fieldArray[i]);
			validatePaymentInstructionInput(arrayObject);
		}
	}
	
	// next the left column
	if (typeof(contributionFields) != 'undefined') {
		var fieldArray = eval("contributionFields");
		for (var i=0; i<fieldArray.length; i++) {
			var arrayObject = eval(fieldArray[i]);
			validatePaymentInstructionInput(arrayObject);
		}
	}

}

var payrollSelectedDate;
var payrollDateChecked = 'no';
function validatePayrollDateSelection() {
	//alert('validatePayrollDateSelection()');
	var str_date = uploadFormObj.payrollEffectiveDate.value;
	
	if (str_date.length == 0) {
		alert ("Invalid payroll date. Please use mm/dd/yyyy format or select a valid date from the calendar.");
		uploadFormObj.payrollEffectiveDate.value = payrollCal.gen_date(dt_defaultPayroll);
		payrollDateChecked = 'bad';
		return false;
	}

	var arr_date = str_date.split('/');
	if(isNaN(arr_date[0]) || isNaN(arr_date[1]) || isNaN(arr_date[2]) || arr_date[2].length != 4) {
		alert ("Invalid payroll date. Please use mm/dd/yyyy format or select a valid date from the calendar.");
		uploadFormObj.payrollEffectiveDate.value = payrollCal.gen_date(dt_defaultPayroll);
		payrollDateChecked = 'bad';
		return false;
	}
	// check the payroll date
	if(typeof(payrollCal) != 'undefined') {
		var result = payrollCal.prs_date(str_date, true);
		//alert('result: ' + result);
		if (result == null) {  
			uploadFormObj.payrollEffectiveDate.value = payrollCal.gen_date(dt_defaultPayroll);
			payrollDateChecked = 'bad';
			return false;
		}
	}	
	
	payrollSelectedDate = new Date(arr_date[2],parseInt(arr_date[0],10)-1,arr_date[1]);
	if (arr_date[2] < 2000) {
		alert ("Invalid payroll date selected: '" + str_date + "'.\nAllowed range is " +(this.dt_start.getMonth() +1) + "/" + this.dt_start.getDate() + "/" + this.dt_start.getFullYear()	+" to "	+(this.dt_end.getMonth() +1) + "/" + this.dt_end.getDate() + "/" + this.dt_end.getFullYear()	+ ".");
		uploadFormObj.payrollEffectiveDate.value = payrollCal.gen_date(dt_defaultPayroll);
		payrollDateChecked = 'bad';
		return false;
	}
	payrollDateChecked = 'ok';
	return true;
}

var paymentSelectedDate;
var paymentDateChecked = 'no';
function validatePaymentDateSelection() {

	if (isPaymentSectionShown) {
		//alert('Validating Date');

		var str_date = uploadFormObj.requestEffectiveDate.value;

		if (str_date.length == 0) {
			alert ("Invalid payment effective date. Please use mm/dd/yyyy format or select a valid date from the calendar.");
			uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
			paymentDateChecked = 'bad';
			return false;
		}

		var arr_date = str_date.split('/');
		if(isNaN(arr_date[0]) || isNaN(arr_date[1]) || isNaN(arr_date[2]) || arr_date[2].length != 4) {
			alert ("Invalid payment effective date. Please use mm/dd/yyyy format or select a valid date from the calendar.");
			uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
			paymentDateChecked = 'bad';
			return false;
		}

		if(typeof(cal) != 'undefined') {
			var result = cal.prs_date(str_date, true);
			if (result == null) {
				uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
				paymentDateChecked = 'bad';
				return false;
			}
		}

		paymentSelectedDate = new Date(arr_date[2],parseInt(arr_date[0],10)-1,arr_date[1]);
		if (arr_date[2] < 2000) {
			alert ("Invalid payment effective date selected: '" + str_date + "'.\nAllowed range is " +(this.dt_start.getMonth() +1) + "/" + this.dt_start.getDate() + "/" + this.dt_start.getFullYear()	+" to "	+(this.dt_end.getMonth() +1) + "/" + this.dt_end.getDate() + "/" + this.dt_end.getFullYear()	+ ".");
			uploadFormObj.requestEffectiveDate.value = cal.gen_date(dt_default);
			paymentDateChecked = 'bad';
			return false;
		}

	}

	paymentDateChecked = 'ok';
	return true;
}

function resetValues(){
	if (confirm('Are you sure you want to discard changes?')) {
		resetErrorCount();
		paymentSelectedDate="";
		writeError("");
		uploadFormObj.reset();
		location.href='#TopOfPage';
	}

	//reset all the total row and column fields
	if (contributionFields != null)
		validatePaymentInstructionInput(uploadFormObj.elements['amounts[0]']);
	if (billFields != null)
		validatePaymentInstructionInput(uploadFormObj.elements['billAmounts[0]']);
	if (creditFields != null)
		validatePaymentInstructionInput(uploadFormObj.elements['creditAmounts[0]']);
}

function isNullInput(str) {

	b = ((str==null) || (""==str));
	//alert("isNullInput returns " + b + "for '" + str + "'" );
	return b;
}

function validatePaymentInstructions() {
	var isValid = true;
	var totalValue = calculateTotalPaymentInstructions();

	if (totalValue < 0) {
		writeError(messagePaymentInfo);
		isValid = false;
	}

	if (isCashAccountPresent) { 

		if(typeof(cashAccountTotalBalance) != 'undefined' && calculateTotal(cashAccountRow) > cashAccountTotalBalance) {
			writeError(messageMaxCashValue);
			isValid = false;
		}

		if(totalValue > 0.0 && calculateTotal(cashAccountRow) == totalValue && paymentSelectedDate.valueOf() > defaultEffectiveDate.valueOf()) {
			writeError(messageFutureDated);
			isValid = false;
		}
		
	}

	if(isBillPaymentSectionShown) {
		if(calculateTotal(contributionFields) == 0 && calculateTotal(billFields) > 0) {
			writeError('Bill payment must be submitted with a contribution.  If you would like to submit a bill payment only, use the "Make a payment" function.');
			isValid = false;
		}
		if(typeof(outstandingBillPayment) != 'undefined' && calculateTotal(billFields) > outstandingBillPayment) {
			writeError(messageMaxBillAmount);
			isValid = false;
		}
	}
	
	if(isTemporaryCreditSectionShown) {
		if(calculateTotal(contributionFields) == 0 && calculateTotal(creditFields) > 0) {
			writeError('Temporary credit repayment must be submitted with a contribution.  If you would like to submit a temporary credit repayment only, use the "Make a payment" function.');
			isValid = false;
		}
		if(typeof(outstandingTemporaryCredit) != 'undefined' && calculateTotal(creditFields) > outstandingTemporaryCredit) {
			writeError(messageMaxTempCreditAmount);
			isValid = false;
		}
	}
	

	return isValid;
}

function validatePaymentSection() {
	if (isPaymentSectionShown) {
		//alert('payment section');
		var b1 = true;
		if (paymentDateChecked == 'bad') {
			b1 = false;
		} else if (paymentDateChecked == 'no') {
			b1 = validatePaymentDateSelection();
		}	
		//alert('validate date selection returns ' + b1);
		var b2 = validatePaymentInstructionInputs();
		//alert('validate payment instruction inputs returns ' + b2);
		var b3 = validatePaymentInstructions();
		//alert('validate payment instrutions returns ' + b3);
		return b1 && b2 && b3;
	} else {
		return true;
	}
}

function validateContractSection() {
	//alert('validateContractSection start');
	var eeTotal = 0;
	var erTotal = 0;
	var isValid = true;
	if (payrollDateChecked == 'no') {
		isValid = validatePayrollDateSelection();
	}	
	
	if (uploadFormObj.lastPayroll && uploadFormObj.lastPayroll[0].disabled != true) {
		if (uploadFormObj.lastPayroll[0].checked==false && uploadFormObj.lastPayroll[1].checked==false) {
			writeError(messageValidGenerateStmt);
			isValid = false;
		}
	}
		
	if(document.getElementById('eeParticipantTotal')!=null){
		eeTotal = parseCurrencyInput(document.getElementById('eeParticipantTotal').innerHTML);
	}
	if(document.getElementById('erParticipantTotal')!=null){
		erTotal = parseCurrencyInput(document.getElementById('erParticipantTotal').innerHTML);
	}
	var loanTotal = 0;
	if(document.getElementById('loanParticipantTotal') != null) {
		loanTotal = parseCurrencyInput(document.getElementById('loanParticipantTotal').innerHTML);
	}

	if (loanTotal == 0 ) {
		if ((eeTotal + erTotal) <= 0) {
			writeError('The total of your contribution submission must be greater than zero.');
			isValid = false;
		}
	} else {
		if ((eeTotal + erTotal) < 0) {
			writeError('The total of your contribution and / or loan repayment submission must be greater than zero.');
			isValid = false;
		}
	}

	var totalContributionsAndLoans = parseCurrencyInput(document.getElementById('totalContributionsAndLoans').innerHTML);
	var paymentContributionTotal = 0.0;
	
	if(document.getElementById('contributionTotal') != null)  
		paymentContributionTotal = parseCurrencyInput(document.getElementById('contributionTotal').innerHTML);

	// overpayment
	if (paymentContributionTotal != 0 && paymentContributionTotal > totalContributionsAndLoans) {
		if (!isInternalUser) {
			writeError('Payment amount is more than the total submission amount. Revise either the payment amount or submission details.');
			isValid = false;
		} else if (systemStatus != "14") {
			if (!confirm('Payment amount is more than the total submission amount. If you proceed and the payment is more than $0.98 greater than the submission total, the submission will be created in Apollo with the payment in "WA" status and a SIL will be generated.  Once the submission is on Apollo, you may either cancel the transaction or approve the payment.  Are you sure you want to proceed?')) {
				isValid = false;
			}
		}		
	}
	// underpayment
	if (paymentContributionTotal != 0 && paymentContributionTotal < totalContributionsAndLoans) {
		if (!isInternalUser) {
			writeError('Payment amount is less than the total submission amount. Revise either the payment amount or submission details.');
			isValid = false;
		} else if (systemStatus != "14") {
			if (!confirm('Payment amount is less than the total submission amount. If you proceed and the payment is more than $0.98 less than the submission total, the submission will be created in Apollo with the payment in "WA" status and a SIL will be generated.  Once the submission is on Apollo, you may either cancel the transaction or approve the payment.   Are you sure you want to proceed?')) {
				isValid = false;
			}
		}		
	}
	
	// max total contribution and loan amount
	if (totalContributionsAndLoans > 100000000.00) {
		writeError('Invalid total submission amount. Total is greater than allowable maximum of $99,999,999.99. Please ensure the submisison is a valid amount or contact your client account representative.');
		isValid = false;
	}
	//alert('return isValid: ' +isValid);
	return isValid;
}

function validateParticipantSection() {
	var isValid = true;
	var col = 0;
	
	
	while(document.getElementsByName('moneyTypeColumns['+col+']').length != 0) {
		if((document.getElementsByName('moneyTypeColumns['+col+']')[0].value == '-1')
				&& (document.getElementById("moneyFieldsColTotal"+col).innerHTML != '$0.00')
				&& (document.getElementById("moneyFieldsColTotal"+col).innerHTML != '$0.00 ')) {
			isValid = false;
		}
		col++;
	}
	
	if(!isValid) {
		submitted = false;
		submitInProgress = null;
		alert('Please select valid money types from the drop-down menus in the participant details section.');
	}
	return isValid;
}

function validateInputs() {

	var b1 = true;
	if (!isInternalUser) {
		b1 = validatePaymentSection();
		//alert('validate payment returns ' + b1);
	} else {
		if (systemStatus == "14" && paymentsExist) {
			if (!confirm('You are not authorized to provide payment instructions.  Please select "OK" to continue and any payment instructions previously provided will be removed.  If you would like to return to the submission, please select Cancel.')) {
				return false;
			}	
		}
	} 
	
	var b2 = validateContractSection();
	//alert('validate contract section returns ' + b2);
	var b = b1 && b2;
	//alert('validate inputs: ' + b);

	return b;
}

function confirmSend() {
	writeError("");
	resetErrorCount();
	//alert('confirm');
	if (buttonClicked.value == "undo" || buttonClicked.value == "cancel") {
		submitted = true;
		submitInProgress = true;
		return true;
	}

	if (payrollDateChecked == 'no') {
		//alert('payrollDateChecked is no in confirm');
		if(validatePayrollDateSelection() == false) {
			return false;
		}	
	} else {
		if (payrollDateChecked == 'bad') {
			payrollDateChecked = 'no';
			return false;
		} else {
			payrollDateChecked = 'no';
		}		
	}		
	
	if (paymentDateChecked == 'no') {
		//alert('paymentDateChecked is no in confirm');
		if (validatePaymentDateSelection() == false) {
			return false;
		}
	} else {		
		if (paymentDateChecked == 'bad') {
			paymentDateChecked = 'no';
			return false;
		} else {
			paymentDateChecked = 'no';
		}		
	}		
	
	if(deletingParticipants() == false) {
		return false;
	}	

	if(validateParticipantSection() == false) {
		return false;
	}	

	// we only check the dates and cofirm deleted participants for the save actions
	if (buttonClicked.value == "save") {
		if (isFormChanged() || document.forms['editContributionDetailsForm'].elements['forwardFromSave'].value != "") {
			writeError("");
			if (isInternalUser && systemStatus == "14" && paymentsExist) {
				if (confirm('You are not authorized to provide payment instructions.  Please select "OK" to continue and any payment instructions previously provided will be removed.  If you would like to return to the submission, please select Cancel.')) {
					submitted = true;
					submitInProgress = true;
					return true;
				} else {
					return false;
				}		
			} else {
				submitted = true;
				submitInProgress = true;
				return true;
			}	
		} else {
			writeError(messageNoChangesMade);
			return false;
		}		
	}

	if (validateInputs() == true) {
		var message;
		message = "";
		var paymentTotalValue = 0.00;
		
		if (uploadFormObj.ignoreDataCheckWarnings.value == 'true' &&
				uploadFormObj.showConfirmDialog.value == 'false') {
			uploadFormObj.showConfirmDialog.value = 'true';
			writeError("");
			submitted = true;
			submitInProgress = true;
			return true;
		}	

		//if (isFileSectionShown) { 
		
		if (isPaymentSectionShown) {
			paymentTotalValue = calculateTotalPaymentInstructions();

			if (isCashAccountPresent) {
				
				if (paymentTotalValue - calculateTotal(cashAccountRow) > 0.0 
						&& calculateTotal(cashAccountRow) > 0.0) {
					message = message+"You are using funds from your cash account. Contact your client account representative to provide details regarding which transactions to use.\n\n";
				}
			}
			
			if (calculateTotal(contributionFields) > 0 && calculateTotal(contributionFields) < 1) {
				message = message+"WARNING:  Your total is less than $1.00. Are you sure you want to proceed?\n\n";
			}

			if (contributionFields.length > 1 && calculateTotal(cashAccountRow) == paymentTotalValue) {
				message = message+"WARNING:  You have access to direct debit accounts but have not entered any payments. Are you sure you want to proceed?\n\n";
			}
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
		
		if (paymentTotalValue>0.001) {
			message = message+"You are about to make a submission for payroll date ending " + formatDate(payrollSelectedDate) + " with a total contribution amount of " + document.getElementById('totalContributionsAndLoans').innerHTML + ". You are also submitting a payment of " + formatCurrency(paymentTotalValue) + " with a payment effective date of " + formatDate(paymentSelectedDate) + ". Do you want to continue?";

			if(isBillPaymentSectionShown) {
				if(calculateTotal(billFields) > 0.0) {
					message = message+";\nBill payment of "+formatCurrency(calculateTotal(billFields))+ " will also be made.";
				}
			}
			if(isTemporaryCreditSectionShown) {
				if(calculateTotal(creditFields) > 0.0) {
					message = message+";\nTemporary Credit payment of "+formatCurrency(calculateTotal(creditFields))+ " will also be made.";
				}
			}
		} else {
			message = message+"You are about to make a submission for payroll date ending " + formatDate(payrollSelectedDate) + " with a total submission total of " + document.getElementById('totalContributionsAndLoans').innerHTML + ". Do you want to continue?";
		}

		if (confirm(message)) {
			writeError("");
			submitted = true;
			submitInProgress = true;
			return true;
		} else {
			return false;
		}
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

function validateParticipantInstructionsInput(object) {

	//alert(object.name);
	var fieldArray = null;
	var col = object.name.substring(object.name.indexOf("[")+1,object.name.indexOf("]"));
	var rowSubString = object.name.substring(object.name.indexOf("row[")+4);
	var row = rowSubString.substring(0,rowSubString.indexOf("]"));
	var allowNegative = false;
	var messageSubText = 'loan repayment';
	if(object.name.indexOf('contributionColumns') != -1) {
		allowNegative = true;
		messageSubText = 'allocation';
	}	
	
	if (object.value!="0.00") {
		var num = object.value.replace(/\$|\,/g,'');
		if(isNaN(num)) {
			alert("Invalid entry. Please enter only numeric values for " + messageSubText + " amount.");
			object.value="0.00";
		} else if (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) {
			alert("Invalid amount entered. Only two decimal places are permitted.");
			object.value="0.00";
		} else if (!allowNegative && num != Math.abs(num)) {
			alert("Loan repayment amount cannot be negative. If you are trying to correct a previous loan "
					+ "repayment contact your client account representative.");
			object.value="0.00";
		} else if (num > 9999999.99) {
			alert("Invalid " + messageSubText + " amount. Entry is greater than allowable maximum of $9,999,999.99. Please enter a valid amount or contact your client account representative for help.");
			object.value="0.00";
		} else if (num < -9999999.99) {
			alert("Invalid " + messageSubText + " amount. Entry is less than allowable minimum of $-9,999,999.99. Please enter a valid amount or contact your client account representative for help.");
			object.value="0.00";	
		} else {
			object.value=formatCurrencyForInputTextBox(num);
		}
	}		 

	if(object.name.indexOf("contributionColumns")!=-1) {
		fieldArray = eval("moneyFieldsCol"+col);
	} else if(object.name.indexOf("loanColumn")!=-1) {
		fieldArray = eval("loanFieldsCol"+col);
	}
	
//	for (var i=0; i<fieldArray.length; i++) {
//		var arrayObject = eval(fieldArray[i]);
//		if (arrayObject.value!="0.00") {
//				var num = arrayObject.value.replace(/\$|\,/g,'');
//				if(isNaN(num) ||
//				   (num.indexOf(".") != -1 && num.substring(num.indexOf(".")+1,num.length).length > 2) ||
//				   (num != Math.abs(num)) ||
//				   (num > 999999999.99)) {
//					validRow = false;
//				} else {
//					arrayObject.value=formatCurrencyForInputTextBox(num);
//				}
//			}
//	}
	
	// column totals
	if(object.name.indexOf("contributionColumns")!=-1) {
		document.getElementById("moneyFieldsColTotal"+col).innerHTML = formatCurrency(
			moneyFieldsColumnTotals[col] -
			moneyFieldsPageTotals[col] +
			calculateTotal(fieldArray)
		);
	} else if(object.name.indexOf("loanColumn")!=-1) {
		document.getElementById("loanFieldsColTotal"+col).innerHTML = formatCurrency(
			loanFieldsColumnTotals[col] -
			loanFieldsPageTotals[col] +
			calculateTotal(fieldArray)
		);
	}
	
	// row totals
	var wasZero = false;
	var rowTotal = document.getElementById('participantFieldsRowTotal'+row);
	var participantCount = document.getElementById('numberOfParticipants');
	if(rowTotal != null) {
		if (rowTotal.innerHTML == '$0.00') wasZero = true;
		rowTotal.innerHTML = formatCurrency(calculateTotal(eval("participantFieldsRow"+row)));
		if (wasZero && rowTotal.innerHTML != '$0.00') {
			numberOfParticipants++;
		} else {	
			if (!wasZero && rowTotal.innerHTML == '$0.00') {
				numberOfParticipants--;
			}	
		}
		if (participantCount != null) {
			participantCount.innerHTML = numberOfParticipants; 
		} 
	}		

	// populate summary totals accross the page
	populateParticipantTotals();
	
}

function populateParticipantTotals() {
	col = 0;
	var eeParticipantTotal = 0;
	var erParticipantTotal = 0;
	while (document.getElementById("moneyFieldsColTotal"+col) != null) {
		var moneyType = document.getElementById("moneyTypeHeader" + col).value;
		if (moneyType.substring(0,2) == "ER")
			erParticipantTotal = erParticipantTotal + parseCurrencyInput(document.getElementById("moneyFieldsColTotal"+col).innerHTML);
		else
			eeParticipantTotal = eeParticipantTotal + parseCurrencyInput(document.getElementById("moneyFieldsColTotal"+col).innerHTML);
		col++;
	}
	col = 0;
	var loanParticipantTotal = 0;
	while (document.getElementById("loanFieldsColTotal"+col) != null) {
		loanParticipantTotal = loanParticipantTotal + parseCurrencyInput(document.getElementById("loanFieldsColTotal"+col).innerHTML);
		col++;
	}

	
	// grand total
	var total = document.getElementById('participantFieldsGrandTotal');
	if(total != null)  total.innerHTML = formatCurrency(erParticipantTotal + eeParticipantTotal + loanParticipantTotal);

	total = document.getElementById('eeParticipantTotal');
	if(total != null)  total.innerHTML = formatCurrency(eeParticipantTotal);

	total = document.getElementById('erParticipantTotal');
	if(total != null)  total.innerHTML = formatCurrency(erParticipantTotal);

	total = document.getElementById('loanParticipantTotal');
	if(total != null)  total.innerHTML = formatCurrency(loanParticipantTotal);

	total = document.getElementById('totalContributionsAndLoans');
	if(total != null)  total.innerHTML = formatCurrency(erParticipantTotal + eeParticipantTotal + loanParticipantTotal);
}

function changingMoneySource(dropDown) {
	//alert(dropDown.value);
	if(dropDown.value != changeTracker.getOldValue('moneySourceID')) {
		if (submitted) {
			window.status = "Transaction already in progress.  Please wait.";
			dropDown.value = changeTracker.getOldValue('moneySourceID');
			return;
		} 
		if (confirm("You have requested to change the contribution type. The money types displayed will be refreshed for this new selection.\nWould you like to continue?")) {
			// yes save the money source change and get the new money types for this money source
			//submitted = true;
			document.getElementById('saveButton').click();		
		} else {
			dropDown.value = changeTracker.getOldValue('moneySourceID');
		}
	}
	
	if (dropDown.value == 'REG') {
		disableLastquaterSelection(false);
	} else {
		disableLastquaterSelection(true);
	}	

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
	// row totals
	var wasZero = false;
	var rowTotal = document.getElementById('participantFieldsRowTotal'+row);
	var participantCount = document.getElementById('numberOfParticipants');
	if(rowTotal != null) {
		if (rowTotal.innerHTML == '$0.00') wasZero = true;
		if (!wasZero && !checkBox.checked) {
			numberOfParticipants++;
		} else {	
			if (!wasZero && checkBox.checked) {
				numberOfParticipants--;
			}	
		}
		participantCount.innerHTML = numberOfParticipants; 
	}		
	

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
			// delete all the amounts from this row for validation to proceed
			for(i = 0; i < numParticipantsToDelete; i++) {	
				// set the value to 0.00 for each input field in the row and revalidate its dependencies for totals
				var fieldArray = eval("participantFieldsRow" + deletedRows[i]);
				if (fieldArray != null) {
					for (var j=0; j<fieldArray.length; j++) {
						var arrayObject = eval(fieldArray[j]);
						arrayObject.value="0.00";
						validateParticipantInstructionsInput(arrayObject);
					}
				}
			}
		} else {
			return false;
		}
	}
	return true;
}


function disableLastquaterSelection(o){
	if (uploadFormObj != null && uploadFormObj.lastPayroll != undefined) {
		if (o==true) {
			uploadFormObj.lastPayroll[0].checked=false;
			uploadFormObj.lastPayroll[1].checked=false;
		}
		uploadFormObj.lastPayroll[0].disabled=o;
		uploadFormObj.lastPayroll[1].disabled=o;
	}
}

function saveAndForward(forwardLocation) {

	if (submitted) {
		window.status = "Transaction already in progress.  Please wait.";
		return false;
	} else {
		document.forms['editContributionDetailsForm'].elements['forwardFromSave'].value = forwardLocation;
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

	document.forms['editContributionDetailsForm'].elements['forwardFromSave'].value = "setReportPageSize";
	buttonClicked = document.getElementById("saveButton");
	document.getElementById("saveButton").click();
	return;

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