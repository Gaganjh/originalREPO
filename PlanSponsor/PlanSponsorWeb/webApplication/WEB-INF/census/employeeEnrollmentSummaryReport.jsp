<%-- taglib used --%>
 
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%-- Imports --%>
<%@ page import="java.util.List" %>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.EmployeeSummaryDetails" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusUtils" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.ps.service.report.census.valueobject.EmployeeEnrollmentSummaryReportData"%>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>

<style type="text/css">
DIV.scroll {
	OVERFLOW: auto;
	WIDTH: auto;
	BORDER-TOP-STYLE: none;
	BORDER-RIGHT-STYLE: none;
	BORDER-LEFT-STYLE: none;
	HEIGHT: 115px;
	BACKGROUND-COLOR: #fff;
	BORDER-BOTTOM-STYLE: none;
	padding: 8px;
	visibility: visible;
}
</style>

<%
EmployeeEnrollmentSummaryReportData theReport = (EmployeeEnrollmentSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
List pedYears = (List)session.getAttribute(CensusConstants.PED_YEAR_LIST);
pageContext.setAttribute("pedYears",pedYears,PageContext.PAGE_SCOPE);
List pedMonthDays = (List)session.getAttribute(CensusConstants.PED_MONTH_DAY_LIST);
pageContext.setAttribute("pedMonthDays",pedYears,PageContext.PAGE_SCOPE);
%>


<%-- <jsp:useBean id="employeeEnrollmentSummaryReportForm" scope="session" type="com.manulife.pension.ps.web.census.EmployeeEnrollmentSummaryReportForm" /> --%>

<%
EmployeeEnrollmentSummaryReportForm employeeEnrollmentSummaryReportForm = (EmployeeEnrollmentSummaryReportForm)session.getAttribute("employeeEnrollmentSummaryReportForm");
pageContext.setAttribute("employeeEnrollmentSummaryReportForm",employeeEnrollmentSummaryReportForm,PageContext.PAGE_SCOPE);
%>
<c:set var="moneyTypesList" value="${employeeEnrollmentSummaryReportForm.moneyTypes}" scope="page"/>




                                   
<content:contentBean contentId="<%=ContentConstants.NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="NoSearchResults"/>                                                                                          

<content:contentBean contentId="<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>"
                    id="warningDiscardChanges"/>

<content:contentBean contentId="<%=ContentConstants.BLANK_ELIGIBILITY_WARNING%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>"
                    id="blankEligibilityWarning"/>

<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_DATE_INDICATOR_WARNING%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>"
                    id="eligibilityDateIndWarning"/>

<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_MOUSE_OVER_ALERT_ICON%>"
    				type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
    				id="eligibilityMouseOverAlertIcon"/>

<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_MOUSE_OVER_INFO_ICON%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="eligibilityMouseOverInfoIcon"/>

<content:contentBean contentId="<%=ContentConstants.WARNING_MISSING_BIRTHDATE_DM%>"
		    type="<%=ContentConstants.TYPE_MESSAGE%>"
		    id="missingBirthDateWarning"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_MISSING_EMPLOYEE_ADDRESS%>"
		    type="<%=ContentConstants.TYPE_MESSAGE%>"
		    id="missingEmployeeAddressWarning"/>
<content:contentBean contentId="<%=ContentConstants.WARNING_MISSING_BIRTHDATE_AND_EMPLOYEE_ADDRESS%>"
	    type="<%=ContentConstants.TYPE_MESSAGE%>"
		id="missingBirthDateAndEmployeeAddressWarning"/>

<content:contentBean contentId="<%=ContentConstants.NEXT_PLAN_ENTRY_DATE_LABEL%>"
	    type="<%=ContentConstants.TYPE_MESSAGE%>"
		id="nextPlanEntryDatesLabel"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_ELIGIBILITY_CALCULATION_PENDING%>"
	    type="<%=ContentConstants.TYPE_MESSAGE%>"
		id="eligibilityCalculationPending"/>


<%
	boolean showPayrollColumn =  employeeEnrollmentSummaryReportForm.getHasPayrollNumberFeature();
	boolean showDivisionColumn = employeeEnrollmentSummaryReportForm.getHasDivisionFeature();
	boolean showDM = employeeEnrollmentSummaryReportForm.isShowDM();
	boolean showAE = employeeEnrollmentSummaryReportForm.isEZstartOn();
	
	int numOfDisplayColumns = employeeEnrollmentSummaryReportForm.getNumberOfDisplayColumns();
	String  numberOfDisplayColumns = String.valueOf(numOfDisplayColumns);
	int numOfDisplayColumnsLessTwo = numOfDisplayColumns - 2;
	String  numberOfDisplayColumnsLessTwo = String.valueOf(numOfDisplayColumnsLessTwo);

	String style_scrolling="overflow:scroll; overflow-y:hidden; z-index:2; width:700px;border-right: 1px outset #ffffff"; 
	String style_fixed="overflow:hidden; z-index:3; visibility:hidden; position:absolute; width:163px;";
	boolean isMozilla = request.getHeader("User-Agent").toUpperCase().indexOf("GECKO") != -1;

	int rowspan = 3;
	int moneytypewidth = 0;
	int moneyTypeLength = 0;
	if(theReport != null && theReport.getMoneyTypes()!= null && theReport.getMoneyTypes().size() > 0){
		moneytypewidth = (theReport.getMoneyTypes().size())*2 -1;
		moneyTypeLength = theReport.getMoneyTypes().size();
	}
	
	boolean extendTable = false;
	int tableWidth = 700;
	
	// no other features, extend table if > 2 money types
	if (!showPayrollColumn && !showDivisionColumn && !showDM && !showAE && moneyTypeLength > 2) {
		extendTable = true;

	// no other features except for AE, extend table if > 1 money types
	} else if (!showPayrollColumn && !showDivisionColumn && !showDM && showAE && moneyTypeLength > 1) {
		extendTable = true;

	// Direct Mail or both payroll and division
	} else if (showDM || (showPayrollColumn && showDivisionColumn) ) {
		extendTable = true;
		
	// either payroll or division, no DM and money type > 1
	} else if ((showPayrollColumn || showDivisionColumn) && !showDM) {
		extendTable = true;
	} 
	
	if (extendTable) {
		tableWidth = 805;
		style_scrolling="overflow:scroll; overflow-y:hidden; z-index:2; width:805px;border-right: 1px outset #ffffff"; 
	}
	
	int moneyTypesCount = 0;
	if (theReport != null && theReport.getMoneyTypes() != null && theReport.getMoneyTypes().size() > 0) {
		moneyTypesCount = theReport.getMoneyTypes().size();
	}
%>

<c:if test="${not empty param.printFriendly}">
<% style_scrolling="overflow:hidden;"; %>
</c:if>

<c:if test="${empty param.printFriendly}">

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
  <c:if test="${empty param.printFriendly }" >
	<% if (extendTable) { %>
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
  	// fixedTable.style.left = 31;
  	fixedTable.style.visibility = 'visible';
	<% } %>
  </c:if>
</c:if>
}


  
  // Called when Cancel is clicked
  function doCancel() { 
    // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
      document.employeeEnrollmentSummaryReportForm.task.value = "cancel";
      if (document.forms['employeeEnrollmentSummaryReportForm']) {
        document.forms['employeeEnrollmentSummaryReportForm'].elements['isSearch'].value = "true"; 
      }  
      
      if (document.forms['employeeEnrollmentSummaryReportForm']) {              
        document.forms['employeeEnrollmentSummaryReportForm'].submit();
      } else {
        document.forms.employeeEnrollmentSummaryReportForm.submit();
      }
      
      return true;
    }
  }
  
  
   function doCalculate() { 
   
	   if (document.forms['employeeEnrollmentSummaryReportForm']) {
		document.getElementById("calculateButton").disabled=true;       
		document.employeeEnrollmentSummaryReportForm.task.value = "calculate";
		document.employeeEnrollmentSummaryReportForm.fromCalculateButton.value = "true";
	    document.forms['employeeEnrollmentSummaryReportForm'].submit();
	    document.forms['employeeEnrollmentSummaryReportForm'].disabled=false;
	   }
  }
  
  
  // Called when reset is clicked
  function doReset() {    
     // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
      document.employeeEnrollmentSummaryReportForm.task.value = "reset";
      if (document.forms['employeeEnrollmentSummaryReportForm']) {
        document.forms['employeeEnrollmentSummaryReportForm'].elements['isSearch'].value = "true"; 
      }  
      
      if (document.forms['employeeEnrollmentSummaryReportForm']) {              
        document.forms['employeeEnrollmentSummaryReportForm'].submit();
      } else {
        document.forms.employeeEnrollmentSummaryReportForm.submit();
      }
      
      return true;
    }
  }
  
  // Called when Search is clicked
  function doSubmit(){    
    // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName='warningDiscardChanges' attribute='text' filter='true'/>") == false) {      
      return false;
    }
    
    if (document.forms['employeeEnrollmentSummaryReportForm']) {
      document.forms['employeeEnrollmentSummaryReportForm'].elements['isSearch'].value = "true"; 
    }  
    
    if (document.forms['employeeEnrollmentSummaryReportForm']) {              
      document.forms['employeeEnrollmentSummaryReportForm'].submit();
    } else {
      document.forms.employeeEnrollmentSummaryReportForm.submit();
    }
  
  }
  // Called when add employee is clicked
  function doAdd() {
     // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
      window.location="/do/census/addEmployee/?source=eligibilitySummary";
      return true;
    }
  }
 

  // Protects all the links with checks for changes in the page with the exception of pop-ups
  function protectLinks() {
    var hrefs  = document.links;
    if (hrefs != null)
    {
      for (i=0; i<hrefs.length; i++) {
        if(
          hrefs[i].onclick != undefined && 
          (hrefs[i].onclick.toString().indexOf("openWin") != -1 || hrefs[i].onclick.toString().indexOf("popup") != -1 || hrefs[i].onclick.toString().indexOf("doSignOut") != -1)
        ) {
          // don't replace window open or popups as they won't loose there changes with those
        }
        else if(
          hrefs[i].href != undefined && 
          (hrefs[i].href.indexOf("openWin") != -1 || hrefs[i].href.indexOf("popup") != -1 || hrefs[i].href.indexOf("doSignOut") != -1)
        ) {
          // don't replace window open or popups as they won't loose there changes with those
        }
        else if(hrefs[i].onclick != undefined) {
          hrefs[i].onclick = new Function ("var result = discardChanges('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');" + "var childFunction = " + hrefs[i].onclick + "; if(result) result = childFunction(); return result;");
        }
        else{
          hrefs[i].onclick = new Function ("return discardChanges('Warning! The action you have selected will cause your changes to be lost. Select OK to continue or Cancel to return.');");
        }
      }
    }
    
   }  
   
   // Track changes framework
   function isFormChanged() {
     return changeTracker.hasChanged();
   }
    
   // Registration required by Track changes framework
   registerTrackChangesFunction(isFormChanged);
   
   // Registration required to be notified on leaving the page
   if (window.addEventListener) {
     window.addEventListener('load', protectLinks, false);
   } else if (window.attachEvent) {
     window.attachEvent('onload', protectLinks);
   }
   
   function validateMonthDayYearDate(field) {
		if (field != null && field.value != "") {
			var parsedDate=cal_prs_date1(field.value);
			if (parsedDate != null) {
				field.value=formatDate(parsedDate, true);
			}else{
				field.value="";
			}
		}
	}

   function formatDate(dateVal, includeYear) {
		var month = dateVal.getMonth() + 1;
		var day = dateVal.getDate();
		var dateStr = (month<10 ? "0" + month : month) + "/" + (day<10 ? "0" + day : day) + (includeYear ? "/" + dateVal.getFullYear() : "");
		return dateStr;
	}
