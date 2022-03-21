function setDirtyFlag(){
	document.getElementById("dirtyFlagId").value = 'true';
}

//Investment Info: start

//when the no:of:days is changed, this method will be invoked
function transferOutDaysChanged(){
	var frm = document.tabPlanDataForm;
	var transferOutDaysCode = frm.transferOutDaysCode.value;
	setDirtyFlag();
	if(transferOutDaysCode == '30'){
		document.getElementById('custom').style.display ="none";
		document.getElementById('nonCustom').style.display ="block";
	}
	if(transferOutDaysCode == '60'){
		document.getElementById('custom').style.display ="none";
		document.getElementById('nonCustom').style.display ="block";
	}
	if(transferOutDaysCode == '90'){
		document.getElementById('custom').style.display ="none";
		document.getElementById('nonCustom').style.display ="block";
	}
	if(transferOutDaysCode == '00'){
		document.getElementById('custom').style.display ="block";
		document.getElementById('nonCustom').style.display ="none";
		document.getElementById('transferOutDaysCustom').value='';
	}	
}

//Validate no:of:days on onblur event
function validateNoOfDays(field){
	var days = field.value;
	setDirtyFlag();
	//when not empty and not null
	if(days != null && days != ''){
		validate(field);
	}
}

function validate(field){
	var days = field.value;
	//when non-numeric
	if(isNaN(days)) {
		alert(ERR_SH_CONTRI_PCT_NON_NUMERIC_INVALID);
		field.value="";
        field.select();
        return false;
	}
	//when number is decimal
	else if(days.indexOf(".") != -1){
		alert(ERR_SH_CONTRI_PCT_NON_NUMERIC_INVALID);
		field.value="";
        field.select();
        return false;
	}
}
//Investment Info: end

//Safe Harbor: start
//To validate SH Match Contribution row1 percent values
function validatePctRow1(field, matchContr){
	var pct = field.value;
	var frm = document.tabPlanDataForm;
	setDirtyFlag();
	if(pct != null && pct !="" && pct != 0.0){		
		if(validateContributionPctValues(field, 0.1, 100.0, matchContr)){
			return false;
		}
	}
}

//To validate SH match contribution row2 percent values and SH Non-elective percent values 
function validatePct(field, contribution){
	var pct = field.value;
	setDirtyFlag();
	if(pct !=null && pct !="" && pct!= 0.0){
		if(validateContributionPctValues(field, 0.1, 100.0, contribution)){
			return false;
		}
	}
}

//To validate SH Non-elective percent values 
function validateNE(field, contribution){
	var pct = field.value;
	setDirtyFlag();
	if(pct !=null && pct !=""){
		if(validateContributionPctValues(field, 0.1, 100.0, contribution)){
			return false;
		}
	}
}
//to validate contribution percent values for SH match and SH non-elective
function validateContributionPctValues(field, min, max, contribution){
	var num = field.value;
	if(isNaN(num)) {
		alert(ERR_SH_CONTRI_PCT_NON_NUMERIC_INVALID);
		field.value='';
		field.select();
		return true;
	}else if(num.indexOf(".") != -1 && (num.substring(0,num.indexOf(".")).length > 3  || num.substring(num.indexOf(".")+1,num.length).length > 1)){
		alert(ERR_SH_CONTRI_PCT_NON_NUMERIC_INVALID);
		field.value='';
		field.select();
		return true;
	}else if(num < min || num > max){
		if(contribution == 'row1col1'){
			alert(ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL1);
		}
		else if(contribution == 'row1col2'){
			alert(ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW1_COL2);
		}
		else if(contribution == 'row2col1'){
			alert(ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL1);
		}
		else if(contribution == 'row2col2'){
			alert(ERR_SH_CONTRI_PCT_NUMERIC_INVALID_MATCH_PCT_ROW2_COL2);
		}
		else if(contribution == 'ne'){
			alert(ERR_SH_CONTRI_PCT_NUMERIC_INVALID_NE_PCT);
		}
		field.value='';
		field.select();
		return true;
	}else{
		if(contribution == 'row1col2'){
			if(num == 4 || num > 4){
				document.getElementById('matchContributionContribPct2').value='';
				document.getElementById('matchContributionMatchPct2').value='';
			}
		}
		return false;
	}
}



//To validate SH tab data on click of save button
function validateSafeHarborData(){
	var frm = document.tabPlanDataForm;
	//popup error message when numeric value of automaticContributionDaysOther is less than 30 or greater than 90
	var otherDaysValue =frm.SHAutomaticContributionDaysOther.value;
	var min = 30;
	var max = 90;	
	if(document.getElementById('automaticContributionDays').value=='00')
	{
		if(otherDaysValue==null||otherDaysValue=='')
		{
		
		}else if(min!="" && otherDaysValue < min){
		alert(ERR_AUTO_TAB_NO_OF_DAYS_INVALID);
		document.getElementById('automaticContributionDaysOther').value="";
        return true;
		}
		else if(max!="" && otherDaysValue > max){
		alert(ERR_AUTO_TAB_NO_OF_DAYS_INVALID);
		document.getElementById('automaticContributionDaysOther').value="";
        return true;
		}
	}

	
	//popup error message when all money types are excluded
	var vestingCount = $('.excludeCount').val();
	var excludedCount = $('.moneyTypeIDIndval:checked').length;
	if($('input[name="planHasAdditionalEC"]:checked').val() == 'Y') {
		if(vestingCount == excludedCount){	
			alert(ERR_EXCLUDE_ALL_MONEY_TYPE);
			return true;
		}
	}
	
	
	//display message when the match contribution row1 pct values are empty/null
	var row1pct1 = frm.matchContributionContribPct1.value;
	var row1pct2 = frm.matchContributionMatchPct1.value;
	if(row1pct1 == null || row1pct1 == '' || row1pct2 == null || row1pct2 == '' || row1pct1 == 0.0 || row1pct2 == 0.0){
		alert(MISSING_VALUES_MESS);
		return true;
	}	
	return false;
}



