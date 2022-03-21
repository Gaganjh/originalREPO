
<%@ taglib prefix="content" uri="manulife/tags/content"%>
<%@ taglib prefix="ps" uri="manulife/tags/ps"%>

<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page
	import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>

<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>

<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>

<%@ page
	import="com.manulife.pension.ps.web.contract.csf.EligibilityCalculationMoneyType"%>
<jsp:include flush="true" page="../contract/plan/editHandlers.jsp"></jsp:include>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPATION_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ParticipationSection" />
<content:contentBean
	contentId="<%=ContentConstants.NE_WITHDRAWAL_INITIATED%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="neWithdrawalInitated" />
<content:contentBean
	contentId="<%=ContentConstants.NE_WITHDRAWAL_IN_PROGRESS%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="neWithdrawalInProgress" />
<content:contentBean
	contentId="<%=ContentConstants.NE_WITHDRAWAL_COMPLETED%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="neWithdrawalCompleted" />
<content:contentBean
	contentId="<%=ContentConstants.ELIG_CALC_IN_PROGRESS%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="eligCalcInProgress" />

<c:set var="disabled" value="${editEmployeeForm.readOnly}" /> 

<c:set var="optOutCheckboxDisabled" value="${editEmployeeForm.optOutDisabled}" /> 
<%String optOutCheckboxDisabled= pageContext.getAttribute("optOutCheckboxDisabled").toString(); %>
<c:set var="ae90DaysWithdrawalDisabled" value="${editEmployeeForm.ae90DaysOptOutDisabled}" /> 
<c:set var="isPayrollFeedbackServiceEnabled" value="${userProfile.isPayrollFeedbackServiceEnabled()}" />
<%

String ae90DaysWithdrawalDisabled= pageContext.getAttribute("ae90DaysWithdrawalDisabled").toString(); %>
<c:set var="ae90DaysOptOutDatabaseSet" value="${editEmployeeForm.ae90DaysOptOutIndDatabaseSet}" /> 

<c:set var="eligibilityServiceMoneyTypeList" value="${editEmployeeForm.eligibilityServiceMoneyTypes}" />








<form:hidden path="expandParticipation"/>
<form:hidden path="readOnly" />

<c:choose>
	<c:when test="${editEmployeeForm.expandParticipation}">
		<c:set var="expandStyle" value="DISPLAY: block" />
		<c:set var="expandIcon"
			value="/assets/unmanaged/images/minus_icon.gif" />
	</c:when>
	<c:otherwise>
		<c:set var="expandStyle" value="DISPLAY: none" />
		<c:set var="expandIcon" value="/assets/unmanaged/images/plus_icon.gif" />
	</c:otherwise>
</c:choose>

<table class="box" border="0" cellpadding="0" cellspacing="0"
	width="500">

	<tbody>
		<tr class="tablehead">
			<td class="tableheadTD1"><a
				href="javascript:toggleSection('participation', 'headerParticipation','expandParticipation', 'expandParticipationIcon')">
			<img id="expandParticipationIcon"
				src="<c:out value='${expandIcon}'/>" border="0" /></a>&nbsp; <b><content:getAttribute
				id="ParticipationSection" attribute="text" /></b></td>
			<td class="tableheadTD" id="headerParticipation"></td>
		</tr>
	</tbody>
</table>
<jsp:useBean id="rowStyle"
	class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1" />
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell" />
</jsp:useBean>
<script type="text/javascript" >
	
	function checkCalculationOverride(obj){
	var index=obj.id;
	setCalculationOverrideField(obj,index);
	}

   function setCalculationOverrideField(obj,index){
    var calculationOverrideField='eligibilityServiceMoneyTypes['+index+'].calculationOverride';
	var calculationOverride=document.getElementsByName(calculationOverrideField);
	if(obj!=null && obj.value !=""){
	calculationOverride[0].disabled=false;
	calculationOverride[0].checked =true;
	}else{
	calculationOverride[0].checked =false;
	calculationOverride[0].disabled=true;
	}
	}

   function resetEligibilityDate(obj){
		
	var index=obj.id;
	resetEligibilityDateField(obj,index);
   }

   function resetEligibilityDateField(obj,index){
	
	var calculationOverrideField='eligibilityServiceMoneyTypes['+index+'].calculationOverride';
	var calculationOverride=document.getElementsByName(calculationOverrideField);
	var eligibilityDateField='eligibilityServiceMoneyTypes['+index+'].eligibilityDate';
		
	
	if(calculationOverride[0].checked == false){
		var eligibilityDate=document.getElementsByName(eligibilityDateField);
		eligibilityDate[0].value="";
		calculationOverride[0].disabled=true;
	}
	}
