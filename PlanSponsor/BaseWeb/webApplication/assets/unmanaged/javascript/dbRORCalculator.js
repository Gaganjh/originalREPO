var isPageSubmitted = false;

window.onload = function() {
	if(document.getElementById("radio99").checked != true)
		disableDateFields();
}

function doBack() {
    document.RateOfReturnCalculatorForm.action.value = 'back';
    submitForm();
}

function doReset() {
    document.RateOfReturnCalculatorForm.action.value = 'reset';
    submitForm();
}

function doCalculate() {
    document.RateOfReturnCalculatorForm.action.value = 'calculate';
    submitForm();
}

function submitForm() {
    if (isPageSubmitted) {
        window.status = "Transaction already in progress.  Please wait.";
    } else {
        isPageSubmitted = true;
        document.RateOfReturnCalculatorForm.submit();
    }
}
    
function clearDateFields() {
    var frm = document.RateOfReturnCalculatorForm;
    document.getElementById("regFromDate").value="";
    document.getElementById("regToDate").value="";
	disableDateFields();
}

function enableDateFields() {
	var inputFromDate = document.getElementById("regFromDate");
	var inputToDate = document.getElementById("regToDate");
	inputFromDate.disabled=false;
	inputFromDate.style.backgroundColor = "rgb(235,235,228)";
    inputToDate.disabled=false;
	inputToDate.style.backgroundColor = "rgb(235,235,228)";
	enableCalendarPicker();
}

function disableDateFields() {
	var inputFromDate = document.getElementById("regFromDate");
	var inputToDate = document.getElementById("regToDate");
	inputFromDate.disabled=true;
	inputFromDate.style.backgroundColor = "rgb(200,200,200)";
    inputToDate.disabled=true;
	inputToDate.style.backgroundColor = "rgb(200,200,200)";
	disableCalendarPicker();
}


function enableCalendarPicker() {
	document.getElementById("calendarpicker1").firstChild.firstChild.disabled = false;
	document.getElementById("calendarpicker").firstChild.firstChild.disabled = false;
}

function disableCalendarPicker() {
	document.getElementById("calendarpicker1").firstChild.firstChild.disabled = true;
	document.getElementById("calendarpicker").firstChild.firstChild.disabled = true;
}

function checkDateFieldRadio() {
	document.getElementById("radio99").checked = true;
}

