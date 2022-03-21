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
  * Show or hide the customize filter. While displaying the custom filter remove the values from quick filter 
  * and move to custom filer. While hiding the custom filter remove the customer filter 
  * values and reset the quick filter values.
  */
  
function openCustomizeFilter(){
	if(document.getElementById("report_customization_wrapper").style.display == "block"){
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
		document.getElementById("report_customization_wrapper").style.display = "block";
	}
} 
/**
 * While showing the custom filter clear the quick filter values
 */
function clearQuickFilterValue(showFilter){
		if(showFilter == "last_name" || showFilter == "blank_val"){
			var tempName = document.forms['quickFilterForm'].elements['quickFilterNamePhrase'];
			tempName.value = "";
			setFilterFromInput(tempName);
		}else if(showFilter == "first_name" ){
			var tempName = document.forms['quickFilterForm'].elements['quickFilterFirstName'];
			tempName.value = "";
			setFilterFromInput(tempName);
		}else if(showFilter == "ssn" ){
			var tempName1 = document.forms['quickFilterForm'].elements['quickFilterSsnOne'];
			var tempName2 = document.forms['quickFilterForm'].elements['quickFilterSsnTwo'];
			var tempName3 = document.forms['quickFilterForm'].elements['quickFilterSsnThree'];
			tempName1.value = "";
			tempName2.value = "";
			tempName3.value = "";
			setFilterFromInput(tempName1);
			setFilterFromInput(tempName2);
			setFilterFromInput(tempName3);
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
		
		var tempName = document.forms["reportCustomizationForm"].elements["firstName"];
		if(tempName != null ) {
			tempName.value = "";
			setFilterFromInput(tempName);
		}
		var tempName = document.forms["reportCustomizationForm"].elements["ssnOne"];
		if(tempName != null ) {
			tempName.value = "";
			setFilterFromInput(tempName);
		}
		var tempName = document.forms["reportCustomizationForm"].elements["ssnTwo"];
		if(tempName != null ) {
			tempName.value = "";
			setFilterFromInput(tempName);
		}
		var tempName = document.forms["reportCustomizationForm"].elements["ssnThree"];
		if(tempName != null ) {
			tempName.value = "";
			setFilterFromInput(tempName);
		}
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
	} else if(showFilter == "first_name"){
		var tempName = document.forms["reportCustomizationForm"].elements["firstName"];
		tempName.value = document.forms["quickFilterForm"].elements["quickFilterFirstName"].value;
		setFilterFromInput(tempName);
	} else if(showFilter == "ssn"){
		var tempName1 = document.forms["reportCustomizationForm"].elements["ssnOne"];
		var tempName2 = document.forms["reportCustomizationForm"].elements["ssnTwo"];
		var tempName3 = document.forms["reportCustomizationForm"].elements["ssnThree"];
		tempName1.value = document.forms["quickFilterForm"].elements["quickFilterSsnOne"].value;
		tempName2.value = document.forms["quickFilterForm"].elements["quickFilterSsnTwo"].value;
		tempName3.value = document.forms["quickFilterForm"].elements["quickFilterSsnThree"].value;
		setFilterFromInput(tempName1);
		setFilterFromInput(tempName2);
		setFilterFromInput(tempName3);
	} 
}

/**
 * To disable or enable the Quick filter components.
 */
function toggleDisabledQuickFilter(){
	var divValues=new Array("participantFilter");
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
	var divValue=new Array("div_namePhrase","div_firstName", "div_ssn");
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
			} else if( newValue == "first_name" && divValue[x] == divValue[1]){
				tempDivValue.style.display = "block";
			} else if( newValue == "ssn" && divValue[x] == divValue[2]){
				tempDivValue.style.display = "block";
			} else {
				tempDivValue.style.display = "none";
			}
		}
	}
	clearFilterValues(newValue);
	
}

/**
 *Clear the previous entered quick filter category values.
 */
function clearFilterValues(filterName){
	var filterValue=new Array("blank_val","last_name","first_name","ssn");
	var x;
	for (x in filterValue){
		if(filterName != filterValue[x]){
			if(filterValue[x] == "last_name"){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterNamePhrase"];
				tempName.value = "";
				setFilterFromInput(tempName);
			} else  if(filterValue[x] == "first_name"){
				var tempName = document.forms["quickFilterForm"].elements["quickFilterFirstName"];
				tempName.value = "";
				setFilterFromInput(tempName);
			} else  if(filterValue[x] == "ssn"){
				var tempName1 = document.forms["quickFilterForm"].elements["quickFilterSsnOne"];
				var tempName2 = document.forms["quickFilterForm"].elements["quickFilterSsnTwo"];
				var tempName3 = document.forms["quickFilterForm"].elements["quickFilterSsnThree"];
				tempName1.value = "";
				tempName2.value = "";
				tempName3.value = "";
				setFilterFromInput(tempName1);
				setFilterFromInput(tempName2);
				setFilterFromInput(tempName3);
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
	if(customizeFilterVal != "Y"){
	}
	if(customizeFilterVal == ""){
		showQuickFilter(newValue);
	}else if(customizeFilterVal == "Y"){
		showQuickFilter(newValue);
		toggleDisabledQuickFilter();
		document.getElementById("report_customization_wrapper").style.display = "block";
	}else{
		selectQuickFilter();
	}
}

/**
 * Clear the form values.
 */
function doCancel(){
	clearCustomizeFilter();
	doFilter();
}

