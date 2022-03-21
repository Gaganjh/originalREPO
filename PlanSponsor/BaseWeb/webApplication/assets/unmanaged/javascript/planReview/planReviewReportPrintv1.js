//This javascript file is used to scrpiting common functionality in ContractReviewReportPrint Pages
$(document).ready(function(){
	
	if ($('.selectContracts').length == 0) {
		$("#select_all").prop('disabled', true);
		$(".blue-btn_big").prop('disabled', true).addClass("disabled-grey-btn");
			} 
			 if ($('.selectContracts').length == 1) {
				$("#select_all").prop('disabled', true);
			 } 
			 
			$("#select_all").on('click',
			function() {
					var checked_status = this.checked;
					var index = 0;
					$(".selectContracts").each(
					function() {
						
						if ($(this).prop('disabled') != 'disabled') {
							this.checked = checked_status;
							this.value = true;
							$("#selectContracts" + index).val(this.checked);
							$("#allContractSelected").val(this.checked);
						}
						index = index + 1;
					});
					
				});
			
			if($("[id^=selectContracts]").is(':hidden') && $('.selectContracts').length == 0){
				$(".blue-btn_big").prop('disabled', false).removeClass("disabled-grey-btn");
			}
			
			$(".selectContracts").on('click',
					function() {
						if (this.checked != $("#select_all").prop('checked')) {
							$("#select_all").prop('checked', false);
							$("#allContractSelected").val(false);
						}
						var index = this.id;
						$("#selectContracts" + index).val(this.checked);

						
			});
			if($('.selectContracts:checkbox:checked').length == $('.selectContracts:checkbox').length)
			{
				$('#select_all').checked='checked';
			}
});

var oPanelDetail;
var oPanelPreview;
var oPanelConfirm;
var oPanelIndex;
var loadingPanel;

//This array object is used to pass as an argument to the call-back function
var callbackArgument = new Array();     
    
// Preview
YAHOO.util.Event.addListener("preview", "click", displayPreview);
YAHOO.util.Event.addListener("confirm", "click", displayConfirm);
/**
* An associated JavaScript array object to store the action URLs.
*/
var actionURL = {
    "bobPlanReviewPrint" : {
        "Shipping"          : "/do/bob/planReview/Print/?task=printShipping&isPageRegularlyNavigated=true",
        "PrintConfirm"  : "/do/bob/planReview/Print/?task=printConfirm&isPageRegularlyNavigated=true"
    },
    "contractPlanReviewPrint" : {
        "Shipping"          : "/do/bob/contract/planReview/Print/?task=printShipping&isPageRegularlyNavigated=true",
        "PrintConfirm"  : "/do/bob/contract/planReview/Print/?task=printConfirm&isPageRegularlyNavigated=true"
    }
}

var utilities = {

    // Utility function for parsing JSON string
    parseResponseToJSON : function(o) {
        try { 
            return YAHOO.lang.JSON.parse(o.responseText); 
        } catch (x) { 
            return "parseError"; 
        } 
    },
    
    // Asynchronous request call to the server. 
    doAsyncRequest : function(actionPath, callbackFunction,jsonObj) {
    	YAHOO.util.Connect.setForm(document.getElementById('planReviewPrintForm'));
    	//YAHOO.util.Connect.setForm(document.PlanReviewPrintForm,true,true);
        // Make a request
        var request = YAHOO.util.Connect.asyncRequest('POST', actionPath+"&planreviewPrintJsonObj="+jsonObj, callbackFunction);
        //var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
    },
    
    // Set call back arguments  
    setCallbackArgument : function (argument) {
        // remove the array elements. 
        // Note: don't declare a new Array object, the call-back function will not take the new Object
        callbackArgument.splice(0, callbackArgument.length);
        callbackArgument[0] = argument;
    },
    
    
    // Generic function to handle a failure in the server response  
    handleFailure : function(o){ 
        o.argument = null;
        utilities.hideWaitPanel();
    },
        
    // Shows loading panel message
    showWaitPanel : function() {
        waitPanel = document.getElementById("wait_c");
        if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
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
        }       
    },

    /**
    * hides the loading panel
    */
    hideWaitPanel: function () {
        loadingPanel.hide();
    }
    
 }
    


// Cancel the selected funds and close the detail panel
function doCancel() {
    hidePanelDetail();
    hidePanelPreview();
    hidePanelConfirmation();
    document.getElementById('previewPanel').innerHTML = "";
}



// Hides the detail panel
function hidePanelDetail() {
    if(oPanelDetail != null) {
        oPanelDetail.hide();
    }
}

//Hides the preview panel
function hidePanelConfirmation() {
    if(oPanelPreview != null) {
    	oPanelPreview.hide();
    }
}

