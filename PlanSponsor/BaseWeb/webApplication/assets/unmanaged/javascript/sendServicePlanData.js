

// Contribution and Distribution Tab2 : Start

function allowMaxEmployeeRothDeferrals()
{
if(document.getElementById('planAllowRothDeferralsYes').checked)
{
document.getElementById('maxEmployeeRothDeferralsPct').disabled = false;
document.getElementById('maxEmployeeRothDeferralsAmt').disabled = false;

document.getElementById('rothWarningMessageId').style.display = "block";
}else if(document.getElementById('planAllowRothDeferralsNo').checked)
{
document.getElementById('maxEmployeeRothDeferralsPct').value = "";
document.getElementById('maxEmployeeRothDeferralsAmt').value = "";
document.getElementById('maxEmployeeRothDeferralsPct').disabled = true;
document.getElementById('maxEmployeeRothDeferralsAmt').disabled = true;
document.getElementById('rothWarningMessageId').style.display = "none";
}else
{
document.getElementById('maxEmployeeRothDeferralsPct').value = "";
document.getElementById('maxEmployeeRothDeferralsAmt').value = "";
document.getElementById('maxEmployeeRothDeferralsPct').disabled = true;
document.getElementById('maxEmployeeRothDeferralsAmt').disabled = true;
document.getElementById('rothWarningMessageId').style.display = "none";

}
}
function validateWholeNumber(field) {	
	var lowDigitNumber = 1;
	var num = field.value;
	if(num == ""){
		return false;
	}else if(isNaN(num)) {
        alert("The value should be a whole number.");
        field.value="";
        field.select();
        return false;
	} else if (num.indexOf(".") != -1 || (num < lowDigitNumber)) {
        alert("The value should be a whole number.");
        field.value="";
        field.select();
        return false;
	}
}
function validateEmployeeDeferralMax(field) {
	    return validatePlanDataNumberField(field, 0.1, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3, 1, ERR_INVALID_EMPLOYEE_DEFERRAL_MAX, ERR_INVALID_EMPLOYEE_DEFERRAL_MAX);
  }
   function validateEmployeeDeferralMaxDollar(field) {
	    return validatePlanDataNumberField(field, 0, 999999.99, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 6, 2, ERR_INVALID_VALUE, ERR_INVALID_VALUE);
  }  
  function validateEmployeeRothDeferralMax(field) {
	    return validatePlanDataNumberField(field, 0.1, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3, 1, ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX, ERR_INVALID_EMPLOYEE_ROTH_DEFERRAL_MAX);
  }
  function validateEmployeeRothDeferralMaxDollar(field) {
	    return validatePlanDataNumberField(field, 0, 999999.99, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 6, 2, ERR_INVALID_VALUE, ERR_INVALID_VALUE);
  }
  
  function validatePlanDataNumberField(field, min, max, invalidMsg, decimalMsg, highDigitNumber, lowDigitNumber, minMsg, maxMsg) {
    if (field.value.length == 0) {
      return true;
    }

	var num = field.value;
	
	/*
	 * Remove trailing zeros first.
	 */
	
	if(isNaN(num)) {
        alert(invalidMsg);
        field.value="";
        field.select();
        return true;
	}else if((num!=null)&&(num<0))
	{
	 alert(invalidMsg);
        field.value="";
        field.select();
        return true;
	
	}else if(num.indexOf(".") != -1 && ((num.substring(0,num.indexOf(".")).length) > highDigitNumber  || (num.substring(num.indexOf(".")+1,num.length).length) > lowDigitNumber)){	
        alert(decimalMsg);
        field.value="";
        field.select();
        return true;
	}else if (min != null && num < min) {
        alert(minMsg);
        field.value="";
        field.select();
        return true;
	} else if (max != null && num > max) {
        alert(maxMsg);
        field.value="";
        field.select();
        return true;
	} else {
	    return true;
	}
  }
  
