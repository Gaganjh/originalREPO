<%-- taglib used --%>

<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.census.CensusSummaryReportForm" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.CensusSummaryDetails" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.pension.service.employee.util.EmployeeValidationErrorCode" %>
<%@ page import="com.manulife.pension.service.vesting.VestingConstants" %>
<%@ page import="com.manulife.pension.ps.service.report.contract.valueobject.ContractInformationReportData"%>
<%@ page import="com.manulife.pension.service.employee.util.EmployeeValidationError"%> 



<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%
CensusSummaryReportData theReport = (CensusSummaryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);
%>


<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<%
CensusSummaryReportForm censusSummaryReportForm = (CensusSummaryReportForm)session.getAttribute("censusSummaryReportForm");
pageContext.setAttribute("censusSummaryReportForm",censusSummaryReportForm,PageContext.PAGE_SCOPE);
%>



             
<content:contentBean contentId="<%=ContentConstants.MESSAGE_EMPLOYEES_NO_SEARCH_RESULTS%>"
                    type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="noSearchResults"/>
                    
<content:contentBean contentId="<%=ContentConstants.WARNING_MISSING_BIRTH_DATE%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="missingBirthDate"/>
<content:contentBean contentId="<%=ContentConstants.BLANK_ELIGIBILITY_WARNING%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="missingEligibInd"/>
<content:contentBean contentId="<%=ContentConstants.ELIGIBILITY_DATE_INDICATOR_WARNING%>"
                    type="<%=ContentConstants.TYPE_MESSAGE%>" id="missingEligibDate"/>
                                
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
                    
                       
<c:set var="missingBirthDateWarning" ><content:getAttribute id="missingBirthDate" attribute="text"/></c:set> 
<c:set var="missingEligibIndWarning" ><content:getAttribute id="missingEligibInd" attribute="text"/></c:set> 
<c:set var="missingEligibDateWarning" ><content:getAttribute id="missingEligibDate" attribute="text"/></c:set> 
<c:set var="missingVYOSAndHOSWarning" ><content:getAttribute id="missingVYOSAndHOS" attribute="text"/></c:set> 
<c:set var="missingDateOfHireWarning" ><content:getAttribute id="missingDateOfHire" attribute="text"/></c:set> 
<c:set var="missingEmploymentStatusWarning" ><content:getAttribute id="missingEmploymentStatus" attribute="text"/></c:set> 
<c:set var="vyosDateBirthDateWarning" ><content:getAttribute id="vyosDateBirthDate" attribute="text"/></c:set> 
<c:set var="minimumVyosDateWarning" ><content:getAttribute id="minimumVyosDate" attribute="text"/></c:set>                          


<%boolean errors = false;%>


<c:if test="${empty param.printFriendly}">
<script type="text/javascript" >

// Called when add employee is clicked
function doAdd() {
     // Check for changes in the form and return with no submit if required
    if(discardChanges("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>") == false) {      
      return false;
    } else {    
      window.location="/do/census/addEmployee/?source=censusSummary";
      return true;
    }
}
  
