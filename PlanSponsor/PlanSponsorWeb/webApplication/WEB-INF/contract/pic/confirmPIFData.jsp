<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
        
<%@ taglib prefix="ps" uri="manulife/tags/ps" %>
<%@ taglib prefix="content" uri="manulife/tags/content" %>
<%@ taglib prefix="un" uri="http://jakarta.apache.org/taglibs/unstandard-1.0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page import="com.manulife.pension.ps.web.content.ContentConstants" %>
<%@ page import="com.manulife.pension.ps.web.controller.UserProfile"%>

<un:useConstants var="formConstants" className="com.manulife.pension.ps.web.pif.PIFDataForm"/>

<%-- Define required collections --%>
<c:set var="vestingSchedules" scope="request" value="${pifDataForm.lookupData['VESTING_SCHEDULES']}"/>
<c:set var="vestingServiceCreditMethods" scope="request" value="${pifDataForm.lookupData['VESTING_SERVICE_CREDIT_METHOD']}"/>
<c:set var="employerMoneyTypes" scope="request" value="${pifDataForm.pifDataUi.employerMoneyTypeList}"/>
<c:set var="withdrawalReasons" scope="request" value="${pifDataForm.lookupData['PLAN_WITHDRAWAL_REASONS']}"/>

<c:set var="eligibilityPlanEntryFrequencies" scope="request" value="${pifDataForm.lookupData['PLAN_FREQUENCY']}"/>
<c:set var="eligibilityCreditMethods" scope="request" value="${pifDataForm.lookupData['SERVICE_CREDITING_METHOD']}"/>
<c:set var="eligibilityMinimumAges" scope="request" value="${pifDataForm.lookupData['MINIMUM_AGE']}"/>
<c:set var="eligibilityHoursOfServices" scope="request" value="${pifDataForm.lookupData['HOURS_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServices" scope="request" value="${pifDataForm.lookupData['PERIOD_OF_SERVICE']}"/>
<c:set var="eligibilityPeriodOfServiceUnits" scope="request" value="${pifDataForm.lookupData['PERIOD_OF_SERVICE_UNIT']}"/>

<%-- Define variables --%>
<c:set var="disableFields" scope="request" value="true"/>
<c:if test="${pifDataForm.confirmMode}">
<c:set var="disableFieldsForAutoOrSignUp" scope="request" value="false"/>
<c:set var="disableFieldsForAuto" scope="request" value="false"/>
</c:if>

<content:contentBean
  contentId="<%=ContentConstants.ACKNOWLEDGEMENT_TEXT_FOR_TPA_USER%>"
  type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="acknowledgementText" /> 
  
<content:contentBean
  contentId="80715"
  type="<%=ContentConstants.TYPE_DISCLAIMER%>" id="exitMessage" />   

<%-- Defines page level event handlers --%>
<script type="text/javascript">

var exitMessage = "<content:getAttribute beanName='exitMessage' attribute='text'  filter='true' escapeJavaScript="true"/>";

function doExit(){
   	var response = confirm(exitMessage);
	if (response == true) { 
		document.pifDataForm.action="/do/contract/pic/plansubmission/";
		document.pifDataForm.submit();
	}	
}

function doPrint(){
     window.open("/do/contract/pic/confirm/?task=print&printFriendly=true","","width=720,height=480,resizable,toolbar,scrollbars,menubar");
}

</script>

