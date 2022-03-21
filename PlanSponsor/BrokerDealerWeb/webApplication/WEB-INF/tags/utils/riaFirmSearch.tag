<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ tag body-content="empty"%>
<%@ attribute name="firmId" required="true" %>
<%@ attribute name="firmName" required="true" %>

<%
%>

<style type="text/css">
/*margin and padding on body element
  can introduce errors in determining
  element position and are not recommended;
  we turn them off as a foundation for YUI
  CSS treatments. */
body {
	margin:0;
	padding:0;
}
</style>

<style type="text/css">
#riaFirmSearch {
    width:12em; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
	z-index:9000;
}
#riaFirmSearchContainer {
	width:28em; 
}
</style>
<br/>
<div class="yui-skin-sam">
	<div id="riaFirmSearch">
		<input type="hidden" name="lastSelectedRiaFirmName" id="lastSelectedRiaFirmName"/>
		<input type="hidden" id="${firmId}" name="${firmId}"/>
		<form:input  id="${firmName}" path="${firmName}" style='font-weight:normal;font-size:8pt;' maxlength="100"/>
		<div id="riaFirmSearchContainer"></div>
	</div>
</div>

<script type="text/javascript">
    // Use an XHRDataSource
    var oDS = new YAHOO.widget.DS_XHR("/do/firmsearch/", ["ResultSet.Result","firmName","firmId"]);
	
    oDS.scriptQueryAppend = "firmType=RIA";
    
    // Instantiate the AutoComplete
    var oAC = new YAHOO.widget.AutoComplete("${firmName}", "riaFirmSearchContainer", oDS);
	oAC.minQueryLength = 3;
	oAC.maxResultsDisplayed = 10;
	oAC.useShadow = 10;
	oAC.animVert  = false;
	oAC.forceSelection = true;

	//To change the query string to upper case
	oAC.doBeforeSendQuery = function(sQuery) { 
	    return sQuery.toUpperCase(); 
	};
	
	//The firm name that is prepopulated after page submit.
	var prepopulatedFirmName = YAHOO.util.Dom.get("${firmName}").value;

	//To set the hidden field
	function selectEvent_Callback(e, args) {
		//There will be three parameters. Third one is the data to be set in the text box. So args[2] will give the third parameter.
		YAHOO.util.Dom.get("${firmId}").value = args[2][1];
		YAHOO.util.Dom.get("lastSelectedRiaFirmName").value = args[2][0];
		// Only applicable for RIA eStatements Internal
		try{
			doProtectedSubmitWithRIAFirms(document.estatementReportForm, 'fetchDocuments');
		} catch(e){
			// do nothing
		}
		// Only applicable for 
		try{
			addFirm(document.createRiaUserForm)
		}catch (e) {
			// do nothing
		}
		try{
			addFirm(document.manageRiaForm)
		}catch(e) {
			//do nothing
		}
		try{
			addFirm(document.manageBrokerForm)
		}catch(e) {
			//do nothing
		}
		try{
			addFirm(document.manageAssistantForm)
		}catch(e) {
			//do nothing
		}
		try{
			addRiaFirm(document.manageBasicBrokerForm)
		}catch(e) {
			//do nothing
		}
	    try{
			addRiaFirm(document.manageFirmRepForm)
		}catch(e) {
			//do nothing
		}
	}

	/**
	 * This is a private method in autocomplete.js. By default
	 * it will clear the text box if there is no match. We want to 
	 * retain the old text. So we are overriding it. The code that
	 * clears the text box is commented.
	 *
	 * @method _clearSelection
	 * @private
	 */
	oAC._clearSelection = function() {
		var sValue = this._elTextbox.value;
		var sChar = (this.delimChar) ? this.delimChar[0] : null;
		var nIndex = (sChar) ? sValue.lastIndexOf(sChar, sValue.length-2) : -1;
		if(nIndex > -1) {
			this._elTextbox.value = sValue.substring(0,nIndex);
		}
	 /*   else {
			 this._elTextbox.value = "";
		} */
		this._sSavedQuery = this._elTextbox.value;

		// Fire custom event
		this.selectionEnforceEvent.fire(this);
	};
	
	// If no match hidden id will be set as empty
	function selectionEnforceEvent_Callback(e,args) {
		//When the page is loaded for the first time, the firm results will be empty. But the already
		//selected firm name and id will be prepopulated. But during the onblur event of the firmsearch 
		//text box, the current firm name will be compared with the existing result set. Since the result set
		//is empty, the current firm name will be considered invalid even though it is a valid value and also
		//the firm id will be reset to empty. To avoid this we store the prepopulated firm name and compare it with
		//the current value in the text box. If they are same we will not reset the firm id.
	    if(YAHOO.util.Dom.get("${firmName}").value != prepopulatedFirmName) {
			YAHOO.util.Dom.get("${firmId}").value = "";
		}
	}

	oAC.selectionEnforceEvent.subscribe(selectionEnforceEvent_Callback);
	oAC.itemSelectEvent.subscribe(selectEvent_Callback);

	// To include 'More' in the results div
	oAC. doBeforeExpandContainer = function( elTextbox , elContainer , sQuery , aResults ) {
		if(aResults.length > 10) {
			oAC.setFooter("<div id='more' style='text-align:right;' ><span style='font-weight:normal;font-size:8pt;font-weight:bold;color:darkgrey'>More...</span></div>");
		}
		else {
			oAC.setFooter("");
		}
		return true; 		
	}; 

	// Formatting with different font
	oAC.formatResult = function(aResultItem, sQuery) {
		var sKey = aResultItem[1] + " - " + aResultItem[0]; // the entire result key
		var aMarkup = [
			   "<div id='searchresult'>",
					"<span style='font-weight:normal;font-size:8pt'>",
							sKey,
					"</span>",
			   "</div>"
			];
     return (aMarkup.join(""));
	};
	
</script>