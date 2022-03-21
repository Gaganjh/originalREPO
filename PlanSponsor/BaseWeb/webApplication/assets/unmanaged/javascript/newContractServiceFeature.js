
function removeNotSelectedOption() { 

	var deferralType = document.getElementsByName('deferralType')[0];
	if(deferralType.options[0].value == "")
	{
		deferralType.options[0] = null;
		enableDeferralAmounts();
	}
}
function enableDeferralAmounts()
{
	var deferralType = document.getElementsByName('deferralType')[0];
	var selIndex = deferralType.selectedIndex;
	var deferralTypeValue = deferralType.options[selIndex].value;
	determineAutoSignup();
	if(deferralTypeValue == "")
	{
		// not selected - suppress plan limit and disable all aci fields
		hidePageElement('deferralLimitPercentAutoId');
		hidePageElement('deferralLimitPercentId');
		hidePageElement('deferralLimitDollarsId');
		// if autoContributionIncrease is in edit mode
		disableACIAttributes(true);
	}
	else 
	{
		var autoSingup = document.getElementsByName("aciSignupMethod")[0].value;
		if(deferralTypeValue == '%'){
		    if(autoSingup == 'A'){
				showPageElement('deferralLimitPercentAutoId');
				hidePageElement('deferralLimitPercentId');
			}else{
				hidePageElement('deferralLimitPercentAutoId');
				showPageElement('deferralLimitPercentId');
			}
			hidePageElement('deferralLimitDollarsId');
		}

		if(deferralTypeValue == '$'){
			
			showPageElement('deferralLimitDollarsId');
			
			hidePageElement('deferralLimitPercentAutoId');
			hidePageElement('deferralLimitPercentId');
						
		}

		if(deferralTypeValue == 'E')
		{
			
			if(autoSingup == 'A'){
				showPageElement('deferralLimitPercentAutoId');
				hidePageElement('deferralLimitPercentId');
			}else{
				hidePageElement('deferralLimitPercentAutoId');
				showPageElement('deferralLimitPercentId');
			}
			showPageElement('deferralLimitDollarsId');
			
		}
		disableACIAttributes(false);
		
	}
}
function disableACIAttributes(disabled)
{
	
	if(document.getElementsByName("autoContributionIncrease")[0] != null){
		document.getElementsByName("autoContributionIncrease")[0].disabled = disabled;
		document.getElementsByName("autoContributionIncrease")[1].disabled = disabled;
		if(document.getElementsByName("autoContributionIncrease")[2] != null)
			document.getElementsByName("autoContributionIncrease")[2].disabled = disabled;
	}
	
	document.getElementsByName("aciAnniversaryYear")[0].disabled = disabled;
	document.getElementsByName("payrollCutoff")[0].disabled = disabled;
	disableRadio("increaseAnniversary", disabled);

	if(disabled == false)
	{
		if(document.getElementsByName("isDeferralEZiDisabled")[0].value == 'true')
		{
			document.getElementsByName("aciAnniversaryYear")[0].disabled = true;
			document.getElementsByName("payrollCutoff")[0].disabled = true;
			disableRadio("increaseAnniversary", true);
		}
	}
}

function checkECMoneyTypes(){
	//alert('I am at eligiblility1');
	var disable=false;
	if (getRadioValue("eligibilityCalculationInd") == "No" ) {
		hidePageElement('eligibilityMoneyTypesId');
		
	}else{
		showPageElement('eligibilityMoneyTypesId');
 
		var checkEligibilityCalculationInd = document.getElementsByName("checkEligibilityCalculationInd")[0].value;
		
		if(checkEligibilityCalculationInd == "false"){
			
			// If above condition passes then it will check all the money types appears under EC service  
			var size=document.csfForm.eligibilityServiceMoneyTypesListSize.value;
			var element = document.getElementsByName("selectedMoneyTypes");
			
			for(var i=0;i<size;i++){
				element[i].checked=true;
			}
			document.getElementsByName("checkEligibilityCalculationInd")[0].value = "true";	
		}
	}
}

function checkMoneyTppeValue(moneyTypeBox, i) {
	if(!moneyTypeBox.checked){
		document.forms["csfForm"].elements["eligibityServiceMoneyTypeId["+i+"].moneyTypeValue"].value = "No";
	} else {
		document.forms["csfForm"].elements["eligibityServiceMoneyTypeId["+i+"].moneyTypeValue"].value = "Yes";
	}		
}

function checkAutoEnrollment() {
	//alert('checkAutoEnrollment');
	if (getRadioValue("autoEnrollInd") == "No") {
		setRadioValue("directMailInd", "No");
		hidePageElement('initialEnrollmentDateId');
		hidePageElement('directMailIndId');
	}else{
		showPageElement('initialEnrollmentDateId');
		showPageElement('directMailIndId');
	}
}