function validateContributionTabData(){
var frm = document.noticePlanDataForm;	
var maxEmployeeBeforeTaxDeferralAmt= frm.maxEmployeeBeforeTaxDeferralAmt.value;
var maxEmployeeBeforeTaxDeferralPct= frm.maxEmployeeBeforeTaxDeferralPct.value;

if(null!=maxEmployeeBeforeTaxDeferralAmt&&maxEmployeeBeforeTaxDeferralAmt!='')
{
validateEmployeeDeferralMaxDollar(frm.maxEmployeeBeforeTaxDeferralAmt);

}
if(null!=maxEmployeeBeforeTaxDeferralPct&&maxEmployeeBeforeTaxDeferralPct!='')
{
validateEmployeeDeferralMax(frm.maxEmployeeBeforeTaxDeferralPct);
}
if(document.getElementById('planAllowRothDeferralsYes').checked==true)
{
var maxEmployeeRothDeferralsPct= frm.maxEmployeeRothDeferralsPct.value;
var maxEmployeeRothDeferralsAmt= frm.maxEmployeeRothDeferralsAmt.value;
if(null!=maxEmployeeRothDeferralsPct&&maxEmployeeRothDeferralsPct!='')
{
validateEmployeeRothDeferralMax(frm.maxEmployeeRothDeferralsPct);
}
if(null!=maxEmployeeRothDeferralsAmt&&maxEmployeeRothDeferralsAmt!='')
{
validateEmployeeRothDeferralMaxDollar(frm.maxEmployeeRothDeferralsAmt);
}
}
return true;
}
// Contribution and Distribution Tab2 : End

// Automatic Contribution Tab 4 : Start
function assignAutoTabValues()
{
var frm = document.noticePlanDataForm;

var qACAPlanHasSafeHarborMatchOrNonElective = frm.qACAPlanHasSafeHarborMatchOrNonElective.value;
	if(qACAPlanHasSafeHarborMatchOrNonElective == "SHMAC"){
		setRothValues();
		setCatchUpContributionValues();	
	}
	
}

function showOrHideACAFields(){
	var enable = false;
	var eacaEnable = false;
	var qacaEnable = false;
	var acaFieldsDivId = "#acaFieldsDiv";
	var eacaFieldsDivId = "#eacaFieldsDiv";
	var qacaFieldsDivId = "#qacaFieldsDiv";
	
	var frm = document.noticePlanDataForm;
	var contribtionsDistributionComplete = "${noticePlanDataForm.contriAndDistriDataCompleteInd}";

	var acaDisplayed= $("input[name='automaticContributionProvisionType']:checked").val();
	if (acaDisplayed == 'ACA') {
		enable = true;
	}

	if (enable == false) {
		$(acaFieldsDivId).show();
		$(eacaFieldsDivId).hide();
		$(qacaFieldsDivId).hide();
	} else {
		$(acaFieldsDivId).show();
	}  
	var eacaDisplayed= $("input[name='automaticContributionProvisionType']:checked").val();
	if (acaDisplayed == 'EACA') {
		eacaEnable = true;
	}
	if (eacaEnable == false) {   
		$(eacaFieldsDivId).hide();
	} else {
		if(contribtionsDistributionComplete == 'N' || contribtionsDistributionComplete == '' || contribtionsDistributionComplete == null){			
			$(acaFieldsDivId).show();
			$(eacaFieldsDivId).show();
			//return false;
		}else
		{
			$(acaFieldsDivId).show();
			$(eacaFieldsDivId).show();
		}
	}

	var qacaDisplayed= $("input[name='automaticContributionProvisionType']:checked").val();
	if (qacaDisplayed == 'QACA') {
		qacaEnable = true;
	}
	if (qacaEnable == false) {   
		$(qacaFieldsDivId).hide();   
	} else {
		if(contribtionsDistributionComplete == 'N' || contribtionsDistributionComplete == '' || contribtionsDistributionComplete == null){			
			$(acaFieldsDivId).show();   
			$(qacaFieldsDivId).show();
			//return false;
		}else
		{
			$(acaFieldsDivId).show();   
			$(qacaFieldsDivId).show();
		}
	}
}



function getValueFromRadioButton(name) {
	   //Get all elements with the name
	   var buttons = document.getElementsByName(name);
	   for(var i = 0; i < buttons.length; i++) {
	      //Check if button is checked
	      var button = buttons[i];
	      if(button.checked) {
	         //Return value
	         return button.value;
	      }
	   }
	   //No radio button is selected. 
	   return null;
	}


function showorhideACDOther()
{
	var acdOtherId = "#automaticContributionDaysOtherId";
	if(document.getElementById('automaticContributionDays').value=='00')
	{
		$(acdOtherId).show();
	}else
	{
		document.getElementById('automaticContributionDaysOther').value="";
		$(acdOtherId).hide();
	}
}


