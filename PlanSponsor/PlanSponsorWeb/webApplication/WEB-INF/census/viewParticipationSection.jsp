<%@page import="com.manulife.pension.ps.web.Constants"%>
<%@page import="com.manulife.pension.ps.web.controller.UserProfile"%>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="render" uri="manulife/tags/render" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>

<%@ page
	import="com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType"%>
	<%UserProfile userProfile = (UserProfile)session.getAttribute(Constants.USERPROFILE_KEY);
pageContext.setAttribute("userProfile",userProfile,PageContext.PAGE_SCOPE); %>
	
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPATION_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ParticipationSection" />
<content:contentBean
	contentId="<%=ContentConstants.ELIG_CALC_IN_PROGRESS%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="eligCalcInProgress" />
<content:contentBean contentId="<%=ContentConstants.NE_WITHDRAWAL_INITIATED%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="neWithdrawalInitated" />
<content:contentBean contentId="<%=ContentConstants.NE_WITHDRAWAL_IN_PROGRESS%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="neWithdrawalInProgress" />
<content:contentBean contentId="<%=ContentConstants.NE_WITHDRAWAL_COMPLETED%>" type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="neWithdrawalCompleted" />


<c:choose >
  <c:when test="${param.printFriendly == true || employeeForm.expandParticipation}">
    <c:set var="expandStyle" value="DISPLAY:block"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/minus_icon.gif"/>    
  </c:when>
  <c:otherwise>
    <c:set var="expandStyle" value="DISPLAY:none"/>
    <c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif"/>
</c:otherwise>
</c:choose>




<c:choose>
	<c:when test="${not param.printFriendly}">
	<table class=box cellSpacing=0 cellPadding=0
		width=500 border=0>
		<tbody>
				<tr class=tablehead>
				<td class=tableheadTD1>
				    <a href="javascript:toggleSection('participation', 'headerParticipation','expandParticipation', 'expandParticipationIcon')">
				       <img id="expandParticipationIcon" src="<c:out value='${expandIcon}'/>" border="0"/></a>
				    &nbsp;
					<b><content:getAttribute id="ParticipationSection" attribute="text"/></b>
				</td>
				<td class="tableheadTD" id="headerParticipation">
					&nbsp;
				</td>
			</tr>
		</tbody>
	</table>
	</c:when>
	<c:otherwise>
	<table class=box cellSpacing=0 cellPadding=0
		width=500 border=0>
		<tbody>
			<tr class=tablehead>
				<td class=tableheadTD1 colSpan=3>
					<B>Plan participation information </B>
				</td>
			</tr>
		</tbody>
	</table>
	</c:otherwise>
</c:choose>
<form:hidden path="expandParticipation"/>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="2"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<div id="participation" style="<c:out value='${expandStyle}'/>">
<c:remove var="expandStyle"/>


<table border="0" cellpadding="0" cellspacing="0" width="500">
	<tr class="dataCell1">
	<td class="databorder" width="1">
		<img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
	</td>
			<td colspan="4" class="tablesubhead"><b>Eligibility</b><img
			src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
			height="1">
<c:if test="${employeeForm.pendingEligibilityCalculationRequest eq true}">
				<label><span class="style1"><content:getAttribute
							id="eligCalcInProgress" attribute="text" /></span></label>
</c:if>
<c:if test="${employeeForm.pendingEligibilityCalculationRequest ne true}">
<label><span class="style1">&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  </span></label>
</c:if>
		<div align="right"></div>
		</td>
	
		<td width="160" class="tablesubhead">
		<div align="right">
<c:if test="${employeeForm.eligibilityCalcCsfOn eq true}">
<a href="/do/census/eligibilityInformation/?profileId=${employeeForm.profileId}">View eligibility information</a>
</c:if>
		</div>
		</td>
	<td class="databorder" width="1">
		<img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
	</td>
	</tr>
