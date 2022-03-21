/**
 * This method is called during submitting the search button.
 * This method will be called by Quick Filter and Customize Filter during their submit
 *
 */                               
function submitData(filterValue) {
	if(filterValue == "quickFilter"){
		document.forms['quickFilterForm'].elements['showCustomizeFilter'].value = "N";
		setFilterFromInput(document.getElementById("showCustomizeFilter"));
		clearCustomizeFilter();
		setFilterValues();
	}else {
		document.forms['quickFilterForm'].elements['showCustomizeFilter'].value = "Y";
		if(document.forms["reportCustomizationForm"].elements["gatewayChecked"]){
			var tempGatewayChecked = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
			setGatewayOption(tempGatewayChecked);
		}
		if(document.forms["reportCustomizationForm"].elements["managedAccountChecked"]){
			var tempManagedAccountChecked = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
			setManagedAccountOption(tempManagedAccountChecked);
		}
	}
		setFilterFromInput(document.getElementById("showCustomizeFilter")); 
		doFilter();
}

/**
 * Sets the filter using the given HTML select object.
 */

var previousSelectionIndex = 0;

function setFilterFromSelect2(theSelect)
{
  var newValue = theSelect.options[theSelect.selectedIndex].value;
  if ( newValue != "##" ) {
	  filterMap[theSelect.name] = newValue;
  }
  else {
	  theSelect.selectedIndex = previousSelectionIndex;
  }
}

/**
 * Sets the previously selected values.
 */
function setPreviousSelection(theSelect)
{
  previousSelectionIndex = theSelect.selectedIndex;
} 

/**
 * Reset the Gateway value.
 */
/*function setGatewayOption(gatewayCheckBox){
	if(!gatewayCheckBox.checked){
		document.quickFilterForm.resetGateway.value="false";
	} else {
		document.quickFilterForm.resetGateway.value="true";
	}	
}*/

/**
 * Reset the Gateway value. participantSummaryReportForm
 */
function setGatewayOption(gatewayCheckBox){

	if(!gatewayCheckBox.checked){
		document.quickFilterForm.resetGateway.value="false";
		var tempGatewayChecked = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
		tempGatewayChecked.value = "false";
		setFilterFromInput(tempGatewayChecked);
		setFilterFromInput(document.quickFilterForm.resetGateway);
		
	} else {
		document.quickFilterForm.resetGateway.value="true";
		var tempGatewayChecked = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
		tempGatewayChecked.value = "true";
		setFilterFromInput(tempGatewayChecked);
		setFilterFromInput(document.quickFilterForm.resetGateway);
	}	
}

/**
 * Reset the Gateway value. participantSummaryReportForm
 */
function setQuickFilterGatewayOption(gatewayCheckBox){

	if(!gatewayCheckBox.checked){
		document.quickFilterForm.resetGateway.value="false";
		var tempGatewayChecked = document.forms["quickFilterForm"].elements["quickFilterGatewayChecked"];
		tempGatewayChecked.value = "false";
		setFilterFromInput(tempGatewayChecked);
		setFilterFromInput(document.quickFilterForm.resetGateway);
		var tempQuickGatewayChecked = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
		tempQuickGatewayChecked.value = "false";
		tempQuickGatewayChecked.checked = false;
		setFilterFromInput(tempQuickGatewayChecked);
	} else {
		document.quickFilterForm.resetGateway.value="true";
		var tempGatewayChecked = document.forms["quickFilterForm"].elements["quickFilterGatewayChecked"];
		tempGatewayChecked.value = "true";
		setFilterFromInput(tempGatewayChecked);
		setFilterFromInput(document.quickFilterForm.resetGateway);
		var tempQuickGatewayChecked = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
		tempQuickGatewayChecked.checked = true;
		tempQuickGatewayChecked.value = "true";
		setFilterFromInput(tempQuickGatewayChecked);
	}	
}

/**
 * Reset the MA value. participantSummaryReportForm
 */
