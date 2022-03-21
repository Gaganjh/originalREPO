<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@page import="com.manulife.pension.ps.web.census.EmployeeSnapshotProperties"%>

<%@page import="com.manulife.pension.ps.web.census.util.CensusLookups"%>
<%@page import="com.manulife.pension.ps.web.content.ContentConstants"%>
<jsp:include flush="true" page="../contract/plan/editHandlers.jsp"></jsp:include>
<content:contentBean
	contentId="<%=ContentConstants.MISCELLANEOUS_PARTICIPATION_SECTION%>"
	type="<%=ContentConstants.TYPE_MISCELLANEOUS%>"
	id="ParticipationSection" />
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
	document.getElementById(id).focus();
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
		
</script>
<table class="box" border="0" cellpadding="0" cellspacing="0" width="500">

    <tbody>
      <tr class="tablehead">
        <td class="tableheadTD1">
			<b><content:getAttribute id="ParticipationSection" attribute="text"/></b>
        </td>
        <td class="tableheadTD" id="headerParticipation">
        </td>
      </tr>
    </tbody>
  </table>

<jsp:useBean id="rowStyle" class="com.manulife.pension.ps.web.census.util.StyleSwitch">
	<jsp:setProperty name="rowStyle" property="start" value="1"/>
	<jsp:setProperty name="rowStyle" property="prefix" value="dataCell"/>	
</jsp:useBean>

<c:set var="disabled" value="${addEmployeeForm.readOnly}" /> 
<form:hidden path="readOnly" id="disableField" /><%--  input - name="addEmployeeForm" --%>
<table border="0" cellpadding="0" cellspacing="0" width="500">
			<tr class="dataCell1">
				
				<td colspan="4" class="tablesubhead"><b>Eligibility</b><img
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
					height="1"> 
				<td class="databorder" width="1">
				  <img height=1 src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
				</td>
						
			</tr>
   </table>
 <table border="0" cellpadding="0" cellspacing="0" width="500">
   <tbody>
           <!--  Eligibility Indicatory -->
     <tr class="<c:out value="${rowStyle.next}"/>">
       <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td width="174">
       Eligible to participate
       </td>
	   <td width="26" align="right">
       <ps:fieldHilight name="<%=EmployeeSnapshotProperties.EligibilityInd %>"/>
	   </td>		         
       <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	   <td width="297" class="highlight">
		    <form:select path="values[eligibilityInd]" cssStyle="boxbody" disabled="${disabled}">
<form:options items="${addEmployeeForm.eligibleOptions}" itemLabel="label" itemValue="value" />
</form:select>
       	   <ps:trackChanges escape="true" property='values[eligibilityInd]' name="addEmployeeForm"/>			 	
		 </td>
		<td class="databorder" width="1">
		 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
		</td>
     </tr>
<c:if test="${addEmployeeForm.eligibilityCalcCsfOn ==true}">

<input type="hidden" id="eligibilityServiceMoneyTypeSize" value="${fn:length(addEmployeeForm.eligibilityServiceMoneyTypes)}" />
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
               
				<td class="databorder" border="0"><img
					src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
					height="1"></td>
			</tr>
			<!--  Eligibility -->