</table>

	<table id="participationTable" cellSpacing="0" cellPadding="0" width="500" border="0" class="greyborder">
		<tbody>
		
			<!-- Long-term Part-time Assessment Year-->
			<tr class="dataCell0">
				<td class=databorder width="1"><img height=1
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<c:if test="${employeeForm.displayLongTermPartTimeAssessmentYearField}">
					<td width="124">Long-term Part-time Assessment Year</td>
					<td width="26" align="right"><ps:fieldHilight
							name="<%=EmployeeSnapshotProperties.LTPTAssessmentYear%>" /></td>
					<td class="greyborder" width="1"><img height=1
						src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
						<c:set var="change" value="${employeeHistory.propertyMap['partTimeQulicationYr']}"/>
					<td class="highlight"
						onmouseover="<ps:employeeChangeDetail name='change'/>">${employeeForm.longTermPartTimeAssessmentYear}</td>
						
					<c:if test="${userProfile.showCensusHistoryValue}">				
						<td class="greyborder" width="1"> 
						  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
						</td>
						
						<td class="highlight" width="172" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
							<c:if test="${change != null}">
								<c:set var="previousValue" value="${change.previousValue}"/>
							      <c:if test="${previousValue != null}">					
								   	${previousValue}
								  </c:if>
							</c:if>
						</td>
					</c:if>
					<td class="databorder" width="1">
						<img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
				</c:if>
			</tr>

			<!-- Eligibility Ind-->
			<tr class="dataCell1">
				<td class=databorder width="1">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">Eligible to participate</td>
			   <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.EligibilityInd%>"/>
			   </td>		         
				<td class="greyborder" width="1"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['eligibilityInd']}"/>
				<td class="highlight" width="<c:out value='${dataColWidth}'/>" onmouseover="<ps:employeeChangeDetail name='change'/>">
				   	<ps:censusLookup typeName="<%=CensusLookups.BooleanString%>" name="employee" property="employeeVestingVO.planEligibleInd"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" width="172" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						   	<ps:censusLookup typeName="<%=CensusLookups.BooleanString%>" name="change" property="previousValue"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
<c:if test="${employeeForm.eligibilityCalcCsfOn == true}">
			<tr class="dataCell2">
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
			<td width="110" valign="top" border="0"><strong>Money
			Type</strong></td>
			<td align="right" valign="top" border="0"></td>
			<td valign="top" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
			<td width="321" valign="top" border="0"><strong>Eligibility
			date</strong></td>
              <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
					<td class="highlight"/>				
				</c:if>
			<td class="databorder" border="0"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
		</tr>
		<!--  Eligibility -->
<c:forEach items="${employeeForm.eligibilityServiceMoneyTypes}" var="eligibityServiceMoneyTypeId" varStatus="indexVal">
<c:set var="index" value="${indexVal.index}"/>

		<tr class="dataCell2">
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
<td width="110" valign="top" border="0">${eligibityServiceMoneyTypeId.moneyTypeName}</td>


			<td width="26" align="right" valign="top" border="0">
			<%
			String index = pageContext.getAttribute("index").toString();
								String calculationOverrideProperty = "eligibilityServiceMoneyTypes["
								+ index + "].calculationOverride";
								String eligibilityDateProperty = "eligibilityServiceMoneyTypes["
								+ index + "].eligibilityDate";
													
					%>
			<ps:fieldHilight name="<%=eligibilityDateProperty%>"/></td>
			<td valign="top" class="greyborder" border="20"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
				
			<c:set var="propertyNameOne" value="eligibityServiceMoneyTypeId["/>
			<c:set var="key" value="${index}"/>
			<c:set var="propertyNameTwo" value="].eligibilityDate"/>
			<c:set var="propertyName" value="${propertyNameOne}${key}${propertyNameTwo}"/>
			<c:set var="change"	 value="${employeeHistory.propertyMap[propertyName]}" />
			<td width="321" valign="top" class="highlight" border="0" onmouseover="<ps:employeeChangeDetail name='change'/>">
${eligibityServiceMoneyTypeId.eligibilityDate}(mm/dd/yyyy)</td>

               
			   <c:if test="${userProfile.showCensusHistoryValue}">
				
				<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
					
					<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue ne null}">
				<fmt:formatDate value="${change.previousValueAsDate}" pattern="MM/dd/yyyy" />
				</c:if>
				</td>
			</c:if>
			<td class="databorder" border="0"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
		</tr>
		<tr class="dataCell2">
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
			<td colspan="2" valign="top">&nbsp;&nbsp;&nbsp;Calculation
			override</td>
			<td valign="top" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
					<c:set var="propertyNameOne" value="eligibityServiceMoneyTypeId["/>
					<c:set var="key" value="${index}"/>
					<c:set var="propertyNameTwo" value="].calculationOverride"/>
					<c:set var="propertyName" value="${propertyNameOne}${key}${propertyNameTwo}"/>
					<c:set var="change"	 value="${employeeHistory.propertyMap[propertyName]}" />
			<td valign="top" class="highlight" onmouseover="<ps:employeeChangeDetail name='change' />"> 
			 <form:checkbox path="eligibilityServiceMoneyTypes[${index}].calculationOverride" value="Y"   disabled="true"/></td> 

               <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
								
					<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					<c:if test="${change.previousValue != null}">
					 <c:choose>
						<c:when test="${change.previousValue=='Y'}">
							<input type="checkbox" checked="checked" disabled="true" />
						</c:when>
						<c:otherwise>
							<input type="checkbox" disabled="true" />
						</c:otherwise>
					</c:choose>
				</c:if>
				</td>			
				</c:if>
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
				height="1"></td>
		</tr>