function setManagedAccountOption(managedAccountCheckBox){

	if(!managedAccountCheckBox.checked){
		document.quickFilterForm.resetManagedAccount.value="false";
		var tempManagedAccountChecked = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
		tempManagedAccountChecked.value = "false";
		setFilterFromInput(tempManagedAccountChecked);
		setFilterFromInput(document.quickFilterForm.resetManagedAccount);
		
	} else {
		document.quickFilterForm.resetManagedAccount.value="true";
		var tempManagedAccountChecked = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
		tempManagedAccountChecked.value = "true";
		setFilterFromInput(tempManagedAccountChecked);
		setFilterFromInput(document.quickFilterForm.resetManagedAccount);
	}	
}
/**
 * Reset the MA value. participantSummaryReportForm
 */
function setQuickFilterManagedAccountOption(managedAccountCheckBox){

	if(!managedAccountCheckBox.checked){
		document.quickFilterForm.resetManagedAccount.value="false";
		var tempManagedAccountChecked = document.forms["quickFilterForm"].elements["quickFilterManagedAccount"];
		tempManagedAccountChecked.value = "false";
		setFilterFromInput(tempManagedAccountChecked);
		setFilterFromInput(document.quickFilterForm.resetManagedAccount);
		var tempQuickManagedAccountChecked = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
		tempQuickManagedAccountChecked.value = "false";
		tempQuickManagedAccountChecked.checked = false;
		setFilterFromInput(tempQuickManagedAccountChecked);
	} else {
		document.quickFilterForm.resetManagedAccount.value="true";
		var tempManagedAccountChecked = document.forms["quickFilterForm"].elements["quickFilterManagedAccount"];
		tempManagedAccountChecked.value = "true";
		setFilterFromInput(tempManagedAccountChecked);
		setFilterFromInput(document.quickFilterForm.resetManagedAccount);
		var tempQuickManagedAccountChecked = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
		tempQuickManagedAccountChecked.checked = true;
		tempQuickManagedAccountChecked.value = "true";
		setFilterFromInput(tempQuickManagedAccountChecked);
	}	
}

 /**
  * Show or hide the customize filter. While displaying the custom filter remove the values from quick filter 
  * and move to custom filer. While hiding the custom filter remove the customer filter 
  * values and reset the quick filter values.
  */
  
function openCustomizeFilter(){
	if(document.getElementById("report_customization_wrapper").style.display == "block"){
		clearOnLoadContributionStatusFilter();
		clearOnLoadEmploymentStatusFilter();
		showOrHideContributionStatus();
		showOrHideEmploymentStatus();
		clearCustomizeFilter();
		toggleDisabledQuickFilter();
		document.forms['quickFilterForm'].elements['showCustomizeFilter'].value = "N";
	    setFilterFromInput(document.forms['quickFilterForm'].elements['showCustomizeFilter']);
		document.getElementById("report_customization_wrapper").style.display = "none";
		doFilter();
	}else{
		document.forms['quickFilterForm'].elements['showCustomizeFilter'].value = "Y";
	    setFilterFromInput(document.forms['quickFilterForm'].elements['showCustomizeFilter']);
		setFilterValues();
		var tempName = document.forms['quickFilterForm'].elements["participantFilter"];
		var showFilter = tempName.options[tempName.selectedIndex].value;
		clearQuickFilterValue(showFilter);
		tempName.selectedIndex = 0;
		setFilterFromSelect2(tempName);
		toggleDisabledQuickFilter();		
		disableOrSuppressGifl();
		disableOrSuppressMA();
		document.getElementById("report_customization_wrapper").style.display = "block";
	}
} 
/**
 * While showing the custom filter clear the quick filter values
 */
function clearQuickFilterValue(showFilter){
		if(showFilter == "cont_status"){
			var tempName = document.forms["quickFilterForm"].elements["quickFilterStatus"];
			tempName.selectedIndex = 0;
			setFilterFromSelect2(tempName);
		}else if(showFilter == "emp_status"){
			var tempName = document.forms["quickFilterForm"].elements["quickFilterEmploymentStatus"];
			tempName.selectedIndex = 0;
			setFilterFromSelect2(tempName);
		}else if(showFilter == "last_name" || showFilter == "blank_val"){
			var tempName = document.forms['quickFilterForm'].elements['quickFilterNamePhrase'];
			tempName.value = "";
			setFilterFromInput(tempName);
		}else if(showFilter == "total_assets"){
			var tempAssetsFrom = document.forms['quickFilterForm'].elements['quickTotalAssetsFrom'];
			tempAssetsFrom.value = "";
			setFilterFromInput(tempAssetsFrom);
			var tempAssetsTo = document.forms['quickFilterForm'].elements['quickTotalAssetsTo'];
			tempAssetsTo.value = "";
			setFilterFromInput(tempAssetsTo);
		} else if(showFilter == "division" ){
			var tempName = document.forms['quickFilterForm'].elements['quickFilterDivision'];
			tempName.value = "";
			setFilterFromInput(tempName);
		}
		var defaultFilter = "blank_val";
		showQuickFilter(defaultFilter);
}