// Hides the preview panel
function hidePanelPreview() {
    if(oPanelPreview != null) {
        oPanelPreview.hide();
    }
}
    
// Displays preview panel to user.
function showPanelPreview() {
    if(oPanelPreview == null) {
        oPanelPreview = new YAHOO.widget.Panel("previewPanel",  
{ width:"800px", height:"650px", draggable:false, close:false, zindex:4, visible:true, modal:true});       
    }
    oPanelPreview.render();
    oPanelPreview.center();
    oPanelPreview.show();
}


function scrollBarBehaviour() {
	if($(".report_height").height() > 37 )
	{
		 $(".dymanicSCroll").css("max-height", "37px");
	}
}

function showPanelConfirmPreview() {
	hidePanelConfirmation();
	if(oPanelPreview == null) {
        oPanelPreview = new YAHOO.widget.Panel("previewPanel",  
        		{ width:"800px", height:"650px", draggable:false, close:false, zindex:4, visible:true, modal:true});       
    }
    
    oPanelPreview.render();
    oPanelPreview.center();
    oPanelPreview.show();
}

function populateStates(countryCode, selectedState) {

	if ($('.bob_results').val() == 'contract') {
		ajax_getJSON(
				"/do/bob/contract/planReview/Print/?task=getStateDropdownValues&isPageRegularlyNavigated=true",
				{
					countryCode : countryCode,
					selectedState : selectedState
				}, getStateDropdown_callbackMethod);

	} else {
		ajax_getJSON(
				"/do/bob/planReview/Print/?task=getStateDropdownValues&isPageRegularlyNavigated=true",
				{
					countryCode : countryCode,
					selectedState : selectedState
				}, getStateDropdown_callbackMethod);
	}
	
	$('#recipientZipCode').val("");
}

//Callback method for get State Dropdown values
getStateDropdown_callbackMethod = function(parsedData) {

	var stateDropDown = "<option value=''> </option>";

	if (parsedData.countryCode == undefined) {
		return;
	}

	for ( var i = 0; i < parsedData.countryCode.length; i++) {

		stateDropDown += "<option value='"
				+ parsedData.countryCode[i].stateCode + "' " + parsedData.countryCode[i].selected +" >"
				+ parsedData.countryCode[i].stateCode + "</option>";
	}


	$("select#stateCode").html(stateDropDown);

}
var jsonObjparam1 = "";
// Displays the preview to the user.
function displayPreview() {
	var jsonObjparam="";
	

	if ($('.bob_results').val() != 'contract'
			&& $(".selectContracts:checked").length == 0) {
		document.forms['planReviewPrintForm'].action = "/do/bob/planReview/Print/?task=errorMsg";
		navigate("planReviewPrintForm");
		return;
	
	}
 	else if ($('.bob_results').val() == 'contract') {

		var rowIndex = 0;
		var copies = $("#select_name" + rowIndex).val();
		rowIndex = rowIndex + 1;
		var contractId = $("#contractnum" + rowIndex).text();

		var jsonItem = "";
		jsonItem += "{\"ContractId\":\"" + contractId.trim()
				+ "\",\"Copies\":\"" + copies + "\"}";
		jsonObjparam += jsonItem + ",";

	} else {

		$('.selectContracts:checked').each(
				function() {
					var rowIndex = 0;
					rowIndex = parseInt((this).id);
					var copies = $("#select_name" + rowIndex).val();
					rowIndex = rowIndex + 1;
					var contractId = $("#contractnum" + rowIndex).text();

					var jsonItem = "";
					jsonItem += "{\"ContractId\":\"" + contractId.trim()
							+ "\",\"Copies\":\"" + copies + "\"}";
					jsonObjparam += jsonItem + ",";
				});
	}
	
	$(".message_error").html("");
	$(".message").removeClass("message_error");
	
	jsonObjparam1 = jsonObjparam.slice(0,-1);
	utilities.showWaitPanel();
	if ($('.bob_results').val() == 'contract') {
		utilities.doAsyncRequest("/do/bob/contract/planReview/Print/?task=validateUserPrintRequestLimit&isPageRegularlyNavigated=true",
				callback_displayShippingInfoValidate, jsonObjparam1);
	} else {
		utilities.doAsyncRequest("/do/bob/planReview/Print/?task=validateUserPrintRequestLimit&isPageRegularlyNavigated=true",
				callback_displayShippingInfoValidate, jsonObjparam1);
	}
	
} 