//Safe Harbor: End

// Contribution and Distribution Tab2 : Start

function allowMaxEmployeeRothDeferrals()
{
	if(document.getElementById('planAllowRothDeferralsYes').checked || document.getElementById('planAllowRothDeferralsNo').checked)
	{
	document.getElementById('rothWarningMessageId').style.display = "block";
	}else
	{
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
	    return validatePlanDataNumberField(field, 0.01, 999999.99, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 6, 2, ERR_INVALID_VALUE, ERR_INVALID_VALUE);
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
var frm = document.tabPlanDataForm;	
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
// Notice Plan Data Tab4 Validation Starts
  function validateDefaultAnnualIncrease(field, ind) {
  setDirtyFlag();
	  if(ind == 'Y') {
		  return validateNumberField(field, 1, 100, ERR_INVALID_DEFUALT_ANNUAL_INCREASE, ERR_INVALID_DEFUALT_ANNUAL_INCREASE, 0, ERR_INVALID_DEFUALT_ANNUAL_INCREASE, ERR_INVALID_DEFUALT_ANNUAL_INCREASE);
	  } else {
		  return validateNumberField(field, 1, 100, null, null, 0, null, null);
	  }
  }
  function validateDefaultAnnualIncreaseWithDecimal(field, ind) {
  setDirtyFlag();
	  if(ind == 'Y') {
	    return validateNumberField(field, 1, 100, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL, 2, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUALT_ANNUAL_INCREASE_WITH_DECIMAL);
	  } else {
		return validateNumberField(field, 1, 100, null, null, 2, null, null);
	  }
  }   
  function validateQACAMatchingContributions(field, ind) {
   //setDirtyFlag();
   var pct = field.value;
	/*if(pct == null || pct == '' || pct == 0.0){
		alert(MISSING_VALUES_MESS);
		field.select();
        return false;
	}
	else{*/
	
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.0, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE,3, 1, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL1, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL1);
	  } else {
		return validatePlanDataNumberField(field, 0.0, 100.0, null, null, 3,1, null, null);
	  }
	 // }
  }
   function validateQACAMatchingContributionsPctValues(field, ind) {
   //setDirtyFlag();
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.0, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3,1, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL1, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL1);
	  } else {
		return validatePlanDataNumberField(field, 0.0, 100.0, null, null,3, 1, null, null);
	  }
	  
  }
   function validateQACAMatchingContributionsPercentValues(field, ind) {
   //setDirtyFlag();
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.0, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3,1, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL2, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW2_COL2);
	  } else {
		return validatePlanDataNumberField(field, 0.0, 100.0, null, null, 3,1, null, null);
	  }
	  
  }
   
  function populateQACASafeHarborRow2Column2() {
	  
	  var row1Column2 = document.getElementById('qACAMatchContributionMatchPct1');
	  var row2Column1 = document.getElementById('qACAMatchContributionContribPct2');
	  var row2Column2 = document.getElementById('qACAMatchContributionMatchPct2');
	  
	  if(row2Column1.value!='' || row2Column2.value!=''){
		  document.getElementById('qACAMatchContributionMatchPct1Value').value = row1Column2.value;
	  }
	 
  }
  function validateQACAMatchingContributionsValues(field, ind) {
   //setDirtyFlag();
   var pct = field.value;
   var matchContribPct2=document.getElementById('qACAMatchContributionContribPct2').value;
	var matchPct2=document.getElementById('qACAMatchContributionMatchPct2').value;
	/*if(pct == null || pct == '' || pct == 0.0){
		alert(MISSING_VALUES_MESS);
		field.select();
        return false;
	}
	else{*/
	  if(ind == 'Y') {	
	   if(pct == 4 || pct > 4){	
		   document.getElementById('qACAMatchContributionContribPct2').value = '';
		   document.getElementById('qACAMatchContributionMatchPct1Value').value = '';
		   document.getElementById('qACAMatchContributionMatchPct2').value = '';
	   		/*if((matchContribPct2==null || matchContribPct2=='') && (matchPct2==null || matchPct2==''))
	   		{    	
				document.getElementById('qACAMatchContributionMatchPct1Value').value='';
			}  */
	    return validatePlanDataNumberField(field, 0.0, 100.0,ERR_INVALID_VALUE , ERR_INVALID_VALUE,3, 1, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL2, ERR_SH_CONTRI_PCT_MAX_INVALID_MATCH_PCT_ROW1_COL2);
	    }
	   
	  } else {
		return validatePlanDataNumberField(field, 0.0, 100.0, null, null, 3,1, null, null);
	  }
	 // }
  }
   function validateSHMatchVesting1(field, ind) {
   //setDirtyFlag();
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.0, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3,1, ERR_INVALID_QACA_SH_MATCH_VESTING1, ERR_INVALID_QACA_SH_MATCH_VESTING1);
	  } else {
		return validatePlanDataNumberField(field, 0.0, 100.0, null, null,3, 1, null, null);
	  }
  }
  function validateSHMatchVesting2(field, ind) {
   //setDirtyFlag();
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.0, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3,1, ERR_INVALID_QACA_SH_MATCH_VESTING2, ERR_INVALID_QACA_SH_MATCH_VESTING2);
	  } else {
		return validatePlanDataNumberField(field, 0.0, 100.0, null, null, 3,1, null, null);
	  }
  }
  function validateQACASHNonElective(field, ind) {
  //setDirtyFlag();
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.0, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3,1, ERR_SH_CONTRI_PCT_NUMERIC_INVALID_NE_PCT, ERR_SH_CONTRI_PCT_NUMERIC_INVALID_NE_PCT);
	  } else {
		return validatePlanDataNumberField(field, 0.0, 100.0, null, null,3, 1, null, null);
	  }
  }
   function validateAnnualIncreaseWithDecimal(field, ind) {
   //setDirtyFlag();
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.1, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3,1, ERR_INVALID_ANNUAL_INCREASE_WITH_DECIMAL, ERR_INVALID_ANNUAL_INCREASE_WITH_DECIMAL);
	  } else {
		return validatePlanDataNumberField(field, 0.1, 100.0, null, null,3, 1, null, null);
	  }
  } 
  function validateContributionLessDecimal(field, ind) {
   //setDirtyFlag();
	  if(ind == 'Y') {
	    return validatePlanDataNumberField(field, 0.1, 100.0, ERR_INVALID_VALUE, ERR_INVALID_VALUE, 3,1, ERR_INVALID_CONTRIBUTION_LESS_PCT, ERR_INVALID_CONTRIBUTION_LESS_PCT);
	  } else {
		return validatePlanDataNumberField(field, 0.1, 100.0, null, null, 3,1, null, null);
	  }
  }  
   function validateDefaultMaximumAutoIncrease(field, ind) {
	if(ind == 'Y') {  
	  return validatePlanDataNumberField(field, 1, 100, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE, 0, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE);
	} else {
		  return validatePlanDataNumberField(field, 1, 100, null, null, 0, null, null);
	}
  }
  function validateDefaultMaximumAutoIncreaseWithDecimal(field, ind) {
	if(ind == 'Y') {     
	  return validatePlanDataNumberField(field, 1, 100, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL, 2, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL, ERR_INVALID_DEFUAL_MAXIMUM_INCREASE_WITH_DECIMAL);
	} else {
	  return validatePlanDataNumberField(field, 1, 100, null, null, 3,2, null, null);
    }
  }
  function validateFullyVestedContributionPct(field, ind) {
		if(ind == 'Y') {     
		  return validatePlanDataNumberField(field, 0.1, 100, ERR_SH_INVALID_PCT, ERR_SH_INVALID_PCT, 3, 1, ERR_SH_MIN_MAX_PCT, ERR_SH_MIN_MAX_PCT);
		} else {
		  return validatePlanDataNumberField(field, 1, 100, null, null, 3,2, null, null);
	    }
	  }
  
  function validateHireAfterDate(field) {
    return validateField(field, new Array(validateDate), new Array(ERR_INVALID_HIRE_AFTER_DATE), true)
  }
  
  function validateEffectivePlanYearEndDate(field) {
	  return validateField(field, new Array(validateDate), new Array(ERR_SH_INVALID_DATE), true)
  }
  
  
  
 function validateOtherDays(field){
	var days = field.value;
	var lowDigitNumber = 1;		
	if(days !="" && isNaN(days)) {
		alert(ERR_INVALID_VALUE);
		field.value="";
        return true;
	}	
	else if(days !="" && days.indexOf(".") != -1 ){	
		alert(ERR_INVALID_VALUE);
		field.value="";
        return true;
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
 document.getElementById('spdEmployerContributionRef').disabled=false;
 document.getElementById("vestingEACASpan").style.display ="block";

}else
{
document.getElementById('spdEmployerContributionRef').value="";
document.getElementById('spdEmployerContributionRef').disabled=true; 
document.getElementById("vestingEACASpan").style.display ="none";
 //$(spdEmployerContributionDivId).hide();
}
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
document.getElementById('qACAAutomaticContributionDaysOther').disabled = false;
  $(qacdOtherId).show();
}else
{
  document.getElementById('qACAAutomaticContributionDaysOther').value="";   
  $(qacdOtherId).hide();
}
}
function verifySHCompleted()
{
var frm = document.tabPlanDataForm;
var shComplete =frm.safeHarborDataCompleteInd.value;
shComplete ="Y";

var contributionComplete =frm.contriAndDistriDataCompleteInd.value;
var serviceSelected= frm.noticeServiceInd.value;
var noticeType=frm.noticeTypeSelected.value;
var acaDisplayed= $("input[name='automaticContributionProvisionType']:checked").val();
  if(acaDisplayed == 'EACA' || acaDisplayed == 'QACA') {
    if(serviceSelected == 'Yes' && (noticeType == '2' || noticeType  == '3') && (contributionComplete == 'N' || contributionComplete == '' || contributionComplete == null)){			
	alert(CONTRIB_AND_DISTRIB_NOT_COMPLETED_MSG);
	$("#automaticContributionProvisionType1").attr('checked', 'checked');
	document.getElementById("dirtyFlagId").value = 'false';
	return false;
   }else
   {
   switchEACAQACA();
   showOrHideACAFields();
   }
 }
  if(acaDisplayed == 'ACA')
  {
    switchEACAQACA();
    showOrHideACAFields();
  }
  goToUrl('automaticContribution','refresh');

}
function confirmSwitchAutoOptions(message){
	return window.confirm(message);	
}