</script>
</c:if>


<!-- Remove the extra column at the before the report -->
<c:if test="${empty param.printFriendly}">
  <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
  <td >
</c:if>

<c:if test="${not empty param.printFriendly}">
  <td>
</c:if>
<c:if test="${empty param.printFriendly}">
  <p>
  <c:if test="${not empty sessionScope.psErrors}">
<c:set var="errorsExist" value="${true}" scope="page" />
    <div id="errordivcs"><content:errors scope="session"/></div>
    </c:if>
  </p>
</c:if>

<%-- Start of the search table --%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <ps:form method="POST" modelAttribute="employeeEnrollmentSummaryReportForm" name="employeeEnrollmentSummaryReportForm" action="/do/census/employeeEnrollmentSummary" >
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="isSearch"/>
<input type="hidden" name="isInitialSearch" value="false"/>
<input type="hidden" name="currentDate" /><%--  input - name="employeeEnrollmentSummaryReportForm" --%>
 <input type="hidden" name="fromCalculateButton" /> <%--  input - name="employeeEnrollmentSummaryReportForm" --%>
 
  
  
  
  <%-- TAB section --%>
  <table width="<%=tableWidth%>" border="0" cellspacing="0" cellpadding="0">
  <tr>
      <td valign="bottom" colspan="<%=numberOfDisplayColumns%>">
        <DIV class="nav_Main_display" id="div">
          <UL class="">
          <c:if test="${not empty param.printFriendly}">           
            <LI id="tab1" class="off_over" >Summary</LI>
            <LI id="tab2" class="off_over" >Addresses</LI>
            <LI id="tab3" class="on">Eligibility</LI>
<c:if test="${employeeEnrollmentSummaryReportForm.allowedToAccessDeferralTab ==true}">
              <LI id="tab4" class="off_over">Deferrals</LI>
</c:if>

<c:if test="${employeeEnrollmentSummaryReportForm.allowedToAccessVestingTab ==true}">
            <LI id="tab5" class="off_over" >Vesting</LI>
</c:if>


          </c:if>
          <c:if test="${empty param.printFriendly}">
            <LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                <A href="/do/census/censusSummary/">Summary</A>
            </LI>
            <LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                <A href="/do/participant/participantAddresses">Addresses</A>
            </LI>
            <LI id="tab3" class="on">Eligibility</LI>
<c:if test="${employeeEnrollmentSummaryReportForm.allowedToAccessDeferralTab ==true}">
              <LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                <A href="/do/census/deferral">Deferrals</A>
              </LI>
</c:if>
<c:if test="${employeeEnrollmentSummaryReportForm.allowedToAccessVestingTab ==true}">
        	<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                <A href="/do/census/censusVesting/">Vesting</A>
            </LI>	
</c:if>


          </c:if>
          </UL>
        </DIV>
      </td>
  </tr>
  <tr>
    <td colspan="<%=numberOfDisplayColumns%>" height="25" class="tablesubhead"><b>Employee Eligibility Search</b></td>
  </tr>
    
    <tr>
      <td colspan="<%=numberOfDisplayColumns%>" >
        <table width="100%" border="0" cellspacing="0" cellpadding="2">
          <tbody class="datacell2" >
            <tr>
              <td colspan="4" valign="top" class="filterSearch">
                <p>To select a new summary date or participant status or to search for a participant by last name or SSN, make your selection below <br>
                  and click "search" to complete your request.</p>
              </td>              
            </tr>            
            <tr>
              <td width="33%" valign="bottom" class="filterSearch">
              	<table>
              	  <tr><td><b>Segment</b></td></tr>
              	  <tr><td>
                <c:if test="${not empty param.printFriendly}">
