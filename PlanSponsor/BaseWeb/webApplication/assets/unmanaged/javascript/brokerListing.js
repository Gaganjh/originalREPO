/**
 * This method is called during onLoad.
 * 1) This method will set the Quick Filter.
 * 2) The Custom Filter section is either Displayed or Hidden.
 */
function doOnload() {
	showFilterExpression("false");
	
	//Disable Quick Filtering section if "Advance Filtering" section is being shown.
	if (document.getElementById("showAdvanceFilterID").value=="true") {
		resetQuickFilterValues();
		document.getElementById("quickFilterSelected").value="";
		disableQuickFilterCriteria(true);
	}
	showAdvancedFilter();
	// Enable the FirmSearch Containers.
	enableAdvFirmSearchDivSection();
}

/**
 * This method enables the FirmSearch Div Section for Advanced Filter section. The FirmSearch Div Section has a style of "display:none;" by default. 
 * For the Firm Search Autocomplete to work properly, the FirmSearch Div Section need to be displayed.
 *
 * These 2 Elements are disabled by default so that they do not show up during page load up. After page load up is done, we are re-enabling them.
 */
function enableAdvFirmSearchDivSection() {
	if (document.getElementById("firmSearchDivID")) {
		document.getElementById("firmSearchDivID").style.display="block";
	}
}

/**
 * This method is used by both Quick Filter and Advanced Filter.
 * This method is used to submit the form. At any one time, we are submitting only one form: either 
 * Quick Filter or Advanced Filter.
 */
function submitBrokerListing(brokerListingForm) {
    // Only instantiate if one doesn't exist.  
    // Otherwise drop to the show() method.
    loadingPanel = new YAHOO.widget.Panel("wait",  
                        {   width: "250px", 
                            height:"50px",
                            fixedcenter: true, 
                            close: false, 
                            draggable: false, 
                            zindex:4,
                            modal: true,
                            visible: false,
                            constraintoviewport: true
                        } 
                    );
    loadingPanel.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
    loadingPanel.render(document.body);
    loadingPanel.show();    

	brokerListingForm.task.value = "filter";
	brokerListingForm.submit();
}

/**
 * This method is used by both Quick Filter and Advanced Filter.
 * This method is used to reset the form. At any one time, we are submitting only one form: either 
 * Quick Filter or Advanced Filter.
 */
function resetBrokerListing(brokerListingForm) {
	brokerListingForm.task.value = "reset";
	brokerListingForm.submit();
}

/**
 * This method is called when the user clicks on the "Customize Report" Button to enable the Quick Filter.
 * This method is used to reset the form. At any one time, we are submitting only one form: either 
 * Quick Filter or Advanced Filter.
 */
function enableQuickFilter(brokerListingForm) {
	brokerListingForm.task.value = "default";
	brokerListingForm.submit();
}

function gotoBOB(financialRepName) {
	 var reportURL = new URL("/do/bob/blockOfBusiness/Active/");
	 bobQuickFilterForm = document.getElementById("quickFilterForm");
	 reportURL.setParameter("asOfDate", bobQuickFilterForm.asOfDateSelected.value);
	 reportURL.setParameter("financialRepName", financialRepName);
	 location.href = reportURL.encodeURL();
}

/**
 * This method is used by the Quick Filter.
 *  1) Depending on the Filter criteria selected, the appropriate Filter Expression is shown
 * and the rest of Filter Expression elements are not displayed. 
 *  2) The values of all the Quick Filter Expressions are reset to default value (blank).
 * 
 */
function showFilterExpression(resetQuickFilter) {
	quickFilterOptions = document.getElementById("quickFilterSelected");
	var index;
	for(index=0; index < quickFilterOptions.options.length; index++) {
		if(quickFilterOptions.selectedIndex !=index ) {
			var strUser = quickFilterOptions.options[index].value;
			if( strUser!=''){
			document.getElementById(strUser).style.display="none";
			}
		} else {
			var sel= quickFilterOptions.options[quickFilterOptions.selectedIndex].value;
			if( sel!=''){
			document.getElementById(sel).style.display="block";
				}
		}

		//This is done to either show or not show the DIV section of "Firm Search".
		if(quickFilterOptions.options[index].selected) {
			if (document.getElementById("quickFilterFirmSearchDIV")) {
				if (quickFilterOptions.options[index].value == 'bdFirmName') {
					document.getElementById("quickFilterFirmSearchDIV").style.display="block";
				} else {
					document.getElementById("quickFilterFirmSearchDIV").style.display="none";
				}
			}
		}
	}
	if(resetQuickFilter=="true") {
		resetQuickFilterValues();
	}
}

/**
 * This method is used by the Quick Filter.
 * This method would reset the Quick Filter Expression values whenever the Quick Filter is changed.
 */
function resetQuickFilterValues() {
	quickFilterOptions = document.getElementById("quickFilterSelected");
	var index;
	for(index=0; index < quickFilterOptions.options.length; index++) {
		var select = quickFilterOptions.options[index].value;
		if( select!=''){
			document.getElementById(quickFilterOptions.options[index].value).value="";
		}
	}
}