/**
 * Generic handler for date icon clicked
 */ 
 function handleDateIconClicked(evt, fieldId) {
    // Retrieve the field
    var field = document.getElementById(fieldId);
    // Pre-Validate date and blank if not valid
    if (!validateMMddYYYY(field, false)) {
      field.value = '';
    }
    // Popup calendar
    handleCalendarSetup(field, evt,fieldId);
 }	
  /**
 * Handler for calendar window setup
 */ 
function handleCalendarSetup(field, event,fieldId) {

   Calendar = new calendar(field);
   Calendar.year_scroll = true;
   Calendar.time_comp = false;
   
   // Modify calendar position to be slightly above and to right of mouse click
   yPosition = event.screenY - 150;
   xPosition = event.screenX + 80;
   Calendar.popup();
  }
  function setFocus(id) {
  if(document.getElementById(id)!=null){
		document.getElementById(id).onchange();
	}
	}

</script>

<div id="participation" style="<c:out value='${expandStyle}'/>">
		<table border="0" cellpadding="0" cellspacing="0" width="500">
			<tr class="dataCell1">
				<td class="databorder" width="1">
					<img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				<td colspan="4" class="tablesubhead"><b>Eligibility</b><img
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
					height="1"> 
					
<c:if test="${editEmployeeForm.pendingEligibilityCalculationRequest ==true}">

					<label><span class="style1"><content:getAttribute
							id="eligCalcInProgress" attribute="text" /></span></label></c:if>
				<div align="right"></div>
				</td>
				<td class="databorder" width="1">
					<img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>		
			</tr>
		</table>
		

<table border="0" cellpadding="0" cellspacing="0" width="500">
	<tbody>
		<!--  LTPT Assessment Year -->
			<tr class="<c:out value="${rowStyle.next}"/>">
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
				<c:if test="${editEmployeeForm.displayLongTermPartTimeAssessmentYearField}">
				<td width="100">Long-term Part-time Assessment Year</td>
				<td width="26" align="right"><ps:fieldHilight
						name="<%=EmployeeSnapshotProperties.LTPTAssessmentYear%>" /></td>
				<td width="1" class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"	width="1"></td>
				<td width="<c:out value="${dataColWidth}"/>" class="highlight">
					<form:select path="longTermPartTimeAssessmentYear" cssStyle="boxbody" disabled="${disabled}">
						<form:options items="${editEmployeeForm.longTermPartTimeAssessmentYearOptions}"	itemLabel="label" itemValue="value" />
					</form:select> 
					<ps:trackChanges escape="true" collectionProperty="true" property='values[longTermPartTimeAssessmentYear]' name="editEmployeeForm" />
				</td>
				
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder" width="1"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change"
					value="${employeeHistory.propertyMap['partTimeQulicationYr']}" />
				<td class="highlight" width="130"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change != null and change.previousValue != null}">
						${change.previousValue}
				</c:if> &nbsp;</td>
			</c:if>
				
				<td class="databorder" width="1"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
				</c:if>
			</tr>
			<!--  Eligibility -->
		<tr class="<c:out value="${rowStyle.next}"/>">
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="100">Eligible to participate</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.EligibilityInd %>" /></td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="<c:out value="${dataColWidth}"/>" class="highlight">
 <form:select path="values[eligibilityInd]" cssStyle="boxbody" disabled="${disabled}">