<form:select path="segment" disabled="true" >
<form:options items="${employeeEnrollmentSummaryReportForm.segmentList}" itemValue="value" itemLabel="label"/>
</form:select>
                </c:if>
                <c:if test="${empty param.printFriendly}">
                  <form:select path="segment"  tabindex="1">
<form:options items="${employeeEnrollmentSummaryReportForm.segmentList}" itemValue="value" itemLabel="label"/>
                  </form:select>
                </c:if>
                </td></tr></table>
              </td>
              <td width="22%" valign="bottom" class="filterSearch">
                <table>
                  	<tr><td><strong><span id="label_lastName"><b>Last name</b></span></strong></td></tr>
                  	<tr><td>
                <c:if test="${not empty param.printFriendly}">
<form:input path="namePhrase" maxlength="20" readonly="true" cssStyle="inputField"/>
                </c:if>
                <c:if test="${empty param.printFriendly}">
<form:input path="namePhrase" maxlength="20" onkeyup="clearSSNAndPED(event);" cssStyle="inputField" tabindex="2"/>
                </c:if>
                	</td></tr></table>
              </td>
              <td width="23%" valign="bottom" class="filterSearch">
              	<table>
              		<tr><td><strong><span id="label_ssn"><b>SSN</b></span></strong></td></tr>
              		<tr><td>
                <c:if test="${not empty param.printFriendly}">
                <form:password  path="ssnOne" value="${employeeEnrollmentSummaryReportForm.ssnOne}"   style="inputField" readonly="true" size="3" maxlength="3"/>
				<form:password  path="ssnTwo" value="${employeeEnrollmentSummaryReportForm.ssnTwo}" style="inputField" readonly="true" size="2" maxlength="2"/>
				<form:input  path="ssnThree" autocomplete="off" style="inputField" readonly="true" size="4" maxlength="4"/>
                  </c:if>
                <c:if test="${empty param.printFriendly}">
                <form:password  path="ssnOne"  value="${employeeEnrollmentSummaryReportForm.ssnOne}" style="inputField" onkeypress = "clearNameAndPED(event);" onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" tabindex="3"/>
				<form:password  path="ssnTwo" value="${employeeEnrollmentSummaryReportForm.ssnTwo}" style="inputField" onkeyup="return autoTab(this, 2, event);" onkeypress = "clearNameAndPED(event);" size="2" maxlength="2" tabindex="4"/>
				<form:input  path="ssnThree"  autocomplete="off" style="inputField"   onkeypress = "clearNameAndPED(event);" onkeyup="return autoTab(this, 4, event);" size="4" maxlength="4" tabindex="5"/>
                </c:if>
                	</td></tr></table>
              </td>
              <td width="160" valign="bottom" class="filterSearch">
                <% if(showDivisionColumn) { %>
                  <table><tr><td><strong>Division</strong></td></tr>
                  <tr><td>
                  <c:if test="${not empty param.printFriendly}">    
<form:input path="division" readonly="true" cssStyle="inputField"/>
                  </c:if> 
                  <c:if test="${empty param.printFriendly}">
<form:input path="division" cssStyle="inputField" tabindex="6"/>
                  </c:if>
                  </td></tr></table>
                  <% } %>
              </td>
            </tr>
            <tr>
              <td width="34%" valign="top" class="filterSearch">
              
              	<table>
              	  <tr><td>
                  <strong>Enrollment status </strong></td>
                  </tr>
                  <tr><td>
                  <c:if test="${not empty param.printFriendly}">
                    <form:select path="status" disabled="true" >                    
                      <form:option value="All">All</form:option>
<% if (employeeEnrollmentSummaryReportForm.isEZstartOn()) {  %>                                          
                      <form:option value="Pending Enrollment">Pending enrollment</form:option>
                      <form:option value="Pending Eligibility">Pending eligibility</form:option>
<% } %>                      
                      <form:option value="Not eligible">Not eligible</form:option>
                      <form:option value="No Account">No account</form:option>
                      <form:option value="Enrolled">Enrolled</form:option>                    
</form:select>
                  </c:if>
                  <c:if test="${empty param.printFriendly}">  
                    <form:select path="status" tabindex="7" >                   
                      <form:option value="All">All</form:option>
<% if (employeeEnrollmentSummaryReportForm.isEZstartOn()) {  %>                                          
                      <form:option value="Pending Enrollment">Pending enrollment</form:option>
                      <form:option value="Pending Eligibility">Pending eligibility</form:option>
<% } %>                      
                      <form:option value="Not eligible">Not eligible</form:option>
                      <form:option value="No Account">No account</form:option>
                      <form:option value="Enrolled">Enrolled</form:option>                    
</form:select>
                  </c:if>
                </td>
                </tr></table>
              </td>

              <td width="22%" valign="top" class="filterSearch">
                <table width="100%" border="0">
                  <tr><td colspan="2"><strong><nobr>Enrollment processing date</nobr></strong></td></tr>
                  <tr><td width="15%"><strong>from:</strong></td>
                      <td width="85%">                  
	                    <c:if test="${not empty param.printFriendly}">
<form:input path="enrolledFrom" maxlength="10" readonly="true" size="10" />
	                    </c:if>                
	                    <c:if test="${empty param.printFriendly}">
<form:input path="enrolledFrom" maxlength="10" onchange="validateMonthDayYearDate(this)" onkeydown="clearSSNAndNameAndPED(event);" size="10" tabindex="8" />
	  					  <a href="javascript:calFromDate.popup();clearSSNAndNameAndPED(null);" tabindex="9"  ><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"  ></a>                
	                    </c:if>
	                   </td>
	              </tr>
                  <tr><td></td><td>(mm/dd/yyyy)</td></tr>
                 </table>
			  </td>
			  
			  <td width="22%" valign="top" class="filterSearch">
			    <table>
			      <tr><td colspan="2">&nbsp;</td></tr>
			      <tr><td><strong>to:</strong></td>
			          <td>
		                <c:if test="${not empty param.printFriendly}">
<form:input path="enrolledTo" maxlength="10" readonly="true" size="10" />
		                </c:if>                
		                <c:if test="${empty param.printFriendly }">
<form:input path="enrolledTo" maxlength="10" onchange="validateMonthDayYearDate(this)" onkeydown="clearSSNAndNameAndPED(event);" size="10" tabindex="10" />
			    			<a href="javascript:calToDate.popup();clearSSNAndNameAndPED(null);" tabindex="11"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>                
		                </c:if>
		              </td>
		          </tr>
		          <tr><td></td><td>(mm/dd/yyyy)</td></tr>
		        </table>			  
			  </td>
                
                <td width="22%" valign="top" class="filterSearch">                
                 <table>
                  <tr><td>
                  <strong>Employment status</strong></td>
                  </tr>
                  <tr><td>
                  <c:if test="${not empty param.printFriendly}">
                    <form:select path="empStatus" disabled="true" >                    
                      <form:option value="">All</form:option>                    
                      <form:option value="A">Active</form:option>
                      <form:option value="T">Terminated</form:option>
                      <form:option value="R">Retired</form:option>
                      <form:option value="D">Deceased</form:option>
                      <form:option value="P">Disabled</form:option>        
