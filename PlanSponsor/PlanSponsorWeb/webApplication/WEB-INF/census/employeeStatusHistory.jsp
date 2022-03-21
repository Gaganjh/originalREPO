<%-- taglib used --%>

<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@ taglib prefix="report" uri="manulife/tags/report" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- Imports --%>


<%-- Imports --%>

<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@ page import="com.manulife.util.render.RenderConstants" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.content.valueobject.ContentType" %>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeStatusHistoryReportForm" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryReportData" %>
<%@ page import="com.manulife.pension.ps.service.report.census.valueobject.EmployeeStatusHistoryDetails" %>
<%@ page import="com.manulife.pension.ps.web.census.util.EmployeeStatusHistoryReportHelper" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="com.manulife.pension.service.employee.util.EmployeeValidationErrorCode" %>
<%@ page import="com.manulife.pension.service.vesting.VestingConstants" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusErrorCodes" %>
<%@ page import="com.manulife.pension.util.content.GenericException" %>
<%@ page import="com.manulife.pension.ps.web.ErrorCodes" %>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeStatusHistoryReportForm" %>
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psMainTabNav.css" type="text/css">
<link rel="stylesheet" href="/assets/unmanaged/stylesheet/psTabNav.css" type="text/css">

<%
EmployeeStatusHistoryReportData theReport=(EmployeeStatusHistoryReportData)request.getAttribute(Constants.REPORT_BEAN);
pageContext.setAttribute("theReport",theReport,PageContext.PAGE_SCOPE);

EmployeeStatusHistoryReportForm employeeStatusHistoryReportForm=(EmployeeStatusHistoryReportForm)session.getAttribute("employeeStatusHistoryReportForm");
pageContext.setAttribute("employeeStatusHistoryReportForm",employeeStatusHistoryReportForm,PageContext.PAGE_SCOPE);

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

			 
<content:contentBean id ="errorDeleteAll"
			 contentId = "64039" 
			 type ="<%=ContentConstants.TYPE_MISCELLANEOUS%>" />
<content:contentBean id ="errorDOB"
			 contentId = "<%=ErrorCodes.ERROR_EFF_DATE_LESS_DOB%>" 
			 type ="<%=ContentConstants.TYPE_MISCELLANEOUS%>" />
 <content:contentBean id ="errorHD"
			 contentId = "<%=ErrorCodes.ERROR_EFF_DATE_LESS_HIRE_DATE%>" 
			 type ="<%=ContentConstants.TYPE_MISCELLANEOUS%>" />


<content:contentBean id ="warningDiscardChanges"
			contentId = "<%=ContentConstants.WARNING_DISCARD_CHANGES%>"
			type ="<%=ContentConstants.TYPE_MISCELLANEOUS%>" />
	
<content:contentBean id ="warningDelete"
			contentId = "<%=ContentConstants.WARNING_CONTINUE_DELETE%>"
			type ="<%=ContentConstants.TYPE_MISCELLANEOUS%>" />
			
			 

<%boolean errors = false;%>
<style type="text/css">
<!--
div.scroll {
	height: 600px;
	width: 700px;
	overflow: auto;
	border-style: none;
	background-color: #fff;
	padding: 8px;}
div.inline {
	display: inline; }
-->
</style>

<script type="text/javascript">


var submitted = false;

function checkValue (theIndex) {
  var selected = false;
//alert ("check value");
//alert("check value " + theIndex);
//alert(document.forms['employeeStatusHistoryReportForm'].elements['theItem[theIndex].markedForDeletion'].value);
//alert(document.forms['employeeStatusHistoryReportForm'].elements['theItem[2].markedForDeletion'].checked);
//alert(document.forms['employeeStatusHistoryReportForm'].elements['theItem[theIndex].markedForDeletion'].value );
//document.forms['employeeStatusHistoryReportForm'].elements['theItem[theIndex].markedForDeletion'].value =this.checked;
selected =document.forms['employeeStatusHistoryReportForm'].elements['theItemList[' + theIndex +'].markedForDeletionDisplay'].checked
document.forms['employeeStatusHistoryReportForm'].elements['theItemList[' + theIndex +'].markedForDeletion'].value = selected;
//alert ("value " +document.forms['employeeStatusHistoryReportForm'].elements['theItem[' + theIndex +'].markedForDeletion'].value);
}