<form:options items="${editEmployeeForm.eligibleOptions}" itemLabel="label" itemValue="value"/>
</form:select>  <ps:trackChanges escape="true" collectionProperty="true" property='values[eligibilityInd]' name="editEmployeeForm" /></td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder" width="1"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change"
					value="${employeeHistory.propertyMap['eligibilityInd']}" />
				<td class="highlight" width="130"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue != null}">
					<ps:censusLookup typeName="<%=CensusLookups.BooleanString%>"
						name="change" property="previousValue" />
				</c:if> &nbsp;</td>
			</c:if>
			<td class="databorder" width="1"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
<c:if test="${editEmployeeForm.eligibilityCalcCsfOn ==true}">

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
			<input type="hidden" id="eligibilityServiceMoneyTypeSize" value="${fn:length(editEmployeeForm.eligibilityServiceMoneyTypes)}" />
			
			
<c:forEach items="${editEmployeeForm.eligibilityServiceMoneyTypes}" var="eligibityServiceMoneyTypeId" varStatus="index" >

<c:set var="indexvalue" value="${index.index}"/>
				    <%
				    String index = pageContext.getAttribute("indexvalue").toString();
								String calculationOverrideProperty = "eligibityServiceMoneyTypeId["
								+ index + "].calculationOverride";
								String eligibilityDateProperty = "eligibityServiceMoneyTypeId["
								+ index + "].eligibilityDate";
													
					%>
				<tr class="dataCell2">
					<td class="databorder"><img
						src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
						height="1"></td>
<td width="110" valign="top" border="0">${eligibityServiceMoneyTypeId.moneyTypeName}</td>

					<td width="26" align="right" valign="top" border="0">
					<ps:fieldHilight name="<%=eligibilityDateProperty %>" /></td>
					<td valign="top" class="greyborder" border="20"><img
						src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
						height="1"></td>
				
<td width="321" valign="top" class="highlight" border="0">

<form:input path="eligibilityServiceMoneyTypes[${indexvalue}].eligibilityDate" disabled="${disabled}" onchange="validateEligibilityDate(this);checkCalculationOverride(this);" size="10" cssClass="inputAmount" id="<%=String.valueOf(index)%>" /><%-- indexed="true" name="eligibityServiceMoneyTypeId" --%>

					&nbsp; 
					<c:if test="${!disabled}">
					<img onclick="return handleDateIconClicked(event,'<%=String.valueOf(index)%>');" src="/assets/unmanaged/images/cal.gif" border="0">(mm/dd/yyyy)</td>
					</c:if>
                <c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
					
					<c:set var="propertyNameOne" value="eligibityServiceMoneyTypeId["/>
					<c:set var="key" value="${indexvalue}"/>
					<c:set var="propertyNameTwo" value="].eligibilityDate"/>
					<c:set var="propertyName" value="${propertyNameOne}${key}${propertyNameTwo}"/>
					<c:set var="change"	 value="${employeeHistory.propertyMap[propertyName]}" />
					<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					
				<c:if test="${change.previousValue != null}">
				
				<fmt:formatDate value="${change.previousValueAsDate}" pattern="MM/dd/yyyy" />
				</c:if>
				</td>
			</c:if>
					<td class="databorder" border="0"><img
						src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
						height="1"></td>
						<% if (eligibilityDateProperty==null) { %>
						<script type="text/javascript" >
              changeTracker.trackElement('eligibilityServiceMoneyTypes[${indexvalue}].eligibilityDate',false);
          </script>
          <% } else { %>
          <script type="text/javascript" >
              changeTracker.trackElement('eligibilityServiceMoneyTypes[${indexvalue}].eligibilityDate',null);
          </script>
          <% } %>
          
					<%-- <ps:trackChanges property="<%=eligibilityDateProperty%>" name="editEmployeeForm"/> --%>
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
					<td valign="top" class="highlight">
					

							
 <form:checkbox path="eligibilityServiceMoneyTypes[${indexvalue}].calculationOverride" onclick="resetEligibilityDate(this);" value="Y" id="<%=String.valueOf(index)%>"  /></td> 


                  <c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
					
					<c:set var="propertyNameOne" value="eligibityServiceMoneyTypeId["/>
					<c:set var="key" value="${indexvalue}"/>
					<c:set var="propertyNameTwo" value="].calculationOverride"/>
					 <c:set var="propertyName" value="${propertyNameOne}${key}${propertyNameTwo}"/> 
					<c:set var="change"	 value="${employeeHistory.propertyMap[propertyName]}" />
					
					<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					<c:if test="${change.previousValue != null}">
				
				 <c:choose>
						<c:when test="${change.previousValue=='Y'}">
							<input type="checkbox" checked disabled="disabled" />
						</c:when>
						<c:otherwise>
							<input type="checkbox" disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</c:if>
				</td>
				</c:if>
			   <td class="databorder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
							
				</tr>
			  <script>
			   var id= <%=index%>;
			   var calculationOverrideField='eligibilityServiceMoneyTypes['+id+'].calculationOverride';
	           var eligibilityDateField='eligibilityServiceMoneyTypes['+id+'].eligibilityDate';
	           var eligibilityDate=document.getElementById(eligibilityDateField);
	           var disabled=document.getElementById("readOnly");
	           var calculationOverride=document.getElementsByName(calculationOverrideField);
	           if(calculationOverride[0].checked ==true && disabled.value != 'true'){
	             calculationOverride[0].disabled=false;
	           }else{
	             calculationOverride[0].disabled=true;
	           }
			 </script>
