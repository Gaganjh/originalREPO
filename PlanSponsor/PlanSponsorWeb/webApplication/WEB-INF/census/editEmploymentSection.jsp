<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/psweb-taglib.tld" prefix="psw" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeStatusInfo" %>
<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@ page import="com.manulife.pension.ps.web.census.util.VestingHelper" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo" %>
<un:useConstants var="censusConstants" className="com.manulife.pension.ps.web.census.CensusConstants" />
<un:useConstants var="contentConstants" className="com.manulife.pension.ps.web.content.ContentConstants" />

<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYMENT_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="EmploymentSection" />


<%

UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);

EmployeeVestingInfo statusParamVO = (EmployeeVestingInfo)request.getAttribute(CensusConstants.EMPLOYEE_STATUS_HISTORY_ATTRIBUTE);
pageContext.setAttribute("statusParamVO",statusParamVO,PageContext.PAGE_SCOPE);

%>







<c:set var="disabled" value="${editEmployeeForm.readOnly}" /> 
<%Boolean disabled=(Boolean)pageContext.getAttribute("disabled"); %>
 <c:choose >
  <c:when test="${editEmployeeForm.expandEmployment}">
     <c:set var="expandStyle" value="DISPLAY: block"/>
     <c:set var="statusDisplay" value="DISPLAY: none"/>
     <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>
  </c:when>
  <c:otherwise>
     <c:set var="expandStyle" value="DISPLAY: none"/>
     <c:set var="statusDisplay" value="DISPLAY: block"/>
     <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>
  </c:otherwise>
</c:choose>
 

<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">

    <tbody>
      <tr class="tablehead">
        <td class="tableheadTD1" width="200">
		    <a href="javascript:toggleSection('employment', 'headerEmployment','expandEmployment', 'expandEmploymentIcon')">
		       <img id="expandEmploymentIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>
		     &nbsp;
	       	 <b><content:getAttribute id='EmploymentSection' attribute='text'/> </b>
        </td>
		<td class="tableheadTD">
		  <span id="headerEmployment" style="<c:out value='${statusDisplay }'/>">
			 <b>Employment status:</b> <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="editEmployeeForm" property="value(employmentStatus)"/>
		  </span>
		</td>
      </tr>
    </tbody>
  </table>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<form:hidden path="expandEmployment"/>


<div id="employment" style="<c:out value='${expandStyle}'/>">
<table cellSpacing="0" cellPadding="0" width="700" border="0">
<tr>
<td>
     <table border="0" cellpadding="0" cellspacing="0" width="500">
       <tbody>
           <!--  Hire date -->
	       <tr class="<c:out value="${rowStyle.next}"/>">
	         <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	         <td width="100">
	         Hire date
	         </td>
			 <td width="26" align="right">
	         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.HireDate%>"/>
			 </td>		         
	         <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
			 <td width="<c:out value="${dataColWidth}"/>" class="highlight">        	   
 <form:input path="values[hireDate]" disabled="${disabled}" maxlength="10" onchange="validateHireDate(this)" size="10" cssClass="inputAmount" id="value(hireDate)"/> 



	   			&nbsp;
		   		  <c:if test="${!disabled}">		   		   	
				  <a href="javascript:doCalendar('value(hireDate)',0)">
				  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
				  (mm/dd/yyyy)
				  </c:if>
			      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[hireDate]' name="editEmployeeForm"/>			 	
			 </td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder" width="1"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['hireDate']}"/>				
				<td class="highlight" width="130" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">					  
				      <c:if test="${change.previousValue != null}">					
							<fmt:formatDate value="${change.previousValueAsDate}" pattern="MM/dd/yyyy"/>
				  </c:if>
			      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
				 </td>
			 </c:if>
			<td class="databorder" width="1">
			 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
	       </tr>
			<!-- Division -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
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


	           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[division]' name="editEmployeeForm"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['division']}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
