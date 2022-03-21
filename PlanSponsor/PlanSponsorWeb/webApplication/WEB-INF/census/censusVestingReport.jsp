<%-- taglib used --%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%-- Imports --%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusVestingReportForm" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.CensusVestingReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.CensusVestingDetails" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@ page import="com.manulife.pension.service.employee.util.EmployeeValidationErrorCode" %>
<%@ page import="com.manulife.pension.service.vesting.MoneyTypeVestingPercentage"%>
<%@ page import="com.manulife.pension.service.vesting.util.VestingMessageType"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.util.render.NumberRender" %>
<%@ page import="com.manulife.pension.service.vesting.VestingConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.service.contract.valueobject.MoneyTypeVO"%>
<%@ page import="com.manulife.pension.service.employee.util.EmployeeValidationError"%>

 
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">

<%
CensusVestingReportData theReport = (CensusVestingReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%> 

<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>



<%
CensusVestingReportForm censusVestingReportForm = (CensusVestingReportForm)session.getAttribute("censusVestingReportForm");
pageContext.setAttribute("censusVestingReportForm",censusVestingReportForm,PageContext.PAGE_SCOPE);
%>


             
<content:contentBean contentId="<%=ContentConstants.MESSAGE_EMPLOYEES_NO_SEARCH_RESULTS%>" 
					type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noSearchResults"/>
					
<content:contentBean contentId="<%=ContentConstants.WARNING_MISSING_VYOS_HOS%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="missingVYOSAndHOS"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_MISSING_DATE_OF_HIRE%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="missingDateOfHire"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_MISSING_EMPLOYMENT_STATUS%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="missingEmploymentStatus"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_VYOS_DATE_BIRTH_DATE%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="vyosDateBirthDate"/>                                       
<content:contentBean contentId="<%=ContentConstants.WARNING_MINIMUM_VYOS_DATE%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="minimumVyosDate"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_FULLY_VESTED_EFFECTIVE_DATE_BIRTH_DATE%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="fullyVestedEffectiveAndBirthDate"/>
                    <%-- Content used for Javascript validations. --%>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_INVALID_PERCENTAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageInvalidPerc"/>
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_TOO_MANY_DECIMALS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageTooManyDecimals"/>
<content:contentBean
    contentId="56124"
    type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
    id="NonPrintable" />
<content:contentBean contentId="<%=ContentConstants.MESSAGE_VESTING_NOT_ALLOWED_TO_REMOVE_VESTING_PERCENTAGE%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="messageNotAllowedToRemoveExistingVestingPercentages"/>
                    
<c:set var="missingVYOSAndHOSWarning" ><content:getAttribute id="missingVYOSAndHOS" attribute="text"/></c:set>                       
<c:set var="missingDateOfHireWarning" ><content:getAttribute id="missingDateOfHire" attribute="text"/></c:set>     
<c:set var="missingEmploymentStatusWarning"><content:getAttribute id="missingEmploymentStatus" attribute="text"/></c:set>
<c:set var="vyosDateBirthDateWarning"><content:getAttribute id="vyosDateBirthDate" attribute="text"/></c:set>
<c:set var="minimumVyosDateWarning"><content:getAttribute id="minimumVyosDate" attribute="text"/></c:set>
<c:set var="fullyVestedEffectiveAndBirthDateWarning"><content:getAttribute id="fullyVestedEffectiveAndBirthDate" attribute="text"/></c:set>
 
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />
<un:useConstants var="renderConstants" className="com.manulife.util.render.RenderConstants" />

<!-- Bean Definition for CMA Content -->
<content:contentBean
  contentId="${contentConstants.MISCELLANEOUS_EMPLOYEE_SNAPSHOT_PARTICIPANT_PARTIAL_STATUS_WARNING}"
  type="${contentConstants.TYPE_MESSAGE}"
  id="participantPartialStatusMessageContent"/>
 <c:set var="participantPartialStatusMessage"><content:getAttribute id="participantPartialStatusMessageContent" attribute="text"/></c:set>
<script type="text/javascript">

var dirtyCheckMessage = '<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true" escapeJavaScript="true"/>';

  function handleAddClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    <%-- Check if page has been changed --%>
    if (isFormDirty()) {
      <%-- Check user confirmation --%>
      var confirmResponse = confirm(dirtyCheckMessage);
      if (!confirmResponse) {
        resetSubmitInProgress();
        return false;
      }
    }
    <%-- No changes to page so we don't need to confirm, or they approved the confirmation --%>
    window.location="/do/census/addEmployee/?source=censusVesting";
    return true;
  }
  function handleSearchClicked() {
   
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    <%-- Check if page has been changed --%>
    if (isFormDirty()) {
      <%-- Check user confirmation --%>
      var confirmResponse = confirm(dirtyCheckMessage);
      if (!confirmResponse) {
        resetSubmitInProgress();
        return false;
      }
    }
    <%-- No changes to page so we don't need to confirm, or they approved the confirmation --%>
    document.censusVestingReportForm.task.value = "filter";
    return true;
  }
  function handleResetClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    <%-- Check if page has been changed --%>
    if (isFormDirty()) {
      <%-- Check user confirmation --%>
      var confirmResponse = confirm(dirtyCheckMessage);
      if (!confirmResponse) {
        resetSubmitInProgress();
        return false;
      }
    }

    <%-- No changes to page so we don't need to confirm, or they approved the confirmation --%>
    document.censusVestingReportForm.task.value = "reset";
    return true;
  }

  <%-- Handles cancel being clicked --%>
  function handleCancelClicked() {
  
    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }  
  
    <%-- Check if page has been changed --%>
    if (isFormDirty()) {
      <%-- Check user confirmation --%>
      var confirmResponse = confirm(dirtyCheckMessage);
      if (!confirmResponse) {
        resetSubmitInProgress();
        return false;
      }
    }
    <%-- No changes to page so we don't need to confirm, or they approved the confirmation --%>
    // We will allow the event to proceed.

    disableSearchInputs();
    document.censusVestingReportForm.task.value = "reset";
    return true;
  }  

  <%-- Handles save being clicked --%>
  function handleSaveClicked() {
  
    if (!(validatePage())) {
      return false;
    }

    <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }

    disableSearchInputs();
    document.censusVestingReportForm.task.value = "save";
    return true;  
  }

  <%-- Handles edit being clicked --%>
  function handleEditClicked() {
      <%-- Check if submit is in progress --%>
    var submitInProgress = isSubmitInProgress();
    if (submitInProgress) {
      return false;
    }

    //disableIfExistsById('namePhraseId');
    disableSearchInputs();
  
    return true;  
  }

  function disableSearchInputs() {
    disableIfExistsById('segmentId');
    disableIfExistsById('namePhraseId');
    disableIfExistsById('ssnOneId');
    disableIfExistsById('ssnTwoId');
    disableIfExistsById('ssnThreeId');
    disableIfExistsById('divisionId');
    disableIfExistsById('statusId');
    disableIfExistsById('asOfDateId');
  }

  function disableIfExistsById(elementId) {
    // Determine if the elment exists.
    var element = document.getElementById(elementId);
    if ((element != null) && (typeof element != 'undefined')) {
      element.disabled = true;
    } // fi
  }

 function validateMandatory(value) {
   return (value.length != 0);
 }
 
  // common messsages
  var messageInvalidPerc = '<content:getAttribute beanName="messageInvalidPerc" attribute="text" filter="true" escapeJavaScript="true"/>';
  var messageTooManyDecimals = '<content:getAttribute beanName="messageTooManyDecimals" attribute="text" filter="true" escapeJavaScript="true"/>';
  var nonPrintableVestingPercentage = '<content:getAttribute id="NonPrintable" attribute="text"  filter="true" escapeJavaScript="true"><content:param>Vesting percentage</content:param></content:getAttribute>';
  var notAllowedToRemoveExistingVestingPercentagesMessage = '<content:getAttribute beanName="messageNotAllowedToRemoveExistingVestingPercentages" attribute="text" filter="true" escapeJavaScript="true"/>';

