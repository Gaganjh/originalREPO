<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.ps.web.Constants" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_GENERAL_SECTION_FOR_ADD%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="GeneralSection" />

<script type="text/javascript">
<!--
   var birthDateFormat = "Birth date is in wrong format";
   var birthDateInvalid = "Birth date must be at least 15 year ago";
//-->
</script>


<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
%>

<c:set var="disabled" value="${addEmployeeForm.readOnly}" /> 

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
   <tbody>
     <tr class="tablehead">
       <td colspan="3" class="tableheadTD1">

       <b><content:getAttribute id='GeneralSection' attribute='text'/></b>
       </td>
       <td id="headerBasic" class="tableheadTD">
         &nbsp;
       </td>
       
     </tr>
   </tbody>
 </table>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<table border="0" cellpadding="0" cellspacing="0" width="500">
     <tbody>
		<!-- SSN -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="174">
			SSN
			</td>
			 <td width="26" align="right">
			<ps:fieldHilight name="<%=EmployeeSnapshotProperties.SSN%>"/>
			 </td>		         
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight" width="297">        	    
<form:input path="ssn.ssn1" disabled="${disabled.booleanValue()}" maxlength="3" onkeyup="return (validateSsn(this, invalidSsnError) && autoTab(this, 3, event))" size="3" cssClass="mandatory"/>


	        	    &nbsp;
<form:input path="ssn.ssn2" disabled="${disabled.booleanValue()}" maxlength="2" onkeyup="return (validateSsn(this, invalidSsnError) && autoTab(this, 2, event))" size="2" cssClass="mandatory"/>


	        	    &nbsp;
<form:input path="ssn.ssn3" disabled="${disabled.booleanValue()}" maxlength="4" onkeyup="return validateSsn(this, invalidSsnError)" size="4" cssClass="mandatory"/>




	           	    <ps:trackChanges escape="true" property='ssn.ssn1' name="addEmployeeForm"/>
	           	    <ps:trackChanges escape="true" property='ssn.ssn2' name="addEmployeeForm"/>
	           	    <ps:trackChanges escape="true" property='ssn.ssn3' name="addEmployeeForm"/>
			</td>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>

		<!-- Prefix -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="174">
			Prefix	
			</td>
			 <td width="26" align="right">
			<ps:fieldHilight name="<%=EmployeeSnapshotProperties.Prefix%>"/>
			 </td>		         
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">
                   <form:select path="values[prefix]" cssStyle="boxbody" disabled="${disabled}">
<form:options items="${addEmployeeForm.namePrefixOptions}" itemLabel="label" itemValue="value"/>
</form:select>
           	    <ps:trackChanges escape="true"  property='values[prefix]' name="addEmployeeForm"/>
			</td>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>

		<!-- First Name-->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="174">
			First name
			</td>
			 <td width="26" align="right">
			<ps:fieldHilight name="<%=EmployeeSnapshotProperties.FirstName%>"/>
			 </td>		         			
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">        	    
<form:input path="values[firstName]" disabled="${disabled.booleanValue()}" maxlength="18" onchange="validateFirstName(this)" cssClass="mandatory"/>




         	    
           	    <ps:trackChanges escape="true" property='values[firstName]' name="addEmployeeForm"/>
			</td>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>

		<!-- Middle initial-->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="174">
			Middle initial 
			</td>
			 <td width="26" align="right">
			<ps:fieldHilight name="<%=EmployeeSnapshotProperties.MiddleInitial%>"/>
			 </td>		         
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">        	    
<form:input path="values[middleInitial]" disabled="${disabled.booleanValue()}" maxlength="1" onchange="validateMiddleInit(this)" size="1"/>




           	    <ps:trackChanges escape="true" property='values[middleInitial]' name="addEmployeeForm"/>
			</td>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<!-- Last Name-->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="174">
			Last name
			</td>
			 <td width="26" align="right">
			<ps:fieldHilight name="<%=EmployeeSnapshotProperties.LastName%>"/>
			 </td>		         
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">
        	    
<form:input path="values[lastName]" disabled="${disabled.booleanValue()}" maxlength="20" onchange="validateLastName(this)" cssClass="mandatory"/>




           	    <ps:trackChanges escape="true" property='values[lastName]' name="addEmployeeForm"/>
			</td>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>


		<!-- Employee ID -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="174">
			Employee ID 
			</td>
			 <td width="26" align="right">
			<ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmployeeId%>"/>
			 </td>		         
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">
        	    
<form:input path="values[employeeId]" disabled="${disabled.booleanValue()}" maxlength="9" onchange="validateEmployeeId(this)"/>



           	    <ps:trackChanges escape="true" property='values[employeeId]' name="addEmployeeForm"/>
			</td>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<!-- Birth date -->
          <tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder" >
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td width="174">
			Date of birth 
			</td>
			 <td width="26" align="right">
			<ps:fieldHilight name="<%=EmployeeSnapshotProperties.BirthDate%>"/>
			 </td>		         
			<td class="greyborder"> 
			  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
			</td>
			<td class="highlight">
        	   
<form:input path="values[birthDate]" disabled="${disabled.booleanValue()}" id="value(birthDate)" onchange="validateBirthDate(this)" size="10" cssClass="inputAmount"/>
&nbsp;
		   		  <c:if test="${!disabled}">	
				  <a href="javascript:doCalendar('value(birthDate)',0)">
				  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
				  (mm/dd/yyyy)
				  </c:if>
			      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           	    <ps:trackChanges escape="true" property='values[birthDate]' name="addEmployeeForm"/>
			</td>
			<td class="databorder">
			   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>		
		<!-- table footer border -->
        <tr>
          <td height="1" colspan="8" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>			
     </tbody>
</table>
<script type="text/javascript" >
<!--
	
	function doCalendar(fieldName) {
		// Retrieve the field
		var field = document.getElementById(fieldName);
			
		//Null check for Calendar issue(Calendar not working in mozilla)
		if(field != null){
			// Pre-Validate date and blank if not valid
			if (!validateDate(field.value)) {
				field.value = "";
			}
		}
		
		cal = new calendar(document.forms['addEmployeeForm'].elements[fieldName]);
		cal.year_scoll = true;
		cal.time_comp = false;
		cal.popup();
	}
//-->
</SCRIPT>