function showorhideQACDOther()
{
	var qacdOtherId = "#qACAAutomaticContributionDaysOtherId";
	if(document.getElementById('qACAAutomaticContributionDays').value=='00')
	{	
		$(qacdOtherId).show();
	}else
	{
		document.getElementById('qACAAutomaticContributionDaysOther').value="";   
		$(qacdOtherId).hide();
	}
}

function showSPDEmployerContributionRef()
{
	var enable=false;
	var spdEmployerContributionDivId = "#spdEmployerContributionRefDivId";
	$(spdEmployerContributionDivId).show();
	var spdDisplayed= $("input[name='employerContributions']:checked").val();
	if(spdDisplayed=='Y')
	{
		enable=true;
	}
	if(enable==true)
	{
		$(spdEmployerContributionDivId).show();

	}else
	{
		document.getElementById('spdEmployerContributionRef').value="";
		//$(spdEmployerContributionDivId).hide();
	}
}



function showorhideqACAAutomaticContributionDays()
{
	if(document.getElementById('qACAPlanHasAdditionalECYes').checked)
	{
		
		var qacdOtherId = "#qACAAutomaticContributionDaysOtherId";
		if(document.getElementById('qACAAutomaticContributionDays').value=='00')
		{
			$(qacdOtherId).show();
		}else
		{
			document.getElementById('qACAAutomaticContributionDaysOther').value="";    
			$(qacdOtherId).hide();
		}
	}
}


function enableContributionFeature2DateId()
{
	if(document.getElementById("automaticContributionFeature2").checked == true)
	{
		document.forms['noticePlanDataForm'].elements['automaticContributionFeature2'].value="2";
		
	}
	else
	{
		document.forms['noticePlanDataForm'].elements['automaticContributionFeature2'].value="";
		document.getElementById('contributionFeature2DateId').value = "";
	}
}


function enableContributionFeature1Pct()
{
	var frm = document.noticePlanDataForm;
	if(document.getElementById("automaticContributionFeature1").checked == true)
	{
		document.getElementById('automaticContributionFeature1').value="1";
	}
	else
	{
		document.getElementById('automaticContributionFeature1').value="";
		document.getElementById('contributionFeature1Pct').value = "";
	}
}

//If Roth or catchup is selected on Tab2 display, default to Yes and leave editable
//Do not display Roth or Catch up contribution questions if Roth or Catch up contribution = NO	
//Roth
function setRothValues(){
	var frm = document.noticePlanDataForm;
	var qACASafeHarborAppliesToRoth = frm.qACASafeHarborAppliesToRoth.value;
	var contriAndDistriRothDeferral = "${noticePlanDataForm.planAllowRothDeferrals}";
	if(qACASafeHarborAppliesToRoth!=null && qACASafeHarborAppliesToRoth!=""){
		document.getElementById("SafeHarborRothContributionDivId").style.display ="block";
		if(qACASafeHarborAppliesToRoth == 'Y'){
			document.getElementById("SHMAppliedToRothContributionYes").checked = true;
		}
		else{
			document.getElementById("SHMAppliedToRothContributionNo").checked = true;
		}
	}
	else {
		if(contriAndDistriRothDeferral!=null && contriAndDistriRothDeferral!=""){
			if(contriAndDistriRothDeferral == 'Y'){
				document.getElementById("SafeHarborRothContributionDivId").style.display ="block";
				document.getElementById("SHMAppliedToRothContributionYes").checked = true;
			}
			else{
				document.getElementById("SafeHarborRothContributionDivId").style.display ="none";
			}
		}
		else{
			document.getElementById("SafeHarborRothContributionDivId").style.display ="none";
		}
	}
}

