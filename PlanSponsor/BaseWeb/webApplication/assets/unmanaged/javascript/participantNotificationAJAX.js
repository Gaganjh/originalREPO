var actionURL = {
	"IPSR" : {
		"Base" : "/do/investment/viewParticipantNotification/", "Results" : "/do/investment/viewIPSReviewResults/" 
	},
	"IPSRBDW" : {
		"Base" : "/do/bob/investment/viewParticipantNotification/", "Results" : "/do/bob/investment/viewIPSReviewResults/" 
	}
}
var utilities = {
	
	
	/**
     * Displays the errors/information/warnings which is the parsed Object in the 'messages' <div> tag
     */
    displayMessages : function(parsedObject) { 
        document.getElementById("messages").innerHTML = parsedObject.errors[0].messages.unescapeHTML();
    },

	/**
	 * Populates the additional parameter content to the "additionalParamSection" <div> tag
	 */
	showAdditionalParameters : function(o) {
		document.getElementById("additionalParamSection").innerHTML = o.responseText;
		showSection('participantNotificationSection');
		showDialog('additionalParamSection');
		utilities.hideWaitPanel();
	},
	/**
	 * hides the waiting panel
	 */
	hideWaitPanel : function() {
		waitWindow = document.getElementById('ips_body');
		if (waitWindow) {
			waitWindow.style.cursor = "auto";
		}
},

	/**
     * Clears the content of the "messages" <div> tag
     */
    clearMessages : function() {
        document.getElementById("messages").innerHTML = "";
    },
	
	/**
	 * Loads the waiting panel
	 */
	showWaitPanel : function() {
		waitWindow = document.getElementById('ips_body');
		if (waitWindow) {
			waitWindow.style.cursor = "wait";
		}
	}
}

/**
 * This array object is used to pass as an argument to the call-back function
 */
var callbackArgument = new Array();

/**
 * Function to set the call-back argument
 */
function setCallbackArgument(argument, argument2) {
    // remove the array elements. 
    // Note: don't declare a new Array object, the call-back function will not take the new Object
    callbackArgument.splice(0, callbackArgument.length);
    callbackArgument[0] = argument;
    callbackArgument[1] = argument2;	
}

/**
 * 
 * This function will be triggered when the user selects a report in the dropdownlist and clicks go
 * 
 * Gets the funds info for the contract selected
 *
 */
function doViewReports(reviewRequestId, isFromLandingPage) {		
	hideDialog('additionalParamSection');
	document.ipsViewParticiapantNotificationForm.action = actionURL["IPSR"].Base +'?reviewRequestId='+reviewRequestId+'&action=viewParticipantNotificationPDF&isFromLandingPage='+isFromLandingPage, "","width=720,height=480,resizable,toolbar,scrollbars,menubar";	
	document.ipsViewParticiapantNotificationForm.submit();
}

/**
 * 
 * This function will be triggered when the user selects a report in the dropdownlist and clicks go
 * 
 * Gets the funds info for the contract selected
 *
 */
function doViewReportsPs(reviewRequestId, isFromLandingPage, isFromBDW) {	
	if(isFromBDW == 'true'){
		document.ipsViewParticiapantNotificationForm.action = actionURL["IPSRBDW"].Base +'?reviewRequestId='+reviewRequestId+'&action=viewParticipantNotificationPDF&isFromLandingPage='+isFromLandingPage, "","width=720,height=480,resizable,toolbar,scrollbars,menubar";
	}else{
		document.ipsViewParticiapantNotificationForm.action = actionURL["IPSR"].Base +'?reviewRequestId='+reviewRequestId+'&action=viewParticipantNotificationPDF&isFromLandingPage='+isFromLandingPage, "","width=720,height=480,resizable,toolbar,scrollbars,menubar";	
	}
	doValidateOverlayFields(isFromBDW);
}