${change.previousValue}
						  </c:if>
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
			<!-- Employment status  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
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
<form:options items="${editEmployeeForm.employmentStatusOptions}" itemLabel="label" itemValue="value"/>
</form:select> 
	           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[employmentStatus]' name="editEmployeeForm"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['employmentStatus']}"/>				
					<td>
						<c:if test="${change.previousValue != null}">	
						<table>
						<tr>
						
							<td width="80%" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>" class="highlight">
							     <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="change" property="previousValue"/>
														</td>
							<td width="20%">
							<c:if test="${not empty statusParamVO.fullStatusList}">
							     <a href="javascript:expand('statusHistory')">History</a>
							</c:if>
							</td>
						</tr>
						</table>
						</c:if>
					</td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Employment status effective date  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
				Employment status effective date
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmploymentStatusEffectiveDate%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="<c:out value="${dataColWidth}"/>" class="highlight">	        	   
<form:input path="values[employmentStatusEffectiveDate]" disabled="${disabled}" maxlength="10" onchange="validateEmploymentStatusEffDate(this)" size="10" cssClass="inputAmount" id="value(employmentStatusEffectiveDate)"/> 


			   			&nbsp;
			   		  <c:if test="${!disabled}">			   		  	
					  <a href="javascript:doCalendar('value(employmentStatusEffectiveDate)',0)">
					  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
					  (mm/dd/yyyy)
					  </c:if>
				      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           		    <ps:trackChanges escape="true" collectionProperty="true" property='values[employmentStatusEffectiveDate]' name="editEmployeeForm"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
					<c:set var="change" value="${employeeHistory.propertyMap['employmentStatusEffectiveDate']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
								<fmt:formatDate value="${change.previousValueAsDate}" pattern="MM/dd/yyyy"/>
						  </c:if>
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Annual base salary  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
				 Annual base salary
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.AnnualBaseSalary%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">
				   <c:choose>
				    <c:when test="${editEmployeeForm.viewSalary}">
$ <form:input path="values[annualBaseSalary]" disabled="${disabled}" onblur="validateAnnualBaseSalary(this)" cssStyle="{text-align: right}"/>


	           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[annualBaseSalary]' name="editEmployeeForm"/>
				  	</c:when>
				  	<c:otherwise>
				  	
<c:if test="${not empty editEmployeeForm.values['annualBaseSalary']}">
					  	   ******
</c:if> &nbsp;
				  	</c:otherwise>
				  </c:choose>	        	    
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}" >
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
					<c:set var="change" value="${employeeHistory.propertyMap['annualBaseSalary']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
							  <c:choose>
							    <c:when test="${editEmployeeForm.viewSalary}">
							      <c:if test="${change.previousValue != null}">					
							       <fmt:formatNumber value="${change.previousValue}"
							     	   				   	  pattern="$###,###,##0.00"/>
								  </c:if>
						  	</c:when>
						  	<c:otherwise>
					  	    <c:if test="${not empty change.previousValue}">
						  	   ******
						  	</c:if> &nbsp;
						  	</c:otherwise>
						  </c:choose> 
						  </c:if>
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Plan Year to Date compensation -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
				Eligible plan YTD compensation
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.PlanYearToDateComp%>"/>
				 </td>		         				
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">
				  <c:choose>
				    <c:when test="${editEmployeeForm.viewSalary}">
$ <form:input path="values[planYearToDateComp]" disabled="${disabled}" onblur="validateYTDCompensation(this)" cssStyle="{text-align: right}"/>



	           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[planYearToDateComp]' name="editEmployeeForm"/>
				  	</c:when>
				  	<c:otherwise>
<c:if test="${not empty editEmployeeForm.values['planYearToDateComp']}">
					  	   ******
