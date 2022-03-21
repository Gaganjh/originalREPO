// Register the event.
if (window.addEventListener) {
	window.addEventListener('load', protectLinksFromCancel, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', protectLinksFromCancel);
}

// Displays the warning message and takes the confirmation.
function discardChangesWarning() {
	if(document.fundEvaluatorForm.dataModified.value == 'true'){
		 if(confirm(document.fundEvaluatorForm.navigateAwayWarning.value)) {
		return true;
	 } else {
		return false;
	 }
 
	return true;
	}
  
}
function protectLinksFromCancel() {

	var hrefs = document.links;
	if (hrefs != null) {
		for (i = 0; i < hrefs.length; i++) {
			if (hrefs[i].onclick != undefined
					&& (hrefs[i].onclick.toString().indexOf("openWin") != -1
					|| hrefs[i].onclick.toString().indexOf("popup") != -1 
					|| hrefs[i].onclick.toString().indexOf("ievaluator") != -1 
					|| hrefs[i].onclick.toString().indexOf("doSubmit") != -1
					|| hrefs[i].onclick.toString().indexOf("doProtectedSubmit") != -1
					|| hrefs[i].onclick.toString().indexOf("cancelPanel") != -1
					|| hrefs[i].onclick.toString().indexOf("doCancel") != -1)
          || $(hrefs[i]).hasClass("ui-multiselect-all") 
          || $(hrefs[i]).hasClass("ui-multiselect-none") 
          || $(hrefs[i]).hasClass("ui-multiselect-close") 
			    ) {
			} else if (hrefs[i].href != undefined
					&& (hrefs[i].href.indexOf("openWin") != -1
							|| hrefs[i].href.indexOf("popup") != -1 || 
							hrefs[i].href.indexOf("displayPreview") != -1 || 
							hrefs[i].href.indexOf("saveAndClosePanel") != -1 || 
							hrefs[i].href.indexOf("doNext") != -1 || 
							hrefs[i].href.indexOf("doPrevious") != -1 || 
							hrefs[i].href.indexOf("doSubmit") != -1 ||
							hrefs[i].href.indexOf("doProtectedSubmit") != -1  || 
							hrefs[i].href.indexOf("showHint") != -1  || 
							hrefs[i].href.indexOf("doGenerateReport") != -1  || 
							hrefs[i].href.indexOf("doCancel") != -1)
					) {
			} else if (hrefs[i].onclick != undefined) {
					hrefs[i].onclick = new Function(
							"var result = discardChangesWarning();"
									+ "var childFunction = "
									+ hrefs[i].onclick
									+ "; if(result) result = childFunction(); return result;");
			} else {
					hrefs[i].onclick = new Function(
							"return discardChangesWarning();");
			}
		}
	}
}
