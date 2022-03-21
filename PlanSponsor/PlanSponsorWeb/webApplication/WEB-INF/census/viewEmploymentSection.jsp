<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

 <%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeStatusInfo" %>      
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@ page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>
<%@ page import="com.manulife.pension.ps.web.census.util.VestingHelper" %>
<%@ page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo.VestingType" %>
<%@ page import="com.manulife.pension.ps.web.Constants"%>
<%@ page import="com.manulife.pension.ps.web.census.CensusConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ page import="com.manulife.pension.service.employee.valueobject.EmployeeVestingInfo" %>
<%
UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE);
EmployeeVestingInfo statusParamVO = (EmployeeVestingInfo)request.getAttribute(CensusConstants.EMPLOYEE_STATUS_HISTORY_ATTRIBUTE);
pageContext.setAttribute("statusParamVO",statusParamVO,PageContext.PAGE_SCOPE);
%>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_EMPLOYMENT_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="EmploymentSection" />
	








<form:hidden path="expandEmployment"/>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>




<c:choose >
  <c:when test="${param.printFriendly == true || employeeForm.expandEmployment}">
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


<c:choose>
	<c:when test="${not param.printFriendly}">
	<table class=box cellSpacing=0 cellPadding=0 width=500 border=0>
		<tbody>
			<tr class=tablehead>
				<td class=tableheadTD1 width="200">															
				    <a href="javascript:toggleSection('employment', 'headerEmployment','expandEmployment', 'expandEmploymentIcon')">
				       <img id="expandEmploymentIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>
				    &nbsp;
					<b><content:getAttribute id='EmploymentSection' attribute='text'/> </b>
				</td>
				<td class="tableheadTD">
				 <span id="headerEmployment" style="<c:out value='${statusDisplay }'/>">
				   <b>Employment status:</b> <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="employee" property="employeeDetailVO.employmentStatusCode"/>
				 </span>				 
				</td>
			</tr>
		</tbody>
	</table>
	</c:when>
	<c:otherwise>
	<table class="box" cellSpacing="0" cellPadding="0" width="500" border="0">
		<tbody>
			<tr class=tablehead>
				<td class=tableheadTD1>
					<B><content:getAttribute id='EmploymentSection' attribute='text'/> </B>
				</td>
			</tr>
		</tbody>
	</table>
	</c:otherwise>
</c:choose>