function doValidateOverlayFields(isFromBDW) {
	var url;
	if(isFromBDW == 'true'){
		url = actionURL["IPSRBDW"].Base+"?action=validateOverlayFields";
	}else{
		url = actionURL["IPSR"].Base+"?action=validateOverlayFields";	
	}
	
	doAsyncRequest(url, callback_validateOverlayFields);
	//var request = YAHOO.util.Connect.asyncRequest('POST', url, callback_openOverLayPage);	
	
}

var callback_validateOverlayFields = { 

	    success:function(o){
	    	
	    	 parsedObject = parseResponseToJSON(o);
	        // parse the response to JSON
	        
	        if (parsedObject.error != undefined){ 
				$("#errorMessagesDiv").html("<ul class=\"redtext\">" + parsedObject.error +"</ul>");
        		$("#errorMessagesDiv").show();
	        	
	        } else if (parsedObject == "parseError")  {
	        	hideDialog('additionalParamSection');
				
	        	document.ipsViewParticiapantNotificationForm.submit();
	        }
	    utilities.hideWaitPanel();
	    }, 
	    failure: handleFailure,
	    argument: callbackArgument  
	}; 

/**
 * This method is used to cancel the i:report PDF generation.
 * @return
 */
function doCancelIreport() {
	hideDialog('additionalParamSection');
	hide('participantNotificationSection');
}

/**
 * call-back object for OpenNewWindowForIReport.
 * 
 * Response Object o:
 *      The response object may have the 
 *          1. the error messages
 *            
 *
 * Success:
 *      Opens a new window and triggers the i:report PDF generation.
 *
 * Failure:
 *      calls the generic failure function "handleFailure:  
 *
 * argument:
 *      This argument is used to identify the filter option selected
 *
 */
var callback_OpenNewWindowForReviewReport = {
	success : function(o) { 
		 utilities.hideWaitPanel();		 
		 parsedObject = parseResponseToJSON(o);		 
		 if (parsedObject.errors != undefined){          
	            utilities.displayMessages(parsedObject);
	    } else {	      
	    	window.location = actionURL["IPSR"].Base +'?reviewRequestId='+o.argument[0]+'&action=viewParticipantNotificationPDF&isFromLandingPage='+o.argument[1], "","width=720,height=480,resizable,toolbar,scrollbars,menubar";
	    }
	}, 

	failure: handleFailure,
	argument: callbackArgument  
};

/**
 * Shows the modal dialog, which is passed as a parameter
 * 
 * @param id of the modal dialog which is to be shown
 * @param except - id of the drop-down which should not be disabled
 */
function showDialog(id) {
	hideAllSelectBox();
	documentHeight = document.getElementById('page_wrapper_footer').offsetTop;
	panel = document.getElementById('modalGlassPanel');
	panel.style.height = documentHeight + "px";
	if (window.innerWidth) { // NS4, NS6 and Opera
		var oW = window; 
		iWW = oW.innerWidth; 
		iWH = oW.innerHeight; 
		iWW = (iWW - 17);
	} else if (document.documentElement && document.documentElement.clientWidth) { // IE6 in standards compliant mode
	    var oDE = document.documentElement; iWW = oDE.clientWidth; iWH = oDE.clientHeight;  
	} else if (document.body) { // IE4+
	    var oDB = document.body; 
		iWW = oDB.clientWidth; 
		iWH = oDB.clientHeight; 
		iWW = (iWW - 1);			
	}
	
	document.getElementById('modalGlassPanel').style.width = (iWW)+"px";
	document.getElementById('modalGlassPanel').style.height = (iWH)+"px";
	show('modalGlassPanel');
	show(id);
}

/**
     * Hides all the drop-downs in the page. 
     * 
     * @param except - ignores this drop-down
     */
    function hideAllSelectBox() {
        var x = document.getElementsByTagName("select");
        for (i = 0; i < x.length; i++) {
            x[i].style.visibility = "hidden";
        }
    }

