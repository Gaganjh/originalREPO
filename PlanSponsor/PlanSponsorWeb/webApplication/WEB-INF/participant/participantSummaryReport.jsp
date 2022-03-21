<%-- taglib used --%>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>

<%-- Imports --%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantSummaryDetails" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantSummaryReportForm" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<un:useConstants var="psWebConstants" className="com.manulife.pension.ps.web.Constants"/>
<un:useConstants var="commonConstants" className="com.manulife.pension.platform.web.CommonConstants"/>


<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANTS_VIEW_ALL%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="viewAllParticipants"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_PARTICIPANTS_NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="noSearchResults"/>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_SEARCH_FOR_PARTICIPANTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="searchForParticipants"/>								   
<jsp:useBean id="participantSummaryReportForm"
                         scope="session" 
                         class="com.manulife.pension.ps.web.participant.ParticipantSummaryReportForm" />
	
 
<%
 
ParticipantSummaryReportData theReport = (ParticipantSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
// true for Current report or false for historical
boolean isReportCurrent = true;

String tableWidth = participantSummaryReportForm.getTableWidth();
String columnSpan = participantSummaryReportForm.getColumnSpan();
String columnSpanLess2 = participantSummaryReportForm.getColumnSpanLess2();
String columnSpanLess4 = participantSummaryReportForm.getColumnSpanLess4();
String participantTotalsColumnSpan = participantSummaryReportForm.getParticipantTotalsColumnSpan();
String moneyTotalsColumnSpan = participantSummaryReportForm.getMoneyTotalsColumnSpan();

%>

<%
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

String ua = request.getHeader( "User-Agent" );
boolean isMSIE = (ua != null && ua.indexOf("MSIE") != -1);
request.setAttribute("isIE",isMSIE);
boolean isTrident = (ua != null && ua.indexOf("Trident") != -1);
request.setAttribute("isNewIE", isTrident);
boolean isFirefox = (ua != null && ua.indexOf("Firefox") != -1);
request.setAttribute("isMozilla", isFirefox);
%>

<c:if test="${participantSummaryReportForm.asOfDateCurrent ==false}">
<% isReportCurrent = false; %>
</c:if>

<c:if test="${empty param.printFriendly }" >
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
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
  	fixedTable.style.left = 31;
  	fixedTable.style.visibility = 'visible';
  </c:if>
</c:if>
}

/**
 * Sets the filter using the given HTML select object.
 */
function setFilterFromSelect(theSelect)
{
showOrHideStatus();													// CL 110234

  var newValue = theSelect.options[theSelect.selectedIndex].value;
  filterMap[theSelect.name] = newValue;
  
  var selectedDate = theSelect.selectedIndex;
  
    participantStatusLabelDiv = document.getElementById('participantStatusLabel');
    participantStatusComboDiv = document.getElementById('participantStatusCombo');
   

  if (selectedDate > 0) {
	if(participantSummaryReportForm.gatewayChecked!=null){
		participantSummaryReportForm.gatewayChecked.disabled = true;
    	participantSummaryReportForm.gatewayChecked.checked = false;
	}
   } else {
	if(participantSummaryReportForm.gatewayChecked!=null){
		participantSummaryReportForm.gatewayChecked.disabled = false;
	}
	
  }	
  if (selectedDate > 0) {
	if(participantSummaryReportForm.managedAccountChecked!=null){
		participantSummaryReportForm.managedAccountChecked.disabled = true;
    	participantSummaryReportForm.managedAccountChecked.checked = false;
	}
   } else {
	if(participantSummaryReportForm.managedAccountChecked!=null){
		participantSummaryReportForm.managedAccountChecked.disabled = false;
	}	
  }	

}
<%-- CL 110234 Begin--%>
/**
 *  To hide or show the Contribution Status and Employment Status
 */
function showOrHideStatus(){
showOrHideContributionStatus();
showOrHideEmploymentStatus();
}

/**
 *  To hide or show the custom filter Contribution Status 
 *  if as of date is not default then hide the contribution status filter 
 *  from the custom filter otherwise show the status.
 */
function showOrHideContributionStatus(){
	var asOfDateOptions = document.forms["participantSummaryReportForm"].elements["asOfDate"];

	if(asOfDateOptions.selectedIndex > 0){
		document.getElementById("participantStatus").style.display = "none";
		document.getElementById("participantStatus").style.display = "none";

		var tempStatus = document.forms["participantSummaryReportForm"].elements["status"];
		tempStatus.selectedIndex = 0; 
		setFilterFromInput(tempStatus);


	}else if(asOfDateOptions.selectedIndex == 0){
		document.getElementById("participantStatus").style.display = "block";
		document.getElementById("participantStatus").style.display = "block";
	}
}

/**
 *  To hide or show the custom filter Employment Status 
 *  if as of date is not default then hide the Employment status filter 
 *  from the custom filter otherwise show the status.
 */
function showOrHideEmploymentStatus(){
	var asOfDateOptions = document.forms["participantSummaryReportForm"].elements["asOfDate"];
	
	if(asOfDateOptions.selectedIndex > 0){
		document.getElementById("participantEmpStatus").style.display = "none";
		document.getElementById("participantEmpStatus").style.display = "none";

		var tempStatus = document.forms["participantSummaryReportForm"].elements["employmentStatus"];
		tempStatus.selectedIndex = 0; 
		setFilterFromInput(tempStatus);


	}else if(asOfDateOptions.selectedIndex == 0){
		document.getElementById("participantEmpStatus").style.display = "block";
		document.getElementById("participantEmpStatus").style.display = "block";
	}
}
<%-- CL 110234 End--%>
function tooltip(DefInvesValue)
{
	if(DefInvesValue == "TR")
	Tip('Instructions were provided by Trustee - Mapped');
	else if(DefInvesValue == "PR")
	Tip('Instructions prorated - participant instructions incomplete / incorrect');
	else if(DefInvesValue == "PA")
	Tip('Participant Provided');
	else if(DefInvesValue == "DF")
	Tip('Default investment option was used');
	else if(DefInvesValue == "MA")
	Tip('Managed Accounts');
	else
	UnTip();
}

function setGatewayOption(gatewayCheckBox){

	if(!gatewayCheckBox.checked){
		document.participantSummaryReportForm.resetGateway.value="false";
	} else {
		document.participantSummaryReportForm.resetGateway.value="true";
	}	
	   
}