// Called when reset is clicked
function doReset() {
	document.employeeStatusHistoryReportForm.task.value = "default";
		return true;

	

}

// Called when Delete is clicked
function doDelete () { 
    if(!submitted){
    if (isFormChanged()) {
     	if (document.forms['employeeStatusHistoryReportForm']) {       
        document.forms['employeeStatusHistoryReportForm'].elements['task'].value = "save"; 
        document.forms['employeeStatusHistoryReportForm'].elements['savedFlag'].value = "N"; 
        //alert(document.forms['employeeStatusHistoryReportForm'].elements['theItem[0].profileId'].value);
        //alert (document.forms['employeeStatusHistoryReportForm'].elements['task'].value);
  	    if (confirm("<content:getAttribute beanName="warningDelete" attribute="text" filter="true"/>")) {      
            return true;
          } else {
          return false;           
  	  }
  	  }
	 } else {
	  return false;
	 }
       } else {
	 window.status = "Transaction already in progress.  Please wait.";
			 return false;
 }

}

function doConfirmAndSubmit(action) {
    // only there is change, do confirm
 if(!submitted){
    if (isFormChanged()) {
    
	if (confirm("<content:getAttribute beanName="warningDiscardChanges" attribute="text" filter="true"/>")) {
	     submitted = true;
	     doSubmit(action);
	       return true;
	    } else {
	       return false;
	    }
    	 } else {
    	 	submitted = true;
        	doSubmit(action);
		  return true;
	 }
 } else {
     window.status = "Transaction already in progress.  Please wait.";
	 		 return false;
   }
}


  function doSubmit(action) {
	document.forms['employeeStatusHistoryReportForm'].elements['task'].value = "cancel";  	

  }
     // Track changes framework
     function isFormChanged() {
       return changeTracker.hasChanged();
   }
      // Registration required by Track changes framework
   registerTrackChangesFunction(isFormChanged);
  
</script>
<% int numberOfColumns = 9; %>


     <!-- Beginning of census Summary report body -->
     
    
<div id="errordivcs"><content:errors scope="session"/></div>  
<table width="100%" border="0" cellspacing="0" cellpadding="0">

   <c:if test="${not empty psErrors}">
	<tr><td class="redText" colspan="<%=numberOfColumns%>"><ul>
	<c:forEach items="${psErrors}" var="error">
	<%GenericException error=(GenericException)pageContext.getAttribute("error");  %>
		    
	            <% if (error.getErrorCode() == 64039) { %>
	              <li><content:getAttribute beanName="errorDeleteAll" attribute="text" filter="true"/>
	             &nbsp;&nbsp;[64039]	 	    </li>
	            <%}%>
	            <% if (error.getErrorCode() == 56162) { %>
	             <li><content:getAttribute beanName="errorDOB" attribute="text" filter="true"/>
	            &nbsp;&nbsp;[56162]	 	    </li>
	            <%}%>
	            <% if (error.getErrorCode() == 56163) { %>
		     <li><content:getAttribute beanName="errorHD" attribute="text" filter="true"/>
		     &nbsp;&nbsp;[56163]	 	    </li>
	            <%}%>

	           </c:forEach>
	           </ul></td></tr> 

        <%errors=true;%>
        </c:if>
 <ps:form method="POST" action="/do/census/employeeStatusHistory/" modelAttribute="employeeStatusHistoryReportForm" name="employeeStatusHistoryReportForm"> 
 <form:hidden path="profileId"/>
 <form:hidden path="task" value="filter"/>
 <form:hidden path="savedFlag" value="N"/>
	