/**
 * While loading the quick filter clear the custom filter values
 */
function clearCustomizeFilter(){
		var tempName = document.forms["reportCustomizationForm"].elements["namePhrase"];
		tempName.value = "";
		setFilterFromInput(tempName);
		
		var tempName = document.forms["reportCustomizationForm"].elements["division"];
		if(tempName != null ) {
			tempName.value = "";
			setFilterFromInput(tempName);
		}
	
		var tempTotalAssetsFrom = document.forms["reportCustomizationForm"].elements["totalAssetsFrom"];
		tempTotalAssetsFrom.value = "";
		setFilterFromInput(tempTotalAssetsFrom);

		var tempTotalAssetsTo = document.forms["reportCustomizationForm"].elements["totalAssetsTo"];
		tempTotalAssetsTo.value = "";
		setFilterFromInput(tempTotalAssetsTo);

		var tempStatus = document.forms["reportCustomizationForm"].elements["status"];
		tempStatus.selectedIndex = 0; 
		setFilterFromInput(tempStatus);
		
		var tempEmpStatus = document.forms["reportCustomizationForm"].elements["employmentStatus"];
		tempEmpStatus.selectedIndex = 0; 
		setFilterFromInput(tempEmpStatus);
}

/**
 * Quick filter temp values will be moved to form variable 
 */
function setFilterValues(){
	var theSelect = document.forms["quickFilterForm"].elements["participantFilter"];
	var showFilter = theSelect.options[theSelect.selectedIndex].value;
	if(showFilter == "last_name"){
		var tempName = document.forms["reportCustomizationForm"].elements["namePhrase"];
		tempName.value = document.forms["quickFilterForm"].elements["quickFilterNamePhrase"].value;
		setFilterFromInput(tempName);
	} else if(showFilter == "division"){
		var tempName = document.forms["reportCustomizationForm"].elements["division"];
		tempName.value = document.forms["quickFilterForm"].elements["quickFilterDivision"].value;
		setFilterFromInput(tempName);
	} else if(showFilter == "cont_status"){
		var tempQuickrStatus = document.forms["quickFilterForm"].elements["quickFilterStatus"];
		var tempStatus = document.forms["reportCustomizationForm"].elements["status"];
		tempStatus.selectedIndex = tempQuickrStatus.selectedIndex;
		setFilterFromSelect2(tempStatus);
	} else if(showFilter == "emp_status"){
		var tempemploymentStatus = document.forms["quickFilterForm"].elements["quickFilterEmploymentStatus"];
		var tempEmpStatus = document.forms["reportCustomizationForm"].elements["employmentStatus"];
		tempEmpStatus.selectedIndex = tempemploymentStatus.selectedIndex;
		setFilterFromSelect2(tempEmpStatus);
	}else if(showFilter == "total_assets"){
		var temptotalAssetsFrom = document.forms["reportCustomizationForm"].elements["totalAssetsFrom"];
		temptotalAssetsFrom.value = document.forms["quickFilterForm"].elements["quickTotalAssetsFrom"].value;
		setFilterFromInput(temptotalAssetsFrom);
		var temptotalAssetsTo = document.forms["reportCustomizationForm"].elements["totalAssetsTo"];
		temptotalAssetsTo.value = document.forms["quickFilterForm"].elements["quickTotalAssetsTo"].value;
		setFilterFromInput(temptotalAssetsTo);
	}
	else if(showFilter == "gifl"){
		setQuickFilterGatewayOption(document.forms["quickFilterForm"].elements["quickFilterGatewayChecked"]);
	}
	else if(showFilter == "ma"){
		setQuickFilterManagedAccountOption(document.forms["quickFilterForm"].elements["quickFilterManagedAccount"]);
	}
}

/**
 * To disable or enable the Quick filter components.
 */
