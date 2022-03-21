/* 
 * show/hide attributes for auto enrollment and auto contribution increase
 * author Ioana Dogaru
 * */

function hideAttributes(theClass) { 
	var allTrTags=document.getElementsByTagName("tr"); 
	for (i=0; i<allTrTags.length; i++) { 
		if (allTrTags[i].className.match(theClass) != null) { 
			allTrTags[i].style.visibility='hidden'; 
			allTrTags[i].style.display='none'; 
		} 
	} 
	
	var cl = document.getElementById("collapseAE");
	var ex = document.getElementById("expandAE");
	
	if(theClass == 'aci')
	{
		var cl = document.getElementById("collapseACI");
		var ex = document.getElementById("expandACI");
	}
	
	if(ex != null)
	{
		ex.style.visibility='visible';
		ex.style.display=''; 
	}
	if(cl != null)
	{
		cl.style.visibility='hidden';
		cl.style.display='none'; 
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
	
	var signup = getRadioValue("aciSignupMethod");
	if(signup == null || signup == "S")
		hideSignupAttributes();
	
	if(signup == "A")
	{
		var appliesTo = getRadioValue("signupMethodAppliesTo");
		if(appliesTo == null || appliesTo == "A")
		{
			document.getElementsByName("aciEffectiveDateOfChange")[0].disabled = true;
		}
	}

}

function toggleAttributes(theClass){
	var allTrTags=document.getElementsByTagName("tr"); 
	var hdn = false;
	var first = true;
	for (i=0; i<allTrTags.length; i++){
		if (allTrTags[i].className.match(theClass) != null) {
			
			if(allTrTags[i].style.visibility == 'hidden')
			{
				allTrTags[i].style.visibility='visible'; 
				allTrTags[i].style.display=''; 
				if(first)
					hdn = true;
			}
			else{ 
				allTrTags[i].style.visibility='hidden'; 
				allTrTags[i].style.display='none'; 
			} 
			first = false;
		} 
	} 
	

	
	if(theClass == 'autoEnrolled')
	{
		var cl = document.getElementById("collapseAE");
	    var ex = document.getElementById("expandAE");
	} else if(theClass == 'aci')
	{
		var cl = document.getElementById("collapseACI");
		var ex = document.getElementById("expandACI");
	} else if(theClass == 'ecalculationService')
	{
		var cl = document.getElementById("collapseCS");
		var ex = document.getElementById("expandCS");
	}
	if(hdn)
	{
		if(cl != null)
		{
			cl.style.visibility='visible';
			cl.style.display=''; 
		}
		if(ex != null)
		{
			ex.style.visibility='hidden';
			ex.style.display='none'; 
		}
	}
	else
	{
		if(ex != null)
		{
			ex.style.visibility='visible';
			ex.style.display=''; 
		}
		if(cl != null)
		{
			cl.style.visibility='hidden';
			cl.style.display='none'; 
		}
		
	}
	
	var signup = getRadioValue("aciSignupMethod");
	if(document.getElementsByName("aciSignupMethod")[0] != null && (signup == null || signup == "S"))
		hideSignupAttributes();

}

function hideExpandButton()
{
	var exAE = document.getElementById("expandAE");
	var exACI = document.getElementById("expandACI");
	var exECS = document.getElementById("expandCS");
	if(exAE != null)
	{
		exAE.style.visibility='hidden';
		exAE.style.display='none'; 
	}
	
	if(exACI != null)
	{
		exACI.style.visibility='hidden';
		exACI.style.display='none'; 
	}
	if(exECS != null)
	{   
	    exECS.style.visibility='hidden';
		exECS.style.display='none'; 
	}
}

function toggleDates(){
	var allTrTags=document.getElementsByTagName("span"); 
	
	for (i=0; i<allTrTags.length; i++) { 
		if (allTrTags[i].className.match('peds') != null) { 
			var tgText = document.getElementById("toggleText");
			if(allTrTags[i].style.visibility == 'hidden')
			{
				allTrTags[i].style.visibility='visible'; 
				allTrTags[i].style.display=''; 
				tgText.innerHTML = 'Collapse';
			}
			else{ 
				allTrTags[i].style.visibility='hidden'; 
				allTrTags[i].style.display='none'; 
				tgText.innerHTML = 'Show All';
			} 
		} 
	} 
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
		
	if(deferralTypeValue == "")
	{
		// not selected - disable plan limit and all aci fields
		document.getElementsByName("deferralLimitPercent")[0].disabled = true;
		document.getElementsByName("deferralLimitDollars")[0].disabled = true;
		// if autoContributionIncrease is in edit mode
		if(document.getElementsByName("autoContributionIncrease")[0] != null)
		{
			document.getElementsByName("autoContributionIncrease")[0].disabled = true;
			document.getElementsByName("autoContributionIncrease")[1].disabled = true;
			if(document.getElementsByName("autoContributionIncrease")[2] != null)
				document.getElementsByName("autoContributionIncrease")[2].disabled = true;
			disableACIAttributes(true);
		}
	}
	else 
	{
		if(deferralTypeValue == '%')
		{
				document.getElementsByName("deferralLimitPercent")[0].disabled = false;
				document.getElementsByName("deferralLimitDollars")[0].disabled = true;
		}
		
		if(deferralTypeValue == '$')
		{
				document.getElementsByName("deferralLimitPercent")[0].disabled = true;
				document.getElementsByName("deferralLimitDollars")[0].disabled = false;
		}
		
		if(deferralTypeValue == 'E')
		{
				document.getElementsByName("deferralLimitPercent")[0].disabled = false;
				document.getElementsByName("deferralLimitDollars")[0].disabled = false;
		}
		if(document.getElementsByName("autoContributionIncrease")[0] != null)
		{
			document.getElementsByName("autoContributionIncrease")[0].disabled = false;
			document.getElementsByName("autoContributionIncrease")[1].disabled = false;
			if(document.getElementsByName("autoContributionIncrease")[2] != null)
				document.getElementsByName("autoContributionIncrease")[2].disabled = false;
			
			var aci = getRadioValue("autoContributionIncrease");
			
			if (aci == "Y")
			{
				disableACIAttributes(false);
			}
		}
	}
}

function disableAutoContributionIncrease() {
	var disabled = true;
	if(document.getElementsByName("autoContributionIncrease")[0] != null)
	{
		var radioValue = getRadioValue("autoContributionIncrease");
		if (radioValue == "Y") {
			disabled = false;
		}
		disableACIAttributes(disabled);
		var signup = getRadioValue("aciSignupMethod");
		if(signup == null || signup == "S")
			hideSignupAttributes();
		if(signup == "A")
		{
			var appliesTo = getRadioValue("signupMethodAppliesTo");
			if(appliesTo == null || appliesTo == "A")
			{
				document.getElementsByName("aciEffectiveDateOfChange")[0].disabled = true;
			}
		}
	}
	
}

function disableACIAttributes(disabled)
{
	document.getElementsByName("aciSignupMethod")[0].disabled = disabled;
	document.getElementsByName("aciSignupMethod")[1].disabled = disabled;
	document.getElementsByName("signupMethodAppliesTo")[0].disabled = disabled;
	document.getElementsByName("signupMethodAppliesTo")[1].disabled = disabled;
	var	aciEffectiveDateOfChange = document.getElementsByName("aciEffectiveDateOfChange")[0];
	aciEffectiveDateOfChange.disabled = disabled;
	aciAnniversaryDate = document.getElementsByName("aciAnniversaryDate")[0];
	aciAnniversaryDate.disabled = disabled;
	aciAnniversaryYear = document.getElementsByName("aciAnniversaryYear")[0];
	aciAnniversaryYear.disabled = disabled;
	participantMakeChangeDays = document.getElementsByName("participantMakeChangeDays")[0];
	participantMakeChangeDays.disabled = disabled;
	defaultAnnualIncreasePercent = document.getElementsByName("defaultAnnualIncreasePercent")[0];
	defaultAnnualIncreasePercent.disabled = disabled;
	defaultAnnualIncreaseDollars = document.getElementsByName("defaultAnnualIncreaseDollars")[0];
	defaultAnnualIncreaseDollars.disabled = disabled;
	defaultContrDeferralLimitPercent = document.getElementsByName("defaultContrDeferralLimitPercent")[0];
	defaultContrDeferralLimitPercent.disabled = disabled;
	defaultContrDeferralLimitDollars = document.getElementsByName("defaultContrDeferralLimitDollars")[0];
	defaultContrDeferralLimitDollars.disabled = disabled;
	document.getElementsByName("increaseAnniversary")[0].disabled = disabled;
	document.getElementsByName("increaseAnniversary")[1].disabled = disabled;;
	document.getElementsByName("tpaManagingACI")[0].disabled = disabled;
	document.getElementsByName("tpaManagingACI")[1].disabled = disabled;	
	document.getElementsByName("participantChangeAnniversaryDate")[0].disabled = disabled;
	document.getElementsByName("participantChangeAnniversaryDate")[1].disabled = disabled;
	
	if (disabled) {
		document.getElementsByName("participantChangeAnniversaryDate")[0].checked = false;
		document.getElementsByName("participantChangeAnniversaryDate")[1].checked = true;	
		disableAnniversaryFields();
	}
 
	if(disabled == false)
	{
		var deferralType = document.getElementsByName('deferralType')[0];
		var selIndex = deferralType.selectedIndex;
		var deferralTypeValue = deferralType.options[selIndex].value;
		if(deferralTypeValue == '%')
		{
			document.getElementsByName("defaultAnnualIncreaseDollars")[0].disabled = true;
			document.getElementsByName("defaultContrDeferralLimitDollars")[0].disabled = true;
		}
		else if(deferralTypeValue == '$')
		{
			document.getElementsByName("defaultAnnualIncreasePercent")[0].disabled = true;
			document.getElementsByName("defaultContrDeferralLimitPercent")[0].disabled = true;
		}
		
		if(document.getElementsByName("isAppliedToSaved")[0].value == 'true')
		{
			document.getElementsByName("signupMethodAppliesTo")[0].disabled = true;
			document.getElementsByName("signupMethodAppliesTo")[1].disabled = true;
		}
		
		if(document.getElementsByName("isEffectiveDateDisabled")[0].value == 'true')
		{
			document.getElementsByName("aciEffectiveDateOfChange")[0].disabled = true;
		}
		
		if(document.getElementsByName("isFreezePeriod")[0].value == 'true')
		{
			document.getElementsByName("aciAnniversaryDate")[0].disabled = true;
			document.getElementsByName("aciAnniversaryYear")[0].disabled = true;
			document.getElementsByName("participantMakeChangeDays")[0].disabled = true;
			document.getElementsByName("signupMethodAppliesTo")[0].disabled = true;
		}
	}
}

function changeClass(cls) { 
	var element=document.getElementById("tpaCell"); 
	element.className = cls;
	
}

function hideSignupAttributes()
{
	hidePageElement("appliesTo"); 
	hidePageElement("effectiveDate"); 
	hidePageElement("increaseAnniv"); 
	changeClass('datacell2 aci');
}


function showSignupAttributes()
{
	showPageElement("appliesTo"); 
	showPageElement("effectiveDate"); 
	showPageElement("increaseAnniv"); 
	changeClass('datacell1 aci');
	var signup = getRadioValue("aciSignupMethod");
	if(signup == "A")
	{
		var appliesTo = getRadioValue("signupMethodAppliesTo");
		if(appliesTo == null || appliesTo == "A")
		{
			document.getElementsByName("aciEffectiveDateOfChange")[0].disabled = true;
		}
	}
}


function showDateSelection() {
   showPageElement("dayOfMonthSelection");
   showPageElement("monthSelection");
   selectDates();
}

function selectDates() {
  var mmdd = document.getElementsByName("aciAnniversaryDate")[0].value;
  if (mmdd == null) return;
  
  document.getElementById("anniversaryMonthJan").checked = false;
  document.getElementById("anniversaryMonthFeb").checked = false;
  document.getElementById("anniversaryMonthMar").checked = false;
  document.getElementById("anniversaryMonthApr").checked = false;
  document.getElementById("anniversaryMonthMay").checked = false;
  document.getElementById("anniversaryMonthJun").checked = false;
  document.getElementById("anniversaryMonthJul").checked = false;
  document.getElementById("anniversaryMonthAug").checked = false;
  document.getElementById("anniversaryMonthSep").checked = false;
  document.getElementById("anniversaryMonthOct").checked = false;
  document.getElementById("anniversaryMonthNov").checked = false;
  document.getElementById("anniversaryMonthDec").checked = false;
    
  var dateSelectionData = mmdd.split("/");
  if (dateSelectionData[0] == '1' || dateSelectionData[0] == '01') {
      document.getElementById("anniversaryMonthJan").checked = true;
  }
  if (dateSelectionData[0] == '2' || dateSelectionData[0] == '02') {
      document.getElementById("anniversaryMonthFeb").checked = true;
  }
  if (dateSelectionData[0] == '3' || dateSelectionData[0] == '03') {
      document.getElementById("anniversaryMonthMar").checked = true;
  }
  if (dateSelectionData[0] == '4' || dateSelectionData[0] == '04') {
      document.getElementById("anniversaryMonthApr").checked = true;
  }
  if (dateSelectionData[0] == '5' || dateSelectionData[0] == '05') {
      document.getElementById("anniversaryMonthMay").checked = true;
  }
  if (dateSelectionData[0] == '6' || dateSelectionData[0] == '06') {
      document.getElementById("anniversaryMonthJun").checked = true;
  }
  if (dateSelectionData[0] == '7' || dateSelectionData[0] == '07') {
      document.getElementById("anniversaryMonthJul").checked = true;
  }
  if (dateSelectionData[0] == '8' || dateSelectionData[0] == '08') {
      document.getElementById("anniversaryMonthAug").checked = true;
  }
  if (dateSelectionData[0] == '9' || dateSelectionData[0] == '09') {
      document.getElementById("anniversaryMonthSep").checked = true;
  }
  if (dateSelectionData[0] == '10') {
      document.getElementById("anniversaryMonthOct").checked = true;
  }
  if (dateSelectionData[0] == '11') {
      document.getElementById("anniversaryMonthNov").checked = true;
  }
  if (dateSelectionData[0] == '12') {
      document.getElementById("anniversaryMonthDec").checked = true;
  }

  if (dateSelectionData[1] != null) {
  	   var dateIndex = parseInt(dateSelectionData[1],10);
  	   // if the first element in the dropdown is 1 and not empty string then selected index must be dateIndex - 1
   	   if(document.getElementsByName('anniversaryDayOfMonth')[0].options[0] != null &&
   	   document.getElementsByName('anniversaryDayOfMonth')[0].options[0].value == "1")
   	  		dateIndex--;
   	   
   	   document.getElementsByName("anniversaryDayOfMonth")[0].selectedIndex = dateIndex;
   	   
	}
}



function hideDateSelection() {
   hidePageElement("dayOfMonthSelection");
   hidePageElement("monthSelection");
}


function genDate(monthName, dateSelected, month, first) {

   var dateComponent = dateSelected;
   if ((month==2) && (dateSelected >28)) dateComponent = "28";
   if ((month==4) && (dateSelected >30)) dateComponent = "30";
   if ((month==6) && (dateSelected >30)) dateComponent = "30";
   if ((month==9) && (dateSelected >30)) dateComponent = "30";
   if ((month==11) && (dateSelected >30)) dateComponent = "30";
   
   if (first == false) {
       return ", " + monthName +" "+dateComponent;
   } else {
       return monthName +" "+dateComponent;
   }
}


function dateChanged() {
    var selDate = document.getElementsByName("anniversaryDayOfMonth")[0].value	
	var janSel = document.getElementById("anniversaryMonthJan").checked;
	var febSel = document.getElementById("anniversaryMonthFeb").checked;
	var marSel = document.getElementById("anniversaryMonthMar").checked;
	var aprSel = document.getElementById("anniversaryMonthApr").checked;
	var maySel = document.getElementById("anniversaryMonthMay").checked;
	var junSel = document.getElementById("anniversaryMonthJun").checked;
	var julSel = document.getElementById("anniversaryMonthJul").checked;	
	var augSel = document.getElementById("anniversaryMonthAug").checked;	
	var sepSel = document.getElementById("anniversaryMonthSep").checked;	
	var octSel = document.getElementById("anniversaryMonthOct").checked;
	var novSel = document.getElementById("anniversaryMonthNov").checked;
	var decSel = document.getElementById("anniversaryMonthDec").checked;
		
	var result = "";
	var first = true;
	if (janSel) { 
	    result = result + genDate("Jan", selDate, 1, first);
	    first = false;
	}
	
	if (febSel) { 
	    result = result + genDate("Feb", selDate, 2, first);
	    first = false;
	}
	if (marSel) { 
	    result = result + genDate("Mar", selDate, 3, first);
	    first = false;
	}
	if (aprSel) { 
	    result = result + genDate("Apr", selDate, 4, first);
	    first = false;	    
	}
	if (maySel) { 
	    result = result + genDate("May", selDate, 5, first);
	    first = false;
	}
	if (junSel) { 
	    result = result + genDate("Jun", selDate, 6, first);
	    first = false;
	}
	if (julSel) { 
	    result = result + genDate("Jul", selDate, 7, first);
	    first = false;
	}	
	if (augSel) { 
	    result = result + genDate( "Aug", selDate, 8, first);
	    first = false;
	}	
	if (sepSel) { 
	    result = result + genDate("Sep", selDate, 9, first);
	    first = false;
	}
	if (octSel) { 
	    result = result + genDate("Oct", selDate, 10, first);
	    first = false;
	}
	if (novSel) { 
	    result = result + genDate("Nov" , selDate, 11, first);
	    first = false;
	}					
	if (decSel) { 
	    result = result + genDate("Dec", selDate, 12, first);
	    first = false;
	}
	setDateRestrictionText(result);		
	//var dateTargetDiv = document.getElementById("dateTarget");
	//dateTargetDiv.innerHTML  = result + ")";
}


function getNextDate(startDate, monthInc) {
	var month = startDate.getMonth();
	var day = startDate.getDate();
	var year = startDate.getFullYear();
	var newMonth=month+monthInc+1;
	var nextDate = new Date(year + "/" + newMonth + "/" + dayInMonth(newMonth,day,year));

	return nextDate;
}

function formatDate(dateVal, includeYear) {
	var month = dateVal.getMonth() + 1;
	var day = dateVal.getDate();
	var dateStr = (month<10 ? "0" + month : month) + "/" + (day<10 ? "0" + day : day) + (includeYear ? "/" + dateVal.getFullYear() : "");
	return dateStr;
}

function dayInMonth(month,day,year) {
	var dd = new Date(year, month, 0);
	var maxDay = dd.getDate();
	if (day > maxDay) {
		return maxDay;
	} else {
		return day;
	}
} 

// Copied and modified from calendar.js
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

function checkFreezePeriod(element)
{
	if(!isEmpty(document.getElementsByName("aciAnniversaryDate")[0].value )&& 
	   !isEmpty(document.getElementsByName("aciAnniversaryYear")[0].value ) &&
	   !isEmpty(document.getElementsByName("participantMakeChangeDays")[0].value))
	{
		var parsedDate = cal_prs_monthday(document.getElementsByName("aciAnniversaryDate")[0].value);
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
				
				var makeChangeDays = parseInt(document.getElementsByName("participantMakeChangeDays")[0].value,10);
				if(makeChangeDays != 'NaN')
				{
					currentDate.setDate(currentDate.getDate()+makeChangeDays);
					if(parsedDate.getTime() < currentDate.getTime())
					{
						element.value = "";
						if(element == document.getElementsByName("participantMakeChangeDays")[0]){
							document.getElementsByName("optOutDeadlineDays")[0].value = "";
							alert("New days before anniversary is not valid due to freeze period");
						} else if(element == document.getElementsByName("optOutDeadlineDays")[0]) {
							document.getElementsByName("participantMakeChangeDays")[0].value = "";
							alert("New opt-out days is not valid due to freeze period");
						}else {
							alert("New anniversary is not valid due to freeze period");
						}
					}
				}
    
    			var ann_date = new Date();
    			var str_date = document.getElementsByName("aciAnniversaryDate")[0].value
    			var arr_date = str_date.split('/');
    			ann_date.setFullYear(annivYear, arr_date[0]-1, arr_date[1]);
			
			var ann_date_only = ann_date;
			ann_date_only.setHours(0);
			ann_date_only.setMinutes(0);
			ann_date_only.setSeconds(0);
			ann_date_only.setMilliseconds(0);    			

			var now_date = new Date();
   			// only check anniversay date if it has changed
			if (ann_date_only.valueOf() != save_ann_date.valueOf()) {
				if (ann_date < now_date) {
    					alert("Anniversary date cannot be in the past");
					document.getElementsByName("aciAnniversaryYear")[0].value = "";
    				}
			}
    
				if (ann_date.getYear() > (now_date.getYear()+2)) {
					alert("Anniversary date cannot be more than 2 year in the future")
					document.getElementsByName("aciAnniversaryYear")[0].value = "";				
				} 
			}
			else
			{
				alert("Anniversary date must be mm/dd starting yyyy");
				document.getElementsByName("aciAnniversaryYear")[0].value = "";
			}
		}
		else
		{
			document.getElementsByName("aciAnniversaryDate")[0].value = "";
		}
	}
	
}

