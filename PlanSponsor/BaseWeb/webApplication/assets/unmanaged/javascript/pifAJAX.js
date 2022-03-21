var actionURL = {
	"PIF" : {
		"Base" : "/do/contract/pic/plansubmission/", "Edit": "/do/contract/pic/edit/", "Delete" : "/do/contract/pic/delete/"
	}
}
var submissionId="";
var deleteContractNumber ="";
var deleteSubmissionId ="";
var deleteLastUpdatedDate ="";
var deleteUserName ="";
var flag = "";

var utilities = {
	
	/**
	 * Populates the error mesages to content to the "messagesBox" <div> tag
	 */
	displayMessages : function(o) {
		var responseText = o.responseText;

		if(responseText == ''){
			if(flag == '1'){
				document.getElementById('submissionId').value = submissionId; 
				document.pifDataForm.action= actionURL["PIF"].Edit;
				document.pifDataForm.submit();
			} else {
				document.getElementById('deleteContractNumber').value = deleteContractNumber;
				document.getElementById('deleteSubmissionId').value = deleteSubmissionId;
				document.getElementById('deleteLastUpdatedDate').value = deleteLastUpdatedDate;
				document.getElementById('deleteUserName').value = deleteUserName;
				document.deletePIFDataForm.action=actionURL["PIF"].Delete;
				document.deletePIFDataForm.submit();
			}	
		} else {
			document.getElementById("messagesBox").innerHTML = responseText;
		}	
	},

	/**
     * Clears the content of the "messagesBox" <div> tag
     */
    clearMessages : function() {
	    if( document.getElementById("messagesBox") != null &&  document.getElementById("messagesBox") != "")
		{	
			 document.getElementById("messagesBox").innerHTML = "";
		}		
    }
    
}

/**
 * Generic function to handle a failure in the server response
 */
var handleFailure = function(o){ 
    o.argument = null;
    alert("some error happened while validating");
}

/**
 * Makes an AJAX call.
 * This function will be triggered when the user changes the values in the drop down list
 */
function doValidateLock(subId) {
		utilities.clearMessages();
		flag = "1";
		submissionId = subId;
		var actionPath = actionURL["PIF"].Base+"?task=validateLock&submissionId="+submissionId;
		// Make a request
		var request = YAHOO.util.Connect.asyncRequest('GET', actionPath, callbackFunction);	
}

/**
 * Makes an AJAX call.
 * This function will be triggered when the user changes the values in the drop down list
 */
function doValidateDeleteLock(contId,subId,lstUpdDate,delUserName) {
		utilities.clearMessages();
		flag = "0";
		deleteContractNumber = contId;
		deleteSubmissionId = subId;
		deleteLastUpdatedDate = lstUpdDate;
		deleteUserName = delUserName;

		var actionPath = actionURL["PIF"].Base+"?task=validateLock&submissionId="+deleteSubmissionId;
		// Make a request
		var request = YAHOO.util.Connect.asyncRequest('GET', actionPath, callbackFunction);	
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


var callbackFunction = { 

	    success:function(o){
	        // parse the response to JSON
	        parsedObject = parseResponseToJSON(o);  
	        if (parsedObject.errors != undefined){ 
			    utilities.clearMessages(parsedObject);
	        } else if (parsedObject == "parseError")  {
	        	utilities.displayMessages(o);
	        } 
	    }, 
	    failure: handleFailure
};