function checkSendService() {
	if (getRadioValue("noticeServiceInd") == "No") {
		setRadioValue("noticeServiceInd", "No");
		hidePageElement('noticeTypeSelected');
	}else{
		showPageElement('noticeTypeSelected');
	}
}

// edelivery for plan notices and statements
function checkNoiaStatus() {
	if (getRadioValue("noticeOfInternetAvailability") == "No") {
		setRadioValue("wiredAtWork", "No");		
	}
}

function checkPlanHighlights() {
	//alert('checkAutoEnrollment');
	if (getRadioValue("summaryPlanHighlightAvailable") == "No") {
		setRadioValue("summaryPlanHighlightReviewed", "No");
		hidePageElement('summaryPlanHighlightReviewed');
	}else{
		showPageElement('summaryPlanHighlightReviewed');
	}
}

function checkAutoContributionIncrease() {
	determineAutoSignup();
	var autoSingup = document.getElementsByName("aciSignupMethod")[0].value;
	
	if (autoSingup == "A" || autoSingup == "S") {
		document.getElementsByName("resetDeferralValues")[0].value = "false"
		showPageElement('defaultDeferralSection');
	} else {
		hidePageElement('defaultDeferralSection');
		document.getElementsByName("resetDeferralValues")[0].value = "true"
	}
	
	if (getRadioValue("autoContributionIncrease") == "No") {
		hidePageElement('aciAnniversaryId');
		hidePageElement('increaseAnniversaryId');
	}else{
	   	showPageElement('aciAnniversaryId');
		showPageElement('increaseAnniversaryId');
	}
	enableDeferralAmounts();
}

function checkVestingMethod() {
	var disabled = false;
	var vestingPercentagesElement = document.getElementsByName("vestingPercentagesMethod")[0];
	if (vestingPercentagesElement != null) {
		if (vestingPercentagesElement.value == "JHC") {
			//setRadioValue("vestingDataOnStatement", "Yes");
			showPageElement('vestingDataOnStatementId');
		} else if (vestingPercentagesElement.value == "TPAP") {
			//setRadioValue("vestingDataOnStatement", "Yes");
			showPageElement('vestingDataOnStatementId');
		} else if (vestingPercentagesElement.value == "NA") {
			hidePageElement('vestingDataOnStatementId');
			//setRadioValue("vestingDataOnStatement", "No");
		} else {
			setRadioValue("vestingDataOnStatement", "No");
		}
	}    
}

function checkIWithdrawals(businessConverted,checkPayableToCode) {
	var disabled = true;

	// if contract not business converted, disable withdrawalInd radio bottons
	if (businessConverted == false) {
		disableRadio("specialTaxNotice", disabled);
		disableRadio("whoWillReviewWithdrawals", disabled);
		disableRadio("creatorMayApproveInd", disabled);
		disableRadio("participantWithdrawalInd", disabled);
	}

	if (getRadioValue("withdrawalInd") == "Yes"
		&& checkPayableToCode == "TR" ) {
		disabled = true;
	}
}

function checkOnlineLoans(businessConverted,checkPayableToCode) {
	var disabled = true;

	// if contract not business converted, disable participantInitiateLoansInd radio buttons
	if (businessConverted == false) {
		disableRadio("allowOnlineLoans", disabled);
	}

	if (getRadioValue("allowOnlineLoans") == "Yes" ) {
		showPageElement('loansChecksMailedToId');
		showPageElement('whoWillReviewLoanRequestsId');
		showPageElement('creatorMayApproveLoanRequestsIndId');
		showPageElement('allowLoansPackageToGenerateId');

		if (getRadioValue("allowOnlineLoans") == "Yes" ) {
			disabled = false;
		}
		disableRadio("creatorMayApproveLoanRequestsInd", disabled);
		disableRadio("whoWillReviewLoanRequests", disabled);
		disableRadio("allowLoansPackageToGenerate", disabled);
//		disableRadio("participantInitiateLoansInd", disabled);

	}else{
		hidePageElement('loansChecksMailedToId');
		hidePageElement('whoWillReviewLoanRequestsId');
		hidePageElement('creatorMayApproveLoanRequestsIndId');
		hidePageElement('allowLoansPackageToGenerateId');
//		disableRadio("participantInitiateLoansInd", true);
	}

	if (getRadioValue("allowOnlineLoans") == "Yes"
		&& checkPayableToCode == "TR" ) {
		disabled = true;
	}
}

function checkDeferralOnlineItems() { 
	
	determineAutoSignup();
	var autoSingup = document.getElementsByName("aciSignupMethod")[0].value;
	if (autoSingup == "A" || autoSingup == "S") {
		showPageElement('defaultDeferralSection');
		document.getElementsByName("resetDeferralValues")[0].value = "false"
	}else{
		hidePageElement('defaultDeferralSection');
		document.getElementsByName("resetDeferralValues")[0].value = "true"
	}
	enableDeferralAmounts();
}