function toggleDisabledQuickFilter(){
	var divValues=new Array("participantFilter","quickFilterNamePhrase");
	var x;
	for (x in divValues){
		var tempName = document.forms["quickFilterForm"].elements[divValues[x]];
		if(tempName){
			tempName.disabled = tempName.disabled ? false : true;
		}
	}

	var tempName = document.getElementById("quick_report");
	if (tempName.disabled == false) {
		document.getElementById("quick_report").disabled=true;
		document.getElementById("quickFilterSubmitDiv").className = "button_disabled button_disabled_no_hover";
	}
}

/**
 *To display the quick filter correspondent filter category.
 */
function selectQuickFilter(){
	var theSelect = document.forms['quickFilterForm'].elements['participantFilter'];
	var showFilter = theSelect.options[theSelect.selectedIndex].value;
	showQuickFilter(showFilter);
}
/**
 *To display the quick filter correspondent filter category.
 */
function showQuickFilter(showFilter){
	var divValue=new Array("div_namePhrase","div_division", "div_totalAssets","div_status","div_employmentstatus","div_gateway","div_managedAccount");
	var newValue = showFilter;
	var x;
	for (x in divValue){
		
		var tempDivValue = document.getElementById(divValue[x]);
		if(tempDivValue){
			if(newValue == "blank_val" && divValue[x] == divValue[0]){
				tempDivValue.style.display = "block";
				document.forms['quickFilterForm'].elements['quickFilterNamePhrase'].disabled = true;
			} else if(newValue == "last_name" && divValue[x] == divValue[0]){
				tempDivValue.style.display = "block";			
				document.forms['quickFilterForm'].elements['quickFilterNamePhrase'].disabled = false;			
			} else if( newValue == "division" && divValue[x] == divValue[1]){
				tempDivValue.style.display = "block";
			} else if( newValue == "total_assets" && divValue[x] == divValue[2]){
				tempDivValue.style.display = "block";
			}else if( newValue == "cont_status" && divValue[x] == divValue[3]){
				tempDivValue.style.display = "block";
			}else if( newValue == "emp_status" && divValue[x] == divValue[4]){
				tempDivValue.style.display = "block";
			}else if( newValue == "gifl" && divValue[x] == divValue[5]){
				tempDivValue.style.display = "block";
			} 
			else if( newValue == "ma" && divValue[x] == divValue[6]){
				tempDivValue.style.display = "block";
			}else {
				tempDivValue.style.display = "none";
			}
		}
	}
	clearFilterValues(newValue);
	
	// to clear the gifl selection from quick filter criteria
	if (newValue != 'gifl') {
		document.quickFilterForm.resetGateway.value = "false";
		if (document.forms["quickFilterForm"].elements["quickFilterGatewayChecked"]) {
			var tempGatewayChecked = document.forms["quickFilterForm"].elements["quickFilterGatewayChecked"];
			tempGatewayChecked.checked = false;
			setFilterFromInput(tempGatewayChecked);
		}
		setFilterFromInput(document.quickFilterForm.resetGateway);
	}
	// to clear the ma selection from quick filter criteria
	if (newValue != 'ma') {
		document.quickFilterForm.resetManagedAccount.value = "false";
		if (document.forms["quickFilterForm"].elements["quickFilterManagedAccount"]) {
			var tempManagedAccountChecked = document.forms["quickFilterForm"].elements["quickFilterManagedAccount"];			
			tempManagedAccountChecked.checked = false;
			setFilterFromInput(tempManagedAccountChecked);
		}
		setFilterFromInput(document.quickFilterForm.resetManagedAccount);
	} 
}

/**
 *Clear the previous entered quick filter category values.
 */
