// //This javascript file is used to scripting common functionality in PlanReviewReport Pages

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