<% if (userProfile.isInternalUser()) { %>                                   
                      <form:option value="C">Cancelled</form:option>                                          
<% } %>                      
</form:select>
                  </c:if>
                  <c:if test="${empty param.printFriendly}">  
                    <form:select path="empStatus" tabindex="12">                   
                      <form:option value="">All</form:option>                    
                      <form:option value="A">Active</form:option>
                      <form:option value="T">Terminated</form:option>
                      <form:option value="R">Retired</form:option>
                      <form:option value="D">Deceased</form:option>
                      <form:option value="P">Disabled</form:option>                    
<% if (userProfile.isInternalUser()) { %>                                   
                      <form:option value="C">Cancelled</form:option>                                          
<% } %>                      
</form:select>
                  </c:if>
                  </td>
                  </tr></table>
                </td>
            </tr>
<% if (employeeEnrollmentSummaryReportForm.isEZstartOn()) {  %>                                         
            <tr>
              <td  class="filterSearch">
                <strong>Please note:</strong>
                <br><content:getAttribute id="nextPlanEntryDatesLabel" attribute="text">
<content:param>${employeeEnrollmentSummaryReportForm.eedefShortName}</content:param>
                </content:getAttribute> 
                <c:set var="nextPED" value="${employeeEnrollmentSummaryReportForm.nextPED}" scope="page"/>  
                <render:date property="nextPED" patternOut="MM/dd/yyyy"/>
                <br>Your opt out date is 
                <c:set var="nextOptOut" value="${employeeEnrollmentSummaryReportForm.nextOptOut}" scope="page"/>
                <render:date property="nextOptOut" patternOut="MM/dd/yyyy"/>
              </td>
              <td width="22%" valign="top" class="filterSearch">
                <table width="100%" border="0">
                  <tr><td colspan="2"><strong>Plan entry date</strong></td></tr>
                  <tr><td width="15%"><strong>from:</strong></td>
                      <td width="85%">                  
	                    <c:if test="${not empty param.printFriendly}">
<form:input path="fromPED" maxlength="10" readonly="true" size="10" />
	                    </c:if>                
	                    <c:if test="${empty param.printFriendly}">
<form:input path="fromPED" maxlength="10" onchange="validateMonthDayYearDate(this)" onkeydown="clearSSNAndNameAndEPD(event);" size="10" tabindex="13" />
	  					  <a href="javascript:calFromDate1.popup();clearSSNAndNameAndEPD(null);" tabindex="14"><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>                
	                    </c:if>
	                   </td>
	              </tr>
                  <tr><td></td><td>(mm/dd/yyyy)</td></tr>
                 </table>
			  </td>
			  
			  <td width="22%" valign="top" class="filterSearch">
			    <table>
			      <tr><td colspan="2">&nbsp;</td></tr>
			      <tr><td><strong>to:</strong></td>
			          <td>
		                <c:if test="${not empty param.printFriendly}">
<form:input path="toPED" maxlength="10" readonly="true" size="10" />
		            		<% if(employeeEnrollmentSummaryReportForm.isEligibiltyCalcOn()){ %>    
		            		&nbsp;for
		            		<%} %>         
		                </c:if>                
		                <c:if test="${empty param.printFriendly}">
<form:input path="toPED" maxlength="10" onchange="validateMonthDayYearDate(this)" onkeydown="clearSSNAndNameAndEPD(event);" size="10" tabindex="15" />
			    			<a href="javascript:calToDate1.popup();clearSSNAndNameAndEPD(null);" tabindex="16" ><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
			    			<% if(employeeEnrollmentSummaryReportForm.isEligibiltyCalcOn()){ %>    
		            		&nbsp;for
		            		<%} %>                 
		                </c:if>
		              </td>
		          </tr>
		          <tr><td></td><td>(mm/dd/yyyy)</td></tr>
		        </table>			  
			  </td>
			 <% if(employeeEnrollmentSummaryReportForm.isEligibiltyCalcOn()){ %>
			 	<td class="filterSearch" valign="top"> 
			  		<table>
                  	<tr><td colspan="2"><strong>Money type</strong></td></tr>
                 	 <tr>
                  		<td>
                  		<c:if test="${not empty param.printFriendly}">
<form:select path="moneyTypeSelected" disabled="true" >
                  		  <form:option value="All">All</form:option>
                  		  <form:options items="${moneyTypesList}" itemValue="value" itemLabel="label" />
</form:select>
                  		</c:if>
                  		<c:if test="${empty param.printFriendly}">
<form:select path="moneyTypeSelected" tabindex="17" >
                  		  <form:option value="All">All</form:option>
                  		  <form:options items="${moneyTypesList}" itemValue="value" itemLabel="label" />
</form:select>
                  		</c:if>
                  		</td>
                  	</tr>
                 	</table>
			 	 </td>
			 	 
			 <% }else { %>
			 <td colspan="2" class="filterSearch">
              </td>
			 <% } %>
			  <tr>
			  <td colspan="5" valign="top" class="filterSearch">
                <c:if test="${empty param.printFriendly}">
                    <a href="javascript:openStatisticsWindow()">View enrollment stats </a>
                </c:if>
              </td>
              </tr>
              <tr>
              <td class="filterSearch" colspan="1">
               		<%-- Calculation status Message --%>
<c:if test="${employeeEnrollmentSummaryReportForm.pendingEligibilityCalculationRequest ==true}">

				  <content:getAttribute id="eligibilityCalculationPending" attribute="text"/>
</c:if>
              </td>
              <td width="150" valign="bottom" class="filterSearch" colspan="2">
                <c:if test="${empty param.printFriendly}">
<c:if test="${employeeEnrollmentSummaryReportForm.showCalculateButton ==true}">
                  		<input id="calculateButton" type="button" name="Submit" value="calculate" onclick="doCalculate();"  tabindex="18"> 
</c:if>
                </c:if>
              </td>
              <td width="150" valign="bottom" class="filterSearch">
                <c:if test="${empty param.printFriendly}">
<input type="submit" onclick="return doSubmit();" tabindex="19" value="search"/>
                  &nbsp;&nbsp;&nbsp;
<input type="submit" onclick="return doReset();" tabindex="20" value="reset"/>
                </c:if>  
              </td>  
                          
            </tr>
            
<% } else if(employeeEnrollmentSummaryReportForm.isEligibiltyCalcOn()) { %>            
            <tr>
              <td colspan="1" class="filterSearch">
              </td>
              <td width="22%" valign="top" class="filterSearch">
                <table>
                  <tr><td colspan="2"><strong>Plan entry date</strong></td></tr>
                  <tr><td width="15%"><strong>from:</strong></td>
                      <td width+"85%">                  
	                    <c:if test="${not empty param.printFriendly}">
<form:input path="fromPED" readonly="true" size="10" />
	                    </c:if>                
	                    <c:if test="${empty param.printFriendly}">
<form:input path="fromPED" onchange="validateMonthDayYearDate(this)" onkeydown="clearSSNAndNameAndEPD(event);" size="10" tabindex="13" />
	  					  <a href="javascript:calFromDate1.popup();clearSSNAndNameAndEPD(null);" tabindex="14" ><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>                
	                    </c:if>
	                   </td>
	              </tr>
                  <tr><td></td><td>(mm/dd/yyyy)</td></tr>
                 </table>
			  </td>
			  
			  <td width="22%" valign="top" class="filterSearch">
			    <table>
			      <tr><td colspan="2">&nbsp;</td></tr>
			      <tr><td><strong>to:</strong></td>
			          <td>
		                <c:if test="${not empty param.printFriendly}">
<form:input path="toPED" readonly="true" size="10" />
		            		&nbsp;for                
		                </c:if>                
		                <c:if test="${empty param.printFriendly}">
<form:input path="toPED" onchange="validateMonthDayYearDate(this)" onkeydown="clearSSNAndNameAndEPD(event);" size="10" tabindex="15" />
			    			<a href="javascript:calToDate1.popup();clearSSNAndNameAndEPD(null);" tabindex="16"  ><img src="/assets/unmanaged/images/cal.gif" width="16" height="16" border="0" alt="Use the Calendar to pick the date"></a>
			    			&nbsp;for                
		                </c:if>
		              </td>
		          </tr>
		          <tr><td></td><td>(mm/dd/yyyy)</td></tr>
		        </table>			  
			  </td>
			  <td class="filterSearch" valign="top"> 
			  	<table>
                  <tr><td colspan="2" ><strong>Money type</strong></td></tr>
                  <tr>
                  	<td >
<form:select path="moneyTypeSelected" tabindex="17" >
                  		<form:option value="All">All</form:option>
                  		<form:options items="${moneyTypesList}" itemValue="value" itemLabel="label" />
</form:select>
                  	</td>
                  </tr>
                 </table>
			  </td>
			  </tr>
			  
              <tr>
              <td class="filterSearch" colspan="1">
               		<%-- Calculation status Message --%>
<c:if test="${employeeEnrollmentSummaryReportForm.pendingEligibilityCalculationRequest ==true}">

				  <content:getAttribute id="eligibilityCalculationPending" attribute="text"/>
</c:if>
              </td>
              <td width="150" valign="bottom" class="filterSearch" colspan="2">
                <c:if test="${empty param.printFriendly}">
<c:if test="${employeeEnrollmentSummaryReportForm.showCalculateButton ==true}">
                  		<input  id="calculateButton" type="button" name="Submit" value="calculate" onclick="doCalculate()" tabindex="18">
</c:if>
                </c:if>
              </td>
              <td width="150" valign="bottom" class="filterSearch">
                <c:if test="${empty param.printFriendly}">
<input type="submit" onclick="return doSubmit();" tabindex="19" value="search"/>
                  &nbsp;&nbsp;&nbsp;
<input type="submit" onclick="return doReset();" tabindex="20" value="reset"/>
                </c:if>  
              </td>
              </tr>              
            </tr>
<% }else { %>
	 <tr>
              
              <td colspan="3" valign="top" class="filterSearch">
                <c:if test="${empty param.printFriendly}">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                </c:if>
              </td>
              <td width="150" valign="bottom" class="filterSearch">
                <c:if test="${empty param.printFriendly}">
<input type="submit" onclick="return doSubmit();" tabindex="18" value="search"/>
                  &nbsp;&nbsp;&nbsp;
<input type="submit" onclick="return doReset();" tabindex="19" value="reset"/>
                </c:if>  
              </td>              
            </tr>
<% } %>
          </tbody>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="<%=numberOfDisplayColumns%>"> 
        <jsp:include flush="true" page="employeeSnapshotErrors.jsp"/>       
      </td>
    </tr>