function clearFilterValues(filterName){
	var filterValue=new Array("blank_val","last_name","division", "total_assets","cont_status","emp_status","gifl","ma");
	var x;
	for (x in filterValue){
		if(filterName != filterValue[x]){
			if(filterValue[x] == "last_name"){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterNamePhrase"];
				tempName.value = "";
				setFilterFromInput(tempName);
			}else if( filterValue[x] == "cont_status"){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterStatus"];
				tempName.selectedIndex = 0;
				setFilterFromSelect2(tempName);
			}else if( filterValue[x] == "emp_status"){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterEmploymentStatus"];
				tempName.selectedIndex = 0;
				setFilterFromSelect2(tempName);				
			}else if( filterValue[x] == "total_assets"){
				var assetsFrom = document.forms["quickFilterForm"].elements["quickTotalAssetsFrom"];
				assetsFrom.value = "";
				setFilterFromInput(assetsFrom);
				var assetsTo = document.forms["quickFilterForm"].elements["quickTotalAssetsTo"];
				assetsTo.value = "";
				setFilterFromInput(assetsTo);
			}else if( filterValue[x] == "gifl" && document.forms["quickFilterForm"].elements["quickFilterGatewayChecked"]){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterGatewayChecked"];
				tempName.checked = false;
				document.quickFilterForm.resetGateway.value="false";
			}
			else if( filterValue[x] == "ma" && document.forms["quickFilterForm"].elements["quickFilterManagedAccount"]){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterManagedAccount"];
				tempName.checked = false;
				document.quickFilterForm.resetManagedAccount.value="false";
			}else  if(filterValue[x] == "division"){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterDivision"];
				tempName.value = "";
				setFilterFromInput(tempName);
			}
		}
	}
}
/**
 * On load show the quick filter or custom filter
 */
function doOnload(){
	var customizeFilterVal = document.forms['quickFilterForm'].elements['showCustomizeFilter'].value;
	var newValue = "blank_val";
	var gatewayIndValue = "false";
	var managedAccountIndValue = "false";
	if( document.forms["quickFilterForm"].elements["frmGatewayInd"].value == "true" && 
		document.forms["quickFilterForm"].elements["asOfDate"].selectedIndex > 0){ 
		gatewayIndValue = "true";
	}
	if( document.forms["quickFilterForm"].elements["frmManagedAccountInd"].value == "true" && 
			document.forms["quickFilterForm"].elements["asOfDate"].selectedIndex > 0){ 
		managedAccountIndValue = "true";
		}
	if(customizeFilterVal != "Y"){
		clearOnLoadContributionStatusFilter();
		clearOnLoadEmploymentStatusFilter();
	}
	if(customizeFilterVal == ""){
		showQuickFilter(newValue);
	}else if(customizeFilterVal == "Y"){
		showQuickFilter(newValue);
		toggleDisabledQuickFilter();
		document.getElementById("report_customization_wrapper").style.display = "block";
		//To disable the gifl option in custom filter, if the user selected the ?as of date? other then default date.
		if( gatewayIndValue == "true" ){ 
			disableGiflOption();
		}
		//To disable the ma option in custom filter, if the user selected the ?as of date? other then default date.
		if( managedAccountIndValue == "true" ){ 			
			disableMAOption();
		}
		showOrHideContributionStatus();
		showOrHideEmploymentStatus();
	}else{
		selectQuickFilter();
		if( gatewayIndValue == "true" ){ 
			removeGiflOptionInQuickFilter();
		}
		if( managedAccountIndValue == "true" ){ 			
			removeMAOptionInQuickFilter();
		}
	}
}
/**
 * Sets the filter using the given HTML select object.
 */
function setAsOfDateFilter(theSelect)
{
	var newValue = theSelect.options[theSelect.selectedIndex].value;
	filterMap[theSelect.name] = newValue;
	var selectedDate = theSelect.selectedIndex;
	var gatewayChecked = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
	var managedAccountChecked = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
	if (selectedDate > 0) {
		gatewayChecked.disabled = true;
		gatewayChecked.checked = false;
		managedAccountChecked.disabled = true;
		managedAccountChecked.checked = false;
	} else {
		gatewayChecked.disabled = false;
		managedAccountChecked.disabled = false;
	}	
}

/**
 * In quick filter remove the gifl option and in the custom filter disable the gilf options.
 */

