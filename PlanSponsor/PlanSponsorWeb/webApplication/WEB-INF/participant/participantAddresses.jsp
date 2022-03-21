<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.participant.ParticipantAddressesReportForm" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%> 
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@page import="com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails"%>
<%@ page import="com.manulife.pension.ps.service.report.participant.valueobject.ParticipantAddressesReportData" %>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="/WEB-INF/ps-report.tld" prefix="report" %>
<%@ taglib uri="/WEB-INF/render.tld" prefix="render" %>
<%@ taglib uri="/WEB-INF/psweb-taglib" prefix="ps" %>
<%@ taglib uri="/WEB-INF/content-taglib.tld" prefix="content" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">

<%boolean errors = false;%>
<%
ParticipantAddressesReportData theReport = (ParticipantAddressesReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

%>
<%
ParticipantAddressesReportForm participantAddressesReportForm = (ParticipantAddressesReportForm)session.getAttribute("participantAddressesReportForm");
pageContext.setAttribute("participantAddressesReportForm",participantAddressesReportForm,PageContext.PAGE_SCOPE);
%>

<content:contentBean contentId="<%=ContentConstants.MESSAGE_EMPLOYEES_NO_SEARCH_RESULTS%>"
                                   type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
                                   id="noSearchResults"/>


<c:if test="${empty param.printFriendly}" >
<script type="text/javascript" >
// Called when add employee is clicked
function doAdd() {
     // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
      window.location="/do/census/addEmployee/?source=addressSummary";
      return true;
    }
}