</c:if> &nbsp;
				  	</c:otherwise>
				  </c:choose> 	        	    
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change" value="${employeeHistory.propertyMap['planYearToDateComp']}"/>				
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
							 <c:choose>
							    <c:when test="${editEmployeeForm.viewSalary}">
							      <c:if test="${change.previousValue != null}">					
							       <fmt:formatNumber value="${change.previousValue}"
							     	   				   	  pattern="$###,###,##0.00"/>
								  </c:if>
							  	</c:when>
							  	<c:otherwise>
							  	    <c:if test="${not empty change.previousValue}">
								  	   ******
								  	</c:if> &nbsp;
							  	</c:otherwise>
							  </c:choose> 
						  </c:if>
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
		
			<c:if test="${securityProfile.internalUser}">
			<!-- Mask sensitive information  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
				Mask sensitive information
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.MaskSensitiveInformation%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">
	        	    
 <form:radiobutton disabled="${disabled}" path="values[maskSensitiveInformation]" value="Y"/>Yes

<form:radiobutton disabled="${disabled}" path="values[maskSensitiveInformation]" value="N"/>No
 
         	    
	           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[maskSensitiveInformation]' name="editEmployeeForm"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
					<c:set var="change" value="${employeeHistory.propertyMap['maskSensitiveInformation']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
							 	<ps:censusLookup typeName="<%=CensusLookups.BooleanString%>" name="change" property="previousValue"/>
						  </c:if>
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       
			<!-- Mask sensitive information comment  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
				Mask sensitive information comment
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.MaskSensitiveInformationComment%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
 <form:textarea path="values[maskSensitiveInformationComment]" cols="20" disabled="${disabled.booleanValue()}" onblur="return limitTextArea(this, 254)" onkeydown="return limitTextArea(this, 254)" onkeyup="return limitTextArea(this, 254)" rows="3"/> 




	           	    <ps:trackChanges collectionProperty="true" property='values[maskSensitiveInformationComment]' escape="true" name="editEmployeeForm"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
				  <c:set var="change" value="${employeeHistory.propertyMap['maskSensitiveInformationComment']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${change.previousValue != null}">					
${change.previousValue}
						  </c:if>
					 </td>
				 </c:if>
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
				<td width="100">
				   Plan YTD hours worked 
				</td>
				 <td width="26" align="right">
				   <ps:fieldHilight name="<%=EmployeeSnapshotProperties.YearToDatePlanHoursWorked%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight">	        	    
 <form:input path="values[yearToDatePlanHoursWorked]" disabled="${disabled}" maxlength="5" onchange="validateYTDHoursWorked(this)" size="5" cssStyle="{text-align: right}"/> 





	           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[yearToDatePlanHoursWorked]' name="editEmployeeForm"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
					<c:set var="change" value="${employeeHistory.propertyMap['yearToDatePlanHoursWorked']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
						<table>
							<tr>
								<td width="80%" class="highlight">						  
					      			<c:if test="${change.previousValue != null}">					