</c:forEach>
</c:if>
<c:if test="${editEmployeeForm.eligibilityCalcCsfOn !=true}">
		<!-- Eligibility date -->
		<tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
			<td width="100">Eligibility date</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.EligibilityDate%>" /></td>
			<td class="greyborder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
<td class="highlight">
<form:input path="values[eligibilityDate]" disabled="${disabled}" onchange="validateEligibilityDate(this)" size="10" cssClass="inputAmount" id="value(eligibilityDate)"/> &nbsp; 
 <c:if
				test="${!disabled}">
				<a href="javascript:doCalendar('value(eligibilityDate)',0)"> <img
					src="/assets/unmanaged/images/cal.gif" border="0"></a>
						  (mm/dd/yyyy)
						  </c:if> <img src="/assets/unmanaged/images/spacer.gif" border="0"
				height="1" width="1"> <ps:trackChanges escape="true"
				collectionProperty="true" property='values[eligibilityDate]' name="editEmployeeForm"/></td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change"
					value="${employeeHistory.propertyMap['eligibilityDate']}" />
				<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue != null}">
					<fmt:formatDate value="${change.previousValueAsDate}"
						pattern="MM/dd/yyyy" />
				</c:if></td>
			</c:if>
			<td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
</c:if>
		<tr class="dataCell1" style="width: 499">
		         <td class="databorder"></td>
		         <td width="110" class="tablesubhead"><b>Deferrals</b></td>
		         <td class="tablesubhead"><img src="/assets/unmanaged/images/spacer.gif" width="1" border="0" height="1"></td>
		         <td colspan="2" class="tablesubhead">&nbsp;</td>
				 <c:if test="${userProfile.showCensusHistoryValue}">
					<td height="1" colspan="2" class="tablesubhead"><img src="/assets/unmanaged/images/s.gif" height="1" width="1"></td>			
				</c:if>
		         <td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0"></td>
		         </tr>
		<!--  Opt-out -->
<form:hidden path="optOutDisabled" />

		<tr class="<c:out value="${rowStyle.next}"/>">
			<td width="1" class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="100">Opt out</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.OptOut %>" /></td>
			<td width="1" class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="<c:out value="${dataColWidth}"/>" class="highlight">
 <form:checkbox path="values[optOut]" disabled="${(optOutCheckboxDisabled || disabled)}" value="Y"/> 

			<ps:trackChanges escape="true" collectionProperty="true" property='values[optOut]' name="editEmployeeForm"/></td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder" width="1"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change" value="${employeeHistory.propertyMap['optOut']}" />
				<td class="highlight" width="130"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue != null}">
					<c:choose>
						<c:when test="${change.previousValue=='Y'}">
							<input type="checkbox" checked="checked" disabled="disabled" />
						</c:when>
						<c:otherwise>
							<input type="checkbox" disabled="disabled" />
						</c:otherwise>
					</c:choose>
				</c:if> &nbsp;</td>
			</c:if>
			<td class="databorder" width="1"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>

		 <%
		if (optOutCheckboxDisabled=="true") {
		%> 
<input type="hidden" name="value(optOut)"/>
		<%
		}
		%>  

		<!-- AE 90 Days Opt-Out -->