<c:forEach items="${addEmployeeForm.eligibilityServiceMoneyTypes}" var="eligibityServiceMoneyTypeId" varStatus="index" >
<c:set var="indexValue" value="${index.index}"/> 

				    <%
				    String temp = pageContext.getAttribute("indexValue").toString();
								String calculationOverrideProperty = "eligibilityServiceMoneyTypes["
								+ temp + "].calculationOverride";
								String eligibilityDateProperty = "eligibilityServiceMoneyTypes["
								+ temp + "].eligibilityDate";
													
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
<form:input path="eligibilityServiceMoneyTypes[${indexValue}].eligibilityDate" disabled="${disabled.booleanValue()}" onblur="validateEligibilityDate(this);checkCalculationOverride(this);" size="6" cssClass="inputAmount" id="${indexValue}" /> 

					&nbsp; <c:if test="${!disabled}">
					<img onclick="return handleDateIconClicked(event,'${indexValue}');" src="/assets/unmanaged/images/cal.gif" border="0">
					(mm/dd/yyyy)</c:if></td>
                
					<td class="databorder" border="0"><img
						src="/assets/unmanaged/images/spacer.gif" width="1" border="0"
						height="1"></td>
					<ps:trackChanges property="<%=eligibilityDateProperty%>"  name="addEmployeeForm"/>
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
                  
 <form:checkbox path="eligibilityServiceMoneyTypes[${indexValue}].calculationOverride" id="${indexValue}" onclick="resetEligibilityDate(this);" value="Y" /></td> 


                  
			   <td class="databorder"> 
				  <img height="1" src="/assets/unmanaged/images/spacer.gif" width=1 border=0>
				</td>
					
				</tr>
				 <script>
			   var id= <%=temp%>;
			   var calculationOverrideField='eligibilityServiceMoneyTypes['+id+'].calculationOverride';
	           var eligibilityDateField='eligibilityServiceMoneyTypes['+id+'].eligibilityDate';
	           var eligibilityDate=document.getElementsByName(eligibilityDateField);
	           var disabled=document.getElementById("disableField");
	           var calculationOverride=document.getElementsByName(calculationOverrideField);
	          
               if(disabled.value != 'true') {
            	   if(eligibilityDate[0] != null && eligibilityDate[0].value !=""  ){
        	           calculationOverride[0].checked =true;
        	           calculationOverride[0].disabled=false;
        	           }else{
        	           calculationOverride[0].checked =false;
        	           calculationOverride[0].disabled=true;
        	           }
               } else {
            	   if(eligibilityDate[0] != null && eligibilityDate[0].value !=""  ){
        	           calculationOverride[0].checked =true;
        	           calculationOverride[0].disabled=true;
        	           }else{
        	           calculationOverride[0].checked =false;
        	           calculationOverride[0].disabled=true;
        	           }
               }
	           
	           
			 </script>
</c:forEach>
</c:if>
		
<c:if test="${addEmployeeForm.eligibilityCalcCsfOn !=true}">
		<!-- Eligibility date -->
		<tr class="<c:out value="${rowStyle.next}"/>">
			<td class="databorder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
			<td width="100">Eligibility date</td>
			<td width="26" align="right"><ps:fieldHilight
				name="<%=EmployeeSnapshotProperties.EligibilityDate%>" /></td>
			<td class="greyborder"><img height="1"
				src="/assets/unmanaged/images/spacer.gif" width=1 border=0></td>
<td class="highlight"><form:input path="values[eligibilityDate]"  id="value(eligibilityDate)" onchange="validateEligibilityDate(this)" size="10" cssClass="inputAmount" id="value(eligibilityDate)"/> &nbsp;
 
			<c:if test="${!disabled}" >
				<a href="javascript:doCalendar('value(eligibilityDate)',0)"> <img
					src="/assets/unmanaged/images/cal.gif" border="0"></a>
						  (mm/dd/yyyy)
						  </c:if> <img src="/assets/unmanaged/images/spacer.gif" border="0"
				height="1" width="1"> <ps:trackChanges escape="true"
				property='values[eligibilityDate]' name="addEmployeeForm"/></td>
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
		
		
		<tr class="dataCell1">
		         <td class="databorder"></td>
		         <td width="110" class="tablesubhead"><b>Deferrals</b></td>
		         
		         <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" width="1" border="0" height="1"></td>
		         <td colspan="2" class="tablesubhead">&nbsp;</td>
				 
		         <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" width="1" border="0" height="1"></td>
		         </tr>
		
		
           <!--  Opt-out -->
     <tr class="<c:out value="${rowStyle.next}"/>">
       <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td width="174">
       Opt out
       </td>
	   <td width="26" align="right">
       <ps:fieldHilight name="<%=EmployeeSnapshotProperties.OptOut %>"/>
	   </td>		         
       <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	 <td width="297" class="highlight">		            
<form:checkbox path="values[optOut]" disabled="${disabled.booleanValue()}" value="Y"/>
        <ps:trackChanges escape="true" property='values[optOut]' name="addEmployeeForm"/>
	 </td>
	<td class="databorder" width="1">
	 <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
	</td>
     </tr>
     
     <!-- AE 90 Days Opt-Out -->