//catchup
function setCatchUpContributionValues(){
	var frm = document.noticePlanDataForm;
	var qACASHAppliesToCatchUpContributions = frm.qACASHAppliesToCatchUpContributions.value;
	var catchUpContributionsAllowed = "${noticePlanCommonVO.catchUpContributionsAllowed}";
	
	if(qACASHAppliesToCatchUpContributions!=null && qACASHAppliesToCatchUpContributions!=""){
		
		document.getElementById("SafeHarborCatchUpContributionDivId").style.display ="block";
		if(qACASHAppliesToCatchUpContributions == 'Y'){			
			document.getElementById("SHMAppliedToCatchUpContributionYes").checked = true;
		} 
		else if(qACASHAppliesToCatchUpContributions == 'N'){
			document.getElementById("SHMAppliedToCatchUpContributionNo").checked = true;
		}
	 } 
	else{		
		if(catchUpContributionsAllowed!=null && catchUpContributionsAllowed!=""){
			if(catchUpContributionsAllowed == 'Y'){
				document.getElementById("SafeHarborCatchUpContributionDivId").style.display ="block";
				document.getElementById("SHMAppliedToCatchUpContributionYes").checked = true;
			}
			else if(catchUpContributionsAllowed == 'N'){
			
				document.getElementById("SafeHarborCatchUpContributionDivId").style.display ="none";
			}
		}
		else{
			document.getElementById("SafeHarborCatchUpContributionDivId").style.display ="none";
		}
	}
}


// UI validation Tab 4
 function  checkBoxBtatus(){
	
	var contributionFeature1PctMissing = document.getElementById("contributionFeature1PctMissing").value;
	var contributionFeature2DateIdMissing = document.getElementById("contributionFeature2DateIdMissing").value;
	var contributionFeature3SummaryTextMissing = document.getElementById("contributionFeature3SummaryTextMissing").value;
	
	 if(document.getElementById('contributionFeature1Pct').value !=null && document.getElementById('contributionFeature1Pct').value !="" ||
								contributionFeature1PctMissing=="true" )
	 {
		 document.getElementById('automaticContributionFeature1').value="1";
		 document.getElementById('automaticContributionFeature1').checked=true;
	 }
	 else{
		 document.getElementById('automaticContributionFeature1').value="0";
		 document.getElementById('automaticContributionFeature1').checked=false;
	 }
	 
	 if(document.getElementById('contributionFeature2DateId').value !=null && document.getElementById('contributionFeature2DateId').value !="" 
													|| contributionFeature2DateIdMissing=="true" )
	 {
		 document.getElementById('automaticContributionFeature2').value="2";
		 document.getElementById('automaticContributionFeature2').checked=true;
	 }
	 else{
		 document.getElementById('automaticContributionFeature2').checked=false;
	 }
	 
	 if(document.getElementById('contributionFeature3SummaryText').value !=null && document.getElementById('contributionFeature3SummaryText').value !="" || 
																		contributionFeature3SummaryTextMissing =="true")
	 {
		 document.getElementById('automaticContributionFeature3').value="3";
		 document.getElementById('automaticContributionFeature3').checked=true;
	 }
	 else{
		 document.getElementById('automaticContributionFeature3').checked=false;
	 }
}

//Automatic Contribution
function assignPctValues()
{
var pctValue=document.getElementById('qACAMatchContributionMatchPct1').value;
document.getElementById('qACAMatchContributionMatchPct1Value').value = pctValue;
}

 



function enableContributionFeature3SummaryText()
{
	if(document.getElementById("automaticContributionFeature3").checked == true)
	{
		document.forms['noticePlanDataForm'].elements['automaticContributionFeature3'].value="3";
	}
	else
	{
		document.forms['noticePlanDataForm'].elements['automaticContributionFeature3'].value="";
		document.getElementById('contributionFeature3SummaryText').value = "";
	}
} 

function placeSafeHarborVestingTableQACA() {
	   var safeHarborVestingTable = document.getElementById('safeHarborVestingTable');
	   var safeHarborSelected = document.getElementById('qACAPlanHasSafeHarborMatchOrNonElective').value;
	   var shMatchTable = document.getElementById('shMatchVestingTable');
	   var shNonElectiveTable = document.getElementById('shNonElectiveVestingTable');
	   
	   if(safeHarborSelected=='SHMAC') {
		   shMatchTable.rows[1].cells[0].appendChild(safeHarborVestingTable);
		   safeHarborVestingTable.style.display='block';
	   }
	   else if(safeHarborSelected=='SHNEC') {
		   shNonElectiveTable.rows[1].cells[0].appendChild(safeHarborVestingTable);
		   safeHarborVestingTable.style.display='block';
	   }
	   else if(safeHarborSelected=='') {
		   safeHarborVestingTable.style.display='none';
	   }
}
 
// Notice Plan Data Tab4 Validation Ends
// Automatic Contribution Tab 4: End