function switchEACAQACA()
{
// TODO replace with new CMA key for TPDD 260
var message=AC_PROVISION_TYPE_CHANGE;
var frm = document.tabPlanDataForm;
var oldValue=frm.automaticContributionProvisionTypeHidden.value;
  var current = $("input[name='automaticContributionProvisionType']:checked").val();
  
    if(oldValue != null && oldValue != '' && oldValue != current && oldValue != 'ACA')
    {
	if(confirmSwitchAutoOptions(message)){
	}else
	{
	if (oldValue == 'ACA') {
 	 $("#automaticContributionProvisionType1").attr('checked', 'checked');
 	 }
  	if (oldValue == 'EACA') {
  	$("#automaticContributionProvisionType2").attr('checked', 'checked');
 	 }
  	if (oldValue == 'QACA') {
  	$("#automaticContributionProvisionType3").attr('checked', 'checked');
 	 }
	}	
	}

}
function showOrHideACAFields()
{

 var enable = false;
 var eacaEnable = false;
 var qacaEnable = false;
 var acaFieldsDivId = "#acaFieldsDiv";
 var eacaFieldsDivId = "#eacaFieldsDiv";
 var qacaFieldsDivId = "#qacaFieldsDiv";
 var frm = document.tabPlanDataForm;
	
	var contribtionsDistributionComplete = "${tabPlanDataForm.contriAndDistriDataCompleteInd}";
	
		
	
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


//Auto Tab
//this function is called on click of Auto contribution save button
function validateAutoTabData(){	
	
		
	//popup error message when all money types are excluded
	var vestingCount = $('.excludeCount').val();	
	var selectedVesting= $("input[name='automaticContributionProvisionType']:checked").val();
	if (selectedVesting == 'QACA') {	
		var frm = document.tabPlanDataForm;
		 var row1pct1 = frm.qACAMatchContributionContribPct1.value;
			var row1pct2 = frm.qACAMatchContributionMatchPct1.value;
			if(row1pct1 == null || row1pct1 == '' || row1pct2 == null || row1pct2 == '' || row1pct1 == 0.0 || row1pct2 == 0.0){
				alert(MISSING_VALUES_MESS);
				return true;
			}	
	 }
	var excludedCount ;
	var employerContributionsSelected = false;
	  if (selectedVesting == 'EACA') {		  
			excludedCount = $('.moneyTypeIDIndvalEaca:checked').length;
			employerContributionsSelected = $('input[name="employerContributions"]:checked').val() == 'Y';
			}
	  
	  if (selectedVesting == 'QACA') {		  
			excludedCount = $('.moneyTypeIDIndvalQaca:checked').length;
			employerContributionsSelected = $('input[name="qACAPlanHasAdditionalECon"]:checked').val() == 'Y';
			}
	  if(selectedVesting != 'ACA')
	  {
	  if(employerContributionsSelected && vestingCount == excludedCount){	
			alert(ERR_EXCLUDE_ALL_MONEY_TYPE);
			return true;
	  }
	}
	
var frm = document.tabPlanDataForm;	
var acaDisplayed= $("input[name='automaticContributionProvisionType']:checked").val();
if(acaDisplayed==null || acaDisplayed=="")
{
	alert(ERR_AUTO_CONTRIBUTION_TYPE_NOT_SELECTED);
		return true;
}else
{

var checkboxValue=$("input[name='automaticContributionFeature1']:checked").val();
//alert(document.getElementById('automaticContributionFeature1').checked);
var lessDecimalValue='';
if(document.getElementById('automaticContributionFeature1').checked == true)
{	
lessDecimalValue=frm.contributionFeature1Pct.value;
}
if(null!=lessDecimalValue && lessDecimalValue!='')
{
validateContributionLessDecimal(frm.contributionFeature1Pct, 'Y');
}
if(document.getElementById('automaticContributionFeature2').checked == true)
{
var contributionFeature2DateValue=frm.contributionFeature2Date.value;
if(null!=contributionFeature2DateValue && contributionFeature2DateValue!='')
{	
//validateHireAfterDate(frm.contributionFeature2Date);
}
}

}


	if(acaDisplayed=='EACA')
	{
	var otherDaysValue =frm.automaticContributionDaysOther.value;
	var min = 30;
	var max = 90;	
	if(document.getElementById('automaticContributionDays').value=='00')
	{
	
		if(otherDaysValue==null||otherDaysValue=='')
		{
		//alert(MISSING_VALUES_MESS);
		//return true;
	}else if(min != null && min!="" && otherDaysValue < min){
		alert(ERR_AUTO_TAB_NO_OF_DAYS_INVALID);
		document.getElementById('automaticContributionDaysOther').value="";
        return true;
	}
	else if(max != null && max!="" && otherDaysValue > max){
		alert(ERR_AUTO_TAB_NO_OF_DAYS_INVALID);
		document.getElementById('automaticContributionDaysOther').value="";
        return true;
	}
	}
	}
	var qACAPlanHasSafeHarborMatchOrNonElective = frm.qACAPlanHasSafeHarborMatchOrNonElective.value;
	if(acaDisplayed=='QACA')
	{
	var min = 30;
	var max = 90;
	var QacaOtherDaysValue =frm.qACAAutomaticContributionDaysOther.value;		
	if(document.getElementById('qACAAutomaticContributionDays').value=='00')
	{
		if(QacaOtherDaysValue==null||QacaOtherDaysValue=='')
		{
		//alert(MISSING_VALUES_MESS);
		//return true;
		
	}else if(min != null && min!="" && QacaOtherDaysValue < min){
		alert(ERR_AUTO_TAB_NO_OF_DAYS_INVALID);
		document.getElementById('qACAAutomaticContributionDaysOther').value="";
        return true;
	}
	else if(max != null && max!="" && QacaOtherDaysValue > max){
		alert(ERR_AUTO_TAB_NO_OF_DAYS_INVALID);
		document.getElementById('qACAAutomaticContributionDaysOther').value="";
        return true;
	}

	if(qACAPlanHasSafeHarborMatchOrNonElective == "SHMAC"){
	
	var row1pct1 = frm.qACAMatchContributionContribPct1.value;
	var row1pct2 = frm.qACAMatchContributionMatchPct1.value;
	if(row1pct1 == null || row1pct1 == '' || row1pct1 == 0.0 || row1pct2 == null || row1pct2 == '' || row1pct2 == 0.0){
		alert(MISSING_VALUES_MESS);
		return true;
	}else
	{	
	validateQACAMatchingContributions(frm.qACAMatchContributionContribPct1, 'Y');
	//assignPctValues();
	//validateQACAMatchingContributionsValues(frm.qACAMatchContributionMatchPct1, 'Y');
	validateQACAMatchingContributionsPctValues(frm.qACAMatchContributionContribPct2, 'Y');
	validateQACAMatchingContributionsPercentValues(frm.qACAMatchContributionMatchPct2, 'Y');
	}
	if(document.getElementById('qACASHMatchVestingNo').checked)
{

 var vesting1=frm.qACASHMatchVestingPct1.value;
var vesting2=frm.qACASHMatchVestingPct2.value;
if(vesting1 == null || vesting1 == '' || vesting1 == 0.0){
		
	//	return true;
	}else
	{
	validateSHMatchVesting1(frm.qACASHMatchVestingPct1, 'Y');
	
	}
	
	if(vesting2 == null || vesting2 == '' || vesting2 == 0.0)
	{
	}else
	{
	validateSHMatchVesting2(frm.qACASHMatchVestingPct2, 'Y');
	}
}
	
	
	var contribtionsDistributionComplete = "${tabPlanDataForm.contriAndDistriDataCompleteInd}";
	/*if($("#qACAPlanHasSafeHarborMatchOrNonElective").val() == '' && $("#qACAPlanHasSafeHarborMatchOrNonElective").val() != null){
		if(contribtionsDistributionComplete == 'N' || contribtionsDistributionComplete == '' || contribtionsDistributionComplete == null){		
			return true;
		}
	}*/		
	
	}
	if(qACAPlanHasSafeHarborMatchOrNonElective == "SHNEC"){
	var row1pct1 = frm.qACANonElectiveContributionPct.value;

	if(row1pct1 == null || row1pct1 == ''){
	//	alert(MISSING_VALUES_MESS);
	//	return true;
	}else
	{
	validateQACASHNonElective(frm.qACANonElectiveContributionPct, 'Y');
	}

	}
	
	}
	}
	return false;
}

//To check the no of characters not to exceed the specified limit
function textCounter( field, maxlimit ) {
	  var temp = field.value;
	  temp = temp.replace(/[\n\r\n]+/g, '  ');
	  var nChar = temp.replace(/[\t]+/g, '').length;
	  if ( nChar >= maxlimit )
	  {
	    field.value = temp.substring( 0, maxlimit );
	    alert( 'Textarea value can only be '+maxlimit+' characters in length.' );
	    return false;
	  }
}

function modifyBgColorIfMatch(){
	$("#SafeHarborMatchId").removeClass("oddDataRow");
	$("#SafeHarborMatchId").removeClass("evenDataRow");
	$("#SafeHarborMatchId").addClass("oddDataRow");
	
	$("#additionalEC").removeClass("evenDataRow");
	$("#additionalEC").removeClass("oddDataRow");
	$("#additionalEC").addClass("evenDataRow");
	
	$("#qACAAutoDays").removeClass("oddDataRow");
	$("#qACAAutoDays").removeClass("evenDataRow");
	$("#qACAAutoDays").addClass("oddDataRow");
	
	$("#additionalECon").removeClass("evenDataRow");
	$("#additionalECon").removeClass("oddDataRow");
	$("#additionalECon").addClass("evenDataRow");
	
	$("#additionalECRef").removeClass("oddDataRow");
	$("#additionalECRef").removeClass("evenDataRow");
	$("#additionalECRef").addClass("oddDataRow");
	/*
	$("#vesting").removeClass("evenDataRow");
	$("#vesting").removeClass("oddDataRow");
	$("#vesting").addClass("evenDataRow");*/
}

function modifyBgColorIfNonElective(){
	$("#SafeHarborNonElectiveId").removeClass("oddDataRow");
	$("#SafeHarborNonElectiveId").removeClass("evenDataRow");
	$("#SafeHarborNonElectiveId").addClass("oddDataRow");
	
	$("#additionalEC").removeClass("evenDataRow");
	$("#additionalEC").removeClass("oddDataRow");
	$("#additionalEC").addClass("evenDataRow");
	
	$("#qACAAutoDays").removeClass("oddDataRow");
	$("#qACAAutoDays").removeClass("evenDataRow");
	$("#qACAAutoDays").addClass("oddDataRow");
	
	$("#additionalECon").removeClass("evenDataRow");
	$("#additionalECon").removeClass("oddDataRow");
	$("#additionalECon").addClass("evenDataRow");
	
	$("#additionalECRef").removeClass("oddDataRow");
	$("#additionalECRef").removeClass("evenDataRow");
	$("#additionalECRef").addClass("oddDataRow");
	/*
	$("#vesting").removeClass("evenDataRow");
	$("#vesting").removeClass("oddDataRow");
	$("#vesting").addClass("evenDataRow");*/
}

function modifyBgColorIfNone(){
	$("#additionalEC").removeClass("oddDataRow");
	$("#additionalEC").removeClass("evenDataRow");
	$("#additionalEC").addClass("oddDataRow");
	
	$("#qACAAutoDays").removeClass("oddDataRow");
	$("#qACAAutoDays").removeClass("evenDataRow");
	$("#qACAAutoDays").addClass("evenDataRow");
	
	$("#additionalECon").removeClass("evenDataRow");
	$("#additionalECon").removeClass("oddDataRow");
	$("#additionalECon").addClass("oddDataRow");
	
	$("#additionalECRef").removeClass("evenDataRow");
	$("#additionalECRef").removeClass("oddDataRow");
	$("#additionalECRef").addClass("evenDataRow");
	
	
	/*
	$("#vesting").removeClass("oddDataRow");
	$("#vesting").removeClass("evenDataRow");
	$("#vesting").addClass("oddDataRow");*/
}

//If Roth or catchup is selected on Tab2 display, default to Yes and leave editable
//Do not display Roth or Catch up contribution questions if Roth or Catch up contribution = NO	
//Roth
function setRothValues(){
	var frm = document.tabPlanDataForm;
	var qACASafeHarborAppliesToRoth = frm.qACASafeHarborAppliesToRoth.value;
	var contriAndDistriRothDeferral = "${tabPlanDataForm.planAllowRothDeferrals}";
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
	var frm = document.tabPlanDataForm;
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


//Automatic Contribution
function assignPctValues()
{
var pctValue=document.getElementById('qACAMatchContributionMatchPct1').value;
var matchContribPct2=document.getElementById('qACAMatchContributionContribPct2').value;
var matchPct2=document.getElementById('qACAMatchContributionMatchPct2').value;

if(pctValue >= 4)
{
document.getElementById('qACAMatchContributionContribPct2').value = '';
document.getElementById('qACAMatchContributionMatchPct1Value').value = '';
document.getElementById('qACAMatchContributionMatchPct2').value = '';
}else
{ 
document.getElementById('qACAMatchContributionMatchPct1Value').value = pctValue;
}
}
function enableContributionFeature1Pct()
{
	var frm = document.tabPlanDataForm;
	if(document.getElementById("automaticContributionFeature1").checked == true)
	{
		document.getElementById('automaticContributionFeature1').value="1";
		document.getElementById('contributionFeature1Pct').disabled = false;
		
	}
	else
	{
		document.getElementById('automaticContributionFeature1').value="";
		document.getElementById('contributionFeature1Pct').value = "";
		document.getElementById('contributionFeature1Pct').disabled = true;
		

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
		 document.getElementById('contributionFeature1Pct').disabled = false;
		 document.getElementById('automaticContributionFeature1').checked=true;
	 }
	 else{
		 document.getElementById('automaticContributionFeature1').value="0";
		 document.getElementById('contributionFeature1Pct').disabled = true;
		 document.getElementById('automaticContributionFeature1').checked=false;
	 }
	 
	 if(document.getElementById('contributionFeature2DateId').value !=null && document.getElementById('contributionFeature2DateId').value !="" 
													|| contributionFeature2DateIdMissing=="true" )
	 {
		 document.getElementById('automaticContributionFeature2').value="2";
		 document.getElementById('contributionFeature2DateId').disabled = false;
		 document.getElementById('automaticContributionFeature2').checked=true;
	 }
	 else{
		 document.getElementById('contributionFeature2DateId').disabled = true;
		 document.getElementById('automaticContributionFeature2').checked=false;
	 }
	 
	 if(document.getElementById('contributionFeature3SummaryText').value !=null && document.getElementById('contributionFeature3SummaryText').value !="" || 
																		contributionFeature3SummaryTextMissing =="true")
	 {
		 document.getElementById('automaticContributionFeature3').value="3";
		 document.getElementById('contributionFeature3SummaryText').disabled = false;
		 document.getElementById('automaticContributionFeature3').checked=true;
	 }
	 else{
		 document.getElementById('contributionFeature3SummaryText').disabled = true;
		 document.getElementById('automaticContributionFeature3').checked=false;
	 }
	 

	
	
}

function enableContributionFeature2DateId()
{


if(document.getElementById("automaticContributionFeature2").checked == true)
{
	
	document.forms['tabPlanDataForm'].elements['automaticContributionFeature2'].value="2";
    document.getElementById('contributionFeature2DateId').disabled = false;
    document.getElementById('contributionFeature2DateIdImage').setAttribute('onclick', "return handleDateIconClicked(event, 'contributionFeature2DateId');");
}
else
{
	document.forms['tabPlanDataForm'].elements['automaticContributionFeature2'].value="";
	document.getElementById('contributionFeature2DateId').disabled = true;
    document.getElementById('contributionFeature2DateId').value = "";
	$("#contributionFeature2DateIdImage").removeAttr("onclick");
}
}

function enableContributionFeature3SummaryText()
{
if(document.getElementById("automaticContributionFeature3").checked == true)
{
	document.forms['tabPlanDataForm'].elements['automaticContributionFeature3'].value="3";
	document.getElementById('contributionFeature3SummaryText').disabled = false;
}
else
{
	document.forms['tabPlanDataForm'].elements['automaticContributionFeature3'].value="";
	document.getElementById('contributionFeature3SummaryText').disabled = true;
	document.getElementById('contributionFeature3SummaryText').value = "";
}
}
function showorhideqACAAutomaticContributionDays()
{
var qacdOtherId = "#qACAAutomaticContributionDaysOtherId";
if(document.getElementById('qACAPlanHasAdditionalECYes').checked)
{
document.getElementById('qACAAutomaticContributionDays').disabled = false;
if(document.getElementById('qACAAutomaticContributionDays').value=='00')
{
document.getElementById('qACAAutomaticContributionDaysOther').disabled = false;
  $(qacdOtherId).show();
}else
{
  document.getElementById('qACAAutomaticContributionDaysOther').value="";    
  $(qacdOtherId).hide();
}
}else
{
$(qacdOtherId).hide();
document.getElementById('qACAAutomaticContributionDays').disabled = true;
document.getElementById('qACAAutomaticContributionDaysOther').disabled = true;
}
}
function showorhideQACASummaryPlanDesc()
{
if(document.getElementById('qACAPlanHasAdditionalEConYes').checked)
{
document.getElementById('qACASummaryPlanDesc').disabled = false;
document.getElementById("vestingQACASpan").style.display ="block";

}else if(document.getElementById('qACAPlanHasAdditionalEConNo').checked)
{
document.getElementById('qACASummaryPlanDesc').disabled = true;
document.getElementById('qACASummaryPlanDesc').value = "";
document.getElementById("vestingQACASpan").style.display ="none";
}else
{
document.getElementById('qACASummaryPlanDesc').disabled = true;
document.getElementById('qACASummaryPlanDesc').value = "";
document.getElementById("vestingQACASpan").style.display ="none";
}
}
function showorhideQACASHMatchVestingNoFields()
{
if(document.getElementById('qACASHMatchVestingYes').checked)
{
document.getElementById('qACASHMatchVestingPct1').disabled = true;
document.getElementById('qACASHMatchVestingPct2').disabled = true;
document.getElementById('qACASHMatchVestingPct1').value = "";
document.getElementById('qACASHMatchVestingPct2').value = "";
}else if(document.getElementById('qACASHMatchVestingNo').checked)
{
document.getElementById('qACASHMatchVestingPct1').disabled = false;
document.getElementById('qACASHMatchVestingPct2').disabled = false;
}
if(document.getElementById('qACASHMatchVestingNo').checked==false)
{
document.getElementById('qACASHMatchVestingPct1').disabled = true;
document.getElementById('qACASHMatchVestingPct2').disabled = true;
document.getElementById('qACASHMatchVestingPct1').value = "";
document.getElementById('qACASHMatchVestingPct2').value = "";

}
}
 
 function assignAutoTabValues()
 {
 var frm = document.tabPlanDataForm;
 document.getElementById('spdEmployerContributionRef').setAttribute('maxlength', '100');
document.getElementById('contributionFeature3SummaryText').setAttribute('maxlength', '200');
document.getElementById('qACASHNonElectivePlanName').setAttribute('maxlength', '100');
document.getElementById('safeHarbourPlanName').setAttribute('maxlength', '100');
document.getElementById('qACASummaryPlanDesc').setAttribute('maxlength', '100');

var qACAPlanHasSafeHarborMatchOrNonElective = frm.qACAPlanHasSafeHarborMatchOrNonElective.value;
	if(qACAPlanHasSafeHarborMatchOrNonElective == "SHMAC"){
		document.getElementById("SafeHarborMatchId").style.display = "block";
		document.getElementById("SafeHarborNonElectiveId").style.display = "none";
		setRothValues();
		setCatchUpContributionValues();
		modifyBgColorIfMatch();
	} 
	else if(qACAPlanHasSafeHarborMatchOrNonElective == "SHNEC"){
		document.getElementById("SafeHarborNonElectiveId").style.display = "block";
		document.getElementById("SafeHarborMatchId").style.display = "none";
		modifyBgColorIfNonElective();
	}
	else{
		document.getElementById("SafeHarborMatchId").style.display = "none";
		document.getElementById("SafeHarborNonElectiveId").style.display = "none";
		modifyBgColorIfNone();
	}
 
 	//Default 'another plan' question to No. If the answer is Yes, enable the plan name. 	
	var qACAMatchContributionToAnotherPlan = $("input[name='qACAMatchContributionToAnotherPlan']:checked").val();	
	if(qACAMatchContributionToAnotherPlan!=null && qACAMatchContributionToAnotherPlan!=""){
		if(qACAMatchContributionToAnotherPlan == 'Y'){
			document.getElementById("anotherPlanMatchingContributionYes").checked = true;
			document.getElementById("safeHarbourPlanName").disabled=false;
		}else if(qACAMatchContributionToAnotherPlan == 'N'){
			document.getElementById("anotherPlanMatchingContributionNo").checked = true;
			document.getElementById("safeHarbourPlanName").disabled=true;
		}else{
			document.getElementById("safeHarbourPlanName").disabled=true;
		}
	}
	else{
		document.getElementById("anotherPlanMatchingContributionNo").checked = true;
		document.getElementById("safeHarbourPlanName").disabled=true;
	}
 
 
 
	
	//Default 'Non Elective another plan' question to No. If the answer is Yes, enable the plan name.	
	var qACANonElectiveContribOtherPlan = $("input[name='qACANonElectiveContribOtherPlan']:checked").val();
	if(qACANonElectiveContribOtherPlan!=null && qACANonElectiveContribOtherPlan!=""){
		if(qACANonElectiveContribOtherPlan == 'Y'){
			document.getElementById("SHNEAppliedToAnotherPlanYes").checked = true;
			document.getElementById("qACASHNonElectivePlanName").disabled=false;
		} else{  
			document.getElementById("SHNEAppliedToAnotherPlanNo").checked = true;
			document.getElementById("qACASHNonElectivePlanName").disabled=true;
		}
	}
	else{  
		document.getElementById("SHNEAppliedToAnotherPlanNo").checked = true;
		document.getElementById("qACASHNonElectivePlanName").disabled=true;
	}
	
	
	//If plan has Additional Employee contribution, enter plan name else disable
	var planHasAdditionalEmpContribution = frm.qACAPlanHasAdditionalEC.value;
	if(planHasAdditionalEmpContribution!=null && planHasAdditionalEmpContribution!=""){
		if(planHasAdditionalEmpContribution =='Y'){
			document.getElementById("qACAPlanHasAdditionalECYes").checked = true;
			//document.getElementById("qACASummaryPlanDesc").disabled=false;
			//document.getElementById("vesting").style.display ="block";
		} else if(planHasAdditionalEmpContribution =='N'){  
			document.getElementById("qACAPlanHasAdditionalECNo").checked = true;
			//document.getElementById("qACASummaryPlanDesc").disabled=true;
		}else
		{
		document.getElementById("qACASummaryPlanDesc").disabled=true;
		}
	}
	else{
		document.getElementById("qACASummaryPlanDesc").disabled=true;
	}
	 $('#automaticContributionFeature2').change(function() {
        if($(this).is(":checked")) {        	
        	//$('acontributionFeature2DateIdImage').button('option', 'disabled', false);
           $("#contributionFeature2DateIdImage").attr("disabled",false );
           document.getElementById('contributionFeature2DateId').disabled = false;
        }else
        {        
       $("#contributionFeature2DateIdImage").attr("disabled", true);
      //  $('acontributionFeature2DateIdImage').button('option', 'disabled', true);
        document.getElementById('contributionFeature2DateId').disabled = true;
        document.getElementById('contributionFeature2DateId').value = "";
        }
                
    });
	
	$('#qACAPlanHasSafeHarborMatchOrNonElective').change(function() {
	setDirtyFlag();
	var oldValue=qACAPlanHasSafeHarborMatchOrNonElective;
	  var current = $(this).val();
    if(oldValue!=null && oldValue!='' && oldValue!=current)
    {
	var message=ERR_SH_CHANGES_LOST;
	if(confirmSwitchAutoOptions(message)){
		placeSafeHarborVestingTableQACA();
	}else
	{
	$("#qACAPlanHasSafeHarborMatchOrNonElective").val(oldValue);
	}	
	}
    else {
    	placeSafeHarborVestingTableQACA();
    }
    	var contribtionsDistributionComplete = "${tabPlanDataForm.contriAndDistriDataCompleteInd}";
	 	if($("#qACAPlanHasSafeHarborMatchOrNonElective").val() == "SHMAC"){
	 		if(contribtionsDistributionComplete == 'N' || contribtionsDistributionComplete =='' || contribtionsDistributionComplete == null){				
			}
			document.getElementById('SafeHarborMatchId').style.display="block";
			document.getElementById('SafeHarborNonElectiveId').style.display="none";
			setRothValues();
			setCatchUpContributionValues();
			modifyBgColorIfMatch();
		}
	 	else if($("#qACAPlanHasSafeHarborMatchOrNonElective").val() == 'SHNEC'){
	 		document.getElementById('SafeHarborNonElectiveId').style.display="block";
	 		document.getElementById('SafeHarborMatchId').style.display="none";
	 		if(contribtionsDistributionComplete == 'N' || contribtionsDistributionComplete =='' || contribtionsDistributionComplete == null){	 			
			}
	 		modifyBgColorIfNonElective();
		}
	 	else {
	 		document.getElementById('SafeHarborMatchId').style.display="none";
			document.getElementById('SafeHarborNonElectiveId').style.display="none";
			modifyBgColorIfNone();
	 	}
	});
	
	
	
	$('input[name="qACAMatchContributionToAnotherPlan"]').click(function() {	
	 	if($('input[name="qACAMatchContributionToAnotherPlan"]:checked').val() == 'Y'){	 	
			document.getElementById('safeHarbourPlanName').disabled=false;
		}
	 	else if($('input[name="qACAMatchContributionToAnotherPlan"]:checked').val() == 'N'){
	 		document.getElementById('safeHarbourPlanName').value='';
			document.getElementById('safeHarbourPlanName').disabled=true;
		}else
		{
			document.getElementById('safeHarbourPlanName').value='';
			document.getElementById('safeHarbourPlanName').disabled=true;
		}
	});
	
	
	$('input[name="qACANonElectiveContribOtherPlan"]').click(function() {	
	 	if($('input[name="qACANonElectiveContribOtherPlan"]:checked').val() == 'Y'){
			document.getElementById('qACASHNonElectivePlanName').disabled=false;
		}
	 	else if($('input[name="qACANonElectiveContribOtherPlan"]:checked').val() == 'N'){
	 		document.getElementById('qACASHNonElectivePlanName').value='';
			document.getElementById('qACASHNonElectivePlanName').disabled=true;
		}else
		{
			document.getElementById('qACASHNonElectivePlanName').value='';
			document.getElementById('qACASHNonElectivePlanName').disabled=true;
		}
	});	
	
	
	$('input[name="qACAPlanHasAdditionalEC"]').click(function() {
	 	if($('input[name="qACAPlanHasAdditionalEC"]:checked').val() == 'Y'){
			//document.getElementById('qACASummaryPlanDesc').disabled=false;
			//document.getElementById("vesting").style.display ="block";
		}
	 	else if($('input[name="qACAPlanHasAdditionalEC"]:checked').val() == 'N'){
	 		//document.getElementById('qACASummaryPlanDesc').value='';
			//document.getElementById('qACASummaryPlanDesc').disabled=true;
			//document.getElementById("vesting").style.display ="none";
		}
	});
	
 
 }
 
   function validateMonthDay(value) {
    if (value.length==0) {
      return true;
    }

    return getDate(value + "/2000") != null;
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