function validateVestingPercentage(field, isValueRequired) {
    var res;
    if (isValueRequired) {
      res = validateField(field, 
        new Array(
          validateMandatory
          ), 
        new Array(
          notAllowedToRemoveExistingVestingPercentagesMessage
          ),
        true);
      if (! res) {
        return res;
      }
    }
    
    res = validateField(field, 
        new Array(
            validateNonPrintableAscii,
            validatePercentage, 
            validatePercentageRangeOf0Or1To100,
            validatePerDecimal, 
            validatePositive 
            ), 
        new Array(
            nonPrintableVestingPercentage,
            messageInvalidPerc, 
            messageInvalidPerc, 
            messageTooManyDecimals, 
            messageInvalidPerc),
        true);
    return res;
}

  <%-- Handles a vesting percentage change --%>
  function handleVestingPercentageChange(field, isValueRequired) {
    onFieldChange(field);
    validateVestingPercentage(field, isValueRequired);
    formatPercentage(field);
  }

function validatePerDecimal(value) {   
    return value.indexOf(".") == -1 || value.substring(value.indexOf(".")+1,value.length).length <= 3
}

function formatPercentage(field) {
    var v = field.value;
    if (!isNaN(v) && !validatePerDecimal(v)) {
        var index = v.indexOf(".");
        var newValue = v.substring(0, index + 4);
        field.value = newValue;
    }
}

function onFieldChange(obj) {
  document.getElementById('dirtyFlagId').value = 'true';
  handleFieldChanged(obj);
}

  <%-- Handles any field change --%>
  function handleFieldChanged(field) {
    <c:if test="${censusVestingReportForm.displaySaveButton}">
    document.getElementById('saveButtonId').disabled = false;
    </c:if>
  }

function isFormDirty() {
    return (document.getElementById('dirtyFlagId').value == 'true');
}

/**
 * Determines if a discard confirmation should be displayed.
 */ 
function confirmDiscardChanges(warning) {
  if (document.getElementById('dirtyFlagId').value == 'true') {
    return window.confirm(warning);
  } else {
      return true;
  }
}

 /*
  * This method validates the page, and is used when the user clicks 'save'.
  */ 
  function validatePage() {
    // Need to build a list of validations and return false if there are any failures.
    <c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

	<c:if test="${not empty theReport.moneyTypes}">
	  <c:forEach var="moneyTypeHeader" items="${theReport.moneyTypes}"
	    varStatus="moneyTypeHeaderStatus">
	      <c:if test="${censusVestingReportForm.isEditMode && !(theItem.percentages[moneyTypeHeader.id].moneyTypeFullyVested)}">
            <c:if test="${(not empty theItem.percentages[moneyTypeHeader.id].percentage)}">
    if (!(validateVestingPercentage(
	  document.getElementById("percent_r_${theIndex}_mt_${moneyTypeHeaderStatus.index}"), 'true'))) {
	  return false;
	}
            </c:if>
	      </c:if>
	  </c:forEach>
	</c:if>
</c:forEach>
	</c:if>
	return true;
  }