</table>

<c:if test="${empty param.printFriendly}"> 
  <table width="<%=tableWidth%>" border="0" cellspacing="0" cellpadding="0">

</c:if>
<c:if test="${not empty param.printFriendly}">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
</c:if> 
    <%-- Leave a space --%>
    <tr>
      <td colspan="<%=numberOfDisplayColumns%>" >&nbsp;</td>
    </tr>
    <%-- Legend --%>
    <c:if test="${empty param.printFriendly}"> 
      <tr>
        <td colspan="<%=numberOfDisplayColumns%>">
          <strong>Legend:&nbsp;</strong>
          <IMG height=12 src="/assets/unmanaged/images/view_icon.gif" width=12 border=0>&nbsp; View &nbsp;
          <IMG height=12 src="/assets/unmanaged/images/edit_icon.gif" width=12 border=0>&nbsp; Edit &nbsp;
          <IMG height=12 src="/assets/unmanaged/images/history_icon.gif" width=12 border=0>&nbsp; History &nbsp;
          <% if(employeeEnrollmentSummaryReportForm.isEligibiltyCalcOn() ){%>
          <IMG height=12 src="/assets/unmanaged/images/info_icon.gif" width=12 border=0>&nbsp; Eligibility Information  &nbsp;
          <% } %>
          
        </td>
      </tr>
    </c:if>

  
    <tr class="tablehead">
        	<td class="tableheadTD1" width="30%">&nbsp;</td>         
		   
		    <td class="tableheadTDinfo" align="center" width="50%"><b><report:recordCounter report="theReport" label="Employees" /></b></td>
		    <td align="right" class="tableheadTDinfo" width="20%"><report:pageCounter report="theReport" formName="employeeEnrollmentSummaryReportForm"/></td>
		    
		</tr>
     
      </table>


<!-- fixed table -->
<c:if test="${not empty theReport.details}">
	    <c:if test="${empty param.printFriendly}">
	    <% if (extendTable) { %>
	    
	    <div id="fixedTable" style="<%=style_fixed%>">
	    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${empty param.printFriendly}">
	          <td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="30"></td> <!-- action -->
	          <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	</c:if>
          	
          	<td width="130"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="130"></td> <!-- name -->
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr>
        <tr class="tablesubhead">
          <td class="databorder" width="1"></td>
          <c:if test="${empty param.printFriendly }" >
          	<td id="fixedHeaderElement" valign="bottom"><b>Action</b></td>
          	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
		  <td valign="bottom"><report:sort   formName="employeeEnrollmentSummaryReportForm" field="lastName" direction="asc"><b>Name</b></report:sort></td>
          <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		</tr>
		
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>

<%
EmployeeSummaryDetails theIndex = (EmployeeSummaryDetails)pageContext.getAttribute("theItem");

String temp = pageContext.getAttribute("indexValue").toString();
String summaryreport=employeeEnrollmentSummaryReportForm.getTheItem(Integer.parseInt(temp)).getProfileId();
%>
			
<input type="hidden" name="profileId" value="${summaryreport}"/><%-- indexed="true" name="theItem" --%>
<input type="hidden" name="beforeTaxDeferralAmt" value="${theItem.beforeTaxDeferralAmt}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="afterTaxDeferralPct" value="${theItem.afterTaxDeferralPct}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="afterTaxDeferralPct" value="${theItem.afterTaxDeferralPct}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="optOutEditable" value="${theItem.optOutEditable}" /><%--  input - indexed="true" name="theItem" --%>

           <% if (Integer.parseInt(temp) % 2 == 0) { %>
                                <tr class="datacell2">
                                <% } else { %> 
                                <tr class="datacell1">
                                 <% } %> 
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <c:if test="${empty param.printFriendly}" >
          
<c:if test="${theItem.showHistoryAndNameLink ==true}">
            		<td align="center">  <report:actions profile="userProfile" item="theItem" action="evhDeferral"/> </td>
            		<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
<c:if test="${theItem.showHistoryAndNameLink ==false}">
            		<td align="center"> <report:actions profile="userProfile" item="theItem" action="editEmployeeSummary"/> </td>
            		<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
          
          </c:if>
          <td>
<c:if test="${userProfile.welcomePageAccessOnly == false}" >
<c:if test="${theItem.showHistoryAndNameLink ==true}">
	          <ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
	          <span class="printFriendlyLink">
${theItem.fullName}
	          </span>
		      </ps:link>
</c:if>
<c:if test="${theItem.showHistoryAndNameLink ==false}">
${theItem.fullName}
</c:if>
</c:if>
<c:if test="${userProfile.welcomePageAccessOnly == true}">
${theItem.fullName}
</c:if>
		      <br>
	          <render:ssn property="theItem.ssn"/>
          </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
</c:forEach>
        
        </table>
	    </div>
	    <% } %>
	    </c:if>