<div id="employment" style="<c:out value='${expandStyle}'/>">
<c:remove var="expandStyle"/>
<c:remove var="statusDisplay"/>
<table cellSpacing="0" cellPadding="0" width="700" border="0">
<tr>
<td>
	<table cellSpacing="0" cellPadding="0" width="500" border="0" class="greyborder">
		<tbody>
			<!-- Hire date-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder width="1">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
		         <td width="124">
		         Hire date
		         </td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.HireDate%>"/>
			   </td>		         
				<td class="greyborder" width="1"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['hireDate']}"/>
				<td class="highlight" width="<c:out value='${dataColWidth}'/>" onmouseover="<ps:employeeChangeDetail name='change'/>">
				    <fmt:formatDate value="${employee.employeeDetailVO.hireDate}" pattern="MM/dd/yyyy"/>
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
				    <c:set var="previousValue" value="${change.previousValueAsDate}"/>
					<td class="highlight" width="172" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<render:date patternOut="MM/dd/yyyy" property="previousValue"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>

			<!-- Division -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">
				 Division 
				 </td>
			   <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.Division%>"/>
			   </td>		         				 
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['division']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.employeeDetailVO.employerDivision}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>				
				    <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">											   
						 	<c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>
			<!-- Employment status-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">
				  Employment status
				  </td>
			   <td width="26" align="right">
  				  <ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmploymentStatus%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['employmentStatus']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
				      <ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="employee" property="employeeDetailVO.employmentStatusCode"/>
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
					<td>
						<table>
						<tr>
							<td width="80%" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>" class="highlight">
									<ps:censusLookup typeName="<%=CensusLookups.EmploymentStatus%>" name="change" property="previousValue"/>
							</td>
							<td width="20%">
						
							<c:if test="${not empty statusParamVO.fullStatusList}">
								<c:choose>
								<c:when test="${!param.printFriendly}">
							    	<a href="javascript:expand('statusHistory')">History</a>
							    </c:when>
							    </c:choose>
							</c:if>
							</td>
						</tr>
						</table>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
			<!-- Employment statuseffective date -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">
				Employment status effective date
				</td>
			   <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.EmploymentStatusEffectiveDate%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['employmentStatusEffectiveDate']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">                                   
				        <render:date patternOut="MM/dd/yyyy" property="employee.employeeDetailVO.employmentStatusEffDate"/>
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>			
					<c:set var="previousValue" value="${change.previousValueAsDate}"/>	
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	  <render:date property='previousValue' patternOut="MM/dd/yyyy"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
			<!-- Annual base salary  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">
					 Annual base salary
				</td>
			   <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.AnnualBaseSalary%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['annualBaseSalary']}"/>
				<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change'/>">
				  <c:choose>
				    <c:when test="${employeeForm.viewSalary}">
				       <fmt:formatNumber value="${employee.employeeDetailVO.annualBaseSalary}"
				     	   				   	  pattern="$###,###,##0.00"/>
				  	</c:when>
				  	<c:otherwise>
				  	    <c:if test="${not empty employee.employeeDetailVO.annualBaseSalary}">
					  	   ******
					  	</c:if> &nbsp;
				  	</c:otherwise>
				  </c:choose>
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
					<c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					  <c:choose>
					    <c:when test="${employeeForm.viewSalary}">
					      <c:if test="${previousValue != null}">					
					       <fmt:formatNumber value="${previousValue}"
					     	   				   	  pattern="$###,###,##0.00"/>
						  </c:if>
				  	</c:when>
				  	<c:otherwise>
				  	    <c:if test="${not empty previousValue}">
					  	   ******
					  	</c:if> &nbsp;
				  	</c:otherwise>
				  </c:choose>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>

			<!-- Plan Year to Date compensation -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">
					Eligible plan YTD compensation
				</td>
			   <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.PlanYearToDateComp%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['planYearToDateComp']}"/>				
				<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change'/>">
						  <c:choose>
						    <c:when test="${employeeForm.viewSalary}">				
						     <c:set var="change" value="${employeeHistory.propertyMap['planYearToDateComp']}"/>
						       <fmt:formatNumber value="${employee.employeeVestingVO.yearToDateCompensation}"
						     	   				   	  pattern="$###,###,##0.00"/>
					  	</c:when>
					  	<c:otherwise>
				  	    <c:if test="${not empty employee.employeeVestingVO.yearToDateCompensation}">
					  	   ******
					  	</c:if> &nbsp;
					  	</c:otherwise>
					  </c:choose>
					</td>
					<c:if test="${showCensusHistoryValue}">				
						<td class="greyborder"> 
						  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
						</td>			
						<c:set var="previousValue" value="${change.previousValue}"/>							
						<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
						  <c:choose>
						    <c:when test="${employeeForm.viewSalary}">				
						      <c:if test="${previousValue != null}">					
							       <fmt:formatNumber value="${previousValue}"
							     	   				   	  pattern="$###,###,##0.00"/>
							  </c:if>
					  	</c:when>
					  	<c:otherwise>
				  	    <c:if test="${not empty previousValue}">
					  	   ******
					  	</c:if> &nbsp;
					  	</c:otherwise>
					  </c:choose>						  
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
			<c:if test="${securityProfile.internalUser}">
			<!-- Mask sensitive information  -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">Mask sensitive information</td>
			   <td width="26" align="right">
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
		        <c:set var="change" value="${employeeHistory.propertyMap['maskSensitiveInformation']}"/>				
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
				       <ps:censusLookup typeName="<%=CensusLookups.BooleanString%>" name="employee" property="employeeDetailVO.maskSensitiveInfoInd"/>
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>			
				   <c:set var="previousValue" value="${change.previousValue}"/>						
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
					      <c:if test="${previousValue != null}">											   
				              <ps:censusLookup typeName="<%=CensusLookups.BooleanString%>" name="change" property="previousValue"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>			
			<!-- Mask sensitive information comment    -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">Mask sensitive information comment</td>
			   <td width="26" align="right">
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
				     <c:set var="change" value="${employeeHistory.propertyMap['maskSensitiveInformationComment']}"/>