function disableOrSuppressGifl(){
		var frmGatewayInd = document.forms['quickFilterForm'].elements["frmGatewayInd"];
		var participantFilterOptions = document.forms['quickFilterForm'].elements["participantFilter"];		
		clearContributionStatusFilter();
		clearEmploymentStatusFilter();
		if( frmGatewayInd.value == "true"){ 
			var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
			if(asOfDateOptions.selectedIndex > 0){
				var index;
				for(index=0; index < participantFilterOptions.options.length; index++) {
					if(participantFilterOptions.options[index].value == "gifl"){
						participantFilterOptions.remove(index);
						if(document.getElementById("div_gateway").style.display == "block"){
							document.getElementById("div_gateway").style.display = "none";
						}
					}
				}
				disableGiflOption();
				if(participantFilterOptions.selectedIndex == 0){
					var divNamePhrase = document.getElementById("div_namePhrase");
					divNamePhrase.style.display = "block";
				}
			}else if(asOfDateOptions.selectedIndex == 0){
				//To verify the GIFL option already there, if not add otherwise skip the add option.
				var isGiflOptionExist = "false";
				var index;
				for(index=0; index < participantFilterOptions.options.length; index++) {
					if(participantFilterOptions.options[index].value == "gifl"){
						isGiflOptionExist = "true";
					}
				}
			
				if(isGiflOptionExist == "false"){
					var giflOption = document.createElement("OPTION");
					participantFilterOptions.options.add(giflOption);
					giflOption.value = "gifl";
					giflOption.text = "Guaranteed Income feature";
				}
				var gatewayCheckedCheckbox = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
				document.quickFilterForm.resetGateway.value="false";
				gatewayCheckedCheckbox.disabled = false;
				setFilterFromInput(gatewayCheckedCheckbox);
				setFilterFromInput(document.quickFilterForm.resetGateway);
				if(participantFilterOptions.selectedIndex == 0){
					var divNamePhrase = document.getElementById("div_namePhrase");
					divNamePhrase.style.display = "block";
				}
			}
		}
		showOrHideContributionStatus();
		showOrHideEmploymentStatus();	
}

/**
 * In quick filter remove the MA option and in the custom filter disable the MA options.
 */

function disableOrSuppressMA(){
		var frmManagedAccountInd = document.forms['quickFilterForm'].elements["frmManagedAccountInd"];
		var participantFilterOptions = document.forms['quickFilterForm'].elements["participantFilter"];
		clearContributionStatusFilter();
		clearEmploymentStatusFilter();
		if( frmManagedAccountInd.value == "true"){ 
			var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
			if(asOfDateOptions.selectedIndex > 0){
				var index;
				for(index=0; index < participantFilterOptions.options.length; index++) {
					if(participantFilterOptions.options[index].value == "ma"){
						participantFilterOptions.remove(index);
						if(document.getElementById("div_managedAccount").style.display == "block"){
							document.getElementById("div_managedAccount").style.display = "none";
						}
					}
				}
				disableMAOption();
				if(participantFilterOptions.selectedIndex == 0){
					var divNamePhrase = document.getElementById("div_namePhrase");
					divNamePhrase.style.display = "block";
				}
			}else if(asOfDateOptions.selectedIndex == 0){
				//To verify the MA option already there, if not add otherwise skip the add option.
				var isMAOptionExist = "false";
				var index;
				for(index=0; index < participantFilterOptions.options.length; index++) {
					if(participantFilterOptions.options[index].value == "ma"){
						isMAOptionExist = "true";
					}
				}
			
				if(isMAOptionExist == "false"){
					var maOption = document.createElement("OPTION");
					participantFilterOptions.options.add(maOption);
					maOption.value = "ma";
					maOption.text = "Managed Accounts";
				}
				var managedAccountCheckedCheckbox = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
				document.quickFilterForm.resetManagedAccount.value="false";
				managedAccountCheckedCheckbox.disabled = false;
				setFilterFromInput(managedAccountCheckedCheckbox);
				setFilterFromInput(document.quickFilterForm.resetManagedAccount);
				if(participantFilterOptions.selectedIndex == 0){
					var divNamePhrase = document.getElementById("div_namePhrase");
					divNamePhrase.style.display = "block";
				}
			}
		}
		showOrHideContributionStatus();
		showOrHideEmploymentStatus();
}
/**
 * To disable the gifl option, if the as of date is other then default date.
 */

function disableGiflOption() {
		var gatewayCheckedCheckbox = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
		document.quickFilterForm.resetGateway.value="false";
		gatewayCheckedCheckbox.checked = false;
		setFilterFromInput(gatewayCheckedCheckbox);
		gatewayCheckedCheckbox.disabled = true;
		setFilterFromInput(document.quickFilterForm.resetGateway);
}
/**
 * To disable the ma option, if the as of date is other then default date.
 */

