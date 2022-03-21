/**
 * @author Charles Chan
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
  reportURL.setParameter("lastVisited", "true");
  for (var key in filterMap) {
    reportURL.setParameter(key, filterMap[key]);
  }
  location.href = reportURL.encodeURL();
}

/**
 * This function will be called when the user clicks on Print PDF report link.
 */
function doOpenPrintPDF(printParticipant) {
  var reportURL = new URL(document.location.href);
  reportURL.setParameter("actionLabel", "printPDF");
  reportURL.setParameter("printFriendly", "true");
  if (printParticipant) {
	  reportURL.setParameter("printParticipant", "true");
  }
  location.href = reportURL.encodeURL();
}

/**
 * Opens up a new window and perform the same request again (with printFriendly
 * parameter.
 */
function doPrint()
{
  var reportURL = new URL();
  reportURL.setParameter("task", "print");
  reportURL.setParameter("printFriendly", "true");
  window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

/**
 * Opens up a new window and perform the same request again with printFriendly and Participant
 * parametesr.
 */
function doPrintForParticipant()
{
  var reportURL = new URL();
  reportURL.setParameter("task", "print");
  reportURL.setParameter("printFriendly", "true");
  reportURL.setParameter("printParticipant", "true");
  window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

/**
 * Opens up a new window and perform the same request again with printFriendly and Participant
 * parametesr.
 */
function doPrintForConfirm()
{
  var reportURL = new URL();
  reportURL.setParameter("task", "print");
  reportURL.setParameter("printFriendly", "true");
  reportURL.setParameter("action","confirm");
  window.open(reportURL.encodeURL(),"","width=720,height=480,resizable,toolbar,scrollbars,menubar");
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
/*
 * deactivate all the anchors on a page.  used in printfriendly mode
 */

function removeAllLinks() {
	var hrefs  = document.links;
	if (hrefs != null)
	{
		//don't forget, that if you remove the attribute 'href' 
		//then that object isn't even part of the hrefs array anymore. 
		//therefore, when you do i++, you actually skip the next link.
		//therefore, just push all the objects into our own array, 
		//so we can keep track of them.
		var links = new Array();
		for (i=0; i<hrefs.length; i++) { 
			links.push(hrefs[i])
		}
		for ( i = 0 ; i < links.length; i++ ) {
			links[i].removeAttribute('onclick')
			links[i].removeAttribute('href');
			
		}
	}	
}
/**
 * Handler for calendar window setup
 */ 
function handleCalendarSetup(field, event) {
	
   Calendar = new calendar(field);
   Calendar.year_scroll = true;
   Calendar.time_comp = false;
   
   // Modify calendar position to be slightly above and to right of mouse click
   yPosition = event.screenY - 150;
   xPosition = event.screenX + 80;
   
   Calendar.popup();
}

/**
 * Generic handler for date icon clicked
 */ 
 function handleDateIconClicked(evt, fieldId) {

    // Retrieve the field
    var field = document.getElementById(fieldId);

    
    // Pre-Validate date and blank if not valid
    if (typeof field  == "undefined") {
      field.value = '';
    }
    
    // Popup calendar
    handleCalendarSetup(field, evt);
 }
 
 /**
 * Handler for calendar window setup
 */ 
function handleCalendarDateSetup(field, event) {
	
   Calendar = new calendar(field);
   Calendar.year_scroll = false;
   Calendar.time_comp = false;
   
   // Modify calendar position to be slightly above and to right of mouse click
   yPosition = event.screenY - 150;
   xPosition = event.screenX + 80;
   
   Calendar.popup();
}

/**
 * Generic handler for date icon clicked
 */ 
 function handleDateSetUpIconClicked(evt, fieldId) {

    // Retrieve the field
    var field = document.getElementById(fieldId);

    
    // Pre-Validate date and blank if not valid
    if (typeof field  == "undefined") {
      field.value = '';
    }
    
    // Popup calendar
    handleCalendarDateSetup(field, evt);
 }
 /**
  * Added to display 408(a) Disclosure information.
  */
 function doDisplayRegulatoryDisc()
 {
   var reportURL = new URL();
   reportURL.setParameter("task", "regulatoryDisclosure");
   reportURL.setParameter("lastVisited", "true");
   for (var key in filterMap) {
     reportURL.setParameter(key, filterMap[key]);
   }
   location.href = reportURL.encodeURL();
 }