var callback_displayShippingInfoValidate = {
	cache : false,
    success : function(o) {
    	
		if(o.responseText.match("planReviewReportsUnavailablePage")) {
			top.location.reload(true);
			return;
		}
		
		if(o.responseText.match("public_home")) {
			top.location.reload(true);
			return;
		}
    	
    	if( o.responseText == ""){
			if ($('.bob_results').val() == 'contract') {
				utilities.doAsyncRequest(actionURL["contractPlanReviewPrint"].Shipping,
						callback_displayShippingInfo, jsonObjparam1);
			} else {
				utilities.doAsyncRequest(actionURL["bobPlanReviewPrint"].Shipping,
						callback_displayShippingInfo, jsonObjparam1);
			}
    	} else {
    		if ($('.bob_results').val() == 'contract') {
    			document.forms['planReviewPrintForm'].action = "/do/bob/contract/planReview/Print/?task=displayPrintErrors";
    			
    		} else {
    			document.forms['planReviewPrintForm'].action = "/do/bob/planReview/Print/?task=displayPrintErrors";
    		}
			
			document.forms['planReviewPrintForm'].elements['contractIdList'].value = o.responseText ;
			document.forms['planReviewPrintForm'].elements['pageRegularlyNavigated'].value = true;
			document.forms['planReviewPrintForm'].submit();
			
    	}
    },
    failure : utilities.handleFailure,
    argument : callbackArgument
};
		

// Call back handler for get assetclass funds listing when user hits on ranking order.
var callback_displayShippingInfo =    {
    cache : false,
    success : function(o) {


		if(o.responseText.match("planReviewReportsUnavailablePage")) {
			top.location.reload(true);
			return;
		}
		
		if(o.responseText.match("public_home")) {
			top.location.reload(true);
			return;
		}
		
    	
        document.getElementById("previewPanel").innerHTML = o.responseText;
        utilities.hideWaitPanel();
        showPanelPreview();
    	
    },

    failure : utilities.handleFailure,
    argument : callbackArgument
    };