</c:if>

<!-- scrolling table -->
      <div id="scrollingTable" style="<%=style_scrolling%>">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${empty param.printFriendly }" >
	          <td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="30"></td>
	          <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	</c:if>
          	
          	<td width="130"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="130"></td>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	
          	<%if(showPayrollColumn){%>
          	<td width="65"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="65"></td>
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<%} %>
          	<%if(showDivisionColumn){%>
          	  <td width="60"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="60"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	  <%} %>
          	<% if(!showPayrollColumn && !showDivisionColumn && !showDM ){ %>
          	  <td width="120"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="40"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>

          	<%}else if( ( (showPayrollColumn && !showDivisionColumn) || (!showPayrollColumn && showDivisionColumn)) && !showDM && moneyTypesCount == 0) { %>
          	  <td width="110"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="110"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>

          	<%}else { %>
          	  <td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="20"></td>
          	  <td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<% } %>
          	  
       		 <%  if (moneyTypesCount > 0 ){ %>
       		 <% for (int col = 0;col < moneyTypesCount ; col ++){ %>
          
				<td align="right" width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="30"></td>
				<td width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
			<%} %>
			<%} %>
			
			<% if (showDM && moneyTypesCount > 1){ %>
          	<td width="20"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="20"></td> <!-- status --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="20"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="20"></td> <!-- Method --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="30"></td> <!-- Processing Date --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<% }else { %>		
			<td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="50"></td> <!-- status --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="90"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="50"></td> <!-- Method --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="130"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td> <!-- Processing Date --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
			<%} %>

          	<%if(showDM){%>
          	<td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="70"></td> <!-- mailing Date --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="30"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="90"></td> <!-- language --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<% } %>
          	
          	<% if (showAE) {  %>
          	<% if(!showPayrollColumn && !showDivisionColumn && !showDM){ %>
          	<td width="50"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="30"></td> <!-- OptOut --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<% } else {%>
          	<td width="15"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="15"></td> <!-- OptOut --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<% } }%>
          	
          	<% if(!showPayrollColumn && !showDivisionColumn && !showDM ){ %>
          	<td width="100"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="50"></td> <!-- warning/Alert --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<%}else { %>
          	<td width="20"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="20"></td> <!-- warning/Alert --> 
          	<td width="1"><IMG height="1" src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<%} %>
        </tr>
        
        <tr class="tablesubhead"  >
          <td class="databorder" rowspan="<%=rowspan%>" width="1"></td>
          <c:if test="${empty param.printFriendly}">
          	<td id="scrollableHeaderElement" valign="bottom" rowspan="<%=rowspan%>" width="30"><b>Action</b></td>
          	<td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
		  <td valign="bottom" rowspan="<%=rowspan%>"><report:sort  formName="employeeEnrollmentSummaryReportForm" field="lastName" direction="asc"><b>Name</b></report:sort></td>
          <td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
           <%if(showPayrollColumn){%>
          	<td align="center" valign="bottom" rowspan="<%=rowspan%>">
            <b><NOBR><report:sort  formName="employeeEnrollmentSummaryReportForm" field="employerDesignatedID" direction="desc">Employee<br>ID</b></report:sort></NOBR>
          	<td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	</td>
          <% } %>
          
          
		  <%if(showDivisionColumn){%>
		  	<td valign="bottom" rowspan="<%=rowspan%>"><b><NOBR><report:sort formName="employeeEnrollmentSummaryReportForm"  field="organizationUnitID" direction="asc">Division</b></report:sort></NOBR></td>
		  	<td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		 <% } %>
		  	<td valign="bottom" rowspan="<%=rowspan%>"><b><NOBR><report:sort formName="employeeEnrollmentSummaryReportForm"  field="eligibleToEnroll" direction="asc">Eligible to<br/> participate</b></report:sort></NOBR></td>
		  	<td class="dataheaddivider" rowspan="<%=rowspan%>" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  
		   <%  if (theReport.getMoneyTypes() != null && theReport.getMoneyTypes().size() > 0 ){ %>
		   	<% if (moneyTypeLength == 1){ %>
		  		<td align="center" colspan="<%= moneytypewidth  %>"><b><NOBR>Plan entry</NOBR><br>date</b></td>
		  	<% } else { %>
		  		<td align="center" colspan="<%= moneytypewidth  %>"><b>Plan entry date</b></td>
          	<% } %>
		  
		  <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		
		  <% } %>

		<%if(showDM){%>
		  <td valign="bottom"  colspan="9" align="center" ><b>Enrollment</b></td>
		  <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 		<% } else {%>
		  <td valign="bottom"  colspan="7" align="center" ><b>Enrollment</b></td>
		  <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
 		<% } %>
		   
		  <% if (employeeEnrollmentSummaryReportForm.isEZstartOn()) {  %> 
		  <td valign="bottom" align="center" rowspan="<%=rowspan%>" ><b><NOBR><report:sort  formName="employeeEnrollmentSummaryReportForm"   field="optOut" direction="asc">Opt<br>out<br></b></report:sort></NOBR></td>
		  <td class="dataheaddivider" rowspan="<%=rowspan%>" ><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		   <% } %>
		   <% if(!showPayrollColumn && !showDivisionColumn && !showDM ){ %>
		  <td valign="bottom" rowspan="<%=rowspan%>" ><b>Warning/<br/>Alert</b></td>
		  		  
		  <%}else { %>
		  <td valign="bottom" rowspan="<%=rowspan%>" ><b>Warning/<br/>Alert</b></td>
		  
		  <% } %>
		  
		</tr>
		
		
		<tr class="tablesubhead">
		 <% if(theReport.getMoneyTypes() != null && theReport.getMoneyTypes().size() > 0){ %>
		  <td class="dataheaddivider" colspan="<%= (moneytypewidth+7)  %>" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		  <% } else {%>
			   <% if(!showPayrollColumn && !showDivisionColumn && !showDM ){ %>
			  <td class="dataheaddivider" colspan="5" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			  <% }else { %>
			  <td class="dataheaddivider" colspan="5" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
			  <% } %>			  
		  <% } %>	
		</tr>
		
		<tr class="tablesubhead">
		<c:if test="${not empty theReport.moneyTypes}">
<c:forEach items="${theReport.moneyTypes}" var="moneyTypes">
	  	   
            	 <td align="center" valign="bottom" nowrap="nowrap" width="55">
            	 
            	<c:if test="${employeeEnrollmentSummaryReportForm.moneyTypeSelected != 'All' && 
            	
            				employeeEnrollmentSummaryReportForm.moneyTypeSelected != null}">
            	 
<c:if test="${employeeEnrollmentSummaryReportForm.moneyTypeSelected eq moneyTypes.value}">
            	 		
            	 		<strong>${moneyTypes.label}</strong>
            			
</c:if>
            	
<c:if test="${employeeEnrollmentSummaryReportForm.moneyTypeSelected ne moneyTypes.value}">
            	 		
            	 		<strong>${moneyTypes.label}</strong> </a> 
</c:if>
            		
            	</c:if>
            	
            	
            	<c:if test="${employeeEnrollmentSummaryReportForm.moneyTypeSelected == 'All' || 
            	
            				employeeEnrollmentSummaryReportForm.moneyTypeSelected == null}">
            	
            		
            	 		<strong>${moneyTypes.label}</strong>
            		
            	
            	</c:if>
            	
            	</td>
        		 <td class="dataheaddivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