function setManagedAccountOption(managedAccountCheckBox){
	if(!managedAccountCheckBox.checked){
		document.participantSummaryReportForm.resetManagedAccount.value="false";
	} else {
		document.participantSummaryReportForm.resetManagedAccount.value="true";
	}
}
//Called when reset is clicked
function doReset() {
	document.participantSummaryReportForm.task.value = "default";
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


	
	if (document.participantSummaryReportForm.ssnOne) {
			if (document.participantSummaryReportForm.ssnOne.value.length >= 0){
					document.participantSummaryReportForm.namePhrase.value = "";
			}
	}


	if (document.participantSummaryReportForm.ssnTwo) {
			if (document.participantSummaryReportForm.ssnTwo.value.length >= 0){	
				document.participantSummaryReportForm.namePhrase.value = "";
			}
	}

	if (document.participantSummaryReportForm.ssnThree) {
			if (document.participantSummaryReportForm.ssnThree.value.length >= 0){	
				document.participantSummaryReportForm.namePhrase.value = "";
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
	
	
	if (document.participantSummaryReportForm.namePhrase) {
			if (document.participantSummaryReportForm.namePhrase.value.length >= 0){
				document.participantSummaryReportForm.ssnOne.value = "";
				document.participantSummaryReportForm.ssnTwo.value = "";
				document.participantSummaryReportForm.ssnThree.value = "";
			}	
	}
	
}

/**
 * Sets the filter using the given HTML select object.
 */

var previousSelectionIndex = 0;

function setFilterFromSelect2(theSelect)
{
  var newValue = theSelect.options[theSelect.selectedIndex].value;
  if ( newValue != "##" ) {
	  filterMap[theSelect.name] = newValue;
  }
  else {
	  theSelect.selectedIndex = previousSelectionIndex;
  }
}

function setPreviousSelection(theSelect)
{
  previousSelectionIndex = theSelect.selectedIndex;
}

</script>
</c:if>

  <!-- Remove the extra column before the report -->
  <c:if test="${empty param.printFriendly}" >
    <td width="30"><img src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
    <td>
  </c:if>

  <c:if test="${not empty param.printFriendly}" >
    <td>
  </c:if>

      <p>
      <c:if test="${sessionScope.psErrors ne null}">
      <c:if test="${empty param.printFriendly}" >
<c:set var="errorsExist" value="true" scope="page" ></c:set>
        <div id="errordivcs"><content:errors scope="session"/></div>
      </c:if>
      <c:if test="${not empty param.printFriendly}" ><!-- QC Defect fix #6887 starts-->
          <%pageContext.removeAttribute("psErrors") ;%>
      </c:if><!-- QC Defect fix #6887 ends-->
      </c:if>
      </p>

    <!-- Beginning of Participant Summary report body -->
      <ps:form method="post"  action="/do/participant/participantSummary" modelAttribute="participantSummaryReportForm" name="participantSummaryReportForm">
<input type="hidden" name="task" value="filter"/>
<input type="hidden" name="resetGateway" />
<input type="hidden" name="resetManagedAccount" />			
		<%-- TAB section - starts --%>
		<c:if test="${not empty param.printFriendly}" >
			<jsp:include flush="true" page="accountsPageNavigationTabBar.jsp">
				<jsp:param name="selectedTab" value="AccountsSummaryPrintTab"/>
			</jsp:include>
		</c:if>
		<c:if test="${empty param.printFriendly}" >
			<jsp:include flush="true" page="accountsPageNavigationTabBar.jsp">
				<jsp:param name="selectedTab" value="AccountsSummaryTab"/>
			</jsp:include>
		</c:if>
		<%-- TAB section - ends --%>

    <!-- As of Date, # of participants, paging -->
      <table width="730" height="160px" cellspacing="0" cellpadding="0" bgcolor="Beige">

        <!-- Start of body title -->
        <tr class="tablehead" >
          <td class="tablesubhead" colspan="8"><b>Participant Summary Search</b></td>
        </tr>
        <!-- End of body title -->
        <tr class=filterSearch>
          <td colspan="4">	
            <content:getAttribute id="searchForParticipants" attribute="text"></content:getAttribute>
            <c:if test="${not empty layoutPageBean.introduction2}">
              <content:getAttribute beanName="layoutPageBean" attribute="introduction2"/>
              <br>
</c:if>
          </td>
        </tr>
        <tr class=filterSearch>
          <td width="170"><b>Summary as of </b><br>
<input type="hidden" name="baseAsOfDate"/>
           <ps:select name="participantSummaryReportForm" property="asOfDate" onchange="setFilterFromSelect(this);" tabindex="10">
              <ps:dateOption name="<%=Constants.USERPROFILE_KEY%>"
                             property="currentContract.contractDates.asOfDate"
                             renderStyle="<%=RenderConstants.LONG_STYLE%>"/>
              <ps:dateOptions name="<%=Constants.USERPROFILE_KEY%>"
                              property="currentContract.contractDates.monthEndDates"
                              renderStyle="<%=RenderConstants.LONG_STYLE%>"/> 
            </ps:select>
	      </td>
          <td width="170"><b>Last name</b><br>
            <c:if test="${not empty param.printFriendly}" >
<form:input path="namePhrase" onchange="setFilterFromInput(this);" readonly="true" cssClass="inputField" />

            </c:if>
            <c:if test="${empty param.printFriendly}" >
<form:input path="namePhrase" onchange="setFilterFromInput(this);" onkeypress="clearSSN(event);" cssClass="inputField" tabindex="20" />

            </c:if>	            	   
          </td> 
          <td width="170"><b>SSN</b><br>  
						<c:if test="${not empty param.printFriendly}" >
							<form:password  path="ssnOne"  readonly="true" cssClass="inputField"  onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3" value="${participantSummaryReportForm.ssnOne}"/>
							<form:password  path="ssnTwo"  readonly="true"  cssClass="inputField"  onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2" value="${participantSummaryReportForm.ssnTwo}"/>
							<form:input  path="ssnThree" autocomplete="off" readonly="true"  cssClass="inputField"  onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4"/>
						</c:if>
						<c:if test="${empty param.printFriendly}" >
							<form:password  path="ssnOne"  cssClass="inputField" onkeypress="clearName(event);"  onkeyup="return autoTab(this, 3, event);"  size="3" maxlength="3"  tabindex="30" value="${participantSummaryReportForm.ssnOne}"/>
							<form:password  path="ssnTwo"  cssClass="inputField" onkeypress="clearName(event);"  onkeyup ="return autoTab(this, 2, event);" size="2" maxlength="2"  tabindex="31" value="${participantSummaryReportForm.ssnTwo}"/>
							<form:input  path="ssnThree" autocomplete="off" cssClass="inputField"  onkeypress="clearName(event);"   onkeyup ="return autoTab(this, 4, event);" size="4" maxlength="4"  tabindex="32"/>
						</c:if>	            	  

          </td>
			<td width="200" style="padding-top: 9px">
			<c:if test="${participantSummaryReportForm.hasContractGatewayInd ==true}">             
			<% if (isReportCurrent) { %>
				<c:if test="${empty param.printFriendly}" >	
<form:checkbox path="gatewayChecked" onclick="setGatewayOption(this);" disabled="false" tabindex="60" value="true" />

				</c:if>
				<c:if test="${not empty param.printFriendly}" >
<form:checkbox path="gatewayChecked" disabled="true" value="true" />

				</c:if>
		  	<% } else {%>
<form:checkbox path="gatewayChecked" disabled="true" value="true" />

			  <% } %>
			   <b>${psWebConstants.COL_GIFL_FEATURE_TITLE_SC}</b>
</c:if></td>
		</tr>
        <tr class=filterSearch>
		  <%-- CL 110234 Begin--%>
		  <td>
		    <DIV id="participantEmpStatus"> <b>Employment Status</b><br>
                <!-- remove status drop down from Print friendly version -->
                <c:if test="${not empty param.printFriendly}" >
<form:select path="employmentStatus" disabled="true" onchange="setFilterFromSelect2(this);" >

                set the first value of the select
					<form:option value="All">All</form:option>
					

					<form:options items="${participantSummaryReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select>
            </c:if>
            <c:if test="${empty param.printFriendly}" >
<form:select path="employmentStatus" onchange="setFilterFromSelect2(this);" tabindex="50" >
					<form:option value="All">All</form:option>
					
					
<form:options items="${participantSummaryReportForm.statusList}" itemLabel="label" itemValue="value"/> 
</form:select>
            </c:if>
            </DIV>            
       	  </td>	 
		 <%-- CL 110234 End --%>
		  <td>
            <DIV id="participantStatus"> <b>Contribution Status</b>	<br>		<%-- CL 110234 --%>
                <!-- remove status drop down from Print friendly version -->
                <c:if test="${not empty param.printFriendly}" >
<form:select path="status" disabled="true" onchange="setFilterFromSelect2(this);" >
                  <form:option value="All">All</form:option>
                  <form:option value="Active">Active</form:option>
<c:if test="${userProfile.currentContract.hasEmployerMoneyTypeOnly==false}">
                  <form:option value="Active no balance">Active no balance</form:option>
                  <form:option value="Active non contributing">Active non contributing</form:option>
                  <form:option value="Active opted out">Active opted out</form:option>
</c:if>
                  <form:option value="Inactive not vested">Inactive not vested</form:option>
                  <form:option value="Inactive with balance">Inactive with balance</form:option>
<c:if test="${userProfile.currentContract.hasEmployerMoneyTypeOnly ==false}">
                  <form:option value="Opted out not vested">Opted out not vested</form:option>
</c:if>
</form:select>
            </c:if>
            <c:if test="${empty param.printFriendly}" >
<form:select path="status" onchange="setFilterFromSelect2(this);" tabindex="50" >
                <form:option value="All">All</form:option>
                <form:option value="Active">Active</form:option>
<c:if test="${userProfile.currentContract.hasEmployerMoneyTypeOnly ==false}">
                <form:option value="Active no balance">Active no balance</form:option>
                <form:option value="Active non contributing">Active non contributing</form:option>
                <form:option value="Active opted out">Active opted out</form:option>
</c:if>
                <form:option value="Inactive not vested">Inactive not vested</form:option>
                <form:option value="Inactive with balance">Inactive with balance</form:option>
<c:if test="${userProfile.currentContract.hasEmployerMoneyTypeOnly ==false}">
                <form:option value="Opted out not vested">Opted out not vested</form:option>
</c:if>
</form:select>
            </c:if>
            </DIV>            
       	  </td>	
          <td width="170" valign="bottom" class="filterSearch">
<c:if test="${participantSummaryReportForm.showDivision ==true}">
            	<b>Division</b><br>
                <c:if test="${not empty param.printFriendly}" >
<form:input path="division" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${empty param.printFriendly}" >
<form:input path="division" maxlength="25" cssClass="inputField" tabindex="40"/>
			    </c:if>
</c:if>&nbsp;
</td>

			<td style="padding-top: 12px">	
			<c:if test="${participantSummaryReportForm.hasManagedAccountInd ==true}">		
					<%
						if (isReportCurrent) {
					%>
					<c:if test="${empty param.printFriendly}">
						<form:checkbox path="managedAccountChecked"
							onclick="setManagedAccountOption(this);" disabled="false"
							 value="true" />
					</c:if>
					<c:if test="${not empty param.printFriendly}">
						<form:checkbox path="managedAccountChecked" disabled="true" value="true" />
					</c:if>
					<%
						} else {
					%>
					<form:checkbox path="managedAccountChecked" disabled="true" value="true" />
					<%
						}
					%>
					<b>Managed Accounts</b>
				</c:if></td>
		</tr>
           <tr class=filterSearch>
	      <td valign="middle"><div align="left" colspan="4">
            <c:if test="${empty param.printFriendly}" >
              <input type="submit" name="submit" value="search" tabindex="70"/>
              <input type="submit" name="reset" value="reset" tabindex="80" onclick="return doReset();"/>
            </c:if>
            &nbsp;</div>
          </td>
          <td colspan="4" class="filterSearch"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>
        </tr>
        <tr>
          <td colspan="4" class="filterSearch"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>
        </tr>	
      </table>
      <br>
      
	  	 <c:if test="${not empty param.printFriendly}" >
          <table width="<%=tableWidth%>" border="0" cellspacing="0" cellpadding="0">
		</c:if>
		<c:if test="${empty param.printFriendly}" >
      <table width="<%=tableWidth%>" style="width:730px;Overflow-x:auto;" border="0" cellspacing="0" cellpadding="0">   
		</c:if>	
      	<!-- Set the column spacing for the report -->
        <tr>
          <td width="1"></td>
          <td style = 'padding-left: 5px'></td>  	<!--History icon-->
          <td width="1"></td>
          <td width="150"></td>  	<!--Name/SSN-->
<c:if test="${participantSummaryReportForm.showDivision ==true}">
          <td width="1"></td>
          <td width="75"></td>		<!--Division-->
</c:if>
          <td width="1"></td>
          <td width="35"></td> 		<!--Age-->
          <td width="1"></td>
          <td width="125" colspan="3"></td> 	<!--Status-->	<%-- CL 110234 --%>
          <td width="1"></td>
          <td width="50"></td>		<!--Default birth date -->
          <td width="1"></td>
          <td width="75"></td> 		<!--Default enrollment-->
<c:if test="${participantSummaryReportForm.hasRothFeature ==true}">
          <td width="1"></td>
		  <td width="20"></td> 		<!--Roth money-->
</c:if>
          <td width="1"></td>          
		  <td width="85"></td>		<!--Employee assets-->
          <td width="1"></td>
	      <td width="85"></td>		<!--Employer assets-->
          <td width="1"></td>
          <td width="85"></td>		<!--Total assets-->
<c:if test="${participantSummaryReportForm.hasLoansFeature ==true}">
          <td width="1"></td>
          <td width="75"></td>		<!--Outstanding loans-->
</c:if>
<c:if test="${participantSummaryReportForm.hasContractGatewayInd ==true}">
		  <td width="1"></td>
          <td width="20"></td>		<!--Gateway Option-->
</c:if>
						<c:if test="${participantSummaryReportForm.hasManagedAccountInd}">
							<td width="1"></td>
							<td width="20"></td>
							<!--Managed Account-->
						</c:if>
						<td width="5"></td>
          <td width="1"></td>		
        </tr>

		<!-- Start of body title -->
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="<%=columnSpan%>"><b><content:getAttribute beanName="layoutPageBean" attribute="body1Header"/></b></td>
        </tr>
      <!-- End of body title -->

      <!-- Summary section on the top of the report -- START -->
      <c:if test="${not empty theReport.participantSummaryTotals}">
      <c:if test="${empty pageScope.errorsExist}">
<!-- Fix CL #103591--><!--  QC defect #6887 -->
        <tr class="datacell1">
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td colspan="<%=participantTotalsColumnSpan%>" rowspan="5" align="center"><b class="copyBig">Total participants:&nbsp;
<span class="highlight">${theReport.participantSummaryTotals.totalParticipants}</span></b></td>
          <td class="beigeborder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td>&nbsp;</td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td align="right"><b>Employee<br><NOBR>assets($)</NOBR></b></td>
          <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td align="right"><b>Employer<br><NOBR>assets($)</NOBR></b></td>
          <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td align="right"><b>Total<br><NOBR>assets($)</NOBR></b></td>

        <!-- Show loans column -->
<c:if test="${participantSummaryReportForm.hasLoansFeature ==true}">
          <td class="datadivider" width="1"><b><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></b></td>
          <td align="right"><b>Outstanding<br><NOBR>loans($)</NOBR></b></td>
</c:if>

          <td class="dataheaddivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>      
        </tr>

        <tr class="datacell2">
          <td class="databorder" width="1"></td>
          <td class="beigeborder" width="1"></td>
          <td><b>Totals</b></td>
          <td class="datadivider" width="1"></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.employeeAssetsTotal' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>
          <td class="datadivider" width="1"></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.employerAssetsTotal' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>
          <td class="datadivider" width="1"></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.totalAssets' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>

        <!-- Show loans column -->
<c:if test="${participantSummaryReportForm.hasLoansFeature ==true}">
          <td class="datadivider" width="1"></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.outstandingLoans' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>
</c:if>

          <td class="beigeborder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td class="databorder" width="1"></td>
        </tr>

        <tr class="datacell1">
          <td class="databorder" width="1"></td>
          <td  class="beigeborder" width="1"></td>
          <td><b>Averages</b></td>
          <td class="datadivider" width="1"></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.employeeAssetsAverage' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>
          <td class="datadivider" width="1"></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.employerAssetsAverage' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>
          <td class="datadivider" width="1"><b></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.totalAssetsAverage' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>

        <!-- Show the loans column -->
<c:if test="${participantSummaryReportForm.hasLoansFeature ==true}">
          <td class="datadivider" width="1"></td>
          <td align="right"><b><span class="highlight"><render:number property='theReport.participantSummaryTotals.outstandingLoansAverage' defaultValue = "0.00"  pattern="###,###,##0.00"/></span></b></td>
</c:if>

          <td class="dataheaddivider"></td>
          <td class="databorder" width="1"></td>
        </tr>
        </c:if>
        <tr class="datacell2">
          <td class="databorder" width="1"></td>
          <td class="datadivider" width="1"></td>
          <td colspan="<%=moneyTotalsColumnSpan%>" class="beigeborder"></td>
          <td class="databorder" width="1"></td>
        </tr>

        <tr class="datacell1">
          <td class="databorder" width="1"></td>
          <td class="beigeborder" width="1"></td>  
          <td colspan="<%=moneyTotalsColumnSpan%>"></td>
          <td class="databorder" width="1"></td>
        </tr>

		</c:if>

        <tr class="datacell1">
          <td class="databorder" width="1"></td>
          <td  colspan="<%=columnSpanLess2%>">
          <!-- As of Date, # of participants, paging -->
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                <c:if test="${empty param.printFriendly}" >
                  <span class="b11 style1">Participant transaction history <img src="/assets/unmanaged/images/history_icon.gif" width="12" height="12"></span>
                </c:if>	            	  
                </td>
              </tr>
            </table>
          </td>
          <td class="databorder" width="1"></td>
        </tr>

        <tr class="tablehead">
          <td class="tablehead" colspan="<%=columnSpan%>">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td class="tableheadTD" width="50%">
                	&nbsp;
                </td>
                <td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Participants"/></b></td>
                <td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="participantSummaryReportForm"/></td>
              </tr>
            </table>
          </td>
        </tr>
		<c:if test="${empty param.printFriendly}" >
		 </table>

<c:if test="${empty param.printFriendly}" >
<c:choose>
  <c:when test="${isIE eq true}">
<div  id="fixedTable" style="overflow: hidden; z-index: 3; visibility: hidden; position: absolute; width: 196px;">
  </c:when>
  <c:when test="${isNewIE eq true}">
<div  id="fixedTable" style="overflow: hidden; z-index: 3; visibility: hidden; position: absolute; width: 190px;">
  </c:when>
  <c:otherwise>
<div  id="fixedTable" style="overflow: hidden; z-index: 3; visibility: hidden; position: absolute; width: 185px;">
  </c:otherwise>
</c:choose>	
<table border="0" cellspacing="0" width="100%" cellpadding="0">

<tr class="tablesubhead">


	<!-- table details starts -->
 
		<!-- History icon -->
		<td   class="databorder" width="1"></td>
<c:choose>
  <c:when test="${isIE eq true}">
		<td   height="49" width="1"></td>
  </c:when>
  <c:when test="${isNewIE eq true}">
		<td   height="41" width="1"></td>
  </c:when>
  <c:when test="${isMozilla eq true}">
		<td   height="45" width="1"></td>
  </c:when>
  <c:otherwise>
		<td   height="42" width="1"></td> 
  </c:otherwise>
</c:choose>

		<!-- Name/SSN -->
		<td   class="dataheaddivider" valign="bottom" width="1"><img
			src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<c:if test="${empty param.printFriendly}" >
		 	
		<td  valign="bottom">

			<report:sort field="lastName"direction="asc" formName="participantSummaryReportForm">
				<b>Name/SSN</b>
			</report:sort></td>
		</c:if>
		<c:if test="${not empty param.printFriendly}" >
			<td  ><b>Name/SSN</b></td>
		</c:if>

	</tr>
 
 
	<!-- Start of Details -->
	<% boolean beigeBackgroundClsFixed = false; // used to determine if the cell should use the background style
   boolean lastLineBkgrdClassFixed = false; // used to determine if the last line should be beige or not
 %> 
 
 <c:if test="${empty param.printFriendly}" >

<%
	if (!StringUtils.isEmpty(participantSummaryReportForm.getNamePhrase()) ||
		!StringUtils.isEmpty(participantSummaryReportForm.getStatus()) ||
		!StringUtils.isEmpty(participantSummaryReportForm.getEmploymentStatus()) ||
		!StringUtils.isEmpty(participantSummaryReportForm.getDivision()) ||
		!participantSummaryReportForm.getSsn().isEmpty() ||
		participantSummaryReportForm.getGatewayChecked() ||
		participantSummaryReportForm.getManagedAccountChecked())  
	{
%>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td style = 'padding-left: 5px;padding-right: 5px'>&nbsp;</td>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" colspan="<%=columnSpanLess4%>">

		<% if (theReport.getDetails() != null && theReport.getDetails().size() > 0) { // we have search results %>
			<content:getAttribute id="viewAllParticipants" attribute="text">
				<content:param>/do/participant/participantSummary</content:param>
			</content:getAttribute>
		
		<% } else { // no results %>
			<!-- no results -->
			
			<c:if test="${empty pageScope.errorsExist}">
			<content:getAttribute id="noSearchResults" attribute="text">
			<!-- no errors and no results -->
				<content:param>/do/participant/participantSummary</content:param>
			</content:getAttribute>
			</c:if>
		<% 
		   }
		   //lastLineBkgrdClass = true;
		 %>
          
        </tr>
<%
	}
%>

</c:if>
	<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >


 <c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();
				if (Integer.parseInt(temp) % 2 != 0) {
	 	beigeBackgroundClsFixed = true;
	 	lastLineBkgrdClassFixed = true; %>
			<tr class="datacell1">
				<% } else {
		beigeBackgroundClsFixed = false;
		lastLineBkgrdClassFixed = false; %>
				<tr class="datacell2">
					<% } %> 
					<%  
String action1 = "/do/participant/participantAccount/?selectedAsOfDate=" + participantSummaryReportForm.getAsOfDate();	
%> 
					<!-- history icon -->
					<td class="databorder" width="1"></td>
					<c:if test="${empty param.printFriendly}" >
					  <c:choose>
					  <c:when test="${isMozilla eq true}">
							<td style = 'padding-left: 5px;padding-right: 5px;'>
						<ps:link
							action="/do/transaction/participantTransactionHistory/"
							paramId="profileId" paramName="theItem" paramProperty="profileId">
							<img src="/assets/unmanaged/images/history_icon.gif"
								alt="Participant transaction history" 
								border="0">
						</ps:link></td>
					  </c:when>
					  <c:otherwise>
							<td width="21px">
						<ps:link
							action="/do/transaction/participantTransactionHistory/"
							paramId="profileId" paramName="theItem" paramProperty="profileId">
							<img src="/assets/unmanaged/images/history_icon.gif"
								alt="Participant transaction history" width="12" height="12"
								border="0">
						</ps:link></td>
					  </c:otherwise>
					  </c:choose>
					</c:if>
					<c:if test="${not empty param.printFriendly}" >
						<td>&nbsp;</td>
					</c:if>

					<!-- Name/SSN -->
					<td class="datadivider" width="1"></td>
					<td>
					<ps:link action="<%=action1%>" paramId="profileId"
						paramName="theItem" paramProperty="profileId">
${theItem.lastName},
${theItem.firstName} 
						<br>
						 <render:ssn property="theItem.ssn" /> 
					</ps:link></td>
				
				</tr>
</c:forEach>
	</c:if>

	<!-- End of Details -->

	<!-- Start of last line -->
	<!-- let the last line have the same background colour as the previous line -->
	<% if (lastLineBkgrdClassFixed) { %>
	<tr class="datacell1">
		<% } else { %>
		<tr class="datacell2">
			<% } %>
			<!-- History icon -->
			<td class="databorder"><img src="/assets/unmanaged/images/s.gif"
				width="1" height="4"></td>
			<td class="lastrow"></td>

			<!-- Name/SSN -->
			<td class="datadivider"></td>
			<td class="lastrow"></td>

		</tr>

		<!-- End of Last line -->
		<tr>
			<td class="databorder" colspan="<%=columnSpan%>"></td>
		</tr>

</table>

</div> 
      	<div id="scrollingTable" style="width:730px;Overflow-x:auto;Overflow-y:hidden">
</c:if>
      	<div style="width:730px;Overflow-x:auto;Overflow-y:hidden">
		<table border="0" cellspacing="0" width="100%" cellpadding="0">         
		</c:if>	
        <!-- table details starts -->
        <tr class="tablesubhead">
          
        <!-- History icon -->
          <td id='dHisIconBorder' rowspan="3" class="databorder" width="1"></td>
          <td id='dHisIconCell' rowspan="3" style = 'padding-left: 5px;'></td>
          
        <!-- Name/SSN -->
          <td id='dNameIconBorder' rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		<c:if test="${empty param.printFriendly}" >
          <td id='dNameIconCellNotPresent' rowspan="3" valign="bottom"><report:sort field="lastName" direction="asc" formName="participantSummaryReportForm"><b>Name/SSN</b></report:sort>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		  </td>
		</c:if>
		<c:if test="${not empty param.printFriendly}" >
          <td id='dNameIconCell' rowspan="3"><b>Name/SSN</b></td>
		</c:if>
		
        <!-- Division -->
<c:if test="${participantSummaryReportForm.showDivision ==true}">
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom"><report:sort field="division" direction="asc" formName="participantSummaryReportForm"><b>Division</b></report:sort></td>
</c:if>

        <!-- Age -->
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom"><report:sort field="age" direction="desc" formName="participantSummaryReportForm"><b>Age</b></report:sort></td>
          
        <!-- Status -->
          <td rowspan="3" class="dataheaddivider" valign="bottom"  width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
   		   <td  valign="bottom" colspan="3" align="center"><b>Status</b> </td> 
          
        <!-- Default date of birth -->
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom"><b>
			<% if (isReportCurrent) { %>
			  <report:sort field="defaultBirthDate" direction="desc" formName="participantSummaryReportForm">Default<br>date of<br>birth</b></report:sort>
			<% } else { %>
			  Default<br>date of<br>birth</b>
		  	<% } %>
          </td>

        <!-- Default enrollment -->
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom"><b>
			<% if (isReportCurrent) { %>
			  <report:sort field="investmentInstructionType" direction="desc" formName="participantSummaryReportForm">Investment<br>instruction type</b></report:sort>
			<% } else { %>
			  Investment<br>instruction type</b>
		  	<% } %>
          </td>
          
        <!-- Roth money -->
<c:if test="${participantSummaryReportForm.hasRothFeature ==true}">
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom"><b>
            <% if (isReportCurrent) { %>
              <report:sort field="rothInd" direction="desc" formName="participantSummaryReportForm">Roth<br>money</b></report:sort>
            <% } else { %>
              Roth<br>money</b>
            <% } %>
          </td>
</c:if>

        <!-- Employee assets -->
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<report:sort field="employeeAssets" direction="desc" formName="participantSummaryReportForm">Employee<br>assets($)</b></report:sort></NOBR></td>
          
        <!-- Employer assets -->
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<report:sort field="employerAssets" direction="desc" formName="participantSummaryReportForm">Employer<br>assets($)</b></report:sort></NOBR></td>
          
        <!-- Total assets -->
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<report:sort field="totalAssets" direction="desc" formName="participantSummaryReportForm">Total<br>assets($)</b></report:sort></NOBR></td>
          
        <!-- Outstanding loans -->
<c:if test="${participantSummaryReportForm.hasLoansFeature ==true}">
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td  rowspan="3" valign="bottom" align="right"><b><NOBR>&nbsp;&nbsp;&nbsp;<report:sort field="outstandingLoans" direction="desc" formName="participantSummaryReportForm">Outstanding<br>loans($)</b></report:sort></NOBR></td>
</c:if>
			
        <!-- Gateway -->
<c:if test="${participantSummaryReportForm.hasContractGatewayInd ==true}">
          <td rowspan="3" class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td rowspan="3" valign="bottom" align="left"><b>
            <%if(isReportCurrent){%>
              <report:sort field="participantGatewayInd" direction="desc" formName="participantSummaryReportForm">Guar.<br/> Income<br/>feature</report:sort></b>			
            <%}else{ %>
              <b>Guar.<br/> Income<br/> feature</b>
            <%}%>
          </td>
</c:if>
							<c:if test="${participantSummaryReportForm.hasManagedAccountInd}">
								<td rowspan="3" class="dataheaddivider" valign="bottom"
									width="1"><img src="/assets/unmanaged/images/s.gif"
									width="1" height="1"></td>
								<td rowspan="3" valign="bottom" align="left"><b> <%
 	if (isReportCurrent) {
 %>
										<report:sort field="managedAccountStatusInd" direction="desc"
											formName="participantSummaryReportForm">Mgd.<br /> Account<br />feature</report:sort></b>
									<%
										} else {
									%> <b>Mgd.<br /> Account<br /> feature
								</b> <%
 	}
 %></td>
							</c:if>
							<!-- spacer -->
          <td rowspan="3" class="databox"></td>
          <td rowspan="3" class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<%-- CL 110234 Begin--%>
		<tr class="tablesubhead">
			  <td class="dataheaddivider" colspan="1" valign="bottom"><img src="/assets/unmanaged/images/s.gif" height="1"></td>
		</tr>
		
<!-- table details second row starts -->
		<tr class="tablesubhead">
          
        <!-- Status -->
          <td align="left" valign="bottom" nowrap="nowrap" >
		  <% if (isReportCurrent) { %>
			  <report:sort field="employmentStatus" direction="desc" formName="participantSummaryReportForm"><b>Employment</b></report:sort> 
			<% } else { %>
			  <b>Employment</b>
		  	<% } %>
		  </td>
          <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>		   
   		  <td align="left" valign="bottom" nowrap="nowrap" >
		  <% if (isReportCurrent) { %>
			<report:sort field="contributionStatus" direction="desc" formName="participantSummaryReportForm"><b>Contribution</report:sort>  
			<% } else { %>
			  <b>Contribution</b>
		  	<% } %>
		  </td> 
	  	</tr>
	  	<%-- CL 110234 End--%>

<!-- Start of Details -->
<% boolean beigeBackgroundCls = false; // used to determine if the cell should use the background style
   boolean lastLineBkgrdClass = false; // used to determine if the last line should be beige or not
   boolean highlight = false;	// used to determine if the style class has to change
   boolean highlightBirthDate = false;	// used to determine if the style class has to change
   boolean highlightGateway = false;  // used to determine if the style class has to change
%>

<c:if test="${empty param.printFriendly}" >
<%
	if (!StringUtils.isEmpty(participantSummaryReportForm.getNamePhrase()) ||
		!StringUtils.isEmpty(participantSummaryReportForm.getStatus()) ||
		!StringUtils.isEmpty(participantSummaryReportForm.getEmploymentStatus()) ||
		!StringUtils.isEmpty(participantSummaryReportForm.getDivision()) ||
		!participantSummaryReportForm.getSsn().isEmpty() ||
		participantSummaryReportForm.getGatewayChecked() || 
		participantSummaryReportForm.getManagedAccountChecked())  
	{
%>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <c:choose>
		 
		  <c:when test="${isMozilla == true}">
				<td style = 'padding-left: 5px;padding-right: 5px;'>&nbsp;</td>
		  </c:when>
		  <c:otherwise>
				<td width="21px">&nbsp;</td>
		  </c:otherwise>
          </c:choose>
          <td class="datadivider"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top" colspan="<%=columnSpanLess4%>">

		<% if (theReport.getDetails() != null && theReport.getDetails().size() > 0) { // we have search results %>
			<content:getAttribute id="viewAllParticipants" attribute="text">
				<content:param>/do/participant/participantSummary</content:param>
			</content:getAttribute>
		
		<% } else { // no results %>
			<!-- no results -->
			
			<c:if test="${empty pageScope.errorsExist}">
			<content:getAttribute id="noSearchResults" attribute="text">
			<!-- no errors and no results -->
				<content:param>/do/participant/participantSummary</content:param>
			</content:getAttribute>
			</c:if>
		<% 
		   }
		   lastLineBkgrdClass = true;
		 %>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
<%
	}
%>

</c:if>

<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex">
 <c:set var="indexValue" value="${theIndex.index}"/> 
				<% 				
				    String temp = pageContext.getAttribute("indexValue").toString();
				if (Integer.parseInt(temp) % 2 != 0) {
					
	 	beigeBackgroundCls = true;
	 	lastLineBkgrdClass = true; %>
        <tr class="datacell1">
<% } else {
		beigeBackgroundCls = false;
		lastLineBkgrdClass = false; %>
    	<tr class="datacell2">
<% } %>
<% ParticipantSummaryDetails theRecord = (ParticipantSummaryDetails)(pageContext.getAttribute("theItem")); 
// change class if Default Investment says Yes or No
//highlight = isReportCurrent && Character.toUpperCase(theItem.getDefaultInvestment().trim().charAt(0)) == 'Y';

// change class if Default Birthdate says Yes or No
 highlightBirthDate = isReportCurrent && Character.toUpperCase(theRecord.getDefaultBirthDate().trim().charAt(0)) == 'Y';

 highlightGateway = isReportCurrent && Character.toUpperCase(theRecord.getParticipantGatewayInd().trim().charAt(0)) == 'Y'; 

String action = "/do/participant/participantAccount/?selectedAsOfDate=" + participantSummaryReportForm.getAsOfDate();
%>

        <!-- history icon --> 
          <td class="databorder" width="1"></td>
		<c:if test="${empty param.printFriendly }" >
          <td style = 'padding-left: 5px; padding-right: 5px'>
            <ps:link action="/do/transaction/participantTransactionHistory/" paramId="profileId" paramName="theItem" paramProperty="profileId">
              <img src="/assets/unmanaged/images/history_icon.gif" alt="Participant transaction history"  border="0">
            </ps:link>
          </td>
        </c:if>
        <c:if test="${not empty param.printFriendly }" >
          <td>&nbsp;</td>
        </c:if>
        
        <!-- Name/SSN -->
          <td class="datadivider" width="1"></td>
          <td>
            <ps:link action="<%=action%>" paramId="profileId" paramName="theItem" paramProperty="profileId">
${theItem.lastName},
${theItem.firstName}<br>
			  <render:ssn property="theItem.ssn" />
            </ps:link>
          </td>

        <!-- Division -->
<c:if test="${participantSummaryReportForm.showDivision ==true}">
          <td class="datadivider" width="1"></td>
<td nowrap="nowrap">${theItem.division}</td>
</c:if>
        
        <!-- Age -->	
          <td class="datadivider" width="1"></td>
          <td align="right"><render:number property="theItem.age" defaultValue="" type="i" /></td>
<%-- CL 110234 Begin--%>
        <!-- Employment Status -->		  
		  <td class="datadivider" width="1"></td>
          <td><NOBR>
		     <c:if test="${not empty theItem.employmentStatus}">
		       ${theItem.getEmploymentStatusDescription()}
		       </c:if>
		       </NOBR>
		     
          </td>
<%-- CL 110234 End--%>
		 <!-- Status -->
          <td class="datadivider" width="1"></td>
          <td>${theItem.getStatus()}</td>
		  
        <!-- Default date of birth -->
		  <td class="datadivider" width="1"></td>
          <td <% if (highlightBirthDate) { %> class="highlight" <% } %> >
${theItem.defaultBirthDate}
          </td>

        <!-- Investment Instruction Type -->
        <c:if test="${not empty param.printFriendly }" >
          <td class="datadivider" width="1"></td>
          <td>
            ${theItem.getInvestmentInstructionType()}
          </td>
		</c:if>
		<c:if test="${empty param.printFriendly}" >
			<td class="datadivider" width="1"></td>
          		<td onmouseover="tooltip('<%=theRecord.getInvestmentInstructionType()%>')" onmouseout="UnTip()">
            	${theItem.getInvestmentInstructionType()}
            </td>
        </c:if>

		<!-- Roth money -->
<c:if test="${participantSummaryReportForm.hasRothFeature ==true}">
          <td class="datadivider" width="1"></td>
          <td><NOBR>${theItem.getRothInd()}</NOBR></td>
</c:if>

        <!-- Employee assets -->
          <td class="datadivider" width="1"></td>
          <td align="right"><% if (theRecord.getEmployeeAssets() < 0) {%> * <% } else { %><render:number property='theItem.employeeAssets' defaultValue = "0.00"  pattern="###,###,##0.00"/><% } %></td>
          
        <!-- Employer assets -->
          <td class="datadivider" width="1"></td>
          <td align="right"><% if (theRecord.getEmployerAssets() < 0) {%> * <% } else { %><render:number property='theItem.employerAssets' defaultValue = "0.00"  pattern="###,###,##0.00"/><% } %></td>
          
        <!-- Total assets -->
          <td class="datadivider" width="1"></td>
          <td align="right"><% if (theRecord.getTotalAssets() < 0) {%> * <% } else { %><render:number property='theItem.totalAssets' defaultValue = "0.00"  pattern="###,###,##0.00"/><% } %></td>

        <!-- Outstanding loans -->
<c:if test="${participantSummaryReportForm.hasLoansFeature ==true}">
          <td class="datadivider" width="1"></td>
          <td <% if (highlight) { %> class="highlight" <% } %> align="right">
			<% // hyphen if loans has "0.0" value
			if (theRecord.getOutstandingLoans() != 0.0) { %>
			  <render:number property='theItem.outstandingLoans' defaultValue = ""  pattern="###,###,##0.00"/>
			<% } else { %>
			  <!-- hyphen if loans has no value --> -
			<% } %>
          </td>
</c:if>
        
        <!-- Gateway -->
<c:if test="${participantSummaryReportForm.hasContractGatewayInd ==true}">
        
	        <%  boolean liaIndicator = false;
				boolean participantGIFLInd = false;
			%>
<c:if test="${theItem.showLIADetailsSection ==true}">
				<%  liaIndicator = true;%>
</c:if>
			
          <td class="datadivider" width="1"></td>
          <% 
          if (isReportCurrent) { %>
          
<c:if test="${theItem.participantGatewayInd ==Yes}">
				 	<%  participantGIFLInd = true;%>
</c:if>
				 
	          <td <% if (highlightGateway) { %> class="highlight" <% } %>> 
	          <% if(participantGIFLInd && liaIndicator) {%>
${theItem.participantGatewayInd}/${commonConstants.LIA_IND_TEXT}
	          	<% }  else { %>
${theItem.participantGatewayInd}
	          	<% } %> </td>
	       <% }  else { %> <td>
	       
<c:if test="${theItem.defaultGateway ==Yes}">
					 	<%  participantGIFLInd = true;  %>
</c:if>
				
	          	<% if(participantGIFLInd && liaIndicator) {%>
${theItem.defaultGateway}/LIA
	          	<% }  else { %>
${theItem.defaultGateway}
	          <% }  %> </td> 
          <% } %>
</c:if>
									<!-- ManagedAccount -->
									<c:if
										test="${participantSummaryReportForm.hasManagedAccountInd}">
										<td class="datadivider" width="1"></td>
										<td nowrap="nowrap">${theItem.managedAccountStatusInd}</td>
									</c:if>
     
        <!-- spacer -->
        <% if (beigeBackgroundCls) { %>
          <td class="dataheaddivider">
        <% } else { %>
          <td class="beigeborder">
        <% } %>
            <img src="/assets/unmanaged/images/s.gif" width="1" height="1">
          </td>
          <td class="databorder" width="1"></td>
        </tr>
</c:forEach>
</c:if>

<!-- End of Details -->

<!-- Start of last line -->
<!-- let the last line have the same background colour as the previous line -->
<% if (lastLineBkgrdClass) { %>
	<tr class="datacell1">
<% } else { %>
	<tr class="datacell2">
<% } %>
        <!-- History icon -->
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>    
          <td class="lastrow"></td>
          
        <!-- Name/SSN -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        <!-- Division -->
<c:if test="${participantSummaryReportForm.showDivision ==true}">
          <td class="datadivider"></td>
          <td class="lastrow"></td>
</c:if>
          
        <!-- Age -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        <!-- Employment Status --> 
          <td class="datadivider"></td>
          <td class="lastrow"></td>
        
		<!-- Contibution Status --> 
		  <td class="datadivider"></td>
          <td class="lastrow"></td>
		
        <!-- Default date of birth -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        <!-- Default enrollment -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>

		<!-- Roth money -->
<c:if test="${participantSummaryReportForm.hasRothFeature ==true}">
          <td class="datadivider"></td>
          <td class="lastrow"></td>
</c:if>

        <!-- Employee assets -->
		  <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        <!-- Employer assets -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>
          
        <!-- Total assets -->
          <td class="datadivider"></td>
          <td class="lastrow"></td>

        <!-- Outstanding loans -->
<c:if test="${participantSummaryReportForm.hasLoansFeature ==true}">
          <td class="datadivider"></td>
          <td class="lastrow"></td>
</c:if>

        <!-- Gateway -->
<c:if test="${participantSummaryReportForm.hasContractGatewayInd ==true}">
          <td class="datadivider"></td>
          <td class="lastrow"></td>
</c:if>
							<!-- Managed Account -->
							<c:if test="${participantSummaryReportForm.hasManagedAccountInd}">
								<td class="datadivider"></td>
								<td class="lastrow"></td>
							</c:if>

        <!-- spacer -->
          <td class="datadivider"></td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="4"></td>

        </tr>

		<!-- End of Last line -->
        <tr><td class="databorder" colspan="<%=columnSpan%>"></td></tr>   
		<c:if test="${empty param.printFriendly}" >
		</table>
		<c:if test="${isIE eq true}">
		<table>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
			<tr><td></td></tr>
		</table>
		</c:if>
		</div>         
		</c:if>	
        <tr><td colspan="<%=columnSpan%>" align="right"><report:pageCounter report="theReport" arrowColor="black" formName="participantSummaryReportForm"/></td></tr>
        <tr>
          <td colspan="<%=columnSpan%>">
		<c:if test="${empty param.printFriendly}" >
			<table>
			<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td>         
		</c:if>	
            <br>
            <p><content:pageFooter beanName="layoutPageBean"/></p>
            <p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
            <p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
		<c:if test="${empty param.printFriendly}" >
			</td>
			</tr>
			</table>
		</c:if>	
          </td>
        </tr>	
      </ps:form>
      </table>
      <br>
    </td>

    <!-- column to the right of the report -->
    <td width="15"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

  <c:if test="${not empty param.printFriendly}" >
    <content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
            type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
            id="globalDisclosure"/>

    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
      </tr>
    </table>
  </c:if>

<script>

var elementHisIconBorder = document.getElementById('dHisIconBorder');
var elementHisIconCell = document.getElementById('dHisIconCell');
var elementNameIconBorder = document.getElementById('dNameIconBorder');
var elementNameIconCell = document.getElementById('dNameIconCell');
if (elementNameIconCell == null)
	elementNameIconCell = document.getElementById('dNameIconCellNotPresent');
if (elementHisIconBorder != null && elementHisIconCell != null && elementNameIconBorder != null && elementNameIconCell != null) {
	var positionInfo = elementHisIconBorder.getBoundingClientRect();
	var dHBorderWidth = positionInfo.width;
	positionInfo = elementHisIconCell.getBoundingClientRect();
	var dHCellWidth = positionInfo.width;
	positionInfo = elementNameIconBorder.getBoundingClientRect();
	var dNBorderWidth = positionInfo.width;
	positionInfo = elementNameIconCell.getBoundingClientRect();
	var dNCellWidth = positionInfo.width;
	var fTableWidth = dHBorderWidth + dHCellWidth + dNBorderWidth + dNCellWidth-1;
	var style = "overflow: hidden; z-index: 3; visibility: hidden; position: absolute; width: "+fTableWidth+"px;";
	if (document.getElementById('fixedTable') != null)
		document.getElementById('fixedTable').setAttribute("style","width:"+fTableWidth+style);
}

</script>
  
<%-- CL 110234 Begin--%>
<script type="text/javascript" >

<c:if test="${empty param.printFriendly}" >
	showOrHideStatus();
</c:if>

<c:if test="${not empty param.printFriendly}" >
<c:if test="${participantSummaryReportForm.asOfDateCurrent ==false}">
		document.getElementById("participantStatus").style.display = "none";
		document.getElementById("participantStatus").style.display = "none";
		document.getElementById("participantEmpStatus").style.display = "none";
		document.getElementById("participantEmpStatus").style.display = "none";
</c:if>
</c:if>

</script>  
<%-- CL 110234 End--%>