/**
 * This method is called whenever the "Customize Report" link is clicked. 
 * This method would DIsplay / UnDisplay the "Advanced Filtering" section.
 * 1) During Display of "Advanced Filtering" section, values are passed from currently selected Filtering Criteria to corresponding
 * filtering criteria in "Advanced Filtering" section. Also, the Quick Filtering section is disabled.
 * 2) During UnDisplay of "Advanced Filtering" section, the page is reset to show Quick Filter and default report.
 */
function openCloseAdvancedFilter() {
	bobQuickFilterForm = document.getElementById("quickFilterForm");
	if (document.getElementById("showAdvanceFilterID").value=="true") {
		//document.getElementById("showAdvanceFilterID").value="false";
		//disableQuickFilterCriteria(false);
		enableQuickFilter(bobQuickFilterForm);
	} else {
		document.getElementById("showAdvanceFilterID").value="true";
		passValues();
		quickFilterOptions = document.getElementById("quickFilterSelected");
		//quickFilterOptions.selectedIndex == 0;
		quickFilterOptions.options[0].selected=true;
		showFilterExpression('true');
		disableQuickFilterCriteria(true);
	}
	showAdvancedFilter();
}

/**
 * This method is used when "Customize Report" link is clicked. 
 * This method is used to pass values from Quick Filtering Expression of the currently selected Quick Filtering criteria
 * to corresponding Filtering criteria in "Advanced Filtering" section.
 */
function passValues() {
	bobQuickFilterForm = document.getElementById("quickFilterForm");
	bobAdvancedFilterForm = document.getElementById("advancedFilterForm");

	quickFilterOptions = bobQuickFilterForm.quickFilterSelected;
	var index;
	for(index=0; index < quickFilterOptions.options.length; index++) {
		if(quickFilterOptions.options[index].selected) {
			var selectedFilter = quickFilterOptions.options[index].value;
			if(selectedFilter!= null && selectedFilter != 'blankCode' && selectedFilter != 'blank'){
				if(document.getElementById(selectedFilter).type == 'text'){
				document.getElementById("adv"+ selectedFilter).value = document.getElementById(selectedFilter).value;
				break;
				}else {
					var selectedIndex = document.getElementById(selectedFilter).selectedIndex;
					var advFilter = document.getElementById("adv"+ selectedFilter);
					for(i=0;i< advFilter.options.length; i++) {
						if(i == selectedIndex) {
							advFilter.options[i].selected=true;
							break;
						}
					}
					break;
				}
			}
		}
	}
	passFirmIDValues();
}

/**
 * This function will pass the FirmID value. THis is not passed in the normal circumstance, 
 * when the Auto complete is being used.
 */
function passFirmIDValues() {
	var quickFilterFirmID = document.getElementById("quickFilterBDFirmID");
	var bdFirmID = document.getElementById("bdFirmID");
	if (quickFilterFirmID != null && bdFirmID != null) {
		bdFirmID.value = quickFilterFirmID.value;
	}
}

/**
 * This method is used when "Customize Report" link is clicked. 
 * This method disables/enables the Quick Filtering section.
 */
function disableQuickFilterCriteria(disableOrEnable) {
	bobQuickFilterForm = document.getElementById("quickFilterForm");
	bobQuickFilterForm.quickFilterSelected.disabled=disableOrEnable;
	quickFilterOptions = bobQuickFilterForm.quickFilterSelected;
	var index;
	for(index=0; index < quickFilterOptions.options.length; index++) {
		document.getElementById(quickFilterOptions.options[index].value).disabled=disableOrEnable;
	}
	
	if (disableOrEnable == true) {
		document.getElementById("quickFilterSubmit").disabled=disableOrEnable;
		document.getElementById("quickFilterSubmitDiv").className = "button_disabled button_disabled_no_hover";
	}
}


/**
 * This method is used to Display / Undisplay the "Advance Filtering" section.
 */
function showAdvancedFilter() {
	if (document.getElementById("showAdvanceFilterID").value=="true") {
		document.getElementById("report_customization_wrapper").style.display = "block";
		setRegionAndDivisionAfterAdvFilterIsOpen();
	} else {
		document.getElementById("report_customization_wrapper").style.display = "none";
	}
}

/**
 * This method will copy the value of "as of Date" selected in Quick Filter Form to "as of Date" in Advance Filter Form.
 */
function setAsOfDateInAdvFilterForm() {
	bobQuickFilterForm = document.getElementById("quickFilterForm");
	bobAdvancedFilterForm = document.getElementById("advancedFilterForm");
	bobAdvancedFilterForm.asOfDateSelected.value = bobQuickFilterForm.asOfDateSelected.value;
}

/**
 * This method is called whenever the user changes the Region in Region dropdown. This method sets the Division dropdown based
 * on the Region selected.
 */