</c:forEach>
          </c:if>
	  	  
	  	  <td align="center" valign="bottom" nowrap="nowrap" ><b><NOBR><report:sort formName="employeeEnrollmentSummaryReportForm" field="enrollmentStatus" direction="asc">Status</b></report:sort></NOBR></td>
          <td class="dataheaddivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
	  	  
	  	  <td align="center" valign="bottom"  nowrap="nowrap"  ><b><NOBR><report:sort formName="employeeEnrollmentSummaryReportForm" field="enrollmentMethod" direction="asc">Method</b></report:sort></NOBR></td>
          <td class="dataheaddivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
	  	  
	  	  <td align="center" valign="bottom" nowrap="nowrap"><b><NOBR><report:sort  formName="employeeEnrollmentSummaryReportForm" field="enrollmentProcessedDate" direction="asc">Processing<br/>date</b></report:sort></NOBR></td>
          <td class="dataheaddivider" valign="bottom" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>

		<%if(showDM){%>
		  <td align="center" valign="bottom"><b><NOBR><report:sort   formName="employeeEnrollmentSummaryReportForm" field="mailingDate" direction="asc">Kit mailing<br>date</b></report:sort></NOBR></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<% } %>

		  <td align="center" valign="bottom"><b><NOBR><report:sort  formName="employeeEnrollmentSummaryReportForm" field="languageInd" direction="asc">Material<br>language</b></report:sort></NOBR></td>
		  <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	  	</tr>
		
	  <c:if test="${empty param.printFriendly}">
         
      <% if (theReport.getDetails() == null || theReport.getDetails().size() <= 0) { // we have no search results %>      
       <tr class="datacell1">
        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        <td valign="bottom" colspan="<%=(moneytypewidth+23)%>">
        <!-- no results -->
          <c:if test="${empty pageScope.errorsExist}">
            <content:getAttribute id="NoSearchResults" attribute="text"/>
          </c:if>
        </td>
       </tr>
        
      <% } %>
     </c:if>	

<!-- detail line -->        
      <c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<c:set var="indexValue" value="${theIndex.index}"/>

<%
EmployeeSummaryDetails theIndex = (EmployeeSummaryDetails)pageContext.getAttribute("theItem");

String temp = pageContext.getAttribute("indexValue").toString();
String theprofileitems=employeeEnrollmentSummaryReportForm.getTheItem(Integer.parseInt(temp)).getProfileId();
String thelanguageid=employeeEnrollmentSummaryReportForm.getTheItem(Integer.parseInt(temp)).getLanguageInd();

%>

<input type="hidden" name="profileId" value="${theprofileitems}"/> <%-- indexed="true" name="theItem" --%>
<input type="hidden" name="beforeTaxDeferralAmt" value="${theItem.beforeTaxDeferralAmt}" /><%--  input - indexed="true" name="theItem" --%>
<input type="hidden" name="afterTaxDeferralPct" value="${theItem.afterTaxDeferralPct}" /><%--   input - indexed="true" name="theItem" --%>
<input type="hidden" name="afterTaxDeferralPct" value="${theItem.afterTaxDeferralPct}" /><%--   input - indexed="true" name="theItem" --%>
<input type="hidden" name="optOutEditable" value="${theItem.optOutEditable}" /><%--  input - indexed="true" name="theItem" --%>

            <% if (Integer.parseInt(temp) % 2 == 0) { %>
                                <tr class="datacell2">
                                <% } else { %> 
                                <tr class="datacell1">
                                 <% } %> 
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	<!-- action -->
          <c:if test="${empty param.printFriendly}" >
          
<c:if test="${theItem.showHistoryAndNameLink ==true}">
            		<td align="center"><report:actions profile="userProfile" item="theItem" action="evhDeferral"/> </td>
            		<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
<c:if test="${theItem.showHistoryAndNameLink ==false}">
            		<td align="center"><report:actions profile="userProfile" item="theItem" action="editEmployeeSummary"/> </td>
            		<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
          
          </c:if>
          
	<!-- name -->
          <td>
<c:if test="${userProfile.welcomePageAccessOnly == false}">
<c:if test="${theItem.showHistoryAndNameLink ==true}">
	          <ps:link action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
	          <span class="printFriendlyLink">
${theItem.fullName}
	          </span>
		      </ps:link>
</c:if>
<c:if test="${theItem.showHistoryAndNameLink ==false}">
${theItem.fullName}
</c:if>
</c:if>
<c:if test="${userProfile.welcomePageAccessOnly == true}" >
${theItem.fullName}
</c:if>
		      <br>
	          <render:ssn property="theItem.ssn"/>
          </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	<!-- employee id -->
          <%if(showPayrollColumn){%>
<td nowrap="nowrap">${theItem.employerDesignatedID}</td><!--Payroll-->
              <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <%}%>

	<!-- division -->
          <%if(showDivisionColumn){%>

<td nowrap="nowrap">${theItem.division}</td><!--Division-->
              <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <%}%>


	<!-- eligible to participate -->          
<td>${theItem.eligibleToEnroll}</td>
            <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                

	<!-- money types -->
            <%  if (theReport.getMoneyTypes() != null && theReport.getMoneyTypes().size() > 0 ){ %>
            <% for (int col = 0;col < theReport.getMoneyTypes().size() ; col++){ %>
            	 <td align="center"><nobr><%=theIndex.getPlanEntryDates().get(col) %></nobr></td>
        		<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>  
            <% } %>  
            <% } %>

	<!-- status -->            
<td align="left">${theItem.enrollmentStatus}</td>
            <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>  

	<!-- method -->