function updateOptOutDeadline()
{
	if(document.getElementsByName("optOutDeadlineDays")[0] != null)
	{
		document.getElementsByName("optOutDeadlineDays")[0].value = document.getElementsByName("participantMakeChangeDays")[0].value;
	}
}

function updateParticipantMakeChangeDays()
{
	if(document.getElementsByName("participantMakeChangeDays")[0] != null)
	{
		document.getElementsByName("participantMakeChangeDays")[0].value = document.getElementsByName("optOutDeadlineDays")[0].value;
	}
}

function updateDisabledDays()
{
	if(document.getElementsByName("optOutDeadlineDays")[0] != null && document.getElementsByName("participantMakeChangeDays")[0] != null)
	{
		if(document.getElementsByName("optOutDeadlineDays")[0].disabled || getRadioValue("autoEnrollInd") == "No" )
			document.getElementsByName("optOutDeadlineDays")[0].value = document.getElementsByName("participantMakeChangeDays")[0].value;
		
		if(document.getElementsByName("participantMakeChangeDays")[0].disabled)
			document.getElementsByName("participantMakeChangeDays")[0].value = document.getElementsByName("optOutDeadlineDays")[0].value;
	}
}

function isEmpty(s)
{ 
	return ((s == null) || (s.length == 0))
}