// Called when reset is clicked
function doReset() {
	document.censusSummaryReportForm.task.value = "reset";
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
	
	if (document.censusSummaryReportForm.ssnOne) {
			if (document.censusSummaryReportForm.ssnOne.value.length >= 0){
					document.censusSummaryReportForm.namePhrase.value = "";
			}
	}


	if (document.censusSummaryReportForm.ssnTwo) {
			if (document.censusSummaryReportForm.ssnTwo.value.length >= 0){	
				document.censusSummaryReportForm.namePhrase.value = "";
			}
	}

	if (document.censusSummaryReportForm.ssnThree) {
			if (document.censusSummaryReportForm.ssnThree.value.length >= 0){	
				document.censusSummaryReportForm.namePhrase.value = "";
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
	
	
	if (document.censusSummaryReportForm.namePhrase) {
			if (document.censusSummaryReportForm.namePhrase.value.length >= 0){
				document.censusSummaryReportForm.ssnOne.value = "";
				document.censusSummaryReportForm.ssnTwo.value = "";
				document.censusSummaryReportForm.ssnThree.value = "";
			}	
	}
	
}
</script>
</c:if>

<% int numberOfColumns = 13; %>
<c:if test="${empty param.printFriendly }" >
<% numberOfColumns = 15; %>
</c:if>

<c:set var="showDivColumn" value="${userProfile.getCurrentContract().hasSpecialSortCategoryInd()}"/>
<c:if test="${showDivColumn eq 'false'}">
<% numberOfColumns = numberOfColumns - 2; %>
</c:if>

     <p>
       	<c:if test="${sessionScope.psErrors ne null}">
<c:set var="errorsExist" value="true" scope="page" />   
        <div id="errordivcs"><content:errors scope="session"/></div>
        <%errors=true;%>
        </c:if>
     </p>

<!-- Beginning of census Summary report body -->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<ps:form action="/do/census/censusSummary/" modelAttribute="censusSummaryReportForm" name="censusSummaryReportForm" >

		
<input type="hidden" name="task" value="filter"/>
        
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
        
        <%-- TAB section --%>
    	<tr>
      		<td valign="bottom" colspan="4">
      			<DIV class="nav_Main_display" id="div">
      			<UL class="">
        			<LI id="tab1" class="on">Summary</LI>
			        <LI id="tab2" onmouseover="this.className='off_over';" onmouseout="this.className='';">
			        <c:if test="${empty param.printFriendly}" >
			          	<A href="/do/participant/participantAddresses">
			        </c:if>
			        Addresses
			        <c:if test="${empty param.printFriendly}" >
			        	</A>
			        </c:if>
			        </LI>
        			<LI id="tab3" onmouseover="this.className='off_over';" onmouseout="this.className='';">
        			<c:if test="${empty param.printFriendly}" >
        				<A href="/do/census/employeeEnrollmentSummary">
        			</c:if>
        			Eligibility
        			<c:if test="${empty param.printFriendly}" >
			        	</A>
			        </c:if>
        			</LI>

<c:if test="${censusSummaryReportForm.allowedToAccessDeferralTab ==true}">
        			<LI id="tab4" onmouseover="this.className='off_over';" onmouseout="this.className='';">
                    <c:if test="${empty param.printFriendly}" >
                    	<A href="/do/census/deferral">
                    </c:if>
                    Deferrals
                    <c:if test="${empty param.printFriendly}" >
                    	</A>
                    </c:if>
                    </LI>
</c:if>

<c:if test="${censusSummaryReportForm.allowedToAccessVestingTab ==true}">
        			<LI id="tab5" onmouseover="this.className='off_over';" onmouseout="this.className='';">
        			<c:if test="${empty param.printFriendly}" >
        				<A href="/do/census/censusVesting/">
        			</c:if>
        			Vesting
        			<c:if test="${empty param.printFriendly}" >
			        	</A>
			        </c:if>
        			</LI>
</c:if>


      			</UL>
      			</DIV>
      		</td>
    	</tr>
    	<tr>
			<TD colspan="4" height="25" class="tablesubhead"><b>Employee Summary Search</b></TD>
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
<form:options items="${censusSummaryReportForm.segmentList}" itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
			    <c:if test="${empty param.printFriendly}">
				<form:select name="censusSummaryReportForm" path="segment" tabindex="10">
<form:options items="${censusSummaryReportForm.segmentList}" itemLabel="label" itemValue="value"/>
				</form:select>
			    </c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch"><ps:label fieldId="lastName" mandatory="false"><b>Last name</b></ps:label><br>
                <c:if test="${not empty param.printFriendly}">
<form:input path="namePhrase" maxlength="20" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${empty param.printFriendly}">
<form:input path="namePhrase" maxlength="20" onkeyup="clearSSN(event);" id="namePhraseId" cssClass="inputField" tabindex="20"/>
			    </c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch">
            	<ps:label fieldId="ssn" mandatory="false" ><b>SSN</b></ps:label><br>
                <c:if test="${not empty param.printFriendly}">
				<form:password  path="ssnOne"  cssClass="inputField" readonly="true"
						onkeyup="return autoTab(this, 3, event);" size="3" maxlength="3"/>
				<form:password  path="ssnTwo" cssClass="inputField" readonly="true"
						onkeyup="return autoTab(this, 2, event);" size="2" maxlength="2"/>
				<form:input  path="ssnThree" autocomplete="off" cssClass="inputField" readonly="true"
						onkeyup="return autoTab(this, 4, event);"  size="4" maxlength="4"/>
				</c:if>
				<c:if test="${empty param.printFriendly}">
				
				<form:password  path="ssnOne"   cssClass="inputField" onkeypress = "clearName(event);"  value="${censusSummaryReportForm.ssnOne}"
						onkeyup="clearName(event); return autoTab(this, 3, event);" size="3" maxlength="3" tabindex="30"/>
				<form:password  path="ssnTwo"  cssClass="inputField" onkeypress = "clearName(event);" value="${censusSummaryReportForm.ssnTwo}"
						onkeyup="clearName(event); return autoTab(this, 2, event);" size="2" maxlength="2" tabindex="31"/>
				<form:input  path="ssnThree"  autocomplete="off" cssClass="inputField"   onkeypress = "clearName(event);" 
						onkeyup="clearName(event); return autoTab(this, 4, event);" size="4" maxlength="4" tabindex="32"/>
				</c:if>
            </td>
            <td width="160" valign="bottom" class="filterSearch">
            <c:if test="${showDivColumn eq true}">
            	<b>Division</b><br>
                <c:if test="${not empty param.printFriendly}">
<form:input path="division" readonly="true" cssClass="inputField"/>
			    </c:if>
			    <c:if test="${empty param.printFriendly}">
<form:input path="division" cssClass="inputField" tabindex="40"/>
			    </c:if>
</c:if>&nbsp;
            </td>
        </tr>
	    <tr>
	        <td valign="top" class="filterSearch"></td>
	        <td valign="top" class="filterSearch"><b>Employment status</b><br>
	            <c:if test="${not empty param.printFriendly}">
<form:select path="status" disabled="true">
					<%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${censusSummaryReportForm.statusList}" itemLabel="label" itemValue="value"/>
</form:select>
			    </c:if>
				<c:if test="${empty param.printFriendly}">
					<form:select name="censusSummaryReportForm" path="status" tabindex="50">
				    <%-- set the first value of the select --%>
					<form:option value="All">All</form:option>
<form:options items="${censusSummaryReportForm.statusList}" itemLabel="label" itemValue="value"/>
					</form:select>
			    </c:if>
	        </td>
	        <td valign="top" class="filterSearch">&nbsp;</td>
	        <td valign="middle" class="filterSearch"><div align="left">
	        <c:if test="${empty param.printFriendly}">
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
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${empty param.printFriendly}">
	          <td width="30"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="30"></td>
	          <td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	</c:if>
          	<td width="225"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="225"></td>
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="90"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="90"></td>
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="90"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="90"></td>
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<c:if test="${showDivColumn eq true}">
          	  <td width="90"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="90"></td>
          	  <td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	  <td width="90"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="90"></td>
          	  <td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
</c:if>
          	<c:if test="${showDivColumn eq false}">
          	  <td width="181"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="181"></td>
          	  <td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
</c:if>
          	<td width="77"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="77"></td>
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr>
        
        <tr><td colspan="<%=numberOfColumns%>">&nbsp;</td></tr>
       <c:if test="${empty param.printFriendly}">
        <tr><td colspan="<%=numberOfColumns%>" align="left">
        <strong>Legend</strong>:&nbsp;<img src="/assets/unmanaged/images/view_icon.gif" width="12" height="12"/>&nbsp;View&nbsp;<img src="/assets/unmanaged/images/edit_icon.gif" width="12" height="12" />&nbsp;Edit&nbsp;
        <report:actions profile="userProfile"  item="theItem" action="legendResetPassword"  /> 
        </td></tr>
        </c:if>
        
		<!-- Start of body title -->
        <tr class="tablehead">
        	<td class="tableheadTD1" colspan="<%=numberOfColumns%>">         
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            	<tr>
		          <td class="tableheadTD" width="50%">&nbsp;</td>
		          <% if (errors==false) { %>
		          	<td class="tableheadTDinfo"><b><report:recordCounter report="theReport" label="Employees" /></b></td>
		          	<td align="right" class="tableheadTDinfo"><report:pageCounter report="theReport" formName="censusSummaryReportForm"/></td>
		          <% } else { %>
              		<td class="tableheadTDinfo"></td>
	            	<td align="right" class="tableheadTDinfo"></td>
	              <% } %>
		        </tr>
		    </table>
            </td>
        </tr>
		<!-- End of body title -->

        <tr class="tablesubhead">
          <td class="databorder" width="1"></td>
          <c:if test="${empty param.printFriendly}">
          	<td valign="top" width="30"><b>Action</b></td>
          	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
		  <td valign="top"><report:sort field="<%=CensusSummaryReportData.SORT_LAST_NAME%>" direction="asc" formName="censusSummaryReportForm"><b>Name</b></report:sort></td>
          <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><report:sort field="<%=CensusSummaryReportData.SORT_BIRTH_DATE%>" direction="asc" formName="censusSummaryReportForm"><b>Date of birth</b></report:sort></td>
		  <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td valign="top"><report:sort field="<%=CensusSummaryReportData.SORT_HIRE_DATE%>" direction="asc" formName="censusSummaryReportForm"><b>Hire date</b></report:sort></td>
		  <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		  <c:if test="${showDivColumn eq true}">
            <td valign="top"><report:sort field="<%=CensusSummaryReportData.SORT_DIVISION%>" direction="asc" formName="censusSummaryReportForm"><b>Division</b></report:sort></td>
		    <td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
		    <td valign="top"><report:sort field="<%=CensusSummaryReportData.SORT_STATUS%>" direction="asc" formName="censusSummaryReportForm"><b>Employment status</b></report:sort></td>
		  	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
		  <c:if test="${showDivColumn eq false}">
		  	<td valign="top"><report:sort field="<%=CensusSummaryReportData.SORT_STATUS%>" direction="asc" formName="censusSummaryReportForm"><b>Employment status</b></report:sort></td>
		  	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
		  <td valign="top"><b>Warning</b></td>
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

		<c:if test="${not empty theReport.details}">
<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex" >

<c:set var="indexValue" value="${theIndex.index}"/>

		<%  CensusSummaryDetails theIndexValue = (CensusSummaryDetails)pageContext.getAttribute("theItem");
									String temp = pageContext.getAttribute("indexValue").toString();
		  if (Integer.parseInt(temp)% 2 == 0) { %>
	        <tr class="datacell3">
		  <% } else { %>
	        <tr class="datacell1">
		  <% } %>

          <td class="databorder" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <c:if test="${empty param.printFriendly}">
          	<td align="left">  <report:actions profile="userProfile" item="theItem" action="editEmployeeSummary" /> </td>
          	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          </c:if>
          <td>
<c:if test="${userProfile.welcomePageAccessOnly == false}">
<c:if test="${theItem.participantInd ==true}">
          <ps:link styleId="participantAccount_${theItem.profileId}" action="/do/participant/participantAccount/" paramId="profileId" paramName="theItem" paramProperty="profileId">
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
          <td><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"
                           property="theItem.birthDate"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
          <td><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"
                           property="theItem.hireDate"/></td>
          <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                
          <c:if test="${showDivColumn eq true}">
<td>${theItem.division}</td>
            <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>                
            <td><ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="theItem" property="status"/>
            <c:if test="${not empty theItem.employeeStatusDate}">
              <br/>
              <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.employeeStatusDate"/>
            </c:if>
            </td>
          	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
          <c:if test="${showDivColumn == false}">
          	<td><ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="theItem" property="status"/>
          	<c:if test="${ theItem.employeeStatusDate ne null}">
              <br/>
              <render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>" property="theItem.employeeStatusDate"/>
            </c:if>
          	</td>
          	<td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
</c:if>
<c:set var="hasWarnings" value="${theItem.getWarnings().hasWarnings()}"/>
          <c:if test="${hasWarnings == true}">
<c:forEach items="${theItem.censusErrors}" var="error">


 
          		<%EmployeeValidationError error = (EmployeeValidationError)pageContext.getAttribute("error");%>

<% if (error.getErrorCode().equals(EmployeeValidationErrorCode.MissingBirthDate)) {  %>  
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("missingBirthDateWarning").toString()); %>
          		<% } %>
          		<% if (error.getErrorCode().equals(EmployeeValidationErrorCode.BlankEligibilityInd)) {  %>     
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("missingEligibIndWarning").toString()); %>
          	    <% } %>
          		<% if (error.getErrorCode().equals(EmployeeValidationErrorCode.MissingEligibilityDateForNonPpt)) {  %>
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("missingEligibDateWarning").toString()); %>
          		<% } %>
          	    <% if (error.getErrorCode().equals(EmployeeValidationErrorCode.MissingPYOSAndHOS)) {  %>  
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("missingVYOSAndHOSWarning").toString()); %>
          		<% } %>
          		<% if (error.getErrorCode().equals(EmployeeValidationErrorCode.MissingHireDate)) {  %>     
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("missingDateOfHireWarning").toString()); %>
          	    <% } %>
          		<% if (error.getErrorCode().equals(EmployeeValidationErrorCode.MissingEmploymentStatus)) {  %>
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("missingEmploymentStatusWarning").toString()); %>
          		<% } %>
          		<% if (error.getErrorCode().equals(EmployeeValidationErrorCode.PreviousYearsOfServiceEffectiveDateBirthDate)) {  %>
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("vyosDateBirthDateWarning").toString()); %>
          		<% } %>
          		<% if (error.getErrorCode().equals(EmployeeValidationErrorCode.MinPreviousYearsOfServiceEffectiveDate)) {  %>
          		<% theIndexValue.getWarnings().setWarningDescription(pageContext.getAttribute("minimumVyosDateWarning").toString()); %>
          		<% } %>

          		
          	
</c:forEach>
  	        <td align="middle" title="<%=theIndexValue.getWarnings().getWarningDescription()%>"><img src="/assets/unmanaged/images/warning2.gif" width="12" height="12" border="0"></td>          	
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

        <tr><td class="databorder" colspan="<%=numberOfColumns%>"></td></tr>
        <tr>
        	<td colspan="<%=numberOfColumns%>">         
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
		        <td width="50%"></td>
	    		<% if (errors==false) { %>
	        		<td><b><report:recordCounter report="theReport" label="Employees"/></b></td>
	        		<td align="right"><report:pageCounter report="theReport" arrowColor="black" formName="censusSummaryReportForm"/></td>
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