<td>${theItem.enrollmentMethod}<br>
          <% if (theIndex.isParticipantInd() && !"NT".equals(theIndex.getParticipantStatusCode())) { %>
                <ps:link action="/do/participant/participantEnrollmentDetails" paramId="profileId" paramName="theItem" paramProperty="profileId">(details)</ps:link>
          <% } %></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>  

	<!-- processind date -->          
          <td> <render:date property="theItem.enrollmentProcessedDate" patternOut="MM/dd/yyyy"/> </td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>  
          
	<!-- mailing date -->          
            <%if(showDM){%>
            
            <td align="center"><%=theIndex.getMailingDate()%></td> 
            <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
             <%} %>

	<!-- language -->
            <td align="center" >
	    		<c:if test="${not empty param.printFriendly}">
					<%
						String itemvalue = theIndex.getLanguageInd();
						pageContext.setAttribute("itemvalue", itemvalue, PageContext.PAGE_SCOPE);
					%>

					<form:select path="theItemList[${theIndex.index}].languageInd" 
						disabled="true" cssStyle="boxbody" value="${itemvalue}">
						<%-- form:select - indexed="true"    --%>
						<form:option value="EN">English</form:option>
						<form:option value="SP">Spanish</form:option>
						<form:option value="NS">Not-selected</form:option>
					</form:select>
				</c:if> 
				
				<c:if test="${empty param.printFriendly}">
			
				<form:select path="theItemList[${theIndex.index}].languageInd" 
					cssStyle="boxbody" onclick="removeNotSelectedOptionLang('${theIndex.index}')"
					 disabled="<%=!theIndex.isEmploymentStatusActiveOrBlank() %>"
					onchange="enableSubmit();" value="${thelanguageid}">

					
					<form:option value="EN">English</form:option>
					<form:option value="SP">Spanish</form:option>
					<form:option value="NS">Not-selected</form:option>
				</form:select>
			
			

			

		</c:if>
		</td>
		
	   	   <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	<!-- opt out -->	   	  
	   	   <% if (employeeEnrollmentSummaryReportForm.isEZstartOn()) {  %>
	   	   <c:set var="previousValue" value="${theItem.optOutHistory}" scope="page"/>            
           <td align="center" >            
            <c:if test="${not empty param.printFriendly}">   
                 
<form:checkbox path="theItemList[${theIndex.index}].optOut" disabled="true" value="Y" /><%-- indexed="true" name="theItem" --%>
            </c:if>
            <c:if test="${empty param.printFriendly}"> 
            <%
            String enrollmentopt=employeeEnrollmentSummaryReportForm.getTheItem(Integer.parseInt(temp)).getAutoEnrollOptOutInd();
            pageContext.setAttribute("enrollmentopt", enrollmentopt, PageContext.PAGE_SCOPE);
            %>
          
            <div onmouseover="<ps:employeeChangeDetail name='previousValue' />" >  
                <%=CensusUtils.getOptOutError(theIndex)%>       
 				<%
                
                String checkedVal = CensusUtils.getOptOutChecked(employeeEnrollmentSummaryReportForm.getTheItem(Integer.parseInt(temp)));
                pageContext.setAttribute("checkedVal", checkedVal);
                String disabledFlag = CensusUtils.getOptOutDisabled(theIndex);
                if(disabledFlag=="disabled"){
                	pageContext.setAttribute("disabledFlag", true);
                }else{
                	pageContext.setAttribute("disabledFlag", false);
                }
                
                %> 
                
               
                <form:checkbox path="theItemList[${theIndex.index}].autoEnrollOptOutInd" onclick="enableSubmit(); flipIndicator('${theIndex}')" value="Y" checked="${checkedVal}" disabled="${disabledFlag}"/>                   
             </div> 
            </c:if> 
           </td>
		   		<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		   		<% } %>

	<!-- warning/alert -->
            <% boolean hasInfo = employeeEnrollmentSummaryReportForm.hasInfo(employeeEnrollmentSummaryReportForm.getTheItem(Integer.parseInt(temp)).getProfileId()); %>
            <% if(theIndex.getWarnings().hasAnyWarnings() || hasInfo || theIndex.hasAlerts()) { %>          
			<td align="middle"> 
				<table>
					<tr>    
					  <% if(theIndex.hasAlerts()) { %> 
					  	<c:set var="alertMessage" scope="page"> <content:getAttribute id="eligibilityMouseOverAlertIcon" attribute="text"/> </c:set>      
					   	<td align="middle" onmouseover="<ps:warnings name='alertMessage' />" >
					   		<a href="/do/mcdispatch/">
		               			<IMG height=12 src="/assets/unmanaged/images/alert.gif" width=12 border=0>
		               		</a>
		               </td>          
		               <% } %>
			           <% if(theIndex.getWarnings().hasAnyWarnings()) { %> 
			           	<c:set var="war" scope="page">
			              ${theItem.warnings.warningContent}
			          	</c:set>
			           <td align="middle" onmouseover="<ps:warnings name='war' />" ><IMG height=12 src="/assets/unmanaged/images/warning2.gif" width=12 border=0></td>
			          <% } %>
			          <% if(hasInfo) { %>
			          	<c:set var="info" scope="page"><content:getAttribute id="eligibilityMouseOverInfoIcon" attribute="text"/></c:set>  
			          	<td align="middle" onmouseover="<ps:warnings name='info' />" ><IMG height=12 src="/assets/unmanaged/images/icon_info.gif" width=12 border=0></td>      
			          <% } %>  
			       </tr>
			    </table>
			</td>
			
        <% } else { %>
         <%-- No warning or info --%>    
          <td align="middle" >&nbsp;</td> 
           
        <% } %>  
            
            
        <!-- Track changes for the following fields -->
	   <% if (employeeEnrollmentSummaryReportForm.isEZstartOn()) {  %>  
	    	<ps:trackChanges name="employeeEnrollmentSummaryReportForm" indexPrefix="theItemList" index="${theIndex.index}" property="autoEnrollOptOutInd"/>		    
	   
      	   
	  <% } %> 
	  <ps:trackChanges name="employeeEnrollmentSummaryReportForm" indexPrefix="theItemList" index="${theIndex.index}" property="languageInd"/>
  
        </tr>
</c:forEach>
  		</c:if>    


		
		<!-- End of Last line -->        

        </table>
        </div>
      <div>
      <table width="<%=tableWidth%>">
      	
      	 <tr >
		 	<td width="30%">&nbsp;</td> 
		    <td align="center" width="50%"><b><report:recordCounter report="theReport" label="Employees" /></b></td>
		    <td align="right"  width="20%"><report:pageCounter report="theReport" arrowColor="black"  formName="employeeEnrollmentSummaryReportForm"/></td>
		</tr>
      </table>
      </div>       
     <table>        
        <tr>
          <td colspan="<%=numberOfDisplayColumns%>" class="boldText">&nbsp;</td>
        </tr>
        
        <tr> 
          <table width="<%=tableWidth%>"> 
            <tbody>
              <tr>
                <td width="45%">&nbsp;</td>
                <td id="cancelButton" width="35%" align="right" nowrap>
                  <c:if test="${empty param.printFriendly }" >
                    <input name="button" type="submit" class="button134" value="CANCEL" onclick="return doReset();" disabled="true" />
                  </c:if>
                </td>
	            <td id="saveButton" width="20%" align="right" nowrap>
	            	<c:if test="${empty param.printFriendly }" >
	                	<input name="button" type="submit" class="button134" value="SAVE" onclick="return doSave();" disabled="true" />
	                </c:if>
				</td> 
              </tr>
            </tbody>            
          </table>
        </tr>
              
        <tr>
          <td colspan="<%=numberOfDisplayColumns%>">
            <br>
            <p><content:pageFooter beanName="layoutPageBean"/></p>
            <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
            <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
          </td>
        </tr> 
     </table> 

    </ps:form>
    </table>


<%  if(employeeEnrollmentSummaryReportForm.isEZstartOn() || employeeEnrollmentSummaryReportForm.isEligibiltyCalcOn()) { %>		

		<script type="text/javascript" >
		 // create calendar object(s) just after form tag closed
			var calFromDate1 = new calendar(document.forms['employeeEnrollmentSummaryReportForm'].elements['fromPED']);
			calFromDate1.year_scroll = true;
			calFromDate1.time_comp = false;
			var calToDate1 = new calendar(document.forms['employeeEnrollmentSummaryReportForm'].elements['toPED']);
			calToDate1.year_scroll = true;
			calToDate1.time_comp = false;
			
			var calFromDate = new calendar(document.forms['employeeEnrollmentSummaryReportForm'].elements['enrolledFrom']);
			calFromDate.year_scroll = true;
			calFromDate.time_comp = false;
			var calToDate = new calendar(document.forms['employeeEnrollmentSummaryReportForm'].elements['enrolledTo']);
			calToDate.year_scroll = true;
			calToDate.time_comp = false;
		
		</script>
<% }else  if (!employeeEnrollmentSummaryReportForm.isEZstartOn()) {  %>               
	<script type="text/javascript" >
		 // create calendar object(s) just after form tag closed
		var calFromDate = new calendar(document.forms['employeeEnrollmentSummaryReportForm'].elements['enrolledFrom']);
		calFromDate.year_scroll = true;
		calFromDate.time_comp = false;
		var calToDate = new calendar(document.forms['employeeEnrollmentSummaryReportForm'].elements['enrolledTo']);
		calToDate.year_scroll = true;
		calToDate.time_comp = false;

	</script>
<% } %>
      
    


      <br>
    </td>

    <!-- column to the right of the report -->
    <td width="15"></td>

<c:if test="${not empty param.printFriendly}">
  </td>
</c:if>

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

<script language="javascript" src="/assets/unmanaged/javascript/oldstyle_tooltip.js"></script>