<ps:form method="POST" action="/do/contract/pic/confirm/" modelAttribute="pifDataForm" name="pifDataForm">
<input type="hidden" name="contractId"/>
<input type="hidden" name="dirty" id="dirtyFlagId"/>
  <div>
	<c:if test="${not param.printFriendly}">  
	<div>          
		<table border="0" cellpadding="0" cellspacing="5" width="200">
		  <tbody>
			<tr>
			  <td width="100" align="left">
				<input type="button" class="button100Lg"
							 value="exit"
							 property="actionLabel"
							 onclick="return doExit();"/>
			  </td>
			  <td width="100" align="right">
				<input type="button" class="button100Lg"
							 value="print"
							 property="actionLabel"
							 onclick="return doPrint();"/>   
			  </td>
			</tr>
		  </tbody>
		</table>	                   
	</div>	
	</c:if>	
    <div class="messagesBox">
      <%-- Override max height if print friendly is on so we don't scroll --%>
		<ps:messages scope="request" maxHeight="${param.printFriendly ? '1000px' : '100px'}" suppressDuplicateMessages="true" />
    </div>	
	<table>
	   <tr style="height:10px">
		  <td><b>${pifDataForm.planInfoVO.generalInformations.contractName}</b><img src="/assets/unmanaged/images/spacer.gif" width="1">|<img src="/assets/unmanaged/images/spacer.gif" width="3"><b>Contract:</b><img src="/assets/unmanaged/images/spacer.gif"  width="5">${pifDataForm.planInfoVO.generalInformations.contractNumber}<img src="/assets/unmanaged/images/spacer.gif" width="1">|<img src="/assets/unmanaged/images/spacer.gif" width="3"><b>Submission ID:</b><img src="/assets/unmanaged/images/spacer.gif"  width="5">${pifDataForm.submissionId}</td>
	   </tr>
	</table>	
	<jsp:include flush="true" page="general.jsp"></jsp:include>
    <jsp:include flush="true" page="money.jsp"></jsp:include>
    <jsp:include flush="true" page="eligibility.jsp"></jsp:include>
    <jsp:include flush="true" page="contributions.jsp"></jsp:include>
    <jsp:include flush="true" page="vesting.jsp"></jsp:include>
    <jsp:include flush="true" page="forfeitures.jsp"></jsp:include>
    <jsp:include flush="true" page="withdrawals.jsp"></jsp:include>
    <jsp:include flush="true" page="loans.jsp"></jsp:include>
    <jsp:include flush="true" page="other.jsp"></jsp:include>	
	<br>
	  <div>
		<table border=0 cellSpacing=0 cellPadding=0 width="100%">
		<tr>
			<td valign="top">
				<script type="text/javascript">
				$(document).ready(function() {
					authorizationIndicatorId= "#planInfoVO_authorizationIndicator";
					$(authorizationIndicatorId).on("click", function() {
					authorizationIndicatorHiddenId = document.getElementById('authorizationIndicator');
						if($(authorizationIndicatorId).is(':checked')){ 
							authorizationIndicatorHiddenId.value='true';
						}else{
							authorizationIndicatorHiddenId.value='false';
						}
					});
				});
				</script>	
				<input type="checkbox" 
					id="planInfoVO_authorizationIndicator" 
					name="planInfoVO.authorizationIndicator" 
					value="true"						
					onclick="setDirtyFlag();"
					disabled = "true"
					<c:if test="${pifDataForm.planInfoVO.authorizationIndicator}">checked="checked" </c:if> />
<input type="hidden" name="planInfoVO.authorizationIndicator" id="authorizationIndicator" /><%--  input - name="pifDataForm" --%>

			</td>	
			<td >
				${pifDataForm.acknowledgmentText}		
			</td>
		</tr>
		</table>
		<br>
		<c:if test="${not param.printFriendly}"> 		
			<table border=0 cellSpacing=0 cellPadding=0 width="100%">
			<tr>
			  <td align="left">
				<input type="button" class="button100Lg"
							 value="exit"
							 property="actionLabel"
							 onclick="return doExit();"/>
				<input type="button" class="button100Lg"
							 value="print"
							 property="actionLabel"
							 onclick="return doPrint();"/>   
			  </td>	
			</tr>
			</table>
		</c:if>
	  </div>   
 </div>   
</ps:form>
<c:if test="${not empty param.printFriendly }" >
	<content:contentBean contentId="<%=ContentConstants.GLOBAL_DISCLOSURE%>"
        type="<%=ContentConstants.TYPE_MISCELLANEOUS%>" id="globalDisclosure"/>        
	<table width="634" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="100%"><content:getAttribute beanName="globalDisclosure" attribute="text"/></td>
		</tr>
	</table>
</c:if>
<jsp:include flush="true" page="editHandlers.jsp"></jsp:include>
<jsp:include flush="true" page="editUpdates.jsp"></jsp:include>
<jsp:include flush="true" page="errorMessages.jsp"></jsp:include>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/input.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/employeeSnapshot.js"></script>
<script language="JavaScript" type="text/javascript" src="/assets/unmanaged/javascript/tooltip.js"></script>