</c:forEach>
</c:if>
<c:if test="${employeeForm.eligibilityCalcCsfOn !=true}">
			<!-- Eligibility date-->
	          <tr class="dataCell2">
				<td class=databorder>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">Eligibility date</td>
			   <td width="26" align="right">
					<ps:fieldHilight name="<%=EmployeeSnapshotProperties.EligibilityDate%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['eligibilityDate']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
				      <render:date patternOut="MM/dd/yyyy" property="employee.employeeVestingVO.eligibilityDate"/>&nbsp;
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
				    <c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						      <render:date patternOut="MM/dd/yyyy" property="change.previousValueAsDate"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
</c:if>
		 <tr class="dataCell1">
		         <td class="databorder"></td>
		         <td width="110" class="tablesubhead"><b>Deferrals</b></td>
		         <td class="tablesubhead"><img src="/assets/unmanaged/images/spacer.gif" width="1" border="0" height                      ="1"></td>
		         <td colspan="2" class="tablesubhead">&nbsp;</td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td height="1" colspan="2" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>			
				</c:if>
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" border="0" height                    ="1"></td>
		         </tr>
			<!-- Opt-Out -->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">Opt out	</td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.OptOut %>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				  <c:set var="change" value="${employeeHistory.propertyMap['optOut']}"/>
				<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
				   <c:choose>
				     <c:when test="${employee.employeeDetailVO.autoEnrollOptOutInd=='Y'}">
			   			 <input type="checkbox" checked="checked" disabled="true"/>
				   	 </c:when>
				   	 <c:otherwise>
					   	 <input type="checkbox" disabled="true"/>
				   	 </c:otherwise>
				   </c:choose>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>					
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
						   <span >
   					      <c:if test="${previousValue != null}">										
							   <c:choose>
							     <c:when test="${previousValue=='Y'}">
						   			 <input type="checkbox" checked="checked" disabled="true"/>
							   	 </c:when>
							   	 <c:otherwise>
								   	 <input type="checkbox"  disabled="true"/>
							   	 </c:otherwise>
							   </c:choose>
						   </c:if>
		   				   </span>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>

			<!-- AE 90 Days Opt-Out -->
<input type="hidden" name="ae90DaysOptOutDisplayed" /><%--  input - name="employeeForm" --%>
<c:if test="${employeeForm.ae90DaysOptOutDisplayed ==true}">
		          <tr class="<c:out value="${rowStyle.next}"/>">
					<td class=databorder>
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
					<td width="124">90 day withdrawal election	</td>
				   <td width="26" align="right">
			         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.Ae90DaysOptOut %>"/>
				   </td>		         
					<td class="greyborder"> 
					  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					  <c:set var="change" value="${employeeHistory.propertyMap['ae90DaysOptOut']}"/>
					<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change'/>">
					   <c:choose>
					     <c:when test="${employee.employeeDetailVO.ae90DaysOptOutInd=='I'}">
				   			 <input type="checkbox" checked="checked" disabled="true"/>&nbsp;<content:getAttribute id="neWithdrawalInitated" attribute="text"/>
					   	 </c:when>
					   	  <c:when test="${employee.employeeDetailVO.ae90DaysOptOutInd=='P'}">
				   			 <input type="checkbox" checked="checked" disabled="true"/>&nbsp;<content:getAttribute id="neWithdrawalInProgress" attribute="text"/>
					   	 </c:when>
					     <c:when test="${employee.employeeDetailVO.ae90DaysOptOutInd=='C'}">
			   			 	<input type="checkbox" checked="checked" disabled="true"/>&nbsp;<content:getAttribute id="neWithdrawalCompleted" attribute="text"/>
			   			 </c:when>
					   	 <c:otherwise>
						   	 <input type="checkbox" disabled="true"/>
					   	 </c:otherwise>
					   </c:choose>
					</td>
					<c:if test="${userProfile.showCensusHistoryValue}">				
						<td class="greyborder" width="1"> 
						  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
						</td>
						<c:set var="previousValue" value="${change.previousValue}"/>					
						<td class="highlight" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
							   <span >
	 					      <c:if test="${previousValue != null}">										
								   <c:choose>
								     <c:when test="${previousValue=='I'}">
							   			 <input type="checkbox" checked="checked" disabled="true"/>&nbsp;<content:getAttribute id="neWithdrawalInitated" attribute="text"/>
								   	 </c:when>
								   	  <c:when test="${previousValue=='P'}">
							   			 <input type="checkbox" checked="checked" disabled="true"/>&nbsp;<content:getAttribute id="neWithdrawalInProgress" attribute="text"/>
								   	 </c:when>
								   	<c:when test="${previousValue=='C'}">
						   			 	<input type="checkbox" checked="checked" disabled="true"/>&nbsp;<content:getAttribute id="neWithdrawalCompleted" attribute="text"/>
						   			</c:when>
								   	<c:otherwise>
								   		<input type="checkbox"  disabled="true"/>
								   	</c:otherwise>
								   </c:choose>
							   </c:if>
			   				   </span>
						</td>
					</c:if>
					<td class="databorder" width="1">
					 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
				</tr>