<form:hidden path="ae90DaysOptOutDisplayed" />
<c:if test="${addEmployeeForm.ae90DaysOptOutDisplayed ==true}">
	     <tr class="<c:out value="${rowStyle.next}"/>">
	       <td width="1" class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	       <td width="174">
	       90 day withdrawal election
	       </td>
		   <td width="26" align="right">
	       <ps:fieldHilight name="<%=EmployeeSnapshotProperties.Ae90DaysOptOut %>"/>
		   </td>		         
	       <td width="1" class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		 <td width="297" class="highlight">		            
<form:checkbox path="values['ae90DaysOptOut']" disabled="true" value="I"/>
	     </td>
		<td class="databorder" width="1">
		 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
		</td>
	     </tr>
</c:if>

<!-- Before tax deferral percentage   -->
     <tr class="<c:out value="${rowStyle.next}"/>">
       <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td width="174">
       Before tax deferral percentage
       </td>
	   <td width="26" align="right">
       <ps:fieldHilight name="<%=EmployeeSnapshotProperties.BeforeTaxDefPer%>"/>
	   </td>		         
       <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		 <td class="highlight">	        	   
<form:input path="values[beforeTaxDefPer]" disabled="${disabled}" maxlength="7" onchange="validateBeforeTaxDefPer(this)" size="10" cssStyle="{text-align: right}"/> %



		   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		        	   <ps:trackChanges escape="true" property='values[beforeTaxDefPer]' name="addEmployeeForm"/>
		 </td>
		<td class="databorder">
		 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
		</td>
     </tr>

	<!-- Before tax flat dollar deferral  -->
     <tr class="<c:out value="${rowStyle.next}"/>">
       <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td width="174">
       Before tax flat dollar deferral
       </td>
	   <td width="26" align="right">
       <ps:fieldHilight name="<%=EmployeeSnapshotProperties.BeforeTaxFlatDef%>"/>
	   </td>		         
       <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
		 <td class="highlight">	        	   
$<form:input path="values[beforeTaxFlatDef]" disabled="${disabled}" onblur="validateBeforeTaxDefDollar(this)" cssStyle="{text-align: right}"/>



		   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
		        	   <ps:trackChanges escape="true" property='values[beforeTaxFlatDef]' name="addEmployeeForm"/>			 	
		 </td>
		<td class="databorder">
		 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
		</td>
     </tr>

	<!-- After tax deferral percentage  -->
     <tr class="<c:out value="${rowStyle.next}"/>">
       <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td width="174">
       Designated Roth deferral percentage
       </td>
	   <td width="26" align="right">
       <ps:fieldHilight name="<%=EmployeeSnapshotProperties.DesignatedRothDefPer%>"/>
	   </td>		         
       <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
	   <td class="highlight">	        	   
<form:input path="values[designatedRothDefPer]" disabled="${disabled}" maxlength="7" onchange="validateDesignatedRothDefPer(this)" size="10" cssStyle="{text-align: right}"/> %



        <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        <ps:trackChanges escape="true" property='values[designatedRothDefPer]' name="addEmployeeForm"/>			 	
	 </td>
	<td class="databorder">
	 <img height="1" src="/assets/unmanaged/images/spacer.gif" width="1" border="0">
	</td>
     </tr>

<!-- After tax flat dollar deferral  -->
     <tr class="<c:out value="${rowStyle.next}"/>">
       <td class="databorder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
       <td width="174">
       Designated Roth flat dollar deferral
       </td>
	   <td width="26" align="right">
       <ps:fieldHilight name="<%=EmployeeSnapshotProperties.DesignatedRothDefAmt%>"/>
	   </td>		         
       <td class="greyborder"><img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1"></td>
 <td class="highlight">	        	   
$<form:input path="values[designatedRothDefAmt]" disabled="${disabled}" onblur="validateDesignatedRothDefAmt(this)" cssStyle="{text-align: right}"/>



   <img src="/assets/unmanaged/images/spacer.gif" border="0" height="1" width="1">
        	   <ps:trackChanges escape="true" property='values[designatedRothDefAmt]' name="addEmployeeForm"/>			 	
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