//Displays the preview to the user.
function displayConfirm() {
	
	var defaultStatus = $('#defaultAddressCheckBox:checked').val();
	if(defaultStatus == undefined) {
		defaultStatus = 'off';
	}
	
	var makeDefaultAddress = $('#makeDefaultAddressCheckBox:checked').val();
	if(makeDefaultAddress == undefined) {
		makeDefaultAddress = 'off';
	}
	
	var country=$('#country option:selected').val(); 
	var state=$('#stateCode option:selected').val(); 
	
	var jsonItem = "";

	    jsonItem+="{\"defaultAddressCheckBoxInd\":\""+defaultStatus+"\",";
	    
	    var index = 0;
	    $('.shppingAddress').each(function() {
	    	var data = $(this).val();
	    	
	    	// replace " to \" and \ to \\
		    data = data.toString().replace(/\\/g,'\\\\').replace(/"/g,'\\"');
	    	
	    	if(index == 0) {	jsonItem += "\"recipientName\":\""+data+"\",";			}
	    	if(index == 1) {	jsonItem += "\"recipientPhoneNumber\":\""+data+"\",";	}
	    	if(index == 2) {	jsonItem += "\"recipientEmail\":\""+data+"\",";			}
	    	if(index == 3) {	jsonItem += "\"recipientCompany\":\""+data+"\",";		}
	    	if(index == 4) {	jsonItem += "\"addressLine1\":\""+data+"\",";			}
	    	if(index == 5) {	jsonItem += "\"addressLine2\":\""+data+"\",";			}
	    	if(index == 6) {	jsonItem += "\"recipientCity\":\""+data+"\",";			}
	    	if(index == 7) {	jsonItem += "\"recipientState\":\""+state+"\",";		}
	    	if(index == 8) {	jsonItem += "\"recipientZipCode\":\""+data+"\",";		}
	    	if(index == 9) {	jsonItem += "\"recipientCountry\":\""+country+"\",";	}
	    	if(index == 10){	jsonItem += "\"makeDefaultAddressCheckboxInd\":\""+makeDefaultAddress+"\"}";}
	    	
	    	index++;
	    });
	    
	    jsonItem = encodeURIComponent(jsonItem);
	    
	utilities.showWaitPanel();

	    if ($('.bob_results').val() == 'contract') {
		utilities.doAsyncRequest(
				actionURL["contractPlanReviewPrint"].PrintConfirm,
				callback_displayConfirmationInfo, jsonItem);
	} else {
		utilities.doAsyncRequest(actionURL["bobPlanReviewPrint"].PrintConfirm,
				callback_displayConfirmationInfo, jsonItem);
	}
}
  

	// Call back handler for get assetclass funds listing when user hits on ranking order.
	var callback_displayConfirmationInfo =    {
	    cache : false,
	    success : function(o) {
		
			if(o.responseText.match("CSRF Error")) {
				window.location.href = window.location+"?task=csrfError&isPageRegularlyNavigated=true";
				return;
			}
		
			if(o.responseText.match("planReviewReportsUnavailablePage")) {
				top.location.reload(true);
				return;
			}
			
			if(o.responseText.match("public_home")) {
				top.location.reload(true);
				return;
			}
			
	    	document.getElementById('previewPanel').innerHTML = "";
	    	hidePanelPreview();
	        document.getElementById("previewPanel").innerHTML = o.responseText;
	        utilities.hideWaitPanel();
	        showPanelConfirmPreview();
			//scrollBarBehaviour();
	    },
	    failure : utilities.handleFailure,
	    argument : callbackArgument
	    };


function defaultAddressCheckBoxClicked(){
	var status = $('#defaultAddressCheckBox:checked').val();
		if (status == "on") {
			
			if ($('.bob_results').val() == 'contract') {
				ajax_getJSON(
						"/do/bob/contract/planReview/Print/?task=getBrokerDefaultAddress&isPageRegularlyNavigated=true",
						{}, brokerDefaultAddress_callbackMethod);

			} else {
				ajax_getJSON(
						"/do/bob/planReview/Print/?task=getBrokerDefaultAddress&isPageRegularlyNavigated=true",
						{}, brokerDefaultAddress_callbackMethod);
			}
		}
 		else {
	
			$('#recipientName').val("");
			$('#recipientEmail').val("");
			$('#recipientCompany').val("");
			$('#recipientPhoneNumber').val("");
			$('#addressLine1').val("");
			$('#addressLine2').val("");
			$('#recipientCity').val("");
			$('#recipientZipCode').val("");
			$('#country').val('USA');
			
			populateStates('USA', '');
			//$('#stateCode').val("");
 		}	
}


//Callback method for selected Pre defined images for Cover image
brokerDefaultAddress_callbackMethod = function(parsedData) {
	
	populateStates(parsedData.country, parsedData.state);
	
	$('#recipientName').val(parsedData.recipientName);
	$('#recipientEmail').val(parsedData.recipientEmail);
	$('#recipientPhoneNumber').val(parsedData.recipientPhoneNumber);
	$('#recipientCompany').val(parsedData.recipientCompany);
	$('#addressLine1').val(parsedData.addressLine1);
	$('#addressLine2').val(parsedData.addressLine2);
	$('#recipientCity').val(parsedData.recipientCity);
	$('#recipientZipCode').val(parsedData.recipientZipCode);
	
	$('#country').val(parsedData.country);
	
	
	//$('#stateCode').val(parsedData.state);
	//$('#stateCode').val(parsedData.state); // setting again the value to reflect
	
	$('#makeDefaultAddressCheckBox:checked').removeAttr('checked');

}

function makeDefault(){
	var status = $('#makeDefaultAddressSelected').val();
	if(status=="true"){
		$('#checkBoxHidden').val(true);
	}else{
		$('#checkBoxHidden').val(false);
	}
}

function doReturnToPrintPage() {
	doCancel();
	
	if ($('.bob_results').val() == 'contract') {
		ajax_getJSON(
				"/do/bob/contract/planReview/Print/?task=clearPrintReports&isPageRegularlyNavigated=true",
				{}, function(parsedData) {});

	} else {
		
		ajax_getJSON(
				"/do/bob/planReview/Print/?task=clearPrintReports&isPageRegularlyNavigated=true",
				{}, function(parsedData) {});
	}
	
	
	// reset to  defaults on page load.
	 $("#select_all").prop('checked', false);
	
	 $(".selectContracts").each(function(){
		 $(this).prop('checked', false);
	 });
	 
	 $(".numberOfCopiesDD").each(function(){
		 $(this).val('2');
	 });
}
	
$(document).ready(function(){

	$(".downloadContract").on('click',function(){		
			  if(this.checked != $("#Rselect_all").prop('checked')) {
					$("#Rselect_all").prop('checked', false);
					$("#downloadAllContract").val(false);
			  }
			   var index=this.id;
			   $("#downloadContract"+index).val(this.checked);
			   $("#downloadContract").val(this.checked);
		});	
	
	$(".downloadSumContract").on('click',function(){
			  if(this.checked != $("#Eselect_all").prop('checked')) {
					$("#Eselect_all").prop('checked', false);
					$("#downloadAllSumContract").val(false);
			  }
			   var index=this.id;
			   $("#downloadSumContract"+index).val(this.checked);
			   $("#downloadSumContract").val(this.checked);
		});
	
	if($(".message.message_error").css("display") == "block") {
		$(".selectContracts").prop( "checked", false );
	}
});