function setDivision(regionID, divisionID) {
	if (regionID != null && divisionID != null) {
		var divisionIDSelect = null;
		var divisionPartyIDSelect = null;

		// Find out the Division which needs to be selected.
		var regionToDivisionMap = document.getElementById("regionToDivisionIDMap");
		if (regionToDivisionMap != null) {
			regionIDOptions = document.getElementById(regionID);
			if (regionIDOptions != null) {
				for(index=0; index < regionToDivisionMap.options.length; index++) {
					if (regionToDivisionMap.options[index].value == regionIDOptions.options[regionIDOptions.selectedIndex].value) {
						divisionIDSelect = regionToDivisionMap.options[index].text;
						break;
					}	
				}
			}
		}
		// Find out the Division Party ID from the given ID.
		if (divisionIDSelect != null) {
			var divisionIDToPartyIDOptions = document.getElementById("divisionIDToPartyIDMap");
			if (divisionIDToPartyIDOptions != null) {
				for(index=0; index < divisionIDToPartyIDOptions.options.length; index++) {
					if (divisionIDToPartyIDOptions.options[index].value == divisionIDSelect) {
						divisionPartyIDSelect = divisionIDToPartyIDOptions.options[index].text;
						break;
					}	
				}
			}
		}
		// Set the Division.
		divisionDropDown = document.getElementById(divisionID);
		if (divisionDropDown != null) {
			for(index=0; index < divisionDropDown.options.length; index++) {
				if (divisionPartyIDSelect != null) {
					if (divisionDropDown.options[index].value == divisionPartyIDSelect){
						divisionDropDown.selectedIndex == index;
						divisionDropDown.options[index].selected=true;
						break;
					}
				} else {
					divisionDropDown.selectedIndex == 0;
					divisionDropDown.options[0].selected=true;
					break;
				}
			}
		}
	}
}


/**
 * This method is called whenever the user changes the Division in Division dropdown. This method changes the Region Dropdown
 * so that the Region Dropdown contains only those Regions which belong to the selected Division.
 */
function setRegion(regionID, divisionID) {
	if (regionID != null && divisionID != null) {
		divisionDropDown = document.getElementById(divisionID);
		if (divisionDropDown.selectedIndex == 0) {
			// Show all Regions..
			regionDropDown = document.getElementById(regionID);
			removeAllOptions(regionDropDown);

			regionStoreDropDown = document.getElementById("regionStore");
			for(index=0; index < regionStoreDropDown.options.length; index++) {
				addOption(regionDropDown, regionStoreDropDown.options[index].text, regionStoreDropDown.options[index].value);
			}
		} else {
			// Show Regions belonging to current Division.
			
			// Get the division selected.
			var divisionPartyIDSelected = divisionDropDown.options[divisionDropDown.selectedIndex].value;
			
			// Find out division ID from the given Party ID.
			var divisionIDSelect = null;
			var divisionIDToPartyIDOptions = document.getElementById("divisionIDToPartyIDMap");
			if (divisionIDToPartyIDOptions != null) {
				for(index=0; index < divisionIDToPartyIDOptions.options.length; index++) {
					if (divisionIDToPartyIDOptions.options[index].text == divisionPartyIDSelected) {
						divisionSelected = divisionIDToPartyIDOptions.options[index].value;
						break;
					}	
				}
			}

			// Get the regions belonging to the division selected.
			regionDivisionMap = document.getElementById("regionToDivisionIDMap");
			var regionsSelected = new Array();
			var counter = 0;
			for(index=0; index < regionDivisionMap.options.length; index++) {
				if (regionDivisionMap.options[index].text == divisionSelected) {
					regionsSelected[counter++] = regionDivisionMap.options[index].value;
				}
			}
			
			//Set the regions dropdown.
			regionDropDown = document.getElementById(regionID);
			removeAllOptions(regionDropDown);

			regionStoreDropDown = document.getElementById("regionStore");
			for(index=0; index < regionStoreDropDown.options.length; index++) {
				if (index == 0) {
					//Add the default value..
					addOption(regionDropDown, regionStoreDropDown.options[index].text, regionStoreDropDown.options[index].value);
					continue;
				}
				for (regionSelected in regionsSelected) {
					if (regionsSelected[regionSelected] == regionStoreDropDown.options[index].value) {
						addOption(regionDropDown, regionStoreDropDown.options[index].text, regionStoreDropDown.options[index].value);
						break;
					}
				}
			}
		}
	}
}

/**
 * This method adds a new option to the Dropdown "selectbox".
 */
function addOption(selectbox, text, value) {
	var optn = document.createElement("OPTION");
	optn.text = text;
	optn.value = value;
	selectbox.options.add(optn);
}

/**
 * This method removes all the Options from the Dropdown "selectbox".
 */
function removeAllOptions(selectbox) {
	var i;
	for(i=selectbox.options.length-1;i>=0;i--) {
		selectbox.remove(i);
	}
}

