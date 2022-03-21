<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYMENT_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="EmploymentSection" />

<table border="0" cellpadding="0" cellspacing="0" width="500">

    <tbody>
      <tr class="tablehead">
        <td class="tableheadTD1">
        	<b><content:getAttribute id='EmploymentSection' attribute='text'/> </b>
        </td>
       <td class="tableheadTD" id="headerEmployment">
       	&nbsp;
       </td>
      </tr>
    </tbody>
  </table>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<c:set var="disabled" value="${addEmployeeForm.readOnly}" /> 
<div id="employment" style="<c:out value='${expandStyle}'/>">
     <table class="box" border="0" cellpadding="0" cellspacing="0" width="500">
       <tbody>
           <!--  Hire date -->
	       <tr class="<c:out value="${rowStyle.next}"/>">
	         <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	         <td width="174">
	         Hire date
	         </td>
			 <td width="26" align="right">
	         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.HireDate%>"/>
			 </td>		         
	         <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			 <td width="297" class="highlight">
        	   
<form:input path="values[hireDate]" disabled="${disabled}" maxlength="10" id="value(hireDate)" onchange="validateHireDate(this)" size="10" cssClass="inputAmount"/>



		   			&nbsp;
				  <a href="javascript:doCalendar('value(hireDate)',0)">
				  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
				  (mm/dd/yyyy)
			      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[hireDate]'/>			 	
			 </td>
			<td class="databorder" width="1">
			 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
	       </tr>
			<!-- Division -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Division
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.Division%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
<form:input path="values[division]" disabled="${disabled}" maxlength="25" onchange="validateDivision(this)"/>


	           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[division]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
			<!-- Employment status  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Employment status	
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmploymentStatus%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
		         	 <form:select path="values[employmentStatus]" cssStyle="boxbody" disabled="${disabled}">
<form:options items="${addEmployeeForm.employmentStatusOptions}" itemLabel="label" itemValue="value"/>
</form:select> 
	           	    <%-- <ps:trackChanges escape="true" name="addEmployeeForm" property='values[employmentStatus]'/> --%>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Employment status effective date  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Employment status effective date
				</td>
				 <td width="26" align="right">
				 <ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmploymentStatusEffectiveDate%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
<form:input path="values[employmentStatusEffectiveDate]" disabled="${disabled}" id="value(employmentStatusEffectiveDate)" onchange="validateEmploymentStatusEffDate(this)" size="10" cssClass="inputAmount"/>


			   			&nbsp;
			   		  <c:if test="${!disabled}">	
					  <a href="javascript:doCalendar('value(employmentStatusEffectiveDate)',0)">
					  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
						  (mm/dd/yyyy)
					  </c:if>	  
				      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           		    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[employmentStatusEffectiveDate]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Annual base salary  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Annual base salary
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.AnnualBaseSalary%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
$ <form:input path="values[annualBaseSalary]" disabled="${disabled}" onblur="validateAnnualBaseSalary(this)" cssStyle="{text-align: right}"/>



	           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[annualBaseSalary]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Plan Year to Date compensation -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Eligible plan YTD compensation
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.PlanYearToDateComp%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
$ <form:input path="values[planYearToDateComp]" disabled="${disabled}" onblur="validateYTDCompensation(this)" cssStyle="{text-align: right}" id="value(planYearToDateComp)"/>



	           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[planYearToDateComp]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<c:if test="${userProfile.internalUser}">
			<!-- Mask sensitive information  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Mask sensitive information
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.MaskSensitiveInformation%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
<form:radiobutton disabled="${disabled}" path="values[maskSensitiveInformation]" value="Y"/>
						Yes

<form:radiobutton disabled="${disabled}" path="values[maskSensitiveInformation]" value="N"/>
						No

         	    
	           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[maskSensitiveInformation]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
			<!-- Mask sensitive information comment  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
					Mask sensitive information comment
				</td>
				 <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.MaskSensitiveInformationComment%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">
<form:textarea path="values[maskSensitiveInformationComment]" cols="30" disabled="${disabled}" onblur="return limitTextArea(this, 254)" onkeydown="return limitTextArea(this, 254)" onkeyup="return limitTextArea(this, 254)" rows="3"/>




	           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[maskSensitiveInformationComment]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
			</c:if>
			
			<!-- Year to date plan hours worked  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Plan YTD hours worked 
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.YearToDatePlanHoursWorked%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
<form:input path="values[yearToDatePlanHoursWorked]" id="value(yearToDatePlanHoursWorked)" disabled="${disabled}" maxlength="5" onchange="validateYTDHoursWorked(this)" cssStyle="{text-align: right}"/>



	           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[yearToDatePlanHoursWorked]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Plan YTD Hrs Worked/Comp Effective Date  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="174">
				Plan YTD hours worked/eligible comp effective date
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.YearToDatePlanHoursEffDate%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">
<form:input path="values[yearToDatePlanHoursEffDate]" id="value(yearToDatePlanHoursEffDate)" disabled="${disabled}" onchange="validateYTDCompensationDate(this)" size="10" cssClass="inputAmount"/>

			   			&nbsp;
			   		  <c:if test="${!disabled}">	
					  <a href="javascript:doCalendar('value(yearToDatePlanHoursEffDate)',0)">
					  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
					  (mm/dd/yyyy)
					  </c:if>
				      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	    <ps:trackChanges escape="true" name="addEmployeeForm" property='values[yearToDatePlanHoursEffDate]'/>
				</td>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

		    <!-- table footer border  -->
	        <tr>
	          <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	        </tr>			      
       </tbody>
</table>
</div> 