${change.previousValue}
						  			</c:if>
						  		</td>
						  		<td width="20%">
						  			<!-- Added to show the history link -->
						  	  		<c:if test="${not empty employeePlanHoursWorkedHistory}"> 
										<c:choose>
											<c:when test="${!param.printFriendly}">
									    		<a href="javascript:expand('planYTDHoursHistory')">History</a>
									    	</c:when>
								    	</c:choose>
							    	</c:if>  
								</td>
							</tr>
						</table>
						  <!-- End added to show the history link -->
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

			<!-- Plan YTD Hrs Worked/Comp Effective Date  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" >
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="100">
				Plan YTD hours worked/eligible comp effective date
				</td>
				 <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.YearToDatePlanHoursEffDate%>"/>
				 </td>		         
				<td class="greyborder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="<c:out value="${dataColWidth}"/>" class="highlight">	        	    
 <form:input path="values[yearToDatePlanHoursEffDate]" disabled="${disabled}" onchange="validateYTDCompensationDate(this)" size="10" cssClass="inputAmount" id="value(yearToDatePlanHoursEffDate)"/> 

			   			&nbsp;
			   		  <c:if test="${!disabled}">	
					  <a href="javascript:doCalendar('value(yearToDatePlanHoursEffDate)',0)">
					  <img src="/assets/unmanaged/images/cal.gif" border="0"></a>
					  (mm/dd/yyyy)
					  </c:if>
				      <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	           	    <ps:trackChanges escape="true" collectionProperty="true" property='values[yearToDatePlanHoursEffDate]' name="editEmployeeForm"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
					<c:set var="change" value="${employeeHistory.propertyMap['yearToDatePlanHoursEffDate']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${change.previousValue != null}">					
								<fmt:formatDate value="${change.previousValueAsDate}" pattern="MM/dd/yyyy"/>
						  </c:if>
					 </td>
				 </c:if>
				<td class="databorder">
				   <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>	       

		    <!-- table footer border  -->
	        <tr>
	          <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	          <c:if test="${userProfile.showCensusHistoryValue}">
	          <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	          </c:if>
	        </tr>			      
       </tbody>
	</table>
	</td>
	<td valign="top" height="100%">
	<table height="100%">
	<tr><td valign="top" height="50%">
	<!-- Status History fly out chart -->
	<div id="statusHistory" style="DISPLAY: none">
    <table border="0" cellpadding="0" cellspacing="0" width="200">
      <tbody>
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td width="198"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="4"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="3"><b>History</b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <psw:employeeStatusHistoryLink formName="editEmployeeForm"/></b></td>
        <c:if test="${not empty employeeStatusHistory}">
       
              
        <jsp:setProperty name="rowStyle" property="start" value="1"/>
		<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>
              
<c:if test="${not empty statusParamVO.fullStatusList}">
<c:forEach items="${statusParamVO.fullStatusList}" var="statusParam" varStatus="idx">
<c:set var="indexValue" value="${idx.index}"/> 
<%Integer temp = (Integer)pageContext.getAttribute("indexValue");
pageContext.setAttribute("temp",temp);
EmployeeStatusInfo statusParam =(EmployeeStatusInfo)pageContext.getAttribute("statusParam");
%>

        <c:if test="${temp le 4}">
        <tr class="<c:out value="${rowStyle.next}"/>">
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td width="198" valign="top">
          <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="statusParam" property="status"/>, 
          <c:if test="${not empty statusParam.effectiveDate}">
          	<fmt:formatDate value="${statusParam.effectiveDate}" pattern="MM/dd/yyyy"/>
          </c:if><Br>
           <%=VestingHelper.getVestingAuditInfo(statusParam,userProfile, VestingType.EMPLOYMENT_STATUS)%> 
          </td>
          <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
        </c:if>
</c:forEach>
</c:if>

        </c:if>
        <tr>
          <td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
      </tbody>
    </table>			             
    <!-- End Status History fly out chart -->
	</div>
	</td></tr>
	<tr>
	<td valign="bottom" height="50%">
		<!-- Plan YTD Hours Worked -->
		<div id="planYTDHoursHistory" style="DISPLAY: none">
    <table border="0" cellpadding="0" cellspacing="0" width="200">
      <tbody>
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td width="198"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="4"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="5"><b>History</b></td>
        </tr>
        <tr>
	        <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	        <td align="right" class="greyborder" width="35%" >Plan Year End</td>
	        <td align="right" class="greyborder" width="28%">&nbsp;&nbsp;YTD Hours</td>
	        <td align="right" class="greyborder" width="*" >&nbsp;&nbsp;YTD Hours Effective Date</td>
	        <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
      	
        <c:forEach var="historyData" varStatus="indexObj" items="${employeePlanHoursWorkedHistory}" >
        
        <tr class="<c:out value="${rowStyle.next}"/>" >	
           <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
	        <td width="35%" align="right">${historyData.key}</td>
	        <td width="28%" align="right">${historyData.value.value}</td>
	        <td align="right" width="*">${historyData.value.label}</td>
	        <td width="1" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
        </c:forEach>
        
        <tr>
          <td class="databorder" colspan="5"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
      </tbody>
    </table>			             
	</div>
		<!-- End Plan YTD Hours Worked -->
	</td>
	</tr>
	</table>
	
	</td>
</tr>
</table>
</div> 