</c:if>
			
			<!-- Before tax deferral percentage-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">Before tax deferral percentage</td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.BeforeTaxDefPer%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['beforeTaxDefPer']}"/>
				<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change'/>">
				     <fmt:formatNumber value="${employee.employeeDetailVO.beforeTaxDeferralPct}" pattern="###.###'%'"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${previousValue != null}">					
						      <fmt:formatNumber value="${previousValue}" pattern="###.###'%'"/> 						      
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
			<!-- Before tax flat dollar deferral-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class="databorder">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">Before tax flat dollar deferral</td>
			   <td width="26" align="right">
				<ps:fieldHilight name="<%=EmployeeSnapshotProperties.BeforeTaxFlatDef%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['beforeTaxFlatDef']}"/>
				<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change'/>">
	   				   	 <fmt:formatNumber value="${employee.employeeDetailVO.beforeTaxFlatDollarDeferral}"
	   				   	   pattern="$###,###,##0.00"/>	   				   	   
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						      <fmt:formatNumber value="${previousValue}" pattern="$###,###,##0.00"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>

			<!-- After tax deferral percentage-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">Designated Roth deferral percentage</td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.DesignatedRothDefPer%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['designatedRothDefPer']}"/>
				<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change'/>">
				     <fmt:formatNumber value="${employee.employeeDetailVO.designatedRothDeferralPct}" pattern="###.###'%'"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">						  
					      <c:if test="${previousValue != null}">					
						      <fmt:formatNumber value="${previousValue}" pattern="###.###'%'"/> 						      
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>

			<!-- After tax flat dollar deferral-->
	          <tr class="<c:out value="${rowStyle.next}"/>">
				<td class=databorder>
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				<td width="124">Designated Roth flat dollar deferral</td>
			   <td width="26" align="right">
		         <ps:fieldHilight name="<%=EmployeeSnapshotProperties.DesignatedRothDefAmt%>"/>
			   </td>		         
				<td class="greyborder"> 
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<c:set var="change" value="${employeeHistory.propertyMap['designatedRothDefAmt']}"/>
				<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change'/>">				  
	   				   	 <fmt:formatNumber value="${employee.employeeDetailVO.designatedRothDeferralAmt}"
	   				   	  pattern="$###,###,##0.00"/>
				</td>
				<c:if test="${userProfile.showCensusHistoryValue}">				
					<td class="greyborder" width="1"> 
					  <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
					</td>
					<c:set var="previousValue" value="${change.previousValue}"/>
					<td class="highlight" align="left" onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					      <c:if test="${previousValue != null}">					
						      <fmt:formatNumber value="${previousValue}" pattern="$###,###,##0.00"/>
						  </c:if>
					</td>
				</c:if>
				<td class="databorder" width="1">
				 <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
			</tr>
            <tr>
              <td height="1" colspan="6" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              <c:if test="${userProfile.showCensusHistoryValue}">
              <td height="1" colspan="2" class="databorder"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>
              </c:if>
            </tr>			
		</tbody>
	</table>
</div>