function disableMAOption() {
		var maCheckedCheckbox = document.forms["reportCustomizationForm"].elements["managedAccountChecked"];
		document.quickFilterForm.resetManagedAccount.value="false";
		maCheckedCheckbox.checked = false;
		setFilterFromInput(maCheckedCheckbox);
		maCheckedCheckbox.disabled = true;
		setFilterFromInput(document.quickFilterForm.resetManagedAccount);
}

/**
 * On load time the gifl option must be removed, if the as of date other then the default date.
 */
function removeGiflOptionInQuickFilter(){
		var participantFilterOptions = document.forms["quickFilterForm"].elements["participantFilter"];
		var index;
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "gifl"){
					participantFilterOptions.remove(index);
					if(document.getElementById("div_gateway").style.display == "block"){
						document.getElementById("div_gateway").style.display = "none";
					}
			}
		}
}

/**
 * On load time the ma option must be removed, if the as of date other then the default date.
 */
function removeMAOptionInQuickFilter(){
		var participantFilterOptions = document.forms["quickFilterForm"].elements["participantFilter"];
		var index;
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "ma"){
					participantFilterOptions.remove(index);
					if(document.getElementById("div_managedAccount").style.display == "block"){
						document.getElementById("div_managedAccount").style.display = "none";
					}
			}
		}
}

/**
 * Clear the form values.
 */
function doCancel(){
	clearCustomizeFilter();
	document.quickFilterForm.resetGateway.value="false";
	if(document.forms["reportCustomizationForm"].elements["gatewayChecked"]){
		var tempGatewayChecked = document.forms["reportCustomizationForm"].elements["gatewayChecked"];
		tempGatewayChecked.checked = false;
		setFilterFromInput(tempGatewayChecked);
	}
	document.quickFilterForm.resetManagedAccount.value="false";
	if(document.forms["reportCustomizationForm"].elements["ManagedAccountChecked"]){
		var tempManagedAccountChecked = document.forms["reportCustomizationForm"].elements["ManagedAccountChecked"];
		tempManagedAccountChecked.checked = false;
		setFilterFromInput(tempManagedAccountChecked);
	}
	setFilterFromInput(document.quickFilterForm.resetGateway);
	setFilterFromInput(document.quickFilterForm.resetManagedAccount);
	doFilter();
}

/**
 * This function will remove the contribution status from quick filter and also hide the contribution 
 * status select box in quick filter.
 */
function clearContributionStatusFilter(){
	var participantFilterOptions = document.forms['quickFilterForm'].elements["participantFilter"];
	var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
	if(asOfDateOptions.selectedIndex > 0){
		var index;
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "cont_status"){
				participantFilterOptions.remove(index);
				if(document.getElementById("div_status").style.display == "block"){
					document.getElementById("div_status").style.display = "none";
				}
			}
		}
	}else if(asOfDateOptions.selectedIndex == 0){
		
		var isContributionStatusOption = "false";
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "cont_status"){
				isContributionStatusOption = "true";
			}
		}
		if(isContributionStatusOption == "false"){
			var contributionStatusOption = document.createElement("OPTION");
			participantFilterOptions.options.add(contributionStatusOption);
			contributionStatusOption.value = "cont_status";
			contributionStatusOption.text = "Contribution Status";
		}
	}

	var tempParticipantFilter = document.forms['quickFilterForm'].elements["participantFilter"];
	tempParticipantFilter.selectedIndex = 0;
	setFilterFromSelect2(tempParticipantFilter);
	
	var tempQuickFilterStatus = document.forms["quickFilterForm"].elements["quickFilterStatus"];
	tempQuickFilterStatus.selectedIndex = 0;
	setFilterFromSelect2(tempQuickFilterStatus);

	var tempQuickFilterEmploymentStatus = document.forms["quickFilterForm"].elements["quickFilterEmploymentStatus"];
	tempQuickFilterEmploymentStatus.selectedIndex = 0;
	setFilterFromSelect2(tempQuickFilterEmploymentStatus);
	
	var defaultFilter = "blank_val";
	showQuickFilter(defaultFilter);
}

/**
 * This function will remove the employment status from quick filter and also hide the employment 
 * status select box in quick filter.
 */