<input type="hidden" name="ae90DaysOptOutDisplayed" /><%--  input - name="editEmployeeForm" --%>

<c:if test="${editEmployeeForm.ae90DaysOptOutDisplayed ==true}">

			<tr class="<c:out value="${rowStyle.next}"/>">
				<td width="1" class="databorder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="100">90 day withdrawal election</td>
				<td width="26" align="right"><ps:fieldHilight
					name="<%=EmployeeSnapshotProperties.Ae90DaysOptOut %>" /></td>
				<td width="1" class="greyborder"><img
					src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
					width="1"></td>
				<td width="<c:out value="${dataColWidth}"/>" class="highlight">
				<c:choose>
					<c:when test="${editEmployeeForm.values['ae90DaysOptOut'] == 'C'}">
  <form:checkbox path="values[ae90DaysOptOut]" disabled="${(ae90DaysWithdrawalDisabled || disabled)}" value="C"/>&nbsp;<content:getAttribute  

							id="neWithdrawalCompleted" attribute="text" />
					</c:when>
					<c:when test="${editEmployeeForm.values['ae90DaysOptOut'] == 'P'}">
  <form:checkbox path="values[ae90DaysOptOut]" disabled="${(ae90DaysWithdrawalDisabled || disabled)}" value="P"/>&nbsp;<content:getAttribute 

							id="neWithdrawalInProgress" attribute="text" />
					</c:when>
					<c:when test="${editEmployeeForm.values['ae90DaysOptOut'] == 'I'}">
  <form:checkbox path="values[ae90DaysOptOut]" disabled="${(ae90DaysWithdrawalDisabled || disabled)}" value="I"/>&nbsp; 

						   	<%-- <%
						   	if (ae90DaysOptOutDatabaseSet.booleanValue()) {
						   	%>
						<content:getAttribute id="neWithdrawalInitated" attribute="text" />
						<%
						}
						%> --%>
					</c:when>
					<c:otherwise>
  <form:checkbox path="values[ae90DaysOptOut]" disabled="${(ae90DaysWithdrawalDisabled || disabled) }" value="I"/>  

					</c:otherwise>
				</c:choose>   <%
 if (ae90DaysWithdrawalDisabled=="true") {
%> <input type="hidden" name="value(ae90DaysOptOut)"/> <%
 }
 %>  <ps:trackChanges escape="true" collectionProperty="true" property='values[ae90DaysOptOut]' name="editEmployeeForm" /></td>
				<c:if test="${userProfile.showCensusHistoryValue}">
					<td class="greyborder" width="1"><img height="1"
						src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
					</td>
					<c:set var="change"
						value="${employeeHistory.propertyMap['ae90DaysOptOut']}" />
					<td class="highlight" width="130"
						onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
					<c:if test="${change.previousValue != null}">
						<c:choose>
							<c:when test="${change.previousValue=='I'}">
								<input type="checkbox" checked="checked" disabled="disabled" />&nbsp;<content:getAttribute
									id="neWithdrawalInitated" attribute="text" />
							</c:when>
							<c:when test="${change.previousValue=='P'}">
								<input type="checkbox" checked="checked" disabled="disabled" />&nbsp;<content:getAttribute
									id="neWithdrawalInProgress" attribute="text" />
							</c:when>
							<c:when test="${change.previousValue=='C'}">
								<input type="checkbox" checked="checked" disabled="disabled" />&nbsp;<content:getAttribute
									id="neWithdrawalCompleted" attribute="text" />
							</c:when>
							<c:otherwise>
								<input type="checkbox" disabled="disabled" />
							</c:otherwise>
						</c:choose>
					</c:if> &nbsp;</td>
				</c:if>
				<td class="databorder" width="1"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
				</td>
			</tr>
