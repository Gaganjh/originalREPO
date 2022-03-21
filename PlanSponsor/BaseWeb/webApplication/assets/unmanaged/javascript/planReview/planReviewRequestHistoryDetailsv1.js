$(document).ready(function(){
	
	$("#download_button").prop('disabled', 'disabled').addClass("disabled-grey-btn");
	
	$("#Rselect_all").on('click',function(){
		
		 $("#downloadPlanReviewReportInd").val(this.checked);
		if($(".download:checked").length>0){ 
			$("#download_button").prop('disabled', '').removeClass("disabled-grey-btn");
			$("#download_button").removeAttr( "disabled" );
		}
		else
			$("#download_button").prop('disabled', 'disabled').addClass("disabled-grey-btn");
	});
	
	
	$("#Eselect_all").on('click',function(){
		
		 $("#downloadExcecutiveSummaryInd").val(this.checked);
		if($(".download:checked").length>0){ 
			$("#download_button").prop('disabled', '').removeClass("disabled-grey-btn");
			$("#download_button").removeAttr( "disabled" );
		}
		else
			$("#download_button").prop('disabled', 'disabled').addClass("disabled-grey-btn");
	});
	
});

$("#doPrintRequestPage")
		.on('click',
				function() {
					var index = 0;

					if ($('.bob_results').val() == 'bob') {
						document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task=printRequestPage";
					} else {
						document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/contract/planReview/HistoryDetails/?task=printRequestPage";
					}

					navigate("planReviewHistoryDetailsReportForm");
				});

function doBackToMainHistory() {

	if ($('.bob_results').val() == 'bob') {
		document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task=backToMainHistory";
	} else {
		document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/contract/planReview/HistoryDetails/?task=backToMainHistory";
	}

	navigate("planReviewHistoryDetailsReportForm");
}

$("#doPlanRequest")
		.on('click',
				function() {

					if ($('.bob_results').val() == 'bob') {
						document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task=planReviewRequest";
					} else {
						document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/contract/planReview/HistoryDetails/?task=planReviewRequest";
					}

					navigate("planReviewHistoryDetailsReportForm");
				});

//
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
	doAsyncRequest : function(actionPath, callbackFunction, jsonObj) {
		YAHOO.util.Connect.setForm(document.getElementById('planReviewHistoryDetailsReportForm'));
		//YAHOO.util.Connect.setForm(document.PlanReviewPrintForm,true,true);
		// Make a request
		var request = YAHOO.util.Connect.asyncRequest('POST', actionPath
				+ "&jsonObj=" + jsonObj, callbackFunction);
		//var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);
	},

	// Set call back arguments  
	setCallbackArgument : function(argument) {
		// remove the array elements. 
		// Note: don't declare a new Array object, the call-back function will not take the new Object
		callbackArgument.splice(0, callbackArgument.length);
		callbackArgument[0] = argument;
	},

	// Generic function to handle a failure in the server response  
	handleFailure : function(o) {
		o.argument = null;
		utilities.hideWaitPanel();
	},

	// Shows loading panel message
	showWaitPanel : function() {
		waitPanel = document.getElementById("wait_c");
		if (waitPanel == undefined || waitPanel.style.visibility != "visible") {
			loadingPanel = new YAHOO.widget.Panel("wait", {
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
			loadingPanel
					.setBody("<span style='padding-left:20px;float:right;padding-right:30px;padding-top:12px;'>One moment please...</span><img style='padding-top:5px;padding-left:5px;' src='/assets/unmanaged/images/ajax-wait-indicator.gif'/>");
			loadingPanel.render(document.body);
			loadingPanel.show();
		}
	},

	/**
	 * hides the loading panel
	 */
	hideWaitPanel : function() {
		loadingPanel.hide();
	}

}

////////////////////////
var actionURL = {
	"planHistory" : {
		"ShippingDetails" : "/do/bob/planReview/HistoryDetails/?task=shippingDetails&isPageRegularlyNavigated=true",
		"ShippingDetailsContractLevel" : "/do/bob/contract/planReview/HistoryDetails/?task=shippingDetails&isPageRegularlyNavigated=true",
		"showViewDisableReport" : "/do/bob/planReview/HistoryDetails/?task=showViewDisableReport&isPageRegularlyNavigated=true&requestHistoryDetailsReport=true",
		"showViewDisableReportContractLevel" : "/do/bob/contract/planReview/HistoryDetails/?task=showViewDisableReport&isPageRegularlyNavigated=true&requestHistoryDetailsReport=true",
	}
}

//////////////////////
var oPanelDetail;
var oPanelPreview;
var oPanelIndex;
var loadingPanel;

//This array object is used to pass as an argument to the call-back function
var callbackArgument = new Array();

function doShippingDetails(printActivityId) {

	var jsonObjparam = "{ \"printActivityId\" : \"" + printActivityId.trim()
			+ "\" }"

	utilities.showWaitPanel();
	
	if ($('.bob_results').val() == 'bob') {
		utilities.doAsyncRequest(actionURL["planHistory"].ShippingDetails,
				callback_displayShippingDetailsInfo, jsonObjparam);
	} else {
		utilities.doAsyncRequest(actionURL["planHistory"].ShippingDetailsContractLevel,
				callback_displayShippingDetailsInfo, jsonObjparam);
	}
	
	
}

var callback_displayShippingDetailsInfo = {
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
		
		if (o.responseText == '') {
			return;
		}

		document.getElementById("shippingPanel").innerHTML = o.responseText;
		showPanelShippingDetails();
		utilities.hideWaitPanel();
	},
	failure : utilities.handleFailure,
	argument : callbackArgument
};