/**
     * Show a section
     * 
     * @param id - HTML element
     */
    function show(id) {
        section = document.getElementById(id);
        if (section) {            
			if (id=="modalGlassPanel") {
				if (section.style) {
					section.style.height=1225;
					section.style.display='';
				}
			} else {
				if (section.style) {
					section.style.height=380;
					section.style.display='';
				}
			}
        } else {
            //alert ("Error: " + id + " doesn't exist");
        }
    }
	
	/**
	 * Show a section
	 * 
	 * @param id - HTML element
	 */
	function showSection(id) {
		section = document.getElementById(id);
		if (section) {            
			if (section.style) {
				section.style.display='';
			}
		}
	}

/**
 * Hides the dialog, which is passed as a parameter
 * 
 * @param id of the modal dialog which is to be hidden
 */
function hideDialog(id) {
	showAllSelectBox();
	hide(id);
	hide('modalGlassPanel');
}

/**
 * Shows all the drop-downs
 */
function showAllSelectBox() {
    var x = document.getElementsByTagName("select");
    for (i = 0; i < x.length; i++) {
        x[i].style.visibility = "visible";
    }
}
	
/**
 * Hide a section
 * 
 * @param id - HTML element
 */
function hide(id) {
    section = document.getElementById(id);
    if (section) {            
        if (section.style) {
            section.style.display='none';
        }
    }
}

/**
 * Generic function to handle a failure in the server response
 */
var handleFailure = function(o){ 
    o.argument = null;
    utilities.hideWaitPanel();
    //alert("some error happened in the loading jsp");
}

//For the changes made to reports and download drop down list
//YAHOO.util.Event.addListener("outputSelect", "click", doOutputSelect);

/**
 * Makes an AJAX call.
 * This function will be triggered when the user changes the values in the drop down list
 */
function doOutputSelect(reviewRequestId, isFromLandingPage, isFromBDW) {
		var url;
		if(isFromBDW == 'true'){
			url = actionURL["IPSRBDW"].Base+"?action=change&reviewRequestId="+reviewRequestId+"&isFromLandingPage="+isFromLandingPage;
		}else{
			url = actionURL["IPSR"].Base+"?action=change&reviewRequestId="+reviewRequestId+"&isFromLandingPage="+isFromLandingPage;
		}
		document.ipsViewParticiapantNotificationForm.reset();
		doAsyncRequest(url, callback_openOverLayPage);
		//var request = YAHOO.util.Connect.asyncRequest('POST', url, callback_openOverLayPage);	
		
}

/**
 * Generic function to make an AJAX request.
 *
 * @param actionPath        - action url
 * @param callbackFunction  - name of the call-back object to handle the server response
 */
function doAsyncRequest(actionPath, callbackFunction) {
    
    utilities.showWaitPanel(); 	

    // This statement would send the form attributes to the server.
    // If this statement is not present, the form values will not be updated
    YAHOO.util.Connect.setForm(document.ipsViewParticiapantNotificationForm);

    // Make a request
    var request = YAHOO.util.Connect.asyncRequest('POST', actionPath, callbackFunction);	
}

/**
 * Parses the JSON object in the response to a JSON String
 *
 * @param	- connection object
 * @return 	- the native JavaScript representation of the JSON string
 */
function parseResponseToJSON(o) {
	try { 
			parsedObject = YAHOO.lang.JSON.parse(o.responseText); 
			if (parsedObject.sessionExpired) {
				//alert("session expired.... redirecting to login page");
				top.location.reload(true);
			}
			return parsedObject;
		} catch (x) { 
			//alert("JSON Parse failed!" + x	); 
			//alert (o.responseText);
			return "parseError"; 
		} 
}


var callback_openOverLayPage = { 

	    success:function(o){
	        // parse the response to JSON
	        parsedObject = parseResponseToJSON(o);  
	        if (parsedObject.errors != undefined){ 
			    utilities.displayMessages(parsedObject);
	        } else if (parsedObject == "parseError")  {
	        	utilities.showAdditionalParameters(o);
	        } 
	        utilities.hideWaitPanel();
	    }, 
	    failure: handleFailure,
	    argument: callbackArgument  
	}; 