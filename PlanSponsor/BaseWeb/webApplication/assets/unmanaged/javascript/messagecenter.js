
// submit the form
function doSubmit(frm, action) {
	
    // Check if submit is in progress
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    } // fi
	
    frm.elements['action'].value=action
    frm.submit();
}

function doConfirmAndSubmit(frm, action) {
    // Check if submit is in progress
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    } // fi
	
	// only there is change, do confirm
    if (isFormChanged()) {
        if (!(confirm('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.'))) {
            resetSubmitInProgress();
            return false;
        }
    }

    frm.elements['action'].value=action
    frm.submit();
}


function protectLinksFromCancel() {
		
		var hrefs  = document.links;
		if (hrefs != null)
		{
			for (i=0; i<hrefs.length; i++) { 
				if(
					hrefs[i].onclick != undefined && 
					(hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
				) {
					// don't replace window open or popups as they won't loose their changes with those
				}
				else if(
					hrefs[i].href != undefined && 
					(hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
				) {
					// don't replace window open or popups as they won't loose their changes with those
				}
				else if(
					hrefs[i].onclick != undefined 
							&& ((hrefs[i].onclick.toString().indexOf("toggle") != -1)
									|| (hrefs[i].onclick.toString().indexOf("expand") != -1) 
									|| (hrefs[i].onclick.toString().indexOf("showPreviousValue") != -1)
									||(hrefs[i].onclick.toString().indexOf("handleDateIconClicked") != -1)
									||(hrefs[i].onclick.toString().indexOf("doSubmitAdd('add')") != -1))
				) {
						// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				}
				else if(
					hrefs[i].href != undefined 
							&&  ((hrefs[i].href.indexOf("toggle") != -1)
									|| (hrefs[i].href.indexOf("expand") != -1) 
									|| (hrefs[i].href.indexOf("showPreviousValue") != -1)
									|| (hrefs[i].href.indexOf("showSimilarRecord") != -1)
									|| (hrefs[i].href.indexOf("javascript:doStartingDateCal") !=-1)
									)) {
						// don't replace these links as we will do an automatic save on the server side before invoking the requested function
				}
				else if(hrefs[i].onclick != undefined) {
					if (hrefs[i].onclick.toString().indexOf("task=download") != -1) {
						hrefs[i].onclick = new Function ("var result = discardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
					} else {
						hrefs[i].onclick = new Function ("var result = discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
					}				
				}
				else {
					if (hrefs[i].href.indexOf("task=download") != -1) {
						hrefs[i].onclick = new Function ("return discardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');");
					} else {
						hrefs[i].onclick = new Function ("return discardChanges('The action you have selected will cause your changes to be lost.  Select OK to continue or Cancel to return.');");
					}
				}
				//alert (hrefs[i].onclick.toString());
			}
		}
 }	

function isFormChanged() {
	return changeTracker.hasChanged();
}
registerTrackChangesFunction(isFormChanged);

if (window.addEventListener) {
	window.addEventListener('load', protectLinksFromCancel, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', protectLinksFromCancel);
}