</c:if>
<c:if test="${editEmployeeForm.ae90DaysOptOutDisplayed ==false}">

<input type="hidden" name="value(ae90DaysOptOut)"/>
</c:if>
		<!-- Before tax deferral percentage   -->
		<tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="100">Before tax deferral percentage</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.BeforeTaxDefPer%>" /></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>				
<td class="highlight"> <form:input path="values[beforeTaxDefPer]" disabled="${(isPayrollFeedbackServiceEnabled || disabled)}" maxlength="7" onchange="validateBeforeTaxDefPer(this)" size="10" cssStyle="{text-align: right}"/>  % <img



				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"> <ps:trackChanges escape="true"
				collectionProperty="true" property='values[beforeTaxDefPer]' name="editEmployeeForm"/></td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change"
					value="${employeeHistory.propertyMap['beforeTaxDefPer']}" />
				<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue != null}">
					<fmt:formatNumber value="${change.previousValue}"
						pattern="###.###'%'" />
				</c:if> &nbsp;</td>
			</c:if>
			<td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>

		<!-- Before tax flat dollar deferral  -->
		<tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="100">Before tax flat dollar deferral</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.BeforeTaxFlatDef%>" /></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
<td class="highlight">$  <form:input path="values[beforeTaxFlatDef]" disabled="${(isPayrollFeedbackServiceEnabled || disabled)}" onblur="validateBeforeTaxDefDollar(this)" size="10" cssStyle="{text-align: right}"/> 
 


			<img	src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"> <ps:trackChanges escape="true"
				collectionProperty="true" property='values[beforeTaxFlatDef]' name="editEmployeeForm"/></td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change"
					value="${employeeHistory.propertyMap['beforeTaxFlatDef']}" />
				<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue != null}">
					<fmt:formatNumber value="${change.previousValue}"
						pattern="$###,###,###.00" />
				</c:if> &nbsp;</td>
			</c:if>
			<td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>
		<!-- After tax deferral percentage  -->
		<tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="100">Designated Roth deferral percentage</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.DesignatedRothDefPer%>" /></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
<td class="highlight"> <form:input path="values[designatedRothDefPer]" disabled="${(isPayrollFeedbackServiceEnabled || disabled)}" maxlength="7" onchange="validateDesignatedRothDefPer(this)" size="10" cssStyle="{text-align: right}"/>  % <img




				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"> <ps:trackChanges escape="true"
				collectionProperty="true" property='values[designatedRothDefPer]' name="editEmployeeForm"/></td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change"
					value="${employeeHistory.propertyMap['designatedRothDefPer']}" />
				<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue != null}">
					<fmt:formatNumber value="${change.previousValue}"
						pattern="###.###'%'" />
				</c:if> &nbsp;</td>
			</c:if>
			<td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
			</td>
		</tr>



		<!-- After tax flat dollar deferral  -->
		<tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
			<td width="100">Designated Roth flat dollar deferral</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.DesignatedRothDefAmt%>" /></td>
			<td class="greyborder"><img
				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"></td>
<td class="highlight">$  <form:input path="values[designatedRothDefAmt]" disabled="${(isPayrollFeedbackServiceEnabled || disabled)}" onblur="validateDesignatedRothDefAmt(this)" size="10" cssStyle="{text-align: right}"/>  <img



				src="/assets/unmanaged/images/spacer.gif" border="0" height="1"
				width="1"> <ps:trackChanges escape="true"
				collectionProperty="true" property='values[designatedRothDefAmt]' name="editEmployeeForm"/></td>
			<c:if test="${userProfile.showCensusHistoryValue}">
				<td class="greyborder"><img height="1"
					src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
				<c:set var="change"
					value="${employeeHistory.propertyMap['designatedRothDefAmt']}" />
				<td class="highlight"
					onmouseover="<ps:employeeChangeDetail name='change' current='false'/>">
				<c:if test="${change.previousValue != null}">
					<fmt:formatNumber value="${change.previousValue}"
						pattern="$###,###,###.00" />
				</c:if> &nbsp;</td>
			</c:if>
			<td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
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
  </div> 