function hidePageElement(theElement) { 
	var element=document.getElementById(theElement); 
	if(element != null)
	{
		element.style.visibility='hidden'; 
		element.style.display='none'; 
	}
}

function showPageElement(theElement){
	var element=document.getElementById(theElement); 
	if(element != null)
	{
		element.style.visibility='visible'; 
		element.style.display=''; 
	}
}

function setRadioValue(radioName, newValue) {
	var radios = document.getElementsByName(radioName);
	for (var i = 0; radios != null && i < radios.length; ++i) {
		if (radios[i].type == 'radio' && radios[i].name == radioName) {
			radios[i].checked = (radios[i].value == newValue);
		}
	}
}		

function disableRadio(radioName, disable) {
	var radioButtons = document.getElementsByName(radioName);
	for (var i = 0; radioButtons != null && i < radioButtons.length; i++) {
		radioButtons[i].disabled = disable;
	}
}

var cal;

function setupCalendar(theDate) {
	var startDate = new Date();
	startDate.setDate(startDate.getDate() + 1);
	startDate.setHours(0);
	startDate.setMinutes(0);
	startDate.setSeconds(0);
	startDate.setMilliseconds(0);

	var endDate = new Date();
	endDate.setDate(startDate.getDate());
	endDate.setHours(23);
	endDate.setMinutes(59);
	endDate.setSeconds(59);
	endDate.setMilliseconds(999);
	endDate.setFullYear(startDate.getFullYear() + 1);

	var validDates = new Array();
	var nextDate = new Date(startDate);
	while(nextDate <= endDate) {
		var validDate = new Date(nextDate);
		validDates.push(validDate);
		nextDate.setDate(nextDate.getDate() + 1);
	}

	cal = new calendar(document.getElementsByName(theDate)[0], startDate.valueOf(), endDate.valueOf(), validDates);
	cal.year_scroll = false;
	cal.time_comp = false;
}

function popupCalendar(theDate) {

	var v = document.getElementsByName(theDate)[0].value;
	if (v == null || v.length == 0) {
		var today = new Date();
		today.setDate(today.getDate() + 1);
		document.getElementsByName(theDate)[0].value = formatDate(today, true);
	}
	setupCalendar(theDate);
	cal.popup();
}

function formatDate(dateVal, includeYear) {
	var month = dateVal.getMonth() + 1;
	var day = dateVal.getDate();
	var dateStr = (month<10 ? "0" + month : month) + "/" + (day<10 ? "0" + day : day) + (includeYear ? "/" + dateVal.getFullYear() : "");
	return dateStr;
}

function validateMonthDayYearDate(field) {
	if (field != null && field.value != "") {
		var parsedDate=cal_prs_date(field.value);
		if (parsedDate != null) {
			field.value=formatDate(parsedDate, true);
		}
	}
}

