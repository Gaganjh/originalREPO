/**
 * @author Charles Chan
 * $Id: deferralReport.js,v 1.12 2011/10/22 18:53:39 Siby_Thomas Exp $
 */

var filterMap = new Array();

/**
 * Sets the filter using the given HTML select object.
 */
function setFilterFromSelect(theSelect)
{
  var newValue = theSelect.options[theSelect.selectedIndex].value;
  filterMap[theSelect.name] = newValue;
}

function setFilterFromInput(theInput)
{
  filterMap[theInput.name] = theInput.value;
}

/**
 * Filters the report data again.
 */
function doFilter()
{
  var reportURL = new URL();
  reportURL.setParameter("task", "filter");
  for (var key in filterMap) {
    reportURL.setParameter(key, filterMap[key]);
  }
  location.href = reportURL.encodeURL();
}

/**
 * Opens up a new window and perform the same request again (with printFriendly
 * parameter.
 */
function doPrint() {

	var reportURL = new URL();
 	var unprocIndOnly;
 	
 	if (document.forms['participantDeferralChangesReportForm']) {
		if (document.forms['participantDeferralChangesReportForm'].elements['errorsOnPage']	){	
			var errors = document.forms['participantDeferralChangesReportForm'].elements['errorsOnPage'].value;
		}	
		if (errors == "false"){
			var baseToDate = document.forms['participantDeferralChangesReportForm'].elements['baseToDate'].value;
			var baseFromDate = document.forms['participantDeferralChangesReportForm'].elements['baseFromDate'].value;
			var baseUnprocessedIndOnly = document.forms['participantDeferralChangesReportForm'].elements['baseUnprocessedIndOnly'].value;
		
			if (baseUnprocessedIndOnly == "true"){
				document.forms['participantDeferralChangesReportForm'].elements['unprocessedIndOnly'].value = "on";
				document.forms['participantDeferralChangesReportForm'].elements['unprocessedIndOnly'].checked = true;
				unprocIndOnly = "on";
			} else {
				document.forms['participantDeferralChangesReportForm'].elements['unprocessedIndOnly'].value = "off";
				document.forms['participantDeferralChangesReportForm'].elements['unprocessedIndOnly'].checked = false;
				unprocIndOnly = "off";
			}
			document.forms['participantDeferralChangesReportForm'].elements['toDate'].value = baseToDate;
			document.forms['participantDeferralChangesReportForm'].elements['fromDate'].value = baseFromDate;
		
  			reportURL.setParameter("task", "print");
 	 		reportURL.setParameter("printFriendly", "true");
  			reportURL.setParameter("unprocessedIndOnly", unprocIndOnly);
  			reportURL.setParameter("toDate", baseToDate);
  			reportURL.setParameter("fromDate", baseFromDate);
  			reportURL.setParameter("isRefresh", false);
  		
  			window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,");
  		}	
	}
}

/**
 * For How To Read Report/Use Form only.
 * Opens up a new window and perform the same request again with parameter
 * printFriendly, contentKey and ind. 
 */
function doPrintHowTo (contentKey, ind)
{
  var reportURL = new URL();
  reportURL.setParameter("task", "print");
  reportURL.setParameter("printFriendly", "true");
  reportURL.setParameter("contentKey", contentKey);
  reportURL.setParameter("ind", ind);	  
  window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,");
}