${employee.employeeDetailVO.maskSensitiveInfoComments}
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>				
				  <c:set var="previousValue" value="${change.previousValue}"/>					
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						 	<c:out value="${previousValue}"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>			
			</c:if>
			<!-- Year to date plan hours worked    -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">
				   Plan YTD hours worked 
				</td>
			   <td width="26" align="right">
				   <ps:fieldHilight name="<%=EmployeeSnapshotProperties.YearToDatePlanHoursWorked%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['yearToDatePlanHoursWorked']}"/>
				<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change'/>">
${employee.employeeVestingVO.yearToDateHrsWorked}
				        
				        
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>		
					<c:set var="previousValue" value="${change.previousValue}"/>		
					<td class="highlight" align="left" >
						<table>
							<tr>
								<td width="80%" class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">					  
					      			<c:if test="${previousValue != null}">					
						 				<c:out value="${previousValue}"/>
						  			</c:if>
						  		</td>
						  		<td width="20%">
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
					</td>
					
				</c:if>
				
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>			
			<!-- Plan YTD Hrs Worked/Comp Effective Date    -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder" width="1">
				  <img height=1	src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td width="124">
				Plan YTD hours worked/eligible comp effective date
				</td>
			   <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.YearToDatePlanHoursEffDate%>"/>
			   </td>		         
				<td class=greyborder width=1>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['yearToDatePlanHoursEffDate']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">				     
				        <fmt:formatDate value="${employee.employeeVestingVO.yearToDateCompEffDt}" pattern="MM/dd/yyyy"/>				        
				</td>
				<c:if test="${showCensusHistoryValue}">				
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>	
					<c:set var="previousValue" value="${change.previousValueAsDate}"/>			
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
					       <fmt:formatDate value="${previousValue}" pattern="MM/dd/yyyy"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				
			</tr>			
            <tr>
              <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <c:if test="${showCensusHistoryValue}">
              <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              </c:if>
            </tr>			
		</tbody>
	</table>
	</td>
	<td valign="top" height="100%">
	<table height="100%">
	<tr><td valign="top" height="50%">
    <table id="statusHistory" style="DISPLAY: none" border="0" cellpadding="0" cellspacing="0" width="200">
      <tbody>
        <tr>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
          <td width="198"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="4"></td>
          <td width="1"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
        <tr class="tablehead">
          <td class="tableheadTD1" colspan="3"><b>History</b></td>
        </tr>
        <jsp:setProperty name="rowStyle" property="start" value="1"/>
		<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>
              
<c:if test="${not empty statusParamVO.fullStatusList}">
<c:forEach items="${statusParamVO.fullStatusList}" var="statusParam" varStatus="idx" >
<c:set var="indexValue" value="${idx.index}"/> 
<%Integer temp = (Integer)pageContext.getAttribute("indexValue");
EmployeeStatusInfo statusParam =(EmployeeStatusInfo)pageContext.getAttribute("statusParam");
pageContext.setAttribute("temp",temp);
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
        
        <tr>
          <td class="databorder" colspan="3"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
        </tr>
      </tbody>
    </table>			             
	</td>
	</tr>
	<tr >
	<td  valign="bottom" height="50%">
	
	<!-- Plan YTD Hours Worked -->
	
    <table id="planYTDHoursHistory" style="DISPLAY: none"  border="0" cellpadding="0" cellspacing="0" width="200">
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
	</td></tr>
	</table>
	<!-- End Plan YTD Hours worked -->
	</td>
</tr>
</table>
</div>