function clearEmploymentStatusFilter(){
	var participantFilterOptions = document.forms['quickFilterForm'].elements["participantFilter"];
	var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
	if(asOfDateOptions.selectedIndex > 0){
		var index;
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "emp_status"){
				participantFilterOptions.remove(index);
				if(document.getElementById("div_employmentstatus").style.display == "block"){
					document.getElementById("div_employmentstatus").style.display = "none";
				}
			}
		}
	}else if(asOfDateOptions.selectedIndex == 0){
		
		var isEmploymentStatusOption = "false";
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "emp_status"){
				isEmploymentStatusOption = "true";
			}
		}
		if(isEmploymentStatusOption == "false"){
			var employmentStatusOption = document.createElement("OPTION");
			participantFilterOptions.options.add(employmentStatusOption);
			employmentStatusOption.value = "emp_status";
			employmentStatusOption.text = "Employment Status";
		}
	}

	var tempParticipantFilter = document.forms['quickFilterForm'].elements["participantFilter"];
	tempParticipantFilter.selectedIndex = 0;
	setFilterFromSelect2(tempParticipantFilter);
	
	var tempQuickFilterEmpStatus = document.forms["quickFilterForm"].elements["quickFilterEmploymentStatus"];
	tempQuickFilterEmpStatus.selectedIndex = 0;
	setFilterFromSelect2(tempQuickFilterEmpStatus);
		
	var defaultFilter = "blank_val";
	showQuickFilter(defaultFilter);
}


/**
 * This function will remove the contribution status from quick filter and also hide the contribution 
 * status select box in quick filter - onload time or opening the quick filter using the custom filter link.
 */
 
function clearOnLoadContributionStatusFilter(){
	var participantFilterOptions = document.forms['quickFilterForm'].elements["participantFilter"];
	var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
	if(asOfDateOptions.selectedIndex > 0){
		var index;
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "cont_status"){
				participantFilterOptions.remove(index);
				if(document.getElementById("div_status").style.display == "block"){
					document.getElementById("div_status").style.display = "none";
				}
			}
		}
	}
}

/**
 * This function will remove the employment status from quick filter and also hide the employment 
 * status select box in quick filter - onload time or opening the quick filter using the custom filter link.
 */
 
function clearOnLoadEmploymentStatusFilter(){
	var participantFilterOptions = document.forms['quickFilterForm'].elements["participantFilter"];
	var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
	if(asOfDateOptions.selectedIndex > 0){
		var index;
		for(index=0; index < participantFilterOptions.options.length; index++) {
			if(participantFilterOptions.options[index].value == "emp_status"){
				participantFilterOptions.remove(index);
				if(document.getElementById("div_employmentstatus").style.display == "block"){
					document.getElementById("div_employmentstatus").style.display = "none";
				}
			}
		}
	}
}

/**
 *  To hide or show the custom filter Contribution Status 
 *  if as of date is not default then hide the contribution status filter 
 *  from the custom filter otherwise show the status.
 */
function showOrHideContributionStatus(){
	var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
	
	if(asOfDateOptions.selectedIndex > 0){
		document.getElementById("div_show_status").style.display = "none";
		document.getElementById("div_show_status_option").style.display = "none";

		var tempStatus = document.forms["reportCustomizationForm"].elements["status"];
		tempStatus.selectedIndex = 0; 
		setFilterFromInput(tempStatus);


	}else if(asOfDateOptions.selectedIndex == 0){
		document.getElementById("div_show_status").style.display = "block";
		document.getElementById("div_show_status_option").style.display = "block";
	}
}

/**
 *  To hide or show the custom filter Employment Status 
 *  if as of date is not default then hide the Employment status filter 
 *  from the custom filter otherwise show the status.
 */
function showOrHideEmploymentStatus(){
	var asOfDateOptions = document.forms["quickFilterForm"].elements["asOfDate"];
	
	if(asOfDateOptions.selectedIndex > 0){
		document.getElementById("div_show_employment_status").style.display = "none";
		document.getElementById("div_show_employment_status_option").style.display = "none";

		var tempStatus = document.forms["reportCustomizationForm"].elements["employmentStatus"];
		tempStatus.selectedIndex = 0; 
		setFilterFromInput(tempStatus);


	}else if(asOfDateOptions.selectedIndex == 0){
		document.getElementById("div_show_employment_status").style.display = "block";
		document.getElementById("div_show_employment_status_option").style.display = "block";
	}
}