<tr>

          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
	        <td width="30"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="50"></td>
	        <td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="70"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="100"></td>
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="50"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="50"></td>
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
          	<td width="250"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="100"></td>
          	<td width="1"><IMG height=1 src="/assets/unmanaged/images/s.gif" width="1"></td>
        </tr>

	<tr class="tablehead">
	    <td class="tableheadTD1" colspan="<%=numberOfColumns%>">  
	    
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
		<td class="tableheadTDinfo"><b>Employment Status History</b></td>
          	</tr>
            </table>
	    </td>
        </tr>
      <tr class="tablesubhead">  
	<td class="databorder" width="1"></td>
	<td valign="top"><b>Delete</b></td>
	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td valign="top"><b>Employment status effective date</b></td>
	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td valign="top"><b>Employment status</b></td>
	<td class="dataheaddivider" valign="bottom" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	<td valign="top"><b>Changed by</b></td>
	<td class="databorder" width="1"></td>
      </tr>

	<c:if test="${not empty theReport.details}">
	<c:forEach items="${theReport.details}" var="theItem" varStatus="theIndex">
	<c:set var="indexVal" value="${theIndex.index}"/>
	<%
	EmployeeStatusHistoryDetails theItem=(EmployeeStatusHistoryDetails)pageContext.getAttribute("theItem");
	Integer theIndex=(Integer)pageContext.getAttribute("indexVal");
	%>
	
<input type="hidden" name="theItemList[${theIndex.index}].status" value="${theItem.status}" />
<input type="hidden" name="theItemList[${theIndex.index}].lastUpdatedUserId" value="${theItem.lastUpdatedUserId}" /> 
<input type="hidden" name="theItemList[${theIndex.index}].lastUpdatedUserType" value="${theItem.lastUpdatedUserType}"  /> 
<input type="hidden" name="theItemList[${theIndex.index}].sourceChannelCode" value="${theItem.sourceChannelCode}"  />
<fmt:formatDate value="${theItem.effectiveDate}" pattern="MM/dd/yyyy" var="tempProcessing"  />
<input type="hidden" name="theItemList[${theIndex.index}].effectiveDate" value="${tempProcessing}" />
<fmt:formatDate value="${theItem.lastUpdatedTs}" pattern= "MM/dd/yyyy" var="tempProcessingTs" />
<input type="hidden" name="theItemList[${theIndex.index}].lastUpdatedTs" value="${tempProcessingTs}" />
	  

	  <% if (theIndex.intValue() % 2 == 0) { %>
	<tr class="datacell3">
	  <% } else { %>
	<tr class="datacell1">
		  <% } %>
	    <td class="databorder" width="1"></td>
	    <td>
	    
	    <form:checkbox path="theItemList[${theIndex.index}].markedForDeletionDisplay" onchange="checkValue (${theIndex.index})" value="true" /></td>
	    <form:hidden path="theItemList[${theIndex.index}].markedForDeletion" value="${theItem.markedForDeletion}" />
	  <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>

	    <td><render:date patternOut="<%= RenderConstants.MEDIUM_MDY_SLASHED %>"
			      property="theItem.effectiveDate"/></td>
	     <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td align="left" ><ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="theItem" property="status"/></td>
	    <td class="datadivider" width="1"><img src="/assets/unmanaged/images/s.gif" width="1" height="1"></td>
	    <td align="left" ><%=EmployeeStatusHistoryReportHelper.getDisplayInfo (theItem, userProfile)%>
	    <td class="databorder" width="1"></td>

	</tr>
	<ps:trackChanges name="employeeStatusHistoryReportForm" property='<%= "theItemList[" + theIndex +"].markedForDeletion" %>' />

	 </c:forEach>
	</c:if>		

 	
 <tr><td class="databorder" colspan="9"></td></tr>

 	         <tr> 
	           <table> 
	             <tbody>
	             <tr><td width="100%">&nbsp;</td></tr>  
	               <tr>
	                 <td width="45%">&nbsp;</td>                               
	                 <td id="cancelButton" width="35%" align="right" nowrap>
	                     <input name="button" type="submit" class="button134" value="back" onclick="return doConfirmAndSubmit('doCancel')" />
	                 </td>
	                 
	                 <td id="saveButton" width="20%" align="right" nowrap>
	                     <input name="button" type="submit" class="button134" value="save" onclick="return doDelete();"/>
	                 </td> 

	               </tr>
	               
	             </tbody>            
	           </table>
 </ps:form>
	

</table>