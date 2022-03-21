var trackChangesFunction = null;
var cancelWarning = null;

function registerTrackChangesFunction(func, warningMsg) {
	trackChangesFunction = func;
	cancelWarning = warningMsg;
}

function discardChanges() {
  var formChanged = false;
	//alert(trackChangesFunction); 
  if (trackChangesFunction != null) {
  	formChanged = trackChangesFunction();
  }
  
  if (formChanged) {
    if (window.confirm(cancelWarning)) {
      return true;
    } else {
      return false;
    }
  }
  return true;
}

function protectLinksFromCancel() {
	var hrefs = document.links;
	if (hrefs != null) {
		for (i = 0; i < hrefs.length; i++) {
			if (hrefs[i].onclick != undefined
					&& (hrefs[i].onclick.toString().indexOf("openWin") != -1
					|| hrefs[i].onclick.toString().indexOf("popup") != -1 
					|| hrefs[i].onclick.toString().indexOf("doSubmit") != -1
					|| hrefs[i].onclick.toString().indexOf("doProtectedSubmit") != -1
					|| hrefs[i].onclick.toString().indexOf("doCancel") != -1
					|| hrefs[i].onclick.toString().indexOf("doNothing") != -1)
			    ) {
				// don't replace window open or popups as they won't loose their changes with those
			} else if (hrefs[i].href != undefined
					&& (hrefs[i].href.indexOf("openWin") != -1
							|| hrefs[i].href.indexOf("popup") != -1 || 
							hrefs[i].href.indexOf("doSubmit") != -1 ||
							hrefs[i].href.indexOf("doProtectedSubmit") != -1  || 
							hrefs[i].href.indexOf("doCancel") != -1 ||
							hrefs[i].href.indexOf("doNothing") != -1		
					) 
					) {
				// don't replace window open or popups as they won't loose their changes with those
			} else if (hrefs[i].onclick != undefined) {
					hrefs[i].onclick = new Function(
							"var result = discardChanges();"
									+ "var childFunction = "
									+ hrefs[i].onclick
									+ "; if(result) result = childFunction(); return result;");
			} else {
					hrefs[i].onclick = new Function(
							"return discardChanges();");
			}
		}
	}
}
