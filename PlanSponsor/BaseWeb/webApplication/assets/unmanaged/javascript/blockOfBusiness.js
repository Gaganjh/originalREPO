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
			if(strUser!=''){
			document.getElementById(strUser).style.display="none";
			}
		} else {
			var sel=quickFilterOptions.options[quickFilterOptions.selectedIndex].value;
			if(sel!=''){
			document.getElementById(sel).style.display="block";
				}
		}

		//This is done to either show or not show the DIV section of "Firm Search".
		if(quickFilterOptions.options[index].selected) {
			if (document.getElementById("quickFilterFirmSearchDIV")) {
				if (quickFilterOptions.options[index].value == 'firmName') {
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
		if(select!=''){
			document.getElementById(quickFilterOptions.options[index].value).value="";
		}
	}
}

/**
 * This method is used by both Quick Filter and Advanced Filter.
 * This method is used to submit the form. At any one time, we are submitting only one form: either 
 * Quick Filter or Advanced Filter.
 */
function submitBOB(bobForm) {
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

	bobForm.task.value = "filter";
	bobForm.submit();
}

/**
 * This method is used by both Quick Filter and Advanced Filter.
 * This method is used to reset the form. At any one time, we are submitting only one form: either 
 * Quick Filter or Advanced Filter.
 */
function resetBOB(bobForm) {
	bobForm.task.value = "reset";
	bobForm.submit();
}

/**
 * This method is used when the user clicks on the Customize Report button to enable the Quick Filter options
 * This method is used to reset the form. At any one time, we are submitting only one form: either 
 * Quick Filter or Advanced Filter.
 */
function enableQuickFilter(bobForm) {
	bobForm.task.value = "default";
	bobForm.submit();
}

/**
 * This method will be called when the user clicks on a Tab.
 * We are submitting the form to use the search filters entered by user
 * If no filters used, then we just navigate to selected tab without submitting form.
 */
function gotoTab(url) {	
	var reportURL = new URL(url);
	if(document.getElementById("showAdvanceFilterID").value == "true"){
		bobAdvancedFilterForm = document.getElementById("advancedFilterForm");			
		bobAdvancedFilterForm.action = reportURL.encodeURL();
		bobAdvancedFilterForm.submit();				
	}else if(document.getElementById("quickFilterSelected").selectedIndex != 0){	
		bobQuickFilterForm = document.getElementById("quickFilterForm");			
		bobQuickFilterForm.action = reportURL.encodeURL();
		bobQuickFilterForm.submit();		
	}else{
		location.href = reportURL.encodeURL();
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
		quickFilterOptions.selectedIndex == 0;
		//quickFilterOptions.options[0].selected=true;
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
			if(selectedFilter!= null && selectedFilter != 'blankCode'&& selectedFilter != 'blank'){
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
	var quickFilterFirmID = document.getElementById("quickFilterFirmIDSelected");
	var bdFirmID = document.getElementById("firmIDSelected");
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
 * This method will be called when user clicks on a Contract Link in BOB page.
 * @param contractNumber
 * @return
 */
function goToContractInformationPage(contractNumber) {
	 var reportURL = new URL("/do/bob/blockOfBusiness/Active/");
	 reportURL.setParameter("contractNbr", contractNumber);
	 location.href = reportURL.encodeURL();
}

/**
 * This method will be called when user clicks on a Contract Link Pending Tab in BOB page.
 * @param contractNumber
 * @return
 */
function goToSecureDocumentUploadPage(contractNumber) {
	 var reportURL = new URL("/do/bob/blockOfBusiness/Pending/");
	 reportURL.setParameter("contractNbr", contractNumber);
	 location.href = reportURL.encodeURL();
}

//Below functionality for displaying Pop up window using YAHOO Utils

var oViewOverlayPanel;
var overlayLoadingPanel;

//This array object is used to pass as an argument to the call-back function

var callbackArgument = new Array();

var viewOverlayActionURL = {
	"RiaFees" : {
		"RiaBpsFee" : "/do/bob/blockOfBusiness/Active/?task=viewBpsFeeDetails&isPageRegularlyNavigated=true",
		"RiaTieredFee" : "/do/bob/blockOfBusiness/Active/?task=viewTieredFeeDetails&isPageRegularlyNavigated=true",
		"RiaBlendFee" : "/do/bob/blockOfBusiness/Active/?task=viewBlendFeeDetails&isPageRegularlyNavigated=true",
	}
}

var viewOverlayUtilities = {

	// Utility function for parsing JSON string
	parseResponseToJSON : function(o) {
		try {
			return YAHOO.lang.JSON.parse(o.responseText);
		} catch (x) {
			return "parseError";
		}
	},

	// Asynchronous request call to the server. 
	doAsyncRequest : function(actionPath, callbackFunction) {
		YAHOO.util.Connect.setForm(document.planReviewReportForm);
		// Make a request
		var request = YAHOO.util.Connect.asyncRequest('GET', actionPath,
				callbackFunction);
	},

	// Set call back arguments  
	setCallbackArgument : function(argument) {
		callbackArgument.splice(0, callbackArgument.length);
		callbackArgument[0] = argument;
	},

	// Generic function to handle a failure in the server response  
	handleFailure : function(o) {
		o.argument = null;
		viewOverlayUtilities.hideWaitPanel();
	},

	// Shows loading panel message
	showWaitPanel : function() {
		waitPanel = document.getElementById("wait_c");
		if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
			overlayLoadingPanel = new YAHOO.widget.Panel("wait", {
				width : "250px",
				height : "50px",
				fixedcenter : true,
				close : false,
				draggable : false,
				zindex : 4,
				modal : true,
				visible : false,
				constraintoviewport : true
			});
			overlayLoadingPanel
					.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
			overlayLoadingPanel.render(document.body);
			overlayLoadingPanel.show();
		}
	},

	/**
	 * hides the loading panel
	 */
	hideWaitPanel : function() {
		overlayLoadingPanel.hide();
	}

}

function doCloseOverlay() {
	viewOverlayUtilities.hideWaitPanel();
	oViewOverlayPanel.hide();
}

//Displays view of RIA BPS Maximum Overlay panel to user.
function showViewOverlayPanel() {
	if (oViewOverlayPanel == null) {
		oViewOverlayPanel = new YAHOO.widget.Panel("viewOverlayPanel", {
			width : "500px",
			height : "100%",
			draggable : false,
			close : false,
			zindex : 4,
			visible : true,
			modal : true
		});
	}
	oViewOverlayPanel.render();
	oViewOverlayPanel.center();

	oViewOverlayPanel.show();
}

//Call back handler for displaying the RIA BPS Maximum popup window.
var callback_uploadViewOverlay = {
	cache : false,
	success : function(o) {

		document.getElementById("viewOverlayPanel").innerHTML = o.responseText;
		showViewOverlayPanel();
		viewOverlayUtilities.hideWaitPanel();
	},
	failure : viewOverlayUtilities.handleFailure,
	argument : callbackArgument
};

function viewTieredFeeOverlay(contract, index,proposal) {
//	alert("contract : |" + contract + "| proposal : |" + proposal + "|");
	var tieredFeeUrl = viewOverlayActionURL["RiaFees"].RiaTieredFee;
	tieredFeeUrl = tieredFeeUrl+ "&contractNum="+contract +"&propNum=" +proposal;
	viewOverlayUtilities.showWaitPanel();
	viewOverlayUtilities.doAsyncRequest(
			tieredFeeUrl,
			callback_uploadViewOverlay);
}

function viewBlendFeeOverlay(contract, index,proposal) {
//	alert("contract : |" + contract + "| proposal : |" + proposal + "|");
	var blendFeeUrl = viewOverlayActionURL["RiaFees"].RiaBlendFee;
	blendFeeUrl = blendFeeUrl+ "&contractNum="+contract +"&propNum=" +proposal;
	viewOverlayUtilities.showWaitPanel();
	viewOverlayUtilities.doAsyncRequest(
			blendFeeUrl,
			callback_uploadViewOverlay);
}
