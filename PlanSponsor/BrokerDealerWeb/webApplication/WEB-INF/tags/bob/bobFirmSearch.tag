<%@ tag body-content="empty"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- This gives the rvpId for which we need the associated Firm details.--%>
<%@ attribute name="rvpId" required="false" %>

<%-- input ID for firmID--%>
<%@ attribute name="inputIDForFirmID" required="true" %>
<%-- input ID for firmName--%>
<%@ attribute name="inputIDForFirmName" required="true" %>
<%-- input value for firmID--%>
<%@ attribute name="inputValueForFirmID" required="true" %>
<%-- input ID for firmSearch Container--%>
<%@ attribute name="IDfirmSearchContainer" required="true" %>
<%-- input name for firmID--%>
<%@ attribute name="inputNameForFirmID" required="true" %>
<%-- input name for firmName--%>
<%@ attribute name="inputNameForFirmName" required="true" %>
<%-- input value for firmName--%>
<%@ attribute name="inputValueForFirmName" required="true" %>
<%-- input ID for firmSearch DIVtag--%>
<%@ attribute name="firmSearchDivID" required="true"%>
<%-- variable Name that will hold the AutoComplete instance --%>
<%@ attribute name="variableName" required="false"%>
<%-- variable Name that will hold the pre populated Firm name --%>
<%@ attribute name="prepopulatedFirmNameVar" required="false"%>


<c:if test="${empty variableName}">
	<c:set var="variableName" value="oAC" />
</c:if>
<c:if test="${empty prepopulatedFirmNameVar}">
	<c:set var="prepopulatedFirmNameVar" value="prepopulatedFirmName" />
</c:if>

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

#${IDfirmSearchContainer} {
	width: 28em;
}
</style>

<div class=" yui-skin-sam">
	<% if (getJspContext().getAttribute("inputValueForFirmID") == null) {
		getJspContext().setAttribute("inputValueForFirmID", "");
	   } 
	   if (getJspContext().getAttribute("inputValueForFirmName") == null) {
		getJspContext().setAttribute("inputValueForFirmName", "");
	   } 
	%>
	<div id="${firmSearchDivID}" class="padding_bottom" style='width:12em;display:none;'>
		<input id="${inputIDForFirmID}" name="${inputNameForFirmID}" value="${inputValueForFirmID}" type="hidden"/>
		<input id="${inputIDForFirmName}" name="${inputNameForFirmName}" value="${inputValueForFirmName}" type="text" maxlength="1024" style="width:130px;"/>
		<div id="${IDfirmSearchContainer}"></div>
	</div>
</div>

<script type="text/javascript">
	<c:if test="${empty rvpId}">
	    // Use an XHRDataSource
    	    var oDS = new YAHOO.widget.DS_XHR("/do/firmsearch/", ["ResultSet.Result","firmName","firmId"]);
	</c:if>
	<c:if test="${not empty rvpId}">
	    // Use an XHRDataSource
    	    var oDS = new YAHOO.widget.DS_XHR("/do/firmsearch/", ["ResultSet.Result","firmName","firmId"]);
    	    //Pass the extra request parameters related to rvp.
    	    oDS.scriptQueryAppend = "action=filter&rvpId=${rvpId}";
	</c:if>

    // Instantiate the AutoComplete
    var ${variableName} = new YAHOO.widget.AutoComplete("${inputIDForFirmName}", "${IDfirmSearchContainer}", oDS);
	${variableName}.minQueryLength = 3;
	${variableName}.maxResultsDisplayed = 10;
	${variableName}.useShadow = 10;
	${variableName}.animVert  = false;
	${variableName}.forceSelection = true;

	//To change the query string to upper case
	${variableName}.doBeforeSendQuery = function(sQuery) {
	    return sQuery.toUpperCase(); 
	};

	//The firm name that is prepopulated after page submit.
	var ${prepopulatedFirmNameVar} = YAHOO.util.Dom.get("${inputIDForFirmName}").value;

	//To set the hidden field
	function selectEvent_Callback(e, args) {
		//There will be three parameters. Third one is the data to be set in the text box. So args[2] will give the third parameter.
		YAHOO.util.Dom.get("${inputIDForFirmID}").value = args[2][1];
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
	${variableName}._clearSelection = function() {
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
	    if(YAHOO.util.Dom.get("${inputIDForFirmName}").value != ${prepopulatedFirmNameVar}) {
	    	YAHOO.util.Dom.get("${inputIDForFirmID}").value = "";
		}
	}

	${variableName}.selectionEnforceEvent.subscribe(selectionEnforceEvent_Callback);
	${variableName}.itemSelectEvent.subscribe(selectEvent_Callback);

	// To include 'More' in the results div
	${variableName}. doBeforeExpandContainer = function( elTextbox , elContainer , sQuery , aResults ) {
		if(aResults.length > 10) {
			${variableName}.setFooter("<div id='more' style='text-align:right;' ><span style='font-weight:bold;color:darkgrey'>More...</span></div>");
		}
		else {
			${variableName}.setFooter("");
		}
		return true; 		
	}; 

	// Formatting with different font
	${variableName}.formatResult = function(aResultItem, sQuery) {
		var sKey = aResultItem[0]; // the entire result key
		var aMarkup = [
			   "<div id='searchresult'>",
					"<span style='color:black;'>",
							sKey,
					"</span>",
			   "</div>"
			];
     return (aMarkup.join(""));
	};

</script>