YAHOO.util.Event.addListener("shippingDetailsButton", "click",
		doShippingDetails);
YAHOO.util.Event.addListener("viewDisableButton", "click", showViewDisableReportOverlay);

/**
 * An associated JavaScript array object to store the action URLs.
 */
function showPanelShippingDetails() {
	hidePanelPreview();

	if (oPanelPreview == null) {
		oPanelPreview = new YAHOO.widget.Panel("shippingPanel", {
			fixedcenter : true,
			width : "500px",
			height : "400px",
			draggable : false,
			close : false,
			fixedcenter : true,
			zindex : 4,
			visible : true,
			modal : true
		});
	}

	oPanelPreview.render();
	oPanelPreview.show();
}

//////////////////////////////View disable overview --Start
function showViewDisableReportContractLevelOverlay() {
	var jsonObjparam = '';
	utilities.showWaitPanel();
	utilities.doAsyncRequest(actionURL["planHistory"].showViewDisableReportContractLevel,
			callback_displayViewDisableReportOverlay, jsonObjparam);
}

function showViewDisableReportOverlay() {

	var jsonObjparam = '';
	utilities.showWaitPanel();
	utilities.doAsyncRequest(actionURL["planHistory"].showViewDisableReport,
			callback_displayViewDisableReportOverlay, jsonObjparam);
}

var callback_displayViewDisableReportOverlay = {
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
		
		document.getElementById("viewDisableReasonPanel").innerHTML = o.responseText;
		showPanelViewDisableReason();
		utilities.hideWaitPanel();
	},
	failure : utilities.handleFailure,
	argument : callbackArgument
};


function showPanelViewDisableReason() {
	hidePanelDetail();
	
	if (oPanelDetail == null) {
		oPanelDetail = new YAHOO.widget.Panel("viewDisableReasonPanel", {
			fixedcenter : true,
			width : "500px",
			height : "250px",
			draggable : false,
			close : false,
			fixedcenter : true,
			zindex : 4,
			visible : true,
			modal : true
		});
	}

	oPanelDetail.render();
	oPanelDetail.show();
}


function deletePlanReviewReport() {
	document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/planReview/HistoryDetails/?task=deletePlanReviewReport";
	navigate('planReviewHistoryDetailsReportForm');
}
function deletePlanReviewReportContractLevel() {
	document.forms['planReviewHistoryDetailsReportForm'].action = "/do/bob/contract/planReview/HistoryDetails/?task=deletePlanReviewReport";
	navigate('planReviewHistoryDetailsReportForm');
}

// Cancel the selected funds and close the detail panel
function doClose() {
	hidePanelDetail();
	hidePanelPreview();
	document.getElementById('viewDisableReasonPanel').innerHTML = "";
	document.getElementById('shippingPanel').innerHTML = "";
}

// Hides the detail panel
function hidePanelDetail() {
	if (oPanelDetail != null) {
		oPanelDetail.hide();
	}
}

// Hides the preview panel
function hidePanelPreview() {
	if (oPanelPreview != null) {
		oPanelPreview.hide();
	}
}

//Common Function for AJAX Calls
function ajax_getJSON(actionPath, requstParameters, callbackMethod) {
	$.get(actionPath, requstParameters, function(data) {
		// Call back method
		var parsedData = $.parseJSON(data);
		if (parsedData.sessionExpired != undefined) {
			// session expired.... redirecting to login page
			top.location.reload(true);
		} else {
			callbackMethod(parsedData);
		}
	}, "text");
}

//Common Function to set Navigation parameters
function navigate(form) {
	document.forms[form].elements['pageRegularlyNavigated'].value = true;
	document.forms[form].submit();
}