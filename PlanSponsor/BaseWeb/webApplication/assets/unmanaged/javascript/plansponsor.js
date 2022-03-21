var trackChangesFunction = null;
var avoidSignout = null;
var submitInProgress = null;

function registerTrackChangesFunction(func) {
	trackChangesFunction = func;
}

function discardChanges(warning) {
  var formChanged = false;
	//alert(trackChangesFunction); 
  if (trackChangesFunction != null) {
  	formChanged = trackChangesFunction();
  }
  
  if (formChanged) {
    if (window.confirm(warning)) {
      return true;
    } else {
      return false;
    }
  }
  return true;
}

function doSignOut(message) {
	 //alert ("Signout avoidSignout : " + avoidSignout);    
     if (avoidSignout == null && submitInProgress == null){	

  		if (discardChanges(message)) {
    		parent.location.href='/do/home/Signout/';
    		return true;
  		} else {
    		return false;
  		}
     } else if (avoidSignout != null && submitInProgress == null) {
		//alert ("Calling onExit()");	
		avoidSignout = null;	
		onExit();
     } else if (avoidSignout == null && submitInProgress != null) {
     		window.status = "Transaction already in progress.  Please wait.";
			return false;
     }
     // they shouldn't ever both be true	
}
function doContractHome(message) {
	 //alert ("Signout avoidSignout : " + avoidSignout);    
     if (avoidSignout == null && submitInProgress == null){	

  		if (discardChanges(message)) {
    		parent.location.href='/do/home/homePage/';
    		return true;
  		} else {
    		return false;
  		}
     } else if (avoidSignout != null && submitInProgress == null) {
		//alert ("Calling onExit()");	
		avoidSignout = null;	
		onExit();
     } else if (avoidSignout == null && submitInProgress != null) {
     		window.status = "Transaction already in progress.  Please wait.";
			return false;
     }
     // they shouldn't ever both be true	
}
// a cross browser way to retrieve the actual style of an element.
function getStyle(el,styleProp)
{
	var x = document.getElementById(el);
	if (x.currentStyle)
		var y = x.currentStyle[styleProp];
	else if (window.getComputedStyle)
		var y = document.defaultView.getComputedStyle(x,null).getPropertyValue(styleProp);
	return y;
}

function registerWarningOnChangeToLinks(skipIdList) {

	var hrefs  = document.links;
	if (hrefs != null)
	{
		for (i=0; i<hrefs.length; i++) {

		    if (skipIdList != undefined) {
		        var skipped = false;
		    	for (j = 0; j < skipIdList.length && ! skipped; j++) {
		    	  if (skipIdList[j] == hrefs[i].id) {
		    	    // skip anything in the skip ID list.
		    	    skipped = true;
		    	  }
		    	}
		    	if (skipped) {
		    	  continue;
		    	}
		    } 

			if (hrefs[i].onclick != undefined && 
				(hrefs[i].onclick.toString().indexOf("openWin") != -1 ||
				 hrefs[i].onclick.toString().indexOf("toggle") != -1 ||
				 hrefs[i].onclick.toString().indexOf("popup") != -1 ||
				 hrefs[i].onclick.toString().indexOf("doSignOut") != -1))
		    {
				// don't replace window open or div toggle or popups as they won't loose their changes with those
			}
			else if (hrefs[i].href != undefined && 
			         (hrefs[i].href.indexOf("#") == 0 ||
			          hrefs[i].href.indexOf("openWin") != -1 ||
				      hrefs[i].href.indexOf("toggle") != -1 ||
				      hrefs[i].href.indexOf("popup") != -1 ||
				      hrefs[i].href.indexOf("doSignOut") != -1))
		    {
				// don't replace window open or div toggle or popups as they won't loose their changes with those
			}
			else if (hrefs[i].href != undefined 
						&& hrefs[i].href.indexOf("javascript:doCalendar") !=-1) {
					// don't replace calendar links
			}
			else if(hrefs[i].onclick != undefined)
			{
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