function protectLinksFromCancel() {
	var hrefs  = document.links;
	if (hrefs != null)
	{
	    for (i=0; i<hrefs.length; i++) { 
	        if (
	            hrefs[i].onclick != undefined && 
	            (hrefs[i].onclick.toString().indexOf("openWin") != -1 
	            || hrefs[i].onclick.toString().indexOf("popup") != -1)
	        ) {
	            // don't replace window open or popups as they won't loose their changes with those
	        }
	        else if (
	            hrefs[i].href != undefined && 
	            (hrefs[i].href.indexOf("openWin") != -1 
	            || hrefs[i].href.indexOf("popup") != -1)
	        ) {
	            // don't replace window open or popups as they won't loose their changes with those
	        }
	        else if (
	            hrefs[i].onclick != undefined 
	                    && (hrefs[i].onclick.toString().indexOf("toggleSection") != -1) 
	        ) {
	                // don't replace these links as we will do an automatic save on the server side before invoking the requested function
	        }
	        else if (
	            hrefs[i].href != undefined 
	                    &&  ( hrefs[i].href.indexOf("toggleSection") != -1 
	                            || hrefs[i].href.indexOf("javascript:doCalendar") != -1) 
	        ) {
	                // don't replace these links as we will do an automatic save on the server side before invoking the requested function
	        }
	        else if (hrefs[i].onclick != undefined) {
	            if (hrefs[i].onclick.toString().indexOf("task=download") != -1) {
	                hrefs[i].onclick = new Function ("var result = confirmDiscardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
	            } else {
	                hrefs[i].onclick = new Function ("var result = confirmDiscardChanges(dirtyCheckMessage);" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
	            }               
	        }
	        else {
	            if (hrefs[i].href.indexOf("task=download") != -1) {
	                hrefs[i].onclick = new Function ("return confirmDiscardChanges('Warning! You have unsaved changes that will not be included in the download. Do you want to continue with the download?');");
	            } else {
	                hrefs[i].onclick = new Function ("return confirmDiscardChanges(dirtyCheckMessage);");
	            }
	        }
	    }
	}
}   

// Registers the protect links with the functions that are run on load.
if (typeof(runOnLoad) == "function") {
  runOnLoad( protectLinksFromCancel );
}

</script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>



<%boolean errors = false;%>

<% 
int numberOfColumns = 11;
int moneyTypeCount = 0;
int moneyTypesDisplayed = 5;
int rowspan = 1;

if (theReport.getMoneyTypes() != null && theReport.getMoneyTypes().size() > 0) {
	moneyTypeCount = theReport.getMoneyTypes().size();
	rowspan = 3;
}
%>
<c:if test="${empty param.printFriendly}">
<% numberOfColumns = 13; %>
</c:if>

<% 
	String style_scrolling="overflow:scroll; overflow-y:hidden; z-index:2; width:715px;"; 
	String style_fixed="overflow:hidden; z-index:3; visibility:hidden; position:absolute; width:375px;";
	boolean isMozilla = request.getHeader("User-Agent").toUpperCase().indexOf("GECKO") != -1;
	if (isMozilla) {
		style_scrolling="overflow:scroll; overflow-y:hidden; z-index:2; width:730px;"; 
	}
%>

<c:set var="showDivColumn" value="${userProfile.getCurrentContract().hasSpecialSortCategoryInd()}"/>
<c:if test="${showDivColumn eq false}">
<% numberOfColumns = numberOfColumns - 2; %>
</c:if>
<c:if test="${showDivColumn eq true}">
<%
	style_fixed="overflow:hidden; z-index:3; visibility:hidden; position:absolute; width:435px;";
	moneyTypesDisplayed = 4;
%>
</c:if>

<c:if test="${not empty param.printFriendly}">
<% style_scrolling="overflow:hidden;"; %>
</c:if>

<script type="text/javascript">
function matchColumnHeaderHeights() {
  maxHeight=0;
   
  var scrollableHeaderElement = document.getElementById("scrollableHeaderElement");
  
  if(scrollableHeaderElement.offsetHeight){ 
    divHeight=scrollableHeaderElement.offsetHeight;                  
  } else if(scrollableHeaderElement.style.pixelHeight){ 
    divHeight=scrollableHeaderElement.style.pixelHeight;                  
  }
  // calculate maximum height 
  maxHeight=Math.max(maxHeight,divHeight);

  var fixedHeaderElement = document.getElementById('fixedHeaderElement');
  if(fixedHeaderElement.offsetHeight){ 
    divHeight=fixedHeaderElement.offsetHeight;                  
  } else if(fixedHeaderElement.style.pixelHeight){ 
    divHeight=fixedHeaderElement.style.pixelHeight;                  
  }
  // calculate maximum height 
  maxHeight=Math.max(maxHeight,divHeight);

  fixedHeaderElement.style.height=maxHeight + "px";
  scrollableHeaderElement.style.height=maxHeight + "px";
}

if (typeof(runOnLoad) == "function") {
  runOnLoad( matchColumnHeaderHeights );
}
</script>

<script type="text/javascript" >

var fixedTable;
var scrollingTable;


if (window.addEventListener) {
	window.addEventListener('load', init, false);
} else if (window.attachEvent) {
	window.attachEvent('onload', init);
} else if (document.getElementById)
	window.onload=init;
	
function init() {
<c:if test="${not empty theReport.details}">
  <c:if test="${empty param.printFriendly}">
  <% if (moneyTypeCount > moneyTypesDisplayed) { %>
  	scrollingTable = document.getElementById("scrollingTable");
  	fixedTable = document.getElementById("fixedTable");

  	if ( navigator.userAgent.toLowerCase().indexOf( 'gecko' ) != -1 ) {
     	fixedTable.style.overflow='-moz-scrollbars-none'; 
     	scrollingTable.style.overflow='-moz-scrollbars-horizontal';
  	}
  	
  	//alert(scrollingTable.offsetTop);
  	//alert(scrollingTable.offsetParent.offsetParent.offsetTop);
  	
  	fixedTable.style.top = scrollingTable.offsetTop + 
  						   scrollingTable.offsetParent.offsetParent.offsetTop;
  	fixedTable.style.left = 30;
  	fixedTable.style.visibility = 'visible';
  <% } %>
  </c:if>
</c:if>
}

// Called when add employee is clicked
function doAdd() {
  return handleAddClicked();
}
  
// Called when reset is clicked
function doReset() {
	document.censusVestingReportForm.task.value = "reset";
	return true;
}

function clearName(evt){

	//IE or browsers that use the getElementById model
	if (document.getElementById('namePhrase')) {
		if (document.getElementById('namePhrase').value) {
			document.getElementById('namePhrase').value = "";
		}
	}

	//Netscape or browsers that use the document model
  	evt = (evt) ? evt : (window.event) ? event : null;
  	if (evt)
  	{	
    	var charCode = (evt.charCode) ? evt.charCode :
                   ((evt.keyCode) ? evt.keyCode :
                   ((evt.which) ? evt.which : 0));
    	if (charCode == 9) {
    		return false;
    	}
  	}    
	
	if (document.censusVestingReportForm.ssnOne) {
			if (document.censusVestingReportForm.ssnOne.value.length >= 0){
					document.censusVestingReportForm.namePhrase.value = "";
			}
	}


	if (document.censusVestingReportForm.ssnTwo) {
			if (document.censusVestingReportForm.ssnTwo.value.length >= 0){	
				document.censusVestingReportForm.namePhrase.value = "";
			}
	}

	if (document.censusVestingReportForm.ssnThree) {
			if (document.censusVestingReportForm.ssnThree.value.length >= 0){	
				document.censusVestingReportForm.namePhrase.value = "";
			}
	}

}

function clearSSN(evt){

	//IE or browsers that use the getElementById model
	if (document.getElementById('ssnOne')) {
		if (document.getElementById('ssnOne').value) {
			document.getElementById('ssnOne').value = "";
		}
	} 


	if (document.getElementById('ssnTwo')) {
		if (document.getElementById('ssnTwo').value) {
			document.getElementById('ssnTwo').value = "";
		}
	} 

	if (document.getElementById('ssnThree')) {
		if (document.getElementById('ssnThree').value) {
			document.getElementById('ssnThree').value = "";
		}
	} 	
	
	//Netscape or browsers that use the document model
	evt = (evt) ? evt : (window.event) ? event : null;
  	if (evt)
  	{	
    	var charCode = (evt.charCode) ? evt.charCode :
                   ((evt.keyCode) ? evt.keyCode :
                   ((evt.which) ? evt.which : 0));
    	if (charCode == 9) {
    		return false;
    	}
  	}    
	
	
	if (document.censusVestingReportForm.namePhrase) {
			if (document.censusVestingReportForm.namePhrase.value.length >= 0){
				document.censusVestingReportForm.ssnOne.value = "";
				document.censusVestingReportForm.ssnTwo.value = "";
				document.censusVestingReportForm.ssnThree.value = "";
			}	
	}
	
}

</script>


     <p>
       	<c:if test="${not empty sessionScope.psErrors}">
<c:set var="errorsExist" value="${true}" scope="page" />
        <div id="errordivcs"><content:errors scope="session"/></div>
        <%errors=true;%>
        </c:if>
     </p>

<!-- Beginning of census Vesting report body -->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<ps:form action="/do/census/censusVesting/" modelAttribute="censusVestingReportForm" name="censusVestingReportForm">
		<input type="hidden" name="task"/>
		<form:hidden path="dirty" id="dirtyFlagId"/>
        
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
        
        <tr>
      		<td colspan="4" > 
        		<jsp:include flush="true" page="censusVestingErrors.jsp"/>       
      		</td>
    	</tr>
    	<tr>
	    	<td colspan="4"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>
	    </tr>
        
        <%-- TAB section --%>
    	<tr>
      		<td valign="bottom" colspan="4">
      			<DIV class="nav_Main_display" id="div">
      			<UL class="">
        			<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:if test="${not empty param.printFriendly}">
                    Summary
                    </c:if>
                    <c:if test="${empty param.printFriendly}">
			          	<A href="/do/census/censusSummary/">
			        Summary
			        	</A>
			        </c:if>
			        </LI>
			        <LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:if test="${not empty param.printFriendly}">
                    Addresses
                    </c:if>
			        <c:if test="${empty param.printFriendly}">
			          	<A href="/do/participant/participantAddresses">
			        Addresses
			        	</A>
			        </c:if>
			        </LI>
        			<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:if test="${not empty param.printFriendly}">
                    Eligibility
                    </c:if>
        			<c:if test="${empty param.printFriendly}">
        				<A href="/do/census/employeeEnrollmentSummary">
        			Eligibility
			        	</A>
			        </c:if>
        			</LI>
        			
<c:if test="${censusVestingReportForm.allowedToAccessDeferralTab ==true}">
      			  	<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:if test="${not empty param.printFriendly}">
                    Deferrals
                    </c:if>
      			  	<c:if test="${empty param.printFriendly}">
      			  		<A href="/do/census/deferral">
      			  	Deferrals
      			  		</A>
      			  	</c:if>
      			  	</LI>
</c:if>
        			
        			<LI id="tab5" class="on">Vesting</LI>
      			</UL>
      			</DIV>
      		</td>
    	</tr>
    	<tr>
			<TD colspan="4" height="25" class="tablesubhead"><b>Employee Vesting Search</b></TD>
		</tr>
		<tr>
            <td colspan="4" valign="top" class="filterSearch">
            To search for an employee by last name or SSN, make your selection below and click "search" to complete your request.
            </td>
        </tr>
        <tr>
            <td width="220" valign="top" class="filterSearch"><b>Segment</b><br>
                <c:if test="${not empty param.printFriendly}">
<form:select path="segment" disabled="true" >
<form:options items="${censusVestingReportForm.segmentList}" itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
			    <c:if test="${empty param.printFriendly}">
				<form:select path="segment" cssStyle="segmentId" tabindex="10">
<form:options items="${censusVestingReportForm.segmentList}" itemLabel="label" itemValue="value"/>
				</form:select>
			    </c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch"><ps:label fieldId="lastName" mandatory="false"><b>Last name</b></ps:label><br>
                <c:if test="${not empty param.printFriendly}">
<form:input path="namePhrase" maxlength="20" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${empty param.printFriendly}">
<form:input path="namePhrase" maxlength="20" onkeyup="clearSSN(event);" cssClass="inputField" id="namePhraseId" tabindex="20"/>
			    </c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch">
            	<ps:label fieldId="ssn" mandatory="false"><b>SSN</b></ps:label><br>
                <c:if test="${not empty param.printFriendly}">
                <form:password  path="ssnOne"  cssClass="inputField" readonly="true" size="3" onkeyup="return autoTab(this, 3, event);" maxlength="3"/>
				<form:password  path="ssnTwo" cssClass="inputField" readonly="true" size="2" onkeyup="return autoTab(this, 2, event);" maxlength="2"/>
				<form:input  path="ssnThree" autocomplete="off" cssClass="inputField" readonly="true" size="4" onkeyup="return autoTab(this, 4, event);" maxlength="4"/>
				
				</c:if>
				<c:if test="${empty param.printFriendly}">
				<form:password  path="ssnOne" autocomplete="off" cssClass="inputField" onkeypress = "clearName(event);" 
						onkeyup="clearName(event); return autoTab(this, 3, event);" value="${censusVestingReportForm.ssnOne}" size="3" maxlength="3" tabindex="30" id="ssnOneId"/>
				<form:password  path="ssnTwo" autocomplete="off" cssClass="inputField" onkeypress = "clearName(event);" 
						onkeyup="clearName(event); return autoTab(this, 2, event);" value="${censusVestingReportForm.ssnTwo}" size="2" maxlength="2" tabindex="31" id="ssnTwoId"/>
				<form:input path="ssnThree" autocomplete="off"  cssClass="inputField"   onkeypress = "clearName(event);" 
						onkeyup="clearName(event); return autoTab(this, 4, event);" size="4" maxlength="4" tabindex="32" id="ssnThreeId"/>
				</c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch">
            <c:if test="${showDivColumn eq true}">
            	<b>Division</b><br>
                <c:if test="${not empty param.printFriendly}">
<form:input path="division" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${empty param.printFriendly}">
<form:input path="division" cssClass="inputField" id="divisionId" tabindex="40"/>
			    </c:if>
</c:if>&nbsp;
            </td>
        </tr>
	    <tr>
	        <td valign="top" class="filterSearch"><b>Reporting period end date</b><br>
				<ps:select name="censusVestingReportForm" styleId="asOfDateId" property="asOfDate" tabindex="50">
				<ps:dateOptions name="censusVestingReportForm" 
						  property="asOfDateList" 
						  renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
				</ps:select>
	        </td>
	        <td valign="top" class="filterSearch"><b>Employment status</b><br>
				<c:if test="${not empty param.printFriendly}">
<form:select path="status" disabled="true" >
					<%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${censusVestingReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
				<c:if test="${empty param.printFriendly}">
					<form:select cssStyle="statusId" path="status" tabindex="50">
				    <%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${censusVestingReportForm.statusList}" itemLabel="label" itemValue="value"/>
					</form:select>
			    </c:if>
	        </td>
	        <td valign="top" class="filterSearch"></td>
	        <td valign="middle" class="filterSearch"><div align="left">
	        <c:if test="${empty param.printFriendly}">
	        	<input type="submit" name="submit" value="search" tabindex="60" onclick="return handleSearchClicked();" />
	        	<input type="submit" name="reset" value="reset" tabindex="70" onclick="return handleResetClicked();"/>
			</c:if>&nbsp;
	        </div></td>
	    </tr>
	    <tr>
	    	<td colspan="4" class="filterSearch"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>
	    </tr>
		</table>
		
        
		<!-- Start of body title -->
		<table width="100%" border="0" cellspacing="0" cellpadding="0"> 
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr><td>&nbsp;</td></tr>
		<c:if test="${empty param.printFriendly}">
		<c:if test="${censusVestingReportForm.allowedToViewVesting}">
        <tr>
        	<td align="left">
        	  <strong>Legend</strong>:&nbsp;<img 
        	  src="/assets/unmanaged/images/view_icon.gif" width="12" height="12"
        	  />&nbsp;View<c:if 
        	    test="${censusVestingReportForm.displayInfoIcon}"
        	      >&nbsp;<img 
        	      src="/assets/unmanaged/images/info_icon.gif" width="12" height="12" 
        	      />&nbsp;Vesting Information</c:if>
        	</td>
        </tr>
        </c:if>
        </c:if>
        <tr class="tablehead">
        	<td class="tableheadTD1" width="20%">&nbsp;</td>         
		    <% if (errors==false) { %>
		    <td class="tableheadTDinfo" align="left" width="15%"><b><report:recordCounter report="theReport" label="Employees"/></b></td>
		    <td align="right" class="tableheadTDinfo" width="10%"><report:pageCounter report="theReport" formName="censusVestingReportForm"/></td>
		    <% } else { %>
            <td colspan="2"></td>
	        <% } %>
		</tr>
		</table>
		<!-- End of body title -->
		
<c:if test="${not empty theReport.details}">
	    <c:if test="${empty param.printFriendly}">
	    <% if (moneyTypeCount > moneyTypesDisplayed) { %>
	    <div id="fixedTable" style="<%=style_fixed%>">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${empty param.printFriendly}">
	          <td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="30"></td>
	          <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	</c:if>
          	<td width="100"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="150"></td>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${showDivColumn eq true}">
          	  <td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	  <td width="60"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="60"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
</c:if>
          	<c:if test="${showDivColumn eq false}">
          	  <td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
</c:if>
        </tr>
        <tr class="tablesubhead">
          <td class="databorder" width="1"></td>
          <c:if test="${empty param.printFriendly}">
          	<td id="fixedHeaderElement" valign="top"><b>Action</b></td>
          	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
		  <td valign="top"><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_LAST_NAME%>" direction="asc"><b>Name</b></report:sort></td>
          <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top">
            <c:choose>
              <c:when test="${censusVestingReportForm.isVestingServiceFeatureCalculate}">
                <b>Vesting<br>last updated</b>
              </c:when>
              <c:otherwise>
                <b>Vesting<br>effective date</b>
              </c:otherwise>
            </c:choose>
          </td>
          <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <c:if test="${showDivColumn eq true}">
		  	<td valign="top"><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_STATUS%>" direction="asc"><b>Employment status</b></report:sort></td>
		  	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td valign="top"><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_DIVISION%>" direction="asc"><b>Division</b></report:sort></td>
		    <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
		  <c:if test="${showDivColumn eq false}">
		  	<td valign="top"><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_STATUS%>" direction="asc"><b>Employment status</b></report:sort></td>
		  	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
		</tr>
		
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >



<%-- 		<%  CensusVestingDetails theItem = (CensusVestingDetails)pageContext.getAttribute("theItem"); --%>
<%-- 			String temp = pageContext.getAttribute("indexValue").toString(); %> --%>
<!-- 		  if (Integer.parseInt(temp)% 2 == 0) { %> -->
<!-- 	        <tr class="datacell3"> -->
<%-- 		  <% } else { %> --%>
<!-- 	        <tr class="datacell1"> -->
<%-- 		  <% } %> --%>
        <c:set var="indexValue1" value="${theIndex.index}"/>
													<c:if test="${indexValue1 % 2 == 0}">
													 <tr class="datacell3">
														
													</c:if>
													<c:if test="${indexValue1 % 2 != 0}">
														<tr class="datacell1">
													</c:if>
         <%--  <tr class="${((theIndex % 2) == 0) ? "datacell3" : "datacell1"}"> --%>

          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <c:if test="${empty param.printFriendly}">
          	<td align="center"> <report:actions profile="userProfile" item="theItem" action="editEmployeeVesting"/> </td>
          	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
          <td>
       
<c:if test="${userProfile.welcomePageAccessOnly == false}">
<c:if test="${theItem.participantInd ==true}">
          <ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
          <span class="printFriendlyLink">
			${theItem.wholeName}
			          </span>
				      </ps:link>
			</c:if>
			<c:if test="${theItem.participantInd ==false}">
			${theItem.wholeName}
			</c:if>
			</c:if>
			<c:if test="${userProfile.welcomePageAccessOnly == true}">
			${theItem.wholeName}
</c:if>
	      <br>
          <render:ssn property="theItem.ssn"/>
          </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" defaultValue="" property="theItem.vestingEffDate"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>
            <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="theItem" property="status"/>
            <c:if test="${not empty theItem.employeeStatusDate}">
              <br/>
              <fmt:formatDate value="${theItem.employeeStatusDate}" pattern="${renderConstants.MEDIUM_MDY_SLASHED}" />
            </c:if>
          </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <c:if test="${showDivColumn eq true}">
<td>${theItem.division}</td>
            <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                
</c:if>
        </tr>
</c:forEach>
        
        </table>
	    </div>
	    <% } %>
	    </c:if>
</c:if>

		<div id="scrollingTable" style="<%=style_scrolling%>">
		<table width="95%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${empty param.printFriendly}">
	          <td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="30"></td>
	          <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	</c:if>
          	<td width="100"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="150"></td>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${showDivColumn eq true}">
          	  <td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	  <td width="60"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="60"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
</c:if>
          	<c:if test="${showDivColumn eq false}">
          	  <td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
</c:if>
<c:if test="${not empty theReport.moneyTypes}">
<c:forEach items="${theReport.moneyTypes}" var="moneyTypeHeader">
				<td align="right" width="55"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="55"></td>
				<td width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
</c:forEach>
</c:if>
<c:if test="${empty theReport.moneyTypes}">
				<td align="right" width="280"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="280"></td>
				<td width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
</c:if>
			<td width="50"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="50"></td>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr>
        
        <tr class="tablesubhead">
          <td class="databorder" rowspan="<%=rowspan%>" width="1"></td>
          <c:if test="${empty param.printFriendly}">
          	<td id="scrollableHeaderElement" valign="top" rowspan="<%=rowspan%>" width="30"><b>Action</b></td>
          	<td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
		  <td valign="top" rowspan="<%=rowspan%>"><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_LAST_NAME%>" direction="asc"><b>Name</b></report:sort></td>
          <td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" rowspan="<%=rowspan%>">
            <c:choose>
              <c:when test="${censusVestingReportForm.isVestingServiceFeatureCalculate}">
                <b>Vesting<br>last updated</b>
              </c:when>
              <c:otherwise>
                <b>Vesting<br>effective date</b>
              </c:otherwise>
            </c:choose>
          </td>
          <td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <c:if test="${showDivColumn eq true}">
		  	<td valign="top" rowspan="<%=rowspan%>"><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_STATUS%>" direction="asc"><b>Employment status</b></report:sort></td>
		  	<td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td valign="top" rowspan="<%=rowspan%>" ><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_DIVISION%>" direction="asc"><b>Division</b></report:sort></td>
		    <td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
		  <c:if test="${showDivColumn eq false}">
		  	<td valign="top" rowspan="<%=rowspan%>"><report:sort formName="censusVestingReportForm" field="<%=CensusVestingReportData.SORT_STATUS%>" direction="asc"><b>Employment status</b></report:sort></td>
		  	<td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
<c:if test="${not empty theReport.moneyTypes}">
		  <td align="center" colspan="<%=(moneyTypeCount * 2) - 1%>"><strong>Money types</strong></td>
		  <td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
<c:if test="${empty theReport.moneyTypes}">
		  <td align="center" valign="top" rowspan="<%=rowspan%>"><strong>Money types</strong></td>
		  <td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
		  <td valign="top" rowspan="<%=rowspan%>"><b>Warning</b></td>
		  <td class="databorder" rowspan="<%=rowspan%>"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		
<c:if test="${not empty theReport.moneyTypes}">
		<tr class="tablesubhead">
		  <td class="dataheaddivider" colspan="<%=(moneyTypeCount * 2) - 1%>" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		</tr>
		<tr class="tablesubhead">
<c:forEach items="${theReport.moneyTypes}" var="moneyTypeHeader" varStatus="theIndex">
<%--  <c:set var="indexValue" value="${theIndex.index}"/>  --%>
<%-- 	 <%String  theIndexVal = pageContext.getAttribute("indexValue").toString();  --%>
	 
<%-- 	 %>  --%>
	 
<%-- 	 <c:set var="indexValue2" value="${theIndex.index}"/> --%>
<%-- 													<c:if test="${indexValue2 % 2 == 0}"> --%>
<!-- 														<tr > -->
														
<%-- 													</c:if> --%>
<%-- 													<c:if test="${indexValue2 % 2 != 0}"> --%>
<!-- 														<tr class="spec"> -->
<%-- 													</c:if> --%>
													
													
													
													
      		<td align="center" width="55" nowrap="nowrap" title="${moneyTypeHeader.contractLongName}">
      		<c:if test="${moneyTypeHeader.fullyVested == 'Y'}">
      		  *
      		</c:if>
      		${moneyTypeHeader.contractShortName}
            <c:if test="${not empty moneyTypeHeader.tedCode}">
              <br/>
              <span style="{font-size: 9px; color: #666666;}">(${moneyTypeHeader.tedCode})</span>
            </c:if>
            </td>
<%--             <c:if test="${indexValue < '<%=(moneyTypeCount * 2) - 1%>'}"> --%>
            <td class="dataheaddivider" valign="bottom" width="1" height="14"><img src="/assets/unmanaged/images/s.gif" width="1" height="14"></td>
<%--             </c:if> --%>
<%--       		<% if(Integer.parseInt(theIndexVal) < (moneyTypeCount - 1)) { %> --%>
<!-- 			<td class="dataheaddivider" valign="bottom" width="1" height="14"><img src="/assets/unmanaged/images/s.gif" width="1" height="14"></td> -->
<%-- 			<% } %> --%>
</c:forEach>
	  	</tr>
</c:if>
				
        
        <% if (theReport.getDetails() == null || theReport.getDetails().size() == 0) { %>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top" colspan="<%=numberOfColumns - 2%>">
			<content:getAttribute id="noSearchResults" attribute="text"/>
		  </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<% } %>

		<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%CensusVestingDetails theItem = (CensusVestingDetails)pageContext.getAttribute("theItem");%>

          <c:if test="${censusVestingReportForm.displayMouseOverHistory}">
            <c:choose>
              <c:when test="${(not empty theItem.tipChangedOn) || (not empty theItem.tipChangedBy) || (not empty theItem.tipSourceOfTheChange)}">
                <c:set var="rowMouseOverAttributes" >
                onMouseEnter="TagToTip('tipSpan_${theItem.profileId}', SHADOW, true, FADEIN, 0, FADEOUT, 0);" onMouseLeave="UnTip();"
                </c:set>
              </c:when>
              <c:otherwise>
                <c:remove var="rowMouseOverAttributes"/>
              </c:otherwise>
            </c:choose>
          </c:if>
          
          
				<c:set var="indexValue3" value="${theIndex.index}"/>

							<c:if test="${indexValue3 % 2 == 0}">
							    <tr class="datacell3">
														
							</c:if>
							<c:if test="${indexValue3 % 2 != 0}">
								<tr class="datacell1">
							</c:if>

<%-- 		 <%	String temp = pageContext.getAttribute("indexValue").toString(); --%>
<%-- 		  if (Integer.parseInt(temp)% 2 == 0) { %> --%>
<!-- 	        <tr class="datacell3"> -->
<%-- 		  <% } else { %> --%>
<!-- 	        <tr class="datacell1"> -->
<%-- 		  <% } %> --%>
	    <!--   <tr class=""> -->
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <c:if test="${censusVestingReportForm.displayMouseOverHistory}">
              <span id="tipSpan_${theItem.profileId}" style="display:none;">
                <c:if test="${not empty theItem.tipChangedOn}">
                  Changed on: <fmt:formatDate value="${theItem.tipChangedOn}" 
                    pattern="MM/dd/yyyy 'AT' HH:mm 'ET'"/>
                </c:if>
                <c:if test="${not empty theItem.tipChangedByDisplay}">
                  Changed by: ${theItem.tipChangedByDisplay}
                </c:if>
                <c:if test="${not empty theItem.tipSourceOfTheChange}">
                  Source of the change: 
                  <c:choose>
                    <c:when test="${theItem.tipSourceOfTheChange == 'IF'}">
                      Vesting File
                    </c:when>
                    <c:when test="${theItem.tipSourceOfTheChange == 'PC'}">
                      Website
                    </c:when>
                    <c:otherwise>
                      ${theItem.tipSourceOfTheChange}
                    </c:otherwise>
                  </c:choose>
                </c:if>
              </span>
            </c:if>
          <c:if test="${empty param.printFriendly}">
          	<td align="center"> <report:actions profile="userProfile" item="theItem" action="editEmployeeVesting"/> </td>
          	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
          <td>
<c:if test="${userProfile.welcomePageAccessOnly == false}">
<c:if test="${theItem.participantInd ==true}">
          <ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
          <span class="printFriendlyLink">
			${theItem.wholeName}
			          </span>
				      </ps:link>
			</c:if>
			<c:if test="${theItem.participantInd ==false}">
			${theItem.wholeName}
			</c:if>
			</c:if>
			<c:if test="${userProfile.welcomePageAccessOnly == true}">
			${theItem.wholeName}
</c:if>
	      <br>
          <render:ssn property="theItem.ssn"/>
          </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" defaultValue="" property="theItem.vestingEffDate"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>
            <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="theItem" property="status"/>
            <c:if test="${not empty theItem.employeeStatusDate}">
              <br/>
              <fmt:formatDate value="${theItem.employeeStatusDate}" pattern="${renderConstants.MEDIUM_MDY_SLASHED}" />
            </c:if>
          </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                
          <c:if test="${showDivColumn eq true}">
<td>${theItem.division}</td>
            <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                
</c:if>
          
          <c:if test="${not empty theReport.moneyTypes}">
            <c:forEach var="moneyTypeHeader" items="${theReport.moneyTypes}"
              varStatus="moneyTypeHeaderStatus">
              <td align="right" nowrap="nowrap" width="55"
                <c:if test="${((!(theItem.percentages[moneyTypeHeader.id].moneyTypeFullyVested)) && (not empty theItem.percentages[moneyTypeHeader.id].percentage))}">
                        ${rowMouseOverAttributes}
                </c:if>
              >
                <c:choose>
                  <c:when test="${censusVestingReportForm.isEditMode && !(theItem.percentages[moneyTypeHeader.id].moneyTypeFullyVested)}">
<form:input path="censusVestingReportDataUi.rows[${indexValue3}].moneyTypeValues[${moneyTypeHeaderStatus.index}].value" maxlength="7" onchange="handleVestingPercentageChange(this, ${(empty theItem.percentages[moneyTypeHeader.id].percentage) ? 'false':'true'});" size="3" id="percent_r_${indexValue3}_mt_${moneyTypeHeaderStatus.index}"/>

                  </c:when>
                  <c:otherwise>
                    <c:choose>
                      <c:when test="${not empty theItem.percentages[moneyTypeHeader.id].percentage}">
                        <fmt:formatNumber maxFractionDigits="3" pattern="##0.###" value="${theItem.percentages[moneyTypeHeader.id].percentage}" />%
                      </c:when>
                      <c:otherwise>
                        &nbsp;
                      </c:otherwise>
                    </c:choose>
                  </c:otherwise>
                </c:choose>
              </td>
              <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
            </c:forEach>
          </c:if>
<%--
          <c:if test="${not empty theReport.moneyTypes}">
          <c:forEach items="${theReport.moneyTypes}" id="moneyTypeHeader">
			<td align="right" nowrap="nowrap" width="55">
				<%	
					MoneyTypeVestingPercentage mtvp = (MoneyTypeVestingPercentage)theItem.getPercentages().get(moneyTypeHeader.getId());
					java.math.BigDecimal amount = null;
					if (mtvp != null) { amount = (java.math.BigDecimal)mtvp.getPercentage(); }
					if (amount!=null && String.valueOf(amount).trim().length() > 0)
						pageContext.setAttribute("amount", NumberRender.formatByPattern(amount, "", "##0.###", 3, BigDecimal.ROUND_HALF_DOWN), PageContext.PAGE_SCOPE);
					else
						pageContext.removeAttribute("amount", PageContext.PAGE_SCOPE);
				%>
				<c:if test="${not empty amount}">
					${amount} %
				</c:if>
				<c:if test="${empty amount}">
					&nbsp;
				</c:if>
			</td>
			<td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
		  </c:forEach>
		  </c:if>
          <c:if test="${empty theReport.moneyTypes}">
            <td>&nbsp;</td>
            <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
--%>
<c:if test="${empty theReport.moneyTypes}">
		  	<td>&nbsp;</td>
		  	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>

<c:set var="hasWarnings" value="${theItem.getWarnings().hasWarnings()}"/>
          <c:if test="${hasWarnings eq true}">
            <c:if test="${not empty theItem.censusErrors}">
<c:forEach items="${theItem.censusErrors}" var="errorCode" >


<% VestingMessageType errorCode = (VestingMessageType)pageContext.getAttribute("errorCode");
if (errorCode.equals(VestingMessageType.PREVIOUS_YEARS_OF_SERVICE_AND_PLAN_YTD_HOURS_WORKED_NOT_PROVIDED)) {  %>  
          		<% theItem.getWarnings().setWarningDescription(pageContext.getAttribute("missingVYOSAndHOSWarning").toString()); %>
          		<% } %>
          		<% if (errorCode.equals(VestingMessageType.HIRE_DATE_NOT_PROVIDED)) {  %>     
          		<% theItem.getWarnings().setWarningDescription(pageContext.getAttribute("missingDateOfHireWarning").toString()); %>
          	    <% } %>
          		<% if (errorCode.equals(VestingMessageType.EMPLOYMENT_STATUS_NOT_PROVIDED)) {  %>
          		<% theItem.getWarnings().setWarningDescription(pageContext.getAttribute("missingEmploymentStatusWarning").toString()); %>
          		<% } %>

</c:forEach>
            </c:if>


            <c:if test="${not empty theItem.employeeErrors}">
<%--
            err...
            <% String warningMessage = "x"; %>
            <c:forEach items="theItem.employeeErrors" var="employeeError" >
            
            EmployeeValidationErrorCode.FullyVestedEffectiveDateBirthDate
--%>
<c:forEach items="${theItem.employeeErrors}" var="employeeError" >
<%EmployeeValidationError employeeError = (EmployeeValidationError)pageContext.getAttribute("employeeError");%>
                <% if (employeeError.getErrorCode().equals(EmployeeValidationErrorCode.FullyVestedEffectiveDateBirthDate)) {  %>
                <% theItem.getWarnings().setWarningDescription(pageContext.getAttribute("fullyVestedEffectiveAndBirthDateWarning").toString()); %>
                <% } %>   												
                <% if (employeeError.getErrorCode().equals(EmployeeValidationErrorCode.PreviousYearsOfServiceEffectiveDateBirthDate)) {  %>
                <% theItem.getWarnings().setWarningDescription(pageContext.getAttribute("vyosDateBirthDateWarning").toString()); %>
                <% } %>
                <% if (employeeError.getErrorCode().equals(EmployeeValidationErrorCode.MinPreviousYearsOfServiceEffectiveDate)) {  %>
                <% theItem.getWarnings().setWarningDescription(pageContext.getAttribute("minimumVyosDateWarning").toString()); %>
                <% } %>
 
</c:forEach>
<%--            
            <c:set var="warningMessage"  value="${employeeError}" />
                <% theItem.getWarnings().setWarningDescription(warningMessage); %>

            </c:forEach>
--%>
            </c:if>
            <c:if test="${theItem.participantStatusPartial}">
              <% theItem.getWarnings().setWarningDescription(pageContext.getAttribute("participantPartialStatusMessage").toString()); %>
            </c:if>
  	        <td align="center" title="<%=theItem.getWarnings().getWarningDescription()%>">
              <img src="/assets/unmanaged/images/warning2.gif" width="12" height="12" border="0">
            </td>          	
</c:if>
          <c:if test="${hasWarnings eq false}">
            <%-- No warning --%>		
            <td>&nbsp;</td>
</c:if>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
       	  </tr>
</c:forEach>
  		</c:if>    

		<!-- End of Last line -->

        
        </table>
        </div>	
        
        <table width="715" border="0" cellspacing="0" cellpadding="0">
        <tr>       
            <td width="22%"></td>
    		<% if (errors==false) { %>
        		<td align="left" width="15%"><b>&nbsp;<report:recordCounter report="theReport" label="Employees"/></b></td>
        		<td align="right" width="10%"><report:pageCounter report="theReport" arrowColor="black" formName="censusVestingReportForm"/>&nbsp;</td>
    		<% } else { %>
          		<td colspan="2"></td>
            <% } %>
		</tr>

		<c:if test="${censusVestingReportForm.displayEditButton or censusVestingReportForm.displaySaveButton or censusVestingReportForm.displayCancelButton}">
          <tr>
            <td colspan="3">
              <br>

              <table border="0" cellpadding="0" cellspacing="0" width="710">
                <tbody>
                  <tr valign="top">
                    <td width="322">
                      <div align="left">
                      </div>
                    </td>
                    <td width="178" align="right">
                      <c:if test="${censusVestingReportForm.displayCancelButton}">
<input type="submit" name="taskButton"  class="button134" onclick="return handleCancelClicked();" value="cancel" /> 
                      </c:if>
                    </td>
                    <td width="144" align="right">
                      <div align="right">
                        <c:if test="${censusVestingReportForm.displaySaveButton}">
<input type="submit"  id="saveButtonId" class="button134" onclick="return handleSaveClicked();" name="taskButton" disabled="true" value="save" /> 
                        </c:if>
                         <c:if test="${censusVestingReportForm.displayEditButton}">
<input type="submit" class="button134" onclick="return handleEditClicked();" name="taskButton" value="edit" /> 
                        </c:if>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </c:if>
		<tr>
          <td>
            <br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
 			<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
 			<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
          </td>
        </tr>
      </table>
        </table> 
    </ps:form>
</table>

  <c:if test="${not empty param.printFriendly}">
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
  </c:if>