// Called when reset is clicked
function doReset() {
	document.participantAddressesReportForm.task.value = "reset";
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



	if (document.participantAddressesReportForm.ssnOne) {
			if (document.participantAddressesReportForm.ssnOne.value.length >= 0){
					document.participantAddressesReportForm.namePhrase.value = "";
			}
	}


	if (document.participantAddressesReportForm.ssnTwo) {
			if (document.participantAddressesReportForm.ssnTwo.value.length >= 0){
				document.participantAddressesReportForm.namePhrase.value = "";
			}
	}

	if (document.participantAddressesReportForm.ssnThree) {
			if (document.participantAddressesReportForm.ssnThree.value.length >= 0){
				document.participantAddressesReportForm.namePhrase.value = "";
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


	if (document.participantAddressesReportForm.namePhrase) {
			if (document.participantAddressesReportForm.namePhrase.value.length >= 0){
				document.participantAddressesReportForm.ssnOne.value = "";
				document.participantAddressesReportForm.ssnTwo.value = "";
				document.participantAddressesReportForm.ssnThree.value = "";
			}
	}

}

</script>
</c:if>

<% int numberOfColumns = 17; %>
<c:if test="${empty param.printFriendly }" >
<% numberOfColumns = 19; %>
</c:if>

<c:set var="showDivColumn"><%=userProfile.getCurrentContract().hasSpecialSortCategoryInd()%></c:set>
<c:if test="${showDivColumn eq false}">
<% numberOfColumns = numberOfColumns - 2; %>
</c:if>

   	<p>
   		 	<c:if test="${sessionScope.psErrors ne null}">
<c:set var="errorsExist" value="true" scope="page" />
        	<div id="errordivcs"><content:errors scope="session"/></div>
        	<%errors=true;%>
        	</c:if>
    </p>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<ps:form method="POST" modelAttribute="participantAddressesReportForm" name="participantAddressesReportForm" action="/do/participant/participantAddresses">
<input type="hidden" name="task" value="filter"/>
	
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    
	    <%-- TAB section --%>
    	<tr>
      		<td valign="bottom" colspan="4">
      			<DIV class="nav_Main_display" id="div">
      			<UL class="">
        			<LI id="tab1" onmouseover="this.className='off_over';" onmouseout="this.className='';">
        			<c:if test="${empty param.printFriendly }" >
			          	<A href="/do/census/censusSummary/">
			        </c:if>
			        Summary
			        <c:if test="${empty param.printFriendly }" >
			        	</A>
			        </c:if>
			        </LI>
			        <LI id="tab2" class="on">Addresses</LI>
        			<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
        			<c:if test="${empty param.printFriendly }" >
        				<A href="/do/census/employeeEnrollmentSummary">
        			</c:if>
        			Eligibility
        			<c:if test="${empty param.printFriendly }" >
			        	</A>
			        </c:if>
        			</LI>


<c:if test="${participantAddressesReportForm.allowedToAccessDeferralTab ==true}">
        			  <LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
		                <c:if test="${empty param.printFriendly }" >
		                	<A href="/do/census/deferral">
		                </c:if>
		                Deferrals
		                <c:if test="${empty param.printFriendly }" >
		                	</A>
		                </c:if>
		              </LI>
</c:if>

<c:if test="${participantAddressesReportForm.allowedToAccessVestingTab ==true}">
        			<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
        			<c:if test="${empty param.printFriendly }" >
        				<A href="/do/census/censusVesting/">
        			</c:if>
        			Vesting
        			<c:if test="${empty param.printFriendly }" >
			        	</A>
			        </c:if>
        			</LI>
</c:if>

      			</UL>
      			</DIV>
      		</td>
    	</tr>
    	<tr>
			<td colspan="4" height="30" class="tablesubhead"><b>Employee Address Search</b></td>
		</tr>
		<tr>
            <td colspan="4" valign="top" class="filterSearch">
            To search for an employee by last name or SSN, make your selection below and click "search" to complete your request.
            </td>
        </tr>
        <tr>
            <td width="220" valign="top" class="filterSearch"><b>Segment</b><br>
                <c:if test="${not empty param.printFriendly}" >
<form:select path="segment" disabled="true" >
<form:options items="${participantAddressesReportForm.segmentList}" itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
			    <c:if test="${empty param.printFriendly}" >
		<form:select path="segment" tabindex="10">
<form:options items="${participantAddressesReportForm.segmentList}" itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch"><ps:label fieldId="lastName" mandatory="false"><b>Last name</b></ps:label><br>
                <c:if test="${not empty param.printFriendly}" >
<form:input path="namePhrase" maxlength="20" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${empty param.printFriendly}">
<form:input path="namePhrase" maxlength="20" onkeyup="clearSSN(event);" cssClass="inputField" tabindex="20"/>
			    </c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch">
            	<ps:label fieldId="ssn" mandatory="false"><b>SSN</b></ps:label><br>
                <c:if test="${not empty param.printFriendly}" >
                <form:password path="ssnOne" styleClass="inputField" readonly="true" value="${participantAddressesReportForm.ssnOne}"
						onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
				<form:password path="ssnTwo" styleClass="inputField" readonly="true" value="${participantAddressesReportForm.ssnTwo}"
						onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2"/>
				<form:input path="ssnThree" autocomplete="off" styleClass="inputField" readonly="true"
						onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4"/>
				</c:if>
				<c:if test="${empty param.printFriendly}">
				<form:password path="ssnOne" styleClass="inputField" onkeypress="clearName(event);" value="${participantAddressesReportForm.ssnOne}"
						onkeyup="clearName(event); return autoTab(this, 3, event);" size="3" maxlength="3" tabindex="30"/>
				<form:password path="ssnTwo" styleClass="inputField" onkeypress="clearName(event);" value="${participantAddressesReportForm.ssnTwo}"
						onkeyup="clearName(event); return autoTab(this, 3, event);" size="3" maxlength="3" tabindex="30"/>
				<form:input path="ssnThree" autocomplete="off" styleClass="inputField"  onkeypress="clearName(event);"
						onkeyup="clearName(event); return autoTab(this, 4, event);" size="4" maxlength="4" tabindex="32"/>
				</c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch">
            <c:if test="${showDivColumn eq true}">
            	<b>Division</b><br>
                <c:if test="${not empty param.printFriendly}" >
<form:input path="division" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${empty param.printFriendly}" >
<form:input path="division" cssClass="inputField" tabindex="40"/>
			    </c:if>
</c:if>&nbsp;
            </td>
        </tr>
	    <tr>
	        <td valign="top" class="filterSearch">
	        <c:if test="${empty param.printFriendly}" >
	        	<a href="/do/participant/addressHistory">Address History</a>
	        </c:if>
	        </td>
	        <td valign="top" class="filterSearch"><b>Employment status</b><br>
	        <c:if test="${not empty param.printFriendly}" >
<form:select path="status" disabled="true" >
				<%-- set the first value of the select --%>
				<form:option value="All">All</form:option>
<form:options items="${participantAddressesReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select>
			</c:if>
			<c:if test="${empty param.printFriendly }" >
			<form:select path="status" tabindex="50" >
			    <%-- set the first value of the select --%>
				<form:option value="All">All</form:option>
<form:options items="${participantAddressesReportForm.statusList}" itemLabel="label" itemValue="value"/>
				</form:select>
			</c:if>
	        </td>
	        <td valign="top" class="filterSearch">&nbsp;</td>
	        <td valign="middle" class="filterSearch"><div align="left">
	        <c:if test="${empty param.printFriendly}" >
	        	<input type="submit" name="submit" value="search" tabindex="60"/>
	        	<input type="submit" name="reset" value="reset" tabindex="70" onclick="return doReset();"/>
			</c:if>&nbsp;
	        </div></td>
	    </tr>
	    <tr>
	    	<td colspan="4" class="filterSearch"><img src="/assets/unmanaged/images/s.gif" width="1" height="5"></td>
	    </tr>
	   </table>
		
	   <table width="100%" border="0" cellspacing="0" cellpadding="0">
	    <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <c:if test="${empty param.printFriendly}" >
	          <td width="30"><IMG src="/assets/unmanaged/images/s.gif" width="30" height="1"></td>
	          <td width="1"><IMG src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
          <td width="150"><img src="/assets/unmanaged/images/s.gif" width="150" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="150"><img src="/assets/unmanaged/images/s.gif" width="150" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="45"><img src="/assets/unmanaged/images/s.gif" width="45" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="40"><img src="/assets/unmanaged/images/s.gif" width="40" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="70"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td width="65"><img src="/assets/unmanaged/images/s.gif" width="65" height="1"></td>
          <c:if test="${showDivColumn eq true}">
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td width="70"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
            <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td width="70"><img src="/assets/unmanaged/images/s.gif" width="70" height="1"></td>
</c:if>
          <c:if test="${showDivColumn eq false}">   
          	<td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td width="141"><img src="/assets/unmanaged/images/s.gif" width="141" height="1"></td>
</c:if>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    </tr>
	    
	    <tr><td colspan="<%=numberOfColumns%>">&nbsp;</td></tr>
        <c:if test="${empty param.printFriendly }" >
        <tr><td colspan="<%=numberOfColumns%>" align="left">
        <strong>Legend</strong>:&nbsp;<img src="/assets/unmanaged/images/view_icon.gif" width="12" height="12"/>&nbsp;View&nbsp;<img src="/assets/unmanaged/images/edit_icon.gif" width="12" height="12" />&nbsp;Edit
        </td></tr>
        </c:if>
	   
        <tr class="tablehead">
            <td class="tableheadTD1" colspan="<%=numberOfColumns%>">         
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
	            <td class="tableheadTD" width="50%">&nbsp;</td>
	            <% if (errors==false) { %>
	            <td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Employees"/></b></td>
	            <td align="right" class="tableheadTDinfo"><report:pageCounter formName="participantAddressesReportForm" report="theReport"/></td>
              	<% } else { %>
              	<td class="tableheadTDinfo"></td>
	            <td align="right" class="tableheadTDinfo"></td>
	            <% } %>
              </tr>
            </table>
            </td>
        </tr>

        <tr class="tablesubhead">
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <c:if test="${empty param.printFriendly }" >
		  	<td valign="top" width="30"><b>Action</b></td>
		  	<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  </c:if>
	      <td valign="top"><report:sort field="lastName" direction="asc" formName="participantAddressesReportForm"><b>Name</b></report:sort></td>
          <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><b>Address</b></td>
          <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><report:sort field="city" direction="asc" formName="participantAddressesReportForm"><b>City</b></report:sort></td>
          <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><report:sort field="stateCode" direction="asc" formName="participantAddressesReportForm"><b>State</b></report:sort></td>
          <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><report:sort field="zip" direction="asc" formName="participantAddressesReportForm"><b>Zip</b></report:sort></td>
          <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><report:sort field="country" direction="asc" formName="participantAddressesReportForm"><b>Country</b></report:sort></td>
          <c:if test="${showDivColumn eq true}">
		    <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
            <td valign="top"><report:sort field="division" direction="asc" formName="participantAddressesReportForm"><b>Division</b></report:sort></td>		  
            <td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td valign="top"><report:sort field="status" direction="asc" formName="participantAddressesReportForm"><b>Employment status</b></report:sort></td>
</c:if>
          <c:if test="${showDivColumn eq false}">
          	<td class="dataheaddivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<td valign="top"><report:sort field="status" direction="asc" formName="participantAddressesReportForm"><b>Employment status</b></report:sort></td>
</c:if>
          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		
		<% if (theReport.getDetails() == null || theReport.getDetails().size() == 0) { // we have no results %>
        <tr class="datacell1">
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <td valign="top" colspan="<%=numberOfColumns - 2%>">
			<content:getAttribute id="noSearchResults" attribute="text"/>
		  </td>
          <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
        </tr>
		<% } %>
		
		<%if(errors==false) {
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy");
		%>
		<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >
<%CensusSummaryDetails theItem=(CensusSummaryDetails)pageContext.getAttribute("theItem"); %>
<c:set var="indexValue" value="${theIndex.index}"/>
			<%String temp = pageContext.getAttribute("indexValue").toString(); 
			if (Integer.parseInt(temp)% 2 == 0) { %>
	        <tr class="datacell3">
			<% } else { %>
	        <tr class="datacell1">
			<% } %>
			<% boolean missingAddress = (theItem.getAddressLine1() == null) ||  theItem.getAddressLine1().trim().length() == 0; %>

          	<td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	<c:if test="${empty param.printFriendly}" >
	          <td align="center"> <report:actions profile="userProfile" item="theItem" action="editParticipantAddress"/></td>
	          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          	</c:if>
          	<td valign="top" <% if (missingAddress) { %> class="highlight" <% } %> >
<c:if test="${userProfile.welcomePageAccessOnly == false}" >
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
<c:if test="${userProfile.welcomePageAccessOnly == true}" >
${theItem.wholeName}
</c:if>
	        <br>
            <render:ssn property="theItem.ssn"/>
          	</td>
          	<td class="datadivider" valign="top" width="1" height="1"></td>
          	<td valign="top" 
          	<% if (missingAddress) { %> class="highlight">Address missing
          	<% } else { %>
          	> <%=theItem.getAddressLine1()%><br><%=theItem.getAddressLine2()%> 
	        <% } %>
	        </td>
	        <td class="datadivider" valign="top" width="1" height="1"></td>
<td valign="top">${theItem.city}</td>
          	<td class="datadivider" valign="top" width="1" height="1"></td>
<td valign="top">${theItem.stateCode}</td>
          	<td class="datadivider" valign="top" width="1" height="1"></td>
<td valign="top">${theItem.formattedZipCode}</td>
          	<td class="datadivider" valign="top" width="1" height="1"></td>
<td valign="top">${theItem.country}</td>
          	<c:if test="${showDivColumn eq true}">
          	  <td class="datadivider" valign="top" width="1" height="1"></td>
<td>${theItem.division}</td>
          	  <td class="datadivider" valign="top" width="1" height="1"></td>
          	  <td><ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="theItem" property="status"/>
          	  <c:if test="${not empty theItem.employeeStatusDate}">
              <br/>
              <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.employeeStatusDate"/>
              </c:if>
          	  </td>
</c:if>
          	<c:if test="${showDivColumn eq false}">
          	  <td class="datadivider" valign="top" width="1" height="1"></td>
          	  <td><ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="theItem" property="status"/>
          	  <c:if test="${not empty theItem.employeeStatusDate}">
              <br/>
              <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.employeeStatusDate"/>
              </c:if>
          	  </td>
</c:if>
	        <td class="databorder"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	        </tr>
</c:forEach>
		</c:if>
		<% } %>

        <tr><td class="databorder" colspan="<%=numberOfColumns%>"></td></tr>
		<tr>
			<td colspan="<%=numberOfColumns%>">         
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
		        <td width="50%"></td>
	    		<% if (errors==false) { %>
	        	<td><b><report:recordCounter report="theReport" label="Employees"/></b></td>
	        	<td align="right"><report:pageCounter formName="participantAddressesReportForm" report="theReport" arrowColor="black"/></td>
        		<% } else { %>
              	<td colspan="2"></td>
	            <% } %>
		    </tr>
		    </table>
            </td>
		</tr>
	
		<tr>
	  		<td colspan="<%=numberOfColumns%>">
			<br>
			<p><content:pageFooter beanName="layoutPageBean"/></p>
	 		<p class="footnote"><content:pageFootnotes beanName="layoutPageBean"/></p>
	 		<p class="disclaimer"><content:pageDisclaimer beanName="layoutPageBean" index="-1"/></p>
	 		</td>
        </tr>
      </table>
     </ps:form>
    </td>
  </tr>
</table>

<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
        id="globalDisclosure"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>