// returns 0 if there is no anniversary set
function daysToAnniversary()
{
	var days = -1;
	
	if(document.getElementsByName("aciAnniversaryDate")[0] != null &&
	   !isEmpty(document.getElementsByName("aciAnniversaryDate")[0].value )&& 
	   !isEmpty(document.getElementsByName("aciAnniversaryYear")[0].value ))
	{
		var parsedDate = cal_prs_monthday(document.getElementsByName("aciAnniversaryDate")[0].value);
		if(parsedDate != null)
		{
			var annivYear = parseInt(document.getElementsByName("aciAnniversaryYear")[0].value,10);
			if(annivYear != 'NaN' && annivYear > 999 && annivYear < 10000)
			{
				// find next anniversary
				if(parsedDate.getFullYear() < annivYear)
					parsedDate.setFullYear(annivYear);
				currentDate = new Date();
				
				if(currentDate.getTime() > parsedDate.getTime())
				{
					parsedDate.setFullYear(currentDate.getFullYear() + 1);
				}
				
				var diff = parsedDate.getTime() - currentDate.getTime();
				days = Math.round(diff/86400000);
			}		
		}
	}
	
	return days;
	
}


function getNextJan1st()
{
	var currentDate = new Date();
	var nextJan1st = new Date();
	nextJan1st.setMonth(0);
	nextJan1st.setDate(1);
	nextJan1st.setFullYear(currentDate.getFullYear() + 1);
	
	var makeChangeDays = parseInt(document.getElementsByName("participantMakeChangeDays")[0].value,10);
	if(makeChangeDays != 'NaN')
	{
		currentDate.setDate(currentDate.getDate()+makeChangeDays);
		if(nextJan1st.getTime() <= currentDate.getTime())
		{
			nextJan1st.setFullYear(nextJan1st.getFullYear() + 1);
		}
	}
	
	return nextJan1st;
}