function isEmpty(s)
{ 
	return ((s == null) || (s.length == 0))
}
function checkFreezePeriod(element)
{

	var aciAnniversaryDate = document.csfForm.aciAnniversaryDate.value;

	if(!isEmpty(document.getElementsByName("aciAnniversaryYear")[0].value ) &&
			!isEmpty(document.getElementsByName("payrollCutoff")[0].value)){
		var parsedDate = cal_prs_monthday(aciAnniversaryDate, true);
		if(parsedDate != null)
		{
			var annivYear = parseInt(document.getElementsByName("aciAnniversaryYear")[0].value,10);
			if(annivYear != 'NaN' && annivYear > 999 && annivYear < 10000)
			{
				// find next anniversary
				if(parsedDate.getFullYear() < annivYear)
					parsedDate.setFullYear(annivYear);
				currentDate = new Date();
				var checkFreeze = true;
				if(currentDate.getTime() > parsedDate.getTime())
				{
					parsedDate.setFullYear(currentDate.getFullYear() + 1);

				}

				/*var makeChangeDays = parseInt(document.getElementsByName("payrollCutoff")[0].value,10);
				if(makeChangeDays != 'NaN')
				{
					currentDate.setDate(currentDate.getDate()+makeChangeDays);
					if(parsedDate.getTime() < currentDate.getTime())
					{
						element.value = "";
						if(element == document.getElementsByName("payrollCutoff")[0]){
							document.getElementsByName("optOutDeadlineDays")[0].value = "";
							alert("New days before anniversary is not valid due to freeze period");
						} else if(element == document.getElementsByName("optOutDeadlineDays")[0]) {
							document.getElementsByName("payrollCutoff")[0].value = "";
							alert("New opt-out days is not valid due to freeze period");
						}else {
							alert("New anniversary is not valid due to freeze period");
						}
					}
				}*/

				var ann_date = new Date();
				var str_date = aciAnniversaryDate
				var arr_date = str_date.split('/');
				ann_date.setFullYear(annivYear, arr_date[0]-1, arr_date[1]);

				var ann_date_only = ann_date;
				ann_date_only.setHours(0);
				ann_date_only.setMinutes(0);
				ann_date_only.setSeconds(0);
				ann_date_only.setMilliseconds(0);    			

				var now_date = new Date();
				if (ann_date.getYear() > (now_date.getYear()+2)) {
					alert("First scheduled increase starts on year cannot be more than 2 years in the future")
					document.getElementsByName("aciAnniversaryYear")[0].value = "";				
				} 
				if(ann_date < now_date){
					alert("First scheduled increase starts on cannot be in the past")
					document.getElementsByName("aciAnniversaryYear")[0].value = "";		
				}
			}
			else
			{
				alert("Enter a First scheduled increase starts on Value");
				document.getElementsByName("aciAnniversaryYear")[0].value = "";
			}
		}
		else
		{
			alert("First scheduled increase starts Can't be proceded as ACI anniversity Date is null");
			document.getElementsByName("aciAnniversaryYear")[0].value = "";
		}
	}

}
//Copied and modified from calendar.js
function cal_prs_monthday (str_date, suppressMessage) {
	var arr_date = str_date.split('/');

	if (arr_date.length != 2) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nFormat accepted is mm/dd."));

	if (!arr_date[1]) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nNo day of month value can be found."));
	if (!RE_NUM.exec(arr_date[1])) return (suppressMessage ? null : cal_error ("Invalid day of month value: '" + arr_date[1] + "'.\nValue must be numeric."));
	if (!arr_date[0]) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nNo month value can be found."));
	if (!RE_NUM.exec(arr_date[0])) return (suppressMessage ? null : cal_error ("Invalid month value: '" + arr_date[0] + "'.\nValue must be numeric."));

	if(arr_date[0].length > 2) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nFormat accepted is mm/dd."));
	if(arr_date[1].length > 2) return (suppressMessage ? null : cal_error ("Invalid date format: '" + str_date + "'.\nFormat accepted is mm/dd."));

	var dt_date = new Date();
	dt_date.setDate(1);

	if (arr_date[0] < 1 || arr_date[0] > 12) return (suppressMessage ? null : cal_error ("Invalid month value: '" + arr_date[0] + "'.\nAllowed range is 01-12."));
	dt_date.setMonth(arr_date[0]-1);

	var dt_numdays = new Date((new Date()).getYear(), arr_date[0], 0);
	dt_date.setDate(arr_date[1]);
	if (dt_date.getMonth() != (arr_date[0]-1)) return (suppressMessage ? null : alert ("Invalid day of month value: '" + arr_date[1] + "'.\nAllowed range is 01-"+dt_numdays.getDate()+"."));

	return (dt_date);
}
/* 
 * show/hide attributes for auto enrollment and auto contribution increase
 * */

function hideAttributes(theClass) { 
	var allTrTags=document.getElementsByTagName("tr"); 
	for (i=0; i<allTrTags.length; i++) { 
		if (allTrTags[i].className.match(theClass) != null) { 
			allTrTags[i].style.visibility='hidden'; 
			allTrTags[i].style.display='none'; 
		} 
	} 

}
function showAttributes(theClass){
	var allTrTags=document.getElementsByTagName("tr"); 
	for (i=0; i<allTrTags.length; i++) { 
		if (allTrTags[i].className.match(theClass) != null) { 
			allTrTags[i].style.visibility='visible'; 
			allTrTags[i].style.display=''; 
		} 
	} 
}
function getRadioValue(radioName) {
	var radioButtons = document.getElementsByName(radioName);
	if (radioButtons != null && radioButtons.length > 0) {
		var i=0;
		while (i < radioButtons.length && !radioButtons[i].checked) {
			i++;
		}
		if(i==radioButtons.length)
		{
			// no radio button is checked
			return null;
		}
		return radioButtons[i].value;
	}
}

function determineAutoSignup(){
	var planAciValue = document.getElementsByName("planAciInd")[0].value;
	var aciInd = getRadioValue("autoContributionIncrease");
	var cdoInd = getRadioValue("changeDeferralsOnline");
	
	if (getRadioValue("autoContributionIncrease") == "No" && 
			getRadioValue("changeDeferralsOnline") == "Yes" && planAciValue=="N")  {
		document.getElementsByName("aciSignupMethod")[0].value = "S";		
	}else	if (getRadioValue("autoContributionIncrease") == "Yes" && 
				getRadioValue("changeDeferralsOnline") == "Yes" && planAciValue=="Y")  {
		document.getElementsByName("aciSignupMethod")[0].value = "A";		
	}else{
		document.getElementsByName("aciSignupMethod")[0].value = "";		
	}
}